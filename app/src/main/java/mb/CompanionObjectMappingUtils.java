package mb;

import java.util.Set;
import kotlin.collections._Collections;
import oc.ClassId;
import pb.ClassDescriptor;

/* compiled from: CompanionObjectMappingUtils.kt */
/* renamed from: mb.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class CompanionObjectMappingUtils {
    public static final boolean a(CompanionObjectMapping companionObjectMapping, ClassDescriptor classDescriptor) {
        boolean L;
        za.k.e(companionObjectMapping, "<this>");
        za.k.e(classDescriptor, "classDescriptor");
        if (sc.e.x(classDescriptor)) {
            Set<ClassId> b10 = companionObjectMapping.b();
            ClassId k10 = wc.c.k(classDescriptor);
            L = _Collections.L(b10, k10 != null ? k10.g() : null);
            if (L) {
                return true;
            }
        }
        return false;
    }
}
