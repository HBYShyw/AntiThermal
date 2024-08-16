package com.oplus.oms.split.core;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/* loaded from: classes.dex */
public interface IOMSInitializer {
    SplitConfiguration getSplitConfiguration();

    void onApplicationCreate(Application application);

    void onApplicationGetResources(Resources resources);

    void onAttachBaseContext(Context context, SplitConfiguration splitConfiguration);
}
