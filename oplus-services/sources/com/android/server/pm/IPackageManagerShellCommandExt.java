package com.android.server.pm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IPackageManagerShellCommandExt {
    default int customLogicOnCommand(PackageManagerShellCommand packageManagerShellCommand, String str) {
        return -1;
    }

    default int customLogicOnRunList(PackageManagerShellCommand packageManagerShellCommand, String str) {
        return -1;
    }

    default boolean customMatchedOnCommand(String str) {
        return false;
    }

    default boolean interceptInDoRunInstall(PackageManagerShellCommand packageManagerShellCommand) {
        return false;
    }

    default boolean interceptInRunCompile(PackageManagerShellCommand packageManagerShellCommand) {
        return false;
    }

    default boolean interceptInRunUninstall(PackageManagerShellCommand packageManagerShellCommand) {
        return false;
    }
}
