package com.official.nanorus.contacts.presentation.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.entity.contact.Contact;
import com.official.nanorus.contacts.presentation.presenter.AddContactPresenter;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
    @BindView(R.id.iv_photo)
    ImageView photoImageView;

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
    public void onAddClick() {
        String name = nameTextView.getText().toString();
        String surname = surnameTextView.getText().toString();
        String patronymic = patronymicTextView.getText().toString();
        String phone = phoneTextView.getText().toString();
        String email = emailTextView.getText().toString();

        presenter.onAddButtonClicked(new Contact(name, surname, patronymic, phone, email, null));
    }

    @OnClick(R.id.iv_photo)
    public void onImageClick() {
        presenter.onImageClick();
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

    public void chooseImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, 100);
    }

    public void setImage(Bitmap image) {
        Glide.with(this).load(image)
                .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.ic_launcher_background))
                .into(photoImageView);
    }

    public void onWriteDbPermissionResult(boolean granded){
        presenter.onRequestWriteSdPermissionResult(granded);
    }

    public void requestWriteSdPermission() {
        if (getActivity() != null)
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MainActivity.MY_PERMISSIONS_REQUEST_WRITE_SD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Rect rect = new Rect();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outHeight = 60;
                    options.outWidth = 60;
                    options.inSampleSize = 4;
                    Bitmap image = BitmapFactory.decodeStream(imageStream, rect, options);

                    presenter.onImageChosen(image);
                }
                break;
        }
    }
}
