package com.official.nanorus.contacts.presentation.presenter;

import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.MainActivity;

public class ContactsPresenter {

    private MainActivity view;

    public ContactsPresenter() {

    }

    public void bindView(MainActivity view){
        this.view = view;
        onContactsListMenuItemClicked();
    }

    public void onContactsListMenuItemClicked(){
        view.showContacts();
    }

    public void onAddContactMenuItemClicked(){
        view.showAddContact();
    }

    public void releasePresenter() {
        view = null;
    }
}
