package com.coui.appcompat.panel;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.coui.appcompat.grid.COUIPercentWidthFrameLayout;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.panel.COUIPanelPercentFrameLayout;
import com.support.panel.R$dimen;
import com.support.panel.R$styleable;
import f2.COUIPanelMultiWindowUtils;

/* loaded from: classes.dex */
public class COUIPanelPercentFrameLayout extends COUIPercentWidthFrameLayout {

    /* renamed from: u, reason: collision with root package name */
    private static final String f6618u = COUIPanelPercentFrameLayout.class.getSimpleName();

    /* renamed from: p, reason: collision with root package name */
    private final Rect f6619p;

    /* renamed from: q, reason: collision with root package name */
    private int f6620q;

    /* renamed from: r, reason: collision with root package name */
    private float f6621r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f6622s;

    /* renamed from: t, reason: collision with root package name */
    private int f6623t;

    /* loaded from: classes.dex */
    class a extends ViewOutlineProvider {
        a() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            COUIPanelPercentFrameLayout.this.h();
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight() + COUIPanelPercentFrameLayout.this.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_bg_bottom_corner_radius), COUIPanelPercentFrameLayout.this.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_bottom_sheet_bg_top_corner_radius));
        }
    }

    public COUIPanelPercentFrameLayout(Context context) {
        this(context, null);
    }

    private void c(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIPanelPercentFrameLayout);
            this.f6620q = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIPanelPercentFrameLayout_maxPanelHeight, 0);
            obtainStyledAttributes.recycle();
        }
        this.f6621r = COUIPanelMultiWindowUtils.t(getContext(), null) ? 1.0f : 2.0f;
        this.f6620q = COUIPanelMultiWindowUtils.l(getContext(), this.f6620q);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (this.f6623t == -1) {
            return;
        }
        try {
            Resources resources = getContext().getResources();
            Configuration configuration = resources.getConfiguration();
            int i10 = configuration.screenWidthDp;
            int i11 = this.f6623t;
            if (i10 == i11) {
                return;
            }
            configuration.screenWidthDp = i11;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Log.d(f6618u, "enforceChangeScreenWidth : PreferWidth:" + this.f6623t);
        } catch (Exception unused) {
            Log.d(f6618u, "enforceChangeScreenWidth : failed to updateConfiguration");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void i() {
        requestLayout();
    }

    public void g() {
        this.f6623t = -1;
        Log.d(f6618u, "delPreferWidth");
    }

    public boolean getHasAnchor() {
        return this.f6622s;
    }

    public float getRatio() {
        return this.f6621r;
    }

    public void j(Configuration configuration) {
        this.f6621r = COUIPanelMultiWindowUtils.t(getContext(), configuration) ? 1.0f : 2.0f;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f6621r = COUIPanelMultiWindowUtils.t(getContext(), null) ? 1.0f : 2.0f;
        setOutlineProvider(new a());
        setClipToOutline(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.grid.COUIPercentWidthFrameLayout, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (getContext() != null) {
            this.f6620q = COUIPanelMultiWindowUtils.l(getContext(), this.f6620q);
            post(new Runnable() { // from class: f2.g
                @Override // java.lang.Runnable
                public final void run() {
                    COUIPanelPercentFrameLayout.this.i();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.coui.appcompat.grid.COUIPercentWidthFrameLayout, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        getWindowVisibleDisplayFrame(this.f6619p);
        int height = this.f6619p.height();
        int i12 = this.f6620q;
        if (height > i12 && i12 > 0 && i12 < View.MeasureSpec.getSize(i11)) {
            i11 = View.MeasureSpec.makeMeasureSpec(this.f6620q, View.MeasureSpec.getMode(i11));
        }
        setPercentIndentEnabled(((!COUIPanelMultiWindowUtils.t(getContext(), null) && View.MeasureSpec.getSize(i10) < this.f6619p.width()) || COUIResponsiveUtils.i(getContext(), this.f6619p.width())) ? false : true);
        super.onMeasure(i10, i11);
    }

    public void setHasAnchor(boolean z10) {
        this.f6622s = z10;
    }

    public void setPreferWidth(int i10) {
        this.f6623t = i10;
        Log.d(f6618u, "setPreferWidth =：" + this.f6623t);
    }

    public COUIPanelPercentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIPanelPercentFrameLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6621r = 1.0f;
        this.f6622s = false;
        this.f6623t = -1;
        c(attributeSet);
        this.f6619p = new Rect();
    }
}
