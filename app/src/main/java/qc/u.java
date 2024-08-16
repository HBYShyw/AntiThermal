package qc;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;
import qc.d;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: RopeByteString.java */
/* loaded from: classes2.dex */
public class u extends qc.d {

    /* renamed from: l, reason: collision with root package name */
    private static final int[] f17332l;

    /* renamed from: f, reason: collision with root package name */
    private final int f17333f;

    /* renamed from: g, reason: collision with root package name */
    private final qc.d f17334g;

    /* renamed from: h, reason: collision with root package name */
    private final qc.d f17335h;

    /* renamed from: i, reason: collision with root package name */
    private final int f17336i;

    /* renamed from: j, reason: collision with root package name */
    private final int f17337j;

    /* renamed from: k, reason: collision with root package name */
    private int f17338k;

    /* compiled from: RopeByteString.java */
    /* loaded from: classes2.dex */
    private static class b {

        /* renamed from: a, reason: collision with root package name */
        private final Stack<qc.d> f17339a;

        private b() {
            this.f17339a = new Stack<>();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public qc.d b(qc.d dVar, qc.d dVar2) {
            c(dVar);
            c(dVar2);
            qc.d pop = this.f17339a.pop();
            while (!this.f17339a.isEmpty()) {
                pop = new u(this.f17339a.pop(), pop);
            }
            return pop;
        }

        private void c(qc.d dVar) {
            if (dVar.m()) {
                e(dVar);
                return;
            }
            if (dVar instanceof u) {
                u uVar = (u) dVar;
                c(uVar.f17334g);
                c(uVar.f17335h);
            } else {
                String valueOf = String.valueOf(dVar.getClass());
                StringBuilder sb2 = new StringBuilder(valueOf.length() + 49);
                sb2.append("Has a new type of ByteString been created? Found ");
                sb2.append(valueOf);
                throw new IllegalArgumentException(sb2.toString());
            }
        }

        private int d(int i10) {
            int binarySearch = Arrays.binarySearch(u.f17332l, i10);
            return binarySearch < 0 ? (-(binarySearch + 1)) - 1 : binarySearch;
        }

        private void e(qc.d dVar) {
            int d10 = d(dVar.size());
            int i10 = u.f17332l[d10 + 1];
            if (!this.f17339a.isEmpty() && this.f17339a.peek().size() < i10) {
                int i11 = u.f17332l[d10];
                qc.d pop = this.f17339a.pop();
                while (true) {
                    if (this.f17339a.isEmpty() || this.f17339a.peek().size() >= i11) {
                        break;
                    } else {
                        pop = new u(this.f17339a.pop(), pop);
                    }
                }
                u uVar = new u(pop, dVar);
                while (!this.f17339a.isEmpty()) {
                    if (this.f17339a.peek().size() >= u.f17332l[d(uVar.size()) + 1]) {
                        break;
                    } else {
                        uVar = new u(this.f17339a.pop(), uVar);
                    }
                }
                this.f17339a.push(uVar);
                return;
            }
            this.f17339a.push(dVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: RopeByteString.java */
    /* loaded from: classes2.dex */
    public static class c implements Iterator<p> {

        /* renamed from: e, reason: collision with root package name */
        private final Stack<u> f17340e;

        /* renamed from: f, reason: collision with root package name */
        private p f17341f;

        private p a(qc.d dVar) {
            while (dVar instanceof u) {
                u uVar = (u) dVar;
                this.f17340e.push(uVar);
                dVar = uVar.f17334g;
            }
            return (p) dVar;
        }

        private p b() {
            while (!this.f17340e.isEmpty()) {
                p a10 = a(this.f17340e.pop().f17335h);
                if (!a10.isEmpty()) {
                    return a10;
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public p next() {
            p pVar = this.f17341f;
            if (pVar != null) {
                this.f17341f = b();
                return pVar;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17341f != null;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private c(qc.d dVar) {
            this.f17340e = new Stack<>();
            this.f17341f = a(dVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: RopeByteString.java */
    /* loaded from: classes2.dex */
    public class d implements d.a {

        /* renamed from: e, reason: collision with root package name */
        private final c f17342e;

        /* renamed from: f, reason: collision with root package name */
        private d.a f17343f;

        /* renamed from: g, reason: collision with root package name */
        int f17344g;

        @Override // java.util.Iterator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Byte next() {
            return Byte.valueOf(nextByte());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f17344g > 0;
        }

        @Override // qc.d.a
        public byte nextByte() {
            if (!this.f17343f.hasNext()) {
                this.f17343f = this.f17342e.next().iterator();
            }
            this.f17344g--;
            return this.f17343f.nextByte();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private d() {
            c cVar = new c(u.this);
            this.f17342e = cVar;
            this.f17343f = cVar.next().iterator();
            this.f17344g = u.this.size();
        }
    }

    static {
        ArrayList arrayList = new ArrayList();
        int i10 = 1;
        int i11 = 1;
        while (i10 > 0) {
            arrayList.add(Integer.valueOf(i10));
            int i12 = i11 + i10;
            i11 = i10;
            i10 = i12;
        }
        arrayList.add(Integer.MAX_VALUE);
        f17332l = new int[arrayList.size()];
        int i13 = 0;
        while (true) {
            int[] iArr = f17332l;
            if (i13 >= iArr.length) {
                return;
            }
            iArr[i13] = ((Integer) arrayList.get(i13)).intValue();
            i13++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static qc.d C(qc.d dVar, qc.d dVar2) {
        u uVar = dVar instanceof u ? (u) dVar : null;
        if (dVar2.size() == 0) {
            return dVar;
        }
        if (dVar.size() != 0) {
            int size = dVar.size() + dVar2.size();
            if (size < 128) {
                return D(dVar, dVar2);
            }
            if (uVar != null && uVar.f17335h.size() + dVar2.size() < 128) {
                dVar2 = new u(uVar.f17334g, D(uVar.f17335h, dVar2));
            } else if (uVar != null && uVar.f17334g.l() > uVar.f17335h.l() && uVar.l() > dVar2.l()) {
                dVar2 = new u(uVar.f17334g, new u(uVar.f17335h, dVar2));
            } else {
                if (size >= f17332l[Math.max(dVar.l(), dVar2.l()) + 1]) {
                    return new u(dVar, dVar2);
                }
                return new b().b(dVar, dVar2);
            }
        }
        return dVar2;
    }

    private static p D(qc.d dVar, qc.d dVar2) {
        int size = dVar.size();
        int size2 = dVar2.size();
        byte[] bArr = new byte[size + size2];
        dVar.i(bArr, 0, 0, size);
        dVar2.i(bArr, 0, size, size2);
        return new p(bArr);
    }

    private boolean E(qc.d dVar) {
        c cVar = new c(this);
        p next = cVar.next();
        c cVar2 = new c(dVar);
        p next2 = cVar2.next();
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        while (true) {
            int size = next.size() - i10;
            int size2 = next2.size() - i11;
            int min = Math.min(size, size2);
            if (!(i10 == 0 ? next.A(next2, i11, min) : next2.A(next, i10, min))) {
                return false;
            }
            i12 += min;
            int i13 = this.f17333f;
            if (i12 >= i13) {
                if (i12 == i13) {
                    return true;
                }
                throw new IllegalStateException();
            }
            if (min == size) {
                next = cVar.next();
                i10 = 0;
            } else {
                i10 += min;
            }
            if (min == size2) {
                next2 = cVar2.next();
                i11 = 0;
            } else {
                i11 += min;
            }
        }
    }

    public boolean equals(Object obj) {
        int t7;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof qc.d)) {
            return false;
        }
        qc.d dVar = (qc.d) obj;
        if (this.f17333f != dVar.size()) {
            return false;
        }
        if (this.f17333f == 0) {
            return true;
        }
        if (this.f17338k == 0 || (t7 = dVar.t()) == 0 || this.f17338k == t7) {
            return E(dVar);
        }
        return false;
    }

    public int hashCode() {
        int i10 = this.f17338k;
        if (i10 == 0) {
            int i11 = this.f17333f;
            i10 = r(i11, 0, i11);
            if (i10 == 0) {
                i10 = 1;
            }
            this.f17338k = i10;
        }
        return i10;
    }

    @Override // qc.d
    protected void k(byte[] bArr, int i10, int i11, int i12) {
        int i13 = i10 + i12;
        int i14 = this.f17336i;
        if (i13 <= i14) {
            this.f17334g.k(bArr, i10, i11, i12);
        } else {
            if (i10 >= i14) {
                this.f17335h.k(bArr, i10 - i14, i11, i12);
                return;
            }
            int i15 = i14 - i10;
            this.f17334g.k(bArr, i10, i11, i15);
            this.f17335h.k(bArr, 0, i11 + i15, i12 - i15);
        }
    }

    @Override // qc.d
    protected int l() {
        return this.f17337j;
    }

    @Override // qc.d
    protected boolean m() {
        return this.f17333f >= f17332l[this.f17337j];
    }

    @Override // qc.d
    public boolean n() {
        int s7 = this.f17334g.s(0, 0, this.f17336i);
        qc.d dVar = this.f17335h;
        return dVar.s(s7, 0, dVar.size()) == 0;
    }

    @Override // qc.d, java.lang.Iterable
    /* renamed from: o */
    public d.a iterator() {
        return new d();
    }

    @Override // qc.d
    public qc.e p() {
        return qc.e.g(new e());
    }

    @Override // qc.d
    protected int r(int i10, int i11, int i12) {
        int i13 = i11 + i12;
        int i14 = this.f17336i;
        if (i13 <= i14) {
            return this.f17334g.r(i10, i11, i12);
        }
        if (i11 >= i14) {
            return this.f17335h.r(i10, i11 - i14, i12);
        }
        int i15 = i14 - i11;
        return this.f17335h.r(this.f17334g.r(i10, i11, i15), 0, i12 - i15);
    }

    @Override // qc.d
    protected int s(int i10, int i11, int i12) {
        int i13 = i11 + i12;
        int i14 = this.f17336i;
        if (i13 <= i14) {
            return this.f17334g.s(i10, i11, i12);
        }
        if (i11 >= i14) {
            return this.f17335h.s(i10, i11 - i14, i12);
        }
        int i15 = i14 - i11;
        return this.f17335h.s(this.f17334g.s(i10, i11, i15), 0, i12 - i15);
    }

    @Override // qc.d
    public int size() {
        return this.f17333f;
    }

    @Override // qc.d
    protected int t() {
        return this.f17338k;
    }

    @Override // qc.d
    public String v(String str) {
        return new String(u(), str);
    }

    @Override // qc.d
    void y(OutputStream outputStream, int i10, int i11) {
        int i12 = i10 + i11;
        int i13 = this.f17336i;
        if (i12 <= i13) {
            this.f17334g.y(outputStream, i10, i11);
        } else {
            if (i10 >= i13) {
                this.f17335h.y(outputStream, i10 - i13, i11);
                return;
            }
            int i14 = i13 - i10;
            this.f17334g.y(outputStream, i10, i14);
            this.f17335h.y(outputStream, 0, i11 - i14);
        }
    }

    private u(qc.d dVar, qc.d dVar2) {
        this.f17338k = 0;
        this.f17334g = dVar;
        this.f17335h = dVar2;
        int size = dVar.size();
        this.f17336i = size;
        this.f17333f = size + dVar2.size();
        this.f17337j = Math.max(dVar.l(), dVar2.l()) + 1;
    }

    /* compiled from: RopeByteString.java */
    /* loaded from: classes2.dex */
    private class e extends InputStream {

        /* renamed from: e, reason: collision with root package name */
        private c f17346e;

        /* renamed from: f, reason: collision with root package name */
        private p f17347f;

        /* renamed from: g, reason: collision with root package name */
        private int f17348g;

        /* renamed from: h, reason: collision with root package name */
        private int f17349h;

        /* renamed from: i, reason: collision with root package name */
        private int f17350i;

        /* renamed from: j, reason: collision with root package name */
        private int f17351j;

        public e() {
            c();
        }

        private void b() {
            if (this.f17347f != null) {
                int i10 = this.f17349h;
                int i11 = this.f17348g;
                if (i10 == i11) {
                    this.f17350i += i11;
                    this.f17349h = 0;
                    if (this.f17346e.hasNext()) {
                        p next = this.f17346e.next();
                        this.f17347f = next;
                        this.f17348g = next.size();
                    } else {
                        this.f17347f = null;
                        this.f17348g = 0;
                    }
                }
            }
        }

        private void c() {
            c cVar = new c(u.this);
            this.f17346e = cVar;
            p next = cVar.next();
            this.f17347f = next;
            this.f17348g = next.size();
            this.f17349h = 0;
            this.f17350i = 0;
        }

        private int m(byte[] bArr, int i10, int i11) {
            int i12 = i11;
            while (true) {
                if (i12 <= 0) {
                    break;
                }
                b();
                if (this.f17347f != null) {
                    int min = Math.min(this.f17348g - this.f17349h, i12);
                    if (bArr != null) {
                        this.f17347f.i(bArr, this.f17349h, i10, min);
                        i10 += min;
                    }
                    this.f17349h += min;
                    i12 -= min;
                } else if (i12 == i11) {
                    return -1;
                }
            }
            return i11 - i12;
        }

        @Override // java.io.InputStream
        public int available() {
            return u.this.size() - (this.f17350i + this.f17349h);
        }

        @Override // java.io.InputStream
        public void mark(int i10) {
            this.f17351j = this.f17350i + this.f17349h;
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i10, int i11) {
            Objects.requireNonNull(bArr);
            if (i10 >= 0 && i11 >= 0 && i11 <= bArr.length - i10) {
                return m(bArr, i10, i11);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.io.InputStream
        public synchronized void reset() {
            c();
            m(null, 0, this.f17351j);
        }

        @Override // java.io.InputStream
        public long skip(long j10) {
            if (j10 >= 0) {
                if (j10 > 2147483647L) {
                    j10 = 2147483647L;
                }
                return m(null, 0, (int) j10);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.io.InputStream
        public int read() {
            b();
            p pVar = this.f17347f;
            if (pVar == null) {
                return -1;
            }
            int i10 = this.f17349h;
            this.f17349h = i10 + 1;
            return pVar.z(i10) & 255;
        }
    }
}
