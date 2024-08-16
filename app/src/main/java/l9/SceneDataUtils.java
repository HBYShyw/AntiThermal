package l9;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.gson.GsonBuilder;

/* compiled from: SceneDataUtils.java */
/* renamed from: l9.h, reason: use source file name */
/* loaded from: classes2.dex */
public class SceneDataUtils {
    public static Bundle a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Bundle.class, new BundleDeserializer());
            return (Bundle) gsonBuilder.create().fromJson(str, Bundle.class);
        } catch (Throwable th) {
            LogUtils.b("SceneDataUtils", "setBundleFormJson, json=" + str + ", error=" + th);
            return null;
        }
    }
}
