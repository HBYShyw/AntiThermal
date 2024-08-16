package s8;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import b6.LocalLog;
import f6.f;
import java.util.HashMap;
import t8.PowerUsageManager;

/* compiled from: PowerSaveConfig.java */
/* renamed from: s8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSaveConfig {

    /* renamed from: i, reason: collision with root package name */
    private static volatile PowerSaveConfig f18152i;

    /* renamed from: e, reason: collision with root package name */
    private Context f18157e;

    /* renamed from: g, reason: collision with root package name */
    private Handler f18159g;

    /* renamed from: a, reason: collision with root package name */
    private HashMap<String, Integer> f18153a = new HashMap<>();

    /* renamed from: b, reason: collision with root package name */
    private boolean f18154b = false;

    /* renamed from: c, reason: collision with root package name */
    private boolean f18155c = false;

    /* renamed from: d, reason: collision with root package name */
    private boolean f18156d = true;

    /* renamed from: f, reason: collision with root package name */
    private ContentObserver f18158f = null;

    /* renamed from: h, reason: collision with root package name */
    private final Object f18160h = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PowerSaveConfig.java */
    /* renamed from: s8.b$a */
    /* loaded from: classes2.dex */
    public class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        /* JADX WARN: Removed duplicated region for block: B:125:0x02b8 A[Catch: all -> 0x0334, TryCatch #0 {, blocks: (B:8:0x002a, B:10:0x0037, B:13:0x0045, B:15:0x0051, B:18:0x0308, B:20:0x0320, B:21:0x032d, B:25:0x0063, B:27:0x006f, B:30:0x0081, B:32:0x008e, B:34:0x009a, B:36:0x00a6, B:39:0x00b4, B:41:0x00c1, B:44:0x00cf, B:46:0x00db, B:48:0x00ed, B:51:0x00f9, B:53:0x0105, B:55:0x0127, B:59:0x014c, B:60:0x0130, B:62:0x0140, B:66:0x0155, B:68:0x0161, B:71:0x0173, B:73:0x017f, B:76:0x0191, B:78:0x019d, B:81:0x01b1, B:83:0x01bd, B:86:0x01d1, B:88:0x01dd, B:91:0x01f1, B:93:0x01fd, B:96:0x0211, B:98:0x021d, B:101:0x0231, B:103:0x023d, B:106:0x0251, B:109:0x0271, B:112:0x027d, B:114:0x0283, B:117:0x0294, B:119:0x029a, B:123:0x02b2, B:125:0x02b8, B:128:0x02c9, B:130:0x02cf, B:140:0x02a3, B:144:0x02ea, B:146:0x02f8), top: B:7:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:130:0x02cf A[Catch: all -> 0x0334, TryCatch #0 {, blocks: (B:8:0x002a, B:10:0x0037, B:13:0x0045, B:15:0x0051, B:18:0x0308, B:20:0x0320, B:21:0x032d, B:25:0x0063, B:27:0x006f, B:30:0x0081, B:32:0x008e, B:34:0x009a, B:36:0x00a6, B:39:0x00b4, B:41:0x00c1, B:44:0x00cf, B:46:0x00db, B:48:0x00ed, B:51:0x00f9, B:53:0x0105, B:55:0x0127, B:59:0x014c, B:60:0x0130, B:62:0x0140, B:66:0x0155, B:68:0x0161, B:71:0x0173, B:73:0x017f, B:76:0x0191, B:78:0x019d, B:81:0x01b1, B:83:0x01bd, B:86:0x01d1, B:88:0x01dd, B:91:0x01f1, B:93:0x01fd, B:96:0x0211, B:98:0x021d, B:101:0x0231, B:103:0x023d, B:106:0x0251, B:109:0x0271, B:112:0x027d, B:114:0x0283, B:117:0x0294, B:119:0x029a, B:123:0x02b2, B:125:0x02b8, B:128:0x02c9, B:130:0x02cf, B:140:0x02a3, B:144:0x02ea, B:146:0x02f8), top: B:7:0x002a }] */
        /* JADX WARN: Removed duplicated region for block: B:134:0x02e1 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0320 A[Catch: all -> 0x0334, TryCatch #0 {, blocks: (B:8:0x002a, B:10:0x0037, B:13:0x0045, B:15:0x0051, B:18:0x0308, B:20:0x0320, B:21:0x032d, B:25:0x0063, B:27:0x006f, B:30:0x0081, B:32:0x008e, B:34:0x009a, B:36:0x00a6, B:39:0x00b4, B:41:0x00c1, B:44:0x00cf, B:46:0x00db, B:48:0x00ed, B:51:0x00f9, B:53:0x0105, B:55:0x0127, B:59:0x014c, B:60:0x0130, B:62:0x0140, B:66:0x0155, B:68:0x0161, B:71:0x0173, B:73:0x017f, B:76:0x0191, B:78:0x019d, B:81:0x01b1, B:83:0x01bd, B:86:0x01d1, B:88:0x01dd, B:91:0x01f1, B:93:0x01fd, B:96:0x0211, B:98:0x021d, B:101:0x0231, B:103:0x023d, B:106:0x0251, B:109:0x0271, B:112:0x027d, B:114:0x0283, B:117:0x0294, B:119:0x029a, B:123:0x02b2, B:125:0x02b8, B:128:0x02c9, B:130:0x02cf, B:140:0x02a3, B:144:0x02ea, B:146:0x02f8), top: B:7:0x002a }] */
        @Override // android.database.ContentObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onChange(boolean z10, Uri uri) {
            boolean z11;
            LocalLog.l("PowerSaveConfig", "uri change" + uri);
            super.onChange(z10, uri);
            if (PowerSaveConfig.this.f18154b) {
                String str = null;
                synchronized (PowerSaveConfig.this.f18160h) {
                    if (!uri.equals(Settings.System.getUriFor("oplus_customize_aon_gesture_answer_phone_state")) && !uri.equals(Settings.System.getUriFor("oplus_customize_aon_gesture_swip_screen_state"))) {
                        if (uri.equals(Settings.Secure.getUriFor("Setting_AodSwitchEnable"))) {
                            if (PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "Setting_AodSwitchEnable") != 0) {
                                str = "oplus_power_save_aod_state";
                            }
                        } else if (uri.equals(Settings.Secure.getUriFor("Setting_Sub_AodSwitchEnable"))) {
                            if (PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "Setting_Sub_AodSwitchEnable") != 0) {
                                str = "oplus_power_save_sub_aod_state";
                            }
                        } else {
                            boolean z12 = true;
                            if (!uri.equals(Settings.Secure.getUriFor("customize_multimedia_video_super_resolution")) && !uri.equals(Settings.Secure.getUriFor("osie_video_display_switch")) && !uri.equals(Settings.Secure.getUriFor("osie_motion_fluency_switch")) && !uri.equals(Settings.Secure.getUriFor("osie_iris5_switch"))) {
                                if (!uri.equals(Settings.Secure.getUriFor("oplus_customize_aod_curved_display_notification_switch")) && !uri.equals(Settings.Secure.getUriFor("oplus_customize_comm_incallui_curved_display_notification_color"))) {
                                    if (uri.equals(Settings.Global.getUriFor("oppo_comm_incallui_curved_display_call_color"))) {
                                        String stringForUser = Settings.Global.getStringForUser(PowerSaveConfig.this.f18157e.getContentResolver(), "oppo_comm_incallui_curved_display_call_color", -2);
                                        if (stringForUser != null && !stringForUser.equals("none")) {
                                            str = "oplus_power_save_call_ui_curved_display_state";
                                        }
                                    } else if (uri.equals(Settings.Secure.getUriFor("setting_app_startup_anim_speed"))) {
                                        String stringForUser2 = Settings.Secure.getStringForUser(PowerSaveConfig.this.f18157e.getContentResolver(), "setting_app_startup_anim_speed", -2);
                                        if ((PowerUsageManager.x(PowerSaveConfig.this.f18157e).r() > 20 || "0".equals(stringForUser2)) && (PowerUsageManager.x(PowerSaveConfig.this.f18157e).r() <= 20 || "1".equals(stringForUser2))) {
                                            z12 = false;
                                        }
                                        if (z12) {
                                            PowerSaveConfig.this.f18156d = false;
                                            str = "oplus_power_save_anim_state";
                                        }
                                    } else if (uri.equals(Settings.System.getUriFor("keyguard_notice_wakelock_state"))) {
                                        if (PowerSaveUtils.d(PowerSaveConfig.this.f18157e, "keyguard_notice_wakelock_state") != 0) {
                                            str = "oplus_power_save_keyguard_notice_state";
                                        }
                                    } else if (uri.equals(Settings.System.getUriFor("screen_brightness_mode"))) {
                                        if (PowerSaveUtils.d(PowerSaveConfig.this.f18157e, "screen_brightness_mode") != 1) {
                                            str = "oplus_power_save_auto_brightness_state";
                                        }
                                    } else if (uri.equals(Settings.System.getUriFor("dtmf_tone"))) {
                                        if (PowerSaveUtils.a(PowerSaveConfig.this.f18157e, "dtmf_tone", "oplus_power_save_dtmf_tone", true, false)) {
                                            str = "oplus_power_save_dtmf_tone";
                                        }
                                    } else if (uri.equals(Settings.System.getUriFor("lockscreen_sounds_enabled"))) {
                                        if (PowerSaveUtils.a(PowerSaveConfig.this.f18157e, "lockscreen_sounds_enabled", "oplus_power_save_lockscreen_sounds_enabled", true, false)) {
                                            str = "oplus_power_save_lockscreen_sounds_enabled";
                                        }
                                    } else if (uri.equals(Settings.Secure.getUriFor("disable_screen_capture_sound"))) {
                                        if (PowerSaveUtils.a(PowerSaveConfig.this.f18157e, "disable_screen_capture_sound", "oplus_power_save_disable_screen_capture_sound", false, true)) {
                                            str = "oplus_power_save_disable_screen_capture_sound";
                                        }
                                    } else if (uri.equals(Settings.Secure.getUriFor("global_delete_sound"))) {
                                        if (PowerSaveUtils.a(PowerSaveConfig.this.f18157e, "global_delete_sound", "oplus_power_save_global_delete_sound", false, false)) {
                                            str = "oplus_power_save_global_delete_sound";
                                        }
                                    } else if (uri.equals(Settings.Secure.getUriFor("disable_front_finger_sound"))) {
                                        if (PowerSaveUtils.a(PowerSaveConfig.this.f18157e, "disable_front_finger_sound", "oplus_power_save_disable_front_finger_sound", false, true)) {
                                            str = "oplus_power_save_disable_front_finger_sound";
                                        }
                                    } else if (uri.equals(Settings.System.getUriFor("sound_effects_enabled")) && PowerSaveUtils.a(PowerSaveConfig.this.f18157e, "sound_effects_enabled", "oplus_power_save_sound_effects_enabled", true, false)) {
                                        str = "oplus_power_save_sound_effects_enabled";
                                    }
                                }
                                String stringForUser3 = Settings.Secure.getStringForUser(PowerSaveConfig.this.f18157e.getContentResolver(), "oplus_customize_comm_incallui_curved_display_notification_color", -2);
                                if (PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "oplus_customize_aod_curved_display_notification_switch") != 0 || (stringForUser3 != null && !"".equals(stringForUser3))) {
                                    str = "oplus_power_save_curved_display_state";
                                }
                            }
                            boolean z13 = y5.b.w() && PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "customize_multimedia_video_super_resolution") == 1;
                            if (!y5.b.q()) {
                                if (y5.b.v()) {
                                }
                                z11 = false;
                                boolean z14 = !y5.b.q() && PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "osie_motion_fluency_switch") == 1;
                                if (PowerSaveUtils.s() || PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "osie_iris5_switch") != 1) {
                                    z12 = false;
                                }
                                if (!z13 || z11 || z14 || z12) {
                                    str = "oplus_power_save_osie_state";
                                }
                            }
                            if (PowerSaveUtils.c(PowerSaveConfig.this.f18157e, "osie_video_display_switch") == 1) {
                                z11 = true;
                                if (y5.b.q()) {
                                }
                                if (PowerSaveUtils.s()) {
                                }
                                z12 = false;
                                if (!z13) {
                                }
                                str = "oplus_power_save_osie_state";
                            }
                            z11 = false;
                            if (y5.b.q()) {
                            }
                            if (PowerSaveUtils.s()) {
                            }
                            z12 = false;
                            if (!z13) {
                            }
                            str = "oplus_power_save_osie_state";
                        }
                        LocalLog.l("PowerSaveConfig", "not return, switchChangeType: " + str);
                        if (str != null) {
                            PowerSaveConfig.this.f18153a.put(str, 0);
                        }
                    }
                    if (PowerSaveUtils.d(PowerSaveConfig.this.f18157e, "oplus_customize_aon_gesture_answer_phone_state") != 0 || PowerSaveUtils.d(PowerSaveConfig.this.f18157e, "oplus_customize_aon_gesture_swip_screen_state") != 0) {
                        str = "oplus_power_save_aon_state";
                    }
                    LocalLog.l("PowerSaveConfig", "not return, switchChangeType: " + str);
                    if (str != null) {
                    }
                }
                PowerSaveConfig.this.n();
            }
        }
    }

    private PowerSaveConfig(Context context) {
        this.f18159g = null;
        this.f18157e = context;
        HandlerThread handlerThread = new HandlerThread("PowerSaveConfig");
        handlerThread.start();
        this.f18159g = new Handler(handlerThread.getLooper());
        i();
        j();
        m();
    }

    public static PowerSaveConfig f(Context context) {
        if (f18152i == null) {
            synchronized (PowerSaveConfig.class) {
                if (f18152i == null) {
                    f18152i = new PowerSaveConfig(context);
                }
            }
        }
        return f18152i;
    }

    private void i() {
        synchronized (this.f18160h) {
            HashMap<String, Integer> C1 = f.C1(this.f18157e, false);
            this.f18153a = C1;
            if (C1.size() == 0) {
                h();
            }
        }
    }

    private void j() {
        this.f18158f = new a(this.f18159g);
    }

    public HashMap<String, Integer> g() {
        HashMap<String, Integer> hashMap;
        synchronized (this.f18160h) {
            if (this.f18155c) {
                this.f18153a = f.C1(this.f18157e, false);
                this.f18155c = false;
            }
            hashMap = this.f18153a;
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void h() {
        synchronized (this.f18160h) {
            this.f18153a.put("oplus_power_save_aon_state", 0);
            this.f18153a.put("oplus_power_save_aod_state", 0);
            this.f18153a.put("oplus_power_save_sub_aod_state", 0);
            this.f18153a.put("oplus_power_save_osie_state", 0);
            this.f18153a.put("oplus_power_save_curved_display_state", 0);
            this.f18153a.put("oplus_power_save_call_ui_curved_display_state", 0);
            this.f18153a.put("oplus_power_save_anim_state", 0);
            this.f18153a.put("oplus_power_save_keyguard_notice_state", 0);
            this.f18153a.put("oplus_power_save_auto_brightness_state", 0);
            this.f18153a.put("oplus_power_save_dtmf_tone", 0);
            this.f18153a.put("oplus_power_save_lockscreen_sounds_enabled", 0);
            this.f18153a.put("oplus_power_save_disable_screen_capture_sound", 0);
            this.f18153a.put("oplus_power_save_global_delete_sound", 0);
            this.f18153a.put("oplus_power_save_disable_front_finger_sound", 0);
            this.f18153a.put("oplus_power_save_sound_effects_enabled", 0);
            f.D3(this.f18153a, this.f18157e);
            this.f18155c = true;
        }
    }

    public boolean k() {
        return this.f18156d;
    }

    public void l(boolean z10) {
        LocalLog.a("PowerSaveConfig", "notifyChanged is open:" + z10);
        this.f18154b = z10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void m() {
        this.f18157e.getContentResolver().registerContentObserver(Settings.System.getUriFor("oplus_customize_aon_gesture_answer_phone_state"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.System.getUriFor("oplus_customize_aon_gesture_swip_screen_state"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("Setting_AodSwitchEnable"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("Setting_Sub_AodSwitchEnable"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("oplus_customize_aod_curved_display_notification_switch"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("oplus_customize_comm_incallui_curved_display_notification_color"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Global.getUriFor("oppo_comm_incallui_curved_display_call_color"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("setting_app_startup_anim_speed"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.System.getUriFor("keyguard_notice_wakelock_state"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.System.getUriFor("dtmf_tone"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.System.getUriFor("lockscreen_sounds_enabled"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("disable_screen_capture_sound"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("global_delete_sound"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("disable_front_finger_sound"), false, this.f18158f);
        this.f18157e.getContentResolver().registerContentObserver(Settings.System.getUriFor("sound_effects_enabled"), false, this.f18158f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void n() {
        synchronized (this.f18160h) {
            f.D3(this.f18153a, this.f18157e);
        }
    }

    public void o(boolean z10) {
        this.f18156d = z10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p() {
        this.f18157e.getContentResolver().unregisterContentObserver(this.f18158f);
    }
}
