package ob;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.collections.SetsJVM;
import kotlin.collections.r;
import kotlin.collections.s0;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import oc.ClassId;
import oc.FqName;
import pb.ClassDescriptor;

/* compiled from: JavaToKotlinClassMapper.kt */
/* renamed from: ob.d */
/* loaded from: classes2.dex */
public final class JavaToKotlinClassMapper {

    /* renamed from: a */
    public static final JavaToKotlinClassMapper f16359a = new JavaToKotlinClassMapper();

    private JavaToKotlinClassMapper() {
    }

    public static /* synthetic */ ClassDescriptor f(JavaToKotlinClassMapper javaToKotlinClassMapper, FqName fqName, KotlinBuiltIns kotlinBuiltIns, Integer num, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            num = null;
        }
        return javaToKotlinClassMapper.e(fqName, kotlinBuiltIns, num);
    }

    public final ClassDescriptor a(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "mutable");
        FqName o10 = JavaToKotlinClassMap.f16339a.o(sc.e.m(classDescriptor));
        if (o10 != null) {
            ClassDescriptor o11 = wc.c.j(classDescriptor).o(o10);
            za.k.d(o11, "descriptor.builtIns.getB…Name(oppositeClassFqName)");
            return o11;
        }
        throw new IllegalArgumentException("Given class " + classDescriptor + " is not a mutable collection");
    }

    public final ClassDescriptor b(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "readOnly");
        FqName p10 = JavaToKotlinClassMap.f16339a.p(sc.e.m(classDescriptor));
        if (p10 != null) {
            ClassDescriptor o10 = wc.c.j(classDescriptor).o(p10);
            za.k.d(o10, "descriptor.builtIns.getB…Name(oppositeClassFqName)");
            return o10;
        }
        throw new IllegalArgumentException("Given class " + classDescriptor + " is not a read-only collection");
    }

    public final boolean c(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "mutable");
        return JavaToKotlinClassMap.f16339a.k(sc.e.m(classDescriptor));
    }

    public final boolean d(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "readOnly");
        return JavaToKotlinClassMap.f16339a.l(sc.e.m(classDescriptor));
    }

    public final ClassDescriptor e(FqName fqName, KotlinBuiltIns kotlinBuiltIns, Integer num) {
        za.k.e(fqName, "fqName");
        za.k.e(kotlinBuiltIns, "builtIns");
        ClassId m10 = (num == null || !za.k.a(fqName, JavaToKotlinClassMap.f16339a.h())) ? JavaToKotlinClassMap.f16339a.m(fqName) : StandardNames.a(num.intValue());
        if (m10 != null) {
            return kotlinBuiltIns.o(m10.b());
        }
        return null;
    }

    public final Collection<ClassDescriptor> g(FqName fqName, KotlinBuiltIns kotlinBuiltIns) {
        List m10;
        Set d10;
        Set e10;
        za.k.e(fqName, "fqName");
        za.k.e(kotlinBuiltIns, "builtIns");
        ClassDescriptor f10 = f(this, fqName, kotlinBuiltIns, null, 4, null);
        if (f10 == null) {
            e10 = s0.e();
            return e10;
        }
        FqName p10 = JavaToKotlinClassMap.f16339a.p(wc.c.m(f10));
        if (p10 == null) {
            d10 = SetsJVM.d(f10);
            return d10;
        }
        ClassDescriptor o10 = kotlinBuiltIns.o(p10);
        za.k.d(o10, "builtIns.getBuiltInClass…otlinMutableAnalogFqName)");
        m10 = r.m(f10, o10);
        return m10;
    }
}
