package com.oplus.oms.split.splitload;

import android.content.Intent;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.listener.OnSplitLoadListener;
import java.io.File;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SplitMultipleModeLoadTaskImpl extends SplitLoadTask {
    private static final String TAG = "SplitMultipleModeLoadTaskImpl";

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitMultipleModeLoadTaskImpl(SplitLoadManager loadManager, List<Intent> splitFileIntents, OnSplitLoadListener loadListener) {
        super(loadManager, splitFileIntents, loadListener);
    }

    @Override // com.oplus.oms.split.splitload.SplitLoaderWrapper
    public SplitLoader createSplitLoader() {
        return new SplitMultipleModeLoaderImpl(getContext());
    }

    @Override // com.oplus.oms.split.splitload.SplitLoaderWrapper
    public ClassLoader loadCode(ClassLoader parent, String splitName, List<String> addedDexPaths, File optimizedDirectory, File librarySearchPath, List<String> dependencies) throws SplitLoadException {
        SplitDexClassLoader classLoader = SplitApplicationLoaders.getInstance().getClassLoader(splitName);
        SplitLog.d(TAG, "loadCode:" + splitName, new Object[0]);
        if (classLoader == null) {
            SplitDexClassLoader classLoader2 = getSplitLoader().loadCode(parent, splitName, addedDexPaths, optimizedDirectory, librarySearchPath, dependencies);
            classLoader2.setValid(true);
            SplitApplicationLoaders.getInstance().addClassLoader(splitName, classLoader2);
            return classLoader2;
        }
        return classLoader;
    }

    @Override // com.oplus.oms.split.splitload.SplitLoaderWrapper
    public void unloadCode(ClassLoader classLoader) {
        if (classLoader instanceof SplitDexClassLoader) {
            SplitApplicationLoaders.getInstance().removeClassLoader(((SplitDexClassLoader) classLoader).moduleName());
        }
    }
}
