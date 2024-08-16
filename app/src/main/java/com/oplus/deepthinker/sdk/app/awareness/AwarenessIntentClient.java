package com.oplus.deepthinker.sdk.app.awareness;

import android.content.Context;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.IntentResult;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.awareness.intent.AwarenessIntentCallBack;
import com.oplus.deepthinker.sdk.app.awareness.intent.AwarenessIntentQueryListener;
import i6.IDeepThinkerBridge;
import j6.AsyncTasks;
import java.util.HashSet;
import java.util.function.Supplier;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: AwarenessIntentClient.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0017\u001a\u00020\u0016\u0012\u0006\u0010\u001a\u001a\u00020\u0019¢\u0006\u0004\b\u001c\u0010\u001dJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u0002J\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0003\u001a\u00020\u0002J\u0012\u0010\r\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000b\u001a\u00020\u0004J\u0016\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u0004J\u0018\u0010\r\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fJ\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fJ\u001a\u0010\u0014\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0013\u001a\u00020\u0012J\u001e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0013\u001a\u00020\u0012R\u0014\u0010\u0017\u001a\u00020\u00168\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0017\u0010\u0018R\u0014\u0010\u001a\u001a\u00020\u00198\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u001a\u0010\u001b¨\u0006\u001e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessIntentClient;", "", "Lcom/oplus/deepthinker/sdk/app/awareness/intent/AwarenessIntentCallBack;", "callback", "", "registerAwarenessIntent", "Lk6/d;", "Lma/f0;", "registerAwarenessIntentAsync", "unregisterAwarenessIntent", "unregisterAwarenessIntentAsync", "topK", "Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/IntentResult;", "queryAwarenessIntent", "queryAwarenessIntentAsync", "Lcom/oplus/deepthinker/sdk/app/awareness/intent/AwarenessIntentQueryListener;", "listener", "result", "", "extend", "sortAwarenessIntent", "sortAwarenessIntentAsync", "Landroid/content/Context;", "context", "Landroid/content/Context;", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "manager", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "<init>", "(Landroid/content/Context;Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class AwarenessIntentClient {
    private final Context context;
    private final IOplusDeepThinkerManager manager;

    public AwarenessIntentClient(Context context, IOplusDeepThinkerManager iOplusDeepThinkerManager) {
        za.k.e(context, "context");
        za.k.e(iOplusDeepThinkerManager, "manager");
        this.context = context;
        this.manager = iOplusDeepThinkerManager;
    }

    public static /* synthetic */ IntentResult queryAwarenessIntent$default(AwarenessIntentClient awarenessIntentClient, int i10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        return awarenessIntentClient.queryAwarenessIntent(i10);
    }

    public static /* synthetic */ k6.d queryAwarenessIntentAsync$default(AwarenessIntentClient awarenessIntentClient, int i10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        return awarenessIntentClient.queryAwarenessIntentAsync(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: queryAwarenessIntentAsync$lambda-2, reason: not valid java name */
    public static final IntentResult m16queryAwarenessIntentAsync$lambda2(AwarenessIntentClient awarenessIntentClient, int i10) {
        za.k.e(awarenessIntentClient, "this$0");
        return awarenessIntentClient.manager.queryAwarenessIntent(i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: queryAwarenessIntentAsync$lambda-3, reason: not valid java name */
    public static final void m17queryAwarenessIntentAsync$lambda3(AwarenessIntentClient awarenessIntentClient, int i10, AwarenessIntentQueryListener awarenessIntentQueryListener) {
        za.k.e(awarenessIntentClient, "this$0");
        za.k.e(awarenessIntentQueryListener, "$listener");
        awarenessIntentClient.queryAwarenessIntent(i10, awarenessIntentQueryListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: registerAwarenessIntentAsync$lambda-0, reason: not valid java name */
    public static final void m18registerAwarenessIntentAsync$lambda0(AwarenessIntentClient awarenessIntentClient, AwarenessIntentCallBack awarenessIntentCallBack) {
        za.k.e(awarenessIntentClient, "this$0");
        za.k.e(awarenessIntentCallBack, "$callback");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessIntentClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessIntentCallBack.getTag(), awarenessIntentCallBack).setParamsBuilder(AwarenessIntentClient$registerAwarenessIntentAsync$task$1$1.INSTANCE).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    public static /* synthetic */ IntentResult sortAwarenessIntent$default(AwarenessIntentClient awarenessIntentClient, IntentResult intentResult, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return awarenessIntentClient.sortAwarenessIntent(intentResult, z10);
    }

    public static /* synthetic */ k6.d sortAwarenessIntentAsync$default(AwarenessIntentClient awarenessIntentClient, IntentResult intentResult, boolean z10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            z10 = false;
        }
        return awarenessIntentClient.sortAwarenessIntentAsync(intentResult, z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: sortAwarenessIntentAsync$lambda-4, reason: not valid java name */
    public static final IntentResult m19sortAwarenessIntentAsync$lambda4(AwarenessIntentClient awarenessIntentClient, IntentResult intentResult, boolean z10) {
        za.k.e(awarenessIntentClient, "this$0");
        za.k.e(intentResult, "$result");
        return awarenessIntentClient.manager.sortAwarenessIntent(intentResult, z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: unregisterAwarenessIntentAsync$lambda-1, reason: not valid java name */
    public static final void m20unregisterAwarenessIntentAsync$lambda1(AwarenessIntentClient awarenessIntentClient, AwarenessIntentCallBack awarenessIntentCallBack) {
        za.k.e(awarenessIntentClient, "this$0");
        za.k.e(awarenessIntentCallBack, "$callback");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessIntentClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessIntentCallBack.getTag(), awarenessIntentCallBack).setParamsBuilder(AwarenessIntentClient$unregisterAwarenessIntentAsync$task$1$1.INSTANCE).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    public final IntentResult queryAwarenessIntent(int topK) {
        return this.manager.queryAwarenessIntent(topK);
    }

    public final k6.d<IntentResult> queryAwarenessIntentAsync(final int topK) {
        return k6.g.a(new Supplier() { // from class: com.oplus.deepthinker.sdk.app.awareness.j
            @Override // java.util.function.Supplier
            public final Object get() {
                IntentResult m16queryAwarenessIntentAsync$lambda2;
                m16queryAwarenessIntentAsync$lambda2 = AwarenessIntentClient.m16queryAwarenessIntentAsync$lambda2(AwarenessIntentClient.this, topK);
                return m16queryAwarenessIntentAsync$lambda2;
            }
        });
    }

    public final int registerAwarenessIntent(AwarenessIntentCallBack callback) {
        za.k.e(callback, "callback");
        HashSet hashSet = new HashSet();
        hashSet.add(new Event(EventType.AWARENESS_INTENT, null));
        return this.manager.registerEventCallback(callback, new EventConfig((HashSet<Event>) hashSet));
    }

    public final k6.d<Unit> registerAwarenessIntentAsync(final AwarenessIntentCallBack callback) {
        za.k.e(callback, "callback");
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.h
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessIntentClient.m18registerAwarenessIntentAsync$lambda0(AwarenessIntentClient.this, callback);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }

    public final IntentResult sortAwarenessIntent(IntentResult result, boolean extend) {
        za.k.e(result, "result");
        return this.manager.sortAwarenessIntent(result, extend);
    }

    public final k6.d<IntentResult> sortAwarenessIntentAsync(final IntentResult result, final boolean extend) {
        za.k.e(result, "result");
        return k6.g.a(new Supplier() { // from class: com.oplus.deepthinker.sdk.app.awareness.k
            @Override // java.util.function.Supplier
            public final Object get() {
                IntentResult m19sortAwarenessIntentAsync$lambda4;
                m19sortAwarenessIntentAsync$lambda4 = AwarenessIntentClient.m19sortAwarenessIntentAsync$lambda4(AwarenessIntentClient.this, result, extend);
                return m19sortAwarenessIntentAsync$lambda4;
            }
        });
    }

    public final int unregisterAwarenessIntent(AwarenessIntentCallBack callback) {
        za.k.e(callback, "callback");
        return this.manager.unregisterEventCallback(callback);
    }

    public final k6.d<Unit> unregisterAwarenessIntentAsync(final AwarenessIntentCallBack callback) {
        za.k.e(callback, "callback");
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.i
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessIntentClient.m20unregisterAwarenessIntentAsync$lambda1(AwarenessIntentClient.this, callback);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }

    public static /* synthetic */ void queryAwarenessIntent$default(AwarenessIntentClient awarenessIntentClient, int i10, AwarenessIntentQueryListener awarenessIntentQueryListener, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        awarenessIntentClient.queryAwarenessIntent(i10, awarenessIntentQueryListener);
    }

    public static /* synthetic */ k6.d queryAwarenessIntentAsync$default(AwarenessIntentClient awarenessIntentClient, int i10, AwarenessIntentQueryListener awarenessIntentQueryListener, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = 0;
        }
        return awarenessIntentClient.queryAwarenessIntentAsync(i10, awarenessIntentQueryListener);
    }

    public final void queryAwarenessIntent(int i10, AwarenessIntentQueryListener awarenessIntentQueryListener) {
        za.k.e(awarenessIntentQueryListener, "listener");
        Bundle bundle = new Bundle();
        bundle.putInt(IntentResult.BUNDLE_KEY_TOP_K_INTENT, i10);
        this.manager.queryEvent(new Event(EventType.AWARENESS_INTENT, bundle), awarenessIntentQueryListener);
    }

    public final k6.d<Unit> queryAwarenessIntentAsync(final int topK, final AwarenessIntentQueryListener listener) {
        za.k.e(listener, "listener");
        return AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.g
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessIntentClient.m17queryAwarenessIntentAsync$lambda3(AwarenessIntentClient.this, topK, listener);
            }
        });
    }
}
