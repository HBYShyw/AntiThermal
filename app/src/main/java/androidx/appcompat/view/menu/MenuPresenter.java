package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Parcelable;

/* compiled from: MenuPresenter.java */
/* renamed from: androidx.appcompat.view.menu.m, reason: use source file name */
/* loaded from: classes.dex */
public interface MenuPresenter {

    /* compiled from: MenuPresenter.java */
    /* renamed from: androidx.appcompat.view.menu.m$a */
    /* loaded from: classes.dex */
    public interface a {
        boolean a(MenuBuilder menuBuilder);

        void onCloseMenu(MenuBuilder menuBuilder, boolean z10);
    }

    boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl);

    boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl);

    boolean flagActionItems();

    int getId();

    void initForMenu(Context context, MenuBuilder menuBuilder);

    void onCloseMenu(MenuBuilder menuBuilder, boolean z10);

    void onRestoreInstanceState(Parcelable parcelable);

    Parcelable onSaveInstanceState();

    boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder);

    void setCallback(a aVar);

    void updateMenuView(boolean z10);
}
