package g7;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.text.TextUtils;
import android.util.Xml;
import b6.LocalLog;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.util.OplusCommonConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: OGuardRusHelper.java */
/* renamed from: g7.b, reason: use source file name */
/* loaded from: classes.dex */
public class OGuardRusHelper {

    /* renamed from: g, reason: collision with root package name */
    private static final Uri f11597g = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* renamed from: h, reason: collision with root package name */
    private static volatile OGuardRusHelper f11598h;

    /* renamed from: b, reason: collision with root package name */
    private Context f11600b;

    /* renamed from: c, reason: collision with root package name */
    private a f11601c;

    /* renamed from: a, reason: collision with root package name */
    private final Object f11599a = new Object();

    /* renamed from: d, reason: collision with root package name */
    private FileObserver f11602d = null;

    /* renamed from: e, reason: collision with root package name */
    private FileObserver f11603e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f11604f = "";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: OGuardRusHelper.java */
    /* renamed from: g7.b$a */
    /* loaded from: classes.dex */
    public class a {
        a() {
            g();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String d(Context context, String str) {
            if (context != null) {
                InputStream inputStream = null;
                try {
                    try {
                        inputStream = context.getAssets().open(str);
                        String str2 = (String) new BufferedReader(new InputStreamReader(inputStream, "utf-8")).lines().collect(Collectors.joining("\n"));
                        if (inputStream == null) {
                            return str2;
                        }
                        try {
                            inputStream.close();
                            return str2;
                        } catch (Exception unused) {
                            LocalLog.b("OGuardRusHelper", "close asset xml inputStream failed");
                            return str2;
                        }
                    } catch (Throwable th) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Exception unused2) {
                                LocalLog.b("OGuardRusHelper", "close asset xml inputStream failed");
                            }
                        }
                        throw th;
                    }
                } catch (IOException e10) {
                    e10.printStackTrace();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception unused3) {
                            LocalLog.b("OGuardRusHelper", "close asset xml inputStream failed");
                        }
                    }
                }
            }
            return "";
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:10:0x009b, code lost:
        
            if (r8 == null) goto L60;
         */
        /* JADX WARN: Code restructure failed: missing block: B:11:0x00a4, code lost:
        
            if (r7 != 0) goto L72;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x007f, code lost:
        
            r7.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0083, code lost:
        
            b6.LocalLog.b("OGuardRusHelper", "getValueForTag failed");
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x009d, code lost:
        
            r8.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x00a1, code lost:
        
            b6.LocalLog.b("OGuardRusHelper", "getValueForTag failed");
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x004d, code lost:
        
            r3 = r6.next();
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0052, code lost:
        
            if (r3 != 2) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x005c, code lost:
        
            if (r9.equals(r6.getName()) == false) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x0062, code lost:
        
            r2 = r6.nextText();
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x0065, code lost:
        
            if (r3 != 1) goto L83;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x006d, code lost:
        
            r6 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x006e, code lost:
        
            r5 = r8;
            r8 = r6;
            r6 = r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x007d, code lost:
        
            if (r7 == 0) goto L62;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x0068, code lost:
        
            r6 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x0069, code lost:
        
            r5 = r8;
            r8 = r6;
            r6 = r5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x0093, code lost:
        
            r7.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
        
            throw r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0097, code lost:
        
            b6.LocalLog.b("OGuardRusHelper", "getValueForTag failed");
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x009a, code lost:
        
            throw r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:?, code lost:
        
            throw r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x008a, code lost:
        
            r6.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:58:0x008e, code lost:
        
            b6.LocalLog.b("OGuardRusHelper", "getValueForTag failed");
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x004b, code lost:
        
            if (r6 == 0) goto L56;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0076 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:48:0x0093 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:54:? A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:55:0x008a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r6v17 */
        /* JADX WARN: Type inference failed for: r6v19 */
        /* JADX WARN: Type inference failed for: r6v6, types: [org.xmlpull.v1.XmlPullParser] */
        /* JADX WARN: Type inference failed for: r7v0, types: [java.io.File] */
        /* JADX WARN: Type inference failed for: r7v10 */
        /* JADX WARN: Type inference failed for: r7v13 */
        /* JADX WARN: Type inference failed for: r7v15 */
        /* JADX WARN: Type inference failed for: r7v16, types: [org.xmlpull.v1.XmlPullParser] */
        /* JADX WARN: Type inference failed for: r7v19 */
        /* JADX WARN: Type inference failed for: r7v2 */
        /* JADX WARN: Type inference failed for: r7v4 */
        /* JADX WARN: Type inference failed for: r7v5, types: [java.io.StringReader] */
        /* JADX WARN: Type inference failed for: r7v6 */
        /* JADX WARN: Type inference failed for: r7v7, types: [java.io.StringReader] */
        /* JADX WARN: Type inference failed for: r7v8 */
        /* JADX WARN: Type inference failed for: r7v9, types: [java.io.StringReader, java.io.Reader] */
        /* JADX WARN: Type inference failed for: r8v9, types: [org.xmlpull.v1.XmlPullParser] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String f(File file, String str, String str2) {
            Throwable th;
            Exception e10;
            FileInputStream fileInputStream;
            FileInputStream fileInputStream2;
            Object obj;
            FileInputStream fileInputStream3 = null;
            fileInputStream3 = null;
            ?? r62 = 0;
            fileInputStream3 = null;
            fileInputStream3 = null;
            String str3 = "";
            try {
                try {
                    if (file != 0) {
                        fileInputStream = new FileInputStream((File) file);
                        try {
                            ?? newPullParser = Xml.newPullParser();
                            newPullParser.setInput(fileInputStream, null);
                            fileInputStream2 = newPullParser;
                            obj = null;
                        } catch (Exception e11) {
                            file = 0;
                            fileInputStream3 = fileInputStream;
                            e10 = e11;
                            e10.printStackTrace();
                            if (fileInputStream3 != null) {
                            }
                        } catch (Throwable th2) {
                            file = 0;
                            fileInputStream3 = fileInputStream;
                            th = th2;
                            if (fileInputStream3 != null) {
                            }
                            if (file == 0) {
                            }
                        }
                    } else if (str != null) {
                        file = new StringReader(str);
                        try {
                            ?? newPullParser2 = XmlPullParserFactory.newInstance().newPullParser();
                            newPullParser2.setInput(file);
                            newPullParser2.nextTag();
                            fileInputStream2 = newPullParser2;
                            fileInputStream = null;
                            obj = file;
                        } catch (Exception e12) {
                            e10 = e12;
                            e10.printStackTrace();
                            if (fileInputStream3 != null) {
                                try {
                                    fileInputStream3.close();
                                } catch (Exception unused) {
                                    LocalLog.b("OGuardRusHelper", "getValueForTag failed");
                                }
                            }
                        }
                    } else {
                        file = 0;
                        fileInputStream = null;
                    }
                    fileInputStream3 = fileInputStream2;
                    r62 = fileInputStream3;
                    file = obj;
                } catch (Exception e13) {
                    e10 = e13;
                    file = fileInputStream3;
                } catch (Throwable th3) {
                    th = th3;
                    file = fileInputStream3;
                }
                return str3;
            } catch (Throwable th4) {
                th = th4;
            }
        }

        private void g() {
            LocalLog.d("OGuardRusHelper", "OGuardRusHelper: fileParser init");
            try {
                File file = new File("/data/oplus/os/bpm");
                if (file.exists()) {
                    return;
                }
                file.mkdirs();
            } catch (Exception e10) {
                LocalLog.b("OGuardRusHelper", "OGuardRusHelper initDir error: " + e10);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r1v10 */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r1v13 */
        /* JADX WARN: Type inference failed for: r1v14 */
        /* JADX WARN: Type inference failed for: r1v15 */
        /* JADX WARN: Type inference failed for: r1v16 */
        /* JADX WARN: Type inference failed for: r1v17 */
        /* JADX WARN: Type inference failed for: r1v18 */
        /* JADX WARN: Type inference failed for: r1v4 */
        /* JADX WARN: Type inference failed for: r1v5 */
        /* JADX WARN: Type inference failed for: r1v7, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r1v8 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void h(String str, String str2) {
            Throwable th;
            FileOutputStream fileOutputStream;
            Exception e10;
            StringBuilder sb2;
            if (str == null) {
                return;
            }
            File file = new File(str);
            boolean exists = file.exists();
            ?? r12 = exists;
            if (!exists) {
                try {
                    boolean createNewFile = file.createNewFile();
                    r12 = createNewFile;
                    if (!createNewFile) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("saveStrToFile: failed create file ");
                        sb3.append(str);
                        LocalLog.d("OGuardRusHelper", sb3.toString());
                        r12 = sb3;
                    }
                } catch (IOException e11) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("failed create file ");
                    sb4.append(e11);
                    LocalLog.b("OGuardRusHelper", sb4.toString());
                    r12 = sb4;
                }
            }
            if (file.exists()) {
                try {
                    try {
                        fileOutputStream = new FileOutputStream(file);
                    } catch (Exception e12) {
                        fileOutputStream = null;
                        e10 = e12;
                    } catch (Throwable th2) {
                        r12 = 0;
                        th = th2;
                        if (r12 != 0) {
                        }
                        throw th;
                    }
                    try {
                        fileOutputStream.write(str2.getBytes(StandardCharsets.UTF_8));
                        fileOutputStream.flush();
                        try {
                            fileOutputStream.close();
                            r12 = fileOutputStream;
                        } catch (IOException e13) {
                            e = e13;
                            sb2 = new StringBuilder();
                            sb2.append("failed close stream ");
                            sb2.append(e);
                            LocalLog.b("OGuardRusHelper", sb2.toString());
                        }
                    } catch (Exception e14) {
                        e10 = e14;
                        LocalLog.b("OGuardRusHelper", "failed write file " + e10);
                        r12 = fileOutputStream;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                                r12 = fileOutputStream;
                            } catch (IOException e15) {
                                e = e15;
                                sb2 = new StringBuilder();
                                sb2.append("failed close stream ");
                                sb2.append(e);
                                LocalLog.b("OGuardRusHelper", sb2.toString());
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (r12 != 0) {
                        try {
                            r12.close();
                        } catch (IOException e16) {
                            LocalLog.b("OGuardRusHelper", "failed close stream " + e16);
                        }
                    }
                    throw th;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0058  */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0052 A[Catch: Exception -> 0x0048, TRY_LEAVE, TryCatch #1 {Exception -> 0x0048, blocks: (B:18:0x0036, B:20:0x003c, B:7:0x0052, B:5:0x004a), top: B:17:0x0036 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public String e(Context context, String str) {
            Cursor cursor;
            String string;
            String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
            if (context != null) {
                try {
                    cursor = context.getContentResolver().query(OGuardRusHelper.f11597g, strArr, "filtername=\"" + str + "\"", null, null);
                } catch (Exception e10) {
                    e = e10;
                    cursor = null;
                    if (cursor != null) {
                        cursor.close();
                    }
                    LocalLog.l("OGuardRusHelper", "We can not get Filtrate app data from provider, because of " + e);
                    return null;
                }
            } else {
                cursor = null;
            }
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
                } catch (Exception e11) {
                    e = e11;
                    if (cursor != null) {
                    }
                    LocalLog.l("OGuardRusHelper", "We can not get Filtrate app data from provider, because of " + e);
                    return null;
                }
            }
            LocalLog.d("OGuardRusHelper", "the Filtrate app cursor is null !!!");
            string = null;
            if (cursor != null) {
            }
            return string;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: OGuardRusHelper.java */
    /* renamed from: g7.b$b */
    /* loaded from: classes.dex */
    public class b extends FileObserver {
        b(String str) {
            super(str, 8);
        }

        @Override // android.os.FileObserver
        public void onEvent(int i10, String str) {
            if (i10 == 8) {
                if ("sys_oguard_config_list.xml".equals(str)) {
                    LocalLog.a("OGuardRusHelper", "handleUpdateOGUARDConfig!");
                    OGuardRusHelper.this.j("update oguard config file");
                    OGuardRusHelper.this.i("oguard_config");
                } else if ("notify_whitelist.xml".equals(str)) {
                    LocalLog.a("OGuardRusHelper", "handleUpdateNotifyWhiteList!");
                    AppInfoManager.m(OGuardRusHelper.this.f11600b).u();
                }
            }
        }
    }

    private OGuardRusHelper(Context context) {
        this.f11601c = null;
        this.f11600b = context;
        this.f11601c = new a();
        h();
        i("oguard_config");
    }

    private String d(String str) {
        File file = new File(str);
        FileInputStream fileInputStream = null;
        if (!file.exists()) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        try {
            try {
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(file);
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream2, "utf-8"));
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb2.append(readLine);
                        }
                        fileInputStream2.close();
                    } catch (IOException | UnsupportedCharsetException unused) {
                        fileInputStream = fileInputStream2;
                        LocalLog.b("OGuardRusHelper", "convert file to stream failed");
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        return sb2.toString();
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Exception unused2) {
                                LocalLog.b("OGuardRusHelper", "convertStreamToString failed");
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException | UnsupportedCharsetException unused3) {
            }
        } catch (Exception unused4) {
            LocalLog.b("OGuardRusHelper", "convertStreamToString failed");
        }
        return sb2.toString();
    }

    private int e(String str) {
        if (str == null || str.isEmpty()) {
            return -1;
        }
        String f10 = this.f11601c.f(null, str, "version");
        try {
            if (TextUtils.isEmpty(f10)) {
                return -1;
            }
            return Integer.parseInt(f10);
        } catch (Exception unused) {
            LocalLog.b("OGuardRusHelper", "getValueForTag failed");
            return -1;
        }
    }

    public static OGuardRusHelper f(Context context) {
        if (f11598h == null) {
            synchronized (OGuardRusHelper.class) {
                if (f11598h == null) {
                    f11598h = new OGuardRusHelper(context);
                }
            }
        }
        return f11598h;
    }

    private int g(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return 0;
        }
        String f10 = this.f11601c.f(file, null, "version");
        try {
            if (TextUtils.isEmpty(f10)) {
                return 0;
            }
            return Integer.parseInt(f10);
        } catch (Exception e10) {
            e10.printStackTrace();
            return 0;
        }
    }

    private void h() {
        File file = new File("/data/oplus/os/bpm/sys_oguard_config_list.xml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e10) {
                LocalLog.d("OGuardRusHelper", "failed create file " + e10);
            }
        }
        l(true);
        if (new File("/data/oplus/os/bpm/sys_oguard_config_list.xml").exists()) {
            j("initOGuardFile");
        }
        k();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(String str) {
        Bundle bundle = new Bundle();
        synchronized (this.f11599a) {
            bundle.putString("configFile", this.f11604f);
        }
        OplusCommonConfig.getInstance().putConfigInfo(str, bundle, 1);
    }

    private void k() {
        this.f11602d = new b("/data/oplus/os/bpm");
        this.f11603e = new b("/data/oplus/os/battery");
        this.f11602d.startWatching();
        this.f11603e.startWatching();
    }

    public void j(String str) {
        String d10 = d("/data/oplus/os/bpm/sys_oguard_config_list.xml");
        synchronized (this.f11599a) {
            this.f11604f = d10;
        }
    }

    public void l(boolean z10) {
        if (z10) {
            String e10 = this.f11601c.e(this.f11600b, "sys_oguard_config_list");
            String d10 = this.f11601c.d(this.f11600b, "sys_oguard_config_list.xml");
            int e11 = e(e10);
            int e12 = e(d10);
            int g6 = g("/data/oplus/os/bpm/sys_oguard_config_list.xml");
            LocalLog.a("OGuardRusHelper", "updateOGuardConfigList isInitFile:" + z10 + " rusVersion: " + e11 + " assetsVersion = " + e12 + " localVersion: " + g6);
            int max = Math.max(e11, e12);
            if (max != -1) {
                if (e11 < e12) {
                    e10 = d10;
                }
                if (max > g6) {
                    this.f11601c.h("/data/oplus/os/bpm/sys_oguard_config_list.xml", e10);
                    return;
                }
                return;
            }
            return;
        }
        String e13 = this.f11601c.e(this.f11600b, "sys_oguard_config_list");
        if (e13 == null || e13.isEmpty()) {
            return;
        }
        int e14 = e(e13);
        int g10 = g("/data/oplus/os/bpm/sys_oguard_config_list.xml");
        LocalLog.a("OGuardRusHelper", "updateOGuardConfigList isInitFile:" + z10 + " rusVersion: " + e14 + " localVersion: " + g10);
        if (e14 > g10) {
            this.f11601c.h("/data/oplus/os/bpm/sys_oguard_config_list.xml", e13);
        }
    }
}
