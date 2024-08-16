package gc;

import java.util.Iterator;
import java.util.List;
import oc.FqName;
import qb.AnnotationDescriptor;
import qb.g;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: typeEnhancement.kt */
/* loaded from: classes2.dex */
public final class c implements qb.g {

    /* renamed from: e, reason: collision with root package name */
    private final FqName f11650e;

    public c(FqName fqName) {
        za.k.e(fqName, "fqNameToMatch");
        this.f11650e = fqName;
    }

    @Override // qb.g
    public boolean a(FqName fqName) {
        return g.b.b(this, fqName);
    }

    @Override // qb.g
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public b j(FqName fqName) {
        za.k.e(fqName, "fqName");
        if (za.k.a(fqName, this.f11650e)) {
            return b.f11649a;
        }
        return null;
    }

    @Override // qb.g
    public boolean isEmpty() {
        return false;
    }

    @Override // java.lang.Iterable
    public Iterator<AnnotationDescriptor> iterator() {
        List j10;
        j10 = kotlin.collections.r.j();
        return j10.iterator();
    }
}
