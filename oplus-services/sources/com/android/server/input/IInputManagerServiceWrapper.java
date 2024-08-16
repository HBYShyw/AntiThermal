package com.android.server.input;

import com.android.server.input.InputManagerService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInputManagerServiceWrapper {
    default IInputManagerServiceExt getExtImpl() {
        return null;
    }

    default NativeInputManagerService getNative() {
        return null;
    }

    default InputManagerService.WindowManagerCallbacks getWindowManagerCallbacks() {
        return null;
    }

    default Object getInputFilterLock() {
        return new Object();
    }
}
