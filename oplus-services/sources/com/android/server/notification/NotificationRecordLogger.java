package com.android.server.notification;

import android.app.NotificationChannel;
import android.app.Person;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.vr.Vr2dDisplay;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface NotificationRecordLogger {
    public static final String TAG = "NotificationRecordLogger";

    void log(UiEventLogger.UiEventEnum uiEventEnum);

    void log(UiEventLogger.UiEventEnum uiEventEnum, NotificationRecord notificationRecord);

    void logNotificationAdjusted(NotificationRecord notificationRecord, int i, int i2, InstanceId instanceId);

    void logNotificationPosted(NotificationReported notificationReported);

    default NotificationReported prepareToLogNotificationPosted(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, int i, int i2, InstanceId instanceId) {
        NotificationRecordPair notificationRecordPair = new NotificationRecordPair(notificationRecord, notificationRecord2);
        if (notificationRecordPair.shouldLogReported(i2)) {
            return new NotificationReported(notificationRecordPair, NotificationReportedEvent.fromRecordPair(notificationRecordPair), i, i2, instanceId);
        }
        return null;
    }

    default void logNotificationCancelled(NotificationRecord notificationRecord, @NotificationListenerService.NotificationCancelReason int i, int i2) {
        log(NotificationCancelledEvent.fromCancelReason(i, i2), notificationRecord);
    }

    default void logNotificationVisibility(NotificationRecord notificationRecord, boolean z) {
        log(NotificationEvent.fromVisibility(z), notificationRecord);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum NotificationReportedEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_POSTED(162),
        NOTIFICATION_UPDATED(163),
        NOTIFICATION_ADJUSTED(908);

        private final int mId;

        NotificationReportedEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static NotificationReportedEvent fromRecordPair(NotificationRecordPair notificationRecordPair) {
            return notificationRecordPair.old != null ? NOTIFICATION_UPDATED : NOTIFICATION_POSTED;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum NotificationCancelledEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        NOTIFICATION_CANCEL_CLICK(164),
        NOTIFICATION_CANCEL_USER_OTHER(165),
        NOTIFICATION_CANCEL_USER_CANCEL_ALL(166),
        NOTIFICATION_CANCEL_ERROR(167),
        NOTIFICATION_CANCEL_PACKAGE_CHANGED(168),
        NOTIFICATION_CANCEL_USER_STOPPED(169),
        NOTIFICATION_CANCEL_PACKAGE_BANNED(170),
        NOTIFICATION_CANCEL_APP_CANCEL(171),
        NOTIFICATION_CANCEL_APP_CANCEL_ALL(172),
        NOTIFICATION_CANCEL_LISTENER_CANCEL(173),
        NOTIFICATION_CANCEL_LISTENER_CANCEL_ALL(174),
        NOTIFICATION_CANCEL_GROUP_SUMMARY_CANCELED(175),
        NOTIFICATION_CANCEL_GROUP_OPTIMIZATION(176),
        NOTIFICATION_CANCEL_PACKAGE_SUSPENDED(HdmiCecKeycode.UI_SOUND_PRESENTATION_BASS_STEP_PLUS),
        NOTIFICATION_CANCEL_PROFILE_TURNED_OFF(HdmiCecKeycode.UI_SOUND_PRESENTATION_BASS_NEUTRAL),
        NOTIFICATION_CANCEL_UNAUTOBUNDLED(HdmiCecKeycode.UI_SOUND_PRESENTATION_BASS_STEP_MINUS),
        NOTIFICATION_CANCEL_CHANNEL_BANNED(180),
        NOTIFICATION_CANCEL_SNOOZED(181),
        NOTIFICATION_CANCEL_TIMEOUT(182),
        NOTIFICATION_CANCEL_CHANNEL_REMOVED(1261),
        NOTIFICATION_CANCEL_CLEAR_DATA(1262),
        NOTIFICATION_CANCEL_USER_PEEK(190),
        NOTIFICATION_CANCEL_USER_AOD(191),
        NOTIFICATION_CANCEL_USER_BUBBLE(1228),
        NOTIFICATION_CANCEL_USER_LOCKSCREEN(HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS),
        NOTIFICATION_CANCEL_USER_SHADE(192),
        NOTIFICATION_CANCEL_ASSISTANT(906);

        private final int mId;

        NotificationCancelledEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static NotificationCancelledEvent fromCancelReason(@NotificationListenerService.NotificationCancelReason int i, int i2) {
            if (i2 == -1) {
                Log.wtf(NotificationRecordLogger.TAG, "Unexpected surface: " + i2 + " with reason " + i);
                return INVALID;
            }
            if (i != 2) {
                if (1 <= i && i <= 21) {
                    return values()[i];
                }
                if (i == 22) {
                    return NOTIFICATION_CANCEL_ASSISTANT;
                }
                Log.wtf(NotificationRecordLogger.TAG, "Unexpected reason: " + i + " with surface " + i2);
                return INVALID;
            }
            if (i2 == 0) {
                return NOTIFICATION_CANCEL_USER_OTHER;
            }
            if (i2 == 1) {
                return NOTIFICATION_CANCEL_USER_PEEK;
            }
            if (i2 == 2) {
                return NOTIFICATION_CANCEL_USER_AOD;
            }
            if (i2 == 3) {
                return NOTIFICATION_CANCEL_USER_SHADE;
            }
            if (i2 == 4) {
                return NOTIFICATION_CANCEL_USER_BUBBLE;
            }
            if (i2 == 5) {
                return NOTIFICATION_CANCEL_USER_LOCKSCREEN;
            }
            Log.wtf(NotificationRecordLogger.TAG, "Unexpected surface: " + i2 + " with reason " + i);
            return INVALID;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum NotificationEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_OPEN(197),
        NOTIFICATION_CLOSE(198),
        NOTIFICATION_SNOOZED(317),
        NOTIFICATION_NOT_POSTED_SNOOZED(319),
        NOTIFICATION_CLICKED(Vr2dDisplay.DEFAULT_VIRTUAL_DISPLAY_DPI),
        NOTIFICATION_ACTION_CLICKED(321),
        NOTIFICATION_DETAIL_OPEN_SYSTEM(327),
        NOTIFICATION_DETAIL_CLOSE_SYSTEM(328),
        NOTIFICATION_DETAIL_OPEN_USER(329),
        NOTIFICATION_DETAIL_CLOSE_USER(330),
        NOTIFICATION_DIRECT_REPLIED(331),
        NOTIFICATION_SMART_REPLIED(332),
        NOTIFICATION_SMART_REPLY_VISIBLE(333),
        NOTIFICATION_ACTION_CLICKED_0(450),
        NOTIFICATION_ACTION_CLICKED_1(451),
        NOTIFICATION_ACTION_CLICKED_2(452),
        NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_0(453),
        NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_1(454),
        NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_2(455),
        NOTIFICATION_ASSIST_ACTION_CLICKED_0(456),
        NOTIFICATION_ASSIST_ACTION_CLICKED_1(457),
        NOTIFICATION_ASSIST_ACTION_CLICKED_2(458);

        private final int mId;

        NotificationEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static NotificationEvent fromVisibility(boolean z) {
            return z ? NOTIFICATION_OPEN : NOTIFICATION_CLOSE;
        }

        public static NotificationEvent fromExpanded(boolean z, boolean z2) {
            return z2 ? z ? NOTIFICATION_DETAIL_OPEN_USER : NOTIFICATION_DETAIL_CLOSE_USER : z ? NOTIFICATION_DETAIL_OPEN_SYSTEM : NOTIFICATION_DETAIL_CLOSE_SYSTEM;
        }

        public static NotificationEvent fromAction(int i, boolean z, boolean z2) {
            if (i < 0 || i > 2) {
                return NOTIFICATION_ACTION_CLICKED;
            }
            if (z) {
                return values()[NOTIFICATION_ASSIST_ACTION_CLICKED_0.ordinal() + i];
            }
            if (z2) {
                return values()[NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_0.ordinal() + i];
            }
            return values()[NOTIFICATION_ACTION_CLICKED_0.ordinal() + i];
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public enum NotificationPanelEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_PANEL_OPEN(325),
        NOTIFICATION_PANEL_CLOSE(326);

        private final int mId;

        NotificationPanelEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NotificationRecordPair {
        public final NotificationRecord old;
        public final NotificationRecord r;

        /* JADX INFO: Access modifiers changed from: package-private */
        public NotificationRecordPair(NotificationRecord notificationRecord, NotificationRecord notificationRecord2) {
            this.r = notificationRecord;
            this.old = notificationRecord2;
        }

        boolean shouldLogReported(int i) {
            NotificationRecord notificationRecord = this.r;
            if (notificationRecord == null) {
                return false;
            }
            if (this.old == null || i > 0) {
                return true;
            }
            return (Objects.equals(notificationRecord.getSbn().getChannelIdLogTag(), this.old.getSbn().getChannelIdLogTag()) && Objects.equals(this.r.getSbn().getGroupLogTag(), this.old.getSbn().getGroupLogTag()) && this.r.getSbn().getNotification().isGroupSummary() == this.old.getSbn().getNotification().isGroupSummary() && Objects.equals(this.r.getSbn().getNotification().category, this.old.getSbn().getNotification().category) && this.r.getImportance() == this.old.getImportance() && NotificationRecordLogger.getLoggingImportance(this.r) == NotificationRecordLogger.getLoggingImportance(this.old) && this.r.rankingScoreMatches(this.old.getRankingScore())) ? false : true;
        }

        public int getStyle() {
            return getStyle(this.r.getSbn().getNotification().extras);
        }

        private int getStyle(Bundle bundle) {
            String string;
            if (bundle == null || (string = bundle.getString("android.template")) == null || string.isEmpty()) {
                return 0;
            }
            return string.hashCode();
        }

        int getNumPeople() {
            return getNumPeople(this.r.getSbn().getNotification().extras);
        }

        private int getNumPeople(Bundle bundle) {
            ArrayList parcelableArrayList;
            if (bundle == null || (parcelableArrayList = bundle.getParcelableArrayList("android.people.list", Person.class)) == null || parcelableArrayList.isEmpty()) {
                return 0;
            }
            return parcelableArrayList.size();
        }

        int getAssistantHash() {
            String adjustmentIssuer = this.r.getAdjustmentIssuer();
            if (adjustmentIssuer == null) {
                return 0;
            }
            return adjustmentIssuer.hashCode();
        }

        int getInstanceId() {
            if (this.r.getSbn().getInstanceId() == null) {
                return 0;
            }
            return this.r.getSbn().getInstanceId().getId();
        }

        int getNotificationIdHash() {
            return SmallHash.hash(this.r.getSbn().getId() ^ Objects.hashCode(this.r.getSbn().getTag()));
        }

        int getChannelIdHash() {
            return SmallHash.hash(this.r.getSbn().getNotification().getChannelId());
        }

        int getGroupIdHash() {
            return SmallHash.hash(this.r.getSbn().getGroup());
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NotificationReported {
        final int alerting;
        final int assistant_hash;
        final float assistant_ranking_score;
        final String category;
        final int channel_id_hash;
        final int event_id;
        final int group_id_hash;
        final int group_instance_id;
        final int importance;
        final int importance_asst;
        final int importance_initial;
        final int importance_initial_source;
        final int importance_source;
        final int instance_id;
        final boolean is_foreground_service;
        final boolean is_group_summary;
        final boolean is_non_dismissible;
        final boolean is_ongoing;
        final int notification_id_hash;
        final int num_people;
        final String package_name;
        final int position;
        long post_duration_millis;
        final int style;
        final long timeout_millis;
        final int uid;

        /* JADX INFO: Access modifiers changed from: package-private */
        public NotificationReported(NotificationRecordPair notificationRecordPair, NotificationReportedEvent notificationReportedEvent, int i, int i2, InstanceId instanceId) {
            this.event_id = notificationReportedEvent.getId();
            this.uid = notificationRecordPair.r.getUid();
            this.package_name = notificationRecordPair.r.getSbn().getPackageName();
            this.instance_id = notificationRecordPair.getInstanceId();
            this.notification_id_hash = notificationRecordPair.getNotificationIdHash();
            this.channel_id_hash = notificationRecordPair.getChannelIdHash();
            this.group_id_hash = notificationRecordPair.getGroupIdHash();
            this.group_instance_id = instanceId == null ? 0 : instanceId.getId();
            this.is_group_summary = notificationRecordPair.r.getSbn().getNotification().isGroupSummary();
            this.category = notificationRecordPair.r.getSbn().getNotification().category;
            this.style = notificationRecordPair.getStyle();
            this.num_people = notificationRecordPair.getNumPeople();
            this.position = i;
            this.importance = NotificationRecordLogger.getLoggingImportance(notificationRecordPair.r);
            this.alerting = i2;
            this.importance_source = notificationRecordPair.r.getImportanceExplanationCode();
            this.importance_initial = notificationRecordPair.r.getInitialImportance();
            this.importance_initial_source = notificationRecordPair.r.getInitialImportanceExplanationCode();
            this.importance_asst = notificationRecordPair.r.getAssistantImportance();
            this.assistant_hash = notificationRecordPair.getAssistantHash();
            this.assistant_ranking_score = notificationRecordPair.r.getRankingScore();
            this.is_ongoing = notificationRecordPair.r.getSbn().isOngoing();
            this.is_foreground_service = NotificationRecordLogger.isForegroundService(notificationRecordPair.r);
            this.timeout_millis = notificationRecordPair.r.getSbn().getNotification().getTimeoutAfter();
            this.is_non_dismissible = NotificationRecordLogger.isNonDismissible(notificationRecordPair.r);
        }
    }

    static int getLoggingImportance(NotificationRecord notificationRecord) {
        int importance = notificationRecord.getImportance();
        NotificationChannel channel = notificationRecord.getChannel();
        return channel == null ? importance : NotificationChannelLogger.getLoggingImportance(channel, importance);
    }

    static boolean isForegroundService(NotificationRecord notificationRecord) {
        return (notificationRecord.getSbn() == null || notificationRecord.getSbn().getNotification() == null || (notificationRecord.getSbn().getNotification().flags & 64) == 0) ? false : true;
    }

    static boolean isNonDismissible(NotificationRecord notificationRecord) {
        return (notificationRecord.getSbn() == null || notificationRecord.getSbn().getNotification() == null || (notificationRecord.getNotification().flags & 8192) == 0) ? false : true;
    }
}
