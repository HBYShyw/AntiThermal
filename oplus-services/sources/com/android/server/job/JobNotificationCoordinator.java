package com.android.server.job;

import android.app.Notification;
import android.content.pm.UserPackage;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArrayMap;
import android.util.SparseSetArray;
import com.android.internal.annotations.GuardedBy;
import com.android.modules.expresslog.Counter;
import com.android.server.LocalServices;
import com.android.server.job.controllers.JobStatus;
import com.android.server.notification.NotificationManagerInternal;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class JobNotificationCoordinator {
    private static final String TAG = "JobNotificationCoordinator";
    private final Object mUijLock = new Object();
    private final ArrayMap<UserPackage, SparseSetArray<JobServiceContext>> mCurrentAssociations = new ArrayMap<>();
    private final ArrayMap<JobServiceContext, NotificationDetails> mNotificationDetails = new ArrayMap<>();

    @GuardedBy({"mUijLock"})
    private final SparseArrayMap<String, IntArray> mUijNotifications = new SparseArrayMap<>();

    @GuardedBy({"mUijLock"})
    private final SparseArrayMap<String, ArraySet<String>> mUijNotificationChannels = new SparseArrayMap<>();
    private final NotificationManagerInternal mNotificationManagerInternal = (NotificationManagerInternal) LocalServices.getService(NotificationManagerInternal.class);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class NotificationDetails {
        public final int appPid;
        public final int appUid;
        public final int jobEndNotificationPolicy;
        public final String notificationChannel;
        public final int notificationId;
        public final UserPackage userPackage;

        NotificationDetails(UserPackage userPackage, int i, int i2, int i3, String str, int i4) {
            this.userPackage = userPackage;
            this.notificationId = i3;
            this.notificationChannel = str;
            this.appPid = i;
            this.appUid = i2;
            this.jobEndNotificationPolicy = i4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueNotification(JobServiceContext jobServiceContext, String str, int i, int i2, int i3, Notification notification, int i4) {
        validateNotification(str, i2, notification, i4);
        JobStatus runningJobLocked = jobServiceContext.getRunningJobLocked();
        if (runningJobLocked == null) {
            Slog.wtfStack(TAG, "enqueueNotification called with no running job");
            return;
        }
        NotificationDetails notificationDetails = this.mNotificationDetails.get(jobServiceContext);
        if (notificationDetails == null) {
            if (runningJobLocked.startedAsUserInitiatedJob) {
                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_initial_set_notification_call_required", runningJobLocked.getUid());
            } else {
                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_initial_set_notification_call_optional", runningJobLocked.getUid());
            }
        } else {
            if (runningJobLocked.startedAsUserInitiatedJob) {
                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_subsequent_set_notification_call_required", runningJobLocked.getUid());
            } else {
                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_subsequent_set_notification_call_optional", runningJobLocked.getUid());
            }
            if (notificationDetails.notificationId != i3) {
                removeNotificationAssociation(jobServiceContext, 0, runningJobLocked);
                Counter.logIncrementWithUid("job_scheduler.value_cntr_w_uid_set_notification_changed_notification_ids", runningJobLocked.getUid());
            }
        }
        int userId = UserHandle.getUserId(i2);
        if (runningJobLocked.startedAsUserInitiatedJob) {
            notification.flags |= 32768;
            synchronized (this.mUijLock) {
                maybeCreateUijNotificationSetsLocked(userId, str);
                IntArray intArray = (IntArray) this.mUijNotifications.get(userId, str);
                if (intArray.indexOf(i3) == -1) {
                    intArray.add(i3);
                }
                ((ArraySet) this.mUijNotificationChannels.get(userId, str)).add(notification.getChannelId());
            }
        }
        UserPackage of = UserPackage.of(userId, str);
        NotificationDetails notificationDetails2 = new NotificationDetails(of, i, i2, i3, notification.getChannelId(), i4);
        SparseSetArray<JobServiceContext> sparseSetArray = this.mCurrentAssociations.get(of);
        if (sparseSetArray == null) {
            sparseSetArray = new SparseSetArray<>();
            this.mCurrentAssociations.put(of, sparseSetArray);
        }
        sparseSetArray.add(i3, jobServiceContext);
        this.mNotificationDetails.put(jobServiceContext, notificationDetails2);
        this.mNotificationManagerInternal.enqueueNotification(str, str, i2, i, null, i3, notification, userId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeNotificationAssociation(JobServiceContext jobServiceContext, int i, JobStatus jobStatus) {
        NotificationDetails remove = this.mNotificationDetails.remove(jobServiceContext);
        if (remove == null) {
            return;
        }
        SparseSetArray<JobServiceContext> sparseSetArray = this.mCurrentAssociations.get(remove.userPackage);
        if (sparseSetArray == null || !sparseSetArray.remove(remove.notificationId, jobServiceContext)) {
            Slog.wtf(TAG, "Association data structures not in sync");
            return;
        }
        int userId = UserHandle.getUserId(remove.appUid);
        String str = remove.userPackage.packageName;
        int i2 = remove.notificationId;
        ArraySet arraySet = sparseSetArray.get(i2);
        boolean z = true;
        if (arraySet == null || arraySet.isEmpty()) {
            if (remove.jobEndNotificationPolicy == 1 || i == 11 || i == 13) {
                this.mNotificationManagerInternal.cancelNotification(str, str, remove.appUid, remove.appPid, null, i2, userId);
                z = false;
            }
        } else {
            z = true ^ isNotificationUsedForAnyUij(userId, str, i2);
        }
        if (z) {
            this.mNotificationManagerInternal.removeUserInitiatedJobFlagFromNotification(str, i2, userId);
        }
        if (jobStatus == null || !jobStatus.startedAsUserInitiatedJob) {
            return;
        }
        maybeDeleteNotificationIdAssociation(userId, str, i2);
        maybeDeleteNotificationChannelAssociation(userId, str, remove.notificationChannel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNotificationAssociatedWithAnyUserInitiatedJobs(int i, int i2, String str) {
        synchronized (this.mUijLock) {
            IntArray intArray = (IntArray) this.mUijNotifications.get(i2, str);
            if (intArray != null) {
                return intArray.indexOf(i) != -1;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNotificationChannelAssociatedWithAnyUserInitiatedJobs(String str, int i, String str2) {
        synchronized (this.mUijLock) {
            ArraySet arraySet = (ArraySet) this.mUijNotificationChannels.get(i, str2);
            if (arraySet == null) {
                return false;
            }
            return arraySet.contains(str);
        }
    }

    private boolean isNotificationUsedForAnyUij(int i, String str, int i2) {
        ArraySet arraySet;
        SparseSetArray<JobServiceContext> sparseSetArray = this.mCurrentAssociations.get(UserPackage.of(i, str));
        if (sparseSetArray == null || (arraySet = sparseSetArray.get(i2)) == null) {
            return false;
        }
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            JobStatus runningJobLocked = ((JobServiceContext) arraySet.valueAt(size)).getRunningJobLocked();
            if (runningJobLocked != null && runningJobLocked.startedAsUserInitiatedJob) {
                return true;
            }
        }
        return false;
    }

    private void maybeDeleteNotificationIdAssociation(int i, String str, int i2) {
        if (isNotificationUsedForAnyUij(i, str, i2)) {
            return;
        }
        synchronized (this.mUijLock) {
            IntArray intArray = (IntArray) this.mUijNotifications.get(i, str);
            if (intArray != null) {
                intArray.remove(intArray.indexOf(i2));
                if (intArray.size() == 0) {
                    this.mUijNotifications.delete(i, str);
                }
            }
        }
    }

    private void maybeDeleteNotificationChannelAssociation(int i, String str, String str2) {
        JobStatus runningJobLocked;
        for (int size = this.mNotificationDetails.size() - 1; size >= 0; size--) {
            JobServiceContext keyAt = this.mNotificationDetails.keyAt(size);
            NotificationDetails notificationDetails = this.mNotificationDetails.get(keyAt);
            if (notificationDetails != null && UserHandle.getUserId(notificationDetails.appUid) == i && notificationDetails.userPackage.packageName.equals(str) && notificationDetails.notificationChannel.equals(str2) && (runningJobLocked = keyAt.getRunningJobLocked()) != null && runningJobLocked.startedAsUserInitiatedJob) {
                return;
            }
        }
        synchronized (this.mUijLock) {
            ArraySet arraySet = (ArraySet) this.mUijNotificationChannels.get(i, str);
            if (arraySet != null) {
                arraySet.remove(str2);
                if (arraySet.isEmpty()) {
                    this.mUijNotificationChannels.delete(i, str);
                }
            }
        }
    }

    @GuardedBy({"mUijLock"})
    private void maybeCreateUijNotificationSetsLocked(int i, String str) {
        if (this.mUijNotifications.get(i, str) == null) {
            this.mUijNotifications.add(i, str, new IntArray());
        }
        if (this.mUijNotificationChannels.get(i, str) == null) {
            this.mUijNotificationChannels.add(i, str, new ArraySet());
        }
    }

    private void validateNotification(String str, int i, Notification notification, int i2) {
        if (notification == null) {
            throw new NullPointerException("notification");
        }
        if (notification.getSmallIcon() == null) {
            throw new IllegalArgumentException("small icon required");
        }
        if (this.mNotificationManagerInternal.getNotificationChannel(str, i, notification.getChannelId()) == null) {
            throw new IllegalArgumentException("invalid notification channel");
        }
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("invalid job end notification policy");
        }
    }
}
