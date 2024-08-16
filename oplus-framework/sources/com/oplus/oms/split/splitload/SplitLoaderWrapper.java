package com.oplus.oms.split.splitload;

import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
interface SplitLoaderWrapper {
    SplitLoader createSplitLoader();

    ClassLoader loadCode(ClassLoader classLoader, String str, List<String> list, File file, File file2, List<String> list2) throws SplitLoadException;

    void loadResources(String str) throws SplitLoadException;

    void unloadCode(ClassLoader classLoader);
}
