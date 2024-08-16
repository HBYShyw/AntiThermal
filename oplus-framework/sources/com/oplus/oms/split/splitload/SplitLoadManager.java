package com.oplus.oms.split.splitload;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitload.listener.OnSplitLoadListener;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public abstract class SplitLoadManager {
    protected static final String TAG = "SplitLoadManager";
    private final Context mContext;
    final String mCurrentProcessName;
    private final Set<Split> mLoadedSplits = Collections.newSetFromMap(new ConcurrentHashMap());
    private final int mSplitLoadMode;

    public abstract Set<SplitDexClassLoader> getAllClassLoaders();

    public abstract void getResources(Resources resources);

    public abstract void injectPathClassloader();

    public abstract void loadNow(List<Intent> list, OnSplitLoadListener onSplitLoadListener);

    public abstract void preloadInstalledSplits(Collection<String> collection);

    public abstract void unloadSplit(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitLoadManager(Context context, String currentProcessName, int splitLoadMode) {
        this.mContext = context;
        this.mCurrentProcessName = currentProcessName;
        this.mSplitLoadMode = splitLoadMode;
        SplitLog.d(TAG, "splitLoadMode " + splitLoadMode, new Object[0]);
    }

    public Set<String> getLoadedSplitNames() {
        Set<String> loadedSplitNames = new HashSet<>(0);
        for (Split split : this.mLoadedSplits) {
            loadedSplitNames.add(split.splitName);
        }
        SplitLog.d(TAG, "getLoadedSplitNames:" + loadedSplitNames, new Object[0]);
        return loadedSplitNames;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int splitLoadMode() {
        return this.mSplitLoadMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getLoadedSplitApkPaths() {
        Set<String> splitApkPaths = new HashSet<>(0);
        for (Split split : this.mLoadedSplits) {
            File file = new File(split.splitApkPath);
            if (file.exists()) {
                splitApkPaths.add(split.splitApkPath);
            } else {
                SplitLog.d(TAG, "Split has been loaded, but its file " + split.splitApkPath + " is not exist!", new Object[0]);
            }
        }
        return splitApkPaths;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void putSplits(Collection<Split> splits) {
        SplitLog.d(TAG, "putSplits " + splits, new Object[0]);
        this.mLoadedSplits.addAll(splits);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<Split> getLoadedSplits() {
        return this.mLoadedSplits;
    }

    public final Split removeLoadedSplits(String splitName) {
        Iterator<Split> it = this.mLoadedSplits.iterator();
        while (it.hasNext()) {
            Split split = it.next();
            if (TextUtils.equals(split.splitName, splitName)) {
                it.remove();
                SplitLog.d(TAG, "removeLoadedSplits " + splitName, new Object[0]);
                return split;
            }
        }
        return null;
    }

    public String getLoadedSplitVersion(String splitName) {
        for (Split split : this.mLoadedSplits) {
            if (split.splitName.equals(splitName)) {
                return split.splitVersion;
            }
        }
        return "";
    }

    public final void clear() {
        SplitLog.d(TAG, "clear ", new Object[0]);
        this.mLoadedSplits.clear();
    }

    public ClassLoader getOriginClassLoader() {
        ClassLoader classLoader = this.mContext.getClassLoader();
        if (classLoader instanceof SplitDelegateClassloader) {
            return classLoader.getParent();
        }
        return classLoader;
    }

    public ClassLoader getBootClassLoader() {
        ClassLoader classLoader = this.mContext.getClassLoader().getParent();
        if (classLoader instanceof PathClassLoader) {
            SplitLog.d(TAG, "is pathclassloader", new Object[0]);
            return classLoader.getParent();
        }
        return classLoader;
    }
}
