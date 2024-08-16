package com.oplus.wrapper.app;

/* loaded from: classes.dex */
public class Dialog {
    private final android.app.Dialog mDialog;

    public Dialog(android.app.Dialog dialog) {
        this.mDialog = dialog;
    }

    public void setDismissOverride(Runnable override) {
        this.mDialog.setDismissOverride(override);
    }
}
