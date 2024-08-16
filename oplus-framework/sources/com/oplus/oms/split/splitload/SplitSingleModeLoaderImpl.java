package com.oplus.oms.split.splitload;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
final class SplitSingleModeLoaderImpl extends SplitLoader {
    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitSingleModeLoaderImpl(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.oplus.oms.split.splitload.SplitLoader
    public void loadCode2(ClassLoader parent, List<String> dexPaths, File optimizedDirectory, File librarySearchPath) throws SplitLoadException {
        loadLibrary(parent, librarySearchPath);
        loadDex(parent, dexPaths, optimizedDirectory);
    }

    private void loadLibrary(ClassLoader classLoader, File librarySearchPath) throws SplitLoadException {
        if (librarySearchPath != null) {
            try {
                SplitCompatLibraryLoader.load(classLoader, librarySearchPath);
            } catch (Throwable cause) {
                throw new SplitLoadException(-22, cause);
            }
        }
    }

    private void loadDex(ClassLoader classLoader, List<String> dexPaths, File optimizedDirectory) throws SplitLoadException {
        if (dexPaths != null) {
            List<File> dexFiles = new ArrayList<>(dexPaths.size());
            for (String dexPath : dexPaths) {
                dexFiles.add(new File(dexPath));
            }
            try {
                SplitCompatDexLoader.load(classLoader, optimizedDirectory, dexFiles);
            } catch (Throwable cause) {
                throw new SplitLoadException(-23, cause);
            }
        }
    }
}
