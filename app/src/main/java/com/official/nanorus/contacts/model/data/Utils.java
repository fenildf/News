package com.official.nanorus.contacts.model.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.official.nanorus.contacts.app.App;

import java.util.UUID;

public class Utils {
    private static String TAG = Utils.class.getSimpleName();

    public static boolean isSdCardAvailable() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        return isSDSupportedDevice && isSDPresent;
    }

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(3, 13);

    }

    public static String getRealPathFromURI( Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = App.getApp().getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
