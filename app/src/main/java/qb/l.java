package qb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import oc.FqName;

/* compiled from: Annotations.kt */
/* loaded from: classes2.dex */
public final class l implements g {

    /* renamed from: e, reason: collision with root package name */
    private final g f17207e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f17208f;

    /* renamed from: g, reason: collision with root package name */
    private final ya.l<FqName, Boolean> f17209g;

    /* JADX WARN: Multi-variable type inference failed */
    public l(g gVar, boolean z10, ya.l<? super FqName, Boolean> lVar) {
        za.k.e(gVar, "delegate");
        za.k.e(lVar, "fqNameFilter");
        this.f17207e = gVar;
        this.f17208f = z10;
        this.f17209g = lVar;
    }

    private final boolean d(AnnotationDescriptor annotationDescriptor) {
        FqName d10 = annotationDescriptor.d();
        return d10 != null && this.f17209g.invoke(d10).booleanValue();
    }

    @Override // qb.g
    public boolean a(FqName fqName) {
        za.k.e(fqName, "fqName");
        if (this.f17209g.invoke(fqName).booleanValue()) {
            return this.f17207e.a(fqName);
        }
        return false;
    }

    @Override // qb.g
    public boolean isEmpty() {
        boolean z10;
        g gVar = this.f17207e;
        if (!(gVar instanceof Collection) || !((Collection) gVar).isEmpty()) {
            Iterator<AnnotationDescriptor> it = gVar.iterator();
            while (it.hasNext()) {
                if (d(it.next())) {
                    z10 = true;
                    break;
                }
            }
        }
        z10 = false;
        return this.f17208f ? !z10 : z10;
    }

    @Override // java.lang.Iterable
    public Iterator<AnnotationDescriptor> iterator() {
        g gVar = this.f17207e;
        ArrayList arrayList = new ArrayList();
        for (AnnotationDescriptor annotationDescriptor : gVar) {
            if (d(annotationDescriptor)) {
                arrayList.add(annotationDescriptor);
            }
        }
        return arrayList.iterator();
    }

    @Override // qb.g
    public AnnotationDescriptor j(FqName fqName) {
        za.k.e(fqName, "fqName");
        if (this.f17209g.invoke(fqName).booleanValue()) {
            return this.f17207e.j(fqName);
        }
        return null;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public l(g gVar, ya.l<? super FqName, Boolean> lVar) {
        this(gVar, false, lVar);
        za.k.e(gVar, "delegate");
        za.k.e(lVar, "fqNameFilter");
    }
}
