package com.official.nanorus.news.presentation.view.main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.data.TextUtils;
import com.official.nanorus.news.model.repository.CategoriesRepository;
import com.official.nanorus.news.presentation.presenter.MainPresenter;
import com.official.nanorus.news.presentation.view.categories.CategoriesFragment;
import com.official.nanorus.news.presentation.view.news.NewsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainView, NewsFragment.NewsListener, CategoriesFragment.CategoriesListener {

    private final String TAG = this.getClass().getSimpleName();

    public static final int FRAGMENT_CATEGORIES = 0;
    public static final String FRAGMENT_CATEGORIES_TAG = "categories";
    public static final int FRAGMENT_NEWS = 1;
    public static final String FRAGMENT_NEWS_TAG = "news";
    public static final int MENU_ITEM_MAIN = 0;
    public static final int MENU_ITEM_CATEGORIES = 1;
    public static final int MENU_ITEM_SETTINGS = 20;

    public static String ATTACHED_FRAGMENT_KEY = "fragment";
    public static String SELECTED_MENU_ITEM_KEY = "menu_item";
    public static String SELECTED_CATEGORY_KEY = "category";

    @BindView(R.id.frame)
    FrameLayout frameLayout;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    ConstraintLayout navigationHeader;
    ActionBarDrawerToggle drawerToggle;

    MainPresenter presenter;
    CategoriesRepository categoriesRepository;

    CategoriesFragment categoriesFragment;
    NewsFragment newsFragment;

    private int selectedMenuItem = -1;
    private int selectedCategory = -1;
    private int attachedFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            attachedFragment = savedInstanceState.getInt(ATTACHED_FRAGMENT_KEY);
            selectedMenuItem = savedInstanceState.getInt(SELECTED_MENU_ITEM_KEY);
            selectedCategory = savedInstanceState.getInt(SELECTED_CATEGORY_KEY);
        }

        categoriesFragment = new CategoriesFragment();
        newsFragment = new NewsFragment();
        presenter = new MainPresenter();
        categoriesRepository = new CategoriesRepository();

        presenter.bindView(this);
        presenter.startWork(selectedMenuItem, selectedCategory);

        setupNavigationDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void showCategories() {
        if (attachedFragment != FRAGMENT_CATEGORIES) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.replace(R.id.frame, categoriesFragment, FRAGMENT_CATEGORIES_TAG);
            fragmentTransaction.commit();
            attachedFragment = FRAGMENT_CATEGORIES;
        } else {
            categoriesFragment = (CategoriesFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_CATEGORIES_TAG);
        }
    }

    @Override
    public void showNews() {
        if (attachedFragment != FRAGMENT_NEWS) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.frame, newsFragment, FRAGMENT_NEWS_TAG);
            fragmentTransaction.commit();
            attachedFragment = FRAGMENT_NEWS;
        } else {
            newsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_NEWS_TAG);
            newsFragment.setSearchQuery("");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupNavigationDrawer() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationHeader = (ConstraintLayout) navigationView.getHeaderView(0);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
                hideKeyboard();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
                hideKeyboard();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(item -> {
            presenter.saveMenuState(selectedMenuItem);
            switch (item.getItemId()) {
                case MENU_ITEM_MAIN:
                    selectedMenuItem = MENU_ITEM_MAIN;
                    presenter.onMainMenuItemClicked();
                    break;
                case MENU_ITEM_SETTINGS:
                    presenter.onSettingsMenuItemClicked();
                    break;
                default:
                    selectedMenuItem = MENU_ITEM_CATEGORIES;
                    selectedCategory = item.getItemId();
                    presenter.onNewsCategoryMenuItemClicked(item.getItemId());
                    break;
            }
            drawerLayout.closeDrawers();
            return false;
        });
    }

    public void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void setupNavigationMenu(List<Category> categories) {
        final Menu menu = navigationView.getMenu();
        menu.add(0, MENU_ITEM_MAIN, 0, R.string.main_page);
        final SubMenu subMenu = menu.addSubMenu(R.string.news_categories);
        for (int i = 1; i < categories.size(); i++) {
            Category category = categories.get(i);
            String categoryName = category.getName();
            categoryName = TextUtils.uppercaseFirstCharacter(categoryName);
            subMenu.add(0, category.getId(), i, categoryName);
        }
        menu.add(MENU_ITEM_SETTINGS, MENU_ITEM_SETTINGS, 0, R.string.settings);
    }

    @Override
    public void setToolbarButtonHamburger() {

    }

    @Override
    public void setToolbarButtonArray() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setSelectedMenuItem(int item) {
        selectedMenuItem = item;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ATTACHED_FRAGMENT_KEY, attachedFragment);
        outState.putInt(SELECTED_MENU_ITEM_KEY, selectedMenuItem);
        outState.putInt(SELECTED_CATEGORY_KEY, selectedCategory);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.releasePresenter();
        presenter = null;
    }

    @Override
    public void getNews() {
        if (newsFragment != null)
            newsFragment.getNews();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setSelectedFragment(Category category) {
        selectedMenuItem = MENU_ITEM_CATEGORIES;
        selectedCategory = category.getId();
    }

}
