package android.view.rgbnormalize;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

/* loaded from: classes.dex */
public interface IOplusRGBNormalizeManager extends IOplusCommonFeature {
    public static final String ACTIVITY_NAME = "activity_name";
    public static final String BODY = "body";
    public static final IOplusRGBNormalizeManager DEFAULT = new IOplusRGBNormalizeManager() { // from class: android.view.rgbnormalize.IOplusRGBNormalizeManager.1
    };
    public static final String MATRIX = "matrix";
    public static final String POLICY = "policy";
    public static final String TAG = "RGBNormalize";
    public static final String VIEW_NAME = "view_name";
    public static final String VIEW_PARAMS = "view_params";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusRGBNormalizeManager;
    }

    default IOplusRGBNormalizeManager getDefault() {
        return DEFAULT;
    }

    default IOplusRGBNormalizeManager newInstance() {
        return DEFAULT;
    }

    default void hookPerformLaunchActivity(ActivityInfo activityInfo) {
    }

    default void hookPerformResumeActivity(ActivityInfo activityInfo) {
    }

    default void hookHandleBindApplication(ApplicationInfo appInfo) {
    }

    default void beforeDraw(View view) {
    }

    default boolean needSetColorFilterForCurrentRenderingView() {
        return false;
    }

    default ColorMatrixColorFilter getGrayScaleColorFilter() {
        return null;
    }

    default void hookPaintColor(Paint paint) {
    }

    default void hookPaintBitmap(Paint paint, Bitmap bitmap) {
    }

    default void clear() {
    }

    default void onDumpActivity(Context context) {
    }
}
