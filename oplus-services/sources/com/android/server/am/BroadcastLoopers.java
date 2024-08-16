package com.android.server.am;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BroadcastLoopers {
    private static final String TAG = "BroadcastLoopers";

    @GuardedBy({"sLoopers"})
    private static final ArraySet<Looper> sLoopers = new ArraySet<>();

    public static void addLooper(Looper looper) {
        ArraySet<Looper> arraySet = sLoopers;
        synchronized (arraySet) {
            Objects.requireNonNull(looper);
            Looper looper2 = looper;
            arraySet.add(looper);
        }
    }

    public static void addMyLooper() {
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            ArraySet<Looper> arraySet = sLoopers;
            synchronized (arraySet) {
                if (arraySet.add(myLooper)) {
                    Slog.w(TAG, "Found previously unknown looper " + myLooper.getThread());
                }
            }
        }
    }

    public static void waitForIdle(PrintWriter printWriter) {
        waitForCondition(printWriter, new BiConsumer() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BroadcastLoopers.lambda$waitForIdle$1((Looper) obj, (CountDownLatch) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$waitForIdle$1(Looper looper, final CountDownLatch countDownLatch) {
        looper.getQueue().addIdleHandler(new MessageQueue.IdleHandler() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda3
            @Override // android.os.MessageQueue.IdleHandler
            public final boolean queueIdle() {
                boolean lambda$waitForIdle$0;
                lambda$waitForIdle$0 = BroadcastLoopers.lambda$waitForIdle$0(countDownLatch);
                return lambda$waitForIdle$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$waitForIdle$0(CountDownLatch countDownLatch) {
        countDownLatch.countDown();
        return false;
    }

    public static void waitForBarrier(PrintWriter printWriter) {
        waitForCondition(printWriter, new BiConsumer() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BroadcastLoopers.lambda$waitForBarrier$3((Looper) obj, (CountDownLatch) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$waitForBarrier$3(Looper looper, final CountDownLatch countDownLatch) {
        new Handler(looper).post(new Runnable() { // from class: com.android.server.am.BroadcastLoopers$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                countDownLatch.countDown();
            }
        });
    }

    private static void waitForCondition(PrintWriter printWriter, BiConsumer<Looper, CountDownLatch> biConsumer) {
        CountDownLatch countDownLatch;
        ArraySet<Looper> arraySet = sLoopers;
        synchronized (arraySet) {
            int size = arraySet.size();
            countDownLatch = new CountDownLatch(size);
            for (int i = 0; i < size; i++) {
                Looper valueAt = sLoopers.valueAt(i);
                if (valueAt.getQueue().isIdle()) {
                    countDownLatch.countDown();
                } else {
                    biConsumer.accept(valueAt, countDownLatch);
                }
            }
        }
        long j = 0;
        while (countDownLatch.getCount() > 0) {
            long uptimeMillis = SystemClock.uptimeMillis();
            if (uptimeMillis >= 1000 + j) {
                printWriter.println("Waiting for " + countDownLatch.getCount() + " loopers to drain...");
                printWriter.flush();
                j = uptimeMillis;
            }
            SystemClock.sleep(100L);
        }
        printWriter.println("Loopers drained!");
        printWriter.flush();
    }
}
