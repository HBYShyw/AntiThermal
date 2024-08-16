package com.android.server.om;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class PackageAndUser {
    public final String packageName;
    public final int userId;

    PackageAndUser(String str, int i) {
        this.packageName = str;
        this.userId = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PackageAndUser)) {
            return false;
        }
        PackageAndUser packageAndUser = (PackageAndUser) obj;
        return this.packageName.equals(packageAndUser.packageName) && this.userId == packageAndUser.userId;
    }

    public int hashCode() {
        return ((this.packageName.hashCode() + 31) * 31) + this.userId;
    }

    public String toString() {
        return String.format("PackageAndUser{packageName=%s, userId=%d}", this.packageName, Integer.valueOf(this.userId));
    }
}
