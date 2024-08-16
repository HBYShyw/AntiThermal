package com.android.server.input;

import android.os.IBinder;
import android.os.InputConstants;
import android.view.InputApplicationHandle;
import android.view.InputChannel;
import android.view.InputWindowHandle;
import android.view.SurfaceControl;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class GestureMonitorSpyWindow {
    final InputApplicationHandle mApplicationHandle;
    final InputChannel mClientChannel;
    final SurfaceControl mInputSurface;
    final IBinder mMonitorToken;
    final InputWindowHandle mWindowHandle;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GestureMonitorSpyWindow(IBinder iBinder, String str, int i, int i2, int i3, SurfaceControl surfaceControl, InputChannel inputChannel) {
        this.mMonitorToken = iBinder;
        this.mClientChannel = inputChannel;
        this.mInputSurface = surfaceControl;
        InputApplicationHandle inputApplicationHandle = new InputApplicationHandle((IBinder) null, str, InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        this.mApplicationHandle = inputApplicationHandle;
        InputWindowHandle inputWindowHandle = new InputWindowHandle(inputApplicationHandle, i);
        this.mWindowHandle = inputWindowHandle;
        inputWindowHandle.name = str;
        inputWindowHandle.token = inputChannel.getToken();
        inputWindowHandle.layoutParamsType = 2015;
        inputWindowHandle.dispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        inputWindowHandle.ownerPid = i2;
        inputWindowHandle.ownerUid = i3;
        inputWindowHandle.scaleFactor = 1.0f;
        inputWindowHandle.replaceTouchableRegionWithCrop((SurfaceControl) null);
        inputWindowHandle.inputConfig = 16644;
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setInputWindowInfo(surfaceControl, inputWindowHandle);
        transaction.setLayer(surfaceControl, 1);
        transaction.setPosition(surfaceControl, 0.0f, 0.0f);
        transaction.setCrop(surfaceControl, null);
        transaction.show(surfaceControl);
        transaction.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove() {
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.hide(this.mInputSurface);
        transaction.remove(this.mInputSurface);
        transaction.apply();
        this.mClientChannel.dispose();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String dump() {
        return "name='" + this.mWindowHandle.name + "', inputChannelToken=" + this.mClientChannel.getToken() + " displayId=" + this.mWindowHandle.displayId;
    }
}
