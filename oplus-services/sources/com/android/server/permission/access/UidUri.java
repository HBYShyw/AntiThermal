package com.android.server.permission.access;

import android.os.UserHandle;
import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AccessUri.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UidUri extends AccessUri {

    @NotNull
    public static final Companion Companion = new Companion(null);
    private final int uid;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof UidUri) && this.uid == ((UidUri) obj).uid;
    }

    public int hashCode() {
        return Integer.hashCode(this.uid);
    }

    public final int getUserId() {
        return UserHandle.getUserId(this.uid);
    }

    public final int getAppId() {
        return UserHandle.getAppId(this.uid);
    }

    @NotNull
    public String toString() {
        return getScheme() + ":///" + this.uid;
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
