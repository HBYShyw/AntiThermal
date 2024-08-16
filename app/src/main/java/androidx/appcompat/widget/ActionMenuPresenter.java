package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$layout;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.BaseMenuPresenter;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.a {
    private int A;
    private final SparseBooleanArray B;
    e C;
    a D;
    c E;
    private b F;
    final f G;
    int H;

    /* renamed from: o, reason: collision with root package name */
    d f867o;

    /* renamed from: p, reason: collision with root package name */
    private Drawable f868p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f869q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f870r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f871s;

    /* renamed from: t, reason: collision with root package name */
    private int f872t;

    /* renamed from: u, reason: collision with root package name */
    private int f873u;

    /* renamed from: v, reason: collision with root package name */
    private int f874v;

    /* renamed from: w, reason: collision with root package name */
    private boolean f875w;

    /* renamed from: x, reason: collision with root package name */
    private boolean f876x;

    /* renamed from: y, reason: collision with root package name */
    private boolean f877y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f878z;

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        public int f879e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        SavedState() {
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeInt(this.f879e);
        }

        SavedState(Parcel parcel) {
            this.f879e = parcel.readInt();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class a extends MenuPopupHelper {
        public a(Context context, SubMenuBuilder subMenuBuilder, View view) {
            super(context, subMenuBuilder, view, false, R$attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl) subMenuBuilder.getItem()).l()) {
                View view2 = ActionMenuPresenter.this.f867o;
                f(view2 == null ? (View) ((BaseMenuPresenter) ActionMenuPresenter.this).f683m : view2);
            }
            j(ActionMenuPresenter.this.G);
        }

        @Override // androidx.appcompat.view.menu.MenuPopupHelper
        protected void e() {
            ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
            actionMenuPresenter.D = null;
            actionMenuPresenter.H = 0;
            super.e();
        }
    }

    /* loaded from: classes.dex */
    private class b extends ActionMenuItemView.b {
        b() {
        }

        @Override // androidx.appcompat.view.menu.ActionMenuItemView.b
        public ShowableListMenu a() {
            a aVar = ActionMenuPresenter.this.D;
            if (aVar != null) {
                return aVar.c();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private e f882e;

        public c(e eVar) {
            this.f882e = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (((BaseMenuPresenter) ActionMenuPresenter.this).f677g != null) {
                ((BaseMenuPresenter) ActionMenuPresenter.this).f677g.changeMenuMode();
            }
            View view = (View) ((BaseMenuPresenter) ActionMenuPresenter.this).f683m;
            if (view != null && view.getWindowToken() != null && this.f882e.m()) {
                ActionMenuPresenter.this.C = this.f882e;
            }
            ActionMenuPresenter.this.E = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d extends AppCompatImageView implements ActionMenuView.a {

        /* loaded from: classes.dex */
        class a extends ForwardingListener {

            /* renamed from: n, reason: collision with root package name */
            final /* synthetic */ ActionMenuPresenter f885n;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(View view, ActionMenuPresenter actionMenuPresenter) {
                super(view);
                this.f885n = actionMenuPresenter;
            }

            @Override // androidx.appcompat.widget.ForwardingListener
            public ShowableListMenu b() {
                e eVar = ActionMenuPresenter.this.C;
                if (eVar == null) {
                    return null;
                }
                return eVar.c();
            }

            @Override // androidx.appcompat.widget.ForwardingListener
            public boolean c() {
                ActionMenuPresenter.this.D();
                return true;
            }

            @Override // androidx.appcompat.widget.ForwardingListener
            public boolean d() {
                ActionMenuPresenter actionMenuPresenter = ActionMenuPresenter.this;
                if (actionMenuPresenter.E != null) {
                    return false;
                }
                actionMenuPresenter.u();
                return true;
            }
        }

        public d(Context context) {
            super(context, null, R$attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            TooltipCompat.a(this, getContentDescription());
            setOnTouchListener(new a(this, ActionMenuPresenter.this));
        }

        @Override // androidx.appcompat.widget.ActionMenuView.a
        public boolean a() {
            return false;
        }

        @Override // androidx.appcompat.widget.ActionMenuView.a
        public boolean b() {
            return false;
        }

        @Override // android.view.View
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(0);
            ActionMenuPresenter.this.D();
            return true;
        }

        @Override // android.widget.ImageView
        protected boolean setFrame(int i10, int i11, int i12, int i13) {
            boolean frame = super.setFrame(i10, i11, i12, i13);
            Drawable drawable = getDrawable();
            Drawable background = getBackground();
            if (drawable != null && background != null) {
                int width = getWidth();
                int height = getHeight();
                int max = Math.max(width, height) / 2;
                int paddingLeft = (width + (getPaddingLeft() - getPaddingRight())) / 2;
                int paddingTop = (height + (getPaddingTop() - getPaddingBottom())) / 2;
                DrawableCompat.f(background, paddingLeft - max, paddingTop - max, paddingLeft + max, paddingTop + max);
            }
            return frame;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class e extends MenuPopupHelper {
        public e(Context context, MenuBuilder menuBuilder, View view, boolean z10) {
            super(context, menuBuilder, view, z10, R$attr.actionOverflowMenuStyle);
            h(8388613);
            j(ActionMenuPresenter.this.G);
        }

        @Override // androidx.appcompat.view.menu.MenuPopupHelper
        protected void e() {
            if (((BaseMenuPresenter) ActionMenuPresenter.this).f677g != null) {
                ((BaseMenuPresenter) ActionMenuPresenter.this).f677g.close();
            }
            ActionMenuPresenter.this.C = null;
            super.e();
        }
    }

    /* loaded from: classes.dex */
    private class f implements MenuPresenter.a {
        f() {
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public boolean a(MenuBuilder menuBuilder) {
            if (menuBuilder == ((BaseMenuPresenter) ActionMenuPresenter.this).f677g) {
                return false;
            }
            ActionMenuPresenter.this.H = ((SubMenuBuilder) menuBuilder).getItem().getItemId();
            MenuPresenter.a f10 = ActionMenuPresenter.this.f();
            if (f10 != null) {
                return f10.a(menuBuilder);
            }
            return false;
        }

        @Override // androidx.appcompat.view.menu.MenuPresenter.a
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
            if (menuBuilder instanceof SubMenuBuilder) {
                menuBuilder.getRootMenu().close(false);
            }
            MenuPresenter.a f10 = ActionMenuPresenter.this.f();
            if (f10 != null) {
                f10.onCloseMenu(menuBuilder, z10);
            }
        }
    }

    public ActionMenuPresenter(Context context) {
        super(context, R$layout.abc_action_menu_layout, R$layout.abc_action_menu_item_layout);
        this.B = new SparseBooleanArray();
        this.G = new f();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private View s(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup) this.f683m;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = viewGroup.getChildAt(i10);
            if ((childAt instanceof MenuView.a) && ((MenuView.a) childAt).getItemData() == menuItem) {
                return childAt;
            }
        }
        return null;
    }

    public void A(ActionMenuView actionMenuView) {
        this.f683m = actionMenuView;
        actionMenuView.initialize(this.f677g);
    }

    public void B(Drawable drawable) {
        d dVar = this.f867o;
        if (dVar != null) {
            dVar.setImageDrawable(drawable);
        } else {
            this.f869q = true;
            this.f868p = drawable;
        }
    }

    public void C(boolean z10) {
        this.f870r = z10;
        this.f871s = true;
    }

    public boolean D() {
        MenuBuilder menuBuilder;
        if (!this.f870r || x() || (menuBuilder = this.f677g) == null || this.f683m == null || this.E != null || menuBuilder.getNonActionItems().isEmpty()) {
            return false;
        }
        c cVar = new c(new e(this.f676f, this.f677g, this.f867o, true));
        this.E = cVar;
        ((View) this.f683m).post(cVar);
        return true;
    }

    @Override // androidx.core.view.ActionProvider.a
    public void a(boolean z10) {
        if (z10) {
            super.onSubMenuSelected(null);
            return;
        }
        MenuBuilder menuBuilder = this.f677g;
        if (menuBuilder != null) {
            menuBuilder.close(false);
        }
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public void c(MenuItemImpl menuItemImpl, MenuView.a aVar) {
        aVar.initialize(menuItemImpl, 0);
        ActionMenuItemView actionMenuItemView = (ActionMenuItemView) aVar;
        actionMenuItemView.setItemInvoker((ActionMenuView) this.f683m);
        if (this.F == null) {
            this.F = new b();
        }
        actionMenuItemView.setPopupCallback(this.F);
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public boolean e(ViewGroup viewGroup, int i10) {
        if (viewGroup.getChildAt(i10) == this.f867o) {
            return false;
        }
        return super.e(viewGroup, i10);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v1, types: [int] */
    /* JADX WARN: Type inference failed for: r3v12 */
    @Override // androidx.appcompat.view.menu.MenuPresenter
    public boolean flagActionItems() {
        ArrayList<MenuItemImpl> arrayList;
        int i10;
        int i11;
        int i12;
        boolean z10;
        int i13;
        ActionMenuPresenter actionMenuPresenter = this;
        MenuBuilder menuBuilder = actionMenuPresenter.f677g;
        View view = null;
        ?? r32 = 0;
        if (menuBuilder != null) {
            arrayList = menuBuilder.getVisibleItems();
            i10 = arrayList.size();
        } else {
            arrayList = null;
            i10 = 0;
        }
        int i14 = actionMenuPresenter.f874v;
        int i15 = actionMenuPresenter.f873u;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup viewGroup = (ViewGroup) actionMenuPresenter.f683m;
        boolean z11 = false;
        int i16 = 0;
        int i17 = 0;
        for (int i18 = 0; i18 < i10; i18++) {
            MenuItemImpl menuItemImpl = arrayList.get(i18);
            if (menuItemImpl.requiresActionButton()) {
                i16++;
            } else if (menuItemImpl.n()) {
                i17++;
            } else {
                z11 = true;
            }
            if (actionMenuPresenter.f878z && menuItemImpl.isActionViewExpanded()) {
                i14 = 0;
            }
        }
        if (actionMenuPresenter.f870r && (z11 || i17 + i16 > i14)) {
            i14--;
        }
        int i19 = i14 - i16;
        SparseBooleanArray sparseBooleanArray = actionMenuPresenter.B;
        sparseBooleanArray.clear();
        if (actionMenuPresenter.f876x) {
            int i20 = actionMenuPresenter.A;
            i12 = i15 / i20;
            i11 = i20 + ((i15 % i20) / i12);
        } else {
            i11 = 0;
            i12 = 0;
        }
        int i21 = 0;
        int i22 = 0;
        while (i21 < i10) {
            MenuItemImpl menuItemImpl2 = arrayList.get(i21);
            if (menuItemImpl2.requiresActionButton()) {
                View g6 = actionMenuPresenter.g(menuItemImpl2, view, viewGroup);
                if (actionMenuPresenter.f876x) {
                    i12 -= ActionMenuView.l(g6, i11, i12, makeMeasureSpec, r32);
                } else {
                    g6.measure(makeMeasureSpec, makeMeasureSpec);
                }
                int measuredWidth = g6.getMeasuredWidth();
                i15 -= measuredWidth;
                if (i22 == 0) {
                    i22 = measuredWidth;
                }
                int groupId = menuItemImpl2.getGroupId();
                if (groupId != 0) {
                    sparseBooleanArray.put(groupId, true);
                }
                menuItemImpl2.t(true);
                z10 = r32;
                i13 = i10;
            } else if (menuItemImpl2.n()) {
                int groupId2 = menuItemImpl2.getGroupId();
                boolean z12 = sparseBooleanArray.get(groupId2);
                boolean z13 = (i19 > 0 || z12) && i15 > 0 && (!actionMenuPresenter.f876x || i12 > 0);
                boolean z14 = z13;
                i13 = i10;
                if (z13) {
                    View g10 = actionMenuPresenter.g(menuItemImpl2, null, viewGroup);
                    if (actionMenuPresenter.f876x) {
                        int l10 = ActionMenuView.l(g10, i11, i12, makeMeasureSpec, 0);
                        i12 -= l10;
                        if (l10 == 0) {
                            z14 = false;
                        }
                    } else {
                        g10.measure(makeMeasureSpec, makeMeasureSpec);
                    }
                    boolean z15 = z14;
                    int measuredWidth2 = g10.getMeasuredWidth();
                    i15 -= measuredWidth2;
                    if (i22 == 0) {
                        i22 = measuredWidth2;
                    }
                    z13 = z15 & (!actionMenuPresenter.f876x ? i15 + i22 <= 0 : i15 < 0);
                }
                if (z13 && groupId2 != 0) {
                    sparseBooleanArray.put(groupId2, true);
                } else if (z12) {
                    sparseBooleanArray.put(groupId2, false);
                    for (int i23 = 0; i23 < i21; i23++) {
                        MenuItemImpl menuItemImpl3 = arrayList.get(i23);
                        if (menuItemImpl3.getGroupId() == groupId2) {
                            if (menuItemImpl3.l()) {
                                i19++;
                            }
                            menuItemImpl3.t(false);
                        }
                    }
                }
                if (z13) {
                    i19--;
                }
                menuItemImpl2.t(z13);
                z10 = false;
            } else {
                z10 = r32;
                i13 = i10;
                menuItemImpl2.t(z10);
            }
            i21++;
            r32 = z10;
            i10 = i13;
            view = null;
            actionMenuPresenter = this;
        }
        return true;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public View g(MenuItemImpl menuItemImpl, View view, ViewGroup viewGroup) {
        View actionView = menuItemImpl.getActionView();
        if (actionView == null || menuItemImpl.j()) {
            actionView = super.g(menuItemImpl, view, viewGroup);
        }
        actionView.setVisibility(menuItemImpl.isActionViewExpanded() ? 8 : 0);
        ActionMenuView actionMenuView = (ActionMenuView) viewGroup;
        ViewGroup.LayoutParams layoutParams = actionView.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            actionView.setLayoutParams(actionMenuView.generateLayoutParams(layoutParams));
        }
        return actionView;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public MenuView h(ViewGroup viewGroup) {
        MenuView menuView = this.f683m;
        MenuView h10 = super.h(viewGroup);
        if (menuView != h10) {
            ((ActionMenuView) h10).setPresenter(this);
        }
        return h10;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        super.initForMenu(context, menuBuilder);
        Resources resources = context.getResources();
        ActionBarPolicy b10 = ActionBarPolicy.b(context);
        if (!this.f871s) {
            this.f870r = b10.h();
        }
        if (!this.f877y) {
            this.f872t = b10.c();
        }
        if (!this.f875w) {
            this.f874v = b10.d();
        }
        int i10 = this.f872t;
        if (this.f870r) {
            if (this.f867o == null) {
                d dVar = new d(this.f675e);
                this.f867o = dVar;
                if (this.f869q) {
                    dVar.setImageDrawable(this.f868p);
                    this.f868p = null;
                    this.f869q = false;
                }
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                this.f867o.measure(makeMeasureSpec, makeMeasureSpec);
            }
            i10 -= this.f867o.getMeasuredWidth();
        } else {
            this.f867o = null;
        }
        this.f873u = i10;
        this.A = (int) (resources.getDisplayMetrics().density * 56.0f);
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter
    public boolean j(int i10, MenuItemImpl menuItemImpl) {
        return menuItemImpl.l();
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z10) {
        r();
        super.onCloseMenu(menuBuilder, z10);
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public void onRestoreInstanceState(Parcelable parcelable) {
        int i10;
        MenuItem findItem;
        if ((parcelable instanceof SavedState) && (i10 = ((SavedState) parcelable).f879e) > 0 && (findItem = this.f677g.findItem(i10)) != null) {
            onSubMenuSelected((SubMenuBuilder) findItem.getSubMenu());
        }
    }

    @Override // androidx.appcompat.view.menu.MenuPresenter
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.f879e = this.H;
        return savedState;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean z10 = false;
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder subMenuBuilder2 = subMenuBuilder;
        while (subMenuBuilder2.getParentMenu() != this.f677g) {
            subMenuBuilder2 = (SubMenuBuilder) subMenuBuilder2.getParentMenu();
        }
        View s7 = s(subMenuBuilder2.getItem());
        if (s7 == null) {
            return false;
        }
        this.H = subMenuBuilder.getItem().getItemId();
        int size = subMenuBuilder.size();
        int i10 = 0;
        while (true) {
            if (i10 >= size) {
                break;
            }
            MenuItem item = subMenuBuilder.getItem(i10);
            if (item.isVisible() && item.getIcon() != null) {
                z10 = true;
                break;
            }
            i10++;
        }
        a aVar = new a(this.f676f, subMenuBuilder, s7);
        this.D = aVar;
        aVar.g(z10);
        this.D.k();
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    public boolean r() {
        return v() | u();
    }

    public Drawable t() {
        d dVar = this.f867o;
        if (dVar != null) {
            return dVar.getDrawable();
        }
        if (this.f869q) {
            return this.f868p;
        }
        return null;
    }

    public boolean u() {
        Object obj;
        c cVar = this.E;
        if (cVar != null && (obj = this.f683m) != null) {
            ((View) obj).removeCallbacks(cVar);
            this.E = null;
            return true;
        }
        e eVar = this.C;
        if (eVar == null) {
            return false;
        }
        eVar.b();
        return true;
    }

    @Override // androidx.appcompat.view.menu.BaseMenuPresenter, androidx.appcompat.view.menu.MenuPresenter
    public void updateMenuView(boolean z10) {
        super.updateMenuView(z10);
        ((View) this.f683m).requestLayout();
        MenuBuilder menuBuilder = this.f677g;
        boolean z11 = false;
        if (menuBuilder != null) {
            ArrayList<MenuItemImpl> actionItems = menuBuilder.getActionItems();
            int size = actionItems.size();
            for (int i10 = 0; i10 < size; i10++) {
                ActionProvider b10 = actionItems.get(i10).b();
                if (b10 != null) {
                    b10.i(this);
                }
            }
        }
        MenuBuilder menuBuilder2 = this.f677g;
        ArrayList<MenuItemImpl> nonActionItems = menuBuilder2 != null ? menuBuilder2.getNonActionItems() : null;
        if (this.f870r && nonActionItems != null) {
            int size2 = nonActionItems.size();
            if (size2 == 1) {
                z11 = !nonActionItems.get(0).isActionViewExpanded();
            } else if (size2 > 0) {
                z11 = true;
            }
        }
        if (z11) {
            if (this.f867o == null) {
                this.f867o = new d(this.f675e);
            }
            ViewGroup viewGroup = (ViewGroup) this.f867o.getParent();
            if (viewGroup != this.f683m) {
                if (viewGroup != null) {
                    viewGroup.removeView(this.f867o);
                }
                ActionMenuView actionMenuView = (ActionMenuView) this.f683m;
                actionMenuView.addView(this.f867o, actionMenuView.f());
            }
        } else {
            d dVar = this.f867o;
            if (dVar != null) {
                Object parent = dVar.getParent();
                Object obj = this.f683m;
                if (parent == obj) {
                    ((ViewGroup) obj).removeView(this.f867o);
                }
            }
        }
        ((ActionMenuView) this.f683m).setOverflowReserved(this.f870r);
    }

    public boolean v() {
        a aVar = this.D;
        if (aVar == null) {
            return false;
        }
        aVar.b();
        return true;
    }

    public boolean w() {
        return this.E != null || x();
    }

    public boolean x() {
        e eVar = this.C;
        return eVar != null && eVar.d();
    }

    public void y(Configuration configuration) {
        if (!this.f875w) {
            this.f874v = ActionBarPolicy.b(this.f676f).d();
        }
        MenuBuilder menuBuilder = this.f677g;
        if (menuBuilder != null) {
            menuBuilder.onItemsChanged(true);
        }
    }

    public void z(boolean z10) {
        this.f878z = z10;
    }
}
