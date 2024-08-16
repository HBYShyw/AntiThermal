package com.android.server.locksettings;

import android.hardware.authsecret.IAuthSecret;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class AuthSecretHidlAdapter implements IAuthSecret {
    private final android.hardware.authsecret.V1_0.IAuthSecret mImpl;

    public int getInterfaceVersion() throws RemoteException {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AuthSecretHidlAdapter(android.hardware.authsecret.V1_0.IAuthSecret iAuthSecret) {
        this.mImpl = iAuthSecret;
    }

    public void setPrimaryUserCredential(byte[] bArr) throws RemoteException {
        ArrayList arrayList = new ArrayList(bArr.length);
        for (byte b : bArr) {
            arrayList.add(Byte.valueOf(b));
        }
        this.mImpl.primaryUserCredential(arrayList);
    }

    public IBinder asBinder() {
        throw new UnsupportedOperationException("AuthSecretHidlAdapter does not support asBinder");
    }

    public String getInterfaceHash() throws RemoteException {
        throw new UnsupportedOperationException("AuthSecretHidlAdapter does not support getInterfaceHash");
    }
}
