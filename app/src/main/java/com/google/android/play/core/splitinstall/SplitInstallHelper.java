package com.google.android.play.core.splitinstall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.res.Resources;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallHelper;

/* loaded from: classes.dex */
public class SplitInstallHelper {

    /* renamed from: a, reason: collision with root package name */
    public static final String f9567a = "SplitInstallHelper";

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public static void loadLibrary(Context context, String str) {
        OplusSplitInstallHelper.loadLibrary(context, str);
    }

    public static void loadResources(Activity activity, Resources resources) {
        OplusSplitInstallHelper.loadResources(activity, resources);
    }

    public static void loadResources(Service service) {
        OplusSplitInstallHelper.loadResources(service);
    }

    public static void loadResources(BroadcastReceiver broadcastReceiver, Context context) {
        OplusSplitInstallHelper.loadResources(broadcastReceiver, context);
    }
}
