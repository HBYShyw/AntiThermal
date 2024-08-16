package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class AccessibilityManager {
    android.view.accessibility.AccessibilityManager mAccessibilityManager;

    public AccessibilityManager(android.view.accessibility.AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }

    public boolean isHighTextContrastEnabled() {
        return this.mAccessibilityManager.isHighTextContrastEnabled();
    }
}
