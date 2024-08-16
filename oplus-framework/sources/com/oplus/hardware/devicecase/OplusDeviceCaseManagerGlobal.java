package com.oplus.hardware.devicecase;

import android.graphics.Rect;
import android.view.View;
import java.util.List;

/* loaded from: classes.dex */
public class OplusDeviceCaseManagerGlobal {
    private static final String TAG = "OplusDeviceCaseManagerGlobal";
    private static OplusDeviceCaseManagerGlobal sInstance;
    private DeviceCaseCoverPanel mDeviceCaseCoverPanel = null;

    private OplusDeviceCaseManagerGlobal() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OplusDeviceCaseManagerGlobal getInstance() {
        OplusDeviceCaseManagerGlobal oplusDeviceCaseManagerGlobal;
        synchronized (OplusDeviceCaseManagerGlobal.class) {
            if (sInstance == null) {
                sInstance = new OplusDeviceCaseManagerGlobal();
            }
            oplusDeviceCaseManagerGlobal = sInstance;
        }
        return oplusDeviceCaseManagerGlobal;
    }

    public void showContentView(View view, int type, int flag, List<Rect> viewPorts) {
        if (this.mDeviceCaseCoverPanel == null) {
            this.mDeviceCaseCoverPanel = new DeviceCaseCoverPanel(view.getContext(), viewPorts);
        }
        this.mDeviceCaseCoverPanel.setWindowType(type);
        this.mDeviceCaseCoverPanel.setContentFlag(flag);
        this.mDeviceCaseCoverPanel.setupCustomView(view);
        this.mDeviceCaseCoverPanel.show();
    }

    public void hideContentView(View view) {
        DeviceCaseCoverPanel deviceCaseCoverPanel = this.mDeviceCaseCoverPanel;
        if (deviceCaseCoverPanel != null && view != null) {
            deviceCaseCoverPanel.remove(view);
            this.mDeviceCaseCoverPanel = null;
        }
    }
}
