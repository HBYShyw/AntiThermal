package me;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* compiled from: RealBufferedSource.kt */
/* renamed from: me.u, reason: use source file name */
/* loaded from: classes2.dex */
public final class RealBufferedSource implements BufferedSource {

    /* renamed from: e, reason: collision with root package name */
    public final Source f15526e;

    /* renamed from: f, reason: collision with root package name */
    public final d f15527f;

    /* renamed from: g, reason: collision with root package name */
    public boolean f15528g;

    public RealBufferedSource(Source source) {
        za.k.e(source, "source");
        this.f15526e = source;
        this.f15527f = new d();
    }

    @Override // me.BufferedSource
    public long I(g gVar) {
        za.k.e(gVar, "bytes");
        return m(gVar, 0L);
    }

    @Override // me.BufferedSource
    public String J(Charset charset) {
        za.k.e(charset, "charset");
        this.f15527f.C0(this.f15526e);
        return this.f15527f.J(charset);
    }

    @Override // me.BufferedSource
    public byte M() {
        p0(1L);
        return this.f15527f.M();
    }

    @Override // me.BufferedSource
    public void V(long j10) {
        if (!(!this.f15528g)) {
            throw new IllegalStateException("closed".toString());
        }
        while (j10 > 0) {
            if (this.f15527f.v0() == 0 && this.f15526e.read(this.f15527f, 8192L) == -1) {
                throw new EOFException();
            }
            long min = Math.min(j10, this.f15527f.v0());
            this.f15527f.V(min);
            j10 -= min;
        }
    }

    @Override // me.BufferedSource
    public boolean W(long j10) {
        if (j10 >= 0) {
            if (!(!this.f15528g)) {
                throw new IllegalStateException("closed".toString());
            }
            while (this.f15527f.v0() < j10) {
                if (this.f15526e.read(this.f15527f, 8192L) == -1) {
                    return false;
                }
            }
            return true;
        }
        throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
    }

    @Override // me.BufferedSource
    public String Z() {
        return x(Long.MAX_VALUE);
    }

    @Override // me.BufferedSource, me.BufferedSink
    public d a() {
        return this.f15527f;
    }

    public long b(byte b10) {
        return c(b10, 0L, Long.MAX_VALUE);
    }

    @Override // me.BufferedSource
    public byte[] b0(long j10) {
        p0(j10);
        return this.f15527f.b0(j10);
    }

    public long c(byte b10, long j10, long j11) {
        if (!(!this.f15528g)) {
            throw new IllegalStateException("closed".toString());
        }
        if (!(0 <= j10 && j10 <= j11)) {
            throw new IllegalArgumentException(("fromIndex=" + j10 + " toIndex=" + j11).toString());
        }
        while (j10 < j11) {
            long N = this.f15527f.N(b10, j10, j11);
            if (N != -1) {
                return N;
            }
            long v02 = this.f15527f.v0();
            if (v02 >= j11 || this.f15526e.read(this.f15527f, 8192L) == -1) {
                return -1L;
            }
            j10 = Math.max(j10, v02);
        }
        return -1L;
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f15528g) {
            return;
        }
        this.f15528g = true;
        this.f15526e.close();
        this.f15527f.b();
    }

    @Override // me.BufferedSource
    public d g() {
        return this.f15527f;
    }

    @Override // me.BufferedSource
    public long g0(g gVar) {
        za.k.e(gVar, "targetBytes");
        return u(gVar, 0L);
    }

    @Override // me.BufferedSource
    public g h(long j10) {
        p0(j10);
        return this.f15527f.h(j10);
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return !this.f15528g;
    }

    @Override // me.BufferedSource
    public short k0() {
        p0(2L);
        return this.f15527f.k0();
    }

    @Override // me.BufferedSource
    public long l0(Sink sink) {
        za.k.e(sink, "sink");
        long j10 = 0;
        while (this.f15526e.read(this.f15527f, 8192L) != -1) {
            long u7 = this.f15527f.u();
            if (u7 > 0) {
                j10 += u7;
                sink.write(this.f15527f, u7);
            }
        }
        if (this.f15527f.v0() <= 0) {
            return j10;
        }
        long v02 = j10 + this.f15527f.v0();
        d dVar = this.f15527f;
        sink.write(dVar, dVar.v0());
        return v02;
    }

    public long m(g gVar, long j10) {
        za.k.e(gVar, "bytes");
        if (!(!this.f15528g)) {
            throw new IllegalStateException("closed".toString());
        }
        while (true) {
            long S = this.f15527f.S(gVar, j10);
            if (S != -1) {
                return S;
            }
            long v02 = this.f15527f.v0();
            if (this.f15526e.read(this.f15527f, 8192L) == -1) {
                return -1L;
            }
            j10 = Math.max(j10, (v02 - gVar.t()) + 1);
        }
    }

    @Override // me.BufferedSource
    public BufferedSource n0() {
        return n.b(new PeekSource(this));
    }

    @Override // me.BufferedSource
    public int o() {
        p0(4L);
        return this.f15527f.o();
    }

    @Override // me.BufferedSource
    public void p0(long j10) {
        if (!W(j10)) {
            throw new EOFException();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0031, code lost:
    
        if (r0 == 0) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0034, code lost:
    
        r0 = new java.lang.StringBuilder();
        r0.append("Expected leading [0-9a-fA-F] character but was 0x");
        r1 = sd.CharJVM.a(16);
        r1 = sd.CharJVM.a(r1);
        r1 = java.lang.Integer.toString(r2, r1);
        za.k.d(r1, "toString(this, checkRadix(radix))");
        r0.append(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005d, code lost:
    
        throw new java.lang.NumberFormatException(r0.toString());
     */
    @Override // me.BufferedSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long r0() {
        p0(1L);
        int i10 = 0;
        while (true) {
            int i11 = i10 + 1;
            if (!W(i11)) {
                break;
            }
            byte L = this.f15527f.L(i10);
            if ((L < 48 || L > 57) && ((L < 97 || L > 102) && (L < 65 || L > 70))) {
                break;
            }
            i10 = i11;
        }
        return this.f15527f.r0();
    }

    @Override // me.Source
    public long read(d dVar, long j10) {
        za.k.e(dVar, "sink");
        if (j10 >= 0) {
            if (!(!this.f15528g)) {
                throw new IllegalStateException("closed".toString());
            }
            if (this.f15527f.v0() == 0 && this.f15526e.read(this.f15527f, 8192L) == -1) {
                return -1L;
            }
            return this.f15527f.read(dVar, Math.min(j10, this.f15527f.v0()));
        }
        throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
    }

    @Override // me.BufferedSource
    public boolean s() {
        if (!this.f15528g) {
            return this.f15527f.s() && this.f15526e.read(this.f15527f, 8192L) == -1;
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // me.BufferedSource
    public InputStream s0() {
        return new a();
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.f15526e.timeout();
    }

    public String toString() {
        return "buffer(" + this.f15526e + ')';
    }

    public long u(g gVar, long j10) {
        za.k.e(gVar, "targetBytes");
        if (!(!this.f15528g)) {
            throw new IllegalStateException("closed".toString());
        }
        while (true) {
            long U = this.f15527f.U(gVar, j10);
            if (U != -1) {
                return U;
            }
            long v02 = this.f15527f.v0();
            if (this.f15526e.read(this.f15527f, 8192L) == -1) {
                return -1L;
            }
            j10 = Math.max(j10, v02);
        }
    }

    public int v() {
        p0(4L);
        return this.f15527f.i0();
    }

    public short w() {
        p0(2L);
        return this.f15527f.j0();
    }

    @Override // me.BufferedSource
    public String x(long j10) {
        if (j10 >= 0) {
            long j11 = j10 == Long.MAX_VALUE ? Long.MAX_VALUE : j10 + 1;
            long c10 = c((byte) 10, 0L, j11);
            if (c10 != -1) {
                return ne.a.c(this.f15527f, c10);
            }
            if (j11 < Long.MAX_VALUE && W(j11) && this.f15527f.L(j11 - 1) == 13 && W(1 + j11) && this.f15527f.L(j11) == 10) {
                return ne.a.c(this.f15527f, j11);
            }
            d dVar = new d();
            d dVar2 = this.f15527f;
            dVar2.w(dVar, 0L, Math.min(32, dVar2.v0()));
            throw new EOFException("\\n not found: limit=" + Math.min(this.f15527f.v0(), j10) + " content=" + dVar.e0().j() + (char) 8230);
        }
        throw new IllegalArgumentException(("limit < 0: " + j10).toString());
    }

    @Override // me.BufferedSource
    public int z(q qVar) {
        za.k.e(qVar, "options");
        if (!(!this.f15528g)) {
            throw new IllegalStateException("closed".toString());
        }
        while (true) {
            int d10 = ne.a.d(this.f15527f, qVar, true);
            if (d10 != -2) {
                if (d10 != -1) {
                    this.f15527f.V(qVar.g()[d10].t());
                    return d10;
                }
            } else if (this.f15526e.read(this.f15527f, 8192L) == -1) {
                break;
            }
        }
        return -1;
    }

    /* compiled from: RealBufferedSource.kt */
    /* renamed from: me.u$a */
    /* loaded from: classes2.dex */
    public static final class a extends InputStream {
        a() {
        }

        @Override // java.io.InputStream
        public int available() {
            RealBufferedSource realBufferedSource = RealBufferedSource.this;
            if (!realBufferedSource.f15528g) {
                return (int) Math.min(realBufferedSource.f15527f.v0(), Integer.MAX_VALUE);
            }
            throw new IOException("closed");
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            RealBufferedSource.this.close();
        }

        @Override // java.io.InputStream
        public int read() {
            RealBufferedSource realBufferedSource = RealBufferedSource.this;
            if (!realBufferedSource.f15528g) {
                if (realBufferedSource.f15527f.v0() == 0) {
                    RealBufferedSource realBufferedSource2 = RealBufferedSource.this;
                    if (realBufferedSource2.f15526e.read(realBufferedSource2.f15527f, 8192L) == -1) {
                        return -1;
                    }
                }
                return RealBufferedSource.this.f15527f.M() & 255;
            }
            throw new IOException("closed");
        }

        public String toString() {
            return RealBufferedSource.this + ".inputStream()";
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i10, int i11) {
            za.k.e(bArr, "data");
            if (!RealBufferedSource.this.f15528g) {
                b.b(bArr.length, i10, i11);
                if (RealBufferedSource.this.f15527f.v0() == 0) {
                    RealBufferedSource realBufferedSource = RealBufferedSource.this;
                    if (realBufferedSource.f15526e.read(realBufferedSource.f15527f, 8192L) == -1) {
                        return -1;
                    }
                }
                return RealBufferedSource.this.f15527f.X(bArr, i10, i11);
            }
            throw new IOException("closed");
        }
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) {
        za.k.e(byteBuffer, "sink");
        if (this.f15527f.v0() == 0 && this.f15526e.read(this.f15527f, 8192L) == -1) {
            return -1;
        }
        return this.f15527f.read(byteBuffer);
    }
}
