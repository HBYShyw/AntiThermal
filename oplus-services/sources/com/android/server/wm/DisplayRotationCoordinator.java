package com.android.server.wm;

import android.view.Display;
import com.android.internal.annotations.VisibleForTesting;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class DisplayRotationCoordinator {
    private static final String TAG = "DisplayRotationCoordinator";
    private int mDefaultDisplayCurrentRotation;
    private int mDefaultDisplayDefaultRotation;

    @VisibleForTesting
    Runnable mDefaultDisplayRotationChangedCallback;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDefaultDisplayRotationChanged(int i) {
        this.mDefaultDisplayCurrentRotation = i;
        Runnable runnable = this.mDefaultDisplayRotationChangedCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDefaultDisplayDefaultRotation(int i) {
        this.mDefaultDisplayDefaultRotation = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDefaultDisplayCurrentRotation() {
        return this.mDefaultDisplayCurrentRotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDefaultDisplayRotationChangedCallback(Runnable runnable) {
        if (this.mDefaultDisplayRotationChangedCallback != null) {
            throw new UnsupportedOperationException("Multiple clients unsupported");
        }
        this.mDefaultDisplayRotationChangedCallback = runnable;
        if (this.mDefaultDisplayCurrentRotation != this.mDefaultDisplayDefaultRotation) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeDefaultDisplayRotationChangedCallback() {
        this.mDefaultDisplayRotationChangedCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSecondaryInternalDisplay(DisplayContent displayContent) {
        Display display;
        return (displayContent.isDefaultDisplay || (display = displayContent.mDisplay) == null || display.getType() != 1) ? false : true;
    }
}
