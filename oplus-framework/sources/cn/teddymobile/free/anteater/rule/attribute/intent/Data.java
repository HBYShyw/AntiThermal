package cn.teddymobile.free.anteater.rule.attribute.intent;

import android.util.Pair;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.utils.RegularExpressionUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Data {
    private static final String JSON_FIELD_ALIAS = "alias";
    private static final String JSON_FIELD_CAPTURE_PATTERN = "capture_pattern";
    private static final String TAG = Data.class.getSimpleName();
    private final String mAlias;
    private final String mCapturePattern;

    public Data(JSONObject dataObject) throws JSONException {
        this.mCapturePattern = dataObject.optString(JSON_FIELD_CAPTURE_PATTERN, null);
        this.mAlias = dataObject.getString(JSON_FIELD_ALIAS);
    }

    public String toString() {
        return "Alias = " + this.mAlias + "\nCapturePattern = " + this.mCapturePattern;
    }

    public Pair<String, String> getValue(String data) {
        Pair<String, String> result = null;
        if (data != null) {
            String str = TAG;
            Logger.i(str, "Data Value = " + data);
            String str2 = this.mCapturePattern;
            if (str2 != null) {
                data = RegularExpressionUtils.capture(data, str2);
                Logger.i(str, "Pattern value = " + data);
            }
            result = new Pair<>(this.mAlias, data);
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }
}
