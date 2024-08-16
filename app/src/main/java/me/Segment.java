package me;

import kotlin.collections._ArraysJvm;
import za.DefaultConstructorMarker;

/* compiled from: Segment.kt */
/* renamed from: me.v, reason: use source file name */
/* loaded from: classes2.dex */
public final class Segment {

    /* renamed from: h, reason: collision with root package name */
    public static final a f15530h = new a(null);

    /* renamed from: a, reason: collision with root package name */
    public final byte[] f15531a;

    /* renamed from: b, reason: collision with root package name */
    public int f15532b;

    /* renamed from: c, reason: collision with root package name */
    public int f15533c;

    /* renamed from: d, reason: collision with root package name */
    public boolean f15534d;

    /* renamed from: e, reason: collision with root package name */
    public boolean f15535e;

    /* renamed from: f, reason: collision with root package name */
    public Segment f15536f;

    /* renamed from: g, reason: collision with root package name */
    public Segment f15537g;

    /* compiled from: Segment.kt */
    /* renamed from: me.v$a */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public Segment() {
        this.f15531a = new byte[8192];
        this.f15535e = true;
        this.f15534d = false;
    }

    public final void a() {
        Segment segment = this.f15537g;
        int i10 = 0;
        if (segment != this) {
            za.k.b(segment);
            if (segment.f15535e) {
                int i11 = this.f15533c - this.f15532b;
                Segment segment2 = this.f15537g;
                za.k.b(segment2);
                int i12 = 8192 - segment2.f15533c;
                Segment segment3 = this.f15537g;
                za.k.b(segment3);
                if (!segment3.f15534d) {
                    Segment segment4 = this.f15537g;
                    za.k.b(segment4);
                    i10 = segment4.f15532b;
                }
                if (i11 > i12 + i10) {
                    return;
                }
                Segment segment5 = this.f15537g;
                za.k.b(segment5);
                f(segment5, i11);
                b();
                SegmentPool.b(this);
                return;
            }
            return;
        }
        throw new IllegalStateException("cannot compact".toString());
    }

    public final Segment b() {
        Segment segment = this.f15536f;
        if (segment == this) {
            segment = null;
        }
        Segment segment2 = this.f15537g;
        za.k.b(segment2);
        segment2.f15536f = this.f15536f;
        Segment segment3 = this.f15536f;
        za.k.b(segment3);
        segment3.f15537g = this.f15537g;
        this.f15536f = null;
        this.f15537g = null;
        return segment;
    }

    public final Segment c(Segment segment) {
        za.k.e(segment, "segment");
        segment.f15537g = this;
        segment.f15536f = this.f15536f;
        Segment segment2 = this.f15536f;
        za.k.b(segment2);
        segment2.f15537g = segment;
        this.f15536f = segment;
        return segment;
    }

    public final Segment d() {
        this.f15534d = true;
        return new Segment(this.f15531a, this.f15532b, this.f15533c, true, false);
    }

    public final Segment e(int i10) {
        Segment c10;
        if (i10 > 0 && i10 <= this.f15533c - this.f15532b) {
            if (i10 >= 1024) {
                c10 = d();
            } else {
                c10 = SegmentPool.c();
                byte[] bArr = this.f15531a;
                byte[] bArr2 = c10.f15531a;
                int i11 = this.f15532b;
                _ArraysJvm.h(bArr, bArr2, 0, i11, i11 + i10, 2, null);
            }
            c10.f15533c = c10.f15532b + i10;
            this.f15532b += i10;
            Segment segment = this.f15537g;
            za.k.b(segment);
            segment.c(c10);
            return c10;
        }
        throw new IllegalArgumentException("byteCount out of range".toString());
    }

    public final void f(Segment segment, int i10) {
        za.k.e(segment, "sink");
        if (segment.f15535e) {
            int i11 = segment.f15533c;
            if (i11 + i10 > 8192) {
                if (!segment.f15534d) {
                    int i12 = segment.f15532b;
                    if ((i11 + i10) - i12 <= 8192) {
                        byte[] bArr = segment.f15531a;
                        _ArraysJvm.h(bArr, bArr, 0, i12, i11, 2, null);
                        segment.f15533c -= segment.f15532b;
                        segment.f15532b = 0;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
            byte[] bArr2 = this.f15531a;
            byte[] bArr3 = segment.f15531a;
            int i13 = segment.f15533c;
            int i14 = this.f15532b;
            _ArraysJvm.f(bArr2, bArr3, i13, i14, i14 + i10);
            segment.f15533c += i10;
            this.f15532b += i10;
            return;
        }
        throw new IllegalStateException("only owner can write".toString());
    }

    public Segment(byte[] bArr, int i10, int i11, boolean z10, boolean z11) {
        za.k.e(bArr, "data");
        this.f15531a = bArr;
        this.f15532b = i10;
        this.f15533c = i11;
        this.f15534d = z10;
        this.f15535e = z11;
    }
}
