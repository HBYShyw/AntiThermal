package cn.teddymobile.free.anteater.rule.attribute.intent;

import android.os.Bundle;
import android.util.Pair;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.utils.JsonUtils;
import cn.teddymobile.free.anteater.rule.utils.RegularExpressionUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Extra {
    private static final String JSON_FIELD_ALIAS = "alias";
    private static final String JSON_FIELD_CAPTURE_PATTERN = "capture_pattern";
    private static final String JSON_FIELD_EXTRA_KEY = "extra_key";
    private static final String JSON_FIELD_JSON_PATTERN = "json_pattern";
    private static final String TAG = Extra.class.getSimpleName();
    private final String mAlias;
    private final String mCapturePattern;
    private final String mExtraKey;
    private final String mJsonPattern;

    public Extra(JSONObject attributeObject) throws JSONException {
        this.mExtraKey = attributeObject.getString(JSON_FIELD_EXTRA_KEY);
        this.mAlias = attributeObject.getString(JSON_FIELD_ALIAS);
        this.mCapturePattern = attributeObject.optString(JSON_FIELD_CAPTURE_PATTERN, null);
        this.mJsonPattern = attributeObject.optString(JSON_FIELD_JSON_PATTERN, null);
    }

    public String toString() {
        return "ExtraKey = " + this.mExtraKey + "\nAlias = " + this.mAlias + "\nCapturePattern = " + this.mCapturePattern + "\nJsonPattern = " + this.mJsonPattern;
    }

    public Pair<String, String> getValue(Bundle bundle) {
        Pair<String, String> result = null;
        if (bundle != null) {
            String value = bundle.get(this.mExtraKey) != null ? String.valueOf(bundle.get(this.mExtraKey)) : null;
            String str = TAG;
            Logger.i(str, "Extra value = " + value);
            String str2 = this.mJsonPattern;
            if (str2 != null) {
                value = JsonUtils.getValue(value, str2);
                Logger.i(str, "Json value = " + value);
            }
            String str3 = this.mCapturePattern;
            if (str3 != null) {
                value = RegularExpressionUtils.capture(value, str3);
                Logger.i(str, "Pattern value = " + value);
            }
            result = new Pair<>(this.mAlias, value);
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }
}
