package com.android.server.inputmethod;

import android.os.IBinder;
import android.os.InputConstants;
import android.os.Process;
import android.view.InputApplicationHandle;
import android.view.InputChannel;
import android.view.InputWindowHandle;
import android.view.SurfaceControl;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HandwritingEventReceiverSurface {
    static final boolean DEBUG = HandwritingModeController.DEBUG;
    public static final String TAG = "HandwritingEventReceiverSurface";
    private final InputChannel mClientChannel;
    private final SurfaceControl mInputSurface;
    private boolean mIsIntercepting;
    private final InputWindowHandle mWindowHandle;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HandwritingEventReceiverSurface(String str, int i, SurfaceControl surfaceControl, InputChannel inputChannel) {
        this.mClientChannel = inputChannel;
        this.mInputSurface = surfaceControl;
        InputWindowHandle inputWindowHandle = new InputWindowHandle(new InputApplicationHandle((IBinder) null, str, InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS), i);
        this.mWindowHandle = inputWindowHandle;
        inputWindowHandle.name = str;
        inputWindowHandle.token = inputChannel.getToken();
        inputWindowHandle.layoutParamsType = 2015;
        inputWindowHandle.dispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        inputWindowHandle.ownerPid = Process.myPid();
        inputWindowHandle.ownerUid = Process.myUid();
        inputWindowHandle.scaleFactor = 1.0f;
        inputWindowHandle.inputConfig = 49420;
        inputWindowHandle.replaceTouchableRegionWithCrop((SurfaceControl) null);
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setInputWindowInfo(surfaceControl, inputWindowHandle);
        transaction.setLayer(surfaceControl, 2);
        transaction.setPosition(surfaceControl, 0.0f, 0.0f);
        transaction.setCrop(surfaceControl, null);
        transaction.show(surfaceControl);
        transaction.apply();
        this.mIsIntercepting = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startIntercepting(int i, int i2) {
        InputWindowHandle inputWindowHandle = this.mWindowHandle;
        inputWindowHandle.ownerPid = i;
        inputWindowHandle.ownerUid = i2;
        inputWindowHandle.inputConfig &= -16385;
        new SurfaceControl.Transaction().setInputWindowInfo(this.mInputSurface, this.mWindowHandle).apply();
        this.mIsIntercepting = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIntercepting() {
        return this.mIsIntercepting;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove() {
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.remove(this.mInputSurface);
        transaction.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputChannel getInputChannel() {
        return this.mClientChannel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getSurface() {
        return this.mInputSurface;
    }

    InputWindowHandle getInputWindowHandle() {
        return this.mWindowHandle;
    }
}
