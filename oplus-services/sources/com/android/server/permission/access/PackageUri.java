package com.android.server.permission.access;

import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import com.android.server.slice.SliceClientPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AccessUri.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PackageUri extends AccessUri {

    @NotNull
    public static final Companion Companion = new Companion(null);

    @NotNull
    private final String packageName;
    private final int userId;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PackageUri)) {
            return false;
        }
        PackageUri packageUri = (PackageUri) obj;
        return Intrinsics.areEqual(this.packageName, packageUri.packageName) && this.userId == packageUri.userId;
    }

    public int hashCode() {
        return (this.packageName.hashCode() * 31) + Integer.hashCode(this.userId);
    }

    @NotNull
    public final String getPackageName() {
        return this.packageName;
    }

    public final int getUserId() {
        return this.userId;
    }

    @NotNull
    public String toString() {
        return getScheme() + ":///" + this.packageName + SliceClientPermissions.SliceAuthority.DELIMITER + this.userId;
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
