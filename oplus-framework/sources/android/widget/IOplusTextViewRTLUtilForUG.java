package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.Layout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;

/* loaded from: classes.dex */
public interface IOplusTextViewRTLUtilForUG extends IOplusCommonFeature {
    public static final IOplusTextViewRTLUtilForUG DEFAULT = new IOplusTextViewRTLUtilForUG() { // from class: android.widget.IOplusTextViewRTLUtilForUG.1
    };

    default IOplusTextViewRTLUtilForUG getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusTextViewRTLUtilForUG;
    }

    default Layout.Alignment getLayoutAlignmentForTextView(Layout.Alignment alignment, Context context, TextView textView) {
        return alignment;
    }

    default TextDirectionHeuristic getTextDirectionHeuristicForTextView(boolean defaultIsRtl) {
        return defaultIsRtl ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
    }

    default void initRtlParameter(Resources res) {
    }

    default boolean getOplusSupportRtl() {
        return false;
    }

    default boolean getDirectionAnyRtl() {
        return false;
    }

    default boolean getTextViewStart() {
        return false;
    }

    default boolean hasRtlSupportForView(Context context) {
        return context != null && context.getApplicationInfo().hasRtlSupport();
    }

    default void updateRtlParameterForUG(String[] availableLocales, Configuration newConfig) {
    }

    @Deprecated
    default void updateRtlParameterForUG(Resources res, Configuration newConfig) {
    }
}
