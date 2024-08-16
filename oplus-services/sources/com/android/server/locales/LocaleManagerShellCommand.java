package com.android.server.locales;

import android.app.ActivityManager;
import android.app.ILocaleManager;
import android.app.LocaleConfig;
import android.os.LocaleList;
import android.os.RemoteException;
import android.os.ShellCommand;
import android.os.UserHandle;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocaleManagerShellCommand extends ShellCommand {
    private final ILocaleManager mBinderService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocaleManagerShellCommand(ILocaleManager iLocaleManager) {
        this.mBinderService = iLocaleManager;
    }

    public int onCommand(String str) {
        if (str == null) {
            return handleDefaultCommands(str);
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -843437997:
                if (str.equals("set-app-localeconfig")) {
                    c = 0;
                    break;
                }
                break;
            case -232514593:
                if (str.equals("get-app-localeconfig")) {
                    c = 1;
                    break;
                }
                break;
            case 819706294:
                if (str.equals("get-app-locales")) {
                    c = 2;
                    break;
                }
                break;
            case 1730458818:
                if (str.equals("set-app-locales")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return runSetAppOverrideLocaleConfig();
            case 1:
                return runGetAppOverrideLocaleConfig();
            case 2:
                return runGetAppLocales();
            case 3:
                return runSetAppLocales();
            default:
                return handleDefaultCommands(str);
        }
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        outPrintWriter.println("Locale manager (locale) shell commands:");
        outPrintWriter.println("  help");
        outPrintWriter.println("      Print this help text.");
        outPrintWriter.println("  set-app-locales <PACKAGE_NAME> [--user <USER_ID>] [--locales <LOCALE_INFO>][--delegate <FROM_DELEGATE>]");
        outPrintWriter.println("      Set the locales for the specified app.");
        outPrintWriter.println("      --user <USER_ID>: apply for the given user, the current user is used when unspecified.");
        outPrintWriter.println("      --locales <LOCALE_INFO>: The language tags of locale to be included as a single String separated by commas.");
        outPrintWriter.println("                 eg. en,en-US,hi ");
        outPrintWriter.println("                 Empty locale list is used when unspecified.");
        outPrintWriter.println("      --delegate <FROM_DELEGATE>: The locales are set from a delegate, the value could be true or false. false is the default when unspecified.");
        outPrintWriter.println("  get-app-locales <PACKAGE_NAME> [--user <USER_ID>]");
        outPrintWriter.println("      Get the locales for the specified app.");
        outPrintWriter.println("      --user <USER_ID>: get for the given user, the current user is used when unspecified.");
        outPrintWriter.println("  set-app-localeconfig <PACKAGE_NAME> [--user <USER_ID>] [--locales <LOCALE_INFO>]");
        outPrintWriter.println("      Set the override LocaleConfig for the specified app.");
        outPrintWriter.println("      --user <USER_ID>: apply for the given user, the current user is used when unspecified.");
        outPrintWriter.println("      --locales <LOCALE_INFO>: The language tags of locale to be included as a single String separated by commas.");
        outPrintWriter.println("                 eg. en,en-US,hi ");
        outPrintWriter.println("                 Empty locale list is used when typing a 'empty' word");
        outPrintWriter.println("                 NULL is used when unspecified.");
        outPrintWriter.println("  get-app-localeconfig <PACKAGE_NAME> [--user <USER_ID>]");
        outPrintWriter.println("      Get the locales within the override LocaleConfig for the specified app.");
        outPrintWriter.println("      --user <USER_ID>: get for the given user, the current user is used when unspecified.");
    }

    private int runSetAppLocales() {
        char c;
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg != null) {
            int currentUser = ActivityManager.getCurrentUser();
            LocaleList emptyLocaleList = LocaleList.getEmptyLocaleList();
            boolean z = false;
            while (true) {
                String nextOption = getNextOption();
                if (nextOption != null) {
                    switch (nextOption.hashCode()) {
                        case 835076901:
                            if (nextOption.equals("--delegate")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 1333469547:
                            if (nextOption.equals("--user")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 1724392377:
                            if (nextOption.equals("--locales")) {
                                c = 2;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            z = parseFromDelegate();
                            break;
                        case 1:
                            currentUser = UserHandle.parseUserArg(getNextArgRequired());
                            break;
                        case 2:
                            emptyLocaleList = parseLocales();
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown option: " + nextOption);
                    }
                } else {
                    try {
                        this.mBinderService.setApplicationLocales(nextArg, currentUser, emptyLocaleList, z);
                    } catch (RemoteException e) {
                        getOutPrintWriter().println("Remote Exception: " + e);
                    } catch (IllegalArgumentException unused) {
                        getOutPrintWriter().println("Unknown package " + nextArg + " for userId " + currentUser);
                    }
                    return 0;
                }
            }
        } else {
            errPrintWriter.println("Error: no package specified");
            return -1;
        }
    }

    private int runGetAppLocales() {
        PrintWriter errPrintWriter = getErrPrintWriter();
        String nextArg = getNextArg();
        if (nextArg != null) {
            int currentUser = ActivityManager.getCurrentUser();
            String nextOption = getNextOption();
            if (nextOption != null) {
                if ("--user".equals(nextOption)) {
                    currentUser = UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new IllegalArgumentException("Unknown option: " + nextOption);
                }
            }
            try {
                LocaleList applicationLocales = this.mBinderService.getApplicationLocales(nextArg, currentUser);
                getOutPrintWriter().println("Locales for " + nextArg + " for user " + currentUser + " are [" + applicationLocales.toLanguageTags() + "]");
                return 0;
            } catch (RemoteException e) {
                getOutPrintWriter().println("Remote Exception: " + e);
                return 0;
            } catch (IllegalArgumentException unused) {
                getOutPrintWriter().println("Unknown package " + nextArg + " for userId " + currentUser);
                return 0;
            }
        }
        errPrintWriter.println("Error: no package specified");
        return -1;
    }

    private int runSetAppOverrideLocaleConfig() {
        String nextArg = getNextArg();
        if (nextArg != null) {
            int currentUser = ActivityManager.getCurrentUser();
            LocaleConfig localeConfig = null;
            LocaleList localeList = null;
            while (true) {
                String nextOption = getNextOption();
                if (nextOption != null) {
                    if (nextOption.equals("--user")) {
                        currentUser = UserHandle.parseUserArg(getNextArgRequired());
                    } else if (nextOption.equals("--locales")) {
                        localeList = parseOverrideLocales();
                    } else {
                        throw new IllegalArgumentException("Unknown option: " + nextOption);
                    }
                } else {
                    if (localeList != null) {
                        try {
                            localeConfig = new LocaleConfig(localeList);
                        } catch (RemoteException e) {
                            getOutPrintWriter().println("Remote Exception: " + e);
                            return 0;
                        }
                    }
                    this.mBinderService.setOverrideLocaleConfig(nextArg, currentUser, localeConfig);
                    return 0;
                }
            }
        } else {
            getErrPrintWriter().println("Error: no package specified");
            return -1;
        }
    }

    private int runGetAppOverrideLocaleConfig() {
        String nextArg = getNextArg();
        if (nextArg != null) {
            int currentUser = ActivityManager.getCurrentUser();
            String nextOption = getNextOption();
            if (nextOption != null) {
                if ("--user".equals(nextOption)) {
                    currentUser = UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new IllegalArgumentException("Unknown option: " + nextOption);
                }
            }
            try {
                LocaleConfig overrideLocaleConfig = this.mBinderService.getOverrideLocaleConfig(nextArg, currentUser);
                if (overrideLocaleConfig == null) {
                    getOutPrintWriter().println("LocaleConfig for " + nextArg + " for user " + currentUser + " is null");
                    return 0;
                }
                LocaleList supportedLocales = overrideLocaleConfig.getSupportedLocales();
                if (supportedLocales == null) {
                    getOutPrintWriter().println("Locales within the LocaleConfig for " + nextArg + " for user " + currentUser + " are null");
                    return 0;
                }
                getOutPrintWriter().println("Locales within the LocaleConfig for " + nextArg + " for user " + currentUser + " are [" + supportedLocales.toLanguageTags() + "]");
                return 0;
            } catch (RemoteException e) {
                getOutPrintWriter().println("Remote Exception: " + e);
                return 0;
            }
        }
        getErrPrintWriter().println("Error: no package specified");
        return -1;
    }

    private LocaleList parseOverrideLocales() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            return null;
        }
        if (nextArg.equals("empty")) {
            return LocaleList.getEmptyLocaleList();
        }
        if (nextArg.startsWith("-")) {
            throw new IllegalArgumentException("Unknown locales: " + nextArg);
        }
        return LocaleList.forLanguageTags(nextArg);
    }

    private LocaleList parseLocales() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            return LocaleList.getEmptyLocaleList();
        }
        if (nextArg.startsWith("-")) {
            throw new IllegalArgumentException("Unknown locales: " + nextArg);
        }
        return LocaleList.forLanguageTags(nextArg);
    }

    private boolean parseFromDelegate() {
        String nextArg = getNextArg();
        if (nextArg == null) {
            return false;
        }
        if (nextArg.startsWith("-")) {
            throw new IllegalArgumentException("Unknown source: " + nextArg);
        }
        return Boolean.parseBoolean(nextArg);
    }
}
