package com.official.nanorus.contacts.presentation.presenter;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.Utils;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.presentation.ui.Toaster;
import com.official.nanorus.contacts.presentation.view.AddContactFragment;

public class AddContactPresenter {

    private AddContactFragment view;
    private ContactsInteractor interactor;
    private ResourceManager resourceManager;
    private String image;
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
            interactor.addContact(contact, new DatabaseManager.SuccessListener() {
                @Override
                public void onSuccess() {
                    if (AddContactPresenter.this.image != null) {
                        if (resourceManager.checkWriteSdPermission()) {
                            interactor.saveContactPhoto(AddContactPresenter.this.image, AddContactPresenter.this.photoFileName);
                            resetImage();
                        } else
                            view.requestWriteSdPermission();
                    }
                    view.clearFields();
                    Toaster.shortToast(resourceManager.getStringAddContactSuccess());
                }

                @Override
                public void onFail() {
                    Toaster.shortToast(resourceManager.getStringAddContactFail());
                }
            });
        }
    }


    private void resetImage() {
        view.resetImage();
        this.image = null;
    }

    public void onRequestWriteSdPermissionResult(boolean granded) {
        if (this.image != null) {
            if (granded) {
                interactor.saveContactPhoto(this.image, this.photoFileName);
                resetImage();
            } else
                view.requestWriteSdPermission();
        }
    }

    public void onImageChosen(String imageUri) {
        if (imageUri != null) {
            this.image = imageUri;
            view.setImage(imageUri);
        }
    }

    public void releasePresenter() {
        resourceManager.clearImageCache();
        view = null;
        interactor = null;
        resourceManager = null;
    }

    public void onImageClick() {
        view.chooseImage();
    }

    public void saveImage() {
        if (this.image != null) {
            resourceManager.saveImageToCache(this.image);
        }
    }

    private void restoreState(Contact contact) {
        String cachedImage = resourceManager.getImageFromCache();
        if (cachedImage != null) {
            this.image = cachedImage;
            view.setImage(this.image);
        }
        view.setFields(contact);
    }

    public void onRotate(Contact contact) {
        restoreState(contact);
    }

}
