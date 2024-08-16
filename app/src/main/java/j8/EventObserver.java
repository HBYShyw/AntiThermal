package j8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.ArraySet;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import h8.IPage;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* compiled from: EventObserver.java */
/* renamed from: j8.a, reason: use source file name */
/* loaded from: classes.dex */
public class EventObserver {

    /* renamed from: i, reason: collision with root package name */
    private static final String f13051i = "a";

    /* renamed from: j, reason: collision with root package name */
    private static Context f13052j;

    /* renamed from: k, reason: collision with root package name */
    private static final Map<Integer, Set<IPage>> f13053k = new ArrayMap();

    /* renamed from: a, reason: collision with root package name */
    private h f13054a = null;

    /* renamed from: b, reason: collision with root package name */
    private e f13055b = null;

    /* renamed from: c, reason: collision with root package name */
    private i f13056c = null;

    /* renamed from: d, reason: collision with root package name */
    private BroadcastReceiver f13057d = null;

    /* renamed from: e, reason: collision with root package name */
    private BroadcastReceiver f13058e = null;

    /* renamed from: f, reason: collision with root package name */
    private ContentObserver f13059f = new a(new Handler());

    /* renamed from: g, reason: collision with root package name */
    private ContentObserver f13060g = new b(new Handler());

    /* renamed from: h, reason: collision with root package name */
    private ContentObserver f13061h = new c(new Handler());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$a */
    /* loaded from: classes.dex */
    public class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean d12 = f6.f.d1(EventObserver.f13052j);
            synchronized (EventObserver.f13053k) {
                Set set = (Set) EventObserver.f13053k.get(Integer.valueOf(EventType.SCENE_MODE_AUDIO_IN));
                if (set != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("boolean_wireless_reverse_state", d12);
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        ((IPage) it.next()).c(EventType.SCENE_MODE_AUDIO_IN, bundle);
                    }
                }
            }
        }
    }

    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$b */
    /* loaded from: classes.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            int c12 = f6.f.c1(EventObserver.f13052j);
            synchronized (EventObserver.f13053k) {
                Set set = (Set) EventObserver.f13053k.get(Integer.valueOf(EventType.SCENE_MODE_CAMERA));
                if (set != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("int_reverse_threshold_val", c12);
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        ((IPage) it.next()).c(EventType.SCENE_MODE_CAMERA, bundle);
                    }
                }
            }
        }
    }

    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$c */
    /* loaded from: classes.dex */
    class c extends ContentObserver {
        c(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean J0 = f6.f.J0(EventObserver.f13052j);
            synchronized (EventObserver.f13053k) {
                Set set = (Set) EventObserver.f13053k.get(Integer.valueOf(EventType.SCENE_MODE_DOWNLOAD));
                if (set != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("boolean_smartcharge_state", J0);
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        ((IPage) it.next()).c(EventType.SCENE_MODE_DOWNLOAD, bundle);
                    }
                }
            }
        }
    }

    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$d */
    /* loaded from: classes.dex */
    private static class d {

        /* renamed from: a, reason: collision with root package name */
        private static final EventObserver f13065a = new EventObserver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$e */
    /* loaded from: classes.dex */
    public class e extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        private final Context f13066a;

        public e(Context context) {
            super(new Handler());
            this.f13066a = context.getApplicationContext();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean z11 = Settings.System.getIntForUser(this.f13066a.getContentResolver(), "high_performance_mode_on", 0, 0) == 1;
            synchronized (EventObserver.f13053k) {
                Set set = (Set) EventObserver.f13053k.get(Integer.valueOf(EventType.SCENE_MODE_AUDIO_OUT));
                if (set != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("boolean_highpref_state", z11);
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        ((IPage) it.next()).c(EventType.SCENE_MODE_AUDIO_OUT, bundle);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$f */
    /* loaded from: classes.dex */
    public class f extends BroadcastReceiver {
        private f() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.ADDITIONAL_BATTERY_CHANGED".equals(intent.getAction())) {
                synchronized (EventObserver.f13053k) {
                    Set set = (Set) EventObserver.f13053k.get(101);
                    if (set != null) {
                        Intent intent2 = new Intent(intent);
                        Iterator it = set.iterator();
                        while (it.hasNext()) {
                            ((IPage) it.next()).e(intent2);
                        }
                    }
                }
            }
        }

        /* synthetic */ f(EventObserver eventObserver, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$g */
    /* loaded from: classes.dex */
    public class g extends BroadcastReceiver {
        private g() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                synchronized (EventObserver.f13053k) {
                    Set set = (Set) EventObserver.f13053k.get(100);
                    if (set != null) {
                        Intent intent2 = new Intent(intent);
                        Iterator it = set.iterator();
                        while (it.hasNext()) {
                            ((IPage) it.next()).e(intent2);
                        }
                    }
                }
            }
        }

        /* synthetic */ g(EventObserver eventObserver, a aVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$h */
    /* loaded from: classes.dex */
    public class h extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        private final Context f13070a;

        public h(Context context) {
            super(new Handler());
            this.f13070a = context.getApplicationContext();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean z11 = Settings.Global.getInt(this.f13070a.getContentResolver(), "low_power", 0) == 1;
            synchronized (EventObserver.f13053k) {
                Set set = (Set) EventObserver.f13053k.get(200);
                if (set != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("boolean_powersave_state", z11);
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        ((IPage) it.next()).c(200, bundle);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: EventObserver.java */
    /* renamed from: j8.a$i */
    /* loaded from: classes.dex */
    public static class i extends ContentObserver {
        public i() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            boolean a12 = f6.f.a1(EventObserver.f13052j);
            synchronized (EventObserver.f13053k) {
                Set set = (Set) EventObserver.f13053k.get(Integer.valueOf(EventType.SCENE_MODE_LOCATION));
                if (set != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("boolean_s_powersave_state", a12);
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        ((IPage) it.next()).c(EventType.SCENE_MODE_LOCATION, bundle);
                    }
                }
            }
        }
    }

    public static EventObserver d(Context context) {
        f13052j = context.getApplicationContext();
        return d.f13065a;
    }

    private void e() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ADDITIONAL_BATTERY_CHANGED");
        f fVar = new f(this, null);
        this.f13058e = fVar;
        f13052j.registerReceiver(fVar, intentFilter, 4);
    }

    private void f() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        this.f13057d = new g(this, null);
        Intent intent = new Intent(f13052j.registerReceiver(this.f13057d, intentFilter, 2));
        Map<Integer, Set<IPage>> map = f13053k;
        synchronized (map) {
            Set<IPage> set = map.get(100);
            if (set != null) {
                Iterator<IPage> it = set.iterator();
                while (it.hasNext()) {
                    it.next().e(intent);
                }
            }
        }
    }

    private void g() {
        if (f6.f.i1()) {
            this.f13055b = new e(f13052j);
            f13052j.getContentResolver().registerContentObserver(Settings.System.getUriFor("high_performance_mode_on"), false, this.f13055b, 0);
        }
    }

    private void h() {
        this.f13054a = new h(f13052j);
        f13052j.getContentResolver().registerContentObserver(Settings.Global.getUriFor("low_power"), false, this.f13054a, -2);
    }

    private void i() {
        f13052j.getContentResolver().registerContentObserver(Settings.System.getUriFor("wireless_reverse_battery_level_threshold"), false, this.f13060g, 0);
    }

    private void j() {
        f13052j.getContentResolver().registerContentObserver(Settings.System.getUriFor("smart_charge_switch_state"), false, this.f13061h, 0);
    }

    private void k() {
        this.f13056c = new i();
        f13052j.getContentResolver().registerContentObserver(Settings.System.getUriFor("super_powersave_mode_state"), false, this.f13056c, 0);
    }

    private void l() {
        f13052j.getContentResolver().registerContentObserver(Settings.System.getUriFor("wireless_reverse_charging_state"), false, this.f13059f, 0);
    }

    private void n() {
        BroadcastReceiver broadcastReceiver = this.f13058e;
        if (broadcastReceiver != null) {
            f13052j.unregisterReceiver(broadcastReceiver);
            this.f13058e = null;
        }
    }

    private void o() {
        BroadcastReceiver broadcastReceiver = this.f13057d;
        if (broadcastReceiver != null) {
            f13052j.unregisterReceiver(broadcastReceiver);
            this.f13057d = null;
        }
    }

    private void p() {
        if (this.f13055b != null) {
            f13052j.getContentResolver().unregisterContentObserver(this.f13055b);
        }
    }

    private void q() {
        if (this.f13054a != null) {
            f13052j.getContentResolver().unregisterContentObserver(this.f13054a);
        }
    }

    private void r() {
        if (this.f13060g != null) {
            f13052j.getContentResolver().unregisterContentObserver(this.f13060g);
        }
    }

    private void s() {
        if (this.f13059f != null) {
            f13052j.getContentResolver().unregisterContentObserver(this.f13061h);
        }
    }

    private void t() {
        if (this.f13056c != null) {
            f13052j.getContentResolver().unregisterContentObserver(this.f13056c);
        }
    }

    private void u() {
        if (this.f13059f != null) {
            f13052j.getContentResolver().unregisterContentObserver(this.f13059f);
        }
    }

    public void c(IPage iPage, int i10) {
        Map<Integer, Set<IPage>> map = f13053k;
        synchronized (map) {
            Set<IPage> set = map.get(Integer.valueOf(i10));
            if (set == null || set.size() < 1) {
                if (set == null) {
                    set = new ArraySet<>();
                }
                map.put(Integer.valueOf(i10), set);
                if (i10 == 100) {
                    f();
                } else if (i10 != 101) {
                    switch (i10) {
                        case 200:
                            h();
                            break;
                        case EventType.SCENE_MODE_LOCATION /* 201 */:
                            k();
                            break;
                        case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                            g();
                            break;
                        case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                            l();
                            break;
                        case EventType.SCENE_MODE_CAMERA /* 204 */:
                            i();
                            break;
                        case EventType.SCENE_MODE_VIDEO /* 205 */:
                            break;
                        case EventType.SCENE_MODE_DOWNLOAD /* 206 */:
                            j();
                            break;
                        default:
                            LocalLog.l(f13051i, "eventType is " + i10 + ", not match case, do not to register");
                            break;
                    }
                } else {
                    e();
                }
            }
            if (!set.contains(iPage)) {
                set.add(iPage);
            }
        }
    }

    public void m(IPage iPage, int i10) {
        Map<Integer, Set<IPage>> map = f13053k;
        synchronized (map) {
            Set<IPage> set = map.get(Integer.valueOf(i10));
            if (set != null) {
                set.remove(iPage);
                if (set.size() < 1) {
                    if (i10 == 100) {
                        o();
                    } else if (i10 != 101) {
                        switch (i10) {
                            case 200:
                                q();
                                break;
                            case EventType.SCENE_MODE_LOCATION /* 201 */:
                                t();
                                break;
                            case EventType.SCENE_MODE_AUDIO_OUT /* 202 */:
                                p();
                                break;
                            case EventType.SCENE_MODE_AUDIO_IN /* 203 */:
                                u();
                                break;
                            case EventType.SCENE_MODE_CAMERA /* 204 */:
                                r();
                                break;
                            case EventType.SCENE_MODE_VIDEO /* 205 */:
                                break;
                            case EventType.SCENE_MODE_DOWNLOAD /* 206 */:
                                s();
                                break;
                            default:
                                LocalLog.l(f13051i, "eventType is " + i10 + ", not match case, do not unregister");
                                break;
                        }
                    } else {
                        n();
                    }
                }
            }
        }
    }
}
