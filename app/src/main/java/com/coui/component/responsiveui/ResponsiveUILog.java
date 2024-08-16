package com.coui.component.responsiveui;

import android.util.Log;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import kotlin.Metadata;
import sd.Indent;
import za.k;

/* compiled from: ResponsiveUILog.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001b\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b!\u0010\"J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0006\u0010\u0005\u001a\u00020\u0004J\u0018\u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\u0006R\u0017\u0010\u000e\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0011\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u000b\u001a\u0004\b\u0010\u0010\rR\u0017\u0010\u0014\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u000b\u001a\u0004\b\u0013\u0010\rR\u0017\u0010\u0017\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u0015\u0010\u000b\u001a\u0004\b\u0016\u0010\rR\u0017\u0010\u001a\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u0018\u0010\u000b\u001a\u0004\b\u0019\u0010\rR\u0017\u0010\u001d\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u001b\u0010\u000b\u001a\u0004\b\u001c\u0010\rR\u0017\u0010 \u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u001e\u0010\u000b\u001a\u0004\b\u001f\u0010\r¨\u0006#"}, d2 = {"Lcom/coui/component/responsiveui/ResponsiveUILog;", "", "", TriggerEvent.NOTIFICATION_TAG, "Lma/f0;", "logStatus", "", "level", "", "isLoggable", "a", "Z", "getLOG_VERBOSE", "()Z", "LOG_VERBOSE", "b", "getLOG_DEBUG", "LOG_DEBUG", "c", "getLOG_INFO", "LOG_INFO", "d", "getLOG_WARN", "LOG_WARN", "e", "getLOG_ERROR", "LOG_ERROR", "f", "getLOG_ASSERT", "LOG_ASSERT", "g", "getLOG_SILENT", "LOG_SILENT", "<init>", "()V", "coui-support-responsive_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class ResponsiveUILog {
    public static final ResponsiveUILog INSTANCE = new ResponsiveUILog();

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_VERBOSE;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_DEBUG;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_INFO;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_WARN;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_ERROR;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_ASSERT;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private static final boolean LOG_SILENT;

    static {
        boolean isLoggable = Log.isLoggable("COUI", 2);
        LOG_VERBOSE = isLoggable;
        boolean isLoggable2 = Log.isLoggable("COUI", 3);
        LOG_DEBUG = isLoggable2;
        boolean isLoggable3 = Log.isLoggable("COUI", 4);
        LOG_INFO = isLoggable3;
        boolean isLoggable4 = Log.isLoggable("COUI", 5);
        LOG_WARN = isLoggable4;
        boolean isLoggable5 = Log.isLoggable("COUI", 6);
        LOG_ERROR = isLoggable5;
        boolean isLoggable6 = Log.isLoggable("COUI", 7);
        LOG_ASSERT = isLoggable6;
        LOG_SILENT = (isLoggable || isLoggable2 || isLoggable3 || isLoggable4 || isLoggable5 || isLoggable6) ? false : true;
    }

    private ResponsiveUILog() {
    }

    public final boolean getLOG_ASSERT() {
        return LOG_ASSERT;
    }

    public final boolean getLOG_DEBUG() {
        return LOG_DEBUG;
    }

    public final boolean getLOG_ERROR() {
        return LOG_ERROR;
    }

    public final boolean getLOG_INFO() {
        return LOG_INFO;
    }

    public final boolean getLOG_SILENT() {
        return LOG_SILENT;
    }

    public final boolean getLOG_VERBOSE() {
        return LOG_VERBOSE;
    }

    public final boolean getLOG_WARN() {
        return LOG_WARN;
    }

    public final boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    public final void logStatus(String str) {
        String f10;
        k.e(str, TriggerEvent.NOTIFICATION_TAG);
        boolean isLoggable = k.a(str, "COUI") ? LOG_VERBOSE : Log.isLoggable(str, 2);
        boolean isLoggable2 = k.a(str, "COUI") ? LOG_DEBUG : Log.isLoggable(str, 3);
        boolean isLoggable3 = k.a(str, "COUI") ? LOG_INFO : Log.isLoggable(str, 2);
        boolean isLoggable4 = k.a(str, "COUI") ? LOG_WARN : Log.isLoggable(str, 2);
        boolean isLoggable5 = k.a(str, "COUI") ? LOG_ERROR : Log.isLoggable(str, 2);
        boolean isLoggable6 = k.a(str, "COUI") ? LOG_ASSERT : Log.isLoggable(str, 2);
        f10 = Indent.f("\n            Log status for tag: " + str + "\n            VERBOSE: " + isLoggable + "\n            DEBUG: " + isLoggable2 + "\n            INFO: " + isLoggable3 + "\n            WARN: " + isLoggable4 + "\n            ERROR: " + isLoggable5 + "\n            ASSERT: " + isLoggable6 + "\n            SILENT: " + (k.a(str, "COUI") ? LOG_SILENT : (isLoggable || isLoggable2 || isLoggable3 || isLoggable4 || isLoggable5 || isLoggable6) ? false : true) + "\n            ");
        Log.println(7, "COUI", f10);
    }

    public final void logStatus() {
        logStatus("COUI");
    }
}
