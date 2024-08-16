package com.oplus.deepthinker.sdk.app.feature;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import com.oplus.deepthinker.sdk.app.feature.AppSwitchCallback;
import kotlin.Metadata;
import ya.a;
import za.Lambda;

/* compiled from: AppSwitchCallback.kt */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/os/Bundle;", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
final class AppSwitchCallback$Companion$registerAppSwitchCallback$1 extends Lambda implements a<Bundle> {
    final /* synthetic */ AppSwitchCallback.AppSwitchEventConfig $config;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AppSwitchCallback$Companion$registerAppSwitchCallback$1(AppSwitchCallback.AppSwitchEventConfig appSwitchEventConfig) {
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
