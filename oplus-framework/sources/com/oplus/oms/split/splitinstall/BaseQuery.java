package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.oplus.oms.split.common.SplitLog;

/* loaded from: classes.dex */
public abstract class BaseQuery implements Runnable {
    private static final String TAG = "BaseQuery";
    private static final long TIME_OUT = 3000;
    protected Context mContext;
    protected QueryStatus mQueryStatus;
    protected volatile boolean mHasCallBack = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public abstract void processRunnable();

    public BaseQuery(Context context, QueryStatus status) {
        this.mContext = context;
        this.mQueryStatus = status;
    }

    public void putTimeOutCallBack() {
        this.mHandler.postDelayed(new Runnable() { // from class: com.oplus.oms.split.splitinstall.BaseQuery$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseQuery.this.lambda$putTimeOutCallBack$0();
            }
        }, TIME_OUT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putTimeOutCallBack$0() {
        pushQueryCallBack(3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void pushQueryCallBack(int status) {
        if (this.mQueryStatus == null || this.mHasCallBack) {
            return;
        }
        this.mQueryStatus.setQueryResult(status);
        this.mHasCallBack = true;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            processRunnable();
        } catch (Exception e) {
            SplitLog.e(TAG, "query error, message = " + e.getMessage() + ", Trace = " + Log.getStackTraceString(e), new Object[0]);
            pushQueryCallBack(3);
        }
    }
}
