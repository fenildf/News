package com.official.nanorus.contacts.presentation.presenter;

import android.provider.ContactsContract;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.presentation.view.ContactsListFragment;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ContactsListPresenter {

    private ContactsInteractor interactor;
    private ContactsListFragment view;
    Observable<List<Contact>> contactObservable;

    public ContactsListPresenter() {
        interactor = ContactsInteractor.getInstance();
        contactObservable = interactor.getContacts().observeOn(AndroidSchedulers.mainThread());
        contactObservable.subscribe(contacts -> {
                    Collections.reverse(contacts);
                    view.updateContactList(contacts);
                }
        );
    }

    public void bindView(ContactsListFragment view) {
        this.view = view;
    }

    public void releasePresenter() {
        view = null;
        interactor = null;
    }
}
