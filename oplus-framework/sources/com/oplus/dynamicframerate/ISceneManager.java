package com.oplus.dynamicframerate;

import android.view.Display;

/* loaded from: classes.dex */
public interface ISceneManager {
    void handleScheduleTraversals(Object obj);

    void onDoFrameFinished();

    void onStateSlientUpdate(int i);

    void onUpdateInternalDisplay(Display display);

    void onVriUpdate(Object obj);
}
