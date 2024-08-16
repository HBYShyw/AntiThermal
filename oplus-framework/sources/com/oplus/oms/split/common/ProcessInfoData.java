package com.oplus.oms.split.common;

import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class ProcessInfoData {
    private String mActionName;
    private String mProcessName;
    private List<String> mSplitList;

    public ProcessInfoData(String processName, String actionName, List<String> splitList) {
        this.mProcessName = processName;
        this.mActionName = actionName;
        this.mSplitList = splitList;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessInfoData that = (ProcessInfoData) o;
        if (Objects.equals(this.mProcessName, that.mProcessName) && Objects.equals(this.mActionName, that.mActionName) && listEquals(this.mSplitList, that.mSplitList)) {
            return true;
        }
        return false;
    }

    private boolean listEquals(List<String> first, List<String> second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null || first.size() != second.size()) {
            return false;
        }
        for (String str : first) {
            if (!second.contains(str)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mProcessName, this.mActionName, this.mSplitList);
    }

    public void setProcessName(String name) {
        this.mProcessName = name;
    }

    public void setActionName(String name) {
        this.mActionName = name;
    }

    public void setSplitList(List<String> list) {
        this.mSplitList = list;
    }

    public List<String> getSplitList() {
        return this.mSplitList;
    }

    public String getActionName() {
        return this.mActionName;
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public String toString() {
        return "ProcessInfoData{mProcessName='" + this.mProcessName + "', mActionName='" + this.mActionName + "', mSplitList=" + this.mSplitList + '}';
    }
}
