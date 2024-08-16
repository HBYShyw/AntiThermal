package com.oplus.oms.split.splitload;

import android.text.TextUtils;
import com.oplus.oms.split.common.SplitLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class SplitApplicationLoaders {
    private static final String TAG = "SplitApplicationLoaders";
    private static final AtomicReference<SplitApplicationLoaders> sInstance = new AtomicReference<>();
    private final Set<SplitDexClassLoader> mSplitDexClassLoaders = Collections.newSetFromMap(new ConcurrentHashMap());
    private final List<WeakReference<SplitDexClassLoader>> mGCQueue = new ArrayList();

    public static SplitApplicationLoaders getInstance() {
        AtomicReference<SplitApplicationLoaders> atomicReference = sInstance;
        if (atomicReference.get() == null) {
            atomicReference.set(new SplitApplicationLoaders());
        }
        return atomicReference.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addClassLoader(String splitName, SplitDexClassLoader classLoader) {
        SplitLog.d(TAG, "new ClassLoader: %s[0x%d]", splitName, Integer.valueOf(classLoader.hashCode()));
        this.mSplitDexClassLoaders.add(classLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<SplitDexClassLoader> getValidClassLoaders() {
        Set<SplitDexClassLoader> validClassLoaders = new HashSet<>(this.mSplitDexClassLoaders.size());
        for (SplitDexClassLoader classLoader : this.mSplitDexClassLoaders) {
            if (classLoader.isValid()) {
                validClassLoaders.add(classLoader);
            }
        }
        SplitLog.d(TAG, "getValidClassLoaders:" + this.mSplitDexClassLoaders, new Object[0]);
        return validClassLoaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<SplitDexClassLoader> getValidClassLoaders(List<String> moduleNames) {
        if (moduleNames == null) {
            return null;
        }
        Set<SplitDexClassLoader> loaders = new HashSet<>(moduleNames.size());
        for (SplitDexClassLoader classLoader : this.mSplitDexClassLoaders) {
            if (moduleNames.contains(classLoader.moduleName()) && classLoader.isValid()) {
                loaders.add(classLoader);
            }
        }
        SplitLog.d(TAG, "getValidClassLoaders:" + this.mSplitDexClassLoaders, new Object[0]);
        return loaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitDexClassLoader getValidClassLoader(String moduleName) {
        for (SplitDexClassLoader classLoader : this.mSplitDexClassLoaders) {
            if (classLoader.moduleName().equals(moduleName) && classLoader.isValid()) {
                return classLoader;
            }
        }
        return null;
    }

    public SplitDexClassLoader getClassLoader(String moduleName) {
        for (SplitDexClassLoader classLoader : this.mSplitDexClassLoaders) {
            if (classLoader.moduleName().equals(moduleName)) {
                return classLoader;
            }
        }
        Iterator<WeakReference<SplitDexClassLoader>> it = this.mGCQueue.iterator();
        while (it.hasNext()) {
            WeakReference<SplitDexClassLoader> wr = it.next();
            if (wr == null) {
                it.remove();
            } else {
                SplitDexClassLoader loader = wr.get();
                if (loader == null) {
                    it.remove();
                } else if (TextUtils.equals(loader.moduleName(), moduleName)) {
                    it.remove();
                    this.mSplitDexClassLoaders.add(loader);
                    SplitLog.d(TAG, "use recycle cl:%s[%d] ", moduleName, Integer.valueOf(loader.hashCode()));
                    return loader;
                }
            }
        }
        return null;
    }

    public void removeClassLoader(String moduleName) {
        if (TextUtils.isEmpty(moduleName)) {
            return;
        }
        SplitDexClassLoader loader = null;
        Iterator<SplitDexClassLoader> it = this.mSplitDexClassLoaders.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SplitDexClassLoader classLoader = it.next();
            if (moduleName.equals(classLoader.moduleName())) {
                loader = classLoader;
                break;
            }
        }
        if (loader == null) {
            return;
        }
        this.mSplitDexClassLoaders.remove(loader);
        this.mGCQueue.add(new WeakReference<>(loader));
        SplitLog.d(TAG, "removeClassLoader: %s[0x%d]", moduleName, Integer.valueOf(loader.hashCode()));
    }

    public Set<SplitDexClassLoader> getAllClassLoaders() {
        SplitLog.d(TAG, "getAllClassLoaders:" + this.mSplitDexClassLoaders, new Object[0]);
        return this.mSplitDexClassLoaders;
    }
}
