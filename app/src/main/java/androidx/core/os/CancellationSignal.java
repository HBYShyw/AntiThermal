package androidx.core.os;

/* compiled from: CancellationSignal.java */
/* renamed from: androidx.core.os.e, reason: use source file name */
/* loaded from: classes.dex */
public final class CancellationSignal {

    /* renamed from: a, reason: collision with root package name */
    private boolean f2211a;

    /* renamed from: b, reason: collision with root package name */
    private b f2212b;

    /* renamed from: c, reason: collision with root package name */
    private Object f2213c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f2214d;

    /* compiled from: CancellationSignal.java */
    /* renamed from: androidx.core.os.e$a */
    /* loaded from: classes.dex */
    static class a {
        static void a(Object obj) {
            ((android.os.CancellationSignal) obj).cancel();
        }

        static android.os.CancellationSignal b() {
            return new android.os.CancellationSignal();
        }
    }

    /* compiled from: CancellationSignal.java */
    /* renamed from: androidx.core.os.e$b */
    /* loaded from: classes.dex */
    public interface b {
        void a();
    }

    private void d() {
        while (this.f2214d) {
            try {
                wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    public void a() {
        synchronized (this) {
            if (this.f2211a) {
                return;
            }
            this.f2211a = true;
            this.f2214d = true;
            b bVar = this.f2212b;
            Object obj = this.f2213c;
            if (bVar != null) {
                try {
                    bVar.a();
                } catch (Throwable th) {
                    synchronized (this) {
                        this.f2214d = false;
                        notifyAll();
                        throw th;
                    }
                }
            }
            if (obj != null) {
                a.a(obj);
            }
            synchronized (this) {
                this.f2214d = false;
                notifyAll();
            }
        }
    }

    public boolean b() {
        boolean z10;
        synchronized (this) {
            z10 = this.f2211a;
        }
        return z10;
    }

    public void c(b bVar) {
        synchronized (this) {
            d();
            if (this.f2212b == bVar) {
                return;
            }
            this.f2212b = bVar;
            if (this.f2211a && bVar != null) {
                bVar.a();
            }
        }
    }
}
