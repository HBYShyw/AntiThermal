package ub;

import pb.b1;
import vb.ReflectJavaElement;

/* compiled from: RuntimeSourceElementFactory.kt */
/* renamed from: ub.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class RuntimeSourceElementFactory implements ec.b {

    /* renamed from: a, reason: collision with root package name */
    public static final RuntimeSourceElementFactory f18984a = new RuntimeSourceElementFactory();

    /* compiled from: RuntimeSourceElementFactory.kt */
    /* renamed from: ub.l$a */
    /* loaded from: classes2.dex */
    public static final class a implements ec.a {

        /* renamed from: b, reason: collision with root package name */
        private final ReflectJavaElement f18985b;

        public a(ReflectJavaElement reflectJavaElement) {
            za.k.e(reflectJavaElement, "javaElement");
            this.f18985b = reflectJavaElement;
        }

        @Override // pb.SourceElement
        public b1 a() {
            b1 b1Var = b1.f16671a;
            za.k.d(b1Var, "NO_SOURCE_FILE");
            return b1Var;
        }

        @Override // ec.a
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public ReflectJavaElement b() {
            return this.f18985b;
        }

        public String toString() {
            return a.class.getName() + ": " + b();
        }
    }

    private RuntimeSourceElementFactory() {
    }

    @Override // ec.b
    public ec.a a(fc.l lVar) {
        za.k.e(lVar, "javaElement");
        return new a((ReflectJavaElement) lVar);
    }
}
