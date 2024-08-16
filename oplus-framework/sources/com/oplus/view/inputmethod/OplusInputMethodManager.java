package com.oplus.view.inputmethod;

import android.content.ComponentName;
import android.content.Context;
import android.os.ResultReceiver;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.OplusInputMethodManagerInternal;

/* loaded from: classes.dex */
public final class OplusInputMethodManager {

    /* loaded from: classes.dex */
    private static final class OplusInputMethodManagerHolder {
        private static final OplusInputMethodManager INSTANCE = new OplusInputMethodManager();

        private OplusInputMethodManagerHolder() {
        }
    }

    private OplusInputMethodManager() {
    }

    public static OplusInputMethodManager getInstance() {
        return OplusInputMethodManagerHolder.INSTANCE;
    }

    public void hideCurrentInputMethod() {
        OplusInputMethodManagerInternal.getInstance().hideCurrentInputMethod();
    }

    public static void hideSoftInput(Context context) {
        OplusInputMethodManagerInternal.getInstance().hideCurrentInputMethod();
    }

    @Deprecated
    public boolean setDefaultInputMethodByCustomize(String packageName) {
        return OplusInputMethodManagerInternal.getInstance().setDefaultInputMethodByCustomize(packageName);
    }

    @Deprecated
    public String getDefaultInputMethodByCustomize() {
        return OplusInputMethodManagerInternal.getInstance().getDefaultInputMethodByCustomize();
    }

    @Deprecated
    public boolean clearDefaultInputMethodByCustomize() {
        return OplusInputMethodManagerInternal.getInstance().clearDefaultInputMethodByCustomize();
    }

    @Deprecated
    public void updateTouchDeviceId(int deviceId) {
    }

    @Deprecated
    public void updateCursorAnchorInfoToSynergy(CursorAnchorInfo cursorAnchorInfo) {
    }

    public void registerInputMethodSynergyService(ComponentName synergyName, boolean register) {
        OplusInputMethodManagerInternal.getInstance().registerInputMethodSynergyService(synergyName, register);
    }

    public void commitTextByOtherSide() {
        OplusInputMethodManagerInternal.getInstance().commitTextByOtherSide();
    }

    public int getInputMethodWindowVisibleHeight(Context context) {
        if (context != null) {
            return OplusInputMethodManagerInternal.getInstance().getInputMethodWindowVisibleHeight(context.getDisplay().getDisplayId());
        }
        return 0;
    }

    public void registerCursorAnchorInfoListener(ResultReceiver receiver) {
        if (receiver != null) {
            OplusInputMethodManagerInternal.getInstance().registerCursorAnchorInfoListener(receiver);
        }
    }

    public void unregisterCursorAnchorInfoListener(ResultReceiver receiver) {
        if (receiver != null) {
            OplusInputMethodManagerInternal.getInstance().unregisterCursorAnchorInfoListener(receiver);
        }
    }
}
