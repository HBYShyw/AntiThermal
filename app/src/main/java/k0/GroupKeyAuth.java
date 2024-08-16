package k0;

import e1.Base64Utils;
import i0.EncryptException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import l0.Authentication;
import l0.IAuthConfig;
import org.json.JSONException;
import org.json.JSONObject;
import u0.GukManager;
import v0.GukConfig;
import v0.GukSignatureData;

/* compiled from: GroupKeyAuth.java */
/* renamed from: k0.d, reason: use source file name */
/* loaded from: classes.dex */
public class GroupKeyAuth extends Authentication {

    /* renamed from: a, reason: collision with root package name */
    private final GukManager f14002a = new GukManager();

    private String c(GukSignatureData gukSignatureData) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("label", gukSignatureData.d());
        jSONObject.put("version", gukSignatureData.f());
        d(gukSignatureData, jSONObject);
        jSONObject.put("dataDgst", Base64Utils.b(gukSignatureData.a()));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("m", jSONObject);
        jSONObject2.put("mac", Base64Utils.b(gukSignatureData.h()));
        return jSONObject2.toString();
    }

    private void d(GukSignatureData gukSignatureData, JSONObject jSONObject) {
        GukConfig gukConfig = new GukConfig();
        if (!Arrays.equals(gukSignatureData.c(), gukConfig.b())) {
            jSONObject.put("info", Base64Utils.b(gukSignatureData.c()));
        }
        if (!Arrays.equals(gukSignatureData.g(), gukConfig.e())) {
            jSONObject.put("salt", Base64Utils.b(gukSignatureData.g()));
        }
        if (gukSignatureData.e() != gukConfig.d()) {
            jSONObject.put("l", gukSignatureData.e());
        }
        if (gukSignatureData.b().equals(gukConfig.a())) {
            return;
        }
        jSONObject.put("hash", gukSignatureData.b());
    }

    @Override // l0.Authentication
    public void a(IAuthConfig iAuthConfig) {
        if (iAuthConfig instanceof GukConfig) {
            this.f14002a.b((GukConfig) iAuthConfig);
        }
    }

    @Override // l0.Authentication
    public String b(String str) {
        try {
            GukSignatureData c10 = this.f14002a.c(str.getBytes(StandardCharsets.UTF_8));
            if (c10 == null) {
                return null;
            }
            return c(c10);
        } catch (JSONException e10) {
            throw new EncryptException(e10);
        }
    }
}
