package com.official.nanorus.contacts.presentation.presenter;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.model.repository.ContactsRepository;
import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.AddContactFragment;

public class AddContactPresenter {

    private AddContactFragment view;
    private ContactsInteractor interactor;
    private ResourceManager resourceManager;

    public AddContactPresenter() {
        interactor = ContactsInteractor.getInstance();
        resourceManager = new ResourceManager();
    }

    public void bindView(AddContactFragment view) {
        this.view = view;
    }

    public void onAddButtonClicked(Contact contact) {
        if (contact.getName() == null || contact.getPhone() == null || contact.getName().isEmpty() || contact.getPhone().isEmpty()) {
            Toaster.shortToast("Please fill in the required fields");
        } else {
            interactor.addContact(contact, new DatabaseManager.AddContactListener(){
                @Override
                public void onSuccess() {
                    Toaster.shortToast(resourceManager.getStringAddContactSuccess());
                }

                @Override
                public void onFail() {
                    Toaster.shortToast(resourceManager.getStringAddContactFail());
                }
            });
        }
    }

}
