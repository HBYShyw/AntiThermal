package com.oplus.powermanager.powercurve.graph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.oplus.battery.R;
import f3.COUIToolTips;
import l8.IPowerRankingUpdate;

/* loaded from: classes2.dex */
public class PowerCurvePreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private UsageGraph f10320e;

    /* renamed from: f, reason: collision with root package name */
    private IPowerRankingUpdate f10321f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f10322g;

    /* renamed from: h, reason: collision with root package name */
    private COUIToolTips f10323h;

    /* loaded from: classes2.dex */
    class a implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f10324e;

        a(View view) {
            this.f10324e = view;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PowerCurvePreference.this.d(this.f10324e);
        }
    }

    /* loaded from: classes2.dex */
    class b implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f10326e;

        b(View view) {
            this.f10326e = view;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PowerCurvePreference.this.d(this.f10326e);
        }
    }

    public PowerCurvePreference(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(View view) {
        if (this.f10323h == null) {
            this.f10323h = new COUIToolTips(getContext(), 1);
        }
        this.f10323h.y(View.inflate(getContext(), R.layout.power_ranking_help, null));
        this.f10323h.A(true);
        this.f10323h.B(view, 4);
    }

    public void e(IPowerRankingUpdate iPowerRankingUpdate) {
        this.f10321f = iPowerRankingUpdate;
    }

    public void f(boolean z10) {
        this.f10322g = z10;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        LocalLog.a("PowerCurvePreference", "onBindViewHolder");
        super.onBindViewHolder(preferenceViewHolder);
        UsageGraph usageGraph = (UsageGraph) preferenceViewHolder.a(R.id.usage_graph);
        this.f10320e = usageGraph;
        usageGraph.setPowerRankingUpdate(this.f10321f);
        this.f10320e.setTouchEnable(this.f10322g);
        preferenceViewHolder.itemView.setBackground(getContext().getDrawable(R.drawable.ic_top_radius16_background));
        View a10 = preferenceViewHolder.a(R.id.tips_text);
        RelativeLayout relativeLayout = (RelativeLayout) preferenceViewHolder.a(R.id.curve_help_layout);
        View a11 = preferenceViewHolder.a(R.id.curve_help_icon);
        relativeLayout.setOnClickListener(new a(a11));
        a10.setOnClickListener(new b(a11));
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        LocalLog.a("PowerCurvePreference", "onDetached");
        e(null);
        UsageGraph usageGraph = this.f10320e;
        if (usageGraph != null) {
            usageGraph.setPowerRankingUpdate(null);
            this.f10320e = null;
        }
        super.onDetached();
    }

    public PowerCurvePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PowerCurvePreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public PowerCurvePreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10320e = null;
        this.f10321f = null;
        this.f10322g = true;
        setLayoutResource(R.layout.curve_layout);
        setSelectable(false);
    }
}
