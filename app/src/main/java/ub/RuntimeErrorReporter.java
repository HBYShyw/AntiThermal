package ub;

import cd.ErrorReporter;
import java.util.List;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;

/* compiled from: RuntimeErrorReporter.kt */
/* renamed from: ub.j, reason: use source file name */
/* loaded from: classes2.dex */
public final class RuntimeErrorReporter implements ErrorReporter {

    /* renamed from: b, reason: collision with root package name */
    public static final RuntimeErrorReporter f18980b = new RuntimeErrorReporter();

    private RuntimeErrorReporter() {
    }

    @Override // cd.ErrorReporter
    public void a(ClassDescriptor classDescriptor, List<String> list) {
        za.k.e(classDescriptor, "descriptor");
        za.k.e(list, "unresolvedSuperClasses");
        throw new IllegalStateException("Incomplete hierarchy for class " + classDescriptor.getName() + ", unresolved classes " + list);
    }

    @Override // cd.ErrorReporter
    public void b(CallableMemberDescriptor callableMemberDescriptor) {
        za.k.e(callableMemberDescriptor, "descriptor");
        throw new IllegalStateException("Cannot infer visibility for " + callableMemberDescriptor);
    }
}
