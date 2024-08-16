package com.android.server.autofill;

import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.os.Bundle;
import android.service.autofill.FillResponse;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface ISessionExt {
    public static final int FILL_REQUEST_BY_NATIVE = 0;
    public static final int FILL_REQUEST_BY_OPLUS_AUTOFILL_REENTER_VIEW = 1;
    public static final int SAVE_REQUEST_BY_NATIVE = 0;
    public static final int SAVE_REQUEST_BY_OPLUS_AUTOFILL_ACTIVITY_FINISH = 1;

    default String getOplusAutofillDatasetFlag() {
        return null;
    }

    default void hookHandleEmptyCurrentView(Bundle bundle) {
    }

    default Bundle hookOnFillRequestClientState(Bundle bundle) {
        return bundle;
    }

    default Bundle hookOnSaveRequestClientState(Bundle bundle) {
        return bundle;
    }

    default void hookSanitizeForParceling(AssistStructure assistStructure) {
    }

    default void hookSetOnFillRequestReason(int i) {
    }

    default void hookSetOnSaveRequestReason(int i) {
    }

    default boolean hookShouldRequestNewFillResponse() {
        return false;
    }

    default boolean isOplusAutofillService() {
        return false;
    }

    default void setRemoteServiceComponentName(ComponentName componentName) {
    }

    default boolean skipSaveUi() {
        return false;
    }

    default boolean skipSaveUiAndNativeProcess() {
        return false;
    }

    default boolean useOplusAutofillService(Bundle bundle, FillResponse fillResponse) {
        return false;
    }
}
