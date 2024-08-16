package gc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kd.u;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import ob.JavaToKotlinClassMap;
import oc.FqNameUnsafe;
import yb.AbstractAnnotationTypeQualifierResolver;
import yb.AnnotationQualifierApplicabilityType;
import yb.JavaTypeQualifiersByElementType;
import za.Lambda;

/* compiled from: AbstractSignatureParts.kt */
/* renamed from: gc.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractSignatureParts<TAnnotation> {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractSignatureParts.kt */
    /* renamed from: gc.a$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final kd.i f11641a;

        /* renamed from: b, reason: collision with root package name */
        private final JavaTypeQualifiersByElementType f11642b;

        /* renamed from: c, reason: collision with root package name */
        private final kd.o f11643c;

        public a(kd.i iVar, JavaTypeQualifiersByElementType javaTypeQualifiersByElementType, kd.o oVar) {
            this.f11641a = iVar;
            this.f11642b = javaTypeQualifiersByElementType;
            this.f11643c = oVar;
        }

        public final JavaTypeQualifiersByElementType a() {
            return this.f11642b;
        }

        public final kd.i b() {
            return this.f11641a;
        }

        public final kd.o c() {
            return this.f11643c;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AbstractSignatureParts.kt */
    /* renamed from: gc.a$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<Integer, e> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ q f11644e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ e[] f11645f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(q qVar, e[] eVarArr) {
            super(1);
            this.f11644e = qVar;
            this.f11645f = eVarArr;
        }

        public final e a(int i10) {
            int D;
            Map<Integer, e> a10;
            e eVar;
            q qVar = this.f11644e;
            if (qVar != null && (a10 = qVar.a()) != null && (eVar = a10.get(Integer.valueOf(i10))) != null) {
                return eVar;
            }
            e[] eVarArr = this.f11645f;
            if (i10 >= 0) {
                D = _Arrays.D(eVarArr);
                if (i10 <= D) {
                    return eVarArr[i10];
                }
            }
            return e.f11657e.a();
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ e invoke(Integer num) {
            return a(num.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AbstractSignatureParts.kt */
    /* renamed from: gc.a$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<TAnnotation, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AbstractSignatureParts<TAnnotation> f11646e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(AbstractSignatureParts<TAnnotation> abstractSignatureParts) {
            super(1);
            this.f11646e = abstractSignatureParts;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(TAnnotation tannotation) {
            za.k.e(tannotation, "$this$extractNullability");
            return Boolean.valueOf(this.f11646e.r(tannotation));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AbstractSignatureParts.kt */
    /* renamed from: gc.a$d */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.l<a, Iterable<? extends a>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ AbstractSignatureParts<TAnnotation> f11647e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ kd.p f11648f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(AbstractSignatureParts<TAnnotation> abstractSignatureParts, kd.p pVar) {
            super(1);
            this.f11647e = abstractSignatureParts;
            this.f11648f = pVar;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Iterable<a> invoke(a aVar) {
            kd.n F;
            List<kd.o> B0;
            int u7;
            int u10;
            a aVar2;
            kd.g z10;
            za.k.e(aVar, "it");
            if (this.f11647e.u()) {
                kd.i b10 = aVar.b();
                if (((b10 == null || (z10 = this.f11648f.z(b10)) == null) ? null : this.f11648f.E0(z10)) != null) {
                    return null;
                }
            }
            kd.i b11 = aVar.b();
            if (b11 == null || (F = this.f11648f.F(b11)) == null || (B0 = this.f11648f.B0(F)) == null) {
                return null;
            }
            List<kd.m> h10 = this.f11648f.h(aVar.b());
            kd.p pVar = this.f11648f;
            AbstractSignatureParts<TAnnotation> abstractSignatureParts = this.f11647e;
            Iterator<T> it = B0.iterator();
            Iterator<T> it2 = h10.iterator();
            u7 = kotlin.collections.s.u(B0, 10);
            u10 = kotlin.collections.s.u(h10, 10);
            ArrayList arrayList = new ArrayList(Math.min(u7, u10));
            while (it.hasNext() && it2.hasNext()) {
                Object next = it.next();
                kd.m mVar = (kd.m) it2.next();
                kd.o oVar = (kd.o) next;
                if (pVar.d0(mVar)) {
                    aVar2 = new a(null, aVar.a(), oVar);
                } else {
                    kd.i X = pVar.X(mVar);
                    aVar2 = new a(X, abstractSignatureParts.c(X, aVar.a()), oVar);
                }
                arrayList.add(aVar2);
            }
            return arrayList;
        }
    }

    private final NullabilityQualifierWithMigrationStatus B(NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus, NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus2) {
        return nullabilityQualifierWithMigrationStatus == null ? nullabilityQualifierWithMigrationStatus2 : nullabilityQualifierWithMigrationStatus2 == null ? nullabilityQualifierWithMigrationStatus : (!nullabilityQualifierWithMigrationStatus.d() || nullabilityQualifierWithMigrationStatus2.d()) ? (nullabilityQualifierWithMigrationStatus.d() || !nullabilityQualifierWithMigrationStatus2.d()) ? (nullabilityQualifierWithMigrationStatus.c().compareTo(nullabilityQualifierWithMigrationStatus2.c()) >= 0 && nullabilityQualifierWithMigrationStatus.c().compareTo(nullabilityQualifierWithMigrationStatus2.c()) > 0) ? nullabilityQualifierWithMigrationStatus : nullabilityQualifierWithMigrationStatus2 : nullabilityQualifierWithMigrationStatus : nullabilityQualifierWithMigrationStatus2;
    }

    private final List<a> C(kd.i iVar) {
        return f(new a(iVar, c(iVar, m()), null), new d(this, v()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final JavaTypeQualifiersByElementType c(kd.i iVar, JavaTypeQualifiersByElementType javaTypeQualifiersByElementType) {
        return h().c(javaTypeQualifiersByElementType, i(iVar));
    }

    private final e d(kd.i iVar) {
        h hVar;
        h t7 = t(iVar);
        f fVar = null;
        if (t7 == null) {
            kd.i p10 = p(iVar);
            hVar = p10 != null ? t(p10) : null;
        } else {
            hVar = t7;
        }
        kd.p v7 = v();
        JavaToKotlinClassMap javaToKotlinClassMap = JavaToKotlinClassMap.f16339a;
        if (javaToKotlinClassMap.l(s(v7.v0(iVar)))) {
            fVar = f.READ_ONLY;
        } else if (javaToKotlinClassMap.k(s(v7.s0(iVar)))) {
            fVar = f.MUTABLE;
        }
        return new e(hVar, fVar, v().i0(iVar) || A(iVar), hVar != t7);
    }

    /* JADX WARN: Code restructure failed: missing block: B:80:0x012a, code lost:
    
        if ((r0 != null && r0.c()) != false) goto L93;
     */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0154  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final e e(a aVar) {
        Iterable<? extends TAnnotation> j10;
        AnnotationQualifierApplicabilityType l10;
        NullabilityQualifierWithMigrationStatus d10;
        boolean z10;
        kd.o c10;
        NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus;
        kd.n F;
        if (aVar.b() == null) {
            kd.p v7 = v();
            kd.o c11 = aVar.c();
            if ((c11 != null ? v7.x(c11) : null) == u.IN) {
                return e.f11657e.a();
            }
        }
        boolean z11 = aVar.c() == null;
        kd.i b10 = aVar.b();
        if (b10 == null || (j10 = i(b10)) == null) {
            j10 = kotlin.collections.r.j();
        }
        kd.p v10 = v();
        kd.i b11 = aVar.b();
        kd.o T = (b11 == null || (F = v10.F(b11)) == null) ? null : v10.T(F);
        boolean z12 = l() == AnnotationQualifierApplicabilityType.TYPE_PARAMETER_BOUNDS;
        if (z11) {
            if (!z12 && o()) {
                kd.i b12 = aVar.b();
                if (b12 != null && w(b12)) {
                    Iterable<TAnnotation> k10 = k();
                    ArrayList arrayList = new ArrayList();
                    for (TAnnotation tannotation : k10) {
                        if (!h().m(tannotation)) {
                            arrayList.add(tannotation);
                        }
                    }
                    j10 = _Collections.m0(arrayList, j10);
                }
            }
            j10 = _Collections.k0(k(), j10);
        }
        f e10 = h().e(j10);
        NullabilityQualifierWithMigrationStatus f10 = h().f(j10, new c(this));
        if (f10 != null) {
            return new e(f10.c(), e10, f10.c() == h.NOT_NULL && T != null, f10.d());
        }
        if (!z11 && !z12) {
            l10 = AnnotationQualifierApplicabilityType.TYPE_USE;
        } else {
            l10 = l();
        }
        JavaTypeQualifiersByElementType a10 = aVar.a();
        yb.r a11 = a10 != null ? a10.a(l10) : null;
        NullabilityQualifierWithMigrationStatus j11 = T != null ? j(T) : null;
        if (j11 == null || (d10 = NullabilityQualifierWithMigrationStatus.b(j11, h.NOT_NULL, false, 2, null)) == null) {
            d10 = a11 != null ? a11.d() : null;
        }
        if ((j11 != null ? j11.c() : null) != h.NOT_NULL) {
            if (T != null) {
            }
            z10 = false;
            c10 = aVar.c();
            if (c10 != null || (nullabilityQualifierWithMigrationStatus = j(c10)) == null) {
                nullabilityQualifierWithMigrationStatus = null;
            } else if (nullabilityQualifierWithMigrationStatus.c() == h.NULLABLE) {
                nullabilityQualifierWithMigrationStatus = NullabilityQualifierWithMigrationStatus.b(nullabilityQualifierWithMigrationStatus, h.FORCE_FLEXIBILITY, false, 2, null);
            }
            NullabilityQualifierWithMigrationStatus B = B(nullabilityQualifierWithMigrationStatus, d10);
            return new e(B != null ? B.c() : null, e10, z10, B == null && B.d());
        }
        z10 = true;
        c10 = aVar.c();
        if (c10 != null) {
        }
        nullabilityQualifierWithMigrationStatus = null;
        NullabilityQualifierWithMigrationStatus B2 = B(nullabilityQualifierWithMigrationStatus, d10);
        return new e(B2 != null ? B2.c() : null, e10, z10, B2 == null && B2.d());
    }

    private final <T> List<T> f(T t7, ya.l<? super T, ? extends Iterable<? extends T>> lVar) {
        ArrayList arrayList = new ArrayList(1);
        g(t7, arrayList, lVar);
        return arrayList;
    }

    private final <T> void g(T t7, List<T> list, ya.l<? super T, ? extends Iterable<? extends T>> lVar) {
        list.add(t7);
        Iterable<? extends T> invoke = lVar.invoke(t7);
        if (invoke != null) {
            Iterator<? extends T> it = invoke.iterator();
            while (it.hasNext()) {
                g(it.next(), list, lVar);
            }
        }
    }

    private final NullabilityQualifierWithMigrationStatus j(kd.o oVar) {
        boolean z10;
        boolean z11;
        boolean z12;
        List<kd.i> arrayList;
        boolean z13;
        kd.p v7 = v();
        if (!z(oVar)) {
            return null;
        }
        List<kd.i> l10 = v7.l(oVar);
        boolean z14 = l10 instanceof Collection;
        if (!z14 || !l10.isEmpty()) {
            Iterator<T> it = l10.iterator();
            while (it.hasNext()) {
                if (!v7.v((kd.i) it.next())) {
                    z10 = false;
                    break;
                }
            }
        }
        z10 = true;
        if (z10) {
            return null;
        }
        if (!z14 || !l10.isEmpty()) {
            Iterator<T> it2 = l10.iterator();
            while (it2.hasNext()) {
                if (t((kd.i) it2.next()) != null) {
                    z11 = true;
                    break;
                }
            }
        }
        z11 = false;
        if (z11) {
            arrayList = l10;
        } else {
            if (!z14 || !l10.isEmpty()) {
                Iterator<T> it3 = l10.iterator();
                while (it3.hasNext()) {
                    if (p((kd.i) it3.next()) != null) {
                        z12 = true;
                        break;
                    }
                }
            }
            z12 = false;
            if (!z12) {
                return null;
            }
            arrayList = new ArrayList<>();
            Iterator<T> it4 = l10.iterator();
            while (it4.hasNext()) {
                kd.i p10 = p((kd.i) it4.next());
                if (p10 != null) {
                    arrayList.add(p10);
                }
            }
        }
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator<T> it5 = arrayList.iterator();
            while (it5.hasNext()) {
                if (!v7.h0((kd.i) it5.next())) {
                    z13 = false;
                    break;
                }
            }
        }
        z13 = true;
        return new NullabilityQualifierWithMigrationStatus(z13 ? h.NULLABLE : h.NOT_NULL, arrayList != l10);
    }

    private final h t(kd.i iVar) {
        kd.p v7 = v();
        if (v7.B(v7.v0(iVar))) {
            return h.NULLABLE;
        }
        if (v7.B(v7.s0(iVar))) {
            return null;
        }
        return h.NOT_NULL;
    }

    public abstract boolean A(kd.i iVar);

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0066, code lost:
    
        if (r10 != false) goto L24;
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0070  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final ya.l<Integer, e> b(kd.i iVar, Iterable<? extends kd.i> iterable, q qVar, boolean z10) {
        int u7;
        boolean z11;
        int size;
        int i10;
        Object W;
        kd.i b10;
        boolean z12;
        za.k.e(iVar, "<this>");
        za.k.e(iterable, "overrides");
        List<a> C = C(iVar);
        u7 = kotlin.collections.s.u(iterable, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<? extends kd.i> it = iterable.iterator();
        while (it.hasNext()) {
            arrayList.add(C(it.next()));
        }
        if (!q()) {
            if (x()) {
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator<? extends kd.i> it2 = iterable.iterator();
                    while (it2.hasNext()) {
                        if (!y(iVar, it2.next())) {
                            z12 = true;
                            break;
                        }
                    }
                }
                z12 = false;
            }
            z11 = false;
            size = !z11 ? 1 : C.size();
            e[] eVarArr = new e[size];
            i10 = 0;
            while (i10 < size) {
                e e10 = e(C.get(i10));
                ArrayList arrayList2 = new ArrayList();
                Iterator it3 = arrayList.iterator();
                while (it3.hasNext()) {
                    W = _Collections.W((List) it3.next(), i10);
                    a aVar = (a) W;
                    e d10 = (aVar == null || (b10 = aVar.b()) == null) ? null : d(b10);
                    if (d10 != null) {
                        arrayList2.add(d10);
                    }
                }
                eVarArr[i10] = typeEnhancementUtils.a(e10, arrayList2, i10 == 0 && x(), i10 == 0 && n(), z10);
                i10++;
            }
            return new b(qVar, eVarArr);
        }
        z11 = true;
        if (!z11) {
        }
        e[] eVarArr2 = new e[size];
        i10 = 0;
        while (i10 < size) {
        }
        return new b(qVar, eVarArr2);
    }

    public abstract AbstractAnnotationTypeQualifierResolver<TAnnotation> h();

    public abstract Iterable<TAnnotation> i(kd.i iVar);

    public abstract Iterable<TAnnotation> k();

    public abstract AnnotationQualifierApplicabilityType l();

    public abstract JavaTypeQualifiersByElementType m();

    public abstract boolean n();

    public abstract boolean o();

    public abstract kd.i p(kd.i iVar);

    public boolean q() {
        return false;
    }

    public abstract boolean r(TAnnotation tannotation);

    public abstract FqNameUnsafe s(kd.i iVar);

    public abstract boolean u();

    public abstract kd.p v();

    public abstract boolean w(kd.i iVar);

    public abstract boolean x();

    public abstract boolean y(kd.i iVar, kd.i iVar2);

    public abstract boolean z(kd.o oVar);
}
