package v6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.powermanager.powercurve.SaveBatteryStatsReceiver;
import f6.ChargeUtil;
import f6.CommonUtil;
import f7.AbnormalPushOrBeatUpload;
import ha.StorageMonitorService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import v4.GuardElfContext;
import w4.Affair;
import w4.IAffairCallback;
import x5.UploadDataUtil;

/* compiled from: AffairDcsHelper.java */
/* renamed from: v6.a, reason: use source file name */
/* loaded from: classes.dex */
public class AffairDcsHelper implements IAffairCallback {

    /* renamed from: g, reason: collision with root package name */
    private static final String f19126g = "a";

    /* renamed from: e, reason: collision with root package name */
    private Context f19127e;

    /* renamed from: f, reason: collision with root package name */
    private UploadDataUtil f19128f;

    /* compiled from: AffairDcsHelper.java */
    /* renamed from: v6.a$b */
    /* loaded from: classes.dex */
    private static class b {

        /* renamed from: a, reason: collision with root package name */
        private static final AffairDcsHelper f19129a = new AffairDcsHelper();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AffairDcsHelper.java */
    /* renamed from: v6.a$c */
    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private Context f19130e;

        /* renamed from: f, reason: collision with root package name */
        private String f19131f;

        /* renamed from: g, reason: collision with root package name */
        private String f19132g;

        public c(Context context, String str, String str2) {
            this.f19130e = context;
            this.f19131f = str;
            this.f19132g = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            AffairDcsHelper.this.f19128f.c(this.f19130e, this.f19131f, this.f19132g);
        }
    }

    private void b() {
        UploadDataUtil S0 = UploadDataUtil.S0(this.f19127e);
        S0.i(Integer.toString(f6.f.p(this.f19127e)));
        S0.j(Integer.toString(ChargeUtil.n(this.f19127e)));
        f6.f.S1(this.f19127e, 0);
    }

    private void c() {
        HashMap hashMap = new HashMap();
        hashMap.put("switch", CommonUtil.A() + "_static");
        UploadDataUtil.S0(this.f19127e).J(hashMap);
    }

    private void d() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        f6.f.l(arrayList, arrayList2, this.f19127e);
        HashMap hashMap = new HashMap();
        hashMap.put("prohibit_foreground_num", String.valueOf(arrayList2.size()));
        hashMap.put("allow_background_num", String.valueOf(arrayList.size()));
        StringBuilder sb2 = new StringBuilder("");
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            sb2.append(((String) it.next()) + ",");
        }
        hashMap.put("prohibit_foreground_pkg", sb2.toString());
        StringBuilder sb3 = new StringBuilder("");
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            sb3.append(((String) it2.next()) + ",");
        }
        hashMap.put("allow_background_pkg", sb3.toString());
        this.f19128f.i0(hashMap);
    }

    private void e() {
        UploadDataUtil.S0(this.f19127e).l0();
    }

    private void f() {
        StorageMonitorService.Y(this.f19127e).v0();
    }

    public static AffairDcsHelper g() {
        return b.f19129a;
    }

    private void h(String str, ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        Iterator<String> it = arrayList.iterator();
        String str2 = "";
        while (it.hasNext()) {
            str2 = it.next();
            this.f19128f.g(str, str2);
        }
        LocalLog.a(f19126g, " dataSize: " + arrayList.size() + " " + str2);
    }

    private void i(String str, String str2) {
        if (str == null || str2 == null || str.equals("") || str2.equals("") || CommonUtil.V()) {
            return;
        }
        new Thread(new c(this.f19127e, str2, str)).start();
    }

    private void j(String str, ArrayList<String> arrayList) {
        if (arrayList == null || str == null) {
            return;
        }
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            this.f19128f.P0(str, arrayList.get(i10));
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        switch (i10) {
            case EventType.SCENE_MODE_READING /* 207 */:
                d();
                e();
                c();
                f();
                b();
                Settings.System.putLong(this.f19127e.getContentResolver(), "OPLUS_BATTERY_DATE_CHANGE_TIME", System.currentTimeMillis());
                SaveBatteryStatsReceiver.p(this.f19127e);
                return;
            case EventType.SCENE_MODE_AUDIO_CALL /* 208 */:
                String stringExtra = intent.getStringExtra("eventId");
                String stringExtra2 = intent.getStringExtra("act");
                if (stringExtra == null || stringExtra2 == null || stringExtra.equals("") || stringExtra2.equals("")) {
                    return;
                }
                this.f19128f.e(stringExtra, stringExtra2);
                return;
            case EventType.SCENE_MODE_VIDEO_CALL /* 209 */:
                i(intent.getStringExtra("pkgName"), intent.getStringExtra("anrType"));
                return;
            case EventType.SCENE_MODE_FILE_DOWNLOAD /* 210 */:
                j(intent.getStringExtra("eventId"), intent.getStringArrayListExtra("data"));
                return;
            case EventType.SCENE_MODE_GAME /* 211 */:
                ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("data");
                String stringExtra3 = intent.getStringExtra("type");
                if (stringExtra3 == null || stringArrayListExtra == null || stringArrayListExtra.size() == 0) {
                    return;
                }
                h(stringExtra3, stringArrayListExtra);
                return;
            case EventType.SCENE_MODE_VIDEO_LIVE /* 212 */:
            case EventType.SCENE_MODE_HOLIDAY /* 213 */:
            default:
                return;
            case EventType.SCENE_MODE_FILE_UPLOAD /* 214 */:
                new AbnormalPushOrBeatUpload(this.f19127e).a(intent.getStringExtra("NK_pkgName[0]"), intent.getIntExtra("NK_pushPeriod[0]", 0), 1);
                return;
            case EventType.SCENE_MODE_MUSIC_PLAY /* 215 */:
                new AbnormalPushOrBeatUpload(this.f19127e).a(intent.getStringExtra("NK_pkgName[0]"), intent.getIntExtra("NK_pushPeriod[0]", 0), 2);
                return;
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    public void k() {
        registerAction();
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_READING);
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_CALL);
        Affair.f().g(this, EventType.SCENE_MODE_VIDEO_CALL);
        Affair.f().g(this, EventType.SCENE_MODE_FILE_DOWNLOAD);
        Affair.f().g(this, EventType.SCENE_MODE_GAME);
        Affair.f().g(this, EventType.SCENE_MODE_FILE_UPLOAD);
        Affair.f().g(this, EventType.SCENE_MODE_MUSIC_PLAY);
    }

    private AffairDcsHelper() {
        Context c10 = GuardElfContext.e().c();
        this.f19127e = c10;
        this.f19128f = UploadDataUtil.S0(c10);
    }
}
