package bc;

import java.util.Iterator;
import kotlin.collections._Collections;
import mb.StandardNames;
import oc.FqName;
import qb.AnnotationDescriptor;
import qb.g;
import rd.Sequence;
import rd._Sequences;
import ya.l;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: LazyJavaAnnotations.kt */
/* loaded from: classes2.dex */
public final class d implements qb.g {

    /* renamed from: e, reason: collision with root package name */
    private final g f4685e;

    /* renamed from: f, reason: collision with root package name */
    private final fc.d f4686f;

    /* renamed from: g, reason: collision with root package name */
    private final boolean f4687g;

    /* renamed from: h, reason: collision with root package name */
    private final fd.h<fc.a, AnnotationDescriptor> f4688h;

    /* compiled from: LazyJavaAnnotations.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements l<fc.a, AnnotationDescriptor> {
        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final AnnotationDescriptor invoke(fc.a aVar) {
            za.k.e(aVar, "annotation");
            return zb.c.f20391a.e(aVar, d.this.f4685e, d.this.f4687g);
        }
    }

    public d(g gVar, fc.d dVar, boolean z10) {
        za.k.e(gVar, "c");
        za.k.e(dVar, "annotationOwner");
        this.f4685e = gVar;
        this.f4686f = dVar;
        this.f4687g = z10;
        this.f4688h = gVar.a().u().b(new a());
    }

    @Override // qb.g
    public boolean a(FqName fqName) {
        return g.b.b(this, fqName);
    }

    @Override // qb.g
    public boolean isEmpty() {
        return this.f4686f.i().isEmpty() && !this.f4686f.k();
    }

    @Override // java.lang.Iterable
    public Iterator<AnnotationDescriptor> iterator() {
        Sequence K;
        Sequence w10;
        Sequence z10;
        Sequence o10;
        K = _Collections.K(this.f4686f.i());
        w10 = _Sequences.w(K, this.f4688h);
        z10 = _Sequences.z(w10, zb.c.f20391a.a(StandardNames.a.f15337y, this.f4686f, this.f4685e));
        o10 = _Sequences.o(z10);
        return o10.iterator();
    }

    @Override // qb.g
    public AnnotationDescriptor j(FqName fqName) {
        AnnotationDescriptor invoke;
        za.k.e(fqName, "fqName");
        fc.a j10 = this.f4686f.j(fqName);
        return (j10 == null || (invoke = this.f4688h.invoke(j10)) == null) ? zb.c.f20391a.a(fqName, this.f4686f, this.f4685e) : invoke;
    }

    public /* synthetic */ d(g gVar, fc.d dVar, boolean z10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(gVar, dVar, (i10 & 4) != 0 ? false : z10);
    }
}
