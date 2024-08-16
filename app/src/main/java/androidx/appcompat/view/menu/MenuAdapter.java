package androidx.appcompat.view.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.appcompat.view.menu.MenuView;
import java.util.ArrayList;

/* compiled from: MenuAdapter.java */
/* renamed from: androidx.appcompat.view.menu.f, reason: use source file name */
/* loaded from: classes.dex */
public class MenuAdapter extends BaseAdapter {

    /* renamed from: e, reason: collision with root package name */
    MenuBuilder f731e;

    /* renamed from: f, reason: collision with root package name */
    private int f732f = -1;

    /* renamed from: g, reason: collision with root package name */
    private boolean f733g;

    /* renamed from: h, reason: collision with root package name */
    private final boolean f734h;

    /* renamed from: i, reason: collision with root package name */
    private final LayoutInflater f735i;

    /* renamed from: j, reason: collision with root package name */
    private final int f736j;

    public MenuAdapter(MenuBuilder menuBuilder, LayoutInflater layoutInflater, boolean z10, int i10) {
        this.f734h = z10;
        this.f735i = layoutInflater;
        this.f731e = menuBuilder;
        this.f736j = i10;
        a();
    }

    void a() {
        MenuItemImpl expandedItem = this.f731e.getExpandedItem();
        if (expandedItem != null) {
            ArrayList<MenuItemImpl> nonActionItems = this.f731e.getNonActionItems();
            int size = nonActionItems.size();
            for (int i10 = 0; i10 < size; i10++) {
                if (nonActionItems.get(i10) == expandedItem) {
                    this.f732f = i10;
                    return;
                }
            }
        }
        this.f732f = -1;
    }

    public MenuBuilder b() {
        return this.f731e;
    }

    @Override // android.widget.Adapter
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public MenuItemImpl getItem(int i10) {
        ArrayList<MenuItemImpl> nonActionItems = this.f734h ? this.f731e.getNonActionItems() : this.f731e.getVisibleItems();
        int i11 = this.f732f;
        if (i11 >= 0 && i10 >= i11) {
            i10++;
        }
        return nonActionItems.get(i10);
    }

    public void d(boolean z10) {
        this.f733g = z10;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        ArrayList<MenuItemImpl> nonActionItems = this.f734h ? this.f731e.getNonActionItems() : this.f731e.getVisibleItems();
        if (this.f732f < 0) {
            return nonActionItems.size();
        }
        return nonActionItems.size() - 1;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        return i10;
    }

    @Override // android.widget.Adapter
    public View getView(int i10, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.f735i.inflate(this.f736j, viewGroup, false);
        }
        int groupId = getItem(i10).getGroupId();
        int i11 = i10 - 1;
        ListMenuItemView listMenuItemView = (ListMenuItemView) view;
        listMenuItemView.setGroupDividerEnabled(this.f731e.isGroupDividerEnabled() && groupId != (i11 >= 0 ? getItem(i11).getGroupId() : groupId));
        MenuView.a aVar = (MenuView.a) view;
        if (this.f733g) {
            listMenuItemView.setForceShowIcon(true);
        }
        aVar.initialize(getItem(i10), 0);
        return view;
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        a();
        super.notifyDataSetChanged();
    }
}
