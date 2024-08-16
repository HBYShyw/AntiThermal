package com.oplus.epona.ipc.local;

import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.epona.Call;
import com.oplus.epona.Epona;
import com.oplus.epona.IRemoteTransfer;
import com.oplus.epona.ITransferCallback;
import com.oplus.epona.Request;
import com.oplus.epona.Response;
import com.oplus.epona.ipc.local.RemoteTransfer;
import com.oplus.epona.utils.Logger;

/* loaded from: classes.dex */
public class RemoteTransfer extends IRemoteTransfer.Stub {
    public static final String APP_PLATFORM_PACKAGE_NAME = "com.oplus.appplatform";
    private static final String TAG = "RemoteTransfer";
    private static volatile RemoteTransfer sInstance;

    private RemoteTransfer() {
    }

    public static RemoteTransfer getInstance() {
        if (sInstance == null) {
            synchronized (RemoteTransfer.class) {
                if (sInstance == null) {
                    sInstance = new RemoteTransfer();
                }
            }
        }
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$asyncCall$0(ITransferCallback iTransferCallback, Response response) {
        try {
            iTransferCallback.onReceive(response);
        } catch (RemoteException e10) {
            Logger.e(TAG, "failed to asyncCall and exception is %s", e10.toString());
        }
    }

    @Override // com.oplus.epona.IRemoteTransfer
    public void asyncCall(Request request, final ITransferCallback iTransferCallback) {
        Epona.newCall(request).asyncExecute(new Call.Callback() { // from class: n6.a
            @Override // com.oplus.epona.Call.Callback
            public final void onReceive(Response response) {
                RemoteTransfer.lambda$asyncCall$0(ITransferCallback.this, response);
            }
        });
    }

    @Override // com.oplus.epona.IRemoteTransfer
    public Response call(Request request) {
        return Epona.newCall(request).execute();
    }

    @Override // com.oplus.epona.IRemoteTransfer.Stub, android.os.Binder
    public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
        try {
            return super.onTransact(i10, parcel, parcel2, i11);
        } catch (RuntimeException e10) {
            Logger.e(TAG, "onTransact Exception: " + e10.toString(), new Object[0]);
            throw e10;
        }
    }
}
