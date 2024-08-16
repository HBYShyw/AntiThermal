package com.oplus.wrapper.window;

import android.graphics.Rect;
import android.os.Parcelable;
import android.util.Log;

/* loaded from: classes.dex */
public class WindowContainerTransaction {
    private static final String TAG = "WindowContainerTransaction";
    private final android.window.WindowContainerTransaction mWindowContainerTransaction;

    public WindowContainerTransaction() {
        this.mWindowContainerTransaction = new android.window.WindowContainerTransaction();
    }

    public WindowContainerTransaction(android.window.WindowContainerTransaction windowContainerTransaction) {
        this.mWindowContainerTransaction = windowContainerTransaction;
    }

    public boolean isEmpty() {
        return this.mWindowContainerTransaction.isEmpty();
    }

    public WindowContainerTransaction reorder(WindowContainerToken child, boolean onTop) {
        this.mWindowContainerTransaction.reorder(child.get(), onTop);
        return this;
    }

    public WindowContainerTransaction setBounds(WindowContainerToken container, Rect bounds) {
        this.mWindowContainerTransaction.setBounds(container.get(), bounds);
        return this;
    }

    public WindowContainerTransaction setHidden(WindowContainerToken container, boolean hidden) {
        this.mWindowContainerTransaction.setHidden(container.get(), hidden);
        return this;
    }

    public WindowContainerTransaction setWindowingMode(WindowContainerToken container, int windowingMode) {
        this.mWindowContainerTransaction.setWindowingMode(container.get(), windowingMode);
        return this;
    }

    public android.window.WindowContainerTransaction get() {
        return this.mWindowContainerTransaction;
    }

    public WindowContainerTransaction restoreTransientOrder(WindowContainerToken container) {
        android.window.WindowContainerToken windowContainerToken = container.get();
        if (windowContainerToken == null) {
            Log.d(TAG, "restoreTransientOrder: getWindowContainerToken is null");
            return null;
        }
        android.window.WindowContainerTransaction transaction = this.mWindowContainerTransaction.restoreTransientOrder(windowContainerToken);
        return new WindowContainerTransaction(transaction);
    }

    public WindowContainerTransaction setDoNotPip(WindowContainerToken container) {
        android.window.WindowContainerToken containerToken = container.get();
        if (containerToken == null) {
            Log.d(TAG, "setDoNotPip: getWindowContainerToken is null");
            return null;
        }
        android.window.WindowContainerTransaction transaction = this.mWindowContainerTransaction.setDoNotPip(containerToken);
        if (transaction == null) {
            Log.d(TAG, "setDoNotPip: return null");
            return null;
        }
        return new WindowContainerTransaction(transaction);
    }

    public Parcelable getWindowContainerTransaction() {
        return this.mWindowContainerTransaction;
    }
}
