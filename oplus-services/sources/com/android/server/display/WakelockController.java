package com.android.server.display;

import android.hardware.display.DisplayManagerInternal;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class WakelockController {
    private static final boolean DEBUG = false;

    @VisibleForTesting
    static final int WAKE_LOCK_MAX = 5;
    public static final int WAKE_LOCK_PROXIMITY_DEBOUNCE = 3;
    public static final int WAKE_LOCK_PROXIMITY_NEGATIVE = 2;
    public static final int WAKE_LOCK_PROXIMITY_POSITIVE = 1;
    public static final int WAKE_LOCK_STATE_CHANGED = 4;
    public static final int WAKE_LOCK_UNFINISHED_BUSINESS = 5;
    private final int mDisplayId;
    private final DisplayManagerInternal.DisplayPowerCallbacks mDisplayPowerCallbacks;
    private boolean mHasProximityDebounced;
    private boolean mIsProximityNegativeAcquired;
    private boolean mIsProximityPositiveAcquired;
    private boolean mOnStateChangedPending;
    private final String mSuspendBlockerIdOnStateChanged;
    private final String mSuspendBlockerIdProxDebounce;
    private final String mSuspendBlockerIdProxNegative;
    private final String mSuspendBlockerIdProxPositive;
    private final String mSuspendBlockerIdUnfinishedBusiness;
    private final String mTag;
    private boolean mUnfinishedBusiness;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface WAKE_LOCK_TYPE {
    }

    public WakelockController(int i, DisplayManagerInternal.DisplayPowerCallbacks displayPowerCallbacks) {
        this.mDisplayId = i;
        this.mTag = "WakelockController[" + i + "]";
        this.mDisplayPowerCallbacks = displayPowerCallbacks;
        this.mSuspendBlockerIdUnfinishedBusiness = "[" + i + "]unfinished business";
        this.mSuspendBlockerIdOnStateChanged = "[" + i + "]on state changed";
        this.mSuspendBlockerIdProxPositive = "[" + i + "]prox positive";
        this.mSuspendBlockerIdProxNegative = "[" + i + "]prox negative";
        this.mSuspendBlockerIdProxDebounce = "[" + i + "]prox debounce";
    }

    public boolean acquireWakelock(int i) {
        return acquireWakelockInternal(i);
    }

    public boolean releaseWakelock(int i) {
        return releaseWakelockInternal(i);
    }

    public void releaseAll() {
        for (int i = 1; i <= 5; i++) {
            releaseWakelockInternal(i);
        }
    }

    private boolean acquireWakelockInternal(int i) {
        if (i == 1) {
            return acquireProxPositiveSuspendBlocker();
        }
        if (i == 2) {
            return acquireProxNegativeSuspendBlocker();
        }
        if (i == 3) {
            return acquireProxDebounceSuspendBlocker();
        }
        if (i == 4) {
            return acquireStateChangedSuspendBlocker();
        }
        if (i == 5) {
            return acquireUnfinishedBusinessSuspendBlocker();
        }
        throw new RuntimeException("Invalid wakelock attempted to be acquired");
    }

    private boolean releaseWakelockInternal(int i) {
        if (i == 1) {
            return releaseProxPositiveSuspendBlocker();
        }
        if (i == 2) {
            return releaseProxNegativeSuspendBlocker();
        }
        if (i == 3) {
            return releaseProxDebounceSuspendBlocker();
        }
        if (i == 4) {
            return releaseStateChangedSuspendBlocker();
        }
        if (i == 5) {
            return releaseUnfinishedBusinessSuspendBlocker();
        }
        throw new RuntimeException("Invalid wakelock attempted to be released");
    }

    private boolean acquireProxPositiveSuspendBlocker() {
        if (this.mIsProximityPositiveAcquired) {
            return false;
        }
        this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        this.mIsProximityPositiveAcquired = true;
        return true;
    }

    private boolean acquireStateChangedSuspendBlocker() {
        if (this.mOnStateChangedPending) {
            return false;
        }
        this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
        this.mOnStateChangedPending = true;
        return true;
    }

    private boolean releaseStateChangedSuspendBlocker() {
        if (!this.mOnStateChangedPending) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
        this.mOnStateChangedPending = false;
        return true;
    }

    private boolean acquireUnfinishedBusinessSuspendBlocker() {
        if (this.mUnfinishedBusiness) {
            return false;
        }
        this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
        this.mUnfinishedBusiness = true;
        return true;
    }

    private boolean releaseUnfinishedBusinessSuspendBlocker() {
        if (!this.mUnfinishedBusiness) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdUnfinishedBusiness);
        this.mUnfinishedBusiness = false;
        return true;
    }

    private boolean releaseProxPositiveSuspendBlocker() {
        if (!this.mIsProximityPositiveAcquired) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        this.mIsProximityPositiveAcquired = false;
        return true;
    }

    private boolean acquireProxNegativeSuspendBlocker() {
        if (this.mIsProximityNegativeAcquired) {
            return false;
        }
        this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        this.mIsProximityNegativeAcquired = true;
        return true;
    }

    private boolean releaseProxNegativeSuspendBlocker() {
        if (!this.mIsProximityNegativeAcquired) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        this.mIsProximityNegativeAcquired = false;
        return true;
    }

    private boolean acquireProxDebounceSuspendBlocker() {
        if (this.mHasProximityDebounced) {
            return false;
        }
        this.mDisplayPowerCallbacks.acquireSuspendBlocker(this.mSuspendBlockerIdProxDebounce);
        this.mHasProximityDebounced = true;
        return true;
    }

    private boolean releaseProxDebounceSuspendBlocker() {
        if (!this.mHasProximityDebounced) {
            return false;
        }
        this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxDebounce);
        this.mHasProximityDebounced = false;
        return true;
    }

    public Runnable getOnProximityPositiveRunnable() {
        return new Runnable() { // from class: com.android.server.display.WakelockController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WakelockController.this.lambda$getOnProximityPositiveRunnable$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnProximityPositiveRunnable$0() {
        if (this.mIsProximityPositiveAcquired) {
            this.mIsProximityPositiveAcquired = false;
            this.mDisplayPowerCallbacks.onProximityPositive();
            this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxPositive);
        }
    }

    public Runnable getOnStateChangedRunnable() {
        return new Runnable() { // from class: com.android.server.display.WakelockController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WakelockController.this.lambda$getOnStateChangedRunnable$1();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnStateChangedRunnable$1() {
        if (this.mOnStateChangedPending) {
            this.mOnStateChangedPending = false;
            this.mDisplayPowerCallbacks.onStateChanged();
            this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdOnStateChanged);
        }
    }

    public Runnable getOnProximityNegativeRunnable() {
        return new Runnable() { // from class: com.android.server.display.WakelockController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                WakelockController.this.lambda$getOnProximityNegativeRunnable$2();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOnProximityNegativeRunnable$2() {
        if (this.mIsProximityNegativeAcquired) {
            this.mIsProximityNegativeAcquired = false;
            this.mDisplayPowerCallbacks.onProximityNegative();
            this.mDisplayPowerCallbacks.releaseSuspendBlocker(this.mSuspendBlockerIdProxNegative);
        }
    }

    public void dumpLocal(PrintWriter printWriter) {
        printWriter.println("WakelockController State:");
        printWriter.println("  mDisplayId=" + this.mDisplayId);
        printWriter.println("  mUnfinishedBusiness=" + hasUnfinishedBusiness());
        printWriter.println("  mOnStateChangePending=" + isOnStateChangedPending());
        printWriter.println("  mOnProximityPositiveMessages=" + isProximityPositiveAcquired());
        printWriter.println("  mOnProximityNegativeMessages=" + isProximityNegativeAcquired());
    }

    @VisibleForTesting
    String getSuspendBlockerUnfinishedBusinessId() {
        return this.mSuspendBlockerIdUnfinishedBusiness;
    }

    @VisibleForTesting
    String getSuspendBlockerOnStateChangedId() {
        return this.mSuspendBlockerIdOnStateChanged;
    }

    @VisibleForTesting
    String getSuspendBlockerProxPositiveId() {
        return this.mSuspendBlockerIdProxPositive;
    }

    @VisibleForTesting
    String getSuspendBlockerProxNegativeId() {
        return this.mSuspendBlockerIdProxNegative;
    }

    @VisibleForTesting
    String getSuspendBlockerProxDebounceId() {
        return this.mSuspendBlockerIdProxDebounce;
    }

    @VisibleForTesting
    boolean hasUnfinishedBusiness() {
        return this.mUnfinishedBusiness;
    }

    @VisibleForTesting
    boolean isOnStateChangedPending() {
        return this.mOnStateChangedPending;
    }

    @VisibleForTesting
    boolean isProximityPositiveAcquired() {
        return this.mIsProximityPositiveAcquired;
    }

    @VisibleForTesting
    boolean isProximityNegativeAcquired() {
        return this.mIsProximityNegativeAcquired;
    }

    @VisibleForTesting
    boolean hasProximitySensorDebounced() {
        return this.mHasProximityDebounced;
    }
}
