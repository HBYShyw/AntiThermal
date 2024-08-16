package me;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import za.DefaultConstructorMarker;

/* compiled from: Timeout.kt */
/* renamed from: me.b0, reason: use source file name */
/* loaded from: classes2.dex */
public class Timeout {

    /* renamed from: d, reason: collision with root package name */
    public static final b f15466d = new b(null);

    /* renamed from: e, reason: collision with root package name */
    public static final Timeout f15467e = new a();

    /* renamed from: a, reason: collision with root package name */
    private boolean f15468a;

    /* renamed from: b, reason: collision with root package name */
    private long f15469b;

    /* renamed from: c, reason: collision with root package name */
    private long f15470c;

    /* compiled from: Timeout.kt */
    /* renamed from: me.b0$a */
    /* loaded from: classes2.dex */
    public static final class a extends Timeout {
        a() {
        }

        @Override // me.Timeout
        public Timeout d(long j10) {
            return this;
        }

        @Override // me.Timeout
        public void f() {
        }

        @Override // me.Timeout
        public Timeout g(long j10, TimeUnit timeUnit) {
            za.k.e(timeUnit, "unit");
            return this;
        }
    }

    /* compiled from: Timeout.kt */
    /* renamed from: me.b0$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public Timeout a() {
        this.f15468a = false;
        return this;
    }

    public Timeout b() {
        this.f15470c = 0L;
        return this;
    }

    public long c() {
        if (this.f15468a) {
            return this.f15469b;
        }
        throw new IllegalStateException("No deadline".toString());
    }

    public Timeout d(long j10) {
        this.f15468a = true;
        this.f15469b = j10;
        return this;
    }

    public boolean e() {
        return this.f15468a;
    }

    public void f() {
        if (!Thread.currentThread().isInterrupted()) {
            if (this.f15468a && this.f15469b - System.nanoTime() <= 0) {
                throw new InterruptedIOException("deadline reached");
            }
            return;
        }
        throw new InterruptedIOException("interrupted");
    }

    public Timeout g(long j10, TimeUnit timeUnit) {
        za.k.e(timeUnit, "unit");
        if (j10 >= 0) {
            this.f15470c = timeUnit.toNanos(j10);
            return this;
        }
        throw new IllegalArgumentException(("timeout < 0: " + j10).toString());
    }

    public long h() {
        return this.f15470c;
    }
}
