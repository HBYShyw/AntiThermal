package com.android.server.rotationresolver;

import android.content.ComponentName;
import android.os.ShellCommand;
import android.rotationresolver.RotationResolverInternal;
import android.service.rotationresolver.RotationResolutionRequest;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class RotationResolverShellCommand extends ShellCommand {
    private static final int INITIAL_RESULT_CODE = -1;
    static final TestableRotationCallbackInternal sTestableRotationCallbackInternal = new TestableRotationCallbackInternal();
    private final RotationResolverManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RotationResolverShellCommand(RotationResolverManagerService rotationResolverManagerService) {
        this.mService = rotationResolverManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TestableRotationCallbackInternal implements RotationResolverInternal.RotationResolverCallbackInternal {
        private int mLastCallbackResultCode = -1;

        TestableRotationCallbackInternal() {
        }

        public void onSuccess(int i) {
            this.mLastCallbackResultCode = i;
        }

        public void onFailure(int i) {
            this.mLastCallbackResultCode = i;
        }

        public void reset() {
            this.mLastCallbackResultCode = -1;
        }

        public int getLastCallbackCode() {
            return this.mLastCallbackResultCode;
        }
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -2084150080:
                if (str.equals("get-bound-package")) {
                    c = 0;
                    break;
                }
                break;
            case 384662079:
                if (str.equals("resolve-rotation")) {
                    c = 1;
                    break;
                }
                break;
            case 1104883342:
                if (str.equals("set-temporary-service")) {
                    c = 2;
                    break;
                }
                break;
            case 1820466124:
                if (str.equals("get-last-resolution")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return getBoundPackageName();
            case 1:
                return runResolveRotation();
            case 2:
                return setTemporaryService();
            case 3:
                return getLastResolution();
            default:
                return handleDefaultCommands(str);
        }
    }

    private int getBoundPackageName() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        ComponentName componentNameShellCommand = this.mService.getComponentNameShellCommand(Integer.parseInt(getNextArgRequired()));
        outPrintWriter.println(componentNameShellCommand == null ? "" : componentNameShellCommand.getPackageName());
        return 0;
    }

    private int setTemporaryService() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        int parseInt = Integer.parseInt(getNextArgRequired());
        String nextArg = getNextArg();
        if (nextArg == null) {
            this.mService.resetTemporaryService(parseInt);
            outPrintWriter.println("RotationResolverService temporary reset. ");
            return 0;
        }
        int parseInt2 = Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(parseInt, nextArg, parseInt2);
        outPrintWriter.println("RotationResolverService temporarily set to " + nextArg + " for " + parseInt2 + "ms");
        return 0;
    }

    private int runResolveRotation() {
        this.mService.resolveRotationShellCommand(Integer.parseInt(getNextArgRequired()), sTestableRotationCallbackInternal, new RotationResolutionRequest("", 0, 0, true, 2000L));
        return 0;
    }

    private int getLastResolution() {
        getOutPrintWriter().println(sTestableRotationCallbackInternal.getLastCallbackCode());
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Rotation Resolver commands: ");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println();
        outPrintWriter.println("  resolve-rotation USER_ID: request a rotation resolution.");
        outPrintWriter.println("  get-last-resolution: show the last rotation resolution result.");
        outPrintWriter.println("  get-bound-package USER_ID:");
        outPrintWriter.println("    Print the bound package that implements the service.");
        outPrintWriter.println("  set-temporary-service USER_ID [COMPONENT_NAME DURATION]");
        outPrintWriter.println("    Temporarily (for DURATION ms) changes the service implementation.");
        outPrintWriter.println("    To reset, call with just the USER_ID argument.");
    }
}
