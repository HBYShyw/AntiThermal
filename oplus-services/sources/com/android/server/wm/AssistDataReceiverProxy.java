package com.android.server.wm;

import android.app.IAssistDataReceiver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.server.am.AssistDataRequester;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class AssistDataReceiverProxy implements AssistDataRequester.AssistDataRequesterCallbacks, IBinder.DeathRecipient {
    private static final String TAG = "ActivityTaskManager";
    private String mCallerPackage;
    private IAssistDataReceiver mReceiver;

    public boolean canHandleReceivedAssistDataLocked() {
        return true;
    }

    public AssistDataReceiverProxy(IAssistDataReceiver iAssistDataReceiver, String str) {
        this.mReceiver = iAssistDataReceiver;
        this.mCallerPackage = str;
        linkToDeath();
    }

    public void onAssistDataReceivedLocked(Bundle bundle, int i, int i2) {
        IAssistDataReceiver iAssistDataReceiver = this.mReceiver;
        if (iAssistDataReceiver != null) {
            try {
                iAssistDataReceiver.onHandleAssistData(bundle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to proxy assist data to receiver in package=" + this.mCallerPackage, e);
            }
        }
    }

    public void onAssistScreenshotReceivedLocked(Bitmap bitmap) {
        IAssistDataReceiver iAssistDataReceiver = this.mReceiver;
        if (iAssistDataReceiver != null) {
            try {
                iAssistDataReceiver.onHandleAssistScreenshot(bitmap);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to proxy assist screenshot to receiver in package=" + this.mCallerPackage, e);
            }
        }
    }

    public void onAssistRequestCompleted() {
        unlinkToDeath();
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        unlinkToDeath();
    }

    private void linkToDeath() {
        try {
            this.mReceiver.asBinder().linkToDeath(this, 0);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not link to client death", e);
        }
    }

    private void unlinkToDeath() {
        IAssistDataReceiver iAssistDataReceiver = this.mReceiver;
        if (iAssistDataReceiver != null) {
            iAssistDataReceiver.asBinder().unlinkToDeath(this, 0);
        }
        this.mReceiver = null;
    }
}
