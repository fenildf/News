package com.official.nanorus.contacts.model.repository;

import com.official.nanorus.contacts.entity.data.contact.Contact;
import com.official.nanorus.contacts.model.data.AppPreferencesManager;
import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.SuccessListener;
import com.official.nanorus.contacts.model.data.database.contacts.ContactsDatabaseManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ContactsRepository {

    private ContactsDatabaseManager contactsDatabaseManager;
    private ResourceManager resourceManager;
    private AppPreferencesManager preferencesManager;

    private static ContactsRepository instance;

    public static ContactsRepository getInstance() {
        if (instance == null)
            instance = new ContactsRepository();
        return instance;
    }

    public ContactsRepository() {
        contactsDatabaseManager = ContactsDatabaseManager.getInstance();
        resourceManager = new ResourceManager();
        preferencesManager = AppPreferencesManager.getInstance();
    }

    public Observable<List<Contact>> getContacts() {
        return contactsDatabaseManager.getContacts().subscribeOn(Schedulers.io());
    }

    public void addContact(Contact contact, SuccessListener successListener) {
        contactsDatabaseManager.putContact(contact, successListener);
    }

    public void saveContactPhoto(String image, String photoFileName) {
        resourceManager.saveContactPhoto(image, photoFileName);
    }

    public void setLastMenuItem(int item) {
        preferencesManager.setSelectedMenuItem(item);
    }

    public int getLastMenuItem() {
        return preferencesManager.getSelectedMenuItem();
    }

    public void deleteContact(int id, SuccessListener successListener) {
        contactsDatabaseManager.deleteContact(id, successListener);
    }

    public void clearContacts(SuccessListener successListener) {
        contactsDatabaseManager.clearContacts(successListener);
    }
}
