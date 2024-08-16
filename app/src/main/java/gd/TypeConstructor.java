package gd;

import java.util.Collection;
import java.util.List;
import mb.KotlinBuiltIns;
import pb.ClassifierDescriptor;
import pb.TypeParameterDescriptor;

/* compiled from: TypeConstructor.java */
/* renamed from: gd.g1, reason: use source file name */
/* loaded from: classes2.dex */
public interface TypeConstructor extends kd.n {
    List<TypeParameterDescriptor> getParameters();

    Collection<g0> q();

    KotlinBuiltIns t();

    TypeConstructor u(hd.g gVar);

    ClassifierDescriptor v();

    boolean w();
}
