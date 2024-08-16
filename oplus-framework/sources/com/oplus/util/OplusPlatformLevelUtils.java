package com.oplus.util;

import android.content.Context;
import android.net.wifi.OplusWifiManager;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class OplusPlatformLevelUtils {
    private static final long GB = 1073741824;
    public static final int LEVEL_HIGH = 3;
    public static final int LEVEL_LOW = 1;
    public static final int LEVEL_MIDDLE = 2;
    public static final int LEVEL_TYPE_FOR_CPU = 2;
    public static final int LEVEL_TYPE_FOR_GPU = 3;
    public static final int LEVEL_TYPE_FOR_RAM = 1;
    public static final int LEVEL_UNDEF = -1;
    private static final int PLATFORM_INFO_LENGTH = 6;
    private static final String REGEX = "^(c\\:[1-3]\\,g\\:[1-3]){1}$";
    private static final String TAG = "OplusPlatformLevelUtils";
    private static final int THRESHOLD_HIGH_RAM = 12;
    private static final int THRESHOLD_LOW_RAM = 4;
    private Context mContext;
    public static final boolean IS_LIGHT_OS = SystemProperties.getBoolean("ro.oplus.lightos", false);
    public static final int TOTAL_RAM = obtainRam();
    private static final String PLATFORM_INFO = SystemProperties.get("ro.soc.model", "");
    private static final int ANIMATION_LEVEL = SystemProperties.getInt("ro.oplus.animationlevel", 0);
    private static final int GAUSSIAN_LEVEL = SystemProperties.getInt("ro.oplus.gaussianlevel", 0);
    private static int sRamLevel = -1;
    private static int sCpuLevel = -1;
    private static int sGpuLevel = -1;
    private static volatile OplusPlatformLevelUtils sInstance = null;

    private OplusPlatformLevelUtils(Context context) {
        this.mContext = context;
        if (context == null) {
            Log.e(TAG, "OplusPlatformLevelUtils mContext is null!");
        }
        if (sRamLevel == -1 || sCpuLevel == -1 || sGpuLevel == -1) {
            initPlatformLevel();
        }
    }

    public static OplusPlatformLevelUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OplusPlatformLevelUtils.class) {
                if (sInstance == null) {
                    sInstance = new OplusPlatformLevelUtils(context);
                }
            }
        }
        return sInstance;
    }

    private void initPlatformLevel() {
        sRamLevel = initPlatformRamLevel();
        String platformLevelList = parseDefaultPlatformList();
        parsePlatformLevelList(platformLevelList);
    }

    private boolean isLegalString(String regex, String str) {
        boolean isMatch = Pattern.matches(regex, str);
        return isMatch;
    }

    private String getUnifiedPlatformInfo() {
        String platformInfo = PLATFORM_INFO.toUpperCase();
        if (platformInfo.startsWith("MT") || platformInfo.startsWith("SM") || platformInfo.startsWith("SDM")) {
            return platformInfo.substring(0, 6);
        }
        if (platformInfo.startsWith("MSM")) {
            return platformInfo.substring(0, 7);
        }
        return platformInfo;
    }

    private String parseDefaultPlatformList() {
        String platformInfoForCpu;
        String platformInfoForGpu;
        String[] highCpuList = getPlatformList(201785406);
        String[] middleCpuList = getPlatformList(201785408);
        String[] lowCpuList = getPlatformList(201785410);
        String[] highGpuList = getPlatformList(201785407);
        String[] middleGpuList = getPlatformList(201785409);
        String[] lowGpuList = getPlatformList(201785411);
        String platformInfo = getUnifiedPlatformInfo();
        if (ArrayUtils.contains(highCpuList, platformInfo)) {
            platformInfoForCpu = platformLevelConcat("c", 3);
        } else if (ArrayUtils.contains(middleCpuList, platformInfo)) {
            platformInfoForCpu = platformLevelConcat("c", 2);
        } else {
            platformInfoForCpu = ArrayUtils.contains(lowCpuList, platformInfo) ? platformLevelConcat("c", 1) : platformLevelConcat("c", -1);
        }
        if (ArrayUtils.contains(highGpuList, platformInfo)) {
            platformInfoForGpu = platformLevelConcat("g", 3);
        } else if (ArrayUtils.contains(middleGpuList, platformInfo)) {
            platformInfoForGpu = platformLevelConcat("g", 2);
        } else {
            platformInfoForGpu = ArrayUtils.contains(lowGpuList, platformInfo) ? platformLevelConcat("g", 1) : platformLevelConcat("g", -1);
        }
        String platformLevelList = platformInfoForCpu + "," + platformInfoForGpu;
        try {
            SystemProperties.set("persist.sys.oplus.platformlevel", platformLevelList);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return platformLevelList;
    }

    private void parsePlatformLevelList(String str) {
        List<String> platformLevelList = Arrays.asList(str.split(","));
        List<String> cpuLevelList = Arrays.asList(platformLevelList.get(0).split(":"));
        List<String> gpuLevelList = Arrays.asList(platformLevelList.get(1).split(":"));
        sCpuLevel = Integer.parseInt(cpuLevelList.get(1));
        sGpuLevel = Integer.parseInt(gpuLevelList.get(1));
    }

    private String[] getPlatformList(int id) {
        return this.mContext.getResources().getStringArray(id);
    }

    private String platformLevelConcat(String type, int level) {
        return type + ":" + level;
    }

    private static int obtainRam() {
        try {
            Class clazz = Class.forName("android.os.Process");
            Method method = clazz.getMethod("getTotalMemory", new Class[0]);
            long totalMemory = ((Long) method.invoke(null, new Object[0])).longValue();
            long totalPhysicalMemory = ((GB + totalMemory) - 1) & (-1073741824);
            int ramG = (int) (((totalPhysicalMemory / OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect) / OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect) / OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect);
            return ramG;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    private int initPlatformRamLevel() {
        int i = TOTAL_RAM;
        if (i <= 4) {
            return 1;
        }
        if (i >= 12) {
            return 3;
        }
        return 2;
    }

    private int getPlatformRamLevel() {
        if (sRamLevel == -1) {
            Log.e(TAG, "ram level not inited");
        }
        return sRamLevel;
    }

    private int getPlatformCpuLevel() {
        if (sCpuLevel == -1) {
            Log.e(TAG, "cpu level not set!!please set in res file first!!");
        }
        return sCpuLevel;
    }

    private int getPlatformGpuLevel() {
        if (sGpuLevel == -1) {
            Log.e(TAG, "gpu level not set!!please set in res file first!!");
        }
        return sGpuLevel;
    }

    public int getPlatformLevel(int type) {
        if (type == 1) {
            return getPlatformRamLevel();
        }
        if (type == 2) {
            return getPlatformCpuLevel();
        }
        if (type == 3) {
            return getPlatformGpuLevel();
        }
        return -1;
    }

    public int getPlatformAnimationLevel() {
        int i = ANIMATION_LEVEL;
        if (i >= 1 && i <= 3) {
            return i;
        }
        if (IS_LIGHT_OS) {
            return 1;
        }
        int i2 = sGpuLevel;
        if (i2 == 2 || i2 == 3) {
            return i2;
        }
        return 3;
    }

    public int getPlatformGaussianLevel() {
        int i = GAUSSIAN_LEVEL;
        if (i >= 1 && i <= 3) {
            return i;
        }
        int i2 = sGpuLevel;
        if (i2 != -1) {
            return i2;
        }
        return 3;
    }
}
