package com.oplus.onetrace.entities;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class TaskInfo implements Parcelable {
    public static final Parcelable.Creator<TaskInfo> CREATOR = new Parcelable.Creator<TaskInfo>() { // from class: com.oplus.onetrace.entities.TaskInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TaskInfo createFromParcel(Parcel source) {
            return new TaskInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };
    private final String mTaskName;
    private List<TaskInfo> mThreadTasks;
    private final int mTid;

    public TaskInfo(int tid, String taskName) {
        this.mTid = tid;
        this.mTaskName = taskName;
    }

    public TaskInfo(Parcel in) {
        this.mTid = in.readInt();
        this.mTaskName = in.readString();
        ArrayList arrayList = new ArrayList();
        in.readParcelableList(arrayList, TaskInfo.class.getClassLoader());
        this.mThreadTasks = arrayList.isEmpty() ? null : arrayList;
    }

    public int getTaskId() {
        return this.mTid;
    }

    public String getTaskName() {
        return this.mTaskName;
    }

    public List<TaskInfo> getThreadTasks() {
        if (this.mThreadTasks == null) {
            return null;
        }
        return new ArrayList(this.mThreadTasks);
    }

    public int getThreadCount() {
        List<TaskInfo> list = this.mThreadTasks;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("TaskInfo{ taskId=").append(this.mTid).append(", cmdline=").append(this.mTaskName);
        if (this.mThreadTasks != null) {
            builder.append(", threadTasks=").append(this.mThreadTasks);
        }
        return builder.append(" }").toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mTid);
        dest.writeString(this.mTaskName);
        dest.writeParcelableList(this.mThreadTasks, flags);
    }

    public void addAllThreadTasks(Map<Integer, String> threadMap) {
        if (threadMap == null) {
            return;
        }
        this.mThreadTasks = (List) threadMap.entrySet().stream().map(new Function() { // from class: com.oplus.onetrace.entities.TaskInfo$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return TaskInfo.lambda$addAllThreadTasks$0((Map.Entry) obj);
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ TaskInfo lambda$addAllThreadTasks$0(Map.Entry entry) {
        return new TaskInfo(((Integer) entry.getKey()).intValue(), (String) entry.getValue());
    }
}
