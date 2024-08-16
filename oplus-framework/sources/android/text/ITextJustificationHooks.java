package android.text;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface ITextJustificationHooks extends IOplusCommonFeature {
    public static final ITextJustificationHooks DEFAULT = new ITextJustificationHooks() { // from class: android.text.ITextJustificationHooks.1
    };

    default ITextJustificationHooks getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.ITextJustificationHooks;
    }

    default void setTextViewParaSpacing(Object textview, float paraSpacing, Layout layout) {
    }

    default float getTextViewParaSpacing(Object textview) {
        return 0.0f;
    }

    default float getTextViewDefaultLineMulti(Object textview, float pxSize, float oriValue) {
        return oriValue;
    }

    default float calculateAddedWidth(float justifyWidth, float width, int spaces, int start, int end, boolean charsValid, char[] chars, CharSequence text, int mstart) {
        return 0.0f;
    }

    default float getLayoutParaSpacingAdded(StaticLayout layout, Object builder, boolean moreChars, CharSequence source, int endPos) {
        return 0.0f;
    }

    default void setLayoutParaSpacingAdded(Object object, float paraSpacing) {
    }

    default boolean lineShouldIncludeFontPad(boolean firstLine, StaticLayout layout) {
        return firstLine;
    }

    default boolean lineNeedMultiply(boolean needMultiply, boolean addLastLineLineSpacing, boolean lastLine, StaticLayout layout) {
        return needMultiply && (addLastLineLineSpacing || !lastLine);
    }
}
