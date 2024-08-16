package eb;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import za.k;

/* compiled from: PlatformThreadLocalRandom.kt */
/* renamed from: eb.a, reason: use source file name */
/* loaded from: classes2.dex */
public final class PlatformThreadLocalRandom extends db.a {
    @Override // db.a
    public Random c() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        k.d(current, "current()");
        return current;
    }
}
