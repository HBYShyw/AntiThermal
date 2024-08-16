package com.android.server.timezonedetector;

import android.app.ActivityManagerInternal;
import com.android.server.LocalServices;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface CurrentUserIdentityInjector {
    public static final CurrentUserIdentityInjector REAL = new Real();

    int getCurrentUserId();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Real implements CurrentUserIdentityInjector {
        protected Real() {
        }

        @Override // com.android.server.timezonedetector.CurrentUserIdentityInjector
        public int getCurrentUserId() {
            return ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).getCurrentUserId();
        }
    }
}
