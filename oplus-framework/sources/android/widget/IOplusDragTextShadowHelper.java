package android.widget;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.view.View;

/* loaded from: classes.dex */
public interface IOplusDragTextShadowHelper extends IOplusCommonFeature {
    public static final IOplusDragTextShadowHelper DEFAULT = new IOplusDragTextShadowHelper() { // from class: android.widget.IOplusDragTextShadowHelper.1
    };

    default IOplusDragTextShadowHelper getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDragTextShadowHelper;
    }

    default View.DragShadowBuilder getOplusTextThumbnailBuilder(View textview, String text) {
        return null;
    }
}
