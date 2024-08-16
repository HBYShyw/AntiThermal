package com.coui.appcompat.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R$string;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.appcompat.widget.n0;
import androidx.core.view.ViewCompat;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$plurals;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;
import g2.COUIPopupListWindow;
import g2.COUISubMenuClickListener;
import g2.PopupListItem;
import h3.UIUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import k2.COUIHintRedDotHelper;

/* loaded from: classes.dex */
public class COUIActionMenuView extends ActionMenuView {
    private List<Class<?>> A;
    private int B;
    private int C;
    private boolean D;
    private int E;
    private HashMap<Integer, Integer> F;
    private int G;
    private int H;
    private int I;
    private int J;
    private int K;
    private int L;
    private int M;
    private int N;
    private COUIHintRedDotHelper O;
    private PopupWindow.OnDismissListener P;
    private View Q;
    private String R;
    private String S;
    private int T;
    private ArrayList<PopupListItem> U;
    private int V;
    private COUISubMenuClickListener W;

    /* renamed from: q, reason: collision with root package name */
    public COUIPopupListWindow f7960q;

    /* renamed from: r, reason: collision with root package name */
    private ArrayList<PopupListItem> f7961r;

    /* renamed from: s, reason: collision with root package name */
    private MenuItemImpl f7962s;

    /* renamed from: t, reason: collision with root package name */
    private int f7963t;

    /* renamed from: u, reason: collision with root package name */
    private int f7964u;

    /* renamed from: v, reason: collision with root package name */
    private int f7965v;

    /* renamed from: w, reason: collision with root package name */
    private int f7966w;

    /* renamed from: x, reason: collision with root package name */
    private int f7967x;

    /* renamed from: y, reason: collision with root package name */
    private int f7968y;

    /* renamed from: z, reason: collision with root package name */
    private MenuBuilder f7969z;

    /* loaded from: classes.dex */
    class a implements View.OnLongClickListener {
        a() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return true;
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnClickListener {

        /* loaded from: classes.dex */
        class a implements AdapterView.OnItemClickListener {
            a() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
                if (((PopupListItem) COUIActionMenuView.this.f7961r.get(i10)).h()) {
                    return;
                }
                COUIActionMenuView.this.f7969z.performItemAction(COUIActionMenuView.this.f7969z.getNonActionItems().get(i10), 0);
                COUIActionMenuView.this.f7960q.dismiss();
            }
        }

        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIActionMenuView cOUIActionMenuView = COUIActionMenuView.this;
            if (cOUIActionMenuView.f7960q == null) {
                Configuration configuration = cOUIActionMenuView.getContext().getResources().getConfiguration();
                configuration.densityDpi = COUIActionMenuView.this.getContext().getResources().getDisplayMetrics().densityDpi;
                COUIActionMenuView.this.f7960q = new COUIPopupListWindow(new ContextThemeWrapper(COUIActionMenuView.this.getContext().createConfigurationContext(configuration), R$style.Theme_COUI_Main));
                COUIActionMenuView.this.f7960q.H(true);
                COUIActionMenuView.this.f7960q.setInputMethodMode(2);
                COUIActionMenuView.this.f7960q.b(true);
                COUIActionMenuView cOUIActionMenuView2 = COUIActionMenuView.this;
                cOUIActionMenuView2.f7960q.setOnDismissListener(cOUIActionMenuView2.P);
                COUIActionMenuView.this.f7961r = new ArrayList();
            }
            COUIActionMenuView.this.f7961r.clear();
            if (COUIActionMenuView.this.f7969z != null) {
                int i10 = 0;
                while (i10 < COUIActionMenuView.this.f7969z.getNonActionItems().size()) {
                    COUIActionMenuView cOUIActionMenuView3 = COUIActionMenuView.this;
                    cOUIActionMenuView3.f7962s = cOUIActionMenuView3.f7969z.getNonActionItems().get(i10);
                    COUIActionMenuView.this.f7961r.add(new PopupListItem(COUIActionMenuView.this.f7962s.getIcon(), COUIActionMenuView.this.f7962s.getTitle() != null ? COUIActionMenuView.this.f7962s.getTitle().toString() : "", COUIActionMenuView.this.f7962s.isCheckable(), COUIActionMenuView.this.f7962s.isChecked(), COUIActionMenuView.this.F.containsKey(Integer.valueOf(COUIActionMenuView.this.f7962s.getItemId())) ? ((Integer) COUIActionMenuView.this.F.get(Integer.valueOf(COUIActionMenuView.this.f7962s.getItemId()))).intValue() : -1, COUIActionMenuView.this.f7962s.isEnabled(), (COUIActionMenuView.this.V != i10 || COUIActionMenuView.this.U == null || COUIActionMenuView.this.U.size() <= 0) ? null : COUIActionMenuView.this.U));
                    i10++;
                }
                COUIActionMenuView cOUIActionMenuView4 = COUIActionMenuView.this;
                cOUIActionMenuView4.f7960q.J(cOUIActionMenuView4.f7961r);
                COUIActionMenuView.this.f7960q.M(new a());
                COUIActionMenuView cOUIActionMenuView5 = COUIActionMenuView.this;
                cOUIActionMenuView5.f7960q.Q(0, UIUtil.f(cOUIActionMenuView5.getContext()));
                if (COUIActionMenuView.this.W != null) {
                    COUIActionMenuView cOUIActionMenuView6 = COUIActionMenuView.this;
                    cOUIActionMenuView6.f7960q.P(cOUIActionMenuView6.W);
                }
            }
            COUIActionMenuView cOUIActionMenuView7 = COUIActionMenuView.this;
            cOUIActionMenuView7.f7960q.R(cOUIActionMenuView7.Q);
        }
    }

    public COUIActionMenuView(Context context) {
        this(context, null);
    }

    private void C(View view, int i10, Canvas canvas) {
        int i11;
        int i12;
        float x10;
        float x11;
        int i13 = i10 != -1 ? i10 != 0 ? 2 : 1 : 0;
        if (view != null) {
            int n10 = this.O.n(i13, i10);
            int m10 = this.O.m(i13);
            if (i13 == 1) {
                i11 = this.I;
                i12 = this.J;
            } else if (i10 < 100) {
                i11 = this.L;
                i12 = this.K;
            } else {
                i11 = this.M;
                i12 = this.K;
            }
            RectF rectF = new RectF();
            if ((view instanceof ActionMenuItemView) && ((ActionMenuItemView) view).getItemData().getIcon() == null) {
                if (D()) {
                    x11 = (view.getX() + i11) - this.E;
                    x10 = x11 - n10;
                } else {
                    x10 = ((view.getX() + view.getWidth()) - i11) + this.E;
                    x11 = n10 + x10;
                }
            } else if (D()) {
                x11 = ((view.getX() + i11) - this.E) + this.f7967x;
                x10 = x11 - n10;
            } else {
                x10 = (((view.getX() + view.getWidth()) - i11) + this.E) - this.f7967x;
                x11 = n10 + x10;
            }
            float f10 = (this.N - i12) + this.f7968y;
            rectF.left = x10;
            rectF.top = f10;
            rectF.right = x11;
            rectF.bottom = m10 + f10;
            this.O.f(canvas, i13, Integer.valueOf(i10), rectF);
        }
    }

    private boolean D() {
        return ViewCompat.x(this) == 1;
    }

    private int E(View view, int i10, int i11, int i12, int i13) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        int i14 = marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
        view.measure(ViewGroup.getChildMeasureSpec(i10, getPaddingLeft() + getPaddingRight() + i14 + i11, marginLayoutParams.width), ViewGroup.getChildMeasureSpec(i12, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + i13, marginLayoutParams.height));
        return view.getMeasuredWidth() + i14;
    }

    private void F() {
        int i10 = 0;
        int i11 = -1;
        int i12 = -1;
        for (int i13 = 0; i13 < getChildCount(); i13++) {
            if (getChildAt(i13).getVisibility() != 8) {
                i10++;
                if (i10 == 1) {
                    i11 = i13;
                    i12 = i11;
                } else {
                    i12 = i13;
                }
            }
        }
        if (i11 != -1 && !this.D && i10 > 1) {
            View childAt = getChildAt(i11);
            if (childAt instanceof ActionMenuItemView) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                if (((ActionMenuItemView) childAt).getItemData().getIcon() == null) {
                    if (D()) {
                        marginLayoutParams.rightMargin = this.f7965v;
                    } else {
                        marginLayoutParams.leftMargin = this.f7965v;
                    }
                } else if (D()) {
                    marginLayoutParams.rightMargin = this.f7966w;
                } else {
                    marginLayoutParams.leftMargin = this.f7966w;
                }
            }
        }
        if (i12 != -1) {
            View childAt2 = getChildAt(i12);
            if (childAt2 instanceof ActionMenuItemView) {
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) childAt2.getLayoutParams();
                if (((ActionMenuItemView) childAt2).getItemData().getIcon() == null) {
                    if (D()) {
                        marginLayoutParams2.leftMargin = this.f7965v;
                        return;
                    } else {
                        marginLayoutParams2.rightMargin = this.f7965v;
                        return;
                    }
                }
                if (D()) {
                    marginLayoutParams2.leftMargin = this.f7966w;
                } else {
                    marginLayoutParams2.rightMargin = this.f7966w;
                }
            }
        }
    }

    private String G(int i10) {
        return i10 != -1 ? i10 != 0 ? getResources().getQuantityString(this.T, i10, Integer.valueOf(i10)) : this.S : "";
    }

    public void B() {
        this.H = 0;
        this.G = 0;
        this.F.clear();
    }

    public void H(ArrayList<PopupListItem> arrayList, int i10) {
        ArrayList<PopupListItem> arrayList2;
        this.U = arrayList;
        this.V = i10;
        if (i10 < 0 || (arrayList2 = this.f7961r) == null || arrayList2.size() < i10 - 1) {
            return;
        }
        this.f7961r.get(i10).n(true);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        view.setOnLongClickListener(new a());
        view.setHapticFeedbackEnabled(false);
        TooltipCompat.a(view, "");
        if (((ActionMenuView.LayoutParams) layoutParams).f901a) {
            this.Q = view;
            view.setBackgroundResource(R$drawable.coui_toolbar_menu_bg);
            layoutParams.height = -1;
            this.Q.setMinimumWidth(this.f7963t);
            View view2 = this.Q;
            view2.setPadding(this.f7964u, view2.getPaddingTop(), this.f7964u, this.Q.getPaddingBottom());
            this.Q.setOnTouchListener(null);
            view.setOnClickListener(new b());
        }
        super.addView(view, i10, layoutParams);
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public void b() {
        COUIPopupListWindow cOUIPopupListWindow = this.f7960q;
        if (cOUIPopupListWindow != null) {
            cOUIPopupListWindow.dismiss();
        }
        super.b();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i10 = 0; i10 < getChildCount(); i10++) {
            View childAt = getChildAt(i10);
            if (this.F.containsKey(Integer.valueOf(childAt.getId()))) {
                C(childAt, this.F.get(Integer.valueOf(childAt.getId())).intValue(), canvas);
            }
            if (((ActionMenuView.LayoutParams) childAt.getLayoutParams()).f901a) {
                int i11 = this.G == 0 ? -1 : this.H;
                C(childAt, i11, canvas);
                childAt.setContentDescription(this.R + "," + G(i11));
            }
        }
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public Menu getMenu() {
        MenuBuilder menuBuilder = (MenuBuilder) super.getMenu();
        this.f7969z = menuBuilder;
        return menuBuilder;
    }

    public View getOverFlowMenuButton() {
        return this.Q;
    }

    @Override // androidx.appcompat.widget.ActionMenuView, androidx.appcompat.view.menu.MenuView
    public void initialize(MenuBuilder menuBuilder) {
        this.f7969z = menuBuilder;
        super.initialize(menuBuilder);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.ActionMenuView, androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        int childCount = getChildCount();
        int i14 = 0;
        int i15 = 0;
        for (int i16 = 0; i16 < childCount; i16++) {
            if (getChildAt(i16).getVisibility() != 8) {
                i15++;
            }
        }
        boolean b10 = n0.b(this);
        int i17 = (i13 - i11) / 2;
        if (this.D) {
            if (b10) {
                int width = getWidth() - getPaddingRight();
                while (i14 < childCount) {
                    View childAt = getChildAt(i14);
                    ActionMenuView.LayoutParams layoutParams = (ActionMenuView.LayoutParams) childAt.getLayoutParams();
                    if (childAt.getVisibility() != 8) {
                        int i18 = width - ((LinearLayout.LayoutParams) layoutParams).rightMargin;
                        int measuredWidth = childAt.getMeasuredWidth();
                        int measuredHeight = childAt.getMeasuredHeight();
                        int i19 = i17 - (measuredHeight / 2);
                        childAt.layout(i18 - measuredWidth, i19, i18, measuredHeight + i19);
                        width = i18 - ((measuredWidth + ((LinearLayout.LayoutParams) layoutParams).leftMargin) + this.B);
                    }
                    i14++;
                }
                return;
            }
            int paddingLeft = getPaddingLeft();
            while (i14 < childCount) {
                View childAt2 = getChildAt(i14);
                ActionMenuView.LayoutParams layoutParams2 = (ActionMenuView.LayoutParams) childAt2.getLayoutParams();
                if (childAt2.getVisibility() != 8) {
                    int i20 = paddingLeft + ((LinearLayout.LayoutParams) layoutParams2).leftMargin;
                    int measuredWidth2 = childAt2.getMeasuredWidth();
                    int measuredHeight2 = childAt2.getMeasuredHeight();
                    int i21 = i17 - (measuredHeight2 / 2);
                    childAt2.layout(i20, i21, i20 + measuredWidth2, measuredHeight2 + i21);
                    paddingLeft = i20 + measuredWidth2 + ((LinearLayout.LayoutParams) layoutParams2).rightMargin + this.B;
                }
                i14++;
            }
            return;
        }
        if (b10) {
            int paddingLeft2 = getPaddingLeft();
            boolean z11 = true;
            for (int i22 = childCount - 1; i22 >= 0; i22--) {
                View childAt3 = getChildAt(i22);
                ActionMenuView.LayoutParams layoutParams3 = (ActionMenuView.LayoutParams) childAt3.getLayoutParams();
                if (childAt3.getVisibility() != 8) {
                    paddingLeft2 += ((LinearLayout.LayoutParams) layoutParams3).leftMargin;
                    if (z11) {
                        if ((childAt3 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt3).getText())) {
                            paddingLeft2 += this.C;
                        }
                        z11 = false;
                    }
                    int measuredWidth3 = childAt3.getMeasuredWidth();
                    int measuredHeight3 = childAt3.getMeasuredHeight();
                    int i23 = i17 - (measuredHeight3 / 2);
                    if (i22 == 0 && i15 > 1) {
                        int width2 = ((getWidth() - getPaddingRight()) - ((LinearLayout.LayoutParams) layoutParams3).rightMargin) - measuredWidth3;
                        if ((childAt3 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt3).getText())) {
                            width2 -= this.E;
                        }
                        childAt3.layout(width2, i23, measuredWidth3 + width2, measuredHeight3 + i23);
                    } else {
                        childAt3.layout(paddingLeft2, i23, paddingLeft2 + measuredWidth3, measuredHeight3 + i23);
                        paddingLeft2 += measuredWidth3 + ((LinearLayout.LayoutParams) layoutParams3).rightMargin + this.B;
                    }
                }
            }
            return;
        }
        int width3 = getWidth() - getPaddingRight();
        boolean z12 = true;
        for (int i24 = childCount - 1; i24 >= 0; i24--) {
            View childAt4 = getChildAt(i24);
            ActionMenuView.LayoutParams layoutParams4 = (ActionMenuView.LayoutParams) childAt4.getLayoutParams();
            if (childAt4.getVisibility() != 8) {
                width3 -= ((LinearLayout.LayoutParams) layoutParams4).rightMargin;
                if (z12) {
                    if ((childAt4 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt4).getText())) {
                        width3 -= this.C;
                    }
                    z12 = false;
                }
                int measuredWidth4 = childAt4.getMeasuredWidth();
                int measuredHeight4 = childAt4.getMeasuredHeight();
                int i25 = i17 - (measuredHeight4 / 2);
                if (i24 == 0 && i15 > 1) {
                    int paddingLeft3 = getPaddingLeft() + ((LinearLayout.LayoutParams) layoutParams4).leftMargin;
                    if ((childAt4 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt4).getText())) {
                        paddingLeft3 += this.E;
                    }
                    childAt4.layout(paddingLeft3, i25, measuredWidth4 + paddingLeft3, measuredHeight4 + i25);
                } else {
                    childAt4.layout(width3 - measuredWidth4, i25, width3, measuredHeight4 + i25);
                    width3 -= (measuredWidth4 + ((LinearLayout.LayoutParams) layoutParams4).leftMargin) + this.B;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.ActionMenuView, androidx.appcompat.widget.LinearLayoutCompat, android.view.View
    public void onMeasure(int i10, int i11) {
        if (this.f7969z == null) {
            super.onMeasure(i10, i11);
            return;
        }
        this.D = true;
        if ((getParent() instanceof COUIToolbar) && ((COUIToolbar) getParent()).getIsTitleCenterStyle()) {
            this.D = false;
        }
        setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        boolean z10 = ViewCompat.x(this) == 1;
        int size = View.MeasureSpec.getSize(i10);
        View.MeasureSpec.getSize(i11);
        F();
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 < getChildCount(); i14++) {
            View childAt = getChildAt(i14);
            i12 += E(childAt, i10, i12, i11, 0);
            if (childAt.getMeasuredHeight() > i13) {
                i13 = childAt.getMeasuredHeight();
            }
        }
        if (this.D) {
            int childCount = getChildCount();
            if (childCount > 0) {
                int i15 = -1;
                int i16 = 0;
                for (int i17 = 0; i17 < childCount; i17++) {
                    if (getChildAt(i17).getVisibility() != 8) {
                        i16++;
                        i15 = i17;
                    }
                }
                int i18 = i12 + ((i16 - 1) * this.B);
                if (i15 != -1) {
                    View childAt2 = getChildAt(i15);
                    if ((childAt2 instanceof TextView) && !TextUtils.isEmpty(((TextView) childAt2).getText())) {
                        i18 += this.C;
                    }
                }
                size = i18;
            } else {
                size = 0;
            }
            if (z10) {
                setPadding(getPaddingLeft(), getPaddingTop(), 0, getPaddingBottom());
            }
        }
        setMeasuredDimension(size, i13);
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public boolean p() {
        View view;
        ArrayList<PopupListItem> arrayList;
        Context context = getContext();
        int i10 = 0;
        if (context != null && (context instanceof Activity)) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return false;
            }
        }
        if (this.f7960q == null || (view = this.Q) == null || view.getParent() == null) {
            return false;
        }
        this.f7961r.clear();
        while (i10 < this.f7969z.getNonActionItems().size()) {
            MenuItemImpl menuItemImpl = this.f7969z.getNonActionItems().get(i10);
            this.f7962s = menuItemImpl;
            this.f7961r.add(new PopupListItem(menuItemImpl.getIcon(), this.f7962s.getTitle() != null ? this.f7962s.getTitle().toString() : "", this.f7962s.isCheckable(), this.f7962s.isChecked(), this.F.containsKey(Integer.valueOf(this.f7962s.getItemId())) ? this.F.get(Integer.valueOf(this.f7962s.getItemId())).intValue() : -1, this.f7962s.isEnabled(), (this.V != i10 || (arrayList = this.U) == null || arrayList.size() <= 0) ? null : this.U));
            i10++;
        }
        ((BaseAdapter) this.f7960q.z().getAdapter()).notifyDataSetChanged();
        this.f7960q.R(this.Q);
        return true;
    }

    @Override // androidx.appcompat.widget.ActionMenuView
    public void setOverflowReserved(boolean z10) {
        ArrayList<PopupListItem> arrayList;
        super.setOverflowReserved(z10);
        COUIPopupListWindow cOUIPopupListWindow = this.f7960q;
        if (cOUIPopupListWindow == null || !cOUIPopupListWindow.isShowing()) {
            return;
        }
        this.f7961r.clear();
        if (this.f7969z.getNonActionItems().size() == 0) {
            ((BaseAdapter) this.f7960q.z().getAdapter()).notifyDataSetChanged();
            this.f7960q.dismiss();
            return;
        }
        int i10 = 0;
        while (i10 < this.f7969z.getNonActionItems().size()) {
            MenuItemImpl menuItemImpl = this.f7969z.getNonActionItems().get(i10);
            this.f7962s = menuItemImpl;
            this.f7961r.add(new PopupListItem(menuItemImpl.getIcon(), this.f7962s.getTitle() != null ? this.f7962s.getTitle().toString() : "", this.f7962s.isCheckable(), this.f7962s.isChecked(), this.F.containsKey(Integer.valueOf(this.f7962s.getItemId())) ? this.F.get(Integer.valueOf(this.f7962s.getItemId())).intValue() : -1, this.f7962s.isEnabled(), (this.V != i10 || (arrayList = this.U) == null || arrayList.size() <= 0) ? null : this.U));
            i10++;
        }
        ((BaseAdapter) this.f7960q.z().getAdapter()).notifyDataSetChanged();
        this.f7960q.G(false);
        COUIPopupListWindow cOUIPopupListWindow2 = this.f7960q;
        cOUIPopupListWindow2.update(cOUIPopupListWindow2.getWidth(), this.f7960q.getHeight());
    }

    public void setPopupWindowOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.P = onDismissListener;
    }

    public void setSubMenuClickListener(COUISubMenuClickListener cOUISubMenuClickListener) {
        this.W = cOUISubMenuClickListener;
    }

    public COUIActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7969z = null;
        this.A = new ArrayList();
        this.D = true;
        this.E = 0;
        this.U = null;
        this.V = -1;
        this.f7963t = getResources().getDimensionPixelSize(R$dimen.coui_action_menu_item_min_width);
        this.f7964u = getResources().getDimensionPixelSize(R$dimen.overflow_button_padding_horizontal);
        this.f7966w = getResources().getDimensionPixelSize(R$dimen.toolbar_edge_icon_menu_item_margin);
        this.f7967x = getResources().getDimensionPixelSize(R$dimen.toolbar_icon_item_horizontal_offset);
        this.f7968y = getResources().getDimensionPixelSize(R$dimen.toolbar_item_vertical_offset);
        this.B = getResources().getDimensionPixelSize(R$dimen.coui_actionbar_menuitemview_item_spacing);
        this.F = new HashMap<>();
        this.I = getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_red_dot_horizontal_offset);
        this.J = getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_red_dot_vertical_offset);
        this.K = getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_red_dot_with_number_vertical_offset);
        this.L = getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_red_dot_with_number_horizontal_offset);
        this.M = getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_red_dot_with_big_number_horizontal_offset);
        this.N = getResources().getDimensionPixelSize(R$dimen.coui_toolbar_menu_icon_top_padding);
        this.O = new COUIHintRedDotHelper(getContext(), null, R$styleable.COUIHintRedDot, 0, R$style.Widget_COUI_COUIHintRedDot_Small);
        this.R = getResources().getString(R$string.abc_action_menu_overflow_description);
        this.S = getResources().getString(com.support.appcompat.R$string.red_dot_description);
        this.T = R$plurals.red_dot_with_number_description;
    }
}
