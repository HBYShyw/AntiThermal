package com.android.server.am;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class StrictModeViolationDialog extends BaseErrorDialog {
    static final int ACTION_OK = 0;
    static final int ACTION_OK_AND_REPORT = 1;
    static final long DISMISS_TIMEOUT = 60000;
    private static final String TAG = "StrictModeViolationDialog";
    private final Handler mHandler;
    private final ProcessRecord mProc;
    private final AppErrorResult mResult;
    private final ActivityManagerService mService;

    public StrictModeViolationDialog(Context context, ActivityManagerService activityManagerService, AppErrorResult appErrorResult, ProcessRecord processRecord) {
        super(context);
        CharSequence applicationLabel;
        Handler handler = new Handler() { // from class: com.android.server.am.StrictModeViolationDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                ActivityManagerGlobalLock activityManagerGlobalLock = StrictModeViolationDialog.this.mService.mProcLock;
                ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        if (StrictModeViolationDialog.this.mProc != null) {
                            StrictModeViolationDialog.this.mProc.mErrorState.getDialogController().clearViolationDialogs();
                        }
                    } catch (Throwable th) {
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                StrictModeViolationDialog.this.mResult.set(message.what);
                StrictModeViolationDialog.this.dismiss();
            }
        };
        this.mHandler = handler;
        Resources resources = context.getResources();
        this.mService = activityManagerService;
        this.mProc = processRecord;
        this.mResult = appErrorResult;
        if (processRecord.getPkgList().size() == 1 && (applicationLabel = context.getPackageManager().getApplicationLabel(processRecord.info)) != null) {
            setMessage(resources.getString(17041663, applicationLabel.toString(), processRecord.info.processName));
        } else {
            setMessage(resources.getString(17041664, processRecord.processName.toString()));
        }
        setCancelable(false);
        setButton(-1, resources.getText(R.string.fingerprint_acquired_insufficient), handler.obtainMessage(0));
        if (processRecord.mErrorState.getErrorReportReceiver() != null) {
            setButton(-2, resources.getText(R.string.years), handler.obtainMessage(1));
        }
        getWindow().addPrivateFlags(256);
        getWindow().setTitle("Strict Mode Violation: " + processRecord.info.processName);
        handler.sendMessageDelayed(handler.obtainMessage(0), 60000L);
    }

    @Override // com.android.server.am.BaseErrorDialog
    protected void closeDialog() {
        this.mHandler.obtainMessage(0).sendToTarget();
    }
}
