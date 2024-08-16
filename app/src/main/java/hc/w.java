package hc;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import gd.g0;
import hc.m;
import java.util.Iterator;
import java.util.List;
import kotlin.collections._Collections;
import mb.StandardNames;
import ob.JavaToKotlinClassMap;
import oc.ClassId;
import oc.FqNameUnsafe;
import pb.CallableDescriptor;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.ReceiverParameterDescriptor;
import pb.SimpleFunctionDescriptor;
import pb.ValueParameterDescriptor;
import xc.JvmClassName;
import xc.JvmPrimitiveType;
import yb.h0;

/* compiled from: methodSignatureMapping.kt */
/* loaded from: classes2.dex */
public final class w {
    private static final void a(StringBuilder sb2, g0 g0Var) {
        sb2.append(g(g0Var));
    }

    public static final String b(FunctionDescriptor functionDescriptor, boolean z10, boolean z11) {
        String b10;
        za.k.e(functionDescriptor, "<this>");
        StringBuilder sb2 = new StringBuilder();
        if (z11) {
            if (functionDescriptor instanceof ConstructorDescriptor) {
                b10 = "<init>";
            } else {
                b10 = functionDescriptor.getName().b();
                za.k.d(b10, "name.asString()");
            }
            sb2.append(b10);
        }
        sb2.append("(");
        ReceiverParameterDescriptor r02 = functionDescriptor.r0();
        if (r02 != null) {
            g0 type = r02.getType();
            za.k.d(type, "it.type");
            a(sb2, type);
        }
        Iterator<ValueParameterDescriptor> it = functionDescriptor.l().iterator();
        while (it.hasNext()) {
            g0 type2 = it.next().getType();
            za.k.d(type2, "parameter.type");
            a(sb2, type2);
        }
        sb2.append(")");
        if (z10) {
            if (e.c(functionDescriptor)) {
                sb2.append("V");
            } else {
                g0 f10 = functionDescriptor.f();
                za.k.b(f10);
                a(sb2, f10);
            }
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public static /* synthetic */ String c(FunctionDescriptor functionDescriptor, boolean z10, boolean z11, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = true;
        }
        if ((i10 & 2) != 0) {
            z11 = true;
        }
        return b(functionDescriptor, z10, z11);
    }

    public static final String d(CallableDescriptor callableDescriptor) {
        za.k.e(callableDescriptor, "<this>");
        SignatureBuildingComponents signatureBuildingComponents = SignatureBuildingComponents.f12209a;
        if (sc.e.E(callableDescriptor)) {
            return null;
        }
        DeclarationDescriptor b10 = callableDescriptor.b();
        ClassDescriptor classDescriptor = b10 instanceof ClassDescriptor ? (ClassDescriptor) b10 : null;
        if (classDescriptor == null || classDescriptor.getName().g()) {
            return null;
        }
        CallableDescriptor a10 = callableDescriptor.a();
        SimpleFunctionDescriptor simpleFunctionDescriptor = a10 instanceof SimpleFunctionDescriptor ? (SimpleFunctionDescriptor) a10 : null;
        if (simpleFunctionDescriptor == null) {
            return null;
        }
        return methodSignatureBuildingUtils.a(signatureBuildingComponents, classDescriptor, c(simpleFunctionDescriptor, false, false, 3, null));
    }

    public static final boolean e(CallableDescriptor callableDescriptor) {
        Object q02;
        FunctionDescriptor k10;
        Object q03;
        za.k.e(callableDescriptor, "f");
        if (!(callableDescriptor instanceof FunctionDescriptor)) {
            return false;
        }
        FunctionDescriptor functionDescriptor = (FunctionDescriptor) callableDescriptor;
        if (!za.k.a(functionDescriptor.getName().b(), EventType.STATE_PACKAGE_CHANGED_REMOVE) || functionDescriptor.l().size() != 1 || h0.h((CallableMemberDescriptor) callableDescriptor)) {
            return false;
        }
        List<ValueParameterDescriptor> l10 = functionDescriptor.a().l();
        za.k.d(l10, "f.original.valueParameters");
        q02 = _Collections.q0(l10);
        g0 type = ((ValueParameterDescriptor) q02).getType();
        za.k.d(type, "f.original.valueParameters.single().type");
        m g6 = g(type);
        m.d dVar = g6 instanceof m.d ? (m.d) g6 : null;
        if ((dVar != null ? dVar.i() : null) != JvmPrimitiveType.INT || (k10 = yb.f.k(functionDescriptor)) == null) {
            return false;
        }
        List<ValueParameterDescriptor> l11 = k10.a().l();
        za.k.d(l11, "overridden.original.valueParameters");
        q03 = _Collections.q0(l11);
        g0 type2 = ((ValueParameterDescriptor) q03).getType();
        za.k.d(type2, "overridden.original.valueParameters.single().type");
        m g10 = g(type2);
        DeclarationDescriptor b10 = k10.b();
        za.k.d(b10, "overridden.containingDeclaration");
        return za.k.a(wc.c.m(b10), StandardNames.a.f15294c0.j()) && (g10 instanceof m.c) && za.k.a(((m.c) g10).i(), "java/lang/Object");
    }

    public static final String f(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "<this>");
        JavaToKotlinClassMap javaToKotlinClassMap = JavaToKotlinClassMap.f16339a;
        FqNameUnsafe j10 = wc.c.l(classDescriptor).j();
        za.k.d(j10, "fqNameSafe.toUnsafe()");
        ClassId n10 = javaToKotlinClassMap.n(j10);
        if (n10 != null) {
            String f10 = JvmClassName.b(n10).f();
            za.k.d(f10, "byClassId(it).internalName");
            return f10;
        }
        return e.b(classDescriptor, null, 2, null);
    }

    public static final m g(g0 g0Var) {
        za.k.e(g0Var, "<this>");
        return (m) e.e(g0Var, o.f12196a, TypeMappingMode.f12122o, a0.f12109a, null, null, 32, null);
    }
}
