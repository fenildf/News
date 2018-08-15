package com.official.nanorus.contacts.presentation.view.add_contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.data.contact.Contact;
import com.official.nanorus.contacts.model.data.Utils;
import com.official.nanorus.contacts.presentation.presenter.AddContactPresenter;
import com.official.nanorus.contacts.presentation.view.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactFragment extends Fragment implements IAddContactView {

    public static final String SAVE_INSTANCE_CONTACT = "contact";
    private static final int REQUEST_CODE_IMAGE = 100;

    public final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.et_name)
    EditText nameEditText;
    @BindView(R.id.et_surname)
    EditText surnameEditText;
    @BindView(R.id.et_patronymic)
    EditText patronymicEditText;
    @BindView(R.id.et_phone)
    EditText phoneEditText;
    @BindView(R.id.et_email)
    EditText emailEditText;
    @BindView(R.id.iv_photo)
    ImageView photoImageView;
    @BindView(R.id.btn_delete)
    Button deleteImageButton;

    AddContactPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_add_contact, null);
        ButterKnife.bind(this, view);
        presenter = new AddContactPresenter();
        presenter.bindView(this);

        if (savedInstanceState != null) {
            Contact contact = (Contact) savedInstanceState.getSerializable(SAVE_INSTANCE_CONTACT);
            presenter.onRotate(contact);
        } else {
            presenter.onCreate();
        }
        return view;
    }


    @OnClick(R.id.btn_add)
    public void onAddClick() {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String patronymic = patronymicEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        presenter.onAddButtonClicked(new Contact(name, surname, patronymic, phone, email, null));
    }

    @OnClick(R.id.iv_photo)
    public void onImageClick() {
        presenter.onImageClick();
    }

    @Override
    public void setFields(Contact contact) {
        nameEditText.setText(contact.getName());
        surnameEditText.setText(contact.getSurname());
        patronymicEditText.setText(contact.getPatronymic());
        emailEditText.setText(contact.getEmail());
        phoneEditText.setText(contact.getPhone());
    }

    @Override
    public void showDeleteImageButton(boolean show) {
        if (show)
            deleteImageButton.setVisibility(View.VISIBLE);
        else
            deleteImageButton.setVisibility(View.GONE);
    }

    @Override
    public void showImage(boolean show) {
        if (show) {
            photoImageView.setVisibility(View.VISIBLE);
        } else {
            photoImageView.setVisibility(View.GONE);
            showDeleteImageButton(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResumeView();
    }

    @Override
    public void clearFields() {
        nameEditText.setText("");
        surnameEditText.setText("");
        patronymicEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
    }

    @Override
    public void chooseImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, REQUEST_CODE_IMAGE);
    }

    @Override
    public void setImage(String image) {
        showDeleteImageButton(true);
        Glide.with(this).load(image)
                .apply(RequestOptions.bitmapTransform(new CircleCrop())
                        .placeholder(R.drawable.ic_no_photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .into(photoImageView);
    }

    @Override
    public void resetImage() {
        Glide.with(this).load(R.drawable.ic_no_photo)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(photoImageView);
        showDeleteImageButton(false);
    }

    public void onWriteDbPermissionResult(boolean granded) {
        presenter.onRequestWriteSdPermissionResult(granded);
    }

    @Override
    public void requestWriteSdPermission() {
        if (getActivity() != null)
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.MY_PERMISSIONS_REQUEST_WRITE_SD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        presenter.onImageChosen(Utils.getRealPathFromURI(selectedImage));
                    }
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        presenter.releasePresenter();
        presenter = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        Contact contact = new Contact(nameEditText.getText().toString(), surnameEditText.getText().toString(),
                patronymicEditText.getText().toString(), phoneEditText.getText().toString(), emailEditText.getText().toString(), null
        );
        outState.putSerializable(SAVE_INSTANCE_CONTACT, contact);
        presenter.saveImage();
    }

    @OnClick(R.id.btn_delete)
    public void onDeleteImageClicked() {
        presenter.onDeleteImageClicked();
    }
}
