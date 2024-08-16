package com.android.server.pm.permission;

import android.util.SparseArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DevicePermissionState {
    private final SparseArray<UserPermissionState> mUserStates = new SparseArray<>();

    public UserPermissionState getUserState(int i) {
        return this.mUserStates.get(i);
    }

    public UserPermissionState getOrCreateUserState(int i) {
        UserPermissionState userPermissionState = this.mUserStates.get(i);
        if (userPermissionState != null) {
            return userPermissionState;
        }
        UserPermissionState userPermissionState2 = new UserPermissionState();
        this.mUserStates.put(i, userPermissionState2);
        return userPermissionState2;
    }

    public void removeUserState(int i) {
        this.mUserStates.delete(i);
    }

    public int[] getUserIds() {
        int size = this.mUserStates.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = this.mUserStates.keyAt(i);
        }
        return iArr;
    }
}
