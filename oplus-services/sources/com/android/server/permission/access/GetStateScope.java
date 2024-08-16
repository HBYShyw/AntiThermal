package com.android.server.permission.access;

import org.jetbrains.annotations.NotNull;

/* compiled from: AccessState.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GetStateScope {

    @NotNull
    private final AccessState state;

    public GetStateScope(@NotNull AccessState accessState) {
        this.state = accessState;
    }

    @NotNull
    public final AccessState getState() {
        return this.state;
    }
}
