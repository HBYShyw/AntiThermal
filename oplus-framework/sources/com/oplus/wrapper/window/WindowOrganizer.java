package com.oplus.wrapper.window;

import com.oplus.wrapper.view.RemoteAnimationAdapter;

/* loaded from: classes.dex */
public class WindowOrganizer {
    private final android.window.WindowOrganizer mWindowOrganizer = new android.window.WindowOrganizer();

    public int applySyncTransaction(WindowContainerTransaction t, WindowContainerTransactionCallback callback) {
        return this.mWindowOrganizer.applySyncTransaction(t.get(), callback.get());
    }

    public int startLegacyTransition(int type, RemoteAnimationAdapter adapter, WindowContainerTransactionCallback syncCallback, WindowContainerTransaction t) {
        return this.mWindowOrganizer.startLegacyTransition(type, adapter.getRemoteAnimationAdapter(), syncCallback.get(), t.get());
    }
}
