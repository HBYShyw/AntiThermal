package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;

/* loaded from: classes.dex */
public interface IOplusCustomizeTextViewFeature extends IOplusCommonFeature {
    public static final IOplusCustomizeTextViewFeature DEFAULT = new IOplusCustomizeTextViewFeature() { // from class: android.widget.IOplusCustomizeTextViewFeature.1
    };
    public static final String NAME = "IOplusCustomizeTextViewFeature";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCustomizeTextViewFeature;
    }

    default IOplusCustomizeTextViewFeature getDefault() {
        return DEFAULT;
    }

    default void init(Context context) {
    }

    default boolean getClipboardStatus() {
        return true;
    }
}
