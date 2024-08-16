package com.android.server.utils.quota;

import android.util.proto.ProtoOutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class Category {
    public static final Category SINGLE_CATEGORY = new Category("SINGLE");
    private final int mHash;
    private final String mName;

    public Category(String str) {
        this.mName = str;
        this.mHash = str.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Category) {
            return this.mName.equals(((Category) obj).mName);
        }
        return false;
    }

    public int hashCode() {
        return this.mHash;
    }

    public String toString() {
        return "Category{" + this.mName + "}";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, this.mName);
        protoOutputStream.end(start);
    }
}
