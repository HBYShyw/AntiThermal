package com.oplus.oms.split.core;

import android.util.Log;
import com.oplus.oms.split.common.SplitLog;

/* loaded from: classes.dex */
public class SampleLogger implements SplitLog.Logger {
    private final boolean mEnableLog;

    public SampleLogger(boolean enableLog) {
        this.mEnableLog = enableLog;
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void v(String tag, String format, Object... params) {
        if (!this.mEnableLog) {
            return;
        }
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        Log.v(tag, log);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void i(String tag, String format, Object... params) {
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        Log.i(tag, log);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void d(String tag, String format, Object... params) {
        if (!this.mEnableLog) {
            return;
        }
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        Log.d(tag, log);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void w(String tag, String format, Object... params) {
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        Log.w(tag, log);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void e(String tag, String format, Object... params) {
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        Log.e(tag, log);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void v(String tag, String msg, Throwable throwable) {
        if (!this.mEnableLog) {
            return;
        }
        Log.v(tag, msg, throwable);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void i(String tag, String msg, Throwable throwable) {
        Log.i(tag, msg, throwable);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void w(String tag, String msg, Throwable throwable) {
        Log.w(tag, msg, throwable);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void d(String tag, String msg, Throwable throwable) {
        if (!this.mEnableLog) {
            return;
        }
        Log.d(tag, msg, throwable);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
    }

    @Override // com.oplus.oms.split.common.SplitLog.Logger
    public void printErrStackTrace(String tag, Throwable tr, String format, Object... params) {
        String log = (params == null || params.length == 0) ? format : String.format(format, params);
        if (log == null) {
            log = "";
        }
        Log.e(tag, log + "  " + Log.getStackTraceString(tr));
    }
}
