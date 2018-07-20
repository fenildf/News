package com.official.nanorus.contacts.model.data;

import android.os.Environment;

import java.util.UUID;

public class Utils {

    public static boolean isSdCardAvailable() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        return isSDSupportedDevice && isSDPresent;
    }

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(3, 13);

    }

}
