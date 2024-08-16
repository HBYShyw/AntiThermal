package com.oplus.util;

import android.app.ActivityManager;
import android.app.OplusActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.OplusManager;
import android.os.OplusPropertyList;
import android.os.OplusSystemProperties;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.content.PackageMonitor;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.widget.OplusMaxLinearLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class OplusDisplayCompatUtils {
    public static final float COMPAT_ASPECT_RATIO = 1.7778f;
    private static final int CUTOUT_MODE_DEFAULT = 0;
    private static final int CUTOUT_MODE_HIDE = 2;
    private static final int CUTOUT_MODE_SHOW = 1;
    private static final int[] CUTOUT_MODE_SUPPLIERS_TYPES;
    private static final List<String> DEFAULT_FOLD_SCREEN_AUTO_MATCH_LIST;
    private static final List<String> DEFAULT_FOLD_SCREEN_FORCE_AUTO_MATCH_LIST;
    private static final List<String> DEFAULT_FOLD_SCREEN_FORCE_NON_HIDE_CUTOUT_LIST;
    private static final List<String> DEFAULT_FOLD_SCREEN_NON_FORCE_IMMERSIVE_LIST;
    private static final float DEFAULT_MAX_ASPECT_RATIO = 2.0f;
    private static final List<String> DEFAULT_NON_IMMERSIVE_LIST;
    private static final List<String> DEFAULT_NO_COMPAT_LIST;
    private static final HashMap<Integer, Pair<Integer, Integer>> DEVICES_MAP;
    private static final int DISPLAY_CUTOUT_POSITION_LEFT = 1;
    private static final int DISPLAY_CUTOUT_POSITION_MIDDLE = 2;
    private static final int DISPLAY_CUTOUT_POSITION_NONE = 0;
    private static final int FOLD_MEDIUM_MAX_SIZE_DP = 840;
    private static final int FOLD_SMALL_MAX_SIZE_DP = 600;
    private static final int FOLD_SMALL_MIN_SIZE_DP = 0;
    private static final String KEY_APP_LIST_CUTOUT_DEFAULT = "key_display_nonimmersive_local_apps";
    private static final String KEY_APP_LIST_CUTOUT_HIDE = "cutout_hide_app_list";
    private static final String KEY_APP_LIST_CUTOUT_SHOW = "key_display_immersive_local_apps";
    private static final String KEY_LOCAL_COMPAT_APPS = "key_display_compat_local_apps_v1";
    private static final String KEY_LOCAL_FULLSCREEN_APPS = "key_display_fullscreen_local_apps_v1";
    private static final String KEY_SHOW_FULLSCREEN_DIALOG_APPS = "key_display_show_dialog_local_apps";
    private static final String OPLUS_DISPLAY_COMPAT_CONFIG_DIR = "/data/oplus/os/displaycompat";
    private static final String OPLUS_DISPLAY_COMPAT_CONFIG_FILE_PATH = "/data/oplus/os/displaycompat/sys_display_compat_config.xml";
    public static final int OPLUS_LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHOW = 5;
    private static final String OPLUS_PERMISSION_SAFE_WINDOW = "com.oplus.permission.safe.WINDOW";
    public static final int SCREEN_TYPE_DEFAULT = 1;
    public static final int SCREEN_TYPE_LARGE = 3;
    public static final int SCREEN_TYPE_MEDIUM = 2;
    public static final int SCREEN_TYPE_SMALL = 1;
    private static final String TAG = "OplusDisplayCompatUtils";
    private static final String TAG_ENABLE = "enable_display_compat";
    private static final String TAG_ENABLE_IMMERSIVE = "enable_display_immersive";
    private static final String TAG_FOLD_SCREEN_PREFIX = "fold_";
    private static final Map<String, Integer> TAG_TO_TYPE;
    private static final Set<String> THIRD_PARTY_APP_EXCLUDE;
    private static final Set<String> THIRD_PARTY_APP_INCLUDE;
    private static final Map<Integer, Integer> TYPE_TO_CUTOUT_MODE;
    private static volatile OplusDisplayCompatUtils sDisplayCompatUtils;
    private ConfigFileObserver mConfigFileObserver;
    private boolean mIsSystem;
    private SettingsContentObserver mSettingsContentObserver;
    public static final boolean SUPPORT_REVISE_SQUARE_DISPLAY_ORIENTATION = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_REVISE_SQUARE_DISPLAY_ORIENTATION);
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final boolean IS_FOLD_SCREEN = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_DEFAULT_TOP_DISPLAY);
    private final OplusDisplayCompatData mCompatData = new OplusDisplayCompatData();
    private final Object mLock = new Object();
    private final MyPackageMonitor mMyPackageMonitor = new MyPackageMonitor();
    private Context mContext = null;
    private PackageManager mPackageManager = null;

    static {
        HashSet hashSet = new HashSet();
        THIRD_PARTY_APP_INCLUDE = hashSet;
        HashSet hashSet2 = new HashSet();
        THIRD_PARTY_APP_EXCLUDE = hashSet2;
        ArrayList arrayList = new ArrayList();
        DEFAULT_FOLD_SCREEN_NON_FORCE_IMMERSIVE_LIST = arrayList;
        ArrayList arrayList2 = new ArrayList();
        DEFAULT_FOLD_SCREEN_FORCE_AUTO_MATCH_LIST = arrayList2;
        ArrayList arrayList3 = new ArrayList();
        DEFAULT_FOLD_SCREEN_FORCE_NON_HIDE_CUTOUT_LIST = arrayList3;
        ArrayList arrayList4 = new ArrayList();
        DEFAULT_NO_COMPAT_LIST = arrayList4;
        ArrayList arrayList5 = new ArrayList();
        DEFAULT_NON_IMMERSIVE_LIST = arrayList5;
        ArrayList arrayList6 = new ArrayList();
        DEFAULT_FOLD_SCREEN_AUTO_MATCH_LIST = arrayList6;
        HashMap hashMap = new HashMap();
        TAG_TO_TYPE = hashMap;
        CUTOUT_MODE_SUPPLIERS_TYPES = new int[]{14, 4, 5, 6, 18, 16, 17, 10, 11};
        HashMap hashMap2 = new HashMap();
        TYPE_TO_CUTOUT_MODE = hashMap2;
        HashMap<Integer, Pair<Integer, Integer>> hashMap3 = new HashMap<>();
        DEVICES_MAP = hashMap3;
        sDisplayCompatUtils = null;
        hashMap3.put(1, new Pair<>(0, 600));
        Integer valueOf = Integer.valueOf(FOLD_MEDIUM_MAX_SIZE_DP);
        hashMap3.put(2, new Pair<>(600, valueOf));
        hashMap3.put(3, new Pair<>(valueOf, Integer.valueOf(OplusMaxLinearLayout.INVALID_MAX_VALUE)));
        hashSet.addAll(Arrays.asList("com.nearme.gamecenter.ddz.nearme.gamecenter", "com.google.android.inputmethod.latin", "jp.softbank.mb.parentalcontrols", "com.android.vending", "com.android.chrome", "com.google.android.dialer", "com.android.permissioncontroller", "com.google.android.permissioncontroller", OplusManager.GMAP_PNAME, "com.heytap.yoli", "com.google.android.gms", "com.felicanetworks.mfs", "com.justsafe.seed"));
        hashSet2.addAll(Arrays.asList("com.android.calculator2", "com.android.calendar", "com.ctsi.emm", "com.justsy.launcher", "com.justsy.portal", "com.justsy.mdm"));
        arrayList.addAll(Arrays.asList("com.meitu.meiyancamera", "com.adobe.reader", "com.smile.gifmaker", "com.facebook.katana", "com.baidu.searchbox", "cn.cntv", "com.instagram.android"));
        arrayList2.addAll(Arrays.asList("com.tencent.mm"));
        arrayList3.addAll(Arrays.asList("com.tencent.mm"));
        arrayList6.addAll(Arrays.asList("com.meitu.meiyancamera"));
        arrayList4.addAll(Arrays.asList("com.justsy.launcher", "com.justsy.portal", "com.justsy.mdm", "com.ctsi.emm"));
        arrayList5.addAll(Arrays.asList("com.walkgame.ismarttv", "net.fetnet.fetvod", "com.justsy.launcher", "com.justsy.portal", "com.justsy.mdm"));
        hashMap.put("white", 0);
        hashMap.put("black", 1);
        hashMap.put("immersive", 10);
        hashMap.put("nonimmersive", 11);
        hashMap.put("fold_auto_match", 18);
        hashMap.put("fold_non_immersive", 17);
        hashMap.put("fold_immersive", 16);
        hashMap.put("fold_non_force_immersive", 13);
        hashMap.put("fold_force_auto_match", 14);
        hashMap.put("fold_force_non_hide_cutout", 14);
        hashMap2.put(14, 0);
        hashMap2.put(4, 0);
        hashMap2.put(5, 1);
        hashMap2.put(6, 2);
        hashMap2.put(18, 0);
        hashMap2.put(16, 1);
        hashMap2.put(17, 2);
        hashMap2.put(10, 1);
        hashMap2.put(11, 2);
    }

    private OplusDisplayCompatUtils() {
    }

    public static OplusDisplayCompatUtils getInstance() {
        if (sDisplayCompatUtils == null) {
            synchronized (OplusDisplayCompatUtils.class) {
                if (sDisplayCompatUtils == null) {
                    sDisplayCompatUtils = new OplusDisplayCompatUtils();
                }
            }
        }
        return sDisplayCompatUtils;
    }

    public void init(Context context) {
        this.mPackageManager = context.getPackageManager();
        if (OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_SCREEN_HETEROMORPHISM)) {
            setDisplayCutoutType();
            synchronized (this.mLock) {
                this.mCompatData.setHasHeteromorphismFeature(true);
            }
        }
        onSystemReady(context);
    }

    private void registerReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.oplus.util.OplusDisplayCompatUtils.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context1, Intent intent) {
                String action = intent.getAction();
                if (OplusDisplayCompatUtils.this.isOnlyDisplayCompatEnabled() && "android.intent.action.BOOT_COMPLETED".equals(action)) {
                    OplusDisplayCompatUtils.this.loadInstalledImeAppList();
                }
            }
        }, filter);
        IntentFilter filterMultiUser = new IntentFilter();
        filterMultiUser.addAction("android.intent.action.USER_SWITCHED");
        filterMultiUser.addAction("android.intent.action.USER_ADDED");
        filterMultiUser.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.oplus.util.OplusDisplayCompatUtils.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context1, Intent intent) {
                OplusDisplayCompatUtils.this.update();
            }
        }, filterMultiUser);
    }

    private void onSystemReady(Context context) {
        this.mContext = context;
        this.mIsSystem = true;
        registerSettingsObserver();
        registerReceivers();
        registerPackageMonitor();
        initRUSObserver();
        update();
    }

    private void initRUSObserver() {
        initDir();
        initFileObserver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update() {
        updateSettings();
        updateInstalledApps();
        readDisplayCompatConfig();
    }

    private void updateInstalledApps() {
        loadInstalledImeAppList();
        loadInstalledCompatAppList();
        loadInstalledThirdPartyApps();
    }

    private void registerSettingsObserver() {
        this.mSettingsContentObserver = new SettingsContentObserver(new Handler(Looper.myLooper()));
        registerSettingsObserver(Settings.Global.getUriFor(KEY_LOCAL_COMPAT_APPS));
        registerSettingsObserver(Settings.Global.getUriFor(KEY_LOCAL_FULLSCREEN_APPS));
        registerSettingsObserver(Settings.Global.getUriFor(KEY_APP_LIST_CUTOUT_DEFAULT));
        registerSettingsObserver(Settings.Global.getUriFor(KEY_APP_LIST_CUTOUT_SHOW));
        registerSettingsObserver(Settings.Global.getUriFor(KEY_APP_LIST_CUTOUT_HIDE));
        registerSettingsObserver(Settings.Global.getUriFor(KEY_SHOW_FULLSCREEN_DIALOG_APPS));
    }

    private void registerSettingsObserver(Uri settingUri) {
        this.mContext.getContentResolver().registerContentObserver(settingUri, true, this.mSettingsContentObserver);
    }

    public void initData(Context context) {
        this.mContext = context;
        if (context != null) {
            this.mPackageManager = context.getPackageManager();
        }
        initData();
    }

    public void initData() {
        try {
            OplusDisplayCompatData serverData = OplusActivityManager.getInstance().getDisplayCompatData();
            synchronized (this.mLock) {
                getDisplayCompatData().updateFrom(serverData);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "init data error , " + e);
        }
    }

    public OplusDisplayCompatData getDisplayCompatData() {
        return this.mCompatData;
    }

    private void initDir() {
        if (DEBUG) {
            Slog.i(TAG, "initDir start");
        }
        File displayCompatDir = new File(OPLUS_DISPLAY_COMPAT_CONFIG_DIR);
        File displayCompatConfigFile = new File(OPLUS_DISPLAY_COMPAT_CONFIG_FILE_PATH);
        try {
            if (!displayCompatDir.exists()) {
                displayCompatDir.mkdirs();
            }
            if (!displayCompatConfigFile.exists()) {
                displayCompatConfigFile.createNewFile();
            }
        } catch (IOException e) {
            Slog.e(TAG, "initDir failed!!!");
        }
        changeModFile(OPLUS_DISPLAY_COMPAT_CONFIG_FILE_PATH);
    }

    private void initFileObserver() {
        ConfigFileObserver configFileObserver = new ConfigFileObserver(OPLUS_DISPLAY_COMPAT_CONFIG_FILE_PATH);
        this.mConfigFileObserver = configFileObserver;
        configFileObserver.startWatching();
    }

    private void changeModFile(String fileName) {
        try {
            File file = new File(fileName);
            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_WRITE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_WRITE);
            Path path = Paths.get(file.getAbsolutePath(), new String[0]);
            Files.setPosixFilePermissions(path, perms);
        } catch (Exception e) {
            Slog.w(TAG, " " + e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x00db, code lost:
    
        r4 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00dc, code lost:
    
        r5 = com.oplus.util.OplusDisplayCompatUtils.TAG;
        r6 = new java.lang.StringBuilder();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void readDisplayCompatConfig() {
        int type;
        if (!this.mIsSystem) {
            return;
        }
        File configFile = new File(OPLUS_DISPLAY_COMPAT_CONFIG_FILE_PATH);
        if (!configFile.exists() || configFile.length() == 0) {
            initDefaultAppList();
            return;
        }
        Trace.traceBegin(32L, "OplusDisplayCompatUtils.readDisplayCompatConfig");
        logDIfNeeded("readDisplayCompatConfig: start");
        FileInputStream stream = null;
        try {
            try {
                stream = new FileInputStream(configFile);
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);
                do {
                    type = parser.next();
                    if (type == 2) {
                        String tagName = parser.getName();
                        synchronized (this.mLock) {
                            Map<String, Integer> map = TAG_TO_TYPE;
                            if (map.containsKey(tagName)) {
                                if (IS_FOLD_SCREEN || tagName == null || !tagName.contains(TAG_FOLD_SCREEN_PREFIX)) {
                                    Integer dataType = map.get(tagName);
                                    String pkg = parser.nextText();
                                    this.mCompatData.getList(dataType.intValue()).add(pkg);
                                    formatLogDIfNeeded("readDisplayCompatConfig: tag=%s, type=%d, pkg=%s", tagName, dataType, pkg);
                                }
                            } else if (TAG_ENABLE.equals(tagName)) {
                                boolean enabled = Boolean.parseBoolean(parser.nextText());
                                this.mCompatData.setDisplatOptEnabled(enabled);
                                formatLogDIfNeeded("readDisplayCompatConfig: tag=%s, enable=%b", tagName, Boolean.valueOf(enabled));
                            } else if (TAG_ENABLE_IMMERSIVE.equals(tagName)) {
                                boolean enabled2 = Boolean.parseBoolean(parser.nextText());
                                this.mCompatData.setRusImmersiveDefault(enabled2);
                                formatLogDIfNeeded("readDisplayCompatConfig: tag=%s, enable=%b", tagName, Boolean.valueOf(enabled2));
                            }
                        }
                    }
                } while (type != 1);
                stream.close();
            } catch (Exception e) {
                Slog.e(TAG, "failed parsing ", e);
                initDefaultAppList();
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e2) {
                        e = e2;
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        Slog.e(str, sb.append("Failed to close state FileInputStream ").append(e).toString());
                        logDIfNeeded("readDisplayCompatConfig: end");
                        Trace.traceEnd(32L);
                    }
                }
            }
            logDIfNeeded("readDisplayCompatConfig: end");
            Trace.traceEnd(32L);
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e3) {
                    Slog.e(TAG, "Failed to close state FileInputStream " + e3);
                }
            }
            throw th;
        }
    }

    private String parseSafeString(XmlPullParser parser, String tag) {
        String value = parser.getAttributeValue(null, tag);
        return TextUtils.isEmpty(value) ? "" : value;
    }

    private void registerPackageMonitor() {
        if (!this.mIsSystem) {
            return;
        }
        this.mMyPackageMonitor.register(this.mContext, null, UserHandle.ALL, true);
    }

    public boolean isOnlyDisplayCompatEnabled() {
        return this.mCompatData.getDisplayCompatEnabled();
    }

    public boolean hasHeteromorphismFeature() {
        return this.mCompatData.hasHeteromorphismFeature();
    }

    public boolean getImmersiveDefault() {
        boolean rusImmersiveDefault;
        synchronized (this.mLock) {
            rusImmersiveDefault = this.mCompatData.getRusImmersiveDefault();
        }
        return rusImmersiveDefault;
    }

    public boolean inWhitePkgList(String pkg) {
        return isPkgInList(pkg, 0);
    }

    public boolean inBlackPkgList(String pkg) {
        return isPkgInList(pkg, 1);
    }

    public boolean inRusImmersivePkgList(String pkg) {
        return isPkgInList(pkg, 10);
    }

    public boolean inRusNonImmersivePkgList(String pkg) {
        synchronized (this.mLock) {
            if (this.mCompatData.getDisplayCutoutType() != 1) {
                if (DEBUG) {
                    Log.d(TAG, "because this is not a left cutout, the nonimmersive list is not working");
                }
                return false;
            }
            return isPkgInList(pkg, 11);
        }
    }

    public boolean inLocalCompatPkgList(String pkg) {
        return isPkgInList(pkg, 2);
    }

    public boolean inLocalFullScreenPkgList(String pkg) {
        return isPkgInList(pkg, 3);
    }

    public boolean inLocalNonImmersivePkgList(String pkg) {
        return isPkgInList(pkg, 4);
    }

    public boolean inLocalImmersivePkgList(String pkg) {
        return isPkgInList(pkg, 5);
    }

    public boolean inInstalledCompatPkgList(String pkg) {
        return isPkgInList(pkg, 7);
    }

    public boolean inInstalledThirdPartyAppList(String pkg) {
        return isPkgInList(pkg, 12);
    }

    public boolean inInstalledImeList(String pkg) {
        return isPkgInList(pkg, 8);
    }

    public boolean inAlreadyShowDialogList(String pkg) {
        return isPkgInList(pkg, 9);
    }

    public boolean inNeedAdujstSizeList(String pkg) {
        return false;
    }

    public boolean inCompatPkgList(String pkg) {
        return false;
    }

    public boolean needCompatPkgByVersionName(String pkg) {
        return false;
    }

    public boolean shouldCompatAdjustForPkg(String pkg) {
        if (!isOnlyDisplayCompatEnabled() || inInstalledImeList(pkg) || inBlackPkgList(pkg)) {
            return false;
        }
        if (inWhitePkgList(pkg)) {
            return true;
        }
        if (inLocalFullScreenPkgList(pkg) || !inInstalledCompatPkgList(pkg)) {
            return false;
        }
        return true;
    }

    public boolean neverLayoutInDisplayCutout(String packageName) {
        if (inRusNonImmersivePkgList(packageName) && !inLocalImmersivePkgList(packageName)) {
            return true;
        }
        return false;
    }

    public boolean shouldNonImmersiveAdjustForPkg(String pkg) {
        boolean result;
        if (getImmersiveDefault()) {
            result = false;
        } else {
            result = inInstalledThirdPartyAppList(pkg);
        }
        if (inInstalledImeList(pkg)) {
            result = true;
        }
        if (inLocalImmersivePkgList(pkg)) {
            return false;
        }
        if (inLocalNonImmersivePkgList(pkg)) {
            return true;
        }
        if (inRusImmersivePkgList(pkg) || isPkgInList(pkg, 16)) {
            return false;
        }
        if (inRusNonImmersivePkgList(pkg) || isPkgInList(pkg, 17) || shouldCompatAdjustForPkg(pkg)) {
            return true;
        }
        return result;
    }

    public boolean isForceHideCutout(String pkg) {
        if (TextUtils.isEmpty(pkg) || isPkgInList(pkg, 15) || isPkgInList(pkg, 14)) {
            return false;
        }
        if (!isPkgInList(pkg, 6) && !inRusNonImmersivePkgList(pkg)) {
            return false;
        }
        return true;
    }

    public int getAppCutoutMode(String pkg) {
        int mode = 1;
        if (inInstalledThirdPartyAppList(pkg) || inInstalledImeList(pkg)) {
            mode = 0;
        }
        for (int type : CUTOUT_MODE_SUPPLIERS_TYPES) {
            if (isPkgInList(pkg, type)) {
                return TYPE_TO_CUTOUT_MODE.getOrDefault(Integer.valueOf(type), Integer.valueOf(mode)).intValue();
            }
        }
        return mode;
    }

    public float getMaxAspectRatio(ActivityInfo info) {
        float maxAspectRatio = info.getMaxAspectRatio();
        boolean compat = shouldCompatAdjustForPkg(info.packageName);
        if (compat) {
            Slog.d(TAG, info.packageName + ", maxAspectRatio: " + info.getMaxAspectRatio() + " >>> 1.7778");
            return 1.7778f;
        }
        if (inLocalFullScreenPkgList(info.packageName)) {
            Slog.d(TAG, info.packageName + ", maxAspectRatio: " + info.getMaxAspectRatio() + " >>> 0.0");
            return 0.0f;
        }
        return maxAspectRatio;
    }

    public boolean shouldHideFullscreenButtonForPkg(String pkg) {
        if (!inWhitePkgList(pkg)) {
            return false;
        }
        return true;
    }

    public boolean shouldShowFullscreenDialogForPkg(String pkg) {
        if (!inAlreadyShowDialogList(pkg)) {
            return true;
        }
        return false;
    }

    public boolean shouldAdjustRealSizeForPkg(String pkg) {
        return false;
    }

    public void updateLocalAppsListForPkg(String pkg) {
        if (this.mIsSystem) {
            removeCompatApp(pkg);
        }
    }

    private void removeCompatApp(String pkg) {
        updateLocalApp(pkg, false, 2, KEY_LOCAL_COMPAT_APPS);
        updateLocalApp(pkg, true, 3, KEY_LOCAL_FULLSCREEN_APPS);
    }

    private void updateLocalApp(String pkg, boolean add, int type, String key) {
        synchronized (this.mLock) {
            List<String> list = getAppList(type);
            boolean contains = list.contains(pkg);
            if (!contains && add) {
                list.add(pkg);
            } else if (contains && !add) {
                list.remove(pkg);
            }
            Settings.Global.putString(this.mContext.getContentResolver(), key, String.join(",", list));
        }
    }

    public void updateLocalImmersiveListForPkg(String pkg) {
    }

    public void updateLocalShowDialogListForPkg(String pkg) {
        if (this.mContext == null) {
            return;
        }
        updateLocalApp(pkg, true, 9, KEY_SHOW_FULLSCREEN_DIALOG_APPS);
    }

    public void removeLocalShowDialogListForPkg(String pkg) {
        if (this.mContext == null) {
            return;
        }
        updateLocalApp(pkg, false, 9, KEY_SHOW_FULLSCREEN_DIALOG_APPS);
    }

    private void initDefaultAppList() {
        if (IS_FOLD_SCREEN) {
            this.mCompatData.putList(13, DEFAULT_FOLD_SCREEN_NON_FORCE_IMMERSIVE_LIST);
            this.mCompatData.putList(14, DEFAULT_FOLD_SCREEN_FORCE_AUTO_MATCH_LIST);
            this.mCompatData.putList(15, DEFAULT_FOLD_SCREEN_FORCE_NON_HIDE_CUTOUT_LIST);
            this.mCompatData.putList(18, DEFAULT_FOLD_SCREEN_AUTO_MATCH_LIST);
        }
        this.mCompatData.putList(1, DEFAULT_NO_COMPAT_LIST);
        this.mCompatData.putList(11, DEFAULT_NON_IMMERSIVE_LIST);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldCompat(String packageName) {
        return shouldCompat(packageName, null);
    }

    private boolean shouldCompat(String packageName, PackageInfo pkgInfo) {
        float maxAspectRatio = DEFAULT_MAX_ASPECT_RATIO;
        int privateFlags = 0;
        if (isThirdPartyApp(packageName)) {
            try {
                PackageManager packageManager = this.mPackageManager;
                if (packageManager != null && pkgInfo == null) {
                    pkgInfo = packageManager.getPackageInfoAsUser(packageName, 8192, ActivityManager.getCurrentUser());
                }
                if (pkgInfo != null) {
                    maxAspectRatio = pkgInfo.applicationInfo.maxAspectRatio;
                    if (pkgInfo.applicationInfo.targetSdkVersion >= 26 && maxAspectRatio <= 0.0f) {
                        maxAspectRatio = DEFAULT_MAX_ASPECT_RATIO;
                    }
                    privateFlags = pkgInfo.applicationInfo.privateFlags;
                }
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return maxAspectRatio < DEFAULT_MAX_ASPECT_RATIO && (privateFlags & 1024) == 0 && (privateFlags & 4096) == 0;
    }

    private boolean isOplusApp(String packageName) {
        String[] prefixs = this.mContext.getResources().getStringArray(201785390);
        for (String prefix : prefixs) {
            if (packageName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isThirdPartyApp(String packageName) {
        PackageManager packageManager;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        if (THIRD_PARTY_APP_INCLUDE.contains(packageName)) {
            return true;
        }
        if (THIRD_PARTY_APP_EXCLUDE.contains(packageName) || isOplusApp(packageName)) {
            return false;
        }
        try {
            packageManager = this.mPackageManager;
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (packageManager == null) {
            return false;
        }
        PackageInfo packageInfo = packageManager.getPackageInfoAsUser(packageName, 8192, ActivityManager.getCurrentUser());
        boolean isDataApp = (packageInfo.applicationInfo.flags & 1) == 0;
        return isDataApp;
    }

    public boolean checkIfCtsOrEmptyPkg(String name) {
        return TextUtils.isEmpty(name) || name.startsWith("android.server.cts") || name.startsWith("android.server.am") || name.startsWith("android.server.wm") || name.startsWith("android.view.cts") || name.startsWith("android.view.surfacecontrol.cts") || name.startsWith("android.assist");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ConfigFileObserver extends FileObserver {
        private final String mFocusPath;

        public ConfigFileObserver(String path) {
            super(path, 8);
            this.mFocusPath = path;
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, String path) {
            if (event == 8 && this.mFocusPath.equals(OplusDisplayCompatUtils.OPLUS_DISPLAY_COMPAT_CONFIG_FILE_PATH)) {
                Slog.i(OplusDisplayCompatUtils.TAG, "FileObserver: onEvent");
                OplusDisplayCompatUtils.this.readDisplayCompatConfig();
            }
        }
    }

    private void loadLocalAppListFromSettings(String settingsKey, int listType) {
        Context context = this.mContext;
        if (context == null) {
            return;
        }
        String pkgList = Settings.Global.getString(context.getContentResolver(), settingsKey);
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(pkgList)) {
            list.addAll(Arrays.asList(pkgList.split(",")));
        }
        synchronized (this.mLock) {
            this.mCompatData.putList(listType, list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadInstalledImeAppList() {
        if (this.mContext == null) {
            return;
        }
        List<String> imeList = new ArrayList<>();
        try {
            List<ResolveInfo> list = this.mContext.getPackageManager().queryIntentServicesAsUser(new Intent("android.view.InputMethod"), 131200, ActivityManager.getCurrentUser());
            if (list != null) {
                for (ResolveInfo resolveInfo : list) {
                    if (resolveInfo != null) {
                        String packageName = resolveInfo.serviceInfo.packageName;
                        imeList.add(packageName);
                    }
                }
            }
        } catch (Exception e) {
            Slog.d(TAG, "loadInstalledImeAppList failed");
        }
        synchronized (this.mLock) {
            putAppList(8, imeList);
        }
    }

    private void loadInstalledCompatAppList() {
        if (this.mContext == null) {
            return;
        }
        long begin = System.currentTimeMillis();
        List<String> thirdPartyNeedCompatAppsList = new ArrayList<>();
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            List<PackageInfo> installedPackageList = packageManager.getInstalledPackagesAsUser(0, ActivityManager.getCurrentUser());
            for (PackageInfo packageInfo : installedPackageList) {
                if (packageInfo != null) {
                    try {
                        String packageName = packageInfo.packageName;
                        if (shouldCompat(packageName, packageInfo)) {
                            thirdPartyNeedCompatAppsList.add(packageName);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e2) {
        }
        synchronized (this.mLock) {
            putAppList(7, thirdPartyNeedCompatAppsList);
        }
        long cost = System.currentTimeMillis() - begin;
        Slog.i(TAG, "loadInstalledCompatAppList time cost =" + cost);
    }

    private void loadInstalledThirdPartyApps() {
        if (this.mContext == null) {
            return;
        }
        List<String> thirdPartyAppsList = new ArrayList<>();
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            List<PackageInfo> installedPackageList = packageManager.getInstalledPackagesAsUser(0, ActivityManager.getCurrentUser());
            for (PackageInfo packageInfo : installedPackageList) {
                if (packageInfo != null && packageInfo.packageName != null) {
                    String packageName = packageInfo.packageName;
                    if (THIRD_PARTY_APP_INCLUDE.contains(packageName)) {
                        thirdPartyAppsList.add(packageName);
                    } else if (!THIRD_PARTY_APP_EXCLUDE.contains(packageName) && !isOplusApp(packageName)) {
                        boolean z = true;
                        if ((packageInfo.applicationInfo.flags & 1) != 0) {
                            z = false;
                        }
                        boolean isDataApp = z;
                        if (isDataApp) {
                            thirdPartyAppsList.add(packageName);
                            if (DEBUG) {
                                Log.d(TAG, "thirdPartyAppsList add : " + packageName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "fail to loadInstalledThirdPartyApps: " + e);
        }
        putAppList(12, thirdPartyAppsList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class SettingsContentObserver extends ContentObserver {
        SettingsContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            OplusDisplayCompatUtils.this.updateSettings();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettings() {
        loadLocalAppListFromSettings(KEY_APP_LIST_CUTOUT_DEFAULT, 4);
        loadLocalAppListFromSettings(KEY_APP_LIST_CUTOUT_SHOW, 5);
        loadLocalAppListFromSettings(KEY_APP_LIST_CUTOUT_HIDE, 6);
        loadLocalAppListFromSettings(KEY_LOCAL_COMPAT_APPS, 2);
        loadLocalAppListFromSettings(KEY_LOCAL_FULLSCREEN_APPS, 3);
        loadLocalAppListFromSettings(KEY_SHOW_FULLSCREEN_DIALOG_APPS, 9);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyPackageMonitor extends PackageMonitor {
        private MyPackageMonitor() {
        }

        public void onPackageRemoved(String packageName, int uid) {
            if (TextUtils.isEmpty(packageName)) {
                return;
            }
            OplusDisplayCompatUtils.this.loadInstalledImeAppList();
            OplusDisplayCompatUtils.this.removePkgInList(packageName, 7);
            OplusDisplayCompatUtils.this.removePkgInList(packageName, 12);
            OplusDisplayCompatUtils.this.removeLocalShowDialogListForPkg(packageName);
        }

        public void onPackageAdded(String packageName, int uid) {
            if (TextUtils.isEmpty(packageName)) {
                return;
            }
            OplusDisplayCompatUtils.this.loadInstalledImeAppList();
            if (!OplusDisplayCompatUtils.this.inInstalledImeList(packageName) && OplusDisplayCompatUtils.this.shouldCompat(packageName)) {
                OplusDisplayCompatUtils.this.addPkgInList(packageName, 7);
            }
            if (!OplusDisplayCompatUtils.this.inInstalledThirdPartyAppList(packageName) && OplusDisplayCompatUtils.this.isThirdPartyApp(packageName)) {
                OplusDisplayCompatUtils.this.addPkgInList(packageName, 12);
            }
        }

        public void onPackageModified(String packageName) {
            if (TextUtils.isEmpty(packageName)) {
                return;
            }
            OplusDisplayCompatUtils.this.loadInstalledImeAppList();
            OplusDisplayCompatUtils oplusDisplayCompatUtils = OplusDisplayCompatUtils.this;
            oplusDisplayCompatUtils.updatePkgInList(packageName, 7, oplusDisplayCompatUtils.shouldCompat(packageName) && !OplusDisplayCompatUtils.this.inInstalledImeList(packageName));
        }
    }

    private void setDisplayCutoutType() {
        String value;
        int cutoutType = 1;
        try {
            value = OplusSystemProperties.get(OplusPropertyList.PROPERTY_SCREEN_HETEROMORPHISM, "");
            Log.d(TAG, "cutout size: " + value);
        } catch (Exception e) {
            Log.d(TAG, "fail to set display cutout type");
        }
        if (value == null) {
            return;
        }
        String[] sizes = value.split("[,:]");
        if (sizes.length == 4) {
            int x1 = Integer.parseInt(sizes[0]);
            int x2 = Integer.parseInt(sizes[2]);
            if (x1 >= 50 || x2 >= 300) {
                cutoutType = 2;
            }
        }
        synchronized (this.mLock) {
            this.mCompatData.setDisplayCutoutType(cutoutType);
        }
        Log.d(TAG, "set display cutout type : " + cutoutType);
    }

    public List<String> getAppList(int type) {
        List<String> list;
        if (OplusDisplayCompatData.checkTypeInvalid(type)) {
            Slog.e(TAG, "getAppList: invalid params " + type);
            return new ArrayList();
        }
        synchronized (this.mLock) {
            list = this.mCompatData.getList(type);
        }
        return list;
    }

    public void addPkgInList(String pkg, int type) {
        if (OplusDisplayCompatData.checkTypeInvalid(type) || TextUtils.isEmpty(pkg)) {
            Slog.e(TAG, "addPkgInList: invalid params " + pkg + ", " + type);
            return;
        }
        Context context = this.mContext;
        if (context == null) {
            Slog.w(TAG, "addPkgInList no-context");
        } else {
            context.enforceCallingOrSelfPermission(OPLUS_PERMISSION_SAFE_WINDOW, "addPkgInList");
            updatePkgInList(pkg, type, true);
        }
    }

    public void removePkgInList(String pkg, int type) {
        if (OplusDisplayCompatData.checkTypeInvalid(type) || TextUtils.isEmpty(pkg)) {
            Slog.e(TAG, "removePkgInList: invalid params " + pkg + ", " + type);
            return;
        }
        Context context = this.mContext;
        if (context == null) {
            Slog.w(TAG, "addPkgInList no-context");
        } else {
            context.enforceCallingOrSelfPermission(OPLUS_PERMISSION_SAFE_WINDOW, "removePkgInList");
            updatePkgInList(pkg, type, false);
        }
    }

    public boolean isPkgInList(String pkg, int type) {
        boolean contains;
        if (OplusDisplayCompatData.checkTypeInvalid(type) || TextUtils.isEmpty(pkg)) {
            Slog.e(TAG, "isPkgInList: invalid params " + pkg + ", " + type);
            return false;
        }
        synchronized (this.mLock) {
            contains = this.mCompatData.getList(type).contains(pkg);
        }
        return contains;
    }

    public void putAppList(int type, List<String> list) {
        if (OplusDisplayCompatData.checkTypeInvalid(type) || list == null) {
            Slog.e(TAG, "putAppList: invalid params " + type);
            return;
        }
        synchronized (this.mLock) {
            this.mCompatData.putList(type, list);
        }
    }

    public void updatePkgInList(String pkg, int type, boolean add) {
        synchronized (this.mLock) {
            List<String> list = getAppList(type);
            boolean contains = list.contains(pkg);
            if (!contains && add) {
                list.add(pkg);
            } else if (contains && !add) {
                list.remove(pkg);
            }
        }
    }

    public static void logD(String content) {
        Slog.d(TAG, content);
    }

    public static void logDIfNeeded(String content) {
        if (DEBUG) {
            logD(content);
        }
    }

    public static void formatLogDIfNeeded(String format, Object... args) {
        if (DEBUG) {
            formatLogD(format, args);
        }
    }

    public static void formatLogD(String format, Object... args) {
        if (format != null) {
            try {
                Slog.d(TAG, String.format(format, args));
            } catch (Exception e) {
                Slog.w(TAG, "format error.", e);
            }
        }
    }

    public static int getScreenType(DisplayMetrics metrics) {
        if (metrics == null) {
            return 1;
        }
        int sw = (int) (Math.min(metrics.widthPixels, metrics.heightPixels) / metrics.density);
        for (Map.Entry<Integer, Pair<Integer, Integer>> next : DEVICES_MAP.entrySet()) {
            Pair<Integer, Integer> pairSize = next.getValue();
            if (sw > ((Integer) pairSize.first).intValue() && sw <= ((Integer) pairSize.second).intValue()) {
                int ret = next.getKey().intValue();
                return ret;
            }
        }
        return 1;
    }
}
