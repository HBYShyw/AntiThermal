package com.facebook.rebound.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import n3.OrigamiValueConverter;
import n3.SpringConfig;
import n3.SpringConfigRegistry;
import n3.SpringListener;
import n3.SpringSystem;

/* loaded from: classes.dex */
public class SpringConfiguratorView extends FrameLayout {

    /* renamed from: r, reason: collision with root package name */
    private static final DecimalFormat f8252r = new DecimalFormat("#.#");

    /* renamed from: e, reason: collision with root package name */
    private final e f8253e;

    /* renamed from: f, reason: collision with root package name */
    private final List<SpringConfig> f8254f;

    /* renamed from: g, reason: collision with root package name */
    private final n3.f f8255g;

    /* renamed from: h, reason: collision with root package name */
    private final float f8256h;

    /* renamed from: i, reason: collision with root package name */
    private final float f8257i;

    /* renamed from: j, reason: collision with root package name */
    private final SpringConfigRegistry f8258j;

    /* renamed from: k, reason: collision with root package name */
    private final int f8259k;

    /* renamed from: l, reason: collision with root package name */
    private SeekBar f8260l;

    /* renamed from: m, reason: collision with root package name */
    private SeekBar f8261m;

    /* renamed from: n, reason: collision with root package name */
    private Spinner f8262n;

    /* renamed from: o, reason: collision with root package name */
    private TextView f8263o;

    /* renamed from: p, reason: collision with root package name */
    private TextView f8264p;

    /* renamed from: q, reason: collision with root package name */
    private SpringConfig f8265q;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements View.OnTouchListener {
        private b() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return true;
            }
            SpringConfiguratorView.this.p();
            return true;
        }
    }

    /* loaded from: classes.dex */
    private class c implements SpringListener {
        private c() {
        }

        @Override // n3.SpringListener
        public void onSpringActivate(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringAtRest(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringEndStateChange(n3.f fVar) {
        }

        @Override // n3.SpringListener
        public void onSpringUpdate(n3.f fVar) {
            float c10 = (float) fVar.c();
            float f10 = SpringConfiguratorView.this.f8257i;
            SpringConfiguratorView.this.setTranslationY((c10 * (SpringConfiguratorView.this.f8256h - f10)) + f10);
        }
    }

    /* loaded from: classes.dex */
    private class d implements SeekBar.OnSeekBarChangeListener {
        private d() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i10, boolean z10) {
            if (seekBar == SpringConfiguratorView.this.f8260l) {
                double d10 = ((i10 * 200.0f) / 100000.0f) + 0.0f;
                SpringConfiguratorView.this.f8265q.f15748b = OrigamiValueConverter.d(d10);
                String format = SpringConfiguratorView.f8252r.format(d10);
                SpringConfiguratorView.this.f8264p.setText("T:" + format);
            }
            if (seekBar == SpringConfiguratorView.this.f8261m) {
                double d11 = ((i10 * 50.0f) / 100000.0f) + 0.0f;
                SpringConfiguratorView.this.f8265q.f15747a = OrigamiValueConverter.a(d11);
                String format2 = SpringConfiguratorView.f8252r.format(d11);
                SpringConfiguratorView.this.f8263o.setText("F:" + format2);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class e extends BaseAdapter {

        /* renamed from: e, reason: collision with root package name */
        private final Context f8269e;

        /* renamed from: f, reason: collision with root package name */
        private final List<String> f8270f = new ArrayList();

        public e(Context context) {
            this.f8269e = context;
        }

        public void a(String str) {
            this.f8270f.add(str);
            notifyDataSetChanged();
        }

        public void b() {
            this.f8270f.clear();
            notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.f8270f.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i10) {
            return this.f8270f.get(i10);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            TextView textView;
            if (view == null) {
                textView = new TextView(this.f8269e);
                textView.setLayoutParams(new AbsListView.LayoutParams(-1, -1));
                int d10 = o3.a.d(12.0f, SpringConfiguratorView.this.getResources());
                textView.setPadding(d10, d10, d10, d10);
                textView.setTextColor(SpringConfiguratorView.this.f8259k);
            } else {
                textView = (TextView) view;
            }
            textView.setText(this.f8270f.get(i10));
            return textView;
        }
    }

    /* loaded from: classes.dex */
    private class f implements AdapterView.OnItemSelectedListener {
        private f() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i10, long j10) {
            SpringConfiguratorView springConfiguratorView = SpringConfiguratorView.this;
            springConfiguratorView.f8265q = (SpringConfig) springConfiguratorView.f8254f.get(i10);
            SpringConfiguratorView springConfiguratorView2 = SpringConfiguratorView.this;
            springConfiguratorView2.q(springConfiguratorView2.f8265q);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public SpringConfiguratorView(Context context) {
        this(context, null);
    }

    private View n(Context context) {
        Resources resources = getResources();
        int d10 = o3.a.d(5.0f, resources);
        int d11 = o3.a.d(10.0f, resources);
        int d12 = o3.a.d(20.0f, resources);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(0, -2, 1.0f);
        layoutParams.setMargins(0, 0, d10, 0);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(o3.a.a(-1, o3.a.d(300.0f, resources)));
        FrameLayout frameLayout2 = new FrameLayout(context);
        FrameLayout.LayoutParams b10 = o3.a.b();
        b10.setMargins(0, d12, 0, 0);
        frameLayout2.setLayoutParams(b10);
        frameLayout2.setBackgroundColor(Color.argb(100, 0, 0, 0));
        frameLayout.addView(frameLayout2);
        this.f8262n = new Spinner(context, 0);
        FrameLayout.LayoutParams c10 = o3.a.c();
        c10.gravity = 48;
        c10.setMargins(d11, d11, d11, 0);
        this.f8262n.setLayoutParams(c10);
        frameLayout2.addView(this.f8262n);
        LinearLayout linearLayout = new LinearLayout(context);
        FrameLayout.LayoutParams c11 = o3.a.c();
        c11.setMargins(0, 0, 0, o3.a.d(80.0f, resources));
        c11.gravity = 80;
        linearLayout.setLayoutParams(c11);
        linearLayout.setOrientation(1);
        frameLayout2.addView(linearLayout);
        LinearLayout linearLayout2 = new LinearLayout(context);
        FrameLayout.LayoutParams c12 = o3.a.c();
        c12.setMargins(d11, d11, d11, d12);
        linearLayout2.setPadding(d11, d11, d11, d11);
        linearLayout2.setLayoutParams(c12);
        linearLayout2.setOrientation(0);
        linearLayout.addView(linearLayout2);
        SeekBar seekBar = new SeekBar(context);
        this.f8260l = seekBar;
        seekBar.setLayoutParams(layoutParams);
        linearLayout2.addView(this.f8260l);
        TextView textView = new TextView(getContext());
        this.f8264p = textView;
        textView.setTextColor(this.f8259k);
        FrameLayout.LayoutParams a10 = o3.a.a(o3.a.d(50.0f, resources), -1);
        this.f8264p.setGravity(19);
        this.f8264p.setLayoutParams(a10);
        this.f8264p.setMaxLines(1);
        linearLayout2.addView(this.f8264p);
        LinearLayout linearLayout3 = new LinearLayout(context);
        FrameLayout.LayoutParams c13 = o3.a.c();
        c13.setMargins(d11, d11, d11, d12);
        linearLayout3.setPadding(d11, d11, d11, d11);
        linearLayout3.setLayoutParams(c13);
        linearLayout3.setOrientation(0);
        linearLayout.addView(linearLayout3);
        SeekBar seekBar2 = new SeekBar(context);
        this.f8261m = seekBar2;
        seekBar2.setLayoutParams(layoutParams);
        linearLayout3.addView(this.f8261m);
        TextView textView2 = new TextView(getContext());
        this.f8263o = textView2;
        textView2.setTextColor(this.f8259k);
        FrameLayout.LayoutParams a11 = o3.a.a(o3.a.d(50.0f, resources), -1);
        this.f8263o.setGravity(19);
        this.f8263o.setLayoutParams(a11);
        this.f8263o.setMaxLines(1);
        linearLayout3.addView(this.f8263o);
        View view = new View(context);
        FrameLayout.LayoutParams a12 = o3.a.a(o3.a.d(60.0f, resources), o3.a.d(40.0f, resources));
        a12.gravity = 49;
        view.setLayoutParams(a12);
        view.setOnTouchListener(new b());
        view.setBackgroundColor(Color.argb(255, 0, 164, EventType.SCENE_MODE_VIDEO_CALL));
        frameLayout.addView(view);
        return frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() {
        this.f8255g.o(this.f8255g.e() == 1.0d ? UserProfileInfo.Constant.NA_LAT_LON : 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q(SpringConfig springConfig) {
        int round = Math.round(((((float) OrigamiValueConverter.c(springConfig.f15748b)) - 0.0f) * 100000.0f) / 200.0f);
        int round2 = Math.round(((((float) OrigamiValueConverter.b(springConfig.f15747a)) - 0.0f) * 100000.0f) / 50.0f);
        this.f8260l.setProgress(round);
        this.f8261m.setProgress(round2);
    }

    public void o() {
        Map<SpringConfig, String> b10 = this.f8258j.b();
        this.f8253e.b();
        this.f8254f.clear();
        for (Map.Entry<SpringConfig, String> entry : b10.entrySet()) {
            if (entry.getKey() != SpringConfig.f15746c) {
                this.f8254f.add(entry.getKey());
                this.f8253e.a(entry.getValue());
            }
        }
        this.f8254f.add(SpringConfig.f15746c);
        this.f8253e.a(b10.get(SpringConfig.f15746c));
        this.f8253e.notifyDataSetChanged();
        if (this.f8254f.size() > 0) {
            this.f8262n.setSelection(0);
        }
    }

    public SpringConfiguratorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @TargetApi(11)
    public SpringConfiguratorView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f8254f = new ArrayList();
        this.f8259k = Color.argb(255, 225, 225, 225);
        SpringSystem g6 = SpringSystem.g();
        this.f8258j = SpringConfigRegistry.c();
        e eVar = new e(context);
        this.f8253e = eVar;
        Resources resources = getResources();
        this.f8257i = o3.a.d(40.0f, resources);
        float d10 = o3.a.d(280.0f, resources);
        this.f8256h = d10;
        n3.f c10 = g6.c();
        this.f8255g = c10;
        c10.m(1.0d).o(1.0d).a(new c());
        addView(n(context));
        d dVar = new d();
        this.f8260l.setMax(100000);
        this.f8260l.setOnSeekBarChangeListener(dVar);
        this.f8261m.setMax(100000);
        this.f8261m.setOnSeekBarChangeListener(dVar);
        this.f8262n.setAdapter((SpinnerAdapter) eVar);
        this.f8262n.setOnItemSelectedListener(new f());
        o();
        setTranslationY(d10);
    }
}
