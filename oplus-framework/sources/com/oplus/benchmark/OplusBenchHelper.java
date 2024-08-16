package com.oplus.benchmark;

import android.app.ActivityThread;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PerformanceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.screenmode.OplusRefreshRateInjectorImpl;
import com.oplus.uah.UAHResClient;
import com.oplus.uah.info.UAHEventRequest;
import com.oplus.uah.info.UAHResourceInfo;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class OplusBenchHelper {
    private static final int BENCH_CHECK = 3;
    private static final String BENCH_MARK_ANTUTU = "com.antutu.ABenchMark";
    private static final String BENCH_MARK_ANTUTU_3D = "com.antutu.benchmark.full";
    private static final int BENCH_MARK_ANTUTU_DATABASE = 13;
    private static final int BENCH_MARK_ANTUTU_FINISHED = 46;
    private static final int BENCH_MARK_ANTUTU_FIRST_STEP = 44;
    private static final int BENCH_MARK_ANTUTU_GEMM_MULTITHREAD = 10;
    private static final String BENCH_MARK_ANTUTU_LITE = "com.antutu.ABenchMark.lite";
    private static final int BENCH_MARK_ANTUTU_MULTITASK = 9;
    private static final int BENCH_MARK_ANTUTU_MULTITHREAD = 8;
    private static final int BENCH_MARK_ANTUTU_OPENMP = 7;
    private static final int BENCH_MARK_ANTUTU_QRCODE = 22;
    private static final int BENCH_MARK_ANTUTU_RAM_SPEED = 11;
    private static final int BENCH_MARK_ANTUTU_RANDOM_READ = 18;
    private static final int BENCH_MARK_ANTUTU_RANDOM_WRITE = 19;
    private static final int BENCH_MARK_ANTUTU_SEQUENTIAL_READ = 16;
    private static final int BENCH_MARK_ANTUTU_SEQUENTIAL_WRITE = 17;
    private static final int BENCH_MARK_ANTUTU_UI_LAG = 20;
    private static final int BENCH_MARK_ANTUTU_UX_FIRST_STEP = 20;
    private static final int BENCH_MARK_ANTUTU_WEBVIEW = 21;
    private static final String BENCH_MARK_LUDASHI = "com.ludashi.benchmark";
    private static final String BENCH_MODE_DATEBASE_MEM_MAX = "29999";
    private static final String BENCH_MODE_DATEBASE_MT6895 = "10027";
    private static final String BENCH_MODE_DATEBASE_MT6983 = "10087";
    private static final String BENCH_MODE_DATEBASE_SM6225 = "10047";
    private static final String BENCH_MODE_DATEBASE_SM6375 = "10067";
    private static final String BENCH_MODE_DATEBASE_SM8250 = "10107";
    private static final String BENCH_MODE_DATEBASE_SM8550 = "10007";
    private static final String BENCH_MODE_DISABLE = "0";
    private static final String BENCH_MODE_ENABLE = "1";
    private static final String BENCH_MODE_ENABLE_WITH_JPEG_MUTIL = "2";
    private static final String BENCH_MODE_GEMM_MULTITHREAD_MT6989 = "30004";
    private static final String BENCH_MODE_GEMM_MULTITHREAD_SM8650 = "30024";
    private static final String BENCH_MODE_MULTITASK_MT6989 = "30006";
    private static final String BENCH_MODE_MULTITASK_SM8650 = "30026";
    private static final String BENCH_MODE_MULTITHREAD_MT6989 = "30003";
    private static final String BENCH_MODE_MULTITHREAD_SM8650 = "30023";
    private static final String BENCH_MODE_OPENMP_MT6989 = "30005";
    private static final String BENCH_MODE_OPENMP_SM8650 = "30025";
    private static final String BENCH_MODE_RANDOMREAD_MEM_MAX = "29999";
    private static final String BENCH_MODE_RANDOMREAD_MT6895 = "10025";
    private static final String BENCH_MODE_RANDOMREAD_MT6983 = "10085";
    private static final String BENCH_MODE_RANDOMREAD_SM6225 = "10045";
    private static final String BENCH_MODE_RANDOMREAD_SM6375 = "10065";
    private static final String BENCH_MODE_RANDOMREAD_SM8250 = "10105";
    private static final String BENCH_MODE_RANDOMREAD_SM8550 = "10005";
    private static final String BENCH_MODE_RANDOMWRITE_MEM_MAX = "29999";
    private static final String BENCH_MODE_RANDOMWRITE_MT6895 = "10026";
    private static final String BENCH_MODE_RANDOMWRITE_MT6983 = "10086";
    private static final String BENCH_MODE_RANDOMWRITE_SM6225 = "10046";
    private static final String BENCH_MODE_RANDOMWRITE_SM6375 = "10066";
    private static final String BENCH_MODE_RANDOMWRITE_SM8250 = "10106";
    private static final String BENCH_MODE_RANDOMWRITE_SM8550 = "10006";
    private static final String BENCH_MODE_SEQUENTIAL_READ_MEM_MAX = "29999";
    private static final String BENCH_MODE_SEQUENTIAL_READ_MT6895 = "10023";
    private static final String BENCH_MODE_SEQUENTIAL_READ_MT6983 = "10083";
    private static final String BENCH_MODE_SEQUENTIAL_READ_SM6225 = "10043";
    private static final String BENCH_MODE_SEQUENTIAL_READ_SM6375 = "10063";
    private static final String BENCH_MODE_SEQUENTIAL_READ_SM8250 = "10103";
    private static final String BENCH_MODE_SEQUENTIAL_READ_SM8550 = "10003";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_MEM_MAX = "29999";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_MT6895 = "10024";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_MT6983 = "10084";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_SM6225 = "10044";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_SM6375 = "10064";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_SM8250 = "10104";
    private static final String BENCH_MODE_SEQUENTIAL_WRITE_SM8550 = "10004";
    private static final String BENCH_MODE_UI_LAG_MT6989 = "35003";
    private static final String BENCH_MODE_UI_LAG_SM8650 = "35023";
    private static final long BITMAP_CACHE_TIMEOUT = 1000;
    private static final int COASTLINE2 = 2;
    private static final int DELAY_MS_BENCH_CHECK = 1000;
    private static final int DELAY_MS_COASTLINE2 = 175000;
    private static final int DELAY_MS_SEASONS = 10000;
    private static final String PKG_NAME = "";
    private static final int REFRESH_RATE_60 = 2;
    private static final int SEASONS = 1;
    private static final String SYSTEM_PROPERTIES_SPEC = "sys.oplus.high.performance.spec";
    private static final String TAG = "OplusBenchHelper";
    private static final int TIMEOUT_COASTLINE2 = 56000;
    private static final int TIMEOUT_SEASONS = 78000;
    private static final int UAH_EVENT_BENCH_POWER_LIMIT = 214;
    private static final boolean DEBUG = !SystemProperties.getBoolean("ro.build.release_type", false);
    private static int sStepCount = 0;
    private static int sHandle = 0;
    private static ArrayList<UAHResourceInfo> sList = null;
    private static String lastResStr = "";
    private static int lastResId = -999;
    private static long lastTimestamp = -999;
    private static Bitmap bitmapCache = null;
    private static boolean sIsDx2 = Build.HARDWARE.startsWith("mt6985");
    private static boolean sIsMT6895 = Build.HARDWARE.startsWith("mt6895");
    private static boolean sIsSM8550 = Build.SOC_MODEL.startsWith("SM8550");
    private static boolean sIsSM6225 = Build.SOC_MODEL.startsWith("SM6225");
    private static boolean sIsSM6375 = Build.SOC_MODEL.startsWith("SM6375");
    private static boolean sIsMT6983 = Build.HARDWARE.startsWith("mt6983");
    private static boolean sIsMT6989 = Build.HARDWARE.startsWith("mt6989");
    private static boolean sIsSM8250 = Build.SOC_MODEL.startsWith("SM8250");
    private static boolean sIsSM8650 = Build.SOC_MODEL.startsWith("SM8650");
    private static OplusBenchHelper sInstance = null;
    private static final Object mLock = new Object();
    private UAHResClient mResClient = UAHResClient.get(OplusBenchHelper.class);
    private Handler mHandler = new Handler() { // from class: com.oplus.benchmark.OplusBenchHelper.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    OplusBenchHelper.sHandle = OplusBenchHelper.this.mResClient.acquireEvent(new UAHEventRequest(214, "", OplusBenchHelper.TIMEOUT_SEASONS, OplusBenchHelper.sList));
                    if (OplusBenchHelper.DEBUG && OplusBenchHelper.sHandle > 0) {
                        Log.d(OplusBenchHelper.TAG, "handleMessage SEASONS, acquireEvent success");
                        return;
                    }
                    return;
                case 2:
                    OplusBenchHelper.sHandle = OplusBenchHelper.this.mResClient.acquireEvent(new UAHEventRequest(214, "", OplusBenchHelper.TIMEOUT_COASTLINE2, OplusBenchHelper.sList));
                    if (OplusBenchHelper.DEBUG && OplusBenchHelper.sHandle > 0) {
                        Log.d(OplusBenchHelper.TAG, "handleMessage COASTLINE2, acquireEvent success");
                        return;
                    }
                    return;
                case 3:
                    if (OplusBenchHelper.this.isBenchMode()) {
                        OplusBenchHelper.this.mHandler.sendEmptyMessageDelayed(3, 1000L);
                        if (OplusBenchHelper.DEBUG) {
                            Log.d(OplusBenchHelper.TAG, "handleMessage BENCH_CHECK");
                            return;
                        }
                        return;
                    }
                    OplusBenchHelper.this.mHandler.removeMessages(1);
                    OplusBenchHelper.this.mHandler.removeMessages(2);
                    if (OplusBenchHelper.sHandle > 0) {
                        OplusBenchHelper.this.mResClient.release(OplusBenchHelper.sHandle);
                    }
                    if (OplusBenchHelper.DEBUG) {
                        Log.d(OplusBenchHelper.TAG, "removeMessage and release uah event");
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    private OplusBenchHelper() {
    }

    public static OplusBenchHelper getInstance() {
        OplusBenchHelper oplusBenchHelper;
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new OplusBenchHelper();
            }
            oplusBenchHelper = sInstance;
        }
        return oplusBenchHelper;
    }

    public boolean isEnableBitmapCache() {
        String benchMode = SystemProperties.get(SYSTEM_PROPERTIES_SPEC, "0");
        if ("1".equals(benchMode) && BENCH_MARK_LUDASHI.equals(ActivityThread.currentPackageName())) {
            return true;
        }
        Bitmap bitmap = bitmapCache;
        if (bitmap != null) {
            bitmap.recycle();
            bitmapCache = null;
            return false;
        }
        return false;
    }

    public Bitmap getBitmapCache(Resources res, int id, BitmapFactory.Options opts) {
        if (!isEnableBitmapCache()) {
            return null;
        }
        synchronized (mLock) {
            if (opts == null) {
                if (lastResId == id && lastResStr.equals(res.toString()) && bitmapCache != null && System.currentTimeMillis() - lastTimestamp < 1000) {
                    if (DEBUG) {
                        Log.i(TAG, "using bitmap cache");
                    }
                    lastTimestamp = System.currentTimeMillis();
                    return Bitmap.createBitmap(bitmapCache);
                }
            }
            return null;
        }
    }

    public void setBitmapCache(Bitmap cache, Resources res, int id) {
        if (!isEnableBitmapCache()) {
            return;
        }
        synchronized (mLock) {
            lastResStr = res.toString();
            lastResId = id;
            bitmapCache = cache;
            lastTimestamp = System.currentTimeMillis();
            if (DEBUG) {
                Log.i(TAG, "caching bitmap");
            }
        }
    }

    public static boolean isInBenchMode() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBenchMode() {
        String benchMode = SystemProperties.get(SYSTEM_PROPERTIES_SPEC, "0");
        if (DEBUG) {
            Log.d(TAG, "isBenchMode:" + benchMode);
        }
        if ("0".equals(benchMode)) {
            return false;
        }
        return true;
    }

    public static boolean isAntutuApp(String pkgName) {
        if (isAntutuMainApp(pkgName) || isAntutu3DApp(pkgName)) {
            return true;
        }
        return false;
    }

    public static boolean isAntutuMainApp(String pkgName) {
        if (BENCH_MARK_ANTUTU.equals(pkgName)) {
            return true;
        }
        return sIsSM6225 && BENCH_MARK_ANTUTU_LITE.equals(pkgName);
    }

    public static boolean isAntutu3DApp(String pkgName) {
        if (BENCH_MARK_ANTUTU_3D.equals(pkgName)) {
            return true;
        }
        return false;
    }

    public void benchStepCheck(Context mContext, Intent intent) {
        String pkgName = intent.getPackage();
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, "benchMode:" + isInBenchMode() + "; pkgName" + pkgName);
        }
        if (isInBenchMode() && isAntutuMainApp(pkgName) && intent.hasExtra("uid")) {
            int bm_uid = intent.getIntExtra("uid", -999);
            if (z) {
                Log.d(TAG, "bm_uid:" + bm_uid);
            }
            switch (bm_uid) {
                case 7:
                    if (sIsMT6989) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_OPENMP_MT6989);
                    } else if (sIsSM8650) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_OPENMP_SM8650);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_OPENMP");
                        return;
                    }
                    return;
                case 8:
                    PerformanceManager.enableMultiThreadOptimize();
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_MULTITHREAD");
                        return;
                    }
                    return;
                case 9:
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_MULTITASK");
                        return;
                    }
                    return;
                case 10:
                    if (sIsMT6989) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_GEMM_MULTITHREAD_MT6989);
                    } else if (sIsSM8650) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_GEMM_MULTITHREAD_SM8650);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_GEMM_MULTITHREAD");
                        return;
                    }
                    return;
                case 11:
                    OplusRefreshRateInjectorImpl.enterPSModeOnRate(true, 2);
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_RAM_SPEED");
                        return;
                    }
                    return;
                case 13:
                    if (sIsSM8550) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_DATEBASE_SM8550);
                    } else if (sIsMT6895) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_DATEBASE_MT6895);
                    } else if (sIsSM6225) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_DATEBASE_SM6225);
                    } else if (sIsSM6375) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM6375);
                    } else if (sIsMT6983) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_DATEBASE_MT6983);
                    } else if (sIsSM8250) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_DATEBASE_SM8250);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_DATABASE");
                        return;
                    }
                    return;
                case 16:
                    if (sIsSM8550) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM8550);
                    } else if (sIsMT6895) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_MT6895);
                    } else if (sIsSM6225) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM6225);
                    } else if (sIsSM6375) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM6375);
                    } else if (sIsMT6983) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_MT6983);
                    } else if (sIsSM8250) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM8250);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_RANDOM_READ");
                        return;
                    }
                    return;
                case 17:
                    if (sIsSM8550) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_WRITE_SM8550);
                    } else if (sIsMT6895) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_WRITE_MT6895);
                    } else if (sIsSM6225) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_WRITE_SM6225);
                    } else if (sIsSM6375) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM6375);
                    } else if (sIsMT6983) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_WRITE_MT6983);
                    } else if (sIsSM8250) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_WRITE_SM8250);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_RANDOM_WRITE");
                        return;
                    }
                    return;
                case 18:
                    if (sIsSM8550) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMREAD_SM8550);
                    } else if (sIsMT6895) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMREAD_MT6895);
                    } else if (sIsSM6225) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMREAD_SM6225);
                    } else if (sIsSM6375) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM6375);
                    } else if (sIsMT6983) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMREAD_MT6983);
                    } else if (sIsSM8250) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMREAD_SM8250);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_RANDOM_READ");
                        return;
                    }
                    return;
                case 19:
                    if (sIsSM8550) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMWRITE_SM8550);
                    } else if (sIsMT6895) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMWRITE_MT6895);
                    } else if (sIsSM6225) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMWRITE_SM6225);
                    } else if (sIsSM6375) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_SEQUENTIAL_READ_SM6375);
                    } else if (sIsMT6983) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMWRITE_MT6983);
                    } else if (sIsSM8250) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_RANDOMWRITE_SM8250);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_RANDOM_WRITE");
                        return;
                    }
                    return;
                case 20:
                    OplusRefreshRateInjectorImpl.enterPSMode(true);
                    if (sIsMT6989) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_UI_LAG_MT6989);
                    } else if (sIsSM8650) {
                        SystemProperties.set(SYSTEM_PROPERTIES_SPEC, BENCH_MODE_UI_LAG_SM8650);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_UX_FIRST_STEP");
                        return;
                    }
                    return;
                case 21:
                    SystemProperties.set(SYSTEM_PROPERTIES_SPEC, "2");
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_WEBVIEW");
                        return;
                    }
                    return;
                case 22:
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_QRCODE");
                        return;
                    }
                    return;
                case 44:
                    if (sIsSM8650) {
                        int i = sStepCount + 1;
                        sStepCount = i;
                        if (i > 1) {
                            sStepCount = 0;
                            sHandle = 0;
                            this.mHandler.sendEmptyMessageDelayed(1, 10000L);
                            this.mHandler.sendEmptyMessageDelayed(2, 175000L);
                            this.mHandler.sendEmptyMessageDelayed(3, 1000L);
                            if (z) {
                                Log.d(TAG, "send message");
                            }
                        }
                    }
                    if (sIsDx2) {
                        OplusRefreshRateInjectorImpl.enterPSMode(true);
                    } else {
                        OplusRefreshRateInjectorImpl.enterPSModeOnRate(true, 2);
                    }
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_FIRST_STEP");
                        return;
                    }
                    return;
                case 46:
                    OplusRefreshRateInjectorImpl.enterPSMode(false);
                    SystemProperties.set(SYSTEM_PROPERTIES_SPEC, "1");
                    if (z) {
                        Log.d(TAG, "BENCH_MARK_ANTUTU_FINISHED");
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static void handleCompactMemory() {
    }
}
