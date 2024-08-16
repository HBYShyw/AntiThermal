package com.oplus.ota;

import android.content.Context;
import android.os.IBinder;
import android.os.IRecoverySystem;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusRecoverySystem {
    private static final File LOG_FILE;
    private static final int REAL_PATH_START_NUM = 4;
    private static final File RECOVERY_DIR;
    private static final String TAG = "OplusRecoverySystem";
    private static final int WAIT_TIME = 3000;
    private static File sRECOVERYDIR = new File("/cache/recovery");
    private static File sCOMMANDFILE = new File(sRECOVERYDIR, "command");
    private static File sLOGFILE = new File(sRECOVERYDIR, "log");

    static {
        File file = new File("/cache/recovery");
        RECOVERY_DIR = file;
        LOG_FILE = new File(file, "log");
    }

    public static void installOplusOtaPackage(Context context, ArrayList<File> packageFileList) throws IOException {
        if (packageFileList == null) {
            Log.e(TAG, "installOplusOtaPackage failed before reboot, packageFileList is null!!!");
        } else {
            File packageFile = packageFileList.get(0);
            RecoverySystem.installPackage(context, packageFile);
        }
    }

    public static void installOplusOtaPackageSpecial(Context context, ArrayList<File> packageFileList) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (packageFileList == null) {
            Log.e(TAG, "installOplusOtaPackage failed before reboot, packageFileList is null!!!");
            return;
        }
        StringBuilder command = new StringBuilder();
        boolean bWipeData = false;
        Iterator<File> it = packageFileList.iterator();
        while (it.hasNext()) {
            File packageFile = it.next();
            String filename = packageFile.getCanonicalPath();
            if (filename == null || !filename.equals("/--wipe_data")) {
                Log.i(TAG, "filename=" + filename);
                if (filename != null && filename.startsWith("/mnt")) {
                    filename = filename.substring(4);
                }
                Log.w(TAG, "!!! REBOOT TO INSTALL " + filename + " !!!");
                if (filename != null) {
                    String arg = "--special_update_package=" + filename;
                    command.append(arg);
                    command.append("\n");
                }
            } else {
                bWipeData = true;
            }
        }
        if (bWipeData) {
            Log.w(TAG, "!!!WIPE DATA FOR OTA!!!");
            command.append("--wipe_data");
            command.append("\n");
        }
        rebootToRecoveryWithCommand(context, command.toString());
        throw new IOException("Reboot failed (no permissions?)");
    }

    public static void oplusInstallPackageSpecial(Context context, File packageFile) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        String filename = packageFile.getCanonicalPath();
        FileWriter uncryptFile = new FileWriter(new File(sRECOVERYDIR, "uncrypt_file"));
        try {
            uncryptFile.write(filename + "\n");
            uncryptFile.close();
            Log.w(TAG, "!!! REBOOTING TO INSTALL " + filename + " !!!");
            String filenameArg = "--special_update_package=" + filename;
            String localeArg = "--locale=" + Locale.getDefault().toString();
            rebootToRecoveryWithCommand(context, filenameArg, localeArg);
        } catch (Throwable th) {
            uncryptFile.close();
            throw th;
        }
    }

    public static void oplusInstallPackageList(Context context, ArrayList<File> packageFileList) throws IOException {
        if (packageFileList == null) {
            Log.e(TAG, "oplusInstallPackageList failed before reboot, packageFileList is null!!!");
            return;
        }
        StringBuilder command = new StringBuilder();
        Iterator<File> it = packageFileList.iterator();
        while (it.hasNext()) {
            File packageFile = it.next();
            String filename = packageFile.getCanonicalPath();
            Log.w(TAG, "!!! REBOOT TO INSTALL " + filename + " !!!");
            if (!TextUtils.isEmpty(filename)) {
                String arg = "--special_update_package=" + filename;
                command.append(arg);
                command.append("\n");
            }
        }
        String localeArg = "--locale=" + Locale.getDefault().toString();
        if (!TextUtils.isEmpty(localeArg)) {
            command.append(localeArg);
            command.append("\n");
        }
        rebootToRecoveryWithCommand(context, command.toString());
        throw new IOException("Reboot failed (no permissions?)");
    }

    private static void setupBcb(Context context, String... args) {
        LOG_FILE.delete();
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            if (!TextUtils.isEmpty(arg)) {
                command.append(arg);
                command.append("\n");
            }
        }
        try {
            IBinder service = ServiceManager.getService("recovery");
            IRecoverySystem recoveryService = IRecoverySystem.Stub.asInterface(service);
            recoveryService.setupBcb(command.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void rebootToRecoveryWithCommand(Context context, String... args) throws IOException {
        LOG_FILE.delete();
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            if (!TextUtils.isEmpty(arg)) {
                command.append(arg);
                command.append("\n");
            }
        }
        try {
            IBinder service = ServiceManager.getService("recovery");
            IRecoverySystem recoveryService = IRecoverySystem.Stub.asInterface(service);
            recoveryService.rebootRecoveryWithCommand(command.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        throw new IOException("Reboot failed (no permissions?)");
    }

    private static void bootCommand(Context context, String... args) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        sRECOVERYDIR.mkdirs();
        sCOMMANDFILE.delete();
        sLOGFILE.delete();
        FileWriter command = new FileWriter(sCOMMANDFILE);
        try {
            for (String arg : args) {
                if (!TextUtils.isEmpty(arg)) {
                    command.write(arg);
                    command.write("\n");
                }
            }
            command.close();
            PowerManager pm = (PowerManager) context.getSystemService("power");
            pm.reboot("recovery");
            throw new IOException("Reboot failed (no permissions?)");
        } catch (Throwable th) {
            command.close();
            throw th;
        }
    }

    public static void installOplusSauPackageSpecial(Context context, ArrayList<File> packageFileList) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (packageFileList == null) {
            Log.e(TAG, "installOplusSauPackageSpecial failed before reboot, packageFileList is null!!!");
            return;
        }
        sLOGFILE.delete();
        StringBuilder command = new StringBuilder();
        boolean bWipeData = false;
        Iterator<File> it = packageFileList.iterator();
        while (it.hasNext()) {
            File packageFile = it.next();
            String filename = packageFile.getCanonicalPath();
            if (filename == null || !filename.equals("/--wipe_data")) {
                Log.i(TAG, "filename=" + filename);
                if (filename != null && filename.startsWith("/mnt")) {
                    filename = filename.substring(4);
                }
                Log.w(TAG, "!!! REBOOT TO INSTALL " + filename + " !!!");
                if (filename != null) {
                    String arg = "--special_update_package=" + filename;
                    command.append(arg);
                    command.append("\n");
                }
            } else {
                bWipeData = true;
            }
        }
        if (bWipeData) {
            Log.w(TAG, "!!!WIPE DATA FOR OTA!!!");
            command.append("--wipe_data");
            command.append("\n");
        }
        setupBcb(context, command.toString());
        PowerManager pm = (PowerManager) context.getSystemService("power");
        pm.reboot("sau");
        throw new IOException("Reboot failed (no permissions?)");
    }

    public static void installOplusSauPackage(Context context, ArrayList<File> packageFileList) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (packageFileList == null) {
            Log.e(TAG, "installOplusSauPackage failed before reboot, packageFileList is null!!!");
            return;
        }
        sRECOVERYDIR.mkdirs();
        sCOMMANDFILE.delete();
        sLOGFILE.delete();
        FileWriter command = new FileWriter(sCOMMANDFILE);
        boolean bWipeData = false;
        try {
            Iterator<File> it = packageFileList.iterator();
            while (it.hasNext()) {
                File packageFile = it.next();
                String filename = packageFile.getCanonicalPath();
                if (filename == null || !filename.equals("/--wipe_data")) {
                    Log.i(TAG, "installOplusSauPackage filename=" + filename);
                    if (filename != null && filename.startsWith("/mnt")) {
                        filename = filename.substring(4);
                    }
                    Log.w(TAG, "!!! REBOOT TO INSTALL " + filename + " !!!");
                    if (filename != null) {
                        String arg = "--update_package=" + filename;
                        command.write(arg);
                        command.write("\n");
                    }
                } else {
                    bWipeData = true;
                }
            }
            if (bWipeData) {
                Log.w(TAG, "!!!WIPE DATA FOR OTA!!!");
                command.write("--wipe_data");
                command.write("\n");
            }
            command.close();
            PowerManager pm = (PowerManager) context.getSystemService("power");
            pm.reboot("sau");
            throw new IOException("Reboot failed (no permissions?)");
        } catch (Throwable th) {
            command.close();
            throw th;
        }
    }

    public static void installOplusLocalPackage(Context context, ArrayList<File> packageFileList) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (packageFileList == null) {
            Log.e(TAG, "installOplusLocalPackage failed before reboot, packageFileList is null!!!");
            return;
        }
        sLOGFILE.delete();
        StringBuilder command = new StringBuilder();
        boolean bWipeData = false;
        Iterator<File> it = packageFileList.iterator();
        while (it.hasNext()) {
            File packageFile = it.next();
            String filename = packageFile.getCanonicalPath();
            if (filename == null || !filename.equals("/--wipe_data")) {
                Log.i(TAG, "filename=" + filename);
                if (filename != null && filename.startsWith("/mnt")) {
                    filename = filename.substring(4);
                }
                Log.w(TAG, "installOplusLocalPackage !!! REBOOT TO INSTALL " + filename + " !!!");
                if (filename != null) {
                    String arg = "--update_package=" + filename;
                    command.append(arg);
                    command.append("\n");
                }
            } else {
                bWipeData = true;
            }
        }
        if (bWipeData) {
            Log.w(TAG, "!!!WIPE DATA FOR OTA!!!");
            command.append("--wipe_data");
            command.append("\n");
        }
        setupBcb(context, command.toString());
        PowerManager pm = (PowerManager) context.getSystemService("power");
        pm.reboot("recovery");
        throw new IOException("Reboot failed (no permissions?)");
    }

    public static void restoreOplusModem(Context context, String imagePath, int type) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (imagePath == null) {
            Log.e(TAG, "restoreOplusModem failed before reboot, packageFileList is null!!!");
            return;
        }
        sRECOVERYDIR.mkdirs();
        sCOMMANDFILE.delete();
        sLOGFILE.delete();
        FileWriter command = new FileWriter(sCOMMANDFILE);
        try {
            File imageFile = new File(imagePath);
            String filename = imageFile.getCanonicalPath();
            if (filename != null && filename.startsWith("/mnt")) {
                filename = filename.substring(4);
            }
            Log.w(TAG, "!!! REBOOT TO RESTORE " + filename + " !!!");
            if (filename != null) {
                String arg = "--restore_modem=" + filename;
                command.write(arg);
                command.write("\n");
            }
            command.close();
            PowerManager pm = (PowerManager) context.getSystemService("power");
            pm.reboot("recovery");
            throw new IOException("Reboot failed (no permissions?)");
        } catch (Throwable th) {
            command.close();
            throw th;
        }
    }

    public static void oplusInstallPackageFromNvId(Context context, String nvID) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        if (!TextUtils.isEmpty(nvID)) {
            Log.w(TAG, "!!! REBOOTING TO OTG INSTALL, nvID= " + nvID + " !!!");
            String filenameArg = "--update_package_nv_id=" + nvID;
            String localeArg = "--locale=" + Locale.getDefault().toString();
            rebootToRecoveryWithCommand(context, filenameArg, localeArg);
        }
    }

    public static void installVabPackage(Context context, File payload, File payloadProperties) throws IOException {
        context.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        String updateArgs = null;
        RecoverySystem.BLOCK_MAP_FILE.delete();
        try {
            RecoverySystem.processPackage(context, payloadProperties, null, null);
            SystemProperties.set("sys.oplus.copy_map_file", "payload_properties");
            updateArgs = "@/cache/recovery/payload_properties.map";
            Thread.sleep(3000L);
        } catch (Exception e) {
            Log.e(TAG, "Error uncrypting payload_properties file", e);
        }
        while (true) {
            Log.w(TAG, "sleep loop for wait copy payload_properties to reserve done");
            if (SystemProperties.getInt("sys.oplus.copy_map_file", -1) == 0) {
                break;
            }
            try {
                Thread.sleep(3000L);
            } catch (Exception e2) {
                Log.e(TAG, "sleep error");
            }
        }
        Log.w(TAG, "cpoy payload_properties done");
        Log.w(TAG, "temp debug");
        RecoverySystem.BLOCK_MAP_FILE.delete();
        try {
            RecoverySystem.processPackage(context, payload, null, null);
            SystemProperties.set("sys.oplus.copy_map_file", "payload");
            Log.w(TAG, "copy paylod.map to reserve");
            updateArgs = "@/cache/recovery/payload.map;" + updateArgs;
            Thread.sleep(3000L);
        } catch (Exception e3) {
            Log.e(TAG, "Error uncrypting payload file", e3);
        }
        while (true) {
            Log.w(TAG, "sleep loop for wait copy payload_properties to reserve done");
            if (SystemProperties.getInt("sys.oplus.copy_map_file", -1) == 0) {
                break;
            }
            try {
                Thread.sleep(3000L);
            } catch (Exception e4) {
                Log.e(TAG, "sleep error");
            }
        }
        Log.w(TAG, "cpoy payload_properties done");
        Log.w(TAG, "start reboot recovery install wifi-ota with args:" + updateArgs);
        if (updateArgs != null) {
            String filenameArg = "--vab_wifi_ota=" + updateArgs;
            String localeArg = "--locale=" + Locale.getDefault().toString();
            rebootToRecoveryWithCommand(context, filenameArg, localeArg);
        }
    }
}
