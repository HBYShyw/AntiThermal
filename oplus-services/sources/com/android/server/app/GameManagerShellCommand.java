package com.android.server.app;

import android.app.ActivityManager;
import android.app.IGameManagerService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ShellCommand;
import com.android.server.app.GameManagerService;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.StringJoiner;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class GameManagerShellCommand extends ShellCommand {
    private static final String BATTERY_MODE_NUM = "3";
    private static final String BATTERY_MODE_STR = "battery";
    private static final String CUSTOM_MODE_NUM = "4";
    private static final String CUSTOM_MODE_STR = "custom";
    private static final String PERFORMANCE_MODE_NUM = "2";
    private static final String PERFORMANCE_MODE_STR = "performance";
    private static final String STANDARD_MODE_NUM = "1";
    private static final String STANDARD_MODE_STR = "standard";
    private static final String UNSUPPORTED_MODE_NUM = String.valueOf(0);
    private static final String UNSUPPORTED_MODE_STR = "unsupported";
    IGameManagerShellCommandExt mIGameManagerShellCommandExt = (IGameManagerShellCommandExt) ExtLoader.type(IGameManagerShellCommandExt.class).base(this).create();

    private static String gameModeIntToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "" : CUSTOM_MODE_STR : BATTERY_MODE_STR : PERFORMANCE_MODE_STR : STANDARD_MODE_STR : UNSUPPORTED_MODE_STR;
    }

    public int onCommand(String str) {
        char c;
        if (str == null) {
            return handleDefaultCommands(str);
        }
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            switch (str.hashCode()) {
                case -1207633086:
                    if (str.equals("list-configs")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -729460415:
                    if (str.equals("list-modes")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 100897:
                    if (str.equals("ext")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 113762:
                    if (str.equals("set")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 3357091:
                    if (str.equals(GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 108404047:
                    if (str.equals("reset")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            if (c == 0) {
                return runSetGameModeConfig(outPrintWriter);
            }
            if (c == 1) {
                return runResetGameModeConfig(outPrintWriter);
            }
            if (c == 2) {
                return runSetGameMode(outPrintWriter);
            }
            if (c == 3) {
                return runListGameModes(outPrintWriter);
            }
            if (c == 4) {
                return runListGameModeConfigs(outPrintWriter);
            }
            if (c == 5) {
                return this.mIGameManagerShellCommandExt.onCommandExt(outPrintWriter);
            }
            return handleDefaultCommands(str);
        } catch (Exception e) {
            outPrintWriter.println("Error: " + e);
            return -1;
        }
    }

    private int runListGameModes(PrintWriter printWriter) throws ServiceManager.ServiceNotFoundException, RemoteException {
        String nextArgRequired = getNextArgRequired();
        int currentUser = ActivityManager.getCurrentUser();
        GameManagerService gameManagerService = (GameManagerService) ServiceManager.getService("game");
        String gameModeIntToString = gameModeIntToString(gameManagerService.getGameMode(nextArgRequired, currentUser));
        StringJoiner stringJoiner = new StringJoiner(",");
        for (int i : gameManagerService.getAvailableGameModes(nextArgRequired, currentUser)) {
            stringJoiner.add(gameModeIntToString(i));
        }
        printWriter.println(nextArgRequired + " current mode: " + gameModeIntToString + ", available game modes: [" + stringJoiner + "]");
        return 0;
    }

    private int runListGameModeConfigs(PrintWriter printWriter) throws ServiceManager.ServiceNotFoundException, RemoteException {
        String nextArgRequired = getNextArgRequired();
        String interventionList = ((GameManagerService) ServiceManager.getService("game")).getInterventionList(nextArgRequired, ActivityManager.getCurrentUser());
        if (interventionList == null) {
            printWriter.println("No interventions found for " + nextArgRequired);
            return 0;
        }
        printWriter.println(nextArgRequired + " interventions: " + interventionList);
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int runSetGameMode(PrintWriter printWriter) throws ServiceManager.ServiceNotFoundException, RemoteException {
        int currentUser;
        char c;
        String nextOption = getNextOption();
        String nextArgRequired = (nextOption == null || !nextOption.equals("--user")) ? null : getNextArgRequired();
        String nextArgRequired2 = getNextArgRequired();
        String nextArgRequired3 = getNextArgRequired();
        IGameManagerService asInterface = IGameManagerService.Stub.asInterface(ServiceManager.getServiceOrThrow("game"));
        if (nextArgRequired != null) {
            currentUser = Integer.parseInt(nextArgRequired);
        } else {
            currentUser = ActivityManager.getCurrentUser();
        }
        boolean z = false;
        boolean z2 = false;
        for (int i : asInterface.getAvailableGameModes(nextArgRequired3, currentUser)) {
            if (i == 2) {
                z2 = true;
            } else if (i == 3) {
                z = true;
            }
        }
        String lowerCase = nextArgRequired2.toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase.hashCode()) {
            case -1480388560:
                if (lowerCase.equals(PERFORMANCE_MODE_STR)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1349088399:
                if (lowerCase.equals(CUSTOM_MODE_STR)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -331239923:
                if (lowerCase.equals(BATTERY_MODE_STR)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 49:
                if (lowerCase.equals(STANDARD_MODE_NUM)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 50:
                if (lowerCase.equals(PERFORMANCE_MODE_NUM)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 51:
                if (lowerCase.equals(BATTERY_MODE_NUM)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 52:
                if (lowerCase.equals(CUSTOM_MODE_NUM)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1312628413:
                if (lowerCase.equals(STANDARD_MODE_STR)) {
                    c = 7;
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
            case 4:
                if (z2) {
                    asInterface.setGameMode(nextArgRequired3, 2, currentUser);
                    printWriter.println("Set game mode to `PERFORMANCE` for user `" + currentUser + "` in game `" + nextArgRequired3 + "`");
                    return 0;
                }
                printWriter.println("Game mode: " + nextArgRequired2 + " not supported by " + nextArgRequired3);
                return -1;
            case 1:
            case 6:
                asInterface.setGameMode(nextArgRequired3, 4, currentUser);
                printWriter.println("Set game mode to `CUSTOM` for user `" + currentUser + "` in game `" + nextArgRequired3 + "`");
                return 0;
            case 2:
            case 5:
                if (z) {
                    asInterface.setGameMode(nextArgRequired3, 3, currentUser);
                    printWriter.println("Set game mode to `BATTERY` for user `" + currentUser + "` in game `" + nextArgRequired3 + "`");
                    return 0;
                }
                printWriter.println("Game mode: " + nextArgRequired2 + " not supported by " + nextArgRequired3);
                return -1;
            case 3:
            case 7:
                asInterface.setGameMode(nextArgRequired3, 1, currentUser);
                printWriter.println("Set game mode to `STANDARD` for user `" + currentUser + "` in game `" + nextArgRequired3 + "`");
                return 0;
            default:
                printWriter.println("Invalid game mode: " + nextArgRequired2);
                return -1;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x003d, code lost:
    
        if (r1.equals("--fps") == false) goto L8;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0011. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0044. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int runSetGameModeConfig(PrintWriter printWriter) throws ServiceManager.ServiceNotFoundException, RemoteException {
        int currentUser;
        String str = null;
        int i = 4;
        String str2 = null;
        String str3 = null;
        while (true) {
            int i2 = i;
            while (true) {
                String nextOption = getNextOption();
                char c = 0;
                if (nextOption != null) {
                    switch (nextOption.hashCode()) {
                        case 43000649:
                            break;
                        case 1333227331:
                            if (nextOption.equals("--mode")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1333469547:
                            if (nextOption.equals("--user")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1807206472:
                            if (nextOption.equals("--downscale")) {
                                c = 3;
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
                            if (str2 == null) {
                                String nextArgRequired = getNextArgRequired();
                                if (nextArgRequired != null && GameManagerService.getFpsInt(nextArgRequired) == -1) {
                                    printWriter.println("Invalid frame rate '" + nextArgRequired + "'");
                                    return -1;
                                }
                                str2 = nextArgRequired;
                            } else {
                                printWriter.println("Duplicate option '" + nextOption + "'");
                                return -1;
                            }
                            break;
                        case 1:
                            break;
                        case 2:
                            if (str == null) {
                                str = getNextArgRequired();
                            } else {
                                printWriter.println("Duplicate option '" + nextOption + "'");
                                return -1;
                            }
                        case 3:
                            if (str3 == null) {
                                String nextArgRequired2 = getNextArgRequired();
                                if ("disable".equals(nextArgRequired2)) {
                                    nextArgRequired2 = "-1";
                                } else {
                                    try {
                                        Float.parseFloat(nextArgRequired2);
                                    } catch (NumberFormatException unused) {
                                        printWriter.println("Invalid scaling ratio '" + nextArgRequired2 + "'");
                                        return -1;
                                    }
                                }
                                str3 = nextArgRequired2;
                            } else {
                                printWriter.println("Duplicate option '" + nextOption + "'");
                                return -1;
                            }
                        default:
                            printWriter.println("Invalid option '" + nextOption + "'");
                            return -1;
                    }
                } else {
                    String nextArgRequired3 = getNextArgRequired();
                    if (str != null) {
                        currentUser = Integer.parseInt(str);
                    } else {
                        currentUser = ActivityManager.getCurrentUser();
                    }
                    GameManagerService gameManagerService = (GameManagerService) ServiceManager.getService("game");
                    if (gameManagerService == null) {
                        printWriter.println("Failed to find GameManagerService on device");
                        return -1;
                    }
                    gameManagerService.setGameModeConfigOverride(nextArgRequired3, currentUser, i2, str2, str3);
                    printWriter.println("Set custom mode intervention config for user `" + currentUser + "` in game `" + nextArgRequired3 + "` as: `downscaling-ratio: " + str3 + ";fps-override: " + str2 + "`");
                    return 0;
                }
            }
            i = Integer.parseInt(getNextArgRequired());
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int runResetGameModeConfig(PrintWriter printWriter) throws ServiceManager.ServiceNotFoundException, RemoteException {
        int currentUser;
        boolean z;
        String str = null;
        String str2 = null;
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                if (nextOption.equals("--mode")) {
                    if (str2 == null) {
                        str2 = getNextArgRequired();
                    } else {
                        printWriter.println("Duplicate option '" + nextOption + "'");
                        return -1;
                    }
                } else {
                    if (!nextOption.equals("--user")) {
                        printWriter.println("Invalid option '" + nextOption + "'");
                        return -1;
                    }
                    if (str == null) {
                        str = getNextArgRequired();
                    } else {
                        printWriter.println("Duplicate option '" + nextOption + "'");
                        return -1;
                    }
                }
            } else {
                String nextArgRequired = getNextArgRequired();
                GameManagerService gameManagerService = (GameManagerService) ServiceManager.getService("game");
                if (str != null) {
                    currentUser = Integer.parseInt(str);
                } else {
                    currentUser = ActivityManager.getCurrentUser();
                }
                if (str2 == null) {
                    gameManagerService.resetGameModeConfigOverride(nextArgRequired, currentUser, -1);
                    return 0;
                }
                String lowerCase = str2.toLowerCase(Locale.getDefault());
                lowerCase.hashCode();
                switch (lowerCase.hashCode()) {
                    case -1480388560:
                        if (lowerCase.equals(PERFORMANCE_MODE_STR)) {
                            z = false;
                            break;
                        }
                        z = -1;
                        break;
                    case -331239923:
                        if (lowerCase.equals(BATTERY_MODE_STR)) {
                            z = true;
                            break;
                        }
                        z = -1;
                        break;
                    case 50:
                        if (lowerCase.equals(PERFORMANCE_MODE_NUM)) {
                            z = 2;
                            break;
                        }
                        z = -1;
                        break;
                    case 51:
                        if (lowerCase.equals(BATTERY_MODE_NUM)) {
                            z = 3;
                            break;
                        }
                        z = -1;
                        break;
                    default:
                        z = -1;
                        break;
                }
                switch (z) {
                    case false:
                    case true:
                        gameManagerService.resetGameModeConfigOverride(nextArgRequired, currentUser, 2);
                        return 0;
                    case true:
                    case true:
                        gameManagerService.resetGameModeConfigOverride(nextArgRequired, currentUser, 3);
                        return 0;
                    default:
                        printWriter.println("Invalid game mode: " + str2);
                        return -1;
                }
            }
        }
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Game manager (game) commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("      Print this help text.");
        outPrintWriter.println("  downscale");
        outPrintWriter.println("      Deprecated. Please use `custom` command.");
        outPrintWriter.println("  list-configs <PACKAGE_NAME>");
        outPrintWriter.println("      Lists the current intervention configs of an app.");
        outPrintWriter.println("  list-modes <PACKAGE_NAME>");
        outPrintWriter.println("      Lists the current selected and available game modes of an app.");
        outPrintWriter.println("  mode [--user <USER_ID>] [1|2|3|4|standard|performance|battery|custom] <PACKAGE_NAME>");
        outPrintWriter.println("      Set app to run in the specified game mode, if supported.");
        outPrintWriter.println("      --user <USER_ID>: apply for the given user,");
        outPrintWriter.println("                        the current user is used when unspecified.");
        outPrintWriter.println("  set [intervention configs] <PACKAGE_NAME>");
        outPrintWriter.println("      Set app to run at custom mode using provided intervention configs");
        outPrintWriter.println("      Intervention configs consists of:");
        outPrintWriter.println("      --downscale [0.3|0.35|0.4|0.45|0.5|0.55|0.6|0.65");
        outPrintWriter.println("                  |0.7|0.75|0.8|0.85|0.9|disable]: Set app to run at the");
        outPrintWriter.println("                                                   specified scaling ratio.");
        outPrintWriter.println("      --fps [30|45|60|90|120|disable]: Set app to run at the specified fps,");
        outPrintWriter.println("                                       if supported.");
        outPrintWriter.println("  reset [--mode [2|3|performance|battery] --user <USER_ID>] <PACKAGE_NAME>");
        outPrintWriter.println("      Resets the game mode of the app to device configuration.");
        outPrintWriter.println("      This should only be used to reset any override to non custom game mode");
        outPrintWriter.println("      applied using the deprecated `set` command");
        outPrintWriter.println("      --mode [2|3|performance|battery]: apply for the given mode,");
        outPrintWriter.println("                                        resets all modes when unspecified.");
        outPrintWriter.println("      --user <USER_ID>: apply for the given user,");
        outPrintWriter.println("                        the current user is used when unspecified.");
        this.mIGameManagerShellCommandExt.onHelp(outPrintWriter);
    }
}
