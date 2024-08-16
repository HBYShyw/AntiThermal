package com.coui.appcompat.menu;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.core.view.ViewCompat;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import g3.COUIViewExplorerByTouchHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import z2.COUIChangeTextUtil;

/* loaded from: classes.dex */
public class COUISupportMenuView extends View {
    static final int[] C = {R.attr.state_enabled};
    static final int[] D = {-16842910};
    static final int[] E = {R.attr.state_pressed, R.attr.state_enabled};
    static final int[] F = {-16842919, R.attr.state_enabled};
    private COUIViewExplorerByTouchHelper A;
    private COUIViewExplorerByTouchHelper.a B;

    /* renamed from: e, reason: collision with root package name */
    private int f6464e;

    /* renamed from: f, reason: collision with root package name */
    private List<COUISupportMenuItem> f6465f;

    /* renamed from: g, reason: collision with root package name */
    private Rect f6466g;

    /* renamed from: h, reason: collision with root package name */
    private int f6467h;

    /* renamed from: i, reason: collision with root package name */
    private int f6468i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f6469j;

    /* renamed from: k, reason: collision with root package name */
    private int f6470k;

    /* renamed from: l, reason: collision with root package name */
    private Paint f6471l;

    /* renamed from: m, reason: collision with root package name */
    private float f6472m;

    /* renamed from: n, reason: collision with root package name */
    private int f6473n;

    /* renamed from: o, reason: collision with root package name */
    private int f6474o;

    /* renamed from: p, reason: collision with root package name */
    private int f6475p;

    /* renamed from: q, reason: collision with root package name */
    private int f6476q;

    /* renamed from: r, reason: collision with root package name */
    private int f6477r;

    /* renamed from: s, reason: collision with root package name */
    private int f6478s;

    /* renamed from: t, reason: collision with root package name */
    private int f6479t;

    /* renamed from: u, reason: collision with root package name */
    private Drawable f6480u;

    /* renamed from: v, reason: collision with root package name */
    private int f6481v;

    /* renamed from: w, reason: collision with root package name */
    private int f6482w;

    /* renamed from: x, reason: collision with root package name */
    private int f6483x;

    /* renamed from: y, reason: collision with root package name */
    private int f6484y;

    /* renamed from: z, reason: collision with root package name */
    private int f6485z;

    /* loaded from: classes.dex */
    class a implements COUIViewExplorerByTouchHelper.a {

        /* renamed from: a, reason: collision with root package name */
        private int f6486a = -1;

        a() {
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public void a(int i10, Rect rect) {
            int i11;
            Paint.FontMetricsInt fontMetricsInt = COUISupportMenuView.this.f6471l.getFontMetricsInt();
            int i12 = (COUISupportMenuView.this.f6481v / 2) + ((COUISupportMenuView.this.f6481v + COUISupportMenuView.this.f6468i) * (i10 % COUISupportMenuView.this.f6464e));
            if (COUISupportMenuView.this.isLayoutRtl()) {
                i12 = COUISupportMenuView.this.getWidth() - ((COUISupportMenuView.this.f6468i + (COUISupportMenuView.this.f6481v / 2)) + ((COUISupportMenuView.this.f6481v + COUISupportMenuView.this.f6468i) * (i10 % COUISupportMenuView.this.f6464e)));
            }
            int i13 = COUISupportMenuView.this.f6468i + i12;
            int unused = COUISupportMenuView.this.f6475p;
            int i14 = i10 / COUISupportMenuView.this.f6464e;
            if (i10 < COUISupportMenuView.this.f6464e) {
                i11 = COUISupportMenuView.this.f6475p;
            } else {
                i11 = COUISupportMenuView.this.f6483x;
            }
            rect.set(i12, i11, i13, (((COUISupportMenuView.this.f6467h + i11) + COUISupportMenuView.this.f6478s) + fontMetricsInt.bottom) - fontMetricsInt.top);
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public CharSequence b(int i10) {
            String c10 = ((COUISupportMenuItem) COUISupportMenuView.this.f6465f.get(i10)).c();
            return c10 != null ? c10 : getClass().getSimpleName();
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public CharSequence c() {
            return Button.class.getName();
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int d() {
            return -1;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int e() {
            return COUISupportMenuView.this.f6479t;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public void f(int i10, int i11, boolean z10) {
            if (((COUISupportMenuItem) COUISupportMenuView.this.f6465f.get(i10)).b() != null) {
                ((COUISupportMenuItem) COUISupportMenuView.this.f6465f.get(i10)).b().a(i10);
            }
            COUISupportMenuView.this.A.sendEventForVirtualView(i10, 1);
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int g(float f10, float f11) {
            int r10 = COUISupportMenuView.this.r((int) f10, (int) f11);
            this.f6486a = r10;
            return r10;
        }

        @Override // g3.COUIViewExplorerByTouchHelper.a
        public int h() {
            return COUISupportMenuView.this.f6470k;
        }
    }

    public COUISupportMenuView(Context context) {
        this(context, null);
    }

    private void n() {
        Iterator<COUISupportMenuItem> it = this.f6465f.iterator();
        while (it.hasNext()) {
            Drawable a10 = it.next().a();
            if (a10 != null && a10.isStateful()) {
                a10.setState(F);
            }
        }
        this.f6469j = false;
        invalidate();
    }

    private String o(String str, Paint paint, int i10) {
        int breakText = paint.breakText(str, true, i10, null);
        if (breakText == str.length()) {
            return str;
        }
        return str.substring(0, breakText - 1) + "...";
    }

    private void p(int i10, Rect rect) {
        int i11 = this.f6481v;
        int i12 = (i11 / 2) + ((i11 + this.f6468i) * (i10 % this.f6464e));
        if (isLayoutRtl()) {
            int width = getWidth();
            int i13 = this.f6468i;
            int i14 = this.f6481v;
            i12 = width - (((i14 / 2) + i13) + ((i14 + i13) * (i10 % this.f6464e)));
        }
        int i15 = this.f6475p;
        int i16 = this.f6464e;
        int i17 = i10 / i16;
        if (i10 >= i16) {
            i15 += this.f6483x;
        }
        rect.set(i12, i15, this.f6468i + i12, this.f6467h + i15);
    }

    private void q(int i10) {
        Drawable a10 = this.f6465f.get(i10).a();
        StateListDrawable stateListDrawable = new StateListDrawable();
        int[] iArr = E;
        a10.setState(iArr);
        stateListDrawable.addState(iArr, a10.getCurrent());
        int[] iArr2 = C;
        a10.setState(iArr2);
        stateListDrawable.addState(iArr2, a10.getCurrent());
        int[] iArr3 = D;
        a10.setState(iArr3);
        stateListDrawable.addState(iArr3, a10.getCurrent());
        int[] iArr4 = F;
        a10.setState(iArr4);
        stateListDrawable.addState(iArr4, a10.getCurrent());
        this.f6465f.get(i10).d(stateListDrawable);
        this.f6465f.get(i10).a().setCallback(this);
        n();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int r(float f10, float f11) {
        int i10;
        int i11 = this.f6479t;
        if (i11 < 1) {
            return -1;
        }
        if (i11 <= this.f6464e) {
            if (isLayoutRtl()) {
                f10 = getWidth() - f10;
            }
            i10 = (int) (f10 / (getWidth() / this.f6479t));
        } else {
            if (isLayoutRtl()) {
                f10 = getWidth() - f10;
            }
            int width = getWidth();
            int i12 = this.f6464e;
            i10 = (int) (f10 / (width / i12));
            if (f11 > this.f6483x) {
                i10 += i12;
            }
        }
        if (i10 < this.f6479t) {
            return i10;
        }
        return -1;
    }

    public void clearAccessibilityFocus() {
        COUIViewExplorerByTouchHelper cOUIViewExplorerByTouchHelper = this.A;
        if (cOUIViewExplorerByTouchHelper != null) {
            cOUIViewExplorerByTouchHelper.a();
        }
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        COUIViewExplorerByTouchHelper cOUIViewExplorerByTouchHelper = this.A;
        if (cOUIViewExplorerByTouchHelper == null || !cOUIViewExplorerByTouchHelper.dispatchHoverEvent(motionEvent)) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float y4 = motionEvent.getY();
        if (motionEvent.getPointerCount() == 1 && y4 >= 0.0f) {
            if (motionEvent.getAction() == 0) {
                this.f6470k = r(motionEvent.getX(), motionEvent.getY());
            }
        } else {
            n();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void drawableStateChanged() {
        Drawable a10;
        int i10 = this.f6470k;
        if (i10 >= 0 && i10 < this.f6479t && (a10 = this.f6465f.get(i10).a()) != null && a10.isStateful()) {
            a10.setState(getDrawableState());
        }
        super.drawableStateChanged();
    }

    public boolean isLayoutRtl() {
        return getLayoutDirection() == 1;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i10 = this.f6479t;
        if (i10 < 1) {
            return;
        }
        if (i10 <= this.f6464e) {
            int width = getWidth();
            int i11 = this.f6468i;
            int i12 = this.f6479t;
            this.f6481v = (width - (i11 * i12)) / i12;
        } else {
            int width2 = getWidth();
            int i13 = this.f6468i;
            int i14 = this.f6464e;
            this.f6481v = (width2 - (i13 * i14)) / i14;
        }
        this.f6484y = (this.f6481v + this.f6468i) - (this.f6485z * 2);
        for (int i15 = 0; i15 < this.f6479t; i15++) {
            p(i15, this.f6466g);
            COUISupportMenuItem cOUISupportMenuItem = this.f6465f.get(i15);
            cOUISupportMenuItem.a().setBounds(this.f6466g);
            cOUISupportMenuItem.a().draw(canvas);
            this.f6471l.setColor(this.f6473n);
            int i16 = -this.f6471l.getFontMetricsInt().top;
            Rect rect = this.f6466g;
            canvas.drawText(o(cOUISupportMenuItem.c(), this.f6471l, this.f6484y), rect.left + (this.f6468i / 2), rect.bottom + this.f6478s + i16, this.f6471l);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        Paint.FontMetricsInt fontMetricsInt = this.f6471l.getFontMetricsInt();
        int i12 = this.f6475p + this.f6467h + this.f6478s + (fontMetricsInt.bottom - fontMetricsInt.top) + this.f6476q;
        this.f6483x = i12;
        if (this.f6479t > this.f6464e) {
            i12 *= 2;
        }
        setMeasuredDimension(this.f6482w, i12);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.f6469j = true;
            invalidate();
            return true;
        }
        if (action != 1) {
            if (action != 3) {
                return true;
            }
            n();
            return false;
        }
        int i10 = this.f6470k;
        if (i10 >= 0) {
            this.f6465f.get(i10).b().a(this.f6470k);
        }
        n();
        return false;
    }

    public void setColorSupportMenuItem(List<COUISupportMenuItem> list) {
        this.f6465f = list;
        int size = list.size();
        if (size <= 0) {
            return;
        }
        if (size > 10) {
            this.f6479t = 10;
            this.f6465f = this.f6465f.subList(0, 10);
        } else if (size == 7) {
            this.f6479t = 6;
            this.f6465f = this.f6465f.subList(0, 6);
        } else if (size == 9) {
            this.f6479t = 8;
            this.f6465f = this.f6465f.subList(0, 8);
        } else {
            this.f6479t = size;
        }
        if (size > 5) {
            this.f6464e = size / 2;
        } else {
            this.f6464e = 5;
        }
        for (int i10 = 0; i10 < this.f6479t; i10++) {
            q(i10);
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        super.verifyDrawable(drawable);
        return true;
    }

    public COUISupportMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUISupportMenuView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6464e = 5;
        this.f6465f = new ArrayList();
        this.f6466g = new Rect();
        this.f6469j = false;
        this.f6470k = -1;
        this.f6472m = 30.0f;
        this.f6479t = 0;
        this.B = new a();
        Paint paint = new Paint();
        this.f6471l = paint;
        paint.setTextAlign(Paint.Align.CENTER);
        this.f6471l.setAntiAlias(true);
        this.f6482w = (int) getResources().getDimension(R$dimen.coui_support_menu_width);
        this.f6475p = (int) getResources().getDimension(R$dimen.coui_support_menu_padding_top);
        this.f6476q = (int) getResources().getDimension(R$dimen.coui_support_menu_padding_bottom);
        this.f6477r = (int) getResources().getDimension(R$dimen.coui_support_menu_view_padding_bottom);
        this.f6467h = (int) getResources().getDimension(R$dimen.coui_support_menu_item_height);
        this.f6468i = (int) getResources().getDimension(R$dimen.coui_support_menu_item_width);
        this.f6478s = (int) getResources().getDimension(R$dimen.coui_support_menu_text_padding_top);
        this.f6484y = (int) getResources().getDimension(R$dimen.coui_support_menu_text_max_length);
        this.f6485z = (int) getResources().getDimension(R$dimen.coui_support_menu_text_padding_side);
        this.f6472m = (int) getResources().getDimension(R$dimen.coui_support_menu_item_textsize);
        this.f6474o = getResources().getColor(R$color.coui_support_menu_textcolor_select);
        this.f6473n = getResources().getColor(R$color.coui_support_menu_textcolor_normal);
        this.f6480u = getResources().getDrawable(R$drawable.coui_support_menu_item_cover);
        float e10 = (int) COUIChangeTextUtil.e(this.f6472m, getResources().getConfiguration().fontScale, 4);
        this.f6472m = e10;
        this.f6471l.setTextSize(e10);
        setClickable(true);
        COUIViewExplorerByTouchHelper cOUIViewExplorerByTouchHelper = new COUIViewExplorerByTouchHelper(this);
        this.A = cOUIViewExplorerByTouchHelper;
        cOUIViewExplorerByTouchHelper.c(this.B);
        ViewCompat.l0(this, this.A);
        ViewCompat.w0(this, 1);
    }
}
