package com.oplus.oms.split.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import com.oplus.oms.split.core.splitmanager.OMSInitializer;
import com.oplus.oms.split.core.splitmanager.SplitManagerExt;
import java.util.List;

/* loaded from: classes.dex */
public class Oms {
    private Oms() {
    }

    public static void onAttachBaseContext(Context base, SplitConfiguration config) {
        OMSInitializer.get().onAttachBaseContext(base, config);
    }

    public static void onApplicationCreate(Application app) {
        OMSInitializer.get().onApplicationCreate(app);
    }

    public static void onApplicationGetResources(Resources res) {
        OMSInitializer.get().onApplicationGetResources(res);
    }

    public static void unloadSplit(Context context, String splitName) {
        SplitManagerExt.get().unloadSplit(context, splitName);
    }

    public static void setNetworkStrategy(int netWorkingType) {
        SplitManagerExt.get().setNetworkStrategy(netWorkingType);
    }

    public static int getSplitVersionCode(Context context, String splitName) {
        return SplitManagerExt.get().getSplitVersionCode(context, splitName);
    }

    public static boolean scheduledDownload(Context context, List<String> splitNames) {
        return SplitManagerExt.get().scheduledDownload(context, splitNames);
    }

    public static ClassLoader getSplitClassLoader(String splitName) {
        return SplitManagerExt.get().getSplitClassLoader(splitName);
    }
}
