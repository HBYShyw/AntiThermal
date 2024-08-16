package com.oplus.wrapper.hardware.input;

import android.view.InputEvent;

/* loaded from: classes.dex */
public class InputManager {
    private final android.hardware.input.InputManager mInputManager;
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = getInjectInputEventModeWaitForFinish();
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = getInjectInputEventModeWaitForResult();
    public static final int INJECT_INPUT_EVENT_MODE_ASYNC = getInjectInputEventModeAsync();

    public InputManager(android.hardware.input.InputManager inputManager) {
        this.mInputManager = inputManager;
    }

    private static int getInjectInputEventModeWaitForFinish() {
        return 2;
    }

    private static int getInjectInputEventModeWaitForResult() {
        return 1;
    }

    private static int getInjectInputEventModeAsync() {
        return 0;
    }

    public boolean injectInputEvent(InputEvent event, int mode) {
        return this.mInputManager.injectInputEvent(event, mode);
    }
}
