package com.oplus.deepthinker.sdk.app.awareness;

import android.content.Context;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.awareness.service.AwarenessServiceCallBack;
import com.oplus.deepthinker.sdk.app.awareness.service.AwarenessServiceQueryListener;
import i6.IDeepThinkerBridge;
import j6.AsyncTasks;
import java.util.HashSet;
import java.util.function.Supplier;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: AwarenessServiceClient.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0017\u001a\u00020\u0016\u0012\u0006\u0010\u001a\u001a\u00020\u0019¢\u0006\u0004\b\u001c\u0010\u001dJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u0002J\u0012\u0010\r\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000b\u001a\u00020\u0004J\u0016\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u0004J\u0018\u0010\r\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fJ\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fJ\u001a\u0010\u0014\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0013\u001a\u00020\u0012J\u001e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0013\u001a\u00020\u0012R\u0014\u0010\u0017\u001a\u00020\u00168\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0017\u0010\u0018R\u0014\u0010\u001a\u001a\u00020\u00198\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001a\u0010\u001b¨\u0006\u001e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessServiceClient;", "", "Lcom/oplus/deepthinker/sdk/app/awareness/service/AwarenessServiceCallBack;", "callback", "", "registerAwarenessService", "Lk6/d;", "Lma/f0;", "registerAwarenessServiceAsync", "unregisterAwarenessService", "unregisterAwarenessServiceAsync", "topK", "Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/ServiceResult;", "queryAwarenessService", "queryAwarenessServiceAsync", "Lcom/oplus/deepthinker/sdk/app/awareness/service/AwarenessServiceQueryListener;", "listener", "result", "", "extend", "sortAwarenessService", "sortAwarenessServiceAsync", "Landroid/content/Context;", "context", "Landroid/content/Context;", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "manager", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "<init>", "(Landroid/content/Context;Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class AwarenessServiceClient {
    private final Context context;
    private final IOplusDeepThinkerManager manager;

    public AwarenessServiceClient(Context context, IOplusDeepThinkerManager iOplusDeepThinkerManager) {
        za.k.e(context, "context");
        za.k.e(iOplusDeepThinkerManager, "manager");
        this.context = context;
        this.manager = iOplusDeepThinkerManager;
    }

    public static /* synthetic */ ServiceResult queryAwarenessService$default(AwarenessServiceClient awarenessServiceClient, int i10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        return awarenessServiceClient.queryAwarenessService(i10);
    }

    public static /* synthetic */ k6.d queryAwarenessServiceAsync$default(AwarenessServiceClient awarenessServiceClient, int i10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        return awarenessServiceClient.queryAwarenessServiceAsync(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: queryAwarenessServiceAsync$lambda-2, reason: not valid java name */
    public static final ServiceResult m21queryAwarenessServiceAsync$lambda2(AwarenessServiceClient awarenessServiceClient, int i10) {
        za.k.e(awarenessServiceClient, "this$0");
        return awarenessServiceClient.manager.queryAwarenessService(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: queryAwarenessServiceAsync$lambda-3, reason: not valid java name */
    public static final void m22queryAwarenessServiceAsync$lambda3(AwarenessServiceClient awarenessServiceClient, int i10, AwarenessServiceQueryListener awarenessServiceQueryListener) {
        za.k.e(awarenessServiceClient, "this$0");
        za.k.e(awarenessServiceQueryListener, "$listener");
        awarenessServiceClient.queryAwarenessService(i10, awarenessServiceQueryListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: registerAwarenessServiceAsync$lambda-0, reason: not valid java name */
    public static final void m23registerAwarenessServiceAsync$lambda0(AwarenessServiceClient awarenessServiceClient, AwarenessServiceCallBack awarenessServiceCallBack) {
        za.k.e(awarenessServiceClient, "this$0");
        za.k.e(awarenessServiceCallBack, "$callback");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessServiceClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessServiceCallBack.getTag(), awarenessServiceCallBack).setParamsBuilder(AwarenessServiceClient$registerAwarenessServiceAsync$task$1$1.INSTANCE).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    public static /* synthetic */ ServiceResult sortAwarenessService$default(AwarenessServiceClient awarenessServiceClient, ServiceResult serviceResult, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return awarenessServiceClient.sortAwarenessService(serviceResult, z10);
    }

    public static /* synthetic */ k6.d sortAwarenessServiceAsync$default(AwarenessServiceClient awarenessServiceClient, ServiceResult serviceResult, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return awarenessServiceClient.sortAwarenessServiceAsync(serviceResult, z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: sortAwarenessServiceAsync$lambda-4, reason: not valid java name */
    public static final ServiceResult m24sortAwarenessServiceAsync$lambda4(AwarenessServiceClient awarenessServiceClient, ServiceResult serviceResult, boolean z10) {
        za.k.e(awarenessServiceClient, "this$0");
        za.k.e(serviceResult, "$result");
        return awarenessServiceClient.manager.sortAwarenessService(serviceResult, z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: unregisterAwarenessServiceAsync$lambda-1, reason: not valid java name */
    public static final void m25unregisterAwarenessServiceAsync$lambda1(AwarenessServiceClient awarenessServiceClient, AwarenessServiceCallBack awarenessServiceCallBack) {
        za.k.e(awarenessServiceClient, "this$0");
        za.k.e(awarenessServiceCallBack, "$callback");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessServiceClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessServiceCallBack.getTag(), awarenessServiceCallBack).setParamsBuilder(AwarenessServiceClient$unregisterAwarenessServiceAsync$task$1$1.INSTANCE).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    public final ServiceResult queryAwarenessService(int topK) {
        return this.manager.queryAwarenessService(topK);
    }

    public final k6.d<ServiceResult> queryAwarenessServiceAsync(final int topK) {
        return k6.g.a(new Supplier() { // from class: com.oplus.deepthinker.sdk.app.awareness.o
            @Override // java.util.function.Supplier
            public final Object get() {
                ServiceResult m21queryAwarenessServiceAsync$lambda2;
                m21queryAwarenessServiceAsync$lambda2 = AwarenessServiceClient.m21queryAwarenessServiceAsync$lambda2(AwarenessServiceClient.this, topK);
                return m21queryAwarenessServiceAsync$lambda2;
            }
        });
    }

    public final int registerAwarenessService(AwarenessServiceCallBack callback) {
        za.k.e(callback, "callback");
        HashSet hashSet = new HashSet();
        hashSet.add(new Event(EventType.AWARENESS_SERVICE, null));
        return this.manager.registerEventCallback(callback, new EventConfig((HashSet<Event>) hashSet));
    }

    public final k6.d<Unit> registerAwarenessServiceAsync(final AwarenessServiceCallBack callback) {
        za.k.e(callback, "callback");
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.m
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessServiceClient.m23registerAwarenessServiceAsync$lambda0(AwarenessServiceClient.this, callback);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }

    public final ServiceResult sortAwarenessService(ServiceResult result, boolean extend) {
        za.k.e(result, "result");
        return this.manager.sortAwarenessService(result, extend);
    }

    public final k6.d<ServiceResult> sortAwarenessServiceAsync(final ServiceResult result, final boolean extend) {
        za.k.e(result, "result");
        return k6.g.a(new Supplier() { // from class: com.oplus.deepthinker.sdk.app.awareness.p
            @Override // java.util.function.Supplier
            public final Object get() {
                ServiceResult m24sortAwarenessServiceAsync$lambda4;
                m24sortAwarenessServiceAsync$lambda4 = AwarenessServiceClient.m24sortAwarenessServiceAsync$lambda4(AwarenessServiceClient.this, result, extend);
                return m24sortAwarenessServiceAsync$lambda4;
            }
        });
    }

    public final int unregisterAwarenessService(AwarenessServiceCallBack callback) {
        za.k.e(callback, "callback");
        return this.manager.unregisterEventCallback(callback);
    }

    public final k6.d<Unit> unregisterAwarenessServiceAsync(final AwarenessServiceCallBack callback) {
        za.k.e(callback, "callback");
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.n
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessServiceClient.m25unregisterAwarenessServiceAsync$lambda1(AwarenessServiceClient.this, callback);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }

    public static /* synthetic */ void queryAwarenessService$default(AwarenessServiceClient awarenessServiceClient, int i10, AwarenessServiceQueryListener awarenessServiceQueryListener, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        awarenessServiceClient.queryAwarenessService(i10, awarenessServiceQueryListener);
    }

    public static /* synthetic */ k6.d queryAwarenessServiceAsync$default(AwarenessServiceClient awarenessServiceClient, int i10, AwarenessServiceQueryListener awarenessServiceQueryListener, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        return awarenessServiceClient.queryAwarenessServiceAsync(i10, awarenessServiceQueryListener);
    }

    public final void queryAwarenessService(int i10, AwarenessServiceQueryListener awarenessServiceQueryListener) {
        za.k.e(awarenessServiceQueryListener, "listener");
        Bundle bundle = new Bundle();
        bundle.putInt(ServiceResult.BUNDLE_KEY_TOP_K_SERVICE, i10);
        this.manager.queryEvent(new Event(EventType.AWARENESS_SERVICE, bundle), awarenessServiceQueryListener);
    }

    public final k6.d<Unit> queryAwarenessServiceAsync(final int topK, final AwarenessServiceQueryListener listener) {
        za.k.e(listener, "listener");
        return AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.l
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessServiceClient.m22queryAwarenessServiceAsync$lambda3(AwarenessServiceClient.this, topK, listener);
            }
        });
    }
}
