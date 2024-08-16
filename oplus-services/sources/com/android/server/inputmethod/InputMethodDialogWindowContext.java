package com.android.server.inputmethod;

import android.R;
import android.app.ActivityThread;
import android.content.Context;
import android.view.ContextThemeWrapper;
import com.android.internal.annotations.VisibleForTesting;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InputMethodDialogWindowContext {
    private Context mDialogWindowContext;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public Context get(int i) {
        Context context = this.mDialogWindowContext;
        if (context == null || context.getDisplayId() != i) {
            this.mDialogWindowContext = new ContextThemeWrapper(ActivityThread.currentActivityThread().getSystemUiContext(i).createWindowContext(2012, null), R.style.Theme.DeviceDefault.Settings);
        }
        return this.mDialogWindowContext;
    }
}
