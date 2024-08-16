package com.oplus.deepthinker.platform.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.eventhub.sdk.aidl.DeviceEventResult;
import com.oplus.eventhub.sdk.aidl.IEventCallback;

/* loaded from: classes.dex */
public class EventCallbackDelegate extends Binder implements IInterface {
    private static final String DESCRIPTOR = "com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback";
    private static final int TRANSACT_CALLBACK = 1;
    private IEventCallback mCallback;

    public EventCallbackDelegate(IEventCallback callback) {
        this.mCallback = callback;
        attachInterface(this, DESCRIPTOR);
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case 1:
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    DeviceEventResult result = DeviceEventResult.CREATOR.createFromParcel(data);
                    this.mCallback.onEventStateChanged(result);
                }
                reply.writeNoException();
                return true;
            case 1598968902:
                reply.writeString(DESCRIPTOR);
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }
}
