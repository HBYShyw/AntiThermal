package android.nwpower;

import android.nwpower.IOAppNetControl;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.List;

/* loaded from: classes.dex */
public class OAppNetControlManager implements IOAppNetControlManager {
    private static final String OAPPNETCONTROL_SERVICE = "oappnetcontrol";
    private static OAppNetControlManager sInstance;
    private final IOAppNetControl mService = IOAppNetControl.Stub.asInterface(ServiceManager.getService(OAPPNETCONTROL_SERVICE));
    private static final boolean BG_APP_NET_CONTROL_SWITCH_ENABLE = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_NWPOWER_BG_APP_NET_CONTROL_SWITCH_ENABLE);
    private static final boolean PROC_NET_CONTROL_SWITCH_ENABLE = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_NWPOWER_PROC_NET_CONTROL_SWITCH_ENABLE);

    public static OAppNetControlManager getInstance() {
        OAppNetControlManager oAppNetControlManager;
        synchronized (OAppNetControlManager.class) {
            if (sInstance == null) {
                sInstance = new OAppNetControlManager();
            }
            oAppNetControlManager = sInstance;
        }
        return oAppNetControlManager;
    }

    protected OAppNetControlManager() {
        Slog.d("OAppNetControlManager", "Service init ok!");
    }

    @Override // android.nwpower.IOAppNetControlManager
    public boolean getStaticOAppNetControlEnable() {
        return BG_APP_NET_CONTROL_SWITCH_ENABLE || PROC_NET_CONTROL_SWITCH_ENABLE;
    }

    @Override // android.nwpower.IOAppNetControlManager
    public void setFirewall(int uid, boolean allow) {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return;
        }
        try {
            iOAppNetControl.setFirewall(uid, allow);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public void setFirewallWithArgs(int uid, boolean allow, int netrestore, int scenes) {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return;
        }
        try {
            iOAppNetControl.setFirewallWithArgs(uid, allow, netrestore, scenes);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public void destroySocket(int uid) {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return;
        }
        try {
            iOAppNetControl.destroySocket(uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public void legacyDestroySocket() {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return;
        }
        try {
            iOAppNetControl.legacyDestroySocket();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public int networkDisableWhiteList(List<String> appConfigUids, int enable) {
        if (this.mService == null || appConfigUids == null || appConfigUids.size() == 0) {
            return 1;
        }
        try {
            int returnValue = this.mService.networkDisableWhiteList(appConfigUids, enable);
            return returnValue;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public void destroySocketForProc(int uid, int pid) {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return;
        }
        try {
            iOAppNetControl.destroySocketForProc(uid, pid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public void notifyUnFreezeForProc(int uid, int pid) {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return;
        }
        try {
            iOAppNetControl.notifyUnFreezeForProc(uid, pid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.nwpower.IOAppNetControlManager
    public long[] requestAppFireWallHistoryStamp(int uid) {
        IOAppNetControl iOAppNetControl = this.mService;
        if (iOAppNetControl == null) {
            return new long[]{0, 0, 0};
        }
        try {
            return iOAppNetControl.requestAppFireWallHistoryStamp(uid);
        } catch (RemoteException e) {
            return new long[]{0, 0, 0};
        }
    }
}
