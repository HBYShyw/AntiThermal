package android.net;

import android.content.Context;
import android.content.IOplusContextEx;
import android.net.IOplusNetworkingControlManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DebugUtils;
import android.util.Log;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusNetworkingControlManager {
    public static final String ACTION_APP_NETWORK_NOT_ALLOWED = "oplus.intent.action.APP_NETWORK_NOT_ALLOWED";
    public static final int ALLOW = 1;
    public static final int DENY = 2;
    public static final String EXTRA_NETWORK_TYPE = "networkType";
    public static final String EXTRA_PACKAGE_NAME = "packageName";
    public static final int INVALID_UID = -1;
    public static final int POLICY_AllOW_MOBILEDATA_REJECT_WIFI = 2;
    public static final int POLICY_NONE = 0;
    public static final int POLICY_REJECT_ALL = 4;
    public static final int POLICY_REJECT_MOBILEDATA_AllOW_WIFI = 1;
    public static final int REJECT_MOBILEDATA = 5;
    public static final int REJECT_WIFI = 6;
    private static final String TAG = "OplusNetworkingControlManager";
    public static final int TYPE_MOBILEDATA = 0;
    public static final int TYPE_MOBILEDATA_MTK = 1;
    public static final int TYPE_MOBILEDATA_QCOM = 2;
    public static final int TYPE_WIFI = 3;
    private static OplusNetworkingControlManager mInstance = null;
    private IOplusNetworkingControlManager mService;
    private final DeathRecipient mServiceDeath;

    private OplusNetworkingControlManager() {
        this.mServiceDeath = new DeathRecipient();
        this.mService = getService();
    }

    public static OplusNetworkingControlManager getOplusNetworkingControlManager() {
        if (mInstance == null) {
            mInstance = new OplusNetworkingControlManager();
        }
        return mInstance;
    }

    public OplusNetworkingControlManager(Context context, IOplusNetworkingControlManager service) {
        this.mServiceDeath = new DeathRecipient();
        if (service == null) {
            throw new IllegalArgumentException("missing IOplusNetworkingControlManager");
        }
        this.mService = service;
    }

    public static OplusNetworkingControlManager from(Context context) {
        return (OplusNetworkingControlManager) context.getSystemService(IOplusContextEx.NETWORKING_CONTROL_SERVICE);
    }

    private IOplusNetworkingControlManager getService() {
        if (this.mService == null) {
            Log.d(TAG, "getService init");
            IOplusNetworkingControlManager temp = IOplusNetworkingControlManager.Stub.asInterface(ServiceManager.getService(IOplusContextEx.NETWORKING_CONTROL_SERVICE));
            if (this.mService == null && temp != null) {
                try {
                    this.mService = temp;
                    temp.asBinder().linkToDeath(this.mServiceDeath, 0);
                } catch (Exception e) {
                    Log.v(TAG, "getService Exception");
                    this.mService = null;
                }
            }
        }
        return this.mService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DeathRecipient implements IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(OplusNetworkingControlManager.TAG, "binderDied, resetService");
            OplusNetworkingControlManager.this.resetServiceCache();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetServiceCache() {
        IOplusNetworkingControlManager iOplusNetworkingControlManager = this.mService;
        if (iOplusNetworkingControlManager != null) {
            iOplusNetworkingControlManager.asBinder().unlinkToDeath(this.mServiceDeath, 0);
            this.mService = null;
        }
    }

    public void setUidPolicy(int uid, int policy) {
        IOplusNetworkingControlManager service = getService();
        this.mService = service;
        if (service == null) {
            return;
        }
        try {
            service.setUidPolicy(uid, policy);
        } catch (RemoteException e) {
            Log.e(TAG, "setUidPolicy: Exception = " + e.getMessage());
        }
    }

    public int getUidPolicy(int uid) {
        IOplusNetworkingControlManager service = getService();
        this.mService = service;
        if (service == null) {
            return 0;
        }
        try {
            return service.getUidPolicy(uid);
        } catch (RemoteException e) {
            Log.e(TAG, "getUidPolicy: Exception = " + e.getMessage());
            return 0;
        }
    }

    public int[] getUidsWithPolicy(int policy) {
        IOplusNetworkingControlManager service = getService();
        this.mService = service;
        if (service == null) {
            return null;
        }
        try {
            return service.getUidsWithPolicy(policy);
        } catch (RemoteException e) {
            Log.e(TAG, "getUidsWithPolicy: Exception = " + e.getMessage());
            return null;
        }
    }

    public Map<Integer, Integer> getPolicyList() {
        IOplusNetworkingControlManager service = getService();
        this.mService = service;
        if (service == null) {
            return null;
        }
        try {
            return service.getPolicyList();
        } catch (RemoteException e) {
            Log.e(TAG, "getPolicyList: Exception = " + e.getMessage());
            return null;
        }
    }

    public static String uidPoliciesToString(int uidPolicies) {
        StringBuilder string = new StringBuilder().append(uidPolicies).append(" (");
        if (uidPolicies == 0) {
            string.append("NONE");
        } else {
            string.append(DebugUtils.flagsToString(OplusNetworkingControlManager.class, "POLICY_", uidPolicies));
        }
        string.append(")");
        return string.toString();
    }

    public void factoryReset() {
        IOplusNetworkingControlManager service = getService();
        this.mService = service;
        if (service == null) {
            return;
        }
        try {
            service.factoryReset();
        } catch (RemoteException e) {
            Log.e(TAG, "factoryReset: Exception = " + e.getMessage());
        }
    }
}
