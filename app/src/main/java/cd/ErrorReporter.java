package cd;

import java.util.List;
import pb.CallableMemberDescriptor;
import pb.ClassDescriptor;

/* compiled from: ErrorReporter.java */
/* renamed from: cd.r, reason: use source file name */
/* loaded from: classes2.dex */
public interface ErrorReporter {

    /* renamed from: a, reason: collision with root package name */
    public static final ErrorReporter f5285a = new a();

    /* compiled from: ErrorReporter.java */
    /* renamed from: cd.r$a */
    /* loaded from: classes2.dex */
    static class a implements ErrorReporter {
        a() {
        }

        private static /* synthetic */ void c(int i10) {
            Object[] objArr = new Object[3];
            if (i10 != 1) {
                objArr[0] = "descriptor";
            } else {
                objArr[0] = "unresolvedSuperClasses";
            }
            objArr[1] = "kotlin/reflect/jvm/internal/impl/serialization/deserialization/ErrorReporter$1";
            if (i10 != 2) {
                objArr[2] = "reportIncompleteHierarchy";
            } else {
                objArr[2] = "reportCannotInferVisibility";
            }
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
        }

        @Override // cd.ErrorReporter
        public void a(ClassDescriptor classDescriptor, List<String> list) {
            if (classDescriptor == null) {
                c(0);
            }
            if (list == null) {
                c(1);
            }
        }

        @Override // cd.ErrorReporter
        public void b(CallableMemberDescriptor callableMemberDescriptor) {
            if (callableMemberDescriptor == null) {
                c(2);
            }
        }
    }

    void a(ClassDescriptor classDescriptor, List<String> list);

    void b(CallableMemberDescriptor callableMemberDescriptor);
}
