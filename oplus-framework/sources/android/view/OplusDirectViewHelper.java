package android.view;

import android.os.Parcel;
import com.oplus.direct.OplusDirectFindCmd;
import com.oplus.direct.OplusDirectFindResult;
import com.oplus.direct.OplusDirectUtils;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OplusDirectViewHelper extends OplusDummyDirectViewHelper {
    private static final boolean DBG = OplusDirectUtils.DBG;
    private static final String TAG = "DirectService";
    private final OplusDirectViewDump mDump;
    private final WeakReference<ViewRootImpl> mViewAncestor;

    public OplusDirectViewHelper(WeakReference<ViewRootImpl> viewAncestor) {
        super(viewAncestor);
        this.mDump = new OplusDirectViewDump();
        this.mViewAncestor = viewAncestor;
    }

    @Override // android.view.OplusDummyDirectViewHelper, android.view.IOplusDirectWindow
    public void directFindCmd(OplusDirectFindCmd findCmd) {
        if (findCmd == null) {
            return;
        }
        ViewRootImpl viewAncestor = this.mViewAncestor.get();
        if (viewAncestor != null && viewAncestor.mView != null) {
            this.mDump.findCmd(viewAncestor, findCmd);
        } else {
            OplusDirectUtils.onFindFailed(findCmd.getCallback(), OplusDirectFindResult.ERROR_NO_VIEW);
        }
    }

    @Override // android.view.OplusDummyDirectViewHelper, android.view.IOplusDirectViewHelper
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        OplusDirectFindCmd findCmd;
        switch (code) {
            case 10008:
                data.enforceInterface(IOplusWindow.DESCRIPTOR);
                if (data.readInt() != 0) {
                    findCmd = OplusDirectFindCmd.CREATOR.createFromParcel(data);
                } else {
                    findCmd = null;
                }
                directFindCmd(findCmd);
                return true;
            default:
                return false;
        }
    }
}
