package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class WindowInsetsController {
    private final android.view.WindowInsetsController mWindowInsetsController;

    public WindowInsetsController(android.view.WindowInsetsController windowInsetsController) {
        this.mWindowInsetsController = windowInsetsController;
    }

    public void setAnimationsDisabled(boolean disable) {
        this.mWindowInsetsController.setAnimationsDisabled(disable);
    }
}
