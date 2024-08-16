package com.oplus.deepthinker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.provider.oplus.Telephony;
import android.util.Log;
import com.oplus.deepthinker.sdk.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.userprofile.WifiLocationLabel;
import com.oplus.eventhub.sdk.aidl.Event;
import com.oplus.eventhub.sdk.aidl.EventConfig;
import com.oplus.eventhub.sdk.aidl.EventRequestConfig;
import com.oplus.eventhub.sdk.aidl.IEventCallback;
import com.oplus.eventhub.sdk.aidl.TriggerEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public class OplusDeepThinkerManagerDecor implements IOplusDeepThinkerManager {
    public static final String OPLUS_PKG = "com.oplus.deepthinker";
    private static final String TAG = "ManagerDecor";
    private static final long THREAD_LIVE_TIME = 30;
    private static final String THREAD_NAME_FORMAT = "deepthinker-";
    private static final int THREAD_POOL_SIZE = 5;
    private static volatile OplusDeepThinkerManagerDecor sInstance;
    private Context mContext;
    private EnableStateChangeReceiver mEnableStateChangeReceiver;
    private IOplusDeepThinkerManager mImpl;
    private final ExecutorService mExecutor = new ThreadPoolExecutor(0, 5, THREAD_LIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DeepThinkerThreadFactory(THREAD_NAME_FORMAT));
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private Vector<WeakReference<ServiceStateObserver>> mObserverRefs = new Vector<>();
    private AtomicBoolean mIsApplicationEnable = new AtomicBoolean(false);
    private final Runnable mEnableStateRunnable = new Runnable() { // from class: com.oplus.deepthinker.OplusDeepThinkerManagerDecor$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            OplusDeepThinkerManagerDecor.this.lambda$new$0();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        final ServiceStateObserver observer;
        EnableStateChangeReceiver enableStateChangeReceiver = this.mEnableStateChangeReceiver;
        if (enableStateChangeReceiver != null) {
            this.mContext.unregisterReceiver(enableStateChangeReceiver);
            this.mEnableStateChangeReceiver = null;
        }
        List<WeakReference<ServiceStateObserver>> refsCopy = new ArrayList<>(this.mObserverRefs);
        for (WeakReference<ServiceStateObserver> ref : refsCopy) {
            if (ref != null && (observer = ref.get()) != null) {
                ExecutorService executorService = this.mExecutor;
                Objects.requireNonNull(observer);
                executorService.execute(new Runnable() { // from class: com.oplus.deepthinker.OplusDeepThinkerManagerDecor$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ServiceStateObserver.this.onStartup();
                    }
                });
            }
        }
    }

    public static OplusDeepThinkerManagerDecor getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OplusDeepThinkerManagerDecor.class) {
                if (sInstance == null) {
                    sInstance = new OplusDeepThinkerManagerDecor(context);
                }
            }
        }
        return sInstance;
    }

    private OplusDeepThinkerManagerDecor(Context context) {
        this.mContext = context.getApplicationContext();
        setImpl(true);
        checkApplicationEnableState(this.mContext);
    }

    private void checkApplicationEnableState(Context context) {
        registerEnableStateReceiver();
        UserManager userManager = (UserManager) context.getSystemService(Telephony.Carriers.USER);
        if (userManager.isUserUnlocked()) {
            Log.i(TAG, "UserUnlocked, checkApplicationEnableState : enable state true ");
            onApplicationEnabled();
        } else {
            Log.i(TAG, "Userlocked, checkApplicationEnableState : enable state false ");
            onApplicationDisabled();
        }
    }

    private void onApplicationDisabled() {
        this.mIsApplicationEnable.set(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onApplicationEnabled() {
        setImpl(false);
        this.mIsApplicationEnable.set(true);
        this.mMainHandler.post(this.mEnableStateRunnable);
    }

    private void registerEnableStateReceiver() {
        this.mEnableStateChangeReceiver = new EnableStateChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.USER_UNLOCKED");
        this.mContext.registerReceiver(this.mEnableStateChangeReceiver, filter);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void registerServiceStateObserver(final ServiceStateObserver observer) {
        this.mMainHandler.post(new Runnable() { // from class: com.oplus.deepthinker.OplusDeepThinkerManagerDecor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                OplusDeepThinkerManagerDecor.this.lambda$registerServiceStateObserver$2(observer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerServiceStateObserver$2(ServiceStateObserver observer) {
        this.mObserverRefs.add(new WeakReference<>(observer));
        Log.i(TAG, "registerServiceStateObserver success ");
        this.mObserverRefs.removeIf(new Predicate() { // from class: com.oplus.deepthinker.OplusDeepThinkerManagerDecor$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return OplusDeepThinkerManagerDecor.lambda$registerServiceStateObserver$1((WeakReference) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$registerServiceStateObserver$1(WeakReference it) {
        return it == null || it.get() == null;
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getAlgorithmPlatformVersion() {
        return this.mImpl.getAlgorithmPlatformVersion();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public PredictAABResult getPredictAABResult() {
        return this.mImpl.getPredictAABResult();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<PredictResult> getAppPredictResultMap(String callerName) {
        return this.mImpl.getAppPredictResultMap(callerName);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public PredictResult getAppPredictResult(String callerName) {
        return this.mImpl.getAppPredictResult(callerName);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public DeepSleepPredictResult getDeepSleepPredictResult() {
        return this.mImpl.getDeepSleepPredictResult();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public SleepRecord getLastDeepSleepRecord() {
        return this.mImpl.getLastDeepSleepRecord();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public TotalPredictResult getDeepSleepTotalPredictResult() {
        return this.mImpl.getDeepSleepTotalPredictResult();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public DeepSleepPredictResult getPredictResultWithFeedBack() {
        return this.mImpl.getPredictResultWithFeedBack();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getAppType(String packageName) {
        return this.mImpl.getAppType(packageName);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public Map getAppTypeMap(List<String> packageNameList) {
        return this.mImpl.getAppTypeMap(packageNameList);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getSmartGpsBssidList() {
        return this.mImpl.getSmartGpsBssidList();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void triggerHookEvent(TriggerEvent triggerEvent) {
        this.mImpl.triggerHookEvent(triggerEvent);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void triggerHookEvent(int eventType, int uid, String pkgName, Bundle extra) {
        this.mImpl.triggerHookEvent(eventType, uid, pkgName, extra);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void triggerHookEventAsync(Handler handler, int eventID, int uid, String pkg, Bundle extra) {
        this.mImpl.triggerHookEventAsync(handler, eventID, uid, pkg, extra);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean registerCallback(IEventCallback callback, EventRequestConfig config) {
        return this.mImpl.registerCallback(callback, config);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean unregisterCallback(IEventCallback callback) {
        return this.mImpl.unregisterCallback(callback);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int registerEventCallback(IEventCallback callback, EventConfig config) {
        return this.mImpl.registerEventCallback(callback, config);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int unregisterEventCallback(IEventCallback callback) {
        return this.mImpl.unregisterEventCallback(callback);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<Event> getAvailableEvent() {
        return this.mImpl.getAvailableEvent();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean isAvailableEvent(Event event) {
        return this.mImpl.isAvailableEvent(event);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public Bundle call(Bundle request) {
        return this.mImpl.call(request);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByTime() {
        return this.mImpl.getAppQueueSortedByTime();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByCount() {
        return this.mImpl.getAppQueueSortedByCount();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<String> getAppQueueSortedByComplex() {
        return this.mImpl.getAppQueueSortedByComplex();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public List<WifiLocationLabel> getWifiLocationLabels() {
        return this.mImpl.getWifiLocationLabels();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getInOutDoorState() {
        return this.mImpl.getInOutDoorState();
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public int getInOutDoorState(Bundle args) {
        return this.mImpl.getInOutDoorState(args);
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public void run(Runnable runnable) {
        this.mImpl.run(runnable);
    }

    public void onServiceDied() {
        final ServiceStateObserver observer;
        List<WeakReference<ServiceStateObserver>> refsCopy = new ArrayList<>(this.mObserverRefs);
        for (WeakReference<ServiceStateObserver> ref : refsCopy) {
            if (ref != null && (observer = ref.get()) != null) {
                ExecutorService executorService = this.mExecutor;
                Objects.requireNonNull(observer);
                executorService.execute(new Runnable() { // from class: com.oplus.deepthinker.OplusDeepThinkerManagerDecor$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        ServiceStateObserver.this.onServiceDied();
                    }
                });
            }
        }
    }

    private void setImpl(boolean stub) {
        IOplusDeepThinkerManager iOplusDeepThinkerManager = this.mImpl;
        if (iOplusDeepThinkerManager != null) {
            boolean current = !(iOplusDeepThinkerManager instanceof OplusDeepThinkerManager);
            if (stub == current) {
                return;
            }
        }
        if (stub) {
            this.mImpl = new IOplusDeepThinkerManager() { // from class: com.oplus.deepthinker.OplusDeepThinkerManagerDecor.1
            };
        } else {
            this.mImpl = new OplusDeepThinkerManager(this.mContext, this.mExecutor);
        }
    }

    @Override // com.oplus.deepthinker.IOplusDeepThinkerManager
    public boolean isApplicationEnable() {
        return this.mIsApplicationEnable.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class EnableStateChangeReceiver extends BroadcastReceiver {
        private EnableStateChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.i(OplusDeepThinkerManagerDecor.TAG, "receive USER_UNLOCKED broadcast!");
            OplusDeepThinkerManagerDecor.getInstance(context).onApplicationEnabled();
        }
    }

    /* loaded from: classes.dex */
    private class DeepThinkerThreadFactory implements ThreadFactory {
        private final String mNamePrefix;
        private final AtomicInteger mNextId = new AtomicInteger(1);

        DeepThinkerThreadFactory(String whatFeatureOfGroup) {
            this.mNamePrefix = whatFeatureOfGroup;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable task) {
            String name = this.mNamePrefix + this.mNextId.getAndIncrement();
            return new Thread(null, task, name, 0L);
        }
    }
}
