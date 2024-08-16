package android.view.autolayout;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/* loaded from: classes.dex */
public interface IOplusAutoLayoutManager extends IOplusCommonFeature, IOplusAutoLayout2Manager {
    public static final String ACTIVITIES = "activities";
    public static final String ACTIVITY = "activity";
    public static final String ACTIVITY_NAME = "activity_name";
    public static final String ACTIVITY_POLICY = "activity_policy";
    public static final String APP_POLICY_NAME = "app";
    public static final String AUTOLAYOUT_NEED_COVER = "autolayout_need_cover";
    public static final String AUTOLAYOUT_SWITCH_STATUE = "autolayout_switch_status";
    public static final String AUTO_LAYOUT = "autolayout";
    public static final String CUSTOM_POLICY_NAME = "custom";
    public static final String GLOBAL = "global";
    public static final String GLOBAL_POLICY = "policy";
    public static final String HAS_COVER = "has_cover";
    public static final String STRETCH_POLICY = "stretch_policy";
    public static final String TAG = "AutoLayout";
    public static final IOplusAutoLayoutManager mDefault = new IOplusAutoLayoutManager() { // from class: android.view.autolayout.IOplusAutoLayoutManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusAutoLayoutManager;
    }

    default IOplusAutoLayoutManager getDefault() {
        return mDefault;
    }

    default IOplusAutoLayoutManager newInstance() {
        return mDefault;
    }

    default void hookPerformLaunchActivity(ActivityInfo activityInfo, Configuration configuration) {
    }

    default void hookPerformResumeActivity(ActivityInfo activityInfo, Configuration configuration) {
    }

    default void hookConfigurationChangedActivity(ActivityInfo activityInfo, Configuration configuration) {
    }

    default void handleBindApplication() {
    }

    default void hookHandleBindApplication(ApplicationInfo appInfo, Context context) {
    }

    default DisplayMetrics getAutoLayoutDisplayMetrics(DisplayMetrics originalMetrics) {
        return originalMetrics;
    }

    default void beforeUpdateDisplayListIfDirty(View view) {
    }

    default void beforeDraw(View view, Canvas canvas) {
    }

    default void afterDraw(View view, Canvas canvas) {
    }

    default int[] beforeMeasure(View view, int widthMeasureSpec, int heightMeasureSpec) {
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

    default int[] hookSetMeasureDimension(View view, int measuredWidth, int measuredHeight) {
        return new int[]{measuredWidth, measuredHeight};
    }

    default void afterMeasure(View view) {
    }

    default int[] beforeLayout(View view, int l, int t, int r, int b) {
        return new int[]{l, t, r, b};
    }

    default void afterLayout(View view) {
    }

    default void setTo(Configuration configuration) {
    }

    default void updateFrom(Configuration configuration) {
    }

    default ViewGroup.LayoutParams hookSetLayoutParams(ViewGroup.LayoutParams params) {
        return params;
    }

    default void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
    }

    default void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
    }

    default Rect drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        return dst;
    }

    default void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
    }

    default void start() {
    }

    default void end() {
    }

    default ImageView.ScaleType modifyScaleType(ImageView.ScaleType scaleType) {
        return scaleType;
    }

    default String dumpString(Object viewInfo) {
        return "";
    }
}
