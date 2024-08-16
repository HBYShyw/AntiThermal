package com.oplus.oms.split.splitload;

import android.content.Intent;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.listener.OnSplitLoadListener;
import java.io.File;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SplitSingleModeLoadTaskImpl extends SplitLoadTask {
    private static final String TAG = "SplitSingleModeLoadTaskImpl";

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitSingleModeLoadTaskImpl(SplitLoadManager loadManager, List<Intent> splitFileIntents, OnSplitLoadListener loadListener) {
        super(loadManager, splitFileIntents, loadListener);
    }

    @Override // com.oplus.oms.split.splitload.SplitLoaderWrapper
    public SplitLoader createSplitLoader() {
        return new SplitSingleModeLoaderImpl(getContext());
    }

    @Override // com.oplus.oms.split.splitload.SplitLoaderWrapper
    public ClassLoader loadCode(ClassLoader parent, String splitName, List<String> addedDexPaths, File optimizedDirectory, File librarySearchPath, List<String> dependencies) throws SplitLoadException {
        getSplitLoader().loadCode2(parent, addedDexPaths, optimizedDirectory, librarySearchPath);
        return parent;
    }

    @Override // com.oplus.oms.split.splitload.SplitLoaderWrapper
    public void unloadCode(ClassLoader classLoader) {
        try {
            SplitCompatDexLoader.unLoad(classLoader);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            SplitLog.w(TAG, "unloadCode error " + e.getMessage(), new Object[0]);
        }
    }
}
