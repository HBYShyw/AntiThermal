package com.android.server.broadcastradio.aidl;

import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.LocalLog;
import android.util.Log;
import com.android.server.utils.Slogf;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class RadioLogger {
    private final boolean mDebug;
    private final LocalLog mEventLogger;
    private final String mTag;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RadioLogger(String str, int i) {
        this.mTag = str;
        this.mDebug = Log.isLoggable(str, 3);
        this.mEventLogger = new LocalLog(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logRadioEvent(String str, Object... objArr) {
        this.mEventLogger.log(TextUtils.formatSimple(str, objArr));
        if (this.mDebug) {
            Slogf.d(this.mTag, str, objArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        this.mEventLogger.dump(indentingPrintWriter);
    }
}
