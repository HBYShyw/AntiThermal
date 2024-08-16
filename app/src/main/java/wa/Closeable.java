package wa;

/* compiled from: Closeable.kt */
/* renamed from: wa.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class Closeable {
    public static final void a(java.io.Closeable closeable, Throwable th) {
        if (closeable != null) {
            if (th == null) {
                closeable.close();
                return;
            }
            try {
                closeable.close();
            } catch (Throwable th2) {
                ma.b.a(th, th2);
            }
        }
    }
}
