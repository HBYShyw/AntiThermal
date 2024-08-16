package r8;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.OplusPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.Log;
import b6.LocalLog;
import b8.OplusIconUtils;
import b9.PowerSipper;
import com.oplus.battery.R;
import com.oplus.icon.OplusUxIconManager;
import com.oplus.multiapp.OplusMultiAppManager;
import f6.CommonUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import r8.BatterySipper;
import y5.AppFeature;

/* compiled from: BatterySipperUtils.java */
/* renamed from: r8.b, reason: use source file name */
/* loaded from: classes2.dex */
public class BatterySipperUtils {

    /* renamed from: a, reason: collision with root package name */
    private static String f17639a = "BatterySipperUtils";

    /* renamed from: b, reason: collision with root package name */
    private static final ArrayMap<String, Drawable> f17640b = new ArrayMap<>();

    /* renamed from: c, reason: collision with root package name */
    private static final Object f17641c = new Object();

    public static void a() {
        ArrayMap<String, Drawable> arrayMap = f17640b;
        synchronized (arrayMap) {
            arrayMap.clear();
        }
    }

    public static String b(double d10, Context context) {
        return context.getResources().getString(R.string.percentage, d10 < 1.0E-4d ? "0" : new DecimalFormat("#.##").format(d10));
    }

    public static List<PowerSipper> c(Context context, ArrayList<PowerSipper> arrayList, boolean z10) {
        ArrayList arrayList2 = new ArrayList();
        List<String> a10 = AppFeature.a();
        if (arrayList != null && arrayList.size() != 0) {
            ArrayList arrayList3 = new ArrayList();
            int i10 = 0;
            while (true) {
                if (i10 >= arrayList.size()) {
                    break;
                }
                PowerSipper powerSipper = arrayList.get(i10);
                Log.d(f17639a, z10 + " addPowerSipperPreference label=" + powerSipper.f4615s + " pkgName=" + powerSipper.f4603g + " power=" + powerSipper.f4613q + " percent=" + powerSipper.f4613q + " foreActTime=" + powerSipper.f4608l);
                if (z10 && powerSipper.f4608l < 1000) {
                    Log.d(f17639a, "skip sipper screenTime less than 1s");
                    break;
                }
                if (!z10 && (powerSipper.f4613q * 100.0d) / f6.f.n(context) < 0.1d) {
                    Log.d(f17639a, "skip sipper percent less than 0.1");
                    break;
                }
                if (a10 == null || a10.isEmpty() || !a10.contains(powerSipper.f4603g)) {
                    if ("USER".equals(powerSipper.f4601e)) {
                        if (powerSipper.f4616t != 999) {
                            powerSipper.f4614r = context.getResources().getDrawable(R.drawable.ic_multi_user);
                            try {
                                UserInfo userInfo = ((UserManager) context.getSystemService("user")).getUserInfo(powerSipper.f4616t);
                                if (userInfo != null) {
                                    powerSipper.f4618v = userInfo.name + "-" + context.getResources().getString(R.string.battery_ui_multi_user);
                                }
                            } catch (Exception e10) {
                                e10.printStackTrace();
                                Log.d(f17639a, "get user name error!");
                            }
                            if (z10) {
                                powerSipper.f4619w = d(powerSipper.f4608l, context);
                            } else {
                                powerSipper.f4619w = b((powerSipper.f4613q * 100.0d) / f6.f.n(context), context);
                            }
                            powerSipper.f4620x = false;
                            powerSipper.c(false);
                            arrayList2.add(powerSipper);
                            Log.d(f17639a, "addUserPowerSipperPreference");
                        }
                    } else {
                        if (BatterySipper.a.APP.toString().equals(powerSipper.f4601e)) {
                            e(context, powerSipper.f4602f, powerSipper);
                        } else {
                            f(context, powerSipper.f4601e, powerSipper);
                        }
                        if (arrayList3.contains(powerSipper.f4615s)) {
                            Log.d(f17639a, "addPowerSipperPreference skip double label=" + powerSipper.f4615s);
                        } else {
                            arrayList3.add(powerSipper.f4615s);
                            String str = powerSipper.f4603g;
                            if (str != null && !"".equals(str)) {
                                synchronized (f17641c) {
                                    Drawable drawable = powerSipper.f4614r;
                                    powerSipper.f4614r = drawable != null ? OplusIconUtils.b(context, drawable) : new ColorDrawable(0);
                                }
                                powerSipper.f4618v = powerSipper.f4615s;
                                if (z10) {
                                    powerSipper.f4619w = d(powerSipper.f4608l, context);
                                } else {
                                    powerSipper.f4619w = b((powerSipper.f4613q * 100.0d) / f6.f.n(context), context);
                                }
                                arrayList2.add(powerSipper);
                            } else {
                                Log.d(f17639a, "skip add sipper for null label, packageName=" + powerSipper.f4602f);
                            }
                        }
                    }
                }
                i10++;
            }
        }
        return arrayList2;
    }

    public static String d(long j10, Context context) {
        if (j10 < 0) {
            j10 = 0;
        }
        return CommonUtil.f(context, j10, true);
    }

    private static void e(Context context, String str, PowerSipper powerSipper) {
        ApplicationInfo applicationInfo;
        Drawable drawable;
        String str2 = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b(f17639a, "Fail to get application info e=" + e10);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            drawable = null;
        } else {
            if (!applicationInfo.enabled && !f6.f.m1(applicationInfo, context)) {
                LocalLog.a(f17639a, "addAppPreferenceHandle: disabled pkg=" + applicationInfo.packageName);
                return;
            }
            CharSequence loadLabel = applicationInfo.loadLabel(context.getPackageManager());
            if (loadLabel != null) {
                if (UserHandle.getUserId(powerSipper.f4616t) == 999) {
                    str2 = OplusMultiAppManager.getInstance().getMultiAppAlias(powerSipper.f4602f);
                } else {
                    str2 = f6.f.i(loadLabel.toString().trim());
                }
            }
            ArrayMap<String, Drawable> arrayMap = f17640b;
            synchronized (arrayMap) {
                Drawable drawable2 = arrayMap.get(str);
                if (drawable2 == null) {
                    drawable = new OplusPackageManager(context).getApplicationIconCache(applicationInfo);
                    arrayMap.put(str, drawable);
                } else {
                    drawable = drawable2;
                }
            }
            LocalLog.b(f17639a, "get icon" + drawable);
        }
        if (str2 == null || "".equals(str2)) {
            str2 = str;
        }
        if (drawable == null) {
            drawable = context.getResources().getDrawable(R.drawable.pm_power_usage_system);
        }
        if (drawable != null) {
            synchronized (f17641c) {
                drawable = OplusIconUtils.b(context, drawable);
            }
        }
        if ("mediaserver".equals(str)) {
            str = context.getResources().getString(R.string.process_mediaserver_label);
            drawable = context.getResources().getDrawable(R.drawable.pm_media_service);
        } else if ("dex2oat".equals(str)) {
            drawable = context.getResources().getDrawable(R.drawable.pm_power_usage_system);
        } else {
            str = str2;
        }
        powerSipper.f4615s = str;
        powerSipper.f4614r = drawable;
    }

    public static void f(Context context, String str, PowerSipper powerSipper) {
        String str2;
        int i10;
        Drawable uxIconDrawable;
        PackageManager packageManager = context.getPackageManager();
        if (BatterySipper.a.IDLE.toString().equals(str)) {
            str2 = context.getResources().getString(R.string.power_idle);
            i10 = R.drawable.pm_ic_settings_phone_idle;
        } else if (BatterySipper.a.CELL.toString().equals(str)) {
            str2 = context.getResources().getString(R.string.power_cell);
            i10 = R.drawable.pm_ic_settings_cell_standby;
        } else if (BatterySipper.a.PHONE.toString().equals(str)) {
            str2 = context.getResources().getString(R.string.power_phone);
            i10 = R.drawable.pm_ic_settings_voice_calls;
        } else if (BatterySipper.a.WIFI.toString().equals(str)) {
            str2 = context.getResources().getString(R.string.power_wifi);
            i10 = R.drawable.pm_ic_settings_wifi;
        } else if (BatterySipper.a.BLUETOOTH.toString().equals(str)) {
            str2 = context.getResources().getString(R.string.power_bluetooth);
            i10 = R.drawable.pm_ic_settings_bluetooth;
        } else if (BatterySipper.a.SCREEN.toString().equals(str)) {
            str2 = context.getResources().getString(R.string.power_screen);
            i10 = R.drawable.pm_ic_settings_display;
        } else {
            if (BatterySipper.a.FLASHLIGHT.toString().equals(str)) {
                str2 = context.getResources().getString(R.string.power_flashlight);
            } else if (BatterySipper.a.UNACCOUNTED.toString().equals(str)) {
                str2 = context.getResources().getString(R.string.power_other);
            } else if (BatterySipper.a.OVERCOUNTED.toString().equals(str)) {
                str2 = context.getResources().getString(R.string.power_other);
            } else if (BatterySipper.a.CAMERA.toString().equals(str)) {
                str2 = context.getResources().getString(R.string.power_camera);
            } else if (BatterySipper.a.AMBIENT_DISPLAY.toString().equals(str)) {
                str2 = context.getResources().getString(R.string.ambient_display_screen_title);
            } else {
                str2 = null;
                i10 = 0;
            }
            i10 = R.drawable.pm_power_usage_system;
        }
        if (i10 > 0) {
            uxIconDrawable = OplusUxIconManager.getUxIconDrawable(packageManager, context.getPackageName(), context.getResources().getDrawable(i10), false);
        } else {
            uxIconDrawable = OplusUxIconManager.getUxIconDrawable(packageManager, context.getPackageName(), context.getResources().getDrawable(R.drawable.pm_power_usage_system), false);
        }
        if (str2 == null) {
            str2 = context.getResources().getString(R.string.power_other);
        }
        powerSipper.f4615s = str2;
        powerSipper.f4614r = uxIconDrawable;
        Log.d(f17639a, "addPowerSipper label=" + str2 + " drainType=" + str);
    }
}
