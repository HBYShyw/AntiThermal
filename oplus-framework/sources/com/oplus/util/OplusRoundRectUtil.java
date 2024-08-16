package com.oplus.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class OplusRoundRectUtil {
    private static OplusRoundRectUtil sInstance;
    private Path mPath = new Path();

    private OplusRoundRectUtil() {
    }

    public static synchronized OplusRoundRectUtil getInstance() {
        OplusRoundRectUtil oplusRoundRectUtil;
        synchronized (OplusRoundRectUtil.class) {
            if (sInstance == null) {
                sInstance = new OplusRoundRectUtil();
            }
            oplusRoundRectUtil = sInstance;
        }
        return oplusRoundRectUtil;
    }

    public Drawable getRoundRectDrawable(int width, int height, int radius, int backgroundColor, int foregroundColor) {
        Rect rect = new Rect(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backgroundColor);
        Path path = getPath(rect, radius);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(foregroundColor);
        canvas.drawPath(path, paint);
        return new BitmapDrawable(bitmap);
    }

    public Path getPath(Rect rect, float radius) {
        return getPath(rect.left, rect.top, rect.right, rect.bottom, radius);
    }

    public Path getPath(RectF rect, float radius) {
        return getPath(rect.left, rect.top, rect.right, rect.bottom, radius);
    }

    public Path getPath(float left, float top, float right, float bottom, float radius) {
        return getPath(left, top, right, bottom, radius, true, true, true, true);
    }

    public synchronized Path getPath(float left, float top, float right, float bottom, float radius, boolean tl, boolean tr, boolean bl, boolean br) {
        float radius2;
        float vertexRatio;
        float controlRatio;
        Path path = this.mPath;
        if (path != null) {
            path.reset();
        } else {
            this.mPath = new Path();
        }
        if (radius >= 0.0f) {
            radius2 = radius;
        } else {
            radius2 = 0.0f;
        }
        float width = right - left;
        float height = bottom - top;
        if (radius2 / Math.min(width / 2.0f, height / 2.0f) > 0.5d) {
            float percentage = ((radius2 / Math.min(width / 2.0f, height / 2.0f)) - 0.5f) / 0.4f;
            float clampedPer = Math.min(1.0f, percentage);
            float percentage2 = 1.0f - (0.13877845f * clampedPer);
            vertexRatio = percentage2;
        } else {
            vertexRatio = 1.0f;
        }
        if (radius2 / Math.min(width / 2.0f, height / 2.0f) > 0.6f) {
            float percentage3 = ((radius2 / Math.min(width / 2.0f, height / 2.0f)) - 0.6f) / 0.3f;
            float clampedPer2 = Math.min(1.0f, percentage3);
            float controlRatio2 = (0.042454004f * clampedPer2) + 1.0f;
            controlRatio = controlRatio2;
        } else {
            controlRatio = 1.0f;
        }
        this.mPath.moveTo((width / 2.0f) + left, top);
        if (tr) {
            this.mPath.lineTo(Math.max(width / 2.0f, width - (((radius2 / 100.0f) * 128.19f) * vertexRatio)) + left, top);
            this.mPath.cubicTo((left + width) - (((radius2 / 100.0f) * 83.62f) * controlRatio), top, (left + width) - ((radius2 / 100.0f) * 67.45f), top + ((radius2 / 100.0f) * 4.64f), (left + width) - ((radius2 / 100.0f) * 51.16f), top + ((radius2 / 100.0f) * 13.36f));
            this.mPath.cubicTo((left + width) - ((radius2 / 100.0f) * 34.86f), top + ((radius2 / 100.0f) * 22.07f), (left + width) - ((radius2 / 100.0f) * 22.07f), top + ((radius2 / 100.0f) * 34.86f), (left + width) - ((radius2 / 100.0f) * 13.36f), top + ((radius2 / 100.0f) * 51.16f));
            this.mPath.cubicTo((left + width) - ((radius2 / 100.0f) * 4.64f), top + ((radius2 / 100.0f) * 67.45f), left + width, top + ((radius2 / 100.0f) * 83.62f * controlRatio), left + width, top + Math.min(height / 2.0f, (radius2 / 100.0f) * 128.19f * vertexRatio));
        } else {
            this.mPath.lineTo(left + width, top);
        }
        if (br) {
            this.mPath.lineTo(left + width, Math.max(height / 2.0f, height - (((radius2 / 100.0f) * 128.19f) * vertexRatio)) + top);
            this.mPath.cubicTo(left + width, (top + height) - (((radius2 / 100.0f) * 83.62f) * controlRatio), (left + width) - ((radius2 / 100.0f) * 4.64f), (top + height) - ((radius2 / 100.0f) * 67.45f), (left + width) - ((radius2 / 100.0f) * 13.36f), (top + height) - ((radius2 / 100.0f) * 51.16f));
            this.mPath.cubicTo((left + width) - ((radius2 / 100.0f) * 22.07f), (top + height) - ((radius2 / 100.0f) * 34.86f), (left + width) - ((radius2 / 100.0f) * 34.86f), (top + height) - ((radius2 / 100.0f) * 22.07f), (left + width) - ((radius2 / 100.0f) * 51.16f), (top + height) - ((radius2 / 100.0f) * 13.36f));
            this.mPath.cubicTo((left + width) - ((radius2 / 100.0f) * 67.45f), (top + height) - ((radius2 / 100.0f) * 4.64f), (left + width) - (((radius2 / 100.0f) * 83.62f) * controlRatio), top + height, left + Math.max(width / 2.0f, width - (((radius2 / 100.0f) * 128.19f) * vertexRatio)), top + height);
        } else {
            this.mPath.lineTo(left + width, top + height);
        }
        if (bl) {
            this.mPath.lineTo(Math.min(width / 2.0f, (radius2 / 100.0f) * 128.19f * vertexRatio) + left, top + height);
            this.mPath.cubicTo(left + ((radius2 / 100.0f) * 83.62f * controlRatio), top + height, left + ((radius2 / 100.0f) * 67.45f), (top + height) - ((radius2 / 100.0f) * 4.64f), left + ((radius2 / 100.0f) * 51.16f), (top + height) - ((radius2 / 100.0f) * 13.36f));
            this.mPath.cubicTo(left + ((radius2 / 100.0f) * 34.86f), (top + height) - ((radius2 / 100.0f) * 22.07f), left + ((radius2 / 100.0f) * 22.07f), (top + height) - ((radius2 / 100.0f) * 34.86f), left + ((radius2 / 100.0f) * 13.36f), (top + height) - ((radius2 / 100.0f) * 51.16f));
            this.mPath.cubicTo(((radius2 / 100.0f) * 4.64f) + left, (top + height) - ((radius2 / 100.0f) * 67.45f), left, (top + height) - (((radius2 / 100.0f) * 83.62f) * controlRatio), left, top + Math.max(height / 2.0f, height - (((radius2 / 100.0f) * 128.19f) * vertexRatio)));
        } else {
            this.mPath.lineTo(left, top + height);
        }
        if (tl) {
            this.mPath.lineTo(left, Math.min(height / 2.0f, (radius2 / 100.0f) * 128.19f * vertexRatio) + top);
            this.mPath.cubicTo(left, top + ((radius2 / 100.0f) * 83.62f * controlRatio), left + ((radius2 / 100.0f) * 4.64f), top + ((radius2 / 100.0f) * 67.45f), left + ((radius2 / 100.0f) * 13.36f), top + ((radius2 / 100.0f) * 51.16f));
            this.mPath.cubicTo(left + ((radius2 / 100.0f) * 22.07f), top + ((radius2 / 100.0f) * 34.86f), left + ((radius2 / 100.0f) * 34.86f), top + ((radius2 / 100.0f) * 22.07f), left + ((radius2 / 100.0f) * 51.16f), top + ((radius2 / 100.0f) * 13.36f));
            this.mPath.cubicTo(((radius2 / 100.0f) * 67.45f) + left, ((radius2 / 100.0f) * 4.64f) + top, ((radius2 / 100.0f) * 83.62f * controlRatio) + left, top, left + Math.min(width / 2.0f, (radius2 / 100.0f) * 128.19f * vertexRatio), top);
        } else {
            this.mPath.lineTo(left, top);
        }
        this.mPath.close();
        return new Path(this.mPath);
    }
}
