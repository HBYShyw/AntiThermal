package db;

import java.util.Random;

/* compiled from: PlatformRandom.kt */
/* loaded from: classes2.dex */
public abstract class a extends Random {
    @Override // db.Random
    public int b() {
        return c().nextInt();
    }

    public abstract Random c();
}
