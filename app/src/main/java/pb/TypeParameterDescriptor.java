package pb;

import fd.StorageManager;
import gd.TypeConstructor;
import gd.Variance;
import java.util.List;

/* compiled from: TypeParameterDescriptor.java */
/* renamed from: pb.f1, reason: use source file name */
/* loaded from: classes2.dex */
public interface TypeParameterDescriptor extends ClassifierDescriptor, kd.o {
    boolean N();

    @Override // pb.ClassifierDescriptor, pb.DeclarationDescriptor
    TypeParameterDescriptor a();

    List<gd.g0> getUpperBounds();

    int j();

    @Override // pb.ClassifierDescriptor
    TypeConstructor n();

    StorageManager o0();

    Variance s();

    boolean t0();
}
