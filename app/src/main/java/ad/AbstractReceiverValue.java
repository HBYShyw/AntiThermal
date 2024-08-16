package ad;

import gd.g0;

/* compiled from: AbstractReceiverValue.java */
/* renamed from: ad.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class AbstractReceiverValue implements ReceiverValue {

    /* renamed from: a, reason: collision with root package name */
    protected final g0 f224a;

    /* renamed from: b, reason: collision with root package name */
    private final ReceiverValue f225b;

    public AbstractReceiverValue(g0 g0Var, ReceiverValue receiverValue) {
        if (g0Var == null) {
            c(0);
        }
        this.f224a = g0Var;
        this.f225b = receiverValue == null ? this : receiverValue;
    }

    private static /* synthetic */ void c(int i10) {
        String str = (i10 == 1 || i10 == 2) ? "@NotNull method %s.%s must not return null" : "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        Object[] objArr = new Object[(i10 == 1 || i10 == 2) ? 2 : 3];
        if (i10 == 1 || i10 == 2) {
            objArr[0] = "kotlin/reflect/jvm/internal/impl/resolve/scopes/receivers/AbstractReceiverValue";
        } else {
            objArr[0] = "receiverType";
        }
        if (i10 == 1) {
            objArr[1] = "getType";
        } else if (i10 != 2) {
            objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/scopes/receivers/AbstractReceiverValue";
        } else {
            objArr[1] = "getOriginal";
        }
        if (i10 != 1 && i10 != 2) {
            objArr[2] = "<init>";
        }
        String format = String.format(str, objArr);
        if (i10 != 1 && i10 != 2) {
            throw new IllegalArgumentException(format);
        }
        throw new IllegalStateException(format);
    }

    @Override // ad.ReceiverValue
    public g0 getType() {
        g0 g0Var = this.f224a;
        if (g0Var == null) {
            c(1);
        }
        return g0Var;
    }
}
