package z0;

import org.json.JSONObject;
import s0.CryptoConfig;

/* compiled from: NoiseCryptoConfig.java */
/* renamed from: z0.e, reason: use source file name */
/* loaded from: classes.dex */
public class NoiseCryptoConfig implements CryptoConfig {

    /* renamed from: a, reason: collision with root package name */
    private final long f20184a;

    public NoiseCryptoConfig(long j10) {
        this.f20184a = j10;
    }

    @Override // s0.CryptoConfig
    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("version", this.f20184a);
        return jSONObject;
    }
}
