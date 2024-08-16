package aa;

import android.app.Application;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.OplusPackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Xml;
import b6.LocalLog;
import ba.CdoGameAppsConfigListParser;
import ba.SysStartupManagerConfigListParser;
import ba.SysStartupV3ConfigListParser;
import com.oplus.datasync.OplusDataSyncManager;
import com.oplus.ota.OplusSystemUpdateInfo;
import com.oplus.settings.OplusSettings;
import com.oplus.startupapp.data.database.RecordDatabase;
import com.oplus.thermalcontrol.ThermalControlConfig;
import f6.CommonUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import u9.StartupConst;
import u9.StartupManager;
import x9.WhiteListUtils;
import z9.AppToShow;

/* compiled from: StartupDataUtils.java */
/* renamed from: aa.i, reason: use source file name */
/* loaded from: classes2.dex */
public class StartupDataUtils {

    /* renamed from: l, reason: collision with root package name */
    private static volatile StartupDataUtils f177l;

    /* renamed from: a, reason: collision with root package name */
    private Context f178a;

    /* renamed from: b, reason: collision with root package name */
    private RecordDatabase f179b;

    /* renamed from: e, reason: collision with root package name */
    private Set<String> f182e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f183f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f184g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f185h;

    /* renamed from: k, reason: collision with root package name */
    private boolean f188k;

    /* renamed from: c, reason: collision with root package name */
    private ArrayList<String> f180c = new ArrayList<>();

    /* renamed from: d, reason: collision with root package name */
    private ArrayList<String> f181d = new ArrayList<>();

    /* renamed from: i, reason: collision with root package name */
    private volatile boolean f186i = true;

    /* renamed from: j, reason: collision with root package name */
    private volatile boolean f187j = false;

    private StartupDataUtils(Context context) {
        this.f182e = new HashSet();
        this.f183f = true;
        this.f184g = true;
        this.f185h = true;
        this.f188k = false;
        this.f178a = context;
        if (!Application.getProcessName().equals("com.oplus.battery:remote")) {
            this.f179b = RecordDatabase.u(this.f178a);
        }
        this.f183f = q();
        SharedPreferences sharedPreferences = this.f178a.getSharedPreferences("check_monitor_init", 0);
        this.f184g = sharedPreferences.getBoolean("is_need_monitor", true);
        this.f182e = sharedPreferences.getStringSet("monitor_list", new HashSet());
        boolean c10 = y5.b.c();
        this.f185h = c10;
        p(c10);
        this.f188k = t();
        LocalLog.l("StartupManager", "IsNeedMonitor:" + this.f184g + " IsInChina:" + this.f185h + " MonitorList:" + this.f182e + " sysAppUploadPreSwitch: " + this.f186i + " sysAppUploadReleaseSwitch: " + this.f187j);
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x019c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01ab A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:66:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean D(List<String> list, List<String> list2, Map<String, List<String>> map, Map<String, List<String>> map2, boolean z10) {
        String str;
        String str2;
        Throwable th;
        OutputStream outputStream;
        Throwable th2;
        String str3;
        OutputStream outputStream2;
        String str4;
        String str5 = "StartupManager";
        List<String> list3 = map.get("userCare");
        List<String> list4 = map.get("pkgName");
        List<String> c10 = WhiteListUtils.c(this.f178a, z10);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        List<String> list5 = map2.get("pkgName");
        List<String> list6 = map2.get("userCare");
        List<String> c11 = WhiteListUtils.c(this.f178a, !z10);
        String str6 = z10 ? "1" : "0";
        String str7 = z10 ? "0" : "1";
        try {
            outputStream = OplusSettings.writeConfig(this.f178a, "startup/startup_dynamic_list.xml", 0);
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                newSerializer.setOutput(outputStream, "utf-8");
                try {
                    newSerializer.startDocument(null, Boolean.TRUE);
                    Iterator<String> it = list4.iterator();
                    while (true) {
                        str3 = str5;
                        outputStream2 = outputStream;
                        int i10 = 2;
                        str4 = str7;
                        int i11 = 1;
                        if (it.hasNext()) {
                            try {
                                String next = it.next();
                                if (arrayList.contains(next)) {
                                    outputStream = outputStream2;
                                    str5 = str3;
                                    str7 = str4;
                                } else {
                                    arrayList.add(next);
                                    if (!c10.contains(next)) {
                                        i10 = 0;
                                    }
                                    if (!list3.contains(next)) {
                                        i11 = 0;
                                    }
                                    Iterator<String> it2 = it;
                                    newSerializer.startTag(null, "dynamic");
                                    newSerializer.attribute(null, "pkgName", next);
                                    newSerializer.attribute(null, "type", str6);
                                    newSerializer.attribute(null, "source", String.valueOf(i10 | i11));
                                    newSerializer.attribute(null, "switch", list.contains(next) ? "1" : "0");
                                    newSerializer.endTag(null, "dynamic");
                                    outputStream = outputStream2;
                                    str5 = str3;
                                    str7 = str4;
                                    it = it2;
                                }
                            } catch (Exception e10) {
                                e = e10;
                                outputStream = outputStream2;
                                str = " saveConfigToDynamicFile stream close failed.";
                                str2 = str3;
                                try {
                                    LocalLog.c(str2, "saveConfigToDynamicFile failed.", e);
                                    if (outputStream != null) {
                                        return false;
                                    }
                                    try {
                                        outputStream.close();
                                        return false;
                                    } catch (IOException e11) {
                                        LocalLog.c(str2, str, e11);
                                        return false;
                                    }
                                } catch (Throwable th3) {
                                    th2 = th3;
                                    th = th2;
                                    if (outputStream != null) {
                                        try {
                                            outputStream.close();
                                            throw th;
                                        } catch (IOException e12) {
                                            LocalLog.c(str2, str, e12);
                                            throw th;
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                outputStream = outputStream2;
                                th = th4;
                                str = " saveConfigToDynamicFile stream close failed.";
                                str2 = str3;
                                if (outputStream != null) {
                                }
                            }
                        } else {
                            try {
                                break;
                            } catch (Exception e13) {
                                e = e13;
                                str = " saveConfigToDynamicFile stream close failed.";
                                str2 = str3;
                                outputStream = outputStream2;
                                LocalLog.c(str2, "saveConfigToDynamicFile failed.", e);
                                if (outputStream != null) {
                                }
                            } catch (Throwable th5) {
                                th2 = th5;
                                str = " saveConfigToDynamicFile stream close failed.";
                                str2 = str3;
                                outputStream = outputStream2;
                                th = th2;
                                if (outputStream != null) {
                                }
                            }
                        }
                    }
                    for (String str8 : list5) {
                        if (!arrayList2.contains(str8)) {
                            arrayList2.add(str8);
                            int i12 = c11.contains(str8) ? 2 : 0;
                            int i13 = list6.contains(str8) ? 1 : 0;
                            newSerializer.startTag(null, "dynamic");
                            newSerializer.attribute(null, "pkgName", str8);
                            String str9 = str4;
                            newSerializer.attribute(null, "type", str9);
                            newSerializer.attribute(null, "source", String.valueOf(i12 | i13));
                            newSerializer.attribute(null, "switch", list2.contains(str8) ? "1" : "0");
                            newSerializer.endTag(null, "dynamic");
                            str4 = str9;
                        }
                    }
                    newSerializer.endDocument();
                    if (outputStream2 != null) {
                        try {
                            outputStream2.close();
                        } catch (IOException e14) {
                            LocalLog.c(str3, " saveConfigToDynamicFile stream close failed.", e14);
                        }
                    }
                    return true;
                } catch (Exception e15) {
                    e = e15;
                    str2 = str5;
                    str = " saveConfigToDynamicFile stream close failed.";
                } catch (Throwable th6) {
                    th2 = th6;
                    str2 = str5;
                    str = " saveConfigToDynamicFile stream close failed.";
                }
            } catch (Exception e16) {
                e = e16;
                str = " saveConfigToDynamicFile stream close failed.";
                str2 = "StartupManager";
            } catch (Throwable th7) {
                th2 = th7;
                str = " saveConfigToDynamicFile stream close failed.";
                str2 = "StartupManager";
            }
        } catch (Exception e17) {
            e = e17;
            str = " saveConfigToDynamicFile stream close failed.";
            str2 = "StartupManager";
            outputStream = null;
        } catch (Throwable th8) {
            str = " saveConfigToDynamicFile stream close failed.";
            str2 = "StartupManager";
            th = th8;
            outputStream = null;
        }
    }

    public static void J(Bundle bundle) {
        try {
            OplusDataSyncManager.getInstance().updateAppData("startup", bundle);
        } catch (Exception e10) {
            e10.printStackTrace();
            Log.d("StartupManager", "update app data failed");
        }
    }

    private void P(List<String> list, boolean z10) {
        ArrayList<String> arrayList = z10 ? this.f180c : this.f181d;
        String str = z10 ? "auto_start" : "associate_start";
        synchronized (arrayList) {
            arrayList.clear();
            arrayList.addAll(list);
        }
        this.f178a.getContentResolver().notifyChange(Uri.parse("content://com.oplus.startup.provider/" + str), null);
        LocalLog.a("StartupManager", "notify startup allow list changed. " + str);
    }

    private void c(Map<String, List<String>> map, String str, boolean z10) {
        if (map == null) {
            return;
        }
        synchronized (map) {
            for (List<String> list : map.values()) {
                if (list.contains(str)) {
                    LocalLog.a("StartupManager", "remove pkgName: " + str + " from dynamic config, isAutoStart: " + z10);
                    list.remove(str);
                }
            }
        }
    }

    public static synchronized StartupDataUtils h(Context context) {
        StartupDataUtils startupDataUtils;
        synchronized (StartupDataUtils.class) {
            if (f177l == null) {
                f177l = new StartupDataUtils(context.getApplicationContext());
            }
            startupDataUtils = f177l;
        }
        return startupDataUtils;
    }

    private void p(boolean z10) {
        SharedPreferences sharedPreferences = this.f178a.getSharedPreferences("sysAppPreventStartRecordUploadConfig", 0);
        this.f186i = sharedPreferences.getBoolean("preSwitch", z10);
        this.f187j = sharedPreferences.getBoolean("releaseSwitch", false);
    }

    private boolean q() {
        String processName = Application.getProcessName();
        return processName.contains("com.oplus.persist.system") || processName.equals("com.oplus.battery");
    }

    private boolean t() {
        String str = SystemProperties.get("ro.build.version.ota");
        return (str != null && str.contains("PRE")) || SystemProperties.getBoolean("persist.sys.startup.debug.pre", false);
    }

    public static void u(String str) {
        String substring = str.substring(str.lastIndexOf(47) + 1);
        Log.d("StartupManager", "notifyListChanged:  " + substring);
        Bundle bundle = new Bundle();
        bundle.putString("updatelist", substring);
        bundle.putInt("userid", UserHandle.myUserId());
        J(bundle);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void v(List<String> list, List<String> list2, boolean z10, boolean z11) {
        Throwable th;
        ContentProviderClient contentProviderClient;
        Exception e10;
        LocalLog.a("StartupManager", "notify main proc list changed.");
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("allow_list", new ArrayList<>(list));
        bundle.putStringArrayList("other_allow_list", new ArrayList<>(list2));
        bundle.putBoolean("autostart_check", z10);
        bundle.putBoolean("exactly_start_type_changed", z11);
        try {
            contentProviderClient = this.f178a.getContentResolver().acquireUnstableContentProviderClient(StartupConst.f18932b);
            if (contentProviderClient != null) {
                try {
                    try {
                        contentProviderClient.call("update_main_proc_list", null, bundle);
                    } catch (Throwable th2) {
                        th = th2;
                        if (contentProviderClient != null) {
                            contentProviderClient.close();
                        }
                        throw th;
                    }
                } catch (Exception e11) {
                    e10 = e11;
                    e10.printStackTrace();
                    if (contentProviderClient == null) {
                        return;
                    }
                    contentProviderClient.close();
                }
            }
            if (contentProviderClient == null) {
                return;
            }
        } catch (Exception e12) {
            e10 = e12;
            contentProviderClient = null;
        } catch (Throwable th3) {
            th = th3;
            contentProviderClient = null;
            if (contentProviderClient != null) {
            }
            throw th;
        }
        contentProviderClient.close();
    }

    private String z(InputStream inputStream) {
        String str = null;
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[4096];
        while (true) {
            try {
                try {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                } catch (IOException e10) {
                    LocalLog.a("StartupManager", "readTextFile get error: " + e10);
                }
            } catch (Throwable th) {
                try {
                    byteArrayOutputStream.close();
                    inputStream.close();
                } catch (IOException unused) {
                }
                throw th;
            }
        }
        str = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        try {
            byteArrayOutputStream.close();
            inputStream.close();
        } catch (IOException unused2) {
            return str;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v11 */
    /* JADX WARN: Type inference failed for: r5v12 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:37:0x001d -> B:13:0x0047). Please report as a decompilation issue!!! */
    public String A(String str) {
        InputStream inputStream;
        String str2 = null;
        str2 = null;
        str2 = null;
        InputStream inputStream2 = null;
        try {
            try {
            } catch (Exception e10) {
                e10.printStackTrace();
                str = str;
            }
            if (str == 0) {
                LocalLog.a("StartupManager", "cannot get data from oplus settings, invalid args , filePath is null.");
                return null;
            }
            try {
                inputStream = OplusSettings.readConfig(this.f178a, (String) str, 0);
            } catch (Exception e11) {
                e = e11;
                inputStream = null;
            } catch (Throwable th) {
                th = th;
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Exception e12) {
                        e12.printStackTrace();
                    }
                }
                throw th;
            }
            try {
                str2 = z(inputStream);
                str = inputStream;
                if (inputStream != null) {
                    inputStream.close();
                    str = inputStream;
                }
            } catch (Exception e13) {
                e = e13;
                LocalLog.a("StartupManager", "getDataFromColorSettings, error:" + e.toString());
                str = inputStream;
                if (inputStream != null) {
                    inputStream.close();
                    str = inputStream;
                }
                return str2;
            }
            return str2;
        } catch (Throwable th2) {
            th = th2;
            inputStream2 = str;
        }
    }

    public void B(List<String> list) {
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
    }

    public boolean C(Map<String, List<String>> map, Map<String, List<String>> map2, boolean z10, boolean z11) {
        Log.d("StartupManager", "saveConfigToDynamicFile: start save dynamic file ");
        List<String> list = map.get("switch");
        List<String> list2 = map2.get("switch");
        boolean D = D(list, list2, map, map2, z10);
        if (D) {
            u("startup/startup_dynamic_list.xml");
            if (this.f183f) {
                P(list, z10);
                if (!z11) {
                    P(list2, !z10);
                }
            } else {
                v(list, list2, z10, z11);
            }
        }
        return D;
    }

    public void E(Map<String, List<String>> map, boolean z10) {
        C(map, x(!z10), z10, true);
    }

    public void F(String str, List<String> list) {
        OutputStream outputStream;
        OutputStreamWriter outputStreamWriter;
        if (list != null && !list.isEmpty() && str != null) {
            BufferedWriter bufferedWriter = null;
            try {
                try {
                    outputStream = OplusSettings.writeConfig(this.f178a, str, 0);
                    try {
                        outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
                        try {
                            try {
                                BufferedWriter bufferedWriter2 = new BufferedWriter(outputStreamWriter);
                                try {
                                    Iterator<String> it = list.iterator();
                                    while (it.hasNext()) {
                                        bufferedWriter2.write(it.next());
                                        bufferedWriter2.newLine();
                                    }
                                    bufferedWriter2.flush();
                                    bufferedWriter2.close();
                                    outputStreamWriter.close();
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                } catch (Exception e10) {
                                    e = e10;
                                    bufferedWriter = bufferedWriter2;
                                    LocalLog.b("StartupManager", "saveDataToColorSettings, error:" + e.toString());
                                    if (bufferedWriter != null) {
                                        bufferedWriter.close();
                                    }
                                    if (outputStreamWriter != null) {
                                        outputStreamWriter.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    bufferedWriter = bufferedWriter2;
                                    if (bufferedWriter != null) {
                                        try {
                                            bufferedWriter.close();
                                        } catch (Exception e11) {
                                            e11.printStackTrace();
                                            throw th;
                                        }
                                    }
                                    if (outputStreamWriter != null) {
                                        outputStreamWriter.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Exception e12) {
                            e = e12;
                        }
                    } catch (Exception e13) {
                        e = e13;
                        outputStreamWriter = null;
                    } catch (Throwable th3) {
                        th = th3;
                        outputStreamWriter = null;
                    }
                } catch (Exception e14) {
                    e = e14;
                    outputStream = null;
                    outputStreamWriter = null;
                } catch (Throwable th4) {
                    th = th4;
                    outputStream = null;
                    outputStreamWriter = null;
                }
            } catch (Exception e15) {
                e15.printStackTrace();
            }
        } else {
            LocalLog.l("StartupManager", "saveDataToColorSettings, invalid args, data=" + list);
        }
    }

    public void G(List<String> list, boolean z10) {
        ArrayMap arrayMap = new ArrayMap();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            arrayMap.put(it.next(), Boolean.valueOf(z10));
        }
        M(true, arrayMap, true);
        this.f179b.v().J(!z10, list, true);
    }

    public void H(String str, String str2) {
        BufferedWriter bufferedWriter = null;
        try {
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(OplusSettings.writeConfig(this.f178a, str, 0), "utf-8"));
                    try {
                        bufferedWriter2.write(str2);
                        bufferedWriter2.flush();
                        bufferedWriter2.close();
                    } catch (Exception e10) {
                        e = e10;
                        bufferedWriter = bufferedWriter2;
                        e.printStackTrace();
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        if ("startup/startup_dynamic_list.xml".equals(str)) {
                        }
                        u(str);
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (Exception e11) {
                                e11.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Exception e12) {
                    e = e12;
                }
            } catch (Exception e13) {
                e13.printStackTrace();
            }
            if (!"startup/startup_dynamic_list.xml".equals(str) || "/startup/startup_manager.xml".equals(str)) {
                u(str);
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void I(List<String> list) {
        list.add("data/oplus/os//startup/bootallow.txt");
        list.add("data/oplus/os//startup/bootoption.txt");
        list.add("data/oplus/os//startup/bootfixed.txt");
        list.add("data/oplus/os//startup/associate_startup_default_list.xml");
        list.add("data/oplus/os//startup/fixedRecord.txt");
        list.add("data/oplus/os//startup/broadcast_action_white.txt");
        list.add("data/oplus/os//startup/autoSyncWhiteList.txt");
        list.add("data/oplus/os//startup/browserWhiteList.txt");
        list.add("data/oplus/os//startup/broadcastlist.xml");
        list.add("data/oplus/os//startup/hide_white_list.xml");
    }

    public void K(Bundle bundle) {
        ArrayList<String> stringArrayList = bundle.getStringArrayList("allow_list");
        ArrayList<String> stringArrayList2 = bundle.getStringArrayList("other_allow_list");
        boolean z10 = bundle.getBoolean("autostart_check");
        boolean z11 = bundle.getBoolean("exactly_start_type_changed");
        if (stringArrayList != null && stringArrayList2 != null) {
            LocalLog.a("StartupManager", "update main proc list");
            P(stringArrayList, z10);
            if (z11) {
                return;
            }
            P(stringArrayList2, !z10);
            return;
        }
        LocalLog.a("StartupManager", " get null data from ui");
    }

    public void L(boolean z10, String str, boolean z11, boolean z12) {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(str, Boolean.valueOf(z12));
        M(z10, arrayMap, z11);
    }

    public void M(boolean z10, Map<String, Boolean> map, boolean z11) {
        boolean z12;
        Map<String, List<String>> x10 = h(this.f178a).x(z10);
        List<String> list = x10.get("userCare");
        List<String> list2 = x10.get("switch");
        List<String> list3 = x10.get("pkgName");
        loop0: while (true) {
            z12 = true;
            for (String str : map.keySet()) {
                boolean booleanValue = map.get(str).booleanValue();
                boolean z13 = false;
                if (list3.contains(str)) {
                    if (z11 && !list.contains(str)) {
                        LocalLog.a("StartupManager", "updateOption: user_care: " + str);
                        list.add(str);
                        z13 = true;
                    }
                    if (!z11 && !list.contains(str) && !booleanValue) {
                        LocalLog.a("StartupManager", "updateOption: remove: " + str);
                        list3.remove(str);
                        list2.remove(str);
                        z13 = true;
                    }
                    if (booleanValue && !list2.contains(str)) {
                        LocalLog.a("StartupManager", "updateOption: add allow: " + str);
                        list2.add(str);
                        z13 = true;
                    }
                    if (!booleanValue && list2.contains(str)) {
                        LocalLog.a("StartupManager", "updateOption: deny: " + str);
                        list2.remove(str);
                    }
                    z12 = z13;
                } else {
                    list3.add(str);
                    if (z11) {
                        LocalLog.a("StartupManager", "updateOption: user_care: " + str);
                        list.add(str);
                        z13 = true;
                    }
                    if (booleanValue) {
                        LocalLog.a("StartupManager", "updateOption: allow: " + str);
                        list2.add(str);
                    } else {
                        z12 = z13;
                    }
                }
            }
            break loop0;
        }
        if (z12) {
            h(this.f178a).E(x10, z10);
        }
    }

    public void N() {
        LocalLog.a("StartupManager", "update SPS");
        if (this.f178a == null || UserHandle.myUserId() != 0) {
            return;
        }
        if (!SPSListUtils.c(this.f178a).h()) {
            SPSListUtils.c(this.f178a).n(null);
        }
        SPSListUtils.c(this.f178a).o();
        SPSListUtils.c(this.f178a).m();
        SPSListUtils.c(this.f178a).l();
    }

    public void O(Map<String, Boolean> map, boolean z10) {
        if (map == null || map.isEmpty()) {
            return;
        }
        M(z10, map, true);
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            this.f179b.v().K(!map.get(r2).booleanValue(), it.next(), z10);
        }
    }

    public void Q(String str) {
        String e10 = e("/data/oplus/os/startup/sys_startupmanager_monitor_list.xml".equals(str) ? "sys_startupmanager_monitor_list" : "sys_ams_skipbroadcast");
        if (e10 != null) {
            H(str, e10);
            StartupDataSyncUtils.d(str);
        }
    }

    public void R(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            this.f186i = Boolean.parseBoolean(str);
        }
        if (!TextUtils.isEmpty(str2)) {
            this.f187j = Boolean.parseBoolean(str2);
        }
        SharedPreferences.Editor edit = this.f178a.getSharedPreferences("sysAppPreventStartRecordUploadConfig", 0).edit();
        edit.putBoolean("preSwitch", this.f186i);
        edit.putBoolean("releaseSwitch", this.f187j);
        edit.apply();
    }

    public void a() {
        ArrayList arrayList = new ArrayList();
        I(arrayList);
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            File file = new File(it.next());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void b(String str) {
        Map<String, Map<String, List<String>>> w10 = w();
        Map<String, List<String>> map = w10.get("1");
        Map<String, List<String>> map2 = w10.get("0");
        c(map, str, true);
        c(map2, str, false);
        C(map, map2, true, false);
    }

    public ArrayList<String> d(boolean z10) {
        ArrayList<String> arrayList = z10 ? this.f180c : this.f181d;
        synchronized (arrayList) {
            if (arrayList.isEmpty()) {
                arrayList.addAll(x(z10).get("switch"));
            }
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0042 A[Catch: Exception -> 0x0047, TRY_LEAVE, TryCatch #0 {Exception -> 0x0047, blocks: (B:16:0x002d, B:18:0x0033, B:7:0x0042), top: B:15:0x002d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String e(String str) {
        Cursor cursor;
        String string;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            cursor = this.f178a.getContentResolver().query(StartupConst.f18931a, strArr, "filtername=\"" + str + "\"", null, null);
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
                } catch (Exception unused) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    return null;
                }
            }
            string = null;
            if (cursor != null) {
            }
            return string;
        } catch (Exception unused2) {
            cursor = null;
        }
    }

    public List<String> f(boolean z10) {
        ArrayList arrayList = new ArrayList();
        Iterator<AppToShow> it = this.f179b.v().s(z10, true).iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().f20305b.trim());
        }
        return arrayList;
    }

    public List<String> g(boolean z10) {
        ArrayList<String> y4 = y(z10 ? "/startup/autostart_hide_list.txt" : "/startup/associate_hide_list.txt");
        y4.addAll(WhiteListUtils.c(this.f178a, z10));
        return y4;
    }

    public boolean i() {
        return this.f184g && this.f185h;
    }

    public Set<String> j() {
        return this.f182e;
    }

    public List<String> k(boolean z10) {
        if (z10) {
            return y("/startup/autostart_white_list.txt");
        }
        return y("/startup/associate_white_list.txt");
    }

    public void l(boolean z10) {
        this.f184g = z10;
        SharedPreferences.Editor edit = this.f178a.getSharedPreferences("check_monitor_init", 0).edit();
        edit.putBoolean("is_need_monitor", this.f184g);
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.f182e);
        edit.putStringSet("monitor_list", hashSet);
        edit.apply();
    }

    public void m() {
        LocalLog.a("StartupManager", "InitCheckListRunnable run ");
        new SysStartupManagerConfigListParser(true, this.f178a).u(false);
        LocalLog.a("StartupManager", "SysStartupV3ConfigListParser run ");
        new SysStartupV3ConfigListParser(this.f178a).u(false);
        LocalLog.a("StartupManager", "SysStartupV3ConfigListParser end ");
        new CdoGameAppsConfigListParser(this.f178a).u(false);
        a();
        LocalLog.a("StartupManager", "InitCheckListRunnable end ");
    }

    public void n(Set<String> set) {
        this.f182e.clear();
        this.f182e.addAll(set);
    }

    public void o() {
        SysStartupManagerConfigListParser sysStartupManagerConfigListParser = new SysStartupManagerConfigListParser(true, this.f178a);
        SysStartupV3ConfigListParser sysStartupV3ConfigListParser = new SysStartupV3ConfigListParser(this.f178a);
        boolean i10 = sysStartupManagerConfigListParser.i();
        boolean i11 = sysStartupV3ConfigListParser.i();
        LocalLog.a("StartupManager", "isNeedInitStartup:" + i10 + " isNeedInitStartupV3:" + i11);
        if (!s(this.f178a) || UserHandle.myUserId() != 0 || i10 || i11) {
            LocalLog.l("StartupManager", "not ota version, need init list");
            m();
        }
        if (CommonUtil.T(this.f178a)) {
            return;
        }
        StartupManager.i(this.f178a).u(w());
    }

    public boolean r() {
        if (this.f188k) {
            return this.f186i;
        }
        return this.f187j;
    }

    public boolean s(Context context) {
        try {
            OplusSystemUpdateInfo systemUpdateInfo = OplusPackageManager.getOplusPackageManager(context).getSystemUpdateInfo();
            if (systemUpdateInfo.getUpdateType() == 1) {
                if (systemUpdateInfo.isUpdateSucc()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e10) {
            Log.e("StartupManager", "" + e10.getMessage());
            return false;
        }
    }

    public Map<String, Map<String, List<String>>> w() {
        ArrayMap arrayMap;
        XmlPullParser newPullParser;
        String str;
        String str2;
        String A = A("startup/startup_dynamic_list.xml");
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        String str3 = "switch";
        arrayMap3.put("switch", arrayList);
        arrayMap3.put("userCare", arrayList2);
        String str4 = "pkgName";
        arrayMap3.put("pkgName", arrayList3);
        arrayMap4.put("switch", arrayList4);
        arrayMap4.put("userCare", arrayList5);
        arrayMap4.put("pkgName", arrayList6);
        arrayMap2.put("1", arrayMap3);
        arrayMap2.put("0", arrayMap4);
        if (A == null) {
            arrayMap2.put("emptyKey", null);
            return arrayMap2;
        }
        try {
            newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (Exception e10) {
            e = e10;
            arrayMap = arrayMap2;
        }
        if (newPullParser == null) {
            LocalLog.a("StartupManager", "xml pull parser is null.");
            return arrayMap2;
        }
        newPullParser.setInput(new StringReader(A));
        while (true) {
            int next = newPullParser.next();
            if (2 == next && "dynamic".equals(newPullParser.getName())) {
                String attributeValue = newPullParser.getAttributeValue(null, str4);
                str2 = str4;
                String attributeValue2 = newPullParser.getAttributeValue(null, "source");
                arrayMap = arrayMap2;
                try {
                    String attributeValue3 = newPullParser.getAttributeValue(null, "type");
                    String attributeValue4 = newPullParser.getAttributeValue(null, str3);
                    if (attributeValue == null || attributeValue2 == null || attributeValue3 == null || attributeValue4 == null) {
                        str = str3;
                    } else {
                        if (LocalLog.f()) {
                            StringBuilder sb2 = new StringBuilder();
                            str = str3;
                            sb2.append(" read dynamic: pkg: ");
                            sb2.append(attributeValue);
                            sb2.append(" source: ");
                            sb2.append(attributeValue2);
                            sb2.append(" type: ");
                            sb2.append(attributeValue3);
                            sb2.append(" all: ");
                            sb2.append(attributeValue4);
                            LocalLog.a("StartupManager", sb2.toString());
                        } else {
                            str = str3;
                        }
                        int parseInt = Integer.parseInt(attributeValue2.trim());
                        if ("1".equals(attributeValue3.trim())) {
                            if ("1".equals(attributeValue4.trim())) {
                                arrayList.add(attributeValue);
                            }
                            if ((parseInt & 1) == 1) {
                                arrayList2.add(attributeValue);
                            }
                            arrayList3.add(attributeValue);
                        }
                        if ("0".equals(attributeValue3.trim())) {
                            if ("1".equals(attributeValue4.trim())) {
                                arrayList4.add(attributeValue);
                            }
                            if ((parseInt & 1) == 1) {
                                arrayList5.add(attributeValue);
                            }
                            arrayList6.add(attributeValue);
                        }
                    }
                    next = next;
                } catch (Exception e11) {
                    e = e11;
                    LocalLog.c("StartupManager", "parsing failed.", e);
                    return arrayMap;
                }
            } else {
                arrayMap = arrayMap2;
                str = str3;
                str2 = str4;
            }
            if (next == 1) {
                break;
            }
            str4 = str2;
            arrayMap2 = arrayMap;
            str3 = str;
        }
        return arrayMap;
    }

    public Map<String, List<String>> x(boolean z10) {
        ArrayMap arrayMap = new ArrayMap();
        Map<String, Map<String, List<String>>> w10 = w();
        arrayMap.putAll(w10.get(z10 ? "1" : "0"));
        w10.clear();
        return arrayMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0097 A[Catch: Exception -> 0x0093, TryCatch #6 {Exception -> 0x0093, blocks: (B:51:0x008f, B:42:0x0097, B:44:0x009c), top: B:50:0x008f }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x009c A[Catch: Exception -> 0x0093, TRY_LEAVE, TryCatch #6 {Exception -> 0x0093, blocks: (B:51:0x008f, B:42:0x0097, B:44:0x009c), top: B:50:0x008f }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x008f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<String> y(String str) {
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        Exception e10;
        ArrayList<String> arrayList = new ArrayList<>();
        Context context = this.f178a;
        if (context != null && str != null) {
            BufferedReader bufferedReader2 = null;
            try {
                try {
                    inputStream = OplusSettings.readConfig(context, str, 0);
                    try {
                        inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                    } catch (Exception e11) {
                        bufferedReader = null;
                        e10 = e11;
                        inputStreamReader = null;
                    } catch (Throwable th) {
                        th = th;
                        inputStreamReader = null;
                    }
                    try {
                        bufferedReader = new BufferedReader(inputStreamReader);
                        while (true) {
                            try {
                                try {
                                    String readLine = bufferedReader.readLine();
                                    if (readLine == null) {
                                        break;
                                    }
                                    if (!TextUtils.isEmpty(readLine)) {
                                        arrayList.add(readLine.trim());
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    bufferedReader2 = bufferedReader;
                                    if (bufferedReader2 != null) {
                                        try {
                                            bufferedReader2.close();
                                        } catch (Exception e12) {
                                            e12.printStackTrace();
                                            throw th;
                                        }
                                    }
                                    if (inputStreamReader != null) {
                                        inputStreamReader.close();
                                    }
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    throw th;
                                }
                            } catch (Exception e13) {
                                e10 = e13;
                                arrayList.clear();
                                LocalLog.b("StartupManager", "getDataFromColorSettings, error:" + e10.toString());
                                if (bufferedReader != null) {
                                    bufferedReader.close();
                                }
                                if (inputStreamReader != null) {
                                    inputStreamReader.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                return arrayList;
                            }
                        }
                        bufferedReader.close();
                        inputStreamReader.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (Exception e14) {
                        e10 = e14;
                        bufferedReader = null;
                    } catch (Throwable th3) {
                        th = th3;
                        if (bufferedReader2 != null) {
                        }
                        if (inputStreamReader != null) {
                        }
                        if (inputStream != null) {
                        }
                        throw th;
                    }
                } catch (Exception e15) {
                    inputStreamReader = null;
                    bufferedReader = null;
                    e10 = e15;
                    inputStream = null;
                } catch (Throwable th4) {
                    th = th4;
                    inputStream = null;
                    inputStreamReader = null;
                }
            } catch (Exception e16) {
                e16.printStackTrace();
            }
            return arrayList;
        }
        LocalLog.l("StartupManager", "getDataFromColorSettings, invalid args, context=" + this.f178a + ", filePath=" + str);
        return arrayList;
    }
}
