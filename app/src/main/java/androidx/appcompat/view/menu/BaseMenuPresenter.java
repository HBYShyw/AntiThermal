package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import java.util.ArrayList;

/* compiled from: BaseMenuPresenter.java */
/* renamed from: androidx.appcompat.view.menu.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseMenuPresenter implements MenuPresenter {

    /* renamed from: e, reason: collision with root package name */
    protected Context f675e;

    /* renamed from: f, reason: collision with root package name */
    protected Context f676f;

    /* renamed from: g, reason: collision with root package name */
    protected MenuBuilder f677g;

    /* renamed from: h, reason: collision with root package name */
    protected LayoutInflater f678h;

    /* renamed from: i, reason: collision with root package name */
    protected LayoutInflater f679i;

    /* renamed from: j, reason: collision with root package name */
    private MenuPresenter.a f680j;

    /* renamed from: k, reason: collision with root package name */
    private int f681k;

    /* renamed from: l, reason: collision with root package name */
    private int f682l;

    /* renamed from: m, reason: collision with root package name */
    protected MenuView f683m;

    /* renamed from: n, reason: collision with root package name */
    private int f684n;

    public BaseMenuPresenter(Context context, int i10, int i11) {
        this.f675e = context;
        this.f678h = LayoutInflater.from(context);
        this.f681k = i10;
        this.f682l = i11;
    }

    protected void b(View view, int i10) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        ((ViewGroup) this.f683m).addView(view, i10);
    }

    public abstract void c(MenuItemImpl menuItemImpl, MenuView.a aVar);

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public MenuView.a d(ViewGroup viewGroup) {
        return (MenuView.a) this.f678h.inflate(this.f682l, viewGroup, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean e(ViewGroup viewGroup, int i10) {
        viewGroup.removeViewAt(i10);
        return true;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public MenuPresenter.a f() {
        return this.f680j;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public View g(MenuItemImpl menuItemImpl, View view, ViewGroup viewGroup) {
        MenuView.a aVar;
        if (view instanceof MenuView.a) {
            aVar = (MenuView.a) view;
        } else {
            aVar = d(viewGroup);
        }
        c(menuItemImpl, aVar);
        return (View) aVar;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public int getId() {
        return this.f684n;
    }

    public MenuView h(ViewGroup viewGroup) {
        if (this.f683m == null) {
            MenuView menuView = (MenuView) this.f678h.inflate(this.f681k, viewGroup, false);
            this.f683m = menuView;
            menuView.initialize(this.f677g);
            updateMenuView(true);
        }
        return this.f683m;
    }

    public void i(int i10) {
        this.f684n = i10;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.f676f = context;
        this.f679i = LayoutInflater.from(context);
        this.f677g = menuBuilder;
    }

    public abstract boolean j(int i10, MenuItemImpl menuItemImpl);

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        MenuPresenter.a aVar = this.f680j;
        if (aVar != null) {
            aVar.onCloseMenu(menuBuilder, z10);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [androidx.appcompat.view.menu.g] */
    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        MenuPresenter.a aVar = this.f680j;
        SubMenuBuilder subMenuBuilder2 = subMenuBuilder;
        if (aVar == null) {
            return false;
        }
        if (subMenuBuilder == null) {
            subMenuBuilder2 = this.f677g;
        }
        return aVar.a(subMenuBuilder2);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.a aVar) {
        this.f680j = aVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void updateMenuView(boolean z10) {
        ViewGroup viewGroup = (ViewGroup) this.f683m;
        if (viewGroup == null) {
            return;
        }
        MenuBuilder menuBuilder = this.f677g;
        int i10 = 0;
        if (menuBuilder != null) {
            menuBuilder.flagActionItems();
            ArrayList<MenuItemImpl> visibleItems = this.f677g.getVisibleItems();
            int size = visibleItems.size();
            int i11 = 0;
            for (int i12 = 0; i12 < size; i12++) {
                MenuItemImpl menuItemImpl = visibleItems.get(i12);
                if (j(i11, menuItemImpl)) {
                    View childAt = viewGroup.getChildAt(i11);
                    MenuItemImpl itemData = childAt instanceof MenuView.a ? ((MenuView.a) childAt).getItemData() : null;
                    View g6 = g(menuItemImpl, childAt, viewGroup);
                    if (menuItemImpl != itemData) {
                        g6.setPressed(false);
                        g6.jumpDrawablesToCurrentState();
                    }
                    if (g6 != childAt) {
                        b(g6, i11);
                    }
                    i11++;
                }
            }
            i10 = i11;
        }
        while (i10 < viewGroup.getChildCount()) {
            if (!e(viewGroup, i10)) {
                i10++;
            }
        }
    }
}
