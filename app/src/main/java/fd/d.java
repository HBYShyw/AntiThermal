package fd;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import za.DefaultConstructorMarker;

/* compiled from: locks.kt */
/* loaded from: classes2.dex */
public class d implements k {

    /* renamed from: b, reason: collision with root package name */
    private final Lock f11421b;

    public d(Lock lock) {
        za.k.e(lock, "lock");
        this.f11421b = lock;
    }

    @Override // fd.k
    public void a() {
        this.f11421b.unlock();
    }

    @Override // fd.k
    public void b() {
        this.f11421b.lock();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Lock c() {
        return this.f11421b;
    }

    public /* synthetic */ d(Lock lock, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? new ReentrantLock() : lock);
    }
}
