package com.coui.appcompat.bottomnavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.support.bars.R$color;
import com.support.bars.R$dimen;
import java.util.ArrayList;

@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class COUINavigationMenuView extends BottomNavigationMenuView {
    private int O;
    private int P;
    private COUINavigationItemView Q;
    private int R;
    private int S;
    private int[] T;
    private int U;

    /* loaded from: classes.dex */
    class a implements View.OnLongClickListener {
        a() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return true;
        }
    }

    public COUINavigationMenuView(Context context) {
        super(context);
        this.R = -1;
        this.O = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_edge_item_padding);
        setClipChildren(false);
        setClipToPadding(false);
        this.T = new int[10];
    }

    private void u() {
        if (this.U == 1) {
            this.O = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_edge_item_default_padding);
            this.P = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_item_default_height);
        } else {
            this.O = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_edge_item_padding);
            this.P = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_item_height);
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        super.addView(view);
        view.setOnLongClickListener(new a());
    }

    @Override // com.google.android.material.bottomnavigation.BottomNavigationMenuView, com.google.android.material.navigation.NavigationBarMenuView
    protected NavigationBarItemView f(Context context) {
        COUINavigationItemView cOUINavigationItemView = new COUINavigationItemView(context);
        this.Q = cOUINavigationItemView;
        return cOUINavigationItemView;
    }

    public COUINavigationItemView getCOUINavigationItemView() {
        return this.Q;
    }

    public int getEnlargeId() {
        int i10 = this.R;
        return i10 == -1 ? i10 : getMenu().getVisibleItems().get(this.R).getItemId();
    }

    public ArrayList<MenuItemImpl> getVisibleItems() {
        return getMenu().getVisibleItems();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        u();
    }

    @Override // com.google.android.material.bottomnavigation.BottomNavigationMenuView, android.view.View
    protected void onMeasure(int i10, int i11) {
        int size = View.MeasureSpec.getSize(i10) - (this.O * 2);
        int size2 = getMenu().getVisibleItems().size();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.coui_tool_navigation_item_height);
        this.P = dimensionPixelSize;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824);
        int i12 = size / (size2 == 0 ? 1 : size2);
        int i13 = size - (i12 * size2);
        for (int i14 = 0; i14 < size2; i14++) {
            int[] iArr = this.T;
            iArr[i14] = i12;
            if (i13 > 0) {
                iArr[i14] = iArr[i14] + 1;
                i13--;
            }
        }
        int i15 = 0;
        int i16 = 0;
        for (int i17 = 0; i17 < getChildCount(); i17++) {
            View childAt = getChildAt(i17);
            if (childAt.getVisibility() != 8) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(this.T[i16], 1073741824), makeMeasureSpec);
                childAt.getLayoutParams().width = childAt.getMeasuredWidth();
                i15 += childAt.getMeasuredWidth();
                i16++;
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(i15, View.MeasureSpec.makeMeasureSpec(i15, 1073741824), 0), View.resolveSizeAndState(this.P, makeMeasureSpec, 0));
    }

    public void p() {
        for (int i10 = 0; i10 < getMenu().size(); i10++) {
            ((COUINavigationItemView) g(getMenu().getItem(i10).getItemId())).x();
        }
    }

    public MenuItemImpl q(int i10) {
        return getMenu().getVisibleItems().get(i10);
    }

    public void r() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        layoutParams.gravity = 81;
        setLayoutParams(layoutParams);
    }

    public void s(boolean z10, int i10) {
        this.R = i10;
        if (!z10 || i10 < 0) {
            return;
        }
        int i11 = 0;
        while (i11 < getMenu().getVisibleItems().size()) {
            NavigationBarItemView g6 = g(getMenu().getVisibleItems().get(i11).getItemId());
            if (g6 instanceof COUINavigationItemView) {
                ((COUINavigationItemView) g6).D(true, i11 == this.R);
            }
            i11++;
        }
    }

    public void setItemLayoutType(int i10) {
        this.U = i10;
        u();
        for (int i11 = 0; i11 < getMenu().size(); i11++) {
            NavigationBarItemView g6 = g(getMenu().getItem(i11).getItemId());
            if (g6 instanceof COUINavigationItemView) {
                ((COUINavigationItemView) g6).w(this.U);
            }
        }
    }

    public void setTextSize(int i10) {
        this.S = i10;
        if (getMenu() != null) {
            for (int i11 = 0; i11 < getMenu().size(); i11++) {
                NavigationBarItemView g6 = g(getMenu().getItem(i11).getItemId());
                if (g6 instanceof COUINavigationItemView) {
                    ((COUINavigationItemView) g6).setTextSize(this.S);
                }
            }
        }
    }

    public void t() {
        for (int i10 = 0; i10 < getMenu().size(); i10++) {
            NavigationBarItemView g6 = g(getMenu().getItem(i10).getItemId());
            g6.setTextColor(ColorStateList.valueOf(ContextCompat.c(getContext(), R$color.coui_navigation_enlarge_item_color)));
            if (g6 instanceof COUINavigationItemView) {
                ((COUINavigationItemView) g6).E();
            }
        }
    }
}
