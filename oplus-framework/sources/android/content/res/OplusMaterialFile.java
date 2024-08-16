package android.content.res;

import android.text.TextUtils;
import android.util.Slog;
import com.oplus.deepthinker.OplusDeepThinkerManager;
import com.oplus.os.OplusEnvironment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import oplus.content.res.OplusExtraConfiguration;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusMaterialFile extends OplusBaseFile {
    protected static final int BUILDER_LEN = 64;
    protected static final String CUSTOM_SINGLE_NIGHT_XML = "ux_custom_color_night.xml";
    protected static final String CUSTOM_SINGLE_NORMAL_XML = "ux_custom_color.xml";
    protected static final String MATERIAL_COMPANY_NIGHT_XML = "coui_theme_color_company_night.xml";
    protected static final String MATERIAL_COMPANY_XML = "coui_theme_color_company.xml";
    protected static final String MATERIAL_ONLINE_NIGHT_XML = "coui_theme_color_night_online.xml";
    protected static final String MATERIAL_ONLINE_NORMAL_XML = "coui_theme_color_online.xml";
    protected static final String MATERIAL_WALLPAPER_NIGHT_XML = "coui_theme_color_wallpaper_night.xml";
    protected static final String MATERIAL_WALLPAPER_XML = "coui_theme_color_wallpaper.xml";
    protected static final int OPLUS_COMPANY_CUSTOM_FLAG = 524288;
    protected static final String OPLUS_COMPANY_CUSTOM_XML_PATH = "" + OplusEnvironment.getMyCompanyDirectory() + "/media/res/";
    protected static final int OPLUS_CUSTOM_FLAG = 131072;
    protected static final int OPLUS_MATERIAL_COLOR_GROUP = 65535;
    protected static final String OPLUS_MATERIAL_XML_PATH = "data/oplus/uxres/uxcolor/";
    protected static final int OPLUS_ONLINE_FLAG = 1048576;
    protected static final int OPLUS_WALLPAPER_FLAG = 262144;

    public OplusMaterialFile(String packageName, IResourcesImplExt baseResources) {
        super(packageName, baseResources, true, true, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static OplusMaterialFile getMaterialFile(String packageName, IResourcesImplExt baseResources) {
        if (TextUtils.isEmpty(packageName) || baseResources == null) {
            return null;
        }
        WeakReference weakReference = sCacheFiles.get(packageName);
        OplusMaterialFile materialFile = null;
        if (weakReference != null) {
            materialFile = (OplusMaterialFile) weakReference.get();
        }
        if (materialFile != null) {
            materialFile.setResource(baseResources);
            return materialFile;
        }
        OplusMaterialFile materialFile2 = new OplusMaterialFile(packageName, baseResources);
        if (!materialFile2.isMaterialMetaEnable(packageName)) {
            return null;
        }
        sCacheFiles.put(packageName, new WeakReference<>(materialFile2));
        return materialFile2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clears() {
        this.mIntegers.clear();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean loadMaterialColor() {
        boolean hasValues = false;
        try {
            boolean night = isNightMode(this.mBaseResources);
            OplusExtraConfiguration extraConfig = this.mBaseResources.getSystemConfiguration().getOplusExtraConfiguration();
            if (extraConfig == null) {
                return false;
            }
            Long color = Long.valueOf(extraConfig.mMaterialColor);
            int userId = extraConfig.mUserId;
            InputStream inputStream = null;
            InputStream mergeStream = null;
            int groupIndex = -1;
            int themeIndex = -1;
            boolean isGroup = true;
            StringBuilder iBuilder = new StringBuilder(64);
            StringBuilder mBuilder = new StringBuilder(64);
            iBuilder.append(OPLUS_MATERIAL_XML_PATH);
            mBuilder.append(OPLUS_MATERIAL_XML_PATH);
            if ((color.longValue() & 131072) == 131072) {
                isGroup = false;
                if (userId > 0) {
                    iBuilder.append(userId).append(File.separator).append(CUSTOM_SINGLE_NORMAL_XML);
                } else {
                    iBuilder.append(CUSTOM_SINGLE_NORMAL_XML);
                }
                inputStream = super.getFileInputStream(iBuilder.toString());
                if (night) {
                    if (userId > 0) {
                        mBuilder.append(userId).append(File.separator).append(CUSTOM_SINGLE_NIGHT_XML);
                    } else {
                        mBuilder.append(CUSTOM_SINGLE_NIGHT_XML);
                    }
                    mergeStream = super.getFileInputStream(mBuilder.toString());
                }
            } else if ((color.longValue() & 1048576) == 1048576) {
                themeIndex = super.getThemeIndexValue(extraConfig.mUserId);
                groupIndex = (int) (extraConfig.mMaterialColor & 65535);
                inputStream = super.getFileInputStream(iBuilder.append(MATERIAL_ONLINE_NORMAL_XML).toString());
                mergeStream = night ? super.getFileInputStream(mBuilder.append(MATERIAL_ONLINE_NIGHT_XML).toString()) : null;
            } else if ((color.longValue() & 262144) == 262144) {
                if (userId > 0) {
                    iBuilder.append(userId).append(File.separator).append(MATERIAL_WALLPAPER_XML);
                } else {
                    iBuilder.append(MATERIAL_WALLPAPER_XML);
                }
                inputStream = super.getFileInputStream(iBuilder.toString());
                if (night) {
                    if (userId > 0) {
                        mBuilder.append(userId).append(File.separator).append(MATERIAL_WALLPAPER_NIGHT_XML);
                    } else {
                        mBuilder.append(MATERIAL_WALLPAPER_NIGHT_XML);
                    }
                    mergeStream = super.getFileInputStream(mBuilder.toString());
                }
                themeIndex = super.getThemeIndexValue(extraConfig.mUserId);
            } else if ((color.longValue() & 524288) == 524288) {
                if (night) {
                    inputStream = super.getFileInputStream(OPLUS_COMPANY_CUSTOM_XML_PATH + MATERIAL_COMPANY_XML);
                } else {
                    mergeStream = super.getFileInputStream(OPLUS_COMPANY_CUSTOM_XML_PATH + MATERIAL_COMPANY_NIGHT_XML);
                }
                themeIndex = super.getThemeIndexValue(extraConfig.mUserId);
            }
            if (inputStream != null) {
                hasValues = true;
                parserMaterialStream(groupIndex, themeIndex, inputStream, isGroup);
            }
            if (mergeStream != null) {
                hasValues = true;
                parserMaterialStream(groupIndex, themeIndex, mergeStream, isGroup);
                return true;
            }
            return hasValues;
        } catch (Exception e) {
            Slog.e("OplusBaseFile", "loadMaterialColor e: " + e.toString());
            return hasValues;
        }
    }

    private void parserMaterialStream(int groupIndex, int themeIndex, InputStream in, boolean isGroup) {
        StringBuilder sb;
        BufferedInputStream bufferedinputstream = null;
        try {
            try {
                XmlPullParser xmlpullparser = XmlPullParserFactory.newInstance().newPullParser();
                BufferedInputStream bufferedinputstream2 = new BufferedInputStream(in, 8192);
                xmlpullparser.setInput(bufferedinputstream2, null);
                if (!isGroup) {
                    mergeCustomValues(groupIndex, xmlpullparser);
                } else {
                    mergeGroupValues(groupIndex, themeIndex, xmlpullparser);
                }
                try {
                    bufferedinputstream2.close();
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e = e;
                    sb = new StringBuilder();
                    Slog.e("OplusBaseFile", sb.append("IOException happened when parserMaterialStream, msg = ").append(e.toString()).toString());
                }
            } catch (XmlPullParserException e2) {
                Slog.e("OplusBaseFile", "IOException happened when parserMaterialStream, msg = " + e2.toString());
                if (0 != 0) {
                    try {
                        bufferedinputstream.close();
                    } catch (IOException e3) {
                        e = e3;
                        sb = new StringBuilder();
                        Slog.e("OplusBaseFile", sb.append("IOException happened when parserMaterialStream, msg = ").append(e.toString()).toString());
                    }
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    bufferedinputstream.close();
                } catch (IOException e4) {
                    Slog.e("OplusBaseFile", "IOException happened when parserMaterialStream, msg = " + e4.toString());
                    throw th;
                }
            }
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    private void mergeCustomValues(int index, XmlPullParser xmlpullparser) {
        try {
            xmlpullparser.nextTag();
            super.readValue(xmlpullparser, "resources", "color", "name");
        } catch (IOException e) {
            Slog.e("OplusBaseFile", "mergeCustomValues IOException, msg = " + e.toString());
        } catch (IllegalArgumentException | XmlPullParserException e2) {
            Slog.e("OplusBaseFile", "mergeCustomValues XmlPullParserException, msg = " + e2.toString());
        }
    }

    private void mergeGroupValues(int groupIndex, int themeIndex, XmlPullParser parser) {
        try {
            parser.nextTag();
            readRoot(parser, "resources", groupIndex, themeIndex);
        } catch (Exception e) {
            Slog.e("OplusBaseFile", "mergeOnlineValues exception, msg = " + e.toString());
        }
    }

    private void readRoot(XmlPullParser parser, String root, int groupIndex, int themeIndex) throws XmlPullParserException, IOException {
        parser.require(2, null, root);
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String name = parser.getName();
                if (name.equals("group")) {
                    readChild(parser, "group", groupIndex, themeIndex);
                } else {
                    skip(parser);
                }
            }
        }
    }

    private void readChild(XmlPullParser parser, String child, int groupIndex, int themeIndex) throws XmlPullParserException, IOException, NumberFormatException {
        parser.require(2, null, child);
        int index = -1;
        int id = 0;
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String name = parser.getName();
                if (name.equals("index")) {
                    index = readIndex(parser);
                } else if (name.equals("child") && (index == groupIndex || groupIndex == -1)) {
                    String ids = parser.getAttributeValue(null, OplusDeepThinkerManager.ARG_ID);
                    if (!TextUtils.isEmpty(ids)) {
                        id = Integer.parseInt(ids);
                    }
                    if (themeIndex >= 0 || id == 0) {
                        super.readValue(parser, "child", "color", "name");
                    } else {
                        super.skip(parser);
                    }
                } else {
                    super.skip(parser);
                }
            }
        }
    }

    private int readIndex(XmlPullParser parser) throws IOException, XmlPullParserException {
        try {
            return Integer.parseInt(parser.nextText());
        } catch (NumberFormatException e) {
            Slog.e("OplusBaseFile", "readIndex exception, msg = " + e.toString());
            return -1;
        }
    }
}
