package com.android.server.location.gnss;

import android.content.Context;
import android.os.Looper;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class NetworkTimeHelper {
    static final boolean USE_TIME_DETECTOR_IMPL = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface InjectTimeCallback {
        void injectTime(long j, long j2, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void demandUtcTimeInjection();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void dump(PrintWriter printWriter);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onNetworkAvailable();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setPeriodicTimeInjectionMode(boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static NetworkTimeHelper create(Context context, Looper looper, InjectTimeCallback injectTimeCallback) {
        return new NtpNetworkTimeHelper(context, looper, injectTimeCallback);
    }
}
