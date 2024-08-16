package a1;

import b1.KeyPairContainer;
import e1.Base64Utils;
import e1.KeyUtil;
import e1.i;
import java.security.KeyPair;
import java.util.Date;
import org.json.JSONObject;
import s0.CipherContainer;

/* compiled from: Util.java */
/* loaded from: classes.dex */
public class d {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static CipherContainer a(String str) {
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString("cipher");
        String optString2 = jSONObject.optString("iv");
        if (optString.isEmpty() || optString2.isEmpty()) {
            return null;
        }
        return new CipherContainer(Base64Utils.a(optString), Base64Utils.a(optString2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyPairContainer b(String str) {
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString("alias");
        String optString2 = jSONObject.optString("alg");
        String optString3 = jSONObject.optString("pub_key");
        String optString4 = jSONObject.optString("priv_key");
        if (!optString.isEmpty() && !optString2.isEmpty() && !optString3.isEmpty() && !optString4.isEmpty()) {
            KeyPair keyPair = new KeyPair(KeyUtil.b(Base64Utils.a(optString3), optString2), KeyUtil.a(Base64Utils.a(optString4), optString2));
            String optString5 = jSONObject.optString("expire_date");
            return new KeyPairContainer(optString, keyPair, optString5.isEmpty() ? null : new Date(Long.parseLong(optString5)));
        }
        i.d("Util", "jsonToKeyPair the key pair content is corrupted.");
        return null;
    }
}
