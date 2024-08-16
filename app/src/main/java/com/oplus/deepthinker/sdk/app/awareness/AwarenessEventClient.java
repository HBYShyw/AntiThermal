package com.oplus.deepthinker.sdk.app.awareness;

import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventCallBack;
import com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEvent;
import com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEventCategory;
import com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEventId;
import com.oplus.deepthinker.sdk.app.awareness.capability.impl.ActivityRecognizeEventListener;
import com.oplus.thermalcontrol.ThermalControlConfig;
import i6.IDeepThinkerBridge;
import j6.AsyncTasks;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import k6.TaskCompletionSource;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: AwarenessEventClient.kt */
@Metadata(bv = {}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\u0006\u0018\u0000 '2\u00020\u0001:\u0001'B\u0017\u0012\u0006\u0010\u001d\u001a\u00020\u001c\u0012\u0006\u0010 \u001a\u00020\u001f¢\u0006\u0004\b%\u0010&J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u000bH\u0002J\u001e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u000bH\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u000bH\u0002J\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\f\u001a\u00020\u000bH\u0002J\u0018\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u001e\u0010\u0012\u001a\u00020\u00072\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00020\u00102\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0006\u0010\u0015\u001a\u00020\u0004J\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J\u000e\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u0017J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u000e\u001a\u00020\u0017J\u0006\u0010\u001a\u001a\u00020\u0004J\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006R\u0014\u0010\u001d\u001a\u00020\u001c8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001d\u0010\u001eR\u0014\u0010 \u001a\u00020\u001f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b \u0010!R \u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\"8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b#\u0010$¨\u0006("}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessEventClient;", "", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEventCategory;", ThermalControlConfig.CONFIG_TYPE_CATEGORY, "", "registerAwarenessEvent", "Lk6/d;", "Lma/f0;", "registerAwarenessEventAsync", "unregisterAwarenessEvent", "unregisterAwarenessEventAsync", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/AwarenessEventCallBack;", "callback", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/EventQueryListener;", "listener", "queryAwarenessEvent", "", "categories", "queryAwarenessEvents", "Landroid/os/Bundle;", "putEventCategory", "registerRecognizedActivityEvent", "registerRecognizedActivityEventAsync", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ActivityRecognizeEventListener;", "queryRecognizedActivityEvent", "queryRecognizedActivityEventAsync", "unregisterRecognizedActivityEvent", "unregisterRecognizedActivityEventAsync", "Landroid/content/Context;", "context", "Landroid/content/Context;", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "manager", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "", "eventRegistryMap", "Ljava/util/Map;", "<init>", "(Landroid/content/Context;Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;)V", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class AwarenessEventClient {
    private static final String TAG = "AwarenessEventClient";
    private final Context context;
    private final Map<CapabilityEventCategory, AwarenessEventCallBack> eventRegistryMap;
    private final IOplusDeepThinkerManager manager;

    public AwarenessEventClient(Context context, IOplusDeepThinkerManager iOplusDeepThinkerManager) {
        za.k.e(context, "context");
        za.k.e(iOplusDeepThinkerManager, "manager");
        this.context = context;
        this.manager = iOplusDeepThinkerManager;
        this.eventRegistryMap = new ArrayMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void putEventCategory(Bundle bundle, CapabilityEventCategory capabilityEventCategory) {
        bundle.putParcelable("awareness_capability", capabilityEventCategory);
    }

    private final void queryAwarenessEvent(CapabilityEventCategory capabilityEventCategory, EventQueryListener eventQueryListener) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("awareness_capability", capabilityEventCategory);
        this.manager.queryEvent(new Event(EventType.AWARENESS_CAPABILITY, bundle), eventQueryListener);
    }

    private final void queryAwarenessEvents(List<CapabilityEventCategory> list, EventQueryListener eventQueryListener) {
        HashSet hashSet = new HashSet();
        for (CapabilityEventCategory capabilityEventCategory : list) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("awareness_capability", capabilityEventCategory);
            hashSet.add(new Event(EventType.AWARENESS_CAPABILITY, bundle));
        }
        this.manager.queryEvents(new EventConfig((HashSet<Event>) hashSet), eventQueryListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: queryRecognizedActivityEventAsync$lambda-0, reason: not valid java name */
    public static final void m10queryRecognizedActivityEventAsync$lambda0(AwarenessEventClient awarenessEventClient, ActivityRecognizeEventListener activityRecognizeEventListener) {
        za.k.e(awarenessEventClient, "this$0");
        za.k.e(activityRecognizeEventListener, "$listener");
        awarenessEventClient.queryRecognizedActivityEvent(activityRecognizeEventListener);
    }

    private final int registerAwarenessEvent(CapabilityEventCategory category) {
        final String valueOf = String.valueOf(category.getEventId());
        AwarenessEventCallBack awarenessEventCallBack = new AwarenessEventCallBack(valueOf) { // from class: com.oplus.deepthinker.sdk.app.awareness.AwarenessEventClient$registerAwarenessEvent$callback$1
            @Override // com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventCallBack
            public void onEventStateChanged(CapabilityEvent<?> capabilityEvent) {
                za.k.e(capabilityEvent, "capabilityEvent");
                SDKLog.i("AwarenessEventClient", za.k.l("onEventStateChanged: event id = ", Integer.valueOf(capabilityEvent.getEventId())));
            }
        };
        int registerAwarenessEvent = registerAwarenessEvent(category, awarenessEventCallBack);
        if (registerAwarenessEvent == 1) {
            synchronized (this.eventRegistryMap) {
                this.eventRegistryMap.put(category, awarenessEventCallBack);
                SDKLog.i(TAG, za.k.l("registerAwarenessEvent: eventRegistryMap size = ", Integer.valueOf(this.eventRegistryMap.size())));
                Unit unit = Unit.f15173a;
            }
        }
        return registerAwarenessEvent;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [com.oplus.deepthinker.sdk.app.awareness.AwarenessEventClient$registerAwarenessEventAsync$callback$1, com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventCallBack] */
    private final k6.d<Unit> registerAwarenessEventAsync(final CapabilityEventCategory category) {
        final String valueOf = String.valueOf(category.getEventId());
        final ?? r12 = new AwarenessEventCallBack(valueOf) { // from class: com.oplus.deepthinker.sdk.app.awareness.AwarenessEventClient$registerAwarenessEventAsync$callback$1
            @Override // com.oplus.deepthinker.sdk.app.awareness.capability.AwarenessEventCallBack
            public void onEventStateChanged(CapabilityEvent<?> capabilityEvent) {
                za.k.e(capabilityEvent, "capabilityEvent");
                SDKLog.i("AwarenessEventClient", za.k.l("onEventStateChanged: event id = ", Integer.valueOf(capabilityEvent.getEventId())));
            }
        };
        k6.d<Unit> registerAwarenessEventAsync = registerAwarenessEventAsync(category, r12);
        registerAwarenessEventAsync.a(new k6.b<Unit>() { // from class: com.oplus.deepthinker.sdk.app.awareness.AwarenessEventClient$registerAwarenessEventAsync$1
            @Override // k6.b
            public void onSuccess(Unit result) {
                Map map;
                Map map2;
                Map map3;
                za.k.e(result, "result");
                map = AwarenessEventClient.this.eventRegistryMap;
                AwarenessEventClient awarenessEventClient = AwarenessEventClient.this;
                CapabilityEventCategory capabilityEventCategory = category;
                AwarenessEventClient$registerAwarenessEventAsync$callback$1 awarenessEventClient$registerAwarenessEventAsync$callback$1 = r12;
                synchronized (map) {
                    map2 = awarenessEventClient.eventRegistryMap;
                    map2.put(capabilityEventCategory, awarenessEventClient$registerAwarenessEventAsync$callback$1);
                    map3 = awarenessEventClient.eventRegistryMap;
                    SDKLog.i("AwarenessEventClient", za.k.l("registerAwarenessEventAsync: eventRegistryMap size = ", Integer.valueOf(map3.size())));
                    Unit unit = Unit.f15173a;
                }
            }
        });
        return registerAwarenessEventAsync;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: registerAwarenessEventAsync$lambda-9, reason: not valid java name */
    public static final void m11registerAwarenessEventAsync$lambda9(AwarenessEventClient awarenessEventClient, AwarenessEventCallBack awarenessEventCallBack, CapabilityEventCategory capabilityEventCategory) {
        za.k.e(awarenessEventClient, "this$0");
        za.k.e(awarenessEventCallBack, "$callback");
        za.k.e(capabilityEventCategory, "$category");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessEventClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessEventCallBack.getTag(), awarenessEventCallBack).setParamsBuilder(new AwarenessEventClient$registerAwarenessEventAsync$task$1$1(awarenessEventClient, capabilityEventCategory)).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    private final int unregisterAwarenessEvent(CapabilityEventCategory category) {
        AwarenessEventCallBack remove;
        synchronized (this.eventRegistryMap) {
            remove = this.eventRegistryMap.containsKey(category) ? this.eventRegistryMap.remove(category) : null;
            SDKLog.i(TAG, za.k.l("unregisterAwarenessEvent: eventRegistryMap size = ", Integer.valueOf(this.eventRegistryMap.size())));
            Unit unit = Unit.f15173a;
        }
        AwarenessEventCallBack awarenessEventCallBack = remove;
        return awarenessEventCallBack == null ? AwarenessStatusCodes.CAPABILITY_NOT_REGISTERED : unregisterAwarenessEvent(awarenessEventCallBack);
    }

    private final k6.d<Unit> unregisterAwarenessEventAsync(CapabilityEventCategory category) {
        AwarenessEventCallBack remove;
        synchronized (this.eventRegistryMap) {
            remove = this.eventRegistryMap.containsKey(category) ? this.eventRegistryMap.remove(category) : null;
            SDKLog.i(TAG, za.k.l("unregisterAwarenessEventAsync: eventRegistryMap size = ", Integer.valueOf(this.eventRegistryMap.size())));
            Unit unit = Unit.f15173a;
        }
        AwarenessEventCallBack awarenessEventCallBack = remove;
        if (awarenessEventCallBack == null) {
            TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
            taskCompletionSource.c(AwarenessStatusCodes.CAPABILITY_NOT_REGISTERED, "CAPABILITY_NOT_REGISTERED");
            return taskCompletionSource.a();
        }
        return unregisterAwarenessEventAsync(awarenessEventCallBack);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: unregisterAwarenessEventAsync$lambda-10, reason: not valid java name */
    public static final void m12unregisterAwarenessEventAsync$lambda10(AwarenessEventClient awarenessEventClient, AwarenessEventCallBack awarenessEventCallBack) {
        za.k.e(awarenessEventClient, "this$0");
        za.k.e(awarenessEventCallBack, "$callback");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessEventClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessEventCallBack.getTag(), awarenessEventCallBack).setParamsBuilder(AwarenessEventClient$unregisterAwarenessEventAsync$task$1$1.INSTANCE).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    public final void queryRecognizedActivityEvent(ActivityRecognizeEventListener activityRecognizeEventListener) {
        za.k.e(activityRecognizeEventListener, "listener");
        queryAwarenessEvent(new CapabilityEventCategory(CapabilityEventId.ACTIVITY_RECOGNIZE_EVENT_ID, 0, 2, null), activityRecognizeEventListener);
    }

    public final k6.d<Unit> queryRecognizedActivityEventAsync(final ActivityRecognizeEventListener listener) {
        za.k.e(listener, "listener");
        return AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.c
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessEventClient.m10queryRecognizedActivityEventAsync$lambda0(AwarenessEventClient.this, listener);
            }
        });
    }

    public final int registerRecognizedActivityEvent() {
        return registerAwarenessEvent(new CapabilityEventCategory(CapabilityEventId.ACTIVITY_RECOGNIZE_EVENT_ID, 0, 2, null));
    }

    public final k6.d<Unit> registerRecognizedActivityEventAsync() {
        return registerAwarenessEventAsync(new CapabilityEventCategory(CapabilityEventId.ACTIVITY_RECOGNIZE_EVENT_ID, 0, 2, null));
    }

    public final int unregisterRecognizedActivityEvent() {
        return unregisterAwarenessEvent(new CapabilityEventCategory(CapabilityEventId.ACTIVITY_RECOGNIZE_EVENT_ID, 0, 2, null));
    }

    public final k6.d<Unit> unregisterRecognizedActivityEventAsync() {
        return unregisterAwarenessEventAsync(new CapabilityEventCategory(CapabilityEventId.ACTIVITY_RECOGNIZE_EVENT_ID, 0, 2, null));
    }

    private final k6.d<Unit> registerAwarenessEventAsync(final CapabilityEventCategory category, final AwarenessEventCallBack callback) {
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.b
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessEventClient.m11registerAwarenessEventAsync$lambda9(AwarenessEventClient.this, callback, category);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }

    private final int registerAwarenessEvent(CapabilityEventCategory category, AwarenessEventCallBack callback) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("awareness_capability", category);
        Event event = new Event(EventType.AWARENESS_CAPABILITY, bundle);
        HashSet hashSet = new HashSet();
        hashSet.add(event);
        return this.manager.registerEventCallback(callback, new EventConfig((HashSet<Event>) hashSet));
    }

    private final int unregisterAwarenessEvent(AwarenessEventCallBack callback) {
        return this.manager.unregisterEventCallback(callback);
    }

    private final k6.d<Unit> unregisterAwarenessEventAsync(final AwarenessEventCallBack callback) {
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.a
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessEventClient.m12unregisterAwarenessEventAsync$lambda10(AwarenessEventClient.this, callback);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }
}
