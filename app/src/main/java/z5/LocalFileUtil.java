package z5;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import b6.LocalLog;
import b9.PowerUtils;
import com.oplus.settings.OplusBaseSettings;
import com.oplus.settings.OplusSettings;
import f6.f;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import w5.OplusBatteryConstants;

/* compiled from: LocalFileUtil.java */
/* renamed from: z5.b, reason: use source file name */
/* loaded from: classes.dex */
public class LocalFileUtil {

    /* renamed from: a, reason: collision with root package name */
    private static LocalFileUtil f20241a;

    private LocalFileUtil() {
    }

    public static InputStream a(String str, Context context) {
        try {
            return OplusSettings.readConfig(context, str, 0);
        } catch (Throwable th) {
            LocalLog.d("LocalFileUtil", "Fail to read file from Provider e=" + th);
            return null;
        }
    }

    public static InputStream b(String str, Context context, int i10) {
        try {
            return OplusBaseSettings.readConfigAsUser(context, str, i10, 0);
        } catch (Throwable th) {
            LocalLog.d("LocalFileUtil", "Fail to read file from Provider e=" + th);
            return null;
        }
    }

    public static synchronized LocalFileUtil c() {
        LocalFileUtil localFileUtil;
        synchronized (LocalFileUtil.class) {
            if (f20241a == null) {
                f20241a = new LocalFileUtil();
            }
            localFileUtil = f20241a;
        }
        return localFileUtil;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0041, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0042, code lost:
    
        r0 = new java.lang.StringBuilder();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static List<String> d(InputStream inputStream) {
        StringBuilder sb2;
        int next;
        String attributeValue;
        ArrayList arrayList = new ArrayList();
        try {
            if (inputStream == null) {
                Log.e("LocalFileUtil", "getListFromStream stream is null");
                return arrayList;
            }
            try {
                try {
                    try {
                        XmlPullParser newPullParser = Xml.newPullParser();
                        newPullParser.setInput(inputStream, null);
                        do {
                            next = newPullParser.next();
                            if (next == 2 && "p".equals(newPullParser.getName()) && (attributeValue = newPullParser.getAttributeValue(null, "att")) != null) {
                                arrayList.add(attributeValue);
                            }
                        } while (next != 1);
                        inputStream.close();
                    } catch (IOException e10) {
                        LocalLog.d("LocalFileUtil", "failed IOException " + e10);
                        try {
                            inputStream.close();
                        } catch (IOException e11) {
                            e = e11;
                            sb2 = new StringBuilder();
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.d("LocalFileUtil", sb2.toString());
                            return arrayList;
                        }
                    } catch (NullPointerException e12) {
                        LocalLog.d("LocalFileUtil", "failed parsing " + e12);
                        try {
                            inputStream.close();
                        } catch (IOException e13) {
                            e = e13;
                            sb2 = new StringBuilder();
                            sb2.append("Failed to close state FileInputStream ");
                            sb2.append(e);
                            LocalLog.d("LocalFileUtil", sb2.toString());
                            return arrayList;
                        }
                    }
                } catch (IndexOutOfBoundsException e14) {
                    LocalLog.d("LocalFileUtil", "failed parsing " + e14);
                    try {
                        inputStream.close();
                    } catch (IOException e15) {
                        e = e15;
                        sb2 = new StringBuilder();
                        sb2.append("Failed to close state FileInputStream ");
                        sb2.append(e);
                        LocalLog.d("LocalFileUtil", sb2.toString());
                        return arrayList;
                    }
                } catch (NumberFormatException e16) {
                    LocalLog.d("LocalFileUtil", "failed parsing " + e16);
                    try {
                        inputStream.close();
                    } catch (IOException e17) {
                        e = e17;
                        sb2 = new StringBuilder();
                        sb2.append("Failed to close state FileInputStream ");
                        sb2.append(e);
                        LocalLog.d("LocalFileUtil", sb2.toString());
                        return arrayList;
                    }
                }
            } catch (XmlPullParserException e18) {
                LocalLog.d("LocalFileUtil", "failed parsing " + e18);
                try {
                    inputStream.close();
                } catch (IOException e19) {
                    e = e19;
                    sb2 = new StringBuilder();
                    sb2.append("Failed to close state FileInputStream ");
                    sb2.append(e);
                    LocalLog.d("LocalFileUtil", sb2.toString());
                    return arrayList;
                }
            }
            return arrayList;
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (IOException e20) {
                LocalLog.d("LocalFileUtil", "Failed to close state FileInputStream " + e20);
            }
            throw th;
        }
    }

    public static OutputStream e(String str, Context context) {
        try {
            return OplusSettings.writeConfig(context, str, 0);
        } catch (Throwable th) {
            LocalLog.d("LocalFileUtil", "Fail to get output stream from colorProvider e=" + th);
            return null;
        }
    }

    public static OutputStream f(String str, Context context, int i10) {
        try {
            return OplusBaseSettings.writeConfigAsUser(context, str, i10, 0);
        } catch (Throwable th) {
            LocalLog.d("LocalFileUtil", "Fail to get output stream from colorProvider e=" + th);
            return null;
        }
    }

    private boolean h(String str, String str2) {
        File file = new File(str);
        if (!file.exists()) {
            try {
                if (!file.mkdirs()) {
                    LocalLog.a("LocalFileUtil", "initFile: failed create dir = " + str);
                } else {
                    LocalLog.d("LocalFileUtil", "initFile: create dir = " + str);
                }
            } catch (Exception e10) {
                LocalLog.d("LocalFileUtil", "failed create dir " + e10);
            }
        }
        boolean z10 = false;
        File file2 = new File(str, str2);
        if (!file2.exists()) {
            try {
                if (!file2.createNewFile()) {
                    LocalLog.a("LocalFileUtil", "initFile: file.createNewFile() failed");
                } else {
                    z10 = true;
                    LocalLog.d("LocalFileUtil", "initFile: create file = " + str2);
                }
            } catch (IOException e11) {
                LocalLog.d("LocalFileUtil", "failed create file " + e11);
            }
        }
        return z10;
    }

    private static void n(OutputStream outputStream, String str) {
        StringBuilder sb2;
        OutputStreamWriter outputStreamWriter;
        if (outputStream == null) {
            LocalLog.d("LocalFileUtil", "Fail to writeFileToProvider");
            return;
        }
        OutputStreamWriter outputStreamWriter2 = null;
        try {
            outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");
        } catch (Throwable th) {
            th = th;
        }
        try {
            outputStreamWriter.write(str);
            outputStreamWriter.flush();
            try {
                outputStreamWriter.close();
            } catch (IOException e10) {
                e = e10;
                sb2 = new StringBuilder();
                sb2.append("Failed close stream ");
                sb2.append(e);
                LocalLog.d("LocalFileUtil", sb2.toString());
            }
        } catch (Throwable th2) {
            th = th2;
            outputStreamWriter2 = outputStreamWriter;
            try {
                LocalLog.d("LocalFileUtil", "Fail to write file to colorProvider e=" + th);
                if (outputStreamWriter2 != null) {
                    try {
                        outputStreamWriter2.close();
                    } catch (IOException e11) {
                        e = e11;
                        sb2 = new StringBuilder();
                        sb2.append("Failed close stream ");
                        sb2.append(e);
                        LocalLog.d("LocalFileUtil", sb2.toString());
                    }
                }
            } catch (Throwable th3) {
                if (outputStreamWriter2 != null) {
                    try {
                        outputStreamWriter2.close();
                    } catch (IOException e12) {
                        LocalLog.d("LocalFileUtil", "Failed close stream " + e12);
                    }
                }
                throw th3;
            }
        }
    }

    private static void o(OutputStream outputStream, List<String> list) {
        StringBuilder sb2;
        if (outputStream == null) {
            return;
        }
        try {
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(outputStream, "UTF-8");
            newSerializer.startDocument(null, Boolean.TRUE);
            newSerializer.startTag(null, "gs");
            for (int i10 = 0; i10 < list.size(); i10++) {
                String str = list.get(i10);
                if (str != null) {
                    newSerializer.startTag(null, "p");
                    newSerializer.attribute(null, "att", str);
                    newSerializer.endTag(null, "p");
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
                LocalLog.d("LocalFileUtil", sb2.toString());
            }
        } catch (Throwable th) {
            try {
                LocalLog.d("LocalFileUtil", "Fail to write list to colorProvider e=" + th);
                try {
                    outputStream.close();
                } catch (IOException e11) {
                    e = e11;
                    sb2 = new StringBuilder();
                    sb2.append("Failed to close stream ");
                    sb2.append(e);
                    LocalLog.d("LocalFileUtil", sb2.toString());
                }
            } catch (Throwable th2) {
                try {
                    outputStream.close();
                } catch (IOException e12) {
                    LocalLog.d("LocalFileUtil", "Failed to close stream " + e12);
                }
                throw th2;
            }
        }
    }

    private void p(File file, String str) {
        StringBuilder sb2;
        OutputStreamWriter outputStreamWriter;
        if (file == null) {
            return;
        }
        OutputStreamWriter outputStreamWriter2 = null;
        try {
            try {
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
                try {
                    outputStreamWriter.write(str);
                    outputStreamWriter.flush();
                } catch (IOException e10) {
                    e = e10;
                    outputStreamWriter2 = outputStreamWriter;
                    LocalLog.d("LocalFileUtil", "failed write file " + e);
                    if (outputStreamWriter2 != null) {
                        try {
                            outputStreamWriter2.close();
                        } catch (IOException e11) {
                            e = e11;
                            sb2 = new StringBuilder();
                            sb2.append("failed close stream ");
                            sb2.append(e);
                            LocalLog.d("LocalFileUtil", sb2.toString());
                        }
                    }
                } catch (IllegalArgumentException e12) {
                    e = e12;
                    outputStreamWriter2 = outputStreamWriter;
                    LocalLog.d("LocalFileUtil", "failed write file " + e);
                    if (outputStreamWriter2 != null) {
                        try {
                            outputStreamWriter2.close();
                        } catch (IOException e13) {
                            e = e13;
                            sb2 = new StringBuilder();
                            sb2.append("failed close stream ");
                            sb2.append(e);
                            LocalLog.d("LocalFileUtil", sb2.toString());
                        }
                    }
                } catch (IllegalStateException e14) {
                    e = e14;
                    outputStreamWriter2 = outputStreamWriter;
                    LocalLog.d("LocalFileUtil", "failed write file " + e);
                    if (outputStreamWriter2 != null) {
                        try {
                            outputStreamWriter2.close();
                        } catch (IOException e15) {
                            e = e15;
                            sb2 = new StringBuilder();
                            sb2.append("failed close stream ");
                            sb2.append(e);
                            LocalLog.d("LocalFileUtil", sb2.toString());
                        }
                    }
                } catch (Exception e16) {
                    e = e16;
                    outputStreamWriter2 = outputStreamWriter;
                    LocalLog.d("LocalFileUtil", "failed write file " + e);
                    if (outputStreamWriter2 != null) {
                        try {
                            outputStreamWriter2.close();
                        } catch (IOException e17) {
                            e = e17;
                            sb2 = new StringBuilder();
                            sb2.append("failed close stream ");
                            sb2.append(e);
                            LocalLog.d("LocalFileUtil", sb2.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    outputStreamWriter2 = outputStreamWriter;
                    if (outputStreamWriter2 != null) {
                        try {
                            outputStreamWriter2.close();
                        } catch (IOException e18) {
                            LocalLog.d("LocalFileUtil", "failed close stream " + e18);
                        }
                    }
                    throw th;
                }
            } catch (IOException e19) {
                e = e19;
            } catch (IllegalArgumentException e20) {
                e = e20;
            } catch (IllegalStateException e21) {
                e = e21;
            } catch (Exception e22) {
                e = e22;
            }
            try {
                outputStreamWriter.close();
            } catch (IOException e23) {
                e = e23;
                sb2 = new StringBuilder();
                sb2.append("failed close stream ");
                sb2.append(e);
                LocalLog.d("LocalFileUtil", sb2.toString());
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void q(OutputStream outputStream) {
        try {
            XmlSerializer newSerializer = Xml.newSerializer();
            newSerializer.setOutput(outputStream, "utf-8");
            newSerializer.startDocument(null, Boolean.TRUE);
            newSerializer.startTag(null, "filter-conf");
            newSerializer.startTag(null, "version");
            newSerializer.text("2019111510");
            newSerializer.endTag(null, "version");
            newSerializer.startTag(null, "filter-name");
            newSerializer.text("power_surveillance");
            newSerializer.endTag(null, "filter-name");
            newSerializer.startTag(null, "UpdateStatsInternalInMinute");
            newSerializer.text("30");
            newSerializer.endTag(null, "UpdateStatsInternalInMinute");
            newSerializer.startTag(null, "MaxPackageNumToReport");
            newSerializer.text("5");
            newSerializer.endTag(null, "MaxPackageNumToReport");
            newSerializer.endTag(null, "filter-conf");
            newSerializer.endDocument();
        } catch (IOException e10) {
            LocalLog.d("LocalFileUtil", "Failed to writeSurrveillanceConfig: " + e10);
        }
    }

    public void g() {
        String str = OplusBatteryConstants.f19349a;
        File file = new File(str);
        if (!file.exists() && !file.mkdir()) {
            LocalLog.a("LocalFileUtil", "init: xmlDir.mkdir failed");
        }
        h(str, "startinfo_user_not_restrict_thistime.xml");
        h(str, "guardelf_config.xml");
        h("/data/oplus/os/startup", "startup_manager.xml");
        h("battery", "sys_ams_processfilter_list.xml");
        h("/data/oplus/os/startup", "sys_startupmanager_monitor_list.xml");
        h("/data/oplus/os/config", "adb_installer_status.xml");
        h("/data/oplus/os/config", "crashclear_white_list.xml");
        h("/data/oplus/os/config", "third_app_dex_list.xml");
        h("/data/oplus/os/config", "sys_pms_defaultpackage_list.xml");
        h("/data/oplus/os/config", "sys_wms_intercept_window.xml");
        h("/data/oplus/os/config", "sys_ams_skipbroadcast.xml");
        h("/data/oplus/os/startup", "sys_rom_black_list.xml");
        h("/data/oplus/os/config", "systemConfigList.xml");
        h(str, "cpu_abnormal_list.xml");
        h("/data/oplus/os/config", "sys_wms_split_app.xml");
        h("/data/oplus/os/freeform", "sys_freeform_config.xml");
        h(str, "app_traffic_deepsleep.xml");
        h(str, "app_traffic_deepsleep_start_time.xml");
        h(str, "app_traffic_autostop_deepsleep.xml");
    }

    public void i(Context context) {
        if (y5.b.I()) {
            ArrayList arrayList = new ArrayList();
            f.l(arrayList, new ArrayList(), context);
            PowerUtils.p(arrayList);
        }
    }

    public void j(Context context) {
        OutputStream e10 = e("battery" + File.separator + "power_surveillance.xml", context);
        if (e10 == null) {
            LocalLog.d("LocalFileUtil", "Fail to saveParametersToConfigFile");
        } else {
            q(e10);
        }
    }

    public List<String> k(String str, String str2, Context context) {
        return d(a(str + File.separator + str2, context));
    }

    public void l(String str, String str2, List<String> list, Context context) {
        if (str == null || str2 == null || list == null) {
            return;
        }
        synchronized (f20241a) {
            o(e(str + File.separator + str2, context), list);
        }
    }

    public void m(String str, String str2, String str3, Context context) {
        if (str == null || str2 == null || str3 == null || context == null) {
            return;
        }
        n(e(str + File.separator + str2, context), str3);
        File file = new File(str, str2);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    LocalLog.d("LocalFileUtil", "saveStrToFile: failed create file " + str + "/" + str2);
                }
            } catch (IOException e10) {
                LocalLog.d("LocalFileUtil", "failed create file " + e10);
            }
        }
        synchronized (f20241a) {
            p(file, str3);
        }
    }
}
