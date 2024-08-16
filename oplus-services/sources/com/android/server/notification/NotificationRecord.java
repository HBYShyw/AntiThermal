package com.android.server.notification;

import android.R;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Person;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioSystem;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.service.notification.Adjustment;
import android.service.notification.NotificationListenerService;
import android.service.notification.NotificationStats;
import android.service.notification.SnoozeCriterion;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.widget.RemoteViews;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.server.EventLogTags;
import com.android.server.LocalServices;
import com.android.server.job.JobSchedulerShellCommand;
import com.android.server.notification.NotificationUsageStats;
import com.android.server.uri.UriGrantsManagerInternal;
import dalvik.annotation.optimization.NeverCompile;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class NotificationRecord {
    private static final int MAX_SOUND_DELAY_MS = 2000;
    private static final int NOTIFICATION_REAR_LIGHT_DEFAULT_COLOR_RM = 570490654;
    private static final String SPECIAL_LIGHTS_SETTING_NAME = "settings_special_lights";
    boolean isCanceled;
    public boolean isUpdate;
    private String mAdjustmentIssuer;
    private final List<Adjustment> mAdjustments;
    private boolean mAllowBubble;
    private boolean mAppDemotedFromConvo;
    private AudioAttributes mAttributes;
    private int mAuthoritativeRank;
    private NotificationChannel mChannel;
    private float mContactAffinity;
    private final Context mContext;
    private long mCreationTimeMs;
    private boolean mEditChoicesBeforeSending;
    private boolean mFlagBubbleRemoved;
    private String mGlobalSortKey;
    private ArraySet<Uri> mGrantableUris;
    private boolean mHasSeenSmartReplies;
    private boolean mHasSentValidMsg;
    private boolean mHidden;
    private int mImportance;
    private boolean mImportanceFixed;
    private boolean mIntercept;
    private boolean mInterceptSet;
    private long mInterruptionTimeMs;
    private boolean mIsAppImportanceLocked;
    private boolean mIsInterruptive;
    private boolean mIsNotConversationOverride;
    private long mLastAudiblyAlertedMs;
    private long mLastIntrusive;
    private Light mLight;
    private int mNumberOfSmartActionsAdded;
    private int mNumberOfSmartRepliesAdded;
    final int mOriginalFlags;
    private int mPackagePriority;
    private int mPackageVisibility;
    private ArrayList<String> mPeopleOverride;
    private ArraySet<String> mPhoneNumbers;
    private boolean mPkgAllowedAsConvo;
    private boolean mPostSilently;
    private boolean mPreChannelsNotification;
    private boolean mRecentlyIntrusive;
    private boolean mRecordedInterruption;
    private ShortcutInfo mShortcutInfo;
    private boolean mShowBadge;
    private ArrayList<CharSequence> mSmartReplies;
    private ArrayList<SnoozeCriterion> mSnoozeCriteria;
    private Uri mSound;
    private final NotificationStats mStats;
    private boolean mSuggestionsGeneratedByAssistant;
    private ArrayList<Notification.Action> mSystemGeneratedSmartActions;
    final int mTargetSdkVersion;
    private boolean mTextChanged;

    @VisibleForTesting
    final long mUpdateTimeMs;
    private String mUserExplanation;
    private int mUserSentiment;
    private VibrationEffect mVibration;
    private long mVisibleSinceMs;
    IBinder permissionOwner;
    private final StatusBarNotification sbn;
    NotificationUsageStats.SingleNotificationStats stats;
    static final String TAG = "NotificationService--NotificationRecord";
    static final boolean DBG = Log.isLoggable(TAG, 3);
    private int mSystemImportance = JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
    private int mAssistantImportance = JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
    private float mRankingScore = 0.0f;
    private int mCriticality = 2;
    private int mImportanceExplanationCode = 0;
    private int mInitialImportanceExplanationCode = 0;
    private int mSuppressedVisualEffects = 0;
    private boolean mPendingLogUpdate = false;
    private int mProposedImportance = JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
    private boolean mSensitiveContent = false;
    private boolean mLedRM = SystemProperties.getBoolean("ro.oplus.display.led.rm", false);
    private INotificationRecordWrapper mNRWrapper = new NotificationRecordWrapper();
    private INotificationRecordExt mNotificationRecordExt = (INotificationRecordExt) ExtLoader.type(INotificationRecordExt.class).base(this).create();
    IActivityManager mAm = ActivityManager.getService();
    UriGrantsManagerInternal mUgmInternal = (UriGrantsManagerInternal) LocalServices.getService(UriGrantsManagerInternal.class);
    private long mRankingTimeMs = calculateRankingTimeMs(0);

    public NotificationRecord(Context context, StatusBarNotification statusBarNotification, NotificationChannel notificationChannel) {
        this.mImportance = JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE;
        this.mPreChannelsNotification = true;
        this.sbn = statusBarNotification;
        this.mTargetSdkVersion = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getPackageTargetSdkVersion(statusBarNotification.getPackageName());
        this.mOriginalFlags = statusBarNotification.getNotification().flags;
        long postTime = statusBarNotification.getPostTime();
        this.mCreationTimeMs = postTime;
        this.mUpdateTimeMs = postTime;
        this.mInterruptionTimeMs = postTime;
        this.mContext = context;
        this.stats = new NotificationUsageStats.SingleNotificationStats();
        this.mChannel = notificationChannel;
        this.mPreChannelsNotification = isPreChannelsNotification();
        this.mSound = calculateSound();
        this.mVibration = calculateVibration();
        this.mAttributes = calculateAttributes();
        this.mImportance = calculateInitialImportance();
        this.mLight = calculateLights();
        this.mAdjustments = new ArrayList();
        this.mStats = new NotificationStats();
        calculateUserSentiment();
        calculateGrantableUris();
    }

    private boolean isPreChannelsNotification() {
        return "miscellaneous".equals(getChannel().getId()) && this.mTargetSdkVersion < 26;
    }

    private Uri calculateSound() {
        Uri sound;
        Notification notification = getSbn().getNotification();
        if (this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || (sound = this.mChannel.getSound()) == null) {
            return null;
        }
        if (!this.mPreChannelsNotification) {
            return sound;
        }
        if ((notification.defaults & 1) != 0) {
            return Settings.System.DEFAULT_NOTIFICATION_URI;
        }
        return notification.sound;
    }

    private boolean isSpecialLightCase() {
        boolean z = false;
        if (SystemProperties.getBoolean("flag.notification_manager_service.light", false)) {
            Log.i(TAG, "isSpecialLightCase - Use nms light debug flag");
            return true;
        }
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "customize_breath_light_mms", 0) != 1) {
            Log.i(TAG, "isSpecialLightCase - Use setting [breath light] flag OFF");
            return false;
        }
        if (getImportance() < 3) {
            Log.i(TAG, "import low: " + getSbn());
            return false;
        }
        if (getSbn() != null) {
            if (!getSbn().isClearable()) {
                Log.i(TAG, "No Clearable notification: " + getSbn());
                return false;
            }
            Notification notification = getSbn().getNotification();
            if ((notification == null || (notification.flags & 64) == 0) ? false : true) {
                Log.i(TAG, "Foreground service notification: " + getSbn());
                return false;
            }
        }
        if (getSbn() != null) {
            String opPkg = getSbn().getOpPkg();
            String packageName = getSbn().getPackageName();
            int uid = getSbn().getUid();
            int userId = getSbn().getUserId();
            boolean isInLightList = NotificationManagerService.isInLightList(packageName, userId);
            if ("com.android.incallui".equals(packageName)) {
                isInLightList = false;
            }
            Log.i(TAG, "isSpecialLightCase, Opkey: " + opPkg + ", key: " + packageName + ", uid: " + uid + ", userId: " + userId + ", isInList: " + isInLightList);
            if (!isInLightList && "com.android.contacts".equals(packageName) && NotificationManagerService.isInLightList("com.android.incallui", 0) && getChannel() != null && "contacts_missed_call_notification_channel_id".equals(getChannel().getId())) {
                Log.i(TAG, "missing call");
                return true;
            }
            z = isInLightList;
        }
        if (DBG) {
            Log.d(TAG, "isSpecialLightCase false, sbn=" + getSbn());
        }
        return z;
    }

    private Light calculateLights() {
        boolean isSupportRearLight = isSupportRearLight();
        int i = NOTIFICATION_REAR_LIGHT_DEFAULT_COLOR_RM;
        int color = isSupportRearLight ? NOTIFICATION_REAR_LIGHT_DEFAULT_COLOR_RM : this.mContext.getResources().getColor(R.color.primary_text_disable_only_holo_dark);
        int integer = this.mContext.getResources().getInteger(R.integer.config_doubleTapOnHomeBehavior);
        int integer2 = this.mContext.getResources().getInteger(R.integer.config_doublePressOnPowerBehavior);
        int lightColor = getChannel().getLightColor() != 0 ? getChannel().getLightColor() : color;
        Light light = null;
        if (this.mNRWrapper.getNotificationRecordExt().hasCustomizeBreathLight()) {
            if (!isSupportRearLight) {
                i = 537266689;
            }
            StatusBarNotification sbn = getSbn();
            if (sbn != null) {
                i = this.mNRWrapper.getNotificationRecordExt().calculateColor(sbn.getPackageName(), sbn.getUserId(), i);
            }
            if (!isSpecialLightCase()) {
                return null;
            }
            Log.d(TAG, "channelLightColor = 537266689, defaultLightOn = 2, defaultLightOff = 4");
            return new Light(isSupportRearLight ? i : 537266689, 2, 4);
        }
        Light calculateLights = this.mNRWrapper.getNotificationRecordExt().calculateLights(this.mPreChannelsNotification, lightColor, integer, integer2);
        if (!this.mPreChannelsNotification) {
            return calculateLights;
        }
        Notification notification = getSbn().getNotification();
        if ((notification.flags & 1) != 0) {
            light = new Light(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            if ((notification.defaults & 4) != 0) {
                light = new Light(color, integer, integer2);
            }
        }
        return this.mNRWrapper.getNotificationRecordExt().calculateLights(getChannel().shouldShowLights(), light);
    }

    private boolean isSupportRearLight() {
        boolean isSupportRearLight = this.mNRWrapper.getNotificationRecordExt().getIsSupportRearLight();
        Log.d(TAG, "isSupportRearLight , isRearLight = " + isSupportRearLight + " mLedRM = " + this.mLedRM + " isMultilLed = " + this.mNRWrapper.getNotificationRecordExt().isMultilLed());
        return (this.mLedRM && isSupportRearLight) || this.mNRWrapper.getNotificationRecordExt().isMultilLed();
    }

    private VibrationEffect calculateVibration() {
        VibrationEffect vibrationEffect;
        VibratorHelper vibratorHelper = new VibratorHelper(this.mContext);
        Notification notification = getSbn().getNotification();
        boolean z = (notification.flags & 4) != 0;
        VibrationEffect createDefaultVibration = vibratorHelper.createDefaultVibration(z);
        if (this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
            createDefaultVibration = this.mNRWrapper.getNotificationRecordExt().createDefaultVibration(vibratorHelper, z);
            if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                Log.d(TAG, "create default vibration for jp version.");
            }
        }
        if (!getChannel().shouldVibrate()) {
            vibrationEffect = null;
        } else if (getChannel().getVibrationPattern() == null) {
            if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                Log.d(TAG, "using default vibration pattern.");
            }
            vibrationEffect = createDefaultVibration;
        } else {
            long[] vibrationPattern = getChannel().getVibrationPattern();
            if (!this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
                vibrationPattern = this.mNRWrapper.getNotificationRecordExt().modifyVibrationPatternIfNeeded(vibrationPattern, z);
            }
            vibrationEffect = VibratorHelper.createWaveformVibration(vibrationPattern, z);
        }
        if (!this.mPreChannelsNotification || (getChannel().getUserLockedFields() & 16) != 0) {
            return vibrationEffect;
        }
        if ((notification.defaults & 2) != 0) {
            if (!this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                return createDefaultVibration;
            }
            Log.d(TAG, "using default vibration pattern.");
            return createDefaultVibration;
        }
        long[] jArr = notification.vibrate;
        if (!this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
            if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                Log.d(TAG, "vibration pattern in this notification: " + Arrays.toString(jArr));
            }
            jArr = this.mNRWrapper.getNotificationRecordExt().modifyVibrationPatternIfNeeded(jArr, z);
        }
        return VibratorHelper.createWaveformVibration(jArr, z);
    }

    private AudioAttributes calculateAttributes() {
        Notification notification = getSbn().getNotification();
        AudioAttributes audioAttributes = getChannel().getAudioAttributes();
        if (audioAttributes == null) {
            audioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
        }
        if (!this.mPreChannelsNotification || (getChannel().getUserLockedFields() & 32) != 0) {
            return audioAttributes;
        }
        AudioAttributes audioAttributes2 = notification.audioAttributes;
        if (audioAttributes2 != null) {
            return audioAttributes2;
        }
        int i = notification.audioStreamType;
        if (i >= 0 && i < AudioSystem.getNumStreamTypes()) {
            return new AudioAttributes.Builder().setInternalLegacyStreamType(notification.audioStreamType).build();
        }
        int i2 = notification.audioStreamType;
        if (i2 == -1) {
            return audioAttributes;
        }
        Log.w(TAG, String.format("Invalid stream type: %d", Integer.valueOf(i2)));
        return audioAttributes;
    }

    private int calculateInitialImportance() {
        Notification notification = getSbn().getNotification();
        int importance = getChannel().getImportance();
        boolean z = true;
        this.mInitialImportanceExplanationCode = getChannel().hasUserSetImportance() ? 2 : 1;
        if ((notification.flags & 128) != 0) {
            notification.priority = 2;
        }
        int clamp = NotificationManagerService.clamp(notification.priority, -2, 2);
        notification.priority = clamp;
        int i = clamp != -2 ? clamp != -1 ? (clamp == 0 || !(clamp == 1 || clamp == 2)) ? 3 : 4 : 2 : 1;
        NotificationUsageStats.SingleNotificationStats singleNotificationStats = this.stats;
        singleNotificationStats.requestedImportance = i;
        if (this.mSound == null && this.mVibration == null) {
            z = false;
        }
        singleNotificationStats.isNoisy = z;
        if (this.mPreChannelsNotification && (importance == -1000 || !getChannel().hasUserSetImportance())) {
            boolean z2 = this.stats.isNoisy;
            int i2 = (z2 || i <= 2) ? i : 2;
            importance = notification.fullScreenIntent != null ? 4 : (!z2 || i2 >= 3) ? i2 : 3;
            this.mInitialImportanceExplanationCode = 5;
        }
        int adjustImportanceForPackage = this.mNRWrapper.getNotificationRecordExt().adjustImportanceForPackage(importance);
        this.stats.naturalImportance = adjustImportanceForPackage;
        return adjustImportanceForPackage;
    }

    public void copyRankingInformation(NotificationRecord notificationRecord) {
        this.mContactAffinity = notificationRecord.mContactAffinity;
        this.mRecentlyIntrusive = notificationRecord.mRecentlyIntrusive;
        this.mPackagePriority = notificationRecord.mPackagePriority;
        this.mPackageVisibility = notificationRecord.mPackageVisibility;
        this.mIntercept = notificationRecord.mIntercept;
        this.mHidden = notificationRecord.mHidden;
        this.mRankingTimeMs = calculateRankingTimeMs(notificationRecord.getRankingTimeMs());
        this.mCreationTimeMs = notificationRecord.mCreationTimeMs;
        this.mVisibleSinceMs = notificationRecord.mVisibleSinceMs;
        if (notificationRecord.getSbn().getOverrideGroupKey() == null || getSbn().isAppGroup()) {
            return;
        }
        getSbn().setOverrideGroupKey(notificationRecord.getSbn().getOverrideGroupKey());
    }

    public Notification getNotification() {
        return getSbn().getNotification();
    }

    public int getFlags() {
        return getSbn().getNotification().flags;
    }

    public UserHandle getUser() {
        return getSbn().getUser();
    }

    public String getKey() {
        return getSbn().getKey();
    }

    public int getUserId() {
        return getSbn().getUserId();
    }

    public int getUid() {
        return getSbn().getUid();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(ProtoOutputStream protoOutputStream, long j, boolean z, int i) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, getSbn().getKey());
        protoOutputStream.write(1159641169922L, i);
        if (getChannel() != null) {
            protoOutputStream.write(1138166333444L, getChannel().getId());
        }
        protoOutputStream.write(1133871366152L, getLight() != null);
        protoOutputStream.write(1133871366151L, getVibration() != null);
        protoOutputStream.write(1120986464259L, getSbn().getNotification().flags);
        protoOutputStream.write(1138166333449L, getGroupKey());
        protoOutputStream.write(1172526071818L, getImportance());
        if (getSound() != null) {
            protoOutputStream.write(1138166333445L, getSound().toString());
        }
        if (getAudioAttributes() != null) {
            getAudioAttributes().dumpDebug(protoOutputStream, 1146756268038L);
        }
        protoOutputStream.write(1138166333451L, getSbn().getPackageName());
        protoOutputStream.write(1138166333452L, getSbn().getOpPkg());
        protoOutputStream.end(start);
    }

    String formatRemoteViews(RemoteViews remoteViews) {
        return remoteViews == null ? "null" : String.format("%s/0x%08x (%d bytes): %s", remoteViews.getPackage(), Integer.valueOf(remoteViews.getLayoutId()), Integer.valueOf(remoteViews.estimateMemoryUsage()), remoteViews.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NeverCompile
    public void dump(PrintWriter printWriter, String str, Context context, boolean z) {
        Notification notification = getSbn().getNotification();
        printWriter.println(str + this);
        String str2 = str + "  ";
        printWriter.println(str2 + "uid=" + getSbn().getUid() + " userId=" + getSbn().getUserId());
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("opPkg=");
        sb.append(getSbn().getOpPkg());
        printWriter.println(sb.toString());
        printWriter.println(str2 + "icon=" + notification.getSmallIcon());
        printWriter.println(str2 + "flags=0x" + Integer.toHexString(notification.flags));
        printWriter.println(str2 + "originalFlags=0x" + Integer.toHexString(this.mOriginalFlags));
        printWriter.println(str2 + "pri=" + notification.priority);
        printWriter.println(str2 + "key=" + getSbn().getKey());
        printWriter.println(str2 + "seen=" + this.mStats.hasSeen());
        printWriter.println(str2 + "groupKey=" + getGroupKey());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append("notification=");
        printWriter.println(sb2.toString());
        dumpNotification(printWriter, str2 + str2, notification, z);
        printWriter.println(str2 + "publicNotification=");
        dumpNotification(printWriter, str2 + str2, notification.publicVersion, z);
        printWriter.println(str2 + "stats=" + this.stats.toString());
        printWriter.println(str2 + "mContactAffinity=" + this.mContactAffinity);
        printWriter.println(str2 + "mRecentlyIntrusive=" + this.mRecentlyIntrusive);
        printWriter.println(str2 + "mPackagePriority=" + this.mPackagePriority);
        printWriter.println(str2 + "mPackageVisibility=" + this.mPackageVisibility);
        printWriter.println(str2 + "mSystemImportance=" + NotificationListenerService.Ranking.importanceToString(this.mSystemImportance));
        printWriter.println(str2 + "mAsstImportance=" + NotificationListenerService.Ranking.importanceToString(this.mAssistantImportance));
        printWriter.println(str2 + "mImportance=" + NotificationListenerService.Ranking.importanceToString(this.mImportance));
        printWriter.println(str2 + "mImportanceExplanation=" + ((Object) getImportanceExplanation()));
        printWriter.println(str2 + "mProposedImportance=" + NotificationListenerService.Ranking.importanceToString(this.mProposedImportance));
        printWriter.println(str2 + "mIsAppImportanceLocked=" + this.mIsAppImportanceLocked);
        printWriter.println(str2 + "mSensitiveContent=" + this.mSensitiveContent);
        printWriter.println(str2 + "mIntercept=" + this.mIntercept);
        printWriter.println(str2 + "mHidden==" + this.mHidden);
        printWriter.println(str2 + "mGlobalSortKey=" + this.mGlobalSortKey);
        printWriter.println(str2 + "mRankingTimeMs=" + this.mRankingTimeMs);
        printWriter.println(str2 + "mCreationTimeMs=" + this.mCreationTimeMs);
        printWriter.println(str2 + "mVisibleSinceMs=" + this.mVisibleSinceMs);
        printWriter.println(str2 + "mUpdateTimeMs=" + this.mUpdateTimeMs);
        printWriter.println(str2 + "mInterruptionTimeMs=" + this.mInterruptionTimeMs);
        printWriter.println(str2 + "mSuppressedVisualEffects= " + this.mSuppressedVisualEffects);
        if (this.mPreChannelsNotification) {
            printWriter.println(str2 + String.format("defaults=0x%08x flags=0x%08x", Integer.valueOf(notification.defaults), Integer.valueOf(notification.flags)));
            printWriter.println(str2 + "n.sound=" + notification.sound);
            printWriter.println(str2 + "n.audioStreamType=" + notification.audioStreamType);
            printWriter.println(str2 + "n.audioAttributes=" + notification.audioAttributes);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(String.format("  led=0x%08x onMs=%d offMs=%d", Integer.valueOf(notification.ledARGB), Integer.valueOf(notification.ledOnMS), Integer.valueOf(notification.ledOffMS)));
            printWriter.println(sb3.toString());
            printWriter.println(str2 + "vibrate=" + Arrays.toString(notification.vibrate));
        }
        printWriter.println(str2 + "mSound= " + this.mSound);
        printWriter.println(str2 + "mVibration= " + this.mVibration);
        printWriter.println(str2 + "mAttributes= " + this.mAttributes);
        printWriter.println(str2 + "mLight= " + this.mLight);
        printWriter.println(str2 + "mShowBadge=" + this.mShowBadge);
        printWriter.println(str2 + "mColorized=" + notification.isColorized());
        printWriter.println(str2 + "mAllowBubble=" + this.mAllowBubble);
        printWriter.println(str2 + "isBubble=" + notification.isBubbleNotification());
        printWriter.println(str2 + "mIsInterruptive=" + this.mIsInterruptive);
        printWriter.println(str2 + "effectiveNotificationChannel=" + getChannel());
        if (getPeopleOverride() != null) {
            printWriter.println(str2 + "overridePeople= " + TextUtils.join(",", getPeopleOverride()));
        }
        if (getSnoozeCriteria() != null) {
            printWriter.println(str2 + "snoozeCriteria=" + TextUtils.join(",", getSnoozeCriteria()));
        }
        printWriter.println(str2 + "mAdjustments=" + this.mAdjustments);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append("shortcut=");
        sb4.append(notification.getShortcutId());
        sb4.append(" found valid? ");
        sb4.append(this.mShortcutInfo != null);
        printWriter.println(sb4.toString());
        printWriter.println(str2 + "mUserVisOverride=" + getPackageVisibilityOverride());
    }

    private void dumpNotification(PrintWriter printWriter, String str, Notification notification, boolean z) {
        if (notification == null) {
            printWriter.println(str + "None");
            return;
        }
        printWriter.println(str + "fullscreenIntent=" + notification.fullScreenIntent);
        printWriter.println(str + "contentIntent=" + notification.contentIntent);
        printWriter.println(str + "deleteIntent=" + notification.deleteIntent);
        printWriter.println(str + "number=" + notification.number);
        printWriter.println(str + "groupAlertBehavior=" + notification.getGroupAlertBehavior());
        printWriter.println(str + "when=" + notification.when);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("tickerText=");
        printWriter.print(sb.toString());
        if (!TextUtils.isEmpty(notification.tickerText)) {
            String charSequence = notification.tickerText.toString();
            if (z) {
                printWriter.print(charSequence.length() > 16 ? charSequence.substring(0, 8) : "");
                printWriter.println("...");
            } else {
                printWriter.println(charSequence);
            }
        } else {
            printWriter.println("null");
        }
        printWriter.println(str + "vis=" + notification.visibility);
        printWriter.println(str + "contentView=" + formatRemoteViews(notification.contentView));
        printWriter.println(str + "bigContentView=" + formatRemoteViews(notification.bigContentView));
        printWriter.println(str + "headsUpContentView=" + formatRemoteViews(notification.headsUpContentView));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(String.format("color=0x%08x", Integer.valueOf(notification.color)));
        printWriter.println(sb2.toString());
        printWriter.println(str + "timeout=" + TimeUtils.formatForLogging(notification.getTimeoutAfter()));
        Notification.Action[] actionArr = notification.actions;
        if (actionArr != null && actionArr.length > 0) {
            printWriter.println(str + "actions={");
            int length = notification.actions.length;
            for (int i = 0; i < length; i++) {
                Notification.Action action = notification.actions[i];
                if (action != null) {
                    Object[] objArr = new Object[4];
                    objArr[0] = str;
                    objArr[1] = Integer.valueOf(i);
                    objArr[2] = action.title;
                    PendingIntent pendingIntent = action.actionIntent;
                    objArr[3] = pendingIntent == null ? "null" : pendingIntent.toString();
                    printWriter.println(String.format("%s    [%d] \"%s\" -> %s", objArr));
                }
            }
            printWriter.println(str + "  }");
        }
        Bundle bundle = notification.extras;
        if (bundle == null || bundle.size() <= 0) {
            return;
        }
        printWriter.println(str + "extras={");
        for (String str2 : notification.extras.keySet()) {
            printWriter.print(str + "    " + str2 + "=");
            Object obj = notification.extras.get(str2);
            if (obj == null) {
                printWriter.println("null");
            } else {
                printWriter.print(obj.getClass().getSimpleName());
                if (z && (obj instanceof CharSequence) && shouldRedactStringExtra(str2)) {
                    printWriter.print(String.format(" [length=%d]", Integer.valueOf(((CharSequence) obj).length())));
                } else if (obj instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) obj;
                    printWriter.print(String.format(" (%dx%d)", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight())));
                } else if (obj.getClass().isArray()) {
                    int length2 = Array.getLength(obj);
                    printWriter.print(" (" + length2 + ")");
                    if (!z) {
                        for (int i2 = 0; i2 < length2; i2++) {
                            printWriter.println();
                            printWriter.print(String.format("%s      [%d] %s", str, Integer.valueOf(i2), String.valueOf(Array.get(obj, i2))));
                        }
                    }
                } else {
                    printWriter.print(" (" + String.valueOf(obj) + ")");
                }
                printWriter.println();
            }
        }
        printWriter.println(str + "}");
    }

    private boolean shouldRedactStringExtra(String str) {
        if (str == null) {
            return true;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1349298919:
                if (str.equals("android.template")) {
                    c = 0;
                    break;
                }
                break;
            case -330858995:
                if (str.equals("android.substName")) {
                    c = 1;
                    break;
                }
                break;
            case 1258919194:
                if (str.equals("android.support.v4.app.extra.COMPAT_TEMPLATE")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                return false;
            default:
                return true;
        }
    }

    public final String toString() {
        return String.format("NotificationRecord(0x%08x: pkg=%s user=%s id=%d tag=%s importance=%d key=%s: %s)", Integer.valueOf(System.identityHashCode(this)), getSbn().getPackageName(), getSbn().getUser(), Integer.valueOf(getSbn().getId()), getSbn().getTag(), Integer.valueOf(this.mImportance), getSbn().getKey(), getSbn().getNotification());
    }

    public boolean hasAdjustment(String str) {
        synchronized (this.mAdjustments) {
            Iterator<Adjustment> it = this.mAdjustments.iterator();
            while (it.hasNext()) {
                if (it.next().getSignals().containsKey(str)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void addAdjustment(Adjustment adjustment) {
        synchronized (this.mAdjustments) {
            this.mAdjustments.add(adjustment);
        }
    }

    public void applyAdjustments() {
        System.currentTimeMillis();
        synchronized (this.mAdjustments) {
            for (Adjustment adjustment : this.mAdjustments) {
                Bundle signals = adjustment.getSignals();
                if (signals.containsKey("key_people")) {
                    ArrayList<String> stringArrayList = adjustment.getSignals().getStringArrayList("key_people");
                    setPeopleOverride(stringArrayList);
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_people", stringArrayList.toString());
                }
                if (signals.containsKey("key_snooze_criteria")) {
                    ArrayList<SnoozeCriterion> parcelableArrayList = adjustment.getSignals().getParcelableArrayList("key_snooze_criteria", SnoozeCriterion.class);
                    setSnoozeCriteria(parcelableArrayList);
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_snooze_criteria", parcelableArrayList.toString());
                }
                if (signals.containsKey("key_group_key")) {
                    String string = adjustment.getSignals().getString("key_group_key");
                    setOverrideGroupKey(string);
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_group_key", string);
                }
                if (signals.containsKey("key_user_sentiment") && !this.mIsAppImportanceLocked && (getChannel().getUserLockedFields() & 4) == 0) {
                    setUserSentiment(adjustment.getSignals().getInt("key_user_sentiment", 0));
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_user_sentiment", Integer.toString(getUserSentiment()));
                }
                if (signals.containsKey("key_contextual_actions")) {
                    setSystemGeneratedSmartActions(signals.getParcelableArrayList("key_contextual_actions", Notification.Action.class));
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_contextual_actions", getSystemGeneratedSmartActions().toString());
                }
                if (signals.containsKey("key_text_replies")) {
                    setSmartReplies(signals.getCharSequenceArrayList("key_text_replies"));
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_text_replies", getSmartReplies().toString());
                }
                if (signals.containsKey("key_importance")) {
                    int min = Math.min(4, Math.max(JobSchedulerShellCommand.CMD_ERR_NO_PACKAGE, signals.getInt("key_importance")));
                    setAssistantImportance(min);
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_importance", Integer.toString(min));
                }
                if (signals.containsKey("key_ranking_score")) {
                    this.mRankingScore = signals.getFloat("key_ranking_score");
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_ranking_score", Float.toString(this.mRankingScore));
                }
                if (signals.containsKey("key_not_conversation")) {
                    this.mIsNotConversationOverride = signals.getBoolean("key_not_conversation");
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_not_conversation", Boolean.toString(this.mIsNotConversationOverride));
                }
                if (signals.containsKey("key_importance_proposal")) {
                    this.mProposedImportance = signals.getInt("key_importance_proposal");
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_importance_proposal", Integer.toString(this.mProposedImportance));
                }
                if (signals.containsKey("key_sensitive_content")) {
                    this.mSensitiveContent = signals.getBoolean("key_sensitive_content");
                    EventLogTags.writeNotificationAdjusted(getKey(), "key_sensitive_content", Boolean.toString(this.mSensitiveContent));
                }
                if (!signals.isEmpty() && adjustment.getIssuer() != null) {
                    this.mAdjustmentIssuer = adjustment.getIssuer();
                }
            }
            this.mAdjustments.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getAdjustmentIssuer() {
        return this.mAdjustmentIssuer;
    }

    public void setIsAppImportanceLocked(boolean z) {
        this.mIsAppImportanceLocked = z;
        calculateUserSentiment();
    }

    public void setContactAffinity(float f) {
        this.mContactAffinity = f;
    }

    public float getContactAffinity() {
        return this.mContactAffinity;
    }

    public void setRecentlyIntrusive(boolean z) {
        this.mRecentlyIntrusive = z;
        if (z) {
            this.mLastIntrusive = System.currentTimeMillis();
        }
    }

    public boolean isRecentlyIntrusive() {
        return this.mRecentlyIntrusive;
    }

    public long getLastIntrusive() {
        return this.mLastIntrusive;
    }

    public void setPackagePriority(int i) {
        this.mPackagePriority = i;
    }

    public int getPackagePriority() {
        return this.mPackagePriority;
    }

    public void setPackageVisibilityOverride(int i) {
        this.mPackageVisibility = i;
    }

    public int getPackageVisibilityOverride() {
        return this.mPackageVisibility;
    }

    private String getUserExplanation() {
        if (this.mUserExplanation == null) {
            this.mUserExplanation = this.mContext.getResources().getString(R.string.mediasize_iso_a0);
        }
        return this.mUserExplanation;
    }

    public void setSystemImportance(int i) {
        this.mSystemImportance = i;
        calculateImportance();
    }

    public void setAssistantImportance(int i) {
        this.mAssistantImportance = i;
    }

    public int getAssistantImportance() {
        return this.mAssistantImportance;
    }

    public void setImportanceFixed(boolean z) {
        this.mImportanceFixed = z;
    }

    public boolean isImportanceFixed() {
        return this.mImportanceFixed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void calculateImportance() {
        int i;
        this.mImportance = calculateInitialImportance();
        this.mImportanceExplanationCode = this.mInitialImportanceExplanationCode;
        if (!getChannel().hasUserSetImportance() && (i = this.mAssistantImportance) != -1000 && !this.mImportanceFixed) {
            this.mImportance = i;
            this.mImportanceExplanationCode = 3;
        }
        int i2 = this.mSystemImportance;
        if (i2 != -1000) {
            this.mImportance = i2;
            this.mImportanceExplanationCode = 4;
            this.mImportance = this.mNRWrapper.getNotificationRecordExt().adjustImportanceForPackage(this.mImportance);
        }
    }

    public int getImportance() {
        return this.mImportance;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInitialImportance() {
        return this.stats.naturalImportance;
    }

    public int getProposedImportance() {
        return this.mProposedImportance;
    }

    public boolean hasSensitiveContent() {
        return this.mSensitiveContent;
    }

    public float getRankingScore() {
        return this.mRankingScore;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getImportanceExplanationCode() {
        return this.mImportanceExplanationCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInitialImportanceExplanationCode() {
        return this.mInitialImportanceExplanationCode;
    }

    public CharSequence getImportanceExplanation() {
        int i = this.mImportanceExplanationCode;
        if (i == 1) {
            return "app";
        }
        if (i == 2) {
            return "user";
        }
        if (i == 3) {
            return "asst";
        }
        if (i == 4) {
            return "system";
        }
        if (i != 5) {
            return null;
        }
        return "app";
    }

    public boolean setIntercepted(boolean z) {
        this.mIntercept = z;
        this.mInterceptSet = true;
        return z;
    }

    public void setCriticality(int i) {
        this.mCriticality = i;
    }

    public int getCriticality() {
        return this.mCriticality;
    }

    public boolean isIntercepted() {
        return this.mIntercept;
    }

    public boolean hasInterceptBeenSet() {
        return this.mInterceptSet;
    }

    public boolean isNewEnoughForAlerting(long j) {
        return getFreshnessMs(j) <= 2000;
    }

    public void setHidden(boolean z) {
        this.mHidden = z;
    }

    public boolean isHidden() {
        return this.mHidden;
    }

    public boolean isForegroundService() {
        return (getFlags() & 64) != 0;
    }

    public void setPostSilently(boolean z) {
        this.mPostSilently = z;
    }

    public boolean shouldPostSilently() {
        return this.mPostSilently;
    }

    public void setSuppressedVisualEffects(int i) {
        this.mSuppressedVisualEffects = i;
    }

    public int getSuppressedVisualEffects() {
        return this.mSuppressedVisualEffects;
    }

    public boolean isCategory(String str) {
        return Objects.equals(getNotification().category, str);
    }

    public boolean isAudioAttributesUsage(int i) {
        AudioAttributes audioAttributes = this.mAttributes;
        return audioAttributes != null && audioAttributes.getUsage() == i;
    }

    public long getRankingTimeMs() {
        return this.mRankingTimeMs;
    }

    public int getFreshnessMs(long j) {
        return (int) (j - this.mUpdateTimeMs);
    }

    public int getLifespanMs(long j) {
        return (int) (j - this.mCreationTimeMs);
    }

    public int getExposureMs(long j) {
        long j2 = this.mVisibleSinceMs;
        if (j2 == 0) {
            return 0;
        }
        return (int) (j - j2);
    }

    public int getInterruptionMs(long j) {
        return (int) (j - this.mInterruptionTimeMs);
    }

    public long getUpdateTimeMs() {
        return this.mUpdateTimeMs;
    }

    public void setVisibility(boolean z, int i, int i2, NotificationRecordLogger notificationRecordLogger) {
        long currentTimeMillis = System.currentTimeMillis();
        this.mVisibleSinceMs = z ? currentTimeMillis : this.mVisibleSinceMs;
        this.stats.onVisibilityChanged(z);
        MetricsLogger.action(getLogMaker(currentTimeMillis).setCategory(128).setType(z ? 1 : 2).addTaggedData(798, Integer.valueOf(i)).addTaggedData(1395, Integer.valueOf(i2)));
        if (z) {
            setSeen();
            MetricsLogger.histogram(this.mContext, "note_freshness", getFreshnessMs(currentTimeMillis));
        }
        EventLogTags.writeNotificationVisibility(getKey(), z ? 1 : 0, getLifespanMs(currentTimeMillis), getFreshnessMs(currentTimeMillis), 0, i);
        notificationRecordLogger.logNotificationVisibility(this, z);
    }

    private long calculateRankingTimeMs(long j) {
        Notification notification = getNotification();
        long j2 = notification.when;
        if (j2 == 0 || j2 > getSbn().getPostTime()) {
            return j > 0 ? j : getSbn().getPostTime();
        }
        return notification.when;
    }

    public void setGlobalSortKey(String str) {
        this.mGlobalSortKey = str;
    }

    public String getGlobalSortKey() {
        return this.mGlobalSortKey;
    }

    public boolean isSeen() {
        return this.mStats.hasSeen();
    }

    public void setSeen() {
        this.mStats.setSeen();
        if (this.mTextChanged) {
            setInterruptive(true);
        }
    }

    public void setAuthoritativeRank(int i) {
        this.mAuthoritativeRank = i;
    }

    public int getAuthoritativeRank() {
        return this.mAuthoritativeRank;
    }

    public String getGroupKey() {
        return getSbn().getGroupKey();
    }

    public void setOverrideGroupKey(String str) {
        getSbn().setOverrideGroupKey(str);
    }

    public NotificationChannel getChannel() {
        return this.mChannel;
    }

    public boolean getIsAppImportanceLocked() {
        return this.mIsAppImportanceLocked;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateNotificationChannel(NotificationChannel notificationChannel) {
        if (notificationChannel != null) {
            this.mChannel = notificationChannel;
            calculateImportance();
            calculateUserSentiment();
        }
    }

    public void setShowBadge(boolean z) {
        this.mShowBadge = z;
    }

    public boolean canBubble() {
        return this.mAllowBubble;
    }

    public void setAllowBubble(boolean z) {
        this.mAllowBubble = z;
    }

    public boolean canShowBadge() {
        return this.mShowBadge;
    }

    public Light getLight() {
        return this.mLight;
    }

    public Uri getSound() {
        return this.mSound;
    }

    public VibrationEffect getVibration() {
        return this.mVibration;
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAttributes;
    }

    public ArrayList<String> getPeopleOverride() {
        return this.mPeopleOverride;
    }

    public void setInterruptive(boolean z) {
        this.mIsInterruptive = z;
        long currentTimeMillis = System.currentTimeMillis();
        this.mInterruptionTimeMs = z ? currentTimeMillis : this.mInterruptionTimeMs;
        if (z) {
            MetricsLogger.action(getLogMaker().setCategory(1501).setType(1).addTaggedData(1500, Integer.valueOf(getInterruptionMs(currentTimeMillis))));
            MetricsLogger.histogram(this.mContext, "note_interruptive", getInterruptionMs(currentTimeMillis));
        }
    }

    public void setAudiblyAlerted(boolean z) {
        this.mLastAudiblyAlertedMs = z ? System.currentTimeMillis() : -1L;
    }

    public void setTextChanged(boolean z) {
        this.mTextChanged = z;
    }

    public void setRecordedInterruption(boolean z) {
        this.mRecordedInterruption = z;
    }

    public boolean hasRecordedInterruption() {
        return this.mRecordedInterruption;
    }

    public boolean isInterruptive() {
        return this.mIsInterruptive;
    }

    public boolean isTextChanged() {
        return this.mTextChanged;
    }

    public long getLastAudiblyAlertedMs() {
        return this.mLastAudiblyAlertedMs;
    }

    protected void setPeopleOverride(ArrayList<String> arrayList) {
        this.mPeopleOverride = arrayList;
    }

    public ArrayList<SnoozeCriterion> getSnoozeCriteria() {
        return this.mSnoozeCriteria;
    }

    protected void setSnoozeCriteria(ArrayList<SnoozeCriterion> arrayList) {
        this.mSnoozeCriteria = arrayList;
    }

    private void calculateUserSentiment() {
        if ((getChannel().getUserLockedFields() & 4) != 0 || this.mIsAppImportanceLocked) {
            this.mUserSentiment = 1;
        }
    }

    private void setUserSentiment(int i) {
        this.mUserSentiment = i;
    }

    public int getUserSentiment() {
        return this.mUserSentiment;
    }

    public NotificationStats getStats() {
        return this.mStats;
    }

    public void recordExpanded() {
        this.mStats.setExpanded();
    }

    public void recordDirectReplied() {
        this.mStats.setDirectReplied();
    }

    public void recordDismissalSurface(int i) {
        this.mStats.setDismissalSurface(i);
    }

    public void recordDismissalSentiment(int i) {
        this.mStats.setDismissalSentiment(i);
    }

    public void recordSnoozed() {
        this.mStats.setSnoozed();
    }

    public void recordViewedSettings() {
        this.mStats.setViewedSettings();
    }

    public void setNumSmartRepliesAdded(int i) {
        this.mNumberOfSmartRepliesAdded = i;
    }

    public int getNumSmartRepliesAdded() {
        return this.mNumberOfSmartRepliesAdded;
    }

    public void setNumSmartActionsAdded(int i) {
        this.mNumberOfSmartActionsAdded = i;
    }

    public int getNumSmartActionsAdded() {
        return this.mNumberOfSmartActionsAdded;
    }

    public void setSuggestionsGeneratedByAssistant(boolean z) {
        this.mSuggestionsGeneratedByAssistant = z;
    }

    public boolean getSuggestionsGeneratedByAssistant() {
        return this.mSuggestionsGeneratedByAssistant;
    }

    public boolean getEditChoicesBeforeSending() {
        return this.mEditChoicesBeforeSending;
    }

    public void setEditChoicesBeforeSending(boolean z) {
        this.mEditChoicesBeforeSending = z;
    }

    public boolean hasSeenSmartReplies() {
        return this.mHasSeenSmartReplies;
    }

    public void setSeenSmartReplies(boolean z) {
        this.mHasSeenSmartReplies = z;
    }

    public boolean hasBeenVisiblyExpanded() {
        return this.stats.hasBeenVisiblyExpanded();
    }

    public boolean isFlagBubbleRemoved() {
        return this.mFlagBubbleRemoved;
    }

    public void setFlagBubbleRemoved(boolean z) {
        this.mFlagBubbleRemoved = z;
    }

    public void setSystemGeneratedSmartActions(ArrayList<Notification.Action> arrayList) {
        this.mSystemGeneratedSmartActions = arrayList;
    }

    public ArrayList<Notification.Action> getSystemGeneratedSmartActions() {
        return this.mSystemGeneratedSmartActions;
    }

    public void setSmartReplies(ArrayList<CharSequence> arrayList) {
        this.mSmartReplies = arrayList;
    }

    public ArrayList<CharSequence> getSmartReplies() {
        return this.mSmartReplies;
    }

    public boolean isProxied() {
        return !Objects.equals(getSbn().getPackageName(), getSbn().getOpPkg());
    }

    public int getNotificationType() {
        if (isConversation()) {
            return 1;
        }
        return getImportance() >= 3 ? 2 : 4;
    }

    public ArraySet<Uri> getGrantableUris() {
        return this.mGrantableUris;
    }

    protected void calculateGrantableUris() {
        NotificationChannel channel;
        Notification notification = getNotification();
        notification.visitUris(new Consumer() { // from class: com.android.server.notification.NotificationRecord$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NotificationRecord.this.lambda$calculateGrantableUris$0((Uri) obj);
            }
        });
        if (notification.getChannelId() == null || (channel = getChannel()) == null) {
            return;
        }
        visitGrantableUri(channel.getSound(), (channel.getUserLockedFields() & 32) != 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$calculateGrantableUris$0(Uri uri) {
        visitGrantableUri(uri, false, false);
    }

    private void visitGrantableUri(Uri uri, boolean z, boolean z2) {
        int uid;
        if (uri == null || !"content".equals(uri.getScheme()) || (uid = getSbn().getUid()) == 1000) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mUgmInternal.checkGrantUriPermission(uid, null, ContentProvider.getUriWithoutUserId(uri), 1, ContentProvider.getUserIdFromUri(uri, UserHandle.getUserId(uid)));
                if (this.mGrantableUris == null) {
                    this.mGrantableUris = new ArraySet<>();
                }
                this.mGrantableUris.add(uri);
            } catch (SecurityException e) {
                if (!z) {
                    if (z2) {
                        this.mSound = Settings.System.DEFAULT_NOTIFICATION_URI;
                        Log.w(TAG, "Replacing " + uri + " from " + uid + ": " + e.getMessage());
                    } else {
                        if (this.mTargetSdkVersion >= 28) {
                            throw e;
                        }
                        Log.w(TAG, "Ignoring " + uri + " from " + uid + ": " + e.getMessage());
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public LogMaker getLogMaker(long j) {
        LogMaker addTaggedData = getSbn().getLogMaker().addTaggedData(858, Integer.valueOf(this.mImportance)).addTaggedData(793, Integer.valueOf(getLifespanMs(j))).addTaggedData(795, Integer.valueOf(getFreshnessMs(j))).addTaggedData(794, Integer.valueOf(getExposureMs(j))).addTaggedData(1500, Integer.valueOf(getInterruptionMs(j)));
        int i = this.mImportanceExplanationCode;
        if (i != 0) {
            addTaggedData.addTaggedData(1688, Integer.valueOf(i));
            int i2 = this.mImportanceExplanationCode;
            if ((i2 == 3 || i2 == 4) && this.stats.naturalImportance != -1000) {
                addTaggedData.addTaggedData(1690, Integer.valueOf(this.mInitialImportanceExplanationCode));
                addTaggedData.addTaggedData(1689, Integer.valueOf(this.stats.naturalImportance));
            }
        }
        int i3 = this.mAssistantImportance;
        if (i3 != -1000) {
            addTaggedData.addTaggedData(1691, Integer.valueOf(i3));
        }
        String str = this.mAdjustmentIssuer;
        if (str != null) {
            addTaggedData.addTaggedData(1742, Integer.valueOf(str.hashCode()));
        }
        return addTaggedData;
    }

    public LogMaker getLogMaker() {
        return getLogMaker(System.currentTimeMillis());
    }

    public LogMaker getItemLogMaker() {
        return getLogMaker().setCategory(128);
    }

    public boolean hasUndecoratedRemoteView() {
        Notification notification = getNotification();
        return (notification.contentView != null || notification.bigContentView != null || notification.headsUpContentView != null) && !(notification.isStyle(Notification.DecoratedCustomViewStyle.class) || notification.isStyle(Notification.DecoratedMediaCustomViewStyle.class));
    }

    public void setShortcutInfo(ShortcutInfo shortcutInfo) {
        this.mShortcutInfo = shortcutInfo;
    }

    public ShortcutInfo getShortcutInfo() {
        return this.mShortcutInfo;
    }

    public void setHasSentValidMsg(boolean z) {
        this.mHasSentValidMsg = z;
    }

    public void userDemotedAppFromConvoSpace(boolean z) {
        this.mAppDemotedFromConvo = z;
    }

    public void setPkgAllowedAsConvo(boolean z) {
        this.mPkgAllowedAsConvo = z;
    }

    public boolean isConversation() {
        ShortcutInfo shortcutInfo;
        Notification notification = getNotification();
        if (this.mChannel.isDemoted() || this.mAppDemotedFromConvo || this.mIsNotConversationOverride) {
            return false;
        }
        if (!notification.isStyle(Notification.MessagingStyle.class)) {
            return this.mPkgAllowedAsConvo && this.mTargetSdkVersion < 30 && "msg".equals(getNotification().category);
        }
        if (!this.mNRWrapper.getNotificationRecordExt().getSupportConversation()) {
            return false;
        }
        if (this.mTargetSdkVersion >= 30 && notification.isStyle(Notification.MessagingStyle.class) && ((shortcutInfo = this.mShortcutInfo) == null || isOnlyBots(shortcutInfo.getPersons()))) {
            return false;
        }
        return (this.mHasSentValidMsg && this.mShortcutInfo == null) ? false : true;
    }

    private boolean isOnlyBots(Person[] personArr) {
        if (personArr == null || personArr.length == 0) {
            return false;
        }
        for (Person person : personArr) {
            if (!person.isBot()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StatusBarNotification getSbn() {
        return this.sbn;
    }

    public boolean rankingScoreMatches(float f) {
        return ((double) Math.abs(this.mRankingScore - f)) < 1.0E-4d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPendingLogUpdate(boolean z) {
        this.mPendingLogUpdate = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasPendingLogUpdate() {
        return this.mPendingLogUpdate;
    }

    public void mergePhoneNumbers(ArraySet<String> arraySet) {
        if (arraySet == null || arraySet.size() == 0) {
            return;
        }
        if (this.mPhoneNumbers == null) {
            this.mPhoneNumbers = new ArraySet<>();
        }
        this.mPhoneNumbers.addAll((ArraySet<? extends String>) arraySet);
    }

    public ArraySet<String> getPhoneNumbers() {
        return this.mPhoneNumbers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Light {
        public final int color;
        public final int offMs;
        public final int onMs;

        public Light(int i, int i2, int i3) {
            this.color = i;
            this.onMs = i2;
            this.offMs = i3;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || Light.class != obj.getClass()) {
                return false;
            }
            Light light = (Light) obj;
            return this.color == light.color && this.onMs == light.onMs && this.offMs == light.offMs;
        }

        public int hashCode() {
            return (((this.color * 31) + this.onMs) * 31) + this.offMs;
        }

        public String toString() {
            return "Light{color=" + this.color + ", onMs=" + this.onMs + ", offMs=" + this.offMs + '}';
        }
    }

    public INotificationRecordWrapper getWrapper() {
        return this.mNRWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class NotificationRecordWrapper implements INotificationRecordWrapper {
        private NotificationRecordWrapper() {
        }

        @Override // com.android.server.notification.INotificationRecordWrapper
        public INotificationRecordExt getNotificationRecordExt() {
            return NotificationRecord.this.mNotificationRecordExt;
        }
    }
}
