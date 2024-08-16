package com.android.server.inputmethod;

import android.content.Context;
import android.content.DialogInterface;
import android.os.IBinder;
import android.widget.CompoundButton;
import com.android.server.inputmethod.InputMethodSubtypeSwitchingController;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInputMethodMenuControllerExt {
    default void hideInputMethodMenu() {
    }

    default boolean isInputMethodMenuShowing() {
        return false;
    }

    default void setShowHardKeyboardSwitch(boolean z) {
    }

    default void setShowImeWithHardKeyboard(boolean z) {
    }

    default boolean showInputMethodMenu(Context context, int i, int i2, IBinder iBinder, List<InputMethodSubtypeSwitchingController.ImeSubtypeListItem> list, int i3, boolean z, boolean z2, CompoundButton.OnCheckedChangeListener onCheckedChangeListener, DialogInterface.OnClickListener onClickListener, DialogInterface.OnCancelListener onCancelListener) {
        return false;
    }
}
