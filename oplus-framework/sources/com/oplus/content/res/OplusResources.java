package com.oplus.content.res;

import android.content.res.Resources;

/* loaded from: classes.dex */
public class OplusResources {
    private final Resources mResources;

    public OplusResources(Resources resources) {
        this.mResources = resources;
    }

    public boolean getThemeChanged() {
        return this.mResources.mResourcesExt.getThemeChanged();
    }

    public void setIsThemeChanged(boolean changed) {
        this.mResources.mResourcesExt.setIsThemeChanged(changed);
    }
}
