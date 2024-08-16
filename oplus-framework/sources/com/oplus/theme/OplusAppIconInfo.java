package com.oplus.theme;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class OplusAppIconInfo {
    public static final String ALL_APPS = "allApps.xml";
    private static final String TAG = "OplusAppIconInfo";
    private static String mCurrentTag = null;
    private static ArrayList<String> mAllIconNames = new ArrayList<>();
    private static ArrayList<String> mAllPackageNames = new ArrayList<>();
    private static Map<String, String> sDiffPackage = new HashMap();
    private static boolean sParsered = false;

    static {
        Resources res = Resources.getSystem();
        String[] appIconsInfo = res.getStringArray(201785375);
        if (appIconsInfo != null) {
            for (String iconInfo : appIconsInfo) {
                if (!TextUtils.isEmpty(iconInfo)) {
                    String[] iconPkgArray = TextUtils.split(iconInfo, "/");
                    if (iconPkgArray.length == 2) {
                        sDiffPackage.put(iconPkgArray[1], iconPkgArray[0]);
                    }
                }
            }
        }
        sDiffPackage.put("com.android.stk", "ic_launcher_stk.png");
        sDiffPackage.put("com.finshell.wallet", "ic_launcher_wallet.png");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class IconXmlHandler extends DefaultHandler {
        IconXmlHandler() {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            OplusAppIconInfo.mCurrentTag = localName;
            if (OplusAppIconInfo.mCurrentTag.equalsIgnoreCase("icon")) {
                String iconName = attributes.getValue("name");
                if (iconName != null) {
                    OplusAppIconInfo.mAllIconNames.add(iconName);
                } else {
                    OplusAppIconInfo.mAllIconNames.add("no_icon_name");
                }
                String packageName = attributes.getValue("package");
                if (packageName != null) {
                    OplusAppIconInfo.mAllPackageNames.add(packageName);
                } else {
                    OplusAppIconInfo.mAllIconNames.add("no_package_name");
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String name) throws SAXException {
            OplusAppIconInfo.mCurrentTag = null;
        }
    }

    public static void parseXml(InputStream inStream) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser saxParser = spf.newSAXParser();
        saxParser.parse(inStream, new IconXmlHandler());
        inStream.close();
    }

    public static boolean parseIconXml() {
        return false;
    }

    public static synchronized boolean parseIconXmlForUser(int userId) {
        synchronized (OplusAppIconInfo.class) {
            if (sParsered) {
                return true;
            }
            String defaultXml = OplusThemeUtil.getDefaultThemePath() + ALL_APPS;
            ZipFile param = null;
            InputStream input = null;
            mAllIconNames.clear();
            mAllPackageNames.clear();
            if (!OplusThirdPartUtil.mIsDefaultTheme) {
                boolean checkDiff = false;
                try {
                    try {
                        String thirdIconPath = OplusThirdPartUtil.sThemePath + "icons";
                        File file = new File(thirdIconPath);
                        if (file.exists()) {
                            param = new ZipFile(thirdIconPath);
                            ZipEntry entry = param.getEntry(ALL_APPS);
                            if (entry == null) {
                                input = new FileInputStream(defaultXml);
                            } else {
                                input = param.getInputStream(entry);
                                checkDiff = true;
                            }
                        } else {
                            input = new FileInputStream(defaultXml);
                        }
                        parseXml(input);
                        input.close();
                        if (param != null) {
                            param.close();
                        }
                        if (checkDiff) {
                            checkDiffPackages();
                        }
                        return true;
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (Exception e) {
                                Log.w(TAG, "input param close error on third theme.");
                            }
                        }
                        if (param != null) {
                            param.close();
                        }
                        sParsered = true;
                    }
                } catch (Exception e2) {
                    Log.w(TAG, "parseIconXml error on third theme.");
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Exception e3) {
                            Log.w(TAG, "input param close error on third theme.");
                            return false;
                        }
                    }
                    if (param != null) {
                        param.close();
                    }
                    sParsered = true;
                    return false;
                }
            }
            try {
                try {
                    input = new FileInputStream(defaultXml);
                    parseXml(input);
                    input.close();
                    try {
                        input.close();
                        sParsered = true;
                    } catch (Exception e4) {
                        Log.w(TAG, "input close error on default theme.");
                    }
                    return true;
                } finally {
                    if (input != null) {
                        try {
                        } catch (Exception e5) {
                        }
                    }
                }
            } catch (Exception e6) {
                Log.w(TAG, "parseIconXml error on default theme.");
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e7) {
                        Log.w(TAG, "input close error on default theme.");
                        return false;
                    }
                }
                sParsered = true;
                return false;
            }
        }
    }

    public static boolean isThirdPart(ApplicationInfo ai) {
        if (mAllPackageNames.contains(ai.packageName)) {
            return false;
        }
        return true;
    }

    public static boolean isThirdPartbyIconName(String iconName) {
        if (mAllIconNames.contains(iconName)) {
            return false;
        }
        return true;
    }

    public static int getAppsNumbers() {
        return mAllPackageNames.size();
    }

    public static int indexOfPackageName(String name) {
        return mAllPackageNames.indexOf(name);
    }

    public static String getPackageName(int index) {
        return mAllPackageNames.get(index);
    }

    public static int indexOfIconName(String name) {
        return mAllIconNames.indexOf(name);
    }

    public static String getIconName(int index) {
        if (index < mAllIconNames.size()) {
            return mAllIconNames.get(index);
        }
        return "";
    }

    public static synchronized void reset() {
        synchronized (OplusAppIconInfo.class) {
            sParsered = false;
        }
    }

    private static void checkDiffPackages() {
        Set<Map.Entry<String, String>> entrySet = sDiffPackage.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry != null) {
                String key = entry.getKey();
                String value = entry.getValue();
                int packIndex = mAllPackageNames.indexOf(key);
                int iconIndex = mAllIconNames.indexOf(value);
                if (iconIndex < 0 || packIndex < 0) {
                    mAllPackageNames.add(key);
                    mAllIconNames.add(value);
                }
            }
        }
    }
}
