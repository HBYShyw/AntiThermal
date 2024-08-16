package x0;

import java.security.KeyPair;

/* compiled from: LocalBizKeyPairs.java */
/* renamed from: x0.b, reason: use source file name */
/* loaded from: classes.dex */
public class LocalBizKeyPairs {

    /* renamed from: a, reason: collision with root package name */
    private final Object f19446a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private String f19447b;

    /* renamed from: c, reason: collision with root package name */
    private String f19448c;

    /* renamed from: d, reason: collision with root package name */
    private KeyPair f19449d;

    /* renamed from: e, reason: collision with root package name */
    private KeyPair f19450e;

    public void a(String str, KeyPair keyPair, KeyPair keyPair2, String str2) {
        synchronized (this.f19446a) {
            this.f19447b = str;
            this.f19450e = keyPair;
            this.f19449d = keyPair2;
            this.f19448c = str2;
        }
    }

    public void b(LocalBizKeyPairs localBizKeyPairs) {
        synchronized (this.f19446a) {
            this.f19447b = localBizKeyPairs.c();
            this.f19450e = localBizKeyPairs.e();
            this.f19449d = localBizKeyPairs.d();
            this.f19448c = localBizKeyPairs.f();
        }
    }

    public String c() {
        return this.f19447b;
    }

    public KeyPair d() {
        return this.f19449d;
    }

    public KeyPair e() {
        return this.f19450e;
    }

    public String f() {
        return this.f19448c;
    }

    public LocalBizKeyPairs g() {
        LocalBizKeyPairs localBizKeyPairs;
        synchronized (this.f19446a) {
            localBizKeyPairs = new LocalBizKeyPairs();
            localBizKeyPairs.b(this);
        }
        return localBizKeyPairs;
    }
}
