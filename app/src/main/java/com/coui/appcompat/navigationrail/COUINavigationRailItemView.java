package com.coui.appcompat.navigationrail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import com.google.android.material.R$id;
import com.google.android.material.navigation.NavigationBarItemView;
import com.support.bars.R$dimen;
import com.support.bars.R$layout;

@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class COUINavigationRailItemView extends NavigationBarItemView {
    private final TextView J;
    private final TextView K;
    private int L;

    public COUINavigationRailItemView(Context context) {
        super(context);
        this.J = (TextView) findViewById(R$id.navigation_bar_item_small_label_view);
        this.K = (TextView) findViewById(R$id.navigation_bar_item_large_label_view);
        setTextSize(context.getResources().getDimensionPixelSize(R$dimen.coui_navigation_item_text_size));
    }

    @Override // com.google.android.material.navigation.NavigationBarItemView
    protected int getItemDefaultMarginResId() {
        return R$dimen.coui_navigation_rail_item_icon_margin;
    }

    @Override // com.google.android.material.navigation.NavigationBarItemView
    protected int getItemLayoutResId() {
        return R$layout.coui_navigation_rail_item_layout;
    }

    @Override // android.view.View
    public boolean performLongClick() {
        return false;
    }

    public void setTextSize(int i10) {
        this.L = i10;
        this.J.setTextSize(0, i10);
        this.K.setTextSize(0, this.L);
    }
}
