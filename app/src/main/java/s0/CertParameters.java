package s0;

import android.content.Context;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import t0.InvalidArgumentException;

/* compiled from: CertParameters.java */
/* renamed from: s0.b, reason: use source file name */
/* loaded from: classes.dex */
public class CertParameters {

    /* renamed from: a, reason: collision with root package name */
    private final Context f17912a;

    /* renamed from: b, reason: collision with root package name */
    private final c f17913b;

    /* renamed from: c, reason: collision with root package name */
    private final X509Certificate[] f17914c;

    /* renamed from: d, reason: collision with root package name */
    private final X509Certificate[] f17915d;

    /* renamed from: e, reason: collision with root package name */
    private final X509Certificate f17916e;

    /* renamed from: f, reason: collision with root package name */
    private final Map<String, String> f17917f;

    /* renamed from: g, reason: collision with root package name */
    private final Map<String, String> f17918g;

    /* renamed from: h, reason: collision with root package name */
    private final String f17919h;

    /* compiled from: CertParameters.java */
    /* renamed from: s0.b$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private Context f17920a = null;

        /* renamed from: b, reason: collision with root package name */
        private c f17921b = c.OPLUS_LIST;

        /* renamed from: c, reason: collision with root package name */
        private X509Certificate[] f17922c = null;

        /* renamed from: d, reason: collision with root package name */
        private X509Certificate[] f17923d = null;

        /* renamed from: e, reason: collision with root package name */
        private X509Certificate f17924e = null;

        /* renamed from: f, reason: collision with root package name */
        private String f17925f = null;

        /* renamed from: g, reason: collision with root package name */
        private String f17926g = null;

        /* renamed from: h, reason: collision with root package name */
        private String f17927h = null;

        /* renamed from: i, reason: collision with root package name */
        private String f17928i = null;

        /* renamed from: j, reason: collision with root package name */
        private String f17929j = null;

        /* renamed from: k, reason: collision with root package name */
        private String f17930k = null;

        /* renamed from: l, reason: collision with root package name */
        private String f17931l = null;

        /* renamed from: m, reason: collision with root package name */
        private String f17932m = null;

        /* renamed from: n, reason: collision with root package name */
        private String f17933n = null;

        /* renamed from: o, reason: collision with root package name */
        private String f17934o = null;

        /* renamed from: p, reason: collision with root package name */
        private String f17935p = null;

        /* renamed from: q, reason: collision with root package name */
        private String f17936q = null;

        /* renamed from: r, reason: collision with root package name */
        private String f17937r = null;

        private static void t(Object... objArr) {
            if (objArr != null) {
                for (Object obj : objArr) {
                    if (obj == null) {
                        throw new InvalidArgumentException("The setting parameter cannot be null");
                    }
                }
                return;
            }
            throw new InvalidArgumentException("The setting parameter cannot be null");
        }

        public CertParameters s() {
            CertParameters certParameters = new CertParameters(this);
            if (certParameters.f17916e != null) {
                return certParameters;
            }
            throw new InvalidArgumentException("The endCertificate has not been set, please set the endCertificate");
        }

        public b u(Context context) {
            this.f17920a = context;
            return this;
        }

        public b v(X509Certificate x509Certificate) {
            t(x509Certificate);
            this.f17924e = x509Certificate;
            return this;
        }
    }

    /* compiled from: CertParameters.java */
    /* renamed from: s0.b$c */
    /* loaded from: classes.dex */
    public enum c {
        OPLUS_LIST,
        THIRD_PARTY_LIST,
        SYSTEM_LIST
    }

    public Context b() {
        return this.f17912a;
    }

    public X509Certificate c() {
        return this.f17916e;
    }

    public X509Certificate[] d() {
        return this.f17915d;
    }

    public X509Certificate[] e() {
        return this.f17914c;
    }

    public c f() {
        return this.f17913b;
    }

    private CertParameters(b bVar) {
        this.f17912a = bVar.f17920a;
        this.f17913b = bVar.f17921b;
        this.f17914c = bVar.f17922c;
        this.f17915d = bVar.f17923d;
        this.f17916e = bVar.f17924e;
        this.f17919h = bVar.f17925f;
        HashMap hashMap = new HashMap();
        hashMap.put("CN", bVar.f17926g);
        hashMap.put("O", bVar.f17928i);
        hashMap.put("L", bVar.f17930k);
        hashMap.put("ST", bVar.f17931l);
        hashMap.put("C", bVar.f17929j);
        hashMap.put("OU", bVar.f17927h);
        this.f17917f = hashMap;
        HashMap hashMap2 = new HashMap();
        hashMap2.put("CN", bVar.f17932m);
        hashMap2.put("O", bVar.f17934o);
        hashMap2.put("L", bVar.f17936q);
        hashMap2.put("ST", bVar.f17937r);
        hashMap2.put("C", bVar.f17935p);
        hashMap2.put("OU", bVar.f17933n);
        this.f17918g = hashMap2;
    }
}
