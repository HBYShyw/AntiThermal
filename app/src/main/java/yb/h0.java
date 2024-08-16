package yb;

import ac.JavaClassDescriptor;
import gd.o0;
import hd.TypeCheckingProcedure;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyAccessorDescriptor;
import pb.PropertyDescriptor;
import pb.SimpleFunctionDescriptor;
import za.Lambda;

/* compiled from: specialBuiltinMembers.kt */
/* loaded from: classes2.dex */
public final class h0 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: specialBuiltinMembers.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f20086e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf(ClassicBuiltinSpecialProperties.f20089a.b(wc.c.s(callableMemberDescriptor)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: specialBuiltinMembers.kt */
    /* loaded from: classes2.dex */
    public static final class b extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f20087e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf(e.f20066n.j((SimpleFunctionDescriptor) callableMemberDescriptor));
        }
    }

    /* compiled from: specialBuiltinMembers.kt */
    /* loaded from: classes2.dex */
    static final class c extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f20088e = new c();

        c() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf(KotlinBuiltIns.f0(callableMemberDescriptor) && f.m(callableMemberDescriptor) != null);
        }
    }

    public static final boolean a(CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(callableMemberDescriptor, "<this>");
        return d(callableMemberDescriptor) != null;
    }

    public static final String b(CallableMemberDescriptor callableMemberDescriptor) {
        CallableMemberDescriptor s7;
        Name i10;
        za.k.e(callableMemberDescriptor, "callableMemberDescriptor");
        CallableMemberDescriptor c10 = c(callableMemberDescriptor);
        if (c10 == null || (s7 = wc.c.s(c10)) == null) {
            return null;
        }
        if (s7 instanceof PropertyDescriptor) {
            return ClassicBuiltinSpecialProperties.f20089a.a(s7);
        }
        if (!(s7 instanceof SimpleFunctionDescriptor) || (i10 = e.f20066n.i((SimpleFunctionDescriptor) s7)) == null) {
            return null;
        }
        return i10.b();
    }

    private static final CallableMemberDescriptor c(CallableMemberDescriptor callableMemberDescriptor) {
        if (KotlinBuiltIns.f0(callableMemberDescriptor)) {
            return d(callableMemberDescriptor);
        }
        return null;
    }

    public static final <T extends CallableMemberDescriptor> T d(T t7) {
        za.k.e(t7, "<this>");
        if (!SpecialGenericSignatures.f20091a.g().contains(t7.getName()) && !g.f20075a.d().contains(wc.c.s(t7).getName())) {
            return null;
        }
        if (t7 instanceof PropertyDescriptor ? true : t7 instanceof PropertyAccessorDescriptor) {
            return (T) wc.c.f(t7, false, a.f20086e, 1, null);
        }
        if (t7 instanceof SimpleFunctionDescriptor) {
            return (T) wc.c.f(t7, false, b.f20087e, 1, null);
        }
        return null;
    }

    public static final <T extends CallableMemberDescriptor> T e(T t7) {
        za.k.e(t7, "<this>");
        T t10 = (T) d(t7);
        if (t10 != null) {
            return t10;
        }
        f fVar = f.f20072n;
        Name name = t7.getName();
        za.k.d(name, "name");
        if (fVar.l(name)) {
            return (T) wc.c.f(t7, false, c.f20088e, 1, null);
        }
        return null;
    }

    public static final boolean f(ClassDescriptor classDescriptor, CallableDescriptor callableDescriptor) {
        za.k.e(classDescriptor, "<this>");
        za.k.e(callableDescriptor, "specialCallableDescriptor");
        DeclarationDescriptor b10 = callableDescriptor.b();
        za.k.c(b10, "null cannot be cast to non-null type org.jetbrains.kotlin.descriptors.ClassDescriptor");
        o0 x10 = ((ClassDescriptor) b10).x();
        za.k.d(x10, "specialCallableDescripto…ssDescriptor).defaultType");
        ClassDescriptor s7 = sc.e.s(classDescriptor);
        while (true) {
            if (s7 == null) {
                return false;
            }
            if (!(s7 instanceof JavaClassDescriptor)) {
                if (TypeCheckingProcedure.b(s7.x(), x10) != null) {
                    return !KotlinBuiltIns.f0(s7);
                }
            }
            s7 = sc.e.s(s7);
        }
    }

    public static final boolean g(CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(callableMemberDescriptor, "<this>");
        return wc.c.s(callableMemberDescriptor).b() instanceof JavaClassDescriptor;
    }

    public static final boolean h(CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(callableMemberDescriptor, "<this>");
        return g(callableMemberDescriptor) || KotlinBuiltIns.f0(callableMemberDescriptor);
    }
}
