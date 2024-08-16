package com.android.server.tare;

import android.os.Binder;
import com.android.modules.utils.BasicShellCommandHandler;
import com.android.server.health.HealthServiceWrapperHidl;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TareShellCommand extends BasicShellCommandHandler {
    static final int COMMAND_ERROR = -1;
    static final int COMMAND_SUCCESS = 0;
    private final InternalResourceService mIrs;

    public TareShellCommand(InternalResourceService internalResourceService) {
        this.mIrs = internalResourceService;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x003f A[Catch: Exception -> 0x0044, TRY_LEAVE, TryCatch #0 {Exception -> 0x0044, blocks: (B:5:0x000b, B:13:0x0035, B:16:0x003a, B:18:0x003f, B:20:0x001b, B:23:0x0026), top: B:4:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int onCommand(String str) {
        boolean z;
        PrintWriter outPrintWriter = getOutPrintWriter();
        String str2 = str != null ? str : "";
        try {
            int hashCode = str2.hashCode();
            if (hashCode != -1272052579) {
                if (hashCode == 1983838258 && str2.equals("set-vip")) {
                    z = true;
                    if (z) {
                        return runClearVip(outPrintWriter);
                    }
                    if (z) {
                        return runSetVip(outPrintWriter);
                    }
                    return handleDefaultCommands(str);
                }
                z = -1;
                if (z) {
                }
            } else {
                if (str2.equals("clear-vip")) {
                    z = false;
                    if (z) {
                    }
                }
                z = -1;
                if (z) {
                }
            }
        } catch (Exception e) {
            outPrintWriter.println("Exception: " + e);
            return -1;
        }
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("TARE commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("    Print this help text.");
        outPrintWriter.println("  clear-vip");
        outPrintWriter.println("    Clears all VIP settings resulting from previous calls using `set-vip` and");
        outPrintWriter.println("    resets them all to default.");
        outPrintWriter.println("  set-vip <USER_ID> <PACKAGE> <true|false|default>");
        outPrintWriter.println("    Designate the app as a Very Important Package or not. A VIP is allowed to");
        outPrintWriter.println("    do as much work as it wants, regardless of TARE state.");
        outPrintWriter.println("    The user ID must be an explicit user ID. USER_ALL, CURRENT, etc. are not");
        outPrintWriter.println("    supported.");
        outPrintWriter.println();
    }

    private void checkPermission(String str) throws Exception {
        if (this.mIrs.getContext().checkCallingOrSelfPermission("android.permission.CHANGE_APP_IDLE_STATE") == 0) {
            return;
        }
        throw new SecurityException("Uid " + Binder.getCallingUid() + " not permitted to " + str);
    }

    private int runClearVip(PrintWriter printWriter) throws Exception {
        checkPermission("clear vip");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mIrs.executeClearVip(printWriter);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private int runSetVip(PrintWriter printWriter) throws Exception {
        checkPermission("modify vip");
        int parseInt = Integer.parseInt(getNextArgRequired());
        String nextArgRequired = getNextArgRequired();
        String nextArgRequired2 = getNextArgRequired();
        Boolean valueOf = HealthServiceWrapperHidl.INSTANCE_VENDOR.equals(nextArgRequired2) ? null : Boolean.valueOf(nextArgRequired2);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mIrs.executeSetVip(printWriter, parseInt, nextArgRequired, valueOf);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
