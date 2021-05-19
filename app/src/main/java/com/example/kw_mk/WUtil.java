package com.example.kw_mk;

import android.app.Activity;
import android.util.Patterns;
import android.widget.Toast;

public class WUtil {
    public WUtil(){
    }

    public static final String INTENT_PATH = "path";
    public static final String INTENT_MEDIA = "media";

    public static final int GALLERY_IMAGE = 0;
    public static final int GALLERY_VIDEO = 1;

    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isStorageUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches()
                && url.contains("https://firebasestorage.googleapis.com/v0/b/cp-project-7bfa3.appspot.com/o/posts");
    }

    public static String storageUrlToName(String url){
        return url.split("\\?")[0].split("%2F")[url.split("\\?")[0].split("%2F").length - 1];
    }
}
