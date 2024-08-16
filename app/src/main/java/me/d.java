package me;

import java.io.Closeable;
import java.io.EOFException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import kotlin.collections._ArraysJvm;
import sd.Charsets;

/* compiled from: Buffer.kt */
/* loaded from: classes2.dex */
public final class d implements BufferedSource, BufferedSink, Cloneable, ByteChannel {

    /* renamed from: e, reason: collision with root package name */
    public Segment f15484e;

    /* renamed from: f, reason: collision with root package name */
    private long f15485f;

    /* compiled from: Buffer.kt */
    /* loaded from: classes2.dex */
    public static final class a implements Closeable {

        /* renamed from: e, reason: collision with root package name */
        public d f15486e;

        /* renamed from: f, reason: collision with root package name */
        private Segment f15487f;

        /* renamed from: h, reason: collision with root package name */
        public byte[] f15489h;

        /* renamed from: g, reason: collision with root package name */
        public long f15488g = -1;

        /* renamed from: i, reason: collision with root package name */
        public int f15490i = -1;

        /* renamed from: j, reason: collision with root package name */
        public int f15491j = -1;

        public final void b(Segment segment) {
            this.f15487f = segment;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            if (this.f15486e != null) {
                this.f15486e = null;
                b(null);
                this.f15488g = -1L;
                this.f15489h = null;
                this.f15490i = -1;
                this.f15491j = -1;
                return;
            }
            throw new IllegalStateException("not attached to a buffer".toString());
        }
    }

    @Override // me.BufferedSink
    /* renamed from: A0, reason: merged with bridge method [inline-methods] */
    public d f0(byte[] bArr) {
        za.k.e(bArr, "source");
        return O(bArr, 0, bArr.length);
    }

    @Override // me.BufferedSink
    /* renamed from: B0, reason: merged with bridge method [inline-methods] */
    public d O(byte[] bArr, int i10, int i11) {
        za.k.e(bArr, "source");
        long j10 = i11;
        me.b.b(bArr.length, i10, j10);
        int i12 = i11 + i10;
        while (i10 < i12) {
            Segment y02 = y0(1);
            int min = Math.min(i12 - i10, 8192 - y02.f15533c);
            int i13 = i10 + min;
            _ArraysJvm.f(bArr, y02.f15531a, y02.f15533c, i10, i13);
            y02.f15533c += min;
            i10 = i13;
        }
        u0(v0() + j10);
        return this;
    }

    public long C0(Source source) {
        za.k.e(source, "source");
        long j10 = 0;
        while (true) {
            long read = source.read(this, 8192L);
            if (read == -1) {
                return j10;
            }
            j10 += read;
        }
    }

    @Override // me.BufferedSink
    /* renamed from: D0, reason: merged with bridge method [inline-methods] */
    public d t(int i10) {
        Segment y02 = y0(1);
        byte[] bArr = y02.f15531a;
        int i11 = y02.f15533c;
        y02.f15533c = i11 + 1;
        bArr[i11] = (byte) i10;
        u0(v0() + 1);
        return this;
    }

    @Override // me.BufferedSink
    /* renamed from: E0, reason: merged with bridge method [inline-methods] */
    public d T(long j10) {
        if (j10 == 0) {
            return t(48);
        }
        long j11 = (j10 >>> 1) | j10;
        long j12 = j11 | (j11 >>> 2);
        long j13 = j12 | (j12 >>> 4);
        long j14 = j13 | (j13 >>> 8);
        long j15 = j14 | (j14 >>> 16);
        long j16 = j15 | (j15 >>> 32);
        long j17 = j16 - ((j16 >>> 1) & 6148914691236517205L);
        long j18 = ((j17 >>> 2) & 3689348814741910323L) + (j17 & 3689348814741910323L);
        long j19 = ((j18 >>> 4) + j18) & 1085102592571150095L;
        long j20 = j19 + (j19 >>> 8);
        long j21 = j20 + (j20 >>> 16);
        int i10 = (int) ((((j21 & 63) + ((j21 >>> 32) & 63)) + 3) / 4);
        Segment y02 = y0(i10);
        byte[] bArr = y02.f15531a;
        int i11 = y02.f15533c;
        for (int i12 = (i11 + i10) - 1; i12 >= i11; i12--) {
            bArr[i12] = ne.a.a()[(int) (15 & j10)];
            j10 >>>= 4;
        }
        y02.f15533c += i10;
        u0(v0() + i10);
        return this;
    }

    @Override // me.BufferedSink
    /* renamed from: F0, reason: merged with bridge method [inline-methods] */
    public d p(int i10) {
        Segment y02 = y0(4);
        byte[] bArr = y02.f15531a;
        int i11 = y02.f15533c;
        int i12 = i11 + 1;
        bArr[i11] = (byte) ((i10 >>> 24) & 255);
        int i13 = i12 + 1;
        bArr[i12] = (byte) ((i10 >>> 16) & 255);
        int i14 = i13 + 1;
        bArr[i13] = (byte) ((i10 >>> 8) & 255);
        bArr[i14] = (byte) (i10 & 255);
        y02.f15533c = i14 + 1;
        u0(v0() + 4);
        return this;
    }

    @Override // me.BufferedSink
    /* renamed from: G0, reason: merged with bridge method [inline-methods] */
    public d k(int i10) {
        Segment y02 = y0(2);
        byte[] bArr = y02.f15531a;
        int i11 = y02.f15533c;
        int i12 = i11 + 1;
        bArr[i11] = (byte) ((i10 >>> 8) & 255);
        bArr[i12] = (byte) (i10 & 255);
        y02.f15533c = i12 + 1;
        u0(v0() + 2);
        return this;
    }

    public d H0(String str, int i10, int i11, Charset charset) {
        za.k.e(str, "string");
        za.k.e(charset, "charset");
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("beginIndex < 0: " + i10).toString());
        }
        if (i11 >= i10) {
            if (i11 <= str.length()) {
                if (za.k.a(charset, Charsets.f18469b)) {
                    return R(str, i10, i11);
                }
                String substring = str.substring(i10, i11);
                za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                byte[] bytes = substring.getBytes(charset);
                za.k.d(bytes, "this as java.lang.String).getBytes(charset)");
                return O(bytes, 0, bytes.length);
            }
            throw new IllegalArgumentException(("endIndex > string.length: " + i11 + " > " + str.length()).toString());
        }
        throw new IllegalArgumentException(("endIndex < beginIndex: " + i11 + " < " + i10).toString());
    }

    @Override // me.BufferedSource
    public long I(g gVar) {
        za.k.e(gVar, "bytes");
        return S(gVar, 0L);
    }

    @Override // me.BufferedSink
    /* renamed from: I0, reason: merged with bridge method [inline-methods] */
    public d E(String str) {
        za.k.e(str, "string");
        return R(str, 0, str.length());
    }

    @Override // me.BufferedSource
    public String J(Charset charset) {
        za.k.e(charset, "charset");
        return m0(this.f15485f, charset);
    }

    @Override // me.BufferedSink
    /* renamed from: J0, reason: merged with bridge method [inline-methods] */
    public d R(String str, int i10, int i11) {
        char charAt;
        za.k.e(str, "string");
        if (!(i10 >= 0)) {
            throw new IllegalArgumentException(("beginIndex < 0: " + i10).toString());
        }
        if (i11 >= i10) {
            if (!(i11 <= str.length())) {
                throw new IllegalArgumentException(("endIndex > string.length: " + i11 + " > " + str.length()).toString());
            }
            while (i10 < i11) {
                char charAt2 = str.charAt(i10);
                if (charAt2 < 128) {
                    Segment y02 = y0(1);
                    byte[] bArr = y02.f15531a;
                    int i12 = y02.f15533c - i10;
                    int min = Math.min(i11, 8192 - i12);
                    int i13 = i10 + 1;
                    bArr[i10 + i12] = (byte) charAt2;
                    while (true) {
                        i10 = i13;
                        if (i10 >= min || (charAt = str.charAt(i10)) >= 128) {
                            break;
                        }
                        i13 = i10 + 1;
                        bArr[i10 + i12] = (byte) charAt;
                    }
                    int i14 = y02.f15533c;
                    int i15 = (i12 + i10) - i14;
                    y02.f15533c = i14 + i15;
                    u0(v0() + i15);
                } else {
                    if (charAt2 < 2048) {
                        Segment y03 = y0(2);
                        byte[] bArr2 = y03.f15531a;
                        int i16 = y03.f15533c;
                        bArr2[i16] = (byte) ((charAt2 >> 6) | 192);
                        bArr2[i16 + 1] = (byte) ((charAt2 & '?') | 128);
                        y03.f15533c = i16 + 2;
                        u0(v0() + 2);
                    } else if (charAt2 >= 55296 && charAt2 <= 57343) {
                        int i17 = i10 + 1;
                        char charAt3 = i17 < i11 ? str.charAt(i17) : (char) 0;
                        if (charAt2 <= 56319) {
                            if (56320 <= charAt3 && charAt3 < 57344) {
                                int i18 = (((charAt2 & 1023) << 10) | (charAt3 & 1023)) + 65536;
                                Segment y04 = y0(4);
                                byte[] bArr3 = y04.f15531a;
                                int i19 = y04.f15533c;
                                bArr3[i19] = (byte) ((i18 >> 18) | 240);
                                bArr3[i19 + 1] = (byte) (((i18 >> 12) & 63) | 128);
                                bArr3[i19 + 2] = (byte) (((i18 >> 6) & 63) | 128);
                                bArr3[i19 + 3] = (byte) ((i18 & 63) | 128);
                                y04.f15533c = i19 + 4;
                                u0(v0() + 4);
                                i10 += 2;
                            }
                        }
                        t(63);
                        i10 = i17;
                    } else {
                        Segment y05 = y0(3);
                        byte[] bArr4 = y05.f15531a;
                        int i20 = y05.f15533c;
                        bArr4[i20] = (byte) ((charAt2 >> '\f') | 224);
                        bArr4[i20 + 1] = (byte) ((63 & (charAt2 >> 6)) | 128);
                        bArr4[i20 + 2] = (byte) ((charAt2 & '?') | 128);
                        y05.f15533c = i20 + 3;
                        u0(v0() + 3);
                    }
                    i10++;
                }
            }
            return this;
        }
        throw new IllegalArgumentException(("endIndex < beginIndex: " + i11 + " < " + i10).toString());
    }

    public d K0(int i10) {
        if (i10 < 128) {
            t(i10);
        } else if (i10 < 2048) {
            Segment y02 = y0(2);
            byte[] bArr = y02.f15531a;
            int i11 = y02.f15533c;
            bArr[i11] = (byte) ((i10 >> 6) | 192);
            bArr[i11 + 1] = (byte) ((i10 & 63) | 128);
            y02.f15533c = i11 + 2;
            u0(v0() + 2);
        } else {
            boolean z10 = false;
            if (55296 <= i10 && i10 < 57344) {
                z10 = true;
            }
            if (z10) {
                t(63);
            } else if (i10 < 65536) {
                Segment y03 = y0(3);
                byte[] bArr2 = y03.f15531a;
                int i12 = y03.f15533c;
                bArr2[i12] = (byte) ((i10 >> 12) | 224);
                bArr2[i12 + 1] = (byte) (((i10 >> 6) & 63) | 128);
                bArr2[i12 + 2] = (byte) ((i10 & 63) | 128);
                y03.f15533c = i12 + 3;
                u0(v0() + 3);
            } else if (i10 <= 1114111) {
                Segment y04 = y0(4);
                byte[] bArr3 = y04.f15531a;
                int i13 = y04.f15533c;
                bArr3[i13] = (byte) ((i10 >> 18) | 240);
                bArr3[i13 + 1] = (byte) (((i10 >> 12) & 63) | 128);
                bArr3[i13 + 2] = (byte) (((i10 >> 6) & 63) | 128);
                bArr3[i13 + 3] = (byte) ((i10 & 63) | 128);
                y04.f15533c = i13 + 4;
                u0(v0() + 4);
            } else {
                throw new IllegalArgumentException("Unexpected code point: 0x" + me.b.i(i10));
            }
        }
        return this;
    }

    public final byte L(long j10) {
        me.b.b(v0(), j10, 1L);
        Segment segment = this.f15484e;
        if (segment == null) {
            za.k.b(null);
            throw null;
        }
        if (v0() - j10 < j10) {
            long v02 = v0();
            while (v02 > j10) {
                segment = segment.f15537g;
                za.k.b(segment);
                v02 -= segment.f15533c - segment.f15532b;
            }
            za.k.b(segment);
            return segment.f15531a[(int) ((segment.f15532b + j10) - v02)];
        }
        long j11 = 0;
        while (true) {
            long j12 = (segment.f15533c - segment.f15532b) + j11;
            if (j12 <= j10) {
                segment = segment.f15536f;
                za.k.b(segment);
                j11 = j12;
            } else {
                za.k.b(segment);
                return segment.f15531a[(int) ((segment.f15532b + j10) - j11)];
            }
        }
    }

    @Override // me.BufferedSource
    public byte M() {
        if (v0() != 0) {
            Segment segment = this.f15484e;
            za.k.b(segment);
            int i10 = segment.f15532b;
            int i11 = segment.f15533c;
            int i12 = i10 + 1;
            byte b10 = segment.f15531a[i10];
            u0(v0() - 1);
            if (i12 == i11) {
                this.f15484e = segment.b();
                SegmentPool.b(segment);
            } else {
                segment.f15532b = i12;
            }
            return b10;
        }
        throw new EOFException();
    }

    public long N(byte b10, long j10, long j11) {
        Segment segment;
        int i10;
        long j12 = 0;
        boolean z10 = false;
        if (0 <= j10 && j10 <= j11) {
            z10 = true;
        }
        if (!z10) {
            throw new IllegalArgumentException(("size=" + v0() + " fromIndex=" + j10 + " toIndex=" + j11).toString());
        }
        if (j11 > v0()) {
            j11 = v0();
        }
        if (j10 == j11 || (segment = this.f15484e) == null) {
            return -1L;
        }
        if (v0() - j10 < j10) {
            j12 = v0();
            while (j12 > j10) {
                segment = segment.f15537g;
                za.k.b(segment);
                j12 -= segment.f15533c - segment.f15532b;
            }
            while (j12 < j11) {
                byte[] bArr = segment.f15531a;
                int min = (int) Math.min(segment.f15533c, (segment.f15532b + j11) - j12);
                i10 = (int) ((segment.f15532b + j10) - j12);
                while (i10 < min) {
                    if (bArr[i10] != b10) {
                        i10++;
                    }
                }
                j12 += segment.f15533c - segment.f15532b;
                segment = segment.f15536f;
                za.k.b(segment);
                j10 = j12;
            }
            return -1L;
        }
        while (true) {
            long j13 = (segment.f15533c - segment.f15532b) + j12;
            if (j13 > j10) {
                break;
            }
            segment = segment.f15536f;
            za.k.b(segment);
            j12 = j13;
        }
        while (j12 < j11) {
            byte[] bArr2 = segment.f15531a;
            int min2 = (int) Math.min(segment.f15533c, (segment.f15532b + j11) - j12);
            i10 = (int) ((segment.f15532b + j10) - j12);
            while (i10 < min2) {
                if (bArr2[i10] != b10) {
                    i10++;
                }
            }
            j12 += segment.f15533c - segment.f15532b;
            segment = segment.f15536f;
            za.k.b(segment);
            j10 = j12;
        }
        return -1L;
        return (i10 - segment.f15532b) + j12;
    }

    public long S(g gVar, long j10) {
        long j11 = j10;
        za.k.e(gVar, "bytes");
        if (!(gVar.t() > 0)) {
            throw new IllegalArgumentException("bytes is empty".toString());
        }
        long j12 = 0;
        if (j11 >= 0) {
            Segment segment = this.f15484e;
            if (segment != null) {
                if (v0() - j11 < j11) {
                    long v02 = v0();
                    while (v02 > j11) {
                        segment = segment.f15537g;
                        za.k.b(segment);
                        v02 -= segment.f15533c - segment.f15532b;
                    }
                    byte[] l10 = gVar.l();
                    byte b10 = l10[0];
                    int t7 = gVar.t();
                    long v03 = (v0() - t7) + 1;
                    while (v02 < v03) {
                        byte[] bArr = segment.f15531a;
                        int min = (int) Math.min(segment.f15533c, (segment.f15532b + v03) - v02);
                        for (int i10 = (int) ((segment.f15532b + j11) - v02); i10 < min; i10++) {
                            if (bArr[i10] == b10 && ne.a.b(segment, i10 + 1, l10, 1, t7)) {
                                return (i10 - segment.f15532b) + v02;
                            }
                        }
                        v02 += segment.f15533c - segment.f15532b;
                        segment = segment.f15536f;
                        za.k.b(segment);
                        j11 = v02;
                    }
                } else {
                    while (true) {
                        long j13 = (segment.f15533c - segment.f15532b) + j12;
                        if (j13 > j11) {
                            break;
                        }
                        segment = segment.f15536f;
                        za.k.b(segment);
                        j12 = j13;
                    }
                    byte[] l11 = gVar.l();
                    byte b11 = l11[0];
                    int t10 = gVar.t();
                    long v04 = (v0() - t10) + 1;
                    while (j12 < v04) {
                        byte[] bArr2 = segment.f15531a;
                        long j14 = j12;
                        int min2 = (int) Math.min(segment.f15533c, (segment.f15532b + v04) - j12);
                        for (int i11 = (int) ((segment.f15532b + j11) - j14); i11 < min2; i11++) {
                            if (bArr2[i11] == b11 && ne.a.b(segment, i11 + 1, l11, 1, t10)) {
                                return (i11 - segment.f15532b) + j14;
                            }
                        }
                        j12 = j14 + (segment.f15533c - segment.f15532b);
                        segment = segment.f15536f;
                        za.k.b(segment);
                        j11 = j12;
                    }
                }
            }
            return -1L;
        }
        throw new IllegalArgumentException(("fromIndex < 0: " + j11).toString());
    }

    public long U(g gVar, long j10) {
        int i10;
        int i11;
        za.k.e(gVar, "targetBytes");
        long j11 = 0;
        if (j10 >= 0) {
            Segment segment = this.f15484e;
            if (segment == null) {
                return -1L;
            }
            if (v0() - j10 < j10) {
                j11 = v0();
                while (j11 > j10) {
                    segment = segment.f15537g;
                    za.k.b(segment);
                    j11 -= segment.f15533c - segment.f15532b;
                }
                if (gVar.t() == 2) {
                    byte e10 = gVar.e(0);
                    byte e11 = gVar.e(1);
                    while (j11 < v0()) {
                        byte[] bArr = segment.f15531a;
                        i10 = (int) ((segment.f15532b + j10) - j11);
                        int i12 = segment.f15533c;
                        while (i10 < i12) {
                            byte b10 = bArr[i10];
                            if (b10 != e10 && b10 != e11) {
                                i10++;
                            }
                            i11 = segment.f15532b;
                        }
                        j11 += segment.f15533c - segment.f15532b;
                        segment = segment.f15536f;
                        za.k.b(segment);
                        j10 = j11;
                    }
                    return -1L;
                }
                byte[] l10 = gVar.l();
                while (j11 < v0()) {
                    byte[] bArr2 = segment.f15531a;
                    i10 = (int) ((segment.f15532b + j10) - j11);
                    int i13 = segment.f15533c;
                    while (i10 < i13) {
                        byte b11 = bArr2[i10];
                        for (byte b12 : l10) {
                            if (b11 == b12) {
                                i11 = segment.f15532b;
                            }
                        }
                        i10++;
                    }
                    j11 += segment.f15533c - segment.f15532b;
                    segment = segment.f15536f;
                    za.k.b(segment);
                    j10 = j11;
                }
                return -1L;
            }
            while (true) {
                long j12 = (segment.f15533c - segment.f15532b) + j11;
                if (j12 > j10) {
                    break;
                }
                segment = segment.f15536f;
                za.k.b(segment);
                j11 = j12;
            }
            if (gVar.t() == 2) {
                byte e12 = gVar.e(0);
                byte e13 = gVar.e(1);
                while (j11 < v0()) {
                    byte[] bArr3 = segment.f15531a;
                    i10 = (int) ((segment.f15532b + j10) - j11);
                    int i14 = segment.f15533c;
                    while (i10 < i14) {
                        byte b13 = bArr3[i10];
                        if (b13 != e12 && b13 != e13) {
                            i10++;
                        }
                        i11 = segment.f15532b;
                    }
                    j11 += segment.f15533c - segment.f15532b;
                    segment = segment.f15536f;
                    za.k.b(segment);
                    j10 = j11;
                }
                return -1L;
            }
            byte[] l11 = gVar.l();
            while (j11 < v0()) {
                byte[] bArr4 = segment.f15531a;
                i10 = (int) ((segment.f15532b + j10) - j11);
                int i15 = segment.f15533c;
                while (i10 < i15) {
                    byte b14 = bArr4[i10];
                    for (byte b15 : l11) {
                        if (b14 == b15) {
                            i11 = segment.f15532b;
                        }
                    }
                    i10++;
                }
                j11 += segment.f15533c - segment.f15532b;
                segment = segment.f15536f;
                za.k.b(segment);
                j10 = j11;
            }
            return -1L;
            return (i10 - i11) + j11;
        }
        throw new IllegalArgumentException(("fromIndex < 0: " + j10).toString());
    }

    @Override // me.BufferedSource
    public void V(long j10) {
        while (j10 > 0) {
            Segment segment = this.f15484e;
            if (segment != null) {
                int min = (int) Math.min(j10, segment.f15533c - segment.f15532b);
                long j11 = min;
                u0(v0() - j11);
                j10 -= j11;
                int i10 = segment.f15532b + min;
                segment.f15532b = i10;
                if (i10 == segment.f15533c) {
                    this.f15484e = segment.b();
                    SegmentPool.b(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    @Override // me.BufferedSource
    public boolean W(long j10) {
        return this.f15485f >= j10;
    }

    public int X(byte[] bArr, int i10, int i11) {
        za.k.e(bArr, "sink");
        me.b.b(bArr.length, i10, i11);
        Segment segment = this.f15484e;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(i11, segment.f15533c - segment.f15532b);
        byte[] bArr2 = segment.f15531a;
        int i12 = segment.f15532b;
        _ArraysJvm.f(bArr2, bArr, i10, i12, i12 + min);
        segment.f15532b += min;
        u0(v0() - min);
        if (segment.f15532b == segment.f15533c) {
            this.f15484e = segment.b();
            SegmentPool.b(segment);
        }
        return min;
    }

    @Override // me.BufferedSource
    public String Z() {
        return x(Long.MAX_VALUE);
    }

    @Override // me.BufferedSource, me.BufferedSink
    public d a() {
        return this;
    }

    public byte[] a0() {
        return b0(v0());
    }

    public final void b() {
        V(v0());
    }

    @Override // me.BufferedSource
    public byte[] b0(long j10) {
        if (!(j10 >= 0 && j10 <= 2147483647L)) {
            throw new IllegalArgumentException(("byteCount: " + j10).toString());
        }
        if (v0() >= j10) {
            byte[] bArr = new byte[(int) j10];
            h0(bArr);
            return bArr;
        }
        throw new EOFException();
    }

    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public d clone() {
        return v();
    }

    @Override // me.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    public g e0() {
        return h(v0());
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof d)) {
                return false;
            }
            d dVar = (d) obj;
            if (v0() != dVar.v0()) {
                return false;
            }
            if (v0() != 0) {
                Segment segment = this.f15484e;
                za.k.b(segment);
                Segment segment2 = dVar.f15484e;
                za.k.b(segment2);
                int i10 = segment.f15532b;
                int i11 = segment2.f15532b;
                long j10 = 0;
                while (j10 < v0()) {
                    long min = Math.min(segment.f15533c - i10, segment2.f15533c - i11);
                    long j11 = 0;
                    while (j11 < min) {
                        int i12 = i10 + 1;
                        int i13 = i11 + 1;
                        if (segment.f15531a[i10] != segment2.f15531a[i11]) {
                            return false;
                        }
                        j11++;
                        i10 = i12;
                        i11 = i13;
                    }
                    if (i10 == segment.f15533c) {
                        segment = segment.f15536f;
                        za.k.b(segment);
                        i10 = segment.f15532b;
                    }
                    if (i11 == segment2.f15533c) {
                        segment2 = segment2.f15536f;
                        za.k.b(segment2);
                        i11 = segment2.f15532b;
                    }
                    j10 += min;
                }
            }
        }
        return true;
    }

    @Override // me.BufferedSink, me.Sink, java.io.Flushable
    public void flush() {
    }

    @Override // me.BufferedSource
    public d g() {
        return this;
    }

    @Override // me.BufferedSource
    public long g0(g gVar) {
        za.k.e(gVar, "targetBytes");
        return U(gVar, 0L);
    }

    @Override // me.BufferedSource
    public g h(long j10) {
        if (!(j10 >= 0 && j10 <= 2147483647L)) {
            throw new IllegalArgumentException(("byteCount: " + j10).toString());
        }
        if (v0() < j10) {
            throw new EOFException();
        }
        if (j10 >= 4096) {
            g x02 = x0((int) j10);
            V(j10);
            return x02;
        }
        return new g(b0(j10));
    }

    public void h0(byte[] bArr) {
        za.k.e(bArr, "sink");
        int i10 = 0;
        while (i10 < bArr.length) {
            int X = X(bArr, i10, bArr.length - i10);
            if (X == -1) {
                throw new EOFException();
            }
            i10 += X;
        }
    }

    public int hashCode() {
        Segment segment = this.f15484e;
        if (segment == null) {
            return 0;
        }
        int i10 = 1;
        do {
            int i11 = segment.f15533c;
            for (int i12 = segment.f15532b; i12 < i11; i12++) {
                i10 = (i10 * 31) + segment.f15531a[i12];
            }
            segment = segment.f15536f;
            za.k.b(segment);
        } while (segment != this.f15484e);
        return i10;
    }

    public int i0() {
        return me.b.f(o());
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return true;
    }

    public short j0() {
        return me.b.g(k0());
    }

    @Override // me.BufferedSource
    public short k0() {
        if (v0() >= 2) {
            Segment segment = this.f15484e;
            za.k.b(segment);
            int i10 = segment.f15532b;
            int i11 = segment.f15533c;
            if (i11 - i10 < 2) {
                return (short) ((M() & 255) | ((M() & 255) << 8));
            }
            byte[] bArr = segment.f15531a;
            int i12 = i10 + 1;
            int i13 = i12 + 1;
            int i14 = ((bArr[i10] & 255) << 8) | (bArr[i12] & 255);
            u0(v0() - 2);
            if (i13 == i11) {
                this.f15484e = segment.b();
                SegmentPool.b(segment);
            } else {
                segment.f15532b = i13;
            }
            return (short) i14;
        }
        throw new EOFException();
    }

    @Override // me.BufferedSource
    public long l0(Sink sink) {
        za.k.e(sink, "sink");
        long v02 = v0();
        if (v02 > 0) {
            sink.write(this, v02);
        }
        return v02;
    }

    public String m0(long j10, Charset charset) {
        za.k.e(charset, "charset");
        if (!(j10 >= 0 && j10 <= 2147483647L)) {
            throw new IllegalArgumentException(("byteCount: " + j10).toString());
        }
        if (this.f15485f < j10) {
            throw new EOFException();
        }
        if (j10 == 0) {
            return "";
        }
        Segment segment = this.f15484e;
        za.k.b(segment);
        int i10 = segment.f15532b;
        if (i10 + j10 > segment.f15533c) {
            return new String(b0(j10), charset);
        }
        int i11 = (int) j10;
        String str = new String(segment.f15531a, i10, i11, charset);
        int i12 = segment.f15532b + i11;
        segment.f15532b = i12;
        this.f15485f -= j10;
        if (i12 == segment.f15533c) {
            this.f15484e = segment.b();
            SegmentPool.b(segment);
        }
        return str;
    }

    @Override // me.BufferedSource
    public BufferedSource n0() {
        return n.b(new PeekSource(this));
    }

    @Override // me.BufferedSource
    public int o() {
        if (v0() >= 4) {
            Segment segment = this.f15484e;
            za.k.b(segment);
            int i10 = segment.f15532b;
            int i11 = segment.f15533c;
            if (i11 - i10 < 4) {
                return (M() & 255) | ((M() & 255) << 24) | ((M() & 255) << 16) | ((M() & 255) << 8);
            }
            byte[] bArr = segment.f15531a;
            int i12 = i10 + 1;
            int i13 = i12 + 1;
            int i14 = ((bArr[i10] & 255) << 24) | ((bArr[i12] & 255) << 16);
            int i15 = i13 + 1;
            int i16 = i14 | ((bArr[i13] & 255) << 8);
            int i17 = i15 + 1;
            int i18 = i16 | (bArr[i15] & 255);
            u0(v0() - 4);
            if (i17 == i11) {
                this.f15484e = segment.b();
                SegmentPool.b(segment);
            } else {
                segment.f15532b = i17;
            }
            return i18;
        }
        throw new EOFException();
    }

    public String o0() {
        return m0(this.f15485f, Charsets.f18469b);
    }

    @Override // me.BufferedSource
    public void p0(long j10) {
        if (this.f15485f < j10) {
            throw new EOFException();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00aa A[EDGE_INSN: B:41:0x00aa->B:38:0x00aa BREAK  A[LOOP:0: B:4:0x000d->B:40:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a2  */
    @Override // me.BufferedSource
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long r0() {
        int i10;
        int i11;
        if (v0() != 0) {
            int i12 = 0;
            boolean z10 = false;
            long j10 = 0;
            do {
                Segment segment = this.f15484e;
                za.k.b(segment);
                byte[] bArr = segment.f15531a;
                int i13 = segment.f15532b;
                int i14 = segment.f15533c;
                while (i13 < i14) {
                    byte b10 = bArr[i13];
                    if (b10 < 48 || b10 > 57) {
                        if (b10 >= 97 && b10 <= 102) {
                            i10 = b10 - 97;
                        } else if (b10 >= 65 && b10 <= 70) {
                            i10 = b10 - 65;
                        } else {
                            if (i12 == 0) {
                                throw new NumberFormatException("Expected leading [0-9a-fA-F] character but was 0x" + me.b.h(b10));
                            }
                            z10 = true;
                            if (i13 != i14) {
                                this.f15484e = segment.b();
                                SegmentPool.b(segment);
                            } else {
                                segment.f15532b = i13;
                            }
                            if (!z10) {
                                break;
                            }
                        }
                        i11 = i10 + 10;
                    } else {
                        i11 = b10 - 48;
                    }
                    if (((-1152921504606846976L) & j10) != 0) {
                        throw new NumberFormatException("Number too large: " + new d().T(j10).t(b10).o0());
                    }
                    j10 = (j10 << 4) | i11;
                    i13++;
                    i12++;
                }
                if (i13 != i14) {
                }
                if (!z10) {
                }
            } while (this.f15484e != null);
            u0(v0() - i12);
            return j10;
        }
        throw new EOFException();
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) {
        za.k.e(byteBuffer, "sink");
        Segment segment = this.f15484e;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteBuffer.remaining(), segment.f15533c - segment.f15532b);
        byteBuffer.put(segment.f15531a, segment.f15532b, min);
        int i10 = segment.f15532b + min;
        segment.f15532b = i10;
        this.f15485f -= min;
        if (i10 == segment.f15533c) {
            this.f15484e = segment.b();
            SegmentPool.b(segment);
        }
        return min;
    }

    @Override // me.BufferedSource
    public boolean s() {
        return this.f15485f == 0;
    }

    @Override // me.BufferedSource
    public InputStream s0() {
        return new b();
    }

    public String t0(long j10) {
        return m0(j10, Charsets.f18469b);
    }

    @Override // me.Source
    public Timeout timeout() {
        return Timeout.f15467e;
    }

    public String toString() {
        return w0().toString();
    }

    public final long u() {
        long v02 = v0();
        if (v02 == 0) {
            return 0L;
        }
        Segment segment = this.f15484e;
        za.k.b(segment);
        Segment segment2 = segment.f15537g;
        za.k.b(segment2);
        if (segment2.f15533c < 8192 && segment2.f15535e) {
            v02 -= r2 - segment2.f15532b;
        }
        return v02;
    }

    public final void u0(long j10) {
        this.f15485f = j10;
    }

    public final d v() {
        d dVar = new d();
        if (v0() != 0) {
            Segment segment = this.f15484e;
            za.k.b(segment);
            Segment d10 = segment.d();
            dVar.f15484e = d10;
            d10.f15537g = d10;
            d10.f15536f = d10;
            for (Segment segment2 = segment.f15536f; segment2 != segment; segment2 = segment2.f15536f) {
                Segment segment3 = d10.f15537g;
                za.k.b(segment3);
                za.k.b(segment2);
                segment3.c(segment2.d());
            }
            dVar.u0(v0());
        }
        return dVar;
    }

    public final long v0() {
        return this.f15485f;
    }

    public final d w(d dVar, long j10, long j11) {
        za.k.e(dVar, "out");
        me.b.b(v0(), j10, j11);
        if (j11 != 0) {
            dVar.u0(dVar.v0() + j11);
            Segment segment = this.f15484e;
            while (true) {
                za.k.b(segment);
                int i10 = segment.f15533c;
                int i11 = segment.f15532b;
                if (j10 < i10 - i11) {
                    break;
                }
                j10 -= i10 - i11;
                segment = segment.f15536f;
            }
            while (j11 > 0) {
                za.k.b(segment);
                Segment d10 = segment.d();
                int i12 = d10.f15532b + ((int) j10);
                d10.f15532b = i12;
                d10.f15533c = Math.min(i12 + ((int) j11), d10.f15533c);
                Segment segment2 = dVar.f15484e;
                if (segment2 == null) {
                    d10.f15537g = d10;
                    d10.f15536f = d10;
                    dVar.f15484e = d10;
                } else {
                    za.k.b(segment2);
                    Segment segment3 = segment2.f15537g;
                    za.k.b(segment3);
                    segment3.c(d10);
                }
                j11 -= d10.f15533c - d10.f15532b;
                segment = segment.f15536f;
                j10 = 0;
            }
        }
        return this;
    }

    public final g w0() {
        if (v0() <= 2147483647L) {
            return x0((int) v0());
        }
        throw new IllegalStateException(("size > Int.MAX_VALUE: " + v0()).toString());
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) {
        za.k.e(byteBuffer, "source");
        int remaining = byteBuffer.remaining();
        int i10 = remaining;
        while (i10 > 0) {
            Segment y02 = y0(1);
            int min = Math.min(i10, 8192 - y02.f15533c);
            byteBuffer.get(y02.f15531a, y02.f15533c, min);
            i10 -= min;
            y02.f15533c += min;
        }
        this.f15485f += remaining;
        return remaining;
    }

    @Override // me.BufferedSource
    public String x(long j10) {
        if (j10 >= 0) {
            long j11 = j10 != Long.MAX_VALUE ? j10 + 1 : Long.MAX_VALUE;
            long N = N((byte) 10, 0L, j11);
            if (N != -1) {
                return ne.a.c(this, N);
            }
            if (j11 < v0() && L(j11 - 1) == 13 && L(j11) == 10) {
                return ne.a.c(this, j11);
            }
            d dVar = new d();
            w(dVar, 0L, Math.min(32, v0()));
            throw new EOFException("\\n not found: limit=" + Math.min(v0(), j10) + " content=" + dVar.e0().j() + (char) 8230);
        }
        throw new IllegalArgumentException(("limit < 0: " + j10).toString());
    }

    public final g x0(int i10) {
        if (i10 == 0) {
            return g.f15494i;
        }
        me.b.b(v0(), 0L, i10);
        Segment segment = this.f15484e;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        while (i12 < i10) {
            za.k.b(segment);
            int i14 = segment.f15533c;
            int i15 = segment.f15532b;
            if (i14 != i15) {
                i12 += i14 - i15;
                i13++;
                segment = segment.f15536f;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] bArr = new byte[i13];
        int[] iArr = new int[i13 * 2];
        Segment segment2 = this.f15484e;
        int i16 = 0;
        while (i11 < i10) {
            za.k.b(segment2);
            bArr[i16] = segment2.f15531a;
            i11 += segment2.f15533c - segment2.f15532b;
            iArr[i16] = Math.min(i11, i10);
            iArr[i16 + i13] = segment2.f15532b;
            segment2.f15534d = true;
            i16++;
            segment2 = segment2.f15536f;
        }
        return new x(bArr, iArr);
    }

    public final Segment y0(int i10) {
        if (i10 >= 1 && i10 <= 8192) {
            Segment segment = this.f15484e;
            if (segment == null) {
                Segment c10 = SegmentPool.c();
                this.f15484e = c10;
                c10.f15537g = c10;
                c10.f15536f = c10;
                return c10;
            }
            za.k.b(segment);
            Segment segment2 = segment.f15537g;
            za.k.b(segment2);
            if (segment2.f15533c + i10 > 8192 || !segment2.f15535e) {
                segment2 = segment2.c(SegmentPool.c());
            }
            return segment2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    @Override // me.BufferedSource
    public int z(q qVar) {
        za.k.e(qVar, "options");
        int e10 = ne.a.e(this, qVar, false, 2, null);
        if (e10 == -1) {
            return -1;
        }
        V(qVar.g()[e10].t());
        return e10;
    }

    @Override // me.BufferedSink
    /* renamed from: z0, reason: merged with bridge method [inline-methods] */
    public d F(g gVar) {
        za.k.e(gVar, "byteString");
        gVar.x(this, 0, gVar.t());
        return this;
    }

    /* compiled from: Buffer.kt */
    /* loaded from: classes2.dex */
    public static final class b extends InputStream {
        b() {
        }

        @Override // java.io.InputStream
        public int available() {
            return (int) Math.min(d.this.v0(), Integer.MAX_VALUE);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.InputStream
        public int read() {
            if (d.this.v0() > 0) {
                return d.this.M() & 255;
            }
            return -1;
        }

        public String toString() {
            return d.this + ".inputStream()";
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i10, int i11) {
            za.k.e(bArr, "sink");
            return d.this.X(bArr, i10, i11);
        }
    }

    @Override // me.Sink
    public void write(d dVar, long j10) {
        Segment segment;
        za.k.e(dVar, "source");
        if (dVar != this) {
            me.b.b(dVar.v0(), 0L, j10);
            while (j10 > 0) {
                Segment segment2 = dVar.f15484e;
                za.k.b(segment2);
                int i10 = segment2.f15533c;
                za.k.b(dVar.f15484e);
                if (j10 < i10 - r2.f15532b) {
                    Segment segment3 = this.f15484e;
                    if (segment3 != null) {
                        za.k.b(segment3);
                        segment = segment3.f15537g;
                    } else {
                        segment = null;
                    }
                    if (segment != null && segment.f15535e) {
                        if ((segment.f15533c + j10) - (segment.f15534d ? 0 : segment.f15532b) <= 8192) {
                            Segment segment4 = dVar.f15484e;
                            za.k.b(segment4);
                            segment4.f(segment, (int) j10);
                            dVar.u0(dVar.v0() - j10);
                            u0(v0() + j10);
                            return;
                        }
                    }
                    Segment segment5 = dVar.f15484e;
                    za.k.b(segment5);
                    dVar.f15484e = segment5.e((int) j10);
                }
                Segment segment6 = dVar.f15484e;
                za.k.b(segment6);
                long j11 = segment6.f15533c - segment6.f15532b;
                dVar.f15484e = segment6.b();
                Segment segment7 = this.f15484e;
                if (segment7 == null) {
                    this.f15484e = segment6;
                    segment6.f15537g = segment6;
                    segment6.f15536f = segment6;
                } else {
                    za.k.b(segment7);
                    Segment segment8 = segment7.f15537g;
                    za.k.b(segment8);
                    segment8.c(segment6).a();
                }
                dVar.u0(dVar.v0() - j11);
                u0(v0() + j11);
                j10 -= j11;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    @Override // me.Source
    public long read(d dVar, long j10) {
        za.k.e(dVar, "sink");
        if (!(j10 >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j10).toString());
        }
        if (v0() == 0) {
            return -1L;
        }
        if (j10 > v0()) {
            j10 = v0();
        }
        dVar.write(this, j10);
        return j10;
    }
}
