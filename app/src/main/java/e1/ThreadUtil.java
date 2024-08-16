package e1;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* compiled from: ThreadUtil.java */
/* renamed from: e1.k, reason: use source file name */
/* loaded from: classes.dex */
public class ThreadUtil {

    /* renamed from: a, reason: collision with root package name */
    private static volatile Handler f10963a;

    /* renamed from: b, reason: collision with root package name */
    private static volatile ExecutorService f10964b;

    public static Handler a() {
        if (f10963a == null) {
            f10963a = new Handler(Looper.getMainLooper());
        }
        return f10963a;
    }

    private static synchronized ExecutorService b() {
        ExecutorService executorService;
        synchronized (ThreadUtil.class) {
            if (f10964b == null) {
                f10964b = Executors.newCachedThreadPool();
            }
            executorService = f10964b;
        }
        return executorService;
    }

    public static Future<?> c(Runnable runnable) {
        return b().submit(runnable);
    }
}
