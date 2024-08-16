package com.oplus.darkmode;

import android.app.ActivityThread;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ComposeShader;
import android.graphics.IBaseCanvasExt;
import android.graphics.LinearGradient;
import android.graphics.NinePatch;
import android.graphics.OplusDarkModeThirdPartyFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.SumEntity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusDarkModeThirdInvertManager {
    private static final int COLORFUL_BITMAP = 8;
    private static final int FOREGROUND_ICON_SIZE = 40;
    private static final int FULL_DARK_BITMAP = 2;
    private static final int FULL_LIGHT_BITMAP = 1;
    private static final int HAS_WHITE_BITMAP = 7;
    private static final ColorFilter INVERT_FILTER = new ColorMatrixColorFilter(new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    private static final int MORE_BLACK_BITMAP = 5;
    private static final int MORE_WHITE_BITMAP = 4;
    private static final int WHITE_BLACK_BALANCE_BITMAP = 6;
    private static final boolean isDebug = false;
    private final Application mApplication;
    private float mBackgroundMaxL;
    private RectF mBounds;
    private OplusDarkModeThirdPartyFilter[] mCacheThirdPartyFilter;
    private RectF mDrawnBounds;
    private float mForegroundMinL;
    private boolean mIsAppSupportDarkMode;

    /* loaded from: classes.dex */
    private static class Holder {
        static final OplusDarkModeThirdInvertManager INSTANCE = new OplusDarkModeThirdInvertManager();

        private Holder() {
        }
    }

    public static OplusDarkModeThirdInvertManager getInstance() {
        return Holder.INSTANCE;
    }

    private OplusDarkModeThirdInvertManager() {
        this.mIsAppSupportDarkMode = false;
        this.mBackgroundMaxL = -1.0f;
        this.mForegroundMinL = -1.0f;
        this.mCacheThirdPartyFilter = new OplusDarkModeThirdPartyFilter[5];
        this.mDrawnBounds = new RectF();
        this.mBounds = new RectF();
        this.mApplication = ActivityThread.currentApplication();
    }

    public void setBackgroundMaxL(float value) {
        this.mBackgroundMaxL = value;
    }

    public float getBackgroundMaxL() {
        return this.mBackgroundMaxL;
    }

    public float getForegroundMinL() {
        return this.mForegroundMinL;
    }

    public void setForegroundMinL(float value) {
        this.mForegroundMinL = value;
    }

    public static void attachApplication(Application application) {
    }

    public void setIsSupportDarkModeStatus(int isSupportDarkMode) {
        this.mIsAppSupportDarkMode = isSupportDarkMode == 1;
    }

    private ColorFilter getBitmapFilter(int type) {
        OplusDarkModeThirdPartyFilter colorFilter = this.mCacheThirdPartyFilter[type];
        boolean createColorFilter = (colorFilter != null && colorFilter.getLABBgMaxL() == this.mBackgroundMaxL && colorFilter.getLABFgMinL() == this.mForegroundMinL) ? false : true;
        if (createColorFilter) {
            int bgColor = ColorUtils.LABToColor(this.mBackgroundMaxL, 0.0d, 0.0d);
            float[] hsl = new float[3];
            ColorUtils.colorToHSL(bgColor, hsl);
            float bgL = hsl[2];
            int fgColor = ColorUtils.LABToColor(this.mForegroundMinL, 0.0d, 0.0d);
            ColorUtils.colorToHSL(fgColor, hsl);
            OplusDarkModeThirdPartyFilter newColorFilter = new OplusDarkModeThirdPartyFilter(type, bgL, hsl[2], this.mBackgroundMaxL, this.mForegroundMinL);
            this.mCacheThirdPartyFilter[type] = newColorFilter;
            return newColorFilter;
        }
        return colorFilter;
    }

    public boolean isInDarkMode() {
        return this.mIsAppSupportDarkMode;
    }

    public boolean isInDarkMode(boolean isHardware) {
        return this.mIsAppSupportDarkMode;
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0284  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x02be  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0267  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void calculateBitmapColor(Bitmap bitmap, boolean isNinePatch, RectF rectF, int transformType) {
        boolean z;
        boolean maybeBlackPure;
        int whitePixelNum;
        boolean isPure;
        if (bitmap == null || OplusDarkModeUtils.hasCalculatedColor(bitmap)) {
            return;
        }
        if (bitmap.getConfig() == Bitmap.Config.HARDWARE || bitmap.getWidth() == 0) {
            z = true;
        } else {
            if (bitmap.getHeight() != 0) {
                int whitePixelNum2 = 0;
                int blackPixelNum = 0;
                int colorfulPixelNum = 0;
                int alphaPixelNum = 0;
                int heightSpacing = Math.max(1, bitmap.getHeight() / 10);
                int widthSpacing = Math.max(1, bitmap.getWidth() / 10);
                int allPixelNum = 0;
                float[] hsl = new float[3];
                int startColumn = 0;
                int endColumn = bitmap.getWidth();
                int startRow = 0;
                int endRow = bitmap.getHeight();
                float totalLightness = 0.0f;
                float maxH = 0.0f;
                float minH = 0.0f;
                float maxS = 0.0f;
                float minS = 0.0f;
                float maxL = 0.0f;
                float minL = 0.0f;
                if (isNinePatch) {
                    startColumn = 1;
                    endColumn = bitmap.getWidth() - 1;
                    startRow = 1;
                    endRow = bitmap.getHeight() - 1;
                }
                int i = startColumn;
                while (true) {
                    maybeBlackPure = false;
                    whitePixelNum = whitePixelNum2;
                    if (i >= endColumn) {
                        break;
                    }
                    int j = startRow;
                    while (j < endRow) {
                        int allPixelNum2 = allPixelNum + 1;
                        int pixel = bitmap.getPixel(i, j);
                        int startColumn2 = startColumn;
                        int alpha = Color.alpha(pixel);
                        int endColumn2 = endColumn;
                        int startRow2 = startRow;
                        if (alpha < 76.5d) {
                            alphaPixelNum++;
                        } else {
                            ColorUtils.colorToHSL(pixel, hsl);
                            if (OplusDarkModeUtils.isMaybeWhiteColor(hsl)) {
                                whitePixelNum++;
                            } else if (OplusDarkModeUtils.isMaybeBlackColor(hsl)) {
                                blackPixelNum++;
                            } else {
                                colorfulPixelNum++;
                            }
                            if (hsl[0] > maxH) {
                                float maxH2 = hsl[0];
                                if (minH != 0.0f) {
                                    maxH = maxH2;
                                } else {
                                    maxH = maxH2;
                                    minH = maxH2;
                                }
                            } else {
                                float maxH3 = hsl[0];
                                if (maxH3 < minH) {
                                    minH = hsl[0];
                                }
                            }
                            if (hsl[1] > maxS) {
                                float maxS2 = hsl[1];
                                if (minS != 0.0f) {
                                    maxS = maxS2;
                                } else {
                                    maxS = maxS2;
                                    minS = maxS;
                                }
                            } else {
                                float maxS3 = hsl[1];
                                if (maxS3 < minS) {
                                    minS = hsl[1];
                                }
                            }
                            if (hsl[2] > maxL) {
                                float maxL2 = hsl[2];
                                if (minL != 0.0f) {
                                    maxL = maxL2;
                                } else {
                                    maxL = maxL2;
                                    minL = maxL;
                                }
                            } else {
                                float maxL3 = hsl[2];
                                if (maxL3 < minL) {
                                    minL = hsl[2];
                                }
                            }
                            totalLightness += hsl[2];
                        }
                        j += heightSpacing;
                        allPixelNum = allPixelNum2;
                        startColumn = startColumn2;
                        endColumn = endColumn2;
                        startRow = startRow2;
                    }
                    i += widthSpacing;
                    whitePixelNum2 = whitePixelNum;
                }
                if (!isNinePatch && allPixelNum == 0) {
                    OplusDarkModeUtils.setHasCalculatedColor(bitmap, true);
                    return;
                }
                int startRow3 = 0;
                int endRow2 = bitmap.getWidth();
                if (isNinePatch && allPixelNum != 0) {
                    startRow3 = 1;
                    endRow2 = bitmap.getWidth() - 1;
                }
                int row = bitmap.getHeight() / 2;
                int i2 = startRow3;
                int allPixelNum3 = allPixelNum;
                int alphaPixelNum2 = alphaPixelNum;
                int alphaPixelNum3 = colorfulPixelNum;
                int colorfulPixelNum2 = blackPixelNum;
                int blackPixelNum2 = whitePixelNum;
                while (i2 < endRow2) {
                    int allPixelNum4 = allPixelNum3 + 1;
                    int pixel2 = bitmap.getPixel(i2, row);
                    int startRow4 = startRow3;
                    int alpha2 = Color.alpha(pixel2);
                    int row2 = row;
                    if (alpha2 < 76.5d) {
                        alphaPixelNum2++;
                    } else {
                        ColorUtils.colorToHSL(pixel2, hsl);
                        if (OplusDarkModeUtils.isMaybeWhiteColor(hsl)) {
                            blackPixelNum2++;
                        } else if (OplusDarkModeUtils.isMaybeBlackColor(hsl)) {
                            colorfulPixelNum2++;
                        } else {
                            alphaPixelNum3++;
                        }
                        if (hsl[0] > maxH) {
                            maxH = hsl[0];
                            if (minH == 0.0f) {
                                minH = maxH;
                            }
                        } else if (hsl[0] < minH) {
                            minH = hsl[0];
                        }
                        if (hsl[1] > maxS) {
                            maxS = hsl[1];
                            if (minS == 0.0f) {
                                minS = maxS;
                            }
                        } else if (hsl[1] < minS) {
                            minS = hsl[1];
                        }
                        if (hsl[2] > maxL) {
                            maxL = hsl[2];
                            if (minL == 0.0f) {
                                minL = maxL;
                            }
                        } else if (hsl[2] < minL) {
                            minL = hsl[2];
                        }
                        totalLightness += hsl[2];
                    }
                    i2 += widthSpacing;
                    startRow3 = startRow4;
                    row = row2;
                    allPixelNum3 = allPixelNum4;
                }
                if (alphaPixelNum2 == allPixelNum3) {
                    OplusDarkModeUtils.setHasCalculatedColor(bitmap, true);
                    return;
                }
                double notAlphaPixelNum = allPixelNum3 - alphaPixelNum2;
                double colorfulPixelPercent = (alphaPixelNum3 * 1.0d) / notAlphaPixelNum;
                double whitePixelPercent = (blackPixelNum2 * 1.0d) / notAlphaPixelNum;
                double blackPixelPercent = (colorfulPixelNum2 * 1.0d) / notAlphaPixelNum;
                double averageLightness = (totalLightness * 1.0d) / notAlphaPixelNum;
                float hDelta = maxH - minH;
                float sDelta = maxS - minS;
                float lDelta = maxL - minL;
                if (hDelta <= 30.0f && sDelta <= 0.1d) {
                    isPure = true;
                    boolean isBlackAndWhite = ((double) lDelta) < 0.8d && ((double) sDelta) <= 0.15d;
                    boolean maybeWhitePure = !OplusDarkModeUtils.isWeiBo(this.mApplication) ? colorfulPixelPercent <= 0.01d && averageLightness >= 0.995d : colorfulPixelPercent <= 0.2d && averageLightness >= 0.85d;
                    if (colorfulPixelPercent <= 0.2d && averageLightness <= 0.15d) {
                        maybeBlackPure = true;
                    }
                    if (!isNinePatch) {
                        Log.i("tag1", "colorfulPixelPercent:" + colorfulPixelPercent + ",whitePixelPercent:" + whitePixelPercent + ",blackPixelPercent:" + blackPixelPercent + ",averageLightness:" + averageLightness);
                    }
                    if ((!isPure || maybeBlackPure) && averageLightness <= 0.3d) {
                        OplusDarkModeUtils.setColorState(bitmap, 2);
                    } else if (isPure && maybeWhitePure && averageLightness >= 0.7d && !isBlackAndWhite) {
                        OplusDarkModeUtils.setColorState(bitmap, 1);
                    } else if (colorfulPixelPercent >= 0.3d) {
                        OplusDarkModeUtils.setColorState(bitmap, 8);
                    } else if (whitePixelPercent == 1.0d) {
                        OplusDarkModeUtils.setColorState(bitmap, 1);
                    } else if (isNinePatch && whitePixelPercent >= 0.25d) {
                        OplusDarkModeUtils.setColorState(bitmap, 1);
                    } else if (whitePixelPercent > 0.7d) {
                        OplusDarkModeUtils.setColorState(bitmap, 4);
                    } else if (blackPixelPercent == 1.0d) {
                        OplusDarkModeUtils.setColorState(bitmap, 2);
                    } else if (blackPixelPercent > 0.7d) {
                        OplusDarkModeUtils.setColorState(bitmap, 5);
                    } else if (Math.abs(whitePixelPercent - colorfulPixelNum2) < 0.3d) {
                        OplusDarkModeUtils.setColorState(bitmap, 6);
                    } else if (whitePixelPercent > 0.0d) {
                        OplusDarkModeUtils.setColorState(bitmap, 7);
                    }
                    OplusDarkModeUtils.setHasCalculatedColor(bitmap, true);
                    return;
                }
                isPure = false;
                if (((double) lDelta) < 0.8d) {
                }
                if (!OplusDarkModeUtils.isWeiBo(this.mApplication)) {
                }
                if (colorfulPixelPercent <= 0.2d) {
                    maybeBlackPure = true;
                }
                if (!isNinePatch) {
                }
                if (isPure) {
                }
                OplusDarkModeUtils.setColorState(bitmap, 2);
                OplusDarkModeUtils.setHasCalculatedColor(bitmap, true);
                return;
            }
            z = true;
        }
        OplusDarkModeUtils.setHasCalculatedColor(bitmap, z);
    }

    private Paint changePaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF, boolean isNinePatch, IBaseCanvasExt canvas) {
        Paint paint2;
        Paint paint3;
        Paint paint4;
        Paint paint5;
        Paint paint6;
        Paint paint7;
        Paint paint8;
        Paint paint9;
        if (bitmap != null && !bitmap.isRecycled()) {
            if (paint != null) {
                if (makeColorFilterColor(paint.getColorFilter(), canvas != null ? canvas.getTransformType() : 2, canvas)) {
                    OplusDarkModeUtils.setHasCalculatedColor(bitmap, true);
                    return paint;
                }
            }
            calculateBitmapColor(bitmap, isNinePatch, rectF, canvas != null ? canvas.getTransformType() : 2);
            int bitmapState = OplusDarkModeUtils.getColorState(bitmap);
            int transformType = canvas != null ? canvas.getTransformType() : 2;
            if (isNinePatch && (bitmapState == 1 || bitmapState == 4 || bitmapState == 7 || bitmapState == 6)) {
                if (paint != null) {
                    paint9 = paint;
                } else {
                    paint9 = new Paint();
                }
                paint9.setColorFilter(getBitmapFilter(3));
                return paint9;
            }
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            double bitmapDensity = (bitmap.getDensity() * 1.0d) / 160.0d;
            double bitmapWidthDp = bitmapWidth / bitmapDensity;
            double bitmapHeightDp = bitmapHeight / bitmapDensity;
            float screenWidth = OplusDarkModeUtils.getScreenWidth(this.mApplication);
            switch (transformType) {
                case 1:
                    if (bitmapHeightDp <= 40.0d && bitmapWidthDp <= 40.0d) {
                        if (bitmapState == 2 || bitmapState == 5) {
                            if (paint != null) {
                                paint3 = paint;
                            } else {
                                paint3 = new Paint();
                            }
                            paint3.setColorFilter(getBitmapFilter(4));
                            return paint3;
                        }
                        if (bitmapState == 1 || bitmapState == 4) {
                            if (paint != null) {
                                paint4 = paint;
                            } else {
                                paint4 = new Paint();
                            }
                            paint4.setColorFilter(getBitmapFilter(3));
                            return paint4;
                        }
                        if (bitmapState == 7 || bitmapState == 6) {
                            if (paint != null) {
                                paint5 = paint;
                            } else {
                                paint5 = new Paint();
                            }
                            paint5.setColorFilter(getBitmapFilter(2));
                            return paint5;
                        }
                    } else if (bitmapState == 1) {
                        if (paint != null) {
                            paint2 = paint;
                        } else {
                            paint2 = new Paint();
                        }
                        paint2.setColorFilter(getBitmapFilter(3));
                        return paint2;
                    }
                    return paint;
                case 2:
                    if (bitmapHeightDp <= 40.0d && bitmapWidthDp <= 40.0d) {
                        if (bitmapState == 2 || bitmapState == 5) {
                            if (paint != null) {
                                paint7 = paint;
                            } else {
                                paint7 = new Paint();
                            }
                            paint7.setColorFilter(getBitmapFilter(4));
                            return paint7;
                        }
                        if (bitmapState == 6) {
                            if (paint != null) {
                                paint8 = paint;
                            } else {
                                paint8 = new Paint();
                            }
                            paint8.setColorFilter(getBitmapFilter(1));
                            return paint8;
                        }
                    } else if (bitmapWidth >= screenWidth * 0.8d && bitmapState == 1) {
                        if (paint != null) {
                            paint6 = paint;
                        } else {
                            paint6 = new Paint();
                        }
                        paint6.setColorFilter(getBitmapFilter(3));
                        return paint6;
                    }
                    return paint;
                default:
                    return paint;
            }
        }
        return paint;
    }

    public void changePaintWhenDrawText(Paint paint, IBaseCanvasExt canvas) {
        if (isInDarkMode()) {
            int color = paint.getColor();
            paint.setColor(getDarkModeColor(color, 2));
        }
    }

    public int changeWhenDrawColor(int color, boolean isDarkMode, IBaseCanvasExt canvas) {
        if (isDarkMode) {
            return getDarkModeColor(color, 1);
        }
        return color;
    }

    public void changePaintWhenDrawArea(Paint paint, RectF rectF, Path path, IBaseCanvasExt canvas) {
        Shader shader = paint.getShader();
        if (shader != null) {
            handleShader(paint, rectF, shader, canvas);
            return;
        }
        if (!makeColorFilterColor(paint.getColorFilter(), path != null ? 2 : 1, canvas)) {
            handleAreaColor(paint, rectF, path, canvas);
        }
    }

    public void changePaintWhenDrawArea(Paint paint, RectF rectF, IBaseCanvasExt canvas) {
        changePaintWhenDrawArea(paint, rectF, null, canvas);
    }

    private void handleAreaColor(Paint paint, RectF rectF, Path path, IBaseCanvasExt canvas) {
        int color = paint.getColor();
        boolean hasPath = path != null;
        int transformType = canvas != null ? canvas.getTransformType() : 2;
        if (hasPath) {
            if (OplusDarkModeUtils.isMaybeWhiteColor(paint.getColor()) && transformType == 2) {
                transformType = 1;
            }
            paint.setColor(getDarkModeColor(color, transformType));
            return;
        }
        if (paint.getStyle() == Paint.Style.STROKE && paint.getStrokeWidth() <= OplusDarkModeUtils.getDpDensity(this.mApplication) * 3.0f) {
            paint.setColor(getDarkModeColor(paint.getColor(), 2));
            return;
        }
        int transformType2 = changeAreaTransformType(rectF, canvas, transformType);
        if (OplusDarkModeUtils.isMaybeWhiteColor(paint.getColor()) && transformType2 == 2) {
            transformType2 = 1;
        }
        paint.setColor(getDarkModeColor(color, transformType2));
    }

    private int changeAreaTransformType(RectF rectF, IBaseCanvasExt canvas, int transformType) {
        int viewType = canvas != null ? canvas.getOplusViewType() : 0;
        if (viewType != 2) {
            return transformType;
        }
        return 1;
    }

    private void handleShader(Paint paint, RectF rectF, Shader shader, IBaseCanvasExt canvas) {
        long[] colors;
        List<long[]> colors2;
        if (shader instanceof BitmapShader) {
            Bitmap bitmap = ((BitmapShader) shader).getWrapper().getBitmap();
            changePaintWhenDrawBitmap(paint, bitmap, rectF, canvas);
            return;
        }
        if ((shader instanceof LinearGradient) || (shader instanceof SweepGradient) || (shader instanceof RadialGradient)) {
            if (shader != null && shader.mShaderExt != null && (colors = shader.mShaderExt.getColorLongs()) != null) {
                long[] newColors = (long[]) colors.clone();
                int transformType = changeAreaTransformType(rectF, canvas, canvas != null ? canvas.getTransformType() : 2);
                getShaderDarkModeColors(newColors, transformType);
                shader.mShaderExt.setColors(newColors);
                return;
            }
            return;
        }
        if ((shader instanceof ComposeShader) && shader != null && shader.mShaderExt != null && (colors2 = shader.mShaderExt.getComposeShaderColor()) != null && colors2.size() == 2) {
            int transformType2 = changeAreaTransformType(rectF, canvas, canvas != null ? canvas.getTransformType() : 2);
            List<long[]> newColors2 = new ArrayList<>(colors2);
            for (int i = 0; i < newColors2.size(); i++) {
                getShaderDarkModeColors(newColors2.get(i), transformType2);
            }
            shader.mShaderExt.setComposeShaderColor(newColors2);
        }
    }

    public Paint changePaintWhenDrawBitmap(Paint paint, Bitmap bitmap, RectF rectF, IBaseCanvasExt canvas) {
        return changePaintWhenDrawBitmap(paint, bitmap, rectF, false, canvas);
    }

    public Paint changePaintWhenDrawPatch(NinePatch ninePatch, Paint paint, RectF rectF, IBaseCanvasExt canvas) {
        return changePaintWhenDrawBitmap(paint, ninePatch.getBitmap(), rectF, true, canvas);
    }

    public void changeColorFilterInDarkMode(ColorFilter colorFilter) {
        makeColorFilterColor(colorFilter, 2);
    }

    private boolean makeColorFilterColor(ColorFilter colorFilter, int transformType) {
        return makeColorFilterColor(colorFilter, transformType, null);
    }

    private boolean makeColorFilterColor(ColorFilter colorFilter, int transformType, IBaseCanvasExt canvas) {
        boolean shouldInvert = false;
        int originColor = -1;
        if (colorFilter instanceof PorterDuffColorFilter) {
            originColor = ((PorterDuffColorFilter) colorFilter).getColor();
            shouldInvert = true;
        } else if (colorFilter instanceof BlendModeColorFilter) {
            originColor = ((BlendModeColorFilter) colorFilter).getColor();
            shouldInvert = true;
        }
        if (shouldInvert) {
            int newColor = originColor;
            if (canvas != null) {
                int viewType = canvas.getOplusViewType();
                switch (viewType) {
                    case 1:
                        if (transformType == 1 && OplusDarkModeUtils.isMaybeBlackColor(originColor)) {
                            transformType = 2;
                            break;
                        }
                        break;
                }
            }
            switch (transformType) {
                case 1:
                    newColor = OplusDarkModeUtils.makeSVGColorBackground(originColor, this.mBackgroundMaxL);
                    break;
                case 2:
                    newColor = OplusDarkModeUtils.makeSVGColorForeground(originColor, this.mForegroundMinL);
                    break;
            }
            if (newColor != originColor) {
                OplusDarkModeUtils.setColorFilterColor(colorFilter, newColor);
            }
        }
        return shouldInvert;
    }

    public void getShaderDarkModeColors(long[] colors, int transformType) {
        if (colors == null) {
            return;
        }
        int size = colors.length;
        for (int i = 0; i < size; i++) {
            long color = colors[i];
            colors[i] = Color.pack(getDarkModeColor(Color.argb(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)), transformType));
        }
    }

    public int[] getDarkModeColors(int[] colors, IBaseCanvasExt canvas) {
        if (colors == null) {
            return null;
        }
        int[] darkModeColor = (int[]) colors.clone();
        for (int i = 0; i < colors.length; i++) {
            darkModeColor[i] = getDarkModeColor(darkModeColor[i], canvas != null ? canvas.getTransformType() : 2);
        }
        return darkModeColor;
    }

    private int getDarkModeColor(int color, int transformType) {
        switch (transformType) {
            case 1:
                return OplusDarkModeUtils.makeColorBackground(color, this.mBackgroundMaxL);
            case 2:
                return OplusDarkModeUtils.makeColorForeground(color, this.mForegroundMinL);
            default:
                return color;
        }
    }

    public IBaseCanvasExt.RealPaintState getRealPaintState(Paint paint) {
        if (paint == null) {
            return null;
        }
        IBaseCanvasExt.RealPaintState realPaintState = new IBaseCanvasExt.RealPaintState();
        realPaintState.color = paint.getColor();
        realPaintState.colorFilter = paint.getColorFilter();
        Shader shader = paint.getShader();
        if ((shader instanceof LinearGradient) || (shader instanceof SweepGradient) || (shader instanceof RadialGradient)) {
            if (shader != null && shader.mShaderExt != null) {
                realPaintState.shaderColors = shader.mShaderExt.getColorLongs();
            }
        } else if ((shader instanceof ComposeShader) && shader != null && shader.mShaderExt != null) {
            realPaintState.composeShaderColors = shader.mShaderExt.getComposeShaderColor();
        }
        if (paint.getColorFilter() instanceof PorterDuffColorFilter) {
            realPaintState.colorFilterColor = ((PorterDuffColorFilter) paint.getColorFilter()).getColor();
        } else if (paint.getColorFilter() instanceof BlendModeColorFilter) {
            realPaintState.colorFilterColor = ((BlendModeColorFilter) paint.getColorFilter()).getColor();
        }
        return realPaintState;
    }

    public void resetRealPaintIfNeed(Paint paint, IBaseCanvasExt.RealPaintState realPaintState) {
        if (paint == null || realPaintState == null) {
            return;
        }
        paint.setColor(realPaintState.color);
        paint.setColorFilter(realPaintState.colorFilter);
        Shader shader = paint.getShader();
        if ((shader instanceof LinearGradient) || (shader instanceof SweepGradient) || (shader instanceof RadialGradient)) {
            if (shader != null && shader.mShaderExt != null && realPaintState.shaderColors != null) {
                shader.mShaderExt.setColors(realPaintState.shaderColors);
            }
        } else if ((shader instanceof ComposeShader) && shader != null && shader.mShaderExt != null && realPaintState.composeShaderColors != null) {
            shader.mShaderExt.setComposeShaderColor(realPaintState.composeShaderColors);
        }
        if (paint.getColorFilter() instanceof PorterDuffColorFilter) {
            OplusDarkModeUtils.setColorFilterColor(paint.getColorFilter(), realPaintState.colorFilterColor);
        } else if (paint.getColorFilter() instanceof BlendModeColorFilter) {
            OplusDarkModeUtils.setColorFilterColor(paint.getColorFilter(), realPaintState.colorFilterColor);
        }
    }

    private boolean isCrudeView(View view, Canvas canvas) {
        if (OplusDarkModeUtils.getCrudeState(view) == 1) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        return viewGroup.getChildCount() > 0;
    }

    public void markBackground(View view, Canvas canvas) {
        if (this.mIsAppSupportDarkMode) {
            OplusDarkModeUtils.setViewType(canvas, OplusDarkModeUtils.getViewType(view));
            OplusDarkModeUtils.setCanvasTransformType(canvas, 1, view.getWidth(), view.getHeight());
        }
    }

    private void markCrude(View view, Canvas canvas) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            this.mBounds.setEmpty();
            this.mDrawnBounds.setEmpty();
            if (childCount > 1) {
                for (int i = childCount - 1; i >= 0; i--) {
                    View child = viewGroup.getChildAt(i);
                    this.mBounds.set(child.getX(), child.getY(), child.getX() + child.getWidth(), child.getY() + child.getHeight());
                    if (!this.mDrawnBounds.isEmpty() && this.mBounds.contains(this.mDrawnBounds)) {
                        OplusDarkModeUtils.setCrudeState(child, 1);
                    }
                    this.mDrawnBounds.union(this.mBounds);
                }
            }
        }
    }

    public void markDispatchDraw(ViewGroup viewGroup, Canvas canvas) {
        if (this.mIsAppSupportDarkMode) {
            markCrude(viewGroup, canvas);
        }
    }

    public void markViewTypeBySize(View view) {
        if (!this.mIsAppSupportDarkMode) {
            return;
        }
        float density = OplusDarkModeUtils.getDpDensity(view);
        float screenWidth = OplusDarkModeUtils.getScreenWidth(view);
        int viewHeight = view.getHeight();
        int viewWidth = view.getWidth();
        if (viewHeight <= 80.0f * density && viewWidth <= screenWidth / 2.0f) {
            OplusDarkModeUtils.setViewType(view, 1);
        } else if (viewWidth >= screenWidth * 0.8d) {
            if (viewHeight >= 8.0f * density) {
                OplusDarkModeUtils.setViewType(view, 2);
            } else {
                OplusDarkModeUtils.setViewType(view, 3);
            }
        }
    }

    public void markDrawChild(ViewGroup viewGroup, View view, Canvas canvas) {
        if (this.mIsAppSupportDarkMode) {
            OplusDarkModeUtils.setViewType(canvas, OplusDarkModeUtils.getViewType(view));
            OplusDarkModeUtils.setCanvasTransformType(canvas, 1, view.getWidth(), view.getHeight());
        }
    }

    public void markForeground(View view, Canvas canvas) {
        if (this.mIsAppSupportDarkMode) {
            OplusDarkModeUtils.setViewType(canvas, OplusDarkModeUtils.getViewType(view));
            OplusDarkModeUtils.setCanvasTransformType(canvas, isCrudeView(view, canvas) ? 1 : 2, view.getWidth(), view.getHeight());
        }
    }

    public void markOnDraw(View view, Canvas canvas) {
        markForeground(view, canvas);
    }

    public void markDrawFadingEdge(View view, Canvas canvas) {
        if (this.mIsAppSupportDarkMode) {
            OplusDarkModeUtils.setCanvasTransformType(canvas, 1, view.getWidth(), view.getHeight());
        }
    }

    public ColorFilter getColorFilterWhenDrawVectorDrawable(SumEntity HEntity, SumEntity SEntity, SumEntity LEntity) {
        if (LEntity.count() > 0 && HEntity.delta() <= 20.0f && SEntity.delta() <= 0.1f && LEntity.average() <= 0.5f) {
            return INVERT_FILTER;
        }
        return null;
    }
}
