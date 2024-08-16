package q0;

import e1.Base64Utils;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import s0.n;

/* compiled from: RsaSceneData.java */
/* renamed from: q0.i, reason: use source file name */
/* loaded from: classes.dex */
public class RsaSceneData extends n {

    /* renamed from: f, reason: collision with root package name */
    private SecretKey f16790f;

    @Override // s0.n
    public JSONObject a() {
        JSONObject a10 = super.a();
        SecretKey secretKey = this.f16790f;
        if (secretKey != null) {
            a10.put("encryptKey", Base64Utils.b(secretKey.getEncoded()));
        }
        return a10;
    }

    @Override // s0.n
    public boolean h(JSONObject jSONObject) {
        super.h(jSONObject);
        String optString = jSONObject.optString("encryptKey");
        if (optString.equals("")) {
            return false;
        }
        this.f16790f = new SecretKeySpec(Base64Utils.a(optString), "AES");
        return true;
    }

    public SecretKey n() {
        return this.f16790f;
    }

    public void o(SecretKey secretKey) {
        this.f16790f = secretKey;
    }
}
