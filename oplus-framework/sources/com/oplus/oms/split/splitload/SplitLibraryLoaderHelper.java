package com.oplus.oms.split.splitload;

import android.app.Application;
import android.content.Context;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInfoManager;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import com.oplus.oms.split.splitrequest.SplitInstallUtil;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SplitLibraryLoaderHelper {
    private static final String TAG = "SplitLibraryLoaderHelper";

    private SplitLibraryLoaderHelper() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x0046, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean loadSplitLibrary(Context context, String libraryName) {
        if (!SplitLoadManagerImpl.hasInstance()) {
            SplitLog.w(TAG, "hasInstance null", new Object[0]);
            return false;
        }
        if (SplitLoadManagerImpl.getInstance().splitLoadMode() != 1) {
            SplitLog.w(TAG, "splitLoadMode not MULTIPLE_CLASSLOADER", new Object[0]);
            return false;
        }
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        if (manager == null) {
            SplitLog.w(TAG, "manager null", new Object[0]);
            return false;
        }
        Collection<SplitInfo> splits = manager.getAllSplitInfo(context);
        if (splits == null) {
            SplitLog.w(TAG, "splits null", new Object[0]);
            return false;
        }
        for (SplitInfo info : splits) {
            SplitInfo.LibData libData = null;
            try {
                libData = info.getPrimaryLibData(context);
            } catch (IOException e) {
                SplitLog.e(TAG, "loadSplitLibrary error", new Object[0]);
            }
            if (libData == null) {
                SplitLog.w(TAG, "splits libData null", new Object[0]);
            } else {
                String splitName = info.getSplitName();
                List<SplitInfo.LibData.Lib> libs = libData.getLibs();
                Iterator<SplitInfo.LibData.Lib> it = libs.iterator();
                while (true) {
                    if (it.hasNext()) {
                        SplitInfo.LibData.Lib lib = it.next();
                        SplitLog.d(TAG, "splits libData getName:" + lib.getName() + ",libraryName:" + libraryName, new Object[0]);
                        if (lib.getName().equals(System.mapLibraryName(libraryName))) {
                            if (context instanceof Application) {
                                int splitVersion = SplitInstallUtil.getSplitInstallVersion(context, splitName);
                                String libPath = SplitPathManager.require().getSplitLibDir(splitName, libData.getAbi(), splitVersion).getAbsolutePath() + File.separator + lib.getName();
                                SplitLog.d(TAG, "splits libData libPath:" + libPath, new Object[0]);
                                try {
                                    System.load(libPath);
                                    return true;
                                } catch (UnsatisfiedLinkError error) {
                                    SplitLog.w(TAG, "splits libData error:" + error.getMessage(), new Object[0]);
                                    return false;
                                }
                            }
                            SplitDexClassLoader classLoader = SplitApplicationLoaders.getInstance().getValidClassLoader(info.getSplitName());
                            SplitLog.d(TAG, "splits libData classLoader:" + classLoader + ",info.getSplitName:" + info.getSplitName(), new Object[0]);
                            if (classLoader != null) {
                                return loadSplitLibrary0(classLoader, info.getSplitName(), libraryName);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean loadSplitLibrary0(ClassLoader classLoader, String splitName, String name) {
        try {
            Class<?> splitLoaderCl = classLoader.loadClass("com.oplus.oms.split.core.splitlib." + splitName + "SplitLibraryLoader");
            Object splitLoader = splitLoaderCl.getDeclaredConstructor(null).newInstance(new Object[0]);
            Method method = HiddenApiReflection.findMethod(splitLoaderCl, "loadSplitLibrary", (Class<?>[]) new Class[]{String.class});
            method.invoke(splitLoader, name);
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException exception) {
            SplitLog.e(TAG, "loadSplitLibrary0 error", exception);
            return false;
        }
    }
}
