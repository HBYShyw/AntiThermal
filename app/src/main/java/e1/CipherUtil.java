package e1;

import org.json.JSONObject;
import s0.CipherContainer;

/* compiled from: CipherUtil.java */
/* renamed from: e1.b, reason: use source file name */
/* loaded from: classes.dex */
public class CipherUtil {
    private static CipherContainer a(String str) {
        JSONObject jSONObject = new JSONObject(str);
        return new CipherContainer(Base64Utils.a(jSONObject.getString("cipher")), Base64Utils.a(jSONObject.getString("iv")));
    }

    private static String b(CipherContainer cipherContainer) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("cipher", Base64Utils.b(cipherContainer.a()));
        jSONObject.put("iv", Base64Utils.b(cipherContainer.b()));
        return jSONObject.toString();
    }

    public static CipherContainer c(String str) {
        return a(str);
    }

    public static String d(CipherContainer cipherContainer) {
        return b(cipherContainer);
    }
}
