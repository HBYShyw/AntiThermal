package com.android.server.wm;

import android.app.ActivityOptions;
import android.app.BackgroundStartPrivileges;
import android.app.IApplicationThread;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayInfo;
import android.view.RemoteAnimationAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.server.am.PendingIntentRecord;
import com.android.server.uri.NeededUriGrants;
import com.android.server.uri.UriGrantsManagerInternal;
import com.android.server.wm.ActivityMetricsLogger;
import com.android.server.wm.ActivityStarter;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.IntFunction;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityStartController {
    private static final int DO_PENDING_ACTIVITY_LAUNCHES_MSG = 1;
    private static final String TAG = "ActivityTaskManager";
    private final BackgroundActivityStartController mBalController;
    boolean mCheckedForSetup;
    private final ActivityStarter.Factory mFactory;
    private boolean mInExecution;
    private ActivityRecord mLastHomeActivityStartRecord;
    private int mLastHomeActivityStartResult;
    private ActivityStarter mLastStarter;
    private final PendingRemoteAnimationRegistry mPendingRemoteAnimationRegistry;
    private final ActivityTaskManagerService mService;
    private final ActivityTaskSupervisor mSupervisor;
    private ActivityRecord[] tmpOutRecord;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ActivityStartController(ActivityTaskManagerService activityTaskManagerService) {
        this(activityTaskManagerService, r0, new ActivityStarter.DefaultFactory(activityTaskManagerService, r0, new ActivityStartInterceptor(activityTaskManagerService, r0)));
        ActivityTaskSupervisor activityTaskSupervisor = activityTaskManagerService.mTaskSupervisor;
    }

    @VisibleForTesting
    ActivityStartController(ActivityTaskManagerService activityTaskManagerService, ActivityTaskSupervisor activityTaskSupervisor, ActivityStarter.Factory factory) {
        this.tmpOutRecord = new ActivityRecord[1];
        this.mCheckedForSetup = false;
        this.mInExecution = false;
        this.mService = activityTaskManagerService;
        this.mSupervisor = activityTaskSupervisor;
        this.mFactory = factory;
        factory.setController(this);
        this.mPendingRemoteAnimationRegistry = new PendingRemoteAnimationRegistry(activityTaskManagerService.mGlobalLock, activityTaskManagerService.mH);
        this.mBalController = new BackgroundActivityStartController(activityTaskManagerService, activityTaskSupervisor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityStarter obtainStarter(Intent intent, String str) {
        return this.mFactory.obtain().setIntent(intent).setReason(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onExecutionStarted() {
        this.mInExecution = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInExecution() {
        return this.mInExecution;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onExecutionComplete(ActivityStarter activityStarter) {
        this.mInExecution = false;
        if (this.mLastStarter == null) {
            this.mLastStarter = this.mFactory.obtain();
        }
        this.mLastStarter.set(activityStarter);
        this.mFactory.recycle(activityStarter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postStartActivityProcessingForLastStarter(ActivityRecord activityRecord, int i, Task task) {
        ActivityStarter activityStarter = this.mLastStarter;
        if (activityStarter == null) {
            return;
        }
        activityStarter.postStartActivityProcessing(activityRecord, i, task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startHomeActivity(Intent intent, ActivityInfo activityInfo, String str, TaskDisplayArea taskDisplayArea) {
        ActivityOptions makeBasic = ActivityOptions.makeBasic();
        makeBasic.setLaunchWindowingMode(1);
        if (!ActivityRecord.isResolverActivity(activityInfo.name)) {
            makeBasic.setLaunchActivityType(2);
        }
        makeBasic.setLaunchDisplayId(taskDisplayArea.getDisplayId());
        makeBasic.setLaunchTaskDisplayArea(taskDisplayArea.mRemoteToken.toWindowContainerToken());
        this.mSupervisor.beginDeferResume();
        try {
            Task orCreateRootHomeTask = taskDisplayArea.getOrCreateRootHomeTask(true);
            this.mSupervisor.endDeferResume();
            this.mLastHomeActivityStartResult = obtainStarter(intent, "startHomeActivity: " + str).setOutActivity(this.tmpOutRecord).setCallingUid(0).setActivityInfo(activityInfo).setActivityOptions(makeBasic.toBundle()).execute();
            this.mLastHomeActivityStartRecord = this.tmpOutRecord[0];
            DisplayInfo displayInfo = orCreateRootHomeTask != null ? orCreateRootHomeTask.getDisplayInfo() : null;
            if (orCreateRootHomeTask != null) {
                if (orCreateRootHomeTask.mInResumeTopActivity || !(displayInfo == null || displayInfo.displayId == 0 || displayInfo.type != 1)) {
                    this.mSupervisor.scheduleResumeTopActivities();
                }
            }
        } catch (Throwable th) {
            this.mSupervisor.endDeferResume();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSetupActivity() {
        Bundle bundle;
        if (this.mCheckedForSetup) {
            return;
        }
        ContentResolver contentResolver = this.mService.mContext.getContentResolver();
        if (this.mService.mFactoryTest == 1 || Settings.Global.getInt(contentResolver, "device_provisioned", 0) == 0) {
            return;
        }
        this.mCheckedForSetup = true;
        Intent intent = new Intent("android.intent.action.UPGRADE_SETUP");
        List<ResolveInfo> queryIntentActivities = this.mService.mContext.getPackageManager().queryIntentActivities(intent, 1049728);
        if (queryIntentActivities.isEmpty()) {
            return;
        }
        ResolveInfo resolveInfo = queryIntentActivities.get(0);
        Bundle bundle2 = resolveInfo.activityInfo.metaData;
        String string = bundle2 != null ? bundle2.getString("android.SETUP_VERSION") : null;
        if (string == null && (bundle = resolveInfo.activityInfo.applicationInfo.metaData) != null) {
            string = bundle.getString("android.SETUP_VERSION");
        }
        String stringForUser = Settings.Secure.getStringForUser(contentResolver, "last_setup_shown", contentResolver.getUserId());
        if (string == null || string.equals(stringForUser)) {
            return;
        }
        intent.setFlags(268435456);
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        obtainStarter(intent, "startSetupActivity").setCallingUid(0).setActivityInfo(resolveInfo.activityInfo).execute();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int checkTargetUser(int i, boolean z, int i2, int i3, String str) {
        if (z) {
            return this.mService.handleIncomingUser(i2, i3, i, str);
        }
        this.mService.mAmInternal.ensureNotSpecialUser(i);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int startActivityInPackage(int i, int i2, int i3, String str, String str2, Intent intent, String str3, IBinder iBinder, String str4, int i4, int i5, SafeActivityOptions safeActivityOptions, int i6, Task task, String str5, boolean z, PendingIntentRecord pendingIntentRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        return obtainStarter(intent, str5).setCallingUid(i).setRealCallingPid(i2).setRealCallingUid(i3).setCallingPackage(str).setCallingFeatureId(str2).setResolvedType(str3).setResultTo(iBinder).setResultWho(str4).setRequestCode(i4).setStartFlags(i5).setActivityOptions(safeActivityOptions).setUserId(checkTargetUser(i6, z, i2, i3, str5)).setInTask(task).setOriginatingPendingIntent(pendingIntentRecord).setBackgroundStartPrivileges(backgroundStartPrivileges).execute();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int startActivitiesInPackage(int i, String str, String str2, Intent[] intentArr, String[] strArr, IBinder iBinder, SafeActivityOptions safeActivityOptions, int i2, boolean z, PendingIntentRecord pendingIntentRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        return startActivitiesInPackage(i, 0, -1, str, str2, intentArr, strArr, iBinder, safeActivityOptions, i2, z, pendingIntentRecord, backgroundStartPrivileges);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int startActivitiesInPackage(int i, int i2, int i3, String str, String str2, Intent[] intentArr, String[] strArr, IBinder iBinder, SafeActivityOptions safeActivityOptions, int i4, boolean z, PendingIntentRecord pendingIntentRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        return startActivities(null, i, i2, i3, str, str2, intentArr, strArr, iBinder, safeActivityOptions, checkTargetUser(i4, z, Binder.getCallingPid(), Binder.getCallingUid(), "startActivityInPackage"), "startActivityInPackage", pendingIntentRecord, backgroundStartPrivileges);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0229, code lost:
    
        monitor-exit(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x022a, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        android.os.Binder.restoreCallingIdentity(r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0230, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0256, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0257, code lost:
    
        r3 = r29.mService.mWindowManager.mStartingSurfaceController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x025d, code lost:
    
        if (r39 != null) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x025f, code lost:
    
        r19 = r39.getOriginalOptions();
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0263, code lost:
    
        r3.endDeferAddStartingWindow(r19);
        r29.mService.continueWindowLayout();
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x026d, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x026e, code lost:
    
        r0 = r29.mService.mWindowManager.mStartingSurfaceController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0274, code lost:
    
        if (r39 == null) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0276, code lost:
    
        r19 = r39.getOriginalOptions();
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x027a, code lost:
    
        r0.endDeferAddStartingWindow(r19);
        r29.mService.continueWindowLayout();
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0284, code lost:
    
        monitor-exit(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0285, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x028b, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x028c, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x028e, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0291, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0191, code lost:
    
        r25 = r11;
        r26 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x019c, code lost:
    
        if (r31.size() <= 1) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x019e, code lost:
    
        r0 = new java.lang.StringBuilder("startActivities: different apps [");
        r4 = r31.size();
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01ab, code lost:
    
        if (r5 >= r4) goto L131;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01ad, code lost:
    
        r0.append((java.lang.String) r31.valueAt(r5));
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01b8, code lost:
    
        if (r5 != (r4 - 1)) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01ba, code lost:
    
        r6 = "]";
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01bf, code lost:
    
        r0.append(r6);
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01bd, code lost:
    
        r6 = ", ";
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01c5, code lost:
    
        r0.append(" from ");
        r0.append(r34);
        android.util.Slog.wtf(com.android.server.wm.ActivityStartController.TAG, r0.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01d6, code lost:
    
        r0 = new com.android.server.wm.ActivityRecord[1];
        r2 = r29.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01df, code lost:
    
        monitor-enter(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01e0, code lost:
    
        r29.mService.deferWindowLayout();
        r29.mService.mWindowManager.mStartingSurfaceController.beginDeferAddStartingWindow();
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01ee, code lost:
    
        r5 = r38;
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01f2, code lost:
    
        r6 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01f4, code lost:
    
        if (r4 >= r6) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01f6, code lost:
    
        r5 = r25[r4].setResultTo(r5).setOutActivity(r0).execute();
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0204, code lost:
    
        if (r5 >= 0) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0231, code lost:
    
        r5 = r0[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0233, code lost:
    
        if (r5 == null) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0239, code lost:
    
        if (r5.getUid() != r14) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x023b, code lost:
    
        r5 = r5.token;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0251, code lost:
    
        r4 = r4 + 1;
        r26 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0240, code lost:
    
        if (r4 >= (r6 - 1)) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0242, code lost:
    
        r25[r4 + 1].getIntent().addFlags(268435456);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x024f, code lost:
    
        r5 = r38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0206, code lost:
    
        r4 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0207, code lost:
    
        if (r4 >= r6) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0209, code lost:
    
        r29.mFactory.recycle(r25[r4]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0210, code lost:
    
        r4 = r4 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int startActivities(IApplicationThread iApplicationThread, int i, int i2, int i3, String str, String str2, Intent[] intentArr, String[] strArr, IBinder iBinder, SafeActivityOptions safeActivityOptions, int i4, String str3, PendingIntentRecord pendingIntentRecord, BackgroundStartPrivileges backgroundStartPrivileges) {
        int i5;
        int i6;
        Intent[] intentArr2;
        int length;
        ActivityStarter[] activityStarterArr;
        int i7;
        int i8;
        Intent intent;
        SparseArray sparseArray;
        NeededUriGrants neededUriGrants;
        String str4;
        int i9;
        SafeActivityOptions safeActivityOptions2;
        if (intentArr == null) {
            throw new NullPointerException("intents is null");
        }
        if (strArr == null) {
            throw new NullPointerException("resolvedTypes is null");
        }
        if (intentArr.length != strArr.length) {
            throw new IllegalArgumentException("intents are length different than resolvedTypes");
        }
        int callingPid = i2 != 0 ? i2 : Binder.getCallingPid();
        int i10 = i3;
        if (i10 == -1) {
            i10 = Binder.getCallingUid();
        }
        if (i >= 0) {
            i5 = i;
            i6 = -1;
        } else if (iApplicationThread == null) {
            i6 = callingPid;
            i5 = i10;
        } else {
            i5 = -1;
            i6 = -1;
        }
        int computeResolveFilterUid = ActivityStarter.computeResolveFilterUid(i5, i10, -10000);
        SparseArray sparseArray2 = new SparseArray();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        ActivityOptions activityOptions = null;
        SafeActivityOptions selectiveCloneLaunchOptions = safeActivityOptions != null ? safeActivityOptions.selectiveCloneLaunchOptions() : null;
        try {
            intentArr2 = (Intent[]) ArrayUtils.filterNotNull(intentArr, new IntFunction() { // from class: com.android.server.wm.ActivityStartController$$ExternalSyntheticLambda0
                @Override // java.util.function.IntFunction
                public final Object apply(int i11) {
                    Intent[] lambda$startActivities$0;
                    lambda$startActivities$0 = ActivityStartController.lambda$startActivities$0(i11);
                    return lambda$startActivities$0;
                }
            });
            length = intentArr2.length;
            activityStarterArr = new ActivityStarter[length];
            i7 = i4;
            i8 = 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
        while (true) {
            SparseArray sparseArray3 = sparseArray2;
            if (i8 >= intentArr2.length) {
                break;
            }
            Intent intent2 = intentArr2[i8];
            if (intent2.hasFileDescriptors()) {
                throw new IllegalArgumentException("File descriptors passed in Intent");
            }
            int adjustUserIdIfNeed = this.mService.getWrapper().getExtImpl().adjustUserIdIfNeed(intent2, i7);
            boolean z = intent2.getComponent() != null;
            Intent intent3 = new Intent(intent2);
            boolean z2 = z;
            int i11 = i8;
            ActivityStarter[] activityStarterArr2 = activityStarterArr;
            int i12 = length;
            int i13 = computeResolveFilterUid;
            int i14 = i6;
            ActivityInfo activityInfoForUser = this.mService.mAmInternal.getActivityInfoForUser(this.mSupervisor.resolveActivity(intent3, strArr[i8], 0, null, adjustUserIdIfNeed, i13, i14), adjustUserIdIfNeed);
            if (activityInfoForUser != null) {
                try {
                    UriGrantsManagerInternal uriGrantsManagerInternal = this.mSupervisor.mService.mUgmInternal;
                    ApplicationInfo applicationInfo = activityInfoForUser.applicationInfo;
                    computeResolveFilterUid = i13;
                    intent = intent3;
                    NeededUriGrants checkGrantUriPermissionFromIntent = uriGrantsManagerInternal.checkGrantUriPermissionFromIntent(intent, computeResolveFilterUid, applicationInfo.packageName, UserHandle.getUserId(applicationInfo.uid));
                    ApplicationInfo applicationInfo2 = activityInfoForUser.applicationInfo;
                    if ((applicationInfo2.privateFlags & 2) != 0) {
                        throw new IllegalArgumentException("FLAG_CANT_SAVE_STATE not supported here");
                    }
                    sparseArray = sparseArray3;
                    sparseArray.put(applicationInfo2.uid, applicationInfo2.packageName);
                    neededUriGrants = checkGrantUriPermissionFromIntent;
                } catch (SecurityException unused) {
                    Slog.d(TAG, "Not allowed to start activity since no uri permission.");
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return -96;
                }
            } else {
                computeResolveFilterUid = i13;
                sparseArray = sparseArray3;
                intent = intent3;
                neededUriGrants = null;
            }
            Intent[] intentArr3 = intentArr2;
            boolean z3 = i11 == intentArr2.length - 1;
            if (z3) {
                str4 = str3;
                i9 = adjustUserIdIfNeed;
                safeActivityOptions2 = safeActivityOptions;
            } else {
                str4 = str3;
                i9 = adjustUserIdIfNeed;
                safeActivityOptions2 = selectiveCloneLaunchOptions;
            }
            ActivityStarter requestCode = obtainStarter(intent, str4).setIntentGrants(neededUriGrants).setCaller(iApplicationThread).setResolvedType(strArr[i11]).setActivityInfo(activityInfoForUser).setRequestCode(-1);
            i6 = i14;
            activityStarterArr2[i11] = requestCode.setCallingPid(i6).setCallingUid(i5).setCallingPackage(str).setCallingFeatureId(str2).setRealCallingPid(callingPid).setRealCallingUid(i10).setActivityOptions(safeActivityOptions2).setComponentSpecified(z2).setAllowPendingRemoteAnimationRegistryLookup(z3).setOriginatingPendingIntent(pendingIntentRecord).setBackgroundStartPrivileges(backgroundStartPrivileges);
            intentArr2 = intentArr3;
            i7 = i9;
            sparseArray2 = sparseArray;
            activityStarterArr = activityStarterArr2;
            length = i12;
            i8 = i11 + 1;
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Intent[] lambda$startActivities$0(int i) {
        return new Intent[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int startActivityInTaskFragment(TaskFragment taskFragment, Intent intent, Bundle bundle, IBinder iBinder, int i, int i2, IBinder iBinder2) {
        ActivityRecord forTokenLocked = iBinder != null ? ActivityRecord.forTokenLocked(iBinder) : null;
        return obtainStarter(intent, "startActivityInTaskFragment").setActivityOptions(bundle).setInTaskFragment(taskFragment).setResultTo(iBinder).setRequestCode(-1).setCallingUid(i).setCallingPid(i2).setRealCallingUid(i).setRealCallingPid(i2).setUserId(forTokenLocked != null ? forTokenLocked.mUserId : this.mService.getCurrentUserId()).setErrorCallbackToken(iBinder2).execute();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startExistingRecentsIfPossible(Intent intent, ActivityOptions activityOptions) {
        try {
            Trace.traceBegin(32L, "startExistingRecents");
            if (startExistingRecents(intent, activityOptions)) {
                Trace.traceEnd(32L);
                return true;
            }
            Trace.traceEnd(32L);
            return false;
        } catch (Throwable th) {
            Trace.traceEnd(32L);
            throw th;
        }
    }

    private boolean startExistingRecents(Intent intent, ActivityOptions activityOptions) {
        ActivityRecord activityRecord;
        Task rootTask = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea().getRootTask(0, this.mService.getRecentTasks().getRecentsComponent().equals(intent.getComponent()) ? 3 : 2);
        if (rootTask == null || (activityRecord = rootTask.topRunningActivity()) == null || activityRecord.isVisibleRequested() || !activityRecord.attachedToProcess() || !activityRecord.mActivityComponent.equals(intent.getComponent()) || !this.mService.isCallerRecents(activityRecord.getUid()) || activityRecord.mDisplayContent.isKeyguardLocked()) {
            return false;
        }
        this.mService.mRootWindowContainer.startPowerModeLaunchIfNeeded(true, activityRecord);
        ActivityMetricsLogger.LaunchingState notifyActivityLaunching = this.mSupervisor.getActivityMetricsLogger().notifyActivityLaunching(intent);
        Task task = activityRecord.getTask();
        this.mService.deferWindowLayout();
        try {
            TransitionController transitionController = activityRecord.mTransitionController;
            Transition collectingTransition = transitionController.getCollectingTransition();
            if (collectingTransition != null) {
                collectingTransition.setRemoteAnimationApp(activityRecord.app.getThread());
                transitionController.setTransientLaunch(activityRecord, TaskDisplayArea.getRootTaskAbove(rootTask));
            }
            task.moveToFront("startExistingRecents");
            task.mInResumeTopActivity = true;
            task.resumeTopActivity(null, activityOptions, true);
            this.mSupervisor.getActivityMetricsLogger().notifyActivityLaunched(notifyActivityLaunching, 2, false, activityRecord, activityOptions);
            return true;
        } finally {
            task.mInResumeTopActivity = false;
            this.mService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerRemoteAnimationForNextActivityStart(String str, RemoteAnimationAdapter remoteAnimationAdapter, IBinder iBinder) {
        this.mPendingRemoteAnimationRegistry.addPendingAnimation(str, remoteAnimationAdapter, iBinder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingRemoteAnimationRegistry getPendingRemoteAnimationRegistry() {
        return this.mPendingRemoteAnimationRegistry;
    }

    void dumpLastHomeActivityStartResult(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("mLastHomeActivityStartResult=");
        printWriter.println(this.mLastHomeActivityStartResult);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str, String str2) {
        boolean z;
        ActivityRecord activityRecord;
        boolean z2 = true;
        boolean z3 = false;
        boolean z4 = str2 != null;
        ActivityRecord activityRecord2 = this.mLastHomeActivityStartRecord;
        if (activityRecord2 == null || (z4 && !str2.equals(activityRecord2.packageName))) {
            z = false;
        } else {
            dumpLastHomeActivityStartResult(printWriter, str);
            printWriter.print(str);
            printWriter.println("mLastHomeActivityStartRecord:");
            this.mLastHomeActivityStartRecord.dump(printWriter, str + "  ", true);
            z = true;
        }
        ActivityStarter activityStarter = this.mLastStarter;
        if (activityStarter != null) {
            if (!z4 || activityStarter.relatedToPackage(str2) || ((activityRecord = this.mLastHomeActivityStartRecord) != null && str2.equals(activityRecord.packageName))) {
                z3 = true;
            }
            if (z3) {
                if (z) {
                    z2 = z;
                } else {
                    dumpLastHomeActivityStartResult(printWriter, str);
                }
                printWriter.print(str);
                printWriter.println("mLastStarter:");
                this.mLastStarter.dump(printWriter, str + "  ");
                if (z4) {
                    return;
                } else {
                    z = z2;
                }
            }
        }
        if (z) {
            return;
        }
        printWriter.print(str);
        printWriter.println("(nothing)");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackgroundActivityStartController getBackgroundActivityLaunchController() {
        return this.mBalController;
    }
}
