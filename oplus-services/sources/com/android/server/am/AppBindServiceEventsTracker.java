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
final class AppBindServiceEventsTracker extends BaseAppStateTimeSlotEventsTracker<AppBindServiceEventsPolicy, BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents> implements ActivityManagerInternal.BindServiceEventListener {
    static final boolean DEBUG_APP_STATE_BIND_SERVICE_EVENT_TRACKER = false;
    static final String TAG = "ActivityManager";

    @Override // com.android.server.am.BaseAppStateTracker
    @AppRestrictionController.TrackerType
    int getType() {
        return 7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppBindServiceEventsTracker(Context context, AppRestrictionController appRestrictionController) {
        this(context, appRestrictionController, null, null);
    }

    AppBindServiceEventsTracker(Context context, AppRestrictionController appRestrictionController, Constructor<? extends BaseAppStateTracker.Injector<AppBindServiceEventsPolicy>> constructor, Object obj) {
        super(context, appRestrictionController, constructor, obj);
        BaseAppStateTracker.Injector<T> injector = this.mInjector;
        injector.setPolicy(new AppBindServiceEventsPolicy(injector, this));
    }

    public void onBindingService(String str, int i) {
        if (((AppBindServiceEventsPolicy) this.mInjector.getPolicy()).isEnabled()) {
            onNewEvent(str, i);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() {
        super.onSystemReady();
        this.mInjector.getActivityManagerInternal().addBindServiceEventListener(this);
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents createAppStateEvents(int i, String str) {
        return new BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents(i, str, ((AppBindServiceEventsPolicy) this.mInjector.getPolicy()).getTimeSlotSize(), "ActivityManager", (BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
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
        printWriter.println("APP BIND SERVICE EVENT TRACKER:");
        super.dump(printWriter, "  " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AppBindServiceEventsPolicy extends BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy<AppBindServiceEventsTracker> {
        static final boolean DEFAULT_BG_BIND_SVC_MONITOR_ENABLED = true;
        static final long DEFAULT_BG_BIND_SVC_WINDOW = 86400000;
        static final int DEFAULT_BG_EX_BIND_SVC_THRESHOLD = 10000;
        static final String KEY_BG_BIND_SVC_MONITOR_ENABLED = "bg_bind_svc_monitor_enabled";
        static final String KEY_BG_BIND_SVC_WINDOW = "bg_bind_svc_window";
        static final String KEY_BG_EX_BIND_SVC_THRESHOLD = "bg_ex_bind_svc_threshold";

        @Override // com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy
        String getEventName() {
            return "bindservice";
        }

        AppBindServiceEventsPolicy(BaseAppStateTracker.Injector injector, AppBindServiceEventsTracker appBindServiceEventsTracker) {
            super(injector, appBindServiceEventsTracker, KEY_BG_BIND_SVC_MONITOR_ENABLED, true, KEY_BG_BIND_SVC_WINDOW, 86400000L, KEY_BG_EX_BIND_SVC_THRESHOLD, 10000);
        }

        @Override // com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy, com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.println("APP BIND SERVICE EVENT TRACKER POLICY SETTINGS:");
            super.dump(printWriter, "  " + str);
        }
    }
}
