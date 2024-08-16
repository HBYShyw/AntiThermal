package rb;

import java.util.Collection;
import oc.ClassId;
import oc.FqName;
import oc.Name;
import pb.ClassDescriptor;

/* compiled from: ClassDescriptorFactory.kt */
/* renamed from: rb.b, reason: use source file name */
/* loaded from: classes2.dex */
public interface ClassDescriptorFactory {
    ClassDescriptor a(ClassId classId);

    boolean b(FqName fqName, Name name);

    Collection<ClassDescriptor> c(FqName fqName);
}
