package com.official.nanorus.contacts.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.data.contact.Contact;
import com.official.nanorus.contacts.model.data.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactsViewHolder> {

    public final String TAG = this.getClass().getSimpleName();

    private List<Contact> dataList;
    private ResourceManager resourceManager;
    private Context context;

    public ContactsRecyclerViewAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
        resourceManager = new ResourceManager();
    }

    public void clearList() {
        if (dataList != null)
            dataList.clear();
    }
    public void updateList(List<Contact> list) {
        dataList.addAll(0, list);
    }

    public interface ContactsListListener {
        void onContactLongClicked(int id, String name);
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
        String photoUri = resourceManager.getContactPhotoUri(dataList.get(position).getImage());
        Glide.with(holder.itemView.getContext())
                .load(photoUri)
                .apply(RequestOptions.bitmapTransform(new CircleCrop())
                        .placeholder(R.drawable.ic_no_photo)
                )
                .into(holder.photoImageView);

        holder.itemView.setOnLongClickListener(view -> {
            try {
                ((ContactsListListener) context)
                        .onContactLongClicked(dataList.get(position).getId(), dataList.get(position).getName());
            } catch (ClassCastException cce) {
                Log.d(TAG, cce.getMessage());
            }
            return false;
        });
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
