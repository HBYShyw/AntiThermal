package com.oplus.resolver;

import android.app.OplusActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.app.chooser.DisplayResolveInfo;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusResolveData;
import com.oplus.util.OplusResolverIntentUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public class OplusResolverInfoHelper {
    private static final String APP_EVENT_ID = "resolver_app";
    private static final String CODE = "20120";
    private static final String DEFAULT_TYPE = "all";
    private static final String GALLERY_PIN_LIST = "gallery_pin_list";
    private static final String KEY_ACTION = "action";
    private static final String KEY_CATEGORIES = "categories";
    private static final String KEY_CHOOSE = "isChooser";
    private static final String KEY_COMPONENT_LABEL = "componentLabel";
    private static final String KEY_DATA = "data";
    private static final String KEY_INTENT = "intent";
    private static final String KEY_MIME_TYPE = "mimeType";
    private static final String KEY_NAME = "name";
    private static final String KEY_POSITION = "position";
    private static final String KEY_REFERRER_PACKAGE = "referrerPackage";
    private static final String KEY_SCHEME = "scheme";
    private static final String KEY_TARGET_PACKAGE = "targetPackage";
    private static final String KEY_TYPE = "type";
    private static final String SHOW_EVENT_ID = "resolver_show";
    private static final String TAG = "OplusResolveInfoHelper";
    private static final String UPDATE_PIN_LIST = "update_pin_List";
    private static OplusResolverInfoHelper sResolveInfoHelper;
    private Context mContext;
    private SharedPreferences mPinnedSharedPrefs;
    private SharedPreferences mResolvePrefs;
    private HashMap<String, List<String>> mCloudBlackResolveMap = new HashMap<>();
    private HashMap<String, List<String>> mCloudResolveMap = new HashMap<>();
    private HashMap<String, List<String>> mCloudChooserMap = new HashMap<>();
    private HashMap<String, List<String>> mCloudBlackChooseMap = new HashMap<>();
    private HashMap<String, List<String>> mCloudBlackChooseActivityMap = new HashMap<>();
    private HashMap<String, String> mIconNameMap = new HashMap<>();
    private HashMap<String, List<String>> mCloudApkSubContractMap = new HashMap<>();
    private HashMap<String, List<String>> mCloudWhiteResolveMap = new HashMap<>();
    private List<ResolveInfo> mPriorityList = new ArrayList();
    private List<ResolveInfo> mPinnedList = new ArrayList();
    private List<String> mDefalutNoPriorityTypeList = new ArrayList();
    private HashMap<Integer, DisplayResolveInfo> mWhiteResolveInfo = new HashMap<>();

    public static OplusResolverInfoHelper getInstance(Context context) {
        if (sResolveInfoHelper == null) {
            sResolveInfoHelper = new OplusResolverInfoHelper(context.getApplicationContext());
        }
        return sResolveInfoHelper;
    }

    public void initData() {
        try {
            OplusActivityManager mOplusActivityManager = new OplusActivityManager();
            OplusResolveData data = mOplusActivityManager.getResolveData();
            if (data == null) {
                Log.e(TAG, "data is null");
                return;
            }
            this.mCloudBlackResolveMap = data.getBlackResolveMap();
            this.mCloudBlackChooseMap = data.getBlackChoosePackageMap();
            this.mCloudBlackChooseActivityMap = data.getBlackChooseActivityMap();
            this.mCloudResolveMap = data.getResolveMap();
            this.mCloudChooserMap = data.getChooseMap();
            this.mCloudApkSubContractMap = data.getApkSubContractMap();
            this.mCloudWhiteResolveMap = data.getWhiteResolveMap();
            Log.d(TAG, "mCloudBlackResolveMap = " + this.mCloudBlackResolveMap);
            Log.d(TAG, "mCloudBlackChooseMap = " + this.mCloudBlackChooseMap);
            Log.d(TAG, "mCloudBlackChooseActivityMap = " + this.mCloudBlackChooseActivityMap);
            Log.d(TAG, "mCloudResolveMap = " + this.mCloudResolveMap);
            Log.d(TAG, "mCloudChooserMap = " + this.mCloudChooserMap);
            Log.d(TAG, "mCloudApkSubContractMap = " + this.mCloudApkSubContractMap);
            Log.d(TAG, "mCloudWhiteResolveMap = " + this.mCloudWhiteResolveMap);
        } catch (RemoteException e) {
            Log.e(TAG, "init data RemoteException , " + e);
            e.printStackTrace();
        } catch (Exception e2) {
            Log.e(TAG, "init data Exception , " + e2);
            e2.printStackTrace();
        }
    }

    public int getResolveTopSize(Intent intent) {
        String type = OplusResolverIntentUtil.getIntentType(intent);
        if (this.mPriorityList == null) {
            return 0;
        }
        if (!OplusResolverIntentUtil.DEFAULT_APP_EMAIL.equals(type) || !isSupportTopApp()) {
            return this.mPriorityList.size();
        }
        return 0;
    }

    private OplusResolverInfoHelper(Context context) {
        this.mContext = context;
        this.mDefalutNoPriorityTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_LAUNCHER);
        this.mDefalutNoPriorityTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_MESSAGE);
        this.mDefalutNoPriorityTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_DIALER);
        this.mDefalutNoPriorityTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_MARKET);
        this.mDefalutNoPriorityTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_CONTACTS);
        this.mDefalutNoPriorityTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_CAMERA);
    }

    private List<String> getPriorityListFromXml(boolean isChoose, String type) {
        if (isChoose) {
            List<String> priority = getChooserListWithType(type);
            return priority;
        }
        List<String> priority2 = getResolveListWithType(type);
        return priority2;
    }

    public void resort(List<ResolveInfo> list, Intent intent) {
        resort(null, list, intent, 0);
    }

    public void resort(ComponentName nearbyComponent, List<ResolveInfo> list, Intent intent, int profilePage) {
        initData();
        String type = OplusResolverIntentUtil.getIntentType(intent);
        List<ResolveInfo> list2 = filterBlackApps(intent, type, list);
        boolean isChoose = OplusResolverIntentUtil.isChooserAction(intent);
        startToResort(nearbyComponent, list2, type, getPriorityListFromXml(isChoose, type), isChoose, profilePage);
    }

    public DisplayResolveInfo getWhiteResolveInfo(int page) {
        if (this.mWhiteResolveInfo.containsKey(Integer.valueOf(page))) {
            return this.mWhiteResolveInfo.get(Integer.valueOf(page));
        }
        return null;
    }

    public void clearWhiteResolverInfo() {
        HashMap<Integer, DisplayResolveInfo> hashMap = this.mWhiteResolveInfo;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    private List<ResolveInfo> filterBlackApps(Intent intent, String type, List<ResolveInfo> result) {
        if (result == null || result.isEmpty()) {
            return result;
        }
        boolean isChoose = OplusResolverIntentUtil.isChooserAction(intent);
        if (isChoose) {
            List<String> blackChooseList = getBlackChooseListWithType(type);
            if (blackChooseList != null && !blackChooseList.isEmpty()) {
                Iterator<ResolveInfo> it = result.iterator();
                while (it.hasNext()) {
                    ActivityInfo ai = it.next().activityInfo;
                    if (ai != null && OplusResolverIntentUtil.isInDataSet(blackChooseList, ai.packageName)) {
                        Log.d(TAG, "remove black choose package : " + ai.packageName);
                        it.remove();
                    }
                }
            }
            List<String> blackChooseListActivity = getBlackChooseActivityListWithType(type);
            if (blackChooseListActivity != null && !blackChooseListActivity.isEmpty()) {
                Iterator<ResolveInfo> it2 = result.iterator();
                while (it2.hasNext()) {
                    String componentName = it2.next().getComponentInfo().getComponentName().flattenToShortString();
                    if (OplusResolverIntentUtil.isInDataSet(blackChooseListActivity, componentName)) {
                        Log.d(TAG, "remove black choose componentName : " + componentName);
                        it2.remove();
                    }
                }
            }
        } else {
            List<String> blackResolveList = getBlackResolveListWithType(type);
            if (blackResolveList != null && !blackResolveList.isEmpty()) {
                Iterator<ResolveInfo> it3 = result.iterator();
                while (it3.hasNext()) {
                    ActivityInfo ai2 = it3.next().activityInfo;
                    if (ai2 != null && OplusResolverIntentUtil.isInDataSet(blackResolveList, ai2.packageName)) {
                        Log.d(TAG, "remove black resolve package : " + ai2.packageName);
                        it3.remove();
                    }
                }
            }
        }
        return result;
    }

    private void startToResort(ComponentName nearbyComponent, List<ResolveInfo> list, String type, List<String> priority, boolean isChoose, int profilePage) {
        Log.d(TAG, "start to resort : " + list);
        if (!isChoose) {
            HashMap<String, String> map = new HashMap<>();
            map.put("type", type);
            OplusStatistics.onCommon(this.mContext, CODE, SHOW_EVENT_ID, (Map<String, String>) map, false);
            Log.d(TAG, "statistics data [resolver_show]: " + map);
        }
        if (this.mPinnedSharedPrefs == null) {
            this.mPinnedSharedPrefs = OplusResolverSharedPrefs.getPinnedSharedPrefs(this.mContext);
        }
        List<ResolveInfo> otherList = new ArrayList<>();
        this.mPriorityList.clear();
        this.mPinnedList.clear();
        ArrayList<String> pinPrefList = new ArrayList<>();
        if (isChoose && type != null) {
            String galleryPinList = null;
            boolean isUpdatePinnedList = this.mPinnedSharedPrefs.getBoolean(UPDATE_PIN_LIST, false);
            if (!isUpdatePinnedList) {
                galleryPinList = getAllTypePinnedString();
                Settings.Secure.putString(this.mContext.getContentResolver(), GALLERY_PIN_LIST, galleryPinList);
            }
            if (TextUtils.isEmpty(galleryPinList)) {
                galleryPinList = Settings.Secure.getString(this.mContext.getContentResolver(), GALLERY_PIN_LIST);
            }
            Log.d(TAG, "pinList = " + galleryPinList);
            if (!TextUtils.isEmpty(galleryPinList)) {
                pinPrefList = new ArrayList<>(Arrays.asList(galleryPinList.split(";")));
            }
        }
        ResolveInfo mNearbyResolveInfo = getNearbyResolveInfo(nearbyComponent, list);
        for (int i = 0; i < list.size(); i++) {
            String componentName = list.get(i).getComponentInfo().getComponentName().flattenToShortString();
            String packageName = list.get(i).activityInfo.packageName;
            boolean isPinned = false;
            if (isChoose && pinPrefList.size() > 0) {
                isPinned = pinPrefList.contains(componentName);
            }
            if (isPinned) {
                this.mPinnedList.add(list.get(i));
                Log.d(TAG, "pinnedList add : " + list.get(i));
            } else if (priority != null && (priority.contains(componentName) || priority.contains(packageName))) {
                this.mPriorityList.add(list.get(i));
                Log.d(TAG, "priorityList add : " + list.get(i));
            } else {
                otherList.add(list.get(i));
            }
        }
        ResolveInfo mOshareResolveInfo = getOshareResolveInfo(this.mContext, otherList);
        if (!this.mPinnedList.isEmpty()) {
            sortPinnedList(pinPrefList);
        }
        if (!this.mPriorityList.isEmpty()) {
            sortPriorityList(priority);
        }
        list.clear();
        if (mNearbyResolveInfo != null) {
            list.add(mNearbyResolveInfo);
        }
        list.addAll(this.mPinnedList);
        if (mOshareResolveInfo != null) {
            list.add(mOshareResolveInfo);
        }
        list.addAll(this.mPriorityList);
        list.addAll(otherList);
        apkSubContractProcess(list, type);
        if (this.mWhiteResolveInfo.get(Integer.valueOf(profilePage)) != null) {
            list.remove(this.mWhiteResolveInfo.get(Integer.valueOf(profilePage)).getResolveInfo());
        }
        Log.d(TAG, "finish to resort : " + list);
    }

    private String getAllTypePinnedString() {
        String pinnedString = null;
        Set<String> allPinPrefSet = new HashSet<>();
        ArrayList<String> allTypeList = new ArrayList<>();
        String galleryPinList = Settings.Secure.getString(this.mContext.getContentResolver(), GALLERY_PIN_LIST);
        if (!TextUtils.isEmpty(galleryPinList)) {
            allPinPrefSet.addAll(new HashSet<>(Arrays.asList(galleryPinList.split(";"))));
        }
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_LAUNCHER);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_MESSAGE);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_DIALER);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_CONTACTS);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_BROWSER);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_CAMERA);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_AUDIO);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_VIDEO);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_EMAIL);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_TEXT);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_PDF);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_WORD);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_EXCEL);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_PPT);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_APPLICATION);
        allTypeList.add(OplusResolverIntentUtil.DEFAULT_APP_MARKET);
        Iterator<String> it = allTypeList.iterator();
        while (it.hasNext()) {
            String type = it.next();
            Set<String> pinPrefSet = this.mPinnedSharedPrefs.getStringSet(type, null);
            if (pinPrefSet != null) {
                allPinPrefSet.addAll(pinPrefSet);
            }
        }
        if (!allPinPrefSet.isEmpty()) {
            List<String> allList = new ArrayList<>(allPinPrefSet);
            Collections.sort(allList);
            pinnedString = listToString(allList, PhoneNumberUtilsExtImpl.WAIT);
        }
        this.mPinnedSharedPrefs.edit().putBoolean(UPDATE_PIN_LIST, true).apply();
        return pinnedString;
    }

    private String listToString(List<String> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sb.append(list.get(i));
            } else {
                sb.append(list.get(i));
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public int getExpandSizeWithoutMoreIcon(List<ResolveInfo> list, Intent intent) {
        initData();
        String type = OplusResolverIntentUtil.getIntentType(intent);
        List<ResolveInfo> list2 = filterBlackApps(intent, type, list);
        boolean isChoose = OplusResolverIntentUtil.isChooserAction(intent);
        List<String> priority = getPriorityListFromXml(isChoose, type);
        int result = 0;
        for (ResolveInfo info : list2) {
            String componentName = info.getComponentInfo().getComponentName().flattenToShortString();
            String packageName = info.activityInfo.packageName;
            if (priority != null && (priority.contains(componentName) || priority.contains(packageName))) {
                result++;
            }
        }
        return result;
    }

    private void sortPinnedList(ArrayList<String> list) {
        List<ResolveInfo> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < this.mPinnedList.size(); j++) {
                String packageName = this.mPinnedList.get(j).activityInfo.packageName;
                String componentName = this.mPinnedList.get(j).getComponentInfo().getComponentName().flattenToShortString();
                if (list.get(i).equals(componentName) || list.get(i).equals(packageName)) {
                    resultList.add(this.mPinnedList.get(j));
                }
            }
        }
        Log.d(TAG, "sort pinnedList : " + resultList);
        this.mPinnedList.clear();
        this.mPinnedList.addAll(resultList);
    }

    public void sortGalleryPinnedList(List<ResolveInfo> list) {
        ComponentName cName;
        this.mPinnedList.clear();
        String pinnedString = Settings.Secure.getString(this.mContext.getContentResolver(), GALLERY_PIN_LIST);
        if (!TextUtils.isEmpty(pinnedString) && list != null && !list.isEmpty()) {
            for (ResolveInfo info : list) {
                if (info != null && info.getComponentInfo() != null && (cName = info.getComponentInfo().getComponentName()) != null) {
                    final String packageName = info.getComponentInfo().packageName;
                    final String componentName = cName.flattenToShortString();
                    Stream<String> pinnedStream = Arrays.stream(pinnedString.split(";"));
                    if (pinnedStream.anyMatch(new Predicate() { // from class: com.oplus.resolver.OplusResolverInfoHelper$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return OplusResolverInfoHelper.lambda$sortGalleryPinnedList$0(componentName, packageName, (String) obj);
                        }
                    })) {
                        this.mPinnedList.add(info);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$sortGalleryPinnedList$0(String componentName, String packageName, String it) {
        return it.equals(componentName) || it.equals(packageName);
    }

    public List<ResolveInfo> getPinnedList() {
        return this.mPinnedList;
    }

    private void sortPriorityList(List<String> list) {
        List<ResolveInfo> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < this.mPriorityList.size(); j++) {
                String packageName = this.mPriorityList.get(j).activityInfo.packageName;
                String componentName = this.mPriorityList.get(j).getComponentInfo().getComponentName().flattenToShortString();
                if (list.get(i).equals(componentName) || list.get(i).equals(packageName)) {
                    resultList.add(this.mPriorityList.get(j));
                }
            }
        }
        Log.d(TAG, "sort priorityList : " + resultList);
        this.mPriorityList.clear();
        this.mPriorityList.addAll(resultList);
    }

    private List<String> getBlackResolveListWithType(String type) {
        if (type != null) {
            List<String> list = this.mCloudBlackResolveMap.get(type);
            return list;
        }
        return null;
    }

    private List<String> getBlackChooseListWithType(String type) {
        if (type != null) {
            List<String> list = this.mCloudBlackChooseMap.get(type);
            return list;
        }
        return null;
    }

    private List<String> getBlackChooseActivityListWithType(String type) {
        if (type != null) {
            List<String> list = this.mCloudBlackChooseActivityMap.get(type);
            return list;
        }
        return null;
    }

    private List<String> getResolveListWithType(String type) {
        if (type != null) {
            List<String> list = this.mCloudResolveMap.get(type);
            return list;
        }
        return null;
    }

    private List<String> getChooserListWithType(String type) {
        if (type != null) {
            if (!this.mDefalutNoPriorityTypeList.contains(type)) {
                List<String> defaultList = this.mCloudChooserMap.get(DEFAULT_TYPE);
                List<String> list = this.mCloudChooserMap.get(type);
                if (list == null || list.isEmpty()) {
                    return defaultList;
                }
                if (defaultList != null) {
                    for (int i = 0; i < defaultList.size(); i++) {
                        String componentName = defaultList.get(i);
                        if (!list.contains(componentName)) {
                            list.add(componentName);
                        }
                    }
                }
                return list;
            }
            return this.mCloudChooserMap.get(type);
        }
        return null;
    }

    public void setWhiteResolveInfo(DisplayResolveInfo displayResolveInfo, Intent intent, int page) {
        List<String> packageNameList;
        if (intent == null || displayResolveInfo == null) {
            return;
        }
        String type = OplusResolverIntentUtil.getIntentType(intent);
        boolean isChoose = OplusResolverIntentUtil.isChooserAction(intent);
        if (isChoose || displayResolveInfo.getResolveInfo() == null || this.mCloudWhiteResolveMap.get(type) == null || (packageNameList = this.mCloudWhiteResolveMap.get(type)) == null || packageNameList.isEmpty()) {
            return;
        }
        for (String packageName : packageNameList) {
            if (displayResolveInfo.getResolveInfo().activityInfo.packageName.equals(packageName)) {
                Log.d(TAG, "setWhiteResolveInfo packageName: " + packageName + " page: " + page);
                this.mWhiteResolveInfo.put(Integer.valueOf(page), displayResolveInfo);
            }
        }
    }

    private List<String> getApkSubContractMap(String type) {
        if (type != null) {
            ArrayList<String> result = new ArrayList<>();
            List<String> specificTypeList = this.mCloudApkSubContractMap.get(type);
            if (specificTypeList != null) {
                result.addAll(specificTypeList);
            }
            List<String> defaultTypeList = this.mCloudApkSubContractMap.get(DEFAULT_TYPE);
            if (defaultTypeList != null) {
                result.addAll(defaultTypeList);
            }
            return result;
        }
        return null;
    }

    private void apkSubContractProcess(List<ResolveInfo> originList, String type) {
        Log.d(TAG, "Sort before apkSubContractProcess: " + originList);
        List<ResolveInfo> hideList = getApkSubContractHideList(originList, type);
        if (hideList != null) {
            originList.removeAll(hideList);
        }
    }

    public int getApkSubContractListSize(List<ResolveInfo> originList, String type) {
        List<ResolveInfo> hideList = getApkSubContractHideList(originList, type);
        if (hideList == null) {
            return 0;
        }
        Log.d(TAG, "apkSubContractSize: " + hideList.size());
        return hideList.size();
    }

    private List<ResolveInfo> getApkSubContractHideList(List<ResolveInfo> originList, String type) {
        List<String> splicePackageNameList;
        List<ResolveInfo> hideList = new ArrayList<>();
        boolean isNeedShow = true;
        try {
            splicePackageNameList = getApkSubContractMap(type);
        } catch (Exception e) {
            Log.d(TAG, "getApkSubContractHideList exception: " + e.getMessage());
            e.printStackTrace();
        }
        if (splicePackageNameList == null) {
            return null;
        }
        for (String splicePackageName : splicePackageNameList) {
            String[] packageNameList = splicePackageName.split("#");
            if (packageNameList == null) {
                return null;
            }
            for (String packageName : packageNameList) {
                Iterator<ResolveInfo> it = originList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        ResolveInfo resolveInfo = it.next();
                        if (resolveInfo.activityInfo.packageName.equals(packageName)) {
                            if (!isNeedShow && !hideList.contains(resolveInfo)) {
                                hideList.add(resolveInfo);
                            }
                            isNeedShow = false;
                        }
                    }
                }
            }
            isNeedShow = true;
        }
        return hideList;
    }

    public void statisticsData(ResolveInfo ri, Intent intent, int which, String referrerPackage) {
        HashMap<String, String> map = new HashMap<>();
        String componentName = ri.getComponentInfo().getComponentName().flattenToShortString();
        PackageManager pm = this.mContext.getPackageManager();
        CharSequence componentLabel = ri.loadLabel(pm);
        String type = OplusResolverIntentUtil.getIntentType(intent);
        boolean isChooser = OplusResolverIntentUtil.isChooserAction(intent);
        map.put("intent", intent + "");
        map.put("action", intent.getAction() + "");
        map.put(KEY_CATEGORIES, intent.getCategories() + "");
        map.put("data", intent.getData() + "");
        map.put(KEY_MIME_TYPE, intent.getType() + "");
        map.put(KEY_SCHEME, intent.getScheme() + "");
        map.put("type", type);
        map.put(KEY_CHOOSE, isChooser + "");
        map.put(KEY_REFERRER_PACKAGE, referrerPackage);
        map.put(KEY_TARGET_PACKAGE, ri.activityInfo.packageName);
        map.put("name", componentName);
        map.put(KEY_COMPONENT_LABEL, ((Object) componentLabel) + "");
        map.put(KEY_POSITION, which + "");
        OplusStatistics.onCommon(this.mContext, CODE, APP_EVENT_ID, (Map<String, String>) map, false);
        Log.d(TAG, "statistics data [resolver_app] :" + map);
    }

    public void adjustPosition(List<ResolveInfo> resolveInfoList, List<String> priorPackage) {
        List<ResolveInfo> prior = new ArrayList<>();
        List<ResolveInfo> priorSort = new ArrayList<>();
        List<ResolveInfo> rest = new ArrayList<>();
        for (int i = 0; i < resolveInfoList.size(); i++) {
            String packageName = resolveInfoList.get(i).activityInfo.packageName;
            if (priorPackage.contains(packageName)) {
                ResolveInfo ri = resolveInfoList.get(i);
                prior.add(ri);
            } else {
                rest.add(resolveInfoList.get(i));
            }
        }
        for (int j = 0; j < priorPackage.size(); j++) {
            for (int k = 0; k < prior.size(); k++) {
                if (priorPackage.get(j).equals(prior.get(k).activityInfo.packageName)) {
                    priorSort.add(prior.get(k));
                }
            }
        }
        resolveInfoList.clear();
        resolveInfoList.addAll(priorSort);
        resolveInfoList.addAll(rest);
    }

    public boolean isMarketRecommendType(String type) {
        if (OplusResolverIntentUtil.DEFAULT_APP_EMAIL.equals(type) || OplusResolverIntentUtil.DEFAULT_APP_VIDEO.equals(type) || OplusResolverIntentUtil.DEFAULT_APP_TEXT.equals(type) || OplusResolverIntentUtil.DEFAULT_APP_PDF.equals(type) || OplusResolverIntentUtil.DEFAULT_APP_WORD.equals(type) || OplusResolverIntentUtil.DEFAULT_APP_EXCEL.equals(type) || OplusResolverIntentUtil.DEFAULT_APP_PPT.equals(type)) {
            return true;
        }
        return false;
    }

    private boolean isSupportTopApp() {
        return OplusFeatureConfigManager.getInstacne().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL);
    }

    public Map<String, String> getIconsMap() {
        OplusResolveData data;
        try {
            OplusActivityManager mOplusActivityManager = new OplusActivityManager();
            data = mOplusActivityManager.getResolveData();
        } catch (RemoteException re) {
            Log.e(TAG, "getIconsMap RemoteException , " + re.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "getIconsMap Exception , " + e.getMessage());
            e.printStackTrace();
        }
        if (data == null) {
            Log.e(TAG, "data is null");
            return this.mIconNameMap;
        }
        this.mIconNameMap = data.getIconName();
        Log.d(TAG, "iconMapsSize:" + this.mIconNameMap.size());
        return this.mIconNameMap;
    }

    private ResolveInfo getNearbyResolveInfo(ComponentName nearbyComponent, List<ResolveInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            ActivityInfo firstActivityInfo = list.get(i).activityInfo;
            if (nearbyComponent != null && firstActivityInfo != null && firstActivityInfo.getComponentName() != null && firstActivityInfo.getComponentName().flattenToShortString().equals(nearbyComponent.flattenToShortString())) {
                return list.remove(i);
            }
        }
        return null;
    }

    private ResolveInfo getOshareResolveInfo(Context context, List<ResolveInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (this.mResolvePrefs == null) {
            this.mResolvePrefs = OplusResolverSharedPrefs.getResolverSharedPrefs(context);
        }
        if (this.mResolvePrefs.getBoolean(OplusResolverSharedPrefs.OTA_COLOROS13, true)) {
            for (ResolveInfo resolveInfo : list) {
                if (OplusOShareUtil.isOsharePackage(context, resolveInfo.activityInfo.packageName)) {
                    list.remove(resolveInfo);
                    return resolveInfo;
                }
            }
        }
        return null;
    }
}
