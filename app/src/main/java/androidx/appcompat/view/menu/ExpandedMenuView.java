package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.TintTypedArray;

/* loaded from: classes.dex */
public final class ExpandedMenuView extends ListView implements MenuBuilder.b, MenuView, AdapterView.OnItemClickListener {

    /* renamed from: g, reason: collision with root package name */
    private static final int[] f635g = {R.attr.background, R.attr.divider};

    /* renamed from: e, reason: collision with root package name */
    private MenuBuilder f636e;

    /* renamed from: f, reason: collision with root package name */
    private int f637f;

    public ExpandedMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listViewStyle);
    }

    @Override // androidx.appcompat.view.menu.MenuBuilder.b
    public boolean a(MenuItemImpl menuItemImpl) {
        return this.f636e.performItemAction(menuItemImpl, 0);
    }

    public int getWindowAnimations() {
        return this.f637f;
    }

    @Override // androidx.appcompat.view.menu.MenuView
    public void initialize(MenuBuilder menuBuilder) {
        this.f636e = menuBuilder;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setChildrenDrawingCacheEnabled(false);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i10, long j10) {
        a((MenuItemImpl) getAdapter().getItem(i10));
    }

    public ExpandedMenuView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet);
        setOnItemClickListener(this);
        TintTypedArray w10 = TintTypedArray.w(context, attributeSet, f635g, i10, 0);
        if (w10.s(0)) {
            setBackgroundDrawable(w10.g(0));
        }
        if (w10.s(1)) {
            setDivider(w10.g(1));
        }
        w10.x();
    }
}
