package e1;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/* compiled from: FileUtil.java */
/* renamed from: e1.e, reason: use source file name */
/* loaded from: classes.dex */
public class FileUtil {
    public static String a(String... strArr) {
        int length = strArr.length;
        StringBuilder sb2 = new StringBuilder();
        int i10 = 0;
        while (i10 < length - 1) {
            if (strArr[i10] != null) {
                sb2.append(strArr[i10]);
                sb2.append("/");
            }
            i10++;
        }
        sb2.append(strArr[i10]);
        return sb2.toString();
    }

    public static void b(Context context, String str) {
        if (str == null) {
            return;
        }
        context.deleteSharedPreferences(str);
    }

    public static File c(String str, Context context) {
        return new File(a(context.getFilesDir().getAbsolutePath(), "pkicryptosdk", str));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:70:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:72:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0143 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x012a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0111 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v0, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r11v11, types: [java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r11v18 */
    /* JADX WARN: Type inference failed for: r11v19, types: [java.io.Reader, java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r11v4 */
    /* JADX WARN: Type inference failed for: r11v5, types: [java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v10 */
    /* JADX WARN: Type inference failed for: r4v11 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v5, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r4v6, types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r4v7, types: [java.io.FileInputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r4v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static List<String> d(File file, ReadWriteLock readWriteLock) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2;
        boolean exists = file.exists();
        ?? r42 = exists;
        if (!exists) {
            return null;
        }
        if (readWriteLock != null) {
            Lock readLock = readWriteLock.readLock();
            readLock.lock();
            r42 = readLock;
        }
        try {
            try {
                r42 = new FileInputStream((File) file);
            } catch (IOException e10) {
                e = e10;
                file = 0;
                r42 = 0;
                bufferedReader2 = null;
            } catch (Throwable th) {
                th = th;
                r42 = 0;
                bufferedReader = null;
            }
            try {
                file = new InputStreamReader((InputStream) r42, StandardCharsets.UTF_8);
                try {
                    bufferedReader2 = new BufferedReader(file);
                } catch (IOException e11) {
                    e = e11;
                    bufferedReader2 = null;
                } catch (Throwable th2) {
                    bufferedReader = null;
                    th = th2;
                    if (bufferedReader != null) {
                    }
                    if (file != 0) {
                    }
                    if (r42 != 0) {
                    }
                    if (readWriteLock == null) {
                    }
                }
            } catch (IOException e12) {
                e = e12;
                file = 0;
                bufferedReader2 = null;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                r42 = r42;
                th = th;
                file = bufferedReader;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e13) {
                        i.b("FileUtil", "readFile bufferedReader close error. " + e13);
                    }
                }
                if (file != 0) {
                    try {
                        file.close();
                    } catch (IOException e14) {
                        i.b("FileUtil", "readFile inputStreamReader close error. " + e14);
                    }
                }
                if (r42 != 0) {
                    try {
                        r42.close();
                    } catch (IOException e15) {
                        i.b("FileUtil", "readFile fileInputStream close error. " + e15);
                    }
                }
                if (readWriteLock == null) {
                    readWriteLock.readLock().unlock();
                    throw th;
                }
                throw th;
            }
            try {
                ArrayList arrayList = new ArrayList();
                while (true) {
                    String readLine = bufferedReader2.readLine();
                    if (readLine != null) {
                        arrayList.add(readLine);
                    } else {
                        try {
                            break;
                        } catch (IOException e16) {
                            i.b("FileUtil", "readFile bufferedReader close error. " + e16);
                        }
                    }
                }
                bufferedReader2.close();
                try {
                    file.close();
                } catch (IOException e17) {
                    i.b("FileUtil", "readFile inputStreamReader close error. " + e17);
                }
                try {
                    r42.close();
                } catch (IOException e18) {
                    i.b("FileUtil", "readFile fileInputStream close error. " + e18);
                }
                if (readWriteLock != null) {
                    readWriteLock.readLock().unlock();
                }
                return arrayList;
            } catch (IOException e19) {
                e = e19;
                i.b("FileUtil", "readFile error. " + e);
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e20) {
                        i.b("FileUtil", "readFile bufferedReader close error. " + e20);
                    }
                }
                if (file != 0) {
                    try {
                        file.close();
                    } catch (IOException e21) {
                        i.b("FileUtil", "readFile inputStreamReader close error. " + e21);
                    }
                }
                if (r42 != 0) {
                    try {
                        r42.close();
                    } catch (IOException e22) {
                        i.b("FileUtil", "readFile fileInputStream close error. " + e22);
                    }
                }
                if (readWriteLock != null) {
                    readWriteLock.readLock().unlock();
                }
                return null;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }
}
