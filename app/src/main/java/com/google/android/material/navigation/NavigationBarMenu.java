package com.google.android.material.navigation;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;

/* compiled from: NavigationBarMenu.java */
/* renamed from: com.google.android.material.navigation.a, reason: use source file name */
/* loaded from: classes.dex */
public final class NavigationBarMenu extends MenuBuilder {

    /* renamed from: a, reason: collision with root package name */
    private final Class<?> f9027a;

    /* renamed from: b, reason: collision with root package name */
    private final int f9028b;

    public NavigationBarMenu(Context context, Class<?> cls, int i10) {
        super(context);
        this.f9027a = cls;
        this.f9028b = i10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.view.menu.MenuBuilder
    public MenuItem addInternal(int i10, int i11, int i12, CharSequence charSequence) {
        if (size() + 1 <= this.f9028b) {
            stopDispatchingItemsChanged();
            MenuItem addInternal = super.addInternal(i10, i11, i12, charSequence);
            if (addInternal instanceof MenuItemImpl) {
                ((MenuItemImpl) addInternal).s(true);
            }
            startDispatchingItemsChanged();
            return addInternal;
        }
        String simpleName = this.f9027a.getSimpleName();
        throw new IllegalArgumentException("Maximum number of items supported by " + simpleName + " is " + this.f9028b + ". Limit can be checked with " + simpleName + "#getMaxItemCount()");
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder, android.view.Menu
    public SubMenu addSubMenu(int i10, int i11, int i12, CharSequence charSequence) {
        throw new UnsupportedOperationException(this.f9027a.getSimpleName() + " does not support submenus");
    }
}
