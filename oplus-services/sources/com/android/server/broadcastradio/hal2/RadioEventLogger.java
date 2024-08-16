package com.android.server.broadcastradio.hal2;

import android.util.IndentingPrintWriter;
import android.util.LocalLog;
import android.util.Log;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class RadioEventLogger {
    private final LocalLog mEventLogger;
    private final String mTag;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RadioEventLogger(String str, int i) {
        this.mTag = str;
        this.mEventLogger = new LocalLog(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logRadioEvent(String str, Object... objArr) {
        String format = String.format(str, objArr);
        this.mEventLogger.log(format);
        if (Log.isLoggable(this.mTag, 3)) {
            Slog.d(this.mTag, format);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        this.mEventLogger.dump(indentingPrintWriter);
    }
}
