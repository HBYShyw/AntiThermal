package com.coui.appcompat.navigationrail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.navigationrail.NavigationRailMenuView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.support.appcompat.R$attr;
import com.support.bars.R$dimen;
import com.support.bars.R$drawable;
import com.support.bars.R$style;
import com.support.bars.R$styleable;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class COUINavigationRailView extends NavigationRailView {

    /* renamed from: p, reason: collision with root package name */
    private View f6492p;

    public COUINavigationRailView(Context context) {
        this(context, null);
    }

    private void q(Context context) {
        View view = new View(context);
        this.f6492p = view;
        COUIDarkModeUtil.b(view, false);
        this.f6492p.setBackgroundColor(COUIContextUtil.a(context, R$attr.couiColorDivider));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R$dimen.coui_navigation_shadow_height), -1);
        layoutParams.gravity = 8388613;
        this.f6492p.setLayoutParams(layoutParams);
        addView(this.f6492p);
    }

    public View getDividerView() {
        return this.f6492p;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.navigationrail.NavigationRailView, com.google.android.material.navigation.NavigationBarView
    /* renamed from: l */
    public NavigationRailMenuView d(Context context) {
        return new COUINavigationRailMenuView(context);
    }

    public COUINavigationRailView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, com.google.android.material.R$attr.navigationRailStyle);
    }

    public COUINavigationRailView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, R$style.Widget_COUI_COUINavigationRailView);
    }

    @SuppressLint({"RestrictedApi"})
    public COUINavigationRailView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, R$styleable.COUINavigationRailView, i10, i11);
        if (w10.k(R$styleable.COUINavigationRailView_navigationRailType, 0) == 0) {
            setBackgroundResource(R$drawable.coui_bottom_tool_navigation_item_bg);
        }
        setElevation(0.0f);
        w10.x();
        q(context);
    }
}
