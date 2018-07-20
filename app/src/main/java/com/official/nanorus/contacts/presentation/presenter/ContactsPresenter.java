package com.official.nanorus.contacts.presentation.presenter;

import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.presentation.view.MainActivity;

public class ContactsPresenter {

    private MainActivity view;
    private ContactsInteractor interactor;

    public ContactsPresenter() {
        interactor = ContactsInteractor.getInstance();
    }

    public void bindView(MainActivity view) {
        this.view = view;
        view.setSelectedMenuItem(interactor.getLastMenuItem());
        if (interactor.getLastMenuItem() == MainActivity.FRAGMENT_CONTACTS_LIST) {
            onContactsListMenuItemClicked();
        } else if (interactor.getLastMenuItem() == MainActivity.FRAGMENT_ADD_CONTACT)
            onAddContactMenuItemClicked();
    }

    public void onContactsListMenuItemClicked() {
        view.showContacts();
    }

    public void onAddContactMenuItemClicked() {
        view.showAddContact();
    }

    public void releasePresenter() {
        view = null;
    }

    public void saveMenuState(int item) {
        interactor.setLastMenuItem(item);
    }
}
