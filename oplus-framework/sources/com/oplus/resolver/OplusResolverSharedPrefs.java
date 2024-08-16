package com.oplus.resolver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.storage.StorageManager;
import java.io.File;

/* loaded from: classes.dex */
public class OplusResolverSharedPrefs {
    public static final String OTA_COLOROS13 = "ota_coloros13";
    public static final String OTA_COLOROS13_TIPS = "ota_coloros13_tips";
    private static final String PINNED_SHARED_PREFS_NAME = "chooser_pin_settings";
    private static final String RESOLVER_SHARED_PREFS_NAME = "resolver_share_pref";

    public static SharedPreferences getPinnedSharedPrefs(Context context) {
        File prefsFile = new File(new File(Environment.getDataUserCePackageDirectory(StorageManager.UUID_PRIVATE_INTERNAL, context.getUserId(), context.getPackageName()), "shared_prefs"), "chooser_pin_settings.xml");
        return context.getSharedPreferences(prefsFile, 0);
    }

    public static SharedPreferences getResolverSharedPrefs(Context context) {
        File prefsFile = new File(new File(Environment.getDataUserCePackageDirectory(StorageManager.UUID_PRIVATE_INTERNAL, context.getUserId(), context.getPackageName()), "shared_prefs"), "resolver_share_pref.xml");
        return context.getSharedPreferences(prefsFile, 0);
    }
}
