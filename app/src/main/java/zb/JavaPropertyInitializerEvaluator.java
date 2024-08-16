package zb;

import fc.n;
import pb.PropertyDescriptor;
import za.k;

/* compiled from: JavaPropertyInitializerEvaluator.kt */
/* renamed from: zb.f, reason: use source file name */
/* loaded from: classes2.dex */
public interface JavaPropertyInitializerEvaluator {

    /* compiled from: JavaPropertyInitializerEvaluator.kt */
    /* renamed from: zb.f$a */
    /* loaded from: classes2.dex */
    public static final class a implements JavaPropertyInitializerEvaluator {

        /* renamed from: a, reason: collision with root package name */
        public static final a f20403a = new a();

        private a() {
        }

        @Override // zb.JavaPropertyInitializerEvaluator
        public uc.g<?> a(n nVar, PropertyDescriptor propertyDescriptor) {
            k.e(nVar, "field");
            k.e(propertyDescriptor, "descriptor");
            return null;
        }
    }

    uc.g<?> a(n nVar, PropertyDescriptor propertyDescriptor);
}
