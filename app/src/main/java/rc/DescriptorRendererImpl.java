package rc;

import com.coui.appcompat.touchsearchview.COUIAccessibilityUtil;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import gd.IntersectionTypeConstructor;
import gd.TypeConstructor;
import gd.TypeProjection;
import gd.Variance;
import gd.a0;
import gd.g0;
import gd.i0;
import gd.o0;
import gd.p;
import gd.s1;
import gd.v1;
import gd.w0;
import gd.x1;
import id.ErrorType;
import id.ErrorTypeConstructor;
import id.ErrorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections._Sets;
import kotlin.collections.r;
import kotlin.collections.s;
import ma.NoWhenBranchMatchedException;
import ma.Unit;
import mb.KotlinBuiltIns;
import mb.StandardNames;
import mb.functionTypes;
import oc.FqName;
import oc.FqNameUnsafe;
import oc.Name;
import oc.SpecialNames;
import od.capitalizeDecapitalize;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.ClassifierDescriptor;
import pb.ClassifierDescriptorWithTypeParameters;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.DeclarationDescriptorVisitor;
import pb.DeclarationDescriptorWithSource;
import pb.DescriptorVisibilities;
import pb.FunctionDescriptor;
import pb.MemberDescriptor;
import pb.Modality;
import pb.ModuleDescriptor;
import pb.NotFoundClasses;
import pb.PackageFragmentDescriptor;
import pb.PackageViewDescriptor;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.PropertyGetterDescriptor;
import pb.PropertySetterDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeAliasDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import pb.VariableDescriptor;
import pb.g1;
import pb.s0;
import pb.u;
import pb.w;
import qb.AnnotationDescriptor;
import qb.AnnotationUseSiteTarget;
import rc.c;
import sd.CharJVM;
import sd.StringsJVM;
import sd._Strings;
import sd.v;
import uc.q;
import za.Lambda;

/* compiled from: DescriptorRendererImpl.kt */
/* renamed from: rc.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class DescriptorRendererImpl extends rc.c implements rc.f {

    /* renamed from: l, reason: collision with root package name */
    private final DescriptorRendererOptionsImpl f17725l;

    /* renamed from: m, reason: collision with root package name */
    private final ma.h f17726m;

    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$a */
    /* loaded from: classes2.dex */
    private final class a implements DeclarationDescriptorVisitor<Unit, StringBuilder> {

        /* compiled from: DescriptorRendererImpl.kt */
        /* renamed from: rc.d$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public /* synthetic */ class C0098a {

            /* renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f17728a;

            static {
                int[] iArr = new int[l.values().length];
                try {
                    iArr[l.PRETTY.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[l.DEBUG.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[l.NONE.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                f17728a = iArr;
            }
        }

        public a() {
        }

        private final void t(PropertyAccessorDescriptor propertyAccessorDescriptor, StringBuilder sb2, String str) {
            int i10 = C0098a.f17728a[DescriptorRendererImpl.this.l0().ordinal()];
            if (i10 != 1) {
                if (i10 != 2) {
                    return;
                }
                p(propertyAccessorDescriptor, sb2);
                return;
            }
            DescriptorRendererImpl.this.R0(propertyAccessorDescriptor, sb2);
            sb2.append(str + " for ");
            DescriptorRendererImpl descriptorRendererImpl = DescriptorRendererImpl.this;
            PropertyDescriptor K0 = propertyAccessorDescriptor.K0();
            za.k.d(K0, "descriptor.correspondingProperty");
            descriptorRendererImpl.A1(K0, sb2);
        }

        public void A(ValueParameterDescriptor valueParameterDescriptor, StringBuilder sb2) {
            za.k.e(valueParameterDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.S1(valueParameterDescriptor, true, sb2, true);
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit a(PropertySetterDescriptor propertySetterDescriptor, StringBuilder sb2) {
            w(propertySetterDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit b(TypeParameterDescriptor typeParameterDescriptor, StringBuilder sb2) {
            z(typeParameterDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit c(ClassDescriptor classDescriptor, StringBuilder sb2) {
            n(classDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit d(PackageFragmentDescriptor packageFragmentDescriptor, StringBuilder sb2) {
            r(packageFragmentDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit e(ValueParameterDescriptor valueParameterDescriptor, StringBuilder sb2) {
            A(valueParameterDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit f(PropertyDescriptor propertyDescriptor, StringBuilder sb2) {
            u(propertyDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit g(PackageViewDescriptor packageViewDescriptor, StringBuilder sb2) {
            s(packageViewDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit h(FunctionDescriptor functionDescriptor, StringBuilder sb2) {
            p(functionDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit i(TypeAliasDescriptor typeAliasDescriptor, StringBuilder sb2) {
            y(typeAliasDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit j(ModuleDescriptor moduleDescriptor, StringBuilder sb2) {
            q(moduleDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit k(PropertyGetterDescriptor propertyGetterDescriptor, StringBuilder sb2) {
            v(propertyGetterDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit l(ConstructorDescriptor constructorDescriptor, StringBuilder sb2) {
            o(constructorDescriptor, sb2);
            return Unit.f15173a;
        }

        @Override // pb.DeclarationDescriptorVisitor
        public /* bridge */ /* synthetic */ Unit m(ReceiverParameterDescriptor receiverParameterDescriptor, StringBuilder sb2) {
            x(receiverParameterDescriptor, sb2);
            return Unit.f15173a;
        }

        public void n(ClassDescriptor classDescriptor, StringBuilder sb2) {
            za.k.e(classDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.X0(classDescriptor, sb2);
        }

        public void o(ConstructorDescriptor constructorDescriptor, StringBuilder sb2) {
            za.k.e(constructorDescriptor, "constructorDescriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.c1(constructorDescriptor, sb2);
        }

        public void p(FunctionDescriptor functionDescriptor, StringBuilder sb2) {
            za.k.e(functionDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.i1(functionDescriptor, sb2);
        }

        public void q(ModuleDescriptor moduleDescriptor, StringBuilder sb2) {
            za.k.e(moduleDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.s1(moduleDescriptor, sb2, true);
        }

        public void r(PackageFragmentDescriptor packageFragmentDescriptor, StringBuilder sb2) {
            za.k.e(packageFragmentDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.w1(packageFragmentDescriptor, sb2);
        }

        public void s(PackageViewDescriptor packageViewDescriptor, StringBuilder sb2) {
            za.k.e(packageViewDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.y1(packageViewDescriptor, sb2);
        }

        public void u(PropertyDescriptor propertyDescriptor, StringBuilder sb2) {
            za.k.e(propertyDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.A1(propertyDescriptor, sb2);
        }

        public void v(PropertyGetterDescriptor propertyGetterDescriptor, StringBuilder sb2) {
            za.k.e(propertyGetterDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            t(propertyGetterDescriptor, sb2, "getter");
        }

        public void w(PropertySetterDescriptor propertySetterDescriptor, StringBuilder sb2) {
            za.k.e(propertySetterDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            t(propertySetterDescriptor, sb2, "setter");
        }

        public void x(ReceiverParameterDescriptor receiverParameterDescriptor, StringBuilder sb2) {
            za.k.e(receiverParameterDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            sb2.append(receiverParameterDescriptor.getName());
        }

        public void y(TypeAliasDescriptor typeAliasDescriptor, StringBuilder sb2) {
            za.k.e(typeAliasDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.I1(typeAliasDescriptor, sb2);
        }

        public void z(TypeParameterDescriptor typeParameterDescriptor, StringBuilder sb2) {
            za.k.e(typeParameterDescriptor, "descriptor");
            za.k.e(sb2, "builder");
            DescriptorRendererImpl.this.N1(typeParameterDescriptor, sb2, true);
        }
    }

    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$b */
    /* loaded from: classes2.dex */
    public /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f17729a;

        /* renamed from: b, reason: collision with root package name */
        public static final /* synthetic */ int[] f17730b;

        static {
            int[] iArr = new int[m.values().length];
            try {
                iArr[m.PLAIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[m.HTML.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            f17729a = iArr;
            int[] iArr2 = new int[k.values().length];
            try {
                iArr2[k.ALL.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr2[k.ONLY_NON_SYNTHESIZED.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr2[k.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            f17730b = iArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<TypeProjection, CharSequence> {
        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(TypeProjection typeProjection) {
            za.k.e(typeProjection, "it");
            if (typeProjection.b()) {
                return "*";
            }
            DescriptorRendererImpl descriptorRendererImpl = DescriptorRendererImpl.this;
            g0 type = typeProjection.getType();
            za.k.d(type, "it.type");
            String w10 = descriptorRendererImpl.w(type);
            if (typeProjection.a() == Variance.INVARIANT) {
                return w10;
            }
            return typeProjection.a() + ' ' + w10;
        }
    }

    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$d */
    /* loaded from: classes2.dex */
    static final class d extends Lambda implements ya.a<DescriptorRendererImpl> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: DescriptorRendererImpl.kt */
        /* renamed from: rc.d$d$a */
        /* loaded from: classes2.dex */
        public static final class a extends Lambda implements ya.l<rc.f, Unit> {

            /* renamed from: e, reason: collision with root package name */
            public static final a f17733e = new a();

            a() {
                super(1);
            }

            public final void a(rc.f fVar) {
                List m10;
                Set<FqName> k10;
                za.k.e(fVar, "$this$withOptions");
                Set<FqName> l10 = fVar.l();
                m10 = r.m(StandardNames.a.C, StandardNames.a.D);
                k10 = _Sets.k(l10, m10);
                fVar.a(k10);
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(rc.f fVar) {
                a(fVar);
                return Unit.f15173a;
            }
        }

        d() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final DescriptorRendererImpl invoke() {
            rc.c y4 = DescriptorRendererImpl.this.y(a.f17733e);
            za.k.c(y4, "null cannot be cast to non-null type org.jetbrains.kotlin.renderer.DescriptorRendererImpl");
            return (DescriptorRendererImpl) y4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$e */
    /* loaded from: classes2.dex */
    public static final class e extends Lambda implements ya.l<uc.g<?>, CharSequence> {
        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(uc.g<?> gVar) {
            za.k.e(gVar, "it");
            return DescriptorRendererImpl.this.b1(gVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$f */
    /* loaded from: classes2.dex */
    public static final class f extends Lambda implements ya.l<ValueParameterDescriptor, CharSequence> {

        /* renamed from: e, reason: collision with root package name */
        public static final f f17735e = new f();

        f() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(ValueParameterDescriptor valueParameterDescriptor) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$g */
    /* loaded from: classes2.dex */
    public static final class g extends Lambda implements ya.l<g0, CharSequence> {
        g() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CharSequence invoke(g0 g0Var) {
            DescriptorRendererImpl descriptorRendererImpl = DescriptorRendererImpl.this;
            za.k.d(g0Var, "it");
            return descriptorRendererImpl.w(g0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorRendererImpl.kt */
    /* renamed from: rc.d$h */
    /* loaded from: classes2.dex */
    public static final class h extends Lambda implements ya.l<g0, Object> {

        /* renamed from: e, reason: collision with root package name */
        public static final h f17737e = new h();

        h() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Object invoke(g0 g0Var) {
            za.k.e(g0Var, "it");
            return g0Var instanceof w0 ? ((w0) g0Var).f1() : g0Var;
        }
    }

    public DescriptorRendererImpl(DescriptorRendererOptionsImpl descriptorRendererOptionsImpl) {
        ma.h b10;
        za.k.e(descriptorRendererOptionsImpl, "options");
        this.f17725l = descriptorRendererOptionsImpl;
        descriptorRendererOptionsImpl.k0();
        b10 = ma.j.b(new d());
        this.f17726m = b10;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void A1(PropertyDescriptor propertyDescriptor, StringBuilder sb2) {
        if (!y0()) {
            if (!x0()) {
                B1(propertyDescriptor, sb2);
                List<ReceiverParameterDescriptor> w02 = propertyDescriptor.w0();
                za.k.d(w02, "property.contextReceiverParameters");
                d1(w02, sb2);
                u g6 = propertyDescriptor.g();
                za.k.d(g6, "property.visibility");
                V1(g6, sb2);
                boolean z10 = false;
                r1(sb2, e0().contains(rc.e.CONST) && propertyDescriptor.I(), "const");
                n1(propertyDescriptor, sb2);
                q1(propertyDescriptor, sb2);
                v1(propertyDescriptor, sb2);
                if (e0().contains(rc.e.LATEINIT) && propertyDescriptor.x0()) {
                    z10 = true;
                }
                r1(sb2, z10, "lateinit");
                m1(propertyDescriptor, sb2);
            }
            R1(this, propertyDescriptor, sb2, false, 4, null);
            List<TypeParameterDescriptor> m10 = propertyDescriptor.m();
            za.k.d(m10, "property.typeParameters");
            P1(m10, sb2, true);
            C1(propertyDescriptor, sb2);
        }
        s1(propertyDescriptor, sb2, true);
        sb2.append(": ");
        g0 type = propertyDescriptor.getType();
        za.k.d(type, "property.type");
        sb2.append(w(type));
        D1(propertyDescriptor, sb2);
        k1(propertyDescriptor, sb2);
        List<TypeParameterDescriptor> m11 = propertyDescriptor.m();
        za.k.d(m11, "property.typeParameters");
        W1(m11, sb2);
    }

    private final void B1(PropertyDescriptor propertyDescriptor, StringBuilder sb2) {
        Object q02;
        if (e0().contains(rc.e.ANNOTATIONS)) {
            V0(this, sb2, propertyDescriptor, null, 2, null);
            w v02 = propertyDescriptor.v0();
            if (v02 != null) {
                U0(sb2, v02, AnnotationUseSiteTarget.FIELD);
            }
            w s02 = propertyDescriptor.s0();
            if (s02 != null) {
                U0(sb2, s02, AnnotationUseSiteTarget.PROPERTY_DELEGATE_FIELD);
            }
            if (l0() == l.NONE) {
                PropertyGetterDescriptor h10 = propertyDescriptor.h();
                if (h10 != null) {
                    U0(sb2, h10, AnnotationUseSiteTarget.PROPERTY_GETTER);
                }
                PropertySetterDescriptor k10 = propertyDescriptor.k();
                if (k10 != null) {
                    U0(sb2, k10, AnnotationUseSiteTarget.PROPERTY_SETTER);
                    List<ValueParameterDescriptor> l10 = k10.l();
                    za.k.d(l10, "setter.valueParameters");
                    q02 = _Collections.q0(l10);
                    ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) q02;
                    za.k.d(valueParameterDescriptor, "it");
                    U0(sb2, valueParameterDescriptor, AnnotationUseSiteTarget.SETTER_PARAMETER);
                }
            }
        }
    }

    private final void C1(CallableDescriptor callableDescriptor, StringBuilder sb2) {
        ReceiverParameterDescriptor r02 = callableDescriptor.r0();
        if (r02 != null) {
            U0(sb2, r02, AnnotationUseSiteTarget.RECEIVER);
            g0 type = r02.getType();
            za.k.d(type, "receiver.type");
            sb2.append(g1(type));
            sb2.append(".");
        }
    }

    private final void D1(CallableDescriptor callableDescriptor, StringBuilder sb2) {
        ReceiverParameterDescriptor r02;
        if (m0() && (r02 = callableDescriptor.r0()) != null) {
            sb2.append(" on ");
            g0 type = r02.getType();
            za.k.d(type, "receiver.type");
            sb2.append(w(type));
        }
    }

    private final void E1(StringBuilder sb2, o0 o0Var) {
        if (!za.k.a(o0Var, s1.f11884b) && !s1.k(o0Var)) {
            if (ErrorUtils.o(o0Var)) {
                if (B0()) {
                    TypeConstructor W0 = o0Var.W0();
                    za.k.c(W0, "null cannot be cast to non-null type org.jetbrains.kotlin.types.error.ErrorTypeConstructor");
                    sb2.append(f1(((ErrorTypeConstructor) W0).d(0)));
                    return;
                }
                sb2.append("???");
                return;
            }
            if (i0.a(o0Var)) {
                e1(sb2, o0Var);
                return;
            } else if (X1(o0Var)) {
                j1(sb2, o0Var);
                return;
            } else {
                e1(sb2, o0Var);
                return;
            }
        }
        sb2.append("???");
    }

    private final void F1(StringBuilder sb2) {
        int length = sb2.length();
        if (length == 0 || sb2.charAt(length - 1) != ' ') {
            sb2.append(' ');
        }
    }

    private final void G1(ClassDescriptor classDescriptor, StringBuilder sb2) {
        if (I0() || KotlinBuiltIns.m0(classDescriptor.x())) {
            return;
        }
        Collection<g0> q10 = classDescriptor.n().q();
        za.k.d(q10, "klass.typeConstructor.supertypes");
        if (q10.isEmpty()) {
            return;
        }
        if (q10.size() == 1 && KotlinBuiltIns.b0(q10.iterator().next())) {
            return;
        }
        F1(sb2);
        sb2.append(": ");
        _Collections.a0(q10, sb2, ", ", null, null, 0, null, new g(), 60, null);
    }

    private final void H1(FunctionDescriptor functionDescriptor, StringBuilder sb2) {
        r1(sb2, functionDescriptor.C0(), "suspend");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void I1(TypeAliasDescriptor typeAliasDescriptor, StringBuilder sb2) {
        V0(this, sb2, typeAliasDescriptor, null, 2, null);
        u g6 = typeAliasDescriptor.g();
        za.k.d(g6, "typeAlias.visibility");
        V1(g6, sb2);
        n1(typeAliasDescriptor, sb2);
        sb2.append(l1("typealias"));
        sb2.append(" ");
        s1(typeAliasDescriptor, sb2, true);
        List<TypeParameterDescriptor> B = typeAliasDescriptor.B();
        za.k.d(B, "typeAlias.declaredTypeParameters");
        P1(B, sb2, false);
        W0(typeAliasDescriptor, sb2);
        sb2.append(" = ");
        sb2.append(w(typeAliasDescriptor.n0()));
    }

    private final String K0() {
        return O(">");
    }

    private final void L(StringBuilder sb2, DeclarationDescriptor declarationDescriptor) {
        DeclarationDescriptor b10;
        String name;
        if ((declarationDescriptor instanceof PackageFragmentDescriptor) || (declarationDescriptor instanceof PackageViewDescriptor) || (b10 = declarationDescriptor.b()) == null || (b10 instanceof ModuleDescriptor)) {
            return;
        }
        sb2.append(" ");
        sb2.append(o1("defined in"));
        sb2.append(" ");
        FqNameUnsafe m10 = sc.e.m(b10);
        za.k.d(m10, "getFqName(containingDeclaration)");
        sb2.append(m10.e() ? "root package" : u(m10));
        if (G0() && (b10 instanceof PackageFragmentDescriptor) && (declarationDescriptor instanceof DeclarationDescriptorWithSource) && (name = ((DeclarationDescriptorWithSource) declarationDescriptor).z().a().getName()) != null) {
            sb2.append(" ");
            sb2.append(o1("in file"));
            sb2.append(" ");
            sb2.append(name);
        }
    }

    private final boolean L0(g0 g0Var) {
        return functionTypes.q(g0Var) || !g0Var.i().isEmpty();
    }

    private final void L1(StringBuilder sb2, g0 g0Var, TypeConstructor typeConstructor) {
        s0 a10 = g1.a(g0Var);
        if (a10 == null) {
            sb2.append(K1(typeConstructor));
            sb2.append(J1(g0Var.U0()));
        } else {
            z1(sb2, a10);
        }
    }

    private final void M(StringBuilder sb2, List<? extends TypeProjection> list) {
        _Collections.a0(list, sb2, ", ", null, null, 0, null, new c(), 60, null);
    }

    private final Modality M0(MemberDescriptor memberDescriptor) {
        if (memberDescriptor instanceof ClassDescriptor) {
            return ((ClassDescriptor) memberDescriptor).getKind() == ClassKind.INTERFACE ? Modality.ABSTRACT : Modality.FINAL;
        }
        DeclarationDescriptor b10 = memberDescriptor.b();
        ClassDescriptor classDescriptor = b10 instanceof ClassDescriptor ? (ClassDescriptor) b10 : null;
        if (classDescriptor != null && (memberDescriptor instanceof CallableMemberDescriptor)) {
            CallableMemberDescriptor callableMemberDescriptor = (CallableMemberDescriptor) memberDescriptor;
            za.k.d(callableMemberDescriptor.e(), "this.overriddenDescriptors");
            if ((!r0.isEmpty()) && classDescriptor.o() != Modality.FINAL) {
                return Modality.OPEN;
            }
            if (classDescriptor.getKind() == ClassKind.INTERFACE && !za.k.a(callableMemberDescriptor.g(), DescriptorVisibilities.f16729a)) {
                Modality o10 = callableMemberDescriptor.o();
                Modality modality = Modality.ABSTRACT;
                return o10 == modality ? modality : Modality.OPEN;
            }
            return Modality.FINAL;
        }
        return Modality.FINAL;
    }

    static /* synthetic */ void M1(DescriptorRendererImpl descriptorRendererImpl, StringBuilder sb2, g0 g0Var, TypeConstructor typeConstructor, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            typeConstructor = g0Var.W0();
        }
        descriptorRendererImpl.L1(sb2, g0Var, typeConstructor);
    }

    private final String N() {
        int i10 = b.f17729a[z0().ordinal()];
        if (i10 == 1) {
            return O("->");
        }
        if (i10 == 2) {
            return "&rarr;";
        }
        throw new NoWhenBranchMatchedException();
    }

    private final boolean N0(AnnotationDescriptor annotationDescriptor) {
        return za.k.a(annotationDescriptor.d(), StandardNames.a.E);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void N1(TypeParameterDescriptor typeParameterDescriptor, StringBuilder sb2, boolean z10) {
        if (z10) {
            sb2.append(O0());
        }
        if (E0()) {
            sb2.append("/*");
            sb2.append(typeParameterDescriptor.j());
            sb2.append("*/ ");
        }
        r1(sb2, typeParameterDescriptor.N(), "reified");
        String c10 = typeParameterDescriptor.s().c();
        boolean z11 = true;
        r1(sb2, c10.length() > 0, c10);
        V0(this, sb2, typeParameterDescriptor, null, 2, null);
        s1(typeParameterDescriptor, sb2, z10);
        int size = typeParameterDescriptor.getUpperBounds().size();
        if ((size > 1 && !z10) || size == 1) {
            g0 next = typeParameterDescriptor.getUpperBounds().iterator().next();
            if (!KotlinBuiltIns.i0(next)) {
                sb2.append(" : ");
                za.k.d(next, "upperBound");
                sb2.append(w(next));
            }
        } else if (z10) {
            for (g0 g0Var : typeParameterDescriptor.getUpperBounds()) {
                if (!KotlinBuiltIns.i0(g0Var)) {
                    if (z11) {
                        sb2.append(" : ");
                    } else {
                        sb2.append(" & ");
                    }
                    za.k.d(g0Var, "upperBound");
                    sb2.append(w(g0Var));
                    z11 = false;
                }
            }
        }
        if (z10) {
            sb2.append(K0());
        }
    }

    private final String O(String str) {
        return z0().b(str);
    }

    private final String O0() {
        return O("<");
    }

    private final void O1(StringBuilder sb2, List<? extends TypeParameterDescriptor> list) {
        Iterator<? extends TypeParameterDescriptor> it = list.iterator();
        while (it.hasNext()) {
            N1(it.next(), sb2, false);
            if (it.hasNext()) {
                sb2.append(", ");
            }
        }
    }

    private final boolean P0(CallableMemberDescriptor callableMemberDescriptor) {
        return !callableMemberDescriptor.e().isEmpty();
    }

    private final void P1(List<? extends TypeParameterDescriptor> list, StringBuilder sb2, boolean z10) {
        if (!J0() && (!list.isEmpty())) {
            sb2.append(O0());
            O1(sb2, list);
            sb2.append(K0());
            if (z10) {
                sb2.append(" ");
            }
        }
    }

    private final void Q0(StringBuilder sb2, gd.a aVar) {
        m z02 = z0();
        m mVar = m.HTML;
        if (z02 == mVar) {
            sb2.append("<font color=\"808080\"><i>");
        }
        sb2.append(" /* = ");
        u1(sb2, aVar.e0());
        sb2.append(" */");
        if (z0() == mVar) {
            sb2.append("</i></font>");
        }
    }

    private final void Q1(VariableDescriptor variableDescriptor, StringBuilder sb2, boolean z10) {
        if (z10 || !(variableDescriptor instanceof ValueParameterDescriptor)) {
            sb2.append(l1(variableDescriptor.p0() ? "var" : "val"));
            sb2.append(" ");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void R0(PropertyAccessorDescriptor propertyAccessorDescriptor, StringBuilder sb2) {
        n1(propertyAccessorDescriptor, sb2);
    }

    static /* synthetic */ void R1(DescriptorRendererImpl descriptorRendererImpl, VariableDescriptor variableDescriptor, StringBuilder sb2, boolean z10, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            z10 = false;
        }
        descriptorRendererImpl.Q1(variableDescriptor, sb2, z10);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0041  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void S0(FunctionDescriptor functionDescriptor, StringBuilder sb2) {
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13 = false;
        if (functionDescriptor.Y()) {
            Collection<? extends FunctionDescriptor> e10 = functionDescriptor.e();
            za.k.d(e10, "functionDescriptor.overriddenDescriptors");
            if (!e10.isEmpty()) {
                Iterator<T> it = e10.iterator();
                while (it.hasNext()) {
                    if (((FunctionDescriptor) it.next()).Y()) {
                        z12 = false;
                        break;
                    }
                }
            }
            z12 = true;
            if (z12 || Q()) {
                z10 = true;
                if (functionDescriptor.Q0()) {
                    Collection<? extends FunctionDescriptor> e11 = functionDescriptor.e();
                    za.k.d(e11, "functionDescriptor.overriddenDescriptors");
                    if (!e11.isEmpty()) {
                        Iterator<T> it2 = e11.iterator();
                        while (it2.hasNext()) {
                            if (((FunctionDescriptor) it2.next()).Q0()) {
                                z11 = false;
                                break;
                            }
                        }
                    }
                    z11 = true;
                    if (z11 || Q()) {
                        z13 = true;
                    }
                }
                r1(sb2, functionDescriptor.X(), "tailrec");
                H1(functionDescriptor, sb2);
                r1(sb2, functionDescriptor.y(), "inline");
                r1(sb2, z13, "infix");
                r1(sb2, z10, "operator");
            }
        }
        z10 = false;
        if (functionDescriptor.Q0()) {
        }
        r1(sb2, functionDescriptor.X(), "tailrec");
        H1(functionDescriptor, sb2);
        r1(sb2, functionDescriptor.y(), "inline");
        r1(sb2, z13, "infix");
        r1(sb2, z10, "operator");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x008e, code lost:
    
        if ((m() ? r10.z0() : wc.c.c(r10)) != false) goto L32;
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void S1(ValueParameterDescriptor valueParameterDescriptor, boolean z10, StringBuilder sb2, boolean z11) {
        boolean z12;
        if (z11) {
            sb2.append(l1("value-parameter"));
            sb2.append(" ");
        }
        if (E0()) {
            sb2.append("/*");
            sb2.append(valueParameterDescriptor.j());
            sb2.append("*/ ");
        }
        V0(this, sb2, valueParameterDescriptor, null, 2, null);
        r1(sb2, valueParameterDescriptor.i0(), "crossinline");
        r1(sb2, valueParameterDescriptor.g0(), "noinline");
        boolean z13 = true;
        if (t0()) {
            CallableDescriptor b10 = valueParameterDescriptor.b();
            ClassConstructorDescriptor classConstructorDescriptor = b10 instanceof ClassConstructorDescriptor ? (ClassConstructorDescriptor) b10 : null;
            if (classConstructorDescriptor != null && classConstructorDescriptor.J()) {
                z12 = true;
                if (z12) {
                    r1(sb2, P(), "actual");
                }
                U1(valueParameterDescriptor, z10, sb2, z11, z12);
                if (V() != null) {
                }
                z13 = false;
                if (z13) {
                    return;
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" = ");
                ya.l<ValueParameterDescriptor, String> V = V();
                za.k.b(V);
                sb3.append(V.invoke(valueParameterDescriptor));
                sb2.append(sb3.toString());
                return;
            }
        }
        z12 = false;
        if (z12) {
        }
        U1(valueParameterDescriptor, z10, sb2, z11, z12);
        if (V() != null) {
        }
        z13 = false;
        if (z13) {
        }
    }

    private final List<String> T0(AnnotationDescriptor annotationDescriptor) {
        int u7;
        int u10;
        List m02;
        List<String> t02;
        ClassConstructorDescriptor Z;
        List<ValueParameterDescriptor> l10;
        int u11;
        Map<Name, uc.g<?>> a10 = annotationDescriptor.a();
        List list = null;
        ClassDescriptor i10 = q0() ? wc.c.i(annotationDescriptor) : null;
        if (i10 != null && (Z = i10.Z()) != null && (l10 = Z.l()) != null) {
            ArrayList arrayList = new ArrayList();
            for (Object obj : l10) {
                if (((ValueParameterDescriptor) obj).z0()) {
                    arrayList.add(obj);
                }
            }
            u11 = s.u(arrayList, 10);
            ArrayList arrayList2 = new ArrayList(u11);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(((ValueParameterDescriptor) it.next()).getName());
            }
            list = arrayList2;
        }
        if (list == null) {
            list = r.j();
        }
        ArrayList arrayList3 = new ArrayList();
        for (Object obj2 : list) {
            za.k.d((Name) obj2, "it");
            if (!a10.containsKey(r5)) {
                arrayList3.add(obj2);
            }
        }
        u7 = s.u(arrayList3, 10);
        ArrayList arrayList4 = new ArrayList(u7);
        Iterator it2 = arrayList3.iterator();
        while (it2.hasNext()) {
            arrayList4.add(((Name) it2.next()).b() + " = ...");
        }
        Set<Map.Entry<Name, uc.g<?>>> entrySet = a10.entrySet();
        u10 = s.u(entrySet, 10);
        ArrayList arrayList5 = new ArrayList(u10);
        Iterator<T> it3 = entrySet.iterator();
        while (it3.hasNext()) {
            Map.Entry entry = (Map.Entry) it3.next();
            Name name = (Name) entry.getKey();
            uc.g<?> gVar = (uc.g) entry.getValue();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(name.b());
            sb2.append(" = ");
            sb2.append(!list.contains(name) ? b1(gVar) : "...");
            arrayList5.add(sb2.toString());
        }
        m02 = _Collections.m0(arrayList4, arrayList5);
        t02 = _Collections.t0(m02);
        return t02;
    }

    private final void T1(Collection<? extends ValueParameterDescriptor> collection, boolean z10, StringBuilder sb2) {
        boolean Y1 = Y1(z10);
        int size = collection.size();
        D0().b(size, sb2);
        int i10 = 0;
        for (ValueParameterDescriptor valueParameterDescriptor : collection) {
            D0().a(valueParameterDescriptor, i10, size, sb2);
            S1(valueParameterDescriptor, Y1, sb2, false);
            D0().d(valueParameterDescriptor, i10, size, sb2);
            i10++;
        }
        D0().c(size, sb2);
    }

    private final void U0(StringBuilder sb2, qb.a aVar, AnnotationUseSiteTarget annotationUseSiteTarget) {
        boolean L;
        if (e0().contains(rc.e.ANNOTATIONS)) {
            Set<FqName> l10 = aVar instanceof g0 ? l() : X();
            ya.l<AnnotationDescriptor, Boolean> R = R();
            for (AnnotationDescriptor annotationDescriptor : aVar.i()) {
                L = _Collections.L(l10, annotationDescriptor.d());
                if (!L && !N0(annotationDescriptor) && (R == null || R.invoke(annotationDescriptor).booleanValue())) {
                    sb2.append(r(annotationDescriptor, annotationUseSiteTarget));
                    if (W()) {
                        sb2.append('\n');
                        za.k.d(sb2, "append('\\n')");
                    } else {
                        sb2.append(" ");
                    }
                }
            }
        }
    }

    private final void U1(VariableDescriptor variableDescriptor, boolean z10, StringBuilder sb2, boolean z11, boolean z12) {
        g0 type = variableDescriptor.getType();
        za.k.d(type, "variable.type");
        ValueParameterDescriptor valueParameterDescriptor = variableDescriptor instanceof ValueParameterDescriptor ? (ValueParameterDescriptor) variableDescriptor : null;
        g0 q02 = valueParameterDescriptor != null ? valueParameterDescriptor.q0() : null;
        g0 g0Var = q02 == null ? type : q02;
        r1(sb2, q02 != null, "vararg");
        if (z12 || (z11 && !y0())) {
            Q1(variableDescriptor, sb2, z12);
        }
        if (z10) {
            s1(variableDescriptor, sb2, z11);
            sb2.append(": ");
        }
        sb2.append(w(g0Var));
        k1(variableDescriptor, sb2);
        if (!E0() || q02 == null) {
            return;
        }
        sb2.append(" /*");
        sb2.append(w(type));
        sb2.append("*/");
    }

    static /* synthetic */ void V0(DescriptorRendererImpl descriptorRendererImpl, StringBuilder sb2, qb.a aVar, AnnotationUseSiteTarget annotationUseSiteTarget, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            annotationUseSiteTarget = null;
        }
        descriptorRendererImpl.U0(sb2, aVar, annotationUseSiteTarget);
    }

    private final boolean V1(u uVar, StringBuilder sb2) {
        if (!e0().contains(rc.e.VISIBILITY)) {
            return false;
        }
        if (f0()) {
            uVar = uVar.f();
        }
        if (!s0() && za.k.a(uVar, DescriptorVisibilities.f16740l)) {
            return false;
        }
        sb2.append(l1(uVar.c()));
        sb2.append(" ");
        return true;
    }

    private final void W0(ClassifierDescriptorWithTypeParameters classifierDescriptorWithTypeParameters, StringBuilder sb2) {
        List<TypeParameterDescriptor> B = classifierDescriptorWithTypeParameters.B();
        za.k.d(B, "classifier.declaredTypeParameters");
        List<TypeParameterDescriptor> parameters = classifierDescriptorWithTypeParameters.n().getParameters();
        za.k.d(parameters, "classifier.typeConstructor.parameters");
        if (E0() && classifierDescriptorWithTypeParameters.r() && parameters.size() > B.size()) {
            sb2.append(" /*captured type parameters: ");
            O1(sb2, parameters.subList(B.size(), parameters.size()));
            sb2.append("*/");
        }
    }

    private final void W1(List<? extends TypeParameterDescriptor> list, StringBuilder sb2) {
        List<g0> N;
        if (J0()) {
            return;
        }
        ArrayList arrayList = new ArrayList(0);
        for (TypeParameterDescriptor typeParameterDescriptor : list) {
            List<g0> upperBounds = typeParameterDescriptor.getUpperBounds();
            za.k.d(upperBounds, "typeParameter.upperBounds");
            N = _Collections.N(upperBounds, 1);
            for (g0 g0Var : N) {
                StringBuilder sb3 = new StringBuilder();
                Name name = typeParameterDescriptor.getName();
                za.k.d(name, "typeParameter.name");
                sb3.append(v(name, false));
                sb3.append(" : ");
                za.k.d(g0Var, "it");
                sb3.append(w(g0Var));
                arrayList.add(sb3.toString());
            }
        }
        if (!arrayList.isEmpty()) {
            sb2.append(" ");
            sb2.append(l1("where"));
            sb2.append(" ");
            _Collections.a0(arrayList, sb2, ", ", null, null, 0, null, null, 124, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void X0(ClassDescriptor classDescriptor, StringBuilder sb2) {
        ClassConstructorDescriptor Z;
        boolean z10 = classDescriptor.getKind() == ClassKind.ENUM_ENTRY;
        if (!y0()) {
            V0(this, sb2, classDescriptor, null, 2, null);
            List<ReceiverParameterDescriptor> P0 = classDescriptor.P0();
            za.k.d(P0, "klass.contextReceivers");
            d1(P0, sb2);
            if (!z10) {
                u g6 = classDescriptor.g();
                za.k.d(g6, "klass.visibility");
                V1(g6, sb2);
            }
            if ((classDescriptor.getKind() != ClassKind.INTERFACE || classDescriptor.o() != Modality.ABSTRACT) && (!classDescriptor.getKind().b() || classDescriptor.o() != Modality.FINAL)) {
                Modality o10 = classDescriptor.o();
                za.k.d(o10, "klass.modality");
                p1(o10, sb2, M0(classDescriptor));
            }
            n1(classDescriptor, sb2);
            r1(sb2, e0().contains(rc.e.INNER) && classDescriptor.r(), "inner");
            r1(sb2, e0().contains(rc.e.DATA) && classDescriptor.R0(), "data");
            r1(sb2, e0().contains(rc.e.INLINE) && classDescriptor.y(), "inline");
            r1(sb2, e0().contains(rc.e.VALUE) && classDescriptor.q(), ThermalBaseConfig.Item.ATTR_VALUE);
            r1(sb2, e0().contains(rc.e.FUN) && classDescriptor.L(), "fun");
            Y0(classDescriptor, sb2);
        }
        if (!sc.e.x(classDescriptor)) {
            if (!y0()) {
                F1(sb2);
            }
            s1(classDescriptor, sb2, true);
        } else {
            a1(classDescriptor, sb2);
        }
        if (z10) {
            return;
        }
        List<TypeParameterDescriptor> B = classDescriptor.B();
        za.k.d(B, "klass.declaredTypeParameters");
        P1(B, sb2, false);
        W0(classDescriptor, sb2);
        if (!classDescriptor.getKind().b() && T() && (Z = classDescriptor.Z()) != null) {
            sb2.append(" ");
            V0(this, sb2, Z, null, 2, null);
            u g10 = Z.g();
            za.k.d(g10, "primaryConstructor.visibility");
            V1(g10, sb2);
            sb2.append(l1("constructor"));
            List<ValueParameterDescriptor> l10 = Z.l();
            za.k.d(l10, "primaryConstructor.valueParameters");
            T1(l10, Z.O(), sb2);
        }
        G1(classDescriptor, sb2);
        W1(B, sb2);
    }

    private final boolean X1(g0 g0Var) {
        boolean z10;
        if (!functionTypes.o(g0Var)) {
            return false;
        }
        List<TypeProjection> U0 = g0Var.U0();
        if (!(U0 instanceof Collection) || !U0.isEmpty()) {
            Iterator<T> it = U0.iterator();
            while (it.hasNext()) {
                if (((TypeProjection) it.next()).b()) {
                    z10 = false;
                    break;
                }
            }
        }
        z10 = true;
        return z10;
    }

    private final DescriptorRendererImpl Y() {
        return (DescriptorRendererImpl) this.f17726m.getValue();
    }

    private final void Y0(ClassDescriptor classDescriptor, StringBuilder sb2) {
        sb2.append(l1(rc.c.f17702a.a(classDescriptor)));
    }

    private final boolean Y1(boolean z10) {
        int i10 = b.f17730b[i0().ordinal()];
        if (i10 != 1) {
            if (i10 != 2) {
                if (i10 == 3) {
                    return false;
                }
                throw new NoWhenBranchMatchedException();
            }
            if (z10) {
                return false;
            }
        }
        return true;
    }

    private final void a1(DeclarationDescriptor declarationDescriptor, StringBuilder sb2) {
        if (n0()) {
            if (y0()) {
                sb2.append("companion object");
            }
            F1(sb2);
            DeclarationDescriptor b10 = declarationDescriptor.b();
            if (b10 != null) {
                sb2.append("of ");
                Name name = b10.getName();
                za.k.d(name, "containingDeclaration.name");
                sb2.append(v(name, false));
            }
        }
        if (E0() || !za.k.a(declarationDescriptor.getName(), SpecialNames.f16449d)) {
            if (!y0()) {
                F1(sb2);
            }
            Name name2 = declarationDescriptor.getName();
            za.k.d(name2, "descriptor.name");
            sb2.append(v(name2, true));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String b1(uc.g<?> gVar) {
        String j02;
        String c02;
        if (gVar instanceof uc.b) {
            c02 = _Collections.c0(((uc.b) gVar).b(), ", ", "{", "}", 0, null, new e(), 24, null);
            return c02;
        }
        if (gVar instanceof uc.a) {
            j02 = v.j0(rc.c.s(this, ((uc.a) gVar).b(), null, 2, null), "@");
            return j02;
        }
        if (gVar instanceof q) {
            q.b b10 = ((q) gVar).b();
            if (b10 instanceof q.b.a) {
                return ((q.b.a) b10).a() + "::class";
            }
            if (!(b10 instanceof q.b.C0110b)) {
                throw new NoWhenBranchMatchedException();
            }
            q.b.C0110b c0110b = (q.b.C0110b) b10;
            String b11 = c0110b.b().b().b();
            za.k.d(b11, "classValue.classId.asSingleFqName().asString()");
            for (int i10 = 0; i10 < c0110b.a(); i10++) {
                b11 = "kotlin.Array<" + b11 + '>';
            }
            return b11 + "::class";
        }
        return gVar.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void c1(ConstructorDescriptor constructorDescriptor, StringBuilder sb2) {
        boolean z10;
        boolean z11;
        ClassConstructorDescriptor Z;
        ArrayList arrayList;
        String c02;
        V0(this, sb2, constructorDescriptor, null, 2, null);
        if (this.f17725l.S() || constructorDescriptor.K().o() != Modality.SEALED) {
            u g6 = constructorDescriptor.g();
            za.k.d(g6, "constructor.visibility");
            if (V1(g6, sb2)) {
                z10 = true;
                m1(constructorDescriptor, sb2);
                z11 = (p0() && constructorDescriptor.J() && !z10) ? false : true;
                if (z11) {
                    sb2.append(l1("constructor"));
                }
                ClassifierDescriptorWithTypeParameters b10 = constructorDescriptor.b();
                za.k.d(b10, "constructor.containingDeclaration");
                if (w0()) {
                    if (z11) {
                        sb2.append(" ");
                    }
                    s1(b10, sb2, true);
                    List<TypeParameterDescriptor> m10 = constructorDescriptor.m();
                    za.k.d(m10, "constructor.typeParameters");
                    P1(m10, sb2, false);
                }
                List<ValueParameterDescriptor> l10 = constructorDescriptor.l();
                za.k.d(l10, "constructor.valueParameters");
                T1(l10, constructorDescriptor.O(), sb2);
                if (o0() && !constructorDescriptor.J() && (b10 instanceof ClassDescriptor) && (Z = ((ClassDescriptor) b10).Z()) != null) {
                    List<ValueParameterDescriptor> l11 = Z.l();
                    za.k.d(l11, "primaryConstructor.valueParameters");
                    arrayList = new ArrayList();
                    for (Object obj : l11) {
                        ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) obj;
                        if (!valueParameterDescriptor.z0() && valueParameterDescriptor.q0() == null) {
                            arrayList.add(obj);
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        sb2.append(" : ");
                        sb2.append(l1("this"));
                        c02 = _Collections.c0(arrayList, ", ", "(", ")", 0, null, f.f17735e, 24, null);
                        sb2.append(c02);
                    }
                }
                if (w0()) {
                    return;
                }
                List<TypeParameterDescriptor> m11 = constructorDescriptor.m();
                za.k.d(m11, "constructor.typeParameters");
                W1(m11, sb2);
                return;
            }
        }
        z10 = false;
        m1(constructorDescriptor, sb2);
        if (p0()) {
        }
        if (z11) {
        }
        ClassifierDescriptorWithTypeParameters b102 = constructorDescriptor.b();
        za.k.d(b102, "constructor.containingDeclaration");
        if (w0()) {
        }
        List<ValueParameterDescriptor> l102 = constructorDescriptor.l();
        za.k.d(l102, "constructor.valueParameters");
        T1(l102, constructorDescriptor.O(), sb2);
        if (o0()) {
            List<ValueParameterDescriptor> l112 = Z.l();
            za.k.d(l112, "primaryConstructor.valueParameters");
            arrayList = new ArrayList();
            while (r0.hasNext()) {
            }
            if (!arrayList.isEmpty()) {
            }
        }
        if (w0()) {
        }
    }

    private final void d1(List<? extends ReceiverParameterDescriptor> list, StringBuilder sb2) {
        int l10;
        if (!list.isEmpty()) {
            sb2.append("context(");
            int i10 = 0;
            for (ReceiverParameterDescriptor receiverParameterDescriptor : list) {
                int i11 = i10 + 1;
                U0(sb2, receiverParameterDescriptor, AnnotationUseSiteTarget.RECEIVER);
                g0 type = receiverParameterDescriptor.getType();
                za.k.d(type, "contextReceiver.type");
                sb2.append(g1(type));
                l10 = r.l(list);
                if (i10 == l10) {
                    sb2.append(") ");
                } else {
                    sb2.append(", ");
                }
                i10 = i11;
            }
        }
    }

    private final void e1(StringBuilder sb2, g0 g0Var) {
        V0(this, sb2, g0Var, null, 2, null);
        p pVar = g0Var instanceof p ? (p) g0Var : null;
        o0 i12 = pVar != null ? pVar.i1() : null;
        if (i0.a(g0Var)) {
            if (ld.a.s(g0Var) && k0()) {
                sb2.append(f1(ErrorUtils.f12833a.p(g0Var)));
            } else {
                if ((g0Var instanceof ErrorType) && !d0()) {
                    sb2.append(((ErrorType) g0Var).f1());
                } else {
                    sb2.append(g0Var.W0().toString());
                }
                sb2.append(J1(g0Var.U0()));
            }
        } else if (g0Var instanceof w0) {
            sb2.append(((w0) g0Var).f1().toString());
        } else if (i12 instanceof w0) {
            sb2.append(((w0) i12).f1().toString());
        } else {
            M1(this, sb2, g0Var, null, 2, null);
        }
        if (g0Var.X0()) {
            sb2.append("?");
        }
        if (gd.s0.c(g0Var)) {
            sb2.append(" & Any");
        }
    }

    private final String f1(String str) {
        int i10 = b.f17729a[z0().ordinal()];
        if (i10 == 1) {
            return str;
        }
        if (i10 != 2) {
            throw new NoWhenBranchMatchedException();
        }
        return "<font color=red><b>" + str + "</b></font>";
    }

    private final String g1(g0 g0Var) {
        String w10 = w(g0Var);
        if (!X1(g0Var) || s1.l(g0Var)) {
            return w10;
        }
        return '(' + w10 + ')';
    }

    private final String h1(List<Name> list) {
        return O(RenderingUtils.c(list));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void i1(FunctionDescriptor functionDescriptor, StringBuilder sb2) {
        if (!y0()) {
            if (!x0()) {
                V0(this, sb2, functionDescriptor, null, 2, null);
                List<ReceiverParameterDescriptor> w02 = functionDescriptor.w0();
                za.k.d(w02, "function.contextReceiverParameters");
                d1(w02, sb2);
                u g6 = functionDescriptor.g();
                za.k.d(g6, "function.visibility");
                V1(g6, sb2);
                q1(functionDescriptor, sb2);
                if (Z()) {
                    n1(functionDescriptor, sb2);
                }
                v1(functionDescriptor, sb2);
                if (Z()) {
                    S0(functionDescriptor, sb2);
                } else {
                    H1(functionDescriptor, sb2);
                }
                m1(functionDescriptor, sb2);
                if (E0()) {
                    if (functionDescriptor.B0()) {
                        sb2.append("/*isHiddenToOvercomeSignatureClash*/ ");
                    }
                    if (functionDescriptor.L0()) {
                        sb2.append("/*isHiddenForResolutionEverywhereBesideSupercalls*/ ");
                    }
                }
            }
            sb2.append(l1("fun"));
            sb2.append(" ");
            List<TypeParameterDescriptor> m10 = functionDescriptor.m();
            za.k.d(m10, "function.typeParameters");
            P1(m10, sb2, true);
            C1(functionDescriptor, sb2);
        }
        s1(functionDescriptor, sb2, true);
        List<ValueParameterDescriptor> l10 = functionDescriptor.l();
        za.k.d(l10, "function.valueParameters");
        T1(l10, functionDescriptor.O(), sb2);
        D1(functionDescriptor, sb2);
        g0 f10 = functionDescriptor.f();
        if (!H0() && (C0() || f10 == null || !KotlinBuiltIns.B0(f10))) {
            sb2.append(": ");
            sb2.append(f10 == null ? "[NULL]" : w(f10));
        }
        List<TypeParameterDescriptor> m11 = functionDescriptor.m();
        za.k.d(m11, "function.typeParameters");
        W1(m11, sb2);
    }

    private final void j1(StringBuilder sb2, g0 g0Var) {
        Name name;
        char L0;
        int P;
        int P2;
        int l10;
        Object e02;
        int length = sb2.length();
        V0(Y(), sb2, g0Var, null, 2, null);
        boolean z10 = sb2.length() != length;
        g0 j10 = functionTypes.j(g0Var);
        List<g0> e10 = functionTypes.e(g0Var);
        if (!e10.isEmpty()) {
            sb2.append("context(");
            l10 = r.l(e10);
            Iterator<g0> it = e10.subList(0, l10).iterator();
            while (it.hasNext()) {
                t1(sb2, it.next());
                sb2.append(", ");
            }
            e02 = _Collections.e0(e10);
            t1(sb2, (g0) e02);
            sb2.append(") ");
        }
        boolean q10 = functionTypes.q(g0Var);
        boolean X0 = g0Var.X0();
        boolean z11 = X0 || (z10 && j10 != null);
        if (z11) {
            if (q10) {
                sb2.insert(length, '(');
            } else {
                if (z10) {
                    L0 = _Strings.L0(sb2);
                    CharJVM.c(L0);
                    P = v.P(sb2);
                    if (sb2.charAt(P - 1) != ')') {
                        P2 = v.P(sb2);
                        sb2.insert(P2, "()");
                    }
                }
                sb2.append("(");
            }
        }
        r1(sb2, q10, "suspend");
        if (j10 != null) {
            boolean z12 = (X1(j10) && !j10.X0()) || L0(j10);
            if (z12) {
                sb2.append("(");
            }
            t1(sb2, j10);
            if (z12) {
                sb2.append(")");
            }
            sb2.append(".");
        }
        sb2.append("(");
        if (functionTypes.m(g0Var) && g0Var.U0().size() <= 1) {
            sb2.append("???");
        } else {
            int i10 = 0;
            for (TypeProjection typeProjection : functionTypes.l(g0Var)) {
                int i11 = i10 + 1;
                if (i10 > 0) {
                    sb2.append(", ");
                }
                if (j0()) {
                    g0 type = typeProjection.getType();
                    za.k.d(type, "typeProjection.type");
                    name = functionTypes.d(type);
                } else {
                    name = null;
                }
                if (name != null) {
                    sb2.append(v(name, false));
                    sb2.append(": ");
                }
                sb2.append(x(typeProjection));
                i10 = i11;
            }
        }
        sb2.append(") ");
        sb2.append(N());
        sb2.append(" ");
        t1(sb2, functionTypes.k(g0Var));
        if (z11) {
            sb2.append(")");
        }
        if (X0) {
            sb2.append("?");
        }
    }

    private final void k1(VariableDescriptor variableDescriptor, StringBuilder sb2) {
        uc.g<?> f02;
        if (!c0() || (f02 = variableDescriptor.f0()) == null) {
            return;
        }
        sb2.append(" = ");
        sb2.append(O(b1(f02)));
    }

    private final String l1(String str) {
        int i10 = b.f17729a[z0().ordinal()];
        if (i10 == 1) {
            return str;
        }
        if (i10 != 2) {
            throw new NoWhenBranchMatchedException();
        }
        if (S()) {
            return str;
        }
        return "<b>" + str + "</b>";
    }

    private final void m1(CallableMemberDescriptor callableMemberDescriptor, StringBuilder sb2) {
        if (e0().contains(rc.e.MEMBER_KIND) && E0() && callableMemberDescriptor.getKind() != CallableMemberDescriptor.a.DECLARATION) {
            sb2.append("/*");
            sb2.append(capitalizeDecapitalize.f(callableMemberDescriptor.getKind().name()));
            sb2.append("*/ ");
        }
    }

    private final void n1(MemberDescriptor memberDescriptor, StringBuilder sb2) {
        r1(sb2, memberDescriptor.D(), "external");
        r1(sb2, e0().contains(rc.e.EXPECT) && memberDescriptor.U(), "expect");
        r1(sb2, e0().contains(rc.e.ACTUAL) && memberDescriptor.N0(), "actual");
    }

    private final void p1(Modality modality, StringBuilder sb2, Modality modality2) {
        if (r0() || modality != modality2) {
            r1(sb2, e0().contains(rc.e.MODALITY), capitalizeDecapitalize.f(modality.name()));
        }
    }

    private final void q1(CallableMemberDescriptor callableMemberDescriptor, StringBuilder sb2) {
        if (sc.e.J(callableMemberDescriptor) && callableMemberDescriptor.o() == Modality.FINAL) {
            return;
        }
        if (h0() == j.RENDER_OVERRIDE && callableMemberDescriptor.o() == Modality.OPEN && P0(callableMemberDescriptor)) {
            return;
        }
        Modality o10 = callableMemberDescriptor.o();
        za.k.d(o10, "callable.modality");
        p1(o10, sb2, M0(callableMemberDescriptor));
    }

    private final void r1(StringBuilder sb2, boolean z10, String str) {
        if (z10) {
            sb2.append(l1(str));
            sb2.append(" ");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void s1(DeclarationDescriptor declarationDescriptor, StringBuilder sb2, boolean z10) {
        Name name = declarationDescriptor.getName();
        za.k.d(name, "descriptor.name");
        sb2.append(v(name, z10));
    }

    private final void t1(StringBuilder sb2, g0 g0Var) {
        v1 Z0 = g0Var.Z0();
        gd.a aVar = Z0 instanceof gd.a ? (gd.a) Z0 : null;
        if (aVar != null) {
            if (u0()) {
                u1(sb2, aVar.e0());
                return;
            }
            u1(sb2, aVar.i1());
            if (v0()) {
                Q0(sb2, aVar);
                return;
            }
            return;
        }
        u1(sb2, g0Var);
    }

    private final void u1(StringBuilder sb2, g0 g0Var) {
        if ((g0Var instanceof x1) && m() && !((x1) g0Var).b1()) {
            sb2.append("<Not computed yet>");
            return;
        }
        v1 Z0 = g0Var.Z0();
        if (Z0 instanceof a0) {
            sb2.append(((a0) Z0).g1(this, this));
        } else if (Z0 instanceof o0) {
            E1(sb2, (o0) Z0);
        }
    }

    private final void v1(CallableMemberDescriptor callableMemberDescriptor, StringBuilder sb2) {
        if (e0().contains(rc.e.OVERRIDE) && P0(callableMemberDescriptor) && h0() != j.RENDER_OPEN) {
            r1(sb2, true, "override");
            if (E0()) {
                sb2.append("/*");
                sb2.append(callableMemberDescriptor.e().size());
                sb2.append("*/ ");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void w1(PackageFragmentDescriptor packageFragmentDescriptor, StringBuilder sb2) {
        x1(packageFragmentDescriptor.d(), "package-fragment", sb2);
        if (m()) {
            sb2.append(" in ");
            s1(packageFragmentDescriptor.b(), sb2, false);
        }
    }

    private final void x1(FqName fqName, String str, StringBuilder sb2) {
        sb2.append(l1(str));
        FqNameUnsafe j10 = fqName.j();
        za.k.d(j10, "fqName.toUnsafe()");
        String u7 = u(j10);
        if (u7.length() > 0) {
            sb2.append(" ");
            sb2.append(u7);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void y1(PackageViewDescriptor packageViewDescriptor, StringBuilder sb2) {
        x1(packageViewDescriptor.d(), "package", sb2);
        if (m()) {
            sb2.append(" in context of ");
            s1(packageViewDescriptor.A0(), sb2, false);
        }
    }

    private final void z1(StringBuilder sb2, s0 s0Var) {
        s0 c10 = s0Var.c();
        if (c10 != null) {
            z1(sb2, c10);
            sb2.append('.');
            Name name = s0Var.b().getName();
            za.k.d(name, "possiblyInnerType.classifierDescriptor.name");
            sb2.append(v(name, false));
        } else {
            TypeConstructor n10 = s0Var.b().n();
            za.k.d(n10, "possiblyInnerType.classi…escriptor.typeConstructor");
            sb2.append(K1(n10));
        }
        sb2.append(J1(s0Var.a()));
    }

    public ya.l<g0, g0> A0() {
        return this.f17725l.a0();
    }

    public boolean B0() {
        return this.f17725l.b0();
    }

    public boolean C0() {
        return this.f17725l.c0();
    }

    public c.l D0() {
        return this.f17725l.d0();
    }

    public boolean E0() {
        return this.f17725l.e0();
    }

    public boolean F0() {
        return this.f17725l.f0();
    }

    public boolean G0() {
        return this.f17725l.g0();
    }

    public boolean H0() {
        return this.f17725l.h0();
    }

    public boolean I0() {
        return this.f17725l.i0();
    }

    public boolean J0() {
        return this.f17725l.j0();
    }

    public String J1(List<? extends TypeProjection> list) {
        za.k.e(list, "typeArguments");
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(O0());
        M(sb2, list);
        sb2.append(K0());
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public String K1(TypeConstructor typeConstructor) {
        za.k.e(typeConstructor, "typeConstructor");
        ClassifierDescriptor v7 = typeConstructor.v();
        if (v7 instanceof TypeParameterDescriptor ? true : v7 instanceof ClassDescriptor ? true : v7 instanceof TypeAliasDescriptor) {
            return Z0(v7);
        }
        if (v7 == null) {
            if (typeConstructor instanceof IntersectionTypeConstructor) {
                return ((IntersectionTypeConstructor) typeConstructor).f(h.f17737e);
            }
            return typeConstructor.toString();
        }
        throw new IllegalStateException(("Unexpected classifier: " + v7.getClass()).toString());
    }

    public boolean P() {
        return this.f17725l.r();
    }

    public boolean Q() {
        return this.f17725l.s();
    }

    public ya.l<AnnotationDescriptor, Boolean> R() {
        return this.f17725l.t();
    }

    public boolean S() {
        return this.f17725l.u();
    }

    public boolean T() {
        return this.f17725l.v();
    }

    public ClassifierNamePolicy U() {
        return this.f17725l.w();
    }

    public ya.l<ValueParameterDescriptor, String> V() {
        return this.f17725l.x();
    }

    public boolean W() {
        return this.f17725l.y();
    }

    public Set<FqName> X() {
        return this.f17725l.z();
    }

    public boolean Z() {
        return this.f17725l.A();
    }

    public String Z0(ClassifierDescriptor classifierDescriptor) {
        za.k.e(classifierDescriptor, "klass");
        if (ErrorUtils.m(classifierDescriptor)) {
            return classifierDescriptor.n().toString();
        }
        return U().a(classifierDescriptor, this);
    }

    @Override // rc.f
    public void a(Set<FqName> set) {
        za.k.e(set, "<set-?>");
        this.f17725l.a(set);
    }

    public boolean a0() {
        return this.f17725l.B();
    }

    @Override // rc.f
    public void b(boolean z10) {
        this.f17725l.b(z10);
    }

    public boolean b0() {
        return this.f17725l.C();
    }

    @Override // rc.f
    public void c(Set<? extends rc.e> set) {
        za.k.e(set, "<set-?>");
        this.f17725l.c(set);
    }

    public boolean c0() {
        return this.f17725l.D();
    }

    @Override // rc.f
    public void d(m mVar) {
        za.k.e(mVar, "<set-?>");
        this.f17725l.d(mVar);
    }

    public boolean d0() {
        return this.f17725l.E();
    }

    @Override // rc.f
    public void e(boolean z10) {
        this.f17725l.e(z10);
    }

    public Set<rc.e> e0() {
        return this.f17725l.F();
    }

    @Override // rc.f
    public boolean f() {
        return this.f17725l.f();
    }

    public boolean f0() {
        return this.f17725l.G();
    }

    @Override // rc.f
    public void g(boolean z10) {
        this.f17725l.g(z10);
    }

    public final DescriptorRendererOptionsImpl g0() {
        return this.f17725l;
    }

    @Override // rc.f
    public void h(boolean z10) {
        this.f17725l.h(z10);
    }

    public j h0() {
        return this.f17725l.H();
    }

    @Override // rc.f
    public void i(boolean z10) {
        this.f17725l.i(z10);
    }

    public k i0() {
        return this.f17725l.I();
    }

    @Override // rc.f
    public void j(boolean z10) {
        this.f17725l.j(z10);
    }

    public boolean j0() {
        return this.f17725l.J();
    }

    @Override // rc.f
    public void k(k kVar) {
        za.k.e(kVar, "<set-?>");
        this.f17725l.k(kVar);
    }

    public boolean k0() {
        return this.f17725l.K();
    }

    @Override // rc.f
    public Set<FqName> l() {
        return this.f17725l.l();
    }

    public l l0() {
        return this.f17725l.L();
    }

    @Override // rc.f
    public boolean m() {
        return this.f17725l.m();
    }

    public boolean m0() {
        return this.f17725l.M();
    }

    @Override // rc.f
    public rc.a n() {
        return this.f17725l.n();
    }

    public boolean n0() {
        return this.f17725l.N();
    }

    @Override // rc.f
    public void o(ClassifierNamePolicy classifierNamePolicy) {
        za.k.e(classifierNamePolicy, "<set-?>");
        this.f17725l.o(classifierNamePolicy);
    }

    public boolean o0() {
        return this.f17725l.O();
    }

    public String o1(String str) {
        za.k.e(str, "message");
        int i10 = b.f17729a[z0().ordinal()];
        if (i10 == 1) {
            return str;
        }
        if (i10 != 2) {
            throw new NoWhenBranchMatchedException();
        }
        return "<i>" + str + "</i>";
    }

    @Override // rc.f
    public void p(boolean z10) {
        this.f17725l.p(z10);
    }

    public boolean p0() {
        return this.f17725l.P();
    }

    @Override // rc.c
    public String q(DeclarationDescriptor declarationDescriptor) {
        za.k.e(declarationDescriptor, "declarationDescriptor");
        StringBuilder sb2 = new StringBuilder();
        declarationDescriptor.H0(new a(), sb2);
        if (F0()) {
            L(sb2, declarationDescriptor);
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public boolean q0() {
        return this.f17725l.Q();
    }

    @Override // rc.c
    public String r(AnnotationDescriptor annotationDescriptor, AnnotationUseSiteTarget annotationUseSiteTarget) {
        za.k.e(annotationDescriptor, "annotation");
        StringBuilder sb2 = new StringBuilder();
        sb2.append('@');
        if (annotationUseSiteTarget != null) {
            sb2.append(annotationUseSiteTarget.b() + COUIAccessibilityUtil.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        }
        g0 type = annotationDescriptor.getType();
        sb2.append(w(type));
        if (a0()) {
            List<String> T0 = T0(annotationDescriptor);
            if (b0() || (!T0.isEmpty())) {
                _Collections.a0(T0, sb2, ", ", "(", ")", 0, null, null, 112, null);
            }
        }
        if (E0() && (i0.a(type) || (type.W0().v() instanceof NotFoundClasses.b))) {
            sb2.append(" /* annotation class not found */");
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public boolean r0() {
        return this.f17725l.R();
    }

    public boolean s0() {
        return this.f17725l.S();
    }

    @Override // rc.c
    public String t(String str, String str2, KotlinBuiltIns kotlinBuiltIns) {
        String H0;
        String H02;
        boolean D;
        za.k.e(str, "lowerRendered");
        za.k.e(str2, "upperRendered");
        za.k.e(kotlinBuiltIns, "builtIns");
        if (RenderingUtils.f(str, str2)) {
            D = StringsJVM.D(str2, "(", false, 2, null);
            if (D) {
                return '(' + str + ")!";
            }
            return str + '!';
        }
        ClassifierNamePolicy U = U();
        ClassDescriptor w10 = kotlinBuiltIns.w();
        za.k.d(w10, "builtIns.collection");
        H0 = v.H0(U.a(w10, this), "Collection", null, 2, null);
        String d10 = RenderingUtils.d(str, H0 + "Mutable", str2, H0, H0 + "(Mutable)");
        if (d10 != null) {
            return d10;
        }
        String d11 = RenderingUtils.d(str, H0 + "MutableMap.MutableEntry", str2, H0 + "Map.Entry", H0 + "(Mutable)Map.(Mutable)Entry");
        if (d11 != null) {
            return d11;
        }
        ClassifierNamePolicy U2 = U();
        ClassDescriptor j10 = kotlinBuiltIns.j();
        za.k.d(j10, "builtIns.array");
        H02 = v.H0(U2.a(j10, this), "Array", null, 2, null);
        String d12 = RenderingUtils.d(str, H02 + O("Array<"), str2, H02 + O("Array<out "), H02 + O("Array<(out) "));
        if (d12 != null) {
            return d12;
        }
        return '(' + str + ".." + str2 + ')';
    }

    public boolean t0() {
        return this.f17725l.T();
    }

    @Override // rc.c
    public String u(FqNameUnsafe fqNameUnsafe) {
        za.k.e(fqNameUnsafe, "fqName");
        List<Name> h10 = fqNameUnsafe.h();
        za.k.d(h10, "fqName.pathSegments()");
        return h1(h10);
    }

    public boolean u0() {
        return this.f17725l.U();
    }

    @Override // rc.c
    public String v(Name name, boolean z10) {
        za.k.e(name, "name");
        String O = O(RenderingUtils.b(name));
        if (!S() || z0() != m.HTML || !z10) {
            return O;
        }
        return "<b>" + O + "</b>";
    }

    public boolean v0() {
        return this.f17725l.V();
    }

    @Override // rc.c
    public String w(g0 g0Var) {
        za.k.e(g0Var, "type");
        StringBuilder sb2 = new StringBuilder();
        t1(sb2, A0().invoke(g0Var));
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public boolean w0() {
        return this.f17725l.W();
    }

    @Override // rc.c
    public String x(TypeProjection typeProjection) {
        List<? extends TypeProjection> e10;
        za.k.e(typeProjection, "typeProjection");
        StringBuilder sb2 = new StringBuilder();
        e10 = CollectionsJVM.e(typeProjection);
        M(sb2, e10);
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public boolean x0() {
        return this.f17725l.X();
    }

    public boolean y0() {
        return this.f17725l.Y();
    }

    public m z0() {
        return this.f17725l.Z();
    }
}
