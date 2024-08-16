package ba;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.settings.OplusSettings;
import com.oplus.thermalcontrol.ThermalControlConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: StartupBaseParser.java */
/* renamed from: ba.b, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class StartupBaseParser {

    /* renamed from: i, reason: collision with root package name */
    private static final Uri f4624i = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* renamed from: a, reason: collision with root package name */
    protected long f4625a;

    /* renamed from: b, reason: collision with root package name */
    protected String f4626b;

    /* renamed from: c, reason: collision with root package name */
    private String f4627c;

    /* renamed from: d, reason: collision with root package name */
    private String f4628d;

    /* renamed from: e, reason: collision with root package name */
    private String f4629e;

    /* renamed from: f, reason: collision with root package name */
    private String f4630f;

    /* renamed from: g, reason: collision with root package name */
    private String f4631g;

    /* renamed from: h, reason: collision with root package name */
    private Context f4632h;

    public StartupBaseParser(Context context, String str, String str2, String str3, String str4, String str5) {
        this.f4632h = context;
        this.f4627c = str;
        this.f4630f = str2;
        this.f4629e = str3;
        this.f4628d = str4;
        this.f4631g = str5;
    }

    private boolean b(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return false;
        }
        long h10 = h(str);
        long h11 = h(str2);
        LocalLog.l(this.f4627c, "old version: " + h10 + ", new version: " + h11);
        boolean z10 = h11 >= h10;
        if (z10) {
            h10 = h11;
        }
        this.f4625a = h10;
        return z10;
    }

    private String d() {
        String q10 = q(this.f4628d);
        String o10 = o(this.f4630f);
        if (b(q10, o10)) {
            this.f4626b = "LOCAL_RUS_FILE";
            return o10;
        }
        this.f4626b = "LOCAL_FILTER_NAME";
        return q10;
    }

    private XmlPullParser g(String str) {
        XmlPullParser xmlPullParser = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(new StringReader(str));
            xmlPullParser.nextTag();
            return xmlPullParser;
        } catch (IOException | XmlPullParserException e10) {
            LocalLog.c(this.f4627c, "parse version get error: ", e10);
            return xmlPullParser;
        }
    }

    public static boolean j(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.compile("-?[0-9]*").matcher(str).matches();
    }

    private String n(InputStream inputStream) {
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
                    LocalLog.a(this.f4627c, "readTextFile get error: " + e10);
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

    private String p() {
        String str = this.f4631g;
        if (str != null) {
            return q(str);
        }
        return null;
    }

    abstract void a();

    abstract void c(XmlPullParser xmlPullParser);

    /* JADX WARN: Removed duplicated region for block: B:11:0x0050 A[Catch: Exception -> 0x0060, TRY_ENTER, TRY_LEAVE, TryCatch #0 {Exception -> 0x0060, blocks: (B:7:0x0010, B:11:0x0050, B:16:0x005f, B:21:0x005c, B:23:0x0034, B:25:0x003a, B:9:0x0047, B:18:0x0057), top: B:6:0x0010, inners: #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String e(String str) {
        String str2 = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Context context = this.f4632h;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            Cursor query = context.getContentResolver().query(f4624i, strArr, "filtername=\"" + str + "\"", null, null);
            if (query != null) {
                try {
                    if (query.getCount() > 0) {
                        int columnIndex = query.getColumnIndex(ThermalControlConfig.COLUMN_NAME_XML);
                        query.moveToNext();
                        str2 = query.getString(columnIndex);
                        if (query != null) {
                            query.close();
                        }
                        return str2;
                    }
                } finally {
                }
            }
            LocalLog.l(this.f4627c, "remote config not exist");
            if (query != null) {
            }
            return str2;
        } catch (Exception e10) {
            LocalLog.c(this.f4627c, "We can not get remote app data from provider.", e10);
            return str2;
        }
    }

    public String f() {
        String e10 = e(this.f4629e);
        String d10 = d();
        if (!b(d10, e10)) {
            return d10;
        }
        this.f4626b = "RUS_FILTER_NAME";
        return e10;
    }

    public long h(String str) {
        int next;
        XmlPullParser g6 = g(str);
        long j10 = -1;
        if (g6 == null) {
            LocalLog.a(this.f4627c, "getXmlVersion：failed to get XmlPullParser.");
            return -1L;
        }
        do {
            try {
                next = g6.next();
                if (next == 2 && "version".equals(g6.getName())) {
                    String nextText = g6.nextText();
                    if (j(nextText)) {
                        j10 = Long.parseLong(nextText);
                    }
                }
            } catch (IOException | XmlPullParserException e10) {
                LocalLog.c(this.f4627c, "parse version get error: ", e10);
            }
        } while (next != 1);
        return j10;
    }

    public boolean i() {
        String e10 = e(this.f4629e);
        String o10 = o(this.f4630f);
        if (TextUtils.isEmpty(e10)) {
            return false;
        }
        long h10 = h(o10);
        long h11 = h(e10);
        LocalLog.a("StartupManager", "localVersion:" + h10 + " remoteVersion:" + h11);
        return h11 > h10;
    }

    boolean k(String str, XmlPullParser xmlPullParser, boolean z10) {
        if (!"filter-name".equals(str) || !z10) {
            return true;
        }
        String nextText = xmlPullParser.nextText();
        if (!TextUtils.isEmpty(nextText) && nextText.equals(this.f4629e)) {
            return true;
        }
        LocalLog.a(this.f4627c, "parse file ERROR: " + nextText + " is not match with " + this.f4629e);
        return false;
    }

    abstract void l(boolean z10, String str);

    /* JADX WARN: Removed duplicated region for block: B:43:0x009b A[Catch: Exception -> 0x0097, TryCatch #4 {Exception -> 0x0097, blocks: (B:52:0x0093, B:43:0x009b, B:45:0x00a0), top: B:51:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00a0 A[Catch: Exception -> 0x0097, TRY_LEAVE, TryCatch #4 {Exception -> 0x0097, blocks: (B:52:0x0093, B:43:0x009b, B:45:0x00a0), top: B:51:0x0093 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0093 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<String> m(String str) {
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        Exception e10;
        ArrayList<String> arrayList = new ArrayList<>();
        if (str == null) {
            LocalLog.a(this.f4627c, "cannot get data from oplus settings, invalid args , filePath is null.");
            return arrayList;
        }
        BufferedReader bufferedReader2 = null;
        try {
            try {
                inputStream = OplusSettings.readConfig(this.f4632h, str, 0);
                try {
                    inputStreamReader = new InputStreamReader(inputStream, "utf-8");
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
                                } catch (Throwable th) {
                                    th = th;
                                    bufferedReader2 = bufferedReader;
                                    if (bufferedReader2 != null) {
                                        try {
                                            bufferedReader2.close();
                                        } catch (Exception e11) {
                                            e11.printStackTrace();
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
                            } catch (Exception e12) {
                                e10 = e12;
                                arrayList.clear();
                                LocalLog.a(this.f4627c, "getDataFromColorSettings, error:" + e10.toString());
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
                    } catch (Exception e13) {
                        bufferedReader = null;
                        e10 = e13;
                    } catch (Throwable th2) {
                        th = th2;
                        if (bufferedReader2 != null) {
                        }
                        if (inputStreamReader != null) {
                        }
                        if (inputStream != null) {
                        }
                        throw th;
                    }
                } catch (Exception e14) {
                    bufferedReader = null;
                    e10 = e14;
                    inputStreamReader = null;
                } catch (Throwable th3) {
                    th = th3;
                    inputStreamReader = null;
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [ba.b] */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5 */
    /* JADX WARN: Type inference failed for: r5v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r5v8, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r5v9 */
    public String o(String str) {
        String str2 = null;
        str2 = null;
        str2 = null;
        InputStream inputStream = null;
        try {
            try {
            } catch (Throwable th) {
                th = th;
                inputStream = str;
            }
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        if (str == 0) {
            LocalLog.l(this.f4627c, "cannot get data from oplus settings, invalid args , filePath is null.");
            return null;
        }
        try {
            str = OplusSettings.readConfig(this.f4632h, (String) str, 0);
        } catch (Exception e11) {
            e = e11;
            str = 0;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e12) {
                    e12.printStackTrace();
                }
            }
            throw th;
        }
        try {
            str2 = n(str);
        } catch (Exception e13) {
            e = e13;
            LocalLog.b(this.f4627c, "getDataFromColorSettings, error:" + e.toString());
            if (str != 0) {
                str.close();
                str = str;
            }
            return str2;
        }
        if (str != 0) {
            str.close();
            str = str;
        }
        return str2;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0070 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String q(String str) {
        FileInputStream fileInputStream;
        String str2;
        StringBuilder sb2;
        File file = new File(str);
        FileInputStream fileInputStream2 = null;
        r4 = null;
        r4 = null;
        String str3 = null;
        if (!file.exists()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
        } catch (Exception e10) {
            e = e10;
            fileInputStream = null;
        } catch (Throwable th) {
            th = th;
            if (fileInputStream2 != null) {
            }
            throw th;
        }
        try {
            try {
                str3 = n(fileInputStream);
                try {
                    fileInputStream.close();
                } catch (IOException e11) {
                    e = e11;
                    str2 = this.f4627c;
                    sb2 = new StringBuilder();
                    sb2.append("close ");
                    sb2.append(str);
                    sb2.append(" exception: ");
                    sb2.append(e);
                    LocalLog.a(str2, sb2.toString());
                    return str3;
                }
            } catch (Throwable th2) {
                th = th2;
                fileInputStream2 = fileInputStream;
                if (fileInputStream2 != null) {
                    try {
                        fileInputStream2.close();
                    } catch (IOException e12) {
                        LocalLog.a(this.f4627c, "close " + str + " exception: " + e12);
                    }
                }
                throw th;
            }
        } catch (Exception e13) {
            e = e13;
            LocalLog.a(this.f4627c, "read " + str + " exception: " + e);
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e14) {
                    e = e14;
                    str2 = this.f4627c;
                    sb2 = new StringBuilder();
                    sb2.append("close ");
                    sb2.append(str);
                    sb2.append(" exception: ");
                    sb2.append(e);
                    LocalLog.a(str2, sb2.toString());
                    return str3;
                }
            }
            return str3;
        }
        return str3;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void r(String str, List<String> list) {
        OutputStream outputStream;
        OutputStreamWriter outputStreamWriter;
        if (list != null && !list.isEmpty() && str != null) {
            LocalLog.a(this.f4627c, "start saving oplus settings file: " + str);
            boolean z10 = false;
            BufferedWriter bufferedWriter = null;
            try {
                try {
                    outputStream = OplusSettings.writeConfig(this.f4632h, str, 0);
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
                                    z10 = true;
                                    bufferedWriter2.close();
                                    outputStreamWriter.close();
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                } catch (Exception e10) {
                                    e = e10;
                                    bufferedWriter = bufferedWriter2;
                                    LocalLog.a(this.f4627c, "failed to save oplus settings :" + str + " error:" + e.toString());
                                    if (bufferedWriter != null) {
                                        bufferedWriter.close();
                                    }
                                    if (outputStreamWriter != null) {
                                        outputStreamWriter.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    if (z10) {
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
            if (z10) {
                return;
            }
            s(str);
            return;
        }
        LocalLog.a(this.f4627c, "try to save oplus settings failed, invalid args, data: " + list + " file: " + str);
    }

    abstract void s(String str);

    /* JADX WARN: Removed duplicated region for block: B:11:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void t(String str, String str2) {
        boolean z10 = false;
        BufferedWriter bufferedWriter = null;
        try {
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(OplusSettings.writeConfig(this.f4632h, str, 0), "utf-8"));
                    try {
                        bufferedWriter2.write(str2);
                        bufferedWriter2.flush();
                        z10 = true;
                        bufferedWriter2.close();
                    } catch (Exception e10) {
                        e = e10;
                        bufferedWriter = bufferedWriter2;
                        e.printStackTrace();
                        if (bufferedWriter != null) {
                            bufferedWriter.close();
                        }
                        if (z10) {
                        }
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
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e12) {
                e = e12;
            }
        } catch (Exception e13) {
            e13.printStackTrace();
        }
        if (z10) {
            return;
        }
        s(str);
    }

    public boolean u(boolean z10) {
        int next;
        String f10 = f();
        LocalLog.l(this.f4627c, "mParseFile: " + this.f4626b + " mParseVersion: " + this.f4625a);
        if (TextUtils.isEmpty(f10)) {
            LocalLog.l(this.f4627c, "remote & local is both not exist, read system_ext.");
            f10 = p();
            if (TextUtils.isEmpty(f10)) {
                LocalLog.l(this.f4627c, "cannot find file from system_ext.");
                return false;
            }
        }
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            if (newPullParser == null) {
                LocalLog.a(this.f4627c, "xml pull parser is null.");
                return false;
            }
            newPullParser.setInput(new StringReader(f10));
            newPullParser.nextTag();
            a();
            do {
                next = newPullParser.next();
                if (next == 2) {
                    String name = newPullParser.getName();
                    if (!k(name, newPullParser, z10)) {
                        return false;
                    }
                    v(name, newPullParser);
                } else if (next == 3) {
                    c(newPullParser);
                }
            } while (next != 1);
            l(z10, f10);
            return true;
        } catch (Exception e10) {
            LocalLog.c(this.f4627c, "parsing failed.", e10);
            LocalLog.a(this.f4627c, "parse file ERROR");
            return false;
        }
    }

    abstract void v(String str, XmlPullParser xmlPullParser);
}
