package com.coui.appcompat.toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;

@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class COUIActionMenuItemView extends ActionMenuItemView {
    private int A;

    /* renamed from: w, reason: collision with root package name */
    private int f7956w;

    /* renamed from: x, reason: collision with root package name */
    private int f7957x;

    /* renamed from: y, reason: collision with root package name */
    private int f7958y;

    /* renamed from: z, reason: collision with root package name */
    private int f7959z;

    public COUIActionMenuItemView(Context context) {
        this(context, null);
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, androidx.appcompat.view.menu.MenuView.a
    public void initialize(MenuItemImpl menuItemImpl, int i10) {
        super.initialize(menuItemImpl, i10);
        boolean z10 = menuItemImpl.getIcon() == null;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = z10 ? -2 : -1;
        if (!z10 && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(this.A);
        }
        setLayoutParams(layoutParams);
        setBackgroundResource(z10 ? R$drawable.coui_toolbar_text_menu_bg : R$drawable.coui_toolbar_menu_bg);
        if (!z10) {
            int i11 = this.f7956w;
            int i12 = this.f7957x;
            setPadding(i11, i12, i11, i12);
        } else {
            int i13 = this.f7959z;
            int i14 = this.f7958y;
            setPadding(i13, i14, i13, i14);
        }
    }

    @Override // androidx.appcompat.view.menu.ActionMenuItemView, android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        MenuItemImpl itemData = getItemData();
        if (itemData == null || itemData.getIcon() == null) {
            return;
        }
        this.A = getContext().createConfigurationContext(configuration).getResources().getDimensionPixelSize(R$dimen.coui_action_menu_item_view_margin_end);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(this.A);
        }
    }

    public COUIActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public COUIActionMenuItemView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f7956w = context.getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_bg_padding_horizontal);
        this.f7957x = context.getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_bg_padding_vertical);
        this.f7959z = context.getResources().getDimensionPixelSize(R$dimen.coui_toolbar_text_menu_bg_padding_horizontal);
        this.f7958y = context.getResources().getDimensionPixelSize(R$dimen.coui_toolbar_text_menu_bg_padding_vertical);
        this.A = context.getResources().getDimensionPixelSize(R$dimen.coui_action_menu_item_view_margin_end);
    }
}
