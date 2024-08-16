package g2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.widget.CompoundButtonCompat;
import b3.COUITintUtil;
import c.AppCompatResources;
import com.coui.appcompat.reddot.COUIHintRedDot;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;
import com.support.appcompat.R$style;
import java.util.List;
import z2.COUIChangeTextUtil;

/* compiled from: DefaultAdapter.java */
/* renamed from: g2.d, reason: use source file name */
/* loaded from: classes.dex */
public class DefaultAdapter extends BaseAdapter {

    /* renamed from: e, reason: collision with root package name */
    private Context f11528e;

    /* renamed from: f, reason: collision with root package name */
    private List<PopupListItem> f11529f;

    /* renamed from: g, reason: collision with root package name */
    private int f11530g;

    /* renamed from: h, reason: collision with root package name */
    private int f11531h;

    /* renamed from: i, reason: collision with root package name */
    private int f11532i;

    /* renamed from: j, reason: collision with root package name */
    private int f11533j;

    /* renamed from: k, reason: collision with root package name */
    private int f11534k;

    /* renamed from: l, reason: collision with root package name */
    private int f11535l;

    /* renamed from: m, reason: collision with root package name */
    private int f11536m;

    /* renamed from: n, reason: collision with root package name */
    private ColorStateList f11537n;

    /* renamed from: o, reason: collision with root package name */
    private int f11538o;

    /* renamed from: p, reason: collision with root package name */
    private float f11539p;

    /* renamed from: q, reason: collision with root package name */
    private float f11540q;

    /* renamed from: r, reason: collision with root package name */
    private View.AccessibilityDelegate f11541r;

    /* renamed from: s, reason: collision with root package name */
    private ColorStateList f11542s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f11543t = false;

    /* compiled from: DefaultAdapter.java */
    /* renamed from: g2.d$a */
    /* loaded from: classes.dex */
    class a extends View.AccessibilityDelegate {
        a() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("");
        }
    }

    /* compiled from: DefaultAdapter.java */
    /* renamed from: g2.d$b */
    /* loaded from: classes.dex */
    static class b {

        /* renamed from: a, reason: collision with root package name */
        ImageView f11545a;

        /* renamed from: b, reason: collision with root package name */
        TextView f11546b;

        /* renamed from: c, reason: collision with root package name */
        CheckBox f11547c;

        /* renamed from: d, reason: collision with root package name */
        COUIHintRedDot f11548d;

        /* renamed from: e, reason: collision with root package name */
        LinearLayout f11549e;

        /* renamed from: f, reason: collision with root package name */
        ImageView f11550f;

        b() {
        }
    }

    public DefaultAdapter(Context context, List<PopupListItem> list) {
        this.f11528e = context;
        this.f11529f = list;
        Resources resources = context.getResources();
        this.f11530g = resources.getDimensionPixelSize(R$dimen.coui_popup_list_padding_vertical);
        this.f11531h = resources.getDimensionPixelSize(R$dimen.coui_popup_list_window_item_padding_top_and_bottom);
        this.f11532i = resources.getDimensionPixelSize(R$dimen.coui_popup_list_window_item_min_height);
        this.f11533j = resources.getDimensionPixelOffset(R$dimen.coui_popup_list_window_content_min_width_with_checkbox);
        this.f11534k = this.f11528e.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_window_item_title_margin_with_no_icon);
        this.f11535l = this.f11528e.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_window_item_title_margin_left);
        this.f11536m = this.f11528e.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_window_item_title_margin_right);
        this.f11539p = this.f11528e.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_window_item_title_text_size);
        this.f11540q = this.f11528e.getResources().getConfiguration().fontScale;
        this.f11541r = new a();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{R$attr.couiPopupListWindowTextColor, R$attr.couiColorPrimaryTextOnPopup});
        this.f11537n = AppCompatResources.a(this.f11528e, R$color.coui_popup_list_window_text_color_light);
        this.f11538o = obtainStyledAttributes.getColor(1, this.f11528e.getResources().getColor(R$color.coui_popup_list_selected_text_color));
        obtainStyledAttributes.recycle();
    }

    private void a(LinearLayout linearLayout, CheckBox checkBox, ImageView imageView, TextView textView, PopupListItem popupListItem, boolean z10) {
        boolean h10 = popupListItem.h();
        if (popupListItem.i()) {
            int minimumWidth = linearLayout.getMinimumWidth();
            int i10 = this.f11533j;
            if (minimumWidth != i10) {
                linearLayout.setMinimumWidth(i10);
            }
            if (h10) {
                imageView.setVisibility(0);
                checkBox.setVisibility(8);
                return;
            }
            imageView.setVisibility(8);
            checkBox.setVisibility(0);
            checkBox.setChecked(popupListItem.j());
            checkBox.setEnabled(z10);
            if (popupListItem.j()) {
                textView.setTextColor(this.f11538o);
                COUITintUtil.c(CompoundButtonCompat.a(checkBox), ColorStateList.valueOf(this.f11538o));
                return;
            }
            return;
        }
        if (linearLayout.getMinimumWidth() == this.f11533j) {
            linearLayout.setMinimumWidth(0);
        }
        checkBox.setVisibility(8);
        imageView.setVisibility(h10 ? 0 : 8);
    }

    private void b(ImageView imageView, TextView textView, PopupListItem popupListItem, boolean z10) {
        Drawable a10;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        if (popupListItem.b() == 0 && popupListItem.a() == null) {
            imageView.setVisibility(8);
            layoutParams.setMarginStart(this.f11534k);
            if (popupListItem.c() == -1 && !popupListItem.i()) {
                layoutParams.setMarginEnd(this.f11534k);
            } else {
                layoutParams.setMarginEnd(0);
            }
        } else {
            imageView.setVisibility(0);
            layoutParams.setMarginStart(this.f11535l);
            if (popupListItem.c() == -1 && !popupListItem.i()) {
                layoutParams.setMarginEnd(this.f11536m);
            } else {
                layoutParams.setMarginEnd(0);
            }
            if (popupListItem.a() == null) {
                a10 = this.f11528e.getResources().getDrawable(popupListItem.b());
            } else {
                a10 = popupListItem.a();
            }
            imageView.setImageDrawable(a10);
        }
        textView.setLayoutParams(layoutParams);
    }

    private void c(PopupListItem popupListItem, COUIHintRedDot cOUIHintRedDot) {
        cOUIHintRedDot.setPointNumber(popupListItem.c());
        int c10 = popupListItem.c();
        if (c10 == -1) {
            cOUIHintRedDot.setPointMode(0);
        } else if (c10 != 0) {
            cOUIHintRedDot.setPointMode(2);
            cOUIHintRedDot.setVisibility(0);
        } else {
            cOUIHintRedDot.setPointMode(1);
            cOUIHintRedDot.setVisibility(0);
        }
    }

    private void e(TextView textView, PopupListItem popupListItem, boolean z10) {
        textView.setEnabled(z10);
        textView.setTextAppearance(R$style.couiTextAppearanceHeadline6);
        textView.setText(popupListItem.e());
        textView.setTextColor(this.f11537n);
        ColorStateList colorStateList = this.f11542s;
        if (colorStateList != null) {
            textView.setTextColor(colorStateList);
        } else if (popupListItem.g() != null) {
            textView.setTextColor(popupListItem.g());
        } else if (popupListItem.f() >= 0) {
            textView.setTextColor(popupListItem.f());
        }
        textView.setTextSize(0, COUIChangeTextUtil.e(this.f11539p, this.f11540q, 5));
    }

    public void d(int i10) {
        this.f11538o = i10;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f11529f.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i10) {
        return this.f11529f.get(i10);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        return i10;
    }

    @Override // android.widget.Adapter
    public View getView(int i10, View view, ViewGroup viewGroup) {
        b bVar;
        if (view == null) {
            b bVar2 = new b();
            View inflate = LayoutInflater.from(this.f11528e).inflate(R$layout.coui_popup_list_window_item, viewGroup, false);
            bVar2.f11545a = (ImageView) inflate.findViewById(R$id.popup_list_window_item_icon);
            bVar2.f11546b = (TextView) inflate.findViewById(R$id.popup_list_window_item_title);
            bVar2.f11549e = (LinearLayout) inflate.findViewById(R$id.content);
            bVar2.f11548d = (COUIHintRedDot) inflate.findViewById(R$id.red_dot);
            bVar2.f11547c = (CheckBox) inflate.findViewById(R$id.checkbox);
            bVar2.f11550f = (ImageView) inflate.findViewById(R$id.arrow);
            CheckBox checkBox = bVar2.f11547c;
            if (checkBox != null) {
                checkBox.setAccessibilityDelegate(this.f11541r);
                bVar2.f11547c.setBackground(null);
            }
            if (this.f11543t) {
                COUIChangeTextUtil.c(bVar2.f11546b, 4);
            }
            inflate.setTag(bVar2);
            bVar = bVar2;
            view = inflate;
        } else {
            bVar = (b) view.getTag();
        }
        if (getCount() == 1) {
            view.setMinimumHeight(this.f11532i + (this.f11530g * 2));
            int i11 = this.f11531h;
            int i12 = this.f11530g;
            view.setPadding(0, i11 + i12, 0, i11 + i12);
        } else if (i10 == 0) {
            view.setMinimumHeight(this.f11532i + this.f11530g);
            int i13 = this.f11531h;
            view.setPadding(0, this.f11530g + i13, 0, i13);
        } else if (i10 == getCount() - 1) {
            view.setMinimumHeight(this.f11532i + this.f11530g);
            int i14 = this.f11531h;
            view.setPadding(0, i14, 0, this.f11530g + i14);
        } else {
            view.setMinimumHeight(this.f11532i);
            int i15 = this.f11531h;
            view.setPadding(0, i15, 0, i15);
        }
        boolean k10 = this.f11529f.get(i10).k();
        view.setEnabled(k10);
        c(this.f11529f.get(i10), bVar.f11548d);
        b(bVar.f11545a, bVar.f11546b, this.f11529f.get(i10), k10);
        e(bVar.f11546b, this.f11529f.get(i10), k10);
        a((LinearLayout) view, bVar.f11547c, bVar.f11550f, bVar.f11546b, this.f11529f.get(i10), k10);
        return view;
    }
}
