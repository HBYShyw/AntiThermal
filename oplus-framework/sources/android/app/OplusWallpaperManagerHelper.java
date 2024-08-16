package android.app;

import android.app.OplusUxIconConstants;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.OplusPropertyList;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Xml;
import com.oplus.multiuser.OplusMultiUserManager;
import com.oplus.os.OplusEnvironment;
import com.oplus.theme.OplusThemeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class OplusWallpaperManagerHelper {
    private static final String ATTRIBUTE_MIN_RAM_LIMIT = "minRamLimit";
    private static final String BASE_WALLPAPER_DEFAULT_DIR = "/decouping_wallpaper/";
    private static final String CUSTOM_LOCK_WALLPAPER_NAME = "default_wallpaper_lock";
    private static final int CUSTOM_THEME_FLAG = 256;
    private static final String CUSTOM_WALLPAPER_NAME = "default_wallpaper";
    private static final String DEFAULT_LOCK_WALLPAPER_NAME = "default_wallpaper_lock";
    private static final String DEFAULT_MULTI_SYS_WALLPAPER_NAME = "default_multi_sys_wallpaper";
    private static final String DEFAULT_PATH_THEME = "default";
    private static final String DEFAULT_WALLPAPER_NAME = "default_wallpaper";
    private static final long GB = 1073741824;
    private static final String OPERATOR = "operator";
    private static final String PHONE_COLOR_MAPS_FILE_NAME = "phone_color_default_theme_maps";
    private static final String PHONE_COLOR_MAPS_FILE_SUFFIX = ".xml";
    private static final String PROP_HW_PHONE_COLOR = "ro.hw.phone.color";
    private static final int RAM_UNIT_GAP = 1000;
    private static final String TAG = "OplusWallpaperManagerHelper";
    private static final String TAG_DEFAULT_LOCK_WALLPAPER = "DefaultWallpaperLock";
    private static final String TAG_DEFAULT_WALLPAPER = "DefaultWallpaper";
    private static final String TAG_DEFAULT_WALLPAPER_COMPONENT = "DefaultWallpaperComponent";
    private static final String TAG_PHONE_COLOR = "PhoneColor";
    private static final String WALLPAPER_CUSTOM_FILE_DIR = "/media/wallpaper/";
    private static final String WALLPAPER_SUFFIX = ".png";
    private static final String XML_ENCODING = "UTF-8";
    private static final String OPLUS_MODULE_DEFAULT_WALLPAPER_DIR = "/decouping_wallpaper/default/";
    private static final String BASE_PRODUCT_DEFAULT_DIR = OplusEnvironment.getOplusProductDirectory().getAbsolutePath() + OPLUS_MODULE_DEFAULT_WALLPAPER_DIR;
    private static final String CUSTOM_WALLPAPER = OplusEnvironment.getMyCompanyDirectory().getAbsolutePath();
    private static boolean sIsCurrentCustomTheme = false;
    private static String sCurrentCustomThemeDir = "default";
    private static Map<Integer, String> sDefaultWallpaperCache = new ArrayMap();
    private static ComponentName sDefaultWallpaperComponent = null;
    private static boolean sDefaultWallpaperComponentInited = false;

    public static InputStream openDefaultWallpaper(Context context, int which) {
        try {
            long startTime = SystemClock.uptimeMillis();
            InputStream inputStream = null;
            String defaultWallpaperFileName = getDefaultWallpaperFileName(context, which);
            if (!TextUtils.isEmpty(defaultWallpaperFileName)) {
                try {
                    inputStream = new FileInputStream(defaultWallpaperFileName);
                } catch (Exception e) {
                    inputStream = null;
                    Log.e(TAG, "openDefaultWallpaper. failed to open " + defaultWallpaperFileName + " , e = " + e);
                }
            }
            if (inputStream == null) {
                inputStream = openDefaultWallpaperFromRes(context, which);
            }
            long costTime = SystemClock.uptimeMillis() - startTime;
            Log.i(TAG, "openDefaultWallpaper. costTime = " + costTime + " , defaultWallpaperFileName = " + defaultWallpaperFileName + " , which = " + which);
            return inputStream;
        } catch (Exception e2) {
            Log.e(TAG, "openDefaultWallpaper: e = " + e2);
            return null;
        }
    }

    public static ComponentName getDefaultWallpaperComponent(Context context, int userId) {
        try {
            if (context == null) {
                Log.e(TAG, "getDefaultWallpaperComponent: The context is null.");
                return null;
            }
            ComponentName defaultWallpaper = null;
            if (isCurrentCustomTheme(userId) || isPacmanVersion()) {
                defaultWallpaper = findCustomDefaultDynamicWallpaper(context);
            }
            if (defaultWallpaper != null) {
                return defaultWallpaper;
            }
            if (!sDefaultWallpaperComponentInited) {
                String defaultWallpaperStr = getDefaultWallpaperComponetString(context);
                if (!TextUtils.isEmpty(defaultWallpaperStr)) {
                    ComponentName defaultWallpaper2 = ComponentName.unflattenFromString(defaultWallpaperStr);
                    Intent defaultWallpaperIntent = new Intent("android.service.wallpaper.WallpaperService");
                    defaultWallpaperIntent.setComponent(defaultWallpaper2);
                    if (context.getPackageManager().resolveService(defaultWallpaperIntent, 128) != null) {
                        sDefaultWallpaperComponent = defaultWallpaper2;
                    }
                }
                sDefaultWallpaperComponentInited = true;
            }
            Log.i(TAG, "getDefaultWallpaperComponent sDefaultWallpaperComponent = " + sDefaultWallpaperComponent);
            return sDefaultWallpaperComponent;
        } catch (Exception e) {
            Log.e(TAG, "getDefaultWallpaperComponent: e = " + e);
            return null;
        }
    }

    public static ComponentName getDefaultWallpaperComponent(Context context) {
        return getDefaultWallpaperComponent(context, context.getUserId());
    }

    private static boolean isCurrentCustomTheme(int userId) {
        String key = OplusUxIconConstants.SystemProperty.KEY_THEME_FLAG;
        if (userId > 0) {
            key = OplusUxIconConstants.SystemProperty.KEY_THEME_FLAG + "." + userId;
        }
        long themeFlag = SystemProperties.getLong(key, 0L);
        Log.i(TAG, "isCurrentCustomTheme. themeFlag = " + themeFlag);
        return (256 & themeFlag) != 0;
    }

    private static int getDefaultWallpaperResId(Context context) {
        return 201850902;
    }

    private static synchronized void addDefaultWallpaperCache(int which, String wallpaper) {
        synchronized (OplusWallpaperManagerHelper.class) {
            sDefaultWallpaperCache.put(Integer.valueOf(which), wallpaper);
        }
    }

    private static String getDefaultWallpaperFileName(Context context, int which) {
        int userId = context.getUserId();
        if (which == 2) {
            userId = getCurrentUserId(context);
        }
        Log.i(TAG, "getDefaultWallpaperFileName: userId = " + userId);
        if (isCurrentCustomTheme(userId) != sIsCurrentCustomTheme) {
            sDefaultWallpaperCache.clear();
            sIsCurrentCustomTheme = !sIsCurrentCustomTheme;
        }
        String currentCustomThemeDir = getCurrentCustomThemeDir(context, userId);
        if (!sCurrentCustomThemeDir.equalsIgnoreCase(currentCustomThemeDir)) {
            sDefaultWallpaperCache.clear();
            sCurrentCustomThemeDir = currentCustomThemeDir;
        }
        String cacheDefaultWallpaper = sDefaultWallpaperCache.get(Integer.valueOf(which));
        Log.i(TAG, "getDefaultWallpaperFileName cacheDefaultWallpaper = " + cacheDefaultWallpaper + " which = " + which);
        if (!TextUtils.isEmpty(cacheDefaultWallpaper)) {
            return cacheDefaultWallpaper;
        }
        String fileName = getMyEngineeringFileName(context, which);
        if (fileName == null && OplusMultiUserManager.getInstance().isMultiSystemUserId(userId) && !isCurrentCustomTheme(userId)) {
            fileName = getMultiSystemFileName(context, which);
        }
        if (fileName == null) {
            fileName = getModuleWallpaperFileName(context, which);
        }
        if (fileName == null) {
            fileName = getOperatorFileName(context, which);
        }
        if (fileName == null) {
            fileName = getCustomThemeFileName(context, which);
        }
        if (fileName == null) {
            fileName = getColorFileName(context, which);
        }
        if (fileName == null) {
            fileName = getNoColorFileName(context, which);
        }
        Log.d(TAG, "getDefaultWallpaperFileName final fileName = " + fileName);
        if (!TextUtils.isEmpty(fileName)) {
            addDefaultWallpaperCache(which, fileName);
        }
        return fileName;
    }

    private static String getMyEngineeringFileName(Context context, int which) {
        File engineerWallpaperDir = new File(OplusEnvironment.getMyEngineeringDirectory().getAbsolutePath() + "/media/wallpaper");
        File customWallpaper = new File(engineerWallpaperDir, "default_wallpaper.png");
        Log.d(TAG, "getMyEngineeringFileName. customWallpaper = " + customWallpaper.getAbsolutePath());
        if (customWallpaper.exists()) {
            Log.d(TAG, "getMyEngineeringFileName customWallpaper dir exist");
            return customWallpaper.getAbsolutePath();
        }
        Log.d(TAG, "getMyEngineeringFileName customWallpaper dir not exist");
        return null;
    }

    private static String getMultiSystemFileName(Context context, int which) {
        if (which != 1) {
            return null;
        }
        String fileName = BASE_PRODUCT_DEFAULT_DIR + DEFAULT_MULTI_SYS_WALLPAPER_NAME + ".png";
        File file = new File(fileName);
        Log.d(TAG, "getMultiSystemFileName default fileName = " + fileName);
        if (!file.exists()) {
            Log.d(TAG, "getMultiSystemFileName default fileName not exist");
            return null;
        }
        return fileName;
    }

    private static String getNoColorFileName(Context context, int which) {
        String fileName;
        if (which == 2) {
            fileName = BASE_PRODUCT_DEFAULT_DIR + "default_wallpaper_lock.png";
        } else {
            fileName = BASE_PRODUCT_DEFAULT_DIR + "default_wallpaper.png";
        }
        File file = new File(fileName);
        Log.d(TAG, "getNoColorFileName default fileName = " + fileName);
        if (!file.exists()) {
            Log.d(TAG, "getNoColorFileName default fileName not exist");
            return null;
        }
        return fileName;
    }

    private static String getColorFileName(Context context, int which) {
        String fileName = null;
        String hwPhoneColor = SystemProperties.get("ro.hw.phone.color");
        if (!TextUtils.isEmpty(hwPhoneColor)) {
            String[] wallpaper = findPhoneColorDefaultWallpaper(hwPhoneColor);
            if (wallpaper != null && !TextUtils.isEmpty(wallpaper[0]) && !TextUtils.isEmpty(wallpaper[1])) {
                StringBuilder sb = new StringBuilder();
                String str = BASE_PRODUCT_DEFAULT_DIR;
                String systemFileName = sb.append(str).append(wallpaper[0]).append(".png").toString();
                String lockFileName = str + wallpaper[1] + ".png";
                File systemFile = new File(systemFileName);
                File lockFile = new File(lockFileName);
                if (systemFile.exists()) {
                    addDefaultWallpaperCache(1, systemFileName);
                    if (which == 1) {
                        fileName = systemFileName;
                    }
                } else {
                    Log.d(TAG, "getColorFileName system not exist system =  " + systemFileName);
                }
                if (lockFile.exists()) {
                    addDefaultWallpaperCache(2, lockFileName);
                    if (which == 2) {
                        return lockFileName;
                    }
                    return fileName;
                }
                Log.d(TAG, "getColorFileName lock not exist  lock  = " + lockFileName);
                return fileName;
            }
            Log.d(TAG, "getColorFileName phoneColorDefaultTheme is empty");
            return null;
        }
        Log.d(TAG, "getColorFileName hwPhoneColor is empty");
        return null;
    }

    private static String getOperatorFileName(Context context, int which) {
        String fileName;
        String sysOperatorName = SystemProperties.get(OplusPropertyList.PROPERTY_OPLUS_OPERATOR);
        if (!TextUtils.isEmpty(sysOperatorName)) {
            String sysOperatorName2 = sysOperatorName.trim().toLowerCase();
            if (which == 2) {
                fileName = BASE_PRODUCT_DEFAULT_DIR + "default_wallpaper_lock_operator_" + sysOperatorName2 + ".png";
            } else {
                fileName = BASE_PRODUCT_DEFAULT_DIR + "default_wallpaper_operator_" + sysOperatorName2 + ".png";
            }
            File file = new File(fileName);
            Log.d(TAG, "getOperatorFileName operator fileName = " + fileName);
            if (!file.exists()) {
                Log.d(TAG, "getOperatorFileName operator not exist ");
                return null;
            }
            return fileName;
        }
        Log.d(TAG, "getOperatorFileName valid operator  " + sysOperatorName);
        return null;
    }

    private static String getCustomThemeFileName(Context context, int which) {
        int userId = context.getUserId();
        if (which == 2) {
            userId = getCurrentUserId(context);
        }
        Log.i(TAG, "getCustomThemeFileName: userId = " + userId);
        if (!isCurrentCustomTheme(userId)) {
            return null;
        }
        File customWallpaperDir = new File(OplusEnvironment.getMyCompanyDirectory().getAbsolutePath() + WALLPAPER_CUSTOM_FILE_DIR + getCurrentCustomThemeDir(context, userId));
        if (which == 2) {
            File customWallpaperLock = new File(customWallpaperDir, "default_wallpaper_lock.png");
            Log.d(TAG, "getCustomThemeFileName. customWallpaperLock = " + customWallpaperLock.getAbsolutePath());
            if (customWallpaperLock.exists()) {
                Log.d(TAG, "getCustomThemeFileName customWallpaperLock exist");
                return customWallpaperLock.getAbsolutePath();
            }
            Log.d(TAG, "getCustomThemeFileName customWallpaperLock not exist");
        } else {
            File customWallpaperSystem = new File(customWallpaperDir, "default_wallpaper.png");
            Log.d(TAG, "getCustomThemeFileName. customWallpaperSystem = " + customWallpaperSystem.getAbsolutePath());
            if (customWallpaperSystem.exists()) {
                Log.d(TAG, "getCustomThemeFileName customWallpaperSystem exist");
                return customWallpaperSystem.getAbsolutePath();
            }
            Log.d(TAG, "getCustomThemeFileName customWallpaperSystem not exist");
        }
        return null;
    }

    private static InputStream openDefaultWallpaperFromRes(Context context, int which) {
        return context.getResources().openRawResource(getDefaultWallpaperResId(context));
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:12:0x0095. Please report as an issue. */
    private static String[] findPhoneColorDefaultWallpaper(String phoneColorName) {
        StringBuilder sb;
        if (TextUtils.isEmpty(phoneColorName)) {
            Log.d(TAG, "findPhoneColorDefaultWallpaper: The phoneColorName is empty!");
            return null;
        }
        String[] defaultTheme = new String[2];
        InputStream inputStream = null;
        File phoneColorMapFile = null;
        try {
            if (0 == 0) {
                try {
                    phoneColorMapFile = new File(BASE_PRODUCT_DEFAULT_DIR + PHONE_COLOR_MAPS_FILE_NAME + PHONE_COLOR_MAPS_FILE_SUFFIX);
                    if (!phoneColorMapFile.exists()) {
                        Log.e(TAG, "findPhoneColorDefaultWallpaper: The phone color map file is not exists!");
                        return null;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "findPhoneColorDefaultWallpaper: e = " + e);
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Exception e2) {
                            e = e2;
                            sb = new StringBuilder();
                            Log.e(TAG, sb.append("findPhoneColorDefaultWallpaper: Closing inputStream. e = ").append(e).toString());
                            Log.i(TAG, "findPhoneColorDefaultWallpaper: defaultTheme = " + defaultTheme[0] + " defaultTheme[]");
                            return defaultTheme;
                        }
                    }
                }
            }
            Log.i(TAG, "findPhoneColorDefaultWallpaper: phoneColorMapFile = " + phoneColorMapFile);
            InputStream inputStream2 = new FileInputStream(phoneColorMapFile);
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream2, XML_ENCODING);
            String foundColorName = null;
            for (int event = pullParser.getEventType(); event != 1; event = pullParser.next()) {
                switch (event) {
                    case 2:
                        String pullParserName = pullParser.getName();
                        if (TAG_PHONE_COLOR.equals(pullParserName)) {
                            String colorName = new String(pullParser.getAttributeValue(0));
                            if (phoneColorName.equalsIgnoreCase(colorName)) {
                                foundColorName = colorName;
                            } else {
                                foundColorName = null;
                                defaultTheme[0] = null;
                                defaultTheme[1] = null;
                            }
                        }
                        if (foundColorName != null) {
                            if (TAG_DEFAULT_WALLPAPER.equals(pullParserName)) {
                                defaultTheme[0] = pullParser.nextText();
                                break;
                            } else if (TAG_DEFAULT_LOCK_WALLPAPER.equals(pullParserName)) {
                                defaultTheme[1] = pullParser.nextText();
                                break;
                            }
                        }
                        break;
                }
                if (TextUtils.isEmpty(defaultTheme[0]) || TextUtils.isEmpty(defaultTheme[1])) {
                }
            }
            try {
                inputStream2.close();
            } catch (Exception e3) {
                e = e3;
                sb = new StringBuilder();
                Log.e(TAG, sb.append("findPhoneColorDefaultWallpaper: Closing inputStream. e = ").append(e).toString());
                Log.i(TAG, "findPhoneColorDefaultWallpaper: defaultTheme = " + defaultTheme[0] + " defaultTheme[]");
                return defaultTheme;
            }
            Log.i(TAG, "findPhoneColorDefaultWallpaper: defaultTheme = " + defaultTheme[0] + " defaultTheme[]");
            return defaultTheme;
        } finally {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (Exception e4) {
                    Log.e(TAG, "findPhoneColorDefaultWallpaper: Closing inputStream. e = " + e4);
                }
            }
        }
    }

    public static String getModuleWallpaperFileName(Context context, int which) {
        try {
            String[] imgMouleDirs = {OplusEnvironment.getMyCompanyDirectory().getAbsolutePath(), OplusEnvironment.getMyCarrierDirectory().getAbsolutePath(), OplusEnvironment.getMyBigballDirectory().getAbsolutePath()};
            for (String wallpaperDir : imgMouleDirs) {
                Log.d(TAG, "getModuleWallpaperFileName:current module: " + wallpaperDir);
                File oplusCustomWallpaperDir = new File(wallpaperDir + OPLUS_MODULE_DEFAULT_WALLPAPER_DIR);
                if (which == 2) {
                    File oplusCustomWallpaperLock = new File(oplusCustomWallpaperDir, "default_wallpaper_lock.png");
                    if (oplusCustomWallpaperLock.exists()) {
                        Log.d(TAG, "getModuleWallpaperFileName WallpaperLock exist in this module");
                        return oplusCustomWallpaperLock.getAbsolutePath();
                    }
                    Log.d(TAG, "getModuleWallpaperFileName WallpaperLock not exist in this module");
                } else {
                    File oplusCustomWallpaperSystem = new File(oplusCustomWallpaperDir, "default_wallpaper.png");
                    if (oplusCustomWallpaperSystem.exists()) {
                        Log.d(TAG, "getModuleWallpaperFileName WallpaperSystem exist in this module");
                        return oplusCustomWallpaperSystem.getAbsolutePath();
                    }
                    Log.d(TAG, "getModuleWallpaperFileName WallpaperSystem not exist in this module");
                }
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getModuleWallpaperFileName: e = " + e);
            return null;
        }
    }

    public static boolean checkCustomizeWallpaperDir() {
        try {
            String[] imgMouleDirs = {OplusEnvironment.getMyCompanyDirectory().getAbsolutePath(), OplusEnvironment.getMyCarrierDirectory().getAbsolutePath(), OplusEnvironment.getMyBigballDirectory().getAbsolutePath()};
            for (String wallpaperDir : imgMouleDirs) {
                Log.d(TAG, "checkCustomizeWallpaperDir:current module: " + wallpaperDir);
                File oplusCustomWallpaperDir = new File(wallpaperDir + OPLUS_MODULE_DEFAULT_WALLPAPER_DIR);
                if (oplusCustomWallpaperDir.exists()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Log.e(TAG, "checkCustomizeWallpaperDir: e = " + e);
            return false;
        }
    }

    private static String getDefaultWallpaperComponetString(Context context) {
        String hwPhoneColor = SystemProperties.get("ro.hw.phone.color");
        return findPhoneColorDefaultWallpaperComponet(hwPhoneColor);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:23:0x008c. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0133  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String findPhoneColorDefaultWallpaperComponet(String phoneColorName) {
        StringBuilder sb;
        File phoneColorMapFile;
        if (TextUtils.isEmpty(phoneColorName)) {
            Log.i(TAG, "findPhoneColorDefaultWallpaperComponet: The phoneColorName is empty!");
        }
        String defaultWallpaperComponet = null;
        String noColorDefaultWallpaperComponet = null;
        InputStream inputStream = null;
        try {
            try {
                phoneColorMapFile = new File(BASE_PRODUCT_DEFAULT_DIR + PHONE_COLOR_MAPS_FILE_NAME + PHONE_COLOR_MAPS_FILE_SUFFIX);
            } catch (Exception e) {
                Log.e(TAG, "findPhoneColorDefaultWallpaperComponet: e = " + e);
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        e = e2;
                        sb = new StringBuilder();
                        Log.e(TAG, sb.append("findPhoneColorDefaultWallpaperComponet: Closing inputStream. e = ").append(e).toString());
                        if (TextUtils.isEmpty(defaultWallpaperComponet)) {
                        }
                        Log.i(TAG, "findPhoneColorDefaultWallpaperComponet: defaultWallpaperComponet = " + defaultWallpaperComponet);
                        return defaultWallpaperComponet;
                    }
                }
            }
            if (!phoneColorMapFile.exists()) {
                Log.e(TAG, "findPhoneColorDefaultWallpaperComponet: The phone color map file is not exists!");
                if (0 == 0) {
                    return null;
                }
                try {
                    inputStream.close();
                    return null;
                } catch (Exception e3) {
                    Log.e(TAG, "findPhoneColorDefaultWallpaperComponet: Closing inputStream. e = " + e3);
                    return null;
                }
            }
            Log.i(TAG, "findPhoneColorDefaultWallpaperComponet: phoneColorMapFile = " + phoneColorMapFile);
            InputStream inputStream2 = new FileInputStream(phoneColorMapFile);
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream2, XML_ENCODING);
            boolean foundColorName = false;
            boolean foundWallpaperName = false;
            boolean inPhoneColorTag = false;
            for (int eventType = xmlPullParser.getEventType(); eventType != 1; eventType = xmlPullParser.next()) {
                switch (eventType) {
                    case 2:
                        String tagName = xmlPullParser.getName();
                        if (TAG_PHONE_COLOR.equals(tagName)) {
                            inPhoneColorTag = true;
                            String colorName = new String(xmlPullParser.getAttributeValue(0));
                            if (phoneColorName.equalsIgnoreCase(colorName)) {
                                foundColorName = true;
                            }
                        } else if (TAG_DEFAULT_WALLPAPER_COMPONENT.equals(tagName)) {
                            if (isNotReachingMinRamLimit(xmlPullParser)) {
                                foundColorName = false;
                                break;
                            } else if (foundColorName) {
                                defaultWallpaperComponet = xmlPullParser.nextText();
                                foundWallpaperName = true;
                                break;
                            } else if (!inPhoneColorTag) {
                                noColorDefaultWallpaperComponet = xmlPullParser.nextText();
                                if (TextUtils.isEmpty(phoneColorName)) {
                                    foundWallpaperName = true;
                                    break;
                                }
                            }
                        }
                        break;
                    case 3:
                        if (TAG_PHONE_COLOR.equals(xmlPullParser.getName())) {
                            inPhoneColorTag = false;
                            break;
                        }
                        break;
                }
                if (!foundWallpaperName) {
                }
            }
            try {
                inputStream2.close();
            } catch (Exception e4) {
                e = e4;
                sb = new StringBuilder();
                Log.e(TAG, sb.append("findPhoneColorDefaultWallpaperComponet: Closing inputStream. e = ").append(e).toString());
                if (TextUtils.isEmpty(defaultWallpaperComponet)) {
                }
                Log.i(TAG, "findPhoneColorDefaultWallpaperComponet: defaultWallpaperComponet = " + defaultWallpaperComponet);
                return defaultWallpaperComponet;
            }
            if (TextUtils.isEmpty(defaultWallpaperComponet)) {
                defaultWallpaperComponet = noColorDefaultWallpaperComponet;
            }
            Log.i(TAG, "findPhoneColorDefaultWallpaperComponet: defaultWallpaperComponet = " + defaultWallpaperComponet);
            return defaultWallpaperComponet;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (Exception e5) {
                    Log.e(TAG, "findPhoneColorDefaultWallpaperComponet: Closing inputStream. e = " + e5);
                }
            }
            throw th;
        }
    }

    private static boolean isReachingMinRamLimit(XmlPullParser xmlPullParser) {
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            if (ATTRIBUTE_MIN_RAM_LIMIT.equals(attributeName)) {
                String ramLimitStr = xmlPullParser.getAttributeValue(i);
                int ramLimit = parseRamLimit(ramLimitStr);
                int ram = obtainRam();
                Log.i(TAG, "isReachingMinRamLimit limit=" + ramLimit + " ram=" + ram);
                return ram == -1 || ram >= ramLimit;
            }
        }
        return true;
    }

    private static boolean isNotReachingMinRamLimit(XmlPullParser xmlPullParser) {
        return !isReachingMinRamLimit(xmlPullParser);
    }

    private static int parseRamLimit(String ramLimitStr) {
        if (TextUtils.isEmpty(ramLimitStr)) {
            Log.e(TAG, "parseDeviceRamLimit empty str");
            return 0;
        }
        try {
            return Integer.decode(ramLimitStr).intValue();
        } catch (NumberFormatException e) {
            Log.e(TAG, "parseDeviceRamLimit decode error, " + e.getMessage());
            return 0;
        }
    }

    private static int obtainRam() {
        try {
            Class clazz = Class.forName("android.os.Process");
            Method method = clazz.getMethod("getTotalMemory", new Class[0]);
            long totalMemory = ((Long) method.invoke(null, new Object[0])).longValue();
            long totalPhysicalMemory = ((GB + totalMemory) - 1) & (-1073741824);
            long ramMB = (totalPhysicalMemory / 1000) / 1000;
            return Math.round(((float) ramMB) / 1000.0f);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    public static boolean needDefaultImageWallpaper(Context context, int userId) {
        if (context == null) {
            Log.e(TAG, "needDefaultImageWallpaper: context is null");
            return true;
        }
        Log.i(TAG, "needDefaultImageWallpaper: userId is " + userId);
        if (!TextUtils.isEmpty(getMyEngineeringFileName(context, 1)) || checkCustomizeWallpaperDir()) {
            return true;
        }
        if (isCurrentCustomTheme(userId) || isPacmanVersion()) {
            return !getCustomLiveWallpaperConfig().exists();
        }
        return OplusMultiUserManager.getInstance().isMultiSystemUserId(userId);
    }

    public static boolean isCustomThemeStaticWallpaper(Context context, int userId) {
        return isCurrentCustomTheme(userId) && findCustomDefaultDynamicWallpaper(context) == null;
    }

    public static File getCustomLiveWallpaperConfig() {
        return new File(CUSTOM_WALLPAPER + WALLPAPER_CUSTOM_FILE_DIR + "default", "phone_color_default_theme_maps.xml");
    }

    public static String getCurrentCustomThemeDir(Context context, int userId) {
        String customThemeDir = "default";
        if (context == null) {
            Log.e(TAG, "getCurrentCustomThemeDir: context is null");
            return "default";
        }
        try {
            String customThemePath = Settings.System.getStringForUser(context.getContentResolver(), OplusThemeUtil.CUSTOM_THEME_PATH_SETTING, userId);
            if (!TextUtils.isEmpty(customThemePath) && !customThemePath.equalsIgnoreCase(OplusThemeUtil.CUSTOM_THEME_PATH)) {
                if (customThemePath.endsWith("/")) {
                    customThemePath = customThemePath.substring(0, customThemePath.length() - 1);
                }
                String tempDir = customThemePath.substring(customThemePath.lastIndexOf("/") + 1);
                if (!TextUtils.isEmpty(tempDir)) {
                    customThemeDir = tempDir;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getCurrentCustomThemeDir: e = " + e);
            customThemeDir = "default";
        }
        Log.i(TAG, "getCurrentCustomThemeDir: customThemeDir = " + customThemeDir);
        return customThemeDir;
    }

    public static boolean isCtsTest() {
        return !SystemProperties.getBoolean("persist.sys.permission.enable", true);
    }

    private static int getCurrentUserId(Context context) {
        try {
            int userId = ActivityManager.getCurrentUser();
            return userId;
        } catch (Exception e) {
            Log.w(TAG, "getCurrentUserId: e = " + e);
            if (context == null) {
                return 0;
            }
            int userId2 = context.getUserId();
            return userId2;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:19:0x005f. Please report as an issue. */
    private static ComponentName findCustomDefaultDynamicWallpaper(Context context) {
        StringBuilder sb;
        File phoneColorMapFile;
        ComponentName defaultWallpaper = null;
        InputStream inputStream = null;
        try {
            try {
                phoneColorMapFile = getCustomLiveWallpaperConfig();
            } catch (Exception e) {
                Log.e(TAG, "findCustomDefaultDynamicWallpaper: e = " + e);
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Exception e2) {
                        e = e2;
                        sb = new StringBuilder();
                        Log.e(TAG, sb.append("findCustomDefaultDynamicWallpaper: Closing inputStream. e = ").append(e).toString());
                        Log.i(TAG, "findCustomDefaultDynamicWallpaper: defaultWallpaper = " + defaultWallpaper);
                        return defaultWallpaper;
                    }
                }
            }
            if (!phoneColorMapFile.exists()) {
                Log.e(TAG, "findCustomDefaultDynamicWallpaper: The phone color map file is not exists!");
                if (0 == 0) {
                    return null;
                }
                try {
                    inputStream.close();
                    return null;
                } catch (Exception e3) {
                    Log.e(TAG, "findCustomDefaultDynamicWallpaper: Closing inputStream. e = " + e3);
                    return null;
                }
            }
            Log.i(TAG, "findCustomDefaultDynamicWallpaper: phoneColorMapFile = " + phoneColorMapFile);
            InputStream inputStream2 = new FileInputStream(phoneColorMapFile);
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setInput(inputStream2, XML_ENCODING);
            for (int event = pullParser.getEventType(); event != 1; event = pullParser.next()) {
                switch (event) {
                    case 2:
                        String pullParserName = pullParser.getName();
                        if (TAG_DEFAULT_WALLPAPER_COMPONENT.equals(pullParserName) && !isNotReachingMinRamLimit(pullParser)) {
                            String defaultWallpaperComponentStr = pullParser.nextText();
                            Log.i(TAG, "findCustomDefaultDynamicWallpaper: defaultWallpaperComponentStr = " + defaultWallpaperComponentStr);
                            ComponentName defaultWallpaperComp = ComponentName.unflattenFromString(defaultWallpaperComponentStr);
                            if (defaultWallpaperComp != null && isWallpaperExist(context, defaultWallpaperComp)) {
                                defaultWallpaper = defaultWallpaperComp;
                            }
                            break;
                        }
                        break;
                }
            }
            try {
                inputStream2.close();
            } catch (Exception e4) {
                e = e4;
                sb = new StringBuilder();
                Log.e(TAG, sb.append("findCustomDefaultDynamicWallpaper: Closing inputStream. e = ").append(e).toString());
                Log.i(TAG, "findCustomDefaultDynamicWallpaper: defaultWallpaper = " + defaultWallpaper);
                return defaultWallpaper;
            }
            Log.i(TAG, "findCustomDefaultDynamicWallpaper: defaultWallpaper = " + defaultWallpaper);
            return defaultWallpaper;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    inputStream.close();
                } catch (Exception e5) {
                    Log.e(TAG, "findCustomDefaultDynamicWallpaper: Closing inputStream. e = " + e5);
                }
            }
            throw th;
        }
    }

    private static boolean isWallpaperExist(Context context, ComponentName wallpaper) {
        try {
            Intent wallpaperIntent = new Intent("android.service.wallpaper.WallpaperService");
            wallpaperIntent.setComponent(wallpaper);
            return context.getPackageManager().resolveService(wallpaperIntent, 128) != null;
        } catch (Exception e) {
            Log.e(TAG, "isWallpaperExist: e = " + e);
            return false;
        }
    }

    private static boolean isPacmanVersion() {
        String name = SystemProperties.get(OplusPropertyList.PROPERTY_OPLUS_VENDOR_MARKET_NAME, "");
        boolean isPacman = "OnePlus Nord 2 PAC-MAN ED".equals(name);
        Log.i(TAG, "isPacmanVersion. isPacman = " + isPacman);
        return isPacman;
    }
}
