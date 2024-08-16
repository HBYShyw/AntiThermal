package androidx.core.os;

import android.os.Trace;

/* compiled from: TraceCompat.java */
@Deprecated
/* renamed from: androidx.core.os.i, reason: use source file name */
/* loaded from: classes.dex */
public final class TraceCompat {

    /* compiled from: TraceCompat.java */
    /* renamed from: androidx.core.os.i$a */
    /* loaded from: classes.dex */
    static class a {
        static void a(String str) {
            Trace.beginSection(str);
        }

        static void b() {
            Trace.endSection();
        }
    }

    public static void a(String str) {
        a.a(str);
    }

    public static void b() {
        a.b();
    }
}
