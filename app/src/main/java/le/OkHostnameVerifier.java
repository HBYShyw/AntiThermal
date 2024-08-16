package le;

import ae.hostnames;
import com.oplus.deepthinker.sdk.app.awareness.fence.impl.SpecifiedLocationFence;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import kotlin.Metadata;
import kotlin.collections._Collections;
import kotlin.collections.r;
import me.c0;
import sd.StringsJVM;
import sd.v;
import za.k;

/* compiled from: OkHostnameVerifier.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0018\u0010\t\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\f\u0010\n\u001a\u00020\u0002*\u00020\u0002H\u0002J\f\u0010\u000b\u001a\u00020\u0006*\u00020\u0002H\u0002J\u001c\u0010\r\u001a\u00020\u00062\b\u0010\b\u001a\u0004\u0018\u00010\u00022\b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0002J\u001e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\u0018\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\u0016\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\u001a"}, d2 = {"Lle/d;", "Ljavax/net/ssl/HostnameVerifier;", "", "ipAddress", "Ljava/security/cert/X509Certificate;", "certificate", "", "h", "hostname", "g", "b", "d", SpecifiedLocationFence.BUNDLE_KEY_PATTERN, "f", "", "type", "", "c", "host", "Ljavax/net/ssl/SSLSession;", "session", "verify", "e", "a", "<init>", "()V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* renamed from: le.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class OkHostnameVerifier implements HostnameVerifier {

    /* renamed from: a, reason: collision with root package name */
    public static final OkHostnameVerifier f14709a = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    private final String b(String str) {
        if (!d(str)) {
            return str;
        }
        Locale locale = Locale.US;
        k.d(locale, "US");
        String lowerCase = str.toLowerCase(locale);
        k.d(lowerCase, "this as java.lang.String).toLowerCase(locale)");
        return lowerCase;
    }

    private final List<String> c(X509Certificate certificate, int type) {
        List<String> j10;
        Object obj;
        List<String> j11;
        try {
            Collection<List<?>> subjectAlternativeNames = certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                j11 = r.j();
                return j11;
            }
            ArrayList arrayList = new ArrayList();
            for (List<?> list : subjectAlternativeNames) {
                if (list != null && list.size() >= 2 && k.a(list.get(0), Integer.valueOf(type)) && (obj = list.get(1)) != null) {
                    arrayList.add((String) obj);
                }
            }
            return arrayList;
        } catch (CertificateParsingException unused) {
            j10 = r.j();
            return j10;
        }
    }

    private final boolean d(String str) {
        return str.length() == ((int) c0.b(str, 0, 0, 3, null));
    }

    private final boolean f(String hostname, String pattern) {
        boolean D;
        boolean q10;
        boolean D2;
        boolean q11;
        boolean q12;
        boolean q13;
        boolean I;
        boolean D3;
        int U;
        boolean q14;
        int Z;
        if (!(hostname == null || hostname.length() == 0)) {
            D = StringsJVM.D(hostname, ".", false, 2, null);
            if (!D) {
                q10 = StringsJVM.q(hostname, "..", false, 2, null);
                if (!q10) {
                    if (!(pattern == null || pattern.length() == 0)) {
                        D2 = StringsJVM.D(pattern, ".", false, 2, null);
                        if (!D2) {
                            q11 = StringsJVM.q(pattern, "..", false, 2, null);
                            if (!q11) {
                                q12 = StringsJVM.q(hostname, ".", false, 2, null);
                                if (!q12) {
                                    hostname = k.l(hostname, ".");
                                }
                                String str = hostname;
                                q13 = StringsJVM.q(pattern, ".", false, 2, null);
                                if (!q13) {
                                    pattern = k.l(pattern, ".");
                                }
                                String b10 = b(pattern);
                                I = v.I(b10, "*", false, 2, null);
                                if (!I) {
                                    return k.a(str, b10);
                                }
                                D3 = StringsJVM.D(b10, "*.", false, 2, null);
                                if (D3) {
                                    U = v.U(b10, '*', 1, false, 4, null);
                                    if (U != -1 || str.length() < b10.length() || k.a("*.", b10)) {
                                        return false;
                                    }
                                    String substring = b10.substring(1);
                                    k.d(substring, "this as java.lang.String).substring(startIndex)");
                                    q14 = StringsJVM.q(str, substring, false, 2, null);
                                    if (!q14) {
                                        return false;
                                    }
                                    int length = str.length() - substring.length();
                                    if (length > 0) {
                                        Z = v.Z(str, '.', length - 1, false, 4, null);
                                        if (Z != -1) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private final boolean g(String hostname, X509Certificate certificate) {
        String b10 = b(hostname);
        List<String> c10 = c(certificate, 2);
        if ((c10 instanceof Collection) && c10.isEmpty()) {
            return false;
        }
        Iterator<T> it = c10.iterator();
        while (it.hasNext()) {
            if (f14709a.f(b10, (String) it.next())) {
                return true;
            }
        }
        return false;
    }

    private final boolean h(String ipAddress, X509Certificate certificate) {
        String e10 = hostnames.e(ipAddress);
        List<String> c10 = c(certificate, 7);
        if ((c10 instanceof Collection) && c10.isEmpty()) {
            return false;
        }
        Iterator<T> it = c10.iterator();
        while (it.hasNext()) {
            if (k.a(e10, hostnames.e((String) it.next()))) {
                return true;
            }
        }
        return false;
    }

    public final List<String> a(X509Certificate certificate) {
        List<String> m02;
        k.e(certificate, "certificate");
        m02 = _Collections.m0(c(certificate, 7), c(certificate, 2));
        return m02;
    }

    public final boolean e(String host, X509Certificate certificate) {
        k.e(host, "host");
        k.e(certificate, "certificate");
        return ae.d.i(host) ? h(host, certificate) : g(host, certificate);
    }

    @Override // javax.net.ssl.HostnameVerifier
    public boolean verify(String host, SSLSession session) {
        Certificate certificate;
        k.e(host, "host");
        k.e(session, "session");
        if (d(host)) {
            try {
                certificate = session.getPeerCertificates()[0];
                if (certificate == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.security.cert.X509Certificate");
                }
            } catch (SSLException unused) {
                return false;
            }
        }
        return e(host, (X509Certificate) certificate);
    }
}
