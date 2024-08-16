package q8;

import android.content.Context;
import android.graphics.Paint;

/* compiled from: LinePaint.java */
/* renamed from: q8.b, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class LinePaint extends Paint {
    public LinePaint(Context context) {
        setAntiAlias(true);
        setStyle(Paint.Style.STROKE);
        setStrokeCap(Paint.Cap.ROUND);
        setStrokeJoin(Paint.Join.ROUND);
    }
}
