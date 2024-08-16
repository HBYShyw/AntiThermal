package db;

import java.util.Random;
import za.k;

/* compiled from: PlatformRandom.kt */
/* loaded from: classes2.dex */
public final class b extends db.a {

    /* renamed from: g, reason: collision with root package name */
    private final a f10891g = new a();

    /* compiled from: PlatformRandom.kt */
    /* loaded from: classes2.dex */
    public static final class a extends ThreadLocal<Random> {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Random initialValue() {
            return new Random();
        }
    }

    @Override // db.a
    public Random c() {
        Random random = this.f10891g.get();
        k.d(random, "implStorage.get()");
        return random;
    }
}
