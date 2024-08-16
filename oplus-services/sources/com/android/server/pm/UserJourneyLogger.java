package com.android.server.pm;

import android.content.pm.UserInfo;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.job.controllers.JobStatus;
import java.util.concurrent.ThreadLocalRandom;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UserJourneyLogger {
    public static final int ERROR_CODE_ABORTED = 3;
    public static final int ERROR_CODE_INCOMPLETE_OR_TIMEOUT = 2;
    public static final int ERROR_CODE_INVALID_SESSION_ID = 0;
    public static final int ERROR_CODE_NULL_USER_INFO = 4;
    public static final int ERROR_CODE_UNSPECIFIED = -1;
    public static final int ERROR_CODE_USER_ALREADY_AN_ADMIN = 5;
    public static final int ERROR_CODE_USER_IS_NOT_AN_ADMIN = 6;
    public static final int EVENT_STATE_BEGIN = 1;
    public static final int EVENT_STATE_CANCEL = 3;
    public static final int EVENT_STATE_ERROR = 4;
    public static final int EVENT_STATE_FINISH = 2;
    public static final int EVENT_STATE_NONE = 0;
    private static final int USER_ID_KEY_MULTIPLICATION = 100;
    public static final int USER_JOURNEY_GRANT_ADMIN = 7;
    public static final int USER_JOURNEY_REVOKE_ADMIN = 8;
    public static final int USER_JOURNEY_UNKNOWN = 0;
    public static final int USER_JOURNEY_USER_CREATE = 4;
    public static final int USER_JOURNEY_USER_LIFECYCLE = 9;
    public static final int USER_JOURNEY_USER_REMOVE = 6;
    public static final int USER_JOURNEY_USER_START = 3;
    public static final int USER_JOURNEY_USER_STOP = 5;
    public static final int USER_JOURNEY_USER_SWITCH_FG = 2;
    public static final int USER_JOURNEY_USER_SWITCH_UI = 1;
    public static final int USER_LIFECYCLE_EVENT_CREATE_USER = 3;
    public static final int USER_LIFECYCLE_EVENT_GRANT_ADMIN = 9;
    public static final int USER_LIFECYCLE_EVENT_REMOVE_USER = 8;
    public static final int USER_LIFECYCLE_EVENT_REVOKE_ADMIN = 10;
    public static final int USER_LIFECYCLE_EVENT_START_USER = 2;
    public static final int USER_LIFECYCLE_EVENT_STOP_USER = 7;
    public static final int USER_LIFECYCLE_EVENT_SWITCH_USER = 1;
    public static final int USER_LIFECYCLE_EVENT_UNKNOWN = 0;
    public static final int USER_LIFECYCLE_EVENT_UNLOCKED_USER = 6;
    public static final int USER_LIFECYCLE_EVENT_UNLOCKING_USER = 5;
    public static final int USER_LIFECYCLE_EVENT_USER_RUNNING_LOCKED = 4;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final SparseArray<UserJourneySession> mUserIdToUserJourneyMap = new SparseArray<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UserJourney {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UserJourneyErrorCode {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UserLifecycleEvent {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface UserLifecycleEventState {
    }

    @UserLifecycleEventState
    private static int errorToFinishState(@UserJourneyErrorCode int i) {
        if (i != -1) {
            return i != 3 ? 4 : 3;
        }
        return 2;
    }

    private int getUserJourneyKey(int i, @UserJourney int i2) {
        return (i * 100) + i2;
    }

    @UserLifecycleEvent
    private static int journeyToEvent(@UserJourney int i) {
        switch (i) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 9;
            case 8:
                return 10;
            default:
                return 0;
        }
    }

    public static int getUserTypeForStatsd(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1103927049:
                if (str.equals("android.os.usertype.full.GUEST")) {
                    c = 0;
                    break;
                }
                break;
            case -159818852:
                if (str.equals("android.os.usertype.profile.MANAGED")) {
                    c = 1;
                    break;
                }
                break;
            case 34001850:
                if (str.equals("android.os.usertype.system.HEADLESS")) {
                    c = 2;
                    break;
                }
                break;
            case 485661392:
                if (str.equals("android.os.usertype.full.SYSTEM")) {
                    c = 3;
                    break;
                }
                break;
            case 942013715:
                if (str.equals("android.os.usertype.full.SECONDARY")) {
                    c = 4;
                    break;
                }
                break;
            case 1711075452:
                if (str.equals("android.os.usertype.full.RESTRICTED")) {
                    c = 5;
                    break;
                }
                break;
            case 1765400260:
                if (str.equals("android.os.usertype.full.DEMO")) {
                    c = 6;
                    break;
                }
                break;
            case 1966344346:
                if (str.equals("android.os.usertype.profile.CLONE")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 3;
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 5;
            case 6:
                return 4;
            case 7:
                return 8;
            default:
                return 0;
        }
    }

    @VisibleForTesting
    public void logUserLifecycleJourneyReported(UserJourneySession userJourneySession, @UserJourney int i, int i2, int i3, int i4, int i5, @UserJourneyErrorCode int i6) {
        if (userJourneySession == null) {
            writeUserLifecycleJourneyReported(-1L, i, i2, i3, i4, i5, 0, -1L);
        } else {
            writeUserLifecycleJourneyReported(userJourneySession.mSessionId, i, i2, i3, i4, i5, i6, System.currentTimeMillis() - userJourneySession.mStartTimeInMills);
        }
    }

    @VisibleForTesting
    public void writeUserLifecycleJourneyReported(long j, int i, int i2, int i3, int i4, int i5, int i6, long j2) {
        FrameworkStatsLog.write(264, j, i, i2, i3, i4, i5, i6, j2);
    }

    @VisibleForTesting
    public void logUserLifecycleEventOccurred(UserJourneySession userJourneySession, int i, @UserLifecycleEvent int i2, @UserLifecycleEventState int i3, @UserJourneyErrorCode int i4) {
        if (userJourneySession == null) {
            writeUserLifecycleEventOccurred(-1L, i, i2, 4, 0);
        } else {
            writeUserLifecycleEventOccurred(userJourneySession.mSessionId, i, i2, i3, i4);
        }
    }

    @VisibleForTesting
    public void writeUserLifecycleEventOccurred(long j, int i, int i2, int i3, int i4) {
        FrameworkStatsLog.write(265, j, i, i2, i3, i4);
    }

    public void logUserLifecycleEvent(int i, @UserLifecycleEvent int i2, @UserLifecycleEventState int i3) {
        logUserLifecycleEventOccurred(findUserJourneySession(i), i, i2, i3, -1);
    }

    private UserJourneySession findUserJourneySession(int i) {
        synchronized (this.mLock) {
            int size = this.mUserIdToUserJourneyMap.size();
            for (int i2 = 0; i2 < size; i2++) {
                int keyAt = this.mUserIdToUserJourneyMap.keyAt(i2);
                if (keyAt / 100 == i) {
                    return this.mUserIdToUserJourneyMap.get(keyAt);
                }
            }
            return null;
        }
    }

    @VisibleForTesting
    public UserJourneySession finishAndClearIncompleteUserJourney(int i, @UserJourney int i2) {
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(i, i2);
            UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(userJourneyKey);
            if (userJourneySession == null) {
                return null;
            }
            logUserLifecycleEventOccurred(userJourneySession, i, journeyToEvent(userJourneySession.mJourney), 4, 2);
            logUserLifecycleJourneyReported(userJourneySession, i2, -1, i, getUserTypeForStatsd(""), -1, 2);
            this.mUserIdToUserJourneyMap.remove(userJourneyKey);
            return userJourneySession;
        }
    }

    public UserJourneySession logUserJourneyFinish(int i, UserInfo userInfo, @UserJourney int i2) {
        return logUserJourneyFinishWithError(i, userInfo, i2, -1);
    }

    @VisibleForTesting
    public UserJourneySession logUserSwitchJourneyFinish(int i, UserInfo userInfo) {
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(userInfo.id, 2);
            int userJourneyKey2 = getUserJourneyKey(userInfo.id, 1);
            if (this.mUserIdToUserJourneyMap.contains(userJourneyKey)) {
                return logUserJourneyFinish(i, userInfo, 2);
            }
            if (!this.mUserIdToUserJourneyMap.contains(userJourneyKey2)) {
                return null;
            }
            return logUserJourneyFinish(i, userInfo, 1);
        }
    }

    public UserJourneySession logUserJourneyFinishWithError(int i, UserInfo userInfo, @UserJourney int i2, @UserJourneyErrorCode int i3) {
        if (userInfo == null) {
            return null;
        }
        synchronized (this.mLock) {
            int errorToFinishState = errorToFinishState(i3);
            int userJourneyKey = getUserJourneyKey(userInfo.id, i2);
            UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(userJourneyKey);
            if (userJourneySession == null) {
                return null;
            }
            logUserLifecycleEventOccurred(userJourneySession, userInfo.id, journeyToEvent(userJourneySession.mJourney), errorToFinishState, i3);
            logUserLifecycleJourneyReported(userJourneySession, i2, i, userInfo.id, getUserTypeForStatsd(userInfo.userType), userInfo.flags, i3);
            this.mUserIdToUserJourneyMap.remove(userJourneyKey);
            return userJourneySession;
        }
    }

    public UserJourneySession logDelayedUserJourneyFinishWithError(int i, UserInfo userInfo, @UserJourney int i2, @UserJourneyErrorCode int i3) {
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(userInfo.id, i2);
            UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(userJourneyKey);
            if (userJourneySession == null) {
                return null;
            }
            logUserLifecycleJourneyReported(userJourneySession, i2, i, userInfo.id, getUserTypeForStatsd(userInfo.userType), userInfo.flags, i3);
            this.mUserIdToUserJourneyMap.remove(userJourneyKey);
            return userJourneySession;
        }
    }

    public UserJourneySession logNullUserJourneyError(@UserJourney int i, int i2, int i3, String str, int i4) {
        UserJourneySession userJourneySession;
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(i3, i);
            userJourneySession = this.mUserIdToUserJourneyMap.get(userJourneyKey);
            logUserLifecycleEventOccurred(userJourneySession, i3, journeyToEvent(i), 4, 4);
            logUserLifecycleJourneyReported(userJourneySession, i, i2, i3, getUserTypeForStatsd(str), i4, 4);
            this.mUserIdToUserJourneyMap.remove(userJourneyKey);
        }
        return userJourneySession;
    }

    public UserJourneySession logUserCreateJourneyFinish(int i, UserInfo userInfo) {
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(-1, 4);
            UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(userJourneyKey);
            if (userJourneySession == null) {
                return null;
            }
            logUserLifecycleEventOccurred(userJourneySession, userInfo.id, 3, 2, -1);
            logUserLifecycleJourneyReported(userJourneySession, 4, i, userInfo.id, getUserTypeForStatsd(userInfo.userType), userInfo.flags, -1);
            this.mUserIdToUserJourneyMap.remove(userJourneyKey);
            return userJourneySession;
        }
    }

    public UserJourneySession logUserJourneyBegin(int i, @UserJourney int i2) {
        UserJourneySession userJourneySession;
        long nextLong = ThreadLocalRandom.current().nextLong(1L, JobStatus.NO_LATEST_RUNTIME);
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(i, i2);
            userJourneySession = new UserJourneySession(nextLong, i2);
            this.mUserIdToUserJourneyMap.append(userJourneyKey, userJourneySession);
            logUserLifecycleEventOccurred(userJourneySession, i, journeyToEvent(userJourneySession.mJourney), 1, -1);
        }
        return userJourneySession;
    }

    public UserJourneySession startSessionForDelayedJourney(int i, @UserJourney int i2, long j) {
        UserJourneySession userJourneySession;
        long nextLong = ThreadLocalRandom.current().nextLong(1L, JobStatus.NO_LATEST_RUNTIME);
        synchronized (this.mLock) {
            int userJourneyKey = getUserJourneyKey(i, i2);
            userJourneySession = new UserJourneySession(nextLong, i2, j);
            this.mUserIdToUserJourneyMap.append(userJourneyKey, userJourneySession);
        }
        return userJourneySession;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class UserJourneySession {

        @UserJourney
        public final int mJourney;
        public final long mSessionId;
        public long mStartTimeInMills;

        @VisibleForTesting
        public UserJourneySession(long j, @UserJourney int i) {
            this.mJourney = i;
            this.mSessionId = j;
            this.mStartTimeInMills = System.currentTimeMillis();
        }

        @VisibleForTesting
        public UserJourneySession(long j, @UserJourney int i, long j2) {
            this.mJourney = i;
            this.mSessionId = j;
            this.mStartTimeInMills = j2;
        }
    }
}
