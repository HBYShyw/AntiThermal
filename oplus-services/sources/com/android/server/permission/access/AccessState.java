package com.android.server.permission.access;

import android.util.SparseArray;
import org.jetbrains.annotations.NotNull;

/* compiled from: AccessState.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AccessState {

    @NotNull
    private final SystemState systemState;

    @NotNull
    private final SparseArray<UserState> userStates;

    private AccessState(SystemState systemState, SparseArray<UserState> sparseArray) {
        this.systemState = systemState;
        this.userStates = sparseArray;
    }

    @NotNull
    public final SystemState getSystemState() {
        return this.systemState;
    }

    @NotNull
    public final SparseArray<UserState> getUserStates() {
        return this.userStates;
    }

    public AccessState() {
        this(new SystemState(), new SparseArray());
    }

    @NotNull
    public final AccessState copy() {
        SystemState copy = this.systemState.copy();
        SparseArray<UserState> clone = this.userStates.clone();
        int size = clone.size();
        for (int i = 0; i < size; i++) {
            clone.setValueAt(i, clone.valueAt(i).copy());
        }
        return new AccessState(copy, clone);
    }
}
