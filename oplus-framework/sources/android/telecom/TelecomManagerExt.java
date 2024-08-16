package android.telecom;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.telecom.ITelecomServiceExt;
import java.util.Objects;

/* loaded from: classes.dex */
public class TelecomManagerExt {
    private static final Object CACHE_LOCK = new Object();
    private static final DeathRecipient SERVICE_DEATH = new DeathRecipient();
    private static final String TAG = "TelecomManagerExt";
    public static final String TELECOM_EXT = "telecom_ext";
    private static ITelecomServiceExt sTelecomServiceExt;
    private final Context mContext;

    public TelecomManagerExt(Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext != null && Objects.equals(context.getAttributionTag(), appContext.getAttributionTag())) {
            this.mContext = appContext;
        } else {
            this.mContext = context;
        }
    }

    public static TelecomManagerExt from(Context context) {
        return (TelecomManagerExt) context.getSystemService(TELECOM_EXT);
    }

    public void cancelMissedCallsNotification(Bundle extras) {
        Log.i(TAG, "cancelMissedCallsNotification intent = " + extras);
        try {
            if (isServiceConnected()) {
                getITelecomServiceExt().cancelMissedCallsNotification(this.mContext.getOpPackageName(), extras);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error calling ITelecomServiceExt#cancelMissedCallsNotification", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetServiceCache() {
        synchronized (CACHE_LOCK) {
            ITelecomServiceExt iTelecomServiceExt = sTelecomServiceExt;
            if (iTelecomServiceExt != null) {
                iTelecomServiceExt.asBinder().unlinkToDeath(SERVICE_DEATH, 0);
                sTelecomServiceExt = null;
            }
        }
    }

    private boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }

    private ITelecomServiceExt getITelecomServiceExt() {
        ITelecomServiceExt temp;
        ITelecomServiceExt temp2;
        synchronized (CACHE_LOCK) {
            if (sTelecomServiceExt == null && (temp2 = ITelecomServiceExt.Stub.asInterface(ServiceManager.getService(TELECOM_EXT))) != null) {
                try {
                    sTelecomServiceExt = temp2;
                    temp2.asBinder().linkToDeath(SERVICE_DEATH, 0);
                } catch (Exception e) {
                    sTelecomServiceExt = null;
                }
            }
            temp = sTelecomServiceExt;
        }
        return temp;
    }

    private boolean isServiceConnected() {
        boolean isConnected = getITelecomServiceExt() != null;
        if (!isConnected) {
            Log.e(TAG, "TelecomServiceExt not found.");
        }
        return isConnected;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DeathRecipient implements IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            TelecomManagerExt.resetServiceCache();
        }
    }
}
