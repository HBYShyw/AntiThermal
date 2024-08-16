package e1;

import java.util.Map;
import org.json.JSONObject;
import s0.CryptoConfig;

/* compiled from: CryptoConfigUtil.java */
/* renamed from: e1.c, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoConfigUtil {
    public static String a(Map<String, CryptoConfig> map) {
        JSONObject jSONObject = new JSONObject();
        for (String str : map.keySet()) {
            CryptoConfig cryptoConfig = map.get(str);
            if (cryptoConfig != null) {
                jSONObject.put(str, cryptoConfig.a());
            } else {
                throw new NullPointerException("scene(" + str + ") not found in cryptoConfigs.");
            }
        }
        return jSONObject.toString();
    }
}
