package com.oplus.wrapper.app;

/* loaded from: classes.dex */
public class UiModeManager {
    private final android.app.UiModeManager mUiModeManager;

    public UiModeManager(android.app.UiModeManager uiModeManager) {
        this.mUiModeManager = uiModeManager;
    }

    public boolean setNightModeActivated(boolean active) {
        return this.mUiModeManager.setNightModeActivated(active);
    }
}
