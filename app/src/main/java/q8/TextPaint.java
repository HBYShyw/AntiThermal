package q8;

import android.content.Context;
import android.graphics.Paint;
import com.oplus.battery.R;

/* compiled from: TextPaint.java */
/* renamed from: q8.d, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class TextPaint extends Paint {
    public TextPaint(Context context) {
        setStrokeWidth(1.0f);
        setAntiAlias(true);
        setStyle(Paint.Style.FILL);
        setTextSize(context.getResources().getDimension(R.dimen.usage_graph_text_size));
    }
}
