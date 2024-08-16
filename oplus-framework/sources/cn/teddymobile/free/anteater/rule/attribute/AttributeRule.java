package cn.teddymobile.free.anteater.rule.attribute;

import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public interface AttributeRule {
    JSONObject extractAttribute(View view);

    void loadFromJSON(JSONObject jSONObject) throws JSONException;
}
