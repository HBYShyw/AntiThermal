package android.content.res;

import android.app.ActivityThread;
import android.app.Application;
import android.app.IOplusCompactWindowAppManager;
import android.app.ResourcesManagerExtImpl;
import android.common.OplusFeatureCache;
import android.content.res.OplusThemeZipFile;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.DisplayAdjustments;
import android.view.autolayout.IOplusAutoLayoutManager;
import android.view.debug.IOplusViewDebugManager;
import com.google.android.collect.Sets;
import com.oplus.theme.OplusAppIconInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class ResourcesImplExt implements IResourcesImplExt {
    public static final int COOKIE_TYPE_FRAMEWORK = 1;
    public static final int COOKIE_TYPE_OPLUS = 2;
    public static final int COOKIE_TYPE_OTHERPACKAGE = 3;
    private static final boolean DEBUG = false;
    private static final int DEBUG_DEPTH = 30;
    private static final String PATH_PNG_SUFFIX = ".png";
    private static final String TAG = "ResourcesImplExt";
    protected OplusAccessibleResources mAccessibleResources;
    private DisplayAdjustments mCompactWindowAdjustments;
    private IOplusCompactWindowAppManager mCompactWindowAppManager;
    protected OplusThemeResourcesPackage mIconThemeResources;
    private boolean mIsAppConfig;
    private String mName;
    private ResourcesImpl mResourcesImpl;
    protected OplusThemeResourcesSystem mSystemThemeResources;
    protected OplusThemeResources mThemeResources;
    private static final boolean DEBUG_COMPACT_WINDOW = SystemProperties.getBoolean("persist.sys.debug.compactwindow.displayadjustment", false);
    private static final boolean DISABLE_COMPACT_WINDOW_DISPLAY_ADJUSTMENT = SystemProperties.getBoolean("persist.sys.disable.compactwindow.displayadjustment", false);
    private static final String WE_CHAT_PKG = "com.tencent.mm";
    private static final Set<String> BLOCK_SET_APPCONFIG_FOR_COMPACT_WINDOW = Sets.newHashSet(new String[]{WE_CHAT_PKG, "com.taobao.taobao"});
    protected boolean mIsHasValues = false;
    protected boolean mIsHasDrawables = false;
    protected boolean mIsHasSystemValues = false;
    protected boolean mIsHasSystemDrawables = false;
    protected boolean mIsHasAcessValues = false;
    protected boolean mIsHasAcessDrawables = false;
    protected SparseArray<CharSequence> mCharSequences = new SparseArray<>();
    protected SparseIntArray mCookies = new SparseIntArray();
    protected SparseArray<Integer> mIntegers = new SparseArray<>();
    protected SparseArray<Integer> mThemeIntegers = new SparseArray<>();
    protected SparseArray<Boolean> mSkipFiles = new SparseArray<>();
    protected SparseArray<CharSequence> mSkipIcons = new SparseArray<>();
    protected boolean mThemeChanged = false;
    protected SparseArray<Boolean> mLoadArrary = new SparseArray<>();

    public ResourcesImplExt(Object resImpl) {
        this.mResourcesImpl = null;
        this.mResourcesImpl = (ResourcesImpl) resImpl;
        init(null);
        this.mCompactWindowAppManager = OplusFeatureCache.getOrCreate(IOplusCompactWindowAppManager.DEFAULT, new Object[0]);
    }

    String getResourcePackageName(int resid) {
        ResourcesImpl resourcesImpl = this.mResourcesImpl;
        if (resourcesImpl != null) {
            return resourcesImpl.getResourcePackageName(resid);
        }
        return null;
    }

    String getResourceName(int resid) {
        ResourcesImpl resourcesImpl = this.mResourcesImpl;
        if (resourcesImpl != null) {
            return resourcesImpl.getResourceName(resid);
        }
        return null;
    }

    void getValue(int id, TypedValue outValue, boolean resolveRefs) {
        ResourcesImpl resourcesImpl = this.mResourcesImpl;
        if (resourcesImpl != null) {
            resourcesImpl.getValue(id, outValue, resolveRefs);
        }
    }

    public void clearCache() {
        this.mThemeChanged = true;
    }

    public final void init(String name) {
        this.mName = name;
        initThemeResource(name);
        OplusThemeResources oplusThemeResources = this.mThemeResources;
        if (oplusThemeResources != null) {
            this.mIsHasValues = oplusThemeResources.hasValues();
            this.mIsHasDrawables = this.mThemeResources.hasDrawables();
        }
        OplusThemeResourcesSystem oplusThemeResourcesSystem = this.mSystemThemeResources;
        if (oplusThemeResourcesSystem != null) {
            this.mIsHasSystemValues = oplusThemeResourcesSystem.hasValues();
            this.mIsHasSystemDrawables = this.mSystemThemeResources.hasDrawables();
        }
        OplusAccessibleResources oplusAccessibleResources = this.mAccessibleResources;
        if (oplusAccessibleResources != null) {
            this.mIsHasAcessValues = oplusAccessibleResources.hasValues();
            this.mIsHasAcessDrawables = this.mAccessibleResources.hasDrawables();
        }
    }

    public String getPackageName() {
        return this.mName;
    }

    public synchronized Drawable loadOverlayDrawable(Resources wrapper, TypedValue value, int id) {
        OplusAccessibleResources oplusAccessibleResources;
        OplusThemeResourcesSystem oplusThemeResourcesSystem;
        OplusThemeResources oplusThemeResources;
        if (!this.mIsHasDrawables && !this.mIsHasAcessDrawables && !this.mIsHasSystemDrawables) {
            return null;
        }
        String path = value.string.toString();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Drawable drawable = null;
        if (this.mSkipFiles.get(id) == null) {
            OplusThemeZipFile.ThemeFileInfo themeFileInfo = null;
            int cookieId = getCookieType(value.assetCookie, id);
            if (this.mIsHasDrawables && (oplusThemeResources = this.mThemeResources) != null) {
                themeFileInfo = oplusThemeResources.getThemeFileStream(cookieId, path);
            }
            if (this.mIsHasSystemDrawables && themeFileInfo == null && (oplusThemeResourcesSystem = this.mSystemThemeResources) != null && cookieId < 3) {
                themeFileInfo = oplusThemeResourcesSystem.getThemeFileStream(cookieId, path);
            }
            if (this.mIsHasAcessDrawables && themeFileInfo == null && (oplusAccessibleResources = this.mAccessibleResources) != null) {
                themeFileInfo = oplusAccessibleResources.getAccessibleStream(cookieId, path);
            }
            try {
                if (themeFileInfo == null) {
                    this.mSkipFiles.put(id, true);
                    return null;
                }
                try {
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        if (themeFileInfo.mDensity == 0) {
                            options.inDensity = wrapper.getDisplayMetrics().densityDpi;
                        } else {
                            options.inDensity = themeFileInfo.mDensity;
                        }
                        InputStream input = themeFileInfo.mInput;
                        drawable = Drawable.createFromResourceStream(wrapper, value, input, path, options);
                    } catch (Throwable th) {
                        if (themeFileInfo != null) {
                            try {
                                themeFileInfo.mInput.close();
                            } catch (Exception e) {
                            }
                        }
                        throw th;
                    }
                } catch (OutOfMemoryError e2) {
                    Log.e(TAG, "out of memory !!");
                    if (themeFileInfo != null) {
                        themeFileInfo.mInput.close();
                    }
                }
                if (themeFileInfo != null) {
                    themeFileInfo.mInput.close();
                }
            } catch (Exception e3) {
            }
        }
        if (drawable != null) {
            this.mLoadArrary.put(id, true);
        }
        return drawable;
    }

    public TypedArray replaceTypedArray(TypedArray typedarray) {
        Integer res;
        if (this.mIsHasValues || this.mIsHasAcessValues || this.mIsHasSystemValues) {
            int[] ai = typedarray.mData;
            int cookie = typedarray.mValue.assetCookie;
            for (int i = 0; i < ai.length; i += 7) {
                int type = ai[i + 0];
                int id = ai[i + 3];
                if ((type >= 16 && type <= 31) || type == 5) {
                    Integer res2 = getThemeInt(id, cookie);
                    if (res2 != null) {
                        ai[i + 1] = res2.intValue();
                    } else if (type >= 28 && type <= 31 && (res = getColorValue(id, cookie)) != null) {
                        ai[i + 1] = res.intValue();
                    }
                }
            }
        }
        return typedarray;
    }

    public void setIsThemeChanged(boolean changed) {
        this.mThemeChanged = changed;
    }

    public boolean getThemeChanged() {
        return this.mThemeChanged;
    }

    public synchronized Drawable loadIcon(Resources wrapper, int id, String str, boolean useWrap) {
        Drawable drawable = null;
        TypedValue value = new TypedValue();
        getValue(id, value, true);
        String path = value.string.toString();
        if (str != null) {
            String temp = path.substring(path.lastIndexOf("/") + 1);
            path = path.replace(temp, str);
        }
        if (this.mSkipIcons.get(id) == null) {
            if (this.mIconThemeResources == null) {
                this.mIconThemeResources = OplusThemeResourcesPackage.getThemeResources(this, "icons");
            }
            OplusThemeZipFile.ThemeFileInfo themeFileInfo = this.mIconThemeResources.getIconFileStream(path, useWrap);
            int dotIndex = path.lastIndexOf(".");
            if (themeFileInfo == null && !path.endsWith(".png") && dotIndex > -1) {
                path = path.substring(0, dotIndex) + ".png";
                themeFileInfo = this.mIconThemeResources.getIconFileStream(path, useWrap);
            }
            if (themeFileInfo == null) {
                this.mSkipIcons.put(id, path);
                return null;
            }
            try {
                if (themeFileInfo != null) {
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        if (themeFileInfo.mDensity == 0) {
                            options.inDensity = wrapper.getDisplayMetrics().densityDpi;
                        } else {
                            options.inDensity = themeFileInfo.mDensity;
                        }
                        InputStream input = themeFileInfo.mInput;
                        drawable = Drawable.createFromResourceStream(wrapper, value, input, path, options);
                    } catch (OutOfMemoryError e) {
                        if (themeFileInfo != null) {
                            themeFileInfo.mInput.close();
                        }
                    } catch (Throwable th) {
                        if (themeFileInfo != null) {
                            try {
                                themeFileInfo.mInput.close();
                            } catch (IOException e2) {
                            }
                        }
                        throw th;
                    }
                }
                if (themeFileInfo != null) {
                    themeFileInfo.mInput.close();
                }
            } catch (IOException e3) {
            }
        }
        return drawable;
    }

    public synchronized InputStream openThemeRawResource(int id, TypedValue outValue) throws Resources.NotFoundException {
        OplusAccessibleResources oplusAccessibleResources;
        OplusThemeResourcesSystem oplusThemeResourcesSystem;
        OplusThemeResources oplusThemeResources;
        if ((!this.mIsHasDrawables && !this.mIsHasAcessDrawables && !this.mIsHasSystemDrawables) || TextUtils.isEmpty(outValue.string)) {
            return null;
        }
        OplusThemeZipFile.ThemeFileInfo themeFileInfo = null;
        if (this.mSkipFiles.get(id) == null) {
            String path = outValue.string.toString();
            int cookieId = getCookieType(outValue.assetCookie, id);
            if (this.mIsHasDrawables && (oplusThemeResources = this.mThemeResources) != null) {
                themeFileInfo = oplusThemeResources.getThemeFileStream(cookieId, path);
            }
            if (this.mIsHasSystemDrawables && themeFileInfo == null && (oplusThemeResourcesSystem = this.mSystemThemeResources) != null && cookieId < 3) {
                themeFileInfo = oplusThemeResourcesSystem.getThemeFileStream(cookieId, path);
            }
            if (this.mIsHasAcessDrawables && themeFileInfo == null && (oplusAccessibleResources = this.mAccessibleResources) != null) {
                themeFileInfo = oplusAccessibleResources.getAccessibleStream(cookieId, path);
            }
            if (themeFileInfo != null) {
                InputStream input = themeFileInfo.mInput;
                return input;
            }
            this.mSkipFiles.put(id, true);
        }
        return null;
    }

    public CharSequence getThemeCharSequence(int id) {
        if (!this.mIsHasValues) {
            return null;
        }
        CharSequence res = null;
        try {
            int index = this.mCharSequences.indexOfKey(id);
            if (index >= 0) {
                res = this.mCharSequences.valueAt(index);
            } else {
                OplusThemeResources oplusThemeResources = this.mThemeResources;
                if (oplusThemeResources != null) {
                    res = oplusThemeResources.getThemeCharSequence(id);
                }
            }
            if (res != null) {
                this.mCharSequences.put(id, res);
            }
        } catch (Exception e) {
            Log.e(TAG, "getThemeCharSequence exception: ", e);
        }
        return res;
    }

    public SparseArray<Boolean> getLoadArrary() {
        return this.mLoadArrary;
    }

    public boolean isHasDrawables() {
        return this.mIsHasDrawables || this.mIsHasAcessDrawables || this.mIsHasSystemDrawables;
    }

    public int updateExConfiguration(ResourcesImpl resources, Configuration config) {
        Configuration oldConfig;
        if (resources == null || (oldConfig = resources.getConfiguration()) == null || config == null) {
            return 0;
        }
        int diff = oldConfig.diff(config);
        return diff;
    }

    public void checkUpdate(int changes, boolean languageChaged) {
        boolean needNew = OplusExtraConfiguration.needNewResources(changes);
        boolean needAccessNew = OplusExtraConfiguration.needAccessNewResources(changes);
        boolean nightChange = (changes & 512) != 0;
        if ((this.mThemeResources != null || this.mSystemThemeResources != null || this.mIconThemeResources != null) && needNew) {
            clear(true);
            this.mIsHasValues = false;
            this.mIsHasDrawables = false;
            this.mIsHasSystemValues = false;
            this.mIsHasSystemDrawables = false;
            OplusAppIconInfo.reset();
            OplusThemeResources oplusThemeResources = this.mThemeResources;
            if (oplusThemeResources != null) {
                oplusThemeResources.setResource(this);
                this.mThemeResources.checkUpdate();
                this.mIsHasValues = this.mThemeResources.hasValues();
                this.mIsHasDrawables = this.mThemeResources.hasDrawables();
            }
            OplusThemeResourcesSystem oplusThemeResourcesSystem = this.mSystemThemeResources;
            if (oplusThemeResourcesSystem != null) {
                oplusThemeResourcesSystem.checkUpdate();
                this.mIsHasSystemValues = this.mSystemThemeResources.hasValues();
                this.mIsHasSystemDrawables = this.mSystemThemeResources.hasDrawables();
            }
            OplusThemeResourcesPackage oplusThemeResourcesPackage = this.mIconThemeResources;
            if (oplusThemeResourcesPackage != null) {
                oplusThemeResourcesPackage.checkUpdate();
            }
        }
        if (this.mAccessibleResources != null) {
            if (needAccessNew || nightChange) {
                clear(false);
                this.mIsHasAcessValues = false;
                this.mIsHasAcessDrawables = false;
                this.mAccessibleResources.checkUpdate(true);
                this.mIsHasAcessValues = this.mAccessibleResources.hasValues();
                this.mIsHasAcessDrawables = this.mAccessibleResources.hasDrawables();
            }
        }
    }

    public void getExValue(int id, TypedValue outValue, boolean resolveRefs) {
        Integer res;
        if (!this.mIsHasValues && !this.mIsHasAcessValues && !this.mIsHasSystemValues) {
            return;
        }
        if ((outValue.type >= 16 && outValue.type <= 31) || outValue.type == 5) {
            Integer res2 = getThemeInt(id, outValue.resourceId, outValue.assetCookie);
            if (res2 != null) {
                outValue.data = res2.intValue();
            } else if (outValue.type >= 28 && outValue.type <= 31 && (res = getColorValue(id, outValue.resourceId, outValue.assetCookie)) != null) {
                outValue.data = res.intValue();
            }
        }
    }

    public void initThemeResource(String name) {
        if (TextUtils.isEmpty(name) || OplusThemeResources.FRAMEWORK_PACKAGE.equals(name) || OplusThemeResources.OPLUS_PACKAGE.equals(name)) {
            this.mSystemThemeResources = OplusThemeResources.getSystem(this);
            return;
        }
        int tempMask = StrictMode.allowThreadDiskWritesMask();
        try {
            this.mThemeResources = OplusThemeResourcesPackage.getThemeResources(this, name);
            this.mAccessibleResources = OplusAccessibleResources.getAccessResources(this, name);
        } finally {
            StrictMode.setThreadPolicyMask(tempMask);
        }
    }

    private int getCookieType(int cookie, int id) {
        if (id == 0) {
            return id;
        }
        int i = 0;
        try {
            i = this.mCookies.get(id);
            if (i == 0) {
                String packageName = getResourcePackageName(id);
                if (OplusThemeResources.FRAMEWORK_PACKAGE.equals(packageName)) {
                    i = 1;
                } else if (OplusThemeResources.OPLUS_PACKAGE.equals(packageName)) {
                    i = 2;
                } else {
                    i = 3;
                }
                this.mCookies.put(id, i);
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "getCookieType. e = " + e);
        } catch (ArrayIndexOutOfBoundsException e2) {
            Log.e(TAG, "getCookieType. OutOfBounds e = " + e2);
        }
        return i;
    }

    private Integer getThemeInt(int id, int cookie) {
        return getThemeInt(id, 0, cookie);
    }

    private Integer getThemeInt(int id, int resourceId, int cookie) {
        OplusThemeResources oplusThemeResources;
        if (!this.mIsHasValues && !this.mIsHasSystemValues) {
            return null;
        }
        Integer res = null;
        try {
            int index = this.mThemeIntegers.indexOfKey(id);
            if (index >= 0) {
                return this.mThemeIntegers.valueAt(index);
            }
            if (this.mIsHasValues && (oplusThemeResources = this.mThemeResources) != null && (res = oplusThemeResources.getThemeInt(id)) == null && resourceId != 0 && resourceId != id) {
                res = this.mThemeResources.getThemeInt(resourceId);
            }
            if (this.mIsHasSystemValues && res == null && this.mSystemThemeResources != null) {
                int cookieId = getCookieType(cookie, id);
                if (cookieId < 3) {
                    res = this.mSystemThemeResources.getThemeInt(id, cookieId);
                }
                int cookieResId = getCookieType(cookie, resourceId);
                if (res == null && resourceId != 0 && resourceId != id && cookieResId < 3) {
                    res = this.mSystemThemeResources.getThemeInt(resourceId, cookieResId);
                }
            }
            this.mThemeIntegers.put(id, res);
            return res;
        } catch (Exception e) {
            Log.e(TAG, "getThemeInt. e = " + e);
            return null;
        }
    }

    private Integer getColorValue(int id, int cookie) {
        return getColorValue(id, 0, cookie);
    }

    private Integer getColorValue(int id, int resourceId, int cookie) {
        if (!this.mIsHasAcessValues || id == 0) {
            return null;
        }
        Integer color = null;
        try {
            int index = this.mIntegers.indexOfKey(id);
            if (index >= 0) {
                return this.mIntegers.valueAt(index);
            }
            int cookieId = getCookieType(cookie, id);
            OplusAccessibleResources oplusAccessibleResources = this.mAccessibleResources;
            if (oplusAccessibleResources != null && cookieId == 3 && (color = oplusAccessibleResources.getAccessibleInt(id)) == null && resourceId != 0 && resourceId != id) {
                int vcookieId = getCookieType(cookie, resourceId);
                if (vcookieId == 3) {
                    color = this.mAccessibleResources.getAccessibleInt(resourceId);
                }
            }
            this.mIntegers.put(id, color);
            return color;
        } catch (Exception e) {
            Log.e(TAG, "getColorValue. e = " + e);
            return null;
        }
    }

    private void clear(boolean theme) {
        clearCache();
        clearSync();
        if (theme) {
            this.mThemeIntegers.clear();
        } else {
            this.mIntegers.clear();
        }
        this.mCharSequences.clear();
        this.mLoadArrary.clear();
        this.mCookies.clear();
        this.mResourcesImpl.getWrapper().getPreloadedDrawables().clear();
        this.mResourcesImpl.getWrapper().getPreloadedColorDrawables().clear();
        this.mResourcesImpl.getWrapper().getPreloadedComplexColors().clear();
    }

    private synchronized void clearSync() {
        this.mSkipFiles.clear();
        this.mSkipIcons.clear();
    }

    public ResourcesImpl getResourcesImpl() {
        return this.mResourcesImpl;
    }

    public Configuration getConfiguration() {
        return this.mResourcesImpl.getConfiguration();
    }

    public DisplayMetrics getDisplayMetrics() {
        return this.mResourcesImpl.getDisplayMetrics();
    }

    public Configuration getSystemConfiguration() {
        return Resources.getSystem().getConfiguration();
    }

    public DisplayAdjustments getCompactWindowAdjustments() {
        if (DISABLE_COMPACT_WINDOW_DISPLAY_ADJUSTMENT) {
            return null;
        }
        return this.mCompactWindowAdjustments;
    }

    public void updateCompactWindowAdjustments(Configuration oldOverrideConfig, Configuration newOverrideConfig) {
        if ((this.mCompactWindowAdjustments == null && newOverrideConfig != null && newOverrideConfig.windowConfiguration.getWindowingMode() == 120) || ResourcesManagerExtImpl.inOplusCompatMode(newOverrideConfig)) {
            this.mCompactWindowAdjustments = new DisplayAdjustments(newOverrideConfig);
            return;
        }
        if (this.mCompactWindowAdjustments != null && (newOverrideConfig == null || newOverrideConfig.windowConfiguration.getWindowingMode() != 120 || ResourcesManagerExtImpl.inOplusCompatMode(newOverrideConfig))) {
            this.mCompactWindowAdjustments = null;
        } else if ((!this.mIsAppConfig && this.mCompactWindowAdjustments != null && newOverrideConfig.windowConfiguration.getWindowingMode() == 120) || ResourcesManagerExtImpl.inOplusCompatMode(newOverrideConfig)) {
            this.mCompactWindowAdjustments.setConfiguration(newOverrideConfig);
        }
    }

    public DisplayMetrics getCompactWindowMetrics(ResourcesImpl resources, DisplayMetrics oldMetrics) {
        IOplusCompactWindowAppManager iOplusCompactWindowAppManager = this.mCompactWindowAppManager;
        if (iOplusCompactWindowAppManager != null) {
            return iOplusCompactWindowAppManager.getCompactWindowMetrics(resources, oldMetrics);
        }
        return null;
    }

    public DisplayMetrics hookGetDisplayMetrics(DisplayMetrics originalMetrics) {
        return getOplusAutoLayoutManager().getAutoLayoutDisplayMetrics(originalMetrics);
    }

    private IOplusAutoLayoutManager getOplusAutoLayoutManager() {
        return (IOplusAutoLayoutManager) OplusFeatureCache.getOrCreate(IOplusAutoLayoutManager.mDefault, new Object[0]);
    }

    public void setIsAppConfig(boolean isAppConfig) {
        Application app = ActivityThread.currentApplication();
        if (app == null || BLOCK_SET_APPCONFIG_FOR_COMPACT_WINDOW.contains(app.getApplicationInfo().packageName)) {
            return;
        }
        this.mIsAppConfig = isAppConfig;
    }

    public boolean getIsAppConfig() {
        return this.mIsAppConfig;
    }

    public void markOnConfigChange(ResourcesImpl impl, DisplayMetrics displayMetrics, Configuration configuration) {
        ((IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0])).markOnConfigChange(impl, displayMetrics, configuration);
    }
}
