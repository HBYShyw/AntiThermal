package com.android.server.inputmethod;

import android.os.Handler;
import android.os.IBinder;
import com.android.server.inputmethod.InputMethodManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInputMethodManagerServiceWrapper {
    default boolean chooseNewDefaultIMELocked() {
        return false;
    }

    default InputMethodBindingController getBindingController() {
        return null;
    }

    default InputMethodManagerService.ClientState getCurClient() {
        return null;
    }

    default IBinder getCurTokenLocked() {
        return null;
    }

    default Handler getHandler() {
        return null;
    }

    default InputMethodMenuController getInputMethodMenuController() {
        return null;
    }

    default boolean isInputShown() {
        return false;
    }

    default boolean isShowRequested() {
        return false;
    }

    default void setImeWindowStatus(int i, int i2) {
    }

    default void setInputShown(boolean z) {
    }

    default void setSelectedMethodIdLocked(String str) {
    }

    default void setShowRequested(boolean z) {
    }

    default IInputMethodManagerServiceExt getExtImpl() {
        return new IInputMethodManagerServiceExt() { // from class: com.android.server.inputmethod.IInputMethodManagerServiceWrapper.1
        };
    }
}
