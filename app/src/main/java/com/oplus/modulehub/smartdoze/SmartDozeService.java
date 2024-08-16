package com.oplus.modulehub.smartdoze;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Xml;
import b6.LocalLog;
import c7.WhiteListManager;
import com.oplus.deepthinker.sdk.app.ClientConnection;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.modulehub.smartdoze.SmartDozeService;
import com.oplus.statistics.record.StatIdManager;
import d7.RomUpdateHelper;
import e7.ScenarioUtil;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.Executors;
import org.xmlpull.v1.XmlSerializer;
import w4.Affair;
import w4.IAffairCallback;

/* loaded from: classes.dex */
public class SmartDozeService extends Service implements RomUpdateHelper.b, IAffairCallback {
    private static Handler A;

    /* renamed from: z, reason: collision with root package name */
    private static HandlerThread f9914z;

    /* renamed from: e, reason: collision with root package name */
    private Handler f9915e;

    /* renamed from: f, reason: collision with root package name */
    private PowerManager f9916f;

    /* renamed from: g, reason: collision with root package name */
    private AlarmManager f9917g;

    /* renamed from: h, reason: collision with root package name */
    private WhiteListManager f9918h;

    /* renamed from: i, reason: collision with root package name */
    private RomUpdateHelper f9919i;

    /* renamed from: j, reason: collision with root package name */
    private PendingIntent f9920j;

    /* renamed from: k, reason: collision with root package name */
    private PowerManager.WakeLock f9921k;

    /* renamed from: l, reason: collision with root package name */
    private ClientConnection f9922l;

    /* renamed from: m, reason: collision with root package name */
    private OplusDeepThinkerManager f9923m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f9924n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f9925o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f9926p = false;

    /* renamed from: q, reason: collision with root package name */
    private boolean f9927q = true;

    /* renamed from: r, reason: collision with root package name */
    private boolean f9928r = false;

    /* renamed from: s, reason: collision with root package name */
    private boolean f9929s = false;

    /* renamed from: t, reason: collision with root package name */
    private boolean f9930t = false;

    /* renamed from: u, reason: collision with root package name */
    private long f9931u = 0;

    /* renamed from: v, reason: collision with root package name */
    private long f9932v = 0;

    /* renamed from: w, reason: collision with root package name */
    private BroadcastReceiver f9933w = new a();

    /* renamed from: x, reason: collision with root package name */
    private BroadcastReceiver f9934x = new b();

    /* renamed from: y, reason: collision with root package name */
    private final BroadcastReceiver f9935y = new c();

    /* loaded from: classes.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!SmartDozeService.this.f9919i.d()) {
                LocalLog.a("SmartDoze:SmartDozeService", "deviceidle broadcast,smartdoze closed");
            }
            synchronized (SmartDozeService.this) {
                if ("android.os.action.DEVICE_IDLE_MODE_CHANGED".equals(intent.getAction())) {
                    SmartDozeService smartDozeService = SmartDozeService.this;
                    smartDozeService.f9929s = smartDozeService.f9916f.isDeviceIdleMode();
                    if (!SmartDozeService.this.f9929s) {
                        SmartDozeService.this.f9930t = true;
                        SmartDozeService.this.F();
                    } else {
                        SmartDozeService.this.J();
                    }
                    LocalLog.a("SmartDoze:SmartDozeService", "br deviceilde:" + SmartDozeService.this.f9929s);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    class b extends BroadcastReceiver {
        b() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z10;
            boolean z11;
            if (!SmartDozeService.this.f9919i.d()) {
                LocalLog.a("SmartDoze:SmartDozeService", "battery change broadcast,smartdoze closed");
            }
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                if (intent.getIntExtra("plugged", 0) != 0) {
                    synchronized (SmartDozeService.this) {
                        z11 = !SmartDozeService.this.f9925o;
                        SmartDozeService.this.f9925o = true;
                        if (SmartDozeService.this.f9929s || SmartDozeService.this.f9928r) {
                            SmartDozeService.this.f9928r = false;
                            SmartDozeService.this.f9929s = false;
                        }
                    }
                    if (z11) {
                        SmartDozeService.this.J();
                        return;
                    }
                    return;
                }
                synchronized (SmartDozeService.this) {
                    z10 = SmartDozeService.this.f9925o;
                    SmartDozeService.this.f9925o = false;
                }
                if (z10) {
                    SmartDozeService.this.F();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    class c extends BroadcastReceiver {
        c() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("action.guardelf.smartdoze.TRY_ENTER_DOZE".equals(intent.getAction())) {
                LocalLog.a("SmartDoze:SmartDozeService", "try enter to doze");
                if (SmartDozeService.this.f9921k == null) {
                    SmartDozeService smartDozeService = SmartDozeService.this;
                    smartDozeService.f9921k = smartDozeService.f9916f.newWakeLock(1, "Smartdoze:check");
                }
                SmartDozeService.this.f9921k.acquire(StatIdManager.EXPIRE_TIME_MS);
                SmartDozeService.this.y();
                if (SmartDozeService.this.f9921k.isHeld()) {
                    SmartDozeService.this.f9921k.release();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 0) {
                return;
            }
            boolean d10 = SmartDozeService.this.f9919i.d();
            if (!d10) {
                LocalLog.a("SmartDoze:SmartDozeService", "screen broadcast,smartdoze closed");
            }
            synchronized (SmartDozeService.this) {
                int i10 = message.arg1;
                if (i10 == 1) {
                    SmartDozeService.this.f9928r = false;
                    SmartDozeService.this.f9932v = SystemClock.elapsedRealtime();
                    SmartDozeService.this.f9924n = true;
                    SmartDozeService.this.J();
                    LocalLog.a("SmartDoze:SmartDozeService", "screen on,mLastScreenOn=" + SmartDozeService.this.f9932v);
                } else if (i10 == 2) {
                    SmartDozeService.this.f9924n = false;
                    long elapsedRealtime = SystemClock.elapsedRealtime() - SmartDozeService.this.f9932v;
                    long j10 = 120000;
                    if (SmartDozeService.this.f9930t && elapsedRealtime < 120000) {
                        SmartDozeService.this.G(120000L);
                    } else {
                        j10 = 300000;
                        SmartDozeService.this.f9930t = false;
                        if (d10) {
                            SmartDozeService.this.H(300000L);
                        }
                    }
                    if (!SmartDozeService.this.f9929s && !SmartDozeService.this.f9925o && !SmartDozeService.this.f9924n) {
                        LocalLog.a("SmartDoze:SmartDozeService", "screen off,delay to enterDoze =" + j10 + ",mIsIdleWhenLastScreenOff=" + SmartDozeService.this.f9930t + ",mLastScreenOn=" + SmartDozeService.this.f9932v);
                    }
                    SmartDozeService.this.f9931u = SystemClock.elapsedRealtime();
                }
            }
        }
    }

    private boolean B() {
        if (this.f9923m == null) {
            OplusDeepThinkerManager oplusDeepThinkerManager = new OplusDeepThinkerManager(getApplicationContext());
            this.f9923m = oplusDeepThinkerManager;
            oplusDeepThinkerManager.setRemote(this.f9922l.getDeepThinkerBridge());
        }
        return this.f9923m.getIdleScreenResultInShortTime() == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void C() {
        synchronized (this) {
            this.f9924n = this.f9916f.isInteractive();
            this.f9929s = this.f9916f.isDeviceIdleMode();
            this.f9919i.c();
        }
    }

    private boolean D() {
        if (!this.f9919i.d()) {
            return false;
        }
        synchronized (this) {
            if (!this.f9925o && !this.f9924n) {
                if (!this.f9918h.h(this.f9915e, this.f9922l)) {
                    return false;
                }
                Set<String> g6 = this.f9918h.g();
                E(g6);
                LocalLog.a("SmartDoze:SmartDozeService", "add whitelist to deviceidleController: " + g6);
                synchronized (this) {
                    Intent intent = new Intent("oplus.intent.action_GUARDELF_SMART_DOZE_CHANGE");
                    intent.putExtra("smartdoze_enter", true);
                    sendBroadcast(intent, "oplus.permission.OPLUS_COMPONENT_SAFE");
                    this.f9928r = true;
                    LocalLog.a("SmartDoze:SmartDozeService", "setDeviceIdle,deviceidle=" + this.f9929s);
                }
                return true;
            }
            return false;
        }
    }

    private static void E(Set<String> set) {
        File dataDirectory = Environment.getDataDirectory();
        if (dataDirectory.exists()) {
            File file = new File(dataDirectory.getAbsolutePath() + "/system/smart_doze_config_local.xml");
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        LocalLog.a("SmartDoze:SmartDozeService", "saveSmartDozeWhiteListSetByUser: failed create file /system/smart_doze_config_local.xml");
                    }
                } catch (IOException unused) {
                    LocalLog.a("SmartDoze:SmartDozeService", "saveSmartDozeWhiteListSetByUser: failed create file");
                }
            }
            if (!file.exists() || set == null || set.isEmpty()) {
                return;
            }
            FileOutputStream fileOutputStream = null;
            try {
                try {
                    try {
                        FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                        try {
                            XmlSerializer newSerializer = Xml.newSerializer();
                            newSerializer.setOutput(fileOutputStream2, "UTF-8");
                            newSerializer.startDocument(null, Boolean.TRUE);
                            newSerializer.startTag(null, "gs");
                            newSerializer.startTag(null, "name");
                            newSerializer.text("doze_wl_smart_doze_local");
                            newSerializer.endTag(null, "name");
                            for (String str : set) {
                                if (str != null) {
                                    newSerializer.startTag(null, "wl");
                                    newSerializer.text(str);
                                    newSerializer.endTag(null, "wl");
                                }
                            }
                            newSerializer.endTag(null, "gs");
                            newSerializer.endDocument();
                            newSerializer.flush();
                            fileOutputStream2.close();
                        } catch (Exception unused2) {
                            fileOutputStream = fileOutputStream2;
                            LocalLog.a("SmartDoze:SmartDozeService", "saveSmartDozeWhiteListSetByUser: failed write file");
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                            }
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream = fileOutputStream2;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException unused3) {
                                    LocalLog.a("SmartDoze:SmartDozeService", "saveSmartDozeWhiteListSetByUser failed close stream");
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Exception unused4) {
                }
            } catch (IOException unused5) {
                LocalLog.a("SmartDoze:SmartDozeService", "saveSmartDozeWhiteListSetByUser failed close stream");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F() {
        LocalLog.a("SmartDoze:SmartDozeService", "scheduleNextCheck");
        J();
        G(240000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G(long j10) {
        LocalLog.a("SmartDoze:SmartDozeService", "screen off,delay try to enterDoze =" + j10);
        H(j10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void H(long j10) {
        if (!this.f9919i.d()) {
            LocalLog.a("SmartDoze:SmartDozeService", "smartdoze closed, do not check");
            return;
        }
        synchronized (this) {
            if (!this.f9929s && !this.f9925o && !this.f9924n) {
                LocalLog.a("SmartDoze:SmartDozeService", "condition ok, really start schedule next condition");
                J();
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (j10 <= 0) {
                    j10 = 240000;
                }
                this.f9917g.setExact(3, elapsedRealtime + j10, this.f9920j);
                LocalLog.a("SmartDoze:SmartDozeService", "set tryEnterDozePendingIntent alarm");
                return;
            }
            LocalLog.a("SmartDoze:SmartDozeService", "in idle/charge/screenon state need not check");
        }
    }

    public static void I(Context context) {
        if (context == null) {
            return;
        }
        LocalLog.a("SmartDoze:SmartDozeService", "start smart doze service");
        Intent intent = new Intent();
        intent.setClassName(context, SmartDozeService.class.getName());
        context.startService(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void J() {
        LocalLog.a("SmartDoze:SmartDozeService", "remove alarm: check condition");
        this.f9917g.cancel(this.f9920j);
    }

    private boolean x() {
        boolean z10;
        boolean z11;
        boolean z12;
        StringBuilder sb2 = new StringBuilder();
        synchronized (this) {
            z10 = true;
            z11 = false;
            if (!this.f9929s && !this.f9924n) {
                z12 = false;
            }
            z12 = true;
        }
        if (!this.f9919i.d()) {
            sb2.append("smartdoze closed");
        } else if (z12) {
            sb2.append("idle or sceen on;");
        } else if (A()) {
            sb2.append("charging;");
        } else {
            boolean B = B();
            LocalLog.a("SmartDoze:SmartDozeService", "mayLongSleep = " + B);
            if (!B) {
                sb2.append("sleep pridict not long sleep;");
                z10 = false;
            }
            if (z10 && ScenarioUtil.i(this)) {
                sb2.append("phone is in use;");
            } else {
                z11 = z10;
            }
        }
        if (!z11) {
            LocalLog.a("SmartDoze:SmartDozeService", "checkIfCanEnterDoze,not match,because:" + ((Object) sb2));
        }
        return z11;
    }

    private void z() {
        Intent intent = new Intent("action.guardelf.smartdoze.TRY_ENTER_DOZE");
        intent.setPackage("com.oplus.battery");
        this.f9920j = PendingIntent.getBroadcast(this, 0, intent, 67108864);
        registerReceiver(this.f9935y, new IntentFilter("action.guardelf.smartdoze.TRY_ENTER_DOZE"), null, this.f9915e, 2);
    }

    public synchronized boolean A() {
        LocalLog.a("SmartDoze:SmartDozeService", "isDeviceCharging,=" + this.f9925o);
        return this.f9925o;
    }

    public void K() {
        Affair.f().i(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().i(this, EventType.SCENE_MODE_LOCATION);
    }

    @Override // d7.RomUpdateHelper.b
    public void a() {
        if (this.f9919i.d()) {
            LocalLog.a("SmartDoze:SmartDozeService", "onconfig Change");
            F();
        } else {
            A.removeCallbacksAndMessages(null);
            J();
        }
    }

    @Override // android.app.Service
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("SmartDozeService:\n");
        sb2.append("mIsFeatureOpen=");
        sb2.append(this.f9919i.d());
        sb2.append("\n");
        synchronized (this) {
            sb2.append("mIsIdled=");
            sb2.append(this.f9929s);
            sb2.append(",mIsSetIdle=");
            sb2.append(this.f9928r);
            sb2.append("\n");
            sb2.append("mScreenOn=");
            sb2.append(this.f9924n);
            sb2.append(",mIsCharging=");
            sb2.append(this.f9925o);
            sb2.append("\n");
            sb2.append("mIsInit=");
            sb2.append(this.f9926p);
            sb2.append(",mIsFirstStart=");
            sb2.append(this.f9927q);
            sb2.append("\n");
            sb2.append("mLastScreenOff=");
            sb2.append(this.f9931u);
            sb2.append(",mLastScreenOn=");
            sb2.append(this.f9932v);
            sb2.append("\n");
        }
        sb2.append("whiteList=");
        sb2.append(this.f9918h.g());
        sb2.append("\n");
        printWriter.println(sb2.toString());
        printWriter.close();
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Intent intent) {
        Message obtain = Message.obtain();
        if (i10 == 201) {
            obtain.what = 0;
            obtain.arg1 = 2;
        } else if (i10 == 202) {
            obtain.what = 0;
            obtain.arg1 = 1;
        }
        Handler handler = A;
        if (handler != null) {
            handler.sendMessage(obtain);
        }
    }

    @Override // w4.IAffairCallback
    public void execute(int i10, Bundle bundle) {
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        LocalLog.a("SmartDoze:SmartDozeService", "onCreate,this = " + this);
        this.f9916f = (PowerManager) getSystemService("power");
        this.f9917g = (AlarmManager) getSystemService("alarm");
        Context applicationContext = getApplicationContext();
        if (applicationContext == null) {
            stopSelf();
            return;
        }
        if (!this.f9926p) {
            this.f9926p = true;
        }
        this.f9918h = new WhiteListManager(applicationContext);
        RomUpdateHelper romUpdateHelper = new RomUpdateHelper(this);
        this.f9919i = romUpdateHelper;
        romUpdateHelper.h(this);
        HandlerThread handlerThread = new HandlerThread("enter_doze");
        handlerThread.start();
        this.f9915e = new Handler(handlerThread.getLooper());
        HandlerThread handlerThread2 = new HandlerThread("smartdoze.handler", 10);
        f9914z = handlerThread2;
        handlerThread2.start();
        Looper looper = f9914z.getLooper();
        if (looper != null) {
            d dVar = new d(looper);
            A = dVar;
            dVar.post(new Runnable() { // from class: c7.a
                @Override // java.lang.Runnable
                public final void run() {
                    SmartDozeService.this.C();
                }
            });
            z();
            registerAction();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
            registerReceiver(this.f9933w, intentFilter, null, A, 2);
            registerReceiver(this.f9934x, new IntentFilter("android.intent.action.BATTERY_CHANGED"), null, A, 2);
            this.f9922l = new ClientConnection(getApplicationContext(), Executors.newFixedThreadPool(3), this.f9915e);
            OplusDeepThinkerManager oplusDeepThinkerManager = new OplusDeepThinkerManager(getApplicationContext());
            this.f9923m = oplusDeepThinkerManager;
            oplusDeepThinkerManager.setRemote(this.f9922l.getDeepThinkerBridge());
            return;
        }
        stopSelf();
    }

    @Override // android.app.Service
    public void onDestroy() {
        K();
        unregisterReceiver(this.f9933w);
        unregisterReceiver(this.f9934x);
        unregisterReceiver(this.f9935y);
        Handler handler = A;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        HandlerThread handlerThread = f9914z;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
        this.f9919i.i();
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i10, int i11) {
        if (!this.f9919i.d()) {
            return super.onStartCommand(intent, i10, i11);
        }
        if (this.f9927q) {
            this.f9927q = false;
        }
        return super.onStartCommand(intent, i10, i11);
    }

    @Override // w4.IAffairCallback
    public void registerAction() {
        Affair.f().g(this, EventType.SCENE_MODE_AUDIO_OUT);
        Affair.f().g(this, EventType.SCENE_MODE_LOCATION);
    }

    public void y() {
        if (!x()) {
            G(600000L);
        } else if (D()) {
            J();
        } else {
            G(600000L);
        }
    }
}
