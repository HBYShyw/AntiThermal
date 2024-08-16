package com.oplus.theme;

import android.app.OplusUxIconConstants;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusThirdPartUtil {
    public static final char[] CHARS;
    private static final String[] DIRS_DRAWABLE;
    private static final String[] DIRS_DRAWABLE_XH;
    private static final String[] DIRS_DRAWABLE_XXH;
    public static final String LAUNCHER_PACKAGE = "com.android.launcher";
    private static final int NUM = 3;
    public static final String O;
    private static final String TAG = "OplusThirdPartUtil";
    private static final long THIRD_FLAG = 1;
    public static final String ZIPICONS = "icons";
    public static final String ZIPLAUNCHER;
    public static boolean mIsDefaultTheme;
    public static String sThemePath;

    static {
        char[] cArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', PhoneNumberUtilsExtImpl.WILD, 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        CHARS = cArr;
        char c = cArr[14];
        char c2 = cArr[15];
        String valueOf = String.valueOf(new char[]{c, c2, c2, c});
        O = valueOf;
        ZIPLAUNCHER = "com." + valueOf + ".launcher";
        sThemePath = "/data/theme/";
        mIsDefaultTheme = true;
        DIRS_DRAWABLE = new String[]{"res/drawable-hdpi/", "res/drawable-xhdpi/", "res/drawable-xxhdpi/"};
        DIRS_DRAWABLE_XH = new String[]{"res/drawable-xhdpi/", "res/drawable-hdpi/", "res/drawable-xxhdpi/"};
        DIRS_DRAWABLE_XXH = new String[]{"res/drawable-xxhdpi/", "res/drawable-xhdpi/", "res/drawable-hdpi/"};
    }

    private OplusThirdPartUtil() {
    }

    public static boolean moveFile(String themeFileName, String resourceName, String destName) throws Exception {
        ZipFile zipFile = new ZipFile(themeFileName);
        ZipEntry entry = zipFile.getEntry(resourceName);
        if (entry == null) {
            return false;
        }
        InputStream stream = zipFile.getInputStream(entry);
        FileOutputStream out = new FileOutputStream(destName);
        byte[] buf = new byte[1024000];
        while (true) {
            int count = stream.read(buf);
            if (count > 0) {
                out.write(buf, 0, count);
            } else {
                stream.close();
                out.close();
                return true;
            }
        }
    }

    public static boolean clearDir(String path) {
        try {
            File themeDir = new File(path);
            for (String str : themeDir.list()) {
                File oldTheme = new File(path, str);
                if (oldTheme.exists()) {
                    oldTheme.delete();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Drawable getDrawableForUser(int id, Resources res, int userId) {
        String nameString = res.getResourceEntryName(id);
        return getLauncherDrawableByNameForUser(res, nameString + OplusUxIconConstants.IconLoader.PNG_REG, userId);
    }

    public static Drawable getLauncherDrawableByNameForUser(Resources res, String nameString, int userId) {
        return getDrawableByNameForLauncher(res, nameString, userId);
    }

    public static Drawable getIconDrawableByNameForUser(Resources res, String nameString, int userId) {
        return getDrawableByNameForUser(res, nameString, "icons", userId);
    }

    public static Drawable getDrawableByNameForUser(Resources res, String nameString, int userId) {
        return getDrawableByNameForLauncher(res, nameString, userId);
    }

    public static Drawable getDrawableByNameForLauncher(Resources res, String nameString, int userId) {
        Drawable drawable = null;
        String path = sThemePath;
        InputStream iStream = null;
        ZipFile file = null;
        String launcherName = getLauncherName(path);
        try {
            try {
                file = new ZipFile(path + launcherName);
                iStream = getDrawableStream(res, file, nameString);
                if (iStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(iStream);
                    drawable = new BitmapDrawable(res, bitmap);
                }
                try {
                    file.close();
                } catch (IOException e) {
                }
                if (iStream != null) {
                    iStream.close();
                }
            } catch (Exception e2) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e3) {
                    }
                }
                if (iStream != null) {
                    iStream.close();
                }
            } catch (Throwable th) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e4) {
                    }
                }
                if (iStream != null) {
                    try {
                        iStream.close();
                        throw th;
                    } catch (IOException e5) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (IOException e6) {
        }
        return drawable;
    }

    public static Drawable getDrawableByNameForUser(Resources res, String nameString, String zipPath, int userId) {
        Drawable drawable = null;
        String path = sThemePath;
        InputStream iStream = null;
        ZipFile file = null;
        try {
            try {
                file = new ZipFile(path + zipPath);
                iStream = getDrawableStream(res, file, nameString);
                if (iStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(iStream);
                    drawable = new BitmapDrawable(res, bitmap);
                }
                try {
                    file.close();
                } catch (IOException e) {
                }
                if (iStream != null) {
                    iStream.close();
                }
            } catch (Exception e2) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e3) {
                    }
                }
                if (iStream != null) {
                    iStream.close();
                }
            } catch (Throwable th) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e4) {
                    }
                }
                if (iStream != null) {
                    try {
                        iStream.close();
                        throw th;
                    } catch (IOException e5) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (IOException e6) {
        }
        return drawable;
    }

    public static Drawable getDrawable(int id, Resources res) {
        String nameString = res.getResourceEntryName(id);
        return getLauncherDrawableByNameForUser(res, nameString + OplusUxIconConstants.IconLoader.PNG_REG, 0);
    }

    public static Drawable getLauncherDrawableByName(Resources res, String nameString) {
        return getDrawableByNameForLauncher(res, nameString, 0);
    }

    public static Drawable getIconDrawableByName(Resources res, String nameString) {
        return getDrawableByNameForUser(res, nameString, "icons", 0);
    }

    public static Drawable getDrawableByName(Resources res, String nameString) {
        return getDrawableByNameForLauncher(res, nameString, 0);
    }

    public static Drawable getDrawableByName(Resources res, String nameString, String zipPath) {
        Drawable drawable = null;
        String path = sThemePath;
        InputStream iStream = null;
        ZipFile file = null;
        try {
            try {
                file = new ZipFile(path + zipPath);
                iStream = getDrawableStream(res, file, nameString);
                if (iStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(iStream);
                    drawable = new BitmapDrawable(res, bitmap);
                }
                try {
                    file.close();
                } catch (IOException e) {
                }
                if (iStream != null) {
                    iStream.close();
                }
            } catch (Exception e2) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e3) {
                    }
                }
                if (iStream != null) {
                    iStream.close();
                }
            } catch (Throwable th) {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e4) {
                    }
                }
                if (iStream != null) {
                    try {
                        iStream.close();
                        throw th;
                    } catch (IOException e5) {
                        throw th;
                    }
                }
                throw th;
            }
        } catch (IOException e6) {
        }
        return drawable;
    }

    private static InputStream getDrawableStream(Resources res, ZipFile file, String entryname) throws Exception {
        String[] temdirs = DIRS_DRAWABLE;
        float dpi = res.getDisplayMetrics().density;
        if (dpi >= 3.0f) {
            temdirs = DIRS_DRAWABLE_XXH;
        } else if (dpi >= 2.0f) {
            temdirs = DIRS_DRAWABLE_XH;
        }
        for (int i = 0; i <= 2; i++) {
            ZipEntry entry = file.getEntry(temdirs[i] + entryname);
            if (entry != null) {
                InputStream is = file.getInputStream(entry);
                return is;
            }
        }
        return null;
    }

    protected static String getThemePathForUser(Resources resources, long themeFlag, int userId) {
        OplusExtraConfiguration extraConfig;
        if ((256 & themeFlag) == 0) {
            if ((1 & themeFlag) != 0) {
                if (userId <= 0) {
                    return "/data/theme/";
                }
                String themePath = "/data/theme/" + userId + "/";
                return themePath;
            }
            String themePath2 = OplusThemeUtil.getDefaultThemePath();
            return themePath2;
        }
        if (resources == null || (extraConfig = resources.getConfiguration().getOplusExtraConfiguration()) == null) {
            String themePath3 = OplusThemeUtil.CUSTOM_THEME_PATH;
            return themePath3;
        }
        String path = SystemProperties.get(OplusThemeUtil.CUSTOM_THEME_PATH_DEFAULT);
        if (TextUtils.isEmpty(path)) {
            String themePath4 = OplusThemeUtil.CUSTOM_THEME_PATH;
            return themePath4;
        }
        String themePath5 = extraConfig.mThemePrefix;
        return themePath5;
    }

    public static void setDefaultTheme() {
        setDefaultTheme(0);
    }

    public static boolean getDefaultTheme() {
        return getDefaultTheme(0);
    }

    public static void setDefaultTheme(int userId) {
    }

    public static void setDefaultTheme(Resources resources, int userId) {
        long themeFlag = SystemProperties.getLong(getThemeKeyForUser(userId), 0L);
        if ((1 & themeFlag) == 0) {
            mIsDefaultTheme = true;
        } else {
            mIsDefaultTheme = false;
        }
        sThemePath = getThemePathForUser(resources, themeFlag, userId);
    }

    public static boolean getDefaultTheme(int userId) {
        long themeFlag = SystemProperties.getLong(getThemeKeyForUser(userId), 0L);
        if ((1 & themeFlag) == 0) {
            return true;
        }
        return false;
    }

    public static String getThemeKeyForUser(int userId) {
        if (userId <= 0) {
            return OplusUxIconConstants.SystemProperty.KEY_THEME_FLAG;
        }
        StringBuilder builder = new StringBuilder().append(OplusUxIconConstants.SystemProperty.KEY_THEME_FLAG).append(".").append(userId);
        return builder.toString();
    }

    public static String getLauncherName(String path) {
        File file = new File(path + "com.android.launcher");
        if (file.exists()) {
            return "com.android.launcher";
        }
        StringBuilder append = new StringBuilder().append(path);
        String str = ZIPLAUNCHER;
        File file2 = new File(append.append(str).toString());
        return file2.exists() ? str : "com.android.launcher";
    }
}
