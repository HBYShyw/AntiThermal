package hc;

import pb.ClassDescriptor;

/* compiled from: methodSignatureBuildingUtils.kt */
/* renamed from: hc.v, reason: use source file name */
/* loaded from: classes2.dex */
public final class methodSignatureBuildingUtils {
    public static final String a(SignatureBuildingComponents signatureBuildingComponents, ClassDescriptor classDescriptor, String str) {
        za.k.e(signatureBuildingComponents, "<this>");
        za.k.e(classDescriptor, "classDescriptor");
        za.k.e(str, "jvmDescriptor");
        return signatureBuildingComponents.k(w.f(classDescriptor), str);
    }
}
