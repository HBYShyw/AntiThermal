package com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager;

import android.os.Bundle;
import android.os.RemoteException;
import com.oplus.deepthinker.sdk.app.IProviderFeature;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.ServiceHolder;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.periodtopapps.PeriodTopAppsResult;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.AppActionPeriodLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.AppBgVectorLabelUtils;
import i6.IDeepThinkerBridge;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AppDomainManager implements IAppDomainManager {
    private static final String TAG = "AppDomainManager";
    private ServiceHolder mServiceHolder;

    public AppDomainManager(ServiceHolder serviceHolder) {
        this.mServiceHolder = serviceHolder;
    }

    private IDeepThinkerBridge getDeepThinkerBinder() {
        return this.mServiceHolder.getDeepThinkerBridge();
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public Map<String, Map<Double, Double>> getAllAppActivePeriod(int i10) {
        Bundle call;
        String str;
        Bundle bundle = new Bundle();
        bundle.putInt(IProviderFeature.COMMON_TYPE1, UserProfileConstants.LabelId.APP_ACTIVE_CLUSTER.getValue());
        bundle.putInt(IProviderFeature.COMMON_ARG1, i10);
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null && (call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_USERPROFILE, IProviderFeature.USERPROFILE_SPECIFIC_QUERY, bundle)) != null) {
                try {
                    str = call.getString(IProviderFeature.USERPROFILE_QUERY_RESULT);
                } catch (Throwable th) {
                    SDKLog.e("Get app active result error: " + th);
                    str = null;
                }
                List<AppActionPeriodLabel> constructList = AppActionPeriodLabel.constructList(str);
                if (constructList != null && constructList.size() > 0) {
                    HashMap hashMap = new HashMap();
                    for (AppActionPeriodLabel appActionPeriodLabel : constructList) {
                        hashMap.put(appActionPeriodLabel.getPackage(), appActionPeriodLabel.getPeriodMap());
                    }
                    return hashMap;
                }
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAllAppActivePeriod failed " + e10);
        }
        return null;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public List<PeriodTopAppsResult> getAllPeriodDurationTopApps(int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAllPeriodDurationTopApps(i10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAllPeriodDurationTopApps failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public List<PeriodTopAppsResult> getAllPeriodFrequencyTopApps(int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAllPeriodFrequencyTopApps(i10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAllPeriodFrequencyTopApps failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public Map<Double, Double> getAppActivePeriod(int i10, String str) {
        Bundle call;
        Bundle bundle = new Bundle();
        bundle.putInt(IProviderFeature.COMMON_TYPE1, UserProfileConstants.LabelId.APP_ACTIVE_CLUSTER.getValue());
        bundle.putInt(IProviderFeature.COMMON_ARG1, i10);
        bundle.putString(IProviderFeature.COMMON_ARG2, str);
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null || (call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_USERPROFILE, IProviderFeature.USERPROFILE_SPECIFIC_QUERY, bundle)) == null) {
                return null;
            }
            AppActionPeriodLabel appActionPeriodLabel = new AppActionPeriodLabel(call);
            if (appActionPeriodLabel.validate()) {
                return appActionPeriodLabel.getPeriodMap();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppActivePeriod failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public Map<String, Integer> getAppBgVectorLabelResult() {
        return AppBgVectorLabelUtils.getAppBgVectorLabelResult(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public PredictResult getAppPredictResult(String str) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppPredictResult(str);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppPredictResult failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public List<PredictResult> getAppPredictResultMap(String str) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppPredictResultMap(str);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppPredictResultMap failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public List<String> getAppQueueSortedByComplex() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppQueueSortedByComplex();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppQueueSortedByComplex failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public List<String> getAppQueueSortedByCount() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppQueueSortedByCount();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppQueueSortedByCount failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public List<String> getAppQueueSortedByTime() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppQueueSortedByTime();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppQueueSortedByTime failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public PeriodTopAppsResult getCertainPeriodDurationTopApps(float f10, int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getCertainPeriodDurationTopApps(f10, i10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getCertainPeriodDurationTopApps failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public PeriodTopAppsResult getCertainPeriodFrequencyTopApps(float f10, int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getCertainPeriodFrequencyTopApps(f10, i10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getCertainPeriodFrequencyTopApps failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IAppDomainManager
    public PredictAABResult getPredictAABResult() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getPredictAABResult();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getPredictAABResult failed " + e10);
            return null;
        }
    }
}
