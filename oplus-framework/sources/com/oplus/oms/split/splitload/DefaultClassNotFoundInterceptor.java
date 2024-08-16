package com.oplus.oms.split.splitload;

import android.content.Context;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.extension.AABExtension;
import com.oplus.oms.split.splitrequest.SplitInfoManager;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
final class DefaultClassNotFoundInterceptor implements ClassNotFoundInterceptor {
    private static final String TAG = "Split:ClassNotFound";
    private final Context mContext;
    private final ClassLoader mOriginClassLoader;
    private final int mSplitLoadMode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultClassNotFoundInterceptor(Context appContext, ClassLoader originClassLoader, int splitLoadMode) {
        this.mContext = appContext;
        this.mOriginClassLoader = originClassLoader;
        this.mSplitLoadMode = splitLoadMode;
    }

    @Override // com.oplus.oms.split.splitload.ClassNotFoundInterceptor
    public Class<?> findClass(String name) {
        if (SplitLoadManagerImpl.hasInstance()) {
            int i = this.mSplitLoadMode;
            if (i == 1) {
                return onClassNotFound(name);
            }
            if (i == 2) {
                return onClassNotFound2(name);
            }
            return null;
        }
        return null;
    }

    private Class<?> onClassNotFound(String name) {
        Class<?> ret = findClassInSplits(name);
        if (ret != null) {
            return ret;
        }
        Class<?> fakeComponent = AABExtension.getInstance().getFakeComponent(name);
        if (fakeComponent != null || isSplitEntryFragments(name)) {
            String splitName = AABExtension.getInstance().getSplitNameForComponent(name);
            List<String> splitNames = new ArrayList<>();
            splitNames.add(splitName);
            SplitLoadManagerImpl.getInstance().preloadInstalledSplits(splitNames);
            Class<?> ret2 = findClassInSplits(name);
            if (ret2 != null) {
                SplitLog.d(TAG, "Class " + name + " is found in Split after loading " + splitName + " installed splits.", new Object[0]);
                return ret2;
            }
            if (fakeComponent != null) {
                SplitLog.w(TAG, "Split component " + name + " is still not found after installed " + splitName + " splits, return a " + fakeComponent.getSimpleName() + " to avoid crash", new Object[0]);
                return fakeComponent;
            }
            return null;
        }
        return null;
    }

    private boolean isSplitEntryFragments(String name) {
        List<String> splitEntryFragments;
        SplitInfoManager infoManager = SplitInfoManagerImpl.getInstance();
        if (infoManager != null && (splitEntryFragments = infoManager.getSplitEntryFragments(this.mContext)) != null && !splitEntryFragments.isEmpty()) {
            return splitEntryFragments.contains(name);
        }
        return false;
    }

    private Class<?> onClassNotFound2(String name) {
        Class<?> fakeComponent = AABExtension.getInstance().getFakeComponent(name);
        if (fakeComponent != null || isSplitEntryFragments(name)) {
            String splitName = AABExtension.getInstance().getSplitNameForComponent(name);
            List<String> splitNames = new ArrayList<>();
            splitNames.add(splitName);
            SplitLoadManagerImpl.getInstance().preloadInstalledSplits(splitNames);
            try {
                return this.mOriginClassLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                if (fakeComponent != null) {
                    SplitLog.w(TAG, "Split2 component " + name + " is still not found after installing " + splitName + " installed splits,return a " + fakeComponent.getSimpleName() + " to avoid crash", new Object[0]);
                    return fakeComponent;
                }
                return null;
            }
        }
        return null;
    }

    private Class<?> findClassInSplits(String name) {
        Set<SplitDexClassLoader> splitDexClassLoaders = SplitApplicationLoaders.getInstance().getValidClassLoaders();
        for (SplitDexClassLoader classLoader : splitDexClassLoaders) {
            try {
                Class<?> clazz = classLoader.loadClassItself(name);
                SplitLog.d(TAG, "Class " + name + " is found in " + classLoader.moduleName() + " ClassLoader", new Object[0]);
                return clazz;
            } catch (ClassNotFoundException e) {
                SplitLog.w(TAG, "Class " + name + " is not found in " + classLoader.moduleName() + " ClassLoader", new Object[0]);
            }
        }
        return null;
    }
}
