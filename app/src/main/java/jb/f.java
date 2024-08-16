package jb;

import ma.Unit;
import pb.FunctionDescriptor;
import pb.PropertyDescriptor;
import sb.DeclarationDescriptorVisitorEmptyBodies;

/* compiled from: util.kt */
/* loaded from: classes2.dex */
public class f extends DeclarationDescriptorVisitorEmptyBodies<KCallableImpl<?>, Unit> {

    /* renamed from: a, reason: collision with root package name */
    private final KDeclarationContainerImpl f13190a;

    public f(KDeclarationContainerImpl kDeclarationContainerImpl) {
        za.k.e(kDeclarationContainerImpl, "container");
        this.f13190a = kDeclarationContainerImpl;
    }

    @Override // sb.DeclarationDescriptorVisitorEmptyBodies, pb.DeclarationDescriptorVisitor
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public KCallableImpl<?> h(FunctionDescriptor functionDescriptor, Unit unit) {
        za.k.e(functionDescriptor, "descriptor");
        za.k.e(unit, "data");
        return new KFunctionImpl(this.f13190a, functionDescriptor);
    }

    @Override // pb.DeclarationDescriptorVisitor
    /* renamed from: q, reason: merged with bridge method [inline-methods] */
    public KCallableImpl<?> f(PropertyDescriptor propertyDescriptor, Unit unit) {
        za.k.e(propertyDescriptor, "descriptor");
        za.k.e(unit, "data");
        int i10 = (propertyDescriptor.m0() != null ? 1 : 0) + (propertyDescriptor.r0() != null ? 1 : 0);
        if (propertyDescriptor.p0()) {
            if (i10 == 0) {
                return new q(this.f13190a, propertyDescriptor);
            }
            if (i10 == 1) {
                return new r(this.f13190a, propertyDescriptor);
            }
            if (i10 == 2) {
                return new s(this.f13190a, propertyDescriptor);
            }
        } else {
            if (i10 == 0) {
                return new w(this.f13190a, propertyDescriptor);
            }
            if (i10 == 1) {
                return new x(this.f13190a, propertyDescriptor);
            }
            if (i10 == 2) {
                return new y(this.f13190a, propertyDescriptor);
            }
        }
        throw new KotlinReflectionInternalError("Unsupported property: " + propertyDescriptor);
    }
}
