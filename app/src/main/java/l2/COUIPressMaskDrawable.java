package l2;

import android.graphics.Canvas;
import m2.COUIRoundDrawable;

/* compiled from: COUIPressMaskDrawable.java */
/* renamed from: l2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPressMaskDrawable extends COUIRoundDrawable {

    /* renamed from: j, reason: collision with root package name */
    private int f14578j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public COUIPressMaskDrawable(int i10) {
        this.f14578j = i10;
    }

    @Override // m2.COUIRoundDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        j(this.f14578j);
        super.draw(canvas);
    }
}
