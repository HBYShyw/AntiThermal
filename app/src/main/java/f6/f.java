package f6;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.OplusPackageManager;
import android.content.pm.PackageManager;
import android.os.IDeviceIdleController;
import android.os.OplusBatteryManager;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UidBatteryConsumer;
import android.os.UserHandle;
import android.os.customize.OplusCustomizeRestrictionManager;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import android.view.OplusBaseLayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.window.embedding.SplitController;
import b6.LocalLog;
import com.android.internal.os.PowerProfile;
import com.android.internal.policy.SystemBarUtils;
import com.android.internal.telephony.SmsApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oplus.battery.R;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.powermanager.fuelgaue.basic.customized.PowerSaveLevelPicker;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import com.oplus.settings.OplusSettings;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.util.OplusTypeCastingHelper;
import e6.SmartModeSharepref;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import r8.BatterySipper;
import t8.PowerUsageManager;
import u4.IRemoteGuardElfInterface;
import x5.UploadDataUtil;
import y5.AppFeature;
import z5.LocalFileUtil;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f11392a = true;

    /* renamed from: b, reason: collision with root package name */
    private static boolean f11393b = false;

    /* renamed from: c, reason: collision with root package name */
    private static final List<String> f11394c = Arrays.asList("screen_on_ps_on", "screen_on_ps_off", "screen_off_ps_on", "screen_off_ps_off");

    /* renamed from: d, reason: collision with root package name */
    private static List<String> f11395d = Arrays.asList("com.baidu.map.location", "com.oplus.pictorial", "com.oplus.battery");

    /* renamed from: e, reason: collision with root package name */
    private static List<Integer> f11396e = Arrays.asList(Integer.valueOf(DataTypeConstants.USER_ACTION));

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: Utils.java */
    /* loaded from: classes.dex */
    public class a extends TypeToken<Map<Integer, Integer>> {
        a() {
        }
    }

    /* compiled from: Utils.java */
    /* loaded from: classes.dex */
    class b implements Comparator<BatterySipper> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(BatterySipper batterySipper, BatterySipper batterySipper2) {
            return Double.compare(batterySipper2.f17617i, batterySipper.f17617i);
        }
    }

    public static boolean A(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "customize_charge_switch_state", 0, 0) == 1;
    }

    public static long A0(Context context) {
        return SmartModeSharepref.c(context, "smart_charge_notificate_time_be_running", 0L);
    }

    public static ArrayMap<String, Integer> A1(Context context) {
        String str = "battery" + File.separator + "kill_frequent_third_app.xml";
        try {
            InputStream readConfig = OplusSettings.readConfig(context, str, 0);
            if (readConfig != null) {
                return z1(readConfig);
            }
        } catch (Exception unused) {
            LocalLog.d("Utils", "readXMLFile  " + str + " got exception");
        }
        return new ArrayMap<>();
    }

    public static void A2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_backlight_state", i10, 0);
        LocalLog.l("Utils", "setPowerSaveBacklightSwitchState: state=" + i10);
    }

    public static void A3(HashMap<String, Integer> hashMap, Context context) {
        OutputStream e10 = LocalFileUtil.e("battery" + File.separator + "power_consum_app_first.xml", context);
        if (e10 == null) {
            LocalLog.a("Utils", "power_consum_opt_status: failed create file");
            return;
        }
        try {
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                newSerializer.setOutput(e10, "UTF-8");
                newSerializer.startDocument(null, Boolean.TRUE);
                newSerializer.startTag(null, "appFirst");
                if (LocalLog.g()) {
                    LocalLog.a("Utils", "map size = " + hashMap.keySet().size());
                }
                int i10 = 0;
                for (String str : hashMap.keySet()) {
                    String valueOf = String.valueOf(hashMap.get(str));
                    int i11 = i10 + 1;
                    if (i10 < 5 && LocalLog.g()) {
                        LocalLog.a("Utils", "key = " + str + " value = " + valueOf);
                    }
                    newSerializer.startTag(null, str);
                    newSerializer.text(valueOf);
                    newSerializer.endTag(null, str);
                    i10 = i11;
                }
                newSerializer.endTag(null, "appFirst");
                newSerializer.endDocument();
                newSerializer.flush();
            } catch (Throwable th) {
                try {
                    e10.close();
                } catch (IOException unused) {
                    LocalLog.a("Utils", "update switch status: failed close stream");
                }
                throw th;
            }
        } catch (IOException e11) {
            LocalLog.a("Utils", "updateAppfirstXml: " + e11.toString());
        }
        try {
            e10.close();
        } catch (IOException unused2) {
            LocalLog.a("Utils", "update switch status: failed close stream");
        }
    }

    public static boolean B(Context context) {
        int i10;
        try {
            i10 = Settings.Secure.getInt(context.getContentResolver(), "deepsleep_switch_state", D1("intelligent_deep_sleep_mode", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT));
        } catch (Exception e10) {
            LocalLog.a("Utils", "getDeepSleepModeSwitchState:  " + e10);
            i10 = 0;
        }
        LocalLog.l("Utils", "deepSleepNetOffSwitchState: " + i10);
        return 1 == i10;
    }

    public static int B0(Context context) {
        return SmartModeSharepref.b(context, "smart_charge_notificate_times_be_running", 0);
    }

    public static HashMap<String, Integer> B1(Context context) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        InputStream a10 = LocalFileUtil.a("battery" + File.separator + "power_consum_opt_status.xml", context);
        try {
            if (a10 == null) {
                return hashMap;
            }
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(a10, null);
                int i10 = AppFeature.D() ? 2 : 0;
                for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                    if (eventType == 2) {
                        String name = newPullParser.getName();
                        if (!"pkgStatus".equals(name)) {
                            if (newPullParser.next() != 1) {
                                i10 = Integer.parseInt(newPullParser.getText());
                            }
                            if (LocalLog.g()) {
                                LocalLog.a("Utils", "tag1 = " + name + " tag2 = " + i10);
                            }
                            hashMap.put(name, Integer.valueOf(i10));
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e10) {
                LocalLog.a("Utils", "readFromPCOXml: Got exception." + e10.toString());
            }
            try {
                a10.close();
            } catch (IOException unused) {
                LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception close xmlReader.");
            }
            return hashMap;
        } catch (Throwable th) {
            try {
                a10.close();
            } catch (IOException unused2) {
                LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception close xmlReader.");
            }
            throw th;
        }
    }

    public static void B2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_backlight_switch_state", i10, 0);
        LocalLog.l("Utils", "setPowerSaveBacklightSwitchState: state=" + i10);
    }

    public static void B3(ArrayMap<String, Integer> arrayMap, Context context) {
        OutputStream e10 = LocalFileUtil.e("battery" + File.separator + "kill_frequent_third_app.xml", context);
        if (e10 == null) {
            LocalLog.a("Utils", "updateMapFromFileLocked: failed create file");
            return;
        }
        try {
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                newSerializer.setOutput(e10, "UTF-8");
                newSerializer.startDocument(null, Boolean.TRUE);
                newSerializer.startTag(null, "gs");
                LocalLog.a("Utils", "map size = " + arrayMap.keySet().size());
                int i10 = 0;
                for (String str : arrayMap.keySet()) {
                    Integer num = arrayMap.get(str);
                    int i11 = i10 + 1;
                    if (i10 < 5) {
                        LocalLog.a("Utils", "key = " + str + " value = " + num);
                    }
                    if (str != null) {
                        newSerializer.startTag(null, "p");
                        newSerializer.attribute(null, "k", str);
                        newSerializer.attribute(null, "v", String.valueOf(num));
                        newSerializer.endTag(null, "p");
                    }
                    i10 = i11;
                }
                newSerializer.endTag(null, "gs");
                newSerializer.endDocument();
                newSerializer.flush();
            } catch (Throwable th) {
                try {
                    e10.close();
                } catch (IOException unused) {
                    LocalLog.a("Utils", "update switch status: failed close stream");
                }
                throw th;
            }
        } catch (IOException e11) {
            LocalLog.a("Utils", "updateMapFromFileLocked: " + e11.toString());
        }
        try {
            e10.close();
        } catch (IOException unused2) {
            LocalLog.a("Utils", "update switch status: failed close stream");
        }
    }

    public static String C(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService("telecom");
        if (telecomManager != null) {
            return telecomManager.getDefaultDialerPackage();
        }
        return null;
    }

    public static int C0(Context context) {
        return SmartModeSharepref.b(context, "smart_charge_notificate_times_journey_scene", -1);
    }

    public static HashMap<String, Integer> C1(Context context, boolean z10) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        InputStream b10 = LocalFileUtil.b(z10 ? "battery" + File.separator + "power_save_rus_config_list.xml" : "battery" + File.separator + "power_save_cfg.xml", context, 0);
        try {
            if (b10 == null) {
                return hashMap;
            }
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(b10, null);
                int i10 = AppFeature.D() ? 2 : 0;
                for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                    if (eventType == 2) {
                        String name = newPullParser.getName();
                        if (!"settingStatus".equals(name)) {
                            if (newPullParser.next() != 1 && newPullParser.getText() != null && !TextUtils.isEmpty(newPullParser.getText())) {
                                i10 = Integer.parseInt(newPullParser.getText());
                            }
                            if (LocalLog.g()) {
                                LocalLog.a("Utils", "tag1 = " + name + " tag2 = " + i10);
                            }
                            hashMap.put(name, Integer.valueOf(i10));
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e10) {
                LocalLog.a("Utils", "readFromPSCXml: Got exception." + e10.toString());
            }
            try {
                b10.close();
            } catch (IOException unused) {
                LocalLog.a("Utils", "readFromPowerSaveCfgXml: Got exception close xmlReader.");
            }
            return hashMap;
        } catch (Throwable th) {
            try {
                b10.close();
            } catch (IOException unused2) {
                LocalLog.a("Utils", "readFromPowerSaveCfgXml: Got exception close xmlReader.");
            }
            throw th;
        }
    }

    public static void C2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_rus_five_g_state", i10, 0);
        LocalLog.l("Utils", "setPowerSaveRUSFiveGState: state=" + i10);
    }

    public static void C3(HashMap<String, Integer> hashMap, Context context) {
        OutputStream e10 = LocalFileUtil.e("battery" + File.separator + "power_consum_opt_status.xml", context);
        if (e10 == null) {
            LocalLog.a("Utils", "power_consum_opt_status: failed create file");
            return;
        }
        try {
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                newSerializer.setOutput(e10, "UTF-8");
                newSerializer.startDocument(null, Boolean.TRUE);
                newSerializer.startTag(null, "pkgStatus");
                if (LocalLog.g()) {
                    LocalLog.a("Utils", "map size = " + hashMap.keySet().size());
                }
                int i10 = 0;
                for (String str : hashMap.keySet()) {
                    String valueOf = String.valueOf(hashMap.get(str));
                    int i11 = i10 + 1;
                    if (i10 < 5 && LocalLog.g()) {
                        LocalLog.a("Utils", "key = " + str + " value = " + valueOf);
                    }
                    newSerializer.startTag(null, str);
                    newSerializer.text(valueOf);
                    newSerializer.endTag(null, str);
                    i10 = i11;
                }
                newSerializer.endTag(null, "pkgStatus");
                newSerializer.endDocument();
                newSerializer.flush();
            } catch (Throwable th) {
                try {
                    e10.close();
                } catch (IOException unused) {
                    LocalLog.a("Utils", "update switch status: failed close stream");
                }
                throw th;
            }
        } catch (IOException e11) {
            LocalLog.a("Utils", "updatePCOxml: " + e11.toString());
        }
        try {
            e10.close();
        } catch (IOException unused2) {
            LocalLog.a("Utils", "update switch status: failed close stream");
        }
    }

    public static String D(Context context) {
        ComponentName defaultMmsApplication = SmsApplication.getDefaultMmsApplication(context, false);
        if (defaultMmsApplication != null) {
            return defaultMmsApplication.getPackageName();
        }
        return null;
    }

    public static int D0(Context context) {
        return SmartModeSharepref.b(context, "smart_charge_notificate_times_low_power_scene", -1);
    }

    public static int D1(String str, String str2) {
        int parseInt;
        for (int i10 = 0; i10 < CommonUtil.O().size(); i10++) {
            if (CommonUtil.O().get(i10).split(",")[0].equals(str)) {
                String str3 = CommonUtil.O().get(i10).split(",")[1];
                str2.hashCode();
                char c10 = 65535;
                switch (str2.hashCode()) {
                    case -1607594657:
                        if (str2.equals("endHour")) {
                            c10 = 0;
                            break;
                        }
                        break;
                    case -1298779273:
                        if (str2.equals("endMin")) {
                            c10 = 1;
                            break;
                        }
                        break;
                    case -1073191379:
                        if (str2.equals("beginHour")) {
                            c10 = 2;
                            break;
                        }
                        break;
                    case 1489406185:
                        if (str2.equals("beginMin")) {
                            c10 = 3;
                            break;
                        }
                        break;
                }
                switch (c10) {
                    case 0:
                        parseInt = Integer.parseInt(str3.split("-")[1].split(":")[0]);
                        break;
                    case 1:
                        parseInt = Integer.parseInt(str3.split("-")[1].split(":")[1]);
                        break;
                    case 2:
                        parseInt = Integer.parseInt(str3.split("-")[0].split(":")[0]);
                        break;
                    case 3:
                        parseInt = Integer.parseInt(str3.split("-")[0].split(":")[1]);
                        break;
                    default:
                        parseInt = Integer.parseInt(str3);
                        break;
                }
                return parseInt;
            }
        }
        return 0;
    }

    public static void D2(Context context, int i10) {
        LocalLog.l("Utils", "setPowerSaveRefresh: state" + i10);
        Settings.Secure.putIntForUser(context.getContentResolver(), "oplus_customize_screen_refresh_rate", i10, 0);
    }

    public static void D3(HashMap<String, Integer> hashMap, Context context) {
        int i10 = 0;
        OutputStream f10 = LocalFileUtil.f("battery" + File.separator + "power_save_cfg.xml", context, 0);
        if (f10 == null) {
            LocalLog.a("Utils", "power_save_cfg: failed create file");
            return;
        }
        try {
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                newSerializer.setOutput(f10, "UTF-8");
                newSerializer.startDocument(null, Boolean.TRUE);
                newSerializer.startTag(null, "settingStatus");
                if (LocalLog.g()) {
                    LocalLog.a("Utils", "map size = " + hashMap.keySet().size());
                }
                for (String str : hashMap.keySet()) {
                    String valueOf = String.valueOf(hashMap.get(str));
                    int i11 = i10 + 1;
                    if (i10 < 5 && LocalLog.g()) {
                        LocalLog.l("Utils", "key = " + str + " value = " + valueOf);
                    }
                    newSerializer.startTag(null, str);
                    newSerializer.text(valueOf);
                    newSerializer.endTag(null, str);
                    i10 = i11;
                }
                newSerializer.endTag(null, "settingStatus");
                newSerializer.endDocument();
                newSerializer.flush();
            } catch (Throwable th) {
                try {
                    f10.close();
                } catch (IOException unused) {
                    LocalLog.a("Utils", "update switch status: failed close stream");
                }
                throw th;
            }
        } catch (IOException e10) {
            LocalLog.a("Utils", "updatePSCxml: " + e10.toString());
        }
        try {
            f10.close();
        } catch (IOException unused2) {
            LocalLog.a("Utils", "update switch status: failed close stream");
        }
    }

    public static String E(Context context, BatterySipper batterySipper) {
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(batterySipper.b());
        if (packagesForUid == null || packagesForUid.length == 0) {
            return "";
        }
        int i10 = 0;
        for (int i11 = 0; i11 < packagesForUid.length; i11++) {
            if (packagesForUid[i11].equals(batterySipper.f17622n)) {
                i10 = i11;
            }
        }
        return packagesForUid[i10];
    }

    public static int E0(Context context) {
        return SmartModeSharepref.b(context, "smart_charge_notificate_times_screen_off_scene", -1);
    }

    public static void E1(IDeviceIdleController iDeviceIdleController, String str, Context context) {
        try {
            iDeviceIdleController.removePowerSaveWhitelistApp(str);
            LocalLog.a("Utils", "removeDozeWhitelist: pkg=" + str);
        } catch (Exception e10) {
            LocalLog.c("Utils", "removeDozeWhitelist: Exception! ", e10);
        }
    }

    public static void E2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_screen_refresh_rate", i10, 0);
    }

    public static void E3(String str, String str2, Context context) {
        int i10 = 0;
        while (true) {
            if (i10 >= CommonUtil.O().size()) {
                break;
            }
            if (str.equals(CommonUtil.O().get(i10).split(",")[0])) {
                CommonUtil.O().set(i10, str + "," + str2);
                break;
            }
            i10++;
        }
        F3(CommonUtil.O(), context);
    }

    public static boolean F(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "dialog_no_need_remind", 0, 0) == 1;
    }

    public static int F0(Context context) {
        return SmartModeSharepref.b(context, "smart_charge_notificate_times_morning_out_scene", -1);
    }

    public static void F1(Context context) {
        if (G(context) == 0 || L(context) == 0) {
            return;
        }
        l2(context, 0);
    }

    public static void F2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_screenoff_time_state", i10, 0);
    }

    public static void F3(List<String> list, Context context) {
        OutputStream e10 = LocalFileUtil.e("battery" + File.separator + "switch_status_config.xml", context);
        try {
            if (e10 == null) {
                LocalLog.a("Utils", "updateSwitchStatusList: failed create file");
                return;
            }
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                newSerializer.setOutput(e10, "UTF-8");
                newSerializer.startDocument(null, Boolean.TRUE);
                newSerializer.startTag(null, "switch");
                for (int i10 = 0; i10 < list.size(); i10++) {
                    String str = list.get(i10).split(",")[0];
                    String str2 = list.get(i10).split(",")[1];
                    newSerializer.startTag(null, str);
                    newSerializer.text(str2);
                    newSerializer.endTag(null, str);
                }
                newSerializer.endTag(null, "switch");
                newSerializer.endDocument();
                newSerializer.flush();
            } catch (Exception unused) {
                LocalLog.a("Utils", "update switch status: failed write file");
            }
            try {
                e10.close();
            } catch (IOException unused2) {
                LocalLog.a("Utils", "update switch status: failed close stream");
            }
        } catch (Throwable th) {
            try {
                e10.close();
            } catch (IOException unused3) {
                LocalLog.a("Utils", "update switch status: failed close stream");
            }
            throw th;
        }
    }

    public static int G(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "power_save_disable_five_g_state", D1("five_g_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT));
    }

    public static long G0(Context context) {
        return Settings.System.getLong(context.getContentResolver(), "smart_charge_protection_end_time", 0L);
    }

    public static void G1(Context context) {
        if (G(context) == 0 || L(context) == 1) {
            return;
        }
        l2(context, 1);
    }

    public static void G2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_sync_state", i10, 0);
    }

    private static void G3(Context context) {
        HashMap hashMap = new HashMap();
        String valueOf = String.valueOf(System.currentTimeMillis());
        String valueOf2 = String.valueOf(PowerUsageManager.x(context).r());
        hashMap.put("open_time", valueOf);
        hashMap.put("open_level", valueOf2);
        LocalLog.a("Utils", "uploadSpeedChargeData: eventMap=" + hashMap);
        UploadDataUtil.S0(context).W0("speed_charge_mode", hashMap, false);
    }

    public static List<String> H(Context context) {
        ArrayList arrayList = new ArrayList();
        InputStream a10 = LocalFileUtil.a("battery" + File.separator + "doze_wl_local.xml", context);
        try {
        } catch (IOException e10) {
            LocalLog.c("Utils", "getDozeDefaultWhiteList: Got execption close xmlReader. ", e10);
        }
        if (a10 == null) {
            return arrayList;
        }
        try {
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(a10, null);
                w1(newPullParser, arrayList);
                a10.close();
            } catch (Throwable th) {
                try {
                    a10.close();
                } catch (IOException e11) {
                    LocalLog.c("Utils", "getDozeDefaultWhiteList: Got execption close xmlReader. ", e11);
                }
                throw th;
            }
        } catch (Exception e12) {
            LocalLog.c("Utils", "getDozeDefaultWhiteList: Got execption. ", e12);
            a10.close();
        }
        return arrayList;
    }

    public static int H0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_charge_protection_status", 0, 0);
    }

    public static void H1(Context context) {
        if (G(context) == 0 || L(context) == 2) {
            return;
        }
        l2(context, 2);
    }

    public static void H2(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "power_save_disable_five_g_tips", i10);
        LocalLog.l("Utils", "setPowerSaveTipsState: state=" + i10);
    }

    private static void H3(OutputStream outputStream, List<String> list, List<String> list2) {
        StringBuilder sb2;
        if (outputStream == null) {
            return;
        }
        try {
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(outputStream, "UTF-8");
            newSerializer.startDocument(null, Boolean.TRUE);
            newSerializer.startTag(null, "gs");
            newSerializer.startTag(null, "filter-name");
            newSerializer.text("allow_prohibit");
            newSerializer.endTag(null, "filter-name");
            for (int i10 = 0; i10 < list.size(); i10++) {
                String str = list.get(i10);
                if (str != null) {
                    newSerializer.startTag(null, "allow");
                    newSerializer.text(str);
                    newSerializer.endTag(null, "allow");
                }
            }
            for (int i11 = 0; i11 < list2.size(); i11++) {
                String str2 = list2.get(i11);
                if (str2 != null) {
                    newSerializer.startTag(null, "prohibit");
                    newSerializer.text(str2);
                    newSerializer.endTag(null, "prohibit");
                }
            }
            newSerializer.endTag(null, "gs");
            newSerializer.endDocument();
            newSerializer.flush();
            try {
                outputStream.close();
            } catch (IOException e10) {
                e = e10;
                sb2 = new StringBuilder();
                sb2.append("Failed to close stream ");
                sb2.append(e);
                LocalLog.d("Utils", sb2.toString());
            }
        } catch (Throwable th) {
            try {
                LocalLog.d("Utils", "Fail to write list to colorProvider e=" + th);
                try {
                    outputStream.close();
                } catch (IOException e11) {
                    e = e11;
                    sb2 = new StringBuilder();
                    sb2.append("Failed to close stream ");
                    sb2.append(e);
                    LocalLog.d("Utils", sb2.toString());
                }
            } catch (Throwable th2) {
                try {
                    outputStream.close();
                } catch (IOException e12) {
                    LocalLog.d("Utils", "Failed to close stream " + e12);
                }
                throw th2;
            }
        }
    }

    public static boolean I() {
        return f11393b;
    }

    public static int I0(Context context) {
        int S0 = S0(context);
        ContentResolver contentResolver = context.getContentResolver();
        if (S0 == 2) {
            S0 = 1;
        }
        return Settings.System.getIntForUser(contentResolver, "smart_charge_protection_switch_state", S0, 0);
    }

    public static void I1(Context context) {
        context.sendBroadcastAsUser(new Intent("oplus.intent.action.RESET_BATTERY_STATS"), UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
        LocalLog.a("Utils", "resetBatteryStats");
    }

    public static void I2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "regular_charge_protection_status", i10, 0);
    }

    public static int J(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "Setting_SetTimeEndHour", D1("silent_mode_type_custom", "endHour"), 0);
    }

    public static boolean J0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_charge_switch_state", AppFeature.l() ? 1 : 0, 0) == 1;
    }

    public static void J1(Context context) {
        t2(context, System.currentTimeMillis());
        s2(context, 0L);
        M2(context, 0L);
    }

    public static void J2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "regular_charge_protection_switch_state", i10, 0);
    }

    public static int K(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "Setting_SetTimeEndMin", D1("silent_mode_type_custom", "endMin"), 0);
    }

    public static int K0(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "smart_long_charge_protection_deepthinker_total_times", 0);
    }

    @SuppressLint({"WrongConstant"})
    private static void K1(Context context) {
        List<String> H = H(context);
        if (H != null && !H.isEmpty()) {
            List list = null;
            IDeviceIdleController asInterface = IDeviceIdleController.Stub.asInterface(ServiceManager.getService("deviceidle"));
            try {
                list = Arrays.asList(asInterface.getUserPowerWhitelist());
            } catch (Exception unused) {
                LocalLog.a("Utils", "restoreAllDozeDefaultWhitelist: getUserPowerWhitelist Exception!");
            }
            if (list == null) {
                return;
            }
            for (int i10 = 0; i10 < H.size(); i10++) {
                String str = H.get(i10);
                if (!list.contains(str)) {
                    try {
                        context.getPackageManager().getApplicationInfo(str, 4194304);
                        a(asInterface, str, context);
                    } catch (PackageManager.NameNotFoundException unused2) {
                    }
                }
            }
            for (int i11 = 0; i11 < list.size(); i11++) {
                String str2 = (String) list.get(i11);
                if (!H.contains(str2)) {
                    E1(asInterface, str2, context);
                }
            }
            return;
        }
        LocalLog.a("Utils", "restoreAllDozeDefaultWhitelist: listDozeDftWL=" + H);
    }

    public static void K2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "reverse_hightemp_threshold", i10, 0);
    }

    public static int L(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "power_save_five_g_request", D1("five_g_request", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT));
    }

    public static int L0(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "smart_long_charge_protection_deepthinker_wrong_times", 0);
    }

    public static synchronized boolean L1(Context context, IRemoteGuardElfInterface iRemoteGuardElfInterface) {
        synchronized (f.class) {
            LocalLog.a("Utils", "restore all default wl.");
            K1(context);
            if (iRemoteGuardElfInterface == null) {
                LocalLog.a("Utils", "restore all default wl: remoteGuardElfService is null!");
                return false;
            }
            try {
                iRemoteGuardElfInterface.x();
                return true;
            } catch (Exception unused) {
                LocalLog.a("Utils", "restore all default wl Exception!");
                return false;
            }
        }
    }

    public static void L2(Context context, int i10) {
        Settings.System.putInt(context.getContentResolver(), "oplus_rm_sleep_state", i10);
    }

    public static boolean M(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "estimated_time", D1("full_power_estimated_time_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0) == 1;
    }

    public static int M0(Context context) {
        return SmartModeSharepref.b(context, "smart_long_charge_protection_enter_protect_times", 0);
    }

    public static void M1(IDeviceIdleController iDeviceIdleController, String str, Context context) {
        List<String> H = H(context);
        if (H != null && !H.isEmpty()) {
            boolean contains = H.contains(str);
            boolean h12 = h1(iDeviceIdleController, str);
            LocalLog.a("Utils", "restoreDftDozeWhiteList: isDftWL=" + contains + ", isNowWL=" + h12);
            if (contains && !h12) {
                a(iDeviceIdleController, str, context);
                return;
            } else {
                if (!h12 || contains) {
                    return;
                }
                E1(iDeviceIdleController, str, context);
                return;
            }
        }
        LocalLog.a("Utils", "restoreDftDozeWhiteList: listDftDozeWL=" + H);
    }

    public static void M2(Context context, long j10) {
        if (LocalLog.g()) {
            LocalLog.a("Utils", "setScrOnSeconds: " + j10);
        }
        Settings.Global.putLong(context.getContentResolver(), "last_charge_scr_on_duration_time", j10);
    }

    public static boolean N(Context context) {
        return context.getResources().getBoolean(R.bool.is_immsersive_theme);
    }

    public static long N0(Context context) {
        return Settings.Global.getLong(context.getContentResolver(), "smart_long_charge_protection_guide_open_time", 0L);
    }

    public static void N1(List<String> list, List<String> list2, Context context) {
        if (list == null || list2 == null) {
            return;
        }
        H3(LocalFileUtil.e("battery" + File.separator + "allow_prohibit_app.xml", context), list, list2);
    }

    public static void N2(Context context, long j10) {
        SmartModeSharepref.e(context, "screen_off_current_time", j10);
    }

    public static boolean O(Context context, String str) {
        return SmartModeSharepref.a(context, "high_power_notification_no_more" + str, false);
    }

    public static int O0(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "smart_long_charge_protection_guide_open_times", 0);
    }

    public static void O1(Context context, boolean z10, long j10) {
        Intent intent = new Intent(z10 ? "oplus.intent.action.wirelesslowPower.start" : "oplus.intent.action.wirelesslowPower.end");
        intent.setPackage("com.oplus.battery");
        ((AlarmManager) context.getSystemService("alarm")).setExactAndAllowWhileIdle(0, j10, PendingIntent.getBroadcastAsUser(context, 0, intent, 67108864, UserHandle.ALL));
    }

    public static void O2(Context context, long j10) {
        SmartModeSharepref.e(context, "screenoff_deepsleep_elapsed_time", j10);
    }

    public static int P(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "smart_long_charge_protection_guide_open_pop_up", 1);
    }

    public static int P0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_long_charge_protection_notify_end_times", 0, 0);
    }

    public static void P1(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Intent intent = new Intent("oplus.intent.action.wirelessreverse.timeout");
        intent.setPackage("com.oplus.battery");
        ((AlarmManager) context.getSystemService("alarm")).setExact(2, elapsedRealtime + 120000, PendingIntent.getBroadcastAsUser(context, 0, intent, 67108864, UserHandle.ALL));
    }

    public static void P2(Context context, boolean z10) {
        SmartModeSharepref.f(context, "is_screen_on", z10);
    }

    public static boolean Q(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "project_support_oplus_store", 1, 0) == 1;
    }

    public static int Q0(Context context) {
        return SmartModeSharepref.b(context, "smart_long_charge_protection_satisfy_scene_times", 0);
    }

    public static void Q1(Context context, boolean z10) {
        Settings.System.putInt(context.getContentResolver(), "auto_power_protect_state", z10 ? 1 : 0);
    }

    public static void Q2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "slient_mode_type_state", i10, 0);
    }

    public static boolean R(Context context) {
        return SmartModeSharepref.a(context, "wireless_charging_notificate", false);
    }

    public static long R0(Context context) {
        return Settings.Global.getLong(context.getContentResolver(), "smart_long_charge_protection_study_day", 0L);
    }

    public static void R1(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "battery_health_current_data", i10, 0);
    }

    public static void R2(Context context, int i10) {
        int w02 = w0();
        if (w02 == i10) {
            return;
        }
        LocalLog.a("Utils", "newVal = " + i10 + ", stateNow = " + w02);
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            cls.getMethod("setWirelessUserSleepMode", String.class).invoke(cls.newInstance(), i10 + "");
        } catch (Exception e10) {
            LocalLog.a("Utils", "setWirelessTXEnable error = " + e10.toString());
        }
    }

    public static long S(Context context) {
        long j10 = Settings.Global.getLong(context.getContentResolver(), "last_charge_duration_time", 0L);
        if (LocalLog.g()) {
            LocalLog.a("Utils", "getLastChargeDurationTime: " + j10);
        }
        return j10;
    }

    public static int S0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_long_charge_protection_switch_state", w(context) ? 1 : 0, 0);
    }

    public static void S1(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "battery_health_enter_times_daily", i10);
    }

    public static void S2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_charge_income_day_statistics", i10, 0);
    }

    public static long T(Context context) {
        long j10 = Settings.Global.getLong(context.getContentResolver(), "last_charge_time", System.currentTimeMillis());
        if (LocalLog.g()) {
            LocalLog.a("Utils", "getLastChargeTime: " + j10);
        }
        return j10;
    }

    public static int T0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_long_charge_protection_threshold_gear", 0, 0);
    }

    public static void T1(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "battery_health_current_test_mode", z10 ? 1 : 0, 0);
    }

    public static void T2(Map<Integer, Integer> map, Context context) {
        if (map == null || map.isEmpty() || map.size() < 1) {
            return;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences("SmartChargeUtils", 0).edit();
        String json = new Gson().toJson(map);
        edit.clear();
        edit.putString("smart_charge_incomel_time_map", json);
        edit.commit();
    }

    public static int U(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "last_level", 100, 0);
    }

    public static boolean U0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "speed_charge_dialog_remind", 0, 0) == 1;
    }

    public static void U1(Context context, int i10) {
        SmartModeSharepref.d(context, "battery_level_backup", i10);
    }

    public static void U2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_charge_income_total_time", i10, 0);
    }

    public static long V(Context context) {
        long j10 = Settings.Global.getLong(context.getContentResolver(), "last_scr_on_time", System.currentTimeMillis());
        if (LocalLog.g()) {
            LocalLog.a("Utils", "getLastScrChangeTime: " + j10);
        }
        return j10;
    }

    public static boolean V0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "speed_charge_state", 0, 0) == 3;
    }

    public static void V1(boolean z10) {
        f11392a = z10;
    }

    public static void V2(Context context, long j10) {
        SmartModeSharepref.e(context, "smart_charge_notificate_time_be_running", j10);
    }

    public static boolean W(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "low_power_charging_state", D1("low_power_charging_pref", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0) == 1;
    }

    public static View W0(Context context) {
        return X0(context, SystemBarUtils.getStatusBarHeight(context));
    }

    public static void W1(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "Setting_SetTimeBeginHour", i10, 0);
    }

    public static void W2(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_charge_notificate_times_be_running", i10);
    }

    public static long X(Context context, boolean z10) {
        int i10;
        int i11;
        Calendar calendar = Calendar.getInstance();
        int i12 = 0;
        if (calendar != null) {
            i12 = calendar.get(11);
            i11 = calendar.get(12);
            i10 = calendar.get(13);
        } else {
            i10 = 0;
            i11 = 0;
        }
        int s7 = z10 ? s(context) : J(context);
        int t7 = z10 ? t(context) : K(context);
        if (i12 > s7 || (i12 == s7 && i11 >= t7)) {
            s7 += 24;
        }
        long j10 = (((((s7 * 60) + t7) * 60) - (((i12 * 60) + i11) * 60)) - i10) * 1000;
        if (z10) {
            LocalLog.a("Utils", "getMillisToBeginTime  begin = " + s7 + ":" + t7 + " ,msToStart=" + j10);
        } else {
            LocalLog.a("Utils", "getMillisToEndTime  end = " + s7 + ":" + t7 + " ,msToEnd=" + j10);
        }
        return j10;
    }

    public static View X0(Context context, int i10) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, i10));
        return imageView;
    }

    public static void X1(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "Setting_SetTimeBeginMin", i10, 0);
    }

    public static void X2(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_charge_notificate_times_journey_scene", i10);
    }

    public static boolean Y(Context context) {
        return SmartModeSharepref.a(context, "notify_restricted_app", false);
    }

    public static boolean Y0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "super_endurance_mode_state", 0, 0) == 1;
    }

    public static void Y1(Context context, boolean z10) {
        SmartModeSharepref.f(context, "is_charging_screen_off", z10);
    }

    public static void Y2(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_charge_notificate_times_low_power_scene", i10);
    }

    public static int Z() {
        int i10;
        Calendar calendar = Calendar.getInstance();
        int i11 = 0;
        if (calendar != null) {
            i11 = calendar.get(11);
            i10 = calendar.get(12);
        } else {
            i10 = 0;
        }
        return (i11 * 60) + i10;
    }

    public static boolean Z0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "super_powersave_launcher_enter", 0, 0) == 1;
    }

    public static void Z1(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "project_support_charge_protection_rus", z10 ? 1 : 0, 0);
    }

    public static void Z2(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_charge_notificate_times_screen_off_scene", i10);
    }

    public static void a(IDeviceIdleController iDeviceIdleController, String str, Context context) {
        try {
            iDeviceIdleController.addPowerSaveWhitelistApp(str);
            LocalLog.a("Utils", "addDozeWhitelist: pkg=" + str);
        } catch (Exception e10) {
            LocalLog.c("Utils", "addDozeWhitelist: Exception! ", e10);
        }
    }

    public static int a0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_auto_close_switch", b0(context), 0);
    }

    public static boolean a1(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "super_powersave_mode_state", 0, 0) == 1;
    }

    public static void a2(boolean z10) {
        new OplusBatteryManager().setPsyMmiChgEn(z10 ? "0" : "1");
    }

    public static void a3(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_charge_notificate_times_morning_out_scene", i10);
    }

    public static void b(ServiceConnection serviceConnection, Context context) {
        LocalLog.a("Utils", "bindRemoteGuardElfService");
        try {
            Intent intent = new Intent();
            intent.setAction("com.oplus.athena.RemoteGuardElfService");
            intent.setPackage("com.oplus.athena");
            context.bindService(intent, serviceConnection, 1);
        } catch (Throwable th) {
            LocalLog.c("Utils", "bindRemoteGuardElfService exception.", th);
        }
    }

    public static int b0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_auto_close_switch", y5.b.b() ? 1 : D1("auto_close_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    public static boolean b1() {
        String str = null;
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            Object invoke = cls.getMethod("getWirelessTXEnable", new Class[0]).invoke(cls.newInstance(), new Object[0]);
            if (invoke != null) {
                str = String.valueOf(invoke.toString());
            }
        } catch (Exception e10) {
            LocalLog.a("Utils", "getWirelessTXEnable error = " + e10.toString());
        }
        if (str != null) {
            return str.startsWith("enable") || str.startsWith("charging");
        }
        return false;
    }

    public static void b2(Context context, int i10, boolean z10) {
        if (i10 == 4) {
            return;
        }
        a2(z10);
    }

    public static void b3(Context context, long j10) {
        if (G0(context) < j10 || j10 == 0) {
            Settings.System.putLong(context.getContentResolver(), "smart_charge_protection_end_time", j10);
        }
    }

    public static double c(List<BatterySipper> list, Context context) {
        double n10 = n(context);
        if (n10 < 3000.0d) {
            n10 = 3000.0d;
        }
        double d10 = UserProfileInfo.Constant.NA_LAT_LON;
        for (int size = list.size() - 1; size >= 0; size--) {
            BatterySipper batterySipper = list.get(size);
            if (!e1(batterySipper)) {
                double d11 = batterySipper.f17617i;
                if (d11 >= 0.001d && d11 <= 5.0d * n10) {
                    d10 += d11;
                }
            }
            list.remove(size);
        }
        return d10;
    }

    public static int c0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_open_level", PowerSaveLevelPicker.g(), 0);
    }

    public static int c1(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "wireless_reverse_battery_level_threshold", 25, 0);
    }

    public static void c2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "charge_protection_current_state", z10 ? 1 : 0, 0);
    }

    public static void c3(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_charge_protection_status", i10, 0);
    }

    public static void d(Context context, boolean z10) {
        Intent intent = new Intent(z10 ? "oplus.intent.action.wirelesslowPower.start" : "oplus.intent.action.wirelesslowPower.end");
        intent.setPackage("com.oplus.battery");
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getBroadcastAsUser(context, 0, intent, 67108864, UserHandle.ALL));
    }

    public static int d0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_open_level_switch", D1("open_level_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    public static boolean d1(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "wireless_reverse_charging_state", 0, 0) == 1;
    }

    public static void d2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_charge_current_state", z10 ? 1 : 0, 0);
        SmartModeSharepref.d(context, "smart_charge_current_state", z10 ? 1 : 0);
    }

    public static void d3(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_charge_protection_switch_state", i10, 0);
    }

    public static void e(Context context) {
        LocalLog.a("Utils", "cancelWirelessReverseTimeOutAlarm");
        Intent intent = new Intent("oplus.intent.action.wirelessreverse.timeout");
        intent.setPackage("com.oplus.battery");
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getBroadcastAsUser(context, 0, intent, 67108864, UserHandle.ALL));
    }

    public static int e0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_backlight_switch_state", D1("screen_bright_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    public static boolean e1(BatterySipper batterySipper) {
        if (batterySipper.f17623o != BatterySipper.a.APP) {
            return false;
        }
        return f11395d.contains(batterySipper.f17622n) || f11396e.contains(Integer.valueOf(batterySipper.b()));
    }

    public static void e2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "customize_charge_switch_state", z10 ? 1 : 0, 0);
    }

    public static void e3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_charge_switch_state", z10 ? 1 : 0, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0037, code lost:
    
        if (r3 < r5) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0029, code lost:
    
        if (r3 < r7) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean f(Context context) {
        int s7 = s(context);
        int t7 = t(context);
        int J = J(context);
        int K = K(context);
        long Z = Z();
        long j10 = (s7 * 60) + t7;
        long j11 = (J * 60) + K;
        boolean z10 = true;
        boolean z11 = false;
        if (j11 > j10) {
            if (Z >= j10) {
            }
            z10 = false;
            z11 = z10;
        } else if (j11 < j10) {
            if (Z >= j11) {
            }
            z11 = z10;
        }
        LocalLog.a("Utils", "checkInDuration: begin = " + s7 + ":" + t7 + ", end = " + J + ":" + K + ", now = " + Z + ", beginTimeInMin = " + j10 + ", endTimeInMin = " + j11 + ", inDuration = " + z11);
        return z11;
    }

    public static boolean f0(Context context) {
        return ((PowerManager) context.getSystemService("power")).isPowerSaveMode();
    }

    private static boolean f1(BatterySipper.a aVar) {
        return (aVar == BatterySipper.a.APP || aVar == BatterySipper.a.PHONE || aVar == BatterySipper.a.WIFI || aVar == BatterySipper.a.BLUETOOTH) ? false : true;
    }

    public static void f2(Context context, boolean z10) {
        Settings.Secure.putInt(context.getContentResolver(), "deepsleep_switch_state", z10 ? 1 : 0);
    }

    public static void f3(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "smart_long_charge_protection_deepthinker_total_times", i10);
    }

    public static List<BatterySipper> g(List<UidBatteryConsumer> list) {
        ArrayList arrayList = new ArrayList();
        for (UidBatteryConsumer uidBatteryConsumer : list) {
            BatterySipper batterySipper = new BatterySipper();
            batterySipper.f17623o = BatterySipper.a.APP;
            batterySipper.f17609a = uidBatteryConsumer.getUid();
            batterySipper.f17622n = uidBatteryConsumer.getPackageWithHighestDrain();
            batterySipper.f17617i = uidBatteryConsumer.getConsumedPower();
            batterySipper.f17618j = uidBatteryConsumer.getConsumedPower();
            batterySipper.f17610b = uidBatteryConsumer.getTimeInStateMs(0);
            batterySipper.f17611c = uidBatteryConsumer.getTimeInStateMs(1);
            batterySipper.f17614f = uidBatteryConsumer.getUsageDurationMillis(11);
            batterySipper.f17615g = uidBatteryConsumer.getUsageDurationMillis(2);
            batterySipper.f17612d = uidBatteryConsumer.getUsageDurationMillis(12);
            batterySipper.f17616h = uidBatteryConsumer.getConsumedPower(3);
            batterySipper.f17619k = uidBatteryConsumer.getConsumedPower(0);
            arrayList.add(batterySipper);
        }
        return arrayList;
    }

    public static int g0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_rus_five_g_state", 1, 0);
    }

    public static boolean g1(Context context) {
        return (Settings.Global.getInt(context.getContentResolver(), "oplus_system_folding_mode", -1) == 1 || AppFeature.m()) && !y5.b.l();
    }

    public static void g2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "dialog_no_need_remind", z10 ? 1 : 0, 0);
    }

    public static void g3(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "smart_long_charge_protection_deepthinker_wrong_times", i10);
    }

    public static String h(String str, int i10) {
        try {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            byte[] bytes = str.getBytes("GBK");
            int length = bytes.length;
            for (int i11 = 0; i11 < length; i11++) {
                bytes[i11] = (byte) (bytes[i11] ^ i10);
            }
            return new String(bytes, "GBK");
        } catch (Exception e10) {
            Log.e("Utils", "encrypt() e: " + e10);
            return "";
        }
    }

    public static int h0(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "oplus_customize_screen_refresh_rate", 0, 0);
    }

    public static boolean h1(IDeviceIdleController iDeviceIdleController, String str) {
        boolean z10;
        try {
            z10 = iDeviceIdleController.isPowerSaveWhitelistApp(str);
        } catch (Exception e10) {
            e10.printStackTrace();
            z10 = false;
        }
        LocalLog.a("Utils", "isDozeWhiteListApp: " + z10);
        return z10;
    }

    public static void h2(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "power_save_disable_five_g_state", i10);
        LocalLog.l("Utils", "setDisableFiveGState: state=" + i10);
    }

    public static void h3(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_long_charge_protection_enter_protect_times", i10);
    }

    public static String i(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() > 0 && str.charAt(0) == 160) {
            str = str.substring(1);
        }
        return (str.length() <= 0 || str.charAt(str.length() - 1) != 160) ? str : str.substring(0, str.length() - 1);
    }

    public static int i0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_screen_refresh_rate", D1("decrease_screen_refresh", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    public static boolean i1() {
        return y5.b.n() && !y5.b.i();
    }

    public static void i2(boolean z10) {
        f11393b = z10;
    }

    public static void i3(Context context, long j10) {
        Settings.Global.putLong(context.getContentResolver(), "smart_long_charge_protection_guide_open_time", j10);
    }

    public static float j(Context context, float f10) {
        return (context.getResources().getDisplayMetrics().density * f10) + ((f10 >= 0.0f ? 1 : -1) * 0.5f);
    }

    public static int j0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_screenoff_time_state", D1("screen_auto_off_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    public static boolean j1(String str, BatterySipper batterySipper) {
        if (str == null || "".equals(str) || "*wakelock*".equals(str) || "dex2oat".equals(str)) {
            return false;
        }
        return !"mediaserver".equals(str) || batterySipper.b() == 1013;
    }

    public static void j2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "Setting_SetTimeEndHour", i10, 0);
    }

    public static void j3(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "smart_long_charge_protection_guide_open_times", i10);
    }

    public static int k(Context context) {
        return g1(context) ? 2131820872 : 2131820871;
    }

    public static int k0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_sync_state", D1("back_synchronize_switch", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    public static boolean k1(Context context) {
        return false;
    }

    public static void k2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "Setting_SetTimeEndMin", i10, 0);
    }

    public static void k3(Context context, long j10) {
        Settings.Global.putLong(context.getContentResolver(), "smart_long_charge_protection_notify_end_latest_time", j10);
    }

    public static void l(List<String> list, List<String> list2, Context context) {
        StringBuilder sb2;
        if (list == null || list2 == null) {
            return;
        }
        InputStream a10 = LocalFileUtil.a("battery" + File.separator + "allow_prohibit_app.xml", context);
        try {
            if (a10 == null) {
                LocalLog.b("Utils", "Fail to getAllowProhibitList");
                return;
            }
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(a10, null);
                v1(newPullParser, list, list2);
                try {
                    a10.close();
                } catch (IOException e10) {
                    e = e10;
                    sb2 = new StringBuilder();
                    sb2.append("getAllowProhibitList: Got exception close stream IOException e=");
                    sb2.append(e);
                    LocalLog.a("Utils", sb2.toString());
                }
            } catch (Exception e11) {
                LocalLog.a("Utils", "getAllowProhibitList: Got exception e=" + e11);
                try {
                    a10.close();
                } catch (IOException e12) {
                    e = e12;
                    sb2 = new StringBuilder();
                    sb2.append("getAllowProhibitList: Got exception close stream IOException e=");
                    sb2.append(e);
                    LocalLog.a("Utils", sb2.toString());
                }
            }
        } catch (Throwable th) {
            try {
                a10.close();
            } catch (IOException e13) {
                LocalLog.a("Utils", "getAllowProhibitList: Got exception close stream IOException e=" + e13);
            }
            throw th;
        }
    }

    public static int l0(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "power_save_disable_five_g_tips", 0);
    }

    public static boolean l1(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "oguard_init", 0) == 1;
    }

    public static void l2(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "power_save_five_g_request", i10);
        LocalLog.l("Utils", "setFiveGRequestState: state=" + i10);
    }

    public static void l3(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_long_charge_protection_notify_end_times", i10, 0);
    }

    public static boolean m(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "auto_power_protect_state", 1) != 0;
    }

    public static int m0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "regular_charge_protection_status", 0, 0);
    }

    public static boolean m1(ApplicationInfo applicationInfo, Context context) {
        if (OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.pms_app_frozen")) {
            try {
                return OplusPackageManager.getOplusPackageManager(context).getOplusFreezePackageState(applicationInfo.packageName, UserHandle.getUserId(applicationInfo.uid)) == 2;
            } catch (Throwable th) {
                Log.e("Utils", "isOplusDisable, e =" + th.getMessage());
            }
        }
        return false;
    }

    public static void m2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "estimated_time", z10 ? 1 : 0, 0);
    }

    public static void m3(Context context, int i10) {
        SmartModeSharepref.d(context, "smart_long_charge_protection_satisfy_scene_times", i10);
    }

    public static double n(Context context) {
        return new PowerProfile(context).getBatteryCapacity();
    }

    public static int n0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "regular_charge_protection_switch_state", 0, 0);
    }

    public static boolean n1(Context context) {
        OplusCustomizeRestrictionManager oplusCustomizeRestrictionManager = OplusCustomizeRestrictionManager.getInstance(context);
        return (oplusCustomizeRestrictionManager != null ? oplusCustomizeRestrictionManager.isPowerSavingModeDisabled((ComponentName) null) : false) || y5.b.C();
    }

    public static void n2(WindowManager.LayoutParams layoutParams, int i10) {
        try {
            OplusBaseLayoutParams oplusBaseLayoutParams = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, layoutParams);
            if (oplusBaseLayoutParams != null) {
                oplusBaseLayoutParams.ignoreHomeMenuKey = i10;
            }
        } catch (Exception e10) {
            LocalLog.b("Utils", e10.toString());
        }
    }

    public static void n3(Context context, long j10) {
        Settings.Global.putLong(context.getContentResolver(), "smart_long_charge_protection_study_day", j10);
    }

    public static int o(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "battery_health_current_data", 100, 0);
    }

    public static int o0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "reverse_hightemp_threshold", 450, 0);
    }

    public static boolean o1(Activity activity) {
        if (activity == null) {
            LocalLog.a("Utils", "isSettingsNavMode: activity is null");
            return true;
        }
        return SplitController.d().e(activity);
    }

    public static void o2(Context context, String str, boolean z10) {
        SmartModeSharepref.f(context, "high_power_notification_no_more" + str, z10);
    }

    public static void o3(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "smart_long_charge_protection_threshold_gear", i10, 0);
    }

    public static int p(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "battery_health_enter_times_daily", 0);
    }

    public static int p0(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "oplus_rm_sleep_state", 1);
    }

    private static boolean p1(int i10) {
        return UserHandle.getAppIdFromSharedAppGid(i10) > 0;
    }

    public static void p2(Context context, int i10) {
        Settings.Global.putInt(context.getContentResolver(), "smart_long_charge_protection_guide_open_pop_up", i10);
    }

    public static void p3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "speed_charge_dialog_remind", z10 ? 1 : 0, 0);
    }

    public static int q(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "battery_health_current_test_mode", 0, 0);
    }

    public static long q0(Context context) {
        long j10 = Settings.Global.getLong(context.getContentResolver(), "last_charge_scr_on_duration_time", 0L);
        if (LocalLog.g()) {
            LocalLog.a("Utils", "getScrOnSeconds: " + j10);
        }
        return j10;
    }

    public static boolean q1(Context context) {
        boolean z10 = false;
        try {
            OplusCustomizeRestrictionManager oplusCustomizeRestrictionManager = OplusCustomizeRestrictionManager.getInstance(context);
            if (oplusCustomizeRestrictionManager != null) {
                z10 = ((Boolean) d.d(oplusCustomizeRestrictionManager, "isSleepStandbyOptimizationDisabled")).booleanValue();
            }
        } catch (Throwable th) {
            LocalLog.b("Utils", "isSleepStandbyOptimizationDisabled: e = " + th.getMessage());
        }
        LocalLog.a("Utils", "isSleepStandbyOptimizationDisabled: customizeDisabled = " + z10);
        return z10;
    }

    public static void q2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "project_support_oplus_store", z10 ? 1 : 0, 0);
    }

    public static void q3(Context context, boolean z10) {
        try {
            try {
                OplusBatteryManager oplusBatteryManager = new OplusBatteryManager();
                Method b10 = d.a().b("android.os.OplusBatteryManager", "setCustomSelectChgMode", Integer.TYPE, Boolean.TYPE);
                if (z10) {
                    r3(context, true);
                    b10.invoke(oplusBatteryManager, 3, Boolean.TRUE);
                    LocalLog.a("Utils", "setSpeedChargeMode enable speedcharge");
                } else {
                    r3(context, false);
                    b10.invoke(oplusBatteryManager, 0, Boolean.FALSE);
                    LocalLog.a("Utils", "setSpeedChargeMode disable speedcharge");
                }
                if (!z10) {
                    return;
                }
            } catch (Exception unused) {
                LocalLog.b("Utils", "setSpeedChargeMode Exception");
                if (!z10) {
                    return;
                }
            }
            G3(context);
        } catch (Throwable th) {
            if (z10) {
                G3(context);
            }
            throw th;
        }
    }

    public static int r(Context context) {
        return SmartModeSharepref.b(context, "battery_level_backup", 0);
    }

    public static long r0(Context context) {
        return SmartModeSharepref.c(context, "screen_off_current_time", 0L);
    }

    public static boolean r1(Context context) {
        boolean z10;
        OplusCustomizeRestrictionManager oplusCustomizeRestrictionManager;
        try {
            oplusCustomizeRestrictionManager = OplusCustomizeRestrictionManager.getInstance(context);
        } catch (Throwable th) {
            Log.e("Utils", "isSuperPowerSaveDisabled: e = " + th.getMessage());
        }
        if (oplusCustomizeRestrictionManager != null) {
            z10 = ((Boolean) d.d(oplusCustomizeRestrictionManager, "isSuperPowerSavingModeDisabled")).booleanValue();
            return !z10 || y5.b.j();
        }
        z10 = false;
        if (z10) {
        }
    }

    public static void r2(Context context, boolean z10) {
        SmartModeSharepref.f(context, "wireless_charging_notificate", z10);
    }

    public static void r3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "speed_charge_state", z10 ? 3 : 0, 0);
    }

    public static int s(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "Setting_SetTimeBeginHour", D1("silent_mode_type_custom", "beginHour"), 0);
    }

    public static long s0(Context context) {
        return SmartModeSharepref.c(context, "screenoff_deepsleep_elapsed_time", 0L);
    }

    public static boolean s1() {
        return !y5.b.j() && y5.b.I();
    }

    public static void s2(Context context, long j10) {
        Settings.Global.putLong(context.getContentResolver(), "last_charge_duration_time", j10);
    }

    public static void s3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "super_endurance_mode_state", z10 ? 1 : 0, 0);
    }

    public static int t(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "Setting_SetTimeBeginMin", D1("silent_mode_type_custom", "beginMin"), 0);
    }

    public static boolean t0(Context context) {
        return SmartModeSharepref.a(context, "is_screen_on", false);
    }

    public static boolean t1(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "oplus_support_fiveg_status", 0, 0) == 1;
    }

    public static void t2(Context context, long j10) {
        Settings.Global.putLong(context.getContentResolver(), "last_charge_time", j10);
    }

    public static void t3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "super_powersave_launcher_enter", z10 ? 1 : 0, 0);
    }

    public static boolean u(Context context) {
        return SmartModeSharepref.a(context, "is_charging_screen_off", false);
    }

    public static int u0(Context context) {
        return g1(context) ? 2131820882 : 2131820883;
    }

    private static boolean u1(int i10) {
        return i10 >= 1000 && i10 < 10000;
    }

    public static void u2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "last_level", i10, 0);
    }

    public static void u3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "super_powersave_mode_state", z10 ? 1 : 0, 0);
    }

    public static boolean v(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "project_support_charge_protection_rus", 1, 0) == 1;
    }

    public static int v0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "slient_mode_type_state", D1("silent_mode_type_state", CpuLevelConfig.ThermalCpuLevelPolicy.SKIP_GOPLUS_DEFAULT), 0);
    }

    private static void v1(XmlPullParser xmlPullParser, List<String> list, List<String> list2) {
        try {
            int eventType = xmlPullParser.getEventType();
            String str = null;
            while (eventType != 1) {
                if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    if (xmlPullParser.next() != 1) {
                        str = xmlPullParser.getText();
                    }
                    if (str != null && "allow".equals(name)) {
                        if (!list.contains(str)) {
                            list.add(str);
                        }
                    } else if (str != null && "prohibit".equals(name) && !list2.contains(str)) {
                        list2.add(str);
                    }
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception unused) {
            LocalLog.a("Utils", "parseXml: Got execption.");
        }
    }

    public static void v2(Context context, long j10) {
        if (LocalLog.g()) {
            LocalLog.a("Utils", "setLastScrChangeTime: " + j10);
        }
        Settings.Global.putLong(context.getContentResolver(), "last_scr_on_time", j10);
    }

    public static void v3(View view, float f10) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                layoutParams2.weight = f10;
                view.setLayoutParams(layoutParams2);
                LocalLog.a("Utils", "setViewWeight: " + f10);
                return;
            }
            if (layoutParams instanceof LinearLayoutCompat.LayoutParams) {
                LinearLayoutCompat.LayoutParams layoutParams3 = (LinearLayoutCompat.LayoutParams) layoutParams;
                ((LinearLayout.LayoutParams) layoutParams3).weight = f10;
                view.setLayoutParams(layoutParams3);
                LocalLog.a("Utils", "setViewWeight: " + f10);
                return;
            }
            return;
        }
        LocalLog.a("Utils", "setViewWeight: view is null");
    }

    public static boolean w(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "charge_protection_switch_state", 0, 0) == 1;
    }

    private static int w0() {
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            Object invoke = cls.getMethod("getWirelessUserSleepMode", new Class[0]).invoke(cls.newInstance(), new Object[0]);
            if (invoke != null) {
                return Integer.parseInt(invoke.toString());
            }
            return 0;
        } catch (Exception e10) {
            LocalLog.a("Utils", "getWirelessTXEnable error = " + e10.toString());
            return 0;
        }
    }

    private static void w1(XmlPullParser xmlPullParser, List<String> list) {
        try {
            int eventType = xmlPullParser.getEventType();
            String str = null;
            while (eventType != 1) {
                if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    if (xmlPullParser.next() != 1) {
                        str = xmlPullParser.getText();
                    }
                    if (str != null && "wl".equals(name) && !list.contains(str)) {
                        list.add(str);
                    }
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e10) {
            LocalLog.c("Utils", "parseDozeWhiteList: Got execption. ", e10);
        }
    }

    public static void w2(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "low_power_charging_state", z10 ? 1 : 0, 0);
    }

    public static void w3(boolean z10) {
        if (b1() == z10) {
            return;
        }
        try {
            Class<?> cls = Class.forName("android.os.OplusBatteryManager");
            Method method = cls.getMethod("setWirelessTXEnable", String.class);
            Object newInstance = cls.newInstance();
            Object[] objArr = new Object[1];
            objArr[0] = z10 ? "1" : "0";
            method.invoke(newInstance, objArr);
        } catch (Exception e10) {
            LocalLog.a("Utils", "setWirelessTXEnable error = " + e10.toString());
        }
    }

    public static boolean x(Context context) {
        return SmartModeSharepref.a(context, "is_charger_pluged", false);
    }

    public static int x0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_charge_income_day_statistics", 0, 0);
    }

    public static HashMap<String, Integer> x1(Context context) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        InputStream a10 = LocalFileUtil.a("battery" + File.separator + "power_consum_app_first.xml", context);
        try {
            if (a10 == null) {
                return hashMap;
            }
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(a10, null);
                int i10 = 0;
                for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                    if (eventType == 2) {
                        String name = newPullParser.getName();
                        if (!"appFirst".equals(name)) {
                            if (newPullParser.next() != 1) {
                                i10 = Integer.parseInt(newPullParser.getText());
                            }
                            if (LocalLog.g()) {
                                LocalLog.a("Utils", "tag1 = " + name + " tag2 = " + i10);
                            }
                            hashMap.put(name, Integer.valueOf(i10));
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e10) {
                LocalLog.a("Utils", "readFromPCOXml: Got exception." + e10.toString());
            }
            try {
                a10.close();
            } catch (IOException unused) {
                LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception close xmlReader.");
            }
            return hashMap;
        } catch (Throwable th) {
            try {
                a10.close();
            } catch (IOException unused2) {
                LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception close xmlReader.");
            }
            throw th;
        }
    }

    public static void x2(Context context, boolean z10) {
        SmartModeSharepref.f(context, "notify_restricted_app", z10);
    }

    public static void x3(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "wireless_reverse_battery_level_threshold", i10, 0);
    }

    public static List<BatterySipper> y(List<BatterySipper> list) {
        String str;
        ArrayList arrayList = new ArrayList();
        SparseArray sparseArray = new SparseArray();
        ArrayList arrayList2 = new ArrayList();
        int size = list.size();
        for (int i10 = 0; i10 < size; i10++) {
            BatterySipper batterySipper = list.get(i10);
            if (batterySipper.f17621m) {
                if (LocalLog.g()) {
                    LocalLog.a("Utils", "sipper.shouldHide: pkg:" + batterySipper.f17622n + " power:" + batterySipper.f17617i + " uid:" + batterySipper.b());
                }
            } else if (f1(batterySipper.f17623o)) {
                if (LocalLog.g()) {
                    LocalLog.a("Utils", "skip:" + batterySipper.f17622n + "ignoreType");
                }
            } else if (batterySipper.b() <= 1000) {
                if (LocalLog.g()) {
                    LocalLog.a("Utils", "getCoalescedUsageList: uid(" + batterySipper.b() + ") name(" + batterySipper.f17622n + "). ignore!!!");
                }
            } else {
                UserHandle.getUserId(batterySipper.b());
                BatterySipper.a aVar = batterySipper.f17623o;
                BatterySipper.a aVar2 = BatterySipper.a.APP;
                if (aVar == aVar2 && batterySipper.b() == 0) {
                    if (LocalLog.g()) {
                        LocalLog.a("Utils", "getCoalescedUsageList: ignore ROOT_UID!!! name=" + batterySipper.f17622n);
                    }
                } else if (batterySipper.f17623o == aVar2 && "noPkg".equals(batterySipper.f17622n)) {
                    if (LocalLog.g()) {
                        LocalLog.a("Utils", "getCoalescedUsageList: noPkg. uid=" + batterySipper.b() + ", totalPowerMah=" + batterySipper.f17618j + ", screenPowerMah=" + batterySipper.f17619k);
                    }
                } else if (batterySipper.b() != 0 && batterySipper.b() != 1000) {
                    int b10 = batterySipper.b();
                    if (p1(batterySipper.b())) {
                        b10 = UserHandle.getUid(0, UserHandle.getAppIdFromSharedAppGid(batterySipper.b()));
                        if (LocalLog.g()) {
                            Log.d("Utils", "getCoalescedUsageList: change SharedGid(" + batterySipper.b() + ") to " + b10);
                        }
                    }
                    if (!u1(b10) && batterySipper.f17622n != null) {
                        if (y5.b.I() && b10 != batterySipper.b()) {
                            BatterySipper batterySipper2 = new BatterySipper();
                            batterySipper2.a(batterySipper);
                            arrayList2.add(batterySipper2);
                        } else if (sparseArray.indexOfKey(b10) < 0) {
                            sparseArray.put(b10, batterySipper);
                        } else {
                            arrayList2.add(batterySipper);
                        }
                    } else if (LocalLog.g()) {
                        LocalLog.a("Utils", "getCoalescedUsageList: uid(" + b10 + ") , name is null. ignore!!!");
                    }
                } else {
                    if (LocalLog.g()) {
                        LocalLog.a("Utils", "system uid = " + batterySipper.b());
                        LocalLog.a("Utils", "system pkg = " + batterySipper.f17622n);
                    }
                    arrayList.add(batterySipper);
                }
            }
        }
        for (int i11 = 0; i11 < arrayList2.size(); i11++) {
            BatterySipper batterySipper3 = (BatterySipper) arrayList2.get(i11);
            int b11 = batterySipper3.b();
            int indexOfKey = sparseArray.indexOfKey(b11);
            if (indexOfKey < 0) {
                sparseArray.put(b11, batterySipper3);
            } else {
                BatterySipper batterySipper4 = (BatterySipper) sparseArray.valueAt(indexOfKey);
                batterySipper4.a(batterySipper3);
                if (batterySipper4.f17622n == null && (str = batterySipper3.f17622n) != null) {
                    batterySipper4.f17622n = str;
                }
            }
        }
        int size2 = sparseArray.size();
        for (int i12 = 0; i12 < size2; i12++) {
            arrayList.add((BatterySipper) sparseArray.valueAt(i12));
        }
        Collections.sort(arrayList, new b());
        return arrayList;
    }

    public static Map<Integer, Integer> y0(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SmartChargeUtils", 0);
        HashMap hashMap = new HashMap();
        String string = sharedPreferences.getString("smart_charge_incomel_time_map", null);
        return string == null ? hashMap : (Map) new Gson().fromJson(string, new a().getType());
    }

    public static void y1(Context context) {
        InputStream a10 = LocalFileUtil.a("battery" + File.separator + "switch_status_config.xml", context);
        try {
            try {
                XmlPullParser newPullParser = Xml.newPullParser();
                newPullParser.setInput(a10, null);
                int i10 = 0;
                for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                    if (eventType == 2) {
                        String str = CommonUtil.O().get(i10).split(",")[0];
                        String name = newPullParser.getName();
                        if (name.equals(str)) {
                            String nextText = newPullParser.nextText();
                            CommonUtil.O().set(i10, name + "," + nextText);
                            if ("screen_auto_off_switch".equals(name)) {
                                F2(context, Integer.parseInt(nextText));
                            } else if ("back_synchronize_switch".equals(name)) {
                                G2(context, Integer.parseInt(nextText));
                            } else if ("decrease_screen_refresh".equals(name)) {
                                E2(context, Integer.parseInt(nextText));
                            }
                            i10++;
                            LocalLog.a("Utils", "tag = " + name + " status = " + nextText + " index = " + i10);
                        }
                    }
                }
                if (a10 == null) {
                    return;
                }
            } catch (Throwable th) {
                if (a10 != null) {
                    try {
                        a10.close();
                    } catch (IOException unused) {
                        LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception close xmlReader.");
                    }
                }
                throw th;
            }
        } catch (Exception unused2) {
            LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception.");
            if (a10 == null) {
                return;
            }
        }
        try {
            a10.close();
        } catch (IOException unused3) {
            LocalLog.a("Utils", "readFromSwitchStatusXml: Got exception close xmlReader.");
        }
    }

    public static void y2(Context context) {
        Settings.Global.putInt(context.getContentResolver(), "oguard_init", 1);
    }

    public static void y3(Context context, boolean z10) {
        Settings.System.putIntForUser(context.getContentResolver(), "wireless_reverse_charging_state", z10 ? 1 : 0, 0);
    }

    public static String z(Context context) {
        return Settings.System.getString(context.getContentResolver(), "competition_event_enable");
    }

    public static int z0(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "smart_charge_income_total_time", 0, 0);
    }

    private static ArrayMap<String, Integer> z1(InputStream inputStream) {
        StringBuilder sb2;
        int next;
        String attributeValue;
        ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
        LocalLog.a("Utils", "do read map");
        try {
            try {
                try {
                    try {
                        try {
                            try {
                                XmlPullParser newPullParser = Xml.newPullParser();
                                newPullParser.setInput(inputStream, null);
                                do {
                                    next = newPullParser.next();
                                    if (next == 2 && "p".equals(newPullParser.getName()) && (attributeValue = newPullParser.getAttributeValue(null, "k")) != null) {
                                        String attributeValue2 = newPullParser.getAttributeValue(null, "v");
                                        if (!TextUtils.isEmpty(attributeValue2)) {
                                            arrayMap.put(attributeValue, Integer.valueOf(attributeValue2));
                                        }
                                        LocalLog.d("Utils", "readMapFromFileLocked  pkg == " + attributeValue + ", count = " + attributeValue2);
                                    }
                                } while (next != 1);
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e10) {
                                        e = e10;
                                        sb2 = new StringBuilder();
                                        sb2.append("Failed to close state FileInputStream ");
                                        sb2.append(e);
                                        LocalLog.d("Utils", sb2.toString());
                                        return arrayMap;
                                    }
                                }
                            } catch (XmlPullParserException e11) {
                                LocalLog.d("Utils", "failed parsing " + e11);
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e12) {
                                        e = e12;
                                        sb2 = new StringBuilder();
                                        sb2.append("Failed to close state FileInputStream ");
                                        sb2.append(e);
                                        LocalLog.d("Utils", sb2.toString());
                                        return arrayMap;
                                    }
                                }
                            }
                        } catch (NullPointerException e13) {
                            LocalLog.d("Utils", "failed parsing " + e13);
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e14) {
                                    e = e14;
                                    sb2 = new StringBuilder();
                                    sb2.append("Failed to close state FileInputStream ");
                                    sb2.append(e);
                                    LocalLog.d("Utils", sb2.toString());
                                    return arrayMap;
                                }
                            }
                        }
                    } catch (IndexOutOfBoundsException e15) {
                        LocalLog.d("Utils", "failed parsing " + e15);
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e16) {
                                e = e16;
                                sb2 = new StringBuilder();
                                sb2.append("Failed to close state FileInputStream ");
                                sb2.append(e);
                                LocalLog.d("Utils", sb2.toString());
                                return arrayMap;
                            }
                        }
                    }
                } catch (IOException e17) {
                    LocalLog.d("Utils", "failed IOException " + e17);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e18) {
                            e = e18;
                            sb2 = new StringBuilder();
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.d("Utils", sb2.toString());
                            return arrayMap;
                        }
                    }
                }
            } catch (NumberFormatException e19) {
                LocalLog.d("Utils", "failed parsing " + e19);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e20) {
                        e = e20;
                        sb2 = new StringBuilder();
                        sb2.append("Failed to close state FileInputStream ");
                        sb2.append(e);
                        LocalLog.d("Utils", sb2.toString());
                        return arrayMap;
                    }
                }
            }
            return arrayMap;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e21) {
                    LocalLog.d("Utils", "Failed to close state FileInputStream " + e21);
                }
            }
            throw th;
        }
    }

    public static void z2(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "power_save_auto_close_switch", i10, 0);
    }

    public static void z3(ServiceConnection serviceConnection, Context context) {
        try {
            context.unbindService(serviceConnection);
        } catch (Throwable th) {
            LocalLog.c("Utils", "bindRemoteGuardElfService exception.", th);
        }
    }
}
