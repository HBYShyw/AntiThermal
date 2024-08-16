package androidx.recyclerview.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: DividerItemDecoration.java */
/* renamed from: androidx.recyclerview.widget.f, reason: use source file name */
/* loaded from: classes.dex */
public class DividerItemDecoration extends RecyclerView.o {

    /* renamed from: d, reason: collision with root package name */
    private static final int[] f3728d = {R.attr.listDivider};

    /* renamed from: a, reason: collision with root package name */
    private Drawable f3729a;

    /* renamed from: b, reason: collision with root package name */
    private int f3730b;

    /* renamed from: c, reason: collision with root package name */
    private final Rect f3731c = new Rect();

    public DividerItemDecoration(Context context, int i10) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(f3728d);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        this.f3729a = drawable;
        if (drawable == null) {
            Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        obtainStyledAttributes.recycle();
        m(i10);
    }

    private void j(Canvas canvas, RecyclerView recyclerView) {
        int height;
        int i10;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            i10 = recyclerView.getPaddingTop();
            height = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            canvas.clipRect(recyclerView.getPaddingLeft(), i10, recyclerView.getWidth() - recyclerView.getPaddingRight(), height);
        } else {
            height = recyclerView.getHeight();
            i10 = 0;
        }
        int childCount = recyclerView.getChildCount();
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = recyclerView.getChildAt(i11);
            recyclerView.getLayoutManager().P(childAt, this.f3731c);
            int round = this.f3731c.right + Math.round(childAt.getTranslationX());
            this.f3729a.setBounds(round - this.f3729a.getIntrinsicWidth(), i10, round, height);
            this.f3729a.draw(canvas);
        }
        canvas.restore();
    }

    private void k(Canvas canvas, RecyclerView recyclerView) {
        int width;
        int i10;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            i10 = recyclerView.getPaddingLeft();
            width = recyclerView.getWidth() - recyclerView.getPaddingRight();
            canvas.clipRect(i10, recyclerView.getPaddingTop(), width, recyclerView.getHeight() - recyclerView.getPaddingBottom());
        } else {
            width = recyclerView.getWidth();
            i10 = 0;
        }
        int childCount = recyclerView.getChildCount();
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = recyclerView.getChildAt(i11);
            recyclerView.getDecoratedBoundsWithMargins(childAt, this.f3731c);
            int round = this.f3731c.bottom + Math.round(childAt.getTranslationY());
            this.f3729a.setBounds(i10, round - this.f3729a.getIntrinsicHeight(), width, round);
            this.f3729a.draw(canvas);
        }
        canvas.restore();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void e(Rect rect, View view, RecyclerView recyclerView, RecyclerView.z zVar) {
        Drawable drawable = this.f3729a;
        if (drawable == null) {
            rect.set(0, 0, 0, 0);
        } else if (this.f3730b == 1) {
            rect.set(0, 0, 0, drawable.getIntrinsicHeight());
        } else {
            rect.set(0, 0, drawable.getIntrinsicWidth(), 0);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void g(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
        if (recyclerView.getLayoutManager() == null || this.f3729a == null) {
            return;
        }
        if (this.f3730b == 1) {
            k(canvas, recyclerView);
        } else {
            j(canvas, recyclerView);
        }
    }

    public void l(Drawable drawable) {
        if (drawable != null) {
            this.f3729a = drawable;
            return;
        }
        throw new IllegalArgumentException("Drawable cannot be null.");
    }

    public void m(int i10) {
        if (i10 != 0 && i10 != 1) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        this.f3730b = i10;
    }
}
