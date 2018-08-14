package com.official.nanorus.contacts.model.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.official.nanorus.contacts.R;
import com.official.nanorus.contacts.app.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void saveContactPhoto(String image, String name) {
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory() + "/ContactsApp/photos/");
        sdCardDirectory.mkdirs();
        File imageFile = new File(sdCardDirectory, name + ".png");
        saveImage(image, imageFile);
    }

    private void saveImage(String image, File file) {
        File src = new File(image);
        try {
            file.createNewFile();
            copy(src, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst, false);
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public String getContactPhotoUri(String name) {
        return Environment.getExternalStorageDirectory() + "/ContactsApp/photos/" + name + ".png";

    }

    public void saveImageToCache(String image) {
        File cacheDir = new File(getCacheDir(), "pic");
        cacheDir.mkdirs();
        File f = new File(cacheDir, "addContact.png");
        File src = new File(image);
        try {
            if (!src.getAbsolutePath().equals(f.getAbsolutePath())) {
                f.createNewFile();
                copy(src, f);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageFromCache() {
        File cacheDir = new File(getCacheDir(), "pic");
        cacheDir.mkdirs();
        File f = new File(cacheDir, "addContact.png");
        return f.getAbsolutePath();
    }

    public File getCacheDir() {
        return context.getCacheDir();
    }

    public void clearImageCache() {
        File cacheDir = new File(getCacheDir(), "pic");
        File f = new File(cacheDir, "addContact.png");
        f.delete();
    }

    public String getStringContactDeleted() {
        return context.getString(R.string.contact_deleted);
    }

    public String getStringContactNotDeleted() {
        return context.getString(R.string.contact_not_deleted);
    }

    public String getStringContactsCleared() {
        return context.getString(R.string.contacts_cleared);
    }

    public String getStringContactsNotCleared() {
        return context.getString(R.string.contacts_not_cleared);
    }

    public String getStringNews(){
        return context.getString(R.string.news);
    }

    public String getStringNoInternet(){
        return context.getString(R.string.no_internet);
    }
}
