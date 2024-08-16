package android.location;

import android.common.OplusFeatureCache;
import android.content.Context;
import android.os.Binder;
import android.provider.Settings;
import android.util.Log;
import com.oplus.compatibility.OplusCompatibilityManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.permission.IOplusPermissionCheckInjector;

/* loaded from: classes.dex */
public class LocationManagerExtImpl implements ILocationManagerExt {
    private static final int OPLUS_GPS_STATUS = 32;
    private static final String STEALTH_SECURITY_MODE = "stealth_security_mode";
    private static Boolean sIsSupportStealthMode = null;
    private final String TAG = "LocationManagerExtImpl";
    private LocationManager mLocationManager;

    public LocationManagerExtImpl(Object base) {
        this.mLocationManager = (LocationManager) base;
    }

    public boolean checkPermission(int pid, int uid, String access) {
        return OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.ACCESS_FINE_LOCATION", pid, uid, access);
    }

    public void handleCompatibilityException(Context context) {
        String packageName = getPackageName(context);
        OplusCompatibilityManager compatibilityManager = new OplusCompatibilityManager();
        compatibilityManager.handleCompatibilityException(32, packageName);
    }

    private String getPackageName(Context context) {
        int uid = Binder.getCallingUid();
        String[] packages = context.getPackageManager().getPackagesForUid(uid);
        if (packages == null || 1 != packages.length) {
            Log.d("LocationManagerExtImpl", "packages is invalid!!");
            return "";
        }
        String packageName = packages[0];
        Log.d("LocationManagerExtImpl", "only one name : " + packageName);
        return packageName;
    }

    public boolean isStealthMode(Context context) {
        return isSupportStealthMode() && Settings.Global.getInt(context.getContentResolver(), STEALTH_SECURITY_MODE, 0) == 1;
    }

    public boolean isSupportStealthMode() {
        if (sIsSupportStealthMode == null) {
            sIsSupportStealthMode = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_ALERT_SLIDER) && OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_PERMISSION_STEALTH_SECURITY_MODE));
        }
        return sIsSupportStealthMode.booleanValue();
    }
}
