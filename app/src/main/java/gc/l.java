package gc;

import ac.JavaCallableMemberDescriptor;
import ac.JavaMethodDescriptor;
import ac.JavaPropertyDescriptor;
import cc.JavaDescriptorUtil;
import cc.LazyJavaAnnotationDescriptor;
import cc.LazyJavaClassDescriptor;
import gd.g0;
import gd.n0;
import gd.s1;
import gd.v1;
import hc.SignatureBuildingComponents;
import hc.methodSignatureBuildingUtils;
import hc.w;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import ma.u;
import ob.JavaToKotlinClassMap;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import qb.AnnotationDescriptor;
import qb.g;
import sb.PropertyGetterDescriptorImpl;
import yb.AnnotationQualifierApplicabilityType;
import yb.j0;
import za.Lambda;

/* compiled from: signatureEnhancement.kt */
/* loaded from: classes2.dex */
public final class l {

    /* renamed from: a, reason: collision with root package name */
    private final gc.d f11713a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: signatureEnhancement.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<v1, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f11714e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(v1 v1Var) {
            ClassifierDescriptor v7 = v1Var.W0().v();
            if (v7 == null) {
                return Boolean.FALSE;
            }
            Name name = v7.getName();
            JavaToKotlinClassMap javaToKotlinClassMap = JavaToKotlinClassMap.f16339a;
            return Boolean.valueOf(za.k.a(name, javaToKotlinClassMap.h().g()) && za.k.a(wc.c.h(v7), javaToKotlinClassMap.h()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: signatureEnhancement.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<CallableMemberDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f11715e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            ReceiverParameterDescriptor r02 = callableMemberDescriptor.r0();
            za.k.b(r02);
            g0 type = r02.getType();
            za.k.d(type, "it.extensionReceiverParameter!!.type");
            return type;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: signatureEnhancement.kt */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.l<CallableMemberDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f11716e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            g0 f10 = callableMemberDescriptor.f();
            za.k.b(f10);
            return f10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: signatureEnhancement.kt */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.l<CallableMemberDescriptor, g0> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ValueParameterDescriptor f11717e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(ValueParameterDescriptor valueParameterDescriptor) {
            super(1);
            this.f11717e = valueParameterDescriptor;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final g0 invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            g0 type = callableMemberDescriptor.l().get(this.f11717e.j()).getType();
            za.k.d(type, "it.valueParameters[p.index].type");
            return type;
        }
    }

    /* compiled from: signatureEnhancement.kt */
    /* loaded from: classes2.dex */
    static final class e extends Lambda implements ya.l<v1, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f11718e = new e();

        e() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(v1 v1Var) {
            za.k.e(v1Var, "it");
            return Boolean.valueOf(v1Var instanceof n0);
        }
    }

    public l(gc.d dVar) {
        za.k.e(dVar, "typeEnhancement");
        this.f11713a = dVar;
    }

    private final boolean a(g0 g0Var) {
        return s1.c(g0Var, a.f11714e);
    }

    private final g0 b(n nVar, g0 g0Var, List<? extends g0> list, q qVar, boolean z10) {
        return this.f11713a.a(g0Var, nVar.b(g0Var, list, qVar, z10), nVar.u());
    }

    private final g0 c(CallableMemberDescriptor callableMemberDescriptor, qb.a aVar, boolean z10, bc.g gVar, AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType, q qVar, boolean z11, ya.l<? super CallableMemberDescriptor, ? extends g0> lVar) {
        int u7;
        n nVar = new n(aVar, z10, gVar, annotationQualifierApplicabilityType, false, 16, null);
        g0 invoke = lVar.invoke(callableMemberDescriptor);
        Collection<? extends CallableMemberDescriptor> e10 = callableMemberDescriptor.e();
        za.k.d(e10, "overriddenDescriptors");
        u7 = kotlin.collections.s.u(e10, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (CallableMemberDescriptor callableMemberDescriptor2 : e10) {
            za.k.d(callableMemberDescriptor2, "it");
            arrayList.add(lVar.invoke(callableMemberDescriptor2));
        }
        return b(nVar, invoke, arrayList, qVar, z11);
    }

    static /* synthetic */ g0 d(l lVar, n nVar, g0 g0Var, List list, q qVar, boolean z10, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            qVar = null;
        }
        q qVar2 = qVar;
        if ((i10 & 8) != 0) {
            z10 = false;
        }
        return lVar.b(nVar, g0Var, list, qVar2, z10);
    }

    static /* synthetic */ g0 e(l lVar, CallableMemberDescriptor callableMemberDescriptor, qb.a aVar, boolean z10, bc.g gVar, AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType, q qVar, boolean z11, ya.l lVar2, int i10, Object obj) {
        return lVar.c(callableMemberDescriptor, aVar, z10, gVar, annotationQualifierApplicabilityType, qVar, (i10 & 32) != 0 ? false : z11, lVar2);
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x0217  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x015e  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01ee A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01f8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final <D extends CallableMemberDescriptor> D f(D d10, bc.g gVar) {
        D d11;
        g0 g0Var;
        JavaMethodDescriptor javaMethodDescriptor;
        k kVar;
        int u7;
        PropertyDescriptor propertyDescriptor;
        AnnotationQualifierApplicabilityType annotationQualifierApplicabilityType;
        g0 e10;
        g0 f10;
        boolean z10;
        g0 g0Var2;
        int u10;
        boolean z11;
        g0 type;
        q qVar;
        List<q> a10;
        Object W;
        if (!(d10 instanceof JavaCallableMemberDescriptor)) {
            return d10;
        }
        JavaCallableMemberDescriptor javaCallableMemberDescriptor = (JavaCallableMemberDescriptor) d10;
        boolean z12 = true;
        if (javaCallableMemberDescriptor.getKind() == CallableMemberDescriptor.a.FAKE_OVERRIDE && javaCallableMemberDescriptor.T0().e().size() == 1) {
            return d10;
        }
        bc.g h10 = bc.a.h(gVar, k(d10, gVar));
        if (d10 instanceof JavaPropertyDescriptor) {
            JavaPropertyDescriptor javaPropertyDescriptor = (JavaPropertyDescriptor) d10;
            PropertyGetterDescriptorImpl h11 = javaPropertyDescriptor.h();
            if ((h11 == null || h11.d0()) ? false : true) {
                PropertyGetterDescriptorImpl h12 = javaPropertyDescriptor.h();
                za.k.b(h12);
                d11 = h12;
                if (javaCallableMemberDescriptor.r0() == null) {
                    FunctionDescriptor functionDescriptor = (FunctionDescriptor) (!(d11 instanceof FunctionDescriptor) ? null : d11);
                    g0Var = j(d10, functionDescriptor != null ? (ValueParameterDescriptor) functionDescriptor.E(JavaMethodDescriptor.K) : null, h10, null, false, b.f11715e);
                } else {
                    g0Var = null;
                }
                javaMethodDescriptor = !(d10 instanceof JavaMethodDescriptor) ? (JavaMethodDescriptor) d10 : null;
                if (javaMethodDescriptor != null) {
                    SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
                    DeclarationDescriptor b10 = javaMethodDescriptor.b();
                    za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
                    String a11 = methodSignatureBuildingUtils.a(signatureBuildingComponents, (ClassDescriptor) b10, w.c(javaMethodDescriptor, false, false, 3, null));
                    if (a11 != null) {
                        kVar = j.d().get(a11);
                        if (kVar != null) {
                            kVar.a().size();
                            javaCallableMemberDescriptor.l().size();
                        }
                        boolean z13 = (!j0.c(gVar.a().i()) || h10.a().q().b()) && j0.b(d10);
                        List<ValueParameterDescriptor> l10 = d11.l();
                        za.k.d(l10, "annotationOwnerForMember.valueParameters");
                        char c10 = '\n';
                        u7 = kotlin.collections.s.u(l10, 10);
                        ArrayList arrayList = new ArrayList(u7);
                        for (ValueParameterDescriptor valueParameterDescriptor : l10) {
                            if (kVar == null || (a10 = kVar.a()) == null) {
                                qVar = null;
                            } else {
                                W = _Collections.W(a10, valueParameterDescriptor.j());
                                qVar = (q) W;
                            }
                            ArrayList arrayList2 = arrayList;
                            arrayList2.add(j(d10, valueParameterDescriptor, h10, qVar, z13, new d(valueParameterDescriptor)));
                            arrayList = arrayList2;
                            c10 = '\n';
                        }
                        ArrayList arrayList3 = arrayList;
                        propertyDescriptor = (PropertyDescriptor) (!(d10 instanceof PropertyDescriptor) ? null : d10);
                        if (propertyDescriptor == null && JavaDescriptorUtil.a(propertyDescriptor)) {
                            annotationQualifierApplicabilityType = AnnotationQualifierApplicabilityType.FIELD;
                        } else {
                            annotationQualifierApplicabilityType = AnnotationQualifierApplicabilityType.METHOD_RETURN_TYPE;
                        }
                        e10 = e(this, d10, d11, true, h10, annotationQualifierApplicabilityType, kVar != null ? kVar.b() : null, false, c.f11716e, 32, null);
                        f10 = javaCallableMemberDescriptor.f();
                        za.k.b(f10);
                        if (!a(f10)) {
                            ReceiverParameterDescriptor r02 = javaCallableMemberDescriptor.r0();
                            if (!((r02 == null || (type = r02.getType()) == null) ? false : a(type))) {
                                List<ValueParameterDescriptor> l11 = javaCallableMemberDescriptor.l();
                                za.k.d(l11, "valueParameters");
                                if (!(l11 instanceof Collection) || !l11.isEmpty()) {
                                    Iterator<T> it = l11.iterator();
                                    while (it.hasNext()) {
                                        g0 type2 = ((ValueParameterDescriptor) it.next()).getType();
                                        za.k.d(type2, "it.type");
                                        if (a(type2)) {
                                            z11 = true;
                                            break;
                                        }
                                    }
                                }
                                z11 = false;
                                if (!z11) {
                                    z10 = false;
                                    ma.o<CallableDescriptor.a<?>, ?> a12 = !z10 ? u.a(vc.d.a(), new yb.j(d10)) : null;
                                    if (g0Var == null && e10 == null) {
                                        if (!arrayList3.isEmpty()) {
                                            Iterator it2 = arrayList3.iterator();
                                            while (it2.hasNext()) {
                                                if (((g0) it2.next()) != null) {
                                                    break;
                                                }
                                            }
                                        }
                                        z12 = false;
                                        if (!z12 && a12 == null) {
                                            return d10;
                                        }
                                    }
                                    if (g0Var != null) {
                                        ReceiverParameterDescriptor r03 = javaCallableMemberDescriptor.r0();
                                        g0Var2 = r03 != null ? r03.getType() : null;
                                    } else {
                                        g0Var2 = g0Var;
                                    }
                                    u10 = kotlin.collections.s.u(arrayList3, 10);
                                    ArrayList arrayList4 = new ArrayList(u10);
                                    int i10 = 0;
                                    for (Object obj : arrayList3) {
                                        int i11 = i10 + 1;
                                        if (i10 < 0) {
                                            kotlin.collections.r.t();
                                        }
                                        g0 g0Var3 = (g0) obj;
                                        if (g0Var3 == null) {
                                            g0Var3 = javaCallableMemberDescriptor.l().get(i10).getType();
                                            za.k.d(g0Var3, "valueParameters[index].type");
                                        }
                                        arrayList4.add(g0Var3);
                                        i10 = i11;
                                    }
                                    if (e10 == null) {
                                        e10 = javaCallableMemberDescriptor.f();
                                        za.k.b(e10);
                                    }
                                    JavaCallableMemberDescriptor G = javaCallableMemberDescriptor.G(g0Var2, arrayList4, e10, a12);
                                    za.k.c(G, "null cannot be cast to non-null type D of org.jetbrains.kotlin.load.java.typeEnhancement.SignatureEnhancement.enhanceSignature");
                                    return G;
                                }
                            }
                        }
                        z10 = true;
                        if (!z10) {
                        }
                        if (g0Var == null) {
                            if (!arrayList3.isEmpty()) {
                            }
                            z12 = false;
                            if (!z12) {
                                return d10;
                            }
                        }
                        if (g0Var != null) {
                        }
                        u10 = kotlin.collections.s.u(arrayList3, 10);
                        ArrayList arrayList42 = new ArrayList(u10);
                        int i102 = 0;
                        while (r3.hasNext()) {
                        }
                        if (e10 == null) {
                        }
                        JavaCallableMemberDescriptor G2 = javaCallableMemberDescriptor.G(g0Var2, arrayList42, e10, a12);
                        za.k.c(G2, "null cannot be cast to non-null type D of org.jetbrains.kotlin.load.java.typeEnhancement.SignatureEnhancement.enhanceSignature");
                        return G2;
                    }
                }
                kVar = null;
                if (kVar != null) {
                }
                if (j0.c(gVar.a().i())) {
                }
                List<ValueParameterDescriptor> l102 = d11.l();
                za.k.d(l102, "annotationOwnerForMember.valueParameters");
                char c102 = '\n';
                u7 = kotlin.collections.s.u(l102, 10);
                ArrayList arrayList5 = new ArrayList(u7);
                while (r18.hasNext()) {
                }
                ArrayList arrayList32 = arrayList5;
                propertyDescriptor = (PropertyDescriptor) (!(d10 instanceof PropertyDescriptor) ? null : d10);
                if (propertyDescriptor == null && JavaDescriptorUtil.a(propertyDescriptor)) {
                }
                e10 = e(this, d10, d11, true, h10, annotationQualifierApplicabilityType, kVar != null ? kVar.b() : null, false, c.f11716e, 32, null);
                f10 = javaCallableMemberDescriptor.f();
                za.k.b(f10);
                if (!a(f10)) {
                }
                z10 = true;
                if (!z10) {
                }
                if (g0Var == null) {
                }
                if (g0Var != null) {
                }
                u10 = kotlin.collections.s.u(arrayList32, 10);
                ArrayList arrayList422 = new ArrayList(u10);
                int i1022 = 0;
                while (r3.hasNext()) {
                }
                if (e10 == null) {
                }
                JavaCallableMemberDescriptor G22 = javaCallableMemberDescriptor.G(g0Var2, arrayList422, e10, a12);
                za.k.c(G22, "null cannot be cast to non-null type D of org.jetbrains.kotlin.load.java.typeEnhancement.SignatureEnhancement.enhanceSignature");
                return G22;
            }
        }
        d11 = d10;
        if (javaCallableMemberDescriptor.r0() == null) {
        }
        if (!(d10 instanceof JavaMethodDescriptor)) {
        }
        if (javaMethodDescriptor != null) {
        }
        kVar = null;
        if (kVar != null) {
        }
        if (j0.c(gVar.a().i())) {
        }
        List<ValueParameterDescriptor> l1022 = d11.l();
        za.k.d(l1022, "annotationOwnerForMember.valueParameters");
        char c1022 = '\n';
        u7 = kotlin.collections.s.u(l1022, 10);
        ArrayList arrayList52 = new ArrayList(u7);
        while (r18.hasNext()) {
        }
        ArrayList arrayList322 = arrayList52;
        propertyDescriptor = (PropertyDescriptor) (!(d10 instanceof PropertyDescriptor) ? null : d10);
        if (propertyDescriptor == null && JavaDescriptorUtil.a(propertyDescriptor)) {
        }
        e10 = e(this, d10, d11, true, h10, annotationQualifierApplicabilityType, kVar != null ? kVar.b() : null, false, c.f11716e, 32, null);
        f10 = javaCallableMemberDescriptor.f();
        za.k.b(f10);
        if (!a(f10)) {
        }
        z10 = true;
        if (!z10) {
        }
        if (g0Var == null) {
        }
        if (g0Var != null) {
        }
        u10 = kotlin.collections.s.u(arrayList322, 10);
        ArrayList arrayList4222 = new ArrayList(u10);
        int i10222 = 0;
        while (r3.hasNext()) {
        }
        if (e10 == null) {
        }
        JavaCallableMemberDescriptor G222 = javaCallableMemberDescriptor.G(g0Var2, arrayList4222, e10, a12);
        za.k.c(G222, "null cannot be cast to non-null type D of org.jetbrains.kotlin.load.java.typeEnhancement.SignatureEnhancement.enhanceSignature");
        return G222;
    }

    private final g0 j(CallableMemberDescriptor callableMemberDescriptor, ValueParameterDescriptor valueParameterDescriptor, bc.g gVar, q qVar, boolean z10, ya.l<? super CallableMemberDescriptor, ? extends g0> lVar) {
        bc.g h10;
        return c(callableMemberDescriptor, valueParameterDescriptor, false, (valueParameterDescriptor == null || (h10 = bc.a.h(gVar, valueParameterDescriptor.i())) == null) ? gVar : h10, AnnotationQualifierApplicabilityType.VALUE_PARAMETER, qVar, z10, lVar);
    }

    private final <D extends CallableMemberDescriptor> qb.g k(D d10, bc.g gVar) {
        int u7;
        List<? extends AnnotationDescriptor> k02;
        ClassifierDescriptor a10 = pb.s.a(d10);
        if (a10 == null) {
            return d10.i();
        }
        LazyJavaClassDescriptor lazyJavaClassDescriptor = a10 instanceof LazyJavaClassDescriptor ? (LazyJavaClassDescriptor) a10 : null;
        List<fc.a> Z0 = lazyJavaClassDescriptor != null ? lazyJavaClassDescriptor.Z0() : null;
        if (Z0 == null || Z0.isEmpty()) {
            return d10.i();
        }
        u7 = kotlin.collections.s.u(Z0, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = Z0.iterator();
        while (it.hasNext()) {
            arrayList.add(new LazyJavaAnnotationDescriptor(gVar, (fc.a) it.next(), true));
        }
        g.a aVar = qb.g.f17195b;
        k02 = _Collections.k0(d10.i(), arrayList);
        return aVar.a(k02);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <D extends CallableMemberDescriptor> Collection<D> g(bc.g gVar, Collection<? extends D> collection) {
        int u7;
        za.k.e(gVar, "c");
        za.k.e(collection, "platformSignatures");
        u7 = kotlin.collections.s.u(collection, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            arrayList.add(f((CallableMemberDescriptor) it.next(), gVar));
        }
        return arrayList;
    }

    public final g0 h(g0 g0Var, bc.g gVar) {
        List j10;
        za.k.e(g0Var, "type");
        za.k.e(gVar, "context");
        n nVar = new n(null, false, gVar, AnnotationQualifierApplicabilityType.TYPE_USE, true);
        j10 = kotlin.collections.r.j();
        g0 d10 = d(this, nVar, g0Var, j10, null, false, 12, null);
        return d10 == null ? g0Var : d10;
    }

    public final List<g0> i(TypeParameterDescriptor typeParameterDescriptor, List<? extends g0> list, bc.g gVar) {
        int u7;
        List j10;
        za.k.e(typeParameterDescriptor, "typeParameter");
        za.k.e(list, "bounds");
        za.k.e(gVar, "context");
        u7 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList = new ArrayList(u7);
        for (g0 g0Var : list) {
            if (!ld.a.b(g0Var, e.f11718e)) {
                n nVar = new n(typeParameterDescriptor, false, gVar, AnnotationQualifierApplicabilityType.TYPE_PARAMETER_BOUNDS, false, 16, null);
                j10 = kotlin.collections.r.j();
                g0 d10 = d(this, nVar, g0Var, j10, null, false, 12, null);
                if (d10 != null) {
                    g0Var = d10;
                }
            }
            arrayList.add(g0Var);
        }
        return arrayList;
    }
}
