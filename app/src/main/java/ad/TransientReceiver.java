package ad;

import gd.g0;

/* compiled from: TransientReceiver.java */
/* renamed from: ad.j, reason: use source file name */
/* loaded from: classes2.dex */
public class TransientReceiver extends AbstractReceiverValue {
    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TransientReceiver(g0 g0Var) {
        this(g0Var, null);
        if (g0Var == null) {
            c(0);
        }
    }

    private static /* synthetic */ void c(int i10) {
        Object[] objArr = new Object[3];
        if (i10 != 2) {
            objArr[0] = "type";
        } else {
            objArr[0] = "newType";
        }
        objArr[1] = "kotlin/reflect/jvm/internal/impl/resolve/scopes/receivers/TransientReceiver";
        if (i10 != 2) {
            objArr[2] = "<init>";
        } else {
            objArr[2] = "replaceType";
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", objArr));
    }

    public String toString() {
        return "{Transient} : " + getType();
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private TransientReceiver(g0 g0Var, ReceiverValue receiverValue) {
        super(g0Var, receiverValue);
        if (g0Var == null) {
            c(1);
        }
    }
}
