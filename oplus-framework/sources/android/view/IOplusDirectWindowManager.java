package android.view;

import android.os.RemoteException;
import com.oplus.direct.OplusDirectFindCmd;

/* loaded from: classes.dex */
public interface IOplusDirectWindowManager extends IOplusBaseWindowManager {
    public static final int DIRECT_FIND_CMD = 10402;
    public static final int IOPLUSDIRECTWINDOWMANAGER_INDEX = 10401;
    public static final int SECTION_INDEX = 200;

    void directFindCmd(OplusDirectFindCmd oplusDirectFindCmd) throws RemoteException;
}
