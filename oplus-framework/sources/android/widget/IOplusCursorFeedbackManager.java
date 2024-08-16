package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.view.KeyEvent;

/* loaded from: classes.dex */
public interface IOplusCursorFeedbackManager extends IOplusCommonFeature {
    public static final IOplusCursorFeedbackManager DEFAULT = new IOplusCursorFeedbackManager() { // from class: android.widget.IOplusCursorFeedbackManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCursorFeedbackManager;
    }

    default IOplusCursorFeedbackManager getDefault() {
        return DEFAULT;
    }

    default IOplusCursorFeedbackManager newInstance() {
        return DEFAULT;
    }

    default void init(Context context) {
    }

    default boolean refreshCursorRenderTime(Editor editor) {
        return false;
    }

    default int getExtraCursorWidth(Editor editor) {
        return 0;
    }

    default int getExtraLeftOffset(Editor editor) {
        return 0;
    }

    default void startCursorAnimator(Editor editor) {
    }

    default void stopCursorAnimator(Editor editor) {
    }

    default void handleKeyCodeDelDown(Editor editor, KeyEvent keyEvent) {
    }

    default void handleKeyCodeDelUp(Editor editor, KeyEvent keyEvent) {
    }

    default void editorDetachFromWindow(Editor editor) {
    }
}
