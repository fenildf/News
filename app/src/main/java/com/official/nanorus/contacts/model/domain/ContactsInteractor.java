package com.official.nanorus.contacts.model.domain;

import android.graphics.Bitmap;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;
import com.official.nanorus.contacts.model.repository.ContactsRepository;
import com.official.nanorus.contacts.presentation.presenter.AddContactPresenter;

import java.util.List;

import rx.Observable;

public class ContactsInteractor {

    private static ContactsInteractor instance;

    public static ContactsInteractor getInstance() {
        if (instance == null)
            instance = new ContactsInteractor();
        return instance;
    }

    private ContactsRepository repository;

    public ContactsInteractor() {
        repository = ContactsRepository.getInstance();
    }

    public Observable<List<Contact>> getContacts() {
        return repository.getContacts();
    }

    public void addContact(Contact contact, DatabaseManager.AddContactListener addContactListener) {
        repository.addContact(contact, addContactListener);
    }

    public void saveContactPhoto(Bitmap image, String photoFileName) {
        repository.saveContactPhoto(image, photoFileName);
    }

    public int getLastMenuItem() {
        return repository.getLastMenuItem();
    }

    public void setLastMenuItem(int item) {
        repository.setLastMenuItem(item);
    }
}
