package com.oplus.thermalcontrol;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;
import b6.LocalLog;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.oplus.thermalcontrol.config.ThermalConfigUtil;
import com.oplus.thermalcontrol.config.ThermalWindowConfigInfo;
import com.oplus.thermalcontrol.config.feature.CpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.GpuLevelConfig;
import com.oplus.thermalcontrol.config.feature.HeatSourceLevelConfig;
import com.oplus.thermalcontrol.config.feature.HeatSourceOffsetConfig;
import com.oplus.thermalcontrol.config.feature.TemperatureLevelConfig;
import com.oplus.thermalcontrol.config.feature.TsensorConfig;
import com.oplus.thermalcontrol.config.feature.TsensorExceptScenesConfig;
import com.oplus.thermalcontrol.config.policy.ThermalAmbientPolicy;
import com.oplus.thermalcontrol.config.policy.ThermalPolicy;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import y5.AppFeature;

/* loaded from: classes2.dex */
public class ThermalControlConfig {
    public static final int AGING_HIGH_TEMP_SAFETY_LEVEL_DEFAULT = 19;
    public static final int AMBIENT_MODE_DISABLE = 0;
    public static final int AMBIENT_OFFSET_MODE = 1;
    public static final int AMBIENT_TEMP_LOW_LIMIT_DEFAULT = 22;
    public static final int AMBIENT_TEMP_UPPER_LIMIT_DEFAULT = 28;
    public static final int AMBIENT_XML_MODE = 2;
    public static final int CATEGORY_TEMP_LEVEL_LIMIT_DEFAULT = 3;
    public static final int CATEGORY_TYPE_GAME = 4;
    public static final String COLUMN_NAME_XML = "xml";
    public static final String CONFIG_TYPE_CATEGORY = "category";
    public static final int CONFIG_TYPE_CATEGORY_INDEX = 4;
    public static final String CONFIG_TYPE_CONFIG = "config";
    public static final String CONFIG_TYPE_DEFAULT = "default";
    public static final int CONFIG_TYPE_DEFAULT_INDEX = 5;
    public static final String CONFIG_TYPE_FEATURE = "feature";
    public static final String CONFIG_TYPE_FEATURE_CONFIG = "featureConfigItem";
    public static final String CONFIG_TYPE_GLOBAL_POLICY = "globalPolicy";
    public static final String CONFIG_TYPE_POLICY_CONFIG = "thermalPolicyConfigItem";
    public static final String CONFIG_TYPE_SAFETY_TEST = "safetyTest";
    public static final int CONFIG_TYPE_SAFETY_TEST_INDEX = 1;
    public static final String CONFIG_TYPE_SCENE = "scene";
    public static final int CONFIG_TYPE_SCENE_INDEX = 3;
    public static final String CONFIG_TYPE_SCREEN_OFF = "screenOff";
    public static final int CONFIG_TYPE_SCREEN_OFF_INDEX = 2;
    public static final String CONFIG_TYPE_SPECIFIC = "specific";
    public static final String CONFIG_TYPE_SPECIFIC_ENV = "specificEnv";
    public static final String CONFIG_TYPE_SPECIFIC_SCENE = "specificScene";
    public static final int CONFIG_TYPE_UNKOWN_INDEX = -1;
    public static final String CONFIG_TYPE_VERSION_CONFIG = "version";
    public static final String FILE_PATH_BIG_BALL = "/my_bigball/etc/temperature_profile";
    public static final String FILE_PATH_ODM = "/odm/etc/temperature_profile";
    public static final int FLOAT_WINDOW_ACQUIRE_LEVEL_DEFAULT = 5;
    public static final int FLOAT_WINDOW_REFRESH_LEVEL_DEFAULT = 10;
    private static final String FOLDING_THERMAL_CONTROL_XML_FILE_PATH = "sys_thermal_control_config_folding.xml";
    private static final String FOLDING_THERMAL_CONTROL_XML_FILE_ROOT_ITEM = "sys_thermal_control_list_folding";
    private static final long FOUR_HOUR = 14400000;
    public static final String GEAR_CONFIG_ITEM = "gear_config";
    private static final String GT_THERMAL_CONTROL_XML_FILE_PATH = "sys_thermal_control_config_gt.xml";
    private static final String HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH = "sys_thermal_control_config_high_ambient.xml";
    private static final String HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM = "sys_thermal_control_list_high_ambient";
    public static final int HIGH_TEMPERATURE_THRESHOLD_DEFAULT = 43;
    public static final int HIGH_TEMP_SAFETY_LEVEL_DEFAULT = 15;
    public static final int HIGH_TEMP_SAFETY_LEVEL_MIN = 11;
    public static final int INDEX_GAME_HIGHPERF_MODE = 6;
    public static final int INDEX_GAME_NORMAL_MODE = 7;
    public static final int INDEX_GAME_POWERSAVE_MODE = 5;
    public static final int INDEX_HIGH_PREF_MODE = 1;
    public static final int INDEX_NORMAL_MODE = 2;
    public static final int INDEX_POWERSAVE_MODE = 3;
    public static final int INDEX_RACING_MODE = 0;
    public static final int INDEX_SPOWERSAVE_MODE = 4;
    public static final String INT_VALUE_CONFIG_ITEM = "intVal";
    private static final String LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH = "sys_thermal_control_config_low_ambient.xml";
    private static final String LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM = "sys_thermal_control_list_low_ambient";
    public static final String ORIGINAL_DEFAULT_ITEM = "common_config";
    public static final String ORIGINAL_SAFETY_TEST_ITEM = "safety_test";
    public static final String ORIGINAL_SCREEN_OFF_ITEM = "screen_off";
    private static final String PRE_THERMAL_CONTROL_XML_FILE_PATH = "sys_thermal_control_config.xml";
    private static final String PRE_THERMAL_CONTROL_XML_FILE_ROOT_ITEM = "sys_thermal_control_list";
    public static final int RACING_HIGH_TEMP_SAFETY_LEVEL_DEFAULT = 15;
    private static final String SPLIT_THERMAL_CONTROL_XML_FILE_PATH = "sys_thermal_control_config_split.xml";
    private static final String SPLIT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM = "sys_thermal_control_list_split";
    public static final String TAG = "ThermalControlConfig";
    public static final int THERMAL_OPTIMIZATION_REPORT_LEVEL_DEFAULT = 7;
    private static volatile ThermalControlConfig sConfig;
    private final ThermalControlConfigInfo mConfigInfo;
    private Context mContext;
    private final ThermalControlConfigInfo mFoldingConfigInfo;
    private final ThermalControlConfigInfo mHighAmbientConfigInfo;
    private final Object mLock;
    private final ThermalControlConfigInfo mLowAmbientConfigInfo;
    private final ThermalControlConfigInfo mSplitConfigInfo;
    private ThermalControlUtils mUtils;
    private final ThermalWindowConfigInfo mWindowConfigInfo;
    public static final Uri CONTENT_URI = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");
    public static final boolean XML_DBG = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    /* loaded from: classes2.dex */
    public class ThermalControlConfigInfo {
        public boolean mThermalControlEnabled = true;
        public boolean mSafetyTestEnabled = true;
        public boolean mSafetyOptimizeEnabled = false;
        public boolean mAgingThermalControlEnabled = false;
        public boolean mIpaEnabled = false;
        public int mCategoryMsgDelayVal = 60;
        public int mAgingCpuLevelRestrictVal = 4;
        public int mSafetyThermalLevelVal = 11;
        public int mRestrictTempGearVal = 10;
        public int mOtaThermalControlVal = -3;
        public int mRacingAdditionVal = 0;
        public int mHighPrefAdditionVal = 0;
        public int mNormalAdditionVal = 0;
        public int mPowersaveAdditionVal = 0;
        public int mSpowersaveAdditionVal = 0;
        public int mGameHighPrefAdditionVal = 0;
        public int mGamePowersaveAdditionVal = 0;
        public int mGameNormalAdditionVal = 0;
        public int mFloatWindowAdditionVal = 0;
        public int mEnvironmentThresholdVal = 30;
        public int mHighTempSafetyLevelVal = 15;
        public int mAgingHighTempSafetyLevelVal = 19;
        public int mAmbientKeepTimeVal = 5;
        public int mAmbientPolicyMode = 0;
        public int mFloatWindowRefreshLevel = 10;
        public int mHighTemperatureThreshold = 43;
        public int mSafetyProtectMsgDelayVal = 3600;
        public int mAmbientTempLowerLimit = 22;
        public int mAmbientTempUpperLimit = 28;
        public int mAppSwitchSafetyModeLevel = 3;
        public int mFloatingWindowsInfoAcquireLevel = 5;
        public int mThermalOptimizationReportLevel = 7;
        public int mRacingHighTempSafetyLevelVal = 15;
        public String mSafetyTestConfigList = "01111,10111,11011,11101,11110,00111,01011,01101,01110,10011,10101,10110,11001,11010,11100,11111";
        public String mConfigVersion = "2000010101";
        public String mDefaultConfigVersion = "2000010101";
        public String mRusConfigVersion = "2000010101";
        public String mConfigFile = ThermalControlConfig.PRE_THERMAL_CONTROL_XML_FILE_PATH;
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mGlobalPolicyConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mSafetySceneConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mScreenOffConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mSpecificAppConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mSpecificAppSceneConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mSpecificAppEnvConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mSceneConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mAppCategoryConfig = new ArrayMap<>();
        public final ArrayMap<String, SparseArray<ThermalPolicy>> mDefaultConfig = new ArrayMap<>();
        public final SparseArray<ThermalAmbientPolicy> mAmbientConfig = new SparseArray<>();
        public final ArrayMap<String, ArrayMap<String, ThermalBaseConfig>> mFeatureConfigs = new ArrayMap<>();

        public ThermalControlConfigInfo() {
        }

        public void clear() {
            this.mGlobalPolicyConfig.clear();
            this.mSafetySceneConfig.clear();
            this.mScreenOffConfig.clear();
            this.mSpecificAppConfig.clear();
            this.mSpecificAppSceneConfig.clear();
            this.mSpecificAppEnvConfig.clear();
            this.mSceneConfig.clear();
            this.mAppCategoryConfig.clear();
            this.mDefaultConfig.clear();
            this.mAmbientConfig.clear();
            this.mFeatureConfigs.clear();
        }

        public <T extends ThermalBaseConfig> T getFeatureConfigBy(String str, String str2, Class<T> cls) {
            synchronized (ThermalControlConfig.this.mLock) {
                ArrayMap<String, ThermalBaseConfig> arrayMap = this.mFeatureConfigs.get(str);
                if (arrayMap == null) {
                    return null;
                }
                ThermalBaseConfig thermalBaseConfig = arrayMap.get(str2);
                return thermalBaseConfig != null ? (T) thermalBaseConfig.get(cls) : null;
            }
        }

        public void parseFeatureNode(Element element) {
            String attribute = element.getAttribute("name");
            ArrayMap<String, ThermalBaseConfig> arrayMap = new ArrayMap<>();
            synchronized (ThermalControlConfig.this.mLock) {
                this.mFeatureConfigs.put(attribute, arrayMap);
            }
            NodeList childNodes = element.getChildNodes();
            for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
                Node item = childNodes.item(i10);
                if (item.getNodeType() == 1) {
                    Element element2 = (Element) item;
                    String attribute2 = element2.getAttribute("name");
                    ThermalBaseConfig create = ThermalBaseConfig.create(1, attribute2);
                    create.mConfigName = attribute2;
                    create.mType = 1;
                    synchronized (ThermalControlConfig.this.mLock) {
                        arrayMap.put(create.mConfigName, create);
                    }
                    NamedNodeMap attributes = element2.getAttributes();
                    Map<String, String> configProperties = create.getConfigProperties();
                    configProperties.clear();
                    for (int i11 = 0; i11 < attributes.getLength(); i11++) {
                        Node item2 = attributes.item(i11);
                        configProperties.put(item2.getNodeName(), item2.getNodeValue());
                    }
                    create.parseConfigItems(element2);
                }
            }
        }

        public String toString() {
            return "ConfigFile=" + this.mConfigFile + "\nConfigVersion=" + this.mConfigVersion + "\nfeature_enable_item=" + this.mThermalControlEnabled + "\nfeature_safety_test_enable_item=" + this.mSafetyTestEnabled + "\nfeature_safety_optimize_enable_item=" + this.mSafetyOptimizeEnabled + "\n";
        }
    }

    private ThermalControlConfig(Context context) {
        boolean z10;
        Object obj = new Object();
        this.mLock = obj;
        this.mConfigInfo = new ThermalControlConfigInfo();
        ThermalControlConfigInfo thermalControlConfigInfo = new ThermalControlConfigInfo();
        this.mLowAmbientConfigInfo = thermalControlConfigInfo;
        ThermalControlConfigInfo thermalControlConfigInfo2 = new ThermalControlConfigInfo();
        this.mHighAmbientConfigInfo = thermalControlConfigInfo2;
        ThermalControlConfigInfo thermalControlConfigInfo3 = new ThermalControlConfigInfo();
        this.mFoldingConfigInfo = thermalControlConfigInfo3;
        ThermalControlConfigInfo thermalControlConfigInfo4 = new ThermalControlConfigInfo();
        this.mSplitConfigInfo = thermalControlConfigInfo4;
        ThermalWindowConfigInfo thermalWindowConfigInfo = new ThermalWindowConfigInfo();
        this.mWindowConfigInfo = thermalWindowConfigInfo;
        this.mContext = context;
        this.mUtils = ThermalControlUtils.getInstance(context);
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, PRE_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        String dataFromProvider2 = ThermalConfigUtil.getDataFromProvider(context, FOLDING_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        String dataFromProvider3 = ThermalConfigUtil.getDataFromProvider(context, SPLIT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        if (y5.b.D() && isGTmodeOn()) {
            String dataFromProvider4 = ThermalConfigUtil.getDataFromProvider(context, "sys_thermal_control_gt_list");
            if (dataFromProvider4 == null || TextUtils.isEmpty(dataFromProvider4)) {
                addGTDefaultConfig();
            } else if (parseVersionFromRUS(dataFromProvider4, false, false) > parseVersionFromGTDefault()) {
                parseFilterListFromXml(dataFromProvider4, false, false);
            } else {
                addGTDefaultConfig();
            }
            z10 = false;
        } else {
            z10 = false;
            initConfigInfo(dataFromProvider, false, false, false, false);
            initConfigInfo(dataFromProvider2, true, false, false, false);
            initConfigInfo(dataFromProvider3, false, true, false, false);
        }
        if (isAmbientXmlMode() && !thermalControlConfigInfo3.mThermalControlEnabled && !thermalControlConfigInfo4.mThermalControlEnabled) {
            initAmbientConfigInfo(context);
        } else {
            synchronized (obj) {
                thermalControlConfigInfo.clear();
                thermalControlConfigInfo2.clear();
                thermalControlConfigInfo.mThermalControlEnabled = z10;
                thermalControlConfigInfo2.mThermalControlEnabled = z10;
            }
            LocalLog.a(TAG, "no need to load ambient config info");
        }
        thermalWindowConfigInfo.initConfig(context);
    }

    private void checkAmbientPolicyModeChange() {
        boolean z10;
        synchronized (this.mLock) {
            z10 = false;
            if (isAmbientXmlMode() && !this.mFoldingConfigInfo.mThermalControlEnabled && !this.mSplitConfigInfo.mThermalControlEnabled) {
                if (!this.mLowAmbientConfigInfo.mThermalControlEnabled && !this.mHighAmbientConfigInfo.mThermalControlEnabled) {
                    z10 = true;
                }
            } else {
                this.mLowAmbientConfigInfo.clear();
                this.mHighAmbientConfigInfo.clear();
                this.mLowAmbientConfigInfo.mThermalControlEnabled = false;
                this.mHighAmbientConfigInfo.mThermalControlEnabled = false;
            }
        }
        if (z10) {
            initAmbientConfigInfo(this.mContext);
        }
    }

    private boolean checkXmlRootItem(String str, boolean z10, boolean z11, boolean z12, boolean z13) {
        if (z10) {
            if (str != null && str.equals(FOLDING_THERMAL_CONTROL_XML_FILE_ROOT_ITEM)) {
                return true;
            }
            LocalLog.b(TAG, "loadApListFromXml, xml root tag is not sys_thermal_control_list_folding");
            return false;
        }
        if (z11) {
            if (str != null && str.equals(SPLIT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM)) {
                return true;
            }
            LocalLog.b(TAG, "loadApListFromXml, xml root tag is not sys_thermal_control_list_split");
            return false;
        }
        if (z12) {
            if (str != null && str.equals(LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM)) {
                return true;
            }
            LocalLog.b(TAG, "loadApListFromXml, xml root tag is not sys_thermal_control_list_low_ambient");
            return false;
        }
        if (z13) {
            if (str != null && str.equals(HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM)) {
                return true;
            }
            LocalLog.b(TAG, "loadApListFromXml, xml root tag is not sys_thermal_control_list_high_ambient");
            return false;
        }
        if (str != null && str.equals(PRE_THERMAL_CONTROL_XML_FILE_ROOT_ITEM)) {
            return true;
        }
        LocalLog.b(TAG, "loadApListFromXml, xml root tag is not sys_thermal_control_list");
        return false;
    }

    private void clearAllPolicyConfigMap(boolean z10, boolean z11) {
        clearAllPolicyConfigMap(z10, z11, false, false);
    }

    private void commonParseConfigForDocument(Document document, boolean z10, boolean z11, boolean z12, boolean z13) {
        Element element;
        String tagName;
        Element documentElement = document.getDocumentElement();
        if (documentElement == null) {
            LocalLog.b(TAG, "commonParseConfigForDocument, root == null");
            return;
        }
        String trim = documentElement.getTagName().trim();
        if (XML_DBG) {
            LocalLog.d(TAG, "loadApListFromXml, xml root is " + trim);
        }
        if (!checkXmlRootItem(trim, z10, z11, z12, z13)) {
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
            return;
        }
        NodeList childNodes = documentElement.getChildNodes();
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null) {
                if (tagName.equals("version")) {
                    ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo(z10, z11, z12, z13);
                    thermalControlConfigInfo.mConfigVersion = element.getTextContent();
                    LocalLog.a(TAG, "mConfigVersion=" + thermalControlConfigInfo.mConfigVersion);
                } else if (tagName.equals(CONFIG_TYPE_FEATURE_CONFIG)) {
                    parseXMLFeatureConfigItem(element, z10, z11, z12, z13);
                } else if (tagName.equals(CONFIG_TYPE_POLICY_CONFIG)) {
                    parseXMLPolicyConfiguration(element, z10, z11, z12, z13);
                } else if (tagName.equals("config")) {
                    if (ThermalAmbientPolicy.KEY_AMBIENT_LEVEL.equals(element.getAttribute("name")) && !z10 && !z11 && !z12 && !z13) {
                        parseXMLAmbientConfiguration(element, z10, z11, z12, z13);
                    }
                } else if (tagName.equals(CONFIG_TYPE_FEATURE)) {
                    getThermalControlConfigInfo(z10, z11, z12, z13).parseFeatureNode(element);
                }
            }
        }
    }

    public static ThermalControlConfig getInstance(Context context) {
        if (sConfig == null) {
            synchronized (ThermalControlConfig.class) {
                if (sConfig == null) {
                    sConfig = new ThermalControlConfig(context);
                }
            }
        }
        return sConfig;
    }

    private ThermalControlConfigInfo getThermalControlConfigInfo(boolean z10, boolean z11, boolean z12, boolean z13) {
        if (z10) {
            return this.mFoldingConfigInfo;
        }
        if (z11) {
            return this.mSplitConfigInfo;
        }
        if (z12) {
            return this.mLowAmbientConfigInfo;
        }
        if (z13) {
            return this.mHighAmbientConfigInfo;
        }
        return this.mConfigInfo;
    }

    private void initAmbientConfigInfo(Context context) {
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        String dataFromProvider2 = ThermalConfigUtil.getDataFromProvider(context, HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        synchronized (this.mLock) {
            this.mLowAmbientConfigInfo.clear();
            this.mHighAmbientConfigInfo.clear();
        }
        initConfigInfo(dataFromProvider, false, false, true, false);
        initConfigInfo(dataFromProvider2, false, false, false, true);
    }

    private void initConfigInfo(String str, boolean z10, boolean z11, boolean z12, boolean z13) {
        if (str == null || TextUtils.isEmpty(str)) {
            addDefaultConfig(z10, z11, z12, z13);
        } else if (parseVersionFromRUS(str, z10, z11, z12, z13) > parseVersionFromDefault(z10, z11, z12, z13)) {
            parseFilterListFromXml(str, z10, z11, z12, z13);
        } else {
            addDefaultConfig(z10, z11, z12, z13);
        }
    }

    private boolean isGTmodeOn() {
        int intForUser = Settings.System.getIntForUser(this.mContext.getContentResolver(), "gt_mode_state_setting", 0, 0);
        LocalLog.l(TAG, "isGTmodeOn, gtState =" + intForUser);
        return intForUser == 1;
    }

    private void parseFilterListFromXml(String str, boolean z10, boolean z11) {
        parseFilterListFromXml(str, z10, z11, false, false);
    }

    private long parseVersionFromDefault(boolean z10, boolean z11) {
        return parseVersionFromDefault(z10, z11, false, false);
    }

    private long parseVersionFromGTDefault() {
        DocumentBuilder newDocumentBuilder;
        File thermalConfigFile;
        Element element;
        String tagName;
        try {
            newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            thermalConfigFile = ThermalConfigUtil.thermalConfigFile(GT_THERMAL_CONTROL_XML_FILE_PATH);
        } catch (IOException | ParserConfigurationException | SAXException unused) {
        }
        if (!thermalConfigFile.exists()) {
            return 0L;
        }
        Document parse = newDocumentBuilder.parse(thermalConfigFile);
        if (parse == null) {
            return 0L;
        }
        Element documentElement = parse.getDocumentElement();
        String trim = documentElement.getTagName().trim();
        if (trim != null && trim.equals(PRE_THERMAL_CONTROL_XML_FILE_ROOT_ITEM)) {
            NodeList childNodes = documentElement.getChildNodes();
            int i10 = 0;
            while (true) {
                if (i10 >= childNodes.getLength()) {
                    break;
                }
                Node item = childNodes.item(i10);
                if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null && tagName.equals("version")) {
                    this.mConfigInfo.mDefaultConfigVersion = element.getTextContent();
                    LocalLog.l(TAG, "parseVersionFromGTDefault mDefaultConfigVersion =" + this.mConfigInfo.mDefaultConfigVersion);
                    if (ThermalConfigUtil.isNumeric(this.mConfigInfo.mDefaultConfigVersion)) {
                        return Long.parseLong(this.mConfigInfo.mDefaultConfigVersion);
                    }
                } else {
                    i10++;
                }
            }
        }
        return 0L;
    }

    private long parseVersionFromRUS(String str, boolean z10, boolean z11) {
        return parseVersionFromRUS(str, z10, z11, false, false);
    }

    private void parseXMLAmbientConfiguration(Element element, boolean z10, boolean z11, boolean z12, boolean z13) {
        Element element2;
        String tagName;
        NodeList childNodes = element.getChildNodes();
        ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo(z10, z11, z12, z13);
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if ((item instanceof Element) && (tagName = (element2 = (Element) item).getTagName()) != null) {
                try {
                    if (tagName.equals("temp")) {
                        ThermalAmbientPolicy createFromElement = ThermalAmbientPolicy.createFromElement(element2);
                        synchronized (this.mLock) {
                            int i11 = createFromElement.level;
                            if (i11 != Integer.MIN_VALUE) {
                                thermalControlConfigInfo.mAmbientConfig.put(i11, createFromElement);
                            }
                            LocalLog.a(TAG, "" + createFromElement);
                        }
                    }
                } catch (NumberFormatException e10) {
                    LocalLog.b(TAG, "parseXMLAmbientConfiguration " + e10.toString());
                }
            }
        }
    }

    private void parseXMLFeatureConfigItem(Element element, boolean z10, boolean z11, boolean z12, boolean z13) {
        NodeList childNodes = element.getChildNodes();
        ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo(z10, z11, z12, z13);
        for (int i10 = 0; i10 < childNodes.getLength(); i10++) {
            Node item = childNodes.item(i10);
            if (item instanceof Element) {
                Element element2 = (Element) item;
                String tagName = element2.getTagName();
                synchronized (this.mLock) {
                    if (tagName != null) {
                        try {
                            if (tagName.equals("feature_enable_item") && element2.getAttribute("booleanVal") != "") {
                                thermalControlConfigInfo.mThermalControlEnabled = Boolean.parseBoolean(element2.getAttribute("booleanVal"));
                            } else if (tagName.equals("aging_thermal_control_enable_item") && element2.getAttribute("booleanVal") != "") {
                                thermalControlConfigInfo.mAgingThermalControlEnabled = Boolean.parseBoolean(element2.getAttribute("booleanVal"));
                            } else if (tagName.equals("feature_safety_test_enable_item") && element2.getAttribute("booleanVal") != "") {
                                thermalControlConfigInfo.mSafetyTestEnabled = Boolean.parseBoolean(element2.getAttribute("booleanVal"));
                            } else if (!tagName.equals("ipa_feature_item") || element2.getAttribute("booleanVal") == "") {
                                if (tagName.equals("feature_safety_optimize_enable_item") && element2.getAttribute("booleanVal") != "") {
                                    thermalControlConfigInfo.mSafetyOptimizeEnabled = Boolean.parseBoolean(element2.getAttribute("booleanVal"));
                                } else if (tagName.equals("safety_test_config_item") && element2.getAttribute("stringVal") != "") {
                                    thermalControlConfigInfo.mSafetyTestConfigList = element2.getAttribute("stringVal");
                                } else if (tagName.equals("category_msg_delay_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mCategoryMsgDelayVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("aging_cpu_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mAgingCpuLevelRestrictVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("safety_thermal_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mSafetyThermalLevelVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("ota_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mOtaThermalControlVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("restrict_temp_gear_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mRestrictTempGearVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("restrict_level_config_item") && element2.getAttribute("stringVal") != "") {
                                    this.mUtils.setRestrictLevelList(element2.getAttribute("stringVal"));
                                } else if (tagName.equals("racing_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mRacingAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("high_perf_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mHighPrefAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("normal_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mNormalAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("powersave_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mPowersaveAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("s_powersave_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mSpowersaveAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("game_high_perf_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mGameHighPrefAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("game_powersave_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mGamePowersaveAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("game_normal_mode_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mGameNormalAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("float_window_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mFloatWindowAdditionVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("environment_threshold_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mEnvironmentThresholdVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("high_temp_safety_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mHighTempSafetyLevelVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("aging_high_temp_safety_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mAgingHighTempSafetyLevelVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("float_window_refresh_restrict_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mFloatWindowRefreshLevel = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("ambient_temp_low_limit_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mAmbientTempLowerLimit = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("ambient_temp_upper_limit_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mAmbientTempUpperLimit = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("app_switch_safety_mode_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mAppSwitchSafetyModeLevel = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("safety_mode_msg_delay_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mSafetyProtectMsgDelayVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("float_window_info_acquire_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                    thermalControlConfigInfo.mFloatingWindowsInfoAcquireLevel = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                } else if (tagName.equals("scene_listen_list") && element2.getAttribute("stringVal") != "") {
                                    this.mUtils.setSceneListenList(element2.getAttribute("stringVal"));
                                } else if (!tagName.equals("ambient_keep_time") || element2.getAttribute(INT_VALUE_CONFIG_ITEM) == "") {
                                    if (!tagName.equals("feature_ambient_policy_mode") || element2.getAttribute(INT_VALUE_CONFIG_ITEM) == "") {
                                        if (!tagName.equals("hight_temp_threshold") || element2.getAttribute(INT_VALUE_CONFIG_ITEM) == "") {
                                            if (tagName.equals("thermal_optimization_report_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                                thermalControlConfigInfo.mThermalOptimizationReportLevel = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                            } else if (tagName.equals("racing_high_temp_safety_level_item") && element2.getAttribute(INT_VALUE_CONFIG_ITEM) != "") {
                                                thermalControlConfigInfo.mRacingHighTempSafetyLevelVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                            }
                                        } else if (z10) {
                                            this.mFoldingConfigInfo.mHighTemperatureThreshold = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                        } else if (z11) {
                                            this.mSplitConfigInfo.mHighTemperatureThreshold = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                        } else {
                                            this.mConfigInfo.mHighTemperatureThreshold = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                        }
                                    } else if (!z10 && !z11 && !z12 && !z13) {
                                        thermalControlConfigInfo.mAmbientPolicyMode = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                    }
                                } else if (!z10 && !z11 && !z12 && !z13) {
                                    thermalControlConfigInfo.mAmbientKeepTimeVal = Integer.parseInt(element2.getAttribute(INT_VALUE_CONFIG_ITEM));
                                }
                            } else if (!z10 && !z11) {
                                this.mConfigInfo.mIpaEnabled = Boolean.parseBoolean(element2.getAttribute("booleanVal"));
                                this.mUtils.setIpaFeatureState(this.mConfigInfo.mIpaEnabled);
                            }
                        } catch (Exception e10) {
                            LocalLog.b(TAG, "parseXMLFeatureConfigItem " + e10.toString());
                        }
                    }
                }
            }
        }
    }

    private void parseXMLPolicyConfiguration(Element element, boolean z10, boolean z11, boolean z12, boolean z13) {
        Element element2;
        String tagName;
        NodeList nodeList;
        NodeList nodeList2;
        NodeList nodeList3;
        Element element3;
        String tagName2;
        NodeList childNodes = element.getChildNodes();
        int i10 = 0;
        while (i10 < childNodes.getLength()) {
            Node item = childNodes.item(i10);
            if ((item instanceof Element) && (tagName = (element2 = (Element) item).getTagName()) != null && (tagName.equals(CONFIG_TYPE_GLOBAL_POLICY) || tagName.equals(CONFIG_TYPE_SAFETY_TEST) || tagName.equals(CONFIG_TYPE_SCREEN_OFF) || tagName.equals(CONFIG_TYPE_SPECIFIC) || tagName.equals(CONFIG_TYPE_SPECIFIC_SCENE) || tagName.equals(CONFIG_TYPE_SPECIFIC_ENV) || tagName.equals(CONFIG_TYPE_SCENE) || tagName.equals(CONFIG_TYPE_CATEGORY) || tagName.equals(CONFIG_TYPE_DEFAULT))) {
                NodeList childNodes2 = element2.getChildNodes();
                ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo(z10, z11, z12, z13);
                int i11 = 0;
                while (i11 < childNodes2.getLength()) {
                    Node item2 = childNodes2.item(i11);
                    if (item2 instanceof Element) {
                        Element element4 = (Element) item2;
                        if (element4.getTagName() != null) {
                            String tagName3 = element4.getTagName();
                            SparseArray<ThermalPolicy> sparseArray = new SparseArray<>();
                            NodeList childNodes3 = element4.getChildNodes();
                            int i12 = 0;
                            while (i12 < childNodes3.getLength()) {
                                Node item3 = childNodes3.item(i12);
                                NodeList nodeList4 = childNodes;
                                if (!(item3 instanceof Element) || (tagName2 = (element3 = (Element) item3).getTagName()) == null) {
                                    nodeList3 = childNodes2;
                                } else {
                                    nodeList3 = childNodes2;
                                    try {
                                        if (tagName2.equals(GEAR_CONFIG_ITEM)) {
                                            ThermalPolicy createFromElement = ThermalPolicy.createFromElement(element3, tagName3, tagName2);
                                            ThermalPolicy thermalPolicy = thermalControlConfigInfo.mGlobalPolicyConfig.containsKey(CONFIG_TYPE_GLOBAL_POLICY) ? thermalControlConfigInfo.mGlobalPolicyConfig.get(CONFIG_TYPE_GLOBAL_POLICY).get(createFromElement.gearLevel) : null;
                                            if (thermalPolicy != null) {
                                                ThermalPolicy m26clone = thermalPolicy.m26clone();
                                                m26clone.overridePolicy(createFromElement);
                                                createFromElement = m26clone;
                                            }
                                            if (sparseArray.indexOfKey(createFromElement.gearLevel) >= 0) {
                                                sparseArray.remove(createFromElement.gearLevel);
                                            }
                                            sparseArray.put(createFromElement.gearLevel, createFromElement);
                                        }
                                    } catch (Exception e10) {
                                        LocalLog.b(TAG, "parseXMLPolicyConfiguration: " + e10.toString());
                                    }
                                }
                                i12++;
                                childNodes = nodeList4;
                                childNodes2 = nodeList3;
                            }
                            nodeList = childNodes;
                            nodeList2 = childNodes2;
                            for (String str : tagName3.split("-")) {
                                if (str != null) {
                                    synchronized (this.mLock) {
                                        char c10 = 65535;
                                        switch (tagName.hashCode()) {
                                            case -2132874958:
                                                if (tagName.equals(CONFIG_TYPE_SPECIFIC)) {
                                                    c10 = 5;
                                                    break;
                                                }
                                                break;
                                            case -750448971:
                                                if (tagName.equals(CONFIG_TYPE_GLOBAL_POLICY)) {
                                                    c10 = 0;
                                                    break;
                                                }
                                                break;
                                            case -731626917:
                                                if (tagName.equals(CONFIG_TYPE_SPECIFIC_ENV)) {
                                                    c10 = 4;
                                                    break;
                                                }
                                                break;
                                            case -96723324:
                                                if (tagName.equals(CONFIG_TYPE_SAFETY_TEST)) {
                                                    c10 = 1;
                                                    break;
                                                }
                                                break;
                                            case 50511102:
                                                if (tagName.equals(CONFIG_TYPE_CATEGORY)) {
                                                    c10 = 7;
                                                    break;
                                                }
                                                break;
                                            case 109254796:
                                                if (tagName.equals(CONFIG_TYPE_SCENE)) {
                                                    c10 = 6;
                                                    break;
                                                }
                                                break;
                                            case 125078883:
                                                if (tagName.equals(CONFIG_TYPE_SCREEN_OFF)) {
                                                    c10 = 2;
                                                    break;
                                                }
                                                break;
                                            case 1293758074:
                                                if (tagName.equals(CONFIG_TYPE_SPECIFIC_SCENE)) {
                                                    c10 = 3;
                                                    break;
                                                }
                                                break;
                                            case 1544803905:
                                                if (tagName.equals(CONFIG_TYPE_DEFAULT)) {
                                                    c10 = '\b';
                                                    break;
                                                }
                                                break;
                                        }
                                        switch (c10) {
                                            case 0:
                                                thermalControlConfigInfo.mGlobalPolicyConfig.put(str, sparseArray);
                                                break;
                                            case 1:
                                                thermalControlConfigInfo.mSafetySceneConfig.put(str, sparseArray);
                                                break;
                                            case 2:
                                                thermalControlConfigInfo.mScreenOffConfig.put(str, sparseArray);
                                                break;
                                            case 3:
                                                thermalControlConfigInfo.mSpecificAppSceneConfig.put(str, sparseArray);
                                                break;
                                            case 4:
                                                thermalControlConfigInfo.mSpecificAppEnvConfig.put(str, sparseArray);
                                                break;
                                            case 5:
                                                thermalControlConfigInfo.mSpecificAppConfig.put(str, sparseArray);
                                                break;
                                            case 6:
                                                thermalControlConfigInfo.mSceneConfig.put(str, sparseArray);
                                                break;
                                            case 7:
                                                thermalControlConfigInfo.mAppCategoryConfig.put(str, sparseArray);
                                                break;
                                            case '\b':
                                                thermalControlConfigInfo.mDefaultConfig.put(str, sparseArray);
                                                break;
                                        }
                                    }
                                }
                            }
                            i11++;
                            childNodes = nodeList;
                            childNodes2 = nodeList2;
                        }
                    }
                    nodeList = childNodes;
                    nodeList2 = childNodes2;
                    i11++;
                    childNodes = nodeList;
                    childNodes2 = nodeList2;
                }
            }
            i10++;
            childNodes = childNodes;
        }
    }

    private void setConfigThermalControlEnabled(boolean z10, boolean z11, boolean z12, boolean z13, boolean z14) {
        getThermalControlConfigInfo(z10, z11, z12, z13).mThermalControlEnabled = z14;
    }

    public void addDefaultConfig(boolean z10, boolean z11) {
        addDefaultConfig(z10, z11, false, false);
    }

    public void addGTDefaultConfig() {
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File thermalConfigFile = ThermalConfigUtil.thermalConfigFile(GT_THERMAL_CONTROL_XML_FILE_PATH);
            if (!thermalConfigFile.exists()) {
                if (XML_DBG) {
                    LocalLog.d(TAG, "GT Xml not exist");
                }
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(PRE_THERMAL_CONTROL_XML_FILE_PATH);
            }
            if (XML_DBG) {
                LocalLog.d(TAG, "GT loadApListFromXml, parsing " + thermalConfigFile.getPath());
            }
            Document parse = thermalConfigFile != null ? newDocumentBuilder.parse(thermalConfigFile) : null;
            if (parse == null) {
                LocalLog.b(TAG, "GT loadApListFromXml, null doc!");
                this.mConfigInfo.mThermalControlEnabled = false;
            } else {
                commonParseConfigForDocument(parse, false, false, false, false);
            }
        } catch (IOException e10) {
            LocalLog.b(TAG, "GT IOException:" + e10);
            this.mConfigInfo.mThermalControlEnabled = false;
        } catch (ParserConfigurationException e11) {
            LocalLog.b(TAG, "GT ParserConfigurationException:" + e11);
            this.mConfigInfo.mThermalControlEnabled = false;
        } catch (SAXException e12) {
            LocalLog.b(TAG, "GT SAXException:" + e12);
            this.mConfigInfo.mThermalControlEnabled = false;
        }
    }

    public int getAdditionValWithFloatWindow() {
        synchronized (this.mLock) {
            if (!this.mUtils.isFloatWindowOn()) {
                return 0;
            }
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null) {
                return 0;
            }
            return thermalControlConfigInfo.mFloatWindowAdditionVal;
        }
    }

    public int getAdditionValWithModeType(int i10) {
        int i11;
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo != null) {
                switch (i10) {
                    case 0:
                        i11 = thermalControlConfigInfo.mRacingAdditionVal;
                        break;
                    case 1:
                        i11 = thermalControlConfigInfo.mHighPrefAdditionVal;
                        break;
                    case 2:
                        i11 = thermalControlConfigInfo.mNormalAdditionVal;
                        break;
                    case 3:
                        i11 = thermalControlConfigInfo.mPowersaveAdditionVal;
                        break;
                    case 4:
                        i11 = thermalControlConfigInfo.mSpowersaveAdditionVal;
                        break;
                    case 5:
                        i11 = thermalControlConfigInfo.mGamePowersaveAdditionVal;
                        break;
                    case 6:
                        i11 = thermalControlConfigInfo.mGameHighPrefAdditionVal;
                        break;
                    case 7:
                        i11 = thermalControlConfigInfo.mGameNormalAdditionVal;
                        break;
                }
            }
            i11 = 0;
        }
        return i11;
    }

    public int getAgingCpuLevelRestrictVal() {
        int i10;
        int i11;
        ThermalControlConfigInfo thermalControlConfigInfo;
        ThermalControlConfigInfo thermalControlConfigInfo2;
        if (this.mUtils.isFoldingMode() && (thermalControlConfigInfo = this.mFoldingConfigInfo) != null && thermalControlConfigInfo.mThermalControlEnabled && thermalControlConfigInfo.mAgingCpuLevelRestrictVal >= 0) {
            if (this.mUtils.isSplitMode() && (thermalControlConfigInfo2 = this.mSplitConfigInfo) != null && thermalControlConfigInfo2.mThermalControlEnabled) {
                return thermalControlConfigInfo2.mAgingCpuLevelRestrictVal;
            }
            return this.mFoldingConfigInfo.mAgingCpuLevelRestrictVal;
        }
        if (isAmbientXmlMode()) {
            if (this.mUtils.getAmbientTempState() == 3) {
                ThermalControlConfigInfo thermalControlConfigInfo3 = this.mHighAmbientConfigInfo;
                if (thermalControlConfigInfo3.mThermalControlEnabled && (i11 = thermalControlConfigInfo3.mAgingCpuLevelRestrictVal) >= 0) {
                    return i11;
                }
            }
            if (this.mUtils.getAmbientTempState() == 1) {
                ThermalControlConfigInfo thermalControlConfigInfo4 = this.mLowAmbientConfigInfo;
                if (thermalControlConfigInfo4.mThermalControlEnabled && (i10 = thermalControlConfigInfo4.mAgingCpuLevelRestrictVal) >= 0) {
                    return i10;
                }
            }
        }
        if (this.mConfigInfo.mAgingCpuLevelRestrictVal < 0) {
            return 0;
        }
        LocalLog.a(TAG, "getThermalControlConfigInfo: default");
        return this.mConfigInfo.mAgingCpuLevelRestrictVal;
    }

    public int getAgingHighTempSafetyLevel() {
        int i10;
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null || (i10 = thermalControlConfigInfo.mAgingHighTempSafetyLevelVal) <= 15) {
                return 19;
            }
            return i10;
        }
    }

    public long getAmbientKeepTimeVal() {
        int i10 = this.mConfigInfo.mAmbientKeepTimeVal;
        if (i10 > 0) {
            return i10 * 60 * 1000;
        }
        return 0L;
    }

    public long getAmbientPolicyModeVal() {
        return this.mConfigInfo.mAmbientPolicyMode;
    }

    public int getAmbientTemperatureLowerLimit() {
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null) {
                return 22;
            }
            return thermalControlConfigInfo.mAmbientTempLowerLimit;
        }
    }

    public int getAmbientTemperatureUpperLimit() {
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null) {
                return 28;
            }
            return thermalControlConfigInfo.mAmbientTempUpperLimit;
        }
    }

    public int getAppSwitchSafetyModeLevel() {
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null) {
                return 3;
            }
            return thermalControlConfigInfo.mAppSwitchSafetyModeLevel;
        }
    }

    public long getCategoryMsgDelayVal() {
        if (AppFeature.z()) {
            return 0L;
        }
        return getThermalControlConfigInfo().mCategoryMsgDelayVal * 1000;
    }

    public int getFloatWindowRefreshLevel() {
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null) {
                return 10;
            }
            return thermalControlConfigInfo.mFloatWindowRefreshLevel;
        }
    }

    public int getFloatingWindowsInfoAcquireLevel() {
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null) {
                return 10;
            }
            return thermalControlConfigInfo.mFloatingWindowsInfoAcquireLevel;
        }
    }

    public Map<String, Integer> getHeatSourceLevel(Map<String, Integer> map) {
        HeatSourceLevelConfig heatSourceLevelConfig = (HeatSourceLevelConfig) getThermalControlConfigInfo().getFeatureConfigBy("heatsource", HeatSourceLevelConfig.CONFIG_NAME, HeatSourceLevelConfig.class);
        if (heatSourceLevelConfig == null) {
            LocalLog.b(TAG, "HeatSourceLevel featureConfig is null");
            return new HashMap();
        }
        return heatSourceLevelConfig.getHeatSourceLevel(map);
    }

    public Map<String, Integer> getHeatSourceOffset(int i10, Map<String, Integer> map) {
        HeatSourceOffsetConfig heatSourceOffsetConfig = (HeatSourceOffsetConfig) getThermalControlConfigInfo().getFeatureConfigBy("heatsource", HeatSourceOffsetConfig.CONFIG_NAME, HeatSourceOffsetConfig.class);
        if (heatSourceOffsetConfig == null) {
            LocalLog.b(TAG, "HeatSourceOffset featureConfig is null");
            return new HashMap();
        }
        return heatSourceOffsetConfig.getHeatSourceOffset(i10, map);
    }

    public int getHighTempSafetyLevel() {
        int i10;
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null || (i10 = thermalControlConfigInfo.mHighTempSafetyLevelVal) <= 11) {
                return 15;
            }
            return i10;
        }
    }

    public int getHighTemperatureThreshold() {
        ThermalControlConfigInfo thermalControlConfigInfo;
        synchronized (this.mLock) {
            if (this.mUtils.isFoldingMode()) {
                if (this.mUtils.isSplitMode()) {
                    thermalControlConfigInfo = this.mSplitConfigInfo;
                } else {
                    thermalControlConfigInfo = this.mFoldingConfigInfo;
                }
            } else {
                thermalControlConfigInfo = this.mConfigInfo;
            }
            if (thermalControlConfigInfo == null) {
                return 43;
            }
            return thermalControlConfigInfo.mHighTemperatureThreshold;
        }
    }

    public boolean getIsCrossUpdate() {
        try {
            Class<?> cls = Class.forName("android.content.pm.OplusPackageManager");
            boolean booleanValue = ((Boolean) cls.getMethod("isCrossVersionUpdate", new Class[0]).invoke(cls.getConstructor(new Class[0]).newInstance(new Object[0]), new Object[0])).booleanValue();
            LocalLog.a(TAG, "isCrossUpdate=" + booleanValue);
            return booleanValue;
        } catch (Exception e10) {
            e10.printStackTrace();
            return false;
        }
    }

    public int getOtaThermalControlVal() {
        if (SystemClock.elapsedRealtime() > FOUR_HOUR || !getIsCrossUpdate() || ((int) this.mUtils.getCurrentTemperature(false)) >= getHighTemperatureThreshold()) {
            return 0;
        }
        ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
        LocalLog.a(TAG, "mOtaThermalControlVal=" + thermalControlConfigInfo.mOtaThermalControlVal);
        return thermalControlConfigInfo.mOtaThermalControlVal;
    }

    public int getRacingHighTempSafetyLevel() {
        int i10;
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            if (thermalControlConfigInfo == null || (i10 = thermalControlConfigInfo.mRacingHighTempSafetyLevelVal) < 15) {
                return 15;
            }
            return i10;
        }
    }

    public int getRestrictTempGearVal() {
        int i10;
        int i11;
        ThermalControlConfigInfo thermalControlConfigInfo;
        ThermalControlConfigInfo thermalControlConfigInfo2;
        int i12;
        if (this.mUtils.isFoldingMode() && (thermalControlConfigInfo = this.mFoldingConfigInfo) != null && thermalControlConfigInfo.mThermalControlEnabled && thermalControlConfigInfo.mRestrictTempGearVal >= 0) {
            return (!this.mUtils.isSplitMode() || (thermalControlConfigInfo2 = this.mSplitConfigInfo) == null || !thermalControlConfigInfo2.mThermalControlEnabled || (i12 = thermalControlConfigInfo2.mRestrictTempGearVal) < 0) ? this.mFoldingConfigInfo.mRestrictTempGearVal : i12;
        }
        if (isAmbientXmlMode()) {
            if (this.mUtils.getAmbientTempState() == 3) {
                ThermalControlConfigInfo thermalControlConfigInfo3 = this.mHighAmbientConfigInfo;
                if (thermalControlConfigInfo3.mThermalControlEnabled && (i11 = thermalControlConfigInfo3.mRestrictTempGearVal) >= 0) {
                    return i11;
                }
            }
            if (this.mUtils.getAmbientTempState() == 1) {
                ThermalControlConfigInfo thermalControlConfigInfo4 = this.mLowAmbientConfigInfo;
                if (thermalControlConfigInfo4.mThermalControlEnabled && (i10 = thermalControlConfigInfo4.mRestrictTempGearVal) >= 0) {
                    return i10;
                }
            }
        }
        if (this.mConfigInfo.mRestrictTempGearVal < 0) {
            return 0;
        }
        LocalLog.a(TAG, "getThermalControlConfigInfo: default");
        return this.mConfigInfo.mRestrictTempGearVal;
    }

    public long getSafetyProtectMsgDelayVal() {
        return getThermalControlConfigInfo().mSafetyProtectMsgDelayVal * 1000;
    }

    public String getSafetyTestConfig() {
        ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
        LocalLog.a(TAG, "mSafetyTestConfigList=" + thermalControlConfigInfo.mSafetyTestConfigList);
        return thermalControlConfigInfo.mSafetyTestConfigList;
    }

    public int getSafetyThermalLevelVal() {
        ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
        LocalLog.a(TAG, "mSafetyThermalLevelVal=" + thermalControlConfigInfo.mSafetyThermalLevelVal);
        return thermalControlConfigInfo.mSafetyThermalLevelVal;
    }

    public ThermalAmbientPolicy getThermalAmbientPolicy(int i10) {
        return this.mConfigInfo.mAmbientConfig.get(i10);
    }

    public String getThermalConfig() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.mUtils.isFoldingMode() && this.mFoldingConfigInfo.mThermalControlEnabled) {
            if (this.mUtils.isSplitMode()) {
                ThermalControlConfigInfo thermalControlConfigInfo = this.mSplitConfigInfo;
                if (thermalControlConfigInfo.mThermalControlEnabled) {
                    stringBuffer.append(thermalControlConfigInfo);
                }
            }
            stringBuffer.append(this.mFoldingConfigInfo);
        } else {
            stringBuffer.append(this.mConfigInfo);
        }
        stringBuffer.append("isSpecialHighTemp=" + AppFeature.A() + "\n");
        stringBuffer.append("safety_test_need_enable=" + AppFeature.D() + "\n");
        stringBuffer.append("feature_ambient_policy_mode=" + this.mConfigInfo.mAmbientPolicyMode + "\n\n");
        stringBuffer.append(this.mWindowConfigInfo);
        stringBuffer.append("\nLowAmbientConfigVersion=" + this.mLowAmbientConfigInfo.mConfigVersion);
        stringBuffer.append("\nHighAmbientConfigVersion=" + this.mHighAmbientConfigInfo.mConfigVersion);
        return stringBuffer.toString();
    }

    public CpuLevelConfig.ThermalCpuLevelPolicy getThermalCpuLevelPolicy(int i10) {
        CpuLevelConfig cpuLevelConfig = (CpuLevelConfig) getThermalControlConfigInfo().getFeatureConfigBy("level", CpuLevelConfig.CONFIG_NAME, CpuLevelConfig.class);
        if (cpuLevelConfig == null) {
            LocalLog.b(TAG, "cpu featureConfig is null");
            return new CpuLevelConfig.ThermalCpuLevelPolicy(i10);
        }
        return cpuLevelConfig.getThermalCpuLevelPolicy(i10);
    }

    public GpuLevelConfig.ThermalGpuLevelPolicy getThermalGpuLevelPolicy(int i10) {
        GpuLevelConfig gpuLevelConfig = (GpuLevelConfig) getThermalControlConfigInfo().getFeatureConfigBy("level", GpuLevelConfig.CONFIG_NAME, GpuLevelConfig.class);
        if (gpuLevelConfig == null) {
            LocalLog.b(TAG, "gpu featureConfig is null");
            return new GpuLevelConfig.ThermalGpuLevelPolicy(i10);
        }
        return gpuLevelConfig.getThermalGpuLevelPolicy(i10);
    }

    public int getThermalOptimizationReportLevel() {
        int i10;
        synchronized (this.mLock) {
            i10 = getThermalControlConfigInfo().mThermalOptimizationReportLevel;
        }
        return i10;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0378 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ThermalPolicy getThermalPolicyInfo(int i10, String str, String str2, String str3, int i11, boolean z10) {
        ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap;
        ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap2;
        String str4;
        ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap3;
        ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap4;
        ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap5;
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo();
            ThermalPolicy thermalPolicy = null;
            if (thermalControlConfigInfo == null) {
                LocalLog.b(TAG, "configInfo is null.");
                return null;
            }
            String str5 = CONFIG_TYPE_DEFAULT;
            if (this.mUtils.getAmbientTemperature() > 0 && this.mUtils.getAmbientTemperature() < thermalControlConfigInfo.mEnvironmentThresholdVal) {
                str5 = ThermalPolicy.KEY_THERMAL_ENVIRONMENT;
            }
            if (i10 != -1) {
                if (i10 == 1) {
                    ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap6 = thermalControlConfigInfo.mSafetySceneConfig;
                    if (arrayMap6 != null && !arrayMap6.isEmpty()) {
                        if (thermalControlConfigInfo.mSafetySceneConfig.containsKey(str)) {
                            thermalPolicy = thermalControlConfigInfo.mSafetySceneConfig.get(str).get(i11);
                        } else if (thermalControlConfigInfo.mSafetySceneConfig.containsKey(ORIGINAL_SAFETY_TEST_ITEM)) {
                            thermalPolicy = thermalControlConfigInfo.mSafetySceneConfig.get(ORIGINAL_SAFETY_TEST_ITEM).get(i11);
                            str = ORIGINAL_SAFETY_TEST_ITEM;
                        }
                        if (thermalPolicy != null) {
                        }
                        return thermalPolicy;
                    }
                    str = null;
                    if (thermalPolicy != null) {
                    }
                    return thermalPolicy;
                }
                if (i10 == 2) {
                    ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap7 = thermalControlConfigInfo.mScreenOffConfig;
                    if (arrayMap7 != null && !arrayMap7.isEmpty()) {
                        if (thermalControlConfigInfo.mScreenOffConfig.containsKey(str)) {
                            thermalPolicy = thermalControlConfigInfo.mScreenOffConfig.get(str).get(i11);
                        } else if (thermalControlConfigInfo.mScreenOffConfig.containsKey(ORIGINAL_SCREEN_OFF_ITEM)) {
                            thermalPolicy = thermalControlConfigInfo.mScreenOffConfig.get(ORIGINAL_SCREEN_OFF_ITEM).get(i11);
                            str = ORIGINAL_SCREEN_OFF_ITEM;
                        }
                        if (thermalPolicy != null) {
                        }
                        return thermalPolicy;
                    }
                    str = null;
                    if (thermalPolicy != null) {
                    }
                    return thermalPolicy;
                }
                if (i10 != 3) {
                    if (i10 != 4) {
                        if (i10 == 5) {
                            if (ThermalPolicy.KEY_THERMAL_ENVIRONMENT.equals(str5) && (arrayMap5 = thermalControlConfigInfo.mSpecificAppEnvConfig) != null && !arrayMap5.isEmpty()) {
                                str4 = str2 + "_" + str5;
                                if (thermalControlConfigInfo.mSpecificAppEnvConfig.containsKey(str4)) {
                                    thermalPolicy = thermalControlConfigInfo.mSpecificAppEnvConfig.get(str4).get(i11);
                                    str = str4;
                                }
                            }
                            ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap8 = thermalControlConfigInfo.mSpecificAppConfig;
                            if (arrayMap8 != null && !arrayMap8.isEmpty() && thermalControlConfigInfo.mSpecificAppConfig.containsKey(str2)) {
                                thermalPolicy = thermalControlConfigInfo.mSpecificAppConfig.get(str2).get(i11);
                                str = str2;
                            } else {
                                ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap9 = thermalControlConfigInfo.mDefaultConfig;
                                if (arrayMap9 != null && !arrayMap9.isEmpty()) {
                                    if (thermalControlConfigInfo.mDefaultConfig.containsKey(str)) {
                                        thermalPolicy = thermalControlConfigInfo.mDefaultConfig.get(str).get(i11);
                                    } else if (thermalControlConfigInfo.mDefaultConfig.containsKey(ORIGINAL_DEFAULT_ITEM)) {
                                        thermalPolicy = thermalControlConfigInfo.mDefaultConfig.get(ORIGINAL_DEFAULT_ITEM).get(i11);
                                        str = ORIGINAL_DEFAULT_ITEM;
                                    }
                                }
                            }
                        }
                        str = null;
                    } else {
                        if (ThermalPolicy.KEY_THERMAL_ENVIRONMENT.equals(str5) && (arrayMap4 = thermalControlConfigInfo.mSpecificAppEnvConfig) != null && !arrayMap4.isEmpty()) {
                            str4 = str2 + "_" + str5;
                            if (thermalControlConfigInfo.mSpecificAppEnvConfig.containsKey(str4)) {
                                thermalPolicy = thermalControlConfigInfo.mSpecificAppEnvConfig.get(str4).get(i11);
                                str = str4;
                            }
                        }
                        ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap10 = thermalControlConfigInfo.mSpecificAppConfig;
                        if (arrayMap10 != null && !arrayMap10.isEmpty() && thermalControlConfigInfo.mSpecificAppConfig.containsKey(str2)) {
                            thermalPolicy = thermalControlConfigInfo.mSpecificAppConfig.get(str2).get(i11);
                            str = str2;
                        } else {
                            ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap11 = thermalControlConfigInfo.mAppCategoryConfig;
                            if (arrayMap11 != null && !arrayMap11.isEmpty()) {
                                if (thermalControlConfigInfo.mAppCategoryConfig.containsKey(str)) {
                                    thermalPolicy = thermalControlConfigInfo.mAppCategoryConfig.get(str).get(i11);
                                } else if (thermalControlConfigInfo.mAppCategoryConfig.containsKey(str3)) {
                                    thermalPolicy = thermalControlConfigInfo.mAppCategoryConfig.get(str3).get(i11);
                                    str = str3;
                                }
                            }
                            ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap12 = thermalControlConfigInfo.mDefaultConfig;
                            if (arrayMap12 != null && !arrayMap12.isEmpty()) {
                                if (thermalControlConfigInfo.mDefaultConfig.containsKey(str)) {
                                    thermalPolicy = thermalControlConfigInfo.mDefaultConfig.get(str).get(i11);
                                } else if (thermalControlConfigInfo.mDefaultConfig.containsKey(ORIGINAL_DEFAULT_ITEM)) {
                                    thermalPolicy = thermalControlConfigInfo.mDefaultConfig.get(ORIGINAL_DEFAULT_ITEM).get(i11);
                                    str = ORIGINAL_DEFAULT_ITEM;
                                }
                            }
                            str = null;
                        }
                    }
                    if (thermalPolicy != null || str == null || str.equals(thermalPolicy.categoryName)) {
                        return thermalPolicy;
                    }
                    ThermalPolicy m26clone = thermalPolicy.m26clone();
                    m26clone.categoryName = str;
                    return m26clone;
                }
            }
            if (z10 && !this.mUtils.isFloatWindowOn() && (arrayMap3 = thermalControlConfigInfo.mSpecificAppSceneConfig) != null && !arrayMap3.isEmpty()) {
                str4 = str2 + "_" + str;
                if (thermalControlConfigInfo.mSpecificAppSceneConfig.containsKey(str4)) {
                    thermalPolicy = thermalControlConfigInfo.mSpecificAppSceneConfig.get(str4).get(i11);
                    str = str4;
                    if (thermalPolicy != null) {
                    }
                    return thermalPolicy;
                }
            }
            if (ThermalPolicy.KEY_THERMAL_ENVIRONMENT.equals(str5) && (arrayMap2 = thermalControlConfigInfo.mSpecificAppEnvConfig) != null && !arrayMap2.isEmpty()) {
                str4 = str2 + "_" + str5;
                if (thermalControlConfigInfo.mSpecificAppEnvConfig.containsKey(str4)) {
                    thermalPolicy = thermalControlConfigInfo.mSpecificAppEnvConfig.get(str4).get(i11);
                    str = str4;
                    if (thermalPolicy != null) {
                    }
                    return thermalPolicy;
                }
            }
            if (z10 && (arrayMap = thermalControlConfigInfo.mSpecificAppConfig) != null && !arrayMap.isEmpty() && thermalControlConfigInfo.mSpecificAppConfig.containsKey(str2)) {
                thermalPolicy = thermalControlConfigInfo.mSpecificAppConfig.get(str2).get(i11);
                str = str2;
                if (thermalPolicy != null) {
                }
                return thermalPolicy;
            }
            ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap13 = thermalControlConfigInfo.mSceneConfig;
            if (arrayMap13 != null && !arrayMap13.isEmpty() && thermalControlConfigInfo.mSceneConfig.containsKey(str)) {
                thermalPolicy = thermalControlConfigInfo.mSceneConfig.get(str).get(i11);
            } else {
                ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap14 = thermalControlConfigInfo.mAppCategoryConfig;
                if (arrayMap14 != null && !arrayMap14.isEmpty() && thermalControlConfigInfo.mAppCategoryConfig.containsKey(str3)) {
                    thermalPolicy = thermalControlConfigInfo.mAppCategoryConfig.get(str3).get(i11);
                    str = str3;
                } else {
                    ArrayMap<String, SparseArray<ThermalPolicy>> arrayMap15 = thermalControlConfigInfo.mDefaultConfig;
                    if (arrayMap15 != null && !arrayMap15.isEmpty()) {
                        if (thermalControlConfigInfo.mDefaultConfig.containsKey(str)) {
                            thermalPolicy = thermalControlConfigInfo.mDefaultConfig.get(str).get(i11);
                        } else if (thermalControlConfigInfo.mDefaultConfig.containsKey(ORIGINAL_DEFAULT_ITEM)) {
                            thermalPolicy = thermalControlConfigInfo.mDefaultConfig.get(ORIGINAL_DEFAULT_ITEM).get(i11);
                            str = ORIGINAL_DEFAULT_ITEM;
                        }
                    }
                    str = null;
                }
            }
            if (thermalPolicy != null) {
            }
            return thermalPolicy;
        }
    }

    public int getThermalSerious(int i10, int i11) {
        if (i11 != -2) {
            return i11;
        }
        TemperatureLevelConfig temperatureLevelConfig = (TemperatureLevelConfig) getThermalControlConfigInfo().getFeatureConfigBy("level", TemperatureLevelConfig.CONFIG_NAME, TemperatureLevelConfig.class);
        if (temperatureLevelConfig == null) {
            return -2;
        }
        return temperatureLevelConfig.getTempSeriousLevel(i10);
    }

    public Map<String, TsensorConfig.ThermalTsensorPolicy> getTsensorPolicy(Map<String, Integer> map) {
        TsensorConfig tsensorConfig = (TsensorConfig) getThermalControlConfigInfo().getFeatureConfigBy("tsensor_feature", TsensorConfig.CONFIG_NAME, TsensorConfig.class);
        if (tsensorConfig == null) {
            LocalLog.b(TAG, "TsensorPolicy featureConfig is null");
            return new ArrayMap();
        }
        return tsensorConfig.getTsensorPolicy(map);
    }

    public int getZoomWindowRestrictPolicy(String str, String str2, int i10) {
        return this.mWindowConfigInfo.getRestrictPolicy(str, str2, i10);
    }

    public boolean isAgingThermalControlEnable() {
        return getThermalControlConfigInfo().mAgingThermalControlEnabled;
    }

    public boolean isAmbientOffsetMode() {
        return getAmbientPolicyModeVal() == 1;
    }

    public boolean isAmbientPolicyEnable() {
        return getAmbientPolicyModeVal() != 0;
    }

    public boolean isAmbientXmlMode() {
        return getAmbientPolicyModeVal() == 2;
    }

    public boolean isIpaFeatureEnable() {
        synchronized (this.mLock) {
            ThermalControlConfigInfo thermalControlConfigInfo = this.mConfigInfo;
            if (thermalControlConfigInfo == null) {
                return false;
            }
            return thermalControlConfigInfo.mIpaEnabled;
        }
    }

    public boolean isSafetyOptimizeEnabled() {
        return getThermalControlConfigInfo().mSafetyOptimizeEnabled;
    }

    public boolean isSafetyTestEnable() {
        return getThermalControlConfigInfo().mSafetyTestEnabled;
    }

    public boolean isThermalControlEnable() {
        return getThermalControlConfigInfo().mThermalControlEnabled;
    }

    public boolean isTsensorExceptScene(TsensorConfig.ThermalTsensorPolicy thermalTsensorPolicy, String str, String str2) {
        TsensorExceptScenesConfig tsensorExceptScenesConfig = (TsensorExceptScenesConfig) getThermalControlConfigInfo().getFeatureConfigBy("tsensor_feature", TsensorExceptScenesConfig.CONFIG_NAME, TsensorExceptScenesConfig.class);
        if (tsensorExceptScenesConfig == null) {
            LocalLog.b(TAG, "ExceptScenes featureConfig is null");
            return false;
        }
        return tsensorExceptScenesConfig.isExceptScene(thermalTsensorPolicy, str, str2);
    }

    public void noteAmbientThermalControlConfigChange(Context context, boolean z10) {
        if (isAmbientXmlMode() && !this.mFoldingConfigInfo.mThermalControlEnabled && !this.mSplitConfigInfo.mThermalControlEnabled) {
            String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, z10 ? LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM : HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
            if (TextUtils.isEmpty(dataFromProvider)) {
                return;
            }
            if (parseVersionFromRUS(dataFromProvider, false, false, z10, !z10) > parseVersionFromDefault(false, false, z10, !z10)) {
                clearAllPolicyConfigMap(false, false, z10, !z10);
                parseFilterListFromXml(dataFromProvider, false, false, z10, !z10);
                LocalLog.l(TAG, "Use Config RUS!");
                return;
            } else {
                clearAllPolicyConfigMap(false, false, z10, !z10);
                addDefaultConfig(false, false, z10, !z10);
                LocalLog.l(TAG, "Use Config local!");
                return;
            }
        }
        LocalLog.a(TAG, "no need to load ambient config info");
    }

    public void noteFoldingThermalControlConfigChange(Context context) {
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, FOLDING_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        if (TextUtils.isEmpty(dataFromProvider)) {
            return;
        }
        if (parseVersionFromRUS(dataFromProvider, true, false) > parseVersionFromDefault(true, false)) {
            clearAllPolicyConfigMap(true, false);
            parseFilterListFromXml(dataFromProvider, true, false);
            LocalLog.l(TAG, "Use folding Config RUS!");
        } else {
            clearAllPolicyConfigMap(true, false);
            addDefaultConfig(true, false);
            LocalLog.l(TAG, "Use folding Config local!");
        }
    }

    public void noteGTState(boolean z10) {
        if (y5.b.D() && z10) {
            String dataFromProvider = ThermalConfigUtil.getDataFromProvider(this.mContext, "sys_thermal_control_gt_list");
            if (TextUtils.isEmpty(dataFromProvider)) {
                clearAllPolicyConfigMap(false, false);
                addGTDefaultConfig();
            } else if (parseVersionFromRUS(dataFromProvider, false, false) > parseVersionFromGTDefault()) {
                clearAllPolicyConfigMap(false, false);
                parseFilterListFromXml(dataFromProvider, false, false);
            } else {
                clearAllPolicyConfigMap(false, false);
                addGTDefaultConfig();
            }
        } else {
            String dataFromProvider2 = ThermalConfigUtil.getDataFromProvider(this.mContext, PRE_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
            if (!TextUtils.isEmpty(dataFromProvider2)) {
                clearAllPolicyConfigMap(false, false);
                parseFilterListFromXml(dataFromProvider2, false, false);
            } else {
                clearAllPolicyConfigMap(false, false);
                addDefaultConfig(false, false);
            }
        }
        checkAmbientPolicyModeChange();
    }

    public void noteGTThermalControlConfigChange(Context context) {
        if (y5.b.D()) {
            String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, "sys_thermal_control_gt_list");
            if (TextUtils.isEmpty(dataFromProvider) || !isGTmodeOn()) {
                return;
            }
            if (parseVersionFromRUS(dataFromProvider, false, false) > parseVersionFromGTDefault()) {
                if (isGTmodeOn()) {
                    clearAllPolicyConfigMap(false, false);
                    parseFilterListFromXml(dataFromProvider, false, false);
                }
            } else {
                clearAllPolicyConfigMap(false, false);
                addGTDefaultConfig();
            }
            checkAmbientPolicyModeChange();
        }
    }

    public void noteSplitThermalControlConfigChange(Context context) {
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, SPLIT_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        if (TextUtils.isEmpty(dataFromProvider)) {
            return;
        }
        if (parseVersionFromRUS(dataFromProvider, false, true) > parseVersionFromDefault(false, true)) {
            clearAllPolicyConfigMap(false, true);
            parseFilterListFromXml(dataFromProvider, false, true);
            LocalLog.l(TAG, "Use Config RUS!");
        } else {
            clearAllPolicyConfigMap(false, true);
            addDefaultConfig(false, true);
            LocalLog.l(TAG, "Use Config local!");
        }
    }

    public void noteThermalControlConfigChange(Context context) {
        String dataFromProvider = ThermalConfigUtil.getDataFromProvider(context, PRE_THERMAL_CONTROL_XML_FILE_ROOT_ITEM);
        if (TextUtils.isEmpty(dataFromProvider)) {
            return;
        }
        if (parseVersionFromRUS(dataFromProvider, false, false) > parseVersionFromDefault(false, false)) {
            clearAllPolicyConfigMap(false, false);
            parseFilterListFromXml(dataFromProvider, false, false);
            LocalLog.l(TAG, "Use Config RUS!");
        } else {
            clearAllPolicyConfigMap(false, false);
            addDefaultConfig(false, false);
            LocalLog.l(TAG, "Use Config local!");
        }
        checkAmbientPolicyModeChange();
    }

    public void noteThermalWindowConfigChange(Context context) {
        this.mWindowConfigInfo.noteThermalWindowConfigChange(context);
    }

    private void clearAllPolicyConfigMap(boolean z10, boolean z11, boolean z12, boolean z13) {
        synchronized (this.mLock) {
            getThermalControlConfigInfo(z10, z11, z12, z13).clear();
        }
    }

    private void parseFilterListFromXml(String str, boolean z10, boolean z11, boolean z12, boolean z13) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            newInstance.setExpandEntityReferences(false);
            Document parse = newInstance.newDocumentBuilder().parse(byteArrayInputStream);
            if (parse == null) {
                LocalLog.b(TAG, "loadApListFromXml, null doc!");
                setConfigThermalControlEnabled(z10, z11, z12, z13, false);
                return;
            }
            if (z10) {
                this.mFoldingConfigInfo.mConfigFile = "sys_thermal_control_list_folding.xml";
            } else if (z11) {
                this.mSplitConfigInfo.mConfigFile = "sys_thermal_control_list_split.xml";
            } else if (z12) {
                this.mLowAmbientConfigInfo.mConfigFile = "sys_thermal_control_list_low_ambient.xml";
            } else if (z13) {
                this.mHighAmbientConfigInfo.mConfigFile = "sys_thermal_control_list_high_ambient.xml";
            } else {
                this.mConfigInfo.mConfigFile = "sys_thermal_control_list.xml";
            }
            try {
                commonParseConfigForDocument(parse, z10, z11, z12, z13);
            } catch (Exception e10) {
                LocalLog.b(TAG, "Exception:" + e10);
            }
        } catch (IOException e11) {
            LocalLog.b(TAG, "IOException:" + e11);
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
        } catch (ParserConfigurationException e12) {
            LocalLog.b(TAG, "ParserConfigurationException:" + e12);
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
        } catch (SAXException e13) {
            LocalLog.b(TAG, "SAXException:" + e13);
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
        }
    }

    private long parseVersionFromDefault(boolean z10, boolean z11, boolean z12, boolean z13) {
        File thermalConfigFile;
        Document parse;
        Element element;
        String tagName;
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            if (z10) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(FOLDING_THERMAL_CONTROL_XML_FILE_PATH);
            } else if (z11) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(SPLIT_THERMAL_CONTROL_XML_FILE_PATH);
            } else if (z12) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH);
            } else if (z13) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH);
            } else {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(PRE_THERMAL_CONTROL_XML_FILE_PATH);
            }
            InputStream decryptXmlFile = ThermalConfigXmlEncryption.decryptXmlFile(thermalConfigFile.getPath());
            if (decryptXmlFile != null) {
                parse = newDocumentBuilder.parse(decryptXmlFile);
            } else {
                parse = newDocumentBuilder.parse(thermalConfigFile);
            }
            if (parse == null) {
                LocalLog.b(TAG, "loadApListFromXml, null doc!");
                return 0L;
            }
            Element documentElement = parse.getDocumentElement();
            if (!checkXmlRootItem(documentElement.getTagName().trim(), z10, z11, z12, z13)) {
                return 0L;
            }
            NodeList childNodes = documentElement.getChildNodes();
            int i10 = 0;
            while (true) {
                if (i10 >= childNodes.getLength()) {
                    break;
                }
                Node item = childNodes.item(i10);
                if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null && tagName.equals("version")) {
                    ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo(z10, z11, z12, z13);
                    String textContent = element.getTextContent();
                    thermalControlConfigInfo.mDefaultConfigVersion = textContent;
                    if (ThermalConfigUtil.isNumeric(textContent)) {
                        return Long.parseLong(thermalControlConfigInfo.mDefaultConfigVersion);
                    }
                    LocalLog.l(TAG, "parseVersion: not numeric. mDefaultConfigVersion: " + thermalControlConfigInfo.mDefaultConfigVersion);
                } else {
                    i10++;
                }
            }
            return 0L;
        } catch (IOException e10) {
            LocalLog.b(TAG, "IOException:" + e10);
            return 0L;
        } catch (ParserConfigurationException e11) {
            LocalLog.b(TAG, "ParserConfigurationException:" + e11);
            return 0L;
        } catch (SAXException e12) {
            LocalLog.b(TAG, "SAXException:" + e12);
            return 0L;
        }
    }

    private long parseVersionFromRUS(String str, boolean z10, boolean z11, boolean z12, boolean z13) {
        Element documentElement;
        Element element;
        String tagName;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
            DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
            int i10 = 0;
            newInstance.setExpandEntityReferences(false);
            Document parse = newInstance.newDocumentBuilder().parse(byteArrayInputStream);
            if (parse == null) {
                LocalLog.b(TAG, "loadApListFromXml, null doc!");
                return 0L;
            }
            try {
                documentElement = parse.getDocumentElement();
            } catch (Exception e10) {
                LocalLog.b(TAG, "Exception:" + e10);
            }
            if (!checkXmlRootItem(documentElement.getTagName().trim(), z10, z11, z12, z13)) {
                return 0L;
            }
            NodeList childNodes = documentElement.getChildNodes();
            while (true) {
                if (i10 >= childNodes.getLength()) {
                    break;
                }
                Node item = childNodes.item(i10);
                if ((item instanceof Element) && (tagName = (element = (Element) item).getTagName()) != null && tagName.equals("version")) {
                    ThermalControlConfigInfo thermalControlConfigInfo = getThermalControlConfigInfo(z10, z11, z12, z13);
                    String textContent = element.getTextContent();
                    thermalControlConfigInfo.mRusConfigVersion = textContent;
                    if (ThermalConfigUtil.isNumeric(textContent)) {
                        return Long.parseLong(thermalControlConfigInfo.mRusConfigVersion);
                    }
                    LocalLog.l(TAG, "parseVersion: not numeric. mRusConfigVersion: " + thermalControlConfigInfo.mRusConfigVersion);
                }
                i10++;
            }
            return 0L;
        } catch (IOException e11) {
            LocalLog.b(TAG, "IOException:" + e11);
            return 0L;
        } catch (ParserConfigurationException e12) {
            LocalLog.b(TAG, "ParserConfigurationException:" + e12);
            return 0L;
        } catch (SAXException e13) {
            LocalLog.b(TAG, "SAXException:" + e13);
            return 0L;
        }
    }

    public void addDefaultConfig(boolean z10, boolean z11, boolean z12, boolean z13) {
        File thermalConfigFile;
        Document parse;
        try {
            DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            if (z10) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(FOLDING_THERMAL_CONTROL_XML_FILE_PATH);
            } else if (z11) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(SPLIT_THERMAL_CONTROL_XML_FILE_PATH);
            } else if (z12) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH);
            } else if (z13) {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH);
            } else {
                thermalConfigFile = ThermalConfigUtil.thermalConfigFile(PRE_THERMAL_CONTROL_XML_FILE_PATH);
            }
            if (XML_DBG) {
                LocalLog.d(TAG, "loadApListFromXml, parsing " + thermalConfigFile.getPath());
            }
            InputStream decryptXmlFile = ThermalConfigXmlEncryption.decryptXmlFile(thermalConfigFile.getPath());
            if (decryptXmlFile != null) {
                parse = newDocumentBuilder.parse(decryptXmlFile);
            } else {
                parse = newDocumentBuilder.parse(thermalConfigFile);
            }
            if (parse == null) {
                LocalLog.b(TAG, "loadApListFromXml, null doc!");
                setConfigThermalControlEnabled(z10, z11, z12, z13, false);
                return;
            }
            if (z10) {
                this.mFoldingConfigInfo.mConfigFile = FOLDING_THERMAL_CONTROL_XML_FILE_PATH;
            } else if (z11) {
                this.mSplitConfigInfo.mConfigFile = SPLIT_THERMAL_CONTROL_XML_FILE_PATH;
            } else if (z12) {
                this.mLowAmbientConfigInfo.mConfigFile = LOW_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH;
            } else if (z13) {
                this.mHighAmbientConfigInfo.mConfigFile = HIGH_AMBIENT_THERMAL_CONTROL_XML_FILE_PATH;
            } else {
                this.mConfigInfo.mConfigFile = PRE_THERMAL_CONTROL_XML_FILE_PATH;
            }
            commonParseConfigForDocument(parse, z10, z11, z12, z13);
        } catch (IOException e10) {
            LocalLog.b(TAG, "IOException:" + e10);
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
        } catch (ParserConfigurationException e11) {
            LocalLog.b(TAG, "ParserConfigurationException:" + e11);
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
        } catch (SAXException e12) {
            LocalLog.b(TAG, "SAXException:" + e12);
            setConfigThermalControlEnabled(z10, z11, z12, z13, false);
        }
    }

    private ThermalControlConfigInfo getThermalControlConfigInfo() {
        ThermalControlConfigInfo thermalControlConfigInfo;
        ThermalControlConfigInfo thermalControlConfigInfo2;
        if (this.mUtils.isFoldingMode() && (thermalControlConfigInfo = this.mFoldingConfigInfo) != null && thermalControlConfigInfo.mThermalControlEnabled) {
            return (this.mUtils.isSplitMode() && (thermalControlConfigInfo2 = this.mSplitConfigInfo) != null && thermalControlConfigInfo2.mThermalControlEnabled) ? thermalControlConfigInfo2 : this.mFoldingConfigInfo;
        }
        if (isAmbientXmlMode()) {
            if (this.mUtils.getAmbientTempState() == 3) {
                ThermalControlConfigInfo thermalControlConfigInfo3 = this.mHighAmbientConfigInfo;
                if (thermalControlConfigInfo3.mThermalControlEnabled) {
                    return thermalControlConfigInfo3;
                }
            }
            if (this.mUtils.getAmbientTempState() == 1) {
                ThermalControlConfigInfo thermalControlConfigInfo4 = this.mLowAmbientConfigInfo;
                if (thermalControlConfigInfo4.mThermalControlEnabled) {
                    return thermalControlConfigInfo4;
                }
            }
        }
        return this.mConfigInfo;
    }
}
