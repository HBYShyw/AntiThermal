package com.android.server.location.contexthub;

import android.content.Context;
import android.os.ShellCommand;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ContextHubShellCommand extends ShellCommand {
    private final Context mContext;
    private final ContextHubService mInternal;

    public ContextHubShellCommand(Context context, ContextHubService contextHubService) {
        this.mInternal = contextHubService;
        this.mContext = context;
    }

    public int onCommand(String str) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_CONTEXT_HUB", "ContextHubShellCommand");
        if ("deny".equals(str)) {
            return runDisableAuth();
        }
        return handleDefaultCommands(str);
    }

    private int runDisableAuth() {
        this.mInternal.denyClientAuthState(Integer.decode(getNextArgRequired()).intValue(), getNextArgRequired(), Long.decode(getNextArgRequired()).longValue());
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("ContextHub commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("      Print this help text.");
        outPrintWriter.println("  deny [contextHubId] [packageName] [nanoAppId]");
        outPrintWriter.println("    Immediately transitions the package's authentication state to denied so");
        outPrintWriter.println("    can no longer communciate with the nanoapp.");
    }
}
