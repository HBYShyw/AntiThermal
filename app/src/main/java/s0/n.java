package s0;

import com.oplus.thermalcontrol.ThermalControlConfig;
import e1.DateUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: SceneData.java */
/* loaded from: classes.dex */
public abstract class n {

    /* renamed from: a, reason: collision with root package name */
    private String f18014a;

    /* renamed from: b, reason: collision with root package name */
    private EncryptAlgorithmEnum f18015b;

    /* renamed from: c, reason: collision with root package name */
    private long f18016c;

    /* renamed from: d, reason: collision with root package name */
    private long f18017d;

    /* renamed from: e, reason: collision with root package name */
    private long f18018e;

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ThermalControlConfig.CONFIG_TYPE_SCENE, this.f18014a);
        EncryptAlgorithmEnum encryptAlgorithmEnum = this.f18015b;
        if (encryptAlgorithmEnum != null) {
            jSONObject.put("encrypt_alg", encryptAlgorithmEnum.name());
        }
        jSONObject.put("version", this.f18017d);
        return jSONObject;
    }

    public EncryptAlgorithmEnum b() {
        return this.f18015b;
    }

    public long c() {
        return this.f18016c;
    }

    public long d() {
        return this.f18018e;
    }

    public String e() {
        return this.f18014a;
    }

    public long f() {
        return this.f18017d;
    }

    public boolean g() {
        return DateUtil.a() > this.f18018e;
    }

    public boolean h(JSONObject jSONObject) {
        try {
            this.f18014a = jSONObject.getString(ThermalControlConfig.CONFIG_TYPE_SCENE);
            String optString = jSONObject.optString("encrypt_alg");
            if (!optString.equals("")) {
                this.f18015b = EncryptAlgorithmEnum.d(optString);
            }
            this.f18017d = jSONObject.getLong("version");
            return true;
        } catch (JSONException e10) {
            e1.i.b("SceneData", "restore error. " + e10);
            return false;
        }
    }

    public void i(EncryptAlgorithmEnum encryptAlgorithmEnum) {
        this.f18015b = encryptAlgorithmEnum;
    }

    public void j(long j10) {
        this.f18016c = j10;
    }

    public void k(long j10) {
        this.f18018e = j10;
    }

    public void l(String str) {
        this.f18014a = str;
    }

    public void m(long j10) {
        this.f18017d = j10;
    }
}
