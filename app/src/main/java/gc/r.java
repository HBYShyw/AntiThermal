package gc;

import gd.g0;
import java.util.List;
import kotlin.collections._Collections;
import ob.JavaToKotlinClassMapper;
import oc.FqName;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import yb.b0;

/* compiled from: typeEnhancement.kt */
/* loaded from: classes2.dex */
public final class r {

    /* renamed from: a, reason: collision with root package name */
    private static final qb.g f11736a;

    /* renamed from: b, reason: collision with root package name */
    private static final c f11737b;

    /* compiled from: typeEnhancement.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f11738a;

        static {
            int[] iArr = new int[h.values().length];
            try {
                iArr[h.NULLABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[h.NOT_NULL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f11738a = iArr;
        }
    }

    static {
        FqName fqName = b0.f20038u;
        za.k.d(fqName, "ENHANCED_NULLABILITY_ANNOTATION");
        f11736a = new c(fqName);
        FqName fqName2 = b0.f20039v;
        za.k.d(fqName2, "ENHANCED_MUTABILITY_ANNOTATION");
        f11737b = new c(fqName2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final qb.g e(List<? extends qb.g> list) {
        Object q02;
        List z02;
        int size = list.size();
        if (size == 0) {
            throw new IllegalStateException("At least one Annotations object expected".toString());
        }
        if (size != 1) {
            z02 = _Collections.z0(list);
            return new qb.k((List<? extends qb.g>) z02);
        }
        q02 = _Collections.q0(list);
        return (qb.g) q02;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassifierDescriptor f(ClassifierDescriptor classifierDescriptor, e eVar, o oVar) {
        JavaToKotlinClassMapper javaToKotlinClassMapper = JavaToKotlinClassMapper.f16359a;
        if (!p.a(oVar) || !(classifierDescriptor instanceof ClassDescriptor)) {
            return null;
        }
        if (eVar.c() == f.READ_ONLY && oVar == o.FLEXIBLE_LOWER) {
            ClassDescriptor classDescriptor = (ClassDescriptor) classifierDescriptor;
            if (javaToKotlinClassMapper.c(classDescriptor)) {
                return javaToKotlinClassMapper.a(classDescriptor);
            }
        }
        if (eVar.c() != f.MUTABLE || oVar != o.FLEXIBLE_UPPER) {
            return null;
        }
        ClassDescriptor classDescriptor2 = (ClassDescriptor) classifierDescriptor;
        if (javaToKotlinClassMapper.d(classDescriptor2)) {
            return javaToKotlinClassMapper.b(classDescriptor2);
        }
        return null;
    }

    public static final qb.g g() {
        return f11736a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Boolean h(e eVar, o oVar) {
        if (!p.a(oVar)) {
            return null;
        }
        h d10 = eVar.d();
        int i10 = d10 == null ? -1 : a.f11738a[d10.ordinal()];
        if (i10 == 1) {
            return Boolean.TRUE;
        }
        if (i10 != 2) {
            return null;
        }
        return Boolean.FALSE;
    }

    public static final boolean i(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        return typeEnhancementUtils.c(hd.q.f12241a, g0Var);
    }
}
