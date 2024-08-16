package com.oplus.oms.split.splitload;

import android.content.Context;
import com.oplus.oms.split.common.SplitLog;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

/* loaded from: classes.dex */
final class SplitDelegateClassloader extends PathClassLoader {
    private static final String TAG = "SplitDelegateClassloader";
    private ClassNotFoundInterceptor mClassNotFoundInterceptor;
    private BaseDexClassLoader mOriginClassLoader;

    SplitDelegateClassloader(ClassLoader parent) {
        super("", parent);
        this.mOriginClassLoader = (BaseDexClassLoader) parent;
    }

    private static void reflectPackageInfoClassloader(Context baseContext, ClassLoader reflectClassLoader) throws NoSuchFieldException, IllegalAccessException {
        Object packageInfo = HiddenApiReflection.findField(baseContext, "mPackageInfo").get(baseContext);
        if (packageInfo != null) {
            HiddenApiReflection.findField(packageInfo, "mClassLoader").set(packageInfo, reflectClassLoader);
        }
        HiddenApiReflection.findField(baseContext, "mClassLoader").set(baseContext, reflectClassLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void inject(ClassLoader originalClassloader, Context baseContext) throws NoSuchFieldException, IllegalAccessException {
        SplitDelegateClassloader classloader = new SplitDelegateClassloader(originalClassloader);
        reflectPackageInfoClassloader(baseContext, classloader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setClassNotFoundInterceptor(ClassNotFoundInterceptor classNotFoundInterceptor) {
        this.mClassNotFoundInterceptor = classNotFoundInterceptor;
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result;
        try {
            return this.mOriginClassLoader.loadClass(name);
        } catch (ClassNotFoundException error) {
            ClassNotFoundInterceptor classNotFoundInterceptor = this.mClassNotFoundInterceptor;
            if (classNotFoundInterceptor != null && (result = classNotFoundInterceptor.findClass(name)) != null) {
                return result;
            }
            throw error;
        }
    }

    @Override // java.lang.ClassLoader
    public Enumeration<URL> getResources(String name) throws IOException {
        return this.mOriginClassLoader.getResources(name);
    }

    @Override // java.lang.ClassLoader
    public URL getResource(String name) {
        return this.mOriginClassLoader.getResource(name);
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    protected URL findResource(String name) {
        URL resource = super.findResource(name);
        if (resource == null) {
            Set<SplitDexClassLoader> splitDexClassLoaders = SplitApplicationLoaders.getInstance().getValidClassLoaders();
            for (SplitDexClassLoader loader : splitDexClassLoaders) {
                resource = loader.findResourceItself(name);
                if (resource != null) {
                    break;
                }
            }
        }
        return resource;
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    protected Enumeration<URL> findResources(String name) {
        Enumeration<URL> resources = super.findResources(name);
        if (resources == null) {
            Set<SplitDexClassLoader> splitDexClassLoaders = SplitApplicationLoaders.getInstance().getValidClassLoaders();
            for (SplitDexClassLoader loader : splitDexClassLoaders) {
                resources = loader.findResourcesItself(name);
                if (resources != null) {
                    break;
                }
            }
        }
        return resources;
    }

    @Override // java.lang.ClassLoader
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override // dalvik.system.BaseDexClassLoader, java.lang.ClassLoader
    public String findLibrary(String name) {
        SplitLog.d(TAG, "findLibrary0 " + name, new Object[0]);
        String libName = this.mOriginClassLoader.findLibrary(name);
        if (libName == null) {
            Set<SplitDexClassLoader> splitDexClassLoaders = SplitApplicationLoaders.getInstance().getValidClassLoaders();
            for (SplitDexClassLoader classLoader : splitDexClassLoaders) {
                libName = classLoader.findLibraryItself(name);
                if (libName != null) {
                    break;
                }
            }
        }
        return libName;
    }
}
