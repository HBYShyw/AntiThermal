package android.app;

import android.app.IApplicationThread;
import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public abstract class OplusBaseApplicationThread extends IApplicationThread.Stub {
    private static final String TAG = "OplusBaseApplicationThr";
    private OplusApplicationThreadHelper oplusAppThreadHelper = new OplusApplicationThreadHelper(this);

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        OplusApplicationThreadHelper oplusApplicationThreadHelper = this.oplusAppThreadHelper;
        if (oplusApplicationThreadHelper != null && oplusApplicationThreadHelper.onTransact(code, data, reply, flags)) {
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    public void scheduleApplicationInfoChangedForSwitchUser(ApplicationInfo ai, int updateFrameworkRes) {
    }
}
