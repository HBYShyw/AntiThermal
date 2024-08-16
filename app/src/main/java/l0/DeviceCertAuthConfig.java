package l0;

import java.security.PublicKey;

/* compiled from: DeviceCertAuthConfig.java */
/* renamed from: l0.c, reason: use source file name */
/* loaded from: classes.dex */
public class DeviceCertAuthConfig implements IAuthConfig {

    /* renamed from: a, reason: collision with root package name */
    private PublicKey f14573a;

    /* renamed from: b, reason: collision with root package name */
    private long f14574b = 0;

    /* renamed from: c, reason: collision with root package name */
    private boolean f14575c = true;

    public PublicKey a() {
        return this.f14573a;
    }

    public long b() {
        return this.f14574b;
    }

    public boolean c() {
        return this.f14575c;
    }

    public void d(boolean z10) {
        this.f14575c = z10;
    }

    public void e(PublicKey publicKey) {
        this.f14573a = publicKey;
    }

    public void f(long j10) {
        this.f14574b = j10;
    }
}
