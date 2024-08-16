package com.android.server.accessibility;

import android.content.res.Resources;
import android.hardware.fingerprint.IFingerprintClientActiveCallback;
import android.hardware.fingerprint.IFingerprintService;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.util.FrameworkStatsLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FingerprintGestureDispatcher extends IFingerprintClientActiveCallback.Stub implements Handler.Callback {
    private static final String LOG_TAG = "FingerprintGestureDispatcher";
    private static final int MSG_REGISTER = 1;
    private static final int MSG_UNREGISTER = 2;
    private final List<FingerprintGestureClient> mCapturingClients;
    private final IFingerprintService mFingerprintService;
    private final Handler mHandler;
    private final boolean mHardwareSupportsGestures;
    private final Object mLock;
    private boolean mRegisteredReadOnlyExceptInHandler;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface FingerprintGestureClient {
        boolean isCapturingFingerprintGestures();

        void onFingerprintGesture(int i);

        void onFingerprintGestureDetectionActiveChanged(boolean z);
    }

    public FingerprintGestureDispatcher(IFingerprintService iFingerprintService, Resources resources, Object obj) {
        this.mCapturingClients = new ArrayList(0);
        this.mFingerprintService = iFingerprintService;
        this.mHardwareSupportsGestures = resources.getBoolean(17891696);
        this.mLock = obj;
        this.mHandler = new Handler(this);
    }

    public FingerprintGestureDispatcher(IFingerprintService iFingerprintService, Resources resources, Object obj, Handler handler) {
        this.mCapturingClients = new ArrayList(0);
        this.mFingerprintService = iFingerprintService;
        this.mHardwareSupportsGestures = resources.getBoolean(17891696);
        this.mLock = obj;
        this.mHandler = handler;
    }

    public void updateClientList(List<? extends FingerprintGestureClient> list) {
        if (this.mHardwareSupportsGestures) {
            synchronized (this.mLock) {
                this.mCapturingClients.clear();
                for (int i = 0; i < list.size(); i++) {
                    FingerprintGestureClient fingerprintGestureClient = list.get(i);
                    if (fingerprintGestureClient.isCapturingFingerprintGestures()) {
                        this.mCapturingClients.add(fingerprintGestureClient);
                    }
                }
                if (this.mCapturingClients.isEmpty()) {
                    if (this.mRegisteredReadOnlyExceptInHandler) {
                        this.mHandler.obtainMessage(2).sendToTarget();
                    }
                } else if (!this.mRegisteredReadOnlyExceptInHandler) {
                    this.mHandler.obtainMessage(1).sendToTarget();
                }
            }
        }
    }

    public void onClientActiveChanged(boolean z) {
        if (this.mHardwareSupportsGestures) {
            synchronized (this.mLock) {
                for (int i = 0; i < this.mCapturingClients.size(); i++) {
                    this.mCapturingClients.get(i).onFingerprintGestureDetectionActiveChanged(!z);
                }
            }
        }
    }

    public boolean isFingerprintGestureDetectionAvailable() {
        if (!this.mHardwareSupportsGestures) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return !this.mFingerprintService.isClientActive();
        } catch (RemoteException unused) {
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean onFingerprintGesture(int i) {
        int i2;
        synchronized (this.mLock) {
            if (this.mCapturingClients.isEmpty()) {
                return false;
            }
            switch (i) {
                case 280:
                    i2 = 4;
                    break;
                case 281:
                    i2 = 8;
                    break;
                case FrameworkStatsLog.DISPLAY_WAKE_REPORTED /* 282 */:
                    i2 = 2;
                    break;
                case 283:
                    i2 = 1;
                    break;
                default:
                    return false;
            }
            ArrayList arrayList = new ArrayList(this.mCapturingClients);
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                ((FingerprintGestureClient) arrayList.get(i3)).onFingerprintGesture(i2);
            }
            return true;
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        long clearCallingIdentity;
        int i = message.what;
        if (i == 1) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    this.mFingerprintService.addClientActiveCallback(this);
                    this.mRegisteredReadOnlyExceptInHandler = true;
                } catch (RemoteException unused) {
                    Slog.e(LOG_TAG, "Failed to register for fingerprint activity callbacks");
                }
                return false;
            } finally {
            }
        }
        if (i == 2) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    this.mFingerprintService.removeClientActiveCallback(this);
                } catch (RemoteException unused2) {
                    Slog.e(LOG_TAG, "Failed to unregister for fingerprint activity callbacks");
                }
                this.mRegisteredReadOnlyExceptInHandler = false;
                return true;
            } finally {
            }
        }
        Slog.e(LOG_TAG, "Unknown message: " + message.what);
        return false;
    }
}
