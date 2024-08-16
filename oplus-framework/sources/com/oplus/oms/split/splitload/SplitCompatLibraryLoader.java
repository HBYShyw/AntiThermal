package com.oplus.oms.split.splitload;

import android.os.Build;
import com.oplus.oms.split.common.SplitLog;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
final class SplitCompatLibraryLoader {
    private static final String TAG = "SplitCompatLibraryLoader";

    private SplitCompatLibraryLoader() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void load(ClassLoader classLoader, File folder) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (folder == null || !folder.exists()) {
            SplitLog.e(TAG, "load, folder " + folder + " is illegal", new Object[0]);
            return;
        }
        try {
            V25.load(classLoader, folder);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            SplitLog.e(TAG, "load, v25 fail, sdk: " + Build.VERSION.SDK_INT + ", error: " + e.getMessage() + ", try to fallback to V23", new Object[0]);
            V23.load(classLoader, folder);
        }
    }

    /* loaded from: classes.dex */
    private static final class V23 {
        private V23() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void load(ClassLoader classLoader, File folder) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassCastException {
            Field pathListField = HiddenApiReflection.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);
            if (dexPathList == null) {
                SplitLog.w(SplitCompatLibraryLoader.TAG, "V23 dexPathList null", new Object[0]);
                return;
            }
            Field nativeLibraryDirectories = HiddenApiReflection.findField(dexPathList, "nativeLibraryDirectories");
            List<File> origLibDirs = (List) nativeLibraryDirectories.get(dexPathList);
            if (origLibDirs == null) {
                origLibDirs = new ArrayList<>(2);
            }
            Iterator<File> libDirIt = origLibDirs.iterator();
            while (true) {
                if (!libDirIt.hasNext()) {
                    break;
                }
                File libDir = libDirIt.next();
                if (folder.equals(libDir)) {
                    libDirIt.remove();
                    break;
                }
            }
            origLibDirs.add(0, folder);
            Field systemNativeLibraryDirectories = HiddenApiReflection.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> origSystemLibDirs = (List) systemNativeLibraryDirectories.get(dexPathList);
            if (origSystemLibDirs == null) {
                origSystemLibDirs = new ArrayList<>(2);
            }
            List<File> newLibDirs = new ArrayList<>(origLibDirs.size() + origSystemLibDirs.size() + 1);
            newLibDirs.addAll(origLibDirs);
            newLibDirs.addAll(origSystemLibDirs);
            Method makeElements = HiddenApiReflection.findMethod(dexPathList, "makePathElements", (Class<?>[]) new Class[]{List.class, File.class, List.class});
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            Object[] elements = (Object[]) makeElements.invoke(dexPathList, newLibDirs, null, suppressedExceptions);
            Field nativeLibraryPathElements = HiddenApiReflection.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }

    /* loaded from: classes.dex */
    private static final class V25 {
        private V25() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void load(ClassLoader classLoader, File folder) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassCastException {
            Field pathListField = HiddenApiReflection.findField(classLoader, "pathList");
            Object dexPathList = pathListField.get(classLoader);
            if (dexPathList == null) {
                SplitLog.w(SplitCompatLibraryLoader.TAG, "V25 dexPathList null", new Object[0]);
                return;
            }
            Field nativeLibraryDirectories = HiddenApiReflection.findField(dexPathList, "nativeLibraryDirectories");
            List<File> origLibDirs = (List) nativeLibraryDirectories.get(dexPathList);
            if (origLibDirs == null) {
                origLibDirs = new ArrayList<>(2);
            }
            Iterator<File> libDirIt = origLibDirs.iterator();
            while (true) {
                if (!libDirIt.hasNext()) {
                    break;
                }
                File libDir = libDirIt.next();
                if (folder.equals(libDir)) {
                    libDirIt.remove();
                    break;
                }
            }
            origLibDirs.add(0, folder);
            Field systemNativeLibraryDirectories = HiddenApiReflection.findField(dexPathList, "systemNativeLibraryDirectories");
            List<File> origSystemLibDirs = (List) systemNativeLibraryDirectories.get(dexPathList);
            if (origSystemLibDirs == null) {
                origSystemLibDirs = new ArrayList<>(2);
            }
            List<File> newLibDirs = new ArrayList<>(origLibDirs.size() + origSystemLibDirs.size() + 1);
            newLibDirs.addAll(origLibDirs);
            newLibDirs.addAll(origSystemLibDirs);
            Method makeElements = HiddenApiReflection.findMethod(dexPathList, "makePathElements", (Class<?>[]) new Class[]{List.class});
            Object[] elements = (Object[]) makeElements.invoke(dexPathList, newLibDirs);
            Field nativeLibraryPathElements = HiddenApiReflection.findField(dexPathList, "nativeLibraryPathElements");
            nativeLibraryPathElements.set(dexPathList, elements);
        }
    }
}
