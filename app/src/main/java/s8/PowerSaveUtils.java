package s8;

import android.content.ContentResolver;
import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import b6.LocalLog;
import com.oplus.osense.OsenseResClient;
import com.oplus.osense.info.OsenseNotifyRequest;
import t8.PowerUsageManager;

/* compiled from: PowerSaveUtils.java */
/* renamed from: s8.e, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerSaveUtils {
    public static void A(Context context, int i10, int i11) {
        try {
            r(context, "oplus_customize_aon_gesture_answer_phone_state", i10, b(context));
            r(context, "oplus_customize_aon_gesture_swip_screen_state", i11, b(context));
        } catch (Exception e10) {
            LocalLog.a("PowerSaveUtils", "setAONMode, failed to put mode:  The mode value maybe illegal!!");
            e10.printStackTrace();
        }
    }

    public static void B(Context context, String str) {
        try {
            k(context, "oppo_comm_incallui_curved_display_call_color", str, b(context));
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void C(Context context, String str) {
        try {
            p(context, "oplus_customize_aod_curved_display_notification_switch", 1, b(context));
            q(context, "oplus_customize_comm_incallui_curved_display_notification_color", str, b(context));
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void D(Context context) {
        r(context, "keyguard_notice_wakelock_state", 1, b(context));
    }

    public static void E(Context context, int i10, int i11, int i12, int i13) {
        if (i13 != 0) {
            m(context, true);
            return;
        }
        try {
            p(context, "customize_multimedia_video_super_resolution", i10, b(context));
            p(context, "osie_video_display_switch", i11, b(context));
            p(context, "osie_motion_fluency_switch", i12, b(context));
            p(context, "osie_iris5_switch", i10 | i11 | i12, b(context));
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void F(Context context, String str, boolean z10, boolean z11) {
        if (z10) {
            r(context, str, e(1, z11), b(context));
        } else {
            p(context, str, e(1, z11), b(context));
        }
    }

    public static void G(Context context) {
        p(context, "Setting_Sub_AodSwitchEnable", 1, b(context));
    }

    public static boolean a(Context context, String str, String str2, boolean z10, boolean z11) {
        if (z10) {
            return d(context, str) != e(0, z11);
        }
        if ("disable_front_finger_sound".equals(str) && c(context, str) == e(1, z11)) {
            return true;
        }
        return ("disable_front_finger_sound".equals(str) || c(context, str) == e(0, z11)) ? false : true;
    }

    private static int b(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "powersave_backup_user", 0, 0);
    }

    public static int c(Context context, String str) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), str, -1, -2);
    }

    public static int d(Context context, String str) {
        return Settings.System.getIntForUser(context.getContentResolver(), str, -1, -2);
    }

    public static int e(int i10, boolean z10) {
        if (z10) {
            if (i10 == 1) {
                return 0;
            }
        } else if (i10 != 1) {
            return 0;
        }
        return 1;
    }

    public static void f(Context context, String str, String str2, boolean z10, boolean z11) {
        if (z10) {
            if (d(context, str) != e(0, z11)) {
                r(context, str, e(0, z11), -2);
                g(context, str2, 1);
                return;
            }
            return;
        }
        if ("disable_front_finger_sound".equals(str) && c(context, str) == e(1, z11)) {
            p(context, str, e(0, z11), -2);
            g(context, str2, 1);
        }
        if ("disable_front_finger_sound".equals(str) || c(context, str) == e(0, z11)) {
            return;
        }
        p(context, str, e(0, z11), -2);
        g(context, str2, 1);
    }

    public static void g(Context context, String str, int i10) {
        LocalLog.l("PowerSaveUtils", "putStateToMap:" + str + i10);
        PowerSaveConfig.f(context).g().put(str, Integer.valueOf(i10));
    }

    public static void h(Context context) {
        Settings.System.putIntForUser(context.getContentResolver(), "screen_brightness_mode", 0, b(context));
    }

    public static void i(Context context, int i10) {
        if (i10 == 0) {
            return;
        }
        q(context, "setting_app_startup_anim_speed", i10 != 1 ? i10 != 2 ? "null" : "2" : "1", b(context));
        g(context, "oplus_power_save_anim_state", 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean j(Context context, int i10) {
        LocalLog.a("PowerSaveUtils", "setBackupUser userId:" + i10);
        return Settings.System.putIntForUser(context.getContentResolver(), "powersave_backup_user", i10, 0);
    }

    public static void k(Context context, String str, String str2, int i10) {
        try {
            Settings.Global.putStringForUser(context.getContentResolver(), str, str2, i10);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void l(Context context, boolean z10) {
        LocalLog.l("PowerSaveUtils", "setLowPowerMode:" + z10);
        OsenseNotifyRequest osenseNotifyRequest = new OsenseNotifyRequest(3, z10 ? 1 : 0);
        OsenseResClient osenseResClient = OsenseResClient.get(PowerSaveUtils.class);
        if (osenseResClient != null) {
            osenseResClient.osenseSetNotification(osenseNotifyRequest);
        } else {
            LocalLog.b("PowerSaveUtils", "OsenseResClient is null");
        }
    }

    public static void m(Context context, boolean z10) {
        n(context, z10 ? 1 : 0);
        Settings.Secure.putInt(context.getContentResolver(), "osie_iris5_switch", z10 ? 1 : 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void n(Context context, int i10) {
        boolean z10;
        LocalLog.a("PowerSaveUtils", "setToggleState: state = " + i10);
        IBinder service = ServiceManager.getService("SurfaceFlinger");
        if (service != null) {
            Parcel obtain = Parcel.obtain();
            obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
            obtain.writeInt(i10);
            LocalLog.a("PowerSaveUtils", "setToggleState: parse SurfaceFlinger");
            try {
                try {
                    z10 = service.transact(20011, obtain, null, 0);
                    try {
                        LocalLog.a("PowerSaveUtils", "setOsieToggleState: result: " + z10);
                    } catch (Exception e10) {
                        e = e10;
                        Log.w("PowerSaveUtils", "setToggleState: failed", e);
                        ContentResolver contentResolver = context.getContentResolver();
                        if (!z10) {
                        }
                        Settings.Secure.putIntForUser(contentResolver, "customize_multimedia_osie", i10, 0);
                    }
                } finally {
                    obtain.recycle();
                }
            } catch (Exception e11) {
                e = e11;
                z10 = false;
            }
        } else {
            LocalLog.a("PowerSaveUtils", "setToggleState: get SurfaceFlinger Service failed");
            z10 = false;
        }
        ContentResolver contentResolver2 = context.getContentResolver();
        if (!z10) {
            i10 = 0;
        }
        Settings.Secure.putIntForUser(contentResolver2, "customize_multimedia_osie", i10, 0);
    }

    public static void o(Context context) {
        String stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "setting_app_startup_anim_speed", -2);
        if (PowerUsageManager.x(context).r() > 20) {
            if (PowerUsageManager.x(context).r() > 20) {
                if ("2-385-470".equals(stringForUser) || "2".equals(stringForUser)) {
                    q(context, "setting_app_startup_anim_speed", "1", -2);
                    g(context, "oplus_power_save_anim_state", 2);
                    return;
                }
                return;
            }
            return;
        }
        if (stringForUser == null) {
            q(context, "setting_app_startup_anim_speed", "0", -2);
            g(context, "oplus_power_save_anim_state", -1);
            return;
        }
        if (stringForUser.equals("0-280-320") || stringForUser.equals("0")) {
            return;
        }
        q(context, "setting_app_startup_anim_speed", "0", -2);
        if (!stringForUser.equals("1-350-420") && !stringForUser.equals("1")) {
            if (stringForUser.equals("2-385-470") || stringForUser.equals("2")) {
                g(context, "oplus_power_save_anim_state", 2);
                return;
            }
            return;
        }
        g(context, "oplus_power_save_anim_state", 1);
    }

    public static void p(Context context, String str, int i10, int i11) {
        try {
            Settings.Secure.putIntForUser(context.getContentResolver(), str, i10, i11);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void q(Context context, String str, String str2, int i10) {
        try {
            Settings.Secure.putStringForUser(context.getContentResolver(), str, str2, i10);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static void r(Context context, String str, int i10, int i11) {
        try {
            Settings.System.putIntForUser(context.getContentResolver(), str, i10, i11);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }

    public static boolean s() {
        return y5.b.p() || "1".equals(SystemProperties.get("persist.sys.dpp.feature"));
    }

    public static void t(Context context) {
        if (c(context, "Setting_AodSwitchEnable") == 1) {
            try {
                p(context, "Setting_AodSwitchEnable", 0, -2);
            } catch (Exception e10) {
                e10.printStackTrace();
            }
            g(context, "oplus_power_save_aod_state", 1);
        }
    }

    public static void u(Context context) {
        if (d(context, "oplus_customize_aon_gesture_answer_phone_state") == 0 && d(context, "oplus_customize_aon_gesture_swip_screen_state") == 0) {
            return;
        }
        int i10 = d(context, "oplus_customize_aon_gesture_answer_phone_state") != 0 ? 1 : 0;
        if (d(context, "oplus_customize_aon_gesture_swip_screen_state") != 0) {
            i10 += 2;
        }
        try {
            r(context, "oplus_customize_aon_gesture_answer_phone_state", 0, -2);
            r(context, "oplus_customize_aon_gesture_swip_screen_state", 0, -2);
        } catch (Exception e10) {
            LocalLog.a("PowerSaveUtils", "setAONMode, failed to put mode:  The mode value maybe illegal!!");
            e10.printStackTrace();
        }
        g(context, "oplus_power_save_aon_state", i10);
    }

    public static void v(Context context) {
        String stringForUser = Settings.Global.getStringForUser(context.getContentResolver(), "oppo_comm_incallui_curved_display_call_color", -2);
        if (stringForUser == null || stringForUser.equals("none")) {
            return;
        }
        char c10 = 65535;
        switch (stringForUser.hashCode()) {
            case 112785:
                if (stringForUser.equals("red")) {
                    c10 = 0;
                    break;
                }
                break;
            case 3027034:
                if (stringForUser.equals("blue")) {
                    c10 = 1;
                    break;
                }
                break;
            case 3178592:
                if (stringForUser.equals("gold")) {
                    c10 = 2;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                g(context, "oplus_power_save_call_ui_curved_display_state", 2);
                break;
            case 1:
                g(context, "oplus_power_save_call_ui_curved_display_state", 4);
                break;
            case 2:
                g(context, "oplus_power_save_call_ui_curved_display_state", 3);
                break;
        }
        k(context, "oppo_comm_incallui_curved_display_call_color", "none", -2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0034, code lost:
    
        if (r1.equals("blue") == false) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void w(Context context) {
        String stringForUser;
        boolean z10 = true;
        if (c(context, "oplus_customize_aod_curved_display_notification_switch") != 1 || (stringForUser = Settings.Secure.getStringForUser(context.getContentResolver(), "oplus_customize_comm_incallui_curved_display_notification_color", -2)) == null) {
            return;
        }
        switch (stringForUser.hashCode()) {
            case 112785:
                if (stringForUser.equals("red")) {
                    z10 = false;
                    break;
                }
                z10 = -1;
                break;
            case 3027034:
                break;
            case 3178592:
                if (stringForUser.equals("gold")) {
                    z10 = 2;
                    break;
                }
                z10 = -1;
                break;
            default:
                z10 = -1;
                break;
        }
        switch (z10) {
            case false:
                g(context, "oplus_power_save_curved_display_state", 2);
                break;
            case true:
                g(context, "oplus_power_save_curved_display_state", 4);
                break;
            case true:
                g(context, "oplus_power_save_curved_display_state", 3);
                break;
        }
        p(context, "oplus_customize_aod_curved_display_notification_switch", 0, -2);
        q(context, "oplus_customize_comm_incallui_curved_display_notification_color", "", -2);
    }

    public static void x(Context context) {
        if (d(context, "keyguard_notice_wakelock_state") == 1) {
            r(context, "keyguard_notice_wakelock_state", 0, -2);
            g(context, "oplus_power_save_keyguard_notice_state", 1);
        }
    }

    public static void y(Context context) {
        if (c(context, "Setting_Sub_AodSwitchEnable") == 1) {
            p(context, "Setting_Sub_AodSwitchEnable", 0, -2);
            g(context, "oplus_power_save_sub_aod_state", 1);
        }
    }

    public static void z(Context context) {
        try {
            p(context, "Setting_AodSwitchEnable", 1, b(context));
            p(context, "Setting_AodEnable", 1, b(context));
        } catch (Exception e10) {
            e10.printStackTrace();
        }
    }
}
