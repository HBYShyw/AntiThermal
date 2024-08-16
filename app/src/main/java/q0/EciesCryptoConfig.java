package q0;

import e1.Base64Utils;
import org.json.JSONObject;
import s0.CryptoConfig;

/* compiled from: EciesCryptoConfig.java */
/* renamed from: q0.b, reason: use source file name */
/* loaded from: classes.dex */
public class EciesCryptoConfig implements CryptoConfig {

    /* renamed from: a, reason: collision with root package name */
    private final String f16770a;

    /* renamed from: b, reason: collision with root package name */
    private final long f16771b;

    /* renamed from: c, reason: collision with root package name */
    private final long f16772c;

    /* renamed from: d, reason: collision with root package name */
    private final byte[] f16773d;

    /* renamed from: e, reason: collision with root package name */
    private final byte[] f16774e;

    public EciesCryptoConfig(String str, long j10, long j11, byte[] bArr, byte[] bArr2) {
        this.f16770a = str;
        this.f16771b = j10;
        this.f16772c = j11;
        this.f16773d = bArr;
        this.f16774e = bArr2;
    }

    @Override // s0.CryptoConfig
    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("tmpPublicKey", this.f16770a);
        jSONObject.put("version", this.f16771b);
        jSONObject.put("certVersion", this.f16772c);
        byte[] bArr = this.f16773d;
        if (bArr != null) {
            jSONObject.put("salt", Base64Utils.b(bArr));
        }
        byte[] bArr2 = this.f16774e;
        if (bArr2 != null) {
            jSONObject.put("info", Base64Utils.b(bArr2));
        }
        return jSONObject;
    }
}
