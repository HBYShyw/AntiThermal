package com.oplus.oms.split.core.splitinstall;

import java.util.List;

/* loaded from: classes.dex */
public class OplusSplitInstallRequest {
    private final List<String> mModuleNames;

    protected OplusSplitInstallRequest(List<String> moduleNames) {
        this.mModuleNames = moduleNames;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<String> getModuleNames() {
        return this.mModuleNames;
    }

    public String toString() {
        return "SplitInstallRequest{modulesNames=" + this.mModuleNames + "}";
    }
}
