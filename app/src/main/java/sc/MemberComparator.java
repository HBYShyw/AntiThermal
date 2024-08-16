package sc;

import java.util.Comparator;
import pb.ClassDescriptor;
import pb.ConstructorDescriptor;
import pb.DeclarationDescriptor;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import pb.TypeAliasDescriptor;

/* compiled from: MemberComparator.java */
/* renamed from: sc.h, reason: use source file name */
/* loaded from: classes2.dex */
public class MemberComparator implements Comparator<DeclarationDescriptor> {

    /* renamed from: e, reason: collision with root package name */
    public static final MemberComparator f18438e = new MemberComparator();

    private MemberComparator() {
    }

    private static Integer b(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
        int c10 = c(declarationDescriptor2) - c(declarationDescriptor);
        if (c10 != 0) {
            return Integer.valueOf(c10);
        }
        if (e.B(declarationDescriptor) && e.B(declarationDescriptor2)) {
            return 0;
        }
        int compareTo = declarationDescriptor.getName().compareTo(declarationDescriptor2.getName());
        if (compareTo != 0) {
            return Integer.valueOf(compareTo);
        }
        return null;
    }

    private static int c(DeclarationDescriptor declarationDescriptor) {
        if (e.B(declarationDescriptor)) {
            return 8;
        }
        if (declarationDescriptor instanceof ConstructorDescriptor) {
            return 7;
        }
        if (declarationDescriptor instanceof PropertyDescriptor) {
            return ((PropertyDescriptor) declarationDescriptor).r0() == null ? 6 : 5;
        }
        if (declarationDescriptor instanceof FunctionDescriptor) {
            return ((FunctionDescriptor) declarationDescriptor).r0() == null ? 4 : 3;
        }
        if (declarationDescriptor instanceof ClassDescriptor) {
            return 2;
        }
        return declarationDescriptor instanceof TypeAliasDescriptor ? 1 : 0;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(DeclarationDescriptor declarationDescriptor, DeclarationDescriptor declarationDescriptor2) {
        Integer b10 = b(declarationDescriptor, declarationDescriptor2);
        if (b10 != null) {
            return b10.intValue();
        }
        return 0;
    }
}
