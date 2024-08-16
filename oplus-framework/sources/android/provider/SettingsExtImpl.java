package android.provider;

import android.content.ContentResolver;
import android.os.OplusPropertyList;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import com.oplus.view.OplusWindowUtils;
import java.util.Set;

/* loaded from: classes.dex */
public class SettingsExtImpl implements ISettingsExt {
    private static final int INITIAL_VALUE = 0;
    private static final int USER_MULTI_APP = 999;
    private static final int USER_SYSTEM = 0;
    public static String TAG = "SettingsExtImpl";
    private static volatile int sMAXBRIGHTNESS = 0;
    private static volatile int sACTUALMAXBRIGHTNESS = 0;
    private static volatile int sACTUALMINBRIGHTNESS = 0;
    private static volatile int sTALKBACKMAX = 0;
    private static volatile int sTALKBACKMIN = 0;
    private static volatile SettingsExtImpl sInstance = null;

    private SettingsExtImpl(Object base) {
    }

    public static SettingsExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (SettingsExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new SettingsExtImpl(base);
                }
            }
        }
        return sInstance;
    }

    public int getAutoBrightnessValueForUserWithDef(String name, String value, int def) {
        try {
            if (sMAXBRIGHTNESS == 0 && "screen_brightness".equals(name)) {
                sMAXBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS, 0);
            }
            if ("screen_brightness".equals(name) && sMAXBRIGHTNESS > 255) {
                int valueChanged = def;
                if (value != null) {
                    int brightnessValue = Integer.parseInt(value) * 256;
                    valueChanged = Math.round(brightnessValue / (sMAXBRIGHTNESS + 1));
                }
                if (valueChanged > 255) {
                    return 255;
                }
                return valueChanged;
            }
            return Integer.MIN_VALUE;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public int getAutoBrightnessValueForUser(ContentResolver cr, String name, String value) {
        int valueChanged = Integer.MIN_VALUE;
        try {
            if (isTalkback(cr, name)) {
                return getBrightnessFortalkback(cr, name, Integer.parseInt(value));
            }
            if (sMAXBRIGHTNESS == 0 && ("screen_brightness".equals(name) || "screen_auto_brightness_adj".equals(name))) {
                sMAXBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS, 0);
            }
            if ((!"screen_brightness".equals(name) && !"screen_auto_brightness_adj".equals(name)) || sMAXBRIGHTNESS <= 255) {
                return Integer.MIN_VALUE;
            }
            if (value != null) {
                valueChanged = Math.round((Float.parseFloat(value) * 255.0f) / sMAXBRIGHTNESS);
            }
            if (valueChanged > 255) {
                return 255;
            }
            return valueChanged;
        } catch (NumberFormatException e) {
            Log.e(TAG, "NumberFormatException: " + e.getMessage());
            return Integer.MIN_VALUE;
        }
    }

    public int putAutoBrightnessValueForUser(ContentResolver cr, String packageName, String name, int value) {
        if (isTalkback(cr, name)) {
            return putBrightnessFortalkback(cr, name, value);
        }
        if (sMAXBRIGHTNESS == 0 && "screen_brightness".equals(name)) {
            sMAXBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS, 0);
        }
        if ("screen_brightness".equals(name) && sMAXBRIGHTNESS > 255) {
            int brightnessValue = Math.round((sMAXBRIGHTNESS * value) / 255.0f);
            return brightnessValue;
        }
        return value;
    }

    public void addKeyToPublicSettings(Set<String> publicSettings) {
        publicSettings.add("screen_auto_brightness_adj_talkback");
    }

    private int getBrightnessFortalkback(ContentResolver cr, String name, int value) {
        if (sTALKBACKMAX == 0 || sTALKBACKMIN == 0) {
            sTALKBACKMAX = SystemProperties.getInt("sys.internal.screen_brightness_talkback_max", 255);
            sTALKBACKMIN = SystemProperties.getInt("sys.internal.screen_brightness_talkback_min", 1);
        }
        if (sACTUALMINBRIGHTNESS == 0 || sACTUALMAXBRIGHTNESS == 0) {
            sACTUALMAXBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS, sTALKBACKMAX);
            sACTUALMINBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS_MIN, sTALKBACKMIN);
        }
        double brightnessStep = (sACTUALMAXBRIGHTNESS - sACTUALMINBRIGHTNESS) / (sTALKBACKMAX - sTALKBACKMIN);
        int val = Math.max(value, sACTUALMINBRIGHTNESS);
        int result = ((int) Math.round((val - sACTUALMINBRIGHTNESS) / brightnessStep)) + sTALKBACKMIN;
        if (result == sTALKBACKMIN) {
            result++;
        }
        Log.d(TAG, "getBrightnessFortalkback: ( " + val + "->" + result + "); brightnessStep = " + brightnessStep + " Talkback Range (" + sTALKBACKMIN + " ~ " + sTALKBACKMAX + "); Actual Range (" + sACTUALMINBRIGHTNESS + " ~ " + sACTUALMAXBRIGHTNESS + ")");
        return result;
    }

    private int putBrightnessFortalkback(ContentResolver cr, String name, int value) {
        if (sTALKBACKMAX == 0 || sTALKBACKMIN == 0) {
            sTALKBACKMAX = SystemProperties.getInt("sys.internal.screen_brightness_talkback_max", 255);
            sTALKBACKMIN = SystemProperties.getInt("sys.internal.screen_brightness_talkback_min", 1);
        }
        if (sACTUALMINBRIGHTNESS == 0 || sACTUALMAXBRIGHTNESS == 0) {
            sACTUALMAXBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS, sTALKBACKMAX);
            sACTUALMINBRIGHTNESS = SystemProperties.getInt(OplusPropertyList.PROPERTY_MULTI_BRIGHTNESS_MIN, sTALKBACKMIN);
        }
        double brightnessStep = (sACTUALMAXBRIGHTNESS - sACTUALMINBRIGHTNESS) / (sTALKBACKMAX - sTALKBACKMIN);
        int result = ((int) Math.round((value - sTALKBACKMIN) * brightnessStep)) + sACTUALMINBRIGHTNESS;
        int mode = Settings.System.getInt(cr, "screen_brightness_mode", 0);
        if (1 == mode) {
            if (result < 0) {
                Log.w(TAG, "putBrightnessFortalkback input wrong value " + result);
            }
            result = Math.max(result, sTALKBACKMIN);
            Settings.System.putInt(cr, "screen_auto_brightness_adj_talkback", result);
        }
        Log.d(TAG, "putBrightnessFortalkback: ( " + value + "->" + result + "); brightnessStep = " + brightnessStep + " Talkback Range (" + sTALKBACKMIN + " ~ " + sTALKBACKMAX + "); Actual Range (" + sACTUALMINBRIGHTNESS + " ~ " + sACTUALMAXBRIGHTNESS + ")");
        return result;
    }

    private boolean isTalkback(ContentResolver cr, String name) {
        if (cr != null && cr.getPackageName().equals(OplusWindowUtils.PACKAGE_TALKBACK) && name != null && "screen_brightness".equals(name)) {
            return true;
        }
        return false;
    }

    public int redirectUserIfNeeded(int userHandle, String name) {
        if (userHandle == 999 && name != null && "default_input_method".equals(name)) {
            return 0;
        }
        return userHandle;
    }
}
