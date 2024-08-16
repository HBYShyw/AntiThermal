package com.android.server.locksettings;

import android.app.ActivityManager;
import android.app.admin.PasswordMetrics;
import android.content.Context;
import android.os.ShellCommand;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class LockSettingsShellCommand extends ShellCommand {
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_GET_DISABLED = "get-disabled";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_REMOVE_CACHE = "remove-cache";
    private static final String COMMAND_REQUIRE_STRONG_AUTH = "require-strong-auth";
    private static final String COMMAND_SET_DISABLED = "set-disabled";
    private static final String COMMAND_SET_PASSWORD = "set-password";
    private static final String COMMAND_SET_PATTERN = "set-pattern";
    private static final String COMMAND_SET_PIN = "set-pin";
    private static final String COMMAND_SET_ROR_PROVIDER_PACKAGE = "set-resume-on-reboot-provider-package";
    private static final String COMMAND_VERIFY = "verify";
    private final int mCallingPid;
    private final int mCallingUid;
    private final Context mContext;
    private int mCurrentUserId;
    private final LockPatternUtils mLockPatternUtils;
    private String mOld = "";
    private String mNew = "";

    /* JADX INFO: Access modifiers changed from: package-private */
    public LockSettingsShellCommand(LockPatternUtils lockPatternUtils, Context context, int i, int i2) {
        this.mLockPatternUtils = lockPatternUtils;
        this.mCallingPid = i;
        this.mCallingUid = i2;
        this.mContext = context;
    }

    public int onCommand(String str) {
        char c;
        char c2;
        char c3;
        if (str == null) {
            return handleDefaultCommands(str);
        }
        try {
            this.mCurrentUserId = ActivityManager.getService().getCurrentUser().id;
            parseArgs();
            boolean z = true;
            if (!this.mLockPatternUtils.hasSecureLockScreen()) {
                switch (str.hashCode()) {
                    case -1473704173:
                        if (str.equals(COMMAND_GET_DISABLED)) {
                            c3 = 1;
                            break;
                        }
                        c3 = 65535;
                        break;
                    case 3198785:
                        if (str.equals(COMMAND_HELP)) {
                            c3 = 0;
                            break;
                        }
                        c3 = 65535;
                        break;
                    case 75288455:
                        if (str.equals(COMMAND_SET_DISABLED)) {
                            c3 = 2;
                            break;
                        }
                        c3 = 65535;
                        break;
                    case 1062640281:
                        if (str.equals(COMMAND_SET_ROR_PROVIDER_PACKAGE)) {
                            c3 = 3;
                            break;
                        }
                        c3 = 65535;
                        break;
                    default:
                        c3 = 65535;
                        break;
                }
                if (c3 != 0 && c3 != 1 && c3 != 2 && c3 != 3) {
                    getErrPrintWriter().println("The device does not support lock screen - ignoring the command.");
                    return -1;
                }
            }
            switch (str.hashCode()) {
                case -1957541639:
                    if (str.equals(COMMAND_REMOVE_CACHE)) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1473704173:
                    if (str.equals(COMMAND_GET_DISABLED)) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -868666698:
                    if (str.equals(COMMAND_REQUIRE_STRONG_AUTH)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 3198785:
                    if (str.equals(COMMAND_HELP)) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 75288455:
                    if (str.equals(COMMAND_SET_DISABLED)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 1062640281:
                    if (str.equals(COMMAND_SET_ROR_PROVIDER_PACKAGE)) {
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
                runRemoveCache();
                return 0;
            }
            if (c == 1) {
                runSetResumeOnRebootProviderPackage();
                return 0;
            }
            if (c == 2) {
                runRequireStrongAuth();
                return 0;
            }
            if (c == 3) {
                onHelp();
                return 0;
            }
            if (c == 4) {
                runGetDisabled();
                return 0;
            }
            if (c == 5) {
                runSetDisabled();
                return 0;
            }
            if (!checkCredential()) {
                return -1;
            }
            switch (str.hashCode()) {
                case -2044327643:
                    if (str.equals(COMMAND_SET_PATTERN)) {
                        c2 = 0;
                        break;
                    }
                    c2 = 65535;
                    break;
                case -819951495:
                    if (str.equals(COMMAND_VERIFY)) {
                        c2 = 4;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 94746189:
                    if (str.equals(COMMAND_CLEAR)) {
                        c2 = 3;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 1021333414:
                    if (str.equals(COMMAND_SET_PASSWORD)) {
                        c2 = 1;
                        break;
                    }
                    c2 = 65535;
                    break;
                case 1983832490:
                    if (str.equals(COMMAND_SET_PIN)) {
                        c2 = 2;
                        break;
                    }
                    c2 = 65535;
                    break;
                default:
                    c2 = 65535;
                    break;
            }
            if (c2 == 0) {
                z = runSetPattern();
            } else if (c2 == 1) {
                z = runSetPassword();
            } else if (c2 == 2) {
                z = runSetPin();
            } else if (c2 == 3) {
                z = runClear();
            } else if (c2 == 4) {
                runVerify();
            } else {
                getErrPrintWriter().println("Unknown command: " + str);
            }
            return z ? 0 : -1;
        } catch (Exception e) {
            getErrPrintWriter().println("Error while executing command: " + str);
            e.printStackTrace(getErrPrintWriter());
            return -1;
        }
    }

    private void runVerify() {
        getOutPrintWriter().println("Lock credential verified successfully");
    }

    public void onHelp() {
        PrintWriter outPrintWriter = getOutPrintWriter();
        try {
            outPrintWriter.println("lockSettings service commands:");
            outPrintWriter.println("");
            outPrintWriter.println("NOTE: when lock screen is set, all commands require the --old <CREDENTIAL> argument.");
            outPrintWriter.println("");
            outPrintWriter.println("  help");
            outPrintWriter.println("    Prints this help text.");
            outPrintWriter.println("");
            outPrintWriter.println("  get-disabled [--old <CREDENTIAL>] [--user USER_ID]");
            outPrintWriter.println("    Checks whether lock screen is disabled.");
            outPrintWriter.println("");
            outPrintWriter.println("  set-disabled [--old <CREDENTIAL>] [--user USER_ID] <true|false>");
            outPrintWriter.println("    When true, disables lock screen.");
            outPrintWriter.println("");
            outPrintWriter.println("  set-pattern [--old <CREDENTIAL>] [--user USER_ID] <PATTERN>");
            outPrintWriter.println("    Sets the lock screen as pattern, using the given PATTERN to unlock.");
            outPrintWriter.println("");
            outPrintWriter.println("  set-pin [--old <CREDENTIAL>] [--user USER_ID] <PIN>");
            outPrintWriter.println("    Sets the lock screen as PIN, using the given PIN to unlock.");
            outPrintWriter.println("");
            outPrintWriter.println("  set-password [--old <CREDENTIAL>] [--user USER_ID] <PASSWORD>");
            outPrintWriter.println("    Sets the lock screen as password, using the given PASSOWRD to unlock.");
            outPrintWriter.println("");
            outPrintWriter.println("  clear [--old <CREDENTIAL>] [--user USER_ID]");
            outPrintWriter.println("    Clears the lock credentials.");
            outPrintWriter.println("");
            outPrintWriter.println("  verify [--old <CREDENTIAL>] [--user USER_ID]");
            outPrintWriter.println("    Verifies the lock credentials.");
            outPrintWriter.println("");
            outPrintWriter.println("  remove-cache [--user USER_ID]");
            outPrintWriter.println("    Removes cached unified challenge for the managed profile.");
            outPrintWriter.println("");
            outPrintWriter.println("  set-resume-on-reboot-provider-package <package_name>");
            outPrintWriter.println("    Sets the package name for server based resume on reboot service provider.");
            outPrintWriter.println("");
            outPrintWriter.println("  require-strong-auth [--user USER_ID] <reason>");
            outPrintWriter.println("    Requires the strong authentication. The current supported reasons: STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN.");
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

    private void parseArgs() {
        while (true) {
            String nextOption = getNextOption();
            if (nextOption != null) {
                if ("--old".equals(nextOption)) {
                    this.mOld = getNextArgRequired();
                } else if ("--user".equals(nextOption)) {
                    int parseUserArg = UserHandle.parseUserArg(getNextArgRequired());
                    this.mCurrentUserId = parseUserArg;
                    if (parseUserArg == -2) {
                        this.mCurrentUserId = ActivityManager.getCurrentUser();
                    }
                } else {
                    getErrPrintWriter().println("Unknown option: " + nextOption);
                    throw new IllegalArgumentException();
                }
            } else {
                this.mNew = getNextArg();
                return;
            }
        }
    }

    private LockscreenCredential getOldCredential() {
        if (TextUtils.isEmpty(this.mOld)) {
            return LockscreenCredential.createNone();
        }
        if (this.mLockPatternUtils.isLockPasswordEnabled(this.mCurrentUserId)) {
            if (LockPatternUtils.isQualityAlphabeticPassword(this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mCurrentUserId))) {
                return LockscreenCredential.createPassword(this.mOld);
            }
            return LockscreenCredential.createPin(this.mOld);
        }
        if (this.mLockPatternUtils.isLockPatternEnabled(this.mCurrentUserId)) {
            return LockscreenCredential.createPattern(LockPatternUtils.byteArrayToPattern(this.mOld.getBytes()));
        }
        return LockscreenCredential.createPassword(this.mOld);
    }

    private boolean runSetPattern() {
        LockscreenCredential createPattern = LockscreenCredential.createPattern(LockPatternUtils.byteArrayToPattern(this.mNew.getBytes()));
        if (!isNewCredentialSufficient(createPattern)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(createPattern, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Pattern set to '" + this.mNew + "'");
        return true;
    }

    private boolean runSetPassword() {
        LockscreenCredential createPassword = LockscreenCredential.createPassword(this.mNew);
        if (!isNewCredentialSufficient(createPassword)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(createPassword, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Password set to '" + this.mNew + "'");
        return true;
    }

    private boolean runSetPin() {
        LockscreenCredential createPin = LockscreenCredential.createPin(this.mNew);
        if (!isNewCredentialSufficient(createPin)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(createPin, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Pin set to '" + this.mNew + "'");
        return true;
    }

    private boolean runSetResumeOnRebootProviderPackage() {
        String str = this.mNew;
        Slog.i("ShellCommand", "Setting persist.sys.resume_on_reboot_provider_package to " + str);
        this.mContext.enforcePermission("android.permission.BIND_RESUME_ON_REBOOT_SERVICE", this.mCallingPid, this.mCallingUid, "ShellCommand");
        SystemProperties.set("persist.sys.resume_on_reboot_provider_package", str);
        return true;
    }

    private boolean runRequireStrongAuth() {
        String str = this.mNew;
        str.hashCode();
        if (str.equals("STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN")) {
            this.mCurrentUserId = -1;
            this.mLockPatternUtils.requireStrongAuth(32, -1);
            getOutPrintWriter().println("Require strong auth for USER_ID " + this.mCurrentUserId + " because of " + this.mNew);
            return true;
        }
        getErrPrintWriter().println("Unsupported reason: " + str);
        return false;
    }

    private boolean runClear() {
        LockscreenCredential createNone = LockscreenCredential.createNone();
        if (!isNewCredentialSufficient(createNone)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(createNone, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Lock credential cleared");
        return true;
    }

    private boolean isNewCredentialSufficient(LockscreenCredential lockscreenCredential) {
        List validatePassword;
        PasswordMetrics requestedPasswordMetrics = this.mLockPatternUtils.getRequestedPasswordMetrics(this.mCurrentUserId);
        int requestedPasswordComplexity = this.mLockPatternUtils.getRequestedPasswordComplexity(this.mCurrentUserId);
        if (lockscreenCredential.isPassword() || lockscreenCredential.isPin()) {
            validatePassword = PasswordMetrics.validatePassword(requestedPasswordMetrics, requestedPasswordComplexity, lockscreenCredential.isPin(), lockscreenCredential.getCredential());
        } else {
            validatePassword = PasswordMetrics.validatePasswordMetrics(requestedPasswordMetrics, requestedPasswordComplexity, new PasswordMetrics(lockscreenCredential.isPattern() ? 1 : -1));
        }
        if (validatePassword.isEmpty()) {
            return true;
        }
        getOutPrintWriter().println("New credential doesn't satisfy admin policies: " + validatePassword.get(0));
        return false;
    }

    private void runSetDisabled() {
        boolean parseBoolean = Boolean.parseBoolean(this.mNew);
        this.mLockPatternUtils.setLockScreenDisabled(parseBoolean, this.mCurrentUserId);
        getOutPrintWriter().println("Lock screen disabled set to " + parseBoolean);
    }

    private void runGetDisabled() {
        getOutPrintWriter().println(this.mLockPatternUtils.isLockScreenDisabled(this.mCurrentUserId));
    }

    private boolean checkCredential() {
        if (this.mLockPatternUtils.isSecure(this.mCurrentUserId)) {
            if (this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(this.mCurrentUserId)) {
                getOutPrintWriter().println("Profile uses unified challenge");
                return false;
            }
            try {
                boolean checkCredential = this.mLockPatternUtils.checkCredential(getOldCredential(), this.mCurrentUserId, (LockPatternUtils.CheckCredentialProgressCallback) null);
                if (!checkCredential) {
                    if (!this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(this.mCurrentUserId)) {
                        this.mLockPatternUtils.reportFailedPasswordAttempt(this.mCurrentUserId);
                    }
                    getOutPrintWriter().println("Old password '" + this.mOld + "' didn't match");
                } else {
                    this.mLockPatternUtils.reportSuccessfulPasswordAttempt(this.mCurrentUserId);
                }
                return checkCredential;
            } catch (LockPatternUtils.RequestThrottledException unused) {
                getOutPrintWriter().println("Request throttled");
                return false;
            }
        }
        if (this.mOld.isEmpty()) {
            return true;
        }
        getOutPrintWriter().println("Old password provided but user has no password");
        return false;
    }

    private void runRemoveCache() {
        this.mLockPatternUtils.removeCachedUnifiedChallenge(this.mCurrentUserId);
        getOutPrintWriter().println("Password cached removed for user " + this.mCurrentUserId);
    }
}
