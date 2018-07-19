package com.official.nanorus.contacts.presentation.presenter;

import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.MainActivity;

public class ContactsPresenter {

    private MainActivity view;

    public ContactsPresenter() {

    }

    public void bindView(MainActivity view){
        this.view = view;
    }

    public void onContactsListMenuItemClicked(){
        view.showContacts();
    }

    public void onAddContactMenuItemClicked(){
        view.showAddContact();
    }

}
