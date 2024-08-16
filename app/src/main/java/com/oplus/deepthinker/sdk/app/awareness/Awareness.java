package com.oplus.deepthinker.sdk.app.awareness;

import android.content.Context;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import kotlin.Metadata;

/* compiled from: Awareness.kt */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/Awareness;", "", "()V", "getEventClient", "Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessEventClient;", "context", "Landroid/content/Context;", "manager", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "getFenceClient", "Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessFenceClient;", "getIntentClient", "Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessIntentClient;", "getServiceClient", "Lcom/oplus/deepthinker/sdk/app/awareness/AwarenessServiceClient;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class Awareness {
    public static final Awareness INSTANCE = new Awareness();

    private Awareness() {
    }

    public static final AwarenessEventClient getEventClient(Context context, IOplusDeepThinkerManager manager) {
        za.k.e(context, "context");
        za.k.e(manager, "manager");
        return new AwarenessEventClient(context, manager);
    }

    public static final AwarenessFenceClient getFenceClient(Context context, IOplusDeepThinkerManager manager) {
        za.k.e(context, "context");
        za.k.e(manager, "manager");
        return new AwarenessFenceClient(context, manager);
    }

    public static final AwarenessIntentClient getIntentClient(Context context, IOplusDeepThinkerManager manager) {
        za.k.e(context, "context");
        za.k.e(manager, "manager");
        return new AwarenessIntentClient(context, manager);
    }

    public static final AwarenessServiceClient getServiceClient(Context context, IOplusDeepThinkerManager manager) {
        za.k.e(context, "context");
        za.k.e(manager, "manager");
        return new AwarenessServiceClient(context, manager);
    }
}
