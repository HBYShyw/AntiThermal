package cn.teddymobile.free.anteater.rule.trigger.resource;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.trigger.TriggerRule;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ResourceNameRule implements TriggerRule {
    private static final String JSON_FIELD_ACTION = "action";
    private static final String JSON_FIELD_RECURSIVE = "recursive";
    private static final String JSON_FIELD_RESOURCE_NAME = "resource_name";
    private static final String TAG = ResourceNameRule.class.getSimpleName();
    private String mAction = null;
    private String mResourceName = null;
    private boolean mRecursive = false;

    public String toString() {
        return "[ResourceNameRule] ResourceName = " + this.mResourceName + "\nAction = " + this.mAction + "\nRecursive = " + this.mRecursive;
    }

    @Override // cn.teddymobile.free.anteater.rule.trigger.TriggerRule
    public void loadFromJSON(JSONObject ruleObject) throws JSONException {
        this.mAction = ruleObject.getString("action");
        this.mResourceName = ruleObject.getString(JSON_FIELD_RESOURCE_NAME);
        this.mRecursive = ruleObject.optBoolean(JSON_FIELD_RECURSIVE, false);
    }

    @Override // cn.teddymobile.free.anteater.rule.trigger.TriggerRule
    public boolean fitAction(String action, View view) {
        boolean result = false;
        if (action != null && action.equals(this.mAction)) {
            try {
                String resourceName = view.getContext().getResources().getResourceName(view.getId());
                if (resourceName != null && resourceName.endsWith(this.mResourceName)) {
                    result = true;
                } else {
                    String str = TAG;
                    Logger.w(str, "ResourceName is incorrect.");
                    Logger.w(str, "Expected = " + this.mResourceName);
                    Logger.w(str, "Actual = " + resourceName);
                }
            } catch (Resources.NotFoundException e) {
                String str2 = TAG;
                Logger.w(str2, "ResourceName is not found.");
                Logger.w(str2, "Expected = " + this.mResourceName);
            }
            if (!result && this.mRecursive && (view instanceof ViewGroup)) {
                Logger.i(TAG, "Check ResourceName recursive.");
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
