package k8;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import b6.LocalLog;
import b9.BatteryPowerHelper;
import b9.PowerSipper;
import h8.IPowerRankingPresenter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiConsumer;
import l8.IPowerRankingUpdate;
import r8.BatterySipperUtils;
import r8.MultiUserBean;
import t8.PowerUsageManager;

/* compiled from: PowerRankingPresenter.java */
/* renamed from: k8.j, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerRankingPresenter implements IPowerRankingPresenter {

    /* renamed from: c, reason: collision with root package name */
    private Context f14158c;

    /* renamed from: d, reason: collision with root package name */
    private IPowerRankingUpdate f14159d;

    /* renamed from: e, reason: collision with root package name */
    private HandlerThread f14160e;

    /* renamed from: f, reason: collision with root package name */
    private b f14161f;

    /* renamed from: i, reason: collision with root package name */
    private PowerUsageManager.e f14164i;

    /* renamed from: a, reason: collision with root package name */
    private long f14156a = 0;

    /* renamed from: b, reason: collision with root package name */
    private long f14157b = 0;

    /* renamed from: g, reason: collision with root package name */
    private PowerUsageManager f14162g = null;

    /* renamed from: h, reason: collision with root package name */
    private boolean f14163h = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerRankingPresenter.java */
    /* renamed from: k8.j$a */
    /* loaded from: classes2.dex */
    public class a implements PowerUsageManager.e {
        a() {
        }

        @Override // t8.PowerUsageManager.e
        public void d() {
        }

        @Override // t8.PowerUsageManager.e
        public void g(int i10) {
            int[] y4 = PowerRankingPresenter.this.f14162g.y(null, new ArrayList<>());
            int i11 = y4[0];
            int i12 = y4[1];
            LocalLog.a("PowerRankingPresenter", "issue size " + i12);
            PowerRankingPresenter.this.f14159d.c(i12, i11);
        }
    }

    /* compiled from: PowerRankingPresenter.java */
    /* renamed from: k8.j$b */
    /* loaded from: classes2.dex */
    private class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 101) {
                return;
            }
            Bundle data = message.getData();
            PowerRankingPresenter.this.i(data.getLong("startTime"), data.getLong("endTime"), data.getBoolean("isShowScreen"));
        }
    }

    public PowerRankingPresenter(Context context, IPowerRankingUpdate iPowerRankingUpdate) {
        this.f14158c = context;
        this.f14159d = iPowerRankingUpdate;
        HandlerThread handlerThread = new HandlerThread("PowerRankingPresenterWorkThread");
        this.f14160e = handlerThread;
        handlerThread.start();
        this.f14161f = new b(this.f14160e.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void g(ArrayList arrayList, Integer num, MultiUserBean multiUserBean) {
        LocalLog.a("PowerRankingPresenter", "multiUserMap: K:" + num + " power:" + multiUserBean.a() + " time" + multiUserBean.b());
        arrayList.add(new PowerSipper("USER", num.toString(), multiUserBean.b(), multiUserBean.a(), num.intValue()));
    }

    private void h() {
        if (this.f14162g.E() && this.f14164i == null) {
            a aVar = new a();
            this.f14164i = aVar;
            this.f14162g.q(aVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(long j10, long j11, boolean z10) {
        Iterator it;
        this.f14156a = j10 > j11 ? j11 : j10;
        this.f14157b = j11;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        LocalLog.a("PowerRankingPresenter", "mSipperListStartTime:" + this.f14156a + "mSipperListEndTime:" + this.f14157b);
        Context context = this.f14158c;
        long j12 = this.f14156a;
        BatteryPowerHelper.c(context, arrayList, j12 - 60000, j12 + 1200000, false);
        Context context2 = this.f14158c;
        long j13 = this.f14157b;
        BatteryPowerHelper.c(context2, arrayList2, j13 - 60000, j13 + 1200000, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.f14156a);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(this.f14157b);
        long j14 = 0;
        boolean z11 = false;
        while (calendar.getTimeInMillis() < calendar2.getTimeInMillis()) {
            if (calendar.get(11) == 18 && calendar.get(12) == 0) {
                j14 = calendar.getTimeInMillis();
                z11 = true;
            }
            calendar.add(12, 30);
        }
        LocalLog.a("PowerRankingPresenter", "needAdd:" + z11);
        LocalLog.a("PowerRankingPresenter", "time:" + j14);
        if (z11) {
            long d10 = BatteryPowerHelper.d(false, j14);
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            BatteryPowerHelper.c(this.f14158c, arrayList5, d10 - 300000, d10 + 300000, false);
            BatteryPowerHelper.f(arrayList5, arrayList, arrayList4);
            LocalLog.a("PowerRankingPresenter", "listBeforeReset size:" + arrayList4.size());
            LocalLog.a("PowerRankingPresenter", "listReset size:" + arrayList5.size());
            LocalLog.a("PowerRankingPresenter", "listEnd size:" + arrayList2.size());
            BatteryPowerHelper.e(arrayList4, arrayList2, arrayList3);
        } else {
            BatteryPowerHelper.f(arrayList2, arrayList, arrayList3);
        }
        HashMap hashMap = new HashMap();
        Iterator it2 = arrayList3.iterator();
        while (it2.hasNext()) {
            PowerSipper powerSipper = (PowerSipper) it2.next();
            int userId = UserHandle.getUserId(powerSipper.f4616t);
            if (userId == 0 || userId == 999) {
                it = it2;
            } else {
                it2.remove();
                MultiUserBean multiUserBean = (MultiUserBean) hashMap.get(Integer.valueOf(userId));
                if (multiUserBean == null) {
                    multiUserBean = new MultiUserBean(userId, powerSipper.f4613q, powerSipper.f4608l);
                    it = it2;
                } else {
                    multiUserBean.c(multiUserBean.a() + powerSipper.f4613q);
                    it = it2;
                    multiUserBean.d(Math.min(multiUserBean.b() + powerSipper.f4608l, this.f14157b - this.f14156a));
                }
                hashMap.put(Integer.valueOf(userId), multiUserBean);
            }
            it2 = it;
        }
        hashMap.forEach(new BiConsumer() { // from class: k8.i
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PowerRankingPresenter.g(arrayList3, (Integer) obj, (MultiUserBean) obj2);
            }
        });
        LocalLog.a("PowerRankingPresenter", "listResult size:" + arrayList3.size());
        if (z10) {
            arrayList3.sort(PowerSipper.f4600z);
            this.f14159d.Y(BatterySipperUtils.c(this.f14158c, arrayList3, true), true);
            ArrayList arrayList6 = new ArrayList();
            Iterator it3 = arrayList3.iterator();
            while (it3.hasNext()) {
                arrayList6.add(new PowerSipper((PowerSipper) it3.next()));
            }
            arrayList6.sort(PowerSipper.f4599y);
            this.f14159d.Y(BatterySipperUtils.c(this.f14158c, arrayList6, false), false);
            return;
        }
        arrayList3.sort(PowerSipper.f4599y);
        this.f14159d.Y(BatterySipperUtils.c(this.f14158c, arrayList3, false), false);
        ArrayList arrayList7 = new ArrayList();
        Iterator it4 = arrayList3.iterator();
        while (it4.hasNext()) {
            arrayList7.add(new PowerSipper((PowerSipper) it4.next()));
        }
        arrayList7.sort(PowerSipper.f4600z);
        this.f14159d.Y(BatterySipperUtils.c(this.f14158c, arrayList7, true), true);
    }

    @Override // h8.IPowerRankingPresenter
    public void a(long j10, long j11, boolean z10) {
        this.f14161f.removeMessages(101);
        Message obtain = Message.obtain();
        obtain.what = 101;
        Bundle bundle = new Bundle();
        bundle.putLong("startTime", j10);
        bundle.putLong("endTime", j11);
        bundle.putBoolean("isShowScreen", z10);
        obtain.setData(bundle);
        this.f14161f.sendMessage(obtain);
    }

    @Override // h8.IPowerRankingPresenter
    public void b() {
        this.f14162g = PowerUsageManager.x(this.f14158c);
        h();
    }

    @Override // h8.IPowerRankingPresenter
    public void onDestroy() {
        PowerUsageManager.e eVar;
        BatterySipperUtils.a();
        this.f14160e.quitSafely();
        PowerUsageManager powerUsageManager = this.f14162g;
        if (powerUsageManager == null || (eVar = this.f14164i) == null) {
            return;
        }
        powerUsageManager.D(eVar);
    }
}
