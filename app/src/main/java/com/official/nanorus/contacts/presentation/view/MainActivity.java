package com.official.nanorus.contacts.presentation.view;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.support.design.widget.NavigationView;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.presentation.presenter.ContactsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frame)
    FrameLayout frameLayout;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    ConstraintLayout navigationHeader;
    ActionBarDrawerToggle drawerToggle;

    ContactsPresenter presenter;
    ContactsListFragment contactsListFragment;
    AddContactFragment addContactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupNavigationDrawer();

        contactsListFragment = new ContactsListFragment();
        addContactFragment = new AddContactFragment();

        presenter = new ContactsPresenter();
        presenter.bindView(this);
    }

    public void showContacts(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.frame, contactsListFragment);
        fragmentTransaction.commit();
    }
    public void showAddContact(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frame, addContactFragment);
        fragmentTransaction.commit();
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

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_contacts:
                    presenter.onContactsListMenuItemClicked();
                    break;
                case R.id.menu_item_add_contact:
                    presenter.onAddContactMenuItemClicked();
                    break;
            }
            drawerLayout.closeDrawers();
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
