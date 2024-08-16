package com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import com.oplus.deepthinker.sdk.app.IProviderFeature;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.ServiceHolder;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager;
import com.oplus.deepthinker.sdk.app.userprofile.labels.AppTypePreferenceLabel;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.AppTypePreferenceLabelUtils;
import i6.IDeepThinkerBridge;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class DeviceDomainManager implements IDeviceDomainManager {
    private static final int ANDROID_R = 30;
    public static final String ARG_EVENT_ID = "event_id";
    public static final String ARG_EXTRA = "extra";
    public static final String ARG_ID = "id";
    public static final String ARG_PKG = "pkg";
    public static final int EVENTFOUNTAIN_RANGE_END = 199999;
    public static final int EVENTFOUNTAIN_RANGE_START = 100000;
    private static final String SPLIT = "|";
    private static final String TAG = "DeviceDomainManager";
    public static final String TARGET_EVENTFOUNTAIN = "eventfountain_call_handle";
    public static final String TRIGGER_EVENT = "trigger_event";
    private final Context mContext;
    private ServiceHolder mServiceHolder;
    private TriggerEventRunnable mTriggerEventRunnable;

    /* loaded from: classes.dex */
    class TriggerEventRunnable implements Runnable {
        int mEventID;
        Bundle mExtra;
        String mPkg;
        int mUid;

        TriggerEventRunnable(int i10, int i11, String str, Bundle bundle) {
            this.mEventID = i10;
            this.mUid = i11;
            this.mPkg = str;
            this.mExtra = bundle;
        }

        @Override // java.lang.Runnable
        public void run() {
            DeviceDomainManager.this.triggerHookEvent(this.mEventID, this.mUid, this.mPkg, this.mExtra);
        }
    }

    public DeviceDomainManager(Context context, ServiceHolder serviceHolder) {
        this.mContext = context;
        this.mServiceHolder = serviceHolder;
    }

    private Map<String, Integer> getAppImportanceResultByMethod(final String str, Map<String, Integer> map) {
        SparseArray<HashMap<String, Integer>> splitSourceByUser = splitSourceByUser(map);
        int size = splitSourceByUser.size();
        final CountDownLatch countDownLatch = new CountDownLatch(size);
        final HashMap hashMap = new HashMap();
        for (int i10 = 0; i10 < size; i10++) {
            final int keyAt = splitSourceByUser.keyAt(i10);
            final HashMap<String, Integer> valueAt = splitSourceByUser.valueAt(i10);
            new Thread(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.a
                @Override // java.lang.Runnable
                public final void run() {
                    DeviceDomainManager.this.lambda$getAppImportanceResultByMethod$0(str, keyAt, valueAt, hashMap, countDownLatch);
                }
            }).start();
        }
        try {
            countDownLatch.await(30L, TimeUnit.SECONDS);
        } catch (Exception e10) {
            SDKLog.e(TAG, "getAppImportanceResultByMethod", e10);
        }
        return hashMap;
    }

    private Map<String, Integer> getAppImportanceResultByUser(String str, int i10, HashMap<String, Integer> hashMap) {
        Bundle call;
        Uri uri = IProviderFeature.FEATURE_PROVIDER;
        Uri build = uri.buildUpon().encodedAuthority("" + i10 + "@" + uri.getEncodedAuthority()).build();
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IProviderFeature.FEATURE_APP_IMPORTANCE_PARAM_SERIALIZABLE_MAP, hashMap);
        try {
            if (getDeepThinkerBinder() == null || (call = contentResolver.call(build, IProviderFeature.FEATURE_APP_IMPORTANCE, str, bundle)) == null) {
                return null;
            }
            return (HashMap) call.getSerializable(IProviderFeature.FEATURE_APP_IMPORTANCE_PARAM_SERIALIZABLE_MAP);
        } catch (Throwable th) {
            SDKLog.e(TAG, "getAppImportanceResultByUser - " + str, th);
            return null;
        }
    }

    private IDeepThinkerBridge getDeepThinkerBinder() {
        return this.mServiceHolder.getDeepThinkerBridge();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAppImportanceResultByMethod$0(String str, int i10, HashMap hashMap, Map map, CountDownLatch countDownLatch) {
        Map<String, Integer> appImportanceResultByUser = getAppImportanceResultByUser(str, i10, hashMap);
        HashMap hashMap2 = new HashMap();
        if (appImportanceResultByUser != null) {
            for (Map.Entry<String, Integer> entry : appImportanceResultByUser.entrySet()) {
                if (!TextUtils.isEmpty(entry.getKey())) {
                    hashMap2.put(entry.getKey() + SPLIT + i10, entry.getValue());
                }
            }
        }
        map.putAll(hashMap2);
        countDownLatch.countDown();
    }

    private SparseArray<HashMap<String, Integer>> splitSourceByUser(Map<String, Integer> map) {
        SparseArray<HashMap<String, Integer>> sparseArray = new SparseArray<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            int i10 = -1;
            String str = null;
            String key = entry.getKey();
            if (!TextUtils.isEmpty(key)) {
                int lastIndexOf = key.lastIndexOf(SPLIT);
                try {
                    str = key.substring(0, lastIndexOf);
                    i10 = Integer.parseInt(key.substring(lastIndexOf + 1));
                } catch (Exception e10) {
                    SDKLog.e(TAG, "splitSourceByUser", e10);
                }
                if (!TextUtils.isEmpty(str) && i10 >= 0) {
                    HashMap<String, Integer> hashMap = sparseArray.get(i10);
                    if (hashMap == null) {
                        hashMap = new HashMap<>();
                        sparseArray.put(i10, hashMap);
                    }
                    hashMap.put(str, entry.getValue());
                }
            }
        }
        return sparseArray;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public Map<String, Integer> getAllAppImportance(Map<String, Integer> map) {
        return getAppImportanceResultByMethod(IProviderFeature.FEATURE_APP_IMPORTANCE_GET_RESULT, map);
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public Map<String, Integer> getAllAppImportanceLocked(Map<String, Integer> map) {
        return getAppImportanceResultByMethod(IProviderFeature.FEATURE_APP_IMPORTANCE_GET_RESULT_LOCKED, map);
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public int getAppType(String str) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppType(str);
            }
            return -1;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppType failed " + e10);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public Map getAppTypeMap(List<String> list) {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                return deepThinkerBinder.getAppTypeMap(list);
            }
            return null;
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "getAppTypeMap failed " + e10);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public List<Event> getAvailableEvent() {
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                try {
                    ArrayList arrayList = new ArrayList();
                    List<Integer> capability = deepThinkerBinder.capability();
                    if (capability != null) {
                        for (Integer num : capability) {
                            if (num != null && num.intValue() >= 100000 && num.intValue() <= 199999) {
                                arrayList.add(new Event(num.intValue() - 100000, null));
                            }
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        return arrayList;
                    }
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getAvailableEvent", e10);
                }
            }
        } catch (Exception e11) {
            SDKLog.e(TAG, "getAvailableEvent", e11);
        }
        return null;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public AppTypePreferenceLabel getRecentAppTypePreference() {
        return AppTypePreferenceLabelUtils.getRecentPreference(getDeepThinkerBinder());
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public boolean isAvailableEvent(Event event) {
        List<Event> availableEvent = getAvailableEvent();
        if (availableEvent == null || event == null) {
            return false;
        }
        return availableEvent.contains(event);
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
        if (event != null && iEventQueryListener != null) {
            try {
                IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
                if (deepThinkerBinder != null) {
                    deepThinkerBinder.queryEvent(event, iEventQueryListener);
                    return;
                }
                return;
            } catch (RemoteException e10) {
                SDKLog.e(TAG, "queryEvent failed: " + e10);
                return;
            }
        }
        SDKLog.e(TAG, "queryEvent: null parameter");
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
        if (iEventQueryListener != null && eventConfig != null && !eventConfig.getAllEvents().isEmpty()) {
            try {
                IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
                if (deepThinkerBinder != null) {
                    deepThinkerBinder.queryEvents(eventConfig, iEventQueryListener);
                    return;
                }
                return;
            } catch (RemoteException e10) {
                SDKLog.e(TAG, "queryEvents failed: " + e10);
                return;
            }
        }
        SDKLog.e(TAG, "queryEvents：invalid parameter");
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public int registerEventCallback(IEventCallback iEventCallback, EventConfig eventConfig) {
        if (iEventCallback != null && eventConfig != null && !eventConfig.getAllEvents().isEmpty()) {
            try {
                int hashCode = iEventCallback.hashCode();
                int myPid = Process.myPid();
                IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
                if (deepThinkerBinder == null) {
                    return 128;
                }
                return deepThinkerBinder.registerEventCallback(String.valueOf(hashCode) + myPid, iEventCallback, eventConfig);
            } catch (RemoteException e10) {
                SDKLog.e(TAG, "registerEventCallback", e10);
                return 128;
            }
        }
        SDKLog.e(TAG, "registerEventCallback invalid para. null or without request config.");
        return 16;
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public void triggerHookEvent(TriggerEvent triggerEvent) {
        if (triggerEvent == null) {
            SDKLog.e(TAG, "triggerHookEvent null parameter.");
            return;
        }
        try {
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_EVENT_ID, triggerEvent.getEventId());
                bundle.putInt("id", triggerEvent.getPid());
                bundle.putString(ARG_PKG, triggerEvent.getPkgName());
                bundle.putBundle(ARG_EXTRA, triggerEvent.getExtraData());
                deepThinkerBinder.call("eventfountain_call_handle", TRIGGER_EVENT, bundle);
            }
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "triggerHookEvent", e10);
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public void triggerHookEventAsync(Handler handler, int i10, int i11, String str, Bundle bundle) {
        if (handler == null) {
            return;
        }
        TriggerEventRunnable triggerEventRunnable = this.mTriggerEventRunnable;
        if (triggerEventRunnable == null) {
            this.mTriggerEventRunnable = new TriggerEventRunnable(i10, i11, str, bundle);
        } else {
            triggerEventRunnable.mEventID = i10;
            triggerEventRunnable.mUid = i11;
            triggerEventRunnable.mPkg = str;
            triggerEventRunnable.mExtra = bundle;
        }
        handler.post(this.mTriggerEventRunnable);
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public int unregisterEventCallback(IEventCallback iEventCallback) {
        if (iEventCallback == null) {
            SDKLog.e(TAG, "unregisterEventCallback null parameter.");
            return 16;
        }
        try {
            int hashCode = iEventCallback.hashCode();
            int myPid = Process.myPid();
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null) {
                return 128;
            }
            return deepThinkerBinder.unregisterEventCallback(String.valueOf(hashCode) + myPid);
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "unregisterEventCallback", e10);
            return 128;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public int unregisterEventCallbackWithArgs(IEventCallback iEventCallback, EventConfig eventConfig) {
        if (iEventCallback == null) {
            SDKLog.e(TAG, "unregisterEventCallbackWithArgs null parameter.");
            return 16;
        }
        try {
            int hashCode = iEventCallback.hashCode();
            int myPid = Process.myPid();
            IDeepThinkerBridge deepThinkerBinder = getDeepThinkerBinder();
            if (deepThinkerBinder == null) {
                return 128;
            }
            return deepThinkerBinder.unregisterEventCallbackWithArgs(String.valueOf(hashCode) + myPid, eventConfig);
        } catch (RemoteException e10) {
            SDKLog.e(TAG, "unregisterEventCallback", e10);
            return 128;
        }
    }

    @Override // com.oplus.deepthinker.sdk.app.deepthinkermanager.IDeviceDomainManager
    public void triggerHookEvent(int i10, int i11, String str, Bundle bundle) {
        triggerHookEvent(new TriggerEvent(i10, i11, str, bundle));
    }
}
