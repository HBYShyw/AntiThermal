package com.oplus.epona;

import com.oplus.epona.internal.RealCall;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class Route {
    private final int mMaxRequests = 64;
    private ExecutorService mExecutorService = executorService();
    private ArrayDeque<RealCall.AsyncCall> mReadyAsyncCalls = new ArrayDeque<>();
    private ArrayDeque<RealCall.AsyncCall> mRunningAsyncCalls = new ArrayDeque<>();

    private ThreadFactory createThreadFactory(final String str, final Boolean bool) {
        return new ThreadFactory() { // from class: com.oplus.epona.a
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread lambda$createThreadFactory$0;
                lambda$createThreadFactory$0 = Route.lambda$createThreadFactory$0(str, bool, runnable);
                return lambda$createThreadFactory$0;
            }
        };
    }

    private synchronized ExecutorService executorService() {
        if (this.mExecutorService == null) {
            this.mExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue(), createThreadFactory("Epona Route", Boolean.FALSE));
        }
        return this.mExecutorService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Thread lambda$createThreadFactory$0(String str, Boolean bool, Runnable runnable) {
        Thread thread = new Thread(runnable, str);
        thread.setDaemon(bool.booleanValue());
        return thread;
    }

    private synchronized void promoteCalls() {
        if (this.mRunningAsyncCalls.size() >= 64) {
            return;
        }
        if (this.mReadyAsyncCalls.isEmpty()) {
            return;
        }
        Iterator<RealCall.AsyncCall> it = this.mReadyAsyncCalls.iterator();
        while (it.hasNext()) {
            RealCall.AsyncCall next = it.next();
            this.mRunningAsyncCalls.add(next);
            this.mExecutorService.execute(next);
            this.mReadyAsyncCalls.remove(next);
            if (this.mRunningAsyncCalls.size() >= 64) {
                return;
            }
        }
    }

    public synchronized void asyncExecute(RealCall.AsyncCall asyncCall) {
        if (this.mRunningAsyncCalls.size() < 64) {
            this.mRunningAsyncCalls.add(asyncCall);
            this.mExecutorService.execute(asyncCall);
        } else {
            this.mReadyAsyncCalls.add(asyncCall);
        }
    }

    public void executed(RealCall realCall) {
    }

    public void finished(RealCall.AsyncCall asyncCall, boolean z10) {
        synchronized (this) {
            this.mRunningAsyncCalls.remove(asyncCall);
            if (!z10) {
                this.mReadyAsyncCalls.add(asyncCall);
            }
        }
        promoteCalls();
    }

    public void finished(RealCall realCall) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RealCall newCall(Request request) {
        return RealCall.newCall(this, request);
    }
}
