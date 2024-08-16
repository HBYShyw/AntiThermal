package yb;

import ac.JavaClassDescriptor;
import ac.JavaMethodDescriptor;
import hc.m;
import java.util.List;
import kotlin.collections._Collections;
import mb.KotlinBuiltIns;
import oc.Name;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ClassifierDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.ValueParameterDescriptor;
import sc.ExternalOverridabilityCondition;
import yb.SpecialGenericSignatures;
import za.DefaultConstructorMarker;

/* compiled from: JavaIncompatibilityRulesOverridabilityCondition.kt */
/* renamed from: yb.t, reason: use source file name */
/* loaded from: classes2.dex */
public final class JavaIncompatibilityRulesOverridabilityCondition implements ExternalOverridabilityCondition {

    /* renamed from: a, reason: collision with root package name */
    public static final a f20135a = new a(null);

    /* compiled from: JavaIncompatibilityRulesOverridabilityCondition.kt */
    /* renamed from: yb.t$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final boolean b(FunctionDescriptor functionDescriptor) {
            Object q02;
            if (functionDescriptor.l().size() != 1) {
                return false;
            }
            DeclarationDescriptor b10 = functionDescriptor.b();
            ClassDescriptor classDescriptor = b10 instanceof ClassDescriptor ? (ClassDescriptor) b10 : null;
            if (classDescriptor == null) {
                return false;
            }
            List<ValueParameterDescriptor> l10 = functionDescriptor.l();
            za.k.d(l10, "f.valueParameters");
            q02 = _Collections.q0(l10);
            ClassifierDescriptor v7 = ((ValueParameterDescriptor) q02).getType().W0().v();
            ClassDescriptor classDescriptor2 = v7 instanceof ClassDescriptor ? (ClassDescriptor) v7 : null;
            if (classDescriptor2 == null) {
                return false;
            }
            return KotlinBuiltIns.q0(classDescriptor) && za.k.a(wc.c.l(classDescriptor), wc.c.l(classDescriptor2));
        }

        private final hc.m c(FunctionDescriptor functionDescriptor, ValueParameterDescriptor valueParameterDescriptor) {
            if (!hc.w.e(functionDescriptor) && !b(functionDescriptor)) {
                gd.g0 type = valueParameterDescriptor.getType();
                za.k.d(type, "valueParameterDescriptor.type");
                return hc.w.g(type);
            }
            gd.g0 type2 = valueParameterDescriptor.getType();
            za.k.d(type2, "valueParameterDescriptor.type");
            return hc.w.g(ld.a.u(type2));
        }

        public final boolean a(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2) {
            List<ma.o> G0;
            za.k.e(callableDescriptor, "superDescriptor");
            za.k.e(callableDescriptor2, "subDescriptor");
            if ((callableDescriptor2 instanceof JavaMethodDescriptor) && (callableDescriptor instanceof FunctionDescriptor)) {
                JavaMethodDescriptor javaMethodDescriptor = (JavaMethodDescriptor) callableDescriptor2;
                javaMethodDescriptor.l().size();
                FunctionDescriptor functionDescriptor = (FunctionDescriptor) callableDescriptor;
                functionDescriptor.l().size();
                List<ValueParameterDescriptor> l10 = javaMethodDescriptor.a().l();
                za.k.d(l10, "subDescriptor.original.valueParameters");
                List<ValueParameterDescriptor> l11 = functionDescriptor.T0().l();
                za.k.d(l11, "superDescriptor.original.valueParameters");
                G0 = _Collections.G0(l10, l11);
                for (ma.o oVar : G0) {
                    ValueParameterDescriptor valueParameterDescriptor = (ValueParameterDescriptor) oVar.a();
                    ValueParameterDescriptor valueParameterDescriptor2 = (ValueParameterDescriptor) oVar.b();
                    za.k.d(valueParameterDescriptor, "subParameter");
                    boolean z10 = c((FunctionDescriptor) callableDescriptor2, valueParameterDescriptor) instanceof m.d;
                    za.k.d(valueParameterDescriptor2, "superParameter");
                    if (z10 != (c(functionDescriptor, valueParameterDescriptor2) instanceof m.d)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private final boolean c(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor) {
        if ((callableDescriptor instanceof CallableMemberDescriptor) && (callableDescriptor2 instanceof FunctionDescriptor) && !KotlinBuiltIns.f0(callableDescriptor2)) {
            f fVar = f.f20072n;
            FunctionDescriptor functionDescriptor = (FunctionDescriptor) callableDescriptor2;
            Name name = functionDescriptor.getName();
            za.k.d(name, "subDescriptor.name");
            if (!fVar.l(name)) {
                SpecialGenericSignatures.a aVar = SpecialGenericSignatures.f20091a;
                Name name2 = functionDescriptor.getName();
                za.k.d(name2, "subDescriptor.name");
                if (!aVar.k(name2)) {
                    return false;
                }
            }
            CallableMemberDescriptor e10 = h0.e((CallableMemberDescriptor) callableDescriptor);
            boolean z10 = callableDescriptor instanceof FunctionDescriptor;
            FunctionDescriptor functionDescriptor2 = z10 ? (FunctionDescriptor) callableDescriptor : null;
            if ((!(functionDescriptor2 != null && functionDescriptor.B0() == functionDescriptor2.B0())) && (e10 == null || !functionDescriptor.B0())) {
                return true;
            }
            if ((classDescriptor instanceof JavaClassDescriptor) && functionDescriptor.l0() == null && e10 != null && !h0.f(classDescriptor, e10)) {
                if ((e10 instanceof FunctionDescriptor) && z10 && f.k((FunctionDescriptor) e10) != null) {
                    String c10 = hc.w.c(functionDescriptor, false, false, 2, null);
                    FunctionDescriptor T0 = ((FunctionDescriptor) callableDescriptor).T0();
                    za.k.d(T0, "superDescriptor.original");
                    if (za.k.a(c10, hc.w.c(T0, false, false, 2, null))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override // sc.ExternalOverridabilityCondition
    public ExternalOverridabilityCondition.a a() {
        return ExternalOverridabilityCondition.a.CONFLICTS_ONLY;
    }

    @Override // sc.ExternalOverridabilityCondition
    public ExternalOverridabilityCondition.b b(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor) {
        za.k.e(callableDescriptor, "superDescriptor");
        za.k.e(callableDescriptor2, "subDescriptor");
        if (c(callableDescriptor, callableDescriptor2, classDescriptor)) {
            return ExternalOverridabilityCondition.b.INCOMPATIBLE;
        }
        if (f20135a.a(callableDescriptor, callableDescriptor2)) {
            return ExternalOverridabilityCondition.b.INCOMPATIBLE;
        }
        return ExternalOverridabilityCondition.b.UNKNOWN;
    }
}
