package android.view.autolayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class AutoLayoutCanvas {
    private static final int SAMPLE_POSITION_BOTTOM = 3;
    private static final int SAMPLE_POSITION_CENTER = 2;
    private static final int SAMPLE_POSITION_LEFT = 4;
    private static final int SAMPLE_POSITION_RIGHT = 5;
    private static final int SAMPLE_POSITION_TOP = 1;
    private static final int SCALE_DIRECTION_HORIZONTAL = 2;
    private static final int SCALE_DIRECTION_NONE = 0;
    private static final int SCALE_DIRECTION_VERTICAL = 1;
    private static final int SCALE_LIMIT = 8;
    private static Canvas sCanvas;
    private static AutoLayoutCanvas sInstance;
    private View mView;

    private AutoLayoutCanvas() {
    }

    public static AutoLayoutCanvas getInstance(Canvas canvas) {
        if (sInstance == null) {
            sInstance = new AutoLayoutCanvas();
        }
        sCanvas = canvas;
        return sInstance;
    }

    public static AutoLayoutCanvas getInstance() {
        if (sInstance == null) {
            sInstance = new AutoLayoutCanvas();
        }
        return sInstance;
    }

    public final void setView(View view) {
        this.mView = view;
    }

    public final void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        AutoLayoutDebug.log(this.mView + " drawBitmap with left " + left + " top " + top);
        scaleBitmap(bitmap, null, null);
    }

    public final void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        AutoLayoutDebug.log(this.mView + " drawBitmap with matrixs " + matrix);
        matrix.reset();
        scaleBitmap(bitmap, null, null);
    }

    public final Rect drawBitmap(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        AutoLayoutDebug.log(this.mView + " drawBitmap with src " + src + " dst " + dst);
        return scaleBitmap(bitmap, src, dst);
    }

    public final void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {
    }

    public final void start() {
        sCanvas.save();
    }

    public final void end() {
        sCanvas.restore();
    }

    private Rect scaleBitmap(Bitmap bitmap, Rect src, Rect dst) {
        if (!shouldRedrawBitmap(bitmap, dst)) {
            return dst;
        }
        AutoLayoutDebug.appendExtraInfo(this.mView, "bitmap " + bitmap + " " + SystemClock.elapsedRealtime());
        int direction = getBitmapScaleDirection(bitmap, dst);
        if (direction == 0) {
            AutoLayoutDebug.appendExtraInfo(this.mView, "scale type none");
            return dst;
        }
        Rect newRect = new Rect();
        try {
            sCanvas.setMatrix(new Matrix());
            drawGradientBackground(bitmap, src, dst, direction);
            Rect newRect2 = transformCanvasAndDraw(bitmap, src, dst, direction);
            return newRect2;
        } catch (Exception e) {
            AutoLayoutDebug.logE(e);
            return newRect;
        }
    }

    private void drawGradientBackground(Bitmap bitmap, Rect src, Rect dst, int scaleDirection) throws Exception {
        if (scaleDirection == 1) {
            drawTopGradientBackground(bitmap, src, dst, scaleDirection);
            drawBottomGradientBackground(bitmap, src, dst, scaleDirection);
        } else {
            drawLeftGradientBackground(bitmap, src, dst, scaleDirection);
            drawRightGradientBackground(bitmap, src, dst, scaleDirection);
        }
    }

    private void drawLeftGradientBackground(Bitmap bitmap, Rect src, Rect dst, int scaleDirection) throws Exception {
        int[] iArr = new int[5];
        int[] topPixels = sampleBitmapHorizontal(bitmap, 1, 4);
        int[] iArr2 = new int[5];
        int[] centerPixels = sampleBitmapHorizontal(bitmap, 2, 4);
        int[] iArr3 = new int[5];
        int[] bottomPixels = sampleBitmapHorizontal(bitmap, 3, 4);
        int averageTopPixel = calAveragePixel(topPixels);
        calAveragePixel(centerPixels);
        int averageBottomPixel = calAveragePixel(bottomPixels);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, this.mView.getMeasuredHeight(), averageTopPixel, averageBottomPixel, Shader.TileMode.CLAMP);
        Paint backgroundPaint = new Paint();
        backgroundPaint.setShader(linearGradient);
        int drawEndX = this.mView.getMeasuredWidth() / 2;
        sCanvas.drawRect(new Rect(0, 0, drawEndX, this.mView.getMeasuredHeight()), backgroundPaint);
    }

    private void drawRightGradientBackground(Bitmap bitmap, Rect src, Rect dst, int scaleDirection) throws Exception {
        int[] iArr = new int[5];
        int[] topPixels = sampleBitmapHorizontal(bitmap, 1, 5);
        int[] iArr2 = new int[5];
        int[] centerPixels = sampleBitmapHorizontal(bitmap, 2, 5);
        int[] iArr3 = new int[5];
        int[] bottomPixels = sampleBitmapHorizontal(bitmap, 3, 5);
        int averageTopPixel = calAveragePixel(topPixels);
        calAveragePixel(centerPixels);
        int averageBottomPixel = calAveragePixel(bottomPixels);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, this.mView.getMeasuredHeight(), averageTopPixel, averageBottomPixel, Shader.TileMode.CLAMP);
        Paint backgroundPaint = new Paint();
        backgroundPaint.setShader(linearGradient);
        int drawStartX = this.mView.getMeasuredWidth() / 2;
        int drawEndX = this.mView.getMeasuredWidth();
        sCanvas.drawRect(new Rect(drawStartX, 0, drawEndX, this.mView.getMeasuredHeight()), backgroundPaint);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0053, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int[] sampleBitmapHorizontal(Bitmap bitmap, int positionY, int positionX) throws Exception {
        int[] pixels = new int[5];
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int sampleY = 0;
        int sampleStep = Math.min(2, bitmapWidth / 20);
        switch (positionY) {
            case 1:
                sampleY = (int) (bitmapHeight * 0.15f);
                break;
            case 2:
                sampleY = (int) (bitmapHeight * 0.5f);
                break;
            case 3:
                sampleY = (int) (bitmapHeight * 0.85f);
                break;
        }
        switch (positionX) {
            case 4:
                for (int i = 0; i < 5; i++) {
                    pixels[i] = bitmap.getPixel((i * sampleStep) + 2, sampleY);
                }
                break;
            case 5:
                for (int i2 = 0; i2 < 5; i2++) {
                    pixels[i2] = bitmap.getPixel((bitmapWidth - (i2 * sampleStep)) - 2, sampleY);
                }
                break;
        }
    }

    private void drawTopGradientBackground(Bitmap bitmap, Rect src, Rect dst, int scaleDirection) throws Exception {
        int[] leftPixels = sampleBitmapVertical(bitmap, 1, 4);
        int[] rightPixels = sampleBitmapVertical(bitmap, 1, 5);
        int averageLeftPixel = calAveragePixel(leftPixels);
        int averageRightPixel = calAveragePixel(rightPixels);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, this.mView.getMeasuredWidth(), 0.0f, averageLeftPixel, averageRightPixel, Shader.TileMode.CLAMP);
        Paint backgroundPaint = new Paint();
        backgroundPaint.setShader(linearGradient);
        int drawEndY = this.mView.getMeasuredHeight() / 2;
        sCanvas.drawRect(new Rect(0, 0, this.mView.getMeasuredWidth(), drawEndY), backgroundPaint);
    }

    private void drawBottomGradientBackground(Bitmap bitmap, Rect src, Rect dst, int scaleDirection) throws Exception {
        int[] leftPixels = sampleBitmapVertical(bitmap, 3, 4);
        int[] rightPixels = sampleBitmapVertical(bitmap, 3, 5);
        int averageLeftPixel = calAveragePixel(leftPixels);
        int averageRightPixel = calAveragePixel(rightPixels);
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, this.mView.getMeasuredWidth(), 0.0f, averageLeftPixel, averageRightPixel, Shader.TileMode.CLAMP);
        Paint backgroundPaint = new Paint();
        backgroundPaint.setShader(linearGradient);
        int drawStartY = this.mView.getMeasuredHeight() / 2;
        int drawEndY = this.mView.getMeasuredHeight();
        sCanvas.drawRect(new Rect(0, drawStartY, this.mView.getMeasuredWidth(), drawEndY), backgroundPaint);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004d, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int[] sampleBitmapVertical(Bitmap bitmap, int positionY, int positionX) throws Exception {
        int[] pixels = new int[5];
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int sampleX = 0;
        int sampleStep = Math.min(2, bitmapHeight / 20);
        switch (positionX) {
            case 4:
                sampleX = (int) (bitmapWidth * 0.15f);
                break;
            case 5:
                sampleX = (int) (bitmapWidth * 0.85f);
                break;
        }
        switch (positionY) {
            case 1:
                for (int i = 0; i < 5; i++) {
                    pixels[i] = bitmap.getPixel(sampleX, (i * sampleStep) + 2);
                }
                break;
            case 3:
                for (int i2 = 0; i2 < 5; i2++) {
                    pixels[i2] = bitmap.getPixel(sampleX, (bitmapHeight - (i2 * sampleStep)) - 2);
                }
                break;
        }
    }

    private int calAveragePixel(int[] pixels) throws Exception {
        int length = pixels.length;
        float allAlpha = 0.0f;
        float allRed = 0.0f;
        float allGreen = 0.0f;
        float allBlue = 0.0f;
        for (int singlePixel : pixels) {
            Color color = Color.valueOf(singlePixel);
            allAlpha += color.alpha();
            allRed += color.red();
            allGreen += color.green();
            allBlue += color.blue();
        }
        float averageAlpha = allAlpha / length;
        float averageRed = allRed / length;
        float averageGreen = allGreen / length;
        float averageBlue = allBlue / length;
        Color newColor = Color.valueOf(averageRed, averageGreen, averageBlue, averageAlpha);
        AutoLayoutDebug.log("calAveragePixel averageAlpha " + averageAlpha + " averageRed " + averageRed + " averageGreen " + averageGreen + " averageBlue " + averageBlue);
        return newColor.toArgb();
    }

    private boolean shouldRedrawBitmap(Bitmap bitmap, Rect dst) {
        View view = this.mView;
        if (view == null || sCanvas == null) {
            return false;
        }
        return AutoLayoutUtils.shouldRedrawThisImage(view);
    }

    private boolean isFullWidthBitmap() {
        View view = this.mView;
        if (view == null || sCanvas == null) {
            return false;
        }
        AutoLayoutViewInfo viewInfo = AutoLayoutUtils.getViewInfo(view);
        return viewInfo.getWidthType() == 3;
    }

    private int getBitmapScaleDirection(Bitmap bitmap, Rect dst) {
        ImageView imageView = (ImageView) this.mView;
        float realBitmapRatio = (bitmap.getHeight() * 1.0f) / bitmap.getWidth();
        float realImageViewRatio = (imageView.getMeasuredHeight() * 1.0f) / imageView.getMeasuredWidth();
        float scaleRatioWidth = (imageView.getMeasuredWidth() * 1.0f) / bitmap.getWidth();
        float scaleRatioHeight = (imageView.getMeasuredHeight() * 1.0f) / bitmap.getHeight();
        String scaleInfo = "realBitmapRatio " + realBitmapRatio + " bitmap height " + bitmap.getHeight() + " realImageViewRatio " + realImageViewRatio + " imageView height " + imageView.getMeasuredHeight();
        AutoLayoutDebug.appendExtraInfo(this.mView, scaleInfo);
        if (realBitmapRatio == realImageViewRatio && bitmap.getHeight() == imageView.getHeight()) {
            return 0;
        }
        if (scaleRatioWidth > 8.0f && scaleRatioHeight > 8.0f) {
            return 0;
        }
        if (realBitmapRatio < realImageViewRatio) {
            return 1;
        }
        return 2;
    }

    private Rect transformCanvasAndDraw(Bitmap bitmap, Rect src, Rect dst, int scaleDirection) throws Exception {
        View view = this.mView;
        if (!(view instanceof ImageView)) {
            return dst;
        }
        ImageView imageView = (ImageView) view;
        RectF realBitmapSrc = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        RectF targetBitmapDst = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        float scaleRatio = 1.0f;
        int offset = 0;
        Rect newRect = new Rect();
        if (scaleDirection == 2) {
            targetBitmapDst.bottom = imageView.getMeasuredHeight();
            scaleRatio = imageView.getMeasuredHeight() / realBitmapSrc.height();
            int scaledBitmapWidth = (int) (realBitmapSrc.width() * scaleRatio);
            offset = (imageView.getMeasuredWidth() - scaledBitmapWidth) / 2;
            if (dst != null) {
                newRect.set(offset, 0, offset + scaledBitmapWidth, (int) targetBitmapDst.height());
            } else {
                sCanvas.translate(offset, 0.0f);
                sCanvas.scale(scaleRatio, scaleRatio);
            }
        } else if (scaleDirection == 1) {
            targetBitmapDst.right = imageView.getMeasuredWidth();
            scaleRatio = imageView.getMeasuredWidth() / realBitmapSrc.width();
            int scaledBitmapHeight = (int) (realBitmapSrc.height() * scaleRatio);
            offset = (imageView.getMeasuredHeight() - scaledBitmapHeight) / 2;
            if (dst != null) {
                newRect.set(0, offset, (int) targetBitmapDst.width(), offset + scaledBitmapHeight);
            } else {
                sCanvas.translate(0.0f, offset);
                sCanvas.scale(scaleRatio, scaleRatio);
            }
        }
        String debugStr = "transformCanvas offset = " + offset + " scaleRatio = " + scaleRatio + " realBitmapSrc = " + realBitmapSrc + " scaleDirection = " + scaleDirection;
        AutoLayoutDebug.appendExtraInfo(this.mView, debugStr);
        return newRect;
    }
}
