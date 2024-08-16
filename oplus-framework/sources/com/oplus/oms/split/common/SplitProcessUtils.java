package com.oplus.oms.split.common;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class SplitProcessUtils {
    private static final String SPLIT_LOAD_ACTION = "split.load.action";
    private static final String SPLIT_NAME = "split_name";
    private static final String SPLIT_STATUS = "split_status";
    private static final int SPLIT_STATUS_LOADED = 1;
    private static final int SPLIT_STATUS_UNLOADED = 2;
    private static final HashMap<String, ProcessInfoData> sProcessMap = (HashMap) SplitBaseInfoProvider.getProcessMap();
    private static final HashMap<String, SplitInfoData> sSplitMap = (HashMap) SplitBaseInfoProvider.getSplitMap();

    private SplitProcessUtils() {
    }

    public static void broadCastLoaded(Context context, ArrayList<String> splitNames, boolean isLoad) {
        Intent intent = new Intent(SPLIT_LOAD_ACTION);
        intent.setPackage(context.getPackageName());
        intent.putStringArrayListExtra("split_name", splitNames);
        if (isLoad) {
            intent.putExtra(SPLIT_STATUS, 1);
        } else {
            intent.putExtra(SPLIT_STATUS, 2);
        }
        context.sendBroadcast(intent);
    }

    public static List<ProcessInfoData> getSubProcessInfoData(String splitName) {
        HashMap<String, SplitInfoData> hashMap;
        if (splitName == null || (hashMap = sSplitMap) == null) {
            return Collections.emptyList();
        }
        SplitInfoData moudle = hashMap.get(splitName);
        if (moudle == null) {
            return Collections.emptyList();
        }
        List<ProcessInfoData> processList = new ArrayList<>();
        List<String> subProcessName = moudle.getSubProcessList();
        if (subProcessName == null) {
            return Collections.emptyList();
        }
        for (String process : subProcessName) {
            ProcessInfoData subProcessInfo = sProcessMap.get(process);
            if (subProcessInfo != null) {
                processList.add(subProcessInfo);
            }
        }
        return processList;
    }

    public static ProcessInfoData getMainProcessInfoData(String splitName) {
        HashMap<String, SplitInfoData> hashMap;
        SplitInfoData moudle;
        if (splitName == null || (hashMap = sSplitMap) == null || (moudle = hashMap.get(splitName)) == null) {
            return null;
        }
        return sProcessMap.get(moudle.getMainProcessName());
    }

    public static Set<SplitInfoData> getSplitInfoDataFromProcess(String processName) {
        Set<SplitInfoData> result = new HashSet<>();
        if (processName == null || sSplitMap == null) {
            return result;
        }
        ProcessInfoData process = sProcessMap.get(processName);
        if (process == null) {
            return result;
        }
        List<String> splitList = process.getSplitList();
        if (splitList == null) {
            return result;
        }
        for (String splitName : splitList) {
            SplitInfoData info = sSplitMap.get(splitName);
            if (info != null) {
                result.add(info);
            }
        }
        return result;
    }
}
