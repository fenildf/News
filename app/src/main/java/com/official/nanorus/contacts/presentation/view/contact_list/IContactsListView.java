package com.official.nanorus.contacts.presentation.view.contact_list;

import com.official.nanorus.contacts.entity.data.contact.Contact;

import java.util.List;

public interface IContactsListView {
    void addContact();

    void clearContactList();

    void updateContactList(List<Contact> contacts);
}
