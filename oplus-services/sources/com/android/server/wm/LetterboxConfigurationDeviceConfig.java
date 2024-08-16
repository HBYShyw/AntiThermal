package com.android.server.wm;

import android.provider.DeviceConfig;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
final class LetterboxConfigurationDeviceConfig implements DeviceConfig.OnPropertiesChangedListener {
    private static final boolean DEFAULT_VALUE_ALLOW_IGNORE_ORIENTATION_REQUEST = true;
    private static final boolean DEFAULT_VALUE_ENABLE_CAMERA_COMPAT_TREATMENT = true;
    private static final boolean DEFAULT_VALUE_ENABLE_COMPAT_FAKE_FOCUS = true;
    private static final boolean DEFAULT_VALUE_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY = true;
    private static final boolean DEFAULT_VALUE_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY = true;
    static final String KEY_ALLOW_IGNORE_ORIENTATION_REQUEST = "allow_ignore_orientation_request";
    static final String KEY_ENABLE_CAMERA_COMPAT_TREATMENT = "enable_compat_camera_treatment";
    static final String KEY_ENABLE_COMPAT_FAKE_FOCUS = "enable_compat_fake_focus";
    static final String KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY = "enable_display_rotation_immersive_app_compat_policy";
    static final String KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY = "enable_letterbox_translucent_activity";

    @VisibleForTesting
    static final Map<String, Boolean> sKeyToDefaultValueMap;
    private boolean mIsCameraCompatTreatmentEnabled = true;
    private boolean mIsDisplayRotationImmersiveAppCompatPolicyEnabled = true;
    private boolean mIsAllowIgnoreOrientationRequest = true;
    private boolean mIsCompatFakeFocusAllowed = true;
    private boolean mIsTranslucentLetterboxingAllowed = true;
    private final ArraySet<String> mActiveDeviceConfigsSet = new ArraySet<>();

    static {
        Boolean bool = Boolean.TRUE;
        sKeyToDefaultValueMap = Map.of(KEY_ENABLE_CAMERA_COMPAT_TREATMENT, bool, KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY, bool, KEY_ALLOW_IGNORE_ORIENTATION_REQUEST, bool, KEY_ENABLE_COMPAT_FAKE_FOCUS, bool, KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY, bool);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LetterboxConfigurationDeviceConfig(Executor executor) {
        DeviceConfig.addOnPropertiesChangedListener("window_manager", executor, this);
    }

    public void onPropertiesChanged(DeviceConfig.Properties properties) {
        for (int size = this.mActiveDeviceConfigsSet.size() - 1; size >= 0; size--) {
            String valueAt = this.mActiveDeviceConfigsSet.valueAt(size);
            if (properties.getKeyset().contains(valueAt)) {
                readAndSaveValueFromDeviceConfig(valueAt);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateFlagActiveStatus(boolean z, String str) {
        if (z) {
            this.mActiveDeviceConfigsSet.add(str);
            readAndSaveValueFromDeviceConfig(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getFlag(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1925051249:
                if (str.equals(KEY_ENABLE_COMPAT_FAKE_FOCUS)) {
                    c = 0;
                    break;
                }
                break;
            case -1785181243:
                if (str.equals(KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY)) {
                    c = 1;
                    break;
                }
                break;
            case 111337355:
                if (str.equals(KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY)) {
                    c = 2;
                    break;
                }
                break;
            case 735247337:
                if (str.equals(KEY_ALLOW_IGNORE_ORIENTATION_REQUEST)) {
                    c = 3;
                    break;
                }
                break;
            case 1471627327:
                if (str.equals(KEY_ENABLE_CAMERA_COMPAT_TREATMENT)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return this.mIsCompatFakeFocusAllowed;
            case 1:
                return this.mIsDisplayRotationImmersiveAppCompatPolicyEnabled;
            case 2:
                return this.mIsTranslucentLetterboxingAllowed;
            case 3:
                return this.mIsAllowIgnoreOrientationRequest;
            case 4:
                return this.mIsCameraCompatTreatmentEnabled;
            default:
                throw new AssertionError("Unexpected flag name: " + str);
        }
    }

    private void readAndSaveValueFromDeviceConfig(String str) {
        Boolean bool = sKeyToDefaultValueMap.get(str);
        if (bool == null) {
            throw new AssertionError("Haven't found default value for flag: " + str);
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1925051249:
                if (str.equals(KEY_ENABLE_COMPAT_FAKE_FOCUS)) {
                    c = 0;
                    break;
                }
                break;
            case -1785181243:
                if (str.equals(KEY_ENABLE_DISPLAY_ROTATION_IMMERSIVE_APP_COMPAT_POLICY)) {
                    c = 1;
                    break;
                }
                break;
            case 111337355:
                if (str.equals(KEY_ENABLE_LETTERBOX_TRANSLUCENT_ACTIVITY)) {
                    c = 2;
                    break;
                }
                break;
            case 735247337:
                if (str.equals(KEY_ALLOW_IGNORE_ORIENTATION_REQUEST)) {
                    c = 3;
                    break;
                }
                break;
            case 1471627327:
                if (str.equals(KEY_ENABLE_CAMERA_COMPAT_TREATMENT)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.mIsCompatFakeFocusAllowed = getDeviceConfig(str, bool.booleanValue());
                return;
            case 1:
                this.mIsDisplayRotationImmersiveAppCompatPolicyEnabled = getDeviceConfig(str, bool.booleanValue());
                return;
            case 2:
                this.mIsTranslucentLetterboxingAllowed = getDeviceConfig(str, bool.booleanValue());
                return;
            case 3:
                this.mIsAllowIgnoreOrientationRequest = getDeviceConfig(str, bool.booleanValue());
                return;
            case 4:
                this.mIsCameraCompatTreatmentEnabled = getDeviceConfig(str, bool.booleanValue());
                return;
            default:
                throw new AssertionError("Unexpected flag name: " + str);
        }
    }

    private boolean getDeviceConfig(String str, boolean z) {
        return DeviceConfig.getBoolean("window_manager", str, z);
    }
}
