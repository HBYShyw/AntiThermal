package android.view;

import android.common.IOplusCommonFeature;
import android.os.RemoteException;
import com.oplus.direct.OplusDirectFindCmd;

/* loaded from: classes.dex */
public interface IOplusDirectWindow extends IOplusCommonFeature {
    default void directFindCmd(OplusDirectFindCmd findCmd) throws RemoteException {
    }
}
