package com.google.android.material.internal;

import android.content.Context;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;

/* loaded from: classes.dex */
public class NavigationMenu extends MenuBuilder {
    public NavigationMenu(Context context) {
        super(context);
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder, android.view.Menu
    public SubMenu addSubMenu(int i10, int i11, int i12, CharSequence charSequence) {
        MenuItemImpl menuItemImpl = (MenuItemImpl) addInternal(i10, i11, i12, charSequence);
        NavigationSubMenu navigationSubMenu = new NavigationSubMenu(getContext(), this, menuItemImpl);
        menuItemImpl.w(navigationSubMenu);
        return navigationSubMenu;
    }
}
