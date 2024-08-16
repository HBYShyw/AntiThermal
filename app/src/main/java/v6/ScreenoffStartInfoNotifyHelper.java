package v6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import b6.LocalLog;
import c6.NotifyUtil;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import java.util.ArrayList;
import java.util.List;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;
import z5.GuardElfDataManager;

/* compiled from: ScreenoffStartInfoNotifyHelper.java */
/* renamed from: v6.e, reason: use source file name */
/* loaded from: classes.dex */
public class ScreenoffStartInfoNotifyHelper implements IAffairCallback {

    /* renamed from: n, reason: collision with root package name */
    private static final String f19168n = "e";

    /* renamed from: e, reason: collision with root package name */
    private Context f19169e;

    /* renamed from: f, reason: collision with root package name */
    private UploadDataUtil f19170f;

    /* renamed from: g, reason: collision with root package name */
    private NotifyUtil f19171g;

    /* renamed from: h, reason: collision with root package name */
    private List<String> f19172h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f19173i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f19174j;

    /* renamed from: k, reason: collision with root package name */
    private long f19175k;

    /* renamed from: l, reason: collision with root package name */
    private Handler f19176l;

    /* renamed from: m, reason: collision with root package name */
    private final Runnable f19177m;

    /* compiled from: ScreenoffStartInfoNotifyHelper.java */
    /* renamed from: v6.e$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (GuardElfContext.e().h().isInteractive()) {
                ScreenoffStartInfoNotifyHelper.this.f();
            }
        }
    }

    /* compiled from: ScreenoffStartInfoNotifyHelper.java */
    /* renamed from: v6.e$b */
    /* loaded from: classes.dex */
    private static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final ScreenoffStartInfoNotifyHelper f19179a = new ScreenoffStartInfoNotifyHelper(null);
    }

    /* synthetic */ ScreenoffStartInfoNotifyHelper(a aVar) {
        this();
    }

    public static ScreenoffStartInfoNotifyHelper b() {
        return b.f19179a;
    }

    private void c(Intent intent) {
        String stringExtra = intent.getStringExtra("type");
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("data");
        if (stringArrayListExtra != null && !stringArrayListExtra.isEmpty()) {
            if (stringExtra != null && stringExtra.equals("startinfo")) {
                for (int i10 = 0; i10 < stringArrayListExtra.size(); i10++) {
                    LocalLog.a(f19168n, stringArrayListExtra.get(i10));
                }
                GuardElfDataManager.d(this.f19169e).g(this.f19169e, stringExtra, stringArrayListExtra);
                f();
                this.f19170f.L0(stringExtra, stringArrayListExtra);
                return;
            }
            LocalLog.a(f19168n, "handleMonitorDataAction: is not start info type. return");
            return;
        }
        LocalLog.a(f19168n, "handleMonitorDataAction: list is null or empty. return. type=" + stringExtra);
    }

    private void d(boolean z10) {
        f6.f.x2(this.f19169e, z10);
        LocalLog.a(f19168n, "handleNotifyRestrictedApp: enable=" + z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        LocalLog.a(f19168n, "notifySreenoffAndStartInfoNotRestrict");
        if (this.f19172h.size() > 20) {
            this.f19172h.clear();
        }
        List<String> e10 = GuardElfDataManager.d(this.f19169e).e("screenoff_user_not_restrict_third_app_thistime.xml");
        List<String> e11 = GuardElfDataManager.d(this.f19169e).e("startinfo_user_not_restrict_thistime.xml");
        int size = e10.size();
        int size2 = e11.size();
        for (int i10 = 0; i10 < size; i10++) {
            String str = e10.get(i10);
            e11.contains(str);
            if (!this.f19172h.contains(str)) {
                this.f19172h.add(str);
                this.f19174j = true;
            }
        }
        for (int i11 = 0; i11 < e11.size(); i11++) {
            String str2 = e11.get(i11);
            if (!this.f19172h.contains(str2)) {
                this.f19172h.add(str2);
                this.f19174j = true;
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j10 = this.f19175k;
        if (currentTimeMillis > j10 && !DateUtils.isToday(j10)) {
            this.f19173i = false;
        }
        String str3 = null;
        if (size != 0) {
            str3 = e10.get(0);
        } else if (size2 != 0) {
            str3 = e11.get(0);
        }
        if (this.f19174j) {
            this.f19173i = true;
            this.f19175k = currentTimeMillis;
            NotifyUtil.v(this.f19169e).Q(str3, true);
        } else if (!this.f19173i) {
            this.f19173i = true;
            this.f19175k = currentTimeMillis;
            NotifyUtil.v(this.f19169e).Q(str3, false);
        }
        this.f19174j = false;
    }

    public void e() {
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        if (i10 != 202) {
            if (i10 == 213) {
                c(intent);
                return;
            } else {
                if (i10 != 222) {
                    return;
                }
                d(intent.getBooleanExtra("enable", false));
                return;
            }
        }
        if (this.f19176l == null) {
            this.f19176l = new Handler(Looper.myLooper());
        }
        this.f19176l.removeCallbacks(this.f19177m);
        this.f19176l.sendMessageDelayed(Message.obtain(this.f19176l, this.f19177m), 3000L);
        if (f6.f.Y(this.f19169e)) {
            LocalLog.a(f19168n, "handleScreenOnAction: notifySreenoffRestricts");
            this.f19171g.T();
            this.f19171g.R();
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_HOLIDAY);
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().g(this, EventType.SCENE_MODE_CONFERENCE);
    }

    private ScreenoffStartInfoNotifyHelper() {
        this.f19172h = new ArrayList();
        this.f19173i = false;
        this.f19174j = false;
        this.f19175k = -1L;
        this.f19177m = new a();
        Context c10 = GuardElfContext.e().c();
        this.f19169e = c10;
        this.f19170f = UploadDataUtil.S0(c10);
        this.f19171g = NotifyUtil.v(this.f19169e);
    }
}
