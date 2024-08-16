package com.oplus.wrapper.view.inputmethod;

/* loaded from: classes.dex */
public class InputMethodManager {
    private final android.view.inputmethod.InputMethodManager mInputMethodManager;

    public InputMethodManager(android.view.inputmethod.InputMethodManager inputMethodManager) {
        this.mInputMethodManager = inputMethodManager;
    }

    public int getInputMethodWindowVisibleHeight() {
        return this.mInputMethodManager.getInputMethodWindowVisibleHeight();
    }
}
