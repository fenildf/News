package com.official.nanorus.contacts.presentation.presenter;

import android.graphics.Bitmap;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.Utils;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.model.repository.ContactsRepository;
import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.AddContactFragment;

public class AddContactPresenter {

    private AddContactFragment view;
    private ContactsInteractor interactor;
    private ResourceManager resourceManager;
    private Bitmap image;
    private String photoFileName;

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
            this.photoFileName = Utils.generateRandomString();
            contact.setImage(photoFileName);
            interactor.addContact(contact, new DatabaseManager.AddContactListener() {
                @Override
                public void onSuccess() {
                    Toaster.shortToast(resourceManager.getStringAddContactSuccess());
                    view.clearFields();
                    if (resourceManager.checkWriteSdPermission())
                        interactor.saveContactPhoto(AddContactPresenter.this.image, AddContactPresenter.this.photoFileName);
                    else
                        view.requestWriteSdPermission();
                }

                @Override
                public void onFail() {
                    Toaster.shortToast(resourceManager.getStringAddContactFail());
                }
            });
        }
    }


    public void onRequestWriteSdPermissionResult(boolean granded) {
        if (granded)
            interactor.saveContactPhoto(this.image, this.photoFileName);
        else
            view.requestWriteSdPermission();
    }

    public void onImageChosen(Bitmap image) {
        this.image = image;
        view.setImage(image);
    }

    public void releasePresenter() {
        view = null;
        interactor = null;
        resourceManager = null;
    }

    public void onImageClick() {
        view.chooseImage();
    }
}
