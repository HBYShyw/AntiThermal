package me;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JvmOkio.kt */
/* loaded from: classes2.dex */
public class m implements Source {

    /* renamed from: e, reason: collision with root package name */
    private final InputStream f15509e;

    /* renamed from: f, reason: collision with root package name */
    private final Timeout f15510f;

    public m(InputStream inputStream, Timeout timeout) {
        za.k.e(inputStream, "input");
        za.k.e(timeout, "timeout");
        this.f15509e = inputStream;
        this.f15510f = timeout;
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f15509e.close();
    }

    @Override // me.Source
    public long read(d dVar, long j10) {
        za.k.e(dVar, "sink");
        if (j10 == 0) {
            return 0L;
        }
        if (j10 >= 0) {
            try {
                this.f15510f.f();
                Segment y02 = dVar.y0(1);
                int read = this.f15509e.read(y02.f15531a, y02.f15533c, (int) Math.min(j10, 8192 - y02.f15533c));
                if (read == -1) {
                    if (y02.f15532b != y02.f15533c) {
                        return -1L;
                    }
                    dVar.f15484e = y02.b();
                    SegmentPool.b(y02);
                    return -1L;
                }
                y02.f15533c += read;
                long j11 = read;
                dVar.u0(dVar.v0() + j11);
                return j11;
            } catch (AssertionError e10) {
                if (n.c(e10)) {
                    throw new IOException(e10);
                }
                throw e10;
            }
        }
        throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.f15510f;
    }

    public String toString() {
        return "source(" + this.f15509e + ')';
    }
}
