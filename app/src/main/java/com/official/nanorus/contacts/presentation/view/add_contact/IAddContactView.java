package com.official.nanorus.contacts.presentation.view.add_contact;

import com.official.nanorus.contacts.entity.data.contact.Contact;

public interface IAddContactView {
    void requestWriteSdPermission();

    void clearFields();

    void resetImage();

    void setImage(String imageUri);

    void chooseImage();

    void setFields(Contact contact);
}
