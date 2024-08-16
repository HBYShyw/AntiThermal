package com.android.server.permission.access;

import com.android.modules.utils.BinaryXmlPullParser;
import com.android.modules.utils.BinaryXmlSerializer;
import com.android.server.pm.pkg.PackageState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AccessPolicy.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class SchemePolicy {
    public abstract int getDecision(@NotNull GetStateScope getStateScope, @NotNull AccessUri accessUri, @NotNull AccessUri accessUri2);

    @NotNull
    public abstract String getObjectScheme();

    @NotNull
    public abstract String getSubjectScheme();

    public void onAppIdAdded(@NotNull MutateStateScope mutateStateScope, int i) {
    }

    public void onAppIdRemoved(@NotNull MutateStateScope mutateStateScope, int i) {
    }

    public void onInitialized(@NotNull MutateStateScope mutateStateScope) {
    }

    public void onPackageAdded(@NotNull MutateStateScope mutateStateScope, @NotNull PackageState packageState) {
    }

    public void onPackageInstalled(@NotNull MutateStateScope mutateStateScope, @NotNull PackageState packageState, int i) {
    }

    public void onPackageRemoved(@NotNull MutateStateScope mutateStateScope, @NotNull String str, int i) {
    }

    public void onPackageUninstalled(@NotNull MutateStateScope mutateStateScope, @NotNull String str, int i, int i2) {
    }

    public void onStateMutated(@NotNull GetStateScope getStateScope) {
    }

    public void onStorageVolumeMounted(@NotNull MutateStateScope mutateStateScope, @Nullable String str, boolean z) {
    }

    public void onSystemReady(@NotNull MutateStateScope mutateStateScope) {
    }

    public void onUserAdded(@NotNull MutateStateScope mutateStateScope, int i) {
    }

    public void onUserRemoved(@NotNull MutateStateScope mutateStateScope, int i) {
    }

    public void parseSystemState(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull AccessState accessState) {
    }

    public void parseUserState(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull AccessState accessState, int i) {
    }

    public void serializeSystemState(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull AccessState accessState) {
    }

    public void serializeUserState(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull AccessState accessState, int i) {
    }

    public abstract void setDecision(@NotNull MutateStateScope mutateStateScope, @NotNull AccessUri accessUri, @NotNull AccessUri accessUri2, int i);
}
