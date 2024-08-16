package androidx.viewpager.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.content.ContextCompat;

/* loaded from: classes.dex */
public class PagerTabStrip extends PagerTitleStrip {
    private final Paint A;
    private final Rect B;
    private int C;
    private boolean D;
    private boolean E;
    private int F;
    private boolean G;
    private float H;
    private float I;
    private int J;

    /* renamed from: u, reason: collision with root package name */
    private int f4181u;

    /* renamed from: v, reason: collision with root package name */
    private int f4182v;

    /* renamed from: w, reason: collision with root package name */
    private int f4183w;

    /* renamed from: x, reason: collision with root package name */
    private int f4184x;

    /* renamed from: y, reason: collision with root package name */
    private int f4185y;

    /* renamed from: z, reason: collision with root package name */
    private int f4186z;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PagerTabStrip.this.f4191e.setCurrentItem(r0.getCurrentItem() - 1);
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ViewPager viewPager = PagerTabStrip.this.f4191e;
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public PagerTabStrip(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.viewpager.widget.PagerTitleStrip
    public void d(int i10, float f10, boolean z10) {
        Rect rect = this.B;
        int height = getHeight();
        int left = this.f4193g.getLeft() - this.f4186z;
        int right = this.f4193g.getRight() + this.f4186z;
        int i11 = height - this.f4182v;
        rect.set(left, i11, right, height);
        super.d(i10, f10, z10);
        this.C = (int) (Math.abs(f10 - 0.5f) * 2.0f * 255.0f);
        rect.union(this.f4193g.getLeft() - this.f4186z, i11, this.f4193g.getRight() + this.f4186z, height);
        invalidate(rect);
    }

    public boolean getDrawFullUnderline() {
        return this.D;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.viewpager.widget.PagerTitleStrip
    public int getMinHeight() {
        return Math.max(super.getMinHeight(), this.f4185y);
    }

    public int getTabIndicatorColor() {
        return this.f4181u;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int left = this.f4193g.getLeft() - this.f4186z;
        int right = this.f4193g.getRight() + this.f4186z;
        int i10 = height - this.f4182v;
        this.A.setColor((this.C << 24) | (this.f4181u & 16777215));
        float f10 = height;
        canvas.drawRect(left, i10, right, f10, this.A);
        if (this.D) {
            this.A.setColor((-16777216) | (this.f4181u & 16777215));
            canvas.drawRect(getPaddingLeft(), height - this.F, getWidth() - getPaddingRight(), f10, this.A);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0 && this.G) {
            return false;
        }
        float x10 = motionEvent.getX();
        float y4 = motionEvent.getY();
        if (action == 0) {
            this.H = x10;
            this.I = y4;
            this.G = false;
        } else if (action != 1) {
            if (action == 2 && (Math.abs(x10 - this.H) > this.J || Math.abs(y4 - this.I) > this.J)) {
                this.G = true;
            }
        } else if (x10 < this.f4193g.getLeft() - this.f4186z) {
            ViewPager viewPager = this.f4191e;
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else if (x10 > this.f4193g.getRight() + this.f4186z) {
            ViewPager viewPager2 = this.f4191e;
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
        return true;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i10) {
        super.setBackgroundColor(i10);
        if (this.E) {
            return;
        }
        this.D = (i10 & (-16777216)) == 0;
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        if (this.E) {
            return;
        }
        this.D = drawable == null;
    }

    @Override // android.view.View
    public void setBackgroundResource(int i10) {
        super.setBackgroundResource(i10);
        if (this.E) {
            return;
        }
        this.D = i10 == 0;
    }

    public void setDrawFullUnderline(boolean z10) {
        this.D = z10;
        this.E = true;
        invalidate();
    }

    @Override // android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
        int i14 = this.f4183w;
        if (i13 < i14) {
            i13 = i14;
        }
        super.setPadding(i10, i11, i12, i13);
    }

    public void setTabIndicatorColor(int i10) {
        this.f4181u = i10;
        this.A.setColor(i10);
        invalidate();
    }

    public void setTabIndicatorColorResource(int i10) {
        setTabIndicatorColor(ContextCompat.c(getContext(), i10));
    }

    @Override // androidx.viewpager.widget.PagerTitleStrip
    public void setTextSpacing(int i10) {
        int i11 = this.f4184x;
        if (i10 < i11) {
            i10 = i11;
        }
        super.setTextSpacing(i10);
    }

    public PagerTabStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.A = paint;
        this.B = new Rect();
        this.C = 255;
        this.D = false;
        this.E = false;
        int i10 = this.f4204r;
        this.f4181u = i10;
        paint.setColor(i10);
        float f10 = context.getResources().getDisplayMetrics().density;
        this.f4182v = (int) ((3.0f * f10) + 0.5f);
        this.f4183w = (int) ((6.0f * f10) + 0.5f);
        this.f4184x = (int) (64.0f * f10);
        this.f4186z = (int) ((16.0f * f10) + 0.5f);
        this.F = (int) ((1.0f * f10) + 0.5f);
        this.f4185y = (int) ((f10 * 32.0f) + 0.5f);
        this.J = ViewConfiguration.get(context).getScaledTouchSlop();
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setTextSpacing(getTextSpacing());
        setWillNotDraw(false);
        this.f4192f.setFocusable(true);
        this.f4192f.setOnClickListener(new a());
        this.f4194h.setFocusable(true);
        this.f4194h.setOnClickListener(new b());
        if (getBackground() == null) {
            this.D = true;
        }
    }
}
