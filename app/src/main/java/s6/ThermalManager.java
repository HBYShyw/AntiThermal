package s6;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.statistics.DataTypeConstants;
import com.oplus.statistics.record.StatIdManager;
import com.oplus.thermalcontrol.ThermalControlConfig;
import d6.ConfigUpdateUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import s6.ThermalFactory;
import y5.AppFeature;

/* compiled from: ThermalManager.java */
/* renamed from: s6.h, reason: use source file name */
/* loaded from: classes.dex */
public class ThermalManager {
    private static final ArrayList<String> R = new a();
    protected ThermalFactory.b J;
    protected Context K;
    protected boolean L;
    protected boolean O;
    protected boolean P;
    private Bundle Q;

    /* renamed from: a, reason: collision with root package name */
    public ThermalHandler f18120a;

    /* renamed from: b, reason: collision with root package name */
    protected String f18121b;

    /* renamed from: c, reason: collision with root package name */
    protected long f18122c;

    /* renamed from: d, reason: collision with root package name */
    protected long f18123d;

    /* renamed from: e, reason: collision with root package name */
    protected String f18124e = "no file";

    /* renamed from: f, reason: collision with root package name */
    protected int f18125f = -999;

    /* renamed from: g, reason: collision with root package name */
    protected int f18126g = -999;

    /* renamed from: h, reason: collision with root package name */
    protected int f18127h = -999;

    /* renamed from: i, reason: collision with root package name */
    protected int f18128i = -999;

    /* renamed from: j, reason: collision with root package name */
    protected int f18129j = 40;

    /* renamed from: k, reason: collision with root package name */
    protected int f18130k = 460;

    /* renamed from: l, reason: collision with root package name */
    protected int f18131l = 430;

    /* renamed from: m, reason: collision with root package name */
    protected int f18132m = 410;

    /* renamed from: n, reason: collision with root package name */
    protected long f18133n = StatIdManager.EXPIRE_TIME_MS;

    /* renamed from: o, reason: collision with root package name */
    protected int f18134o = 490;

    /* renamed from: p, reason: collision with root package name */
    protected int f18135p = 470;

    /* renamed from: q, reason: collision with root package name */
    protected int f18136q = DataLinkConstants.USER_PROFILE_LABEL;

    /* renamed from: r, reason: collision with root package name */
    protected int f18137r = 490;

    /* renamed from: s, reason: collision with root package name */
    protected int f18138s = 490;

    /* renamed from: t, reason: collision with root package name */
    protected int f18139t = 470;

    /* renamed from: u, reason: collision with root package name */
    protected int f18140u = DataLinkConstants.USER_PROFILE_LABEL;

    /* renamed from: v, reason: collision with root package name */
    protected int f18141v = 490;

    /* renamed from: w, reason: collision with root package name */
    protected int f18142w = 490;

    /* renamed from: x, reason: collision with root package name */
    protected int f18143x = 470;

    /* renamed from: y, reason: collision with root package name */
    protected int f18144y = DataLinkConstants.USER_PROFILE_LABEL;

    /* renamed from: z, reason: collision with root package name */
    protected int f18145z = 490;
    protected int A = 750;
    protected int B = 30;
    protected int C = 15;
    protected long D = 60000;
    protected int E = 5;
    protected boolean F = false;
    protected boolean G = false;
    protected boolean H = true;
    protected ArrayList<String> I = new ArrayList<>();
    protected int M = -999;
    protected int N = -999;

    /* compiled from: ThermalManager.java */
    /* renamed from: s6.h$a */
    /* loaded from: classes.dex */
    class a extends ArrayList<String> {
        a() {
            add("com.android.contacts");
            add("com.android.incallui");
            add("com.android.mms");
            add("com.google.android.dialer");
            add("com.google.android.apps.messaging");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ThermalManager.java */
    /* renamed from: s6.h$b */
    /* loaded from: classes.dex */
    public class b implements FileFilter {
        b() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File file) {
            LocalLog.l("ThermalManager", "file " + file.getName());
            return file.getName().startsWith("sys_high_temp_protect");
        }
    }

    public ThermalManager(ThermalFactory.b bVar, Context context, Looper looper) {
        this.L = true;
        this.J = bVar;
        this.K = context;
        ThermalHandler thermalHandler = new ThermalHandler(looper, this.K, this);
        this.f18120a = thermalHandler;
        thermalHandler.sendEmptyMessage(1015);
        if (ActivityManager.getCurrentUser() != this.K.getUserId()) {
            this.L = false;
            LocalLog.a("ThermalManager", "mUserForeground false.");
        }
    }

    private void F(String str) {
        int next;
        if (str != null && !"".equals(str)) {
            ArrayList arrayList = new ArrayList();
            try {
                XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                newPullParser.setInput(new StringReader(str));
                newPullParser.nextTag();
                do {
                    next = newPullParser.next();
                    if (next == 2) {
                        String name = newPullParser.getName();
                        if ("HighTemperatureProtectSwitch".equals(name)) {
                            this.G = Boolean.parseBoolean(newPullParser.nextText());
                            LocalLog.l("ThermalManager", "updateConfig second switch " + this.G);
                        } else {
                            boolean equals = "HighTemperatureProtectThresholdIn".equals(name);
                            int i10 = DataLinkConstants.USER_PROFILE_LABEL;
                            if (equals) {
                                int parseInt = Integer.parseInt(newPullParser.nextText());
                                if (parseInt > 0) {
                                    i10 = parseInt;
                                }
                                this.f18140u = i10;
                                LocalLog.l("ThermalManager", "updateConfig original second in " + this.f18140u);
                            } else if ("HighTemperatureProtectThresholdOut".equals(name)) {
                                int parseInt2 = Integer.parseInt(newPullParser.nextText());
                                this.f18141v = parseInt2 > 0 ? parseInt2 : 490;
                                LocalLog.l("ThermalManager", "updateConfig original second out " + this.f18141v);
                            } else if ("CountBatteryChangedForFosUpdateTime".equals(name)) {
                                int parseInt3 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt3 <= 0) {
                                    parseInt3 = 30;
                                }
                                this.B = parseInt3;
                            } else if ("HighTemperatureProtectFirstStepIn".equals(name)) {
                                int parseInt4 = Integer.parseInt(newPullParser.nextText());
                                this.f18138s = parseInt4 > 0 ? parseInt4 : 490;
                                LocalLog.a("ThermalManager", "updateConfig original first In " + this.f18138s);
                            } else if ("HighTemperatureProtectFirstStepOut".equals(name)) {
                                int parseInt5 = Integer.parseInt(newPullParser.nextText());
                                this.f18139t = parseInt5 > 0 ? parseInt5 : 470;
                                LocalLog.a("ThermalManager", "updateConfig original first Out " + this.f18139t);
                            } else if ("HighTemperatureProtectShutDown".equals(name)) {
                                int parseInt6 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt6 <= 0) {
                                    parseInt6 = 750;
                                }
                                this.A = parseInt6;
                                LocalLog.a("ThermalManager", "updateConfig final " + this.A);
                            } else if ("HighTemperatureShutdownUpdateTime".equals(name)) {
                                int parseInt7 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt7 <= 0) {
                                    parseInt7 = 15;
                                }
                                this.C = parseInt7;
                            } else if ("HighTemperatureFirstStepSwitch".equals(name)) {
                                this.F = Boolean.parseBoolean(newPullParser.nextText());
                                LocalLog.l("ThermalManager", "updateConfig first switch " + this.F);
                            } else if ("HighTemperatureShutdownSwitch".equals(name)) {
                                this.H = Boolean.parseBoolean(newPullParser.nextText());
                                LocalLog.l("ThermalManager", "updateConfig fianl switch " + this.H);
                            } else if ("DoubleCheckTime".equals(name)) {
                                long parseLong = Long.parseLong(newPullParser.nextText());
                                if (parseLong <= 0) {
                                    parseLong = 60000;
                                }
                                this.D = parseLong;
                            } else if ("ToleranceSecondStepIn".equals(name)) {
                                int parseInt8 = Integer.parseInt(newPullParser.nextText());
                                this.f18127h = parseInt8 > 0 ? parseInt8 : -999;
                                LocalLog.a("ThermalManager", "updateConfig tolerance second In " + this.f18127h);
                            } else if ("ToleranceSecondStepOut".equals(name)) {
                                int parseInt9 = Integer.parseInt(newPullParser.nextText());
                                this.f18128i = parseInt9 > 0 ? parseInt9 : -999;
                                LocalLog.a("ThermalManager", "updateConfig tolerance second Out=" + this.f18128i);
                            } else if ("ToleranceFirstStepIn".equals(name)) {
                                int parseInt10 = Integer.parseInt(newPullParser.nextText());
                                this.f18125f = parseInt10 > 0 ? parseInt10 : -999;
                                LocalLog.a("ThermalManager", "updateConfig tolerance first In=" + this.f18125f);
                            } else if ("ToleranceFirstStepOut".equals(name)) {
                                int parseInt11 = Integer.parseInt(newPullParser.nextText());
                                this.f18126g = parseInt11 > 0 ? parseInt11 : -999;
                                LocalLog.a("ThermalManager", "updateConfig tolerance first Out=" + this.f18126g);
                            } else if ("ToleranceInterval".equals(name)) {
                                long parseLong2 = Long.parseLong(newPullParser.nextText());
                                if (parseLong2 <= 0) {
                                    parseLong2 = StatIdManager.EXPIRE_TIME_MS;
                                }
                                this.f18133n = parseLong2;
                            } else if ("ToleranceThreshold".equals(name)) {
                                int parseInt12 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt12 <= 0) {
                                    parseInt12 = 40;
                                }
                                this.f18129j = parseInt12;
                                LocalLog.l("ThermalManager", "updateConfig tolerance Threshold " + this.f18129j);
                            } else if ("ToleranceStart".equals(name)) {
                                int parseInt13 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt13 <= 0) {
                                    parseInt13 = 430;
                                }
                                this.f18131l = parseInt13;
                            } else if ("ToleranceStop".equals(name)) {
                                int parseInt14 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt14 <= 0) {
                                    parseInt14 = 410;
                                }
                                this.f18132m = parseInt14;
                            } else if ("ExceptTemperature".equals(name)) {
                                int parseInt15 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt15 <= 0) {
                                    parseInt15 = 460;
                                }
                                this.f18130k = parseInt15;
                            } else if ("HighTemperatureProtectThresholdInEnvHigh".equals(name)) {
                                int parseInt16 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt16 > 0) {
                                    i10 = parseInt16;
                                }
                                this.f18136q = i10;
                                LocalLog.l("ThermalManager", "updateConfig EnvTemp second in " + this.f18136q);
                            } else if ("HighTemperatureProtectThresholdOutEnvHigh".equals(name)) {
                                int parseInt17 = Integer.parseInt(newPullParser.nextText());
                                this.f18137r = parseInt17 > 0 ? parseInt17 : 490;
                                LocalLog.l("ThermalManager", "updateConfig EnvTemp second out " + this.f18137r);
                            } else if ("HighTemperatureProtectFirstStepInEnvHigh".equals(name)) {
                                int parseInt18 = Integer.parseInt(newPullParser.nextText());
                                this.f18134o = parseInt18 > 0 ? parseInt18 : 490;
                                LocalLog.l("ThermalManager", "updateConfig EnvTemp first in " + this.f18134o);
                            } else if ("HighTemperatureProtectFirstStepOutEnvHigh".equals(name)) {
                                int parseInt19 = Integer.parseInt(newPullParser.nextText());
                                this.f18135p = parseInt19 > 0 ? parseInt19 : 470;
                                LocalLog.l("ThermalManager", "updateConfig EnvTemp first in " + this.f18135p);
                            } else if ("HighTempDialogTime".equals(name)) {
                                int parseInt20 = Integer.parseInt(newPullParser.nextText());
                                if (parseInt20 <= 0) {
                                    parseInt20 = 5;
                                }
                                this.E = parseInt20;
                            } else if ("HighTempFilterList".equals(name)) {
                                String nextText = newPullParser.nextText();
                                if (nextText != null && !arrayList.contains(nextText)) {
                                    arrayList.add(nextText);
                                }
                                LocalLog.l("ThermalManager", "updateConfig listPkg: " + arrayList);
                            }
                        }
                    }
                } while (next != 1);
            } catch (IOException e10) {
                e10.printStackTrace();
            } catch (XmlPullParserException e11) {
                e11.printStackTrace();
            }
            if (arrayList.size() > 0) {
                this.I.clear();
            }
            for (int i11 = 0; i11 < arrayList.size(); i11++) {
                String str2 = (String) arrayList.get(i11);
                if (!this.I.contains(str2)) {
                    this.I.add(str2);
                }
            }
            LocalLog.l("ThermalManager", "mWhiteListAll: " + arrayList);
            return;
        }
        LocalLog.l("ThermalManager", "updateConfigFromXml file empty");
    }

    private void a(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception unused) {
            }
        }
    }

    private void c() {
        LocalLog.l("ThermalManager", "configUpdate");
        String i10 = i();
        LocalLog.a("ThermalManager", "configUpdate content=" + i10);
        F(i10);
        b();
        B();
    }

    private ArrayList<String> k() {
        ArrayList<String> arrayList = new ArrayList<>();
        int i10 = 0;
        if (this.I.size() <= 0) {
            while (true) {
                ArrayList<String> arrayList2 = R;
                if (i10 >= arrayList2.size()) {
                    break;
                }
                String str = arrayList2.get(i10);
                if (!arrayList.contains(str)) {
                    arrayList.add(str);
                }
                i10++;
            }
        } else {
            while (i10 < this.I.size()) {
                String str2 = this.I.get(i10);
                if (!arrayList.contains(str2)) {
                    arrayList.add(str2);
                }
                i10++;
            }
        }
        String C = f6.f.C(this.K);
        if (C != null && !arrayList.contains(C)) {
            LocalLog.a("ThermalManager", "dialer: " + C);
            arrayList.add(C);
        }
        String D = f6.f.D(this.K);
        if (D != null && !arrayList.contains(D)) {
            LocalLog.a("ThermalManager", "mms: " + D);
            arrayList.add(D);
        }
        LocalLog.a("ThermalManager", "whiteList: " + arrayList);
        return arrayList;
    }

    private File l(String str) {
        File file = new File(str);
        if (!file.exists()) {
            LocalLog.l("ThermalManager", "readConfigFromXml path not exist. " + str);
            return null;
        }
        if (!file.isDirectory()) {
            LocalLog.l("ThermalManager", "readConfigFromXml path is not dir. " + str);
            return null;
        }
        File[] listFiles = file.listFiles(new b());
        if (listFiles != null && listFiles.length > 0) {
            return listFiles[0];
        }
        LocalLog.l("ThermalManager", "readConfigFromXml no file match. " + str);
        return null;
    }

    private XmlPullParser p(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new InputStreamReader(inputStream, "UTF-8"));
            newPullParser.nextTag();
            return newPullParser;
        } catch (Exception e10) {
            LocalLog.b("ThermalManager", "get xml parser get error: " + e10);
            return null;
        }
    }

    private boolean q() {
        return "true".equals(SystemProperties.get("persist.sys.oplus.eng.full.aging"));
    }

    private boolean r(String str) {
        return Pattern.compile("-?[0-9]*").matcher(str).matches();
    }

    private boolean s() {
        return AppFeature.A();
    }

    private long x(String str) {
        int next;
        if (str == null) {
            LocalLog.l("ThermalManager", "parseVersion: null string");
            return 0L;
        }
        if (TextUtils.isEmpty(str)) {
            LocalLog.l("ThermalManager", "parseVersion: empty string");
            return 0L;
        }
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e10) {
            LocalLog.l("ThermalManager", "parseVersion  error: " + e10);
        }
        if (byteArrayInputStream == null) {
            LocalLog.l("ThermalManager", "parseVersion: null inputStream");
            return 0L;
        }
        XmlPullParser p10 = p(byteArrayInputStream);
        if (p10 == null) {
            LocalLog.l("ThermalManager", "parseVersion: null XmlPullParser");
            return 0L;
        }
        do {
            try {
                try {
                    try {
                        try {
                            try {
                                try {
                                    next = p10.next();
                                    if (next == 2 && "version".equals(p10.getName())) {
                                        String nextText = p10.nextText();
                                        if (r(nextText)) {
                                            return Long.parseLong(nextText);
                                        }
                                        LocalLog.l("ThermalManager", "parseVersion: not numeric. versionText: " + nextText);
                                    }
                                } catch (IndexOutOfBoundsException e11) {
                                    LocalLog.l("ThermalManager", "parse version get error: " + e11);
                                } catch (NumberFormatException e12) {
                                    LocalLog.l("ThermalManager", "parse version get error: " + e12);
                                }
                            } catch (NullPointerException e13) {
                                LocalLog.l("ThermalManager", "parse version get error: " + e13);
                            }
                        } catch (IOException e14) {
                            LocalLog.l("ThermalManager", "parse version get error: " + e14);
                        }
                    } catch (Exception e15) {
                        LocalLog.l("ThermalManager", "parse version get error: " + e15);
                    }
                } catch (XmlPullParserException e16) {
                    LocalLog.l("ThermalManager", "parse version get error: " + e16);
                }
            } finally {
                a(byteArrayInputStream);
            }
        } while (next != 1);
        return 0L;
    }

    public void A() {
        this.O = false;
        this.P = false;
        LocalLog.l("ThermalManager", "resetSimuEnvTemp");
    }

    public void B() {
        LocalLog.a("ThermalManager", "sendHighTempWhiteListBroadcast");
        Intent intent = new Intent("oplus.intent.action.HIGH_TEMP_WHITE_LIST");
        intent.putStringArrayListExtra("filterapplist", k());
        this.K.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    public int C(boolean z10) {
        if (z10) {
            this.O = true;
            this.P = false;
        } else {
            this.O = false;
            this.P = true;
        }
        LocalLog.l("ThermalManager", "setSimuEnvTemp isHigh=" + z10);
        Bundle bundle = this.Q;
        if (bundle == null) {
            LocalLog.l("ThermalManager", "BundleBatteryBak is null");
            return -1;
        }
        v(bundle);
        return 0;
    }

    public void D(int i10) {
        this.M = i10;
    }

    public int E(int i10) {
        this.N = i10;
        LocalLog.l("ThermalManager", "setSimuTriggerTemp " + i10);
        Bundle bundle = this.Q;
        if (bundle == null) {
            LocalLog.l("ThermalManager", "BundleBatteryBak is null");
            return -1;
        }
        v(bundle);
        return 0;
    }

    protected void b() {
        this.f18142w = this.f18138s;
        this.f18143x = this.f18139t;
        this.f18144y = this.f18140u;
        this.f18145z = this.f18141v;
        LocalLog.l("ThermalManager", "configThreshold  firstStepTempIn=" + this.f18142w + ", firstStepTempOut=" + this.f18143x + ", secondStepTempIn=" + this.f18144y + ", secondStepTempOut=" + this.f18145z);
    }

    public String d() {
        int i10 = this.f18142w;
        int i11 = this.f18143x;
        int i12 = this.f18144y;
        int i13 = this.f18145z;
        int i14 = this.A;
        boolean z10 = this.F;
        boolean z11 = this.G;
        boolean z12 = this.H;
        boolean q10 = q();
        boolean s7 = s();
        if (s7) {
            LocalLog.l("ThermalManager", "getConfig: isOffAging=" + s7 + ", isAging=" + q10);
            i10 = DataLinkConstants.RUS_UPDATE;
            i11 = 9990;
            z10 = false;
            i12 = 10000;
            i13 = 9990;
            z11 = false;
        } else if (q10) {
            LocalLog.l("ThermalManager", "getConfig: isOffAging=" + s7 + ", isAging=" + q10);
            i11 = 580;
            i12 = DataLinkConstants.THUMBNAIL_HEALTH;
            i10 = 590;
            i13 = 590;
        }
        if (AppFeature.h()) {
            i14 = 630;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Source=" + this.f18121b + "\n");
        stringBuffer.append("RusVersion=" + this.f18122c + "\n");
        stringBuffer.append("LocalVersion=" + this.f18123d + "\n");
        stringBuffer.append("Type=" + this.J.name() + "\n");
        stringBuffer.append("FirstStepSwitch=" + z10 + "\n");
        stringBuffer.append("SecondStepSwitch=" + z11 + "\n");
        stringBuffer.append("FinalStepSwitch=" + z12 + "\n");
        stringBuffer.append("FirstStepTempIn=" + i10 + "\n");
        stringBuffer.append("FirstStepTempOut=" + i11 + "\n");
        stringBuffer.append("SecondStepTempIn=" + i12 + "\n");
        stringBuffer.append("SecondStepTempOut=" + i13 + "\n");
        stringBuffer.append("FinalStepTemp=" + i14 + "\n");
        stringBuffer.append("DoubleCheckTime=" + (this.D / 1000) + "\n");
        stringBuffer.append("DialogTime=" + this.B + "\n");
        stringBuffer.append("ShutdownTime=" + this.C + "\n");
        stringBuffer.append("TempFromBoard=" + AppFeature.x() + "\n");
        stringBuffer.append("isOffAging=" + s7 + "\n");
        stringBuffer.append("isAging=" + q10 + "\n");
        stringBuffer.append("isFactory=" + AppFeature.h() + "\n");
        stringBuffer.append("HighTempDialogTime=" + j() + "\n");
        stringBuffer.append("WhiteList=" + k() + "\n");
        return stringBuffer.toString();
    }

    public int e() {
        return this.B;
    }

    public long f() {
        return this.D;
    }

    public int g() {
        if (AppFeature.h()) {
            return 630;
        }
        return this.A;
    }

    public int h() {
        int i10 = this.f18142w;
        boolean q10 = q();
        boolean s7 = s();
        if (s7) {
            LocalLog.l("ThermalManager", "getFirstStepTempIn: isOffAging=" + s7 + ", isAging=" + q10);
            return DataLinkConstants.RUS_UPDATE;
        }
        if (!q10) {
            return i10;
        }
        LocalLog.l("ThermalManager", "getFirstStepTempIn: isOffAging=" + s7 + ", isAging=" + q10);
        return 590;
    }

    protected String i() {
        String z10 = z();
        String y4 = y();
        this.f18123d = x(z10);
        long x10 = x(y4);
        this.f18122c = x10;
        if (x10 >= this.f18123d) {
            this.f18121b = "RUS Provider";
            LocalLog.l("ThermalManager", "getHighTempConfigString remote., localVersion=" + this.f18123d + ", remoteVersion=" + this.f18122c);
            return y4;
        }
        this.f18121b = this.f18124e;
        LocalLog.l("ThermalManager", "getHighTempConfigString local., localVersion=" + this.f18123d + ", remoteVersion=" + this.f18122c);
        return z10;
    }

    public int j() {
        return this.E;
    }

    public int m() {
        int i10 = this.f18144y;
        boolean q10 = q();
        boolean s7 = s();
        if (s7) {
            LocalLog.l("ThermalManager", "getSecondStepTempIn: isOffAging=" + s7 + ", isAging=" + q10);
            return DataLinkConstants.RUS_UPDATE;
        }
        if (!q10) {
            return i10;
        }
        LocalLog.l("ThermalManager", "getSecondStepTempIn: isOffAging=" + s7 + ", isAging=" + q10);
        return DataLinkConstants.THUMBNAIL_HEALTH;
    }

    public int n() {
        return this.C;
    }

    public int o() {
        return this.M;
    }

    public void t() {
        LocalLog.l("ThermalManager", "onCreate");
        c();
    }

    public void u() {
        LocalLog.l("ThermalManager", "onRUSUpdate");
        c();
    }

    public void v(Bundle bundle) {
        int i10;
        this.Q = new Bundle(bundle);
        if (!this.L) {
            LocalLog.a("ThermalManager", "skip onTemperatureChanged for user background");
            return;
        }
        int i11 = bundle.getInt("highTempProtectSrcTemp");
        int i12 = bundle.getInt("status");
        if (AppFeature.x() && (i10 = this.N) > 0) {
            LocalLog.l("ThermalManager", "onTemperatureChanged: SimuTriggerTemp " + i10);
            i11 = i10;
        }
        int i13 = this.f18142w;
        int i14 = this.f18143x;
        int i15 = this.f18144y;
        int i16 = this.f18145z;
        int i17 = this.A;
        boolean z10 = this.F;
        boolean z11 = this.G;
        boolean z12 = this.H;
        boolean q10 = q();
        boolean s7 = s();
        if (s7) {
            LocalLog.l("ThermalManager", "onTemperatureChanged: isOffAging=" + s7 + ", isAging=" + q10);
            i13 = DataLinkConstants.RUS_UPDATE;
            i14 = 9990;
            z10 = false;
            i15 = 10000;
            i16 = 9990;
            z11 = false;
        } else if (q10) {
            LocalLog.l("ThermalManager", "onTemperatureChanged: isOffAging=" + s7 + ", isAging=" + q10);
            i14 = 580;
            i15 = DataLinkConstants.THUMBNAIL_HEALTH;
            i13 = 590;
            i16 = 590;
        }
        if (AppFeature.h()) {
            i17 = 630;
        }
        this.f18120a.M(i11, i12);
        if (i11 >= i17 && z12) {
            this.f18120a.sendEmptyMessage(1013);
        } else if (i11 >= i15 && z11) {
            if (!ThermalControlConfig.getInstance(this.K).isThermalControlEnable()) {
                this.f18120a.sendEmptyMessage(1012);
            }
        } else if (i11 >= i13 && z10 && !ThermalControlConfig.getInstance(this.K).isThermalControlEnable()) {
            this.f18120a.sendEmptyMessage(1011);
        }
        if (i11 <= i16 && !ThermalControlConfig.getInstance(this.K).isThermalControlEnable()) {
            this.f18120a.sendEmptyMessage(DataTypeConstants.SPECIAL_APP_START);
        }
        if (i11 > i14 || ThermalControlConfig.getInstance(this.K).isThermalControlEnable()) {
            return;
        }
        this.f18120a.sendEmptyMessage(DataTypeConstants.PAGE_VISIT);
    }

    public void w(boolean z10) {
        LocalLog.l("ThermalManager", "onUserChange");
        this.L = z10;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0037 A[Catch: Exception -> 0x002d, TRY_LEAVE, TryCatch #0 {Exception -> 0x002d, blocks: (B:18:0x001b, B:20:0x0021, B:7:0x0037, B:5:0x002f), top: B:17:0x001b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected String y() {
        Cursor cursor;
        String string;
        try {
            cursor = this.K.getContentResolver().query(ConfigUpdateUtil.C0, new String[]{ThermalControlConfig.COLUMN_NAME_XML}, "filtername=\"sys_high_temp_protect\"", null, null);
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
                    LocalLog.d("ThermalManager", "We can not get Filtrate app data from provider,because of " + e);
                    return null;
                }
            }
            LocalLog.d("ThermalManager", "The Filtrate app cursor is null !!!  filterName=sys_high_temp_protect");
            string = null;
            if (cursor != null) {
            }
            return string;
        } catch (Exception e11) {
            e = e11;
            cursor = null;
        }
    }

    protected String z() {
        int read;
        File l10 = l("/my_bigball/etc/temperature_profile/");
        FileInputStream fileInputStream = null;
        fileInputStream = null;
        fileInputStream = null;
        fileInputStream = null;
        fileInputStream = null;
        if (l10 == null || !l10.exists()) {
            LocalLog.l("ThermalManager", "DEFAULT_BIGBALL_FILE_PATH file not found");
            l10 = l("/odm/etc/temperature_profile/");
            if (l10 == null || !l10.exists()) {
                LocalLog.l("ThermalManager", "DEFAULT_ODM_FILE_PATH file not found");
                l10 = l("/my_product/etc/temperature_profile/");
                if (l10 == null || !l10.exists()) {
                    LocalLog.l("ThermalManager", "readConfigFromXml file not found");
                    return null;
                }
            }
        }
        this.f18124e = l10.getName();
        LocalLog.l("ThermalManager", "readConfigFromXml " + this.f18124e);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            try {
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(l10);
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            read = fileInputStream2.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            stringBuffer.append(new String(bArr, 0, read, "UTF-8"));
                        }
                        fileInputStream2.close();
                        fileInputStream = read;
                    } catch (FileNotFoundException e10) {
                        e = e10;
                        fileInputStream = fileInputStream2;
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                            fileInputStream = fileInputStream;
                        }
                        return stringBuffer.toString();
                    } catch (UnsupportedEncodingException e11) {
                        e = e11;
                        fileInputStream = fileInputStream2;
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                            fileInputStream = fileInputStream;
                        }
                        return stringBuffer.toString();
                    } catch (IOException e12) {
                        e = e12;
                        fileInputStream = fileInputStream2;
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            fileInputStream.close();
                            fileInputStream = fileInputStream;
                        }
                        return stringBuffer.toString();
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e13) {
                                e13.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e14) {
                    e = e14;
                } catch (UnsupportedEncodingException e15) {
                    e = e15;
                } catch (IOException e16) {
                    e = e16;
                }
            } catch (IOException e17) {
                e17.printStackTrace();
            }
            return stringBuffer.toString();
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
