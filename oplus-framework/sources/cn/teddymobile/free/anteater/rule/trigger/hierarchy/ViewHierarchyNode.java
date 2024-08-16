package cn.teddymobile.free.anteater.rule.trigger.hierarchy;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import cn.teddymobile.free.anteater.logger.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ViewHierarchyNode {
    private static final String CHILD_INDEX_ARBITRARY = "?";
    private static final String CHILD_INDEX_FIRST = "first";
    private static final String CHILD_INDEX_LAST = "last";
    private static final String CLASS_NAME_ARBITRARY = "?";
    private static final String JSON_FIELD_CHILD_INDEX = "child_index";
    private static final String JSON_FIELD_CLASS_NAME = "class_name";
    private static final String JSON_FIELD_CLASS_NAME_OBFUSCATED = "class_name_obfuscated";
    private static final String TAG = ViewHierarchyNode.class.getSimpleName();
    private final String mChildIndex;
    private final boolean mChildIndexArbitrary;
    private final String mClassName;
    private final boolean mClassNameArbitrary;
    private final boolean mClassNameObfuscated;

    public ViewHierarchyNode(JSONObject nodeObject) throws JSONException {
        String string = nodeObject.getString(JSON_FIELD_CLASS_NAME);
        this.mClassName = string;
        String string2 = nodeObject.getString(JSON_FIELD_CHILD_INDEX);
        this.mChildIndex = string2;
        this.mClassNameObfuscated = nodeObject.optBoolean(JSON_FIELD_CLASS_NAME_OBFUSCATED, false);
        this.mClassNameArbitrary = string.equals("?");
        this.mChildIndexArbitrary = string2.equals("?");
    }

    public String toString() {
        return "ClassName = " + this.mClassName + "\nClassNameObfuscated = " + this.mClassNameObfuscated + "\nChildIndex = " + this.mChildIndex;
    }

    public boolean fitView(View view) {
        boolean result = false;
        if (view != null && (view.getParent() instanceof ViewGroup)) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (this.mClassNameObfuscated || this.mClassNameArbitrary || view.getClass().getName().equals(this.mClassName)) {
                if (this.mChildIndexArbitrary) {
                    result = true;
                } else {
                    Integer indexNumber = null;
                    if (this.mChildIndex.equals(CHILD_INDEX_FIRST)) {
                        indexNumber = 0;
                    } else if (this.mChildIndex.equals(CHILD_INDEX_LAST)) {
                        indexNumber = Integer.valueOf(parent.getChildCount() - 1);
                    } else if (TextUtils.isDigitsOnly(this.mChildIndex)) {
                        indexNumber = Integer.valueOf(Integer.parseInt(this.mChildIndex));
                    } else {
                        Logger.w(TAG, "Unknown child index " + this.mChildIndex);
                    }
                    if (indexNumber != null) {
                        if (indexNumber.intValue() >= 0 && indexNumber.intValue() < parent.getChildCount()) {
                            if (parent.getChildAt(indexNumber.intValue()).equals(view)) {
                                result = true;
                            } else {
                                String str = TAG;
                                Logger.w(str, "Child is incorrect.");
                                Logger.w(str, "Parent = " + parent.getClass().getName());
                                Logger.w(str, "Expected = " + view);
                                Logger.w(str, "Actual = " + parent.getChildAt(indexNumber.intValue()));
                            }
                        } else {
                            String str2 = TAG;
                            Logger.w(str2, "Child index is out of bounds.");
                            Logger.w(str2, "Parent = " + parent.getClass().getName());
                            Logger.w(str2, "Child = " + view.getClass().getName());
                            Logger.w(str2, "Index = " + indexNumber + "/" + parent.getChildCount());
                        }
                    }
                }
            } else {
                String str3 = TAG;
                Logger.w(str3, "Child class is incorrect.");
                Logger.w(str3, "Expected = " + this.mClassName + " " + this.mClassNameObfuscated);
                Logger.w(str3, "Actual = " + view.getClass().getName());
            }
        } else if (view != null) {
            Logger.w(TAG, view.getClass().getName() + " parent is not ViewGroup.");
        } else {
            Logger.w(TAG, "View is null.");
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }

    public View getView(View view) {
        View result = null;
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            Integer indexNumber = null;
            if (this.mChildIndex.equals(CHILD_INDEX_FIRST)) {
                indexNumber = 0;
            } else if (this.mChildIndex.equals(CHILD_INDEX_LAST)) {
                indexNumber = Integer.valueOf(parent.getChildCount() - 1);
            } else if (TextUtils.isDigitsOnly(this.mChildIndex)) {
                indexNumber = Integer.valueOf(Integer.parseInt(this.mChildIndex));
            } else {
                Logger.w(TAG, "Unknown child index " + this.mChildIndex);
            }
            if (indexNumber != null) {
                if (indexNumber.intValue() >= 0 && indexNumber.intValue() < parent.getChildCount()) {
                    View child = parent.getChildAt(indexNumber.intValue());
                    if (this.mClassNameObfuscated) {
                        return child;
                    }
                    if (child.getClass().getName().equals(this.mClassName)) {
                        result = child;
                    } else {
                        Integer autoFixIndex = autoFix(parent, this.mClassName);
                        if (autoFixIndex != null) {
                            String str = TAG;
                            Logger.w(str, "Child class is incorrect.");
                            Logger.w(str, "Parent = " + parent.getClass().getName());
                            Logger.w(str, "Index = " + this.mChildIndex);
                            Logger.w(str, "Expected = " + this.mClassName);
                            Logger.w(str, "Actual = " + child.getClass().getName());
                            Logger.w(str, "Auto fix index to " + autoFixIndex);
                            result = parent.getChildAt(autoFixIndex.intValue());
                        } else {
                            String str2 = TAG;
                            Logger.w(str2, "Child class is incorrect.");
                            Logger.w(str2, "Parent = " + parent.getClass().getName());
                            Logger.w(str2, "Index = " + this.mChildIndex);
                            Logger.w(str2, "Expected = " + this.mClassName);
                            Logger.w(str2, "Actual = " + child.getClass().getName());
                        }
                    }
                } else {
                    String str3 = TAG;
                    Logger.w(str3, "Child index is out of bounds.");
                    Logger.w(str3, "Parent = " + parent.getClass().getName());
                    Logger.w(str3, "Child = " + this.mClassName);
                    Logger.w(str3, "Index = " + this.mChildIndex + "/" + parent.getChildCount());
                }
            }
        } else if (view != null) {
            Logger.w(TAG, view.getClass().getName() + " parent is not ViewGroup.");
        } else {
            Logger.w(TAG, "View is null.");
        }
        Logger.i(TAG, getClass().getSimpleName() + " Result = " + result);
        return result;
    }

    private Integer autoFix(ViewGroup parent, String className) {
        if (parent != null && className != null) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child.getClass().getName().equals(className)) {
                    return Integer.valueOf(i);
                }
            }
            return null;
        }
        return null;
    }
}
