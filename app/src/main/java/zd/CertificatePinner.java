package zd;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Metadata;
import kotlin.collections._Collections;
import le.CertificateChainCleaner;
import me.g;
import sd.StringsJVM;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.TypeIntrinsics;

/* compiled from: CertificatePinner.kt */
@Metadata(bv = {}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\"\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0003\b\f\u000fB#\b\u0000\u0012\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001c\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010¢\u0006\u0004\b\u001e\u0010\u001fJ\u001c\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J+\u0010\f\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0012\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00040\tH\u0000¢\u0006\u0004\b\f\u0010\rJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\u0017\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0010H\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u0013\u0010\u0016\u001a\u00020\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0018\u001a\u00020\u0017H\u0016R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u00108\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0011\u0010\u0019\u001a\u0004\b\u001a\u0010\u001b¨\u0006 "}, d2 = {"Lzd/g;", "", "", "hostname", "", "Ljava/security/cert/Certificate;", "peerCertificates", "Lma/f0;", "a", "Lkotlin/Function0;", "Ljava/security/cert/X509Certificate;", "cleanedPeerCertificatesFn", "b", "(Ljava/lang/String;Lya/a;)V", "Lzd/g$c;", "c", "Lle/c;", "certificateChainCleaner", "e", "(Lle/c;)Lzd/g;", "other", "", "equals", "", "hashCode", "Lle/c;", "d", "()Lle/c;", "", "pins", "<init>", "(Ljava/util/Set;Lle/c;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: zd.g, reason: use source file name */
/* loaded from: classes2.dex */
public final class CertificatePinner {

    /* renamed from: c, reason: collision with root package name */
    public static final b f20571c = new b(null);

    /* renamed from: d, reason: collision with root package name */
    public static final CertificatePinner f20572d = new a().a();

    /* renamed from: a, reason: collision with root package name */
    private final Set<c> f20573a;

    /* renamed from: b, reason: collision with root package name */
    private final CertificateChainCleaner f20574b;

    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\u0006"}, d2 = {"Lzd/g$a;", "", "Lzd/g;", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.g$a */
    /* loaded from: classes2.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final List<c> f20575a = new ArrayList();

        public final CertificatePinner a() {
            Set D0;
            D0 = _Collections.D0(this.f20575a);
            return new CertificatePinner(D0, null, 2, 0 == true ? 1 : 0);
        }
    }

    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ\f\u0010\u0004\u001a\u00020\u0003*\u00020\u0002H\u0007J\f\u0010\u0005\u001a\u00020\u0003*\u00020\u0002H\u0007J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\u0006H\u0007R\u0014\u0010\u000b\u001a\u00020\n8\u0006X\u0087\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\f¨\u0006\u000f"}, d2 = {"Lzd/g$b;", "", "Ljava/security/cert/X509Certificate;", "Lme/g;", "b", "c", "Ljava/security/cert/Certificate;", "certificate", "", "a", "Lzd/g;", "DEFAULT", "Lzd/g;", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.g$b */
    /* loaded from: classes2.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String a(Certificate certificate) {
            za.k.e(certificate, "certificate");
            if (certificate instanceof X509Certificate) {
                return za.k.l("sha256/", c((X509Certificate) certificate).a());
            }
            throw new IllegalArgumentException("Certificate pinning requires X509 certificates".toString());
        }

        public final me.g b(X509Certificate x509Certificate) {
            za.k.e(x509Certificate, "<this>");
            g.a aVar = me.g.f15493h;
            byte[] encoded = x509Certificate.getPublicKey().getEncoded();
            za.k.d(encoded, "publicKey.encoded");
            return g.a.e(aVar, encoded, 0, 0, 3, null).r();
        }

        public final me.g c(X509Certificate x509Certificate) {
            za.k.e(x509Certificate, "<this>");
            g.a aVar = me.g.f15493h;
            byte[] encoded = x509Certificate.getPublicKey().getEncoded();
            za.k.d(encoded, "publicKey.encoded");
            return g.a.e(aVar, encoded, 0, 0, 3, null).s();
        }
    }

    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002J\b\u0010\u0006\u001a\u00020\u0002H\u0016J\u0013\u0010\b\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\n\u001a\u00020\tH\u0016R\u0017\u0010\u000b\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0010\u001a\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0014"}, d2 = {"Lzd/g$c;", "", "", "hostname", "", "c", "toString", "other", "equals", "", "hashCode", "hashAlgorithm", "Ljava/lang/String;", "b", "()Ljava/lang/String;", "Lme/g;", "hash", "Lme/g;", "a", "()Lme/g;", "okhttp"}, k = 1, mv = {1, 6, 0})
    /* renamed from: zd.g$c */
    /* loaded from: classes2.dex */
    public static final class c {

        /* renamed from: a, reason: collision with root package name */
        private final String f20576a;

        /* renamed from: b, reason: collision with root package name */
        private final String f20577b;

        /* renamed from: c, reason: collision with root package name */
        private final me.g f20578c;

        /* renamed from: a, reason: from getter */
        public final me.g getF20578c() {
            return this.f20578c;
        }

        /* renamed from: b, reason: from getter */
        public final String getF20577b() {
            return this.f20577b;
        }

        public final boolean c(String hostname) {
            boolean D;
            boolean D2;
            boolean u7;
            int Z;
            boolean u10;
            za.k.e(hostname, "hostname");
            D = StringsJVM.D(this.f20576a, "**.", false, 2, null);
            if (D) {
                int length = this.f20576a.length() - 3;
                int length2 = hostname.length() - length;
                u10 = StringsJVM.u(hostname, hostname.length() - length, this.f20576a, 3, length, false, 16, null);
                if (!u10) {
                    return false;
                }
                if (length2 != 0 && hostname.charAt(length2 - 1) != '.') {
                    return false;
                }
            } else {
                D2 = StringsJVM.D(this.f20576a, "*.", false, 2, null);
                if (D2) {
                    int length3 = this.f20576a.length() - 1;
                    int length4 = hostname.length() - length3;
                    u7 = StringsJVM.u(hostname, hostname.length() - length3, this.f20576a, 1, length3, false, 16, null);
                    if (!u7) {
                        return false;
                    }
                    Z = sd.v.Z(hostname, '.', length4 - 1, false, 4, null);
                    if (Z != -1) {
                        return false;
                    }
                } else {
                    return za.k.a(hostname, this.f20576a);
                }
            }
            return true;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof c)) {
                return false;
            }
            c cVar = (c) other;
            return za.k.a(this.f20576a, cVar.f20576a) && za.k.a(this.f20577b, cVar.f20577b) && za.k.a(this.f20578c, cVar.f20578c);
        }

        public int hashCode() {
            return (((this.f20576a.hashCode() * 31) + this.f20577b.hashCode()) * 31) + this.f20578c.hashCode();
        }

        public String toString() {
            return this.f20577b + '/' + this.f20578c.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CertificatePinner.kt */
    @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"", "Ljava/security/cert/X509Certificate;", "a", "()Ljava/util/List;"}, k = 3, mv = {1, 6, 0})
    /* renamed from: zd.g$d */
    /* loaded from: classes2.dex */
    public static final class d extends Lambda implements ya.a<List<? extends X509Certificate>> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ List<Certificate> f20580f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ String f20581g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        d(List<? extends Certificate> list, String str) {
            super(0);
            this.f20580f = list;
            this.f20581g = str;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final List<X509Certificate> invoke() {
            int u7;
            CertificateChainCleaner f20574b = CertificatePinner.this.getF20574b();
            List<Certificate> a10 = f20574b == null ? null : f20574b.a(this.f20580f, this.f20581g);
            if (a10 == null) {
                a10 = this.f20580f;
            }
            u7 = kotlin.collections.s.u(a10, 10);
            ArrayList arrayList = new ArrayList(u7);
            Iterator<T> it = a10.iterator();
            while (it.hasNext()) {
                arrayList.add((X509Certificate) ((Certificate) it.next()));
            }
            return arrayList;
        }
    }

    public CertificatePinner(Set<c> set, CertificateChainCleaner certificateChainCleaner) {
        za.k.e(set, "pins");
        this.f20573a = set;
        this.f20574b = certificateChainCleaner;
    }

    public final void a(String str, List<? extends Certificate> list) {
        za.k.e(str, "hostname");
        za.k.e(list, "peerCertificates");
        b(str, new d(list, str));
    }

    public final void b(String hostname, ya.a<? extends List<? extends X509Certificate>> cleanedPeerCertificatesFn) {
        za.k.e(hostname, "hostname");
        za.k.e(cleanedPeerCertificatesFn, "cleanedPeerCertificatesFn");
        List<c> c10 = c(hostname);
        if (c10.isEmpty()) {
            return;
        }
        List<? extends X509Certificate> invoke = cleanedPeerCertificatesFn.invoke();
        for (X509Certificate x509Certificate : invoke) {
            me.g gVar = null;
            me.g gVar2 = null;
            for (c cVar : c10) {
                String f20577b = cVar.getF20577b();
                if (za.k.a(f20577b, "sha256")) {
                    if (gVar == null) {
                        gVar = f20571c.c(x509Certificate);
                    }
                    if (za.k.a(cVar.getF20578c(), gVar)) {
                        return;
                    }
                } else if (za.k.a(f20577b, "sha1")) {
                    if (gVar2 == null) {
                        gVar2 = f20571c.b(x509Certificate);
                    }
                    if (za.k.a(cVar.getF20578c(), gVar2)) {
                        return;
                    }
                } else {
                    throw new AssertionError(za.k.l("unsupported hashAlgorithm: ", cVar.getF20577b()));
                }
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Certificate pinning failure!");
        sb2.append("\n  Peer certificate chain:");
        for (X509Certificate x509Certificate2 : invoke) {
            sb2.append("\n    ");
            sb2.append(f20571c.a(x509Certificate2));
            sb2.append(": ");
            sb2.append(x509Certificate2.getSubjectDN().getName());
        }
        sb2.append("\n  Pinned certificates for ");
        sb2.append(hostname);
        sb2.append(":");
        for (c cVar2 : c10) {
            sb2.append("\n    ");
            sb2.append(cVar2);
        }
        String sb3 = sb2.toString();
        za.k.d(sb3, "StringBuilder().apply(builderAction).toString()");
        throw new SSLPeerUnverifiedException(sb3);
    }

    public final List<c> c(String hostname) {
        List<c> j10;
        za.k.e(hostname, "hostname");
        Set<c> set = this.f20573a;
        j10 = kotlin.collections.r.j();
        for (Object obj : set) {
            if (((c) obj).c(hostname)) {
                if (j10.isEmpty()) {
                    j10 = new ArrayList<>();
                }
                TypeIntrinsics.a(j10).add(obj);
            }
        }
        return j10;
    }

    /* renamed from: d, reason: from getter */
    public final CertificateChainCleaner getF20574b() {
        return this.f20574b;
    }

    public final CertificatePinner e(CertificateChainCleaner certificateChainCleaner) {
        za.k.e(certificateChainCleaner, "certificateChainCleaner");
        return za.k.a(this.f20574b, certificateChainCleaner) ? this : new CertificatePinner(this.f20573a, certificateChainCleaner);
    }

    public boolean equals(Object other) {
        if (other instanceof CertificatePinner) {
            CertificatePinner certificatePinner = (CertificatePinner) other;
            if (za.k.a(certificatePinner.f20573a, this.f20573a) && za.k.a(certificatePinner.f20574b, this.f20574b)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (1517 + this.f20573a.hashCode()) * 41;
        CertificateChainCleaner certificateChainCleaner = this.f20574b;
        return hashCode + (certificateChainCleaner != null ? certificateChainCleaner.hashCode() : 0);
    }

    public /* synthetic */ CertificatePinner(Set set, CertificateChainCleaner certificateChainCleaner, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(set, (i10 & 2) != 0 ? null : certificateChainCleaner);
    }
}
