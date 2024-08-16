package b9;

import android.app.AppOpsManager;
import android.app.OplusWhiteListManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IContentProvider;
import android.content.pm.ApplicationInfo;
import android.content.pm.OplusPackageManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IDeviceIdleController;
import android.os.RemoteException;
import android.util.Log;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.util.OplusCommonConfig;
import f6.f;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import w5.OplusBatteryConstants;
import y5.AppFeature;

/* compiled from: PowerUtils.java */
/* renamed from: b9.d, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final Uri f4621a = Uri.parse("content://com.oplus.startup.provider");

    /* renamed from: b, reason: collision with root package name */
    private static final Uri f4622b = Uri.parse("content://com.oplus.startup.provider");

    public static void a(IDeviceIdleController iDeviceIdleController, String str, Context context) {
        try {
            iDeviceIdleController.addPowerSaveWhitelistApp(str);
            LocalLog.a("PowerUtils", "addDozeWhitelist: pkg=" + str);
        } catch (Exception e10) {
            LocalLog.c("PowerUtils", "addDozeWhitelist: Exception! ", e10);
        }
    }

    public static String b(long j10) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        long j11 = j10 / 1073741824;
        if (j11 >= 1) {
            return decimalFormat.format(j11) + " GB";
        }
        long j12 = j10 / 1048576;
        if (j12 >= 1) {
            return decimalFormat.format(j12) + " MB";
        }
        long j13 = j10 / 1024;
        if (j13 >= 1) {
            return decimalFormat.format(j13) + " KB";
        }
        return String.valueOf(0);
    }

    public static int c(Context context, String str) {
        int f10 = f(context, str, "getAssociateStartState");
        Log.d("PowerUtils", "getAssociateState Pkg=" + str + " hide=" + (((f10 & 1) == 0 && (f10 & 8) == 0) ? false : true) + " enable=" + (f10 == 4));
        return f10;
    }

    public static int d(Context context, String str) {
        int f10 = f(context, str, "getStartupState");
        Log.d("PowerUtils", "getAutoStartState Pkg=" + str + " hide=" + (((f10 & 1) == 0 && (f10 & 8) == 0) ? false : true) + " enable=" + (f10 == 4) + " state:" + f10);
        return f10;
    }

    public static Bundle e(String str, PowerSipper powerSipper) {
        int[] iArr;
        double[] dArr;
        Bundle bundle = new Bundle();
        bundle.putString("title", str);
        if (powerSipper == null) {
            bundle.putIntArray("types", new int[]{R.string.usage_type_foreground_activity, R.string.usage_type_background_activity, R.string.usage_type_computed_power});
            bundle.putDoubleArray("values", new double[3]);
            return bundle;
        }
        if (powerSipper.f4601e.equals("APP")) {
            iArr = new int[]{R.string.usage_type_foreground_activity, R.string.usage_type_background_activity, R.string.battery_detail_view_keep_active_time, R.string.battery_detail_view_wlan_upload_stream, R.string.battery_detail_view_wlan_download_stream, R.string.usage_type_computed_power};
            dArr = new double[]{powerSipper.f4608l, powerSipper.f4609m, powerSipper.f4610n, powerSipper.f4611o, powerSipper.f4612p, powerSipper.f4613q};
        } else if (powerSipper.f4601e.equals("SMALLAPP")) {
            iArr = new int[]{R.string.usage_type_on_time, R.string.usage_type_computed_power};
            dArr = new double[]{powerSipper.f4607k, powerSipper.f4613q};
        } else {
            iArr = new int[]{R.string.usage_type_on_time, R.string.usage_type_computed_power};
            dArr = new double[]{powerSipper.f4607k, powerSipper.f4613q};
        }
        bundle.putIntArray("types", iArr);
        bundle.putDoubleArray("values", dArr);
        return bundle;
    }

    private static int f(Context context, String str, String str2) {
        Bundle bundle = new Bundle();
        IContentProvider iContentProvider = null;
        try {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = f4621a;
                IContentProvider acquireUnstableProvider = contentResolver.acquireUnstableProvider(uri);
                if (acquireUnstableProvider != null) {
                    try {
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(str);
                        bundle.putStringArrayList("packageList", arrayList);
                        Bundle call = acquireUnstableProvider.call(context.getAttributionSource(), uri.getAuthority(), str2, (String) null, bundle);
                        Set<String> keySet = call.keySet();
                        if (keySet.contains(str)) {
                            String concat = str.concat("_canBeOpened");
                            int i10 = ((Boolean) call.get(str)).booleanValue() ? 4 : 2;
                            if (keySet.contains(concat)) {
                                if (!((Boolean) call.get(concat)).booleanValue()) {
                                    i10 |= 8;
                                }
                            }
                            context.getContentResolver().releaseUnstableProvider(acquireUnstableProvider);
                            return i10;
                        }
                    } catch (Exception e10) {
                        e = e10;
                        iContentProvider = acquireUnstableProvider;
                        Log.e("PowerUtils", "getState " + e);
                        if (iContentProvider == null) {
                            return 1;
                        }
                        context.getContentResolver().releaseUnstableProvider(iContentProvider);
                        return 1;
                    } catch (Throwable th) {
                        th = th;
                        iContentProvider = acquireUnstableProvider;
                        if (iContentProvider != null) {
                            context.getContentResolver().releaseUnstableProvider(iContentProvider);
                        }
                        throw th;
                    }
                }
                if (acquireUnstableProvider == null) {
                    return 1;
                }
                context.getContentResolver().releaseUnstableProvider(acquireUnstableProvider);
                return 1;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e11) {
            e = e11;
        }
    }

    public static boolean g(String str) {
        List<String> a10 = AppFeature.a();
        if (a10 == null || !a10.contains(str)) {
            return false;
        }
        LocalLog.a("PowerUtils", "isCustomWhitelsit: pkg=" + str);
        return true;
    }

    public static boolean h(IDeviceIdleController iDeviceIdleController, String str) {
        boolean z10;
        try {
            z10 = iDeviceIdleController.isPowerSaveWhitelistApp(str);
        } catch (Exception e10) {
            e10.printStackTrace();
            z10 = false;
        }
        LocalLog.a("PowerUtils", "isDozeWhiteListApp: " + z10);
        return z10;
    }

    public static boolean i(Context context, String str) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
            if ((applicationInfo.flags & 1) != 0) {
                Log.d("PowerUtils", "PowerLimitDisable FLAG SYSTEM!");
                return false;
            }
            if ((applicationInfo.enabled || f.m1(applicationInfo, context)) ? false : true) {
                Log.d("PowerUtils", "PowerLimitDisable app disable!");
                return false;
            }
            if (new OplusWhiteListManager(context).getGlobalWhiteList().contains(str)) {
                Log.d("PowerUtils", "PowerLimitDisable: OplusWhiteList pkg=" + str);
                return false;
            }
            if (OplusBatteryConstants.f19362n.contains(str)) {
                Log.d("PowerUtils", "PowerLimitDisable: POWER_CONTROL_SYS_DEFAULT_LIST pkg=" + str);
                return false;
            }
            Log.d("PowerUtils", "PowerLimitEnable: pkg=" + str);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("PowerUtils", "PowerLimitDisable applicationInfo not found!");
            return false;
        }
    }

    public static void j(IDeviceIdleController iDeviceIdleController, String str, Context context) {
        try {
            iDeviceIdleController.removePowerSaveWhitelistApp(str);
            LocalLog.a("PowerUtils", "removeDozeWhitelist: pkg=" + str);
        } catch (Exception e10) {
            LocalLog.c("PowerUtils", "removeDozeWhitelist: Exception! ", e10);
        }
    }

    public static void k(String str, int i10, Context context) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(str, 128);
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.c("PowerUtils", "Cannot find package: " + str, e10);
            applicationInfo = null;
        }
        if (applicationInfo == null) {
            return;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
        if (applicationInfo.targetSdkVersion < 26) {
            appOpsManager.setMode(63, applicationInfo.uid, str, i10);
        }
        appOpsManager.setMode(70, applicationInfo.uid, str, i10);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0062, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005f, code lost:
    
        if (r1 == null) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String l(String str, String str2, boolean z10, Context context) {
        String str3 = "false";
        IContentProvider iContentProvider = null;
        try {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = f4621a;
                iContentProvider = contentResolver.acquireUnstableProvider(uri);
                if (iContentProvider != null) {
                    Bundle bundle = new Bundle();
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(str2);
                    bundle.putStringArrayList("packageList", arrayList);
                    bundle.putBoolean(str2, z10);
                    Bundle call = iContentProvider.call(context.getAttributionSource(), uri.getAuthority(), str, (String) null, bundle);
                    if (call != null) {
                        str3 = call.getString("returnValue");
                    }
                }
            } catch (Exception e10) {
                LocalLog.b("PowerUtils", "setStartUpList " + e10);
            }
        } finally {
            if (iContentProvider != null) {
                context.getContentResolver().releaseUnstableProvider(iContentProvider);
            }
        }
    }

    public static void m(String str, Context context) {
        try {
            new OplusPackageManager(context).oplusUnFreezePackage(str, context.getUserId(), 1, 0, context.getPackageName());
        } catch (RemoteException e10) {
            LocalLog.b("PowerUtils", "Fail to unFreezeApp e=" + e10);
        }
    }

    public static void n(String str, Context context, boolean z10) {
        Log.d("PowerUtils", "updateAssociateStartEnable:" + l("setAssociateStartState", str, z10, context.getApplicationContext()));
    }

    public static void o(String str, Context context, boolean z10) {
        Log.d("PowerUtils", "updateAutoStartUpEnable:" + l("setStartupState", str, z10, context.getApplicationContext()));
    }

    public static void p(List<String> list) {
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = new ArrayList<>(list);
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            Log.d("PowerUtils", "updateOneKeyWhiteListToAms " + it.next());
        }
        bundle.putStringArrayList("background_protect_list", arrayList);
        try {
            OplusCommonConfig.getInstance().putConfigInfo("background_protect_list", bundle, 1);
        } catch (Exception e10) {
            LocalLog.b("PowerUtils", "put guard protect list to ams get error: " + e10);
        } catch (NoSuchMethodError e11) {
            LocalLog.b("PowerUtils", "put guard protect list to ams get error: " + e11);
        }
    }
}
