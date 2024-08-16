package com.oplus.epona.provider;

/* loaded from: classes.dex */
public class ProviderMethodInfo {
    private String[] mAllAppOp;
    private String[] mAnyAppOp;
    private String mMethodName;
    private String[] mMethodParams;
    private String mSpecifyAppOp;

    public ProviderMethodInfo(String str, String[] strArr, String str2, String[] strArr2, String[] strArr3) {
        this.mMethodName = str;
        this.mMethodParams = strArr;
        this.mSpecifyAppOp = str2;
        this.mAnyAppOp = strArr2;
        this.mAllAppOp = strArr3;
    }

    public String[] getAllAppOp() {
        return this.mAllAppOp;
    }

    public String[] getAnyAppOp() {
        return this.mAnyAppOp;
    }

    public String getMethodName() {
        return this.mMethodName;
    }

    public String[] getMethodParams() {
        return this.mMethodParams;
    }

    public String getSpecifyAppOp() {
        return this.mSpecifyAppOp;
    }

    public boolean hasAppOpStrAnnotation() {
        return !this.mSpecifyAppOp.isEmpty() || this.mAnyAppOp.length > 0 || this.mAllAppOp.length > 0;
    }
}
