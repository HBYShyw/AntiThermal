package com.android.server.am;

import android.R;
import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.BidiFormatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.bluetooth.BluetoothStatsLog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppErrorDialog extends BaseErrorDialog implements View.OnClickListener {
    static int ALREADY_SHOWING = -3;
    static final int APP_INFO = 8;
    static int BACKGROUND_USER = -2;
    static final int CANCEL = 7;
    static int CANT_SHOW = -1;
    static final long DISMISS_TIMEOUT = 300000;
    static final int FORCE_QUIT = 1;
    static final int FORCE_QUIT_AND_REPORT = 2;
    static final int MUTE = 5;
    static final int RESTART = 3;
    static final int TIMEOUT = 6;
    private final Handler mHandler;
    private final boolean mIsRestartable;
    private final ProcessRecord mProc;
    private final ActivityManagerGlobalLock mProcLock;
    private final AppErrorResult mResult;
    private final ActivityManagerService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Data {
        ApplicationErrorReport.CrashInfo crashInfo;
        boolean isForeground;
        boolean isRestartableForService;
        ProcessRecord proc;
        boolean repeating;
        AppErrorResult result;
        int taskId;
        long vmSize;
    }

    public AppErrorDialog(Context context, ActivityManagerService activityManagerService, Data data) {
        super(context);
        CharSequence applicationLabel;
        Handler handler = new Handler() { // from class: com.android.server.am.AppErrorDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                AppErrorDialog.this.setResult(message.what);
                AppErrorDialog.this.dismiss();
            }
        };
        this.mHandler = handler;
        Resources resources = context.getResources();
        this.mService = activityManagerService;
        this.mProcLock = activityManagerService.mProcLock;
        ProcessRecord processRecord = data.proc;
        this.mProc = processRecord;
        this.mResult = data.result;
        boolean z = false;
        if ((data.taskId != -1 || data.isRestartableForService) && Settings.Global.getInt(context.getContentResolver(), "show_restart_in_crash_dialog", 0) != 0) {
            z = true;
        }
        this.mIsRestartable = z;
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        if (processRecord.getPkgList().size() == 1 && (applicationLabel = context.getPackageManager().getApplicationLabel(processRecord.info)) != null) {
            setTitle(resources.getString(data.repeating ? R.string.app_not_found : R.string.app_info, bidiFormatter.unicodeWrap(applicationLabel.toString()), bidiFormatter.unicodeWrap(processRecord.info.processName)));
        } else {
            setTitle(resources.getString(data.repeating ? R.string.app_suspended_title : R.string.app_suspended_more_details, bidiFormatter.unicodeWrap(processRecord.processName.toString())));
        }
        setCancelable(true);
        setCancelMessage(handler.obtainMessage(7));
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.setTitle("Application Error: " + processRecord.info.processName);
        attributes.privateFlags = attributes.privateFlags | BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_FAIL_REASON__PAIRING_FAIL_REASON_UNKNOWN_IO_CAP;
        getWindow().setAttributes(attributes);
        if (processRecord.isPersistent()) {
            getWindow().setType(2010);
        }
        handler.sendMessageDelayed(handler.obtainMessage(6), 300000L);
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.custom);
        Context context = getContext();
        LayoutInflater.from(context).inflate(R.layout.app_not_authorized, (ViewGroup) frameLayout, true);
        boolean z = this.mProc.mErrorState.getErrorReportReceiver() != null;
        TextView textView = (TextView) findViewById(R.id.app_ops);
        textView.setOnClickListener(this);
        textView.setVisibility(this.mIsRestartable ? 0 : 8);
        TextView textView2 = (TextView) findViewById(R.id.app_name_text);
        textView2.setOnClickListener(this);
        textView2.setVisibility(z ? 0 : 8);
        ((TextView) findViewById(R.id.appPredictor)).setOnClickListener(this);
        ((TextView) findViewById(R.id.anyRtl)).setOnClickListener(this);
        boolean z2 = (Build.IS_USER || Settings.Global.getInt(context.getContentResolver(), "development_settings_enabled", 0) == 0 || Settings.Global.getInt(context.getContentResolver(), "show_mute_in_crash_dialog", 0) == 0) ? false : true;
        TextView textView3 = (TextView) findViewById(R.id.app_name_divider);
        textView3.setOnClickListener(this);
        textView3.setVisibility(z2 ? 0 : 8);
        findViewById(R.id.eight).setVisibility(0);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (!this.mResult.mHasResult) {
            setResult(1);
        }
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResult(int i) {
        ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                ProcessRecord processRecord = this.mProc;
                if (processRecord != null) {
                    processRecord.mErrorState.getDialogController().clearCrashDialogs(false);
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterProcLockedSection();
        this.mResult.set(i);
        this.mHandler.removeMessages(6);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.anyRtl:
                this.mHandler.obtainMessage(8).sendToTarget();
                return;
            case R.id.appPredictor:
                this.mHandler.obtainMessage(1).sendToTarget();
                return;
            case R.id.app_name_divider:
                this.mHandler.obtainMessage(5).sendToTarget();
                return;
            case R.id.app_name_text:
                this.mHandler.obtainMessage(2).sendToTarget();
                return;
            case R.id.app_ops:
                this.mHandler.obtainMessage(3).sendToTarget();
                return;
            default:
                return;
        }
    }
}
