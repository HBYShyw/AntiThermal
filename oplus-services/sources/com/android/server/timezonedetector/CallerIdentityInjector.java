package com.android.server.timezonedetector;

import android.os.Binder;
import android.os.UserHandle;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface CallerIdentityInjector {
    public static final CallerIdentityInjector REAL = new Real();

    long clearCallingIdentity();

    int getCallingUserId();

    void restoreCallingIdentity(long j);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Real implements CallerIdentityInjector {
        protected Real() {
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public int getCallingUserId() {
            return UserHandle.getCallingUserId();
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public long clearCallingIdentity() {
            return Binder.clearCallingIdentity();
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public void restoreCallingIdentity(long j) {
            Binder.restoreCallingIdentity(j);
        }
    }
}
