package com.android.server.pm;

import android.content.pm.UserInfo;
import android.content.pm.UserProperties;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.UserManager;
import android.util.DebugUtils;
import com.android.internal.annotations.Keep;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class UserManagerInternal {
    public static final int OWNER_TYPE_DEVICE_OWNER = 0;
    public static final int OWNER_TYPE_NO_OWNER = 3;
    public static final int OWNER_TYPE_PROFILE_OWNER = 1;
    public static final int OWNER_TYPE_PROFILE_OWNER_OF_ORGANIZATION_OWNED_DEVICE = 2;
    private static final String PREFIX_USER_ASSIGNMENT_RESULT = "USER_ASSIGNMENT_RESULT_";
    private static final String PREFIX_USER_START_MODE = "USER_START_MODE_";

    @Keep
    public static final int USER_ASSIGNMENT_RESULT_FAILURE = -1;

    @Keep
    public static final int USER_ASSIGNMENT_RESULT_SUCCESS_ALREADY_VISIBLE = 3;

    @Keep
    public static final int USER_ASSIGNMENT_RESULT_SUCCESS_INVISIBLE = 2;

    @Keep
    public static final int USER_ASSIGNMENT_RESULT_SUCCESS_VISIBLE = 1;

    @Keep
    public static final int USER_START_MODE_BACKGROUND = 2;

    @Keep
    public static final int USER_START_MODE_BACKGROUND_VISIBLE = 3;

    @Keep
    public static final int USER_START_MODE_FOREGROUND = 1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface OwnerType {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UserAssignmentResult {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface UserLifecycleListener {
        default void onUserCreated(UserInfo userInfo, Object obj) {
        }

        default void onUserRemoved(UserInfo userInfo) {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface UserRestrictionsListener {
        void onUserRestrictionsChanged(int i, Bundle bundle, Bundle bundle2);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UserStartMode {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface UserVisibilityListener {
        void onUserVisibilityChanged(int i, boolean z);
    }

    public abstract void addUserLifecycleListener(UserLifecycleListener userLifecycleListener);

    public abstract void addUserRestrictionsListener(UserRestrictionsListener userRestrictionsListener);

    public abstract void addUserVisibilityListener(UserVisibilityListener userVisibilityListener);

    @UserAssignmentResult
    public abstract int assignUserToDisplayOnStart(int i, int i2, @UserStartMode int i3, int i4);

    public abstract boolean assignUserToExtraDisplay(int i, int i2);

    public abstract UserInfo createUserEvenWhenDisallowed(String str, String str2, int i, String[] strArr, Object obj) throws UserManager.CheckedUserOperationException;

    public abstract boolean exists(int i);

    public abstract int getBootUser(boolean z) throws UserManager.CheckedUserOperationException;

    public abstract int[] getDisplaysAssignedToUser(int i);

    public abstract int getMainDisplayAssignedToUser(int i);

    public abstract int getMainUserId();

    public abstract int[] getProfileIds(int i, boolean z);

    public abstract int getProfileParentId(int i);

    public abstract int getUserAssignedToDisplay(int i);

    public abstract int[] getUserIds();

    public abstract UserInfo getUserInfo(int i);

    public abstract UserInfo[] getUserInfos();

    public abstract UserProperties getUserProperties(int i);

    public abstract boolean getUserRestriction(int i, String str);

    public abstract int[] getUserTypesForStatsd(int[] iArr);

    public abstract List<UserInfo> getUsers(boolean z);

    public abstract List<UserInfo> getUsers(boolean z, boolean z2, boolean z3);

    public abstract boolean hasUserRestriction(String str, int i);

    @Deprecated
    public abstract boolean isDeviceManaged();

    public abstract boolean isProfileAccessible(int i, int i2, String str, boolean z);

    public abstract boolean isSettingRestrictedForUser(String str, int i, String str2, int i2);

    public abstract boolean isUserInitialized(int i);

    @Deprecated
    public abstract boolean isUserManaged(int i);

    public abstract boolean isUserRunning(int i);

    public abstract boolean isUserUnlocked(int i);

    public abstract boolean isUserUnlockingOrUnlocked(int i);

    public abstract boolean isUserVisible(int i);

    public abstract boolean isUserVisible(int i, int i2);

    public abstract void onEphemeralUserStop(int i);

    public abstract void onSystemUserVisibilityChanged(boolean z);

    public abstract void removeAllUsers();

    public abstract boolean removeUserEvenWhenDisallowed(int i);

    public abstract void removeUserLifecycleListener(UserLifecycleListener userLifecycleListener);

    public abstract void removeUserRestrictionsListener(UserRestrictionsListener userRestrictionsListener);

    public abstract void removeUserState(int i);

    public abstract void removeUserVisibilityListener(UserVisibilityListener userVisibilityListener);

    public abstract void setDefaultCrossProfileIntentFilters(int i, int i2);

    @Deprecated
    public abstract void setDeviceManaged(boolean z);

    public abstract void setDevicePolicyUserRestrictions(int i, Bundle bundle, RestrictionsSet restrictionsSet, boolean z);

    public abstract void setForceEphemeralUsers(boolean z);

    public abstract void setUserIcon(int i, Bitmap bitmap);

    @Deprecated
    public abstract void setUserManaged(int i, boolean z);

    public abstract void setUserRestriction(int i, String str, boolean z);

    public abstract void setUserState(int i, int i2);

    public abstract boolean shouldIgnorePrepareStorageErrors(int i);

    public abstract void unassignUserFromDisplayOnStop(int i);

    public abstract boolean unassignUserFromExtraDisplay(int i, int i2);

    public static String userAssignmentResultToString(@UserAssignmentResult int i) {
        return DebugUtils.constantToString(UserManagerInternal.class, PREFIX_USER_ASSIGNMENT_RESULT, i);
    }

    public static String userStartModeToString(@UserStartMode int i) {
        return DebugUtils.constantToString(UserManagerInternal.class, PREFIX_USER_START_MODE, i);
    }
}
