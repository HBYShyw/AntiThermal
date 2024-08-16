package com.android.server.am;

import android.app.AnrController;
import android.content.Context;
import android.os.Handler;
import com.android.internal.annotations.GuardedBy;
import com.android.server.am.AppErrorDialog;
import com.android.server.am.AppNotRespondingDialog;
import com.android.server.wm.WindowManagerInternal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ErrorDialogController {

    @GuardedBy({"mProcLock"})
    private AnrController mAnrController;

    @GuardedBy({"mProcLock"})
    private List<AppNotRespondingDialog> mAnrDialogs;
    private final ProcessRecord mApp;

    @GuardedBy({"mProcLock"})
    private List<AppErrorDialog> mCrashDialogs;
    private final ActivityManagerGlobalLock mProcLock;
    private final ActivityManagerService mService;

    @GuardedBy({"mProcLock"})
    private List<StrictModeViolationDialog> mViolationDialogs;

    @GuardedBy({"mProcLock"})
    private AppWaitingForDebuggerDialog mWaitDialog;

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasCrashDialogs() {
        return this.mCrashDialogs != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public List<AppErrorDialog> getCrashDialogs() {
        return this.mCrashDialogs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasAnrDialogs() {
        return this.mAnrDialogs != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public List<AppNotRespondingDialog> getAnrDialogs() {
        return this.mAnrDialogs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasViolationDialogs() {
        return this.mViolationDialogs != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public boolean hasDebugWaitingDialog() {
        return this.mWaitDialog != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void clearAllErrorDialogs() {
        clearCrashDialogs();
        clearAnrDialogs();
        clearViolationDialogs();
        clearWaitingDialog();
        clearAnrErrorDialogs();
        clearAnrErrorProgressDialogs();
    }

    @GuardedBy({"mProcLock"})
    void clearCrashDialogs() {
        clearCrashDialogs(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void clearCrashDialogs(boolean z) {
        List<AppErrorDialog> list = this.mCrashDialogs;
        if (list == null) {
            return;
        }
        if (z) {
            scheduleForAllDialogs(list, new ErrorDialogController$$ExternalSyntheticLambda4());
        }
        this.mCrashDialogs = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void clearAnrDialogs() {
        List<AppNotRespondingDialog> list = this.mAnrDialogs;
        if (list == null) {
            return;
        }
        scheduleForAllDialogs(list, new ErrorDialogController$$ExternalSyntheticLambda4());
        this.mAnrDialogs = null;
        this.mAnrController = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void clearViolationDialogs() {
        List<StrictModeViolationDialog> list = this.mViolationDialogs;
        if (list == null) {
            return;
        }
        scheduleForAllDialogs(list, new ErrorDialogController$$ExternalSyntheticLambda4());
        this.mViolationDialogs = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void clearWaitingDialog() {
        final AppWaitingForDebuggerDialog appWaitingForDebuggerDialog = this.mWaitDialog;
        if (appWaitingForDebuggerDialog == null) {
            return;
        }
        Handler handler = this.mService.mUiHandler;
        Objects.requireNonNull(appWaitingForDebuggerDialog);
        handler.post(new Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BaseErrorDialog.this.dismiss();
            }
        });
        this.mWaitDialog = null;
    }

    @GuardedBy({"mProcLock"})
    void scheduleForAllDialogs(final List<? extends BaseErrorDialog> list, final Consumer<BaseErrorDialog> consumer) {
        this.mService.mUiHandler.post(new Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ErrorDialogController.this.lambda$scheduleForAllDialogs$0(list, consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleForAllDialogs$0(List list, Consumer consumer) {
        if (list != null) {
            forAllDialogs(list, consumer);
        }
    }

    void forAllDialogs(List<? extends BaseErrorDialog> list, Consumer<BaseErrorDialog> consumer) {
        for (int size = list.size() - 1; size >= 0; size--) {
            consumer.accept(list.get(size));
        }
    }

    @GuardedBy({"mProcLock"})
    void showCrashDialogs(AppErrorDialog.Data data) {
        List<Context> displayContexts = getDisplayContexts(false);
        this.mCrashDialogs = new ArrayList();
        for (int size = displayContexts.size() - 1; size >= 0; size--) {
            this.mCrashDialogs.add(new AppErrorDialog(displayContexts.get(size), this.mService, data));
        }
        this.mService.mUiHandler.post(new Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ErrorDialogController.this.lambda$showCrashDialogs$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCrashDialogs$1() {
        List<AppErrorDialog> list;
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                list = this.mCrashDialogs;
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        if (list != null) {
            forAllDialogs(list, new ErrorDialogController$$ExternalSyntheticLambda2());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void showAnrDialogs(AppNotRespondingDialog.Data data) {
        List<Context> displayContexts = getDisplayContexts(this.mApp.mErrorState.isSilentAnr());
        this.mAnrDialogs = new ArrayList();
        for (int size = displayContexts.size() - 1; size >= 0; size--) {
            this.mAnrDialogs.add(new AppNotRespondingDialog(this.mService, displayContexts.get(size), data));
        }
        scheduleForAllDialogs(this.mAnrDialogs, new ErrorDialogController$$ExternalSyntheticLambda2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void showViolationDialogs(AppErrorResult appErrorResult) {
        List<Context> displayContexts = getDisplayContexts(false);
        this.mViolationDialogs = new ArrayList();
        for (int size = displayContexts.size() - 1; size >= 0; size--) {
            this.mViolationDialogs.add(new StrictModeViolationDialog(displayContexts.get(size), this.mService, appErrorResult, this.mApp));
        }
        scheduleForAllDialogs(this.mViolationDialogs, new ErrorDialogController$$ExternalSyntheticLambda2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void showDebugWaitingDialogs() {
        this.mWaitDialog = new AppWaitingForDebuggerDialog(this.mService, getDisplayContexts(true).get(0), this.mApp);
        this.mService.mUiHandler.post(new Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ErrorDialogController.this.lambda$showDebugWaitingDialogs$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDebugWaitingDialogs$2() {
        AppWaitingForDebuggerDialog appWaitingForDebuggerDialog;
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                appWaitingForDebuggerDialog = this.mWaitDialog;
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        if (appWaitingForDebuggerDialog != null) {
            appWaitingForDebuggerDialog.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void showAnrErrorDialogs(int i) {
        List<Context> displayContexts = getDisplayContexts(this.mApp.mErrorState.isSilentAnr());
        ProcessRecord processRecord = this.mApp;
        processRecord.mErrorState.mProcessErrorStateRecordExt.showAnrErrorDialogs(this.mService, displayContexts, processRecord, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void showAnrProgressDialogs() {
        List<Context> displayContexts = getDisplayContexts(this.mApp.mErrorState.isSilentAnr());
        ProcessRecord processRecord = this.mApp;
        processRecord.mErrorState.mProcessErrorStateRecordExt.showAnrErrorProgressDialogs(this.mService, displayContexts, processRecord);
    }

    @GuardedBy({"mProcLock"})
    void clearAnrErrorDialogs() {
        ProcessRecord processRecord = this.mApp;
        processRecord.mErrorState.mProcessErrorStateRecordExt.clearAnrErrorDialogs(this.mService, processRecord);
    }

    @GuardedBy({"mProcLock"})
    void clearAnrErrorProgressDialogs() {
        ProcessRecord processRecord = this.mApp;
        processRecord.mErrorState.mProcessErrorStateRecordExt.clearAnrErrorProgressDialogs(this.mService, processRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public AnrController getAnrController() {
        return this.mAnrController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mProcLock"})
    public void setAnrController(AnrController anrController) {
        this.mAnrController = anrController;
    }

    private List<Context> getDisplayContexts(boolean z) {
        Context context;
        ArrayList arrayList = new ArrayList();
        if (!z) {
            this.mApp.getWindowProcessController().getDisplayContextsWithErrorDialogs(arrayList);
        }
        if (arrayList.isEmpty() || z) {
            ActivityManagerService activityManagerService = this.mService;
            WindowManagerInternal windowManagerInternal = activityManagerService.mWmInternal;
            if (windowManagerInternal != null) {
                context = windowManagerInternal.getTopFocusedDisplayUiContext();
            } else {
                context = activityManagerService.mUiContext;
            }
            arrayList.add(context);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ErrorDialogController(ProcessRecord processRecord) {
        this.mApp = processRecord;
        ActivityManagerService activityManagerService = processRecord.mService;
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
    }
}
