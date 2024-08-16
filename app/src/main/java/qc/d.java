package qc;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* compiled from: ByteString.java */
/* loaded from: classes2.dex */
public abstract class d implements Iterable<Byte> {

    /* renamed from: e, reason: collision with root package name */
    public static final d f17259e = new p(new byte[0]);

    /* compiled from: ByteString.java */
    /* loaded from: classes2.dex */
    public interface a extends Iterator<Byte> {
        byte nextByte();
    }

    private static d c(Iterator<d> it, int i10) {
        if (i10 == 1) {
            return it.next();
        }
        int i11 = i10 >>> 1;
        return c(it, i11).d(c(it, i10 - i11));
    }

    public static d e(Iterable<d> iterable) {
        Collection collection;
        if (!(iterable instanceof Collection)) {
            collection = new ArrayList();
            Iterator<d> it = iterable.iterator();
            while (it.hasNext()) {
                collection.add(it.next());
            }
        } else {
            collection = (Collection) iterable;
        }
        if (collection.isEmpty()) {
            return f17259e;
        }
        return c(collection.iterator(), collection.size());
    }

    public static d f(byte[] bArr) {
        return g(bArr, 0, bArr.length);
    }

    public static d g(byte[] bArr, int i10, int i11) {
        byte[] bArr2 = new byte[i11];
        System.arraycopy(bArr, i10, bArr2, 0, i11);
        return new p(bArr2);
    }

    public static d h(String str) {
        try {
            return new p(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e10) {
            throw new RuntimeException("UTF-8 not supported?", e10);
        }
    }

    public static b q() {
        return new b(128);
    }

    public d d(d dVar) {
        int size = size();
        int size2 = dVar.size();
        if (size + size2 < 2147483647L) {
            return u.C(this, dVar);
        }
        StringBuilder sb2 = new StringBuilder(53);
        sb2.append("ByteString would be too long: ");
        sb2.append(size);
        sb2.append("+");
        sb2.append(size2);
        throw new IllegalArgumentException(sb2.toString());
    }

    public void i(byte[] bArr, int i10, int i11, int i12) {
        if (i10 < 0) {
            StringBuilder sb2 = new StringBuilder(30);
            sb2.append("Source offset < 0: ");
            sb2.append(i10);
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        if (i11 < 0) {
            StringBuilder sb3 = new StringBuilder(30);
            sb3.append("Target offset < 0: ");
            sb3.append(i11);
            throw new IndexOutOfBoundsException(sb3.toString());
        }
        if (i12 >= 0) {
            int i13 = i10 + i12;
            if (i13 <= size()) {
                int i14 = i11 + i12;
                if (i14 <= bArr.length) {
                    if (i12 > 0) {
                        k(bArr, i10, i11, i12);
                        return;
                    }
                    return;
                } else {
                    StringBuilder sb4 = new StringBuilder(34);
                    sb4.append("Target end offset < 0: ");
                    sb4.append(i14);
                    throw new IndexOutOfBoundsException(sb4.toString());
                }
            }
            StringBuilder sb5 = new StringBuilder(34);
            sb5.append("Source end offset < 0: ");
            sb5.append(i13);
            throw new IndexOutOfBoundsException(sb5.toString());
        }
        StringBuilder sb6 = new StringBuilder(23);
        sb6.append("Length < 0: ");
        sb6.append(i12);
        throw new IndexOutOfBoundsException(sb6.toString());
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void k(byte[] bArr, int i10, int i11, int i12);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int l();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean m();

    public abstract boolean n();

    @Override // java.lang.Iterable
    /* renamed from: o */
    public abstract a iterator();

    public abstract e p();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int r(int i10, int i11, int i12);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int s(int i10, int i11, int i12);

    public abstract int size();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int t();

    public String toString() {
        return String.format("<ByteString@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), Integer.valueOf(size()));
    }

    public byte[] u() {
        int size = size();
        if (size == 0) {
            return j.f17315a;
        }
        byte[] bArr = new byte[size];
        k(bArr, 0, 0, size);
        return bArr;
    }

    public abstract String v(String str);

    public String w() {
        try {
            return v("UTF-8");
        } catch (UnsupportedEncodingException e10) {
            throw new RuntimeException("UTF-8 not supported?", e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(OutputStream outputStream, int i10, int i11) {
        if (i10 < 0) {
            StringBuilder sb2 = new StringBuilder(30);
            sb2.append("Source offset < 0: ");
            sb2.append(i10);
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        if (i11 >= 0) {
            int i12 = i10 + i11;
            if (i12 <= size()) {
                if (i11 > 0) {
                    y(outputStream, i10, i11);
                    return;
                }
                return;
            } else {
                StringBuilder sb3 = new StringBuilder(39);
                sb3.append("Source end offset exceeded: ");
                sb3.append(i12);
                throw new IndexOutOfBoundsException(sb3.toString());
            }
        }
        StringBuilder sb4 = new StringBuilder(23);
        sb4.append("Length < 0: ");
        sb4.append(i11);
        throw new IndexOutOfBoundsException(sb4.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void y(OutputStream outputStream, int i10, int i11);

    /* compiled from: ByteString.java */
    /* loaded from: classes2.dex */
    public static final class b extends OutputStream {

        /* renamed from: j, reason: collision with root package name */
        private static final byte[] f17260j = new byte[0];

        /* renamed from: e, reason: collision with root package name */
        private final int f17261e;

        /* renamed from: f, reason: collision with root package name */
        private final ArrayList<d> f17262f;

        /* renamed from: g, reason: collision with root package name */
        private int f17263g;

        /* renamed from: h, reason: collision with root package name */
        private byte[] f17264h;

        /* renamed from: i, reason: collision with root package name */
        private int f17265i;

        b(int i10) {
            if (i10 >= 0) {
                this.f17261e = i10;
                this.f17262f = new ArrayList<>();
                this.f17264h = new byte[i10];
                return;
            }
            throw new IllegalArgumentException("Buffer size < 0");
        }

        private byte[] b(byte[] bArr, int i10) {
            byte[] bArr2 = new byte[i10];
            System.arraycopy(bArr, 0, bArr2, 0, Math.min(bArr.length, i10));
            return bArr2;
        }

        private void c(int i10) {
            this.f17262f.add(new p(this.f17264h));
            int length = this.f17263g + this.f17264h.length;
            this.f17263g = length;
            this.f17264h = new byte[Math.max(this.f17261e, Math.max(i10, length >>> 1))];
            this.f17265i = 0;
        }

        private void m() {
            int i10 = this.f17265i;
            byte[] bArr = this.f17264h;
            if (i10 >= bArr.length) {
                this.f17262f.add(new p(this.f17264h));
                this.f17264h = f17260j;
            } else if (i10 > 0) {
                this.f17262f.add(new p(b(bArr, i10)));
            }
            this.f17263g += this.f17265i;
            this.f17265i = 0;
        }

        public String toString() {
            return String.format("<ByteString.Output@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), Integer.valueOf(u()));
        }

        public synchronized int u() {
            return this.f17263g + this.f17265i;
        }

        public synchronized d v() {
            m();
            return d.e(this.f17262f);
        }

        @Override // java.io.OutputStream
        public synchronized void write(int i10) {
            if (this.f17265i == this.f17264h.length) {
                c(1);
            }
            byte[] bArr = this.f17264h;
            int i11 = this.f17265i;
            this.f17265i = i11 + 1;
            bArr[i11] = (byte) i10;
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] bArr, int i10, int i11) {
            byte[] bArr2 = this.f17264h;
            int length = bArr2.length;
            int i12 = this.f17265i;
            if (i11 <= length - i12) {
                System.arraycopy(bArr, i10, bArr2, i12, i11);
                this.f17265i += i11;
            } else {
                int length2 = bArr2.length - i12;
                System.arraycopy(bArr, i10, bArr2, i12, length2);
                int i13 = i11 - length2;
                c(i13);
                System.arraycopy(bArr, i10 + length2, this.f17264h, 0, i13);
                this.f17265i = i13;
            }
        }
    }
}
