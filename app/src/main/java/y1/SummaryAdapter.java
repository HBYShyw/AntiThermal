package y1;

import android.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$layout;

/* compiled from: SummaryAdapter.java */
/* renamed from: y1.c, reason: use source file name */
/* loaded from: classes.dex */
public class SummaryAdapter extends BaseAdapter {

    /* renamed from: k, reason: collision with root package name */
    private static final int f19830k = R$layout.coui_alert_dialog_summary_item;

    /* renamed from: e, reason: collision with root package name */
    private boolean f19831e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f19832f;

    /* renamed from: g, reason: collision with root package name */
    private Context f19833g;

    /* renamed from: h, reason: collision with root package name */
    private CharSequence[] f19834h;

    /* renamed from: i, reason: collision with root package name */
    private CharSequence[] f19835i;

    /* renamed from: j, reason: collision with root package name */
    private int[] f19836j;

    /* compiled from: SummaryAdapter.java */
    /* renamed from: y1.c$b */
    /* loaded from: classes.dex */
    private class b {

        /* renamed from: a, reason: collision with root package name */
        TextView f19837a;

        /* renamed from: b, reason: collision with root package name */
        TextView f19838b;

        /* renamed from: c, reason: collision with root package name */
        ImageView f19839c;

        /* renamed from: d, reason: collision with root package name */
        LinearLayout f19840d;

        private b() {
        }
    }

    public SummaryAdapter(Context context, boolean z10, boolean z11, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, int[] iArr) {
        this.f19831e = z10;
        this.f19832f = z11;
        this.f19833g = context;
        this.f19834h = charSequenceArr;
        this.f19835i = charSequenceArr2;
        this.f19836j = iArr;
    }

    private void c(int i10, View view) {
        int dimensionPixelSize = this.f19833g.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_bottom_extra_new);
        int dimensionPixelSize2 = this.f19833g.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_vertical_new);
        int paddingLeft = view.getPaddingLeft();
        int paddingRight = view.getPaddingRight();
        if (i10 == getCount() - 1 && this.f19832f) {
            view.setPadding(paddingLeft, dimensionPixelSize2, paddingRight, dimensionPixelSize + dimensionPixelSize2);
        } else if (i10 == 0 && this.f19831e) {
            view.setPadding(paddingLeft, dimensionPixelSize + dimensionPixelSize2, paddingRight, dimensionPixelSize2);
        } else {
            view.setPadding(paddingLeft, dimensionPixelSize2, paddingRight, dimensionPixelSize2);
        }
    }

    @Override // android.widget.Adapter
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public CharSequence getItem(int i10) {
        CharSequence[] charSequenceArr = this.f19834h;
        if (charSequenceArr == null) {
            return null;
        }
        return charSequenceArr[i10];
    }

    public CharSequence b(int i10) {
        CharSequence[] charSequenceArr = this.f19835i;
        if (charSequenceArr != null && i10 < charSequenceArr.length) {
            return charSequenceArr[i10];
        }
        return null;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        CharSequence[] charSequenceArr = this.f19834h;
        if (charSequenceArr == null) {
            return 0;
        }
        return charSequenceArr.length;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        return i10;
    }

    @Override // android.widget.Adapter
    public View getView(int i10, View view, ViewGroup viewGroup) {
        b bVar;
        if (view == null) {
            view = LayoutInflater.from(this.f19833g).inflate(f19830k, viewGroup, false);
            bVar = new b();
            bVar.f19837a = (TextView) view.findViewById(R.id.text1);
            bVar.f19838b = (TextView) view.findViewById(R$id.summary_text2);
            bVar.f19839c = (ImageView) view.findViewById(R$id.item_divider);
            bVar.f19840d = (LinearLayout) view.findViewById(R$id.main_layout);
            view.setTag(bVar);
        } else {
            bVar = (b) view.getTag();
        }
        CharSequence item = getItem(i10);
        CharSequence b10 = b(i10);
        bVar.f19837a.setText(item);
        if (TextUtils.isEmpty(b10)) {
            bVar.f19838b.setVisibility(8);
        } else {
            bVar.f19838b.setVisibility(0);
            bVar.f19838b.setText(b10);
        }
        c(i10, bVar.f19840d);
        int[] iArr = this.f19836j;
        if (iArr != null && i10 >= 0 && i10 < iArr.length) {
            bVar.f19837a.setTextColor(iArr[i10]);
        }
        if (bVar.f19839c != null) {
            if (getCount() > 1 && i10 != getCount() - 1) {
                bVar.f19839c.setVisibility(0);
            } else {
                bVar.f19839c.setVisibility(8);
            }
        }
        view.requestLayout();
        return view;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return true;
    }
}
