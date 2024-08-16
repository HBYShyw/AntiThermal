package q0;

import e1.Base64Utils;
import org.json.JSONObject;

/* compiled from: EciesNegotiationInfo.java */
/* renamed from: q0.e, reason: use source file name */
/* loaded from: classes.dex */
public class EciesNegotiationInfo {

    /* renamed from: a, reason: collision with root package name */
    private byte[] f16779a;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f16780b;

    /* renamed from: c, reason: collision with root package name */
    private byte[] f16781c;

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        byte[] bArr = this.f16779a;
        if (bArr != null) {
            jSONObject.put("t_pub", Base64Utils.b(bArr));
        }
        byte[] bArr2 = this.f16780b;
        if (bArr2 != null) {
            jSONObject.put("salt", Base64Utils.b(bArr2));
        }
        byte[] bArr3 = this.f16781c;
        if (bArr3 != null) {
            jSONObject.put("info", Base64Utils.b(bArr3));
        }
        return jSONObject;
    }

    public byte[] b() {
        return this.f16781c;
    }

    public byte[] c() {
        return this.f16780b;
    }

    public byte[] d() {
        return this.f16779a;
    }

    public boolean e(JSONObject jSONObject) {
        String optString = jSONObject.optString("t_pub");
        if (optString.equals("")) {
            return false;
        }
        this.f16779a = Base64Utils.a(optString);
        String optString2 = jSONObject.optString("salt");
        if (!optString2.equals("")) {
            this.f16780b = Base64Utils.a(optString2);
        }
        String optString3 = jSONObject.optString("info");
        if (optString3.equals("")) {
            return true;
        }
        this.f16781c = Base64Utils.a(optString3);
        return true;
    }

    public void f(byte[] bArr) {
        this.f16781c = bArr;
    }

    public void g(byte[] bArr) {
        this.f16780b = bArr;
    }

    public void h(byte[] bArr) {
        this.f16779a = bArr;
    }
}
