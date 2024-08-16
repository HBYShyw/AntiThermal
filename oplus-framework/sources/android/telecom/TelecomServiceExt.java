package android.telecom;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.android.internal.telecom.ITelecomServiceExt;

/* loaded from: classes.dex */
public class TelecomServiceExt {
    private static final boolean DBG = Build.isDebuggable();
    private static final String TAG = TelecomServiceExt.class.getSimpleName();
    private final IBinder mBinder = new ITelecomServiceExt.Stub() { // from class: android.telecom.TelecomServiceExt.1
        @Override // com.android.internal.telecom.ITelecomServiceExt
        public void cancelMissedCallsNotification(String callingPackage, Bundle extras) {
            TelecomServiceExt.this.cancelMissedCallsNotification(callingPackage, extras);
        }
    };

    public void cancelMissedCallsNotification(String callingPackage, Bundle extras) {
        Log.d(TAG, "cancelMissedCallsNotification: " + callingPackage + ", extras = " + extras);
    }

    public final IBinder getBinder() {
        return this.mBinder;
    }
}
