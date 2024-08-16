package com.oplus.deepsleep;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import b6.LocalLog;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class DeepSleepRecord {
    private static final int DEFAULT_BUFFER_SIZE = 512;
    private static final long DELAY_RCD_FILE_SIZE_CHECK = 10800000;
    private static final int FILE_WRITE_WAST_WARNING_TIME = 10;
    private static final long FIRST_DELAY_RCD_FILE_SIZE_CHECK = 60000;
    private static final long MAX_FILE_SIZE = 5242880;
    private static final int MSG_RCD_FILE_SIZE_CHECK = 100;
    private static final String RCD_FILE_NAME = "deepsleepRcd.txt";
    private static final String TAG = "DeepSleepRecord";
    private WorkHandler mHandler;
    private boolean mNeedOverwite = false;
    private String mRcdFilePath = "/data/oplus/os/battery";

    /* loaded from: classes.dex */
    private class WorkHandler extends Handler {
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 100) {
                return;
            }
            DeepSleepRecord.this.rcdFileSizeCheck();
            DeepSleepRecord.this.mHandler.sendEmptyMessageDelayed(100, DeepSleepRecord.DELAY_RCD_FILE_SIZE_CHECK);
        }

        private WorkHandler(Looper looper) {
            super(looper);
        }
    }

    public DeepSleepRecord(Context context, Looper looper) {
        WorkHandler workHandler = new WorkHandler(looper);
        this.mHandler = workHandler;
        workHandler.sendEmptyMessageDelayed(100, FIRST_DELAY_RCD_FILE_SIZE_CHECK);
        LocalLog.a(TAG, "DeepSleepRecord: RcdFilePath=" + this.mRcdFilePath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rcdFileSizeCheck() {
        File file = new File(this.mRcdFilePath, RCD_FILE_NAME);
        if (file.exists()) {
            long length = file.length();
            if (length > MAX_FILE_SIZE) {
                this.mNeedOverwite = true;
                LocalLog.l(TAG, "rcdFileSizeCheck: file size exceed limit. filesize=" + length);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00c6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String getRcdEvents() {
        Throwable th;
        FileInputStream fileInputStream;
        NullPointerException e10;
        IndexOutOfBoundsException e11;
        IOException e12;
        StringBuilder sb2;
        File file = new File(this.mRcdFilePath, RCD_FILE_NAME);
        ?? exists = file.exists();
        if (exists == 0) {
            LocalLog.a(TAG, "getRcdEvents: file not exist " + this.mRcdFilePath + "/" + RCD_FILE_NAME);
            return "noFile";
        }
        try {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (IOException e13) {
                fileInputStream = null;
                e12 = e13;
            } catch (IndexOutOfBoundsException e14) {
                fileInputStream = null;
                e11 = e14;
            } catch (NullPointerException e15) {
                fileInputStream = null;
                e10 = e15;
            } catch (Throwable th2) {
                exists = 0;
                th = th2;
                if (exists != 0) {
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
                } catch (IOException e16) {
                    LocalLog.b(TAG, "Fail to close inputStream e=" + e16);
                }
                return byteArrayOutputStream2;
            } catch (IOException e17) {
                e12 = e17;
                e12.printStackTrace();
                if (fileInputStream == null) {
                    return "readFail";
                }
                try {
                    fileInputStream.close();
                    return "readFail";
                } catch (IOException e18) {
                    e = e18;
                    sb2 = new StringBuilder();
                    sb2.append("Fail to close inputStream e=");
                    sb2.append(e);
                    LocalLog.b(TAG, sb2.toString());
                    return "readFail";
                }
            } catch (IndexOutOfBoundsException e19) {
                e11 = e19;
                e11.printStackTrace();
                if (fileInputStream == null) {
                    return "readFail";
                }
                try {
                    fileInputStream.close();
                    return "readFail";
                } catch (IOException e20) {
                    e = e20;
                    sb2 = new StringBuilder();
                    sb2.append("Fail to close inputStream e=");
                    sb2.append(e);
                    LocalLog.b(TAG, sb2.toString());
                    return "readFail";
                }
            } catch (NullPointerException e21) {
                e10 = e21;
                e10.printStackTrace();
                if (fileInputStream == null) {
                    return "readFail";
                }
                try {
                    fileInputStream.close();
                    return "readFail";
                } catch (IOException e22) {
                    e = e22;
                    sb2 = new StringBuilder();
                    sb2.append("Fail to close inputStream e=");
                    sb2.append(e);
                    LocalLog.b(TAG, sb2.toString());
                    return "readFail";
                }
            }
        } catch (Throwable th3) {
            th = th3;
            if (exists != 0) {
                try {
                    exists.close();
                } catch (IOException e23) {
                    LocalLog.b(TAG, "Fail to close inputStream e=" + e23);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void recoardEvent(String str, long j10, boolean z10) {
        StringBuilder sb2;
        long elapsedRealtime;
        StringBuilder sb3;
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        if (this.mNeedOverwite) {
            this.mNeedOverwite = false;
            z10 = false;
        }
        LocalLog.a(TAG, "recoardEvent: append=" + z10 + ", event=" + str);
        if (!new File(this.mRcdFilePath).exists()) {
            LocalLog.a(TAG, "recoardEvent: filePath not exist. " + this.mRcdFilePath);
            return;
        }
        File file = new File(this.mRcdFilePath, RCD_FILE_NAME);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    LocalLog.a(TAG, "recoardEvent: failed create file " + this.mRcdFilePath + "/" + RCD_FILE_NAME);
                }
            } catch (IOException e10) {
                LocalLog.a(TAG, "failed create file " + e10);
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        Date time = calendar.getTime();
        BufferedWriter bufferedWriter = null;
        BufferedWriter bufferedWriter2 = null;
        try {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, z10), StandardCharsets.UTF_8.name());
                BufferedWriter bufferedWriter3 = new BufferedWriter(outputStreamWriter);
                try {
                    sb3 = new StringBuilder();
                    sb3.append(time.toString());
                    sb3.append(" : ");
                    sb3.append(str);
                    sb3.append('\n');
                    bufferedWriter3.append((CharSequence) sb3.toString());
                    bufferedWriter3.flush();
                    outputStreamWriter.close();
                } catch (IOException e11) {
                    e = e11;
                    bufferedWriter2 = bufferedWriter3;
                    LocalLog.b(TAG, "recoardEvent: Fail to writefile e=" + e);
                    bufferedWriter = bufferedWriter2;
                    if (bufferedWriter2 != null) {
                        try {
                            bufferedWriter2.close();
                            bufferedWriter = bufferedWriter2;
                        } catch (IOException e12) {
                            e = e12;
                            sb2 = new StringBuilder();
                            sb2.append("recoardEvent :Fail to close writer e=");
                            sb2.append(e);
                            LocalLog.b(TAG, sb2.toString());
                            elapsedRealtime = SystemClock.elapsedRealtime() - elapsedRealtime2;
                            if (elapsedRealtime <= 10) {
                            }
                        }
                    }
                    elapsedRealtime = SystemClock.elapsedRealtime() - elapsedRealtime2;
                    if (elapsedRealtime <= 10) {
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedWriter = bufferedWriter3;
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e13) {
                            LocalLog.b(TAG, "recoardEvent :Fail to close writer e=" + e13);
                        }
                    }
                    throw th;
                }
                try {
                    bufferedWriter3.close();
                    bufferedWriter = sb3;
                } catch (IOException e14) {
                    e = e14;
                    sb2 = new StringBuilder();
                    sb2.append("recoardEvent :Fail to close writer e=");
                    sb2.append(e);
                    LocalLog.b(TAG, sb2.toString());
                    elapsedRealtime = SystemClock.elapsedRealtime() - elapsedRealtime2;
                    if (elapsedRealtime <= 10) {
                    }
                }
            } catch (IOException e15) {
                e = e15;
            }
            elapsedRealtime = SystemClock.elapsedRealtime() - elapsedRealtime2;
            if (elapsedRealtime <= 10) {
                LocalLog.a(TAG, "recoardEvent :exit. cost " + elapsedRealtime + " ms");
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
