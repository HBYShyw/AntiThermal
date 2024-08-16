package com.android.server.bluetooth;

import android.content.AttributionSource;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.BasicShellCommandHandler;
import java.io.PrintWriter;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothShellCommand extends BasicShellCommandHandler {
    private static final String TAG = BluetoothShellCommand.class.getSimpleName();

    @VisibleForTesting
    final BluetoothCommand[] mBluetoothCommands = {new Enable(), new Disable(), new WaitForAdapterState()};
    private final Context mContext;
    private final BluetoothManagerService mManagerService;

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public abstract class BluetoothCommand {
        final boolean mIsPrivileged;
        final String mName;

        abstract int exec(String str) throws RemoteException;

        abstract void onHelp(PrintWriter printWriter);

        BluetoothCommand(boolean z, String str) {
            this.mIsPrivileged = z;
            Objects.requireNonNull(str, "Command name cannot be null");
            this.mName = str;
        }

        String getName() {
            return this.mName;
        }

        boolean isMatch(String str) {
            return this.mName.equals(str);
        }

        boolean isPrivileged() {
            return this.mIsPrivileged;
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class Enable extends BluetoothCommand {
        Enable() {
            super(false, IOplusBluetoothManagerServiceExt.FLAG_ENABLE);
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(String str) throws RemoteException {
            return BluetoothShellCommand.this.mManagerService.enable(AttributionSource.myAttributionSource()) ? 0 : -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(PrintWriter printWriter) {
            printWriter.println("  " + getName());
            printWriter.println("    Enable Bluetooth on this device.");
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class Disable extends BluetoothCommand {
        Disable() {
            super(false, "disable");
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(String str) throws RemoteException {
            return BluetoothShellCommand.this.mManagerService.disable(AttributionSource.myAttributionSource(), true) ? 0 : -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(PrintWriter printWriter) {
            printWriter.println("  " + getName());
            printWriter.println("    Disable Bluetooth on this device.");
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class WaitForAdapterState extends BluetoothCommand {
        WaitForAdapterState() {
            super(false, "wait-for-state");
        }

        private int getWaitingState(String str) {
            if (!str.startsWith(getName() + ":")) {
                return -1;
            }
            String[] split = str.split(":", 2);
            if (split.length != 2 || !getName().equals(split[0])) {
                String str2 = getName() + ": Invalid state format: " + str;
                Log.e(BluetoothShellCommand.TAG, str2);
                PrintWriter errPrintWriter = BluetoothShellCommand.this.getErrPrintWriter();
                errPrintWriter.println(BluetoothShellCommand.TAG + ": " + str2);
                BluetoothShellCommand.this.printHelp(errPrintWriter);
                throw new IllegalArgumentException();
            }
            String str3 = split[1];
            str3.hashCode();
            if (str3.equals("STATE_OFF")) {
                return 10;
            }
            if (str3.equals("STATE_ON")) {
                return 12;
            }
            String str4 = getName() + ": Invalid state value: " + split[1] + ". From: " + str;
            Log.e(BluetoothShellCommand.TAG, str4);
            PrintWriter errPrintWriter2 = BluetoothShellCommand.this.getErrPrintWriter();
            errPrintWriter2.println(BluetoothShellCommand.TAG + ": " + str4);
            BluetoothShellCommand.this.printHelp(errPrintWriter2);
            throw new IllegalArgumentException();
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        boolean isMatch(String str) {
            return getWaitingState(str) != -1;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public int exec(String str) throws RemoteException {
            int i = BluetoothShellCommand.this.mManagerService.waitForManagerState(getWaitingState(str)) ? 0 : -1;
            Log.d(BluetoothShellCommand.TAG, str + ": Return value is " + i);
            return i;
        }

        @Override // com.android.server.bluetooth.BluetoothShellCommand.BluetoothCommand
        public void onHelp(PrintWriter printWriter) {
            printWriter.println("  " + getName() + ":<STATE>");
            printWriter.println("    Wait until the adapter state is <STATE>. <STATE> can be one of STATE_OFF | STATE_ON");
            printWriter.println("    Note: This command can timeout and failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothShellCommand(BluetoothManagerService bluetoothManagerService, Context context) {
        this.mManagerService = bluetoothManagerService;
        this.mContext = context;
    }

    public int onCommand(String str) {
        int callingUid;
        if (str == null) {
            return handleDefaultCommands((String) null);
        }
        for (BluetoothCommand bluetoothCommand : this.mBluetoothCommands) {
            if (bluetoothCommand.isMatch(str)) {
                if (bluetoothCommand.isPrivileged() && (callingUid = Binder.getCallingUid()) != 0) {
                    throw new SecurityException("Uid " + callingUid + " does not have access to " + str + " bluetooth command");
                }
                try {
                    PrintWriter outPrintWriter = getOutPrintWriter();
                    StringBuilder sb = new StringBuilder();
                    String str2 = TAG;
                    sb.append(str2);
                    sb.append(": Exec ");
                    sb.append(str);
                    outPrintWriter.println(sb.toString());
                    Log.d(str2, "Exec " + str);
                    int exec = bluetoothCommand.exec(str);
                    if (exec == 0) {
                        String str3 = str + ": Success";
                        Log.d(str2, str3);
                        getOutPrintWriter().println(str3);
                    } else {
                        String str4 = str + ": Failed with status=" + exec;
                        Log.e(str2, str4);
                        getErrPrintWriter().println(str2 + ": " + str4);
                    }
                    return exec;
                } catch (RemoteException e) {
                    Log.w(TAG, str + ": error\nException: " + e.getMessage());
                    getErrPrintWriter().println(str + ": error\nException: " + e.getMessage());
                    e.rethrowFromSystemServer();
                }
            }
        }
        return handleDefaultCommands(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printHelp(PrintWriter printWriter) {
        printWriter.println("Bluetooth Manager Commands:");
        printWriter.println("  help or -h");
        printWriter.println("    Print this help text.");
        for (BluetoothCommand bluetoothCommand : this.mBluetoothCommands) {
            bluetoothCommand.onHelp(printWriter);
        }
    }

    public void onHelp() {
        printHelp(getOutPrintWriter());
    }
}
