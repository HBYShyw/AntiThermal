package com.oplus.oms.split.splitload.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
final class SplitComponentInfoProvider {
    private final Map<String, String> mComponentMap = new HashMap();
    private final Set<String> mSplitNames;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SplitComponentInfoProvider(Set<String> splitNames) {
        this.mSplitNames = splitNames;
    }

    public Map<String, String> getComponentMap() {
        return this.mComponentMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getSplitApplicationName(String splitName) {
        return ComponentInfoManager.getSplitApplication(splitName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, List<String>> getSplitActivitiesMap() {
        Map<String, List<String>> splitActivitiesMap = new HashMap<>(0);
        for (String splitName : this.mSplitNames) {
            String[] result = ComponentInfoManager.getSplitActivities(splitName);
            if (result != null && result.length > 0) {
                List<String> activities = new ArrayList<>();
                Collections.addAll(activities, result);
                splitActivitiesMap.put(splitName, activities);
                for (String activity : result) {
                    this.mComponentMap.put(activity, splitName);
                }
            }
        }
        return splitActivitiesMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getSplitServices() {
        List<String> services = new ArrayList<>();
        for (String splitName : this.mSplitNames) {
            String[] result = ComponentInfoManager.getSplitServices(splitName);
            if (result != null && result.length > 0) {
                Collections.addAll(services, result);
                for (String service : result) {
                    this.mComponentMap.put(service, splitName);
                }
            }
        }
        return services;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getSplitReceivers() {
        List<String> receivers = new ArrayList<>();
        for (String splitName : this.mSplitNames) {
            String[] result = ComponentInfoManager.getSplitReceivers(splitName);
            if (result != null && result.length > 0) {
                Collections.addAll(receivers, result);
                for (String receive : result) {
                    this.mComponentMap.put(receive, splitName);
                }
            }
        }
        return receivers;
    }
}
