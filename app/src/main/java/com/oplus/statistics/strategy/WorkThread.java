package com.oplus.statistics.strategy;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.SparseArray;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class WorkThread extends HandlerThread {
    public static final int MSG_WHAT_CHATTY_EVENT = 1;
    private static final String TAG = "WorkThread";
    private Handler mHandler;
    private final List<Runnable> mPendingTaskList;
    private final SparseArray<PendingTask> mPendingTaskMap;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface MsgWhatType {
    }

    /* loaded from: classes2.dex */
    static class PendingTask {
        private final long delayMillis;
        private final Runnable runnable;

        public PendingTask(Runnable runnable, long j10) {
            this.runnable = runnable;
            this.delayMillis = j10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        private static final WorkThread INSTANCE = new WorkThread();

        private SingletonHolder() {
        }
    }

    public static void execute(Runnable runnable) {
        getInstance().post(runnable);
    }

    public static WorkThread getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$onLooperPrepared$0() {
        return "onLooperPrepared, but looper is null";
    }

    public synchronized boolean hasMessages(int i10) {
        Handler handler = this.mHandler;
        if (handler != null) {
            return handler.hasMessages(i10);
        }
        return this.mPendingTaskMap.get(i10) != null;
    }

    @Override // android.os.HandlerThread
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        Looper looper = getLooper();
        if (looper == null) {
            LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.strategy.e
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$onLooperPrepared$0;
                    lambda$onLooperPrepared$0 = WorkThread.lambda$onLooperPrepared$0();
                    return lambda$onLooperPrepared$0;
                }
            });
            return;
        }
        synchronized (this) {
            this.mHandler = new Handler(looper);
            Iterator<Runnable> it = this.mPendingTaskList.iterator();
            while (it.hasNext()) {
                this.mHandler.post(it.next());
            }
            this.mPendingTaskList.clear();
            for (int i10 = 0; i10 < this.mPendingTaskMap.size(); i10++) {
                PendingTask valueAt = this.mPendingTaskMap.valueAt(i10);
                this.mHandler.postDelayed(valueAt.runnable, valueAt.delayMillis);
            }
            this.mPendingTaskMap.clear();
        }
    }

    public synchronized void post(Runnable runnable) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(runnable);
        } else {
            this.mPendingTaskList.add(runnable);
        }
    }

    public synchronized void postDelay(int i10, Runnable runnable, long j10) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.postDelayed(runnable, j10);
        } else {
            this.mPendingTaskMap.put(i10, new PendingTask(runnable, j10));
        }
    }

    public synchronized void removeMessages(int i10) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeMessages(i10);
        } else {
            this.mPendingTaskMap.remove(i10);
        }
    }

    private WorkThread() {
        super("OplusTrack-thread");
        this.mPendingTaskList = new ArrayList();
        this.mPendingTaskMap = new SparseArray<>();
        start();
    }
}
