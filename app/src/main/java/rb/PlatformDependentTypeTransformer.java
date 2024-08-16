package rb;

import gd.o0;
import oc.ClassId;
import za.k;

/* compiled from: PlatformDependentTypeTransformer.kt */
/* renamed from: rb.e, reason: use source file name */
/* loaded from: classes2.dex */
public interface PlatformDependentTypeTransformer {

    /* compiled from: PlatformDependentTypeTransformer.kt */
    /* renamed from: rb.e$a */
    /* loaded from: classes2.dex */
    public static final class a implements PlatformDependentTypeTransformer {

        /* renamed from: a, reason: collision with root package name */
        public static final a f17692a = new a();

        private a() {
        }

        @Override // rb.PlatformDependentTypeTransformer
        public o0 a(ClassId classId, o0 o0Var) {
            k.e(classId, "classId");
            k.e(o0Var, "computedType");
            return o0Var;
        }
    }

    o0 a(ClassId classId, o0 o0Var);
}
