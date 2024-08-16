package androidx.preference;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/* loaded from: classes.dex */
public class DropDownPreference extends ListPreference {

    /* renamed from: p, reason: collision with root package name */
    private final Context f3249p;

    /* renamed from: q, reason: collision with root package name */
    private final ArrayAdapter f3250q;

    /* renamed from: r, reason: collision with root package name */
    private Spinner f3251r;

    /* renamed from: s, reason: collision with root package name */
    private final AdapterView.OnItemSelectedListener f3252s;

    /* loaded from: classes.dex */
    class a implements AdapterView.OnItemSelectedListener {
        a() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i10, long j10) {
            if (i10 >= 0) {
                String charSequence = DropDownPreference.this.m()[i10].toString();
                if (charSequence.equals(DropDownPreference.this.n()) || !DropDownPreference.this.callChangeListener(charSequence)) {
                    return;
                }
                DropDownPreference.this.p(charSequence);
            }
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public DropDownPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.dropdownPreferenceStyle);
    }

    private int r(String str) {
        CharSequence[] m10 = m();
        if (str == null || m10 == null) {
            return -1;
        }
        for (int length = m10.length - 1; length >= 0; length--) {
            if (m10[length].equals(str)) {
                return length;
            }
        }
        return -1;
    }

    private void s() {
        this.f3250q.clear();
        if (j() != null) {
            for (CharSequence charSequence : j()) {
                this.f3250q.add(charSequence.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void notifyChanged() {
        super.notifyChanged();
        ArrayAdapter arrayAdapter = this.f3250q;
        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        Spinner spinner = (Spinner) preferenceViewHolder.itemView.findViewById(R$id.spinner);
        this.f3251r = spinner;
        spinner.setAdapter((SpinnerAdapter) this.f3250q);
        this.f3251r.setOnItemSelectedListener(this.f3252s);
        this.f3251r.setSelection(r(n()));
        super.onBindViewHolder(preferenceViewHolder);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.DialogPreference, androidx.preference.Preference
    public void onClick() {
        this.f3251r.performClick();
    }

    protected ArrayAdapter q() {
        return new ArrayAdapter(this.f3249p, R.layout.simple_spinner_dropdown_item);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f3252s = new a();
        this.f3249p = context;
        this.f3250q = q();
        s();
    }
}
