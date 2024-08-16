package r6;

import android.app.ActivityManager;
import android.app.OplusActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import com.oplus.deepthinker.sdk.app.userprofile.labels.utils.InnerUtils;
import com.oplus.statistics.DataTypeConstants;
import d6.ConfigUpdateUtil;
import f6.CommonUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.PatternSyntaxException;
import t8.PowerCpuUtil;
import x5.UploadDataUtil;
import y5.AppFeature;
import z5.GuardElfDataManager;
import z5.LocalFileUtil;

/* compiled from: ProcessCpuManager.java */
/* renamed from: r6.a, reason: use source file name */
/* loaded from: classes.dex */
public class ProcessCpuManager {

    /* renamed from: k, reason: collision with root package name */
    private static ProcessCpuManager f17533k;

    /* renamed from: a, reason: collision with root package name */
    private final HandlerThread f17534a;

    /* renamed from: b, reason: collision with root package name */
    private a f17535b;

    /* renamed from: c, reason: collision with root package name */
    private Context f17536c;

    /* renamed from: d, reason: collision with root package name */
    private AtomicBoolean f17537d;

    /* renamed from: e, reason: collision with root package name */
    private Object f17538e;

    /* renamed from: f, reason: collision with root package name */
    private AtomicBoolean f17539f;

    /* renamed from: g, reason: collision with root package name */
    private long f17540g;

    /* renamed from: h, reason: collision with root package name */
    private GuardElfDataManager f17541h;

    /* renamed from: i, reason: collision with root package name */
    private UploadDataUtil f17542i;

    /* renamed from: j, reason: collision with root package name */
    private ConfigUpdateUtil f17543j;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ProcessCpuManager.java */
    /* renamed from: r6.a$a */
    /* loaded from: classes.dex */
    public class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            long j10;
            float f10;
            long j11;
            float f11;
            super.handleMessage(message);
            int i10 = message.what;
            if (i10 != 3001) {
                float f12 = 1.0f;
                switch (i10) {
                    case DataTypeConstants.USER_ACTION /* 1001 */:
                        LocalLog.a("ProcessCpuManager", "MSG_CPU_TRACK_START update!");
                        ProcessCpuManager.this.f17537d.set(true);
                        removeMessages(3001);
                        long uptimeMillis = SystemClock.uptimeMillis();
                        synchronized (ProcessCpuManager.this.f17538e) {
                            ProcessCpuManager.this.f17541h.j();
                            try {
                                new OplusActivityManager().updateCpuTracker(0L);
                            } catch (Exception e10) {
                                e10.printStackTrace();
                            }
                            Message obtainMessage = obtainMessage(DataTypeConstants.APP_LOG);
                            obtainMessage.obj = String.valueOf(uptimeMillis);
                            obtainMessage.arg1 = message.arg1;
                            sendMessageDelayed(obtainMessage, ProcessCpuManager.this.f17543j.o());
                        }
                        return;
                    case DataTypeConstants.APP_LOG /* 1002 */:
                        LocalLog.a("ProcessCpuManager", "MSG_SHORT_CPU_TRACK update!");
                        boolean z10 = message.arg1 == 1;
                        try {
                            j10 = Long.parseLong((String) message.obj);
                        } catch (NumberFormatException unused) {
                            j10 = 0;
                        }
                        synchronized (ProcessCpuManager.this.f17538e) {
                            try {
                                f12 = new OplusActivityManager().updateCpuTracker(j10);
                            } catch (Exception e11) {
                                e11.printStackTrace();
                            }
                            f10 = f12;
                        }
                        if (j10 > 0 ? ProcessCpuManager.this.g(j10, z10, 0, f10) : false) {
                            ProcessCpuManager.this.f17537d.set(false);
                            return;
                        }
                        synchronized (ProcessCpuManager.this.f17538e) {
                            ProcessCpuManager.this.f17541h.j();
                            long uptimeMillis2 = SystemClock.uptimeMillis();
                            Message obtainMessage2 = obtainMessage(DataTypeConstants.PAGE_VISIT);
                            obtainMessage2.obj = String.valueOf(uptimeMillis2);
                            obtainMessage2.arg1 = 1;
                            sendMessageDelayed(obtainMessage2, ProcessCpuManager.this.f17543j.p());
                        }
                        return;
                    case DataTypeConstants.PAGE_VISIT /* 1003 */:
                        LocalLog.a("ProcessCpuManager", "MSG_LONG_CPU_TRACK update!");
                        boolean z11 = message.arg1 == 1;
                        try {
                            j11 = Long.parseLong((String) message.obj);
                        } catch (NumberFormatException unused2) {
                            j11 = 0;
                        }
                        synchronized (ProcessCpuManager.this.f17538e) {
                            try {
                                f12 = new OplusActivityManager().updateCpuTracker(j11);
                            } catch (Exception e12) {
                                e12.printStackTrace();
                            }
                            f11 = f12;
                        }
                        if (j11 > 0) {
                            ProcessCpuManager.this.g(j11, z11, 1, f11);
                        }
                        ProcessCpuManager.this.f17537d.set(false);
                        return;
                    default:
                        return;
                }
            }
            synchronized (ProcessCpuManager.this.f17538e) {
                ProcessCpuManager.this.p(new ArrayList());
            }
        }
    }

    private ProcessCpuManager(Context context) {
        HandlerThread handlerThread = new HandlerThread("GuardElfCpu", 10);
        this.f17534a = handlerThread;
        this.f17535b = null;
        this.f17537d = new AtomicBoolean(false);
        this.f17538e = null;
        this.f17539f = new AtomicBoolean(false);
        this.f17540g = 0L;
        this.f17542i = null;
        this.f17536c = context;
        this.f17540g = SystemClock.uptimeMillis();
        handlerThread.start();
        this.f17535b = new a(handlerThread.getLooper());
        this.f17538e = new Object();
        this.f17541h = GuardElfDataManager.d(context);
        this.f17543j = ConfigUpdateUtil.n(context);
        this.f17542i = UploadDataUtil.S0(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0396 A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x03a7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:157:0x03e9  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x03fa A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0462 A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:167:0x04a9  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x055a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0564  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0567 A[Catch: all -> 0x074e, TRY_LEAVE, TryCatch #7 {all -> 0x074e, blocks: (B:128:0x06e7, B:180:0x04eb, B:187:0x0567, B:189:0x05f4, B:192:0x05fd, B:194:0x0611, B:195:0x0635, B:197:0x0640, B:200:0x066c, B:202:0x0672, B:203:0x06c9, B:208:0x069e, B:209:0x06cd, B:213:0x06db, B:216:0x061b, B:279:0x074c, B:222:0x0503, B:225:0x0535, B:233:0x0555, B:262:0x06fc, B:264:0x070c, B:265:0x0719, B:267:0x071f, B:268:0x073d, B:271:0x073f, B:272:0x0747), top: B:13:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0640 A[Catch: all -> 0x074e, TryCatch #7 {all -> 0x074e, blocks: (B:128:0x06e7, B:180:0x04eb, B:187:0x0567, B:189:0x05f4, B:192:0x05fd, B:194:0x0611, B:195:0x0635, B:197:0x0640, B:200:0x066c, B:202:0x0672, B:203:0x06c9, B:208:0x069e, B:209:0x06cd, B:213:0x06db, B:216:0x061b, B:279:0x074c, B:222:0x0503, B:225:0x0535, B:233:0x0555, B:262:0x06fc, B:264:0x070c, B:265:0x0719, B:267:0x071f, B:268:0x073d, B:271:0x073f, B:272:0x0747), top: B:13:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:199:0x066a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:211:0x06d6  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0665  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0539  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x045a  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0231 A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x023e A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x024c A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0269 A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0238 A[Catch: all -> 0x074a, TryCatch #4 {all -> 0x074a, blocks: (B:15:0x0025, B:18:0x0035, B:20:0x003f, B:21:0x004e, B:23:0x0056, B:26:0x005d, B:28:0x0086, B:29:0x00a2, B:31:0x00a4, B:35:0x00b7, B:37:0x011a, B:39:0x011d, B:40:0x013e, B:42:0x0144, B:46:0x0167, B:49:0x0172, B:53:0x017c, B:58:0x0183, B:60:0x0227, B:62:0x0231, B:64:0x023e, B:67:0x0246, B:69:0x024c, B:71:0x0252, B:72:0x0269, B:74:0x026f, B:76:0x0275, B:78:0x02c7, B:79:0x0296, B:81:0x02a2, B:83:0x02a8, B:84:0x02ab, B:86:0x02b1, B:87:0x02bb, B:91:0x0238, B:99:0x01b4, B:97:0x01e0, B:94:0x0206, B:118:0x02da, B:120:0x02e0, B:122:0x02f6, B:124:0x02fc, B:129:0x031e, B:131:0x0326, B:135:0x0334, B:138:0x0348, B:140:0x0352, B:145:0x0384, B:147:0x0396, B:152:0x03a9, B:154:0x03b3, B:155:0x03de, B:158:0x03f4, B:160:0x03fa, B:162:0x0462, B:165:0x04a1, B:172:0x04b4, B:176:0x04c3, B:178:0x04cb, B:236:0x046c, B:239:0x0476, B:242:0x0480, B:245:0x048a, B:248:0x0494, B:275:0x0048), top: B:14:0x0025 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean g(long j10, boolean z10, int i10, float f10) {
        ArrayList<String> arrayList;
        boolean z11;
        boolean contains;
        boolean X;
        ArrayList<String> arrayList2;
        boolean contains2;
        ArrayList arrayList3;
        boolean z12;
        ArrayList<String> arrayList4;
        ArrayMap arrayMap;
        boolean contains3;
        ArrayList<String> arrayList5;
        boolean z13;
        boolean z14;
        int i11;
        boolean z15;
        String str;
        boolean z16;
        String str2;
        ArrayList<String> arrayList6;
        int i12;
        int i13;
        boolean z17;
        boolean z18;
        boolean z19;
        DecimalFormat decimalFormat;
        Object obj;
        ArrayList<String> arrayList7;
        boolean z20;
        String str3;
        boolean z21;
        boolean z22;
        ArrayList arrayList8;
        Class<?> cls;
        List<String> list;
        boolean z23;
        List list2;
        int i14;
        ArrayList<String> arrayList9;
        int i15;
        String str4;
        float f11;
        int indexOf;
        String l10;
        ArrayList arrayList10;
        String[] split;
        List arrayList11 = new ArrayList();
        try {
            arrayList11 = new OplusActivityManager().getCpuWorkingStats();
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        if (arrayList11 == null) {
            return i10 == 0;
        }
        Object obj2 = this.f17538e;
        synchronized (obj2) {
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                DecimalFormat decimalFormat2 = new DecimalFormat("######0.0");
                int size = arrayList11.size();
                boolean z24 = i10 != 0;
                long uptimeMillis = SystemClock.uptimeMillis() - j10;
                long j11 = 0;
                if (i10 == 0) {
                    j11 = this.f17543j.o();
                } else if (i10 == 1) {
                    j11 = this.f17543j.p();
                }
                if (uptimeMillis >= j11 / 2 && uptimeMillis <= j11 * 2) {
                    LocalLog.a("ProcessCpuManager", "totalPercent=" + f10 + ", type=" + i10);
                    if (f10 < this.f17543j.C()) {
                        LocalLog.a("ProcessCpuManager", "total percent less than " + this.f17543j.C());
                        return true;
                    }
                    int C = this.f17543j.C();
                    boolean z25 = this.f17543j.a() && z10;
                    List<String> m10 = CommonUtil.m(this.f17536c);
                    ArrayList<String> D = CommonUtil.D(this.f17536c);
                    ArrayList<String> z26 = CommonUtil.z(this.f17536c, 6);
                    List<String> e11 = this.f17541h.e("not_restrict.xml");
                    ArrayList<String> J = CommonUtil.J();
                    boolean z27 = z24;
                    ArrayList<String> q10 = CommonUtil.q(this.f17536c);
                    boolean z28 = z25;
                    List<Integer> k10 = k(this.f17536c);
                    List<String> F = CommonUtil.F(this.f17536c);
                    DecimalFormat decimalFormat3 = decimalFormat2;
                    ArrayList arrayList12 = new ArrayList(this.f17541h.f());
                    ArrayList<String> c10 = this.f17543j.c();
                    ArrayList arrayList13 = new ArrayList();
                    String m11 = this.f17543j.m();
                    String l11 = this.f17543j.l();
                    ArrayList<ActivityManager.RunningAppProcessInfo> L = CommonUtil.L(this.f17536c);
                    if (L == null) {
                        return true;
                    }
                    ArrayList<String> arrayList14 = J;
                    ArrayList arrayList15 = new ArrayList();
                    List<String> list3 = F;
                    ArrayList arrayList16 = new ArrayList();
                    List<String> list4 = e11;
                    ArrayMap arrayMap2 = new ArrayMap();
                    ArrayList<String> arrayList17 = z26;
                    ArrayList<String> arrayList18 = D;
                    ArrayList arrayList19 = new ArrayList();
                    int i16 = 0;
                    while (i16 < L.size()) {
                        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = L.get(i16);
                        arrayList15.add(Integer.valueOf(runningAppProcessInfo.pid));
                        arrayList16.add(runningAppProcessInfo.pkgList[0]);
                        i16++;
                        L = L;
                    }
                    int i17 = 0;
                    int i18 = -1;
                    while (i17 < size) {
                        String str5 = (String) arrayList11.get(i17);
                        String str6 = "";
                        try {
                            split = str5.split(InnerUtils.EQUAL);
                        } catch (IndexOutOfBoundsException e12) {
                            e = e12;
                            list2 = arrayList11;
                        } catch (NumberFormatException e13) {
                            e = e13;
                            list2 = arrayList11;
                        } catch (PatternSyntaxException e14) {
                            e = e14;
                            list2 = arrayList11;
                        }
                        if (split != null) {
                            list2 = arrayList11;
                            try {
                                i14 = size;
                            } catch (IndexOutOfBoundsException e15) {
                                e = e15;
                                i14 = size;
                                arrayList9 = q10;
                                LocalLog.d("ProcessCpuManager", "string = " + str5 + " IndexOutOfBoundsException e = " + e);
                                i15 = i18;
                                str4 = str6;
                                f11 = 0.0f;
                                indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                if (indexOf < 0) {
                                }
                                if (l10 != null) {
                                }
                                arrayList10 = arrayList16;
                                i17++;
                                arrayList16 = arrayList10;
                                size = i14;
                                q10 = arrayList9;
                                i18 = i15;
                                arrayList11 = list2;
                            } catch (NumberFormatException e16) {
                                e = e16;
                                i14 = size;
                                arrayList9 = q10;
                                LocalLog.d("ProcessCpuManager", "string = " + str5 + " NumberFormatException e = " + e);
                                i15 = i18;
                                str4 = str6;
                                f11 = 0.0f;
                                indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                if (indexOf < 0) {
                                }
                                if (l10 != null) {
                                }
                                arrayList10 = arrayList16;
                                i17++;
                                arrayList16 = arrayList10;
                                size = i14;
                                q10 = arrayList9;
                                i18 = i15;
                                arrayList11 = list2;
                            } catch (PatternSyntaxException e17) {
                                e = e17;
                                i14 = size;
                                StringBuilder sb2 = new StringBuilder();
                                arrayList9 = q10;
                                sb2.append("string = ");
                                sb2.append(str5);
                                sb2.append("PatternSyntaxException e = ");
                                sb2.append(e);
                                LocalLog.d("ProcessCpuManager", sb2.toString());
                                i15 = i18;
                                str4 = str6;
                                f11 = 0.0f;
                                indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                if (indexOf < 0) {
                                }
                                if (l10 != null) {
                                }
                                arrayList10 = arrayList16;
                                i17++;
                                arrayList16 = arrayList10;
                                size = i14;
                                q10 = arrayList9;
                                i18 = i15;
                                arrayList11 = list2;
                            }
                            if (split.length == 3) {
                                try {
                                    str6 = split[0];
                                    i18 = Integer.parseInt(split[1]);
                                    f11 = Float.parseFloat(split[2]);
                                    arrayList9 = q10;
                                    i15 = i18;
                                    str4 = str6;
                                } catch (IndexOutOfBoundsException e18) {
                                    e = e18;
                                    arrayList9 = q10;
                                    LocalLog.d("ProcessCpuManager", "string = " + str5 + " IndexOutOfBoundsException e = " + e);
                                    i15 = i18;
                                    str4 = str6;
                                    f11 = 0.0f;
                                    indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                    if (indexOf < 0) {
                                    }
                                    if (l10 != null) {
                                    }
                                    arrayList10 = arrayList16;
                                    i17++;
                                    arrayList16 = arrayList10;
                                    size = i14;
                                    q10 = arrayList9;
                                    i18 = i15;
                                    arrayList11 = list2;
                                } catch (NumberFormatException e19) {
                                    e = e19;
                                    arrayList9 = q10;
                                    LocalLog.d("ProcessCpuManager", "string = " + str5 + " NumberFormatException e = " + e);
                                    i15 = i18;
                                    str4 = str6;
                                    f11 = 0.0f;
                                    indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                    if (indexOf < 0) {
                                    }
                                    if (l10 != null) {
                                    }
                                    arrayList10 = arrayList16;
                                    i17++;
                                    arrayList16 = arrayList10;
                                    size = i14;
                                    q10 = arrayList9;
                                    i18 = i15;
                                    arrayList11 = list2;
                                } catch (PatternSyntaxException e20) {
                                    e = e20;
                                    StringBuilder sb22 = new StringBuilder();
                                    arrayList9 = q10;
                                    sb22.append("string = ");
                                    sb22.append(str5);
                                    sb22.append("PatternSyntaxException e = ");
                                    sb22.append(e);
                                    LocalLog.d("ProcessCpuManager", sb22.toString());
                                    i15 = i18;
                                    str4 = str6;
                                    f11 = 0.0f;
                                    indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                    if (indexOf < 0) {
                                    }
                                    if (l10 != null) {
                                    }
                                    arrayList10 = arrayList16;
                                    i17++;
                                    arrayList16 = arrayList10;
                                    size = i14;
                                    q10 = arrayList9;
                                    i18 = i15;
                                    arrayList11 = list2;
                                }
                                indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                                if (indexOf < 0) {
                                    l10 = (String) arrayList16.get(indexOf);
                                } else {
                                    l10 = l(i15, str4);
                                }
                                if (l10 != null && !l10.isEmpty()) {
                                    if (!arrayList12.contains(l10)) {
                                        if (LocalLog.g()) {
                                            LocalLog.a("ProcessCpuManager", "pkg is in foreList pkg = " + l10);
                                        }
                                    } else {
                                        if (m10.contains(l10)) {
                                            if (LocalLog.g()) {
                                                StringBuilder sb3 = new StringBuilder();
                                                arrayList10 = arrayList16;
                                                sb3.append("skip foreground package ");
                                                sb3.append(l10);
                                                sb3.append(", percent=");
                                                sb3.append(f11);
                                                LocalLog.a("ProcessCpuManager", sb3.toString());
                                            }
                                        } else {
                                            arrayList10 = arrayList16;
                                            if (k10.contains(Integer.valueOf(i15)) && !arrayList13.contains(l10)) {
                                                arrayList13.add(l10);
                                            }
                                            arrayMap2.put(l10, Float.valueOf((arrayMap2.containsKey(l10) ? ((Float) arrayMap2.get(l10)).floatValue() : 0.0f) + f11));
                                        }
                                        i17++;
                                        arrayList16 = arrayList10;
                                        size = i14;
                                        q10 = arrayList9;
                                        i18 = i15;
                                        arrayList11 = list2;
                                    }
                                }
                                arrayList10 = arrayList16;
                                i17++;
                                arrayList16 = arrayList10;
                                size = i14;
                                q10 = arrayList9;
                                i18 = i15;
                                arrayList11 = list2;
                            }
                        } else {
                            list2 = arrayList11;
                            i14 = size;
                        }
                        f11 = 0.0f;
                        arrayList9 = q10;
                        i15 = i18;
                        str4 = str6;
                        indexOf = arrayList15.indexOf(Integer.valueOf(i15));
                        if (indexOf < 0) {
                        }
                        if (l10 != null) {
                            if (!arrayList12.contains(l10)) {
                            }
                        }
                        arrayList10 = arrayList16;
                        i17++;
                        arrayList16 = arrayList10;
                        size = i14;
                        q10 = arrayList9;
                        i18 = i15;
                        arrayList11 = list2;
                    }
                    ArrayList<String> arrayList20 = q10;
                    boolean z29 = z27;
                    int i19 = 0;
                    while (i19 < arrayMap2.size()) {
                        String str7 = (String) arrayMap2.keyAt(i19);
                        float floatValue = ((Float) arrayMap2.get(str7)).floatValue();
                        if (floatValue < C) {
                            if (LocalLog.g()) {
                                LocalLog.a("ProcessCpuManager", "pkg percent < threshold pkg = " + str7);
                            }
                            arrayList3 = arrayList13;
                            i11 = C;
                            arrayList2 = arrayList17;
                            arrayList5 = arrayList14;
                            arrayMap = arrayMap2;
                        } else {
                            ArrayList<String> arrayList21 = arrayList20;
                            if (!arrayList21.contains(str7) && !arrayList13.contains(str7)) {
                                arrayList = arrayList18;
                                z11 = false;
                                contains = arrayList.contains(str7);
                                X = CommonUtil.X(this.f17536c, str7);
                                arrayList2 = arrayList17;
                                contains2 = arrayList2.contains(str7);
                                if (list4 != null || list3 == null) {
                                    arrayList3 = arrayList13;
                                    list3 = list3;
                                    arrayList20 = arrayList21;
                                    z12 = false;
                                    ArrayMap arrayMap3 = arrayMap2;
                                    arrayList4 = arrayList14;
                                    arrayMap = arrayMap3;
                                } else {
                                    arrayList3 = arrayList13;
                                    List<String> list5 = list4;
                                    if (list5.contains(str7)) {
                                        list4 = list5;
                                        list = list3;
                                    } else {
                                        list4 = list5;
                                        list = list3;
                                        if (!list.contains(str7)) {
                                            z23 = false;
                                            arrayList20 = arrayList21;
                                            ArrayMap arrayMap4 = arrayMap2;
                                            arrayList4 = arrayList14;
                                            arrayMap = arrayMap4;
                                            boolean z30 = z23;
                                            list3 = list;
                                            z12 = z30;
                                        }
                                    }
                                    z23 = true;
                                    arrayList20 = arrayList21;
                                    ArrayMap arrayMap42 = arrayMap2;
                                    arrayList4 = arrayList14;
                                    arrayMap = arrayMap42;
                                    boolean z302 = z23;
                                    list3 = list;
                                    z12 = z302;
                                }
                                contains3 = arrayList4.contains(str7);
                                arrayList5 = arrayList4;
                                z13 = !CommonUtil.R(this.f17536c, str7);
                                if (str7 == null) {
                                    arrayList18 = arrayList;
                                    if (str7.equals("android")) {
                                        z14 = true;
                                        if (!z13 || str7 == null) {
                                            i11 = C;
                                        } else {
                                            i11 = C;
                                            if (str7.contains(":")) {
                                                LocalLog.d("ProcessCpuManager", "skip illegal package name " + str7);
                                            }
                                        }
                                        if (floatValue > this.f17543j.D()) {
                                            str = m11;
                                            z16 = true;
                                            z15 = true;
                                        } else {
                                            z15 = z29;
                                            str = l11;
                                            z16 = false;
                                        }
                                        if (LocalLog.g()) {
                                            arrayList6 = arrayList2;
                                            i12 = i19;
                                            StringBuilder sb4 = new StringBuilder();
                                            sb4.append(str7);
                                            str2 = str7;
                                            sb4.append("/");
                                            sb4.append(floatValue);
                                            sb4.append("/audio=");
                                            sb4.append(z11);
                                            sb4.append("/front=");
                                            sb4.append(contains2);
                                            sb4.append("/important=");
                                            sb4.append(contains);
                                            sb4.append("/lock=");
                                            sb4.append(contains3);
                                            sb4.append("/elf=");
                                            sb4.append(z12);
                                            sb4.append("/extreme=");
                                            sb4.append(z16);
                                            sb4.append("/native=");
                                            sb4.append(z13);
                                            sb4.append(", isSystemServer=");
                                            sb4.append(z14);
                                            LocalLog.a("ProcessCpuManager", sb4.toString());
                                        } else {
                                            str2 = str7;
                                            arrayList6 = arrayList2;
                                            i12 = i19;
                                        }
                                        boolean z31 = (contains || !str.contains("import")) && !((z11 && str.contains("audio")) || ((contains2 && str.contains("recent")) || ((z12 && str.contains("guard")) || ((X && str.contains("system")) || ((contains3 && str.contains("lock")) || z14)))));
                                        if (this.f17543j.b()) {
                                            boolean z32 = (z11 || z14) ? false : true;
                                            if (!z32 || c10 == null || c10.size() <= 0) {
                                                i13 = i10;
                                                z17 = z12;
                                                z18 = contains3;
                                                z19 = contains2;
                                                decimalFormat = decimalFormat3;
                                                obj = obj2;
                                                String str8 = str2;
                                                arrayList7 = c10;
                                                z20 = z31;
                                                str3 = str8;
                                            } else {
                                                ArrayList<String> arrayList22 = c10;
                                                i13 = i10;
                                                z20 = z31;
                                                if (i13 == 1) {
                                                    str3 = str2;
                                                    if (arrayList22.contains(str3)) {
                                                        arrayList7 = arrayList22;
                                                        StringBuilder sb5 = new StringBuilder();
                                                        z19 = contains2;
                                                        sb5.append("Kill pkg ");
                                                        sb5.append(str3);
                                                        sb5.append("  Cpu too high ");
                                                        z18 = contains3;
                                                        obj = obj2;
                                                        z17 = z12;
                                                        decimalFormat = decimalFormat3;
                                                        sb5.append(decimalFormat.format(floatValue));
                                                        LocalLog.b("ProcessCpuManager", sb5.toString());
                                                        CommonUtil.Y(this.f17536c, -1, str3, "cpumanager");
                                                    } else {
                                                        z17 = z12;
                                                        z18 = contains3;
                                                        arrayList7 = arrayList22;
                                                        z19 = contains2;
                                                        decimalFormat = decimalFormat3;
                                                        obj = obj2;
                                                        PowerCpuUtil.e(str3, floatValue);
                                                    }
                                                } else {
                                                    z17 = z12;
                                                    z18 = contains3;
                                                    z19 = contains2;
                                                    decimalFormat = decimalFormat3;
                                                    str3 = str2;
                                                    obj = obj2;
                                                    arrayList7 = arrayList22;
                                                }
                                            }
                                            if (z32) {
                                                PowerCpuUtil.e(str3, floatValue);
                                            }
                                        } else {
                                            i13 = i10;
                                            z17 = z12;
                                            z18 = contains3;
                                            z19 = contains2;
                                            decimalFormat = decimalFormat3;
                                            obj = obj2;
                                            String str9 = str2;
                                            arrayList7 = c10;
                                            z20 = z31;
                                            str3 = str9;
                                            if ((z11 || z14) ? false : true) {
                                                PowerCpuUtil.e(str3, floatValue);
                                            }
                                        }
                                        if (i13 == 0 || z16) {
                                            z21 = z20;
                                            z22 = true;
                                        } else {
                                            z22 = false;
                                            z21 = false;
                                        }
                                        if (z13) {
                                            z21 = false;
                                        }
                                        if (z22) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put(DeviceDomainManager.ARG_PKG, str3);
                                            hashMap.put("version", String.valueOf(CommonUtil.I(this.f17536c, str3)));
                                            double d10 = floatValue;
                                            hashMap.put("per", decimalFormat.format(d10));
                                            hashMap.put("total", decimalFormat.format(f10));
                                            hashMap.put("audio", String.valueOf(z11));
                                            hashMap.put("impt", String.valueOf(contains));
                                            hashMap.put("system", String.valueOf(X));
                                            hashMap.put("kill", String.valueOf(z21));
                                            hashMap.put("extreme", String.valueOf(z16));
                                            hashMap.put("lock", String.valueOf(z18));
                                            hashMap.put("elf", String.valueOf(z17));
                                            hashMap.put("front", String.valueOf(z19));
                                            hashMap.put("native", String.valueOf(z13));
                                            hashMap.put("isSystemServer", String.valueOf(z14));
                                            try {
                                                cls = Class.forName("android.os.OplusPowerMonitor");
                                            } catch (Exception e21) {
                                                e = e21;
                                            }
                                            try {
                                                String str10 = (String) cls.getMethod("getAtdTask", new Class[0]).invoke(cls.newInstance(), new Object[0]);
                                                if (str10 != null) {
                                                    hashMap.put("abnormal_cpu_monitor_info", str10);
                                                }
                                            } catch (Exception e22) {
                                                e = e22;
                                                LocalLog.a("ProcessCpuManager", "getAtdTask error = " + e.toString());
                                                this.f17542i.t(hashMap);
                                                if (LocalLog.g()) {
                                                }
                                                if (!z21) {
                                                }
                                                if (!CommonUtil.X(this.f17536c, str3)) {
                                                }
                                                arrayList8 = arrayList19;
                                                z29 = z15;
                                                i19 = i12 + 1;
                                                arrayMap2 = arrayMap;
                                                decimalFormat3 = decimalFormat;
                                                arrayList19 = arrayList8;
                                                obj2 = obj;
                                                arrayList13 = arrayList3;
                                                arrayList14 = arrayList5;
                                                C = i11;
                                                arrayList17 = arrayList6;
                                                c10 = arrayList7;
                                            }
                                            this.f17542i.t(hashMap);
                                            if (LocalLog.g()) {
                                                LocalLog.a("ProcessCpuManager", "upload CPU event " + str3 + ", per=" + decimalFormat.format(d10));
                                            }
                                        }
                                        if (!z21 && z28) {
                                            if (AppFeature.D()) {
                                                LocalLog.b("ProcessCpuManager", "K " + str3 + "  Cpu too high " + decimalFormat.format(floatValue));
                                                CommonUtil.Y(this.f17536c, -1, str3, "cpumanager");
                                            } else {
                                                LocalLog.b("ProcessCpuManager", "F " + str3 + "  Cpu too high " + decimalFormat.format(floatValue));
                                                CommonUtil.d(this.f17536c, str3, "cpumanager");
                                            }
                                            PowerCpuUtil.d(str3);
                                        } else if (!CommonUtil.X(this.f17536c, str3)) {
                                            if (i10 == 1) {
                                                arrayList8 = arrayList19;
                                                arrayList8.add(str3);
                                                z29 = z15;
                                                i19 = i12 + 1;
                                                arrayMap2 = arrayMap;
                                                decimalFormat3 = decimalFormat;
                                                arrayList19 = arrayList8;
                                                obj2 = obj;
                                                arrayList13 = arrayList3;
                                                arrayList14 = arrayList5;
                                                C = i11;
                                                arrayList17 = arrayList6;
                                                c10 = arrayList7;
                                            }
                                            arrayList8 = arrayList19;
                                            z29 = z15;
                                            i19 = i12 + 1;
                                            arrayMap2 = arrayMap;
                                            decimalFormat3 = decimalFormat;
                                            arrayList19 = arrayList8;
                                            obj2 = obj;
                                            arrayList13 = arrayList3;
                                            arrayList14 = arrayList5;
                                            C = i11;
                                            arrayList17 = arrayList6;
                                            c10 = arrayList7;
                                        }
                                        arrayList8 = arrayList19;
                                        z29 = z15;
                                        i19 = i12 + 1;
                                        arrayMap2 = arrayMap;
                                        decimalFormat3 = decimalFormat;
                                        arrayList19 = arrayList8;
                                        obj2 = obj;
                                        arrayList13 = arrayList3;
                                        arrayList14 = arrayList5;
                                        C = i11;
                                        arrayList17 = arrayList6;
                                        c10 = arrayList7;
                                    }
                                } else {
                                    arrayList18 = arrayList;
                                }
                                z14 = false;
                                if (z13) {
                                }
                                i11 = C;
                                if (floatValue > this.f17543j.D()) {
                                }
                                if (LocalLog.g()) {
                                }
                                if (contains) {
                                }
                                if (this.f17543j.b()) {
                                }
                                if (i13 == 0) {
                                }
                                z21 = z20;
                                z22 = true;
                                if (z13) {
                                }
                                if (z22) {
                                }
                                if (!z21) {
                                }
                                if (!CommonUtil.X(this.f17536c, str3)) {
                                }
                                arrayList8 = arrayList19;
                                z29 = z15;
                                i19 = i12 + 1;
                                arrayMap2 = arrayMap;
                                decimalFormat3 = decimalFormat;
                                arrayList19 = arrayList8;
                                obj2 = obj;
                                arrayList13 = arrayList3;
                                arrayList14 = arrayList5;
                                C = i11;
                                arrayList17 = arrayList6;
                                c10 = arrayList7;
                            }
                            arrayList = arrayList18;
                            z11 = true;
                            contains = arrayList.contains(str7);
                            X = CommonUtil.X(this.f17536c, str7);
                            arrayList2 = arrayList17;
                            contains2 = arrayList2.contains(str7);
                            if (list4 != null) {
                            }
                            arrayList3 = arrayList13;
                            list3 = list3;
                            arrayList20 = arrayList21;
                            z12 = false;
                            ArrayMap arrayMap32 = arrayMap2;
                            arrayList4 = arrayList14;
                            arrayMap = arrayMap32;
                            contains3 = arrayList4.contains(str7);
                            arrayList5 = arrayList4;
                            z13 = !CommonUtil.R(this.f17536c, str7);
                            if (str7 == null) {
                            }
                            z14 = false;
                            if (z13) {
                            }
                            i11 = C;
                            if (floatValue > this.f17543j.D()) {
                            }
                            if (LocalLog.g()) {
                            }
                            if (contains) {
                            }
                            if (this.f17543j.b()) {
                            }
                            if (i13 == 0) {
                            }
                            z21 = z20;
                            z22 = true;
                            if (z13) {
                            }
                            if (z22) {
                            }
                            if (!z21) {
                            }
                            if (!CommonUtil.X(this.f17536c, str3)) {
                            }
                            arrayList8 = arrayList19;
                            z29 = z15;
                            i19 = i12 + 1;
                            arrayMap2 = arrayMap;
                            decimalFormat3 = decimalFormat;
                            arrayList19 = arrayList8;
                            obj2 = obj;
                            arrayList13 = arrayList3;
                            arrayList14 = arrayList5;
                            C = i11;
                            arrayList17 = arrayList6;
                            c10 = arrayList7;
                        }
                        arrayList7 = c10;
                        obj = obj2;
                        arrayList6 = arrayList2;
                        i12 = i19;
                        decimalFormat = decimalFormat3;
                        arrayList8 = arrayList19;
                        i19 = i12 + 1;
                        arrayMap2 = arrayMap;
                        decimalFormat3 = decimalFormat;
                        arrayList19 = arrayList8;
                        obj2 = obj;
                        arrayList13 = arrayList3;
                        arrayList14 = arrayList5;
                        C = i11;
                        arrayList17 = arrayList6;
                        c10 = arrayList7;
                    }
                    Object obj3 = obj2;
                    ArrayList arrayList23 = arrayList19;
                    PowerCpuUtil.c(this.f17536c);
                    if (!arrayList23.isEmpty()) {
                        p(arrayList23);
                        this.f17535b.sendEmptyMessageDelayed(3001, 70000L);
                    }
                    if (LocalLog.g()) {
                        LocalLog.a("ProcessCpuManager", "cpu check finish. type=" + i10 + ", shouldFinish=" + z29);
                    }
                    return z29;
                }
                LocalLog.l("ProcessCpuManager", "wrong sample time, return.");
                return true;
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public static String i(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager == null) {
            return null;
        }
        String parameters = audioManager.getParameters("get_record_pid");
        if (TextUtils.isEmpty(parameters)) {
            return null;
        }
        return parameters;
    }

    public static synchronized ProcessCpuManager j(Context context) {
        ProcessCpuManager processCpuManager;
        synchronized (ProcessCpuManager.class) {
            if (f17533k == null) {
                f17533k = new ProcessCpuManager(context);
            }
            processCpuManager = f17533k;
        }
        return processCpuManager;
    }

    public static List<Integer> k(Context context) {
        return o(i(context));
    }

    private String l(int i10, String str) {
        int m10 = m(i10);
        if (m10 == -1) {
            return "";
        }
        if (m10 < 10000) {
            return str == null ? "" : str;
        }
        if (str != null && str.equals("dex2oat")) {
            return str;
        }
        PackageManager packageManager = this.f17536c.getPackageManager();
        String nameForUid = packageManager.getNameForUid(m10);
        if (nameForUid == null) {
            nameForUid = "";
        }
        if (!nameForUid.contains(":")) {
            return nameForUid;
        }
        String[] packagesForUid = packageManager.getPackagesForUid(m10);
        return (packagesForUid == null || packagesForUid.length < 1) ? "" : packagesForUid[0];
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:6|7|9|10|11|12|(3:(4:13|14|(1:16)(0)|28)|28|29)|19|20|21|22|(2:30|31)|32) */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0065, code lost:
    
        r7 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0066, code lost:
    
        b6.LocalLog.b("ProcessCpuManager", "error parsing high-cpu process uid " + r7);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00bd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:100:0x008d -> B:31:0x00f3). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int m(int i10) {
        FileInputStream fileInputStream;
        ?? r22;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2;
        Throwable th;
        String readLine;
        String str = "/proc/" + String.valueOf(i10) + "/status";
        int i11 = -1;
        if (!new File(str).exists()) {
            return -1;
        }
        FileInputStream fileInputStream2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(str);
                try {
                    inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
                    try {
                        r22 = new BufferedReader(inputStreamReader);
                        try {
                            do {
                                try {
                                    try {
                                        readLine = r22.readLine();
                                        if (readLine != null) {
                                        }
                                        break;
                                    } catch (RuntimeException unused) {
                                        fileInputStream2 = fileInputStream;
                                        bufferedReader2 = r22;
                                        if (fileInputStream2 != null) {
                                            try {
                                                fileInputStream2.close();
                                            } catch (IOException unused2) {
                                            }
                                        }
                                        if (bufferedReader2 != null) {
                                            try {
                                                bufferedReader2.close();
                                            } catch (IOException e10) {
                                                e10.printStackTrace();
                                            }
                                        }
                                        if (inputStreamReader != null) {
                                            inputStreamReader.close();
                                        }
                                        return i11;
                                    } catch (Exception unused3) {
                                        fileInputStream2 = fileInputStream;
                                        bufferedReader = r22;
                                        if (fileInputStream2 != null) {
                                            try {
                                                fileInputStream2.close();
                                            } catch (IOException unused4) {
                                            }
                                        }
                                        if (bufferedReader != null) {
                                            try {
                                                bufferedReader.close();
                                            } catch (IOException e11) {
                                                e11.printStackTrace();
                                            }
                                        }
                                        if (inputStreamReader != null) {
                                            inputStreamReader.close();
                                        }
                                        return i11;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (IOException unused5) {
                                        }
                                    }
                                    if (r22 != 0) {
                                        try {
                                            r22.close();
                                        } catch (IOException e12) {
                                            e12.printStackTrace();
                                        }
                                    }
                                    if (inputStreamReader == null) {
                                        try {
                                            inputStreamReader.close();
                                            throw th;
                                        } catch (IOException e13) {
                                            e13.printStackTrace();
                                            throw th;
                                        }
                                    }
                                    throw th;
                                }
                            } while (!readLine.startsWith("Uid:"));
                            break;
                            fileInputStream.close();
                        } catch (IOException unused6) {
                        }
                        String trim = readLine.substring(4, readLine.length()).trim();
                        i11 = Integer.parseInt(trim.substring(0, trim.indexOf("\t")));
                        try {
                            r22.close();
                        } catch (IOException e14) {
                            e14.printStackTrace();
                        }
                        inputStreamReader.close();
                    } catch (RuntimeException unused7) {
                        r22 = 0;
                    } catch (Exception unused8) {
                        r22 = 0;
                    } catch (Throwable th3) {
                        r22 = 0;
                        th = th3;
                    }
                } catch (RuntimeException unused9) {
                    inputStreamReader = null;
                    r22 = 0;
                } catch (Exception unused10) {
                    inputStreamReader = null;
                    r22 = 0;
                } catch (Throwable th4) {
                    th = th4;
                    r22 = 0;
                    th = th;
                    inputStreamReader = r22;
                    if (fileInputStream != null) {
                    }
                    if (r22 != 0) {
                    }
                    if (inputStreamReader == null) {
                    }
                }
            } catch (RuntimeException unused11) {
                inputStreamReader = null;
                bufferedReader2 = null;
            } catch (Exception unused12) {
                inputStreamReader = null;
                bufferedReader = null;
            } catch (Throwable th5) {
                th = th5;
                fileInputStream = null;
                r22 = 0;
            }
        } catch (IOException e15) {
            e15.printStackTrace();
        }
        return i11;
    }

    private static List<Integer> o(String str) {
        if (TextUtils.isEmpty(str)) {
            return new ArrayList();
        }
        ArraySet arraySet = new ArraySet();
        String[] split = str.split(":");
        for (int i10 = 1; i10 < split.length; i10++) {
            try {
                arraySet.add(Integer.valueOf(Integer.parseInt(split[i10])));
            } catch (NumberFormatException e10) {
                LocalLog.c("ProcessCpuManager", split[i10] + " is not number", e10);
            }
        }
        return new ArrayList(arraySet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p(List<String> list) {
        if (list != null) {
            synchronized (this.f17538e) {
                LocalFileUtil.c().l("battery", "cpu_abnormal_list.xml", list, this.f17536c);
            }
        }
    }

    public void h() {
        this.f17535b.removeMessages(3001);
        p(new ArrayList());
    }

    public boolean n() {
        return this.f17537d.get();
    }

    public void q(boolean z10) {
        this.f17539f.set(z10);
    }

    public void r() {
        s(true);
    }

    public void s(boolean z10) {
        if (SystemClock.uptimeMillis() - this.f17540g < (this.f17539f.get() ? 300000 : 180000)) {
            LocalLog.l("ProcessCpuManager", "wait for more time to trigger cpu check");
            return;
        }
        if (this.f17537d.get()) {
            LocalLog.l("ProcessCpuManager", "already sampling, return.");
            return;
        }
        this.f17535b.removeMessages(DataTypeConstants.USER_ACTION);
        this.f17535b.removeMessages(DataTypeConstants.APP_LOG);
        this.f17535b.removeMessages(DataTypeConstants.PAGE_VISIT);
        Message obtainMessage = this.f17535b.obtainMessage(DataTypeConstants.USER_ACTION);
        obtainMessage.arg1 = z10 ? 1 : 0;
        this.f17535b.sendMessage(obtainMessage);
    }
}
