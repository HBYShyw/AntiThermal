package com.android.server.devicepolicy;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.UserHandle;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class DevicePolicyManagerServiceShellCommand extends ShellCommand {
    private static final String CMD_CLEAR_FREEZE_PERIOD_RECORD = "clear-freeze-period-record";
    private static final String CMD_FORCE_NETWORK_LOGS = "force-network-logs";
    private static final String CMD_FORCE_SECURITY_LOGS = "force-security-logs";
    private static final String CMD_IS_SAFE_OPERATION = "is-operation-safe";
    private static final String CMD_IS_SAFE_OPERATION_BY_REASON = "is-operation-safe-by-reason";
    private static final String CMD_LIST_OWNERS = "list-owners";
    private static final String CMD_LIST_POLICY_EXEMPT_APPS = "list-policy-exempt-apps";
    private static final String CMD_MARK_PO_ON_ORG_OWNED_DEVICE = "mark-profile-owner-on-organization-owned-device";
    private static final String CMD_REMOVE_ACTIVE_ADMIN = "remove-active-admin";
    private static final String CMD_SET_ACTIVE_ADMIN = "set-active-admin";
    private static final String CMD_SET_DEVICE_OWNER = "set-device-owner";
    private static final String CMD_SET_PROFILE_OWNER = "set-profile-owner";
    private static final String CMD_SET_SAFE_OPERATION = "set-operation-safe";
    private static final String DO_ONLY_OPTION = "--device-owner-only";
    private static final String USER_OPTION = "--user";
    private ComponentName mComponent;
    private final DevicePolicyManagerService mService;
    private boolean mSetDoOnly;
    private int mUserId = 0;

    private static String safeToString(boolean z) {
        return z ? "SAFE" : "UNSAFE";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DevicePolicyManagerServiceShellCommand(DevicePolicyManagerService devicePolicyManagerService) {
        Objects.requireNonNull(devicePolicyManagerService);
        this.mService = devicePolicyManagerService;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            outPrintWriter.printf("DevicePolicyManager Service (device_policy) commands:\n\n", new Object[0]);
            showHelp(outPrintWriter);
            outPrintWriter.close();
        } catch (Throwable th) {
            if (outPrintWriter != null) {
                try {
                    outPrintWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public int onCommand(String str) {
        char c;
        if (str == null) {
            return handleDefaultCommands(str);
        }
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            switch (str.hashCode()) {
                case -2077120112:
                    if (str.equals(CMD_FORCE_NETWORK_LOGS)) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -1819296492:
                    if (str.equals(CMD_LIST_POLICY_EXEMPT_APPS)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -1791908857:
                    if (str.equals(CMD_SET_DEVICE_OWNER)) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1073491921:
                    if (str.equals(CMD_LIST_OWNERS)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -905136898:
                    if (str.equals(CMD_SET_SAFE_OPERATION)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -898547037:
                    if (str.equals(CMD_IS_SAFE_OPERATION_BY_REASON)) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -776610703:
                    if (str.equals(CMD_REMOVE_ACTIVE_ADMIN)) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -577127626:
                    if (str.equals(CMD_IS_SAFE_OPERATION)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -536624985:
                    if (str.equals(CMD_CLEAR_FREEZE_PERIOD_RECORD)) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 547934547:
                    if (str.equals(CMD_SET_ACTIVE_ADMIN)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 639813476:
                    if (str.equals(CMD_SET_PROFILE_OWNER)) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 1325530298:
                    if (str.equals(CMD_FORCE_SECURITY_LOGS)) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case 1509758184:
                    if (str.equals(CMD_MARK_PO_ON_ORG_OWNED_DEVICE)) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    int runIsSafeOperation = runIsSafeOperation(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runIsSafeOperation;
                case 1:
                    int runIsSafeOperationByReason = runIsSafeOperationByReason(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runIsSafeOperationByReason;
                case 2:
                    int runSetSafeOperation = runSetSafeOperation(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runSetSafeOperation;
                case 3:
                    int runListOwners = runListOwners(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runListOwners;
                case 4:
                    int runListPolicyExemptApps = runListPolicyExemptApps(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runListPolicyExemptApps;
                case 5:
                    int runSetActiveAdmin = runSetActiveAdmin(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runSetActiveAdmin;
                case 6:
                    int runSetDeviceOwner = runSetDeviceOwner(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runSetDeviceOwner;
                case 7:
                    int runSetProfileOwner = runSetProfileOwner(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runSetProfileOwner;
                case '\b':
                    int runRemoveActiveAdmin = runRemoveActiveAdmin(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runRemoveActiveAdmin;
                case '\t':
                    int runClearFreezePeriodRecord = runClearFreezePeriodRecord(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runClearFreezePeriodRecord;
                case '\n':
                    int runForceNetworkLogs = runForceNetworkLogs(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runForceNetworkLogs;
                case 11:
                    int runForceSecurityLogs = runForceSecurityLogs(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runForceSecurityLogs;
                case '\f':
                    int runMarkProfileOwnerOnOrganizationOwnedDevice = runMarkProfileOwnerOnOrganizationOwnedDevice(outPrintWriter);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return runMarkProfileOwnerOnOrganizationOwnedDevice;
                default:
                    int onInvalidCommand = onInvalidCommand(outPrintWriter, str);
                    if (outPrintWriter != null) {
                        outPrintWriter.close();
                    }
                    return onInvalidCommand;
            }
        } catch (Throwable th) {
            if (outPrintWriter != null) {
                try {
                    outPrintWriter.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private int onInvalidCommand(PrintWriter printWriter, String str) {
        if (super.handleDefaultCommands(str) == 0) {
            return 0;
        }
        printWriter.printf("Usage: \n", new Object[0]);
        showHelp(printWriter);
        return -1;
    }

    private void showHelp(PrintWriter printWriter) {
        printWriter.printf("  help\n", new Object[0]);
        printWriter.printf("    Prints this help text.\n\n", new Object[0]);
        printWriter.printf("  %s <OPERATION_ID>\n", CMD_IS_SAFE_OPERATION);
        printWriter.printf("    Checks if the give operation is safe \n\n", new Object[0]);
        printWriter.printf("  %s <REASON_ID>\n", CMD_IS_SAFE_OPERATION_BY_REASON);
        printWriter.printf("    Checks if the operations are safe for the given reason\n\n", new Object[0]);
        printWriter.printf("  %s <OPERATION_ID> <REASON_ID>\n", CMD_SET_SAFE_OPERATION);
        printWriter.printf("    Emulates the result of the next call to check if the given operation is safe \n\n", new Object[0]);
        printWriter.printf("  %s\n", CMD_LIST_OWNERS);
        printWriter.printf("    Lists the device / profile owners per user \n\n", new Object[0]);
        printWriter.printf("  %s\n", CMD_LIST_POLICY_EXEMPT_APPS);
        printWriter.printf("    Lists the apps that are exempt from policies\n\n", new Object[0]);
        printWriter.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_SET_ACTIVE_ADMIN, USER_OPTION);
        printWriter.printf("    Sets the given component as active admin for an existing user.\n\n", new Object[0]);
        printWriter.printf("  %s [ %s <USER_ID> | current *EXPERIMENTAL* ] [ %s ]<COMPONENT>\n", CMD_SET_DEVICE_OWNER, USER_OPTION, DO_ONLY_OPTION);
        printWriter.printf("    Sets the given component as active admin, and its package as device owner.\n\n", new Object[0]);
        printWriter.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_SET_PROFILE_OWNER, USER_OPTION);
        printWriter.printf("    Sets the given component as active admin and profile owner for an existing user.\n\n", new Object[0]);
        printWriter.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_REMOVE_ACTIVE_ADMIN, USER_OPTION);
        printWriter.printf("    Disables an active admin, the admin must have declared android:testOnly in the application in its manifest. This will also remove device and profile owners.\n\n", new Object[0]);
        printWriter.printf("  %s\n", CMD_CLEAR_FREEZE_PERIOD_RECORD);
        printWriter.printf("    Clears framework-maintained record of past freeze periods that the device went through. For use during feature development to prevent triggering restriction on setting freeze periods.\n\n", new Object[0]);
        printWriter.printf("  %s\n", CMD_FORCE_NETWORK_LOGS);
        printWriter.printf("    Makes all network logs available to the DPC and triggers DeviceAdminReceiver.onNetworkLogsAvailable() if needed.\n\n", new Object[0]);
        printWriter.printf("  %s\n", CMD_FORCE_SECURITY_LOGS);
        printWriter.printf("    Makes all security logs available to the DPC and triggers DeviceAdminReceiver.onSecurityLogsAvailable() if needed.\n\n", new Object[0]);
        printWriter.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_MARK_PO_ON_ORG_OWNED_DEVICE, USER_OPTION);
        printWriter.printf("    Marks the profile owner of the given user as managing an organization-owneddevice. That will give it access to device identifiers (such as serial number, IMEI and MEID), as well as other privileges.\n\n", new Object[0]);
    }

    private int runIsSafeOperation(PrintWriter printWriter) {
        int parseInt = Integer.parseInt(getNextArgRequired());
        int unsafeOperationReason = this.mService.getUnsafeOperationReason(parseInt);
        printWriter.printf("Operation %s is %s. Reason: %s\n", DevicePolicyManager.operationToString(parseInt), safeToString(unsafeOperationReason == -1), DevicePolicyManager.operationSafetyReasonToString(unsafeOperationReason));
        return 0;
    }

    private int runIsSafeOperationByReason(PrintWriter printWriter) {
        int parseInt = Integer.parseInt(getNextArgRequired());
        printWriter.printf("Operations affected by %s are %s\n", DevicePolicyManager.operationSafetyReasonToString(parseInt), safeToString(this.mService.isSafeOperation(parseInt)));
        return 0;
    }

    private int runSetSafeOperation(PrintWriter printWriter) {
        int parseInt = Integer.parseInt(getNextArgRequired());
        int parseInt2 = Integer.parseInt(getNextArgRequired());
        this.mService.setNextOperationSafety(parseInt, parseInt2);
        printWriter.printf("Next call to check operation %s will return %s\n", DevicePolicyManager.operationToString(parseInt), DevicePolicyManager.operationSafetyReasonToString(parseInt2));
        return 0;
    }

    private int printAndGetSize(PrintWriter printWriter, Collection<?> collection, String str) {
        if (collection.isEmpty()) {
            printWriter.printf("no %ss\n", str);
            return 0;
        }
        int size = collection.size();
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(size);
        objArr[1] = str;
        objArr[2] = size == 1 ? "" : "s";
        printWriter.printf("%d %s%s:\n", objArr);
        return size;
    }

    private int runListOwners(PrintWriter printWriter) {
        List<OwnerShellData> listAllOwners = this.mService.listAllOwners();
        int printAndGetSize = printAndGetSize(printWriter, listAllOwners, "owner");
        if (printAndGetSize == 0) {
            return 0;
        }
        for (int i = 0; i < printAndGetSize; i++) {
            OwnerShellData ownerShellData = listAllOwners.get(i);
            printWriter.printf("User %2d: admin=%s", Integer.valueOf(ownerShellData.userId), ownerShellData.admin.flattenToShortString());
            if (ownerShellData.isDeviceOwner) {
                printWriter.print(",DeviceOwner");
            }
            if (ownerShellData.isProfileOwner) {
                printWriter.print(",ProfileOwner");
            }
            if (ownerShellData.isManagedProfileOwner) {
                printWriter.printf(",ManagedProfileOwner(parentUserId=%d)", Integer.valueOf(ownerShellData.parentUserId));
            }
            if (ownerShellData.isAffiliated) {
                printWriter.print(",Affiliated");
            }
            printWriter.println();
        }
        return 0;
    }

    private int runListPolicyExemptApps(PrintWriter printWriter) {
        List<String> listPolicyExemptApps = this.mService.listPolicyExemptApps();
        int printAndGetSize = printAndGetSize(printWriter, listPolicyExemptApps, "policy exempt app");
        if (printAndGetSize == 0) {
            return 0;
        }
        for (int i = 0; i < printAndGetSize; i++) {
            printWriter.printf("  %d: %s\n", Integer.valueOf(i), listPolicyExemptApps.get(i));
        }
        return 0;
    }

    private int runSetActiveAdmin(PrintWriter printWriter) {
        parseArgs();
        this.mService.setActiveAdmin(this.mComponent, true, this.mUserId);
        printWriter.printf("Success: Active admin set to component %s\n", this.mComponent.flattenToShortString());
        return 0;
    }

    private int runSetDeviceOwner(PrintWriter printWriter) {
        parseArgs();
        boolean z = true;
        this.mService.setActiveAdmin(this.mComponent, true, this.mUserId);
        try {
            DevicePolicyManagerService devicePolicyManagerService = this.mService;
            ComponentName componentName = this.mComponent;
            int i = this.mUserId;
            if (this.mSetDoOnly) {
                z = false;
            }
            if (!devicePolicyManagerService.setDeviceOwner(componentName, i, z)) {
                throw new RuntimeException("Can't set package " + this.mComponent + " as device owner.");
            }
            this.mService.setUserProvisioningState(3, this.mUserId);
            printWriter.printf("Success: Device owner set to package %s\n", this.mComponent.flattenToShortString());
            printWriter.printf("Active admin set to component %s\n", this.mComponent.flattenToShortString());
            return 0;
        } catch (Exception e) {
            this.mService.removeActiveAdmin(this.mComponent, 0);
            throw e;
        }
    }

    private int runRemoveActiveAdmin(PrintWriter printWriter) {
        parseArgs();
        this.mService.forceRemoveActiveAdmin(this.mComponent, this.mUserId);
        printWriter.printf("Success: Admin removed %s\n", this.mComponent);
        return 0;
    }

    private int runSetProfileOwner(PrintWriter printWriter) {
        parseArgs();
        this.mService.setActiveAdmin(this.mComponent, true, this.mUserId);
        try {
            if (!this.mService.setProfileOwner(this.mComponent, this.mUserId)) {
                throw new RuntimeException("Can't set component " + this.mComponent.flattenToShortString() + " as profile owner for user " + this.mUserId);
            }
            this.mService.setUserProvisioningState(3, this.mUserId);
            printWriter.printf("Success: Active admin and profile owner set to %s for user %d\n", this.mComponent.flattenToShortString(), Integer.valueOf(this.mUserId));
            return 0;
        } catch (Exception e) {
            this.mService.removeActiveAdmin(this.mComponent, this.mUserId);
            throw e;
        }
    }

    private int runClearFreezePeriodRecord(PrintWriter printWriter) {
        this.mService.clearSystemUpdatePolicyFreezePeriodRecord();
        printWriter.printf("Success\n", new Object[0]);
        return 0;
    }

    private int runForceNetworkLogs(PrintWriter printWriter) {
        while (true) {
            long forceNetworkLogs = this.mService.forceNetworkLogs();
            if (forceNetworkLogs != 0) {
                printWriter.printf("We have to wait for %d milliseconds...\n", Long.valueOf(forceNetworkLogs));
                SystemClock.sleep(forceNetworkLogs);
            } else {
                printWriter.printf("Success\n", new Object[0]);
                return 0;
            }
        }
    }

    private int runForceSecurityLogs(PrintWriter printWriter) {
        while (true) {
            long forceSecurityLogs = this.mService.forceSecurityLogs();
            if (forceSecurityLogs != 0) {
                printWriter.printf("We have to wait for %d milliseconds...\n", Long.valueOf(forceSecurityLogs));
                SystemClock.sleep(forceSecurityLogs);
            } else {
                printWriter.printf("Success\n", new Object[0]);
                return 0;
            }
        }
    }

    private int runMarkProfileOwnerOnOrganizationOwnedDevice(PrintWriter printWriter) {
        parseArgs();
        this.mService.setProfileOwnerOnOrganizationOwnedDevice(this.mComponent, this.mUserId, true);
        printWriter.printf("Success\n", new Object[0]);
        return 0;
    }

    private void parseArgs() {
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                if (USER_OPTION.equals(nextOption)) {
                    int parseUserArg = UserHandle.parseUserArg(getNextArgRequired());
                    this.mUserId = parseUserArg;
                    if (parseUserArg == -2) {
                        this.mUserId = ActivityManager.getCurrentUser();
                    }
                } else if (DO_ONLY_OPTION.equals(nextOption)) {
                    this.mSetDoOnly = true;
                } else {
                    throw new IllegalArgumentException("Unknown option: " + nextOption);
                }
            } else {
                this.mComponent = parseComponentName(getNextArgRequired());
                return;
            }
        }
    }

    private ComponentName parseComponentName(String str) {
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        if (unflattenFromString != null) {
            return unflattenFromString;
        }
        throw new IllegalArgumentException("Invalid component " + str);
    }
}
