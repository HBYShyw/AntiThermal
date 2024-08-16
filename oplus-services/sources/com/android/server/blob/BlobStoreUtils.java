package com.android.server.blob;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.UserHandle;
import android.text.format.TimeMigrationUtils;
import android.util.Slog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BlobStoreUtils {
    private static final String DESC_RES_TYPE_STRING = "string";

    BlobStoreUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Resources getPackageResources(Context context, String str, int i) {
        try {
            return context.createContextAsUser(UserHandle.of(i), 0).getPackageManager().getResourcesForApplication(str);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.d(BlobStoreConfig.TAG, "Unknown package in user " + i + ": " + str, e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getDescriptionResourceId(Resources resources, String str, String str2) {
        return resources.getIdentifier(str, DESC_RES_TYPE_STRING, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getDescriptionResourceId(Context context, String str, String str2, int i) {
        Resources packageResources = getPackageResources(context, str2, i);
        if (packageResources == null) {
            return 0;
        }
        return getDescriptionResourceId(packageResources, str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatTime(long j) {
        return TimeMigrationUtils.formatMillisWithFixedFormat(j);
    }
}
