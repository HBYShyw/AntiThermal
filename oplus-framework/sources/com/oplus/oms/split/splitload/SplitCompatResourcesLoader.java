package com.oplus.oms.split.splitload;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.compat.SplitResourcesLoader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class SplitCompatResourcesLoader {
    private static final String TAG = "SplitCompatResourcesLoader";
    private static final SplitResourcesLoader sResourcesLoader = getSplitResourcesLoader();

    private SplitCompatResourcesLoader() {
    }

    public static void loadResources(Context context, Resources resources) throws SplitCompatResourcesException {
        sResourcesLoader.loadResources(context, resources);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void loadResources(Context context, Resources preResources, String splitApkPath) throws SplitCompatResourcesException {
        sResourcesLoader.loadResources(context, preResources, splitApkPath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class V21 extends VersionCompat {
        private V21() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void installSplitResDirs(Resources preResources, List<String> splitResPaths) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method method = VersionCompat.getAddAssetPathMethod();
            for (String splitResPath : splitResPaths) {
                method.invoke(preResources.getAssets(), splitResPath);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class VersionCompat {
        private static Method getCookieNameMethod;
        private static Method mAddAssetPathMethod;
        private static Method mGetApkAssetsMethod;
        private static Method mGetAssetPathMethod;
        private static Field mStringBlocksField;

        private VersionCompat() {
        }

        static Method getAddAssetPathMethod() throws NoSuchMethodException {
            if (mAddAssetPathMethod == null) {
                mAddAssetPathMethod = HiddenApiReflection.findMethod((Class<?>) AssetManager.class, "addAssetPath", (Class<?>[]) new Class[]{String.class});
            }
            return mAddAssetPathMethod;
        }

        static Method getGetAssetPathMethod() throws ClassNotFoundException, NoSuchMethodException {
            if (mGetAssetPathMethod == null) {
                Class<?> clazz = Class.forName("android.content.res.ApkAssets");
                mGetAssetPathMethod = HiddenApiReflection.findMethod(clazz, "getAssetPath", (Class<?>[]) new Class[0]);
            }
            return mGetAssetPathMethod;
        }

        static Method getGetApkAssetsMethod() throws NoSuchMethodException {
            if (mGetApkAssetsMethod == null) {
                mGetApkAssetsMethod = HiddenApiReflection.findMethod((Class<?>) AssetManager.class, "getApkAssets", (Class<?>[]) new Class[0]);
            }
            return mGetApkAssetsMethod;
        }

        static Field mStringBlocksInAssetManager() throws NoSuchFieldException {
            if (mStringBlocksField == null) {
                mStringBlocksField = HiddenApiReflection.findField((Class<?>) AssetManager.class, "mStringBlocks");
            }
            return mStringBlocksField;
        }

        static Method getGetCookieNameMethod() throws NoSuchMethodException {
            if (getCookieNameMethod == null) {
                getCookieNameMethod = HiddenApiReflection.findMethod((Class<?>) AssetManager.class, "getCookieName", (Class<?>[]) new Class[]{Integer.TYPE});
            }
            return getCookieNameMethod;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DefaultSplitResourcesLoader implements SplitResourcesLoader {
        private DefaultSplitResourcesLoader() {
        }

        @Override // com.oplus.oms.split.splitload.compat.SplitResourcesLoader
        public void loadResources(Context context, Resources resources) throws SplitCompatResourcesException {
            checkOrUpdateResources(context, resources);
        }

        @Override // com.oplus.oms.split.splitload.compat.SplitResourcesLoader
        public void loadResources(Context context, Resources preResources, String splitApkPath) throws SplitCompatResourcesException {
            try {
                List<String> loadedResDirs = getLoadedResourcesDirs(preResources.getAssets());
                if (!loadedResDirs.contains(splitApkPath)) {
                    try {
                        installSplitResDirs(preResources, Collections.singletonList(splitApkPath));
                        SplitLog.d(SplitCompatResourcesLoader.TAG, "Install split " + splitApkPath + " resources for application.", new Object[0]);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                        throw new SplitCompatResourcesException("installSplitResDirs error " + context.getClass().getName(), exception);
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
                throw new SplitCompatResourcesException("getLoadedResourcesDirs error " + context.getClass().getName(), e);
            }
        }

        private static void checkOrUpdateResources(Context context, Resources resources) throws SplitCompatResourcesException {
            try {
                List<String> loadedResDirsInAsset = getLoadedResourcesDirs(resources.getAssets());
                Collection<String> loadedSplitPaths = getLoadedSplitPaths();
                if (loadedSplitPaths != null && !loadedSplitPaths.isEmpty() && !loadedResDirsInAsset.containsAll(loadedSplitPaths)) {
                    List<String> unloadedSplitPaths = new ArrayList<>();
                    for (String splitPath : loadedSplitPaths) {
                        if (!loadedResDirsInAsset.contains(splitPath)) {
                            unloadedSplitPaths.add(splitPath);
                        }
                    }
                    try {
                        installSplitResDirs(resources, unloadedSplitPaths);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
                        throw new SplitCompatResourcesException("Failed to install resources " + unloadedSplitPaths + " for " + context.getClass().getName(), exception);
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
                throw new SplitCompatResourcesException("Failed to get all loaded split resources for " + context.getClass().getName(), e);
            }
        }

        private static Collection<String> getLoadedSplitPaths() {
            SplitLoadManager loadManager = SplitLoadManagerImpl.getInstance();
            if (loadManager != null) {
                return loadManager.getLoadedSplitApkPaths();
            }
            return Collections.emptyList();
        }

        private static void installSplitResDirs(Resources resources, List<String> splitResPaths) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            V21.installSplitResDirs(resources, splitResPaths);
        }

        private static List<String> getLoadedResourcesDirs(AssetManager asset) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException, NoSuchFieldException {
            List<String> existedAppResDirList = new ArrayList<>();
            Object[] apkAssets = (Object[]) VersionCompat.getGetApkAssetsMethod().invoke(asset, new Object[0]);
            if (apkAssets != null) {
                for (Object apkAsset : apkAssets) {
                    String path = (String) VersionCompat.getGetAssetPathMethod().invoke(apkAsset, new Object[0]);
                    existedAppResDirList.add(path);
                }
            }
            return existedAppResDirList;
        }
    }

    private static SplitResourcesLoader getSplitResourcesLoader() {
        return new DefaultSplitResourcesLoader();
    }
}
