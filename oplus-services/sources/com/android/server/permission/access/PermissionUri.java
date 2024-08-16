package com.android.server.permission.access;

import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AccessUri.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PermissionUri extends AccessUri {

    @NotNull
    public static final Companion Companion = new Companion(null);

    @NotNull
    private final String permissionName;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof PermissionUri) && Intrinsics.areEqual(this.permissionName, ((PermissionUri) obj).permissionName);
    }

    public int hashCode() {
        return this.permissionName.hashCode();
    }

    @NotNull
    public final String getPermissionName() {
        return this.permissionName;
    }

    @NotNull
    public String toString() {
        return getScheme() + ":///" + this.permissionName;
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
