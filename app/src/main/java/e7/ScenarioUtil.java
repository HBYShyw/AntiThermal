package e7;

import android.app.OplusNotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.telephony.OplusOSTelephonyManager;
import android.telephony.TelephonyManager;
import androidx.core.content.ContextCompat;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ScenarioUtil.java */
/* renamed from: e7.a, reason: use source file name */
/* loaded from: classes.dex */
public class ScenarioUtil {
    public static String a(Context context) {
        String parameters;
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, getActiveAudioPids, context is null");
            return null;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager == null || (parameters = audioManager.getParameters("get_pid")) == null || parameters.length() == 0) {
            return null;
        }
        return parameters;
    }

    public static String b(Context context) {
        String parameters;
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, getActiveRecordPids, context is null");
            return null;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (audioManager == null || (parameters = audioManager.getParameters("get_record_pid")) == null || parameters.length() == 0) {
            return null;
        }
        return parameters;
    }

    public static List<String> c() {
        ArrayList arrayList = new ArrayList();
        try {
            OplusNotificationManager oplusNotificationManager = new OplusNotificationManager();
            String[] enableNavigationApps = oplusNotificationManager.getEnableNavigationApps(-1);
            if (enableNavigationApps != null && enableNavigationApps.length > 0) {
                for (String str : enableNavigationApps) {
                    if (oplusNotificationManager.shouldKeepAlive(str, -2)) {
                        arrayList.add(str);
                    }
                }
            }
        } catch (Exception unused) {
            LocalLog.a("SmartDoze:ScenarioManager", "error, isNavigation Exception");
        }
        return arrayList;
    }

    public static boolean d(Context context) {
        if (context != null) {
            return b(context) != null;
        }
        LocalLog.b("SmartDoze:ScenarioManager", "error, isAudioIn, context is null");
        return false;
    }

    public static boolean e(Context context) {
        if (context != null) {
            return a(context) != null;
        }
        LocalLog.b("SmartDoze:ScenarioManager", "error, isAudioOut, context is null");
        return false;
    }

    public static boolean f(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, isBasedOnMtk, context is null");
            return false;
        }
        return context.getPackageManager().hasSystemFeature("oplus.hw.manufacturer.mtk");
    }

    public static boolean g(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, isBasedOnQcom, context is null");
            return false;
        }
        return context.getPackageManager().hasSystemFeature("oplus.hw.manufacturer.qualcomm");
    }

    public static boolean h(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error,isBluetoothConnected, context is null");
            return false;
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ContextCompat.a(context, "android.permission.BLUETOOTH_CONNECT") == 0 && defaultAdapter != null) {
            if (!defaultAdapter.isEnabled()) {
                return false;
            }
            int profileConnectionState = defaultAdapter.getProfileConnectionState(2);
            int profileConnectionState2 = defaultAdapter.getProfileConnectionState(1);
            int profileConnectionState3 = defaultAdapter.getProfileConnectionState(3);
            if (profileConnectionState != 2) {
                profileConnectionState = profileConnectionState2 == 2 ? profileConnectionState2 : profileConnectionState3 == 2 ? profileConnectionState3 : -1;
            }
            return (profileConnectionState == -1 && ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(7) == null) ? false : true;
        }
        LocalLog.b("SmartDoze:ScenarioManager", "adapter is null or BLUETOOTH_CONNECT Permission denied");
        return false;
    }

    public static boolean i(Context context) {
        if (k()) {
            LocalLog.a("SmartDoze:ScenarioManager", "in using mode,reason = navigating");
            return true;
        }
        if (context == null) {
            LocalLog.a("SmartDoze:ScenarioManager", "error, in using mode,reason = context_unknow");
            return true;
        }
        if (!d(context) && !e(context)) {
            if (h(context)) {
                LocalLog.a("SmartDoze:ScenarioManager", "in using mode,reason = BLE Connect");
                return true;
            }
            if (l(context)) {
                LocalLog.a("SmartDoze:ScenarioManager", "in using mode,reason = in call");
                return true;
            }
            if (!n(context)) {
                return false;
            }
            LocalLog.a("SmartDoze:ScenarioManager", "in using mode,reason = downloading");
            return true;
        }
        LocalLog.a("SmartDoze:ScenarioManager", "in using mode,reason = audio in/out");
        return true;
    }

    public static boolean j(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, isMtkGeminiSupport, context is null");
            return false;
        }
        return context.getPackageManager().hasSystemFeature("mtk.gemini.support");
    }

    public static boolean k() {
        return !c().isEmpty();
    }

    public static boolean l(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, isPhoneInCall, context is null");
            return false;
        }
        if (m(context)) {
            int phoneCount = TelephonyManager.getDefault().getPhoneCount();
            for (int i10 = 0; i10 < phoneCount; i10++) {
                if (OplusOSTelephonyManager.getDefault(context).getCallStateGemini(i10) != 0) {
                    LocalLog.a("SmartDoze:ScenarioManager", "Single card phone in call!!");
                }
            }
            return false;
        }
        int phoneCount2 = TelephonyManager.getDefault().getPhoneCount();
        for (int i11 = 0; i11 < phoneCount2; i11++) {
            if (OplusOSTelephonyManager.getDefault(context).getCallStateGemini(i11) != 0) {
                LocalLog.a("SmartDoze:ScenarioManager", "Phone in call!!");
            }
        }
        return false;
        return true;
    }

    public static boolean m(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, isSingleSimcardSlotDevice, context is null");
            return false;
        }
        if (g(context)) {
            return !TelephonyManager.getDefault().isMultiSimEnabled();
        }
        if (f(context)) {
            return !j(context);
        }
        return true;
    }

    public static boolean n(Context context) {
        if (context == null) {
            LocalLog.b("SmartDoze:ScenarioManager", "error, isUsingNetwork, context is null");
            return false;
        }
        ArrayList<String> a10 = TrafficUtil.a(context);
        LocalLog.a("SmartDoze:ScenarioManager", "netUsingList = " + a10);
        if (a10 == null) {
            return false;
        }
        return !a10.isEmpty();
    }
}
