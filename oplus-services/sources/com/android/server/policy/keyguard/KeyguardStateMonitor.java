package com.android.server.policy.keyguard;

import android.app.ActivityManager;
import android.content.Context;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.policy.IKeyguardService;
import com.android.internal.policy.IKeyguardStateCallback;
import com.android.internal.widget.LockPatternUtils;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class KeyguardStateMonitor extends IKeyguardStateCallback.Stub {
    private static final String TAG = "KeyguardStateMonitor";
    private final StateCallback mCallback;
    private final LockPatternUtils mLockPatternUtils;
    private volatile boolean mIsShowing = true;
    private volatile boolean mSimSecure = true;
    private volatile boolean mInputRestricted = true;
    private volatile boolean mTrusted = false;
    private int mCurrentUserId = ActivityManager.getCurrentUser();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface StateCallback {
        void onShowingChanged();

        void onTrustedChanged();
    }

    public KeyguardStateMonitor(Context context, IKeyguardService iKeyguardService, StateCallback stateCallback) {
        this.mLockPatternUtils = new LockPatternUtils(context);
        this.mCallback = stateCallback;
        try {
            iKeyguardService.addStateMonitorCallback(this);
        } catch (RemoteException e) {
            Slog.w(TAG, "Remote Exception", e);
        }
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public boolean isSecure(int i) {
        return this.mLockPatternUtils.isSecure(i) || this.mSimSecure;
    }

    public boolean isInputRestricted() {
        return this.mInputRestricted;
    }

    public boolean isTrusted() {
        return this.mTrusted;
    }

    public int getCurrentUser() {
        return this.mCurrentUserId;
    }

    public void onShowingStateChanged(boolean z, int i) {
        if (i != this.mCurrentUserId) {
            return;
        }
        this.mIsShowing = z;
        this.mCallback.onShowingChanged();
    }

    public void onSimSecureStateChanged(boolean z) {
        this.mSimSecure = z;
    }

    public synchronized void setCurrentUser(int i) {
        this.mCurrentUserId = i;
    }

    public void onInputRestrictedStateChanged(boolean z) {
        this.mInputRestricted = z;
    }

    public void onTrustedChanged(boolean z) {
        this.mTrusted = z;
        this.mCallback.onTrustedChanged();
    }

    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + TAG);
        String str2 = str + "  ";
        printWriter.println(str2 + "mIsShowing=" + this.mIsShowing);
        printWriter.println(str2 + "mSimSecure=" + this.mSimSecure);
        printWriter.println(str2 + "mInputRestricted=" + this.mInputRestricted);
        printWriter.println(str2 + "mTrusted=" + this.mTrusted);
        printWriter.println(str2 + "mCurrentUserId=" + this.mCurrentUserId);
    }
}
