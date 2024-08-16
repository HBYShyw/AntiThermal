package zd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLSocket;
import kotlin.Metadata;
import kotlin.collections._Collections;
import za.DefaultConstructorMarker;

/* compiled from: ConnectionSpec.kt */
@Metadata(bv = {}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0002%&B9\b\u0000\u0012\u0006\u0010\u0012\u001a\u00020\u0004\u0012\u0006\u0010\u0016\u001a\u00020\u0004\u0012\u000e\u0010!\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010 \u0012\u000e\u0010\"\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010 ¢\u0006\u0004\b#\u0010$J\u0018\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u001f\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0000¢\u0006\u0004\b\b\u0010\tJ\u000e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0002J\u0013\u0010\r\u001a\u00020\u00042\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0016R\u0017\u0010\u0012\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\u00020\u00048\u0007¢\u0006\f\n\u0004\b\u0016\u0010\u0013\u001a\u0004\b\u0017\u0010\u0015R\u0019\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u00188G¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0019\u0010\u001f\u001a\n\u0012\u0004\u0012\u00020\u001d\u0018\u00010\u00188G¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001b¨\u0006'"}, d2 = {"Lzd/l;", "", "Ljavax/net/ssl/SSLSocket;", "sslSocket", "", "isFallback", "g", "Lma/f0;", "c", "(Ljavax/net/ssl/SSLSocket;Z)V", "socket", "e", "other", "equals", "", "hashCode", "", "toString", "isTls", "Z", "f", "()Z", "supportsTlsExtensions", "h", "", "Lzd/i;", "d", "()Ljava/util/List;", "cipherSuites", "Lzd/e0;", "i", "tlsVersions", "", "cipherSuitesAsString", "tlsVersionsAsString", "<init>", "(ZZ[Ljava/lang/String;[Ljava/lang/String;)V", "a", "b", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.l, reason: use source file name */
/* loaded from: classes2.dex */
public final class ConnectionSpec {

    /* renamed from: e, reason: collision with root package name */
    public static final b f20656e = new b(null);

    /* renamed from: f, reason: collision with root package name */
    private static final CipherSuite[] f20657f;

    /* renamed from: g, reason: collision with root package name */
    private static final CipherSuite[] f20658g;

    /* renamed from: h, reason: collision with root package name */
    public static final ConnectionSpec f20659h;

    /* renamed from: i, reason: collision with root package name */
    public static final ConnectionSpec f20660i;

    /* renamed from: j, reason: collision with root package name */
    public static final ConnectionSpec f20661j;

    /* renamed from: k, reason: collision with root package name */
    public static final ConnectionSpec f20662k;

    /* renamed from: a, reason: collision with root package name */
    private final boolean f20663a;

    /* renamed from: b, reason: collision with root package name */
    private final boolean f20664b;

    /* renamed from: c, reason: collision with root package name */
    private final String[] f20665c;

    /* renamed from: d, reason: collision with root package name */
    private final String[] f20666d;

    /* compiled from: ConnectionSpec.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eR\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0007\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\t\u0010\bR\u0014\u0010\n\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\n\u0010\bR\u001a\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\u0005R\u0014\u0010\f\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\f\u0010\b¨\u0006\u000f"}, d2 = {"Lzd/l$b;", "", "", "Lzd/i;", "APPROVED_CIPHER_SUITES", "[Lzd/i;", "Lzd/l;", "CLEARTEXT", "Lzd/l;", "COMPATIBLE_TLS", "MODERN_TLS", "RESTRICTED_CIPHER_SUITES", "RESTRICTED_TLS", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.l$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        CipherSuite cipherSuite = CipherSuite.f20627o1;
        CipherSuite cipherSuite2 = CipherSuite.f20630p1;
        CipherSuite cipherSuite3 = CipherSuite.f20633q1;
        CipherSuite cipherSuite4 = CipherSuite.f20585a1;
        CipherSuite cipherSuite5 = CipherSuite.f20597e1;
        CipherSuite cipherSuite6 = CipherSuite.f20588b1;
        CipherSuite cipherSuite7 = CipherSuite.f20600f1;
        CipherSuite cipherSuite8 = CipherSuite.f20618l1;
        CipherSuite cipherSuite9 = CipherSuite.f20615k1;
        CipherSuite[] cipherSuiteArr = {cipherSuite, cipherSuite2, cipherSuite3, cipherSuite4, cipherSuite5, cipherSuite6, cipherSuite7, cipherSuite8, cipherSuite9};
        f20657f = cipherSuiteArr;
        CipherSuite[] cipherSuiteArr2 = {cipherSuite, cipherSuite2, cipherSuite3, cipherSuite4, cipherSuite5, cipherSuite6, cipherSuite7, cipherSuite8, cipherSuite9, CipherSuite.L0, CipherSuite.M0, CipherSuite.f20611j0, CipherSuite.f20614k0, CipherSuite.H, CipherSuite.L, CipherSuite.f20616l};
        f20658g = cipherSuiteArr2;
        a c10 = new a(true).c((CipherSuite[]) Arrays.copyOf(cipherSuiteArr, cipherSuiteArr.length));
        TlsVersion tlsVersion = TlsVersion.TLS_1_3;
        TlsVersion tlsVersion2 = TlsVersion.TLS_1_2;
        f20659h = c10.j(tlsVersion, tlsVersion2).h(true).a();
        f20660i = new a(true).c((CipherSuite[]) Arrays.copyOf(cipherSuiteArr2, cipherSuiteArr2.length)).j(tlsVersion, tlsVersion2).h(true).a();
        f20661j = new a(true).c((CipherSuite[]) Arrays.copyOf(cipherSuiteArr2, cipherSuiteArr2.length)).j(tlsVersion, tlsVersion2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).h(true).a();
        f20662k = new a(false).a();
    }

    public ConnectionSpec(boolean z10, boolean z11, String[] strArr, String[] strArr2) {
        this.f20663a = z10;
        this.f20664b = z11;
        this.f20665c = strArr;
        this.f20666d = strArr2;
    }

    private final ConnectionSpec g(SSLSocket sslSocket, boolean isFallback) {
        String[] enabledCipherSuites;
        String[] enabledProtocols;
        Comparator b10;
        if (this.f20665c != null) {
            String[] enabledCipherSuites2 = sslSocket.getEnabledCipherSuites();
            za.k.d(enabledCipherSuites2, "sslSocket.enabledCipherSuites");
            enabledCipherSuites = ae.d.D(enabledCipherSuites2, this.f20665c, CipherSuite.f20586b.c());
        } else {
            enabledCipherSuites = sslSocket.getEnabledCipherSuites();
        }
        if (this.f20666d != null) {
            String[] enabledProtocols2 = sslSocket.getEnabledProtocols();
            za.k.d(enabledProtocols2, "sslSocket.enabledProtocols");
            String[] strArr = this.f20666d;
            b10 = pa.b.b();
            enabledProtocols = ae.d.D(enabledProtocols2, strArr, b10);
        } else {
            enabledProtocols = sslSocket.getEnabledProtocols();
        }
        String[] supportedCipherSuites = sslSocket.getSupportedCipherSuites();
        za.k.d(supportedCipherSuites, "supportedCipherSuites");
        int w10 = ae.d.w(supportedCipherSuites, "TLS_FALLBACK_SCSV", CipherSuite.f20586b.c());
        if (isFallback && w10 != -1) {
            za.k.d(enabledCipherSuites, "cipherSuitesIntersection");
            String str = supportedCipherSuites[w10];
            za.k.d(str, "supportedCipherSuites[indexOfFallbackScsv]");
            enabledCipherSuites = ae.d.n(enabledCipherSuites, str);
        }
        a aVar = new a(this);
        za.k.d(enabledCipherSuites, "cipherSuitesIntersection");
        a b11 = aVar.b((String[]) Arrays.copyOf(enabledCipherSuites, enabledCipherSuites.length));
        za.k.d(enabledProtocols, "tlsVersionsIntersection");
        return b11.i((String[]) Arrays.copyOf(enabledProtocols, enabledProtocols.length)).a();
    }

    public final void c(SSLSocket sslSocket, boolean isFallback) {
        za.k.e(sslSocket, "sslSocket");
        ConnectionSpec g6 = g(sslSocket, isFallback);
        if (g6.i() != null) {
            sslSocket.setEnabledProtocols(g6.f20666d);
        }
        if (g6.d() != null) {
            sslSocket.setEnabledCipherSuites(g6.f20665c);
        }
    }

    public final List<CipherSuite> d() {
        List<CipherSuite> z02;
        String[] strArr = this.f20665c;
        if (strArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(CipherSuite.f20586b.b(str));
        }
        z02 = _Collections.z0(arrayList);
        return z02;
    }

    public final boolean e(SSLSocket socket) {
        Comparator b10;
        za.k.e(socket, "socket");
        if (!this.f20663a) {
            return false;
        }
        String[] strArr = this.f20666d;
        if (strArr != null) {
            String[] enabledProtocols = socket.getEnabledProtocols();
            b10 = pa.b.b();
            if (!ae.d.t(strArr, enabledProtocols, b10)) {
                return false;
            }
        }
        String[] strArr2 = this.f20665c;
        return strArr2 == null || ae.d.t(strArr2, socket.getEnabledCipherSuites(), CipherSuite.f20586b.c());
    }

    public boolean equals(Object other) {
        if (!(other instanceof ConnectionSpec)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        boolean z10 = this.f20663a;
        ConnectionSpec connectionSpec = (ConnectionSpec) other;
        if (z10 != connectionSpec.f20663a) {
            return false;
        }
        return !z10 || (Arrays.equals(this.f20665c, connectionSpec.f20665c) && Arrays.equals(this.f20666d, connectionSpec.f20666d) && this.f20664b == connectionSpec.f20664b);
    }

    /* renamed from: f, reason: from getter */
    public final boolean getF20663a() {
        return this.f20663a;
    }

    /* renamed from: h, reason: from getter */
    public final boolean getF20664b() {
        return this.f20664b;
    }

    public int hashCode() {
        if (!this.f20663a) {
            return 17;
        }
        String[] strArr = this.f20665c;
        int hashCode = (527 + (strArr == null ? 0 : Arrays.hashCode(strArr))) * 31;
        String[] strArr2 = this.f20666d;
        return ((hashCode + (strArr2 != null ? Arrays.hashCode(strArr2) : 0)) * 31) + (!this.f20664b ? 1 : 0);
    }

    public final List<TlsVersion> i() {
        List<TlsVersion> z02;
        String[] strArr = this.f20666d;
        if (strArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            arrayList.add(TlsVersion.f20563f.a(str));
        }
        z02 = _Collections.z0(arrayList);
        return z02;
    }

    public String toString() {
        if (!this.f20663a) {
            return "ConnectionSpec()";
        }
        return "ConnectionSpec(cipherSuites=" + ((Object) Objects.toString(d(), "[all enabled]")) + ", tlsVersions=" + ((Object) Objects.toString(i(), "[all enabled]")) + ", supportsTlsExtensions=" + this.f20664b + ')';
    }

    /* compiled from: ConnectionSpec.kt */
    @Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u00002\u00020\u0001B\u0011\b\u0010\u0012\u0006\u0010\u0014\u001a\u00020\u000f¢\u0006\u0004\b#\u0010\u0019B\u0011\b\u0016\u0012\u0006\u0010$\u001a\u00020\u0012¢\u0006\u0004\b#\u0010%J!\u0010\u0005\u001a\u00020\u00002\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00030\u0002\"\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J!\u0010\b\u001a\u00020\u00002\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00070\u0002\"\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ!\u0010\f\u001a\u00020\u00002\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0\u0002\"\u00020\n¢\u0006\u0004\b\f\u0010\rJ!\u0010\u000e\u001a\u00020\u00002\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00070\u0002\"\u00020\u0007¢\u0006\u0004\b\u000e\u0010\tJ\u0010\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u000fH\u0007J\u0006\u0010\u0013\u001a\u00020\u0012R\"\u0010\u0014\u001a\u00020\u000f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R*\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0004\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR*\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u000b\u0010\u001a\u001a\u0004\b\u001f\u0010\u001c\"\u0004\b \u0010\u001eR\"\u0010\u0010\u001a\u00020\u000f8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u0010\u0010\u0015\u001a\u0004\b!\u0010\u0017\"\u0004\b\"\u0010\u0019¨\u0006&"}, d2 = {"Lzd/l$a;", "", "", "Lzd/i;", "cipherSuites", "c", "([Lzd/i;)Lzd/l$a;", "", "b", "([Ljava/lang/String;)Lzd/l$a;", "Lzd/e0;", "tlsVersions", "j", "([Lzd/e0;)Lzd/l$a;", "i", "", "supportsTlsExtensions", "h", "Lzd/l;", "a", "tls", "Z", "d", "()Z", "setTls$okhttp", "(Z)V", "[Ljava/lang/String;", "getCipherSuites$okhttp", "()[Ljava/lang/String;", "e", "([Ljava/lang/String;)V", "getTlsVersions$okhttp", "g", "getSupportsTlsExtensions$okhttp", "f", "<init>", "connectionSpec", "(Lzd/l;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.l$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private boolean f20667a;

        /* renamed from: b, reason: collision with root package name */
        private String[] f20668b;

        /* renamed from: c, reason: collision with root package name */
        private String[] f20669c;

        /* renamed from: d, reason: collision with root package name */
        private boolean f20670d;

        public a(boolean z10) {
            this.f20667a = z10;
        }

        public final ConnectionSpec a() {
            return new ConnectionSpec(this.f20667a, this.f20670d, this.f20668b, this.f20669c);
        }

        public final a b(String... cipherSuites) {
            za.k.e(cipherSuites, "cipherSuites");
            if (!getF20667a()) {
                throw new IllegalArgumentException("no cipher suites for cleartext connections".toString());
            }
            if (!(cipherSuites.length == 0)) {
                e((String[]) cipherSuites.clone());
                return this;
            }
            throw new IllegalArgumentException("At least one cipher suite is required".toString());
        }

        public final a c(CipherSuite... cipherSuites) {
            za.k.e(cipherSuites, "cipherSuites");
            if (getF20667a()) {
                ArrayList arrayList = new ArrayList(cipherSuites.length);
                for (CipherSuite cipherSuite : cipherSuites) {
                    arrayList.add(cipherSuite.getF20654a());
                }
                Object[] array = arrayList.toArray(new String[0]);
                Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                String[] strArr = (String[]) array;
                return b((String[]) Arrays.copyOf(strArr, strArr.length));
            }
            throw new IllegalArgumentException("no cipher suites for cleartext connections".toString());
        }

        /* renamed from: d, reason: from getter */
        public final boolean getF20667a() {
            return this.f20667a;
        }

        public final void e(String[] strArr) {
            this.f20668b = strArr;
        }

        public final void f(boolean z10) {
            this.f20670d = z10;
        }

        public final void g(String[] strArr) {
            this.f20669c = strArr;
        }

        public final a h(boolean supportsTlsExtensions) {
            if (getF20667a()) {
                f(supportsTlsExtensions);
                return this;
            }
            throw new IllegalArgumentException("no TLS extensions for cleartext connections".toString());
        }

        public final a i(String... tlsVersions) {
            za.k.e(tlsVersions, "tlsVersions");
            if (!getF20667a()) {
                throw new IllegalArgumentException("no TLS versions for cleartext connections".toString());
            }
            if (!(tlsVersions.length == 0)) {
                g((String[]) tlsVersions.clone());
                return this;
            }
            throw new IllegalArgumentException("At least one TLS version is required".toString());
        }

        public final a j(TlsVersion... tlsVersions) {
            za.k.e(tlsVersions, "tlsVersions");
            if (getF20667a()) {
                ArrayList arrayList = new ArrayList(tlsVersions.length);
                for (TlsVersion tlsVersion : tlsVersions) {
                    arrayList.add(tlsVersion.getF20570e());
                }
                Object[] array = arrayList.toArray(new String[0]);
                Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                String[] strArr = (String[]) array;
                return i((String[]) Arrays.copyOf(strArr, strArr.length));
            }
            throw new IllegalArgumentException("no TLS versions for cleartext connections".toString());
        }

        public a(ConnectionSpec connectionSpec) {
            za.k.e(connectionSpec, "connectionSpec");
            this.f20667a = connectionSpec.getF20663a();
            this.f20668b = connectionSpec.f20665c;
            this.f20669c = connectionSpec.f20666d;
            this.f20670d = connectionSpec.getF20664b();
        }
    }
}
