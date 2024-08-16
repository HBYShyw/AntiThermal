package hb;

import za.k;

/* compiled from: exceptions.kt */
/* loaded from: classes2.dex */
public final class a extends Exception {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a(IllegalAccessException illegalAccessException) {
        super(illegalAccessException);
        k.e(illegalAccessException, "cause");
    }
}
