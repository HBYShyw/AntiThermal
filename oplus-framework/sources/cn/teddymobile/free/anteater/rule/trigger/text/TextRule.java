package cn.teddymobile.free.anteater.rule.trigger.text;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.trigger.TriggerRule;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class TextRule implements TriggerRule {
    private static final String JSON_FIELD_ACTION = "action";
    private static final String JSON_FIELD_KEYWORD = "keyword";
    private static final String JSON_FIELD_RECURSIVE = "recursive";
    private static final String JSON_FIELD_USE_CONTENT_DESCRIPTION = "use_content_description";
    private static final String TAG = TextRule.class.getSimpleName();
    private String mAction = null;
    private String mKeyword = null;
    private boolean mRecursive = false;
    private boolean mUseContentDescription = false;

    public String toString() {
        return "[TextRule] Action = " + this.mAction + "\nKeyword = " + this.mKeyword + "\nRecursive = " + this.mRecursive + "\nUseContentDescription = " + this.mUseContentDescription;
    }

    @Override // cn.teddymobile.free.anteater.rule.trigger.TriggerRule
    public void loadFromJSON(JSONObject ruleObject) throws JSONException {
        this.mAction = ruleObject.getString("action");
        this.mKeyword = ruleObject.getString(JSON_FIELD_KEYWORD);
        this.mRecursive = ruleObject.optBoolean(JSON_FIELD_RECURSIVE, true);
        this.mUseContentDescription = ruleObject.optBoolean(JSON_FIELD_USE_CONTENT_DESCRIPTION, true);
    }

    @Override // cn.teddymobile.free.anteater.rule.trigger.TriggerRule
    public boolean fitAction(String action, View view) {
        boolean result = false;
        if (action != null && action.equals(this.mAction)) {
            if (view instanceof TextView) {
                String str = TAG;
                Logger.i(str, view.getClass().getName() + " is TextView.");
                TextView textView = (TextView) view;
                if (textView.getText() != null) {
                    if (textView.getText().toString().contains(this.mKeyword)) {
                        result = true;
                    } else {
                        Logger.w(str, "Text is incorrect.");
                        Logger.w(str, "Expected = " + this.mKeyword);
                        Logger.w(str, "Actual = " + textView.getText().toString());
                    }
                } else {
                    Logger.w(str, "Text is null.");
                }
            } else {
                Logger.i(TAG, view.getClass().getName() + " is not TextView.");
            }
            if (!result && this.mUseContentDescription && view.getContentDescription() != null) {
                String str2 = TAG;
                Logger.i(str2, "Check ContentDescription.");
                if (view.getContentDescription().toString().contains(this.mKeyword)) {
                    result = true;
                } else {
                    Logger.w(str2, "ContentDescription is incorrect.");
                    Logger.w(str2, "Expected = " + this.mKeyword);
                    Logger.w(str2, "Actual = " + view.getContentDescription().toString());
                }
            }
            if (!result && this.mRecursive && (view instanceof ViewGroup)) {
                Logger.i(TAG, "Check Action recursive.");
                ViewGroup viewGroup = (ViewGroup) view;
                int i = 0;
                while (true) {
                    if (i >= viewGroup.getChildCount()) {
                        break;
                    }
                    View child = viewGroup.getChildAt(i);
                    if (!fitAction(action, child)) {
                        i++;
                    } else {
                        result = true;
                        break;
                    }
                }
            }
        } else {
            String str3 = TAG;
            Logger.w(str3, "Action is incorrect.");
            Logger.w(str3, "Expected = " + this.mAction);
            Logger.w(str3, "Actual = " + action);
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }
}
