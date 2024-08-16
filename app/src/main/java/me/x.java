package me;

import java.security.MessageDigest;
import kotlin.collections._ArraysJvm;

/* compiled from: SegmentedByteString.kt */
/* loaded from: classes2.dex */
public final class x extends g {

    /* renamed from: j, reason: collision with root package name */
    private final transient byte[][] f15543j;

    /* renamed from: k, reason: collision with root package name */
    private final transient int[] f15544k;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x(byte[][] bArr, int[] iArr) {
        super(g.f15494i.f());
        za.k.e(bArr, "segments");
        za.k.e(iArr, "directory");
        this.f15543j = bArr;
        this.f15544k = iArr;
    }

    private final g B() {
        return new g(A());
    }

    private final Object writeReplace() {
        g B = B();
        za.k.c(B, "null cannot be cast to non-null type java.lang.Object");
        return B;
    }

    public byte[] A() {
        byte[] bArr = new byte[t()];
        int length = z().length;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        while (i10 < length) {
            int i13 = y()[length + i10];
            int i14 = y()[i10];
            int i15 = i14 - i11;
            _ArraysJvm.f(z()[i10], bArr, i12, i13, i13 + i15);
            i12 += i15;
            i10++;
            i11 = i14;
        }
        return bArr;
    }

    @Override // me.g
    public String a() {
        return B().a();
    }

    @Override // me.g
    public g c(String str) {
        za.k.e(str, "algorithm");
        MessageDigest messageDigest = MessageDigest.getInstance(str);
        int length = z().length;
        int i10 = 0;
        int i11 = 0;
        while (i10 < length) {
            int i12 = y()[length + i10];
            int i13 = y()[i10];
            messageDigest.update(z()[i10], i12, i13 - i11);
            i10++;
            i11 = i13;
        }
        byte[] digest = messageDigest.digest();
        za.k.d(digest, "digestBytes");
        return new g(digest);
    }

    @Override // me.g
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof g) {
            g gVar = (g) obj;
            if (gVar.t() == t() && n(0, gVar, 0, t())) {
                return true;
            }
        }
        return false;
    }

    @Override // me.g
    public int h() {
        return y()[z().length - 1];
    }

    @Override // me.g
    public int hashCode() {
        int g6 = g();
        if (g6 != 0) {
            return g6;
        }
        int length = z().length;
        int i10 = 0;
        int i11 = 1;
        int i12 = 0;
        while (i10 < length) {
            int i13 = y()[length + i10];
            int i14 = y()[i10];
            byte[] bArr = z()[i10];
            int i15 = (i14 - i12) + i13;
            while (i13 < i15) {
                i11 = (i11 * 31) + bArr[i13];
                i13++;
            }
            i10++;
            i12 = i14;
        }
        p(i11);
        return i11;
    }

    @Override // me.g
    public String j() {
        return B().j();
    }

    @Override // me.g
    public byte[] l() {
        return A();
    }

    @Override // me.g
    public byte m(int i10) {
        b.b(y()[z().length - 1], i10, 1L);
        int b10 = ne.c.b(this, i10);
        return z()[b10][(i10 - (b10 == 0 ? 0 : y()[b10 - 1])) + y()[z().length + b10]];
    }

    @Override // me.g
    public boolean n(int i10, g gVar, int i11, int i12) {
        za.k.e(gVar, "other");
        if (i10 < 0 || i10 > t() - i12) {
            return false;
        }
        int i13 = i12 + i10;
        int b10 = ne.c.b(this, i10);
        while (i10 < i13) {
            int i14 = b10 == 0 ? 0 : y()[b10 - 1];
            int i15 = y()[b10] - i14;
            int i16 = y()[z().length + b10];
            int min = Math.min(i13, i15 + i14) - i10;
            if (!gVar.o(i11, z()[b10], i16 + (i10 - i14), min)) {
                return false;
            }
            i11 += min;
            i10 += min;
            b10++;
        }
        return true;
    }

    @Override // me.g
    public boolean o(int i10, byte[] bArr, int i11, int i12) {
        za.k.e(bArr, "other");
        if (i10 < 0 || i10 > t() - i12 || i11 < 0 || i11 > bArr.length - i12) {
            return false;
        }
        int i13 = i12 + i10;
        int b10 = ne.c.b(this, i10);
        while (i10 < i13) {
            int i14 = b10 == 0 ? 0 : y()[b10 - 1];
            int i15 = y()[b10] - i14;
            int i16 = y()[z().length + b10];
            int min = Math.min(i13, i15 + i14) - i10;
            if (!b.a(z()[b10], i16 + (i10 - i14), bArr, i11, min)) {
                return false;
            }
            i11 += min;
            i10 += min;
            b10++;
        }
        return true;
    }

    @Override // me.g
    public String toString() {
        return B().toString();
    }

    @Override // me.g
    public g v() {
        return B().v();
    }

    @Override // me.g
    public void x(d dVar, int i10, int i11) {
        za.k.e(dVar, "buffer");
        int i12 = i10 + i11;
        int b10 = ne.c.b(this, i10);
        while (i10 < i12) {
            int i13 = b10 == 0 ? 0 : y()[b10 - 1];
            int i14 = y()[b10] - i13;
            int i15 = y()[z().length + b10];
            int min = Math.min(i12, i14 + i13) - i10;
            int i16 = i15 + (i10 - i13);
            Segment segment = new Segment(z()[b10], i16, i16 + min, true, false);
            Segment segment2 = dVar.f15484e;
            if (segment2 == null) {
                segment.f15537g = segment;
                segment.f15536f = segment;
                dVar.f15484e = segment;
            } else {
                za.k.b(segment2);
                Segment segment3 = segment2.f15537g;
                za.k.b(segment3);
                segment3.c(segment);
            }
            i10 += min;
            b10++;
        }
        dVar.u0(dVar.v0() + i11);
    }

    public final int[] y() {
        return this.f15544k;
    }

    public final byte[][] z() {
        return this.f15543j;
    }
}
