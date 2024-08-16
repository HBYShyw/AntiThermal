package com.android.server.job;

import android.net.Network;
import android.util.ArraySet;
import com.android.server.job.controllers.JobStatus;
import com.android.server.job.restrictions.JobRestriction;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface StateChangedListener {
    void onControllerStateChanged(ArraySet<JobStatus> arraySet);

    void onDeviceIdleStateChanged(boolean z);

    void onNetworkChanged(JobStatus jobStatus, Network network);

    void onRestrictedBucketChanged(List<JobStatus> list);

    void onRestrictionStateChanged(JobRestriction jobRestriction, boolean z);

    void onRunJobNow(JobStatus jobStatus);
}
