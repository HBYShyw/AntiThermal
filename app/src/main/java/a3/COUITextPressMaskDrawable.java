package a3;

import android.graphics.Canvas;
import android.graphics.Rect;
import m2.COUIRoundDrawable;

/* compiled from: COUITextPressMaskDrawable.java */
/* renamed from: a3.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUITextPressMaskDrawable extends COUIRoundDrawable {
    @Override // m2.COUIRoundDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        j(Math.min(bounds.right - bounds.left, bounds.bottom - bounds.top) / 2.0f);
        super.draw(canvas);
    }
}
