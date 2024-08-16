package com.coui.appcompat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.n0;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.support.appcompat.R$color;
import java.util.Arrays;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIResponsiveGridMaskView extends View {

    /* renamed from: e, reason: collision with root package name */
    private int f8051e;

    /* renamed from: f, reason: collision with root package name */
    private int[] f8052f;

    /* renamed from: g, reason: collision with root package name */
    private int f8053g;

    /* renamed from: h, reason: collision with root package name */
    private int f8054h;

    /* renamed from: i, reason: collision with root package name */
    private MarginType f8055i;

    /* renamed from: j, reason: collision with root package name */
    private final Rect f8056j;

    /* renamed from: k, reason: collision with root package name */
    private final Rect f8057k;

    /* renamed from: l, reason: collision with root package name */
    private final Paint f8058l;

    /* renamed from: m, reason: collision with root package name */
    private final Paint f8059m;

    /* renamed from: n, reason: collision with root package name */
    private ResponsiveUIModel f8060n;

    /* renamed from: o, reason: collision with root package name */
    private Context f8061o;

    public COUIResponsiveGridMaskView(Context context) {
        super(context);
        this.f8051e = 0;
        this.f8053g = 0;
        this.f8054h = 0;
        this.f8055i = MarginType.MARGIN_SMALL;
        this.f8056j = new Rect();
        this.f8057k = new Rect();
        this.f8058l = new Paint();
        this.f8059m = new Paint();
        a(context);
    }

    private void a(Context context) {
        this.f8061o = context;
        this.f8060n = new ResponsiveUIModel(context, 0, 0);
        b();
        this.f8058l.setColor(COUIContextUtil.d(context, R$color.responsive_ui_column_hint_margin));
        this.f8059m.setColor(COUIContextUtil.d(context, R$color.responsive_ui_column_hint_column));
    }

    private void b() {
        this.f8060n.chooseMargin(this.f8055i);
        this.f8051e = this.f8060n.columnCount();
        this.f8052f = this.f8060n.columnWidth();
        this.f8053g = this.f8060n.gutter();
        this.f8054h = this.f8060n.margin();
        int i10 = 0;
        for (int i11 : this.f8052f) {
            Log.d("COUIResponsiveGridMaskView", "requestLatestGridParams: " + i11);
            i10 += i11;
        }
        Log.d("COUIResponsiveGridMaskView", "requestLatestGridParams: \ngetMeasureWidth() = " + getMeasuredWidth() + "\nmMargin = " + this.f8054h + "\nmGutter = " + this.f8053g + "\nmColumnWidth = " + Arrays.toString(this.f8052f) + "\nmColumnCount = " + this.f8051e + "\nsum(columnWidth) = " + i10 + "\ntotal = (mMargin * 2) + (mColumnWidth * mColumnCount) + (mGutter * (mColumnCount - 1)) = " + ((this.f8054h * 2) + i10 + (this.f8053g * (this.f8051e - 1))));
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        this.f8061o = null;
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (n0.b(this)) {
            int measuredWidth = getMeasuredWidth();
            Log.d("COUIResponsiveGridMaskView", "onDraw: total" + getMeasuredWidth());
            this.f8056j.set(measuredWidth, 0, measuredWidth - ((int) (((float) this.f8054h) + 0.0f)), getHeight());
            canvas.drawRect(this.f8056j, this.f8058l);
            Log.d("COUIResponsiveGridMaskView", "onDraw: right margin:0.0 - " + (this.f8054h + 0.0f));
            float f10 = ((float) this.f8054h) + 0.0f;
            int i10 = 0;
            while (i10 < this.f8051e) {
                this.f8057k.set(measuredWidth - ((int) f10), 0, measuredWidth - ((int) (this.f8052f[i10] + f10)), getHeight());
                canvas.drawRect(this.f8057k, this.f8059m);
                Log.d("COUIResponsiveGridMaskView", "onDraw: column:" + f10 + " - " + (this.f8052f[i10] + f10));
                if (i10 != this.f8051e - 1) {
                    Log.d("COUIResponsiveGridMaskView", "onDraw: gap:" + (this.f8052f[i10] + f10) + " - " + (this.f8052f[i10] + f10 + this.f8053g));
                }
                f10 += this.f8052f[i10] + (i10 == this.f8051e + (-1) ? 0 : this.f8053g);
                i10++;
            }
            this.f8056j.set(measuredWidth - ((int) f10), 0, measuredWidth - ((int) (this.f8054h + f10)), getHeight());
            canvas.drawRect(this.f8056j, this.f8058l);
            Log.d("COUIResponsiveGridMaskView", "onDraw: left margin:" + f10 + " - " + (this.f8054h + f10));
            return;
        }
        Log.d("COUIResponsiveGridMaskView", "onDraw: total width: " + getMeasuredWidth());
        this.f8056j.set(0, 0, (int) (((float) this.f8054h) + 0.0f), getHeight());
        canvas.drawRect(this.f8056j, this.f8058l);
        Log.d("COUIResponsiveGridMaskView", "onDraw: left margin: 0.0 - " + (this.f8054h + 0.0f) + " width: " + this.f8054h);
        float f11 = ((float) this.f8054h) + 0.0f;
        int i11 = 0;
        while (i11 < this.f8051e) {
            this.f8057k.set((int) f11, 0, (int) (this.f8052f[i11] + f11), getHeight());
            canvas.drawRect(this.f8057k, this.f8059m);
            Log.d("COUIResponsiveGridMaskView", "onDraw: column " + i11 + " :" + f11 + " - " + (this.f8052f[i11] + f11) + " width: " + this.f8052f[i11]);
            if (i11 != this.f8051e - 1) {
                Log.d("COUIResponsiveGridMaskView", "onDraw: gap " + i11 + " :" + (this.f8052f[i11] + f11) + " - " + (this.f8052f[i11] + f11 + this.f8053g) + " width: " + this.f8053g);
            }
            f11 += this.f8052f[i11] + (i11 == this.f8051e + (-1) ? 0 : this.f8053g);
            i11++;
        }
        this.f8056j.set((int) f11, 0, (int) (this.f8054h + f11), getHeight());
        canvas.drawRect(this.f8056j, this.f8058l);
        Log.d("COUIResponsiveGridMaskView", "onDraw: right margin:" + f11 + " - " + (this.f8054h + f11) + " width:" + this.f8054h);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        super.onMeasure(i10, i11);
        this.f8060n.rebuild(getMeasuredWidth(), getMeasuredHeight());
        b();
    }

    public void setMarginType(MarginType marginType) {
        this.f8055i = marginType;
    }

    public COUIResponsiveGridMaskView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f8051e = 0;
        this.f8053g = 0;
        this.f8054h = 0;
        this.f8055i = MarginType.MARGIN_SMALL;
        this.f8056j = new Rect();
        this.f8057k = new Rect();
        this.f8058l = new Paint();
        this.f8059m = new Paint();
        a(context);
    }

    public COUIResponsiveGridMaskView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f8051e = 0;
        this.f8053g = 0;
        this.f8054h = 0;
        this.f8055i = MarginType.MARGIN_SMALL;
        this.f8056j = new Rect();
        this.f8057k = new Rect();
        this.f8058l = new Paint();
        this.f8059m = new Paint();
        a(context);
    }
}
