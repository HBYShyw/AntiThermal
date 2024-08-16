package com.android.server.locales;

import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AppLocaleChangedAtomRecord {
    private static final String DEFAULT_PREFIX = "default-";
    final int mCallingUid;
    String mNewLocales;
    String mPrevLocales;
    int mTargetUid = -1;
    int mStatus = 0;
    int mCaller = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppLocaleChangedAtomRecord(int i) {
        this.mNewLocales = DEFAULT_PREFIX;
        this.mPrevLocales = DEFAULT_PREFIX;
        this.mCallingUid = i;
        Locale locale = Locale.getDefault();
        if (locale != null) {
            this.mNewLocales = DEFAULT_PREFIX + locale.toLanguageTag();
            this.mPrevLocales = DEFAULT_PREFIX + locale.toLanguageTag();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNewLocales(String str) {
        this.mNewLocales = convertEmptyLocales(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTargetUid(int i) {
        this.mTargetUid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPrevLocales(String str) {
        this.mPrevLocales = convertEmptyLocales(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStatus(int i) {
        this.mStatus = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCaller(int i) {
        this.mCaller = i;
    }

    private String convertEmptyLocales(String str) {
        Locale locale;
        if (!"".equals(str) || (locale = Locale.getDefault()) == null) {
            return str;
        }
        return DEFAULT_PREFIX + locale.toLanguageTag();
    }
}
