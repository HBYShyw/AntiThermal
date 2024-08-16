package com.coui.appcompat.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import c.AppCompatResources;
import com.support.panel.R$attr;
import com.support.panel.R$color;
import com.support.panel.R$dimen;
import com.support.panel.R$drawable;
import com.support.panel.R$style;
import com.support.panel.R$styleable;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUIDraggableVerticalLinearLayout extends LinearLayout {

    /* renamed from: e, reason: collision with root package name */
    private ImageView f6550e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f6551f;

    /* renamed from: g, reason: collision with root package name */
    private Drawable f6552g;

    /* renamed from: h, reason: collision with root package name */
    private int f6553h;

    /* renamed from: i, reason: collision with root package name */
    private int f6554i;

    /* renamed from: j, reason: collision with root package name */
    private float f6555j;

    /* renamed from: k, reason: collision with root package name */
    private int f6556k;

    /* renamed from: l, reason: collision with root package name */
    private int f6557l;

    /* renamed from: m, reason: collision with root package name */
    private int f6558m;

    /* renamed from: n, reason: collision with root package name */
    private int f6559n;

    public COUIDraggableVerticalLinearLayout(Context context) {
        this(context, null);
    }

    private void a(AttributeSet attributeSet, int i10, int i11) {
        setOrientation(1);
        this.f6550e = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R$dimen.coui_panel_drag_view_width), (int) getResources().getDimension(R$dimen.coui_panel_drag_view_height));
        layoutParams.gravity = 1;
        this.f6550e.setLayoutParams(layoutParams);
        COUIDarkModeUtil.b(this.f6550e, false);
        setDragViewByTypeArray(getContext().obtainStyledAttributes(attributeSet, R$styleable.COUIDraggableVerticalLinearLayout, i10, i11));
        if (attributeSet != null) {
            int styleAttribute = attributeSet.getStyleAttribute();
            this.f6554i = styleAttribute;
            if (styleAttribute == 0) {
                this.f6554i = i10;
            }
        } else {
            this.f6554i = i10;
        }
        b();
        addView(this.f6550e);
    }

    private void b() {
        this.f6555j = getElevation();
        this.f6556k = getPaddingLeft();
        this.f6557l = getPaddingTop();
        this.f6558m = getPaddingRight();
        this.f6559n = getPaddingBottom();
    }

    private void setDragViewByTypeArray(TypedArray typedArray) {
        if (typedArray != null) {
            this.f6551f = typedArray.getBoolean(R$styleable.COUIDraggableVerticalLinearLayout_hasShadowNinePatchDrawable, false);
            int resourceId = typedArray.getResourceId(R$styleable.COUIDraggableVerticalLinearLayout_dragViewIcon, R$drawable.coui_panel_drag_view);
            int color = typedArray.getColor(R$styleable.COUIDraggableVerticalLinearLayout_dragViewTintColor, getContext().getResources().getColor(R$color.coui_panel_drag_view_color));
            typedArray.recycle();
            Drawable b10 = AppCompatResources.b(getContext(), resourceId);
            if (b10 != null) {
                b10.setTint(color);
                this.f6550e.setImageDrawable(b10);
            }
            if (this.f6551f) {
                setBackground(getContext().getDrawable(R$drawable.coui_panel_bg_with_shadow));
            } else {
                setBackground(getContext().getDrawable(R$drawable.coui_panel_bg_without_shadow));
            }
        }
    }

    public ImageView getDragView() {
        return this.f6550e;
    }

    public void setDragViewDrawable(Drawable drawable) {
        if (drawable != null) {
            this.f6552g = drawable;
            this.f6550e.setImageDrawable(drawable);
        }
    }

    public void setDragViewDrawableTintColor(int i10) {
        Drawable drawable = this.f6552g;
        if (drawable == null || this.f6553h == i10) {
            return;
        }
        this.f6553h = i10;
        drawable.setTint(i10);
        this.f6550e.setImageDrawable(this.f6552g);
    }

    @Deprecated
    public void setHasShadowNinePatchDrawable(boolean z10) {
        this.f6551f = z10;
        if (z10) {
            setBackground(getContext().getDrawable(R$drawable.coui_panel_bg_with_shadow));
            setElevation(0.0f);
        } else {
            setBackground(getContext().getDrawable(R$drawable.coui_panel_bg_without_shadow));
            setPadding(this.f6556k, this.f6557l, this.f6558m, this.f6559n);
            setElevation(this.f6555j);
        }
        invalidate();
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i10) {
        super.setOrientation(1);
    }

    public COUIDraggableVerticalLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.couiDraggableVerticalLinearLayoutStyle);
    }

    public COUIDraggableVerticalLinearLayout(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, COUIContextUtil.e(context) ? R$style.COUIDraggableVerticalLinearLayout_Dark : R$style.COUIDraggableVerticalLinearLayout);
    }

    public COUIDraggableVerticalLinearLayout(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f6551f = false;
        this.f6555j = 0.0f;
        this.f6556k = 0;
        this.f6557l = 0;
        this.f6558m = 0;
        this.f6559n = 0;
        a(attributeSet, i10, i11);
    }
}
