package android.view.rgbnormalize;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.rgbnormalize.RGBNormalizePolicyHelper;
import com.android.internal.util.ImageUtils;

/* loaded from: classes.dex */
public class OplusRGBNormalizeManager implements IOplusRGBNormalizeManager {
    private static final int ALPHA_TOLERANCE = 50;
    private static final int ERROR_INT_VALUE = Integer.MIN_VALUE;
    private String mActivityName;
    private RGBNormalizePolicyHelper.OplusRGBViewInfo mAncestorRgbInfo;
    private ColorMatrix mColorMatrix;
    private ColorMatrixColorFilter mGrayScaleColorFilter;
    private Paint mLastPaint;
    private long mMainThreadId;
    private float[] mMatrix;
    private boolean mNeedSetColorFilter;
    private RGBNormalizePolicyHelper.OplusRGBViewInfo mViewSelfRgbInfo;
    private static final String TAG = OplusRGBNormalizeManager.class.getSimpleName();
    private static final float[] DEFAULT_MATRIX = {0.65f, 0.0f, 0.0f, 0.0f, 40.0f, 0.0f, 0.65f, 0.0f, 0.0f, 40.0f, 0.0f, 0.0f, 0.65f, 0.0f, 40.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};

    public OplusRGBNormalizeManager() {
        try {
            this.mMainThreadId = Looper.getMainLooper().getThread().getId();
        } catch (Exception e) {
            Log.d(TAG, "failed to get main thread id - " + e.getMessage());
        }
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void hookHandleBindApplication(ApplicationInfo appInfo) {
        RGBNormalizePolicyHelper.initRGBNormalizeApplicationInfo(appInfo);
        updateColorMatrixForCurrentView();
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void hookPerformLaunchActivity(ActivityInfo activityInfo) {
        this.mActivityName = activityInfo.name;
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void hookPerformResumeActivity(ActivityInfo activityInfo) {
        this.mActivityName = activityInfo.name;
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void beforeDraw(View view) {
        boolean isInPolicy = isInPolicy(view);
        this.mNeedSetColorFilter = isInPolicy;
        if (isInPolicy) {
            updateColorMatrixForCurrentView();
        }
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public boolean needSetColorFilterForCurrentRenderingView() {
        return this.mNeedSetColorFilter;
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public ColorMatrixColorFilter getGrayScaleColorFilter() {
        return this.mGrayScaleColorFilter;
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void hookPaintColor(Paint paint) {
        if (needSetColorFilterForCurrentRenderingView() && paint != null && paint.getColorFilter() == null && checkThread()) {
            this.mLastPaint = paint;
            int originColor = paint.getColor();
            if (this.mAncestorRgbInfo == null && this.mViewSelfRgbInfo == null) {
                paint.setColorFilter(this.mGrayScaleColorFilter);
                return;
            }
            RGBNormalizePolicyHelper.OplusRGBViewInfo viewInfo = getCorrectRgbInfo();
            if (viewInfo == null) {
                return;
            }
            int maxGrayValue = viewInfo.getMaxGrayScaleValue();
            int minGrayValue = viewInfo.getMinGrayScaleValue();
            if (ImageUtils.isGrayscale(originColor) && Color.alpha(originColor) > 50) {
                if (Color.red(originColor) < minGrayValue) {
                    paint.setColor(Color.rgb(minGrayValue, minGrayValue, minGrayValue));
                } else if (Color.red(originColor) > maxGrayValue) {
                    paint.setColor(Color.rgb(maxGrayValue, maxGrayValue, maxGrayValue));
                }
            }
        }
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void hookPaintBitmap(Paint paint, Bitmap bitmap) {
        if (needSetColorFilterForCurrentRenderingView() && paint != null && paint.getColorFilter() == null && bitmap != null && checkThread()) {
            this.mLastPaint = paint;
            int mainColor = getCenterMainColor(bitmap);
            if (mainColor == Integer.MIN_VALUE) {
                return;
            }
            int mainGrayValue = Color.red(mainColor);
            if (this.mAncestorRgbInfo == null && this.mViewSelfRgbInfo == null) {
                paint.setColorFilter(this.mGrayScaleColorFilter);
                return;
            }
            RGBNormalizePolicyHelper.OplusRGBViewInfo viewInfo = getCorrectRgbInfo();
            if (viewInfo == null) {
                return;
            }
            int maxGrayValue = viewInfo.getMaxGrayScaleValue();
            int minGrayValue = viewInfo.getMinGrayScaleValue();
            if (ImageUtils.isGrayscale(mainColor)) {
                if (mainGrayValue == 0) {
                    float[] matrix = {1.0f, 0.0f, 0.0f, 0.0f, minGrayValue, 0.0f, 1.0f, 0.0f, 0.0f, minGrayValue, 0.0f, 0.0f, 1.0f, 0.0f, minGrayValue, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
                    ColorMatrix colorMatrix = new ColorMatrix(matrix);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                    paint.setColorFilter(filter);
                    return;
                }
                if (mainGrayValue > maxGrayValue) {
                    float modifyRatio = (maxGrayValue * 1.0f) / mainGrayValue;
                    float[] matrix2 = {modifyRatio, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, modifyRatio, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, modifyRatio, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
                    ColorMatrix colorMatrix2 = new ColorMatrix(matrix2);
                    ColorMatrixColorFilter filter2 = new ColorMatrixColorFilter(colorMatrix2);
                    paint.setColorFilter(filter2);
                    return;
                }
                if (mainGrayValue < minGrayValue) {
                    float modifyRatio2 = (minGrayValue * 1.0f) / mainGrayValue;
                    float[] matrix3 = {modifyRatio2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, modifyRatio2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, modifyRatio2, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
                    ColorMatrix colorMatrix3 = new ColorMatrix(matrix3);
                    ColorMatrixColorFilter filter3 = new ColorMatrixColorFilter(colorMatrix3);
                    paint.setColorFilter(filter3);
                }
            }
        }
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void clear() {
        if (this.mLastPaint != null && checkThread()) {
            this.mLastPaint.setColorFilter(null);
            this.mLastPaint = null;
        }
    }

    @Override // android.view.rgbnormalize.IOplusRGBNormalizeManager
    public void onDumpActivity(Context context) {
        RGBNormalizePolicyHelper.analyzeHitRate(context);
    }

    private boolean isInPolicy(View view) {
        boolean ancestorInPolicy = false;
        ViewParent ancestor = view.getParent();
        if (ancestor != null && (ancestor instanceof View)) {
            while (ancestor != null && !ancestorInPolicy) {
                ancestorInPolicy = RGBNormalizePolicyHelper.isViewInPolicy(this.mActivityName, (View) ancestor);
                ViewParent parent = ancestor.getParent();
                if (parent != null && (parent instanceof View)) {
                    ancestor = parent;
                } else {
                    ancestor = null;
                }
            }
        }
        this.mAncestorRgbInfo = RGBNormalizePolicyHelper.getCurrentRenderingViewInfo();
        boolean inPolicy = RGBNormalizePolicyHelper.isViewInPolicy(this.mActivityName, view);
        this.mViewSelfRgbInfo = RGBNormalizePolicyHelper.getCurrentRenderingViewInfo();
        if (view instanceof ViewGroup) {
            return inPolicy || ancestorInPolicy;
        }
        return ancestorInPolicy;
    }

    private void updateColorMatrixForCurrentView() {
        float[] matrix = RGBNormalizePolicyHelper.getMatrix();
        this.mMatrix = matrix;
        float[] fArr = DEFAULT_MATRIX;
        int defaultSize = fArr.length;
        if (matrix == null || matrix.length != defaultSize) {
            this.mMatrix = fArr;
        }
        if (this.mColorMatrix == null) {
            this.mColorMatrix = new ColorMatrix();
        }
        this.mColorMatrix.set(this.mMatrix);
        if (this.mGrayScaleColorFilter == null) {
            this.mGrayScaleColorFilter = new ColorMatrixColorFilter(this.mColorMatrix);
        }
        this.mGrayScaleColorFilter.setColorMatrix(this.mColorMatrix);
    }

    private int getCenterMainColor(Bitmap bitmap) {
        Rect region = new Rect();
        region.set((int) (bitmap.getWidth() * 0.4f), (int) (bitmap.getHeight() * 0.4f), (int) (bitmap.getWidth() * 0.6f), (int) (bitmap.getHeight() * 0.6f));
        int[] pixels = getPixelsFromBitmap(bitmap, region);
        int allRed = 0;
        int allGreen = 0;
        int allBlue = 0;
        int allAlpha = 0;
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            if (Color.alpha(pixels[i]) >= 50) {
                allRed += Color.red(pixels[i]);
                allGreen += Color.green(pixels[i]);
                allBlue += Color.blue(pixels[i]);
                allAlpha += Color.alpha(pixels[i]);
                count++;
            }
        }
        if (count == 0) {
            return Integer.MIN_VALUE;
        }
        return Color.argb(allAlpha / count, allRed / count, allGreen / count, allBlue / count);
    }

    private int[] getPixelsFromBitmap(Bitmap bitmap, Rect region) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int[] pixels = new int[bitmapWidth * bitmapHeight];
        bitmap.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        if (region == null) {
            return pixels;
        }
        int regionWidth = region.width();
        int regionHeight = region.height();
        int[] subsetPixels = new int[regionWidth * regionHeight];
        for (int row = 0; row < regionHeight; row++) {
            System.arraycopy(pixels, ((region.top + row) * bitmapWidth) + region.left, subsetPixels, row * regionWidth, regionWidth);
        }
        return subsetPixels;
    }

    private RGBNormalizePolicyHelper.OplusRGBViewInfo getCorrectRgbInfo() {
        RGBNormalizePolicyHelper.OplusRGBViewInfo oplusRGBViewInfo = this.mViewSelfRgbInfo;
        if (oplusRGBViewInfo == null) {
            return this.mAncestorRgbInfo;
        }
        return oplusRGBViewInfo;
    }

    private boolean checkThread() {
        long currThreadId = Thread.currentThread().getId();
        if (currThreadId != this.mMainThreadId) {
            return false;
        }
        return true;
    }
}
