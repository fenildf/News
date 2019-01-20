package com.official.nanorus.news.model.data;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;

import com.official.nanorus.news.app.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
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

    public static String getRealPathFromURI(Uri contentUri) {
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

    public static float pxToDp(int dp) {
        Resources r = App.getApp().getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static boolean checkNetworkError(Throwable throwable) {
        return throwable.toString().contains("Unable to resolve host");
    }

    public static String mapDateFromApi(String apiDate) {
        String clearApiDate = apiDate.replace("T", ",")
                .replace("Z", ";");
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss;", Locale.ENGLISH);
        SimpleDateFormat myFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm", Locale.ENGLISH);
        String uiDate = "";
        try {
            uiDate = myFormat.format(fromUser.parse(clearApiDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return uiDate;
    }

    public static String getAppLanguage() {
        String lang = Locale.getDefault().getLanguage();
        if (lang.equals("uk"))
            lang = "ua";
        return lang;
    }
}
