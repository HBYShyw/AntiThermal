package pb;

/* compiled from: ModalityUtils.kt */
/* renamed from: pb.f0, reason: use source file name */
/* loaded from: classes2.dex */
public final class ModalityUtils {
    public static final boolean a(ClassDescriptor classDescriptor) {
        za.k.e(classDescriptor, "<this>");
        return classDescriptor.o() == Modality.FINAL && classDescriptor.getKind() != ClassKind.ENUM_CLASS;
    }
}
