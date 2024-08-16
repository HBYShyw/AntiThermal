package androidx.viewpager.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.ViewPager;
import java.lang.ref.WeakReference;
import java.util.Locale;

@ViewPager.e
/* loaded from: classes.dex */
public class PagerTitleStrip extends ViewGroup {

    /* renamed from: s, reason: collision with root package name */
    private static final int[] f4189s = {R.attr.textAppearance, R.attr.textSize, R.attr.textColor, R.attr.gravity};

    /* renamed from: t, reason: collision with root package name */
    private static final int[] f4190t = {R.attr.textAllCaps};

    /* renamed from: e, reason: collision with root package name */
    ViewPager f4191e;

    /* renamed from: f, reason: collision with root package name */
    TextView f4192f;

    /* renamed from: g, reason: collision with root package name */
    TextView f4193g;

    /* renamed from: h, reason: collision with root package name */
    TextView f4194h;

    /* renamed from: i, reason: collision with root package name */
    private int f4195i;

    /* renamed from: j, reason: collision with root package name */
    float f4196j;

    /* renamed from: k, reason: collision with root package name */
    private int f4197k;

    /* renamed from: l, reason: collision with root package name */
    private int f4198l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f4199m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f4200n;

    /* renamed from: o, reason: collision with root package name */
    private final a f4201o;

    /* renamed from: p, reason: collision with root package name */
    private WeakReference<PagerAdapter> f4202p;

    /* renamed from: q, reason: collision with root package name */
    private int f4203q;

    /* renamed from: r, reason: collision with root package name */
    int f4204r;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class a extends DataSetObserver implements ViewPager.i, ViewPager.h {

        /* renamed from: a, reason: collision with root package name */
        private int f4205a;

        a() {
        }

        @Override // androidx.viewpager.widget.ViewPager.h
        public void a(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            PagerTitleStrip.this.b(pagerAdapter, pagerAdapter2);
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
            pagerTitleStrip.c(pagerTitleStrip.f4191e.getCurrentItem(), PagerTitleStrip.this.f4191e.getAdapter());
            PagerTitleStrip pagerTitleStrip2 = PagerTitleStrip.this;
            float f10 = pagerTitleStrip2.f4196j;
            if (f10 < 0.0f) {
                f10 = 0.0f;
            }
            pagerTitleStrip2.d(pagerTitleStrip2.f4191e.getCurrentItem(), f10, true);
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageScrollStateChanged(int i10) {
            this.f4205a = i10;
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageScrolled(int i10, float f10, int i11) {
            if (f10 > 0.5f) {
                i10++;
            }
            PagerTitleStrip.this.d(i10, f10, false);
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageSelected(int i10) {
            if (this.f4205a == 0) {
                PagerTitleStrip pagerTitleStrip = PagerTitleStrip.this;
                pagerTitleStrip.c(pagerTitleStrip.f4191e.getCurrentItem(), PagerTitleStrip.this.f4191e.getAdapter());
                PagerTitleStrip pagerTitleStrip2 = PagerTitleStrip.this;
                float f10 = pagerTitleStrip2.f4196j;
                if (f10 < 0.0f) {
                    f10 = 0.0f;
                }
                pagerTitleStrip2.d(pagerTitleStrip2.f4191e.getCurrentItem(), f10, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends SingleLineTransformationMethod {

        /* renamed from: e, reason: collision with root package name */
        private Locale f4207e;

        b(Context context) {
            this.f4207e = context.getResources().getConfiguration().locale;
        }

        @Override // android.text.method.ReplacementTransformationMethod, android.text.method.TransformationMethod
        public CharSequence getTransformation(CharSequence charSequence, View view) {
            CharSequence transformation = super.getTransformation(charSequence, view);
            if (transformation != null) {
                return transformation.toString().toUpperCase(this.f4207e);
            }
            return null;
        }
    }

    public PagerTitleStrip(Context context) {
        this(context, null);
    }

    private static void setSingleLineAllCaps(TextView textView) {
        textView.setTransformationMethod(new b(textView.getContext()));
    }

    public void a(int i10, float f10) {
        this.f4192f.setTextSize(i10, f10);
        this.f4193g.setTextSize(i10, f10);
        this.f4194h.setTextSize(i10, f10);
    }

    void b(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
        if (pagerAdapter != null) {
            pagerAdapter.unregisterDataSetObserver(this.f4201o);
            this.f4202p = null;
        }
        if (pagerAdapter2 != null) {
            pagerAdapter2.registerDataSetObserver(this.f4201o);
            this.f4202p = new WeakReference<>(pagerAdapter2);
        }
        ViewPager viewPager = this.f4191e;
        if (viewPager != null) {
            this.f4195i = -1;
            this.f4196j = -1.0f;
            c(viewPager.getCurrentItem(), pagerAdapter2);
            requestLayout();
        }
    }

    void c(int i10, PagerAdapter pagerAdapter) {
        int count = pagerAdapter != null ? pagerAdapter.getCount() : 0;
        this.f4199m = true;
        CharSequence charSequence = null;
        this.f4192f.setText((i10 < 1 || pagerAdapter == null) ? null : pagerAdapter.getPageTitle(i10 - 1));
        this.f4193g.setText((pagerAdapter == null || i10 >= count) ? null : pagerAdapter.getPageTitle(i10));
        int i11 = i10 + 1;
        if (i11 < count && pagerAdapter != null) {
            charSequence = pagerAdapter.getPageTitle(i11);
        }
        this.f4194h.setText(charSequence);
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max(0, (int) (((getWidth() - getPaddingLeft()) - getPaddingRight()) * 0.8f)), Integer.MIN_VALUE);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(Math.max(0, (getHeight() - getPaddingTop()) - getPaddingBottom()), Integer.MIN_VALUE);
        this.f4192f.measure(makeMeasureSpec, makeMeasureSpec2);
        this.f4193g.measure(makeMeasureSpec, makeMeasureSpec2);
        this.f4194h.measure(makeMeasureSpec, makeMeasureSpec2);
        this.f4195i = i10;
        if (!this.f4200n) {
            d(i10, this.f4196j, false);
        }
        this.f4199m = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i10, float f10, boolean z10) {
        int i11;
        int i12;
        int i13;
        int i14;
        if (i10 != this.f4195i) {
            c(i10, this.f4191e.getAdapter());
        } else if (!z10 && f10 == this.f4196j) {
            return;
        }
        this.f4200n = true;
        int measuredWidth = this.f4192f.getMeasuredWidth();
        int measuredWidth2 = this.f4193g.getMeasuredWidth();
        int measuredWidth3 = this.f4194h.getMeasuredWidth();
        int i15 = measuredWidth2 / 2;
        int width = getWidth();
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i16 = paddingRight + i15;
        int i17 = (width - (paddingLeft + i15)) - i16;
        float f11 = 0.5f + f10;
        if (f11 > 1.0f) {
            f11 -= 1.0f;
        }
        int i18 = ((width - i16) - ((int) (i17 * f11))) - i15;
        int i19 = measuredWidth2 + i18;
        int baseline = this.f4192f.getBaseline();
        int baseline2 = this.f4193g.getBaseline();
        int baseline3 = this.f4194h.getBaseline();
        int max = Math.max(Math.max(baseline, baseline2), baseline3);
        int i20 = max - baseline;
        int i21 = max - baseline2;
        int i22 = max - baseline3;
        int max2 = Math.max(Math.max(this.f4192f.getMeasuredHeight() + i20, this.f4193g.getMeasuredHeight() + i21), this.f4194h.getMeasuredHeight() + i22);
        int i23 = this.f4198l & 112;
        if (i23 == 16) {
            i11 = (((height - paddingTop) - paddingBottom) - max2) / 2;
        } else {
            if (i23 != 80) {
                i12 = i20 + paddingTop;
                i13 = i21 + paddingTop;
                i14 = paddingTop + i22;
                TextView textView = this.f4193g;
                textView.layout(i18, i13, i19, textView.getMeasuredHeight() + i13);
                int min = Math.min(paddingLeft, (i18 - this.f4197k) - measuredWidth);
                TextView textView2 = this.f4192f;
                textView2.layout(min, i12, measuredWidth + min, textView2.getMeasuredHeight() + i12);
                int max3 = Math.max((width - paddingRight) - measuredWidth3, i19 + this.f4197k);
                TextView textView3 = this.f4194h;
                textView3.layout(max3, i14, max3 + measuredWidth3, textView3.getMeasuredHeight() + i14);
                this.f4196j = f10;
                this.f4200n = false;
            }
            i11 = (height - paddingBottom) - max2;
        }
        i12 = i20 + i11;
        i13 = i21 + i11;
        i14 = i11 + i22;
        TextView textView4 = this.f4193g;
        textView4.layout(i18, i13, i19, textView4.getMeasuredHeight() + i13);
        int min2 = Math.min(paddingLeft, (i18 - this.f4197k) - measuredWidth);
        TextView textView22 = this.f4192f;
        textView22.layout(min2, i12, measuredWidth + min2, textView22.getMeasuredHeight() + i12);
        int max32 = Math.max((width - paddingRight) - measuredWidth3, i19 + this.f4197k);
        TextView textView32 = this.f4194h;
        textView32.layout(max32, i14, max32 + measuredWidth3, textView32.getMeasuredHeight() + i14);
        this.f4196j = f10;
        this.f4200n = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMinHeight() {
        Drawable background = getBackground();
        if (background != null) {
            return background.getIntrinsicHeight();
        }
        return 0;
    }

    public int getTextSpacing() {
        return this.f4197k;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) parent;
            PagerAdapter adapter = viewPager.getAdapter();
            viewPager.setInternalPageChangeListener(this.f4201o);
            viewPager.addOnAdapterChangeListener(this.f4201o);
            this.f4191e = viewPager;
            WeakReference<PagerAdapter> weakReference = this.f4202p;
            b(weakReference != null ? weakReference.get() : null, adapter);
            return;
        }
        throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewPager viewPager = this.f4191e;
        if (viewPager != null) {
            b(viewPager.getAdapter(), null);
            this.f4191e.setInternalPageChangeListener(null);
            this.f4191e.removeOnAdapterChangeListener(this.f4201o);
            this.f4191e = null;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        if (this.f4191e != null) {
            float f10 = this.f4196j;
            if (f10 < 0.0f) {
                f10 = 0.0f;
            }
            d(this.f4195i, f10, true);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        int max;
        if (View.MeasureSpec.getMode(i10) == 1073741824) {
            int paddingTop = getPaddingTop() + getPaddingBottom();
            int childMeasureSpec = ViewGroup.getChildMeasureSpec(i11, paddingTop, -2);
            int size = View.MeasureSpec.getSize(i10);
            int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(i10, (int) (size * 0.2f), -2);
            this.f4192f.measure(childMeasureSpec2, childMeasureSpec);
            this.f4193g.measure(childMeasureSpec2, childMeasureSpec);
            this.f4194h.measure(childMeasureSpec2, childMeasureSpec);
            if (View.MeasureSpec.getMode(i11) == 1073741824) {
                max = View.MeasureSpec.getSize(i11);
            } else {
                max = Math.max(getMinHeight(), this.f4193g.getMeasuredHeight() + paddingTop);
            }
            setMeasuredDimension(size, View.resolveSizeAndState(max, i11, this.f4193g.getMeasuredState() << 16));
            return;
        }
        throw new IllegalStateException("Must measure with an exact width");
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.f4199m) {
            return;
        }
        super.requestLayout();
    }

    public void setGravity(int i10) {
        this.f4198l = i10;
        requestLayout();
    }

    public void setNonPrimaryAlpha(float f10) {
        int i10 = ((int) (f10 * 255.0f)) & 255;
        this.f4203q = i10;
        int i11 = (i10 << 24) | (this.f4204r & 16777215);
        this.f4192f.setTextColor(i11);
        this.f4194h.setTextColor(i11);
    }

    public void setTextColor(int i10) {
        this.f4204r = i10;
        this.f4193g.setTextColor(i10);
        int i11 = (this.f4203q << 24) | (this.f4204r & 16777215);
        this.f4192f.setTextColor(i11);
        this.f4194h.setTextColor(i11);
    }

    public void setTextSpacing(int i10) {
        this.f4197k = i10;
        requestLayout();
    }

    public PagerTitleStrip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4195i = -1;
        this.f4196j = -1.0f;
        this.f4201o = new a();
        TextView textView = new TextView(context);
        this.f4192f = textView;
        addView(textView);
        TextView textView2 = new TextView(context);
        this.f4193g = textView2;
        addView(textView2);
        TextView textView3 = new TextView(context);
        this.f4194h = textView3;
        addView(textView3);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f4189s);
        boolean z10 = false;
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId != 0) {
            TextViewCompat.n(this.f4192f, resourceId);
            TextViewCompat.n(this.f4193g, resourceId);
            TextViewCompat.n(this.f4194h, resourceId);
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        if (dimensionPixelSize != 0) {
            a(0, dimensionPixelSize);
        }
        if (obtainStyledAttributes.hasValue(2)) {
            int color = obtainStyledAttributes.getColor(2, 0);
            this.f4192f.setTextColor(color);
            this.f4193g.setTextColor(color);
            this.f4194h.setTextColor(color);
        }
        this.f4198l = obtainStyledAttributes.getInteger(3, 80);
        obtainStyledAttributes.recycle();
        this.f4204r = this.f4193g.getTextColors().getDefaultColor();
        setNonPrimaryAlpha(0.6f);
        this.f4192f.setEllipsize(TextUtils.TruncateAt.END);
        this.f4193g.setEllipsize(TextUtils.TruncateAt.END);
        this.f4194h.setEllipsize(TextUtils.TruncateAt.END);
        if (resourceId != 0) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, f4190t);
            z10 = obtainStyledAttributes2.getBoolean(0, false);
            obtainStyledAttributes2.recycle();
        }
        if (z10) {
            setSingleLineAllCaps(this.f4192f);
            setSingleLineAllCaps(this.f4193g);
            setSingleLineAllCaps(this.f4194h);
        } else {
            this.f4192f.setSingleLine();
            this.f4193g.setSingleLine();
            this.f4194h.setSingleLine();
        }
        this.f4197k = (int) (context.getResources().getDisplayMetrics().density * 16.0f);
    }
}
