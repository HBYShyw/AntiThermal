package z0;

import y0.CipherState;

/* compiled from: CipherStatePair.java */
/* renamed from: z0.b, reason: use source file name */
/* loaded from: classes.dex */
public class CipherStatePair {

    /* renamed from: a, reason: collision with root package name */
    private CipherState f20169a;

    /* renamed from: b, reason: collision with root package name */
    private CipherState f20170b;

    public CipherStatePair(CipherState cipherState, CipherState cipherState2) {
        this.f20169a = cipherState;
        this.f20170b = cipherState2;
    }

    public CipherState a() {
        return this.f20170b;
    }

    public CipherState b() {
        return this.f20169a;
    }

    public void c() {
        CipherState cipherState = this.f20169a;
        this.f20169a = this.f20170b;
        this.f20170b = cipherState;
    }
}
