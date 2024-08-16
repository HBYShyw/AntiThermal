package yb;

import ac.JavaClassDescriptor;

/* compiled from: JavaClassesTracker.kt */
/* renamed from: yb.q, reason: use source file name */
/* loaded from: classes2.dex */
public interface JavaClassesTracker {

    /* compiled from: JavaClassesTracker.kt */
    /* renamed from: yb.q$a */
    /* loaded from: classes2.dex */
    public static final class a implements JavaClassesTracker {

        /* renamed from: a, reason: collision with root package name */
        public static final a f20127a = new a();

        private a() {
        }

        @Override // yb.JavaClassesTracker
        public void a(JavaClassDescriptor javaClassDescriptor) {
            za.k.e(javaClassDescriptor, "classDescriptor");
        }
    }

    void a(JavaClassDescriptor javaClassDescriptor);
}
