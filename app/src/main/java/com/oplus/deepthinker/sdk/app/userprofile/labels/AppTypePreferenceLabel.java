package com.oplus.deepthinker.sdk.app.userprofile.labels;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AppTypePreferenceLabel {
    private static final String TAG = "AppTypePreferenceLabel";
    private List<Integer> mSortedAppTypeList = new LinkedList();
    private Map<Integer, Detail> mDetailMap = new HashMap();

    /* loaded from: classes.dex */
    public static class Detail {
        public int useCount;
        public long useTime;
    }

    public AppTypePreferenceLabel() {
    }

    public List<Integer> getSortedAppTypeList() {
        return this.mSortedAppTypeList;
    }

    public Detail getTypeDetailInfo(int i10) {
        return this.mDetailMap.get(Integer.valueOf(i10));
    }

    public AppTypePreferenceLabel(List<Integer> list, Map<Integer, Detail> map) {
        this.mSortedAppTypeList.addAll(list);
        this.mDetailMap.putAll(map);
    }
}
