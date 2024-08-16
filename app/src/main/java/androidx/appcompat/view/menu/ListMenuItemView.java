package androidx.appcompat.view.menu;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class ListMenuItemView extends LinearLayout implements MenuView.a, AbsListView.SelectionBoundsAdjuster {

    /* renamed from: e, reason: collision with root package name */
    private MenuItemImpl f638e;

    /* renamed from: f, reason: collision with root package name */
    private ImageView f639f;

    /* renamed from: g, reason: collision with root package name */
    private RadioButton f640g;

    /* renamed from: h, reason: collision with root package name */
    private TextView f641h;

    /* renamed from: i, reason: collision with root package name */
    private CheckBox f642i;

    /* renamed from: j, reason: collision with root package name */
    private TextView f643j;

    /* renamed from: k, reason: collision with root package name */
    private ImageView f644k;

    /* renamed from: l, reason: collision with root package name */
    private ImageView f645l;

    /* renamed from: m, reason: collision with root package name */
    private LinearLayout f646m;

    /* renamed from: n, reason: collision with root package name */
    private Drawable f647n;

    /* renamed from: o, reason: collision with root package name */
    private int f648o;

    /* renamed from: p, reason: collision with root package name */
    private Context f649p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f650q;

    /* renamed from: r, reason: collision with root package name */
    private Drawable f651r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f652s;

    /* renamed from: t, reason: collision with root package name */
    private LayoutInflater f653t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f654u;

    public ListMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.listMenuViewStyle);
    }

    private void a(View view) {
        b(view, -1);
    }

    private void b(View view, int i10) {
        LinearLayout linearLayout = this.f646m;
        if (linearLayout != null) {
            linearLayout.addView(view, i10);
        } else {
            addView(view, i10);
        }
    }

    private void c() {
        CheckBox checkBox = (CheckBox) getInflater().inflate(R$layout.abc_list_menu_item_checkbox, (ViewGroup) this, false);
        this.f642i = checkBox;
        a(checkBox);
    }

    private void d() {
        ImageView imageView = (ImageView) getInflater().inflate(R$layout.abc_list_menu_item_icon, (ViewGroup) this, false);
        this.f639f = imageView;
        b(imageView, 0);
    }

    private void e() {
        RadioButton radioButton = (RadioButton) getInflater().inflate(R$layout.abc_list_menu_item_radio, (ViewGroup) this, false);
        this.f640g = radioButton;
        a(radioButton);
    }

    private LayoutInflater getInflater() {
        if (this.f653t == null) {
            this.f653t = LayoutInflater.from(getContext());
        }
        return this.f653t;
    }

    private void setSubMenuArrowVisible(boolean z10) {
        ImageView imageView = this.f644k;
        if (imageView != null) {
            imageView.setVisibility(z10 ? 0 : 8);
        }
    }

    @Override // android.widget.AbsListView.SelectionBoundsAdjuster
    public void adjustListItemSelectionBounds(Rect rect) {
        ImageView imageView = this.f645l;
        if (imageView == null || imageView.getVisibility() != 0) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f645l.getLayoutParams();
        rect.top += this.f645l.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    public void f(boolean z10, char c10) {
        int i10 = (z10 && this.f638e.z()) ? 0 : 8;
        if (i10 == 0) {
            this.f643j.setText(this.f638e.h());
        }
        if (this.f643j.getVisibility() != i10) {
            this.f643j.setVisibility(i10);
        }
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public MenuItemImpl getItemData() {
        return this.f638e;
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public void initialize(MenuItemImpl menuItemImpl, int i10) {
        this.f638e = menuItemImpl;
        setVisibility(menuItemImpl.isVisible() ? 0 : 8);
        setTitle(menuItemImpl.i(this));
        setCheckable(menuItemImpl.isCheckable());
        f(menuItemImpl.z(), menuItemImpl.g());
        setIcon(menuItemImpl.getIcon());
        setEnabled(menuItemImpl.isEnabled());
        setSubMenuArrowVisible(menuItemImpl.hasSubMenu());
        setContentDescription(menuItemImpl.getContentDescription());
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewCompat.p0(this, this.f647n);
        TextView textView = (TextView) findViewById(R$id.title);
        this.f641h = textView;
        int i10 = this.f648o;
        if (i10 != -1) {
            textView.setTextAppearance(this.f649p, i10);
        }
        this.f643j = (TextView) findViewById(R$id.shortcut);
        ImageView imageView = (ImageView) findViewById(R$id.submenuarrow);
        this.f644k = imageView;
        if (imageView != null) {
            imageView.setImageDrawable(this.f651r);
        }
        this.f645l = (ImageView) findViewById(R$id.group_divider);
        this.f646m = (LinearLayout) findViewById(R$id.content);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        if (this.f639f != null && this.f650q) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.f639f.getLayoutParams();
            int i12 = layoutParams.height;
            if (i12 > 0 && layoutParams2.width <= 0) {
                layoutParams2.width = i12;
            }
        }
        super.onMeasure(i10, i11);
    }

    @Override // androidx.appcompat.view.menu.MenuView.a
    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setCheckable(boolean z10) {
        CompoundButton compoundButton;
        CompoundButton compoundButton2;
        if (!z10 && this.f640g == null && this.f642i == null) {
            return;
        }
        if (this.f638e.m()) {
            if (this.f640g == null) {
                e();
            }
            compoundButton = this.f640g;
            compoundButton2 = this.f642i;
        } else {
            if (this.f642i == null) {
                c();
            }
            compoundButton = this.f642i;
            compoundButton2 = this.f640g;
        }
        if (z10) {
            compoundButton.setChecked(this.f638e.isChecked());
            if (compoundButton.getVisibility() != 0) {
                compoundButton.setVisibility(0);
            }
            if (compoundButton2 == null || compoundButton2.getVisibility() == 8) {
                return;
            }
            compoundButton2.setVisibility(8);
            return;
        }
        CheckBox checkBox = this.f642i;
        if (checkBox != null) {
            checkBox.setVisibility(8);
        }
        RadioButton radioButton = this.f640g;
        if (radioButton != null) {
            radioButton.setVisibility(8);
        }
    }

    public void setChecked(boolean z10) {
        CompoundButton compoundButton;
        if (this.f638e.m()) {
            if (this.f640g == null) {
                e();
            }
            compoundButton = this.f640g;
        } else {
            if (this.f642i == null) {
                c();
            }
            compoundButton = this.f642i;
        }
        compoundButton.setChecked(z10);
    }

    public void setForceShowIcon(boolean z10) {
        this.f654u = z10;
        this.f650q = z10;
    }

    public void setGroupDividerEnabled(boolean z10) {
        ImageView imageView = this.f645l;
        if (imageView != null) {
            imageView.setVisibility((this.f652s || !z10) ? 8 : 0);
        }
    }

    public void setIcon(Drawable drawable) {
        boolean z10 = this.f638e.y() || this.f654u;
        if (z10 || this.f650q) {
            ImageView imageView = this.f639f;
            if (imageView == null && drawable == null && !this.f650q) {
                return;
            }
            if (imageView == null) {
                d();
            }
            if (drawable == null && !this.f650q) {
                this.f639f.setVisibility(8);
                return;
            }
            ImageView imageView2 = this.f639f;
            if (!z10) {
                drawable = null;
            }
            imageView2.setImageDrawable(drawable);
            if (this.f639f.getVisibility() != 0) {
                this.f639f.setVisibility(0);
            }
        }
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence != null) {
            this.f641h.setText(charSequence);
            if (this.f641h.getVisibility() != 0) {
                this.f641h.setVisibility(0);
                return;
            }
            return;
        }
        if (this.f641h.getVisibility() != 8) {
            this.f641h.setVisibility(8);
        }
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet);
        TintTypedArray w10 = TintTypedArray.w(getContext(), attributeSet, R$styleable.MenuView, i10, 0);
        this.f647n = w10.g(R$styleable.MenuView_android_itemBackground);
        this.f648o = w10.n(R$styleable.MenuView_android_itemTextAppearance, -1);
        this.f650q = w10.a(R$styleable.MenuView_preserveIconSpacing, false);
        this.f649p = context;
        this.f651r = w10.g(R$styleable.MenuView_subMenuArrow);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(null, new int[]{R.attr.divider}, R$attr.dropDownListViewStyle, 0);
        this.f652s = obtainStyledAttributes.hasValue(0);
        w10.x();
        obtainStyledAttributes.recycle();
    }
}
