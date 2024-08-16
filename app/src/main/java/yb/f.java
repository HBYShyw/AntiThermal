package yb;

import kotlin.collections._Collections;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.FunctionDescriptor;
import yb.SpecialGenericSignatures;
import za.Lambda;

/* compiled from: specialBuiltinMembers.kt */
/* loaded from: classes2.dex */
public final class f extends SpecialGenericSignatures {

    /* renamed from: n, reason: collision with root package name */
    public static final f f20072n = new f();

    /* compiled from: specialBuiltinMembers.kt */
    /* loaded from: classes2.dex */
    static final class a extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f20073e = new a();

        a() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf(f.f20072n.j(callableMemberDescriptor));
        }
    }

    /* compiled from: specialBuiltinMembers.kt */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f20074e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf((callableMemberDescriptor instanceof FunctionDescriptor) && f.f20072n.j(callableMemberDescriptor));
        }
    }

    private f() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean j(CallableMemberDescriptor callableMemberDescriptor) {
        boolean L;
        L = _Collections.L(SpecialGenericSignatures.f20091a.e(), hc.w.d(callableMemberDescriptor));
        return L;
    }

    public static final FunctionDescriptor k(FunctionDescriptor functionDescriptor) {
        za.k.e(functionDescriptor, "functionDescriptor");
        f fVar = f20072n;
        Name name = functionDescriptor.getName();
        za.k.d(name, "functionDescriptor.name");
        if (fVar.l(name)) {
            return (FunctionDescriptor) wc.c.f(functionDescriptor, false, a.f20073e, 1, null);
        }
        return null;
    }

    public static final SpecialGenericSignatures.b m(CallableMemberDescriptor callableMemberDescriptor) {
        CallableMemberDescriptor f10;
        String d10;
        za.k.e(callableMemberDescriptor, "<this>");
        SpecialGenericSignatures.a aVar = SpecialGenericSignatures.f20091a;
        if (!aVar.d().contains(callableMemberDescriptor.getName()) || (f10 = wc.c.f(callableMemberDescriptor, false, b.f20074e, 1, null)) == null || (d10 = hc.w.d(f10)) == null) {
            return null;
        }
        return aVar.l(d10);
    }

    public final boolean l(Name name) {
        za.k.e(name, "<this>");
        return SpecialGenericSignatures.f20091a.d().contains(name);
    }
}
