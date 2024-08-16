package com.oplus.darkmode;

import android.view.OplusWindowManager;

/* loaded from: classes.dex */
public class OplusDarkModeHelper {
    public static void registerOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) {
        try {
            OplusWindowManager windowManager = new OplusWindowManager();
            windowManager.registerOnUiModeConfigurationChangeFinishListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterOnUiModeConfigurationChangeFinishListener(IOplusDarkModeListener listener) {
        try {
            OplusWindowManager windowManager = new OplusWindowManager();
            windowManager.unregisterOnUiModeConfigurationChangeFinishListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
