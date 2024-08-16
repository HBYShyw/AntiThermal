package me;

import java.nio.ByteBuffer;

/* compiled from: RealBufferedSink.kt */
/* renamed from: me.t, reason: use source file name */
/* loaded from: classes2.dex */
public final class RealBufferedSink implements BufferedSink {

    /* renamed from: e, reason: collision with root package name */
    public final Sink f15523e;

    /* renamed from: f, reason: collision with root package name */
    public final d f15524f;

    /* renamed from: g, reason: collision with root package name */
    public boolean f15525g;

    public RealBufferedSink(Sink sink) {
        za.k.e(sink, "sink");
        this.f15523e = sink;
        this.f15524f = new d();
    }

    @Override // me.BufferedSink
    public BufferedSink E(String str) {
        za.k.e(str, "string");
        if (!this.f15525g) {
            this.f15524f.E(str);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public BufferedSink F(g gVar) {
        za.k.e(gVar, "byteString");
        if (!this.f15525g) {
            this.f15524f.F(gVar);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public BufferedSink O(byte[] bArr, int i10, int i11) {
        za.k.e(bArr, "source");
        if (!this.f15525g) {
            this.f15524f.O(bArr, i10, i11);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public BufferedSink R(String str, int i10, int i11) {
        za.k.e(str, "string");
        if (!this.f15525g) {
            this.f15524f.R(str, i10, i11);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public BufferedSink T(long j10) {
        if (!this.f15525g) {
            this.f15524f.T(j10);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public d a() {
        return this.f15524f;
    }

    public BufferedSink b() {
        if (!this.f15525g) {
            long u7 = this.f15524f.u();
            if (u7 > 0) {
                this.f15523e.write(this.f15524f, u7);
            }
            return this;
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f15525g) {
            return;
        }
        Throwable th = null;
        try {
            if (this.f15524f.v0() > 0) {
                Sink sink = this.f15523e;
                d dVar = this.f15524f;
                sink.write(dVar, dVar.v0());
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            this.f15523e.close();
        } catch (Throwable th3) {
            if (th == null) {
                th = th3;
            }
        }
        this.f15525g = true;
        if (th != null) {
            throw th;
        }
    }

    @Override // me.BufferedSink
    public BufferedSink f0(byte[] bArr) {
        za.k.e(bArr, "source");
        if (!this.f15525g) {
            this.f15524f.f0(bArr);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink, me.Sink, java.io.Flushable
    public void flush() {
        if (!this.f15525g) {
            if (this.f15524f.v0() > 0) {
                Sink sink = this.f15523e;
                d dVar = this.f15524f;
                sink.write(dVar, dVar.v0());
            }
            this.f15523e.flush();
            return;
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return !this.f15525g;
    }

    @Override // me.BufferedSink
    public BufferedSink k(int i10) {
        if (!this.f15525g) {
            this.f15524f.k(i10);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public BufferedSink p(int i10) {
        if (!this.f15525g) {
            this.f15524f.p(i10);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSink
    public BufferedSink t(int i10) {
        if (!this.f15525g) {
            this.f15524f.t(i10);
            return b();
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.Sink
    public Timeout timeout() {
        return this.f15523e.timeout();
    }

    public String toString() {
        return "buffer(" + this.f15523e + ')';
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) {
        za.k.e(byteBuffer, "source");
        if (!this.f15525g) {
            int write = this.f15524f.write(byteBuffer);
            b();
            return write;
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.Sink
    public void write(d dVar, long j10) {
        za.k.e(dVar, "source");
        if (!this.f15525g) {
            this.f15524f.write(dVar, j10);
            b();
            return;
        }
        throw new IllegalStateException("closed".toString());
    }
}
