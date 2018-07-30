package com.official.nanorus.contacts.presentation.view;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.widget.FrameLayout;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.presentation.presenter.ContactsPresenter;
import com.official.nanorus.contacts.presentation.ui.adapters.ContactsRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity implements ContactsListFragment.ContactListListener, ContactsRecyclerViewAdapter.ContactsListListener {

    private final String TAG = this.getClass().getSimpleName();

    public static final int MY_PERMISSIONS_REQUEST_WRITE_SD = 1;

    public static int FRAGMENT_CONTACTS_LIST = 0;
    public static int FRAGMENT_ADD_CONTACT = 1;
    public static String FRAGMENT_CONTACTS_LIST_TAG = "fragment_contacts";
    public static String FRAGMENT_ADD_CONTACT_TAG = "fragment_add_contact";
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
    MenuItem deleteMenuItem;

    ContactsPresenter presenter;
    ContactsListFragment contactsListFragment;
    AddContactFragment addContactFragment;

    private int selectedMenuItem = 0;
    private int attachedFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null)
            attachedFragment = savedInstanceState.getInt(ATTACHED_FRAGMENT_KEY);

        contactsListFragment = new ContactsListFragment();
        addContactFragment = new AddContactFragment();


        setupNavigationDrawer();

        presenter = new ContactsPresenter();
        presenter.bindView(this);
    }

    public void showContacts() {
        if (attachedFragment != FRAGMENT_CONTACTS_LIST) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            fragmentTransaction.replace(R.id.frame, contactsListFragment, FRAGMENT_CONTACTS_LIST_TAG);
            fragmentTransaction.commit();
            attachedFragment = FRAGMENT_CONTACTS_LIST;
            updateUI();
        } else {
            contactsListFragment = (ContactsListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_CONTACTS_LIST_TAG);
        }
    }

    public void showAddContact() {
        if (attachedFragment != FRAGMENT_ADD_CONTACT) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.replace(R.id.frame, addContactFragment, FRAGMENT_ADD_CONTACT_TAG);
            fragmentTransaction.commit();
            attachedFragment = FRAGMENT_ADD_CONTACT;
            updateUI();
        } else {
            addContactFragment = (AddContactFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_ADD_CONTACT_TAG);
        }
    }

    public void updateUI() {
        if (attachedFragment == FRAGMENT_CONTACTS_LIST) {
            if (deleteMenuItem != null)
                deleteMenuItem.setVisible(true);
        } else if (attachedFragment == FRAGMENT_ADD_CONTACT) {
            if (deleteMenuItem != null)
                deleteMenuItem.setVisible(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.clear_contacts:
                presenter.onClearContactsClicked();
                break;
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
                    selectedMenuItem = FRAGMENT_CONTACTS_LIST;
                    break;
                case R.id.menu_item_add_contact:
                    presenter.onAddContactMenuItemClicked();
                    selectedMenuItem = FRAGMENT_ADD_CONTACT;
                    break;
            }
            drawerLayout.closeDrawers();
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_SD: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addContactFragment.onWriteDbPermissionResult(true);
                } else {
                    addContactFragment.onWriteDbPermissionResult(false);
                }
                break;
            }
        }
    }

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
        presenter.saveMenuState(selectedMenuItem);
        presenter.releasePresenter();
        presenter = null;
    }

    @Override
    public void addContact() {
        setSelectedMenuItem(FRAGMENT_ADD_CONTACT);
        presenter.saveMenuState(selectedMenuItem);
        presenter.onAddContactMenuItemClicked();
    }

    public void showDeleteContactDialog(int id, String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_delete_contact) + " " + name + "?")
                .setPositiveButton(R.string.delete, (dialog, id12) -> presenter.onContactSelectedAction(id))
                .setNegativeButton(R.string.cancel, (dialog, id1) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onContactLongClicked(int id, String name) {
        presenter.onContactSelected(id, name);
    }

    public void showClearContactsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_clear_contacts))
                .setPositiveButton(R.string.delete, (dialog, id12) -> presenter.onClearContactsAction())
                .setNegativeButton(R.string.cancel, (dialog, id1) -> dialog.dismiss());
        builder.show();
    }

    public void refreshContacts() {
        if (contactsListFragment != null)
            contactsListFragment.refreshContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_toolbar_menu, menu);
        deleteMenuItem = menu.findItem(R.id.clear_contacts);
        updateUI();
        return super.onCreateOptionsMenu(menu);
    }


}
