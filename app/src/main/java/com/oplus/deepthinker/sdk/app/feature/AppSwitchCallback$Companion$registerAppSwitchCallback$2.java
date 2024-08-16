package com.oplus.deepthinker.sdk.app.feature;

import android.os.Bundle;
import android.os.RemoteException;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.feature.AppSwitchCallback;
import i6.IDeepThinkerBridge;
import kotlin.Metadata;
import ma.Unit;
import ya.a;
import ya.l;
import za.Lambda;
import za.k;

/* compiled from: AppSwitchCallback.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Li6/a;", "it", "Lma/f0;", "invoke", "(Li6/a;)V", "<anonymous>"}, k = 3, mv = {1, 6, 0})
/* loaded from: classes.dex */
final class AppSwitchCallback$Companion$registerAppSwitchCallback$2 extends Lambda implements l<IDeepThinkerBridge, Unit> {
    final /* synthetic */ AppSwitchCallback $callback;
    final /* synthetic */ AppSwitchCallback.AppSwitchEventConfig $config;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppSwitchCallback.kt */
    @Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/os/Bundle;", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.feature.AppSwitchCallback$Companion$registerAppSwitchCallback$2$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements a<Bundle> {
        final /* synthetic */ AppSwitchCallback.AppSwitchEventConfig $config;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(AppSwitchCallback.AppSwitchEventConfig appSwitchEventConfig) {
            super(0);
            this.$config = appSwitchEventConfig;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // ya.a
        public final Bundle invoke() {
            Bundle bundle = new Bundle();
            AppSwitchCallback.AppSwitchEventConfig appSwitchEventConfig = this.$config;
            InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
            companion.putApiCode(bundle, 3);
            companion.putFunction(bundle, 101);
            appSwitchEventConfig.extract$com_oplus_deepthinker_sdk_release(bundle);
            return bundle;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AppSwitchCallback$Companion$registerAppSwitchCallback$2(AppSwitchCallback appSwitchCallback, AppSwitchCallback.AppSwitchEventConfig appSwitchEventConfig) {
        super(1);
        this.$callback = appSwitchCallback;
        this.$config = appSwitchEventConfig;
    }

    @Override // ya.l
    public /* bridge */ /* synthetic */ Unit invoke(IDeepThinkerBridge iDeepThinkerBridge) {
        invoke2(iDeepThinkerBridge);
        return Unit.f15173a;
    }

    /* renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(IDeepThinkerBridge iDeepThinkerBridge) {
        k.e(iDeepThinkerBridge, "it");
        try {
            InternalApiCall.apiCall$default(new InternalApiCall().setRemote(iDeepThinkerBridge).setApiCallback(this.$callback.tag, this.$callback).setParamsBuilder(new AnonymousClass1(this.$config)), "AtomFeature", InternalApiCall.VERSION, false, 4, null);
        } catch (RemoteException e10) {
            SDKLog.e("AppSwitchCallback", k.l("registerAppSwitchCallback : ", e10));
        }
    }
}
