package me;

import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import kotlin.collections._ArraysJvm;
import sd.StringsJVM;
import za.DefaultConstructorMarker;

/* compiled from: ByteString.kt */
/* loaded from: classes2.dex */
public class g implements Serializable, Comparable<g> {

    /* renamed from: h, reason: collision with root package name */
    public static final a f15493h = new a(null);

    /* renamed from: i, reason: collision with root package name */
    public static final g f15494i = new g(new byte[0]);
    private static final long serialVersionUID = 1;

    /* renamed from: e, reason: collision with root package name */
    private final byte[] f15495e;

    /* renamed from: f, reason: collision with root package name */
    private transient int f15496f;

    /* renamed from: g, reason: collision with root package name */
    private transient String f15497g;

    /* compiled from: ByteString.kt */
    /* loaded from: classes2.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ g e(a aVar, byte[] bArr, int i10, int i11, int i12, Object obj) {
            if ((i12 & 1) != 0) {
                i10 = 0;
            }
            if ((i12 & 2) != 0) {
                i11 = b.c();
            }
            return aVar.d(bArr, i10, i11);
        }

        public final g a(String str) {
            za.k.e(str, "<this>");
            if (str.length() % 2 == 0) {
                int length = str.length() / 2;
                byte[] bArr = new byte[length];
                for (int i10 = 0; i10 < length; i10++) {
                    int i11 = i10 * 2;
                    bArr[i10] = (byte) ((ne.b.b(str.charAt(i11)) << 4) + ne.b.b(str.charAt(i11 + 1)));
                }
                return new g(bArr);
            }
            throw new IllegalArgumentException(("Unexpected hex string: " + str).toString());
        }

        public final g b(String str, Charset charset) {
            za.k.e(str, "<this>");
            za.k.e(charset, "charset");
            byte[] bytes = str.getBytes(charset);
            za.k.d(bytes, "this as java.lang.String).getBytes(charset)");
            return new g(bytes);
        }

        public final g c(String str) {
            za.k.e(str, "<this>");
            g gVar = new g(d0.a(str));
            gVar.q(str);
            return gVar;
        }

        public final g d(byte[] bArr, int i10, int i11) {
            byte[] j10;
            za.k.e(bArr, "<this>");
            int e10 = b.e(bArr, i11);
            b.b(bArr.length, i10, e10);
            j10 = _ArraysJvm.j(bArr, i10, e10 + i10);
            return new g(j10);
        }

        public final g f(InputStream inputStream, int i10) {
            za.k.e(inputStream, "<this>");
            int i11 = 0;
            if (i10 >= 0) {
                byte[] bArr = new byte[i10];
                while (i11 < i10) {
                    int read = inputStream.read(bArr, i11, i10 - i11);
                    if (read == -1) {
                        throw new EOFException();
                    }
                    i11 += read;
                }
                return new g(bArr);
            }
            throw new IllegalArgumentException(("byteCount < 0: " + i10).toString());
        }
    }

    public g(byte[] bArr) {
        za.k.e(bArr, "data");
        this.f15495e = bArr;
    }

    public static final g d(String str) {
        return f15493h.c(str);
    }

    private final void readObject(ObjectInputStream objectInputStream) {
        g f10 = f15493h.f(objectInputStream, objectInputStream.readInt());
        Field declaredField = g.class.getDeclaredField("e");
        declaredField.setAccessible(true);
        declaredField.set(this, f10.f15495e);
    }

    private final void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeInt(this.f15495e.length);
        objectOutputStream.write(this.f15495e);
    }

    public String a() {
        return Base64.b(f(), null, 1, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0033, code lost:
    
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:?, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x002e, code lost:
    
        if (r0 < r1) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0028, code lost:
    
        if (r7 < r8) goto L13;
     */
    @Override // java.lang.Comparable
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int compareTo(g gVar) {
        za.k.e(gVar, "other");
        int t7 = t();
        int t10 = gVar.t();
        int min = Math.min(t7, t10);
        for (int i10 = 0; i10 < min; i10++) {
            int e10 = e(i10) & 255;
            int e11 = gVar.e(i10) & 255;
            if (e10 == e11) {
            }
        }
        if (t7 == t10) {
            return 0;
        }
    }

    public g c(String str) {
        za.k.e(str, "algorithm");
        MessageDigest messageDigest = MessageDigest.getInstance(str);
        messageDigest.update(this.f15495e, 0, t());
        byte[] digest = messageDigest.digest();
        za.k.d(digest, "digestBytes");
        return new g(digest);
    }

    public final byte e(int i10) {
        return m(i10);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof g) {
            g gVar = (g) obj;
            if (gVar.t() == f().length && gVar.o(0, f(), 0, f().length)) {
                return true;
            }
        }
        return false;
    }

    public final byte[] f() {
        return this.f15495e;
    }

    public final int g() {
        return this.f15496f;
    }

    public int h() {
        return f().length;
    }

    public int hashCode() {
        int g6 = g();
        if (g6 != 0) {
            return g6;
        }
        int hashCode = Arrays.hashCode(f());
        p(hashCode);
        return hashCode;
    }

    public final String i() {
        return this.f15497g;
    }

    public String j() {
        String n10;
        char[] cArr = new char[f().length * 2];
        int i10 = 0;
        for (byte b10 : f()) {
            int i11 = i10 + 1;
            cArr[i10] = ne.b.f()[(b10 >> 4) & 15];
            i10 = i11 + 1;
            cArr[i11] = ne.b.f()[b10 & 15];
        }
        n10 = StringsJVM.n(cArr);
        return n10;
    }

    public byte[] l() {
        return f();
    }

    public byte m(int i10) {
        return f()[i10];
    }

    public boolean n(int i10, g gVar, int i11, int i12) {
        za.k.e(gVar, "other");
        return gVar.o(i11, f(), i10, i12);
    }

    public boolean o(int i10, byte[] bArr, int i11, int i12) {
        za.k.e(bArr, "other");
        return i10 >= 0 && i10 <= f().length - i12 && i11 >= 0 && i11 <= bArr.length - i12 && b.a(f(), i10, bArr, i11, i12);
    }

    public final void p(int i10) {
        this.f15496f = i10;
    }

    public final void q(String str) {
        this.f15497g = str;
    }

    public final g r() {
        return c("SHA-1");
    }

    public final g s() {
        return c("SHA-256");
    }

    public final int t() {
        return h();
    }

    public String toString() {
        String z10;
        String z11;
        String z12;
        g gVar;
        byte[] j10;
        if (f().length == 0) {
            return "[size=0]";
        }
        int a10 = ne.b.a(f(), 64);
        if (a10 == -1) {
            if (f().length <= 64) {
                return "[hex=" + j() + ']';
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[size=");
            sb2.append(f().length);
            sb2.append(" hex=");
            int d10 = b.d(this, 64);
            if (!(d10 <= f().length)) {
                throw new IllegalArgumentException(("endIndex > length(" + f().length + ')').toString());
            }
            if (d10 + 0 >= 0) {
                if (d10 == f().length) {
                    gVar = this;
                } else {
                    j10 = _ArraysJvm.j(f(), 0, d10);
                    gVar = new g(j10);
                }
                sb2.append(gVar.j());
                sb2.append("…]");
                return sb2.toString();
            }
            throw new IllegalArgumentException("endIndex < beginIndex".toString());
        }
        String w10 = w();
        String substring = w10.substring(0, a10);
        za.k.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        z10 = StringsJVM.z(substring, "\\", "\\\\", false, 4, null);
        z11 = StringsJVM.z(z10, "\n", "\\n", false, 4, null);
        z12 = StringsJVM.z(z11, "\r", "\\r", false, 4, null);
        if (a10 < w10.length()) {
            return "[size=" + f().length + " text=" + z12 + "…]";
        }
        return "[text=" + z12 + ']';
    }

    public final boolean u(g gVar) {
        za.k.e(gVar, "prefix");
        return n(0, gVar, 0, gVar.t());
    }

    public g v() {
        for (int i10 = 0; i10 < f().length; i10++) {
            byte b10 = f()[i10];
            if (b10 >= 65 && b10 <= 90) {
                byte[] f10 = f();
                byte[] copyOf = Arrays.copyOf(f10, f10.length);
                za.k.d(copyOf, "copyOf(this, size)");
                copyOf[i10] = (byte) (b10 + 32);
                for (int i11 = i10 + 1; i11 < copyOf.length; i11++) {
                    byte b11 = copyOf[i11];
                    if (b11 >= 65 && b11 <= 90) {
                        copyOf[i11] = (byte) (b11 + 32);
                    }
                }
                return new g(copyOf);
            }
        }
        return this;
    }

    public String w() {
        String i10 = i();
        if (i10 != null) {
            return i10;
        }
        String b10 = d0.b(l());
        q(b10);
        return b10;
    }

    public void x(d dVar, int i10, int i11) {
        za.k.e(dVar, "buffer");
        ne.b.d(this, dVar, i10, i11);
    }
}
