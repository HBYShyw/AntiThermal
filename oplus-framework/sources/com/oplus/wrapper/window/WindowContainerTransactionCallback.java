package com.oplus.wrapper.window;

import android.view.SurfaceControl;

/* loaded from: classes.dex */
public abstract class WindowContainerTransactionCallback {
    private final WindowContainerTransactionCallbackInner mInnerWindow = new WindowContainerTransactionCallbackInner();

    public abstract void onTransactionReady(int i, SurfaceControl.Transaction transaction);

    /* loaded from: classes.dex */
    private class WindowContainerTransactionCallbackInner extends android.window.WindowContainerTransactionCallback {
        private WindowContainerTransactionCallbackInner() {
        }

        public void onTransactionReady(int id, SurfaceControl.Transaction t) {
            onTransactionReady(id, t);
        }
    }

    public android.window.WindowContainerTransactionCallback get() {
        return this.mInnerWindow;
    }
}
