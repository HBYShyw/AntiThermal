package dc;

import gd.TypeUsage;
import kotlin.collections.SetsJVM;
import pb.TypeParameterDescriptor;
import za.k;

/* compiled from: JavaTypeAttributes.kt */
/* loaded from: classes2.dex */
public final class b {
    public static final a a(TypeUsage typeUsage, boolean z10, boolean z11, TypeParameterDescriptor typeParameterDescriptor) {
        k.e(typeUsage, "<this>");
        return new a(typeUsage, null, z11, z10, typeParameterDescriptor != null ? SetsJVM.d(typeParameterDescriptor) : null, null, 34, null);
    }

    public static /* synthetic */ a b(TypeUsage typeUsage, boolean z10, boolean z11, TypeParameterDescriptor typeParameterDescriptor, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            z10 = false;
        }
        if ((i10 & 2) != 0) {
            z11 = false;
        }
        if ((i10 & 4) != 0) {
            typeParameterDescriptor = null;
        }
        return a(typeUsage, z10, z11, typeParameterDescriptor);
    }
}
