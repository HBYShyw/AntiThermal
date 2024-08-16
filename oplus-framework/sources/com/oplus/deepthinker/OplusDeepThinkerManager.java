package com.oplus.deepthinker;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import com.oplus.deepthinker.platform.server.ClientConnection;
import com.oplus.deepthinker.platform.server.FrameworkInvokeDelegate;
import com.oplus.deepthinker.sdk.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.userprofile.WifiLocationLabel;
import com.oplus.deepthinker.sdk.common.utils.SDKLog;
import com.oplus.deepthinker.userprofile.utils.WifiLocationLabelUtils;
import com.oplus.eventhub.sdk.aidl.DeviceEvent;
import com.oplus.eventhub.sdk.aidl.Event;
import com.oplus.eventhub.sdk.aidl.EventConfig;
import com.oplus.eventhub.sdk.aidl.EventRequestConfig;
import com.oplus.eventhub.sdk.aidl.IEventCallback;
import com.oplus.eventhub.sdk.aidl.TriggerEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
class OplusDeepThinkerManager implements IOplusDeepThinkerManager {
    public static final String ARG_EVENT_ID = "event_id";
    public static final String ARG_EXTRA = "extra";
    public static final String ARG_ID = "id";
    public static final String ARG_PKG = "pkg";
    public static final String COMMON_RESULT = "common_result";
    public static final int EVENTFOUNTAIN_RANGE_END = 199999;
    public static final int EVENTFOUNTAIN_RANGE_START = 100000;
    public static final String FEATURE_ABILITY_INOUTDOOR = "ability_in_outdoor";
    private static final String TAG = "OplusDeepThinkerManager";
    public static final String TARGET_EVENTFOUNTAIN = "eventfountain_call_handle";
    public static final String TRIGGER_EVENT = "trigger_event";
    private ClientConnection mClientConnection;
    private final Executor mExecutor;
    private TriggerEventRunnable mTriggereventRunnable;

    public OplusDeepThinkerManager(Context context, Executor executor) {
        this.mExecutor = executor;
        this.mClientConnection = new ClientConnection(context, executor);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getAlgorithmPlatformVersion() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getPlatformVersion();
            }
            return -1;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAlgorithmPlatformVersion", e);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public PredictAABResult getPredictAABResult() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getPredictAABResult();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getPredictAABResult", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<PredictResult> getAppPredictResultMap(String callerName) {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getAppPredictResultMap(callerName);
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAppPredictResultMap", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public PredictResult getAppPredictResult(String callerName) {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getAppPredictResult(callerName);
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAppPredictResult", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public DeepSleepPredictResult getDeepSleepPredictResult() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getDeepSleepPredictResult();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getDeepSleepPredictResult", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public SleepRecord getLastDeepSleepRecord() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getLastDeepSleepRecord();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getLastDeepSleepRecord", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public TotalPredictResult getDeepSleepTotalPredictResult() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getDeepSleepTotalPredictResult();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getDeepSleepTotalPredictResult", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public DeepSleepPredictResult getPredictResultWithFeedBack() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getPredictResultWithFeedBack();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getAppType(String packageName) {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getAppType(packageName);
            }
            return -1;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAppType", e);
            return -1;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public Map getAppTypeMap(List<String> packageNameList) {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getAppTypeMap(packageNameList);
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAppTypeMap", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getSmartGpsBssidList() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getSmartGpsBssidList();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getSmartGpsBssidList", e);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v3, types: [int] */
    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getInOutDoorState() {
        Bundle bundle;
        String str = TAG;
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null && (bundle = delegate.call(FEATURE_ABILITY_INOUTDOOR, null, null)) != null) {
                try {
                    str = bundle.getInt(COMMON_RESULT, 0);
                    return str;
                } catch (Throwable e) {
                    SDKLog.e(TAG, "getInOutDoorState: " + e.toString());
                }
            }
        } catch (RemoteException e2) {
            SDKLog.e(str, "getInOutDoorState", e2);
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v3, types: [int] */
    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getInOutDoorState(Bundle args) {
        Bundle bundle;
        String str = TAG;
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null && (bundle = delegate.call(FEATURE_ABILITY_INOUTDOOR, null, args)) != null) {
                try {
                    str = bundle.getInt(COMMON_RESULT, 0);
                    return str;
                } catch (Throwable e) {
                    SDKLog.e(TAG, "getInOutDoorState: " + e.toString());
                }
            }
        } catch (RemoteException e2) {
            SDKLog.e(str, "getInOutDoorState", e2);
        }
        return 0;
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void triggerHookEvent(TriggerEvent triggerEvent) {
        if (triggerEvent == null) {
            SDKLog.e(TAG, "triggerHookEvent null parameter.");
            return;
        }
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_EVENT_ID, triggerEvent.getEventId());
                bundle.putInt(ARG_ID, triggerEvent.getPid());
                bundle.putString("pkg", triggerEvent.getPkgName());
                bundle.putBundle(ARG_EXTRA, triggerEvent.getExtraData());
                delegate.call(TARGET_EVENTFOUNTAIN, TRIGGER_EVENT, bundle);
            }
        } catch (RemoteException e) {
            SDKLog.e(TAG, "triggerHookEvent", e);
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void triggerHookEvent(int eventType, int uid, String pkgName, Bundle extra) {
        TriggerEvent event = new TriggerEvent(eventType, uid, pkgName, extra);
        triggerHookEvent(event);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void triggerHookEventAsync(Handler handler, int eventID, int uid, String pkg, Bundle extra) {
        if (handler == null) {
            return;
        }
        TriggerEventRunnable triggerEventRunnable = this.mTriggereventRunnable;
        if (triggerEventRunnable == null) {
            this.mTriggereventRunnable = new TriggerEventRunnable(eventID, uid, pkg, extra);
        } else {
            triggerEventRunnable.mEventID = eventID;
            this.mTriggereventRunnable.mUid = uid;
            this.mTriggereventRunnable.mPkg = pkg;
            this.mTriggereventRunnable.mExtra = extra;
        }
        handler.post(this.mTriggereventRunnable);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean registerCallback(IEventCallback callback, EventRequestConfig config) {
        if (callback == null || config == null || config.getAllEvents().isEmpty()) {
            SDKLog.e(TAG, "registerCallback invalid para. null or without request config.");
            return false;
        }
        HashSet<Event> events = new HashSet<>();
        Iterator<DeviceEvent> it = config.getDeviceEventSet().iterator();
        while (it.hasNext()) {
            DeviceEvent deviceEvent = it.next();
            if (deviceEvent != null) {
                events.add(new Event(deviceEvent.getEventType(), null));
            }
        }
        return registerEventCallback(callback, new EventConfig(events)) == 1;
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean unregisterCallback(IEventCallback callback) {
        if (callback != null) {
            return unregisterEventCallback(callback) == 1;
        }
        SDKLog.e(TAG, "unRegisterCallback null parameter.");
        return false;
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int registerEventCallback(IEventCallback callback, EventConfig config) {
        if (callback == null || config == null || config.getAllEvents().isEmpty()) {
            SDKLog.e(TAG, "registerEventCallback invalid para. null or without request config.");
            return 16;
        }
        try {
            int fingerprint = callback.hashCode();
            int pid = Process.myPid();
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.registerEventCallback(String.valueOf(fingerprint) + pid, callback, config);
            }
            return 128;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "registerEventCallback", e);
            return 128;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int unregisterEventCallback(IEventCallback callback) {
        if (callback == null) {
            SDKLog.e(TAG, "unregisterEventCallback null parameter.");
            return 16;
        }
        try {
            int fingerPrint = callback.hashCode();
            int pid = Process.myPid();
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.unregisterEventCallback(String.valueOf(fingerPrint) + pid);
            }
            return 128;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "unregisterEventCallback", e);
            return 128;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v3, types: [boolean] */
    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<Event> getAvailableEvent() {
        String str = "getAvailableEvent";
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                try {
                    List<Event> eventList = new ArrayList<>();
                    List<Integer> list = delegate.capability();
                    if (list == null) {
                        return null;
                    }
                    for (Integer id : list) {
                        if (id != null && id.intValue() >= 100000 && id.intValue() <= 199999) {
                            eventList.add(new Event(id.intValue() - EVENTFOUNTAIN_RANGE_START, null));
                        }
                    }
                    str = eventList.isEmpty();
                    if (str == 0) {
                        return eventList;
                    }
                } catch (Exception e) {
                    SDKLog.e(TAG, "getAvailableEvent", e);
                }
            }
        } catch (Exception e2) {
            SDKLog.e(TAG, str, e2);
        }
        return null;
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean isAvailableEvent(Event event) {
        List<Event> events = getAvailableEvent();
        if (events != null) {
            return events.contains(event);
        }
        return false;
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByCount() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getAppQueueSortedByCount();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAppQueueSortedByCount", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByComplex() {
        try {
            FrameworkInvokeDelegate delegate = this.mClientConnection.getInvokeDelegate();
            if (delegate != null) {
                return delegate.getAppQueueSortedByComplex();
            }
            return null;
        } catch (RemoteException e) {
            SDKLog.e(TAG, "getAppQueueSortedByComplex", e);
            return null;
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<WifiLocationLabel> getWifiLocationLabels() {
        return WifiLocationLabelUtils.getWifiLocationLabels(this.mClientConnection.getInvokeDelegate());
    }

    /* loaded from: classes.dex */
    class TriggerEventRunnable implements Runnable {
        int mEventID;
        Bundle mExtra;
        String mPkg;
        int mUid;

        TriggerEventRunnable(int eventID, int uid, String pkg, Bundle extra) {
            this.mEventID = eventID;
            this.mUid = uid;
            this.mPkg = pkg;
            this.mExtra = extra;
        }

        @Override // java.lang.Runnable
        public void run() {
            OplusDeepThinkerManager.this.triggerHookEvent(this.mEventID, this.mUid, this.mPkg, this.mExtra);
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void run(Runnable runnable) {
        this.mExecutor.execute(runnable);
    }
}
