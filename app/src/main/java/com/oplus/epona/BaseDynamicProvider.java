package com.oplus.epona;

import android.content.Context;

/* loaded from: classes.dex */
public abstract class BaseDynamicProvider implements DynamicProvider {
    private final String mPackageName;

    public BaseDynamicProvider(String str) {
        this.mPackageName = str;
    }

    private String buildName(String str) {
        return this.mPackageName + "." + str;
    }

    @Override // com.oplus.epona.DynamicProvider
    public String getName() {
        return buildName(getProviderName());
    }

    protected abstract String getProviderName();

    public BaseDynamicProvider(Context context) {
        this(context.getPackageName());
    }
}
