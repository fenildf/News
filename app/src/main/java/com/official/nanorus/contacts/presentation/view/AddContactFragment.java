package com.official.nanorus.contacts.presentation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.presentation.presenter.AddContactPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactFragment extends Fragment {

    @BindView(R.id.et_name)
    TextView nameTextView;
    @BindView(R.id.et_surname)
    TextView surnameTextView;
    @BindView(R.id.et_patronymic)
    TextView patronymicTextView;
    @BindView(R.id.et_phone)
    TextView phoneTextView;
    @BindView(R.id.et_email)
    TextView emailTextView;

    AddContactPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, null);
        ButterKnife.bind(this, view);
        presenter = new AddContactPresenter();
        presenter.bindView(this);
        return view;
    }

    @OnClick(R.id.btn_add)
    public void onAddClick(){
        String name = nameTextView.getText().toString();
        String surname = surnameTextView.getText().toString();
        String patronymic = patronymicTextView.getText().toString();
        String phone = phoneTextView.getText().toString();
        String email = emailTextView.getText().toString();

        presenter.onAddButtonClicked(new Contact(name, surname, patronymic, phone, email ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.releasePresenter();
        presenter = null;
    }

    public void clearFields() {
        nameTextView.setText("");
        surnameTextView.setText("");
        patronymicTextView.setText("");
        phoneTextView.setText("");
        emailTextView.setText("");
    }
}
