package com.coui.appcompat.seekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import com.support.appcompat.R$attr;
import com.support.control.R$dimen;
import v1.COUIContextUtil;

/* compiled from: TextDrawable.java */
/* renamed from: com.coui.appcompat.seekbar.c, reason: use source file name */
/* loaded from: classes.dex */
public class TextDrawable extends ShapeDrawable {

    /* renamed from: a, reason: collision with root package name */
    private Context f7612a;

    /* renamed from: b, reason: collision with root package name */
    private Paint f7613b;

    /* renamed from: c, reason: collision with root package name */
    private String f7614c;

    /* renamed from: d, reason: collision with root package name */
    private final int f7615d;

    /* renamed from: e, reason: collision with root package name */
    private int f7616e;

    /* renamed from: f, reason: collision with root package name */
    private int f7617f;

    /* renamed from: g, reason: collision with root package name */
    private int f7618g;

    /* renamed from: h, reason: collision with root package name */
    private int f7619h;

    public TextDrawable(Context context) {
        super(new RectShape());
        this.f7614c = "";
        this.f7615d = 144;
        this.f7612a = context;
        this.f7618g = context.getResources().getDimensionPixelOffset(R$dimen.coui_seekbar_popup_text_size_small);
        this.f7616e = context.getResources().getDimensionPixelOffset(R$dimen.coui_seekbar_popup_text_height);
        this.f7617f = context.getResources().getDimensionPixelOffset(R$dimen.coui_seekbar_popup_text_margin_bottom);
        this.f7619h = context.getResources().getDimensionPixelOffset(R$dimen.coui_seekbar_popup_text_padding_end);
        Paint paint = new Paint();
        this.f7613b = paint;
        paint.setColor(COUIContextUtil.a(context, R$attr.couiColorPrimaryNeutral));
        this.f7613b.setAntiAlias(true);
        this.f7613b.setStyle(Paint.Style.FILL);
        this.f7613b.setTypeface(Typeface.create("sans-serif-medium", 0));
        if (a()) {
            this.f7613b.setTextAlign(Paint.Align.LEFT);
        } else {
            this.f7613b.setTextAlign(Paint.Align.RIGHT);
        }
        this.f7613b.setStrokeWidth(0.0f);
        getPaint().setColor(0);
    }

    public boolean a() {
        return this.f7612a.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect bounds = getBounds();
        int save = canvas.save();
        canvas.translate(bounds.left, bounds.top);
        this.f7613b.setTextSize(this.f7618g);
        if (a()) {
            canvas.drawText(this.f7614c, this.f7619h, this.f7618g, this.f7613b);
        } else {
            canvas.drawText(this.f7614c, 144 - this.f7619h, this.f7618g, this.f7613b);
        }
        canvas.restoreToCount(save);
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f7616e + this.f7617f;
    }

    @Override // android.graphics.drawable.ShapeDrawable, android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return 144;
    }
}
