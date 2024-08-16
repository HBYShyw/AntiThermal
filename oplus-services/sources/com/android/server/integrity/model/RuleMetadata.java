package com.android.server.integrity.model;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RuleMetadata {
    private final String mRuleProvider;
    private final String mVersion;

    public RuleMetadata(String str, String str2) {
        this.mRuleProvider = str;
        this.mVersion = str2;
    }

    public String getRuleProvider() {
        return this.mRuleProvider;
    }

    public String getVersion() {
        return this.mVersion;
    }
}
