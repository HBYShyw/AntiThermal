package com.android.server.am;

import android.app.ActivityManagerInternal;
import android.content.Context;
import android.os.SystemClock;
import android.util.proto.ProtoOutputStream;
import com.android.server.am.AppRestrictionController;
import com.android.server.am.BaseAppStateEvents;
import com.android.server.am.BaseAppStateTimeSlotEventsTracker;
import com.android.server.am.BaseAppStateTracker;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
final class AppBroadcastEventsTracker extends BaseAppStateTimeSlotEventsTracker<AppBroadcastEventsPolicy, BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents> implements ActivityManagerInternal.BroadcastEventListener {
    static final boolean DEBUG_APP_STATE_BROADCAST_EVENT_TRACKER = false;
    static final String TAG = "ActivityManager";

    @Override // com.android.server.am.BaseAppStateTracker
    @AppRestrictionController.TrackerType
    int getType() {
        return 6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppBroadcastEventsTracker(Context context, AppRestrictionController appRestrictionController) {
        this(context, appRestrictionController, null, null);
    }

    AppBroadcastEventsTracker(Context context, AppRestrictionController appRestrictionController, Constructor<? extends BaseAppStateTracker.Injector<AppBroadcastEventsPolicy>> constructor, Object obj) {
        super(context, appRestrictionController, constructor, obj);
        BaseAppStateTracker.Injector<T> injector = this.mInjector;
        injector.setPolicy(new AppBroadcastEventsPolicy(injector, this));
    }

    public void onSendingBroadcast(String str, int i) {
        if (((AppBroadcastEventsPolicy) this.mInjector.getPolicy()).isEnabled()) {
            onNewEvent(str, i);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() {
        super.onSystemReady();
        this.mInjector.getActivityManagerInternal().addBroadcastEventListener(this);
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents createAppStateEvents(int i, String str) {
        return new BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents(i, str, ((AppBroadcastEventsPolicy) this.mInjector.getPolicy()).getTimeSlotSize(), "ActivityManager", (BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents createAppStateEvents(BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents simpleAppStateTimeslotEvents) {
        return new BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents(simpleAppStateTimeslotEvents);
    }

    @Override // com.android.server.am.BaseAppStateTracker
    byte[] getTrackerInfoForStatsd(int i) {
        int totalEventsLocked = getTotalEventsLocked(i, SystemClock.elapsedRealtime());
        if (totalEventsLocked == 0) {
            return null;
        }
        ProtoOutputStream protoOutputStream = new ProtoOutputStream();
        protoOutputStream.write(1120986464257L, totalEventsLocked);
        protoOutputStream.flush();
        return protoOutputStream.getBytes();
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println("APP BROADCAST EVENT TRACKER:");
        super.dump(printWriter, "  " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AppBroadcastEventsPolicy extends BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy<AppBroadcastEventsTracker> {
        static final boolean DEFAULT_BG_BROADCAST_MONITOR_ENABLED = true;
        static final long DEFAULT_BG_BROADCAST_WINDOW = 86400000;
        static final int DEFAULT_BG_EX_BROADCAST_THRESHOLD = 10000;
        static final String KEY_BG_BROADCAST_MONITOR_ENABLED = "bg_broadcast_monitor_enabled";
        static final String KEY_BG_BROADCAST_WINDOW = "bg_broadcast_window";
        static final String KEY_BG_EX_BROADCAST_THRESHOLD = "bg_ex_broadcast_threshold";

        @Override // com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy
        String getEventName() {
            return "broadcast";
        }

        AppBroadcastEventsPolicy(BaseAppStateTracker.Injector injector, AppBroadcastEventsTracker appBroadcastEventsTracker) {
            super(injector, appBroadcastEventsTracker, KEY_BG_BROADCAST_MONITOR_ENABLED, true, KEY_BG_BROADCAST_WINDOW, 86400000L, KEY_BG_EX_BROADCAST_THRESHOLD, 10000);
        }

        @Override // com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy, com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.println("APP BROADCAST EVENT TRACKER POLICY SETTINGS:");
            super.dump(printWriter, "  " + str);
        }
    }
}
