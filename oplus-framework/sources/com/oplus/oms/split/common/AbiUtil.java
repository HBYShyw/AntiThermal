package com.oplus.oms.split.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes.dex */
public class AbiUtil {
    private static final int ABI_DIR_LENGTH = 3;
    private static final String ARMV5 = "armeabi";
    private static final String ARMV7 = "armeabi-v7a";
    private static final String ARMV8 = "arm64-v8a";
    private static final String ARM_64 = "arm64";
    private static final String TAG = "Split:AbiUtil";
    private static final String X86 = "x86";
    private static final String X86_64 = "x86_64";
    private static final AtomicReference<String> sBasePrimaryAbi = new AtomicReference<>();
    private static final AtomicReference<String> sCurrentInstructionSet = new AtomicReference<>();

    private AbiUtil() {
    }

    private static List<String> getSupportedAbis() {
        return Arrays.asList(Build.SUPPORTED_ABIS);
    }

    private static String getCurrentInstructionSet() {
        AtomicReference<String> atomicReference = sCurrentInstructionSet;
        if (!TextUtils.isEmpty(atomicReference.get())) {
            return atomicReference.get();
        }
        try {
            Class<?> clazz = Class.forName("dalvik.system.VMRuntime");
            Method currentGet = clazz.getDeclaredMethod("getCurrentInstructionSet", new Class[0]);
            currentGet.setAccessible(true);
            atomicReference.compareAndSet(null, (String) currentGet.invoke(null, new Object[0]));
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            SplitLog.e(TAG, "getCurrentInstructionSet error", new Object[0]);
        }
        return sCurrentInstructionSet.get();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static String findPrimaryAbiFromCurrentInstructionSet(String currentInstructionSet) {
        char c;
        if (TextUtils.isEmpty(currentInstructionSet)) {
            return null;
        }
        switch (currentInstructionSet.hashCode()) {
            case -806050265:
                if (currentInstructionSet.equals(X86_64)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 117110:
                if (currentInstructionSet.equals(X86)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 93084186:
                if (currentInstructionSet.equals(ARM_64)) {
                    c = 2;
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
                return X86;
            case 1:
                return X86_64;
            case 2:
                return ARMV8;
            default:
                return null;
        }
    }

    private static String findPrimaryAbiFromProperties(Context context) throws IOException {
        InputStream is = null;
        try {
            try {
                is = context.getAssets().open("base.app.cpu.abilist.properties");
                Properties properties = new Properties();
                properties.load(is);
                String abis = properties.getProperty("abiList");
                if (!TextUtils.isEmpty(abis)) {
                    String[] abiArray = abis.split(",");
                    Set<String> abiList = new HashSet<>();
                    Collections.addAll(abiList, abiArray);
                    if (!abiList.isEmpty()) {
                        Set<String> sortedAbis = sortAbis(abiList);
                        SplitLog.d(TAG, "sorted abis: " + sortedAbis, new Object[0]);
                        return findBasePrimaryAbi(sortedAbis);
                    }
                }
                FileUtil.closeQuietly(is);
                return null;
            } catch (IOException e) {
                throw new IOException("Failed to read asset file 'assets/base.app.cpu.abilist.properties'!", e);
            }
        } finally {
            FileUtil.closeQuietly(is);
        }
    }

    private static String findPrimaryAbiFromBaseApk(Context context) throws IOException {
        String baseApk = context.getApplicationInfo().sourceDir;
        Set<String> apkSupportedAbis = new HashSet<>();
        try {
            ZipFile zipFile = new ZipFile(baseApk);
            try {
                Enumeration<? extends ZipEntry> e = zipFile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = e.nextElement();
                    String entryName = entry.getName();
                    if (entryName.charAt(0) >= 'l' && entryName.charAt(0) <= 'l' && entryName.startsWith("lib/") && entryName.endsWith(SplitConstants.DOT_SO)) {
                        String[] abiDirNames = entryName.split("/");
                        if (abiDirNames.length == 3) {
                            apkSupportedAbis.add(abiDirNames[1]);
                        }
                    }
                }
                zipFile.close();
                Set<String> sortedAbis = sortAbis(apkSupportedAbis);
                SplitLog.d(TAG, "sorted abis: " + sortedAbis, new Object[0]);
                return findBasePrimaryAbi(sortedAbis);
            } finally {
            }
        } catch (IOException e2) {
            throw new IOException("Failed to open base apk " + baseApk, e2);
        }
    }

    private static Set<String> sortAbis(Set<String> abis) {
        if (abis.isEmpty() || abis.size() == 1) {
            return abis;
        }
        Set<String> ret = new HashSet<>(abis.size());
        if (abis.contains(ARMV8)) {
            ret.add(ARMV8);
        }
        if (abis.contains(ARMV7)) {
            ret.add(ARMV7);
        }
        if (abis.contains(ARMV5)) {
            ret.add(ARMV5);
        }
        if (abis.contains(X86)) {
            ret.add(X86);
        }
        if (abis.contains(X86_64)) {
            ret.add(X86_64);
        }
        return ret;
    }

    public static String getBasePrimaryAbi(Context context) throws IOException {
        String str;
        if (context == null) {
            return null;
        }
        AtomicReference<String> atomicReference = sBasePrimaryAbi;
        if (!TextUtils.isEmpty(atomicReference.get())) {
            return atomicReference.get();
        }
        synchronized (AbiUtil.class) {
            ApplicationInfo info = context.getApplicationInfo();
            try {
                Field primaryCpuAbiField = ApplicationInfo.class.getField("primaryCpuAbi");
                primaryCpuAbiField.setAccessible(true);
                atomicReference.compareAndSet(null, (String) primaryCpuAbiField.get(info));
                SplitLog.d(TAG, "Succeed to get primaryCpuAbi " + atomicReference + " from ApplicationInfo.", new Object[0]);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                SplitLog.w(TAG, "Failed to get primaryCpuAbi from ApplicationInfo.", e);
            }
            AtomicReference<String> atomicReference2 = sBasePrimaryAbi;
            if (TextUtils.isEmpty(atomicReference2.get())) {
                String currentInstructionSet = getCurrentInstructionSet();
                atomicReference2.compareAndSet(null, findPrimaryAbiFromCurrentInstructionSet(currentInstructionSet));
                if (TextUtils.isEmpty(atomicReference2.get())) {
                    SplitLog.d(TAG, "Failed to get primaryCpuAbi from currentInstructionSet.", new Object[0]);
                    atomicReference2.compareAndSet(null, findPrimaryAbiFromProperties(context));
                    if (TextUtils.isEmpty(atomicReference2.get())) {
                        SplitLog.d(TAG, "Failed to get primaryCpuAbi from Properties.", new Object[0]);
                        atomicReference2.compareAndSet(null, findPrimaryAbiFromBaseApk(context));
                        SplitLog.d(TAG, "Succeed to get primaryCpuAbi " + atomicReference2 + " from BaseApk.", new Object[0]);
                    } else {
                        SplitLog.d(TAG, "Succeed to get primaryCpuAbi " + atomicReference2 + " from Properties.", new Object[0]);
                    }
                } else {
                    SplitLog.d(TAG, "Succeed to get primaryCpuAbi " + atomicReference2 + " from currentInstructionSet.", new Object[0]);
                }
            }
            str = atomicReference2.get();
        }
        return str;
    }

    private static String findBasePrimaryAbi(Collection<String> sortedAbis) throws IOException {
        List<String> supportedAbis = getSupportedAbis();
        if (sortedAbis == null || sortedAbis.isEmpty()) {
            return supportedAbis.get(0);
        }
        for (String abi : supportedAbis) {
            if (sortedAbis.contains(abi)) {
                return abi;
            }
        }
        throw new IOException("No supported abi for this device, supported abis:" + supportedAbis + ", sorted abis: " + sortedAbis);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x004f A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String findSplitPrimaryAbi(String basePrimaryAbi, List<String> splitAbis) {
        char c;
        if (splitAbis == null) {
            return null;
        }
        if (splitAbis.contains(basePrimaryAbi)) {
            return basePrimaryAbi;
        }
        switch (basePrimaryAbi.hashCode()) {
            case -738963905:
                if (basePrimaryAbi.equals(ARMV5)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 117110:
                if (basePrimaryAbi.equals(X86)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 145444210:
                if (basePrimaryAbi.equals(ARMV7)) {
                    c = 1;
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
            case 1:
                if (splitAbis.contains(ARMV5)) {
                    return ARMV5;
                }
                return null;
            case 2:
                List<String> supportedAbis = getSupportedAbis();
                if (supportedAbis.contains(ARMV7) && splitAbis.contains(ARMV7)) {
                    return ARMV7;
                }
                break;
            default:
                return null;
        }
    }
}
