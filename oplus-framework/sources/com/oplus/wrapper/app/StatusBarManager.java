package com.oplus.wrapper.app;

/* loaded from: classes.dex */
public class StatusBarManager {
    private final android.app.StatusBarManager mStatusBarManager;
    public static final int DISABLE_NONE = getDisableNone();
    public static final int DISABLE_EXPAND = getDisableExpand();

    public StatusBarManager(android.app.StatusBarManager statusBarManager) {
        this.mStatusBarManager = statusBarManager;
    }

    public void expandNotificationsPanel() {
        this.mStatusBarManager.expandNotificationsPanel();
    }

    public void collapsePanels() {
        this.mStatusBarManager.collapsePanels();
    }

    public void disable2(int what) {
        this.mStatusBarManager.disable2(what);
    }

    public void disable(int what) {
        this.mStatusBarManager.disable(what);
    }

    public void setDisabledForSetup(boolean disabled) {
        this.mStatusBarManager.setDisabledForSetup(disabled);
    }

    private static int getDisableNone() {
        return 0;
    }

    private static int getDisableExpand() {
        return 65536;
    }
}
