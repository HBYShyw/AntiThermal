package com.oplus.util;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.Collection;
import java.util.Set;

/* loaded from: classes.dex */
public class OplusResolverIntentUtil {
    public static final String DEFAULT_APP_APPLICATION = "application";
    public static final String DEFAULT_APP_AUDIO = "audio";
    public static final String DEFAULT_APP_BROWSER = "browser";
    public static final String DEFAULT_APP_CAMERA = "camera";
    public static final String DEFAULT_APP_CONTACTS = "contacts";
    public static final String DEFAULT_APP_DIALER = "dialer";
    public static final String DEFAULT_APP_EMAIL = "email";
    public static final String DEFAULT_APP_EXCEL = "excel";
    public static final String DEFAULT_APP_GALLERY = "gallery";
    public static final String DEFAULT_APP_LAUNCHER = "launcher";
    public static final String DEFAULT_APP_MARKET = "market";
    public static final String DEFAULT_APP_MESSAGE = "message";
    public static final String DEFAULT_APP_PDF = "pdf";
    public static final String DEFAULT_APP_PPT = "ppt";
    public static final String DEFAULT_APP_TEXT = "txt";
    public static final String DEFAULT_APP_VIDEO = "video";
    public static final String DEFAULT_APP_WORD = "word";
    private static final String TAG = "OplusResolverIntentUtil";

    public static boolean isChooserAction(Intent intent) {
        String action;
        if (intent == null || (action = intent.getAction()) == null || (!action.equals("android.intent.action.SEND") && !action.equals("android.intent.action.SEND_MULTIPLE"))) {
            return false;
        }
        return true;
    }

    public static String getIntentType(Intent intent) {
        String action = intent.getAction();
        Set<String> categories = intent.getCategories();
        String scheme = intent.getScheme();
        String type = intent.getType();
        Uri data = intent.getData();
        String host = data != null ? data.getHost() : null;
        String defaultAppType = "others";
        if (TextUtils.equals(action, "android.intent.action.MAIN") && isInDataSet(categories, "android.intent.category.HOME")) {
            defaultAppType = DEFAULT_APP_LAUNCHER;
        } else if (TextUtils.equals(scheme, "sms") || TextUtils.equals(scheme, "mms") || TextUtils.equals(scheme, "smsto") || TextUtils.equals(scheme, "mmsto")) {
            defaultAppType = DEFAULT_APP_MESSAGE;
        } else if (TextUtils.equals(action, "android.intent.action.DIAL") || TextUtils.equals(scheme, "tel") || TextUtils.equals(type, "vnd.android.cursor.dir/calls")) {
            defaultAppType = DEFAULT_APP_DIALER;
        } else if (TextUtils.equals(type, "vnd.android.cursor.dir/contact") || TextUtils.equals(type, "vnd.android.cursor.dir/phone_v2")) {
            defaultAppType = DEFAULT_APP_CONTACTS;
        } else if (TextUtils.equals(scheme, "http") || TextUtils.equals(scheme, "https")) {
            defaultAppType = DEFAULT_APP_BROWSER;
        } else if (TextUtils.equals(action, "android.media.action.IMAGE_CAPTURE") || TextUtils.equals(action, "android.media.action.VIDEO_CAPTURE") || TextUtils.equals(action, "android.media.action.VIDEO_CAMERA") || TextUtils.equals(action, "android.media.action.STILL_IMAGE_CAMERA") || TextUtils.equals(action, "com.oplus.action.CAMERA")) {
            defaultAppType = DEFAULT_APP_CAMERA;
        } else if (!TextUtils.isEmpty(type) && type.startsWith("image")) {
            defaultAppType = DEFAULT_APP_GALLERY;
        } else if (!TextUtils.isEmpty(type) && type.startsWith(DEFAULT_APP_AUDIO)) {
            defaultAppType = DEFAULT_APP_AUDIO;
        } else if (!TextUtils.isEmpty(type) && type.startsWith(DEFAULT_APP_VIDEO)) {
            defaultAppType = DEFAULT_APP_VIDEO;
        } else if (TextUtils.equals(scheme, "mailto")) {
            defaultAppType = DEFAULT_APP_EMAIL;
        } else if (TextUtils.equals(type, "text/plain")) {
            defaultAppType = DEFAULT_APP_TEXT;
        } else if (TextUtils.equals(type, "application/pdf")) {
            defaultAppType = DEFAULT_APP_PDF;
        } else if (TextUtils.equals(type, "application/msword") || TextUtils.equals(type, "application/ms-word") || TextUtils.equals(type, "application/vnd.ms-word") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.wordprocessingml.document") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.wordprocessingml.template") || TextUtils.equals(type, "application/vnd.ms-word.document.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-word.template.macroenabled.12")) {
            defaultAppType = DEFAULT_APP_WORD;
        } else if (TextUtils.equals(type, "application/msexcel") || TextUtils.equals(type, "application/ms-excel") || TextUtils.equals(type, "application/vnd.ms-excel") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.spreadsheetml.template") || TextUtils.equals(type, "application/vnd.ms-excel.sheet.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-excel.template.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-excel.addin.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-excel.sheet.binary.macroenabled.12")) {
            defaultAppType = DEFAULT_APP_EXCEL;
        } else if (TextUtils.equals(type, "application/mspowerpoint") || TextUtils.equals(type, "application/ms-powerpoint") || TextUtils.equals(type, "application/vnd.ms-powerpoint") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.presentationml.presentation") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.presentationml.template") || TextUtils.equals(type, "application/vnd.openxmlformats-officedocument.presentationml.slideshow") || TextUtils.equals(type, "application/vnd.ms-powerpoint.presentation.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-powerpoint.template.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-powerpoint.slideshow.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-powerpoint.addin.macroenabled.12") || TextUtils.equals(type, "application/vnd.ms-powerpoint.slide.macroenabled.12")) {
            defaultAppType = DEFAULT_APP_PPT;
        } else if (TextUtils.equals(scheme, DEFAULT_APP_MARKET) && TextUtils.equals(host, "details")) {
            defaultAppType = DEFAULT_APP_MARKET;
        } else if (TextUtils.equals(type, "application/vnd.android.package-archive")) {
            defaultAppType = DEFAULT_APP_APPLICATION;
        }
        Log.d(TAG, "The MIME data type of this intent is " + defaultAppType);
        return defaultAppType;
    }

    public static <T> boolean isInDataSet(Collection<T> dataSet, T e) {
        if (dataSet == null || dataSet.isEmpty() || e == null) {
            return false;
        }
        return dataSet.contains(e);
    }
}
