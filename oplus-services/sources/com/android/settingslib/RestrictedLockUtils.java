package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RestrictedLockUtils {
    @RequiresApi(23)
    public static EnforcedAdmin getProfileOrDeviceOwner(Context context, UserHandle userHandle) {
        return getProfileOrDeviceOwner(context, null, userHandle);
    }

    @RequiresApi(23)
    public static EnforcedAdmin getProfileOrDeviceOwner(Context context, String str, UserHandle userHandle) {
        DevicePolicyManager devicePolicyManager;
        ComponentName deviceOwnerComponentOnAnyUser;
        if (userHandle == null || (devicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy")) == null) {
            return null;
        }
        try {
            ComponentName profileOwner = ((DevicePolicyManager) context.createPackageContextAsUser(context.getPackageName(), 0, userHandle).getSystemService(DevicePolicyManager.class)).getProfileOwner();
            if (profileOwner != null) {
                return new EnforcedAdmin(profileOwner, str, userHandle);
            }
            if (!Objects.equals(devicePolicyManager.getDeviceOwnerUser(), userHandle) || (deviceOwnerComponentOnAnyUser = devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == null) {
                return null;
            }
            return new EnforcedAdmin(deviceOwnerComponentOnAnyUser, str, userHandle);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @RequiresApi(23)
    public static void sendShowAdminSupportDetailsIntent(Context context, EnforcedAdmin enforcedAdmin) {
        Intent showAdminSupportDetailsIntent = getShowAdminSupportDetailsIntent(context, enforcedAdmin);
        int myUserId = UserHandle.myUserId();
        if (enforcedAdmin != null) {
            UserHandle userHandle = enforcedAdmin.user;
            if (userHandle != null && isCurrentUserOrProfile(context, userHandle.getIdentifier())) {
                myUserId = enforcedAdmin.user.getIdentifier();
            }
            showAdminSupportDetailsIntent.putExtra("android.app.extra.RESTRICTION", enforcedAdmin.enforcedRestriction);
        }
        context.startActivityAsUser(showAdminSupportDetailsIntent, UserHandle.of(myUserId));
    }

    public static Intent getShowAdminSupportDetailsIntent(Context context, EnforcedAdmin enforcedAdmin) {
        Intent intent = new Intent("android.settings.SHOW_ADMIN_SUPPORT_DETAILS");
        if (enforcedAdmin != null) {
            ComponentName componentName = enforcedAdmin.component;
            if (componentName != null) {
                intent.putExtra("android.app.extra.DEVICE_ADMIN", componentName);
            }
            intent.putExtra("android.intent.extra.USER", enforcedAdmin.user);
        }
        return intent;
    }

    @RequiresApi(33)
    public static void sendShowRestrictedSettingDialogIntent(Context context, String str, int i) {
        context.startActivity(getShowRestrictedSettingsIntent(str, i));
    }

    private static Intent getShowRestrictedSettingsIntent(String str, int i) {
        Intent intent = new Intent("android.settings.SHOW_RESTRICTED_SETTING_DIALOG");
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.UID", i);
        return intent;
    }

    @RequiresApi(23)
    public static boolean isCurrentUserOrProfile(Context context, int i) {
        return ((UserManager) context.getSystemService(UserManager.class)).getUserProfiles().contains(UserHandle.of(i));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class EnforcedAdmin {
        public static final EnforcedAdmin MULTIPLE_ENFORCED_ADMIN = new EnforcedAdmin();

        @Nullable
        public ComponentName component;

        @Nullable
        public String enforcedRestriction;

        @Nullable
        public UserHandle user;

        public static EnforcedAdmin createDefaultEnforcedAdminWithRestriction(String str) {
            EnforcedAdmin enforcedAdmin = new EnforcedAdmin();
            enforcedAdmin.enforcedRestriction = str;
            return enforcedAdmin;
        }

        public EnforcedAdmin(ComponentName componentName, UserHandle userHandle) {
            this.enforcedRestriction = null;
            this.component = componentName;
            this.user = userHandle;
        }

        public EnforcedAdmin(ComponentName componentName, String str, UserHandle userHandle) {
            this.component = componentName;
            this.enforcedRestriction = str;
            this.user = userHandle;
        }

        public EnforcedAdmin(EnforcedAdmin enforcedAdmin) {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
            if (enforcedAdmin == null) {
                throw new IllegalArgumentException();
            }
            this.component = enforcedAdmin.component;
            this.enforcedRestriction = enforcedAdmin.enforcedRestriction;
            this.user = enforcedAdmin.user;
        }

        public EnforcedAdmin() {
            this.component = null;
            this.enforcedRestriction = null;
            this.user = null;
        }

        public static EnforcedAdmin combine(EnforcedAdmin enforcedAdmin, EnforcedAdmin enforcedAdmin2) {
            if (enforcedAdmin == null) {
                return enforcedAdmin2;
            }
            if (enforcedAdmin2 == null || enforcedAdmin.equals(enforcedAdmin2)) {
                return enforcedAdmin;
            }
            if (!enforcedAdmin.enforcedRestriction.equals(enforcedAdmin2.enforcedRestriction)) {
                throw new IllegalArgumentException("Admins with different restriction cannot be combined");
            }
            return MULTIPLE_ENFORCED_ADMIN;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            EnforcedAdmin enforcedAdmin = (EnforcedAdmin) obj;
            return Objects.equals(this.user, enforcedAdmin.user) && Objects.equals(this.component, enforcedAdmin.component) && Objects.equals(this.enforcedRestriction, enforcedAdmin.enforcedRestriction);
        }

        public int hashCode() {
            return Objects.hash(this.component, this.enforcedRestriction, this.user);
        }

        public String toString() {
            return "EnforcedAdmin{component=" + this.component + ", enforcedRestriction='" + this.enforcedRestriction + ", user=" + this.user + '}';
        }
    }
}
