package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;

/* loaded from: classes.dex */
public class OplusMagnifierHooksImpl implements IOplusMagnifierHooks {
    private static Bitmap mShadowBitmap;

    @Override // android.widget.IOplusMagnifierHooks
    public int getMagnifierWidth(TypedArray a, Context context) {
        return (int) context.getResources().getDimension(201654405);
    }

    @Override // android.widget.IOplusMagnifierHooks
    public int getMagnifierHeight(TypedArray a, Context context) {
        return (int) context.getResources().getDimension(201654406);
    }

    @Override // android.widget.IOplusMagnifierHooks
    public float getMagnifierCornerRadius(TypedArray a, Context context) {
        return context.getResources().getDimension(201654404);
    }

    @Override // android.widget.IOplusMagnifierHooks
    public void decodeShadowBitmap(Context context) {
        mShadowBitmap = BitmapFactory.decodeResource(context.getResources(), 201850965);
    }

    @Override // android.widget.IOplusMagnifierHooks
    public void recycleShadowBitmap() {
        Bitmap bitmap = mShadowBitmap;
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override // android.widget.IOplusMagnifierHooks
    public void drawShadowBitmap(int contentWidth, int contentHeight, RecordingCanvas canvas, Paint paint) {
        Rect srcRect2 = new Rect(0, 0, mShadowBitmap.getWidth(), mShadowBitmap.getHeight());
        Rect dstRect2 = new Rect(0, 0, contentWidth, contentHeight);
        canvas.drawBitmap(mShadowBitmap, srcRect2, dstRect2, paint);
    }
}
