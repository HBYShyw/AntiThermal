package qb;

import java.util.Iterator;
import java.util.List;
import oc.FqName;
import qb.g;

/* compiled from: AnnotationsImpl.kt */
/* renamed from: qb.h, reason: use source file name */
/* loaded from: classes2.dex */
public final class AnnotationsImpl implements g {

    /* renamed from: e, reason: collision with root package name */
    private final List<AnnotationDescriptor> f17198e;

    /* JADX WARN: Multi-variable type inference failed */
    public AnnotationsImpl(List<? extends AnnotationDescriptor> list) {
        za.k.e(list, "annotations");
        this.f17198e = list;
    }

    @Override // qb.g
    public boolean a(FqName fqName) {
        return g.b.b(this, fqName);
    }

    @Override // qb.g
    public boolean isEmpty() {
        return this.f17198e.isEmpty();
    }

    @Override // java.lang.Iterable
    public Iterator<AnnotationDescriptor> iterator() {
        return this.f17198e.iterator();
    }

    @Override // qb.g
    public AnnotationDescriptor j(FqName fqName) {
        return g.b.a(this, fqName);
    }

    public String toString() {
        return this.f17198e.toString();
    }
}
