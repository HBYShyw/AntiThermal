package com.oplus.thermalcontrol.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import b6.LocalLog;
import com.oplus.thermalcontrol.ThermalControlConfig;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: classes2.dex */
public class ThermalWindowConfigInfo {
    public static final String ATTR_NAME = "name";
    public static final String ATTR_POLICY = "policy";
    public static final String ATTR_RESTRICT_PACKAGES = "restrict_packages";
    public static final String ATTR_TYPE_RESTRICT_ACTIVITIES = "restrict_activities";
    public static final String ATTR_TYPE_RESTRICT_WINDOWS = "restrict_windows";
    public static final int POLICY_FORCE_STOP = 2;
    public static final int POLICY_KILL = 1;
    public static final int POLICY_NONE = 0;
    public static final String TAG = "ThermalWindowConfigInfo";
    public static final String TAG_ARRAY = "array";
    public static final String ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_PATH = "sys_thermal_zoom_window_restrict_list.xml";
    public static final String ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_ROOT_ITEM = "sys_thermal_zoom_window_restrict_list";
    private String mConfigVersion = "2000010101";
    private String mDefaultConfigVersion = "2000010101";
    private String mRusConfigVersion = "2000010101";
    private final ArrayMap<String, Integer> mRestrictPackages = new ArrayMap<>();
    private final ArrayMap<String, Integer> mRestrictActivities = new ArrayMap<>();
    private final ArrayMap<String, Integer> mRestrictWindows = new ArrayMap<>();
    private final Object mWindowConfigLock = new Object();

    private long getDefaultVersion() {
        String parseVersionForDocument;
        Document defaultConfigDocument = ThermalConfigUtil.getDefaultConfigDocument(ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_PATH);
        if (defaultConfigDocument == null || (parseVersionForDocument = ThermalConfigUtil.parseVersionForDocument(defaultConfigDocument, ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_ROOT_ITEM)) == null) {
            return 0L;
        }
        this.mDefaultConfigVersion = parseVersionForDocument;
        return Long.parseLong(parseVersionForDocument);
    }

    private void initDefaultConfig() {
        Document defaultConfigDocument = ThermalConfigUtil.getDefaultConfigDocument(ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_PATH);
        if (defaultConfigDocument == null) {
            LocalLog.b(TAG, "addDefaultConfig, null doc!");
        } else {
            parseConfigForDocument(defaultConfigDocument);
        }
    }

    private void parseConfigForDocument(Document document) {
        Element element;
        String tagName;
        Element documentElement = document.getDocumentElement();
        if (documentElement == null) {
            LocalLog.b(TAG, "parseConfigForDocument, root == null");
            return;
        }
        String trim = documentElement.getTagName().trim();
        if (ThermalControlConfig.XML_DBG) {
            LocalLog.d(TAG, "loadApListFromXml, xml root is " + trim);
        }
        NodeList childNodes = documentElement.getChildNodes();
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null) {
                if (tagName.equals("version")) {
                    this.mConfigVersion = element.getTextContent();
                    LocalLog.a(TAG, "mConfigVersion=" + element.getTextContent());
                } else if (tagName.equals(TAG_ARRAY)) {
                    parseRestrictList(element);
                }
            }
        }
    }

    private void parseConfigFromRUS(String str) {
        Document configDocumentFromXml = ThermalConfigUtil.getConfigDocumentFromXml(str);
        if (configDocumentFromXml == null) {
            LocalLog.b(TAG, "loadApListFromXml, null doc!");
        } else {
            parseConfigForDocument(configDocumentFromXml);
        }
    }

    private void parseRestrictList(Element element) {
        ArrayMap<String, Integer> arrayMap;
        String attribute = element.getAttribute("name");
        attribute.hashCode();
        char c10 = 65535;
        switch (attribute.hashCode()) {
            case -1314518704:
                if (attribute.equals(ATTR_TYPE_RESTRICT_ACTIVITIES)) {
                    c10 = 0;
                    break;
                }
                break;
            case 1943504896:
                if (attribute.equals(ATTR_TYPE_RESTRICT_WINDOWS)) {
                    c10 = 1;
                    break;
                }
                break;
            case 1985355536:
                if (attribute.equals(ATTR_RESTRICT_PACKAGES)) {
                    c10 = 2;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                arrayMap = this.mRestrictActivities;
                break;
            case 1:
                arrayMap = this.mRestrictWindows;
                break;
            case 2:
                arrayMap = this.mRestrictPackages;
                break;
            default:
                arrayMap = null;
                break;
        }
        if (arrayMap == null) {
            LocalLog.b(TAG, "parseRestrictList: restrictList is null, Type:" + attribute);
            return;
        }
        if (ThermalControlConfig.XML_DBG) {
            LocalLog.l(TAG, "parseRestrictList Type: " + attribute);
        }
        parseRestrictListItems(element, arrayMap);
    }

    private void parseRestrictListItems(Element element, ArrayMap<String, Integer> arrayMap) {
        Element element2;
        String tagName;
        synchronized (this.mWindowConfigLock) {
            arrayMap.clear();
        }
        NodeList childNodes = element.getChildNodes();
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if ((item instanceof Element) && (tagName = (element2 = (Element) item).getTagName()) != null && tagName.equals(ThermalBaseConfig.Item.ATTR_VALUE)) {
                if (ThermalControlConfig.XML_DBG) {
                    LocalLog.l(TAG, " " + element2.getTextContent() + ", " + element2.getAttribute(ATTR_POLICY));
                }
                synchronized (this.mWindowConfigLock) {
                    if (!arrayMap.containsKey(element2.getTextContent())) {
                        arrayMap.put(element2.getTextContent(), Integer.valueOf(ThermalConfigUtil.StringToInt(element2.getAttribute(ATTR_POLICY)).orElse(0).intValue()));
                    }
                }
            }
        }
    }

    private long parseVersionFromRUS(String str) {
        String parseVersionForDocument;
        Document configDocumentFromXml = ThermalConfigUtil.getConfigDocumentFromXml(str);
        if (configDocumentFromXml == null || (parseVersionForDocument = ThermalConfigUtil.parseVersionForDocument(configDocumentFromXml, ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_ROOT_ITEM)) == null) {
            return 0L;
        }
        this.mRusConfigVersion = parseVersionForDocument;
        return Long.parseLong(parseVersionForDocument);
    }

    public int getRestrictPolicy(String str, String str2, int i10) {
        synchronized (this.mWindowConfigLock) {
            if (i10 == 2038) {
                if (TextUtils.isEmpty(str2)) {
                    if (this.mRestrictPackages.containsKey(str)) {
                        return this.mRestrictPackages.get(str).intValue();
                    }
                    return 0;
                }
            }
            if (this.mRestrictActivities.containsKey(str2)) {
                return this.mRestrictActivities.get(str2).intValue();
            }
            return 0;
        }
    }

    public void initConfig(Context context) {
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_ROOT_ITEM);
        if (dataFromProvider == null || TextUtils.isEmpty(dataFromProvider)) {
            initDefaultConfig();
        } else if (parseVersionFromRUS(dataFromProvider) > getDefaultVersion()) {
            parseConfigFromRUS(dataFromProvider);
        } else {
            initDefaultConfig();
        }
    }

    public void noteThermalWindowConfigChange(Context context) {
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, ZOOM_WINDOW_RESTRICT_LIST_XML_FILE_ROOT_ITEM);
        if (dataFromProvider == null || TextUtils.isEmpty(dataFromProvider)) {
            return;
        }
        if (parseVersionFromRUS(dataFromProvider) > getDefaultVersion()) {
            parseConfigFromRUS(dataFromProvider);
            LocalLog.l(TAG, "Use Config RUS!");
        } else {
            initDefaultConfig();
            LocalLog.l(TAG, "Use Config local!");
        }
    }

    public String toString() {
        return "zoom_window_config_version=" + this.mConfigVersion + "\nzoom_window_restrict_packages=" + this.mRestrictPackages + "\nzoom_window_restrict_activities=" + this.mRestrictActivities + "\nzoom_window_restrict_windows=" + this.mRestrictWindows + "\n";
    }
}
