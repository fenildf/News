package com.official.nanorus.contacts.model.repository;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;

import java.util.List;

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

    public Observable<List<Contact>> getContacts() {
        return databaseManager.getContacts().map(contactList -> {
            for (Contact contact : contactList) {
                contact.setImage(null);
            }
            return contactList;
        }).subscribeOn(Schedulers.io());
    }

    public void addContact(Contact contact, DatabaseManager.AddContactListener addContactListener) {
        databaseManager.putContact(contact, addContactListener);
    }

}
