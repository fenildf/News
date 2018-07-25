package com.official.nanorus.contacts.presentation.presenter;

import android.util.Log;

import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.presentation.view.ContactsListFragment;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ContactsListPresenter {

    public final String TAG = this.getClass().getSimpleName();

    private ContactsInteractor interactor;
    private ContactsListFragment view;
    private Observable<List<Contact>> contactsObservable;
    private Subscription contactsSubscription;

    public ContactsListPresenter() {
        interactor = ContactsInteractor.getInstance();
        contactsObservable = interactor.getContacts().observeOn(AndroidSchedulers.mainThread());
    }

    public void bindView(ContactsListFragment view) {
        this.view = view;
        refreshContacts();
    }


    public void releasePresenter() {
        view = null;
        interactor = null;
    }

    public void onFabClicked() {
        view.addContact();
    }

    public void refreshContacts() {
        Log.d(TAG, "refreshContacts()");
        view.clearContactList();
        if (contactsSubscription != null && !contactsSubscription.isUnsubscribed()) {
            contactsSubscription.unsubscribe();
        }
        contactsSubscription = contactsObservable.subscribe(contacts -> {
                    Collections.reverse(contacts);
                    view.updateContactList(contacts);
                },
                throwable -> Log.d(TAG, throwable.getMessage())
                );
    }
}
