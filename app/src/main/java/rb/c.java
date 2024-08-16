package rb;

import pb.ClassDescriptor;
import pb.SimpleFunctionDescriptor;
import za.k;

/* compiled from: PlatformDependentDeclarationFilter.kt */
/* loaded from: classes2.dex */
public interface c {

    /* compiled from: PlatformDependentDeclarationFilter.kt */
    /* loaded from: classes2.dex */
    public static final class a implements c {

        /* renamed from: a, reason: collision with root package name */
        public static final a f17689a = new a();

        private a() {
        }

        @Override // rb.c
        public boolean d(ClassDescriptor classDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor) {
            k.e(classDescriptor, "classDescriptor");
            k.e(simpleFunctionDescriptor, "functionDescriptor");
            return true;
        }
    }

    /* compiled from: PlatformDependentDeclarationFilter.kt */
    /* loaded from: classes2.dex */
    public static final class b implements c {

        /* renamed from: a, reason: collision with root package name */
        public static final b f17690a = new b();

        private b() {
        }

        @Override // rb.c
        public boolean d(ClassDescriptor classDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor) {
            k.e(classDescriptor, "classDescriptor");
            k.e(simpleFunctionDescriptor, "functionDescriptor");
            return !simpleFunctionDescriptor.i().a(d.a());
        }
    }

    boolean d(ClassDescriptor classDescriptor, SimpleFunctionDescriptor simpleFunctionDescriptor);
}
