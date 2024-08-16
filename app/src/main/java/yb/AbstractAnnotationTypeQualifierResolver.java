package yb;

import gc.NullabilityQualifierWithMigrationStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.collections._Arrays;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import mb.StandardNames;
import oc.FqName;
import qb.KotlinTarget;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: AbstractAnnotationTypeQualifierResolver.kt */
/* renamed from: yb.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractAnnotationTypeQualifierResolver<TAnnotation> {

    /* renamed from: c, reason: collision with root package name */
    private static final a f20000c = new a(null);

    /* renamed from: d, reason: collision with root package name */
    @Deprecated
    private static final Map<String, AnnotationQualifierApplicabilityType> f20001d;

    /* renamed from: a, reason: collision with root package name */
    private final JavaTypeEnhancementState f20002a;

    /* renamed from: b, reason: collision with root package name */
    private final ConcurrentHashMap<Object, TAnnotation> f20003b;

    /* compiled from: AbstractAnnotationTypeQualifierResolver.kt */
    /* renamed from: yb.a$a */
    /* loaded from: classes2.dex */
    private static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AbstractAnnotationTypeQualifierResolver.kt */
    /* renamed from: yb.a$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<TAnnotation, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f20004e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(TAnnotation tannotation) {
            za.k.e(tannotation, "$this$extractNullability");
            return Boolean.FALSE;
        }
    }

    static {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType : AnnotationQualifierApplicabilityType.values()) {
            String b10 = annotationQualifierApplicabilityType.b();
            if (linkedHashMap.get(b10) == null) {
                linkedHashMap.put(b10, annotationQualifierApplicabilityType);
            }
        }
        f20001d = linkedHashMap;
    }

    public AbstractAnnotationTypeQualifierResolver(JavaTypeEnhancementState javaTypeEnhancementState) {
        za.k.e(javaTypeEnhancementState, "javaTypeEnhancementState");
        this.f20002a = javaTypeEnhancementState;
        this.f20003b = new ConcurrentHashMap<>();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Set<AnnotationQualifierApplicabilityType> a(Set<? extends AnnotationQualifierApplicabilityType> set) {
        Set r02;
        Set j10;
        Set<AnnotationQualifierApplicabilityType> k10;
        if (!set.contains(AnnotationQualifierApplicabilityType.TYPE_USE)) {
            return set;
        }
        r02 = _Arrays.r0(AnnotationQualifierApplicabilityType.values());
        j10 = _Sets.j(r02, AnnotationQualifierApplicabilityType.TYPE_PARAMETER_BOUNDS);
        k10 = _Sets.k(j10, set);
        return k10;
    }

    private final r d(TAnnotation tannotation) {
        NullabilityQualifierWithMigrationStatus g6;
        r r10 = r(tannotation);
        if (r10 != null) {
            return r10;
        }
        ma.o<TAnnotation, Set<AnnotationQualifierApplicabilityType>> t7 = t(tannotation);
        if (t7 == null) {
            return null;
        }
        TAnnotation a10 = t7.a();
        Set<AnnotationQualifierApplicabilityType> b10 = t7.b();
        ReportLevel q10 = q(tannotation);
        if (q10 == null) {
            q10 = p(a10);
        }
        if (q10.c() || (g6 = g(a10, b.f20004e)) == null) {
            return null;
        }
        return new r(NullabilityQualifierWithMigrationStatus.b(g6, null, q10.d(), 1, null), b10, false, 4, null);
    }

    private final NullabilityQualifierWithMigrationStatus g(TAnnotation tannotation, ya.l<? super TAnnotation, Boolean> lVar) {
        NullabilityQualifierWithMigrationStatus n10;
        NullabilityQualifierWithMigrationStatus n11 = n(tannotation, lVar.invoke(tannotation).booleanValue());
        if (n11 != null) {
            return n11;
        }
        TAnnotation s7 = s(tannotation);
        if (s7 == null) {
            return null;
        }
        ReportLevel p10 = p(tannotation);
        if (p10.c() || (n10 = n(s7, lVar.invoke(s7).booleanValue())) == null) {
            return null;
        }
        return NullabilityQualifierWithMigrationStatus.b(n10, null, p10.d(), 1, null);
    }

    private final TAnnotation h(TAnnotation tannotation, FqName fqName) {
        for (TAnnotation tannotation2 : k(tannotation)) {
            if (za.k.a(i(tannotation2), fqName)) {
                return tannotation2;
            }
        }
        return null;
    }

    private final boolean l(TAnnotation tannotation, FqName fqName) {
        Iterable<TAnnotation> k10 = k(tannotation);
        if ((k10 instanceof Collection) && ((Collection) k10).isEmpty()) {
            return false;
        }
        Iterator<TAnnotation> it = k10.iterator();
        while (it.hasNext()) {
            if (za.k.a(i(it.next()), fqName)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0078, code lost:
    
        if (r5.equals("ALWAYS") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008d, code lost:
    
        if (r5.equals("NEVER") == false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0099, code lost:
    
        r5 = gc.h.NULLABLE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0096, code lost:
    
        if (r5.equals("MAYBE") == false) goto L41;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:31:0x006e. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final NullabilityQualifierWithMigrationStatus n(TAnnotation tannotation, boolean z10) {
        gc.h hVar;
        Object U;
        FqName i10 = i(tannotation);
        if (i10 == null) {
            return null;
        }
        ReportLevel invoke = this.f20002a.c().invoke(i10);
        if (invoke.c()) {
            return null;
        }
        if (c0.l().contains(i10)) {
            hVar = gc.h.NULLABLE;
        } else if (c0.k().contains(i10)) {
            hVar = gc.h.NOT_NULL;
        } else if (za.k.a(i10, c0.g())) {
            hVar = gc.h.NULLABLE;
        } else if (za.k.a(i10, c0.h())) {
            hVar = gc.h.FORCE_FLEXIBILITY;
        } else if (za.k.a(i10, c0.f())) {
            U = _Collections.U(b(tannotation, false));
            String str = (String) U;
            if (str != null) {
                switch (str.hashCode()) {
                    case 73135176:
                        break;
                    case 74175084:
                        break;
                    case 433141802:
                        if (str.equals("UNKNOWN")) {
                            hVar = gc.h.FORCE_FLEXIBILITY;
                            break;
                        }
                        return null;
                    case 1933739535:
                        break;
                    default:
                        return null;
                }
            }
            hVar = gc.h.NOT_NULL;
        } else if (za.k.a(i10, c0.d())) {
            hVar = gc.h.NULLABLE;
        } else if (za.k.a(i10, c0.c())) {
            hVar = gc.h.NOT_NULL;
        } else if (za.k.a(i10, c0.a())) {
            hVar = gc.h.NOT_NULL;
        } else {
            if (!za.k.a(i10, c0.b())) {
                return null;
            }
            hVar = gc.h.NULLABLE;
        }
        return new NullabilityQualifierWithMigrationStatus(hVar, invoke.d() || z10);
    }

    private final ReportLevel o(TAnnotation tannotation) {
        FqName i10 = i(tannotation);
        if (i10 != null && c.c().containsKey(i10)) {
            return this.f20002a.c().invoke(i10);
        }
        return p(tannotation);
    }

    private final ReportLevel p(TAnnotation tannotation) {
        ReportLevel q10 = q(tannotation);
        return q10 != null ? q10 : this.f20002a.d().a();
    }

    private final ReportLevel q(TAnnotation tannotation) {
        Iterable<String> b10;
        Object U;
        ReportLevel reportLevel = this.f20002a.d().c().get(i(tannotation));
        if (reportLevel != null) {
            return reportLevel;
        }
        TAnnotation h10 = h(tannotation, c.d());
        if (h10 == null || (b10 = b(h10, false)) == null) {
            return null;
        }
        U = _Collections.U(b10);
        String str = (String) U;
        if (str == null) {
            return null;
        }
        ReportLevel b11 = this.f20002a.d().b();
        if (b11 != null) {
            return b11;
        }
        int hashCode = str.hashCode();
        if (hashCode == -2137067054) {
            if (str.equals("IGNORE")) {
                return ReportLevel.IGNORE;
            }
            return null;
        }
        if (hashCode == -1838656823) {
            if (str.equals("STRICT")) {
                return ReportLevel.STRICT;
            }
            return null;
        }
        if (hashCode == 2656902 && str.equals("WARN")) {
            return ReportLevel.WARN;
        }
        return null;
    }

    private final r r(TAnnotation tannotation) {
        r rVar;
        if (this.f20002a.b() || (rVar = c.a().get(i(tannotation))) == null) {
            return null;
        }
        ReportLevel o10 = o(tannotation);
        if (!(o10 != ReportLevel.IGNORE)) {
            o10 = null;
        }
        if (o10 == null) {
            return null;
        }
        return r.b(rVar, NullabilityQualifierWithMigrationStatus.b(rVar.d(), null, o10.d(), 1, null), null, false, 6, null);
    }

    private final ma.o<TAnnotation, Set<AnnotationQualifierApplicabilityType>> t(TAnnotation tannotation) {
        TAnnotation h10;
        TAnnotation tannotation2;
        if (this.f20002a.d().d() || (h10 = h(tannotation, c.e())) == null) {
            return null;
        }
        Iterator<TAnnotation> it = k(tannotation).iterator();
        while (true) {
            if (!it.hasNext()) {
                tannotation2 = null;
                break;
            }
            tannotation2 = it.next();
            if (s(tannotation2) != null) {
                break;
            }
        }
        if (tannotation2 == null) {
            return null;
        }
        Iterable<String> b10 = b(h10, true);
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Iterator<String> it2 = b10.iterator();
        while (it2.hasNext()) {
            AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType = f20001d.get(it2.next());
            if (annotationQualifierApplicabilityType != null) {
                linkedHashSet.add(annotationQualifierApplicabilityType);
            }
        }
        return new ma.o<>(tannotation2, a(linkedHashSet));
    }

    protected abstract Iterable<String> b(TAnnotation tannotation, boolean z10);

    public final JavaTypeQualifiersByElementType c(JavaTypeQualifiersByElementType javaTypeQualifiersByElementType, Iterable<? extends TAnnotation> iterable) {
        EnumMap<AnnotationQualifierApplicabilityType, r> b10;
        za.k.e(iterable, "annotations");
        if (this.f20002a.b()) {
            return javaTypeQualifiersByElementType;
        }
        ArrayList<r> arrayList = new ArrayList();
        Iterator<? extends TAnnotation> it = iterable.iterator();
        while (it.hasNext()) {
            r d10 = d(it.next());
            if (d10 != null) {
                arrayList.add(d10);
            }
        }
        if (arrayList.isEmpty()) {
            return javaTypeQualifiersByElementType;
        }
        EnumMap enumMap = (javaTypeQualifiersByElementType == null || (b10 = javaTypeQualifiersByElementType.b()) == null) ? new EnumMap(AnnotationQualifierApplicabilityType.class) : new EnumMap((EnumMap) b10);
        boolean z10 = false;
        for (r rVar : arrayList) {
            Iterator<AnnotationQualifierApplicabilityType> it2 = rVar.e().iterator();
            while (it2.hasNext()) {
                enumMap.put((EnumMap) it2.next(), (AnnotationQualifierApplicabilityType) rVar);
                z10 = true;
            }
        }
        return !z10 ? javaTypeQualifiersByElementType : new JavaTypeQualifiersByElementType(enumMap);
    }

    public final gc.f e(Iterable<? extends TAnnotation> iterable) {
        gc.f fVar;
        za.k.e(iterable, "annotations");
        Iterator<? extends TAnnotation> it = iterable.iterator();
        gc.f fVar2 = null;
        while (it.hasNext()) {
            FqName i10 = i(it.next());
            if (c0.m().contains(i10)) {
                fVar = gc.f.READ_ONLY;
            } else if (c0.j().contains(i10)) {
                fVar = gc.f.MUTABLE;
            } else {
                continue;
            }
            if (fVar2 != null && fVar2 != fVar) {
                return null;
            }
            fVar2 = fVar;
        }
        return fVar2;
    }

    public final NullabilityQualifierWithMigrationStatus f(Iterable<? extends TAnnotation> iterable, ya.l<? super TAnnotation, Boolean> lVar) {
        za.k.e(iterable, "annotations");
        za.k.e(lVar, "forceWarning");
        Iterator<? extends TAnnotation> it = iterable.iterator();
        NullabilityQualifierWithMigrationStatus nullabilityQualifierWithMigrationStatus = null;
        while (it.hasNext()) {
            NullabilityQualifierWithMigrationStatus g6 = g(it.next(), lVar);
            if (nullabilityQualifierWithMigrationStatus != null) {
                if (g6 != null && !za.k.a(g6, nullabilityQualifierWithMigrationStatus) && (!g6.d() || nullabilityQualifierWithMigrationStatus.d())) {
                    if (g6.d() || !nullabilityQualifierWithMigrationStatus.d()) {
                        return null;
                    }
                }
            }
            nullabilityQualifierWithMigrationStatus = g6;
        }
        return nullabilityQualifierWithMigrationStatus;
    }

    protected abstract FqName i(TAnnotation tannotation);

    protected abstract Object j(TAnnotation tannotation);

    protected abstract Iterable<TAnnotation> k(TAnnotation tannotation);

    public final boolean m(TAnnotation tannotation) {
        za.k.e(tannotation, "annotation");
        TAnnotation h10 = h(tannotation, StandardNames.a.H);
        if (h10 == null) {
            return false;
        }
        Iterable<String> b10 = b(h10, false);
        if ((b10 instanceof Collection) && ((Collection) b10).isEmpty()) {
            return false;
        }
        Iterator<String> it = b10.iterator();
        while (it.hasNext()) {
            if (za.k.a(it.next(), KotlinTarget.I.name())) {
                return true;
            }
        }
        return false;
    }

    public final TAnnotation s(TAnnotation tannotation) {
        boolean L;
        TAnnotation tannotation2;
        za.k.e(tannotation, "annotation");
        if (this.f20002a.d().d()) {
            return null;
        }
        L = _Collections.L(c.b(), i(tannotation));
        if (L || l(tannotation, c.f())) {
            return tannotation;
        }
        if (!l(tannotation, c.g())) {
            return null;
        }
        ConcurrentHashMap<Object, TAnnotation> concurrentHashMap = this.f20003b;
        Object j10 = j(tannotation);
        TAnnotation tannotation3 = concurrentHashMap.get(j10);
        if (tannotation3 != null) {
            return tannotation3;
        }
        Iterator<TAnnotation> it = k(tannotation).iterator();
        while (true) {
            if (!it.hasNext()) {
                tannotation2 = null;
                break;
            }
            tannotation2 = s(it.next());
            if (tannotation2 != null) {
                break;
            }
        }
        if (tannotation2 == null) {
            return null;
        }
        TAnnotation putIfAbsent = concurrentHashMap.putIfAbsent(j10, tannotation2);
        return putIfAbsent == null ? tannotation2 : putIfAbsent;
    }
}
