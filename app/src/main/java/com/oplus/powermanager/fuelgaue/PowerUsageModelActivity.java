package com.oplus.powermanager.fuelgaue;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.OplusWhiteListManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.OplusPackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.COUIFastScroller;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;
import b8.OplusIconUtils;
import c8.WordQuery;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.grid.COUIPercentWidthRecyclerView;
import com.coui.appcompat.searchview.COUISearchViewAnimate;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerUsageModelActivity;
import com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity;
import com.oplus.powermanager.fuelgaue.base.RecyclerHeadScaleWithSearchBhv;
import com.oplus.powermanager.fuelgaue.base.ViewGroupUtil;
import com.oplus.powermanager.fuelgaue.basic.customized.PowerProtectInfoView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import o2.SpringOverScroller;
import w2.COUIStatusBarResponseUtil;
import w5.OplusBatteryConstants;
import x5.UploadDataUtil;
import x8.DatabaseHelper;
import y5.AppFeature;
import z7.AppInfoWrapper;
import z7.AppWrapperComparator;
import z7.RecyclerSmoothScrollToTopTask;

/* loaded from: classes.dex */
public class PowerUsageModelActivity extends BaseSearchViewActivity implements COUIStatusBarResponseUtil.c {

    /* renamed from: g, reason: collision with root package name */
    private RecyclerHeadScaleWithSearchBhv f10144g;

    /* renamed from: h, reason: collision with root package name */
    private h f10145h;

    /* renamed from: i, reason: collision with root package name */
    private COUIPercentWidthRecyclerView f10146i;

    /* renamed from: j, reason: collision with root package name */
    private ViewGroup f10147j;

    /* renamed from: k, reason: collision with root package name */
    private RecyclerSmoothScrollToTopTask f10148k;

    /* renamed from: l, reason: collision with root package name */
    private COUIFastScroller f10149l;

    /* renamed from: m, reason: collision with root package name */
    private l f10150m;

    /* renamed from: p, reason: collision with root package name */
    private int f10153p;

    /* renamed from: s, reason: collision with root package name */
    private String f10156s;

    /* renamed from: e, reason: collision with root package name */
    private List<i> f10142e = Collections.synchronizedList(new ArrayList());

    /* renamed from: f, reason: collision with root package name */
    private List<i> f10143f = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    private int[] f10151n = new int[2];

    /* renamed from: o, reason: collision with root package name */
    private String f10152o = null;

    /* renamed from: q, reason: collision with root package name */
    private boolean f10154q = false;

    /* renamed from: r, reason: collision with root package name */
    private boolean f10155r = false;

    /* renamed from: t, reason: collision with root package name */
    private BroadcastReceiver f10157t = new g();

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            PowerUsageModelActivity.this.f10144g.setScaleEnable(false);
        }
    }

    /* loaded from: classes.dex */
    class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            PowerUsageModelActivity.this.f10144g.setScaleEnable(true);
            LocalLog.a("PowerUsageModelActivity", "onAnimationEnd: setScaleEnable true");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements View.OnTouchListener {
        c() {
        }

        @Override // android.view.View.OnTouchListener
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (PowerUsageModelActivity.this.f10146i.getAdapter() == null) {
                return true;
            }
            if (PowerUsageModelActivity.this.f10148k != null && PowerUsageModelActivity.this.f10148k.a()) {
                LocalLog.a("PowerUsageModelActivity", "touch it");
                PowerUsageModelActivity.this.f10148k.c(true);
            }
            if (!PowerUsageModelActivity.this.f10149l.U() || motionEvent.getAction() != 1) {
                return false;
            }
            PowerUsageModelActivity.this.K();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends RecyclerView.t {
        d() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.t
        public void onScrollStateChanged(RecyclerView recyclerView, int i10) {
            super.onScrollStateChanged(recyclerView, i10);
            PowerUsageModelActivity powerUsageModelActivity = PowerUsageModelActivity.this;
            powerUsageModelActivity.f10153p = Math.max(((BaseSearchViewActivity) powerUsageModelActivity).mSearchView.getMeasuredHeight(), PowerUsageModelActivity.this.f10153p);
            if (i10 == 0) {
                PowerUsageModelActivity.this.K();
            } else {
                PowerUsageModelActivity.this.f10144g.setSpringReset();
                if (!PowerUsageModelActivity.this.f10154q) {
                    PowerUsageModelActivity.this.f10149l.j0(PowerUsageModelActivity.this.getHeaderHeight());
                    PowerUsageModelActivity.this.f10154q = true;
                }
            }
            if (!PowerUsageModelActivity.this.f10149l.M()) {
                PowerUsageModelActivity.this.f10149l.g0(false);
            }
            LocalLog.a("PowerUsageModelActivity", "action =" + i10 + " Measuredheight=" + ((BaseSearchViewActivity) PowerUsageModelActivity.this).mSearchView.getMeasuredHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e implements COUISearchViewAnimate.u {
        e() {
        }

        @Override // com.coui.appcompat.searchview.COUISearchViewAnimate.u
        public void onStateChange(int i10, int i11) {
            LocalLog.a("PowerUsageModelActivity", "search onStateChange " + i11);
            PowerUsageModelActivity.this.f10144g.setScaleEnable(false);
            PowerUsageModelActivity.this.f10146i.scrollToPosition(0);
            try {
                Field declaredField = COUIRecyclerView.class.getDeclaredField("w");
                declaredField.setAccessible(true);
                ((SpringOverScroller) declaredField.get(PowerUsageModelActivity.this.f10146i)).springBack(PowerUsageModelActivity.this.f10146i.getScrollX(), PowerUsageModelActivity.this.f10146i.getScrollY(), 0, 0, 0, 0);
            } catch (IllegalAccessException | NoSuchFieldException e10) {
                LocalLog.b("PowerUsageModelActivity", "performSpringBackToResetOverScrolling,  e=" + e10);
            }
            PowerUsageModelActivity.this.f10144g.setListFirstChildInitY();
            PowerUsageModelActivity.this.f10149l.g0(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f implements SearchView.m {
        f() {
        }

        @Override // androidx.appcompat.widget.SearchView.m
        public boolean a(String str) {
            PowerUsageModelActivity.this.f10152o = str;
            PowerUsageModelActivity.this.f10143f.clear();
            for (int i10 = 0; i10 < PowerUsageModelActivity.this.f10142e.size(); i10++) {
                if (((i) PowerUsageModelActivity.this.f10142e.get(i10)).f20247b.toLowerCase().contains(str.toLowerCase())) {
                    PowerUsageModelActivity.this.f10143f.add((i) PowerUsageModelActivity.this.f10142e.get(i10));
                }
            }
            PowerUsageModelActivity powerUsageModelActivity = PowerUsageModelActivity.this;
            powerUsageModelActivity.handleQueryText(str, powerUsageModelActivity.f10143f);
            return true;
        }

        @Override // androidx.appcompat.widget.SearchView.m
        public boolean b(String str) {
            return false;
        }
    }

    /* loaded from: classes.dex */
    class g extends BroadcastReceiver {
        g() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
                LocalLog.a("PowerUsageModelActivity", "mReceiver:  pkgName= " + schemeSpecificPart);
                if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    LocalLog.a("PowerUsageModelActivity", "mReceiver: replacing ignore pkgName= " + schemeSpecificPart);
                    return;
                }
                if (TextUtils.isEmpty(schemeSpecificPart) || PowerUsageModelActivity.this.f10142e == null || PowerUsageModelActivity.this.f10142e.size() <= 0) {
                    return;
                }
                for (int i10 = 0; i10 < PowerUsageModelActivity.this.f10142e.size(); i10++) {
                    if (schemeSpecificPart.equals(((i) PowerUsageModelActivity.this.f10142e.get(i10)).f20246a) && PowerUsageModelActivity.this.f10145h != null) {
                        PowerUsageModelActivity.this.f10142e.remove(i10);
                        PowerUsageModelActivity.this.f10145h.notifyDataSetChanged();
                        LocalLog.a("PowerUsageModelActivity", "onReceive: notifyDataSetChanged");
                        LocalLog.a("PowerUsageModelActivity", "mReceiver: remove package, pkgName = " + schemeSpecificPart);
                        return;
                    }
                }
                return;
            }
            if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                PowerUsageModelActivity.this.f10150m.c(new k(Boolean.FALSE));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class h extends RecyclerView.h {

        /* renamed from: a, reason: collision with root package name */
        private int f10165a;

        /* renamed from: b, reason: collision with root package name */
        private List<i> f10166b;

        /* renamed from: c, reason: collision with root package name */
        private View f10167c;

        /* loaded from: classes.dex */
        private class a extends RecyclerView.c0 {

            /* renamed from: a, reason: collision with root package name */
            int f10169a;

            /* renamed from: b, reason: collision with root package name */
            PowerProtectInfoView f10170b;

            /* renamed from: com.oplus.powermanager.fuelgaue.PowerUsageModelActivity$h$a$a, reason: collision with other inner class name */
            /* loaded from: classes.dex */
            class ViewOnClickListenerC0024a implements View.OnClickListener {

                /* renamed from: e, reason: collision with root package name */
                final /* synthetic */ h f10172e;

                ViewOnClickListenerC0024a(h hVar) {
                    this.f10172e = hVar;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    i iVar = (i) h.this.f10166b.get(a.this.f10169a);
                    String str = iVar.f20247b;
                    String str2 = iVar.f20246a;
                    Intent intent = new Intent(PowerUsageModelActivity.this, (Class<?>) PowerControlActivity.class);
                    intent.putExtra("title", str);
                    intent.putExtra("pkgName", str2);
                    intent.putExtra("callingSource", -1);
                    intent.putExtra("drainType", "APP");
                    intent.setFlags(603979776);
                    PowerUsageModelActivity.this.startActivity(intent);
                    UploadDataUtil.S0(PowerUsageModelActivity.this).d(str2);
                }
            }

            public a(View view) {
                super(view);
                if (view == h.this.f10167c) {
                    return;
                }
                this.f10170b = (PowerProtectInfoView) view;
                view.setOnClickListener(new ViewOnClickListenerC0024a(h.this));
            }

            public void a(int i10) {
                this.f10169a = i10;
            }
        }

        public h(List<i> list, View view) {
            this.f10165a = 0;
            this.f10166b = list;
            if (view != null) {
                this.f10167c = view;
                this.f10165a = 0;
            } else {
                this.f10165a = 2;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemCount() {
            if (this.f10165a == 2) {
                List<i> list = this.f10166b;
                if (list == null) {
                    return 0;
                }
                return list.size();
            }
            List<i> list2 = this.f10166b;
            if (list2 == null) {
                return 1;
            }
            return 1 + list2.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemViewType(int i10) {
            return (this.f10167c == null || i10 != 0) ? 2 : 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public void onBindViewHolder(RecyclerView.c0 c0Var, int i10) {
            List<i> list = this.f10166b;
            if (list != null && list.size() != 0) {
                if (getItemViewType(i10) == 2 && (c0Var instanceof a)) {
                    PowerProtectInfoView powerProtectInfoView = ((a) c0Var).f10170b;
                    if (i10 < (this.f10165a == 0 ? this.f10166b.size() + 1 : this.f10166b.size()) - 1) {
                        powerProtectInfoView.setDividerLineVisibility(0);
                    } else {
                        powerProtectInfoView.setDividerLineVisibility(8);
                    }
                    if (this.f10167c != null) {
                        i10--;
                    } else if (this.f10165a == 0) {
                        LocalLog.a("PowerUsageModelActivity", "mHeaderView == null");
                        return;
                    } else if (i10 < 0 || i10 >= this.f10166b.size()) {
                        LocalLog.b("PowerUsageModelActivity", "onBindViewHolder: position not in range of mListst");
                        return;
                    }
                    i iVar = this.f10166b.get(i10);
                    Drawable drawable = iVar.f10174f;
                    if (drawable != null) {
                        powerProtectInfoView.setIcon(drawable);
                    } else {
                        PowerUsageModelActivity.this.f10150m.c(new j(iVar, powerProtectInfoView));
                    }
                    COUICardListHelper.d(c0Var.itemView, COUICardListHelper.a(this.f10166b.size(), i10));
                    powerProtectInfoView.setTitle(this.f10166b.get(i10).f20247b);
                    ((a) c0Var).a(i10);
                    return;
                }
                return;
            }
            LocalLog.b("PowerUsageModelActivity", "onBindViewHolder: mList is null or empty");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public RecyclerView.c0 onCreateViewHolder(ViewGroup viewGroup, int i10) {
            if (this.f10167c != null && i10 == 0) {
                return new a(this.f10167c);
            }
            return new a(new PowerProtectInfoView(viewGroup.getContext()));
        }
    }

    /* loaded from: classes.dex */
    public static class i extends AppInfoWrapper {

        /* renamed from: f, reason: collision with root package name */
        public Drawable f10174f;
    }

    /* loaded from: classes.dex */
    private class j implements l.a<Drawable> {

        /* renamed from: a, reason: collision with root package name */
        private i f10175a;

        /* renamed from: b, reason: collision with root package name */
        private PowerProtectInfoView f10176b;

        public j(i iVar, PowerProtectInfoView powerProtectInfoView) {
            this.f10175a = iVar;
            this.f10176b = powerProtectInfoView;
        }

        @Override // com.oplus.powermanager.fuelgaue.PowerUsageModelActivity.l.a
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public Drawable b() {
            Drawable applicationIconCache = new OplusPackageManager(PowerUsageModelActivity.this).getApplicationIconCache(this.f10175a.f20248c);
            if (applicationIconCache == null) {
                applicationIconCache = PowerUsageModelActivity.this.getResources().getDrawable(R.drawable.pm_power_usage_system);
            }
            return applicationIconCache != null ? OplusIconUtils.b(PowerUsageModelActivity.this, applicationIconCache) : applicationIconCache;
        }

        @Override // com.oplus.powermanager.fuelgaue.PowerUsageModelActivity.l.a
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void a(Drawable drawable) {
            PowerProtectInfoView powerProtectInfoView = this.f10176b;
            if (powerProtectInfoView == null || !powerProtectInfoView.getTitle().equals(this.f10175a.f20247b)) {
                return;
            }
            this.f10176b.setIcon(drawable);
            this.f10175a.f10174f = drawable;
        }
    }

    /* loaded from: classes.dex */
    private class k implements l.a<Void> {

        /* renamed from: a, reason: collision with root package name */
        Boolean f10178a;

        /* renamed from: b, reason: collision with root package name */
        List<i> f10179b = new ArrayList();

        public k(Boolean bool) {
            this.f10178a = Boolean.TRUE;
            this.f10178a = bool;
        }

        @Override // com.oplus.powermanager.fuelgaue.PowerUsageModelActivity.l.a
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public Void b() {
            this.f10179b = PowerUsageModelActivity.this.I();
            return null;
        }

        @Override // com.oplus.powermanager.fuelgaue.PowerUsageModelActivity.l.a
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void a(Void r52) {
            if (PowerUsageModelActivity.this.f10142e == null) {
                PowerUsageModelActivity.this.f10142e = new ArrayList();
            } else {
                PowerUsageModelActivity.this.f10142e.clear();
            }
            PowerUsageModelActivity.this.f10142e.addAll(this.f10179b);
            if (this.f10178a.booleanValue() && PowerUsageModelActivity.this.f10152o != null) {
                PowerUsageModelActivity.this.f10143f.clear();
                for (int i10 = 0; i10 < PowerUsageModelActivity.this.f10142e.size(); i10++) {
                    if (((i) PowerUsageModelActivity.this.f10142e.get(i10)).f20247b.toLowerCase().contains(PowerUsageModelActivity.this.f10152o.toLowerCase())) {
                        PowerUsageModelActivity.this.f10143f.add((i) PowerUsageModelActivity.this.f10142e.get(i10));
                    }
                }
                PowerUsageModelActivity powerUsageModelActivity = PowerUsageModelActivity.this;
                powerUsageModelActivity.handleQueryText(powerUsageModelActivity.f10152o, PowerUsageModelActivity.this.f10143f);
                PowerUsageModelActivity.this.f10152o = null;
            }
            if (PowerUsageModelActivity.this.f10145h == null) {
                PowerUsageModelActivity powerUsageModelActivity2 = PowerUsageModelActivity.this;
                powerUsageModelActivity2.f10145h = new h(powerUsageModelActivity2.f10142e, PowerUsageModelActivity.this.getHeaderView());
            }
            if (PowerUsageModelActivity.this.getHeaderInitHeight() != 0) {
                PowerUsageModelActivity.this.f10146i.setAdapter(PowerUsageModelActivity.this.f10145h);
                if (this.f10178a.booleanValue()) {
                    PowerUsageModelActivity.this.f10146i.scrollToPosition(0);
                }
                ViewGroupUtil.setVisibilityWithChild(PowerUsageModelActivity.this.f10147j, 8);
                PowerUsageModelActivity.this.handleContentVisibleIfNeeded();
                PowerUsageModelActivity.this.f10155r = false;
            } else {
                PowerUsageModelActivity.this.f10155r = true;
            }
            LocalLog.a("PowerUsageModelActivity", "onPostExecute: mRefreshAtNotLayout=" + PowerUsageModelActivity.this.f10155r);
        }
    }

    private String F() {
        return this.mContext.getResources().getConfiguration().getLocales().get(0) != null ? this.mContext.getResources().getConfiguration().getLocales().get(0).getLanguage() : "";
    }

    private void G() {
        addSearchStateChangeListener(new e());
        setSearchQueryListener(new f());
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void H() {
        this.f10147j = (ViewGroup) findViewById(R.id.loading_container);
        if (!isInSearchStatus()) {
            ViewGroupUtil.setVisibilityWithChild(this.f10147j, 0);
        }
        COUIPercentWidthRecyclerView cOUIPercentWidthRecyclerView = (COUIPercentWidthRecyclerView) findViewById(android.R.id.list);
        this.f10146i = cOUIPercentWidthRecyclerView;
        cOUIPercentWidthRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.f10146i.setOnTouchListener(new c());
        this.f10146i.addOnScrollListener(new d());
        COUIFastScroller cOUIFastScroller = new COUIFastScroller(null, this);
        this.f10149l = cOUIFastScroller;
        cOUIFastScroller.h0(false);
        this.f10149l.g0(false);
        this.f10146i.setNestedScrollingEnabled(true);
        setStatusBarClickListener(this);
        G();
    }

    private void J(List<i> list) {
        if (list == null) {
            return;
        }
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(list, new AppWrapperComparator(this, new ArrayList(), new ArrayList()));
        } catch (IllegalArgumentException e10) {
            LocalLog.l("PowerUsageModelActivity", "Collections.sort IllegalArgumentException: " + e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void K() {
        this.f10153p = Math.max(this.mSearchView.getMeasuredHeight(), this.f10153p);
        Rect rect = new Rect();
        this.mSearchView.getGlobalVisibleRect(rect);
        if (rect.height() > 5) {
            this.f10144g.setSpringValue(rect.height());
            this.f10144g.setSpringEndValue(this.f10153p);
        }
        LocalLog.a("PowerUsageModelActivity", "updateSpringSystem: rect.height()=" + rect.height() + " rect.width()=" + rect.width() + " initheight=" + this.f10153p);
    }

    public List<i> I() {
        LocalLog.a("PowerUsageModelActivity", "refreshStats");
        ArrayList arrayList = new ArrayList();
        List<String> a10 = AppFeature.a();
        ArrayList globalWhiteList = new OplusWhiteListManager(this).getGlobalWhiteList();
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(128);
        a8.a e10 = a8.a.e();
        ArrayMap<String, String> c10 = DatabaseHelper.u(this.mContext).c();
        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & 1) != 0) {
                LocalLog.a("PowerUsageModelActivity", "refreshStats: app flags system! " + applicationInfo.packageName);
            } else if (!applicationInfo.enabled && !f6.f.m1(applicationInfo, this)) {
                LocalLog.a("PowerUsageModelActivity", "refreshStats: disabled pkg=" + applicationInfo.packageName);
            } else if (a10 != null && a10.contains(applicationInfo.packageName)) {
                LocalLog.a("PowerUsageModelActivity", "refreshStats: listCustmize pkg=" + applicationInfo.packageName);
            } else if (globalWhiteList.contains(applicationInfo.packageName)) {
                LocalLog.a("PowerUsageModelActivity", "refreshStats: OplusWhiteList pkg=" + applicationInfo.packageName);
            } else if (OplusBatteryConstants.f19362n.contains(applicationInfo.packageName)) {
                LocalLog.a("PowerUsageModelActivity", "refreshStats: POWER_CONTROL_SYS_DEFAULT_LIST pkg=" + applicationInfo.packageName);
            } else {
                String str = c10.get(applicationInfo.packageName);
                if (str == null) {
                    CharSequence loadLabel = applicationInfo.loadLabel(getPackageManager());
                    if (loadLabel != null) {
                        str = f6.f.i(loadLabel.toString().trim());
                    }
                    if (TextUtils.isEmpty(str)) {
                        str = applicationInfo.packageName;
                    }
                }
                i iVar = new i();
                iVar.f20247b = str;
                iVar.f20246a = applicationInfo.packageName;
                iVar.f20248c = applicationInfo;
                int b10 = e10.b(str);
                iVar.f20250e = b10;
                String c11 = e10.c(b10);
                if (AppFeature.D() && Locale.getDefault().equals(Locale.TAIWAN) && TextUtils.equals(c11, "…")) {
                    c11 = WordQuery.c(str);
                }
                char[] charArray = c11.toCharArray();
                if (charArray != null && charArray.length > 0) {
                    iVar.f20249d = charArray[0];
                } else {
                    iVar.f20249d = '#';
                }
                arrayList.add(iVar);
            }
        }
        J(arrayList);
        return arrayList;
    }

    @Override // w2.COUIStatusBarResponseUtil.c
    public void b() {
        if (hasWindowFocus() && this.f10146i.getVisibility() == 0) {
            RecyclerSmoothScrollToTopTask recyclerSmoothScrollToTopTask = this.f10148k;
            if (recyclerSmoothScrollToTopTask == null) {
                this.f10148k = new RecyclerSmoothScrollToTopTask(this.f10146i);
            } else {
                recyclerSmoothScrollToTopTask.e();
            }
            this.f10148k.d();
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity
    protected void handleGlobalLayout() {
        COUIPercentWidthRecyclerView cOUIPercentWidthRecyclerView;
        if (!this.f10155r || (cOUIPercentWidthRecyclerView = this.f10146i) == null) {
            return;
        }
        cOUIPercentWidthRecyclerView.setAdapter(this.f10145h);
        this.f10146i.scrollToPosition(0);
        ViewGroupUtil.setVisibilityWithChild(this.f10147j, 8);
        handleContentVisibleIfNeeded();
        this.f10155r = false;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f10149l.g0(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity, com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LocalLog.a("PowerUsageModelActivity", "onCreate");
        super.onCreate(bundle);
        setContentView(R.layout.power_usage_mode_layout);
        this.f10150m = new l(null);
        this.f10150m.c(new k(Boolean.TRUE));
        initBaseView();
        this.f10144g = (RecyclerHeadScaleWithSearchBhv) getBaseBehavior();
        setAnimEnterListener(new a());
        setAnimBackListener(new b());
        initBaseSearchView(new h(this.f10143f, null));
        H();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        registerReceiver(this.f10157t, intentFilter, 2);
        this.f10156s = F();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.f10150m.f();
        unregisterReceiver(this.f10157t);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseSearchViewActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        LocalLog.a("PowerUsageModelActivity", "onResume");
        super.onResume();
        String F = F();
        if (this.f10156s.equals(F)) {
            return;
        }
        this.f10150m = new l(null);
        this.f10150m.c(new k(Boolean.FALSE));
        this.f10156s = F;
        LocalLog.a("PowerUsageModelActivity", "locale changed, refresh data");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class l {

        /* renamed from: a, reason: collision with root package name */
        private final ExecutorService f10181a;

        /* renamed from: b, reason: collision with root package name */
        private final Handler f10182b;

        /* loaded from: classes.dex */
        public interface a<Result> {
            void a(Result result);

            Result b();
        }

        private l() {
            this.f10181a = Executors.newFixedThreadPool(5);
            this.f10182b = new Handler(Looper.getMainLooper());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(final a aVar) {
            try {
                final Object b10 = aVar.b();
                this.f10182b.post(new Runnable() { // from class: com.oplus.powermanager.fuelgaue.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        PowerUsageModelActivity.l.a.this.a(b10);
                    }
                });
            } catch (Exception e10) {
                e10.printStackTrace();
            }
        }

        public <Result> void c(final a<Result> aVar) {
            this.f10181a.execute(new Runnable() { // from class: com.oplus.powermanager.fuelgaue.b
                @Override // java.lang.Runnable
                public final void run() {
                    PowerUsageModelActivity.l.this.e(aVar);
                }
            });
        }

        public void f() {
            this.f10181a.shutdown();
        }

        /* synthetic */ l(a aVar) {
            this();
        }
    }
}
