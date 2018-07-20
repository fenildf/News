package com.official.nanorus.contacts.model.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.app.App;
import com.official.nanorus.contacts.presentation.ui.Toaster;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResourceManager {
    private Context context;

    public ResourceManager() {
        this.context = App.getApp().getApplicationContext();
    }

    public String getStringAddContactSuccess() {
        return context.getString(R.string.contact_added);
    }

    public String getStringAddContactFail() {
        return context.getString(R.string.contact_did_not_add);
    }

    public boolean checkWriteSdPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void saveContactPhoto(Bitmap bitmap, String name) {
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + "/ContactsApp/photos/");
        sdCardDirectory.mkdirs();
        File imageFile = new File(sdCardDirectory, name + ".png");
        saveImage(bitmap, imageFile);
    }

    private void saveImage(Bitmap bitmap, File file) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fileOutputStream != null) {
                fileOutputStream.write(bytes.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContactPhotoUri(String name) {
        return Environment.getExternalStorageDirectory() + "/ContactsApp/photos/" + name + ".png";

    }
}
