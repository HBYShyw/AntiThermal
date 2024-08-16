package com.oplus.kinect;

import android.content.ComponentName;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.oplus.kinect.IRemoteServiceCallback;

/* loaded from: classes.dex */
public abstract class GestureStateCallback {
    public static final int MSG_NOTIFYRESULT = 0;
    public static final int MSG_SERVICEDISCONNECTED = 1;
    public IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() { // from class: com.oplus.kinect.GestureStateCallback.1
        Handler mHandler = new Handler() { // from class: com.oplus.kinect.GestureStateCallback.1.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        int[] value = (int[]) msg.obj;
                        GestureStateCallback.this.notifyResult(value);
                        return;
                    default:
                        return;
                }
            }
        };

        @Override // com.oplus.kinect.IRemoteServiceCallback
        public void notifyResult(int[] value) throws RemoteException {
            Message.obtain(this.mHandler, 0, -1, -1, value).sendToTarget();
        }
    };

    public void notifyResult(int[] value) {
    }

    public void onServiceDisconnected(ComponentName name) {
    }
}
