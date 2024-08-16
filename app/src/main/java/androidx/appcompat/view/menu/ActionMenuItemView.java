package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.ForwardingListener;
import androidx.appcompat.widget.TooltipCompat;

/* loaded from: classes.dex */
public class ActionMenuItemView extends AppCompatTextView implements MenuView.a, View.OnClickListener, ActionMenuView.a {

    /* renamed from: l, reason: collision with root package name */
    MenuItemImpl f623l;

    /* renamed from: m, reason: collision with root package name */
    private CharSequence f624m;

    /* renamed from: n, reason: collision with root package name */
    private Drawable f625n;

    /* renamed from: o, reason: collision with root package name */
    MenuBuilder.b f626o;

    /* renamed from: p, reason: collision with root package name */
    private ForwardingListener f627p;

    /* renamed from: q, reason: collision with root package name */
    b f628q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f629r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f630s;

    /* renamed from: t, reason: collision with root package name */
    private int f631t;

    /* renamed from: u, reason: collision with root package name */
    private int f632u;

    /* renamed from: v, reason: collision with root package name */
    private int f633v;

    /* loaded from: classes.dex */
    private class a extends ForwardingListener {
        public a() {
            super(ActionMenuItemView.this);
        }

        @Override // androidx.appcompat.widget.ForwardingListener
        public ShowableListMenu b() {
            b bVar = ActionMenuItemView.this.f628q;
            if (bVar != null) {
                return bVar.a();
            }
            return null;
        }

        @Override // androidx.appcompat.widget.ForwardingListener
        protected boolean c() {
            ShowableListMenu b10;
            ActionMenuItemView actionMenuItemView = ActionMenuItemView.this;
            MenuBuilder.b bVar = actionMenuItemView.f626o;
            return bVar != null && bVar.a(actionMenuItemView.f623l) && (b10 = b()) != null && b10.a();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class b {
        public abstract ShowableListMenu a();
    }

    public ActionMenuItemView(Context context) {
        this(context, null);
    }

    private boolean q() {
        Configuration configuration = getContext().getResources().getConfiguration();
        int i10 = configuration.screenWidthDp;
        return i10 >= 480 || (i10 >= 640 && configuration.screenHeightDp >= 480) || configuration.orientation == 2;
    }

    private void r() {
        boolean z10 = true;
        boolean z11 = !TextUtils.isEmpty(this.f624m);
        if (this.f625n != null && (!this.f623l.A() || (!this.f629r && !this.f630s))) {
            z10 = false;
        }
        boolean z12 = z11 & z10;
        setText(z12 ? this.f624m : null);
        CharSequence contentDescription = this.f623l.getContentDescription();
        if (TextUtils.isEmpty(contentDescription)) {
            setContentDescription(z12 ? null : this.f623l.getTitle());
        } else {
            setContentDescription(contentDescription);
        }
        CharSequence tooltipText = this.f623l.getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            TooltipCompat.a(this, z12 ? null : this.f623l.getTitle());
        } else {
            TooltipCompat.a(this, tooltipText);
        }
    }

    @Override // androidx.appcompat.widget.ActionMenuView.a
    public boolean a() {
        return p();
    }

    @Override // androidx.appcompat.widget.ActionMenuView.a
    public boolean b() {
        return p() && this.f623l.getIcon() == null;
    }

    @Override // android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public MenuItemImpl getItemData() {
        return this.f623l;
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public void initialize(MenuItemImpl menuItemImpl, int i10) {
        this.f623l = menuItemImpl;
        setIcon(menuItemImpl.getIcon());
        setTitle(menuItemImpl.i(this));
        setId(menuItemImpl.getItemId());
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        setEnabled(menuItemImpl.isEnabled());
        if (menuItemImpl.hasSubMenu() && this.f627p == null) {
            this.f627p = new a();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        MenuBuilder.b bVar = this.f626o;
        if (bVar != null) {
            bVar.a(this.f623l);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f629r = q();
        r();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int i10, int i11) {
        int i12;
        int i13;
        boolean p10 = p();
        if (p10 && (i13 = this.f632u) >= 0) {
            super.setPadding(i13, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        super.onMeasure(i10, i11);
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        int measuredWidth = getMeasuredWidth();
        if (mode == Integer.MIN_VALUE) {
            i12 = Math.min(size, this.f631t);
        } else {
            i12 = this.f631t;
        }
        if (mode != 1073741824 && this.f631t > 0 && measuredWidth < i12) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(i12, 1073741824), i11);
        }
        if (p10 || this.f625n == null) {
            return;
        }
        super.setPadding((getMeasuredWidth() - this.f625n.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(null);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ForwardingListener forwardingListener;
        if (this.f623l.hasSubMenu() && (forwardingListener = this.f627p) != null && forwardingListener.onTouch(this, motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean p() {
        return !TextUtils.isEmpty(getText());
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public boolean prefersCondensedTitle() {
        return true;
    }

    public void setCheckable(boolean z10) {
    }

    public void setChecked(boolean z10) {
    }

    public void setExpandedFormat(boolean z10) {
        if (this.f630s != z10) {
            this.f630s = z10;
            MenuItemImpl menuItemImpl = this.f623l;
            if (menuItemImpl != null) {
                menuItemImpl.c();
            }
        }
    }

    public void setIcon(Drawable drawable) {
        this.f625n = drawable;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int i10 = this.f633v;
            if (intrinsicWidth > i10) {
                intrinsicHeight = (int) (intrinsicHeight * (i10 / intrinsicWidth));
                intrinsicWidth = i10;
            }
            if (intrinsicHeight > i10) {
                intrinsicWidth = (int) (intrinsicWidth * (i10 / intrinsicHeight));
            } else {
                i10 = intrinsicHeight;
            }
            drawable.setBounds(0, 0, intrinsicWidth, i10);
        }
        setCompoundDrawables(drawable, null, null, null);
        r();
    }

    public void setItemInvoker(MenuBuilder.b bVar) {
        this.f626o = bVar;
    }

    @Override // android.widget.TextView, android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
        this.f632u = i10;
        super.setPadding(i10, i11, i12, i13);
    }

    public void setPopupCallback(b bVar) {
        this.f628q = bVar;
    }

    public void setTitle(CharSequence charSequence) {
        this.f624m = charSequence;
        r();
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        Resources resources = context.getResources();
        this.f629r = q();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionMenuItemView, i10, 0);
        this.f631t = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ActionMenuItemView_android_minWidth, 0);
        obtainStyledAttributes.recycle();
        this.f633v = (int) ((resources.getDisplayMetrics().density * 32.0f) + 0.5f);
        setOnClickListener(this);
        this.f632u = -1;
        setSaveEnabled(false);
    }
}
