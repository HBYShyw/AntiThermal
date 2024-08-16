package com.oplus.statistics.record;

import android.content.Context;
import com.oplus.statistics.storage.PreferenceHandler;
import java.util.UUID;

/* loaded from: classes2.dex */
public class StatIdManager {
    public static final long EXPIRE_TIME_MS = 30000;
    private static final String SP_KEY_APP_EXIT_TIME = "AppExitTime";
    private static final String SP_KEY_APP_SESSION_ID = "AppSessionId";
    private String mAppSessionId;
    private long mExitAppTime;

    /* loaded from: classes2.dex */
    private static class Holder {
        private static final StatIdManager INSTANCE = new StatIdManager();

        private Holder() {
        }
    }

    private String buildSessionId() {
        return UUID.randomUUID().toString();
    }

    private long getAppLastExitTimeFromSp(Context context) {
        return PreferenceHandler.getLong(context, SP_KEY_APP_EXIT_TIME, 0L);
    }

    private String getAppSessionIdFromSp(Context context) {
        return PreferenceHandler.getString(context, SP_KEY_APP_SESSION_ID, "");
    }

    public static StatIdManager getInstance() {
        return Holder.INSTANCE;
    }

    private boolean isAppSessionIdFresh(Context context) {
        if (this.mExitAppTime == 0) {
            this.mExitAppTime = getAppLastExitTimeFromSp(context);
        }
        long currentTimeMillis = System.currentTimeMillis() - this.mExitAppTime;
        return currentTimeMillis > 0 && currentTimeMillis < EXPIRE_TIME_MS;
    }

    private void setAppExitTime2Sp(Context context, long j10) {
        PreferenceHandler.setLong(context, SP_KEY_APP_EXIT_TIME, j10);
    }

    private void setAppSessionId2Sp(Context context, String str) {
        PreferenceHandler.setString(context, SP_KEY_APP_SESSION_ID, str);
    }

    public String getAppSessionId(Context context) {
        if (this.mAppSessionId == null) {
            refreshAppSessionIdIfNeed(context);
        }
        return this.mAppSessionId;
    }

    public void onAppExit(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        this.mExitAppTime = currentTimeMillis;
        setAppExitTime2Sp(context, currentTimeMillis);
    }

    public void refreshAppSessionId(Context context) {
        String buildSessionId = buildSessionId();
        this.mAppSessionId = buildSessionId;
        setAppSessionId2Sp(context, buildSessionId);
    }

    public void refreshAppSessionIdIfNeed(Context context) {
        if (!isAppSessionIdFresh(context)) {
            refreshAppSessionId(context);
        } else {
            this.mAppSessionId = getAppSessionIdFromSp(context);
        }
    }

    private StatIdManager() {
        this.mAppSessionId = null;
        this.mExitAppTime = 0L;
    }
}
