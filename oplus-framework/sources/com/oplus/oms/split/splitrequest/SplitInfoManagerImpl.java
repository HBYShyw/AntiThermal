package com.oplus.oms.split.splitrequest;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.SplitBaseInfoProvider;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitrequest.SplitInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class SplitInfoManagerImpl implements SplitInfoManager {
    private static final String TAG = "SplitInfoManagerImpl";
    private final AtomicReference<SplitDetails> mSplitDetailsRef = new AtomicReference<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        private static final SplitInfoManager INSTANCE = new SplitInfoManagerImpl();

        private SingletonHolder() {
        }
    }

    public static SplitInfoManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private SplitDetails getSplitDetails() {
        return this.mSplitDetailsRef.get();
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public String getBaseAppVersionName(Context context) {
        SplitDetails details = getOrCreateSplitDetails(context);
        if (details != null) {
            return details.getAppVersionName();
        }
        return null;
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public String getOmsId(Context context) {
        SplitDetails details = getOrCreateSplitDetails(context);
        if (details != null) {
            return details.getOmsId();
        }
        return null;
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public List<String> getSplitEntryFragments(Context context) {
        SplitDetails details = getOrCreateSplitDetails(context);
        if (details != null) {
            return details.getSplitEntryFragments();
        }
        return Collections.emptyList();
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public SplitInfo getSplitInfo(Context context, String splitName) {
        SplitDetails details = getOrCreateSplitDetails(context);
        if (details != null) {
            Collection<SplitInfo> splits = details.getSplitInfoListing().getSplitInfoMap().values();
            for (SplitInfo split : splits) {
                if (split.getSplitName().equals(splitName)) {
                    return split;
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public SplitInfo getSplitUpdateInfo(Context context, String splitName) {
        return null;
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public List<SplitInfo> getSplitInfos(Context context, Collection<String> splitNames) {
        SplitDetails details = getOrCreateSplitDetails(context);
        if (details != null) {
            Collection<SplitInfo> splits = details.getSplitInfoListing().getSplitInfoMap().values();
            List<SplitInfo> splitInfos = new ArrayList<>(splitNames.size());
            for (SplitInfo split : splits) {
                if (splitNames.contains(split.getSplitName())) {
                    splitInfos.add(split);
                    SplitLog.d(TAG, "getSplitInfos " + split, new Object[0]);
                }
            }
            return splitInfos;
        }
        return Collections.emptyList();
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public Collection<SplitInfo> getAllSplitInfo(Context context) {
        SplitDetails details = getOrCreateSplitDetails(context);
        if (details != null) {
            LinkedHashMap<String, SplitInfo> splitInfoMap = details.getSplitInfoListing().getSplitInfoMap();
            return splitInfoMap.values();
        }
        return Collections.emptyList();
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public Collection<SplitInfo> getAllUpdateSplitInfo(Context context) {
        return Collections.emptyList();
    }

    @Override // com.oplus.oms.split.splitrequest.SplitInfoManager
    public String getDefaultSplitInfoVersion() {
        return SplitBaseInfoProvider.getDefaultSplitInfoVersion();
    }

    private SplitDetails createSplitDetailsForDefaultVersion(Context context, String defaultVersion) {
        try {
            String defaultSplitInfoFileName = SplitConstants.OMS + File.separator + SplitConstants.OMS_PREFIX + defaultVersion + SplitConstants.DOT_JSON;
            SplitLog.d(TAG, "Default split file name: " + defaultSplitInfoFileName, new Object[0]);
            long currentTime = System.currentTimeMillis();
            SplitDetails details = parseSplitContentsForDefaultVersion(context, defaultSplitInfoFileName);
            SplitLog.d(TAG, "Cost " + (System.currentTimeMillis() - currentTime) + " mil-second to parse default split info", new Object[0]);
            return details;
        } catch (IOException | JSONException e) {
            SplitLog.e(TAG, "Failed to create default split info!", e);
            return null;
        }
    }

    private synchronized SplitDetails getOrCreateSplitDetails(Context context) {
        SplitDetails details = getSplitDetails();
        if (details == null) {
            String defaultVersion = getDefaultSplitInfoVersion();
            SplitLog.d(TAG, " defaultVersion :" + defaultVersion, new Object[0]);
            details = createSplitDetailsForDefaultVersion(context, defaultVersion);
            if (details == null || !TextUtils.isEmpty(details.getOmsId())) {
                this.mSplitDetailsRef.compareAndSet(null, details);
            } else {
                return null;
            }
        }
        return details;
    }

    private static SplitDetails parseSplitContentsForDefaultVersion(Context context, String fileName) throws IOException, JSONException {
        String content = readInputStreamContent(createInputStreamFromAssets(context, fileName));
        return parseSplitsContent(content);
    }

    private static InputStream createInputStreamFromAssets(Context context, String fileName) {
        Resources resources = context.getResources();
        if (resources == null || fileName == null) {
            return null;
        }
        try {
            AssetManager assetManager = resources.getAssets();
            if (assetManager == null) {
                return null;
            }
            InputStream is = assetManager.open(fileName);
            return is;
        } catch (IOException e) {
            return null;
        }
    }

    private static String readInputStreamContent(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder stringBuffer = new StringBuilder();
        while (true) {
            String str = br.readLine();
            if (str != null) {
                stringBuffer.append(str);
            } else {
                FileUtil.closeQuietly(is);
                FileUtil.closeQuietly(br);
                return stringBuffer.toString();
            }
        }
    }

    private static SplitDetails parseSplitsContent(String content) throws JSONException {
        List<String> splitEntryFragments;
        JSONObject contentObj;
        JSONArray updateSplitsArray;
        List<String> workProcesses;
        JSONArray processes;
        HashMap<String, String> infoForSplit;
        List<String> dependencies;
        int i;
        List<SplitInfo.ApkData> apkDataList;
        String str;
        String str2;
        JSONArray array;
        if (content == null) {
            return null;
        }
        LinkedHashMap<String, SplitInfo> splitInfoMap = new LinkedHashMap<>();
        JSONObject contentObj2 = new JSONObject(content);
        String omsId = contentObj2.optString("mOmsId");
        String appVersionName = contentObj2.optString("mBaseVersionName");
        JSONArray updateSplitsArray2 = contentObj2.optJSONArray("mUpdateSplits");
        List<SplitUpdateInfo> updateSplits = new ArrayList<>();
        String str3 = "mSplitName";
        if (updateSplitsArray2 != null) {
            for (int i2 = 0; i2 < updateSplitsArray2.length(); i2++) {
                JSONObject itemObj = updateSplitsArray2.getJSONObject(i2);
                String splitName = itemObj.optString("mSplitName");
                String version = itemObj.optString("mSplitVersion");
                updateSplits.add(new SplitUpdateInfo(splitName, version));
            }
        }
        JSONArray splitEntryFragmentsArray = contentObj2.optJSONArray("mSplitEntryFragments");
        if (splitEntryFragmentsArray != null && splitEntryFragmentsArray.length() > 0) {
            List<String> splitEntryFragments2 = new ArrayList<>(splitEntryFragmentsArray.length());
            for (int i3 = 0; i3 < splitEntryFragmentsArray.length(); i3++) {
                String str4 = splitEntryFragmentsArray.getString(i3);
                splitEntryFragments2.add(str4);
            }
            splitEntryFragments = splitEntryFragments2;
        } else {
            splitEntryFragments = null;
        }
        JSONArray array2 = contentObj2.optJSONArray("mSplits");
        if (array2 == null) {
            throw new OMSRunTimeException("No splits found in split-details file!");
        }
        int i4 = 0;
        while (i4 < array2.length()) {
            JSONObject itemObj2 = array2.getJSONObject(i4);
            boolean builtIn = itemObj2.optBoolean("mBuiltIn");
            String splitName2 = itemObj2.optString(str3);
            int splitVersionCode = itemObj2.optInt("mVersionCode");
            String splitVersionName = itemObj2.optString("mVersionName");
            JSONObject infoSplit = itemObj2.optJSONObject("mInfoForSplit");
            HashMap<String, String> infoForSplit2 = new HashMap<>();
            if (infoSplit != null) {
                Iterator<String> keys = infoSplit.keys();
                while (keys.hasNext()) {
                    String finalKey = keys.next();
                    String finalValue = infoSplit.getString(finalKey);
                    infoForSplit2.put(finalKey, finalValue);
                }
            }
            int minSdkVersion = itemObj2.optInt("mMinSdkVersion");
            int dexNumber = itemObj2.optInt("mDexNumber");
            JSONArray processes2 = itemObj2.optJSONArray("mWorkProcesses");
            if (processes2 == null || processes2.length() <= 0) {
                contentObj = contentObj2;
                updateSplitsArray = updateSplitsArray2;
                workProcesses = null;
            } else {
                contentObj = contentObj2;
                updateSplitsArray = updateSplitsArray2;
                List<String> workProcesses2 = new ArrayList<>(processes2.length());
                for (int k = 0; k < processes2.length(); k++) {
                    workProcesses2.add(processes2.optString(k));
                }
                workProcesses = workProcesses2;
            }
            JSONArray dependenciesArray = itemObj2.optJSONArray("mDependencies");
            if (dependenciesArray != null && dependenciesArray.length() > 0) {
                processes = processes2;
                List<String> dependencies2 = new ArrayList<>(dependenciesArray.length());
                int m = 0;
                while (true) {
                    infoForSplit = infoForSplit2;
                    if (m >= dependenciesArray.length()) {
                        break;
                    }
                    dependencies2.add(dependenciesArray.optString(m));
                    m++;
                    infoForSplit2 = infoForSplit;
                }
                dependencies = dependencies2;
            } else {
                processes = processes2;
                infoForSplit = infoForSplit2;
                dependencies = null;
            }
            JSONArray apkDataArray = itemObj2.optJSONArray("mApkData");
            String str5 = "mSize";
            String str6 = "mMd5";
            if (apkDataArray != null && apkDataArray.length() > 0) {
                List<SplitInfo.ApkData> apkDataList2 = new ArrayList<>(apkDataArray.length());
                int p = 0;
                while (true) {
                    i = i4;
                    int i5 = apkDataArray.length();
                    if (p >= i5) {
                        break;
                    }
                    JSONObject apkDataObj = apkDataArray.optJSONObject(p);
                    String md5 = apkDataObj.optString("mMd5");
                    long size = apkDataObj.optLong("mSize");
                    SplitInfo.ApkData apkData = new SplitInfo.ApkData(null, null, md5, size);
                    apkDataList2.add(apkData);
                    p++;
                    i4 = i;
                    apkDataArray = apkDataArray;
                }
                apkDataList = apkDataList2;
            } else {
                i = i4;
                apkDataList = null;
            }
            JSONArray libDataArray = itemObj2.optJSONArray("mLibData");
            List<SplitInfo.LibData> libDataList = null;
            if (libDataArray != null && libDataArray.length() > 0) {
                libDataList = new ArrayList<>(libDataArray.length());
                int j = 0;
                while (j < libDataArray.length()) {
                    JSONObject libDataObj = libDataArray.optJSONObject(j);
                    JSONObject itemObj3 = itemObj2;
                    String cpuAbi = libDataObj.optString("mAbi");
                    JSONArray libDataArray2 = libDataArray;
                    JSONArray jniLibsArray = libDataObj.optJSONArray("mJniLibs");
                    List<SplitInfo.LibData.Lib> jniLibs = new ArrayList<>();
                    if (jniLibsArray == null || jniLibsArray.length() <= 0) {
                        str = str6;
                        str2 = str5;
                        array = array2;
                    } else {
                        int k2 = 0;
                        while (true) {
                            array = array2;
                            if (k2 >= jniLibsArray.length()) {
                                break;
                            }
                            JSONObject libObj = jniLibsArray.optJSONObject(k2);
                            JSONArray jniLibsArray2 = jniLibsArray;
                            String name = libObj.optString("mName");
                            JSONArray splitEntryFragmentsArray2 = splitEntryFragmentsArray;
                            String soMd5 = libObj.optString(str6);
                            String str7 = str3;
                            long soSize = libObj.optLong(str5);
                            String str8 = str6;
                            SplitInfo.LibData.Lib lib = new SplitInfo.LibData.Lib(name, soMd5, soSize);
                            jniLibs.add(lib);
                            k2++;
                            array2 = array;
                            jniLibsArray = jniLibsArray2;
                            splitEntryFragmentsArray = splitEntryFragmentsArray2;
                            updateSplits = updateSplits;
                            str3 = str7;
                            str6 = str8;
                            str5 = str5;
                        }
                        str = str6;
                        str2 = str5;
                    }
                    SplitInfo.LibData libInfo = new SplitInfo.LibData(cpuAbi, jniLibs);
                    libDataList.add(libInfo);
                    j++;
                    itemObj2 = itemObj3;
                    libDataArray = libDataArray2;
                    array2 = array;
                    splitEntryFragmentsArray = splitEntryFragmentsArray;
                    updateSplits = updateSplits;
                    str3 = str3;
                    str6 = str;
                    str5 = str2;
                }
            }
            SplitInfo splitInfo = new SplitInfo(splitName2, appVersionName, splitVersionCode, splitVersionName, builtIn, minSdkVersion, dexNumber, workProcesses, dependencies, apkDataList, libDataList, infoForSplit);
            splitInfoMap.put(splitName2, splitInfo);
            i4 = i + 1;
            contentObj2 = contentObj;
            updateSplitsArray2 = updateSplitsArray;
            array2 = array2;
            splitEntryFragmentsArray = splitEntryFragmentsArray;
            updateSplits = updateSplits;
            str3 = str3;
        }
        SplitInfoListing splitInfoListing = new SplitInfoListing(splitInfoMap);
        return new SplitDetails(omsId, appVersionName, updateSplits, splitEntryFragments, splitInfoListing);
    }
}
