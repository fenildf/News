package com.official.nanorus.contacts.presentation.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.presentation.presenter.ContactsListPresenter;
import com.official.nanorus.contacts.presentation.ui.adapters.ContactsRecyclerViewAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListFragment extends Fragment {

    @BindView(R.id.rv_contact_list)
    RecyclerView contactsRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContactsRecyclerViewAdapter adapter;

    ContactsListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        ButterKnife.bind(this, view);

        presenter = new ContactsListPresenter();
        presenter.bindView(this);

        initContactList();
        return view;
    }

    public void initContactList(){
        layoutManager = new LinearLayoutManager(contactsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new ContactsRecyclerViewAdapter();
        contactsRecyclerView.setAdapter(adapter);
        contactsRecyclerView.setLayoutManager(layoutManager);
    }

    public void updateContactList(List<Contact> contacts) {
        adapter.updateList(contacts);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.releasePresenter();
        presenter = null;
    }
}
