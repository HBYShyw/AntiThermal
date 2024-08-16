package com.android.server.display.mode;

import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.IThermalEventListener;
import android.os.Temperature;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.SurfaceControl;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.server.display.mode.DisplayModeDirector;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SkinThermalStatusObserver extends IThermalEventListener.Stub implements DisplayManager.DisplayListener {
    private static final String TAG = "SkinThermalStatusObserver";
    private final Handler mHandler;
    private final DisplayModeDirector.Injector mInjector;
    private boolean mLoggingEnabled;

    @GuardedBy({"mThermalObserverLock"})
    private int mStatus;
    private final Object mThermalObserverLock;

    @GuardedBy({"mThermalObserverLock"})
    private final SparseArray<SparseArray<SurfaceControl.RefreshRateRange>> mThermalThrottlingByDisplay;
    private final VotesStorage mVotesStorage;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SkinThermalStatusObserver(DisplayModeDirector.Injector injector, VotesStorage votesStorage) {
        this(injector, votesStorage, BackgroundThread.getHandler());
    }

    @VisibleForTesting
    SkinThermalStatusObserver(DisplayModeDirector.Injector injector, VotesStorage votesStorage, Handler handler) {
        this.mThermalObserverLock = new Object();
        this.mStatus = -1;
        this.mThermalThrottlingByDisplay = new SparseArray<>();
        this.mInjector = injector;
        this.mVotesStorage = votesStorage;
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void observe() {
        if (this.mInjector.registerThermalServiceListener(this)) {
            this.mInjector.registerDisplayListener(this, this.mHandler, 7L);
            populateInitialDisplayInfo();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLoggingEnabled(boolean z) {
        this.mLoggingEnabled = z;
    }

    public void notifyThrottling(Temperature temperature) {
        int status = temperature.getStatus();
        synchronized (this.mThermalObserverLock) {
            if (this.mStatus == status) {
                return;
            }
            this.mStatus = status;
            this.mHandler.post(new Runnable() { // from class: com.android.server.display.mode.SkinThermalStatusObserver$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    SkinThermalStatusObserver.this.updateVotes();
                }
            });
            if (this.mLoggingEnabled) {
                Slog.d(TAG, "New thermal throttling status , current thermal status = " + status);
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int i) {
        updateThermalRefreshRateThrottling(i);
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Display added:" + i);
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(final int i) {
        synchronized (this.mThermalObserverLock) {
            this.mThermalThrottlingByDisplay.remove(i);
            this.mHandler.post(new Runnable() { // from class: com.android.server.display.mode.SkinThermalStatusObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SkinThermalStatusObserver.this.lambda$onDisplayRemoved$0(i);
                }
            });
        }
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Display removed and voted: displayId=" + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayRemoved$0(int i) {
        this.mVotesStorage.updateVote(i, 12, null);
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int i) {
        updateThermalRefreshRateThrottling(i);
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Display changed:" + i);
        }
    }

    private void populateInitialDisplayInfo() {
        DisplayInfo displayInfo = new DisplayInfo();
        Display[] displays = this.mInjector.getDisplays();
        int length = displays.length;
        SparseArray sparseArray = new SparseArray(length);
        for (Display display : displays) {
            int displayId = display.getDisplayId();
            display.getDisplayInfo(displayInfo);
            sparseArray.put(displayId, displayInfo.thermalRefreshRateThrottling);
        }
        synchronized (this.mThermalObserverLock) {
            for (int i = 0; i < length; i++) {
                this.mThermalThrottlingByDisplay.put(sparseArray.keyAt(i), (SparseArray) sparseArray.valueAt(i));
            }
        }
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Display initial info:" + sparseArray);
        }
    }

    private void updateThermalRefreshRateThrottling(final int i) {
        DisplayInfo displayInfo = new DisplayInfo();
        this.mInjector.getDisplayInfo(i, displayInfo);
        SparseArray<SurfaceControl.RefreshRateRange> sparseArray = displayInfo.thermalRefreshRateThrottling;
        synchronized (this.mThermalObserverLock) {
            this.mThermalThrottlingByDisplay.put(i, sparseArray);
            this.mHandler.post(new Runnable() { // from class: com.android.server.display.mode.SkinThermalStatusObserver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SkinThermalStatusObserver.this.lambda$updateThermalRefreshRateThrottling$1(i);
                }
            });
        }
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Thermal throttling updated: display=" + i + ", map=" + sparseArray);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVotes() {
        int i;
        SparseArray<SparseArray<SurfaceControl.RefreshRateRange>> clone;
        synchronized (this.mThermalObserverLock) {
            i = this.mStatus;
            clone = this.mThermalThrottlingByDisplay.clone();
        }
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Updating votes for status=" + i + ", map=" + clone);
        }
        int size = clone.size();
        for (int i2 = 0; i2 < size; i2++) {
            reportThrottlingIfNeeded(clone.keyAt(i2), i, clone.valueAt(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateVoteForDisplay, reason: merged with bridge method [inline-methods] */
    public void lambda$updateThermalRefreshRateThrottling$1(int i) {
        int i2;
        SparseArray<SurfaceControl.RefreshRateRange> sparseArray;
        synchronized (this.mThermalObserverLock) {
            i2 = this.mStatus;
            sparseArray = this.mThermalThrottlingByDisplay.get(i);
        }
        if (sparseArray == null) {
            Slog.d(TAG, "Updating votes, display already removed, display=" + i);
            return;
        }
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Updating votes for status=" + i2 + ", display =" + i + ", map=" + sparseArray);
        }
        reportThrottlingIfNeeded(i, i2, sparseArray);
    }

    private void reportThrottlingIfNeeded(int i, int i2, SparseArray<SurfaceControl.RefreshRateRange> sparseArray) {
        if (i2 == -1) {
            return;
        }
        if (sparseArray == null) {
            Slog.d(TAG, "throttlingMap is null, check why value is null");
            return;
        }
        if (sparseArray.size() == 0) {
            fallbackReportThrottlingIfNeeded(i, i2);
            return;
        }
        SurfaceControl.RefreshRateRange findBestMatchingRefreshRateRange = findBestMatchingRefreshRateRange(i2, sparseArray);
        Vote forRenderFrameRates = findBestMatchingRefreshRateRange != null ? Vote.forRenderFrameRates(findBestMatchingRefreshRateRange.min, findBestMatchingRefreshRateRange.max) : null;
        this.mVotesStorage.updateVote(i, 12, forRenderFrameRates);
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Voted: vote=" + forRenderFrameRates + ", display =" + i);
        }
    }

    private SurfaceControl.RefreshRateRange findBestMatchingRefreshRateRange(int i, SparseArray<SurfaceControl.RefreshRateRange> sparseArray) {
        SurfaceControl.RefreshRateRange refreshRateRange = null;
        while (i >= 0) {
            refreshRateRange = sparseArray.get(i);
            if (refreshRateRange != null) {
                break;
            }
            i--;
        }
        return refreshRateRange;
    }

    private void fallbackReportThrottlingIfNeeded(int i, int i2) {
        Vote forRenderFrameRates = i2 >= 4 ? Vote.forRenderFrameRates(0.0f, 60.0f) : null;
        this.mVotesStorage.updateVote(i, 12, forRenderFrameRates);
        if (this.mLoggingEnabled) {
            Slog.d(TAG, "Voted(fallback): vote=" + forRenderFrameRates + ", display =" + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpLocked(PrintWriter printWriter) {
        int i;
        SparseArray<SparseArray<SurfaceControl.RefreshRateRange>> clone;
        synchronized (this.mThermalObserverLock) {
            i = this.mStatus;
            clone = this.mThermalThrottlingByDisplay.clone();
        }
        printWriter.println("  SkinThermalStatusObserver:");
        printWriter.println("    mStatus: " + i);
        printWriter.println("    mThermalThrottlingByDisplay: " + clone);
    }
}
