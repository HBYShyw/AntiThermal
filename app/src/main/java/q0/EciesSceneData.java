package q0;

import e1.Base64Utils;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import s0.n;

/* compiled from: EciesSceneData.java */
/* renamed from: q0.g, reason: use source file name */
/* loaded from: classes.dex */
public class EciesSceneData extends n {

    /* renamed from: f, reason: collision with root package name */
    private SecretKey f16784f;

    /* renamed from: g, reason: collision with root package name */
    private EciesNegotiationInfo f16785g;

    /* renamed from: h, reason: collision with root package name */
    private long f16786h;

    @Override // s0.n
    public JSONObject a() {
        JSONObject a10 = super.a();
        SecretKey secretKey = this.f16784f;
        if (secretKey != null) {
            a10.put("encryptKey", Base64Utils.b(secretKey.getEncoded()));
        }
        EciesNegotiationInfo eciesNegotiationInfo = this.f16785g;
        if (eciesNegotiationInfo != null) {
            a10.put("ec_info", eciesNegotiationInfo.a());
        }
        a10.put("cert_version", this.f16786h);
        return a10;
    }

    @Override // s0.n
    public boolean h(JSONObject jSONObject) {
        super.h(jSONObject);
        String optString = jSONObject.optString("encryptKey");
        if (optString.equals("")) {
            return false;
        }
        this.f16784f = new SecretKeySpec(Base64Utils.a(optString), "AES");
        long optLong = jSONObject.optLong("cert_version", 0L);
        if (optLong == 0) {
            return false;
        }
        this.f16786h = optLong;
        JSONObject optJSONObject = jSONObject.optJSONObject("ec_info");
        if (optJSONObject == null) {
            return false;
        }
        EciesNegotiationInfo eciesNegotiationInfo = new EciesNegotiationInfo();
        this.f16785g = eciesNegotiationInfo;
        return eciesNegotiationInfo.e(optJSONObject);
    }

    public long n() {
        return this.f16786h;
    }

    public SecretKey o() {
        return this.f16784f;
    }

    public EciesNegotiationInfo p() {
        return this.f16785g;
    }

    public void q(long j10) {
        this.f16786h = j10;
    }

    public void r(SecretKey secretKey) {
        this.f16784f = secretKey;
    }

    public void s(EciesNegotiationInfo eciesNegotiationInfo) {
        this.f16785g = eciesNegotiationInfo;
    }
}
