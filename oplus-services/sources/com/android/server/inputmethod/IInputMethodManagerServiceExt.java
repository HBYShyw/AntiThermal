package com.android.server.inputmethod;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodInfo;
import com.android.internal.inputmethod.IInputMethod;
import com.android.server.inputmethod.InputMethodSubtypeSwitchingController;
import com.android.server.inputmethod.InputMethodUtils;
import java.io.FileDescriptor;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IInputMethodManagerServiceExt {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ILifecycleExt {
        default InputMethodManagerService initInputMethodManagerService(Context context) {
            return null;
        }
    }

    default void configDebug(FileDescriptor fileDescriptor, String[] strArr) {
    }

    default void configInputMethodAfterQuery() {
    }

    default Looper getBackgroundLooper() {
        return null;
    }

    default String getDebugTAG(String str) {
        return str;
    }

    default InputMethodInfo getDefaultInputMethodByConfig(int i) {
        return null;
    }

    default boolean hideInputMethodForce(int i, int i2) {
        return false;
    }

    default boolean isCarDisplayId(int i) {
        return false;
    }

    default boolean isMultiAppUserId(int i) {
        return false;
    }

    default boolean isMultiAppUserId(int i, InputMethodUtils.InputMethodSettings inputMethodSettings) {
        return false;
    }

    default boolean isSecureServcie() {
        return false;
    }

    default void logDebug(String str) {
    }

    default void logDebugIme(String str) {
    }

    default void logMethodCallers(String str) {
    }

    default void notifyImeAttributeChanged(boolean z, EditorInfo editorInfo, int i) {
    }

    default boolean onApplyImeVisibility(boolean z) {
        return false;
    }

    default boolean onCalledWithValidTokenLocked(IBinder iBinder) {
        return false;
    }

    default void onFinishPackageChanges(int i) {
    }

    default void onImeInitialized(int i) {
    }

    default void onInputMethodPickByUser(InputMethodSubtypeSwitchingController.ImeSubtypeListItem imeSubtypeListItem, InputMethodSubtypeSwitchingController.ImeSubtypeListItem imeSubtypeListItem2) {
    }

    default boolean onInputMethodQueried(String str, String str2) {
        return false;
    }

    default void onServerRegisterContentObserver(ContentObserver contentObserver, int i) {
    }

    default boolean onServerSettingsObserverChanged(ContentObserver contentObserver, int i, boolean z, Uri uri) {
        return false;
    }

    default boolean onSetImeWindowStatus(int i, int i2) {
        return false;
    }

    default String onSetSelectedMethodId(String str) {
        return str;
    }

    default boolean shouldHideImeSwitcher() {
        return false;
    }

    default boolean shouldIgnoreFocusCheck(IBinder iBinder, int i) {
        return false;
    }

    default boolean shouldIgnoreShowBySynergy(String str) {
        return false;
    }

    default boolean shouldIgnoreStartInput(Context context, int i, EditorInfo editorInfo, int i2, int i3, boolean z) {
        return false;
    }

    default boolean shouldInterceptIme(int i) {
        return false;
    }

    default void startInputToSynergy(IInputMethod.StartInputParams startInputParams) {
    }

    default void unfreezeInputMethodPackage(InputMethodInfo inputMethodInfo) {
    }

    default void updateDefaultEnabledImes(List<InputMethodInfo> list) {
    }

    default void updateOsenseAction() {
    }
}
