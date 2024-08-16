package com.android.server.app;

import android.app.ActivityManager;
import android.app.IActivityTaskManager;
import android.content.ComponentName;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.LruCache;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class GameTaskInfoProvider {
    private static final String TAG = "GameTaskInfoProvider";
    private static final int TASK_INFO_CACHE_MAX_SIZE = 50;
    private final IActivityTaskManager mActivityTaskManager;
    private final GameClassifier mGameClassifier;
    private final UserHandle mUserHandle;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final LruCache<Integer, GameTaskInfo> mGameTaskInfoCache = new LruCache<>(50);

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameTaskInfoProvider(UserHandle userHandle, IActivityTaskManager iActivityTaskManager, GameClassifier gameClassifier) {
        this.mUserHandle = userHandle;
        this.mActivityTaskManager = iActivityTaskManager;
        this.mGameClassifier = gameClassifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameTaskInfo get(int i) {
        ComponentName componentName;
        synchronized (this.mLock) {
            GameTaskInfo gameTaskInfo = this.mGameTaskInfoCache.get(Integer.valueOf(i));
            if (gameTaskInfo != null) {
                return gameTaskInfo;
            }
            ActivityManager.RunningTaskInfo runningTaskInfo = getRunningTaskInfo(i);
            if (runningTaskInfo == null || (componentName = runningTaskInfo.baseActivity) == null) {
                return null;
            }
            return generateGameInfo(i, componentName);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameTaskInfo get(int i, ComponentName componentName) {
        synchronized (this.mLock) {
            GameTaskInfo gameTaskInfo = this.mGameTaskInfoCache.get(Integer.valueOf(i));
            if (gameTaskInfo != null) {
                if (!gameTaskInfo.mComponentName.equals(componentName)) {
                    return gameTaskInfo;
                }
                Slog.w(TAG, "Found cached task info for taskId " + i + " but cached component name " + gameTaskInfo.mComponentName + " does not match " + componentName);
            }
            return generateGameInfo(i, componentName);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityManager.RunningTaskInfo getRunningTaskInfo(int i) {
        try {
            for (ActivityManager.RunningTaskInfo runningTaskInfo : this.mActivityTaskManager.getTasks(Integer.MAX_VALUE, false, false, -1)) {
                if (runningTaskInfo.taskId == i) {
                    return runningTaskInfo;
                }
            }
            return null;
        } catch (RemoteException unused) {
            Slog.w(TAG, "Failed to fetch running tasks");
            return null;
        }
    }

    private GameTaskInfo generateGameInfo(int i, ComponentName componentName) {
        GameTaskInfo gameTaskInfo = new GameTaskInfo(i, this.mGameClassifier.isGame(componentName.getPackageName(), this.mUserHandle), componentName);
        synchronized (this.mLock) {
            this.mGameTaskInfoCache.put(Integer.valueOf(i), gameTaskInfo);
        }
        return gameTaskInfo;
    }
}
