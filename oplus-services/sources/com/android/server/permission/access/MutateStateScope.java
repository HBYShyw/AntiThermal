package com.android.server.permission.access;

import org.jetbrains.annotations.NotNull;

/* compiled from: AccessState.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MutateStateScope extends GetStateScope {

    @NotNull
    private final AccessState newState;

    @NotNull
    private final AccessState oldState;

    @NotNull
    public final AccessState getOldState() {
        return this.oldState;
    }

    @NotNull
    public final AccessState getNewState() {
        return this.newState;
    }

    public MutateStateScope(@NotNull AccessState accessState, @NotNull AccessState accessState2) {
        super(accessState2);
        this.oldState = accessState;
        this.newState = accessState2;
    }
}
