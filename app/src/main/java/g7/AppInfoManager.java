package g7;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.ClientConnection;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.oguard.data.database.OGuardDataBase;
import com.oplus.statistics.record.StatIdManager;
import f6.CommonUtil;
import f6.f;
import h7.OGuardConst;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import v4.CustmizeAllowBgRunable;
import w4.Affair;
import w4.IAffairCallback;
import y5.AppFeature;
import z5.GuardElfDataManager;

/* compiled from: AppInfoManager.java */
/* renamed from: g7.a, reason: use source file name */
/* loaded from: classes.dex */
public class AppInfoManager implements IAffairCallback {

    /* renamed from: n, reason: collision with root package name */
    private static volatile AppInfoManager f11577n;

    /* renamed from: e, reason: collision with root package name */
    private Context f11578e;

    /* renamed from: f, reason: collision with root package name */
    private Handler f11579f;

    /* renamed from: g, reason: collision with root package name */
    private OplusDeepThinkerManager f11580g;

    /* renamed from: h, reason: collision with root package name */
    private ClientConnection f11581h;

    /* renamed from: i, reason: collision with root package name */
    private List<String> f11582i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private List<String> f11583j = new ArrayList();

    /* renamed from: k, reason: collision with root package name */
    private List<String> f11584k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    private List<String> f11585l = new ArrayList();

    /* renamed from: m, reason: collision with root package name */
    private Map<String, Integer> f11586m = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppInfoManager.java */
    /* renamed from: g7.a$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AppInfoManager.this.f11578e.getContentResolver().notifyChange(OGuardConst.f12014b, null);
        }
    }

    /* compiled from: AppInfoManager.java */
    /* renamed from: g7.a$b */
    /* loaded from: classes.dex */
    class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f11588e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f11589f;

        b(String str, int i10) {
            this.f11588e = str;
            this.f11589f = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            AppInfoManager.this.v(this.f11588e, this.f11589f);
        }
    }

    /* compiled from: AppInfoManager.java */
    /* renamed from: g7.a$c */
    /* loaded from: classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AppInfoManager.this.q();
            Iterator it = AppInfoManager.this.f11583j.iterator();
            while (it.hasNext()) {
                AppInfoManager.this.C((String) it.next(), 1);
            }
            AppInfoManager.this.f11578e.getContentResolver().notifyChange(OGuardConst.f12014b, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AppInfoManager.java */
    /* renamed from: g7.a$d */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f11592e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f11593f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ boolean f11594g;

        d(String str, int i10, boolean z10) {
            this.f11592e = str;
            this.f11593f = i10;
            this.f11594g = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            AppInfoManager.this.t(this.f11592e, this.f11593f);
            if (this.f11594g) {
                if (this.f11593f == 2) {
                    if (AppInfoManager.this.f11585l.contains(this.f11592e)) {
                        return;
                    }
                    AppInfoManager.this.f11585l.add(this.f11592e);
                    return;
                }
                AppInfoManager.this.f11585l.remove(this.f11592e);
            }
        }
    }

    /* compiled from: AppInfoManager.java */
    /* renamed from: g7.a$e */
    /* loaded from: classes.dex */
    class e extends Handler {
        public e(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    AppInfoManager.this.o();
                    return;
                case 2:
                    AppInfoManager.this.s(message);
                    return;
                case 3:
                    AppInfoManager.this.z();
                    return;
                case 4:
                    AppInfoManager.this.q();
                    return;
                case 5:
                    AppInfoManager.this.p();
                    return;
                case 6:
                    AppInfoManager.this.A(message);
                    return;
                default:
                    return;
            }
        }
    }

    private AppInfoManager(Context context) {
        this.f11578e = context;
        HandlerThread handlerThread = new HandlerThread("app_info_manager");
        handlerThread.start();
        this.f11579f = new e(handlerThread.getLooper());
        n();
    }

    private void B(String str, int i10) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("background_value", Integer.valueOf(i10));
        this.f11578e.getContentResolver().update(OGuardConst.f12014b, contentValues, "pkg_name==?", new String[]{str});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C(String str, int i10) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("optimize_value", Integer.valueOf(i10));
        this.f11578e.getContentResolver().update(OGuardConst.f12014b, contentValues, "pkg_name==?", new String[]{str});
    }

    private Map<String, Integer> l(List<String> list) {
        return this.f11580g.getAppTypeMap(list);
    }

    public static AppInfoManager m(Context context) {
        if (f11577n == null) {
            synchronized (AppInfoManager.class) {
                if (f11577n == null) {
                    f11577n = new AppInfoManager(context);
                }
            }
        }
        return f11577n;
    }

    private void n() {
        if (this.f11580g == null) {
            this.f11580g = new OplusDeepThinkerManager(this.f11578e);
            ClientConnection clientConnection = new ClientConnection(this.f11578e, Executors.newFixedThreadPool(3), this.f11579f);
            this.f11581h = clientConnection;
            this.f11580g.setRemote(clientConnection.getDeepThinkerBridge());
        }
        this.f11579f.sendEmptyMessage(4);
        this.f11579f.sendEmptyMessageDelayed(5, 1800000L);
        this.f11579f.sendEmptyMessageDelayed(3, 1800000L);
        registerAction();
        r();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        int i10;
        int i11;
        if (!f.l1(this.f11578e) || OGuardDataBase.v(this.f11578e).u().b() <= 10) {
            if (CommonUtil.T(this.f11578e)) {
                LocalLog.b("AppInfoManager", "initAllAppInfo skip boot reg");
                this.f11579f.sendEmptyMessageDelayed(1, 60000L);
                return;
            }
            if (this.f11579f.hasMessages(1)) {
                this.f11579f.removeMessages(1);
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            f.l(arrayList, arrayList2, this.f11578e);
            List<ApplicationInfo> installedApplications = this.f11578e.getPackageManager().getInstalledApplications(128);
            ContentValues[] contentValuesArr = new ContentValues[installedApplications.size()];
            for (int i12 = 0; i12 < installedApplications.size(); i12++) {
                ApplicationInfo applicationInfo = installedApplications.get(i12);
                ContentValues contentValues = new ContentValues();
                contentValues.put(TriggerEvent.EXTRA_UID, Integer.valueOf(applicationInfo.uid));
                contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, applicationInfo.packageName);
                contentValues.put("app_type", this.f11586m.getOrDefault(applicationInfo.packageName, 0));
                if (this.f11584k.contains(applicationInfo.packageName) || (applicationInfo.flags & 1) != 0) {
                    i10 = -1;
                } else {
                    i10 = this.f11583j.contains(applicationInfo.packageName) ? 1 : 0;
                }
                contentValues.put("optimize_value", Integer.valueOf(i10));
                if (arrayList.contains(applicationInfo.packageName) || this.f11585l.contains(applicationInfo.packageName)) {
                    i11 = 2;
                } else {
                    i11 = arrayList2.contains(applicationInfo.packageName) ? 1 : 0;
                }
                contentValues.put("background_value", Integer.valueOf(i11));
                contentValuesArr[i12] = contentValues;
            }
            try {
                this.f11578e.getContentResolver().bulkInsert(OGuardConst.f12014b, contentValuesArr);
            } catch (Exception e10) {
                LocalLog.b("AppInfoManager", "Fail to insert e=" + e10);
            }
            f.y2(this.f11578e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() {
        if (CommonUtil.T(this.f11578e)) {
            LocalLog.b("AppInfoManager", "initAppNameList skip boot reg");
            this.f11579f.sendEmptyMessageDelayed(5, 60000L);
            return;
        }
        if (this.f11579f.hasMessages(5)) {
            this.f11579f.removeMessages(5);
        }
        List<ApplicationInfo> installedApplications = this.f11578e.getPackageManager().getInstalledApplications(128);
        this.f11582i.clear();
        Iterator<ApplicationInfo> it = installedApplications.iterator();
        while (it.hasNext()) {
            this.f11582i.add(it.next().packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        List<String> e10 = GuardElfDataManager.d(this.f11578e).e("notify_whitelist.xml");
        this.f11583j = e10;
        if (e10.size() == 0) {
            this.f11579f.sendEmptyMessageDelayed(4, StatIdManager.EXPIRE_TIME_MS);
        }
        if (AppFeature.a() != null) {
            this.f11584k = AppFeature.a();
        }
        this.f11585l = CustmizeAllowBgRunable.i(this.f11578e).j();
    }

    private void r() {
        this.f11579f.postDelayed(new a(), StatIdManager.EXPIRE_TIME_MS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s(Message message) {
        Bundle data = message.getData();
        int i10 = data.getInt(TriggerEvent.EXTRA_UID);
        String string = data.getString(DeviceDomainManager.ARG_PKG);
        if (data.getBoolean("delete")) {
            this.f11578e.getContentResolver().delete(OGuardConst.f12014b, "pkg_name==?", new String[]{string});
            return;
        }
        Handler handler = this.f11579f;
        handler.sendMessageDelayed(handler.obtainMessage(6, string), 60000L);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TriggerEvent.EXTRA_UID, Integer.valueOf(i10));
        contentValues.put(UserProfileConstants.COLUMN_PKG_NAME, string);
        contentValues.put("app_type", Integer.valueOf(this.f11580g.getAppType(string)));
        contentValues.put("optimize_value", Integer.valueOf(this.f11583j.contains(string) ? 1 : 0));
        contentValues.put("background_value", Integer.valueOf((this.f11584k.contains(string) || this.f11585l.contains(string)) ? 2 : 0));
        try {
            this.f11578e.getContentResolver().insert(OGuardConst.f12014b, contentValues);
        } catch (Exception e10) {
            LocalLog.b("AppInfoManager", "Fail to insert e=" + e10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t(String str, int i10) {
        B(str, i10);
        this.f11578e.getContentResolver().notifyChange(OGuardConst.f12014b, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v(String str, int i10) {
        C(str, i10);
        this.f11578e.getContentResolver().notifyChange(OGuardConst.f12014b, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z() {
        if (l(this.f11582i) != null) {
            this.f11586m = l(this.f11582i);
        }
        Message obtain = Message.obtain();
        obtain.what = 1;
        this.f11579f.sendMessage(obtain);
    }

    public void A(Message message) {
        String str = (String) message.obj;
        int appType = this.f11580g.getAppType(str);
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_type", Integer.valueOf(appType));
        String[] strArr = {str};
        ContentResolver contentResolver = this.f11578e.getContentResolver();
        Uri uri = OGuardConst.f12014b;
        contentResolver.update(uri, contentValues, "pkg_name==?", strArr);
        this.f11578e.getContentResolver().notifyChange(uri, null);
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        int i11;
        if (intent.getData() != null) {
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
            LocalLog.l("AppInfoManager", "affairType:" + i10 + " replace:" + booleanExtra + "pkg:" + schemeSpecificPart);
            if (booleanExtra) {
                return;
            }
            try {
                i11 = this.f11578e.getPackageManager().getApplicationInfo(schemeSpecificPart, 128).uid;
            } catch (PackageManager.NameNotFoundException e10) {
                e10.printStackTrace();
                i11 = -1;
            }
            Message obtain = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt(TriggerEvent.EXTRA_UID, i11);
            bundle.putString(DeviceDomainManager.ARG_PKG, schemeSpecificPart);
            obtain.what = 2;
            if (i10 != 1101) {
                if (i10 != 1102) {
                    return;
                }
                bundle.putBoolean("delete", true);
                obtain.setData(bundle);
                this.f11579f.sendMessage(obtain);
                this.f11582i.remove(schemeSpecificPart);
                return;
            }
            bundle.putBoolean("delete", false);
            obtain.setData(bundle);
            this.f11579f.sendMessage(obtain);
            if (this.f11582i.contains(schemeSpecificPart)) {
                return;
            }
            this.f11582i.add(schemeSpecificPart);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, 1101);
        Affair.f().g(this, 1102);
    }

    public void u() {
        this.f11579f.post(new c());
    }

    public void w() {
        this.f11579f.sendEmptyMessage(1);
    }

    public void x(String str, int i10, boolean z10) {
        this.f11579f.post(new d(str, i10, z10));
    }

    public void y(String str, int i10) {
        this.f11579f.post(new b(str, i10));
    }
}
