package com.oplus.oms.split.splitload;

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
final class SplitCompatDexLoader {
    private static final String TAG = "SplitCompatDexLoader";
    private static int sPatchDexCount = 0;

    private SplitCompatDexLoader() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void load(ClassLoader classLoader, File dexOptDir, List<File> files) throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (!files.isEmpty()) {
            V23.load(classLoader, files, dexOptDir);
            sPatchDexCount = files.size();
        } else {
            SplitLog.w(TAG, "load null", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void unLoad(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        if (sPatchDexCount <= 0) {
            return;
        }
        Field pathListField = HiddenApiReflection.findField(classLoader, "pathList");
        Object dexPathList = pathListField.get(classLoader);
        HiddenApiReflection.reduceFieldArray(dexPathList, "dexElements", sPatchDexCount);
    }

    /* loaded from: classes.dex */
    private static final class V23 {
        private V23() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void load(ClassLoader loader, List<File> additionalClassPathEntries, File optimizedDirectory) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException {
            Field pathListField = HiddenApiReflection.findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);
            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            HiddenApiReflection.expandFieldArray(dexPathList, "dexElements", makePathElements(dexPathList, new ArrayList(additionalClassPathEntries), optimizedDirectory, suppressedExceptions));
            if (!suppressedExceptions.isEmpty()) {
                Iterator<IOException> it = suppressedExceptions.iterator();
                if (it.hasNext()) {
                    IOException e = it.next();
                    SplitLog.e(SplitCompatDexLoader.TAG, "Exception in makePathElement", e);
                    throw e;
                }
            }
        }

        private static Object[] makePathElements(Object dexPathList, ArrayList<File> files, File optimizedDirectory, ArrayList<IOException> suppressedExceptions) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Method makePathElements;
            try {
                makePathElements = HiddenApiReflection.findMethod(dexPathList, "makePathElements", (Class<?>[]) new Class[]{List.class, File.class, List.class});
            } catch (NoSuchMethodException e) {
                SplitLog.e(SplitCompatDexLoader.TAG, "NoSuchMethodException: makePathElements(List,File,List) failure", new Object[0]);
                try {
                    makePathElements = HiddenApiReflection.findMethod(dexPathList, "makePathElements", (Class<?>[]) new Class[]{ArrayList.class, File.class, ArrayList.class});
                } catch (NoSuchMethodException e2) {
                    SplitLog.e(SplitCompatDexLoader.TAG, "NoSuchMethodException: makeDexElements(ArrayList,File,ArrayList) failure", new Object[0]);
                    try {
                        SplitLog.w(SplitCompatDexLoader.TAG, "NoSuchMethodException: try use v19 instead", new Object[0]);
                        return V19.makeDexElements(dexPathList, files, optimizedDirectory, suppressedExceptions);
                    } catch (NoSuchMethodException e22) {
                        SplitLog.e(SplitCompatDexLoader.TAG, "NoSuchMethodException: makeDexElements(List,File,List) failure", new Object[0]);
                        throw e22;
                    }
                }
            }
            return (Object[]) makePathElements.invoke(dexPathList, files, optimizedDirectory, suppressedExceptions);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class V19 {
        private V19() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, File optimizedDirectory, ArrayList<IOException> suppressedExceptions) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Method makeDexElements;
            try {
                makeDexElements = HiddenApiReflection.findMethod(dexPathList, "makeDexElements", (Class<?>[]) new Class[]{ArrayList.class, File.class, ArrayList.class});
            } catch (NoSuchMethodException e) {
                SplitLog.e(SplitCompatDexLoader.TAG, "NoSuchMethodException: makeDexElements(ArrayList,File,ArrayList) failure", new Object[0]);
                try {
                    makeDexElements = HiddenApiReflection.findMethod(dexPathList, "makeDexElements", (Class<?>[]) new Class[]{List.class, File.class, List.class});
                } catch (NoSuchMethodException e1) {
                    SplitLog.e(SplitCompatDexLoader.TAG, "NoSuchMethodException: makeDexElements(List,File,List) failure", new Object[0]);
                    throw e1;
                }
            }
            return (Object[]) makeDexElements.invoke(dexPathList, files, optimizedDirectory, suppressedExceptions);
        }
    }
}
