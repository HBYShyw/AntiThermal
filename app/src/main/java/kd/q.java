package kd;

import gd.Variance;
import ma.NoWhenBranchMatchedException;

/* compiled from: TypeSystemContext.kt */
/* loaded from: classes2.dex */
public final class q {

    /* compiled from: TypeSystemContext.kt */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a {

        /* renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f14298a;

        static {
            int[] iArr = new int[Variance.values().length];
            try {
                iArr[Variance.INVARIANT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[Variance.IN_VARIANCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[Variance.OUT_VARIANCE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f14298a = iArr;
        }
    }

    public static final u a(Variance variance) {
        za.k.e(variance, "<this>");
        int i10 = a.f14298a[variance.ordinal()];
        if (i10 == 1) {
            return u.INV;
        }
        if (i10 == 2) {
            return u.IN;
        }
        if (i10 == 3) {
            return u.OUT;
        }
        throw new NoWhenBranchMatchedException();
    }
}
