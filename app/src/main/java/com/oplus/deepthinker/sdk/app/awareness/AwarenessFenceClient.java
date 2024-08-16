package com.oplus.deepthinker.sdk.app.awareness;

import android.content.Context;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFenceCallBack;
import com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFenceQueryListener;
import i6.IDeepThinkerBridge;
import j6.AsyncTasks;
import java.util.HashSet;
import kotlin.Metadata;
import ma.Unit;

/* compiled from: AwarenessFenceClient.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0015\u001a\u00020\u0014\u0012\u0006\u0010\u0018\u001a\u00020\u0017¢\u0006\u0004\b\u001a\u0010\u001bJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0014\u0010\u0007\u001a\u00020\u0004*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0016\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bJ\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\f2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00040\f2\u0006\u0010\t\u001a\u00020\bJ\u0016\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0010J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\f2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u0010R\u0014\u0010\u0015\u001a\u00020\u00148\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0018\u001a\u00020\u00178\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0018\u0010\u0019¨\u0006\u001c"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessFenceClient;", "", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "awarenessFence", "Lma/f0;", "fillAwarenessFence", "Landroid/os/Bundle;", "putAwarenessFence", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceCallBack;", "callback", "", "registerAwarenessFence", "Lk6/d;", "registerAwarenessFenceAsync", "unregisterAwarenessFence", "unregisterAwarenessFenceAsync", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceQueryListener;", "listener", "queryAwarenessFence", "queryAwarenessFenceAsync", "Landroid/content/Context;", "context", "Landroid/content/Context;", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "manager", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "<init>", "(Landroid/content/Context;Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class AwarenessFenceClient {
    private final Context context;
    private final IOplusDeepThinkerManager manager;

    public AwarenessFenceClient(Context context, IOplusDeepThinkerManager iOplusDeepThinkerManager) {
        za.k.e(context, "context");
        za.k.e(iOplusDeepThinkerManager, "manager");
        this.context = context;
        this.manager = iOplusDeepThinkerManager;
    }

    private final void fillAwarenessFence(AwarenessFence awarenessFence) {
        String packageName;
        if (awarenessFence.getPackageName() == null && (packageName = this.context.getPackageName()) != null) {
            awarenessFence.setPackageName(packageName);
        }
        if (awarenessFence.getFenceId() == null) {
            awarenessFence.setFenceId(String.valueOf(awarenessFence.hashCode()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void putAwarenessFence(Bundle bundle, AwarenessFence awarenessFence) {
        bundle.putParcelable("awareness_fence", awarenessFence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: queryAwarenessFenceAsync$lambda-2, reason: not valid java name */
    public static final void m13queryAwarenessFenceAsync$lambda2(AwarenessFenceClient awarenessFenceClient, AwarenessFence awarenessFence, AwarenessFenceQueryListener awarenessFenceQueryListener) {
        za.k.e(awarenessFenceClient, "this$0");
        za.k.e(awarenessFence, "$awarenessFence");
        za.k.e(awarenessFenceQueryListener, "$listener");
        awarenessFenceClient.queryAwarenessFence(awarenessFence, awarenessFenceQueryListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: registerAwarenessFenceAsync$lambda-0, reason: not valid java name */
    public static final void m14registerAwarenessFenceAsync$lambda0(AwarenessFenceClient awarenessFenceClient, AwarenessFenceCallBack awarenessFenceCallBack, AwarenessFence awarenessFence) {
        za.k.e(awarenessFenceClient, "this$0");
        za.k.e(awarenessFenceCallBack, "$callback");
        za.k.e(awarenessFence, "$awarenessFence");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessFenceClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessFenceCallBack.getTag(), awarenessFenceCallBack).setParamsBuilder(new AwarenessFenceClient$registerAwarenessFenceAsync$task$1$1(awarenessFenceClient, awarenessFence)).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: unregisterAwarenessFenceAsync$lambda-1, reason: not valid java name */
    public static final void m15unregisterAwarenessFenceAsync$lambda1(AwarenessFenceClient awarenessFenceClient, AwarenessFenceCallBack awarenessFenceCallBack) {
        za.k.e(awarenessFenceClient, "this$0");
        za.k.e(awarenessFenceCallBack, "$callback");
        InternalApiCall internalApiCall = new InternalApiCall();
        IDeepThinkerBridge deepThinkerBridge = awarenessFenceClient.manager.getDeepThinkerBridge();
        za.k.d(deepThinkerBridge, "manager.deepThinkerBridge");
        internalApiCall.setRemote(deepThinkerBridge).setApiCallback(awarenessFenceCallBack.getTag(), awarenessFenceCallBack).setParamsBuilder(AwarenessFenceClient$unregisterAwarenessFenceAsync$task$1$1.INSTANCE).apiCall("eventfountain_call_handle", InternalApiCall.VERSION, true);
    }

    public final void queryAwarenessFence(AwarenessFence awarenessFence, AwarenessFenceQueryListener awarenessFenceQueryListener) {
        za.k.e(awarenessFence, "awarenessFence");
        za.k.e(awarenessFenceQueryListener, "listener");
        fillAwarenessFence(awarenessFence);
        Bundle bundle = new Bundle();
        bundle.putParcelable("awareness_fence", awarenessFence);
        this.manager.queryEvent(new Event(EventType.AWARENESS_FENCE, bundle), awarenessFenceQueryListener);
    }

    public final k6.d<Unit> queryAwarenessFenceAsync(final AwarenessFence awarenessFence, final AwarenessFenceQueryListener listener) {
        za.k.e(awarenessFence, "awarenessFence");
        za.k.e(listener, "listener");
        fillAwarenessFence(awarenessFence);
        return AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.d
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessFenceClient.m13queryAwarenessFenceAsync$lambda2(AwarenessFenceClient.this, awarenessFence, listener);
            }
        });
    }

    public final int registerAwarenessFence(AwarenessFence awarenessFence, AwarenessFenceCallBack callback) {
        za.k.e(awarenessFence, "awarenessFence");
        za.k.e(callback, "callback");
        fillAwarenessFence(awarenessFence);
        Bundle bundle = new Bundle();
        bundle.putParcelable("awareness_fence", awarenessFence);
        HashSet hashSet = new HashSet();
        hashSet.add(new Event(EventType.AWARENESS_FENCE, bundle));
        return this.manager.registerEventCallback(callback, new EventConfig((HashSet<Event>) hashSet));
    }

    public final k6.d<Unit> registerAwarenessFenceAsync(final AwarenessFence awarenessFence, final AwarenessFenceCallBack callback) {
        za.k.e(awarenessFence, "awarenessFence");
        za.k.e(callback, "callback");
        fillAwarenessFence(awarenessFence);
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.f
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessFenceClient.m14registerAwarenessFenceAsync$lambda0(AwarenessFenceClient.this, callback, awarenessFence);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }

    public final int unregisterAwarenessFence(AwarenessFenceCallBack callback) {
        za.k.e(callback, "callback");
        return this.manager.unregisterEventCallback(callback);
    }

    public final k6.d<Unit> unregisterAwarenessFenceAsync(final AwarenessFenceCallBack callback) {
        za.k.e(callback, "callback");
        k6.d<Unit> d10 = AsyncTasks.d(new Runnable() { // from class: com.oplus.deepthinker.sdk.app.awareness.e
            @Override // java.lang.Runnable
            public final void run() {
                AwarenessFenceClient.m15unregisterAwarenessFenceAsync$lambda1(AwarenessFenceClient.this, callback);
            }
        });
        callback.setTask$com_oplus_deepthinker_sdk_release(d10);
        return d10;
    }
}
