package com.android.server.inputmethod;

import android.os.Binder;
import android.os.IBinder;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.WindowManager;
import android.view.inputmethod.ImeTracker;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.inputmethod.InputMethodDebug;
import com.android.server.LocalServices;
import com.android.server.inputmethod.InputMethodManagerService;
import com.android.server.wm.ImeTargetChangeListener;
import com.android.server.wm.WindowManagerInternal;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.WeakHashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ImeVisibilityStateComputer {
    private static boolean DEBUG = InputMethodManagerService.DEBUG;
    public static final int STATE_HIDE_IME = 0;
    public static final int STATE_HIDE_IME_EXPLICIT = 5;
    public static final int STATE_HIDE_IME_NOT_ALWAYS = 6;
    public static final int STATE_INVALID = -1;
    public static final int STATE_REMOVE_IME_SNAPSHOT = 8;
    public static final int STATE_SHOW_IME = 1;
    public static final int STATE_SHOW_IME_ABOVE_OVERLAY = 2;
    public static final int STATE_SHOW_IME_BEHIND_OVERLAY = 3;
    public static final int STATE_SHOW_IME_IMPLICIT = 7;
    public static final int STATE_SHOW_IME_SNAPSHOT = 4;
    private String TAG;
    private IBinder mCurVisibleImeInputTarget;
    private IBinder mCurVisibleImeLayeringOverlay;
    final InputMethodManagerService.ImeDisplayValidator mImeDisplayValidator;
    private boolean mInputShown;
    private final ImeVisibilityPolicy mPolicy;
    private final WeakHashMap<IBinder, ImeTargetWindowState> mRequestWindowStateMap;
    private boolean mRequestedImeScreenshot;
    boolean mRequestedShowExplicitly;
    private final InputMethodManagerService mService;
    boolean mShowForced;
    private final WindowManagerInternal mWindowManagerInternal;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface Injector {
        default InputMethodManagerService.ImeDisplayValidator getImeValidator() {
            return null;
        }

        default WindowManagerInternal getWmService() {
            return null;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface VisibilityState {
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ImeVisibilityStateComputer(InputMethodManagerService inputMethodManagerService) {
        this(inputMethodManagerService, r1, new InputMethodManagerService.ImeDisplayValidator() { // from class: com.android.server.inputmethod.ImeVisibilityStateComputer$$ExternalSyntheticLambda0
            @Override // com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator
            public final int getDisplayImePolicy(int i) {
                return r1.getDisplayImePolicy(i);
            }
        }, new ImeVisibilityPolicy());
        WindowManagerInternal windowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        final WindowManagerInternal windowManagerInternal2 = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        Objects.requireNonNull(windowManagerInternal2);
    }

    @VisibleForTesting
    public ImeVisibilityStateComputer(InputMethodManagerService inputMethodManagerService, Injector injector) {
        this(inputMethodManagerService, injector.getWmService(), injector.getImeValidator(), new ImeVisibilityPolicy());
    }

    private ImeVisibilityStateComputer(InputMethodManagerService inputMethodManagerService, WindowManagerInternal windowManagerInternal, InputMethodManagerService.ImeDisplayValidator imeDisplayValidator, ImeVisibilityPolicy imeVisibilityPolicy) {
        this.TAG = "ImeVisibilityStateComputer";
        this.mRequestWindowStateMap = new WeakHashMap<>();
        this.mService = inputMethodManagerService;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mImeDisplayValidator = imeDisplayValidator;
        this.mPolicy = imeVisibilityPolicy;
        windowManagerInternal.setInputMethodTargetChangeListener(new ImeTargetChangeListener() { // from class: com.android.server.inputmethod.ImeVisibilityStateComputer.1
            public void onImeTargetOverlayVisibilityChanged(IBinder iBinder, int i, boolean z, boolean z2) {
                ImeVisibilityStateComputer imeVisibilityStateComputer = ImeVisibilityStateComputer.this;
                if (!z || z2 || i == 3) {
                    iBinder = null;
                }
                imeVisibilityStateComputer.mCurVisibleImeLayeringOverlay = iBinder;
            }

            public void onImeInputTargetVisibilityChanged(IBinder iBinder, boolean z, boolean z2) {
                if (ImeVisibilityStateComputer.this.mCurVisibleImeInputTarget == iBinder && ((!z || z2) && ImeVisibilityStateComputer.this.mCurVisibleImeLayeringOverlay != null)) {
                    ImeVisibilityStateComputer.this.mService.onApplyImeVisibilityFromComputer(iBinder, new ImeVisibilityResult(5, 37));
                }
                ImeVisibilityStateComputer imeVisibilityStateComputer = ImeVisibilityStateComputer.this;
                if (!z || z2) {
                    iBinder = null;
                }
                imeVisibilityStateComputer.mCurVisibleImeInputTarget = iBinder;
            }
        });
        this.TAG = inputMethodManagerService.getWrapper().getExtImpl().getDebugTAG(this.TAG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onImeShowFlags(ImeTracker.Token token, int i) {
        if (this.mPolicy.mA11yRequestingNoSoftKeyboard || this.mPolicy.mImeHiddenByDisplayPolicy) {
            ImeTracker.forLogging().onFailed(token, 4);
            return false;
        }
        ImeTracker.forLogging().onProgress(token, 4);
        if ((i & 2) != 0) {
            this.mRequestedShowExplicitly = true;
            this.mShowForced = true;
        } else if ((i & 1) == 0) {
            this.mRequestedShowExplicitly = true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canHideIme(ImeTracker.Token token, int i) {
        if ((i & 1) != 0 && (this.mRequestedShowExplicitly || this.mShowForced)) {
            if (DEBUG) {
                Slog.v(this.TAG, "Not hiding: explicit show not cancelled by non-explicit hide");
            }
            ImeTracker.forLogging().onFailed(token, 6);
            return false;
        }
        if (this.mShowForced && (i & 2) != 0) {
            if (DEBUG) {
                Slog.v(this.TAG, "Not hiding: forced show not cancelled by not-always hide");
            }
            ImeTracker.forLogging().onFailed(token, 7);
            return false;
        }
        ImeTracker.forLogging().onProgress(token, 7);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getImeShowFlags() {
        return this.mShowForced ? 3 : 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearImeShowFlags() {
        this.mRequestedShowExplicitly = false;
        this.mShowForced = false;
        this.mInputShown = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int computeImeDisplayId(ImeTargetWindowState imeTargetWindowState, int i) {
        int computeImeDisplayIdForTarget = InputMethodManagerService.computeImeDisplayIdForTarget(i, this.mImeDisplayValidator);
        imeTargetWindowState.setImeDisplayId(computeImeDisplayIdForTarget);
        this.mPolicy.setImeHiddenByDisplayPolicy(computeImeDisplayIdForTarget == -1);
        return computeImeDisplayIdForTarget;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestImeVisibility(IBinder iBinder, boolean z) {
        ImeTargetWindowState orCreateWindowState = getOrCreateWindowState(iBinder);
        if (!this.mPolicy.mPendingA11yRequestingHideKeyboard) {
            orCreateWindowState.setRequestedImeVisible(z);
        } else {
            this.mPolicy.mPendingA11yRequestingHideKeyboard = false;
        }
        orCreateWindowState.setRequestImeToken(new Binder());
        setWindowStateInner(iBinder, orCreateWindowState);
    }

    ImeTargetWindowState getOrCreateWindowState(IBinder iBinder) {
        ImeTargetWindowState imeTargetWindowState = this.mRequestWindowStateMap.get(iBinder);
        return imeTargetWindowState == null ? new ImeTargetWindowState(0, 0, false, false, false) : imeTargetWindowState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImeTargetWindowState getWindowStateOrNull(IBinder iBinder) {
        return this.mRequestWindowStateMap.get(iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowState(IBinder iBinder, ImeTargetWindowState imeTargetWindowState) {
        ImeTargetWindowState imeTargetWindowState2 = this.mRequestWindowStateMap.get(iBinder);
        if (imeTargetWindowState2 != null && imeTargetWindowState.hasEditorFocused() && imeTargetWindowState.mToolType != 2) {
            imeTargetWindowState.setRequestedImeVisible(imeTargetWindowState2.mRequestedImeVisible);
        }
        setWindowStateInner(iBinder, imeTargetWindowState);
    }

    private void setWindowStateInner(IBinder iBinder, ImeTargetWindowState imeTargetWindowState) {
        if (DEBUG) {
            Slog.d(this.TAG, "setWindowStateInner, windowToken=" + iBinder + ", state=" + imeTargetWindowState);
        }
        this.mRequestWindowStateMap.put(iBinder, imeTargetWindowState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ImeVisibilityResult {
        private final int mReason;

        @VisibilityState
        private final int mState;

        ImeVisibilityResult(@VisibilityState int i, int i2) {
            this.mState = i;
            this.mReason = i2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @VisibilityState
        public int getState() {
            return this.mState;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getReason() {
            return this.mReason;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImeVisibilityResult computeState(ImeTargetWindowState imeTargetWindowState, boolean z) {
        int i = imeTargetWindowState.mSoftInputModeState & 15;
        boolean z2 = (imeTargetWindowState.mSoftInputModeState & 240) == 16 || this.mService.mRes.getConfiguration().isLayoutSizeAtLeast(3);
        boolean z3 = (imeTargetWindowState.mSoftInputModeState & 256) != 0;
        if (imeTargetWindowState.hasEditorFocused() && shouldRestoreImeVisibility(imeTargetWindowState)) {
            if (DEBUG) {
                Slog.v(this.TAG, "Will show input to restore visibility");
            }
            imeTargetWindowState.setRequestedImeVisible(true);
            setWindowStateInner(getWindowTokenFrom(imeTargetWindowState), imeTargetWindowState);
            return new ImeVisibilityResult(7, 23);
        }
        if (i != 0) {
            if (i == 1) {
                ImeTargetWindowState windowStateOrNull = getWindowStateOrNull(this.mService.mLastImeTargetWindow);
                if (windowStateOrNull != null) {
                    imeTargetWindowState.setRequestedImeVisible(windowStateOrNull.mRequestedImeVisible);
                }
            } else if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i == 5) {
                            if (DEBUG) {
                                Slog.v(this.TAG, "Window asks to always show input");
                            }
                            if (z) {
                                if (imeTargetWindowState.hasImeFocusChanged()) {
                                    return new ImeVisibilityResult(7, 8);
                                }
                            } else {
                                Slog.e(this.TAG, "SOFT_INPUT_STATE_ALWAYS_VISIBLE is ignored because there is no focused view that also returns true from View#onCheckIsTextEditor()");
                            }
                        }
                    } else if (z3) {
                        if (z) {
                            if (DEBUG) {
                                Slog.v(this.TAG, "Window asks to show input going forward");
                            }
                            return new ImeVisibilityResult(7, 7);
                        }
                        Slog.e(this.TAG, "SOFT_INPUT_STATE_VISIBLE is ignored because there is no focused view that also returns true from View#onCheckIsTextEditor()");
                    }
                } else if (imeTargetWindowState.hasImeFocusChanged()) {
                    if (DEBUG) {
                        Slog.v(this.TAG, "Window asks to hide input");
                    }
                    return new ImeVisibilityResult(5, 14);
                }
            } else if (z3) {
                if (DEBUG) {
                    Slog.v(this.TAG, "Window asks to hide input going forward");
                }
                return new ImeVisibilityResult(5, 13);
            }
        } else if (imeTargetWindowState.hasImeFocusChanged() && (!imeTargetWindowState.hasEditorFocused() || !z2)) {
            if (WindowManager.LayoutParams.mayUseInputMethod(imeTargetWindowState.getWindowFlags())) {
                if (DEBUG) {
                    Slog.v(this.TAG, "Unspecified window will hide input");
                }
                return new ImeVisibilityResult(6, 12);
            }
        } else if (imeTargetWindowState.hasEditorFocused() && z2 && z3) {
            if (DEBUG) {
                Slog.v(this.TAG, "Unspecified window will show input");
            }
            return new ImeVisibilityResult(7, 6);
        }
        if (!imeTargetWindowState.hasImeFocusChanged() && imeTargetWindowState.isStartInputByGainFocus()) {
            if (DEBUG) {
                Slog.v(this.TAG, "Same window without editor will hide input");
            }
            return new ImeVisibilityResult(5, 21);
        }
        if (imeTargetWindowState.hasEditorFocused() || !this.mInputShown || !imeTargetWindowState.isStartInputByGainFocus() || !this.mService.mInputMethodDeviceConfigs.shouldHideImeWhenNoEditorFocus()) {
            return null;
        }
        if (DEBUG) {
            Slog.v(this.TAG, "Window without editor will hide input");
        }
        return new ImeVisibilityResult(5, 33);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public ImeVisibilityResult onInteractiveChanged(IBinder iBinder, boolean z) {
        ImeTargetWindowState windowStateOrNull = getWindowStateOrNull(iBinder);
        if (windowStateOrNull != null && windowStateOrNull.isRequestedImeVisible() && this.mInputShown && !z) {
            this.mRequestedImeScreenshot = true;
            return new ImeVisibilityResult(4, 34);
        }
        if (!z || !this.mRequestedImeScreenshot) {
            return null;
        }
        this.mRequestedImeScreenshot = false;
        return new ImeVisibilityResult(8, 35);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder getWindowTokenFrom(IBinder iBinder) {
        for (IBinder iBinder2 : this.mRequestWindowStateMap.keySet()) {
            if (this.mRequestWindowStateMap.get(iBinder2).getRequestImeToken() == iBinder) {
                return iBinder2;
            }
        }
        return this.mService.mCurFocusedWindow;
    }

    IBinder getWindowTokenFrom(ImeTargetWindowState imeTargetWindowState) {
        for (IBinder iBinder : this.mRequestWindowStateMap.keySet()) {
            if (this.mRequestWindowStateMap.get(iBinder) == imeTargetWindowState) {
                return iBinder;
            }
        }
        return null;
    }

    boolean shouldRestoreImeVisibility(ImeTargetWindowState imeTargetWindowState) {
        int softInputModeState = imeTargetWindowState.getSoftInputModeState();
        int i = softInputModeState & 15;
        if (i != 2) {
            if (i == 3) {
                return false;
            }
        } else if ((softInputModeState & 256) != 0) {
            return false;
        }
        return this.mWindowManagerInternal.shouldRestoreImeVisibility(getWindowTokenFrom(imeTargetWindowState));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInputShown() {
        return this.mInputShown;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInputShown(boolean z) {
        this.mInputShown = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        protoOutputStream.write(1133871366154L, this.mRequestedShowExplicitly);
        protoOutputStream.write(1133871366155L, this.mShowForced);
        protoOutputStream.write(1133871366168L, this.mPolicy.isA11yRequestNoSoftKeyboard());
        protoOutputStream.write(1133871366156L, this.mInputShown);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        PrintWriterPrinter printWriterPrinter = new PrintWriterPrinter(printWriter);
        printWriterPrinter.println(" mRequestedShowExplicitly=" + this.mRequestedShowExplicitly + " mShowForced=" + this.mShowForced);
        StringBuilder sb = new StringBuilder();
        sb.append("  mImeHiddenByDisplayPolicy=");
        sb.append(this.mPolicy.isImeHiddenByDisplayPolicy());
        printWriterPrinter.println(sb.toString());
        printWriterPrinter.println("  mInputShown=" + this.mInputShown);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    static class ImeVisibilityPolicy {
        private boolean mA11yRequestingNoSoftKeyboard;
        private boolean mImeHiddenByDisplayPolicy;
        private boolean mPendingA11yRequestingHideKeyboard;

        ImeVisibilityPolicy() {
        }

        void setImeHiddenByDisplayPolicy(boolean z) {
            this.mImeHiddenByDisplayPolicy = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isImeHiddenByDisplayPolicy() {
            return this.mImeHiddenByDisplayPolicy;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setA11yRequestNoSoftKeyboard(int i) {
            boolean z = (i & 3) == 1;
            this.mA11yRequestingNoSoftKeyboard = z;
            if (z) {
                this.mPendingA11yRequestingHideKeyboard = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isA11yRequestNoSoftKeyboard() {
            return this.mA11yRequestingNoSoftKeyboard;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImeVisibilityPolicy getImePolicy() {
        return this.mPolicy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ImeTargetWindowState {
        private final boolean mHasFocusedEditor;
        private int mImeDisplayId;
        private final boolean mImeFocusChanged;
        private final boolean mIsStartInputByGainFocus;
        private IBinder mRequestImeToken;
        private boolean mRequestedImeVisible;
        private final int mSoftInputModeState;
        private final int mToolType;
        private final int mWindowFlags;

        ImeTargetWindowState(int i, int i2, boolean z, boolean z2, boolean z3) {
            this(i, i2, z, z2, z3, 0);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ImeTargetWindowState(int i, int i2, boolean z, boolean z2, boolean z3, int i3) {
            this.mImeDisplayId = 0;
            this.mSoftInputModeState = i;
            this.mWindowFlags = i2;
            this.mImeFocusChanged = z;
            this.mHasFocusedEditor = z2;
            this.mIsStartInputByGainFocus = z3;
            this.mToolType = i3;
        }

        boolean hasImeFocusChanged() {
            return this.mImeFocusChanged;
        }

        boolean hasEditorFocused() {
            return this.mHasFocusedEditor;
        }

        boolean isStartInputByGainFocus() {
            return this.mIsStartInputByGainFocus;
        }

        int getSoftInputModeState() {
            return this.mSoftInputModeState;
        }

        int getWindowFlags() {
            return this.mWindowFlags;
        }

        int getToolType() {
            return this.mToolType;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setImeDisplayId(int i) {
            this.mImeDisplayId = i;
        }

        int getImeDisplayId() {
            return this.mImeDisplayId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRequestedImeVisible(boolean z) {
            this.mRequestedImeVisible = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isRequestedImeVisible() {
            return this.mRequestedImeVisible;
        }

        void setRequestImeToken(IBinder iBinder) {
            this.mRequestImeToken = iBinder;
        }

        IBinder getRequestImeToken() {
            return this.mRequestImeToken;
        }

        public String toString() {
            return "ImeTargetWindowState{ imeToken " + this.mRequestImeToken + " imeFocusChanged " + this.mImeFocusChanged + " hasEditorFocused " + this.mHasFocusedEditor + " requestedImeVisible " + this.mRequestedImeVisible + " imeDisplayId " + this.mImeDisplayId + " softInputModeState " + InputMethodDebug.softInputModeToString(this.mSoftInputModeState) + " isStartInputByGainFocus " + this.mIsStartInputByGainFocus + "}";
        }
    }
}
