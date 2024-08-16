package com.android.server.am;

import android.R;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.BidiFormatter;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.bluetooth.BluetoothStatsLog;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppNotRespondingDialog extends BaseErrorDialog implements View.OnClickListener {
    public static final int ALREADY_SHOWING = -2;
    public static final int CANT_SHOW = -1;
    static final int FORCE_CLOSE = 1;
    private static final String TAG = "AppNotRespondingDialog";
    static final int WAIT = 2;
    static final int WAIT_AND_REPORT = 3;
    private final Data mData;
    private final Handler mHandler;
    private final ProcessRecord mProc;
    private final ActivityManagerService mService;

    public AppNotRespondingDialog(ActivityManagerService activityManagerService, Context context, Data data) {
        super(context);
        int i;
        String string;
        this.mHandler = new Handler() { // from class: com.android.server.am.AppNotRespondingDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                MetricsLogger.action(AppNotRespondingDialog.this.getContext(), FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_SESSION_CALLBACK, message.what);
                int i2 = message.what;
                if (i2 == 1) {
                    AppNotRespondingDialog.this.mProc.mErrorState.mProcessErrorStateRecordExt.notifyTheiaAnrFinished(AppNotRespondingDialog.this.mProc.mPid, AppNotRespondingDialog.this.mProc.uid, AppNotRespondingDialog.this.mProc.processName, "closeUser");
                    if (!AppNotRespondingDialog.this.mProc.mErrorState.mProcessErrorStateRecordExt.isTheiaAnrTestApp(AppNotRespondingDialog.this.mProc.processName)) {
                        AppNotRespondingDialog.this.mService.killAppAtUsersRequest(AppNotRespondingDialog.this.mProc);
                    }
                } else if (i2 == 2 || i2 == 3) {
                    ActivityManagerService activityManagerService2 = AppNotRespondingDialog.this.mService;
                    ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService2) {
                        try {
                            ProcessRecord processRecord = AppNotRespondingDialog.this.mProc;
                            ProcessErrorStateRecord processErrorStateRecord = processRecord.mErrorState;
                            r2 = message.what == 3 ? AppNotRespondingDialog.this.mService.mAppErrors.createAppErrorIntentLOSP(processRecord, System.currentTimeMillis(), null) : null;
                            ActivityManagerGlobalLock activityManagerGlobalLock = AppNotRespondingDialog.this.mService.mProcLock;
                            ActivityManagerService.boostPriorityForProcLockedSection();
                            synchronized (activityManagerGlobalLock) {
                                try {
                                    processErrorStateRecord.setNotResponding(false);
                                    processErrorStateRecord.getDialogController().clearAnrDialogs();
                                } catch (Throwable th) {
                                    ActivityManagerService.resetPriorityAfterProcLockedSection();
                                    throw th;
                                }
                            }
                            ActivityManagerService.resetPriorityAfterProcLockedSection();
                            AppNotRespondingDialog.this.mService.mServices.scheduleServiceTimeoutLocked(processRecord);
                            if (AppNotRespondingDialog.this.mData.isContinuousAnr) {
                                AppNotRespondingDialog.this.mService.mInternal.rescheduleAnrDialog(AppNotRespondingDialog.this.mData);
                            }
                        } catch (Throwable th2) {
                            ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th2;
                        }
                    }
                    ActivityManagerService.resetPriorityAfterLockedSection();
                }
                if (r2 != null) {
                    try {
                        AppNotRespondingDialog.this.getContext().startActivity(r2);
                    } catch (ActivityNotFoundException e) {
                        Slog.w(AppNotRespondingDialog.TAG, "bug report receiver dissappeared", e);
                    }
                }
                AppNotRespondingDialog.this.dismiss();
            }
        };
        this.mService = activityManagerService;
        ProcessRecord processRecord = data.proc;
        this.mProc = processRecord;
        this.mData = data;
        Resources resources = context.getResources();
        setCancelable(false);
        ApplicationInfo applicationInfo = data.aInfo;
        CharSequence charSequence = null;
        CharSequence loadLabel = applicationInfo != null ? applicationInfo.loadLabel(context.getPackageManager()) : null;
        if (processRecord.getPkgList().size() != 1 || (charSequence = context.getPackageManager().getApplicationLabel(processRecord.info)) == null) {
            if (loadLabel != null) {
                charSequence = processRecord.processName;
                i = R.string.autofill_city_re;
            } else {
                loadLabel = processRecord.processName;
                i = R.string.autofill_continue_yes;
            }
        } else if (loadLabel != null) {
            i = R.string.autofill_card_number_re;
        } else {
            charSequence = processRecord.processName;
            i = 17039683;
            loadLabel = charSequence;
        }
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        if (charSequence != null) {
            string = resources.getString(i, bidiFormatter.unicodeWrap(loadLabel.toString()), bidiFormatter.unicodeWrap(charSequence.toString()));
        } else {
            string = resources.getString(i, bidiFormatter.unicodeWrap(loadLabel.toString()));
        }
        setTitle(string);
        if (data.aboveSystem) {
            getWindow().setType(2010);
        }
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.setTitle("Application Not Responding: " + processRecord.info.processName);
        attributes.privateFlags = BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_FAIL_REASON__PAIRING_FAIL_REASON_UNKNOWN_IO_CAP;
        getWindow().setAttributes(attributes);
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LayoutInflater.from(getContext()).inflate(R.layout.app_error_dialog, (ViewGroup) findViewById(R.id.custom), true);
        TextView textView = (TextView) findViewById(R.id.app_name_text);
        textView.setOnClickListener(this);
        textView.setVisibility(this.mProc.mErrorState.getErrorReportReceiver() != null ? 0 : 8);
        ((TextView) findViewById(R.id.appPredictor)).setOnClickListener(this);
        ((TextView) findViewById(R.id.appop)).setOnClickListener(this);
        findViewById(R.id.eight).setVisibility(0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == 16908759) {
            this.mHandler.obtainMessage(1).sendToTarget();
        } else if (id == 16908761) {
            this.mHandler.obtainMessage(3).sendToTarget();
        } else {
            if (id != 16908763) {
                return;
            }
            this.mHandler.obtainMessage(2).sendToTarget();
        }
    }

    @Override // com.android.server.am.BaseErrorDialog
    protected void closeDialog() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Data {
        final ApplicationInfo aInfo;
        final boolean aboveSystem;
        final boolean isContinuousAnr;
        final ProcessRecord proc;

        public Data(ProcessRecord processRecord, ApplicationInfo applicationInfo, boolean z, boolean z2) {
            this.proc = processRecord;
            this.aInfo = applicationInfo;
            this.aboveSystem = z;
            this.isContinuousAnr = z2;
        }
    }
}
