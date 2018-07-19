package com.official.nanorus.contacts.model.domain;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;
import com.official.nanorus.contacts.model.repository.ContactsRepository;

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

    public Observable<Contact> getContacts() {
        return repository.getContacts();
    }

    public void addContact(Contact contact, DatabaseManager.AddContactListener addContactListener) {
        repository.addContact(contact, addContactListener);
    }
}
