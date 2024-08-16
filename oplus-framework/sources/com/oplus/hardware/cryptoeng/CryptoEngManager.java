package com.oplus.hardware.cryptoeng;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import vendor.oplus.hardware.cryptoeng.ICryptoeng;

/* loaded from: classes.dex */
public class CryptoEngManager {
    private static final String TAG = "CryptoEngManager";
    private static volatile CryptoEngManager sInstance;
    private static String sServiceName = ICryptoeng.DESCRIPTOR + "/default";
    private volatile ICryptoeng mCryptoEngService;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.hardware.cryptoeng.CryptoEngManager.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.i(CryptoEngManager.TAG, CryptoEngManager.sServiceName + " binderDied");
            synchronized (CryptoEngManager.class) {
                CryptoEngManager.this.mCryptoEngService = null;
            }
        }
    };

    /* loaded from: classes.dex */
    public static class CommandId {
        public static final byte CE_CMD_CLEAN_UP = 53;
        public static final byte CE_CMD_CRYPTO_SUPPORT = 60;
        public static final byte CE_CMD_ENGINEER = 90;
        public static final byte CE_CMD_FINDPHONE_GET_STATUS = 18;
        public static final byte CE_CMD_GENERATE_PKI_CERT = 24;
        public static final byte CE_CMD_GET_SECURETYPE = 54;
        public static final byte CE_CMD_GOOGLE_ATTESTATION_VERIFY = 4;
        public static final byte CE_CMD_GOOGLE_ATTESTATION_WRITE = 3;
        public static final byte CE_CMD_HDCP_KEY_VERIFY = 52;
        public static final byte CE_CMD_HDCP_KEY_WRITE = 51;
        public static final byte CE_CMD_VERIFY_PKI_CERT = 25;
        public static final byte CE_CMD_WIDEVINE_SUPPORT = 59;
    }

    private CryptoEngManager() {
    }

    public static CryptoEngManager getInstance() {
        if (sInstance == null) {
            synchronized (CryptoEngManager.class) {
                if (sInstance == null) {
                    sInstance = new CryptoEngManager();
                }
            }
        }
        return sInstance;
    }

    private synchronized ICryptoeng getService() {
        if (this.mCryptoEngService == null) {
            IBinder binder = ServiceManager.getService(sServiceName);
            if (binder == null) {
                Log.w(TAG, "getService fail." + sServiceName);
                return null;
            }
            try {
                binder.linkToDeath(this.mDeathRecipient, 0);
                this.mCryptoEngService = ICryptoeng.Stub.asInterface(binder);
                if (this.mCryptoEngService == null) {
                    Log.e(TAG, "asInterface fail.");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "linkToDeath fail ", e);
                return null;
            }
        }
        return this.mCryptoEngService;
    }

    public byte[] cryptoEngCommand(byte[] inData) {
        try {
            ICryptoeng service = getService();
            if (service != null) {
                return service.cryptoeng_invoke_command(inData);
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "get_project failed.", e);
            return null;
        }
    }
}
