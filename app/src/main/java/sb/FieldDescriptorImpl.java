package sb;

import pb.PropertyDescriptor;
import qb.AnnotatedImpl;

/* compiled from: FieldDescriptorImpl.kt */
/* renamed from: sb.o, reason: use source file name */
/* loaded from: classes2.dex */
public final class FieldDescriptorImpl extends AnnotatedImpl implements pb.w {

    /* renamed from: f, reason: collision with root package name */
    private final PropertyDescriptor f18325f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FieldDescriptorImpl(qb.g gVar, PropertyDescriptor propertyDescriptor) {
        super(gVar);
        za.k.e(gVar, "annotations");
        za.k.e(propertyDescriptor, "correspondingProperty");
        this.f18325f = propertyDescriptor;
    }
}
