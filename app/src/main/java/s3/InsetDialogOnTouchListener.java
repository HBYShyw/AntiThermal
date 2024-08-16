package s3;

import android.R;
import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/* compiled from: InsetDialogOnTouchListener.java */
/* renamed from: s3.a, reason: use source file name */
/* loaded from: classes.dex */
public class InsetDialogOnTouchListener implements View.OnTouchListener {

    /* renamed from: e, reason: collision with root package name */
    private final Dialog f18039e;

    /* renamed from: f, reason: collision with root package name */
    private final int f18040f;

    /* renamed from: g, reason: collision with root package name */
    private final int f18041g;

    /* renamed from: h, reason: collision with root package name */
    private final int f18042h;

    public InsetDialogOnTouchListener(Dialog dialog, Rect rect) {
        this.f18039e = dialog;
        this.f18040f = rect.left;
        this.f18041g = rect.top;
        this.f18042h = ViewConfiguration.get(dialog.getContext()).getScaledWindowTouchSlop();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View findViewById = view.findViewById(R.id.content);
        int left = this.f18040f + findViewById.getLeft();
        int width = findViewById.getWidth() + left;
        if (new RectF(left, this.f18041g + findViewById.getTop(), width, findViewById.getHeight() + r3).contains(motionEvent.getX(), motionEvent.getY())) {
            return false;
        }
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        if (motionEvent.getAction() == 1) {
            obtain.setAction(4);
        }
        view.performClick();
        return this.f18039e.onTouchEvent(obtain);
    }
}
