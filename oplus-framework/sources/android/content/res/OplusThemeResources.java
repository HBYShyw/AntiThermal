package android.content.res;

import android.content.res.OplusThemeZipFile;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.oplus.theme.OplusThemeUtil;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusThemeResources {
    public static final String CUSTOM_THEME_PATH;
    public static final int CUSTON_FLAG = 256;
    public static final String DEFAULT_CUSTOM_PROP = SystemProperties.get(OplusThemeUtil.CUSTOM_THEME_PATH_DEFAULT);
    public static final MetaData[] DEFAULT_THEME_META;
    public static final String FRAMEWORK_NAME = "framework-res";
    public static final String FRAMEWORK_PACKAGE = "android";
    public static final String ICONS_NAME = "icons";
    public static final String LAUNCHER_PACKAGE = "com.android.launcher";
    public static final String LOCKSCREEN_PACKAGE = "lockscreen";
    public static final String OPLUS_NAME = "oplus-framework-res";
    public static final String OPLUS_PACKAGE = "oplus";
    public static final String SYSTEMUI = "com.android.systemui";
    public static final String SYSTEM_THEME_PATH;
    private static final String TAG = "OplusThemeResources";
    public static final String THIRD_THEME_PATH = "/data/theme/";
    public static MetaData[] sCustomThemeMeta;
    private static OplusThemeResourcesSystem sSystem;
    public static MetaData[] sThemeMetaPath;
    private boolean mHasDrawable;
    private boolean mHasValue;
    private MetaData mMetaData;
    private String mPackageName;
    private IResourcesImplExt mResources;
    private OplusThemeResources mWrapped;
    private final boolean debug = true;
    private boolean mIsHasWrapped = false;
    private volatile OplusThemeZipFile mPackageZipFile = null;

    static {
        String defaultThemePath = OplusThemeUtil.getDefaultThemePath();
        SYSTEM_THEME_PATH = defaultThemePath;
        String str = OplusThemeUtil.CUSTOM_THEME_PATH;
        CUSTOM_THEME_PATH = str;
        DEFAULT_THEME_META = r3;
        sCustomThemeMeta = new MetaData[2];
        MetaData[] metaDataArr = {new MetaData(defaultThemePath, true, true, true), new MetaData("/data/theme/", true, true, true)};
        sCustomThemeMeta[0] = new MetaData(defaultThemePath, true, true, true);
        sCustomThemeMeta[1] = new MetaData(str, true, true, true);
        sThemeMetaPath = metaDataArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static final class MetaData {
        public boolean supportCharSequence = true;
        public boolean supportFile = true;
        public boolean supportInt = true;
        public String themePath;

        public MetaData(String path, boolean supportInt, boolean supportCharSequence, boolean supportFile) {
            this.themePath = null;
            this.themePath = path;
        }
    }

    public OplusThemeResources(OplusThemeResources themeResources, IResourcesImplExt resources, String name, MetaData metaData) {
        this.mResources = null;
        this.mWrapped = null;
        this.mMetaData = null;
        this.mPackageName = null;
        if (themeResources != null) {
            if (themeResources.mPackageZipFile != null) {
                this.mWrapped = themeResources;
            } else {
                this.mWrapped = null;
            }
        }
        this.mMetaData = metaData;
        this.mPackageName = name;
        this.mResources = resources;
        checkUpdate();
    }

    public boolean hasValues() {
        return this.mHasValue;
    }

    public static OplusThemeResourcesSystem getSystem(IResourcesImplExt resources) {
        if (sSystem == null) {
            sSystem = OplusThemeResourcesSystem.getTopLevelThemeResources(resources);
        }
        return sSystem;
    }

    public static OplusThemeResourcesSystem getSystem() {
        return sSystem;
    }

    public static OplusThemeResources getTopLevelThemeResources(IResourcesImplExt resources, String path) {
        setThemePath(resources, path);
        OplusThemeResources themeResources = null;
        int i = 0;
        while (true) {
            MetaData[] metaDataArr = sThemeMetaPath;
            if (i < metaDataArr.length) {
                OplusThemeResources res = new OplusThemeResources(themeResources, resources, path, metaDataArr[i]);
                themeResources = res;
                i++;
            } else {
                return themeResources;
            }
        }
    }

    public static void setThemePath(IResourcesImplExt resources, String packageName) {
        OplusExtraConfiguration extraConfig;
        if (resources == null) {
            return;
        }
        if (FRAMEWORK_NAME.equals(packageName) || OPLUS_NAME.equals(packageName)) {
            Configuration configuration = resources.getConfiguration();
            extraConfig = configuration != null ? configuration.getOplusExtraConfiguration() : null;
        } else {
            Configuration systemConfiguration = resources.getSystemConfiguration();
            extraConfig = systemConfiguration != null ? systemConfiguration.getOplusExtraConfiguration() : null;
        }
        if (extraConfig == null) {
            return;
        }
        long themeFlag = extraConfig.mThemeChangedFlags;
        if ((themeFlag & 256) == 256) {
            String str = DEFAULT_CUSTOM_PROP;
            if (!TextUtils.isEmpty(str) && !str.equals(CUSTOM_THEME_PATH)) {
                sCustomThemeMeta[1].themePath = extraConfig.mThemePrefix;
            }
            sThemeMetaPath = sCustomThemeMeta;
            return;
        }
        sThemeMetaPath = DEFAULT_THEME_META;
    }

    public boolean checkUpdate() {
        checkMetaPath(this.mResources, this.mPackageName);
        boolean reject = OplusBaseFile.rejectTheme(this.mResources, this.mPackageName);
        if (!reject) {
            this.mPackageZipFile = OplusThemeZipFile.getThemeZipFile(this.mMetaData, this.mPackageName, this.mResources);
        }
        if (this.mPackageZipFile != null && this.mPackageZipFile.shouldReload()) {
            this.mPackageZipFile.setResource(this.mResources);
            this.mPackageZipFile.clear();
        }
        this.mIsHasWrapped = (this.mWrapped == null || reject) ? false : true;
        boolean z = (this.mPackageZipFile != null || this.mIsHasWrapped) && !reject;
        this.mHasValue = z;
        this.mHasDrawable = z;
        return z | z;
    }

    private void checkMetaPath(IResourcesImplExt resources, String packageName) {
        OplusExtraConfiguration extraConfig;
        if (resources != null) {
            MetaData metaData = this.mMetaData;
            MetaData[] metaDataArr = DEFAULT_THEME_META;
            if (metaData == metaDataArr[0] || metaData == sCustomThemeMeta[0]) {
                return;
            }
            if (FRAMEWORK_NAME.equals(packageName) || OPLUS_NAME.equals(packageName)) {
                extraConfig = resources.getConfiguration().getOplusExtraConfiguration();
            } else {
                extraConfig = resources.getSystemConfiguration().getOplusExtraConfiguration();
            }
            if (extraConfig == null) {
                return;
            }
            long themeFlag = extraConfig.mThemeChangedFlags;
            if ((themeFlag & 256) == 256) {
                String str = DEFAULT_CUSTOM_PROP;
                if (!TextUtils.isEmpty(str) && !str.equals(CUSTOM_THEME_PATH)) {
                    sCustomThemeMeta[1].themePath = extraConfig.mThemePrefix;
                }
                this.mMetaData = sCustomThemeMeta[1];
                return;
            }
            this.mMetaData = metaDataArr[1];
        }
    }

    private boolean hasValuesInner() {
        if ((this.mPackageZipFile != null && this.mPackageZipFile.hasValues()) || (this.mIsHasWrapped && this.mWrapped.hasValuesInner())) {
            return true;
        }
        return false;
    }

    protected boolean isMutiPackage() {
        return false;
    }

    public boolean containsEntry(String path) {
        OplusThemeResources oplusThemeResources;
        if (this.mPackageZipFile == null) {
            return false;
        }
        boolean isExists = this.mPackageZipFile.containsEntry(path);
        if (!isExists && !this.mPackageZipFile.exists() && (oplusThemeResources = this.mWrapped) != null) {
            return oplusThemeResources.mPackageZipFile.containsEntry(path);
        }
        return isExists;
    }

    public CharSequence getThemeCharSequence(int id) {
        return getThemeCharSequenceInner(id);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CharSequence getThemeCharSequenceInner(int id) {
        OplusThemeResources oplusThemeResources;
        CharSequence res = null;
        if (this.mPackageZipFile != null) {
            checkAndInitZip(this.mPackageZipFile);
            res = this.mPackageZipFile.getCharSequence(id);
        }
        if (res == null && this.mIsHasWrapped && (oplusThemeResources = this.mWrapped) != null) {
            checkAndInitZip(oplusThemeResources.mPackageZipFile);
            CharSequence res2 = this.mWrapped.mPackageZipFile.getCharSequence(id);
            return res2;
        }
        return res;
    }

    public OplusThemeZipFile.ThemeFileInfo getThemeFileStream(int id, String path) {
        return getThemeFileStream(path);
    }

    public OplusThemeZipFile.ThemeFileInfo getPackageThemeFileStream(int index, String path) {
        return getPackageThemeFileStreamInner(index, path);
    }

    public OplusThemeZipFile.ThemeFileInfo getThemeFileStream(String path) {
        return getThemeFileStreamInner(path);
    }

    public OplusThemeZipFile.ThemeFileInfo getThemeFileStream(String path, boolean useWrap) {
        return getThemeFileStreamInner(path, useWrap);
    }

    protected OplusThemeZipFile.ThemeFileInfo getThemeFileStreamInner(String path, boolean useWrap) {
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = null;
        if (this.mPackageZipFile != null && !useWrap) {
            checkAndInitZip(this.mPackageZipFile);
            themeFileInfo = this.mPackageZipFile.getInputStream(path);
        }
        OplusThemeResources oplusThemeResources = this.mWrapped;
        if (oplusThemeResources != null && this.mIsHasWrapped && useWrap) {
            checkAndInitZip(oplusThemeResources.mPackageZipFile);
            return this.mWrapped.mPackageZipFile.getInputStream(path);
        }
        return themeFileInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OplusThemeZipFile.ThemeFileInfo getThemeFileStreamInner(String path) {
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = null;
        checkAndInitZip(this.mPackageZipFile);
        if (this.mPackageZipFile != null) {
            themeFileInfo = this.mPackageZipFile.getInputStream(path);
        }
        OplusThemeResources oplusThemeResources = this.mWrapped;
        if (oplusThemeResources != null && themeFileInfo == null && oplusThemeResources.mPackageZipFile != null && this.mIsHasWrapped) {
            checkAndInitZip(this.mWrapped.mPackageZipFile);
            OplusThemeZipFile.ThemeFileInfo themeFileInfo2 = this.mWrapped.mPackageZipFile.getInputStream(path);
            return themeFileInfo2;
        }
        return themeFileInfo;
    }

    protected OplusThemeZipFile.ThemeFileInfo getPackageThemeFileStreamInner(int index, String path) {
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = null;
        checkAndInitZip(this.mPackageZipFile);
        if (this.mPackageZipFile != null) {
            themeFileInfo = this.mPackageZipFile.getInputStream(index, path);
        }
        OplusThemeResources oplusThemeResources = this.mWrapped;
        if (oplusThemeResources != null && themeFileInfo == null && oplusThemeResources.mPackageZipFile != null && this.mIsHasWrapped) {
            checkAndInitZip(this.mWrapped.mPackageZipFile);
            OplusThemeZipFile.ThemeFileInfo themeFileInfo2 = this.mWrapped.mPackageZipFile.getInputStream(index, path);
            return themeFileInfo2;
        }
        return themeFileInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OplusThemeZipFile.ThemeFileInfo getIconFileStream(String path, boolean useWrap) {
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = null;
        if (this.mPackageZipFile != null && !useWrap) {
            checkAndInitZip(this.mPackageZipFile);
            themeFileInfo = this.mPackageZipFile.getIconInputStream(path);
        }
        OplusThemeResources oplusThemeResources = this.mWrapped;
        if (oplusThemeResources != null && this.mIsHasWrapped && useWrap) {
            checkAndInitZip(oplusThemeResources.mPackageZipFile);
            return this.mWrapped.mPackageZipFile.getIconInputStream(path);
        }
        return themeFileInfo;
    }

    public Integer getThemeInt(int id) {
        return getThemeIntInner(id);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Integer getThemeIntInner(int id) {
        OplusThemeResources oplusThemeResources;
        Integer res = null;
        if (this.mPackageZipFile != null) {
            checkAndInitZip(this.mPackageZipFile);
            res = this.mPackageZipFile.getInt(id);
        }
        if (res == null && this.mIsHasWrapped && (oplusThemeResources = this.mWrapped) != null) {
            checkAndInitZip(oplusThemeResources.mPackageZipFile);
            Integer res2 = this.mWrapped.mPackageZipFile.getInt(id);
            return res2;
        }
        return res;
    }

    public boolean hasDrawables() {
        return this.mHasDrawable;
    }

    protected boolean hasDrawableInner() {
        OplusThemeResources oplusThemeResources;
        boolean isValid = false;
        if (this.mPackageZipFile != null) {
            isValid = this.mPackageZipFile.hasZipDrawables();
        }
        if (!isValid && this.mIsHasWrapped && (oplusThemeResources = this.mWrapped) != null) {
            boolean isValid2 = oplusThemeResources.mPackageZipFile.hasZipDrawables();
            return isValid2;
        }
        return isValid;
    }

    public void setResource(IResourcesImplExt res) {
        this.mResources = res;
        if (this.mPackageZipFile != null) {
            this.mPackageZipFile.setResource(this.mResources);
        }
    }

    public boolean checkThemePackageExit() {
        if (this.mPackageZipFile != null) {
            return this.mPackageZipFile.exists();
        }
        return true;
    }

    private void checkAndInitZip(OplusThemeZipFile zipFile) {
        if (zipFile != null && !zipFile.mHasInit) {
            zipFile.initZipFile();
        }
    }
}
