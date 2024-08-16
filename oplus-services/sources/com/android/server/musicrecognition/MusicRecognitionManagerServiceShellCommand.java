package com.android.server.musicrecognition;

import android.os.ShellCommand;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class MusicRecognitionManagerServiceShellCommand extends ShellCommand {
    private final MusicRecognitionManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MusicRecognitionManagerServiceShellCommand(MusicRecognitionManagerService musicRecognitionManagerService) {
        this.mService = musicRecognitionManagerService;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        PrintWriter outPrintWriter = getOutPrintWriter();
        if ("set".equals(str)) {
            return requestSet(outPrintWriter);
        }
        return handleDefaultCommands(str);
    }

    private int requestSet(PrintWriter printWriter) {
        String nextArgRequired = getNextArgRequired();
        if ("temporary-service".equals(nextArgRequired)) {
            return setTemporaryService(printWriter);
        }
        printWriter.println("Invalid set: " + nextArgRequired);
        return -1;
    }

    private int setTemporaryService(PrintWriter printWriter) {
        int parseInt = Integer.parseInt(getNextArgRequired());
        String nextArg = getNextArg();
        if (nextArg == null) {
            this.mService.resetTemporaryService(parseInt);
            return 0;
        }
        int parseInt2 = Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(parseInt, nextArg, parseInt2);
        printWriter.println("MusicRecognitionService temporarily set to " + nextArg + " for " + parseInt2 + "ms");
        return 0;
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            outPrintWriter.println("MusicRecognition Service (music_recognition) commands:");
            outPrintWriter.println("  help");
            outPrintWriter.println("    Prints this help text.");
            outPrintWriter.println("");
            outPrintWriter.println("  set temporary-service USER_ID [COMPONENT_NAME DURATION]");
            outPrintWriter.println("    Temporarily (for DURATION ms) changes the service implementation.");
            outPrintWriter.println("    To reset, call with just the USER_ID argument.");
            outPrintWriter.println("");
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
}
