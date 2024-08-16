package androidx.window.core;

import android.util.Log;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import kotlin.Metadata;
import za.k;

/* compiled from: SpecificationComputer.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u0018\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"Landroidx/window/core/AndroidLogger;", "Landroidx/window/core/Logger;", "", TriggerEvent.NOTIFICATION_TAG, "message", "Lma/f0;", "a", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class AndroidLogger implements Logger {

    /* renamed from: a, reason: collision with root package name */
    public static final AndroidLogger f4301a = new AndroidLogger();

    private AndroidLogger() {
    }

    @Override // androidx.window.core.Logger
    public void a(String str, String str2) {
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        k.e(str2, "message");
        Log.d(str, str2);
    }
}
