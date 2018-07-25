package com.official.nanorus.contacts.presentation.presenter;

import android.util.Log;

import com.official.nanorus.contacts.entity.data.contact.Contact;
import com.official.nanorus.contacts.model.domain.ContactsInteractor;
import com.official.nanorus.contacts.presentation.view.ContactsListFragment;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ContactsListPresenter {

    public final String TAG = this.getClass().getSimpleName();

    private ContactsInteractor interactor;
    private ContactsListFragment view;
    private Observable<List<Contact>> contactsObservable;
    private Disposable contactsDisponsable;

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
        if (contactsDisponsable != null && !contactsDisponsable.isDisposed()) {
            contactsDisponsable.dispose();
        }
        contactsDisponsable = contactsObservable.subscribe(contacts -> {
                    Collections.reverse(contacts);
                    view.updateContactList(contacts);
                },
                throwable -> Log.d(TAG, throwable.getMessage())
                );
    }
}
