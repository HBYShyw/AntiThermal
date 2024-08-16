package com.oplus.epona.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.lang.ref.WeakReference;
import java.util.Stack;

/* loaded from: classes.dex */
public class ActivityStackManager {
    private final Stack<WeakReference<Activity>> mActivityStack = new Stack<>();
    private final Application.ActivityLifecycleCallbacks mActivityLifecycleCallback = new Application.ActivityLifecycleCallbacks() { // from class: com.oplus.epona.internal.ActivityStackManager.1
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            ActivityStackManager.this.pushTask(activity);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            ActivityStackManager.this.removeTask(activity);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void pushTask(Activity activity) {
        if (activity == null) {
            return;
        }
        this.mActivityStack.push(new WeakReference<>(activity));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeTask(Activity activity) {
        Activity activity2;
        if (activity == null) {
            return;
        }
        for (int size = this.mActivityStack.size() - 1; size >= 0; size--) {
            try {
                WeakReference<Activity> weakReference = this.mActivityStack.get(size);
                if (weakReference != null && (activity2 = weakReference.get()) != null && activity2.getClass().getSimpleName().equals(activity.getClass().getSimpleName())) {
                    this.mActivityStack.remove(size);
                    break;
                }
            } catch (Exception unused) {
            }
        }
    }

    public void attach(Application application) {
        if (application != null) {
            application.registerActivityLifecycleCallbacks(this.mActivityLifecycleCallback);
        }
    }

    public Activity getCurrentActivity() {
        try {
            if (this.mActivityStack.size() <= 0) {
                return null;
            }
            return this.mActivityStack.get(r1.size() - 1).get();
        } catch (Exception unused) {
            return null;
        }
    }
}
