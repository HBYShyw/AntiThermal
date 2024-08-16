package com.oplus.statistics.record;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/* loaded from: classes2.dex */
public class AppLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private boolean hasInit;
    private int mActivityCount;

    /* loaded from: classes2.dex */
    private static class Holder {
        private static final AppLifecycleCallbacks INSTANCE = new AppLifecycleCallbacks();

        private Holder() {
        }
    }

    public static AppLifecycleCallbacks getInstance() {
        return Holder.INSTANCE;
    }

    private boolean isAppBoot() {
        return this.mActivityCount == 1;
    }

    private boolean isAppExit() {
        return this.mActivityCount == 0;
    }

    public synchronized void init(Application application) {
        if (!this.hasInit) {
            application.registerActivityLifecycleCallbacks(this);
            this.hasInit = true;
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        if (isAppBoot()) {
            StatIdManager.getInstance().refreshAppSessionIdIfNeed(activity.getApplicationContext());
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        this.mActivityCount++;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        this.mActivityCount--;
        if (isAppExit()) {
            StatIdManager.getInstance().onAppExit(activity.getApplicationContext());
        }
    }

    private AppLifecycleCallbacks() {
        this.mActivityCount = 0;
    }
}
