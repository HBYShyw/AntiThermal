package qc;

import java.io.OutputStream;
import java.util.NoSuchElementException;
import qc.d;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LiteralByteString.java */
/* loaded from: classes2.dex */
public class p extends d {

    /* renamed from: f, reason: collision with root package name */
    protected final byte[] f17327f;

    /* renamed from: g, reason: collision with root package name */
    private int f17328g = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: LiteralByteString.java */
    /* loaded from: classes2.dex */
    public class b implements d.a {

        /* renamed from: e, reason: collision with root package name */
        private int f17329e;

        /* renamed from: f, reason: collision with root package name */
        private final int f17330f;

        @Override // java.util.Iterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Byte next() {
            return Byte.valueOf(nextByte());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17329e < this.f17330f;
        }

        @Override // qc.d.a
        public byte nextByte() {
            try {
                byte[] bArr = p.this.f17327f;
                int i10 = this.f17329e;
                this.f17329e = i10 + 1;
                return bArr[i10];
            } catch (ArrayIndexOutOfBoundsException e10) {
                throw new NoSuchElementException(e10.getMessage());
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private b() {
            this.f17329e = 0;
            this.f17330f = p.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(byte[] bArr) {
        this.f17327f = bArr;
    }

    static int C(int i10, byte[] bArr, int i11, int i12) {
        for (int i13 = i11; i13 < i11 + i12; i13++) {
            i10 = (i10 * 31) + bArr[i13];
        }
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean A(p pVar, int i10, int i11) {
        if (i11 <= pVar.size()) {
            if (i10 + i11 <= pVar.size()) {
                byte[] bArr = this.f17327f;
                byte[] bArr2 = pVar.f17327f;
                int B = B() + i11;
                int B2 = B();
                int B3 = pVar.B() + i10;
                while (B2 < B) {
                    if (bArr[B2] != bArr2[B3]) {
                        return false;
                    }
                    B2++;
                    B3++;
                }
                return true;
            }
            int size = pVar.size();
            StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: ");
            sb2.append(i10);
            sb2.append(", ");
            sb2.append(i11);
            sb2.append(", ");
            sb2.append(size);
            throw new IllegalArgumentException(sb2.toString());
        }
        int size2 = size();
        StringBuilder sb3 = new StringBuilder(40);
        sb3.append("Length too large: ");
        sb3.append(i11);
        sb3.append(size2);
        throw new IllegalArgumentException(sb3.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int B() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof d) || size() != ((d) obj).size()) {
            return false;
        }
        if (size() == 0) {
            return true;
        }
        if (obj instanceof p) {
            return A((p) obj, 0, size());
        }
        if (obj instanceof u) {
            return obj.equals(this);
        }
        String valueOf = String.valueOf(obj.getClass());
        StringBuilder sb2 = new StringBuilder(valueOf.length() + 49);
        sb2.append("Has a new type of ByteString been created? Found ");
        sb2.append(valueOf);
        throw new IllegalArgumentException(sb2.toString());
    }

    public int hashCode() {
        int i10 = this.f17328g;
        if (i10 == 0) {
            int size = size();
            i10 = r(size, 0, size);
            if (i10 == 0) {
                i10 = 1;
            }
            this.f17328g = i10;
        }
        return i10;
    }

    @Override // qc.d
    protected void k(byte[] bArr, int i10, int i11, int i12) {
        System.arraycopy(this.f17327f, i10, bArr, i11, i12);
    }

    @Override // qc.d
    protected int l() {
        return 0;
    }

    @Override // qc.d
    protected boolean m() {
        return true;
    }

    @Override // qc.d
    public boolean n() {
        int B = B();
        return y.f(this.f17327f, B, size() + B);
    }

    @Override // qc.d, java.lang.Iterable
    /* renamed from: o, reason: merged with bridge method [inline-methods] */
    public d.a iterator() {
        return new b();
    }

    @Override // qc.d
    public e p() {
        return e.h(this);
    }

    @Override // qc.d
    protected int r(int i10, int i11, int i12) {
        return C(i10, this.f17327f, B() + i11, i12);
    }

    @Override // qc.d
    protected int s(int i10, int i11, int i12) {
        int B = B() + i11;
        return y.g(i10, this.f17327f, B, i12 + B);
    }

    @Override // qc.d
    public int size() {
        return this.f17327f.length;
    }

    @Override // qc.d
    protected int t() {
        return this.f17328g;
    }

    @Override // qc.d
    public String v(String str) {
        return new String(this.f17327f, B(), size(), str);
    }

    @Override // qc.d
    void y(OutputStream outputStream, int i10, int i11) {
        outputStream.write(this.f17327f, B() + i10, i11);
    }

    public byte z(int i10) {
        return this.f17327f[i10];
    }
}
