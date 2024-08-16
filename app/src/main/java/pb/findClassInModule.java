package pb;

import gb.KDeclarationContainer;
import java.util.List;
import kotlin.collections._Collections;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import rd.Sequence;
import rd._Sequences;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;

/* compiled from: findClassInModule.kt */
/* renamed from: pb.x, reason: use source file name */
/* loaded from: classes2.dex */
public final class findClassInModule {

    /* compiled from: findClassInModule.kt */
    /* renamed from: pb.x$a */
    /* loaded from: classes2.dex */
    /* synthetic */ class a extends FunctionReference implements ya.l<ClassId, ClassId> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f16747n = new a();

        a() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(ClassId.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "getOuterClassId()Lorg/jetbrains/kotlin/name/ClassId;";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final ClassId invoke(ClassId classId) {
            za.k.e(classId, "p0");
            return classId.g();
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "getOuterClassId";
        }
    }

    /* compiled from: findClassInModule.kt */
    /* renamed from: pb.x$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<ClassId, Integer> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f16748e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Integer invoke(ClassId classId) {
            za.k.e(classId, "it");
            return 0;
        }
    }

    public static final ClassDescriptor a(ModuleDescriptor moduleDescriptor, ClassId classId) {
        za.k.e(moduleDescriptor, "<this>");
        za.k.e(classId, "classId");
        ClassifierDescriptor b10 = b(moduleDescriptor, classId);
        if (b10 instanceof ClassDescriptor) {
            return (ClassDescriptor) b10;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00e8  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final ClassifierDescriptor b(ModuleDescriptor moduleDescriptor, ClassId classId) {
        Object T;
        Object T2;
        ClassifierDescriptor e10;
        Object T3;
        za.k.e(moduleDescriptor, "<this>");
        za.k.e(classId, "classId");
        ModuleDescriptor a10 = sc.o.a(moduleDescriptor);
        if (a10 == null) {
            FqName h10 = classId.h();
            za.k.d(h10, "classId.packageFqName");
            PackageViewDescriptor H = moduleDescriptor.H(h10);
            List<Name> f10 = classId.i().f();
            za.k.d(f10, "classId.relativeClassName.pathSegments()");
            zc.h u7 = H.u();
            T3 = _Collections.T(f10);
            za.k.d(T3, "segments.first()");
            e10 = u7.e((Name) T3, xb.d.FROM_DESERIALIZATION);
            if (e10 == null) {
                return null;
            }
            for (Name name : f10.subList(1, f10.size())) {
                if (!(e10 instanceof ClassDescriptor)) {
                    return null;
                }
                zc.h F0 = ((ClassDescriptor) e10).F0();
                za.k.d(name, "name");
                ClassifierDescriptor e11 = F0.e(name, xb.d.FROM_DESERIALIZATION);
                e10 = e11 instanceof ClassDescriptor ? (ClassDescriptor) e11 : null;
                if (e10 == null) {
                    return null;
                }
            }
        } else {
            FqName h11 = classId.h();
            za.k.d(h11, "classId.packageFqName");
            PackageViewDescriptor H2 = a10.H(h11);
            List<Name> f11 = classId.i().f();
            za.k.d(f11, "classId.relativeClassName.pathSegments()");
            zc.h u10 = H2.u();
            T = _Collections.T(f11);
            za.k.d(T, "segments.first()");
            ClassifierDescriptor e12 = u10.e((Name) T, xb.d.FROM_DESERIALIZATION);
            if (e12 != null) {
                for (Name name2 : f11.subList(1, f11.size())) {
                    if (e12 instanceof ClassDescriptor) {
                        zc.h F02 = ((ClassDescriptor) e12).F0();
                        za.k.d(name2, "name");
                        ClassifierDescriptor e13 = F02.e(name2, xb.d.FROM_DESERIALIZATION);
                        e12 = e13 instanceof ClassDescriptor ? (ClassDescriptor) e13 : null;
                        if (e12 != null) {
                        }
                    }
                }
                if (e12 == null) {
                    return e12;
                }
                FqName h12 = classId.h();
                za.k.d(h12, "classId.packageFqName");
                PackageViewDescriptor H3 = moduleDescriptor.H(h12);
                List<Name> f12 = classId.i().f();
                za.k.d(f12, "classId.relativeClassName.pathSegments()");
                zc.h u11 = H3.u();
                T2 = _Collections.T(f12);
                za.k.d(T2, "segments.first()");
                e10 = u11.e((Name) T2, xb.d.FROM_DESERIALIZATION);
                if (e10 == null) {
                    return null;
                }
                for (Name name3 : f12.subList(1, f12.size())) {
                    if (!(e10 instanceof ClassDescriptor)) {
                        return null;
                    }
                    zc.h F03 = ((ClassDescriptor) e10).F0();
                    za.k.d(name3, "name");
                    ClassifierDescriptor e14 = F03.e(name3, xb.d.FROM_DESERIALIZATION);
                    e10 = e14 instanceof ClassDescriptor ? (ClassDescriptor) e14 : null;
                    if (e10 == null) {
                        return null;
                    }
                }
            }
            e12 = null;
            if (e12 == null) {
            }
        }
        return e10;
    }

    public static final ClassDescriptor c(ModuleDescriptor moduleDescriptor, ClassId classId, NotFoundClasses notFoundClasses) {
        Sequence f10;
        Sequence w10;
        List<Integer> C;
        za.k.e(moduleDescriptor, "<this>");
        za.k.e(classId, "classId");
        za.k.e(notFoundClasses, "notFoundClasses");
        ClassDescriptor a10 = a(moduleDescriptor, classId);
        if (a10 != null) {
            return a10;
        }
        f10 = rd.l.f(classId, a.f16747n);
        w10 = _Sequences.w(f10, b.f16748e);
        C = _Sequences.C(w10);
        return notFoundClasses.d(classId, C);
    }

    public static final TypeAliasDescriptor d(ModuleDescriptor moduleDescriptor, ClassId classId) {
        za.k.e(moduleDescriptor, "<this>");
        za.k.e(classId, "classId");
        ClassifierDescriptor b10 = b(moduleDescriptor, classId);
        if (b10 instanceof TypeAliasDescriptor) {
            return (TypeAliasDescriptor) b10;
        }
        return null;
    }
}
