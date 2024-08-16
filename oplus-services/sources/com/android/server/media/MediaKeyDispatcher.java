package com.android.server.media;

import android.app.PendingIntent;
import android.content.Context;
import android.media.session.MediaSession;
import android.view.KeyEvent;
import com.android.server.power.stats.BatteryStatsImpl;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class MediaKeyDispatcher {
    static final int KEY_EVENT_DOUBLE_TAP = 2;
    static final int KEY_EVENT_LONG_PRESS = 8;
    static final int KEY_EVENT_SINGLE_TAP = 1;
    static final int KEY_EVENT_TRIPLE_TAP = 4;
    private Map<Integer, Integer> mOverriddenKeyEvents;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    @interface KeyEventType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isDoubleTapOverridden(int i) {
        return (i & 2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLongPressOverridden(int i) {
        return (i & 8) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSingleTapOverridden(int i) {
        return (i & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTripleTapOverridden(int i) {
        return (i & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PendingIntent getMediaButtonReceiver(KeyEvent keyEvent, int i, boolean z) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaSession.Token getMediaSession(KeyEvent keyEvent, int i, boolean z) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDoubleTap(KeyEvent keyEvent) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLongPress(KeyEvent keyEvent) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSingleTap(KeyEvent keyEvent) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTripleTap(KeyEvent keyEvent) {
    }

    public MediaKeyDispatcher(Context context) {
        HashMap hashMap = new HashMap();
        this.mOverriddenKeyEvents = hashMap;
        hashMap.put(126, 0);
        this.mOverriddenKeyEvents.put(Integer.valueOf(BatteryStatsImpl.ExternalStatsSync.UPDATE_ALL), 0);
        this.mOverriddenKeyEvents.put(85, 0);
        this.mOverriddenKeyEvents.put(91, 0);
        this.mOverriddenKeyEvents.put(79, 0);
        this.mOverriddenKeyEvents.put(86, 0);
        this.mOverriddenKeyEvents.put(87, 0);
        this.mOverriddenKeyEvents.put(88, 0);
        this.mOverriddenKeyEvents.put(25, 0);
        this.mOverriddenKeyEvents.put(24, 0);
        this.mOverriddenKeyEvents.put(164, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Integer, Integer> getOverriddenKeyEvents() {
        return this.mOverriddenKeyEvents;
    }

    void setOverriddenKeyEvents(int i, int i2) {
        this.mOverriddenKeyEvents.put(Integer.valueOf(i), Integer.valueOf(i2));
    }
}
