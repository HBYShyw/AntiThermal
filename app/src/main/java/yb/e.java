package yb;

import java.util.Map;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.CallableMemberDescriptor;
import pb.SimpleFunctionDescriptor;
import za.Lambda;

/* compiled from: specialBuiltinMembers.kt */
/* loaded from: classes2.dex */
public final class e extends SpecialGenericSignatures {

    /* renamed from: n, reason: collision with root package name */
    public static final e f20066n = new e();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: specialBuiltinMembers.kt */
    /* loaded from: classes2.dex */
    public static final class a extends Lambda implements ya.l<CallableMemberDescriptor, Boolean> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ SimpleFunctionDescriptor f20067e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(SimpleFunctionDescriptor simpleFunctionDescriptor) {
            super(1);
            this.f20067e = simpleFunctionDescriptor;
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Boolean invoke(CallableMemberDescriptor callableMemberDescriptor) {
            za.k.e(callableMemberDescriptor, "it");
            return Boolean.valueOf(SpecialGenericSignatures.f20091a.j().containsKey(hc.w.d(this.f20067e)));
        }
    }

    private e() {
    }

    public final Name i(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        za.k.e(simpleFunctionDescriptor, "functionDescriptor");
        Map<String, Name> j10 = SpecialGenericSignatures.f20091a.j();
        String d10 = hc.w.d(simpleFunctionDescriptor);
        if (d10 == null) {
            return null;
        }
        return j10.get(d10);
    }

    public final boolean j(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        za.k.e(simpleFunctionDescriptor, "functionDescriptor");
        return KotlinBuiltIns.f0(simpleFunctionDescriptor) && wc.c.f(simpleFunctionDescriptor, false, new a(simpleFunctionDescriptor), 1, null) != null;
    }

    public final boolean k(SimpleFunctionDescriptor simpleFunctionDescriptor) {
        za.k.e(simpleFunctionDescriptor, "<this>");
        return za.k.a(simpleFunctionDescriptor.getName().b(), "removeAt") && za.k.a(hc.w.d(simpleFunctionDescriptor), SpecialGenericSignatures.f20091a.h().b());
    }
}
