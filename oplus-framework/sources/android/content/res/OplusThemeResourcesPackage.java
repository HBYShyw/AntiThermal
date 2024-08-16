package android.content.res;

import android.content.res.OplusThemeResources;
import android.content.res.OplusThemeZipFile;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusThemeResourcesPackage extends OplusThemeResources {
    private static final String TAG = "OplusThemeResourcesPackage";
    private static final Map<String, WeakReference<OplusThemeResourcesPackage>> sPackageResources = new HashMap();
    private final boolean DEBUG;

    public OplusThemeResourcesPackage(OplusThemeResourcesPackage themeResourcesPackage, IResourcesImplExt resources, String packageName, OplusThemeResources.MetaData metaData) {
        super(themeResourcesPackage, resources, packageName, metaData);
        this.DEBUG = true;
    }

    public static OplusThemeResourcesPackage getThemeResources(IResourcesImplExt resources, String packageName) {
        OplusThemeResourcesPackage themeResourcesPackage = null;
        Map<String, WeakReference<OplusThemeResourcesPackage>> map = sPackageResources;
        synchronized (map) {
            if (map.containsKey(packageName)) {
                themeResourcesPackage = map.get(packageName).get();
            }
            if (themeResourcesPackage == null) {
                themeResourcesPackage = getTopLevelThemeResources(resources, packageName);
                map.put(packageName, new WeakReference<>(themeResourcesPackage));
            } else if (!themeResourcesPackage.checkThemePackageExit()) {
                themeResourcesPackage.checkUpdate();
                map.remove(packageName);
            }
        }
        return themeResourcesPackage;
    }

    public static OplusThemeResourcesPackage getTopLevelThemeResources(IResourcesImplExt resources, String pathName) {
        setThemePath(resources, pathName);
        OplusThemeResourcesPackage themeResourcesPackage = null;
        for (int i = 0; i < sThemeMetaPath.length; i++) {
            themeResourcesPackage = new OplusThemeResourcesPackage(themeResourcesPackage, resources, pathName, sThemeMetaPath[i]);
        }
        return themeResourcesPackage;
    }

    @Override // android.content.res.OplusThemeResources
    public CharSequence getThemeCharSequence(int id) {
        CharSequence res = super.getThemeCharSequence(id);
        if (res == null && getSystem() != null) {
            return getSystem().getThemeCharSequence(id);
        }
        return res;
    }

    @Override // android.content.res.OplusThemeResources
    public OplusThemeZipFile.ThemeFileInfo getThemeFileStream(int index, String path) {
        return getPackageThemeFileStream(index, path);
    }

    @Override // android.content.res.OplusThemeResources
    protected boolean isMutiPackage() {
        return true;
    }

    @Override // android.content.res.OplusThemeResources
    public void setResource(IResourcesImplExt res) {
        super.setResource(res);
    }
}
