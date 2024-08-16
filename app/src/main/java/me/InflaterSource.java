package me;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* compiled from: InflaterSource.kt */
/* renamed from: me.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class InflaterSource implements Source {

    /* renamed from: e, reason: collision with root package name */
    private final BufferedSource f15505e;

    /* renamed from: f, reason: collision with root package name */
    private final Inflater f15506f;

    /* renamed from: g, reason: collision with root package name */
    private int f15507g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f15508h;

    public InflaterSource(BufferedSource bufferedSource, Inflater inflater) {
        za.k.e(bufferedSource, "source");
        za.k.e(inflater, "inflater");
        this.f15505e = bufferedSource;
        this.f15506f = inflater;
    }

    private final void m() {
        int i10 = this.f15507g;
        if (i10 == 0) {
            return;
        }
        int remaining = i10 - this.f15506f.getRemaining();
        this.f15507g -= remaining;
        this.f15505e.V(remaining);
    }

    public final long b(d dVar, long j10) {
        za.k.e(dVar, "sink");
        if (j10 >= 0) {
            if (!(!this.f15508h)) {
                throw new IllegalStateException("closed".toString());
            }
            if (j10 == 0) {
                return 0L;
            }
            try {
                Segment y02 = dVar.y0(1);
                int min = (int) Math.min(j10, 8192 - y02.f15533c);
                c();
                int inflate = this.f15506f.inflate(y02.f15531a, y02.f15533c, min);
                m();
                if (inflate > 0) {
                    y02.f15533c += inflate;
                    long j11 = inflate;
                    dVar.u0(dVar.v0() + j11);
                    return j11;
                }
                if (y02.f15532b == y02.f15533c) {
                    dVar.f15484e = y02.b();
                    SegmentPool.b(y02);
                }
                return 0L;
            } catch (DataFormatException e10) {
                throw new IOException(e10);
            }
        }
        throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
    }

    public final boolean c() {
        if (!this.f15506f.needsInput()) {
            return false;
        }
        if (this.f15505e.s()) {
            return true;
        }
        Segment segment = this.f15505e.a().f15484e;
        za.k.b(segment);
        int i10 = segment.f15533c;
        int i11 = segment.f15532b;
        int i12 = i10 - i11;
        this.f15507g = i12;
        this.f15506f.setInput(segment.f15531a, i11, i12);
        return false;
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.f15508h) {
            return;
        }
        this.f15506f.end();
        this.f15508h = true;
        this.f15505e.close();
    }

    @Override // me.Source
    public long read(d dVar, long j10) {
        za.k.e(dVar, "sink");
        do {
            long b10 = b(dVar, j10);
            if (b10 > 0) {
                return b10;
            }
            if (this.f15506f.finished() || this.f15506f.needsDictionary()) {
                return -1L;
            }
        } while (!this.f15505e.s());
        throw new EOFException("source exhausted prematurely");
    }

    @Override // me.Source
    public Timeout timeout() {
        return this.f15505e.timeout();
    }
}
