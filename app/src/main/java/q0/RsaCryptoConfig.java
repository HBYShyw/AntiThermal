package q0;

import org.json.JSONObject;
import s0.CryptoConfig;

/* compiled from: RsaCryptoConfig.java */
/* renamed from: q0.h, reason: use source file name */
/* loaded from: classes.dex */
public class RsaCryptoConfig implements CryptoConfig {

    /* renamed from: a, reason: collision with root package name */
    private final String f16787a;

    /* renamed from: b, reason: collision with root package name */
    private final long f16788b;

    /* renamed from: c, reason: collision with root package name */
    private final long f16789c;

    public RsaCryptoConfig(String str, long j10, long j11) {
        this.f16787a = str;
        this.f16788b = j10;
        this.f16789c = j11;
    }

    @Override // s0.CryptoConfig
    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("protectedKey", this.f16787a);
        jSONObject.put("version", this.f16788b);
        jSONObject.put("certVersion", this.f16789c);
        return jSONObject;
    }

    public long b() {
        return this.f16789c;
    }
}
