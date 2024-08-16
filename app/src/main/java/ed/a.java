package ed;

import fd.StorageManager;
import java.util.Iterator;
import java.util.List;
import oc.FqName;
import qb.AnnotationDescriptor;
import qb.g;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: DeserializedAnnotations.kt */
/* loaded from: classes2.dex */
public class a implements qb.g {

    /* renamed from: f, reason: collision with root package name */
    static final /* synthetic */ gb.l<Object>[] f11033f = {Reflection.g(new PropertyReference1Impl(Reflection.b(a.class), "annotations", "getAnnotations()Ljava/util/List;"))};

    /* renamed from: e, reason: collision with root package name */
    private final fd.i f11034e;

    public a(StorageManager storageManager, ya.a<? extends List<? extends AnnotationDescriptor>> aVar) {
        za.k.e(storageManager, "storageManager");
        za.k.e(aVar, "compute");
        this.f11034e = storageManager.g(aVar);
    }

    private final List<AnnotationDescriptor> d() {
        return (List) fd.m.a(this.f11034e, this, f11033f[0]);
    }

    @Override // qb.g
    public boolean a(FqName fqName) {
        return g.b.b(this, fqName);
    }

    @Override // qb.g
    public boolean isEmpty() {
        return d().isEmpty();
    }

    @Override // java.lang.Iterable
    public Iterator<AnnotationDescriptor> iterator() {
        return d().iterator();
    }

    @Override // qb.g
    public AnnotationDescriptor j(FqName fqName) {
        return g.b.a(this, fqName);
    }
}
