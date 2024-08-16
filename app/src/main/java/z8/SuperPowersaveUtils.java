package z8;

import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Process;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import e6.GeneralShareprefHelper;
import f6.f;
import v4.GuardElfContext;

/* compiled from: SuperPowersaveUtils.java */
/* renamed from: z8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class SuperPowersaveUtils {

    /* renamed from: f, reason: collision with root package name */
    private static final Uri f20291f = Uri.parse("content://com.oplus.sceneservice.lightprovider");

    /* renamed from: g, reason: collision with root package name */
    private static final Uri f20292g = Uri.parse("content://com.oplus.gestureguide/black/setting");

    /* renamed from: h, reason: collision with root package name */
    private static final Uri f20293h = Uri.parse("content://com.oplus.appmanager.provider.db/settings_table");

    /* renamed from: i, reason: collision with root package name */
    private static volatile SuperPowersaveUtils f20294i = null;

    /* renamed from: a, reason: collision with root package name */
    private BluetoothAdapter f20295a;

    /* renamed from: b, reason: collision with root package name */
    private WifiManager f20296b;

    /* renamed from: c, reason: collision with root package name */
    private ConnectivityManager f20297c;

    /* renamed from: d, reason: collision with root package name */
    private UserHandle f20298d;

    /* renamed from: e, reason: collision with root package name */
    private Context f20299e;

    private SuperPowersaveUtils(Context context) {
        this.f20295a = null;
        this.f20298d = null;
        this.f20299e = context;
        this.f20295a = BluetoothAdapter.getDefaultAdapter();
        this.f20296b = (WifiManager) context.getSystemService(ThermalPolicy.KEY_WIFI);
        this.f20297c = (ConnectivityManager) this.f20299e.getSystemService("connectivity");
        this.f20298d = Process.myUserHandle();
    }

    public static boolean A(Context context) {
        return GeneralShareprefHelper.a(context, "superPowersaveSharePref", "is_bt_change", false);
    }

    public static boolean F(Context context) {
        return GeneralShareprefHelper.a(context, "superPowersaveSharePref", "is_gps_change", false);
    }

    private int G(Cursor cursor, String str) {
        if (cursor.getColumnIndex(str) >= 0) {
            return cursor.getColumnIndex(str);
        }
        return 0;
    }

    public static SuperPowersaveUtils H(Context context) {
        if (f20294i == null) {
            synchronized (SuperPowersaveUtils.class) {
                if (f20294i == null) {
                    f20294i = new SuperPowersaveUtils(context);
                }
            }
        }
        return f20294i;
    }

    public static void Q(Context context, String str, int i10) {
        if (context == null) {
            LocalLog.a("SuperPowersaveUtils", "put: context is null ");
            return;
        }
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("packageName", str);
            contentValues.put("startState", Integer.valueOf(i10));
            context.getContentResolver().insert(f20293h, contentValues);
        } catch (Exception unused) {
            LocalLog.a("SuperPowersaveUtils", "put: error !! can not insert !!");
        }
        LocalLog.a("SuperPowersaveUtils", "put: packageName = " + str + ", startState = " + i10);
    }

    public static void q0(Context context, boolean z10) {
        GeneralShareprefHelper.f(context, "superPowersaveSharePref", "is_bt_change", z10);
    }

    public static void v0(Context context, boolean z10) {
        GeneralShareprefHelper.f(context, "superPowersaveSharePref", "is_gps_change", z10);
    }

    private static void x0(int i10) {
        LocalLog.a("SuperPowersaveUtils", "setToggleState: state = " + i10);
        IBinder service = ServiceManager.getService("SurfaceFlinger");
        if (service != null) {
            Parcel obtain = Parcel.obtain();
            obtain.writeInterfaceToken("android.ui.ISurfaceComposer");
            obtain.writeInt(i10);
            LocalLog.a("SuperPowersaveUtils", "setToggleState: parse SurfaceFlinger");
            try {
                try {
                    service.transact(20011, obtain, null, 0);
                } catch (Exception e10) {
                    LocalLog.m("SuperPowersaveUtils", "setToggleState: failed", e10);
                }
                return;
            } finally {
                obtain.recycle();
            }
        }
        LocalLog.a("SuperPowersaveUtils", "setToggleState: get SurfaceFlinger Service failed");
    }

    public void A0(int i10) {
        Q(this.f20299e, "com.coloros.sceneservice", i10);
    }

    public int B() {
        return Settings.Global.getInt(this.f20299e.getContentResolver(), "oplus_customize_comm_incallui_side_notification_incomming_type", 3);
    }

    public void B0(boolean z10) {
        Settings.Secure.putIntForUser(this.f20299e.getContentResolver(), "gesture_mistouch_switch_app_enable", z10 ? 1 : 0, 0);
    }

    public int C() {
        return Settings.Global.getInt(this.f20299e.getContentResolver(), "oplus_customize_aod_curved_display_notification_switch", 0);
    }

    public void C0(boolean z10) {
        Settings.Global.putInt(this.f20299e.getContentResolver(), "wifi_scan_always_enabled", z10 ? 1 : 0);
    }

    public long D() {
        return GeneralShareprefHelper.c(this.f20299e, "superPowersaveSharePref", "enter_super_powersave_time", -1L);
    }

    public void D0() {
        try {
            this.f20299e.getContentResolver().call(f20291f, "", "", (Bundle) null);
        } catch (Exception e10) {
            LocalLog.b("SuperPowersaveUtils", "subscribeScene error:" + e10);
        }
    }

    public int E() {
        return Settings.Secure.getInt(this.f20299e.getContentResolver(), "location_mode", 0);
    }

    public boolean I() {
        boolean isKeyguardLocked = ((KeyguardManager) this.f20299e.getSystemService("keyguard")).isKeyguardLocked();
        boolean isInteractive = GuardElfContext.e().h().isInteractive();
        LocalLog.a("SuperPowersaveUtils", "lockflag " + isKeyguardLocked + ", screenflag " + isInteractive);
        return isInteractive && isKeyguardLocked;
    }

    public int J() {
        return Settings.Secure.getInt(this.f20299e.getContentResolver(), "oplus_customize_color_mode", 0);
    }

    public int K() {
        return Settings.Secure.getInt(this.f20299e.getContentResolver(), "osie_iris5_switch", 0);
    }

    public int L() {
        return M(this.f20299e, "com.coloros.sceneservice");
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x003e, code lost:
    
        if (r2 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005d, code lost:
    
        b6.LocalLog.a("SuperPowersaveUtils", "getStartstate: packageName does not exist");
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0062, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x005a, code lost:
    
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0058, code lost:
    
        if (0 == 0) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int M(Context context, String str) {
        if (context == null) {
            LocalLog.a("SuperPowersaveUtils", "getStartstate: context is null ");
            return -1;
        }
        Cursor cursor = null;
        try {
            try {
                cursor = context.getContentResolver().query(f20293h, null, "packageName = ?", new String[]{str}, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToNext();
                    int i10 = cursor.getInt(G(cursor, "startState"));
                    cursor.close();
                    return i10;
                }
                LocalLog.b("SuperPowersaveUtils", "getStartstate: cursor is null ");
            } catch (Exception e10) {
                LocalLog.b("SuperPowersaveUtils", "getStartstate exception: " + e10);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public boolean N() {
        return Settings.Secure.getIntForUser(this.f20299e.getContentResolver(), "gesture_mistouch_switch_app_enable", 0, 0) == 1;
    }

    public boolean O() {
        WifiManager wifiManager = this.f20296b;
        if (wifiManager != null) {
            return wifiManager.isWifiApEnabled();
        }
        return false;
    }

    public boolean P() {
        return Settings.Global.getInt(this.f20299e.getContentResolver(), "wifi_scan_always_enabled", 0) == 1;
    }

    public void R(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_auto_rotation_state", i10);
    }

    public void S(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_auto_sync_state", z10);
    }

    public void T(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_statusbar_int_percent_setting", i10);
    }

    public void U(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_screen_gesture_state", z10);
    }

    public void V(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_bluetooth_state", z10);
    }

    public void W(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_call_curved_display_state", i10);
    }

    public void X(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_curved_display_state", i10);
    }

    public void Y(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_gps_state", i10);
    }

    public void Z(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_oplus_color_mode_state", i10);
    }

    public void a() {
        ConnectivityManager connectivityManager = this.f20297c;
        if (connectivityManager != null) {
            connectivityManager.stopTethering(0);
        }
    }

    public void a0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_oplus_color_mode_set", z10);
    }

    public int b() {
        return Settings.System.getInt(this.f20299e.getContentResolver(), "accelerometer_rotation", 0);
    }

    public void b0(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_vision_effect_state", i10);
    }

    public boolean c() {
        return ContentResolver.getMasterSyncAutomaticallyAsUser(this.f20298d.getIdentifier());
    }

    public void c0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_powersave_screenofftime_switch_state", z10);
    }

    public int d() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_auto_rotation_state", 0);
    }

    public void d0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_powersave_auto_off_switch_state", z10);
    }

    public boolean e() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_auto_sync_state", false);
    }

    public void e0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_powersave_backlight_switch_state", z10);
    }

    public int f() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_statusbar_int_percent_setting", 0);
    }

    public void f0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_powersave_refresh_switch_state", z10);
    }

    public boolean g() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_screen_gesture_state", false);
    }

    public void g0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_powersave_state", z10);
    }

    public boolean h() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_bluetooth_state", false);
    }

    public void h0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_powersave_sync_switch_state", z10);
    }

    public int i() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_call_curved_display_state", 3);
    }

    public void i0(int i10) {
        GeneralShareprefHelper.d(this.f20299e, "superPowersaveSharePref", "backup_scene_service_start_state", i10);
    }

    public int j() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_curved_display_state", 0);
    }

    public void j0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_gesture_mistouch_switch", z10);
    }

    public int k() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_gps_state", 0);
    }

    public void k0(boolean z10) {
        GeneralShareprefHelper.f(this.f20299e, "superPowersaveSharePref", "backup_wifi_scan_always_state", z10);
    }

    public int l() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_oplus_color_mode_state", 0);
    }

    public void l0(int i10) {
        Settings.System.putInt(this.f20299e.getContentResolver(), "accelerometer_rotation", i10);
    }

    public boolean m() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_oplus_color_mode_set", false);
    }

    public void m0(boolean z10) {
        ContentResolver.setMasterSyncAutomaticallyAsUser(z10, this.f20298d.getIdentifier());
    }

    public int n() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_vision_effect_state", 0);
    }

    public void n0(int i10) {
        Settings.System.putIntForUser(this.f20299e.getContentResolver(), "display_power_percent", i10, -2);
    }

    public boolean o() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_powersave_screenofftime_switch_state", true);
    }

    public void o0(boolean z10) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", (Integer) 1);
        contentValues.put("open_type", Integer.valueOf(z10 ? 1 : 0));
        contentValues.put("select_type", (Integer) (-1));
        ContentResolver contentResolver = this.f20299e.getContentResolver();
        Uri uri = f20292g;
        if (contentResolver.update(uri, contentValues, null, null) > 0) {
            this.f20299e.getContentResolver().notifyChange(uri, null);
        }
    }

    public boolean p() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_powersave_auto_off_switch_state", true);
    }

    public void p0(boolean z10) {
        if (this.f20295a != null) {
            if (ContextCompat.a(this.f20299e, "android.permission.BLUETOOTH_CONNECT") != 0) {
                LocalLog.b("SuperPowersaveUtils", "BLUETOOTH_CONNECT Permission denied");
            } else if (z10) {
                this.f20295a.enable();
            } else {
                this.f20295a.disable();
            }
        }
    }

    public boolean q() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_powersave_backlight_switch_state", true);
    }

    public boolean r() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_powersave_refresh_switch_state", true);
    }

    public void r0(int i10) {
        Settings.Global.putInt(this.f20299e.getContentResolver(), "oplus_customize_comm_incallui_side_notification_incomming_type", i10);
    }

    public boolean s() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_powersave_state", false);
    }

    public void s0(int i10) {
        Settings.Global.putInt(this.f20299e.getContentResolver(), "oplus_customize_aod_curved_display_notification_switch", i10);
    }

    public boolean t() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_powersave_sync_switch_state", true);
    }

    public void t0(long j10) {
        GeneralShareprefHelper.e(this.f20299e, "superPowersaveSharePref", "enter_super_powersave_time", j10);
    }

    public int u() {
        return GeneralShareprefHelper.b(this.f20299e, "superPowersaveSharePref", "backup_scene_service_start_state", 0);
    }

    public void u0(int i10) {
        Settings.Secure.putInt(this.f20299e.getContentResolver(), "location_mode", i10);
    }

    public boolean v() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_gesture_mistouch_switch", false);
    }

    public boolean w() {
        return GeneralShareprefHelper.a(this.f20299e, "superPowersaveSharePref", "backup_wifi_scan_always_state", false);
    }

    public void w0(int i10) {
        Settings.Secure.putInt(this.f20299e.getContentResolver(), "oplus_customize_color_mode", i10);
    }

    public int x() {
        return Settings.System.getIntForUser(this.f20299e.getContentResolver(), "display_power_percent", f.D1("battery_percentage_setting_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), -2);
    }

    public boolean y() {
        int i10;
        Cursor cursor = null;
        try {
            try {
                cursor = this.f20299e.getContentResolver().query(f20292g, null, "id = 1", null, null);
                if (cursor == null || cursor.getCount() <= 0) {
                    i10 = 0;
                } else {
                    cursor.moveToFirst();
                    i10 = cursor.getInt(G(cursor, "open_type"));
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e10) {
                e10.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
                i10 = 0;
            }
            return i10 == 1;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public void y0(int i10) {
        x0(i10);
        Settings.Secure.putInt(this.f20299e.getContentResolver(), "osie_iris5_switch", i10);
    }

    public boolean z() {
        return Settings.Global.getInt(this.f20299e.getContentResolver(), "bluetooth_on", 0) == 1;
    }

    public void z0(Context context, boolean z10) {
        if (z10) {
            f.B2(context, 1);
            Settings.Global.putStringForUser(context.getContentResolver(), "battery_saver_constants", "soundtrigger_disabled=true,aod_disabled=true", 0);
        } else if (q()) {
            f.B2(context, 1);
            Settings.Global.putStringForUser(context.getContentResolver(), "battery_saver_constants", "soundtrigger_disabled=false,aod_disabled=false", 0);
        } else {
            f.B2(context, 0);
            Settings.Global.putStringForUser(context.getContentResolver(), "battery_saver_constants", "soundtrigger_disabled=false,aod_disabled=false", 0);
        }
    }
}
