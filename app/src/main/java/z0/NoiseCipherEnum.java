package z0;

import s0.CryptoParameters;

/* compiled from: NoiseCipherEnum.java */
/* renamed from: z0.d, reason: use source file name */
/* loaded from: classes.dex */
public enum NoiseCipherEnum {
    AESGCM(32, 12, CryptoParameters.b.f17973e),
    AESCTR(32, 16, CryptoParameters.b.f17974f);


    /* renamed from: e, reason: collision with root package name */
    private final int f20181e;

    /* renamed from: f, reason: collision with root package name */
    private final int f20182f;

    /* renamed from: g, reason: collision with root package name */
    private final CryptoParameters.b f20183g;

    NoiseCipherEnum(int i10, int i11, CryptoParameters.b bVar) {
        this.f20181e = i10;
        this.f20182f = i11;
        this.f20183g = bVar;
    }

    public CryptoParameters.b a() {
        return this.f20183g;
    }

    public int b() {
        return this.f20182f;
    }

    public int c() {
        return this.f20181e;
    }
}
