package cn.teddymobile.free.anteater.rule;

import cn.teddymobile.free.anteater.rule.attribute.AttributeRule;
import cn.teddymobile.free.anteater.rule.attribute.intent.IntentRule;
import cn.teddymobile.free.anteater.rule.attribute.reflection.ReflectionRule;
import cn.teddymobile.free.anteater.rule.attribute.webview.WebViewRule;
import cn.teddymobile.free.anteater.rule.html.HtmlRule;
import cn.teddymobile.free.anteater.rule.html.javascript.JavaScriptRule;
import cn.teddymobile.free.anteater.rule.trigger.TriggerRule;
import cn.teddymobile.free.anteater.rule.trigger.context.ContextRule;
import cn.teddymobile.free.anteater.rule.trigger.hierarchy.ViewHierarchyRule;
import cn.teddymobile.free.anteater.rule.trigger.resource.ResourceNameRule;
import cn.teddymobile.free.anteater.rule.trigger.text.TextRule;

/* loaded from: classes.dex */
public class RuleFactory {
    private static final String ATTRIBUTE_METHOD_INTENT = "intent";
    private static final String ATTRIBUTE_METHOD_REFLECTION = "reflection";
    private static final String ATTRIBUTE_METHOD_WEB_VIEW = "web_view";
    private static final String TRIGGER_METHOD_CONTEXT = "context";
    private static final String TRIGGER_METHOD_RESOURCE_NAME = "resource_name";
    private static final String TRIGGER_METHOD_TEXT = "text";
    private static final String TRIGGER_METHOD_VIEW_HIERARCHY = "view_hierarchy";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static TriggerRule createTriggerRule(String triggerMethod) throws IllegalArgumentException {
        char c;
        switch (triggerMethod.hashCode()) {
            case 3556653:
                if (triggerMethod.equals("text")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 951530927:
                if (triggerMethod.equals(TRIGGER_METHOD_CONTEXT)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 979421212:
                if (triggerMethod.equals(TRIGGER_METHOD_RESOURCE_NAME)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1420918043:
                if (triggerMethod.equals(TRIGGER_METHOD_VIEW_HIERARCHY)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return new TextRule();
            case 1:
                return new ResourceNameRule();
            case 2:
                return new ViewHierarchyRule();
            case 3:
                return new ContextRule();
            default:
                throw new IllegalArgumentException("Trigger method " + triggerMethod + " is undefined");
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static AttributeRule createAttributeRule(String attributeMethod) throws IllegalArgumentException {
        char c;
        switch (attributeMethod.hashCode()) {
            case -1366299605:
                if (attributeMethod.equals(ATTRIBUTE_METHOD_REFLECTION)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1183762788:
                if (attributeMethod.equals("intent")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -718398288:
                if (attributeMethod.equals(ATTRIBUTE_METHOD_WEB_VIEW)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return new IntentRule();
            case 1:
                return new WebViewRule();
            case 2:
                return new ReflectionRule();
            default:
                throw new IllegalArgumentException("Attribute method " + attributeMethod + " is undefined");
        }
    }

    public static HtmlRule createDecodeRule() {
        return new JavaScriptRule();
    }
}
