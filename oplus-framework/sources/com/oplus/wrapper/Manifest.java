package com.oplus.wrapper;

/* loaded from: classes.dex */
public final class Manifest {

    /* loaded from: classes.dex */
    public static final class Permission {
        public static final String INTERACT_ACROSS_USERS = getInteractAcrossUsers();
        public static final String DEVICE_POWER = getDevicePower();
        public static final String USER_ACTIVITY = getUserActivity();
        public static final String INTERNAL_SYSTEM_WINDOW = getInternalSystemWindow();
        public static final String INTERACT_ACROSS_USERS_FULL = getInteractAcrossUsersFull();
        public static final String DISPATCH_PROVISIONING_MESSAGE = getDispatchProvisioningMessage();

        private Permission() {
        }

        private static String getInteractAcrossUsers() {
            return "android.permission.INTERACT_ACROSS_USERS";
        }

        private static String getDevicePower() {
            return "android.permission.DEVICE_POWER";
        }

        private static String getUserActivity() {
            return "android.permission.USER_ACTIVITY";
        }

        private static String getInternalSystemWindow() {
            return "android.permission.INTERNAL_SYSTEM_WINDOW";
        }

        private static String getInteractAcrossUsersFull() {
            return "android.permission.INTERACT_ACROSS_USERS_FULL";
        }

        private static String getDispatchProvisioningMessage() {
            return "android.permission.DISPATCH_PROVISIONING_MESSAGE";
        }
    }
}
