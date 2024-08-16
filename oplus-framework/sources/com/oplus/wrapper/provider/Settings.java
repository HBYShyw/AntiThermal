package com.oplus.wrapper.provider;

import android.content.ContentResolver;
import android.provider.Settings;

/* loaded from: classes.dex */
public class Settings {

    /* loaded from: classes.dex */
    public static class Global {
        public static final int ZEN_MODE_IMPORTANT_INTERRUPTIONS = getZenModeImportantInterruptions();
        public static final int ZEN_MODE_OFF = getZenModeOff();
        public static final String DEVICE_PROVISIONING_MOBILE_DATA_ENABLED = getDeviceProvisioningMobileDataEnabled();
        public static final String LOW_POWER_MODE = getLowPowerMode();

        private static int getZenModeImportantInterruptions() {
            return 1;
        }

        private static int getZenModeOff() {
            return 0;
        }

        public static boolean putString(ContentResolver resolver, String name, String value, String tag, boolean makeDefault, boolean overrideableByRestore) {
            return Settings.Global.putString(resolver, name, value, tag, makeDefault, overrideableByRestore);
        }

        private static String getDeviceProvisioningMobileDataEnabled() {
            return "device_provisioning_mobile_data";
        }

        private static String getLowPowerMode() {
            return "low_power";
        }
    }

    /* loaded from: classes.dex */
    public static class Secure {
        public static final int LOCATION_CHANGER_SYSTEM_SETTINGS = getLocationChangerSystemSettings();
        public static final String LOCATION_CHANGER = getLocationChanger();
        public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED = getAccessibilityDisplayMagnificationEnabled();
        public static final String MANAGED_PROVISIONING_DPC_DOWNLOADED = getManagedProvisioningDpcDownloaded();
        public static final String USER_SETUP_COMPLETE = getUserSetupComplete();
        public static final String DOZE_ALWAYS_ON = getDozeAlwaysOn();

        public static int getIntForUser(ContentResolver resolver, String name, int userHandle) throws Settings.SettingNotFoundException {
            return Settings.Secure.getIntForUser(resolver, name, userHandle);
        }

        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            return Settings.Secure.getIntForUser(cr, name, def, userHandle);
        }

        public static boolean putStringForUser(ContentResolver cr, String name, String value, int userHandle) {
            return Settings.Secure.putStringForUser(cr, name, value, userHandle);
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            return Settings.Secure.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putIntForUser(ContentResolver cr, String name, int value, int userHandle) {
            return Settings.Secure.putIntForUser(cr, name, value, userHandle);
        }

        private static int getLocationChangerSystemSettings() {
            return 1;
        }

        private static String getLocationChanger() {
            return "location_changer";
        }

        private static String getAccessibilityDisplayMagnificationEnabled() {
            return "accessibility_display_magnification_enabled";
        }

        private static String getManagedProvisioningDpcDownloaded() {
            return "managed_provisioning_dpc_downloaded";
        }

        private static String getUserSetupComplete() {
            return "user_setup_complete";
        }

        private static String getDozeAlwaysOn() {
            return "doze_always_on";
        }
    }

    /* loaded from: classes.dex */
    public static class System {
        public static int getIntForUser(ContentResolver cr, String name, int def, int userHandle) {
            return Settings.System.getIntForUser(cr, name, def, userHandle);
        }

        public static int getIntForUser(ContentResolver cr, String name, int userHandle) throws Settings.SettingNotFoundException {
            return Settings.System.getIntForUser(cr, name, userHandle);
        }

        public static boolean putIntForUser(ContentResolver cr, String name, int value, int userHandle) {
            return Settings.System.putIntForUser(cr, name, value, userHandle);
        }

        public static String getStringForUser(ContentResolver resolver, String name, int userHandle) {
            return Settings.System.getStringForUser(resolver, name, userHandle);
        }

        public static boolean putStringForUser(ContentResolver resolver, String name, String value, int userHandle) {
            return Settings.System.putStringForUser(resolver, name, value, userHandle);
        }
    }
}
