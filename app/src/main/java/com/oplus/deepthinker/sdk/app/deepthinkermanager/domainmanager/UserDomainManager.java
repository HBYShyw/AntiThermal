package com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.oplus.deepthinker.sdk.app.IProviderFeature;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.ServiceHolder;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResultWithPercentile;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager;
import com.oplus.deepthinker.sdk.app.feature.eventassociation.PerformanceOfLongCharging;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.CommuteLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.LeaveHomeLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.NotificationLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.SleepHabitLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.CommuteLabelUtils;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.NotificationLabelUtils;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.TaxiHabitUtils;
import i6.IDeepThinkerBridge;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class UserDomainManager implements IUserDomainManager {
    private static final String TAG = "UserDomainManager";
    private ServiceHolder mServiceHolder;

    public UserDomainManager(ServiceHolder serviceHolder) {
        this.mServiceHolder = serviceHolder;
    }

    private IDeepThinkerBridge getDeepThinkerBinder() {
        return this.mServiceHolder.getDeepThinkerBridge();
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public Map<String, NotificationLabel.Details> getAllNotificationLabelDetail() {
        return NotificationLabelUtils.getPkgDetail(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public Map<String, Integer> getAllNotificationLabelResult() {
        return NotificationLabelUtils.getPkgResultMap(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public Map<Integer, Double> getCandidateTaxiApp() {
        String string;
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("label_id", UserProfileConstants.LabelId.APP_TAXI_HABIT.getValue());
                SDKLog.i(TAG, "getCandidateCarOrderApp: args:" + bundle);
                Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_USERPROFILE, IProviderFeature.USERPROFILE_SPECIFIC_QUERY, bundle);
                if (call != null && call.containsKey(IProviderFeature.USERPROFILE_QUERY_RESULT) && (string = call.getString(IProviderFeature.USERPROFILE_QUERY_RESULT)) != null && string.length() > 0) {
                    return TaxiHabitUtils.convertToMapping((Map) new Gson().fromJson(string, new TypeToken<HashMap<String, Double>>() { // from class: com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.UserDomainManager.1
                    }.getType()));
                }
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getCandidateCarOrderApp: RemoteException; failed to get app list; " + e10);
        } catch (JsonSyntaxException e11) {
            SDKLog.e(TAG, "getCandidateCarOrderApp: JsonSyntaxException; failed to convert json to map; " + e11);
        }
        return new HashMap();
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public boolean getChargeStatus(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                SDKLog.d(TAG, "targetRecall" + bundle.getDouble("targetRecall") + " coefficient " + bundle.getDouble("targetCoefficient"));
                Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_CHARGE_PREDICTION, null, bundle);
                if (call != null) {
                    try {
                        return call.getBoolean(IProviderFeature.COMMON_RESULT, false);
                    } catch (Throwable th) {
                        SDKLog.e(TAG, "getChargeStatus: " + th.getMessage());
                    }
                }
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getChargeStatus failed " + e10.getMessage());
        }
        return false;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public CommuteLabel getCommuteLabel() {
        return CommuteLabelUtils.getCommuteLabel(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public DeepSleepPredictResult getDeepSleepPredictResult() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getDeepSleepPredictResult();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getDeepSleepPredictResult", e10);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x001d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public DeepSleepPredictResultWithPercentile getDeepSleepPredictResultWithPercentile() {
        String str;
        IDeepThinkerBridge deepThinkerBinder;
        try {
            deepThinkerBinder = getDeepThinkerBinder();
        } catch (RemoteException e10) {
            SDKLog.e(TAG, e10.getMessage());
        }
        if (deepThinkerBinder != null) {
            str = deepThinkerBinder.getDeepSleepPredictResultWithPercentile();
            if (!TextUtils.isEmpty(str)) {
                try {
                    return (DeepSleepPredictResultWithPercentile) new Gson().fromJson(str, DeepSleepPredictResultWithPercentile.class);
                } catch (JsonSyntaxException e11) {
                    SDKLog.e(TAG, "format DeepSleepPredictResultWithPercentile error:" + e11.getMessage());
                }
            }
            return null;
        }
        str = null;
        if (!TextUtils.isEmpty(str)) {
        }
        return null;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public TotalPredictResult getDeepSleepTotalPredictResult() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getDeepSleepTotalPredictResult();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getDeepSleepTotalPredictResult", e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public String getHabitForTime(int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null) {
                return "";
            }
            Bundle bundle = new Bundle();
            bundle.putInt(EventType.EventAssociationExtra.BUSINESS_ID, i10);
            bundle.putInt(EventType.EventAssociationExtra.QUERY_TYPE, 1);
            Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_EVENT_ASSOCIATION, null, bundle);
            return (call == null || !call.containsKey(IProviderFeature.COMMON_RESULT)) ? "" : call.getString(IProviderFeature.COMMON_RESULT);
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "RemoteException; failed to get app habit: " + e10);
            return "";
        } catch (Throwable th) {
            SDKLog.e(TAG, "Throwable; failed to get app habit:: " + th);
            return "";
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public int getHabitStatus(Bundle bundle) {
        if (bundle == null) {
            return 2;
        }
        bundle.putInt(EventType.EventAssociationExtra.QUERY_TYPE, 0);
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null) {
                return 4;
            }
            Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_EVENT_ASSOCIATION, null, bundle);
            if (call == null || !call.containsKey(EventType.EventAssociationExtra.LEARN_STATUS)) {
                return 3;
            }
            return call.getInt(EventType.EventAssociationExtra.LEARN_STATUS);
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getHabitStatus: RemoteException; failed to get app habit: " + e10);
            return 4;
        } catch (Throwable th) {
            SDKLog.e(TAG, "getHabitStatus: Throwable; failed to get app habit:: " + th);
            return 4;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public int getIdleScreenResultInLongTime() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getIdleScreenResultInLongTime();
            }
            return -1;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getIdleScreenResultInLongTime", e10);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public int getIdleScreenResultInMiddleTime() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getIdleScreenResultInMiddleTime();
            }
            return -1;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getIdleScreenResultInMiddleTime", e10);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public int getIdleScreenResultInShortTime() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getIdleScreenResultInShortTime();
            }
            return -1;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getIdleScreenResultInShortTime", e10);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public SleepRecord getLastDeepSleepRecord() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getLastDeepSleepRecord();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getLastDeepSleepRecord", e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public LeaveHomeLabel getLeaveHomeHabitResult() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null) {
                return null;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("label_id", UserProfileConstants.LabelId.LEAVE_HOME.getValue());
            Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_USERPROFILE, IProviderFeature.USERPROFILE_SPECIFIC_QUERY, bundle);
            if (call == null || !call.containsKey(IProviderFeature.USERPROFILE_QUERY_RESULT)) {
                return null;
            }
            String string = call.getString(IProviderFeature.USERPROFILE_QUERY_RESULT);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            SDKLog.d(TAG, "getLeaveHomeHabitResult:" + string);
            return (LeaveHomeLabel) new Gson().fromJson(string, LeaveHomeLabel.class);
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getLeaveHomeHabitResult: RemoteException:" + e10);
            return null;
        } catch (JsonSyntaxException e11) {
            SDKLog.e(TAG, "getLeaveHomeHabitResult: JsonSyntaxException:" + e11);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public PerformanceOfLongCharging getPerformanceForLongCharging(int i10, float f10, int i11) {
        if (i10 == -1) {
            return new PerformanceOfLongCharging();
        }
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                SDKLog.i(TAG, "businessId : " + i10 + " " + EventType.EventAssociationExtra.THRESHOLD + " : " + f10 + " " + EventType.EventAssociationExtra.LEARN_PERIOD + ":" + i11);
                Bundle bundle = new Bundle();
                bundle.putInt(EventType.EventAssociationExtra.BUSINESS_ID, i10);
                bundle.putFloat(EventType.EventAssociationExtra.THRESHOLD, f10);
                bundle.putInt(EventType.EventAssociationExtra.LEARN_PERIOD, i11);
                bundle.putInt(EventType.EventAssociationExtra.QUERY_TYPE, 1);
                Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_EVENT_ASSOCIATION, null, bundle);
                if (call != null && (call.getSerializable(IProviderFeature.COMMON_RESULT) instanceof PerformanceOfLongCharging)) {
                    PerformanceOfLongCharging performanceOfLongCharging = (PerformanceOfLongCharging) call.getSerializable(IProviderFeature.COMMON_RESULT);
                    if (performanceOfLongCharging != null) {
                        SDKLog.i(TAG, performanceOfLongCharging.toString());
                        return performanceOfLongCharging;
                    }
                    SDKLog.i(TAG, "qualifiedActions size is null!");
                }
            }
        } catch (RemoteException e10) {
            SDKLog.i(TAG, "RemoteException; failed to get performance of longCharging: " + e10.getMessage());
        } catch (Throwable th) {
            SDKLog.i(TAG, "Throwable; failed to get performance of longCharging: " + th.getMessage());
        }
        return new PerformanceOfLongCharging();
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public DeepSleepPredictResult getPredictResultWithFeedBack() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getPredictResultWithFeedBack();
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, e10.getMessage());
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public SleepHabitLabel getSleepHabitResult() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null) {
                return null;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("label_id", UserProfileConstants.LabelId.SLEEP_HABIT.getValue());
            Bundle call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_USERPROFILE, IProviderFeature.USERPROFILE_SPECIFIC_QUERY, bundle);
            if (call == null || !call.containsKey(IProviderFeature.USERPROFILE_QUERY_RESULT)) {
                return null;
            }
            String string = call.getString(IProviderFeature.USERPROFILE_QUERY_RESULT);
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            SDKLog.d(TAG, "getSleepHabitResult:" + string);
            return (SleepHabitLabel) new Gson().fromJson(string, SleepHabitLabel.class);
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getSleepHabitResult: RemoteException:" + e10);
            return null;
        } catch (JsonSyntaxException e11) {
            SDKLog.e(TAG, "getSleepHabitResult: JsonSyntaxException:" + e11);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public boolean isMeituanLover(Bundle bundle) {
        Bundle call;
        if (bundle == null) {
            return false;
        }
        bundle.putInt(EventType.EventAssociationExtra.QUERY_TYPE, 1);
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null && (call = deepThinkerBinder.call(IProviderFeature.FEATURE_ABILITY_EVENT_ASSOCIATION, null, bundle)) != null) {
                return call.getBoolean(IProviderFeature.COMMON_RESULT, false);
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "RemoteException; failed to get meituan habit: " + e10.getMessage());
        } catch (Throwable th) {
            SDKLog.e(TAG, "Throwable; failed to get meituan habit:: " + th.getMessage());
        }
        return false;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public IntentResult queryAwarenessIntent(int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.queryAwarenessIntent(i10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "queryAwarenessIntent failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public ServiceResult queryAwarenessService(int i10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.queryAwarenessService(i10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "queryAwarenessService failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public IntentResult sortAwarenessIntent(IntentResult intentResult, boolean z10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.sortAwarenessIntent(intentResult, z10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "sortAwarenessIntent failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IUserDomainManager
    public ServiceResult sortAwarenessService(ServiceResult serviceResult, boolean z10) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.sortAwarenessService(serviceResult, z10);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "sortAwarenessService failed " + e10);
            return null;
        }
    }
}
