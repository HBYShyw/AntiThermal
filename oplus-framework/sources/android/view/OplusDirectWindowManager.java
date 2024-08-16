package android.view;

import android.os.RemoteException;
import com.oplus.direct.OplusDirectFindCmd;

/* loaded from: classes.dex */
public class OplusDirectWindowManager implements IOplusDirectWindowManager {
    private static final String TAG = "OplusDirectWindowManager";

    @Override // android.view.IOplusDirectWindowManager
    public void directFindCmd(OplusDirectFindCmd findCmd) throws RemoteException {
        OplusWindowManager.getInstance().directFindCmd(findCmd);
    }
}
