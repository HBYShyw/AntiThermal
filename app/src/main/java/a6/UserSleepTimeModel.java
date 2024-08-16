package a6;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepCluster;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.app.aidl.proton.deepsleep.PredictResultType;
import com.oplus.statistics.util.TimeInfoUtil;
import ea.DeepThinkerProxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/* compiled from: UserSleepTimeModel.java */
/* renamed from: a6.a, reason: use source file name */
/* loaded from: classes.dex */
public class UserSleepTimeModel {

    /* renamed from: i, reason: collision with root package name */
    private static final String f55i = "a";

    /* renamed from: j, reason: collision with root package name */
    private static Context f56j;

    /* renamed from: a, reason: collision with root package name */
    private OplusDeepThinkerManager f57a;

    /* renamed from: b, reason: collision with root package name */
    private long f58b;

    /* renamed from: c, reason: collision with root package name */
    private long f59c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f60d;

    /* renamed from: e, reason: collision with root package name */
    private DeepSleepPredictResult f61e;

    /* renamed from: f, reason: collision with root package name */
    private HandlerThread f62f;

    /* renamed from: g, reason: collision with root package name */
    private Handler f63g;

    /* renamed from: h, reason: collision with root package name */
    private ContentObserver f64h;

    /* compiled from: UserSleepTimeModel.java */
    /* renamed from: a6.a$a */
    /* loaded from: classes.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (UserSleepTimeModel.this.f57a != null) {
                UserSleepTimeModel userSleepTimeModel = UserSleepTimeModel.this;
                userSleepTimeModel.f61e = userSleepTimeModel.f57a.getDeepSleepPredictResult();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: UserSleepTimeModel.java */
    /* renamed from: a6.a$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            UserSleepTimeModel.this.f57a = DeepThinkerProxy.j(UserSleepTimeModel.f56j).i();
            if (UserSleepTimeModel.this.f57a != null) {
                UserSleepTimeModel userSleepTimeModel = UserSleepTimeModel.this;
                userSleepTimeModel.f61e = userSleepTimeModel.f57a.getDeepSleepPredictResult();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UserSleepTimeModel.java */
    /* renamed from: a6.a$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        private static final UserSleepTimeModel f67a = new UserSleepTimeModel(UserSleepTimeModel.f56j, null);
    }

    /* synthetic */ UserSleepTimeModel(Context context, a aVar) {
        this(context);
    }

    private long e(double d10, long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        calendar.set(13, 0);
        calendar.set(14, 0);
        int i10 = (int) (60.0d * d10);
        int i11 = i10 / 60;
        int i12 = i10 % 60;
        calendar.set(11, i11);
        calendar.set(12, i12);
        String str = f55i;
        LocalLog.a(str, "convertDoubleTime: time=" + d10 + ", hour=" + i11 + ", minute=" + i12 + ", c.getTime()=" + calendar.getTime());
        if (i11 >= 24.0d) {
            LocalLog.a(str, "convertDoubleTime: teime error. time=" + d10 + ", hour=" + i11 + ", minute=" + i12);
            return -1L;
        }
        return calendar.getTimeInMillis();
    }

    private boolean f() {
        List<Pair<Double, Double>> h10 = h();
        if (h10 != null && !h10.isEmpty()) {
            long currentTimeMillis = System.currentTimeMillis();
            List<Pair<Long, Long>> m10 = m(h10, currentTimeMillis);
            if (m10 != null && !m10.isEmpty()) {
                return p(m10, currentTimeMillis);
            }
            LocalLog.a(f55i, "forecastSleepDurationAI: listSleepDuration is empty.");
            return false;
        }
        LocalLog.a(f55i, "forecastSleepDurationAI: no listCluster.");
        return false;
    }

    private void g(boolean z10) {
        long currentTimeMillis = System.currentTimeMillis();
        int nextInt = z10 ? new Random().nextInt(59) : 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(11, 0);
        calendar.set(12, nextInt);
        long timeInMillis = calendar.getTimeInMillis();
        calendar.set(11, 6);
        calendar.set(12, nextInt);
        long timeInMillis2 = calendar.getTimeInMillis();
        if (currentTimeMillis >= timeInMillis && currentTimeMillis >= timeInMillis2 - 3600000) {
            this.f58b = timeInMillis + TimeInfoUtil.MILLISECOND_OF_A_DAY;
            this.f59c = timeInMillis2 + TimeInfoUtil.MILLISECOND_OF_A_DAY;
        } else {
            this.f58b = timeInMillis;
            this.f59c = timeInMillis2;
        }
        this.f60d = false;
        calendar.setTimeInMillis(this.f58b);
        if (LocalLog.g()) {
            LocalLog.a(f55i, "forecastSleepDuration: dft. start: " + calendar.getTime());
        }
        calendar.setTimeInMillis(this.f59c);
        if (LocalLog.g()) {
            LocalLog.a(f55i, "forecastSleepDuration: dft. end: " + calendar.getTime());
        }
    }

    private List<Pair<Double, Double>> h() {
        DeepSleepPredictResult deepSleepPredictResult;
        ArrayList arrayList = new ArrayList();
        try {
            if (LocalLog.g()) {
                LocalLog.a(f55i, "getAISleepDuration: predictResult is " + this.f61e);
            }
            deepSleepPredictResult = this.f61e;
        } catch (NumberFormatException unused) {
            LocalLog.a(f55i, "getAISleepDuration: NumberFormatException.");
        } catch (Throwable unused2) {
            LocalLog.a(f55i, "getAISleepDuration: e.");
        }
        if (deepSleepPredictResult == null) {
            LocalLog.b(f55i, "getAISleepDuration: predictResult is null.");
            return null;
        }
        if (PredictResultType.PREDICT_RESULT_TYPE_OK != deepSleepPredictResult.getResultType()) {
            if (LocalLog.g()) {
                LocalLog.a(f55i, "getAISleepDuration: predictResult=" + this.f61e.getResultType());
            }
            return null;
        }
        List<DeepSleepCluster> deepSleepClusterList = this.f61e.getDeepSleepClusterList();
        if (deepSleepClusterList != null && !deepSleepClusterList.isEmpty()) {
            LocalLog.a(f55i, "getAISleepDuration: size=" + deepSleepClusterList.size());
            StringBuilder sb2 = new StringBuilder();
            for (int i10 = 0; i10 < deepSleepClusterList.size(); i10++) {
                DeepSleepCluster deepSleepCluster = deepSleepClusterList.get(i10);
                double sleepTimePeriod = deepSleepCluster.getSleepTimePeriod();
                double wakeTimePeriod = deepSleepCluster.getWakeTimePeriod();
                arrayList.add(new Pair(Double.valueOf(sleepTimePeriod), Double.valueOf(wakeTimePeriod)));
                sb2.append(sleepTimePeriod);
                sb2.append(" : ");
                sb2.append(wakeTimePeriod);
                sb2.append(". ");
                LocalLog.a(f55i, "getAISleepDuration: startTimeDouble=" + sleepTimePeriod + ", endTimeDouble=" + wakeTimePeriod + ", i=" + i10);
            }
            LocalLog.a(f55i, "getAISleepDuration: strSleepDuration=" + ((Object) sb2));
            return arrayList;
        }
        LocalLog.a(f55i, "getAISleepDuration: no listCluster.");
        return null;
    }

    private void i() {
        new Thread(new b()).start();
    }

    public static UserSleepTimeModel j(Context context) {
        f56j = context.getApplicationContext();
        return c.f67a;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0050, code lost:
    
        if (r21 >= (r12 - 3600000)) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0052, code lost:
    
        r10 = r10 + com.oplus.statistics.util.TimeInfoUtil.MILLISECOND_OF_A_DAY;
        r12 = r12 + com.oplus.statistics.util.TimeInfoUtil.MILLISECOND_OF_A_DAY;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0061, code lost:
    
        if (r21 >= (r12 - 3600000)) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List<Pair<Long, Long>> m(List<Pair<Double, Double>> list, long j10) {
        ArrayList arrayList = new ArrayList();
        for (int i10 = 0; i10 < list.size(); i10++) {
            double doubleValue = ((Double) list.get(i10).first).doubleValue();
            double doubleValue2 = ((Double) list.get(i10).second).doubleValue();
            long e10 = e(doubleValue, j10);
            long e11 = e(doubleValue2, j10);
            if (e10 < 0 || e11 < 0) {
                LocalLog.a(f55i, "parseAISleepDuration: time error. startTimeDouble=" + doubleValue + ", endTimeDouble=" + doubleValue2);
            } else if (doubleValue >= doubleValue2) {
                if (doubleValue > doubleValue2) {
                    e10 -= TimeInfoUtil.MILLISECOND_OF_A_DAY;
                } else {
                    e11 = j10 + TimeInfoUtil.MILLISECOND_OF_A_DAY;
                    e10 = j10;
                }
                String str = f55i;
                LocalLog.a(str, "parseAISleepDuration: startTimeInMillis=" + n(e10));
                LocalLog.a(str, "parseAISleepDuration: endTimeInMillis=" + n(e11));
                long j11 = e11 - e10;
                if (j11 < 3600000) {
                    LocalLog.a(str, "parseAISleepDuration: too short " + n(j11));
                } else {
                    arrayList.add(new Pair(Long.valueOf(e10), Long.valueOf(e11)));
                }
            }
        }
        return arrayList;
    }

    private String n(long j10) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        return calendar.getTime().toString();
    }

    private boolean p(List<Pair<Long, Long>> list, long j10) {
        Pair<Long, Long> pair = null;
        long j11 = Long.MAX_VALUE;
        long j12 = 0;
        Pair<Long, Long> pair2 = null;
        for (int i10 = 0; i10 < list.size(); i10++) {
            long longValue = ((Long) list.get(i10).first).longValue();
            long longValue2 = ((Long) list.get(i10).second).longValue();
            if (j10 < longValue || j10 >= longValue2) {
                long j13 = longValue - j10;
                if (j13 < j11) {
                    pair2 = list.get(i10);
                    j11 = j13;
                }
            } else {
                long j14 = longValue2 - j10;
                if (j14 > j12) {
                    pair = list.get(i10);
                    j12 = j14;
                }
            }
        }
        if (pair == null && pair2 == null) {
            LocalLog.a(f55i, "selectAISleepDuration: no suitable duration.");
            for (int i11 = 0; i11 < list.size(); i11++) {
                LocalLog.a(f55i, "selectAISleepDuration: startTimeInMillis=" + n(((Long) list.get(i11).first).longValue()) + ", endTimeInMillis=" + n(((Long) list.get(i11).second).longValue()));
            }
            return false;
        }
        this.f60d = true;
        if (pair != null) {
            this.f58b = ((Long) pair.first).longValue();
            this.f59c = ((Long) pair.second).longValue();
        } else if (pair2 != null) {
            this.f58b = ((Long) pair2.first).longValue();
            this.f59c = ((Long) pair2.second).longValue();
        }
        String str = f55i;
        LocalLog.a(str, "selectAISleepDuration: start: " + n(this.f58b));
        LocalLog.a(str, "selectAISleepDuration: end: " + n(this.f59c));
        return true;
    }

    public long k() {
        return this.f59c;
    }

    public long l() {
        return this.f58b;
    }

    public void o(boolean z10) {
        if (f()) {
            return;
        }
        g(z10);
    }

    private UserSleepTimeModel(Context context) {
        this.f58b = -1L;
        this.f59c = -1L;
        this.f60d = false;
        f56j = context;
        HandlerThread handlerThread = new HandlerThread("UserSleepTime");
        this.f62f = handlerThread;
        handlerThread.start();
        this.f63g = new Handler(this.f62f.getLooper());
        i();
        this.f64h = new a(this.f63g);
        try {
            f56j.getContentResolver().registerContentObserver(Uri.parse("content://com.oplus.proton.algorithm/deepSleepTable"), false, this.f64h);
        } catch (Exception unused) {
            LocalLog.a(f55i, "registerContentObserver: e.");
        }
    }
}
