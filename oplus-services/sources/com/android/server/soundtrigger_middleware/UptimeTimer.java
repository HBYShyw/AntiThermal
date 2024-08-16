package com.android.server.soundtrigger_middleware;

import android.os.Handler;
import android.os.HandlerThread;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UptimeTimer {
    private final Handler mHandler;
    private final HandlerThread mHandlerThread;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    interface Task {
        void cancel();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UptimeTimer(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Task createTask(Runnable runnable, long j) {
        Object obj = new Object();
        TaskImpl taskImpl = new TaskImpl(this.mHandler, obj);
        this.mHandler.postDelayed(runnable, obj, j);
        return taskImpl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void quit() {
        this.mHandlerThread.quitSafely();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class TaskImpl implements Task {
        private final Handler mHandler;
        private final Object mToken;

        public TaskImpl(Handler handler, Object obj) {
            this.mHandler = handler;
            this.mToken = obj;
        }

        @Override // com.android.server.soundtrigger_middleware.UptimeTimer.Task
        public void cancel() {
            this.mHandler.removeCallbacksAndMessages(this.mToken);
        }
    }
}
