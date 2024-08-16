package com.oplus.battery.backup;

import android.content.Context;
import android.os.Bundle;
import b6.LocalLog;
import com.oplus.backup.sdk.common.host.BREngineConfig;
import com.oplus.backup.sdk.component.BRPluginHandler;
import com.oplus.backup.sdk.component.plugin.RestorePlugin;
import com.oplus.backup.sdk.host.listener.ProgressHelper;
import f6.f;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import z5.LocalFileUtil;

/* loaded from: classes.dex */
public class GuardElfRestorePlugin extends RestorePlugin {
    private static final int DEFAULT_BUFFER_SIZE = 512;
    private static final int MAX_COUNT = 1;
    private static final String PARENT_FOLDER = "Setting";
    private static final String RESTORE_FOLDER = PARENT_FOLDER + File.separator + "Battery";
    private static final String TAG = "GuardElfRestorePlugin";
    private BRPluginHandler mBRPluginHandler = null;
    private BREngineConfig mRestoreConfig = null;
    private Context mContext = null;
    private boolean mIsCancel = false;
    private boolean mIsPause = false;
    private String mBackContent = null;
    private String mSwitchContent = null;
    private String mPcoContent = null;
    private int mCompleteCount = 0;
    private Object mPauseLock = new Object();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00c4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getFileContent(String str) {
        FileInputStream fileInputStream;
        StringBuilder sb2;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(this.mRestoreConfig.getRestoreRootPath());
        String str2 = File.separator;
        sb3.append(str2);
        sb3.append(RESTORE_FOLDER);
        sb3.append(str2);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append(str);
        ?? r22 = 0;
        try {
            try {
                fileInputStream = new FileInputStream(getFileDescriptor(sb5.toString(), 268435456));
            } catch (IOException e10) {
                e = e10;
                fileInputStream = null;
            } catch (IndexOutOfBoundsException e11) {
                e = e11;
                fileInputStream = null;
            } catch (NullPointerException e12) {
                e = e12;
                fileInputStream = null;
            } catch (Throwable th) {
                th = th;
                if (r22 != 0) {
                }
                throw th;
            }
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[512];
                while (true) {
                    int read = fileInputStream.read(bArr, 0, 512);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                String byteArrayOutputStream2 = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
                try {
                    fileInputStream.close();
                } catch (IOException e13) {
                    LocalLog.b(TAG, "Fail to close inputStream e=" + e13);
                }
                return byteArrayOutputStream2;
            } catch (IOException e14) {
                e = e14;
                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e15) {
                        e = e15;
                        sb2 = new StringBuilder();
                        sb2.append("Fail to close inputStream e=");
                        sb2.append(e);
                        LocalLog.b(TAG, sb2.toString());
                        return null;
                    }
                }
                return null;
            } catch (IndexOutOfBoundsException e16) {
                e = e16;
                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e17) {
                        e = e17;
                        sb2 = new StringBuilder();
                        sb2.append("Fail to close inputStream e=");
                        sb2.append(e);
                        LocalLog.b(TAG, sb2.toString());
                        return null;
                    }
                }
                return null;
            } catch (NullPointerException e18) {
                e = e18;
                e.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e19) {
                        e = e19;
                        sb2 = new StringBuilder();
                        sb2.append("Fail to close inputStream e=");
                        sb2.append(e);
                        LocalLog.b(TAG, sb2.toString());
                        return null;
                    }
                }
                return null;
            }
        } catch (Throwable th2) {
            th = th2;
            r22 = sb5;
            if (r22 != 0) {
                try {
                    r22.close();
                } catch (IOException e20) {
                    LocalLog.b(TAG, "Fail to close inputStream e=" + e20);
                }
            }
            throw th;
        }
    }

    private String getFileContentByName(String str) {
        if ("allow_prohibit_app.xml".equals(str)) {
            return this.mBackContent;
        }
        if ("switch_status_config.xml".equals(str)) {
            return this.mSwitchContent;
        }
        if ("power_consum_opt_status.xml".equals(str)) {
            return this.mPcoContent;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void restoreFile(String str) {
        StringBuilder sb2;
        OutputStream e10 = LocalFileUtil.e(("battery" + File.separator) + str, this.mContext);
        String fileContentByName = getFileContentByName(str);
        BufferedWriter bufferedWriter = null;
        bufferedWriter = null;
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(e10, StandardCharsets.UTF_8.name());
                BufferedWriter bufferedWriter3 = new BufferedWriter(outputStreamWriter);
                try {
                    bufferedWriter3.write(fileContentByName);
                    bufferedWriter3.flush();
                    outputStreamWriter.close();
                    try {
                        bufferedWriter3.close();
                    } catch (IOException e11) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Fail to close writer e=");
                        sb3.append(e11);
                        LocalLog.b(TAG, sb3.toString());
                        bufferedWriter = sb3;
                    }
                    if (e10 != null) {
                        try {
                            e10.close();
                        } catch (IOException e12) {
                            e = e12;
                            sb2 = new StringBuilder();
                            sb2.append("Fail to close outputStream e=");
                            sb2.append(e);
                            LocalLog.b(TAG, sb2.toString());
                        }
                    }
                } catch (IOException e13) {
                    e = e13;
                    bufferedWriter2 = bufferedWriter3;
                    LocalLog.b(TAG, "Fail to restoreFile e=" + e);
                    bufferedWriter = bufferedWriter2;
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                            bufferedWriter = bufferedWriter2;
                        } catch (IOException e14) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Fail to close writer e=");
                            sb4.append(e14);
                            LocalLog.b(TAG, sb4.toString());
                            bufferedWriter = sb4;
                        }
                    }
                    if (e10 != null) {
                        try {
                            e10.close();
                        } catch (IOException e15) {
                            e = e15;
                            sb2 = new StringBuilder();
                            sb2.append("Fail to close outputStream e=");
                            sb2.append(e);
                            LocalLog.b(TAG, sb2.toString());
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedWriter = bufferedWriter3;
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e16) {
                            LocalLog.b(TAG, "Fail to close writer e=" + e16);
                        }
                    }
                    if (e10 != null) {
                        try {
                            e10.close();
                            throw th;
                        } catch (IOException e17) {
                            LocalLog.b(TAG, "Fail to close outputStream e=" + e17);
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (IOException e18) {
                e = e18;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onCancel(Bundle bundle) {
        this.mIsCancel = true;
        this.mIsPause = false;
        synchronized (this.mPauseLock) {
            this.mPauseLock.notifyAll();
            LocalLog.a(TAG, "onCancel mLock.notifyAll()");
        }
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onContinue(Bundle bundle) {
        this.mIsPause = false;
        synchronized (this.mPauseLock) {
            this.mPauseLock.notifyAll();
            LocalLog.a(TAG, "onContinue mPauseLock.notifyAll()");
        }
    }

    @Override // com.oplus.backup.sdk.component.plugin.AbstractPlugin
    public void onCreate(Context context, BRPluginHandler bRPluginHandler, BREngineConfig bREngineConfig) {
        super.onCreate(context, bRPluginHandler, bREngineConfig);
        this.mBRPluginHandler = bRPluginHandler;
        this.mRestoreConfig = bREngineConfig;
        this.mContext = context;
        LocalLog.a(TAG, "onCreate");
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public Bundle onDestroy(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        ProgressHelper.putBRResult(bundle2, this.mIsCancel ? 3 : 1);
        ProgressHelper.putMaxCount(bundle2, 1);
        ProgressHelper.putCompletedCount(bundle2, this.mCompleteCount);
        LocalLog.a(TAG, "onDestroy:" + bundle2);
        return bundle2;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onPause(Bundle bundle) {
        this.mIsPause = true;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public Bundle onPrepare(Bundle bundle) {
        this.mBackContent = getFileContent("allow_prohibit_app.xml");
        this.mSwitchContent = getFileContent("switch_status_config.xml");
        this.mPcoContent = getFileContent("power_consum_opt_status.xml");
        Bundle bundle2 = new Bundle();
        bundle2.putInt(ProgressHelper.MAX_COUNT, 1);
        LocalLog.a(TAG, "onPrepare:" + bundle2);
        return bundle2;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public Bundle onPreview(Bundle bundle) {
        return null;
    }

    @Override // com.oplus.backup.sdk.component.plugin.IBRPlugin
    public void onRestore(Bundle bundle) {
        if (!this.mIsCancel) {
            synchronized (this.mPauseLock) {
                while (this.mIsPause) {
                    try {
                        LocalLog.a(TAG, "on pause wait lock here");
                        this.mPauseLock.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
            restoreFile("allow_prohibit_app.xml");
            restoreFile("switch_status_config.xml");
            f.y1(this.mContext);
            restoreFile("power_consum_opt_status.xml");
            this.mCompleteCount++;
            Bundle bundle2 = new Bundle();
            ProgressHelper.putMaxCount(bundle2, 1);
            ProgressHelper.putCompletedCount(bundle2, this.mCompleteCount);
            this.mBRPluginHandler.updateProgress(bundle2);
            LocalLog.a(TAG, "onRestore complete!");
            return;
        }
        LocalLog.a(TAG, "onRestore cancel!");
    }
}
