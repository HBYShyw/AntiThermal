package me;

/* compiled from: ForwardingSource.kt */
/* renamed from: me.i, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ForwardingSource implements Source {

    /* renamed from: e, reason: collision with root package name */
    private final Source f15498e;

    public ForwardingSource(Source source) {
        za.k.e(source, "delegate");
        this.f15498e = source;
    }

    public final Source b() {
        return this.f15498e;
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f15498e.close();
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.f15498e.timeout();
    }

    public String toString() {
        return getClass().getSimpleName() + '(' + this.f15498e + ')';
    }
}
