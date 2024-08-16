package com.android.server.biometrics;

import android.provider.DeviceConfig;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BiometricStrengthController {
    private static final String KEY_BIOMETRIC_STRENGTHS = "biometric_strengths";
    private static final String TAG = "BiometricStrengthController";
    private DeviceConfig.OnPropertiesChangedListener mDeviceConfigListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.biometrics.BiometricStrengthController$$ExternalSyntheticLambda0
        public final void onPropertiesChanged(DeviceConfig.Properties properties) {
            BiometricStrengthController.this.lambda$new$0(properties);
        }
    };
    private final BiometricService mService;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains(KEY_BIOMETRIC_STRENGTHS)) {
            updateStrengths();
        }
    }

    public BiometricStrengthController(BiometricService biometricService) {
        this.mService = biometricService;
    }

    public void startListening() {
        DeviceConfig.addOnPropertiesChangedListener("biometrics", BackgroundThread.getExecutor(), this.mDeviceConfigListener);
    }

    public void updateStrengths() {
        String string = DeviceConfig.getString("biometrics", KEY_BIOMETRIC_STRENGTHS, "null");
        if ("null".equals(string) || string.isEmpty()) {
            revertStrengths();
        } else {
            updateStrengths(string);
        }
    }

    private void updateStrengths(String str) {
        Map<Integer, Integer> idToStrengthMap = getIdToStrengthMap(str);
        if (idToStrengthMap == null) {
            return;
        }
        Iterator<BiometricSensor> it = this.mService.mSensors.iterator();
        while (it.hasNext()) {
            BiometricSensor next = it.next();
            int i = next.id;
            if (idToStrengthMap.containsKey(Integer.valueOf(i))) {
                int intValue = idToStrengthMap.get(Integer.valueOf(i)).intValue();
                Slog.d(TAG, "updateStrengths: update sensorId=" + i + " to newStrength=" + intValue);
                next.updateStrength(intValue);
            }
        }
    }

    private void revertStrengths() {
        Iterator<BiometricSensor> it = this.mService.mSensors.iterator();
        while (it.hasNext()) {
            BiometricSensor next = it.next();
            Slog.d(TAG, "updateStrengths: revert sensorId=" + next.id + " to oemStrength=" + next.oemStrength);
            next.updateStrength(next.oemStrength);
        }
    }

    private static Map<Integer, Integer> getIdToStrengthMap(String str) {
        if (str == null || str.isEmpty()) {
            Slog.d(TAG, "Flags are null or empty");
            return null;
        }
        HashMap hashMap = new HashMap();
        try {
            for (String str2 : str.split(",")) {
                String[] split = str2.split(":");
                hashMap.put(Integer.valueOf(Integer.parseInt(split[0])), Integer.valueOf(Integer.parseInt(split[1])));
            }
            return hashMap;
        } catch (Exception unused) {
            Slog.e(TAG, "Can't parse flag: " + str);
            return null;
        }
    }
}
