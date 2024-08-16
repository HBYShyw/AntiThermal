package com.android.server.am;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.MutableLong;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.util.proto.ProtoUtils;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppTimeTracker {
    private final ArrayMap<String, MutableLong> mPackageTimes = new ArrayMap<>();
    private final PendingIntent mReceiver;
    private String mStartedPackage;
    private MutableLong mStartedPackageTime;
    private long mStartedTime;
    private long mTotalTime;

    public AppTimeTracker(PendingIntent pendingIntent) {
        this.mReceiver = pendingIntent;
    }

    public void start(String str) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.mStartedTime == 0) {
            this.mStartedTime = elapsedRealtime;
        }
        if (str.equals(this.mStartedPackage)) {
            return;
        }
        MutableLong mutableLong = this.mStartedPackageTime;
        if (mutableLong != null) {
            long j = elapsedRealtime - this.mStartedTime;
            mutableLong.value += j;
            this.mTotalTime += j;
        }
        this.mStartedPackage = str;
        MutableLong mutableLong2 = this.mPackageTimes.get(str);
        this.mStartedPackageTime = mutableLong2;
        if (mutableLong2 == null) {
            MutableLong mutableLong3 = new MutableLong(0L);
            this.mStartedPackageTime = mutableLong3;
            this.mPackageTimes.put(str, mutableLong3);
        }
    }

    public void stop() {
        if (this.mStartedTime != 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mStartedTime;
            this.mTotalTime += elapsedRealtime;
            MutableLong mutableLong = this.mStartedPackageTime;
            if (mutableLong != null) {
                mutableLong.value += elapsedRealtime;
            }
            this.mStartedPackage = null;
            this.mStartedPackageTime = null;
        }
    }

    public void deliverResult(Context context) {
        stop();
        Bundle bundle = new Bundle();
        bundle.putLong("android.activity.usage_time", this.mTotalTime);
        Bundle bundle2 = new Bundle();
        for (int size = this.mPackageTimes.size() - 1; size >= 0; size--) {
            bundle2.putLong(this.mPackageTimes.keyAt(size), this.mPackageTimes.valueAt(size).value);
        }
        bundle.putBundle("android.usage_time_packages", bundle2);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        try {
            this.mReceiver.send(context, 0, intent);
        } catch (PendingIntent.CanceledException unused) {
        }
    }

    public void dumpWithHeader(PrintWriter printWriter, String str, boolean z) {
        printWriter.print(str);
        printWriter.print("AppTimeTracker #");
        printWriter.print(Integer.toHexString(System.identityHashCode(this)));
        printWriter.println(":");
        dump(printWriter, str + "  ", z);
    }

    public void dump(PrintWriter printWriter, String str, boolean z) {
        printWriter.print(str);
        printWriter.print("mReceiver=");
        printWriter.println(this.mReceiver);
        printWriter.print(str);
        printWriter.print("mTotalTime=");
        TimeUtils.formatDuration(this.mTotalTime, printWriter);
        printWriter.println();
        for (int i = 0; i < this.mPackageTimes.size(); i++) {
            printWriter.print(str);
            printWriter.print("mPackageTime:");
            printWriter.print(this.mPackageTimes.keyAt(i));
            printWriter.print("=");
            TimeUtils.formatDuration(this.mPackageTimes.valueAt(i).value, printWriter);
            printWriter.println();
        }
        if (!z || this.mStartedTime == 0) {
            return;
        }
        printWriter.print(str);
        printWriter.print("mStartedTime=");
        TimeUtils.formatDuration(SystemClock.elapsedRealtime(), this.mStartedTime, printWriter);
        printWriter.println();
        printWriter.print(str);
        printWriter.print("mStartedPackage=");
        printWriter.println(this.mStartedPackage);
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, boolean z) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, this.mReceiver.toString());
        protoOutputStream.write(1112396529666L, this.mTotalTime);
        for (int i = 0; i < this.mPackageTimes.size(); i++) {
            long start2 = protoOutputStream.start(2246267895811L);
            protoOutputStream.write(1138166333441L, this.mPackageTimes.keyAt(i));
            protoOutputStream.write(1112396529666L, this.mPackageTimes.valueAt(i).value);
            protoOutputStream.end(start2);
        }
        if (z) {
            long j2 = this.mStartedTime;
            if (j2 != 0) {
                ProtoUtils.toDuration(protoOutputStream, 1146756268036L, j2, SystemClock.elapsedRealtime());
                protoOutputStream.write(1138166333445L, this.mStartedPackage);
            }
        }
        protoOutputStream.end(start);
    }
}
