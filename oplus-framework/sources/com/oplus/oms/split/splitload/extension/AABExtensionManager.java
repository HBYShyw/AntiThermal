package com.oplus.oms.split.splitload.extension;

import android.app.Application;
import android.content.Context;

/* loaded from: classes.dex */
interface AABExtensionManager {
    void activeApplication(Application application, Context context) throws AABExtensionException;

    Application createApplication(ClassLoader classLoader, String str) throws AABExtensionException;

    String getSplitNameForComponent(String str);

    boolean isSplitActivity(String str);

    boolean isSplitReceiver(String str);

    boolean isSplitService(String str);
}
