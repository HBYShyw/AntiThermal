package com.oplus.powermanager.powerusage.view;

import a3.COUITextViewCompatUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.oplus.battery.R;
import f6.f;
import l8.IPowerInspectUpdate;

/* loaded from: classes2.dex */
public class OneKeyTitlePreference extends PreferenceCategory {

    /* renamed from: n, reason: collision with root package name */
    private IPowerInspectUpdate f10398n;

    /* renamed from: o, reason: collision with root package name */
    private Context f10399o;

    /* renamed from: p, reason: collision with root package name */
    private TextView f10400p;

    /* renamed from: q, reason: collision with root package name */
    private TextView f10401q;

    /* renamed from: r, reason: collision with root package name */
    private View f10402r;

    /* renamed from: s, reason: collision with root package name */
    private View f10403s;

    /* renamed from: t, reason: collision with root package name */
    private String f10404t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f10405u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f10406v;

    /* loaded from: classes2.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean z10 = !OneKeyTitlePreference.this.f10398n.l();
            LocalLog.a("OneKeyTitlePreference", "SelectAllTitleView click to " + z10);
            OneKeyTitlePreference.this.f10398n.H(z10);
        }
    }

    public OneKeyTitlePreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f10398n = null;
        setLayoutResource(R.layout.one_key_title_category);
    }

    @Override // androidx.preference.PreferenceCategory, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.a(R.id.one_key_category_title);
        this.f10400p = textView;
        textView.setText(this.f10404t);
        this.f10401q = (TextView) preferenceViewHolder.a(R.id.one_key_select_all_title);
        this.f10402r = preferenceViewHolder.a(R.id.view_top);
        this.f10403s = preferenceViewHolder.a(R.id.view);
        COUITextViewCompatUtil.b(this.f10401q);
        this.f10401q.setOnClickListener(new a());
        if (this.f10404t.equals("")) {
            this.f10400p.setVisibility(8);
            preferenceViewHolder.a(R.id.view).setVisibility(8);
            preferenceViewHolder.a(R.id.view_bottom).setVisibility(8);
        } else {
            this.f10400p.setVisibility(0);
            preferenceViewHolder.a(R.id.view).setVisibility(8);
            if (this.f10404t.equals(this.f10399o.getString(R.string.one_key_resolved_issues))) {
                preferenceViewHolder.a(R.id.view).setVisibility(8);
                preferenceViewHolder.a(R.id.view_top).setVisibility(8);
            }
        }
        v(!this.f10398n.l());
        u(this.f10406v);
    }

    public void t(boolean z10) {
        this.f10405u = z10;
    }

    public void u(boolean z10) {
        View view;
        this.f10406v = z10;
        if (!this.f10404t.equals(this.f10399o.getString(R.string.one_key_resolved_issues)) || this.f10403s == null || (view = this.f10402r) == null) {
            return;
        }
        view.setLayoutParams(new RelativeLayout.LayoutParams(-1, (int) f.j(this.f10399o, 8.0f)));
        this.f10402r.setVisibility(z10 ? 0 : 8);
        this.f10403s.setVisibility(z10 ? 0 : 8);
    }

    public void v(boolean z10) {
        if (this.f10405u && this.f10401q != null && this.f10404t.equals(this.f10400p.getText().toString())) {
            LocalLog.a("OneKeyTitlePreference", "updateSelectStatus: " + z10);
            this.f10401q.setVisibility(0);
            this.f10401q.setText(z10 ? this.f10399o.getString(R.string.one_key_select_all) : this.f10399o.getString(R.string.one_key_select_all_cancel));
        }
    }

    public OneKeyTitlePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10398n = null;
        setLayoutResource(R.layout.one_key_title_category);
    }

    public OneKeyTitlePreference(Context context, String str, boolean z10, IPowerInspectUpdate iPowerInspectUpdate) {
        super(context);
        this.f10404t = str;
        this.f10405u = z10;
        this.f10399o = context;
        this.f10398n = iPowerInspectUpdate;
        setLayoutResource(R.layout.one_key_title_category);
    }
}
