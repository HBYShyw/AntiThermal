package qb;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import oc.FqName;
import rd.Sequence;
import rd._Sequences;
import za.Lambda;

/* compiled from: Annotations.kt */
/* loaded from: classes2.dex */
public final class k implements g {

    /* renamed from: e, reason: collision with root package name */
    private final List<g> f17204e;

    /* compiled from: Annotations.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<g, AnnotationDescriptor> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FqName f17205e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(FqName fqName) {
            super(1);
            this.f17205e = fqName;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final AnnotationDescriptor invoke(g gVar) {
            za.k.e(gVar, "it");
            return gVar.j(this.f17205e);
        }
    }

    /* compiled from: Annotations.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<g, Sequence<? extends AnnotationDescriptor>> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f17206e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Sequence<AnnotationDescriptor> invoke(g gVar) {
            Sequence<AnnotationDescriptor> K;
            za.k.e(gVar, "it");
            K = _Collections.K(gVar);
            return K;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public k(List<? extends g> list) {
        za.k.e(list, "delegates");
        this.f17204e = list;
    }

    @Override // qb.g
    public boolean a(FqName fqName) {
        Sequence K;
        za.k.e(fqName, "fqName");
        K = _Collections.K(this.f17204e);
        Iterator it = K.iterator();
        while (it.hasNext()) {
            if (((g) it.next()).a(fqName)) {
                return true;
            }
        }
        return false;
    }

    @Override // qb.g
    public boolean isEmpty() {
        List<g> list = this.f17204e;
        if ((list instanceof Collection) && list.isEmpty()) {
            return true;
        }
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            if (!((g) it.next()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override // java.lang.Iterable
    public Iterator<AnnotationDescriptor> iterator() {
        Sequence K;
        Sequence q10;
        K = _Collections.K(this.f17204e);
        q10 = _Sequences.q(K, b.f17206e);
        return q10.iterator();
    }

    @Override // qb.g
    public AnnotationDescriptor j(FqName fqName) {
        Sequence K;
        Sequence x10;
        Object p10;
        za.k.e(fqName, "fqName");
        K = _Collections.K(this.f17204e);
        x10 = _Sequences.x(K, new a(fqName));
        p10 = _Sequences.p(x10);
        return (AnnotationDescriptor) p10;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public k(g... gVarArr) {
        this((List<? extends g>) r2);
        List f02;
        za.k.e(gVarArr, "delegates");
        f02 = _Arrays.f0(gVarArr);
    }
}
