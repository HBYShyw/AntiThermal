package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ApexSystemServiceInfo implements Comparable<ApexSystemServiceInfo> {
    final int mInitOrder;
    final String mJarPath;
    final String mName;

    public ApexSystemServiceInfo(String str, String str2, int i) {
        this.mName = str;
        this.mJarPath = str2;
        this.mInitOrder = i;
    }

    public String getName() {
        return this.mName;
    }

    public String getJarPath() {
        return this.mJarPath;
    }

    public int getInitOrder() {
        return this.mInitOrder;
    }

    @Override // java.lang.Comparable
    public int compareTo(ApexSystemServiceInfo apexSystemServiceInfo) {
        int i = this.mInitOrder;
        int i2 = apexSystemServiceInfo.mInitOrder;
        if (i == i2) {
            return this.mName.compareTo(apexSystemServiceInfo.mName);
        }
        return -Integer.compare(i, i2);
    }
}
