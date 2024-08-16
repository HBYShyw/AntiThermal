package com.android.server.devicestate;

import android.os.IBinder;
import android.util.Slog;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OverrideRequestController {
    static final int FLAG_POWER_SAVE_ENABLED = 2;
    static final int FLAG_THERMAL_CRITICAL = 1;
    static final int STATUS_ACTIVE = 1;
    static final int STATUS_CANCELED = 2;
    static final int STATUS_UNKNOWN = 0;
    private static final String TAG = "OverrideRequestController";
    private OverrideRequest mBaseStateRequest;
    private final StatusChangeListener mListener;
    private OverrideRequest mRequest;
    private boolean mStickyRequest;
    private boolean mStickyRequestsAllowed;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface RequestStatus {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface StatusChangeListener {
        void onStatusChanged(OverrideRequest overrideRequest, int i, int i2);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface StatusChangedFlag {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String statusToString(int i) {
        if (i == 0) {
            return "UNKNOWN";
        }
        if (i == 1) {
            return "ACTIVE";
        }
        if (i == 2) {
            return "CANCELED";
        }
        throw new IllegalArgumentException("Unknown status: " + i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverrideRequestController(StatusChangeListener statusChangeListener) {
        this.mListener = statusChangeListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStickyRequestsAllowed(boolean z) {
        this.mStickyRequestsAllowed = z;
        if (z) {
            return;
        }
        cancelStickyRequest();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRequest(OverrideRequest overrideRequest) {
        OverrideRequest overrideRequest2 = this.mRequest;
        this.mRequest = overrideRequest;
        this.mListener.onStatusChanged(overrideRequest, 1, 0);
        if (overrideRequest2 != null) {
            cancelRequestLocked(overrideRequest2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBaseStateRequest(OverrideRequest overrideRequest) {
        OverrideRequest overrideRequest2 = this.mBaseStateRequest;
        this.mBaseStateRequest = overrideRequest;
        this.mListener.onStatusChanged(overrideRequest, 1, 0);
        if (overrideRequest2 != null) {
            cancelRequestLocked(overrideRequest2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelRequest(OverrideRequest overrideRequest) {
        if (hasRequest(overrideRequest.getToken(), overrideRequest.getRequestType())) {
            cancelCurrentRequestLocked();
        }
    }

    void cancelStickyRequest() {
        if (this.mStickyRequest) {
            cancelCurrentRequestLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelOverrideRequest() {
        cancelCurrentRequestLocked();
    }

    void cancelBaseStateOverrideRequest() {
        cancelCurrentBaseStateRequestLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasRequest(IBinder iBinder, int i) {
        if (i == 1) {
            OverrideRequest overrideRequest = this.mBaseStateRequest;
            return overrideRequest != null && iBinder == overrideRequest.getToken();
        }
        OverrideRequest overrideRequest2 = this.mRequest;
        return overrideRequest2 != null && iBinder == overrideRequest2.getToken();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleProcessDied(int i) {
        OverrideRequest overrideRequest = this.mBaseStateRequest;
        if (overrideRequest != null && overrideRequest.getPid() == i) {
            cancelCurrentBaseStateRequestLocked();
        }
        OverrideRequest overrideRequest2 = this.mRequest;
        if (overrideRequest2 == null || overrideRequest2.getPid() != i) {
            return;
        }
        if (this.mStickyRequestsAllowed) {
            this.mStickyRequest = true;
        } else {
            cancelCurrentRequestLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleBaseStateChanged(int i) {
        OverrideRequest overrideRequest = this.mBaseStateRequest;
        if (overrideRequest != null && i != overrideRequest.getRequestedState()) {
            cancelBaseStateOverrideRequest();
        }
        OverrideRequest overrideRequest2 = this.mRequest;
        if (overrideRequest2 == null || (overrideRequest2.getFlags() & 1) == 0) {
            return;
        }
        cancelCurrentRequestLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleNewSupportedStates(int[] iArr, int i) {
        int i2 = (i == 3 ? 1 : 0) | 0 | (i == 4 ? 2 : 0);
        OverrideRequest overrideRequest = this.mBaseStateRequest;
        if (overrideRequest != null && !contains(iArr, overrideRequest.getRequestedState())) {
            cancelCurrentBaseStateRequestLocked(i2);
        }
        OverrideRequest overrideRequest2 = this.mRequest;
        if (overrideRequest2 == null || contains(iArr, overrideRequest2.getRequestedState())) {
            return;
        }
        cancelCurrentRequestLocked(i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpInternal(PrintWriter printWriter) {
        OverrideRequest overrideRequest = this.mRequest;
        boolean z = overrideRequest != null;
        printWriter.println();
        printWriter.println("Override Request active: " + z);
        if (z) {
            printWriter.println("Request: mPid=" + overrideRequest.getPid() + ", mRequestedState=" + overrideRequest.getRequestedState() + ", mFlags=" + overrideRequest.getFlags() + ", mStatus=" + statusToString(1));
        }
    }

    private void cancelRequestLocked(OverrideRequest overrideRequest) {
        cancelRequestLocked(overrideRequest, 0);
    }

    private void cancelRequestLocked(OverrideRequest overrideRequest, int i) {
        this.mListener.onStatusChanged(overrideRequest, 2, i);
    }

    private void cancelCurrentRequestLocked() {
        cancelCurrentRequestLocked(0);
    }

    private void cancelCurrentRequestLocked(int i) {
        OverrideRequest overrideRequest = this.mRequest;
        if (overrideRequest == null) {
            Slog.w(TAG, "Attempted to cancel a null OverrideRequest");
            return;
        }
        this.mStickyRequest = false;
        cancelRequestLocked(overrideRequest, i);
        this.mRequest = null;
    }

    private void cancelCurrentBaseStateRequestLocked() {
        cancelCurrentBaseStateRequestLocked(0);
    }

    private void cancelCurrentBaseStateRequestLocked(int i) {
        OverrideRequest overrideRequest = this.mBaseStateRequest;
        if (overrideRequest == null) {
            Slog.w(TAG, "Attempted to cancel a null OverrideRequest");
        } else {
            cancelRequestLocked(overrideRequest, i);
            this.mBaseStateRequest = null;
        }
    }

    private static boolean contains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }
}
