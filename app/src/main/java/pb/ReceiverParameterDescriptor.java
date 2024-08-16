package pb;

import ad.ReceiverValue;
import gd.TypeSubstitutor;

/* compiled from: ReceiverParameterDescriptor.java */
/* renamed from: pb.x0, reason: use source file name */
/* loaded from: classes2.dex */
public interface ReceiverParameterDescriptor extends ParameterDescriptor {
    @Override // pb.Substitutable
    CallableDescriptor c(TypeSubstitutor typeSubstitutor);

    ReceiverValue getValue();
}
