package com.oplus.wrapper.media.permission;

/* loaded from: classes.dex */
public class Identity {
    private final android.media.permission.Identity mIdentity = new android.media.permission.Identity();

    public void setPackageName(String packageName) {
        this.mIdentity.packageName = packageName;
    }

    public String getPackageName() {
        return this.mIdentity.packageName;
    }

    public android.media.permission.Identity getIdentity() {
        return this.mIdentity;
    }

    public void setPid(int pid) {
        this.mIdentity.pid = pid;
    }

    public void setUid(int uid) {
        this.mIdentity.uid = uid;
    }
}
