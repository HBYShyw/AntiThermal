package com.android.server.wm;

import android.app.admin.DevicePolicyCache;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.pm.UserManagerInternal;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.utils.UserTokenWatcher;
import com.android.server.wm.LockTaskController;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class KeyguardDisableHandler {
    private static final String TAG = "WindowManager";
    private final UserTokenWatcher mAppTokenWatcher;
    private final UserTokenWatcher.Callback mCallback;
    private int mCurrentUser = 0;
    private Injector mInjector;
    private final UserTokenWatcher mSystemTokenWatcher;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface Injector {
        boolean dpmRequiresPassword(int i);

        void enableKeyguard(boolean z);

        int getProfileParentId(int i);

        boolean isKeyguardSecure(int i);
    }

    @VisibleForTesting
    KeyguardDisableHandler(Injector injector, Handler handler) {
        UserTokenWatcher.Callback callback = new UserTokenWatcher.Callback() { // from class: com.android.server.wm.KeyguardDisableHandler.1
            public void acquired(int i) {
                KeyguardDisableHandler.this.updateKeyguardEnabled(i);
            }

            public void released(int i) {
                KeyguardDisableHandler.this.updateKeyguardEnabled(i);
            }
        };
        this.mCallback = callback;
        this.mInjector = injector;
        this.mAppTokenWatcher = new UserTokenWatcher(callback, handler, TAG);
        this.mSystemTokenWatcher = new UserTokenWatcher(callback, handler, TAG);
    }

    public void setCurrentUser(int i) {
        synchronized (this) {
            this.mCurrentUser = i;
            updateKeyguardEnabledLocked(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateKeyguardEnabled(int i) {
        synchronized (this) {
            updateKeyguardEnabledLocked(i);
        }
    }

    private void updateKeyguardEnabledLocked(int i) {
        int i2 = this.mCurrentUser;
        if (i2 == i || i == -1) {
            this.mInjector.enableKeyguard(shouldKeyguardBeEnabled(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableKeyguard(IBinder iBinder, String str, int i, int i2) {
        watcherForCallingUid(iBinder, i).acquire(iBinder, str, this.mInjector.getProfileParentId(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reenableKeyguard(IBinder iBinder, int i, int i2) {
        watcherForCallingUid(iBinder, i).release(iBinder, this.mInjector.getProfileParentId(i2));
    }

    private UserTokenWatcher watcherForCallingUid(IBinder iBinder, int i) {
        if (Process.isApplicationUid(i)) {
            return this.mAppTokenWatcher;
        }
        if (i == 1000 && (iBinder instanceof LockTaskController.LockTaskToken)) {
            return this.mSystemTokenWatcher;
        }
        throw new UnsupportedOperationException("Only apps can use the KeyguardLock API");
    }

    private boolean shouldKeyguardBeEnabled(int i) {
        boolean dpmRequiresPassword = this.mInjector.dpmRequiresPassword(this.mCurrentUser);
        boolean z = false;
        boolean z2 = (dpmRequiresPassword || this.mInjector.isKeyguardSecure(this.mCurrentUser)) ? false : true;
        boolean z3 = !dpmRequiresPassword;
        if ((z2 && this.mAppTokenWatcher.isAcquired(i)) || (z3 && this.mSystemTokenWatcher.isAcquired(i))) {
            z = true;
        }
        return !z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyguardDisableHandler create(Context context, final WindowManagerPolicy windowManagerPolicy, Handler handler) {
        final UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        return new KeyguardDisableHandler(new Injector() { // from class: com.android.server.wm.KeyguardDisableHandler.2
            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public boolean dpmRequiresPassword(int i) {
                return DevicePolicyCache.getInstance().getPasswordQuality(i) != 0;
            }

            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public boolean isKeyguardSecure(int i) {
                return windowManagerPolicy.isKeyguardSecure(i);
            }

            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public int getProfileParentId(int i) {
                return userManagerInternal.getProfileParentId(i);
            }

            @Override // com.android.server.wm.KeyguardDisableHandler.Injector
            public void enableKeyguard(boolean z) {
                windowManagerPolicy.enableKeyguard(z);
            }
        }, handler);
    }
}
