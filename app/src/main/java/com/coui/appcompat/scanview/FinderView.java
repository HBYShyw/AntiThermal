package com.coui.appcompat.scanview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import c.AppCompatResources;
import com.support.component.R$dimen;
import com.support.component.R$drawable;
import kotlin.Metadata;
import za.k;

/* compiled from: FinderView.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001fB\u000f\u0012\u0006\u0010\u001b\u001a\u00020\u001a¢\u0006\u0004\b\u001c\u0010\u001dJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0014J\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0016R\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\f\u0010\rR\u0016\u0010\u0011\u001a\u00020\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0013\u001a\u00020\u00048\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0012\u0010\u0010R\u0016\u0010\u0017\u001a\u00020\u00148\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0015\u0010\u0016R\u0016\u0010\u0019\u001a\u00020\u00148\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0018\u0010\u0016¨\u0006 "}, d2 = {"Lcom/coui/appcompat/scanview/FinderView;", "Landroid/widget/FrameLayout;", "Landroid/view/View;", "changedView", "", "visibility", "Lma/f0;", "onVisibilityChanged", "Landroid/graphics/Canvas;", "canvas", "onDraw", "Landroid/graphics/drawable/Drawable;", "e", "Landroid/graphics/drawable/Drawable;", "scanDrawable", "f", "I", "scanLineHeight", "g", "scannerTop", "", "h", "Z", "canDraw", "i", "keepDrawing", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "j", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class FinderView extends FrameLayout {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private Drawable scanDrawable;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private int scanLineHeight;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata */
    private int scannerTop;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata */
    private boolean canDraw;

    /* renamed from: i, reason: collision with root package name and from kotlin metadata */
    private boolean keepDrawing;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FinderView(Context context) {
        super(context);
        k.e(context, "context");
        this.scannerTop = -60;
        this.keepDrawing = true;
        setWillNotDraw(false);
        this.canDraw = false;
        this.scanLineHeight = getResources().getDimensionPixelSize(R$dimen.coui_component_scan_line_height);
        this.scanDrawable = AppCompatResources.b(context, R$drawable.coui_component_barcode_scan_line);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        k.e(canvas, "canvas");
        if (this.canDraw) {
            int i10 = this.scannerTop + 10;
            this.scannerTop = i10;
            if (i10 > getMeasuredHeight() - this.scanLineHeight) {
                this.scannerTop = 0;
            }
            Drawable drawable = this.scanDrawable;
            if (drawable != null) {
                drawable.setBounds(0, this.scannerTop, getMeasuredWidth(), this.scannerTop + this.scanLineHeight);
            }
            Drawable drawable2 = this.scanDrawable;
            if (drawable2 != null) {
                drawable2.draw(canvas);
            }
            if (this.keepDrawing) {
                postInvalidate();
            }
        }
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i10) {
        k.e(view, "changedView");
        this.canDraw = i10 == 0;
        super.onVisibilityChanged(view, i10);
    }
}
