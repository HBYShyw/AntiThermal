package com.android.server.am;

import android.provider.DeviceConfig;
import com.android.server.am.BaseAppStateTracker;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BaseAppStatePolicy<T extends BaseAppStateTracker> {
    protected final boolean mDefaultTrackerEnabled;
    protected final BaseAppStateTracker.Injector<?> mInjector;
    protected final String mKeyTrackerEnabled;
    protected final T mTracker;
    volatile boolean mTrackerEnabled;

    public int getProposedRestrictionLevel(String str, int i, int i2) {
        return 0;
    }

    public abstract void onTrackerEnabled(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStatePolicy(BaseAppStateTracker.Injector<?> injector, T t, String str, boolean z) {
        this.mInjector = injector;
        this.mTracker = t;
        this.mKeyTrackerEnabled = str;
        this.mDefaultTrackerEnabled = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateTrackerEnabled() {
        boolean z = DeviceConfig.getBoolean("activity_manager", this.mKeyTrackerEnabled, this.mDefaultTrackerEnabled);
        if (z != this.mTrackerEnabled) {
            this.mTrackerEnabled = z;
            onTrackerEnabled(z);
        }
    }

    public void onPropertiesChanged(String str) {
        if (this.mKeyTrackerEnabled.equals(str)) {
            updateTrackerEnabled();
        }
    }

    public void onSystemReady() {
        updateTrackerEnabled();
    }

    public boolean isEnabled() {
        return this.mTrackerEnabled;
    }

    public int shouldExemptUid(int i) {
        return this.mTracker.mAppRestrictionController.getBackgroundRestrictionExemptionReason(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print(this.mKeyTrackerEnabled);
        printWriter.print('=');
        printWriter.println(this.mTrackerEnabled);
    }
}
