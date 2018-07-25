package com.official.nanorus.contacts.presentation.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.presentation.presenter.ContactsListPresenter;
import com.official.nanorus.contacts.presentation.ui.adapters.ContactsRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsListFragment extends Fragment  {

    public final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_contact_list)
    RecyclerView contactsRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContactsRecyclerViewAdapter adapter;

    ContactsListPresenter presenter;

    public interface ContactListListener {
        void addContact();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        ButterKnife.bind(this, view);

        initContactList();

        presenter = new ContactsListPresenter();
        presenter.bindView(this);
        return view;
    }

    public void initContactList() {
        layoutManager = new LinearLayoutManager(contactsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new ContactsRecyclerViewAdapter(this.getContext());
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(layoutManager);
    }

    public void clearContactList(){
        adapter.clearList();
        adapter.notifyDataSetChanged();
    }
    public void updateContactList(List<Contact> contacts) {
        adapter.updateList(contacts);
        adapter.notifyDataSetChanged();
    }

    public void refreshContacts(){
        presenter.refreshContacts();
    }

    public void addContact() {
        try {
            ((ContactListListener) getActivity()).addContact();
        } catch (ClassCastException cce) {
            Log.d(TAG, cce.getMessage());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        presenter.releasePresenter();
        presenter = null;
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        presenter.onFabClicked();
    }
}
