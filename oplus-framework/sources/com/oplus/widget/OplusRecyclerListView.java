package com.oplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import com.android.internal.app.AlertController;

/* loaded from: classes.dex */
public class OplusRecyclerListView extends AlertController.RecycleListView {
    private Path mClipPath;
    private int mCornerRadius;
    private boolean mNeedClip;

    public OplusRecyclerListView(Context context) {
        this(context, null);
    }

    public OplusRecyclerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCornerRadius = context.getResources().getDimensionPixelOffset(201654310);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        if (this.mNeedClip) {
            clipRoundBounds(canvas);
        }
        super.draw(canvas);
        canvas.restore();
    }

    private void clipRoundBounds(Canvas canvas) {
        int i = this.mCornerRadius;
        float[] cornerRadius = {i, i, i, i, 0.0f, 0.0f, 0.0f, 0.0f};
        if (this.mClipPath == null) {
            Path path = new Path();
            this.mClipPath = path;
            path.addRoundRect(getLeft(), getTop(), getRight(), getBottom(), cornerRadius, Path.Direction.CW);
        }
        canvas.clipPath(this.mClipPath);
    }

    public void setNeedClip(boolean needClip) {
        this.mNeedClip = needClip;
    }
}
