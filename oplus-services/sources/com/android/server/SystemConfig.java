package com.android.server;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.pm.FeatureInfo;
import android.os.Build;
import android.os.CarrierAssociatedAppEntry;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Process;
import android.os.SystemProperties;
import android.os.VintfRuntimeInfo;
import android.os.incremental.IncrementalManager;
import android.os.storage.StorageManager;
import android.permission.PermissionManager;
import android.sysprop.ApexProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimingsTraceLog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.build.UnboundedSdkLevel;
import com.android.server.am.HostingRecord;
import com.android.server.pm.permission.PermissionAllowlist;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SystemConfig {
    private static final int ALLOW_ALL = -1;
    private static final int ALLOW_APP_CONFIGS = 8;
    private static final int ALLOW_ASSOCIATIONS = 128;
    private static final int ALLOW_FEATURES = 1;
    private static final int ALLOW_HIDDENAPI_WHITELISTING = 64;
    private static final int ALLOW_IMPLICIT_BROADCASTS = 512;
    private static final int ALLOW_LIBS = 2;
    private static final int ALLOW_OEM_PERMISSIONS = 32;
    private static final int ALLOW_OVERRIDE_APP_RESTRICTIONS = 256;
    private static final int ALLOW_PERMISSIONS = 4;
    private static final int ALLOW_PRIVAPP_PERMISSIONS = 16;
    private static final int ALLOW_VENDOR_APEX = 1024;
    private static final String SKU_PROPERTY = "ro.boot.product.hardware.sku";
    static final String TAG = "SystemConfig";
    private static final String VENDOR_SKU_PROPERTY = "ro.boot.product.vendor.sku";
    static SystemConfig sInstance;
    private String mModulesInstallerPackageName;
    private String mOverlayConfigSignaturePackage;
    private static final ArrayMap<String, ArraySet<String>> EMPTY_PERMISSIONS = new ArrayMap<>();
    private static final ISystemConfigStaticWrapper STATIC_WRAPPER = new SystemConfigStaticWrapper();
    int[] mGlobalGids = EmptyArray.INT;
    final SparseArray<ArraySet<String>> mSystemPermissions = new SparseArray<>();
    final ArrayList<PermissionManager.SplitPermissionInfo> mSplitPermissions = new ArrayList<>();
    final ArrayMap<String, SharedLibraryEntry> mSharedLibraries = new ArrayMap<>();
    final ArrayMap<String, FeatureInfo> mAvailableFeatures = new ArrayMap<>();
    final ArraySet<String> mUnavailableFeatures = new ArraySet<>();
    final ArrayMap<String, PermissionEntry> mPermissions = new ArrayMap<>();
    final ArraySet<String> mAllowInPowerSaveExceptIdle = new ArraySet<>();
    final ArraySet<String> mAllowInPowerSave = new ArraySet<>();
    final ArraySet<String> mAllowInDataUsageSave = new ArraySet<>();
    final ArraySet<String> mAllowUnthrottledLocation = new ArraySet<>();
    final ArrayMap<String, ArraySet<String>> mAllowAdasSettings = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mAllowIgnoreLocationSettings = new ArrayMap<>();
    final ArraySet<String> mAllowImplicitBroadcasts = new ArraySet<>();
    final ArraySet<String> mBgRestrictionExemption = new ArraySet<>();
    final ArraySet<String> mLinkedApps = new ArraySet<>();
    final ArraySet<ComponentName> mDefaultVrComponents = new ArraySet<>();
    final ArraySet<ComponentName> mBackupTransportWhitelist = new ArraySet<>();
    final ArrayMap<String, ArrayMap<String, Boolean>> mPackageComponentEnabledState = new ArrayMap<>();
    final ArraySet<String> mHiddenApiPackageWhitelist = new ArraySet<>();
    final ArraySet<String> mDisabledUntilUsedPreinstalledCarrierApps = new ArraySet<>();
    final ArrayMap<String, List<CarrierAssociatedAppEntry>> mDisabledUntilUsedPreinstalledCarrierAssociatedApps = new ArrayMap<>();
    private final PermissionAllowlist mPermissionAllowlist = new PermissionAllowlist();
    final ArrayMap<String, ArraySet<String>> mAllowedAssociations = new ArrayMap<>();
    private final ArraySet<String> mBugreportWhitelistedPackages = new ArraySet<>();
    private final ArraySet<String> mAppDataIsolationWhitelistedApps = new ArraySet<>();
    private ArrayMap<String, Set<String>> mPackageToUserTypeWhitelist = new ArrayMap<>();
    private ArrayMap<String, Set<String>> mPackageToUserTypeBlacklist = new ArrayMap<>();
    private final ArraySet<String> mRollbackWhitelistedPackages = new ArraySet<>();
    private final ArraySet<String> mAutomaticRollbackDenylistedPackages = new ArraySet<>();
    private final ArraySet<String> mWhitelistedStagedInstallers = new ArraySet<>();
    private final ArrayMap<String, String> mAllowedVendorApexes = new ArrayMap<>();
    private final Set<String> mInstallConstraintsAllowlist = new ArraySet();
    private final ArrayMap<String, String> mUpdateOwnersForSystemApps = new ArrayMap<>();
    private final Set<String> mInitialNonStoppedSystemPackages = new ArraySet();
    private final ArrayMap<String, String> mAppMetadataFilePaths = new ArrayMap<>();
    private Map<String, Map<String, String>> mNamedActors = null;
    ISystemConfigExt mSystemConfigExt = (ISystemConfigExt) ExtLoader.type(ISystemConfigExt.class).base(this).create();
    ISystemConfigSocExt mSystemConfigSocExt = (ISystemConfigSocExt) ExtLoader.type(ISystemConfigSocExt.class).base(this).create();

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAtLeastSdkLevel(String str) {
        try {
            return UnboundedSdkLevel.isAtLeast(str);
        } catch (IllegalArgumentException unused) {
            return false;
        }
    }

    private static boolean isAtMostSdkLevel(String str) {
        try {
            return UnboundedSdkLevel.isAtMost(str);
        } catch (IllegalArgumentException unused) {
            return true;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SharedLibraryEntry {
        public final boolean canBeSafelyIgnored;
        public final String[] dependencies;
        public final String filename;
        public final boolean isNative;
        public final String name;
        public final String onBootclasspathBefore;
        public final String onBootclasspathSince;

        @VisibleForTesting
        public SharedLibraryEntry(String str, String str2, String[] strArr, boolean z) {
            this(str, str2, strArr, null, null, z);
        }

        @VisibleForTesting
        public SharedLibraryEntry(String str, String str2, String[] strArr, String str3, String str4) {
            this(str, str2, strArr, str3, str4, false);
        }

        SharedLibraryEntry(String str, String str2, String[] strArr, String str3, String str4, boolean z) {
            this.name = str;
            this.filename = str2;
            this.dependencies = strArr;
            this.onBootclasspathSince = str3;
            this.onBootclasspathBefore = str4;
            this.isNative = z;
            this.canBeSafelyIgnored = (str3 != null && SystemConfig.isAtLeastSdkLevel(str3)) || !(str4 == null || SystemConfig.isAtLeastSdkLevel(str4));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class PermissionEntry {
        public int[] gids;
        public final String name;
        public boolean perUser;

        PermissionEntry(String str, boolean z) {
            this.name = str;
            this.perUser = z;
        }
    }

    public static SystemConfig getInstance() {
        SystemConfig systemConfig;
        if (!isSystemProcess()) {
            Slog.wtf(TAG, "SystemConfig is being accessed by a process other than system_server.");
        }
        synchronized (SystemConfig.class) {
            if (sInstance == null) {
                sInstance = new SystemConfig();
            }
            systemConfig = sInstance;
        }
        return systemConfig;
    }

    public int[] getGlobalGids() {
        return this.mGlobalGids;
    }

    public SparseArray<ArraySet<String>> getSystemPermissions() {
        return this.mSystemPermissions;
    }

    public ArrayList<PermissionManager.SplitPermissionInfo> getSplitPermissions() {
        return this.mSplitPermissions;
    }

    public ArrayMap<String, SharedLibraryEntry> getSharedLibraries() {
        return this.mSharedLibraries;
    }

    public ArrayMap<String, FeatureInfo> getAvailableFeatures() {
        return this.mAvailableFeatures;
    }

    public ArrayMap<String, PermissionEntry> getPermissions() {
        return this.mPermissions;
    }

    public ArraySet<String> getAllowImplicitBroadcasts() {
        return this.mAllowImplicitBroadcasts;
    }

    public ArraySet<String> getAllowInPowerSaveExceptIdle() {
        return this.mAllowInPowerSaveExceptIdle;
    }

    public ArraySet<String> getAllowInPowerSave() {
        return this.mAllowInPowerSave;
    }

    public ArraySet<String> getAllowInDataUsageSave() {
        return this.mAllowInDataUsageSave;
    }

    public ArraySet<String> getAllowUnthrottledLocation() {
        return this.mAllowUnthrottledLocation;
    }

    public ArrayMap<String, ArraySet<String>> getAllowAdasLocationSettings() {
        return this.mAllowAdasSettings;
    }

    public ArrayMap<String, ArraySet<String>> getAllowIgnoreLocationSettings() {
        return this.mAllowIgnoreLocationSettings;
    }

    public ArraySet<String> getBgRestrictionExemption() {
        return this.mBgRestrictionExemption;
    }

    public ArraySet<String> getLinkedApps() {
        return this.mLinkedApps;
    }

    public ArraySet<String> getHiddenApiWhitelistedApps() {
        return this.mHiddenApiPackageWhitelist;
    }

    public ArraySet<ComponentName> getDefaultVrComponents() {
        return this.mDefaultVrComponents;
    }

    public ArraySet<ComponentName> getBackupTransportWhitelist() {
        return this.mBackupTransportWhitelist;
    }

    public ArrayMap<String, Boolean> getComponentsEnabledStates(String str) {
        return this.mPackageComponentEnabledState.get(str);
    }

    public ArraySet<String> getDisabledUntilUsedPreinstalledCarrierApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierApps;
    }

    public ArrayMap<String, List<CarrierAssociatedAppEntry>> getDisabledUntilUsedPreinstalledCarrierAssociatedApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;
    }

    public PermissionAllowlist getPermissionAllowlist() {
        return this.mPermissionAllowlist;
    }

    public ArrayMap<String, ArraySet<String>> getAllowedAssociations() {
        return this.mAllowedAssociations;
    }

    public ArraySet<String> getBugreportWhitelistedPackages() {
        return this.mBugreportWhitelistedPackages;
    }

    public Set<String> getRollbackWhitelistedPackages() {
        return this.mRollbackWhitelistedPackages;
    }

    public Set<String> getAutomaticRollbackDenylistedPackages() {
        return this.mAutomaticRollbackDenylistedPackages;
    }

    public Set<String> getWhitelistedStagedInstallers() {
        return this.mWhitelistedStagedInstallers;
    }

    public Map<String, String> getAllowedVendorApexes() {
        return this.mAllowedVendorApexes;
    }

    public Set<String> getInstallConstraintsAllowlist() {
        return this.mInstallConstraintsAllowlist;
    }

    public String getModulesInstallerPackageName() {
        return this.mModulesInstallerPackageName;
    }

    public String getSystemAppUpdateOwnerPackageName(String str) {
        return this.mUpdateOwnersForSystemApps.get(str);
    }

    public ArraySet<String> getAppDataIsolationWhitelistedApps() {
        return this.mAppDataIsolationWhitelistedApps;
    }

    public ArrayMap<String, Set<String>> getAndClearPackageToUserTypeWhitelist() {
        ArrayMap<String, Set<String>> arrayMap = this.mPackageToUserTypeWhitelist;
        this.mPackageToUserTypeWhitelist = new ArrayMap<>(0);
        return arrayMap;
    }

    public ArrayMap<String, Set<String>> getAndClearPackageToUserTypeBlacklist() {
        ArrayMap<String, Set<String>> arrayMap = this.mPackageToUserTypeBlacklist;
        this.mPackageToUserTypeBlacklist = new ArrayMap<>(0);
        return arrayMap;
    }

    public Map<String, Map<String, String>> getNamedActors() {
        Map<String, Map<String, String>> map = this.mNamedActors;
        return map != null ? map : Collections.emptyMap();
    }

    public String getOverlayConfigSignaturePackage() {
        if (TextUtils.isEmpty(this.mOverlayConfigSignaturePackage)) {
            return null;
        }
        return this.mOverlayConfigSignaturePackage;
    }

    public Set<String> getInitialNonStoppedSystemPackages() {
        return this.mInitialNonStoppedSystemPackages;
    }

    public ArrayMap<String, String> getAppMetadataFilePaths() {
        return this.mAppMetadataFilePaths;
    }

    @VisibleForTesting
    public SystemConfig(boolean z) {
        if (z) {
            Slog.w(TAG, "Constructing a test SystemConfig");
            readAllPermissions();
            this.mSystemConfigExt.readConfigInConstructor();
            return;
        }
        Slog.w(TAG, "Constructing an empty test SystemConfig");
    }

    SystemConfig() {
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog(TAG, 524288L);
        timingsTraceLog.traceBegin("readAllPermissions");
        try {
            readAllPermissions();
            readPublicNativeLibrariesList();
            this.mSystemConfigExt.readConfigInConstructor();
        } finally {
            timingsTraceLog.traceEnd();
        }
    }

    private void readAllPermissions() {
        XmlPullParser newPullParser = Xml.newPullParser();
        readPermissions(newPullParser, Environment.buildPath(Environment.getRootDirectory(), new String[]{"etc", "sysconfig"}), -1);
        readPermissions(newPullParser, Environment.buildPath(Environment.getRootDirectory(), new String[]{"etc", "permissions"}), -1);
        int i = Build.VERSION.DEVICE_INITIAL_SDK_INT <= 27 ? 1183 : 1171;
        readPermissions(newPullParser, Environment.buildPath(Environment.getVendorDirectory(), new String[]{"etc", "sysconfig"}), i);
        readPermissions(newPullParser, Environment.buildPath(Environment.getVendorDirectory(), new String[]{"etc", "permissions"}), i);
        String str = SystemProperties.get(VENDOR_SKU_PROPERTY, "");
        if (!str.isEmpty()) {
            String str2 = "sku_" + str;
            readPermissions(newPullParser, Environment.buildPath(Environment.getVendorDirectory(), new String[]{"etc", "sysconfig", str2}), i);
            readPermissions(newPullParser, Environment.buildPath(Environment.getVendorDirectory(), new String[]{"etc", "permissions", str2}), i);
        }
        readPermissions(newPullParser, Environment.buildPath(Environment.getOdmDirectory(), new String[]{"etc", "sysconfig"}), i);
        readPermissions(newPullParser, Environment.buildPath(Environment.getOdmDirectory(), new String[]{"etc", "permissions"}), i);
        String str3 = SystemProperties.get(SKU_PROPERTY, "");
        if (!str3.isEmpty()) {
            String str4 = "sku_" + str3;
            readPermissions(newPullParser, Environment.buildPath(Environment.getOdmDirectory(), new String[]{"etc", "sysconfig", str4}), i);
            readPermissions(newPullParser, Environment.buildPath(Environment.getOdmDirectory(), new String[]{"etc", "permissions", str4}), i);
        }
        readPermissions(newPullParser, Environment.buildPath(Environment.getOemDirectory(), new String[]{"etc", "sysconfig"}), 1185);
        readPermissions(newPullParser, Environment.buildPath(Environment.getOemDirectory(), new String[]{"etc", "permissions"}), 1185);
        int i2 = Build.VERSION.DEVICE_INITIAL_SDK_INT <= 30 ? -1 : 2015;
        readPermissions(newPullParser, Environment.buildPath(Environment.getProductDirectory(), new String[]{"etc", "sysconfig"}), i2);
        readPermissions(newPullParser, Environment.buildPath(Environment.getProductDirectory(), new String[]{"etc", "permissions"}), i2);
        readPermissions(newPullParser, Environment.buildPath(Environment.getSystemExtDirectory(), new String[]{"etc", "sysconfig"}), -1);
        readPermissions(newPullParser, Environment.buildPath(Environment.getSystemExtDirectory(), new String[]{"etc", "permissions"}), -1);
        if (isSystemProcess()) {
            for (File file : FileUtils.listFilesOrEmpty(Environment.getApexDirectory())) {
                if (!file.isFile() && !file.getPath().contains("@")) {
                    readPermissions(newPullParser, Environment.buildPath(file, new String[]{"etc", "permissions"}), 19);
                }
            }
        }
    }

    @VisibleForTesting
    public void readPermissions(XmlPullParser xmlPullParser, File file, int i) {
        if (!file.exists() || !file.isDirectory()) {
            if (i == -1) {
                Slog.w(TAG, "No directory " + file + ", skipping");
                return;
            }
            return;
        }
        if (!file.canRead()) {
            Slog.w(TAG, "Directory " + file + " cannot be read");
            return;
        }
        File file2 = null;
        for (File file3 : file.listFiles()) {
            if (file3.isFile()) {
                if (file3.getPath().endsWith("etc/permissions/platform.xml")) {
                    file2 = file3;
                } else if (!file3.getPath().endsWith(".xml")) {
                    Slog.i(TAG, "Non-xml file " + file3 + " in " + file + " directory, ignoring");
                } else if (!file3.canRead()) {
                    Slog.w(TAG, "Permissions library file " + file3 + " cannot be read");
                } else if (!this.mSystemConfigExt.filterFileInReadPermissions(file3)) {
                    readPermissionsFromXml(xmlPullParser, file3, i);
                }
            }
        }
        if (file2 != null) {
            readPermissionsFromXml(xmlPullParser, file2, i);
        }
    }

    private void logNotAllowedInPartition(String str, File file, XmlPullParser xmlPullParser) {
        Slog.w(TAG, "<" + str + "> not allowed in partition of " + file + " at " + xmlPullParser.getPositionDescription());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:61:0x011c. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:66:0x0314. Please report as an issue. */
    /* JADX WARN: Not initialized variable reg: 25, insn: 0x1124: MOVE (r3 I:??[OBJECT, ARRAY]) = (r25 I:??[OBJECT, ARRAY]), block:B:659:0x1123 */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0965 A[Catch: IOException -> 0x1120, XmlPullParserException -> 0x1122, all -> 0x11da, TryCatch #5 {all -> 0x11da, blocks: (B:68:0x10da, B:72:0x031b, B:74:0x032c, B:76:0x0352, B:78:0x0358, B:79:0x037e, B:80:0x0385, B:82:0x0396, B:83:0x03ba, B:85:0x03c0, B:86:0x03e6, B:88:0x03ec, B:89:0x03f3, B:91:0x0404, B:92:0x0459, B:93:0x0429, B:95:0x042f, B:96:0x0454, B:98:0x0460, B:100:0x0466, B:101:0x0492, B:102:0x0489, B:103:0x048f, B:105:0x0499, B:107:0x04a6, B:109:0x04ca, B:112:0x04f2, B:113:0x04fb, B:114:0x04f8, B:116:0x0502, B:118:0x0510, B:120:0x053a, B:122:0x053e, B:124:0x0541, B:125:0x0548, B:127:0x054c, B:128:0x0533, B:129:0x0549, B:130:0x0551, B:132:0x0557, B:133:0x057f, B:134:0x057a, B:135:0x0584, B:137:0x058a, B:138:0x05b2, B:139:0x05ad, B:141:0x05b9, B:143:0x05bf, B:144:0x0615, B:145:0x05e2, B:147:0x05ea, B:149:0x05f1, B:150:0x0611, B:151:0x0612, B:152:0x061a, B:154:0x0637, B:155:0x06e4, B:156:0x065d, B:158:0x0663, B:159:0x0688, B:161:0x068e, B:162:0x06b3, B:164:0x06bb, B:166:0x06bf, B:167:0x06c6, B:169:0x06d0, B:170:0x06e1, B:171:0x06db, B:174:0x06e9, B:175:0x071b, B:177:0x071c, B:178:0x073f, B:179:0x0740, B:180:0x0749, B:182:0x074f, B:183:0x0777, B:184:0x0772, B:185:0x077c, B:187:0x0782, B:188:0x07aa, B:189:0x07a5, B:191:0x07b1, B:193:0x07ba, B:194:0x07e3, B:196:0x07eb, B:197:0x0814, B:199:0x0826, B:200:0x0830, B:201:0x0853, B:202:0x0850, B:204:0x085a, B:206:0x0860, B:207:0x088c, B:208:0x0883, B:209:0x0889, B:211:0x0893, B:212:0x0898, B:214:0x08a2, B:216:0x08c3, B:220:0x08e8, B:222:0x094e, B:226:0x0965, B:228:0x0972, B:230:0x097f, B:232:0x098c, B:233:0x0999, B:236:0x09a4, B:238:0x09ae, B:240:0x09b4, B:241:0x09e0, B:242:0x09d7, B:243:0x09dd, B:245:0x09e7, B:249:0x09f6, B:258:0x0a02, B:252:0x0a31, B:254:0x0a3b, B:255:0x0a45, B:256:0x0a76, B:260:0x0a07, B:261:0x0a4e, B:262:0x0a73, B:264:0x0a7d, B:266:0x0a86, B:267:0x0ae7, B:268:0x0aab, B:270:0x0ab1, B:271:0x0ade, B:272:0x0ae4, B:273:0x0aeb, B:275:0x0af4, B:277:0x0b00, B:278:0x0b58, B:280:0x0b25, B:281:0x0b4a, B:282:0x0b55, B:284:0x0b5e, B:286:0x0b64, B:287:0x0b90, B:288:0x0b87, B:289:0x0b8d, B:291:0x0b97, B:293:0x0b9d, B:294:0x0bc9, B:295:0x0bc0, B:296:0x0bc6, B:298:0x0bd0, B:300:0x0bd8, B:301:0x0c06, B:302:0x0bfd, B:303:0x0c03, B:305:0x0c0d, B:307:0x0c19, B:308:0x0c73, B:309:0x0c3c, B:311:0x0c46, B:314:0x0c4e, B:315:0x0c59, B:317:0x0c61, B:320:0x0c6c, B:322:0x0c70, B:324:0x0c7a, B:326:0x0c86, B:327:0x0ce0, B:328:0x0ca9, B:330:0x0cb3, B:333:0x0cbb, B:334:0x0cc6, B:336:0x0cce, B:339:0x0cd9, B:341:0x0cdd, B:343:0x0ce7, B:345:0x0ced, B:346:0x0d19, B:347:0x0d10, B:348:0x0d16, B:350:0x0d20, B:352:0x0d26, B:353:0x0d52, B:354:0x0d49, B:355:0x0d4f, B:357:0x0d59, B:359:0x0d5f, B:360:0x0d90, B:361:0x0d82, B:362:0x0d8d, B:364:0x0d97, B:366:0x0d9d, B:367:0x0dce, B:368:0x0dc0, B:369:0x0dcb, B:371:0x0dd5, B:373:0x0ddb, B:374:0x0e0e, B:375:0x0e00, B:376:0x0e0b, B:379:0x0e17, B:383:0x0e3d, B:384:0x0e6f, B:386:0x0e62, B:387:0x0e29, B:388:0x0e6b, B:391:0x0e79, B:393:0x0e9b, B:394:0x0f7c, B:396:0x0ec1, B:398:0x0ee9, B:403:0x0ef5, B:407:0x0eff, B:411:0x0f0e, B:413:0x0f20, B:414:0x0f2e, B:415:0x0f26, B:416:0x0f3d, B:418:0x0f4e, B:420:0x0f58, B:422:0x0f62, B:423:0x0f6f, B:426:0x0f77, B:429:0x0f85, B:430:0x0f8a, B:433:0x0f98, B:435:0x0f9e, B:436:0x0fc5, B:438:0x0fce, B:439:0x0ff7, B:441:0x0ffd, B:442:0x102e, B:444:0x103c, B:445:0x1046, B:446:0x104d, B:447:0x104a, B:450:0x1058, B:452:0x105e, B:453:0x1085, B:454:0x108e, B:457:0x1099, B:459:0x10a1, B:460:0x10d6, B:461:0x10ae, B:462:0x10d3, B:645:0x113b, B:649:0x1144, B:654:0x1114, B:655:0x111f), top: B:5:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0970  */
    /* JADX WARN: Removed duplicated region for block: B:409:0x0f0a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0f4e A[Catch: IOException -> 0x1120, XmlPullParserException -> 0x1122, all -> 0x11da, TryCatch #5 {all -> 0x11da, blocks: (B:68:0x10da, B:72:0x031b, B:74:0x032c, B:76:0x0352, B:78:0x0358, B:79:0x037e, B:80:0x0385, B:82:0x0396, B:83:0x03ba, B:85:0x03c0, B:86:0x03e6, B:88:0x03ec, B:89:0x03f3, B:91:0x0404, B:92:0x0459, B:93:0x0429, B:95:0x042f, B:96:0x0454, B:98:0x0460, B:100:0x0466, B:101:0x0492, B:102:0x0489, B:103:0x048f, B:105:0x0499, B:107:0x04a6, B:109:0x04ca, B:112:0x04f2, B:113:0x04fb, B:114:0x04f8, B:116:0x0502, B:118:0x0510, B:120:0x053a, B:122:0x053e, B:124:0x0541, B:125:0x0548, B:127:0x054c, B:128:0x0533, B:129:0x0549, B:130:0x0551, B:132:0x0557, B:133:0x057f, B:134:0x057a, B:135:0x0584, B:137:0x058a, B:138:0x05b2, B:139:0x05ad, B:141:0x05b9, B:143:0x05bf, B:144:0x0615, B:145:0x05e2, B:147:0x05ea, B:149:0x05f1, B:150:0x0611, B:151:0x0612, B:152:0x061a, B:154:0x0637, B:155:0x06e4, B:156:0x065d, B:158:0x0663, B:159:0x0688, B:161:0x068e, B:162:0x06b3, B:164:0x06bb, B:166:0x06bf, B:167:0x06c6, B:169:0x06d0, B:170:0x06e1, B:171:0x06db, B:174:0x06e9, B:175:0x071b, B:177:0x071c, B:178:0x073f, B:179:0x0740, B:180:0x0749, B:182:0x074f, B:183:0x0777, B:184:0x0772, B:185:0x077c, B:187:0x0782, B:188:0x07aa, B:189:0x07a5, B:191:0x07b1, B:193:0x07ba, B:194:0x07e3, B:196:0x07eb, B:197:0x0814, B:199:0x0826, B:200:0x0830, B:201:0x0853, B:202:0x0850, B:204:0x085a, B:206:0x0860, B:207:0x088c, B:208:0x0883, B:209:0x0889, B:211:0x0893, B:212:0x0898, B:214:0x08a2, B:216:0x08c3, B:220:0x08e8, B:222:0x094e, B:226:0x0965, B:228:0x0972, B:230:0x097f, B:232:0x098c, B:233:0x0999, B:236:0x09a4, B:238:0x09ae, B:240:0x09b4, B:241:0x09e0, B:242:0x09d7, B:243:0x09dd, B:245:0x09e7, B:249:0x09f6, B:258:0x0a02, B:252:0x0a31, B:254:0x0a3b, B:255:0x0a45, B:256:0x0a76, B:260:0x0a07, B:261:0x0a4e, B:262:0x0a73, B:264:0x0a7d, B:266:0x0a86, B:267:0x0ae7, B:268:0x0aab, B:270:0x0ab1, B:271:0x0ade, B:272:0x0ae4, B:273:0x0aeb, B:275:0x0af4, B:277:0x0b00, B:278:0x0b58, B:280:0x0b25, B:281:0x0b4a, B:282:0x0b55, B:284:0x0b5e, B:286:0x0b64, B:287:0x0b90, B:288:0x0b87, B:289:0x0b8d, B:291:0x0b97, B:293:0x0b9d, B:294:0x0bc9, B:295:0x0bc0, B:296:0x0bc6, B:298:0x0bd0, B:300:0x0bd8, B:301:0x0c06, B:302:0x0bfd, B:303:0x0c03, B:305:0x0c0d, B:307:0x0c19, B:308:0x0c73, B:309:0x0c3c, B:311:0x0c46, B:314:0x0c4e, B:315:0x0c59, B:317:0x0c61, B:320:0x0c6c, B:322:0x0c70, B:324:0x0c7a, B:326:0x0c86, B:327:0x0ce0, B:328:0x0ca9, B:330:0x0cb3, B:333:0x0cbb, B:334:0x0cc6, B:336:0x0cce, B:339:0x0cd9, B:341:0x0cdd, B:343:0x0ce7, B:345:0x0ced, B:346:0x0d19, B:347:0x0d10, B:348:0x0d16, B:350:0x0d20, B:352:0x0d26, B:353:0x0d52, B:354:0x0d49, B:355:0x0d4f, B:357:0x0d59, B:359:0x0d5f, B:360:0x0d90, B:361:0x0d82, B:362:0x0d8d, B:364:0x0d97, B:366:0x0d9d, B:367:0x0dce, B:368:0x0dc0, B:369:0x0dcb, B:371:0x0dd5, B:373:0x0ddb, B:374:0x0e0e, B:375:0x0e00, B:376:0x0e0b, B:379:0x0e17, B:383:0x0e3d, B:384:0x0e6f, B:386:0x0e62, B:387:0x0e29, B:388:0x0e6b, B:391:0x0e79, B:393:0x0e9b, B:394:0x0f7c, B:396:0x0ec1, B:398:0x0ee9, B:403:0x0ef5, B:407:0x0eff, B:411:0x0f0e, B:413:0x0f20, B:414:0x0f2e, B:415:0x0f26, B:416:0x0f3d, B:418:0x0f4e, B:420:0x0f58, B:422:0x0f62, B:423:0x0f6f, B:426:0x0f77, B:429:0x0f85, B:430:0x0f8a, B:433:0x0f98, B:435:0x0f9e, B:436:0x0fc5, B:438:0x0fce, B:439:0x0ff7, B:441:0x0ffd, B:442:0x102e, B:444:0x103c, B:445:0x1046, B:446:0x104d, B:447:0x104a, B:450:0x1058, B:452:0x105e, B:453:0x1085, B:454:0x108e, B:457:0x1099, B:459:0x10a1, B:460:0x10d6, B:461:0x10ae, B:462:0x10d3, B:645:0x113b, B:649:0x1144, B:654:0x1114, B:655:0x111f), top: B:5:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0f58 A[Catch: IOException -> 0x1120, XmlPullParserException -> 0x1122, all -> 0x11da, TryCatch #5 {all -> 0x11da, blocks: (B:68:0x10da, B:72:0x031b, B:74:0x032c, B:76:0x0352, B:78:0x0358, B:79:0x037e, B:80:0x0385, B:82:0x0396, B:83:0x03ba, B:85:0x03c0, B:86:0x03e6, B:88:0x03ec, B:89:0x03f3, B:91:0x0404, B:92:0x0459, B:93:0x0429, B:95:0x042f, B:96:0x0454, B:98:0x0460, B:100:0x0466, B:101:0x0492, B:102:0x0489, B:103:0x048f, B:105:0x0499, B:107:0x04a6, B:109:0x04ca, B:112:0x04f2, B:113:0x04fb, B:114:0x04f8, B:116:0x0502, B:118:0x0510, B:120:0x053a, B:122:0x053e, B:124:0x0541, B:125:0x0548, B:127:0x054c, B:128:0x0533, B:129:0x0549, B:130:0x0551, B:132:0x0557, B:133:0x057f, B:134:0x057a, B:135:0x0584, B:137:0x058a, B:138:0x05b2, B:139:0x05ad, B:141:0x05b9, B:143:0x05bf, B:144:0x0615, B:145:0x05e2, B:147:0x05ea, B:149:0x05f1, B:150:0x0611, B:151:0x0612, B:152:0x061a, B:154:0x0637, B:155:0x06e4, B:156:0x065d, B:158:0x0663, B:159:0x0688, B:161:0x068e, B:162:0x06b3, B:164:0x06bb, B:166:0x06bf, B:167:0x06c6, B:169:0x06d0, B:170:0x06e1, B:171:0x06db, B:174:0x06e9, B:175:0x071b, B:177:0x071c, B:178:0x073f, B:179:0x0740, B:180:0x0749, B:182:0x074f, B:183:0x0777, B:184:0x0772, B:185:0x077c, B:187:0x0782, B:188:0x07aa, B:189:0x07a5, B:191:0x07b1, B:193:0x07ba, B:194:0x07e3, B:196:0x07eb, B:197:0x0814, B:199:0x0826, B:200:0x0830, B:201:0x0853, B:202:0x0850, B:204:0x085a, B:206:0x0860, B:207:0x088c, B:208:0x0883, B:209:0x0889, B:211:0x0893, B:212:0x0898, B:214:0x08a2, B:216:0x08c3, B:220:0x08e8, B:222:0x094e, B:226:0x0965, B:228:0x0972, B:230:0x097f, B:232:0x098c, B:233:0x0999, B:236:0x09a4, B:238:0x09ae, B:240:0x09b4, B:241:0x09e0, B:242:0x09d7, B:243:0x09dd, B:245:0x09e7, B:249:0x09f6, B:258:0x0a02, B:252:0x0a31, B:254:0x0a3b, B:255:0x0a45, B:256:0x0a76, B:260:0x0a07, B:261:0x0a4e, B:262:0x0a73, B:264:0x0a7d, B:266:0x0a86, B:267:0x0ae7, B:268:0x0aab, B:270:0x0ab1, B:271:0x0ade, B:272:0x0ae4, B:273:0x0aeb, B:275:0x0af4, B:277:0x0b00, B:278:0x0b58, B:280:0x0b25, B:281:0x0b4a, B:282:0x0b55, B:284:0x0b5e, B:286:0x0b64, B:287:0x0b90, B:288:0x0b87, B:289:0x0b8d, B:291:0x0b97, B:293:0x0b9d, B:294:0x0bc9, B:295:0x0bc0, B:296:0x0bc6, B:298:0x0bd0, B:300:0x0bd8, B:301:0x0c06, B:302:0x0bfd, B:303:0x0c03, B:305:0x0c0d, B:307:0x0c19, B:308:0x0c73, B:309:0x0c3c, B:311:0x0c46, B:314:0x0c4e, B:315:0x0c59, B:317:0x0c61, B:320:0x0c6c, B:322:0x0c70, B:324:0x0c7a, B:326:0x0c86, B:327:0x0ce0, B:328:0x0ca9, B:330:0x0cb3, B:333:0x0cbb, B:334:0x0cc6, B:336:0x0cce, B:339:0x0cd9, B:341:0x0cdd, B:343:0x0ce7, B:345:0x0ced, B:346:0x0d19, B:347:0x0d10, B:348:0x0d16, B:350:0x0d20, B:352:0x0d26, B:353:0x0d52, B:354:0x0d49, B:355:0x0d4f, B:357:0x0d59, B:359:0x0d5f, B:360:0x0d90, B:361:0x0d82, B:362:0x0d8d, B:364:0x0d97, B:366:0x0d9d, B:367:0x0dce, B:368:0x0dc0, B:369:0x0dcb, B:371:0x0dd5, B:373:0x0ddb, B:374:0x0e0e, B:375:0x0e00, B:376:0x0e0b, B:379:0x0e17, B:383:0x0e3d, B:384:0x0e6f, B:386:0x0e62, B:387:0x0e29, B:388:0x0e6b, B:391:0x0e79, B:393:0x0e9b, B:394:0x0f7c, B:396:0x0ec1, B:398:0x0ee9, B:403:0x0ef5, B:407:0x0eff, B:411:0x0f0e, B:413:0x0f20, B:414:0x0f2e, B:415:0x0f26, B:416:0x0f3d, B:418:0x0f4e, B:420:0x0f58, B:422:0x0f62, B:423:0x0f6f, B:426:0x0f77, B:429:0x0f85, B:430:0x0f8a, B:433:0x0f98, B:435:0x0f9e, B:436:0x0fc5, B:438:0x0fce, B:439:0x0ff7, B:441:0x0ffd, B:442:0x102e, B:444:0x103c, B:445:0x1046, B:446:0x104d, B:447:0x104a, B:450:0x1058, B:452:0x105e, B:453:0x1085, B:454:0x108e, B:457:0x1099, B:459:0x10a1, B:460:0x10d6, B:461:0x10ae, B:462:0x10d3, B:645:0x113b, B:649:0x1144, B:654:0x1114, B:655:0x111f), top: B:5:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0f62 A[Catch: IOException -> 0x1120, XmlPullParserException -> 0x1122, all -> 0x11da, TryCatch #5 {all -> 0x11da, blocks: (B:68:0x10da, B:72:0x031b, B:74:0x032c, B:76:0x0352, B:78:0x0358, B:79:0x037e, B:80:0x0385, B:82:0x0396, B:83:0x03ba, B:85:0x03c0, B:86:0x03e6, B:88:0x03ec, B:89:0x03f3, B:91:0x0404, B:92:0x0459, B:93:0x0429, B:95:0x042f, B:96:0x0454, B:98:0x0460, B:100:0x0466, B:101:0x0492, B:102:0x0489, B:103:0x048f, B:105:0x0499, B:107:0x04a6, B:109:0x04ca, B:112:0x04f2, B:113:0x04fb, B:114:0x04f8, B:116:0x0502, B:118:0x0510, B:120:0x053a, B:122:0x053e, B:124:0x0541, B:125:0x0548, B:127:0x054c, B:128:0x0533, B:129:0x0549, B:130:0x0551, B:132:0x0557, B:133:0x057f, B:134:0x057a, B:135:0x0584, B:137:0x058a, B:138:0x05b2, B:139:0x05ad, B:141:0x05b9, B:143:0x05bf, B:144:0x0615, B:145:0x05e2, B:147:0x05ea, B:149:0x05f1, B:150:0x0611, B:151:0x0612, B:152:0x061a, B:154:0x0637, B:155:0x06e4, B:156:0x065d, B:158:0x0663, B:159:0x0688, B:161:0x068e, B:162:0x06b3, B:164:0x06bb, B:166:0x06bf, B:167:0x06c6, B:169:0x06d0, B:170:0x06e1, B:171:0x06db, B:174:0x06e9, B:175:0x071b, B:177:0x071c, B:178:0x073f, B:179:0x0740, B:180:0x0749, B:182:0x074f, B:183:0x0777, B:184:0x0772, B:185:0x077c, B:187:0x0782, B:188:0x07aa, B:189:0x07a5, B:191:0x07b1, B:193:0x07ba, B:194:0x07e3, B:196:0x07eb, B:197:0x0814, B:199:0x0826, B:200:0x0830, B:201:0x0853, B:202:0x0850, B:204:0x085a, B:206:0x0860, B:207:0x088c, B:208:0x0883, B:209:0x0889, B:211:0x0893, B:212:0x0898, B:214:0x08a2, B:216:0x08c3, B:220:0x08e8, B:222:0x094e, B:226:0x0965, B:228:0x0972, B:230:0x097f, B:232:0x098c, B:233:0x0999, B:236:0x09a4, B:238:0x09ae, B:240:0x09b4, B:241:0x09e0, B:242:0x09d7, B:243:0x09dd, B:245:0x09e7, B:249:0x09f6, B:258:0x0a02, B:252:0x0a31, B:254:0x0a3b, B:255:0x0a45, B:256:0x0a76, B:260:0x0a07, B:261:0x0a4e, B:262:0x0a73, B:264:0x0a7d, B:266:0x0a86, B:267:0x0ae7, B:268:0x0aab, B:270:0x0ab1, B:271:0x0ade, B:272:0x0ae4, B:273:0x0aeb, B:275:0x0af4, B:277:0x0b00, B:278:0x0b58, B:280:0x0b25, B:281:0x0b4a, B:282:0x0b55, B:284:0x0b5e, B:286:0x0b64, B:287:0x0b90, B:288:0x0b87, B:289:0x0b8d, B:291:0x0b97, B:293:0x0b9d, B:294:0x0bc9, B:295:0x0bc0, B:296:0x0bc6, B:298:0x0bd0, B:300:0x0bd8, B:301:0x0c06, B:302:0x0bfd, B:303:0x0c03, B:305:0x0c0d, B:307:0x0c19, B:308:0x0c73, B:309:0x0c3c, B:311:0x0c46, B:314:0x0c4e, B:315:0x0c59, B:317:0x0c61, B:320:0x0c6c, B:322:0x0c70, B:324:0x0c7a, B:326:0x0c86, B:327:0x0ce0, B:328:0x0ca9, B:330:0x0cb3, B:333:0x0cbb, B:334:0x0cc6, B:336:0x0cce, B:339:0x0cd9, B:341:0x0cdd, B:343:0x0ce7, B:345:0x0ced, B:346:0x0d19, B:347:0x0d10, B:348:0x0d16, B:350:0x0d20, B:352:0x0d26, B:353:0x0d52, B:354:0x0d49, B:355:0x0d4f, B:357:0x0d59, B:359:0x0d5f, B:360:0x0d90, B:361:0x0d82, B:362:0x0d8d, B:364:0x0d97, B:366:0x0d9d, B:367:0x0dce, B:368:0x0dc0, B:369:0x0dcb, B:371:0x0dd5, B:373:0x0ddb, B:374:0x0e0e, B:375:0x0e00, B:376:0x0e0b, B:379:0x0e17, B:383:0x0e3d, B:384:0x0e6f, B:386:0x0e62, B:387:0x0e29, B:388:0x0e6b, B:391:0x0e79, B:393:0x0e9b, B:394:0x0f7c, B:396:0x0ec1, B:398:0x0ee9, B:403:0x0ef5, B:407:0x0eff, B:411:0x0f0e, B:413:0x0f20, B:414:0x0f2e, B:415:0x0f26, B:416:0x0f3d, B:418:0x0f4e, B:420:0x0f58, B:422:0x0f62, B:423:0x0f6f, B:426:0x0f77, B:429:0x0f85, B:430:0x0f8a, B:433:0x0f98, B:435:0x0f9e, B:436:0x0fc5, B:438:0x0fce, B:439:0x0ff7, B:441:0x0ffd, B:442:0x102e, B:444:0x103c, B:445:0x1046, B:446:0x104d, B:447:0x104a, B:450:0x1058, B:452:0x105e, B:453:0x1085, B:454:0x108e, B:457:0x1099, B:459:0x10a1, B:460:0x10d6, B:461:0x10ae, B:462:0x10d3, B:645:0x113b, B:649:0x1144, B:654:0x1114, B:655:0x111f), top: B:5:0x0029 }] */
    /* JADX WARN: Removed duplicated region for block: B:585:0x1150  */
    /* JADX WARN: Removed duplicated region for block: B:588:0x1163  */
    /* JADX WARN: Removed duplicated region for block: B:591:0x116e  */
    /* JADX WARN: Removed duplicated region for block: B:594:0x117f  */
    /* JADX WARN: Removed duplicated region for block: B:597:0x118f  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x119d  */
    /* JADX WARN: Removed duplicated region for block: B:609:0x11c6  */
    /* JADX WARN: Removed duplicated region for block: B:618:0x1174  */
    /* JADX WARN: Removed duplicated region for block: B:619:0x115c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void readPermissionsFromXml(XmlPullParser xmlPullParser, File file, int i) {
        Throwable th;
        FileReader fileReader;
        String str;
        FileReader fileReader2;
        String str2;
        XmlPullParserException xmlPullParserException;
        IOException iOException;
        int i2;
        int version;
        Iterator<String> it;
        int next;
        int i3;
        String str3;
        char c;
        boolean z;
        boolean z2;
        boolean z3;
        boolean exists;
        int parseInt;
        boolean z4;
        String str4 = "Got exception parsing permissions.";
        try {
            FileReader fileReader3 = new FileReader(file);
            Slog.i(TAG, "Reading permissions from " + file);
            boolean isLowRamDeviceStatic = ActivityManager.isLowRamDeviceStatic();
            try {
                try {
                    try {
                        xmlPullParser.setInput(fileReader3);
                        try {
                            do {
                                try {
                                    next = xmlPullParser.next();
                                    i3 = 1;
                                    if (next != 2) {
                                    }
                                    break;
                                } catch (XmlPullParserException e) {
                                    fileReader2 = fileReader3;
                                    xmlPullParserException = e;
                                    str2 = str4;
                                }
                            } while (next != 1);
                            break;
                            if (next != 2) {
                                throw new XmlPullParserException("No start tag found");
                            }
                            if (!xmlPullParser.getName().equals("permissions")) {
                                try {
                                    if (!xmlPullParser.getName().equals("config")) {
                                        if (this.mSystemConfigExt.skipTagExceptionAndReturn(xmlPullParser.getName(), file)) {
                                            IoUtils.closeQuietly(fileReader3);
                                            return;
                                        }
                                        throw new XmlPullParserException("Unexpected start tag in " + file + ": found " + xmlPullParser.getName() + ", expected 'permissions' or 'config'");
                                    }
                                } catch (IOException e2) {
                                    iOException = e2;
                                    str = "Got exception parsing permissions.";
                                    fileReader2 = fileReader3;
                                    Slog.w(TAG, str, iOException);
                                    IoUtils.closeQuietly(fileReader2);
                                    if (StorageManager.isFileEncrypted()) {
                                    }
                                    if (StorageManager.hasAdoptable()) {
                                    }
                                    if (ActivityManager.isLowRamDeviceStatic()) {
                                    }
                                    version = IncrementalManager.getVersion();
                                    if (version > 0) {
                                    }
                                    addFeature("android.software.app_enumeration", i2);
                                    if (Build.VERSION.DEVICE_INITIAL_SDK_INT >= 29) {
                                    }
                                    enableIpSecTunnelMigrationOnVsrUAndAbove();
                                    if (isErofsSupported()) {
                                    }
                                    it = this.mUnavailableFeatures.iterator();
                                    while (it.hasNext()) {
                                    }
                                } catch (XmlPullParserException e3) {
                                    xmlPullParserException = e3;
                                    str2 = "Got exception parsing permissions.";
                                    fileReader2 = fileReader3;
                                    Slog.w(TAG, str2, xmlPullParserException);
                                    IoUtils.closeQuietly(fileReader2);
                                    if (StorageManager.isFileEncrypted()) {
                                    }
                                    if (StorageManager.hasAdoptable()) {
                                    }
                                    if (ActivityManager.isLowRamDeviceStatic()) {
                                    }
                                    version = IncrementalManager.getVersion();
                                    if (version > 0) {
                                    }
                                    addFeature("android.software.app_enumeration", i2);
                                    if (Build.VERSION.DEVICE_INITIAL_SDK_INT >= 29) {
                                    }
                                    enableIpSecTunnelMigrationOnVsrUAndAbove();
                                    if (isErofsSupported()) {
                                    }
                                    it = this.mUnavailableFeatures.iterator();
                                    while (it.hasNext()) {
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    fileReader = fileReader3;
                                    IoUtils.closeQuietly(fileReader);
                                    throw th;
                                }
                            }
                            boolean z5 = i == -1;
                            boolean z6 = (i & 2) != 0;
                            boolean z7 = (i & 1) != 0;
                            boolean z8 = (i & 4) != 0;
                            boolean z9 = (i & 8) != 0;
                            boolean z10 = (i & 16) != 0;
                            boolean z11 = (i & 32) != 0;
                            boolean z12 = (i & 64) != 0;
                            boolean z13 = (i & 128) != 0;
                            boolean z14 = (i & 256) != 0;
                            boolean z15 = (i & 512) != 0;
                            boolean z16 = (i & 1024) != 0;
                            while (true) {
                                XmlUtils.nextElement(xmlPullParser);
                                if (xmlPullParser.getEventType() != i3) {
                                    String name = xmlPullParser.getName();
                                    if (name != null) {
                                        switch (name.hashCode()) {
                                            case -2040330235:
                                                if (name.equals("allow-unthrottled-location")) {
                                                    c = 11;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -1882490007:
                                                if (name.equals("allow-in-power-save")) {
                                                    c = '\t';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -1582324217:
                                                if (name.equals("allow-adas-location-settings")) {
                                                    c = '\f';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -1554938271:
                                                if (name.equals("named-actor")) {
                                                    c = 29;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -1461465444:
                                                if (name.equals("component-override")) {
                                                    c = 18;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -1390350881:
                                                if (name.equals("install-constraints-allowed")) {
                                                    c = '#';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -1005864890:
                                                if (name.equals("disabled-until-used-preinstalled-carrier-app")) {
                                                    c = 21;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -980620291:
                                                if (name.equals("allow-association")) {
                                                    c = 25;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -979207434:
                                                if (name.equals("feature")) {
                                                    c = 6;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -972849788:
                                                if (name.equals("automatic-rollback-denylisted-app")) {
                                                    c = ' ';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -828905863:
                                                if (name.equals("unavailable-feature")) {
                                                    c = 7;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -642819164:
                                                if (name.equals("allow-in-power-save-except-idle")) {
                                                    c = '\b';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -634266752:
                                                if (name.equals("bg-restriction-exemption")) {
                                                    c = 16;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -625731345:
                                                if (name.equals("asl-file")) {
                                                    c = '&';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -560717308:
                                                if (name.equals("allow-ignore-location-settings")) {
                                                    c = '\r';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -517618225:
                                                if (name.equals("permission")) {
                                                    c = 1;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case -150068154:
                                                if (name.equals("install-in-user-type")) {
                                                    c = 28;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 98629247:
                                                if (name.equals("group")) {
                                                    c = 0;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 166208699:
                                                if (name.equals("library")) {
                                                    c = 5;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 180165796:
                                                if (name.equals("hidden-api-whitelisted-app")) {
                                                    c = 24;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 347247519:
                                                if (name.equals("backup-transport-whitelisted-service")) {
                                                    c = 19;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 414198242:
                                                if (name.equals("allowed-vendor-apex")) {
                                                    c = '\"';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 783200107:
                                                if (name.equals("update-ownership")) {
                                                    c = '$';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 802332808:
                                                if (name.equals("allow-in-data-usage-save")) {
                                                    c = '\n';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 953292141:
                                                if (name.equals("assign-permission")) {
                                                    c = 2;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 968751633:
                                                if (name.equals("rollback-whitelisted-app")) {
                                                    c = 31;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1005096720:
                                                if (name.equals("apex-library")) {
                                                    c = 4;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1044015374:
                                                if (name.equals("oem-permissions")) {
                                                    c = 23;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1046683496:
                                                if (name.equals("whitelisted-staged-installer")) {
                                                    c = '!';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1121420326:
                                                if (name.equals("app-link")) {
                                                    c = 15;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1269564002:
                                                if (name.equals("split-permission")) {
                                                    c = 3;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1347585732:
                                                if (name.equals("app-data-isolation-whitelisted-app")) {
                                                    c = 26;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1567330472:
                                                if (name.equals("default-enabled-vr-app")) {
                                                    c = 17;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1633270165:
                                                if (name.equals("disabled-until-used-preinstalled-carrier-associated-app")) {
                                                    c = 20;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1723146313:
                                                if (name.equals("privapp-permissions")) {
                                                    c = 22;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1723586945:
                                                if (name.equals("bugreport-whitelisted")) {
                                                    c = 27;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1748566401:
                                                if (name.equals("initial-package-state")) {
                                                    c = '%';
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1793277898:
                                                if (name.equals("overlay-config-signature")) {
                                                    c = 30;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            case 1954925533:
                                                if (name.equals("allow-implicit-broadcast")) {
                                                    c = 14;
                                                    break;
                                                }
                                                c = 65535;
                                                break;
                                            default:
                                                c = 65535;
                                                break;
                                        }
                                        FileReader fileReader4 = fileReader3;
                                        String str5 = str4;
                                        boolean z17 = isLowRamDeviceStatic;
                                        boolean z18 = z15;
                                        boolean z19 = z14;
                                        boolean z20 = z13;
                                        String str6 = null;
                                        switch (c) {
                                            case 0:
                                                z = z16;
                                                if (z5) {
                                                    String attributeValue = xmlPullParser.getAttributeValue(null, "gid");
                                                    if (attributeValue != null) {
                                                        this.mGlobalGids = ArrayUtils.appendInt(this.mGlobalGids, Process.getGidForName(attributeValue));
                                                    } else {
                                                        Slog.w(TAG, "<" + name + "> without gid in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 1:
                                                z = z16;
                                                if (z8) {
                                                    String attributeValue2 = xmlPullParser.getAttributeValue(null, "name");
                                                    if (attributeValue2 == null) {
                                                        Slog.w(TAG, "<" + name + "> without name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                        XmlUtils.skipCurrentTag(xmlPullParser);
                                                    } else {
                                                        readPermission(xmlPullParser, attributeValue2.intern());
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                    XmlUtils.skipCurrentTag(xmlPullParser);
                                                }
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 2:
                                                z = z16;
                                                if (z8) {
                                                    String attributeValue3 = xmlPullParser.getAttributeValue(null, "name");
                                                    if (attributeValue3 == null) {
                                                        Slog.w(TAG, "<" + name + "> without name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                        XmlUtils.skipCurrentTag(xmlPullParser);
                                                    } else {
                                                        String attributeValue4 = xmlPullParser.getAttributeValue(null, "uid");
                                                        if (attributeValue4 == null) {
                                                            Slog.w(TAG, "<" + name + "> without uid in " + file + " at " + xmlPullParser.getPositionDescription());
                                                            XmlUtils.skipCurrentTag(xmlPullParser);
                                                        } else {
                                                            int uidForName = Process.getUidForName(attributeValue4);
                                                            if (uidForName < 0) {
                                                                Slog.w(TAG, "<" + name + "> with unknown uid \"" + attributeValue4 + "  in " + file + " at " + xmlPullParser.getPositionDescription());
                                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                            } else {
                                                                String intern = attributeValue3.intern();
                                                                ArraySet<String> arraySet = this.mSystemPermissions.get(uidForName);
                                                                if (arraySet == null) {
                                                                    arraySet = new ArraySet<>();
                                                                    this.mSystemPermissions.put(uidForName, arraySet);
                                                                }
                                                                arraySet.add(intern);
                                                            }
                                                        }
                                                    }
                                                    z16 = z;
                                                    fileReader3 = fileReader4;
                                                    str4 = str5;
                                                    isLowRamDeviceStatic = z17;
                                                    z15 = z18;
                                                    z14 = z19;
                                                    z13 = z20;
                                                    i3 = 1;
                                                    break;
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                            case 3:
                                                z = z16;
                                                if (z8) {
                                                    readSplitPermission(xmlPullParser, file);
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                    XmlUtils.skipCurrentTag(xmlPullParser);
                                                }
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 4:
                                            case 5:
                                                if (z6) {
                                                    String attributeValue5 = xmlPullParser.getAttributeValue(null, "name");
                                                    String attributeValue6 = xmlPullParser.getAttributeValue(null, "file");
                                                    String attributeValue7 = xmlPullParser.getAttributeValue(null, "dependency");
                                                    String attributeValue8 = xmlPullParser.getAttributeValue(null, "min-device-sdk");
                                                    z = z16;
                                                    String attributeValue9 = xmlPullParser.getAttributeValue(null, "max-device-sdk");
                                                    if (attributeValue5 == null) {
                                                        Slog.w(TAG, "<" + name + "> without name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else if (attributeValue6 == null) {
                                                        Slog.w(TAG, "<" + name + "> without file in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        if (attributeValue8 != null && !isAtLeastSdkLevel(attributeValue8)) {
                                                            z2 = false;
                                                            if (attributeValue9 != null && !isAtMostSdkLevel(attributeValue9)) {
                                                                z3 = false;
                                                                exists = new File(attributeValue6).exists();
                                                                if (!z2 && z3 && exists) {
                                                                    this.mSharedLibraries.put(attributeValue5, new SharedLibraryEntry(attributeValue5, attributeValue6, attributeValue7 == null ? new String[0] : attributeValue7.split(":"), xmlPullParser.getAttributeValue(null, "on-bootclasspath-since"), xmlPullParser.getAttributeValue(null, "on-bootclasspath-before")));
                                                                } else {
                                                                    StringBuilder sb = new StringBuilder("Ignore shared library ");
                                                                    sb.append(attributeValue5);
                                                                    sb.append(":");
                                                                    if (!z2) {
                                                                        sb.append(" min-device-sdk=");
                                                                        sb.append(attributeValue8);
                                                                    }
                                                                    if (!z3) {
                                                                        sb.append(" max-device-sdk=");
                                                                        sb.append(attributeValue9);
                                                                    }
                                                                    if (!exists) {
                                                                        sb.append(" ");
                                                                        sb.append(attributeValue6);
                                                                        sb.append(" does not exist");
                                                                    }
                                                                    Slog.i(TAG, sb.toString());
                                                                }
                                                            }
                                                            z3 = true;
                                                            exists = new File(attributeValue6).exists();
                                                            if (!z2) {
                                                            }
                                                            StringBuilder sb2 = new StringBuilder("Ignore shared library ");
                                                            sb2.append(attributeValue5);
                                                            sb2.append(":");
                                                            if (!z2) {
                                                            }
                                                            if (!z3) {
                                                            }
                                                            if (!exists) {
                                                            }
                                                            Slog.i(TAG, sb2.toString());
                                                        }
                                                        z2 = true;
                                                        if (attributeValue9 != null) {
                                                            z3 = false;
                                                            exists = new File(attributeValue6).exists();
                                                            if (!z2) {
                                                            }
                                                            StringBuilder sb22 = new StringBuilder("Ignore shared library ");
                                                            sb22.append(attributeValue5);
                                                            sb22.append(":");
                                                            if (!z2) {
                                                            }
                                                            if (!z3) {
                                                            }
                                                            if (!exists) {
                                                            }
                                                            Slog.i(TAG, sb22.toString());
                                                        }
                                                        z3 = true;
                                                        exists = new File(attributeValue6).exists();
                                                        if (!z2) {
                                                        }
                                                        StringBuilder sb222 = new StringBuilder("Ignore shared library ");
                                                        sb222.append(attributeValue5);
                                                        sb222.append(":");
                                                        if (!z2) {
                                                        }
                                                        if (!z3) {
                                                        }
                                                        if (!exists) {
                                                        }
                                                        Slog.i(TAG, sb222.toString());
                                                    }
                                                } else {
                                                    z = z16;
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 6:
                                                if (z7) {
                                                    String attributeValue10 = xmlPullParser.getAttributeValue(null, "name");
                                                    int readIntAttribute = XmlUtils.readIntAttribute(xmlPullParser, "version", 0);
                                                    boolean z21 = !z17 ? true : !"true".equals(xmlPullParser.getAttributeValue(null, "notLowRam"));
                                                    if (attributeValue10 == null) {
                                                        Slog.w(TAG, "<" + name + "> without name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else if (z21) {
                                                        addFeature(attributeValue10, readIntAttribute);
                                                        this.mSystemConfigExt.onAddFeatureInRead(attributeValue10, readIntAttribute, file);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 7:
                                                if (z7) {
                                                    String attributeValue11 = xmlPullParser.getAttributeValue(null, "name");
                                                    if (attributeValue11 == null) {
                                                        Slog.w(TAG, "<" + name + "> without name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mUnavailableFeatures.add(attributeValue11);
                                                        this.mSystemConfigExt.onAddUnAvailableFeatureInRead(attributeValue11, file);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '\b':
                                                if (z19) {
                                                    String attributeValue12 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue12 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mAllowInPowerSaveExceptIdle.add(attributeValue12);
                                                        this.mSystemConfigExt.addPowerSaveWhitelistExceptIdleForCota(attributeValue12);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '\t':
                                                if (z19) {
                                                    String attributeValue13 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue13 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mAllowInPowerSave.add(attributeValue13);
                                                        this.mSystemConfigExt.addPowerSaveWhitelistForCota(attributeValue13);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '\n':
                                                if (z19) {
                                                    String attributeValue14 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue14 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mAllowInDataUsageSave.add(attributeValue14);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 11:
                                                if (z19) {
                                                    String attributeValue15 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue15 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mAllowUnthrottledLocation.add(attributeValue15);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '\f':
                                                if (z19) {
                                                    String attributeValue16 = xmlPullParser.getAttributeValue(null, "package");
                                                    String attributeValue17 = xmlPullParser.getAttributeValue(null, "attributionTag");
                                                    if (attributeValue16 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        ArraySet<String> arraySet2 = this.mAllowAdasSettings.get(attributeValue16);
                                                        if (arraySet2 == null || !arraySet2.isEmpty()) {
                                                            if (arraySet2 == null) {
                                                                arraySet2 = new ArraySet<>(1);
                                                                this.mAllowAdasSettings.put(attributeValue16, arraySet2);
                                                            }
                                                            if (!"*".equals(attributeValue17)) {
                                                                if (!"null".equals(attributeValue17)) {
                                                                    str6 = attributeValue17;
                                                                }
                                                                arraySet2.add(str6);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '\r':
                                                if (z19) {
                                                    String attributeValue18 = xmlPullParser.getAttributeValue(null, "package");
                                                    String attributeValue19 = xmlPullParser.getAttributeValue(null, "attributionTag");
                                                    if (attributeValue18 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        ArraySet<String> arraySet3 = this.mAllowIgnoreLocationSettings.get(attributeValue18);
                                                        if (arraySet3 == null || !arraySet3.isEmpty()) {
                                                            if (arraySet3 == null) {
                                                                arraySet3 = new ArraySet<>(1);
                                                                this.mAllowIgnoreLocationSettings.put(attributeValue18, arraySet3);
                                                            }
                                                            if (!"*".equals(attributeValue19)) {
                                                                if (!"null".equals(attributeValue19)) {
                                                                    str6 = attributeValue19;
                                                                }
                                                                arraySet3.add(str6);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 14:
                                                if (z18) {
                                                    String attributeValue20 = xmlPullParser.getAttributeValue(null, "action");
                                                    if (attributeValue20 == null) {
                                                        Slog.w(TAG, "<" + name + "> without action in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mAllowImplicitBroadcasts.add(attributeValue20);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 15:
                                                if (z9) {
                                                    String attributeValue21 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue21 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mLinkedApps.add(attributeValue21);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 16:
                                                if (z19) {
                                                    String attributeValue22 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue22 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mBgRestrictionExemption.add(attributeValue22);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 17:
                                                if (z9) {
                                                    String attributeValue23 = xmlPullParser.getAttributeValue(null, "package");
                                                    String attributeValue24 = xmlPullParser.getAttributeValue(null, "class");
                                                    if (attributeValue23 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else if (attributeValue24 == null) {
                                                        Slog.w(TAG, "<" + name + "> without class in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mDefaultVrComponents.add(new ComponentName(attributeValue23, attributeValue24));
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 18:
                                                readComponentOverrides(xmlPullParser, file);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 19:
                                                if (z7) {
                                                    String attributeValue25 = xmlPullParser.getAttributeValue(null, HostingRecord.HOSTING_TYPE_SERVICE);
                                                    if (attributeValue25 == null) {
                                                        Slog.w(TAG, "<" + name + "> without service in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        ComponentName unflattenFromString = ComponentName.unflattenFromString(attributeValue25);
                                                        if (unflattenFromString == null) {
                                                            Slog.w(TAG, "<" + name + "> with invalid service name " + attributeValue25 + " in " + file + " at " + xmlPullParser.getPositionDescription());
                                                        } else {
                                                            this.mBackupTransportWhitelist.add(unflattenFromString);
                                                        }
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 20:
                                                if (z9) {
                                                    String attributeValue26 = xmlPullParser.getAttributeValue(null, "package");
                                                    String attributeValue27 = xmlPullParser.getAttributeValue(null, "carrierAppPackage");
                                                    if (attributeValue26 != null && attributeValue27 != null) {
                                                        String attributeValue28 = xmlPullParser.getAttributeValue(null, "addedInSdk");
                                                        if (TextUtils.isEmpty(attributeValue28)) {
                                                            parseInt = -1;
                                                        } else {
                                                            try {
                                                                parseInt = Integer.parseInt(attributeValue28);
                                                            } catch (NumberFormatException unused) {
                                                                Slog.w(TAG, "<" + name + "> addedInSdk not an integer in " + file + " at " + xmlPullParser.getPositionDescription());
                                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                            }
                                                        }
                                                        List<CarrierAssociatedAppEntry> list = this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.get(attributeValue27);
                                                        if (list == null) {
                                                            list = new ArrayList<>();
                                                            this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.put(attributeValue27, list);
                                                        }
                                                        list.add(new CarrierAssociatedAppEntry(attributeValue26, parseInt));
                                                    }
                                                    Slog.w(TAG, "<" + name + "> without package or carrierAppPackage in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 21:
                                                if (z9) {
                                                    String attributeValue29 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue29 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mDisabledUntilUsedPreinstalledCarrierApps.add(attributeValue29);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 22:
                                                if (z10) {
                                                    if (!file.toPath().startsWith(Environment.getVendorDirectory().toPath() + "/")) {
                                                        if (!file.toPath().startsWith(Environment.getOdmDirectory().toPath() + "/")) {
                                                            z4 = false;
                                                            boolean startsWith = file.toPath().startsWith(Environment.getProductDirectory().toPath() + "/") | this.mSystemConfigExt.isFilePartitionWithProductFlag(file);
                                                            boolean startsWith2 = file.toPath().startsWith(Environment.getSystemExtDirectory().toPath() + "/");
                                                            Path path = file.toPath();
                                                            StringBuilder sb3 = new StringBuilder();
                                                            sb3.append(Environment.getApexDirectory().toPath());
                                                            sb3.append("/");
                                                            boolean z22 = !path.startsWith(sb3.toString()) && ((Boolean) ApexProperties.updatable().orElse(Boolean.FALSE)).booleanValue();
                                                            if (!z4) {
                                                                readPrivAppPermissions(xmlPullParser, this.mPermissionAllowlist.getVendorPrivilegedAppAllowlist());
                                                            } else if (startsWith) {
                                                                readPrivAppPermissions(xmlPullParser, this.mPermissionAllowlist.getProductPrivilegedAppAllowlist());
                                                            } else if (startsWith2) {
                                                                readPrivAppPermissions(xmlPullParser, this.mPermissionAllowlist.getSystemExtPrivilegedAppAllowlist());
                                                            } else if (z22) {
                                                                readApexPrivAppPermissions(xmlPullParser, file, Environment.getApexDirectory().toPath());
                                                            } else {
                                                                readPrivAppPermissions(xmlPullParser, this.mPermissionAllowlist.getPrivilegedAppAllowlist());
                                                            }
                                                        }
                                                    }
                                                    z4 = true;
                                                    boolean startsWith3 = file.toPath().startsWith(Environment.getProductDirectory().toPath() + "/") | this.mSystemConfigExt.isFilePartitionWithProductFlag(file);
                                                    boolean startsWith22 = file.toPath().startsWith(Environment.getSystemExtDirectory().toPath() + "/");
                                                    Path path2 = file.toPath();
                                                    StringBuilder sb32 = new StringBuilder();
                                                    sb32.append(Environment.getApexDirectory().toPath());
                                                    sb32.append("/");
                                                    if (path2.startsWith(sb32.toString())) {
                                                    }
                                                    if (!z4) {
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                    XmlUtils.skipCurrentTag(xmlPullParser);
                                                }
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 23:
                                                if (z11) {
                                                    readOemPermissions(xmlPullParser);
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                    XmlUtils.skipCurrentTag(xmlPullParser);
                                                }
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 24:
                                                if (z12) {
                                                    String attributeValue30 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue30 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mHiddenApiPackageWhitelist.add(attributeValue30);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 25:
                                                if (z20) {
                                                    String attributeValue31 = xmlPullParser.getAttributeValue(null, "target");
                                                    if (attributeValue31 == null) {
                                                        Slog.w(TAG, "<" + name + "> without target in " + file + " at " + xmlPullParser.getPositionDescription());
                                                        XmlUtils.skipCurrentTag(xmlPullParser);
                                                    } else {
                                                        String attributeValue32 = xmlPullParser.getAttributeValue(null, "allowed");
                                                        if (attributeValue32 == null) {
                                                            Slog.w(TAG, "<" + name + "> without allowed in " + file + " at " + xmlPullParser.getPositionDescription());
                                                            XmlUtils.skipCurrentTag(xmlPullParser);
                                                        } else {
                                                            String intern2 = attributeValue31.intern();
                                                            String intern3 = attributeValue32.intern();
                                                            ArraySet<String> arraySet4 = this.mAllowedAssociations.get(intern2);
                                                            if (arraySet4 == null) {
                                                                arraySet4 = new ArraySet<>();
                                                                this.mAllowedAssociations.put(intern2, arraySet4);
                                                            }
                                                            Slog.i(TAG, "Adding association: " + intern2 + " <- " + intern3);
                                                            arraySet4.add(intern3);
                                                        }
                                                    }
                                                    z = z16;
                                                    z16 = z;
                                                    fileReader3 = fileReader4;
                                                    str4 = str5;
                                                    isLowRamDeviceStatic = z17;
                                                    z15 = z18;
                                                    z14 = z19;
                                                    z13 = z20;
                                                    i3 = 1;
                                                    break;
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                            case 26:
                                                String attributeValue33 = xmlPullParser.getAttributeValue(null, "package");
                                                if (attributeValue33 == null) {
                                                    Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    this.mAppDataIsolationWhitelistedApps.add(attributeValue33);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 27:
                                                String attributeValue34 = xmlPullParser.getAttributeValue(null, "package");
                                                if (attributeValue34 == null) {
                                                    Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    this.mBugreportWhitelistedPackages.add(attributeValue34);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 28:
                                                readInstallInUserType(xmlPullParser, this.mPackageToUserTypeWhitelist, this.mPackageToUserTypeBlacklist);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 29:
                                                String safeIntern = TextUtils.safeIntern(xmlPullParser.getAttributeValue(null, "namespace"));
                                                String attributeValue35 = xmlPullParser.getAttributeValue(null, "name");
                                                String safeIntern2 = TextUtils.safeIntern(xmlPullParser.getAttributeValue(null, "package"));
                                                if (TextUtils.isEmpty(safeIntern)) {
                                                    Slog.wtf(TAG, "<" + name + "> without namespace in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else if (TextUtils.isEmpty(attributeValue35)) {
                                                    Slog.wtf(TAG, "<" + name + "> without actor name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else if (TextUtils.isEmpty(safeIntern2)) {
                                                    Slog.wtf(TAG, "<" + name + "> without package name in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    if ("android".equalsIgnoreCase(safeIntern)) {
                                                        throw new IllegalStateException("Defining " + attributeValue35 + " as " + safeIntern2 + " for the android namespace is not allowed");
                                                    }
                                                    if (this.mNamedActors == null) {
                                                        this.mNamedActors = new ArrayMap();
                                                    }
                                                    Map<String, String> map = this.mNamedActors.get(safeIntern);
                                                    if (map == null) {
                                                        map = new ArrayMap<>();
                                                        this.mNamedActors.put(safeIntern, map);
                                                    } else if (map.containsKey(attributeValue35)) {
                                                        throw new IllegalStateException("Duplicate actor definition for " + safeIntern + "/" + attributeValue35 + "; defined as both " + map.get(attributeValue35) + " and " + safeIntern2);
                                                    }
                                                    map.put(attributeValue35, safeIntern2);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 30:
                                                if (z5) {
                                                    String attributeValue36 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue36 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        if (!TextUtils.isEmpty(this.mOverlayConfigSignaturePackage)) {
                                                            throw new IllegalStateException("Reference signature package defined as both " + this.mOverlayConfigSignaturePackage + " and " + attributeValue36);
                                                        }
                                                        this.mOverlayConfigSignaturePackage = attributeValue36.intern();
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case 31:
                                                String attributeValue37 = xmlPullParser.getAttributeValue(null, "package");
                                                if (attributeValue37 == null) {
                                                    Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    this.mRollbackWhitelistedPackages.add(attributeValue37);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case ' ':
                                                String attributeValue38 = xmlPullParser.getAttributeValue(null, "package");
                                                if (attributeValue38 == null) {
                                                    Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    this.mAutomaticRollbackDenylistedPackages.add(attributeValue38);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '!':
                                                if (z9) {
                                                    String attributeValue39 = xmlPullParser.getAttributeValue(null, "package");
                                                    boolean readBooleanAttribute = XmlUtils.readBooleanAttribute(xmlPullParser, "isModulesInstaller", false);
                                                    if (attributeValue39 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mWhitelistedStagedInstallers.add(attributeValue39);
                                                    }
                                                    if (readBooleanAttribute) {
                                                        if (this.mModulesInstallerPackageName != null) {
                                                            throw new IllegalStateException("Multiple modules installers");
                                                        }
                                                        this.mModulesInstallerPackageName = attributeValue39;
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '\"':
                                                if (z16) {
                                                    String attributeValue40 = xmlPullParser.getAttributeValue(null, "package");
                                                    String attributeValue41 = xmlPullParser.getAttributeValue(null, "installerPackage");
                                                    if (attributeValue40 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    }
                                                    if (attributeValue41 == null) {
                                                        Slog.w(TAG, "<" + name + "> without installerPackage in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    }
                                                    if (attributeValue40 != null && attributeValue41 != null) {
                                                        this.mAllowedVendorApexes.put(attributeValue40, attributeValue41);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '#':
                                                if (z9) {
                                                    String attributeValue42 = xmlPullParser.getAttributeValue(null, "package");
                                                    if (attributeValue42 == null) {
                                                        Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                    } else {
                                                        this.mInstallConstraintsAllowlist.add(attributeValue42);
                                                    }
                                                } else {
                                                    logNotAllowedInPartition(name, file, xmlPullParser);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '$':
                                                String attributeValue43 = xmlPullParser.getAttributeValue(null, "package");
                                                String attributeValue44 = xmlPullParser.getAttributeValue(null, "installer");
                                                if (TextUtils.isEmpty(attributeValue43)) {
                                                    Slog.w(TAG, "<" + name + "> without valid package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else if (TextUtils.isEmpty(attributeValue44)) {
                                                    Slog.w(TAG, "<" + name + "> without valid installer in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    this.mUpdateOwnersForSystemApps.put(attributeValue43, attributeValue44);
                                                }
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '%':
                                                String attributeValue45 = xmlPullParser.getAttributeValue(null, "package");
                                                String attributeValue46 = xmlPullParser.getAttributeValue(null, "stopped");
                                                if (TextUtils.isEmpty(attributeValue45)) {
                                                    Slog.w(TAG, "<" + name + "> without package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else if (TextUtils.isEmpty(attributeValue46)) {
                                                    Slog.w(TAG, "<" + name + "> without stopped in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else if (!Boolean.parseBoolean(attributeValue46)) {
                                                    this.mInitialNonStoppedSystemPackages.add(attributeValue45);
                                                }
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            case '&':
                                                String attributeValue47 = xmlPullParser.getAttributeValue(null, "package");
                                                String attributeValue48 = xmlPullParser.getAttributeValue(null, "path");
                                                if (TextUtils.isEmpty(attributeValue47)) {
                                                    Slog.w(TAG, "<" + name + "> without valid package in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else if (TextUtils.isEmpty(attributeValue48)) {
                                                    Slog.w(TAG, "<" + name + "> without valid path in " + file + " at " + xmlPullParser.getPositionDescription());
                                                } else {
                                                    this.mAppMetadataFilePaths.put(attributeValue47, attributeValue48);
                                                }
                                                z = z16;
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                            default:
                                                z = z16;
                                                Slog.w(TAG, "Tag " + name + " is unknown in " + file + " at " + xmlPullParser.getPositionDescription());
                                                XmlUtils.skipCurrentTag(xmlPullParser);
                                                z16 = z;
                                                fileReader3 = fileReader4;
                                                str4 = str5;
                                                isLowRamDeviceStatic = z17;
                                                z15 = z18;
                                                z14 = z19;
                                                z13 = z20;
                                                i3 = 1;
                                                break;
                                        }
                                    } else {
                                        XmlUtils.skipCurrentTag(xmlPullParser);
                                    }
                                } else {
                                    IoUtils.closeQuietly(fileReader3);
                                }
                            }
                        } catch (IOException e4) {
                            e = e4;
                            iOException = e;
                            Slog.w(TAG, str, iOException);
                            IoUtils.closeQuietly(fileReader2);
                            if (StorageManager.isFileEncrypted()) {
                                i2 = 0;
                                addFeature("android.software.file_based_encryption", 0);
                                addFeature("android.software.securely_removes_users", 0);
                            } else {
                                i2 = 0;
                            }
                            if (StorageManager.hasAdoptable()) {
                                addFeature("android.software.adoptable_storage", i2);
                            }
                            if (ActivityManager.isLowRamDeviceStatic()) {
                                addFeature("android.hardware.ram.low", i2);
                            } else {
                                addFeature("android.hardware.ram.normal", i2);
                            }
                            version = IncrementalManager.getVersion();
                            if (version > 0) {
                                addFeature("android.software.incremental_delivery", version);
                            }
                            addFeature("android.software.app_enumeration", i2);
                            if (Build.VERSION.DEVICE_INITIAL_SDK_INT >= 29) {
                                addFeature("android.software.ipsec_tunnels", i2);
                            }
                            enableIpSecTunnelMigrationOnVsrUAndAbove();
                            if (isErofsSupported()) {
                                if (isKernelVersionAtLeast(5, 10)) {
                                    addFeature("android.software.erofs", i2);
                                } else if (isKernelVersionAtLeast(4, 19)) {
                                    addFeature("android.software.erofs_legacy", i2);
                                }
                            }
                            it = this.mUnavailableFeatures.iterator();
                            while (it.hasNext()) {
                                String next2 = it.next();
                                if (!this.mSystemConfigExt.interceptRemoveFeatureInRead(next2)) {
                                    removeFeature(next2);
                                }
                            }
                        } catch (XmlPullParserException e5) {
                            xmlPullParserException = e5;
                            str2 = str3;
                        }
                    } catch (IOException e6) {
                        e = e6;
                        str = str4;
                        fileReader2 = fileReader3;
                    } catch (Throwable th3) {
                        th = th3;
                        fileReader = fileReader3;
                        th = th;
                        IoUtils.closeQuietly(fileReader);
                        throw th;
                    }
                } catch (XmlPullParserException e7) {
                    str2 = "Got exception parsing permissions.";
                    fileReader2 = fileReader3;
                    xmlPullParserException = e7;
                }
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (FileNotFoundException unused2) {
            Slog.w(TAG, "Couldn't find or open permissions file " + file);
        }
    }

    private void enableIpSecTunnelMigrationOnVsrUAndAbove() {
        if (SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT) > 33) {
            addFeature("android.software.ipsec_tunnel_migration", 0);
        }
    }

    private void addFeature(String str, int i) {
        FeatureInfo featureInfo = this.mAvailableFeatures.get(str);
        if (featureInfo == null) {
            FeatureInfo featureInfo2 = new FeatureInfo();
            featureInfo2.name = str;
            featureInfo2.version = i;
            this.mAvailableFeatures.put(str, featureInfo2);
            return;
        }
        featureInfo.version = Math.max(featureInfo.version, i);
    }

    private void removeFeature(String str) {
        if (this.mAvailableFeatures.remove(str) != null) {
            Slog.d(TAG, "Removed unavailable feature " + str);
        }
    }

    void readPermission(XmlPullParser xmlPullParser, String str) throws IOException, XmlPullParserException {
        if (this.mPermissions.containsKey(str)) {
            throw new IllegalStateException("Duplicate permission definition for " + str);
        }
        PermissionEntry permissionEntry = new PermissionEntry(str, XmlUtils.readBooleanAttribute(xmlPullParser, "perUser", false));
        this.mPermissions.put(str, permissionEntry);
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && xmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                if ("group".equals(xmlPullParser.getName())) {
                    String attributeValue = xmlPullParser.getAttributeValue(null, "gid");
                    if (attributeValue != null) {
                        int gidForName = Process.getGidForName(attributeValue);
                        if (this.mSystemConfigSocExt.shouldAppendPermGid(gidForName)) {
                            permissionEntry.gids = ArrayUtils.appendInt(permissionEntry.gids, gidForName);
                        }
                    } else {
                        Slog.w(TAG, "<group> without gid at " + xmlPullParser.getPositionDescription());
                    }
                }
                XmlUtils.skipCurrentTag(xmlPullParser);
            }
        }
    }

    private void readPrivAppPermissions(XmlPullParser xmlPullParser, ArrayMap<String, ArrayMap<String, Boolean>> arrayMap) throws IOException, XmlPullParserException {
        readPermissionAllowlist(xmlPullParser, arrayMap, "privapp-permissions");
    }

    private void readInstallInUserType(XmlPullParser xmlPullParser, Map<String, Set<String>> map, Map<String, Set<String>> map2) throws IOException, XmlPullParserException {
        String attributeValue = xmlPullParser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(attributeValue)) {
            Slog.w(TAG, "package is required for <install-in-user-type> in " + xmlPullParser.getPositionDescription());
            return;
        }
        Set<String> set = map.get(attributeValue);
        Set<String> set2 = map2.get(attributeValue);
        int depth = xmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            String name = xmlPullParser.getName();
            if ("install-in".equals(name)) {
                String attributeValue2 = xmlPullParser.getAttributeValue(null, "user-type");
                if (TextUtils.isEmpty(attributeValue2)) {
                    Slog.w(TAG, "user-type is required for <install-in-user-type> in " + xmlPullParser.getPositionDescription());
                } else {
                    if (set == null) {
                        set = new ArraySet<>();
                        map.put(attributeValue, set);
                    }
                    set.add(attributeValue2);
                }
            } else if ("do-not-install-in".equals(name)) {
                String attributeValue3 = xmlPullParser.getAttributeValue(null, "user-type");
                if (TextUtils.isEmpty(attributeValue3)) {
                    Slog.w(TAG, "user-type is required for <install-in-user-type> in " + xmlPullParser.getPositionDescription());
                } else {
                    if (set2 == null) {
                        set2 = new ArraySet<>();
                        map2.put(attributeValue, set2);
                    }
                    set2.add(attributeValue3);
                }
            } else {
                Slog.w(TAG, "unrecognized tag in <install-in-user-type> in " + xmlPullParser.getPositionDescription());
            }
        }
    }

    void readOemPermissions(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        readPermissionAllowlist(xmlPullParser, this.mPermissionAllowlist.getOemAppAllowlist(), "oem-permissions");
    }

    private static void readPermissionAllowlist(XmlPullParser xmlPullParser, ArrayMap<String, ArrayMap<String, Boolean>> arrayMap, String str) throws IOException, XmlPullParserException {
        String attributeValue = xmlPullParser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(attributeValue)) {
            Slog.w(TAG, "package is required for <" + str + "> in " + xmlPullParser.getPositionDescription());
            return;
        }
        ArrayMap<String, Boolean> arrayMap2 = arrayMap.get(attributeValue);
        if (arrayMap2 == null) {
            arrayMap2 = new ArrayMap<>();
        }
        int depth = xmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            String name = xmlPullParser.getName();
            if ("permission".equals(name)) {
                String attributeValue2 = xmlPullParser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(attributeValue2)) {
                    Slog.w(TAG, "name is required for <permission> in " + xmlPullParser.getPositionDescription());
                } else {
                    arrayMap2.put(attributeValue2, Boolean.TRUE);
                }
            } else if ("deny-permission".equals(name)) {
                String attributeValue3 = xmlPullParser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(attributeValue3)) {
                    Slog.w(TAG, "name is required for <deny-permission> in " + xmlPullParser.getPositionDescription());
                } else {
                    arrayMap2.put(attributeValue3, Boolean.FALSE);
                }
            }
        }
        arrayMap.put(attributeValue, arrayMap2);
    }

    private void readSplitPermission(XmlPullParser xmlPullParser, File file) throws IOException, XmlPullParserException {
        int parseInt;
        String attributeValue = xmlPullParser.getAttributeValue(null, "name");
        if (attributeValue == null) {
            Slog.w(TAG, "<split-permission> without name in " + file + " at " + xmlPullParser.getPositionDescription());
            XmlUtils.skipCurrentTag(xmlPullParser);
            return;
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, "targetSdk");
        if (TextUtils.isEmpty(attributeValue2)) {
            parseInt = FrameworkStatsLog.WIFI_BYTES_TRANSFER_BY_FG_BG;
        } else {
            try {
                parseInt = Integer.parseInt(attributeValue2);
            } catch (NumberFormatException unused) {
                Slog.w(TAG, "<split-permission> targetSdk not an integer in " + file + " at " + xmlPullParser.getPositionDescription());
                XmlUtils.skipCurrentTag(xmlPullParser);
                return;
            }
        }
        int depth = xmlPullParser.getDepth();
        ArrayList arrayList = new ArrayList();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            if ("new-permission".equals(xmlPullParser.getName())) {
                String attributeValue3 = xmlPullParser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(attributeValue3)) {
                    Slog.w(TAG, "name is required for <new-permission> in " + xmlPullParser.getPositionDescription());
                } else {
                    arrayList.add(attributeValue3);
                }
            } else {
                XmlUtils.skipCurrentTag(xmlPullParser);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        this.mSplitPermissions.add(new PermissionManager.SplitPermissionInfo(attributeValue, arrayList, parseInt));
    }

    private void readComponentOverrides(XmlPullParser xmlPullParser, File file) throws IOException, XmlPullParserException {
        String attributeValue = xmlPullParser.getAttributeValue(null, "package");
        if (attributeValue == null) {
            Slog.w(TAG, "<component-override> without package in " + file + " at " + xmlPullParser.getPositionDescription());
            return;
        }
        String intern = attributeValue.intern();
        int depth = xmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            if ("component".equals(xmlPullParser.getName())) {
                String attributeValue2 = xmlPullParser.getAttributeValue(null, "class");
                String attributeValue3 = xmlPullParser.getAttributeValue(null, "enabled");
                if (attributeValue2 == null) {
                    Slog.w(TAG, "<component> without class in " + file + " at " + xmlPullParser.getPositionDescription());
                    return;
                }
                if (attributeValue3 == null) {
                    Slog.w(TAG, "<component> without enabled in " + file + " at " + xmlPullParser.getPositionDescription());
                    return;
                }
                if (attributeValue2.startsWith(".")) {
                    attributeValue2 = intern + attributeValue2;
                }
                String intern2 = attributeValue2.intern();
                ArrayMap<String, Boolean> arrayMap = this.mPackageComponentEnabledState.get(intern);
                if (arrayMap == null) {
                    arrayMap = new ArrayMap<>();
                    this.mPackageComponentEnabledState.put(intern, arrayMap);
                }
                arrayMap.put(intern2, Boolean.valueOf(!"false".equals(attributeValue3)));
            }
        }
    }

    private void readPublicNativeLibrariesList() {
        readPublicLibrariesListFile(new File("/vendor/etc/public.libraries.txt"));
        String[] strArr = {"/system/etc", "/system_ext/etc", "/product/etc"};
        for (int i = 0; i < 3; i++) {
            String str = strArr[i];
            File[] listFiles = new File(str).listFiles();
            if (listFiles == null) {
                Slog.w(TAG, "Public libraries file folder missing: " + str);
            } else {
                for (File file : listFiles) {
                    String name = file.getName();
                    if (name.startsWith("public.libraries-") && name.endsWith(".txt")) {
                        readPublicLibrariesListFile(file);
                    }
                }
            }
        }
    }

    private void readPublicLibrariesListFile(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        if (!readLine.isEmpty() && !readLine.startsWith("#")) {
                            String str = readLine.trim().split(" ")[0];
                            SharedLibraryEntry sharedLibraryEntry = new SharedLibraryEntry(str, str, new String[0], true);
                            this.mSharedLibraries.put(sharedLibraryEntry.name, sharedLibraryEntry);
                        }
                    } else {
                        bufferedReader.close();
                        return;
                    }
                } finally {
                }
            }
        } catch (IOException e) {
            Slog.w(TAG, "Failed to read public libraries file " + file, e);
        }
    }

    private String getApexModuleNameFromFilePath(Path path, Path path2) {
        if (!path.startsWith(path2)) {
            throw new IllegalArgumentException("File " + path + " is not part of an APEX.");
        }
        if (path.getNameCount() <= path2.getNameCount() + 1) {
            throw new IllegalArgumentException("File " + path + " is in the APEX partition, but not inside a module.");
        }
        return path.getName(path2.getNameCount()).toString();
    }

    @VisibleForTesting
    public void readApexPrivAppPermissions(XmlPullParser xmlPullParser, File file, Path path) throws IOException, XmlPullParserException {
        String apexModuleNameFromFilePath = getApexModuleNameFromFilePath(file.toPath(), path);
        ArrayMap apexPrivilegedAppAllowlists = this.mPermissionAllowlist.getApexPrivilegedAppAllowlists();
        ArrayMap<String, ArrayMap<String, Boolean>> arrayMap = (ArrayMap) apexPrivilegedAppAllowlists.get(apexModuleNameFromFilePath);
        if (arrayMap == null) {
            arrayMap = new ArrayMap<>();
            apexPrivilegedAppAllowlists.put(apexModuleNameFromFilePath, arrayMap);
        }
        readPrivAppPermissions(xmlPullParser, arrayMap);
    }

    private static boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }

    private static boolean isErofsSupported() {
        try {
            return Files.exists(Paths.get("/sys/fs/erofs", new String[0]), new LinkOption[0]);
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean isKernelVersionAtLeast(int i, int i2) {
        String[] split = VintfRuntimeInfo.getKernelVersion().split("\\.");
        if (split.length < 2) {
            return false;
        }
        try {
            int parseInt = Integer.parseInt(split[0]);
            return parseInt > i || (parseInt == i && Integer.parseInt(split[1]) >= i2);
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public static ISystemConfigStaticWrapper getStaticWrapper() {
        return STATIC_WRAPPER;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class SystemConfigStaticWrapper implements ISystemConfigStaticWrapper {
        public int getAllowAllFlag() {
            return -1;
        }

        public int getAllowFeaturesFlag() {
            return 1;
        }

        public int getAllowPermissionsFlag() {
            return 4;
        }

        public int getAllowPrivAppPermissionsFlag() {
            return 16;
        }

        private SystemConfigStaticWrapper() {
        }
    }
}
