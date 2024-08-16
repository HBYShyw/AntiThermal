package y1;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$style;

/* compiled from: COUIListDialogAdapter.java */
/* renamed from: y1.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIListDialogAdapter extends BaseAdapter {

    /* renamed from: e, reason: collision with root package name */
    private final int f19798e;

    /* renamed from: f, reason: collision with root package name */
    private Context f19799f;

    /* renamed from: g, reason: collision with root package name */
    private CharSequence[] f19800g;

    /* renamed from: h, reason: collision with root package name */
    private int[] f19801h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f19802i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f19803j;

    /* compiled from: COUIListDialogAdapter.java */
    /* renamed from: y1.a$a */
    /* loaded from: classes.dex */
    public class a {

        /* renamed from: a, reason: collision with root package name */
        TextView f19804a;

        /* renamed from: b, reason: collision with root package name */
        ImageView f19805b;

        /* renamed from: c, reason: collision with root package name */
        LinearLayout f19806c;

        public a() {
        }
    }

    private View b(int i10, View view, ViewGroup viewGroup) {
        a aVar;
        if (view == null) {
            view = LayoutInflater.from(this.f19799f).inflate(this.f19798e, viewGroup, false);
            aVar = new a();
            aVar.f19804a = (TextView) view.findViewById(R.id.text1);
            aVar.f19805b = (ImageView) view.findViewById(R$id.item_divider);
            aVar.f19806c = (LinearLayout) view.findViewById(R$id.main_layout);
            view.setTag(aVar);
        } else {
            aVar = (a) view.getTag();
        }
        aVar.f19804a.setText(getItem(i10));
        int[] iArr = this.f19801h;
        if (iArr != null) {
            int i11 = iArr[i10];
            if (i11 > 0) {
                aVar.f19804a.setTextAppearance(this.f19799f, i11);
            } else {
                aVar.f19804a.setTextAppearance(this.f19799f, R$style.DefaultDialogItemTextStyle);
            }
        }
        if (aVar.f19805b != null) {
            if (getCount() > 1 && i10 != getCount() - 1) {
                aVar.f19805b.setVisibility(0);
            } else {
                aVar.f19805b.setVisibility(8);
            }
        }
        return view;
    }

    private void c(int i10, View view) {
        int dimensionPixelSize = this.f19799f.getResources().getDimensionPixelSize(R$dimen.coui_bottom_alert_dialog_vertical_button_padding_bottom_extra_new);
        Resources resources = this.f19799f.getResources();
        int i11 = R$dimen.coui_bottom_alert_dialog_vertical_button_padding_vertical_new;
        int dimensionPixelSize2 = resources.getDimensionPixelSize(i11);
        int dimensionPixelSize3 = this.f19799f.getResources().getDimensionPixelSize(R$dimen.alert_dialog_list_item_padding_left);
        int dimensionPixelSize4 = this.f19799f.getResources().getDimensionPixelSize(i11);
        int dimensionPixelSize5 = this.f19799f.getResources().getDimensionPixelSize(R$dimen.alert_dialog_list_item_padding_right);
        this.f19799f.getResources().getDimensionPixelSize(R$dimen.alert_dialog_list_item_min_height);
        if (i10 == getCount() - 1 && this.f19803j) {
            view.setPadding(dimensionPixelSize3, dimensionPixelSize2, dimensionPixelSize5, dimensionPixelSize4 + dimensionPixelSize);
        } else if (i10 == 0 && this.f19802i) {
            view.setPadding(dimensionPixelSize3, dimensionPixelSize2 + dimensionPixelSize, dimensionPixelSize5, dimensionPixelSize4);
        } else {
            view.setPadding(dimensionPixelSize3, dimensionPixelSize2, dimensionPixelSize5, dimensionPixelSize4);
        }
    }

    @Override // android.widget.Adapter
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public CharSequence getItem(int i10) {
        CharSequence[] charSequenceArr = this.f19800g;
        if (charSequenceArr == null) {
            return null;
        }
        return charSequenceArr[i10];
    }

    public void d(boolean z10) {
        this.f19803j = z10;
    }

    public void e(boolean z10) {
        this.f19802i = z10;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        CharSequence[] charSequenceArr = this.f19800g;
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
        View b10 = b(i10, view, viewGroup);
        c(i10, b10.findViewById(R$id.main_layout));
        return b10;
    }
}
