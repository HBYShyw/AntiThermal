package com.oplus.network;

import android.os.ServiceManager;
import android.util.Log;
import com.oplus.network.IOplusNetdEventCb;
import com.oplus.network.IOplusNetworkCmdService;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OplusNetworkSysManager {
    private static final String SRV_NAME = "oplusnetcmd";
    private static final String TAG = "OplusNetworkSysManager";
    private static OplusNetworkSysManager mInstance;
    private IOplusNetworkCmdService mNetworkCmdService;
    private final ArrayList<IOplusNetworkEventCb> mNetworkEventCbList = new ArrayList<>();
    private final ArrayList<IOplusDnsHookCb> mDnsHookCbList = new ArrayList<>();
    private boolean mRegisterSucc = false;
    private final IOplusNetdEventCb mNetdEventIf = new IOplusNetdEventCb.Stub() { // from class: com.oplus.network.OplusNetworkSysManager.1
        @Override // com.oplus.network.IOplusNetdEventCb
        public void onDnsEvent(int netId, int eventType, int returnCode, int latencyMs, String hostname, String[] ipAddresses, int ipAddressesCount, int uid) {
            synchronized (OplusNetworkSysManager.this.mNetworkEventCbList) {
                Iterator it = OplusNetworkSysManager.this.mNetworkEventCbList.iterator();
                while (it.hasNext()) {
                    IOplusNetworkEventCb cb = (IOplusNetworkEventCb) it.next();
                    try {
                        cb.onDnsEvent(netId, eventType, returnCode, latencyMs, hostname, ipAddresses, ipAddressesCount, uid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(OplusNetworkSysManager.TAG, "cb.onDnsEvent faield!" + e.getMessage());
                    }
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface IOplusDnsHookCb {
        void onFirstDnsHookNotify(String str);
    }

    /* loaded from: classes.dex */
    public interface IOplusNetworkEventCb {
        void onDnsEvent(int i, int i2, int i3, int i4, String str, String[] strArr, int i5, int i6);
    }

    private OplusNetworkSysManager() {
        getNetworkCmdService();
    }

    public static OplusNetworkSysManager getInstance() {
        OplusNetworkSysManager oplusNetworkSysManager;
        synchronized (OplusNetworkSysManager.class) {
            if (mInstance == null) {
                mInstance = new OplusNetworkSysManager();
            }
            oplusNetworkSysManager = mInstance;
        }
        return oplusNetworkSysManager;
    }

    private IOplusNetworkCmdService getNetworkCmdService() {
        if (this.mNetworkCmdService == null) {
            this.mNetworkCmdService = IOplusNetworkCmdService.Stub.asInterface(ServiceManager.getService(SRV_NAME));
        }
        return this.mNetworkCmdService;
    }

    private void updateCbRegister() {
        int totalCount = this.mNetworkEventCbList.size() + this.mDnsHookCbList.size();
        boolean z = this.mRegisterSucc;
        if (!z && totalCount > 0) {
            try {
                getNetworkCmdService().registerOplusNetdEvent(this.mNetdEventIf);
                Log.d(TAG, "mNetworkStackService.registerOplusNetdEvent succ");
                this.mRegisterSucc = true;
                return;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "mNetworkStackService.registerOplusNetdEvent failed! " + e.getMessage());
                return;
            }
        }
        if (z && totalCount == 0) {
            try {
                getNetworkCmdService().unregisterOplusNetdEvent(this.mNetdEventIf);
                Log.d(TAG, "mNetworkStackService.unregisterOplusNetdEvent succ");
                this.mRegisterSucc = false;
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.e(TAG, "mNetworkStackService.registerOplusNetdEvent failed! " + e2.getMessage());
            }
        }
    }

    public boolean registerNetworkEventCb(IOplusNetworkEventCb cb) {
        synchronized (this.mNetworkEventCbList) {
            if (!this.mNetworkEventCbList.contains(cb)) {
                this.mNetworkEventCbList.add(cb);
                updateCbRegister();
            }
        }
        return true;
    }

    public boolean unregisterNetworkEventCb(IOplusNetworkEventCb cb) {
        synchronized (this.mNetworkEventCbList) {
            if (this.mNetworkEventCbList.contains(cb)) {
                this.mNetworkEventCbList.remove(cb);
                updateCbRegister();
            }
        }
        return true;
    }

    public String oplusNetdCmdParse(String cmd, int[] params) {
        try {
            return getNetworkCmdService().oplusNetdCmdParse(cmd, params);
        } catch (Exception var4) {
            Log.e(TAG, "oplusNetdCmdParse failed! " + var4.getMessage());
            return "fail";
        }
    }
}
