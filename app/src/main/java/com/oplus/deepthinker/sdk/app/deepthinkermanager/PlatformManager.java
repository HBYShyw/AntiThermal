package com.oplus.deepthinker.sdk.app.deepthinkermanager;

import android.os.RemoteException;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.ServiceHolder;
import i6.IDeepThinkerBridge;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class PlatformManager implements IPlatformMananger {
    private static final String TAG = "StateManager";
    private ServiceHolder mServiceHolder;

    public PlatformManager(ServiceHolder serviceHolder) {
        this.mServiceHolder = serviceHolder;
    }

    private IDeepThinkerBridge getDeepThinkerBinder() {
        return this.mServiceHolder.getDeepThinkerBridge();
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IPlatformMananger
    public int availableState(int i10, String str) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.availableState(i10, str);
            }
            return -1;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "availableState failed " + e10);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IPlatformMananger
    public List capability() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.capability();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "capability failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IPlatformMananger
    public Map<String, Integer> checkPermission(int i10, String str) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.checkPermission(i10, str);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "checkPermission failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IPlatformMananger
    public int getAlgorithmPlatformVersion() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getPlatformVersion();
            }
            return -1;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAlgorithmPlatformVersion failed " + e10);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IPlatformMananger
    public void requestGrantPermission(String str) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                deepThinkerBinder.requestGrantPermission(str);
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "requestGrantPermission failed " + e10);
        }
    }
}
