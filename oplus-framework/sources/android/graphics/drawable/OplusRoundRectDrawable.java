package android.graphics.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/* loaded from: classes.dex */
public class OplusRoundRectDrawable extends Drawable {
    Bitmap mBitmap;
    int mBottom;
    Drawable mDrawable;
    int mLeft;
    Paint mPaint;
    float mRadius;
    RectF mRectF;
    int mRight;
    int mTop;

    public OplusRoundRectDrawable(Drawable drawable, float radius) {
        this(drawable, radius, 0, 0, 1080, 2340);
    }

    public OplusRoundRectDrawable(Drawable drawable, float radius, int left, int top, int right, int bottom) {
        this.mRadius = 0.0f;
        this.mDrawable = drawable;
        this.mRadius = radius;
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;
        if (drawable != null) {
            this.mBitmap = drawableToBitmap(drawable);
            Paint paint = new Paint();
            this.mPaint = paint;
            paint.setAntiAlias(true);
            this.mPaint.setDither(true);
            this.mPaint.setShader(new BitmapShader(this.mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        RectF rectF = this.mRectF;
        if (rectF == null) {
            this.mRectF = new RectF(left, top, right, bottom);
        } else {
            rectF.set(left, top, right, bottom);
        }
    }

    public void setColorRoundBounds(int left, int top, int right, int bottom, float radius) {
        RectF rectF = this.mRectF;
        if (rectF == null) {
            this.mRectF = new RectF(left, top, right, bottom);
        } else {
            rectF.set(left, top, right, bottom);
        }
        this.mRadius = radius;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBitmap.getHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBitmap.getWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        RectF rectF = this.mRectF;
        float f = this.mRadius;
        canvas.drawRoundRect(rectF, f, f, this.mPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    Bitmap drawableToBitmap(Drawable drawable) {
        int w = this.mRight - this.mLeft;
        int h = this.mBottom - this.mTop;
        Bitmap.Config config = drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(this.mLeft, this.mTop, this.mRight, this.mBottom);
        drawable.draw(canvas);
        return bitmap;
    }
}
