package com.official.nanorus.contacts.presentation.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.contact.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactsViewHolder> {

    private List<Contact> dataList;

    public ContactsRecyclerViewAdapter() {
        dataList = new ArrayList<>();
    }

    public void updateList(List<Contact> list) {
        dataList.addAll(0, list);
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contact contact = dataList.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.surnameTextView.setText(contact.getSurname());
        holder.patronymicTextView.setText(contact.getPatronymic());
        holder.phoneTextView.setText(contact.getPhone());
        holder.emailTextView.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView surnameTextView;
        TextView patronymicTextView;
        TextView emailTextView;
        TextView phoneTextView;
        ImageView photoImageView;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            surnameTextView = itemView.findViewById(R.id.tv_surname);
            patronymicTextView = itemView.findViewById(R.id.tv_patronymic);
            emailTextView = itemView.findViewById(R.id.tv_email);
            phoneTextView = itemView.findViewById(R.id.tv_phone);
            photoImageView = itemView.findViewById(R.id.iv_photo);
        }
    }

}
