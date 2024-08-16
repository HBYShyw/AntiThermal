package com.oplus.debug;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.OplusAssertTip;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

/* loaded from: classes.dex */
public class ASSERT {
    private static final String ASSERT_STATE = "persist.sys.assert.state";
    private static final int IS_GZIPPED = 4;
    private static final String TAG = "DEBUGLOG.ASSERT";
    private static OplusAssertTip mFunctionProxy = null;

    protected ASSERT() {
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x01f0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x02e0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:161:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0315 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0307 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0335 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0327 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01d8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean epitaph(File temp, String tag, int flags, Context c) {
        Exception exc;
        String process;
        String packageName;
        String PID;
        int maxSize;
        String packageName2;
        if (temp == null) {
            return false;
        }
        InputStream isTempForTrim = null;
        BufferedReader brTempForTrim = null;
        String process2 = null;
        String line = null;
        String PID2 = "NONE";
        try {
            String prop = SystemProperties.get("persist.sys.thridpart.debug", "false");
            int maxSize2 = "true".equals(prop) ? 4096 : 1024;
            try {
                StringBuilder sb = new StringBuilder(maxSize2);
                isTempForTrim = new FileInputStream(temp);
                if ((flags & 4) != 0) {
                    isTempForTrim = new GZIPInputStream(isTempForTrim);
                }
                brTempForTrim = new BufferedReader(new InputStreamReader(isTempForTrim));
                int count = 0;
                while (true) {
                    process = process2;
                    if (count >= maxSize2) {
                        packageName = line;
                        PID = PID2;
                        break;
                    }
                    try {
                        String line2 = brTempForTrim.readLine();
                        if (line2 == null) {
                            packageName = line;
                            PID = PID2;
                            break;
                        }
                        packageName = line;
                        PID = PID2;
                        try {
                            if (line2.startsWith("-----", 0)) {
                                break;
                            }
                            if (line2.startsWith("Process: ", 0)) {
                                try {
                                    process = line2.substring(line2.indexOf(":") + 1).trim();
                                } catch (IOException e) {
                                    e = e;
                                    try {
                                        e.printStackTrace();
                                        if (isTempForTrim != null) {
                                        }
                                        if (brTempForTrim == null) {
                                        }
                                    } catch (Throwable th) {
                                        th = th;
                                        exc = th;
                                        if (isTempForTrim != null) {
                                            try {
                                                isTempForTrim.close();
                                            } catch (Exception e2) {
                                                Log.e(TAG, "finally close is failed.");
                                            }
                                        }
                                        if (brTempForTrim == null) {
                                            throw exc;
                                        }
                                        try {
                                            brTempForTrim.close();
                                            throw exc;
                                        } catch (Exception e3) {
                                            Log.e(TAG, "finally close br failed.");
                                            throw exc;
                                        }
                                    }
                                } catch (Exception e4) {
                                    e = e4;
                                    maxSize = maxSize2;
                                    PID2 = PID;
                                    line = packageName;
                                    try {
                                        Log.e(TAG, "epitaph failed.", e);
                                        if (count == 0) {
                                        }
                                    } catch (IOException e5) {
                                        e = e5;
                                        e.printStackTrace();
                                        if (isTempForTrim != null) {
                                            try {
                                                isTempForTrim.close();
                                            } catch (Exception e6) {
                                                Log.e(TAG, "finally close is failed.");
                                            }
                                        }
                                        if (brTempForTrim == null) {
                                            return false;
                                        }
                                        try {
                                            brTempForTrim.close();
                                            return false;
                                        } catch (Exception e7) {
                                            Log.e(TAG, "finally close br failed.");
                                            return false;
                                        }
                                    } catch (Throwable e8) {
                                        exc = e8;
                                        if (isTempForTrim != null) {
                                        }
                                        if (brTempForTrim == null) {
                                        }
                                    }
                                } catch (Throwable th2) {
                                    exc = th2;
                                    if (isTempForTrim != null) {
                                    }
                                    if (brTempForTrim == null) {
                                    }
                                }
                            }
                            maxSize = maxSize2;
                            try {
                                if (line2.startsWith("PID: ", 0)) {
                                    PID = line2.substring(line2.indexOf(":") + 1).trim();
                                }
                                try {
                                    if (line2.startsWith("Package: ", 0)) {
                                        String start = line2.substring(line2.indexOf(":") + 2);
                                        int end = start.indexOf(" ");
                                        if (end > 0) {
                                            packageName2 = start.substring(0, end).trim();
                                            sb.append(line2);
                                            sb.append("\r\n");
                                            count += line2.length();
                                            line = packageName2;
                                            process2 = process;
                                            PID2 = PID;
                                            maxSize2 = maxSize;
                                        } else {
                                            Log.v(TAG, "pacakge line = " + line2);
                                        }
                                    }
                                    sb.append(line2);
                                    sb.append("\r\n");
                                    count += line2.length();
                                    line = packageName2;
                                    process2 = process;
                                    PID2 = PID;
                                    maxSize2 = maxSize;
                                } catch (IOException e9) {
                                    e = e9;
                                    e.printStackTrace();
                                    if (isTempForTrim != null) {
                                    }
                                    if (brTempForTrim == null) {
                                    }
                                } catch (Exception e10) {
                                    e = e10;
                                    line = packageName2;
                                    PID2 = PID;
                                    Log.e(TAG, "epitaph failed.", e);
                                    if (count == 0) {
                                    }
                                } catch (Throwable th3) {
                                    exc = th3;
                                    if (isTempForTrim != null) {
                                    }
                                    if (brTempForTrim == null) {
                                    }
                                }
                                packageName2 = packageName;
                            } catch (IOException e11) {
                                e = e11;
                            } catch (Exception e12) {
                                e = e12;
                                PID2 = PID;
                                line = packageName;
                            } catch (Throwable th4) {
                                exc = th4;
                            }
                        } catch (IOException e13) {
                            e = e13;
                        } catch (Exception e14) {
                            e = e14;
                            maxSize = maxSize2;
                            PID2 = PID;
                            line = packageName;
                        } catch (Throwable th5) {
                            exc = th5;
                        }
                    } catch (IOException e15) {
                        e = e15;
                    } catch (Exception e16) {
                        e = e16;
                        maxSize = maxSize2;
                    } catch (Throwable th6) {
                        exc = th6;
                    }
                }
                PID2 = PID;
                line = packageName;
                if (count == 0) {
                    try {
                        isTempForTrim.close();
                    } catch (Exception e17) {
                        Log.e(TAG, "finally close is failed.");
                    }
                    try {
                        brTempForTrim.close();
                        return false;
                    } catch (Exception e18) {
                        Log.e(TAG, "finally close br failed.");
                        return false;
                    }
                }
                try {
                    isTempForTrim.close();
                } catch (Exception e19) {
                    Log.e(TAG, "finally close is failed.");
                }
                try {
                    brTempForTrim.close();
                } catch (Exception e20) {
                    Log.e(TAG, "finally close br failed.");
                }
                String process3 = process == null ? "NONE" : process;
                mFunctionProxy = OplusAssertTip.getInstance();
                InputStream isForCopyAssert = null;
                int showResult = -1;
                try {
                    InputStream isForCopyAssert2 = new FileInputStream(temp);
                    if ((flags & 4) != 0) {
                        isForCopyAssert2 = new GZIPInputStream(isForCopyAssert2);
                    }
                    String withoutColonProcessName = process3.replace(':', '_');
                    Log.d(TAG, "after replace ':' with '_' ,the ProcessName is " + withoutColonProcessName);
                    copyAssert(isForCopyAssert2, withoutColonProcessName + "-" + PID2);
                    isForCopyAssert2.close();
                    isForCopyAssert = null;
                    String content = sb.toString();
                    if (SystemProperties.get(ASSERT_STATE, "true").equals("false")) {
                        Log.w(TAG, "assert state is close");
                    } else {
                        try {
                            try {
                                String appName = getAppName(c, line);
                                if (!TextUtils.isEmpty(appName)) {
                                    content = appName + "\n" + content;
                                }
                                showResult = mFunctionProxy.requestShowAssertMessage(content);
                            } catch (Throwable th7) {
                                th = th7;
                                Throwable th8 = th;
                                if (isForCopyAssert != null) {
                                    throw th8;
                                }
                                try {
                                    isForCopyAssert.close();
                                    throw th8;
                                } catch (Exception e21) {
                                    Log.e(TAG, "finally close isForCopyAssert failed.");
                                    throw th8;
                                }
                            }
                        } catch (Exception e22) {
                            e = e22;
                            Log.e(TAG, "epitaph failed.", e);
                            if (isForCopyAssert == null) {
                                return false;
                            }
                            try {
                                isForCopyAssert.close();
                                return false;
                            } catch (Exception e23) {
                                Log.e(TAG, "finally close isForCopyAssert failed.");
                                return false;
                            }
                        }
                    }
                    if (0 != 0) {
                        try {
                            isForCopyAssert.close();
                        } catch (Exception e24) {
                            Log.e(TAG, "finally close isForCopyAssert failed.");
                        }
                    }
                    return -1 != showResult;
                } catch (Exception e25) {
                    e = e25;
                } catch (Throwable th9) {
                    th = th9;
                    Throwable th82 = th;
                    if (isForCopyAssert != null) {
                    }
                }
            } catch (IOException e26) {
                e = e26;
            } catch (Throwable th10) {
                exc = th10;
            }
        } catch (IOException e27) {
            e = e27;
        } catch (Throwable th11) {
            th = th11;
            exc = th;
            if (isTempForTrim != null) {
            }
            if (brTempForTrim == null) {
            }
        }
    }

    public static void copyTombstoneToAssert(String filePath) {
        if (SystemProperties.get("persist.sys.assert.panic", "false").equals("true") || SystemProperties.get("persist.sys.assert.panic.camera", "false").equals("true")) {
            SystemProperties.set("sys.tombstone.file", filePath);
            SystemProperties.set("ctl.start", "tranfer_tomb");
        }
    }

    public static boolean copyAssert(InputStream inputStream, String destFileString) {
        if (!SystemProperties.get("persist.sys.assert.panic", "false").equals("true") && !SystemProperties.get("persist.sys.assert.panic.camera", "false").equals("true")) {
            return true;
        }
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String now = df.format(dt);
        String DestPath = SystemProperties.get("sys.oplus.logkit.assertlog", "");
        File destFile = new File(DestPath + "/" + (destFileString + "-" + now + ".txt"));
        Log.d(TAG, "copyAssert destFile=" + destFile);
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                while (true) {
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead < 0) {
                        return true;
                    }
                    out.write(buffer, 0, bytesRead);
                    out.flush();
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copyAnrToAssert(String filePath, int pid) {
        if (filePath != null) {
            SimpleDateFormat sAnrFileDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
            String formattedDate = sAnrFileDateFormat.format(new Date());
            String tracesName = "traces_" + pid + "_" + formattedDate + ".txt";
            if (SystemProperties.get("persist.sys.assert.panic", "false").equals("true") || SystemProperties.get("persist.sys.assert.panic.camera", "false").equals("true")) {
                SystemProperties.set("sys.anr.srcfile", filePath);
                SystemProperties.set("sys.anr.destfile", tracesName);
                SystemProperties.set("ctl.start", "tranfer_anr");
                copyBinderInfo();
            }
        }
    }

    private static String getAppName(Context c, String packageName) {
        if (packageName == null) {
            Log.w(TAG, "The package name is null, cann't get the app label");
            return null;
        }
        PackageManager pm = c.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai.loadLabel(pm).toString();
        } catch (Exception e) {
            Log.e(TAG, "getAppName failed: " + e);
            return null;
        }
    }

    public static void copyBinderInfo() {
        SystemProperties.set("ctl.start", "copybinderinfo");
    }
}
