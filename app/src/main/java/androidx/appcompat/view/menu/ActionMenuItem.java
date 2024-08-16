package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ActionProvider;
import p.SupportMenuItem;

/* compiled from: ActionMenuItem.java */
/* renamed from: androidx.appcompat.view.menu.a, reason: use source file name */
/* loaded from: classes.dex */
public class ActionMenuItem implements SupportMenuItem {

    /* renamed from: a, reason: collision with root package name */
    private final int f655a;

    /* renamed from: b, reason: collision with root package name */
    private final int f656b;

    /* renamed from: c, reason: collision with root package name */
    private final int f657c;

    /* renamed from: d, reason: collision with root package name */
    private CharSequence f658d;

    /* renamed from: e, reason: collision with root package name */
    private CharSequence f659e;

    /* renamed from: f, reason: collision with root package name */
    private Intent f660f;

    /* renamed from: g, reason: collision with root package name */
    private char f661g;

    /* renamed from: i, reason: collision with root package name */
    private char f663i;

    /* renamed from: k, reason: collision with root package name */
    private Drawable f665k;

    /* renamed from: l, reason: collision with root package name */
    private Context f666l;

    /* renamed from: m, reason: collision with root package name */
    private MenuItem.OnMenuItemClickListener f667m;

    /* renamed from: n, reason: collision with root package name */
    private CharSequence f668n;

    /* renamed from: o, reason: collision with root package name */
    private CharSequence f669o;

    /* renamed from: h, reason: collision with root package name */
    private int f662h = 4096;

    /* renamed from: j, reason: collision with root package name */
    private int f664j = 4096;

    /* renamed from: p, reason: collision with root package name */
    private ColorStateList f670p = null;

    /* renamed from: q, reason: collision with root package name */
    private PorterDuff.Mode f671q = null;

    /* renamed from: r, reason: collision with root package name */
    private boolean f672r = false;

    /* renamed from: s, reason: collision with root package name */
    private boolean f673s = false;

    /* renamed from: t, reason: collision with root package name */
    private int f674t = 16;

    public ActionMenuItem(Context context, int i10, int i11, int i12, int i13, CharSequence charSequence) {
        this.f666l = context;
        this.f655a = i11;
        this.f656b = i10;
        this.f657c = i13;
        this.f658d = charSequence;
    }

    private void c() {
        Drawable drawable = this.f665k;
        if (drawable != null) {
            if (this.f672r || this.f673s) {
                Drawable l10 = DrawableCompat.l(drawable);
                this.f665k = l10;
                Drawable mutate = l10.mutate();
                this.f665k = mutate;
                if (this.f672r) {
                    DrawableCompat.i(mutate, this.f670p);
                }
                if (this.f673s) {
                    DrawableCompat.j(this.f665k, this.f671q);
                }
            }
        }
    }

    @Override // p.SupportMenuItem
    public SupportMenuItem a(ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override // p.SupportMenuItem
    public ActionProvider b() {
        return null;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public boolean collapseActionView() {
        return false;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public SupportMenuItem setActionView(int i10) {
        throw new UnsupportedOperationException();
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public SupportMenuItem setActionView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public boolean expandActionView() {
        return false;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public SupportMenuItem setShowAsActionFlags(int i10) {
        setShowAsAction(i10);
        return this;
    }

    @Override // android.view.MenuItem
    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException();
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public View getActionView() {
        return null;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public int getAlphabeticModifiers() {
        return this.f664j;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.f663i;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public CharSequence getContentDescription() {
        return this.f668n;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.f656b;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        return this.f665k;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public ColorStateList getIconTintList() {
        return this.f670p;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public PorterDuff.Mode getIconTintMode() {
        return this.f671q;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.f660f;
    }

    @Override // android.view.MenuItem
    public int getItemId() {
        return this.f655a;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public int getNumericModifiers() {
        return this.f662h;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.f661g;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.f657c;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return null;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitle() {
        return this.f658d;
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.f659e;
        return charSequence != null ? charSequence : this.f658d;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public CharSequence getTooltipText() {
        return this.f669o;
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return false;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.f674t & 1) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.f674t & 2) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.f674t & 16) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        return (this.f674t & 8) == 0;
    }

    public boolean requiresActionButton() {
        return true;
    }

    public boolean requiresOverflow() {
        return false;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException();
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c10) {
        this.f663i = Character.toLowerCase(c10);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z10) {
        this.f674t = (z10 ? 1 : 0) | (this.f674t & (-2));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z10) {
        this.f674t = (z10 ? 2 : 0) | (this.f674t & (-3));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z10) {
        this.f674t = (z10 ? 16 : 0) | (this.f674t & (-17));
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.f665k = drawable;
        c();
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setIconTintList(ColorStateList colorStateList) {
        this.f670p = colorStateList;
        this.f672r = true;
        c();
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        this.f671q = mode;
        this.f673s = true;
        c();
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.f660f = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c10) {
        this.f661g = c10;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException();
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.f667m = onMenuItemClickListener;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c10, char c11) {
        this.f661g = c10;
        this.f663i = Character.toLowerCase(c11);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public void setShowAsAction(int i10) {
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.f658d = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.f659e = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z10) {
        this.f674t = (this.f674t & 8) | (z10 ? 0 : 8);
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c10, int i10) {
        this.f663i = Character.toLowerCase(c10);
        this.f664j = KeyEvent.normalizeMetaState(i10);
        return this;
    }

    @Override // android.view.MenuItem
    public SupportMenuItem setContentDescription(CharSequence charSequence) {
        this.f668n = charSequence;
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setNumericShortcut(char c10, int i10) {
        this.f661g = c10;
        this.f662h = KeyEvent.normalizeMetaState(i10);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i10) {
        this.f658d = this.f666l.getResources().getString(i10);
        return this;
    }

    @Override // android.view.MenuItem
    public SupportMenuItem setTooltipText(CharSequence charSequence) {
        this.f669o = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i10) {
        this.f665k = ContextCompat.e(this.f666l, i10);
        c();
        return this;
    }

    @Override // p.SupportMenuItem, android.view.MenuItem
    public MenuItem setShortcut(char c10, char c11, int i10, int i11) {
        this.f661g = c10;
        this.f662h = KeyEvent.normalizeMetaState(i10);
        this.f663i = Character.toLowerCase(c11);
        this.f664j = KeyEvent.normalizeMetaState(i11);
        return this;
    }
}
