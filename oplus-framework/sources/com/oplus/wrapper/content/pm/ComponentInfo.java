package com.oplus.wrapper.content.pm;

import android.content.ComponentName;

/* loaded from: classes.dex */
public class ComponentInfo {
    private final android.content.pm.ComponentInfo mComponentInfo;

    public ComponentInfo(android.content.pm.ComponentInfo componentInfo) {
        this.mComponentInfo = componentInfo;
    }

    public ComponentName getComponentName() {
        return this.mComponentInfo.getComponentName();
    }
}
