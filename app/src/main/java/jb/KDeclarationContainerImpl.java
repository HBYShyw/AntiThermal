package jb;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import jb.ReflectProperties;
import kotlin.collections.MapsJVM;
import kotlin.collections._Collections;
import ma.Unit;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import sd.StringsJVM;
import ub.RuntimeModuleData;
import vb.reflectClassUtil;
import za.ClassBasedDeclarationContainer;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.PropertyReference1Impl;
import za.Reflection;
import zc.ResolutionScope;

/* compiled from: KDeclarationContainerImpl.kt */
/* renamed from: jb.o, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class KDeclarationContainerImpl implements ClassBasedDeclarationContainer {

    /* renamed from: e, reason: collision with root package name */
    public static final a f13292e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final Class<?> f13293f = DefaultConstructorMarker.class;

    /* renamed from: g, reason: collision with root package name */
    private static final sd.j f13294g = new sd.j("<v#(\\d+)>");

    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final sd.j a() {
            return KDeclarationContainerImpl.f13294g;
        }
    }

    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$b */
    /* loaded from: classes2.dex */
    public abstract class b {

        /* renamed from: c, reason: collision with root package name */
        static final /* synthetic */ gb.l<Object>[] f13295c = {Reflection.g(new PropertyReference1Impl(Reflection.b(b.class), "moduleData", "getModuleData()Lorg/jetbrains/kotlin/descriptors/runtime/components/RuntimeModuleData;"))};

        /* renamed from: a, reason: collision with root package name */
        private final ReflectProperties.a f13296a;

        /* compiled from: KDeclarationContainerImpl.kt */
        /* renamed from: jb.o$b$a */
        /* loaded from: classes2.dex */
        static final class a extends Lambda implements ya.a<RuntimeModuleData> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ KDeclarationContainerImpl f13298e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(KDeclarationContainerImpl kDeclarationContainerImpl) {
                super(0);
                this.f13298e = kDeclarationContainerImpl;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final RuntimeModuleData invoke() {
                return h0.a(this.f13298e.e());
            }
        }

        public b() {
            this.f13296a = ReflectProperties.d(new a(KDeclarationContainerImpl.this));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final RuntimeModuleData a() {
            T b10 = this.f13296a.b(this, f13295c[0]);
            za.k.d(b10, "<get-moduleData>(...)");
            return (RuntimeModuleData) b10;
        }
    }

    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$c */
    /* loaded from: classes2.dex */
    protected enum c {
        DECLARED,
        INHERITED;

        public final boolean b(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "member");
            return callableMemberDescriptor.getKind().a() == (this == DECLARED);
        }
    }

    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.l<FunctionDescriptor, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final d f13302e = new d();

        d() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(FunctionDescriptor functionDescriptor) {
            za.k.e(functionDescriptor, "descriptor");
            return rc.c.f17711j.q(functionDescriptor) + " | " + l0.f13288a.g(functionDescriptor).a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$e */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.l<PropertyDescriptor, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f13303e = new e();

        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(PropertyDescriptor propertyDescriptor) {
            za.k.e(propertyDescriptor, "descriptor");
            return rc.c.f17711j.q(propertyDescriptor) + " | " + l0.f13288a.f(propertyDescriptor).a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$f */
    /* loaded from: classes2.dex */
    public static final class f extends Lambda implements ya.p<pb.u, pb.u, Integer> {

        /* renamed from: e, reason: collision with root package name */
        public static final f f13304e = new f();

        f() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Integer invoke(pb.u uVar, pb.u uVar2) {
            Integer d10 = DescriptorVisibilities.d(uVar, uVar2);
            return Integer.valueOf(d10 == null ? 0 : d10.intValue());
        }
    }

    /* compiled from: KDeclarationContainerImpl.kt */
    /* renamed from: jb.o$g */
    /* loaded from: classes2.dex */
    public static final class g extends jb.f {
        g(KDeclarationContainerImpl kDeclarationContainerImpl) {
            super(kDeclarationContainerImpl);
        }

        @Override // sb.DeclarationDescriptorVisitorEmptyBodies, pb.DeclarationDescriptorVisitor
        /* renamed from: r, reason: merged with bridge method [inline-methods] */
        public KCallableImpl<?> l(ConstructorDescriptor constructorDescriptor, Unit unit) {
            za.k.e(constructorDescriptor, "descriptor");
            za.k.e(unit, "data");
            throw new IllegalStateException("No constructors should appear here: " + constructorDescriptor);
        }
    }

    private final void A(List<Class<?>> list, String str, boolean z10) {
        list.addAll(O(str));
        int size = ((r2.size() + 32) - 1) / 32;
        for (int i10 = 0; i10 < size; i10++) {
            Class<?> cls = Integer.TYPE;
            za.k.d(cls, "TYPE");
            list.add(cls);
        }
        if (z10) {
            Class<?> cls2 = f13293f;
            list.remove(cls2);
            za.k.d(cls2, "DEFAULT_CONSTRUCTOR_MARKER");
            list.add(cls2);
            return;
        }
        list.add(Object.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int H(ya.p pVar, Object obj, Object obj2) {
        za.k.e(pVar, "$tmp0");
        return ((Number) pVar.invoke(obj, obj2)).intValue();
    }

    private final List<Class<?>> O(String str) {
        boolean H;
        int U;
        int i10;
        ArrayList arrayList = new ArrayList();
        int i11 = 1;
        while (str.charAt(i11) != ')') {
            int i12 = i11;
            while (str.charAt(i12) == '[') {
                i12++;
            }
            char charAt = str.charAt(i12);
            H = sd.v.H("VZCBSIFJD", charAt, false, 2, null);
            if (H) {
                i10 = i12 + 1;
            } else if (charAt == 'L') {
                U = sd.v.U(str, ';', i11, false, 4, null);
                i10 = U + 1;
            } else {
                throw new KotlinReflectionInternalError("Unknown type prefix in the method signature: " + str);
            }
            arrayList.add(R(str, i11, i10));
            i11 = i10;
        }
        return arrayList;
    }

    private final Class<?> P(String str) {
        int U;
        U = sd.v.U(str, ')', 0, false, 6, null);
        return R(str, U + 1, str.length());
    }

    private final Method Q(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, boolean z10) {
        Method Q;
        if (z10) {
            clsArr[0] = cls;
        }
        Method T = T(cls, str, clsArr, cls2);
        if (T != null) {
            return T;
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null && (Q = Q(superclass, str, clsArr, cls2, z10)) != null) {
            return Q;
        }
        Class<?>[] interfaces = cls.getInterfaces();
        za.k.d(interfaces, "interfaces");
        for (Class<?> cls3 : interfaces) {
            za.k.d(cls3, "superInterface");
            Method Q2 = Q(cls3, str, clsArr, cls2, z10);
            if (Q2 != null) {
                return Q2;
            }
            if (z10) {
                Class<?> a10 = ub.e.a(reflectClassUtil.f(cls3), cls3.getName() + "$DefaultImpls");
                if (a10 != null) {
                    clsArr[0] = cls3;
                    Method T2 = T(a10, str, clsArr, cls2);
                    if (T2 != null) {
                        return T2;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    private final Class<?> R(String str, int i10, int i11) {
        String y4;
        char charAt = str.charAt(i10);
        if (charAt == 'L') {
            ClassLoader f10 = reflectClassUtil.f(e());
            String substring = str.substring(i10 + 1, i11 - 1);
            za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            y4 = StringsJVM.y(substring, '/', '.', false, 4, null);
            Class<?> loadClass = f10.loadClass(y4);
            za.k.d(loadClass, "jClass.safeClassLoader.l…d - 1).replace('/', '.'))");
            return loadClass;
        }
        if (charAt == '[') {
            return o0.f(R(str, i10 + 1, i11));
        }
        if (charAt == 'V') {
            Class<?> cls = Void.TYPE;
            za.k.d(cls, "TYPE");
            return cls;
        }
        if (charAt == 'Z') {
            return Boolean.TYPE;
        }
        if (charAt == 'C') {
            return Character.TYPE;
        }
        if (charAt == 'B') {
            return Byte.TYPE;
        }
        if (charAt == 'S') {
            return Short.TYPE;
        }
        if (charAt == 'I') {
            return Integer.TYPE;
        }
        if (charAt == 'F') {
            return Float.TYPE;
        }
        if (charAt == 'J') {
            return Long.TYPE;
        }
        if (charAt == 'D') {
            return Double.TYPE;
        }
        throw new KotlinReflectionInternalError("Unknown type prefix in the method signature: " + str);
    }

    private final Constructor<?> S(Class<?> cls, List<? extends Class<?>> list) {
        try {
            Class[] clsArr = (Class[]) list.toArray(new Class[0]);
            return cls.getDeclaredConstructor((Class[]) Arrays.copyOf(clsArr, clsArr.length));
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    private final Method T(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2) {
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, (Class[]) Arrays.copyOf(clsArr, clsArr.length));
            if (za.k.a(declaredMethod.getReturnType(), cls2)) {
                return declaredMethod;
            }
            Method[] declaredMethods = cls.getDeclaredMethods();
            za.k.d(declaredMethods, "declaredMethods");
            for (Method method : declaredMethods) {
                if (za.k.a(method.getName(), str) && za.k.a(method.getReturnType(), cls2) && Arrays.equals(method.getParameterTypes(), clsArr)) {
                    return method;
                }
            }
            return null;
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    public final Constructor<?> B(String str) {
        za.k.e(str, "desc");
        return S(e(), O(str));
    }

    public final Constructor<?> C(String str) {
        za.k.e(str, "desc");
        Class<?> e10 = e();
        ArrayList arrayList = new ArrayList();
        A(arrayList, str, true);
        Unit unit = Unit.f15173a;
        return S(e10, arrayList);
    }

    public final Method D(String str, String str2, boolean z10) {
        za.k.e(str, "name");
        za.k.e(str2, "desc");
        if (za.k.a(str, "<init>")) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        if (z10) {
            arrayList.add(e());
        }
        A(arrayList, str2, false);
        return Q(M(), str + "$default", (Class[]) arrayList.toArray(new Class[0]), P(str2), z10);
    }

    public final FunctionDescriptor E(String str, String str2) {
        Collection<FunctionDescriptor> J;
        Object q02;
        String c02;
        za.k.e(str, "name");
        za.k.e(str2, "signature");
        if (za.k.a(str, "<init>")) {
            J = _Collections.z0(I());
        } else {
            Name f10 = Name.f(str);
            za.k.d(f10, "identifier(name)");
            J = J(f10);
        }
        Collection<FunctionDescriptor> collection = J;
        ArrayList arrayList = new ArrayList();
        for (Object obj : collection) {
            if (za.k.a(l0.f13288a.g((FunctionDescriptor) obj).a(), str2)) {
                arrayList.add(obj);
            }
        }
        if (arrayList.size() != 1) {
            c02 = _Collections.c0(collection, "\n", null, null, 0, null, d.f13302e, 30, null);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Function '");
            sb2.append(str);
            sb2.append("' (JVM signature: ");
            sb2.append(str2);
            sb2.append(") not resolved in ");
            sb2.append(this);
            sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            sb2.append(c02.length() == 0 ? " no members found" : '\n' + c02);
            throw new KotlinReflectionInternalError(sb2.toString());
        }
        q02 = _Collections.q0(arrayList);
        return (FunctionDescriptor) q02;
    }

    public final Method F(String str, String str2) {
        Method Q;
        za.k.e(str, "name");
        za.k.e(str2, "desc");
        if (za.k.a(str, "<init>")) {
            return null;
        }
        Class<?>[] clsArr = (Class[]) O(str2).toArray(new Class[0]);
        Class<?> P = P(str2);
        Method Q2 = Q(M(), str, clsArr, P, false);
        if (Q2 != null) {
            return Q2;
        }
        if (!M().isInterface() || (Q = Q(Object.class, str, clsArr, P, false)) == null) {
            return null;
        }
        return Q;
    }

    public final PropertyDescriptor G(String str, String str2) {
        Object q02;
        SortedMap h10;
        Object d02;
        String c02;
        Object T;
        za.k.e(str, "name");
        za.k.e(str2, "signature");
        sd.h a10 = f13294g.a(str2);
        if (a10 != null) {
            String str3 = a10.a().a().b().get(1);
            PropertyDescriptor K = K(Integer.parseInt(str3));
            if (K != null) {
                return K;
            }
            throw new KotlinReflectionInternalError("Local property #" + str3 + " not found in " + e());
        }
        Name f10 = Name.f(str);
        za.k.d(f10, "identifier(name)");
        Collection<PropertyDescriptor> N = N(f10);
        ArrayList arrayList = new ArrayList();
        for (Object obj : N) {
            if (za.k.a(l0.f13288a.f((PropertyDescriptor) obj).a(), str2)) {
                arrayList.add(obj);
            }
        }
        if (!arrayList.isEmpty()) {
            if (arrayList.size() != 1) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Object obj2 : arrayList) {
                    pb.u g6 = ((PropertyDescriptor) obj2).g();
                    Object obj3 = linkedHashMap.get(g6);
                    if (obj3 == null) {
                        obj3 = new ArrayList();
                        linkedHashMap.put(g6, obj3);
                    }
                    ((List) obj3).add(obj2);
                }
                h10 = MapsJVM.h(linkedHashMap, new n(f.f13304e));
                Collection values = h10.values();
                za.k.d(values, "properties\n             …\n                }.values");
                d02 = _Collections.d0(values);
                List list = (List) d02;
                if (list.size() == 1) {
                    za.k.d(list, "mostVisibleProperties");
                    T = _Collections.T(list);
                    return (PropertyDescriptor) T;
                }
                Name f11 = Name.f(str);
                za.k.d(f11, "identifier(name)");
                c02 = _Collections.c0(N(f11), "\n", null, null, 0, null, e.f13303e, 30, null);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Property '");
                sb2.append(str);
                sb2.append("' (JVM signature: ");
                sb2.append(str2);
                sb2.append(") not resolved in ");
                sb2.append(this);
                sb2.append(COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                sb2.append(c02.length() == 0 ? " no members found" : '\n' + c02);
                throw new KotlinReflectionInternalError(sb2.toString());
            }
            q02 = _Collections.q0(arrayList);
            return (PropertyDescriptor) q02;
        }
        throw new KotlinReflectionInternalError("Property '" + str + "' (JVM signature: " + str2 + ") not resolved in " + this);
    }

    public abstract Collection<ConstructorDescriptor> I();

    public abstract Collection<FunctionDescriptor> J(Name name);

    public abstract PropertyDescriptor K(int i10);

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004f A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x001e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Collection<KCallableImpl<?>> L(zc.h hVar, c cVar) {
        List z02;
        KCallableImpl kCallableImpl;
        za.k.e(hVar, "scope");
        za.k.e(cVar, "belonginess");
        g gVar = new g(this);
        Collection<DeclarationDescriptor> a10 = ResolutionScope.a.a(hVar, null, null, 3, null);
        ArrayList arrayList = new ArrayList();
        for (DeclarationDescriptor declarationDescriptor : a10) {
            if (declarationDescriptor instanceof CallableMemberDescriptor) {
                CallableMemberDescriptor callableMemberDescriptor = (CallableMemberDescriptor) declarationDescriptor;
                if (!za.k.a(callableMemberDescriptor.g(), DescriptorVisibilities.f16736h) && cVar.b(callableMemberDescriptor)) {
                    kCallableImpl = (KCallableImpl) declarationDescriptor.H0(gVar, Unit.f15173a);
                    if (kCallableImpl == null) {
                        arrayList.add(kCallableImpl);
                    }
                }
            }
            kCallableImpl = null;
            if (kCallableImpl == null) {
            }
        }
        z02 = _Collections.z0(arrayList);
        return z02;
    }

    protected Class<?> M() {
        Class<?> g6 = reflectClassUtil.g(e());
        return g6 == null ? e() : g6;
    }

    public abstract Collection<PropertyDescriptor> N(Name name);
}
