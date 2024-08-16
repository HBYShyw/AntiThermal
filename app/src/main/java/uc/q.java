package uc;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.TypeProjection;
import gd.TypeProjectionImpl;
import gd.Variance;
import gd.c1;
import gd.g0;
import gd.h0;
import gd.i0;
import gd.o0;
import id.ErrorTypeKind;
import id.ErrorUtils;
import java.util.List;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import ma.NoWhenBranchMatchedException;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import oc.ClassId;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.ModuleDescriptor;
import pb.TypeParameterDescriptor;
import pb.findClassInModule;
import za.DefaultConstructorMarker;

/* compiled from: constantValues.kt */
/* loaded from: classes2.dex */
public final class q extends g<b> {

    /* renamed from: b, reason: collision with root package name */
    public static final a f19013b = new a(null);

    /* compiled from: constantValues.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final g<?> a(g0 g0Var) {
            Object q02;
            za.k.e(g0Var, "argumentType");
            if (i0.a(g0Var)) {
                return null;
            }
            int i10 = 0;
            g0 g0Var2 = g0Var;
            while (KotlinBuiltIns.c0(g0Var2)) {
                q02 = _Collections.q0(g0Var2.U0());
                g0Var2 = ((TypeProjection) q02).getType();
                za.k.d(g0Var2, "type.arguments.single().type");
                i10++;
            }
            ClassifierDescriptor v7 = g0Var2.W0().v();
            if (v7 instanceof ClassDescriptor) {
                ClassId k10 = wc.c.k(v7);
                return k10 == null ? new q(new b.a(g0Var)) : new q(k10, i10);
            }
            if (!(v7 instanceof TypeParameterDescriptor)) {
                return null;
            }
            ClassId m10 = ClassId.m(StandardNames.a.f15291b.l());
            za.k.d(m10, "topLevel(StandardNames.FqNames.any.toSafe())");
            return new q(m10, 0);
        }
    }

    /* compiled from: constantValues.kt */
    /* loaded from: classes2.dex */
    public static abstract class b {

        /* compiled from: constantValues.kt */
        /* loaded from: classes2.dex */
        public static final class a extends b {

            /* renamed from: a, reason: collision with root package name */
            private final g0 f19014a;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public a(g0 g0Var) {
                super(null);
                za.k.e(g0Var, "type");
                this.f19014a = g0Var;
            }

            public final g0 a() {
                return this.f19014a;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof a) && za.k.a(this.f19014a, ((a) obj).f19014a);
            }

            public int hashCode() {
                return this.f19014a.hashCode();
            }

            public String toString() {
                return "LocalClass(type=" + this.f19014a + ')';
            }
        }

        /* compiled from: constantValues.kt */
        /* renamed from: uc.q$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static final class C0110b extends b {

            /* renamed from: a, reason: collision with root package name */
            private final ClassLiteralValue f19015a;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public C0110b(ClassLiteralValue classLiteralValue) {
                super(null);
                za.k.e(classLiteralValue, ThermalBaseConfig.Item.ATTR_VALUE);
                this.f19015a = classLiteralValue;
            }

            public final int a() {
                return this.f19015a.c();
            }

            public final ClassId b() {
                return this.f19015a.d();
            }

            public final ClassLiteralValue c() {
                return this.f19015a;
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof C0110b) && za.k.a(this.f19015a, ((C0110b) obj).f19015a);
            }

            public int hashCode() {
                return this.f19015a.hashCode();
            }

            public String toString() {
                return "NormalClass(value=" + this.f19015a + ')';
            }
        }

        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q(b bVar) {
        super(bVar);
        za.k.e(bVar, ThermalBaseConfig.Item.ATTR_VALUE);
    }

    @Override // uc.g
    public g0 a(ModuleDescriptor moduleDescriptor) {
        List e10;
        za.k.e(moduleDescriptor, "module");
        c1 h10 = c1.f11749f.h();
        ClassDescriptor E = moduleDescriptor.t().E();
        za.k.d(E, "module.builtIns.kClass");
        e10 = CollectionsJVM.e(new TypeProjectionImpl(c(moduleDescriptor)));
        return h0.g(h10, E, e10);
    }

    public final g0 c(ModuleDescriptor moduleDescriptor) {
        za.k.e(moduleDescriptor, "module");
        b b10 = b();
        if (b10 instanceof b.a) {
            return ((b.a) b()).a();
        }
        if (b10 instanceof b.C0110b) {
            ClassLiteralValue c10 = ((b.C0110b) b()).c();
            ClassId a10 = c10.a();
            int b11 = c10.b();
            ClassDescriptor a11 = findClassInModule.a(moduleDescriptor, a10);
            if (a11 == null) {
                ErrorTypeKind errorTypeKind = ErrorTypeKind.f12801l;
                String classId = a10.toString();
                za.k.d(classId, "classId.toString()");
                return ErrorUtils.d(errorTypeKind, classId, String.valueOf(b11));
            }
            o0 x10 = a11.x();
            za.k.d(x10, "descriptor.defaultType");
            g0 w10 = ld.a.w(x10);
            for (int i10 = 0; i10 < b11; i10++) {
                w10 = moduleDescriptor.t().l(Variance.INVARIANT, w10);
                za.k.d(w10, "module.builtIns.getArray…Variance.INVARIANT, type)");
            }
            return w10;
        }
        throw new NoWhenBranchMatchedException();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public q(ClassLiteralValue classLiteralValue) {
        this(new b.C0110b(classLiteralValue));
        za.k.e(classLiteralValue, ThermalBaseConfig.Item.ATTR_VALUE);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public q(ClassId classId, int i10) {
        this(new ClassLiteralValue(classId, i10));
        za.k.e(classId, "classId");
    }
}
