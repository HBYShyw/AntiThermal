package me;

/* compiled from: ForwardingSink.kt */
/* renamed from: me.h, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink sink) {
        za.k.e(sink, "delegate");
        this.delegate = sink;
    }

    /* renamed from: -deprecated_delegate, reason: not valid java name */
    public final Sink m28deprecated_delegate() {
        return this.delegate;
    }

    @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.delegate.close();
    }

    public final Sink delegate() {
        return this.delegate;
    }

    @Override // me.Sink, java.io.Flushable
    public void flush() {
        this.delegate.flush();
    }

    @Override // me.Sink
    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public String toString() {
        return getClass().getSimpleName() + '(' + this.delegate + ')';
    }

    @Override // me.Sink
    public void write(d dVar, long j10) {
        za.k.e(dVar, "source");
        this.delegate.write(dVar, j10);
    }
}
