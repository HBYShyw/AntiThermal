package androidx.appcompat.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.R$string;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import c.AppCompatResources;
import p.SupportMenuItem;

/* compiled from: MenuItemImpl.java */
/* renamed from: androidx.appcompat.view.menu.i, reason: use source file name */
/* loaded from: classes.dex */
public final class MenuItemImpl implements SupportMenuItem {
    private View A;
    private ActionProvider B;
    private MenuItem.OnActionExpandListener C;
    private ContextMenu.ContextMenuInfo E;

    /* renamed from: a, reason: collision with root package name */
    private final int f741a;

    /* renamed from: b, reason: collision with root package name */
    private final int f742b;

    /* renamed from: c, reason: collision with root package name */
    private final int f743c;

    /* renamed from: d, reason: collision with root package name */
    private final int f744d;

    /* renamed from: e, reason: collision with root package name */
    private CharSequence f745e;

    /* renamed from: f, reason: collision with root package name */
    private CharSequence f746f;

    /* renamed from: g, reason: collision with root package name */
    private Intent f747g;

    /* renamed from: h, reason: collision with root package name */
    private char f748h;

    /* renamed from: j, reason: collision with root package name */
    private char f750j;

    /* renamed from: l, reason: collision with root package name */
    private Drawable f752l;

    /* renamed from: n, reason: collision with root package name */
    MenuBuilder f754n;

    /* renamed from: o, reason: collision with root package name */
    private SubMenuBuilder f755o;

    /* renamed from: p, reason: collision with root package name */
    private Runnable f756p;

    /* renamed from: q, reason: collision with root package name */
    private MenuItem.OnMenuItemClickListener f757q;

    /* renamed from: r, reason: collision with root package name */
    private CharSequence f758r;

    /* renamed from: s, reason: collision with root package name */
    private CharSequence f759s;

    /* renamed from: z, reason: collision with root package name */
    private int f766z;

    /* renamed from: i, reason: collision with root package name */
    private int f749i = 4096;

    /* renamed from: k, reason: collision with root package name */
    private int f751k = 4096;

    /* renamed from: m, reason: collision with root package name */
    private int f753m = 0;

    /* renamed from: t, reason: collision with root package name */
    private ColorStateList f760t = null;

    /* renamed from: u, reason: collision with root package name */
    private PorterDuff.Mode f761u = null;

    /* renamed from: v, reason: collision with root package name */
    private boolean f762v = false;

    /* renamed from: w, reason: collision with root package name */
    private boolean f763w = false;

    /* renamed from: x, reason: collision with root package name */
    private boolean f764x = false;

    /* renamed from: y, reason: collision with root package name */
    private int f765y = 16;
    private boolean D = false;

    /* compiled from: MenuItemImpl.java */
    /* renamed from: androidx.appcompat.view.menu.i$a */
    /* loaded from: classes.dex */
    class a implements ActionProvider.b {
        a() {
        }

        @Override // androidx.core.view.ActionProvider.b
        public void onActionProviderVisibilityChanged(boolean z10) {
            MenuItemImpl menuItemImpl = MenuItemImpl.this;
            menuItemImpl.f754n.onItemVisibleChanged(menuItemImpl);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MenuItemImpl(MenuBuilder menuBuilder, int i10, int i11, int i12, int i13, CharSequence charSequence, int i14) {
        this.f754n = menuBuilder;
        this.f741a = i11;
        this.f742b = i10;
        this.f743c = i12;
        this.f744d = i13;
        this.f745e = charSequence;
        this.f766z = i14;
    }

    private static void d(StringBuilder sb2, int i10, int i11, String str) {
        if ((i10 & i11) == i11) {
            sb2.append(str);
        }
    }

    private Drawable e(Drawable drawable) {
        if (drawable != null && this.f764x && (this.f762v || this.f763w)) {
            drawable = DrawableCompat.l(drawable).mutate();
            if (this.f762v) {
                DrawableCompat.i(drawable, this.f760t);
            }
            if (this.f763w) {
                DrawableCompat.j(drawable, this.f761u);
            }
            this.f764x = false;
        }
        return drawable;
    }

    public boolean A() {
        return (this.f766z & 4) == 4;
    }

    @Override // p.SupportMenuItem
    public SupportMenuItem a(ActionProvider actionProvider) {
        ActionProvider actionProvider2 = this.B;
        if (actionProvider2 != null) {
            actionProvider2.h();
        }
        this.A = null;
        this.B = actionProvider;
        this.f754n.onItemsChanged(true);
        ActionProvider actionProvider3 = this.B;
        if (actionProvider3 != null) {
            actionProvider3.j(new a());
        }
        return this;
    }

    @Override // p.SupportMenuItem
    public ActionProvider b() {
        return this.B;
    }

    public void c() {
        this.f754n.onItemActionRequestChanged(this);
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public boolean collapseActionView() {
        if ((this.f766z & 8) == 0) {
            return false;
        }
        if (this.A == null) {
            return true;
        }
        MenuItem.OnActionExpandListener onActionExpandListener = this.C;
        if (onActionExpandListener == null || onActionExpandListener.onMenuItemActionCollapse(this)) {
            return this.f754n.collapseItemActionView(this);
        }
        return false;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public boolean expandActionView() {
        if (!j()) {
            return false;
        }
        MenuItem.OnActionExpandListener onActionExpandListener = this.C;
        if (onActionExpandListener == null || onActionExpandListener.onMenuItemActionExpand(this)) {
            return this.f754n.expandItemActionView(this);
        }
        return false;
    }

    public int f() {
        return this.f744d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public char g() {
        return this.f754n.isQwertyMode() ? this.f750j : this.f748h;
    }

    @Override // android.view.MenuItem
    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public View getActionView() {
        View view = this.A;
        if (view != null) {
            return view;
        }
        ActionProvider actionProvider = this.B;
        if (actionProvider == null) {
            return null;
        }
        View d10 = actionProvider.d(this);
        this.A = d10;
        return d10;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f751k;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f750j;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f758r;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f742b;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        Drawable drawable = this.f752l;
        if (drawable != null) {
            return e(drawable);
        }
        if (this.f753m == 0) {
            return null;
        }
        Drawable b10 = AppCompatResources.b(this.f754n.getContext(), this.f753m);
        this.f753m = 0;
        this.f752l = b10;
        return e(b10);
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f760t;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.f761u;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f747g;
    }

    @Override // android.view.MenuItem
    @ViewDebug.CapturedViewProperty
    public int getItemId() {
        return this.f741a;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.E;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public int getNumericModifiers() {
        return this.f749i;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f748h;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f743c;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return this.f755o;
    }

    @Override // android.view.MenuItem
    @ViewDebug.CapturedViewProperty
    public CharSequence getTitle() {
        return this.f745e;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.f746f;
        return charSequence != null ? charSequence : this.f745e;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f759s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String h() {
        char g6 = g();
        if (g6 == 0) {
            return "";
        }
        Resources resources = this.f754n.getContext().getResources();
        StringBuilder sb2 = new StringBuilder();
        if (ViewConfiguration.get(this.f754n.getContext()).hasPermanentMenuKey()) {
            sb2.append(resources.getString(R$string.abc_prepend_shortcut_label));
        }
        int i10 = this.f754n.isQwertyMode() ? this.f751k : this.f749i;
        d(sb2, i10, 65536, resources.getString(R$string.abc_menu_meta_shortcut_label));
        d(sb2, i10, 4096, resources.getString(R$string.abc_menu_ctrl_shortcut_label));
        d(sb2, i10, 2, resources.getString(R$string.abc_menu_alt_shortcut_label));
        d(sb2, i10, 1, resources.getString(R$string.abc_menu_shift_shortcut_label));
        d(sb2, i10, 4, resources.getString(R$string.abc_menu_sym_shortcut_label));
        d(sb2, i10, 8, resources.getString(R$string.abc_menu_function_shortcut_label));
        if (g6 == '\b') {
            sb2.append(resources.getString(R$string.abc_menu_delete_shortcut_label));
        } else if (g6 == '\n') {
            sb2.append(resources.getString(R$string.abc_menu_enter_shortcut_label));
        } else if (g6 != ' ') {
            sb2.append(g6);
        } else {
            sb2.append(resources.getString(R$string.abc_menu_space_shortcut_label));
        }
        return sb2.toString();
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return this.f755o != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence i(MenuView.a aVar) {
        if (aVar != null && aVar.prefersCondensedTitle()) {
            return getTitleCondensed();
        }
        return getTitle();
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public boolean isActionViewExpanded() {
        return this.D;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.f765y & 1) == 1;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.f765y & 2) == 2;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.f765y & 16) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        ActionProvider actionProvider = this.B;
        return (actionProvider == null || !actionProvider.g()) ? (this.f765y & 8) == 0 : (this.f765y & 8) == 0 && this.B.b();
    }

    public boolean j() {
        ActionProvider actionProvider;
        if ((this.f766z & 8) == 0) {
            return false;
        }
        if (this.A == null && (actionProvider = this.B) != null) {
            this.A = actionProvider.d(this);
        }
        return this.A != null;
    }

    public boolean k() {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.f757q;
        if (onMenuItemClickListener != null && onMenuItemClickListener.onMenuItemClick(this)) {
            return true;
        }
        MenuBuilder menuBuilder = this.f754n;
        if (menuBuilder.dispatchMenuItemSelected(menuBuilder, this)) {
            return true;
        }
        Runnable runnable = this.f756p;
        if (runnable != null) {
            runnable.run();
            return true;
        }
        if (this.f747g != null) {
            try {
                this.f754n.getContext().startActivity(this.f747g);
                return true;
            } catch (ActivityNotFoundException e10) {
                Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", e10);
            }
        }
        ActionProvider actionProvider = this.B;
        return actionProvider != null && actionProvider.e();
    }

    public boolean l() {
        return (this.f765y & 32) == 32;
    }

    public boolean m() {
        return (this.f765y & 4) != 0;
    }

    public boolean n() {
        return (this.f766z & 1) == 1;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    /* renamed from: o, reason: merged with bridge method [inline-methods] */
    public SupportMenuItem setActionView(int i10) {
        Context context = this.f754n.getContext();
        setActionView(LayoutInflater.from(context).inflate(i10, (ViewGroup) new LinearLayout(context), false));
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public SupportMenuItem setActionView(View view) {
        int i10;
        this.A = view;
        this.B = null;
        if (view != null && view.getId() == -1 && (i10 = this.f741a) > 0) {
            view.setId(i10);
        }
        this.f754n.onItemActionRequestChanged(this);
        return this;
    }

    public void q(boolean z10) {
        this.D = z10;
        this.f754n.onItemsChanged(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(boolean z10) {
        int i10 = this.f765y;
        int i11 = (z10 ? 2 : 0) | (i10 & (-3));
        this.f765y = i11;
        if (i10 != i11) {
            this.f754n.onItemsChanged(false);
        }
    }

    public boolean requiresActionButton() {
        return (this.f766z & 2) == 2;
    }

    public boolean requiresOverflow() {
        return (requiresActionButton() || n()) ? false : true;
    }

    public void s(boolean z10) {
        this.f765y = (z10 ? 4 : 0) | (this.f765y & (-5));
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c10) {
        if (this.f750j == c10) {
            return this;
        }
        this.f750j = Character.toLowerCase(c10);
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z10) {
        int i10 = this.f765y;
        int i11 = (z10 ? 1 : 0) | (i10 & (-2));
        this.f765y = i11;
        if (i10 != i11) {
            this.f754n.onItemsChanged(false);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z10) {
        if ((this.f765y & 4) != 0) {
            this.f754n.setExclusiveItemChecked(this);
        } else {
            r(z10);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z10) {
        if (z10) {
            this.f765y |= 16;
        } else {
            this.f765y &= -17;
        }
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f753m = 0;
        this.f752l = drawable;
        this.f764x = true;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f760t = colorStateList;
        this.f762v = true;
        this.f764x = true;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.f761u = mode;
        this.f763w = true;
        this.f764x = true;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f747g = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c10) {
        if (this.f748h == c10) {
            return this;
        }
        this.f748h = c10;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        this.C = onActionExpandListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.f757q = onMenuItemClickListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c10, char c11) {
        this.f748h = c10;
        this.f750j = Character.toLowerCase(c11);
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public void setShowAsAction(int i10) {
        int i11 = i10 & 3;
        if (i11 != 0 && i11 != 1 && i11 != 2) {
            throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
        this.f766z = i10;
        this.f754n.onItemActionRequestChanged(this);
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f745e = charSequence;
        this.f754n.onItemsChanged(false);
        SubMenuBuilder subMenuBuilder = this.f755o;
        if (subMenuBuilder != null) {
            subMenuBuilder.setHeaderTitle(charSequence);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f746f = charSequence;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z10) {
        if (x(z10)) {
            this.f754n.onItemVisibleChanged(this);
        }
        return this;
    }

    public void t(boolean z10) {
        if (z10) {
            this.f765y |= 32;
        } else {
            this.f765y &= -33;
        }
    }

    public String toString() {
        CharSequence charSequence = this.f745e;
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.E = contextMenuInfo;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    /* renamed from: v, reason: merged with bridge method [inline-methods] */
    public SupportMenuItem setShowAsActionFlags(int i10) {
        setShowAsAction(i10);
        return this;
    }

    public void w(SubMenuBuilder subMenuBuilder) {
        this.f755o = subMenuBuilder;
        subMenuBuilder.setHeaderTitle(getTitle());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean x(boolean z10) {
        int i10 = this.f765y;
        int i11 = (z10 ? 0 : 8) | (i10 & (-9));
        this.f765y = i11;
        return i10 != i11;
    }

    public boolean y() {
        return this.f754n.getOptionalIconsVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean z() {
        return this.f754n.isShortcutsVisible() && g() != 0;
    }

    @Override // android.view.MenuItem
    public SupportMenuItem setContentDescription(CharSequence charSequence) {
        this.f758r = charSequence;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public SupportMenuItem setTooltipText(CharSequence charSequence) {
        this.f759s = charSequence;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c10, int i10) {
        if (this.f750j == c10 && this.f751k == i10) {
            return this;
        }
        this.f750j = Character.toLowerCase(c10);
        this.f751k = KeyEvent.normalizeMetaState(i10);
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setNumericShortcut(char c10, int i10) {
        if (this.f748h == c10 && this.f749i == i10) {
            return this;
        }
        this.f748h = c10;
        this.f749i = KeyEvent.normalizeMetaState(i10);
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setShortcut(char c10, char c11, int i10, int i11) {
        this.f748h = c10;
        this.f749i = KeyEvent.normalizeMetaState(i10);
        this.f750j = Character.toLowerCase(c11);
        this.f751k = KeyEvent.normalizeMetaState(i11);
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i10) {
        this.f752l = null;
        this.f753m = i10;
        this.f764x = true;
        this.f754n.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i10) {
        return setTitle(this.f754n.getContext().getString(i10));
    }
}
