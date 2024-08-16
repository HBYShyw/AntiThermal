package com.android.server.inputmethod;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.android.internal.compat.IPlatformCompat;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class ImePlatformCompatUtils {
    private final IPlatformCompat mPlatformCompat = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));

    public boolean shouldUseSetInteractiveProtocol(int i) {
        return isChangeEnabledByUid(156215187L, i);
    }

    public boolean shouldClearShowForcedFlag(int i) {
        return isChangeEnabledByUid(214016041L, i);
    }

    private boolean isChangeEnabledByUid(long j, int i) {
        String valueOf;
        boolean z = false;
        try {
            z = this.mPlatformCompat.isChangeEnabledByUid(j, i);
            if (InputMethodManagerService.DEBUG) {
                if (j == 214016041) {
                    valueOf = "CLEAR_SHOW_FORCED_FLAG_WHEN_LEAVING";
                } else {
                    valueOf = j == 156215187 ? "FINISH_INPUT_NO_FALLBACK_CONNECTION" : String.valueOf(j);
                }
                Slog.d("ImePlatformCompatUtils", "isChangeEnabledByUid: changeFlag = " + valueOf + ", uid = " + i + ", result = " + z);
            }
        } catch (RemoteException unused) {
        }
        return z;
    }
}
