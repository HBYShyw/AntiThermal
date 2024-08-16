package yb;

import ac.JavaMethodDescriptor;
import dc.RawSubstitution;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import pb.CallableDescriptor;
import pb.ClassDescriptor;
import pb.FunctionDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.TypeParameterDescriptor;
import pb.ValueParameterDescriptor;
import rd.Sequence;
import rd._Sequences;
import sc.ExternalOverridabilityCondition;
import sc.OverridingUtil;
import za.Lambda;

/* compiled from: ErasedOverridabilityCondition.kt */
/* renamed from: yb.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErasedOverridabilityCondition implements ExternalOverridabilityCondition {

    /* compiled from: ErasedOverridabilityCondition.kt */
    /* renamed from: yb.l$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f20119a;

        static {
            int[] iArr = new int[OverridingUtil.i.a.values().length];
            try {
                iArr[OverridingUtil.i.a.OVERRIDABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            f20119a = iArr;
        }
    }

    /* compiled from: ErasedOverridabilityCondition.kt */
    /* renamed from: yb.l$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.l<ValueParameterDescriptor, gd.g0> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f20120e = new b();

        b() {
            super(1);
        }

        @Override // ya.l
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final gd.g0 invoke(ValueParameterDescriptor valueParameterDescriptor) {
            return valueParameterDescriptor.getType();
        }
    }

    @Override // sc.ExternalOverridabilityCondition
    public ExternalOverridabilityCondition.a a() {
        return ExternalOverridabilityCondition.a.SUCCESS_ONLY;
    }

    @Override // sc.ExternalOverridabilityCondition
    public ExternalOverridabilityCondition.b b(CallableDescriptor callableDescriptor, CallableDescriptor callableDescriptor2, ClassDescriptor classDescriptor) {
        Sequence K;
        Sequence w10;
        Sequence z10;
        List n10;
        Sequence y4;
        boolean z11;
        CallableDescriptor c10;
        List<TypeParameterDescriptor> j10;
        za.k.e(callableDescriptor, "superDescriptor");
        za.k.e(callableDescriptor2, "subDescriptor");
        if (callableDescriptor2 instanceof JavaMethodDescriptor) {
            JavaMethodDescriptor javaMethodDescriptor = (JavaMethodDescriptor) callableDescriptor2;
            za.k.d(javaMethodDescriptor.m(), "subDescriptor.typeParameters");
            if (!(!r7.isEmpty())) {
                OverridingUtil.i w11 = OverridingUtil.w(callableDescriptor, callableDescriptor2);
                if ((w11 != null ? w11.c() : null) != null) {
                    return ExternalOverridabilityCondition.b.UNKNOWN;
                }
                List<ValueParameterDescriptor> l10 = javaMethodDescriptor.l();
                za.k.d(l10, "subDescriptor.valueParameters");
                K = _Collections.K(l10);
                w10 = _Sequences.w(K, b.f20120e);
                gd.g0 f10 = javaMethodDescriptor.f();
                za.k.b(f10);
                z10 = _Sequences.z(w10, f10);
                ReceiverParameterDescriptor r02 = javaMethodDescriptor.r0();
                n10 = kotlin.collections.r.n(r02 != null ? r02.getType() : null);
                y4 = _Sequences.y(z10, n10);
                Iterator it = y4.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z11 = false;
                        break;
                    }
                    gd.g0 g0Var = (gd.g0) it.next();
                    if ((g0Var.U0().isEmpty() ^ true) && !(g0Var.Z0() instanceof dc.h)) {
                        z11 = true;
                        break;
                    }
                }
                if (!z11 && (c10 = callableDescriptor.c(new RawSubstitution(null, 1, null).c())) != null) {
                    if (c10 instanceof SimpleFunctionDescriptor) {
                        SimpleFunctionDescriptor simpleFunctionDescriptor = (SimpleFunctionDescriptor) c10;
                        za.k.d(simpleFunctionDescriptor.m(), "erasedSuper.typeParameters");
                        if (!r7.isEmpty()) {
                            FunctionDescriptor.a<? extends SimpleFunctionDescriptor> A = simpleFunctionDescriptor.A();
                            j10 = kotlin.collections.r.j();
                            c10 = A.n(j10).build();
                            za.k.b(c10);
                        }
                    }
                    OverridingUtil.i.a c11 = OverridingUtil.f18440f.F(c10, callableDescriptor2, false).c();
                    za.k.d(c11, "DEFAULT.isOverridableByW…Descriptor, false).result");
                    if (a.f20119a[c11.ordinal()] == 1) {
                        return ExternalOverridabilityCondition.b.OVERRIDABLE;
                    }
                    return ExternalOverridabilityCondition.b.UNKNOWN;
                }
                return ExternalOverridabilityCondition.b.UNKNOWN;
            }
        }
        return ExternalOverridabilityCondition.b.UNKNOWN;
    }
}
