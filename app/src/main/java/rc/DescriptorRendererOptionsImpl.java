package rc;

import cb.Delegates;
import cb.ObservableProperty;
import gb.KClass;
import gd.g0;
import java.lang.reflect.Field;
import java.util.Set;
import kotlin.collections.s0;
import oc.FqName;
import pb.ValueParameterDescriptor;
import qb.AnnotationDescriptor;
import rc.ClassifierNamePolicy;
import rc.c;
import rc.f;
import sd.StringsJVM;
import za.Lambda;
import za.MutablePropertyReference1Impl;
import za.PropertyReference1Impl;
import za.Reflection;

/* compiled from: DescriptorRendererOptionsImpl.kt */
/* renamed from: rc.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class DescriptorRendererOptionsImpl implements f {
    static final /* synthetic */ gb.l<Object>[] X = {Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "classifierNamePolicy", "getClassifierNamePolicy()Lorg/jetbrains/kotlin/renderer/ClassifierNamePolicy;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "withDefinedIn", "getWithDefinedIn()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "withSourceFileForTopLevel", "getWithSourceFileForTopLevel()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "modifiers", "getModifiers()Ljava/util/Set;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "startFromName", "getStartFromName()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "startFromDeclarationKeyword", "getStartFromDeclarationKeyword()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "debugMode", "getDebugMode()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "classWithPrimaryConstructor", "getClassWithPrimaryConstructor()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "verbose", "getVerbose()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "unitReturnType", "getUnitReturnType()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "withoutReturnType", "getWithoutReturnType()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "enhancedTypes", "getEnhancedTypes()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "normalizedVisibilities", "getNormalizedVisibilities()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderDefaultVisibility", "getRenderDefaultVisibility()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderDefaultModality", "getRenderDefaultModality()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderConstructorDelegation", "getRenderConstructorDelegation()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderPrimaryConstructorParametersAsProperties", "getRenderPrimaryConstructorParametersAsProperties()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "actualPropertiesInPrimaryConstructor", "getActualPropertiesInPrimaryConstructor()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "uninferredTypeParameterAsName", "getUninferredTypeParameterAsName()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "includePropertyConstant", "getIncludePropertyConstant()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "withoutTypeParameters", "getWithoutTypeParameters()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "withoutSuperTypes", "getWithoutSuperTypes()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "typeNormalizer", "getTypeNormalizer()Lkotlin/jvm/functions/Function1;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "defaultParameterValueRenderer", "getDefaultParameterValueRenderer()Lkotlin/jvm/functions/Function1;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "secondaryConstructorsAsPrimary", "getSecondaryConstructorsAsPrimary()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "overrideRenderingPolicy", "getOverrideRenderingPolicy()Lorg/jetbrains/kotlin/renderer/OverrideRenderingPolicy;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "valueParametersHandler", "getValueParametersHandler()Lorg/jetbrains/kotlin/renderer/DescriptorRenderer$ValueParametersHandler;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "textFormat", "getTextFormat()Lorg/jetbrains/kotlin/renderer/RenderingFormat;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "parameterNameRenderingPolicy", "getParameterNameRenderingPolicy()Lorg/jetbrains/kotlin/renderer/ParameterNameRenderingPolicy;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "receiverAfterName", "getReceiverAfterName()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderCompanionObjectName", "getRenderCompanionObjectName()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "propertyAccessorRenderingPolicy", "getPropertyAccessorRenderingPolicy()Lorg/jetbrains/kotlin/renderer/PropertyAccessorRenderingPolicy;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderDefaultAnnotationArguments", "getRenderDefaultAnnotationArguments()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "eachAnnotationOnNewLine", "getEachAnnotationOnNewLine()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "excludedAnnotationClasses", "getExcludedAnnotationClasses()Ljava/util/Set;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "excludedTypeAnnotationClasses", "getExcludedTypeAnnotationClasses()Ljava/util/Set;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "annotationFilter", "getAnnotationFilter()Lkotlin/jvm/functions/Function1;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "annotationArgumentsRenderingPolicy", "getAnnotationArgumentsRenderingPolicy()Lorg/jetbrains/kotlin/renderer/AnnotationArgumentsRenderingPolicy;")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "alwaysRenderModifiers", "getAlwaysRenderModifiers()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderConstructorKeyword", "getRenderConstructorKeyword()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderUnabbreviatedType", "getRenderUnabbreviatedType()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderTypeExpansions", "getRenderTypeExpansions()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "includeAdditionalModifiers", "getIncludeAdditionalModifiers()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "parameterNamesInFunctionalTypes", "getParameterNamesInFunctionalTypes()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "renderFunctionContracts", "getRenderFunctionContracts()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "presentableUnresolvedTypes", "getPresentableUnresolvedTypes()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "boldOnlyForNamesInHtml", "getBoldOnlyForNamesInHtml()Z")), Reflection.e(new MutablePropertyReference1Impl(Reflection.b(DescriptorRendererOptionsImpl.class), "informativeErrorType", "getInformativeErrorType()Z"))};
    private final cb.d A;
    private final cb.d B;
    private final cb.d C;
    private final cb.d D;
    private final cb.d E;
    private final cb.d F;
    private final cb.d G;
    private final cb.d H;
    private final cb.d I;
    private final cb.d J;
    private final cb.d K;
    private final cb.d L;
    private final cb.d M;
    private final cb.d N;
    private final cb.d O;
    private final cb.d P;
    private final cb.d Q;
    private final cb.d R;
    private final cb.d S;
    private final cb.d T;
    private final cb.d U;
    private final cb.d V;
    private final cb.d W;

    /* renamed from: a, reason: collision with root package name */
    private boolean f17757a;

    /* renamed from: b, reason: collision with root package name */
    private final cb.d f17758b = m0(ClassifierNamePolicy.c.f17701a);

    /* renamed from: c, reason: collision with root package name */
    private final cb.d f17759c;

    /* renamed from: d, reason: collision with root package name */
    private final cb.d f17760d;

    /* renamed from: e, reason: collision with root package name */
    private final cb.d f17761e;

    /* renamed from: f, reason: collision with root package name */
    private final cb.d f17762f;

    /* renamed from: g, reason: collision with root package name */
    private final cb.d f17763g;

    /* renamed from: h, reason: collision with root package name */
    private final cb.d f17764h;

    /* renamed from: i, reason: collision with root package name */
    private final cb.d f17765i;

    /* renamed from: j, reason: collision with root package name */
    private final cb.d f17766j;

    /* renamed from: k, reason: collision with root package name */
    private final cb.d f17767k;

    /* renamed from: l, reason: collision with root package name */
    private final cb.d f17768l;

    /* renamed from: m, reason: collision with root package name */
    private final cb.d f17769m;

    /* renamed from: n, reason: collision with root package name */
    private final cb.d f17770n;

    /* renamed from: o, reason: collision with root package name */
    private final cb.d f17771o;

    /* renamed from: p, reason: collision with root package name */
    private final cb.d f17772p;

    /* renamed from: q, reason: collision with root package name */
    private final cb.d f17773q;

    /* renamed from: r, reason: collision with root package name */
    private final cb.d f17774r;

    /* renamed from: s, reason: collision with root package name */
    private final cb.d f17775s;

    /* renamed from: t, reason: collision with root package name */
    private final cb.d f17776t;

    /* renamed from: u, reason: collision with root package name */
    private final cb.d f17777u;

    /* renamed from: v, reason: collision with root package name */
    private final cb.d f17778v;

    /* renamed from: w, reason: collision with root package name */
    private final cb.d f17779w;

    /* renamed from: x, reason: collision with root package name */
    private final cb.d f17780x;

    /* renamed from: y, reason: collision with root package name */
    private final cb.d f17781y;

    /* renamed from: z, reason: collision with root package name */
    private final cb.d f17782z;

    /* compiled from: DescriptorRendererOptionsImpl.kt */
    /* renamed from: rc.g$a */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<ValueParameterDescriptor, String> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f17783e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final String invoke(ValueParameterDescriptor valueParameterDescriptor) {
            za.k.e(valueParameterDescriptor, "it");
            return "...";
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* compiled from: Delegates.kt */
    /* renamed from: rc.g$b */
    /* loaded from: classes2.dex */
    public static final class b<T> extends ObservableProperty<T> {

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ DescriptorRendererOptionsImpl f17784b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(Object obj, DescriptorRendererOptionsImpl descriptorRendererOptionsImpl) {
            super(obj);
            this.f17784b = descriptorRendererOptionsImpl;
        }

        @Override // cb.ObservableProperty
        protected boolean d(gb.l<?> lVar, T t7, T t10) {
            za.k.e(lVar, "property");
            if (this.f17784b.k0()) {
                throw new IllegalStateException("Cannot modify readonly DescriptorRendererOptions");
            }
            return true;
        }
    }

    /* compiled from: DescriptorRendererOptionsImpl.kt */
    /* renamed from: rc.g$c */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<g0, g0> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f17785e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(g0 g0Var) {
            za.k.e(g0Var, "it");
            return g0Var;
        }
    }

    public DescriptorRendererOptionsImpl() {
        Set e10;
        Boolean bool = Boolean.TRUE;
        this.f17759c = m0(bool);
        this.f17760d = m0(bool);
        this.f17761e = m0(e.f17739g);
        Boolean bool2 = Boolean.FALSE;
        this.f17762f = m0(bool2);
        this.f17763g = m0(bool2);
        this.f17764h = m0(bool2);
        this.f17765i = m0(bool2);
        this.f17766j = m0(bool2);
        this.f17767k = m0(bool);
        this.f17768l = m0(bool2);
        this.f17769m = m0(bool2);
        this.f17770n = m0(bool2);
        this.f17771o = m0(bool);
        this.f17772p = m0(bool);
        this.f17773q = m0(bool2);
        this.f17774r = m0(bool2);
        this.f17775s = m0(bool2);
        this.f17776t = m0(bool2);
        this.f17777u = m0(bool2);
        this.f17778v = m0(bool2);
        this.f17779w = m0(bool2);
        this.f17780x = m0(c.f17785e);
        this.f17781y = m0(a.f17783e);
        this.f17782z = m0(bool);
        this.A = m0(j.RENDER_OPEN);
        this.B = m0(c.l.a.f17724a);
        this.C = m0(m.PLAIN);
        this.D = m0(k.ALL);
        this.E = m0(bool2);
        this.F = m0(bool2);
        this.G = m0(l.DEBUG);
        this.H = m0(bool2);
        this.I = m0(bool2);
        e10 = s0.e();
        this.J = m0(e10);
        this.K = m0(h.f17786a.a());
        this.L = m0(null);
        this.M = m0(rc.a.NO_ARGUMENTS);
        this.N = m0(bool2);
        this.O = m0(bool);
        this.P = m0(bool);
        this.Q = m0(bool2);
        this.R = m0(bool);
        this.S = m0(bool);
        this.T = m0(bool2);
        this.U = m0(bool2);
        this.V = m0(bool2);
        this.W = m0(bool);
    }

    private final <T> cb.d<DescriptorRendererOptionsImpl, T> m0(T t7) {
        Delegates delegates = Delegates.f5039a;
        return new b(t7, this);
    }

    public boolean A() {
        return ((Boolean) this.R.a(this, X[42])).booleanValue();
    }

    public boolean B() {
        return f.a.a(this);
    }

    public boolean C() {
        return f.a.b(this);
    }

    public boolean D() {
        return ((Boolean) this.f17777u.a(this, X[19])).booleanValue();
    }

    public boolean E() {
        return ((Boolean) this.W.a(this, X[47])).booleanValue();
    }

    public Set<e> F() {
        return (Set) this.f17761e.a(this, X[3]);
    }

    public boolean G() {
        return ((Boolean) this.f17770n.a(this, X[12])).booleanValue();
    }

    public j H() {
        return (j) this.A.a(this, X[25]);
    }

    public k I() {
        return (k) this.D.a(this, X[28]);
    }

    public boolean J() {
        return ((Boolean) this.S.a(this, X[43])).booleanValue();
    }

    public boolean K() {
        return ((Boolean) this.U.a(this, X[45])).booleanValue();
    }

    public l L() {
        return (l) this.G.a(this, X[31]);
    }

    public boolean M() {
        return ((Boolean) this.E.a(this, X[29])).booleanValue();
    }

    public boolean N() {
        return ((Boolean) this.F.a(this, X[30])).booleanValue();
    }

    public boolean O() {
        return ((Boolean) this.f17773q.a(this, X[15])).booleanValue();
    }

    public boolean P() {
        return ((Boolean) this.O.a(this, X[39])).booleanValue();
    }

    public boolean Q() {
        return ((Boolean) this.H.a(this, X[32])).booleanValue();
    }

    public boolean R() {
        return ((Boolean) this.f17772p.a(this, X[14])).booleanValue();
    }

    public boolean S() {
        return ((Boolean) this.f17771o.a(this, X[13])).booleanValue();
    }

    public boolean T() {
        return ((Boolean) this.f17774r.a(this, X[16])).booleanValue();
    }

    public boolean U() {
        return ((Boolean) this.Q.a(this, X[41])).booleanValue();
    }

    public boolean V() {
        return ((Boolean) this.P.a(this, X[40])).booleanValue();
    }

    public boolean W() {
        return ((Boolean) this.f17782z.a(this, X[24])).booleanValue();
    }

    public boolean X() {
        return ((Boolean) this.f17763g.a(this, X[5])).booleanValue();
    }

    public boolean Y() {
        return ((Boolean) this.f17762f.a(this, X[4])).booleanValue();
    }

    public m Z() {
        return (m) this.C.a(this, X[27]);
    }

    @Override // rc.f
    public void a(Set<FqName> set) {
        za.k.e(set, "<set-?>");
        this.K.b(this, X[35], set);
    }

    public ya.l<g0, g0> a0() {
        return (ya.l) this.f17780x.a(this, X[22]);
    }

    @Override // rc.f
    public void b(boolean z10) {
        this.f17762f.b(this, X[4], Boolean.valueOf(z10));
    }

    public boolean b0() {
        return ((Boolean) this.f17776t.a(this, X[18])).booleanValue();
    }

    @Override // rc.f
    public void c(Set<? extends e> set) {
        za.k.e(set, "<set-?>");
        this.f17761e.b(this, X[3], set);
    }

    public boolean c0() {
        return ((Boolean) this.f17767k.a(this, X[9])).booleanValue();
    }

    @Override // rc.f
    public void d(m mVar) {
        za.k.e(mVar, "<set-?>");
        this.C.b(this, X[27], mVar);
    }

    public c.l d0() {
        return (c.l) this.B.a(this, X[26]);
    }

    @Override // rc.f
    public void e(boolean z10) {
        this.f17759c.b(this, X[1], Boolean.valueOf(z10));
    }

    public boolean e0() {
        return ((Boolean) this.f17766j.a(this, X[8])).booleanValue();
    }

    @Override // rc.f
    public boolean f() {
        return ((Boolean) this.f17769m.a(this, X[11])).booleanValue();
    }

    public boolean f0() {
        return ((Boolean) this.f17759c.a(this, X[1])).booleanValue();
    }

    @Override // rc.f
    public void g(boolean z10) {
        this.f17779w.b(this, X[21], Boolean.valueOf(z10));
    }

    public boolean g0() {
        return ((Boolean) this.f17760d.a(this, X[2])).booleanValue();
    }

    @Override // rc.f
    public void h(boolean z10) {
        this.f17764h.b(this, X[6], Boolean.valueOf(z10));
    }

    public boolean h0() {
        return ((Boolean) this.f17768l.a(this, X[10])).booleanValue();
    }

    @Override // rc.f
    public void i(boolean z10) {
        this.F.b(this, X[30], Boolean.valueOf(z10));
    }

    public boolean i0() {
        return ((Boolean) this.f17779w.a(this, X[21])).booleanValue();
    }

    @Override // rc.f
    public void j(boolean z10) {
        this.E.b(this, X[29], Boolean.valueOf(z10));
    }

    public boolean j0() {
        return ((Boolean) this.f17778v.a(this, X[20])).booleanValue();
    }

    @Override // rc.f
    public void k(k kVar) {
        za.k.e(kVar, "<set-?>");
        this.D.b(this, X[28], kVar);
    }

    public final boolean k0() {
        return this.f17757a;
    }

    @Override // rc.f
    public Set<FqName> l() {
        return (Set) this.K.a(this, X[35]);
    }

    public final void l0() {
        this.f17757a = true;
    }

    @Override // rc.f
    public boolean m() {
        return ((Boolean) this.f17764h.a(this, X[6])).booleanValue();
    }

    @Override // rc.f
    public rc.a n() {
        return (rc.a) this.M.a(this, X[37]);
    }

    @Override // rc.f
    public void o(ClassifierNamePolicy classifierNamePolicy) {
        za.k.e(classifierNamePolicy, "<set-?>");
        this.f17758b.b(this, X[0], classifierNamePolicy);
    }

    @Override // rc.f
    public void p(boolean z10) {
        this.f17778v.b(this, X[20], Boolean.valueOf(z10));
    }

    public final DescriptorRendererOptionsImpl q() {
        DescriptorRendererOptionsImpl descriptorRendererOptionsImpl = new DescriptorRendererOptionsImpl();
        Field[] declaredFields = DescriptorRendererOptionsImpl.class.getDeclaredFields();
        za.k.d(declaredFields, "this::class.java.declaredFields");
        for (Field field : declaredFields) {
            if ((field.getModifiers() & 8) == 0) {
                field.setAccessible(true);
                Object obj = field.get(this);
                ObservableProperty observableProperty = obj instanceof ObservableProperty ? (ObservableProperty) obj : null;
                if (observableProperty != null) {
                    String name = field.getName();
                    za.k.d(name, "field.name");
                    StringsJVM.D(name, "is", false, 2, null);
                    KClass b10 = Reflection.b(DescriptorRendererOptionsImpl.class);
                    String name2 = field.getName();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("get");
                    String name3 = field.getName();
                    za.k.d(name3, "field.name");
                    if (name3.length() > 0) {
                        char upperCase = Character.toUpperCase(name3.charAt(0));
                        String substring = name3.substring(1);
                        za.k.d(substring, "this as java.lang.String).substring(startIndex)");
                        name3 = upperCase + substring;
                    }
                    sb2.append(name3);
                    field.set(descriptorRendererOptionsImpl, descriptorRendererOptionsImpl.m0(observableProperty.a(this, new PropertyReference1Impl(b10, name2, sb2.toString()))));
                }
            }
        }
        return descriptorRendererOptionsImpl;
    }

    public boolean r() {
        return ((Boolean) this.f17775s.a(this, X[17])).booleanValue();
    }

    public boolean s() {
        return ((Boolean) this.N.a(this, X[38])).booleanValue();
    }

    public ya.l<AnnotationDescriptor, Boolean> t() {
        return (ya.l) this.L.a(this, X[36]);
    }

    public boolean u() {
        return ((Boolean) this.V.a(this, X[46])).booleanValue();
    }

    public boolean v() {
        return ((Boolean) this.f17765i.a(this, X[7])).booleanValue();
    }

    public ClassifierNamePolicy w() {
        return (ClassifierNamePolicy) this.f17758b.a(this, X[0]);
    }

    public ya.l<ValueParameterDescriptor, String> x() {
        return (ya.l) this.f17781y.a(this, X[23]);
    }

    public boolean y() {
        return ((Boolean) this.I.a(this, X[33])).booleanValue();
    }

    public Set<FqName> z() {
        return (Set) this.J.a(this, X[34]);
    }
}
