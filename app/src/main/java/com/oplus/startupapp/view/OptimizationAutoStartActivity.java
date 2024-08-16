package com.oplus.startupapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.h0;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;
import com.oplus.startupapp.common.base.BaseActivity;
import da.OptimizationViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import w9.AppNameComparator;
import x9.IconUtils;
import z9.AppToShow;

/* loaded from: classes2.dex */
public class OptimizationAutoStartActivity extends BaseActivity {

    /* renamed from: f, reason: collision with root package name */
    private Button f10512f;

    /* renamed from: g, reason: collision with root package name */
    private RecyclerView f10513g;

    /* renamed from: h, reason: collision with root package name */
    private COUIToolbar f10514h;

    /* renamed from: i, reason: collision with root package name */
    private Drawable f10515i;

    /* renamed from: j, reason: collision with root package name */
    private AppBarLayout f10516j;

    /* renamed from: k, reason: collision with root package name */
    private LinearLayout f10517k;

    /* renamed from: l, reason: collision with root package name */
    private OptimizationViewModel f10518l;

    /* renamed from: m, reason: collision with root package name */
    private d f10519m;

    /* renamed from: n, reason: collision with root package name */
    private Map<String, Boolean> f10520n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class a implements Observer<List<AppToShow>> {
        a() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(List<AppToShow> list) {
            OptimizationAutoStartActivity.this.f10518l.i(list);
            Collections.sort(list, AppNameComparator.f19408g);
            for (AppToShow appToShow : list) {
                Boolean bool = (Boolean) OptimizationAutoStartActivity.this.f10520n.get(appToShow.f20305b);
                if (bool == null) {
                    OptimizationAutoStartActivity.this.f10520n.put(appToShow.f20305b, Boolean.TRUE);
                } else {
                    OptimizationAutoStartActivity.this.f10520n.put(appToShow.f20305b, bool);
                }
            }
            OptimizationAutoStartActivity.this.f10519m.j(list);
            OptimizationAutoStartActivity.this.q();
            OptimizationAutoStartActivity.this.f10518l.h(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class b implements Observer<Map<String, Drawable>> {
        b() {
        }

        @Override // androidx.lifecycle.Observer
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Map<String, Drawable> map) {
            OptimizationAutoStartActivity.this.f10519m.i(map);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            OptimizationAutoStartActivity.this.f10518l.m(OptimizationAutoStartActivity.this.f10520n);
            OptimizationAutoStartActivity.this.finish();
        }
    }

    /* loaded from: classes2.dex */
    public class d extends RecyclerView.h<c> {

        /* renamed from: a, reason: collision with root package name */
        private List<AppToShow> f10524a = new ArrayList();

        /* renamed from: b, reason: collision with root package name */
        private Map<String, Drawable> f10525b = new ArrayMap();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes2.dex */
        public class a implements View.OnClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AppToShow f10527e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ c f10528f;

            a(AppToShow appToShow, c cVar) {
                this.f10527e = appToShow;
                this.f10528f = cVar;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OptimizationAutoStartActivity.this.f10520n.put(this.f10527e.f20305b, Boolean.valueOf(!((Boolean) OptimizationAutoStartActivity.this.f10520n.get(this.f10527e.f20305b)).booleanValue()));
                this.f10528f.f10536d.setChecked(!r3.booleanValue());
                OptimizationAutoStartActivity.this.q();
                d.this.notifyItemChanged(0);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes2.dex */
        public class b implements View.OnClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AppToShow f10530e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ c f10531f;

            b(AppToShow appToShow, c cVar) {
                this.f10530e = appToShow;
                this.f10531f = cVar;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Boolean valueOf = Boolean.valueOf(!((Boolean) OptimizationAutoStartActivity.this.f10520n.get(this.f10530e.f20305b)).booleanValue());
                OptimizationAutoStartActivity.this.f10520n.put(this.f10530e.f20305b, valueOf);
                this.f10531f.f10536d.setChecked(!valueOf.booleanValue());
                Log.d("OptimizationAutoStartActivity", "click package :" + this.f10530e.f20305b + " of Opt view check " + valueOf);
                OptimizationAutoStartActivity.this.q();
                d.this.notifyItemChanged(0);
            }
        }

        /* loaded from: classes2.dex */
        public class c extends RecyclerView.c0 {

            /* renamed from: a, reason: collision with root package name */
            private ImageView f10533a;

            /* renamed from: b, reason: collision with root package name */
            private TextView f10534b;

            /* renamed from: c, reason: collision with root package name */
            private TextView f10535c;

            /* renamed from: d, reason: collision with root package name */
            private CheckBox f10536d;

            /* renamed from: e, reason: collision with root package name */
            private LinearLayout f10537e;

            /* renamed from: f, reason: collision with root package name */
            private TextView f10538f;

            public c(View view, boolean z10) {
                super(view);
                if (z10) {
                    this.f10538f = (TextView) view.findViewById(R.id.title_description);
                    return;
                }
                this.f10537e = (LinearLayout) view.findViewById(R.id.auto_start_detail_layout);
                this.f10533a = (ImageView) view.findViewById(R.id.auto_start_detail_app_icon);
                this.f10534b = (TextView) view.findViewById(R.id.auto_start_detail_title);
                this.f10535c = (TextView) view.findViewById(R.id.auto_start_detail_summary);
                this.f10536d = (CheckBox) view.findViewById(R.id.auto_start_detail_picker);
            }
        }

        public d() {
        }

        private String e(AppToShow appToShow) {
            if (appToShow.f20307d) {
                if (appToShow.f20308e) {
                    return OptimizationAutoStartActivity.this.getString(R.string.startup_applist_summary_both);
                }
                return OptimizationAutoStartActivity.this.getString(R.string.startup_applist_summary_both_forbid);
            }
            if (appToShow.f20308e) {
                return OptimizationAutoStartActivity.this.getString(R.string.startup_applist_summary_background);
            }
            return OptimizationAutoStartActivity.this.getString(R.string.startup_applist_summary_background_forbid);
        }

        private boolean f(int i10) {
            return i10 < 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        @SuppressLint({"NotifyDataSetChanged"})
        public void i(Map<String, Drawable> map) {
            this.f10525b.clear();
            this.f10525b.putAll(map);
            notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: private */
        @SuppressLint({"NotifyDataSetChanged"})
        public void j(List<AppToShow> list) {
            this.f10524a.clear();
            this.f10524a.addAll(list);
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        /* renamed from: g, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(c cVar, int i10) {
            if (i10 == 0) {
                TextView textView = cVar.f10538f;
                OptimizationAutoStartActivity optimizationAutoStartActivity = OptimizationAutoStartActivity.this;
                textView.setText(optimizationAutoStartActivity.getString(R.string.startup_opt_auto_app_list_tile, new Object[]{String.valueOf(optimizationAutoStartActivity.n())}));
                return;
            }
            AppToShow appToShow = this.f10524a.get(i10 - 1);
            Map<String, Drawable> map = this.f10525b;
            if (map != null && map.get(appToShow.f20305b) != null) {
                cVar.f10533a.setImageDrawable(this.f10525b.get(appToShow.f20305b));
            } else {
                cVar.f10533a.setImageDrawable(OptimizationAutoStartActivity.this.f10515i);
            }
            cVar.f10534b.setText(appToShow.f20306c);
            cVar.f10535c.setText(e(appToShow));
            Boolean bool = (Boolean) OptimizationAutoStartActivity.this.f10520n.get(appToShow.f20305b);
            if (bool == null) {
                bool = Boolean.TRUE;
            }
            cVar.f10536d.setChecked(true ^ bool.booleanValue());
            cVar.f10536d.setOnClickListener(new a(appToShow, cVar));
            cVar.f10537e.setOnClickListener(new b(appToShow, cVar));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemCount() {
            List<AppToShow> list = this.f10524a;
            if (list == null) {
                return 0;
            }
            return list.size() + 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemViewType(int i10) {
            return f(i10) ? 0 : 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public c onCreateViewHolder(ViewGroup viewGroup, int i10) {
            if (i10 == 1) {
                return new c(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.optimization_auto_app_detail_item, viewGroup, false), false);
            }
            return new c(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pref_title_layout, viewGroup, false), true);
        }
    }

    private void m() {
        setContentView(R.layout.optimization_auto_start_list_layout);
        x9.c.c(this);
        this.f10513g = (RecyclerView) findViewById(R.id.opt_list_view);
        this.f10512f = (Button) findViewById(R.id.bottom_menu_button);
        this.f10514h = (COUIToolbar) findViewById(R.id.toolbar);
        this.f10516j = (AppBarLayout) findViewById(R.id.abl);
        this.f10517k = (LinearLayout) findViewById(R.id.opt_layout);
        this.f10513g.setLayoutManager(new LinearLayoutManager(this));
        this.f10516j = (AppBarLayout) findViewById(R.id.abl);
    }

    public static int o(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public int n() {
        Iterator<String> it = this.f10520n.keySet().iterator();
        int i10 = 0;
        while (it.hasNext()) {
            Boolean bool = this.f10520n.get(it.next());
            if (bool != null && !bool.booleanValue()) {
                i10++;
            }
        }
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.startupapp.common.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        m();
        p();
    }

    public void p() {
        View findViewById = findViewById(R.id.divider_line);
        setSupportActionBar(this.f10514h);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.s(true);
        }
        this.f10515i = IconUtils.b(this, getDrawable(R.drawable.file_apk_icon));
        this.f10517k.setPadding(0, o(this), 0, 0);
        View b10 = x9.c.b(this);
        this.f10516j.addView(b10, 0, b10.getLayoutParams());
        if (getResources().getBoolean(R.bool.is_immsersive_theme)) {
            findViewById.setVisibility(8);
        }
        this.f10520n = new ArrayMap();
        q();
        OptimizationViewModel optimizationViewModel = (OptimizationViewModel) new h0.a(getApplication()).a(OptimizationViewModel.class);
        this.f10518l = optimizationViewModel;
        optimizationViewModel.k().g(this, new a());
        this.f10518l.j().g(this, new b());
        d dVar = new d();
        this.f10519m = dVar;
        this.f10513g.setAdapter(dVar);
        this.f10512f.setOnClickListener(new c());
    }

    public void q() {
        int n10 = n();
        this.f10512f.setEnabled(n10 > 0);
        this.f10512f.setText(getString(R.string.startup_opt_buttom_description, new Object[]{String.valueOf(n10)}));
    }
}
