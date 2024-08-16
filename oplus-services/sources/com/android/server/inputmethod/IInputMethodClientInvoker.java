package com.android.server.inputmethod;

import android.os.Binder;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Slog;
import android.view.InputChannel;
import com.android.internal.inputmethod.IInputMethodClient;
import com.android.internal.inputmethod.InputBindResult;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class IInputMethodClientInvoker {
    private static boolean DEBUG = InputMethodManagerService.DEBUG;
    private static final String TAG = "InputMethodManagerService";
    private final Handler mHandler;
    private final boolean mIsProxy;
    private final IInputMethodClient mTarget;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IInputMethodClientInvoker create(IInputMethodClient iInputMethodClient, Handler handler) {
        if (iInputMethodClient == null) {
            return null;
        }
        boolean isProxy = Binder.isProxy(iInputMethodClient);
        if (isProxy) {
            handler = null;
        }
        return new IInputMethodClientInvoker(iInputMethodClient, isProxy, handler);
    }

    private IInputMethodClientInvoker(IInputMethodClient iInputMethodClient, boolean z, Handler handler) {
        this.mTarget = iInputMethodClient;
        this.mIsProxy = z;
        this.mHandler = handler;
    }

    private static String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace.length <= 4 ? "<bottom of call stack>" : stackTrace[4].getMethodName();
    }

    private static void logRemoteException(RemoteException remoteException) {
        if (DEBUG || !(remoteException instanceof DeadObjectException)) {
            Slog.w(TAG, "IPC failed at IInputMethodClientInvoker#" + getCallerMethodName(), remoteException);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBindMethod(final InputBindResult inputBindResult) {
        if (this.mIsProxy) {
            lambda$onBindMethod$0(inputBindResult);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$onBindMethod$0(inputBindResult);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onBindMethodInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onBindMethod$0(InputBindResult inputBindResult) {
        InputChannel inputChannel;
        try {
            try {
                this.mTarget.onBindMethod(inputBindResult);
                inputChannel = inputBindResult.channel;
                if (inputChannel == null || !this.mIsProxy) {
                    return;
                }
            } catch (RemoteException e) {
                logRemoteException(e);
                inputChannel = inputBindResult.channel;
                if (inputChannel == null || !this.mIsProxy) {
                    return;
                }
            }
            inputChannel.dispose();
        } catch (Throwable th) {
            InputChannel inputChannel2 = inputBindResult.channel;
            if (inputChannel2 != null && this.mIsProxy) {
                inputChannel2.dispose();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBindAccessibilityService(final InputBindResult inputBindResult, final int i) {
        if (this.mIsProxy) {
            lambda$onBindAccessibilityService$1(inputBindResult, i);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$onBindAccessibilityService$1(inputBindResult, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onBindAccessibilityServiceInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onBindAccessibilityService$1(InputBindResult inputBindResult, int i) {
        InputChannel inputChannel;
        try {
            try {
                this.mTarget.onBindAccessibilityService(inputBindResult, i);
                inputChannel = inputBindResult.channel;
                if (inputChannel == null || !this.mIsProxy) {
                    return;
                }
            } catch (RemoteException e) {
                logRemoteException(e);
                inputChannel = inputBindResult.channel;
                if (inputChannel == null || !this.mIsProxy) {
                    return;
                }
            }
            inputChannel.dispose();
        } catch (Throwable th) {
            InputChannel inputChannel2 = inputBindResult.channel;
            if (inputChannel2 != null && this.mIsProxy) {
                inputChannel2.dispose();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUnbindMethod(final int i, final int i2) {
        if (this.mIsProxy) {
            lambda$onUnbindMethod$2(i, i2);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$onUnbindMethod$2(i, i2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onUnbindMethodInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnbindMethod$2(int i, int i2) {
        try {
            this.mTarget.onUnbindMethod(i, i2);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUnbindAccessibilityService(final int i, final int i2) {
        if (this.mIsProxy) {
            lambda$onUnbindAccessibilityService$3(i, i2);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$onUnbindAccessibilityService$3(i, i2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onUnbindAccessibilityServiceInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnbindAccessibilityService$3(int i, int i2) {
        try {
            this.mTarget.onUnbindAccessibilityService(i, i2);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActive(final boolean z, final boolean z2) {
        if (this.mIsProxy) {
            lambda$setActive$4(z, z2);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$setActive$4(z, z2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setActiveInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setActive$4(boolean z, boolean z2) {
        try {
            this.mTarget.setActive(z, z2);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInteractive(final boolean z, final boolean z2) {
        if (this.mIsProxy) {
            lambda$setInteractive$5(z, z2);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$setInteractive$5(z, z2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setInteractiveInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setInteractive$5(boolean z, boolean z2) {
        try {
            this.mTarget.setInteractive(z, z2);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleStartInputIfNecessary(final boolean z) {
        if (this.mIsProxy) {
            lambda$scheduleStartInputIfNecessary$6(z);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$scheduleStartInputIfNecessary$6(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: scheduleStartInputIfNecessaryInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleStartInputIfNecessary$6(boolean z) {
        try {
            this.mTarget.scheduleStartInputIfNecessary(z);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportFullscreenMode(final boolean z) {
        if (this.mIsProxy) {
            lambda$reportFullscreenMode$7(z);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$reportFullscreenMode$7(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: reportFullscreenModeInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$reportFullscreenMode$7(boolean z) {
        try {
            this.mTarget.reportFullscreenMode(z);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateVirtualDisplayToScreenMatrix(final int i, final float[] fArr) {
        if (this.mIsProxy) {
            lambda$updateVirtualDisplayToScreenMatrix$8(i, fArr);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$updateVirtualDisplayToScreenMatrix$8(i, fArr);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateVirtualDisplayToScreenMatrixInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$updateVirtualDisplayToScreenMatrix$8(int i, float[] fArr) {
        try {
            this.mTarget.updateVirtualDisplayToScreenMatrix(i, fArr);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImeTraceEnabled(final boolean z) {
        if (this.mIsProxy) {
            lambda$setImeTraceEnabled$9(z);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$setImeTraceEnabled$9(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setImeTraceEnabledInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setImeTraceEnabled$9(boolean z) {
        try {
            this.mTarget.setImeTraceEnabled(z);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void throwExceptionFromSystem(final String str) {
        if (this.mIsProxy) {
            lambda$throwExceptionFromSystem$10(str);
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    IInputMethodClientInvoker.this.lambda$throwExceptionFromSystem$10(str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: throwExceptionFromSystemInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$throwExceptionFromSystem$10(String str) {
        try {
            this.mTarget.throwExceptionFromSystem(str);
        } catch (RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder asBinder() {
        return this.mTarget.asBinder();
    }
}
