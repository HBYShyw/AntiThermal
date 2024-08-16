package p0;

import e1.Base64Utils;
import org.json.JSONObject;
import s0.CipherContainer;

/* compiled from: DigitalEnvelopeUtil.java */
/* renamed from: p0.a, reason: use source file name */
/* loaded from: classes.dex */
public class DigitalEnvelopeUtil {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(CipherContainer cipherContainer) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("cipher", Base64Utils.b(cipherContainer.a()));
        jSONObject.put("iv", Base64Utils.b(cipherContainer.b()));
        return jSONObject.toString();
    }
}
