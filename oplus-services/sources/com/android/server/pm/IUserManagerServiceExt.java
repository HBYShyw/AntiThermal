package com.android.server.pm;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.UserManager;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.server.pm.UserManagerService;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IUserManagerServiceExt {
    default boolean checkUserIfNeed(int i) {
        return false;
    }

    default void createUserEnter(long j, String str, String str2, int i, boolean z, int i2) {
    }

    default void createUserExit(long j, String str, String str2, int i, boolean z, int i2, long j2, long j3, long j4, long j5, long j6) {
    }

    default void ensureCanCreateStudyUserOrThrowIfNeeded(int i) throws UserManager.CheckedUserOperationException {
    }

    default int getNextAvailableId(int i) {
        return 0;
    }

    default String[] hookDisallowedPackages(int i, int i2, String[] strArr) {
        return strArr;
    }

    default Set<Integer> hookUsersIdToWrite(Set<Integer> set) {
        return set;
    }

    default void hookUsersUpgraded(SparseArray<UserManagerService.UserData> sparseArray) {
    }

    default void init(Context context) {
    }

    default boolean isCustomUser(int i) {
        return false;
    }

    default boolean isMultiAppUser(int i) {
        return false;
    }

    default void normalizeExternalStorageData(int i) {
    }

    default void onBeforeStartUserExit(int i, long j, long j2, long j3) {
    }

    default void onCreateUserInternal(UserInfo userInfo) {
    }

    default void onMultiAppUserRemoved(Context context, SparseBooleanArray sparseBooleanArray, int i) {
    }

    default void onRemoveUserState(int i) {
    }

    default void onRemoveUserUnchecked(int i) {
    }

    default void ormsCreateUserBoost(int i) {
    }

    default void setUserIsMultiSystem(int i, int i2) {
    }

    default boolean skipCustomUserId(int i) {
        return false;
    }

    default void systemReady() {
    }
}
