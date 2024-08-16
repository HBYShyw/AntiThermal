package com.oplus.uamodel;

import android.content.pm.OplusPackageManager;
import android.os.Build;
import android.os.OplusPropertyList;
import android.os.SystemProperties;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class OplusModelUtil {
    private static final String LOWER_CASE_MODEL_NAME = "pfdm00";
    private static final int MODEL_NAME_ADAPTED_LIST = 740;
    private static final String MODEL_NAME_BETA = "F19";
    private static final String MODEL_NAME_BLADE_A = "10T 5G";
    private static final String MODEL_NAME_CHAKA_PK = "Reno5 Pro";
    private static final String MODEL_NAME_CHONGQING_B = "C65 5G";
    private static final String MODEL_NAME_COFFEE_A = "真我12 Pro至尊版";
    private static final int MODEL_NAME_CUSTOM_LIST = 738;
    private static final String MODEL_NAME_ELSA_EEA = "A94 5G";
    private static final String MODEL_NAME_HIMA_EEA = "Find X3 Neo 5G";
    private static final String MODEL_NAME_KUNLUN_EEA = "Find X3 Lite 5G";
    private static final int MODEL_NAME_MARKET_LIST = 739;
    private static final String MODEL_NAME_MIAMIB = "narzo N55";
    private static final int MODEL_NAME_NFC_COMPATIBLE = 737;
    private static final String MODEL_NAME_PANTHER = "F21 Pro";
    private static final String MODEL_NAME_ZHAOYUN = "A57s";
    private static final String ONLY_WHITELIST_FEATURE = "ONLY_WHITELIST_FEATURE";
    private static final String TAG = "UAmodel";
    private String mOplusModel = Build.MODEL;
    private static final String LITTLE_TAIL_MODEL = OplusPropertyList.PRODUCT_LITTLETAIL_MODEL;
    private static final String OPLUS_MODEL = OplusPropertyList.PRODUCT_OPLUS_MODEL;
    private static final String OPLUS_CONFIDENTIAL_MODEL = OplusPropertyList.ODM_PREV_PRODUCT_NAME;
    private static final String CONFIDENTIAL_PROP = OplusPropertyList.VERSION_CONFIDENTIAL;
    private static final String BUILD_MODEL = Build.MODEL;
    private static final String BUILD_DEVICE = Build.DEVICE;
    private static final String MANUFACTURER = Build.MANUFACTURER;
    private static final String OPLUS_MARKET_NAME = OplusPropertyList.OPLUS_VENDOR_MARKET_NAME;
    private static final String THEME = SystemProperties.get(OplusPropertyList.PROPERTY_HW_PHONE_OPLUS);
    private static final String BARCE_CUSTOM = SystemProperties.get("ro.product.oplus.custom.Barce");
    private static final String GUNDAM = SystemProperties.get("ro.vendor.oplus.gundam");
    private static final List<String> UAMODELLIST_ANTUTU = Arrays.asList("com.antutu.ABenchMark", "com.antutu.aibenchmark", "com.antutu.ABenchMark.lite", "com.uzywpq.cqlzahm", "com.antutu.benchmark.full", "club.antutu.benchmark");
    private static final List<String> UAMODELLIST_FACEBOOK = Arrays.asList("com.facebook.katana");
    private static final List<String> UAMODELLIST_SPECIALCASE = Arrays.asList("flar2.devcheck", "com.finalwire.aida64");
    private static final List<String> UAMODELLIST_BENCHMARK_CUSTOMIZED = Arrays.asList("com.ludashi.benchmark");
    private static final List<String> UAMODELLIST_LITTLETAIL_CUSTOMIZED = Arrays.asList("com.tencent.mobileqq", "com.sina.weibo", "com.ruanmei.ithome", "com.qzone", "com.tencent.karaoke", "com.netease.newsreader.activity", "com.android.sina.weibo.ua", "com.sinamobile.uagenerator");
    private static final List<String> UAMODELLIST_LITTLETAIL_CUSTOMIZED_LAST = Arrays.asList("com.xunmeng.pinduoduo", "com.xunmeng.merchant", "com.wuba.zhuanzhuan", "com.huodao.hdphone", "aihuishou.aihuishouapp", "com.tencent.mm", "com.quark.browser", "com.oppo.community", "com.oppo.store");
    private static final List<String> UAMODELLIST_LITTLETAIL_SPECIALMODEL = Arrays.asList("com.sina.weibo", "com.android.sina.weibo.ua", "com.tencent.mobileqq", "com.qzone", "com.netease.newsreader.activity", "com.heytap.reader");
    private static final List<String> UAMODELLIST_OPLUSCONFIDENTIALMODEL = Arrays.asList("com.tencent.mm", "com.tencent.mobileqq");
    private static final List<String> UAMODELLIST_LITTLETAIL_CUSTOMIZED_EARLY = Arrays.asList("com.tencent.mobileqq", "com.sina.weibo", "com.ruanmei.ithome", "com.qzone", "com.tencent.karaoke", "com.android.sina.weibo.ua", "com.sinamobile.uagenerator");
    private static final List<String> UAMODELLIST_ANTUTU_EARLY = Arrays.asList("com.antutu.ABenchMark", "com.ss.android.ugc.aweme", "com.ss.android.ugc.live", "com.ss.android.ugc.aweme.lite", "com.ss.android.article.video", "com.oppo.usercenter", "com.heytap.vip", "com.oplus.vip", "com.heytap.usercenter");
    private static final List<String> UAMODELLIST_LITTLETAIL_EARLY = Arrays.asList("com.sinamobile.uagenerator", "com.sina.weibo", "com.android.sina.weibo.ua", "com.tencent.mobileqq", "com.qzone", "com.netease.newsreader.activity", "com.ludashi.benchmark", "com.ruanmei.ithome", "com.tencent.karaoke", "com.qiyi.video", "com.taobao.taobao", "com.tmall.wireless", "tv.pps.mobile", "aihuishou.aihuishouapp", "com.xunmeng.pinduoduo", "com.xunmeng.merchant", "com.wuba.zhuanzhuan", "com.huodao.hdphone", "com.oppo.store", "com.oppo.community", "com.jingdong.app.mall");
    private static final List<String> MODEL_DEVICE_LIST_EARLY = Arrays.asList("RMX3370", "RE5473", "RMX3371", "RE54E4L1", "RMX3562", "RE5489", "RMX3478", "PDVM00", "OP4AB5", "PEMM00");
    private static final List<String> MODEL_DEVICE_LIST_GAME = Arrays.asList("CPH2525", "CPH2569", "CPH2541");
    private static final List<String> UAMODELLIST_GAME = Arrays.asList("com.pubg.newstate");

    private boolean setLowerCaseModelCustomList(String uaPackageName, OplusPackageManager pm) {
        if (!BUILD_MODEL.equals("PFDM00") || pm == null || !pm.inCptWhiteList(MODEL_NAME_NFC_COMPATIBLE, uaPackageName)) {
            return false;
        }
        this.mOplusModel = LOWER_CASE_MODEL_NAME;
        return true;
    }

    private boolean setEarlyCustomList(String uaPackageName) {
        List<String> list = MODEL_DEVICE_LIST_EARLY;
        if (!list.contains(BUILD_MODEL) && !list.contains(BUILD_DEVICE)) {
            return false;
        }
        String str = OPLUS_MODEL;
        if (!str.equals("") && UAMODELLIST_LITTLETAIL_EARLY.contains(uaPackageName)) {
            this.mOplusModel = str;
            return true;
        }
        if (str.equals("")) {
            return false;
        }
        String str2 = OPLUS_MARKET_NAME;
        if (str2.equals("") || !UAMODELLIST_ANTUTU_EARLY.contains(uaPackageName)) {
            return false;
        }
        this.mOplusModel = str2;
        return true;
    }

    private boolean setThemeCustomList(String uaPackageName, OplusPackageManager pm) {
        boolean result = false;
        boolean requestedModel = false;
        String str = BUILD_MODEL;
        if (str.equals("PDEM10") && THEME.equals("00FFF008")) {
            requestedModel = true;
        }
        if (str.equals("PDEM30") && THEME.equals("00FFF002")) {
            requestedModel = true;
        }
        if (str.equals("PDHM00") && THEME.equals("00FFF003")) {
            requestedModel = true;
        }
        if (requestedModel) {
            String str2 = OPLUS_MODEL;
            if (!str2.equals("") && UAMODELLIST_LITTLETAIL_CUSTOMIZED_EARLY.contains(uaPackageName)) {
                this.mOplusModel = str2;
                result = true;
            }
        }
        if (requestedModel) {
            String str3 = OPLUS_MODEL;
            if (!str3.equals("") && pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
                this.mOplusModel = str3;
                result = true;
            }
        }
        if (requestedModel && !OPLUS_MODEL.equals("") && pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = Build.MODEL;
            return false;
        }
        return result;
    }

    private boolean setGUNDAMCustomList(String uaPackageName, OplusPackageManager pm) {
        boolean result = false;
        if (!GUNDAM.equals("true")) {
            return false;
        }
        String str = OPLUS_MODEL;
        if (str.equals("") || !BUILD_MODEL.equals("PCLM10")) {
            return false;
        }
        if (UAMODELLIST_LITTLETAIL_CUSTOMIZED.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = Build.MODEL;
            return false;
        }
        return result;
    }

    private boolean setBarceCustomList(String uaPackageName, OplusPackageManager pm) {
        boolean result = false;
        if (!BARCE_CUSTOM.equals("Barce")) {
            return false;
        }
        String str = OPLUS_MODEL;
        if (str.equals("") || !BUILD_MODEL.equals("PCCM00")) {
            return false;
        }
        if (UAMODELLIST_LITTLETAIL_CUSTOMIZED.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = Build.MODEL;
            return false;
        }
        return result;
    }

    private boolean setExpCustomList(String uaPackageName, OplusPackageManager pm) {
        boolean result = false;
        boolean requestedModel = false;
        boolean requestedSpecialCase = false;
        String str = OPLUS_MARKET_NAME;
        StringBuilder sb = new StringBuilder();
        String str2 = MANUFACTURER;
        if (str.equals(sb.append(str2).append(" ").append(MODEL_NAME_KUNLUN_EEA).toString()) && BUILD_MODEL.equals("CPH2145")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_HIMA_EEA) && BUILD_MODEL.equals("CPH2207")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_ELSA_EEA) && BUILD_MODEL.equals("CPH2211")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_CHAKA_PK) && BUILD_MODEL.equals("CPH2201")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_PANTHER) && BUILD_MODEL.equals("CPH2363")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_ZHAOYUN) && BUILD_MODEL.equals("CPH2385")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_BETA) && BUILD_MODEL.equals("CPH2219")) {
            requestedModel = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_MIAMIB) && BUILD_MODEL.equals("RMX3710")) {
            requestedModel = true;
            requestedSpecialCase = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_BLADE_A) && BUILD_MODEL.equals("RMX3612")) {
            requestedModel = true;
            requestedSpecialCase = true;
        }
        if (str.equals(str2 + " " + MODEL_NAME_CHONGQING_B) && BUILD_MODEL.equals("RMX3782")) {
            requestedModel = true;
            requestedSpecialCase = true;
        }
        if (str.equals(MODEL_NAME_COFFEE_A)) {
            requestedModel = true;
            requestedSpecialCase = true;
        }
        String str3 = OPLUS_MODEL;
        if (!str3.equals("") && BUILD_MODEL.equals("RMX3843") && UAMODELLIST_LITTLETAIL_EARLY.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (!str.equals("") && requestedModel && UAMODELLIST_ANTUTU.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (!str.equals("") && requestedSpecialCase && UAMODELLIST_SPECIALCASE.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (!str3.equals("") && requestedSpecialCase && UAMODELLIST_FACEBOOK.contains(uaPackageName)) {
            this.mOplusModel = str3;
            result = true;
        }
        if (requestedModel && !str.equals("") && pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (requestedModel && !str.equals("") && pm != null && pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_CUSTOM_LIST, uaPackageName)) {
            this.mOplusModel = Build.MODEL;
            return false;
        }
        return result;
    }

    private boolean setSpecialModelCustomList(String uaPackageName) {
        String str = OPLUS_MODEL;
        if (str.equals("") || !BUILD_MODEL.equals("PDRM00") || !UAMODELLIST_LITTLETAIL_SPECIALMODEL.contains(uaPackageName)) {
            return false;
        }
        this.mOplusModel = str;
        return true;
    }

    private boolean setLastModelCustomList(String uaPackageName) {
        String str = LITTLE_TAIL_MODEL;
        if (str.equals("")) {
            return false;
        }
        String str2 = BUILD_MODEL;
        if ((!str2.equals("PERM10") && !str2.equals("PHJ110") && !str2.equals("PFTM20")) || !UAMODELLIST_LITTLETAIL_CUSTOMIZED_LAST.contains(uaPackageName)) {
            return false;
        }
        this.mOplusModel = str;
        return true;
    }

    private boolean setConfidentialModelCustomList(String uaPackageName) {
        String str = OPLUS_CONFIDENTIAL_MODEL;
        if (str.equals("") || !CONFIDENTIAL_PROP.equals("true") || !UAMODELLIST_OPLUSCONFIDENTIALMODEL.contains(uaPackageName)) {
            return false;
        }
        this.mOplusModel = str;
        return true;
    }

    private boolean setSinglemodel(String uaPackageName, OplusPackageManager pm) {
        boolean result = false;
        if (!CONFIDENTIAL_PROP.equals("false")) {
            return false;
        }
        if (setExpCustomList(uaPackageName, pm)) {
            result = true;
        }
        if (setGUNDAMCustomList(uaPackageName, pm)) {
            result = true;
        }
        if (setBarceCustomList(uaPackageName, pm)) {
            result = true;
        }
        if (setThemeCustomList(uaPackageName, pm)) {
            result = true;
        }
        if (setSpecialModelCustomList(uaPackageName)) {
            result = true;
        }
        if (setLastModelCustomList(uaPackageName)) {
            result = true;
        }
        if (setEarlyCustomList(uaPackageName)) {
            result = true;
        }
        if (setLowerCaseModelCustomList(uaPackageName, pm)) {
            return true;
        }
        return result;
    }

    private boolean setCommonLittleTailmodel(String uaPackageName) {
        boolean result = false;
        if (!CONFIDENTIAL_PROP.equals("false")) {
            return false;
        }
        String str = LITTLE_TAIL_MODEL;
        if (!str.equals("") && UAMODELLIST_LITTLETAIL_CUSTOMIZED.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (!str.equals("") && UAMODELLIST_BENCHMARK_CUSTOMIZED.contains(uaPackageName)) {
            this.mOplusModel = str;
            result = true;
        }
        if (str.equals("")) {
            return result;
        }
        String str2 = OPLUS_MARKET_NAME;
        if (!str2.equals("") && UAMODELLIST_ANTUTU.contains(uaPackageName)) {
            this.mOplusModel = str2;
            return true;
        }
        return result;
    }

    private int setCommonLittleTailCptWhiteList(String uaPackageName, OplusPackageManager pm) {
        int result = 0;
        if (!CONFIDENTIAL_PROP.equals("false") || pm == null) {
            return 0;
        }
        String str = LITTLE_TAIL_MODEL;
        if (!str.equals("") && pm.inCptWhiteList(MODEL_NAME_ADAPTED_LIST, uaPackageName)) {
            this.mOplusModel = str;
            result = 1;
        }
        if (!str.equals("")) {
            String str2 = OPLUS_MARKET_NAME;
            if (!str2.equals("") && pm.inCptWhiteList(MODEL_NAME_MARKET_LIST, uaPackageName)) {
                this.mOplusModel = str2;
                result = 1;
            }
        }
        if (!str.equals("") && this.mOplusModel.equals(str) && pm.inCptWhiteList(MODEL_NAME_ADAPTED_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_ADAPTED_LIST, uaPackageName)) {
            this.mOplusModel = Build.MODEL;
            result = 2;
        }
        if (!str.equals("")) {
            String str3 = OPLUS_MARKET_NAME;
            if (!str3.equals("") && this.mOplusModel.equals(str3) && pm.inCptWhiteList(MODEL_NAME_MARKET_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_MARKET_LIST, uaPackageName)) {
                this.mOplusModel = Build.MODEL;
                result = 2;
            }
        }
        String str4 = OPLUS_MODEL;
        if (!str4.equals("") && pm.inCptWhiteList(MODEL_NAME_ADAPTED_LIST, uaPackageName)) {
            this.mOplusModel = str4;
            result = 1;
        }
        if (!str4.equals("")) {
            String str5 = OPLUS_MARKET_NAME;
            if (!str5.equals("") && pm.inCptWhiteList(MODEL_NAME_MARKET_LIST, uaPackageName)) {
                this.mOplusModel = str5;
                result = 1;
            }
        }
        if (!str4.equals("") && this.mOplusModel.equals(str4) && pm.inCptWhiteList(MODEL_NAME_ADAPTED_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_ADAPTED_LIST, uaPackageName)) {
            this.mOplusModel = Build.MODEL;
            result = 2;
        }
        if (!str4.equals("")) {
            String str6 = OPLUS_MARKET_NAME;
            if (!str6.equals("") && this.mOplusModel.equals(str6) && pm.inCptWhiteList(MODEL_NAME_MARKET_LIST, ONLY_WHITELIST_FEATURE) && !pm.inCptWhiteList(MODEL_NAME_MARKET_LIST, uaPackageName)) {
                this.mOplusModel = Build.MODEL;
                return 2;
            }
            return result;
        }
        return result;
    }

    public boolean setModelOk(String uaPackageName) {
        boolean canSetUaModel = false;
        if (UAMODELLIST_GAME.contains(uaPackageName) && MODEL_DEVICE_LIST_GAME.contains(BUILD_MODEL)) {
            this.mOplusModel = "A302OP";
            canSetUaModel = true;
        }
        OplusPackageManager pm = new OplusPackageManager();
        if (setConfidentialModelCustomList(uaPackageName)) {
            canSetUaModel = true;
        }
        if (setSinglemodel(uaPackageName, pm)) {
            canSetUaModel = true;
        }
        if (setCommonLittleTailmodel(uaPackageName)) {
            canSetUaModel = true;
        }
        if (setCommonLittleTailCptWhiteList(uaPackageName, pm) == 1) {
            return true;
        }
        if (setCommonLittleTailCptWhiteList(uaPackageName, pm) == 2) {
            return false;
        }
        return canSetUaModel;
    }

    public void changeToSpecialModel() {
        try {
            Field field = Build.class.getField("MODEL");
            field.setAccessible(true);
            field.set(Build.class, this.mOplusModel);
        } catch (IllegalAccessException | NoSuchFieldException e) {
        }
    }
}
