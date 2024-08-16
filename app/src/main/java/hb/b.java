package hb;

import za.k;

/* compiled from: exceptions.kt */
/* loaded from: classes2.dex */
public final class b extends Exception {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(IllegalAccessException illegalAccessException) {
        super("Cannot obtain the delegate of a non-accessible property. Use \"isAccessible = true\" to make the property accessible", illegalAccessException);
        k.e(illegalAccessException, "cause");
    }
}
