package id;

import fd.LockBasedStorageManager;
import gd.TypeSubstitutor;
import gd.n1;
import java.util.List;
import java.util.Set;
import kotlin.collections.SetsJVM;
import kotlin.collections.r;
import oc.Name;
import pb.ClassConstructorDescriptor;
import pb.ClassDescriptor;
import pb.ClassKind;
import pb.DescriptorVisibilities;
import pb.Modality;
import pb.ModuleDescriptor;
import pb.SourceElement;
import pb.ValueParameterDescriptor;
import sb.ClassConstructorDescriptorImpl;
import sb.ClassDescriptorImpl;

/* compiled from: ErrorClassDescriptor.kt */
/* renamed from: id.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class ErrorClassDescriptor extends ClassDescriptorImpl {
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ErrorClassDescriptor(Name name) {
        super(r1, name, r3, r4, r5, r11, false, LockBasedStorageManager.f11424e);
        List j10;
        List<ValueParameterDescriptor> j11;
        Set<ClassConstructorDescriptor> d10;
        za.k.e(name, "name");
        ErrorUtils errorUtils = ErrorUtils.f12833a;
        ModuleDescriptor i10 = errorUtils.i();
        Modality modality = Modality.OPEN;
        ClassKind classKind = ClassKind.CLASS;
        j10 = r.j();
        SourceElement sourceElement = SourceElement.f16664a;
        ClassConstructorDescriptorImpl w12 = ClassConstructorDescriptorImpl.w1(this, qb.g.f17195b.b(), true, sourceElement);
        j11 = r.j();
        w12.z1(j11, DescriptorVisibilities.f16732d);
        za.k.d(w12, "create(this, Annotations…          )\n            }");
        ErrorScopeKind errorScopeKind = ErrorScopeKind.SCOPE_FOR_ERROR_CLASS;
        String name2 = w12.getName().toString();
        za.k.d(name2, "errorConstructor.name.toString()");
        zc.h b10 = ErrorUtils.b(errorScopeKind, name2, "");
        ErrorTypeKind errorTypeKind = ErrorTypeKind.f12826x0;
        w12.p1(new ErrorType(errorUtils.e(errorTypeKind, new String[0]), b10, errorTypeKind, null, false, new String[0], 24, null));
        d10 = SetsJVM.d(w12);
        T0(b10, d10, w12);
    }

    @Override // sb.AbstractClassDescriptor, pb.Substitutable
    /* renamed from: O0 */
    public ClassDescriptor c(TypeSubstitutor typeSubstitutor) {
        za.k.e(typeSubstitutor, "substitutor");
        return this;
    }

    @Override // sb.AbstractClassDescriptor, sb.t
    public zc.h P(n1 n1Var, hd.g gVar) {
        za.k.e(n1Var, "typeSubstitution");
        za.k.e(gVar, "kotlinTypeRefiner");
        ErrorScopeKind errorScopeKind = ErrorScopeKind.SCOPE_FOR_ERROR_CLASS;
        String name = getName().toString();
        za.k.d(name, "name.toString()");
        return ErrorUtils.b(errorScopeKind, name, n1Var.toString());
    }

    @Override // sb.ClassDescriptorImpl
    public String toString() {
        String b10 = getName().b();
        za.k.d(b10, "name.asString()");
        return b10;
    }
}
