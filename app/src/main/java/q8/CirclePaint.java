package q8;

import android.content.Context;
import android.graphics.Paint;
import com.oplus.battery.R;

/* compiled from: CirclePaint.java */
/* renamed from: q8.a, reason: use source file name */
/* loaded from: classes2.dex */
public class CirclePaint extends Paint {
    public CirclePaint(Context context) {
        setStrokeWidth(context.getResources().getDimension(R.dimen.usage_graph_tips_circle_radius));
    }
}
