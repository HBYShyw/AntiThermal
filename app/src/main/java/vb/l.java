package vb;

import com.oplus.backup.sdk.common.utils.Constants;
import fc.d0;
import gb.KDeclarationContainer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Arrays;
import oc.FqName;
import oc.Name;
import pb.Visibilities;
import pb.n1;
import rd.Sequence;
import rd._Sequences;
import za.FunctionReference;
import za.Lambda;
import za.Reflection;
import za.SpreadBuilder;

/* compiled from: ReflectJavaClass.kt */
/* loaded from: classes2.dex */
public final class l extends ReflectJavaElement implements vb.h, ReflectJavaModifierListOwner, fc.g {

    /* renamed from: a, reason: collision with root package name */
    private final Class<?> f19240a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a extends FunctionReference implements ya.l<Member, Boolean> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f19241n = new a();

        a() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(Member.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "isSynthetic()Z";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(Member member) {
            za.k.e(member, "p0");
            return Boolean.valueOf(member.isSynthetic());
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "isSynthetic";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b extends FunctionReference implements ya.l<Constructor<?>, ReflectJavaConstructor> {

        /* renamed from: n, reason: collision with root package name */
        public static final b f19242n = new b();

        b() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(ReflectJavaConstructor.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "<init>(Ljava/lang/reflect/Constructor;)V";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final ReflectJavaConstructor invoke(Constructor<?> constructor) {
            za.k.e(constructor, "p0");
            return new ReflectJavaConstructor(constructor);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "<init>";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class c extends FunctionReference implements ya.l<Member, Boolean> {

        /* renamed from: n, reason: collision with root package name */
        public static final c f19243n = new c();

        c() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(Member.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "isSynthetic()Z";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(Member member) {
            za.k.e(member, "p0");
            return Boolean.valueOf(member.isSynthetic());
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "isSynthetic";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class d extends FunctionReference implements ya.l<Field, ReflectJavaField> {

        /* renamed from: n, reason: collision with root package name */
        public static final d f19244n = new d();

        d() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(ReflectJavaField.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "<init>(Ljava/lang/reflect/Field;)V";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final ReflectJavaField invoke(Field field) {
            za.k.e(field, "p0");
            return new ReflectJavaField(field);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "<init>";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.l<Class<?>, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f19245e = new e();

        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(Class<?> cls) {
            String simpleName = cls.getSimpleName();
            za.k.d(simpleName, "it.simpleName");
            return Boolean.valueOf(simpleName.length() == 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public static final class f extends Lambda implements ya.l<Class<?>, Name> {

        /* renamed from: e, reason: collision with root package name */
        public static final f f19246e = new f();

        f() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Name invoke(Class<?> cls) {
            String simpleName = cls.getSimpleName();
            if (!Name.h(simpleName)) {
                simpleName = null;
            }
            if (simpleName != null) {
                return Name.f(simpleName);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public static final class g extends Lambda implements ya.l<Method, Boolean> {
        g() {
            super(1);
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x001d, code lost:
        
            if (r3.d0(r4) == false) goto L9;
         */
        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Boolean invoke(Method method) {
            boolean z10 = true;
            if (!method.isSynthetic()) {
                if (l.this.I()) {
                    l lVar = l.this;
                    za.k.d(method, Constants.MessagerConstants.METHOD_KEY);
                }
                return Boolean.valueOf(z10);
            }
            z10 = false;
            return Boolean.valueOf(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ReflectJavaClass.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class h extends FunctionReference implements ya.l<Method, ReflectJavaMethod> {

        /* renamed from: n, reason: collision with root package name */
        public static final h f19248n = new h();

        h() {
            super(1);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(ReflectJavaMethod.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "<init>(Ljava/lang/reflect/Method;)V";
        }

        @Override // ya.l
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final ReflectJavaMethod invoke(Method method) {
            za.k.e(method, "p0");
            return new ReflectJavaMethod(method);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "<init>";
        }
    }

    public l(Class<?> cls) {
        za.k.e(cls, "klass");
        this.f19240a = cls;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean d0(Method method) {
        String name = method.getName();
        if (za.k.a(name, "values")) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            za.k.d(parameterTypes, "method.parameterTypes");
            if (parameterTypes.length == 0) {
                return true;
            }
        } else if (za.k.a(name, "valueOf")) {
            return Arrays.equals(method.getParameterTypes(), new Class[]{String.class});
        }
        return false;
    }

    @Override // fc.g
    public boolean A() {
        Boolean e10 = vb.b.f19208a.e(this.f19240a);
        if (e10 != null) {
            return e10.booleanValue();
        }
        return false;
    }

    @Override // fc.g
    public boolean B() {
        return false;
    }

    @Override // fc.g
    public boolean I() {
        return this.f19240a.isEnum();
    }

    @Override // vb.ReflectJavaModifierListOwner
    public int K() {
        return this.f19240a.getModifiers();
    }

    @Override // fc.g
    public boolean N() {
        return this.f19240a.isInterface();
    }

    @Override // fc.g
    public d0 O() {
        return null;
    }

    @Override // fc.g
    public Collection<fc.j> T() {
        List j10;
        Class<?>[] c10 = vb.b.f19208a.c(this.f19240a);
        if (c10 != null) {
            ArrayList arrayList = new ArrayList(c10.length);
            for (Class<?> cls : c10) {
                arrayList.add(new ReflectJavaClassifierType(cls));
            }
            return arrayList;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // fc.g
    /* renamed from: X, reason: merged with bridge method [inline-methods] */
    public List<ReflectJavaConstructor> p() {
        Sequence r10;
        Sequence n10;
        Sequence w10;
        List<ReflectJavaConstructor> C;
        Constructor<?>[] declaredConstructors = this.f19240a.getDeclaredConstructors();
        za.k.d(declaredConstructors, "klass.declaredConstructors");
        r10 = _Arrays.r(declaredConstructors);
        n10 = _Sequences.n(r10, a.f19241n);
        w10 = _Sequences.w(n10, b.f19242n);
        C = _Sequences.C(w10);
        return C;
    }

    @Override // vb.h
    /* renamed from: Y, reason: merged with bridge method [inline-methods] */
    public Class<?> D() {
        return this.f19240a;
    }

    @Override // fc.g
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public List<ReflectJavaField> getFields() {
        Sequence r10;
        Sequence n10;
        Sequence w10;
        List<ReflectJavaField> C;
        Field[] declaredFields = this.f19240a.getDeclaredFields();
        za.k.d(declaredFields, "klass.declaredFields");
        r10 = _Arrays.r(declaredFields);
        n10 = _Sequences.n(r10, c.f19243n);
        w10 = _Sequences.w(n10, d.f19244n);
        C = _Sequences.C(w10);
        return C;
    }

    @Override // fc.g
    /* renamed from: a0, reason: merged with bridge method [inline-methods] */
    public List<Name> Q() {
        Sequence r10;
        Sequence n10;
        Sequence x10;
        List<Name> C;
        Class<?>[] declaredClasses = this.f19240a.getDeclaredClasses();
        za.k.d(declaredClasses, "klass.declaredClasses");
        r10 = _Arrays.r(declaredClasses);
        n10 = _Sequences.n(r10, e.f19245e);
        x10 = _Sequences.x(n10, f.f19246e);
        C = _Sequences.C(x10);
        return C;
    }

    @Override // fc.g
    /* renamed from: b0, reason: merged with bridge method [inline-methods] */
    public List<ReflectJavaMethod> S() {
        Sequence r10;
        Sequence m10;
        Sequence w10;
        List<ReflectJavaMethod> C;
        Method[] declaredMethods = this.f19240a.getDeclaredMethods();
        za.k.d(declaredMethods, "klass.declaredMethods");
        r10 = _Arrays.r(declaredMethods);
        m10 = _Sequences.m(r10, new g());
        w10 = _Sequences.w(m10, h.f19248n);
        C = _Sequences.C(w10);
        return C;
    }

    @Override // fc.g
    /* renamed from: c0, reason: merged with bridge method [inline-methods] */
    public l u() {
        Class<?> declaringClass = this.f19240a.getDeclaringClass();
        if (declaringClass != null) {
            return new l(declaringClass);
        }
        return null;
    }

    @Override // fc.g
    public FqName d() {
        FqName b10 = reflectClassUtil.a(this.f19240a).b();
        za.k.d(b10, "klass.classId.asSingleFqName()");
        return b10;
    }

    public boolean equals(Object obj) {
        return (obj instanceof l) && za.k.a(this.f19240a, ((l) obj).f19240a);
    }

    @Override // fc.s
    public n1 g() {
        int K = K();
        if (Modifier.isPublic(K)) {
            return Visibilities.h.f16718c;
        }
        if (Modifier.isPrivate(K)) {
            return Visibilities.e.f16715c;
        }
        if (Modifier.isProtected(K)) {
            return Modifier.isStatic(K) ? tb.c.f18706c : tb.b.f18705c;
        }
        return tb.a.f18704c;
    }

    @Override // fc.t
    public Name getName() {
        Name f10 = Name.f(this.f19240a.getSimpleName());
        za.k.d(f10, "identifier(klass.simpleName)");
        return f10;
    }

    public int hashCode() {
        return this.f19240a.hashCode();
    }

    @Override // fc.d
    public /* bridge */ /* synthetic */ Collection i() {
        return i();
    }

    @Override // fc.d
    public /* bridge */ /* synthetic */ fc.a j(FqName fqName) {
        return j(fqName);
    }

    @Override // fc.d
    public boolean k() {
        return false;
    }

    @Override // fc.z
    public List<ReflectJavaTypeParameter> m() {
        TypeVariable<Class<?>>[] typeParameters = this.f19240a.getTypeParameters();
        za.k.d(typeParameters, "klass.typeParameters");
        ArrayList arrayList = new ArrayList(typeParameters.length);
        for (TypeVariable<Class<?>> typeVariable : typeParameters) {
            arrayList.add(new ReflectJavaTypeParameter(typeVariable));
        }
        return arrayList;
    }

    @Override // fc.s
    public boolean n() {
        return Modifier.isAbstract(K());
    }

    @Override // fc.s
    public boolean o() {
        return Modifier.isStatic(K());
    }

    @Override // fc.g
    public Collection<fc.j> q() {
        Class cls;
        List m10;
        int u7;
        List j10;
        cls = Object.class;
        if (za.k.a(this.f19240a, cls)) {
            j10 = kotlin.collections.r.j();
            return j10;
        }
        SpreadBuilder spreadBuilder = new SpreadBuilder(2);
        Object genericSuperclass = this.f19240a.getGenericSuperclass();
        spreadBuilder.a(genericSuperclass != null ? genericSuperclass : Object.class);
        Type[] genericInterfaces = this.f19240a.getGenericInterfaces();
        za.k.d(genericInterfaces, "klass.genericInterfaces");
        spreadBuilder.b(genericInterfaces);
        m10 = kotlin.collections.r.m(spreadBuilder.d(new Type[spreadBuilder.c()]));
        u7 = kotlin.collections.s.u(m10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator it = m10.iterator();
        while (it.hasNext()) {
            arrayList.add(new ReflectJavaClassifierType((Type) it.next()));
        }
        return arrayList;
    }

    @Override // fc.g
    public boolean t() {
        Boolean f10 = vb.b.f19208a.f(this.f19240a);
        if (f10 != null) {
            return f10.booleanValue();
        }
        return false;
    }

    public String toString() {
        return l.class.getName() + ": " + this.f19240a;
    }

    @Override // fc.g
    public Collection<fc.w> v() {
        Object[] d10 = vb.b.f19208a.d(this.f19240a);
        if (d10 == null) {
            d10 = new Object[0];
        }
        ArrayList arrayList = new ArrayList(d10.length);
        for (Object obj : d10) {
            arrayList.add(new y(obj));
        }
        return arrayList;
    }

    @Override // fc.s
    public boolean w() {
        return Modifier.isFinal(K());
    }

    @Override // fc.g
    public boolean y() {
        return this.f19240a.isAnnotation();
    }

    @Override // vb.h, fc.d
    public List<ReflectJavaAnnotation> i() {
        List<ReflectJavaAnnotation> j10;
        Annotation[] declaredAnnotations;
        List<ReflectJavaAnnotation> b10;
        AnnotatedElement D = D();
        if (D != null && (declaredAnnotations = D.getDeclaredAnnotations()) != null && (b10 = i.b(declaredAnnotations)) != null) {
            return b10;
        }
        j10 = kotlin.collections.r.j();
        return j10;
    }

    @Override // vb.h, fc.d
    public ReflectJavaAnnotation j(FqName fqName) {
        Annotation[] declaredAnnotations;
        za.k.e(fqName, "fqName");
        AnnotatedElement D = D();
        if (D == null || (declaredAnnotations = D.getDeclaredAnnotations()) == null) {
            return null;
        }
        return i.a(declaredAnnotations, fqName);
    }
}
