package android.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.oplus.util.OplusRoundRectUtil;

/* loaded from: classes.dex */
public class OplusGradientHooksImpl implements IOplusGradientHooks {
    @Override // android.drawable.IOplusGradientHooks
    public void drawRoundRect(boolean smoothRound, Canvas canvas, RectF rect, float rad, boolean haveStroke, Paint fillPaint, Paint strokePaint) {
        if (!smoothRound) {
            canvas.drawRoundRect(rect, rad, rad, fillPaint);
        } else {
            Path path = OplusRoundRectUtil.getInstance().getPath(rect, rad);
            canvas.drawPath(path, fillPaint);
        }
        if (haveStroke) {
            if (!smoothRound) {
                canvas.drawRoundRect(rect, rad, rad, strokePaint);
            } else {
                Path path2 = OplusRoundRectUtil.getInstance().getPath(rect, rad);
                canvas.drawPath(path2, strokePaint);
            }
        }
    }
}
