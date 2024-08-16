package com.oplus.deepthinker.sdk.app.feature;

import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.api.InternalApiCall;
import kotlin.Metadata;
import ya.a;
import za.Lambda;

/* compiled from: AppSwitchCallback.kt */
@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/os/Bundle;", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
final class AppSwitchCallback$Companion$unregisterAppSwitchCallback$1 extends Lambda implements a<Bundle> {
    public static final AppSwitchCallback$Companion$unregisterAppSwitchCallback$1 INSTANCE = new AppSwitchCallback$Companion$unregisterAppSwitchCallback$1();

    AppSwitchCallback$Companion$unregisterAppSwitchCallback$1() {
        super(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // ya.a
    public final Bundle invoke() {
        Bundle bundle = new Bundle();
        InternalApiCall.Companion companion = InternalApiCall.INSTANCE;
        companion.putApiCode(bundle, 3);
        companion.putFunction(bundle, 102);
        return bundle;
    }
}
