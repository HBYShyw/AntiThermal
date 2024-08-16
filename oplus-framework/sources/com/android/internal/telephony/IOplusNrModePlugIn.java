package com.android.internal.telephony;

import android.content.Context;
import android.telephony.Rlog;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusNrModePlugIn {
    default void initialOnce(Context context, String pluginApk) {
        Rlog.d("error", "OplusNrMode not plugIn");
    }

    default void updateDdsAndSubSimInfo(int mCurSubId) {
    }

    default int getNrModeFromCityCfg(int slotId, int modeupdate) {
        return modeupdate;
    }

    default void modemDumpMonitor(String reason, String event) {
    }

    default boolean getDisableSAState() {
        return false;
    }

    default void cancelDisableSAState() {
    }

    default void onRejectedCall(int phoneId, int rat) {
    }

    default void updateCityNrModeCfg() {
    }

    default void onReceiveErrorCode(int messageRat, int messageErrorCode) {
    }

    default Map<String, String> getNrModeChangedEvent() {
        return new HashMap();
    }

    default void addNrModeChangedEvent(int slotId, String bizName, String whatToDo) {
    }

    default boolean addNrModeChangedEvent(String clsName, int slotId, int mode, boolean isSpecial) {
        return true;
    }

    default void reportNetWorkLatency(String info) {
    }

    default void reportGameInfo(String gameInfo) {
    }

    default void reportGameEnterOrLeave(boolean enter) {
    }

    default boolean updateIfGameFeatureBackOffSa() {
        return false;
    }

    default String getGameBackoffSaInfo() {
        return null;
    }

    default void notifyNrModeChanged(int slotId, int nrMode) {
    }
}
