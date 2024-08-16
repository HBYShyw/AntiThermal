package com.android.server.pm.permission;

import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class LegacyPermissionState {
    private final SparseArray<UserState> mUserStates = new SparseArray<>();
    private final SparseBooleanArray mMissing = new SparseBooleanArray();

    public void copyFrom(LegacyPermissionState legacyPermissionState) {
        if (legacyPermissionState == this) {
            return;
        }
        this.mUserStates.clear();
        int size = legacyPermissionState.mUserStates.size();
        for (int i = 0; i < size; i++) {
            this.mUserStates.put(legacyPermissionState.mUserStates.keyAt(i), new UserState(legacyPermissionState.mUserStates.valueAt(i)));
        }
        this.mMissing.clear();
        int size2 = legacyPermissionState.mMissing.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mMissing.put(legacyPermissionState.mMissing.keyAt(i2), legacyPermissionState.mMissing.valueAt(i2));
        }
    }

    public void reset() {
        this.mUserStates.clear();
        this.mMissing.clear();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || LegacyPermissionState.class != obj.getClass()) {
            return false;
        }
        LegacyPermissionState legacyPermissionState = (LegacyPermissionState) obj;
        int size = this.mUserStates.size();
        if (size != legacyPermissionState.mUserStates.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            int keyAt = this.mUserStates.keyAt(i);
            if (!Objects.equals(this.mUserStates.get(keyAt), legacyPermissionState.mUserStates.get(keyAt))) {
                return false;
            }
        }
        return Objects.equals(this.mMissing, legacyPermissionState.mMissing);
    }

    public PermissionState getPermissionState(String str, int i) {
        checkUserId(i);
        UserState userState = this.mUserStates.get(i);
        if (userState == null) {
            return null;
        }
        return userState.getPermissionState(str);
    }

    public void putPermissionState(PermissionState permissionState, int i) {
        checkUserId(i);
        UserState userState = this.mUserStates.get(i);
        if (userState == null) {
            userState = new UserState();
            this.mUserStates.put(i, userState);
        }
        userState.putPermissionState(permissionState);
    }

    public boolean hasPermissionState(Collection<String> collection) {
        int size = this.mUserStates.size();
        for (int i = 0; i < size; i++) {
            UserState valueAt = this.mUserStates.valueAt(i);
            Iterator<String> it = collection.iterator();
            while (it.hasNext()) {
                if (valueAt.getPermissionState(it.next()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<PermissionState> getPermissionStates(int i) {
        checkUserId(i);
        UserState userState = this.mUserStates.get(i);
        if (userState == null) {
            return Collections.emptyList();
        }
        return userState.getPermissionStates();
    }

    public boolean isMissing(int i) {
        checkUserId(i);
        return this.mMissing.get(i);
    }

    public void setMissing(boolean z, int i) {
        checkUserId(i);
        if (z) {
            this.mMissing.put(i, true);
        } else {
            this.mMissing.delete(i);
        }
    }

    private static void checkUserId(int i) {
        if (i >= 0) {
            return;
        }
        throw new IllegalArgumentException("Invalid user ID " + i);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class UserState {
        private final ArrayMap<String, PermissionState> mPermissionStates = new ArrayMap<>();

        public UserState() {
        }

        public UserState(UserState userState) {
            int size = userState.mPermissionStates.size();
            for (int i = 0; i < size; i++) {
                this.mPermissionStates.put(userState.mPermissionStates.keyAt(i), new PermissionState(userState.mPermissionStates.valueAt(i)));
            }
        }

        public PermissionState getPermissionState(String str) {
            return this.mPermissionStates.get(str);
        }

        public void putPermissionState(PermissionState permissionState) {
            this.mPermissionStates.put(permissionState.getName(), permissionState);
        }

        public Collection<PermissionState> getPermissionStates() {
            return Collections.unmodifiableCollection(this.mPermissionStates.values());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class PermissionState {
        private final int mFlags;
        private final boolean mGranted;
        private final String mName;
        private final boolean mRuntime;

        public PermissionState(String str, boolean z, boolean z2, int i) {
            this.mName = str;
            this.mRuntime = z;
            this.mGranted = z2;
            this.mFlags = i;
        }

        private PermissionState(PermissionState permissionState) {
            this.mName = permissionState.mName;
            this.mRuntime = permissionState.mRuntime;
            this.mGranted = permissionState.mGranted;
            this.mFlags = permissionState.mFlags;
        }

        public String getName() {
            return this.mName;
        }

        public boolean isRuntime() {
            return this.mRuntime;
        }

        public boolean isGranted() {
            return this.mGranted;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || PermissionState.class != obj.getClass()) {
                return false;
            }
            PermissionState permissionState = (PermissionState) obj;
            return this.mRuntime == permissionState.mRuntime && this.mGranted == permissionState.mGranted && this.mFlags == permissionState.mFlags && Objects.equals(this.mName, permissionState.mName);
        }

        public int hashCode() {
            return Objects.hash(this.mName, Boolean.valueOf(this.mRuntime), Boolean.valueOf(this.mGranted), Integer.valueOf(this.mFlags));
        }
    }
}
