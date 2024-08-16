package com.oplus.oms.split.splitload;

import android.text.TextUtils;
import com.oplus.oms.split.common.ReflectUtil;
import com.oplus.oms.split.common.SplitLog;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SplitDexClassLoader extends BaseDexClassLoader {
    private static final String SPLIT_INSTALL = "SplitInstallHelper";
    private static final String TAG = "SplitDexClassLoader";
    private final Set<SplitDexClassLoader> mDependenciesLoaders;
    private final String mModuleName;
    private ClassLoader mOrignClassLoader;
    private boolean mValid;

    private SplitDexClassLoader(String moduleName, List<String> dexPaths, File optimizedDirectory, String librarySearchPath, List<String> dependencies, ClassLoader parent) {
        super(dexPaths == null ? "" : TextUtils.join(File.pathSeparator, dexPaths), optimizedDirectory, librarySearchPath, parent);
        SplitLog.d(TAG, "dexPaths:" + dexPaths + ",optimizedDirectory:" + optimizedDirectory + ",librarySearchPath:" + librarySearchPath, new Object[0]);
        this.mModuleName = moduleName;
        this.mDependenciesLoaders = SplitApplicationLoaders.getInstance().getValidClassLoaders(dependencies);
        this.mOrignClassLoader = ReflectUtil.getAppClassLoader();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SplitDexClassLoader create(ClassLoader parent, String moduleName, List<String> dexPaths, File optimizedDirectory, File librarySearchFile, List<String> dependencies) {
        long time = System.currentTimeMillis();
        SplitDexClassLoader cl = new SplitDexClassLoader(moduleName, dexPaths, optimizedDirectory, librarySearchFile == null ? null : librarySearchFile.getAbsolutePath(), dependencies, parent);
        SplitLog.d(TAG, "Cost " + (System.currentTimeMillis() - time) + " ms to load " + moduleName + " code", new Object[0]);
        return cl;
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        int i = 0;
        if (!TextUtils.isEmpty(name) && name.contains(SPLIT_INSTALL) && this.mOrignClassLoader != null) {
            SplitLog.d(TAG, "find class from orignClassLoader - name = " + name, new Object[0]);
            try {
                return this.mOrignClassLoader.loadClass(name);
            } catch (ClassNotFoundException error) {
                throw error;
            }
        }
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e1) {
            if (this.mDependenciesLoaders != null) {
                for (SplitDexClassLoader loader : this.mDependenciesLoaders) {
                    try {
                        return loader.loadClassItself(name);
                    } catch (ClassNotFoundException e) {
                        SplitLog.w(TAG, "SplitDexClassLoader: Class " + name + " is not found in " + loader.moduleName() + " ClassLoader", new Object[i]);
                    }
                }
            }
            throw e1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setValid(boolean valid) {
        this.mValid = valid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValid() {
        return this.mValid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String moduleName() {
        return this.mModuleName;
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    public String findLibrary(String name) {
        Set<SplitDexClassLoader> set;
        SplitLog.d(TAG, "findLibrary " + name, new Object[0]);
        String libName = super.findLibrary(name);
        if (libName == null && (set = this.mDependenciesLoaders) != null) {
            for (SplitDexClassLoader loader : set) {
                libName = loader.findLibrary(name);
                if (libName != null) {
                    break;
                }
            }
        }
        if (libName == null && (getParent() instanceof BaseDexClassLoader)) {
            return ((BaseDexClassLoader) getParent()).findLibrary(name);
        }
        return libName;
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    protected Enumeration<URL> findResources(String name) {
        Set<SplitDexClassLoader> set;
        Enumeration<URL> resources = super.findResources(name);
        if (resources == null && (set = this.mDependenciesLoaders) != null) {
            for (SplitDexClassLoader loader : set) {
                resources = loader.findResourcesItself(name);
                if (resources != null) {
                    break;
                }
            }
        }
        return resources;
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    protected URL findResource(String name) {
        Set<SplitDexClassLoader> set;
        URL resource = super.findResource(name);
        if (resource == null && (set = this.mDependenciesLoaders) != null) {
            for (SplitDexClassLoader loader : set) {
                resource = loader.findResourceItself(name);
                if (resource != null) {
                    break;
                }
            }
        }
        return resource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public URL findResourceItself(String name) {
        return super.findResource(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Enumeration<URL> findResourcesItself(String name) {
        return super.findResources(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String findLibraryItself(String name) {
        return super.findLibrary(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class<?> loadClassItself(String name) throws ClassNotFoundException {
        Class<?> cl = findLoadedClass(name);
        if (cl != null) {
            return cl;
        }
        return super.findClass(name);
    }

    @Override // dalvik.system.BaseDexClassLoader
    public String toString() {
        return "SplitDexClassLoader(0x" + Integer.toHexString(hashCode()) + "){moduleName='" + this.mModuleName + "', valid=" + this.mValid + ", dependenciesLoaders=" + this.mDependenciesLoaders + '}';
    }
}
