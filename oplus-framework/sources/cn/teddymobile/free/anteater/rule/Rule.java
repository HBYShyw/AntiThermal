package cn.teddymobile.free.anteater.rule;

import android.view.View;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.rule.attribute.AttributeRule;
import cn.teddymobile.free.anteater.rule.html.HtmlRule;
import cn.teddymobile.free.anteater.rule.trigger.TriggerRule;
import cn.teddymobile.free.anteater.rule.trigger.context.ContextRule;
import cn.teddymobile.free.anteater.rule.trigger.hierarchy.ViewHierarchyRule;
import cn.teddymobile.free.anteater.rule.trigger.resource.ResourceNameRule;
import cn.teddymobile.free.anteater.rule.trigger.text.TextRule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Rule {
    private static final String JSON_FIELD_ATTRIBUTE_HTML = "html";
    private static final String JSON_FIELD_ATTRIBUTE_RULE = "attribute_rule";
    private static final String JSON_FIELD_EXTRACT_HTML = "extract_html";
    private static final String JSON_FIELD_PACKAGE_NAME = "package_name";
    private static final String JSON_FIELD_PARSER = "parser";
    private static final String JSON_FIELD_SCENE = "scene";
    private static final String JSON_FIELD_TRIGGER_RULE = "trigger_rule";
    private static final String JSON_FIELD_VERSION = "version";
    private static final String TAG = Rule.class.getSimpleName();
    private String mPackageName = null;
    private String mVersion = null;
    private String mScene = null;
    private String mParser = null;
    private boolean mExtractHtml = false;
    private List<TriggerRule> mTriggerRuleList = null;
    private List<AttributeRule> mAttributeRuleList = null;
    private HtmlRule mHtmlRule = null;

    public String toString() {
        return "PackageName = " + this.mPackageName + "\nVersion = " + this.mVersion + "\nScene = " + this.mScene + "\nParser = " + this.mParser + "\nExtractHtml = " + this.mExtractHtml + "\nHtmlRule = " + this.mHtmlRule.getClass().getSimpleName();
    }

    public void loadFromJSON(JSONObject ruleObject) throws JSONException {
        this.mPackageName = ruleObject.getString("package_name");
        this.mVersion = ruleObject.getString("version");
        this.mScene = ruleObject.getString("scene");
        this.mParser = ruleObject.getString(JSON_FIELD_PARSER);
        this.mExtractHtml = ruleObject.optBoolean(JSON_FIELD_EXTRACT_HTML, false);
        this.mTriggerRuleList = new ArrayList();
        JSONObject triggerRuleObject = ruleObject.getJSONObject(JSON_FIELD_TRIGGER_RULE);
        Iterator<String> triggerMethodIterator = triggerRuleObject.keys();
        while (triggerMethodIterator.hasNext()) {
            String triggerMethod = triggerMethodIterator.next();
            TriggerRule triggerRule = RuleFactory.createTriggerRule(triggerMethod);
            triggerRule.loadFromJSON(triggerRuleObject.getJSONObject(triggerMethod));
            this.mTriggerRuleList.add(triggerRule);
        }
        this.mAttributeRuleList = new ArrayList();
        JSONObject attributeRuleObject = ruleObject.getJSONObject(JSON_FIELD_ATTRIBUTE_RULE);
        Iterator<String> attributeMethodIterator = attributeRuleObject.keys();
        while (attributeMethodIterator.hasNext()) {
            String attributeMethod = attributeMethodIterator.next();
            AttributeRule attributeRule = RuleFactory.createAttributeRule(attributeMethod);
            attributeRule.loadFromJSON(attributeRuleObject.getJSONObject(attributeMethod));
            this.mAttributeRuleList.add(attributeRule);
        }
        this.mHtmlRule = RuleFactory.createDecodeRule();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public String getScene() {
        return this.mScene;
    }

    public String getParser() {
        return this.mParser;
    }

    public JSONObject extract(String action, View view) throws JSONException {
        JSONObject result = null;
        boolean triggerRulePassed = false;
        Iterator<TriggerRule> it = this.mTriggerRuleList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            TriggerRule triggerRule = it.next();
            Logger.i(TAG, "Check TriggerRule.");
            if (triggerRule.fitAction(action, view)) {
                triggerRulePassed = true;
                break;
            }
        }
        if (triggerRulePassed) {
            String str = TAG;
            Logger.i(str, "TriggerRule passed.");
            Logger.i(str, "Extract attribute.");
            result = new JSONObject();
            for (AttributeRule attributeRule : this.mAttributeRuleList) {
                JSONObject subResult = attributeRule.extractAttribute(view);
                merge(result, subResult);
            }
            if (this.mExtractHtml) {
                Logger.i(TAG, "Extract html.");
                String html = this.mHtmlRule.getHtml(view);
                result.put(JSON_FIELD_ATTRIBUTE_HTML, html);
            }
        } else {
            Logger.w(TAG, "TriggerRule Failed.");
        }
        if (result != null && !result.keys().hasNext()) {
            result = null;
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }

    public boolean check(String action, View view) {
        for (TriggerRule triggerRule : this.mTriggerRuleList) {
            String str = TAG;
            Logger.i(str, "Check TriggerRule.");
            if (triggerRule instanceof ContextRule) {
                ContextRule contextRule = (ContextRule) triggerRule;
                if (contextRule.fitAction(action, view)) {
                    return true;
                }
                Logger.i(str, "Check ContextRule fail.");
            } else if (triggerRule instanceof ViewHierarchyRule) {
                ViewHierarchyRule viewHierarchyRule = (ViewHierarchyRule) triggerRule;
                if (viewHierarchyRule.fitAction(action, view)) {
                    return true;
                }
                Logger.i(str, "Check ViewHierarchyRule fail.");
            } else if (triggerRule instanceof ResourceNameRule) {
                ResourceNameRule resourceNameRule = (ResourceNameRule) triggerRule;
                if (resourceNameRule.fitAction(action, view)) {
                    return true;
                }
                Logger.i(str, "Check ResourceNameRule fail.");
            } else if (triggerRule instanceof TextRule) {
                TextRule textRule = (TextRule) triggerRule;
                if (textRule.fitAction(action, view)) {
                    return true;
                }
                Logger.i(str, "Check TextRule fail.");
            } else {
                continue;
            }
        }
        return false;
    }

    public boolean check(View view) {
        return check("display", view);
    }

    private void merge(JSONObject result, JSONObject subResult) throws JSONException {
        if (result != null && subResult != null) {
            Iterator<String> iterator = subResult.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                result.put(key, subResult.get(key));
            }
        }
    }
}
