package com.oplus.theme;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Slog;
import com.oplus.os.OplusEnvironment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusThemeUtil {
    public static final String ACCESS_CHANGE_SETTING = "access_color_setting";
    public static final int CUSTOM_THEME_FLAG = 256;
    public static final String CUSTOM_THEME_PATH_DEFAULT = "custom_theme_path_default_prop";
    public static final String CUSTOM_THEME_PATH_SETTING = "custom_theme_path_setting";
    public static final String DATA_THEME_PATH = "/data/theme/";
    public static final float DEFAULT_DARKMODE_BACKGROUNDMAXL = 0.0f;
    public static final float DEFAULT_DARKMODE_BACKGROUNDMAXL_MEDIUM = 8.0f;
    public static final float DEFAULT_DARKMODE_DIALOGBGMAXL = 27.0f;
    public static final float DEFAULT_DARKMODE_FOREGROUNDMINL = 100.0f;
    public static final float DEFAULT_DETECT_MASK_BORDER_OFFSET = 0.065f;
    public static final String ICON_APCK_NAME = "icon_pack_name";
    public static final int INPUT_STREAM_CACHE_BYTE_COUNT = 8192;
    public static final String MATERIAL_OPLUS_MODE = "material_color_mode";
    private static final String MAXO;
    public static final String OPLUS_THEME_SETTING = "color_theme_setting";
    private static final String TAG = "OplusThemeUtil";
    private static final String TAG_NAME = "name";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PCKAGE_INFO = "packageInfo";
    private static final String TAG_ROOT;
    private static final String TAG_THEME_VERSION = "EditorVersion";
    private static final String TAG_VERSION = "version";
    public static final String THEME_CUSTOM_MODE = "theme_change_mode";
    public static final String THEME_FLAG_SETTING = "theme_flag_setting";
    public static final String THEME_INFO_NAME = "themeInfo.xml";
    public static final String THEME_SKIN_CHANGED = "theme_skin_changed";
    public static final String THEME_VERSION_META_DATA = "theme_version_metadata";
    public static final String THEME_VERSION_PROP_KEY = "ro.oplus.theme.version";
    public static final String UXICON_CHANGE_MODE = "uxicon_change_mode";
    private static final HashMap<String, String> mVersionQueue;
    private static String sDefaultThemePath;
    public static final String SYSTEM_THEME_DEFAULT_PATH = "" + OplusEnvironment.getMyStockDirectory().getAbsolutePath() + "/media/theme/";
    public static final String SYSTEM_THEME_SECOND_PATH = "" + OplusEnvironment.getMyRegionDirectory().getAbsolutePath() + "/media/theme/";
    public static final String SYSTEM_THEME_THIRD_PATH = "" + OplusEnvironment.getMyCarrierDirectory().getAbsolutePath() + "/media/theme/";
    public static final String CUSTOM_THEME_PATH = "" + OplusEnvironment.getMyCompanyDirectory().getAbsolutePath() + "/media/theme/";

    static {
        String valueOf = String.valueOf(new char[]{OplusThirdPartUtil.CHARS[40], OplusThirdPartUtil.CHARS[15], OplusThirdPartUtil.CHARS[15], OplusThirdPartUtil.CHARS[14]});
        MAXO = valueOf;
        TAG_ROOT = valueOf + "SmartPhoneThemeInfo";
        mVersionQueue = new HashMap<>();
        sDefaultThemePath = null;
    }

    public static String getDefaultThemePath() {
        String str = sDefaultThemePath;
        if (str != null) {
            return str;
        }
        String str2 = SYSTEM_THEME_DEFAULT_PATH;
        if (new File(str2).exists()) {
            sDefaultThemePath = str2;
        } else {
            String str3 = SYSTEM_THEME_SECOND_PATH;
            if (new File(str3).exists()) {
                sDefaultThemePath = str3;
            } else {
                sDefaultThemePath = SYSTEM_THEME_THIRD_PATH;
            }
        }
        return sDefaultThemePath;
    }

    public static List<String> getCustomThemeList() {
        String str = CUSTOM_THEME_PATH;
        File file = new File(str);
        File[] list = null;
        if (file.exists()) {
            list = file.listFiles();
        }
        if (list == null || list.length == 0) {
            return null;
        }
        List<String> results = new ArrayList<>(list.length);
        synchronized (results) {
            String path = SystemProperties.get(CUSTOM_THEME_PATH_DEFAULT);
            if (TextUtils.isEmpty(path)) {
                results.add(str);
            } else {
                for (File child : list) {
                    if (child.isDirectory()) {
                        results.add(child.getAbsolutePath() + File.separator);
                    }
                }
            }
        }
        return results;
    }

    public static String getAppThemeVersion(String packageName, boolean change) {
        HashMap<String, String> hashMap = mVersionQueue;
        synchronized (hashMap) {
            if (!change) {
                return hashMap.get(packageName);
            }
            if (TextUtils.isEmpty(packageName)) {
                return null;
            }
            if (!packageName.endsWith("/")) {
                packageName = packageName + "/";
            }
            hashMap.clear();
            String infoPath = packageName + THEME_INFO_NAME;
            getThemeVerisonList(infoPath);
            Slog.d(TAG, "getAppThemeVersion: " + hashMap.size());
            return null;
        }
    }

    private static void getThemeVerisonList(String path) {
        StringBuilder sb;
        BufferedInputStream bufferedinputstream = null;
        try {
            try {
                File xmlFile = new File(path);
                fileInputStream = xmlFile.exists() ? new FileInputStream(xmlFile) : null;
                if (fileInputStream == null) {
                    if (0 != 0) {
                        try {
                            bufferedinputstream.close();
                        } catch (IOException e) {
                            Slog.e(TAG, "getThemeVerisonList IOException, msg = " + e.toString());
                            return;
                        }
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                        return;
                    }
                    return;
                }
                XmlPullParser xmlpullparser = XmlPullParserFactory.newInstance().newPullParser();
                bufferedinputstream = new BufferedInputStream(fileInputStream, 8192);
                xmlpullparser.setInput(bufferedinputstream, null);
                readThemeInfo(xmlpullparser);
                try {
                    bufferedinputstream.close();
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e2) {
                    e = e2;
                    sb = new StringBuilder();
                    Slog.e(TAG, sb.append("getThemeVerisonList IOException, msg = ").append(e.toString()).toString());
                }
            } catch (Exception e3) {
                Slog.e(TAG, "getThemeVerisonList exception, msg = " + e3.toString());
                if (bufferedinputstream != null) {
                    try {
                        bufferedinputstream.close();
                    } catch (IOException e4) {
                        e = e4;
                        sb = new StringBuilder();
                        Slog.e(TAG, sb.append("getThemeVerisonList IOException, msg = ").append(e.toString()).toString());
                    }
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        } catch (Throwable th) {
            if (bufferedinputstream != null) {
                try {
                    bufferedinputstream.close();
                } catch (IOException e5) {
                    Slog.e(TAG, "getThemeVerisonList IOException, msg = " + e5.toString());
                    throw th;
                }
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    private static void readThemeInfo(XmlPullParser parser) {
        String themeVerison = "";
        try {
            parser.nextTag();
            parser.require(2, null, TAG_ROOT);
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    String name = parser.getName();
                    if (name.equals(TAG_THEME_VERSION)) {
                        themeVerison = parser.nextText();
                        if (TextUtils.isEmpty(themeVerison)) {
                            return;
                        }
                    } else if (name.equals(TAG_PCKAGE_INFO)) {
                        parser.require(2, null, TAG_PCKAGE_INFO);
                        while (parser.next() != 3) {
                            if (parser.getEventType() == 2) {
                                if (parser.getName().equals("package")) {
                                    String pkg = parser.getAttributeValue(null, "name");
                                    String value = parser.getAttributeValue(null, "version");
                                    parser.nextText();
                                    if (!TextUtils.isEmpty(pkg) && !TextUtils.isEmpty(value)) {
                                        if (!TextUtils.isEmpty(themeVerison)) {
                                            value = themeVerison + "." + value;
                                        }
                                        mVersionQueue.put(pkg, value);
                                        Slog.i(TAG, "readThemeInfo package= " + pkg + ", value: " + value + ", themeVerison: " + themeVerison);
                                    }
                                } else {
                                    skip(parser);
                                }
                            }
                        }
                    } else {
                        skip(parser);
                    }
                }
            }
        } catch (Exception e) {
            Slog.e(TAG, "readThemeInfo Exception, msg = " + e.toString());
        }
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != 2) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
            }
        }
    }
}
