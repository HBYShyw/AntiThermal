package android.content.pm;

import android.app.IOplusCommonInjector;
import android.common.OplusFeatureCache;
import android.content.pm.IPackageParserExt;
import android.content.pm.PackageParser;
import android.content.pm.pkg.FrameworkPackageUserState;
import android.content.res.OplusThemeResources;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import com.android.internal.telephony.OplusTelephonyIntents;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class PackageParserExtImpl implements IPackageParserExt {
    private static final HashMap<String, String> LEGACY_MAP;
    private static final String LEGACY_PREFIX = "oppo";
    private static final String OPLUS_PREFIX = "oplus";
    private static final String PACKAGE_OPLUS = "oplus";
    private PackageParser mPackageParser;

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        LEGACY_MAP = hashMap;
        if (!TextUtils.isEmpty(LEGACY_PREFIX)) {
            hashMap.put("oplus.permission.OPLUS_COMPONENT_SAFE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX).replace(OplusThemeResources.OPLUS_PACKAGE.toUpperCase(), LEGACY_PREFIX.toUpperCase()), "oplus.permission.OPLUS_COMPONENT_SAFE");
            hashMap.put("com.oplus.permission.safe.PRIVATE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PRIVATE");
            hashMap.put("com.oplus.permission.safe.RUS".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.RUS");
            hashMap.put("com.oplus.permission.safe.SAU".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SAU");
            hashMap.put("com.oplus.permission.safe.FACE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.FACE");
            hashMap.put("com.oplus.permission.safe.FINGERPRINT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.FINGERPRINT");
            hashMap.put("com.oplus.permission.safe.BACKUP".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.BACKUP");
            hashMap.put(OplusTelephonyIntents.OPLUS_SAFE_SECURITY_PERMISSION.replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), OplusTelephonyIntents.OPLUS_SAFE_SECURITY_PERMISSION);
            hashMap.put("com.oplus.permission.safe.PERSISTENT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PERSISTENT");
            hashMap.put("com.oplus.permission.safe.FINANCIAL".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.FINANCIAL");
            hashMap.put("com.oplus.permission.safe.AUTHENTICATE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.AUTHENTICATE");
            hashMap.put("com.oplus.permission.safe.KEY_EVENT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.KEY_EVENT");
            hashMap.put("com.oplus.permission.safe.PASSWORD".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PASSWORD");
            hashMap.put("com.oplus.permission.safe.ACCOUNTS".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.ACCOUNTS");
            hashMap.put("com.oplus.permission.safe.SAFE_MANAGER".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SAFE_MANAGER");
            hashMap.put("com.oplus.permission.safe.APP_MANAGER".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.APP_MANAGER");
            hashMap.put("com.oplus.permission.safe.DCS".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.DCS");
            hashMap.put("com.oplus.permission.safe.UPDATE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.UPDATE");
            hashMap.put("com.oplus.permission.safe.PUSH".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PUSH");
            hashMap.put(OplusTelephonyIntents.OPLUS_DEEPTHINKER_SAFE_PERMISSION.replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), OplusTelephonyIntents.OPLUS_DEEPTHINKER_SAFE_PERMISSION);
            hashMap.put("com.oplus.permission.safe.MMS".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.MMS");
            hashMap.put("com.oplus.permission.safe.PHONE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PHONE");
            hashMap.put("com.oplus.permission.safe.KEYGUARD".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.KEYGUARD");
            hashMap.put("com.oplus.permission.safe.LOCATION".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.LOCATION");
            hashMap.put("com.oplus.permission.safe.NFC".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.NFC");
            hashMap.put("com.oplus.permission.safe.CONNECTIVITY".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.CONNECTIVITY");
            hashMap.put("com.oplus.permission.safe.BLUETOOTH".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.BLUETOOTH");
            hashMap.put("com.oplus.permission.safe.CAMERA".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.CAMERA");
            hashMap.put("com.oplus.permission.safe.PICTURE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PICTURE");
            hashMap.put("com.oplus.permission.safe.CLIPBOARD".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.CLIPBOARD");
            hashMap.put("com.oplus.permission.safe.LOG".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.LOG");
            hashMap.put("com.oplus.permission.safe.CLOUD".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.CLOUD");
            hashMap.put("com.oplus.permission.safe.SENSOR".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SENSOR");
            hashMap.put("com.oplus.permission.safe.IOT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.IOT");
            hashMap.put("com.oplus.permission.safe.PROTECT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.PROTECT");
            hashMap.put("com.oplus.permission.safe.SDCARD".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SDCARD");
            hashMap.put("com.oplus.permission.safe.USB".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.USB");
            hashMap.put("com.oplus.permission.safe.EMAIL".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.EMAIL");
            hashMap.put("com.oplus.permission.safe.SETTINGS".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SETTINGS");
            hashMap.put("com.oplus.permission.safe.MEDIA".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.MEDIA");
            hashMap.put("com.oplus.permission.safe.GAME".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.GAME");
            hashMap.put("com.oplus.permission.safe.ASSISTANT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.ASSISTANT");
            hashMap.put("com.oplus.permission.safe.SMART_HOME".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SMART_HOME");
            hashMap.put("com.oplus.permission.safe.WEAR".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.WEAR");
            hashMap.put("com.oplus.permission.safe.VOICE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.VOICE");
            hashMap.put("com.oplus.permission.safe.ENTERTAINMENT".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.ENTERTAINMENT");
            hashMap.put("com.oplus.permission.safe.ACCESSIBILITY".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.ACCESSIBILITY");
            hashMap.put("com.oplus.permission.safe.SHARE".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SHARE");
            hashMap.put("com.oplus.permission.safe.READ_COMMON".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.READ_COMMON");
            hashMap.put("com.oplus.permission.safe.SETTINGS_SEARCH".replace(OplusThemeResources.OPLUS_PACKAGE, LEGACY_PREFIX), "com.oplus.permission.safe.SETTINGS_SEARCH");
        }
    }

    public PackageParserExtImpl(Object base) {
        this.mPackageParser = (PackageParser) base;
    }

    public static void adjustPermissionInParseBaseApkTags(PackageParser.Package pkg) {
        if (pkg == null || pkg.requestedPermissions == null || pkg.requestedPermissions.size() == 0) {
            return;
        }
        Set<Map.Entry<String, String>> permissionEntrys = LEGACY_MAP.entrySet();
        for (Map.Entry<String, String> entry : permissionEntrys) {
            String permissionNameOld = entry.getKey();
            String permissionNameNew = entry.getValue();
            if (pkg.requestedPermissions.contains(permissionNameOld) && !pkg.requestedPermissions.contains(permissionNameNew)) {
                pkg.requestedPermissions.add(permissionNameNew);
            }
            if (pkg.requestedPermissions.contains(permissionNameNew) && !pkg.requestedPermissions.contains(permissionNameOld)) {
                pkg.requestedPermissions.add(permissionNameOld);
            }
        }
    }

    public void hookSetMaxAspectRatio(PackageParser.Package owner, float maxAspectRatio) {
        owner.applicationInfo.maxAspectRatio = maxAspectRatio;
    }

    public void hookParseActivityAlias(PackageParser.Activity a, Resources res, XmlResourceParser parser, PackageParser.Activity target) {
        ((IOplusCommonInjector) OplusFeatureCache.getOrCreate(IOplusCommonInjector.DEFAULT, new Object[0])).hookActivityAliasTheme(a, res, parser, target);
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements IPackageParserExt.IStaticExt {
        private StaticExtImpl() {
        }

        public static StaticExtImpl getInstance(Object obj) {
            return LazyHolder.INSTANCE;
        }

        /* loaded from: classes.dex */
        private static class LazyHolder {
            private static final StaticExtImpl INSTANCE = new StaticExtImpl();

            private LazyHolder() {
            }
        }

        public boolean skipValidateName(String pkgName) {
            return OplusThemeResources.OPLUS_PACKAGE.equals(pkgName);
        }

        public void adjustPermissionInParseBaseApkCommon(PackageParser.Package pkg) {
            if (pkg == null || pkg.requestedPermissions == null || pkg.requestedPermissions.size() == 0) {
                return;
            }
            Set<Map.Entry<String, String>> permissionEntrys = PackageParserExtImpl.LEGACY_MAP.entrySet();
            for (Map.Entry<String, String> entry : permissionEntrys) {
                String permissionNameOld = entry.getKey();
                String permissionNameNew = entry.getValue();
                if (pkg.requestedPermissions.contains(permissionNameOld) && !pkg.requestedPermissions.contains(permissionNameNew)) {
                    pkg.requestedPermissions.add(permissionNameNew);
                }
                if (pkg.requestedPermissions.contains(permissionNameNew) && !pkg.requestedPermissions.contains(permissionNameOld)) {
                    pkg.requestedPermissions.add(permissionNameOld);
                }
            }
        }

        public void hookUpdateApplicationInfo(IApplicationInfoExt applicationInfoExt, FrameworkPackageUserState state) {
            if (applicationInfoExt != null && state != null) {
                applicationInfoExt.setOplusFreezeState(state.getOplusFreezeState());
            }
        }
    }
}
