package com.android.server;

import android.os.SystemProperties;
import com.mediatek.aee.ExceptionLog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class WatchdogSocExtImpl implements IWatchdogSocExt {
    private static final String TAG = "WatchdogSoxExtImpl";
    private ExceptionLog exceptionHang;

    public WatchdogSocExtImpl(Object obj) {
    }

    private long getSfStatus() {
        ExceptionLog exceptionLog = this.exceptionHang;
        if (exceptionLog != null) {
            return exceptionLog.SFMatterJava(0L, 0L);
        }
        return 0L;
    }

    private static int getSfReboot() {
        return SystemProperties.getInt("service.sf.reboot", 0);
    }

    private void setSfReboot() {
        int i = SystemProperties.getInt("service.sf.reboot", 0) + 1;
        if (i > 9) {
            i = 9;
        }
        SystemProperties.set("service.sf.reboot", String.valueOf(i));
    }

    @Override // com.android.server.IWatchdogSocExt
    public void getExceptionLog() {
        this.exceptionHang = ExceptionLog.getInstance();
    }

    @Override // com.android.server.IWatchdogSocExt
    public void WDTMatterJava(long j) {
        ExceptionLog exceptionLog = this.exceptionHang;
        if (exceptionLog != null) {
            exceptionLog.WDTMatterJava(j);
        }
    }

    @Override // com.android.server.IWatchdogSocExt
    public void switchFtrace(int i) {
        ExceptionLog exceptionLog = this.exceptionHang;
        if (exceptionLog != null) {
            exceptionLog.switchFtrace(i);
        }
    }

    @Override // com.android.server.IWatchdogSocExt
    public long getSfHangTime() {
        return getSfStatus();
    }

    @Override // com.android.server.IWatchdogSocExt
    public int getSfRebootTime() {
        return getSfReboot();
    }

    @Override // com.android.server.IWatchdogSocExt
    public void setSfRebootTime() {
        setSfReboot();
    }
}
