package com.coui.appcompat.floatingactionbutton;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import c4.ShapeAppearanceModel;
import com.coui.appcompat.floatingactionbutton.COUIFloatingButton;
import com.coui.appcompat.floatingactionbutton.COUIFloatingButtonItem;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.support.appcompat.R$attr;
import com.support.control.R$color;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$styleable;
import h3.UIUtil;
import m1.COUIAnimationListenerAdapter;
import u2.COUIStateListUtil;
import v1.COUIContextUtil;

/* loaded from: classes.dex */
public class COUIFloatingButtonLabel extends LinearLayout {

    /* renamed from: p, reason: collision with root package name */
    private static final String f6040p = COUIFloatingButtonLabel.class.getSimpleName();

    /* renamed from: e, reason: collision with root package name */
    private int f6041e;

    /* renamed from: f, reason: collision with root package name */
    private int f6042f;

    /* renamed from: g, reason: collision with root package name */
    private float f6043g;

    /* renamed from: h, reason: collision with root package name */
    private ValueAnimator f6044h;

    /* renamed from: i, reason: collision with root package name */
    private TextView f6045i;

    /* renamed from: j, reason: collision with root package name */
    private ShapeableImageView f6046j;

    /* renamed from: k, reason: collision with root package name */
    private CardView f6047k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f6048l;

    /* renamed from: m, reason: collision with root package name */
    private COUIFloatingButtonItem f6049m;

    /* renamed from: n, reason: collision with root package name */
    private COUIFloatingButton.l f6050n;

    /* renamed from: o, reason: collision with root package name */
    private float f6051o;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIFloatingButtonLabel.this.l();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIFloatingButtonItem floatingButtonItem = COUIFloatingButtonLabel.this.getFloatingButtonItem();
            if (COUIFloatingButtonLabel.this.f6050n == null || floatingButtonItem == null) {
                return;
            }
            COUIFloatingButtonLabel.this.f6050n.a(floatingButtonItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends ViewOutlineProvider {
        c() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends ViewOutlineProvider {
        d() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), COUIFloatingButtonLabel.this.getContext().getResources().getDimensionPixelSize(R$dimen.coui_floating_button_item_label_background_radius));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements View.OnTouchListener {
        e() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                COUIFloatingButtonLabel.this.i();
                return false;
            }
            if (action != 1 && action != 3) {
                return false;
            }
            COUIFloatingButtonLabel.this.h();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements ValueAnimator.AnimatorUpdateListener {
        f() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            COUIFloatingButtonLabel.this.f6043g = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (COUIFloatingButtonLabel.this.f6043g >= 0.98f) {
                COUIFloatingButtonLabel.this.f6043g = 0.98f;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g extends COUIAnimationListenerAdapter {
        g() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            COUIFloatingButtonLabel.this.f6044h.start();
        }
    }

    public COUIFloatingButtonLabel(Context context) {
        super(context);
        this.f6041e = 0;
        m(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        performHapticFeedback(302);
        clearAnimation();
        j();
        ShapeableImageView shapeableImageView = this.f6046j;
        shapeableImageView.startAnimation(COUIFABPressFeedbackUtil.c(shapeableImageView, this.f6043g));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        performHapticFeedback(302);
        clearAnimation();
        j();
        COUIFloatingButtonTouchAnimation a10 = COUIFABPressFeedbackUtil.a(this.f6046j);
        ValueAnimator b10 = COUIFABPressFeedbackUtil.b();
        this.f6044h = b10;
        b10.addUpdateListener(new f());
        a10.setAnimationListener(new g());
        this.f6046j.startAnimation(a10);
    }

    private void j() {
        ValueAnimator valueAnimator = this.f6044h;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.f6044h.cancel();
    }

    private void k() {
        this.f6046j.setOnTouchListener(new e());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        COUIFloatingButtonItem floatingButtonItem = getFloatingButtonItem();
        COUIFloatingButton.l lVar = this.f6050n;
        if (lVar == null || floatingButtonItem == null) {
            return;
        }
        lVar.a(floatingButtonItem);
    }

    private void m(Context context, AttributeSet attributeSet) {
        View inflate = LinearLayout.inflate(context, R$layout.coui_floating_button_item_label, this);
        this.f6046j = (ShapeableImageView) inflate.findViewById(R$id.coui_floating_button_child_fab);
        this.f6045i = (TextView) inflate.findViewById(R$id.coui_floating_button_label);
        this.f6047k = (CardView) inflate.findViewById(R$id.coui_floating_button_label_container);
        ShapeableImageView shapeableImageView = this.f6046j;
        Resources resources = getResources();
        int i10 = com.support.appcompat.R$dimen.support_shadow_size_level_three;
        int dimensionPixelOffset = resources.getDimensionPixelOffset(i10);
        Resources resources2 = getResources();
        int i11 = R$color.coui_floating_button_elevation_color;
        UIUtil.j(shapeableImageView, dimensionPixelOffset, resources2.getColor(i11));
        this.f6046j.setOutlineProvider(new c());
        this.f6046j.setShapeAppearanceModel(ShapeAppearanceModel.a().p(ShapeAppearanceModel.f4815m).m());
        UIUtil.j(this.f6047k, getResources().getDimensionPixelOffset(i10), getResources().getColor(i11));
        this.f6047k.setOutlineProvider(new d());
        setOrientation(0);
        setClipChildren(false);
        setClipToPadding(false);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUIFloatingButtonLabel, 0, 0);
        try {
            try {
                int resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUIFloatingButtonLabel_srcCompat, Integer.MIN_VALUE);
                if (resourceId == Integer.MIN_VALUE) {
                    resourceId = obtainStyledAttributes.getResourceId(R$styleable.COUIFloatingButtonLabel_android_src, Integer.MIN_VALUE);
                }
                COUIFloatingButtonItem.b bVar = new COUIFloatingButtonItem.b(getId(), resourceId);
                bVar.l(obtainStyledAttributes.getString(R$styleable.COUIFloatingButtonLabel_fabLabel));
                bVar.k(ColorStateList.valueOf(obtainStyledAttributes.getColor(R$styleable.COUIFloatingButtonLabel_fabBackgroundColor, COUIContextUtil.b(getContext(), R$attr.couiColorPrimary, 0))));
                bVar.n(ColorStateList.valueOf(obtainStyledAttributes.getColor(R$styleable.COUIFloatingButtonLabel_fabLabelColor, Integer.MIN_VALUE)));
                bVar.m(ColorStateList.valueOf(obtainStyledAttributes.getColor(R$styleable.COUIFloatingButtonLabel_fabLabelBackgroundColor, Integer.MIN_VALUE)));
                setFloatingButtonItem(bVar.j());
            } catch (Exception e10) {
                Log.e(f6040p, "Failure setting FabWithLabelView icon" + e10.getMessage());
            }
            obtainStyledAttributes.recycle();
            setClipChildren(false);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void o() {
        LinearLayout.LayoutParams layoutParams;
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.coui_floating_button_fab_normal_size);
        getContext().getResources().getDimensionPixelSize(R$dimen.coui_floating_button_fab_side_margin);
        getContext().getResources().getDimensionPixelSize(R$dimen.coui_floating_button_item_normal_bottom_margin);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.f6046j.getLayoutParams();
        if (getOrientation() == 0) {
            layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 8388613;
        } else {
            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(dimensionPixelSize, -2);
            layoutParams3.gravity = 16;
            layoutParams2.setMargins(0, 0, 0, 0);
            layoutParams = layoutParams3;
        }
        setLayoutParams(layoutParams);
        this.f6046j.setLayoutParams(layoutParams2);
    }

    private void setFabBackgroundColor(ColorStateList colorStateList) {
        this.f6046j.setBackgroundTintList(colorStateList);
    }

    private void setFabIcon(Drawable drawable) {
        this.f6046j.setImageDrawable(drawable);
    }

    private void setLabel(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            this.f6045i.setText(charSequence);
            setLabelEnabled(getOrientation() == 0);
        } else {
            setLabelEnabled(false);
        }
    }

    private void setLabelBackgroundColor(ColorStateList colorStateList) {
        if (colorStateList == ColorStateList.valueOf(Integer.MIN_VALUE)) {
            this.f6047k.setCardBackgroundColor(0);
            this.f6051o = this.f6047k.getElevation();
            this.f6047k.setElevation(0.0f);
        } else {
            this.f6047k.setCardBackgroundColor(colorStateList);
            float f10 = this.f6051o;
            if (f10 != 0.0f) {
                this.f6047k.setElevation(f10);
                this.f6051o = 0.0f;
            }
        }
    }

    private void setLabelEnabled(boolean z10) {
        this.f6048l = z10;
        this.f6047k.setVisibility(z10 ? 0 : 8);
    }

    private void setLabelTextColor(ColorStateList colorStateList) {
        this.f6045i.setTextColor(colorStateList);
    }

    public ImageView getChildFloatingButton() {
        return this.f6046j;
    }

    public COUIFloatingButtonItem getFloatingButtonItem() {
        COUIFloatingButtonItem cOUIFloatingButtonItem = this.f6049m;
        if (cOUIFloatingButtonItem != null) {
            return cOUIFloatingButtonItem;
        }
        throw new IllegalStateException("SpeedDialActionItem not set yet!");
    }

    public COUIFloatingButtonItem.b getFloatingButtonItemBuilder() {
        return new COUIFloatingButtonItem.b(getFloatingButtonItem());
    }

    public CardView getFloatingButtonLabelBackground() {
        return this.f6047k;
    }

    public TextView getFloatingButtonLabelText() {
        return this.f6045i;
    }

    public boolean n() {
        return this.f6048l;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f6041e <= 0) {
            Context createConfigurationContext = getContext().createConfigurationContext(configuration);
            if (COUIResponsiveUtils.j(configuration.screenWidthDp)) {
                this.f6042f = createConfigurationContext.getResources().getDimensionPixelOffset(R$dimen.coui_floating_button_normal_size);
            } else {
                this.f6042f = createConfigurationContext.getResources().getDimensionPixelOffset(R$dimen.coui_floating_button_large_size);
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f6046j.getLayoutParams();
            int i10 = this.f6042f;
            layoutParams.width = i10;
            layoutParams.height = i10;
            this.f6046j.setLayoutParams(layoutParams);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        super.setEnabled(z10);
        this.f6045i.setEnabled(z10);
        this.f6046j.setEnabled(z10);
        this.f6047k.setEnabled(z10);
    }

    public void setFloatingButtonItem(COUIFloatingButtonItem cOUIFloatingButtonItem) {
        this.f6049m = cOUIFloatingButtonItem;
        setId(cOUIFloatingButtonItem.v());
        setLabel(cOUIFloatingButtonItem.w(getContext()));
        setFabIcon(cOUIFloatingButtonItem.u(getContext()));
        ColorStateList t7 = cOUIFloatingButtonItem.t();
        int color = getContext().getResources().getColor(com.support.appcompat.R$color.couiGreenTintControlNormal);
        int b10 = COUIContextUtil.b(getContext(), R$attr.couiColorPrimary, color);
        if (t7 == ColorStateList.valueOf(Integer.MIN_VALUE)) {
            t7 = COUIStateListUtil.a(b10, color);
        }
        setFabBackgroundColor(t7);
        ColorStateList y4 = cOUIFloatingButtonItem.y();
        if (y4 == ColorStateList.valueOf(Integer.MIN_VALUE)) {
            y4 = ResourcesCompat.e(getResources(), R$color.coui_floating_button_label_text_color, getContext().getTheme());
        }
        setLabelTextColor(y4);
        ColorStateList x10 = cOUIFloatingButtonItem.x();
        if (x10 == ColorStateList.valueOf(Integer.MIN_VALUE)) {
            x10 = COUIStateListUtil.a(b10, color);
        }
        setLabelBackgroundColor(x10);
        if (cOUIFloatingButtonItem.z()) {
            k();
        }
        getChildFloatingButton().setOnClickListener(new a());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMainButtonSize(int i10) {
        this.f6041e = i10;
        if (i10 > 0) {
            this.f6042f = i10;
        } else {
            this.f6042f = getResources().getDimensionPixelSize(R$dimen.coui_floating_button_size);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f6046j.getLayoutParams();
        int i11 = this.f6042f;
        layoutParams.width = i11;
        layoutParams.height = i11;
        this.f6046j.setLayoutParams(layoutParams);
    }

    public void setOnActionSelectedListener(COUIFloatingButton.l lVar) {
        this.f6050n = lVar;
        if (lVar != null) {
            getFloatingButtonLabelBackground().setOnClickListener(new b());
        } else {
            getChildFloatingButton().setOnClickListener(null);
            getFloatingButtonLabelBackground().setOnClickListener(null);
        }
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i10) {
        super.setOrientation(i10);
        o();
        if (i10 == 1) {
            setLabelEnabled(false);
        } else {
            setLabel(this.f6045i.getText().toString());
        }
    }

    @Override // android.view.View
    @SuppressLint({"RestrictedApi"})
    public void setVisibility(int i10) {
        super.setVisibility(i10);
        getChildFloatingButton().setVisibility(i10);
        if (n()) {
            getFloatingButtonLabelBackground().setVisibility(i10);
        }
    }

    public COUIFloatingButtonLabel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6041e = 0;
        m(context, attributeSet);
    }

    public COUIFloatingButtonLabel(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6041e = 0;
        m(context, attributeSet);
    }
}
