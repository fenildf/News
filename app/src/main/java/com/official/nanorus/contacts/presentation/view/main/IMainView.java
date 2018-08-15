package com.official.nanorus.contacts.presentation.view.main;

public interface IMainView {
    void setSelectedMenuItem(int lastMenuItem);

    void showContacts();

    void showAddContact();

    void showNews();

    void refreshContacts();

    void showDeleteContactDialog(int id, String name);

    void showClearContactsDialog();

    void showSearchNewsDialog();
}
