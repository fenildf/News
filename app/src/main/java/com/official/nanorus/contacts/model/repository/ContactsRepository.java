package com.official.nanorus.contacts.model.repository;

import com.official.nanorus.contacts.entity.data.contact.Contact;
import com.official.nanorus.contacts.model.data.AppPreferencesManager;
import com.official.nanorus.contacts.model.data.ResourceManager;
import com.official.nanorus.contacts.model.data.database.DatabaseManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ContactsRepository {

    private DatabaseManager databaseManager;
    private ResourceManager resourceManager;
    private AppPreferencesManager preferencesManager;

    private static ContactsRepository instance;

    public static ContactsRepository getInstance() {
        if (instance == null)
            instance = new ContactsRepository();
        return instance;
    }

    public ContactsRepository() {
        databaseManager = DatabaseManager.getInstance();
        resourceManager = new ResourceManager();
        preferencesManager = AppPreferencesManager.getInstance();
    }

    public Observable<List<Contact>> getContacts() {
        return databaseManager.getContacts().subscribeOn(Schedulers.io());
    }

    public void addContact(Contact contact, DatabaseManager.SuccessListener successListener) {
        databaseManager.putContact(contact, successListener);
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

    public void deleteContact(int id, DatabaseManager.SuccessListener successListener) {
        databaseManager.deleteContact(id, successListener);
    }

    public void clearContacts(DatabaseManager.SuccessListener successListener) {
        databaseManager.clearContacts(successListener);
    }
}
