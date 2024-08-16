package com.android.server.inputmethod;

import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.inputmethod.ImeTracker;
import com.android.server.inputmethod.ImeVisibilityStateComputer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
interface ImeVisibilityApplier {
    default void applyImeVisibility(IBinder iBinder, ImeTracker.Token token, @ImeVisibilityStateComputer.VisibilityState int i) {
    }

    default void performHideIme(IBinder iBinder, ImeTracker.Token token, ResultReceiver resultReceiver, int i) {
    }

    default void performShowIme(IBinder iBinder, ImeTracker.Token token, int i, ResultReceiver resultReceiver, int i2) {
    }

    default boolean removeImeScreenshot(int i) {
        return false;
    }

    default boolean showImeScreenshot(IBinder iBinder, int i, ImeTracker.Token token) {
        return false;
    }

    default void updateImeLayeringByTarget(IBinder iBinder) {
    }
}
