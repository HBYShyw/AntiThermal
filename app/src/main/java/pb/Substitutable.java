package pb;

import gd.TypeSubstitutor;
import pb.DeclarationDescriptorNonRoot;

/* compiled from: Substitutable.kt */
/* renamed from: pb.c1, reason: use source file name */
/* loaded from: classes2.dex */
public interface Substitutable<T extends DeclarationDescriptorNonRoot> {
    T c(TypeSubstitutor typeSubstitutor);
}
