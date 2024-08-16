package com.oplus.compat.multisearch;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.OplusActivityTaskManager;
import android.app.TaskStackListener;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.window.TaskAppearedInfo;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.multisearch.IOplusMultiSearchListener;
import com.oplus.multisearch.IOplusMultiSearchManagerSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class FwkCompat {
    private static final String TAG = "MultiSearchFwkCompat";
    public static final int WINDOWING_MODE_FREEFORM = 5;
    public static final int WINDOWING_MODE_FULLSCREEN = 1;
    public static final int WINDOWING_MODE_MULTI_WINDOW = 6;
    public static final int WINDOWING_MODE_PINNED = 2;
    public static final int WINDOWING_MODE_UNDEFINED = 0;
    private final ArrayMap<OnComputeInternalInsetsListenerWrapper, ViewTreeObserver.OnComputeInternalInsetsListener> mRegisteredInsetsListeners = new ArrayMap<>();
    private final ArrayList<TaskStackChangeListener> mTaskStackChangeListeners = new ArrayList<>();
    private TaskStackListenerWrapper mTaskStackListenerWrapper;
    private static final FwkCompat sInstance = new FwkCompat();
    private static final boolean SUPPORT_FOLD_FEATURE = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD);
    private static final boolean SUPPORT_DRAGONFLY_FEATURE = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FOLD_REMAP_DISPLAY_DISABLED);

    /* loaded from: classes.dex */
    public interface OnComputeInternalInsetsListenerWrapper {
        void onComputeInternalInsets(InternalInsetsInfoWrapper internalInsetsInfoWrapper);
    }

    private FwkCompat() {
    }

    public static FwkCompat getInstance() {
        return sInstance;
    }

    public boolean supportMultiSearchFeature(Context context) {
        return SUPPORT_FOLD_FEATURE && !SUPPORT_DRAGONFLY_FEATURE;
    }

    public void setFocusedTask(int taskId) {
        try {
            ActivityTaskManager.getService().setFocusedTask(taskId);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to set focus task", e);
        }
    }

    public void setAvoidMoveToFront(ActivityOptions options) {
        if (options != null) {
            options.setAvoidMoveToFront();
        }
    }

    public void setLaunchWindowingMode(ActivityOptions options, int windowingMode) {
        if (options != null) {
            options.setLaunchWindowingMode(windowingMode);
        }
    }

    public void setLaunchCookie(ActivityOptions options, Binder cookie) {
    }

    public void setMultiTaskMode(ActivityOptions options, boolean b) {
    }

    public void showSurfaceControlInTransaction(SurfaceControl.Transaction t, SurfaceControl sc) {
        if (t != null && sc != null) {
            t.show(sc);
        }
    }

    public void removeSurfaceControlInTransaction(SurfaceControl.Transaction t, SurfaceControl sc) {
        if (t != null && sc != null) {
            t.remove(sc);
        }
    }

    public int getWindowingModeFromTaskInfo(ActivityManager.RunningTaskInfo info) {
        return info.configuration.windowConfiguration.getWindowingMode();
    }

    public ArrayList<IBinder> getLaunchCookieFromTaskInfo(ActivityManager.RunningTaskInfo info) {
        return new ArrayList<>();
    }

    public boolean getVisibleFromTaskInfo(ActivityManager.RunningTaskInfo info) {
        return true;
    }

    public WindowContainerTokenWrapper getWindowContainerTokenFromTaskInfo(ActivityManager.RunningTaskInfo info) {
        return new WindowContainerTokenWrapper(info.token);
    }

    public void addOnComputeInternalInsetsListener(View view, final OnComputeInternalInsetsListenerWrapper listener) {
        if (view == null || listener == null) {
            return;
        }
        ViewTreeObserver.OnComputeInternalInsetsListener delegate = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.oplus.compat.multisearch.FwkCompat.1
            public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo inoutInfo) {
                listener.onComputeInternalInsets(new InternalInsetsInfoWrapper(inoutInfo));
            }
        };
        this.mRegisteredInsetsListeners.put(listener, delegate);
        view.getViewTreeObserver().addOnComputeInternalInsetsListener(delegate);
    }

    public void removeOnComputeInternalInsetsListener(View view, OnComputeInternalInsetsListenerWrapper listener) {
        ViewTreeObserver.OnComputeInternalInsetsListener delegate;
        if (view != null && listener != null && (delegate = this.mRegisteredInsetsListeners.remove(listener)) != null) {
            view.getViewTreeObserver().removeOnComputeInternalInsetsListener(delegate);
        }
    }

    public void registerTaskStackListener(TaskStackChangeListener listener) {
        if (!this.mTaskStackChangeListeners.contains(listener)) {
            this.mTaskStackChangeListeners.add(listener);
        }
        if (this.mTaskStackChangeListeners.isEmpty() || this.mTaskStackListenerWrapper != null) {
            return;
        }
        try {
            this.mTaskStackListenerWrapper = new TaskStackListenerWrapper();
            ActivityTaskManager.getService().registerTaskStackListener(this.mTaskStackListenerWrapper);
        } catch (RemoteException e) {
            this.mTaskStackListenerWrapper = null;
            Log.e(TAG, "Failed to register task stack listener", e);
        }
    }

    public void unregisterTaskStackListener(TaskStackChangeListener listener) {
        this.mTaskStackChangeListeners.remove(listener);
        if (!this.mTaskStackChangeListeners.isEmpty() || this.mTaskStackListenerWrapper == null) {
            return;
        }
        try {
            ActivityTaskManager.getService().unregisterTaskStackListener(this.mTaskStackListenerWrapper);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to unregister task stack listener", e);
        }
        this.mTaskStackListenerWrapper = null;
    }

    /* loaded from: classes.dex */
    public static class WindowContainerTokenWrapper {
        private WindowContainerToken mDelegate;

        public WindowContainerTokenWrapper() {
        }

        public WindowContainerTokenWrapper(WindowContainerToken token) {
            this.mDelegate = token;
        }

        public WindowContainerToken getWindowContainerToken() {
            return this.mDelegate;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WindowContainerTokenWrapper that = (WindowContainerTokenWrapper) o;
            return Objects.equals(this.mDelegate, that.mDelegate);
        }

        public int hashCode() {
            return Objects.hash(this.mDelegate);
        }
    }

    /* loaded from: classes.dex */
    public static class WindowContainerTransactionWrapper {
        private WindowContainerTransaction mDelegate = new WindowContainerTransaction();

        public WindowContainerTransaction getWindowContainerTransaction() {
            return this.mDelegate;
        }

        public WindowContainerTransactionWrapper setBounds(WindowContainerTokenWrapper windowContainerToken, Rect rect) {
            this.mDelegate.setBounds(windowContainerToken.getWindowContainerToken(), rect);
            return this;
        }

        public WindowContainerTransactionWrapper setFocusable(WindowContainerTokenWrapper windowContainerToken, boolean focusable) {
            this.mDelegate.setFocusable(windowContainerToken.getWindowContainerToken(), focusable);
            return this;
        }

        public WindowContainerTransactionWrapper setHidden(WindowContainerTokenWrapper windowContainerToken, boolean hidden) {
            this.mDelegate.setHidden(windowContainerToken.getWindowContainerToken(), hidden);
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static class WindowOrganizerWrapper {
        public static void applyTransaction(WindowContainerTransactionWrapper windowContainerTransaction) {
            try {
                ActivityTaskManager.getService().getWindowOrganizerController().applyTransaction(windowContainerTransaction.getWindowContainerTransaction());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public static SurfaceControl takeScreenshot(WindowContainerTokenWrapper windowContainerToken) {
            SurfaceControl outSurface = new SurfaceControl();
            try {
                return OplusActivityTaskManager.getInstance().takeScreenshot(windowContainerToken.getWindowContainerToken(), outSurface);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class TaskView extends SurfaceView {
        public TaskView(Context context) {
            this(context, null);
        }

        public TaskView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TaskView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public TaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes, true);
            setUseAlpha();
        }
    }

    /* loaded from: classes.dex */
    public static class TaskOrganizerWrapper {
        private final Object mSyncObject = new Object();
        private ArraySet<WindowContainerToken> mTokens = new ArraySet<>();
        private boolean mInterceptBackPressed = false;
        private InnerTaskOrganizer mDelegate = new InnerTaskOrganizer();

        public final void registerOrganizer(int windowingMode) {
            this.mDelegate.registerOrganizer();
        }

        public void setInterceptBackPressedOnTaskRoot(boolean interceptBackPressed) {
            synchronized (this.mSyncObject) {
                this.mInterceptBackPressed = interceptBackPressed;
                ArraySet<WindowContainerToken> arraySet = this.mTokens;
                if (arraySet != null && arraySet.size() != 0) {
                    Iterator<WindowContainerToken> it = this.mTokens.iterator();
                    while (it.hasNext()) {
                        WindowContainerToken token = it.next();
                        this.mDelegate.setInterceptBackPressedOnTaskRoot(token, interceptBackPressed);
                    }
                }
            }
        }

        public final void unregisterOrganizer() {
            this.mDelegate.unregisterOrganizer();
        }

        public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        }

        public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        public void applyTransaction(WindowContainerTransactionWrapper windowContainerTransaction) {
            try {
                ActivityTaskManager.getService().getWindowOrganizerController().applyTransaction(windowContainerTransaction.getWindowContainerTransaction());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* loaded from: classes.dex */
        private class InnerTaskOrganizer extends TaskOrganizer {
            private IOplusMultiSearchManagerSession mBinder;
            private final IOplusMultiSearchListener.Stub mListener = new IOplusMultiSearchListener.Stub() { // from class: com.oplus.compat.multisearch.FwkCompat.TaskOrganizerWrapper.InnerTaskOrganizer.1
                public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
                    InnerTaskOrganizer.this.onBackPressedOnTaskRoot(runningTaskInfo);
                }

                public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) throws RemoteException {
                    InnerTaskOrganizer.this.onTaskAppeared(runningTaskInfo, surfaceControl);
                }

                public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
                    InnerTaskOrganizer.this.onTaskInfoChanged(runningTaskInfo);
                }

                public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
                    InnerTaskOrganizer.this.onTaskVanished(runningTaskInfo);
                }
            };

            InnerTaskOrganizer() {
                try {
                    this.mBinder = OplusActivityTaskManager.getInstance().getMultiSearchSession();
                } catch (RemoteException e) {
                    Log.e(FwkCompat.TAG, "getMultiSearchSession fail exception : " + e);
                }
            }

            public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) {
                if (this.mBinder == null) {
                    return;
                }
                TaskOrganizerWrapper.this.onBackPressedOnTaskRoot(runningTaskInfo);
            }

            public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
                if (this.mBinder == null) {
                    return;
                }
                TaskOrganizerWrapper.this.onTaskAppeared(runningTaskInfo, surfaceControl);
                synchronized (TaskOrganizerWrapper.this.mSyncObject) {
                    setInterceptBackPressedOnTaskRoot(runningTaskInfo.token, TaskOrganizerWrapper.this.mInterceptBackPressed);
                    TaskOrganizerWrapper.this.mTokens.add(runningTaskInfo.token);
                }
            }

            public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
                if (this.mBinder == null) {
                    return;
                }
                TaskOrganizerWrapper.this.onTaskInfoChanged(runningTaskInfo);
            }

            public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
                if (this.mBinder == null) {
                    return;
                }
                TaskOrganizerWrapper.this.onTaskVanished(runningTaskInfo);
                synchronized (TaskOrganizerWrapper.this.mSyncObject) {
                    TaskOrganizerWrapper.this.mTokens.remove(runningTaskInfo.token);
                }
            }

            public List<TaskAppearedInfo> registerOrganizer() {
                try {
                    IOplusMultiSearchManagerSession iOplusMultiSearchManagerSession = this.mBinder;
                    if (iOplusMultiSearchManagerSession != null) {
                        iOplusMultiSearchManagerSession.registerListener(this.mListener);
                        return null;
                    }
                    return null;
                } catch (RemoteException e) {
                    Log.e(FwkCompat.TAG, "registerOrganizer exception : " + e);
                    return null;
                }
            }

            public void unregisterOrganizer() {
                try {
                    IOplusMultiSearchManagerSession iOplusMultiSearchManagerSession = this.mBinder;
                    if (iOplusMultiSearchManagerSession != null) {
                        iOplusMultiSearchManagerSession.unregisterListener(this.mListener);
                    }
                } catch (RemoteException e) {
                    Log.e(FwkCompat.TAG, "unregisterOrganizer exception : " + e);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class InternalInsetsInfoWrapper {
        public static final int TOUCHABLE_INSETS_CONTENT = 1;
        public static final int TOUCHABLE_INSETS_FRAME = 0;
        public static final int TOUCHABLE_INSETS_REGION = 3;
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;
        private ViewTreeObserver.InternalInsetsInfo mDelegate;

        public InternalInsetsInfoWrapper() {
        }

        public InternalInsetsInfoWrapper(ViewTreeObserver.InternalInsetsInfo info) {
            this.mDelegate = info;
        }

        public Region getTouchableInsets() {
            ViewTreeObserver.InternalInsetsInfo internalInsetsInfo = this.mDelegate;
            if (internalInsetsInfo == null) {
                return null;
            }
            return internalInsetsInfo.touchableRegion;
        }

        public void setTouchableInsets(int val) {
            ViewTreeObserver.InternalInsetsInfo internalInsetsInfo = this.mDelegate;
            if (internalInsetsInfo == null) {
                return;
            }
            internalInsetsInfo.setTouchableInsets(val);
        }
    }

    /* loaded from: classes.dex */
    public static class TaskStackChangeListener {
        public void onTaskCreated(int taskId, ComponentName componentName) {
        }

        public void onTaskFocusChanged(int taskId, boolean focused) {
        }

        public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo runningTaskInfo) {
        }

        public void onTaskRemoved(int taskId) {
        }
    }

    /* loaded from: classes.dex */
    public class TaskStackListenerWrapper extends TaskStackListener {
        public TaskStackListenerWrapper() {
        }

        public void onTaskCreated(int taskId, ComponentName componentName) {
            Iterator it = FwkCompat.this.mTaskStackChangeListeners.iterator();
            while (it.hasNext()) {
                TaskStackChangeListener l = (TaskStackChangeListener) it.next();
                l.onTaskCreated(taskId, componentName);
            }
        }

        public void onTaskFocusChanged(int taskId, boolean focused) {
            Iterator it = FwkCompat.this.mTaskStackChangeListeners.iterator();
            while (it.hasNext()) {
                TaskStackChangeListener l = (TaskStackChangeListener) it.next();
                l.onTaskFocusChanged(taskId, focused);
            }
        }

        public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) {
            Iterator it = FwkCompat.this.mTaskStackChangeListeners.iterator();
            while (it.hasNext()) {
                TaskStackChangeListener l = (TaskStackChangeListener) it.next();
                l.onTaskMovedToFront(runningTaskInfo);
            }
        }

        public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo runningTaskInfo) {
            Iterator it = FwkCompat.this.mTaskStackChangeListeners.iterator();
            while (it.hasNext()) {
                TaskStackChangeListener l = (TaskStackChangeListener) it.next();
                l.onTaskRemovalStarted(runningTaskInfo);
            }
        }

        public void onTaskRemoved(int taskId) {
            Iterator it = FwkCompat.this.mTaskStackChangeListeners.iterator();
            while (it.hasNext()) {
                TaskStackChangeListener l = (TaskStackChangeListener) it.next();
                l.onTaskRemoved(taskId);
            }
        }
    }
}
