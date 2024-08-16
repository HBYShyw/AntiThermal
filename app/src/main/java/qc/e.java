package qc;

import java.io.InputStream;
import java.util.ArrayList;
import qc.q;

/* compiled from: CodedInputStream.java */
/* loaded from: classes2.dex */
public final class e {

    /* renamed from: a, reason: collision with root package name */
    private final byte[] f17266a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f17267b;

    /* renamed from: c, reason: collision with root package name */
    private int f17268c;

    /* renamed from: d, reason: collision with root package name */
    private int f17269d;

    /* renamed from: e, reason: collision with root package name */
    private int f17270e;

    /* renamed from: f, reason: collision with root package name */
    private final InputStream f17271f;

    /* renamed from: g, reason: collision with root package name */
    private int f17272g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f17273h;

    /* renamed from: i, reason: collision with root package name */
    private int f17274i;

    /* renamed from: j, reason: collision with root package name */
    private int f17275j;

    /* renamed from: k, reason: collision with root package name */
    private int f17276k;

    /* renamed from: l, reason: collision with root package name */
    private int f17277l;

    /* renamed from: m, reason: collision with root package name */
    private int f17278m;

    /* renamed from: n, reason: collision with root package name */
    private a f17279n;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: CodedInputStream.java */
    /* loaded from: classes2.dex */
    public interface a {
        void onRefill();
    }

    private e(InputStream inputStream) {
        this.f17273h = false;
        this.f17275j = Integer.MAX_VALUE;
        this.f17277l = 64;
        this.f17278m = 67108864;
        this.f17279n = null;
        this.f17266a = new byte[4096];
        this.f17268c = 0;
        this.f17270e = 0;
        this.f17274i = 0;
        this.f17271f = inputStream;
        this.f17267b = false;
    }

    public static int B(int i10, InputStream inputStream) {
        if ((i10 & 128) == 0) {
            return i10;
        }
        int i11 = i10 & 127;
        int i12 = 7;
        while (i12 < 32) {
            int read = inputStream.read();
            if (read == -1) {
                throw k.k();
            }
            i11 |= (read & 127) << i12;
            if ((read & 128) == 0) {
                return i11;
            }
            i12 += 7;
        }
        while (i12 < 64) {
            int read2 = inputStream.read();
            if (read2 == -1) {
                throw k.k();
            }
            if ((read2 & 128) == 0) {
                return i11;
            }
            i12 += 7;
        }
        throw k.f();
    }

    private void N() {
        int i10 = this.f17268c + this.f17269d;
        this.f17268c = i10;
        int i11 = this.f17274i + i10;
        int i12 = this.f17275j;
        if (i11 > i12) {
            int i13 = i11 - i12;
            this.f17269d = i13;
            this.f17268c = i10 - i13;
            return;
        }
        this.f17269d = 0;
    }

    private void O(int i10) {
        if (!T(i10)) {
            throw k.k();
        }
    }

    private void S(int i10) {
        if (i10 >= 0) {
            int i11 = this.f17274i;
            int i12 = this.f17270e;
            int i13 = i11 + i12 + i10;
            int i14 = this.f17275j;
            if (i13 <= i14) {
                int i15 = this.f17268c;
                int i16 = i15 - i12;
                this.f17270e = i15;
                O(1);
                while (true) {
                    int i17 = i10 - i16;
                    int i18 = this.f17268c;
                    if (i17 > i18) {
                        i16 += i18;
                        this.f17270e = i18;
                        O(1);
                    } else {
                        this.f17270e = i17;
                        return;
                    }
                }
            } else {
                R((i14 - i11) - i12);
                throw k.k();
            }
        } else {
            throw k.g();
        }
    }

    private boolean T(int i10) {
        int i11 = this.f17270e;
        if (i11 + i10 > this.f17268c) {
            if (this.f17274i + i11 + i10 > this.f17275j) {
                return false;
            }
            a aVar = this.f17279n;
            if (aVar != null) {
                aVar.onRefill();
            }
            if (this.f17271f != null) {
                int i12 = this.f17270e;
                if (i12 > 0) {
                    int i13 = this.f17268c;
                    if (i13 > i12) {
                        byte[] bArr = this.f17266a;
                        System.arraycopy(bArr, i12, bArr, 0, i13 - i12);
                    }
                    this.f17274i += i12;
                    this.f17268c -= i12;
                    this.f17270e = 0;
                }
                InputStream inputStream = this.f17271f;
                byte[] bArr2 = this.f17266a;
                int i14 = this.f17268c;
                int read = inputStream.read(bArr2, i14, bArr2.length - i14);
                if (read == 0 || read < -1 || read > this.f17266a.length) {
                    StringBuilder sb2 = new StringBuilder(102);
                    sb2.append("InputStream#read(byte[]) returned invalid result: ");
                    sb2.append(read);
                    sb2.append("\nThe InputStream implementation is buggy.");
                    throw new IllegalStateException(sb2.toString());
                }
                if (read > 0) {
                    this.f17268c += read;
                    if ((this.f17274i + i10) - this.f17278m <= 0) {
                        N();
                        if (this.f17268c >= i10) {
                            return true;
                        }
                        return T(i10);
                    }
                    throw k.j();
                }
            }
            return false;
        }
        StringBuilder sb3 = new StringBuilder(77);
        sb3.append("refillBuffer() called when ");
        sb3.append(i10);
        sb3.append(" bytes were already available in buffer");
        throw new IllegalStateException(sb3.toString());
    }

    public static int b(int i10) {
        return (-(i10 & 1)) ^ (i10 >>> 1);
    }

    public static long c(long j10) {
        return (-(j10 & 1)) ^ (j10 >>> 1);
    }

    private void d(int i10) {
        if (this.f17268c - this.f17270e < i10) {
            O(i10);
        }
    }

    public static e g(InputStream inputStream) {
        return new e(inputStream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static e h(p pVar) {
        e eVar = new e(pVar);
        try {
            eVar.j(pVar.size());
            return eVar;
        } catch (k e10) {
            throw new IllegalArgumentException(e10);
        }
    }

    private byte[] x(int i10) {
        if (i10 <= 0) {
            if (i10 == 0) {
                return j.f17315a;
            }
            throw k.g();
        }
        int i11 = this.f17274i;
        int i12 = this.f17270e;
        int i13 = i11 + i12 + i10;
        int i14 = this.f17275j;
        if (i13 > i14) {
            R((i14 - i11) - i12);
            throw k.k();
        }
        if (i10 < 4096) {
            byte[] bArr = new byte[i10];
            int i15 = this.f17268c - i12;
            System.arraycopy(this.f17266a, i12, bArr, 0, i15);
            this.f17270e = this.f17268c;
            int i16 = i10 - i15;
            d(i16);
            System.arraycopy(this.f17266a, 0, bArr, i15, i16);
            this.f17270e = i16;
            return bArr;
        }
        int i17 = this.f17268c;
        this.f17274i = i11 + i17;
        this.f17270e = 0;
        this.f17268c = 0;
        int i18 = i17 - i12;
        int i19 = i10 - i18;
        ArrayList<byte[]> arrayList = new ArrayList();
        while (i19 > 0) {
            int min = Math.min(i19, 4096);
            byte[] bArr2 = new byte[min];
            int i20 = 0;
            while (i20 < min) {
                InputStream inputStream = this.f17271f;
                int read = inputStream == null ? -1 : inputStream.read(bArr2, i20, min - i20);
                if (read == -1) {
                    throw k.k();
                }
                this.f17274i += read;
                i20 += read;
            }
            i19 -= min;
            arrayList.add(bArr2);
        }
        byte[] bArr3 = new byte[i10];
        System.arraycopy(this.f17266a, i12, bArr3, 0, i18);
        for (byte[] bArr4 : arrayList) {
            System.arraycopy(bArr4, 0, bArr3, i18, bArr4.length);
            i18 += bArr4.length;
        }
        return bArr3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x007a, code lost:
    
        if (r2[r3] < 0) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int A() {
        int i10;
        long j10;
        int i11 = this.f17270e;
        int i12 = this.f17268c;
        if (i12 != i11) {
            byte[] bArr = this.f17266a;
            int i13 = i11 + 1;
            byte b10 = bArr[i11];
            if (b10 >= 0) {
                this.f17270e = i13;
                return b10;
            }
            if (i12 - i13 >= 9) {
                int i14 = i13 + 1;
                int i15 = b10 ^ (bArr[i13] << 7);
                long j11 = i15;
                if (j11 >= 0) {
                    int i16 = i14 + 1;
                    int i17 = i15 ^ (bArr[i14] << 14);
                    long j12 = i17;
                    if (j12 >= 0) {
                        i10 = (int) (16256 ^ j12);
                    } else {
                        i14 = i16 + 1;
                        j11 = i17 ^ (bArr[i16] << 21);
                        if (j11 < 0) {
                            j10 = -2080896;
                        } else {
                            i16 = i14 + 1;
                            i10 = (int) ((r0 ^ (r1 << 28)) ^ 266354560);
                            if (bArr[i14] < 0) {
                                i14 = i16 + 1;
                                if (bArr[i16] < 0) {
                                    i16 = i14 + 1;
                                    if (bArr[i14] < 0) {
                                        i14 = i16 + 1;
                                        if (bArr[i16] < 0) {
                                            i16 = i14 + 1;
                                            if (bArr[i14] < 0) {
                                                i14 = i16 + 1;
                                            }
                                        }
                                    }
                                }
                                this.f17270e = i14;
                                return i10;
                            }
                        }
                    }
                    i14 = i16;
                    this.f17270e = i14;
                    return i10;
                }
                j10 = -128;
                i10 = (int) (j11 ^ j10);
                this.f17270e = i14;
                return i10;
            }
        }
        return (int) D();
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b6, code lost:
    
        if (r2[r0] < 0) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public long C() {
        long j10;
        long j11;
        long j12;
        int i10 = this.f17270e;
        int i11 = this.f17268c;
        if (i11 != i10) {
            byte[] bArr = this.f17266a;
            int i12 = i10 + 1;
            byte b10 = bArr[i10];
            if (b10 >= 0) {
                this.f17270e = i12;
                return b10;
            }
            if (i11 - i12 >= 9) {
                int i13 = i12 + 1;
                long j13 = b10 ^ (bArr[i12] << 7);
                if (j13 >= 0) {
                    int i14 = i13 + 1;
                    long j14 = j13 ^ (bArr[i13] << 14);
                    if (j14 >= 0) {
                        j12 = 16256;
                    } else {
                        i13 = i14 + 1;
                        j13 = j14 ^ (bArr[i14] << 21);
                        if (j13 < 0) {
                            j11 = -2080896;
                        } else {
                            i14 = i13 + 1;
                            j14 = j13 ^ (bArr[i13] << 28);
                            if (j14 >= 0) {
                                j12 = 266354560;
                            } else {
                                i13 = i14 + 1;
                                j13 = j14 ^ (bArr[i14] << 35);
                                if (j13 < 0) {
                                    j11 = -34093383808L;
                                } else {
                                    i14 = i13 + 1;
                                    j14 = j13 ^ (bArr[i13] << 42);
                                    if (j14 >= 0) {
                                        j12 = 4363953127296L;
                                    } else {
                                        i13 = i14 + 1;
                                        j13 = j14 ^ (bArr[i14] << 49);
                                        if (j13 >= 0) {
                                            int i15 = i13 + 1;
                                            long j15 = (j13 ^ (bArr[i13] << 56)) ^ 71499008037633920L;
                                            i13 = j15 < 0 ? i15 + 1 : i15;
                                            j10 = j15;
                                            this.f17270e = i13;
                                            return j10;
                                        }
                                        j11 = -558586000294016L;
                                    }
                                }
                            }
                        }
                    }
                    j10 = j14 ^ j12;
                    i13 = i14;
                    this.f17270e = i13;
                    return j10;
                }
                j11 = -128;
                j10 = j13 ^ j11;
                this.f17270e = i13;
                return j10;
            }
        }
        return D();
    }

    long D() {
        long j10 = 0;
        for (int i10 = 0; i10 < 64; i10 += 7) {
            j10 |= (r3 & Byte.MAX_VALUE) << i10;
            if ((w() & 128) == 0) {
                return j10;
            }
        }
        throw k.f();
    }

    public int E() {
        return y();
    }

    public long F() {
        return z();
    }

    public int G() {
        return b(A());
    }

    public long H() {
        return c(C());
    }

    public String I() {
        int A = A();
        int i10 = this.f17268c;
        int i11 = this.f17270e;
        if (A > i10 - i11 || A <= 0) {
            return A == 0 ? "" : new String(x(A), "UTF-8");
        }
        String str = new String(this.f17266a, i11, A, "UTF-8");
        this.f17270e += A;
        return str;
    }

    public String J() {
        byte[] x10;
        int A = A();
        int i10 = this.f17270e;
        if (A <= this.f17268c - i10 && A > 0) {
            x10 = this.f17266a;
            this.f17270e = i10 + A;
        } else {
            if (A == 0) {
                return "";
            }
            x10 = x(A);
            i10 = 0;
        }
        if (y.f(x10, i10, i10 + A)) {
            return new String(x10, i10, A, "UTF-8");
        }
        throw k.d();
    }

    public int K() {
        if (f()) {
            this.f17272g = 0;
            return 0;
        }
        int A = A();
        this.f17272g = A;
        if (z.a(A) != 0) {
            return this.f17272g;
        }
        throw k.c();
    }

    public int L() {
        return A();
    }

    public long M() {
        return C();
    }

    public boolean P(int i10, f fVar) {
        int b10 = z.b(i10);
        if (b10 == 0) {
            long t7 = t();
            fVar.o0(i10);
            fVar.z0(t7);
            return true;
        }
        if (b10 == 1) {
            long z10 = z();
            fVar.o0(i10);
            fVar.V(z10);
            return true;
        }
        if (b10 == 2) {
            d l10 = l();
            fVar.o0(i10);
            fVar.P(l10);
            return true;
        }
        if (b10 == 3) {
            fVar.o0(i10);
            Q(fVar);
            int c10 = z.c(z.a(i10), 4);
            a(c10);
            fVar.o0(c10);
            return true;
        }
        if (b10 == 4) {
            return false;
        }
        if (b10 == 5) {
            int y4 = y();
            fVar.o0(i10);
            fVar.U(y4);
            return true;
        }
        throw k.e();
    }

    public void Q(f fVar) {
        int K;
        do {
            K = K();
            if (K == 0) {
                return;
            }
        } while (P(K, fVar));
    }

    public void R(int i10) {
        int i11 = this.f17268c;
        int i12 = this.f17270e;
        if (i10 <= i11 - i12 && i10 >= 0) {
            this.f17270e = i12 + i10;
        } else {
            S(i10);
        }
    }

    public void a(int i10) {
        if (this.f17272g != i10) {
            throw k.b();
        }
    }

    public int e() {
        int i10 = this.f17275j;
        if (i10 == Integer.MAX_VALUE) {
            return -1;
        }
        return i10 - (this.f17274i + this.f17270e);
    }

    public boolean f() {
        return this.f17270e == this.f17268c && !T(1);
    }

    public void i(int i10) {
        this.f17275j = i10;
        N();
    }

    public int j(int i10) {
        if (i10 >= 0) {
            int i11 = i10 + this.f17274i + this.f17270e;
            int i12 = this.f17275j;
            if (i11 <= i12) {
                this.f17275j = i11;
                N();
                return i12;
            }
            throw k.k();
        }
        throw k.g();
    }

    public boolean k() {
        return C() != 0;
    }

    public d l() {
        int A = A();
        int i10 = this.f17268c;
        int i11 = this.f17270e;
        if (A > i10 - i11 || A <= 0) {
            if (A == 0) {
                return d.f17259e;
            }
            return new p(x(A));
        }
        d cVar = (this.f17267b && this.f17273h) ? new c(this.f17266a, this.f17270e, A) : d.g(this.f17266a, i11, A);
        this.f17270e += A;
        return cVar;
    }

    public double m() {
        return Double.longBitsToDouble(z());
    }

    public int n() {
        return A();
    }

    public int o() {
        return y();
    }

    public long p() {
        return z();
    }

    public float q() {
        return Float.intBitsToFloat(y());
    }

    public void r(int i10, q.a aVar, g gVar) {
        int i11 = this.f17276k;
        if (i11 < this.f17277l) {
            this.f17276k = i11 + 1;
            aVar.m(this, gVar);
            a(z.c(i10, 4));
            this.f17276k--;
            return;
        }
        throw k.h();
    }

    public int s() {
        return A();
    }

    public long t() {
        return C();
    }

    public <T extends q> T u(s<T> sVar, g gVar) {
        int A = A();
        if (this.f17276k < this.f17277l) {
            int j10 = j(A);
            this.f17276k++;
            T a10 = sVar.a(this, gVar);
            a(0);
            this.f17276k--;
            i(j10);
            return a10;
        }
        throw k.h();
    }

    public void v(q.a aVar, g gVar) {
        int A = A();
        if (this.f17276k < this.f17277l) {
            int j10 = j(A);
            this.f17276k++;
            aVar.m(this, gVar);
            a(0);
            this.f17276k--;
            i(j10);
            return;
        }
        throw k.h();
    }

    public byte w() {
        if (this.f17270e == this.f17268c) {
            O(1);
        }
        byte[] bArr = this.f17266a;
        int i10 = this.f17270e;
        this.f17270e = i10 + 1;
        return bArr[i10];
    }

    public int y() {
        int i10 = this.f17270e;
        if (this.f17268c - i10 < 4) {
            O(4);
            i10 = this.f17270e;
        }
        byte[] bArr = this.f17266a;
        this.f17270e = i10 + 4;
        return (bArr[i10] & 255) | ((bArr[i10 + 1] & 255) << 8) | ((bArr[i10 + 2] & 255) << 16) | ((bArr[i10 + 3] & 255) << 24);
    }

    public long z() {
        int i10 = this.f17270e;
        if (this.f17268c - i10 < 8) {
            O(8);
            i10 = this.f17270e;
        }
        byte[] bArr = this.f17266a;
        this.f17270e = i10 + 8;
        return ((bArr[i10 + 7] & 255) << 56) | (bArr[i10] & 255) | ((bArr[i10 + 1] & 255) << 8) | ((bArr[i10 + 2] & 255) << 16) | ((bArr[i10 + 3] & 255) << 24) | ((bArr[i10 + 4] & 255) << 32) | ((bArr[i10 + 5] & 255) << 40) | ((bArr[i10 + 6] & 255) << 48);
    }

    private e(p pVar) {
        this.f17273h = false;
        this.f17275j = Integer.MAX_VALUE;
        this.f17277l = 64;
        this.f17278m = 67108864;
        this.f17279n = null;
        this.f17266a = pVar.f17327f;
        int B = pVar.B();
        this.f17270e = B;
        this.f17268c = B + pVar.size();
        this.f17274i = -this.f17270e;
        this.f17271f = null;
        this.f17267b = true;
    }
}
