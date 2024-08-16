package com.oplus.flexiblewindow;

import android.os.Handler;

/* loaded from: classes.dex */
public class FlexibleHandlerExecutor implements FlexibleExecutor {
    private final Handler mHandler;

    public FlexibleHandlerExecutor(Handler handler) {
        this.mHandler = handler;
    }

    @Override // com.oplus.flexiblewindow.FlexibleExecutor, java.util.concurrent.Executor
    public void execute(Runnable command) {
        if (this.mHandler.getLooper().isCurrentThread()) {
            command.run();
        } else if (!this.mHandler.post(command)) {
            throw new RuntimeException(this.mHandler + " is probably exiting");
        }
    }

    @Override // com.oplus.flexiblewindow.FlexibleExecutor
    public void executeDelayed(Runnable r, long delayMillis) {
        if (!this.mHandler.postDelayed(r, delayMillis)) {
            throw new RuntimeException(this.mHandler + " is probably exiting");
        }
    }

    @Override // com.oplus.flexiblewindow.FlexibleExecutor
    public void removeCallbacks(Runnable r) {
        this.mHandler.removeCallbacks(r);
    }

    @Override // com.oplus.flexiblewindow.FlexibleExecutor
    public boolean hasCallback(Runnable r) {
        return this.mHandler.hasCallbacks(r);
    }
}
