package android.operator;

import java.util.HashSet;

/* loaded from: classes.dex */
public abstract class OplusOperatorManagerInternal {
    public abstract void changePackagesStatusForNewUser(int i);

    public abstract void deleteFirstSwitchNoneClearFile();

    public abstract void deleteFirstSwitchNoneClearFileInEngineerMasterClear();

    public abstract HashSet<String> getGrantedRuntimePermissionsPostInstall(String str);

    public abstract HashSet<String> getGrantedRuntimePermissionsPreload(String str, boolean z);

    public abstract void testInternal();
}
