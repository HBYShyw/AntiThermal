package android.content.res;

import android.app.ActivityThread;
import android.app.OplusActivityManager;
import android.app.OplusUxIconConstants;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.res.OplusThemeResources;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import com.oplus.theme.OplusThemeUtil;
import com.oplus.theme.OplusThirdPartUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import oplus.content.res.OplusExtraConfiguration;
import oplus.util.OplusDisplayUtils;

/* loaded from: classes.dex */
public class OplusThemeZipFile extends OplusBaseFile {
    private static final int ASSETS_THEME_FILE_INDEX_CN = 3;
    private static final int ASSETS_THEME_FILE_INDEX_EN = 4;
    private static final int ASSETS_THEME_FILE_INDEX_TW = 5;
    private static final int ASSETS_THEME_FILE_USE_COUNT = 3;
    private static final String TAG = "OplusThemeZipFile";
    protected boolean mHasInit;
    private long mLastModifyTime;
    private OplusThemeResources.MetaData mMetaData;
    private String mPath;
    private String mThemePkgName;
    private MultiZipFile mZipFile;
    private static final String[] RESOURCES_PATHS = {"res/drawable", "framework-res/res/drawable", "oplus-framework-res/res/drawable", "res/drawable"};
    private static final String[] ASSETS_THEME_VALUE_FILES = {"assets/colors.xml", "framework-res/assets/colors.xml", "oplus-framework-res/assets/colors.xml", "assets/values-cn/colors.xml", "assets/values-en/colors.xml", "assets/values-tw/colors.xml"};
    private static final ConcurrentHashMap<String, ZipEntry> mEntryCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> mCustomValidQueue = new ConcurrentHashMap<>();

    /* loaded from: classes.dex */
    public static class ThemeFileInfo {
        public int mDensity;
        public InputStream mInput;
        public long mSize;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ThemeFileInfo(InputStream input, long size) {
            this.mInput = input;
            this.mSize = size;
        }
    }

    public OplusThemeZipFile(String path, OplusThemeResources.MetaData metaData, String themeName, String packageName, IResourcesImplExt baseResources) {
        super(packageName, baseResources, metaData.supportInt, metaData.supportCharSequence, metaData.supportFile);
        this.mLastModifyTime = -1L;
        this.mPath = null;
        this.mThemePkgName = null;
        this.mMetaData = null;
        this.mZipFile = null;
        this.mHasInit = false;
        this.mLastModifyTime = -1L;
        this.mPath = path;
        this.mMetaData = metaData;
        this.mThemePkgName = themeName;
    }

    public boolean initZipFile() {
        if (isCutsomZipInValid(this.mThemePkgName, this.mMetaData, this.mBaseResources)) {
            this.mHasInit = true;
            return false;
        }
        checkPathForUser();
        boolean flag = false;
        if (this.mZipFile != null) {
            clear();
        }
        openZipFile();
        if (this.mPackageName.equals(OplusThemeResources.FRAMEWORK_PACKAGE) || this.mPackageName.equals(OplusThemeResources.OPLUS_PACKAGE)) {
            loadThemeValues(0, this.mZipFile);
            flag = true;
        } else if (this.mZipFile != null) {
            for (int i = 0; i < 3; i++) {
                loadThemeValues(i, this.mZipFile);
            }
            flag = true;
        }
        this.mHasInit = flag;
        return flag;
    }

    public ThemeFileInfo getInputStream(String path) {
        return getInputStream(0, path);
    }

    public ThemeFileInfo getInputStream(int index, String path) {
        if (!this.mMetaData.supportFile || this.mZipFile == null) {
            return null;
        }
        if (path.endsWith(".xml")) {
            String path2 = path.substring(0, path.lastIndexOf(".")) + OplusUxIconConstants.IconLoader.PNG_REG;
            ThemeFileInfo themeFileInfo = getInputStreamInner(index, path2, this.mZipFile, false);
            if (themeFileInfo == null) {
                return getInputStreamInner(index, path2.substring(0, path2.lastIndexOf(".")) + ".9.png", this.mZipFile, false);
            }
            return themeFileInfo;
        }
        ThemeFileInfo themeFileInfo2 = getInputStreamInner(index, path, this.mZipFile, false);
        if (path.endsWith(".9.png") && themeFileInfo2 == null) {
            return getInputStreamInner(index, path.replace(".9.png", OplusUxIconConstants.IconLoader.PNG_REG), this.mZipFile, false);
        }
        return themeFileInfo2;
    }

    public ThemeFileInfo getIconInputStream(String path) {
        MultiZipFile multiZipFile;
        if (!this.mMetaData.supportFile || (multiZipFile = this.mZipFile) == null) {
            return null;
        }
        ThemeFileInfo themeFileInfo = getInputStreamInner(0, path, multiZipFile, true);
        if (path.endsWith(".xml") && themeFileInfo == null) {
            return getInputStreamInner(0, path.substring(0, path.lastIndexOf(".")) + OplusUxIconConstants.IconLoader.PNG_REG, this.mZipFile, true);
        }
        return themeFileInfo;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001f, code lost:
    
        if (r4 != r2) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isZipFileValid(boolean isCheck) {
        boolean valid = true;
        if (!isCheck) {
            return true;
        }
        File file = new File(this.mPath);
        long modifyTime = file.lastModified();
        if (file.exists()) {
            long j = this.mLastModifyTime;
            if (j != -1) {
            }
            return valid;
        }
        valid = false;
        if (DEBUG_THEME) {
            Log.e(TAG, "check zip invalid: " + this.mPath + " mLastModifyTime= " + this.mLastModifyTime + " modifyTime= " + modifyTime);
        }
        return valid;
    }

    public boolean shouldReload() {
        File file = new File(this.mPath);
        long modifyTime = file.lastModified();
        if (!file.exists()) {
            return false;
        }
        long j = this.mLastModifyTime;
        if (j != -1 && j == modifyTime) {
            return false;
        }
        return true;
    }

    public boolean containsEntry(String s) {
        MultiZipFile multiZipFile = this.mZipFile;
        if (multiZipFile != null && multiZipFile.getEntry(s) != null) {
            return true;
        }
        return false;
    }

    public boolean exists() {
        return new File(this.mPath).exists();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized OplusThemeZipFile getThemeZipFile(OplusThemeResources.MetaData metadata, String packageName, IResourcesImplExt resources) {
        String path;
        boolean isPackageFile;
        OplusThemeZipFile themeZipFile;
        synchronized (OplusThemeZipFile.class) {
            if (metadata == null) {
                return null;
            }
            String process = ActivityThread.currentPackageName();
            for (String suffix : PACKAGE_DISABLE_LIST) {
                if (process != null && process.contains(suffix)) {
                    return null;
                }
            }
            String path2 = getValidZipPath(metadata, packageName, resources);
            boolean isPackageFile2 = isValidThemeFile(path2);
            if ("com.android.launcher".equalsIgnoreCase(packageName) && !isPackageFile2) {
                String path3 = getValidZipPath(metadata, OplusThirdPartUtil.ZIPLAUNCHER, resources);
                path = path3;
                isPackageFile = isValidThemeFile(path3);
            } else {
                path = path2;
                isPackageFile = isPackageFile2;
            }
            if (!isPackageFile) {
                return null;
            }
            WeakReference weakreference = sCacheFiles.get(path);
            if (weakreference != null) {
                themeZipFile = (OplusThemeZipFile) weakreference.get();
            } else {
                themeZipFile = null;
            }
            if (themeZipFile != null) {
                return themeZipFile;
            }
            OplusThemeZipFile themeZipFile2 = new OplusThemeZipFile(path, metadata, packageName, getPackageName(packageName), resources);
            sCacheFiles.put(path, new WeakReference(themeZipFile2));
            return themeZipFile2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasZipDrawables() {
        boolean hasDrawable = false;
        MultiZipFile multiZipFile = this.mZipFile;
        if (multiZipFile == null) {
            return false;
        }
        synchronized (multiZipFile) {
            try {
                Enumeration<?> entrys = this.mZipFile.entries();
                while (true) {
                    if (!entrys.hasMoreElements()) {
                        break;
                    }
                    ZipEntry enumEntry = entrys.nextElement();
                    if (enumEntry != null && !enumEntry.isDirectory() && enumEntry.getName() != null && enumEntry.getName().contains(RESOURCES_PATHS[0])) {
                        hasDrawable = true;
                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                if (DEBUG_THEME) {
                    Log.e(TAG, "Exception when hasZipDrawables, msg = " + e.toString());
                }
            }
        }
        return hasDrawable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void clear() {
        clean(this.mZipFile);
        mEntryCache.clear();
        mCustomValidQueue.clear();
        this.mHasInit = false;
    }

    private static boolean isValidThemeFile(String path) {
        boolean isValid = false;
        File packageFile = new File(path);
        try {
            try {
                if (packageFile.exists()) {
                    if (!packageFile.isDirectory()) {
                        isValid = true;
                    }
                }
            } catch (Exception e) {
                if (DEBUG_THEME) {
                    Log.e(TAG, "isValidThemeFile Exception e: " + e);
                }
            }
            return isValid;
        } finally {
        }
    }

    private static String getValidZipPath(OplusThemeResources.MetaData metadata, String packageName, IResourcesImplExt resources) {
        OplusExtraConfiguration extraConfig;
        if (OplusThemeResources.SYSTEM_THEME_PATH.equalsIgnoreCase(metadata.themePath)) {
            String path = metadata.themePath + packageName;
            return path;
        }
        if (OplusThemeResources.FRAMEWORK_NAME.equals(packageName) || OplusThemeResources.OPLUS_NAME.equals(packageName)) {
            Configuration configuration = resources.getConfiguration();
            extraConfig = configuration != null ? configuration.getOplusExtraConfiguration() : null;
        } else {
            Configuration systemConfiguration = resources.getSystemConfiguration();
            extraConfig = systemConfiguration != null ? systemConfiguration.getOplusExtraConfiguration() : null;
        }
        if (extraConfig == null || (extraConfig.mThemeChangedFlags & 256) != 0) {
            String path2 = metadata.themePath + packageName;
            return path2;
        }
        int userId = extraConfig.mUserId;
        if (userId <= 0) {
            String path3 = metadata.themePath + packageName;
            return path3;
        }
        String path4 = metadata.themePath + userId + "/" + packageName;
        return path4;
    }

    private ThemeFileInfo getInputStreamInner(int index, String path, MultiZipFile file, boolean isCheck) {
        ThemeFileInfo themeFileInfo = getZipInputStream(path, file, isCheck);
        if (themeFileInfo == null && file != null) {
            String str2 = RESOURCES_PATHS[index];
            int i = path.lastIndexOf("/");
            if (i > 0) {
                String str1 = path.substring(i);
                int j = 0;
                while (true) {
                    if (j >= sDensities.length) {
                        break;
                    }
                    String temp = str2 + OplusDisplayUtils.getDensitySuffix(sDensities[j]) + str1;
                    if (path.equalsIgnoreCase(temp) || (themeFileInfo = getZipInputStream(temp, file, isCheck)) == null) {
                        j++;
                    } else if (sDensities[j] > 1) {
                        themeFileInfo.mDensity = sDensities[j];
                    }
                }
            }
        }
        return themeFileInfo;
    }

    private ThemeFileInfo getZipInputStream(String path, MultiZipFile file, boolean isCheck) {
        InputStream inputStream;
        if (file == null) {
            return null;
        }
        try {
            ConcurrentHashMap<String, ZipEntry> concurrentHashMap = mEntryCache;
            ZipEntry zipEntry = concurrentHashMap.get(path);
            if (zipEntry == null && isZipFileValid(isCheck)) {
                zipEntry = this.mZipFile.getEntry(path);
            }
            if (zipEntry == null || !isZipFileValid(isCheck) || (inputStream = file.getInputStream(zipEntry)) == null) {
                return null;
            }
            concurrentHashMap.put(path, zipEntry);
            ThemeFileInfo themeFileInfo = new ThemeFileInfo(inputStream, zipEntry.getSize());
            return themeFileInfo;
        } catch (Exception e) {
            Slog.e(TAG, "OplusThemeZipFile Exception e: " + e + " path= " + path + " file= " + file);
            return null;
        }
    }

    private synchronized void openZipFile() {
        File file = new File(this.mPath);
        if (file.exists() && !file.isDirectory()) {
            long lastModified = file.lastModified();
            this.mLastModifyTime = lastModified;
            if (lastModified != -1) {
                try {
                    this.mZipFile = new MultiZipFile(file);
                } catch (Exception exception) {
                    if (DEBUG_THEME) {
                        Log.w(TAG, "openZipFile Exception e: " + exception + " path= " + this.mPath);
                    }
                }
            }
        } else {
            this.mZipFile = null;
        }
    }

    private void loadThemeValues(int index, MultiZipFile file) {
        int i = sDensities.length - 1;
        Object[] suffix = {OplusDisplayUtils.getDensitySuffix(sDensities[i])};
        ThemeFileInfo themeFileInfo = getZipInputStream(String.format(ASSETS_THEME_VALUE_FILES[index], suffix), file, false);
        parseXmlStream(index, themeFileInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MultiZipFile extends ZipFile {
        public MultiZipFile(File file) throws IOException {
            super(file);
        }
    }

    private void checkPathForUser() {
        if ("com.android.systemui".equals(this.mPackageName) && "/data/theme/".equals(this.mMetaData.themePath)) {
            OplusExtraConfiguration extraConfig = this.mBaseResources.getSystemConfiguration().getOplusExtraConfiguration();
            int userId = extraConfig == null ? 0 : extraConfig.mUserId;
            if (userId <= 0) {
                this.mPath = this.mMetaData.themePath + this.mPackageName;
            } else {
                this.mPath = this.mMetaData.themePath + userId + "/" + this.mPackageName;
            }
        }
    }

    private static boolean isCutsomZipInValid(String themePkgName, OplusThemeResources.MetaData metadata, IResourcesImplExt resources) {
        OplusExtraConfiguration extraConfig;
        boolean include;
        int index;
        ApplicationInfo info;
        if (sNightWhites.contains(themePkgName) || (extraConfig = resources.getSystemConfiguration().getOplusExtraConfiguration()) == null || (extraConfig.mThemeChangedFlags & 256) == 0) {
            return false;
        }
        String[] strArr = OPLUS_CUSTOM_CHECK_LIST;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                include = false;
                break;
            }
            String pkg = strArr[i];
            if (!themePkgName.equals(pkg)) {
                i++;
            } else {
                include = true;
                break;
            }
        }
        if (!include) {
            return false;
        }
        Boolean valid = mCustomValidQueue.get(themePkgName);
        if (valid != null) {
            return valid.booleanValue();
        }
        String themeData = "";
        try {
            IPackageManager packageManager = ActivityThread.getPackageManager();
            String appPkg = themePkgName;
            if (OplusThemeResources.FRAMEWORK_NAME.equals(themePkgName) || OplusThemeResources.OPLUS_NAME.equals(themePkgName)) {
                appPkg = "com.android.systemui";
            }
            if (packageManager != null && (info = packageManager.getApplicationInfo(appPkg, 128L, extraConfig.mUserId)) != null && info.metaData != null) {
                themeData = info.metaData.getString(OplusThemeUtil.THEME_VERSION_META_DATA);
            }
        } catch (Exception e) {
            Slog.e(TAG, "getThemeMetaData exception: " + e.toString());
        }
        if (TextUtils.isEmpty(themeData)) {
            return false;
        }
        int start = themeData.indexOf(".");
        int end = themeData.lastIndexOf(".");
        if (start < 0 || end < 0) {
            return false;
        }
        String version = themeData.substring(start + 1, end);
        if (TextUtils.isEmpty(version)) {
            return false;
        }
        String appStartVersion = themeData.substring(0, start) + "0";
        String themeVersion = "";
        String realProp = "";
        try {
            OplusActivityManager activityManager = new OplusActivityManager();
            themeVersion = activityManager.getAppThemeVersion(themePkgName, false);
            if (!TextUtils.isEmpty(themeVersion) && (index = themeVersion.indexOf(".")) > 0) {
                realProp = themeVersion.substring(0, index);
            }
        } catch (Exception e2) {
            Slog.e(TAG, "getThemeMetaData RemoteException: " + e2.toString());
        }
        if ((TextUtils.isEmpty(realProp) || realProp.equals(appStartVersion)) && (TextUtils.isEmpty(themeVersion) || themeVersion.contains(version))) {
            mCustomValidQueue.put(themePkgName, false);
            return false;
        }
        mCustomValidQueue.put(themePkgName, true);
        return true;
    }
}
