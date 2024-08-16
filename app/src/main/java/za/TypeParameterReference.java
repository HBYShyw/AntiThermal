package za;

import gb.KTypeParameter;
import gb.KVariance;

/* compiled from: TypeParameterReference.kt */
/* renamed from: za.f0, reason: use source file name */
/* loaded from: classes2.dex */
public final class TypeParameterReference implements KTypeParameter {

    /* renamed from: e, reason: collision with root package name */
    public static final a f20367e = new a(null);

    /* compiled from: TypeParameterReference.kt */
    /* renamed from: za.f0$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: TypeParameterReference.kt */
        /* renamed from: za.f0$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public /* synthetic */ class C0118a {

            /* renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f20368a;

            static {
                int[] iArr = new int[KVariance.values().length];
                try {
                    iArr[KVariance.INVARIANT.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[KVariance.IN.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[KVariance.OUT.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                f20368a = iArr;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(KTypeParameter kTypeParameter) {
            k.e(kTypeParameter, "typeParameter");
            StringBuilder sb2 = new StringBuilder();
            int i10 = C0118a.f20368a[kTypeParameter.s().ordinal()];
            if (i10 == 2) {
                sb2.append("in ");
            } else if (i10 == 3) {
                sb2.append("out ");
            }
            sb2.append(kTypeParameter.getName());
            String sb3 = sb2.toString();
            k.d(sb3, "StringBuilder().apply(builderAction).toString()");
            return sb3;
        }
    }
}
