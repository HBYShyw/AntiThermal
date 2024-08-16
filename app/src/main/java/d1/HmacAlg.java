package d1;

import t0.InvalidAlgorithmException;

/* compiled from: HmacAlg.java */
/* renamed from: d1.d, reason: use source file name */
/* loaded from: classes.dex */
public enum HmacAlg {
    HMAC_SHA1("sha1", 1),
    HMAC_SHA256("sha256", 2),
    HMAC_SHA384("sha384", 3),
    HMAC_SHA512("sha512", 4);


    /* renamed from: e, reason: collision with root package name */
    private final int f10702e;

    /* renamed from: f, reason: collision with root package name */
    private final String f10703f;

    HmacAlg(String str, int i10) {
        this.f10703f = str;
        this.f10702e = i10;
    }

    public static HmacAlg a(String str) {
        String lowerCase = str.toLowerCase();
        for (HmacAlg hmacAlg : values()) {
            if (hmacAlg.c().equals(lowerCase)) {
                return hmacAlg;
            }
        }
        throw new InvalidAlgorithmException("Do not support algorithm: " + lowerCase);
    }

    public int b() {
        return this.f10702e;
    }

    public String c() {
        return this.f10703f;
    }
}
