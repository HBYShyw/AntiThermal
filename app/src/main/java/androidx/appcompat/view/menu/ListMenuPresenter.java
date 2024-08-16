package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import androidx.appcompat.R$layout;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import java.util.ArrayList;

/* compiled from: ListMenuPresenter.java */
/* renamed from: androidx.appcompat.view.menu.e, reason: use source file name */
/* loaded from: classes.dex */
public class ListMenuPresenter implements MenuPresenter, AdapterView.OnItemClickListener {

    /* renamed from: e, reason: collision with root package name */
    Context f719e;

    /* renamed from: f, reason: collision with root package name */
    LayoutInflater f720f;

    /* renamed from: g, reason: collision with root package name */
    MenuBuilder f721g;

    /* renamed from: h, reason: collision with root package name */
    ExpandedMenuView f722h;

    /* renamed from: i, reason: collision with root package name */
    int f723i;

    /* renamed from: j, reason: collision with root package name */
    int f724j;

    /* renamed from: k, reason: collision with root package name */
    int f725k;

    /* renamed from: l, reason: collision with root package name */
    private MenuPresenter.a f726l;

    /* renamed from: m, reason: collision with root package name */
    a f727m;

    /* renamed from: n, reason: collision with root package name */
    private int f728n;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ListMenuPresenter.java */
    /* renamed from: androidx.appcompat.view.menu.e$a */
    /* loaded from: classes.dex */
    public class a extends BaseAdapter {

        /* renamed from: e, reason: collision with root package name */
        private int f729e = -1;

        public a() {
            a();
        }

        void a() {
            MenuItemImpl expandedItem = ListMenuPresenter.this.f721g.getExpandedItem();
            if (expandedItem != null) {
                ArrayList<MenuItemImpl> nonActionItems = ListMenuPresenter.this.f721g.getNonActionItems();
                int size = nonActionItems.size();
                for (int i10 = 0; i10 < size; i10++) {
                    if (nonActionItems.get(i10) == expandedItem) {
                        this.f729e = i10;
                        return;
                    }
                }
            }
            this.f729e = -1;
        }

        @Override // android.widget.Adapter
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public MenuItemImpl getItem(int i10) {
            ArrayList<MenuItemImpl> nonActionItems = ListMenuPresenter.this.f721g.getNonActionItems();
            int i11 = i10 + ListMenuPresenter.this.f723i;
            int i12 = this.f729e;
            if (i12 >= 0 && i11 >= i12) {
                i11++;
            }
            return nonActionItems.get(i11);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int size = ListMenuPresenter.this.f721g.getNonActionItems().size() - ListMenuPresenter.this.f723i;
            return this.f729e < 0 ? size : size - 1;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            if (view == null) {
                ListMenuPresenter listMenuPresenter = ListMenuPresenter.this;
                view = listMenuPresenter.f720f.inflate(listMenuPresenter.f725k, viewGroup, false);
            }
            ((MenuView.a) view).initialize(getItem(i10), 0);
            return view;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            a();
            super.notifyDataSetChanged();
        }
    }

    public ListMenuPresenter(Context context, int i10) {
        this(i10, 0);
        this.f719e = context;
        this.f720f = LayoutInflater.from(context);
    }

    public ListAdapter a() {
        if (this.f727m == null) {
            this.f727m = new a();
        }
        return this.f727m;
    }

    public MenuView b(ViewGroup viewGroup) {
        if (this.f722h == null) {
            this.f722h = (ExpandedMenuView) this.f720f.inflate(R$layout.abc_expanded_menu_layout, viewGroup, false);
            if (this.f727m == null) {
                this.f727m = new a();
            }
            this.f722h.setAdapter((ListAdapter) this.f727m);
            this.f722h.setOnItemClickListener(this);
        }
        return this.f722h;
    }

    public void c(Bundle bundle) {
        SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
        if (sparseParcelableArray != null) {
            this.f722h.restoreHierarchyState(sparseParcelableArray);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public void d(Bundle bundle) {
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        ExpandedMenuView expandedMenuView = this.f722h;
        if (expandedMenuView != null) {
            expandedMenuView.saveHierarchyState(sparseArray);
        }
        bundle.putSparseParcelableArray("android:menu:list", sparseArray);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public int getId() {
        return this.f728n;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        if (this.f724j != 0) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, this.f724j);
            this.f719e = contextThemeWrapper;
            this.f720f = LayoutInflater.from(contextThemeWrapper);
        } else if (this.f719e != null) {
            this.f719e = context;
            if (this.f720f == null) {
                this.f720f = LayoutInflater.from(context);
            }
        }
        this.f721g = menuBuilder;
        a aVar = this.f727m;
        if (aVar != null) {
            aVar.notifyDataSetChanged();
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        MenuPresenter.a aVar = this.f726l;
        if (aVar != null) {
            aVar.onCloseMenu(menuBuilder, z10);
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
        this.f721g.performItemAction(this.f727m.getItem(i10), this, 0);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable parcelable) {
        c((Bundle) parcelable);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        if (this.f722h == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        d(bundle);
        return bundle;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        new MenuDialogHelper(subMenuBuilder).c(null);
        MenuPresenter.a aVar = this.f726l;
        if (aVar == null) {
            return true;
        }
        aVar.a(subMenuBuilder);
        return true;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void setCallback(MenuPresenter.a aVar) {
        this.f726l = aVar;
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void updateMenuView(boolean z10) {
        a aVar = this.f727m;
        if (aVar != null) {
            aVar.notifyDataSetChanged();
        }
    }

    public ListMenuPresenter(int i10, int i11) {
        this.f725k = i10;
        this.f724j = i11;
    }
}
