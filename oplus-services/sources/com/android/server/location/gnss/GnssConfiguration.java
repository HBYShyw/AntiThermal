package com.android.server.location.gnss;

import android.content.Context;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.FrameworkStatsLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GnssConfiguration {
    private static final String CONFIG_A_GLONASS_POS_PROTOCOL_SELECT = "A_GLONASS_POS_PROTOCOL_SELECT";
    private static final String CONFIG_C2K_HOST = "C2K_HOST";
    private static final String CONFIG_C2K_PORT = "C2K_PORT";
    private static final String CONFIG_ENABLE_ACTIVE_SIM_EMERGENCY_SUPL = "ENABLE_ACTIVE_SIM_EMERGENCY_SUPL";
    private static final String CONFIG_ENABLE_NI_SUPL_MESSAGE_INJECTION = "ENABLE_NI_SUPL_MESSAGE_INJECTION";
    private static final String CONFIG_ENABLE_PSDS_PERIODIC_DOWNLOAD = "ENABLE_PSDS_PERIODIC_DOWNLOAD";
    private static final String CONFIG_ES_EXTENSION_SEC = "ES_EXTENSION_SEC";
    private static final String CONFIG_GPS_LOCK = "GPS_LOCK";
    static final String CONFIG_LONGTERM_PSDS_SERVER_1 = "LONGTERM_PSDS_SERVER_1";
    static final String CONFIG_LONGTERM_PSDS_SERVER_2 = "LONGTERM_PSDS_SERVER_2";
    static final String CONFIG_LONGTERM_PSDS_SERVER_3 = "LONGTERM_PSDS_SERVER_3";
    private static final String CONFIG_LPP_PROFILE = "LPP_PROFILE";
    static final String CONFIG_NFW_PROXY_APPS = "NFW_PROXY_APPS";
    static final String CONFIG_NORMAL_PSDS_SERVER = "NORMAL_PSDS_SERVER";
    static final String CONFIG_REALTIME_PSDS_SERVER = "REALTIME_PSDS_SERVER";
    private static final String CONFIG_SUPL_ES = "SUPL_ES";
    private static final String CONFIG_SUPL_HOST = "SUPL_HOST";
    private static final String CONFIG_SUPL_MODE = "SUPL_MODE";
    private static final String CONFIG_SUPL_PORT = "SUPL_PORT";
    private static final String CONFIG_SUPL_VER = "SUPL_VER";
    private static final String CONFIG_USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL = "USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL";
    private static final String DEBUG_PROPERTIES_SYSTEM_FILE = "/etc/gps_debug.conf";
    private static final String DEBUG_PROPERTIES_VENDOR_FILE = "/vendor/etc/gps_debug.conf";
    static final String LPP_PROFILE = "persist.sys.gps.lpp";
    private static final int MAX_EMERGENCY_MODE_EXTENSION_SECONDS = 300;
    private final Context mContext;
    private int mEsExtensionSec = 0;
    private final Properties mProperties = new Properties();
    private static final String TAG = "GnssConfiguration";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SetCarrierProperty {
        boolean set(int i);
    }

    private static native HalInterfaceVersion native_get_gnss_configuration_version();

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_emergency_supl_pdn(int i);

    private static native boolean native_set_es_extension_sec(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_gnss_pos_protocol_select(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_gps_lock(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_lpp_profile(int i);

    private static native boolean native_set_satellite_blocklist(int[] iArr, int[] iArr2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_supl_es(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_supl_mode(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean native_set_supl_version(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class HalInterfaceVersion {
        static final int AIDL_INTERFACE = 3;
        final int mMajor;
        final int mMinor;

        HalInterfaceVersion(int i, int i2) {
            this.mMajor = i;
            this.mMinor = i2;
        }
    }

    public GnssConfiguration(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Properties getProperties() {
        return this.mProperties;
    }

    public int getEsExtensionSec() {
        return this.mEsExtensionSec;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSuplHost() {
        return this.mProperties.getProperty(CONFIG_SUPL_HOST);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSuplPort(int i) {
        return getIntConfig(CONFIG_SUPL_PORT, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getC2KHost() {
        return this.mProperties.getProperty(CONFIG_C2K_HOST);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getC2KPort(int i) {
        return getIntConfig(CONFIG_C2K_PORT, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSuplMode(int i) {
        return getIntConfig(CONFIG_SUPL_MODE, i);
    }

    public int getSuplEs(int i) {
        return getIntConfig(CONFIG_SUPL_ES, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getLppProfile() {
        return this.mProperties.getProperty(CONFIG_LPP_PROFILE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getProxyApps() {
        String property = this.mProperties.getProperty(CONFIG_NFW_PROXY_APPS);
        if (TextUtils.isEmpty(property)) {
            return Collections.emptyList();
        }
        String[] split = property.trim().split("\\s+");
        if (split.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(split);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPsdsPeriodicDownloadEnabled() {
        return getBooleanConfig(CONFIG_ENABLE_PSDS_PERIODIC_DOWNLOAD, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isActiveSimEmergencySuplEnabled() {
        return getBooleanConfig(CONFIG_ENABLE_ACTIVE_SIM_EMERGENCY_SUPL, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNiSuplMessageInjectionEnabled() {
        return getBooleanConfig(CONFIG_ENABLE_NI_SUPL_MESSAGE_INJECTION, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLongTermPsdsServerConfigured() {
        return (this.mProperties.getProperty(CONFIG_LONGTERM_PSDS_SERVER_1) == null && this.mProperties.getProperty(CONFIG_LONGTERM_PSDS_SERVER_2) == null && this.mProperties.getProperty(CONFIG_LONGTERM_PSDS_SERVER_3) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSatelliteBlocklist(int[] iArr, int[] iArr2) {
        native_set_satellite_blocklist(iArr, iArr2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HalInterfaceVersion getHalInterfaceVersion() {
        return native_get_gnss_configuration_version();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reloadGpsProperties() {
        reloadGpsProperties(false, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reloadGpsProperties(boolean z, int i) {
        boolean z2 = DEBUG;
        if (z2) {
            Log.d(TAG, "Reset GPS properties, previous size = " + this.mProperties.size() + ", inEmergency:" + z + ", activeSubId=" + i);
        }
        loadPropertiesFromCarrierConfig(z, i);
        if (isSimAbsent(this.mContext)) {
            String str = SystemProperties.get(LPP_PROFILE);
            if (!TextUtils.isEmpty(str)) {
                this.mProperties.setProperty(CONFIG_LPP_PROFILE, str);
            }
        }
        loadPropertiesFromGpsDebugConfig(this.mProperties, DEBUG_PROPERTIES_VENDOR_FILE);
        loadPropertiesFromGpsDebugConfig(this.mProperties, DEBUG_PROPERTIES_SYSTEM_FILE);
        this.mEsExtensionSec = getRangeCheckedConfigEsExtensionSec();
        logConfigurations();
        HalInterfaceVersion halInterfaceVersion = getHalInterfaceVersion();
        if (halInterfaceVersion == null) {
            if (z2) {
                Log.d(TAG, "Skipped configuration update because GNSS configuration in GPS HAL is not supported");
                return;
            }
            return;
        }
        if (isConfigEsExtensionSecSupported(halInterfaceVersion) && !native_set_es_extension_sec(this.mEsExtensionSec)) {
            Log.e(TAG, "Unable to set ES_EXTENSION_SEC: " + this.mEsExtensionSec);
        }
        HashMap hashMap = new HashMap();
        hashMap.put(CONFIG_SUPL_VER, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda0
            @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
            public final boolean set(int i2) {
                boolean native_set_supl_version;
                native_set_supl_version = GnssConfiguration.native_set_supl_version(i2);
                return native_set_supl_version;
            }
        });
        hashMap.put(CONFIG_SUPL_MODE, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda1
            @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
            public final boolean set(int i2) {
                boolean native_set_supl_mode;
                native_set_supl_mode = GnssConfiguration.native_set_supl_mode(i2);
                return native_set_supl_mode;
            }
        });
        if (isConfigSuplEsSupported(halInterfaceVersion)) {
            hashMap.put(CONFIG_SUPL_ES, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda2
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i2) {
                    boolean native_set_supl_es;
                    native_set_supl_es = GnssConfiguration.native_set_supl_es(i2);
                    return native_set_supl_es;
                }
            });
        }
        hashMap.put(CONFIG_LPP_PROFILE, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda3
            @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
            public final boolean set(int i2) {
                boolean native_set_lpp_profile;
                native_set_lpp_profile = GnssConfiguration.native_set_lpp_profile(i2);
                return native_set_lpp_profile;
            }
        });
        hashMap.put(CONFIG_A_GLONASS_POS_PROTOCOL_SELECT, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda4
            @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
            public final boolean set(int i2) {
                boolean native_set_gnss_pos_protocol_select;
                native_set_gnss_pos_protocol_select = GnssConfiguration.native_set_gnss_pos_protocol_select(i2);
                return native_set_gnss_pos_protocol_select;
            }
        });
        hashMap.put(CONFIG_USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda5
            @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
            public final boolean set(int i2) {
                boolean native_set_emergency_supl_pdn;
                native_set_emergency_supl_pdn = GnssConfiguration.native_set_emergency_supl_pdn(i2);
                return native_set_emergency_supl_pdn;
            }
        });
        if (isConfigGpsLockSupported(halInterfaceVersion)) {
            hashMap.put(CONFIG_GPS_LOCK, new SetCarrierProperty() { // from class: com.android.server.location.gnss.GnssConfiguration$$ExternalSyntheticLambda6
                @Override // com.android.server.location.gnss.GnssConfiguration.SetCarrierProperty
                public final boolean set(int i2) {
                    boolean native_set_gps_lock;
                    native_set_gps_lock = GnssConfiguration.native_set_gps_lock(i2);
                    return native_set_gps_lock;
                }
            });
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            String str2 = (String) entry.getKey();
            String property = this.mProperties.getProperty(str2);
            if (property != null) {
                try {
                    if (!((SetCarrierProperty) entry.getValue()).set(Integer.decode(property).intValue())) {
                        Log.e(TAG, "Unable to set " + str2);
                    }
                } catch (NumberFormatException unused) {
                    Log.e(TAG, "Unable to parse propertyName: " + property);
                }
            }
        }
    }

    private void logConfigurations() {
        FrameworkStatsLog.write(132, getSuplHost(), getSuplPort(0), getC2KHost(), getC2KPort(0), getIntConfig(CONFIG_SUPL_VER, 0), getSuplMode(0), getSuplEs(0) == 1, getIntConfig(CONFIG_LPP_PROFILE, 0), getIntConfig(CONFIG_A_GLONASS_POS_PROTOCOL_SELECT, 0), getIntConfig(CONFIG_USE_EMERGENCY_PDN_FOR_EMERGENCY_SUPL, 0) == 1, getIntConfig(CONFIG_GPS_LOCK, 0), getEsExtensionSec(), this.mProperties.getProperty(CONFIG_NFW_PROXY_APPS));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadPropertiesFromCarrierConfig(boolean z, int i) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            return;
        }
        int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
        if (!z || i < 0) {
            i = defaultDataSubscriptionId;
        }
        PersistableBundle configForSubId = SubscriptionManager.isValidSubscriptionId(i) ? carrierConfigManager.getConfigForSubId(i) : carrierConfigManager.getConfig();
        if (configForSubId == null) {
            if (DEBUG) {
                Log.d(TAG, "SIM not ready, use default carrier config.");
            }
            configForSubId = CarrierConfigManager.getDefaultConfig();
        }
        for (String str : configForSubId.keySet()) {
            if (str != null && str.startsWith("gps.")) {
                String upperCase = str.substring(4).toUpperCase();
                Object obj = configForSubId.get(str);
                if (DEBUG) {
                    Log.d(TAG, "Gps config: " + upperCase + " = " + obj);
                }
                if (obj instanceof String) {
                    this.mProperties.setProperty(upperCase, (String) obj);
                } else if (obj != null) {
                    this.mProperties.setProperty(upperCase, obj.toString());
                }
            }
        }
    }

    private void loadPropertiesFromGpsDebugConfig(Properties properties, String str) {
        FileInputStream fileInputStream;
        try {
            FileInputStream fileInputStream2 = null;
            try {
                fileInputStream = new FileInputStream(new File(str));
            } catch (Throwable th) {
                th = th;
            }
            try {
                properties.load(fileInputStream);
                IoUtils.closeQuietly(fileInputStream);
            } catch (Throwable th2) {
                th = th2;
                fileInputStream2 = fileInputStream;
                IoUtils.closeQuietly(fileInputStream2);
                throw th;
            }
        } catch (IOException unused) {
            if (DEBUG) {
                Log.d(TAG, "Could not open GPS configuration file " + str);
            }
        }
    }

    private int getRangeCheckedConfigEsExtensionSec() {
        int intConfig = getIntConfig(CONFIG_ES_EXTENSION_SEC, 0);
        if (intConfig > MAX_EMERGENCY_MODE_EXTENSION_SECONDS) {
            Log.w(TAG, "ES_EXTENSION_SEC: " + intConfig + " too high, reset to " + MAX_EMERGENCY_MODE_EXTENSION_SECONDS);
            return MAX_EMERGENCY_MODE_EXTENSION_SECONDS;
        }
        if (intConfig >= 0) {
            return intConfig;
        }
        Log.w(TAG, "ES_EXTENSION_SEC: " + intConfig + " is negative, reset to zero.");
        return 0;
    }

    private int getIntConfig(String str, int i) {
        String property = this.mProperties.getProperty(str);
        if (TextUtils.isEmpty(property)) {
            return i;
        }
        try {
            return Integer.decode(property).intValue();
        } catch (NumberFormatException unused) {
            Log.e(TAG, "Unable to parse config parameter " + str + " value: " + property + ". Using default value: " + i);
            return i;
        }
    }

    private boolean getBooleanConfig(String str, boolean z) {
        String property = this.mProperties.getProperty(str);
        return TextUtils.isEmpty(property) ? z : Boolean.parseBoolean(property);
    }

    private static boolean isConfigEsExtensionSecSupported(HalInterfaceVersion halInterfaceVersion) {
        return halInterfaceVersion.mMajor >= 2;
    }

    private static boolean isConfigSuplEsSupported(HalInterfaceVersion halInterfaceVersion) {
        return halInterfaceVersion.mMajor < 2;
    }

    private static boolean isConfigGpsLockSupported(HalInterfaceVersion halInterfaceVersion) {
        return halInterfaceVersion.mMajor < 2;
    }

    private static boolean isSimAbsent(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimState() == 1;
    }
}
