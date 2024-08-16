package qc;

import java.util.NoSuchElementException;
import qc.d;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BoundedByteString.java */
/* loaded from: classes2.dex */
public class c extends p {

    /* renamed from: h, reason: collision with root package name */
    private final int f17254h;

    /* renamed from: i, reason: collision with root package name */
    private final int f17255i;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: BoundedByteString.java */
    /* loaded from: classes2.dex */
    public class b implements d.a {

        /* renamed from: e, reason: collision with root package name */
        private int f17256e;

        /* renamed from: f, reason: collision with root package name */
        private final int f17257f;

        @Override // java.util.Iterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Byte next() {
            return Byte.valueOf(nextByte());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17256e < this.f17257f;
        }

        @Override // qc.d.a
        public byte nextByte() {
            int i10 = this.f17256e;
            if (i10 < this.f17257f) {
                byte[] bArr = c.this.f17327f;
                this.f17256e = i10 + 1;
                return bArr[i10];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private b() {
            int B = c.this.B();
            this.f17256e = B;
            this.f17257f = B + c.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(byte[] bArr, int i10, int i11) {
        super(bArr);
        if (i10 < 0) {
            StringBuilder sb2 = new StringBuilder(29);
            sb2.append("Offset too small: ");
            sb2.append(i10);
            throw new IllegalArgumentException(sb2.toString());
        }
        if (i11 < 0) {
            StringBuilder sb3 = new StringBuilder(29);
            sb3.append("Length too small: ");
            sb3.append(i10);
            throw new IllegalArgumentException(sb3.toString());
        }
        if (i10 + i11 <= bArr.length) {
            this.f17254h = i10;
            this.f17255i = i11;
            return;
        }
        StringBuilder sb4 = new StringBuilder(48);
        sb4.append("Offset+Length too large: ");
        sb4.append(i10);
        sb4.append("+");
        sb4.append(i11);
        throw new IllegalArgumentException(sb4.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // qc.p
    public int B() {
        return this.f17254h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // qc.p, qc.d
    public void k(byte[] bArr, int i10, int i11, int i12) {
        System.arraycopy(this.f17327f, B() + i10, bArr, i11, i12);
    }

    @Override // qc.p, qc.d, java.lang.Iterable
    /* renamed from: o */
    public d.a iterator() {
        return new b();
    }

    @Override // qc.p, qc.d
    public int size() {
        return this.f17255i;
    }

    @Override // qc.p
    public byte z(int i10) {
        if (i10 >= 0) {
            if (i10 < size()) {
                return this.f17327f[this.f17254h + i10];
            }
            int size = size();
            StringBuilder sb2 = new StringBuilder(41);
            sb2.append("Index too large: ");
            sb2.append(i10);
            sb2.append(", ");
            sb2.append(size);
            throw new ArrayIndexOutOfBoundsException(sb2.toString());
        }
        StringBuilder sb3 = new StringBuilder(28);
        sb3.append("Index too small: ");
        sb3.append(i10);
        throw new ArrayIndexOutOfBoundsException(sb3.toString());
    }
}
