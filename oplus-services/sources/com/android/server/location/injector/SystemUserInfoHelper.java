package com.android.server.location.injector;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.IActivityManager;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.pm.UserManagerInternal;
import java.io.FileDescriptor;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemUserInfoHelper extends UserInfoHelper {

    @GuardedBy({"this"})
    private IActivityManager mActivityManager;

    @GuardedBy({"this"})
    private ActivityManagerInternal mActivityManagerInternal;
    private final Context mContext;

    @GuardedBy({"this"})
    private UserManager mUserManager;

    @GuardedBy({"this"})
    private UserManagerInternal mUserManagerInternal;

    public SystemUserInfoHelper(Context context) {
        this.mContext = context;
    }

    public synchronized void onSystemReady() {
        UserManagerInternal userManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        Objects.requireNonNull(userManagerInternal);
        this.mUserManagerInternal = userManagerInternal;
        userManagerInternal.addUserVisibilityListener(new UserManagerInternal.UserVisibilityListener() { // from class: com.android.server.location.injector.SystemUserInfoHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.UserManagerInternal.UserVisibilityListener
            public final void onUserVisibilityChanged(int i, boolean z) {
                SystemUserInfoHelper.this.lambda$onSystemReady$0(i, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0(int i, boolean z) {
        dispatchOnVisibleUserChanged(i, z);
    }

    protected final ActivityManagerInternal getActivityManagerInternal() {
        synchronized (this) {
            if (this.mActivityManagerInternal == null) {
                this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            }
        }
        return this.mActivityManagerInternal;
    }

    protected final IActivityManager getActivityManager() {
        synchronized (this) {
            if (this.mActivityManager == null) {
                this.mActivityManager = ActivityManager.getService();
            }
        }
        return this.mActivityManager;
    }

    protected final UserManager getUserManager() {
        synchronized (this) {
            if (this.mUserManager == null) {
                this.mUserManager = (UserManager) this.mContext.getSystemService(UserManager.class);
            }
        }
        return this.mUserManager;
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public int[] getRunningUserIds() {
        IActivityManager activityManager = getActivityManager();
        if (activityManager == null) {
            return new int[0];
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                return activityManager.getRunningUserIds();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public boolean isCurrentUserId(int i) {
        ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return activityManagerInternal.isCurrentProfile(i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public int getCurrentUserId() {
        ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            return -10000;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return activityManagerInternal.getCurrentUserId();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public boolean isVisibleUserId(int i) {
        boolean isUserVisible;
        synchronized (this) {
            Preconditions.checkState(this.mUserManagerInternal != null);
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this) {
                isUserVisible = this.mUserManagerInternal.isUserVisible(i);
            }
            return isUserVisible;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    protected int[] getProfileIds(int i) {
        UserManager userManager = getUserManager();
        Preconditions.checkState(userManager != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return userManager.getEnabledProfileIds(i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.UserInfoHelper
    public void dump(FileDescriptor fileDescriptor, IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        int[] runningUserIds = getRunningUserIds();
        if (runningUserIds.length > 1) {
            indentingPrintWriter.println("running users: u" + Arrays.toString(runningUserIds));
        }
        ActivityManagerInternal activityManagerInternal = getActivityManagerInternal();
        if (activityManagerInternal == null) {
            return;
        }
        int[] currentProfileIds = activityManagerInternal.getCurrentProfileIds();
        indentingPrintWriter.println("current users: u" + Arrays.toString(currentProfileIds));
        UserManager userManager = getUserManager();
        if (userManager != null) {
            for (int i : currentProfileIds) {
                if (userManager.hasUserRestrictionForUser("no_share_location", UserHandle.of(i))) {
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println("u" + i + " restricted");
                    indentingPrintWriter.decreaseIndent();
                }
            }
        }
    }
}
