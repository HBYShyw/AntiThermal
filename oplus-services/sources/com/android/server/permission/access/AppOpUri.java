package com.android.server.permission.access;

import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AccessUri.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AppOpUri extends AccessUri {

    @NotNull
    public static final Companion Companion = new Companion(null);

    @NotNull
    private final String appOpName;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof AppOpUri) && Intrinsics.areEqual(this.appOpName, ((AppOpUri) obj).appOpName);
    }

    public int hashCode() {
        return this.appOpName.hashCode();
    }

    @NotNull
    public final String getAppOpName() {
        return this.appOpName;
    }

    @NotNull
    public String toString() {
        return getScheme() + ":///" + this.appOpName;
    }

    /* compiled from: AccessUri.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
