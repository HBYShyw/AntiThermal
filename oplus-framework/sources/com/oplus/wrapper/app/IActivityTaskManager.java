package com.oplus.wrapper.app;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityClientController;
import android.app.IActivityController;
import android.app.IActivityTaskManager;
import android.app.IAssistDataReceiver;
import android.app.IScreenCaptureObserver;
import android.app.ITaskStackListener;
import android.app.PictureInPictureUiState;
import android.app.ProfilerInfo;
import android.app.WaitResult;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionSession;
import android.util.Log;
import android.view.IRecentsAnimationController;
import android.view.IRecentsAnimationRunner;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.window.BackAnimationAdapter;
import android.window.BackNavigationInfo;
import android.window.IWindowOrganizerController;
import android.window.SplashScreenView;
import com.android.internal.app.IVoiceInteractor;
import com.oplus.wrapper.app.ActivityTaskManager;
import com.oplus.wrapper.app.IActivityTaskManager;
import com.oplus.wrapper.app.IAssistDataReceiver;
import com.oplus.wrapper.view.IRecentsAnimationRunner;
import com.oplus.wrapper.view.RemoteAnimationAdapter;
import com.oplus.wrapper.window.TaskSnapshot;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/* loaded from: classes.dex */
public interface IActivityTaskManager {
    void cancelRecentsAnimation(boolean z) throws RemoteException;

    void detachNavigationBarFromApp(IBinder iBinder) throws RemoteException;

    int getLockTaskModeState() throws RemoteException;

    ActivityTaskManager.RootTaskInfo getRootTaskInfo(int i, int i2) throws RemoteException;

    TaskSnapshot getTaskSnapshot(int i, boolean z, boolean z2) throws RemoteException;

    List<ActivityManager.RunningTaskInfo> getTasks(int i, boolean z, boolean z2, int i2) throws RemoteException;

    void registerRemoteAnimationForNextActivityStart(String str, RemoteAnimationAdapter remoteAnimationAdapter, IBinder iBinder) throws RemoteException;

    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void removeAllVisibleRecentTasks() throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    boolean requestAssistDataForTask(IAssistDataReceiver iAssistDataReceiver, int i, String str, String str2) throws RemoteException;

    void setFocusedTask(int i) throws RemoteException;

    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    void startRecentsActivity(Intent intent, long j, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    TaskSnapshot takeTaskSnapshot(int i, boolean z) throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IActivityTaskManager {
        private final Map<android.app.ITaskStackListener, ITaskStackListener> mListenerMap = new ConcurrentHashMap();
        private final android.app.IActivityTaskManager mTarget = new AnonymousClass1();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.oplus.wrapper.app.IActivityTaskManager$Stub$1, reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends IActivityTaskManager.Stub {
            AnonymousClass1() {
            }

            public int startActivity(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent intent, String s2, IBinder iBinder, String s3, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException {
                return 0;
            }

            public int startActivities(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent[] intents, String[] strings, IBinder iBinder, Bundle bundle, int i) throws RemoteException {
                return 0;
            }

            public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
            }

            public void focusTopTask(int displayId) throws RemoteException {
            }

            public int startActivityAsUser(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent intent, String s2, IBinder iBinder, String s3, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle, int i2) throws RemoteException {
                return 0;
            }

            public boolean startNextMatchingActivity(IBinder iBinder, Intent intent, Bundle bundle) throws RemoteException {
                return false;
            }

            public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra, int displayId) throws RemoteException {
                return Stub.this.getTasks(maxNum, filterOnlyVisibleRecents, keepIntentExtra, displayId);
            }

            public boolean startDreamActivity(Intent intent) throws RemoteException {
                return false;
            }

            public int startActivityIntentSender(android.app.IApplicationThread iApplicationThread, IIntentSender iIntentSender, IBinder iBinder, Intent intent, String s, IBinder iBinder1, String s1, int i, int i1, int i2, Bundle bundle) throws RemoteException {
                return 0;
            }

            public WaitResult startActivityAndWait(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent intent, String s2, IBinder iBinder, String s3, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle, int i2) throws RemoteException {
                return null;
            }

            public int startActivityWithConfig(android.app.IApplicationThread iApplicationThread, String s, String s1, Intent intent, String s2, IBinder iBinder, String s3, int i, int i1, Configuration configuration, Bundle bundle, int i2) throws RemoteException {
                return 0;
            }

            public int startVoiceActivity(String s, String s1, int i, int i1, Intent intent, String s2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException {
                return 0;
            }

            public String getVoiceInteractorPackageName(IBinder iBinder) throws RemoteException {
                return null;
            }

            public boolean requestAssistDataForTask(final android.app.IAssistDataReceiver receiver, int taskId, String callingPackageName, String callingAttributionTag) throws RemoteException {
                IAssistDataReceiver oplusAssistDataReceiver = null;
                if (receiver != null) {
                    oplusAssistDataReceiver = new IAssistDataReceiver.Stub() { // from class: com.oplus.wrapper.app.IActivityTaskManager.Stub.1.1
                        @Override // com.oplus.wrapper.app.IAssistDataReceiver
                        public void onHandleAssistData(Bundle resultData) throws RemoteException {
                            receiver.onHandleAssistData(resultData);
                        }

                        @Override // com.oplus.wrapper.app.IAssistDataReceiver
                        public void onHandleAssistScreenshot(Bitmap screenshot) throws RemoteException {
                            receiver.onHandleAssistScreenshot(screenshot);
                        }
                    };
                }
                return Stub.this.requestAssistDataForTask(oplusAssistDataReceiver, taskId, callingPackageName, callingAttributionTag);
            }

            public int startAssistantActivity(String s, String s1, int i, int i1, Intent intent, String s2, Bundle bundle, int i2) throws RemoteException {
                return 0;
            }

            public int startActivityFromGameSession(android.app.IApplicationThread iApplicationThread, String s, String s1, int i, int i1, Intent intent, int i2, int i3) throws RemoteException {
                return 0;
            }

            public void startRecentsActivity(Intent intent, long l, android.view.IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException {
                IRecentsAnimationRunner wrapperRecentsAnimationRunner = null;
                if (iRecentsAnimationRunner != null) {
                    wrapperRecentsAnimationRunner = new IRecentsAnimationRunner.Stub() { // from class: com.oplus.wrapper.app.IActivityTaskManager.Stub.1.2
                        @Override // com.oplus.wrapper.view.IRecentsAnimationRunner.Stub, android.os.IInterface
                        public IBinder asBinder() {
                            return super.asBinder();
                        }
                    };
                }
                Stub.this.startRecentsActivity(intent, l, wrapperRecentsAnimationRunner);
            }

            public int startActivityFromRecents(int i, Bundle bundle) throws RemoteException {
                return Stub.this.startActivityFromRecents(i, bundle);
            }

            public int startActivityAsCaller(android.app.IApplicationThread iApplicationThread, String s, Intent intent, String s1, IBinder iBinder, String s2, int i, int i1, ProfilerInfo profilerInfo, Bundle bundle, boolean b, int i2) throws RemoteException {
                return 0;
            }

            public boolean isActivityStartAllowedOnDisplay(int i, Intent intent, String s, int i1) throws RemoteException {
                return false;
            }

            public void unhandledBack() throws RemoteException {
            }

            public IActivityClientController getActivityClientController() throws RemoteException {
                return null;
            }

            public int getFrontActivityScreenCompatMode() throws RemoteException {
                return 0;
            }

            public void setFrontActivityScreenCompatMode(int i) throws RemoteException {
            }

            public void setFocusedTask(int i) throws RemoteException {
                Stub.this.setFocusedTask(i);
            }

            public boolean removeTask(int i) throws RemoteException {
                return Stub.this.removeTask(i);
            }

            public void removeAllVisibleRecentTasks() throws RemoteException {
                Stub.this.removeAllVisibleRecentTasks();
            }

            public void moveTaskToFront(android.app.IApplicationThread iApplicationThread, String s, int i, int i1, Bundle bundle) throws RemoteException {
            }

            public ParceledListSlice<ActivityManager.RecentTaskInfo> getRecentTasks(int i, int i1, int i2) throws RemoteException {
                return null;
            }

            public boolean isTopActivityImmersive() throws RemoteException {
                return false;
            }

            public ActivityManager.TaskDescription getTaskDescription(int i) throws RemoteException {
                return null;
            }

            public void reportAssistContextExtras(IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, Uri uri) throws RemoteException {
            }

            public void setFocusedRootTask(int i) throws RemoteException {
            }

            public ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws RemoteException {
                return null;
            }

            public Rect getTaskBounds(int i) throws RemoteException {
                return null;
            }

            public void cancelRecentsAnimation(boolean b) throws RemoteException {
                Stub.this.cancelRecentsAnimation(b);
            }

            public void updateLockTaskPackages(int i, String[] strings) throws RemoteException {
            }

            public boolean isInLockTaskMode() throws RemoteException {
                return false;
            }

            public List<IBinder> getAppTasks(String s) throws RemoteException {
                return null;
            }

            public void startSystemLockTaskMode(int i) throws RemoteException {
            }

            public void stopSystemLockTaskMode() throws RemoteException {
            }

            public void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) throws RemoteException {
            }

            public int addAppTask(IBinder iBinder, Intent intent, ActivityManager.TaskDescription taskDescription, Bitmap bitmap) throws RemoteException {
                return 0;
            }

            public Point getAppTaskThumbnailSize() throws RemoteException {
                return null;
            }

            public void releaseSomeActivities(android.app.IApplicationThread iApplicationThread) throws RemoteException {
            }

            public Bitmap getTaskDescriptionIcon(String s, int i) throws RemoteException {
                return null;
            }

            public void registerTaskStackListener(android.app.ITaskStackListener iTaskStackListener) throws RemoteException {
                if (iTaskStackListener == null) {
                    return;
                }
                ITaskStackListener oplusTaskStackListener = (ITaskStackListener) Stub.this.mListenerMap.computeIfAbsent(iTaskStackListener, new Function() { // from class: com.oplus.wrapper.app.IActivityTaskManager$Stub$1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        ITaskStackListener lambda$registerTaskStackListener$0;
                        lambda$registerTaskStackListener$0 = IActivityTaskManager.Stub.AnonymousClass1.this.lambda$registerTaskStackListener$0((android.app.ITaskStackListener) obj);
                        return lambda$registerTaskStackListener$0;
                    }
                });
                Stub.this.registerTaskStackListener(oplusTaskStackListener);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ ITaskStackListener lambda$registerTaskStackListener$0(final android.app.ITaskStackListener taskStackListener) {
                return new ITaskStackListener() { // from class: com.oplus.wrapper.app.IActivityTaskManager.Stub.1.3
                    @Override // com.oplus.wrapper.app.ITaskStackListener
                    public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) throws RemoteException {
                        android.window.TaskSnapshot taskSnapshot = snapshot.getTaskSnapshot();
                        taskStackListener.onTaskSnapshotChanged(taskId, taskSnapshot);
                    }

                    @Override // com.oplus.wrapper.app.ITaskStackListener
                    public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
                        taskStackListener.onActivityPinned(packageName, userId, taskId, stackId);
                    }

                    @Override // com.oplus.wrapper.app.ITaskStackListener
                    public void onActivityUnpinned() throws RemoteException {
                        taskStackListener.onActivityUnpinned();
                    }

                    @Override // com.oplus.wrapper.app.ITaskStackListener
                    public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo taskInfo) throws RemoteException {
                        taskStackListener.onTaskDescriptionChanged(taskInfo);
                    }
                };
            }

            public void unregisterTaskStackListener(android.app.ITaskStackListener iTaskStackListener) throws RemoteException {
                ITaskStackListener oplusTaskStackListener;
                if (iTaskStackListener == null || (oplusTaskStackListener = (ITaskStackListener) Stub.this.mListenerMap.get(iTaskStackListener)) == null) {
                    return;
                }
                Stub.this.unregisterTaskStackListener(oplusTaskStackListener);
            }

            public void setTaskResizeable(int i, int i1) throws RemoteException {
            }

            public void moveRootTaskToDisplay(int i, int i1) throws RemoteException {
            }

            public void moveTaskToRootTask(int i, int i1, boolean b) throws RemoteException {
            }

            public android.window.TaskSnapshot getTaskSnapshot(int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded) throws RemoteException {
                return Stub.this.getTaskSnapshot(taskId, isLowResolution, takeSnapshotIfNeeded).getTaskSnapshot();
            }

            public void removeRootTasksInWindowingModes(int[] ints) throws RemoteException {
            }

            public void removeRootTasksWithActivityTypes(int[] ints) throws RemoteException {
            }

            public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() throws RemoteException {
                return null;
            }

            public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int i, int i1) throws RemoteException {
                return Stub.this.getRootTaskInfo(i, i1).getRootTaskInfo();
            }

            public List<ActivityTaskManager.RootTaskInfo> getAllRootTaskInfosOnDisplay(int i) throws RemoteException {
                return null;
            }

            public ActivityTaskManager.RootTaskInfo getRootTaskInfoOnDisplay(int i, int i1, int i2) throws RemoteException {
                return null;
            }

            public void setLockScreenShown(boolean b, boolean b1) throws RemoteException {
            }

            public void registerScreenCaptureObserver(IBinder i, IScreenCaptureObserver o) throws RemoteException {
            }

            public void unregisterScreenCaptureObserver(IBinder i, IScreenCaptureObserver o) throws RemoteException {
            }

            public Bundle getAssistContextExtras(int i) throws RemoteException {
                return null;
            }

            public boolean requestAssistContextExtras(int i, android.app.IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, boolean b, boolean b1) throws RemoteException {
                return false;
            }

            public boolean requestAutofillData(android.app.IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, int i) throws RemoteException {
                return false;
            }

            public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
                return false;
            }

            public void keyguardGoingAway(int i) throws RemoteException {
            }

            public void suppressResizeConfigChanges(boolean b) throws RemoteException {
            }

            public IWindowOrganizerController getWindowOrganizerController() throws RemoteException {
                return null;
            }

            public boolean supportsLocalVoiceInteraction() throws RemoteException {
                return false;
            }

            public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
                return null;
            }

            public void cancelTaskWindowTransition(int i) throws RemoteException {
            }

            public android.window.TaskSnapshot takeTaskSnapshot(int i, boolean b) throws RemoteException {
                TaskSnapshot taskSnapshot = Stub.this.takeTaskSnapshot(i, b);
                if (taskSnapshot == null) {
                    return null;
                }
                return taskSnapshot.getTaskSnapshot();
            }

            public int getLastResumedActivityUserId() throws RemoteException {
                return 0;
            }

            public boolean updateConfiguration(Configuration configuration) throws RemoteException {
                return false;
            }

            public void updateLockTaskFeatures(int i, int i1) throws RemoteException {
            }

            public void registerRemoteAnimationForNextActivityStart(String s, android.view.RemoteAnimationAdapter remoteAnimationAdapter, IBinder iBinder) throws RemoteException {
                if (remoteAnimationAdapter == null) {
                    return;
                }
                Stub.this.registerRemoteAnimationForNextActivityStart(s, new RemoteAnimationAdapter(remoteAnimationAdapter), iBinder);
            }

            public void registerRemoteAnimationsForDisplay(int i, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException {
            }

            public void alwaysShowUnsupportedCompileSdkWarning(ComponentName componentName) throws RemoteException {
            }

            public void setVrThread(int i) throws RemoteException {
            }

            public void setPersistentVrThread(int i) throws RemoteException {
            }

            public void stopAppSwitches() throws RemoteException {
            }

            public void resumeAppSwitches() throws RemoteException {
            }

            public void setActivityController(IActivityController iActivityController, boolean b) throws RemoteException {
            }

            public void setVoiceKeepAwake(IVoiceInteractionSession iVoiceInteractionSession, boolean b) throws RemoteException {
            }

            public int getPackageScreenCompatMode(String s) throws RemoteException {
                return 0;
            }

            public void setPackageScreenCompatMode(String s, int i) throws RemoteException {
            }

            public boolean getPackageAskScreenCompat(String s) throws RemoteException {
                return false;
            }

            public void setPackageAskScreenCompat(String s, boolean b) throws RemoteException {
            }

            public void clearLaunchParamsForPackages(List<String> list) throws RemoteException {
            }

            public void onSplashScreenViewCopyFinished(int i, SplashScreenView.SplashScreenViewParcelable splashScreenViewParcelable) throws RemoteException {
            }

            public void onPictureInPictureStateChanged(PictureInPictureUiState pictureInPictureUiState) throws RemoteException {
            }

            public void detachNavigationBarFromApp(IBinder iBinder) throws RemoteException {
                Stub.this.detachNavigationBarFromApp(iBinder);
            }

            public void setRunningRemoteTransitionDelegate(android.app.IApplicationThread iApplicationThread) throws RemoteException {
            }

            public int getLockTaskModeState() throws RemoteException {
                return Stub.this.getLockTaskModeState();
            }

            public BackNavigationInfo startBackNavigation(RemoteCallback c, BackAnimationAdapter b) {
                return null;
            }
        }

        public static IActivityTaskManager asInterface(IBinder obj) {
            return new Proxy(IActivityTaskManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IActivityTaskManager {
            private static final String TAG = "IActivityTaskManager";
            private final Map<ITaskStackListener, TaskStackListener> mIActivityTaskManagerMap = new ConcurrentHashMap();
            private final android.app.IActivityTaskManager mTarget;

            Proxy(android.app.IActivityTaskManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void registerTaskStackListener(final ITaskStackListener listener) throws RemoteException {
                if (listener == null) {
                    return;
                }
                TaskStackListener stackListener = this.mIActivityTaskManagerMap.computeIfAbsent(listener, new Function() { // from class: com.oplus.wrapper.app.IActivityTaskManager$Stub$Proxy$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return IActivityTaskManager.Stub.Proxy.lambda$registerTaskStackListener$0(ITaskStackListener.this, (ITaskStackListener) obj);
                    }
                });
                this.mTarget.registerTaskStackListener(stackListener);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static /* synthetic */ TaskStackListener lambda$registerTaskStackListener$0(ITaskStackListener listener, ITaskStackListener oplusListener) {
                return new TaskStackListener(listener);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                TaskStackListener stackListener;
                if (listener == null || (stackListener = this.mIActivityTaskManagerMap.get(listener)) == null) {
                    return;
                }
                this.mTarget.unregisterTaskStackListener(stackListener);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void setFocusedTask(int taskId) throws RemoteException {
                this.mTarget.setFocusedTask(taskId);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public TaskSnapshot takeTaskSnapshot(int taskId, boolean updateCache) throws RemoteException {
                android.window.TaskSnapshot taskSnapshot = this.mTarget.takeTaskSnapshot(taskId, updateCache);
                if (taskSnapshot == null) {
                    return null;
                }
                return new TaskSnapshot(taskSnapshot);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter, IBinder launchCookie) throws RemoteException {
                android.view.RemoteAnimationAdapter remoteAnimationAdapter = adapter.getRemoteAnimationAdapter();
                if (remoteAnimationAdapter == null) {
                    Log.d(TAG, "registerRemoteAnimationForNextActivityStart getRemoteAnimationAdapter is null");
                } else {
                    this.mTarget.registerRemoteAnimationForNextActivityStart(packageName, remoteAnimationAdapter, launchCookie);
                }
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void cancelRecentsAnimation(boolean restoreHomeRootTaskPosition) throws RemoteException {
                this.mTarget.cancelRecentsAnimation(restoreHomeRootTaskPosition);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void detachNavigationBarFromApp(IBinder transition) throws RemoteException {
                this.mTarget.detachNavigationBarFromApp(transition);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public int getLockTaskModeState() throws RemoteException {
                return this.mTarget.getLockTaskModeState();
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType) throws RemoteException {
                return new ActivityTaskManager.RootTaskInfo(this.mTarget.getRootTaskInfo(windowingMode, activityType));
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public TaskSnapshot getTaskSnapshot(int taskId, boolean isLowResolution, boolean takeSnapshotIfNeeded) throws RemoteException {
                return new TaskSnapshot(this.mTarget.getTaskSnapshot(taskId, isLowResolution, takeSnapshotIfNeeded));
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public List<ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra, int displayId) throws RemoteException {
                return this.mTarget.getTasks(maxNum, filterOnlyVisibleRecents, keepIntentExtra, displayId);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void removeAllVisibleRecentTasks() throws RemoteException {
                this.mTarget.removeAllVisibleRecentTasks();
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public boolean removeTask(int taskId) throws RemoteException {
                return this.mTarget.removeTask(taskId);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
                return this.mTarget.startActivityFromRecents(taskId, options);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public boolean requestAssistDataForTask(final IAssistDataReceiver receiver, int taskId, String callingPackageName, String callingAttributionTag) throws RemoteException {
                android.app.IAssistDataReceiver iAssistDataReceiver = null;
                if (receiver != null) {
                    iAssistDataReceiver = new IAssistDataReceiver.Stub() { // from class: com.oplus.wrapper.app.IActivityTaskManager.Stub.Proxy.1
                        public void onHandleAssistData(Bundle bundle) throws RemoteException {
                            receiver.onHandleAssistData(bundle);
                        }

                        public void onHandleAssistScreenshot(Bitmap bitmap) throws RemoteException {
                            receiver.onHandleAssistScreenshot(bitmap);
                        }
                    };
                }
                return this.mTarget.requestAssistDataForTask(iAssistDataReceiver, taskId, callingPackageName, callingAttributionTag);
            }

            @Override // com.oplus.wrapper.app.IActivityTaskManager
            public void startRecentsActivity(Intent intent, long eventTime, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
                android.view.IRecentsAnimationRunner iRecentsAnimationRunner = null;
                if (recentsAnimationRunner != null) {
                    iRecentsAnimationRunner = new IRecentsAnimationRunner.Stub() { // from class: com.oplus.wrapper.app.IActivityTaskManager.Stub.Proxy.2
                        public void onAnimationCanceled(int[] ints, android.window.TaskSnapshot[] taskSnapshots) throws RemoteException {
                        }

                        public void onAnimationStart(IRecentsAnimationController iRecentsAnimationController, RemoteAnimationTarget[] remoteAnimationTargets, RemoteAnimationTarget[] remoteAnimationTargets1, Rect rect, Rect rect1) throws RemoteException {
                        }

                        public void onTasksAppeared(RemoteAnimationTarget[] remoteAnimationTargets) throws RemoteException {
                        }
                    };
                }
                this.mTarget.startRecentsActivity(intent, eventTime, iRecentsAnimationRunner);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class TaskStackListener extends ITaskStackListener.Stub {
            private final ITaskStackListener mITaskStackListener;

            public TaskStackListener(ITaskStackListener taskStackListener) {
                this.mITaskStackListener = taskStackListener;
            }

            public void onTaskStackChanged() throws RemoteException {
            }

            public void onActivityPinned(String s, int i, int i1, int i2) throws RemoteException {
                this.mITaskStackListener.onActivityPinned(s, i, i1, i2);
            }

            public void onActivityUnpinned() throws RemoteException {
                this.mITaskStackListener.onActivityUnpinned();
            }

            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean b, boolean b1, boolean b2) throws RemoteException {
            }

            public void onActivityForcedResizable(String s, int i, int i1) throws RemoteException {
            }

            public void onActivityDismissingDockedTask() throws RemoteException {
            }

            public void onActivityLaunchOnSecondaryDisplayFailed(ActivityManager.RunningTaskInfo runningTaskInfo, int i) throws RemoteException {
            }

            public void onActivityLaunchOnSecondaryDisplayRerouted(ActivityManager.RunningTaskInfo runningTaskInfo, int i) throws RemoteException {
            }

            public void onTaskCreated(int i, ComponentName componentName) throws RemoteException {
            }

            public void onTaskRemoved(int i) throws RemoteException {
            }

            public void onTaskMovedToFront(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onTaskDescriptionChanged(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
                this.mITaskStackListener.onTaskDescriptionChanged(runningTaskInfo);
            }

            public void onActivityRequestedOrientationChanged(int i, int i1) throws RemoteException {
            }

            public void onTaskRemovalStarted(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onTaskProfileLocked(ActivityManager.RunningTaskInfo runningTaskInfo, int userid) throws RemoteException {
            }

            public void onTaskSnapshotChanged(int i, android.window.TaskSnapshot taskSnapshot) throws RemoteException {
                if (taskSnapshot == null) {
                    return;
                }
                this.mITaskStackListener.onTaskSnapshotChanged(i, new TaskSnapshot(taskSnapshot));
            }

            public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onTaskDisplayChanged(int i, int i1) throws RemoteException {
            }

            public void onRecentTaskListUpdated() throws RemoteException {
            }

            public void onRecentTaskListFrozenChanged(boolean b) throws RemoteException {
            }

            public void onTaskFocusChanged(int i, boolean b) throws RemoteException {
            }

            public void onTaskRequestedOrientationChanged(int i, int i1) throws RemoteException {
            }

            public void onActivityRotation(int i) throws RemoteException {
            }

            public void onTaskMovedToBack(ActivityManager.RunningTaskInfo runningTaskInfo) throws RemoteException {
            }

            public void onLockTaskModeChanged(int i) throws RemoteException {
            }
        }
    }
}
