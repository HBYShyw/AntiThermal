package d6;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.util.Xml;
import b6.LocalLog;
import com.oplus.deepsleep.DeepSleepUtils;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.f;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import m9.AlarmSetter;
import n8.PowerConsumptionOptimizationHelper;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import r9.SimplePowerMonitorUtils;
import s6.ThermalFactory;
import s8.PowerSaveHelper;
import w5.OplusBatteryConstants;
import w6.PluginSupporter;
import w6.RegionPluginUtil;
import x7.ChargeProtectionController;
import y5.AppFeature;
import z5.LocalFileUtil;

/* compiled from: ConfigUpdateUtil.java */
/* renamed from: d6.a, reason: use source file name */
/* loaded from: classes.dex */
public class ConfigUpdateUtil {
    public static final Uri C0 = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");
    private static ConfigUpdateUtil D0 = null;
    private static boolean E0 = true;
    private static boolean F0 = true;
    private static boolean G0 = true;
    private static int H0 = 20;
    private static int I0 = 40;
    private static int J0 = 60;
    private static int K0 = 4;
    private static int L0 = 3;
    private static int M0 = 2;
    private static long N0 = 5000;
    private static long O0 = 60000;
    private static long P0 = 10000;
    private static long Q0 = 5000;
    private static long R0 = 40000;
    private static int S0 = 10;
    private static int T0 = 10;
    private static int U0 = 40;
    private Context Y;

    /* renamed from: a, reason: collision with root package name */
    private String f10720a = "system&recent&lock&audio&guard&import";

    /* renamed from: b, reason: collision with root package name */
    private String f10722b = "system&audio&guard&lock&import";

    /* renamed from: c, reason: collision with root package name */
    private int f10724c = 7200;

    /* renamed from: d, reason: collision with root package name */
    private int f10726d = 0;

    /* renamed from: e, reason: collision with root package name */
    private int f10728e = 0;

    /* renamed from: f, reason: collision with root package name */
    private int f10730f = 200;

    /* renamed from: g, reason: collision with root package name */
    private int f10732g = 30;

    /* renamed from: h, reason: collision with root package name */
    private long f10734h = 300000;

    /* renamed from: i, reason: collision with root package name */
    private long f10736i = 60;

    /* renamed from: j, reason: collision with root package name */
    private long f10738j = 300;

    /* renamed from: k, reason: collision with root package name */
    private long f10740k = 360;

    /* renamed from: l, reason: collision with root package name */
    private long f10742l = 180;

    /* renamed from: m, reason: collision with root package name */
    private long f10744m = 300;

    /* renamed from: n, reason: collision with root package name */
    private long f10746n = 900;

    /* renamed from: o, reason: collision with root package name */
    private long f10748o = 3600;

    /* renamed from: p, reason: collision with root package name */
    private boolean f10750p = true;

    /* renamed from: q, reason: collision with root package name */
    private boolean f10752q = true;

    /* renamed from: r, reason: collision with root package name */
    private long f10754r = 120000;

    /* renamed from: s, reason: collision with root package name */
    private int f10756s = 10;

    /* renamed from: t, reason: collision with root package name */
    private int f10758t = 10;

    /* renamed from: u, reason: collision with root package name */
    private int f10760u = 5;

    /* renamed from: v, reason: collision with root package name */
    private int f10762v = 10;

    /* renamed from: w, reason: collision with root package name */
    private int f10764w = 30;

    /* renamed from: x, reason: collision with root package name */
    private int f10766x = 70;

    /* renamed from: y, reason: collision with root package name */
    private int f10768y = 300;

    /* renamed from: z, reason: collision with root package name */
    private long f10770z = 3600;
    private boolean A = true;
    private int B = 3;
    private int C = 350;
    private boolean D = true;
    private boolean E = true;
    private boolean F = false;
    private boolean G = false;
    private int H = DataLinkConstants.USER_PROFILE_LABEL;
    private int I = 490;
    private boolean J = true;
    private boolean K = true;
    private int L = 30;
    private int M = 0;
    private boolean N = true;
    private boolean O = true;
    private boolean P = false;
    private boolean Q = false;
    private boolean R = true;
    private int S = 5400000;
    private boolean T = true;
    private boolean U = true;
    private boolean V = false;
    private List<String> W = new ArrayList();
    private Object X = new Object();
    private List<String> Z = new ArrayList();

    /* renamed from: a0, reason: collision with root package name */
    private List<String> f10721a0 = new ArrayList(OplusBatteryConstants.f19354f);

    /* renamed from: b0, reason: collision with root package name */
    private List<String> f10723b0 = new ArrayList(OplusBatteryConstants.f19355g);

    /* renamed from: c0, reason: collision with root package name */
    private int f10725c0 = 450;

    /* renamed from: d0, reason: collision with root package name */
    private boolean f10727d0 = true;

    /* renamed from: e0, reason: collision with root package name */
    private boolean f10729e0 = false;

    /* renamed from: f0, reason: collision with root package name */
    private boolean f10731f0 = true;

    /* renamed from: g0, reason: collision with root package name */
    private long f10733g0 = 10000;

    /* renamed from: h0, reason: collision with root package name */
    private int f10735h0 = 380;

    /* renamed from: i0, reason: collision with root package name */
    private float f10737i0 = 1.0f;

    /* renamed from: j0, reason: collision with root package name */
    private int f10739j0 = 350;

    /* renamed from: k0, reason: collision with root package name */
    private long f10741k0 = 60000;

    /* renamed from: l0, reason: collision with root package name */
    private long f10743l0 = 10000;

    /* renamed from: m0, reason: collision with root package name */
    private int f10745m0 = 0;

    /* renamed from: n0, reason: collision with root package name */
    private int f10747n0 = 20;

    /* renamed from: o0, reason: collision with root package name */
    private int f10749o0 = 50;

    /* renamed from: p0, reason: collision with root package name */
    private float f10751p0 = 0.2f;

    /* renamed from: q0, reason: collision with root package name */
    private float f10753q0 = 0.4f;

    /* renamed from: r0, reason: collision with root package name */
    private float f10755r0 = 0.4f;

    /* renamed from: s0, reason: collision with root package name */
    private long f10757s0 = 3600000;

    /* renamed from: t0, reason: collision with root package name */
    private boolean f10759t0 = true;

    /* renamed from: u0, reason: collision with root package name */
    private long f10761u0 = 3600000;

    /* renamed from: v0, reason: collision with root package name */
    private int f10763v0 = 40;

    /* renamed from: w0, reason: collision with root package name */
    private int f10765w0 = 40;

    /* renamed from: x0, reason: collision with root package name */
    private int f10767x0 = 40;

    /* renamed from: y0, reason: collision with root package name */
    private boolean f10769y0 = true;

    /* renamed from: z0, reason: collision with root package name */
    private boolean f10771z0 = false;
    private String A0 = "";
    private List<String> B0 = new ArrayList();

    private ConfigUpdateUtil(Context context) {
        this.Y = null;
        this.Y = context;
    }

    private void A0(String str) {
        this.f10726d = Settings.System.getInt(this.Y.getContentResolver(), "guardelf_config_list_version", 0);
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt == 0 || parseInt <= this.f10726d) {
                return;
            }
            Settings.System.putInt(this.Y.getContentResolver(), "guardelf_config_list_version", parseInt);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void A1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10737i0 = Float.parseFloat(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void B0(String str) {
        if (str == null) {
            return;
        }
        if ("true".equals(str)) {
            this.f10771z0 = true;
        } else if ("false".equals(str)) {
            this.f10771z0 = false;
        } else {
            this.f10771z0 = AppFeature.D();
        }
    }

    private void B1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10743l0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void C1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10739j0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void D1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10741k0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void E1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10731f0 = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void F() {
        if (this.F) {
            Settings.Global.putInt(this.Y.getContentResolver(), "data_space_monitor_curve_switch", 0);
        } else {
            Settings.Global.putInt(this.Y.getContentResolver(), "data_space_monitor_curve_switch", 1);
        }
    }

    private void F0(String str) {
        if (str == null) {
            return;
        }
        try {
            O0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            O0 = 60000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void G0(String str) {
        if (str == null) {
            return;
        }
        try {
            Q0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            Q0 = 5000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void G1(String str) {
        if (str == null) {
            return;
        }
        try {
            S0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            S0 = 10;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void H0(String str) {
        if (str == null) {
            return;
        }
        try {
            R0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            R0 = 40000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void H1(String str) {
        if (str == null) {
            return;
        }
        try {
            U0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            U0 = 40;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0b68  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void I(String str) {
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        int i10;
        float f10;
        float f11;
        float f12;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        float f13;
        float f14;
        int i17;
        String str2;
        XmlPullParserException xmlPullParserException;
        int i18;
        int i19;
        int i20;
        NumberFormatException numberFormatException;
        IOException iOException;
        int i21;
        String str3;
        int i22;
        boolean z14;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        String str4;
        String str5;
        int i28;
        int i29;
        int i30;
        float f15;
        float f16;
        boolean z15;
        int i31;
        float f17;
        boolean z16;
        int i32;
        int i33;
        int i34;
        boolean z17;
        int i35;
        float f18;
        XmlPullParserException e10;
        NumberFormatException e11;
        IOException e12;
        int i36;
        int i37;
        XmlPullParserException e13;
        NumberFormatException e14;
        IOException e15;
        int i38;
        int i39;
        float f19;
        int i40;
        StringBuilder sb2;
        Log.d("ConfigUpdateUtil", "parseChargeXmlValue");
        if (str == null || str.isEmpty()) {
            return;
        }
        int i41 = 5;
        float[] fArr = {0.7f, 0.775f, 0.85f, 0.925f, 1.0f};
        float[] fArr2 = {0.7f, 0.775f, 0.85f, 0.925f, 1.0f};
        int i42 = 240;
        int i43 = 90;
        int i44 = 65;
        int i45 = 33;
        String str6 = "0";
        String str7 = "";
        int i46 = 60;
        int i47 = 120;
        int i48 = 3;
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new StringReader(str));
            newPullParser.nextTag();
            int i49 = 5;
            int i50 = 120;
            f11 = 0.6f;
            f12 = 0.6f;
            i11 = 30;
            i12 = 30;
            i13 = 0;
            i22 = 3;
            i14 = 3;
            i15 = 3;
            f14 = 0.9f;
            i17 = 180;
            i23 = 15;
            float f20 = 0.4f;
            z14 = true;
            z11 = true;
            str2 = null;
            z12 = true;
            z13 = true;
            i19 = 60;
            int i51 = 120;
            int i52 = 60;
            str4 = "";
            str5 = "0";
            i28 = 33;
            i29 = 65;
            int i53 = 90;
            i30 = 240;
            f15 = 0.5f;
            i18 = 5;
            while (true) {
                try {
                    int next = newPullParser.next();
                    StringBuilder sb3 = new StringBuilder();
                    i10 = i49;
                    try {
                        sb3.append("parser.next(): ");
                        sb3.append(next);
                        Log.d("ConfigUpdateUtil", sb3.toString());
                        if (next == 2) {
                            String name = newPullParser.getName();
                            StringBuilder sb4 = new StringBuilder();
                            float f21 = f14;
                            try {
                                sb4.append("parser.getName(): ");
                                sb4.append(name);
                                Log.d("ConfigUpdateUtil", sb4.toString());
                                if ("lifeModeTrigger".equals(name)) {
                                    z14 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    StringBuilder sb5 = new StringBuilder();
                                    i35 = i41;
                                    try {
                                        sb5.append("trigger: ");
                                        sb5.append(name);
                                        Log.d("ConfigUpdateUtil", sb5.toString());
                                    } catch (IOException e16) {
                                        e = e16;
                                        iOException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6 = new StringBuilder();
                                        int i54 = i33;
                                        sb6.append("bSuccess: ");
                                        sb6.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e17) {
                                        e = e17;
                                        numberFormatException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62 = new StringBuilder();
                                        int i542 = i33;
                                        sb62.append("bSuccess: ");
                                        sb62.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e18) {
                                        e = e18;
                                        xmlPullParserException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622 = new StringBuilder();
                                        int i5422 = i33;
                                        sb622.append("bSuccess: ");
                                        sb622.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622.toString());
                                        if (z16) {
                                        }
                                    }
                                } else {
                                    i35 = i41;
                                }
                                if ("smartChargeNotificationTimesBeRunning".equals(name)) {
                                    i22 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    Log.d("ConfigUpdateUtil", "smartChargeNotificationTimesBeRunning: " + name);
                                }
                                if ("longChargeProtectFeatureTrigger".equals(name)) {
                                    z11 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    Log.d("ConfigUpdateUtil", "longChargeProtectFeatureTrigger: " + name);
                                }
                                if ("longChargeProtectStudyDay".equals(name)) {
                                    int parseInt = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    try {
                                        StringBuilder sb7 = new StringBuilder();
                                        i11 = parseInt;
                                        sb7.append("longChargeProtectStudyDay: ");
                                        sb7.append(name);
                                        Log.d("ConfigUpdateUtil", sb7.toString());
                                    } catch (IOException e19) {
                                        e = e19;
                                        i11 = parseInt;
                                        iOException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222 = new StringBuilder();
                                        int i54222 = i33;
                                        sb6222.append("bSuccess: ");
                                        sb6222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e20) {
                                        e = e20;
                                        i11 = parseInt;
                                        numberFormatException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222 = new StringBuilder();
                                        int i542222 = i33;
                                        sb62222.append("bSuccess: ");
                                        sb62222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e21) {
                                        e = e21;
                                        i11 = parseInt;
                                        xmlPullParserException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222 = new StringBuilder();
                                        int i5422222 = i33;
                                        sb622222.append("bSuccess: ");
                                        sb622222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222.toString());
                                        if (z16) {
                                        }
                                    }
                                }
                                if ("longChargeBehaviorR".equals(name)) {
                                    int parseInt2 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    try {
                                        StringBuilder sb8 = new StringBuilder();
                                        i17 = parseInt2;
                                        sb8.append("longChargeBehaviorR: ");
                                        sb8.append(name);
                                        Log.d("ConfigUpdateUtil", sb8.toString());
                                    } catch (IOException e22) {
                                        e = e22;
                                        i17 = parseInt2;
                                        iOException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222 = new StringBuilder();
                                        int i54222222 = i33;
                                        sb6222222.append("bSuccess: ");
                                        sb6222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e23) {
                                        e = e23;
                                        i17 = parseInt2;
                                        numberFormatException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222 = new StringBuilder();
                                        int i542222222 = i33;
                                        sb62222222.append("bSuccess: ");
                                        sb62222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e24) {
                                        e = e24;
                                        i17 = parseInt2;
                                        xmlPullParserException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222 = new StringBuilder();
                                        int i5422222222 = i33;
                                        sb622222222.append("bSuccess: ");
                                        sb622222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222.toString());
                                        if (z16) {
                                        }
                                    }
                                }
                                if ("longChargeBehaviorY".equals(name)) {
                                    int parseInt3 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    try {
                                        StringBuilder sb9 = new StringBuilder();
                                        i23 = parseInt3;
                                        sb9.append("longChargeBehaviorY: ");
                                        sb9.append(name);
                                        Log.d("ConfigUpdateUtil", sb9.toString());
                                    } catch (IOException e25) {
                                        e = e25;
                                        i23 = parseInt3;
                                        iOException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222 = new StringBuilder();
                                        int i54222222222 = i33;
                                        sb6222222222.append("bSuccess: ");
                                        sb6222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e26) {
                                        e = e26;
                                        i23 = parseInt3;
                                        numberFormatException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222 = new StringBuilder();
                                        int i542222222222 = i33;
                                        sb62222222222.append("bSuccess: ");
                                        sb62222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e27) {
                                        e = e27;
                                        i23 = parseInt3;
                                        xmlPullParserException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222 = new StringBuilder();
                                        int i5422222222222 = i33;
                                        sb622222222222.append("bSuccess: ");
                                        sb622222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                }
                                if ("longChargeCalculate".equals(name)) {
                                    f11 = Float.parseFloat(newPullParser.getAttributeValue(null, "value1"));
                                    float parseFloat = Float.parseFloat(newPullParser.getAttributeValue(null, "value2"));
                                    try {
                                        StringBuilder sb10 = new StringBuilder();
                                        f20 = parseFloat;
                                        sb10.append("longChargeCalculate: ");
                                        sb10.append(name);
                                        Log.d("ConfigUpdateUtil", sb10.toString());
                                    } catch (IOException e28) {
                                        e = e28;
                                        f20 = parseFloat;
                                        iOException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222222 = new StringBuilder();
                                        int i54222222222222 = i33;
                                        sb6222222222222.append("bSuccess: ");
                                        sb6222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e29) {
                                        e = e29;
                                        f20 = parseFloat;
                                        numberFormatException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222222 = new StringBuilder();
                                        int i542222222222222 = i33;
                                        sb62222222222222.append("bSuccess: ");
                                        sb62222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e30) {
                                        e = e30;
                                        f20 = parseFloat;
                                        xmlPullParserException = e;
                                        f10 = f15;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222222 = new StringBuilder();
                                        int i5422222222222222 = i33;
                                        sb622222222222222.append("bSuccess: ");
                                        sb622222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                }
                                if ("longChargeProtectCloseTimes".equals(name)) {
                                    i14 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    Log.d("ConfigUpdateUtil", "longChargeProtectCloseTimes: " + name);
                                }
                                if ("longChargeProtectJudgmentThreshold".equals(name)) {
                                    f21 = Float.parseFloat(newPullParser.getAttributeValue(null, "d1"));
                                    fArr[0] = Float.parseFloat(newPullParser.getAttributeValue(null, "m1_1"));
                                    fArr[1] = Float.parseFloat(newPullParser.getAttributeValue(null, "m1_2"));
                                    fArr[2] = Float.parseFloat(newPullParser.getAttributeValue(null, "m1_3"));
                                    fArr[3] = Float.parseFloat(newPullParser.getAttributeValue(null, "m1_4"));
                                    fArr[4] = Float.parseFloat(newPullParser.getAttributeValue(null, "m1_5"));
                                    fArr2[0] = Float.parseFloat(newPullParser.getAttributeValue(null, "m2_1"));
                                    fArr2[1] = Float.parseFloat(newPullParser.getAttributeValue(null, "m2_2"));
                                    fArr2[2] = Float.parseFloat(newPullParser.getAttributeValue(null, "m2_3"));
                                    fArr2[3] = Float.parseFloat(newPullParser.getAttributeValue(null, "m2_4"));
                                    fArr2[4] = Float.parseFloat(newPullParser.getAttributeValue(null, "m2_5"));
                                    f18 = Float.parseFloat(newPullParser.getAttributeValue(null, "c1"));
                                    try {
                                        Log.d("ConfigUpdateUtil", "longChargeProtectJudgmentThreshold: " + name);
                                    } catch (IOException e31) {
                                        e12 = e31;
                                        f10 = f18;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        iOException = e12;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222222222 = new StringBuilder();
                                        int i54222222222222222 = i33;
                                        sb6222222222222222.append("bSuccess: ");
                                        sb6222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e32) {
                                        e11 = e32;
                                        f10 = f18;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        numberFormatException = e11;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222222222 = new StringBuilder();
                                        int i542222222222222222 = i33;
                                        sb62222222222222222.append("bSuccess: ");
                                        sb62222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e33) {
                                        e10 = e33;
                                        f10 = f18;
                                        i42 = i30;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        xmlPullParserException = e10;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222222222 = new StringBuilder();
                                        int i5422222222222222222 = i33;
                                        sb622222222222222222.append("bSuccess: ");
                                        sb622222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                } else {
                                    f18 = f15;
                                }
                            } catch (IOException e34) {
                                iOException = e34;
                                f10 = f15;
                                i42 = i30;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i52;
                                i47 = i51;
                                i16 = i23;
                                i20 = i50;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                            } catch (NumberFormatException e35) {
                                numberFormatException = e35;
                                f10 = f15;
                                i42 = i30;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i52;
                                i47 = i51;
                                i16 = i23;
                                i20 = i50;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                            } catch (XmlPullParserException e36) {
                                xmlPullParserException = e36;
                                f10 = f15;
                                i42 = i30;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i52;
                                i47 = i51;
                                i16 = i23;
                                i20 = i50;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                            }
                            try {
                                if ("LongChargeProtectPowerFullTimeMinute".equals(name)) {
                                    int parseInt4 = Integer.parseInt(newPullParser.getAttributeValue(null, "usb"));
                                    try {
                                        i36 = Integer.parseInt(newPullParser.getAttributeValue(null, "small65W"));
                                        f10 = f18;
                                    } catch (IOException e37) {
                                        f10 = f18;
                                        iOException = e37;
                                        i42 = parseInt4;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222222222222 = new StringBuilder();
                                        int i54222222222222222222 = i33;
                                        sb6222222222222222222.append("bSuccess: ");
                                        sb6222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e38) {
                                        f10 = f18;
                                        numberFormatException = e38;
                                        i42 = parseInt4;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222222222222 = new StringBuilder();
                                        int i542222222222222222222 = i33;
                                        sb62222222222222222222.append("bSuccess: ");
                                        sb62222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e39) {
                                        f10 = f18;
                                        xmlPullParserException = e39;
                                        i42 = parseInt4;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i47 = i51;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222222222222 = new StringBuilder();
                                        int i5422222222222222222222 = i33;
                                        sb622222222222222222222.append("bSuccess: ");
                                        sb622222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                    try {
                                        i20 = Integer.parseInt(newPullParser.getAttributeValue(null, "wireless"));
                                        try {
                                            int parseInt5 = Integer.parseInt(newPullParser.getAttributeValue(null, "big65W"));
                                            try {
                                                StringBuilder sb11 = new StringBuilder();
                                                i52 = parseInt5;
                                                sb11.append("LongChargeProtectPowerFullTimeMinute: ");
                                                sb11.append(name);
                                                Log.d("ConfigUpdateUtil", sb11.toString());
                                                i42 = parseInt4;
                                            } catch (IOException e40) {
                                                e = e40;
                                                i52 = parseInt5;
                                                iOException = e;
                                                i42 = parseInt4;
                                                i16 = i23;
                                                f13 = f20;
                                                z10 = z14;
                                                i48 = i22;
                                                f14 = f21;
                                                i41 = i35;
                                                int i55 = i52;
                                                i47 = i36;
                                                i43 = i53;
                                                i44 = i29;
                                                i45 = i28;
                                                str6 = str5;
                                                str7 = str4;
                                                i46 = i55;
                                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                                i33 = i46;
                                                i32 = i47;
                                                z16 = false;
                                                i22 = i48;
                                                z14 = z10;
                                                i24 = i13;
                                                str3 = str2;
                                                i27 = i10;
                                                i23 = i16;
                                                i21 = i18;
                                                i25 = i43;
                                                str4 = str7;
                                                f17 = f11;
                                                f16 = f13;
                                                i30 = i42;
                                                str5 = str6;
                                                i31 = i20;
                                                f15 = f10;
                                                i26 = i41;
                                                i28 = i45;
                                                z15 = z11;
                                                i29 = i44;
                                                StringBuilder sb6222222222222222222222 = new StringBuilder();
                                                int i54222222222222222222222 = i33;
                                                sb6222222222222222222222.append("bSuccess: ");
                                                sb6222222222222222222222.append(z16);
                                                Log.d("ConfigUpdateUtil", sb6222222222222222222222.toString());
                                                if (z16) {
                                                }
                                            } catch (NumberFormatException e41) {
                                                e = e41;
                                                i52 = parseInt5;
                                                numberFormatException = e;
                                                i42 = parseInt4;
                                                i16 = i23;
                                                f13 = f20;
                                                z10 = z14;
                                                i48 = i22;
                                                f14 = f21;
                                                i41 = i35;
                                                int i56 = i52;
                                                i47 = i36;
                                                i43 = i53;
                                                i44 = i29;
                                                i45 = i28;
                                                str6 = str5;
                                                str7 = str4;
                                                i46 = i56;
                                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                                i33 = i46;
                                                i32 = i47;
                                                z16 = false;
                                                i22 = i48;
                                                z14 = z10;
                                                i24 = i13;
                                                str3 = str2;
                                                i27 = i10;
                                                i23 = i16;
                                                i21 = i18;
                                                i25 = i43;
                                                str4 = str7;
                                                f17 = f11;
                                                f16 = f13;
                                                i30 = i42;
                                                str5 = str6;
                                                i31 = i20;
                                                f15 = f10;
                                                i26 = i41;
                                                i28 = i45;
                                                z15 = z11;
                                                i29 = i44;
                                                StringBuilder sb62222222222222222222222 = new StringBuilder();
                                                int i542222222222222222222222 = i33;
                                                sb62222222222222222222222.append("bSuccess: ");
                                                sb62222222222222222222222.append(z16);
                                                Log.d("ConfigUpdateUtil", sb62222222222222222222222.toString());
                                                if (z16) {
                                                }
                                            } catch (XmlPullParserException e42) {
                                                e = e42;
                                                i52 = parseInt5;
                                                xmlPullParserException = e;
                                                i42 = parseInt4;
                                                i16 = i23;
                                                f13 = f20;
                                                z10 = z14;
                                                i48 = i22;
                                                f14 = f21;
                                                i41 = i35;
                                                int i57 = i52;
                                                i47 = i36;
                                                i43 = i53;
                                                i44 = i29;
                                                i45 = i28;
                                                str6 = str5;
                                                str7 = str4;
                                                i46 = i57;
                                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                                i33 = i46;
                                                i32 = i47;
                                                z16 = false;
                                                i22 = i48;
                                                z14 = z10;
                                                i24 = i13;
                                                str3 = str2;
                                                i27 = i10;
                                                i23 = i16;
                                                i21 = i18;
                                                i25 = i43;
                                                str4 = str7;
                                                f17 = f11;
                                                f16 = f13;
                                                i30 = i42;
                                                str5 = str6;
                                                i31 = i20;
                                                f15 = f10;
                                                i26 = i41;
                                                i28 = i45;
                                                z15 = z11;
                                                i29 = i44;
                                                StringBuilder sb622222222222222222222222 = new StringBuilder();
                                                int i5422222222222222222222222 = i33;
                                                sb622222222222222222222222.append("bSuccess: ");
                                                sb622222222222222222222222.append(z16);
                                                Log.d("ConfigUpdateUtil", sb622222222222222222222222.toString());
                                                if (z16) {
                                                }
                                            }
                                        } catch (IOException e43) {
                                            e = e43;
                                        } catch (NumberFormatException e44) {
                                            e = e44;
                                        } catch (XmlPullParserException e45) {
                                            e = e45;
                                        }
                                    } catch (IOException e46) {
                                        iOException = e46;
                                        i42 = parseInt4;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        int i552 = i52;
                                        i47 = i36;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i552;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222222222222222222 = new StringBuilder();
                                        int i54222222222222222222222222 = i33;
                                        sb6222222222222222222222222.append("bSuccess: ");
                                        sb6222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e47) {
                                        numberFormatException = e47;
                                        i42 = parseInt4;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        int i562 = i52;
                                        i47 = i36;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i562;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222222222222222222 = new StringBuilder();
                                        int i542222222222222222222222222 = i33;
                                        sb62222222222222222222222222.append("bSuccess: ");
                                        sb62222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e48) {
                                        xmlPullParserException = e48;
                                        i42 = parseInt4;
                                        i16 = i23;
                                        i20 = i50;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        int i572 = i52;
                                        i47 = i36;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i572;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222222222222222222 = new StringBuilder();
                                        int i5422222222222222222222222222 = i33;
                                        sb622222222222222222222222222.append("bSuccess: ");
                                        sb622222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                } else {
                                    f10 = f18;
                                    i42 = i30;
                                    i36 = i51;
                                    i20 = i50;
                                }
                            } catch (IOException e49) {
                                f10 = f18;
                                iOException = e49;
                                i42 = i30;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i52;
                                i47 = i51;
                                i16 = i23;
                                i20 = i50;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                                f14 = f21;
                                i41 = i35;
                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                i33 = i46;
                                i32 = i47;
                                z16 = false;
                                i22 = i48;
                                z14 = z10;
                                i24 = i13;
                                str3 = str2;
                                i27 = i10;
                                i23 = i16;
                                i21 = i18;
                                i25 = i43;
                                str4 = str7;
                                f17 = f11;
                                f16 = f13;
                                i30 = i42;
                                str5 = str6;
                                i31 = i20;
                                f15 = f10;
                                i26 = i41;
                                i28 = i45;
                                z15 = z11;
                                i29 = i44;
                                StringBuilder sb6222222222222222222222222222 = new StringBuilder();
                                int i54222222222222222222222222222 = i33;
                                sb6222222222222222222222222222.append("bSuccess: ");
                                sb6222222222222222222222222222.append(z16);
                                Log.d("ConfigUpdateUtil", sb6222222222222222222222222222.toString());
                                if (z16) {
                                }
                            } catch (NumberFormatException e50) {
                                f10 = f18;
                                numberFormatException = e50;
                                i42 = i30;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i52;
                                i47 = i51;
                                i16 = i23;
                                i20 = i50;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                                f14 = f21;
                                i41 = i35;
                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                i33 = i46;
                                i32 = i47;
                                z16 = false;
                                i22 = i48;
                                z14 = z10;
                                i24 = i13;
                                str3 = str2;
                                i27 = i10;
                                i23 = i16;
                                i21 = i18;
                                i25 = i43;
                                str4 = str7;
                                f17 = f11;
                                f16 = f13;
                                i30 = i42;
                                str5 = str6;
                                i31 = i20;
                                f15 = f10;
                                i26 = i41;
                                i28 = i45;
                                z15 = z11;
                                i29 = i44;
                                StringBuilder sb62222222222222222222222222222 = new StringBuilder();
                                int i542222222222222222222222222222 = i33;
                                sb62222222222222222222222222222.append("bSuccess: ");
                                sb62222222222222222222222222222.append(z16);
                                Log.d("ConfigUpdateUtil", sb62222222222222222222222222222.toString());
                                if (z16) {
                                }
                            } catch (XmlPullParserException e51) {
                                f10 = f18;
                                xmlPullParserException = e51;
                                i42 = i30;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i52;
                                i47 = i51;
                                i16 = i23;
                                i20 = i50;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                                f14 = f21;
                                i41 = i35;
                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                i33 = i46;
                                i32 = i47;
                                z16 = false;
                                i22 = i48;
                                z14 = z10;
                                i24 = i13;
                                str3 = str2;
                                i27 = i10;
                                i23 = i16;
                                i21 = i18;
                                i25 = i43;
                                str4 = str7;
                                f17 = f11;
                                f16 = f13;
                                i30 = i42;
                                str5 = str6;
                                i31 = i20;
                                f15 = f10;
                                i26 = i41;
                                i28 = i45;
                                z15 = z11;
                                i29 = i44;
                                StringBuilder sb622222222222222222222222222222 = new StringBuilder();
                                int i5422222222222222222222222222222 = i33;
                                sb622222222222222222222222222222.append("bSuccess: ");
                                sb622222222222222222222222222222.append(z16);
                                Log.d("ConfigUpdateUtil", sb622222222222222222222222222222.toString());
                                if (z16) {
                                }
                            }
                            try {
                                if ("longChargeProtectForgetMechanismTime".equals(name)) {
                                    int parseInt6 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    try {
                                        sb2 = new StringBuilder();
                                        i40 = parseInt6;
                                    } catch (IOException e52) {
                                        e = e52;
                                        i40 = parseInt6;
                                    } catch (NumberFormatException e53) {
                                        e = e53;
                                        i40 = parseInt6;
                                    } catch (XmlPullParserException e54) {
                                        e = e54;
                                        i40 = parseInt6;
                                    }
                                    try {
                                        sb2.append("longChargeProtectForgetMechanismTime: ");
                                        sb2.append(name);
                                        Log.d("ConfigUpdateUtil", sb2.toString());
                                        i12 = i40;
                                    } catch (IOException e55) {
                                        e = e55;
                                        iOException = e;
                                        i16 = i23;
                                        i12 = i40;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        int i5522 = i52;
                                        i47 = i36;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i5522;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222222222222222222222222 = new StringBuilder();
                                        int i54222222222222222222222222222222 = i33;
                                        sb6222222222222222222222222222222.append("bSuccess: ");
                                        sb6222222222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e56) {
                                        e = e56;
                                        numberFormatException = e;
                                        i16 = i23;
                                        i12 = i40;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        int i5622 = i52;
                                        i47 = i36;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i5622;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222222222222222222222222 = new StringBuilder();
                                        int i542222222222222222222222222222222 = i33;
                                        sb62222222222222222222222222222222.append("bSuccess: ");
                                        sb62222222222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e57) {
                                        e = e57;
                                        xmlPullParserException = e;
                                        i16 = i23;
                                        i12 = i40;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        int i5722 = i52;
                                        i47 = i36;
                                        i43 = i53;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i5722;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222222222222222222222222 = new StringBuilder();
                                        int i5422222222222222222222222222222222 = i33;
                                        sb622222222222222222222222222222222.append("bSuccess: ");
                                        sb622222222222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                }
                                if ("longChargeProtectGuideTimes".equals(name)) {
                                    i15 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    Log.d("ConfigUpdateUtil", "longChargeProtectGuideTimes: " + name);
                                }
                                if ("longChargeProtectAIAccuracy".equals(name)) {
                                    float parseFloat2 = Float.parseFloat(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    try {
                                        StringBuilder sb12 = new StringBuilder();
                                        f19 = parseFloat2;
                                        try {
                                            sb12.append("longChargeProtectAIAccuracy: ");
                                            sb12.append(name);
                                            Log.d("ConfigUpdateUtil", sb12.toString());
                                            f12 = f19;
                                        } catch (IOException e58) {
                                            e = e58;
                                            iOException = e;
                                            i16 = i23;
                                            f12 = f19;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            i41 = i35;
                                            int i55222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i55222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb6222222222222222222222222222222222 = new StringBuilder();
                                            int i54222222222222222222222222222222222 = i33;
                                            sb6222222222222222222222222222222222.append("bSuccess: ");
                                            sb6222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (NumberFormatException e59) {
                                            e = e59;
                                            numberFormatException = e;
                                            i16 = i23;
                                            f12 = f19;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            i41 = i35;
                                            int i56222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i56222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb62222222222222222222222222222222222 = new StringBuilder();
                                            int i542222222222222222222222222222222222 = i33;
                                            sb62222222222222222222222222222222222.append("bSuccess: ");
                                            sb62222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (XmlPullParserException e60) {
                                            e = e60;
                                            xmlPullParserException = e;
                                            i16 = i23;
                                            f12 = f19;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            i41 = i35;
                                            int i57222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i57222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb622222222222222222222222222222222222 = new StringBuilder();
                                            int i5422222222222222222222222222222222222 = i33;
                                            sb622222222222222222222222222222222222.append("bSuccess: ");
                                            sb622222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        }
                                    } catch (IOException e61) {
                                        e = e61;
                                        f19 = parseFloat2;
                                    } catch (NumberFormatException e62) {
                                        e = e62;
                                        f19 = parseFloat2;
                                    } catch (XmlPullParserException e63) {
                                        e = e63;
                                        f19 = parseFloat2;
                                    }
                                }
                                if ("OPLUSReserveSoc".equals(name)) {
                                    i13 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    Log.d("ConfigUpdateUtil", "reserveSocData: " + name);
                                }
                                if ("CHGOlcConfig".equals(name)) {
                                    str2 = newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE);
                                    Log.d("ConfigUpdateUtil", "chgOlcConfig: " + name);
                                }
                                if ("SlowChargeProtectFeatureTrigger".equals(name)) {
                                    z12 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    Log.d("ConfigUpdateUtil", "slowChargeProtectFeatureTrigger: " + name);
                                }
                                if ("SlowChargeProtectPowerFullTimeMinute".equals(name)) {
                                    int parseInt7 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                    try {
                                        Log.d("ConfigUpdateUtil", "slowChargeProtectPowerFullTimeMinute: " + name);
                                        i53 = parseInt7;
                                    } catch (IOException e64) {
                                        e12 = e64;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i16 = i23;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        i47 = i36;
                                        i43 = parseInt7;
                                        iOException = e12;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb6222222222222222222222222222222222222 = new StringBuilder();
                                        int i54222222222222222222222222222222222222 = i33;
                                        sb6222222222222222222222222222222222222.append("bSuccess: ");
                                        sb6222222222222222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (NumberFormatException e65) {
                                        e11 = e65;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i16 = i23;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        i47 = i36;
                                        i43 = parseInt7;
                                        numberFormatException = e11;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb62222222222222222222222222222222222222 = new StringBuilder();
                                        int i542222222222222222222222222222222222222 = i33;
                                        sb62222222222222222222222222222222222222.append("bSuccess: ");
                                        sb62222222222222222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    } catch (XmlPullParserException e66) {
                                        e10 = e66;
                                        i44 = i29;
                                        i45 = i28;
                                        str6 = str5;
                                        str7 = str4;
                                        i46 = i52;
                                        i16 = i23;
                                        f13 = f20;
                                        z10 = z14;
                                        i48 = i22;
                                        f14 = f21;
                                        i41 = i35;
                                        i47 = i36;
                                        i43 = parseInt7;
                                        xmlPullParserException = e10;
                                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                        i33 = i46;
                                        i32 = i47;
                                        z16 = false;
                                        i22 = i48;
                                        z14 = z10;
                                        i24 = i13;
                                        str3 = str2;
                                        i27 = i10;
                                        i23 = i16;
                                        i21 = i18;
                                        i25 = i43;
                                        str4 = str7;
                                        f17 = f11;
                                        f16 = f13;
                                        i30 = i42;
                                        str5 = str6;
                                        i31 = i20;
                                        f15 = f10;
                                        i26 = i41;
                                        i28 = i45;
                                        z15 = z11;
                                        i29 = i44;
                                        StringBuilder sb622222222222222222222222222222222222222 = new StringBuilder();
                                        int i5422222222222222222222222222222222222222 = i33;
                                        sb622222222222222222222222222222222222222.append("bSuccess: ");
                                        sb622222222222222222222222222222222222222.append(z16);
                                        Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222.toString());
                                        if (z16) {
                                        }
                                    }
                                }
                                if ("SlowChargeProtectAiAccuracyPercent".equals(name)) {
                                    i37 = Integer.parseInt(newPullParser.getAttributeValue(null, "sleepPercent"));
                                    try {
                                        int parseInt8 = Integer.parseInt(newPullParser.getAttributeValue(null, "wakePercent"));
                                        try {
                                            StringBuilder sb13 = new StringBuilder();
                                            i10 = parseInt8;
                                            sb13.append("slowChargeProtectAiAccuracyPercent: ");
                                            sb13.append(name);
                                            Log.d("ConfigUpdateUtil", sb13.toString());
                                            i41 = i37;
                                        } catch (IOException e67) {
                                            e15 = e67;
                                            i10 = parseInt8;
                                            iOException = e15;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i552222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i552222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb6222222222222222222222222222222222222222 = new StringBuilder();
                                            int i54222222222222222222222222222222222222222 = i33;
                                            sb6222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb6222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (NumberFormatException e68) {
                                            e14 = e68;
                                            i10 = parseInt8;
                                            numberFormatException = e14;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i562222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i562222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb62222222222222222222222222222222222222222 = new StringBuilder();
                                            int i542222222222222222222222222222222222222222 = i33;
                                            sb62222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb62222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (XmlPullParserException e69) {
                                            e13 = e69;
                                            i10 = parseInt8;
                                            xmlPullParserException = e13;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i572222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i572222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb622222222222222222222222222222222222222222 = new StringBuilder();
                                            int i5422222222222222222222222222222222222222222 = i33;
                                            sb622222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb622222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        }
                                    } catch (IOException e70) {
                                        e15 = e70;
                                    } catch (NumberFormatException e71) {
                                        e14 = e71;
                                    } catch (XmlPullParserException e72) {
                                        e13 = e72;
                                    }
                                } else {
                                    i41 = i35;
                                }
                                try {
                                    if ("SlowChargeProtectTriggerThresholdWattage".equals(name)) {
                                        i29 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                        StringBuilder sb14 = new StringBuilder();
                                        i37 = i41;
                                        sb14.append("slowChargeProtectTriggerThresholdWattage: ");
                                        sb14.append(name);
                                        Log.d("ConfigUpdateUtil", sb14.toString());
                                    } else {
                                        i37 = i41;
                                    }
                                    if ("SlowChargeProtectSlowPercent".equals(name)) {
                                        int parseInt9 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                        try {
                                            StringBuilder sb15 = new StringBuilder();
                                            i19 = parseInt9;
                                            sb15.append("slowChargeProtectSlowPercent: ");
                                            sb15.append(name);
                                            Log.d("ConfigUpdateUtil", sb15.toString());
                                        } catch (IOException e73) {
                                            e15 = e73;
                                            i19 = parseInt9;
                                            iOException = e15;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i5522222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i5522222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb6222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i54222222222222222222222222222222222222222222 = i33;
                                            sb6222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb6222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (NumberFormatException e74) {
                                            e14 = e74;
                                            i19 = parseInt9;
                                            numberFormatException = e14;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i5622222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i5622222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb62222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i542222222222222222222222222222222222222222222 = i33;
                                            sb62222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb62222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (XmlPullParserException e75) {
                                            e13 = e75;
                                            i19 = parseInt9;
                                            xmlPullParserException = e13;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i5722222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i5722222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb622222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i5422222222222222222222222222222222222222222222 = i33;
                                            sb622222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb622222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        }
                                    }
                                    if ("SlowChargeProtectSlowWattage".equals(name)) {
                                        int parseInt10 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                        try {
                                            StringBuilder sb16 = new StringBuilder();
                                            i28 = parseInt10;
                                            sb16.append("slowChargeProtectSlowWattage: ");
                                            sb16.append(name);
                                            Log.d("ConfigUpdateUtil", sb16.toString());
                                        } catch (IOException e76) {
                                            e15 = e76;
                                            i28 = parseInt10;
                                            iOException = e15;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i55222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i55222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb6222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i54222222222222222222222222222222222222222222222 = i33;
                                            sb6222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb6222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (NumberFormatException e77) {
                                            e14 = e77;
                                            i28 = parseInt10;
                                            numberFormatException = e14;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i56222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i56222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb62222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i542222222222222222222222222222222222222222222222 = i33;
                                            sb62222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb62222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (XmlPullParserException e78) {
                                            e13 = e78;
                                            i28 = parseInt10;
                                            xmlPullParserException = e13;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i57222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i57222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb622222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i5422222222222222222222222222222222222222222222222 = i33;
                                            sb622222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb622222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        }
                                    }
                                    if ("SmartSpeedChargeAiLeaveHomeFeatureTrigger".equals(name)) {
                                        z13 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                        Log.d("ConfigUpdateUtil", "smartSpeedChargeAiLeaveHomeFeatureTrigger: " + name);
                                    }
                                    if ("SmartSpeedChargeAiLeaveHomePercent".equals(name)) {
                                        i38 = Integer.parseInt(newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE));
                                        try {
                                            Log.d("ConfigUpdateUtil", "smartSpeedChargeAiLeaveHomePercent: " + name);
                                        } catch (IOException e79) {
                                            iOException = e79;
                                            i18 = i38;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i552222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i552222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb6222222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i54222222222222222222222222222222222222222222222222 = i33;
                                            sb6222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb6222222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (NumberFormatException e80) {
                                            numberFormatException = e80;
                                            i18 = i38;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i562222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i562222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb62222222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i542222222222222222222222222222222222222222222222222 = i33;
                                            sb62222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb62222222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (XmlPullParserException e81) {
                                            xmlPullParserException = e81;
                                            i18 = i38;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            int i572222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i572222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb622222222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i5422222222222222222222222222222222222222222222222222 = i33;
                                            sb622222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb622222222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        }
                                    } else {
                                        i38 = i18;
                                    }
                                    try {
                                        if ("BatteryLogPush".equals(name)) {
                                            String attributeValue = newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE);
                                            try {
                                                StringBuilder sb17 = new StringBuilder();
                                                i39 = i38;
                                                try {
                                                    sb17.append("BatteryLogPush: ");
                                                    sb17.append(name);
                                                    Log.d("ConfigUpdateUtil", sb17.toString());
                                                    str5 = attributeValue;
                                                } catch (IOException e82) {
                                                    e = e82;
                                                    iOException = e;
                                                    str7 = str4;
                                                    i46 = i52;
                                                    i16 = i23;
                                                    i41 = i37;
                                                    f13 = f20;
                                                    z10 = z14;
                                                    i48 = i22;
                                                    f14 = f21;
                                                    i47 = i36;
                                                    i43 = i53;
                                                    i44 = i29;
                                                    i45 = i28;
                                                    str6 = attributeValue;
                                                    i18 = i39;
                                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                                    i33 = i46;
                                                    i32 = i47;
                                                    z16 = false;
                                                    i22 = i48;
                                                    z14 = z10;
                                                    i24 = i13;
                                                    str3 = str2;
                                                    i27 = i10;
                                                    i23 = i16;
                                                    i21 = i18;
                                                    i25 = i43;
                                                    str4 = str7;
                                                    f17 = f11;
                                                    f16 = f13;
                                                    i30 = i42;
                                                    str5 = str6;
                                                    i31 = i20;
                                                    f15 = f10;
                                                    i26 = i41;
                                                    i28 = i45;
                                                    z15 = z11;
                                                    i29 = i44;
                                                    StringBuilder sb6222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                                    int i54222222222222222222222222222222222222222222222222222 = i33;
                                                    sb6222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                                    sb6222222222222222222222222222222222222222222222222222.append(z16);
                                                    Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222.toString());
                                                    if (z16) {
                                                    }
                                                } catch (NumberFormatException e83) {
                                                    e = e83;
                                                    numberFormatException = e;
                                                    str7 = str4;
                                                    i46 = i52;
                                                    i16 = i23;
                                                    i41 = i37;
                                                    f13 = f20;
                                                    z10 = z14;
                                                    i48 = i22;
                                                    f14 = f21;
                                                    i47 = i36;
                                                    i43 = i53;
                                                    i44 = i29;
                                                    i45 = i28;
                                                    str6 = attributeValue;
                                                    i18 = i39;
                                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                                    i33 = i46;
                                                    i32 = i47;
                                                    z16 = false;
                                                    i22 = i48;
                                                    z14 = z10;
                                                    i24 = i13;
                                                    str3 = str2;
                                                    i27 = i10;
                                                    i23 = i16;
                                                    i21 = i18;
                                                    i25 = i43;
                                                    str4 = str7;
                                                    f17 = f11;
                                                    f16 = f13;
                                                    i30 = i42;
                                                    str5 = str6;
                                                    i31 = i20;
                                                    f15 = f10;
                                                    i26 = i41;
                                                    i28 = i45;
                                                    z15 = z11;
                                                    i29 = i44;
                                                    StringBuilder sb62222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                                    int i542222222222222222222222222222222222222222222222222222 = i33;
                                                    sb62222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                                    sb62222222222222222222222222222222222222222222222222222.append(z16);
                                                    Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222222.toString());
                                                    if (z16) {
                                                    }
                                                } catch (XmlPullParserException e84) {
                                                    e = e84;
                                                    xmlPullParserException = e;
                                                    str7 = str4;
                                                    i46 = i52;
                                                    i16 = i23;
                                                    i41 = i37;
                                                    f13 = f20;
                                                    z10 = z14;
                                                    i48 = i22;
                                                    f14 = f21;
                                                    i47 = i36;
                                                    i43 = i53;
                                                    i44 = i29;
                                                    i45 = i28;
                                                    str6 = attributeValue;
                                                    i18 = i39;
                                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                                    i33 = i46;
                                                    i32 = i47;
                                                    z16 = false;
                                                    i22 = i48;
                                                    z14 = z10;
                                                    i24 = i13;
                                                    str3 = str2;
                                                    i27 = i10;
                                                    i23 = i16;
                                                    i21 = i18;
                                                    i25 = i43;
                                                    str4 = str7;
                                                    f17 = f11;
                                                    f16 = f13;
                                                    i30 = i42;
                                                    str5 = str6;
                                                    i31 = i20;
                                                    f15 = f10;
                                                    i26 = i41;
                                                    i28 = i45;
                                                    z15 = z11;
                                                    i29 = i44;
                                                    StringBuilder sb622222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                                    int i5422222222222222222222222222222222222222222222222222222 = i33;
                                                    sb622222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                                    sb622222222222222222222222222222222222222222222222222222.append(z16);
                                                    Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222222.toString());
                                                    if (z16) {
                                                    }
                                                }
                                            } catch (IOException e85) {
                                                e = e85;
                                                i39 = i38;
                                            } catch (NumberFormatException e86) {
                                                e = e86;
                                                i39 = i38;
                                            } catch (XmlPullParserException e87) {
                                                e = e87;
                                                i39 = i38;
                                            }
                                        } else {
                                            i39 = i38;
                                        }
                                        try {
                                            if ("ChgRusConfig".equals(name)) {
                                                String attributeValue2 = newPullParser.getAttributeValue(null, ThermalBaseConfig.Item.ATTR_VALUE);
                                                try {
                                                    Log.d("ConfigUpdateUtil", "ChgRusConfig: " + name);
                                                    i34 = i10;
                                                    str4 = attributeValue2;
                                                } catch (IOException e88) {
                                                    iOException = e88;
                                                    i46 = i52;
                                                    i16 = i23;
                                                    i41 = i37;
                                                    f13 = f20;
                                                    z10 = z14;
                                                    i48 = i22;
                                                    i18 = i39;
                                                    i47 = i36;
                                                    i43 = i53;
                                                    i44 = i29;
                                                    i45 = i28;
                                                    str6 = str5;
                                                    str7 = attributeValue2;
                                                    f14 = f21;
                                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                                    i33 = i46;
                                                    i32 = i47;
                                                    z16 = false;
                                                    i22 = i48;
                                                    z14 = z10;
                                                    i24 = i13;
                                                    str3 = str2;
                                                    i27 = i10;
                                                    i23 = i16;
                                                    i21 = i18;
                                                    i25 = i43;
                                                    str4 = str7;
                                                    f17 = f11;
                                                    f16 = f13;
                                                    i30 = i42;
                                                    str5 = str6;
                                                    i31 = i20;
                                                    f15 = f10;
                                                    i26 = i41;
                                                    i28 = i45;
                                                    z15 = z11;
                                                    i29 = i44;
                                                    StringBuilder sb6222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                                    int i54222222222222222222222222222222222222222222222222222222 = i33;
                                                    sb6222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                                    sb6222222222222222222222222222222222222222222222222222222.append(z16);
                                                    Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222222.toString());
                                                    if (z16) {
                                                    }
                                                } catch (NumberFormatException e89) {
                                                    numberFormatException = e89;
                                                    i46 = i52;
                                                    i16 = i23;
                                                    i41 = i37;
                                                    f13 = f20;
                                                    z10 = z14;
                                                    i48 = i22;
                                                    i18 = i39;
                                                    i47 = i36;
                                                    i43 = i53;
                                                    i44 = i29;
                                                    i45 = i28;
                                                    str6 = str5;
                                                    str7 = attributeValue2;
                                                    f14 = f21;
                                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                                    i33 = i46;
                                                    i32 = i47;
                                                    z16 = false;
                                                    i22 = i48;
                                                    z14 = z10;
                                                    i24 = i13;
                                                    str3 = str2;
                                                    i27 = i10;
                                                    i23 = i16;
                                                    i21 = i18;
                                                    i25 = i43;
                                                    str4 = str7;
                                                    f17 = f11;
                                                    f16 = f13;
                                                    i30 = i42;
                                                    str5 = str6;
                                                    i31 = i20;
                                                    f15 = f10;
                                                    i26 = i41;
                                                    i28 = i45;
                                                    z15 = z11;
                                                    i29 = i44;
                                                    StringBuilder sb62222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                                    int i542222222222222222222222222222222222222222222222222222222 = i33;
                                                    sb62222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                                    sb62222222222222222222222222222222222222222222222222222222.append(z16);
                                                    Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222222222.toString());
                                                    if (z16) {
                                                    }
                                                } catch (XmlPullParserException e90) {
                                                    xmlPullParserException = e90;
                                                    i46 = i52;
                                                    i16 = i23;
                                                    i41 = i37;
                                                    f13 = f20;
                                                    z10 = z14;
                                                    i48 = i22;
                                                    i18 = i39;
                                                    i47 = i36;
                                                    i43 = i53;
                                                    i44 = i29;
                                                    i45 = i28;
                                                    str6 = str5;
                                                    str7 = attributeValue2;
                                                    f14 = f21;
                                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                                    i33 = i46;
                                                    i32 = i47;
                                                    z16 = false;
                                                    i22 = i48;
                                                    z14 = z10;
                                                    i24 = i13;
                                                    str3 = str2;
                                                    i27 = i10;
                                                    i23 = i16;
                                                    i21 = i18;
                                                    i25 = i43;
                                                    str4 = str7;
                                                    f17 = f11;
                                                    f16 = f13;
                                                    i30 = i42;
                                                    str5 = str6;
                                                    i31 = i20;
                                                    f15 = f10;
                                                    i26 = i41;
                                                    i28 = i45;
                                                    z15 = z11;
                                                    i29 = i44;
                                                    StringBuilder sb622222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                                    int i5422222222222222222222222222222222222222222222222222222222 = i33;
                                                    sb622222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                                    sb622222222222222222222222222222222222222222222222222222222.append(z16);
                                                    Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222222222.toString());
                                                    if (z16) {
                                                    }
                                                }
                                            } else {
                                                i34 = i10;
                                            }
                                            i41 = i37;
                                            f14 = f21;
                                            i18 = i39;
                                            z17 = true;
                                            i50 = i20;
                                            i51 = i36;
                                            i30 = i42;
                                            f15 = f10;
                                        } catch (IOException e91) {
                                            e = e91;
                                            iOException = e;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            i18 = i39;
                                            int i5522222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i5522222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb6222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i54222222222222222222222222222222222222222222222222222222222 = i33;
                                            sb6222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb6222222222222222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (NumberFormatException e92) {
                                            e = e92;
                                            numberFormatException = e;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            i18 = i39;
                                            int i5622222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i5622222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb62222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i542222222222222222222222222222222222222222222222222222222222 = i33;
                                            sb62222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb62222222222222222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        } catch (XmlPullParserException e93) {
                                            e = e93;
                                            xmlPullParserException = e;
                                            i16 = i23;
                                            i41 = i37;
                                            f13 = f20;
                                            z10 = z14;
                                            i48 = i22;
                                            f14 = f21;
                                            i18 = i39;
                                            int i5722222222 = i52;
                                            i47 = i36;
                                            i43 = i53;
                                            i44 = i29;
                                            i45 = i28;
                                            str6 = str5;
                                            str7 = str4;
                                            i46 = i5722222222;
                                            LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                            i33 = i46;
                                            i32 = i47;
                                            z16 = false;
                                            i22 = i48;
                                            z14 = z10;
                                            i24 = i13;
                                            str3 = str2;
                                            i27 = i10;
                                            i23 = i16;
                                            i21 = i18;
                                            i25 = i43;
                                            str4 = str7;
                                            f17 = f11;
                                            f16 = f13;
                                            i30 = i42;
                                            str5 = str6;
                                            i31 = i20;
                                            f15 = f10;
                                            i26 = i41;
                                            i28 = i45;
                                            z15 = z11;
                                            i29 = i44;
                                            StringBuilder sb622222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                            int i5422222222222222222222222222222222222222222222222222222222222 = i33;
                                            sb622222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                            sb622222222222222222222222222222222222222222222222222222222222.append(z16);
                                            Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222222222222.toString());
                                            if (z16) {
                                            }
                                        }
                                    } catch (IOException e94) {
                                        e = e94;
                                        i39 = i38;
                                    } catch (NumberFormatException e95) {
                                        e = e95;
                                        i39 = i38;
                                    } catch (XmlPullParserException e96) {
                                        e = e96;
                                        i39 = i38;
                                    }
                                } catch (IOException e97) {
                                    iOException = e97;
                                    i16 = i23;
                                    f13 = f20;
                                    z10 = z14;
                                    i48 = i22;
                                    f14 = f21;
                                    int i55222222222 = i52;
                                    i47 = i36;
                                    i43 = i53;
                                    i44 = i29;
                                    i45 = i28;
                                    str6 = str5;
                                    str7 = str4;
                                    i46 = i55222222222;
                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                    i33 = i46;
                                    i32 = i47;
                                    z16 = false;
                                    i22 = i48;
                                    z14 = z10;
                                    i24 = i13;
                                    str3 = str2;
                                    i27 = i10;
                                    i23 = i16;
                                    i21 = i18;
                                    i25 = i43;
                                    str4 = str7;
                                    f17 = f11;
                                    f16 = f13;
                                    i30 = i42;
                                    str5 = str6;
                                    i31 = i20;
                                    f15 = f10;
                                    i26 = i41;
                                    i28 = i45;
                                    z15 = z11;
                                    i29 = i44;
                                    StringBuilder sb6222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                    int i54222222222222222222222222222222222222222222222222222222222222 = i33;
                                    sb6222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                    sb6222222222222222222222222222222222222222222222222222222222222.append(z16);
                                    Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222222222222.toString());
                                    if (z16) {
                                    }
                                } catch (NumberFormatException e98) {
                                    numberFormatException = e98;
                                    i16 = i23;
                                    f13 = f20;
                                    z10 = z14;
                                    i48 = i22;
                                    f14 = f21;
                                    int i56222222222 = i52;
                                    i47 = i36;
                                    i43 = i53;
                                    i44 = i29;
                                    i45 = i28;
                                    str6 = str5;
                                    str7 = str4;
                                    i46 = i56222222222;
                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                    i33 = i46;
                                    i32 = i47;
                                    z16 = false;
                                    i22 = i48;
                                    z14 = z10;
                                    i24 = i13;
                                    str3 = str2;
                                    i27 = i10;
                                    i23 = i16;
                                    i21 = i18;
                                    i25 = i43;
                                    str4 = str7;
                                    f17 = f11;
                                    f16 = f13;
                                    i30 = i42;
                                    str5 = str6;
                                    i31 = i20;
                                    f15 = f10;
                                    i26 = i41;
                                    i28 = i45;
                                    z15 = z11;
                                    i29 = i44;
                                    StringBuilder sb62222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                    int i542222222222222222222222222222222222222222222222222222222222222 = i33;
                                    sb62222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                    sb62222222222222222222222222222222222222222222222222222222222222.append(z16);
                                    Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222222222222222.toString());
                                    if (z16) {
                                    }
                                } catch (XmlPullParserException e99) {
                                    xmlPullParserException = e99;
                                    i16 = i23;
                                    f13 = f20;
                                    z10 = z14;
                                    i48 = i22;
                                    f14 = f21;
                                    int i57222222222 = i52;
                                    i47 = i36;
                                    i43 = i53;
                                    i44 = i29;
                                    i45 = i28;
                                    str6 = str5;
                                    str7 = str4;
                                    i46 = i57222222222;
                                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                    i33 = i46;
                                    i32 = i47;
                                    z16 = false;
                                    i22 = i48;
                                    z14 = z10;
                                    i24 = i13;
                                    str3 = str2;
                                    i27 = i10;
                                    i23 = i16;
                                    i21 = i18;
                                    i25 = i43;
                                    str4 = str7;
                                    f17 = f11;
                                    f16 = f13;
                                    i30 = i42;
                                    str5 = str6;
                                    i31 = i20;
                                    f15 = f10;
                                    i26 = i41;
                                    i28 = i45;
                                    z15 = z11;
                                    i29 = i44;
                                    StringBuilder sb622222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                    int i5422222222222222222222222222222222222222222222222222222222222222 = i33;
                                    sb622222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                    sb622222222222222222222222222222222222222222222222222222222222222.append(z16);
                                    Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222222222222222.toString());
                                    if (z16) {
                                    }
                                }
                            } catch (IOException e100) {
                                iOException = e100;
                                i16 = i23;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                                f14 = f21;
                                i41 = i35;
                                int i552222222222 = i52;
                                i47 = i36;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i552222222222;
                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                                i33 = i46;
                                i32 = i47;
                                z16 = false;
                                i22 = i48;
                                z14 = z10;
                                i24 = i13;
                                str3 = str2;
                                i27 = i10;
                                i23 = i16;
                                i21 = i18;
                                i25 = i43;
                                str4 = str7;
                                f17 = f11;
                                f16 = f13;
                                i30 = i42;
                                str5 = str6;
                                i31 = i20;
                                f15 = f10;
                                i26 = i41;
                                i28 = i45;
                                z15 = z11;
                                i29 = i44;
                                StringBuilder sb6222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                int i54222222222222222222222222222222222222222222222222222222222222222 = i33;
                                sb6222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                sb6222222222222222222222222222222222222222222222222222222222222222.append(z16);
                                Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222222222222222.toString());
                                if (z16) {
                                }
                            } catch (NumberFormatException e101) {
                                numberFormatException = e101;
                                i16 = i23;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                                f14 = f21;
                                i41 = i35;
                                int i562222222222 = i52;
                                i47 = i36;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i562222222222;
                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                                i33 = i46;
                                i32 = i47;
                                z16 = false;
                                i22 = i48;
                                z14 = z10;
                                i24 = i13;
                                str3 = str2;
                                i27 = i10;
                                i23 = i16;
                                i21 = i18;
                                i25 = i43;
                                str4 = str7;
                                f17 = f11;
                                f16 = f13;
                                i30 = i42;
                                str5 = str6;
                                i31 = i20;
                                f15 = f10;
                                i26 = i41;
                                i28 = i45;
                                z15 = z11;
                                i29 = i44;
                                StringBuilder sb62222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                int i542222222222222222222222222222222222222222222222222222222222222222 = i33;
                                sb62222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                sb62222222222222222222222222222222222222222222222222222222222222222.append(z16);
                                Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222222222222222222.toString());
                                if (z16) {
                                }
                            } catch (XmlPullParserException e102) {
                                xmlPullParserException = e102;
                                i16 = i23;
                                f13 = f20;
                                z10 = z14;
                                i48 = i22;
                                f14 = f21;
                                i41 = i35;
                                int i572222222222 = i52;
                                i47 = i36;
                                i43 = i53;
                                i44 = i29;
                                i45 = i28;
                                str6 = str5;
                                str7 = str4;
                                i46 = i572222222222;
                                LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                                i33 = i46;
                                i32 = i47;
                                z16 = false;
                                i22 = i48;
                                z14 = z10;
                                i24 = i13;
                                str3 = str2;
                                i27 = i10;
                                i23 = i16;
                                i21 = i18;
                                i25 = i43;
                                str4 = str7;
                                f17 = f11;
                                f16 = f13;
                                i30 = i42;
                                str5 = str6;
                                i31 = i20;
                                f15 = f10;
                                i26 = i41;
                                i28 = i45;
                                z15 = z11;
                                i29 = i44;
                                StringBuilder sb622222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                                int i5422222222222222222222222222222222222222222222222222222222222222222 = i33;
                                sb622222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                                sb622222222222222222222222222222222222222222222222222222222222222222.append(z16);
                                Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222222222222222222.toString());
                                if (z16) {
                                }
                            }
                        } else {
                            i34 = i10;
                            z17 = true;
                        }
                        if (next == z17) {
                            break;
                        } else {
                            i49 = i34;
                        }
                    } catch (IOException e103) {
                        e = e103;
                        iOException = e;
                        f10 = f15;
                        i42 = i30;
                        i43 = i53;
                        i44 = i29;
                        i45 = i28;
                        str6 = str5;
                        str7 = str4;
                        i46 = i52;
                        i47 = i51;
                        i16 = i23;
                        i20 = i50;
                        f13 = f20;
                        z10 = z14;
                        i48 = i22;
                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + iOException);
                        i33 = i46;
                        i32 = i47;
                        z16 = false;
                        i22 = i48;
                        z14 = z10;
                        i24 = i13;
                        str3 = str2;
                        i27 = i10;
                        i23 = i16;
                        i21 = i18;
                        i25 = i43;
                        str4 = str7;
                        f17 = f11;
                        f16 = f13;
                        i30 = i42;
                        str5 = str6;
                        i31 = i20;
                        f15 = f10;
                        i26 = i41;
                        i28 = i45;
                        z15 = z11;
                        i29 = i44;
                        StringBuilder sb6222222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                        int i54222222222222222222222222222222222222222222222222222222222222222222 = i33;
                        sb6222222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                        sb6222222222222222222222222222222222222222222222222222222222222222222.append(z16);
                        Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222222222222222222.toString());
                        if (z16) {
                        }
                    } catch (NumberFormatException e104) {
                        e = e104;
                        numberFormatException = e;
                        f10 = f15;
                        i42 = i30;
                        i43 = i53;
                        i44 = i29;
                        i45 = i28;
                        str6 = str5;
                        str7 = str4;
                        i46 = i52;
                        i47 = i51;
                        i16 = i23;
                        i20 = i50;
                        f13 = f20;
                        z10 = z14;
                        i48 = i22;
                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + numberFormatException);
                        i33 = i46;
                        i32 = i47;
                        z16 = false;
                        i22 = i48;
                        z14 = z10;
                        i24 = i13;
                        str3 = str2;
                        i27 = i10;
                        i23 = i16;
                        i21 = i18;
                        i25 = i43;
                        str4 = str7;
                        f17 = f11;
                        f16 = f13;
                        i30 = i42;
                        str5 = str6;
                        i31 = i20;
                        f15 = f10;
                        i26 = i41;
                        i28 = i45;
                        z15 = z11;
                        i29 = i44;
                        StringBuilder sb62222222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                        int i542222222222222222222222222222222222222222222222222222222222222222222 = i33;
                        sb62222222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                        sb62222222222222222222222222222222222222222222222222222222222222222222.append(z16);
                        Log.d("ConfigUpdateUtil", sb62222222222222222222222222222222222222222222222222222222222222222222.toString());
                        if (z16) {
                        }
                    } catch (XmlPullParserException e105) {
                        e = e105;
                        xmlPullParserException = e;
                        f10 = f15;
                        i42 = i30;
                        i43 = i53;
                        i44 = i29;
                        i45 = i28;
                        str6 = str5;
                        str7 = str4;
                        i46 = i52;
                        i47 = i51;
                        i16 = i23;
                        i20 = i50;
                        f13 = f20;
                        z10 = z14;
                        i48 = i22;
                        LocalLog.d("ConfigUpdateUtil", "failed parsing " + xmlPullParserException);
                        i33 = i46;
                        i32 = i47;
                        z16 = false;
                        i22 = i48;
                        z14 = z10;
                        i24 = i13;
                        str3 = str2;
                        i27 = i10;
                        i23 = i16;
                        i21 = i18;
                        i25 = i43;
                        str4 = str7;
                        f17 = f11;
                        f16 = f13;
                        i30 = i42;
                        str5 = str6;
                        i31 = i20;
                        f15 = f10;
                        i26 = i41;
                        i28 = i45;
                        z15 = z11;
                        i29 = i44;
                        StringBuilder sb622222222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
                        int i5422222222222222222222222222222222222222222222222222222222222222222222 = i33;
                        sb622222222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
                        sb622222222222222222222222222222222222222222222222222222222222222222222.append(z16);
                        Log.d("ConfigUpdateUtil", sb622222222222222222222222222222222222222222222222222222222222222222222.toString());
                        if (z16) {
                        }
                    }
                } catch (IOException e106) {
                    e = e106;
                    i10 = i49;
                } catch (NumberFormatException e107) {
                    e = e107;
                    i10 = i49;
                } catch (XmlPullParserException e108) {
                    e = e108;
                    i10 = i49;
                }
            }
            z16 = z17;
            i21 = i18;
            i25 = i53;
            i33 = i52;
            i32 = i51;
            i31 = i50;
            f16 = f20;
            i24 = i13;
            str3 = str2;
            i27 = i34;
            i26 = i41;
            f17 = f11;
            z15 = z11;
        } catch (IOException e109) {
            z10 = true;
            z11 = true;
            z12 = true;
            z13 = true;
            i10 = 5;
            f10 = 0.5f;
            f11 = 0.6f;
            f12 = 0.6f;
            i11 = 30;
            i12 = 30;
            i13 = 0;
            i14 = 3;
            i15 = 3;
            i16 = 15;
            f13 = 0.4f;
            f14 = 0.9f;
            i17 = 180;
            str2 = null;
            iOException = e109;
            i18 = 5;
            i19 = 60;
            i20 = 120;
        } catch (NumberFormatException e110) {
            z10 = true;
            z11 = true;
            z12 = true;
            z13 = true;
            i10 = 5;
            f10 = 0.5f;
            f11 = 0.6f;
            f12 = 0.6f;
            i11 = 30;
            i12 = 30;
            i13 = 0;
            i14 = 3;
            i15 = 3;
            i16 = 15;
            f13 = 0.4f;
            f14 = 0.9f;
            i17 = 180;
            str2 = null;
            numberFormatException = e110;
            i18 = 5;
            i19 = 60;
            i20 = 120;
        } catch (XmlPullParserException e111) {
            z10 = true;
            z11 = true;
            z12 = true;
            z13 = true;
            i10 = 5;
            f10 = 0.5f;
            f11 = 0.6f;
            f12 = 0.6f;
            i11 = 30;
            i12 = 30;
            i13 = 0;
            i14 = 3;
            i15 = 3;
            i16 = 15;
            f13 = 0.4f;
            f14 = 0.9f;
            i17 = 180;
            str2 = null;
            xmlPullParserException = e111;
            i18 = 5;
            i19 = 60;
            i20 = 120;
        }
        StringBuilder sb6222222222222222222222222222222222222222222222222222222222222222222222 = new StringBuilder();
        int i54222222222222222222222222222222222222222222222222222222222222222222222 = i33;
        sb6222222222222222222222222222222222222222222222222222222222222222222222.append("bSuccess: ");
        sb6222222222222222222222222222222222222222222222222222222222222222222222.append(z16);
        Log.d("ConfigUpdateUtil", sb6222222222222222222222222222222222222222222222222222222222222222222222.toString());
        if (z16) {
            return;
        }
        ChargeUtil.B(z14);
        ChargeUtil.U(i22);
        ChargeUtil.H(z15, this.Y);
        ChargeUtil.M(i11);
        ChargeUtil.C(i17);
        ChargeUtil.D(i23);
        ChargeUtil.E(f17, f16);
        ChargeUtil.G(i14);
        ChargeUtil.K(f14, fArr, fArr2, f15);
        ChargeUtil.L(i30, i32, i31, i54222222222222222222222222222222222222222222222222222222222222222222222);
        ChargeUtil.I(i12);
        ChargeUtil.J(i15);
        ChargeUtil.F(f12);
        ChargeUtil.N(i24, this.Y);
        ChargeUtil.A(str3, this.Y);
        ChargeUtil.P(z12);
        ChargeUtil.Q(i25);
        ChargeUtil.O(i26, i27);
        ChargeUtil.T(i29);
        ChargeUtil.R(i19);
        ChargeUtil.S(i28);
        ChargeUtil.W(i21);
        ChargeUtil.V(z13);
        ChargeUtil.q(str5);
        ChargeUtil.s(str4);
    }

    private void I0(String str) {
        if (str == null) {
            return;
        }
        try {
            N0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            N0 = 5000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void I1(String str) {
        if (str == null) {
            return;
        }
        try {
            T0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            T0 = 10;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x064f  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void J(String str) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        long[] jArr;
        long[] jArr2;
        boolean[] zArr;
        long[] jArr3;
        String str2;
        ArrayList arrayList4;
        XmlPullParserException xmlPullParserException;
        NumberFormatException numberFormatException;
        NullPointerException nullPointerException;
        String str3;
        IOException iOException;
        IndexOutOfBoundsException indexOutOfBoundsException;
        boolean z10;
        int i10;
        long parseLong;
        int i11;
        long parseLong2;
        long parseLong3;
        long parseLong4;
        long parseLong5;
        long parseLong6;
        long parseLong7;
        long parseLong8;
        String str4;
        String attributeValue;
        String attributeValue2;
        String str5 = "failed parsing ";
        String str6 = "ConfigUpdateUtil";
        Log.d("ConfigUpdateUtil", "parseHighPowerXmlValue");
        if (str == null || str.isEmpty()) {
            return;
        }
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        long[] jArr4 = new long[9];
        long[] jArr5 = new long[9];
        ArrayList arrayList8 = new ArrayList();
        boolean[] zArr2 = new boolean[8];
        long[] jArr6 = new long[4];
        long[] jArr7 = new long[2];
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new StringReader(str));
            newPullParser.nextTag();
            while (true) {
                int next = newPullParser.next();
                Log.d(str6, "parser.next(): " + next);
                if (next == 2) {
                    String name = newPullParser.getName();
                    StringBuilder sb2 = new StringBuilder();
                    str3 = str5;
                    try {
                        sb2.append("parser.getName(): ");
                        sb2.append(name);
                        Log.d(str6, sb2.toString());
                        jArr2 = jArr7;
                        if ("pkgBlackArray".equals(name)) {
                            try {
                                String attributeValue3 = newPullParser.getAttributeValue(null, "name");
                                if (attributeValue3 != null) {
                                    arrayList5.add(attributeValue3);
                                }
                            } catch (IOException e10) {
                                iOException = e10;
                                arrayList = arrayList5;
                                arrayList2 = arrayList6;
                                arrayList3 = arrayList7;
                                jArr = jArr5;
                                zArr = zArr2;
                                jArr3 = jArr6;
                                str5 = str3;
                                str2 = str6;
                                arrayList4 = arrayList8;
                                LocalLog.d(str2, str5 + iOException);
                                z10 = false;
                                Log.d(str2, "bSuccess: " + z10);
                                if (z10) {
                                }
                            } catch (IndexOutOfBoundsException e11) {
                                indexOutOfBoundsException = e11;
                                arrayList = arrayList5;
                                arrayList2 = arrayList6;
                                arrayList3 = arrayList7;
                                jArr = jArr5;
                                zArr = zArr2;
                                jArr3 = jArr6;
                                str2 = str6;
                                arrayList4 = arrayList8;
                                LocalLog.d(str2, str3 + indexOutOfBoundsException);
                                z10 = false;
                                Log.d(str2, "bSuccess: " + z10);
                                if (z10) {
                                }
                            } catch (NullPointerException e12) {
                                nullPointerException = e12;
                                arrayList = arrayList5;
                                arrayList2 = arrayList6;
                                arrayList3 = arrayList7;
                                jArr = jArr5;
                                zArr = zArr2;
                                jArr3 = jArr6;
                                str5 = str3;
                                str2 = str6;
                                arrayList4 = arrayList8;
                                LocalLog.d(str2, str5 + nullPointerException);
                                z10 = false;
                                Log.d(str2, "bSuccess: " + z10);
                                if (z10) {
                                }
                            } catch (NumberFormatException e13) {
                                numberFormatException = e13;
                                arrayList = arrayList5;
                                arrayList2 = arrayList6;
                                arrayList3 = arrayList7;
                                jArr = jArr5;
                                zArr = zArr2;
                                jArr3 = jArr6;
                                str5 = str3;
                                str2 = str6;
                                arrayList4 = arrayList8;
                                LocalLog.d(str2, str5 + numberFormatException);
                                z10 = false;
                                Log.d(str2, "bSuccess: " + z10);
                                if (z10) {
                                }
                            } catch (XmlPullParserException e14) {
                                xmlPullParserException = e14;
                                arrayList = arrayList5;
                                arrayList2 = arrayList6;
                                arrayList3 = arrayList7;
                                jArr = jArr5;
                                zArr = zArr2;
                                jArr3 = jArr6;
                                str5 = str3;
                                str2 = str6;
                                arrayList4 = arrayList8;
                                LocalLog.d(str2, str5 + xmlPullParserException);
                                z10 = false;
                                Log.d(str2, "bSuccess: " + z10);
                                if (z10) {
                                }
                            }
                        }
                        try {
                            if ("pkgWhiteArray1".equals(name) && (attributeValue2 = newPullParser.getAttributeValue(null, "name")) != null) {
                                arrayList6.add(attributeValue2);
                            }
                            if ("pkgWhiteArray2".equals(name) && (attributeValue = newPullParser.getAttributeValue(null, "name")) != null) {
                                arrayList7.add(attributeValue);
                            }
                            arrayList3 = arrayList7;
                            arrayList2 = arrayList6;
                            arrayList = arrayList5;
                            jArr3 = jArr6;
                            zArr = zArr2;
                            ArrayList arrayList9 = arrayList8;
                            if ("normalThreshold".equals(name)) {
                                try {
                                    StringBuilder sb3 = new StringBuilder();
                                    jArr = jArr5;
                                    try {
                                        sb3.append("normalThreshold.equals(tag): ");
                                        sb3.append(next);
                                        Log.d(str6, sb3.toString());
                                        parseLong = Long.parseLong(newPullParser.getAttributeValue(null, ThermalPolicy.KEY_CPU));
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append("valueCpu: ");
                                        i11 = next;
                                        sb4.append(newPullParser.getAttributeValue(null, ThermalPolicy.KEY_CPU));
                                        Log.d(str6, sb4.toString());
                                        parseLong2 = Long.parseLong(newPullParser.getAttributeValue(null, "wakelock"));
                                        parseLong3 = Long.parseLong(newPullParser.getAttributeValue(null, "job1"));
                                        parseLong4 = Long.parseLong(newPullParser.getAttributeValue(null, "job2"));
                                        parseLong5 = Long.parseLong(newPullParser.getAttributeValue(null, "wifiscan"));
                                        parseLong6 = Long.parseLong(newPullParser.getAttributeValue(null, "camera"));
                                        parseLong7 = Long.parseLong(newPullParser.getAttributeValue(null, "flashlight"));
                                        parseLong8 = Long.parseLong(newPullParser.getAttributeValue(null, "gps"));
                                        str4 = str6;
                                    } catch (IOException e15) {
                                        e = e15;
                                        iOException = e;
                                        str2 = str6;
                                        str5 = str3;
                                        arrayList4 = arrayList9;
                                        LocalLog.d(str2, str5 + iOException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (IndexOutOfBoundsException e16) {
                                        e = e16;
                                        indexOutOfBoundsException = e;
                                        str2 = str6;
                                        arrayList4 = arrayList9;
                                        LocalLog.d(str2, str3 + indexOutOfBoundsException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (NullPointerException e17) {
                                        e = e17;
                                        nullPointerException = e;
                                        str2 = str6;
                                        str5 = str3;
                                        arrayList4 = arrayList9;
                                        LocalLog.d(str2, str5 + nullPointerException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (NumberFormatException e18) {
                                        e = e18;
                                        numberFormatException = e;
                                        str2 = str6;
                                        str5 = str3;
                                        arrayList4 = arrayList9;
                                        LocalLog.d(str2, str5 + numberFormatException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (XmlPullParserException e19) {
                                        e = e19;
                                        xmlPullParserException = e;
                                        str2 = str6;
                                        str5 = str3;
                                        arrayList4 = arrayList9;
                                        LocalLog.d(str2, str5 + xmlPullParserException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    }
                                } catch (IOException e20) {
                                    e = e20;
                                    jArr = jArr5;
                                } catch (IndexOutOfBoundsException e21) {
                                    e = e21;
                                    jArr = jArr5;
                                } catch (NullPointerException e22) {
                                    e = e22;
                                    jArr = jArr5;
                                } catch (NumberFormatException e23) {
                                    e = e23;
                                    jArr = jArr5;
                                } catch (XmlPullParserException e24) {
                                    e = e24;
                                    jArr = jArr5;
                                }
                                try {
                                    long parseLong9 = Long.parseLong(newPullParser.getAttributeValue(null, "alarm"));
                                    jArr4[0] = parseLong;
                                    jArr4[1] = parseLong2;
                                    jArr4[2] = parseLong3;
                                    jArr4[3] = parseLong4;
                                    jArr4[4] = parseLong5;
                                    jArr4[5] = parseLong6;
                                    jArr4[6] = parseLong7;
                                    jArr4[7] = parseLong8;
                                    jArr4[8] = parseLong9;
                                } catch (IOException e25) {
                                    iOException = e25;
                                    str5 = str3;
                                    arrayList4 = arrayList9;
                                    str2 = str4;
                                    LocalLog.d(str2, str5 + iOException);
                                    z10 = false;
                                    Log.d(str2, "bSuccess: " + z10);
                                    if (z10) {
                                    }
                                } catch (IndexOutOfBoundsException e26) {
                                    indexOutOfBoundsException = e26;
                                    arrayList4 = arrayList9;
                                    str2 = str4;
                                    LocalLog.d(str2, str3 + indexOutOfBoundsException);
                                    z10 = false;
                                    Log.d(str2, "bSuccess: " + z10);
                                    if (z10) {
                                    }
                                } catch (NullPointerException e27) {
                                    nullPointerException = e27;
                                    str5 = str3;
                                    arrayList4 = arrayList9;
                                    str2 = str4;
                                    LocalLog.d(str2, str5 + nullPointerException);
                                    z10 = false;
                                    Log.d(str2, "bSuccess: " + z10);
                                    if (z10) {
                                    }
                                } catch (NumberFormatException e28) {
                                    numberFormatException = e28;
                                    str5 = str3;
                                    arrayList4 = arrayList9;
                                    str2 = str4;
                                    LocalLog.d(str2, str5 + numberFormatException);
                                    z10 = false;
                                    Log.d(str2, "bSuccess: " + z10);
                                    if (z10) {
                                    }
                                } catch (XmlPullParserException e29) {
                                    xmlPullParserException = e29;
                                    str5 = str3;
                                    arrayList4 = arrayList9;
                                    str2 = str4;
                                    LocalLog.d(str2, str5 + xmlPullParserException);
                                    z10 = false;
                                    Log.d(str2, "bSuccess: " + z10);
                                    if (z10) {
                                    }
                                }
                            } else {
                                i11 = next;
                                str4 = str6;
                                jArr = jArr5;
                            }
                            try {
                                if ("privilegeThreshold".equals(name)) {
                                    long parseLong10 = Long.parseLong(newPullParser.getAttributeValue(null, ThermalPolicy.KEY_CPU));
                                    long parseLong11 = Long.parseLong(newPullParser.getAttributeValue(null, "wakelock"));
                                    long parseLong12 = Long.parseLong(newPullParser.getAttributeValue(null, "job1"));
                                    long parseLong13 = Long.parseLong(newPullParser.getAttributeValue(null, "job2"));
                                    long parseLong14 = Long.parseLong(newPullParser.getAttributeValue(null, "wifiscan"));
                                    long parseLong15 = Long.parseLong(newPullParser.getAttributeValue(null, "camera"));
                                    long parseLong16 = Long.parseLong(newPullParser.getAttributeValue(null, "flashlight"));
                                    long parseLong17 = Long.parseLong(newPullParser.getAttributeValue(null, "gps"));
                                    long parseLong18 = Long.parseLong(newPullParser.getAttributeValue(null, "alarm"));
                                    jArr[0] = parseLong10;
                                    jArr[1] = parseLong11;
                                    jArr[2] = parseLong12;
                                    jArr[3] = parseLong13;
                                    jArr[4] = parseLong14;
                                    jArr[5] = parseLong15;
                                    jArr[6] = parseLong16;
                                    jArr[7] = parseLong17;
                                    jArr[8] = parseLong18;
                                }
                                if ("pkgPrivilege".equals(name)) {
                                    String attributeValue4 = newPullParser.getAttributeValue(null, "name");
                                    str2 = str4;
                                    try {
                                        Log.d(str2, "pkgPrivilege: " + attributeValue4);
                                        if (attributeValue4 != null) {
                                            arrayList4 = arrayList9;
                                            try {
                                                arrayList4.add(attributeValue4);
                                            } catch (IOException e30) {
                                                e = e30;
                                                iOException = e;
                                                str5 = str3;
                                                LocalLog.d(str2, str5 + iOException);
                                                z10 = false;
                                                Log.d(str2, "bSuccess: " + z10);
                                                if (z10) {
                                                }
                                            } catch (IndexOutOfBoundsException e31) {
                                                e = e31;
                                                indexOutOfBoundsException = e;
                                                LocalLog.d(str2, str3 + indexOutOfBoundsException);
                                                z10 = false;
                                                Log.d(str2, "bSuccess: " + z10);
                                                if (z10) {
                                                }
                                            } catch (NullPointerException e32) {
                                                e = e32;
                                                nullPointerException = e;
                                                str5 = str3;
                                                LocalLog.d(str2, str5 + nullPointerException);
                                                z10 = false;
                                                Log.d(str2, "bSuccess: " + z10);
                                                if (z10) {
                                                }
                                            } catch (NumberFormatException e33) {
                                                e = e33;
                                                numberFormatException = e;
                                                str5 = str3;
                                                LocalLog.d(str2, str5 + numberFormatException);
                                                z10 = false;
                                                Log.d(str2, "bSuccess: " + z10);
                                                if (z10) {
                                                }
                                            } catch (XmlPullParserException e34) {
                                                e = e34;
                                                xmlPullParserException = e;
                                                str5 = str3;
                                                LocalLog.d(str2, str5 + xmlPullParserException);
                                                z10 = false;
                                                Log.d(str2, "bSuccess: " + z10);
                                                if (z10) {
                                                }
                                            }
                                        } else {
                                            arrayList4 = arrayList9;
                                        }
                                    } catch (IOException e35) {
                                        e = e35;
                                        arrayList4 = arrayList9;
                                        iOException = e;
                                        str5 = str3;
                                        LocalLog.d(str2, str5 + iOException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (IndexOutOfBoundsException e36) {
                                        e = e36;
                                        arrayList4 = arrayList9;
                                        indexOutOfBoundsException = e;
                                        LocalLog.d(str2, str3 + indexOutOfBoundsException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (NullPointerException e37) {
                                        e = e37;
                                        arrayList4 = arrayList9;
                                        nullPointerException = e;
                                        str5 = str3;
                                        LocalLog.d(str2, str5 + nullPointerException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (NumberFormatException e38) {
                                        e = e38;
                                        arrayList4 = arrayList9;
                                        numberFormatException = e;
                                        str5 = str3;
                                        LocalLog.d(str2, str5 + numberFormatException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    } catch (XmlPullParserException e39) {
                                        e = e39;
                                        arrayList4 = arrayList9;
                                        xmlPullParserException = e;
                                        str5 = str3;
                                        LocalLog.d(str2, str5 + xmlPullParserException);
                                        z10 = false;
                                        Log.d(str2, "bSuccess: " + z10);
                                        if (z10) {
                                        }
                                    }
                                } else {
                                    arrayList4 = arrayList9;
                                    str2 = str4;
                                }
                                if ("trigger".equals(name)) {
                                    boolean parseBoolean = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "total"));
                                    boolean parseBoolean2 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "screenon"));
                                    boolean parseBoolean3 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "screenoff"));
                                    boolean parseBoolean4 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "screenonscan"));
                                    boolean parseBoolean5 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "screenoffscan"));
                                    boolean parseBoolean6 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "detect"));
                                    boolean parseBoolean7 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "notificate"));
                                    boolean parseBoolean8 = Boolean.parseBoolean(newPullParser.getAttributeValue(null, "automaticoptimization"));
                                    zArr[0] = parseBoolean;
                                    zArr[1] = parseBoolean2;
                                    zArr[2] = parseBoolean3;
                                    zArr[3] = parseBoolean4;
                                    zArr[4] = parseBoolean5;
                                    zArr[5] = parseBoolean6;
                                    zArr[6] = parseBoolean7;
                                    zArr[7] = parseBoolean8;
                                    Log.d(str2, "trigger: " + name);
                                }
                                if ("alarmTime".equals(name)) {
                                    long parseLong19 = Long.parseLong(newPullParser.getAttributeValue(null, "screenonalarm"));
                                    Log.d(str2, "valueScreenOnScan: " + parseLong19);
                                    long parseLong20 = Long.parseLong(newPullParser.getAttributeValue(null, "screenoffalarm"));
                                    Log.d(str2, "valueScreenOffScan: " + parseLong20);
                                    long parseLong21 = Long.parseLong(newPullParser.getAttributeValue(null, "detectalarm"));
                                    Log.d(str2, "valueDetect: " + parseLong21);
                                    long parseLong22 = Long.parseLong(newPullParser.getAttributeValue(null, "notificatealarm"));
                                    Log.d(str2, "valueNotificate: " + parseLong22);
                                    jArr3[0] = parseLong19;
                                    jArr3[1] = parseLong20;
                                    jArr3[2] = parseLong21;
                                    jArr3[3] = parseLong22;
                                    Log.d(str2, "alarmTime: " + name);
                                }
                                if ("notificationCycle".equals(name)) {
                                    long parseLong23 = Long.parseLong(newPullParser.getAttributeValue(null, "bgon"));
                                    Log.d(str2, "bgon: " + parseLong23);
                                    long parseLong24 = Long.parseLong(newPullParser.getAttributeValue(null, "bgoff"));
                                    Log.d(str2, "bgoff: " + parseLong24);
                                    jArr2[0] = parseLong23;
                                    jArr2[1] = parseLong24;
                                }
                                i10 = i11;
                            } catch (IOException e40) {
                                e = e40;
                                arrayList4 = arrayList9;
                                str2 = str4;
                            } catch (IndexOutOfBoundsException e41) {
                                e = e41;
                                arrayList4 = arrayList9;
                                str2 = str4;
                            } catch (NullPointerException e42) {
                                e = e42;
                                arrayList4 = arrayList9;
                                str2 = str4;
                            } catch (NumberFormatException e43) {
                                e = e43;
                                arrayList4 = arrayList9;
                                str2 = str4;
                            } catch (XmlPullParserException e44) {
                                e = e44;
                                arrayList4 = arrayList9;
                                str2 = str4;
                            }
                        } catch (IOException e45) {
                            e = e45;
                            arrayList = arrayList5;
                            arrayList2 = arrayList6;
                            arrayList3 = arrayList7;
                            jArr = jArr5;
                            zArr = zArr2;
                            jArr3 = jArr6;
                            str2 = str6;
                            arrayList4 = arrayList8;
                            iOException = e;
                            str5 = str3;
                            LocalLog.d(str2, str5 + iOException);
                            z10 = false;
                            Log.d(str2, "bSuccess: " + z10);
                            if (z10) {
                            }
                        } catch (IndexOutOfBoundsException e46) {
                            e = e46;
                            arrayList = arrayList5;
                            arrayList2 = arrayList6;
                            arrayList3 = arrayList7;
                            jArr = jArr5;
                            zArr = zArr2;
                            jArr3 = jArr6;
                            str2 = str6;
                            arrayList4 = arrayList8;
                            indexOutOfBoundsException = e;
                            LocalLog.d(str2, str3 + indexOutOfBoundsException);
                            z10 = false;
                            Log.d(str2, "bSuccess: " + z10);
                            if (z10) {
                            }
                        } catch (NullPointerException e47) {
                            e = e47;
                            arrayList = arrayList5;
                            arrayList2 = arrayList6;
                            arrayList3 = arrayList7;
                            jArr = jArr5;
                            zArr = zArr2;
                            jArr3 = jArr6;
                            str2 = str6;
                            arrayList4 = arrayList8;
                            nullPointerException = e;
                            str5 = str3;
                            LocalLog.d(str2, str5 + nullPointerException);
                            z10 = false;
                            Log.d(str2, "bSuccess: " + z10);
                            if (z10) {
                            }
                        } catch (NumberFormatException e48) {
                            e = e48;
                            arrayList = arrayList5;
                            arrayList2 = arrayList6;
                            arrayList3 = arrayList7;
                            jArr = jArr5;
                            zArr = zArr2;
                            jArr3 = jArr6;
                            str2 = str6;
                            arrayList4 = arrayList8;
                            numberFormatException = e;
                            str5 = str3;
                            LocalLog.d(str2, str5 + numberFormatException);
                            z10 = false;
                            Log.d(str2, "bSuccess: " + z10);
                            if (z10) {
                            }
                        } catch (XmlPullParserException e49) {
                            e = e49;
                            arrayList = arrayList5;
                            arrayList2 = arrayList6;
                            arrayList3 = arrayList7;
                            jArr = jArr5;
                            zArr = zArr2;
                            jArr3 = jArr6;
                            str2 = str6;
                            arrayList4 = arrayList8;
                            xmlPullParserException = e;
                            str5 = str3;
                            LocalLog.d(str2, str5 + xmlPullParserException);
                            z10 = false;
                            Log.d(str2, "bSuccess: " + z10);
                            if (z10) {
                            }
                        }
                    } catch (IOException e50) {
                        e = e50;
                        arrayList = arrayList5;
                        arrayList2 = arrayList6;
                        arrayList3 = arrayList7;
                        jArr = jArr5;
                        jArr2 = jArr7;
                    } catch (IndexOutOfBoundsException e51) {
                        e = e51;
                        arrayList = arrayList5;
                        arrayList2 = arrayList6;
                        arrayList3 = arrayList7;
                        jArr = jArr5;
                        jArr2 = jArr7;
                        zArr = zArr2;
                        jArr3 = jArr6;
                        str2 = str6;
                        arrayList4 = arrayList8;
                        indexOutOfBoundsException = e;
                        LocalLog.d(str2, str3 + indexOutOfBoundsException);
                        z10 = false;
                        Log.d(str2, "bSuccess: " + z10);
                        if (z10) {
                        }
                    } catch (NullPointerException e52) {
                        e = e52;
                        arrayList = arrayList5;
                        arrayList2 = arrayList6;
                        arrayList3 = arrayList7;
                        jArr = jArr5;
                        jArr2 = jArr7;
                    } catch (NumberFormatException e53) {
                        e = e53;
                        arrayList = arrayList5;
                        arrayList2 = arrayList6;
                        arrayList3 = arrayList7;
                        jArr = jArr5;
                        jArr2 = jArr7;
                    } catch (XmlPullParserException e54) {
                        e = e54;
                        arrayList = arrayList5;
                        arrayList2 = arrayList6;
                        arrayList3 = arrayList7;
                        jArr = jArr5;
                        jArr2 = jArr7;
                    }
                } else {
                    str3 = str5;
                    arrayList = arrayList5;
                    arrayList2 = arrayList6;
                    arrayList3 = arrayList7;
                    jArr = jArr5;
                    jArr2 = jArr7;
                    zArr = zArr2;
                    jArr3 = jArr6;
                    str2 = str6;
                    arrayList4 = arrayList8;
                    i10 = next;
                }
                z10 = true;
                if (i10 == 1) {
                    break;
                }
                arrayList8 = arrayList4;
                str6 = str2;
                str5 = str3;
                jArr7 = jArr2;
                arrayList7 = arrayList3;
                arrayList6 = arrayList2;
                arrayList5 = arrayList;
                jArr6 = jArr3;
                zArr2 = zArr;
                jArr5 = jArr;
            }
        } catch (IOException e55) {
            arrayList = arrayList5;
            arrayList2 = arrayList6;
            arrayList3 = arrayList7;
            jArr = jArr5;
            jArr2 = jArr7;
            zArr = zArr2;
            jArr3 = jArr6;
            str2 = str6;
            arrayList4 = arrayList8;
            iOException = e55;
        } catch (IndexOutOfBoundsException e56) {
            e = e56;
            str3 = str5;
        } catch (NullPointerException e57) {
            arrayList = arrayList5;
            arrayList2 = arrayList6;
            arrayList3 = arrayList7;
            jArr = jArr5;
            jArr2 = jArr7;
            zArr = zArr2;
            jArr3 = jArr6;
            str2 = str6;
            arrayList4 = arrayList8;
            nullPointerException = e57;
        } catch (NumberFormatException e58) {
            arrayList = arrayList5;
            arrayList2 = arrayList6;
            arrayList3 = arrayList7;
            jArr = jArr5;
            jArr2 = jArr7;
            zArr = zArr2;
            jArr3 = jArr6;
            str2 = str6;
            arrayList4 = arrayList8;
            numberFormatException = e58;
        } catch (XmlPullParserException e59) {
            arrayList = arrayList5;
            arrayList2 = arrayList6;
            arrayList3 = arrayList7;
            jArr = jArr5;
            jArr2 = jArr7;
            zArr = zArr2;
            jArr3 = jArr6;
            str2 = str6;
            arrayList4 = arrayList8;
            xmlPullParserException = e59;
        }
        Log.d(str2, "bSuccess: " + z10);
        if (z10) {
            return;
        }
        SimplePowerMonitorUtils.B(arrayList, this.Y);
        SimplePowerMonitorUtils.I(arrayList2);
        SimplePowerMonitorUtils.J(arrayList3, this.Y);
        SimplePowerMonitorUtils.C(jArr4);
        SimplePowerMonitorUtils.F(jArr);
        SimplePowerMonitorUtils.E(arrayList4);
        SimplePowerMonitorUtils.G(zArr);
        SimplePowerMonitorUtils.A(jArr3);
        SimplePowerMonitorUtils.D(jArr2);
    }

    private void J0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10759t0 = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void J1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10768y = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10768y = 300;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0701 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0702 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean K(String str) {
        boolean z10;
        int next;
        if (str == null || str.isEmpty()) {
            return false;
        }
        this.Q = false;
        this.f10723b0.clear();
        try {
            try {
                try {
                    XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                    newPullParser.setInput(new StringReader(str));
                    newPullParser.nextTag();
                    do {
                        next = newPullParser.next();
                        if (next == 2) {
                            String name = newPullParser.getName();
                            if ("version".equals(name)) {
                                String nextText = newPullParser.nextText();
                                if (nextText != null) {
                                    A0(nextText);
                                }
                            } else if ("NotifyInterval".equals(name)) {
                                String nextText2 = newPullParser.nextText();
                                if (nextText2 != null) {
                                    T0(nextText2);
                                }
                            } else if ("UploadInterval".equals(name)) {
                                String nextText3 = newPullParser.nextText();
                                if (nextText3 != null) {
                                    W1(nextText3);
                                }
                            } else if ("AbnormalStartCount".equals(name)) {
                                String nextText4 = newPullParser.nextText();
                                if (nextText4 != null) {
                                    g1(nextText4);
                                }
                            } else if ("CollectStartCount".equals(name)) {
                                String nextText5 = newPullParser.nextText();
                                if (nextText5 != null) {
                                    i1(nextText5);
                                }
                            } else if ("CheckStartTimeInterval".equals(name)) {
                                String nextText6 = newPullParser.nextText();
                                if (nextText6 != null) {
                                    h1(nextText6);
                                }
                            } else if ("ThresholdWorstIntervalPerWakeup".equals(name)) {
                                String nextText7 = newPullParser.nextText();
                                if (nextText7 != null) {
                                    U(nextText7);
                                }
                            } else if ("ThresholdIntervalPerWakeup".equals(name)) {
                                String nextText8 = newPullParser.nextText();
                                if (nextText8 != null) {
                                    S(nextText8);
                                }
                            } else if ("ThresholdWarningIntervalPerWakeup".equals(name)) {
                                String nextText9 = newPullParser.nextText();
                                if (nextText9 != null) {
                                    T(nextText9);
                                }
                            } else if ("ThresholdSeriousIntervalPerAlarm".equals(name)) {
                                String nextText10 = newPullParser.nextText();
                                if (nextText10 != null) {
                                    R(nextText10);
                                }
                            } else if ("ThresholdWakeLockTimeout".equals(name)) {
                                String nextText11 = newPullParser.nextText();
                                if (nextText11 != null) {
                                    X1(nextText11);
                                }
                            } else if ("CpuMonitorSwitch".equals(name)) {
                                String nextText12 = newPullParser.nextText();
                                if (nextText12 != null) {
                                    h0(nextText12);
                                }
                            } else if ("CpuMonitorKillSwitch".equals(name)) {
                                String nextText13 = newPullParser.nextText();
                                if (nextText13 != null) {
                                    f0(nextText13);
                                }
                            } else if ("CpuCheckInterval".equals(name)) {
                                String nextText14 = newPullParser.nextText();
                                if (nextText14 != null) {
                                    d0(nextText14);
                                }
                            } else if ("CpuPercentThresh".equals(name)) {
                                String nextText15 = newPullParser.nextText();
                                if (nextText15 != null) {
                                    i0(nextText15);
                                }
                            } else if ("CpuPerformPercentThresh".equals(name)) {
                                String nextText16 = newPullParser.nextText();
                                if (nextText16 != null) {
                                    j0(nextText16);
                                }
                            } else if ("CpuMonitorPercentThresh".equals(name)) {
                                String nextText17 = newPullParser.nextText();
                                if (nextText17 != null) {
                                    g0(nextText17);
                                }
                            } else if ("TrafficFilterThresh".equals(name)) {
                                String nextText18 = newPullParser.nextText();
                                if (nextText18 != null) {
                                    V1(nextText18);
                                }
                            } else if ("LowMemClearSwitch".equals(name)) {
                                String nextText19 = newPullParser.nextText();
                                if (nextText19 != null) {
                                    O0(nextText19);
                                }
                            } else if ("LowMemClearSaveRecent".equals(name)) {
                                String nextText20 = newPullParser.nextText();
                                if (nextText20 != null) {
                                    N0(nextText20);
                                }
                            } else if ("LowMemClearThresh".equals(name)) {
                                String nextText21 = newPullParser.nextText();
                                if (nextText21 != null) {
                                    M0(nextText21);
                                }
                            } else if ("LowMemClearUploadSwitch".equals(name)) {
                                String nextText22 = newPullParser.nextText();
                                if (nextText22 != null) {
                                    P0(nextText22);
                                }
                            } else if ("AllowUploadGuardelfData".equals(name)) {
                                V(newPullParser.nextText());
                            } else if ("ThreshBattIdleLowLevel".equals(name)) {
                                K1(newPullParser.nextText());
                            } else if ("ThreshBattIdleNormalLevel".equals(name)) {
                                L1(newPullParser.nextText());
                            } else if ("ThreshBattIdleDelay".equals(name)) {
                                J1(newPullParser.nextText());
                            } else if ("ThreshJobMinInterval".equals(name)) {
                                P1(newPullParser.nextText());
                            } else if ("TotalCpuMonitorSwitch".equals(name)) {
                                U1(newPullParser.nextText());
                            } else if ("AppCpuKillSwitch".equals(name)) {
                                X(newPullParser.nextText());
                            } else if ("AppCpuKillTopSwitch".equals(name)) {
                                Y(newPullParser.nextText());
                            } else if ("ThreshTotalCpuSlight".equals(name)) {
                                S1(newPullParser.nextText());
                            } else if ("ThreshTotalCpuMiddle".equals(name)) {
                                R1(newPullParser.nextText());
                            } else if ("ThreshTotalCpuHeavy".equals(name)) {
                                Q1(newPullParser.nextText());
                            } else if ("ThreshCountContinuousSlight".equals(name)) {
                                O1(newPullParser.nextText());
                            } else if ("ThreshCountContinuousMiddle".equals(name)) {
                                N1(newPullParser.nextText());
                            } else if ("ThreshCountContinuousHeavy".equals(name)) {
                                M1(newPullParser.nextText());
                            } else if ("IntervalTotalCpuSample".equals(name)) {
                                I0(newPullParser.nextText());
                            } else if ("IntervalAppCpuKill".equals(name)) {
                                F0(newPullParser.nextText());
                            } else if ("ForeAppStableTime".equals(name)) {
                                T1(newPullParser.nextText());
                            } else if ("IntervalAppCpuSample".equals(name)) {
                                G0(newPullParser.nextText());
                            } else if ("IntervalLongAppCpuSample".equals(name)) {
                                H0(newPullParser.nextText());
                            } else if ("ThreshAppCpuKill".equals(name)) {
                                G1(newPullParser.nextText());
                            } else if ("ThreshAppCpuKillFrame".equals(name)) {
                                I1(newPullParser.nextText());
                            } else if ("ThreshAppCpuKillExtremely".equals(name)) {
                                H1(newPullParser.nextText());
                            } else if ("FilterItemsCpuKill".equals(name)) {
                                w0(newPullParser.nextText());
                            } else if ("FilterItemsCpuKillFrame".equals(name)) {
                                x0(newPullParser.nextText());
                            } else if ("LogLimitEnabled".equals(name)) {
                                String nextText23 = newPullParser.nextText();
                                if (nextText23 != null) {
                                    L0(nextText23);
                                }
                            } else if ("StorageMonitorChatDisable".equals(name)) {
                                n1(newPullParser.nextText());
                            } else if ("DeepSleepSwitch".equals(name)) {
                                q0(newPullParser.nextText());
                            } else if ("ChargeProtectionSwitch".equals(name)) {
                                c0(newPullParser.nextText());
                            } else if ("RecoveryChargingDuration".equals(name)) {
                                c1(newPullParser.nextText());
                            } else if ("DeepSleepNetWorkSwitch".equals(name)) {
                                o0(newPullParser.nextText());
                            } else if ("DeepSleepNetOffNotify".equals(name)) {
                                n0(newPullParser.nextText());
                            } else if ("NetOffNotifyInterval".equals(name)) {
                                Q0(newPullParser.nextText());
                            } else if ("DeepSleepNeedNetonApp".equals(name)) {
                                S0(newPullParser.nextText());
                            } else if ("DeepSleepUseNetworkDisableWhiteList".equals(name)) {
                                R0(newPullParser.nextText());
                            } else if ("DeepSleepAISleepPercent".equals(name)) {
                                DeepSleepUtils.getInstance(this.Y).setAISleepPercent(Integer.parseInt(newPullParser.nextText()));
                            } else if ("DeepSleepAIWakePercent".equals(name)) {
                                DeepSleepUtils.getInstance(this.Y).setAIWakePercent(Integer.parseInt(newPullParser.nextText()));
                            } else if ("DeepSleepResetAirPlane".equals(name)) {
                                p0(newPullParser.nextText());
                            } else if ("DeepSleepIsUseNetworkDisableWhiteList".equals(name)) {
                                m0(newPullParser.nextText());
                            } else if ("AABEnabled".equals(name)) {
                                P(newPullParser.nextText());
                            } else if ("AABAiEnalbed".equals(name)) {
                                O(newPullParser.nextText());
                            } else if ("PowerAlertEnalbed".equals(name)) {
                                V0(newPullParser.nextText());
                            } else if ("PowerDetailsPkg".equals(name)) {
                                W0(newPullParser.nextText());
                            } else if (!"SmartDoze".equals(name)) {
                                if ("SmartDozeEnabled".equals(name)) {
                                    e1(newPullParser.nextText());
                                } else if ("StopDisableNetworkThreshold".equals(name)) {
                                    l1(newPullParser.nextText());
                                } else if ("DeepSleepPredictFeedbackSwitch".equals(name)) {
                                    Z0(newPullParser.nextText());
                                } else if ("StopDisableNetworkThresholdNoAI".equals(name)) {
                                    m1(newPullParser.nextText());
                                } else if ("DisableNetworkAgainThreshold".equals(name)) {
                                    k1(newPullParser.nextText());
                                } else if ("ReverseHighTempThreshold".equals(name)) {
                                    d1(newPullParser.nextText());
                                } else if ("ProjectSupportOplusStore".equals(name)) {
                                    b1(newPullParser.nextText());
                                } else if ("thermalSwitch".equals(name)) {
                                    E1(newPullParser.nextText());
                                } else if ("thermalPeriodTimeMillis".equals(name)) {
                                    y1(newPullParser.nextText());
                                } else if ("thermalPredictThreshold".equals(name)) {
                                    z1(newPullParser.nextText());
                                } else if ("thermalPredictWeight".equals(name)) {
                                    A1(newPullParser.nextText());
                                } else if ("thermalSamplingThreshold".equals(name)) {
                                    C1(newPullParser.nextText());
                                } else if ("thermalSamplingInterval".equals(name)) {
                                    B1(newPullParser.nextText());
                                } else if ("thermalSamplingWindowLength".equals(name)) {
                                    D1(newPullParser.nextText());
                                } else if ("thermalLightThreshold".equals(name)) {
                                    w1(newPullParser.nextText());
                                } else if ("thermalModerateThreshold".equals(name)) {
                                    x1(newPullParser.nextText());
                                } else if ("thermalCriticalThreshold".equals(name)) {
                                    t1(newPullParser.nextText());
                                } else if ("thermalFrontTempWeight".equals(name)) {
                                    v1(newPullParser.nextText());
                                } else if ("thermalFrameTempWeight".equals(name)) {
                                    u1(newPullParser.nextText());
                                } else if ("thermalBackTempWeight".equals(name)) {
                                    s1(newPullParser.nextText());
                                } else if ("job_temperature_protect_timeout".equals(name)) {
                                    r1(newPullParser.nextText());
                                } else if ("job_scene_protect_switch".equals(name)) {
                                    J0(newPullParser.nextText());
                                } else if ("job_scene_protect_timeout".equals(name)) {
                                    K0(newPullParser.nextText());
                                } else if ("GuardElfPolicyKill".equals(name)) {
                                    r0(newPullParser.nextText());
                                } else if ("CurveAlarmWakeup".equals(name)) {
                                    l0(newPullParser.nextText());
                                } else if ("PowerSaveFiveG".equals(name)) {
                                    Y0(newPullParser.nextText());
                                } else if ("oplus_guard_elf_switch".equals(name)) {
                                    B0(newPullParser.nextText());
                                } else if ("disable_screen_awake_pkg".equals(name)) {
                                    u0(newPullParser.nextText());
                                }
                            }
                        }
                    } while (next != 1);
                    z10 = true;
                } catch (IOException e10) {
                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + e10);
                    z10 = false;
                    if (!z10) {
                    }
                } catch (NullPointerException e11) {
                    LocalLog.d("ConfigUpdateUtil", "failed parsing " + e11);
                    z10 = false;
                    if (!z10) {
                    }
                }
            } catch (IndexOutOfBoundsException e12) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e12);
                z10 = false;
                if (!z10) {
                }
            } catch (NumberFormatException e13) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e13);
                z10 = false;
                if (!z10) {
                }
            }
        } catch (XmlPullParserException e14) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing " + e14);
            z10 = false;
            if (!z10) {
            }
        }
        return !z10;
    }

    private void K0(String str) {
        if (str == null) {
            return;
        }
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong > 0) {
                this.f10761u0 = parseLong;
            }
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void K1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10764w = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10764w = 30;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private List<String> L(String str) {
        int next;
        String attributeValue;
        if (str != null && !str.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            boolean z10 = false;
            try {
                XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                newPullParser.setInput(new StringReader(str));
                newPullParser.nextTag();
                do {
                    next = newPullParser.next();
                    if (next == 2 && "p".equals(newPullParser.getName()) && (attributeValue = newPullParser.getAttributeValue(null, "att")) != null) {
                        arrayList.add(attributeValue);
                    }
                } while (next != 1);
                z10 = true;
            } catch (IOException e10) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e10);
            } catch (IndexOutOfBoundsException e11) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e11);
            } catch (NullPointerException e12) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e12);
            } catch (NumberFormatException e13) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e13);
            } catch (XmlPullParserException e14) {
                LocalLog.d("ConfigUpdateUtil", "failed parsing " + e14);
            }
            if (z10) {
                return arrayList;
            }
        }
        return null;
    }

    private void L0(String str) {
        if (str == null) {
            return;
        }
        try {
            SystemProperties.set("sys.loglimit.enabled", str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void L1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10766x = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10766x = 70;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void M(Context context) {
        StringBuilder sb2;
        OutputStream e10 = LocalFileUtil.e("battery" + File.separator + "guardelf_config.xml", context);
        try {
            if (e10 == null) {
                LocalLog.d("ConfigUpdateUtil", "Fail to saveParametersToConfigFile");
                return;
            }
            try {
                Y1(e10);
                try {
                    e10.close();
                } catch (IOException e11) {
                    e = e11;
                    sb2 = new StringBuilder();
                    sb2.append("Failed to close state FileInputStream ");
                    sb2.append(e);
                    LocalLog.d("ConfigUpdateUtil", sb2.toString());
                }
            } catch (Exception e12) {
                LocalLog.d("ConfigUpdateUtil", "FileNotFoundException: " + e12);
                try {
                    e10.close();
                } catch (IOException e13) {
                    e = e13;
                    sb2 = new StringBuilder();
                    sb2.append("Failed to close state FileInputStream ");
                    sb2.append(e);
                    LocalLog.d("ConfigUpdateUtil", sb2.toString());
                }
            }
        } catch (Throwable th) {
            try {
                e10.close();
            } catch (IOException e14) {
                LocalLog.d("ConfigUpdateUtil", "Failed to close state FileInputStream " + e14);
            }
            throw th;
        }
    }

    private void M0(String str) {
        try {
            this.C = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.C = 350;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void M1(String str) {
        if (str == null) {
            return;
        }
        try {
            M0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            M0 = 2;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void N(int i10) {
        f.C2(this.Y, i10);
        f.h2(this.Y, i10);
        f.E3("five_g_switch", String.valueOf(i10), this.Y);
    }

    private void N0(String str) {
        try {
            this.B = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.B = 3;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void N1(String str) {
        if (str == null) {
            return;
        }
        try {
            L0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            L0 = 3;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void O(String str) {
        if (str == null) {
            return;
        }
        try {
            this.U = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.U = true;
            LocalLog.d("ConfigUpdateUtil", "updateAABAISwitch: failed parsing value " + e10);
        }
        RegionPluginUtil o10 = PluginSupporter.m().o();
        if (o10 != null) {
            o10.f(this.U);
        }
    }

    private void O0(String str) {
        try {
            this.A = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.A = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void O1(String str) {
        if (str == null) {
            return;
        }
        try {
            K0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            K0 = 4;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void P(String str) {
        if (str == null) {
            return;
        }
        try {
            this.T = Boolean.parseBoolean(str);
            Settings.Global.putInt(this.Y.getContentResolver(), "adaptive_battery_management_enabled", this.T ? 1 : 0);
        } catch (NumberFormatException e10) {
            this.T = true;
            LocalLog.d("ConfigUpdateUtil", "updateAABSwitch: failed parsing value " + e10);
        }
    }

    private void P0(String str) {
        try {
            this.D = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.D = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void P1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10770z = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10770z = 3600L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void Q0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10724c = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10724c = 7200;
            LocalLog.d("ConfigUpdateUtil", "updateNetOffNotifyInterval: failed parsing value " + e10);
        }
    }

    private void Q1(String str) {
        if (str == null) {
            return;
        }
        try {
            J0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            J0 = 60;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void R(String str) {
        try {
            this.f10742l = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10742l = 180L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void R0(String str) {
        if (str == null) {
            return;
        }
        try {
            if (!this.f10723b0.contains(str)) {
                this.f10723b0.add(str);
            }
            this.Q = true;
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "updateNetworkDisableWhiteList: failed parsing value " + e10);
        }
    }

    private void R1(String str) {
        if (str == null) {
            return;
        }
        try {
            I0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            I0 = 40;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void S(String str) {
        try {
            this.f10738j = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10738j = 300L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void S0(String str) {
        if (str == null) {
            return;
        }
        try {
            if (this.f10721a0.contains(str)) {
                return;
            }
            this.f10721a0.add(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "updateNotDisNetworkAppList: failed parsing value " + e10);
        }
    }

    private void S1(String str) {
        if (str == null) {
            return;
        }
        try {
            H0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            H0 = 20;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void T(String str) {
        try {
            this.f10740k = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10740k = 360L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void T0(String str) {
        try {
            this.f10746n = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10746n = 900L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void T1(String str) {
        if (str == null) {
            return;
        }
        try {
            P0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            P0 = 10000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void U(String str) {
        try {
            this.f10736i = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10736i = 60L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void U1(String str) {
        if (str == null) {
            return;
        }
        try {
            E0 = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            E0 = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void V(String str) {
        if (str == null) {
            return;
        }
        try {
            this.E = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.E = true;
            LocalLog.d("ConfigUpdateUtil", "updateAllowGuardelfData: failed parsing value " + e10);
        }
    }

    private void V0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.V = Boolean.parseBoolean(str);
            LocalLog.a("ConfigUpdateUtil", "updatePowerAlertSwitch: PowerAlertSwitch = " + this.V);
        } catch (NumberFormatException e10) {
            this.V = false;
            LocalLog.d("ConfigUpdateUtil", "updatePowerAlertSwitch: failed parsing value " + e10);
        }
    }

    private void V1(String str) {
        try {
            this.f10762v = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10762v = 10;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void W0(String str) {
        synchronized (this.Z) {
            if (!this.Z.contains(str)) {
                this.Z.add(str);
                LocalLog.a("ConfigUpdateUtil", "updatePowerDetailsPkg: pkg=" + str);
            }
        }
    }

    private void W1(String str) {
        try {
            this.f10748o = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10748o = 3600L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void X(String str) {
        if (str == null) {
            return;
        }
        try {
            F0 = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            F0 = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void X1(String str) {
        try {
            this.f10744m = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10744m = 300L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void Y(String str) {
        if (str == null) {
            return;
        }
        try {
            G0 = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            G0 = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void Y0(String str) {
        if (str == null) {
            return;
        }
        try {
            int parseInt = Integer.parseInt(str);
            if (parseInt == f.g0(this.Y)) {
                return;
            }
            if (parseInt != 1) {
                N(0);
            } else {
                N(1);
            }
            LocalLog.a("ConfigUpdateUtil", "updatePowerSaveFiveGFlag: updatePowerSaveFiveGFlag = " + parseInt);
        } catch (NumberFormatException e10) {
            N(1);
            LocalLog.d("ConfigUpdateUtil", "updatePowerSaveFiveGFlag: failed parsing value " + e10);
        }
    }

    private boolean Y1(OutputStream outputStream) {
        try {
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(outputStream, "utf-8");
            newSerializer.startDocument(null, Boolean.TRUE);
            newSerializer.startTag(null, "guardelf_config.xml");
            newSerializer.startTag(null, "AbnormalStartCount");
            newSerializer.text(String.valueOf(this.f10730f));
            newSerializer.endTag(null, "AbnormalStartCount");
            newSerializer.startTag(null, "CollectStartCount");
            newSerializer.text(String.valueOf(this.f10732g));
            newSerializer.endTag(null, "CollectStartCount");
            newSerializer.startTag(null, "CheckStartTimeInterval");
            newSerializer.text(String.valueOf(this.f10734h));
            newSerializer.endTag(null, "CheckStartTimeInterval");
            newSerializer.startTag(null, "ThresholdWorstIntervalPerWakeup");
            newSerializer.text(String.valueOf(this.f10736i));
            newSerializer.endTag(null, "ThresholdWorstIntervalPerWakeup");
            newSerializer.startTag(null, "ThresholdIntervalPerWakeup");
            newSerializer.text(String.valueOf(this.f10738j));
            newSerializer.endTag(null, "ThresholdIntervalPerWakeup");
            newSerializer.startTag(null, "ThresholdWarningIntervalPerWakeup");
            newSerializer.text(String.valueOf(this.f10740k));
            newSerializer.endTag(null, "ThresholdWarningIntervalPerWakeup");
            newSerializer.startTag(null, "ThresholdSeriousIntervalPerAlarm");
            newSerializer.text(String.valueOf(this.f10742l));
            newSerializer.endTag(null, "ThresholdSeriousIntervalPerAlarm");
            newSerializer.startTag(null, "ThresholdWakeLockTimeout");
            newSerializer.text(String.valueOf(this.f10744m));
            newSerializer.endTag(null, "ThresholdWakeLockTimeout");
            newSerializer.startTag(null, "NotifyInterval");
            newSerializer.text(String.valueOf(this.f10746n));
            newSerializer.endTag(null, "NotifyInterval");
            newSerializer.startTag(null, "UploadInterval");
            newSerializer.text(String.valueOf(this.f10748o));
            newSerializer.endTag(null, "UploadInterval");
            newSerializer.startTag(null, "CpuCheckInterval");
            newSerializer.text(String.valueOf(this.f10754r));
            newSerializer.endTag(null, "CpuCheckInterval");
            newSerializer.startTag(null, "CpuPercentThresh");
            newSerializer.text(String.valueOf(this.f10756s));
            newSerializer.endTag(null, "CpuPercentThresh");
            newSerializer.startTag(null, "TrafficFilterThresh");
            newSerializer.text(String.valueOf(this.f10762v));
            newSerializer.endTag(null, "TrafficFilterThresh");
            newSerializer.startTag(null, "ThreshBattIdleLowLevel");
            newSerializer.text(String.valueOf(this.f10764w));
            newSerializer.endTag(null, "ThreshBattIdleLowLevel");
            newSerializer.startTag(null, "ThreshBattIdleNormalLevel");
            newSerializer.text(String.valueOf(this.f10766x));
            newSerializer.endTag(null, "ThreshBattIdleNormalLevel");
            newSerializer.startTag(null, "ThreshBattIdleDelay");
            newSerializer.text(String.valueOf(this.f10768y));
            newSerializer.endTag(null, "ThreshBattIdleDelay");
            newSerializer.startTag(null, "ThreshJobMinInterval");
            newSerializer.text(String.valueOf(this.f10770z));
            newSerializer.endTag(null, "ThreshJobMinInterval");
            newSerializer.startTag(null, "TotalCpuMonitorSwitch");
            newSerializer.text(String.valueOf(E0));
            newSerializer.endTag(null, "TotalCpuMonitorSwitch");
            newSerializer.startTag(null, "AppCpuKillSwitch");
            newSerializer.text(String.valueOf(F0));
            newSerializer.endTag(null, "AppCpuKillSwitch");
            newSerializer.startTag(null, "ThreshTotalCpuSlight");
            newSerializer.text(String.valueOf(H0));
            newSerializer.endTag(null, "ThreshTotalCpuSlight");
            newSerializer.startTag(null, "ThreshTotalCpuMiddle");
            newSerializer.text(String.valueOf(I0));
            newSerializer.endTag(null, "ThreshTotalCpuMiddle");
            newSerializer.startTag(null, "ThreshTotalCpuHeavy");
            newSerializer.text(String.valueOf(J0));
            newSerializer.endTag(null, "ThreshTotalCpuHeavy");
            newSerializer.startTag(null, "ThreshCountContinuousSlight");
            newSerializer.text(String.valueOf(K0));
            newSerializer.endTag(null, "ThreshCountContinuousSlight");
            newSerializer.startTag(null, "ThreshCountContinuousMiddle");
            newSerializer.text(String.valueOf(L0));
            newSerializer.endTag(null, "ThreshCountContinuousMiddle");
            newSerializer.startTag(null, "ThreshCountContinuousHeavy");
            newSerializer.text(String.valueOf(M0));
            newSerializer.endTag(null, "ThreshCountContinuousHeavy");
            newSerializer.startTag(null, "IntervalTotalCpuSample");
            newSerializer.text(String.valueOf(N0));
            newSerializer.endTag(null, "IntervalTotalCpuSample");
            newSerializer.startTag(null, "IntervalAppCpuKill");
            newSerializer.text(String.valueOf(O0));
            newSerializer.endTag(null, "IntervalAppCpuKill");
            newSerializer.startTag(null, "ForeAppStableTime");
            newSerializer.text(String.valueOf(P0));
            newSerializer.endTag(null, "ForeAppStableTime");
            newSerializer.startTag(null, "thermalSwitch");
            newSerializer.text(String.valueOf(this.f10731f0));
            newSerializer.endTag(null, "thermalSwitch");
            newSerializer.startTag(null, "thermalPeriodTimeMillis");
            newSerializer.text(String.valueOf(this.f10733g0));
            newSerializer.endTag(null, "thermalPeriodTimeMillis");
            newSerializer.startTag(null, "thermalPredictThreshold");
            newSerializer.text(String.valueOf(this.f10735h0));
            newSerializer.endTag(null, "thermalPredictThreshold");
            newSerializer.startTag(null, "thermalPredictWeight");
            newSerializer.text(String.valueOf(this.f10737i0));
            newSerializer.endTag(null, "thermalPredictWeight");
            newSerializer.startTag(null, "thermalSamplingThreshold");
            newSerializer.text(String.valueOf(this.f10739j0));
            newSerializer.endTag(null, "thermalSamplingThreshold");
            newSerializer.startTag(null, "thermalSamplingWindowLength");
            newSerializer.text(String.valueOf(this.f10741k0));
            newSerializer.endTag(null, "thermalSamplingWindowLength");
            newSerializer.startTag(null, "thermalSamplingInterval");
            newSerializer.text(String.valueOf(this.f10743l0));
            newSerializer.endTag(null, "thermalSamplingInterval");
            newSerializer.startTag(null, "thermalLightThreshold");
            newSerializer.text(String.valueOf(this.f10745m0));
            newSerializer.endTag(null, "thermalLightThreshold");
            newSerializer.startTag(null, "thermalModerateThreshold");
            newSerializer.text(String.valueOf(this.f10747n0));
            newSerializer.endTag(null, "thermalModerateThreshold");
            newSerializer.startTag(null, "thermalCriticalThreshold");
            newSerializer.text(String.valueOf(this.f10749o0));
            newSerializer.endTag(null, "thermalCriticalThreshold");
            newSerializer.startTag(null, "thermalFrontTempWeight");
            newSerializer.text(String.valueOf(this.f10751p0));
            newSerializer.endTag(null, "thermalFrontTempWeight");
            newSerializer.startTag(null, "thermalFrameTempWeight");
            newSerializer.text(String.valueOf(this.f10753q0));
            newSerializer.endTag(null, "thermalFrameTempWeight");
            newSerializer.startTag(null, "thermalBackTempWeight");
            newSerializer.text(String.valueOf(this.f10755r0));
            newSerializer.endTag(null, "thermalBackTempWeight");
            newSerializer.startTag(null, "job_temperature_protect_timeout");
            newSerializer.text(String.valueOf(this.f10757s0));
            newSerializer.endTag(null, "job_temperature_protect_timeout");
            newSerializer.startTag(null, "job_scene_protect_timeout");
            newSerializer.text(String.valueOf(this.f10761u0));
            newSerializer.endTag(null, "job_scene_protect_timeout");
            newSerializer.startTag(null, "job_scene_protect_switch");
            newSerializer.text(String.valueOf(this.f10759t0));
            newSerializer.endTag(null, "job_scene_protect_switch");
            newSerializer.startTag(null, "oplus_guard_elf_switch");
            newSerializer.text(String.valueOf(this.f10771z0));
            newSerializer.endTag(null, "oplus_guard_elf_switch");
            newSerializer.startTag(null, "disable_screen_awake_pkg");
            newSerializer.text(String.valueOf(this.A0));
            newSerializer.endTag(null, "disable_screen_awake_pkg");
            newSerializer.endTag(null, "guardelf_config.xml");
            newSerializer.endDocument();
            return true;
        } catch (IOException e10) {
            LocalLog.d("ConfigUpdateUtil", "Failed to write state: " + e10);
            return false;
        }
    }

    private void Z0(String str) {
        try {
            this.f10769y0 = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.f10769y0 = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void b1(String str) {
        if (str == null) {
            return;
        }
        try {
            boolean parseBoolean = Boolean.parseBoolean(str);
            this.f10727d0 = parseBoolean;
            f.q2(this.Y, parseBoolean);
        } catch (NumberFormatException e10) {
            this.f10727d0 = true;
            LocalLog.d("ConfigUpdateUtil", "updateProjectSupportOplusStore: failed parsing value " + e10);
        }
    }

    private void c0(String str) {
        if (str == null) {
            return;
        }
        try {
            boolean parseBoolean = Boolean.parseBoolean(str);
            this.R = parseBoolean;
            f.Z1(this.Y, parseBoolean);
            if (this.R || !AppFeature.D()) {
                return;
            }
            ChargeProtectionController.N(this.Y).V();
        } catch (Exception e10) {
            this.R = true;
            LocalLog.d("ConfigUpdateUtil", "updateChargeProtectionSwitch: failed parsing value " + e10);
        }
    }

    private void c1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.S = Integer.parseInt(str);
        } catch (Exception e10) {
            this.S = 5400000;
            LocalLog.d("ConfigUpdateUtil", "updateRecoveryChargingDuration: failed parsing value " + e10);
        }
    }

    private void d0(String str) {
        try {
            this.f10754r = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10754r = 120000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void d1(String str) {
        if (str == null) {
            return;
        }
        try {
            int parseInt = Integer.parseInt(str);
            this.f10725c0 = parseInt;
            f.K2(this.Y, parseInt);
        } catch (NumberFormatException e10) {
            this.f10725c0 = 450;
            LocalLog.d("ConfigUpdateUtil", "updateReverseHighTempThreshold: failed parsing value " + e10);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x005a A[Catch: Exception -> 0x0041, TRY_LEAVE, TryCatch #1 {Exception -> 0x0041, blocks: (B:18:0x002f, B:20:0x0035, B:7:0x005a, B:5:0x0043), top: B:17:0x002f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String e(String str) {
        Cursor cursor;
        String string;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            cursor = this.Y.getContentResolver().query(C0, strArr, "filtername=\"" + str + "\"", null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        int columnIndex = cursor.getColumnIndex(ThermalControlConfig.COLUMN_NAME_XML);
                        cursor.moveToNext();
                        string = cursor.getString(columnIndex);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (Exception e10) {
                    e = e10;
                    if (cursor != null) {
                        cursor.close();
                    }
                    LocalLog.d("ConfigUpdateUtil", "We can not get Filtrate app data from provider,because of " + e);
                    return null;
                }
            }
            LocalLog.d("ConfigUpdateUtil", "The Filtrate app cursor is null !!!  filterName=" + str);
            string = null;
            if (cursor != null) {
            }
            return string;
        } catch (Exception e11) {
            e = e11;
            cursor = null;
        }
    }

    private void e1(String str) {
        Settings.Secure.putInt(this.Y.getContentResolver(), "guardelf_smart_doze_support", "true".equals(str) ? 1 : 0);
        LocalLog.a("ConfigUpdateUtil", "updateSmartDozeSwitch, value =" + str);
    }

    private void f0(String str) {
        try {
            this.f10752q = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.f10752q = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void g0(String str) {
        try {
            this.f10760u = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10760u = 5;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void g1(String str) {
        try {
            this.f10730f = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10730f = 200;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void h0(String str) {
        try {
            this.f10750p = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.f10750p = true;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void h1(String str) {
        try {
            this.f10734h = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            this.f10734h = 300000L;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void i0(String str) {
        try {
            this.f10756s = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10756s = 10;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void i1(String str) {
        try {
            this.f10732g = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10732g = 30;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void j0(String str) {
        try {
            this.f10758t = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10758t = 10;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void k1(String str) {
        try {
            this.f10767x0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10767x0 = 40;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void l0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10729e0 = Boolean.parseBoolean(str);
            LocalLog.a("ConfigUpdateUtil", "updateCurveAlarmWakeupFlag: PowerCurveAlarmWakeupSwitch = " + this.f10729e0);
        } catch (NumberFormatException e10) {
            this.f10729e0 = false;
            LocalLog.d("ConfigUpdateUtil", "updateCurveAlarmWakeupFlag: failed parsing value " + e10);
        }
    }

    private void l1(String str) {
        try {
            this.f10763v0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10763v0 = 40;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void m0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.M = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.M = 0;
            LocalLog.d("ConfigUpdateUtil", "mDeepSleepIsUseNetworkDisableWhiteList: failed parsing value " + e10);
        }
    }

    private void m1(String str) {
        try {
            this.f10765w0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            this.f10765w0 = 40;
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    public static synchronized ConfigUpdateUtil n(Context context) {
        ConfigUpdateUtil configUpdateUtil;
        synchronized (ConfigUpdateUtil.class) {
            if (D0 == null) {
                D0 = new ConfigUpdateUtil(context);
            }
            configUpdateUtil = D0;
        }
        return configUpdateUtil;
    }

    private void n0(String str) {
        if (str == null) {
            return;
        }
        try {
            int i10 = Settings.System.getInt(this.Y.getContentResolver(), "guardelf_config_list_version", 0);
            this.f10728e = i10;
            if (i10 == 0 || i10 <= this.f10726d || !Boolean.parseBoolean(str) || f.B(this.Y)) {
                return;
            }
            Settings.System.putInt(this.Y.getContentResolver(), "is_need_show_netoff_notify", 1);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "updateDeepSleepNetOffNotify: failed parsing value " + e10);
        }
    }

    private void n1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.F = Boolean.parseBoolean(str);
            LocalLog.d("ConfigUpdateUtil", "storageMonitorChatDisable=" + this.F);
        } catch (NumberFormatException e10) {
            this.F = false;
            LocalLog.d("ConfigUpdateUtil", "updateStorageMonitorChatDisable: failed parsing value " + e10);
        }
    }

    private void o0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.O = Boolean.parseBoolean(str);
            LocalLog.l("ConfigUpdateUtil", "updateDeepSleepNetworkSwitch: " + this.O);
        } catch (NumberFormatException e10) {
            this.O = true;
            LocalLog.d("ConfigUpdateUtil", "updateDeepSleepNetworkSwitch: failed parsing value " + e10);
        }
    }

    private void p0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.P = Boolean.parseBoolean(str);
        } catch (NumberFormatException e10) {
            this.P = false;
            LocalLog.d("ConfigUpdateUtil", "mDeepSleepResetAirPlaneSwitch: failed parsing value " + e10);
        }
    }

    private void q0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.N = Boolean.parseBoolean(str);
            LocalLog.l("ConfigUpdateUtil", "updateDeepSleepSwitch: " + this.N);
        } catch (NumberFormatException e10) {
            this.N = true;
            LocalLog.d("ConfigUpdateUtil", "updateDeepSleepSwitch: failed parsing value " + e10);
        }
    }

    private void r0(String str) {
        if (str == null) {
            return;
        }
        if ("true".equals(str)) {
            PowerConsumptionOptimizationHelper.k(this.Y).t(true, this.Y);
        } else if ("false".equals(str)) {
            PowerConsumptionOptimizationHelper.k(this.Y).t(false, this.Y);
        }
        LocalLog.a("ConfigUpdateUtil", "set TAG_GUARDELF_POLICY_KILL " + PowerConsumptionOptimizationHelper.k(this.Y).j(this.Y));
    }

    private void r1(String str) {
        if (str == null) {
            return;
        }
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong > 0) {
                this.f10757s0 = parseLong;
            }
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void s1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10755r0 = Float.parseFloat(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void t1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10749o0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void u0(String str) {
        if (str == null) {
            return;
        }
        this.A0 = str;
    }

    private void u1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10753q0 = Float.parseFloat(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void v1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10751p0 = Float.parseFloat(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void w0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10720a = str;
        } catch (NumberFormatException e10) {
            this.f10720a = "system&recent&lock&audio&guard&import";
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void w1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10745m0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void x0(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10722b = str;
        } catch (NumberFormatException e10) {
            this.f10722b = "system&audio&guard&lock&import";
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void x1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10747n0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void y1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10733g0 = Long.parseLong(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    private void z1(String str) {
        if (str == null) {
            return;
        }
        try {
            this.f10735h0 = Integer.parseInt(str);
        } catch (NumberFormatException e10) {
            LocalLog.d("ConfigUpdateUtil", "failed parsing value " + e10);
        }
    }

    public int A() {
        return this.f10763v0;
    }

    public int B() {
        return this.f10765w0;
    }

    public int C() {
        return S0;
    }

    public void C0() {
        Log.d("ConfigUpdateUtil", "updateHighPowerConfig");
        String e10 = e("sys_highpower_config_list");
        Log.d("ConfigUpdateUtil", "xmlValue: " + e10);
        if (e10 != null) {
            J(e10);
        }
        if (CommonUtil.T(this.Y)) {
            return;
        }
        AlarmSetter alarmSetter = new AlarmSetter(this.Y);
        alarmSetter.a();
        alarmSetter.d();
    }

    public int D() {
        return U0;
    }

    public void D0() {
        ThermalFactory.a(this.Y, Looper.getMainLooper()).u();
    }

    public int E() {
        return this.f10725c0;
    }

    public void E0() {
        String e10 = e("sys_wms_intercept_window");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "sys_wms_intercept_window.xml", e10, this.Y);
        }
    }

    public void F1() {
        String e10 = e("sys_pms_odex_whitelist");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "third_app_dex_list.xml", e10, this.Y);
        }
    }

    public void G() {
        PowerConsumptionOptimizationHelper.k(this.Y).t(AppFeature.D(), this.Y);
        U0();
        Z();
        a1();
        j1();
        f1();
        Q();
        k0();
        F1();
        o1();
        s0();
        E0();
        a0();
        n7.a.b(this.Y).c();
        q1();
        v0();
        p1();
        t0();
        W();
        z0();
        e0();
        C0();
        b0();
    }

    public boolean H(String str) {
        synchronized (this.X) {
            return this.W.contains(str);
        }
    }

    public void Q() {
        String e10 = e("sys_pms_adbinstaller_switch");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "adb_installer_status.xml", e10, this.Y);
        }
    }

    public void U0() {
        K(e("sys_guardelf_config_list"));
        F();
        M(this.Y.getApplicationContext());
    }

    public void W() {
        String e10 = e("sys_oplus_animation_config");
        if (e10 != null) {
            LocalFileUtil.c().m("animation", "sys_oplus_animation_config.xml", e10, this.Y);
        }
    }

    public void X0() {
        if (UserHandle.myUserId() != 0) {
            return;
        }
        String e10 = e("power_save_rus_config_list");
        if (e10 != null) {
            LocalFileUtil.c().m("battery", "power_save_rus_config_list.xml", e10, this.Y);
        }
        PowerSaveHelper.m(this.Y).I();
        LocalLog.a("ConfigUpdateUtil", "updatePowerSaveConfigList");
    }

    public void Z() {
        synchronized (this.X) {
            List<String> L = L(e("sys_guardelf_limitbkg_blacklist"));
            if (L != null && !L.isEmpty()) {
                if (this.W == null) {
                    this.W = new ArrayList();
                }
                this.W.clear();
                this.W.addAll(L);
            }
        }
    }

    public boolean a() {
        return F0;
    }

    public void a0() {
        String e10 = e("sys_ams_skipbroadcast");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "sys_ams_skipbroadcast.xml", e10, this.Y);
        }
    }

    public void a1() {
        String e10 = e("sys_ams_processfilter_list");
        if (e10 != null) {
            LocalFileUtil.c().m("battery", "sys_ams_processfilter_list.xml", e10, this.Y);
        }
    }

    public boolean b() {
        return G0;
    }

    public void b0() {
        Log.d("ConfigUpdateUtil", "updateChargeConfig");
        String e10 = e("sys_charge_config_list");
        Log.d("ConfigUpdateUtil", "xmlValue: " + e10);
        if (e10 != null) {
            I(e10);
        }
    }

    public ArrayList<String> c() {
        ArrayList<String> arrayList;
        synchronized (this.B0) {
            if (this.B0.isEmpty()) {
                arrayList = new ArrayList<>(OplusBatteryConstants.f19351c);
            } else {
                arrayList = new ArrayList<>(this.B0);
            }
        }
        return arrayList;
    }

    public boolean d() {
        return this.f10729e0;
    }

    public void e0() {
        List<String> L = L(e("sys_guardelf_cpu_kill_top_list"));
        if (L != null && !L.isEmpty()) {
            synchronized (this.B0) {
                this.B0.addAll(L);
            }
        }
        LocalLog.a("ConfigUpdateUtil", "updateCpuKillTopList: list=" + L);
    }

    public boolean f() {
        return this.Q;
    }

    public void f1() {
        String e10 = e("sys_rom_black_list");
        if (e10 != null) {
            LocalFileUtil.c().m("startup", "sys_rom_black_list.xml", e10, this.Y);
        }
    }

    public int g() {
        return this.M;
    }

    public int h() {
        return this.f10724c;
    }

    public boolean i() {
        return this.O;
    }

    public boolean j() {
        return this.P;
    }

    public void j1() {
        String e10 = e("sys_startupmanager_monitor_list");
        if (e10 != null) {
            LocalFileUtil.c().m("startup", "sys_startupmanager_monitor_list.xml", e10, this.Y);
        }
    }

    public boolean k() {
        return this.N;
    }

    public void k0() {
        String e10 = e("sys_ams_crashclear_whitelist");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "crashclear_white_list.xml", e10, this.Y);
        }
    }

    public String l() {
        return this.f10720a;
    }

    public String m() {
        return this.f10722b;
    }

    public long o() {
        return Q0;
    }

    public void o1() {
        String e10 = e("sys_system_config_list");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "systemConfigList.xml", e10, this.Y);
        }
    }

    public long p() {
        return R0;
    }

    public void p1() {
        String e10 = e("sys_freeform_config");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "sys_freeform_config.xml", e10, this.Y);
        }
    }

    public int q() {
        return this.B;
    }

    public void q1() {
        String e10 = e("sys_wms_splitapp_list");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "sys_wms_split_app.xml", e10, this.Y);
        }
    }

    public boolean r() {
        return this.A;
    }

    public int s() {
        return this.C;
    }

    public void s0() {
        String e10 = e("sys_pms_defaultpackage_list");
        if (e10 != null) {
            LocalFileUtil.c().m("config", "sys_pms_defaultpackage_list.xml", e10, this.Y);
        }
    }

    public boolean t() {
        return this.D;
    }

    public void t0() {
        String e10 = e("sys_direct_widget_config_list");
        if (e10 != null) {
            LocalFileUtil.c().m("colordirect", "sys_direct_widget_config_list.xml", e10, this.Y);
        }
    }

    public List<String> u() {
        return this.f10723b0;
    }

    public List<String> v() {
        return this.f10721a0;
    }

    public void v0() {
        String e10 = e("sys_display_compat_config");
        if (e10 != null) {
            LocalFileUtil.c().m("displaycompat", "sys_display_compat_config.xml", e10, this.Y);
        }
    }

    public boolean w() {
        return this.V;
    }

    public boolean x() {
        return f.g0(this.Y) == 1;
    }

    public int y() {
        return this.S;
    }

    public void y0() {
        String e10 = e("sys_extreme_deepsleep_list");
        if (e10 != null) {
            LocalFileUtil.c().m(OplusBatteryConstants.f19356h, "sys_extreme_deepsleep_list.xml", e10, this.Y);
        }
    }

    public int z() {
        return this.f10767x0;
    }

    public void z0() {
        String e10 = e("formatter_compatibility_config_list");
        if (e10 != null) {
            LocalFileUtil.c().m("formatercompact", "formatter_compatibility_config_list.xml", e10, this.Y);
        }
    }
}
