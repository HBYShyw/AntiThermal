package ha;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import z5.LocalFileUtil;

/* compiled from: StorageConfigManager.java */
/* renamed from: ha.a, reason: use source file name */
/* loaded from: classes2.dex */
public class StorageConfigManager {

    /* renamed from: c, reason: collision with root package name */
    private static long f12026c;

    /* renamed from: d, reason: collision with root package name */
    private static Context f12027d;

    /* renamed from: a, reason: collision with root package name */
    private static final boolean f12024a = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* renamed from: b, reason: collision with root package name */
    private static final Uri f12025b = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* renamed from: e, reason: collision with root package name */
    private static boolean f12028e = true;

    private static boolean a(InputStream inputStream, InputStream inputStream2) {
        boolean z10 = f(inputStream2) > f(inputStream);
        if (z10) {
            Log.i("StorageConfigManager", "update config is newer than current existed config");
        }
        return z10;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0042 A[Catch: Exception -> 0x0047, TRY_LEAVE, TryCatch #0 {Exception -> 0x0047, blocks: (B:16:0x002d, B:18:0x0033, B:7:0x0042), top: B:15:0x002d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String b(String str) {
        Cursor cursor;
        String string;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            cursor = f12027d.getContentResolver().query(f12025b, strArr, "filtername=\"" + str + "\"", null, null);
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

    private static long c() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long blockCountLong = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        if (blockCountLong > 274877906944L) {
            return 512L;
        }
        if (blockCountLong > 137438953472L) {
            return 256L;
        }
        if (blockCountLong > 68719476736L) {
            return 128L;
        }
        if (blockCountLong > 34359738368L) {
            return 64L;
        }
        return blockCountLong > 17179869184L ? 32L : 16L;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x004f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String d() {
        BufferedReader bufferedReader;
        IOException e10;
        StringBuilder sb2 = new StringBuilder();
        BufferedReader bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(f12027d.getAssets().open("sys_flash_threshold_default_config.xml"), "UTF-8"));
                while (true) {
                    try {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb2.append(readLine);
                            sb2.append("\n");
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader2 = bufferedReader;
                            if (bufferedReader2 != null) {
                                try {
                                    bufferedReader2.close();
                                } catch (IOException e11) {
                                    e11.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (IOException e12) {
                        e10 = e12;
                        e10.printStackTrace();
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        return sb2.toString();
                    }
                }
                bufferedReader.close();
            } catch (IOException e13) {
                bufferedReader = null;
                e10 = e13;
            } catch (Throwable th2) {
                th = th2;
                if (bufferedReader2 != null) {
                }
                throw th;
            }
        } catch (IOException e14) {
            e14.printStackTrace();
        }
        return sb2.toString();
    }

    public static synchronized ArrayList<Long> e(Context context) {
        ArrayList<Long> g6;
        synchronized (StorageConfigManager.class) {
            f12027d = context;
            g6 = g();
        }
        return g6;
    }

    private static int f(InputStream inputStream) {
        int next;
        try {
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setInput(inputStream, null);
            do {
                next = newPullParser.next();
                if (next == 2 && "version".equals(newPullParser.getName())) {
                    String nextText = newPullParser.nextText();
                    if (!TextUtils.isEmpty(nextText)) {
                        return Integer.parseInt(nextText);
                    }
                }
            } while (next != 1);
            return 0;
        } catch (Exception e10) {
            Log.e("StorageConfigManager", "failed parsing storage config file ", e10);
            return 0;
        }
    }

    private static ArrayList<Long> g() {
        InputStream a10 = LocalFileUtil.a("flash_threshold" + File.separator + "sys_flash_threshold_config.xml", f12027d);
        try {
            try {
                if (a10 == null) {
                    Log.e("StorageConfigManager", "no default and rus config");
                    if (a10 != null) {
                        try {
                            a10.close();
                        } catch (Exception e10) {
                            e10.printStackTrace();
                        }
                    }
                    Log.v("StorageConfigManager", "initConfigFromLocal DONE");
                    return null;
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(a10);
                f12026c = c();
                ArrayList<Long> h10 = h(bufferedInputStream);
                bufferedInputStream.close();
                try {
                    a10.close();
                    Log.v("StorageConfigManager", "initConfigFromLocal DONE");
                } catch (Exception e11) {
                    e11.printStackTrace();
                }
                return h10;
            } catch (Throwable th) {
                if (a10 != null) {
                    try {
                        a10.close();
                    } catch (Exception e12) {
                        e12.printStackTrace();
                        throw th;
                    }
                }
                Log.v("StorageConfigManager", "initConfigFromLocal DONE");
                throw th;
            }
        } catch (IOException e13) {
            Log.e("StorageConfigManager", "config error: " + e13.getMessage());
            if (a10 != null) {
                try {
                    a10.close();
                } catch (Exception e14) {
                    e14.printStackTrace();
                    return null;
                }
            }
            Log.v("StorageConfigManager", "initConfigFromLocal DONE");
            return null;
        }
    }

    private static synchronized ArrayList<Long> h(InputStream inputStream) {
        synchronized (StorageConfigManager.class) {
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setExpandEntityReferences(false);
            Document document = null;
            ArrayList<Long> arrayList = new ArrayList<>();
            try {
                try {
                    try {
                        document = newInstance.newDocumentBuilder().parse(inputStream);
                    } catch (SAXException e10) {
                        e10.printStackTrace();
                    }
                } catch (ParserConfigurationException e11) {
                    e11.printStackTrace();
                }
            } catch (IOException e12) {
                e12.printStackTrace();
            }
            if (document != null) {
                NodeList childNodes = document.getDocumentElement().getChildNodes();
                for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
                    Node item = childNodes.item(i10);
                    if ("isOpen".equals(item.getNodeName())) {
                        int parseInt = Integer.parseInt(item.getTextContent().toString());
                        if (parseInt == 0) {
                            Log.i("StorageConfigManager", "close feature use default value");
                            f12028e = false;
                            return arrayList;
                        }
                        if (parseInt == 1) {
                            f12028e = true;
                        }
                    }
                    if ("flash".equals(item.getNodeName()) && item.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase(ThermalControlConfig.CONFIG_TYPE_DEFAULT)) {
                        NodeList childNodes2 = item.getChildNodes();
                        for (int i11 = 0; i11 < childNodes2.getLength(); i11++) {
                            Node item2 = childNodes2.item(i11);
                            if ("param".equals(item2.getNodeName())) {
                                Node namedItem = item2.getAttributes().getNamedItem("size");
                                Node namedItem2 = item2.getAttributes().getNamedItem(ThermalBaseConfig.Item.ATTR_VALUE);
                                if (namedItem != null && namedItem2 != null) {
                                    long parseLong = Long.parseLong(namedItem.getNodeValue());
                                    long parseLong2 = Long.parseLong(namedItem2.getNodeValue());
                                    if (parseLong == f12026c) {
                                        Log.i("StorageConfigManager", "find the correct pairs  " + f12026c + " " + parseLong2);
                                        arrayList.add(Long.valueOf(parseLong2));
                                    }
                                }
                                Node namedItem3 = item2.getAttributes().getNamedItem("size_low");
                                Node namedItem4 = item2.getAttributes().getNamedItem("value_low");
                                if (namedItem3 != null && namedItem4 != null) {
                                    long parseLong3 = Long.parseLong(namedItem3.getNodeValue());
                                    long parseLong4 = Long.parseLong(namedItem4.getNodeValue());
                                    if (parseLong3 == f12026c) {
                                        Log.i("StorageConfigManager", "find the correct low pairs  " + f12026c + " " + parseLong4);
                                        arrayList.add(Long.valueOf(parseLong4));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            arrayList.add(Long.valueOf(f12026c));
            if (arrayList.size() == 3) {
                Log.i("StorageConfigManager", "find the correct pairs " + f12026c + " " + arrayList.get(0) + " " + arrayList.get(1));
            } else {
                Log.i("StorageConfigManager", "can not find the correct pairs use default value");
            }
            return arrayList;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v15 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v21, types: [java.io.ByteArrayInputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v33 */
    /* JADX WARN: Type inference failed for: r1v34 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v27, types: [z5.b] */
    /* JADX WARN: Type inference failed for: r2v32, types: [z5.b] */
    /* JADX WARN: Type inference failed for: r5v23, types: [boolean] */
    /* JADX WARN: Type inference failed for: r5v25, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r5v26, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r5v27, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r7v42, types: [z5.b] */
    public static synchronized void i(Context context, boolean z10) {
        String str;
        String str2;
        String b10;
        String str3;
        String str4;
        ByteArrayInputStream byteArrayInputStream;
        synchronized (StorageConfigManager.class) {
            f12027d = context;
            ?? d10 = d();
            InputStream a10 = LocalFileUtil.a("flash_threshold" + File.separator + "sys_flash_threshold_config.xml", context);
            f12026c = c();
            if (a10 == null) {
                Log.i("StorageConfigManager", "config file is not exists save default apk file");
                LocalFileUtil.c().m("flash_threshold", "sys_flash_threshold_config.xml", d10, f12027d);
                return;
            }
            InputStream inputStream = null;
            inputStream = null;
            inputStream = null;
            inputStream = null;
            inputStream = null;
            inputStream = null;
            inputStream = null;
            try {
                try {
                    if (z10) {
                        try {
                            a10 = new ByteArrayInputStream(d10.getBytes("UTF-8"));
                            try {
                                b10 = b("sys_flash_threshold_config");
                            } catch (UnsupportedEncodingException e10) {
                                e = e10;
                                d10 = 0;
                            } catch (IOException e11) {
                                e = e11;
                                d10 = 0;
                            } catch (Throwable th) {
                                th = th;
                                d10 = 0;
                            }
                        } catch (UnsupportedEncodingException e12) {
                            e = e12;
                            d10 = 0;
                        } catch (IOException e13) {
                            e = e13;
                            d10 = 0;
                        } catch (Throwable th2) {
                            th = th2;
                            d10 = 0;
                        }
                        if (b10 == null) {
                            try {
                                a10.close();
                            } catch (Exception e14) {
                                Log.e("StorageConfigManager", "failed to close state fileInputStream " + e14.toString());
                            }
                            return;
                        }
                        d10 = new ByteArrayInputStream(b10.getBytes("UTF-8"));
                        try {
                            d10.mark(d10.available());
                            if (a(a10, d10)) {
                                d10.reset();
                                ArrayList<Long> h10 = h(d10);
                                if (h10 != null && h10.size() == 3) {
                                    if (StorageUtils.n(h10.get(0), h10.get(1))) {
                                        Log.i("StorageConfigManager", "save new RUS file");
                                        LocalFileUtil.c().m("flash_threshold", "sys_flash_threshold_config.xml", b10, f12027d);
                                    }
                                } else if (h10.isEmpty() && !f12028e) {
                                    Log.i("StorageConfigManager", "close feature use default data");
                                    LocalFileUtil.c().m("flash_threshold", "sys_flash_threshold_config.xml", b10, f12027d);
                                } else {
                                    Log.i("StorageConfigManager", "parse error");
                                }
                                j();
                            }
                        } catch (UnsupportedEncodingException e15) {
                            e = e15;
                            inputStream = a10;
                            d10 = d10;
                            e.printStackTrace();
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Exception e16) {
                                    str = "StorageConfigManager";
                                    str2 = "failed to close state fileInputStream " + e16.toString();
                                    Log.e(str, str2);
                                    return;
                                }
                            }
                            if (d10 != 0) {
                                d10.close();
                            }
                            return;
                        } catch (IOException e17) {
                            e = e17;
                            inputStream = a10;
                            d10 = d10;
                            e.printStackTrace();
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Exception e18) {
                                    str = "StorageConfigManager";
                                    str2 = "failed to close state fileInputStream " + e18.toString();
                                    Log.e(str, str2);
                                    return;
                                }
                            }
                            if (d10 != 0) {
                                d10.close();
                            }
                            return;
                        } catch (Throwable th3) {
                            th = th3;
                            inputStream = a10;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Exception e19) {
                                    Log.e("StorageConfigManager", "failed to close state fileInputStream " + e19.toString());
                                    throw th;
                                }
                            }
                            if (d10 != 0) {
                                d10.close();
                            }
                            throw th;
                        }
                        try {
                            a10.close();
                            d10.close();
                        } catch (Exception e20) {
                            str = "StorageConfigManager";
                            str2 = "failed to close state fileInputStream " + e20.toString();
                            Log.e(str, str2);
                            return;
                        }
                        return;
                    }
                    try {
                        byteArrayInputStream = new ByteArrayInputStream(d10.getBytes("UTF-8"));
                        try {
                            byteArrayInputStream.mark(byteArrayInputStream.available());
                            ?? a11 = a(a10, byteArrayInputStream);
                            inputStream = a11;
                            if (a11 != 0) {
                                byteArrayInputStream.reset();
                                ?? h11 = h(byteArrayInputStream);
                                if (h11 != 0 && h11.size() == 3) {
                                    boolean n10 = StorageUtils.n((Long) h11.get(0), (Long) h11.get(1));
                                    inputStream = h11;
                                    if (n10) {
                                        Log.i("StorageConfigManager", "save new apk local file");
                                        ?? c10 = LocalFileUtil.c();
                                        ?? r52 = f12027d;
                                        c10.m("flash_threshold", "sys_flash_threshold_config.xml", d10, r52);
                                        inputStream = r52;
                                    }
                                } else if (h11.isEmpty() && !f12028e) {
                                    Log.i("StorageConfigManager", "close feature use default data");
                                    ?? c11 = LocalFileUtil.c();
                                    ?? r53 = f12027d;
                                    c11.m("flash_threshold", "sys_flash_threshold_config.xml", d10, r53);
                                    inputStream = r53;
                                } else {
                                    Log.i("StorageConfigManager", "parse error");
                                    inputStream = h11;
                                }
                            }
                        } catch (UnsupportedEncodingException e21) {
                            e = e21;
                            inputStream = byteArrayInputStream;
                            e.printStackTrace();
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Exception e22) {
                                    str3 = "StorageConfigManager";
                                    str4 = "failed to close state fileInputStream " + e22.toString();
                                    Log.e(str3, str4);
                                    return;
                                }
                            }
                            a10.close();
                            return;
                        } catch (IOException e23) {
                            e = e23;
                            inputStream = byteArrayInputStream;
                            e.printStackTrace();
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Exception e24) {
                                    str3 = "StorageConfigManager";
                                    str4 = "failed to close state fileInputStream " + e24.toString();
                                    Log.e(str3, str4);
                                    return;
                                }
                            }
                            a10.close();
                            return;
                        } catch (Throwable th4) {
                            th = th4;
                            inputStream = byteArrayInputStream;
                        }
                    } catch (UnsupportedEncodingException e25) {
                        e = e25;
                    } catch (IOException e26) {
                        e = e26;
                    }
                    try {
                        byteArrayInputStream.close();
                        a10.close();
                    } catch (Exception e27) {
                        str3 = "StorageConfigManager";
                        str4 = "failed to close state fileInputStream " + e27.toString();
                        Log.e(str3, str4);
                        return;
                    }
                    return;
                } catch (Throwable th5) {
                    th = th5;
                }
                th = th5;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e28) {
                        Log.e("StorageConfigManager", "failed to close state fileInputStream " + e28.toString());
                    }
                }
                a10.close();
                throw th;
            } catch (Throwable th6) {
                th = th6;
            }
        }
    }

    private static void j() {
        StorageUtils.g(f12027d);
        StorageMonitorService.Y(f12027d).L0(StorageUtils.f());
        StorageMonitorService.Y(f12027d).K0(StorageUtils.e());
        Log.i("StorageConfigManager", "DataLowThreshold = " + StorageUtils.b(StorageUtils.f()));
        Log.i("StorageConfigManager", "DataFullThreshold = " + StorageUtils.b(StorageUtils.e()));
    }
}
