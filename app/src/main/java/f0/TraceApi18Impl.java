package f0;

import android.os.Trace;

/* compiled from: TraceApi18Impl.java */
/* renamed from: f0.b, reason: use source file name */
/* loaded from: classes.dex */
final class TraceApi18Impl {
    public static void a(String str) {
        Trace.beginSection(str);
    }

    public static void b() {
        Trace.endSection();
    }
}
