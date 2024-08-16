package android.content.res;

import android.app.ActivityThread;
import android.app.OplusUxIconConstants;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.OplusThemeZipFile;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import com.android.internal.util.XmlUtils;
import com.oplus.theme.OplusThirdPartUtil;
import com.oplus.view.OplusWindowUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipFile;
import oplus.content.res.OplusExtraConfiguration;
import oplus.util.OplusDisplayUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusBaseFile {
    protected static final String ACCESSIBLE = "accessible";
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_PACKAGE = "package";
    protected static final boolean DEBUG_THEME;
    public static final String DISABLE;
    protected static final long FFFFFF8000000000 = -549755813888L;
    protected static final long FFFFFFFF80000000 = -2147483648L;
    protected static final int INPUT_STREAM_CACHE_BYTE_COUNT = 8192;
    protected static final String NINE_SUFFIX = ".9.png";
    public static final String[] OPLUS_CUSTOM_CHECK_LIST;
    protected static final String OPLUS_MATERIAL_ENABLE = "color_material_enable";
    protected static final String OPLUS_NXTHEME_IDENTIFIER = "nxColorThemeIdentifier";
    protected static final String OPLUS_THEME_IDENTIFIER = "couiThemeIdentifier";
    protected static final String OPLUS_XML = "colors.xml";
    public static final String[] PACKAGE_DISABLE_LIST;
    protected static final String PATH_DIVIDER = "/";
    protected static final String PATH_SUFFIX = "#*.png";
    protected static final String PNG_SUFFIX = ".png";
    public static final String S;
    protected static final String TAG = "OplusBaseFile";
    protected static final String TAG_ATTR = "attr";
    protected static final String TAG_BOOLEAN = "bool";
    protected static final String TAG_CHILD = "child";
    protected static final String TAG_COLOR = "color";
    protected static final String TAG_DIMEN = "dimen";
    protected static final String TAG_DRAWABLE = "drawable";
    protected static final String TAG_GROUP = "group";
    protected static final String TAG_ID = "id";
    protected static final String TAG_INDEX = "index";
    protected static final String TAG_INTEGER = "integer";
    protected static final String TAG_RESOURCES = "resources";
    protected static final String TAG_STRING = "string";
    protected static final String TRUE = "true";
    protected static final String XML_SUFFIX = ".xml";
    protected static Map<String, WeakReference<OplusBaseFile>> sCacheFiles;
    protected static int[] sDensities;
    protected static int sDensity;
    protected static ArrayList<String> sNightWhites;
    protected IResourcesImplExt mBaseResources;
    protected SparseArray mCharSequences;
    private boolean mHasParsed;
    protected SparseArray mIntegers;
    protected String mPackageName;
    protected ResourcesImpl mResources;
    private boolean mSupportChar;
    private boolean mSupportFile;
    private boolean mSupportInt;
    private HashMap<String, Integer> mThemeIndexMap = new HashMap<>();

    static {
        String valueOf = String.valueOf(new char[]{OplusThirdPartUtil.CHARS[2], OplusThirdPartUtil.CHARS[14], OplusThirdPartUtil.CHARS[11], OplusThirdPartUtil.CHARS[14], OplusThirdPartUtil.CHARS[17], OplusThirdPartUtil.CHARS[14], OplusThirdPartUtil.CHARS[18]});
        S = valueOf;
        String valueOf2 = String.valueOf(new char[]{OplusThirdPartUtil.CHARS[2], OplusThirdPartUtil.CHARS[19], OplusThirdPartUtil.CHARS[18]});
        DISABLE = valueOf2;
        PACKAGE_DISABLE_LIST = new String[]{"com.android." + valueOf2, "com.android.networkstack"};
        OPLUS_CUSTOM_CHECK_LIST = new String[]{"com.android.systemui", "com.android.settings", "com.android.mms", OplusWindowUtils.PACKAGE_INCALL, OplusUxIconConstants.IconLoader.COM_ANDROID_CONTACTS, "com." + valueOf + ".incallui", "com." + valueOf + ".alarmclock", "com.android.settings.intelligence"};
        DEBUG_THEME = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        sNightWhites = new ArrayList<>();
        sDensity = DisplayMetrics.DENSITY_DEVICE_STABLE;
        sCacheFiles = new ConcurrentHashMap();
        sNightWhites.add("icons");
        sNightWhites.add(OplusThemeResources.LOCKSCREEN_PACKAGE);
        sNightWhites.add("com.android.launcher");
        sDensities = OplusDisplayUtils.getBestDensityOrder(sDensity);
    }

    public OplusBaseFile(String packageName, IResourcesImplExt baseResources, boolean supportInt, boolean supportChar, boolean supportFile) {
        this.mIntegers = null;
        this.mCharSequences = null;
        this.mResources = null;
        this.mBaseResources = null;
        this.mPackageName = null;
        this.mSupportChar = false;
        this.mSupportFile = false;
        this.mSupportInt = false;
        this.mHasParsed = false;
        this.mIntegers = new SparseArray();
        this.mCharSequences = new SparseArray();
        this.mPackageName = packageName;
        this.mSupportInt = supportInt;
        this.mSupportChar = supportChar;
        this.mSupportFile = supportFile;
        this.mBaseResources = baseResources;
        this.mResources = baseResources.getResourcesImpl();
        this.mHasParsed = false;
    }

    public void setResource(IResourcesImplExt baseResources) {
        this.mBaseResources = baseResources;
        this.mResources = baseResources.getResourcesImpl();
    }

    public static boolean rejectTheme(IResourcesImplExt resourcesExt, String packageName) {
        return isNightMode(resourcesExt) && !sNightWhites.contains(packageName);
    }

    public static boolean isNightMode(IResourcesImplExt resourcesExt) {
        Configuration configuration;
        String pkg = resourcesExt.getPackageName();
        if (TextUtils.isEmpty(pkg) || OplusThemeResources.FRAMEWORK_PACKAGE.equals(pkg) || OplusThemeResources.OPLUS_PACKAGE.equals(pkg)) {
            configuration = resourcesExt.getConfiguration();
        } else {
            configuration = resourcesExt.getSystemConfiguration();
        }
        if (configuration == null) {
            return false;
        }
        if (!configuration.isNightModeActive() && (configuration.uiMode & 48) != 48) {
            return false;
        }
        return true;
    }

    public void setParsed(boolean hasParsed) {
        this.mHasParsed = hasParsed;
    }

    public boolean isParsed() {
        return this.mHasParsed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isMaterialMetaEnable(String packageName) {
        int userId = 0;
        try {
            OplusExtraConfiguration extraConfig = this.mBaseResources.getSystemConfiguration().getOplusExtraConfiguration();
            if (extraConfig != null) {
                userId = Math.max(0, extraConfig.mUserId);
            }
            ApplicationInfo info = ActivityThread.getPackageManager().getApplicationInfo(packageName, 128L, userId);
            if (info == null || info.metaData == null) {
                return false;
            }
            boolean enable = info.metaData.getBoolean(OPLUS_MATERIAL_ENABLE);
            return enable;
        } catch (Exception e) {
            Slog.e(TAG, "isColorMetaEnable exception: " + e.toString());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String getPackageName(String packageName) {
        if (!OplusThemeResources.FRAMEWORK_NAME.equals(packageName) && !"icons".equals(packageName)) {
            if (OplusThemeResources.OPLUS_NAME.equals(packageName) || OplusThemeResources.LOCKSCREEN_PACKAGE.equals(packageName)) {
                return OplusThemeResources.OPLUS_PACKAGE;
            }
            return packageName;
        }
        return OplusThemeResources.FRAMEWORK_PACKAGE;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CharSequence getCharSequence(int i) {
        return (CharSequence) this.mCharSequences.get(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Integer getInt(int id) {
        return (Integer) this.mIntegers.get(id);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasValues() {
        if (this.mIntegers.size() <= 0 && this.mCharSequences.size() <= 0) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void clean(ZipFile file) {
        closeZipFile(file);
        this.mIntegers.clear();
        this.mCharSequences.clear();
        sCacheFiles.clear();
    }

    protected synchronized void closeZipFile(ZipFile file) {
        if (file != null) {
            try {
                file.close();
            } catch (Exception exception) {
                if (DEBUG_THEME) {
                    Log.w(TAG, "OplusThemeZipFile Exception exception: " + exception);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void parseXmlStream(int index, OplusThemeZipFile.ThemeFileInfo themeFileInfo) {
        StringBuilder sb;
        if (themeFileInfo == null) {
            return;
        }
        InputStream in = null;
        BufferedInputStream bufferedinputstream = null;
        try {
            try {
                in = themeFileInfo.mInput;
                XmlPullParser xmlpullparser = XmlPullParserFactory.newInstance().newPullParser();
                bufferedinputstream = new BufferedInputStream(in, 8192);
                if (bufferedinputstream.available() <= 0) {
                    try {
                        bufferedinputstream.close();
                        if (in != null) {
                            in.close();
                            return;
                        }
                        return;
                    } catch (IOException e) {
                        if (DEBUG_THEME) {
                            Log.e(TAG, "IOException happened when parseXmlStream finally, msg = " + e.toString());
                            return;
                        }
                        return;
                    }
                }
                xmlpullparser.setInput(bufferedinputstream, null);
                mergeThemeValues(index, xmlpullparser);
                try {
                    bufferedinputstream.close();
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e2) {
                    e = e2;
                    if (DEBUG_THEME) {
                        sb = new StringBuilder();
                        Log.e(TAG, sb.append("IOException happened when parseXmlStream finally, msg = ").append(e.toString()).toString());
                    }
                }
            } catch (IOException | XmlPullParserException e3) {
                if (DEBUG_THEME) {
                    Log.e(TAG, "Exception happened when parseXmlStream, msg = " + e3.toString());
                }
                if (bufferedinputstream != null) {
                    try {
                        bufferedinputstream.close();
                    } catch (IOException e4) {
                        e = e4;
                        if (DEBUG_THEME) {
                            sb = new StringBuilder();
                            Log.e(TAG, sb.append("IOException happened when parseXmlStream finally, msg = ").append(e.toString()).toString());
                        }
                        return;
                    }
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (Throwable th) {
            if (bufferedinputstream != null) {
                try {
                    bufferedinputstream.close();
                } catch (IOException e5) {
                    if (DEBUG_THEME) {
                        Log.e(TAG, "IOException happened when parseXmlStream finally, msg = " + e5.toString());
                    }
                    throw th;
                }
            }
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x000d. Please report as an issue. */
    private void mergeThemeValues(int index, XmlPullParser xmlpullparser) {
        int count;
        String packageName = null;
        String resourceName = null;
        try {
            int eventType = xmlpullparser.getEventType();
            while (eventType != 1) {
                switch (eventType) {
                    case 0:
                        eventType = xmlpullparser.next();
                    case 1:
                    default:
                        eventType = xmlpullparser.next();
                    case 2:
                        String resourceType = xmlpullparser.getName().trim();
                        if (!TextUtils.isEmpty(resourceType) && (count = xmlpullparser.getAttributeCount()) > 0) {
                            for (int i = 0; i < count; i++) {
                                String attributeName = xmlpullparser.getAttributeName(i).trim();
                                if (attributeName.equals("name")) {
                                    resourceName = xmlpullparser.getAttributeValue(i);
                                } else if (attributeName.equals("package")) {
                                    packageName = xmlpullparser.getAttributeValue(i);
                                }
                            }
                            String resourceValue = xmlpullparser.nextText();
                            if (!TextUtils.isEmpty(resourceName) && !TextUtils.isEmpty(resourceValue)) {
                                if (TextUtils.isEmpty(packageName)) {
                                    if (index != 0 && index <= 2) {
                                        if (index == 1) {
                                            packageName = OplusThemeResources.FRAMEWORK_PACKAGE;
                                        } else if (index == 2) {
                                            packageName = OplusThemeResources.OPLUS_PACKAGE;
                                        }
                                    }
                                    packageName = this.mPackageName;
                                }
                                int resourceId = this.mResources.getIdentifier(resourceName, resourceType, packageName);
                                if (resourceId > 0) {
                                    if (!resourceType.equals(TAG_BOOLEAN)) {
                                        if (!resourceType.equals(TAG_COLOR) && !resourceType.equals(TAG_INTEGER) && !resourceType.equals(TAG_DRAWABLE)) {
                                            if (resourceType.equals(TAG_STRING)) {
                                                if (this.mSupportChar && this.mCharSequences.indexOfKey(resourceId) < 0) {
                                                    this.mCharSequences.put(resourceId, resourceValue);
                                                }
                                            } else if (resourceType.equals(TAG_DIMEN) && this.mSupportInt && this.mIntegers.indexOfKey(resourceId) < 0) {
                                                Integer integer = parseDimension(this.mResources, resourceValue.toString());
                                                if (integer != null) {
                                                    this.mIntegers.put(resourceId, integer);
                                                }
                                            }
                                        }
                                        if (this.mSupportInt && this.mIntegers.indexOfKey(resourceId) < 0) {
                                            try {
                                                this.mIntegers.put(resourceId, Integer.valueOf(XmlUtils.convertValueToUnsignedInt(resourceValue.trim(), 0)));
                                            } catch (NumberFormatException e) {
                                                if (DEBUG_THEME) {
                                                    Log.e(TAG, "mergeThemeValues NumberFormatException happened when loadThemeValues, msg = " + e.getMessage());
                                                }
                                            }
                                        }
                                    } else if (this.mSupportInt && this.mIntegers.indexOfKey(resourceId) < 0) {
                                        if (TRUE.equals(resourceValue.trim())) {
                                            this.mIntegers.put(resourceId, 1);
                                        } else {
                                            this.mIntegers.put(resourceId, 0);
                                        }
                                    }
                                }
                            }
                        }
                        eventType = xmlpullparser.next();
                        break;
                    case 3:
                        eventType = xmlpullparser.next();
                }
            }
        } catch (IOException e2) {
            if (DEBUG_THEME) {
                Log.e(TAG, "mergeThemeValues IOException happened when loadThemeValues, msg = " + e2.getMessage());
            }
        } catch (IndexOutOfBoundsException e3) {
            Log.e(TAG, "mergeThemeValues IndexOutOfBoundsException happened when loadThemeValues, msg = " + e3.getMessage());
        } catch (NullPointerException e4) {
            Log.e(TAG, "mergeThemeValues NullPointerException happened when loadThemeValues, msg = " + e4.getMessage());
        } catch (XmlPullParserException e5) {
            if (DEBUG_THEME) {
                Log.e(TAG, "mergeThemeValues XmlPullParserException happened when loadThemeValues, msg = " + e5.getMessage());
            }
        }
    }

    public InputStream getFileInputStream(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            File xmlFile = new File(path);
            if (!xmlFile.exists()) {
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(xmlFile);
            return fileInputStream;
        } catch (FileNotFoundException e) {
            Slog.e(TAG, "getMaterilaStream e: " + e.toString());
            return null;
        }
    }

    public void readValue(XmlPullParser parser, String child, String filterTag, String attr) throws IOException, XmlPullParserException {
        int resourceId;
        parser.require(2, null, child);
        while (parser.next() != 3) {
            if (parser.getEventType() == 2 && parser.getName().equals(filterTag)) {
                String resourceName = parser.getAttributeValue(null, attr).trim();
                String resourceValue = parser.nextText();
                if (!TextUtils.isEmpty(resourceName) && (resourceId = this.mResources.getIdentifier(resourceName, filterTag, this.mPackageName)) > 0) {
                    try {
                        this.mIntegers.put(resourceId, Integer.valueOf(XmlUtils.convertValueToUnsignedInt(resourceValue.trim(), 0)));
                    } catch (NumberFormatException e) {
                        Slog.e(TAG, "readValue exception, msg = " + e.toString());
                    }
                }
            }
        }
    }

    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0098, code lost:
    
        if (r2 == 0) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x009a, code lost:
    
        r11.mThemeIndexMap.put(r11.mPackageName, java.lang.Integer.valueOf(r2));
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00a5, code lost:
    
        return r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0095, code lost:
    
        if (r3 == null) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getThemeIndexValue(int userId) {
        Integer cacheIndex = this.mThemeIndexMap.get(this.mPackageName);
        if (cacheIndex != null) {
            return cacheIndex.intValue();
        }
        int themeIndex = 0;
        TypedArray array = null;
        try {
            try {
                int themeId = this.mResources.getIdentifier(OPLUS_THEME_IDENTIFIER, TAG_ATTR, this.mPackageName);
                if (themeId <= 0) {
                    themeId = this.mResources.getIdentifier(OPLUS_NXTHEME_IDENTIFIER, TAG_ATTR, this.mPackageName);
                }
                ApplicationInfo info = ActivityThread.getPackageManager().getApplicationInfo(this.mPackageName, 0L, userId);
                if (info != null && themeId > 0) {
                    int[] attr = {themeId};
                    Context appCxt = ActivityThread.currentApplication().getApplicationContext();
                    ContextThemeWrapper themeWrapper = new ContextThemeWrapper(appCxt, info.theme);
                    Resources.Theme theme = appCxt.getResources().newTheme();
                    theme.applyStyle(info.theme, true);
                    themeWrapper.setTheme(theme);
                    array = themeWrapper.getTheme().obtainStyledAttributes(attr);
                    themeIndex = array.getInteger(0, 0);
                }
            } catch (Exception e) {
                Slog.e(TAG, "getThemeIndexValue exception, msg = " + e.toString());
            }
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    private Integer parseDimension(ResourcesImpl res, String value) {
        int unitPos;
        int fractionPos;
        int radix;
        int shift;
        int unitType;
        int intPos = -4;
        int dotPos = -3;
        int fractionPos2 = -2;
        int i = 0;
        while (true) {
            if (i >= value.length()) {
                i = -1;
                unitPos = fractionPos2;
                fractionPos = dotPos;
                break;
            }
            char c = value.charAt(i);
            if (intPos == -4 && c >= '0' && c <= '9') {
                intPos = i;
            }
            if (dotPos == -3 && c == '.') {
                dotPos = i;
            }
            if (dotPos != -3 && c >= '0' && c <= '9') {
                fractionPos2 = i;
            }
            if (c < 'a' || c > 'z') {
                i++;
            } else {
                unitPos = fractionPos2;
                fractionPos = dotPos;
                break;
            }
        }
        if (i == -1 || ((fractionPos >= unitPos && unitPos != -2) || unitPos >= i)) {
            return null;
        }
        try {
            float f = Float.parseFloat(value.substring(0, i));
            boolean neg = f < 0.0f;
            if (neg) {
                f = -f;
            }
            long bits = (8388608.0f * f) + 0.5f;
            if ((8388607 & bits) == 0) {
                radix = 0;
                shift = 23;
            } else if (((-8388608) & bits) == 0) {
                radix = 3;
                shift = 0;
            } else if ((FFFFFFFF80000000 & bits) == 0) {
                radix = 2;
                shift = 8;
            } else if ((FFFFFF8000000000 & bits) == 0) {
                radix = 1;
                shift = 16;
            } else {
                radix = 0;
                shift = 23;
            }
            String unit = value.substring(i);
            if (unit.equals("px")) {
                unitType = 0;
            } else {
                if (!unit.equals("dp") && !unit.equals("dip")) {
                    if (unit.equals("sp")) {
                        unitType = 2;
                    } else if (unit.equals("pt")) {
                        unitType = 3;
                    } else if (unit.equals("in")) {
                        unitType = 4;
                    } else {
                        if (!unit.equals("mm")) {
                            return null;
                        }
                        unitType = 5;
                    }
                }
                unitType = 1;
            }
            int mantissa = (int) ((bits >> shift) & 16777215);
            if (neg) {
                mantissa = (-mantissa) & 16777215;
            }
            return Integer.valueOf((mantissa << 8) | (radix << 4) | (unitType << 0));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
