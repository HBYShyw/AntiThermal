package zd;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.Lambda;

/* compiled from: Handshake.kt */
@Metadata(bv = {}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0015B;\b\u0000\u0012\u0006\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u0013\u001a\u00020\u0012\u0012\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u0017\u0012\u0012\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00170 ¢\u0006\u0004\b\"\u0010#J\u0013\u0010\u0004\u001a\u00020\u00032\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016R\u0018\u0010\f\u001a\u00020\u0007*\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u000e\u001a\u00020\r8\u0007¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0013\u001a\u00020\u00128\u0007¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016R\u001d\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u00178\u0007¢\u0006\f\n\u0004\b\u0018\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR!\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\t0\u00178GX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001b¨\u0006$"}, d2 = {"Lzd/s;", "", "other", "", "equals", "", "hashCode", "", "toString", "Ljava/security/cert/Certificate;", "b", "(Ljava/security/cert/Certificate;)Ljava/lang/String;", "name", "Lzd/e0;", "tlsVersion", "Lzd/e0;", "e", "()Lzd/e0;", "Lzd/i;", "cipherSuite", "Lzd/i;", "a", "()Lzd/i;", "", "localCertificates", "Ljava/util/List;", "c", "()Ljava/util/List;", "peerCertificates$delegate", "Lma/h;", "d", "peerCertificates", "Lkotlin/Function0;", "peerCertificatesFn", "<init>", "(Lzd/e0;Lzd/i;Ljava/util/List;Lya/a;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.s, reason: use source file name */
/* loaded from: classes2.dex */
public final class Handshake {

    /* renamed from: e, reason: collision with root package name */
    public static final a f20701e = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final TlsVersion f20702a;

    /* renamed from: b, reason: collision with root package name */
    private final CipherSuite f20703b;

    /* renamed from: c, reason: collision with root package name */
    private final List<Certificate> f20704c;

    /* renamed from: d, reason: collision with root package name */
    private final ma.h f20705d;

    /* compiled from: Handshake.kt */
    @Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000b\u0010\fJ#\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00030\u0004*\f\u0012\u0006\b\u0001\u0012\u00020\u0003\u0018\u00010\u0002H\u0002¢\u0006\u0004\b\u0005\u0010\u0006J\u0013\u0010\t\u001a\u00020\b*\u00020\u0007H\u0007¢\u0006\u0004\b\t\u0010\n¨\u0006\r"}, d2 = {"Lzd/s$a;", "", "", "Ljava/security/cert/Certificate;", "", "b", "([Ljava/security/cert/Certificate;)Ljava/util/List;", "Ljavax/net/ssl/SSLSession;", "Lzd/s;", "a", "(Ljavax/net/ssl/SSLSession;)Lzd/s;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.s$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* compiled from: Handshake.kt */
        @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"", "Ljava/security/cert/Certificate;", "a", "()Ljava/util/List;"}, k = 3, mv = {1, 6, 0})
        /* renamed from: zd.s$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        static final class C0127a extends Lambda implements ya.a<List<? extends Certificate>> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ List<Certificate> f20706e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            C0127a(List<? extends Certificate> list) {
                super(0);
                this.f20706e = list;
            }

            @Override // ya.a
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final List<Certificate> invoke() {
                return this.f20706e;
            }
        }

        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final List<Certificate> b(Certificate[] certificateArr) {
            List<Certificate> j10;
            if (certificateArr != null) {
                return ae.d.v(Arrays.copyOf(certificateArr, certificateArr.length));
            }
            j10 = kotlin.collections.r.j();
            return j10;
        }

        public final Handshake a(SSLSession sSLSession) {
            List<Certificate> j10;
            za.k.e(sSLSession, "<this>");
            String cipherSuite = sSLSession.getCipherSuite();
            if (cipherSuite != null) {
                if (!(za.k.a(cipherSuite, "TLS_NULL_WITH_NULL_NULL") ? true : za.k.a(cipherSuite, "SSL_NULL_WITH_NULL_NULL"))) {
                    CipherSuite b10 = CipherSuite.f20586b.b(cipherSuite);
                    String protocol = sSLSession.getProtocol();
                    if (protocol != null) {
                        if (!za.k.a("NONE", protocol)) {
                            TlsVersion a10 = TlsVersion.f20563f.a(protocol);
                            try {
                                j10 = b(sSLSession.getPeerCertificates());
                            } catch (SSLPeerUnverifiedException unused) {
                                j10 = kotlin.collections.r.j();
                            }
                            return new Handshake(a10, b10, b(sSLSession.getLocalCertificates()), new C0127a(j10));
                        }
                        throw new IOException("tlsVersion == NONE");
                    }
                    throw new IllegalStateException("tlsVersion == null".toString());
                }
                throw new IOException(za.k.l("cipherSuite == ", cipherSuite));
            }
            throw new IllegalStateException("cipherSuite == null".toString());
        }
    }

    /* compiled from: Handshake.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"", "Ljava/security/cert/Certificate;", "a", "()Ljava/util/List;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: zd.s$b */
    /* loaded from: classes2.dex */
    static final class b extends Lambda implements ya.a<List<? extends Certificate>> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.a<List<Certificate>> f20707e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        b(ya.a<? extends List<? extends Certificate>> aVar) {
            super(0);
            this.f20707e = aVar;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<Certificate> invoke() {
            List<Certificate> j10;
            try {
                return this.f20707e.invoke();
            } catch (SSLPeerUnverifiedException unused) {
                j10 = kotlin.collections.r.j();
                return j10;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Handshake(TlsVersion tlsVersion, CipherSuite cipherSuite, List<? extends Certificate> list, ya.a<? extends List<? extends Certificate>> aVar) {
        za.k.e(tlsVersion, "tlsVersion");
        za.k.e(cipherSuite, "cipherSuite");
        za.k.e(list, "localCertificates");
        za.k.e(aVar, "peerCertificatesFn");
        this.f20702a = tlsVersion;
        this.f20703b = cipherSuite;
        this.f20704c = list;
        this.f20705d = ma.i.b(new b(aVar));
    }

    private final String b(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            return ((X509Certificate) certificate).getSubjectDN().toString();
        }
        String type = certificate.getType();
        za.k.d(type, "type");
        return type;
    }

    /* renamed from: a, reason: from getter */
    public final CipherSuite getF20703b() {
        return this.f20703b;
    }

    public final List<Certificate> c() {
        return this.f20704c;
    }

    public final List<Certificate> d() {
        return (List) this.f20705d.getValue();
    }

    /* renamed from: e, reason: from getter */
    public final TlsVersion getF20702a() {
        return this.f20702a;
    }

    public boolean equals(Object other) {
        if (other instanceof Handshake) {
            Handshake handshake = (Handshake) other;
            if (handshake.f20702a == this.f20702a && za.k.a(handshake.f20703b, this.f20703b) && za.k.a(handshake.d(), d()) && za.k.a(handshake.f20704c, this.f20704c)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((((527 + this.f20702a.hashCode()) * 31) + this.f20703b.hashCode()) * 31) + d().hashCode()) * 31) + this.f20704c.hashCode();
    }

    public String toString() {
        int u7;
        int u10;
        List<Certificate> d10 = d();
        u7 = kotlin.collections.s.u(d10, 10);
        ArrayList arrayList = new ArrayList(u7);
        Iterator<T> it = d10.iterator();
        while (it.hasNext()) {
            arrayList.add(b((Certificate) it.next()));
        }
        String obj = arrayList.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Handshake{tlsVersion=");
        sb2.append(this.f20702a);
        sb2.append(" cipherSuite=");
        sb2.append(this.f20703b);
        sb2.append(" peerCertificates=");
        sb2.append(obj);
        sb2.append(" localCertificates=");
        List<Certificate> list = this.f20704c;
        u10 = kotlin.collections.s.u(list, 10);
        ArrayList arrayList2 = new ArrayList(u10);
        Iterator<T> it2 = list.iterator();
        while (it2.hasNext()) {
            arrayList2.add(b((Certificate) it2.next()));
        }
        sb2.append(arrayList2);
        sb2.append('}');
        return sb2.toString();
    }
}
