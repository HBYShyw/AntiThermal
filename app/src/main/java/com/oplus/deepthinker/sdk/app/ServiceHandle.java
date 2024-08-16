package com.oplus.deepthinker.sdk.app;

import android.content.Context;
import com.oplus.deepthinker.sdk.app.api.DefaultTransactionHandle;
import kotlin.Metadata;
import za.k;

/* compiled from: ServiceHandle.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/ServiceHandle;", "Lcom/oplus/deepthinker/sdk/app/api/DefaultTransactionHandle;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ServiceHandle extends DefaultTransactionHandle {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ServiceHandle(Context context) {
        super(context, "deepthinker.intent.action.BIND_INTERFACE_SERVER");
        k.e(context, "context");
    }
}
