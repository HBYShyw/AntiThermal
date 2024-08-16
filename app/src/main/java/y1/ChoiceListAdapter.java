package y1;

import android.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.coui.appcompat.checkbox.COUICheckBox;
import com.support.appcompat.R$id;

/* compiled from: ChoiceListAdapter.java */
/* renamed from: y1.b, reason: use source file name */
/* loaded from: classes.dex */
public class ChoiceListAdapter extends BaseAdapter {

    /* renamed from: e, reason: collision with root package name */
    private Context f19808e;

    /* renamed from: f, reason: collision with root package name */
    private CharSequence[] f19809f;

    /* renamed from: g, reason: collision with root package name */
    private CharSequence[] f19810g;

    /* renamed from: h, reason: collision with root package name */
    private int f19811h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f19812i;

    /* renamed from: j, reason: collision with root package name */
    private boolean[] f19813j;

    /* renamed from: k, reason: collision with root package name */
    private boolean[] f19814k;

    /* renamed from: l, reason: collision with root package name */
    private d f19815l;

    /* renamed from: m, reason: collision with root package name */
    private c f19816m;

    /* renamed from: n, reason: collision with root package name */
    private int f19817n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f19818o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f19819p;

    /* compiled from: ChoiceListAdapter.java */
    /* renamed from: y1.b$a */
    /* loaded from: classes.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /* compiled from: ChoiceListAdapter.java */
    /* renamed from: y1.b$b */
    /* loaded from: classes.dex */
    class b implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f19821e;

        b(int i10) {
            this.f19821e = i10;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            View findViewById = view.findViewById(R$id.checkbox);
            if (findViewById instanceof COUICheckBox) {
                COUICheckBox cOUICheckBox = (COUICheckBox) findViewById;
                if (cOUICheckBox.getState() == 2) {
                    cOUICheckBox.setState(0);
                    ChoiceListAdapter.this.f19813j[this.f19821e] = false;
                } else if (ChoiceListAdapter.this.f19817n > 0 && ChoiceListAdapter.this.f19817n <= ChoiceListAdapter.this.g()) {
                    if (ChoiceListAdapter.this.f19816m != null) {
                        ChoiceListAdapter.this.f19816m.a(ChoiceListAdapter.this.f19817n);
                    }
                } else {
                    cOUICheckBox.setState(2);
                    ChoiceListAdapter.this.f19813j[this.f19821e] = true;
                }
                if (ChoiceListAdapter.this.f19815l != null) {
                    ChoiceListAdapter.this.f19815l.a(this.f19821e, cOUICheckBox.getState() == 2);
                    return;
                }
                return;
            }
            if (findViewById instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) findViewById;
                checkBox.setChecked(!checkBox.isChecked());
                if (ChoiceListAdapter.this.f19815l != null) {
                    ChoiceListAdapter.this.f19815l.a(this.f19821e, checkBox.isChecked());
                }
            }
        }
    }

    /* compiled from: ChoiceListAdapter.java */
    /* renamed from: y1.b$c */
    /* loaded from: classes.dex */
    public interface c {
        void a(int i10);
    }

    /* compiled from: ChoiceListAdapter.java */
    /* renamed from: y1.b$d */
    /* loaded from: classes.dex */
    public interface d {
        void a(int i10, boolean z10);
    }

    /* compiled from: ChoiceListAdapter.java */
    /* renamed from: y1.b$e */
    /* loaded from: classes.dex */
    static class e {

        /* renamed from: a, reason: collision with root package name */
        LinearLayout f19823a;

        /* renamed from: b, reason: collision with root package name */
        TextView f19824b;

        /* renamed from: c, reason: collision with root package name */
        TextView f19825c;

        /* renamed from: d, reason: collision with root package name */
        COUICheckBox f19826d;

        /* renamed from: e, reason: collision with root package name */
        FrameLayout f19827e;

        /* renamed from: f, reason: collision with root package name */
        RadioButton f19828f;

        /* renamed from: g, reason: collision with root package name */
        ImageView f19829g;

        e() {
        }
    }

    public ChoiceListAdapter(Context context, int i10, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean z10) {
        this(context, i10, charSequenceArr, charSequenceArr2, zArr, null, z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int g() {
        int i10 = 0;
        for (boolean z10 : this.f19813j) {
            if (z10) {
                i10++;
            }
        }
        return i10;
    }

    private void j(boolean[] zArr) {
        for (int i10 = 0; i10 < zArr.length; i10++) {
            boolean[] zArr2 = this.f19813j;
            if (i10 >= zArr2.length) {
                return;
            }
            zArr2[i10] = zArr[i10];
        }
    }

    private void k(boolean[] zArr) {
        for (int i10 = 0; i10 < zArr.length; i10++) {
            boolean[] zArr2 = this.f19814k;
            if (i10 >= zArr2.length) {
                return;
            }
            zArr2[i10] = zArr[i10];
        }
    }

    public boolean[] f() {
        return this.f19813j;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        CharSequence[] charSequenceArr = this.f19809f;
        if (charSequenceArr == null) {
            return 0;
        }
        return charSequenceArr.length;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        return i10;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i10) {
        return i10;
    }

    @Override // android.widget.Adapter
    public View getView(int i10, View view, ViewGroup viewGroup) {
        View view2;
        e eVar;
        if (view == null) {
            eVar = new e();
            view2 = LayoutInflater.from(this.f19808e).inflate(this.f19811h, viewGroup, false);
            eVar.f19823a = (LinearLayout) view2.findViewById(R$id.text_layout);
            eVar.f19825c = (TextView) view2.findViewById(R.id.text1);
            eVar.f19824b = (TextView) view2.findViewById(R$id.summary_text2);
            eVar.f19829g = (ImageView) view2.findViewById(R$id.item_divider);
            if (this.f19812i) {
                eVar.f19826d = (COUICheckBox) view2.findViewById(R$id.checkbox);
            } else {
                eVar.f19827e = (FrameLayout) view2.findViewById(R$id.radio_layout);
                eVar.f19828f = (RadioButton) view2.findViewById(R$id.radio_button);
            }
            if (this.f19814k[i10]) {
                eVar.f19825c.setEnabled(false);
                eVar.f19824b.setEnabled(false);
                if (this.f19812i) {
                    eVar.f19826d.setEnabled(false);
                } else {
                    eVar.f19828f.setEnabled(false);
                }
                view2.setOnTouchListener(new a());
            }
            view2.setTag(eVar);
        } else {
            view2 = view;
            eVar = (e) view.getTag();
        }
        if (this.f19812i) {
            eVar.f19826d.setState(this.f19813j[i10] ? 2 : 0);
            view2.setOnClickListener(new b(i10));
        } else {
            eVar.f19828f.setChecked(this.f19813j[i10]);
        }
        CharSequence item = getItem(i10);
        CharSequence i11 = i(i10);
        eVar.f19825c.setText(item);
        if (TextUtils.isEmpty(i11)) {
            eVar.f19824b.setVisibility(8);
        } else {
            eVar.f19824b.setVisibility(0);
            eVar.f19824b.setText(i11);
        }
        if (eVar.f19829g != null) {
            if (getCount() != 1 && i10 != getCount() - 1) {
                eVar.f19829g.setVisibility(0);
            } else {
                eVar.f19829g.setVisibility(8);
            }
        }
        return view2;
    }

    @Override // android.widget.Adapter
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public CharSequence getItem(int i10) {
        CharSequence[] charSequenceArr = this.f19809f;
        if (charSequenceArr == null) {
            return null;
        }
        return charSequenceArr[i10];
    }

    public CharSequence i(int i10) {
        CharSequence[] charSequenceArr = this.f19810g;
        if (charSequenceArr != null && i10 < charSequenceArr.length) {
            return charSequenceArr[i10];
        }
        return null;
    }

    public void l(boolean z10) {
        this.f19819p = z10;
    }

    public void m(boolean z10) {
        this.f19818o = z10;
    }

    public ChoiceListAdapter(Context context, int i10, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean[] zArr2, boolean z10) {
        this(context, i10, charSequenceArr, charSequenceArr2, zArr, zArr2, z10, 0);
    }

    public ChoiceListAdapter(Context context, int i10, CharSequence[] charSequenceArr, CharSequence[] charSequenceArr2, boolean[] zArr, boolean[] zArr2, boolean z10, int i11) {
        this.f19818o = false;
        this.f19819p = false;
        this.f19808e = context;
        this.f19811h = i10;
        this.f19809f = charSequenceArr;
        this.f19810g = charSequenceArr2;
        this.f19812i = z10;
        this.f19813j = new boolean[charSequenceArr.length];
        if (zArr != null) {
            j(zArr);
        }
        this.f19814k = new boolean[this.f19809f.length];
        if (zArr2 != null) {
            k(zArr2);
        }
        this.f19817n = i11;
    }
}
