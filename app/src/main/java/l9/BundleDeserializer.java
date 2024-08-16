package l9;

import android.os.Bundle;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/* compiled from: BundleDeserializer.java */
/* renamed from: l9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class BundleDeserializer implements JsonDeserializer<Bundle> {
    private Bundle b(JsonElement jsonElement) {
        Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
        Bundle bundle = new Bundle(entrySet.size());
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String key = entry.getKey();
            if (key != null) {
                JsonElement value = entry.getValue();
                if (value.isJsonArray()) {
                    bundle.putParcelableArray(key, c(value));
                } else if (value.isJsonObject()) {
                    bundle.putBundle(key, b(value));
                } else if (value.isJsonPrimitive()) {
                    JsonPrimitive asJsonPrimitive = value.getAsJsonPrimitive();
                    if (asJsonPrimitive.isBoolean()) {
                        bundle.putBoolean(key, asJsonPrimitive.getAsBoolean());
                    } else if (asJsonPrimitive.isString() || asJsonPrimitive.isNumber()) {
                        bundle.putString(key, asJsonPrimitive.getAsString());
                    }
                }
            }
        }
        return bundle;
    }

    private Bundle[] c(JsonElement jsonElement) {
        JsonArray asJsonArray = jsonElement.getAsJsonArray();
        int size = asJsonArray.size();
        Bundle[] bundleArr = new Bundle[size];
        for (int i10 = 0; i10 < size; i10++) {
            JsonElement jsonElement2 = asJsonArray.get(i10);
            if (jsonElement2.isJsonObject()) {
                bundleArr[i10] = b(jsonElement2);
            } else {
                Log.e("BundleDeserializer", new JsonTreeReader(jsonElement2).getPath() + " is not json object.");
            }
        }
        return bundleArr;
    }

    @Override // com.google.gson.JsonDeserializer
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Bundle deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        if (jsonElement.isJsonObject()) {
            return b(jsonElement);
        }
        return null;
    }
}
