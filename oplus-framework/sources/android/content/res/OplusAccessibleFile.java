package android.content.res;

import android.content.res.OplusThemeZipFile;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import com.google.android.collect.Sets;
import com.oplus.deepthinker.OplusDeepThinkerManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.zip.ZipFile;
import oplus.content.res.OplusExtraConfiguration;
import oplus.util.OplusDisplayUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusAccessibleFile extends OplusBaseFile {
    private static final int INDEX_COLORS = 0;
    private static final int INDEX_XHDPI = 1;
    private static final int INDEX_XXHDPI = 2;
    private static final String ACCESSIBLE_FILE_PATH = Environment.getSystemExtDirectory() + "/etc/coui_theme_color_accessible.xml";
    private static final HashSet<String> PKG_FOR_WHITE = Sets.newHashSet(new String[]{"com.android.systemui", "com.android.settings", "com.android.launcher"});

    public OplusAccessibleFile(String packageName, IResourcesImplExt baseResources) {
        super(packageName, baseResources, true, true, true);
    }

    public boolean initValue() {
        clearCache(null);
        loadAssetValues();
        return true;
    }

    public void clearCache(ZipFile zipFile) {
        clean(zipFile);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static OplusAccessibleFile getAssetFile(String packageName, IResourcesImplExt baseResources) {
        if (TextUtils.isEmpty(packageName) || baseResources == null || PKG_FOR_WHITE.contains(packageName)) {
            return null;
        }
        String name = getPackageName(packageName);
        String key = name + ":/accessible";
        WeakReference<OplusBaseFile> weakReference = sCacheFiles.get(key);
        OplusAccessibleFile assetsFile = null;
        if (weakReference != null) {
            assetsFile = (OplusAccessibleFile) weakReference.get();
        }
        if (assetsFile != null) {
            assetsFile.setResource(baseResources);
            return assetsFile;
        }
        OplusAccessibleFile assetsFile2 = new OplusAccessibleFile(name, baseResources);
        if (!assetsFile2.isMaterialMetaEnable(packageName)) {
            return null;
        }
        sCacheFiles.put(key, new WeakReference<>(assetsFile2));
        return assetsFile2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OplusThemeZipFile.ThemeFileInfo getAssetInputStream(int index, String path) {
        InputStream is;
        if (TextUtils.isEmpty(path) || path.endsWith(".xml")) {
            return null;
        }
        String path2 = "accessible/" + OplusDisplayUtils.getDrawbleDensityFolder(sDensity) + path.substring(path.lastIndexOf("/"));
        InputStream is2 = getAssetPathStream(this.mResources.getAssets(), path2);
        if (is2 != null) {
            return new OplusThemeZipFile.ThemeFileInfo(is2, 0L);
        }
        for (int i = 0; i < sDensities.length; i++) {
            String temp = "accessible/" + OplusDisplayUtils.getDrawbleDensityFolder(sDensities[i]) + path2.substring(path2.lastIndexOf("/"));
            if (!path2.equalsIgnoreCase(temp) && (is = getAssetPathStream(this.mResources.getAssets(), temp)) != null) {
                OplusThemeZipFile.ThemeFileInfo themeFileInfo = new OplusThemeZipFile.ThemeFileInfo(is, 0L);
                if (sDensities[i] <= 1) {
                    return themeFileInfo;
                }
                themeFileInfo.mDensity = sDensities[i];
                return themeFileInfo;
            }
        }
        return null;
    }

    protected boolean hasDrawables() {
        AssetManager assets = this.mResources.getAssets();
        if (assets == null) {
            return false;
        }
        for (int i = 0; i < sDensities.length; i++) {
            try {
                String path = "accessible/" + OplusDisplayUtils.getDrawbleDensityFolder(sDensities[i]);
                String[] drawables = assets.list(path);
                if (drawables.length > 0) {
                    return true;
                }
            } catch (IOException e) {
                if (!DEBUG_THEME) {
                    return false;
                }
                Log.e("OplusBaseFile", "hasAssetDrawables: asset list exception " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean hasAssetValues() {
        return new File(ACCESSIBLE_FILE_PATH).exists();
    }

    private void loadAssetValues() {
        OplusExtraConfiguration extraConfig;
        InputStream inputStream;
        if (!hasAssetValues() || (extraConfig = this.mBaseResources.getSystemConfiguration().getOplusExtraConfiguration()) == null) {
            return;
        }
        int userId = extraConfig.mUserId;
        int themeIndex = super.getThemeIndexValue(userId);
        if (themeIndex <= 0 || (inputStream = super.getFileInputStream(ACCESSIBLE_FILE_PATH)) == null) {
            return;
        }
        parserAccessibleStream(themeIndex, inputStream);
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x009f A[Catch: all -> 0x0099, IOException -> 0x009b, TRY_LEAVE, TryCatch #6 {IOException -> 0x009b, blocks: (B:43:0x0095, B:36:0x009f), top: B:42:0x0095, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0095 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private synchronized void parserAccessibleStream(int themeIndex, InputStream in) {
        String str;
        String str2;
        BufferedInputStream bufferedinputstream = null;
        try {
            try {
                XmlPullParser xmlpullparser = XmlPullParserFactory.newInstance().newPullParser();
                bufferedinputstream = new BufferedInputStream(in, 8192);
                xmlpullparser.setInput(bufferedinputstream, null);
                xmlpullparser.nextTag();
                readRoot(xmlpullparser, "resources", themeIndex);
            } catch (IOException | IllegalStateException | NumberFormatException | XmlPullParserException e) {
                try {
                    Slog.w("OplusBaseFile", "Exception happened when parserAccessibleStream, msg = " + e.toString());
                    if (bufferedinputstream != null) {
                        try {
                            bufferedinputstream.close();
                        } catch (IOException e2) {
                            str = "OplusBaseFile";
                            str2 = "IOException happened when parserAccessibleStream close, msg = " + e2.toString();
                            Slog.w(str, str2);
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    if (bufferedinputstream != null) {
                        try {
                            bufferedinputstream.close();
                        } catch (IOException e3) {
                            Slog.w("OplusBaseFile", "IOException happened when parserAccessibleStream close, msg = " + e3.toString());
                            throw th;
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (bufferedinputstream != null) {
                }
                if (in != null) {
                }
                throw th;
            }
            try {
                bufferedinputstream.close();
                if (in != null) {
                    in.close();
                }
            } catch (IOException e4) {
                str = "OplusBaseFile";
                str2 = "IOException happened when parserAccessibleStream close, msg = " + e4.toString();
                Slog.w(str, str2);
            }
        } catch (Throwable th3) {
            throw th3;
        }
    }

    private void readRoot(XmlPullParser parser, String root, int themeIndex) throws XmlPullParserException, IOException {
        parser.require(2, null, root);
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String name = parser.getName();
                if (name.equals("group")) {
                    readChild(parser, "group", themeIndex);
                } else {
                    skip(parser);
                }
            }
        }
    }

    private void readChild(XmlPullParser parser, String child, int themeIndex) throws XmlPullParserException, IOException, NumberFormatException {
        parser.require(2, null, child);
        int id = 0;
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String name = parser.getName();
                if (name.equals("child")) {
                    String ids = parser.getAttributeValue(null, OplusDeepThinkerManager.ARG_ID);
                    if (!TextUtils.isEmpty(ids)) {
                        id = Integer.parseInt(ids);
                    }
                    if (themeIndex == id) {
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

    private InputStream getAssetPathStream(AssetManager assets, String path) {
        if (assets == null) {
            return null;
        }
        try {
            InputStream is = assets.open(path);
            return is;
        } catch (IOException e) {
            return null;
        }
    }
}
