package fd;

import ma.Unit;

/* compiled from: locks.kt */
/* loaded from: classes2.dex */
public interface k {

    /* renamed from: a, reason: collision with root package name */
    public static final a f11449a = a.f11450a;

    /* compiled from: locks.kt */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ a f11450a = new a();

        private a() {
        }

        public final d a(Runnable runnable, ya.l<? super InterruptedException, Unit> lVar) {
            if (runnable != null && lVar != null) {
                return new c(runnable, lVar);
            }
            return new d(null, 1, null);
        }
    }

    void a();

    void b();
}
