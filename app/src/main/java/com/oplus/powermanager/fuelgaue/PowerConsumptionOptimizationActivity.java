package com.oplus.powermanager.fuelgaue;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.OplusPackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import b6.LocalLog;
import b8.OplusIconUtils;
import c6.NotifyUtil;
import com.android.internal.policy.SystemBarUtils;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.grid.COUIPercentWidthFrameLayout;
import com.coui.appcompat.list.COUIListView;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerConsumptionOptimizationActivity;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.HeadScaleWithSearchBhv;
import com.oplus.powermanager.fuelgaue.base.HeightView;
import com.oplus.powermanager.fuelgaue.base.PaddingTopView;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import com.oplus.powermanager.fuelgaue.base.ThemeBundleUtils;
import com.oplus.powermanager.fuelgaue.base.TopMarginView;
import com.oplus.powermanager.fuelgaue.base.ViewGroupUtil;
import com.oplus.powermanager.powerconsumopt.PowerConsumptionOptimizationInfoView;
import g2.COUIClickSelectMenu;
import g2.PopupListItem;
import g7.AppInfoManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import m1.COUIMoveEaseInterpolator;
import n8.PowerConsumptionOptimizationHelper;
import v4.GuardElfContext;
import w1.COUIDarkModeUtil;
import w2.COUIStatusBarResponseUtil;
import z5.GuardElfDataManager;
import z7.SmoothScrollToTopTask;

@SuppressLint({"IntentDosDetector"})
/* loaded from: classes.dex */
public class PowerConsumptionOptimizationActivity extends BaseAppCompatActivity implements COUIStatusBarResponseUtil.c, View.OnTouchListener, COUISearchViewAnimate.u {
    private ViewGroup A;
    private ViewGroup B;
    private EffectiveAnimationView C;
    private COUIListView D;
    private int F;
    private boolean G;
    private Interpolator I;
    private Interpolator J;
    private int K;
    private r N;
    private TopMarginView O;
    private ValueAnimator P;
    private ValueAnimator Q;
    private ObjectAnimator R;
    private ObjectAnimator S;
    private ObjectAnimator T;
    private ObjectAnimator U;
    private ObjectAnimator V;
    private ObjectAnimator W;
    private ObjectAnimator X;
    private ObjectAnimator Y;
    private ObjectAnimator Z;

    /* renamed from: a0, reason: collision with root package name */
    private ObjectAnimator f10017a0;

    /* renamed from: b0, reason: collision with root package name */
    private AnimatorSet f10018b0;

    /* renamed from: c0, reason: collision with root package name */
    private AnimatorSet f10019c0;

    /* renamed from: d0, reason: collision with root package name */
    private HeightView f10020d0;

    /* renamed from: e0, reason: collision with root package name */
    private HeightView f10022e0;

    /* renamed from: f, reason: collision with root package name */
    private ExecutorService f10023f;

    /* renamed from: f0, reason: collision with root package name */
    private PaddingTopView f10024f0;

    /* renamed from: g, reason: collision with root package name */
    private ExecutorService f10025g;

    /* renamed from: g0, reason: collision with root package name */
    private CoordinatorLayout f10026g0;

    /* renamed from: h, reason: collision with root package name */
    private o f10027h;

    /* renamed from: i, reason: collision with root package name */
    private COUIListView f10029i;

    /* renamed from: j, reason: collision with root package name */
    private ViewGroup f10031j;

    /* renamed from: k, reason: collision with root package name */
    private SmoothScrollToTopTask f10033k;

    /* renamed from: n, reason: collision with root package name */
    private COUIStatusBarResponseUtil f10039n;

    /* renamed from: o, reason: collision with root package name */
    private COUIToolbar f10041o;

    /* renamed from: p, reason: collision with root package name */
    private AppBarLayout f10043p;

    /* renamed from: q, reason: collision with root package name */
    private int f10045q;

    /* renamed from: r, reason: collision with root package name */
    private TextView f10047r;

    /* renamed from: s, reason: collision with root package name */
    private CoordinatorLayout.e f10049s;

    /* renamed from: t, reason: collision with root package name */
    private HeadScaleWithSearchBhv f10051t;

    /* renamed from: u, reason: collision with root package name */
    private COUISearchViewAnimate f10053u;

    /* renamed from: v, reason: collision with root package name */
    private COUIPercentWidthFrameLayout f10055v;

    /* renamed from: v0, reason: collision with root package name */
    private Handler f10056v0;

    /* renamed from: w, reason: collision with root package name */
    private View f10057w;

    /* renamed from: x, reason: collision with root package name */
    private View f10059x;

    /* renamed from: y, reason: collision with root package name */
    private TopMarginView f10061y;

    /* renamed from: z, reason: collision with root package name */
    private View f10062z;

    /* renamed from: e, reason: collision with root package name */
    private final HashMap<String, Drawable> f10021e = new HashMap<>();

    /* renamed from: l, reason: collision with root package name */
    private Context f10035l = null;

    /* renamed from: m, reason: collision with root package name */
    private Activity f10037m = null;
    private int[] E = new int[2];
    private boolean H = false;
    private List<PowerConsumptionOptimizationHelper.c> L = new ArrayList();
    private List<PowerConsumptionOptimizationHelper.c> M = new ArrayList();

    /* renamed from: h0, reason: collision with root package name */
    private List<PowerConsumptionOptimizationHelper.c> f10028h0 = new ArrayList();

    /* renamed from: i0, reason: collision with root package name */
    private List<String> f10030i0 = new ArrayList();

    /* renamed from: j0, reason: collision with root package name */
    private SharedPreferences f10032j0 = null;

    /* renamed from: k0, reason: collision with root package name */
    private SharedPreferences.Editor f10034k0 = null;

    /* renamed from: l0, reason: collision with root package name */
    private boolean f10036l0 = false;

    /* renamed from: m0, reason: collision with root package name */
    private HashMap<String, Integer> f10038m0 = new HashMap<>();

    /* renamed from: n0, reason: collision with root package name */
    private boolean f10040n0 = false;

    /* renamed from: o0, reason: collision with root package name */
    private boolean f10042o0 = false;

    /* renamed from: p0, reason: collision with root package name */
    private boolean f10044p0 = false;

    /* renamed from: q0, reason: collision with root package name */
    private boolean f10046q0 = false;

    /* renamed from: r0, reason: collision with root package name */
    private boolean f10048r0 = false;

    /* renamed from: s0, reason: collision with root package name */
    private boolean f10050s0 = false;

    /* renamed from: t0, reason: collision with root package name */
    private boolean f10052t0 = false;

    /* renamed from: u0, reason: collision with root package name */
    private int f10054u0 = -1;

    /* renamed from: w0, reason: collision with root package name */
    private HandlerThread f10058w0 = new HandlerThread("Thread Handler");

    /* renamed from: x0, reason: collision with root package name */
    @SuppressLint({"HandlerLeak"})
    private Handler f10060x0 = new i(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (PowerConsumptionOptimizationActivity.this.f10048r0) {
                PowerConsumptionOptimizationActivity.this.f10056v0.sendEmptyMessage(101);
                PowerConsumptionOptimizationActivity.this.f10048r0 = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PowerConsumptionOptimizationActivity.this.f10051t.setScaleEnable(true);
            if (PowerConsumptionOptimizationActivity.this.f10048r0) {
                PowerConsumptionOptimizationActivity.this.f10056v0.sendEmptyMessage(101);
                PowerConsumptionOptimizationActivity.this.f10048r0 = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            for (int i10 = 0; i10 < PowerConsumptionOptimizationActivity.this.f10041o.getChildCount(); i10++) {
                PowerConsumptionOptimizationActivity.this.f10041o.getChildAt(i10).setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {
        d() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            for (int i10 = 0; i10 < PowerConsumptionOptimizationActivity.this.f10041o.getChildCount(); i10++) {
                PowerConsumptionOptimizationActivity.this.f10041o.getChildAt(i10).setVisibility(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e extends AnimatorListenerAdapter {
        e() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            PowerConsumptionOptimizationActivity.this.f10057w.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends Handler {
        f(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i10 = message.what;
            if (i10 != 101) {
                if (i10 == 102 && f6.f.B1(PowerConsumptionOptimizationActivity.this.f10035l).keySet().size() > 0) {
                    PowerConsumptionOptimizationActivity powerConsumptionOptimizationActivity = PowerConsumptionOptimizationActivity.this;
                    powerConsumptionOptimizationActivity.f10038m0 = f6.f.B1(powerConsumptionOptimizationActivity.f10035l);
                    return;
                }
                return;
            }
            PowerConsumptionOptimizationActivity.this.l0(message.arg1 == 1);
        }
    }

    /* loaded from: classes.dex */
    class g implements AbsListView.OnScrollListener {
        g() {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i10, int i11, int i12) {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i10) {
            if (i10 == 1 && PowerConsumptionOptimizationActivity.this.f10033k.g()) {
                LocalLog.a("PowerConsumptionOptimizationActivity", "scroll touch it");
                PowerConsumptionOptimizationActivity.this.f10033k.i(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements View.OnTouchListener {
        h() {
        }

        @Override // android.view.View.OnTouchListener
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (PowerConsumptionOptimizationActivity.this.f10033k == null || !PowerConsumptionOptimizationActivity.this.f10033k.g()) {
                return false;
            }
            LocalLog.a("PowerConsumptionOptimizationActivity", "touch it");
            PowerConsumptionOptimizationActivity.this.f10052t0 = true;
            PowerConsumptionOptimizationActivity.this.f10033k.i(true);
            return false;
        }
    }

    /* loaded from: classes.dex */
    class i extends Handler {
        i(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 != 103) {
                if (i10 != 100) {
                    if (i10 == 104) {
                        PowerConsumptionOptimizationActivity.this.f10027h.notifyDataSetChanged();
                        return;
                    }
                    return;
                } else {
                    LocalLog.a("PowerConsumptionOptimizationActivity", "mHandler isInMultiWindowMode =" + PowerConsumptionOptimizationActivity.this.isInMultiWindowMode());
                    return;
                }
            }
            ViewGroupUtil.setVisibilityWithChild(PowerConsumptionOptimizationActivity.this.f10031j, 8);
            if (PowerConsumptionOptimizationActivity.this.A.getVisibility() != 0) {
                PowerConsumptionOptimizationActivity.this.f10029i.setVisibility(0);
            }
            if (!PowerConsumptionOptimizationActivity.this.f10036l0) {
                PowerConsumptionOptimizationActivity.this.f10029i.addHeaderView(PowerConsumptionOptimizationActivity.this.f10062z);
                PowerConsumptionOptimizationActivity.this.f10036l0 = true;
            }
            if (PowerConsumptionOptimizationActivity.this.f10027h == null) {
                PowerConsumptionOptimizationActivity.this.f10027h = new o();
            }
            if (PowerConsumptionOptimizationActivity.this.f10029i.getAdapter() != PowerConsumptionOptimizationActivity.this.f10027h) {
                PowerConsumptionOptimizationActivity.this.f10029i.setAdapter((ListAdapter) PowerConsumptionOptimizationActivity.this.f10027h);
            } else {
                PowerConsumptionOptimizationActivity.this.f10027h.notifyDataSetChanged();
            }
            LocalLog.a("PowerConsumptionOptimizationActivity", "listview set");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j implements ViewTreeObserver.OnGlobalLayoutListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f10072e;

        j(View view) {
            this.f10072e = view;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (f6.f.o1(PowerConsumptionOptimizationActivity.this) && f6.f.g1(PowerConsumptionOptimizationActivity.this)) {
                Rect rect = new Rect();
                PowerConsumptionOptimizationActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int i10 = rect.top;
                if (i10 != 0) {
                    PowerConsumptionOptimizationActivity.this.f10045q = i10;
                    this.f10072e.setLayoutParams(new AppBarLayout.LayoutParams(-1, PowerConsumptionOptimizationActivity.this.f10045q));
                }
            }
            PowerConsumptionOptimizationActivity powerConsumptionOptimizationActivity = PowerConsumptionOptimizationActivity.this;
            powerConsumptionOptimizationActivity.K = powerConsumptionOptimizationActivity.f10043p.getMeasuredHeight();
            LocalLog.a("PowerConsumptionOptimizationActivity", "mContainerTopPadding " + PowerConsumptionOptimizationActivity.this.K + " statusHeight: " + PowerConsumptionOptimizationActivity.this.f10045q);
            PowerConsumptionOptimizationActivity.this.f10059x.setPadding(0, PowerConsumptionOptimizationActivity.this.K, 0, 0);
            PowerConsumptionOptimizationActivity.this.A.setPadding(0, PowerConsumptionOptimizationActivity.this.f10045q + PowerConsumptionOptimizationActivity.this.f10041o.getHeight(), 0, 0);
            PowerConsumptionOptimizationActivity powerConsumptionOptimizationActivity2 = PowerConsumptionOptimizationActivity.this;
            powerConsumptionOptimizationActivity2.F = powerConsumptionOptimizationActivity2.K + PowerConsumptionOptimizationActivity.this.getResources().getDimensionPixelOffset(R.dimen.list_to_ex_top_padding);
            PowerConsumptionOptimizationActivity.this.f10062z.setLayoutParams(new AbsListView.LayoutParams(-1, PowerConsumptionOptimizationActivity.this.F));
            PowerConsumptionOptimizationActivity.this.initAnimators();
            PowerConsumptionOptimizationActivity.this.doInitSearchState();
            PowerConsumptionOptimizationActivity.this.f10053u.setSearchViewAnimateHeightPercent(1.0f);
            PowerConsumptionOptimizationActivity.this.f10043p.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class k implements ValueAnimator.AnimatorUpdateListener {
        k() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (valueAnimator == null || valueAnimator.getAnimatedValue() == null) {
                return;
            }
            PowerConsumptionOptimizationActivity.this.fadeToolbarChild(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class l extends AnimatorListenerAdapter {
        l() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            for (int i10 = 0; i10 < PowerConsumptionOptimizationActivity.this.f10041o.getChildCount(); i10++) {
                PowerConsumptionOptimizationActivity.this.f10041o.getChildAt(i10).setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class m implements ValueAnimator.AnimatorUpdateListener {
        m() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (valueAnimator == null || valueAnimator.getAnimatedValue() == null) {
                return;
            }
            PowerConsumptionOptimizationActivity.this.fadeToolbarChild(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class n extends AnimatorListenerAdapter {
        n() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            for (int i10 = 0; i10 < PowerConsumptionOptimizationActivity.this.f10041o.getChildCount(); i10++) {
                PowerConsumptionOptimizationActivity.this.f10041o.getChildAt(i10).setVisibility(0);
            }
        }
    }

    /* loaded from: classes.dex */
    private class o extends BaseAdapter {

        /* loaded from: classes.dex */
        class a implements AdapterView.OnItemClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ PowerConsumptionOptimizationHelper.c f10079e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ COUIClickSelectMenu f10080f;

            a(PowerConsumptionOptimizationHelper.c cVar, COUIClickSelectMenu cOUIClickSelectMenu) {
                this.f10079e = cVar;
                this.f10080f = cOUIClickSelectMenu;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
                if (PowerConsumptionOptimizationActivity.this.f10052t0) {
                    PowerConsumptionOptimizationActivity.this.f10052t0 = false;
                    return;
                }
                PowerConsumptionOptimizationActivity.this.f10038m0.remove(this.f10079e.f20246a);
                PowerConsumptionOptimizationActivity.this.f10038m0.put(this.f10079e.f20246a, Integer.valueOf(i10));
                AppInfoManager.m(PowerConsumptionOptimizationActivity.this.f10035l).y(this.f10079e.f20246a, i10);
                f6.f.C3(PowerConsumptionOptimizationActivity.this.f10038m0, PowerConsumptionOptimizationActivity.this.f10035l);
                if (PowerConsumptionOptimizationActivity.this.f10027h != null) {
                    PowerConsumptionOptimizationActivity.this.f10027h.notifyDataSetChanged();
                }
                if (PowerConsumptionOptimizationActivity.this.N != null) {
                    PowerConsumptionOptimizationActivity.this.N.notifyDataSetChanged();
                }
                this.f10080f.d();
            }
        }

        public o() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int size;
            synchronized (PowerConsumptionOptimizationActivity.this.f10028h0) {
                size = PowerConsumptionOptimizationActivity.this.f10028h0.size();
            }
            return size;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i10) {
            Object obj;
            synchronized (PowerConsumptionOptimizationActivity.this.f10028h0) {
                obj = PowerConsumptionOptimizationActivity.this.f10028h0.size() > 0 ? PowerConsumptionOptimizationActivity.this.f10028h0.get(i10) : null;
            }
            return obj;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return 0L;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            int i11;
            int i12;
            View powerConsumptionOptimizationInfoView = view == null ? new PowerConsumptionOptimizationInfoView(PowerConsumptionOptimizationActivity.this) : view;
            PowerConsumptionOptimizationInfoView powerConsumptionOptimizationInfoView2 = (PowerConsumptionOptimizationInfoView) powerConsumptionOptimizationInfoView;
            boolean v7 = powerConsumptionOptimizationInfoView2.v();
            PowerConsumptionOptimizationInfoView powerConsumptionOptimizationInfoView3 = powerConsumptionOptimizationInfoView2;
            if (!v7) {
                powerConsumptionOptimizationInfoView = new PowerConsumptionOptimizationInfoView(PowerConsumptionOptimizationActivity.this);
                powerConsumptionOptimizationInfoView3 = powerConsumptionOptimizationInfoView;
            }
            synchronized (PowerConsumptionOptimizationActivity.this.f10028h0) {
                if (PowerConsumptionOptimizationActivity.this.f10028h0.size() > 0) {
                    PowerConsumptionOptimizationHelper.c cVar = (PowerConsumptionOptimizationHelper.c) PowerConsumptionOptimizationActivity.this.f10028h0.get(i10);
                    String str = cVar.f20246a + cVar.f20248c.uid;
                    powerConsumptionOptimizationInfoView3.setTag(cVar);
                    synchronized (PowerConsumptionOptimizationActivity.this.f10021e) {
                        Drawable drawable = (Drawable) PowerConsumptionOptimizationActivity.this.f10021e.get(str);
                        if (drawable != null) {
                            powerConsumptionOptimizationInfoView3.setIcon(drawable);
                        } else {
                            powerConsumptionOptimizationInfoView3.setIcon(null);
                            p pVar = new p(PowerConsumptionOptimizationActivity.this, null);
                            pVar.b(powerConsumptionOptimizationInfoView3);
                            pVar.executeOnExecutor(PowerConsumptionOptimizationActivity.this.f10023f, cVar);
                        }
                    }
                    powerConsumptionOptimizationInfoView3.setTitle(cVar.f20247b);
                    powerConsumptionOptimizationInfoView3.w();
                    if (PowerConsumptionOptimizationActivity.this.f10038m0.containsKey(cVar.f20246a)) {
                        i12 = ((Integer) PowerConsumptionOptimizationActivity.this.f10038m0.get(cVar.f20246a)).intValue();
                    } else {
                        i12 = GuardElfDataManager.d(PowerConsumptionOptimizationActivity.this.f10035l).e("notify_whitelist.xml").contains(cVar.f20246a) ? 1 : PowerConsumptionOptimizationHelper.k(PowerConsumptionOptimizationActivity.this.f10035l).j(PowerConsumptionOptimizationActivity.this.f10035l) ? 2 : 0;
                    }
                    powerConsumptionOptimizationInfoView3.setText(PowerConsumptionOptimizationActivity.this.f10035l.getResources().getStringArray(R.array.pco_bottom_dialog_items)[i12]);
                    if (i10 < PowerConsumptionOptimizationActivity.this.M.size() - 1) {
                        powerConsumptionOptimizationInfoView3.setDividerLineVisibility(0);
                    } else {
                        powerConsumptionOptimizationInfoView3.setDividerLineVisibility(8);
                    }
                }
            }
            PowerConsumptionOptimizationHelper.c cVar2 = (PowerConsumptionOptimizationHelper.c) PowerConsumptionOptimizationActivity.this.f10028h0.get(i10);
            if (PowerConsumptionOptimizationActivity.this.f10038m0.containsKey(cVar2.f20246a)) {
                i11 = ((Integer) PowerConsumptionOptimizationActivity.this.f10038m0.get(cVar2.f20246a)).intValue();
            } else {
                i11 = PowerConsumptionOptimizationHelper.k(PowerConsumptionOptimizationActivity.this.f10035l).j(PowerConsumptionOptimizationActivity.this.f10035l) ? 2 : 0;
                if (GuardElfDataManager.d(PowerConsumptionOptimizationActivity.this.f10035l).e("notify_whitelist.xml").contains(cVar2.f20246a)) {
                    i11 = 1;
                }
            }
            COUIClickSelectMenu cOUIClickSelectMenu = new COUIClickSelectMenu(PowerConsumptionOptimizationActivity.this.f10037m, powerConsumptionOptimizationInfoView);
            ArrayList<PopupListItem> arrayList = new ArrayList<>();
            arrayList.add(new PopupListItem(null, PowerConsumptionOptimizationActivity.this.f10035l.getString(R.string.pco_bottom_dialog_auto_optimization), i11 == 0, i11 == 0, true));
            arrayList.add(new PopupListItem(null, PowerConsumptionOptimizationActivity.this.f10035l.getString(R.string.pco_bottom_dialog_not_optimization), i11 == 1, i11 == 1, true));
            arrayList.add(new PopupListItem(null, PowerConsumptionOptimizationActivity.this.f10035l.getString(R.string.pco_bottom_dialog_ask_every_time), i11 == 2, i11 == 2, true));
            cOUIClickSelectMenu.e(powerConsumptionOptimizationInfoView, arrayList);
            cOUIClickSelectMenu.g(true);
            cOUIClickSelectMenu.h(new a(cVar2, cOUIClickSelectMenu));
            COUICardListHelper.d(powerConsumptionOptimizationInfoView3, COUICardListHelper.a(getCount(), i10));
            return powerConsumptionOptimizationInfoView;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class p extends AsyncTask<PowerConsumptionOptimizationHelper.c, Integer, Drawable> {

        /* renamed from: a, reason: collision with root package name */
        private PowerConsumptionOptimizationInfoView f10082a;

        private p() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Drawable doInBackground(PowerConsumptionOptimizationHelper.c... cVarArr) {
            Drawable applicationIconCache;
            LocalLog.a("PowerConsumptionOptimizationActivity", "doInBackground");
            PowerConsumptionOptimizationHelper.c cVar = cVarArr[0];
            ApplicationInfo applicationInfo = cVar.f20248c;
            OplusPackageManager oplusPackageManager = new OplusPackageManager(PowerConsumptionOptimizationActivity.this.f10035l);
            if (applicationInfo == null) {
                applicationIconCache = PowerConsumptionOptimizationActivity.this.f10035l.getResources().getDrawable(R.drawable.pm_power_usage_system);
            } else {
                applicationIconCache = oplusPackageManager.getApplicationIconCache(applicationInfo);
            }
            if (applicationIconCache == null) {
                applicationIconCache = PowerConsumptionOptimizationActivity.this.f10035l.getResources().getDrawable(R.drawable.pm_power_usage_system);
            }
            if (applicationIconCache != null) {
                applicationIconCache = OplusIconUtils.b(PowerConsumptionOptimizationActivity.this.f10035l, applicationIconCache);
                PowerConsumptionOptimizationInfoView powerConsumptionOptimizationInfoView = this.f10082a;
                if (powerConsumptionOptimizationInfoView != null && (powerConsumptionOptimizationInfoView.getTag() instanceof PowerConsumptionOptimizationHelper.c) && TextUtils.equals(((PowerConsumptionOptimizationHelper.c) this.f10082a.getTag()).f20247b, cVar.f20247b)) {
                    this.f10082a.setIcon(applicationIconCache);
                }
            }
            synchronized (PowerConsumptionOptimizationActivity.this.f10021e) {
                if (applicationInfo != null) {
                    PowerConsumptionOptimizationActivity.this.f10021e.put(cVar.f20246a + applicationInfo.uid, applicationIconCache);
                }
            }
            LocalLog.a("PowerConsumptionOptimizationActivity", "pkg: " + cVar.f20246a + "load icon over");
            return applicationIconCache;
        }

        public void b(PowerConsumptionOptimizationInfoView powerConsumptionOptimizationInfoView) {
            this.f10082a = powerConsumptionOptimizationInfoView;
        }

        /* synthetic */ p(PowerConsumptionOptimizationActivity powerConsumptionOptimizationActivity, f fVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class q implements SearchView.m {
        private q() {
        }

        @Override // androidx.appcompat.widget.SearchView.m
        public boolean a(String str) {
            if (!TextUtils.isEmpty(str) && !PowerConsumptionOptimizationActivity.this.G) {
                PowerConsumptionOptimizationActivity.this.f10034k0.putBoolean("search_init", true);
                PowerConsumptionOptimizationActivity.this.L.clear();
                for (int i10 = 0; i10 < PowerConsumptionOptimizationActivity.this.M.size(); i10++) {
                    if (((PowerConsumptionOptimizationHelper.c) PowerConsumptionOptimizationActivity.this.M.get(i10)).f20247b.toLowerCase().contains(str.toLowerCase())) {
                        PowerConsumptionOptimizationActivity.this.L.add((PowerConsumptionOptimizationHelper.c) PowerConsumptionOptimizationActivity.this.M.get(i10));
                    }
                }
                PowerConsumptionOptimizationActivity.this.f10029i.setVisibility(8);
                PowerConsumptionOptimizationActivity.this.f10057w.setVisibility(8);
                PowerConsumptionOptimizationActivity.this.A.setVisibility(0);
                PowerConsumptionOptimizationActivity.this.f10040n0 = true;
                PowerConsumptionOptimizationActivity.this.f10034k0.putInt("search_status_fix", 2);
                LocalLog.a("PowerConsumptionOptimizationActivity", "mResultContainer set true");
                if (PowerConsumptionOptimizationActivity.this.L.isEmpty()) {
                    PowerConsumptionOptimizationActivity.this.D.setVisibility(8);
                    PowerConsumptionOptimizationActivity.this.B.setVisibility(0);
                    if (!PowerConsumptionOptimizationActivity.this.C.r()) {
                        if (COUIDarkModeUtil.a(PowerConsumptionOptimizationActivity.this.f10035l)) {
                            PowerConsumptionOptimizationActivity.this.C.setAnimation("search-results-empty-dark.json");
                        } else {
                            PowerConsumptionOptimizationActivity.this.C.setAnimation("search-results-empty-light.json");
                        }
                        PowerConsumptionOptimizationActivity.this.C.u();
                    }
                } else {
                    PowerConsumptionOptimizationActivity.this.B.setVisibility(8);
                    PowerConsumptionOptimizationActivity.this.C.j();
                    PowerConsumptionOptimizationActivity.this.D.setVisibility(0);
                    PowerConsumptionOptimizationActivity.this.N.notifyDataSetChanged();
                }
            } else {
                PowerConsumptionOptimizationActivity.this.f10034k0.putBoolean("search_init", false);
                if (PowerConsumptionOptimizationActivity.this.H) {
                    PowerConsumptionOptimizationActivity.this.L.clear();
                    PowerConsumptionOptimizationActivity.this.f10029i.setVisibility(0);
                    PowerConsumptionOptimizationActivity.this.f10057w.setVisibility(0);
                    PowerConsumptionOptimizationActivity.this.A.setVisibility(8);
                    PowerConsumptionOptimizationActivity.this.f10040n0 = false;
                    PowerConsumptionOptimizationActivity.this.f10034k0.putInt("search_status_fix", 1);
                    LocalLog.a("PowerConsumptionOptimizationActivity", "mResultContainer set false");
                }
            }
            PowerConsumptionOptimizationActivity.this.f10034k0.apply();
            return true;
        }

        @Override // androidx.appcompat.widget.SearchView.m
        public boolean b(String str) {
            return false;
        }

        /* synthetic */ q(PowerConsumptionOptimizationActivity powerConsumptionOptimizationActivity, f fVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class r extends BaseAdapter {

        /* renamed from: e, reason: collision with root package name */
        private List<PowerConsumptionOptimizationHelper.c> f10085e;

        /* loaded from: classes.dex */
        class a implements AdapterView.OnItemClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ PowerConsumptionOptimizationHelper.c f10087e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ COUIClickSelectMenu f10088f;

            a(PowerConsumptionOptimizationHelper.c cVar, COUIClickSelectMenu cOUIClickSelectMenu) {
                this.f10087e = cVar;
                this.f10088f = cOUIClickSelectMenu;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
                PowerConsumptionOptimizationActivity.this.g0();
                PowerConsumptionOptimizationActivity.this.f10038m0.remove(this.f10087e.f20246a);
                PowerConsumptionOptimizationActivity.this.f10038m0.put(this.f10087e.f20246a, Integer.valueOf(i10));
                AppInfoManager.m(PowerConsumptionOptimizationActivity.this.f10035l).y(this.f10087e.f20246a, i10);
                f6.f.C3(PowerConsumptionOptimizationActivity.this.f10038m0, PowerConsumptionOptimizationActivity.this.f10035l);
                if (PowerConsumptionOptimizationActivity.this.f10027h != null) {
                    PowerConsumptionOptimizationActivity.this.f10027h.notifyDataSetChanged();
                }
                if (PowerConsumptionOptimizationActivity.this.N != null) {
                    PowerConsumptionOptimizationActivity.this.N.notifyDataSetChanged();
                }
                this.f10088f.d();
            }
        }

        public r(Context context, List<PowerConsumptionOptimizationHelper.c> list) {
            this.f10085e = list;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            List<PowerConsumptionOptimizationHelper.c> list = this.f10085e;
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i10) {
            List<PowerConsumptionOptimizationHelper.c> list = this.f10085e;
            if (list == null || list.size() <= 0) {
                return null;
            }
            return this.f10085e.get(i10);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return 0L;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            int i11;
            View powerConsumptionOptimizationInfoView = view == null ? new PowerConsumptionOptimizationInfoView(PowerConsumptionOptimizationActivity.this) : view;
            PowerConsumptionOptimizationInfoView powerConsumptionOptimizationInfoView2 = (PowerConsumptionOptimizationInfoView) powerConsumptionOptimizationInfoView;
            boolean v7 = powerConsumptionOptimizationInfoView2.v();
            PowerConsumptionOptimizationInfoView powerConsumptionOptimizationInfoView3 = powerConsumptionOptimizationInfoView2;
            if (!v7) {
                powerConsumptionOptimizationInfoView = new PowerConsumptionOptimizationInfoView(PowerConsumptionOptimizationActivity.this);
                powerConsumptionOptimizationInfoView3 = powerConsumptionOptimizationInfoView;
            }
            List<PowerConsumptionOptimizationHelper.c> list = this.f10085e;
            if (list != null && list.size() > 0) {
                PowerConsumptionOptimizationHelper.c cVar = this.f10085e.get(i10);
                Drawable applicationIconCache = new OplusPackageManager(PowerConsumptionOptimizationActivity.this).getApplicationIconCache(cVar.f20248c);
                if (applicationIconCache == null) {
                    applicationIconCache = PowerConsumptionOptimizationActivity.this.getResources().getDrawable(R.drawable.pm_power_usage_system);
                }
                if (applicationIconCache != null) {
                    powerConsumptionOptimizationInfoView3.setIcon(OplusIconUtils.b(PowerConsumptionOptimizationActivity.this, applicationIconCache));
                }
                powerConsumptionOptimizationInfoView3.setTitle(cVar.f20247b);
                if (i10 < this.f10085e.size() - 1) {
                    powerConsumptionOptimizationInfoView3.setDividerLineVisibility(0);
                } else {
                    powerConsumptionOptimizationInfoView3.setDividerLineVisibility(8);
                }
                if (PowerConsumptionOptimizationActivity.this.f10038m0.containsKey(cVar.f20246a)) {
                    i11 = ((Integer) PowerConsumptionOptimizationActivity.this.f10038m0.get(cVar.f20246a)).intValue();
                } else {
                    i11 = PowerConsumptionOptimizationHelper.k(PowerConsumptionOptimizationActivity.this.f10035l).j(PowerConsumptionOptimizationActivity.this.f10035l) ? 2 : 0;
                    if (GuardElfDataManager.d(PowerConsumptionOptimizationActivity.this.f10035l).e("notify_whitelist.xml").contains(cVar.f20246a)) {
                        i11 = 1;
                    }
                }
                powerConsumptionOptimizationInfoView3.setText(PowerConsumptionOptimizationActivity.this.f10035l.getResources().getStringArray(R.array.pco_bottom_dialog_items)[i11]);
                powerConsumptionOptimizationInfoView3.w();
                COUIClickSelectMenu cOUIClickSelectMenu = new COUIClickSelectMenu(PowerConsumptionOptimizationActivity.this.f10037m, powerConsumptionOptimizationInfoView);
                ArrayList<PopupListItem> arrayList = new ArrayList<>();
                arrayList.add(new PopupListItem(null, PowerConsumptionOptimizationActivity.this.f10035l.getString(R.string.pco_bottom_dialog_auto_optimization), i11 == 0, i11 == 0, true));
                arrayList.add(new PopupListItem(null, PowerConsumptionOptimizationActivity.this.f10035l.getString(R.string.pco_bottom_dialog_not_optimization), i11 == 1, i11 == 1, true));
                arrayList.add(new PopupListItem(null, PowerConsumptionOptimizationActivity.this.f10035l.getString(R.string.pco_bottom_dialog_ask_every_time), i11 == 2, i11 == 2, true));
                cOUIClickSelectMenu.e(powerConsumptionOptimizationInfoView, arrayList);
                cOUIClickSelectMenu.g(true);
                cOUIClickSelectMenu.h(new a(cVar, cOUIClickSelectMenu));
            }
            COUICardListHelper.d(powerConsumptionOptimizationInfoView3, COUICardListHelper.a(getCount(), i10));
            return powerConsumptionOptimizationInfoView;
        }
    }

    private void animBack() {
        this.G = true;
        this.f10019c0.start();
        this.f10047r.animate().alpha(1.0f).setDuration(100L).setStartDelay(100L).start();
        if (ThemeBundleUtils.getImmersiveTheme()) {
            this.Q.start();
        } else {
            this.f10041o.animate().alpha(1.0f).setListener(new d()).setDuration(150L).start();
        }
        ViewGroup viewGroup = this.B;
        if (viewGroup != null && viewGroup.getVisibility() == 0) {
            this.B.setVisibility(8);
            this.C.j();
            this.f10057w.setVisibility(8);
            this.f10057w.setAlpha(0.0f);
        } else {
            this.f10057w.animate().alpha(0.0f).setDuration(150L).setListener(new e()).start();
        }
        this.f10034k0.putInt("search_status_fix", 0);
        this.f10034k0.apply();
        LocalLog.a("PowerConsumptionOptimizationActivity", "animBack");
    }

    private void animToSearch() {
        LocalLog.a("PowerConsumptionOptimizationActivity", "animToSearch");
        this.G = false;
        this.f10018b0.start();
        this.f10047r.animate().alpha(0.0f).setDuration(100L).setStartDelay(0L).start();
        if (ThemeBundleUtils.getImmersiveTheme()) {
            this.P.start();
        } else {
            this.f10041o.animate().alpha(0.0f).setListener(new c()).setDuration(150L).start();
        }
        if (this.f10032j0.getInt("search_status_fix", 0) == 0) {
            this.f10034k0.putInt("search_status_fix", 1);
            this.f10034k0.apply();
        }
        if (this.f10032j0.getInt("search_status_fix", 0) == 1) {
            this.f10057w.setVisibility(0);
            this.f10057w.setAlpha(0.0f);
            this.f10057w.animate().alpha(1.0f).setDuration(150L).setListener(null).start();
            LocalLog.a("PowerConsumptionOptimizationActivity", "not click in while search empty");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doInitSearchState() {
        int i10 = this.f10054u0;
        if (i10 == 1) {
            animToSearch();
        } else if (i10 == 0) {
            this.H = false;
            ViewGroup viewGroup = this.A;
            if (viewGroup != null) {
                viewGroup.setVisibility(8);
                this.f10057w.setVisibility(8);
                this.f10029i.setVisibility(0);
                this.f10040n0 = false;
                LocalLog.a("PowerConsumptionOptimizationActivity", "onStateChange mResultContainer set false");
            }
            animBack();
        }
        this.f10054u0 = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeToolbarChild(float f10) {
        for (int i10 = 0; i10 < this.f10041o.getChildCount(); i10++) {
            this.f10041o.getChildAt(i10).setAlpha(f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g0() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 2);
        }
    }

    private void h0() {
        this.f10053u = (COUISearchViewAnimate) findViewById(R.id.searchView);
        this.f10055v = (COUIPercentWidthFrameLayout) findViewById(R.id.searchViewLayout);
        setSupportActionBar(this.f10041o);
        if (this.f10028h0.size() == 0) {
            PowerConsumptionOptimizationHelper.k(this.f10035l).n(false);
            this.f10028h0 = new ArrayList(PowerConsumptionOptimizationHelper.k(this.f10035l).i());
            this.M = new ArrayList(PowerConsumptionOptimizationHelper.k(this.f10035l).i());
        }
        this.N = new r(this, this.L);
        this.f10057w = findViewById(R.id.background_mask_searchView_below_toolbar);
        this.f10059x = findViewById(R.id.container_searchView_below_toolbar);
        TopMarginView topMarginView = new TopMarginView();
        this.f10061y = topMarginView;
        topMarginView.addView(this.f10047r);
        View view = new View(this);
        this.f10062z = view;
        view.setVisibility(4);
        this.A = (ViewGroup) findViewById(R.id.resultContainer);
        this.B = (ViewGroup) findViewById(R.id.emptyContainer);
        this.C = (EffectiveAnimationView) findViewById(R.id.empty_animation);
        COUIListView cOUIListView = (COUIListView) findViewById(R.id.resultList);
        this.D = cOUIListView;
        ViewCompat.y0(cOUIListView, true);
        this.D.setFocusable(true);
        this.D.setTextFilterEnabled(false);
        this.D.setAdapter((ListAdapter) this.N);
        this.f10057w.setOnTouchListener(this);
        this.f10053u.K(this);
        this.f10053u.setIconCanAnimate(false);
        this.f10053u.getSearchView().setOnQueryTextListener(new q(this, null));
        this.f10053u.getFunctionalButton().setOnClickListener(new View.OnClickListener() { // from class: y7.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PowerConsumptionOptimizationActivity.this.j0(view2);
            }
        });
        this.f10053u.setOnClickListener(new View.OnClickListener() { // from class: y7.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PowerConsumptionOptimizationActivity.this.k0(view2);
            }
        });
        this.I = new COUIMoveEaseInterpolator();
        this.J = new COUIMoveEaseInterpolator();
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        View W0 = f6.f.W0(this);
        this.f10043p.addView(W0, 0, W0.getLayoutParams());
        this.f10045q = SystemBarUtils.getStatusBarHeight(this);
        this.f10043p.getViewTreeObserver().addOnGlobalLayoutListener(new j(W0));
        this.f10042o0 = true;
    }

    private void i0() {
        COUIStatusBarResponseUtil cOUIStatusBarResponseUtil = new COUIStatusBarResponseUtil(this);
        this.f10039n = cOUIStatusBarResponseUtil;
        cOUIStatusBarResponseUtil.g(this);
        this.f10026g0 = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        this.f10043p = (AppBarLayout) findViewById(R.id.power_consumption_optimization_appbarlayout);
        COUIToolbar cOUIToolbar = (COUIToolbar) findViewById(R.id.power_consumption_optimization_toolbar);
        this.f10041o = cOUIToolbar;
        cOUIToolbar.setTitle(getString(R.string.power_consumption_optimization_title));
        this.f10047r = (TextView) findViewById(R.id.toolbar_title);
        this.f10041o.setTitleTextColor(getResources().getColor(R.color.coui_color_primary_neutral));
        CoordinatorLayout.e eVar = (CoordinatorLayout.e) this.f10043p.getLayoutParams();
        this.f10049s = eVar;
        this.f10051t = (HeadScaleWithSearchBhv) eVar.f();
        setSupportActionBar(this.f10041o);
        onGetActionBar().s(true);
        this.f10031j = (ViewGroup) findViewById(R.id.loading_container);
        if (this.f10032j0.getInt("search_status_fix", 0) == 0) {
            ViewGroupUtil.setVisibilityWithChild(this.f10031j, 0);
        }
        COUIListView cOUIListView = (COUIListView) findViewById(android.R.id.list);
        this.f10029i = cOUIListView;
        cOUIListView.setFriction(ViewConfiguration.getScrollFriction() * 2.0f);
        ViewCompat.y0(this.f10029i, true);
        this.f10029i.setFocusable(true);
        this.f10029i.setTextFilterEnabled(false);
        this.f10029i.setOnTouchListener(new h());
        h0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initAnimators() {
        TopMarginView topMarginView = new TopMarginView();
        this.O = topMarginView;
        topMarginView.addView(this.f10055v);
        int viewTopMargin = TopMarginView.getViewTopMargin(this.f10055v);
        int i10 = -Math.max(this.f10055v.getHeight(), this.f10041o.getHeight() - this.f10041o.getPaddingTop());
        this.S = ObjectAnimator.ofInt(this.O, "topMargin", viewTopMargin, i10);
        if (ThemeBundleUtils.getImmersiveTheme()) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.P = ofFloat;
            ofFloat.addUpdateListener(new k());
            this.P.addListener(new l());
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.Q = ofFloat2;
            ofFloat2.addUpdateListener(new m());
            this.Q.addListener(new n());
            this.Q.setDuration(150L);
            this.P.setDuration(150L);
        }
        this.f10022e0 = new HeightView(this.f10053u);
        int height = this.f10053u.getHeight();
        int height2 = this.f10041o.getHeight() - this.f10041o.getPaddingTop();
        this.U = ObjectAnimator.ofInt(this.f10022e0, "height", height, height2);
        HeightView heightView = new HeightView(this.f10062z);
        this.f10020d0 = heightView;
        this.R = ObjectAnimator.ofInt(heightView, "height", this.F, this.f10041o.getHeight() + this.f10045q);
        TopMarginView topMarginView2 = new TopMarginView();
        this.f10061y = topMarginView2;
        topMarginView2.addView(this.f10047r);
        this.T = ObjectAnimator.ofInt(this.f10061y, "topMargin", TopMarginView.getViewTopMargin(this.f10047r), -this.f10047r.getHeight());
        PaddingTopView paddingTopView = new PaddingTopView(this.f10059x);
        this.f10024f0 = paddingTopView;
        this.V = ObjectAnimator.ofInt(paddingTopView, "paddingTop", this.f10059x.getPaddingTop(), this.f10041o.getHeight() + this.f10045q);
        AnimatorSet animatorSet = new AnimatorSet();
        this.f10018b0 = animatorSet;
        animatorSet.playTogether(this.S, this.U, this.V, this.R, this.T);
        this.f10018b0.setDuration(450L);
        this.f10018b0.setInterpolator(this.I);
        this.f10018b0.addListener(new a());
        this.X = ObjectAnimator.ofInt(this.O, "topMargin", i10, viewTopMargin);
        this.Z = ObjectAnimator.ofInt(this.f10022e0, "height", height2, height);
        this.W = ObjectAnimator.ofInt(this.f10020d0, "height", this.f10041o.getHeight() + this.f10045q, this.F);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this.f10061y, "topMargin", -this.f10047r.getHeight(), 0);
        this.Y = ofInt;
        ofInt.addListener(new b());
        this.f10017a0 = ObjectAnimator.ofInt(this.f10024f0, "paddingTop", this.f10041o.getHeight() + this.f10045q, this.K);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.f10019c0 = animatorSet2;
        animatorSet2.playTogether(this.X, this.Z, this.f10017a0, this.W, this.Y);
        this.f10019c0.setDuration(450L);
        this.f10019c0.setInterpolator(this.J);
        this.f10050s0 = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j0(View view) {
        this.f10053u.setSearchViewAnimateHeightPercent(1.0f);
        this.f10053u.L(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void k0(View view) {
        this.f10053u.setSearchViewAnimateHeightPercent(1.0f);
        this.f10053u.L(1);
    }

    private List<PowerConsumptionOptimizationHelper.c> m0(List<PowerConsumptionOptimizationHelper.c> list, String str) {
        ArrayList arrayList = new ArrayList();
        LocalLog.a("PowerConsumptionOptimizationActivity", "top now");
        Iterator<PowerConsumptionOptimizationHelper.c> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            PowerConsumptionOptimizationHelper.c next = it.next();
            if (next.f20247b.equals(str)) {
                arrayList.add(next);
                list.remove(next);
                break;
            }
        }
        arrayList.addAll(list);
        return arrayList;
    }

    @Override // w2.COUIStatusBarResponseUtil.c
    public void b() {
        LocalLog.a("PowerConsumptionOptimizationActivity", "onStatusBarClicked");
        if (hasWindowFocus() && this.f10029i.getVisibility() == 0) {
            if (this.f10033k == null) {
                SmoothScrollToTopTask smoothScrollToTopTask = new SmoothScrollToTopTask(this.f10029i);
                this.f10033k = smoothScrollToTopTask;
                smoothScrollToTopTask.h(true);
            }
            this.f10033k.j();
            this.f10029i.setOnScrollListener(new g());
        }
    }

    public void l0(boolean z10) {
        if (this.f10028h0.size() == 0) {
            PowerConsumptionOptimizationHelper.k(this.f10035l).n(false);
        }
        this.f10028h0 = new ArrayList(PowerConsumptionOptimizationHelper.k(this.f10035l).i());
        this.M = new ArrayList(PowerConsumptionOptimizationHelper.k(this.f10035l).i());
        LocalLog.a("PowerConsumptionOptimizationActivity", "refreshStats " + this.f10028h0.size());
        try {
            LocalLog.a("PowerConsumptionOptimizationActivity", "top = " + getIntent().getStringExtra("top"));
            synchronized (this.f10028h0) {
                if (getIntent().getStringExtra("top") != null && !getIntent().getStringExtra("top").isEmpty()) {
                    ArrayList arrayList = new ArrayList(this.f10028h0);
                    this.f10028h0.clear();
                    this.f10028h0.addAll(m0(arrayList, getIntent().getStringExtra("top")));
                }
            }
        } catch (Exception unused) {
            LocalLog.b("PowerConsumptionOptimizationActivity", "getIntent() is error!");
        }
        if (z10) {
            Iterator<PowerConsumptionOptimizationHelper.c> it = this.f10028h0.iterator();
            while (it.hasNext()) {
                new p(this, null).executeOnExecutor(this.f10025g, it.next());
            }
        }
        this.f10060x0.sendEmptyMessage(103);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f10060x0.hasMessages(100)) {
            this.f10060x0.removeMessages(100);
        }
        this.f10060x0.sendMessageDelayed(Message.obtain(this.f10060x0, 100), 500L);
        if (this.B != null) {
            this.B.setPadding(0, this.f10035l.getResources().getDimensionPixelOffset(R.dimen.search_empty_layout_padding_top), 0, 0);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LocalLog.a("PowerConsumptionOptimizationActivity", "onCreate");
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.power_consumption_optimization_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        this.f10035l = GuardElfContext.e().c();
        this.f10037m = this;
        SharedPreferences sharedPreferences = getSharedPreferences("save_search_status", 0);
        this.f10032j0 = sharedPreferences;
        this.f10034k0 = sharedPreferences.edit();
        this.f10038m0.clear();
        this.f10058w0.start();
        f fVar = new f(this.f10058w0.getLooper());
        this.f10056v0 = fVar;
        fVar.sendEmptyMessage(102);
        this.f10023f = Executors.newCachedThreadPool();
        this.f10025g = Executors.newSingleThreadExecutor();
        i0();
        try {
            if (this.f10032j0.getInt("search_status_fix", 0) != 2) {
                Message obtain = Message.obtain();
                obtain.what = 101;
                obtain.arg1 = 1;
                this.f10056v0.sendMessage(obtain);
                this.f10044p0 = true;
            } else {
                if (!getIntent().getBooleanExtra("click_in", false) && !"notify".equals(getIntent().getStringExtra("target"))) {
                    if (this.f10032j0.getInt("search_status_fix", 0) != 0) {
                        this.f10048r0 = true;
                    }
                }
                Message obtain2 = Message.obtain();
                obtain2.what = 101;
                obtain2.arg1 = 1;
                this.f10056v0.sendMessage(obtain2);
                this.f10034k0.putInt("search_status_fix", 0);
                this.f10034k0.putBoolean("search_init", false);
                this.f10034k0.apply();
                this.f10048r0 = false;
                this.f10044p0 = true;
            }
        } catch (Exception unused) {
            LocalLog.b("PowerConsumptionOptimizationActivity", "getIntent() when onCreate is error!");
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.f10058w0.interrupt();
        this.f10023f.shutdownNow();
        this.f10025g.shutdownNow();
        try {
            getIntent().putExtra("click_in", false);
        } catch (Exception unused) {
            LocalLog.b("PowerConsumptionOptimizationActivity", "getIntent() when ondestroy is error!");
        }
        LocalLog.a("PowerConsumptionOptimizationActivity", "onDestroy");
        super.onDestroy();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        LocalLog.a("PowerConsumptionOptimizationActivity", "onOptionsItemSelected  item.getItemId()=" + menuItem.getItemId());
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        LocalLog.a("PowerConsumptionOptimizationActivity", "onOptionsItemSelected  android.R.id.home");
        finish();
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.f10039n.e();
        if (this.f10042o0) {
            this.f10031j.clearAnimation();
            this.f10026g0.removeView(this.f10031j);
            this.f10042o0 = false;
            LocalLog.a("PowerConsumptionOptimizationActivity", "onPause do something");
        }
        this.f10044p0 = false;
        if (this.f10032j0.getInt("search_status_fix", 0) != 0) {
            this.f10048r0 = true;
        }
        LocalLog.a("PowerConsumptionOptimizationActivity", "onPause");
        super.onPause();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        LocalLog.a("PowerConsumptionOptimizationActivity", "onResume");
        super.onResume();
        this.f10039n.f();
        if (!this.f10044p0 && !this.f10048r0) {
            this.f10056v0.sendEmptyMessage(101);
        }
        if (this.f10060x0.hasMessages(100)) {
            this.f10060x0.removeMessages(100);
        }
        this.f10060x0.sendMessageDelayed(Message.obtain(this.f10060x0, 100), 500L);
        COUISearchViewAnimate cOUISearchViewAnimate = this.f10053u;
        if (cOUISearchViewAnimate != null) {
            if (cOUISearchViewAnimate.getSearchView().getSearchAutoComplete().isFocused()) {
                this.f10053u.L(1);
            } else if (this.f10032j0.getBoolean("search_init", false)) {
                this.f10053u.L(1);
                this.f10034k0.putBoolean("search_init", false);
                this.f10034k0.apply();
            }
        }
        try {
            if ("notify".equals(getIntent().getStringExtra("target"))) {
                NotifyUtil.v(this).m(getIntent().getStringExtra("pkgName"));
            }
        } catch (Exception unused) {
            LocalLog.b("PowerConsumptionOptimizationActivity", "getIntent() wrong!");
        }
        if (this.f10040n0) {
            this.f10029i.setVisibility(8);
            this.f10057w.setVisibility(8);
            this.A.setVisibility(0);
            LocalLog.a("PowerConsumptionOptimizationActivity", "resume set true");
        }
    }

    @Override // com.coui.appcompat.searchview.COUISearchViewAnimate.u
    public void onStateChange(int i10, int i11) {
        LocalLog.a("PowerConsumptionOptimizationActivity", "search onStateChange " + i11);
        this.f10051t.setScaleEnable(false);
        this.f10051t.setListFirstChildInitY();
        if (i11 == 1) {
            this.H = true;
            if (this.f10050s0) {
                animToSearch();
                return;
            } else {
                this.f10054u0 = 1;
                return;
            }
        }
        if (i11 == 0) {
            this.f10034k0.putBoolean("search_init", false);
            this.f10034k0.apply();
            if (this.f10050s0) {
                this.H = false;
                ViewGroup viewGroup = this.A;
                if (viewGroup != null) {
                    viewGroup.setVisibility(8);
                    this.f10057w.setVisibility(8);
                    this.f10029i.setVisibility(0);
                    this.f10040n0 = false;
                    LocalLog.a("PowerConsumptionOptimizationActivity", "onStateChange mResultContainer set false");
                }
                animBack();
                return;
            }
            this.f10054u0 = 0;
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ObjectAnimator objectAnimator;
        if (motionEvent.getAction() != 0 || ((objectAnimator = this.S) != null && objectAnimator.isRunning())) {
            return true;
        }
        this.f10053u.L(0);
        return true;
    }
}
