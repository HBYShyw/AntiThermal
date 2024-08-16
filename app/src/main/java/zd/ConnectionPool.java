package zd;

import de.TaskRunner;
import ee.RealConnectionPool;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;

/* compiled from: ConnectionPool.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0011\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0007\u0010\bB!\b\u0016\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\u0006\u0010\u000e\u001a\u00020\r¢\u0006\u0004\b\u0007\u0010\u000fB\t\b\u0016¢\u0006\u0004\b\u0007\u0010\u0010R\u001a\u0010\u0003\u001a\u00020\u00028\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lzd/k;", "", "Lee/g;", "delegate", "Lee/g;", "a", "()Lee/g;", "<init>", "(Lee/g;)V", "", "maxIdleConnections", "", "keepAliveDuration", "Ljava/util/concurrent/TimeUnit;", "timeUnit", "(IJLjava/util/concurrent/TimeUnit;)V", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConnectionPool {

    /* renamed from: a, reason: collision with root package name */
    private final RealConnectionPool f20655a;

    public ConnectionPool(RealConnectionPool realConnectionPool) {
        za.k.e(realConnectionPool, "delegate");
        this.f20655a = realConnectionPool;
    }

    /* renamed from: a, reason: from getter */
    public final RealConnectionPool getF20655a() {
        return this.f20655a;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ConnectionPool(int i10, long j10, TimeUnit timeUnit) {
        this(new RealConnectionPool(TaskRunner.f10944i, i10, j10, timeUnit));
        za.k.e(timeUnit, "timeUnit");
    }

    public ConnectionPool() {
        this(5, 5L, TimeUnit.MINUTES);
    }
}
