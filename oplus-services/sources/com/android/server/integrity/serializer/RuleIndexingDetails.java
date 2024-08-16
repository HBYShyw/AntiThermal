package com.android.server.integrity.serializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RuleIndexingDetails {
    static final int APP_CERTIFICATE_INDEXED = 2;
    static final String DEFAULT_RULE_KEY = "N/A";
    static final int NOT_INDEXED = 0;
    static final int PACKAGE_NAME_INDEXED = 1;
    private int mIndexType;
    private String mRuleKey;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface IndexType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RuleIndexingDetails(int i) {
        this.mIndexType = i;
        this.mRuleKey = DEFAULT_RULE_KEY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RuleIndexingDetails(int i, String str) {
        this.mIndexType = i;
        this.mRuleKey = str;
    }

    public int getIndexType() {
        return this.mIndexType;
    }

    public String getRuleKey() {
        return this.mRuleKey;
    }
}
