package com.official.nanorus.contacts.presentation.presenter;

import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.SuccessListener;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.MainActivity;

public class MainPresenter {

    private MainActivity view;
    private ContactsInteractor interactor;
    private ResourceManager resourceManager;

    public MainPresenter() {
        interactor = ContactsInteractor.getInstance();
        resourceManager = new ResourceManager();
    }

    public void bindView(MainActivity view) {
        this.view = view;
        view.setSelectedMenuItem(interactor.getLastMenuItem());
        if (interactor.getLastMenuItem() == MainActivity.FRAGMENT_CONTACTS_LIST)
            onContactsListMenuItemClicked();
        else if (interactor.getLastMenuItem() == MainActivity.FRAGMENT_ADD_CONTACT)
            onAddContactMenuItemClicked();
        else if (interactor.getLastMenuItem() == MainActivity.FRAGMENT_NEWS)
            onNewsMenuItemClicked();
    }

    public void onContactsListMenuItemClicked() {
        view.showContacts();
    }

    public void onAddContactMenuItemClicked() {
        view.showAddContact();
    }

    public void onNewsMenuItemClicked() {
        view.showNews();
    }

    public void releasePresenter() {
        view = null;
    }

    public void saveMenuState(int item) {
        interactor.setLastMenuItem(item);
    }

    public void onContactSelectedAction(int id) {
        SuccessListener successListener = new SuccessListener() {
            @Override
            public void onSuccess() {
                Toaster.shortToast(resourceManager.getStringContactDeleted());
            }

            @Override
            public void onFail() {
                Toaster.shortToast(resourceManager.getStringContactNotDeleted());
            }
        };
        interactor.deleteContact(id, successListener);
        view.refreshContacts();
    }

    public void onContactSelected(int id, String name) {
        view.showDeleteContactDialog(id, name);
    }

    public void onClearContactsAction() {
        SuccessListener successListener = new SuccessListener() {
            @Override
            public void onSuccess() {
                Toaster.shortToast(resourceManager.getStringContactsCleared());
            }

            @Override
            public void onFail() {
                Toaster.shortToast(resourceManager.getStringContactsNotCleared());
            }
        };
        interactor.clearContacts(successListener);
        view.refreshContacts();
    }

    public void onClearContactsClicked() {
        view.showClearContactsDialog();
    }

    public void onSearchNewsClicked() {
        view.showSearchNewsDialog();
    }
}
