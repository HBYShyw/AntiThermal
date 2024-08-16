package android.content.res;

import android.content.res.OplusThemeZipFile;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusAccessibleResources {
    private static Map<String, WeakReference<OplusAccessibleResources>> mPackageCaches = new ConcurrentHashMap();
    private String mPackageName;
    private IResourcesImplExt mResources;
    private OplusAccessibleFile mAccessible = null;
    private OplusMaterialFile mMaterialFile = null;
    private boolean mHasValues = false;
    private boolean mHasDrawables = false;
    private boolean mHasMaterialValues = false;

    public OplusAccessibleResources(IResourcesImplExt resources, String packageName) {
        this.mPackageName = null;
        this.mResources = null;
        this.mPackageName = packageName;
        this.mResources = resources;
        checkUpdate(false);
    }

    public static OplusAccessibleResources getAccessResources(IResourcesImplExt resources, String packageName) {
        OplusAccessibleResources mAcessResources = null;
        WeakReference<OplusAccessibleResources> weakReference = mPackageCaches.get(packageName);
        if (weakReference != null) {
            OplusAccessibleResources mAcessResources2 = weakReference.get();
            mAcessResources = mAcessResources2;
        }
        if (mAcessResources != null) {
            mAcessResources.checkUpdate(false);
            return mAcessResources;
        }
        OplusAccessibleResources mAcessResources3 = new OplusAccessibleResources(resources, packageName);
        mPackageCaches.put(packageName, new WeakReference<>(mAcessResources3));
        return mAcessResources3;
    }

    public void checkUpdate(boolean change) {
        checkAssetUpdate();
        checkColorUpdate(change);
    }

    public void setResources(IResourcesImplExt mResources) {
        this.mResources = mResources;
    }

    public boolean hasDrawables() {
        return this.mHasDrawables;
    }

    public boolean hasValues() {
        return this.mHasValues | this.mHasMaterialValues;
    }

    public boolean hasMaterialValues() {
        return this.mHasMaterialValues;
    }

    public Integer getAccessibleInt(int id) {
        Integer value = null;
        if (this.mMaterialFile != null && this.mHasMaterialValues) {
            checkMaterialValues();
            value = this.mMaterialFile.getInt(id);
        }
        if (this.mAccessible != null && this.mHasValues && value == null) {
            checkAssetValues();
            return this.mAccessible.getInt(id);
        }
        return value;
    }

    public CharSequence getAccessibleChars(int id) {
        OplusAccessibleFile oplusAccessibleFile = this.mAccessible;
        if (oplusAccessibleFile != null && this.mHasValues) {
            return oplusAccessibleFile.getCharSequence(id);
        }
        return null;
    }

    public OplusThemeZipFile.ThemeFileInfo getAccessibleStream(int index, String path) {
        OplusAccessibleFile oplusAccessibleFile = this.mAccessible;
        if (oplusAccessibleFile != null && this.mHasDrawables) {
            return oplusAccessibleFile.getAssetInputStream(index, path);
        }
        return null;
    }

    private boolean isAssetEnable() {
        OplusExtraConfiguration extrConfig = this.mResources.getSystemConfiguration().getOplusExtraConfiguration();
        return extrConfig != null && extrConfig.mAccessibleChanged > 0;
    }

    private boolean isMaterialColorEnable() {
        OplusExtraConfiguration extrConfig = this.mResources.getSystemConfiguration().getOplusExtraConfiguration();
        if (extrConfig == null) {
            return false;
        }
        long color = extrConfig.mMaterialColor;
        int userId = extrConfig.mUserId;
        StringBuilder customBuilder = new StringBuilder("data/oplus/uxres/uxcolor/");
        StringBuilder wallpaperBuilder = new StringBuilder("data/oplus/uxres/uxcolor/");
        StringBuilder companyBuilder = new StringBuilder(OplusMaterialFile.OPLUS_COMPANY_CUSTOM_XML_PATH).append("coui_theme_color_company.xml");
        if (userId > 0) {
            customBuilder.append(userId).append(File.separator).append("ux_custom_color.xml");
            wallpaperBuilder.append(userId).append(File.separator).append("coui_theme_color_wallpaper.xml");
        } else {
            customBuilder.append("ux_custom_color.xml");
            wallpaperBuilder.append("coui_theme_color_wallpaper.xml");
        }
        File custom = new File(customBuilder.toString());
        File online = new File("data/oplus/uxres/uxcolor/coui_theme_color_online.xml");
        File wallpaper = new File(wallpaperBuilder.toString());
        File company = new File(companyBuilder.toString());
        if ((color & 131072) != 131072 || !custom.exists()) {
            if ((color & 1048576) != 1048576 || !online.exists()) {
                if ((color & 262144) != 262144 || !wallpaper.exists()) {
                    if ((color & 524288) != 524288 || !company.exists()) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    public void checkAssetUpdate() {
        boolean mNightMode = OplusBaseFile.isNightMode(this.mResources);
        if (!isAssetEnable() || mNightMode) {
            OplusAccessibleFile oplusAccessibleFile = this.mAccessible;
            if (oplusAccessibleFile != null && !mNightMode) {
                oplusAccessibleFile.clearCache(null);
            }
            this.mHasDrawables = false;
            this.mHasValues = false;
            return;
        }
        OplusAccessibleFile assetFile = OplusAccessibleFile.getAssetFile(this.mPackageName, this.mResources);
        this.mAccessible = assetFile;
        if (assetFile != null) {
            this.mHasValues = true;
            this.mHasDrawables = true;
        } else {
            this.mHasValues = false;
            this.mHasDrawables = false;
        }
    }

    private void checkColorUpdate(boolean change) {
        if (!isMaterialColorEnable()) {
            OplusMaterialFile oplusMaterialFile = this.mMaterialFile;
            if (oplusMaterialFile != null) {
                oplusMaterialFile.clears();
            }
            this.mHasMaterialValues = false;
            return;
        }
        OplusMaterialFile materialFile = OplusMaterialFile.getMaterialFile(this.mPackageName, this.mResources);
        this.mMaterialFile = materialFile;
        if (materialFile != null) {
            if (materialFile.hasValues() && change) {
                this.mMaterialFile.clears();
            }
            this.mHasMaterialValues = true;
            return;
        }
        this.mHasMaterialValues = false;
    }

    private synchronized void checkMaterialValues() {
        if (!this.mMaterialFile.hasValues() || !this.mMaterialFile.isParsed()) {
            this.mMaterialFile.loadMaterialColor();
            this.mMaterialFile.setParsed(true);
        }
    }

    private void checkAssetValues() {
        if (!this.mAccessible.hasValues()) {
            this.mAccessible.initValue();
            this.mAccessible.setParsed(true);
        }
    }
}
