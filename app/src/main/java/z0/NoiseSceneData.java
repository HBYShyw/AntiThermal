package z0;

import e1.Base64Utils;
import org.json.JSONObject;
import s0.n;
import y0.CipherState;

/* compiled from: NoiseSceneData.java */
/* renamed from: z0.j, reason: use source file name */
/* loaded from: classes.dex */
public class NoiseSceneData extends n {

    /* renamed from: f, reason: collision with root package name */
    private CipherStatePair f20199f;

    @Override // s0.n
    public JSONObject a() {
        JSONObject a10 = super.a();
        CipherStatePair cipherStatePair = this.f20199f;
        if (cipherStatePair != null) {
            byte[] e10 = cipherStatePair.b().e();
            byte[] e11 = this.f20199f.a().e();
            if (e10 != null) {
                a10.put("send_k", Base64Utils.b(e10));
            }
            if (e11 != null) {
                a10.put("recv_k", Base64Utils.b(e11));
            }
        }
        return a10;
    }

    @Override // s0.n
    public boolean h(JSONObject jSONObject) {
        super.h(jSONObject);
        NoiseCipherEnum noiseCipherEnum = NoiseCipherEnum.AESGCM;
        NonceModeEnum nonceModeEnum = NonceModeEnum.RANDOM_IV;
        CipherState cipherState = new CipherState(noiseCipherEnum, nonceModeEnum);
        CipherState cipherState2 = new CipherState(noiseCipherEnum, nonceModeEnum);
        String optString = jSONObject.optString("send_k");
        String optString2 = jSONObject.optString("recv_k");
        if (optString.equals("") || optString2.equals("")) {
            return false;
        }
        cipherState.g(Base64Utils.a(optString));
        cipherState2.g(Base64Utils.a(optString2));
        this.f20199f = new CipherStatePair(cipherState, cipherState2);
        return true;
    }

    public CipherStatePair n() {
        return this.f20199f;
    }

    public void o(CipherStatePair cipherStatePair) {
        this.f20199f = cipherStatePair;
    }
}
