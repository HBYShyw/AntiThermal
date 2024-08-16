package qc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import qc.i;

/* compiled from: ExtensionRegistryLite.java */
/* loaded from: classes2.dex */
public class g {

    /* renamed from: b, reason: collision with root package name */
    private static final g f17285b = new g(true);

    /* renamed from: a, reason: collision with root package name */
    private final Map<a, i.f<?, ?>> f17286a;

    /* compiled from: ExtensionRegistryLite.java */
    /* loaded from: classes2.dex */
    private static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final Object f17287a;

        /* renamed from: b, reason: collision with root package name */
        private final int f17288b;

        a(Object obj, int i10) {
            this.f17287a = obj;
            this.f17288b = i10;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return this.f17287a == aVar.f17287a && this.f17288b == aVar.f17288b;
        }

        public int hashCode() {
            return (System.identityHashCode(this.f17287a) * 65535) + this.f17288b;
        }
    }

    g() {
        this.f17286a = new HashMap();
    }

    public static g c() {
        return f17285b;
    }

    public static g d() {
        return new g();
    }

    public final void a(i.f<?, ?> fVar) {
        this.f17286a.put(new a(fVar.b(), fVar.d()), fVar);
    }

    public <ContainingType extends q> i.f<ContainingType, ?> b(ContainingType containingtype, int i10) {
        return (i.f) this.f17286a.get(new a(containingtype, i10));
    }

    private g(boolean z10) {
        this.f17286a = Collections.emptyMap();
    }
}
