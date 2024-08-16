package k8;

import a7.PowerConsumeStats;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import b6.LocalLog;
import com.oplus.battery.R;
import h8.IPowerInspectPresenter;
import i8.BasePagePresenter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import l8.IPowerInspectUpdate;
import t8.PowerUsageManager;
import u8.BasicPowerIssue;
import v4.GuardElfContext;
import x5.UploadDataUtil;

/* compiled from: PowerInspectPresenter.java */
/* renamed from: k8.h, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerInspectPresenter extends BasePagePresenter implements IPowerInspectPresenter {

    /* renamed from: k, reason: collision with root package name */
    private final String f14145k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f14146l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f14147m;

    /* renamed from: n, reason: collision with root package name */
    private IPowerInspectUpdate f14148n;

    /* renamed from: o, reason: collision with root package name */
    private Handler f14149o;

    /* renamed from: p, reason: collision with root package name */
    private Runnable f14150p;

    /* renamed from: q, reason: collision with root package name */
    private PowerUsageManager f14151q;

    /* renamed from: r, reason: collision with root package name */
    private Context f14152r;

    /* compiled from: PowerInspectPresenter.java */
    /* renamed from: k8.h$b */
    /* loaded from: classes2.dex */
    private class b extends AsyncTask<Void, Void, Void> {

        /* renamed from: a, reason: collision with root package name */
        ArrayList<BasicPowerIssue> f14153a;

        private b() {
            this.f14153a = new ArrayList<>();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            ArrayMap<Integer, BasicPowerIssue> v7 = PowerUsageManager.x(PowerInspectPresenter.this.f14152r).v();
            HashMap hashMap = new HashMap();
            for (int i10 = 0; i10 < v7.size(); i10++) {
                BasicPowerIssue valueAt = v7.valueAt(i10);
                if (valueAt.g() == 2) {
                    hashMap.put(valueAt.e(), "removed");
                } else if (valueAt.g() == 1) {
                    this.f14153a.add(valueAt);
                }
            }
            PowerInspectPresenter.this.f14148n.J(this.f14153a.size());
            Iterator<BasicPowerIssue> it = this.f14153a.iterator();
            while (it.hasNext()) {
                it.next().c();
            }
            UploadDataUtil.S0(PowerInspectPresenter.this.f14152r).h0(hashMap);
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(Void r12) {
            if (PowerInspectPresenter.this.f14148n != null) {
                PowerInspectPresenter.this.f14148n.D(this.f14153a);
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            PowerInspectPresenter.this.f14148n.K();
        }
    }

    public PowerInspectPresenter(IPowerInspectUpdate iPowerInspectUpdate) {
        super(PowerInspectPresenter.class.getSimpleName());
        this.f14145k = "removed";
        this.f14146l = false;
        this.f14147m = false;
        this.f14149o = null;
        this.f14150p = null;
        this.f14151q = null;
        this.f14152r = null;
        this.f14148n = iPowerInspectUpdate;
        this.f14152r = GuardElfContext.e().c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l() {
        if (this.f14146l) {
            m();
        }
    }

    private void m() {
        LocalLog.a(this.f12677j, "updateActivityAfterCheck");
        ArrayList<BasicPowerIssue> arrayList = new ArrayList<>();
        ArrayList<BasicPowerIssue> arrayList2 = new ArrayList<>();
        PowerUsageManager.x(this.f14152r).y(arrayList, arrayList2);
        this.f14148n.y(arrayList, arrayList2);
    }

    @Override // h8.IPowerInspectPresenter
    public ArrayList<PackageInfo> Q(ArrayList<PowerConsumeStats.b> arrayList) {
        ArrayList<PackageInfo> arrayList2 = new ArrayList<>();
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<PowerConsumeStats.b> it = arrayList.iterator();
            while (it.hasNext()) {
                PowerConsumeStats.b next = it.next();
                if (next != null && !TextUtils.isEmpty(next.f76f)) {
                    try {
                        PackageInfo packageInfo = this.f12676i.getPackageInfo(next.f76f, 0);
                        if (packageInfo != null) {
                            arrayList2.add(packageInfo);
                        }
                    } catch (PackageManager.NameNotFoundException unused) {
                        LocalLog.b(this.f12677j, "getAbnormalPowerItemsPkgInfos NameNotFoundException: " + next.f76f);
                    }
                }
            }
        }
        return arrayList2;
    }

    @Override // h8.IPowerInspectPresenter
    public void V() {
        PowerUsageManager.x(null).F(false);
    }

    @Override // t8.PowerUsageManager.e
    public void d() {
    }

    @Override // com.oplus.powermanager.powerusage.view.PowerCheckboxPreference.b
    public void f(BasicPowerIssue basicPowerIssue) {
        boolean z10 = basicPowerIssue.g() == 1;
        basicPowerIssue.p(!z10);
        long f10 = basicPowerIssue.f();
        if (z10) {
            f10 = -f10;
        }
        String str = basicPowerIssue.e() + "_switch";
        LocalLog.a(this.f12677j, "click " + str + "difftime=" + f10);
        this.f14148n.J(0);
        this.f14148n.i(f10, str, z10);
    }

    @Override // t8.PowerUsageManager.e
    public void g(int i10) {
        this.f14146l = true;
        Handler handler = this.f14149o;
        if ((handler == null || !handler.hasCallbacks(this.f14150p)) && !this.f14147m) {
            m();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.power_use_operation_menu) {
            return;
        }
        new b().execute(new Void[0]);
    }

    @Override // h8.IPowerInspectPresenter
    public void onCreate(Bundle bundle) {
        this.f14147m = false;
        this.f14149o = new Handler();
        Runnable runnable = new Runnable() { // from class: k8.g
            @Override // java.lang.Runnable
            public final void run() {
                PowerInspectPresenter.this.l();
            }
        };
        this.f14150p = runnable;
        this.f14149o.postDelayed(runnable, 500L);
        PowerUsageManager x10 = PowerUsageManager.x(null);
        this.f14151q = x10;
        x10.q(this);
        this.f14151q.E();
    }

    @Override // h8.IPowerInspectPresenter
    public void onDestroy() {
        this.f14147m = true;
        this.f14149o.removeCallbacksAndMessages(null);
        this.f14151q.D(this);
        this.f14148n = null;
    }
}
