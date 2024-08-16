package me;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

/* compiled from: GzipSource.kt */
/* renamed from: me.k, reason: use source file name */
/* loaded from: classes2.dex */
public final class GzipSource implements Source {

    /* renamed from: e, reason: collision with root package name */
    private byte f15500e;

    /* renamed from: f, reason: collision with root package name */
    private final RealBufferedSource f15501f;

    /* renamed from: g, reason: collision with root package name */
    private final Inflater f15502g;

    /* renamed from: h, reason: collision with root package name */
    private final InflaterSource f15503h;

    /* renamed from: i, reason: collision with root package name */
    private final CRC32 f15504i;

    public GzipSource(Source source) {
        za.k.e(source, "source");
        RealBufferedSource realBufferedSource = new RealBufferedSource(source);
        this.f15501f = realBufferedSource;
        Inflater inflater = new Inflater(true);
        this.f15502g = inflater;
        this.f15503h = new InflaterSource(realBufferedSource, inflater);
        this.f15504i = new CRC32();
    }

    private final void b(String str, int i10, int i11) {
        if (i11 == i10) {
            return;
        }
        String format = String.format("%s: actual 0x%08x != expected 0x%08x", Arrays.copyOf(new Object[]{str, Integer.valueOf(i11), Integer.valueOf(i10)}, 3));
        za.k.d(format, "format(this, *args)");
        throw new IOException(format);
    }

    private final void c() {
        this.f15501f.p0(10L);
        byte L = this.f15501f.f15527f.L(3L);
        boolean z10 = ((L >> 1) & 1) == 1;
        if (z10) {
            u(this.f15501f.f15527f, 0L, 10L);
        }
        b("ID1ID2", 8075, this.f15501f.k0());
        this.f15501f.V(8L);
        if (((L >> 2) & 1) == 1) {
            this.f15501f.p0(2L);
            if (z10) {
                u(this.f15501f.f15527f, 0L, 2L);
            }
            long j02 = this.f15501f.f15527f.j0() & 65535;
            this.f15501f.p0(j02);
            if (z10) {
                u(this.f15501f.f15527f, 0L, j02);
            }
            this.f15501f.V(j02);
        }
        if (((L >> 3) & 1) == 1) {
            long b10 = this.f15501f.b((byte) 0);
            if (b10 != -1) {
                if (z10) {
                    u(this.f15501f.f15527f, 0L, b10 + 1);
                }
                this.f15501f.V(b10 + 1);
            } else {
                throw new EOFException();
            }
        }
        if (((L >> 4) & 1) == 1) {
            long b11 = this.f15501f.b((byte) 0);
            if (b11 != -1) {
                if (z10) {
                    u(this.f15501f.f15527f, 0L, b11 + 1);
                }
                this.f15501f.V(b11 + 1);
            } else {
                throw new EOFException();
            }
        }
        if (z10) {
            b("FHCRC", this.f15501f.w(), (short) this.f15504i.getValue());
            this.f15504i.reset();
        }
    }

    private final void m() {
        b("CRC", this.f15501f.v(), (int) this.f15504i.getValue());
        b("ISIZE", this.f15501f.v(), (int) this.f15502g.getBytesWritten());
    }

    private final void u(d dVar, long j10, long j11) {
        Segment segment = dVar.f15484e;
        za.k.b(segment);
        while (true) {
            int i10 = segment.f15533c;
            int i11 = segment.f15532b;
            if (j10 < i10 - i11) {
                break;
            }
            j10 -= i10 - i11;
            segment = segment.f15536f;
            za.k.b(segment);
        }
        while (j11 > 0) {
            int min = (int) Math.min(segment.f15533c - r6, j11);
            this.f15504i.update(segment.f15531a, (int) (segment.f15532b + j10), min);
            j11 -= min;
            segment = segment.f15536f;
            za.k.b(segment);
            j10 = 0;
        }
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.f15503h.close();
    }

    @Override // me.Source
    public long read(d dVar, long j10) {
        za.k.e(dVar, "sink");
        if (!(j10 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
        }
        if (j10 == 0) {
            return 0L;
        }
        if (this.f15500e == 0) {
            c();
            this.f15500e = (byte) 1;
        }
        if (this.f15500e == 1) {
            long v02 = dVar.v0();
            long read = this.f15503h.read(dVar, j10);
            if (read != -1) {
                u(dVar, v02, read);
                return read;
            }
            this.f15500e = (byte) 2;
        }
        if (this.f15500e == 2) {
            m();
            this.f15500e = (byte) 3;
            if (!this.f15501f.s()) {
                throw new IOException("gzip finished without exhausting source");
            }
        }
        return -1L;
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.f15501f.timeout();
    }
}
