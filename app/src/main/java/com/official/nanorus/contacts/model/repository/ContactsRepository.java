package com.official.nanorus.contacts.model.repository;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;

import rx.Observable;
import rx.schedulers.Schedulers;

public class ContactsRepository {

    private DatabaseManager databaseManager;

    private static ContactsRepository instance;

    public static ContactsRepository getInstance() {
        if (instance == null)
            instance = new ContactsRepository();
        return instance;
    }

    public ContactsRepository() {
        databaseManager = DatabaseManager.getInstance();
    }

    public Observable<Contact> getContacts() {
        return databaseManager.getContacts().map(contact -> {
            contact.setImage(null);
            return contact;
        }).subscribeOn(Schedulers.io());
    }

    public void addContact(Contact contact, DatabaseManager.AddContactListener addContactListener) {
        databaseManager.putContact(contact, addContactListener);
    }

}
