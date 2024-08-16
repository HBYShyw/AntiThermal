package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import j.SimpleArrayMap;
import p.SupportMenuItem;
import p.SupportSubMenu;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BaseMenuWrapper.java */
/* renamed from: androidx.appcompat.view.menu.c, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseMenuWrapper {

    /* renamed from: a, reason: collision with root package name */
    final Context f685a;

    /* renamed from: b, reason: collision with root package name */
    private SimpleArrayMap<SupportMenuItem, MenuItem> f686b;

    /* renamed from: c, reason: collision with root package name */
    private SimpleArrayMap<SupportSubMenu, SubMenu> f687c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseMenuWrapper(Context context) {
        this.f685a = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final MenuItem c(MenuItem menuItem) {
        if (!(menuItem instanceof SupportMenuItem)) {
            return menuItem;
        }
        SupportMenuItem supportMenuItem = (SupportMenuItem) menuItem;
        if (this.f686b == null) {
            this.f686b = new SimpleArrayMap<>();
        }
        MenuItem menuItem2 = this.f686b.get(supportMenuItem);
        if (menuItem2 != null) {
            return menuItem2;
        }
        MenuItemWrapperICS menuItemWrapperICS = new MenuItemWrapperICS(this.f685a, supportMenuItem);
        this.f686b.put(supportMenuItem, menuItemWrapperICS);
        return menuItemWrapperICS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SubMenu d(SubMenu subMenu) {
        if (!(subMenu instanceof SupportSubMenu)) {
            return subMenu;
        }
        SupportSubMenu supportSubMenu = (SupportSubMenu) subMenu;
        if (this.f687c == null) {
            this.f687c = new SimpleArrayMap<>();
        }
        SubMenu subMenu2 = this.f687c.get(supportSubMenu);
        if (subMenu2 != null) {
            return subMenu2;
        }
        SubMenuWrapperICS subMenuWrapperICS = new SubMenuWrapperICS(this.f685a, supportSubMenu);
        this.f687c.put(supportSubMenu, subMenuWrapperICS);
        return subMenuWrapperICS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
        SimpleArrayMap<SupportMenuItem, MenuItem> simpleArrayMap = this.f686b;
        if (simpleArrayMap != null) {
            simpleArrayMap.clear();
        }
        SimpleArrayMap<SupportSubMenu, SubMenu> simpleArrayMap2 = this.f687c;
        if (simpleArrayMap2 != null) {
            simpleArrayMap2.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void f(int i10) {
        if (this.f686b == null) {
            return;
        }
        int i11 = 0;
        while (i11 < this.f686b.size()) {
            if (this.f686b.j(i11).getGroupId() == i10) {
                this.f686b.l(i11);
                i11--;
            }
            i11++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void g(int i10) {
        if (this.f686b == null) {
            return;
        }
        for (int i11 = 0; i11 < this.f686b.size(); i11++) {
            if (this.f686b.j(i11).getItemId() == i10) {
                this.f686b.l(i11);
                return;
            }
        }
    }
}
