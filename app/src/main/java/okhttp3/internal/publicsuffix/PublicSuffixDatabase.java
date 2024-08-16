package okhttp3.internal.publicsuffix;

import ae.d;
import ie.Platform;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.collections.CollectionsJVM;
import kotlin.collections._Collections;
import kotlin.collections.r;
import ma.Unit;
import me.BufferedSource;
import me.GzipSource;
import me.n;
import rd.Sequence;
import rd._Sequences;
import sd.v;
import wa.Closeable;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: PublicSuffixDatabase.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0006\u0018\u0000 \t2\u00020\u0001:\u0001\rB\u0007¢\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u001c\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0004H\u0002J\b\u0010\t\u001a\u00020\bH\u0002J\b\u0010\n\u001a\u00020\bH\u0002J\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u000f\u001a\u00020\f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\r\u0010\u000eR\u0014\u0010\u0012\u001a\u00020\u00108\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010\u0011R\u0016\u0010\u0015\u001a\u00020\u00138\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b\u000b\u0010\u0014R\u0016\u0010\u0016\u001a\u00020\u00138\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b\n\u0010\u0014¨\u0006\u0019"}, d2 = {"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "", "", "domain", "", "f", "domainLabels", "b", "Lma/f0;", "e", "d", "c", "Ljava/util/concurrent/atomic/AtomicBoolean;", "a", "Ljava/util/concurrent/atomic/AtomicBoolean;", "listRead", "Ljava/util/concurrent/CountDownLatch;", "Ljava/util/concurrent/CountDownLatch;", "readCompleteLatch", "", "[B", "publicSuffixListBytes", "publicSuffixExceptionListBytes", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class PublicSuffixDatabase {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: f, reason: collision with root package name */
    private static final byte[] f16523f = {42};

    /* renamed from: g, reason: collision with root package name */
    private static final List<String> f16524g;

    /* renamed from: h, reason: collision with root package name */
    private static final PublicSuffixDatabase f16525h;

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final AtomicBoolean listRead = new AtomicBoolean(false);

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private byte[] publicSuffixListBytes;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private byte[] publicSuffixExceptionListBytes;

    /* compiled from: PublicSuffixDatabase.kt */
    @Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0018\u0010\u0019J+\u0010\b\u001a\u0004\u0018\u00010\u0007*\u00020\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0002¢\u0006\u0004\b\b\u0010\tJ\u0006\u0010\u000b\u001a\u00020\nR\u0014\u0010\r\u001a\u00020\f8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u000eR\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00078\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\n8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0016\u0010\u0017¨\u0006\u001a"}, d2 = {"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase$a;", "", "", "", "labels", "", "labelIndex", "", "b", "([B[[BI)Ljava/lang/String;", "Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "c", "", "EXCEPTION_MARKER", "C", "", "PREVAILING_RULE", "Ljava/util/List;", "PUBLIC_SUFFIX_RESOURCE", "Ljava/lang/String;", "WILDCARD_LABEL", "[B", "instance", "Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: okhttp3.internal.publicsuffix.PublicSuffixDatabase$a, reason: from kotlin metadata */
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String b(byte[] bArr, byte[][] bArr2, int i10) {
            int i11;
            boolean z10;
            int d10;
            int d11;
            int length = bArr.length;
            int i12 = 0;
            while (i12 < length) {
                int i13 = (i12 + length) / 2;
                while (i13 > -1 && bArr[i13] != 10) {
                    i13--;
                }
                int i14 = i13 + 1;
                int i15 = 1;
                while (true) {
                    i11 = i14 + i15;
                    if (bArr[i11] == 10) {
                        break;
                    }
                    i15++;
                }
                int i16 = i11 - i14;
                int i17 = i10;
                boolean z11 = false;
                int i18 = 0;
                int i19 = 0;
                while (true) {
                    if (z11) {
                        d10 = 46;
                        z10 = false;
                    } else {
                        z10 = z11;
                        d10 = d.d(bArr2[i17][i18], 255);
                    }
                    d11 = d10 - d.d(bArr[i14 + i19], 255);
                    if (d11 != 0) {
                        break;
                    }
                    i19++;
                    i18++;
                    if (i19 == i16) {
                        break;
                    }
                    if (bArr2[i17].length != i18) {
                        z11 = z10;
                    } else {
                        if (i17 == bArr2.length - 1) {
                            break;
                        }
                        i17++;
                        i18 = -1;
                        z11 = true;
                    }
                }
                if (d11 >= 0) {
                    if (d11 <= 0) {
                        int i20 = i16 - i19;
                        int length2 = bArr2[i17].length - i18;
                        int length3 = bArr2.length;
                        for (int i21 = i17 + 1; i21 < length3; i21++) {
                            length2 += bArr2[i21].length;
                        }
                        if (length2 >= i20) {
                            if (length2 <= i20) {
                                Charset charset = StandardCharsets.UTF_8;
                                k.d(charset, "UTF_8");
                                return new String(bArr, i14, i16, charset);
                            }
                        }
                    }
                    i12 = i11 + 1;
                }
                length = i14 - 1;
            }
            return null;
        }

        public final PublicSuffixDatabase c() {
            return PublicSuffixDatabase.f16525h;
        }
    }

    static {
        List<String> e10;
        e10 = CollectionsJVM.e("*");
        f16524g = e10;
        f16525h = new PublicSuffixDatabase();
    }

    private final List<String> b(List<String> domainLabels) {
        String str;
        String str2;
        String str3;
        List<String> p02;
        if (!this.listRead.get() && this.listRead.compareAndSet(false, true)) {
            e();
        } else {
            try {
                this.readCompleteLatch.await();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }
        if (this.publicSuffixListBytes != null) {
            int size = domainLabels.size();
            byte[][] bArr = new byte[size];
            for (int i10 = 0; i10 < size; i10++) {
                String str4 = domainLabels.get(i10);
                Charset charset = StandardCharsets.UTF_8;
                k.d(charset, "UTF_8");
                byte[] bytes = str4.getBytes(charset);
                k.d(bytes, "this as java.lang.String).getBytes(charset)");
                bArr[i10] = bytes;
            }
            int i11 = 0;
            while (true) {
                if (i11 >= size) {
                    str = null;
                    break;
                }
                int i12 = i11 + 1;
                Companion companion = INSTANCE;
                byte[] bArr2 = this.publicSuffixListBytes;
                if (bArr2 == null) {
                    k.s("publicSuffixListBytes");
                    bArr2 = null;
                }
                String b10 = companion.b(bArr2, bArr, i11);
                if (b10 != null) {
                    str = b10;
                    break;
                }
                i11 = i12;
            }
            if (size > 1) {
                byte[][] bArr3 = (byte[][]) bArr.clone();
                int length = bArr3.length - 1;
                int i13 = 0;
                while (i13 < length) {
                    int i14 = i13 + 1;
                    bArr3[i13] = f16523f;
                    Companion companion2 = INSTANCE;
                    byte[] bArr4 = this.publicSuffixListBytes;
                    if (bArr4 == null) {
                        k.s("publicSuffixListBytes");
                        bArr4 = null;
                    }
                    String b11 = companion2.b(bArr4, bArr3, i13);
                    if (b11 != null) {
                        str2 = b11;
                        break;
                    }
                    i13 = i14;
                }
            }
            str2 = null;
            if (str2 != null) {
                int i15 = size - 1;
                int i16 = 0;
                while (i16 < i15) {
                    int i17 = i16 + 1;
                    Companion companion3 = INSTANCE;
                    byte[] bArr5 = this.publicSuffixExceptionListBytes;
                    if (bArr5 == null) {
                        k.s("publicSuffixExceptionListBytes");
                        bArr5 = null;
                    }
                    str3 = companion3.b(bArr5, bArr, i16);
                    if (str3 != null) {
                        break;
                    }
                    i16 = i17;
                }
            }
            str3 = null;
            if (str3 != null) {
                p02 = v.p0(k.l("!", str3), new char[]{'.'}, false, 0, 6, null);
                return p02;
            }
            if (str == null && str2 == null) {
                return f16524g;
            }
            List<String> p03 = str == null ? null : v.p0(str, new char[]{'.'}, false, 0, 6, null);
            if (p03 == null) {
                p03 = r.j();
            }
            List<String> p04 = str2 != null ? v.p0(str2, new char[]{'.'}, false, 0, 6, null) : null;
            if (p04 == null) {
                p04 = r.j();
            }
            return p03.size() > p04.size() ? p03 : p04;
        }
        throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.".toString());
    }

    private final void d() {
        InputStream resourceAsStream = PublicSuffixDatabase.class.getResourceAsStream("publicsuffixes.gz");
        if (resourceAsStream == null) {
            return;
        }
        BufferedSource b10 = n.b(new GzipSource(n.e(resourceAsStream)));
        try {
            byte[] b02 = b10.b0(b10.o());
            byte[] b03 = b10.b0(b10.o());
            Unit unit = Unit.f15173a;
            Closeable.a(b10, null);
            synchronized (this) {
                k.b(b02);
                this.publicSuffixListBytes = b02;
                k.b(b03);
                this.publicSuffixExceptionListBytes = b03;
            }
            this.readCompleteLatch.countDown();
        } finally {
        }
    }

    private final void e() {
        boolean z10 = false;
        while (true) {
            try {
                try {
                    d();
                    break;
                } catch (InterruptedIOException unused) {
                    Thread.interrupted();
                    z10 = true;
                } catch (IOException e10) {
                    Platform.f12870a.g().j("Failed to read public suffix list", 5, e10);
                    if (z10) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    return;
                }
            } catch (Throwable th) {
                if (z10) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z10) {
            Thread.currentThread().interrupt();
        }
    }

    private final List<String> f(String domain) {
        List<String> p02;
        Object e02;
        List<String> O;
        p02 = v.p0(domain, new char[]{'.'}, false, 0, 6, null);
        e02 = _Collections.e0(p02);
        if (!k.a(e02, "")) {
            return p02;
        }
        O = _Collections.O(p02, 1);
        return O;
    }

    public final String c(String domain) {
        int size;
        int size2;
        Sequence K;
        Sequence l10;
        String u7;
        k.e(domain, "domain");
        String unicode = IDN.toUnicode(domain);
        k.d(unicode, "unicodeDomain");
        List<String> f10 = f(unicode);
        List<String> b10 = b(f10);
        if (f10.size() == b10.size() && b10.get(0).charAt(0) != '!') {
            return null;
        }
        if (b10.get(0).charAt(0) == '!') {
            size = f10.size();
            size2 = b10.size();
        } else {
            size = f10.size();
            size2 = b10.size() + 1;
        }
        K = _Collections.K(f(domain));
        l10 = _Sequences.l(K, size - size2);
        u7 = _Sequences.u(l10, ".", null, null, 0, null, null, 62, null);
        return u7;
    }
}
