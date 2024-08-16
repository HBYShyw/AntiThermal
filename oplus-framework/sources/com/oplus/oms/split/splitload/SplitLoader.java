package com.oplus.oms.split.splitload;

import android.content.Context;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
abstract class SplitLoader {
    private final Context mContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitLoader(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitDexClassLoader loadCode(ClassLoader parent, String moduleNames, List<String> dexPaths, File optimizedDirectory, File librarySearchPath, List<String> dependencies) throws SplitLoadException {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadCode2(ClassLoader parent, List<String> dexPaths, File optimizedDirectory, File librarySearchPath) throws SplitLoadException {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void loadResources(String splitResDir) throws SplitLoadException {
        try {
            Context context = this.mContext;
            SplitCompatResourcesLoader.loadResources(context, context.getResources(), splitResDir);
        } catch (Throwable throwable) {
            throw new SplitLoadException(-21, throwable);
        }
    }
}
