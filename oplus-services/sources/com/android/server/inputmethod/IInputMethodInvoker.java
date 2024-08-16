package com.android.server.inputmethod;

import android.os.Binder;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.util.Slog;
import android.view.InputChannel;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ImeTracker;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodSubtype;
import android.window.ImeOnBackInvokedDispatcher;
import com.android.internal.inputmethod.IInlineSuggestionsRequestCallback;
import com.android.internal.inputmethod.IInputMethod;
import com.android.internal.inputmethod.IInputMethodPrivilegedOperations;
import com.android.internal.inputmethod.IInputMethodSession;
import com.android.internal.inputmethod.IInputMethodSessionCallback;
import com.android.internal.inputmethod.IRemoteInputConnection;
import com.android.internal.inputmethod.InlineSuggestionsRequestInfo;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class IInputMethodInvoker {
    private static boolean DEBUG = InputMethodManagerService.DEBUG;
    private static final String TAG = "InputMethodManagerService";
    private final IInputMethod mTarget;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IInputMethodInvoker create(IInputMethod iInputMethod) {
        if (iInputMethod == null) {
            return null;
        }
        if (!Binder.isProxy(iInputMethod)) {
            throw new UnsupportedOperationException(iInputMethod + " must have been a BinderProxy.");
        }
        return new IInputMethodInvoker(iInputMethod);
    }

    private static String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace.length <= 4 ? "<bottom of call stack>" : stackTrace[4].getMethodName();
    }

    private static void logRemoteException(RemoteException remoteException) {
        if (DEBUG || !(remoteException instanceof DeadObjectException)) {
            Slog.w(TAG, "IPC failed at IInputMethodInvoker#" + getCallerMethodName(), remoteException);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getBinderIdentityHashCode(IInputMethodInvoker iInputMethodInvoker) {
        if (iInputMethodInvoker == null) {
            return 0;
        }
        return System.identityHashCode(iInputMethodInvoker.mTarget);
    }

    private IInputMethodInvoker(IInputMethod iInputMethod) {
        this.mTarget = iInputMethod;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder asBinder() {
        return this.mTarget.asBinder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initializeInternal(IBinder iBinder, IInputMethodPrivilegedOperations iInputMethodPrivilegedOperations, int i) {
        IInputMethod.InitParams initParams = new IInputMethod.InitParams();
        initParams.token = iBinder;
        initParams.privilegedOperations = iInputMethodPrivilegedOperations;
        initParams.navigationBarFlags = i;
        try {
            this.mTarget.initializeInternal(initParams);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCreateInlineSuggestionsRequest(InlineSuggestionsRequestInfo inlineSuggestionsRequestInfo, IInlineSuggestionsRequestCallback iInlineSuggestionsRequestCallback) {
        try {
            this.mTarget.onCreateInlineSuggestionsRequest(inlineSuggestionsRequestInfo, iInlineSuggestionsRequestCallback);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bindInput(InputBinding inputBinding) {
        try {
            this.mTarget.bindInput(inputBinding);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unbindInput() {
        try {
            this.mTarget.unbindInput();
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startInput(IBinder iBinder, IRemoteInputConnection iRemoteInputConnection, EditorInfo editorInfo, boolean z, int i, ImeOnBackInvokedDispatcher imeOnBackInvokedDispatcher) {
        IInputMethod.StartInputParams startInputParams = new IInputMethod.StartInputParams();
        startInputParams.startInputToken = iBinder;
        startInputParams.remoteInputConnection = iRemoteInputConnection;
        startInputParams.editorInfo = editorInfo;
        startInputParams.restarting = z;
        startInputParams.navigationBarFlags = i;
        startInputParams.imeDispatcher = imeOnBackInvokedDispatcher;
        try {
            this.mTarget.startInput(startInputParams);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onNavButtonFlagsChanged(int i) {
        try {
            this.mTarget.onNavButtonFlagsChanged(i);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createSession(InputChannel inputChannel, IInputMethodSessionCallback iInputMethodSessionCallback) {
        try {
            this.mTarget.createSession(inputChannel, iInputMethodSessionCallback);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSessionEnabled(IInputMethodSession iInputMethodSession, boolean z) {
        try {
            this.mTarget.setSessionEnabled(iInputMethodSession, z);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean showSoftInput(IBinder iBinder, ImeTracker.Token token, int i, ResultReceiver resultReceiver) {
        try {
            this.mTarget.showSoftInput(iBinder, token, i, resultReceiver);
            return true;
        } catch (RemoteException e) {
            logRemoteException(e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hideSoftInput(IBinder iBinder, ImeTracker.Token token, int i, ResultReceiver resultReceiver) {
        try {
            this.mTarget.hideSoftInput(iBinder, token, i, resultReceiver);
            return true;
        } catch (RemoteException e) {
            logRemoteException(e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateEditorToolType(int i) {
        try {
            this.mTarget.updateEditorToolType(i);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeInputMethodSubtype(InputMethodSubtype inputMethodSubtype) {
        try {
            this.mTarget.changeInputMethodSubtype(inputMethodSubtype);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void canStartStylusHandwriting(int i) {
        try {
            this.mTarget.canStartStylusHandwriting(i);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startStylusHandwriting(int i, InputChannel inputChannel, List<MotionEvent> list) {
        try {
            this.mTarget.startStylusHandwriting(i, inputChannel, list);
            return true;
        } catch (RemoteException e) {
            logRemoteException(e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initInkWindow() {
        try {
            this.mTarget.initInkWindow();
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishStylusHandwriting() {
        try {
            this.mTarget.finishStylusHandwriting();
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeStylusHandwritingWindow() {
        try {
            this.mTarget.removeStylusHandwritingWindow();
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStylusWindowIdleTimeoutForTest(long j) {
        try {
            this.mTarget.setStylusWindowIdleTimeoutForTest(j);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }
}
