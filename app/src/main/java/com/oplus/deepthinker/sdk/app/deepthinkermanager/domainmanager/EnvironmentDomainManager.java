package com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager;

import android.os.Bundle;
import android.os.RemoteException;
import com.oplus.deepthinker.sdk.app.IProviderFeature;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.ServiceHolder;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager;
import com.oplus.deepthinker.sdk.app.userprofile.labels.ResidenceLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.StayLocationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.WifiLocationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.ResidenceLabelUtils;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.StayLocationLabelUtils;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.WifiLocationLabelUtils;
import i6.IDeepThinkerBridge;
import java.util.List;

/* loaded from: classes.dex */
public class EnvironmentDomainManager implements IEnvironmentDomainManager {
    private static final String TAG = "EnvironmentDomainManager";
    private ServiceHolder mServiceHolder;

    public EnvironmentDomainManager(ServiceHolder serviceHolder) {
        this.mServiceHolder = serviceHolder;
    }

    private IDeepThinkerBridge getDeepThinkerBinder() {
        return this.mServiceHolder.getDeepThinkerBridge();
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public List<ResidenceLabel> getCompanyLabels() {
        return ResidenceLabelUtils.getCompanyLabels(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public List<ResidenceLabel> getHomeLabels() {
        return ResidenceLabelUtils.getHomeLabels(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public int getInOutDoorState() {
        Bundle call;
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null && (call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_INOUTDOOR, null, null)) != null) {
                try {
                    return call.getInt(IProviderFeature.COMMON_RESULT, 0);
                } catch (Throwable th) {
                    SDKLog.e(TAG, "getInOutDoorState: " + th.toString());
                }
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getInOutDoorState failed " + e10);
        }
        return 0;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public List<ResidenceLabel> getResidenceLabels() {
        return ResidenceLabelUtils.getResidenceLabels(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public List<String> getSmartGpsBssidList() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getSmartGpsBssidList();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getSmartGpsBssidList failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public List<StayLocationLabel> getStayLocationLabels() {
        return StayLocationLabelUtils.getStayLocationLabels(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public List<WifiLocationLabel> getWifiLocationLabels() {
        return WifiLocationLabelUtils.getWifiLocationLabels(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IEnvironmentDomainManager
    public int getInOutDoorState(Bundle bundle) {
        Bundle call;
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null && (call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_INOUTDOOR, null, bundle)) != null) {
                try {
                    return call.getInt(IProviderFeature.COMMON_RESULT, 0);
                } catch (Throwable th) {
                    SDKLog.e(TAG, "getInOutDoorState: " + th.toString());
                }
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getInOutDoorState failed " + e10);
        }
        return 0;
    }
}
