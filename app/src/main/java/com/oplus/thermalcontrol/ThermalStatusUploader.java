package com.oplus.thermalcontrol;

import android.content.Context;
import android.os.Looper;
import android.util.SparseArray;
import com.oplus.statistics.util.TimeInfoUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import x5.UploadDataUtil;

/* loaded from: classes2.dex */
public class ThermalStatusUploader {
    private static final String EVENT_TEMP_LEVEL_CHANGE_TRACE = "temp_level_change_trace";
    private static final int MAX_TRACE_COUNT = 100;
    private static final double ZERO_POINT_ONE = 0.1d;
    private static volatile ThermalStatusUploader sThermalStatusUploader;
    private Context mContext;
    private Looper mLooper;
    private SparseArray<HashMap<String, String>> mThermalTrace = new SparseArray<>();
    private UploadDataUtil mUploadUtil;

    public ThermalStatusUploader(Context context, Looper looper) {
        this.mLooper = null;
        this.mUploadUtil = null;
        this.mContext = context;
        this.mLooper = looper;
        this.mUploadUtil = UploadDataUtil.S0(context);
    }

    public static ThermalStatusUploader getInstance(Context context, Looper looper) {
        if (sThermalStatusUploader == null) {
            synchronized (ThermalStatusUploader.class) {
                if (sThermalStatusUploader == null) {
                    sThermalStatusUploader = new ThermalStatusUploader(context, looper);
                }
            }
        }
        return sThermalStatusUploader;
    }

    private String printTimeInMillis(long j10) {
        return new SimpleDateFormat(TimeInfoUtil.TIME_PATTERN_01).format(new Date(j10));
    }

    public void uploadTempLevelChangeTraceEvent(ThermalTraceInfo thermalTraceInfo) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (thermalTraceInfo != null) {
            hashMap = thermalTraceInfo.currentStateTransformToMap();
        }
        this.mUploadUtil.W0(EVENT_TEMP_LEVEL_CHANGE_TRACE, hashMap, false);
    }
}
