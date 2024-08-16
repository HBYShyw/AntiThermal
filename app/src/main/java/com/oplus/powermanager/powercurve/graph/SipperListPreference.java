package com.oplus.powermanager.powercurve.graph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;
import b9.PowerSipper;
import b9.PowerUtils;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerControlActivity;
import com.oplus.powermanager.fuelgaue.base.ViewGroupUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SipperListPreference extends COUIPreference {
    private View F;
    private ViewGroup G;
    private View H;
    private int I;
    private boolean J;
    private COUIRecyclerView K;
    private Context L;
    private List<PowerSipper> M;
    private boolean N;
    private c O;

    /* loaded from: classes2.dex */
    class a implements View.OnTouchListener {
        a() {
        }

        @Override // android.view.View.OnTouchListener
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /* loaded from: classes2.dex */
    class b extends LinearLayoutManager {
        b(Context context, int i10, boolean z10) {
            super(context, i10, z10);
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.p
        public boolean l() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class c extends RecyclerView.h {

        /* renamed from: a, reason: collision with root package name */
        List<PowerSipper> f10335a;

        /* loaded from: classes2.dex */
        private class a extends RecyclerView.c0 {

            /* renamed from: a, reason: collision with root package name */
            int f10337a;

            /* renamed from: b, reason: collision with root package name */
            View f10338b;

            /* renamed from: c, reason: collision with root package name */
            ImageView f10339c;

            /* renamed from: d, reason: collision with root package name */
            ImageView f10340d;

            /* renamed from: e, reason: collision with root package name */
            TextView f10341e;

            /* renamed from: f, reason: collision with root package name */
            TextView f10342f;

            /* renamed from: g, reason: collision with root package name */
            View f10343g;

            /* renamed from: com.oplus.powermanager.powercurve.graph.SipperListPreference$c$a$a, reason: collision with other inner class name */
            /* loaded from: classes2.dex */
            class ViewOnClickListenerC0026a implements View.OnClickListener {

                /* renamed from: e, reason: collision with root package name */
                final /* synthetic */ c f10345e;

                ViewOnClickListenerC0026a(c cVar) {
                    this.f10345e = cVar;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    a aVar = a.this;
                    PowerSipper powerSipper = c.this.f10335a.get(aVar.f10337a);
                    Intent intent = new Intent(SipperListPreference.this.L, (Class<?>) PowerControlActivity.class);
                    intent.putExtras(PowerUtils.e(a.this.f10341e.getText().toString(), powerSipper));
                    intent.putExtra("navigate_title_id", R.string.power_usage_details);
                    intent.putExtra("drainType", powerSipper.f4601e);
                    intent.putExtra("title", a.this.f10341e.getText().toString());
                    intent.putExtra("pkgName", powerSipper.f4603g);
                    intent.putExtra("callingSource", 0);
                    intent.setFlags(603979776);
                    SipperListPreference.this.L.startActivity(intent);
                }
            }

            public a(View view) {
                super(view);
                this.f10338b = view;
                this.f10339c = (ImageView) view.findViewById(R.id.item_arrow_icon);
                this.f10340d = (ImageView) view.findViewById(R.id.icon);
                this.f10341e = (TextView) view.findViewById(R.id.title);
                this.f10342f = (TextView) view.findViewById(R.id.summary);
                this.f10343g = view.findViewById(R.id.divider_line);
                view.setOnClickListener(new ViewOnClickListenerC0026a(c.this));
            }

            public void a(boolean z10) {
                View view = this.f10338b;
                if (view != null) {
                    view.setClickable(z10);
                }
            }

            public void b(int i10) {
                this.f10337a = i10;
            }
        }

        public c() {
            this.f10335a = SipperListPreference.this.M;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void e(List<PowerSipper> list) {
            LocalLog.a("SipperListPreference", "adapter addAll");
            this.f10335a.addAll(list);
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void f() {
            this.f10335a.clear();
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemCount() {
            return this.f10335a.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public void onBindViewHolder(RecyclerView.c0 c0Var, int i10) {
            Log.d("SipperListPreference", "onBindViewHolder");
            COUICardListHelper.d(c0Var.itemView, COUICardListHelper.a(getItemCount(), i10));
            a aVar = (a) c0Var;
            aVar.f10340d.setImageDrawable(this.f10335a.get(i10).f4614r);
            aVar.f10339c.setVisibility(this.f10335a.get(i10).b() ? 0 : 4);
            aVar.f10341e.setText(this.f10335a.get(i10).f4618v);
            aVar.f10342f.setText(this.f10335a.get(i10).f4619w);
            aVar.a(this.f10335a.get(i10).f4620x);
            aVar.b(i10);
            if (i10 < this.f10335a.size() - 1) {
                aVar.f10343g.setVisibility(0);
            } else {
                aVar.f10343g.setVisibility(8);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public RecyclerView.c0 onCreateViewHolder(ViewGroup viewGroup, int i10) {
            Log.d("SipperListPreference", "onCreateViewHolder");
            return new a(LayoutInflater.from(SipperListPreference.this.L).inflate(R.layout.power_sipper_pref, viewGroup, false));
        }
    }

    public SipperListPreference(Context context) {
        super(context);
        this.I = -1;
        this.K = null;
        this.L = null;
        this.M = new ArrayList();
        this.N = true;
        this.O = null;
        this.L = context;
        this.O = new c();
        setLayoutResource(R.layout.sipper_list);
    }

    private void q() {
        if (this.F != null) {
            this.F.setPadding(0, this.L.getResources().getDimensionPixelSize(R.dimen.power_ranking_sipper_none_layout_margin_top), 0, 0);
            this.G.setPadding(0, this.L.getResources().getDimensionPixelSize(R.dimen.power_ranking_sipper_refresh_layout_margin_top), 0, 0);
            View findViewById = this.F.findViewById(R.id.view1);
            findViewById.setVisibility(this.L.getResources().getInteger(R.integer.power_ranking_sipper_none_layout_icon_visibility) == 0 ? 0 : 8);
            ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
            layoutParams.width = this.L.getResources().getDimensionPixelSize(R.dimen.power_ranking_sipper_none_icon_width);
            layoutParams.height = this.L.getResources().getDimensionPixelSize(R.dimen.power_ranking_sipper_none_icon_height);
            findViewById.setLayoutParams(layoutParams);
            LocalLog.a("SipperListPreference", "updateSipperStatusLayout");
        }
    }

    public void j() {
        this.O.f();
    }

    public void l(Configuration configuration) {
        q();
    }

    public void m(boolean z10) {
        this.J = z10;
        r(this.I);
    }

    public void n(boolean z10) {
        View view = this.H;
        if (view != null) {
            view.setVisibility(z10 ? 0 : 8);
        }
    }

    public void o() {
        j();
        r(1);
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        LocalLog.a("SipperListPreference", "Preference onBindViewHolder");
        this.F = preferenceViewHolder.a(R.id.sipper_none_layout);
        this.G = (ViewGroup) preferenceViewHolder.a(R.id.sipper_refresh_layout);
        View a10 = preferenceViewHolder.a(R.id.sipper_status_layout);
        this.H = a10;
        a10.setOnTouchListener(new a());
        int i10 = this.I;
        if (i10 != -1) {
            r(i10);
        } else {
            r(1);
        }
        COUIRecyclerView cOUIRecyclerView = this.K;
        if (cOUIRecyclerView != null && this.O != cOUIRecyclerView.getAdapter()) {
            LocalLog.a("SipperListPreference", "set adapter");
            this.K.setAdapter(this.O);
        } else {
            this.K = (COUIRecyclerView) preferenceViewHolder.a(R.id.sipper_list);
            this.K.setLayoutManager(new b(this.L, 1, false));
            this.K.setAdapter(this.O);
        }
    }

    public void p(List<PowerSipper> list) {
        j();
        if (list.size() == 0) {
            LocalLog.a("SipperListPreference", "list is empty");
            r(0);
        } else {
            r(-1);
            this.O.e(list);
        }
    }

    public void r(int i10) {
        this.I = i10;
        LocalLog.a("SipperListPreference", "updateStatus: " + hashCode() + " " + this.J + " " + i10);
        if (i10 == -1) {
            n(false);
            ViewGroupUtil.setChildVisibility(this.G, 4);
            return;
        }
        if (i10 != 0) {
            if (i10 == 1 && this.G != null) {
                n(true);
                this.F.setVisibility(8);
                ViewGroupUtil.setVisibilityWithChild(this.G, this.J ? 0 : 8);
                return;
            }
            return;
        }
        if (this.F != null) {
            n(true);
            this.F.setVisibility(0);
            ViewGroupUtil.setVisibilityWithChild(this.G, 8);
        }
    }

    public SipperListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.I = -1;
        this.K = null;
        this.L = null;
        this.M = new ArrayList();
        this.N = true;
        this.O = null;
        this.L = context;
        this.O = new c();
        setLayoutResource(R.layout.sipper_list);
    }

    public SipperListPreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.I = -1;
        this.K = null;
        this.L = null;
        this.M = new ArrayList();
        this.N = true;
        this.O = null;
        this.L = context;
        this.O = new c();
        setLayoutResource(R.layout.sipper_list);
    }

    public SipperListPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.I = -1;
        this.K = null;
        this.L = null;
        this.M = new ArrayList();
        this.N = true;
        this.O = null;
        this.L = context;
        this.O = new c();
        setLayoutResource(R.layout.sipper_list);
    }

    public SipperListPreference(Context context, List<PowerSipper> list) {
        super(context);
        this.I = -1;
        this.K = null;
        this.L = null;
        new ArrayList();
        this.N = true;
        this.O = null;
        this.M = list;
        this.L = context;
        this.O = new c();
        setLayoutResource(R.layout.sipper_list);
    }
}
