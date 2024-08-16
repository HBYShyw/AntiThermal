package android.view;

import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.direct.OplusDirectFindCmd;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OplusDummyDirectViewHelper implements IOplusDirectViewHelper {
    public OplusDummyDirectViewHelper(WeakReference<ViewRootImpl> viewAncestor) {
    }

    @Override // android.view.IOplusDirectViewHelper
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        return false;
    }

    @Override // android.view.IOplusDirectWindow
    public void directFindCmd(OplusDirectFindCmd oplusDirectFindCmd) throws RemoteException {
    }
}
