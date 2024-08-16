package android.app;

import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;

/* loaded from: classes.dex */
public class OplusActivityClientExtImpl implements IActivityClientExt {
    private ActivityClient mBase;

    public OplusActivityClientExtImpl(Object base) {
        this.mBase = (ActivityClient) base;
    }

    public void updateActivitySpecificConfig(IBinder token, Configuration config) {
        try {
            ActivityClient.getActivityClientController().updateActivitySpecificConfig(token, config);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }
}
