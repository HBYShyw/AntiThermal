package s9;

import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import org.json.JSONArray;
import org.json.JSONObject;
import za.k;

/* compiled from: DSLUtils.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ*\u0010\t\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0002J(\u0010\u000b\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001¨\u0006\u000e"}, d2 = {"Ls9/b;", "", "Lorg/json/JSONObject;", "entityObject", "", "id", "key", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "b", "jsonObject", "a", "<init>", "()V", "com.oplus.smartengine.smartenginehelper"}, k = 1, mv = {1, 4, 2})
/* renamed from: s9.b, reason: use source file name */
/* loaded from: classes2.dex */
public final class DSLUtils {

    /* renamed from: a, reason: collision with root package name */
    public static final DSLUtils f18190a = new DSLUtils();

    private DSLUtils() {
    }

    private final void b(JSONObject jSONObject, String str, String str2, Object obj) {
        if (k.a(str, jSONObject.optString("id"))) {
            if (obj == null) {
                jSONObject.remove(str2);
            } else {
                jSONObject.put(str2, obj);
            }
        }
    }

    public final void a(JSONObject jSONObject, String str, String str2, Object obj) {
        k.e(jSONObject, "jsonObject");
        k.e(str, "id");
        k.e(str2, "key");
        b(jSONObject, str, str2, obj);
        JSONArray optJSONArray = jSONObject.optJSONArray("child");
        if (optJSONArray != null) {
            int length = optJSONArray.length();
            for (int i10 = 0; i10 < length; i10++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i10);
                if (k.a("constraint", optJSONObject.getString("type"))) {
                    k.d(optJSONObject, "entityObject");
                    a(optJSONObject, str, str2, obj);
                } else {
                    k.d(optJSONObject, "entityObject");
                    b(optJSONObject, str, str2, obj);
                }
            }
        }
    }
}
