package yb;

import mb.CompanionObjectMapping;
import mb.CompanionObjectMappingUtils;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;
import pb.DeclarationDescriptor;
import pb.PropertyDescriptor;

/* compiled from: DescriptorsJvmAbiUtil.java */
/* renamed from: yb.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class DescriptorsJvmAbiUtil {
    private static /* synthetic */ void a(int i10) {
        Object[] objArr = new Object[3];
        if (i10 == 1 || i10 == 2) {
            objArr[0] = "companionObject";
        } else if (i10 != 3) {
            objArr[0] = "propertyDescriptor";
        } else {
            objArr[0] = "memberDescriptor";
        }
        objArr[1] = "kotlin/reflect/jvm/internal/impl/load/java/DescriptorsJvmAbiUtil";
        if (i10 == 1) {
            objArr[2] = "isClassCompanionObjectWithBackingFieldsInOuter";
        } else if (i10 == 2) {
            objArr[2] = "isMappedIntrinsicCompanionObject";
        } else if (i10 != 3) {
            objArr[2] = "isPropertyWithBackingFieldInOuterClass";
        } else {
            objArr[2] = "hasJvmFieldAnnotation";
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    public static boolean b(CallableMemberDescriptor callableMemberDescriptor) {
        pb.w v02;
        if (callableMemberDescriptor == null) {
            a(3);
        }
        if ((callableMemberDescriptor instanceof PropertyDescriptor) && (v02 = ((PropertyDescriptor) callableMemberDescriptor).v0()) != null && v02.i().a(JvmAbi.f20006b)) {
            return true;
        }
        return callableMemberDescriptor.i().a(JvmAbi.f20006b);
    }

    public static boolean c(DeclarationDescriptor declarationDescriptor) {
        if (declarationDescriptor == null) {
            a(1);
        }
        return sc.e.x(declarationDescriptor) && sc.e.w(declarationDescriptor.b()) && !d((ClassDescriptor) declarationDescriptor);
    }

    public static boolean d(ClassDescriptor classDescriptor) {
        if (classDescriptor == null) {
            a(2);
        }
        return CompanionObjectMappingUtils.a(CompanionObjectMapping.f15213a, classDescriptor);
    }

    public static boolean e(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor == null) {
            a(0);
        }
        if (propertyDescriptor.getKind() == CallableMemberDescriptor.a.FAKE_OVERRIDE) {
            return false;
        }
        if (c(propertyDescriptor.b())) {
            return true;
        }
        return sc.e.x(propertyDescriptor.b()) && b(propertyDescriptor);
    }
}
