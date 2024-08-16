package x0;

import java.security.cert.X509Certificate;

/* compiled from: UpgradeCertResponse.java */
/* renamed from: x0.d, reason: use source file name */
/* loaded from: classes.dex */
public class UpgradeCertResponse {

    /* renamed from: b, reason: collision with root package name */
    private String f19454b;

    /* renamed from: c, reason: collision with root package name */
    private X509Certificate f19455c;

    /* renamed from: d, reason: collision with root package name */
    private X509Certificate f19456d;

    /* renamed from: a, reason: collision with root package name */
    private final Object f19453a = new Object();

    /* renamed from: e, reason: collision with root package name */
    private long f19457e = 0;

    public void a(String str, X509Certificate x509Certificate, X509Certificate x509Certificate2, long j10) {
        synchronized (this.f19453a) {
            this.f19454b = str;
            this.f19455c = x509Certificate;
            this.f19456d = x509Certificate2;
            this.f19457e = j10;
        }
    }

    public void b(UpgradeCertResponse upgradeCertResponse) {
        synchronized (this.f19453a) {
            this.f19454b = upgradeCertResponse.c();
            this.f19455c = upgradeCertResponse.e();
            this.f19456d = upgradeCertResponse.d();
            this.f19457e = upgradeCertResponse.f();
        }
    }

    public String c() {
        return this.f19454b;
    }

    public X509Certificate d() {
        return this.f19456d;
    }

    public X509Certificate e() {
        return this.f19455c;
    }

    public long f() {
        return this.f19457e;
    }

    public boolean g() {
        return this.f19455c == null || this.f19456d == null;
    }

    public UpgradeCertResponse h() {
        UpgradeCertResponse upgradeCertResponse;
        synchronized (this.f19453a) {
            upgradeCertResponse = new UpgradeCertResponse();
            upgradeCertResponse.j(this.f19455c);
            upgradeCertResponse.i(this.f19456d);
            upgradeCertResponse.k(this.f19457e);
        }
        return upgradeCertResponse;
    }

    public void i(X509Certificate x509Certificate) {
        this.f19456d = x509Certificate;
    }

    public void j(X509Certificate x509Certificate) {
        this.f19455c = x509Certificate;
    }

    public void k(long j10) {
        this.f19457e = j10;
    }
}
