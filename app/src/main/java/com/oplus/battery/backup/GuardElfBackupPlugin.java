package com.oplus.battery.backup;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import b6.LocalLog;
import com.oplus.backup.sdk.common.host.BREngineConfig;
import com.oplus.backup.sdk.common.utils.ModuleType;
import com.oplus.backup.sdk.compat.DataSizeUtils;
import com.oplus.backup.sdk.component.BRPluginHandler;
import com.oplus.backup.sdk.component.plugin.BackupPlugin;
import com.oplus.backup.sdk.host.listener.ProgressHelper;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import z5.LocalFileUtil;

/* loaded from: classes.dex */
public class GuardElfBackupPlugin extends BackupPlugin {
    private static final int MAX_COUNT = 1;
    private static final String TAG = "GuardElfBackupPlugin";
    private static final String PARENT_FOLDER = "Setting";
    private static final String BACKUP_FOLDER = PARENT_FOLDER + File.separator + "Battery";
    private Context mContext = null;
    private BREngineConfig mBackupConfig = null;
    private boolean mIsCancel = false;
    private boolean mIsPause = false;
    private int mCompleteCount = 0;
    private Object mPauseLock = new Object();

    private void backupFile(String str) {
        StringBuilder sb2;
        BufferedWriter bufferedWriter;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mBackupConfig.getBackupRootPath());
        String str2 = File.separator;
        sb3.append(str2);
        sb3.append(BACKUP_FOLDER);
        sb3.append(str2);
        FileDescriptor fileDescriptor = getFileDescriptor(sb3.toString() + str);
        String fileContent = getFileContent(str);
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileDescriptor), StandardCharsets.UTF_8.name());
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                try {
                    bufferedWriter.write(fileContent);
                    bufferedWriter.flush();
                    outputStreamWriter.close();
                } catch (IOException e10) {
                    e = e10;
                    bufferedWriter2 = bufferedWriter;
                    LocalLog.b(TAG, "Fail to backupFile e=" + e);
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e11) {
                            e = e11;
                            sb2 = new StringBuilder();
                            sb2.append("Fail to close writer e=");
                            sb2.append(e);
                            LocalLog.b(TAG, sb2.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedWriter2 = bufferedWriter;
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                        } catch (IOException e12) {
                            LocalLog.b(TAG, "Fail to close writer e=" + e12);
                        }
                    }
                    throw th;
                }
            } catch (IOException e13) {
                e = e13;
            }
            try {
                bufferedWriter.close();
            } catch (IOException e14) {
                e = e14;
                sb2 = new StringBuilder();
                sb2.append("Fail to close writer e=");
                sb2.append(e);
                LocalLog.b(TAG, sb2.toString());
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private String getFileContent(String str) {
        StringBuilder sb2;
        InputStream a10 = LocalFileUtil.a(("battery" + File.separator) + str, this.mContext);
        String str2 = "";
        if (a10 == null) {
            return "";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                try {
                    int read = a10.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                } catch (IOException e10) {
                    LocalLog.b(TAG, "getFileContent ERROR e=" + e10);
                    try {
                        a10.close();
                    } catch (IOException e11) {
                        LocalLog.b(TAG, "Fail to close inputStream e=" + e11);
                    }
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e12) {
                        e = e12;
                        sb2 = new StringBuilder();
                        sb2.append("Fail to close outputStream e=");
                        sb2.append(e);
                        LocalLog.b(TAG, sb2.toString());
                        return str2;
                    }
                }
            } catch (Throwable th) {
                try {
                    a10.close();
                } catch (IOException e13) {
                    LocalLog.b(TAG, "Fail to close inputStream e=" + e13);
                }
                try {
                    byteArrayOutputStream.close();
                    throw th;
                } catch (IOException e14) {
                    LocalLog.b(TAG, "Fail to close outputStream e=" + e14);
                    throw th;
                }
            }
        }
        str2 = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        try {
            a10.close();
        } catch (IOException e15) {
            LocalLog.b(TAG, "Fail to close inputStream e=" + e15);
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e16) {
            e = e16;
            sb2 = new StringBuilder();
            sb2.append("Fail to close outputStream e=");
            sb2.append(e);
            LocalLog.b(TAG, sb2.toString());
            return str2;
        }
        return str2;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onBackup(Bundle bundle) {
        if (!this.mIsCancel) {
            synchronized (this.mPauseLock) {
                while (this.mIsPause) {
                    try {
                        Log.d(TAG, "on pause wait lock here");
                        this.mPauseLock.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
            backupFile("allow_prohibit_app.xml");
            backupFile("switch_status_config.xml");
            backupFile("power_consum_opt_status.xml");
            this.mCompleteCount++;
            Bundle bundle2 = new Bundle();
            ProgressHelper.putMaxCount(bundle2, 1);
            ProgressHelper.putCompletedCount(bundle2, this.mCompleteCount);
            getPluginHandler().updateProgress(bundle2);
        }
        Log.d(TAG, "onBackup");
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onCancel(Bundle bundle) {
        this.mIsCancel = true;
        this.mIsPause = false;
        synchronized (this.mPauseLock) {
            this.mPauseLock.notifyAll();
            Log.d(TAG, "onCancel mLock.notifyAll()");
        }
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onContinue(Bundle bundle) {
        this.mIsPause = false;
        synchronized (this.mPauseLock) {
            this.mPauseLock.notifyAll();
            Log.d(TAG, "onContinue mPauseLock.notifyAll()");
        }
    }

    @Override // com.oplus.backup.sdk.component.plugin.AbstractPlugin
    public void onCreate(Context context, BRPluginHandler bRPluginHandler, BREngineConfig bREngineConfig) {
        super.onCreate(context, bRPluginHandler, bREngineConfig);
        this.mContext = context;
        this.mBackupConfig = bREngineConfig;
        Log.d(TAG, "onCreate");
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public Bundle onDestroy(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        ProgressHelper.putBRResult(bundle2, this.mIsCancel ? 3 : 1);
        ProgressHelper.putMaxCount(bundle2, 1);
        ProgressHelper.putCompletedCount(bundle2, this.mCompleteCount);
        Log.d(TAG, "onDestroy:" + bundle2);
        return bundle2;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onPause(Bundle bundle) {
        this.mIsPause = true;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public Bundle onPrepare(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        bundle2.putInt(ProgressHelper.MAX_COUNT, 1);
        Log.d(TAG, "onPrepare:" + bundle2);
        return bundle2;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public Bundle onPreview(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        long estimateSize = DataSizeUtils.estimateSize(ModuleType.TYPE_ACCOUNT, 1);
        ProgressHelper.putMaxCount(bundle2, 1);
        ProgressHelper.putPreviewDataSize(bundle2, estimateSize);
        Log.d(TAG, "onPreview:" + bundle2);
        return bundle2;
    }
}
