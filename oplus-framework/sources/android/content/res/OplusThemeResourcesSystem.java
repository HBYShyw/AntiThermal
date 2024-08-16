package android.content.res;

import android.content.res.OplusThemeResources;
import android.content.res.OplusThemeZipFile;
import java.io.File;

/* loaded from: classes.dex */
public class OplusThemeResourcesSystem extends OplusThemeResources {
    private static final String TAG = "OplusThemeResourcesSystem";
    private static OplusThemeResources sOplus;
    private final boolean DEBUG;

    public OplusThemeResourcesSystem(OplusThemeResourcesSystem themeResourcesSystem, IResourcesImplExt resources, OplusThemeResources.MetaData metaData) {
        super(themeResourcesSystem, resources, OplusThemeResources.FRAMEWORK_NAME, metaData);
        this.DEBUG = true;
    }

    public static OplusThemeResourcesSystem getTopLevelThemeResources(IResourcesImplExt resources) {
        sOplus = OplusThemeResources.getTopLevelThemeResources(resources, OplusThemeResources.OPLUS_NAME);
        OplusThemeResourcesSystem themeresourcessystem = null;
        setThemePath(resources, OplusThemeResources.FRAMEWORK_NAME);
        for (int i = 0; i < sThemeMetaPath.length; i++) {
            OplusThemeResourcesSystem res = new OplusThemeResourcesSystem(themeresourcessystem, resources, sThemeMetaPath[i]);
            themeresourcessystem = res;
        }
        return themeresourcessystem;
    }

    @Override // android.content.res.OplusThemeResources
    public boolean checkUpdate() {
        sOplus.checkUpdate();
        return super.checkUpdate();
    }

    @Override // android.content.res.OplusThemeResources
    public CharSequence getThemeCharSequence(int id) {
        CharSequence res = null;
        OplusThemeResources oplusThemeResources = sOplus;
        if (oplusThemeResources != null) {
            res = oplusThemeResources.getThemeCharSequence(id);
        }
        if (res == null) {
            CharSequence res2 = getThemeCharSequenceInner(id);
            return res2;
        }
        return res;
    }

    private OplusThemeZipFile.ThemeFileInfo getThemeFileStreamSystem(String path, String subPath) {
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = getThemeFileStreamInner(path);
        return themeFileInfo;
    }

    private OplusThemeZipFile.ThemeFileInfo getThemeFileStreamOplus(String path, String subPath) {
        OplusThemeResources oplusThemeResources = sOplus;
        if (oplusThemeResources == null) {
            return null;
        }
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = oplusThemeResources.getThemeFileStream(path);
        return themeFileInfo;
    }

    @Override // android.content.res.OplusThemeResources
    public OplusThemeZipFile.ThemeFileInfo getThemeFileStream(int index, String path) {
        if (path == null) {
            return null;
        }
        String res = path.substring(path.lastIndexOf(47) + 1);
        if (2 == index) {
            OplusThemeZipFile.ThemeFileInfo themeFileInfo = getThemeFileStreamOplus(path, res);
            return themeFileInfo;
        }
        OplusThemeZipFile.ThemeFileInfo themeFileInfo2 = getThemeFileStreamSystem(path, res);
        return themeFileInfo2;
    }

    public Integer getThemeInt(int id, int index) {
        OplusThemeResources oplusThemeResources;
        Integer res = null;
        if (index == 2 && (oplusThemeResources = sOplus) != null) {
            res = oplusThemeResources.getThemeInt(id);
        }
        if (res == null) {
            return getThemeIntInner(id);
        }
        return res;
    }

    @Override // android.content.res.OplusThemeResources
    public boolean hasValues() {
        if (super.hasValues() || sOplus.hasValues()) {
            return true;
        }
        return false;
    }

    @Override // android.content.res.OplusThemeResources
    public boolean hasDrawables() {
        return super.hasDrawables() || sOplus.hasDrawables();
    }

    public File getLockscreenWallpaper() {
        return null;
    }

    @Override // android.content.res.OplusThemeResources
    public void setResource(IResourcesImplExt res) {
        sOplus.setResource(res);
        super.setResource(res);
    }
}
