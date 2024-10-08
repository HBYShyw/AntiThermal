package com.android.server.wm;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class LaunchObserverRegistryImpl extends ActivityMetricsLaunchObserver implements ActivityMetricsLaunchObserverRegistry {
    private final Handler mHandler;
    private final ArrayList<ActivityMetricsLaunchObserver> mList = new ArrayList<>();

    public LaunchObserverRegistryImpl(Looper looper) {
        this.mHandler = new Handler(looper);
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserverRegistry
    public void registerLaunchObserver(ActivityMetricsLaunchObserver activityMetricsLaunchObserver) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LaunchObserverRegistryImpl) obj).handleRegisterLaunchObserver((ActivityMetricsLaunchObserver) obj2);
            }
        }, this, activityMetricsLaunchObserver));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserverRegistry
    public void unregisterLaunchObserver(ActivityMetricsLaunchObserver activityMetricsLaunchObserver) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LaunchObserverRegistryImpl) obj).handleUnregisterLaunchObserver((ActivityMetricsLaunchObserver) obj2);
            }
        }, this, activityMetricsLaunchObserver));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onIntentStarted(Intent intent, long j) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda7
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((LaunchObserverRegistryImpl) obj).handleOnIntentStarted((Intent) obj2, ((Long) obj3).longValue());
            }
        }, this, intent, Long.valueOf(j)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onIntentFailed(long j) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LaunchObserverRegistryImpl) obj).handleOnIntentFailed(((Long) obj2).longValue());
            }
        }, this, Long.valueOf(j)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onActivityLaunched(long j, ComponentName componentName, int i) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda4
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                ((LaunchObserverRegistryImpl) obj).handleOnActivityLaunched(((Long) obj2).longValue(), (ComponentName) obj3, ((Integer) obj4).intValue());
            }
        }, this, Long.valueOf(j), componentName, Integer.valueOf(i)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onActivityLaunchCancelled(long j) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((LaunchObserverRegistryImpl) obj).handleOnActivityLaunchCancelled(((Long) obj2).longValue());
            }
        }, this, Long.valueOf(j)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onActivityLaunchFinished(long j, ComponentName componentName, long j2) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda1
            public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                ((LaunchObserverRegistryImpl) obj).handleOnActivityLaunchFinished(((Long) obj2).longValue(), (ComponentName) obj3, ((Long) obj4).longValue());
            }
        }, this, Long.valueOf(j), componentName, Long.valueOf(j2)));
    }

    @Override // com.android.server.wm.ActivityMetricsLaunchObserver
    public void onReportFullyDrawn(long j, long j2) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.wm.LaunchObserverRegistryImpl$$ExternalSyntheticLambda0
            public final void accept(Object obj, Object obj2, Object obj3) {
                ((LaunchObserverRegistryImpl) obj).handleOnReportFullyDrawn(((Long) obj2).longValue(), ((Long) obj3).longValue());
            }
        }, this, Long.valueOf(j), Long.valueOf(j2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRegisterLaunchObserver(ActivityMetricsLaunchObserver activityMetricsLaunchObserver) {
        this.mList.add(activityMetricsLaunchObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnregisterLaunchObserver(ActivityMetricsLaunchObserver activityMetricsLaunchObserver) {
        this.mList.remove(activityMetricsLaunchObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnIntentStarted(Intent intent, long j) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onIntentStarted(intent, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnIntentFailed(long j) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onIntentFailed(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityLaunched(long j, ComponentName componentName, int i) {
        for (int i2 = 0; i2 < this.mList.size(); i2++) {
            this.mList.get(i2).onActivityLaunched(j, componentName, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityLaunchCancelled(long j) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onActivityLaunchCancelled(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityLaunchFinished(long j, ComponentName componentName, long j2) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onActivityLaunchFinished(j, componentName, j2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReportFullyDrawn(long j, long j2) {
        for (int i = 0; i < this.mList.size(); i++) {
            this.mList.get(i).onReportFullyDrawn(j, j2);
        }
    }
}
