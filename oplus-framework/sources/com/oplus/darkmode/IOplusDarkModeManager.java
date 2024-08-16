package com.oplus.darkmode;

import android.app.Application;
import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.IBaseCanvasExt;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.SumEntity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface IOplusDarkModeManager extends IOplusCommonFeature {
    public static final IOplusDarkModeManager DEFAULT = new IOplusDarkModeManager() { // from class: com.oplus.darkmode.IOplusDarkModeManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDarkModeManager;
    }

    default IOplusDarkModeManager getDefault() {
        return DEFAULT;
    }

    default void init(Context context) {
    }

    default boolean forceDarkAllowedDefault(Context context, boolean forceDarkAllowedDefault) {
        return forceDarkAllowedDefault;
    }

    default void changeUsageForceDarkAlgorithmType(View view, int type) {
    }

    default int hideAutoChangeUiMode(int curMode) {
        return curMode;
    }

    default boolean useForcePowerSave() {
        return true;
    }

    default void logForceDarkAllowedStatus(Context context, boolean forceDarkAllowedDefault) {
    }

    default void logConfigurationNightError(Context context, boolean isNightConfiguration) {
    }

    default void setDarkModeProgress(View decor) {
    }

    default boolean forceDarkWithoutTheme(Context context, boolean useAutoDark) {
        return useAutoDark;
    }

    default void startDelayInjectJS(WebView webView) {
    }

    default WebViewClient createWebViewClientWrapper(WebView webView, WebViewClient client) {
        return client;
    }

    default void refreshForceDark(View decor) {
    }

    default int changeSystemUiVisibility(int oldSystemUiVisibility) {
        return oldSystemUiVisibility;
    }

    default void refreshForceDark(View decor, int colorForceDarkValue, float progress, float backgroundMaxL, float foregroundMinL) {
    }

    default int getDarkModeData(String packageName) {
        return 0;
    }

    default boolean isDarkModePage(String packageName, boolean systemDarkMode) {
        return systemDarkMode;
    }

    default float getDarkModeDialogBgMaxL(String packageName) {
        return -1.0f;
    }

    default float getDarkModeBackgroundMaxL(String packageName) {
        return -1.0f;
    }

    default float getDarkModeForegroundMinL(String packageName) {
        return -1.0f;
    }

    default IOplusDarkModeManager newOplusDarkModeManager() {
        return this;
    }

    default void forceDarkWithoutTheme(Context context, View view, boolean useAutoDark) {
    }

    default boolean shouldInterceptConfigRelaunch(int diff, Configuration configuration) {
        return false;
    }

    default boolean setDarkModeProgress(View decor, Configuration configuration) {
        return false;
    }

    default boolean ensureWebSettingDarkMode(WebView webView) {
        return false;
    }

    default void refreshForceDark(View decor, OplusDarkModeData oplusDarkModeData) {
    }

    default void markViewTypeBySize(View view) {
    }

    default void initDarkModeStatus(Application application) {
    }

    default boolean darkenSplitScreenDrawable(View decorView, Drawable resizingDrawable, int left, int top, int right, int bottom, RecordingCanvas canvas, RenderNode mFrameAndBackdropNode) {
        return true;
    }

    default int handleEraseColor(int color) {
        return color;
    }

    default boolean shouldIntercept() {
        return false;
    }

    default Bitmap handleDecodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        return null;
    }

    default Shader getDarkModeLinearGradient(float mX0, float mY0, float mX1, float mY1, int[] mColors, float[] mPositions, int mColor0, int mColor1, Shader.TileMode tileMode) {
        return null;
    }

    default Shader getDarkModeRadialGradient(float mX, float mY, float mRadius, int[] mColors, int mCenterColor, float[] mPositions, int mEdgeColor, Shader.TileMode tileMode) {
        return null;
    }

    default Shader getDarkModeSweepGradient(float mCx, float mCy, int[] mColors, float[] mPositions, int mColor0, int mColor1) {
        return null;
    }

    default int getVectorColor(int color) {
        return color;
    }

    default void changeColorFilterInDarkMode(ColorFilter colorFilter) {
    }

    default boolean isInDarkMode(boolean isHardware) {
        return false;
    }

    default IBaseCanvasExt.RealPaintState getRealPaintState(Paint paint) {
        return null;
    }

    default void changePaintWhenDrawText(Paint paint) {
    }

    default void resetRealPaintIfNeed(Paint paint, IBaseCanvasExt.RealPaintState realPaintState) {
    }

    default void changePaintWhenDrawArea(Paint paint, RectF rectF) {
    }

    default void changePaintWhenDrawArea(Paint paint, RectF rectF, Path path) {
    }

    default void changePaintWhenDrawPatch(NinePatch patch, Paint paint, RectF rectF) {
    }

    default int changeWhenDrawColor(int color, boolean isDarkMode) {
        return color;
    }

    default void changePaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF) {
    }

    default int[] getDarkModeColors(int[] colors) {
        return colors;
    }

    default Paint getPaintWhenDrawPatch(NinePatch patch, Paint paint, RectF rectF) {
        return null;
    }

    default Paint getPaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF) {
        return null;
    }

    default void markDrawChild(ViewGroup viewGroup, View view, Canvas canvas) {
    }

    default void markDispatchDraw(ViewGroup viewGroup, Canvas canvas) {
    }

    default void markBackground(View view, Canvas canvas) {
    }

    default void markForeground(View view, Canvas canvas) {
    }

    default void markOnDraw(View view, Canvas canvas) {
    }

    default void markDrawFadingEdge(View view, Canvas canvas) {
    }

    default void changePaintWhenDrawArea(Paint paint, RectF rectF, IBaseCanvasExt canvas) {
    }

    default void changePaintWhenDrawArea(Paint paint, RectF rectF, Path path, IBaseCanvasExt canvas) {
    }

    default void changePaintWhenDrawPatch(NinePatch patch, Paint paint, RectF rectF, IBaseCanvasExt canvas) {
    }

    default int changeWhenDrawColor(int color, boolean isDarkMode, IBaseCanvasExt canvas) {
        return color;
    }

    default void changePaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF, IBaseCanvasExt canvas) {
    }

    default int[] getDarkModeColors(int[] colors, IBaseCanvasExt canvas) {
        return colors;
    }

    default Paint getPaintWhenDrawPatch(NinePatch patch, Paint paint, RectF rectF, IBaseCanvasExt canvas) {
        return null;
    }

    default Paint getPaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF, IBaseCanvasExt canvas) {
        return null;
    }

    default void changePaintWhenDrawText(Paint paint, IBaseCanvasExt canvas) {
    }

    default ColorFilter getColorFilterWhenDrawVectorDrawable(SumEntity hEntity, SumEntity sEntity, SumEntity lEntity) {
        return null;
    }
}
