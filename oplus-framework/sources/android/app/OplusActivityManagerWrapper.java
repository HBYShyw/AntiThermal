package android.app;

import android.os.Bundle;
import android.os.RemoteException;

/* loaded from: classes.dex */
public class OplusActivityManagerWrapper {
    public static Bundle autoLayoutCall(Bundle bundle) throws RemoteException {
        return OplusActivityManager.getInstance().autoLayoutCall(bundle);
    }
}
