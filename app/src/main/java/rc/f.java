package rc;

import java.util.Set;
import oc.FqName;

/* compiled from: DescriptorRenderer.kt */
/* loaded from: classes2.dex */
public interface f {

    /* compiled from: DescriptorRenderer.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        public static boolean a(f fVar) {
            return fVar.n().b();
        }

        public static boolean b(f fVar) {
            return fVar.n().c();
        }
    }

    void a(Set<FqName> set);

    void b(boolean z10);

    void c(Set<? extends e> set);

    void d(m mVar);

    void e(boolean z10);

    boolean f();

    void g(boolean z10);

    void h(boolean z10);

    void i(boolean z10);

    void j(boolean z10);

    void k(k kVar);

    Set<FqName> l();

    boolean m();

    rc.a n();

    void o(ClassifierNamePolicy classifierNamePolicy);

    void p(boolean z10);
}
