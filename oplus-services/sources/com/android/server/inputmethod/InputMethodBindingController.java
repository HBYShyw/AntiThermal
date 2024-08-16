package com.android.server.inputmethod;

import android.R;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManagerInternal;
import android.graphics.Matrix;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.EventLog;
import android.util.Slog;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.inputmethod.IInputMethod;
import com.android.internal.inputmethod.IInputMethodSession;
import com.android.internal.inputmethod.InputBindResult;
import com.android.server.inputmethod.InputMethodUtils;
import com.android.server.wm.WindowManagerInternal;
import java.util.concurrent.CountDownLatch;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InputMethodBindingController {
    static boolean DEBUG = false;

    @VisibleForTesting
    static final int IME_CONNECTION_BIND_FLAGS = 1082130437;

    @VisibleForTesting
    static final int IME_VISIBLE_BIND_FLAGS = 738725889;
    static final long TIME_TO_RECONNECT = 3000;
    private String TAG;
    private final Context mContext;

    @GuardedBy({"ImfLock.class"})
    private String mCurId;

    @GuardedBy({"ImfLock.class"})
    private Intent mCurIntent;

    @GuardedBy({"ImfLock.class"})
    private IInputMethodInvoker mCurMethod;

    @GuardedBy({"ImfLock.class"})
    private int mCurMethodUid;

    @GuardedBy({"ImfLock.class"})
    private int mCurSeq;

    @GuardedBy({"ImfLock.class"})
    private IBinder mCurToken;

    @GuardedBy({"ImfLock.class"})
    private boolean mHasConnection;
    private final int mImeConnectionBindFlags;

    @GuardedBy({"ImfLock.class"})
    private long mLastBindTime;
    private CountDownLatch mLatchForTesting;

    @GuardedBy({"ImfLock.class"})
    private final ServiceConnection mMainConnection;
    private final ArrayMap<String, InputMethodInfo> mMethodMap;
    private final PackageManagerInternal mPackageManagerInternal;

    @GuardedBy({"ImfLock.class"})
    private String mSelectedMethodId;
    private final InputMethodManagerService mService;
    private final InputMethodUtils.InputMethodSettings mSettings;

    @GuardedBy({"ImfLock.class"})
    private boolean mSupportsStylusHw;

    @GuardedBy({"ImfLock.class"})
    private boolean mVisibleBound;

    @GuardedBy({"ImfLock.class"})
    private final ServiceConnection mVisibleConnection;
    private final WindowManagerInternal mWindowManagerInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputMethodBindingController(InputMethodManagerService inputMethodManagerService) {
        this(inputMethodManagerService, IME_CONNECTION_BIND_FLAGS, null);
    }

    InputMethodBindingController(InputMethodManagerService inputMethodManagerService, int i, CountDownLatch countDownLatch) {
        this.TAG = InputMethodBindingController.class.getSimpleName();
        this.mCurMethodUid = -1;
        this.mVisibleConnection = new ServiceConnection() { // from class: com.android.server.inputmethod.InputMethodBindingController.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName) {
                synchronized (ImfLock.class) {
                    InputMethodBindingController.this.mService.invalidateAutofillSessionLocked();
                    if (InputMethodBindingController.this.isVisibleBound()) {
                        InputMethodBindingController.this.unbindVisibleConnection();
                    }
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                synchronized (ImfLock.class) {
                    InputMethodBindingController.this.mService.invalidateAutofillSessionLocked();
                }
            }
        };
        this.mMainConnection = new ServiceConnection() { // from class: com.android.server.inputmethod.InputMethodBindingController.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Trace.traceBegin(32L, "IMMS.onServiceConnected");
                synchronized (ImfLock.class) {
                    if (InputMethodBindingController.this.mCurIntent != null && componentName.equals(InputMethodBindingController.this.mCurIntent.getComponent())) {
                        InputMethodBindingController.this.mCurMethod = IInputMethodInvoker.create(IInputMethod.Stub.asInterface(iBinder));
                        updateCurrentMethodUid();
                        if (InputMethodBindingController.this.mCurToken == null) {
                            Slog.w(InputMethodBindingController.this.TAG, "Service connected without a token!");
                            InputMethodBindingController.this.unbindCurrentMethod();
                            Trace.traceEnd(32L);
                            return;
                        }
                        if (InputMethodBindingController.DEBUG) {
                            Slog.v(InputMethodBindingController.this.TAG, "Initiating attach with token: " + InputMethodBindingController.this.mCurToken);
                        }
                        InputMethodInfo inputMethodInfo = (InputMethodInfo) InputMethodBindingController.this.mMethodMap.get(InputMethodBindingController.this.mSelectedMethodId);
                        boolean z = InputMethodBindingController.this.mSupportsStylusHw != inputMethodInfo.supportsStylusHandwriting();
                        InputMethodBindingController.this.mSupportsStylusHw = inputMethodInfo.supportsStylusHandwriting();
                        if (z) {
                            InputMethodManager.invalidateLocalStylusHandwritingAvailabilityCaches();
                        }
                        InputMethodBindingController.this.mService.initializeImeLocked(InputMethodBindingController.this.mCurMethod, InputMethodBindingController.this.mCurToken);
                        InputMethodBindingController.this.mService.scheduleNotifyImeUidToAudioService(InputMethodBindingController.this.mCurMethodUid);
                        InputMethodBindingController.this.mService.reRequestCurrentClientSessionLocked();
                        InputMethodBindingController.this.mService.performOnCreateInlineSuggestionsRequestLocked();
                    }
                    InputMethodBindingController.this.mService.scheduleResetStylusHandwriting();
                    Trace.traceEnd(32L);
                    if (InputMethodBindingController.this.mLatchForTesting != null) {
                        InputMethodBindingController.this.mLatchForTesting.countDown();
                    }
                }
            }

            @GuardedBy({"ImfLock.class"})
            private void updateCurrentMethodUid() {
                String packageName = InputMethodBindingController.this.mCurIntent.getComponent().getPackageName();
                int packageUid = InputMethodBindingController.this.mPackageManagerInternal.getPackageUid(packageName, 0L, InputMethodBindingController.this.mSettings.getCurrentUserId());
                if (packageUid < 0) {
                    Slog.e(InputMethodBindingController.this.TAG, "Failed to get UID for package=" + packageName);
                    InputMethodBindingController.this.mCurMethodUid = -1;
                    return;
                }
                InputMethodBindingController.this.mCurMethodUid = packageUid;
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                synchronized (ImfLock.class) {
                    if (InputMethodBindingController.DEBUG) {
                        Slog.v(InputMethodBindingController.this.TAG, "Service disconnected: " + componentName + " mCurIntent=" + InputMethodBindingController.this.mCurIntent);
                    }
                    if (InputMethodBindingController.this.mCurMethod != null && InputMethodBindingController.this.mCurIntent != null && componentName.equals(InputMethodBindingController.this.mCurIntent.getComponent())) {
                        InputMethodBindingController.this.mLastBindTime = SystemClock.uptimeMillis();
                        InputMethodBindingController.this.clearCurMethodAndSessions();
                        InputMethodBindingController.this.mService.clearInputShownLocked();
                        InputMethodBindingController.this.mService.unbindCurrentClientLocked(3);
                    }
                }
            }
        };
        this.mService = inputMethodManagerService;
        this.mContext = inputMethodManagerService.mContext;
        this.mMethodMap = inputMethodManagerService.mMethodMap;
        this.mSettings = inputMethodManagerService.mSettings;
        this.mPackageManagerInternal = inputMethodManagerService.mPackageManagerInternal;
        this.mWindowManagerInternal = inputMethodManagerService.mWindowManagerInternal;
        this.mImeConnectionBindFlags = i;
        this.mLatchForTesting = countDownLatch;
        this.TAG = inputMethodManagerService.getWrapper().getExtImpl().getDebugTAG(this.TAG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public long getLastBindTime() {
        return this.mLastBindTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public boolean hasConnection() {
        return this.mHasConnection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public String getCurId() {
        return this.mCurId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public String getSelectedMethodId() {
        return this.mSelectedMethodId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void setSelectedMethodId(String str) {
        this.mSelectedMethodId = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public IBinder getCurToken() {
        return this.mCurToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public Intent getCurIntent() {
        return this.mCurIntent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public int getSequenceNumber() {
        return this.mCurSeq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void advanceSequenceNumber() {
        int i = this.mCurSeq + 1;
        this.mCurSeq = i;
        if (i <= 0) {
            this.mCurSeq = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public IInputMethodInvoker getCurMethod() {
        return this.mCurMethod;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public int getCurMethodUid() {
        return this.mCurMethodUid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public boolean isVisibleBound() {
        return this.mVisibleBound;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsStylusHandwriting() {
        return this.mSupportsStylusHw;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void unbindCurrentMethod() {
        if (isVisibleBound()) {
            unbindVisibleConnection();
        }
        if (hasConnection()) {
            unbindMainConnection();
        }
        if (getCurToken() != null) {
            removeCurrentToken();
            this.mService.resetSystemUiLocked();
        }
        this.mCurId = null;
        clearCurMethodAndSessions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"ImfLock.class"})
    public void clearCurMethodAndSessions() {
        this.mService.clearClientSessionsLocked();
        this.mCurMethod = null;
        this.mCurMethodUid = -1;
    }

    @GuardedBy({"ImfLock.class"})
    private void removeCurrentToken() {
        int curTokenDisplayIdLocked = this.mService.getCurTokenDisplayIdLocked();
        if (DEBUG) {
            Slog.v(this.TAG, "Removing window token: " + this.mCurToken + " for display: " + curTokenDisplayIdLocked);
        }
        this.mWindowManagerInternal.removeWindowToken(this.mCurToken, false, false, curTokenDisplayIdLocked);
        this.mCurToken = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public InputBindResult bindCurrentMethod() {
        String str = this.mSelectedMethodId;
        if (str == null) {
            Slog.e(this.TAG, "mSelectedMethodId is null!");
            return InputBindResult.NO_IME;
        }
        InputMethodInfo inputMethodInfo = this.mMethodMap.get(str);
        if (inputMethodInfo == null) {
            throw new IllegalArgumentException("Unknown id: " + this.mSelectedMethodId);
        }
        this.mCurIntent = createImeBindingIntent(inputMethodInfo.getComponent());
        if (bindCurrentInputMethodServiceMainConnection()) {
            this.mCurId = inputMethodInfo.getId();
            this.mLastBindTime = SystemClock.uptimeMillis();
            addFreshWindowToken();
            return new InputBindResult(2, (IInputMethodSession) null, (SparseArray) null, (InputChannel) null, this.mCurId, this.mCurSeq, (Matrix) null, false);
        }
        Slog.w("InputMethodManagerService", "Failure connecting to input method service: " + this.mCurIntent);
        this.mCurIntent = null;
        return InputBindResult.IME_NOT_CONNECTED;
    }

    private Intent createImeBindingIntent(ComponentName componentName) {
        ActivityOptions pendingIntentCreatorBackgroundActivityStartMode = ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2);
        Intent intent = new Intent("android.view.InputMethod");
        intent.setComponent(componentName);
        intent.putExtra("android.intent.extra.client_label", R.string.mediasize_iso_a2);
        intent.putExtra("android.intent.extra.client_intent", PendingIntent.getActivity(this.mContext, 0, new Intent("android.settings.INPUT_METHOD_SETTINGS"), 67108864, pendingIntentCreatorBackgroundActivityStartMode.toBundle()));
        return intent;
    }

    @GuardedBy({"ImfLock.class"})
    private void addFreshWindowToken() {
        int displayIdToShowImeLocked = this.mService.getDisplayIdToShowImeLocked();
        this.mCurToken = new Binder();
        this.mService.setCurTokenDisplayIdLocked(displayIdToShowImeLocked);
        if (DEBUG) {
            Slog.v(this.TAG, "Adding window token: " + this.mCurToken + " for display: " + displayIdToShowImeLocked);
        }
        this.mWindowManagerInternal.addWindowToken(this.mCurToken, 2011, displayIdToShowImeLocked, (Bundle) null);
    }

    @GuardedBy({"ImfLock.class"})
    private void unbindMainConnection() {
        this.mContext.unbindService(this.mMainConnection);
        this.mHasConnection = false;
    }

    @GuardedBy({"ImfLock.class"})
    void unbindVisibleConnection() {
        this.mContext.unbindService(this.mVisibleConnection);
        this.mVisibleBound = false;
    }

    @GuardedBy({"ImfLock.class"})
    private boolean bindCurrentInputMethodService(ServiceConnection serviceConnection, int i) {
        Intent intent = this.mCurIntent;
        if (intent == null || serviceConnection == null) {
            Slog.e(this.TAG, "--- bind failed: service = " + this.mCurIntent + ", conn = " + serviceConnection);
            return false;
        }
        return this.mContext.bindServiceAsUser(intent, serviceConnection, i, new UserHandle(this.mSettings.getCurrentUserId()));
    }

    @GuardedBy({"ImfLock.class"})
    private boolean bindCurrentInputMethodServiceMainConnection() {
        boolean bindCurrentInputMethodService = bindCurrentInputMethodService(this.mMainConnection, this.mImeConnectionBindFlags);
        this.mHasConnection = bindCurrentInputMethodService;
        return bindCurrentInputMethodService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void setCurrentMethodVisible() {
        if (this.mCurMethod != null) {
            if (DEBUG) {
                Slog.d(this.TAG, "setCurrentMethodVisible: mCurToken=" + this.mCurToken);
            }
            if (!hasConnection() || isVisibleBound()) {
                return;
            }
            this.mVisibleBound = bindCurrentInputMethodService(this.mVisibleConnection, IME_VISIBLE_BIND_FLAGS);
            return;
        }
        if (!hasConnection()) {
            if (DEBUG) {
                Slog.d(this.TAG, "Cannot show input: no IME bound. Rebinding.");
            }
            bindCurrentMethod();
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis() - this.mLastBindTime;
        if (uptimeMillis >= TIME_TO_RECONNECT) {
            EventLog.writeEvent(32000, getSelectedMethodId(), Long.valueOf(uptimeMillis), 1);
            Slog.w(this.TAG, "Force disconnect/connect to the IME in setCurrentMethodVisible()");
            unbindMainConnection();
            bindCurrentInputMethodServiceMainConnection();
            return;
        }
        if (DEBUG) {
            Slog.d(this.TAG, "Can't show input: connection = " + this.mHasConnection + ", time = " + (TIME_TO_RECONNECT - uptimeMillis));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"ImfLock.class"})
    public void setCurrentMethodNotVisible() {
        if (isVisibleBound()) {
            unbindVisibleConnection();
        }
    }
}
