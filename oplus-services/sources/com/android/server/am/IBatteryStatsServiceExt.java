package com.android.server.am;

import android.content.Context;
import android.os.WorkSource;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IBatteryStatsServiceExt {
    default boolean dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        return false;
    }

    default void init(Context context) {
    }

    default void noteBleScanReset() {
    }

    default void noteBleScanStarted(WorkSource workSource, boolean z) {
    }

    default void noteBleScanStopped(WorkSource workSource, boolean z) {
    }

    default void noteGpsChanged(WorkSource workSource, WorkSource workSource2) {
    }

    default void noteResetAudio() {
    }

    default void noteResetCamera() {
    }

    default void noteResetVideo() {
    }

    default void noteStartAudio(int i) {
    }

    default void noteStartCamera(int i) {
    }

    default void noteStartSensor(int i, int i2) {
    }

    default void noteStartVideo(int i) {
    }

    default void noteStopAudio(int i) {
    }

    default void noteStopCamera(int i) {
    }

    default void noteStopSensor(int i, int i2) {
    }

    default void noteStopVideo(int i) {
    }

    default void noteWifiScanStarted(int i) {
    }

    default void noteWifiScanStartedFromSource(WorkSource workSource) {
    }

    default void noteWifiScanStopped(int i) {
    }

    default void noteWifiScanStoppedFromSource(WorkSource workSource) {
    }

    default void setDumpParam(int i, long j) {
    }

    default void setThermalState(Object obj) {
    }

    default void systemServicesReady() {
    }
}
