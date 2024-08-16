package android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class OplusZoomFullBoundView extends ImageView {
    private Context mContext;

    public OplusZoomFullBoundView(Context context) {
        super(context);
        this.mContext = context;
    }

    public OplusZoomFullBoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#2660F5"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dipToPixel(5));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0f, 0.0f, 1080.0f, 2400.0f);
        canvas.drawRoundRect(rectF, dipToPixel(40), dipToPixel(40), paint);
    }

    public int dipToPixel(int dpValue) {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        if (displayMetrics == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(1, dpValue, displayMetrics);
    }
}
