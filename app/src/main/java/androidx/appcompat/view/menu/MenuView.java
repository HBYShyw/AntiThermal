package androidx.appcompat.view.menu;

/* compiled from: MenuView.java */
/* renamed from: androidx.appcompat.view.menu.n, reason: use source file name */
/* loaded from: classes.dex */
public interface MenuView {

    /* compiled from: MenuView.java */
    /* renamed from: androidx.appcompat.view.menu.n$a */
    /* loaded from: classes.dex */
    public interface a {
        MenuItemImpl getItemData();

        void initialize(MenuItemImpl menuItemImpl, int i10);

        boolean prefersCondensedTitle();
    }

    void initialize(MenuBuilder menuBuilder);
}
