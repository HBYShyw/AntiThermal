package me;

import java.io.OutputStream;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JvmOkio.kt */
/* loaded from: classes2.dex */
public final class r implements Sink {

    /* renamed from: e, reason: collision with root package name */
    private final OutputStream f15515e;

    /* renamed from: f, reason: collision with root package name */
    private final Timeout f15516f;

    public r(OutputStream outputStream, Timeout timeout) {
        za.k.e(outputStream, "out");
        za.k.e(timeout, "timeout");
        this.f15515e = outputStream;
        this.f15516f = timeout;
    }

    @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f15515e.close();
    }

    @Override // me.Sink, java.io.Flushable
    public void flush() {
        this.f15515e.flush();
    }

    @Override // me.Sink
    public Timeout timeout() {
        return this.f15516f;
    }

    public String toString() {
        return "sink(" + this.f15515e + ')';
    }

    @Override // me.Sink
    public void write(d dVar, long j10) {
        za.k.e(dVar, "source");
        b.b(dVar.v0(), 0L, j10);
        while (j10 > 0) {
            this.f15516f.f();
            Segment segment = dVar.f15484e;
            za.k.b(segment);
            int min = (int) Math.min(j10, segment.f15533c - segment.f15532b);
            this.f15515e.write(segment.f15531a, segment.f15532b, min);
            segment.f15532b += min;
            long j11 = min;
            j10 -= j11;
            dVar.u0(dVar.v0() - j11);
            if (segment.f15532b == segment.f15533c) {
                dVar.f15484e = segment.b();
                SegmentPool.b(segment);
            }
        }
    }
}
