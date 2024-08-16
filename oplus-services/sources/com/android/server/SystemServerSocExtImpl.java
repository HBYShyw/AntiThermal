package com.android.server;

import android.content.Context;
import com.android.server.utils.TimingsTraceAndSlog;
import com.mediatek.server.MtkSystemServer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class SystemServerSocExtImpl implements ISystemServerSocExt {
    private static final String TAG = "SystemServerSocExtImpl";
    private static MtkSystemServer sMtkSystemServerIns = MtkSystemServer.getInstance();
    private static final TimingsTraceAndSlog BOOT_TIMINGS_TRACE_LOG = new TimingsTraceAndSlog("SystemServer", 524288);

    @Override // com.android.server.ISystemServerSocExt
    public void startServiceForActivityTrigger(SystemServiceManager systemServiceManager) {
    }

    public SystemServerSocExtImpl(Object obj) {
    }

    @Override // com.android.server.ISystemServerSocExt
    public void setPrameters(SystemServiceManager systemServiceManager, Context context) {
        sMtkSystemServerIns.setPrameters(BOOT_TIMINGS_TRACE_LOG, systemServiceManager, context);
    }

    @Override // com.android.server.ISystemServerSocExt
    public void startOtherServices() {
        sMtkSystemServerIns.startMtkOtherServices();
    }
}
