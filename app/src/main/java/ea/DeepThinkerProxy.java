package ea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.ClientConnection;
import com.oplus.deepthinker.sdk.app.OplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.ServiceStateObserver;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.DeviceEventResult;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.thermalcontrol.ThermalControlUtils;
import f6.CommonUtil;
import g7.AppInfoManager;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import w4.Affair;
import w4.IAffairCallback;

/* compiled from: DeepThinkerProxy.java */
/* renamed from: ea.b, reason: use source file name */
/* loaded from: classes2.dex */
public class DeepThinkerProxy {

    /* renamed from: k, reason: collision with root package name */
    private static volatile DeepThinkerProxy f10975k;

    /* renamed from: a, reason: collision with root package name */
    private Context f10976a;

    /* renamed from: b, reason: collision with root package name */
    private Handler f10977b;

    /* renamed from: c, reason: collision with root package name */
    private OplusDeepThinkerManager f10978c;

    /* renamed from: d, reason: collision with root package name */
    private ServiceStateObserver f10979d;

    /* renamed from: e, reason: collision with root package name */
    private ClientConnection f10980e;

    /* renamed from: f, reason: collision with root package name */
    private b f10981f;

    /* renamed from: g, reason: collision with root package name */
    private Map<Integer, PackageRecord> f10982g;

    /* renamed from: h, reason: collision with root package name */
    private HandlerThread f10983h = new HandlerThread("DeepThinkerProxy");

    /* renamed from: i, reason: collision with root package name */
    private boolean f10984i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f10985j = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DeepThinkerProxy.java */
    /* renamed from: ea.b$a */
    /* loaded from: classes2.dex */
    public class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    /* compiled from: DeepThinkerProxy.java */
    /* renamed from: ea.b$b */
    /* loaded from: classes2.dex */
    public class b implements IAffairCallback {

        /* renamed from: e, reason: collision with root package name */
        private EventConfig f10987e;

        /* renamed from: f, reason: collision with root package name */
        private Thread f10988f = null;

        /* renamed from: g, reason: collision with root package name */
        private int f10989g = 100;

        /* renamed from: h, reason: collision with root package name */
        private int f10990h = 0;

        /* renamed from: i, reason: collision with root package name */
        private int f10991i = 5000;

        /* renamed from: j, reason: collision with root package name */
        public IEventCallback f10992j = new a();

        /* renamed from: k, reason: collision with root package name */
        private Handler f10993k;

        /* compiled from: DeepThinkerProxy.java */
        /* renamed from: ea.b$b$a */
        /* loaded from: classes2.dex */
        class a extends EventCallback {
            a() {
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventCallback, com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback
            public void onEventStateChanged(DeviceEventResult deviceEventResult) {
                int i10;
                int eventStateType = deviceEventResult.getEventStateType();
                if (eventStateType == 0) {
                    LocalLog.a("DeepThinkerProxy", "event enter.");
                    i10 = 1;
                } else if (eventStateType == 1) {
                    LocalLog.a("DeepThinkerProxy", "event exit.");
                    i10 = 0;
                } else if (eventStateType == 2) {
                    LocalLog.a("DeepThinkerProxy", "event update.");
                    i10 = 2;
                } else {
                    LocalLog.a("DeepThinkerProxy", "event without status.");
                    i10 = -1;
                }
                int eventType = deviceEventResult.getEventType();
                int i11 = EventType.SCENE_MODE_AUDIO_IN;
                if (eventType == 203) {
                    LocalLog.a("DeepThinkerProxy", "event AUDIO_IN");
                } else if (eventType == 215) {
                    LocalLog.a("DeepThinkerProxy", "event MUSIC_PLAY");
                    i11 = 107;
                } else if (eventType == 218) {
                    LocalLog.a("DeepThinkerProxy", "event AI_NAVIGATION");
                    i11 = 106;
                } else if (eventType == 220) {
                    LocalLog.a("DeepThinkerProxy", "event SHORT_VIDEO");
                    i11 = EventType.SCENE_MODE_AUDIO_OUT;
                } else if (eventType == 205) {
                    LocalLog.a("DeepThinkerProxy", "event VIDEO.");
                    i11 = 101;
                } else if (eventType == 206) {
                    Log.d("DeepThinkerProxy", "event DOWNLOAD");
                    LocalLog.a("DeepThinkerProxy", "event DOWNLOAD");
                    i11 = 201;
                } else if (eventType == 208) {
                    LocalLog.a("DeepThinkerProxy", "event AUDIO CALL.");
                    i11 = 102;
                } else if (eventType == 209) {
                    LocalLog.a("DeepThinkerProxy", "event SCENE_MODE_VIDEO.");
                    i11 = 103;
                } else if (eventType == 211) {
                    LocalLog.a("DeepThinkerProxy", "event GAME.");
                    i11 = 104;
                } else if (eventType != 212) {
                    i11 = -1;
                } else {
                    LocalLog.a("DeepThinkerProxy", "event VIDEO_LIVE");
                    i11 = 105;
                }
                int pid = deviceEventResult.getPid();
                String pkgName = deviceEventResult.getPkgName();
                if (pid < 0 || TextUtils.isEmpty(pkgName)) {
                    return;
                }
                Bundle extraData = deviceEventResult.getExtraData();
                int i12 = extraData != null ? extraData.getInt(TriggerEvent.EXTRA_UID, -1) : -1;
                if (i12 == -1) {
                    i12 = CommonUtil.P(DeepThinkerProxy.this.f10976a, pkgName);
                }
                if (i10 != -1 && i11 != -1) {
                    LocalLog.a("DeepThinkerProxy", "event valid mActionType " + i10 + " mSceneType " + i11);
                } else {
                    LocalLog.a("DeepThinkerProxy", "event invalid mActionType " + i10 + " mSceneType " + i11);
                }
                if (i10 == 1) {
                    FrameworksProxy.f().l(i11, pkgName, i12);
                    if (i11 != 106) {
                        synchronized (DeepThinkerProxy.this.f10982g) {
                            if (((PackageRecord) DeepThinkerProxy.this.f10982g.get(Integer.valueOf(i11))) == null) {
                                DeepThinkerProxy.this.f10982g.put(Integer.valueOf(i11), new PackageRecord(DeepThinkerProxy.this.f10976a, pkgName, i12));
                            } else {
                                DeepThinkerProxy.this.f10982g.remove(Integer.valueOf(i11));
                                DeepThinkerProxy.this.f10982g.put(Integer.valueOf(i11), new PackageRecord(DeepThinkerProxy.this.f10976a, pkgName, i12));
                            }
                        }
                    }
                } else if (i10 == 0) {
                    FrameworksProxy.f().e(i11, pkgName, i12);
                    if (i11 != 106) {
                        synchronized (DeepThinkerProxy.this.f10982g) {
                            if (((PackageRecord) DeepThinkerProxy.this.f10982g.get(Integer.valueOf(i11))) != null) {
                                DeepThinkerProxy.this.f10982g.remove(Integer.valueOf(i11));
                            }
                        }
                    }
                } else if (i10 == 2) {
                    if (i11 != 106) {
                        synchronized (DeepThinkerProxy.this.f10982g) {
                            PackageRecord packageRecord = (PackageRecord) DeepThinkerProxy.this.f10982g.get(Integer.valueOf(i11));
                            if (packageRecord != null) {
                                if (packageRecord.e() != null && !packageRecord.e().equals(pkgName)) {
                                    FrameworksProxy.f().e(i11, packageRecord.e(), packageRecord.f());
                                    DeepThinkerProxy.this.f10982g.remove(Integer.valueOf(i11));
                                    DeepThinkerProxy.this.f10982g.put(Integer.valueOf(i11), new PackageRecord(DeepThinkerProxy.this.f10976a, pkgName, i12));
                                    FrameworksProxy.f().l(i11, pkgName, i12);
                                }
                            } else {
                                DeepThinkerProxy.this.f10982g.put(Integer.valueOf(i11), new PackageRecord(DeepThinkerProxy.this.f10976a, pkgName, i12));
                                FrameworksProxy.f().l(i11, pkgName, i12);
                            }
                        }
                    } else if (i11 == 106 && extraData != null) {
                        int parseInt = Integer.parseInt(extraData.get(EventType.CHANGED_STATE).toString());
                        String obj = extraData.get(EventType.CHANGED_STATE_VALUE).toString();
                        if (parseInt == 1 && obj != null) {
                            if (obj.equals(EventType.STATE_PACKAGE_CHANGED_ADD)) {
                                FrameworksProxy.f().l(i11, pkgName, i12);
                            } else if (obj.equals(EventType.STATE_PACKAGE_CHANGED_REMOVE)) {
                                FrameworksProxy.f().e(i11, pkgName, i12);
                            }
                        }
                    }
                }
                if (i11 >= 201 || i11 <= 100) {
                    return;
                }
                LocalLog.l("DeepThinkerProxy", "onEventStateChanged " + deviceEventResult.toString());
                ThermalControlUtils.getInstance(DeepThinkerProxy.this.f10976a).doSceneChange(pkgName, i11, i10);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: DeepThinkerProxy.java */
        /* renamed from: ea.b$b$b, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public class RunnableC0031b implements Runnable {

            /* compiled from: DeepThinkerProxy.java */
            /* renamed from: ea.b$b$b$a */
            /* loaded from: classes2.dex */
            class a implements ServiceStateObserver {
                a() {
                }

                @Override // com.oplus.deepthinker.sdk.app.ServiceStateObserver
                public void onServiceDied() {
                    b.this.c();
                }

                @Override // com.oplus.deepthinker.sdk.app.ServiceStateObserver
                public void onStartup() {
                    AppInfoManager.m(DeepThinkerProxy.this.f10976a).w();
                }
            }

            RunnableC0031b() {
            }

            @Override // java.lang.Runnable
            public void run() {
                b.this.c();
                DeepThinkerProxy.this.f10979d = new a();
                DeepThinkerProxy.this.f10978c.setRemote(DeepThinkerProxy.this.f10980e.getDeepThinkerBridge());
                DeepThinkerProxy.this.f10980e.registerServiceStateObserver(DeepThinkerProxy.this.f10979d);
            }
        }

        /* compiled from: DeepThinkerProxy.java */
        /* renamed from: ea.b$b$c */
        /* loaded from: classes2.dex */
        class c extends Handler {
            c(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == b.this.f10989g) {
                    b.this.c();
                }
            }
        }

        public b() {
            this.f10987e = null;
            this.f10993k = new c(DeepThinkerProxy.this.f10983h.getLooper());
            HashSet hashSet = new HashSet();
            hashSet.add(new Event(EventType.SCENE_MODE_VIDEO, null));
            hashSet.add(new Event(EventType.SCENE_MODE_AUDIO_CALL, null));
            hashSet.add(new Event(EventType.SCENE_MODE_VIDEO_CALL, null));
            hashSet.add(new Event(EventType.SCENE_MODE_GAME, null));
            hashSet.add(new Event(EventType.SCENE_MODE_VIDEO_LIVE, null));
            hashSet.add(new Event(EventType.SCENE_MODE_NAVIGATION, null));
            hashSet.add(new Event(EventType.SCENE_MODE_MUSIC_PLAY, null));
            hashSet.add(new Event(EventType.SCENE_MODE_DOWNLOAD, null));
            hashSet.add(new Event(EventType.SCENE_SHORT_VIDEO, null));
            hashSet.add(new Event(EventType.SCENE_MODE_AUDIO_IN, null));
            this.f10987e = new EventConfig((HashSet<Event>) hashSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void c() {
            DeepThinkerProxy.this.f10978c.setRemote(DeepThinkerProxy.this.f10980e.getDeepThinkerBridge());
            int registerEventCallback = DeepThinkerProxy.this.f10978c.registerEventCallback(this.f10992j, this.f10987e);
            int i10 = this.f10990h;
            if (i10 < 5 && registerEventCallback != 1) {
                this.f10990h = i10 + 1;
                if (this.f10993k.hasMessages(this.f10989g)) {
                    this.f10993k.removeMessages(this.f10989g);
                }
                this.f10993k.sendEmptyMessageDelayed(this.f10989g, this.f10991i);
                LocalLog.a("DeepThinkerProxy", "DeepThinkerManager register failed " + this.f10990h + " times");
            }
            if (registerEventCallback == 1) {
                this.f10990h = 0;
                this.f10993k.removeMessages(this.f10989g);
                LocalLog.a("DeepThinkerProxy", "DeepThinkerManager register success!");
            }
        }

        public void d() {
            LocalLog.d("DeepThinkerProxy", "register begin");
            Thread thread = new Thread(new RunnableC0031b());
            this.f10988f = thread;
            thread.start();
        }

        public void e() {
            Affair.f().i(this, 1208);
        }

        @Override // w4.IAffairCallback
        public void execute(int i10, Intent intent) {
            if (i10 != 1208) {
                return;
            }
            LocalLog.a("DeepThinkerProxy", "receive DEEPTHINKER_EVENTFOUNTAIN_STARTUP");
            c();
        }

        @Override // w4.IAffairCallback
        public void execute(int i10, Bundle bundle) {
        }

        public void f() {
            DeepThinkerProxy.this.f10978c.unregisterEventCallback(this.f10992j);
            Thread thread = this.f10988f;
            if (thread != null) {
                thread.interrupt();
                this.f10988f = null;
            }
        }

        @Override // w4.IAffairCallback
        public void registerAction() {
            Affair.f().g(this, 1208);
        }
    }

    private DeepThinkerProxy(Context context) {
        this.f10976a = context;
        l();
    }

    public static DeepThinkerProxy j(Context context) {
        if (f10975k == null) {
            synchronized (DeepThinkerProxy.class) {
                if (f10975k == null) {
                    f10975k = new DeepThinkerProxy(context);
                }
            }
        }
        return f10975k;
    }

    private void l() {
        this.f10983h.start();
        this.f10977b = new a(this.f10983h.getLooper());
        this.f10978c = new OplusDeepThinkerManager(this.f10976a);
        final ClientConnection clientConnection = new ClientConnection(this.f10976a, Executors.newFixedThreadPool(3), this.f10977b);
        this.f10980e = clientConnection;
        OplusDeepThinkerManager oplusDeepThinkerManager = this.f10978c;
        Objects.requireNonNull(clientConnection);
        oplusDeepThinkerManager.setRemoteGetter(new Supplier() { // from class: ea.a
            @Override // java.util.function.Supplier
            public final Object get() {
                return ClientConnection.this.getDeepThinkerBridge();
            }
        });
    }

    public void h() {
        if (this.f10985j) {
            LocalLog.a("DeepThinkerProxy", "DeepThinkerProxy already destroy");
            return;
        }
        this.f10984i = false;
        this.f10985j = true;
        this.f10981f.f();
        this.f10981f.e();
        LocalLog.a("DeepThinkerProxy", "DeepThinkerProxy destroy");
    }

    public OplusDeepThinkerManager i() {
        return this.f10978c;
    }

    public void k() {
        if (this.f10984i) {
            LocalLog.a("DeepThinkerProxy", "DeepThinkerProxy already init");
            return;
        }
        this.f10984i = true;
        this.f10985j = false;
        LocalLog.a("DeepThinkerProxy", "DeepThinkerProxy init");
        if (this.f10981f == null) {
            this.f10981f = new b();
        }
        this.f10981f.registerAction();
        this.f10981f.d();
        this.f10982g = new ArrayMap();
    }
}
