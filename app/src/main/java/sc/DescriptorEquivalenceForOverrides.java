package sc;

import gd.TypeConstructor;
import hd.g;
import java.util.Collection;
import kotlin.collections._Collections;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.MemberDescriptor;
import pb.PackageFragmentDescriptor;
import pb.SourceElement;
import pb.TypeParameterDescriptor;
import sc.OverridingUtil;
import za.Lambda;

/* compiled from: DescriptorEquivalenceForOverrides.kt */
/* renamed from: sc.c, reason: use source file name */
/* loaded from: classes2.dex */
public final class DescriptorEquivalenceForOverrides {

    /* renamed from: a, reason: collision with root package name */
    public static final DescriptorEquivalenceForOverrides f18421a = new DescriptorEquivalenceForOverrides();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorEquivalenceForOverrides.kt */
    /* renamed from: sc.c$a */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.p<DeclarationDescriptor, DeclarationDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f18422e = new a();

        a() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
            return Boolean.FALSE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorEquivalenceForOverrides.kt */
    /* renamed from: sc.c$b */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.p<DeclarationDescriptor, DeclarationDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ CallableDescriptor f18423e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ CallableDescriptor f18424f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
            super(2);
            this.f18423e = callableDescriptor;
            this.f18424f = callableDescriptor2;
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
            return Boolean.valueOf(za.k.a(declarationDescriptor, this.f18423e) && za.k.a(declarationDescriptor2, this.f18424f));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DescriptorEquivalenceForOverrides.kt */
    /* renamed from: sc.c$c */
    /* loaded from: classes2.dex */
    public static final class c extends Lambda implements ya.p<DeclarationDescriptor, DeclarationDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f18425e = new c();

        c() {
            super(2);
        }

        @Override // ya.p
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
            return Boolean.FALSE;
        }
    }

    private DescriptorEquivalenceForOverrides() {
    }

    public static /* synthetic */ boolean c(DescriptorEquivalenceForOverrides descriptorEquivalenceForOverrides, CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, boolean z10, boolean z11, boolean z12, hd.g gVar, int i10, Object obj) {
        if ((i10 & 8) != 0) {
            z11 = true;
        }
        boolean z13 = z11;
        if ((i10 & 16) != 0) {
            z12 = false;
        }
        return descriptorEquivalenceForOverrides.b(callableDescriptor, callableDescriptor2, z10, z13, z12, gVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean d(boolean z10, CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, TypeConstructor typeConstructor, TypeConstructor typeConstructor2) {
        za.k.e(callableDescriptor, "$a");
        za.k.e(callableDescriptor2, "$b");
        za.k.e(typeConstructor, "c1");
        za.k.e(typeConstructor2, "c2");
        if (za.k.a(typeConstructor, typeConstructor2)) {
            return true;
        }
        ClassifierDescriptor v7 = typeConstructor.v();
        ClassifierDescriptor v10 = typeConstructor2.v();
        if ((v7 instanceof TypeParameterDescriptor) && (v10 instanceof TypeParameterDescriptor)) {
            return f18421a.i((TypeParameterDescriptor) v7, (TypeParameterDescriptor) v10, z10, new b(callableDescriptor, callableDescriptor2));
        }
        return false;
    }

    private final boolean e(ClassDescriptor classDescriptor, ClassDescriptor classDescriptor2) {
        return za.k.a(classDescriptor.n(), classDescriptor2.n());
    }

    public static /* synthetic */ boolean g(DescriptorEquivalenceForOverrides descriptorEquivalenceForOverrides, DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2, boolean z10, boolean z11, int i10, Object obj) {
        if ((i10 & 8) != 0) {
            z11 = true;
        }
        return descriptorEquivalenceForOverrides.f(declarationDescriptor, declarationDescriptor2, z10, z11);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ boolean j(DescriptorEquivalenceForOverrides descriptorEquivalenceForOverrides, TypeParameterDescriptor typeParameterDescriptor, TypeParameterDescriptor typeParameterDescriptor2, boolean z10, ya.p pVar, int i10, Object obj) {
        if ((i10 & 8) != 0) {
            pVar = c.f18425e;
        }
        return descriptorEquivalenceForOverrides.i(typeParameterDescriptor, typeParameterDescriptor2, z10, pVar);
    }

    private final boolean k(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2, ya.p<? super DeclarationDescriptor, ? super DeclarationDescriptor, Boolean> pVar, boolean z10) {
        DeclarationDescriptor b10 = declarationDescriptor.b();
        DeclarationDescriptor b11 = declarationDescriptor2.b();
        if (!(b10 instanceof CallableMemberDescriptor) && !(b11 instanceof CallableMemberDescriptor)) {
            return g(this, b10, b11, z10, false, 8, null);
        }
        return pVar.invoke(b10, b11).booleanValue();
    }

    private final SourceElement l(CallableDescriptor callableDescriptor) {
        Object r02;
        while (callableDescriptor instanceof CallableMemberDescriptor) {
            CallableMemberDescriptor callableMemberDescriptor = (CallableMemberDescriptor) callableDescriptor;
            if (callableMemberDescriptor.getKind() != CallableMemberDescriptor.a.FAKE_OVERRIDE) {
                break;
            }
            Collection<? extends CallableMemberDescriptor> e10 = callableMemberDescriptor.e();
            za.k.d(e10, "overriddenDescriptors");
            r02 = _Collections.r0(e10);
            callableDescriptor = (CallableMemberDescriptor) r02;
            if (callableDescriptor == null) {
                return null;
            }
        }
        return callableDescriptor.z();
    }

    public final boolean b(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, boolean z10, boolean z11, boolean z12, hd.g gVar) {
        za.k.e(callableDescriptor, "a");
        za.k.e(callableDescriptor2, "b");
        za.k.e(gVar, "kotlinTypeRefiner");
        if (za.k.a(callableDescriptor, callableDescriptor2)) {
            return true;
        }
        if (!za.k.a(callableDescriptor.getName(), callableDescriptor2.getName())) {
            return false;
        }
        if (z11 && (callableDescriptor instanceof MemberDescriptor) && (callableDescriptor2 instanceof MemberDescriptor) && ((MemberDescriptor) callableDescriptor).U() != ((MemberDescriptor) callableDescriptor2).U()) {
            return false;
        }
        if ((za.k.a(callableDescriptor.b(), callableDescriptor2.b()) && (!z10 || !za.k.a(l(callableDescriptor), l(callableDescriptor2)))) || e.E(callableDescriptor) || e.E(callableDescriptor2) || !k(callableDescriptor, callableDescriptor2, a.f18422e, z10)) {
            return false;
        }
        OverridingUtil i10 = OverridingUtil.i(gVar, new sc.b(z10, callableDescriptor, callableDescriptor2));
        za.k.d(i10, "create(kotlinTypeRefiner…= a && y == b }\n        }");
        OverridingUtil.i.a c10 = i10.E(callableDescriptor, callableDescriptor2, null, !z12).c();
        OverridingUtil.i.a aVar = OverridingUtil.i.a.OVERRIDABLE;
        return c10 == aVar && i10.E(callableDescriptor2, callableDescriptor, null, z12 ^ true).c() == aVar;
    }

    public final boolean f(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2, boolean z10, boolean z11) {
        if ((declarationDescriptor instanceof ClassDescriptor) && (declarationDescriptor2 instanceof ClassDescriptor)) {
            return e((ClassDescriptor) declarationDescriptor, (ClassDescriptor) declarationDescriptor2);
        }
        if ((declarationDescriptor instanceof TypeParameterDescriptor) && (declarationDescriptor2 instanceof TypeParameterDescriptor)) {
            return j(this, (TypeParameterDescriptor) declarationDescriptor, (TypeParameterDescriptor) declarationDescriptor2, z10, null, 8, null);
        }
        if ((declarationDescriptor instanceof CallableDescriptor) && (declarationDescriptor2 instanceof CallableDescriptor)) {
            return c(this, (CallableDescriptor) declarationDescriptor, (CallableDescriptor) declarationDescriptor2, z10, z11, false, g.a.f12215a, 16, null);
        }
        return ((declarationDescriptor instanceof PackageFragmentDescriptor) && (declarationDescriptor2 instanceof PackageFragmentDescriptor)) ? za.k.a(((PackageFragmentDescriptor) declarationDescriptor).d(), ((PackageFragmentDescriptor) declarationDescriptor2).d()) : za.k.a(declarationDescriptor, declarationDescriptor2);
    }

    public final boolean h(TypeParameterDescriptor typeParameterDescriptor, TypeParameterDescriptor typeParameterDescriptor2, boolean z10) {
        za.k.e(typeParameterDescriptor, "a");
        za.k.e(typeParameterDescriptor2, "b");
        return j(this, typeParameterDescriptor, typeParameterDescriptor2, z10, null, 8, null);
    }

    public final boolean i(TypeParameterDescriptor typeParameterDescriptor, TypeParameterDescriptor typeParameterDescriptor2, boolean z10, ya.p<? super DeclarationDescriptor, ? super DeclarationDescriptor, Boolean> pVar) {
        za.k.e(typeParameterDescriptor, "a");
        za.k.e(typeParameterDescriptor2, "b");
        za.k.e(pVar, "equivalentCallables");
        if (za.k.a(typeParameterDescriptor, typeParameterDescriptor2)) {
            return true;
        }
        return !za.k.a(typeParameterDescriptor.b(), typeParameterDescriptor2.b()) && k(typeParameterDescriptor, typeParameterDescriptor2, pVar, z10) && typeParameterDescriptor.j() == typeParameterDescriptor2.j();
    }
}
