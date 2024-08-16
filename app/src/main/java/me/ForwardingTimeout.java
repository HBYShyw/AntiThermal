package me;

import java.util.concurrent.TimeUnit;

/* compiled from: ForwardingTimeout.kt */
/* renamed from: me.j, reason: use source file name */
/* loaded from: classes2.dex */
public class ForwardingTimeout extends Timeout {

    /* renamed from: f, reason: collision with root package name */
    private Timeout f15499f;

    public ForwardingTimeout(Timeout timeout) {
        za.k.e(timeout, "delegate");
        this.f15499f = timeout;
    }

    @Override // me.Timeout
    public Timeout a() {
        return this.f15499f.a();
    }

    @Override // me.Timeout
    public Timeout b() {
        return this.f15499f.b();
    }

    @Override // me.Timeout
    public long c() {
        return this.f15499f.c();
    }

    @Override // me.Timeout
    public Timeout d(long j10) {
        return this.f15499f.d(j10);
    }

    @Override // me.Timeout
    public boolean e() {
        return this.f15499f.e();
    }

    @Override // me.Timeout
    public void f() {
        this.f15499f.f();
    }

    @Override // me.Timeout
    public Timeout g(long j10, TimeUnit timeUnit) {
        za.k.e(timeUnit, "unit");
        return this.f15499f.g(j10, timeUnit);
    }

    public final Timeout i() {
        return this.f15499f;
    }

    public final ForwardingTimeout j(Timeout timeout) {
        za.k.e(timeout, "delegate");
        this.f15499f = timeout;
        return this;
    }
}
