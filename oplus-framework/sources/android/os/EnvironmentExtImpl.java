package android.os;

import android.content.Context;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.os.OplusEnvironment;
import com.oplus.util.OplusResolverIntentUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class EnvironmentExtImpl implements IEnvironmentExt {
    private static final String DEVICE_MANUFACTURE = "manufacture";
    private static final String DEVICE_MANUFACTURE_HYNIX = "HYNIX";
    private static final String DEVICE_MANUFACTURE_MICRON = "MICRON";
    private static final String DEVICE_MANUFACTURE_SANDISK = "SANDISK";
    private static final String DEVICE_VERSION = "version";
    private static final String DEVICE_VERSION_MICRON = "S0J9F8";
    private static final String FEATURE_VIBRATION_ALARM_CLOCK = "oplus.software.vibration_alarm_clock";
    private static final String TAG = "Environment";
    private static volatile EnvironmentExtImpl sInstance;
    private static final String REX_FOR_ELIMINATION_REARRANGEMENT = "^weather_alarm.*.ogg$";
    private static final Pattern ELIMINATION_REARRANGEMENT_PATTERN = Pattern.compile(REX_FOR_ELIMINATION_REARRANGEMENT);

    private EnvironmentExtImpl(Object base) {
    }

    public static EnvironmentExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (EnvironmentExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new EnvironmentExtImpl(base);
                }
            }
        }
        return sInstance;
    }

    public void adjustListInGetInternalMediaDirectories(List<File> res, Context context) {
        addSystemExtFile(res, context);
        addCanonicalFile(res, new File(OplusEnvironment.getMyProductDirectory(), "media"));
        addCanonicalFile(res, new File(OplusEnvironment.getMyCountryDirectory(), "media"));
        addCanonicalFile(res, new File(OplusEnvironment.getMyOperatorDirectory(), "media"));
        addCanonicalFile(res, new File(OplusEnvironment.getMyCompanyDirectory(), "media"));
        addCanonicalFile(res, new File(OplusEnvironment.getMyBigballDirectory(), "media"));
        addCanonicalFile(res, new File(new File("/data/theme"), "ring"));
        addCanonicalFile(res, new File(new File("/data/theme"), OplusResolverIntentUtil.DEFAULT_APP_AUDIO));
        addCanonicalFile(res, new File(new File("/data/theme"), "applying"));
        addCanonicalFile(res, new File("/data/oplus/multimedia", "ringtones"));
        addCanonicalFile(res, new File("/data/oplus/multimedia", "notifications"));
        addCanonicalFile(res, new File("/data/oplus/multimedia", "ui"));
        addCanonicalFile(res, new File("/data/oppo/multimedia", "ringtones"));
        addCanonicalFile(res, new File("/data/oppo/multimedia", "notifications"));
        addCanonicalFile(res, new File("/data/oppo/multimedia", "ui"));
    }

    public boolean interceptMaybeTranslateEmulatedPathToInternal() {
        if (1 == 0) {
            Log.w(TAG, "maybeTranslateEmulatedPathToInternal support translate ext4 path");
        }
        return true;
    }

    public boolean isWhiteListMcp() {
        Map<String, String> results = null;
        if (0 == 0) {
            Log.d(TAG, "isWhiteListMcp,getDeviceInfo result is null,return false");
            return false;
        }
        String deviceVersion = null;
        String deviceManufacture = null;
        for (Map.Entry<String, String> entry : results.entrySet()) {
            String key = entry.getKey();
            if (key.equals(DEVICE_MANUFACTURE)) {
                String deviceManufacture2 = entry.getValue();
                deviceManufacture = deviceManufacture2;
            } else if (key.equals("version")) {
                String deviceVersion2 = entry.getValue();
                deviceVersion = deviceVersion2;
            }
        }
        if (deviceVersion == null || deviceManufacture == null || (!deviceManufacture.equalsIgnoreCase(DEVICE_MANUFACTURE_HYNIX) && !deviceManufacture.equalsIgnoreCase(DEVICE_MANUFACTURE_MICRON) && !deviceManufacture.equalsIgnoreCase(DEVICE_MANUFACTURE_SANDISK))) {
            return false;
        }
        return true;
    }

    private static void addSystemExtFile(List<File> list, Context context) {
        if (isHapticSupport()) {
            addCanonicalFile(list, new File(Environment.getSystemExtDirectory(), "media/audio/dynamic/notifications"));
            boolean isVibrationAlarmClock = isVibrationAlarmClock(context);
            if (isVibrationAlarmClock) {
                File directory = new File(Environment.getSystemExtDirectory(), "media/audio/ui");
                traverse(list, directory);
            } else {
                addCanonicalFile(list, new File(Environment.getSystemExtDirectory(), "media/audio/ui"));
            }
            Log.d(TAG, "addSystemExtFile, isVibrationAlarmClock " + isVibrationAlarmClock);
            addCanonicalFile(list, new File(Environment.getSystemExtDirectory(), "media/audio/alarms"));
            return;
        }
        addCanonicalFile(list, new File(Environment.getSystemExtDirectory(), "media"));
    }

    private static void traverse(List<File> list, File dir) {
        File[] files;
        if (dir.exists() && (files = dir.listFiles()) != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    traverse(list, file);
                } else {
                    String fileName = file.getName();
                    if (!isVibrationAlarmClock(fileName)) {
                        addCanonicalFile(list, file);
                    }
                }
            }
        }
    }

    private static boolean isVibrationAlarmClock(String fileName) {
        Matcher matcher = ELIMINATION_REARRANGEMENT_PATTERN.matcher(fileName);
        return matcher.matches();
    }

    private static boolean isHapticSupport() {
        return OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_RINGTONE_HAPTIC_CHANNEL);
    }

    private static void addCanonicalFile(List<File> list, File file) {
        try {
            list.add(file.getCanonicalFile());
        } catch (IOException e) {
            Log.w(TAG, "Failed to resolve " + file + ": " + e);
            list.add(file);
        }
    }

    private static boolean isVibrationAlarmClock(Context context) {
        return context.getPackageManager().hasSystemFeature(FEATURE_VIBRATION_ALARM_CLOCK);
    }
}
