package com.official.nanorus.news.presentation.view.main;

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
import android.widget.FrameLayout;

import com.official.nanorus.news.R;
import com.official.nanorus.news.entity.data.categories.Category;
import com.official.nanorus.news.model.repository.CategoriesRepository;
import com.official.nanorus.news.presentation.presenter.MainPresenter;
import com.official.nanorus.news.presentation.ui.adapters.CategoriesRecyclerViewAdapter;
import com.official.nanorus.news.presentation.view.categories.CategoriesFragment;
import com.official.nanorus.news.presentation.view.news.NewsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IMainView, NewsFragment.NewsListener, CategoriesRecyclerViewAdapter.CategoryListListener {

    private final String TAG = this.getClass().getSimpleName();

    public static final int FRAGMENT_CATEGORIES = 0;
    public static final String FRAGMENT_CATEGORIES_TAG = "categories";
    public static final int FRAGMENT_NEWS = 1;
    public static final String FRAGMENT_NEWS_TAG = "news";

    public static String ATTACHED_FRAGMENT_KEY = "fragment";

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

    private int selectedMenuItem = 0;
    private int attachedFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            attachedFragment = savedInstanceState.getInt(ATTACHED_FRAGMENT_KEY);
        }

        categoriesFragment = new CategoriesFragment();
        newsFragment = new NewsFragment();
        presenter = new MainPresenter();
        categoriesRepository = new CategoriesRepository();

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
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setDrawerListener(drawerToggle);

        presenter.onNewsCategoriesMenuCreate();

        navigationView.setNavigationItemSelectedListener(item -> {
            selectedMenuItem = FRAGMENT_NEWS;
            presenter.saveMenuState(selectedMenuItem);
            presenter.onNewsCategoryMenuItemClicked(item.getItemId());

            drawerLayout.closeDrawers();
            return false;
        });
    }

    @Override
    public void setMenuNewsCategories(List<Category> categories) {
        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu(R.string.news_categories);
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            String categoryName = category.getName();
            categoryName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1);
            subMenu.add(0, category.getId(), i, categoryName);
        }
    }

    @Override
    public void setSelectedMenuItem(int item) {
        selectedMenuItem = item;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ATTACHED_FRAGMENT_KEY, attachedFragment);
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
    public void onCategoryClicked(Category category) {
        presenter.onCategoryClicked(category);
    }
}
