package com.android.server.wm;

import android.os.RemoteException;
import android.util.Slog;
import android.view.IDisplayChangeWindowCallback;
import android.window.DisplayAreaInfo;
import android.window.WindowContainerTransaction;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.wm.RemoteDisplayChangeController;
import com.android.server.wm.WindowManagerService;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RemoteDisplayChangeController {
    private static final int REMOTE_DISPLAY_CHANGE_TIMEOUT_MS = 800;
    private static final String TAG = "RemoteDisplayChangeController";
    private final int mDisplayId;
    private final WindowManagerService mService;
    private final Runnable mTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.RemoteDisplayChangeController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            RemoteDisplayChangeController.this.onContinueTimedOut();
        }
    };
    private final List<ContinueRemoteDisplayChangeCallback> mCallbacks = new ArrayList();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface ContinueRemoteDisplayChangeCallback {
        void onContinueRemoteDisplayChange(WindowContainerTransaction windowContainerTransaction);
    }

    public RemoteDisplayChangeController(WindowManagerService windowManagerService, int i) {
        this.mService = windowManagerService;
        this.mDisplayId = i;
    }

    public boolean isWaitingForRemoteDisplayChange() {
        return !this.mCallbacks.isEmpty();
    }

    public boolean performRemoteDisplayChange(int i, int i2, DisplayAreaInfo displayAreaInfo, ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback) {
        if (this.mService.mDisplayChangeController == null) {
            return false;
        }
        this.mCallbacks.add(continueRemoteDisplayChangeCallback);
        if (displayAreaInfo != null && ProtoLogCache.WM_DEBUG_CONFIGURATION_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_CONFIGURATION, 1393721079, 85, (String) null, new Object[]{Long.valueOf(i), Long.valueOf(displayAreaInfo.configuration.windowConfiguration.getMaxBounds().width()), Long.valueOf(displayAreaInfo.configuration.windowConfiguration.getMaxBounds().height()), Long.valueOf(i2)});
        }
        IDisplayChangeWindowCallback createCallback = createCallback(continueRemoteDisplayChangeCallback);
        try {
            this.mService.mH.removeCallbacks(this.mTimeoutRunnable);
            this.mService.mH.postDelayed(this.mTimeoutRunnable, 800L);
            this.mService.mDisplayChangeController.onDisplayChange(this.mDisplayId, i, i2, displayAreaInfo, createCallback);
            return true;
        } catch (RemoteException e) {
            Slog.e(TAG, "Exception while dispatching remote display-change", e);
            this.mCallbacks.remove(continueRemoteDisplayChangeCallback);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onContinueTimedOut() {
        Slog.e(TAG, "RemoteDisplayChange timed-out, UI might get messed-up after this.");
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                try {
                    ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback = this.mCallbacks.get(i);
                    if (i == this.mCallbacks.size() - 1) {
                        this.mCallbacks.clear();
                    }
                    continueRemoteDisplayChangeCallback.onContinueRemoteDisplayChange(null);
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void continueDisplayChange(ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback, WindowContainerTransaction windowContainerTransaction) {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int indexOf = this.mCallbacks.indexOf(continueRemoteDisplayChangeCallback);
                if (indexOf < 0) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                for (int i = 0; i < indexOf; i++) {
                    this.mCallbacks.get(i).onContinueRemoteDisplayChange(null);
                }
                this.mCallbacks.subList(0, indexOf + 1).clear();
                if (this.mCallbacks.isEmpty()) {
                    this.mService.mH.removeCallbacks(this.mTimeoutRunnable);
                }
                continueRemoteDisplayChangeCallback.onContinueRemoteDisplayChange(windowContainerTransaction);
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.RemoteDisplayChangeController$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass1 extends IDisplayChangeWindowCallback.Stub {
        final /* synthetic */ ContinueRemoteDisplayChangeCallback val$callback;

        AnonymousClass1(ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback) {
            this.val$callback = continueRemoteDisplayChangeCallback;
        }

        public void continueDisplayChange(final WindowContainerTransaction windowContainerTransaction) {
            WindowManagerGlobalLock windowManagerGlobalLock = RemoteDisplayChangeController.this.mService.mGlobalLock;
            WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!RemoteDisplayChangeController.this.mCallbacks.contains(this.val$callback)) {
                        WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    WindowManagerService.H h = RemoteDisplayChangeController.this.mService.mH;
                    final ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback = this.val$callback;
                    h.post(new Runnable() { // from class: com.android.server.wm.RemoteDisplayChangeController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            RemoteDisplayChangeController.AnonymousClass1.this.lambda$continueDisplayChange$0(continueRemoteDisplayChangeCallback, windowContainerTransaction);
                        }
                    });
                    WindowManagerService.resetPriorityAfterLockedSection();
                } catch (Throwable th) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$continueDisplayChange$0(ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback, WindowContainerTransaction windowContainerTransaction) {
            RemoteDisplayChangeController.this.continueDisplayChange(continueRemoteDisplayChangeCallback, windowContainerTransaction);
        }
    }

    private IDisplayChangeWindowCallback createCallback(ContinueRemoteDisplayChangeCallback continueRemoteDisplayChangeCallback) {
        return new AnonymousClass1(continueRemoteDisplayChangeCallback);
    }
}
