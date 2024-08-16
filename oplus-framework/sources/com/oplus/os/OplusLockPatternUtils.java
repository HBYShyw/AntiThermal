package com.oplus.os;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Singleton;
import com.oplus.os.IOplusLockPatternUtils;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusLockPatternUtils {
    private static final Singleton<IOplusLockPatternUtils> IOplusLockPatternUtilsSingleton = new Singleton<IOplusLockPatternUtils>() { // from class: com.oplus.os.OplusLockPatternUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public IOplusLockPatternUtils m840create() {
            try {
                return IOplusLockPatternUtils.Stub.asInterface(ServiceManager.getService("lock_settings").getExtension());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    };
    private static final String TAG = "OplusLockPatternUtils";

    public static IOplusLockPatternUtils getService() {
        return (IOplusLockPatternUtils) IOplusLockPatternUtilsSingleton.get();
    }

    public byte[] getVersionInfo() {
        try {
            byte[] versionInfo = getService().getVersionInfo();
            return versionInfo;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public void notifySrpLockVerify(byte[] eSalt) {
        try {
            getService().notifySrpLockVerify(eSalt);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void registSrpCredential() {
        try {
            getService().registSrpCredential();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void unRegistSrpCredential() {
        try {
            getService().unRegistSrpCredential();
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public Map<String, byte[]> getDerivedPasswordInfo(byte[] pubkeyForPassword, byte[] pubkeyForSalt) {
        if (pubkeyForPassword == null || pubkeyForSalt == null) {
            Log.e(TAG, "Failure to getDerivedPasswordInfo, pubKey is null");
            return null;
        }
        try {
            Map<String, byte[]> derivedInfo = getService().getDerivedPasswordInfo(pubkeyForPassword, pubkeyForSalt);
            return derivedInfo;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public byte[] getPublicKey() {
        try {
            byte[] publicKey = getService().getPublicKey();
            return publicKey;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public byte[] generateDerivedPassword(byte[] pubKey, byte[] ePassword, byte[] eSalt) {
        if (pubKey == null || ePassword == null || eSalt == null) {
            Log.e(TAG, "Failure to generateDerivedPassword, account or salt byte is null");
            return null;
        }
        try {
            byte[] passwordVerify = getService().generateDerivedPassword(pubKey, ePassword, eSalt);
            return passwordVerify;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }
}
