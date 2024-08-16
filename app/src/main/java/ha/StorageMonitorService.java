package ha;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.OplusPackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.storage.DiskInfo;
import android.os.storage.IStorageManager;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.DebugUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.statistics.record.StatIdManager;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import v1.COUIContextUtil;
import v4.GuardElfContext;
import x1.COUIAlertDialogBuilder;
import x5.UploadDataUtil;
import z5.GuardElfDataManager;

/* compiled from: StorageMonitorService.java */
/* renamed from: ha.c, reason: use source file name */
/* loaded from: classes2.dex */
public class StorageMonitorService {
    private static StorageMonitorService O = null;
    private static boolean P = false;
    private Intent A;
    private Intent B;
    private Intent C;
    private PendingIntent D;
    private PendingIntent E;
    private long F;
    private long G;
    private long H;
    private long I;
    private IStorageManager K;
    private GuardElfDataManager L;

    /* renamed from: c, reason: collision with root package name */
    private Context f12034c;

    /* renamed from: d, reason: collision with root package name */
    private Handler f12035d;

    /* renamed from: q, reason: collision with root package name */
    private boolean f12048q;

    /* renamed from: r, reason: collision with root package name */
    private long f12049r;

    /* renamed from: s, reason: collision with root package name */
    private int f12050s;

    /* renamed from: t, reason: collision with root package name */
    private int f12051t;

    /* renamed from: u, reason: collision with root package name */
    private long f12052u;

    /* renamed from: x, reason: collision with root package name */
    private List<String> f12055x;

    /* renamed from: y, reason: collision with root package name */
    private Intent f12056y;

    /* renamed from: z, reason: collision with root package name */
    private Intent f12057z;

    /* renamed from: a, reason: collision with root package name */
    private final Object f12032a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private final Object f12033b = new Object();

    /* renamed from: e, reason: collision with root package name */
    private boolean f12036e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f12037f = true;

    /* renamed from: g, reason: collision with root package name */
    private boolean f12038g = false;

    /* renamed from: h, reason: collision with root package name */
    private boolean f12039h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f12040i = true;

    /* renamed from: j, reason: collision with root package name */
    private boolean f12041j = false;

    /* renamed from: k, reason: collision with root package name */
    private AlertDialog f12042k = null;

    /* renamed from: l, reason: collision with root package name */
    private AlertDialog f12043l = null;

    /* renamed from: m, reason: collision with root package name */
    private AlertDialog f12044m = null;

    /* renamed from: n, reason: collision with root package name */
    private AlertDialog f12045n = null;

    /* renamed from: o, reason: collision with root package name */
    private AlertDialog f12046o = null;

    /* renamed from: p, reason: collision with root package name */
    private AlertDialog f12047p = null;

    /* renamed from: v, reason: collision with root package name */
    private List<String> f12053v = new ArrayList();

    /* renamed from: w, reason: collision with root package name */
    private List<String> f12054w = new ArrayList();
    private VolumeInfo J = null;
    private final StorageEventListener M = new k();
    private Handler N = new o(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$a */
    /* loaded from: classes2.dex */
    public class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.M();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$b */
    /* loaded from: classes2.dex */
    public class b implements DialogInterface.OnShowListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            if (y5.b.D()) {
                Button i10 = StorageMonitorService.this.f12042k.i(-3);
                i10.setTextColor(StorageMonitorService.this.f12034c.getColor(R.color.couiGreenTintControlNormal));
                i10.setForceDarkAllowed(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$c */
    /* loaded from: classes2.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            StorageMonitorService.this.s0();
            StorageMonitorService.this.f12043l.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$d */
    /* loaded from: classes2.dex */
    public class d implements View.OnClickListener {
        d() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            StorageMonitorService.this.r0();
            StorageMonitorService.this.f12043l.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$e */
    /* loaded from: classes2.dex */
    public class e implements View.OnClickListener {
        e() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            StorageMonitorService.this.e0();
            StorageMonitorService.this.f12043l.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$f */
    /* loaded from: classes2.dex */
    public class f implements View.OnClickListener {
        f() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            StorageMonitorService.this.w0();
            StorageMonitorService.this.f12043l.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$g */
    /* loaded from: classes2.dex */
    public class g implements DialogInterface.OnClickListener {
        g() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            try {
                StorageMonitorService.this.f12034c.startActivityAsUser(StorageMonitorService.this.f12056y, UserHandle.CURRENT);
            } catch (Throwable th) {
                LocalLog.m("StorageMonitorService", "AlertDialogSd: start FileManager exception", th);
            }
            StorageMonitorService.this.b1("fileManager");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$h */
    /* loaded from: classes2.dex */
    public class h implements DialogInterface.OnClickListener {
        h() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.r0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$i */
    /* loaded from: classes2.dex */
    public class i implements DialogInterface.OnClickListener {
        i() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.b1("cancel");
            LocalLog.a("StorageMonitorService", "sdNotEnoughDialog: cancel");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$j */
    /* loaded from: classes2.dex */
    public class j implements DialogInterface.OnClickListener {
        j() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            try {
                StorageMonitorService.this.f12034c.startActivityAsUser(StorageMonitorService.this.f12056y, UserHandle.CURRENT);
            } catch (Throwable th) {
                LocalLog.m("StorageMonitorService", "DialogTaskTerminationSd: start FileManager exception", th);
            }
            StorageMonitorService.this.c1("fileManager");
        }
    }

    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$k */
    /* loaded from: classes2.dex */
    class k extends StorageEventListener {
        k() {
        }

        public void onVolumeStateChanged(VolumeInfo volumeInfo, int i10, int i11) {
            DiskInfo disk = volumeInfo.getDisk();
            if (disk == null || !disk.isSd()) {
                return;
            }
            if (i10 != 2 && i11 == 2) {
                synchronized (StorageMonitorService.this.f12032a) {
                    StorageMonitorService.this.J = volumeInfo;
                    StorageMonitorService.this.f12048q = true;
                    StorageMonitorService.this.T0();
                    StorageMonitorService.this.f12035d.removeMessages(102);
                    StorageMonitorService.this.A0(200L);
                }
                LocalLog.a("StorageMonitorService", "onVolumeStateChanged: external TF card mounted. id=" + volumeInfo.getId() + ", path=" + volumeInfo.path + ", fsUuid=" + volumeInfo.getFsUuid() + ", fsType=" + volumeInfo.fsType + ", oldState=" + DebugUtils.valueToString(VolumeInfo.class, "STATE_", i10) + ", newState=" + DebugUtils.valueToString(VolumeInfo.class, "STATE_", i11));
                return;
            }
            if (i11 == 2 || i10 != 2) {
                return;
            }
            synchronized (StorageMonitorService.this.f12032a) {
                StorageMonitorService.this.J = null;
                StorageMonitorService.this.f12035d.removeMessages(101);
                StorageMonitorService.this.f12035d.removeMessages(102);
                StorageMonitorService.this.f12035d.sendEmptyMessage(102);
            }
            LocalLog.a("StorageMonitorService", "onVolumeStateChanged: external TF card unmounted. id=" + volumeInfo.getId() + ", path=" + volumeInfo.path + ", fsUuid=" + volumeInfo.getFsUuid() + ", fsType=" + volumeInfo.fsType + ", oldState=" + DebugUtils.valueToString(VolumeInfo.class, "STATE_", i10) + ", newState=" + DebugUtils.valueToString(VolumeInfo.class, "STATE_", i11));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$l */
    /* loaded from: classes2.dex */
    public class l implements DialogInterface.OnClickListener {
        l() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.c1("cancel");
            LocalLog.a("StorageMonitorService", "sdNotEnoughDialog: cancel");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$m */
    /* loaded from: classes2.dex */
    public class m implements DialogInterface.OnClickListener {
        m() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            try {
                Intent intent = new Intent("android.settings.MEMORY_CARD_SETTINGS");
                intent.putExtra("turnToSd", true);
                StorageMonitorService.this.f12034c.startActivityAsUser(intent, UserHandle.CURRENT);
            } catch (Throwable th) {
                LocalLog.m("StorageMonitorService", "sdTransferGuide exception", th);
            }
            StorageMonitorService.this.Z0("storageSetting");
            StorageMonitorService.this.f12045n = null;
            LocalLog.a("StorageMonitorService", "sdTransferGuideDial: storageSetting");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$n */
    /* loaded from: classes2.dex */
    public class n implements DialogInterface.OnClickListener {
        n() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.Z0("cancel");
            StorageMonitorService.this.f12045n = null;
            LocalLog.a("StorageMonitorService", "sdTransferGuideDial: cancel");
        }
    }

    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$o */
    /* loaded from: classes2.dex */
    class o extends Handler {
        o(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (new OplusPackageManager(StorageMonitorService.this.f12034c).isClosedSuperFirewall()) {
                LocalLog.l("StorageMonitorService", "for CSF, return!");
                return;
            }
            if (!StorageUtils.i()) {
                int i10 = message.getData().getInt("dataLevel", 0);
                long j10 = message.getData().getLong("dataFree", SystemProperties.getLong("sys.data.free.bytes", -1L));
                boolean z10 = message.getData().getBoolean("withSd", false);
                switch (message.what) {
                    case 106:
                        StorageMonitorService.this.R0();
                        return;
                    case 107:
                        StorageMonitorService.this.Q0();
                        return;
                    case 108:
                        StorageMonitorService.this.P0();
                        return;
                    case 109:
                        StorageMonitorService.this.O0();
                        return;
                    case 110:
                        StorageMonitorService.this.M0(i10, j10, z10);
                        return;
                    case 111:
                        StorageMonitorService.this.N0(i10, j10, z10);
                        return;
                    default:
                        return;
                }
            }
            StorageMonitor.a().c(message.what);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$p */
    /* loaded from: classes2.dex */
    public class p implements DialogInterface.OnClickListener {
        p() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            try {
                StorageMonitorService.this.f12034c.startActivityAsUser(StorageMonitorService.this.X("StorageDialogApp"), UserHandle.CURRENT);
            } catch (Throwable th) {
                LocalLog.m("StorageMonitorService", "DialogTaskTerminationData: start FileCleanUp exception", th);
            }
            StorageMonitorService.this.X0("cleanUp");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$q */
    /* loaded from: classes2.dex */
    public class q implements DialogInterface.OnClickListener {
        q() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.X0("cancel");
            LocalLog.a("StorageMonitorService", "sdNotEnoughDialog: cancel");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$r */
    /* loaded from: classes2.dex */
    public class r implements DialogInterface.OnClickListener {
        r() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.w0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$s */
    /* loaded from: classes2.dex */
    public class s implements DialogInterface.OnClickListener {
        s() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.e0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$t */
    /* loaded from: classes2.dex */
    public class t implements DialogInterface.OnClickListener {
        t() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.s0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$u */
    /* loaded from: classes2.dex */
    public class u implements DialogInterface.OnShowListener {
        u() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            Button i10 = StorageMonitorService.this.f12043l.i(-1);
            i10.getPaint().setFakeBoldText(true);
            int currentTextColor = i10.getCurrentTextColor();
            Button i11 = StorageMonitorService.this.f12043l.i(-3);
            if (y5.b.D()) {
                i11.setForceDarkAllowed(false);
            }
            i11.setTextColor(currentTextColor);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$v */
    /* loaded from: classes2.dex */
    public class v implements DialogInterface.OnClickListener {
        v() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.r0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$w */
    /* loaded from: classes2.dex */
    public class w implements DialogInterface.OnClickListener {
        w() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
            Settings.System.putIntForUser(StorageMonitorService.this.f12034c.getContentResolver(), "message", 1, 0);
            StorageMonitorService.this.d0();
        }
    }

    /* compiled from: StorageMonitorService.java */
    /* renamed from: ha.c$x */
    /* loaded from: classes2.dex */
    private class x extends Handler {
        public x(Looper looper) {
            super(looper);
        }

        private void a(Message message) {
            switch (message.what) {
                case 101:
                    if (StorageMonitorService.this.Q()) {
                        StorageMonitorService.this.E0();
                        return;
                    }
                    return;
                case 102:
                    StorageMonitorService.this.D0();
                    return;
                case 103:
                    if (StorageMonitorService.this.g0()) {
                        StorageMonitorService.this.T0();
                        StorageMonitorService.this.f12048q = true;
                        if (StorageMonitorService.this.Q()) {
                            StorageMonitorService.this.E0();
                            return;
                        }
                        return;
                    }
                    return;
                case 104:
                    StorageMonitorService.this.f12055x.clear();
                    GuardElfDataManager.d(StorageMonitorService.this.f12034c).k(StorageMonitorService.this.f12055x, "sd_ever_mounted.xml");
                    return;
                case 105:
                    int i10 = SystemProperties.getInt("sys.data.free.level", 0);
                    StorageMonitorService storageMonitorService = StorageMonitorService.this;
                    storageMonitorService.u0(i10, storageMonitorService.f12049r);
                    return;
                default:
                    return;
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            synchronized (StorageMonitorService.this.f12032a) {
                a(message);
            }
        }
    }

    @SuppressLint({"WrongConstant"})
    private StorageMonitorService(Context context) {
        this.f12034c = context;
        StorageConfigManager.i(context, false);
        StorageUtils.g(this.f12034c);
        this.F = StorageUtils.f();
        this.G = StorageUtils.e();
        Intent intent = new Intent("oplus.intent.action.filemanager.OPEN_FILEMANAGER");
        this.f12056y = intent;
        intent.addFlags(335544320);
        Intent intent2 = new Intent("oplus.intent.action.filemanager.AKEY_TO_MOVE");
        this.f12057z = intent2;
        intent2.addFlags(603979776);
        Intent intent3 = new Intent("android.intent.action.MANAGE_PACKAGE_STORAGE");
        this.A = intent3;
        intent3.addFlags(335544320);
        Intent intent4 = new Intent("android.intent.action.OPLUS_SDCARD_STORAGE_LOW");
        this.B = intent4;
        intent4.addFlags(67108864);
        Intent intent5 = new Intent("oplus.intent.action.OPLUS_SDCARD_STORAGE_OK");
        this.C = intent5;
        intent5.addFlags(67108864);
        Intent intent6 = new Intent("oplus.intent.action.DIALOG_DATA");
        intent6.setPackage(this.f12034c.getPackageName());
        this.D = PendingIntent.getBroadcast(this.f12034c, 0, intent6, 67108864);
        Intent intent7 = new Intent("oplus.intent.action.DIALOG_DATA");
        intent7.setPackage(this.f12034c.getPackageName());
        this.E = PendingIntent.getBroadcast(this.f12034c, 0, intent7, 67108864);
        this.f12049r = SystemProperties.getLong("sys.data.free.bytes", -1L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A0(long j10) {
        this.f12035d.removeMessages(101);
        this.f12035d.sendEmptyMessageDelayed(101, j10);
    }

    private void B0(long j10) {
        long currentTimeMillis = System.currentTimeMillis();
        long j11 = currentTimeMillis + j10;
        if (j10 <= 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentTimeMillis);
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.add(5, 1);
            int nextInt = new Random().nextInt(calendar.getTimeInMillis() - currentTimeMillis < 21600000 ? (int) (calendar.getTimeInMillis() - currentTimeMillis) : 21600000);
            if (nextInt < 60000) {
                nextInt = 60000;
            }
            j11 = nextInt + currentTimeMillis;
        }
        ((AlarmManager) this.f12034c.getSystemService("alarm")).setExact(1, j11, this.D);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j11);
        LocalLog.a("StorageMonitorService", "schedule Alarm Dialog Data: alarmTime= " + calendar2.getTime());
    }

    private void C0(long j10) {
        long currentTimeMillis = System.currentTimeMillis() + j10;
        ((AlarmManager) this.f12034c.getSystemService("alarm")).setExact(1, currentTimeMillis, this.E);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        LocalLog.a("StorageMonitorService", "schedule Alarm Dialog Sd: alarmTime= " + calendar.getTime());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void D0() {
        P = false;
        LocalLog.a("StorageMonitorService", "sdCardUnmounted");
        if (this.f12036e) {
            this.f12036e = false;
            LocalLog.a("StorageMonitorService", "sdCardUnmounted: Cancelling notification");
            F0();
        }
        p0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void E0() {
        if (this.f12048q && this.J != null) {
            this.f12048q = false;
            HashMap hashMap = new HashMap();
            b0(hashMap);
            if (n0(this.J.getFsUuid())) {
                hashMap.put("first_time_insert", String.valueOf(false));
                a1(hashMap);
                LocalLog.a("StorageMonitorService", "sdTransferGuide: ever mount.");
                return;
            }
            hashMap.put("first_time_insert", String.valueOf(true));
            this.f12055x.add(this.J.getFsUuid());
            this.L.k(this.f12055x, "sd_ever_mounted.xml");
            long d10 = StorageUtils.d();
            long j10 = this.H;
            if (j10 > 8589934592L && j10 > d10) {
                hashMap.put("sd_free_satisfy", String.valueOf(true));
                a1(hashMap);
                H0(108, true);
                return;
            }
            hashMap.put("sd_free_satisfy", String.valueOf(false));
            a1(hashMap);
            LocalLog.a("StorageMonitorService", "sdTransferGuide: ignore. sdfree = " + StorageUtils.b(this.H) + ", datafree = " + StorageUtils.b(d10));
            return;
        }
        this.f12048q = false;
    }

    private void F0() {
        AlertDialog alertDialog = this.f12044m;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.f12044m.cancel();
        }
        this.f12036e = false;
        this.f12034c.sendBroadcastAsUser(this.C, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        L();
    }

    private void G0() {
        if (!x0()) {
            C0(1800000L);
        }
        this.f12036e = true;
        this.f12034c.sendBroadcastAsUser(this.B, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    private void J(boolean z10) {
        int i10 = this.f12050s;
        if (i10 != 2 && i10 != 1) {
            LocalLog.a("StorageMonitorService", "alertDialogData: dataLevel=" + this.f12050s);
            return;
        }
        if (m0()) {
            this.f12038g = true;
            LocalLog.a("StorageMonitorService", "alertDialogData: isScreenLandScape.");
            return;
        }
        AlertDialog alertDialog = this.f12046o;
        if (alertDialog != null && alertDialog.isShowing()) {
            B0(1800000L);
            LocalLog.a("StorageMonitorService", "alertDialogData: dialogTaskFinishData is showing.");
            return;
        }
        J0(99, z10);
        V0(300L, z10);
        if (P && this.f12037f) {
            H0(111, true);
        } else {
            H0(110, true);
        }
        this.f12038g = false;
    }

    private void J0(int i10, boolean z10) {
        if (z10 && this.f12040i) {
            Intent intent = new Intent("com.oplus.battery.StorageMonitorDialogService");
            intent.setPackage(this.f12034c.getPackageName());
            intent.putExtra("msg", i10);
            this.f12034c.startService(intent);
        }
    }

    private void K() {
        ((AlarmManager) this.f12034c.getSystemService("alarm")).cancel(this.D);
    }

    private void L() {
        ((AlarmManager) this.f12034c.getSystemService("alarm")).cancel(this.E);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M() {
        Y0("cancel");
        LocalLog.a("StorageMonitorService", "DialogData: cancel");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M0(int i10, long j10, boolean z10) {
        this.f12051t = this.f12034c.getResources().getConfiguration().uiMode & 48;
        Settings.System.putIntForUser(this.f12034c.getContentResolver(), "batteryuimode", this.f12051t, -2);
        int i11 = i10 == 2 ? R.string.oplus_data_full_title_new : R.string.oplus_data_low_title_new;
        View c02 = c0(j10, i10, z10);
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(a0(this.f12034c), f6.f.k(this.f12034c));
        cOUIAlertDialogBuilder.h0(i11);
        cOUIAlertDialogBuilder.v(c02);
        if (y5.b.D()) {
            cOUIAlertDialogBuilder.c0(R.string.high_performance_dialog_never_remind, new v());
        }
        cOUIAlertDialogBuilder.e0(R.string.clean_button_text, new w());
        cOUIAlertDialogBuilder.a0(R.string.got_it, new a());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f12042k = a10;
        a10.setOnShowListener(new b());
        this.f12042k.setCancelable(false);
        Window window = this.f12042k.getWindow();
        window.setType(2038);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        if (y5.b.D()) {
            if (this.f12034c.getSharedPreferences("sp_storage_do_not_show", 0).getBoolean("sp_storage_do_not_show", true)) {
                this.f12042k.show();
            }
        } else {
            this.f12042k.show();
        }
        f6.f.v3(this.f12042k.findViewById(R.id.customPanel), 1.0f);
        f6.f.v3(this.f12042k.findViewById(R.id.buttonPanel), 0.0f);
        LocalLog.a("StorageMonitorService", "alertDialogData: show DialogData.");
    }

    private void N(boolean z10) {
        J0(99, z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N0(int i10, long j10, boolean z10) {
        this.f12051t = this.f12034c.getResources().getConfiguration().uiMode & 48;
        Settings.System.putIntForUser(this.f12034c.getContentResolver(), "batteryuimode", this.f12051t, -2);
        int i11 = i10 == 2 ? R.string.oplus_data_full_title_new : R.string.oplus_data_low_title_new;
        View c02 = c0(j10, i10, z10);
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(a0(this.f12034c), f6.f.k(this.f12034c));
        String[] stringArray = this.f12034c.getResources().getStringArray(R.array.oplus_data_not_enough_array);
        cOUIAlertDialogBuilder.h0(i11).v(c02);
        if (y5.b.D()) {
            h0(c02, cOUIAlertDialogBuilder, stringArray);
        } else {
            AlertDialog a10 = cOUIAlertDialogBuilder.p(stringArray[0], new t()).d0(stringArray[1], new s()).k(stringArray[2], new r()).d(false).a();
            this.f12043l = a10;
            a10.setOnShowListener(new u());
        }
        Window window = this.f12043l.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        if (y5.b.D()) {
            if (this.f12034c.getSharedPreferences("sp_storage_do_not_show", 0).getBoolean("sp_storage_do_not_show", true)) {
                this.f12043l.show();
            }
        } else {
            this.f12043l.show();
        }
        LocalLog.a("StorageMonitorService", "alertDialogData: show DialogDataMultiKey.");
    }

    private void O(boolean z10) {
        J0(99, z10);
        AlertDialog alertDialog = this.f12044m;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.f12044m.cancel();
            return;
        }
        AlertDialog alertDialog2 = this.f12045n;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.f12045n.cancel();
            return;
        }
        AlertDialog alertDialog3 = this.f12046o;
        if (alertDialog3 != null && alertDialog3.isShowing()) {
            this.f12046o.cancel();
            return;
        }
        AlertDialog alertDialog4 = this.f12047p;
        if (alertDialog4 == null || !alertDialog4.isShowing()) {
            return;
        }
        this.f12047p.cancel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void O0() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(a0(this.f12034c), f6.f.k(this.f12034c));
        cOUIAlertDialogBuilder.h0(R.string.oplus_sd_not_enough_info);
        cOUIAlertDialogBuilder.e0(R.string.clean_button_text, new g());
        if (y5.b.D()) {
            cOUIAlertDialogBuilder.c0(R.string.high_performance_dialog_never_remind, new h());
        }
        cOUIAlertDialogBuilder.a0(R.string.got_it, new i());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f12044m = a10;
        a10.setCancelable(false);
        Window window = this.f12044m.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.f12044m.show();
        TextView textView = (TextView) this.f12044m.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setGravity(17);
        }
        LocalLog.a("StorageMonitorService", "oplusAlertDialogSd: show DialogSd.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void P0() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(a0(this.f12034c), f6.f.k(this.f12034c));
        cOUIAlertDialogBuilder.h0(R.string.sd_transfer_guide);
        cOUIAlertDialogBuilder.e0(R.string.go_to_setting, new m());
        cOUIAlertDialogBuilder.a0(R.string.button_cancel, new n());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f12045n = a10;
        a10.setCancelable(false);
        Window window = this.f12045n.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.f12045n.show();
        TextView textView = (TextView) this.f12045n.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setGravity(17);
        }
        LocalLog.a("StorageMonitorService", "sdTransferGuide: show DialogSdTransfer.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean Q() {
        boolean z10;
        VolumeInfo volumeInfo = this.J;
        boolean z11 = false;
        if (volumeInfo == null) {
            LocalLog.a("StorageMonitorService", "checkSdStorage: VolumeExternalSd is null!");
            return false;
        }
        File path = volumeInfo.getPath();
        if (path == null) {
            LocalLog.a("StorageMonitorService", "checkSdStorage: path is null");
            return false;
        }
        long totalSpace = path.getTotalSpace();
        this.f12052u = totalSpace;
        if (totalSpace <= 0) {
            LocalLog.a("StorageMonitorService", "checkSdStorage: abnormal. totalBytes=" + totalSpace);
            LocalLog.a("StorageMonitorService", "checkSdStorage: abnormal. freeSpace=" + path.getFreeSpace());
            return false;
        }
        if (!l0()) {
            A0(StatIdManager.EXPIRE_TIME_MS);
            LocalLog.a("StorageMonitorService", "checkSdStorage: not Normal Boot");
            return false;
        }
        if (!j0()) {
            A0(StatIdManager.EXPIRE_TIME_MS);
            LocalLog.a("StorageMonitorService", "checkSdStorage: DEVICE_PROVISIONED is not set!!!!!!");
            return false;
        }
        if (P) {
            z10 = false;
        } else {
            P = true;
            LocalLog.a("StorageMonitorService", "Sd Mounted!");
            z10 = true;
        }
        long freeSpace = path.getFreeSpace();
        this.H = freeSpace;
        if (freeSpace < this.I) {
            if (!this.f12036e) {
                LocalLog.a("StorageMonitorService", "freeExternalSd = " + StorageUtils.b(this.H));
                LocalLog.a("StorageMonitorService", "checkSD: Running low on SDCARD. Sending notification");
                G0();
            }
        } else if (this.f12036e) {
            LocalLog.a("StorageMonitorService", "freeExternalSd = " + StorageUtils.b(this.H));
            LocalLog.a("StorageMonitorService", "checkSD: SDCARD available. Cancelling notification");
            F0();
        }
        if (this.H >= 1073741824) {
            if (!this.f12037f) {
                this.f12037f = true;
                LocalLog.a("StorageMonitorService", "oplusCheckSD: SDCARD Sufficient.");
                z11 = true;
            }
            if (!z11 || z10) {
                p0();
            }
            A0(StatIdManager.EXPIRE_TIME_MS);
            return true;
        }
        if (this.f12037f) {
            this.f12037f = false;
            LocalLog.a("StorageMonitorService", "oplusCheckSD: SDCARD not Sufficient.");
            z11 = true;
        }
        if (!z11) {
        }
        p0();
        A0(StatIdManager.EXPIRE_TIME_MS);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Q0() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(a0(this.f12034c), f6.f.k(this.f12034c));
        cOUIAlertDialogBuilder.h0(R.string.oplus_task_finish_phone_low_info);
        cOUIAlertDialogBuilder.e0(R.string.clean_button_text, new p());
        cOUIAlertDialogBuilder.a0(R.string.got_it, new q());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f12046o = a10;
        a10.setCancelable(false);
        Window window = this.f12046o.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.f12046o.show();
        TextView textView = (TextView) this.f12046o.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setGravity(17);
        }
        LocalLog.a("StorageMonitorService", "oplusAlertDialogTaskTerminationData: show...");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void R0() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(a0(this.f12034c), f6.f.k(this.f12034c));
        cOUIAlertDialogBuilder.h0(R.string.oplus_task_finish_sd_low_info);
        cOUIAlertDialogBuilder.e0(R.string.clean_button_text, new j());
        cOUIAlertDialogBuilder.a0(R.string.got_it, new l());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.f12047p = a10;
        a10.setCancelable(false);
        Window window = this.f12047p.getWindow();
        window.setType(2003);
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f6.f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.f12047p.show();
        TextView textView = (TextView) this.f12047p.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setGravity(17);
        }
        LocalLog.a("StorageMonitorService", "oplusAlertDialogTaskTerminationSd: show...");
    }

    private void S0() {
        Intent intent = new Intent();
        intent.setPackage("com.coloros.phonemanager");
        intent.setAction("com.oppo.cleandroid.ui.ClearMainActivity");
        if (this.f12034c.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            this.f12039h = false;
        } else {
            this.f12039h = true;
        }
        LocalLog.a("StorageMonitorService", "mDisableCleanFunc = " + this.f12039h);
    }

    private boolean T() {
        return Settings.System.getIntForUser(this.f12034c.getContentResolver(), "DialogData", 0, 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T0() {
        String str;
        VolumeInfo volumeInfo = this.J;
        if (volumeInfo == null || (str = volumeInfo.fsType) == null) {
            return;
        }
        SystemProperties.set("sys.extsd.file.system", str);
    }

    public static void U0(boolean z10) {
        P = z10;
    }

    private void V0(long j10, boolean z10) {
        if (z10) {
            try {
                Thread.sleep(j10);
            } catch (InterruptedException unused) {
            }
        }
    }

    private void W0(Intent intent) {
        String stringExtra = intent.getStringExtra("package");
        String stringExtra2 = intent.getStringExtra("space");
        if (stringExtra != null && stringExtra2 != null) {
            if ("Phone".equals(stringExtra2)) {
                if (this.f12053v.contains(stringExtra)) {
                    LocalLog.a("StorageMonitorService", "TASK TERMINATION. Phone. pkg(" + stringExtra + ") has showed TaskTermData before.");
                    return;
                }
                long d10 = StorageUtils.d();
                if (d10 >= 0 && d10 <= this.F + 314572800) {
                    this.f12053v.add(stringExtra);
                    y0();
                    return;
                }
                LocalLog.a("StorageMonitorService", "TASK TERMINATION. freeDataSpace=" + StorageUtils.b(d10) + ". ignore.");
                return;
            }
            if ("sd".equals(stringExtra2)) {
                if (this.f12054w.contains(stringExtra)) {
                    LocalLog.a("StorageMonitorService", "TASK TERMINATION. sd. pkg(" + stringExtra + ") has showed TaskTermSd before.");
                    return;
                }
                long Z = Z();
                if (Z >= 0 && Z <= this.I) {
                    this.f12054w.add(stringExtra);
                    z0();
                    return;
                }
                LocalLog.a("StorageMonitorService", "TASK TERMINATION. freeSdSpace=" + StorageUtils.b(Z) + ". ignore.");
                return;
            }
            return;
        }
        LocalLog.a("StorageMonitorService", "TASK TERMINATION. pkg=" + stringExtra + ", space=" + stringExtra2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"WrongConstant"})
    public Intent X(String str) {
        Intent intent = new Intent("oplus.intent.action.CLEAR_MAIN_ACTIVITY");
        intent.putExtra("enter_from", str);
        intent.putExtra("DEEP_CLEAN", 2);
        intent.addFlags(603979776);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void X0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("turnTo", str);
        b0(hashMap);
        if (this.f12053v.size() > 0) {
            hashMap.put(DeviceDomainManager.ARG_PKG, this.f12053v.get(r3.size() - 1));
        }
        LocalLog.a("StorageMonitorService", "uploadDataTaskTermitationDialogTurnTo: eventMap=" + hashMap);
        UploadDataUtil.S0(this.f12034c).W0("data_task_termination_dial", hashMap, false);
    }

    public static synchronized StorageMonitorService Y(Context context) {
        StorageMonitorService storageMonitorService;
        synchronized (StorageMonitorService.class) {
            if (O == null) {
                O = new StorageMonitorService(context);
            }
            storageMonitorService = O;
        }
        return storageMonitorService;
    }

    private void Y0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("turnTo", str);
        b0(hashMap);
        LocalLog.a("StorageMonitorService", "uploadDcsDataDialog: eventMap=" + hashMap);
        if (this.f12050s == 2) {
            UploadDataUtil.S0(this.f12034c).W0("data_full_dial_turn_to", hashMap, false);
        } else {
            UploadDataUtil.S0(this.f12034c).W0("data_low_dial_turn_to", hashMap, false);
        }
    }

    private long Z() {
        File path;
        VolumeInfo volumeInfo = this.J;
        if (volumeInfo == null || volumeInfo.path == null || (path = volumeInfo.getPath()) == null || path.getTotalSpace() <= 0) {
            return -1L;
        }
        long freeSpace = path.getFreeSpace();
        LocalLog.a("StorageMonitorService", "getSdFreeSpace: freeExternalSd = " + StorageUtils.b(freeSpace));
        return freeSpace;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Z0(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("turnTo", str);
        b0(hashMap);
        LocalLog.a("StorageMonitorService", "uploadSdGuideDialogTurnTo: eventMap=" + hashMap);
        UploadDataUtil.S0(this.f12034c).W0("sd_guide_dialog_turn_to", hashMap, false);
    }

    private Context a0(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.Theme_Demo);
        COUIThemeOverlay.i().b(contextThemeWrapper);
        return contextThemeWrapper;
    }

    private void a1(Map<String, String> map) {
        UploadDataUtil.S0(this.f12034c).W0("sdcard_insert", map, false);
    }

    private void b0(Map<String, String> map) {
        if (map == null) {
            return;
        }
        long d10 = StorageUtils.d();
        map.put("data_free_space", String.valueOf(d10));
        map.put("data_free_space_kind", StorageUtils.b(d10));
        map.put("data_show_total_space", String.valueOf(StorageUtils.c()));
        map.put("data_show_total_space_kind", StorageUtils.b(StorageUtils.c()));
        if (this.J != null) {
            map.put("sd_total_space", String.valueOf(this.f12052u));
            map.put("sd_total_space_kind", StorageUtils.b(this.f12052u));
            map.put("sd_free_space", String.valueOf(this.H));
            map.put("sd_free_space_kind", StorageUtils.b(this.H));
            map.put("sd_mounted", String.valueOf(true));
            return;
        }
        map.put("sd_mounted", String.valueOf(false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b1(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("turnTo", str);
        b0(hashMap);
        UploadDataUtil.S0(this.f12034c).W0("sdcard_low_dial_turn_to", hashMap, false);
    }

    private View c0(long j10, int i10, boolean z10) {
        View inflate;
        long j11 = (this.F - j10) + 314572800;
        if (k0()) {
            inflate = View.inflate(COUIContextUtil.c(this.f12034c), R.layout.oplus_storage_data_monitor_dialog_view, null);
        } else {
            inflate = View.inflate(COUIContextUtil.c(this.f12034c), R.layout.oplus_storage_data_monitor_dialog_view_no_curve, null);
        }
        TextView textView = (TextView) inflate.findViewById(R.id.oplus_data_low_full_message_4_id);
        textView.setText(String.format(this.f12034c.getResources().getString(R.string.oplus_data_low_full_message_4), StorageUtils.a(j11, this.f12034c)));
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView textView2 = (TextView) inflate.findViewById(R.id.oplus_data_low_message_5_id);
        textView2.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (i10 != 2) {
            textView2.setText(this.f12034c.getResources().getString(R.string.oplus_data_low_message_5));
        } else if (z10) {
            textView2.setText(this.f12034c.getResources().getString(R.string.oplus_data_full_message_with_sd));
        } else {
            textView2.setText(this.f12034c.getResources().getString(R.string.oplus_data_full_message_no_sd));
        }
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c1(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("turnTo", str);
        b0(hashMap);
        if (this.f12054w.size() > 0) {
            hashMap.put(DeviceDomainManager.ARG_PKG, this.f12054w.get(r3.size() - 1));
        }
        LocalLog.a("StorageMonitorService", "uploadSdTaskTermitationDialogTurnTo: eventMap=" + hashMap);
        UploadDataUtil.S0(this.f12034c).W0("sd_task_termination_dial", hashMap, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d0() {
        try {
            this.f12034c.startActivityAsUser(X(this.f12050s == 2 ? "StorageDialogFull" : "StorageDialogLow"), UserHandle.CURRENT);
        } catch (Throwable th) {
            LocalLog.m("StorageMonitorService", "DialogData: start FileCleanUp exception", th);
        }
        Y0("cleanUp");
        LocalLog.a("StorageMonitorService", "DialogData: start cleanup activity");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e0() {
        try {
            this.f12034c.startActivityAsUser(this.f12057z, UserHandle.CURRENT);
        } catch (Throwable th) {
            LocalLog.m("StorageMonitorService", "DialogDataMultiKey: start OneKeyMove exception", th);
        }
        Y0("aKeyMove");
        LocalLog.a("StorageMonitorService", "DialogDataMultiKey: start akeymove activity");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g0() {
        IStorageManager iStorageManager = this.K;
        if (iStorageManager == null) {
            LocalLog.a("StorageMonitorService", "initGetSDCard: MountService is null!!!");
            return false;
        }
        try {
            for (VolumeInfo volumeInfo : iStorageManager.getVolumes(0)) {
                DiskInfo disk = volumeInfo.getDisk();
                if (disk != null && disk.isSd()) {
                    this.J = volumeInfo;
                    T0();
                    LocalLog.a("StorageMonitorService", "initGetSDCard: get sdcard.");
                }
            }
        } catch (RemoteException unused) {
        }
        return this.J != null;
    }

    private void h0(View view, COUIAlertDialogBuilder cOUIAlertDialogBuilder, String[] strArr) {
        this.f12043l = cOUIAlertDialogBuilder.d(false).a();
        Button button = (Button) view.findViewById(R.id.data_low_full_button_one);
        Button button2 = (Button) view.findViewById(R.id.data_low_full_button_two);
        Button button3 = (Button) view.findViewById(R.id.data_low_full_button_three);
        Button button4 = (Button) view.findViewById(R.id.data_low_full_button_four);
        button.setText(strArr[0]);
        button.setVisibility(0);
        button.getPaint().setFakeBoldText(true);
        button.setOnClickListener(new c());
        button2.setText(R.string.high_performance_dialog_never_remind);
        button2.setVisibility(0);
        button2.setOnClickListener(new d());
        button3.setText(strArr[1]);
        button3.setVisibility(0);
        button3.setOnClickListener(new e());
        button4.setText(strArr[2]);
        button4.setVisibility(0);
        button4.setOnClickListener(new f());
    }

    private boolean i0(boolean z10) {
        AlertDialog alertDialog;
        AlertDialog alertDialog2;
        AlertDialog alertDialog3;
        AlertDialog alertDialog4;
        if (this.f12040i && z10) {
            J0(100, true);
            LocalLog.a("StorageMonitorService", "start check in isAnyDialogShowing");
            V0(300L, true);
            return T() || q0() || ((alertDialog = this.f12044m) != null && alertDialog.isShowing()) || (((alertDialog2 = this.f12045n) != null && alertDialog2.isShowing()) || (((alertDialog3 = this.f12046o) != null && alertDialog3.isShowing()) || ((alertDialog4 = this.f12047p) != null && alertDialog4.isShowing())));
        }
        LocalLog.a("StorageMonitorService", "background not check isAnyDialogShowing");
        return false;
    }

    private boolean j0() {
        return Settings.Global.getInt(this.f12034c.getContentResolver(), "device_provisioned", 0) != 0;
    }

    private boolean k0() {
        return Settings.Global.getInt(this.f12034c.getContentResolver(), "data_space_monitor_curve_switch", 1) == 1;
    }

    private boolean l0() {
        String str = SystemProperties.get("vold.decrypt", "trigger_restart_framework");
        if ("trigger_restart_framework".equals(str)) {
            return true;
        }
        LocalLog.a("StorageMonitorService", "cryptState = " + str);
        return false;
    }

    private boolean m0() {
        return this.f12034c.getResources().getConfiguration().orientation == 2;
    }

    private boolean n0(String str) {
        if (this.f12055x == null) {
            this.f12055x = this.L.e("sd_ever_mounted.xml");
        }
        return this.f12055x.contains(str);
    }

    private boolean o0() {
        boolean z10;
        Iterator<ActivityManager.RunningAppProcessInfo> it = GuardElfContext.e().a().getRunningAppProcesses().iterator();
        while (true) {
            if (!it.hasNext()) {
                z10 = false;
                break;
            }
            if ("com.oplus.battery:ui".equals(it.next().processName)) {
                z10 = true;
                break;
            }
        }
        LocalLog.a("StorageMonitorService", "ui process exist = " + z10);
        return z10;
    }

    private void p0() {
        boolean z10;
        boolean o02 = o0();
        if (this.f12040i && o02) {
            J0(100, true);
            LocalLog.a("StorageMonitorService", "start check in maybeReshow");
            V0(300L, true);
            if (T() || q0()) {
                J0(99, true);
                V0(300L, true);
                LocalLog.a("StorageMonitorService", "maybeReshowDialogData: cancel Dialog.");
                z10 = true;
            } else {
                z10 = false;
            }
            if (z10) {
                J(true);
                return;
            }
            return;
        }
        LocalLog.a("StorageMonitorService", "background not check maybe reshow");
    }

    private boolean q0() {
        return Settings.System.getIntForUser(this.f12034c.getContentResolver(), "DialogDataMultiKey", 0, 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r0() {
        SharedPreferences.Editor edit = this.f12034c.getSharedPreferences("sp_storage_do_not_show", 0).edit();
        edit.putBoolean("sp_storage_do_not_show", false);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s0() {
        try {
            this.f12034c.startActivityAsUser(X(this.f12050s == 2 ? "StorageDialogFull" : "StorageDialogLow"), UserHandle.CURRENT);
        } catch (Throwable th) {
            LocalLog.m("StorageMonitorService", "DialogDataMultiKey: start FileCleanUp exception", th);
        }
        Y0("cleanUp");
        LocalLog.a("StorageMonitorService", "DialogDataMultiKey: start cleanup activity");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u0(int i10, long j10) {
        if (j10 < 0) {
            j10 = StorageUtils.d();
        }
        this.f12049r = j10;
        StorageUtils.g(this.f12034c);
        this.F = StorageUtils.f();
        this.G = StorageUtils.e();
        LocalLog.a("StorageMonitorService", "onDataLevelChanged. dataLevel=" + i10 + ", oldLevle=" + this.f12050s + ", dataFree=" + StorageUtils.b(j10));
        if (SystemProperties.getBoolean("sys.data.level.boot", false)) {
            SystemProperties.set("sys.data.level.boot", String.valueOf(false));
        }
        int i11 = this.f12050s;
        if (i10 == i11 || i10 > 2 || i10 < 0) {
            return;
        }
        this.f12050s = i10;
        boolean o02 = o0();
        if (i10 == 2) {
            Q();
            J(o02);
            return;
        }
        if (i10 == 0) {
            this.f12053v.clear();
            this.f12054w.clear();
            N(true);
            K();
            Settings.System.putIntForUser(this.f12034c.getContentResolver(), "message", 0, 0);
            return;
        }
        if (i10 == 1) {
            if (i11 == 0) {
                Q();
                J(o02);
            } else if (i11 == 2) {
                p0();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w0() {
        Y0("cancel");
        LocalLog.a("StorageMonitorService", "DialogDataMultiKey: cancel");
    }

    private boolean x0() {
        AlertDialog alertDialog = this.f12047p;
        if (alertDialog != null && alertDialog.isShowing()) {
            LocalLog.a("StorageMonitorService", "oplusAlertDialogSd: DialogTaskFinishSd is showing.");
            return false;
        }
        AlertDialog alertDialog2 = this.f12044m;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.f12044m.cancel();
            LocalLog.d("StorageMonitorService", "oplusAlertDialogSd: cacel old DialogSd");
        }
        H0(109, true);
        return true;
    }

    private void y0() {
        AlertDialog alertDialog = this.f12046o;
        if (alertDialog != null && alertDialog.isShowing()) {
            LocalLog.a("StorageMonitorService", "oplusAlertDialogTaskTerminationData: is showing.");
            return;
        }
        boolean o02 = o0();
        J0(99, o02);
        V0(300L, o02);
        H0(107, true);
    }

    private void z0() {
        AlertDialog alertDialog = this.f12047p;
        if (alertDialog != null && alertDialog.isShowing()) {
            LocalLog.a("StorageMonitorService", "oplusAlertDialogTaskTerminationSd: is showing.");
            return;
        }
        AlertDialog alertDialog2 = this.f12044m;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.f12044m.cancel();
            LocalLog.a("StorageMonitorService", "oplusAlertDialogTaskTerminationSd: cancel DialogSd");
        }
        H0(106, true);
    }

    public void H0(int i10, boolean z10) {
        LocalLog.a("StorageMonitorService", "msg " + i10 + ", flag " + z10 + ", user " + this.f12040i);
        if (UserHandle.myUserId() != ActivityManager.getCurrentUser()) {
            LocalLog.a("StorageMonitorService", "background user don't show dialog");
            return;
        }
        boolean z11 = false;
        if (z10) {
            Settings.System.putIntForUser(this.f12034c.getContentResolver(), "message", i10, 0);
        }
        if (i10 != 110 && i10 != 111) {
            Message obtainMessage = this.N.obtainMessage();
            obtainMessage.what = i10;
            this.N.sendMessage(obtainMessage);
            return;
        }
        Intent intent = new Intent("com.oplus.battery.StorageMonitorDialogService");
        intent.setPackage(this.f12034c.getPackageName());
        intent.putExtra("msg", i10);
        intent.putExtra("dataLevel", this.f12050s);
        intent.putExtra("dataFree", this.f12049r);
        if (P && this.f12037f) {
            z11 = true;
        }
        intent.putExtra("withSd", z11);
        this.f12034c.startService(intent);
    }

    public void I0(int i10, int i11, long j10, boolean z10) {
        Message obtainMessage = this.N.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putInt("dataLevel", i11);
        bundle.putLong("dataFree", j10);
        bundle.putBoolean("withSd", z10);
        obtainMessage.what = i10;
        obtainMessage.setData(bundle);
        this.N.sendMessage(obtainMessage);
    }

    public void K0(long j10) {
        this.G = j10;
    }

    public void L0(long j10) {
        this.F = j10;
    }

    public void P() {
        AlertDialog alertDialog = this.f12042k;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.f12042k.cancel();
            return;
        }
        AlertDialog alertDialog2 = this.f12043l;
        if (alertDialog2 == null || !alertDialog2.isShowing()) {
            return;
        }
        this.f12043l.cancel();
    }

    public void R() {
        AlertDialog alertDialog = this.f12042k;
        if (alertDialog != null && alertDialog.isShowing()) {
            Settings.System.putIntForUser(this.f12034c.getContentResolver(), "DialogData", 1, 0);
        } else {
            AlertDialog alertDialog2 = this.f12043l;
            if (alertDialog2 != null && alertDialog2.isShowing()) {
                Settings.System.putIntForUser(this.f12034c.getContentResolver(), "DialogDataMultiKey", 1, 0);
            } else {
                Settings.System.putIntForUser(this.f12034c.getContentResolver(), "DialogData", 0, 0);
                Settings.System.putIntForUser(this.f12034c.getContentResolver(), "DialogDataMultiKey", 0, 0);
            }
        }
        Intent intent = new Intent("oplus.intent.action.CHECK_DIALOG_OVER");
        intent.setPackage(this.f12034c.getPackageName());
        this.f12034c.sendBroadcast(intent);
    }

    public void S() {
        this.f12035d.sendEmptyMessage(104);
        LocalLog.a("StorageMonitorService", "clearSdEverMount");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void U(Intent intent) {
        char c10;
        String action = intent.getAction();
        LocalLog.a("StorageMonitorService", "receive " + action);
        action.hashCode();
        switch (action.hashCode()) {
            case -2105789962:
                if (action.equals("oplus.intent.action.DIALOG_DATA")) {
                    c10 = 0;
                    break;
                }
                c10 = 65535;
                break;
            case -957708272:
                if (action.equals("oplus.intent.action.TASK_TERMINATION_FOR_LOW_STORAGE")) {
                    c10 = 1;
                    break;
                }
                c10 = 65535;
                break;
            case -918390883:
                if (action.equals("oplus.intent.action.DIALOG_SD")) {
                    c10 = 2;
                    break;
                }
                c10 = 65535;
                break;
            case -19011148:
                if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                    c10 = 3;
                    break;
                }
                c10 = 65535;
                break;
            case 735264466:
                if (action.equals("android.intent.action.USER_FOREGROUND")) {
                    c10 = 4;
                    break;
                }
                c10 = 65535;
                break;
            case 1581803861:
                if (action.equals("oplus.intent.action.DATA_LEVEL_CHANGE")) {
                    c10 = 5;
                    break;
                }
                c10 = 65535;
                break;
            case 1713580733:
                if (action.equals("android.intent.action.USER_BACKGROUND")) {
                    c10 = 6;
                    break;
                }
                c10 = 65535;
                break;
            default:
                c10 = 65535;
                break;
        }
        switch (c10) {
            case 0:
                J(o0());
                return;
            case 1:
                if (this.f12039h) {
                    return;
                }
                W0(intent);
                return;
            case 2:
                if (x0()) {
                    return;
                }
                C0(1800000L);
                return;
            case 3:
                LocalLog.a("StorageMonitorService", "ACTION_LOCALE_CHANGED");
                AlertDialog alertDialog = this.f12045n;
                if (alertDialog == null || !alertDialog.isShowing()) {
                    return;
                }
                this.f12045n.cancel();
                H0(108, true);
                return;
            case 4:
                this.f12040i = true;
                boolean o02 = o0();
                int intForUser = Settings.System.getIntForUser(this.f12034c.getContentResolver(), "message", -1, 0);
                LocalLog.a("StorageMonitorService", "msg " + intForUser + ", isShow " + i0(o02));
                if (intForUser == 1 && i0(o02)) {
                    Settings.System.putIntForUser(this.f12034c.getContentResolver(), "message", 0, 0);
                    O(o02);
                } else if (!i0(o02)) {
                    H0(intForUser, false);
                    Settings.System.putIntForUser(this.f12034c.getContentResolver(), "message", 0, 0);
                }
                Settings.System.putIntForUser(this.f12034c.getContentResolver(), "user_create", 0, 0);
                return;
            case 5:
                u0(intent.getIntExtra("level", 0), intent.getLongExtra("dataFree", -1L));
                return;
            case 6:
                J0(99, o0());
                this.f12040i = false;
                Settings.System.putIntForUser(this.f12034c.getContentResolver(), "user_create", 1, 0);
                return;
            default:
                return;
        }
    }

    public void V(PrintWriter printWriter) {
        printWriter.println("sdMounted: " + P);
        printWriter.println("sdTotalSpace: " + StorageUtils.b(this.f12052u));
        printWriter.println("sdFreeSpace: " + StorageUtils.b(this.H));
        printWriter.println("sdLow: " + this.f12036e);
        printWriter.println("sdSufficient: " + this.f12037f);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("sdVolumeFound: ");
        sb2.append(String.valueOf(this.J != null));
        printWriter.println(sb2.toString());
        printWriter.println("isNormalBoot: " + l0());
    }

    public void W(String str) {
        if ("Phone".equals(str)) {
            y0();
        } else if ("sd".equals(str)) {
            z0();
        }
    }

    public void f0() {
        this.L = GuardElfDataManager.d(this.f12034c);
        this.K = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        HandlerThread handlerThread = new HandlerThread("DeviceStorageMonitor" + this.f12034c.getUserId());
        handlerThread.start();
        this.f12035d = new x(handlerThread.getLooper());
        boolean z10 = SystemProperties.getBoolean("sys.data.level.boot", false);
        boolean z11 = Settings.System.getIntForUser(this.f12034c.getContentResolver(), "user_create", 0, 0) == 1;
        if (z10) {
            this.f12050s = 0;
            SystemProperties.set("sys.data.level.boot", String.valueOf(false));
            this.f12035d.sendEmptyMessage(105);
        } else if (z11) {
            Settings.System.putIntForUser(this.f12034c.getContentResolver(), "user_create", 0, 0);
            this.f12050s = 0;
            SystemProperties.set("sys.data.level.boot", String.valueOf(false));
            this.f12035d.sendEmptyMessage(105);
        } else {
            this.f12050s = SystemProperties.getInt("sys.data.free.level", 0);
        }
        S0();
        LocalLog.a("StorageMonitorService", "datalevel=" + this.f12050s + ", mDisableCleanFunc=" + this.f12039h + ", isDataLevelChgBoot=" + z10 + ", isUserCreate=" + z11 + ", dataFree=" + StorageUtils.b(this.f12049r));
        this.I = 52428800L;
        StorageManager storageManager = (StorageManager) this.f12034c.getSystemService(StorageManager.class);
        if (storageManager != null) {
            storageManager.registerListener(this.M);
        } else {
            LocalLog.a("StorageMonitorService", "storageManager is null!");
        }
        this.f12035d.sendEmptyMessage(103);
    }

    public void t0(Configuration configuration) {
        int i10 = configuration.orientation;
        int i11 = configuration.uiMode & 48;
        this.f12051t = Settings.System.getIntForUser(this.f12034c.getContentResolver(), "batteryuimode", i11, -2);
        LocalLog.a("StorageMonitorService", " CONFIGURATION CHANGED: orientation=" + i10 + ", Need Display Exit Landscape=" + this.f12038g + ", curui = " + i11 + ", precur = " + this.f12051t);
        boolean o02 = o0();
        if (i11 != this.f12051t) {
            N(o02);
            J(o02);
        }
        if (1 == i10) {
            if (this.f12038g) {
                J(o02);
                return;
            }
            return;
        }
        if (2 == i10) {
            if (this.f12040i && o02) {
                J0(100, true);
                LocalLog.a("StorageMonitorService", "start check in configuration");
                V0(300L, true);
                if (T() || q0()) {
                    J0(99, true);
                    LocalLog.a("StorageMonitorService", "cancel in configuration");
                    this.f12038g = true;
                    return;
                }
                return;
            }
            LocalLog.a("StorageMonitorService", "background not check configuration");
        }
    }

    public void v0() {
        synchronized (this.f12032a) {
            this.f12053v.clear();
            this.f12054w.clear();
            HashMap hashMap = new HashMap();
            b0(hashMap);
            UploadDataUtil.S0(this.f12034c).W0("storage_status_statistics", hashMap, false);
        }
    }
}
