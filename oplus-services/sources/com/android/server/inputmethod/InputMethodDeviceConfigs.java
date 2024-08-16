package com.android.server.inputmethod;

import android.app.ActivityThread;
import android.provider.DeviceConfig;
import com.android.internal.annotations.VisibleForTesting;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class InputMethodDeviceConfigs {
    private final DeviceConfig.OnPropertiesChangedListener mDeviceConfigChangedListener;
    private boolean mHideImeWhenNoEditorFocus;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InputMethodDeviceConfigs() {
        DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.inputmethod.InputMethodDeviceConfigs$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                InputMethodDeviceConfigs.this.lambda$new$0(properties);
            }
        };
        this.mDeviceConfigChangedListener = onPropertiesChangedListener;
        this.mHideImeWhenNoEditorFocus = DeviceConfig.getBoolean("input_method_manager", "hide_ime_when_no_editor_focus", true);
        DeviceConfig.addOnPropertiesChangedListener("input_method_manager", ActivityThread.currentApplication().getMainExecutor(), onPropertiesChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        if ("input_method_manager".equals(properties.getNamespace())) {
            for (String str : properties.getKeyset()) {
                if ("hide_ime_when_no_editor_focus".equals(str)) {
                    this.mHideImeWhenNoEditorFocus = properties.getBoolean(str, true);
                }
            }
        }
    }

    public boolean shouldHideImeWhenNoEditorFocus() {
        return this.mHideImeWhenNoEditorFocus;
    }

    @VisibleForTesting
    void destroy() {
        DeviceConfig.removeOnPropertiesChangedListener(this.mDeviceConfigChangedListener);
    }
}
