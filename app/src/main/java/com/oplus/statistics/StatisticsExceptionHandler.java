package com.oplus.statistics;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.statistics.agent.ExceptionAgent;
import com.oplus.statistics.data.ExceptionBean;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;

/* loaded from: classes2.dex */
public class StatisticsExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "StatisticsExceptionHand";
    private Context mContext;
    private Thread.UncaughtExceptionHandler mHandler = Thread.getDefaultUncaughtExceptionHandler();

    public StatisticsExceptionHandler(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private String getStackTrace(Throwable th) {
        String str;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            try {
                th.printStackTrace(printWriter);
                str = stringWriter.toString();
            } catch (Exception e10) {
                LogUtil.e(TAG, new g0(e10));
                printWriter.close();
                str = null;
            }
            return str;
        } finally {
            printWriter.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$uncaughtException$0() {
        return "StatisticsExceptionHandler: get the uncaughtException.";
    }

    public void setStatisticsExceptionHandler() {
        if (this == this.mHandler) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.i0
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$uncaughtException$0;
                lambda$uncaughtException$0 = StatisticsExceptionHandler.lambda$uncaughtException$0();
                return lambda$uncaughtException$0;
            }
        });
        String stackTrace = getStackTrace(th);
        long currentTimeMillis = System.currentTimeMillis();
        if (!TextUtils.isEmpty(stackTrace)) {
            ExceptionBean exceptionBean = new ExceptionBean(this.mContext);
            exceptionBean.setCount(1);
            exceptionBean.setEventTime(currentTimeMillis);
            exceptionBean.setException(stackTrace);
            ExceptionAgent.recordException(this.mContext, exceptionBean);
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.mHandler;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        }
    }
}
