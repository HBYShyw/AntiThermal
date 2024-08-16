package com.android.server.notification;

import android.R;
import android.annotation.EnforcePermission;
import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AlarmManager;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.app.AutomaticZenRule;
import android.app.IActivityManager;
import android.app.INotificationManager;
import android.app.ITransientNotification;
import android.app.ITransientNotificationCallback;
import android.app.IUriGrantsManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationHistory;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.StatsManager;
import android.app.UriGrantsManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.app.backup.BackupManager;
import android.app.compat.CompatChanges;
import android.app.role.OnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.app.usage.UsageStatsManagerInternal;
import android.companion.ICompanionDeviceManager;
import android.content.AttributionSource;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ParceledListSlice;
import android.content.pm.ServiceInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutServiceInternal;
import android.content.pm.UserInfo;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioManagerInternal;
import android.media.IRingtonePlayer;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.DeviceIdleManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.TransactionTooLargeException;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.VibrationEffect;
import android.os.WorkSource;
import android.permission.PermissionManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.service.notification.Adjustment;
import android.service.notification.Condition;
import android.service.notification.ConversationChannelWrapper;
import android.service.notification.IConditionProvider;
import android.service.notification.INotificationListener;
import android.service.notification.IStatusBarNotificationHolder;
import android.service.notification.NotificationListenerFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.NotificationRankingUpdate;
import android.service.notification.NotificationStats;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.IntArray;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.StatsEvent;
import android.util.Xml;
import android.util.proto.ProtoOutputStream;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.compat.IPlatformCompat;
import com.android.internal.config.sysui.SystemUiSystemPropertiesFlags;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.SomeArgs;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.function.TriPredicate;
import com.android.internal.widget.LockPatternUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.DeviceIdleInternal;
import com.android.server.EventLogTags;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.job.JobSchedulerInternal;
import com.android.server.lights.LightsManager;
import com.android.server.lights.LogicalLight;
import com.android.server.notification.GroupHelper;
import com.android.server.notification.ManagedServices;
import com.android.server.notification.NotificationManagerService;
import com.android.server.notification.NotificationRecord;
import com.android.server.notification.NotificationRecordLogger;
import com.android.server.notification.ShortcutHelper;
import com.android.server.notification.SnoozeHelper;
import com.android.server.notification.SysUiStatsEvent;
import com.android.server.notification.ZenModeHelper;
import com.android.server.notification.toast.CustomToastRecord;
import com.android.server.notification.toast.TextToastRecord;
import com.android.server.notification.toast.ToastRecord;
import com.android.server.pm.DumpState;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.UserManagerInternal;
import com.android.server.policy.PermissionPolicyInternal;
import com.android.server.power.stats.BatteryStatsImpl;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.uri.UriGrantsManagerInternal;
import com.android.server.utils.PriorityDump;
import com.android.server.utils.Slogf;
import com.android.server.utils.quota.MultiRateLimiter;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.BackgroundActivityStartCallback;
import com.android.server.wm.WindowManagerInternal;
import com.android.server.zenmode.IZenModeManagerExt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import libcore.io.IoUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationManagerService extends SystemService {
    private static final String ATTR_VERSION = "version";
    private static final String BREATHE_LIGHT_DEFAULT_ALLOW_PERIOD = "07002330";
    private static final long CHANGE_BACKGROUND_CUSTOM_TOAST_BLOCK = 128611929;
    private static final int DB_VERSION = 1;
    static final float DEFAULT_MAX_NOTIFICATION_ENQUEUE_RATE = 5.0f;
    private static final int DEFAULT_NOTIFICATION_VIBRATION_STRENGTH = 1800;
    private static final long DELAY_FOR_ASSISTANT_TIME = 200;
    private static final int END_NOTIFICATION_VIBRATION_STRENGTH = 2400;
    private static final int EVENTLOG_ENQUEUE_STATUS_IGNORED = 2;
    private static final int EVENTLOG_ENQUEUE_STATUS_NEW = 0;
    private static final int EVENTLOG_ENQUEUE_STATUS_UPDATE = 1;
    private static final String EXTRA_KEY = "key";
    static final int FINISH_TOKEN_TIMEOUT = 11000;
    static final int INVALID_UID = -1;
    private static final long LIGHTS_OFF_DELAY = 7200000;
    private static final String LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG = "allow-secure-notifications-on-lockscreen";
    private static final String LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_VALUE = "value";
    static final int LONG_DELAY = 3500;
    static final int MATCHES_CALL_FILTER_CONTACTS_TIMEOUT_MS = 3000;
    static final float MATCHES_CALL_FILTER_TIMEOUT_AFFINITY = 1.0f;
    private static final int MAX_AMPLITUDE = 255;
    static final int MAX_PACKAGE_NOTIFICATIONS = 50;
    static final int MAX_PACKAGE_TOASTS = 5;
    static final int MESSAGE_DURATION_REACHED = 2;
    static final int MESSAGE_FINISH_TOKEN_TIMEOUT = 7;
    static final int MESSAGE_LISTENER_HINTS_CHANGED = 5;
    static final int MESSAGE_LISTENER_NOTIFICATION_FILTER_CHANGED = 6;
    static final int MESSAGE_ON_PACKAGE_CHANGED = 8;
    private static final int MESSAGE_RANKING_SORT = 1001;
    private static final int MESSAGE_RECONSIDER_RANKING = 1000;
    static final int MESSAGE_SEND_RANKING_UPDATE = 4;
    private static final long MIN_PACKAGE_OVERRATE_LOG_INTERVAL = 5000;
    private static final long NOTIFICATION_CANCELLATION_REASONS = 175319604;
    private static final int NOTIFICATION_INSTANCE_ID_MAX = 8192;
    private static final long NOTIFICATION_LOG_ASSISTANT_CANCEL = 195579280;
    private static final long NOTIFICATION_TRAMPOLINE_BLOCK = 167676448;
    private static final long NOTIFICATION_TRAMPOLINE_BLOCK_FOR_EXEMPT_ROLES = 227752274;
    private static final long RATE_LIMIT_TOASTS = 174840628;
    public static final int REPORT_REMOTE_VIEWS = 1;
    private static final int REQUEST_CODE_LIGHTS_PERIOD_TIMEOUT = 11;
    private static final int REQUEST_CODE_LIGHTS_TIMEOUT = 10;
    private static final int REQUEST_CODE_TIMEOUT = 1;
    static final String REVIEW_NOTIF_ACTION_CANCELED = "REVIEW_NOTIF_ACTION_CANCELED";
    static final String REVIEW_NOTIF_ACTION_DISMISS = "REVIEW_NOTIF_ACTION_DISMISS";
    static final String REVIEW_NOTIF_ACTION_REMIND = "REVIEW_NOTIF_ACTION_REMIND";
    static final int REVIEW_NOTIF_STATE_DISMISSED = 2;
    static final int REVIEW_NOTIF_STATE_RESHOWN = 3;
    static final int REVIEW_NOTIF_STATE_SHOULD_SHOW = 0;
    static final int REVIEW_NOTIF_STATE_UNKNOWN = -1;
    static final int REVIEW_NOTIF_STATE_USER_INTERACTED = 1;
    static final String ROOT_PKG = "root";
    private static final String SCHEME_TIMEOUT = "timeout";
    static final int SHORT_DELAY = 2000;
    static final long SNOOZE_UNTIL_UNSPECIFIED = -1;
    private static final int START_NOTIFICATION_VIBRATION_STRENGTH = 800;
    private static final String SYSTEM_NOTIFICATION_VIBRATION_INTENSITY = "notification_stepless_vibration_intensity";
    private static final String TAG_NOTIFICATION_POLICY = "notification-policy";
    static final String TOAST_QUOTA_TAG = "toast_quota_tag";
    private static final int USER_ID_MULTI_APP = 999;
    private AccessibilityManager mAccessibilityManager;
    private ActivityManager mActivityManager;
    private AlarmManager mAlarmManager;
    private TriPredicate<String, Integer, String> mAllowedManagedServicePackages;
    private IActivityManager mAm;
    private ActivityManagerInternal mAmi;
    private AppOpsManager mAppOps;
    private UsageStatsManagerInternal mAppUsageStats;
    private Archive mArchive;
    private NotificationAssistants mAssistants;
    private ActivityTaskManagerInternal mAtm;
    LogicalLight mAttentionLight;
    AudioManager mAudioManager;
    AudioManagerInternal mAudioManagerInternal;
    private int mAutoGroupAtCount;

    @GuardedBy({"mNotificationLock"})
    final ArrayMap<Integer, ArrayMap<String, String>> mAutobundledSummaries;
    private int mBeginHour;
    private int mBeginMinute;
    private String mBreathAllowPeriod;
    private Binder mCallNotificationToken;
    private int mCallState;
    private ICompanionDeviceManager mCompanionManager;
    private ConditionProviders mConditionProviders;
    private String mDefaultSearchSelectorPkg;
    private DeviceConfig.OnPropertiesChangedListener mDeviceConfigChangedListener;
    private DeviceIdleManager mDeviceIdleManager;
    private boolean mDisableNotificationEffects;
    private DevicePolicyManagerInternal mDpm;
    private List<ComponentName> mEffectsSuppressors;
    private int mEndHour;
    private int mEndMinute;

    @GuardedBy({"mNotificationLock"})
    final ArrayList<NotificationRecord> mEnqueuedNotifications;
    private SystemUiSystemPropertiesFlags.FlagResolver mFlagResolver;
    final IBinder mForegroundToken;
    private GroupHelper mGroupHelper;
    private WorkerHandler mHandler;
    boolean mHasLight;
    private NotificationHistoryManager mHistoryManager;
    private AudioAttributes mInCallNotificationAudioAttributes;
    private Uri mInCallNotificationUri;
    private float mInCallNotificationVolume;
    protected boolean mInCallStateOffHook;

    @GuardedBy({"mNotificationLock"})
    final ArrayMap<String, InlineReplyUriRecord> mInlineReplyRecordsByKey;
    private final BroadcastReceiver mIntentReceiver;
    private final NotificationManagerInternal mInternalService;
    private int mInterruptionFilter;
    private boolean mIsAutomotive;

    @GuardedBy({"mToastQueue"})
    private boolean mIsCurrentToastShown;
    private boolean mIsFlashing;
    private boolean mIsTelevision;
    private KeyguardManager mKeyguardManager;
    private long mLastOverRateLogTime;
    ArrayList<String> mLights;
    private int mListenerHints;
    private NotificationListeners mListeners;
    private final SparseArray<ArraySet<ComponentName>> mListenersDisablingEffects;
    protected final BroadcastReceiver mLocaleChangeReceiver;
    private boolean mLockScreenAllowSecureNotifications;
    private float mMaxPackageEnqueueRate;
    private MetricsLogger mMetricsLogger;
    private Set<String> mMsgPkgsAllowedAsConvos;
    private INotificationManagerServiceWrapper mNMSWrapper;
    private NotificationChannelLogger mNotificationChannelLogger;

    @VisibleForTesting
    final NotificationDelegate mNotificationDelegate;
    private boolean mNotificationEffectsEnabledForAutomotive;
    private InstanceIdSequence mNotificationInstanceIdSequence;
    private LogicalLight mNotificationLight;
    private final BroadcastReceiver mNotificationLightsOffReceiver;

    @GuardedBy({"mNotificationLock"})
    final ArrayList<NotificationRecord> mNotificationList;
    final Object mNotificationLock;
    boolean mNotificationPulseEnabled;
    private NotificationRecordLogger mNotificationRecordLogger;
    private final BroadcastReceiver mNotificationTimeoutReceiver;
    private int mNotificationVibrationIntensity;

    @GuardedBy({"mNotificationLock"})
    final ArrayMap<String, NotificationRecord> mNotificationsByKey;
    private final BroadcastReceiver mPackageIntentReceiver;
    private IPackageManager mPackageManager;
    private PackageManager mPackageManagerClient;
    PackageManagerInternal mPackageManagerInternal;
    private PermissionHelper mPermissionHelper;
    private PermissionManager mPermissionManager;
    private PermissionPolicyInternal mPermissionPolicyInternal;
    private IPlatformCompat mPlatformCompat;
    private AtomicFile mPolicyFile;
    private PostNotificationTrackerFactory mPostNotificationTrackerFactory;
    private PowerManager mPowerManager;

    @VisibleForTesting
    PreferencesHelper mPreferencesHelper;
    private StatsPullAtomCallbackImpl mPullAtomCallback;
    protected RankingHandler mRankingHandler;

    @VisibleForTesting
    RankingHelper mRankingHelper;
    private final HandlerThread mRankingThread;
    private final BroadcastReceiver mRestoreReceiver;
    private ReviewNotificationPermissionsReceiver mReviewNotificationPermissionsReceiver;
    private volatile RoleObserver mRoleObserver;
    private final SavePolicyFileRunnable mSavePolicyFile;
    private boolean mScreenLocked;
    boolean mScreenOn;

    @VisibleForTesting
    final IBinder mService;
    private SettingsObserver mSettingsObserver;
    private String mSettingsSpecialLights;
    private ShortcutHelper mShortcutHelper;
    private ShortcutHelper.ShortcutListener mShortcutListener;

    @VisibleForTesting
    protected boolean mShowReviewPermissionsNotification;
    protected SnoozeHelper mSnoozeHelper;
    private String mSoundNotificationKey;
    private StatsManager mStatsManager;
    StatusBarManagerInternal mStatusBar;
    private int mStripRemoteViewsSizeBytes;
    private StrongAuthTracker mStrongAuthTracker;
    final ArrayMap<String, NotificationRecord> mSummaryByGroupKey;
    boolean mSystemExemptFromDismissal;
    boolean mSystemReady;
    private TelecomManager mTelecomManager;
    final ArrayList<ToastRecord> mToastQueue;
    private MultiRateLimiter mToastRateLimiter;

    @GuardedBy({"mToastQueue"})
    private final Set<Integer> mToastRateLimitingDisabledUids;
    private IUriGrantsManager mUgm;
    private UriGrantsManagerInternal mUgmInternal;
    private UserManager mUm;
    private UserManagerInternal mUmInternal;
    private NotificationUsageStats mUsageStats;
    private UsageStatsManagerInternal mUsageStatsManagerInternal;
    private boolean mUseAttentionLight;
    private final ManagedServices.UserProfiles mUserProfiles;
    private String mVibrateNotificationKey;
    private VibratorHelper mVibratorHelper;
    private int mWarnRemoteViewsSizeBytes;
    private WindowManagerInternal mWindowManagerInternal;
    protected ZenModeHelper mZenModeHelper;
    private IZenModeManagerExt mZenModeManagerExt;
    public static final String TAG = "NotificationService";
    public static final boolean DBG = Log.isLoggable(TAG, 3);
    public static final boolean ENABLE_CHILD_NOTIFICATIONS = SystemProperties.getBoolean("debug.child_notifs", true);
    static final boolean DEBUG_INTERRUPTIVENESS = SystemProperties.getBoolean("debug.notification.interruptiveness", false);
    static final String[] ALLOWED_ADJUSTMENTS = {"key_people", "key_snooze_criteria", "key_user_sentiment", "key_contextual_actions", "key_text_replies", "key_importance", "key_importance_proposal", "key_sensitive_content", "key_ranking_score", "key_not_conversation"};
    static final String[] NON_BLOCKABLE_DEFAULT_ROLES = {"android.app.role.DIALER", "android.app.role.EMERGENCY"};
    private static final MultiRateLimiter.RateLimit[] TOAST_RATE_LIMITS = {MultiRateLimiter.RateLimit.create(3, Duration.ofSeconds(20)), MultiRateLimiter.RateLimit.create(5, Duration.ofSeconds(42)), MultiRateLimiter.RateLimit.create(6, Duration.ofSeconds(68))};
    private static final String ACTION_NOTIFICATION_TIMEOUT = NotificationManagerService.class.getSimpleName() + ".TIMEOUT";
    private static final String ACTION_NOTIFICATION_LIGHTS_TIMEOUT = NotificationManagerService.class.getSimpleName() + ".LIGHTS_TIMEOUT";
    private static final String ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT = NotificationManagerService.class.getSimpleName() + ".LIGHTS_PERIOD_TIMEOUT";
    private static final int MY_UID = Process.myUid();
    private static final int MY_PID = Process.myPid();
    private static final IBinder ALLOWLIST_TOKEN = new Binder();
    public static boolean mCustomizeBreathLight = false;
    private static ArrayList<String> mLightList = new ArrayList<>();
    private static INotificationManagerServiceExt mNMSExt = (INotificationManagerServiceExt) ExtLoader.type(INotificationManagerServiceExt.class).base((Object) null).create();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface FlagChecker {
        boolean apply(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int clamp(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    private int getRealUserId(int i) {
        if (i == -1) {
            return 0;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$handleGroupedNotificationLocked$9(int i) {
        return (i & 64) == 0 && (i & 32768) == 0;
    }

    int correctCategory(int i, int i2, int i3) {
        int i4 = i & i2;
        return (i4 == 0 || (i3 & i2) != 0) ? (i4 != 0 || (i3 & i2) == 0) ? i : i | i2 : i & (~i2);
    }

    public boolean hasFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Archive {
        final int mBufferSize;
        final Object mBufferLock = new Object();

        @GuardedBy({"mBufferLock"})
        final LinkedList<Pair<StatusBarNotification, Integer>> mBuffer = new LinkedList<>();
        final SparseArray<Boolean> mEnabled = new SparseArray<>();

        public Archive(int i) {
            this.mBufferSize = i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            int size = this.mBuffer.size();
            sb.append("Archive (");
            sb.append(size);
            sb.append(" notification");
            sb.append(size == 1 ? ")" : "s)");
            return sb.toString();
        }

        public void record(StatusBarNotification statusBarNotification, int i) {
            if (this.mEnabled.get(statusBarNotification.getNormalizedUserId(), Boolean.FALSE).booleanValue()) {
                synchronized (this.mBufferLock) {
                    if (this.mBuffer.size() == this.mBufferSize) {
                        this.mBuffer.removeFirst();
                    }
                    this.mBuffer.addLast(new Pair<>(statusBarNotification.cloneLight(), Integer.valueOf(i)));
                }
            }
        }

        public Iterator<Pair<StatusBarNotification, Integer>> descendingIterator() {
            return this.mBuffer.descendingIterator();
        }

        public StatusBarNotification[] getArray(final UserManager userManager, int i, boolean z) {
            StatusBarNotification[] statusBarNotificationArr;
            final ArrayList arrayList = new ArrayList();
            arrayList.add(-1);
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$Archive$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    NotificationManagerService.Archive.lambda$getArray$0(userManager, arrayList);
                }
            });
            synchronized (this.mBufferLock) {
                if (i == 0) {
                    i = this.mBufferSize;
                }
                ArrayList arrayList2 = new ArrayList();
                Iterator<Pair<StatusBarNotification, Integer>> descendingIterator = descendingIterator();
                int i2 = 0;
                while (descendingIterator.hasNext() && i2 < i) {
                    Pair<StatusBarNotification, Integer> next = descendingIterator.next();
                    if (((Integer) next.second).intValue() != 18 || z) {
                        if (arrayList.contains(Integer.valueOf(((StatusBarNotification) next.first).getUserId()))) {
                            i2++;
                            arrayList2.add((StatusBarNotification) next.first);
                        }
                    }
                }
                statusBarNotificationArr = (StatusBarNotification[]) arrayList2.toArray(new StatusBarNotification[arrayList2.size()]);
            }
            return statusBarNotificationArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getArray$0(UserManager userManager, ArrayList arrayList) throws Exception {
            for (int i : userManager.getProfileIds(ActivityManager.getCurrentUser(), false)) {
                arrayList.add(Integer.valueOf(i));
            }
        }

        public void updateHistoryEnabled(int i, boolean z) {
            this.mEnabled.put(i, Boolean.valueOf(z));
            if (i == 0 && NotificationManagerService.mNMSExt != null) {
                this.mEnabled.put(NotificationManagerService.mNMSExt.getMultiAppUserId(), Boolean.valueOf(z));
            }
            if (z) {
                return;
            }
            synchronized (this.mBufferLock) {
                for (int size = this.mBuffer.size() - 1; size >= 0; size--) {
                    if (i == ((StatusBarNotification) this.mBuffer.get(size).first).getNormalizedUserId()) {
                        this.mBuffer.remove(size);
                    }
                }
            }
        }

        public void removeChannelNotifications(String str, int i, String str2) {
            synchronized (this.mBufferLock) {
                Iterator<Pair<StatusBarNotification, Integer>> descendingIterator = descendingIterator();
                while (descendingIterator.hasNext()) {
                    Pair<StatusBarNotification, Integer> next = descendingIterator.next();
                    Object obj = next.first;
                    if (obj != null && i == ((StatusBarNotification) obj).getNormalizedUserId() && str != null && str.equals(((StatusBarNotification) next.first).getPackageName()) && ((StatusBarNotification) next.first).getNotification() != null && Objects.equals(str2, ((StatusBarNotification) next.first).getNotification().getChannelId())) {
                        descendingIterator.remove();
                    }
                }
            }
        }

        void dumpImpl(PrintWriter printWriter, DumpFilter dumpFilter) {
            synchronized (this.mBufferLock) {
                Iterator<Pair<StatusBarNotification, Integer>> descendingIterator = descendingIterator();
                int i = 0;
                while (true) {
                    if (!descendingIterator.hasNext()) {
                        break;
                    }
                    StatusBarNotification statusBarNotification = (StatusBarNotification) descendingIterator.next().first;
                    if (dumpFilter == null || dumpFilter.matches(statusBarNotification)) {
                        printWriter.println("    " + statusBarNotification);
                        i++;
                        if (i >= 5) {
                            if (descendingIterator.hasNext()) {
                                printWriter.println("    ...");
                            }
                        }
                    }
                }
            }
        }
    }

    void loadDefaultApprovedServices(int i) {
        this.mListeners.loadDefaultsFromConfig();
        this.mConditionProviders.loadDefaultsFromConfig();
        this.mAssistants.loadDefaultsFromConfig();
    }

    protected void allowDefaultApprovedServices(int i) {
        ArraySet<ComponentName> defaultComponents = this.mListeners.getDefaultComponents();
        for (int i2 = 0; i2 < defaultComponents.size(); i2++) {
            allowNotificationListener(i, defaultComponents.valueAt(i2));
        }
        ArraySet<String> defaultPackages = this.mConditionProviders.getDefaultPackages();
        for (int i3 = 0; i3 < defaultPackages.size(); i3++) {
            allowDndPackage(i, defaultPackages.valueAt(i3));
        }
        setDefaultAssistantForUser(i);
    }

    protected void migrateDefaultNAS() {
        for (UserInfo userInfo : this.mUm.getUsers()) {
            int identifier = userInfo.getUserHandle().getIdentifier();
            if (!isNASMigrationDone(identifier) && !userInfo.isManagedProfile() && !userInfo.isCloneProfile()) {
                if (this.mAssistants.getAllowedComponents(identifier).size() == 0) {
                    Slog.d(TAG, "NAS Migration: user set to none, disable new NAS setting");
                    setNASMigrationDone(identifier);
                    this.mAssistants.clearDefaults();
                } else {
                    Slog.d(TAG, "Reset NAS setting and migrate to new default");
                    resetAssistantUserSet(identifier);
                    this.mAssistants.resetDefaultAssistantsIfNecessary();
                }
            }
        }
    }

    @VisibleForTesting
    void setNASMigrationDone(int i) {
        for (int i2 : this.mUm.getProfileIds(i, false)) {
            Settings.Secure.putIntForUser(getContext().getContentResolver(), "nas_settings_updated", 1, i2);
        }
    }

    @VisibleForTesting
    boolean isNASMigrationDone(int i) {
        return Settings.Secure.getIntForUser(getContext().getContentResolver(), "nas_settings_updated", 0, i) == 1;
    }

    protected void setDefaultAssistantForUser(int i) {
        String property = DeviceConfig.getProperty("systemui", "nas_default_service");
        if (property != null) {
            ArraySet<ComponentName> queryPackageForServices = this.mAssistants.queryPackageForServices(property, 786432, i);
            for (int i2 = 0; i2 < queryPackageForServices.size(); i2++) {
                if (allowAssistant(i, queryPackageForServices.valueAt(i2))) {
                    return;
                }
            }
        }
        ArraySet<ComponentName> defaultComponents = this.mAssistants.getDefaultComponents();
        for (int i3 = 0; i3 < defaultComponents.size() && !allowAssistant(i, defaultComponents.valueAt(i3)); i3++) {
        }
    }

    @GuardedBy({"mNotificationLock"})
    protected void updateAutobundledSummaryFlags(int i, String str, int i2, boolean z) {
        String str2;
        NotificationRecord notificationRecord;
        ArrayMap<String, String> arrayMap = this.mAutobundledSummaries.get(Integer.valueOf(i));
        if (arrayMap == null || (str2 = arrayMap.get(str)) == null || (notificationRecord = this.mNotificationsByKey.get(str2)) == null || notificationRecord.getSbn().getNotification().flags == i2) {
            return;
        }
        notificationRecord.getSbn().getNotification().flags = i2;
        this.mHandler.post(new EnqueueNotificationRunnable(i, notificationRecord, z, this.mPostNotificationTrackerFactory.newTracker(null)));
    }

    private void allowDndPackage(int i, String str) {
        try {
            getBinderService().setNotificationPolicyAccessGrantedForUser(str, i, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void allowNotificationListener(int i, ComponentName componentName) {
        try {
            getBinderService().setNotificationListenerAccessGrantedForUser(componentName, i, true, true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean allowAssistant(int i, ComponentName componentName) {
        ArraySet<ComponentName> queryPackageForServices = this.mAssistants.queryPackageForServices(null, 786432, i);
        if (componentName == null || !queryPackageForServices.contains(componentName)) {
            return false;
        }
        setNotificationAssistantAccessGrantedForUserInternal(componentName, i, true, false);
        return true;
    }

    void readPolicyXml(InputStream inputStream, boolean z, int i) throws XmlPullParserException, NumberFormatException, IOException {
        TypedXmlPullParser resolvePullParser;
        if (z) {
            resolvePullParser = Xml.newFastPullParser();
            resolvePullParser.setInput(inputStream, StandardCharsets.UTF_8.name());
        } else {
            resolvePullParser = Xml.resolvePullParser(inputStream);
        }
        XmlUtils.beginDocument(resolvePullParser, TAG_NOTIFICATION_POLICY);
        UserInfo userInfo = this.mUmInternal.getUserInfo(i);
        boolean z2 = false;
        boolean z3 = z && (userInfo.isManagedProfile() || userInfo.isCloneProfile());
        int depth = resolvePullParser.getDepth();
        while (XmlUtils.nextElementWithin(resolvePullParser, depth)) {
            if ("zen".equals(resolvePullParser.getName())) {
                this.mZenModeHelper.readXml(resolvePullParser, z, i);
            } else if ("ranking".equals(resolvePullParser.getName())) {
                this.mPreferencesHelper.readXml(resolvePullParser, z, i);
            }
            if (this.mListeners.getConfig().xmlTag.equals(resolvePullParser.getName())) {
                if (!z3) {
                    this.mListeners.readXml(resolvePullParser, this.mAllowedManagedServicePackages, z, i);
                    z2 = true;
                }
            } else if (this.mAssistants.getConfig().xmlTag.equals(resolvePullParser.getName())) {
                if (!z3) {
                    this.mAssistants.readXml(resolvePullParser, this.mAllowedManagedServicePackages, z, i);
                    z2 = true;
                }
            } else if (this.mConditionProviders.getConfig().xmlTag.equals(resolvePullParser.getName())) {
                if (!z3) {
                    this.mConditionProviders.readXml(resolvePullParser, this.mAllowedManagedServicePackages, z, i);
                    z2 = true;
                }
            } else if ("snoozed-notifications".equals(resolvePullParser.getName())) {
                this.mSnoozeHelper.readXml(resolvePullParser, System.currentTimeMillis());
            }
            if (LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG.equals(resolvePullParser.getName()) && (!z || i == 0)) {
                this.mLockScreenAllowSecureNotifications = resolvePullParser.getAttributeBoolean((String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_VALUE, true);
            }
        }
        if (!z2) {
            this.mListeners.migrateToXml();
            this.mAssistants.migrateToXml();
            this.mConditionProviders.migrateToXml();
            handleSavePolicyFile();
        }
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().handleSavePolicyFile();
        }
        this.mAssistants.resetDefaultAssistantsIfNecessary();
    }

    @VisibleForTesting
    protected void loadPolicyFile() {
        if (DBG) {
            Slog.d(TAG, "loadPolicyFile");
        }
        synchronized (this.mPolicyFile) {
            FileInputStream fileInputStream = null;
            try {
                try {
                    try {
                        fileInputStream = this.mPolicyFile.openRead();
                        readPolicyXml(fileInputStream, false, -1);
                    } catch (FileNotFoundException unused) {
                        loadDefaultApprovedServices(0);
                        allowDefaultApprovedServices(0);
                    } catch (NumberFormatException e) {
                        Log.wtf(TAG, "Unable to parse notification policy", e);
                    }
                } catch (IOException e2) {
                    Log.wtf(TAG, "Unable to read notification policy", e2);
                } catch (XmlPullParserException e3) {
                    Log.wtf(TAG, "Unable to parse notification policy", e3);
                }
            } finally {
                IoUtils.closeQuietly(fileInputStream);
            }
        }
    }

    @VisibleForTesting
    protected void handleSavePolicyFile() {
        if (IoThread.getHandler().hasCallbacks(this.mSavePolicyFile)) {
            return;
        }
        IoThread.getHandler().postDelayed(this.mSavePolicyFile, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SavePolicyFileRunnable implements Runnable {
        private SavePolicyFileRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NotificationManagerService.DBG) {
                Slog.d(NotificationManagerService.TAG, "handleSavePolicyFile");
            }
            synchronized (NotificationManagerService.this.mPolicyFile) {
                try {
                    FileOutputStream startWrite = NotificationManagerService.this.mPolicyFile.startWrite();
                    try {
                        NotificationManagerService.this.writePolicyXml(startWrite, false, -1);
                        NotificationManagerService.this.mPolicyFile.finishWrite(startWrite);
                    } catch (Exception e) {
                        Slog.w(NotificationManagerService.TAG, "Failed to save policy file, restoring backup", e);
                        NotificationManagerService.this.mPolicyFile.failWrite(startWrite);
                    }
                } catch (IOException e2) {
                    Slog.w(NotificationManagerService.TAG, "Failed to save policy file", e2);
                    return;
                }
            }
            BackupManager.dataChanged(NotificationManagerService.this.getContext().getPackageName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writePolicyXml(OutputStream outputStream, boolean z, int i) throws IOException {
        TypedXmlSerializer resolveSerializer;
        if (z) {
            resolveSerializer = Xml.newFastSerializer();
            resolveSerializer.setOutput(outputStream, StandardCharsets.UTF_8.name());
        } else {
            resolveSerializer = Xml.resolveSerializer(outputStream);
        }
        resolveSerializer.startDocument((String) null, Boolean.TRUE);
        resolveSerializer.startTag((String) null, TAG_NOTIFICATION_POLICY);
        resolveSerializer.attributeInt((String) null, ATTR_VERSION, 1);
        this.mZenModeHelper.writeXml(resolveSerializer, z, null, i);
        this.mPreferencesHelper.writeXml(resolveSerializer, z, i);
        this.mListeners.writeXml(resolveSerializer, z, i);
        this.mAssistants.writeXml(resolveSerializer, z, i);
        this.mSnoozeHelper.writeXml(resolveSerializer);
        this.mConditionProviders.writeXml(resolveSerializer, z, i);
        if (!z || i == 0) {
            writeSecureNotificationsPolicy(resolveSerializer);
        }
        resolveSerializer.endTag((String) null, TAG_NOTIFICATION_POLICY);
        resolveSerializer.endDocument();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.notification.NotificationManagerService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 implements NotificationDelegate {
        AnonymousClass1() {
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void prepareForPossibleShutdown() {
            NotificationManagerService.this.mHistoryManager.triggerWriteToDisk();
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onSetDisabled(int i) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationManagerService.this.mDisableNotificationEffects = (i & DumpState.DUMP_DOMAIN_PREFERRED) != 0;
                if (NotificationManagerService.this.disableNotificationEffects(null) != null) {
                    if (NotificationManagerService.this.mNMSWrapper != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                        Slog.v(NotificationManagerService.TAG, "clearsound due to set disable by notification delegate.");
                    }
                    NotificationManagerService.this.clearSoundLocked();
                    NotificationManagerService.this.clearVibrateLocked();
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onClearAll(int i, int i2, int i3) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationManagerService.this.cancelAllLocked(i, i2, i3, 3, null, true);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationClick(int i, int i2, String str, NotificationVisibility notificationVisibility) {
            NotificationManagerService.this.exitIdle();
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord == null) {
                    Slog.w(NotificationManagerService.TAG, "No notification with key: " + str);
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                MetricsLogger.action(notificationRecord.getItemLogMaker().setType(4).addTaggedData(798, Integer.valueOf(notificationVisibility.rank)).addTaggedData(1395, Integer.valueOf(notificationVisibility.count)));
                NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.NOTIFICATION_CLICKED, notificationRecord);
                EventLogTags.writeNotificationClicked(str, notificationRecord.getLifespanMs(currentTimeMillis), notificationRecord.getFreshnessMs(currentTimeMillis), notificationRecord.getExposureMs(currentTimeMillis), notificationVisibility.rank, notificationVisibility.count);
                StatusBarNotification sbn = notificationRecord.getSbn();
                NotificationManagerService.this.cancelNotification(i, i2, sbn.getPackageName(), sbn.getTag(), sbn.getId(), 16, 36928, false, notificationRecord.getUserId(), 1, notificationVisibility.rank, notificationVisibility.count, null);
                notificationVisibility.recycle();
                NotificationManagerService.this.reportUserInteraction(notificationRecord);
                NotificationManagerService.this.mAssistants.notifyAssistantNotificationClicked(notificationRecord);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationActionClick(int i, int i2, String str, int i3, Notification.Action action, NotificationVisibility notificationVisibility, boolean z) {
            NotificationManagerService.this.exitIdle();
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord == null) {
                    Slog.w(NotificationManagerService.TAG, "No notification with key: " + str);
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                int i4 = 1;
                LogMaker addTaggedData = notificationRecord.getLogMaker(currentTimeMillis).setCategory(129).setType(4).setSubtype(i3).addTaggedData(798, Integer.valueOf(notificationVisibility.rank)).addTaggedData(1395, Integer.valueOf(notificationVisibility.count)).addTaggedData(1601, Integer.valueOf(action.isContextual() ? 1 : 0));
                if (!z) {
                    i4 = 0;
                }
                MetricsLogger.action(addTaggedData.addTaggedData(1600, Integer.valueOf(i4)).addTaggedData(1629, Integer.valueOf(notificationVisibility.location.toMetricsEventEnum())));
                NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.fromAction(i3, z, action.isContextual()), notificationRecord);
                EventLogTags.writeNotificationActionClicked(str, i3, notificationRecord.getLifespanMs(currentTimeMillis), notificationRecord.getFreshnessMs(currentTimeMillis), notificationRecord.getExposureMs(currentTimeMillis), notificationVisibility.rank, notificationVisibility.count);
                notificationVisibility.recycle();
                NotificationManagerService.this.reportUserInteraction(notificationRecord);
                NotificationManagerService.this.mAssistants.notifyAssistantActionClicked(notificationRecord, action, z);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationClear(int i, int i2, String str, int i3, String str2, int i4, int i5, NotificationVisibility notificationVisibility) {
            String str3;
            int i6;
            int i7;
            String str4;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str2);
                if (notificationRecord != null) {
                    notificationRecord.recordDismissalSurface(i4);
                    notificationRecord.recordDismissalSentiment(i5);
                    str3 = notificationRecord.getSbn().getTag();
                    i6 = notificationRecord.getSbn().getId();
                } else {
                    str3 = null;
                    i6 = 0;
                }
                i7 = i6;
                str4 = str3;
            }
            NotificationManagerService.this.cancelNotification(i, i2, str, str4, i7, 0, NotificationManagerService.this.mFlagResolver.isEnabled(SystemUiSystemPropertiesFlags.NotificationFlags.ALLOW_DISMISS_ONGOING) ? 8192 : 2, true, i3, 2, notificationVisibility.rank, notificationVisibility.count, null);
            notificationVisibility.recycle();
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onPanelRevealed(boolean z, int i) {
            MetricsLogger.visible(NotificationManagerService.this.getContext(), BatteryStatsImpl.ExternalStatsSync.UPDATE_ALL);
            MetricsLogger.histogram(NotificationManagerService.this.getContext(), "note_load", i);
            NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationPanelEvent.NOTIFICATION_PANEL_OPEN);
            EventLogTags.writeNotificationPanelRevealed(i);
            if (z) {
                clearEffects();
            }
            NotificationManagerService.this.mAssistants.onPanelRevealed(i);
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onPanelHidden() {
            MetricsLogger.hidden(NotificationManagerService.this.getContext(), BatteryStatsImpl.ExternalStatsSync.UPDATE_ALL);
            NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationPanelEvent.NOTIFICATION_PANEL_CLOSE);
            EventLogTags.writeNotificationPanelHidden();
            NotificationManagerService.this.mAssistants.onPanelHidden();
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void clearEffects() {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                if (NotificationManagerService.DBG) {
                    Slog.d(NotificationManagerService.TAG, "clearEffects");
                }
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                    Slog.v(NotificationManagerService.TAG, "clearEffects by notification delegate.");
                }
                NotificationManagerService.this.clearSoundLocked();
                NotificationManagerService.this.clearVibrateLocked();
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationError(int i, int i2, final String str, final String str2, final int i3, final int i4, final int i5, final String str3, int i6) {
            boolean z;
            boolean z2;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord findNotificationLocked = NotificationManagerService.this.findNotificationLocked(str, str2, i3, i6);
                z = (findNotificationLocked == null || (findNotificationLocked.getNotification().flags & 64) == 0) ? false : true;
                z2 = (findNotificationLocked == null || (findNotificationLocked.getNotification().flags & 32768) == 0) ? false : true;
            }
            NotificationManagerService.this.cancelNotification(i, i2, str, str2, i3, 0, 0, false, i6, 4, null);
            if (z || z2) {
                final int i7 = z ? 3 : 6;
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$1$$ExternalSyntheticLambda0
                    public final void runOrThrow() {
                        NotificationManagerService.AnonymousClass1.this.lambda$onNotificationError$0(i4, i5, str, str2, i3, str3, i7);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNotificationError$0(int i, int i2, String str, String str2, int i3, String str3, int i4) throws Exception {
            NotificationManagerService.this.mAm.crashApplicationWithType(i, i2, str, -1, "Bad notification(tag=" + str2 + ", id=" + i3 + ") posted from package " + str + ", crashing app(uid=" + i + ", pid=" + i2 + "): " + str3, true, i4);
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationVisibilityChanged(NotificationVisibility[] notificationVisibilityArr, NotificationVisibility[] notificationVisibilityArr2) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                for (NotificationVisibility notificationVisibility : notificationVisibilityArr) {
                    NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(notificationVisibility.key);
                    if (notificationRecord != null) {
                        if (!notificationRecord.isSeen()) {
                            if (NotificationManagerService.DBG) {
                                Slog.d(NotificationManagerService.TAG, "Marking notification as visible " + notificationVisibility.key);
                            }
                            NotificationManagerService.this.reportSeen(notificationRecord);
                        }
                        boolean z = true;
                        notificationRecord.setVisibility(true, notificationVisibility.rank, notificationVisibility.count, NotificationManagerService.this.mNotificationRecordLogger);
                        if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            NotificationManagerService.this.mNMSWrapper.getNMSExt().notifyRecordVisibilityChangedLocked(notificationRecord, true, notificationVisibility.rank, notificationVisibility.count);
                        }
                        NotificationManagerService.this.mAssistants.notifyAssistantVisibilityChangedLocked(notificationRecord, true);
                        if (notificationVisibility.location != NotificationVisibility.NotificationLocation.LOCATION_FIRST_HEADS_UP) {
                            z = false;
                        }
                        if (z || notificationRecord.hasBeenVisiblyExpanded()) {
                            NotificationManagerService.this.logSmartSuggestionsVisible(notificationRecord, notificationVisibility.location.toMetricsEventEnum());
                        }
                        NotificationManagerService.this.maybeRecordInterruptionLocked(notificationRecord);
                        notificationVisibility.recycle();
                    }
                }
                for (NotificationVisibility notificationVisibility2 : notificationVisibilityArr2) {
                    NotificationRecord notificationRecord2 = NotificationManagerService.this.mNotificationsByKey.get(notificationVisibility2.key);
                    if (notificationRecord2 != null) {
                        notificationRecord2.setVisibility(false, notificationVisibility2.rank, notificationVisibility2.count, NotificationManagerService.this.mNotificationRecordLogger);
                        if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            NotificationManagerService.this.mNMSWrapper.getNMSExt().notifyRecordVisibilityChangedLocked(notificationRecord2, false, notificationVisibility2.rank, notificationVisibility2.count);
                        }
                        NotificationManagerService.this.mAssistants.notifyAssistantVisibilityChangedLocked(notificationRecord2, false);
                        notificationVisibility2.recycle();
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationExpansionChanged(String str, boolean z, boolean z2, int i) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    notificationRecord.stats.onExpansionChanged(z, z2);
                    if (notificationRecord.hasBeenVisiblyExpanded()) {
                        NotificationManagerService.this.logSmartSuggestionsVisible(notificationRecord, i);
                    }
                    if (z) {
                        MetricsLogger.action(notificationRecord.getItemLogMaker().setType(z2 ? 3 : 14));
                        NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.fromExpanded(z2, z), notificationRecord);
                    }
                    if (z2 && z) {
                        notificationRecord.recordExpanded();
                        NotificationManagerService.this.reportUserInteraction(notificationRecord);
                    }
                    NotificationManagerService.this.mAssistants.notifyAssistantExpansionChangedLocked(notificationRecord.getSbn(), notificationRecord.getNotificationType(), z, z2);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationDirectReplied(String str) {
            NotificationManagerService.this.exitIdle();
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    notificationRecord.recordDirectReplied();
                    NotificationManagerService.this.mMetricsLogger.write(notificationRecord.getLogMaker().setCategory(1590).setType(4));
                    NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.NOTIFICATION_DIRECT_REPLIED, notificationRecord);
                    NotificationManagerService.this.reportUserInteraction(notificationRecord);
                    NotificationManagerService.this.mAssistants.notifyAssistantNotificationDirectReplyLocked(notificationRecord);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationSmartSuggestionsAdded(String str, int i, int i2, boolean z, boolean z2) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    notificationRecord.setNumSmartRepliesAdded(i);
                    notificationRecord.setNumSmartActionsAdded(i2);
                    notificationRecord.setSuggestionsGeneratedByAssistant(z);
                    notificationRecord.setEditChoicesBeforeSending(z2);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationSmartReplySent(String str, int i, CharSequence charSequence, int i2, boolean z) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    int i3 = 1;
                    LogMaker addTaggedData = notificationRecord.getLogMaker().setCategory(1383).setSubtype(i).addTaggedData(1600, Integer.valueOf(notificationRecord.getSuggestionsGeneratedByAssistant() ? 1 : 0)).addTaggedData(1629, Integer.valueOf(i2)).addTaggedData(1647, Integer.valueOf(notificationRecord.getEditChoicesBeforeSending() ? 1 : 0));
                    if (!z) {
                        i3 = 0;
                    }
                    NotificationManagerService.this.mMetricsLogger.write(addTaggedData.addTaggedData(1648, Integer.valueOf(i3)));
                    NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.NOTIFICATION_SMART_REPLIED, notificationRecord);
                    NotificationManagerService.this.reportUserInteraction(notificationRecord);
                    NotificationManagerService.this.mAssistants.notifyAssistantSuggestedReplySent(notificationRecord.getSbn(), notificationRecord.getNotificationType(), charSequence, notificationRecord.getSuggestionsGeneratedByAssistant());
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationSettingsViewed(String str) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    notificationRecord.recordViewedSettings();
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationBubbleChanged(String str, boolean z, int i) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    if (!z) {
                        notificationRecord.getNotification().flags &= -4097;
                        notificationRecord.setFlagBubbleRemoved(true);
                    } else {
                        notificationRecord.getNotification().flags |= 8;
                        notificationRecord.setFlagBubbleRemoved(false);
                        if (notificationRecord.getNotification().getBubbleMetadata() != null) {
                            notificationRecord.getNotification().getBubbleMetadata().setFlags(i);
                        }
                        NotificationManagerService.this.mHandler.post(new EnqueueNotificationRunnable(notificationRecord.getUser().getIdentifier(), notificationRecord, true, NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onBubbleMetadataFlagChanged(String str, int i) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord != null) {
                    Notification.BubbleMetadata bubbleMetadata = notificationRecord.getNotification().getBubbleMetadata();
                    if (bubbleMetadata == null) {
                        return;
                    }
                    if (i != bubbleMetadata.getFlags()) {
                        if (((bubbleMetadata.getFlags() ^ i) & 2) != 0) {
                            NotificationManagerService.this.clearEffectsLocked(str);
                        }
                        bubbleMetadata.setFlags(i);
                        notificationRecord.getNotification().flags |= 8;
                        NotificationManagerService.this.mHandler.post(new EnqueueNotificationRunnable(notificationRecord.getUser().getIdentifier(), notificationRecord, true, NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void grantInlineReplyUriPermission(String str, Uri uri, UserHandle userHandle, String str2, int i) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                InlineReplyUriRecord inlineReplyUriRecord = NotificationManagerService.this.mInlineReplyRecordsByKey.get(str);
                if (inlineReplyUriRecord == null) {
                    inlineReplyUriRecord = new InlineReplyUriRecord(NotificationManagerService.this.mUgmInternal.newUriPermissionOwner("INLINE_REPLY:" + str), userHandle, str2, str);
                    NotificationManagerService.this.mInlineReplyRecordsByKey.put(str, inlineReplyUriRecord);
                }
                IBinder permissionOwner = inlineReplyUriRecord.getPermissionOwner();
                int userId = inlineReplyUriRecord.getUserId();
                if (UserHandle.getUserId(i) != userId) {
                    try {
                        String[] packagesForUid = NotificationManagerService.this.mPackageManager.getPackagesForUid(i);
                        if (packagesForUid == null) {
                            Log.e(NotificationManagerService.TAG, "Cannot grant uri permission to unknown UID: " + i);
                        }
                        i = NotificationManagerService.this.mPackageManager.getPackageUid(packagesForUid[0], 0L, userId);
                    } catch (RemoteException e) {
                        Log.e(NotificationManagerService.TAG, "Cannot talk to package manager", e);
                    }
                }
                inlineReplyUriRecord.addUri(uri);
                NotificationManagerService.this.grantUriPermission(permissionOwner, uri, i, inlineReplyUriRecord.getPackageName(), userId);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void clearInlineReplyUriPermissions(String str, int i) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                InlineReplyUriRecord inlineReplyUriRecord = NotificationManagerService.this.mInlineReplyRecordsByKey.get(str);
                if (inlineReplyUriRecord != null) {
                    NotificationManagerService.this.destroyPermissionOwner(inlineReplyUriRecord.getPermissionOwner(), inlineReplyUriRecord.getUserId(), "INLINE_REPLY: " + inlineReplyUriRecord.getKey());
                    NotificationManagerService.this.mInlineReplyRecordsByKey.remove(str);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationFeedbackReceived(String str, Bundle bundle) {
            NotificationManagerService.this.exitIdle();
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                if (notificationRecord == null) {
                    if (NotificationManagerService.DBG) {
                        Slog.w(NotificationManagerService.TAG, "No notification with key: " + str);
                    }
                    return;
                }
                NotificationManagerService.this.mAssistants.notifyAssistantFeedbackReceived(notificationRecord, bundle);
            }
        }
    }

    @VisibleForTesting
    void logSmartSuggestionsVisible(NotificationRecord notificationRecord, int i) {
        if ((notificationRecord.getNumSmartRepliesAdded() > 0 || notificationRecord.getNumSmartActionsAdded() > 0) && !notificationRecord.hasSeenSmartReplies()) {
            notificationRecord.setSeenSmartReplies(true);
            this.mMetricsLogger.write(notificationRecord.getLogMaker().setCategory(1382).addTaggedData(1384, Integer.valueOf(notificationRecord.getNumSmartRepliesAdded())).addTaggedData(1599, Integer.valueOf(notificationRecord.getNumSmartActionsAdded())).addTaggedData(1600, Integer.valueOf(notificationRecord.getSuggestionsGeneratedByAssistant() ? 1 : 0)).addTaggedData(1629, Integer.valueOf(i)).addTaggedData(1647, Integer.valueOf(notificationRecord.getEditChoicesBeforeSending() ? 1 : 0)));
            this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.NOTIFICATION_SMART_REPLY_VISIBLE, notificationRecord);
        }
    }

    @GuardedBy({"mNotificationLock"})
    void clearSoundLocked() {
        this.mSoundNotificationKey = null;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            IRingtonePlayer ringtonePlayer = this.mAudioManager.getRingtonePlayer();
            if (ringtonePlayer != null) {
                ringtonePlayer.stopAsync();
            }
        } catch (RemoteException unused) {
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
    }

    @GuardedBy({"mNotificationLock"})
    void clearVibrateLocked() {
        this.mVibrateNotificationKey = null;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mVibratorHelper.cancelVibration();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void clearLightsLocked() {
        this.mLights.clear();
        updateLightsLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void clearEffectsLocked(String str) {
        if (str.equals(this.mSoundNotificationKey)) {
            clearSoundLocked();
        }
        if (str.equals(this.mVibrateNotificationKey)) {
            clearVibrateLocked();
        }
        if (this.mLights.remove(str)) {
            updateLightsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SettingsObserver extends ContentObserver {
        private final Uri LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS;
        private final Uri LOCK_SCREEN_SHOW_NOTIFICATIONS;
        private final Uri NOTIFICATION_BADGING_URI;
        private final Uri NOTIFICATION_BUBBLES_URI;
        private final Uri NOTIFICATION_CUSTOMIZE_BREATH_LIGHT;
        private final Uri NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME;
        private final Uri NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE;
        private final Uri NOTIFICATION_HISTORY_ENABLED;
        private final Uri NOTIFICATION_LIGHT_PULSE_URI;
        private final Uri NOTIFICATION_RATE_LIMIT_URI;
        private final Uri NOTIFICATION_SETTINGS_SPECIAL_LIGHTS;
        private final Uri NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI;
        private final Uri NOTIFICATION_VIBRATION_INTENSITY;

        SettingsObserver(Handler handler) {
            super(handler);
            this.NOTIFICATION_BADGING_URI = Settings.Secure.getUriFor("notification_badging");
            this.NOTIFICATION_BUBBLES_URI = Settings.Secure.getUriFor("notification_bubbles");
            this.NOTIFICATION_LIGHT_PULSE_URI = Settings.System.getUriFor("notification_light_pulse");
            this.NOTIFICATION_RATE_LIMIT_URI = Settings.Global.getUriFor("max_notification_enqueue_rate");
            this.NOTIFICATION_HISTORY_ENABLED = Settings.Secure.getUriFor("notification_history_enabled");
            this.NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI = Settings.Global.getUriFor("qs_media_controls");
            this.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS = Settings.Secure.getUriFor("lock_screen_allow_private_notifications");
            this.LOCK_SCREEN_SHOW_NOTIFICATIONS = Settings.Secure.getUriFor("lock_screen_show_notifications");
            this.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT = Settings.Global.getUriFor("customize_breath_light_mms");
            this.NOTIFICATION_SETTINGS_SPECIAL_LIGHTS = Settings.Secure.getUriFor("settings_special_lights");
            this.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME = Settings.Global.getUriFor("customize_breath_light_time");
            this.NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE = Settings.Global.getUriFor("zen_mode");
            this.NOTIFICATION_VIBRATION_INTENSITY = Settings.System.getUriFor(NotificationManagerService.SYSTEM_NOTIFICATION_VIBRATION_INTENSITY);
        }

        void observe() {
            ContentResolver contentResolver = NotificationManagerService.this.getContext().getContentResolver();
            contentResolver.registerContentObserver(this.NOTIFICATION_BADGING_URI, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_LIGHT_PULSE_URI, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_RATE_LIMIT_URI, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_BUBBLES_URI, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_HISTORY_ENABLED, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI, false, this, -1);
            contentResolver.registerContentObserver(this.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS, false, this, -1);
            contentResolver.registerContentObserver(this.LOCK_SCREEN_SHOW_NOTIFICATIONS, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_SETTINGS_SPECIAL_LIGHTS, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE, false, this, -1);
            contentResolver.registerContentObserver(this.NOTIFICATION_VIBRATION_INTENSITY, false, this, 0);
            update(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri, int i) {
            update(uri);
        }

        public void update(Uri uri) {
            ContentResolver contentResolver = NotificationManagerService.this.getContext().getContentResolver();
            if (uri == null || this.NOTIFICATION_LIGHT_PULSE_URI.equals(uri)) {
                boolean z = Settings.System.getIntForUser(contentResolver, "notification_light_pulse", 0, -2) != 0;
                NotificationManagerService notificationManagerService = NotificationManagerService.this;
                if (notificationManagerService.mNotificationPulseEnabled != z) {
                    notificationManagerService.mNotificationPulseEnabled = z;
                    Slog.d(NotificationManagerService.TAG, "pulseEnabled to " + z);
                    NotificationManagerService.this.updateNotificationPulse();
                }
            }
            if (uri == null || this.NOTIFICATION_RATE_LIMIT_URI.equals(uri)) {
                NotificationManagerService notificationManagerService2 = NotificationManagerService.this;
                notificationManagerService2.mMaxPackageEnqueueRate = Settings.Global.getFloat(contentResolver, "max_notification_enqueue_rate", notificationManagerService2.mMaxPackageEnqueueRate);
            }
            if (uri == null || this.NOTIFICATION_BADGING_URI.equals(uri)) {
                NotificationManagerService.this.mPreferencesHelper.updateBadgingEnabled();
            }
            if (uri == null || this.NOTIFICATION_BUBBLES_URI.equals(uri)) {
                NotificationManagerService.this.mPreferencesHelper.updateBubblesEnabled();
            }
            if (uri == null || this.NOTIFICATION_HISTORY_ENABLED.equals(uri)) {
                IntArray currentProfileIds = NotificationManagerService.this.mUserProfiles.getCurrentProfileIds();
                for (int i = 0; i < currentProfileIds.size(); i++) {
                    NotificationManagerService.this.mArchive.updateHistoryEnabled(currentProfileIds.get(i), Settings.Secure.getIntForUser(contentResolver, "notification_history_enabled", 0, currentProfileIds.get(i)) == 1);
                }
            }
            if (uri == null || this.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT.equals(uri)) {
                NotificationManagerService.mCustomizeBreathLight = Settings.Global.getInt(contentResolver, "customize_breath_light_mms", 0) == 1;
                Slog.d(NotificationManagerService.TAG, "BreathLight: " + NotificationManagerService.mCustomizeBreathLight);
                NotificationManagerService.this.scheduleLightsPeriod();
                NotificationManagerService.this.updateNotificationPulse();
            }
            if (uri == null || this.NOTIFICATION_SETTINGS_SPECIAL_LIGHTS.equals(uri)) {
                NotificationManagerService.this.mSettingsSpecialLights = Settings.Secure.getStringForUser(contentResolver, "settings_special_lights", -2);
                Slog.d(NotificationManagerService.TAG, "light list: " + NotificationManagerService.this.mSettingsSpecialLights);
                NotificationManagerService.this.updateLightList();
                NotificationManagerService.this.updateNotificationPulse();
            }
            if (uri == null || this.NOTIFICATION_CUSTOMIZE_BREATH_LIGHT_TIME.equals(uri)) {
                NotificationManagerService.this.mBreathAllowPeriod = Settings.Global.getString(contentResolver, "customize_breath_light_time");
                NotificationManagerService.this.updatePeriod();
                Slog.d(NotificationManagerService.TAG, "breath time: " + NotificationManagerService.this.mBreathAllowPeriod);
                NotificationManagerService.this.scheduleLightsPeriod();
                NotificationManagerService.this.updateNotificationPulse();
            }
            if (uri == null || this.NOTIFICATION_CUSTOMIZE_BREATH_ZEN_MODE.equals(uri)) {
                NotificationManagerService.this.updateNotificationPulse();
            }
            if (uri == null || this.NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI.equals(uri)) {
                NotificationManagerService.this.mPreferencesHelper.updateMediaNotificationFilteringEnabled();
            }
            if (uri == null || this.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS.equals(uri)) {
                NotificationManagerService.this.mPreferencesHelper.updateLockScreenPrivateNotifications();
            }
            if (uri == null || this.LOCK_SCREEN_SHOW_NOTIFICATIONS.equals(uri)) {
                NotificationManagerService.this.mPreferencesHelper.updateLockScreenShowNotifications();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class StrongAuthTracker extends LockPatternUtils.StrongAuthTracker {
        boolean mIsInLockDownMode;
        SparseBooleanArray mUserInLockDownMode;

        private boolean containsFlag(int i, int i2) {
            return (i & i2) != 0;
        }

        StrongAuthTracker(Context context) {
            super(context);
            this.mUserInLockDownMode = new SparseBooleanArray();
            this.mIsInLockDownMode = false;
        }

        public boolean isInLockDownMode(int i) {
            return this.mUserInLockDownMode.get(i, false);
        }

        public synchronized void onStrongAuthRequiredChanged(int i) {
            boolean containsFlag = containsFlag(getStrongAuthForUser(i), 32);
            if (containsFlag == isInLockDownMode(i)) {
                return;
            }
            if (containsFlag) {
                NotificationManagerService.this.cancelNotificationsWhenEnterLockDownMode(i);
            }
            this.mUserInLockDownMode.put(i, containsFlag);
            if (!containsFlag) {
                NotificationManagerService.this.postNotificationsWhenExitLockDownMode(i);
            }
        }
    }

    public NotificationManagerService(Context context) {
        this(context, new NotificationRecordLoggerImpl(), new InstanceIdSequence(8192));
    }

    @VisibleForTesting
    public NotificationManagerService(Context context, NotificationRecordLogger notificationRecordLogger, InstanceIdSequence instanceIdSequence) {
        super(context);
        this.mScreenLocked = true;
        this.mForegroundToken = new Binder();
        this.mRankingThread = new HandlerThread("ranker", 10);
        this.mHasLight = true;
        this.mListenersDisablingEffects = new SparseArray<>();
        this.mEffectsSuppressors = new ArrayList();
        this.mInterruptionFilter = 0;
        this.mScreenOn = true;
        this.mInCallStateOffHook = false;
        this.mCallNotificationToken = null;
        this.mNotificationLock = new Object();
        this.mNotificationList = new ArrayList<>();
        this.mNotificationsByKey = new ArrayMap<>();
        this.mInlineReplyRecordsByKey = new ArrayMap<>();
        this.mEnqueuedNotifications = new ArrayList<>();
        this.mAutobundledSummaries = new ArrayMap<>();
        this.mToastQueue = new ArrayList<>();
        this.mToastRateLimitingDisabledUids = new ArraySet();
        this.mSummaryByGroupKey = new ArrayMap<>();
        this.mIsCurrentToastShown = false;
        this.mLights = new ArrayList<>();
        this.mUserProfiles = new ManagedServices.UserProfiles();
        this.mLockScreenAllowSecureNotifications = true;
        this.mSystemExemptFromDismissal = false;
        this.mMaxPackageEnqueueRate = DEFAULT_MAX_NOTIFICATION_ENQUEUE_RATE;
        this.mSavePolicyFile = new SavePolicyFileRunnable();
        this.mMsgPkgsAllowedAsConvos = new HashSet();
        this.mNotificationDelegate = new AnonymousClass1();
        this.mLocaleChangeReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
                    SystemNotificationChannels.createAll(context2);
                    NotificationManagerService.this.mZenModeHelper.updateDefaultZenRules(Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
                    NotificationManagerService.this.mPreferencesHelper.onLocaleChanged(context2, ActivityManager.getCurrentUser());
                }
            }
        };
        this.mRestoreReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.os.action.SETTING_RESTORED".equals(intent.getAction())) {
                    try {
                        String stringExtra = intent.getStringExtra("setting_name");
                        String stringExtra2 = intent.getStringExtra("new_value");
                        int intExtra = intent.getIntExtra("restored_from_sdk_int", 0);
                        NotificationManagerService.this.mListeners.onSettingRestored(stringExtra, stringExtra2, intExtra, getSendingUserId());
                        NotificationManagerService.this.mConditionProviders.onSettingRestored(stringExtra, stringExtra2, intExtra, getSendingUserId());
                    } catch (Exception e) {
                        Slog.wtf(NotificationManagerService.TAG, "Cannot restore managed services from settings", e);
                    }
                }
            }
        };
        this.mNotificationTimeoutReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                NotificationRecord findNotificationByKeyLocked;
                String action = intent.getAction();
                if (action != null && NotificationManagerService.ACTION_NOTIFICATION_TIMEOUT.equals(action)) {
                    synchronized (NotificationManagerService.this.mNotificationLock) {
                        findNotificationByKeyLocked = NotificationManagerService.this.findNotificationByKeyLocked(intent.getStringExtra(NotificationManagerService.EXTRA_KEY));
                    }
                    if (findNotificationByKeyLocked != null) {
                        NotificationManagerService.this.cancelNotification(findNotificationByKeyLocked.getSbn().getUid(), findNotificationByKeyLocked.getSbn().getInitialPid(), findNotificationByKeyLocked.getSbn().getPackageName(), findNotificationByKeyLocked.getSbn().getTag(), findNotificationByKeyLocked.getSbn().getId(), 0, 32832, true, findNotificationByKeyLocked.getUserId(), 19, null);
                    }
                }
            }
        };
        this.mPackageIntentReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.5
            /* JADX WARN: Removed duplicated region for block: B:32:0x007b  */
            /* JADX WARN: Removed duplicated region for block: B:35:0x00a1  */
            /* JADX WARN: Removed duplicated region for block: B:38:0x0150  */
            /* JADX WARN: Removed duplicated region for block: B:41:0x0155  */
            /* JADX WARN: Removed duplicated region for block: B:51:0x01c8  */
            /* JADX WARN: Removed duplicated region for block: B:65:0x00ae  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onReceive(Context context2, Intent intent) {
                boolean z;
                boolean z2;
                boolean z3;
                String schemeSpecificPart;
                String[] strArr;
                int[] iArr;
                int[] intArrayExtra;
                boolean z4;
                int[] iArr2;
                boolean z5;
                boolean z6;
                int i;
                int i2;
                int i3;
                boolean z7;
                int i4;
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                    z = false;
                    z2 = false;
                } else {
                    z = action.equals("android.intent.action.PACKAGE_REMOVED");
                    if (!z && !action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                        z2 = action.equals("android.intent.action.PACKAGE_CHANGED");
                        if (z2) {
                            z3 = false;
                        } else {
                            boolean equals = action.equals("android.intent.action.QUERY_PACKAGE_RESTART");
                            if (!equals && !action.equals("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE") && !action.equals("android.intent.action.PACKAGES_SUSPENDED") && !action.equals("android.intent.action.PACKAGES_UNSUSPENDED") && !action.equals("android.intent.action.DISTRACTING_PACKAGES_CHANGED")) {
                                return;
                            } else {
                                z3 = equals;
                            }
                        }
                        int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        boolean z8 = true;
                        boolean z9 = (z || intent.getBooleanExtra("android.intent.extra.REPLACING", false)) ? false : true;
                        if (NotificationManagerService.DBG) {
                            Slog.i(NotificationManagerService.TAG, "action=" + action + " removing=" + z9);
                        }
                        if (!action.equals("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE")) {
                            strArr = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                            iArr = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                        } else {
                            if (action.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                                strArr = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                                iArr2 = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                                z4 = false;
                                z5 = true;
                                z8 = false;
                            } else if (action.equals("android.intent.action.PACKAGES_UNSUSPENDED")) {
                                strArr = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                                iArr2 = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                                z5 = false;
                                z4 = true;
                                z8 = false;
                            } else if (action.equals("android.intent.action.DISTRACTING_PACKAGES_CHANGED")) {
                                if ((intent.getIntExtra("android.intent.extra.distraction_restrictions", 0) & 2) != 0) {
                                    strArr = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                                    intArrayExtra = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                                    z4 = false;
                                } else {
                                    strArr = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                                    intArrayExtra = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                                    z4 = true;
                                    z8 = false;
                                }
                                iArr2 = intArrayExtra;
                                z5 = z8;
                                z8 = false;
                            } else if (z3) {
                                strArr = intent.getStringArrayExtra("android.intent.extra.PACKAGES");
                                iArr = new int[]{intent.getIntExtra("android.intent.extra.UID", -1)};
                            } else {
                                Uri data = intent.getData();
                                if (data == null || (schemeSpecificPart = data.getSchemeSpecificPart()) == null) {
                                    return;
                                }
                                if (z2) {
                                    try {
                                        int applicationEnabledSetting = NotificationManagerService.this.mPackageManager.getApplicationEnabledSetting(schemeSpecificPart, intExtra != -1 ? intExtra : 0);
                                        if (applicationEnabledSetting == 1 || applicationEnabledSetting == 0) {
                                            z8 = false;
                                        }
                                    } catch (RemoteException unused) {
                                    } catch (IllegalArgumentException e) {
                                        if (NotificationManagerService.DBG) {
                                            Slog.i(NotificationManagerService.TAG, "Exception trying to look up app enabled setting", e);
                                        }
                                    }
                                }
                                strArr = new String[]{schemeSpecificPart};
                                iArr = new int[]{intent.getIntExtra("android.intent.extra.UID", -1)};
                            }
                            if (strArr != null && strArr.length > 0) {
                                if (!z8) {
                                    int length = strArr.length;
                                    int i5 = 0;
                                    while (i5 < length) {
                                        String str = strArr[i5];
                                        if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                                            i2 = i5;
                                            i3 = length;
                                            z7 = z9;
                                            i4 = intExtra;
                                            NotificationManagerService.this.mNMSWrapper.getNMSExt().cancelAllNotificationsInt(action, NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, null, 0, 0, !z3, intExtra, 5, null);
                                        } else {
                                            i2 = i5;
                                            i3 = length;
                                            z7 = z9;
                                            i4 = intExtra;
                                            NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, null, 0, 0, !z3, i4, 5, null);
                                        }
                                        i5 = i2 + 1;
                                        intExtra = i4;
                                        length = i3;
                                        z9 = z7;
                                    }
                                } else {
                                    z6 = z9;
                                    i = intExtra;
                                    if (z5 && iArr2 != null && iArr2.length > 0) {
                                        NotificationManagerService.this.hideNotificationsForPackages(strArr, iArr2);
                                    } else if (z4 && iArr2 != null && iArr2.length > 0) {
                                        NotificationManagerService.this.unhideNotificationsForPackages(strArr, iArr2);
                                    }
                                    NotificationManagerService.this.mHandler.scheduleOnPackageChanged(z6, i, strArr, iArr2);
                                }
                            }
                            z6 = z9;
                            i = intExtra;
                            NotificationManagerService.this.mHandler.scheduleOnPackageChanged(z6, i, strArr, iArr2);
                        }
                        iArr2 = iArr;
                        z5 = false;
                        z4 = false;
                        if (strArr != null) {
                            if (!z8) {
                            }
                        }
                        z6 = z9;
                        i = intExtra;
                        NotificationManagerService.this.mHandler.scheduleOnPackageChanged(z6, i, strArr, iArr2);
                    }
                    z2 = false;
                }
                z3 = z2;
                int intExtra2 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                boolean z82 = true;
                if (z) {
                }
                if (NotificationManagerService.DBG) {
                }
                if (!action.equals("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE")) {
                }
                iArr2 = iArr;
                z5 = false;
                z4 = false;
                if (strArr != null) {
                }
                z6 = z9;
                i = intExtra2;
                NotificationManagerService.this.mHandler.scheduleOnPackageChanged(z6, i, strArr, iArr2);
            }
        };
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action.equals("android.intent.action.SCREEN_ON")) {
                    NotificationManagerService notificationManagerService = NotificationManagerService.this;
                    notificationManagerService.mScreenLocked = notificationManagerService.mKeyguardManager.isKeyguardLocked();
                    if (!NotificationManagerService.this.mScreenLocked) {
                        synchronized (NotificationManagerService.this.mNotificationLock) {
                            NotificationManagerService.this.clearLightsLocked();
                        }
                    }
                    Slog.d(NotificationManagerService.TAG, "on screen turned on, screen locked:" + NotificationManagerService.this.mScreenLocked);
                    NotificationManagerService notificationManagerService2 = NotificationManagerService.this;
                    notificationManagerService2.mScreenOn = true;
                    notificationManagerService2.updateNotificationPulse();
                    return;
                }
                if (action.equals("android.intent.action.SCREEN_OFF")) {
                    NotificationManagerService notificationManagerService3 = NotificationManagerService.this;
                    notificationManagerService3.mScreenOn = false;
                    notificationManagerService3.mScreenLocked = true;
                    NotificationManagerService.this.updateNotificationPulse();
                    return;
                }
                if (action.equals("android.intent.action.PHONE_STATE")) {
                    NotificationManagerService.this.mInCallStateOffHook = TelephonyManager.EXTRA_STATE_OFFHOOK.equals(intent.getStringExtra("state"));
                    NotificationManagerService.this.updateNotificationPulse();
                    return;
                }
                if (action.equals("android.intent.action.USER_STOPPED")) {
                    int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    if (intExtra >= 0) {
                        NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, null, null, 0, 0, true, intExtra, 6, null);
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE")) {
                    int intExtra2 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    if (intExtra2 < 0 || NotificationManagerService.this.mDpm.isKeepProfilesRunningEnabled()) {
                        return;
                    }
                    NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, null, null, 0, 0, true, intExtra2, 15, null);
                    NotificationManagerService.this.mSnoozeHelper.clearData(intExtra2);
                    return;
                }
                if (action.equals("android.intent.action.USER_PRESENT")) {
                    synchronized (NotificationManagerService.this.mNotificationLock) {
                        NotificationManagerService.this.clearLightsLocked();
                    }
                    NotificationManagerService.this.mScreenLocked = false;
                    NotificationManagerService.this.updateNotificationPulse();
                    return;
                }
                if (action.equals("android.intent.action.USER_SWITCHED")) {
                    int intExtra3 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    NotificationManagerService.this.mUserProfiles.updateCache(context2);
                    if (!NotificationManagerService.this.mUserProfiles.isProfileUser(intExtra3)) {
                        NotificationManagerService.this.mSettingsObserver.update(null);
                        NotificationManagerService.this.mConditionProviders.onUserSwitched(intExtra3);
                        NotificationManagerService.this.mListeners.onUserSwitched(intExtra3);
                        NotificationManagerService.this.mZenModeHelper.onUserSwitched(intExtra3);
                    }
                    NotificationManagerService.this.mAssistants.onUserSwitched(intExtra3);
                    return;
                }
                if (action.equals("android.intent.action.USER_ADDED")) {
                    int intExtra4 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    if (intExtra4 != -10000) {
                        NotificationManagerService.this.mUserProfiles.updateCache(context2);
                        if (NotificationManagerService.this.mUserProfiles.isProfileUser(intExtra4)) {
                            return;
                        }
                        NotificationManagerService.this.allowDefaultApprovedServices(intExtra4);
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.USER_REMOVED")) {
                    int intExtra5 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    NotificationManagerService.this.mUserProfiles.updateCache(context2);
                    NotificationManagerService.this.mZenModeHelper.onUserRemoved(intExtra5);
                    NotificationManagerService.this.mPreferencesHelper.onUserRemoved(intExtra5);
                    NotificationManagerService.this.mListeners.onUserRemoved(intExtra5);
                    NotificationManagerService.this.mConditionProviders.onUserRemoved(intExtra5);
                    NotificationManagerService.this.mAssistants.onUserRemoved(intExtra5);
                    NotificationManagerService.this.mHistoryManager.onUserRemoved(intExtra5);
                    NotificationManagerService.this.handleSavePolicyFile();
                    return;
                }
                if (action.equals("android.intent.action.USER_UNLOCKED")) {
                    int intExtra6 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    NotificationManagerService.this.mUserProfiles.updateCache(context2);
                    NotificationManagerService.this.mAssistants.onUserUnlocked(intExtra6);
                    if (NotificationManagerService.this.mUserProfiles.isProfileUser(intExtra6)) {
                        return;
                    }
                    NotificationManagerService.this.mConditionProviders.onUserUnlocked(intExtra6);
                    NotificationManagerService.this.mListeners.onUserUnlocked(intExtra6);
                    NotificationManagerService.this.mZenModeHelper.onUserUnlocked(intExtra6);
                    return;
                }
                if (action.equals("android.intent.action.TIME_SET")) {
                    Slog.d(NotificationManagerService.TAG, "Lights time changed");
                    NotificationManagerService.this.updateNotificationPulse();
                    NotificationManagerService.this.scheduleLightsPeriod();
                }
            }
        };
        this.mService = new AnonymousClass11();
        this.mInternalService = new AnonymousClass12();
        this.mShortcutListener = new ShortcutHelper.ShortcutListener() { // from class: com.android.server.notification.NotificationManagerService.13
            @Override // com.android.server.notification.ShortcutHelper.ShortcutListener
            public void onShortcutRemoved(String str) {
                String packageName;
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(str);
                    packageName = notificationRecord != null ? notificationRecord.getSbn().getPackageName() : null;
                }
                boolean z = packageName != null && NotificationManagerService.this.mActivityManager.getPackageImportance(packageName) == 100;
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationRecord notificationRecord2 = NotificationManagerService.this.mNotificationsByKey.get(str);
                    if (notificationRecord2 != null) {
                        notificationRecord2.setShortcutInfo(null);
                        notificationRecord2.getNotification().flags |= 8;
                        NotificationManagerService.this.mHandler.post(new EnqueueNotificationRunnable(notificationRecord2.getUser().getIdentifier(), notificationRecord2, z, NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                    }
                }
            }
        };
        this.mNotificationVibrationIntensity = 1800;
        this.mBreathAllowPeriod = BREATHE_LIGHT_DEFAULT_ALLOW_PERIOD;
        this.mIsFlashing = false;
        this.mBeginHour = 7;
        this.mBeginMinute = 0;
        this.mEndHour = 23;
        this.mEndMinute = 30;
        this.mNotificationLightsOffReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.18
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }
                if (NotificationManagerService.ACTION_NOTIFICATION_LIGHTS_TIMEOUT.equals(action)) {
                    if (NotificationManagerService.this.mIsFlashing) {
                        Slog.d(NotificationManagerService.TAG, "Lights timeout, turn it off");
                    }
                    synchronized (NotificationManagerService.this.mNotificationLock) {
                        NotificationManagerService.this.clearLightsLocked();
                    }
                    return;
                }
                if (NotificationManagerService.ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT.equals(action)) {
                    Slog.d(NotificationManagerService.TAG, "Lights period timeout");
                    NotificationManagerService.this.updateNotificationPulse();
                    NotificationManagerService.this.scheduleLightsPeriod();
                }
            }
        };
        this.mNMSWrapper = new NotificationManagerServiceWrapper();
        this.mZenModeManagerExt = (IZenModeManagerExt) ExtLoader.type(IZenModeManagerExt.class).create();
        this.mNotificationRecordLogger = notificationRecordLogger;
        this.mNotificationInstanceIdSequence = instanceIdSequence;
        Notification.processAllowlistToken = ALLOWLIST_TOKEN;
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().init(this, context, this.mNotificationRecordLogger, this.mNotificationInstanceIdSequence);
        }
    }

    @VisibleForTesting
    void setAudioManager(AudioManager audioManager) {
        this.mAudioManager = audioManager;
    }

    @VisibleForTesting
    void setStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        this.mStrongAuthTracker = strongAuthTracker;
    }

    @VisibleForTesting
    void setKeyguardManager(KeyguardManager keyguardManager) {
        this.mKeyguardManager = keyguardManager;
    }

    @VisibleForTesting
    ShortcutHelper getShortcutHelper() {
        return this.mShortcutHelper;
    }

    @VisibleForTesting
    void setShortcutHelper(ShortcutHelper shortcutHelper) {
        this.mShortcutHelper = shortcutHelper;
    }

    @VisibleForTesting
    VibratorHelper getVibratorHelper() {
        return this.mVibratorHelper;
    }

    @VisibleForTesting
    void setVibratorHelper(VibratorHelper vibratorHelper) {
        this.mVibratorHelper = vibratorHelper;
    }

    @VisibleForTesting
    void setHints(int i) {
        this.mListenerHints = i;
    }

    @VisibleForTesting
    void setLights(LogicalLight logicalLight) {
        this.mNotificationLight = logicalLight;
        this.mAttentionLight = logicalLight;
        this.mNotificationPulseEnabled = true;
    }

    @VisibleForTesting
    void setScreenOn(boolean z) {
        this.mScreenOn = z;
    }

    @VisibleForTesting
    int getNotificationRecordCount() {
        int size;
        synchronized (this.mNotificationLock) {
            size = this.mNotificationList.size() + this.mNotificationsByKey.size() + this.mSummaryByGroupKey.size() + this.mEnqueuedNotifications.size();
            Iterator<NotificationRecord> it = this.mNotificationList.iterator();
            while (it.hasNext()) {
                NotificationRecord next = it.next();
                if (this.mNotificationsByKey.containsKey(next.getKey())) {
                    size--;
                }
                if (next.getSbn().isGroup() && next.getNotification().isGroupSummary()) {
                    size--;
                }
            }
        }
        return size;
    }

    @VisibleForTesting
    void clearNotifications() {
        synchronized (this.mNotificationList) {
            this.mEnqueuedNotifications.clear();
            this.mNotificationList.clear();
            this.mNotificationsByKey.clear();
            this.mSummaryByGroupKey.clear();
        }
    }

    @VisibleForTesting
    void addNotification(NotificationRecord notificationRecord) {
        this.mNotificationList.add(notificationRecord);
        this.mNotificationsByKey.put(notificationRecord.getSbn().getKey(), notificationRecord);
        if (notificationRecord.getSbn().isGroup()) {
            this.mSummaryByGroupKey.put(notificationRecord.getGroupKey(), notificationRecord);
        }
    }

    @VisibleForTesting
    void addEnqueuedNotification(NotificationRecord notificationRecord) {
        this.mEnqueuedNotifications.add(notificationRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public NotificationRecord getNotificationRecord(String str) {
        return this.mNotificationsByKey.get(str);
    }

    @VisibleForTesting
    void setSystemReady(boolean z) {
        this.mSystemReady = z;
    }

    @VisibleForTesting
    void setHandler(WorkerHandler workerHandler) {
        this.mHandler = workerHandler;
    }

    @VisibleForTesting
    void setRankingHelper(RankingHelper rankingHelper) {
        this.mRankingHelper = rankingHelper;
    }

    @VisibleForTesting
    void setPreferencesHelper(PreferencesHelper preferencesHelper) {
        this.mPreferencesHelper = preferencesHelper;
    }

    @VisibleForTesting
    void setZenHelper(ZenModeHelper zenModeHelper) {
        this.mZenModeHelper = zenModeHelper;
    }

    @VisibleForTesting
    void setIsAutomotive(boolean z) {
        this.mIsAutomotive = z;
    }

    @VisibleForTesting
    void setNotificationEffectsEnabledForAutomotive(boolean z) {
        this.mNotificationEffectsEnabledForAutomotive = z;
    }

    @VisibleForTesting
    void setIsTelevision(boolean z) {
        this.mIsTelevision = z;
    }

    @VisibleForTesting
    void setUsageStats(NotificationUsageStats notificationUsageStats) {
        this.mUsageStats = notificationUsageStats;
    }

    @VisibleForTesting
    void setAccessibilityManager(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }

    @VisibleForTesting
    void setTelecomManager(TelecomManager telecomManager) {
        this.mTelecomManager = telecomManager;
    }

    @VisibleForTesting
    void init(WorkerHandler workerHandler, RankingHandler rankingHandler, IPackageManager iPackageManager, PackageManager packageManager, LightsManager lightsManager, NotificationListeners notificationListeners, NotificationAssistants notificationAssistants, ConditionProviders conditionProviders, ICompanionDeviceManager iCompanionDeviceManager, SnoozeHelper snoozeHelper, NotificationUsageStats notificationUsageStats, AtomicFile atomicFile, ActivityManager activityManager, GroupHelper groupHelper, IActivityManager iActivityManager, ActivityTaskManagerInternal activityTaskManagerInternal, UsageStatsManagerInternal usageStatsManagerInternal, DevicePolicyManagerInternal devicePolicyManagerInternal, IUriGrantsManager iUriGrantsManager, UriGrantsManagerInternal uriGrantsManagerInternal, AppOpsManager appOpsManager, UserManager userManager, NotificationHistoryManager notificationHistoryManager, StatsManager statsManager, TelephonyManager telephonyManager, ActivityManagerInternal activityManagerInternal, MultiRateLimiter multiRateLimiter, PermissionHelper permissionHelper, UsageStatsManagerInternal usageStatsManagerInternal2, TelecomManager telecomManager, NotificationChannelLogger notificationChannelLogger, SystemUiSystemPropertiesFlags.FlagResolver flagResolver, PermissionManager permissionManager, PowerManager powerManager, PostNotificationTrackerFactory postNotificationTrackerFactory) {
        String[] strArr;
        this.mHandler = workerHandler;
        Resources resources = getContext().getResources();
        this.mMaxPackageEnqueueRate = Settings.Global.getFloat(getContext().getContentResolver(), "max_notification_enqueue_rate", DEFAULT_MAX_NOTIFICATION_ENQUEUE_RATE);
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        this.mAm = iActivityManager;
        this.mAtm = activityTaskManagerInternal;
        activityTaskManagerInternal.setBackgroundActivityStartCallback(new NotificationTrampolineCallback());
        this.mUgm = iUriGrantsManager;
        this.mUgmInternal = uriGrantsManagerInternal;
        this.mPackageManager = iPackageManager;
        this.mPackageManagerClient = packageManager;
        this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mPermissionManager = permissionManager;
        this.mPermissionPolicyInternal = (PermissionPolicyInternal) LocalServices.getService(PermissionPolicyInternal.class);
        this.mUmInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        this.mUsageStatsManagerInternal = usageStatsManagerInternal2;
        this.mAppOps = appOpsManager;
        this.mAppUsageStats = usageStatsManagerInternal;
        this.mAlarmManager = (AlarmManager) getContext().getSystemService("alarm");
        this.mCompanionManager = iCompanionDeviceManager;
        this.mActivityManager = activityManager;
        this.mAmi = activityManagerInternal;
        this.mDeviceIdleManager = (DeviceIdleManager) getContext().getSystemService(DeviceIdleManager.class);
        this.mDpm = devicePolicyManagerInternal;
        this.mUm = userManager;
        this.mTelecomManager = telecomManager;
        this.mPowerManager = powerManager;
        this.mPostNotificationTrackerFactory = postNotificationTrackerFactory;
        this.mPlatformCompat = IPlatformCompat.Stub.asInterface(ServiceManager.getService("platform_compat"));
        this.mStrongAuthTracker = new StrongAuthTracker(getContext());
        try {
            strArr = resources.getStringArray(R.array.no_ems_support_sim_operators);
        } catch (Resources.NotFoundException unused) {
            strArr = new String[0];
        }
        this.mUsageStats = notificationUsageStats;
        this.mMetricsLogger = new MetricsLogger();
        this.mRankingHandler = rankingHandler;
        this.mConditionProviders = conditionProviders;
        ZenModeHelper zenModeHelper = new ZenModeHelper(getContext(), this.mHandler.getLooper(), this.mConditionProviders, new SysUiStatsEvent.BuilderFactory(), flagResolver, new ZenModeEventLogger(this.mPackageManagerClient));
        this.mZenModeHelper = zenModeHelper;
        zenModeHelper.addCallback(new AnonymousClass7());
        this.mPermissionHelper = permissionHelper;
        this.mNotificationChannelLogger = notificationChannelLogger;
        this.mPreferencesHelper = new PreferencesHelper(getContext(), this.mPackageManagerClient, this.mRankingHandler, this.mZenModeHelper, this.mPermissionHelper, this.mNotificationChannelLogger, this.mAppOps, new SysUiStatsEvent.BuilderFactory(), this.mShowReviewPermissionsNotification);
        this.mRankingHelper = new RankingHelper(getContext(), this.mRankingHandler, this.mPreferencesHelper, this.mZenModeHelper, this.mUsageStats, strArr);
        this.mSnoozeHelper = snoozeHelper;
        this.mGroupHelper = groupHelper;
        this.mVibratorHelper = new VibratorHelper(getContext());
        this.mHistoryManager = notificationHistoryManager;
        this.mListeners = notificationListeners;
        this.mAssistants = notificationAssistants;
        this.mAllowedManagedServicePackages = new TriPredicate() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda4
            public final boolean test(Object obj, Object obj2, Object obj3) {
                return NotificationManagerService.this.canUseManagedServices((String) obj, (Integer) obj2, (String) obj3);
            }
        };
        this.mPolicyFile = atomicFile;
        loadPolicyFile();
        StatusBarManagerInternal statusBarManagerInternal = (StatusBarManagerInternal) getLocalService(StatusBarManagerInternal.class);
        this.mStatusBar = statusBarManagerInternal;
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.setNotificationDelegate(this.mNotificationDelegate);
        }
        this.mNotificationLight = lightsManager.getLight(4);
        this.mAttentionLight = lightsManager.getLight(5);
        this.mInCallNotificationUri = Uri.parse("file://" + resources.getString(R.string.country_detector));
        this.mInCallNotificationAudioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(2).build();
        this.mInCallNotificationVolume = resources.getFloat(R.dimen.config_screenBrightnessSettingMinimumFloat);
        this.mUseAttentionLight = resources.getBoolean(17891881);
        this.mHasLight = resources.getBoolean(17891719);
        boolean z = true;
        if (Settings.Global.getInt(getContext().getContentResolver(), "device_provisioned", 0) == 0) {
            this.mDisableNotificationEffects = true;
        }
        this.mZenModeHelper.initZenMode();
        this.mInterruptionFilter = this.mZenModeHelper.getZenModeListenerInterruptionFilter();
        this.mUserProfiles.updateCache(getContext());
        if (this.mPackageManagerClient.hasSystemFeature("android.hardware.telephony")) {
            telephonyManager.listen(new PhoneStateListener() { // from class: com.android.server.notification.NotificationManagerService.8
                @Override // android.telephony.PhoneStateListener
                public void onCallStateChanged(int i, String str) {
                    if (NotificationManagerService.this.mCallState == i) {
                        return;
                    }
                    if (NotificationManagerService.DBG) {
                        Slog.d(NotificationManagerService.TAG, "Call state changed: " + NotificationManagerService.callStateToString(i));
                    }
                    NotificationManagerService.this.mCallState = i;
                }
            }, 32);
        }
        this.mSettingsObserver = new SettingsObserver(this.mHandler);
        this.mArchive = new Archive(resources.getInteger(R.integer.config_virtualKeyQuietTimeMillis));
        if (!this.mPackageManagerClient.hasSystemFeature("android.software.leanback") && !this.mPackageManagerClient.hasSystemFeature("android.hardware.type.television")) {
            z = false;
        }
        this.mIsTelevision = z;
        this.mIsAutomotive = this.mPackageManagerClient.hasSystemFeature("android.hardware.type.automotive", 0);
        this.mNotificationEffectsEnabledForAutomotive = resources.getBoolean(17891679);
        this.mZenModeHelper.setPriorityOnlyDndExemptPackages(getContext().getResources().getStringArray(R.array.required_apps_managed_profile));
        this.mWarnRemoteViewsSizeBytes = getContext().getResources().getInteger(R.integer.config_wakeUpDelayDoze);
        this.mStripRemoteViewsSizeBytes = getContext().getResources().getInteger(R.integer.config_volte_replacement_rat);
        this.mMsgPkgsAllowedAsConvos = Set.of((Object[]) getStringArrayResource(R.array.networks_not_clear_data));
        this.mDefaultSearchSelectorPkg = getContext().getString(getContext().getResources().getIdentifier("config_defaultSearchSelectorPackageName", "string", PackageManagerService.PLATFORM_PACKAGE_NAME));
        this.mFlagResolver = flagResolver;
        this.mStatsManager = statsManager;
        this.mToastRateLimiter = multiRateLimiter;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.USER_STOPPED");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        getContext().registerReceiverAsUser(this.mIntentReceiver, UserHandle.ALL, intentFilter, null, null);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter2.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter2.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter2.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
        intentFilter2.addDataScheme("package");
        getContext().registerReceiverAsUser(this.mPackageIntentReceiver, UserHandle.ALL, intentFilter2, null, null);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.intent.action.PACKAGES_SUSPENDED");
        intentFilter3.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        intentFilter3.addAction("android.intent.action.DISTRACTING_PACKAGES_CHANGED");
        getContext().registerReceiverAsUser(this.mPackageIntentReceiver, UserHandle.ALL, intentFilter3, null, null);
        getContext().registerReceiverAsUser(this.mPackageIntentReceiver, UserHandle.ALL, new IntentFilter("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE"), null, null);
        IntentFilter intentFilter4 = new IntentFilter(ACTION_NOTIFICATION_TIMEOUT);
        intentFilter4.addDataScheme(SCHEME_TIMEOUT);
        getContext().registerReceiver(this.mNotificationTimeoutReceiver, intentFilter4, 2);
        IntentFilter intentFilter5 = new IntentFilter(ACTION_NOTIFICATION_LIGHTS_TIMEOUT);
        intentFilter5.addAction(ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT);
        getContext().registerReceiver(this.mNotificationLightsOffReceiver, intentFilter5);
        getContext().registerReceiver(this.mRestoreReceiver, new IntentFilter("android.os.action.SETTING_RESTORED"));
        getContext().registerReceiver(this.mLocaleChangeReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.mReviewNotificationPermissionsReceiver = new ReviewNotificationPermissionsReceiver();
        getContext().registerReceiver(this.mReviewNotificationPermissionsReceiver, ReviewNotificationPermissionsReceiver.getFilter(), 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.notification.NotificationManagerService$7, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass7 extends ZenModeHelper.Callback {
        AnonymousClass7() {
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        public void onConfigChanged() {
            NotificationManagerService.this.handleSavePolicyFile();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onZenModeChanged() {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$7$$ExternalSyntheticLambda3
                public final void runOrThrow() {
                    NotificationManagerService.AnonymousClass7.this.lambda$onZenModeChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onZenModeChanged$0() throws Exception {
            NotificationManagerService.this.sendRegisteredOnlyBroadcast("android.app.action.INTERRUPTION_FILTER_CHANGED");
            NotificationManagerService.this.getContext().sendBroadcastAsUser(new Intent("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL").addFlags(67108864), UserHandle.ALL, "android.permission.MANAGE_NOTIFICATIONS");
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationManagerService.this.updateInterruptionFilterLocked();
            }
            NotificationManagerService.this.mRankingHandler.requestSort();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onPolicyChanged() {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$7$$ExternalSyntheticLambda2
                public final void runOrThrow() {
                    NotificationManagerService.AnonymousClass7.this.lambda$onPolicyChanged$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPolicyChanged$1() throws Exception {
            NotificationManagerService.this.sendRegisteredOnlyBroadcast("android.app.action.NOTIFICATION_POLICY_CHANGED");
            NotificationManagerService.this.mRankingHandler.requestSort();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onConsolidatedPolicyChanged() {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$7$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    NotificationManagerService.AnonymousClass7.this.lambda$onConsolidatedPolicyChanged$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConsolidatedPolicyChanged$2() throws Exception {
            NotificationManagerService.this.mRankingHandler.requestSort();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onAutomaticRuleStatusChanged(final int i, final String str, final String str2, final int i2) {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$7$$ExternalSyntheticLambda1
                public final void runOrThrow() {
                    NotificationManagerService.AnonymousClass7.this.lambda$onAutomaticRuleStatusChanged$3(str, str2, i2, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAutomaticRuleStatusChanged$3(String str, String str2, int i, int i2) throws Exception {
            Intent intent = new Intent("android.app.action.AUTOMATIC_ZEN_RULE_STATUS_CHANGED");
            intent.setPackage(str);
            intent.putExtra("android.app.extra.AUTOMATIC_ZEN_RULE_ID", str2);
            intent.putExtra("android.app.extra.AUTOMATIC_ZEN_RULE_STATUS", i);
            NotificationManagerService.this.getContext().sendBroadcastAsUser(intent, UserHandle.of(i2));
        }
    }

    public void onDestroy() {
        getContext().unregisterReceiver(this.mIntentReceiver);
        getContext().unregisterReceiver(this.mPackageIntentReceiver);
        getContext().unregisterReceiver(this.mNotificationTimeoutReceiver);
        getContext().unregisterReceiver(this.mRestoreReceiver);
        getContext().unregisterReceiver(this.mLocaleChangeReceiver);
        DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener = this.mDeviceConfigChangedListener;
        if (onPropertiesChangedListener != null) {
            DeviceConfig.removeOnPropertiesChangedListener(onPropertiesChangedListener);
        }
    }

    protected String[] getStringArrayResource(int i) {
        return getContext().getResources().getStringArray(i);
    }

    public void onStart() {
        SnoozeHelper snoozeHelper = new SnoozeHelper(getContext(), new SnoozeHelper.Callback() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda1
            @Override // com.android.server.notification.SnoozeHelper.Callback
            public final void repost(int i, NotificationRecord notificationRecord, boolean z) {
                NotificationManagerService.this.lambda$onStart$0(i, notificationRecord, z);
            }
        }, this.mUserProfiles);
        File file = new File(Environment.getDataDirectory(), "system");
        this.mRankingThread.start();
        WorkerHandler workerHandler = new WorkerHandler(Looper.myLooper());
        this.mShowReviewPermissionsNotification = getContext().getResources().getBoolean(17891773);
        RankingHandlerWorker rankingHandlerWorker = new RankingHandlerWorker(this.mRankingThread.getLooper());
        IPackageManager packageManager = AppGlobals.getPackageManager();
        PackageManager packageManager2 = getContext().getPackageManager();
        LightsManager lightsManager = (LightsManager) getLocalService(LightsManager.class);
        NotificationListeners notificationListeners = new NotificationListeners(this, getContext(), this.mNotificationLock, this.mUserProfiles, AppGlobals.getPackageManager());
        NotificationAssistants notificationAssistants = new NotificationAssistants(getContext(), this.mNotificationLock, this.mUserProfiles, AppGlobals.getPackageManager());
        ConditionProviders conditionProviders = new ConditionProviders(getContext(), this.mUserProfiles, AppGlobals.getPackageManager());
        NotificationUsageStats notificationUsageStats = new NotificationUsageStats(getContext());
        AtomicFile atomicFile = new AtomicFile(new File(file, "notification_policy.xml"), TAG_NOTIFICATION_POLICY);
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService("activity");
        GroupHelper groupHelper = getGroupHelper();
        IActivityManager service = ActivityManager.getService();
        ActivityTaskManagerInternal activityTaskManagerInternal = (ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class);
        UsageStatsManagerInternal usageStatsManagerInternal = (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class);
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        IUriGrantsManager service2 = UriGrantsManager.getService();
        UriGrantsManagerInternal uriGrantsManagerInternal = (UriGrantsManagerInternal) LocalServices.getService(UriGrantsManagerInternal.class);
        AppOpsManager appOpsManager = (AppOpsManager) getContext().getSystemService(AppOpsManager.class);
        UserManager userManager = (UserManager) getContext().getSystemService(UserManager.class);
        NotificationHistoryManager notificationHistoryManager = new NotificationHistoryManager(getContext(), workerHandler);
        StatsManager statsManager = (StatsManager) getContext().getSystemService("stats");
        this.mStatsManager = statsManager;
        init(workerHandler, rankingHandlerWorker, packageManager, packageManager2, lightsManager, notificationListeners, notificationAssistants, conditionProviders, null, snoozeHelper, notificationUsageStats, atomicFile, activityManager, groupHelper, service, activityTaskManagerInternal, usageStatsManagerInternal, devicePolicyManagerInternal, service2, uriGrantsManagerInternal, appOpsManager, userManager, notificationHistoryManager, statsManager, (TelephonyManager) getContext().getSystemService(TelephonyManager.class), (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class), createToastRateLimiter(), new PermissionHelper(getContext(), AppGlobals.getPackageManager(), AppGlobals.getPermissionManager()), (UsageStatsManagerInternal) LocalServices.getService(UsageStatsManagerInternal.class), (TelecomManager) getContext().getSystemService(TelecomManager.class), new NotificationChannelLoggerImpl(), SystemUiSystemPropertiesFlags.getResolver(), (PermissionManager) getContext().getSystemService(PermissionManager.class), (PowerManager) getContext().getSystemService(PowerManager.class), new PostNotificationTrackerFactory() { // from class: com.android.server.notification.NotificationManagerService.9
        });
        publishBinderService("notification", this.mService, false, 5);
        publishLocalService(NotificationManagerInternal.class, this.mInternalService);
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().onStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(int i, NotificationRecord notificationRecord, boolean z) {
        try {
            if (DBG) {
                Slog.d(TAG, "Reposting " + notificationRecord.getKey() + " " + z);
            }
            enqueueNotificationInternal(notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getOpPkg(), notificationRecord.getSbn().getUid(), notificationRecord.getSbn().getInitialPid(), notificationRecord.getSbn().getTag(), notificationRecord.getSbn().getId(), notificationRecord.getSbn().getNotification(), i, z, false);
        } catch (Exception e) {
            Slog.e(TAG, "Cannot un-snooze notification", e);
        }
    }

    void registerDeviceConfigChange() {
        this.mDeviceConfigChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                NotificationManagerService.this.lambda$registerDeviceConfigChange$1(properties);
            }
        };
        this.mSystemExemptFromDismissal = DeviceConfig.getBoolean("device_policy_manager", "application_exemptions", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", new HandlerExecutor(this.mHandler), this.mDeviceConfigChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDeviceConfigChange$1(DeviceConfig.Properties properties) {
        if ("systemui".equals(properties.getNamespace())) {
            Iterator it = properties.getKeyset().iterator();
            while (it.hasNext()) {
                if ("nas_default_service".equals((String) it.next())) {
                    this.mAssistants.resetDefaultAssistantsIfNecessary();
                }
            }
        }
    }

    private void registerNotificationPreferencesPullers() {
        StatsPullAtomCallbackImpl statsPullAtomCallbackImpl = new StatsPullAtomCallbackImpl();
        this.mPullAtomCallback = statsPullAtomCallbackImpl;
        this.mStatsManager.setPullAtomCallback(10071, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, statsPullAtomCallbackImpl);
        this.mStatsManager.setPullAtomCallback(10072, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mPullAtomCallback);
        this.mStatsManager.setPullAtomCallback(10073, (StatsManager.PullAtomMetadata) null, ConcurrentUtils.DIRECT_EXECUTOR, this.mPullAtomCallback);
        this.mStatsManager.setPullAtomCallback(10084, (StatsManager.PullAtomMetadata) null, BackgroundThread.getExecutor(), this.mPullAtomCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class StatsPullAtomCallbackImpl implements StatsManager.StatsPullAtomCallback {
        private StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int i, List<StatsEvent> list) {
            if (i != 10084) {
                switch (i) {
                    case 10071:
                    case 10072:
                    case 10073:
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown tagId=" + i);
                }
            }
            return NotificationManagerService.this.pullNotificationStates(i, list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullNotificationStates(int i, List<StatsEvent> list) {
        if (i != 10084) {
            switch (i) {
                case 10071:
                    this.mPreferencesHelper.pullPackagePreferencesStats(list, getAllUsersNotificationPermissions());
                    return 0;
                case 10072:
                    this.mPreferencesHelper.pullPackageChannelPreferencesStats(list);
                    return 0;
                case 10073:
                    this.mPreferencesHelper.pullPackageChannelGroupPreferencesStats(list);
                    return 0;
                default:
                    return 0;
            }
        }
        this.mZenModeHelper.pullRules(list);
        return 0;
    }

    private GroupHelper getGroupHelper() {
        int integer = getContext().getResources().getInteger(R.integer.config_bluetooth_max_scan_filters);
        this.mAutoGroupAtCount = integer;
        return new GroupHelper(integer, new GroupHelper.Callback() { // from class: com.android.server.notification.NotificationManagerService.10
            @Override // com.android.server.notification.GroupHelper.Callback
            public void addAutoGroup(String str) {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.addAutogroupKeyLocked(str);
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void removeAutoGroup(String str) {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.removeAutogroupKeyLocked(str);
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void addAutoGroupSummary(int i, String str, String str2, int i2) {
                NotificationRecord createAutoGroupSummary = NotificationManagerService.this.createAutoGroupSummary(i, str, str2, i2);
                if (createAutoGroupSummary != null) {
                    boolean z = NotificationManagerService.this.mActivityManager.getPackageImportance(str) == 100;
                    WorkerHandler workerHandler = NotificationManagerService.this.mHandler;
                    NotificationManagerService notificationManagerService = NotificationManagerService.this;
                    workerHandler.post(new EnqueueNotificationRunnable(i, createAutoGroupSummary, z, notificationManagerService.mPostNotificationTrackerFactory.newTracker(null)));
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void removeAutoGroupSummary(int i, String str) {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.clearAutogroupSummaryLocked(i, str);
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void updateAutogroupSummary(int i, String str, int i2) {
                boolean z = str != null && NotificationManagerService.this.mActivityManager.getPackageImportance(str) == 100;
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.updateAutobundledSummaryFlags(i, str, i2, z);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRegisteredOnlyBroadcast(String str) {
        int[] profileIds = this.mUmInternal.getProfileIds(this.mAmi.getCurrentUserId(), true);
        Intent addFlags = new Intent(str).addFlags(1073741824);
        for (int i : profileIds) {
            getContext().sendBroadcastAsUser(addFlags, UserHandle.of(i), null);
        }
        for (int i2 : profileIds) {
            Iterator<String> it = this.mConditionProviders.getAllowedPackages(i2).iterator();
            while (it.hasNext()) {
                getContext().sendBroadcastAsUser(new Intent(str).setPackage(it.next()).setFlags(67108864), UserHandle.of(i2));
            }
        }
    }

    public void onBootPhase(int i) {
        onBootPhase(i, Looper.getMainLooper());
    }

    @VisibleForTesting
    void onBootPhase(int i, Looper looper) {
        if (i == 500) {
            this.mSystemReady = true;
            this.mAudioManager = (AudioManager) getContext().getSystemService("audio");
            this.mAudioManagerInternal = (AudioManagerInternal) getLocalService(AudioManagerInternal.class);
            this.mWindowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
            this.mKeyguardManager = (KeyguardManager) getContext().getSystemService(KeyguardManager.class);
            this.mZenModeHelper.onSystemReady();
            RoleObserver roleObserver = new RoleObserver(getContext(), (RoleManager) getContext().getSystemService(RoleManager.class), this.mPackageManager, looper);
            roleObserver.init();
            this.mRoleObserver = roleObserver;
            this.mShortcutHelper = new ShortcutHelper((LauncherApps) getContext().getSystemService("launcherapps"), this.mShortcutListener, (ShortcutServiceInternal) getLocalService(ShortcutServiceInternal.class), (UserManager) getContext().getSystemService("user"));
            BubbleExtractor bubbleExtractor = (BubbleExtractor) this.mRankingHelper.findExtractor(BubbleExtractor.class);
            if (bubbleExtractor != null) {
                bubbleExtractor.setShortcutHelper(this.mShortcutHelper);
            }
            registerNotificationPreferencesPullers();
            new LockPatternUtils(getContext()).registerStrongAuthTracker(this.mStrongAuthTracker);
        } else if (i == 600) {
            this.mSettingsObserver.observe();
            this.mListeners.onBootPhaseAppsCanStart();
            this.mAssistants.onBootPhaseAppsCanStart();
            this.mConditionProviders.onBootPhaseAppsCanStart();
            this.mHistoryManager.onBootPhaseAppsCanStart();
            registerDeviceConfigChange();
            migrateDefaultNAS();
            maybeShowInitialReviewPermissionsNotification();
        } else if (i == 550) {
            this.mSnoozeHelper.scheduleRepostsForPersistedNotifications(System.currentTimeMillis());
        } else if (i == 520) {
            this.mPreferencesHelper.updateFixedImportance(this.mUm.getUsers());
            this.mPreferencesHelper.migrateNotificationPermissions(this.mUm.getUsers());
        }
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().onBootPhase(i);
        }
    }

    public void onUserUnlocked(final SystemService.TargetUser targetUser) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                NotificationManagerService.this.lambda$onUserUnlocked$2(targetUser);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserUnlocked$2(SystemService.TargetUser targetUser) {
        Trace.traceBegin(524288L, "notifHistoryUnlockUser");
        try {
            this.mHistoryManager.onUserUnlocked(targetUser.getUserIdentifier());
        } finally {
            Trace.traceEnd(524288L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAppBlockStateChangedBroadcast(final String str, final int i, final boolean z) {
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationManagerService.this.lambda$sendAppBlockStateChangedBroadcast$3(z, str, i);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendAppBlockStateChangedBroadcast$3(boolean z, String str, int i) {
        try {
            getContext().sendBroadcastAsUser(new Intent("android.app.action.APP_BLOCK_STATE_CHANGED").putExtra("android.app.extra.BLOCKED_STATE", z).addFlags(268435456).setPackage(str), UserHandle.of(UserHandle.getUserId(i)), null);
        } catch (SecurityException e) {
            Slog.w(TAG, "Can't notify app about app block change", e);
        }
    }

    public void onUserStopping(final SystemService.TargetUser targetUser) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                NotificationManagerService.this.lambda$onUserStopping$4(targetUser);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserStopping$4(SystemService.TargetUser targetUser) {
        Trace.traceBegin(524288L, "notifHistoryStopUser");
        try {
            this.mHistoryManager.onUserStopped(targetUser.getUserIdentifier());
        } finally {
            Trace.traceEnd(524288L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void updateListenerHintsLocked() {
        int calculateHints = calculateHints();
        int i = this.mListenerHints;
        if (calculateHints == i) {
            return;
        }
        ZenLog.traceListenerHintsChanged(i, calculateHints, this.mEffectsSuppressors.size());
        this.mListenerHints = calculateHints;
        scheduleListenerHintsChanged(calculateHints);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void updateEffectsSuppressorLocked() {
        long calculateSuppressedEffects = calculateSuppressedEffects();
        if (calculateSuppressedEffects == this.mZenModeHelper.getSuppressedEffects()) {
            return;
        }
        ArrayList<ComponentName> suppressors = getSuppressors();
        ZenLog.traceEffectsSuppressorChanged(this.mEffectsSuppressors, suppressors, calculateSuppressedEffects);
        this.mEffectsSuppressors = suppressors;
        this.mZenModeHelper.setSuppressedEffects(calculateSuppressedEffects);
        sendRegisteredOnlyBroadcast("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitIdle() {
        DeviceIdleManager deviceIdleManager = this.mDeviceIdleManager;
        if (deviceIdleManager != null) {
            deviceIdleManager.endIdle("notification interaction");
        }
    }

    void updateNotificationChannelInt(String str, int i, NotificationChannel notificationChannel, boolean z) {
        if (notificationChannel.getImportance() == 0) {
            cancelAllNotificationsInt(MY_UID, MY_PID, str, notificationChannel.getId(), 0, 64, true, UserHandle.getUserId(i), 17, null);
            if (isUidSystemOrPhone(i)) {
                IntArray currentProfileIds = this.mUserProfiles.getCurrentProfileIds();
                int i2 = 0;
                for (int size = currentProfileIds.size(); i2 < size; size = size) {
                    cancelAllNotificationsInt(MY_UID, MY_PID, str, notificationChannel.getId(), 0, 0, true, currentProfileIds.get(i2), 17, null);
                    i2++;
                }
            }
        }
        NotificationChannel notificationChannel2 = this.mPreferencesHelper.getNotificationChannel(str, i, notificationChannel.getId(), true);
        this.mPreferencesHelper.updateNotificationChannel(str, i, notificationChannel, true, Binder.getCallingUid(), isCallerIsSystemOrSystemUi());
        if (this.mPreferencesHelper.onlyHasDefaultChannel(str, i)) {
            this.mPermissionHelper.setNotificationPermission(str, UserHandle.getUserId(i), notificationChannel.getImportance() != 0, true);
        }
        maybeNotifyChannelOwner(str, i, notificationChannel2, notificationChannel);
        if (!z) {
            this.mListeners.notifyNotificationChannelChanged(str, UserHandle.getUserHandleForUid(i), this.mPreferencesHelper.getNotificationChannel(str, i, notificationChannel.getId(), false), 2);
        }
        handleSavePolicyFile();
    }

    private void maybeNotifyChannelOwner(String str, int i, NotificationChannel notificationChannel, NotificationChannel notificationChannel2) {
        try {
            if ((notificationChannel.getImportance() != 0 || notificationChannel2.getImportance() == 0) && (notificationChannel.getImportance() == 0 || notificationChannel2.getImportance() != 0)) {
                return;
            }
            getContext().sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED").putExtra("android.app.extra.NOTIFICATION_CHANNEL_ID", notificationChannel2.getId()).putExtra("android.app.extra.BLOCKED_STATE", notificationChannel2.getImportance() == 0).addFlags(268435456).setPackage(str), UserHandle.of(UserHandle.getUserId(i)), null);
        } catch (SecurityException e) {
            Slog.w(TAG, "Can't notify app about channel change", e);
        }
    }

    void createNotificationChannelGroup(String str, int i, NotificationChannelGroup notificationChannelGroup, boolean z, boolean z2) {
        Objects.requireNonNull(notificationChannelGroup);
        Objects.requireNonNull(str);
        NotificationChannelGroup notificationChannelGroup2 = this.mPreferencesHelper.getNotificationChannelGroup(notificationChannelGroup.getId(), str, i);
        this.mPreferencesHelper.createNotificationChannelGroup(str, i, notificationChannelGroup, z, Binder.getCallingUid(), isCallerIsSystemOrSystemUi());
        if (!z) {
            maybeNotifyChannelGroupOwner(str, i, notificationChannelGroup2, notificationChannelGroup);
        }
        if (z2) {
            return;
        }
        this.mListeners.notifyNotificationChannelGroupChanged(str, UserHandle.of(UserHandle.getCallingUserId()), notificationChannelGroup, 1);
    }

    private void maybeNotifyChannelGroupOwner(String str, int i, NotificationChannelGroup notificationChannelGroup, NotificationChannelGroup notificationChannelGroup2) {
        try {
            if (notificationChannelGroup.isBlocked() != notificationChannelGroup2.isBlocked()) {
                getContext().sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED").putExtra("android.app.extra.NOTIFICATION_CHANNEL_GROUP_ID", notificationChannelGroup2.getId()).putExtra("android.app.extra.BLOCKED_STATE", notificationChannelGroup2.isBlocked()).addFlags(268435456).setPackage(str), UserHandle.of(UserHandle.getUserId(i)), null);
            }
        } catch (SecurityException e) {
            Slog.w(TAG, "Can't notify app about group change", e);
        }
    }

    private ArrayList<ComponentName> getSuppressors() {
        ArrayList<ComponentName> arrayList = new ArrayList<>();
        for (int size = this.mListenersDisablingEffects.size() - 1; size >= 0; size--) {
            Iterator<ComponentName> it = this.mListenersDisablingEffects.valueAt(size).iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeDisabledHints(ManagedServices.ManagedServiceInfo managedServiceInfo) {
        return removeDisabledHints(managedServiceInfo, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeDisabledHints(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
        boolean z = false;
        for (int size = this.mListenersDisablingEffects.size() - 1; size >= 0; size--) {
            int keyAt = this.mListenersDisablingEffects.keyAt(size);
            ArraySet<ComponentName> valueAt = this.mListenersDisablingEffects.valueAt(size);
            if (i == 0 || (keyAt & i) == keyAt) {
                z |= valueAt.remove(managedServiceInfo.component);
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDisabledHints(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
        if ((i & 1) != 0) {
            addDisabledHint(managedServiceInfo, 1);
        }
        if ((i & 2) != 0) {
            addDisabledHint(managedServiceInfo, 2);
        }
        if ((i & 4) != 0) {
            addDisabledHint(managedServiceInfo, 4);
        }
    }

    private void addDisabledHint(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
        if (this.mListenersDisablingEffects.indexOfKey(i) < 0) {
            this.mListenersDisablingEffects.put(i, new ArraySet<>());
        }
        this.mListenersDisablingEffects.get(i).add(managedServiceInfo.component);
    }

    private int calculateHints() {
        int i = 0;
        for (int size = this.mListenersDisablingEffects.size() - 1; size >= 0; size--) {
            int keyAt = this.mListenersDisablingEffects.keyAt(size);
            if (!this.mListenersDisablingEffects.valueAt(size).isEmpty()) {
                i |= keyAt;
            }
        }
        return i;
    }

    private long calculateSuppressedEffects() {
        int calculateHints = calculateHints();
        long j = (calculateHints & 1) != 0 ? 3L : 0L;
        if ((calculateHints & 2) != 0) {
            j |= 1;
        }
        return (calculateHints & 4) != 0 ? j | 2 : j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void updateInterruptionFilterLocked() {
        int zenModeListenerInterruptionFilter = this.mZenModeHelper.getZenModeListenerInterruptionFilter();
        if (zenModeListenerInterruptionFilter == this.mInterruptionFilter) {
            return;
        }
        this.mInterruptionFilter = zenModeListenerInterruptionFilter;
        scheduleInterruptionFilterChanged(zenModeListenerInterruptionFilter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public INotificationManager getBinderService() {
        return INotificationManager.Stub.asInterface(this.mService);
    }

    @GuardedBy({"mNotificationLock"})
    protected void reportSeen(NotificationRecord notificationRecord) {
        if (notificationRecord.isProxied()) {
            return;
        }
        this.mAppUsageStats.reportEvent(notificationRecord.getSbn().getPackageName(), getRealUserId(notificationRecord.getSbn().getUserId()), 10);
    }

    protected int calculateSuppressedVisualEffects(NotificationManager.Policy policy, NotificationManager.Policy policy2, int i) {
        int i2 = policy.suppressedVisualEffects;
        if (i2 == -1) {
            return i2;
        }
        int[] iArr = {4, 8, 16, 32, 64, 128, 256};
        if (i < 28) {
            while (r2 < 7) {
                int i3 = iArr[r2];
                i2 = (i2 & (~i3)) | (i3 & policy2.suppressedVisualEffects);
                r2++;
            }
            if ((i2 & 1) != 0) {
                i2 = i2 | 8 | 4;
            }
            if ((i2 & 2) == 0) {
                return i2;
            }
        } else {
            if (((i2 + (-2)) - 1 > 0 ? 1 : 0) != 0) {
                int i4 = i2 & (-4);
                if ((i4 & 16) != 0) {
                    i4 |= 2;
                }
                return ((i4 & 8) == 0 || (i4 & 4) == 0 || (i4 & 128) == 0) ? i4 : i4 | 1;
            }
            if ((i2 & 1) != 0) {
                i2 = i2 | 8 | 4 | 128;
            }
            if ((i2 & 2) == 0) {
                return i2;
            }
        }
        return i2 | 16;
    }

    @GuardedBy({"mNotificationLock"})
    protected void maybeRecordInterruptionLocked(NotificationRecord notificationRecord) {
        if (!notificationRecord.isInterruptive() || notificationRecord.hasRecordedInterruption()) {
            return;
        }
        this.mAppUsageStats.reportInterruptiveNotification(notificationRecord.getSbn().getPackageName(), notificationRecord.getChannel().getId(), getRealUserId(notificationRecord.getSbn().getUserId()));
        Trace.traceBegin(524288L, "notifHistoryAddItem");
        try {
            if (notificationRecord.getNotification().getSmallIcon() != null) {
                this.mHistoryManager.addNotification(new NotificationHistory.HistoricalNotification.Builder().setPackage(notificationRecord.getSbn().getPackageName()).setUid(notificationRecord.getSbn().getUid()).setUserId(notificationRecord.getSbn().getNormalizedUserId()).setChannelId(notificationRecord.getChannel().getId()).setChannelName(notificationRecord.getChannel().getName().toString()).setPostedTimeMs(System.currentTimeMillis()).setTitle(getHistoryTitle(notificationRecord.getNotification())).setText(getHistoryText(notificationRecord.getSbn().getPackageContext(getContext()), notificationRecord.getNotification())).setIcon(notificationRecord.getNotification().getSmallIcon()).build());
            }
            Trace.traceEnd(524288L);
            notificationRecord.setRecordedInterruption(true);
        } catch (Throwable th) {
            Trace.traceEnd(524288L);
            throw th;
        }
    }

    protected void reportForegroundServiceUpdate(final boolean z, final Notification notification, final int i, final String str, final int i2) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                NotificationManagerService.this.lambda$reportForegroundServiceUpdate$5(z, notification, i, str, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportForegroundServiceUpdate$5(boolean z, Notification notification, int i, String str, int i2) {
        this.mAmi.onForegroundServiceNotificationUpdate(z, notification, i, str, i2);
    }

    protected void maybeReportForegroundServiceUpdate(NotificationRecord notificationRecord, boolean z) {
        if (notificationRecord.isForegroundService()) {
            StatusBarNotification sbn = notificationRecord.getSbn();
            reportForegroundServiceUpdate(z, sbn.getNotification(), sbn.getId(), sbn.getPackageName(), sbn.getUser().getIdentifier());
        }
    }

    private String getHistoryTitle(Notification notification) {
        CharSequence charSequence;
        Bundle bundle = notification.extras;
        if (bundle != null) {
            charSequence = bundle.getCharSequence("android.title");
            if (charSequence == null) {
                charSequence = notification.extras.getCharSequence("android.title.big");
            }
        } else {
            charSequence = null;
        }
        if (charSequence == null) {
            return getContext().getResources().getString(R.string.permlab_recordAudio);
        }
        return String.valueOf(charSequence);
    }

    private String getHistoryText(Context context, Notification notification) {
        CharSequence charSequence;
        List<Notification.MessagingStyle.Message> messages;
        Bundle bundle = notification.extras;
        if (bundle != null) {
            charSequence = bundle.getCharSequence("android.text");
            Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(context, notification);
            if (recoverBuilder.getStyle() instanceof Notification.BigTextStyle) {
                charSequence = ((Notification.BigTextStyle) recoverBuilder.getStyle()).getBigText();
            } else if ((recoverBuilder.getStyle() instanceof Notification.MessagingStyle) && (messages = ((Notification.MessagingStyle) recoverBuilder.getStyle()).getMessages()) != null && messages.size() > 0) {
                charSequence = messages.get(messages.size() - 1).getText();
            }
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = notification.extras.getCharSequence("android.text");
            }
        } else {
            charSequence = null;
        }
        if (charSequence == null) {
            return null;
        }
        return String.valueOf(charSequence);
    }

    protected void maybeRegisterMessageSent(NotificationRecord notificationRecord) {
        if (notificationRecord.isConversation()) {
            if (notificationRecord.getShortcutInfo() != null) {
                if (this.mPreferencesHelper.setValidMessageSent(notificationRecord.getSbn().getPackageName(), notificationRecord.getUid())) {
                    handleSavePolicyFile();
                    return;
                } else {
                    if (notificationRecord.getNotification().getBubbleMetadata() == null || !this.mPreferencesHelper.setValidBubbleSent(notificationRecord.getSbn().getPackageName(), notificationRecord.getUid())) {
                        return;
                    }
                    handleSavePolicyFile();
                    return;
                }
            }
            if (this.mPreferencesHelper.setInvalidMessageSent(notificationRecord.getSbn().getPackageName(), notificationRecord.getUid())) {
                handleSavePolicyFile();
            }
        }
    }

    protected void reportUserInteraction(NotificationRecord notificationRecord) {
        this.mAppUsageStats.reportEvent(notificationRecord.getSbn().getPackageName(), getRealUserId(notificationRecord.getSbn().getUserId()), 7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ToastRecord getToastRecord(int i, int i2, String str, boolean z, IBinder iBinder, CharSequence charSequence, ITransientNotification iTransientNotification, int i3, Binder binder, int i4, ITransientNotificationCallback iTransientNotificationCallback) {
        if (iTransientNotification == null) {
            return new TextToastRecord(this, this.mStatusBar, i, i2, str, z, iBinder, charSequence, i3, binder, i4, iTransientNotificationCallback);
        }
        return new CustomToastRecord(this, i, i2, str, z, iBinder, iTransientNotification, i3, binder, i4);
    }

    @VisibleForTesting
    NotificationManagerInternal getInternalService() {
        return this.mInternalService;
    }

    private MultiRateLimiter createToastRateLimiter() {
        return new MultiRateLimiter.Builder(getContext()).addRateLimits(TOAST_RATE_LIMITS).build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.notification.NotificationManagerService$11, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass11 extends INotificationManager.Stub {
        AnonymousClass11() {
        }

        public void enqueueTextToast(String str, IBinder iBinder, CharSequence charSequence, int i, boolean z, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
            enqueueToast(str, iBinder, charSequence, null, i, z, i2, iTransientNotificationCallback);
        }

        public void enqueueToast(String str, IBinder iBinder, ITransientNotification iTransientNotification, int i, boolean z, int i2) {
            enqueueToast(str, iBinder, null, iTransientNotification, i, z, i2, null);
        }

        private void enqueueToast(String str, IBinder iBinder, CharSequence charSequence, ITransientNotification iTransientNotification, int i, boolean z, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
            int i3;
            ArrayList<ToastRecord> arrayList;
            int userId;
            int mainDisplayAssignedToUser;
            boolean z2 = NotificationManagerService.DBG;
            if (z2) {
                Slog.i(NotificationManagerService.TAG, "enqueueToast pkg=" + str + " token=" + iBinder + " duration=" + i + " isUiContext=" + z + " displayId=" + i2);
            }
            if (str == null || ((charSequence == null && iTransientNotification == null) || ((charSequence != null && iTransientNotification != null) || iBinder == null))) {
                Slog.e(NotificationManagerService.TAG, "Not enqueuing toast. pkg=" + str + " text=" + ((Object) charSequence) + " callback= token=" + iBinder);
                return;
            }
            int callingUid = Binder.getCallingUid();
            if (z || i2 != 0 || !NotificationManagerService.this.mUm.isVisibleBackgroundUsersSupported() || i2 == (mainDisplayAssignedToUser = NotificationManagerService.this.mUmInternal.getMainDisplayAssignedToUser((userId = UserHandle.getUserId(callingUid))))) {
                i3 = i2;
            } else {
                if (z2) {
                    Slogf.d(NotificationManagerService.TAG, "Changing display id from %d to %d on user %d", Integer.valueOf(i2), Integer.valueOf(mainDisplayAssignedToUser), Integer.valueOf(userId));
                }
                i3 = mainDisplayAssignedToUser;
            }
            NotificationManagerService.this.checkCallerIsSameApp(str);
            boolean z3 = NotificationManagerService.this.isCallerIsSystemOrSystemUi() || PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str);
            if (!checkCanEnqueueToast(str, callingUid, i3, iTransientNotification != null, z3)) {
                return;
            }
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldInterceptToast(str)) {
                return;
            }
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldInterceptToast(callingUid, str)) {
                return;
            }
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                i3 = NotificationManagerService.this.mNMSWrapper.getNMSExt().modifyToastDisplayIdForMirageDisplay(i3, str);
            }
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().skipToastWhileActPreload(i3, str)) {
                return;
            }
            ArrayList<ToastRecord> arrayList2 = NotificationManagerService.this.mToastQueue;
            synchronized (arrayList2) {
                try {
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    int callingPid = Binder.getCallingPid();
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        int indexOfToastLocked = NotificationManagerService.this.indexOfToastLocked(str, iBinder);
                        if (indexOfToastLocked >= 0) {
                            NotificationManagerService.this.mToastQueue.get(indexOfToastLocked).update(i);
                            arrayList = arrayList2;
                        } else {
                            int size = NotificationManagerService.this.mToastQueue.size();
                            int i4 = 0;
                            for (int i5 = 0; i5 < size; i5++) {
                                if (NotificationManagerService.this.mToastQueue.get(i5).pkg.equals(str) && (i4 = i4 + 1) >= 5) {
                                    Slog.e(NotificationManagerService.TAG, "Package has already queued " + i4 + " toasts. Not showing more. Package=" + str);
                                    Binder.restoreCallingIdentity(clearCallingIdentity);
                                    return;
                                }
                            }
                            Binder binder = new Binder();
                            NotificationManagerService.this.mWindowManagerInternal.addWindowToken(binder, 2005, i3, (Bundle) null);
                            arrayList = arrayList2;
                            try {
                                ToastRecord toastRecord = NotificationManagerService.this.getToastRecord(callingUid, callingPid, str, z3, iBinder, charSequence, iTransientNotification, i, binder, i3, iTransientNotificationCallback);
                                int size2 = NotificationManagerService.this.mToastQueue.size();
                                if (z3) {
                                    size2 = getInsertIndexForSystemToastLocked();
                                }
                                if (size2 < NotificationManagerService.this.mToastQueue.size()) {
                                    NotificationManagerService.this.mToastQueue.add(size2, toastRecord);
                                    indexOfToastLocked = size2;
                                } else {
                                    NotificationManagerService.this.mToastQueue.add(toastRecord);
                                    indexOfToastLocked = NotificationManagerService.this.mToastQueue.size() - 1;
                                }
                                NotificationManagerService.this.keepProcessAliveForToastIfNeededLocked(callingPid);
                            } catch (Throwable th2) {
                                th = th2;
                                Binder.restoreCallingIdentity(clearCallingIdentity);
                                throw th;
                            }
                        }
                        if (indexOfToastLocked == 0) {
                            NotificationManagerService.this.showNextToastLocked(false);
                        }
                        if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldContinuousShowToast(NotificationManagerService.this.mIsCurrentToastShown, indexOfToastLocked, NotificationManagerService.this.mToastQueue)) {
                            NotificationManagerService.this.mNMSWrapper.getNMSExt().scheduleDurationReachedLocked(NotificationManagerService.this.mHandler, NotificationManagerService.this.mToastQueue.get(0));
                        }
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            }
        }

        @GuardedBy({"mToastQueue"})
        private int getInsertIndexForSystemToastLocked() {
            Iterator<ToastRecord> it = NotificationManagerService.this.mToastQueue.iterator();
            int i = 0;
            while (it.hasNext()) {
                ToastRecord next = it.next();
                if ((i != 0 || !NotificationManagerService.this.mIsCurrentToastShown) && !next.isSystemToast) {
                    return i;
                }
                i++;
            }
            return i;
        }

        private boolean checkCanEnqueueToast(String str, int i, int i2, boolean z, boolean z2) {
            boolean isPackagePaused = isPackagePaused(str);
            boolean z3 = !areNotificationsEnabledForPackage(str, i);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                boolean z4 = NotificationManagerService.this.mActivityManager.getUidImportance(i) == 100;
                Binder.restoreCallingIdentity(clearCallingIdentity);
                if (!z2 && ((z3 && !z4) || isPackagePaused)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Suppressing toast from package ");
                    sb.append(str);
                    sb.append(isPackagePaused ? " due to package suspended." : " by user request.");
                    Slog.e(NotificationManagerService.TAG, sb.toString());
                    return false;
                }
                NotificationManagerService notificationManagerService = NotificationManagerService.this;
                if (notificationManagerService.blockToast(i, z2, z, notificationManagerService.isPackageInForegroundForToast(i))) {
                    Slog.w(NotificationManagerService.TAG, "Blocking custom toast from package " + str + " due to package not in the foreground at time the toast was posted");
                    return false;
                }
                int userId = UserHandle.getUserId(i);
                if (z2 || NotificationManagerService.this.mUmInternal.isUserVisible(userId, i2)) {
                    return true;
                }
                Slog.e(NotificationManagerService.TAG, "Suppressing toast from package " + str + SliceClientPermissions.SliceAuthority.DELIMITER + i + " as user " + userId + " is not visible on display " + i2);
                return false;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }

        public void cancelToast(String str, IBinder iBinder) {
            Slog.i(NotificationManagerService.TAG, "cancelToast pkg=" + str + " token=" + iBinder);
            if (str == null || iBinder == null) {
                Slog.e(NotificationManagerService.TAG, "Not cancelling notification. pkg=" + str + " token=" + iBinder);
                return;
            }
            synchronized (NotificationManagerService.this.mToastQueue) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    int indexOfToastLocked = NotificationManagerService.this.indexOfToastLocked(str, iBinder);
                    if (indexOfToastLocked >= 0) {
                        NotificationManagerService.this.cancelToastLocked(indexOfToastLocked);
                    } else {
                        Slog.w(NotificationManagerService.TAG, "Toast already cancelled. pkg=" + str + " token=" + iBinder);
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        @EnforcePermission("android.permission.MANAGE_TOAST_RATE_LIMITING")
        public void setToastRateLimitingEnabled(boolean z) {
            super.setToastRateLimitingEnabled_enforcePermission();
            synchronized (NotificationManagerService.this.mToastQueue) {
                int callingUid = Binder.getCallingUid();
                int userId = UserHandle.getUserId(callingUid);
                if (z) {
                    NotificationManagerService.this.mToastRateLimitingDisabledUids.remove(Integer.valueOf(callingUid));
                    try {
                        String[] packagesForUid = NotificationManagerService.this.mPackageManager.getPackagesForUid(callingUid);
                        if (packagesForUid == null) {
                            Slog.e(NotificationManagerService.TAG, "setToastRateLimitingEnabled method haven't found any packages for the  given uid: " + callingUid + ", toast rate limiter not reset for that uid.");
                            return;
                        }
                        for (String str : packagesForUid) {
                            NotificationManagerService.this.mToastRateLimiter.clear(userId, str);
                        }
                    } catch (RemoteException e) {
                        Slog.e(NotificationManagerService.TAG, "Failed to reset toast rate limiter for given uid", e);
                    }
                } else {
                    NotificationManagerService.this.mToastRateLimitingDisabledUids.add(Integer.valueOf(callingUid));
                }
            }
        }

        public void finishToken(String str, IBinder iBinder) {
            synchronized (NotificationManagerService.this.mToastQueue) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    int indexOfToastLocked = NotificationManagerService.this.indexOfToastLocked(str, iBinder);
                    if (indexOfToastLocked >= 0) {
                        ToastRecord toastRecord = NotificationManagerService.this.mToastQueue.get(indexOfToastLocked);
                        NotificationManagerService.this.finishWindowTokenLocked(toastRecord.windowToken, toastRecord.displayId);
                    } else {
                        Slog.w(NotificationManagerService.TAG, "Toast already killed. pkg=" + str + " token=" + iBinder);
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void enqueueNotificationWithTag(String str, String str2, String str3, int i, Notification notification, int i2) throws RemoteException {
            NotificationManagerService.this.enqueueNotificationInternal(str, str2, Binder.getCallingUid(), Binder.getCallingPid(), str3, i, notification, i2, false);
        }

        public void cancelNotificationWithTag(String str, String str2, String str3, int i, int i2) {
            NotificationManagerService.this.cancelNotificationInternal(str, str2, Binder.getCallingUid(), Binder.getCallingPid(), str3, i, i2);
        }

        public void cancelAllNotifications(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            NotificationManagerService.this.cancelAllNotificationsInt(Binder.getCallingUid(), Binder.getCallingPid(), str, null, 0, 32832, true, ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, true, false, "cancelAllNotifications", str), 9, null);
        }

        public void silenceNotificationSound() {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mNotificationDelegate.clearEffects();
        }

        public void setNotificationsEnabledForPackage(String str, int i, boolean z) {
            enforceSystemOrSystemUI("setNotificationsEnabledForPackage");
            if (NotificationManagerService.this.mPermissionHelper.hasPermission(i) == z) {
                return;
            }
            NotificationManagerService.this.mPermissionHelper.setNotificationPermission(str, UserHandle.getUserId(i), z, true);
            NotificationManagerService.this.sendAppBlockStateChangedBroadcast(str, i, !z);
            NotificationManagerService.this.mMetricsLogger.write(new LogMaker(147).setType(4).setPackageName(str).setSubtype(z ? 1 : 0));
            NotificationManagerService.this.mNotificationChannelLogger.logAppNotificationsAllowed(i, str, z);
            if (!z) {
                NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, null, 0, 0, true, UserHandle.getUserId(i), 7, null);
            }
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public void setNotificationsEnabledWithImportanceLockForPackage(String str, int i, boolean z) {
            setNotificationsEnabledForPackage(str, i, z);
        }

        public boolean areNotificationsEnabled(String str) {
            return areNotificationsEnabledForPackage(str, Binder.getCallingUid());
        }

        public boolean areNotificationsEnabledForPackage(String str, int i) {
            enforceSystemOrSystemUIOrSamePackage(str, "Caller not system or systemui or same package");
            if (UserHandle.getCallingUserId() != UserHandle.getUserId(i)) {
                NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "canNotifyAsPackage for uid " + i);
            }
            return NotificationManagerService.this.areNotificationsEnabledForPackageInt(str, i);
        }

        public boolean areBubblesAllowed(String str) {
            return getBubblePreferenceForPackage(str, Binder.getCallingUid()) == 1;
        }

        public boolean areBubblesEnabled(UserHandle userHandle) {
            if (UserHandle.getCallingUserId() != userHandle.getIdentifier()) {
                NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "areBubblesEnabled for user " + userHandle.getIdentifier());
            }
            return NotificationManagerService.this.mPreferencesHelper.bubblesEnabled(userHandle);
        }

        public int getBubblePreferenceForPackage(String str, int i) {
            enforceSystemOrSystemUIOrSamePackage(str, "Caller not system or systemui or same package");
            if (UserHandle.getCallingUserId() != UserHandle.getUserId(i)) {
                NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "getBubblePreferenceForPackage for uid " + i);
            }
            return NotificationManagerService.this.mPreferencesHelper.getBubblePreference(str, i);
        }

        public void setBubblesAllowed(String str, int i, int i2) {
            NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell("Caller not system or sysui or shell");
            NotificationManagerService.this.mPreferencesHelper.setBubblesAllowed(str, i, i2);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public boolean shouldHideSilentStatusIcons(String str) {
            NotificationManagerService.this.checkCallerIsSameApp(str);
            if (NotificationManagerService.this.isCallerSystemOrPhone() || NotificationManagerService.this.mListeners.isListenerPackage(str)) {
                return NotificationManagerService.this.mPreferencesHelper.shouldHideSilentStatusIcons();
            }
            throw new SecurityException("Only available for notification listeners");
        }

        public void setHideSilentStatusIcons(boolean z) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mPreferencesHelper.setHideSilentStatusIcons(z);
            NotificationManagerService.this.handleSavePolicyFile();
            NotificationManagerService.this.mListeners.onStatusBarIconsBehaviorChanged(z);
        }

        public void deleteNotificationHistoryItem(String str, int i, long j) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mHistoryManager.deleteNotificationHistoryItem(str, i, j);
        }

        public NotificationListenerFilter getListenerFilter(ComponentName componentName, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mListeners.getNotificationListenerFilter(Pair.create(componentName, Integer.valueOf(i)));
        }

        public void setListenerFilter(ComponentName componentName, int i, NotificationListenerFilter notificationListenerFilter) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mListeners.setNotificationListenerFilter(Pair.create(componentName, Integer.valueOf(i)), notificationListenerFilter);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public int getPackageImportance(String str) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            return NotificationManagerService.this.mPermissionHelper.hasPermission(Binder.getCallingUid()) ? 3 : 0;
        }

        public boolean isImportanceLocked(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.isImportanceLocked(str, i);
        }

        public boolean canShowBadge(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.canShowBadge(str, i);
        }

        public void setShowBadge(String str, int i, boolean z) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mPreferencesHelper.setShowBadge(str, i, z);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public boolean hasSentValidMsg(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.hasSentValidMsg(str, i);
        }

        public boolean isInInvalidMsgState(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.isInInvalidMsgState(str, i);
        }

        public boolean hasUserDemotedInvalidMsgApp(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.hasUserDemotedInvalidMsgApp(str, i);
        }

        public void setInvalidMsgAppDemoted(String str, int i, boolean z) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mPreferencesHelper.setInvalidMsgAppDemoted(str, i, z);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public boolean hasSentValidBubble(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.hasSentValidBubble(str, i);
        }

        public void setNotificationDelegate(String str, String str2) {
            NotificationManagerService.this.checkCallerIsSameApp(str);
            int callingUid = Binder.getCallingUid();
            UserHandle userHandleForUid = UserHandle.getUserHandleForUid(callingUid);
            if (str2 == null) {
                NotificationManagerService.this.mPreferencesHelper.revokeNotificationDelegate(str, Binder.getCallingUid());
                NotificationManagerService.this.handleSavePolicyFile();
                return;
            }
            try {
                ApplicationInfo applicationInfo = NotificationManagerService.this.mPackageManager.getApplicationInfo(str2, 786432L, userHandleForUid.getIdentifier());
                if (applicationInfo != null) {
                    NotificationManagerService.this.mPreferencesHelper.setNotificationDelegate(str, callingUid, str2, applicationInfo.uid);
                    NotificationManagerService.this.handleSavePolicyFile();
                }
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }

        public String getNotificationDelegate(String str) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            return NotificationManagerService.this.mPreferencesHelper.getNotificationDelegate(str, Binder.getCallingUid());
        }

        public boolean canNotifyAsPackage(String str, String str2, int i) {
            NotificationManagerService.this.checkCallerIsSameApp(str);
            int callingUid = Binder.getCallingUid();
            if (UserHandle.getUserHandleForUid(callingUid).getIdentifier() != i) {
                NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "canNotifyAsPackage for user " + i);
            }
            if (str.equals(str2)) {
                return true;
            }
            try {
                ApplicationInfo applicationInfo = NotificationManagerService.this.mPackageManager.getApplicationInfo(str2, 786432L, i);
                if (applicationInfo != null) {
                    return NotificationManagerService.this.mPreferencesHelper.isDelegateAllowed(str2, applicationInfo.uid, str, callingUid);
                }
                return false;
            } catch (RemoteException unused) {
                return false;
            }
        }

        public boolean canUseFullScreenIntent(AttributionSource attributionSource) {
            String packageName = attributionSource.getPackageName();
            int uid = attributionSource.getUid();
            int userId = UserHandle.getUserId(uid);
            NotificationManagerService.this.checkCallerIsSameApp(packageName, uid, userId);
            try {
                return NotificationManagerService.this.checkUseFullScreenIntentPermission(attributionSource, NotificationManagerService.this.mPackageManagerClient.getApplicationInfoAsUser(packageName, 268435456, userId), NotificationManagerService.this.mFlagResolver.isEnabled(SystemUiSystemPropertiesFlags.NotificationFlags.SHOW_STICKY_HUN_FOR_DENIED_FSI), false);
            } catch (PackageManager.NameNotFoundException e) {
                Slog.e(NotificationManagerService.TAG, "Failed to getApplicationInfo() in canUseFullScreenIntent()", e);
                return false;
            }
        }

        public void updateNotificationChannelGroupForPackage(String str, int i, NotificationChannelGroup notificationChannelGroup) throws RemoteException {
            enforceSystemOrSystemUI("Caller not system or systemui");
            NotificationManagerService.this.createNotificationChannelGroup(str, i, notificationChannelGroup, false, false);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public void createNotificationChannelGroups(String str, ParceledListSlice parceledListSlice) throws RemoteException {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            List list = parceledListSlice.getList();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NotificationManagerService.this.createNotificationChannelGroup(str, Binder.getCallingUid(), (NotificationChannelGroup) list.get(i), true, false);
            }
            NotificationManagerService.this.handleSavePolicyFile();
        }

        private void createNotificationChannelsImpl(String str, int i, ParceledListSlice parceledListSlice) {
            createNotificationChannelsImpl(str, i, parceledListSlice, -1);
        }

        private void createNotificationChannelsImpl(String str, int i, ParceledListSlice parceledListSlice, int i2) {
            List list = parceledListSlice.getList();
            int size = list.size();
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() == null || !NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldLimitChannels(NotificationManagerService.this.mPreferencesHelper, str, i, size)) {
                ParceledListSlice<NotificationChannel> notificationChannels = NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(str, i, true);
                boolean z = (notificationChannels == null || notificationChannels.getList().isEmpty()) ? false : true;
                boolean z2 = false;
                int i3 = 0;
                boolean z3 = false;
                while (i3 < size) {
                    NotificationChannel notificationChannel = (NotificationChannel) list.get(i3);
                    Objects.requireNonNull(notificationChannel, "channel in list is null");
                    NotificationManagerService notificationManagerService = NotificationManagerService.this;
                    int i4 = i3;
                    z2 = notificationManagerService.mPreferencesHelper.createNotificationChannel(str, i, notificationChannel, true, notificationManagerService.mConditionProviders.isPackageOrComponentAllowed(str, UserHandle.getUserId(i)), Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
                    if (z2) {
                        NotificationManagerService.this.mListeners.notifyNotificationChannelChanged(str, UserHandle.getUserHandleForUid(i), NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(str, i, notificationChannel.getId(), false), 1);
                        boolean z4 = z || z3;
                        if (!z4) {
                            ParceledListSlice<NotificationChannel> notificationChannels2 = NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(str, i, true);
                            z4 = (notificationChannels2 == null || notificationChannels2.getList().isEmpty()) ? false : true;
                        }
                        if (!z && z4 && !z3 && i2 != -1) {
                            if (NotificationManagerService.this.mPermissionPolicyInternal == null) {
                                NotificationManagerService.this.mPermissionPolicyInternal = (PermissionPolicyInternal) LocalServices.getService(PermissionPolicyInternal.class);
                            }
                            NotificationManagerService.this.mHandler.post(new ShowNotificationPermissionPromptRunnable(str, UserHandle.getUserId(i), i2, NotificationManagerService.this.mPermissionPolicyInternal));
                            z3 = true;
                        }
                    }
                    i3 = i4 + 1;
                }
                if (z2) {
                    NotificationManagerService.this.handleSavePolicyFile();
                }
            }
        }

        public void createNotificationChannels(String str, ParceledListSlice parceledListSlice) {
            int i;
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            try {
                i = NotificationManagerService.this.mAtm.getTaskToShowPermissionDialogOn(str, NotificationManagerService.this.mPackageManager.getPackageUid(str, 0L, UserHandle.getUserId(Binder.getCallingUid())));
            } catch (RemoteException unused) {
                i = -1;
            }
            createNotificationChannelsImpl(str, Binder.getCallingUid(), parceledListSlice, i);
        }

        public void createNotificationChannelsForPackage(String str, int i, ParceledListSlice parceledListSlice) {
            enforceSystemOrSystemUI("only system can call this");
            createNotificationChannelsImpl(str, i, parceledListSlice);
        }

        public void createConversationNotificationChannelForPackage(String str, int i, NotificationChannel notificationChannel, String str2) {
            enforceSystemOrSystemUI("only system can call this");
            Preconditions.checkNotNull(notificationChannel);
            Preconditions.checkNotNull(str2);
            String id = notificationChannel.getId();
            notificationChannel.setId(String.format("%1$s : %2$s", id, str2));
            notificationChannel.setConversationId(id, str2);
            createNotificationChannelsImpl(str, i, new ParceledListSlice(Arrays.asList(notificationChannel)));
            NotificationManagerService.this.mRankingHandler.requestSort();
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public NotificationChannel getNotificationChannel(String str, int i, String str2, String str3) {
            return getConversationNotificationChannel(str, i, str2, str3, true, null);
        }

        public NotificationChannel getConversationNotificationChannel(String str, int i, String str2, String str3, boolean z, String str4) {
            int i2;
            if (canNotifyAsPackage(str, str2, i) || NotificationManagerService.this.isCallerIsSystemOrSysemUiOrShell()) {
                try {
                    i2 = NotificationManagerService.this.mPackageManagerClient.getPackageUidAsUser(str2, i);
                } catch (PackageManager.NameNotFoundException unused) {
                    i2 = -1;
                }
                int i3 = i2;
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !NotificationManagerService.this.isCallerIsSystemOrSysemUiOrShell()) {
                    return NotificationManagerService.this.mNMSWrapper.getNMSExt().getConversationNotificationChannel(str2, i3, str3, str4, z, false);
                }
                return NotificationManagerService.this.mPreferencesHelper.getConversationNotificationChannel(str2, i3, str3, str4, z, false);
            }
            throw new SecurityException("Pkg " + str + " cannot read channels for " + str2 + " in " + i);
        }

        public NotificationChannel getNotificationChannelForPackage(String str, int i, String str2, String str3, boolean z) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mPreferencesHelper.getConversationNotificationChannel(str, i, str2, str3, true, z);
        }

        private void enforceDeletingChannelHasNoFgService(String str, int i, String str2) {
            if (NotificationManagerService.this.mAmi.hasForegroundServiceNotification(str, i, str2)) {
                Slog.w(NotificationManagerService.TAG, "Package u" + i + SliceClientPermissions.SliceAuthority.DELIMITER + str + " may not delete notification channel '" + str2 + "' with fg service");
                throw new SecurityException("Not allowed to delete channel " + str2 + " with a foreground service");
            }
        }

        private void enforceDeletingChannelHasNoUserInitiatedJob(String str, int i, String str2) {
            JobSchedulerInternal jobSchedulerInternal = (JobSchedulerInternal) LocalServices.getService(JobSchedulerInternal.class);
            if (jobSchedulerInternal == null || !jobSchedulerInternal.isNotificationChannelAssociatedWithAnyUserInitiatedJobs(str2, i, str)) {
                return;
            }
            Slog.w(NotificationManagerService.TAG, "Package u" + i + SliceClientPermissions.SliceAuthority.DELIMITER + str + " may not delete notification channel '" + str2 + "' with user-initiated job");
            throw new SecurityException("Not allowed to delete channel " + str2 + " with a user-initiated job");
        }

        public void deleteNotificationChannel(String str, String str2) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            int callingUid = Binder.getCallingUid();
            boolean isCallerIsSystemOrSystemUi = NotificationManagerService.this.isCallerIsSystemOrSystemUi();
            int userId = UserHandle.getUserId(callingUid);
            if ("miscellaneous".equals(str2)) {
                throw new IllegalArgumentException("Cannot delete default channel");
            }
            enforceDeletingChannelHasNoFgService(str, userId, str2);
            enforceDeletingChannelHasNoUserInitiatedJob(str, userId, str2);
            NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, str2, 0, 0, true, userId, 20, null);
            if (NotificationManagerService.this.mPreferencesHelper.deleteNotificationChannel(str, callingUid, str2, callingUid, isCallerIsSystemOrSystemUi)) {
                NotificationManagerService.this.mArchive.removeChannelNotifications(str, userId, str2);
                NotificationManagerService.this.mHistoryManager.deleteNotificationChannel(str, callingUid, str2);
                NotificationManagerService.this.mListeners.notifyNotificationChannelChanged(str, UserHandle.getUserHandleForUid(callingUid), NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(str, callingUid, str2, true), 3);
                NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public NotificationChannelGroup getNotificationChannelGroup(String str, String str2) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !NotificationManagerService.this.isCallerSystemOrPhone()) {
                return NotificationManagerService.this.mNMSWrapper.getNMSExt().getNotificationChannelGroupWithChannels(str, Binder.getCallingUid(), str2, false);
            }
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroupWithChannels(str, Binder.getCallingUid(), str2, false);
        }

        public ParceledListSlice<NotificationChannelGroup> getNotificationChannelGroups(String str) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !NotificationManagerService.this.isCallerSystemOrPhone()) {
                return NotificationManagerService.this.mNMSWrapper.getNMSExt().getNotificationChannelGroups(str, Binder.getCallingUid(), false, false, true);
            }
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(str, Binder.getCallingUid(), false, false, true);
        }

        public void deleteNotificationChannelGroup(String str, String str2) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            int callingUid = Binder.getCallingUid();
            boolean isCallerIsSystemOrSystemUi = NotificationManagerService.this.isCallerIsSystemOrSystemUi();
            NotificationChannelGroup notificationChannelGroupWithChannels = NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroupWithChannels(str, callingUid, str2, false);
            if (notificationChannelGroupWithChannels != null) {
                int userId = UserHandle.getUserId(callingUid);
                List<NotificationChannel> channels = notificationChannelGroupWithChannels.getChannels();
                for (int i = 0; i < channels.size(); i++) {
                    String id = channels.get(i).getId();
                    enforceDeletingChannelHasNoFgService(str, userId, id);
                    enforceDeletingChannelHasNoUserInitiatedJob(str, userId, id);
                }
                int i2 = 0;
                for (List<NotificationChannel> deleteNotificationChannelGroup = NotificationManagerService.this.mPreferencesHelper.deleteNotificationChannelGroup(str, callingUid, str2, callingUid, isCallerIsSystemOrSystemUi); i2 < deleteNotificationChannelGroup.size(); deleteNotificationChannelGroup = deleteNotificationChannelGroup) {
                    NotificationChannel notificationChannel = deleteNotificationChannelGroup.get(i2);
                    NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, notificationChannel.getId(), 0, 0, true, userId, 20, null);
                    NotificationManagerService.this.mListeners.notifyNotificationChannelChanged(str, UserHandle.getUserHandleForUid(callingUid), notificationChannel, 3);
                    i2++;
                }
                NotificationManagerService.this.mListeners.notifyNotificationChannelGroupChanged(str, UserHandle.getUserHandleForUid(callingUid), notificationChannelGroupWithChannels, 3);
                NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public void updateNotificationChannelForPackage(String str, int i, NotificationChannel notificationChannel) {
            NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell("Caller not system or sysui or shell");
            Objects.requireNonNull(notificationChannel);
            NotificationManagerService.this.updateNotificationChannelInt(str, i, notificationChannel, false);
        }

        public void unlockNotificationChannel(String str, int i, String str2) {
            NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell("Caller not system or sysui or shell");
            NotificationManagerService.this.mPreferencesHelper.unlockNotificationChannelImportance(str, i, str2);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public void unlockAllNotificationChannels() {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mPreferencesHelper.unlockAllNotificationChannels();
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public ParceledListSlice<NotificationChannel> getNotificationChannelsForPackage(String str, int i, boolean z) {
            enforceSystemOrSystemUI("getNotificationChannelsForPackage");
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(str, i, z);
        }

        public int getNumNotificationChannelsForPackage(String str, int i, boolean z) {
            enforceSystemOrSystemUI("getNumNotificationChannelsForPackage");
            return NotificationManagerService.this.getNumNotificationChannelsForPackage(str, i, z);
        }

        public boolean onlyHasDefaultChannel(String str, int i) {
            enforceSystemOrSystemUI("onlyHasDefaultChannel");
            return NotificationManagerService.this.mPreferencesHelper.onlyHasDefaultChannel(str, i);
        }

        public int getDeletedChannelCount(String str, int i) {
            enforceSystemOrSystemUI("getDeletedChannelCount");
            return NotificationManagerService.this.mPreferencesHelper.getDeletedChannelCount(str, i);
        }

        public int getBlockedChannelCount(String str, int i) {
            enforceSystemOrSystemUI("getBlockedChannelCount");
            return NotificationManagerService.this.mPreferencesHelper.getBlockedChannelCount(str, i);
        }

        public ParceledListSlice<ConversationChannelWrapper> getConversations(boolean z) {
            enforceSystemOrSystemUI("getConversations");
            ArrayList<ConversationChannelWrapper> conversations = NotificationManagerService.this.mPreferencesHelper.getConversations(NotificationManagerService.this.mUserProfiles.getCurrentProfileIds(), z);
            Iterator<ConversationChannelWrapper> it = conversations.iterator();
            while (it.hasNext()) {
                ConversationChannelWrapper next = it.next();
                if (NotificationManagerService.this.mShortcutHelper == null) {
                    next.setShortcutInfo((ShortcutInfo) null);
                } else {
                    next.setShortcutInfo(NotificationManagerService.this.mShortcutHelper.getValidShortcutInfo(next.getNotificationChannel().getConversationId(), next.getPkg(), UserHandle.of(UserHandle.getUserId(next.getUid()))));
                }
            }
            return new ParceledListSlice<>(conversations);
        }

        public ParceledListSlice<NotificationChannelGroup> getNotificationChannelGroupsForPackage(String str, int i, boolean z) {
            enforceSystemOrSystemUI("getNotificationChannelGroupsForPackage");
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(str, i, z, true, false);
        }

        public ParceledListSlice<ConversationChannelWrapper> getConversationsForPackage(String str, int i) {
            enforceSystemOrSystemUI("getConversationsForPackage");
            ArrayList<ConversationChannelWrapper> conversations = NotificationManagerService.this.mPreferencesHelper.getConversations(str, i);
            Iterator<ConversationChannelWrapper> it = conversations.iterator();
            while (it.hasNext()) {
                ConversationChannelWrapper next = it.next();
                if (NotificationManagerService.this.mShortcutHelper == null) {
                    next.setShortcutInfo((ShortcutInfo) null);
                } else {
                    next.setShortcutInfo(NotificationManagerService.this.mShortcutHelper.getValidShortcutInfo(next.getNotificationChannel().getConversationId(), str, UserHandle.of(UserHandle.getUserId(i))));
                }
            }
            return new ParceledListSlice<>(conversations);
        }

        public NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String str, int i, String str2, boolean z) {
            enforceSystemOrSystemUI("getPopulatedNotificationChannelGroupForPackage");
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroupWithChannels(str, i, str2, z);
        }

        public NotificationChannelGroup getNotificationChannelGroupForPackage(String str, String str2, int i) {
            enforceSystemOrSystemUI("getNotificationChannelGroupForPackage");
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroup(str, str2, i);
        }

        public ParceledListSlice<NotificationChannel> getNotificationChannels(String str, String str2, int i) {
            int i2;
            if (canNotifyAsPackage(str, str2, i) || NotificationManagerService.this.isCallingUidSystem()) {
                try {
                    i2 = NotificationManagerService.this.mPackageManagerClient.getPackageUidAsUser(str2, i);
                } catch (PackageManager.NameNotFoundException unused) {
                    i2 = -1;
                }
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !NotificationManagerService.this.isCallingUidSystem()) {
                    return NotificationManagerService.this.mNMSWrapper.getNMSExt().getNotificationChannels(str2, i2, false);
                }
                return NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(str2, i2, false);
            }
            throw new SecurityException("Pkg " + str + " cannot read channels for " + str2 + " in " + i);
        }

        public ParceledListSlice<NotificationChannel> getNotificationChannelsBypassingDnd(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            if (!areNotificationsEnabledForPackage(str, i)) {
                return ParceledListSlice.emptyList();
            }
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannelsBypassingDnd(str, i);
        }

        public boolean areChannelsBypassingDnd() {
            return NotificationManagerService.this.mPreferencesHelper.areChannelsBypassingDnd();
        }

        public void clearData(String str, int i, boolean z) throws RemoteException {
            Boolean bool;
            Boolean bool2;
            NotificationManagerService.this.checkCallerIsSystem();
            int userId = UserHandle.getUserId(i);
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                NotificationManagerService.this.mNMSWrapper.getNMSExt().cancelAllNotificationsInt(null, NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, null, 0, 0, true, userId == NotificationManagerService.USER_ID_MULTI_APP ? userId : UserHandle.getUserId(Binder.getCallingUid()), 21, null);
            } else {
                NotificationManagerService.this.cancelAllNotificationsInt(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, str, null, 0, 0, true, userId == NotificationManagerService.USER_ID_MULTI_APP ? userId : UserHandle.getUserId(Binder.getCallingUid()), 21, null);
            }
            boolean resetPackage = NotificationManagerService.this.mConditionProviders.resetPackage(str, userId) | false;
            ArrayMap<Boolean, ArrayList<ComponentName>> resetComponents = NotificationManagerService.this.mListeners.resetComponents(str, userId);
            boolean z2 = resetPackage | (resetComponents.get(Boolean.TRUE).size() > 0 || resetComponents.get(Boolean.FALSE).size() > 0);
            int i2 = 0;
            while (true) {
                bool = Boolean.TRUE;
                if (i2 >= resetComponents.get(bool).size()) {
                    break;
                }
                NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(resetComponents.get(bool).get(i2).getPackageName(), userId, false, true);
                i2++;
            }
            ArrayMap<Boolean, ArrayList<ComponentName>> resetComponents2 = NotificationManagerService.this.mAssistants.resetComponents(str, userId);
            boolean z3 = z2 | (resetComponents2.get(bool).size() > 0 || resetComponents2.get(Boolean.FALSE).size() > 0);
            int i3 = 1;
            while (true) {
                bool2 = Boolean.TRUE;
                if (i3 >= resetComponents2.get(bool2).size()) {
                    break;
                }
                NotificationManagerService.this.mAssistants.setPackageOrComponentEnabled(resetComponents2.get(bool2).get(i3).flattenToString(), userId, true, false);
                i3++;
            }
            if (resetComponents2.get(bool2).size() > 0) {
                NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(resetComponents2.get(bool2).get(0).getPackageName(), userId, false, true);
            }
            NotificationManagerService.this.mSnoozeHelper.clearData(UserHandle.getUserId(i), str);
            if (!z) {
                NotificationManagerService.this.mPreferencesHelper.clearData(str, i);
            }
            if (z3) {
                NotificationManagerService.this.getContext().sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(str).addFlags(67108864), UserHandle.of(userId), null);
            }
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public List<String> getAllowedAssistantAdjustments(String str) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            if (!NotificationManagerService.this.isCallerSystemOrPhone() && !NotificationManagerService.this.mAssistants.isPackageAllowed(str, UserHandle.getCallingUserId())) {
                throw new SecurityException("Not currently an assistant");
            }
            return NotificationManagerService.this.mAssistants.getAllowedAssistantAdjustments();
        }

        @Deprecated
        public StatusBarNotification[] getActiveNotifications(String str) {
            return getActiveNotificationsWithAttribution(str, null);
        }

        public StatusBarNotification[] getActiveNotificationsWithAttribution(String str, String str2) {
            NotificationManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_NOTIFICATIONS", "NotificationManagerService.getActiveNotifications");
            ArrayList arrayList = new ArrayList();
            int callingUid = Binder.getCallingUid();
            final ArrayList arrayList2 = new ArrayList();
            arrayList2.add(-1);
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$11$$ExternalSyntheticLambda1
                public final void runOrThrow() {
                    NotificationManagerService.AnonymousClass11.this.lambda$getActiveNotificationsWithAttribution$0(arrayList2);
                }
            });
            if (NotificationManagerService.this.mAppOps.noteOpNoThrow(25, callingUid, str, str2, (String) null) == 0) {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    int size = NotificationManagerService.this.mNotificationList.size();
                    for (int i = 0; i < size; i++) {
                        StatusBarNotification sbn = NotificationManagerService.this.mNotificationList.get(i).getSbn();
                        if (arrayList2.contains(Integer.valueOf(sbn.getUserId()))) {
                            arrayList.add(sbn);
                        }
                    }
                }
            }
            return (StatusBarNotification[]) arrayList.toArray(new StatusBarNotification[arrayList.size()]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getActiveNotificationsWithAttribution$0(ArrayList arrayList) throws Exception {
            for (int i : NotificationManagerService.this.mUm.getProfileIds(ActivityManager.getCurrentUser(), false)) {
                arrayList.add(Integer.valueOf(i));
            }
        }

        public ParceledListSlice<StatusBarNotification> getAppActiveNotifications(String str, int i) {
            ParceledListSlice<StatusBarNotification> parceledListSlice;
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, true, false, "getAppActiveNotifications", str);
            NotificationManagerService.mNMSExt.fixStopForegroundRemoveFlagSlow();
            synchronized (NotificationManagerService.this.mNotificationLock) {
                ArrayMap arrayMap = new ArrayMap(NotificationManagerService.this.mNotificationList.size() + NotificationManagerService.this.mEnqueuedNotifications.size());
                int size = NotificationManagerService.this.mNotificationList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    StatusBarNotification sanitizeSbn = sanitizeSbn(str, handleIncomingUser, NotificationManagerService.this.mNotificationList.get(i2).getSbn());
                    if (sanitizeSbn != null) {
                        arrayMap.put(sanitizeSbn.getKey(), sanitizeSbn);
                    }
                }
                Iterator<NotificationRecord> it = NotificationManagerService.this.mSnoozeHelper.getSnoozed(handleIncomingUser, str).iterator();
                while (it.hasNext()) {
                    StatusBarNotification sanitizeSbn2 = sanitizeSbn(str, handleIncomingUser, it.next().getSbn());
                    if (sanitizeSbn2 != null) {
                        arrayMap.put(sanitizeSbn2.getKey(), sanitizeSbn2);
                    }
                }
                int size2 = NotificationManagerService.this.mEnqueuedNotifications.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    StatusBarNotification sanitizeSbn3 = sanitizeSbn(str, handleIncomingUser, NotificationManagerService.this.mEnqueuedNotifications.get(i3).getSbn());
                    if (sanitizeSbn3 != null) {
                        arrayMap.put(sanitizeSbn3.getKey(), sanitizeSbn3);
                    }
                }
                ArrayList arrayList = new ArrayList(arrayMap.size());
                arrayList.addAll(arrayMap.values());
                parceledListSlice = new ParceledListSlice<>(arrayList);
            }
            return parceledListSlice;
        }

        private StatusBarNotification sanitizeSbn(String str, int i, StatusBarNotification statusBarNotification) {
            if (statusBarNotification.getUserId() != i) {
                return null;
            }
            if (!statusBarNotification.getPackageName().equals(str) && !statusBarNotification.getOpPkg().equals(str)) {
                return null;
            }
            Notification clone = statusBarNotification.getNotification().clone();
            clone.clearAllowlistToken();
            return new StatusBarNotification(statusBarNotification.getPackageName(), statusBarNotification.getOpPkg(), statusBarNotification.getId(), statusBarNotification.getTag(), statusBarNotification.getUid(), statusBarNotification.getInitialPid(), clone, statusBarNotification.getUser(), statusBarNotification.getOverrideGroupKey(), statusBarNotification.getPostTime());
        }

        @RequiresPermission("android.permission.ACCESS_NOTIFICATIONS")
        @Deprecated
        public StatusBarNotification[] getHistoricalNotifications(String str, int i, boolean z) {
            return getHistoricalNotificationsWithAttribution(str, null, i, z);
        }

        @RequiresPermission("android.permission.ACCESS_NOTIFICATIONS")
        public StatusBarNotification[] getHistoricalNotificationsWithAttribution(String str, String str2, int i, boolean z) {
            StatusBarNotification[] array;
            NotificationManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_NOTIFICATIONS", "NotificationManagerService.getHistoricalNotifications");
            if (NotificationManagerService.this.mAppOps.noteOpNoThrow(25, Binder.getCallingUid(), str, str2, (String) null) != 0) {
                return null;
            }
            synchronized (NotificationManagerService.this.mArchive) {
                array = NotificationManagerService.this.mArchive.getArray(NotificationManagerService.this.mUm, i, z);
            }
            return array;
        }

        @RequiresPermission("android.permission.ACCESS_NOTIFICATIONS")
        public NotificationHistory getNotificationHistory(String str, String str2) {
            NotificationManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_NOTIFICATIONS", "NotificationManagerService.getNotificationHistory");
            if (NotificationManagerService.this.mAppOps.noteOpNoThrow(25, Binder.getCallingUid(), str, str2, (String) null) == 0) {
                IntArray currentProfileIds = NotificationManagerService.this.mUserProfiles.getCurrentProfileIds();
                Trace.traceBegin(524288L, "notifHistoryReadHistory");
                try {
                    return NotificationManagerService.this.mHistoryManager.readNotificationHistory(currentProfileIds.toArray());
                } finally {
                    Trace.traceEnd(524288L);
                }
            }
            return new NotificationHistory();
        }

        public void registerListener(INotificationListener iNotificationListener, ComponentName componentName, int i) {
            enforceSystemOrSystemUI("INotificationManager.registerListener");
            NotificationManagerService.this.mListeners.registerSystemService(iNotificationListener, componentName, i, Binder.getCallingUid());
        }

        public void unregisterListener(INotificationListener iNotificationListener, int i) {
            NotificationManagerService.this.mListeners.unregisterService((IInterface) iNotificationListener, i);
        }

        public void cancelNotificationsFromListener(INotificationListener iNotificationListener, String[] strArr) {
            int userId;
            int i;
            int i2;
            ManagedServices.ManagedServiceInfo managedServiceInfo;
            String[] strArr2 = strArr;
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    int i3 = NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(iNotificationListener) ? 22 : 10;
                    if (strArr2 != null) {
                        int length = strArr2.length;
                        int i4 = 0;
                        while (i4 < length) {
                            NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(strArr2[i4]);
                            if (notificationRecord != null && ((userId = notificationRecord.getSbn().getUserId()) == checkServiceTokenLocked.userid || userId == -1 || NotificationManagerService.this.mUserProfiles.isCurrentProfile(userId))) {
                                i = i4;
                                i2 = length;
                                managedServiceInfo = checkServiceTokenLocked;
                                cancelNotificationFromListenerLocked(checkServiceTokenLocked, callingUid, callingPid, notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getTag(), notificationRecord.getSbn().getId(), userId, i3);
                                i4 = i + 1;
                                checkServiceTokenLocked = managedServiceInfo;
                                length = i2;
                                strArr2 = strArr;
                            }
                            i = i4;
                            i2 = length;
                            managedServiceInfo = checkServiceTokenLocked;
                            i4 = i + 1;
                            checkServiceTokenLocked = managedServiceInfo;
                            length = i2;
                            strArr2 = strArr;
                        }
                    } else {
                        NotificationManagerService.this.cancelAllLocked(callingUid, callingPid, checkServiceTokenLocked.userid, 11, checkServiceTokenLocked, checkServiceTokenLocked.supportsProfiles());
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestBindListener(ComponentName componentName) {
            ManagedServices managedServices;
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(componentName.getPackageName());
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (NotificationManagerService.this.mAssistants.isComponentEnabledForCurrentProfiles(componentName)) {
                    managedServices = NotificationManagerService.this.mAssistants;
                } else {
                    managedServices = NotificationManagerService.this.mListeners;
                }
                managedServices.setComponentState(componentName, UserHandle.getUserId(callingUid), true);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestUnbindListener(INotificationListener iNotificationListener) {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    checkServiceTokenLocked.getOwner().setComponentState(checkServiceTokenLocked.component, UserHandle.getUserId(callingUid), false);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestUnbindListenerComponent(ComponentName componentName) {
            ManagedServices managedServices;
            NotificationManagerService.this.checkCallerIsSameApp(componentName.getPackageName());
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    if (NotificationManagerService.this.mAssistants.isComponentEnabledForCurrentProfiles(componentName)) {
                        managedServices = NotificationManagerService.this.mAssistants;
                    } else {
                        managedServices = NotificationManagerService.this.mListeners;
                    }
                    if (managedServices.isPackageOrComponentAllowed(componentName.flattenToString(), UserHandle.getUserId(callingUid))) {
                        managedServices.setComponentState(componentName, UserHandle.getUserId(callingUid), false);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setNotificationsShownFromListener(INotificationListener iNotificationListener, String[] strArr) {
            int userId;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    if (strArr == null) {
                        return;
                    }
                    ArrayList<NotificationRecord> arrayList = new ArrayList<>();
                    int length = strArr.length;
                    for (int i = 0; i < length; i++) {
                        NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(strArr[i]);
                        if (notificationRecord != null && ((userId = notificationRecord.getSbn().getUserId()) == checkServiceTokenLocked.userid || userId == -1 || NotificationManagerService.this.mUserProfiles.isCurrentProfile(userId))) {
                            arrayList.add(notificationRecord);
                            if (!notificationRecord.isSeen()) {
                                if (NotificationManagerService.DBG) {
                                    Slog.d(NotificationManagerService.TAG, "Marking notification as seen " + strArr[i]);
                                }
                                NotificationManagerService.this.reportSeen(notificationRecord);
                                notificationRecord.setSeen();
                                NotificationManagerService.this.maybeRecordInterruptionLocked(notificationRecord);
                            }
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        NotificationManagerService.this.mAssistants.onNotificationsSeenLocked(arrayList);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        @GuardedBy({"mNotificationLock"})
        private void cancelNotificationFromListenerLocked(ManagedServices.ManagedServiceInfo managedServiceInfo, int i, int i2, String str, String str2, int i3, int i4, int i5) {
            NotificationManagerService.this.cancelNotification(i, i2, str, str2, i3, 0, 2, true, i4, i5, managedServiceInfo);
        }

        public void snoozeNotificationUntilContextFromListener(INotificationListener iNotificationListener, String str, String str2) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.snoozeNotificationInt(str, -1L, str2, NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener));
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void snoozeNotificationUntilFromListener(INotificationListener iNotificationListener, String str, long j) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.snoozeNotificationInt(str, j, null, NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener));
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void unsnoozeNotificationFromAssistant(INotificationListener iNotificationListener, String str) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.unsnoozeNotificationInt(str, NotificationManagerService.this.mAssistants.checkServiceTokenLocked(iNotificationListener), false);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void unsnoozeNotificationFromSystemListener(INotificationListener iNotificationListener, String str) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    if (!checkServiceTokenLocked.isSystem) {
                        throw new SecurityException("Not allowed to unsnooze before deadline");
                    }
                    NotificationManagerService.this.unsnoozeNotificationInt(str, checkServiceTokenLocked, true);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void migrateNotificationFilter(INotificationListener iNotificationListener, int i, List<String> list) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    Pair<ComponentName, Integer> create = Pair.create(checkServiceTokenLocked.component, Integer.valueOf(checkServiceTokenLocked.userid));
                    NotificationListenerFilter notificationListenerFilter = NotificationManagerService.this.mListeners.getNotificationListenerFilter(create);
                    if (notificationListenerFilter == null) {
                        notificationListenerFilter = new NotificationListenerFilter();
                    }
                    if (notificationListenerFilter.getDisallowedPackages().isEmpty() && list != null) {
                        for (String str : list) {
                            for (int i2 : NotificationManagerService.this.mUm.getProfileIds(checkServiceTokenLocked.userid, false)) {
                                try {
                                    int uidForPackageAndUser = getUidForPackageAndUser(str, UserHandle.of(i2));
                                    if (uidForPackageAndUser != -1) {
                                        notificationListenerFilter.addPackage(new VersionedPackage(str, uidForPackageAndUser));
                                    }
                                } catch (Exception unused) {
                                }
                            }
                        }
                    }
                    if (notificationListenerFilter.areAllTypesAllowed()) {
                        notificationListenerFilter.setTypes(i);
                    }
                    NotificationManagerService.this.mListeners.setNotificationListenerFilter(create, notificationListenerFilter);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancelNotificationFromListener(INotificationListener iNotificationListener, String str, String str2, int i) {
            int callingUid = Binder.getCallingUid();
            int callingPid = Binder.getCallingPid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    int i2 = NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(iNotificationListener) ? 22 : 10;
                    if (checkServiceTokenLocked.supportsProfiles()) {
                        Slog.e(NotificationManagerService.TAG, "Ignoring deprecated cancelNotification(pkg, tag, id) from " + checkServiceTokenLocked.component + " use cancelNotification(key) instead.");
                    } else {
                        cancelNotificationFromListenerLocked(checkServiceTokenLocked, callingUid, callingPid, str, str2, i, checkServiceTokenLocked.userid, i2);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public ParceledListSlice<StatusBarNotification> getActiveNotificationsFromListener(INotificationListener iNotificationListener, String[] strArr, int i) {
            ParceledListSlice<StatusBarNotification> parceledListSlice;
            NotificationRecord notificationRecord;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                boolean z = strArr != null;
                int length = z ? strArr.length : NotificationManagerService.this.mNotificationList.size();
                ArrayList arrayList = new ArrayList(length);
                for (int i2 = 0; i2 < length; i2++) {
                    if (z) {
                        notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(strArr[i2]);
                    } else {
                        notificationRecord = NotificationManagerService.this.mNotificationList.get(i2);
                    }
                    if (notificationRecord != null) {
                        StatusBarNotification sbn = notificationRecord.getSbn();
                        if (NotificationManagerService.this.isVisibleToListener(sbn, notificationRecord.getNotificationType(), checkServiceTokenLocked)) {
                            if (i != 0) {
                                sbn = sbn.cloneLight();
                            }
                            arrayList.add(sbn);
                        }
                    }
                }
                parceledListSlice = new ParceledListSlice<>(arrayList);
            }
            return parceledListSlice;
        }

        public ParceledListSlice<StatusBarNotification> getSnoozedNotificationsFromListener(INotificationListener iNotificationListener, int i) {
            ParceledListSlice<StatusBarNotification> parceledListSlice;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                List<NotificationRecord> snoozed = NotificationManagerService.this.mSnoozeHelper.getSnoozed();
                int size = snoozed.size();
                ArrayList arrayList = new ArrayList(size);
                for (int i2 = 0; i2 < size; i2++) {
                    NotificationRecord notificationRecord = snoozed.get(i2);
                    if (notificationRecord != null) {
                        StatusBarNotification sbn = notificationRecord.getSbn();
                        if (NotificationManagerService.this.isVisibleToListener(sbn, notificationRecord.getNotificationType(), checkServiceTokenLocked)) {
                            if (i != 0) {
                                sbn = sbn.cloneLight();
                            }
                            arrayList.add(sbn);
                        }
                    }
                }
                parceledListSlice = new ParceledListSlice<>(arrayList);
            }
            return parceledListSlice;
        }

        public void clearRequestedListenerHints(INotificationListener iNotificationListener) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.removeDisabledHints(NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener));
                    NotificationManagerService.this.updateListenerHintsLocked();
                    NotificationManagerService.this.updateEffectsSuppressorLocked();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestHintsFromListener(INotificationListener iNotificationListener, int i) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                    if ((i & 7) != 0) {
                        NotificationManagerService.this.addDisabledHints(checkServiceTokenLocked, i);
                    } else {
                        NotificationManagerService.this.removeDisabledHints(checkServiceTokenLocked, i);
                    }
                    NotificationManagerService.this.updateListenerHintsLocked();
                    NotificationManagerService.this.updateEffectsSuppressorLocked();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getHintsFromListener(INotificationListener iNotificationListener) {
            int i;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                i = NotificationManagerService.this.mListenerHints;
            }
            return i;
        }

        public int getHintsFromListenerNoToken() {
            int i;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                i = NotificationManagerService.this.mListenerHints;
            }
            return i;
        }

        public void requestInterruptionFilterFromListener(INotificationListener iNotificationListener, int i) throws RemoteException {
            int callingUid = Binder.getCallingUid();
            boolean isCallerIsSystemOrSystemUi = NotificationManagerService.this.isCallerIsSystemOrSystemUi();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.mZenModeHelper.requestFromListener(NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener).component, i, callingUid, isCallerIsSystemOrSystemUi);
                    NotificationManagerService.this.updateInterruptionFilterLocked();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public int getInterruptionFilterFromListener(INotificationListener iNotificationListener) throws RemoteException {
            int i;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                i = NotificationManagerService.this.mInterruptionFilter;
            }
            return i;
        }

        public void setOnNotificationPostedTrimFromListener(INotificationListener iNotificationListener, int i) throws RemoteException {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                ManagedServices.ManagedServiceInfo checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
                if (checkServiceTokenLocked == null) {
                    return;
                }
                NotificationManagerService.this.mListeners.setOnNotificationPostedTrimLocked(checkServiceTokenLocked, i);
            }
        }

        public int getZenMode() {
            return NotificationManagerService.this.mZenModeHelper.getZenMode();
        }

        public ZenModeConfig getZenModeConfig() {
            enforceSystemOrSystemUI("INotificationManager.getZenModeConfig");
            return NotificationManagerService.this.mZenModeHelper.getConfig();
        }

        public void setZenMode(int i, Uri uri, String str) throws RemoteException {
            enforceSystemOrSystemUI("INotificationManager.setZenMode");
            int callingUid = Binder.getCallingUid();
            boolean isCallerIsSystemOrSystemUi = NotificationManagerService.this.isCallerIsSystemOrSystemUi();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                NotificationManagerService.this.mZenModeHelper.setManualZenMode(i, uri, null, str, callingUid, isCallerIsSystemOrSystemUi);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public List<ZenModeConfig.ZenRule> getZenRules() throws RemoteException {
            enforcePolicyAccess(Binder.getCallingUid(), "getAutomaticZenRules");
            return NotificationManagerService.this.mZenModeHelper.getZenRules();
        }

        public AutomaticZenRule getAutomaticZenRule(String str) throws RemoteException {
            Objects.requireNonNull(str, "Id is null");
            enforcePolicyAccess(Binder.getCallingUid(), "getAutomaticZenRule");
            return NotificationManagerService.this.mZenModeHelper.getAutomaticZenRule(str);
        }

        public String addAutomaticZenRule(AutomaticZenRule automaticZenRule, String str) {
            Objects.requireNonNull(automaticZenRule, "automaticZenRule is null");
            Objects.requireNonNull(automaticZenRule.getName(), "Name is null");
            if (automaticZenRule.getOwner() == null && automaticZenRule.getConfigurationActivity() == null) {
                throw new NullPointerException("Rule must have a conditionproviderservice and/or configuration activity");
            }
            Objects.requireNonNull(automaticZenRule.getConditionId(), "ConditionId is null");
            NotificationManagerService.this.checkCallerIsSameApp(str);
            if (automaticZenRule.getZenPolicy() != null && automaticZenRule.getInterruptionFilter() != 2) {
                throw new IllegalArgumentException("ZenPolicy is only applicable to INTERRUPTION_FILTER_PRIORITY filters");
            }
            enforcePolicyAccess(Binder.getCallingUid(), "addAutomaticZenRule");
            if (NotificationManagerService.this.isCallingAppIdSystem() && automaticZenRule.getOwner() != null) {
                str = automaticZenRule.getOwner().getPackageName();
            }
            return NotificationManagerService.this.mZenModeHelper.addAutomaticZenRule(str, automaticZenRule, "addAutomaticZenRule", Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
        }

        public boolean updateAutomaticZenRule(String str, AutomaticZenRule automaticZenRule) throws RemoteException {
            Objects.requireNonNull(automaticZenRule, "automaticZenRule is null");
            Objects.requireNonNull(automaticZenRule.getName(), "Name is null");
            if (automaticZenRule.getOwner() == null && automaticZenRule.getConfigurationActivity() == null) {
                throw new NullPointerException("Rule must have a conditionproviderservice and/or configuration activity");
            }
            Objects.requireNonNull(automaticZenRule.getConditionId(), "ConditionId is null");
            enforcePolicyAccess(Binder.getCallingUid(), "updateAutomaticZenRule");
            return NotificationManagerService.this.mZenModeHelper.updateAutomaticZenRule(str, automaticZenRule, "updateAutomaticZenRule", Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
        }

        public boolean removeAutomaticZenRule(String str) throws RemoteException {
            Objects.requireNonNull(str, "Id is null");
            enforcePolicyAccess(Binder.getCallingUid(), "removeAutomaticZenRule");
            return NotificationManagerService.this.mZenModeHelper.removeAutomaticZenRule(str, "removeAutomaticZenRule", Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
        }

        public boolean removeAutomaticZenRules(String str) throws RemoteException {
            Objects.requireNonNull(str, "Package name is null");
            enforceSystemOrSystemUI("removeAutomaticZenRules");
            return NotificationManagerService.this.mZenModeHelper.removeAutomaticZenRules(str, str + "|removeAutomaticZenRules", Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
        }

        public int getRuleInstanceCount(ComponentName componentName) throws RemoteException {
            Objects.requireNonNull(componentName, "Owner is null");
            enforceSystemOrSystemUI("getRuleInstanceCount");
            return NotificationManagerService.this.mZenModeHelper.getCurrentInstanceCount(componentName);
        }

        public void setAutomaticZenRuleState(String str, Condition condition) {
            Objects.requireNonNull(str, "id is null");
            Objects.requireNonNull(condition, "Condition is null");
            enforcePolicyAccess(Binder.getCallingUid(), "setAutomaticZenRuleState");
            NotificationManagerService.this.mZenModeHelper.setAutomaticZenRuleState(str, condition, Binder.getCallingUid(), NotificationManagerService.this.isCallerIsSystemOrSystemUi());
        }

        public void setInterruptionFilter(String str, int i) throws RemoteException {
            enforcePolicyAccess(str, "setInterruptionFilter");
            int zenModeFromInterruptionFilter = NotificationManager.zenModeFromInterruptionFilter(i, -1);
            if (zenModeFromInterruptionFilter == -1) {
                throw new IllegalArgumentException("Invalid filter: " + i);
            }
            int callingUid = Binder.getCallingUid();
            boolean isCallerIsSystemOrSystemUi = NotificationManagerService.this.isCallerIsSystemOrSystemUi();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                NotificationManagerService.this.mZenModeHelper.setManualZenMode(zenModeFromInterruptionFilter, null, str, "setInterruptionFilter", callingUid, isCallerIsSystemOrSystemUi);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void notifyConditions(final String str, IConditionProvider iConditionProvider, final Condition[] conditionArr) {
            final ManagedServices.ManagedServiceInfo checkServiceToken = NotificationManagerService.this.mConditionProviders.checkServiceToken(iConditionProvider);
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService.11.1
                @Override // java.lang.Runnable
                public void run() {
                    NotificationManagerService.this.mConditionProviders.notifyConditions(str, checkServiceToken, conditionArr);
                }
            });
        }

        public void requestUnbindProvider(IConditionProvider iConditionProvider) {
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ManagedServices.ManagedServiceInfo checkServiceToken = NotificationManagerService.this.mConditionProviders.checkServiceToken(iConditionProvider);
                checkServiceToken.getOwner().setComponentState(checkServiceToken.component, UserHandle.getUserId(callingUid), false);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void requestBindProvider(ComponentName componentName) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(componentName.getPackageName());
            int callingUid = Binder.getCallingUid();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                NotificationManagerService.this.mConditionProviders.setComponentState(componentName, UserHandle.getUserId(callingUid), true);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private void enforceSystemOrSystemUI(String str) {
            if (NotificationManagerService.this.isCallerSystemOrPhone()) {
                return;
            }
            NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", str);
        }

        private void enforceSystemOrSystemUIOrSamePackage(String str, String str2) {
            try {
                NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
            } catch (SecurityException unused) {
                NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", str2);
            }
        }

        private void enforcePolicyAccess(int i, String str) {
            if (NotificationManagerService.this.getContext().checkCallingPermission("android.permission.MANAGE_NOTIFICATIONS") == 0) {
                return;
            }
            boolean z = false;
            for (String str2 : NotificationManagerService.this.mPackageManagerClient.getPackagesForUid(i)) {
                if (NotificationManagerService.this.mConditionProviders.isPackageOrComponentAllowed(str2, UserHandle.getUserId(i))) {
                    z = true;
                }
            }
            if (z) {
                return;
            }
            Slog.w(NotificationManagerService.TAG, "Notification policy access denied calling " + str);
            throw new SecurityException("Notification policy access denied");
        }

        private void enforcePolicyAccess(String str, String str2) {
            if (NotificationManagerService.this.getContext().checkCallingPermission("android.permission.MANAGE_NOTIFICATIONS") == 0) {
                return;
            }
            NotificationManagerService.this.checkCallerIsSameApp(str);
            if (checkPolicyAccess(str)) {
                return;
            }
            Slog.w(NotificationManagerService.TAG, "Notification policy access denied calling " + str2);
            throw new SecurityException("Notification policy access denied");
        }

        private boolean checkPackagePolicyAccess(String str) {
            return NotificationManagerService.this.mConditionProviders.isPackageOrComponentAllowed(str, INotificationManager.Stub.getCallingUserHandle().getIdentifier());
        }

        private boolean checkPolicyAccess(String str) {
            try {
                if (ActivityManager.checkComponentPermission("android.permission.MANAGE_NOTIFICATIONS", NotificationManagerService.this.getContext().getPackageManager().getPackageUidAsUser(str, UserHandle.getCallingUserId()), -1, true) == 0) {
                    return true;
                }
                if (!checkPackagePolicyAccess(str) && !NotificationManagerService.this.mListeners.isComponentEnabledForPackage(str)) {
                    if (NotificationManagerService.this.mDpm == null) {
                        return false;
                    }
                    if (!NotificationManagerService.this.mDpm.isActiveProfileOwner(Binder.getCallingUid()) && !NotificationManagerService.this.mDpm.isActiveDeviceOwner(Binder.getCallingUid())) {
                        return false;
                    }
                }
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpAndUsageStatsPermission(NotificationManagerService.this.getContext(), NotificationManagerService.TAG, printWriter)) {
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() == null || !NotificationManagerService.this.mNMSWrapper.getNMSExt().dump(fileDescriptor, printWriter, strArr)) {
                    DumpFilter parseFromArguments = DumpFilter.parseFromArguments(strArr);
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> allUsersNotificationPermissions = NotificationManagerService.this.getAllUsersNotificationPermissions();
                        if (parseFromArguments.stats) {
                            NotificationManagerService.this.dumpJson(printWriter, parseFromArguments, allUsersNotificationPermissions);
                        } else if (parseFromArguments.rvStats) {
                            NotificationManagerService.this.dumpRemoteViewStats(printWriter, parseFromArguments);
                        } else if (parseFromArguments.proto) {
                            NotificationManagerService.this.dumpProto(fileDescriptor, parseFromArguments, allUsersNotificationPermissions);
                        } else if (parseFromArguments.criticalPriority) {
                            NotificationManagerService.this.dumpNotificationRecords(printWriter, parseFromArguments);
                        } else {
                            NotificationManagerService.this.dumpImpl(printWriter, parseFromArguments, allUsersNotificationPermissions);
                        }
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        }

        public ComponentName getEffectsSuppressor() {
            if (NotificationManagerService.this.mEffectsSuppressors.isEmpty()) {
                return null;
            }
            return (ComponentName) NotificationManagerService.this.mEffectsSuppressors.get(0);
        }

        /* JADX WARN: Code restructure failed: missing block: B:15:0x003a, code lost:
        
            if (r5 == 0) goto L25;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x0055, code lost:
        
            r9.this$0.getContext().enforceCallingPermission("android.permission.READ_CONTACTS", "matchesCallFilter requires listener permission, contacts read access, or system level access");
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0053, code lost:
        
            if (r2 == 0) goto L25;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean matchesCallFilter(Bundle bundle) {
            boolean z;
            int i = 0;
            try {
                enforceSystemOrSystemUI("INotificationManager.matchesCallFilter");
                z = true;
            } catch (SecurityException unused) {
                z = false;
            }
            try {
                String[] packagesForUid = NotificationManagerService.this.mPackageManager.getPackagesForUid(Binder.getCallingUid());
                int i2 = 0;
                while (i < packagesForUid.length) {
                    try {
                        i2 |= NotificationManagerService.this.mListeners.hasAllowedListener(packagesForUid[i], Binder.getCallingUserHandle().getIdentifier()) ? 1 : 0;
                        i++;
                    } catch (RemoteException unused2) {
                        i = i2;
                        if (!z) {
                        }
                        return NotificationManagerService.this.mZenModeHelper.matchesCallFilter(Binder.getCallingUserHandle(), bundle, (ValidateNotificationPeople) NotificationManagerService.this.mRankingHelper.findExtractor(ValidateNotificationPeople.class), NotificationManagerService.MATCHES_CALL_FILTER_CONTACTS_TIMEOUT_MS, 1.0f, Binder.getCallingUid());
                    } catch (Throwable th) {
                        th = th;
                        i = i2;
                        if (!z && i == 0) {
                            NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.READ_CONTACTS", "matchesCallFilter requires listener permission, contacts read access, or system level access");
                        }
                        throw th;
                    }
                }
                if (!z) {
                }
            } catch (RemoteException unused3) {
            } catch (Throwable th2) {
                th = th2;
            }
            return NotificationManagerService.this.mZenModeHelper.matchesCallFilter(Binder.getCallingUserHandle(), bundle, (ValidateNotificationPeople) NotificationManagerService.this.mRankingHelper.findExtractor(ValidateNotificationPeople.class), NotificationManagerService.MATCHES_CALL_FILTER_CONTACTS_TIMEOUT_MS, 1.0f, Binder.getCallingUid());
        }

        public void cleanUpCallersAfter(long j) {
            enforceSystemOrSystemUI("INotificationManager.cleanUpCallersAfter");
            NotificationManagerService.this.mZenModeHelper.cleanUpCallersAfter(j);
        }

        public boolean isSystemConditionProviderEnabled(String str) {
            enforceSystemOrSystemUI("INotificationManager.isSystemConditionProviderEnabled");
            return NotificationManagerService.this.mConditionProviders.isSystemProviderEnabled(str);
        }

        public byte[] getBackupPayload(int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            if (NotificationManagerService.DBG) {
                Slog.d(NotificationManagerService.TAG, "getBackupPayload u=" + i);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                NotificationManagerService.this.writePolicyXml(byteArrayOutputStream, true, i);
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                Slog.w(NotificationManagerService.TAG, "getBackupPayload: error writing payload for user " + i, e);
                return null;
            }
        }

        public void applyRestore(byte[] bArr, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            if (NotificationManagerService.DBG) {
                StringBuilder sb = new StringBuilder();
                sb.append("applyRestore u=");
                sb.append(i);
                sb.append(" payload=");
                sb.append(bArr != null ? new String(bArr, StandardCharsets.UTF_8) : null);
                Slog.d(NotificationManagerService.TAG, sb.toString());
            }
            if (bArr == null) {
                Slog.w(NotificationManagerService.TAG, "applyRestore: no payload to restore for user " + i);
                return;
            }
            try {
                NotificationManagerService.this.readPolicyXml(new ByteArrayInputStream(bArr), true, i);
                NotificationManagerService.this.handleSavePolicyFile();
            } catch (IOException | NumberFormatException | XmlPullParserException e) {
                Slog.w(NotificationManagerService.TAG, "applyRestore: error reading payload", e);
            }
        }

        public boolean isNotificationPolicyAccessGranted(String str) {
            return checkPolicyAccess(str);
        }

        public boolean isNotificationPolicyAccessGrantedForPackage(String str) {
            enforceSystemOrSystemUIOrSamePackage(str, "request policy access status for another package");
            return checkPolicyAccess(str);
        }

        public void setNotificationPolicyAccessGranted(String str, boolean z) throws RemoteException {
            setNotificationPolicyAccessGrantedForUser(str, INotificationManager.Stub.getCallingUserHandle().getIdentifier(), z);
        }

        public void setNotificationPolicyAccessGrantedForUser(String str, int i, boolean z) {
            NotificationManagerService.this.checkCallerIsSystemOrShell();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (NotificationManagerService.this.mAllowedManagedServicePackages.test(str, Integer.valueOf(i), NotificationManagerService.this.mConditionProviders.getRequiredPermission())) {
                    NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(str, i, true, z);
                    NotificationManagerService.this.getContext().sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(str).addFlags(67108864), UserHandle.of(i), null);
                    NotificationManagerService.this.handleSavePolicyFile();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public NotificationManager.Policy getNotificationPolicy(String str) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return NotificationManagerService.this.mZenModeHelper.getNotificationPolicy();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public NotificationManager.Policy getConsolidatedNotificationPolicy() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return NotificationManagerService.this.mZenModeHelper.getConsolidatedNotificationPolicy();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setNotificationPolicy(String str, NotificationManager.Policy policy) {
            NotificationManager.Policy policy2 = policy;
            enforcePolicyAccess(str, "setNotificationPolicy");
            int callingUid = Binder.getCallingUid();
            boolean isCallerIsSystemOrSystemUi = NotificationManagerService.this.isCallerIsSystemOrSystemUi();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                ApplicationInfo applicationInfo = NotificationManagerService.this.mPackageManager.getApplicationInfo(str, 0L, UserHandle.getUserId(callingUid));
                NotificationManager.Policy notificationPolicy = NotificationManagerService.this.mZenModeHelper.getNotificationPolicy();
                if (applicationInfo.targetSdkVersion < 28) {
                    int i = policy2.priorityCategories & (-33) & (-65) & (-129);
                    int i2 = notificationPolicy.priorityCategories;
                    policy2 = new NotificationManager.Policy(i | (i2 & 32) | (i2 & 64) | (i2 & 128), policy2.priorityCallSenders, policy2.priorityMessageSenders, policy2.suppressedVisualEffects);
                }
                if (applicationInfo.targetSdkVersion < 30) {
                    policy2 = new NotificationManager.Policy(NotificationManagerService.this.correctCategory(policy2.priorityCategories, 256, notificationPolicy.priorityCategories), policy2.priorityCallSenders, policy2.priorityMessageSenders, policy2.suppressedVisualEffects, notificationPolicy.priorityConversationSenders);
                }
                NotificationManager.Policy policy3 = new NotificationManager.Policy(policy2.priorityCategories, policy2.priorityCallSenders, policy2.priorityMessageSenders, NotificationManagerService.this.calculateSuppressedVisualEffects(policy2, notificationPolicy, applicationInfo.targetSdkVersion), policy2.priorityConversationSenders);
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                    policy3 = NotificationManagerService.this.mNMSWrapper.getNMSExt().adjustNotificationPolicy(str, policy3);
                }
                ZenLog.traceSetNotificationPolicy(str, applicationInfo.targetSdkVersion, policy3);
                NotificationManagerService.this.mZenModeHelper.setNotificationPolicy(policy3, callingUid, isCallerIsSystemOrSystemUi);
            } catch (RemoteException unused) {
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }

        public List<String> getEnabledNotificationListenerPackages() {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mListeners.getAllowedPackages(INotificationManager.Stub.getCallingUserHandle().getIdentifier());
        }

        public List<ComponentName> getEnabledNotificationListeners(int i) {
            NotificationManagerService.this.checkNotificationListenerAccess();
            return NotificationManagerService.this.mListeners.getAllowedComponents(i);
        }

        public ComponentName getAllowedNotificationAssistantForUser(int i) {
            NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell();
            List<ComponentName> allowedComponents = NotificationManagerService.this.mAssistants.getAllowedComponents(i);
            if (allowedComponents.size() > 1) {
                throw new IllegalStateException("At most one NotificationAssistant: " + allowedComponents.size());
            }
            return (ComponentName) CollectionUtils.firstOrNull(allowedComponents);
        }

        public ComponentName getAllowedNotificationAssistant() {
            return getAllowedNotificationAssistantForUser(INotificationManager.Stub.getCallingUserHandle().getIdentifier());
        }

        public ComponentName getDefaultNotificationAssistant() {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mAssistants.getDefaultFromConfig();
        }

        public void setNASMigrationDoneAndResetDefault(int i, boolean z) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.setNASMigrationDone(i);
            if (z) {
                NotificationManagerService.this.mAssistants.resetDefaultFromConfig();
            } else {
                NotificationManagerService.this.mAssistants.clearDefaults();
            }
        }

        public boolean hasEnabledNotificationListener(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mListeners.isPackageAllowed(str, i);
        }

        public boolean isNotificationListenerAccessGranted(ComponentName componentName) {
            Objects.requireNonNull(componentName);
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(componentName.getPackageName());
            return NotificationManagerService.this.mListeners.isPackageOrComponentAllowed(componentName.flattenToString(), INotificationManager.Stub.getCallingUserHandle().getIdentifier());
        }

        public boolean isNotificationListenerAccessGrantedForUser(ComponentName componentName, int i) {
            Objects.requireNonNull(componentName);
            NotificationManagerService.this.checkCallerIsSystem();
            return NotificationManagerService.this.mListeners.isPackageOrComponentAllowed(componentName.flattenToString(), i);
        }

        public boolean isNotificationAssistantAccessGranted(ComponentName componentName) {
            Objects.requireNonNull(componentName);
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(componentName.getPackageName());
            return NotificationManagerService.this.mAssistants.isPackageOrComponentAllowed(componentName.flattenToString(), INotificationManager.Stub.getCallingUserHandle().getIdentifier());
        }

        public void setNotificationListenerAccessGranted(ComponentName componentName, boolean z, boolean z2) throws RemoteException {
            setNotificationListenerAccessGrantedForUser(componentName, INotificationManager.Stub.getCallingUserHandle().getIdentifier(), z, z2);
        }

        public void setNotificationAssistantAccessGranted(ComponentName componentName, boolean z) {
            setNotificationAssistantAccessGrantedForUser(componentName, INotificationManager.Stub.getCallingUserHandle().getIdentifier(), z);
        }

        public void setNotificationListenerAccessGrantedForUser(ComponentName componentName, int i, boolean z, boolean z2) {
            Objects.requireNonNull(componentName);
            NotificationManagerService.this.checkNotificationListenerAccess();
            if (z && componentName.flattenToString().length() > NotificationManager.MAX_SERVICE_COMPONENT_NAME_LENGTH) {
                throw new IllegalArgumentException("Component name too long: " + componentName.flattenToString());
            }
            if (z2 || !isNotificationListenerAccessUserSet(componentName)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (NotificationManagerService.this.mAllowedManagedServicePackages.test(componentName.getPackageName(), Integer.valueOf(i), NotificationManagerService.this.mListeners.getRequiredPermission())) {
                        NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(componentName.flattenToString(), i, false, z, z2);
                        NotificationManagerService.this.mListeners.setPackageOrComponentEnabled(componentName.flattenToString(), i, true, z, z2);
                        NotificationManagerService.this.getContext().sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(componentName.getPackageName()).addFlags(1073741824), UserHandle.of(i), null);
                        NotificationManagerService.this.handleSavePolicyFile();
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        private boolean isNotificationListenerAccessUserSet(ComponentName componentName) {
            return NotificationManagerService.this.mListeners.isPackageOrComponentUserSet(componentName.flattenToString(), INotificationManager.Stub.getCallingUserHandle().getIdentifier());
        }

        public void setNotificationAssistantAccessGrantedForUser(ComponentName componentName, int i, boolean z) {
            NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell();
            Iterator it = NotificationManagerService.this.mUm.getEnabledProfiles(i).iterator();
            while (it.hasNext()) {
                NotificationManagerService.this.mAssistants.setUserSet(((UserInfo) it.next()).id, true);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                NotificationManagerService.this.setNotificationAssistantAccessGrantedForUserInternal(componentName, i, z, true);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void applyEnqueuedAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.mAssistants.checkServiceTokenLocked(iNotificationListener);
                    int size = NotificationManagerService.this.mEnqueuedNotifications.size();
                    boolean z = false;
                    for (int i = 0; i < size; i++) {
                        NotificationRecord notificationRecord = NotificationManagerService.this.mEnqueuedNotifications.get(i);
                        if (Objects.equals(adjustment.getKey(), notificationRecord.getKey()) && Objects.equals(Integer.valueOf(adjustment.getUser()), Integer.valueOf(notificationRecord.getUserId())) && NotificationManagerService.this.mAssistants.isSameUser(iNotificationListener, notificationRecord.getUserId())) {
                            NotificationManagerService.this.applyAdjustment(notificationRecord, adjustment);
                            notificationRecord.applyAdjustments();
                            notificationRecord.calculateImportance();
                            z = true;
                        }
                    }
                    if (!z) {
                        applyAdjustmentFromAssistant(iNotificationListener, adjustment);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void applyAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(adjustment);
            applyAdjustmentsFromAssistant(iNotificationListener, arrayList);
        }

        public void applyAdjustmentsFromAssistant(INotificationListener iNotificationListener, List<Adjustment> list) {
            boolean z;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    NotificationManagerService.this.mAssistants.checkServiceTokenLocked(iNotificationListener);
                    z = false;
                    for (Adjustment adjustment : list) {
                        NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(adjustment.getKey());
                        if (notificationRecord != null && NotificationManagerService.this.mAssistants.isSameUser(iNotificationListener, notificationRecord.getUserId())) {
                            NotificationManagerService.this.applyAdjustment(notificationRecord, adjustment);
                            if (adjustment.getSignals().containsKey("key_importance") && adjustment.getSignals().getInt("key_importance") == 0) {
                                cancelNotificationsFromListener(iNotificationListener, new String[]{notificationRecord.getKey()});
                            } else {
                                notificationRecord.setPendingLogUpdate(true);
                                z = true;
                            }
                        }
                    }
                }
                if (z) {
                    NotificationManagerService.this.mRankingHandler.requestSort();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup) throws RemoteException {
            Objects.requireNonNull(userHandle);
            verifyPrivilegedListener(iNotificationListener, userHandle, false);
            NotificationManagerService.this.createNotificationChannelGroup(str, getUidForPackageAndUser(str, userHandle), notificationChannelGroup, false, true);
            NotificationManagerService.this.handleSavePolicyFile();
        }

        public void updateNotificationChannelFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle, NotificationChannel notificationChannel) throws RemoteException {
            Objects.requireNonNull(notificationChannel);
            Objects.requireNonNull(str);
            Objects.requireNonNull(userHandle);
            verifyPrivilegedListener(iNotificationListener, userHandle, false);
            verifyPrivilegedListenerUriPermission(Binder.getCallingUid(), notificationChannel, NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(str, getUidForPackageAndUser(str, userHandle), notificationChannel.getId(), true));
            NotificationManagerService.this.updateNotificationChannelInt(str, getUidForPackageAndUser(str, userHandle), notificationChannel, true);
        }

        public ParceledListSlice<NotificationChannel> getNotificationChannelsFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle) throws RemoteException {
            Objects.requireNonNull(str);
            Objects.requireNonNull(userHandle);
            verifyPrivilegedListener(iNotificationListener, userHandle, true);
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(str, getUidForPackageAndUser(str, userHandle), false);
        }

        public ParceledListSlice<NotificationChannelGroup> getNotificationChannelGroupsFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle) throws RemoteException {
            Objects.requireNonNull(str);
            Objects.requireNonNull(userHandle);
            verifyPrivilegedListener(iNotificationListener, userHandle, true);
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(str, getUidForPackageAndUser(str, userHandle)));
            return new ParceledListSlice<>(arrayList);
        }

        public boolean isInCall(String str, int i) {
            NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell();
            return NotificationManagerService.this.isCallNotification(str, i);
        }

        public void setPrivateNotificationsAllowed(boolean z) {
            if (NotificationManagerService.this.getContext().checkCallingPermission("android.permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS") != 0) {
                throw new SecurityException("Requires CONTROL_KEYGUARD_SECURE_NOTIFICATIONS permission");
            }
            if (z != NotificationManagerService.this.mLockScreenAllowSecureNotifications) {
                NotificationManagerService.this.mLockScreenAllowSecureNotifications = z;
                NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public boolean getPrivateNotificationsAllowed() {
            if (NotificationManagerService.this.getContext().checkCallingPermission("android.permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS") != 0) {
                throw new SecurityException("Requires CONTROL_KEYGUARD_SECURE_NOTIFICATIONS permission");
            }
            return NotificationManagerService.this.mLockScreenAllowSecureNotifications;
        }

        public boolean isPackagePaused(String str) {
            Objects.requireNonNull(str);
            NotificationManagerService.this.checkCallerIsSameApp(str);
            return NotificationManagerService.this.isPackagePausedOrSuspended(str, Binder.getCallingUid());
        }

        public boolean isPermissionFixed(String str, int i) {
            enforceSystemOrSystemUI("isPermissionFixed");
            return NotificationManagerService.this.mPermissionHelper.isPermissionFixed(str, i);
        }

        private void verifyPrivilegedListener(INotificationListener iNotificationListener, UserHandle userHandle, boolean z) {
            ManagedServices.ManagedServiceInfo checkServiceTokenLocked;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                checkServiceTokenLocked = NotificationManagerService.this.mListeners.checkServiceTokenLocked(iNotificationListener);
            }
            if (!NotificationManagerService.this.hasCompanionDevice(checkServiceTokenLocked)) {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    if (z) {
                        if (NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(checkServiceTokenLocked.service)) {
                        }
                    }
                    throw new SecurityException(checkServiceTokenLocked + " does not have access");
                }
            }
            if (checkServiceTokenLocked.enabledAndUserMatches(userHandle.getIdentifier())) {
                return;
            }
            throw new SecurityException(checkServiceTokenLocked + " does not have access");
        }

        private void verifyPrivilegedListenerUriPermission(final int i, NotificationChannel notificationChannel, NotificationChannel notificationChannel2) {
            final Uri sound = notificationChannel.getSound();
            Uri sound2 = notificationChannel2 != null ? notificationChannel2.getSound() : null;
            if (sound == null || Objects.equals(sound2, sound)) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$11$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    NotificationManagerService.AnonymousClass11.this.lambda$verifyPrivilegedListenerUriPermission$1(i, sound);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$verifyPrivilegedListenerUriPermission$1(int i, Uri uri) throws Exception {
            NotificationManagerService.this.mUgmInternal.checkGrantUriPermission(i, null, ContentProvider.getUriWithoutUserId(uri), 1, ContentProvider.getUserIdFromUri(uri, UserHandle.getUserId(i)));
        }

        private int getUidForPackageAndUser(String str, UserHandle userHandle) throws RemoteException {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return NotificationManagerService.this.mPackageManager.getPackageUid(str, 0L, userHandle.getIdentifier());
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) throws RemoteException {
            new NotificationShellCmd(NotificationManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        public long pullStats(long j, int i, boolean z, List<ParcelFileDescriptor> list) {
            NotificationManagerService.this.checkCallerIsSystemOrShell();
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            TimeUnit timeUnit2 = TimeUnit.NANOSECONDS;
            long convert = timeUnit.convert(j, timeUnit2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (i == 1) {
                    Slog.e(NotificationManagerService.TAG, "pullStats REPORT_REMOTE_VIEWS from: " + convert + "  with " + z);
                    PulledStats remoteViewStats = NotificationManagerService.this.mUsageStats.remoteViewStats(convert, z);
                    if (remoteViewStats != null) {
                        list.add(remoteViewStats.toParcelFileDescriptor(i));
                        Slog.e(NotificationManagerService.TAG, "exiting pullStats with: " + list.size());
                        return timeUnit2.convert(remoteViewStats.endTimeMs(), timeUnit);
                    }
                    Slog.e(NotificationManagerService.TAG, "null stats for: " + i);
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                Slog.e(NotificationManagerService.TAG, "exiting pullStats: bad request");
                return 0L;
            } catch (IOException e) {
                Slog.e(NotificationManagerService.TAG, "exiting pullStats: on error", e);
                return 0L;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    protected void checkNotificationListenerAccess() {
        if (isCallerSystemOrPhone()) {
            return;
        }
        getContext().enforceCallingPermission("android.permission.MANAGE_NOTIFICATION_LISTENERS", "Caller must hold android.permission.MANAGE_NOTIFICATION_LISTENERS");
    }

    @VisibleForTesting
    protected void setNotificationAssistantAccessGrantedForUserInternal(ComponentName componentName, int i, boolean z, boolean z2) {
        List enabledProfiles = this.mUm.getEnabledProfiles(i);
        if (enabledProfiles != null) {
            Iterator it = enabledProfiles.iterator();
            while (it.hasNext()) {
                int i2 = ((UserInfo) it.next()).id;
                if (componentName == null) {
                    ComponentName componentName2 = (ComponentName) CollectionUtils.firstOrNull(this.mAssistants.getAllowedComponents(i2));
                    if (componentName2 != null) {
                        setNotificationAssistantAccessGrantedForUserInternal(componentName2, i2, false, z2);
                    }
                } else if (!z || this.mAllowedManagedServicePackages.test(componentName.getPackageName(), Integer.valueOf(i2), this.mAssistants.getRequiredPermission())) {
                    this.mConditionProviders.setPackageOrComponentEnabled(componentName.flattenToString(), i2, false, z);
                    this.mAssistants.setPackageOrComponentEnabled(componentName.flattenToString(), i2, true, z, z2);
                    getContext().sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(componentName.getPackageName()).addFlags(1073741824), UserHandle.of(i2), null);
                    handleSavePolicyFile();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyAdjustment(NotificationRecord notificationRecord, Adjustment adjustment) {
        if (notificationRecord == null || adjustment.getSignals() == null) {
            return;
        }
        Bundle signals = adjustment.getSignals();
        Bundle.setDefusable(signals, true);
        ArrayList arrayList = new ArrayList();
        for (String str : signals.keySet()) {
            if (!this.mAssistants.isAdjustmentAllowed(str)) {
                if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isStowOptionKey(adjustment, str)) {
                    break;
                } else {
                    arrayList.add(str);
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            signals.remove((String) it.next());
        }
        notificationRecord.addAdjustment(adjustment);
    }

    @GuardedBy({"mNotificationLock"})
    void addAutogroupKeyLocked(String str) {
        NotificationRecord notificationRecord = this.mNotificationsByKey.get(str);
        if (notificationRecord != null && notificationRecord.getSbn().getOverrideGroupKey() == null) {
            addAutoGroupAdjustment(notificationRecord, "ranker_group");
            EventLogTags.writeNotificationAutogrouped(str);
            this.mRankingHandler.requestSort();
        }
    }

    @GuardedBy({"mNotificationLock"})
    void removeAutogroupKeyLocked(String str) {
        NotificationRecord notificationRecord = this.mNotificationsByKey.get(str);
        if (notificationRecord == null) {
            Slog.w(TAG, "Failed to remove autogroup " + str);
            return;
        }
        if (notificationRecord.getSbn().getOverrideGroupKey() != null) {
            addAutoGroupAdjustment(notificationRecord, null);
            EventLogTags.writeNotificationUnautogrouped(str);
            this.mRankingHandler.requestSort();
        }
    }

    private void addAutoGroupAdjustment(NotificationRecord notificationRecord, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("key_group_key", str);
        notificationRecord.addAdjustment(new Adjustment(notificationRecord.getSbn().getPackageName(), notificationRecord.getKey(), bundle, "", notificationRecord.getSbn().getUserId()));
    }

    @GuardedBy({"mNotificationLock"})
    @VisibleForTesting
    void clearAutogroupSummaryLocked(int i, String str) {
        NotificationRecord findNotificationByKeyLocked;
        ArrayMap<String, String> arrayMap = this.mAutobundledSummaries.get(Integer.valueOf(i));
        if (arrayMap == null || !arrayMap.containsKey(str) || (findNotificationByKeyLocked = findNotificationByKeyLocked(arrayMap.remove(str))) == null) {
            return;
        }
        StatusBarNotification sbn = findNotificationByKeyLocked.getSbn();
        cancelNotification(MY_UID, MY_PID, str, sbn.getTag(), sbn.getId(), 0, 0, false, i, 16, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public boolean hasAutoGroupSummaryLocked(StatusBarNotification statusBarNotification) {
        ArrayMap<String, String> arrayMap = this.mAutobundledSummaries.get(Integer.valueOf(statusBarNotification.getUserId()));
        return arrayMap != null && arrayMap.containsKey(statusBarNotification.getPackageName());
    }

    NotificationRecord createAutoGroupSummary(int i, String str, String str2, int i2) {
        NotificationRecord notificationRecord;
        Notification notification;
        ArrayMap<String, String> arrayMap;
        boolean isPermissionFixed = this.mPermissionHelper.isPermissionFixed(str, i);
        synchronized (this.mNotificationLock) {
            NotificationRecord notificationRecord2 = this.mNotificationsByKey.get(str2);
            if (notificationRecord2 == null) {
                return null;
            }
            StatusBarNotification sbn = notificationRecord2.getSbn();
            int identifier = sbn.getUser().getIdentifier();
            int uid = sbn.getUid();
            ArrayMap<String, String> arrayMap2 = this.mAutobundledSummaries.get(Integer.valueOf(identifier));
            if (arrayMap2 == null) {
                arrayMap2 = new ArrayMap<>();
            }
            ArrayMap<String, String> arrayMap3 = arrayMap2;
            this.mAutobundledSummaries.put(Integer.valueOf(identifier), arrayMap3);
            if (arrayMap3.containsKey(str)) {
                notificationRecord = null;
            } else {
                ApplicationInfo applicationInfo = (ApplicationInfo) sbn.getNotification().extras.getParcelable("android.appInfo", ApplicationInfo.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("android.appInfo", applicationInfo);
                Notification build = new Notification.Builder(getContext(), notificationRecord2.getChannel().getId()).setSmallIcon(sbn.getNotification().getSmallIcon()).setGroupSummary(true).setGroupAlertBehavior(2).setGroup("ranker_group").setFlag(i2, true).setColor(sbn.getNotification().color).build();
                build.extras.putAll(bundle);
                Intent launchIntentForPackage = getContext().getPackageManager().getLaunchIntentForPackage(str);
                if (launchIntentForPackage == null && this.mNMSWrapper.getNMSExt() != null) {
                    launchIntentForPackage = this.mNMSWrapper.getNMSExt().createAutoGroupSummaryAppIntent(str);
                }
                Intent intent = launchIntentForPackage;
                if (intent != null) {
                    notification = build;
                    arrayMap = arrayMap3;
                    notification.contentIntent = this.mAmi.getPendingIntentActivityAsApp(0, intent, 67108864, (Bundle) null, str, applicationInfo.uid);
                } else {
                    notification = build;
                    arrayMap = arrayMap3;
                }
                StatusBarNotification statusBarNotification = new StatusBarNotification(sbn.getPackageName(), sbn.getOpPkg(), Integer.MAX_VALUE, "ranker_group", sbn.getUid(), sbn.getInitialPid(), notification, sbn.getUser(), "ranker_group", System.currentTimeMillis());
                NotificationRecord notificationRecord3 = new NotificationRecord(getContext(), statusBarNotification, notificationRecord2.getChannel());
                notificationRecord3.setImportanceFixed(isPermissionFixed);
                notificationRecord3.setIsAppImportanceLocked(notificationRecord2.getIsAppImportanceLocked());
                arrayMap.put(str, statusBarNotification.getKey());
                notificationRecord = notificationRecord3;
            }
            if (notificationRecord == null || !checkDisqualifyingFeatures(identifier, uid, notificationRecord.getSbn().getId(), notificationRecord.getSbn().getTag(), notificationRecord, true, false)) {
                return null;
            }
            return notificationRecord;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String disableNotificationEffects(NotificationRecord notificationRecord) {
        if (this.mDisableNotificationEffects) {
            return "booleanState";
        }
        if ((this.mListenerHints & 1) != 0) {
            return "listenerHints";
        }
        if (notificationRecord != null && notificationRecord.getAudioAttributes() != null) {
            if ((this.mListenerHints & 2) != 0 && notificationRecord.getAudioAttributes().getUsage() != 6) {
                return "listenerNoti";
            }
            if ((this.mListenerHints & 4) != 0 && notificationRecord.getAudioAttributes().getUsage() == 6) {
                return "listenerCall";
            }
        }
        if (this.mCallState == 0 || this.mZenModeHelper.isCall(notificationRecord)) {
            return null;
        }
        return "callState";
    }

    @VisibleForTesting
    protected ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> getAllUsersNotificationPermissions() {
        ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> arrayMap = new ArrayMap<>();
        Iterator it = this.mUm.getUsers().iterator();
        while (it.hasNext()) {
            ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> notificationPermissionValues = this.mPermissionHelper.getNotificationPermissionValues(((UserInfo) it.next()).getUserHandle().getIdentifier());
            for (Pair<Integer, String> pair : notificationPermissionValues.keySet()) {
                arrayMap.put(pair, notificationPermissionValues.get(pair));
            }
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpJson(PrintWriter printWriter, DumpFilter dumpFilter, ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> arrayMap) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("service", "Notification Manager");
            jSONObject.put("bans", this.mPreferencesHelper.dumpBansJson(dumpFilter, arrayMap));
            jSONObject.put("ranking", this.mPreferencesHelper.dumpJson(dumpFilter, arrayMap));
            jSONObject.put("stats", this.mUsageStats.dumpJson(dumpFilter));
            jSONObject.put("channels", this.mPreferencesHelper.dumpChannelsJson(dumpFilter));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        printWriter.println(jSONObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpRemoteViewStats(PrintWriter printWriter, DumpFilter dumpFilter) {
        PulledStats remoteViewStats = this.mUsageStats.remoteViewStats(dumpFilter.since, true);
        if (remoteViewStats == null) {
            printWriter.println("no remote view stats reported.");
        } else {
            remoteViewStats.dump(1, printWriter, dumpFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpProto(FileDescriptor fileDescriptor, DumpFilter dumpFilter, ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> arrayMap) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            for (int i = 0; i < size; i++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i);
                if (!dumpFilter.filtered || dumpFilter.matches(notificationRecord.getSbn())) {
                    notificationRecord.dump(protoOutputStream, 2246267895809L, dumpFilter.redact, 1);
                }
            }
            int size2 = this.mEnqueuedNotifications.size();
            for (int i2 = 0; i2 < size2; i2++) {
                NotificationRecord notificationRecord2 = this.mEnqueuedNotifications.get(i2);
                if (!dumpFilter.filtered || dumpFilter.matches(notificationRecord2.getSbn())) {
                    notificationRecord2.dump(protoOutputStream, 2246267895809L, dumpFilter.redact, 0);
                }
            }
            List<NotificationRecord> snoozed = this.mSnoozeHelper.getSnoozed();
            int size3 = snoozed.size();
            for (int i3 = 0; i3 < size3; i3++) {
                NotificationRecord notificationRecord3 = snoozed.get(i3);
                if (!dumpFilter.filtered || dumpFilter.matches(notificationRecord3.getSbn())) {
                    notificationRecord3.dump(protoOutputStream, 2246267895809L, dumpFilter.redact, 2);
                }
            }
            long start = protoOutputStream.start(1146756268034L);
            this.mZenModeHelper.dump(protoOutputStream);
            Iterator<ComponentName> it = this.mEffectsSuppressors.iterator();
            while (it.hasNext()) {
                it.next().dumpDebug(protoOutputStream, 2246267895812L);
            }
            protoOutputStream.end(start);
            long start2 = protoOutputStream.start(1146756268035L);
            this.mListeners.dump(protoOutputStream, dumpFilter);
            protoOutputStream.end(start2);
            protoOutputStream.write(1120986464260L, this.mListenerHints);
            for (int i4 = 0; i4 < this.mListenersDisablingEffects.size(); i4++) {
                long start3 = protoOutputStream.start(2246267895813L);
                protoOutputStream.write(1120986464257L, this.mListenersDisablingEffects.keyAt(i4));
                ArraySet<ComponentName> valueAt = this.mListenersDisablingEffects.valueAt(i4);
                for (int i5 = 0; i5 < valueAt.size(); i5++) {
                    valueAt.valueAt(i5).dumpDebug(protoOutputStream, 2246267895811L);
                }
                protoOutputStream.end(start3);
            }
            long start4 = protoOutputStream.start(1146756268038L);
            this.mAssistants.dump(protoOutputStream, dumpFilter);
            protoOutputStream.end(start4);
            long start5 = protoOutputStream.start(1146756268039L);
            this.mConditionProviders.dump(protoOutputStream, dumpFilter);
            protoOutputStream.end(start5);
            long start6 = protoOutputStream.start(1146756268040L);
            this.mRankingHelper.dump(protoOutputStream, dumpFilter);
            this.mPreferencesHelper.dump(protoOutputStream, dumpFilter, arrayMap);
            protoOutputStream.end(start6);
        }
        protoOutputStream.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpNotificationRecords(PrintWriter printWriter, DumpFilter dumpFilter) {
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            if (size > 0) {
                printWriter.println("  Notification List:");
                for (int i = 0; i < size; i++) {
                    NotificationRecord notificationRecord = this.mNotificationList.get(i);
                    if (!dumpFilter.filtered || dumpFilter.matches(notificationRecord.getSbn())) {
                        notificationRecord.dump(printWriter, "    ", getContext(), dumpFilter.redact);
                    }
                }
                printWriter.println("  ");
            }
        }
    }

    void dumpImpl(PrintWriter printWriter, DumpFilter dumpFilter, ArrayMap<Pair<Integer, String>, Pair<Boolean, Boolean>> arrayMap) {
        printWriter.print("Current Notification Manager state");
        if (dumpFilter.filtered) {
            printWriter.print(" (filtered to ");
            printWriter.print(dumpFilter);
            printWriter.print(")");
        }
        printWriter.println(':');
        boolean z = dumpFilter.filtered && dumpFilter.zen;
        if (!z) {
            synchronized (this.mToastQueue) {
                int size = this.mToastQueue.size();
                if (size > 0) {
                    printWriter.println("  Toast Queue:");
                    for (int i = 0; i < size; i++) {
                        this.mToastQueue.get(i).dump(printWriter, "    ", dumpFilter);
                    }
                    printWriter.println("  ");
                }
            }
        }
        synchronized (this.mNotificationLock) {
            if (!z) {
                try {
                    if (!dumpFilter.normalPriority) {
                        dumpNotificationRecords(printWriter, dumpFilter);
                    }
                    if (!dumpFilter.filtered) {
                        int size2 = this.mLights.size();
                        if (size2 > 0) {
                            printWriter.println("  Lights List:");
                            for (int i2 = 0; i2 < size2; i2++) {
                                if (i2 == size2 - 1) {
                                    printWriter.print("  > ");
                                } else {
                                    printWriter.print("    ");
                                }
                                printWriter.println(this.mLights.get(i2));
                            }
                            printWriter.println("  ");
                        }
                        printWriter.println("  mUseAttentionLight=" + this.mUseAttentionLight);
                        printWriter.println("  mHasLight=" + this.mHasLight);
                        printWriter.println("  mNotificationPulseEnabled=" + this.mNotificationPulseEnabled);
                        printWriter.println("  mSoundNotificationKey=" + this.mSoundNotificationKey);
                        printWriter.println("  mVibrateNotificationKey=" + this.mVibrateNotificationKey);
                        printWriter.println("  mDisableNotificationEffects=" + this.mDisableNotificationEffects);
                        printWriter.println("  mCallState=" + callStateToString(this.mCallState));
                        printWriter.println("  mSystemReady=" + this.mSystemReady);
                        printWriter.println("  mMaxPackageEnqueueRate=" + this.mMaxPackageEnqueueRate);
                        printWriter.println("  hideSilentStatusBar=" + this.mPreferencesHelper.shouldHideSilentStatusIcons());
                    }
                    printWriter.println("  mArchive=" + this.mArchive.toString());
                    this.mArchive.dumpImpl(printWriter, dumpFilter);
                    if (!z) {
                        int size3 = this.mEnqueuedNotifications.size();
                        if (size3 > 0) {
                            printWriter.println("  Enqueued Notification List:");
                            for (int i3 = 0; i3 < size3; i3++) {
                                NotificationRecord notificationRecord = this.mEnqueuedNotifications.get(i3);
                                if (!dumpFilter.filtered || dumpFilter.matches(notificationRecord.getSbn())) {
                                    notificationRecord.dump(printWriter, "    ", getContext(), dumpFilter.redact);
                                }
                            }
                            printWriter.println("  ");
                        }
                        this.mSnoozeHelper.dump(printWriter, dumpFilter);
                    }
                } finally {
                }
            }
            if (!z) {
                printWriter.println("\n  Ranking Config:");
                this.mRankingHelper.dump(printWriter, "    ", dumpFilter);
                printWriter.println("\n Notification Preferences:");
                this.mPreferencesHelper.dump(printWriter, "    ", dumpFilter, arrayMap);
                printWriter.println("\n  Notification listeners:");
                this.mListeners.dump(printWriter, dumpFilter);
                printWriter.print("    mListenerHints: ");
                printWriter.println(this.mListenerHints);
                printWriter.print("    mListenersDisablingEffects: (");
                int size4 = this.mListenersDisablingEffects.size();
                for (int i4 = 0; i4 < size4; i4++) {
                    int keyAt = this.mListenersDisablingEffects.keyAt(i4);
                    if (i4 > 0) {
                        printWriter.print(';');
                    }
                    printWriter.print("hint[" + keyAt + "]:");
                    ArraySet<ComponentName> valueAt = this.mListenersDisablingEffects.valueAt(i4);
                    int size5 = valueAt.size();
                    for (int i5 = 0; i5 < size5; i5++) {
                        if (i5 > 0) {
                            printWriter.print(',');
                        }
                        ComponentName valueAt2 = valueAt.valueAt(i5);
                        if (valueAt2 != null) {
                            printWriter.print(valueAt2);
                        }
                    }
                }
                printWriter.println(')');
                printWriter.println("\n  Notification assistant services:");
                this.mAssistants.dump(printWriter, dumpFilter);
            }
            if (!dumpFilter.filtered || z) {
                printWriter.println("\n  Zen Mode:");
                printWriter.print("    mInterruptionFilter=");
                printWriter.println(this.mInterruptionFilter);
                this.mZenModeHelper.dump(printWriter, "    ");
                printWriter.println("\n  Zen Log:");
                ZenLog.dump(printWriter, "    ");
            }
            printWriter.println("\n  Condition providers:");
            this.mConditionProviders.dump(printWriter, dumpFilter);
            printWriter.println("\n  Group summaries:");
            for (Map.Entry<String, NotificationRecord> entry : this.mSummaryByGroupKey.entrySet()) {
                NotificationRecord value = entry.getValue();
                printWriter.println("    " + entry.getKey() + " -> " + value.getKey());
                if (this.mNotificationsByKey.get(value.getKey()) != value) {
                    printWriter.println("!!!!!!LEAK: Record not found in mNotificationsByKey.");
                    value.dump(printWriter, "      ", getContext(), dumpFilter.redact);
                }
            }
            if (!z) {
                printWriter.println("\n  Usage Stats:");
                this.mUsageStats.dump(printWriter, "    ", dumpFilter);
            }
            if (DBG) {
                printWriter.println("\n  mCustomizeBreathLight: " + mCustomizeBreathLight);
                printWriter.println("\n  mSettingsSpecialLights: " + this.mSettingsSpecialLights);
                printWriter.println("\n  mBreathAllowPeriod: " + this.mBreathAllowPeriod);
                printWriter.println("\n  mScreenLocked: " + this.mScreenLocked);
                printWriter.println("\n  mIsFlashing: " + this.mIsFlashing);
                printWriter.println("\n  isInBreathAllowPeriod: " + isInBreathAllowPeriod());
                printWriter.println("\n  mLightList: ");
                ArrayList<String> arrayList = mLightList;
                if (arrayList != null) {
                    printWriter.println(arrayList.toString());
                }
                printWriter.println();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.notification.NotificationManagerService$12, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass12 implements NotificationManagerInternal {
        AnonymousClass12() {
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public NotificationChannel getNotificationChannel(String str, int i, String str2) {
            return NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(str, i, str2, false);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public NotificationChannelGroup getNotificationChannelGroup(String str, int i, String str2) {
            return NotificationManagerService.this.mPreferencesHelper.getGroupForChannel(str, i, str2);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void enqueueNotification(String str, String str2, int i, int i2, String str3, int i3, Notification notification, int i4) {
            NotificationManagerService.this.enqueueNotificationInternal(str, str2, i, i2, str3, i3, notification, i4, false);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void enqueueNotification(String str, String str2, int i, int i2, String str3, int i3, Notification notification, int i4, boolean z) {
            NotificationManagerService.this.enqueueNotificationInternal(str, str2, i, i2, str3, i3, notification, i4, z);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void cancelNotification(String str, String str2, int i, int i2, String str3, int i3, int i4) {
            NotificationManagerService.this.cancelNotificationInternal(str, str2, i, i2, str3, i3, i4);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public boolean isNotificationShown(String str, String str2, int i, int i2) {
            return NotificationManagerService.this.isNotificationShownInternal(str, str2, i, i2);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void removeForegroundServiceFlagFromNotification(final String str, final int i, final int i2) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationManagerService.AnonymousClass12.this.lambda$removeForegroundServiceFlagFromNotification$0(str, i, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeForegroundServiceFlagFromNotification$0(String str, int i, int i2) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                removeFlagFromNotificationLocked(str, i, i2, 64);
            }
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void removeUserInitiatedJobFlagFromNotification(final String str, final int i, final int i2) {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationManagerService.AnonymousClass12.this.lambda$removeUserInitiatedJobFlagFromNotification$1(str, i, i2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeUserInitiatedJobFlagFromNotification$1(String str, int i, int i2) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                removeFlagFromNotificationLocked(str, i, i2, 32768);
            }
        }

        @GuardedBy({"mNotificationLock"})
        private void removeFlagFromNotificationLocked(String str, int i, int i2, int i3) {
            boolean z;
            if (NotificationManagerService.this.getNotificationCount(str, i2) > 50) {
                NotificationManagerService.this.mUsageStats.registerOverCountQuota(str);
                z = true;
            } else {
                z = false;
            }
            if (z) {
                NotificationRecord findNotificationLocked = NotificationManagerService.this.findNotificationLocked(str, null, i, i2);
                if (findNotificationLocked != null) {
                    if (NotificationManagerService.DBG) {
                        String str2 = i3 == 64 ? "FGS" : "UIJ";
                        Slog.d(NotificationManagerService.TAG, "Remove " + str2 + " flag not allow. Cancel " + str2 + " notification");
                    }
                    NotificationManagerService.this.removeFromNotificationListsLocked(findNotificationLocked);
                    NotificationManagerService.this.cancelNotificationLocked(findNotificationLocked, false, 8, true, null, SystemClock.elapsedRealtime());
                    return;
                }
                return;
            }
            NotificationManagerService notificationManagerService = NotificationManagerService.this;
            List findNotificationsByListLocked = notificationManagerService.findNotificationsByListLocked(notificationManagerService.mEnqueuedNotifications, str, null, i, i2);
            for (int i4 = 0; i4 < findNotificationsByListLocked.size(); i4++) {
                NotificationRecord notificationRecord = (NotificationRecord) findNotificationsByListLocked.get(i4);
                if (notificationRecord != null) {
                    notificationRecord.getSbn().getNotification().flags = notificationRecord.mOriginalFlags & (~i3);
                }
            }
            NotificationManagerService notificationManagerService2 = NotificationManagerService.this;
            NotificationRecord findNotificationByListLocked = notificationManagerService2.findNotificationByListLocked(notificationManagerService2.mNotificationList, str, null, i, i2);
            if (findNotificationByListLocked != null) {
                findNotificationByListLocked.getSbn().getNotification().flags = findNotificationByListLocked.mOriginalFlags & (~i3);
                NotificationManagerService notificationManagerService3 = NotificationManagerService.this;
                notificationManagerService3.mRankingHelper.sort(notificationManagerService3.mNotificationList);
                NotificationManagerService.this.mListeners.notifyPostedLocked(findNotificationByListLocked, findNotificationByListLocked);
            }
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void onConversationRemoved(String str, int i, Set<String> set) {
            NotificationManagerService.this.onConversationRemovedInternal(str, i, set);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public int getNumNotificationChannelsForPackage(String str, int i, boolean z) {
            return NotificationManagerService.this.getNumNotificationChannelsForPackage(str, i, z);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public boolean areNotificationsEnabledForPackage(String str, int i) {
            return NotificationManagerService.this.areNotificationsEnabledForPackageInt(str, i);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void sendReviewPermissionsNotification() {
            NotificationManagerService notificationManagerService = NotificationManagerService.this;
            if (notificationManagerService.mShowReviewPermissionsNotification) {
                notificationManagerService.checkCallerIsSystem();
                ((NotificationManager) NotificationManagerService.this.getContext().getSystemService(NotificationManager.class)).notify(NotificationManagerService.TAG, 71, NotificationManagerService.this.createReviewPermissionsNotification());
                Settings.Global.putInt(NotificationManagerService.this.getContext().getContentResolver(), "review_permissions_notification_state", 3);
            }
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void cleanupHistoryFiles() {
            NotificationManagerService.this.checkCallerIsSystem();
            NotificationManagerService.this.mHistoryManager.cleanupHistoryFiles();
        }
    }

    int getNumNotificationChannelsForPackage(String str, int i, boolean z) {
        return this.mPreferencesHelper.getNotificationChannels(str, i, z).getList().size();
    }

    String encryptionNotificationKey(Object obj) {
        String obj2;
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            obj2 = (String) obj;
        } else {
            obj2 = obj.toString();
        }
        if (TextUtils.isEmpty(obj2)) {
            return "";
        }
        int indexOf = obj2.indexOf("@");
        return ((indexOf > 0) && (obj2.indexOf(".", indexOf + 2) > -1)) ? "****encryption****" : obj2;
    }

    void cancelNotificationInternal(String str, String str2, int i, int i2, String str3, int i3, int i4) {
        int handleIncomingUser = ActivityManager.handleIncomingUser(i2, i, i4, true, false, "cancelNotificationWithTag", str);
        int resolveNotificationUid = resolveNotificationUid(str2, str, i, handleIncomingUser);
        if (resolveNotificationUid == -1) {
            Slog.w(TAG, str2 + ":" + i + " trying to cancel notification for nonexistent pkg " + str + " in user " + handleIncomingUser);
            return;
        }
        if (!Objects.equals(str, str2)) {
            synchronized (this.mNotificationLock) {
                NotificationRecord findNotificationLocked = findNotificationLocked(str, str3, i3, handleIncomingUser);
                if (findNotificationLocked != null && !Objects.equals(str2, findNotificationLocked.getSbn().getOpPkg()) && i != 1000) {
                    throw new SecurityException(str2 + " does not have permission to cancel a notification they did not post " + str3 + " " + i3);
                }
            }
        }
        cancelNotification(resolveNotificationUid, i2, str, str3, i3, 0, isCallingUidSystem() ? 0 : 33856, false, handleIncomingUser, 8, null);
    }

    boolean isNotificationShownInternal(String str, String str2, int i, int i2) {
        boolean z;
        synchronized (this.mNotificationLock) {
            z = findNotificationLocked(str, str2, i, i2) != null;
        }
        return z;
    }

    void enqueueNotificationInternal(String str, String str2, int i, int i2, String str3, int i3, Notification notification, int i4, boolean z) {
        if (this.mNMSWrapper.getNMSExt() == null || !this.mNMSWrapper.getNMSExt().interceptEnqueueNotificationInternal(str, str2, i, i2, str3, i3, notification, i4)) {
            enqueueNotificationInternal(str, str2, i, i2, str3, i3, notification, i4, false, z);
        }
    }

    void enqueueNotificationInternal(String str, String str2, int i, int i2, String str3, int i3, Notification notification, int i4, boolean z, boolean z2) {
        PostNotificationTracker acquireWakeLockForPost = acquireWakeLockForPost(str, i);
        try {
            if (enqueueNotificationInternal(str, str2, i, i2, str3, i3, notification, i4, z, acquireWakeLockForPost, z2)) {
            }
        } finally {
            acquireWakeLockForPost.cancel();
        }
    }

    private PostNotificationTracker acquireWakeLockForPost(final String str, final int i) {
        if (this.mFlagResolver.isEnabled(SystemUiSystemPropertiesFlags.NotificationFlags.WAKE_LOCK_FOR_POSTING_NOTIFICATION) && ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda8
            public final Object getOrThrow() {
                Boolean lambda$acquireWakeLockForPost$6;
                lambda$acquireWakeLockForPost$6 = NotificationManagerService.lambda$acquireWakeLockForPost$6();
                return lambda$acquireWakeLockForPost$6;
            }
        })).booleanValue()) {
            return (PostNotificationTracker) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda9
                public final Object getOrThrow() {
                    NotificationManagerService.PostNotificationTracker lambda$acquireWakeLockForPost$7;
                    lambda$acquireWakeLockForPost$7 = NotificationManagerService.this.lambda$acquireWakeLockForPost$7(str, i);
                    return lambda$acquireWakeLockForPost$7;
                }
            });
        }
        return this.mPostNotificationTrackerFactory.newTracker(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$acquireWakeLockForPost$6() throws Exception {
        return Boolean.valueOf(DeviceConfig.getBoolean("systemui", "nms_notify_wakelock", false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ PostNotificationTracker lambda$acquireWakeLockForPost$7(String str, int i) throws Exception {
        PowerManager.WakeLock newWakeLock = this.mPowerManager.newWakeLock(1, "NotificationManagerService:post:" + str);
        newWakeLock.setWorkSource(new WorkSource(i, str));
        newWakeLock.acquire(30000L);
        return this.mPostNotificationTrackerFactory.newTracker(newWakeLock);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:127:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x02f4  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:135:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x033d  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0353 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0354  */
    /* JADX WARN: Type inference failed for: r4v13 */
    /* JADX WARN: Type inference failed for: r4v14, types: [int] */
    /* JADX WARN: Type inference failed for: r4v16 */
    /* JADX WARN: Type inference failed for: r5v13, types: [android.util.ArraySet] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean enqueueNotificationInternal(String str, String str2, int i, int i2, String str3, int i3, Notification notification, int i4, boolean z, PostNotificationTracker postNotificationTracker, boolean z2) {
        int i5;
        StatusBarNotification statusBarNotification;
        NotificationRecord notificationRecord;
        int i6;
        boolean z3;
        ShortcutHelper shortcutHelper;
        UserHandle userHandle;
        ShortcutInfo shortcutInfo;
        int size;
        boolean z4;
        if (DBG) {
            Slog.v(TAG, "enqueueNotificationInternal: pkg=" + str + " id=" + i3 + " notification=" + notification);
        }
        if (str == null || notification == null) {
            throw new IllegalArgumentException("null not allowed: pkg=" + str + " id=" + i3 + " notification=" + notification);
        }
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().updateNoClearNotification(notification, str);
        }
        int handleIncomingUser = ActivityManager.handleIncomingUser(i2, i, i4, true, false, "enqueueNotification", str);
        UserHandle of = UserHandle.of(handleIncomingUser);
        int resolveNotificationUid = resolveNotificationUid(str2, str, i, handleIncomingUser);
        if (resolveNotificationUid == -1) {
            throw new SecurityException("Caller " + str2 + ":" + i + " trying to post for invalid pkg " + str + " in user " + i4);
        }
        checkRestrictedCategories(notification);
        ActivityManagerInternal.ServiceNotificationPolicy applyForegroundServiceNotification = this.mAmi.applyForegroundServiceNotification(notification, str3, i3, str, handleIncomingUser);
        try {
            fixNotification(notification, str, str3, i3, handleIncomingUser, resolveNotificationUid, applyForegroundServiceNotification, ((JobSchedulerInternal) LocalServices.getService(JobSchedulerInternal.class)) != null ? !r1.isNotificationAssociatedWithAnyUserInitiatedJobs(i3, handleIncomingUser, str) : true);
            if (applyForegroundServiceNotification == ActivityManagerInternal.ServiceNotificationPolicy.UPDATE_ONLY) {
                if (this.mNMSWrapper.getNMSExt() != null) {
                    i5 = resolveNotificationUid;
                    if (this.mNMSWrapper.getNMSExt().shouldUpdateNavigationMode(str, i3, i5)) {
                        z4 = true;
                        if (isNotificationShownInternal(str, str3, i3, handleIncomingUser) || z4) {
                            reportForegroundServiceUpdate(false, notification, i3, str, handleIncomingUser);
                            if (z4) {
                                return false;
                            }
                            this.mNMSWrapper.getNMSExt().setKeepAliveAppIfNeeded(str, i3, true);
                            return false;
                        }
                    }
                } else {
                    i5 = resolveNotificationUid;
                }
                z4 = false;
                if (isNotificationShownInternal(str, str3, i3, handleIncomingUser)) {
                }
                reportForegroundServiceUpdate(false, notification, i3, str, handleIncomingUser);
                if (z4) {
                }
            } else {
                i5 = resolveNotificationUid;
            }
            this.mUsageStats.registerEnqueuedByApp(str);
            int i7 = i5;
            StatusBarNotification statusBarNotification2 = new StatusBarNotification(str, str2, i3, str3, i5, i2, notification, of, (String) null, System.currentTimeMillis());
            String channelId = notification.getChannelId();
            if (this.mIsTelevision && new Notification.TvExtender(notification).getChannelId() != null) {
                channelId = new Notification.TvExtender(notification).getChannelId();
            }
            String str4 = channelId;
            NotificationChannel conversationNotificationChannel = this.mPreferencesHelper.getConversationNotificationChannel(str, i7, str4, statusBarNotification2.getShortcutId(), true, false);
            if (conversationNotificationChannel == null) {
                Slog.e(TAG, "No Channel found for pkg=" + str + ", channelId=" + str4 + ", id=" + i3 + ", tag=" + str3 + ", opPkg=" + str2 + ", callingUid=" + i + ", userId=" + handleIncomingUser + ", incomingUserId=" + i4 + ", notificationUid=" + i7 + ", notification=" + notification);
                boolean hasPermission = this.mPermissionHelper.hasPermission(i7) ^ true;
                boolean shouldShowNotificationToast = this.mNMSWrapper.getNMSExt() != null ? this.mNMSWrapper.getNMSExt().shouldShowNotificationToast() : true;
                if (hasPermission || !shouldShowNotificationToast) {
                    return false;
                }
                doChannelWarningToast(i7, "Developer warning for package \"" + str + "\"\nFailed to post notification on channel \"" + str4 + "\"\nSee log for more details");
                return false;
            }
            NotificationRecord notificationRecord2 = new NotificationRecord(getContext(), statusBarNotification2, conversationNotificationChannel);
            if (this.mNMSWrapper.getNMSExt() != null) {
                this.mNMSWrapper.getNMSExt().detectRedPacket(i7, i, notificationRecord2);
            }
            notificationRecord2.setIsAppImportanceLocked(this.mPermissionHelper.isPermissionUserSet(str, handleIncomingUser));
            notificationRecord2.setPostSilently(z);
            notificationRecord2.setFlagBubbleRemoved(false);
            notificationRecord2.setPkgAllowedAsConvo(this.mNMSWrapper.getNMSExt() != null ? this.mNMSWrapper.getNMSExt().isAllowedAsConvo(str) : false);
            if (this.mNMSWrapper.getNMSExt() != null) {
                this.mNMSWrapper.getNMSExt().setSupportConversation(notificationRecord2);
            }
            notificationRecord2.setImportanceFixed(this.mPermissionHelper.isPermissionFixed(str, handleIncomingUser));
            if (notification.isFgsOrUij()) {
                if (((conversationNotificationChannel.getUserLockedFields() & 4) == 0 || !conversationNotificationChannel.isUserVisibleTaskShown()) && (notificationRecord2.getImportance() == 1 || notificationRecord2.getImportance() == 0)) {
                    conversationNotificationChannel.setImportance(2);
                    notificationRecord2.setSystemImportance(2);
                    if (!conversationNotificationChannel.isUserVisibleTaskShown()) {
                        conversationNotificationChannel.unlockFields(4);
                        conversationNotificationChannel.setUserVisibleTaskShown(true);
                    }
                    statusBarNotification = statusBarNotification2;
                    notificationRecord = notificationRecord2;
                    i6 = i3;
                    z3 = false;
                    this.mPreferencesHelper.updateNotificationChannel(str, i7, conversationNotificationChannel, false, i, isCallerIsSystemOrSystemUi());
                    notificationRecord.updateNotificationChannel(conversationNotificationChannel);
                    shortcutHelper = this.mShortcutHelper;
                    if (shortcutHelper == null) {
                        userHandle = of;
                        shortcutInfo = shortcutHelper.getValidShortcutInfo(notification.getShortcutId(), str, userHandle);
                    } else {
                        userHandle = of;
                        shortcutInfo = null;
                    }
                    ShortcutInfo shortcutInfo2 = shortcutInfo;
                    if (notification.getShortcutId() != null && shortcutInfo2 == null) {
                        Slog.w(TAG, "notification " + notificationRecord.getKey() + " added an invalid shortcut");
                    }
                    notificationRecord.setShortcutInfo(shortcutInfo2);
                    notificationRecord.setHasSentValidMsg(this.mPreferencesHelper.hasSentValidMsg(str, i7));
                    notificationRecord.userDemotedAppFromConvoSpace(this.mPreferencesHelper.hasUserDemotedInvalidMsgApp(str, i7));
                    if (checkDisqualifyingFeatures(handleIncomingUser, i7, i3, str3, notificationRecord, notificationRecord.getSbn().getOverrideGroupKey() == null ? true : z3, z2)) {
                        return z3;
                    }
                    if (this.mNMSWrapper.getNMSExt() != null) {
                        this.mNMSWrapper.getNMSExt().setKeepAliveAppIfNeeded(str, i6, true);
                    }
                    if (shortcutInfo2 != null) {
                        this.mShortcutHelper.cacheShortcut(shortcutInfo2, userHandle);
                    }
                    ArraySet arraySet = notification.allPendingIntents;
                    if (arraySet != null && (size = arraySet.size()) > 0) {
                        long notificationAllowlistDuration = ((DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class)).getNotificationAllowlistDuration();
                        for (?? r4 = z3; r4 < size; r4++) {
                            PendingIntent pendingIntent = (PendingIntent) notification.allPendingIntents.valueAt(r4);
                            if (pendingIntent != null) {
                                ActivityManagerInternal activityManagerInternal = this.mAmi;
                                IIntentSender target = pendingIntent.getTarget();
                                IBinder iBinder = ALLOWLIST_TOKEN;
                                activityManagerInternal.setPendingIntentAllowlistDuration(target, iBinder, notificationAllowlistDuration, 0, 310, "NotificationManagerService");
                                this.mAmi.setPendingIntentAllowBgActivityStarts(pendingIntent.getTarget(), iBinder, 7);
                            }
                        }
                    }
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        boolean z5 = this.mActivityManager.getPackageImportance(str) == 100 ? true : z3;
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        if (notification.getSmallIcon() != null && this.mNMSWrapper.getNMSExt() != null) {
                            this.mNMSWrapper.getNMSExt().onPendingNotifyPosted(statusBarNotification);
                        }
                        this.mHandler.post(new EnqueueNotificationRunnable(handleIncomingUser, notificationRecord, z5, postNotificationTracker));
                        if (this.mNMSWrapper.getNMSExt() == null) {
                            return true;
                        }
                        this.mNMSWrapper.getNMSExt().notifyNotificationCentent(str, notification.extras.getCharSequence("android.title", "").toString(), notification.extras.getCharSequence("android.text", "").toString());
                        return true;
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
                if (!conversationNotificationChannel.isUserVisibleTaskShown() && !TextUtils.isEmpty(str4) && !"miscellaneous".equals(str4)) {
                    conversationNotificationChannel.setUserVisibleTaskShown(true);
                    notificationRecord2.updateNotificationChannel(conversationNotificationChannel);
                }
            }
            statusBarNotification = statusBarNotification2;
            notificationRecord = notificationRecord2;
            i6 = i3;
            z3 = false;
            shortcutHelper = this.mShortcutHelper;
            if (shortcutHelper == null) {
            }
            ShortcutInfo shortcutInfo22 = shortcutInfo;
            if (notification.getShortcutId() != null) {
                Slog.w(TAG, "notification " + notificationRecord.getKey() + " added an invalid shortcut");
            }
            notificationRecord.setShortcutInfo(shortcutInfo22);
            notificationRecord.setHasSentValidMsg(this.mPreferencesHelper.hasSentValidMsg(str, i7));
            notificationRecord.userDemotedAppFromConvoSpace(this.mPreferencesHelper.hasUserDemotedInvalidMsgApp(str, i7));
            if (checkDisqualifyingFeatures(handleIncomingUser, i7, i3, str3, notificationRecord, notificationRecord.getSbn().getOverrideGroupKey() == null ? true : z3, z2)) {
            }
        } catch (Exception e) {
            if (notification.isForegroundService()) {
                throw new SecurityException("Invalid FGS notification", e);
            }
            Slog.e(TAG, "Cannot fix notification", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConversationRemovedInternal(String str, int i, Set<String> set) {
        checkCallerIsSystem();
        Preconditions.checkStringNotEmpty(str);
        this.mHistoryManager.deleteConversations(str, i, set);
        Iterator<String> it = this.mPreferencesHelper.deleteConversations(str, i, set, 1000, true).iterator();
        while (it.hasNext()) {
            cancelAllNotificationsInt(MY_UID, MY_PID, str, it.next(), 0, 0, true, UserHandle.getUserId(i), 20, null);
        }
        handleSavePolicyFile();
    }

    private void makeStickyHun(Notification notification, String str, int i) {
        if (this.mPermissionHelper.hasRequestedPermission("android.permission.USE_FULL_SCREEN_INTENT", str, i)) {
            notification.flags |= 16384;
        }
        if (notification.contentIntent == null) {
            notification.contentIntent = notification.fullScreenIntent;
        }
        notification.fullScreenIntent = null;
    }

    @VisibleForTesting
    protected void fixNotification(Notification notification, String str, String str2, int i, int i2, int i3, ActivityManagerInternal.ServiceNotificationPolicy serviceNotificationPolicy, boolean z) throws PackageManager.NameNotFoundException, RemoteException {
        ApplicationInfo applicationInfoAsUser = this.mPackageManagerClient.getApplicationInfoAsUser(str, 268435456, i2 == -1 ? 0 : i2);
        Notification.addFieldsFromContext(applicationInfoAsUser, notification);
        if (notification.isForegroundService() && serviceNotificationPolicy == ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE) {
            notification.flags &= -65;
        }
        if (notification.isUserInitiatedJob() && z) {
            notification.flags &= -32769;
        }
        if (notification.isFgsOrUij()) {
            notification.flags &= -17;
        }
        if (this.mFlagResolver.isEnabled(SystemUiSystemPropertiesFlags.NotificationFlags.ALLOW_DISMISS_ONGOING)) {
            if ((notification.flags & 2) > 0 && canBeNonDismissible(applicationInfoAsUser, notification)) {
                notification.flags |= 8192;
            } else {
                notification.flags &= -8193;
            }
        }
        if (getContext().checkPermission("android.permission.USE_COLORIZED_NOTIFICATIONS", -1, i3) == 0) {
            notification.flags |= 2048;
        } else {
            notification.flags &= -2049;
        }
        if (notification.extras.getBoolean("android.allowDuringSetup", false) && getContext().checkPermission("android.permission.NOTIFICATION_DURING_SETUP", -1, i3) != 0) {
            notification.extras.remove("android.allowDuringSetup");
            if (DBG) {
                Slog.w(TAG, "warning: pkg " + str + " attempting to show during setup without holding perm android.permission.NOTIFICATION_DURING_SETUP");
            }
        }
        notification.flags &= -16385;
        boolean z2 = true;
        if (notification.fullScreenIntent != null) {
            if (this.mFlagResolver.isEnabled(SystemUiSystemPropertiesFlags.NotificationFlags.FSI_FORCE_DEMOTE)) {
                makeStickyHun(notification, str, i2);
            } else {
                AttributionSource build = new AttributionSource.Builder(i3).setPackageName(str).build();
                boolean isEnabled = this.mFlagResolver.isEnabled(SystemUiSystemPropertiesFlags.NotificationFlags.SHOW_STICKY_HUN_FOR_DENIED_FSI);
                if (!checkUseFullScreenIntentPermission(build, applicationInfoAsUser, isEnabled, true)) {
                    if (isEnabled) {
                        makeStickyHun(notification, str, i2);
                    } else {
                        notification.fullScreenIntent = null;
                        Slog.w(TAG, "Package " + str + ": Use of fullScreenIntent requires theUSE_FULL_SCREEN_INTENT permission");
                    }
                }
            }
        }
        Notification.Action[] actionArr = notification.actions;
        if (actionArr != null) {
            int length = actionArr.length;
            int i4 = 0;
            while (true) {
                if (i4 >= length) {
                    z2 = false;
                    break;
                } else if (notification.actions[i4] == null) {
                    break;
                } else {
                    i4++;
                }
            }
            if (z2) {
                ArrayList arrayList = new ArrayList();
                for (int i5 = 0; i5 < length; i5++) {
                    Notification.Action action = notification.actions[i5];
                    if (action != null) {
                        arrayList.add(action);
                    }
                }
                if (arrayList.size() != 0) {
                    notification.actions = (Notification.Action[]) arrayList.toArray(new Notification.Action[0]);
                } else {
                    notification.actions = null;
                }
            }
        }
        if (notification.isStyle(Notification.CallStyle.class)) {
            ArrayList actionsListWithSystemActions = ((Notification.CallStyle) Notification.Builder.recoverBuilder(getContext(), notification).getStyle()).getActionsListWithSystemActions();
            Notification.Action[] actionArr2 = new Notification.Action[actionsListWithSystemActions.size()];
            notification.actions = actionArr2;
            actionsListWithSystemActions.toArray(actionArr2);
        }
        if ((notification.isStyle(Notification.MediaStyle.class) || notification.isStyle(Notification.DecoratedMediaCustomViewStyle.class)) && getContext().checkPermission("android.permission.MEDIA_CONTENT_CONTROL", -1, i3) != 0) {
            notification.extras.remove("android.mediaRemoteDevice");
            notification.extras.remove("android.mediaRemoteIcon");
            notification.extras.remove("android.mediaRemoteIntent");
            if (DBG) {
                Slog.w(TAG, "Package " + str + ": Use of setRemotePlayback requires the MEDIA_CONTENT_CONTROL permission");
            }
        }
        if (notification.extras.containsKey("android.substName") && getContext().checkPermission("android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME", -1, i3) != 0) {
            notification.extras.remove("android.substName");
            if (DBG) {
                Slog.w(TAG, "warning: pkg " + str + " attempting to substitute app name without holding perm android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME");
            }
        }
        checkRemoteViews(str, str2, i, notification);
    }

    private boolean canBeNonDismissible(ApplicationInfo applicationInfo, Notification notification) {
        return notification.isMediaNotification() || isEnterpriseExempted(applicationInfo) || notification.isStyle(Notification.CallStyle.class) || isDefaultSearchSelectorPackage(applicationInfo.packageName);
    }

    private boolean isDefaultSearchSelectorPackage(String str) {
        return Objects.equals(this.mDefaultSearchSelectorPkg, str);
    }

    private boolean isEnterpriseExempted(ApplicationInfo applicationInfo) {
        DevicePolicyManagerInternal devicePolicyManagerInternal = this.mDpm;
        if (devicePolicyManagerInternal == null || !(devicePolicyManagerInternal.isActiveProfileOwner(applicationInfo.uid) || this.mDpm.isActiveDeviceOwner(applicationInfo.uid))) {
            return this.mSystemExemptFromDismissal && this.mAppOps.checkOpNoThrow(125, applicationInfo.uid, applicationInfo.packageName) == 0;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkUseFullScreenIntentPermission(AttributionSource attributionSource, ApplicationInfo applicationInfo, boolean z, boolean z2) {
        int checkPermissionForPreflight;
        if (applicationInfo.targetSdkVersion < 29) {
            return true;
        }
        if (!z) {
            return getContext().checkPermission("android.permission.USE_FULL_SCREEN_INTENT", attributionSource.getPid(), attributionSource.getUid()) == 0;
        }
        if (z2) {
            checkPermissionForPreflight = this.mPermissionManager.checkPermissionForDataDelivery("android.permission.USE_FULL_SCREEN_INTENT", attributionSource, (String) null);
        } else {
            checkPermissionForPreflight = this.mPermissionManager.checkPermissionForPreflight("android.permission.USE_FULL_SCREEN_INTENT", attributionSource);
        }
        return checkPermissionForPreflight == 0;
    }

    private void checkRemoteViews(String str, String str2, int i, Notification notification) {
        if (removeRemoteView(str, str2, i, notification.contentView)) {
            notification.contentView = null;
        }
        if (removeRemoteView(str, str2, i, notification.bigContentView)) {
            notification.bigContentView = null;
        }
        if (removeRemoteView(str, str2, i, notification.headsUpContentView)) {
            notification.headsUpContentView = null;
        }
        Notification notification2 = notification.publicVersion;
        if (notification2 != null) {
            if (removeRemoteView(str, str2, i, notification2.contentView)) {
                notification.publicVersion.contentView = null;
            }
            if (removeRemoteView(str, str2, i, notification.publicVersion.bigContentView)) {
                notification.publicVersion.bigContentView = null;
            }
            if (removeRemoteView(str, str2, i, notification.publicVersion.headsUpContentView)) {
                notification.publicVersion.headsUpContentView = null;
            }
        }
    }

    private boolean removeRemoteView(String str, String str2, int i, RemoteViews remoteViews) {
        if (remoteViews == null) {
            return false;
        }
        int estimateMemoryUsage = remoteViews.estimateMemoryUsage();
        if (estimateMemoryUsage > this.mWarnRemoteViewsSizeBytes && estimateMemoryUsage < this.mStripRemoteViewsSizeBytes) {
            Slog.w(TAG, "RemoteViews too large on pkg: " + str + " tag: " + str2 + " id: " + i + " this might be stripped in a future release");
        }
        if (estimateMemoryUsage < this.mStripRemoteViewsSizeBytes) {
            return false;
        }
        this.mUsageStats.registerImageRemoved(str);
        Slog.w(TAG, "Removed too large RemoteViews (" + estimateMemoryUsage + " bytes) on pkg: " + str + " tag: " + str2 + " id: " + i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNotificationBubbleFlags(NotificationRecord notificationRecord, boolean z) {
        Notification.BubbleMetadata bubbleMetadata = notificationRecord.getNotification().getBubbleMetadata();
        if (bubbleMetadata == null) {
            return;
        }
        if (!z) {
            bubbleMetadata.setFlags(bubbleMetadata.getFlags() & (-2));
        }
        if (bubbleMetadata.isBubbleSuppressable()) {
            return;
        }
        bubbleMetadata.setFlags(bubbleMetadata.getFlags() & (-9));
    }

    protected void doChannelWarningToast(int i, final CharSequence charSequence) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda5
            public final void runOrThrow() {
                NotificationManagerService.this.lambda$doChannelWarningToast$8(charSequence);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doChannelWarningToast$8(CharSequence charSequence) throws Exception {
        if (Settings.Global.getInt(getContext().getContentResolver(), "show_notification_channel_warnings", 0) != 0) {
            Toast.makeText(getContext(), this.mHandler.getLooper(), charSequence, 0).show();
        }
    }

    @VisibleForTesting
    int resolveNotificationUid(String str, String str2, int i, int i2) {
        int i3 = -1;
        if (i2 == -1) {
            i2 = 0;
        }
        if (isCallerSameApp(str2, i, i2) && (TextUtils.equals(str, str2) || isCallerSameApp(str, i, i2))) {
            return i;
        }
        try {
            i3 = this.mPackageManagerClient.getPackageUidAsUser(str2, i2);
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (isCallerAndroid(str, i) || this.mPreferencesHelper.isDelegateAllowed(str2, i3, str, i)) {
            return i3;
        }
        throw new SecurityException("Caller " + str + ":" + i + " cannot post for pkg " + str2 + " in user " + i2);
    }

    boolean checkDisqualifyingFeatures(int i, int i2, int i3, String str, NotificationRecord notificationRecord, boolean z, boolean z2) {
        boolean isRecordBlockedLocked;
        int notificationCount;
        Notification notification = notificationRecord.getNotification();
        String packageName = notificationRecord.getSbn().getPackageName();
        boolean z3 = isUidSystemOrPhone(i2) || PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName);
        boolean isListenerPackage = this.mListeners.isListenerPackage(packageName);
        if (!z3 && !isListenerPackage) {
            int callingUid = Binder.getCallingUid();
            synchronized (this.mNotificationLock) {
                if (this.mNotificationsByKey.get(notificationRecord.getSbn().getKey()) == null && isCallerInstantApp(callingUid, i)) {
                    throw new SecurityException("Instant app " + packageName + " cannot create notifications");
                }
                if (this.mNotificationsByKey.get(notificationRecord.getSbn().getKey()) != null && !notificationRecord.getNotification().hasCompletedProgress() && !z) {
                    float appEnqueueRate = this.mUsageStats.getAppEnqueueRate(packageName);
                    if (appEnqueueRate > this.mMaxPackageEnqueueRate) {
                        this.mUsageStats.registerOverRateQuota(packageName);
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (elapsedRealtime - this.mLastOverRateLogTime > MIN_PACKAGE_OVERRATE_LOG_INTERVAL) {
                            Slog.e(TAG, "Package enqueue rate is " + appEnqueueRate + ". Shedding " + notificationRecord.getSbn().getKey() + ". package=" + packageName);
                            this.mLastOverRateLogTime = elapsedRealtime;
                        }
                        return false;
                    }
                }
                if (!notification.isFgsOrUij() && (notificationCount = getNotificationCount(packageName, i, i3, str)) >= 50) {
                    this.mUsageStats.registerOverCountQuota(packageName);
                    Slog.e(TAG, "Package has already posted or enqueued " + notificationCount + " notifications.  Not showing more.  package=" + packageName);
                    return false;
                }
            }
        }
        if (notification.getBubbleMetadata() != null && notification.getBubbleMetadata().getIntent() != null && hasFlag(this.mAmi.getPendingIntentFlags(notification.getBubbleMetadata().getIntent().getTarget()), 67108864)) {
            throw new IllegalArgumentException(notificationRecord.getKey() + " Not posted. PendingIntents attached to bubbles must be mutable");
        }
        Notification.Action[] actionArr = notification.actions;
        if (actionArr != null) {
            for (Notification.Action action : actionArr) {
                if (!(action.getRemoteInputs() == null && action.getDataOnlyRemoteInputs() == null) && hasFlag(this.mAmi.getPendingIntentFlags(action.actionIntent.getTarget()), 67108864)) {
                    throw new IllegalArgumentException(notificationRecord.getKey() + " Not posted. PendingIntents attached to actions with remote inputs must be mutable");
                }
            }
        }
        if (notificationRecord.getSystemGeneratedSmartActions() != null) {
            Iterator<Notification.Action> it = notificationRecord.getSystemGeneratedSmartActions().iterator();
            while (it.hasNext()) {
                Notification.Action next = it.next();
                if (next.getRemoteInputs() != null || next.getDataOnlyRemoteInputs() != null) {
                    if (hasFlag(this.mAmi.getPendingIntentFlags(next.actionIntent.getTarget()), 67108864)) {
                        throw new IllegalArgumentException(notificationRecord.getKey() + " Not posted. PendingIntents attached to contextual actions with remote inputs must be mutable");
                    }
                }
            }
        }
        if (notification.isStyle(Notification.CallStyle.class)) {
            boolean z4 = notification.fullScreenIntent != null;
            boolean z5 = (notification.flags & 16384) != 0;
            if (!notification.isFgsOrUij() && !z4 && !z5 && !z2) {
                throw new IllegalArgumentException(notificationRecord.getKey() + " Not posted. CallStyle notifications must be for a foreground service or user initated job or use a fullScreenIntent.");
            }
        }
        if (this.mSnoozeHelper.isSnoozed(i, packageName, notificationRecord.getKey())) {
            MetricsLogger.action(notificationRecord.getLogMaker().setType(6).setCategory(831));
            this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.NOTIFICATION_NOT_POSTED_SNOOZED, notificationRecord);
            if (DBG) {
                Slog.d(TAG, "Ignored enqueue for snoozed notification " + notificationRecord.getKey());
            }
            this.mSnoozeHelper.update(i, notificationRecord);
            handleSavePolicyFile();
            return false;
        }
        boolean z6 = !areNotificationsEnabledForPackageInt(packageName, i2);
        synchronized (this.mNotificationLock) {
            isRecordBlockedLocked = z6 | isRecordBlockedLocked(notificationRecord);
        }
        if (!isRecordBlockedLocked || notification.isMediaNotification() || isCallNotification(packageName, i2, notification)) {
            return true;
        }
        if (DBG) {
            Slog.e(TAG, "Suppressing notification from package " + notificationRecord.getSbn().getPackageName() + " by user request.");
        }
        this.mUsageStats.registerBlocked(notificationRecord);
        return false;
    }

    private boolean isCallNotification(String str, int i, Notification notification) {
        if (notification.isStyle(Notification.CallStyle.class)) {
            return isCallNotification(str, i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0023, code lost:
    
        if (r4.mTelecomManager.isInSelfManagedCall(r5, android.os.UserHandle.getUserHandleForUid(r6)) != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isCallNotification(String str, int i) {
        TelecomManager telecomManager;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean z = false;
            if (!this.mPackageManagerClient.hasSystemFeature("android.software.telecom") || (telecomManager = this.mTelecomManager) == null) {
                return false;
            }
            try {
                if (!telecomManager.isInManagedCall()) {
                }
                z = true;
                return z;
            } catch (IllegalStateException unused) {
                return false;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean areNotificationsEnabledForPackageInt(String str, int i) {
        return this.mPermissionHelper.hasPermission(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNotificationCount(String str, int i) {
        int i2;
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i3);
                if (notificationRecord.getSbn().getPackageName().equals(str) && notificationRecord.getSbn().getUserId() == i) {
                    i2++;
                }
            }
            int size2 = this.mEnqueuedNotifications.size();
            for (int i4 = 0; i4 < size2; i4++) {
                NotificationRecord notificationRecord2 = this.mEnqueuedNotifications.get(i4);
                if (notificationRecord2.getSbn().getPackageName().equals(str) && notificationRecord2.getSbn().getUserId() == i) {
                    i2++;
                }
            }
        }
        return i2;
    }

    protected int getNotificationCount(String str, int i, int i2, String str2) {
        int i3;
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            i3 = 0;
            for (int i4 = 0; i4 < size; i4++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i4);
                if (notificationRecord.getSbn().getPackageName().equals(str) && notificationRecord.getSbn().getUserId() == i && (notificationRecord.getSbn().getId() != i2 || !TextUtils.equals(notificationRecord.getSbn().getTag(), str2))) {
                    i3++;
                }
            }
            int size2 = this.mEnqueuedNotifications.size();
            for (int i5 = 0; i5 < size2; i5++) {
                NotificationRecord notificationRecord2 = this.mEnqueuedNotifications.get(i5);
                if (notificationRecord2.getSbn().getPackageName().equals(str) && notificationRecord2.getSbn().getUserId() == i) {
                    i3++;
                }
            }
        }
        return i3;
    }

    @GuardedBy({"mNotificationLock"})
    boolean isRecordBlockedLocked(NotificationRecord notificationRecord) {
        return this.mPreferencesHelper.isGroupBlocked(notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getUid(), notificationRecord.getChannel().getGroup()) || notificationRecord.getImportance() == 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SnoozeNotificationRunnable implements Runnable {
        private final long mDuration;
        private final String mKey;
        private final String mSnoozeCriterionId;

        SnoozeNotificationRunnable(String str, long j, String str2) {
            this.mKey = str;
            this.mDuration = j;
            this.mSnoozeCriterionId = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord findInCurrentAndSnoozedNotificationByKeyLocked = NotificationManagerService.this.findInCurrentAndSnoozedNotificationByKeyLocked(this.mKey);
                if (findInCurrentAndSnoozedNotificationByKeyLocked != null) {
                    snoozeLocked(findInCurrentAndSnoozedNotificationByKeyLocked);
                }
            }
        }

        @GuardedBy({"mNotificationLock"})
        void snoozeLocked(NotificationRecord notificationRecord) {
            ArrayList arrayList = new ArrayList();
            if (notificationRecord.getSbn().isGroup()) {
                List<NotificationRecord> findCurrentAndSnoozedGroupNotificationsLocked = NotificationManagerService.this.findCurrentAndSnoozedGroupNotificationsLocked(notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getGroupKey(), notificationRecord.getSbn().getUserId());
                if (notificationRecord.getNotification().isGroupSummary()) {
                    for (int i = 0; i < findCurrentAndSnoozedGroupNotificationsLocked.size(); i++) {
                        if (!this.mKey.equals(findCurrentAndSnoozedGroupNotificationsLocked.get(i).getKey())) {
                            arrayList.add(findCurrentAndSnoozedGroupNotificationsLocked.get(i));
                        }
                    }
                } else if (NotificationManagerService.this.mSummaryByGroupKey.containsKey(notificationRecord.getSbn().getGroupKey()) && findCurrentAndSnoozedGroupNotificationsLocked.size() == 2) {
                    for (int i2 = 0; i2 < findCurrentAndSnoozedGroupNotificationsLocked.size(); i2++) {
                        if (!this.mKey.equals(findCurrentAndSnoozedGroupNotificationsLocked.get(i2).getKey())) {
                            arrayList.add(findCurrentAndSnoozedGroupNotificationsLocked.get(i2));
                        }
                    }
                }
            }
            arrayList.add(notificationRecord);
            if (NotificationManagerService.this.mSnoozeHelper.canSnooze(arrayList.size())) {
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    snoozeNotificationLocked((NotificationRecord) arrayList.get(i3));
                }
            } else {
                Log.w(NotificationManagerService.TAG, "Cannot snooze " + notificationRecord.getKey() + ": too many snoozed notifications");
            }
        }

        @GuardedBy({"mNotificationLock"})
        void snoozeNotificationLocked(NotificationRecord notificationRecord) {
            MetricsLogger.action(notificationRecord.getLogMaker().setCategory(831).setType(2).addTaggedData(1139, Long.valueOf(this.mDuration)).addTaggedData(832, Integer.valueOf(this.mSnoozeCriterionId == null ? 0 : 1)));
            NotificationManagerService.this.mNotificationRecordLogger.log(NotificationRecordLogger.NotificationEvent.NOTIFICATION_SNOOZED, notificationRecord);
            NotificationManagerService.this.reportUserInteraction(notificationRecord);
            NotificationManagerService.this.cancelNotificationLocked(notificationRecord, false, 18, NotificationManagerService.this.removeFromNotificationListsLocked(notificationRecord), null, SystemClock.elapsedRealtime());
            NotificationManagerService.this.updateLightsLocked();
            if (isSnoozable(notificationRecord)) {
                if (this.mSnoozeCriterionId != null) {
                    NotificationManagerService.this.mAssistants.notifyAssistantSnoozedLocked(notificationRecord, this.mSnoozeCriterionId);
                    NotificationManagerService.this.mSnoozeHelper.snooze(notificationRecord, this.mSnoozeCriterionId);
                } else {
                    NotificationManagerService.this.mSnoozeHelper.snooze(notificationRecord, this.mDuration);
                }
                notificationRecord.recordSnoozed();
                NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        private boolean isSnoozable(NotificationRecord notificationRecord) {
            return (notificationRecord.getNotification().isGroupSummary() && "ranker_group".equals(notificationRecord.getNotification().getGroup())) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class CancelNotificationRunnable implements Runnable {
        private final int mCallingPid;
        private final int mCallingUid;
        private final long mCancellationElapsedTimeMs;
        private final int mCount;
        private final int mId;
        private final ManagedServices.ManagedServiceInfo mListener;
        private final int mMustHaveFlags;
        private final int mMustNotHaveFlags;
        private final String mPkg;
        private final int mRank;
        private final int mReason;
        private final boolean mSendDelete;
        private final String mTag;
        private final int mUserId;

        CancelNotificationRunnable(int i, int i2, String str, String str2, int i3, int i4, int i5, boolean z, int i6, int i7, int i8, int i9, ManagedServices.ManagedServiceInfo managedServiceInfo, long j) {
            this.mCallingUid = i;
            this.mCallingPid = i2;
            this.mPkg = str;
            this.mTag = str2;
            this.mId = i3;
            this.mMustHaveFlags = i4;
            this.mMustNotHaveFlags = i5;
            this.mSendDelete = z;
            this.mUserId = i6;
            this.mReason = i7;
            this.mRank = i8;
            this.mCount = i9;
            this.mListener = managedServiceInfo;
            this.mCancellationElapsedTimeMs = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                    Slog.v(NotificationManagerService.TAG, "Notification--cancelNotification: pkg=" + this.mPkg + ",callingUid:" + this.mCallingUid + ",callingPid=" + this.mCallingPid + ",tag:" + NotificationManagerService.this.encryptionNotificationKey(this.mTag) + ",id:" + this.mId + ",userId:" + this.mUserId + ",reason:" + this.mReason);
                }
                NotificationManagerService.this.mNMSWrapper.getNMSExt().detectCancelAction(NotificationManagerService.this.getContext(), this.mId, this.mPkg, this.mUserId);
            }
            ManagedServices.ManagedServiceInfo managedServiceInfo = this.mListener;
            String shortString = managedServiceInfo == null ? null : managedServiceInfo.component.toShortString();
            if (NotificationManagerService.DBG) {
                EventLogTags.writeNotificationCancel(this.mCallingUid, this.mCallingPid, this.mPkg, this.mId, this.mTag, this.mUserId, this.mMustHaveFlags, this.mMustNotHaveFlags, this.mReason, shortString);
            }
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationRecord findNotificationLocked = NotificationManagerService.this.findNotificationLocked(this.mPkg, this.mTag, this.mId, this.mUserId);
                boolean z = true;
                if (findNotificationLocked != null) {
                    if (this.mReason == 1) {
                        NotificationManagerService.this.mUsageStats.registerClickedByUser(findNotificationLocked);
                    }
                    if ((this.mReason == 10 && findNotificationLocked.getNotification().isBubbleNotification()) || (this.mReason == 1 && findNotificationLocked.canBubble() && findNotificationLocked.isFlagBubbleRemoved())) {
                        NotificationManagerService.this.mNotificationDelegate.onBubbleMetadataFlagChanged(findNotificationLocked.getKey(), (findNotificationLocked.getNotification().getBubbleMetadata() != null ? findNotificationLocked.getNotification().getBubbleMetadata().getFlags() : 0) | 2);
                        return;
                    }
                    int i = findNotificationLocked.getNotification().flags;
                    int i2 = this.mMustHaveFlags;
                    if ((i & i2) != i2) {
                        return;
                    }
                    if ((findNotificationLocked.getNotification().flags & this.mMustNotHaveFlags) != 0) {
                        return;
                    }
                    if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                        NotificationManagerService.this.mNMSWrapper.getNMSExt().setKeepAliveAppIfNeed(this.mPkg, this.mId, false);
                    }
                    FlagChecker flagChecker = new FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$CancelNotificationRunnable$$ExternalSyntheticLambda0
                        @Override // com.android.server.notification.NotificationManagerService.FlagChecker
                        public final boolean apply(int i3) {
                            boolean lambda$run$0;
                            lambda$run$0 = NotificationManagerService.CancelNotificationRunnable.this.lambda$run$0(i3);
                            return lambda$run$0;
                        }
                    };
                    NotificationManagerService.this.cancelNotificationLocked(findNotificationLocked, this.mSendDelete, this.mReason, this.mRank, this.mCount, NotificationManagerService.this.removeFromNotificationListsLocked(findNotificationLocked), shortString, this.mCancellationElapsedTimeMs);
                    NotificationManagerService.this.cancelGroupChildrenLocked(findNotificationLocked, this.mCallingUid, this.mCallingPid, shortString, this.mSendDelete, flagChecker, this.mReason, this.mCancellationElapsedTimeMs);
                    NotificationManagerService.this.updateLightsLocked();
                    if (NotificationManagerService.this.mShortcutHelper != null) {
                        NotificationManagerService.this.mShortcutHelper.maybeListenForShortcutChangesForBubbles(findNotificationLocked, true, NotificationManagerService.this.mHandler);
                    }
                } else {
                    if (NotificationManagerService.this.mNMSWrapper.getNMSExt() == null || !NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldUpdateNavigationMode(this.mPkg, this.mId, this.mCallingUid)) {
                        z = false;
                    }
                    if (z && this.mReason == 8) {
                        NotificationManagerService.this.mNMSWrapper.getNMSExt().setKeepAliveAppIfNeed(this.mPkg, this.mId, false);
                    }
                    if (this.mReason != 18 && NotificationManagerService.this.mSnoozeHelper.cancel(this.mUserId, this.mPkg, this.mTag, this.mId)) {
                        NotificationManagerService.this.handleSavePolicyFile();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$run$0(int i) {
            int i2 = this.mReason;
            if (i2 == 2 || i2 == 1 || i2 == 3) {
                if ((i & 4096) != 0) {
                    return false;
                }
            } else if (i2 == 8 && ((i & 64) != 0 || (32768 & i) != 0)) {
                return false;
            }
            return (this.mMustNotHaveFlags & i) == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ShowNotificationPermissionPromptRunnable implements Runnable {
        private final String mPkgName;
        private final PermissionPolicyInternal mPpi;
        private final int mTaskId;
        private final int mUserId;

        ShowNotificationPermissionPromptRunnable(String str, int i, int i2, PermissionPolicyInternal permissionPolicyInternal) {
            this.mPkgName = str;
            this.mUserId = i;
            this.mTaskId = i2;
            this.mPpi = permissionPolicyInternal;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ShowNotificationPermissionPromptRunnable)) {
                return false;
            }
            ShowNotificationPermissionPromptRunnable showNotificationPermissionPromptRunnable = (ShowNotificationPermissionPromptRunnable) obj;
            return Objects.equals(this.mPkgName, showNotificationPermissionPromptRunnable.mPkgName) && this.mUserId == showNotificationPermissionPromptRunnable.mUserId && this.mTaskId == showNotificationPermissionPromptRunnable.mTaskId;
        }

        public int hashCode() {
            return Objects.hash(this.mPkgName, Integer.valueOf(this.mUserId), Integer.valueOf(this.mTaskId));
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mPpi.showNotificationPromptIfNeeded(this.mPkgName, this.mUserId, this.mTaskId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class EnqueueNotificationRunnable implements Runnable {
        private final boolean isAppForeground;
        private final PostNotificationTracker mTracker;
        private final NotificationRecord r;
        private final int userId;

        EnqueueNotificationRunnable(int i, NotificationRecord notificationRecord, boolean z, PostNotificationTracker postNotificationTracker) {
            this.userId = i;
            this.r = notificationRecord;
            this.isAppForeground = z;
            this.mTracker = (PostNotificationTracker) Preconditions.checkNotNull(postNotificationTracker);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (enqueueNotification()) {
                }
            } finally {
                this.mTracker.cancel();
            }
        }

        private boolean enqueueNotification() {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                long longValue = NotificationManagerService.this.mSnoozeHelper.getSnoozeTimeForUnpostedNotification(this.r.getUser().getIdentifier(), this.r.getSbn().getPackageName(), this.r.getSbn().getKey()).longValue();
                long currentTimeMillis = System.currentTimeMillis();
                if (longValue > currentTimeMillis) {
                    new SnoozeNotificationRunnable(this.r.getSbn().getKey(), longValue - currentTimeMillis, null).snoozeLocked(this.r);
                    return false;
                }
                String snoozeContextForUnpostedNotification = NotificationManagerService.this.mSnoozeHelper.getSnoozeContextForUnpostedNotification(this.r.getUser().getIdentifier(), this.r.getSbn().getPackageName(), this.r.getSbn().getKey());
                if (snoozeContextForUnpostedNotification != null) {
                    new SnoozeNotificationRunnable(this.r.getSbn().getKey(), 0L, snoozeContextForUnpostedNotification).snoozeLocked(this.r);
                    return false;
                }
                NotificationRecord findNotificationByKeyLocked = NotificationManagerService.this.findNotificationByKeyLocked(this.r.getKey());
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().onHandleEnqueuedNotification(this.r, findNotificationByKeyLocked)) {
                    return false;
                }
                NotificationManagerService.this.mEnqueuedNotifications.add(this.r);
                NotificationManagerService.this.scheduleTimeoutLocked(this.r);
                StatusBarNotification sbn = this.r.getSbn();
                if (NotificationManagerService.DBG) {
                    Slog.d(NotificationManagerService.TAG, "EnqueueNotificationRunnable.run for: " + sbn.getKey());
                }
                NotificationRecord notificationRecord = NotificationManagerService.this.mNotificationsByKey.get(sbn.getKey());
                if (notificationRecord != null) {
                    this.r.copyRankingInformation(notificationRecord);
                }
                int uid = sbn.getUid();
                int initialPid = sbn.getInitialPid();
                Notification notification = sbn.getNotification();
                String packageName = sbn.getPackageName();
                int id = sbn.getId();
                String tag = sbn.getTag();
                NotificationManagerService.this.updateNotificationBubbleFlags(this.r, this.isAppForeground);
                NotificationManagerService.this.handleGroupedNotificationLocked(this.r, notificationRecord, uid, initialPid);
                if (sbn.isGroup() && notification.isGroupChild()) {
                    NotificationManagerService.this.mSnoozeHelper.repostGroupSummary(packageName, this.r.getUserId(), sbn.getGroupKey());
                }
                if (!packageName.equals("com.android.providers.downloads") || Log.isLoggable("DownloadManager", 2)) {
                    EventLogTags.writeNotificationEnqueue(uid, initialPid, packageName, id, tag, this.userId, notification.toString(), notificationRecord != null ? 1 : 0);
                }
                if (NotificationManagerService.this.mAssistants.isEnabled()) {
                    NotificationManagerService.this.mAssistants.onNotificationEnqueuedLocked(this.r);
                    NotificationManagerService.this.mHandler.postDelayed(new PostNotificationRunnable(this.r.getKey(), this.r.getSbn().getPackageName(), this.r.getUid(), this.mTracker), NotificationManagerService.DELAY_FOR_ASSISTANT_TIME);
                } else {
                    NotificationManagerService.this.mHandler.post(new PostNotificationRunnable(this.r.getKey(), this.r.getSbn().getPackageName(), this.r.getUid(), this.mTracker));
                }
                return true;
            }
        }
    }

    @GuardedBy({"mNotificationLock"})
    boolean isPackagePausedOrSuspended(String str, int i) {
        return isPackageSuspendedForUser(str, i) | ((((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getDistractingPackageRestrictions(str, Binder.getCallingUserHandle().getIdentifier()) & 2) != 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class PostNotificationRunnable implements Runnable {
        private final String key;
        private final PostNotificationTracker mTracker;
        private final String pkg;
        private final int uid;

        PostNotificationRunnable(String str, String str2, int i, PostNotificationTracker postNotificationTracker) {
            this.key = str;
            this.pkg = str2;
            this.uid = i;
            this.mTracker = (PostNotificationTracker) Preconditions.checkNotNull(postNotificationTracker);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (postNotification()) {
                }
            } finally {
                this.mTracker.cancel();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:101:0x02c5 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:107:0x0261  */
        /* JADX WARN: Removed duplicated region for block: B:108:0x01a0 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0164 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:64:0x0202 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:72:0x0259 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:75:0x0268 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:86:0x0320 A[Catch: all -> 0x036b, TryCatch #1 {all -> 0x036b, blocks: (B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:90:0x0348 A[Catch: all -> 0x0397, TryCatch #0 {, blocks: (B:15:0x005e, B:17:0x0069, B:21:0x007f, B:19:0x0087, B:22:0x008a, B:40:0x00d8, B:42:0x00e3, B:46:0x00f9, B:44:0x0101, B:47:0x0104, B:88:0x033e, B:90:0x0348, B:94:0x035e, B:92:0x0366, B:95:0x0369, B:117:0x036c, B:119:0x0376, B:121:0x038c, B:123:0x038f, B:124:0x0396, B:5:0x001c, B:8:0x0028, B:14:0x0046, B:26:0x008c, B:28:0x0096, B:31:0x00a1, B:35:0x00ab, B:37:0x00b3, B:39:0x00c0, B:50:0x0106, B:52:0x011d, B:53:0x0126, B:55:0x0134, B:58:0x013f, B:59:0x0158, B:61:0x0164, B:62:0x01f1, B:64:0x0202, B:65:0x0206, B:67:0x020c, B:69:0x0218, B:70:0x0239, B:72:0x0259, B:73:0x0262, B:75:0x0268, B:77:0x028b, B:79:0x0291, B:81:0x029f, B:84:0x0318, B:86:0x0320, B:87:0x032f, B:98:0x02ad, B:100:0x02b5, B:101:0x02c5, B:103:0x02dd, B:105:0x02e1, B:106:0x02fd, B:108:0x01a0, B:109:0x014b, B:10:0x0040), top: B:4:0x001c, inners: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:97:0x0369 A[EDGE_INSN: B:97:0x0369->B:95:0x0369 BREAK  A[LOOP:3: B:89:0x0346->B:92:0x0366], SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private boolean postNotification() {
            NotificationRecord notificationRecord;
            int indexOfNotificationLocked;
            int i;
            boolean z;
            int size;
            boolean z2 = !NotificationManagerService.this.areNotificationsEnabledForPackageInt(this.pkg, this.uid);
            boolean isCallNotification = NotificationManagerService.this.isCallNotification(this.pkg, this.uid);
            synchronized (NotificationManagerService.this.mNotificationLock) {
                int i2 = 0;
                try {
                    int size2 = NotificationManagerService.this.mEnqueuedNotifications.size();
                    int i3 = 0;
                    while (true) {
                        if (i3 >= size2) {
                            notificationRecord = null;
                            break;
                        }
                        NotificationRecord notificationRecord2 = NotificationManagerService.this.mEnqueuedNotifications.get(i3);
                        if (Objects.equals(this.key, notificationRecord2.getKey())) {
                            notificationRecord = notificationRecord2;
                            break;
                        }
                        i3++;
                    }
                    if (notificationRecord == null) {
                        Slog.i(NotificationManagerService.TAG, "Cannot find enqueued record for key: " + this.key);
                        int size3 = NotificationManagerService.this.mEnqueuedNotifications.size();
                        int i4 = 0;
                        while (true) {
                            if (i4 >= size3) {
                                break;
                            }
                            if (Objects.equals(this.key, NotificationManagerService.this.mEnqueuedNotifications.get(i4).getKey())) {
                                NotificationManagerService.this.mEnqueuedNotifications.remove(i4);
                                break;
                            }
                            i4++;
                        }
                        return false;
                    }
                    final StatusBarNotification sbn = notificationRecord.getSbn();
                    Notification notification = sbn.getNotification();
                    boolean z3 = isCallNotification && notification.isStyle(Notification.CallStyle.class);
                    if (!notification.isMediaNotification() && !z3 && (z2 || NotificationManagerService.this.isRecordBlockedLocked(notificationRecord))) {
                        NotificationManagerService.this.mUsageStats.registerBlocked(notificationRecord);
                        if (NotificationManagerService.DBG) {
                            Slog.e(NotificationManagerService.TAG, "Suppressing notification from package " + this.pkg);
                        }
                        int size4 = NotificationManagerService.this.mEnqueuedNotifications.size();
                        int i5 = 0;
                        while (true) {
                            if (i5 >= size4) {
                                break;
                            }
                            if (Objects.equals(this.key, NotificationManagerService.this.mEnqueuedNotifications.get(i5).getKey())) {
                                NotificationManagerService.this.mEnqueuedNotifications.remove(i5);
                                break;
                            }
                            i5++;
                        }
                        return false;
                    }
                    boolean isPackagePausedOrSuspended = NotificationManagerService.this.isPackagePausedOrSuspended(notificationRecord.getSbn().getPackageName(), notificationRecord.getUid());
                    notificationRecord.setHidden(isPackagePausedOrSuspended);
                    if (isPackagePausedOrSuspended) {
                        NotificationManagerService.this.mUsageStats.registerSuspendedByAdmin(notificationRecord);
                    }
                    NotificationRecord notificationRecord3 = NotificationManagerService.this.mNotificationsByKey.get(this.key);
                    if (notificationRecord3 != null && notificationRecord3.getSbn().getInstanceId() != null) {
                        sbn.setInstanceId(notificationRecord3.getSbn().getInstanceId());
                        indexOfNotificationLocked = NotificationManagerService.this.indexOfNotificationLocked(sbn.getKey());
                        if (indexOfNotificationLocked >= 0) {
                            NotificationManagerService.this.mNotificationList.add(notificationRecord);
                            NotificationManagerService.this.mUsageStats.registerPostedByApp(notificationRecord);
                            NotificationManagerService.this.mUsageStatsManagerInternal.reportNotificationPosted(notificationRecord.getSbn().getOpPkg(), notificationRecord.getSbn().getUser(), this.mTracker.getStartTime());
                            boolean isVisuallyInterruptive = NotificationManagerService.this.isVisuallyInterruptive(null, notificationRecord);
                            notificationRecord.setInterruptive(isVisuallyInterruptive);
                            notificationRecord.setTextChanged(isVisuallyInterruptive);
                        } else {
                            notificationRecord3 = NotificationManagerService.this.mNotificationList.get(indexOfNotificationLocked);
                            NotificationManagerService.this.mNotificationList.set(indexOfNotificationLocked, notificationRecord);
                            NotificationManagerService.this.mUsageStats.registerUpdatedByApp(notificationRecord, notificationRecord3);
                            NotificationManagerService.this.mUsageStatsManagerInternal.reportNotificationUpdated(notificationRecord.getSbn().getOpPkg(), notificationRecord.getSbn().getUser(), this.mTracker.getStartTime());
                            notification.flags |= notificationRecord3.getNotification().flags & 64;
                            notificationRecord.isUpdate = true;
                            notificationRecord.setTextChanged(NotificationManagerService.this.isVisuallyInterruptive(notificationRecord3, notificationRecord));
                        }
                        NotificationManagerService.this.mNotificationsByKey.put(sbn.getKey(), notificationRecord);
                        i = notification.flags;
                        if ((i & 64) != 0) {
                            notification.flags = i | 32;
                        }
                        if ((notification.flags & 2) != 0 && NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            NotificationManagerService.this.mNMSWrapper.getNMSExt().updateNotification(notificationRecord.getUid(), notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getKey(), true);
                        }
                        NotificationManagerService.this.mRankingHelper.extractSignals(notificationRecord);
                        NotificationManagerService notificationManagerService = NotificationManagerService.this;
                        notificationManagerService.mRankingHelper.sort(notificationManagerService.mNotificationList);
                        NotificationManagerService notificationManagerService2 = NotificationManagerService.this;
                        int indexOf = notificationManagerService2.mRankingHelper.indexOf(notificationManagerService2.mNotificationList, notificationRecord);
                        int buzzBeepBlinkLocked = notificationRecord.isHidden() ? NotificationManagerService.this.buzzBeepBlinkLocked(notificationRecord) : 0;
                        if (notification.getSmallIcon() == null) {
                            NotificationManagerService.this.notifyListenersPostedAndLogLocked(notificationRecord, notificationRecord3, this.mTracker, NotificationManagerService.this.mNotificationRecordLogger.prepareToLogNotificationPosted(notificationRecord, notificationRecord3, indexOf, buzzBeepBlinkLocked, NotificationManagerService.this.getGroupInstanceId(notificationRecord.getSbn().getGroupKey())));
                            StatusBarNotification sbn2 = notificationRecord3 != null ? notificationRecord3.getSbn() : null;
                            if ((sbn2 == null || !Objects.equals(sbn2.getGroup(), sbn.getGroup()) || sbn2.getNotification().flags != sbn.getNotification().flags) && !NotificationManagerService.this.isCritical(notificationRecord)) {
                                NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$PostNotificationRunnable$$ExternalSyntheticLambda0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        NotificationManagerService.PostNotificationRunnable.this.lambda$postNotification$0(sbn);
                                    }
                                });
                            }
                            z = true;
                        } else {
                            Slog.e(NotificationManagerService.TAG, "Not posting notification without small icon: " + notification);
                            if (notificationRecord3 != null && !notificationRecord3.isCanceled) {
                                NotificationManagerService.this.mListeners.notifyRemovedLocked(notificationRecord, 4, notificationRecord.getStats());
                                NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService.PostNotificationRunnable.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        NotificationManagerService.this.mGroupHelper.onNotificationRemoved(sbn);
                                    }
                                });
                            }
                            Slog.e(NotificationManagerService.TAG, "WARNING: In a future release this will crash the app: " + sbn.getPackageName());
                            z = false;
                        }
                        if (NotificationManagerService.this.mShortcutHelper != null) {
                            NotificationManagerService.this.mShortcutHelper.maybeListenForShortcutChangesForBubbles(notificationRecord, false, NotificationManagerService.this.mHandler);
                        }
                        NotificationManagerService.this.maybeRecordInterruptionLocked(notificationRecord);
                        NotificationManagerService.this.maybeRegisterMessageSent(notificationRecord);
                        NotificationManagerService.this.maybeReportForegroundServiceUpdate(notificationRecord, true);
                        size = NotificationManagerService.this.mEnqueuedNotifications.size();
                        while (true) {
                            if (i2 < size) {
                                break;
                            }
                            if (Objects.equals(this.key, NotificationManagerService.this.mEnqueuedNotifications.get(i2).getKey())) {
                                NotificationManagerService.this.mEnqueuedNotifications.remove(i2);
                                break;
                            }
                            i2++;
                        }
                        return z;
                    }
                    sbn.setInstanceId(NotificationManagerService.this.mNotificationInstanceIdSequence.newInstanceId());
                    indexOfNotificationLocked = NotificationManagerService.this.indexOfNotificationLocked(sbn.getKey());
                    if (indexOfNotificationLocked >= 0) {
                    }
                    NotificationManagerService.this.mNotificationsByKey.put(sbn.getKey(), notificationRecord);
                    i = notification.flags;
                    if ((i & 64) != 0) {
                    }
                    if ((notification.flags & 2) != 0) {
                        NotificationManagerService.this.mNMSWrapper.getNMSExt().updateNotification(notificationRecord.getUid(), notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getKey(), true);
                    }
                    NotificationManagerService.this.mRankingHelper.extractSignals(notificationRecord);
                    NotificationManagerService notificationManagerService3 = NotificationManagerService.this;
                    notificationManagerService3.mRankingHelper.sort(notificationManagerService3.mNotificationList);
                    NotificationManagerService notificationManagerService22 = NotificationManagerService.this;
                    int indexOf2 = notificationManagerService22.mRankingHelper.indexOf(notificationManagerService22.mNotificationList, notificationRecord);
                    if (notificationRecord.isHidden()) {
                    }
                    if (notification.getSmallIcon() == null) {
                    }
                    if (NotificationManagerService.this.mShortcutHelper != null) {
                    }
                    NotificationManagerService.this.maybeRecordInterruptionLocked(notificationRecord);
                    NotificationManagerService.this.maybeRegisterMessageSent(notificationRecord);
                    NotificationManagerService.this.maybeReportForegroundServiceUpdate(notificationRecord, true);
                    size = NotificationManagerService.this.mEnqueuedNotifications.size();
                    while (true) {
                        if (i2 < size) {
                        }
                        i2++;
                    }
                    return z;
                } catch (Throwable th) {
                    int size5 = NotificationManagerService.this.mEnqueuedNotifications.size();
                    while (true) {
                        if (i2 >= size5) {
                            break;
                        }
                        if (Objects.equals(this.key, NotificationManagerService.this.mEnqueuedNotifications.get(i2).getKey())) {
                            NotificationManagerService.this.mEnqueuedNotifications.remove(i2);
                            break;
                        }
                        i2++;
                    }
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postNotification$0(StatusBarNotification statusBarNotification) {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                NotificationManagerService.this.mGroupHelper.onNotificationPosted(statusBarNotification, NotificationManagerService.this.hasAutoGroupSummaryLocked(statusBarNotification));
            }
        }
    }

    @GuardedBy({"mNotificationLock"})
    InstanceId getGroupInstanceId(String str) {
        NotificationRecord notificationRecord;
        if (str == null || (notificationRecord = this.mSummaryByGroupKey.get(str)) == null) {
            return null;
        }
        return notificationRecord.getSbn().getInstanceId();
    }

    @GuardedBy({"mNotificationLock"})
    @VisibleForTesting
    protected boolean isVisuallyInterruptive(NotificationRecord notificationRecord, NotificationRecord notificationRecord2) {
        Notification.Builder recoverBuilder;
        Notification.Builder recoverBuilder2;
        if (notificationRecord2.getSbn().isGroup() && notificationRecord2.getSbn().getNotification().isGroupSummary()) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is not interruptive: summary");
            }
            return false;
        }
        if (notificationRecord == null) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: new notification");
            }
            return true;
        }
        Notification notification = notificationRecord.getSbn().getNotification();
        Notification notification2 = notificationRecord2.getSbn().getNotification();
        if (notification.extras == null || notification2.extras == null) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is not interruptive: no extras");
            }
            return false;
        }
        if ((notificationRecord2.getSbn().getNotification().flags & 64) != 0) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is not interruptive: foreground service");
            }
            return false;
        }
        String valueOf = String.valueOf(notification.extras.get("android.title"));
        String valueOf2 = String.valueOf(notification2.extras.get("android.title"));
        if (!valueOf.equals(valueOf2)) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: changed title");
                StringBuilder sb = new StringBuilder();
                sb.append("INTERRUPTIVENESS: ");
                sb.append(String.format("   old title: %s (%s@0x%08x)", valueOf, valueOf.getClass(), Integer.valueOf(valueOf.hashCode())));
                Slog.v(TAG, sb.toString());
                Slog.v(TAG, "INTERRUPTIVENESS: " + String.format("   new title: %s (%s@0x%08x)", valueOf2, valueOf2.getClass(), Integer.valueOf(valueOf2.hashCode())));
            }
            return true;
        }
        String valueOf3 = String.valueOf(notification.extras.get("android.text"));
        String valueOf4 = String.valueOf(notification2.extras.get("android.text"));
        if (!valueOf3.equals(valueOf4)) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: changed text");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("INTERRUPTIVENESS: ");
                sb2.append(String.format("   old text: %s (%s@0x%08x)", valueOf3, valueOf3.getClass(), Integer.valueOf(valueOf3.hashCode())));
                Slog.v(TAG, sb2.toString());
                Slog.v(TAG, "INTERRUPTIVENESS: " + String.format("   new text: %s (%s@0x%08x)", valueOf4, valueOf4.getClass(), Integer.valueOf(valueOf4.hashCode())));
            }
            return true;
        }
        if (notification.hasCompletedProgress() != notification2.hasCompletedProgress()) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: completed progress");
            }
            return true;
        }
        if (Notification.areIconsDifferent(notification, notification2)) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: icons differ");
            }
            return true;
        }
        if (notificationRecord2.canBubble()) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is not interruptive: bubble");
            }
            return false;
        }
        if (Notification.areActionsVisiblyDifferent(notification, notification2)) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: changed actions");
            }
            return true;
        }
        try {
            recoverBuilder = Notification.Builder.recoverBuilder(getContext(), notification);
            recoverBuilder2 = Notification.Builder.recoverBuilder(getContext(), notification2);
        } catch (Exception e) {
            Slog.w(TAG, "error recovering builder", e);
        }
        if (Notification.areStyledNotificationsVisiblyDifferent(recoverBuilder, recoverBuilder2)) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: styles differ");
            }
            return true;
        }
        if (Notification.areRemoteViewsChanged(recoverBuilder, recoverBuilder2)) {
            if (DEBUG_INTERRUPTIVENESS) {
                Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord2.getKey() + " is interruptive: remoteviews differ");
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCritical(NotificationRecord notificationRecord) {
        return notificationRecord.getCriticality() < 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void handleGroupedNotificationLocked(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, int i, int i2) {
        NotificationRecord remove;
        StatusBarNotification sbn = notificationRecord.getSbn();
        Notification notification = sbn.getNotification();
        if (notification.isGroupSummary() && !sbn.isAppGroup()) {
            notification.flags &= -513;
        }
        String groupKey = sbn.getGroupKey();
        boolean isGroupSummary = notification.isGroupSummary();
        Notification notification2 = notificationRecord2 != null ? notificationRecord2.getSbn().getNotification() : null;
        String groupKey2 = notificationRecord2 != null ? notificationRecord2.getSbn().getGroupKey() : null;
        boolean z = notificationRecord2 != null && notification2.isGroupSummary();
        if (z && (remove = this.mSummaryByGroupKey.remove(groupKey2)) != notificationRecord2) {
            Slog.w(TAG, "Removed summary didn't match old notification: old=" + notificationRecord2.getKey() + ", removed=" + (remove != null ? remove.getKey() : "<null>"));
        }
        if (isGroupSummary) {
            this.mSummaryByGroupKey.put(groupKey, notificationRecord);
        }
        FlagChecker flagChecker = new FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda7
            @Override // com.android.server.notification.NotificationManagerService.FlagChecker
            public final boolean apply(int i3) {
                boolean lambda$handleGroupedNotificationLocked$9;
                lambda$handleGroupedNotificationLocked$9 = NotificationManagerService.lambda$handleGroupedNotificationLocked$9(i3);
                return lambda$handleGroupedNotificationLocked$9;
            }
        };
        if (z) {
            if (isGroupSummary && groupKey2.equals(groupKey)) {
                return;
            }
            cancelGroupChildrenLocked(notificationRecord2, i, i2, null, false, flagChecker, 8, SystemClock.elapsedRealtime());
        }
    }

    @GuardedBy({"mNotificationLock"})
    @VisibleForTesting
    void scheduleTimeoutLocked(NotificationRecord notificationRecord) {
        if (notificationRecord.getNotification().getTimeoutAfter() > 0) {
            this.mAlarmManager.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + notificationRecord.getNotification().getTimeoutAfter(), PendingIntent.getBroadcast(getContext(), 1, new Intent(ACTION_NOTIFICATION_TIMEOUT).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME).setData(new Uri.Builder().scheme(SCHEME_TIMEOUT).appendPath(notificationRecord.getKey()).build()).addFlags(268435456).putExtra(EXTRA_KEY, notificationRecord.getKey()), 201326592));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:128:0x0363, code lost:
    
        if (android.provider.Settings.System.getIntForUser(getContext().getContentResolver(), "vibrate_when_ringing", 0, -2) != 0) goto L177;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [int] */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r11v11 */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r11v3, types: [int] */
    /* JADX WARN: Type inference failed for: r11v4 */
    /* JADX WARN: Type inference failed for: r11v5 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4 */
    /* JADX WARN: Type inference failed for: r13v5 */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r2v38 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5, types: [int] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    @GuardedBy({"mNotificationLock"})
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    int buzzBeepBlinkLocked(NotificationRecord notificationRecord) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        char c;
        NotificationRecord notificationRecord2;
        boolean z7;
        String str;
        ?? r2;
        boolean z8;
        ?? r11;
        boolean z9;
        ?? r0;
        boolean z10;
        LogicalLight logicalLight;
        boolean z11;
        boolean z12;
        boolean z13;
        boolean z14;
        boolean z15;
        String str2;
        boolean z16;
        boolean z17;
        boolean z18;
        if (this.mIsAutomotive && !this.mNotificationEffectsEnabledForAutomotive) {
            return 0;
        }
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().shouldBeReplacedByFluidCard(notificationRecord)) {
            if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.v(TAG, "buzzBeepBlinkLocked, should be replaced by fluid card");
            }
            return 0;
        }
        String key = notificationRecord.getKey();
        boolean z19 = !this.mIsAutomotive ? notificationRecord.getImportance() < 3 : notificationRecord.getImportance() <= 3;
        boolean z20 = z19 && (!notificationRecord.isIntercepted() || this.mZenModeHelper.getZenMode() == 3);
        if (this.mNMSWrapper.getNMSExt() != null) {
            z = this.mNMSWrapper.getNMSExt().shouldSuppressEffect(notificationRecord);
            if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.v(TAG, "buzzBeepBlinkLocked--aboveThreshold:" + z19 + ",canInterruptLight:" + z20 + ",suppressEffect:" + z + ",isCurrentUser:" + isNotificationForCurrentUser(notificationRecord));
            }
        } else {
            z = false;
        }
        boolean z21 = key != null && key.equals(this.mSoundNotificationKey);
        boolean z22 = key != null && key.equals(this.mVibrateNotificationKey);
        boolean z23 = notificationRecord.isIntercepted() && (notificationRecord.getSuppressedVisualEffects() & 32) != 0;
        if (notificationRecord.isUpdate || notificationRecord.getImportance() <= 1 || z23 || !isNotificationForCurrentUser(notificationRecord)) {
            z2 = false;
        } else {
            sendAccessibilityEvent(notificationRecord);
            z2 = true;
        }
        if (z19 && isNotificationForCurrentUser(notificationRecord) && this.mSystemReady && this.mAudioManager != null) {
            Uri sound = notificationRecord.getSound();
            z8 = (sound == null || Uri.EMPTY.equals(sound)) ? false : true;
            VibrationEffect vibration = notificationRecord.getVibration();
            if (vibration == null && z8 && this.mAudioManager.getRingerModeInternal() == 1 && this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(notificationRecord.getAudioAttributes())) == 0) {
                vibration = this.mVibratorHelper.createFallbackVibration((notificationRecord.getFlags() & 4) != 0);
            }
            VibrationEffect vibrationEffect = vibration;
            z9 = vibrationEffect != null;
            boolean z24 = z8 || z9;
            boolean shouldBlockNotifSound = this.mNMSWrapper.getZenModeManagerExt() != null ? this.mNMSWrapper.getZenModeManagerExt().shouldBlockNotifSound(notificationRecord.getSbn().getPackageName()) : false;
            if (this.mNMSWrapper.getNMSExt() != null) {
                z3 = z19;
                z11 = this.mNMSWrapper.getNMSExt().isAppBlockedByWLB(notificationRecord, this.mPreferencesHelper);
            } else {
                z3 = z19;
                z11 = false;
            }
            if (z24 && !shouldMuteNotificationLocked(notificationRecord) && !shouldBlockNotifSound && !z11) {
                if (!z2) {
                    sendAccessibilityEvent(notificationRecord);
                }
                if (this.mNMSWrapper.getNMSExt() != null) {
                    z12 = this.mNMSWrapper.getNMSExt().isAppRingtonePermissionGrantedInner(notificationRecord.getSbn().getPackageName(), notificationRecord.getUid());
                    z13 = this.mNMSWrapper.getNMSExt().isAppVibrationPermissionGrantedInner(notificationRecord.getSbn().getPackageName(), notificationRecord.getUid());
                    if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                        Slog.v(TAG, "Is ringtone permission granted: " + z12 + "; Is vibration permission granted: " + z13);
                    }
                } else {
                    z12 = false;
                    z13 = false;
                }
                boolean z25 = (this.mNMSWrapper.getNMSExt() == null || !this.mNMSWrapper.getNMSExt().isStudyCenterActivated() || this.mNMSWrapper.getNMSExt().getStudyCenterWhiteLists().contains(notificationRecord.getSbn().getPackageName())) ? false : true;
                if (this.mNMSWrapper.getNMSExt() == null || !this.mNMSWrapper.getNMSExt().isLoggable()) {
                    z4 = z20;
                } else {
                    StringBuilder sb = new StringBuilder();
                    z4 = z20;
                    sb.append("suppressedBySC: ");
                    sb.append(z25);
                    Slog.v(TAG, sb.toString());
                }
                if (DBG) {
                    Slog.v(TAG, "Interrupting!");
                }
                boolean isInsistentUpdate = isInsistentUpdate(notificationRecord);
                if (!z8 || z || !z12 || z25) {
                    z5 = z21;
                    z6 = z22;
                    z14 = false;
                    z15 = false;
                } else if (isInsistentUpdate) {
                    z5 = z21;
                    z6 = z22;
                    z14 = false;
                    z15 = true;
                } else {
                    if (isInCall()) {
                        playInCallNotification();
                        z5 = z21;
                        z6 = z22;
                        z14 = false;
                        z15 = true;
                    } else {
                        if (this.mNMSWrapper.getNMSExt() != null) {
                            if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                                StringBuilder sb2 = new StringBuilder();
                                z6 = z22;
                                sb2.append("In buzzBeepBlinkLocked(): ringtone uri from NotificationRecord = ");
                                sb2.append(sound);
                                Slog.v(TAG, sb2.toString());
                            } else {
                                z6 = z22;
                            }
                            sound = this.mNMSWrapper.getNMSExt().determineFinalSoundUri(sound);
                            z5 = z21;
                            this.mNMSWrapper.getNMSExt().setVibrationType(notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getNotification().extras);
                            if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                                Slog.v(TAG, "Final ringtone uri = " + sound);
                            }
                        } else {
                            z5 = z21;
                            z6 = z22;
                        }
                        if (this.mNMSWrapper.getNMSExt() != null) {
                            z14 = this.mNMSWrapper.getNMSExt().shouldUseHapticFeature(this.mAudioManager, notificationRecord);
                            if (!isInCall() && z14 && z9) {
                                this.mNMSWrapper.getNMSExt().setHapticExtrasForNotification(notificationRecord);
                                Slog.d(TAG, "buzzBeepBlinkLocked(): vibrate through haptic channel.");
                            }
                        } else {
                            z14 = false;
                        }
                        z15 = playSound(notificationRecord, sound);
                    }
                    if (z15) {
                        this.mSoundNotificationKey = key;
                    }
                }
                int ringerModeInternal = this.mAudioManager.getRingerModeInternal();
                boolean z26 = z15;
                if (ringerModeInternal != 1) {
                    if (ringerModeInternal == 2) {
                        str2 = key;
                        z7 = false;
                    } else {
                        str2 = key;
                        z7 = false;
                    }
                    z16 = z7;
                    if (!isInCall() || !z9 || !z16 || z || !z13 || z14 || z25) {
                        notificationRecord2 = notificationRecord;
                        str = str2;
                        z17 = z7;
                    } else if (isInsistentUpdate) {
                        notificationRecord2 = notificationRecord;
                        str = str2;
                        z17 = true;
                    } else {
                        if (this.mNMSWrapper.getNMSExt() != null) {
                            Slog.d(TAG, "vibrateLinearmotorIfNeed, " + sound);
                            this.mNMSWrapper.getNMSExt().setVibrationType(notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getNotification().extras);
                            VibrationEffect determineFinalVibrate = this.mNMSWrapper.getNMSExt().determineFinalVibrate(vibrationEffect);
                            boolean vibrateLinearmotorIfNeed = this.mNMSWrapper.getNMSExt().vibrateLinearmotorIfNeed(z8, sound, z12);
                            notificationRecord2 = notificationRecord;
                            z18 = (vibrateLinearmotorIfNeed || playVibration(notificationRecord2, determineFinalVibrate, z8)) ? true : z7;
                        } else {
                            notificationRecord2 = notificationRecord;
                            z18 = playVibration(notificationRecord2, vibrationEffect, z8);
                        }
                        str = str2;
                        z17 = z18;
                        if (z18) {
                            this.mVibrateNotificationKey = str;
                            z17 = z18;
                        }
                    }
                    this.mAccessibilityManager.startFlashNotificationEvent(getContext(), 3, notificationRecord.getSbn().getPackageName());
                    r11 = z26;
                    c = 4;
                    r2 = z17;
                } else {
                    str2 = key;
                    z7 = false;
                }
                z16 = true;
                if (!isInCall()) {
                }
                notificationRecord2 = notificationRecord;
                str = str2;
                z17 = z7;
                this.mAccessibilityManager.startFlashNotificationEvent(getContext(), 3, notificationRecord.getSbn().getPackageName());
                r11 = z26;
                c = 4;
                r2 = z17;
            } else {
                notificationRecord2 = notificationRecord;
                str = key;
                z4 = z20;
                z5 = z21;
                z6 = z22;
                z7 = false;
                c = 4;
                if ((notificationRecord.getFlags() & 4) != 0) {
                    r2 = 0;
                    z8 = false;
                    r11 = 0;
                } else {
                    r2 = 0;
                    r11 = 0;
                }
            }
        } else {
            z3 = z19;
            z4 = z20;
            z5 = z21;
            z6 = z22;
            c = 4;
            notificationRecord2 = notificationRecord;
            z7 = false;
            str = key;
            r2 = 0;
            z8 = false;
            r11 = 0;
            z9 = false;
        }
        if (z5 && !z8) {
            if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.v(TAG, "clearsound due to notification updated to remove the activelyplaying sound.");
            }
            clearSoundLocked();
        }
        if (z6 && !z9) {
            clearVibrateLocked();
        }
        boolean remove = this.mLights.remove(str);
        if (z4 && !z && canShowLightsLocked(notificationRecord2, z3)) {
            this.mLights.add(str);
            updateLightsLocked();
            if (this.mUseAttentionLight && (logicalLight = this.mAttentionLight) != null) {
                logicalLight.pulse();
            }
            r0 = 1;
        } else {
            if (remove) {
                updateLightsLocked();
            }
            r0 = z7;
        }
        int i = r2 | (r11 != 0 ? 2 : z7) | (r0 != 0 ? c : z7);
        if (i > 0) {
            if (notificationRecord.getSbn().isGroup() && notificationRecord.getSbn().getNotification().isGroupSummary()) {
                if (DEBUG_INTERRUPTIVENESS) {
                    Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord.getKey() + " is not interruptive: summary");
                }
            } else if (notificationRecord.canBubble()) {
                if (DEBUG_INTERRUPTIVENESS) {
                    Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord.getKey() + " is not interruptive: bubble");
                }
            } else {
                notificationRecord2.setInterruptive(true);
                if (DEBUG_INTERRUPTIVENESS) {
                    Slog.v(TAG, "INTERRUPTIVENESS: " + notificationRecord.getKey() + " is interruptive: alerted");
                }
            }
            z10 = true;
            MetricsLogger.action(notificationRecord.getLogMaker().setCategory(199).setType(1).setSubtype(i));
            EventLogTags.writeNotificationAlert(str, (int) r2, (int) r11, (int) r0);
        } else {
            z10 = true;
        }
        notificationRecord2.setAudiblyAlerted((r2 == 0 && r11 == 0) ? z7 : z10);
        return i;
    }

    @GuardedBy({"mNotificationLock"})
    boolean canShowLightsLocked(NotificationRecord notificationRecord, boolean z) {
        if (this.mNMSWrapper.getNMSExt() != null && !this.mNMSWrapper.getNMSExt().hasCustomizeBreathLight()) {
            return false;
        }
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().canShowLightsLocked(notificationRecord)) {
            if (this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.d(TAG, "mNMSExt#canShowLightsLocked returns true.");
            }
            return true;
        }
        if (!this.mHasLight || !this.mNotificationPulseEnabled || notificationRecord.getLight() == null || !z || (notificationRecord.getSuppressedVisualEffects() & 8) != 0) {
            return false;
        }
        Notification notification = notificationRecord.getNotification();
        if (!notificationRecord.isUpdate || (notification.flags & 8) == 0) {
            return ((notificationRecord.getSbn().isGroup() && notificationRecord.getNotification().suppressAlertingDueToGrouping()) || isInCall() || !isNotificationForCurrentUser(notificationRecord)) ? false : true;
        }
        return false;
    }

    @GuardedBy({"mNotificationLock"})
    boolean isInsistentUpdate(NotificationRecord notificationRecord) {
        return (Objects.equals(notificationRecord.getKey(), this.mSoundNotificationKey) || Objects.equals(notificationRecord.getKey(), this.mVibrateNotificationKey)) && isCurrentlyInsistent();
    }

    @GuardedBy({"mNotificationLock"})
    boolean isCurrentlyInsistent() {
        return isLoopingRingtoneNotification(this.mNotificationsByKey.get(this.mSoundNotificationKey)) || isLoopingRingtoneNotification(this.mNotificationsByKey.get(this.mVibrateNotificationKey));
    }

    @GuardedBy({"mNotificationLock"})
    boolean shouldMuteNotificationLocked(NotificationRecord notificationRecord) {
        Notification notification = notificationRecord.getNotification();
        if ((notificationRecord.isUpdate && (notification.flags & 8) != 0) || notificationRecord.shouldPostSilently()) {
            return true;
        }
        String disableNotificationEffects = disableNotificationEffects(notificationRecord);
        if (disableNotificationEffects != null) {
            if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.v(TAG, "buzzBeepBlinkLocked--shouldMuteNotificationLocked disableEffects:" + disableNotificationEffects);
            }
            ZenLog.traceDisableEffects(notificationRecord, disableNotificationEffects);
            return true;
        }
        if (notificationRecord.isIntercepted()) {
            if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.v(TAG, "buzzBeepBlinkLocked--shouldMuteNotificationLocked suppressed due to DND");
            }
            return true;
        }
        if (notificationRecord.getSbn().isGroup() && notification.suppressAlertingDueToGrouping()) {
            return true;
        }
        if (this.mUsageStats.isAlertRateLimited(notificationRecord.getSbn().getPackageName())) {
            Slog.e(TAG, "Muting recently noisy " + notificationRecord.getKey());
            return true;
        }
        if (!isCurrentlyInsistent() || isInsistentUpdate(notificationRecord)) {
            return notificationRecord.isUpdate && !notificationRecord.isInterruptive() && (notificationRecord.canBubble() && (notificationRecord.isFlagBubbleRemoved() || notificationRecord.getNotification().isBubbleNotification())) && notificationRecord.getNotification().getBubbleMetadata() != null && notificationRecord.getNotification().getBubbleMetadata().isNotificationSuppressed();
        }
        return true;
    }

    @GuardedBy({"mNotificationLock"})
    private boolean isLoopingRingtoneNotification(NotificationRecord notificationRecord) {
        return (notificationRecord == null || notificationRecord.getAudioAttributes().getUsage() != 6 || (notificationRecord.getNotification().flags & 4) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean playSound(NotificationRecord notificationRecord, Uri uri) {
        IRingtonePlayer ringtonePlayer;
        boolean z = (notificationRecord.getNotification().flags & 4) != 0;
        if (!this.mAudioManager.isAudioFocusExclusive() && (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(notificationRecord.getAudioAttributes())) != 0 || (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isVibrationRingMuteSupport()))) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    ringtonePlayer = this.mAudioManager.getRingtonePlayer();
                } catch (RemoteException unused) {
                    Slog.v(TAG, "in playAsync(): RemoteException occured.");
                }
                if (ringtonePlayer != null) {
                    if (DBG) {
                        Slog.v(TAG, "Playing sound " + uri + " with attributes " + notificationRecord.getAudioAttributes());
                    }
                    if (this.mNMSWrapper.getNMSExt() != null && !this.mNMSWrapper.getNMSExt().playAsync(ringtonePlayer, notificationRecord, uri, z)) {
                        ringtonePlayer.playAsync(uri, notificationRecord.getSbn().getUser(), z, notificationRecord.getAudioAttributes());
                    }
                    return true;
                }
                Slog.v(TAG, "Ringtone player is null.");
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return false;
    }

    private int[] calculateAmplitude(VibrationEffect vibrationEffect, int[] iArr) {
        int i;
        if (iArr == null || (i = this.mNotificationVibrationIntensity) < 800 || i > END_NOTIFICATION_VIBRATION_STRENGTH) {
            return null;
        }
        int i2 = ((i - 800) * 255) / 1600;
        if (i2 < 1) {
            i2 = 1;
        }
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (iArr[i3] == -1) {
                iArr[i3] = i2;
            }
        }
        Slog.d(TAG, "new amplitude=" + Arrays.toString(iArr));
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean playVibration(final NotificationRecord notificationRecord, final VibrationEffect vibrationEffect, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (z) {
                new Thread(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationManagerService.this.lambda$playVibration$10(notificationRecord, vibrationEffect);
                    }
                }).start();
            } else {
                vibrate(notificationRecord, vibrationEffect, false);
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return true;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playVibration$10(NotificationRecord notificationRecord, VibrationEffect vibrationEffect) {
        int focusRampTimeMs = this.mAudioManager.getFocusRampTimeMs(3, notificationRecord.getAudioAttributes());
        if (DBG) {
            Slog.v(TAG, "Delaying vibration for notification " + notificationRecord.getKey() + " by " + focusRampTimeMs + "ms");
        }
        try {
            Thread.sleep(focusRampTimeMs);
        } catch (InterruptedException unused) {
        }
        synchronized (this.mNotificationLock) {
            if (this.mNotificationsByKey.get(notificationRecord.getKey()) != null) {
                if (notificationRecord.getKey().equals(this.mVibrateNotificationKey)) {
                    vibrate(notificationRecord, vibrationEffect, true);
                } else if (DBG) {
                    Slog.v(TAG, "No vibration for notification " + notificationRecord.getKey() + ": a new notification is vibrating, or effects were cleared while waiting");
                }
            } else {
                Slog.w(TAG, "No vibration for canceled notification " + notificationRecord.getKey());
            }
        }
    }

    private void vibrate(NotificationRecord notificationRecord, VibrationEffect vibrationEffect, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("Notification (");
        sb.append(notificationRecord.getSbn().getOpPkg());
        sb.append(" ");
        sb.append(notificationRecord.getSbn().getUid());
        sb.append(") ");
        sb.append(z ? "(Delayed)" : "");
        this.mVibratorHelper.vibrate(vibrationEffect, notificationRecord.getAudioAttributes(), sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNotificationForCurrentUser(NotificationRecord notificationRecord) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int currentUser = ActivityManager.getCurrentUser();
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isNotificationForCurrentUser(notificationRecord, notificationRecord.getUserId())) || notificationRecord.getUserId() == -1 || notificationRecord.getUserId() == currentUser || this.mUserProfiles.isCurrentProfile(notificationRecord.getUserId());
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    protected void playInCallNotification() {
        ContentResolver contentResolver = getContext().getContentResolver();
        if (this.mAudioManager.getRingerModeInternal() != 2 || Settings.Secure.getIntForUser(contentResolver, "in_call_notification_enabled", 1, contentResolver.getUserId()) == 0) {
            return;
        }
        new Thread() { // from class: com.android.server.notification.NotificationManagerService.14
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    IRingtonePlayer ringtonePlayer = NotificationManagerService.this.mAudioManager.getRingtonePlayer();
                    if (ringtonePlayer != null) {
                        if (NotificationManagerService.this.mCallNotificationToken != null) {
                            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                                Slog.v(NotificationManagerService.TAG, "stop player to play in call notification sound.");
                            }
                            ringtonePlayer.stop(NotificationManagerService.this.mCallNotificationToken);
                        }
                        NotificationManagerService.this.mCallNotificationToken = new Binder();
                        ringtonePlayer.play(NotificationManagerService.this.mCallNotificationToken, NotificationManagerService.this.mInCallNotificationUri, NotificationManagerService.this.mInCallNotificationAudioAttributes, NotificationManagerService.this.mInCallNotificationVolume, false);
                    }
                } catch (RemoteException unused) {
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }.start();
    }

    @GuardedBy({"mToastQueue"})
    void showNextToastLocked(boolean z) {
        if (this.mIsCurrentToastShown) {
            return;
        }
        ToastRecord toastRecord = this.mToastQueue.get(0);
        while (toastRecord != null) {
            int userId = UserHandle.getUserId(toastRecord.uid);
            boolean z2 = !this.mToastRateLimitingDisabledUids.contains(Integer.valueOf(toastRecord.uid));
            boolean z3 = this.mToastRateLimiter.isWithinQuota(userId, toastRecord.pkg, TOAST_QUOTA_TAG) || isExemptFromRateLimiting(toastRecord.pkg, userId);
            boolean isPackageInForegroundForToast = isPackageInForegroundForToast(toastRecord.uid);
            if (tryShowToast(toastRecord, z2, z3, isPackageInForegroundForToast)) {
                scheduleDurationReachedLocked(toastRecord, z);
                this.mIsCurrentToastShown = true;
                if (this.mNMSWrapper.getNMSExt() != null) {
                    this.mNMSWrapper.getNMSExt().setCurrentShowTime(System.currentTimeMillis());
                }
                if (!z2 || isPackageInForegroundForToast) {
                    return;
                }
                this.mToastRateLimiter.noteEvent(userId, toastRecord.pkg, TOAST_QUOTA_TAG);
                return;
            }
            int indexOf = this.mToastQueue.indexOf(toastRecord);
            if (indexOf >= 0) {
                ToastRecord remove = this.mToastQueue.remove(indexOf);
                this.mWindowManagerInternal.removeWindowToken(remove.windowToken, true, remove.displayId);
            }
            toastRecord = this.mToastQueue.size() > 0 ? this.mToastQueue.get(0) : null;
        }
    }

    private boolean tryShowToast(ToastRecord toastRecord, boolean z, boolean z2, boolean z3) {
        if (z && !z2 && !z3) {
            reportCompatRateLimitingToastsChange(toastRecord.uid);
            Slog.w(TAG, "Package " + toastRecord.pkg + " is above allowed toast quota, the following toast was blocked and discarded: " + toastRecord);
            return false;
        }
        if (blockToast(toastRecord.uid, toastRecord.isSystemToast, toastRecord.isAppRendered(), z3)) {
            Slog.w(TAG, "Blocking custom toast from package " + toastRecord.pkg + " due to package not in the foreground at the time of showing the toast");
            return false;
        }
        return toastRecord.show();
    }

    private boolean isExemptFromRateLimiting(String str, int i) {
        try {
            return this.mPackageManager.checkPermission("android.permission.UNLIMITED_TOASTS", str, i) == 0;
        } catch (RemoteException unused) {
            Slog.e(TAG, "Failed to connect with package manager");
            return false;
        }
    }

    private void reportCompatRateLimitingToastsChange(int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mPlatformCompat.reportChangeByUid(RATE_LIMIT_TOASTS, i);
            } catch (RemoteException e) {
                Slog.e(TAG, "Unexpected exception while reporting toast was blocked due to rate limiting", e);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mToastQueue"})
    void cancelToastLocked(int i) {
        ToastRecord toastRecord = this.mToastQueue.get(i);
        toastRecord.hide();
        if (i == 0) {
            this.mIsCurrentToastShown = false;
        }
        ToastRecord remove = this.mToastQueue.remove(i);
        scheduleKillTokenTimeout(remove);
        keepProcessAliveForToastIfNeededLocked(toastRecord.pid);
        if (this.mToastQueue.size() > 0) {
            showNextToastLocked(remove instanceof TextToastRecord);
        }
    }

    void finishWindowTokenLocked(IBinder iBinder, int i) {
        this.mHandler.removeCallbacksAndMessages(iBinder);
        this.mWindowManagerInternal.removeWindowToken(iBinder, true, i);
    }

    @GuardedBy({"mToastQueue"})
    private void scheduleDurationReachedLocked(ToastRecord toastRecord, boolean z) {
        this.mHandler.removeCallbacksAndMessages(toastRecord);
        Message obtain = Message.obtain(this.mHandler, 2, toastRecord);
        int recommendedTimeoutMillis = this.mAccessibilityManager.getRecommendedTimeoutMillis(toastRecord.getDuration() == 1 ? LONG_DELAY : 2000, 2);
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().shouldContinuousShowToast(this.mToastQueue)) {
            recommendedTimeoutMillis = 100;
        }
        if (z) {
            recommendedTimeoutMillis += 250;
        }
        if (toastRecord instanceof TextToastRecord) {
            recommendedTimeoutMillis += 333;
        }
        this.mHandler.sendMessageDelayed(obtain, recommendedTimeoutMillis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDurationReached(ToastRecord toastRecord) {
        if (DBG) {
            Slog.d(TAG, "Timeout pkg=" + toastRecord.pkg + " token=" + toastRecord.token);
        }
        synchronized (this.mToastQueue) {
            int indexOfToastLocked = indexOfToastLocked(toastRecord.pkg, toastRecord.token);
            if (indexOfToastLocked >= 0) {
                cancelToastLocked(indexOfToastLocked);
            }
        }
    }

    @GuardedBy({"mToastQueue"})
    private void scheduleKillTokenTimeout(ToastRecord toastRecord) {
        this.mHandler.removeCallbacksAndMessages(toastRecord);
        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 7, toastRecord), 11000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKillTokenTimeout(ToastRecord toastRecord) {
        if (DBG) {
            Slog.d(TAG, "Kill Token Timeout token=" + toastRecord.windowToken);
        }
        synchronized (this.mToastQueue) {
            finishWindowTokenLocked(toastRecord.windowToken, toastRecord.displayId);
        }
    }

    @GuardedBy({"mToastQueue"})
    int indexOfToastLocked(String str, IBinder iBinder) {
        ArrayList<ToastRecord> arrayList = this.mToastQueue;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ToastRecord toastRecord = arrayList.get(i);
            if (toastRecord.pkg.equals(str) && toastRecord.token == iBinder) {
                return i;
            }
        }
        return -1;
    }

    public void keepProcessAliveForToastIfNeeded(int i) {
        synchronized (this.mToastQueue) {
            keepProcessAliveForToastIfNeededLocked(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mToastQueue"})
    public void keepProcessAliveForToastIfNeededLocked(int i) {
        ArrayList<ToastRecord> arrayList = this.mToastQueue;
        int size = arrayList.size();
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            ToastRecord toastRecord = arrayList.get(i3);
            if (toastRecord.pid == i && toastRecord.keepProcessAlive()) {
                i2++;
            }
        }
        try {
            this.mAm.setProcessImportant(this.mForegroundToken, i, i2 > 0, "toast");
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPackageInForegroundForToast(int i) {
        return this.mAtm.hasResumedActivity(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean blockToast(int i, boolean z, boolean z2, boolean z3) {
        return z2 && !z && !z3 && CompatChanges.isChangeEnabled(CHANGE_BACKGROUND_CUSTOM_TOAST_BLOCK, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0078, code lost:
    
        if (r1.isIntercepted() != false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0082, code lost:
    
        if (r1.isNewEnoughForAlerting(java.lang.System.currentTimeMillis()) == false) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0084, code lost:
    
        buzzBeepBlinkLocked(r1);
        com.android.server.notification.ZenLog.traceAlertOnUpdatedIntercept(r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleRankingReconsideration(Message message) {
        Object obj = message.obj;
        if (obj instanceof RankingReconsideration) {
            RankingReconsideration rankingReconsideration = (RankingReconsideration) obj;
            rankingReconsideration.run();
            synchronized (this.mNotificationLock) {
                NotificationRecord notificationRecord = this.mNotificationsByKey.get(rankingReconsideration.getKey());
                if (notificationRecord == null) {
                    return;
                }
                int findNotificationRecordIndexLocked = findNotificationRecordIndexLocked(notificationRecord);
                boolean isIntercepted = notificationRecord.isIntercepted();
                int packageVisibilityOverride = notificationRecord.getPackageVisibilityOverride();
                boolean isInterruptive = notificationRecord.isInterruptive();
                rankingReconsideration.applyChangesLocked(notificationRecord);
                applyZenModeLocked(notificationRecord);
                this.mRankingHelper.sort(this.mNotificationList);
                boolean z = true;
                boolean z2 = findNotificationRecordIndexLocked != findNotificationRecordIndexLocked(notificationRecord);
                boolean z3 = isIntercepted != notificationRecord.isIntercepted();
                boolean z4 = packageVisibilityOverride != notificationRecord.getPackageVisibilityOverride();
                boolean z5 = notificationRecord.canBubble() && isInterruptive != notificationRecord.isInterruptive();
                if (!z2 && !z3 && !z4 && !z5) {
                    z = false;
                }
                if (z) {
                    this.mHandler.scheduleSendRankingUpdate();
                }
            }
        }
    }

    void handleRankingSort() {
        if (this.mRankingHelper == null) {
            return;
        }
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            ArrayMap arrayMap = new ArrayMap(size);
            for (int i = 0; i < size; i++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i);
                arrayMap.put(notificationRecord.getKey(), new NotificationRecordExtractorData(i, notificationRecord.getPackageVisibilityOverride(), notificationRecord.canShowBadge(), notificationRecord.canBubble(), notificationRecord.getNotification().isBubbleNotification(), notificationRecord.getChannel(), notificationRecord.getGroupKey(), notificationRecord.getPeopleOverride(), notificationRecord.getSnoozeCriteria(), Integer.valueOf(notificationRecord.getUserSentiment()), Integer.valueOf(notificationRecord.getSuppressedVisualEffects()), notificationRecord.getSystemGeneratedSmartActions(), notificationRecord.getSmartReplies(), notificationRecord.getImportance(), notificationRecord.getRankingScore(), notificationRecord.isConversation(), notificationRecord.getProposedImportance(), notificationRecord.hasSensitiveContent()));
                this.mRankingHelper.extractSignals(notificationRecord);
            }
            this.mRankingHelper.sort(this.mNotificationList);
            for (int i2 = 0; i2 < size; i2++) {
                NotificationRecord notificationRecord2 = this.mNotificationList.get(i2);
                if (arrayMap.containsKey(notificationRecord2.getKey())) {
                    if (((NotificationRecordExtractorData) arrayMap.get(notificationRecord2.getKey())).hasDiffForRankingLocked(notificationRecord2, i2)) {
                        this.mHandler.scheduleSendRankingUpdate();
                    }
                    if (notificationRecord2.hasPendingLogUpdate()) {
                        if (((NotificationRecordExtractorData) arrayMap.get(notificationRecord2.getKey())).hasDiffForLoggingLocked(notificationRecord2, i2)) {
                            this.mNotificationRecordLogger.logNotificationAdjusted(notificationRecord2, i2, 0, getGroupInstanceId(notificationRecord2.getSbn().getGroupKey()));
                        }
                        notificationRecord2.setPendingLogUpdate(false);
                    }
                }
            }
        }
    }

    @GuardedBy({"mNotificationLock"})
    private void recordCallerLocked(NotificationRecord notificationRecord) {
        if (this.mZenModeHelper.isCall(notificationRecord)) {
            this.mZenModeHelper.recordCaller(notificationRecord);
        }
    }

    @GuardedBy({"mNotificationLock"})
    private void applyZenModeLocked(NotificationRecord notificationRecord) {
        notificationRecord.setIntercepted(this.mZenModeHelper.shouldIntercept(notificationRecord));
        if (notificationRecord.isIntercepted()) {
            notificationRecord.setSuppressedVisualEffects(this.mZenModeHelper.getConsolidatedNotificationPolicy().suppressedVisualEffects);
        } else {
            notificationRecord.setSuppressedVisualEffects(0);
        }
    }

    @GuardedBy({"mNotificationLock"})
    private int findNotificationRecordIndexLocked(NotificationRecord notificationRecord) {
        return this.mRankingHelper.indexOf(this.mNotificationList, notificationRecord);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendRankingUpdate() {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyRankingUpdateLocked(null);
        }
    }

    private void scheduleListenerHintsChanged(int i) {
        this.mHandler.removeMessages(5);
        this.mHandler.obtainMessage(5, i, 0).sendToTarget();
    }

    private void scheduleInterruptionFilterChanged(int i) {
        this.mHandler.removeMessages(6);
        this.mHandler.obtainMessage(6, i, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleListenerHintsChanged(int i) {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyListenerHintsChangedLocked(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleListenerInterruptionFilterChanged(int i) {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyInterruptionFilterChanged(i);
        }
    }

    void handleOnPackageChanged(boolean z, int i, String[] strArr, int[] iArr) {
        this.mListeners.onPackagesChanged(z, strArr, iArr);
        this.mAssistants.onPackagesChanged(z, strArr, iArr);
        this.mConditionProviders.onPackagesChanged(z, strArr, iArr);
        boolean onPackagesChanged = this.mPreferencesHelper.onPackagesChanged(z, i, strArr, iArr) | z;
        if (z) {
            int min = Math.min(strArr.length, iArr.length);
            for (int i2 = 0; i2 < min; i2++) {
                this.mHistoryManager.onPackageRemoved(UserHandle.getUserId(iArr[i2]), strArr[i2]);
            }
        }
        if (onPackagesChanged) {
            handleSavePolicyFile();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 2:
                    NotificationManagerService.this.handleDurationReached((ToastRecord) message.obj);
                    return;
                case 3:
                default:
                    return;
                case 4:
                    NotificationManagerService.this.handleSendRankingUpdate();
                    return;
                case 5:
                    NotificationManagerService.this.handleListenerHintsChanged(message.arg1);
                    return;
                case 6:
                    NotificationManagerService.this.handleListenerInterruptionFilterChanged(message.arg1);
                    return;
                case 7:
                    NotificationManagerService.this.handleKillTokenTimeout((ToastRecord) message.obj);
                    return;
                case 8:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    NotificationManagerService.this.handleOnPackageChanged(((Boolean) someArgs.arg1).booleanValue(), someArgs.argi1, (String[]) someArgs.arg2, (int[]) someArgs.arg3);
                    someArgs.recycle();
                    return;
            }
        }

        protected void scheduleSendRankingUpdate() {
            if (hasMessages(4)) {
                return;
            }
            sendMessage(Message.obtain(this, 4));
        }

        protected void scheduleCancelNotification(CancelNotificationRunnable cancelNotificationRunnable) {
            if (hasCallbacks(cancelNotificationRunnable)) {
                return;
            }
            sendMessage(Message.obtain(this, cancelNotificationRunnable));
        }

        protected void scheduleOnPackageChanged(boolean z, int i, String[] strArr, int[] iArr) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = Boolean.valueOf(z);
            obtain.argi1 = i;
            obtain.arg2 = strArr;
            obtain.arg3 = iArr;
            sendMessage(Message.obtain(this, 8, obtain));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class RankingHandlerWorker extends Handler implements RankingHandler {
        public RankingHandlerWorker(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1000) {
                NotificationManagerService.this.handleRankingReconsideration(message);
            } else {
                if (i != NotificationManagerService.MESSAGE_RANKING_SORT) {
                    return;
                }
                NotificationManagerService.this.handleRankingSort();
            }
        }

        @Override // com.android.server.notification.RankingHandler
        public void requestSort() {
            removeMessages(NotificationManagerService.MESSAGE_RANKING_SORT);
            Message obtain = Message.obtain();
            obtain.what = NotificationManagerService.MESSAGE_RANKING_SORT;
            sendMessage(obtain);
        }

        @Override // com.android.server.notification.RankingHandler
        public void requestReconsideration(RankingReconsideration rankingReconsideration) {
            sendMessageDelayed(Message.obtain(this, 1000, rankingReconsideration), rankingReconsideration.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    void sendAccessibilityEvent(NotificationRecord notificationRecord) {
        if (this.mAccessibilityManager.isEnabled()) {
            Notification notification = notificationRecord.getNotification();
            String packageName = notificationRecord.getSbn().getPackageName();
            AccessibilityEvent obtain = AccessibilityEvent.obtain(64);
            obtain.setPackageName(packageName);
            obtain.setClassName(Notification.class.getName());
            int packageVisibilityOverride = notificationRecord.getPackageVisibilityOverride();
            if (packageVisibilityOverride == -1000) {
                packageVisibilityOverride = notification.visibility;
            }
            int identifier = notificationRecord.getUser().getIdentifier();
            if ((identifier >= 0 && this.mKeyguardManager.isDeviceLocked(identifier)) && packageVisibilityOverride != 1) {
                obtain.setParcelableData(notification.publicVersion);
            } else {
                obtain.setParcelableData(notification);
            }
            CharSequence charSequence = notification.tickerText;
            if (!TextUtils.isEmpty(charSequence)) {
                obtain.getText().add(charSequence);
            }
            this.mAccessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public boolean removeFromNotificationListsLocked(NotificationRecord notificationRecord) {
        boolean z;
        NotificationRecord findNotificationByListLocked = findNotificationByListLocked(this.mNotificationList, notificationRecord.getKey());
        if (findNotificationByListLocked != null) {
            this.mNotificationList.remove(findNotificationByListLocked);
            this.mNotificationsByKey.remove(findNotificationByListLocked.getSbn().getKey());
            z = true;
        } else {
            z = false;
        }
        while (true) {
            NotificationRecord findNotificationByListLocked2 = findNotificationByListLocked(this.mEnqueuedNotifications, notificationRecord.getKey());
            if (findNotificationByListLocked2 == null) {
                return z;
            }
            this.mEnqueuedNotifications.remove(findNotificationByListLocked2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void cancelNotificationLocked(NotificationRecord notificationRecord, boolean z, @NotificationListenerService.NotificationCancelReason int i, boolean z2, String str, long j) {
        cancelNotificationLocked(notificationRecord, z, i, -1, -1, z2, str, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0182  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0208  */
    /* JADX WARN: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
    @GuardedBy({"mNotificationLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void cancelNotificationLocked(final NotificationRecord notificationRecord, boolean z, @NotificationListenerService.NotificationCancelReason int i, int i2, int i3, boolean z2, String str, long j) {
        String groupKey;
        NotificationRecord notificationRecord2;
        ArrayMap<String, String> arrayMap;
        LogMaker subtype;
        PendingIntent pendingIntent;
        String key = notificationRecord.getKey();
        PendingIntent broadcast = PendingIntent.getBroadcast(getContext(), 1, new Intent(ACTION_NOTIFICATION_TIMEOUT).setData(new Uri.Builder().scheme(SCHEME_TIMEOUT).appendPath(notificationRecord.getKey()).build()).addFlags(268435456), 603979776);
        if (broadcast != null) {
            this.mAlarmManager.cancel(broadcast);
        }
        recordCallerLocked(notificationRecord);
        if (notificationRecord.getStats().getDismissalSurface() == -1) {
            notificationRecord.recordDismissalSurface(0);
        }
        if (z && (pendingIntent = notificationRecord.getNotification().deleteIntent) != null) {
            try {
                ((ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class)).clearPendingIntentAllowBgActivityStarts(pendingIntent.getTarget(), ALLOWLIST_TOKEN);
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Slog.w(TAG, "canceled PendingIntent for " + notificationRecord.getSbn().getPackageName(), e);
            }
        }
        if (z2) {
            if (notificationRecord.getNotification().getSmallIcon() != null) {
                if (i != 18) {
                    notificationRecord.isCanceled = true;
                }
                this.mListeners.notifyRemovedLocked(notificationRecord, i, notificationRecord.getStats());
                this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService.15
                    @Override // java.lang.Runnable
                    public void run() {
                        NotificationManagerService.this.mGroupHelper.onNotificationRemoved(notificationRecord.getSbn());
                    }
                });
            }
            if (key.equals(this.mSoundNotificationKey)) {
                if (this.mNMSWrapper.getNMSExt().shouldForcePlayRedPackageRington(notificationRecord)) {
                    Slog.w(TAG, "We should not stop redpackge ring here");
                } else {
                    if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isLoggable()) {
                        Slog.v(TAG, "clearsound due to notification is cancled.");
                    }
                    clearSoundLocked();
                }
            }
            if (key.equals(this.mVibrateNotificationKey)) {
                clearVibrateLocked();
            }
            this.mLights.remove(key);
        }
        if (i != 2 && i != 3) {
            switch (i) {
                case 8:
                case 9:
                    this.mUsageStats.registerRemovedByApp(notificationRecord);
                    this.mUsageStatsManagerInternal.reportNotificationRemoved(notificationRecord.getSbn().getOpPkg(), notificationRecord.getUser(), j);
                    break;
            }
            groupKey = notificationRecord.getGroupKey();
            notificationRecord2 = this.mSummaryByGroupKey.get(groupKey);
            if (notificationRecord2 != null && notificationRecord2.getKey().equals(key)) {
                this.mSummaryByGroupKey.remove(groupKey);
            }
            arrayMap = this.mAutobundledSummaries.get(Integer.valueOf(notificationRecord.getSbn().getUserId()));
            if (arrayMap != null && notificationRecord.getSbn().getKey().equals(arrayMap.get(notificationRecord.getSbn().getPackageName()))) {
                arrayMap.remove(notificationRecord.getSbn().getPackageName());
            }
            if (i != 20) {
                this.mArchive.record(notificationRecord.getSbn(), i);
            }
            long currentTimeMillis = System.currentTimeMillis();
            subtype = notificationRecord.getItemLogMaker().setType(5).setSubtype(i);
            if (i2 != -1 && i3 != -1) {
                subtype.addTaggedData(798, Integer.valueOf(i2)).addTaggedData(1395, Integer.valueOf(i3));
            }
            MetricsLogger.action(subtype);
            EventLogTags.writeNotificationCanceled(key, i, notificationRecord.getLifespanMs(currentTimeMillis), notificationRecord.getFreshnessMs(currentTimeMillis), notificationRecord.getExposureMs(currentTimeMillis), i2, i3, str);
            if ((2 & notificationRecord.getSbn().getNotification().flags) != 0 && this.mNMSWrapper.getNMSExt() != null) {
                this.mNMSWrapper.getNMSExt().updateNotification(notificationRecord.getUid(), notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getKey(), false);
            }
            if (z2) {
                return;
            }
            this.mNotificationRecordLogger.logNotificationCancelled(notificationRecord, i, notificationRecord.getStats().getDismissalSurface());
            return;
        }
        this.mUsageStats.registerDismissedByUser(notificationRecord);
        groupKey = notificationRecord.getGroupKey();
        notificationRecord2 = this.mSummaryByGroupKey.get(groupKey);
        if (notificationRecord2 != null) {
            this.mSummaryByGroupKey.remove(groupKey);
        }
        arrayMap = this.mAutobundledSummaries.get(Integer.valueOf(notificationRecord.getSbn().getUserId()));
        if (arrayMap != null) {
            arrayMap.remove(notificationRecord.getSbn().getPackageName());
        }
        if (i != 20) {
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        subtype = notificationRecord.getItemLogMaker().setType(5).setSubtype(i);
        if (i2 != -1) {
            subtype.addTaggedData(798, Integer.valueOf(i2)).addTaggedData(1395, Integer.valueOf(i3));
        }
        MetricsLogger.action(subtype);
        EventLogTags.writeNotificationCanceled(key, i, notificationRecord.getLifespanMs(currentTimeMillis2), notificationRecord.getFreshnessMs(currentTimeMillis2), notificationRecord.getExposureMs(currentTimeMillis2), i2, i3, str);
        if ((2 & notificationRecord.getSbn().getNotification().flags) != 0) {
            this.mNMSWrapper.getNMSExt().updateNotification(notificationRecord.getUid(), notificationRecord.getSbn().getPackageName(), notificationRecord.getSbn().getKey(), false);
        }
        if (z2) {
        }
    }

    @VisibleForTesting
    void updateUriPermissions(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, String str, int i) {
        updateUriPermissions(notificationRecord, notificationRecord2, str, i, false);
    }

    @VisibleForTesting
    void updateUriPermissions(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, String str, int i, boolean z) {
        IBinder iBinder;
        String key = notificationRecord != null ? notificationRecord.getKey() : notificationRecord2.getKey();
        boolean z2 = DBG;
        if (z2) {
            Slog.d(TAG, key + ": updating permissions");
        }
        ArraySet<Uri> grantableUris = notificationRecord != null ? notificationRecord.getGrantableUris() : null;
        ArraySet<Uri> grantableUris2 = notificationRecord2 != null ? notificationRecord2.getGrantableUris() : null;
        if (grantableUris == null && grantableUris2 == null) {
            return;
        }
        IBinder iBinder2 = notificationRecord != null ? notificationRecord.permissionOwner : null;
        if (notificationRecord2 != null && iBinder2 == null) {
            iBinder2 = notificationRecord2.permissionOwner;
        }
        if (grantableUris != null && iBinder2 == null) {
            if (z2) {
                Slog.d(TAG, key + ": creating owner");
            }
            iBinder2 = this.mUgmInternal.newUriPermissionOwner("NOTIF:" + key);
        }
        if (grantableUris != null || iBinder2 == null || z) {
            iBinder = iBinder2;
        } else {
            destroyPermissionOwner(iBinder2, UserHandle.getUserId(notificationRecord2.getUid()), key);
            iBinder = null;
        }
        if (grantableUris != null && iBinder != null) {
            for (int i2 = 0; i2 < grantableUris.size(); i2++) {
                Uri valueAt = grantableUris.valueAt(i2);
                if (grantableUris2 == null || !grantableUris2.contains(valueAt)) {
                    if (DBG) {
                        Slog.d(TAG, key + ": granting " + valueAt);
                    }
                    grantUriPermission(iBinder, valueAt, notificationRecord.getUid(), str, i);
                }
            }
        }
        if (grantableUris2 != null && iBinder != null) {
            for (int i3 = 0; i3 < grantableUris2.size(); i3++) {
                Uri valueAt2 = grantableUris2.valueAt(i3);
                if (grantableUris == null || !grantableUris.contains(valueAt2)) {
                    if (DBG) {
                        Slog.d(TAG, key + ": revoking " + valueAt2);
                    }
                    if (z) {
                        revokeUriPermission(iBinder, valueAt2, UserHandle.getUserId(notificationRecord2.getUid()), str, i);
                    } else {
                        revokeUriPermission(iBinder, valueAt2, UserHandle.getUserId(notificationRecord2.getUid()), null, -1);
                    }
                }
            }
        }
        if (notificationRecord != null) {
            notificationRecord.permissionOwner = iBinder;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantUriPermission(IBinder iBinder, Uri uri, int i, String str, int i2) {
        if (uri == null || !"content".equals(uri.getScheme())) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mUgm.grantUriPermissionFromOwner(iBinder, i, str, ContentProvider.getUriWithoutUserId(uri), 1, ContentProvider.getUserIdFromUri(uri, UserHandle.getUserId(i)), i2);
            } catch (RemoteException unused) {
            } catch (SecurityException unused2) {
                Slog.e(TAG, "Cannot grant uri access; " + i + " does not own " + uri);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void revokeUriPermission(IBinder iBinder, Uri uri, int i, String str, int i2) {
        if (uri == null || !"content".equals(uri.getScheme())) {
            return;
        }
        int userIdFromUri = ContentProvider.getUserIdFromUri(uri, i);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mUgmInternal.revokeUriPermissionFromOwner(iBinder, ContentProvider.getUriWithoutUserId(uri), 1, userIdFromUri, str, i2);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyPermissionOwner(IBinder iBinder, int i, String str) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (DBG) {
                Slog.d(TAG, str + ": destroying owner");
            }
            this.mUgmInternal.revokeUriPermissionFromOwner(iBinder, null, -1, i);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    void cancelNotification(int i, int i2, String str, String str2, int i3, int i4, int i5, boolean z, int i6, int i7, ManagedServices.ManagedServiceInfo managedServiceInfo) {
        cancelNotification(i, i2, str, str2, i3, i4, i5, z, i6, i7, -1, -1, managedServiceInfo);
    }

    void cancelNotification(int i, int i2, String str, String str2, int i3, int i4, int i5, boolean z, int i6, int i7, int i8, int i9, ManagedServices.ManagedServiceInfo managedServiceInfo) {
        this.mHandler.scheduleCancelNotification(new CancelNotificationRunnable(i, i2, str, str2, i3, i4, i5, z, i6, i7, i8, i9, managedServiceInfo, SystemClock.elapsedRealtime()));
    }

    private boolean notificationMatchesUserId(NotificationRecord notificationRecord, int i) {
        return (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isNotificationForCurrentUser(notificationRecord, i)) || i == -1 || notificationRecord.getUserId() == -1 || notificationRecord.getUserId() == i;
    }

    private boolean notificationMatchesCurrentProfiles(NotificationRecord notificationRecord, int i) {
        return notificationMatchesUserId(notificationRecord, i) || this.mUserProfiles.isCurrentProfile(notificationRecord.getUserId()) || (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isNotificationForCurrentUser(notificationRecord, notificationRecord.getUserId()));
    }

    void cancelAllNotificationsInt(int i, int i2, String str, String str2, int i3, int i4, boolean z, int i5, int i6, ManagedServices.ManagedServiceInfo managedServiceInfo) {
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().setNavigationStatus(str, str2, i, i2, i6);
        }
        this.mHandler.post(new AnonymousClass16(managedServiceInfo, i, i2, str, i5, i3, i4, i6, z, str2, SystemClock.elapsedRealtime()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.notification.NotificationManagerService$16, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass16 implements Runnable {
        final /* synthetic */ int val$callingPid;
        final /* synthetic */ int val$callingUid;
        final /* synthetic */ long val$cancellationElapsedTimeMs;
        final /* synthetic */ String val$channelId;
        final /* synthetic */ boolean val$doit;
        final /* synthetic */ ManagedServices.ManagedServiceInfo val$listener;
        final /* synthetic */ int val$mustHaveFlags;
        final /* synthetic */ int val$mustNotHaveFlags;
        final /* synthetic */ String val$pkg;
        final /* synthetic */ int val$reason;
        final /* synthetic */ int val$userId;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$run$0(int i, int i2, int i3) {
            return (i3 & i) == i && (i3 & i2) == 0;
        }

        AnonymousClass16(ManagedServices.ManagedServiceInfo managedServiceInfo, int i, int i2, String str, int i3, int i4, int i5, int i6, boolean z, String str2, long j) {
            this.val$listener = managedServiceInfo;
            this.val$callingUid = i;
            this.val$callingPid = i2;
            this.val$pkg = str;
            this.val$userId = i3;
            this.val$mustHaveFlags = i4;
            this.val$mustNotHaveFlags = i5;
            this.val$reason = i6;
            this.val$doit = z;
            this.val$channelId = str2;
            this.val$cancellationElapsedTimeMs = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            ManagedServices.ManagedServiceInfo managedServiceInfo = this.val$listener;
            String shortString = managedServiceInfo == null ? null : managedServiceInfo.component.toShortString();
            EventLogTags.writeNotificationCancelAll(this.val$callingUid, this.val$callingPid, this.val$pkg, this.val$userId, this.val$mustHaveFlags, this.val$mustNotHaveFlags, this.val$reason, shortString);
            if (this.val$doit) {
                synchronized (NotificationManagerService.this.mNotificationLock) {
                    try {
                        try {
                            final int i = this.val$mustHaveFlags;
                            final int i2 = this.val$mustNotHaveFlags;
                            FlagChecker flagChecker = new FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$16$$ExternalSyntheticLambda0
                                @Override // com.android.server.notification.NotificationManagerService.FlagChecker
                                public final boolean apply(int i3) {
                                    boolean lambda$run$0;
                                    lambda$run$0 = NotificationManagerService.AnonymousClass16.lambda$run$0(i, i2, i3);
                                    return lambda$run$0;
                                }
                            };
                            NotificationManagerService notificationManagerService = NotificationManagerService.this;
                            notificationManagerService.cancelAllNotificationsByListLocked(notificationManagerService.mNotificationList, this.val$callingUid, this.val$callingPid, this.val$pkg, true, this.val$channelId, flagChecker, false, this.val$userId, false, this.val$reason, shortString, true, this.val$cancellationElapsedTimeMs);
                            NotificationManagerService notificationManagerService2 = NotificationManagerService.this;
                            notificationManagerService2.cancelAllNotificationsByListLocked(notificationManagerService2.mEnqueuedNotifications, this.val$callingUid, this.val$callingPid, this.val$pkg, true, this.val$channelId, flagChecker, false, this.val$userId, false, this.val$reason, shortString, false, this.val$cancellationElapsedTimeMs);
                            NotificationManagerService.this.mSnoozeHelper.cancel(this.val$userId, this.val$pkg);
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void cancelAllNotificationsByListLocked(ArrayList<NotificationRecord> arrayList, int i, int i2, String str, boolean z, String str2, FlagChecker flagChecker, boolean z2, int i3, boolean z3, int i4, String str3, boolean z4, long j) {
        HashSet hashSet = null;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            NotificationRecord notificationRecord = arrayList.get(size);
            if (z2) {
                if (!notificationMatchesCurrentProfiles(notificationRecord, i3)) {
                }
                if (z) {
                    if (str == null && notificationRecord.getUserId() == -1) {
                    }
                }
                if (flagChecker.apply(notificationRecord.getFlags())) {
                    if (str == null) {
                        if (notificationRecord.getSbn().getPackageName().equals(str)) {
                        }
                    }
                    if (str2 == null) {
                        if (str2.equals(notificationRecord.getChannel().getId())) {
                        }
                    }
                    if (this.mNMSWrapper.getNMSExt() == null && this.mNMSWrapper.getNMSExt().shouldKeepNotifcationWhenForceStop(str, notificationRecord, i4)) {
                    }
                    if (this.mNMSWrapper.getNMSExt() != null || !this.mNMSWrapper.getNMSExt().shouldKeepNotifcationWhenTopStyleNotShow(notificationRecord)) {
                        if (!notificationRecord.getSbn().isGroup() && notificationRecord.getNotification().isGroupChild()) {
                            if (hashSet == null) {
                                hashSet = new HashSet();
                            }
                            hashSet.add(notificationRecord.getKey());
                        } else {
                            arrayList.remove(size);
                            this.mNotificationsByKey.remove(notificationRecord.getKey());
                            notificationRecord.recordDismissalSentiment(1);
                            cancelNotificationLocked(notificationRecord, z3, i4, z4, str3, j);
                        }
                    }
                }
            } else {
                if (!notificationMatchesUserId(notificationRecord, i3)) {
                }
                if (z && str == null) {
                }
                if (flagChecker.apply(notificationRecord.getFlags()) && ((str == null || notificationRecord.getSbn().getPackageName().equals(str)) && (str2 == null || str2.equals(notificationRecord.getChannel().getId())))) {
                    if (this.mNMSWrapper.getNMSExt() == null) {
                    }
                    if (this.mNMSWrapper.getNMSExt() != null) {
                    }
                    if (!notificationRecord.getSbn().isGroup()) {
                    }
                    arrayList.remove(size);
                    this.mNotificationsByKey.remove(notificationRecord.getKey());
                    notificationRecord.recordDismissalSentiment(1);
                    cancelNotificationLocked(notificationRecord, z3, i4, z4, str3, j);
                }
            }
        }
        if (hashSet != null) {
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                NotificationRecord notificationRecord2 = arrayList.get(size2);
                if (hashSet.contains(notificationRecord2.getKey())) {
                    arrayList.remove(size2);
                    this.mNotificationsByKey.remove(notificationRecord2.getKey());
                    notificationRecord2.recordDismissalSentiment(1);
                    cancelNotificationLocked(notificationRecord2, z3, i4, z4, str3, j);
                }
            }
            updateLightsLocked();
        }
    }

    void snoozeNotificationInt(String str, long j, String str2, ManagedServices.ManagedServiceInfo managedServiceInfo) {
        if (managedServiceInfo == null) {
            return;
        }
        String shortString = managedServiceInfo.component.toShortString();
        if ((j > 0 || str2 != null) && str != null) {
            synchronized (this.mNotificationLock) {
                NotificationRecord findInCurrentAndSnoozedNotificationByKeyLocked = findInCurrentAndSnoozedNotificationByKeyLocked(str);
                if (findInCurrentAndSnoozedNotificationByKeyLocked == null) {
                    return;
                }
                if (managedServiceInfo.enabledAndUserMatches(findInCurrentAndSnoozedNotificationByKeyLocked.getSbn().getNormalizedUserId())) {
                    if (DBG) {
                        Slog.d(TAG, String.format("snooze event(%s, %d, %s, %s)", str, Long.valueOf(j), str2, shortString));
                    }
                    this.mHandler.post(new SnoozeNotificationRunnable(str, j, str2));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unsnoozeNotificationInt(String str, ManagedServices.ManagedServiceInfo managedServiceInfo, boolean z) {
        String shortString = managedServiceInfo == null ? null : managedServiceInfo.component.toShortString();
        if (DBG) {
            Slog.d(TAG, String.format("unsnooze event(%s, %s)", str, shortString));
        }
        this.mSnoozeHelper.repost(str, z);
        handleSavePolicyFile();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.notification.NotificationManagerService$17, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass17 implements Runnable {
        final /* synthetic */ int val$callingPid;
        final /* synthetic */ int val$callingUid;
        final /* synthetic */ long val$cancellationElapsedTimeMs;
        final /* synthetic */ boolean val$includeCurrentProfiles;
        final /* synthetic */ ManagedServices.ManagedServiceInfo val$listener;
        final /* synthetic */ int val$reason;
        final /* synthetic */ int val$userId;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$run$0(int i, int i2) {
            return (((11 == i || 3 == i) ? 4130 : 34) & i2) == 0;
        }

        AnonymousClass17(ManagedServices.ManagedServiceInfo managedServiceInfo, int i, int i2, int i3, int i4, boolean z, long j) {
            this.val$listener = managedServiceInfo;
            this.val$callingUid = i;
            this.val$callingPid = i2;
            this.val$userId = i3;
            this.val$reason = i4;
            this.val$includeCurrentProfiles = z;
            this.val$cancellationElapsedTimeMs = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (NotificationManagerService.this.mNotificationLock) {
                ManagedServices.ManagedServiceInfo managedServiceInfo = this.val$listener;
                String shortString = managedServiceInfo == null ? null : managedServiceInfo.component.toShortString();
                EventLogTags.writeNotificationCancelAll(this.val$callingUid, this.val$callingPid, (String) null, this.val$userId, 0, 0, this.val$reason, shortString);
                final int i = this.val$reason;
                FlagChecker flagChecker = new FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$17$$ExternalSyntheticLambda0
                    @Override // com.android.server.notification.NotificationManagerService.FlagChecker
                    public final boolean apply(int i2) {
                        boolean lambda$run$0;
                        lambda$run$0 = NotificationManagerService.AnonymousClass17.lambda$run$0(i, i2);
                        return lambda$run$0;
                    }
                };
                NotificationManagerService notificationManagerService = NotificationManagerService.this;
                notificationManagerService.cancelAllNotificationsByListLocked(notificationManagerService.mNotificationList, this.val$callingUid, this.val$callingPid, null, false, null, flagChecker, this.val$includeCurrentProfiles, this.val$userId, true, this.val$reason, shortString, true, this.val$cancellationElapsedTimeMs);
                NotificationManagerService notificationManagerService2 = NotificationManagerService.this;
                notificationManagerService2.cancelAllNotificationsByListLocked(notificationManagerService2.mEnqueuedNotifications, this.val$callingUid, this.val$callingPid, null, false, null, flagChecker, this.val$includeCurrentProfiles, this.val$userId, true, this.val$reason, shortString, false, this.val$cancellationElapsedTimeMs);
                if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                    NotificationManagerService.this.mNMSWrapper.getNMSExt().clearNonCancelableSummaryKeys(this.val$reason);
                }
                NotificationManagerService.this.mSnoozeHelper.cancel(this.val$userId, this.val$includeCurrentProfiles);
            }
        }
    }

    @GuardedBy({"mNotificationLock"})
    void cancelAllLocked(int i, int i2, int i3, int i4, ManagedServices.ManagedServiceInfo managedServiceInfo, boolean z) {
        this.mHandler.post(new AnonymousClass17(managedServiceInfo, i, i2, i3, i4, z, SystemClock.elapsedRealtime()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void cancelGroupChildrenLocked(NotificationRecord notificationRecord, int i, int i2, String str, boolean z, FlagChecker flagChecker, int i3, long j) {
        if (notificationRecord.getNotification().isGroupSummary()) {
            if (notificationRecord.getSbn().getPackageName() == null) {
                if (DBG) {
                    Slog.e(TAG, "No package for group summary: " + notificationRecord.getKey());
                    return;
                }
                return;
            }
            cancelGroupChildrenByListLocked(this.mNotificationList, notificationRecord, i, i2, str, z, true, flagChecker, i3, j);
            cancelGroupChildrenByListLocked(this.mEnqueuedNotifications, notificationRecord, i, i2, str, z, false, flagChecker, i3, j);
        }
    }

    @GuardedBy({"mNotificationLock"})
    private void cancelGroupChildrenByListLocked(ArrayList<NotificationRecord> arrayList, NotificationRecord notificationRecord, int i, int i2, String str, boolean z, boolean z2, FlagChecker flagChecker, int i3, long j) {
        String packageName = notificationRecord.getSbn().getPackageName();
        int userId = notificationRecord.getUserId();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            NotificationRecord notificationRecord2 = arrayList.get(size);
            StatusBarNotification sbn = notificationRecord2.getSbn();
            if (!sbn.isGroup() || sbn.getNotification().isGroupSummary() || !notificationRecord2.getGroupKey().equals(notificationRecord.getGroupKey()) || ((flagChecker != null && !flagChecker.apply(notificationRecord2.getFlags())) || (notificationRecord2.getChannel().isImportantConversation() && i3 == 2))) {
            }
            EventLogTags.writeNotificationCancel(i, i2, packageName, sbn.getId(), sbn.getTag(), userId, 0, 0, 12, str);
            arrayList.remove(size);
            this.mNotificationsByKey.remove(notificationRecord2.getKey());
            cancelNotificationLocked(notificationRecord2, z, 12, z2, str, j);
        }
    }

    @GuardedBy({"mNotificationLock"})
    void updateLightsLocked() {
        if (this.mNotificationLight == null) {
            Slog.d(TAG, "mNotificationLight is null");
            return;
        }
        NotificationRecord notificationRecord = null;
        while (notificationRecord == null && !this.mLights.isEmpty()) {
            ArrayList<String> arrayList = this.mLights;
            String str = arrayList.get(arrayList.size() - 1);
            NotificationRecord notificationRecord2 = this.mNotificationsByKey.get(str);
            if (notificationRecord2 == null) {
                Slog.wtfStack(TAG, "LED Notification does not exist: " + str);
                this.mLights.remove(str);
            }
            notificationRecord = notificationRecord2;
        }
        if (this.mNMSWrapper.getNMSExt() == null || !this.mNMSWrapper.getNMSExt().updateLightsStateLocked(notificationRecord)) {
            if (notificationRecord == null || isInCall() || this.mScreenOn) {
                this.mNotificationLight.turnOff();
                return;
            }
            NotificationRecord.Light light = notificationRecord.getLight();
            if (light == null || !this.mNotificationPulseEnabled) {
                return;
            }
            this.mNotificationLight.setFlashing(light.color, 1, light.onMs, light.offMs);
        }
    }

    private void scheduleLightsOffTimeoutLocked(NotificationRecord notificationRecord) {
        StatusBarNotification sbn;
        if (notificationRecord == null || (sbn = notificationRecord.getSbn()) == null) {
            return;
        }
        long postTime = sbn.getPostTime();
        long j = SystemProperties.getLong("notification_manager_service.light.timeout", 0L);
        if (j == 0) {
            j = 7200000;
        }
        if (mNMSExt.isLoggable()) {
            Slog.d(TAG, "pkg = " + sbn.getPackageName() + ", posttime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(postTime)) + ", LIGHTS_OFF_DELAY = " + j);
        }
        this.mAlarmManager.setExactAndAllowWhileIdle(0, postTime + j, PendingIntent.getBroadcast(getContext(), 10, new Intent(ACTION_NOTIFICATION_LIGHTS_TIMEOUT).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME).addFlags(268435456), 67108864));
    }

    private boolean updateLightsStateLocked(NotificationRecord notificationRecord) {
        scheduleLightsOffTimeoutLocked(notificationRecord);
        if (notificationRecord == null) {
            if (this.mIsFlashing) {
                Slog.d(TAG, "no ledNotification, turn it off, " + Log.getStackTraceString(new Throwable()));
            }
            this.mIsFlashing = false;
            this.mNotificationLight.turnOff();
            return true;
        }
        ZenModeHelper zenModeHelper = this.mZenModeHelper;
        if (zenModeHelper != null && zenModeHelper.getZenMode() != 0) {
            if (this.mIsFlashing) {
                Slog.d(TAG, "Zen mode ON, turn it off");
            }
            this.mIsFlashing = false;
            this.mNotificationLight.turnOff();
            return true;
        }
        if (!mCustomizeBreathLight) {
            if (this.mIsFlashing) {
                Slog.d(TAG, "not enabled, turn it off");
            }
            this.mIsFlashing = false;
            this.mNotificationLight.turnOff();
            return true;
        }
        if (!isInBreathAllowPeriod()) {
            if (this.mIsFlashing) {
                Slog.d(TAG, "not in period, turn it off");
            }
            this.mIsFlashing = false;
            this.mNotificationLight.turnOff();
            return true;
        }
        NotificationRecord.Light light = notificationRecord.getLight();
        if (light == null) {
            Slog.d(TAG, "light is null");
            return true;
        }
        if (!this.mScreenLocked) {
            if (this.mIsFlashing) {
                Slog.d(TAG, "ScreenUnlock, turn it off");
            }
            this.mIsFlashing = false;
            this.mNotificationLight.turnOff();
        } else if (isLightNotification(notificationRecord)) {
            if (!this.mIsFlashing) {
                Slog.d(TAG, "ledNotification, setFlashing: " + notificationRecord + ", " + Integer.toHexString(light.color));
            }
            this.mIsFlashing = true;
            this.mNotificationLight.setFlashing(light.color, 2, light.onMs, light.offMs);
        } else if (this.mIsFlashing) {
            Slog.d(TAG, "turn it off");
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleLightsPeriod() {
        int i;
        int i2;
        int i3;
        long currentTimeMillis = System.currentTimeMillis();
        long minutes = TimeUnit.DAYS.toMinutes(1L);
        Calendar calendar = Calendar.getInstance();
        if (calendar != null) {
            i2 = calendar.get(11);
            i3 = calendar.get(12);
            i = calendar.get(13);
        } else {
            i = 0;
            i2 = 0;
            i3 = 0;
        }
        long j = (this.mEndHour * 60 * 60) + (this.mEndMinute * 60);
        long j2 = (i2 * 60 * 60) + (i3 * 60) + i;
        long j3 = j - j2;
        long j4 = j3 > 0 ? currentTimeMillis + (1000 * j3) : currentTimeMillis + (((minutes * 60) + j3) * 1000);
        Slog.d(TAG, "scheduleLightsPeriod " + mCustomizeBreathLight + ", timeoutPeriod: " + j4 + ", now: " + System.currentTimeMillis() + ", endSec: " + j + ", nowSec: " + j2 + ", diff: " + j3);
        PendingIntent broadcast = PendingIntent.getBroadcast(getContext(), 11, new Intent(ACTION_NOTIFICATION_LIGHTS_PERIOD_TIMEOUT).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME).addFlags(268435456), 67108864);
        if (mCustomizeBreathLight) {
            this.mAlarmManager.setExactAndAllowWhileIdle(0, j4, broadcast);
        } else {
            this.mAlarmManager.cancel(broadcast);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePeriod() {
        if (TextUtils.isEmpty(this.mBreathAllowPeriod)) {
            Log.d(TAG, "updatePeriod mBreathAllowPeriod is empty and use default item");
            this.mBreathAllowPeriod = BREATHE_LIGHT_DEFAULT_ALLOW_PERIOD;
        }
        try {
            this.mBeginHour = Integer.parseInt(TextUtils.substring(this.mBreathAllowPeriod, 0, 2));
            this.mBeginMinute = Integer.parseInt(TextUtils.substring(this.mBreathAllowPeriod, 2, 4));
            this.mEndHour = Integer.parseInt(TextUtils.substring(this.mBreathAllowPeriod, 4, 6));
            this.mEndMinute = Integer.parseInt(TextUtils.substring(this.mBreathAllowPeriod, 6, 8));
        } catch (NullPointerException unused) {
        } catch (NumberFormatException e) {
            Slog.d(TAG, "isInPeriod: " + this.mBreathAllowPeriod + ", " + e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0034, code lost:
    
        if (r0 < r2) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0026, code lost:
    
        if (r0 < r6) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean isInBreathAllowPeriod() {
        updatePeriod();
        long nowTimeInMinute = getNowTimeInMinute();
        long j = (this.mBeginHour * 60) + this.mBeginMinute;
        long j2 = (this.mEndHour * 60) + this.mEndMinute;
        boolean z = true;
        boolean z2 = false;
        if (j2 > j) {
            if (nowTimeInMinute >= j) {
            }
            z = false;
            z2 = z;
        } else if (j2 < j) {
            if (nowTimeInMinute >= j2) {
            }
            z2 = z;
        }
        if (DBG) {
            Slog.d(TAG, "checkInDuration: begin = " + this.mBeginHour + ":" + this.mBeginMinute + ", end = " + this.mEndHour + ":" + this.mEndMinute + ", now = " + nowTimeInMinute + ", beginTime = " + j + ", endTime = " + j2 + ", inDuration = " + z2);
        }
        return z2;
    }

    private int getNowTimeInMinute() {
        int i;
        int i2;
        Calendar calendar = Calendar.getInstance();
        if (calendar != null) {
            i = calendar.get(11);
            i2 = calendar.get(12);
        } else {
            i = 0;
            i2 = 0;
        }
        return (i * 60) + i2;
    }

    public static boolean isInLightList(String str, int i) {
        ArrayList<String> arrayList = mLightList;
        if (arrayList == null) {
            return false;
        }
        boolean contains = arrayList.contains(str + i);
        Slog.d(TAG, "isInLightList: 0, " + str + ", " + i + ", " + contains);
        return contains;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLightList() {
        ArrayList<String> arrayList = mLightList;
        if (arrayList == null) {
            return;
        }
        arrayList.clear();
        if (TextUtils.isEmpty(this.mSettingsSpecialLights)) {
            return;
        }
        for (String str : this.mSettingsSpecialLights.split(",")) {
            String replace = str.replace("{", "").replace("}", "").replace("\"", "");
            Slog.d(TAG, "item: " + replace);
            if (!TextUtils.isEmpty(replace)) {
                String[] split = replace.split(":");
                if ("true".equals(split[1]) && !TextUtils.isEmpty(split[0]) && !TextUtils.isEmpty(split[1])) {
                    String[] split2 = split[0].split(" ");
                    if (!TextUtils.isEmpty(split2[0]) && !TextUtils.isEmpty(split2[1])) {
                        mLightList.add(split2[0] + split2[1]);
                        Slog.d(TAG, "add: " + split2[0] + ", " + split2[1]);
                    }
                }
            }
        }
    }

    private boolean isLightNotification(NotificationRecord notificationRecord) {
        boolean z = notificationRecord.getLight() != null;
        if (DBG) {
            Slog.d(TAG, "hasLight: " + z + ", " + notificationRecord);
        }
        return z;
    }

    @GuardedBy({"mNotificationLock"})
    List<NotificationRecord> findCurrentAndSnoozedGroupNotificationsLocked(String str, String str2, int i) {
        ArrayList<NotificationRecord> notifications = this.mSnoozeHelper.getNotifications(str, str2, Integer.valueOf(i));
        notifications.addAll(findGroupNotificationsLocked(str, str2, i));
        return notifications;
    }

    @GuardedBy({"mNotificationLock"})
    List<NotificationRecord> findGroupNotificationsLocked(String str, String str2, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(findGroupNotificationByListLocked(this.mNotificationList, str, str2, i));
        arrayList.addAll(findGroupNotificationByListLocked(this.mEnqueuedNotifications, str, str2, i));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public NotificationRecord findInCurrentAndSnoozedNotificationByKeyLocked(String str) {
        NotificationRecord findNotificationByKeyLocked = findNotificationByKeyLocked(str);
        return findNotificationByKeyLocked == null ? this.mSnoozeHelper.getNotification(str) : findNotificationByKeyLocked;
    }

    @GuardedBy({"mNotificationLock"})
    private List<NotificationRecord> findGroupNotificationByListLocked(ArrayList<NotificationRecord> arrayList, String str, String str2, int i) {
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            NotificationRecord notificationRecord = arrayList.get(i2);
            if (notificationMatchesUserId(notificationRecord, i) && notificationRecord.getGroupKey().equals(str2) && notificationRecord.getSbn().getPackageName().equals(str)) {
                arrayList2.add(notificationRecord);
            }
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public NotificationRecord findNotificationByKeyLocked(String str) {
        NotificationRecord findNotificationByListLocked = findNotificationByListLocked(this.mNotificationList, str);
        if (findNotificationByListLocked != null) {
            return findNotificationByListLocked;
        }
        NotificationRecord findNotificationByListLocked2 = findNotificationByListLocked(this.mEnqueuedNotifications, str);
        if (findNotificationByListLocked2 != null) {
            return findNotificationByListLocked2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mNotificationLock"})
    public NotificationRecord findNotificationLocked(String str, String str2, int i, int i2) {
        NotificationRecord findNotificationByListLocked = findNotificationByListLocked(this.mNotificationList, str, str2, i, i2);
        if (findNotificationByListLocked != null) {
            return findNotificationByListLocked;
        }
        NotificationRecord findNotificationByListLocked2 = findNotificationByListLocked(this.mEnqueuedNotifications, str, str2, i, i2);
        if (findNotificationByListLocked2 != null) {
            return findNotificationByListLocked2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public NotificationRecord findNotificationByListLocked(ArrayList<NotificationRecord> arrayList, String str, String str2, int i, int i2) {
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            NotificationRecord notificationRecord = arrayList.get(i3);
            if (notificationMatchesUserId(notificationRecord, i2) && notificationRecord.getSbn().getId() == i && TextUtils.equals(notificationRecord.getSbn().getTag(), str2) && notificationRecord.getSbn().getPackageName().equals(str)) {
                return notificationRecord;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public List<NotificationRecord> findNotificationsByListLocked(ArrayList<NotificationRecord> arrayList, String str, String str2, int i, int i2) {
        ArrayList arrayList2 = new ArrayList();
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            NotificationRecord notificationRecord = arrayList.get(i3);
            if (notificationMatchesUserId(notificationRecord, i2) && notificationRecord.getSbn().getId() == i && TextUtils.equals(notificationRecord.getSbn().getTag(), str2) && notificationRecord.getSbn().getPackageName().equals(str)) {
                arrayList2.add(notificationRecord);
            }
        }
        return arrayList2;
    }

    @GuardedBy({"mNotificationLock"})
    private NotificationRecord findNotificationByListLocked(ArrayList<NotificationRecord> arrayList, String str) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (str.equals(arrayList.get(i).getKey())) {
                return arrayList.get(i);
            }
        }
        return null;
    }

    @GuardedBy({"mNotificationLock"})
    private List<NotificationRecord> findEnqueuedNotificationsForCriteria(String str, String str2, int i, int i2) {
        ArrayList arrayList = new ArrayList();
        int size = this.mEnqueuedNotifications.size();
        for (int i3 = 0; i3 < size; i3++) {
            NotificationRecord notificationRecord = this.mEnqueuedNotifications.get(i3);
            if (notificationMatchesUserId(notificationRecord, i2) && notificationRecord.getSbn().getId() == i && TextUtils.equals(notificationRecord.getSbn().getTag(), str2) && notificationRecord.getSbn().getPackageName().equals(str)) {
                arrayList.add(notificationRecord);
            }
        }
        return arrayList;
    }

    @GuardedBy({"mNotificationLock"})
    int indexOfNotificationLocked(String str) {
        int size = this.mNotificationList.size();
        for (int i = 0; i < size; i++) {
            if (str.equals(this.mNotificationList.get(i).getKey())) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideNotificationsForPackages(String[] strArr, int[] iArr) {
        synchronized (this.mNotificationLock) {
            Set set = (Set) Arrays.stream(iArr).boxed().collect(Collectors.toSet());
            List asList = Arrays.asList(strArr);
            ArrayList arrayList = new ArrayList();
            int size = this.mNotificationList.size();
            for (int i = 0; i < size; i++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i);
                if (asList.contains(notificationRecord.getSbn().getPackageName()) && set.contains(Integer.valueOf(notificationRecord.getUid()))) {
                    notificationRecord.setHidden(true);
                    arrayList.add(notificationRecord);
                }
            }
            this.mListeners.notifyHiddenLocked(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unhideNotificationsForPackages(String[] strArr, int[] iArr) {
        synchronized (this.mNotificationLock) {
            Set set = (Set) Arrays.stream(iArr).boxed().collect(Collectors.toSet());
            List asList = Arrays.asList(strArr);
            ArrayList arrayList = new ArrayList();
            int size = this.mNotificationList.size();
            for (int i = 0; i < size; i++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i);
                if (asList.contains(notificationRecord.getSbn().getPackageName()) && set.contains(Integer.valueOf(notificationRecord.getUid()))) {
                    notificationRecord.setHidden(false);
                    arrayList.add(notificationRecord);
                }
            }
            this.mListeners.notifyUnhiddenLocked(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotificationsWhenEnterLockDownMode(int i) {
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            for (int i2 = 0; i2 < size; i2++) {
                NotificationRecord notificationRecord = this.mNotificationList.get(i2);
                if (notificationRecord.getUser().getIdentifier() == i) {
                    this.mListeners.notifyRemovedLocked(notificationRecord, 23, notificationRecord.getStats());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postNotificationsWhenExitLockDownMode(int i) {
        synchronized (this.mNotificationLock) {
            int size = this.mNotificationList.size();
            long j = 0;
            for (int i2 = 0; i2 < size; i2++) {
                final NotificationRecord notificationRecord = this.mNotificationList.get(i2);
                if (notificationRecord.getUser().getIdentifier() == i) {
                    this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationManagerService.this.lambda$postNotificationsWhenExitLockDownMode$11(notificationRecord);
                        }
                    }, j);
                    j += 20;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postNotificationsWhenExitLockDownMode$11(NotificationRecord notificationRecord) {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyPostedLocked(notificationRecord, notificationRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNotificationPulse() {
        synchronized (this.mNotificationLock) {
            updateLightsLocked();
        }
    }

    protected boolean isCallingUidSystem() {
        return Binder.getCallingUid() == 1000;
    }

    protected boolean isCallingAppIdSystem() {
        return UserHandle.getAppId(Binder.getCallingUid()) == 1000;
    }

    protected boolean isUidSystemOrPhone(int i) {
        int appId = UserHandle.getAppId(i);
        return appId == 1000 || appId == MESSAGE_RANKING_SORT || i == 0;
    }

    protected boolean isCallerSystemOrPhone() {
        return isUidSystemOrPhone(Binder.getCallingUid());
    }

    @VisibleForTesting
    protected boolean isCallerIsSystemOrSystemUi() {
        return isCallerSystemOrPhone() || getContext().checkCallingPermission("android.permission.STATUS_BAR_SERVICE") == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCallerIsSystemOrSysemUiOrShell() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0) {
            return true;
        }
        return isCallerIsSystemOrSystemUi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrShell() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0) {
            return;
        }
        checkCallerIsSystem();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystem() {
        if (isCallerSystemOrPhone()) {
            return;
        }
        throw new SecurityException("Disallowed call for uid " + Binder.getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrSystemUiOrShell() {
        checkCallerIsSystemOrSystemUiOrShell(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrSystemUiOrShell(String str) {
        int callingUid = Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0 || isCallerSystemOrPhone()) {
            return;
        }
        getContext().enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrSameApp(String str) {
        if (isCallerSystemOrPhone()) {
            return;
        }
        checkCallerIsSameApp(str);
    }

    private boolean isCallerAndroid(String str, int i) {
        return isUidSystemOrPhone(i) && str != null && PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRestrictedCategories(Notification notification) {
        try {
            if (!this.mPackageManager.hasSystemFeature("android.hardware.type.automotive", 0)) {
                return;
            }
        } catch (RemoteException unused) {
            if (DBG) {
                Slog.e(TAG, "Unable to confirm if it's safe to skip category restrictions check thus the check will be done anyway");
            }
        }
        if ("car_emergency".equals(notification.category) || "car_warning".equals(notification.category) || "car_information".equals(notification.category)) {
            getContext().enforceCallingPermission("android.permission.SEND_CATEGORY_CAR_NOTIFICATIONS", String.format("Notification category %s restricted", notification.category));
        }
    }

    @VisibleForTesting
    boolean isCallerInstantApp(int i, int i2) {
        if (isUidSystemOrPhone(i)) {
            return false;
        }
        if (i2 == -1) {
            i2 = 0;
        }
        try {
            String[] packagesForUid = this.mPackageManager.getPackagesForUid(i);
            if (packagesForUid == null) {
                throw new SecurityException("Unknown uid " + i);
            }
            String str = packagesForUid[0];
            this.mAppOps.checkPackage(i, str);
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str, 0L, i2);
            if (applicationInfo == null) {
                throw new SecurityException("Unknown package " + str);
            }
            return applicationInfo.isInstantApp();
        } catch (RemoteException e) {
            throw new SecurityException("Unknown uid " + i, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSameApp(String str) {
        checkCallerIsSameApp(str, Binder.getCallingUid(), UserHandle.getCallingUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSameApp(String str, int i, int i2) {
        if ((i == 0 && ROOT_PKG.equals(str)) || this.mPackageManagerInternal.isSameApp(str, i, i2)) {
            return;
        }
        throw new SecurityException("Package " + str + " is not owned by uid " + i);
    }

    private boolean isCallerSameApp(String str, int i, int i2) {
        try {
            checkCallerIsSameApp(str, i, i2);
            return true;
        } catch (SecurityException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String callStateToString(int i) {
        if (i == 0) {
            return "CALL_STATE_IDLE";
        }
        if (i == 1) {
            return "CALL_STATE_RINGING";
        }
        if (i == 2) {
            return "CALL_STATE_OFFHOOK";
        }
        return "CALL_STATE_UNKNOWN_" + i;
    }

    @GuardedBy({"mNotificationLock"})
    NotificationRankingUpdate makeRankingUpdateLocked(ManagedServices.ManagedServiceInfo managedServiceInfo) {
        int size = this.mNotificationList.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            NotificationRecord notificationRecord = this.mNotificationList.get(i);
            if (!isInLockDownMode(notificationRecord.getUser().getIdentifier()) && isVisibleToListener(notificationRecord.getSbn(), notificationRecord.getNotificationType(), managedServiceInfo)) {
                String key = notificationRecord.getSbn().getKey();
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                ranking.populate(key, arrayList.size(), !notificationRecord.isIntercepted(), notificationRecord.getPackageVisibilityOverride(), notificationRecord.getSuppressedVisualEffects(), notificationRecord.getImportance(), notificationRecord.getImportanceExplanation(), notificationRecord.getSbn().getOverrideGroupKey(), notificationRecord.getChannel(), notificationRecord.getPeopleOverride(), notificationRecord.getSnoozeCriteria(), notificationRecord.canShowBadge(), notificationRecord.getUserSentiment(), notificationRecord.isHidden(), notificationRecord.getLastAudiblyAlertedMs(), (notificationRecord.getSound() == null && notificationRecord.getVibration() == null) ? false : true, notificationRecord.getSystemGeneratedSmartActions(), notificationRecord.getSmartReplies(), notificationRecord.canBubble(), notificationRecord.isTextChanged(), notificationRecord.isConversation(), notificationRecord.getShortcutInfo(), notificationRecord.getRankingScore() == 0.0f ? 0 : notificationRecord.getRankingScore() > 0.0f ? 1 : -1, notificationRecord.getNotification().isBubbleNotification(), notificationRecord.getProposedImportance(), notificationRecord.hasSensitiveContent());
                arrayList.add(ranking);
            }
        }
        return new NotificationRankingUpdate((NotificationListenerService.Ranking[]) arrayList.toArray(new NotificationListenerService.Ranking[0]));
    }

    boolean isInLockDownMode(int i) {
        return this.mStrongAuthTracker.isInLockDownMode(i);
    }

    boolean hasCompanionDevice(ManagedServices.ManagedServiceInfo managedServiceInfo) {
        if (this.mCompanionManager == null) {
            this.mCompanionManager = getCompanionManager();
        }
        if (this.mCompanionManager == null) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
            } catch (RemoteException e) {
                Slog.e(TAG, "Cannot reach companion device service", e);
            } catch (SecurityException unused) {
            } catch (Exception e2) {
                Slog.e(TAG, "Cannot verify listener " + managedServiceInfo, e2);
            }
            if (!ArrayUtils.isEmpty(this.mCompanionManager.getAssociations(managedServiceInfo.component.getPackageName(), managedServiceInfo.userid))) {
                return true;
            }
            if (this.mNMSWrapper.getNMSExt() != null) {
                if (this.mNMSWrapper.getNMSExt().canListenNotificationChannelChange(managedServiceInfo.component.getPackageName())) {
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    protected ICompanionDeviceManager getCompanionManager() {
        return ICompanionDeviceManager.Stub.asInterface(ServiceManager.getService("companiondevice"));
    }

    @VisibleForTesting
    boolean isVisibleToListener(StatusBarNotification statusBarNotification, int i, ManagedServices.ManagedServiceInfo managedServiceInfo) {
        if ((this.mNMSWrapper.getNMSExt() != null && !this.mNMSWrapper.getNMSExt().enabledAndUserMatches(statusBarNotification, managedServiceInfo)) || !isInteractionVisibleToListener(managedServiceInfo, statusBarNotification.getUserId())) {
            return false;
        }
        NotificationListenerFilter notificationListenerFilter = this.mListeners.getNotificationListenerFilter(managedServiceInfo.mKey);
        if (notificationListenerFilter != null) {
            return notificationListenerFilter.isTypeAllowed(i) && notificationListenerFilter.isPackageAllowed(new VersionedPackage(statusBarNotification.getPackageName(), statusBarNotification.getUid()));
        }
        return true;
    }

    @VisibleForTesting
    boolean isInteractionVisibleToListener(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
        return !isServiceTokenValid(managedServiceInfo.getService()) || managedServiceInfo.isSameUser(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isServiceTokenValid(IInterface iInterface) {
        boolean isServiceTokenValidLocked;
        synchronized (this.mNotificationLock) {
            isServiceTokenValidLocked = this.mAssistants.isServiceTokenValidLocked(iInterface);
        }
        return isServiceTokenValidLocked;
    }

    private boolean isPackageSuspendedForUser(String str, int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                return this.mPackageManager.isPackageSuspendedForUser(str, UserHandle.getUserId(i));
            } catch (RemoteException unused) {
                throw new SecurityException("Could not talk to package manager service");
            } catch (IllegalArgumentException unused2) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean canUseManagedServices(String str, Integer num, String str2) {
        if (str2 == null) {
            return true;
        }
        try {
            return this.mPackageManager.checkPermission(str2, str, num.intValue()) == 0;
        } catch (RemoteException e) {
            Slog.e(TAG, "can't talk to pm", e);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class TrimCache {
        StatusBarNotification heavy;
        StatusBarNotification sbnClone;
        StatusBarNotification sbnCloneLight;

        TrimCache(StatusBarNotification statusBarNotification) {
            this.heavy = statusBarNotification;
        }

        StatusBarNotification ForListener(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            if (NotificationManagerService.this.mListeners.getOnNotificationPostedTrim(managedServiceInfo) == 1) {
                if (this.sbnCloneLight == null) {
                    this.sbnCloneLight = this.heavy.cloneLight();
                }
                return this.sbnCloneLight;
            }
            if (this.sbnClone == null) {
                this.sbnClone = this.heavy.clone();
            }
            return this.sbnClone;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInCall() {
        int mode;
        return this.mInCallStateOffHook || (mode = this.mAudioManager.getMode()) == 2 || mode == 3;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NotificationAssistants extends ManagedServices {
        private static final String ATT_TYPES = "types";
        static final String TAG_ENABLED_NOTIFICATION_ASSISTANTS = "enabled_assistants";

        @GuardedBy({"mLock"})
        private Set<String> mAllowedAdjustments;
        protected ComponentName mDefaultFromConfig;
        private final Object mLock;

        @Override // com.android.server.notification.ManagedServices
        protected void ensureFilters(ServiceInfo serviceInfo, int i) {
        }

        @Override // com.android.server.notification.ManagedServices
        protected String getRequiredPermission() {
            return "android.permission.REQUEST_NOTIFICATION_ASSISTANT_SERVICE";
        }

        @Override // com.android.server.notification.ManagedServices
        protected void loadDefaultsFromConfig() {
            loadDefaultsFromConfig(true);
        }

        protected void loadDefaultsFromConfig(boolean z) {
            ArraySet arraySet = new ArraySet();
            arraySet.addAll(Arrays.asList(this.mContext.getResources().getString(R.string.config_mainBuiltInDisplayCutoutRectApproximation).split(":")));
            for (int i = 0; i < arraySet.size(); i++) {
                ComponentName unflattenFromString = ComponentName.unflattenFromString((String) arraySet.valueAt(i));
                String str = (String) arraySet.valueAt(i);
                if (unflattenFromString != null) {
                    str = unflattenFromString.getPackageName();
                }
                if (!TextUtils.isEmpty(str) && queryPackageForServices(str, 786432, 0).contains(unflattenFromString)) {
                    if (z) {
                        addDefaultComponentOrPackage(unflattenFromString.flattenToString());
                    } else {
                        this.mDefaultFromConfig = unflattenFromString;
                    }
                }
            }
        }

        ComponentName getDefaultFromConfig() {
            if (this.mDefaultFromConfig == null) {
                loadDefaultsFromConfig(false);
            }
            return this.mDefaultFromConfig;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void upgradeUserSet() {
            Iterator<Integer> it = this.mApproved.keySet().iterator();
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                ArraySet<String> arraySet = this.mUserSetServices.get(Integer.valueOf(intValue));
                this.mIsUserChanged.put(Integer.valueOf(intValue), Boolean.valueOf(arraySet != null && arraySet.size() > 0));
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void addApprovedList(String str, int i, boolean z, String str2) {
            if (!TextUtils.isEmpty(str)) {
                String[] split = str.split(":");
                if (split.length > 1) {
                    Slog.d(this.TAG, "More than one approved assistants");
                    str = split[0];
                }
            }
            super.addApprovedList(str, i, z, str2);
        }

        public NotificationAssistants(Context context, Object obj, ManagedServices.UserProfiles userProfiles, IPackageManager iPackageManager) {
            super(context, obj, userProfiles, iPackageManager);
            this.mLock = new Object();
            this.mAllowedAdjustments = new ArraySet();
            this.mDefaultFromConfig = null;
            int i = 0;
            while (true) {
                String[] strArr = NotificationManagerService.ALLOWED_ADJUSTMENTS;
                if (i >= strArr.length) {
                    return;
                }
                this.mAllowedAdjustments.add(strArr[i]);
                i++;
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected ManagedServices.Config getConfig() {
            ManagedServices.Config config = new ManagedServices.Config();
            config.caption = "notification assistant";
            config.serviceInterface = "android.service.notification.NotificationAssistantService";
            config.xmlTag = TAG_ENABLED_NOTIFICATION_ASSISTANTS;
            config.secureSettingName = "enabled_notification_assistant";
            config.bindPermission = "android.permission.BIND_NOTIFICATION_ASSISTANT_SERVICE";
            config.settingsAction = "android.settings.MANAGE_DEFAULT_APPS_SETTINGS";
            config.clientLabel = R.string.permlab_requestDeletePackages;
            return config;
        }

        @Override // com.android.server.notification.ManagedServices
        protected IInterface asInterface(IBinder iBinder) {
            return INotificationListener.Stub.asInterface(iBinder);
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean checkType(IInterface iInterface) {
            return iInterface instanceof INotificationListener;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void onServiceAdded(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            NotificationManagerService.this.mListeners.registerGuestService(managedServiceInfo);
        }

        @Override // com.android.server.notification.ManagedServices
        @GuardedBy({"mNotificationLock"})
        protected void onServiceRemovedLocked(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            NotificationManagerService.this.mListeners.unregisterService(managedServiceInfo.service, managedServiceInfo.userid);
        }

        @Override // com.android.server.notification.ManagedServices
        public void onUserUnlocked(int i) {
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                Slog.d(this.TAG, "onUserUnlocked u=" + i);
            }
            rebindServices(true, i);
            if (NotificationManagerService.this.mNMSWrapper.getNMSExt() == null || i != NotificationManagerService.this.mNMSWrapper.getNMSExt().getMultiAppUserId()) {
                return;
            }
            Slog.d(this.TAG, "notification assistant : return for multi app");
        }

        protected List<String> getAllowedAssistantAdjustments() {
            ArrayList arrayList;
            synchronized (this.mLock) {
                arrayList = new ArrayList();
                arrayList.addAll(this.mAllowedAdjustments);
            }
            return arrayList;
        }

        protected boolean isAdjustmentAllowed(String str) {
            boolean contains;
            synchronized (this.mLock) {
                contains = this.mAllowedAdjustments.contains(str);
            }
            return contains;
        }

        protected void onNotificationsSeenLocked(ArrayList<NotificationRecord> arrayList) {
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                final ArrayList arrayList2 = new ArrayList(arrayList.size());
                Iterator<NotificationRecord> it = arrayList.iterator();
                while (it.hasNext()) {
                    NotificationRecord next = it.next();
                    if (NotificationManagerService.this.isVisibleToListener(next.getSbn(), next.getNotificationType(), managedServiceInfo) && managedServiceInfo.isSameUser(next.getUserId())) {
                        arrayList2.add(next.getKey());
                    }
                }
                if (!arrayList2.isEmpty()) {
                    NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationManagerService.NotificationAssistants.this.lambda$onNotificationsSeenLocked$0(managedServiceInfo, arrayList2);
                        }
                    });
                }
            }
        }

        protected void onPanelRevealed(final int i) {
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationManagerService.NotificationAssistants.this.lambda$onPanelRevealed$1(managedServiceInfo, i);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPanelRevealed$1(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
            try {
                managedServiceInfo.service.onPanelRevealed(i);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (panel revealed): " + managedServiceInfo, e);
            }
        }

        protected void onPanelHidden() {
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationManagerService.NotificationAssistants.this.lambda$onPanelHidden$2(managedServiceInfo);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPanelHidden$2(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            try {
                managedServiceInfo.service.onPanelHidden();
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (panel hidden): " + managedServiceInfo, e);
            }
        }

        boolean hasUserSet(int i) {
            Boolean bool = this.mIsUserChanged.get(Integer.valueOf(i));
            return bool != null && bool.booleanValue();
        }

        void setUserSet(int i, boolean z) {
            this.mIsUserChanged.put(Integer.valueOf(i), Boolean.valueOf(z));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: notifySeen, reason: merged with bridge method [inline-methods] */
        public void lambda$onNotificationsSeenLocked$0(ManagedServices.ManagedServiceInfo managedServiceInfo, ArrayList<String> arrayList) {
            try {
                managedServiceInfo.service.onNotificationsSeen(arrayList);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (seen): " + managedServiceInfo, e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        @GuardedBy({"mNotificationLock"})
        public void onNotificationEnqueuedLocked(NotificationRecord notificationRecord) {
            boolean isVerboseLogEnabled = isVerboseLogEnabled();
            if (isVerboseLogEnabled) {
                Slog.v(this.TAG, "onNotificationEnqueuedLocked() called with: r = [" + notificationRecord + "]");
            }
            StatusBarNotification sbn = notificationRecord.getSbn();
            for (ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (NotificationManagerService.this.isVisibleToListener(sbn, notificationRecord.getNotificationType(), managedServiceInfo) && managedServiceInfo.isSameUser(notificationRecord.getUserId())) {
                    TrimCache trimCache = new TrimCache(sbn);
                    INotificationListener iNotificationListener = managedServiceInfo.service;
                    StatusBarNotificationHolder statusBarNotificationHolder = new StatusBarNotificationHolder(trimCache.ForListener(managedServiceInfo));
                    if (isVerboseLogEnabled) {
                        try {
                            Slog.v(this.TAG, "calling onNotificationEnqueuedWithChannel " + statusBarNotificationHolder);
                        } catch (RemoteException e) {
                            Slog.e(this.TAG, "unable to notify assistant (enqueued): " + iNotificationListener, e);
                            if ((e instanceof TransactionTooLargeException) && NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                                NotificationManagerService.this.mNMSWrapper.getNMSExt().onClearAllNotifications(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, -1);
                            }
                        }
                    }
                    iNotificationListener.onNotificationEnqueuedWithChannel(statusBarNotificationHolder, notificationRecord.getChannel(), NotificationManagerService.this.makeRankingUpdateLocked(managedServiceInfo));
                }
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantVisibilityChangedLocked(NotificationRecord notificationRecord, final boolean z) {
            final String key = notificationRecord.getSbn().getKey();
            if (NotificationManagerService.DBG) {
                Slog.d(this.TAG, "notifyAssistantVisibilityChangedLocked: " + key);
            }
            notifyAssistantLocked(notificationRecord.getSbn(), notificationRecord.getNotificationType(), true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda8
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantVisibilityChangedLocked$3(key, z, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantVisibilityChangedLocked$3(String str, boolean z, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onNotificationVisibilityChanged(str, z);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (visible): " + iNotificationListener, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantExpansionChangedLocked(StatusBarNotification statusBarNotification, int i, final boolean z, final boolean z2) {
            final String key = statusBarNotification.getKey();
            notifyAssistantLocked(statusBarNotification, i, true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda10
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantExpansionChangedLocked$4(key, z, z2, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantExpansionChangedLocked$4(String str, boolean z, boolean z2, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onNotificationExpansionChanged(str, z, z2);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (expanded): " + iNotificationListener, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantNotificationDirectReplyLocked(NotificationRecord notificationRecord) {
            final String key = notificationRecord.getKey();
            notifyAssistantLocked(notificationRecord.getSbn(), notificationRecord.getNotificationType(), true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda6
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantNotificationDirectReplyLocked$5(key, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantNotificationDirectReplyLocked$5(String str, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onNotificationDirectReply(str);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (expanded): " + iNotificationListener, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantSuggestedReplySent(StatusBarNotification statusBarNotification, int i, final CharSequence charSequence, final boolean z) {
            final String key = statusBarNotification.getKey();
            notifyAssistantLocked(statusBarNotification, i, true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantSuggestedReplySent$6(key, charSequence, z, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantSuggestedReplySent$6(String str, CharSequence charSequence, boolean z, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onSuggestedReplySent(str, charSequence, z ? 1 : 0);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (snoozed): " + iNotificationListener, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantActionClicked(NotificationRecord notificationRecord, final Notification.Action action, final boolean z) {
            final String key = notificationRecord.getSbn().getKey();
            notifyAssistantLocked(notificationRecord.getSbn(), notificationRecord.getNotificationType(), true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda9
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantActionClicked$7(key, action, z, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantActionClicked$7(String str, Notification.Action action, boolean z, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onActionClicked(str, action, z ? 1 : 0);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (snoozed): " + iNotificationListener, e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        @GuardedBy({"mNotificationLock"})
        public void notifyAssistantSnoozedLocked(NotificationRecord notificationRecord, final String str) {
            notifyAssistantLocked(notificationRecord.getSbn(), notificationRecord.getNotificationType(), true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda5
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantSnoozedLocked$8(str, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantSnoozedLocked$8(String str, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onNotificationSnoozedUntilContext(statusBarNotificationHolder, str);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (snoozed): " + iNotificationListener, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantNotificationClicked(NotificationRecord notificationRecord) {
            final String key = notificationRecord.getSbn().getKey();
            notifyAssistantLocked(notificationRecord.getSbn(), notificationRecord.getNotificationType(), true, new BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda7
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    NotificationManagerService.NotificationAssistants.this.lambda$notifyAssistantNotificationClicked$9(key, (INotificationListener) obj, (NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantNotificationClicked$9(String str, INotificationListener iNotificationListener, StatusBarNotificationHolder statusBarNotificationHolder) {
            try {
                iNotificationListener.onNotificationClicked(str);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify assistant (clicked): " + iNotificationListener, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        void notifyAssistantFeedbackReceived(NotificationRecord notificationRecord, Bundle bundle) {
            StatusBarNotification sbn = notificationRecord.getSbn();
            for (ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (NotificationManagerService.this.isVisibleToListener(sbn, notificationRecord.getNotificationType(), managedServiceInfo) && managedServiceInfo.isSameUser(notificationRecord.getUserId())) {
                    INotificationListener iNotificationListener = managedServiceInfo.service;
                    try {
                        iNotificationListener.onNotificationFeedbackReceived(sbn.getKey(), NotificationManagerService.this.makeRankingUpdateLocked(managedServiceInfo), bundle);
                    } catch (RemoteException e) {
                        Slog.e(this.TAG, "unable to notify assistant (feedback): " + iNotificationListener, e);
                    }
                }
            }
        }

        @GuardedBy({"mNotificationLock"})
        private void notifyAssistantLocked(StatusBarNotification statusBarNotification, int i, boolean z, final BiConsumer<INotificationListener, StatusBarNotificationHolder> biConsumer) {
            TrimCache trimCache = new TrimCache(statusBarNotification);
            boolean isVerboseLogEnabled = isVerboseLogEnabled();
            if (isVerboseLogEnabled) {
                Slog.v(this.TAG, "notifyAssistantLocked() called with: sbn = [" + statusBarNotification + "], sameUserOnly = [" + z + "], callback = [" + biConsumer + "]");
            }
            for (ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                boolean z2 = NotificationManagerService.this.isVisibleToListener(statusBarNotification, i, managedServiceInfo) && (!z || managedServiceInfo.isSameUser(statusBarNotification.getUserId()));
                if (isVerboseLogEnabled) {
                    Slog.v(this.TAG, "notifyAssistantLocked info=" + managedServiceInfo + " snbVisible=" + z2);
                }
                if (z2) {
                    final INotificationListener iNotificationListener = managedServiceInfo.service;
                    final StatusBarNotificationHolder statusBarNotificationHolder = new StatusBarNotificationHolder(trimCache.ForListener(managedServiceInfo));
                    NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            biConsumer.accept(iNotificationListener, statusBarNotificationHolder);
                        }
                    });
                }
            }
        }

        public boolean isEnabled() {
            return !getServices().isEmpty();
        }

        protected void resetDefaultAssistantsIfNecessary() {
            Iterator it = this.mUm.getAliveUsers().iterator();
            while (it.hasNext()) {
                int identifier = ((UserInfo) it.next()).getUserHandle().getIdentifier();
                if (!hasUserSet(identifier)) {
                    if (!NotificationManagerService.this.isNASMigrationDone(identifier)) {
                        resetDefaultFromConfig();
                        NotificationManagerService.this.setNASMigrationDone(identifier);
                    }
                    Slog.d(this.TAG, "Approving default notification assistant for user " + identifier);
                    NotificationManagerService.this.setDefaultAssistantForUser(identifier);
                }
            }
        }

        protected void resetDefaultFromConfig() {
            clearDefaults();
            loadDefaultsFromConfig();
        }

        protected void clearDefaults() {
            this.mDefaultComponents.clear();
            this.mDefaultPackages.clear();
        }

        @Override // com.android.server.notification.ManagedServices
        protected void setPackageOrComponentEnabled(String str, int i, boolean z, boolean z2, boolean z3) {
            if (z2) {
                List<ComponentName> allowedComponents = getAllowedComponents(i);
                if (!allowedComponents.isEmpty()) {
                    ComponentName componentName = (ComponentName) CollectionUtils.firstOrNull(allowedComponents);
                    if (componentName.flattenToString().equals(str)) {
                        return;
                    } else {
                        NotificationManagerService.this.setNotificationAssistantAccessGrantedForUserInternal(componentName, i, false, z3);
                    }
                }
            }
            super.setPackageOrComponentEnabled(str, i, z, z2, z3);
        }

        private boolean isVerboseLogEnabled() {
            return Log.isLoggable("notification_assistant", 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mNotificationLock"})
    public void notifyListenersPostedAndLogLocked(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, final PostNotificationTracker postNotificationTracker, final NotificationRecordLogger.NotificationReported notificationReported) {
        final List<Runnable> prepareNotifyPostedLocked = this.mListeners.prepareNotifyPostedLocked(notificationRecord, notificationRecord2, true);
        this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                NotificationManagerService.this.lambda$notifyListenersPostedAndLogLocked$12(prepareNotifyPostedLocked, postNotificationTracker, notificationReported);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListenersPostedAndLogLocked$12(List list, PostNotificationTracker postNotificationTracker, NotificationRecordLogger.NotificationReported notificationReported) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        long finish = postNotificationTracker.finish();
        if (notificationReported != null) {
            notificationReported.post_duration_millis = finish;
            this.mNotificationRecordLogger.logNotificationPosted(notificationReported);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NotificationListeners extends ManagedServices {
        static final String ATT_COMPONENT = "component";
        static final String ATT_PKG = "pkg";
        static final String ATT_TYPES = "types";
        static final String ATT_UID = "uid";
        static final String FLAG_SEPARATOR = "\\|";
        static final String TAG_APPROVED = "allowed";
        static final String TAG_DISALLOWED = "disallowed";
        static final String TAG_ENABLED_NOTIFICATION_LISTENERS = "enabled_listeners";
        static final String TAG_REQUESTED_LISTENER = "listener";
        static final String TAG_REQUESTED_LISTENERS = "request_listeners";
        static final String XML_SEPARATOR = ",";
        private final boolean mIsHeadlessSystemUserMode;
        private final ArraySet<ManagedServices.ManagedServiceInfo> mLightTrimListeners;

        @GuardedBy({"mRequestedNotificationListeners"})
        private final ArrayMap<Pair<ComponentName, Integer>, NotificationListenerFilter> mRequestedNotificationListeners;

        @Override // com.android.server.notification.ManagedServices
        protected int getBindFlags() {
            return 83886337;
        }

        @Override // com.android.server.notification.ManagedServices
        protected String getRequiredPermission() {
            return null;
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean shouldReflectToSettings() {
            return true;
        }

        public NotificationListeners(NotificationManagerService notificationManagerService, Context context, Object obj, ManagedServices.UserProfiles userProfiles, IPackageManager iPackageManager) {
            this(context, obj, userProfiles, iPackageManager, UserManager.isHeadlessSystemUserMode());
        }

        @VisibleForTesting
        public NotificationListeners(Context context, Object obj, ManagedServices.UserProfiles userProfiles, IPackageManager iPackageManager, boolean z) {
            super(context, obj, userProfiles, iPackageManager);
            this.mLightTrimListeners = new ArraySet<>();
            this.mRequestedNotificationListeners = new ArrayMap<>();
            this.mIsHeadlessSystemUserMode = z;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void setPackageOrComponentEnabled(String str, int i, boolean z, boolean z2, boolean z3) {
            super.setPackageOrComponentEnabled(str, i, z, z2, z3);
            this.mContext.sendBroadcastAsUser(new Intent("android.app.action.NOTIFICATION_LISTENER_ENABLED_CHANGED").addFlags(1073741824), UserHandle.of(i), null);
        }

        @Override // com.android.server.notification.ManagedServices
        protected void loadDefaultsFromConfig() {
            String string = this.mContext.getResources().getString(R.string.config_ntpServer);
            if (string != null) {
                String[] split = string.split(":");
                for (int i = 0; i < split.length; i++) {
                    if (!TextUtils.isEmpty(split[i])) {
                        ArraySet<ComponentName> queryPackageForServices = queryPackageForServices(split[i], this.mIsHeadlessSystemUserMode ? 4980736 : 786432, 0);
                        for (int i2 = 0; i2 < queryPackageForServices.size(); i2++) {
                            addDefaultComponentOrPackage(queryPackageForServices.valueAt(i2).flattenToString());
                        }
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected ManagedServices.Config getConfig() {
            ManagedServices.Config config = new ManagedServices.Config();
            config.caption = "notification listener";
            config.serviceInterface = "android.service.notification.NotificationListenerService";
            config.xmlTag = TAG_ENABLED_NOTIFICATION_LISTENERS;
            config.secureSettingName = "enabled_notification_listeners";
            config.bindPermission = "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE";
            config.settingsAction = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
            config.clientLabel = R.string.permlab_register_sim_subscription;
            return config;
        }

        @Override // com.android.server.notification.ManagedServices
        protected IInterface asInterface(IBinder iBinder) {
            return INotificationListener.Stub.asInterface(iBinder);
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean checkType(IInterface iInterface) {
            return iInterface instanceof INotificationListener;
        }

        @Override // com.android.server.notification.ManagedServices
        public void onServiceAdded(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            NotificationRankingUpdate makeRankingUpdateLocked;
            INotificationListener iNotificationListener = managedServiceInfo.service;
            synchronized (NotificationManagerService.this.mNotificationLock) {
                makeRankingUpdateLocked = NotificationManagerService.this.makeRankingUpdateLocked(managedServiceInfo);
                updateUriPermissionsForActiveNotificationsLocked(managedServiceInfo, true);
            }
            try {
                iNotificationListener.onListenerConnected(makeRankingUpdateLocked);
            } catch (RemoteException unused) {
            }
        }

        @Override // com.android.server.notification.ManagedServices
        @GuardedBy({"mNotificationLock"})
        protected void onServiceRemovedLocked(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            updateUriPermissionsForActiveNotificationsLocked(managedServiceInfo, false);
            if (NotificationManagerService.this.removeDisabledHints(managedServiceInfo)) {
                NotificationManagerService.this.updateListenerHintsLocked();
                NotificationManagerService.this.updateEffectsSuppressorLocked();
            }
            this.mLightTrimListeners.remove(managedServiceInfo);
        }

        @Override // com.android.server.notification.ManagedServices
        public void onUserRemoved(int i) {
            super.onUserRemoved(i);
            synchronized (this.mRequestedNotificationListeners) {
                for (int size = this.mRequestedNotificationListeners.size() - 1; size >= 0; size--) {
                    if (((Integer) this.mRequestedNotificationListeners.keyAt(size).second).intValue() == i) {
                        this.mRequestedNotificationListeners.removeAt(size);
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        public void onPackagesChanged(boolean z, String[] strArr, int[] iArr) {
            super.onPackagesChanged(z, strArr, iArr);
            synchronized (this.mRequestedNotificationListeners) {
                if (z) {
                    for (int i = 0; i < strArr.length; i++) {
                        String str = strArr[i];
                        int userId = UserHandle.getUserId(iArr[i]);
                        for (int size = this.mRequestedNotificationListeners.size() - 1; size >= 0; size--) {
                            Pair<ComponentName, Integer> keyAt = this.mRequestedNotificationListeners.keyAt(size);
                            if (((Integer) keyAt.second).intValue() == userId && ((ComponentName) keyAt.first).getPackageName().equals(str)) {
                                this.mRequestedNotificationListeners.removeAt(size);
                            }
                        }
                    }
                }
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    String str2 = strArr[i2];
                    for (int size2 = this.mRequestedNotificationListeners.size() - 1; size2 >= 0; size2--) {
                        this.mRequestedNotificationListeners.valueAt(size2).removePackage(new VersionedPackage(str2, iArr[i2]));
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void readExtraTag(String str, TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
            if (TAG_REQUESTED_LISTENERS.equals(str)) {
                int depth = typedXmlPullParser.getDepth();
                while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                    if (TAG_REQUESTED_LISTENER.equals(typedXmlPullParser.getName())) {
                        int readIntAttribute = XmlUtils.readIntAttribute(typedXmlPullParser, "user");
                        ComponentName unflattenFromString = ComponentName.unflattenFromString(XmlUtils.readStringAttribute(typedXmlPullParser, ATT_COMPONENT));
                        ArraySet arraySet = new ArraySet();
                        int depth2 = typedXmlPullParser.getDepth();
                        int i = 15;
                        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth2)) {
                            if (TAG_APPROVED.equals(typedXmlPullParser.getName())) {
                                i = XmlUtils.readIntAttribute(typedXmlPullParser, ATT_TYPES);
                            } else if (TAG_DISALLOWED.equals(typedXmlPullParser.getName())) {
                                String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, ATT_PKG);
                                int readIntAttribute2 = XmlUtils.readIntAttribute(typedXmlPullParser, "uid");
                                if (!TextUtils.isEmpty(readStringAttribute)) {
                                    arraySet.add(new VersionedPackage(readStringAttribute, readIntAttribute2));
                                }
                            }
                        }
                        NotificationListenerFilter notificationListenerFilter = new NotificationListenerFilter(i, arraySet);
                        synchronized (this.mRequestedNotificationListeners) {
                            this.mRequestedNotificationListeners.put(Pair.create(unflattenFromString, Integer.valueOf(readIntAttribute)), notificationListenerFilter);
                        }
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void writeExtraXmlTags(TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.startTag((String) null, TAG_REQUESTED_LISTENERS);
            synchronized (this.mRequestedNotificationListeners) {
                for (Pair<ComponentName, Integer> pair : this.mRequestedNotificationListeners.keySet()) {
                    NotificationListenerFilter notificationListenerFilter = this.mRequestedNotificationListeners.get(pair);
                    typedXmlSerializer.startTag((String) null, TAG_REQUESTED_LISTENER);
                    XmlUtils.writeStringAttribute(typedXmlSerializer, ATT_COMPONENT, ((ComponentName) pair.first).flattenToString());
                    XmlUtils.writeIntAttribute(typedXmlSerializer, "user", ((Integer) pair.second).intValue());
                    typedXmlSerializer.startTag((String) null, TAG_APPROVED);
                    XmlUtils.writeIntAttribute(typedXmlSerializer, ATT_TYPES, notificationListenerFilter.getTypes());
                    typedXmlSerializer.endTag((String) null, TAG_APPROVED);
                    Iterator it = notificationListenerFilter.getDisallowedPackages().iterator();
                    while (it.hasNext()) {
                        VersionedPackage versionedPackage = (VersionedPackage) it.next();
                        if (!TextUtils.isEmpty(versionedPackage.getPackageName())) {
                            typedXmlSerializer.startTag((String) null, TAG_DISALLOWED);
                            XmlUtils.writeStringAttribute(typedXmlSerializer, ATT_PKG, versionedPackage.getPackageName());
                            XmlUtils.writeIntAttribute(typedXmlSerializer, "uid", versionedPackage.getVersionCode());
                            typedXmlSerializer.endTag((String) null, TAG_DISALLOWED);
                        }
                    }
                    typedXmlSerializer.endTag((String) null, TAG_REQUESTED_LISTENER);
                }
            }
            typedXmlSerializer.endTag((String) null, TAG_REQUESTED_LISTENERS);
        }

        protected NotificationListenerFilter getNotificationListenerFilter(Pair<ComponentName, Integer> pair) {
            NotificationListenerFilter notificationListenerFilter;
            synchronized (this.mRequestedNotificationListeners) {
                notificationListenerFilter = this.mRequestedNotificationListeners.get(pair);
            }
            return notificationListenerFilter;
        }

        protected void setNotificationListenerFilter(Pair<ComponentName, Integer> pair, NotificationListenerFilter notificationListenerFilter) {
            synchronized (this.mRequestedNotificationListeners) {
                this.mRequestedNotificationListeners.put(pair, notificationListenerFilter);
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void ensureFilters(ServiceInfo serviceInfo, int i) {
            int typesFromStringList;
            String obj;
            Pair<ComponentName, Integer> create = Pair.create(serviceInfo.getComponentName(), Integer.valueOf(i));
            synchronized (this.mRequestedNotificationListeners) {
                NotificationListenerFilter notificationListenerFilter = this.mRequestedNotificationListeners.get(create);
                Bundle bundle = serviceInfo.metaData;
                if (bundle != null) {
                    if (notificationListenerFilter == null && bundle.containsKey("android.service.notification.default_filter_types") && (obj = serviceInfo.metaData.get("android.service.notification.default_filter_types").toString()) != null) {
                        this.mRequestedNotificationListeners.put(create, new NotificationListenerFilter(getTypesFromStringList(obj), new ArraySet()));
                    }
                    if (serviceInfo.metaData.containsKey("android.service.notification.disabled_filter_types") && (typesFromStringList = getTypesFromStringList(serviceInfo.metaData.get("android.service.notification.disabled_filter_types").toString())) != 0) {
                        NotificationListenerFilter orDefault = this.mRequestedNotificationListeners.getOrDefault(create, new NotificationListenerFilter());
                        orDefault.setTypes((~typesFromStringList) & orDefault.getTypes());
                        this.mRequestedNotificationListeners.put(create, orDefault);
                    }
                }
            }
        }

        private int getTypesFromStringList(String str) {
            if (str == null) {
                return 0;
            }
            int i = 0;
            for (String str2 : str.split(FLAG_SEPARATOR)) {
                if (!TextUtils.isEmpty(str2)) {
                    if (str2.equalsIgnoreCase("ONGOING")) {
                        i |= 8;
                    } else if (str2.equalsIgnoreCase("CONVERSATIONS")) {
                        i |= 1;
                    } else if (str2.equalsIgnoreCase("SILENT")) {
                        i |= 4;
                    } else if (str2.equalsIgnoreCase("ALERTING")) {
                        i |= 2;
                    } else {
                        try {
                            i |= Integer.parseInt(str2);
                        } catch (NumberFormatException unused) {
                        }
                    }
                }
            }
            return i;
        }

        @GuardedBy({"mNotificationLock"})
        public void setOnNotificationPostedTrimLocked(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
            if (i == 1) {
                this.mLightTrimListeners.add(managedServiceInfo);
            } else {
                this.mLightTrimListeners.remove(managedServiceInfo);
            }
        }

        public int getOnNotificationPostedTrim(ManagedServices.ManagedServiceInfo managedServiceInfo) {
            return this.mLightTrimListeners.contains(managedServiceInfo) ? 1 : 0;
        }

        public void onStatusBarIconsBehaviorChanged(final boolean z) {
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationManagerService.NotificationListeners.this.lambda$onStatusBarIconsBehaviorChanged$0(managedServiceInfo, z);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarIconsBehaviorChanged$0(ManagedServices.ManagedServiceInfo managedServiceInfo, boolean z) {
            try {
                managedServiceInfo.service.onStatusBarIconsBehaviorChanged(z);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify listener (hideSilentStatusIcons): " + managedServiceInfo, e);
            }
        }

        @GuardedBy({"mNotificationLock"})
        @VisibleForTesting
        void notifyPostedLocked(NotificationRecord notificationRecord, NotificationRecord notificationRecord2) {
            notifyPostedLocked(notificationRecord, notificationRecord2, true);
        }

        @GuardedBy({"mNotificationLock"})
        @VisibleForTesting
        private void notifyPostedLocked(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, boolean z) {
            Iterator<Runnable> it = prepareNotifyPostedLocked(notificationRecord, notificationRecord2, z).iterator();
            while (it.hasNext()) {
                NotificationManagerService.this.mHandler.post(it.next());
            }
        }

        @GuardedBy({"mNotificationLock"})
        @VisibleForTesting
        List<Runnable> prepareNotifyPostedLocked(NotificationRecord notificationRecord, NotificationRecord notificationRecord2, boolean z) {
            if (NotificationManagerService.this.isInLockDownMode(notificationRecord.getUser().getIdentifier())) {
                return new ArrayList();
            }
            ArrayList arrayList = new ArrayList();
            try {
                StatusBarNotification sbn = notificationRecord.getSbn();
                StatusBarNotification sbn2 = notificationRecord2 != null ? notificationRecord2.getSbn() : null;
                TrimCache trimCache = new TrimCache(sbn);
                for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                    boolean isVisibleToListener = NotificationManagerService.this.isVisibleToListener(sbn, notificationRecord.getNotificationType(), managedServiceInfo);
                    boolean z2 = sbn2 != null && NotificationManagerService.this.isVisibleToListener(sbn2, notificationRecord2.getNotificationType(), managedServiceInfo);
                    if (z2 || isVisibleToListener) {
                        if (!notificationRecord.isHidden() || managedServiceInfo.targetSdkVersion >= 28) {
                            if (z || managedServiceInfo.targetSdkVersion < 28) {
                                final NotificationRankingUpdate makeRankingUpdateLocked = NotificationManagerService.this.makeRankingUpdateLocked(managedServiceInfo);
                                if (z2 && !isVisibleToListener) {
                                    final StatusBarNotification cloneLight = sbn2.cloneLight();
                                    arrayList.add(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda3
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            NotificationManagerService.NotificationListeners.this.lambda$prepareNotifyPostedLocked$1(managedServiceInfo, cloneLight, makeRankingUpdateLocked);
                                        }
                                    });
                                } else {
                                    int i = managedServiceInfo.userid;
                                    int i2 = i == -1 ? 0 : i;
                                    try {
                                        NotificationManagerService.this.updateUriPermissions(notificationRecord, notificationRecord2, managedServiceInfo.component.getPackageName(), i2);
                                        NotificationManagerService.this.mPackageManagerInternal.grantImplicitAccess(i2, (Intent) null, UserHandle.getAppId(managedServiceInfo.uid), sbn.getUid(), false, false);
                                        final StatusBarNotification ForListener = trimCache.ForListener(managedServiceInfo);
                                        arrayList.add(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda4
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                NotificationManagerService.NotificationListeners.this.lambda$prepareNotifyPostedLocked$2(managedServiceInfo, ForListener, makeRankingUpdateLocked);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e = e;
                                        Slog.e(this.TAG, "Could not notify listeners for " + notificationRecord.getKey(), e);
                                        return arrayList;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e2) {
                e = e2;
            }
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$prepareNotifyPostedLocked$1(ManagedServices.ManagedServiceInfo managedServiceInfo, StatusBarNotification statusBarNotification, NotificationRankingUpdate notificationRankingUpdate) {
            lambda$notifyRemovedLocked$4(managedServiceInfo, statusBarNotification, notificationRankingUpdate, null, 6);
        }

        @GuardedBy({"mNotificationLock"})
        private void updateUriPermissionsForActiveNotificationsLocked(ManagedServices.ManagedServiceInfo managedServiceInfo, boolean z) {
            try {
                Iterator<NotificationRecord> it = NotificationManagerService.this.mNotificationList.iterator();
                while (it.hasNext()) {
                    NotificationRecord next = it.next();
                    if (!z || NotificationManagerService.this.isVisibleToListener(next.getSbn(), next.getNotificationType(), managedServiceInfo)) {
                        if (!next.isHidden() || managedServiceInfo.targetSdkVersion >= 28) {
                            int i = managedServiceInfo.userid;
                            if (i == -1) {
                                i = 0;
                            }
                            int i2 = i;
                            if (z) {
                                NotificationManagerService.this.updateUriPermissions(next, null, managedServiceInfo.component.getPackageName(), i2);
                            } else {
                                NotificationManagerService.this.updateUriPermissions(null, next, managedServiceInfo.component.getPackageName(), i2, true);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Could not ");
                sb.append(z ? "grant" : "revoke");
                sb.append(" Uri permissions to ");
                sb.append(managedServiceInfo.component);
                Slog.e(str, sb.toString(), e);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0053, code lost:
        
            if (r2.targetSdkVersion < 28) goto L37;
         */
        @GuardedBy({"mNotificationLock"})
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void notifyRemovedLocked(final NotificationRecord notificationRecord, final int i, NotificationStats notificationStats) {
            if (NotificationManagerService.this.isInLockDownMode(notificationRecord.getUser().getIdentifier())) {
                return;
            }
            StatusBarNotification sbn = notificationRecord.getSbn();
            final StatusBarNotification cloneLight = sbn.cloneLight();
            Iterator<ManagedServices.ManagedServiceInfo> it = getServices().iterator();
            while (it.hasNext()) {
                final ManagedServices.ManagedServiceInfo next = it.next();
                if (NotificationManagerService.this.isVisibleToListener(sbn, notificationRecord.getNotificationType(), next) && (!notificationRecord.isHidden() || i == 14 || next.targetSdkVersion >= 28)) {
                    final NotificationStats notificationStats2 = NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(next.service) ? notificationStats : null;
                    final NotificationRankingUpdate makeRankingUpdateLocked = NotificationManagerService.this.makeRankingUpdateLocked(next);
                    if (NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                        NotificationManagerService.this.mNMSWrapper.getNMSExt().planNextRemove(NotificationManagerService.this.mHandler, new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationManagerService.NotificationListeners.this.lambda$notifyRemovedLocked$3(next, cloneLight, makeRankingUpdateLocked, notificationStats2, i);
                            }
                        }, sbn.getKey());
                    } else {
                        NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda6
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationManagerService.NotificationListeners.this.lambda$notifyRemovedLocked$4(next, cloneLight, makeRankingUpdateLocked, notificationStats2, i);
                            }
                        });
                    }
                }
            }
            NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationManagerService.NotificationListeners.this.lambda$notifyRemovedLocked$5(notificationRecord);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyRemovedLocked$5(NotificationRecord notificationRecord) {
            NotificationManagerService.this.updateUriPermissions(null, notificationRecord, null, 0);
        }

        @GuardedBy({"mNotificationLock"})
        public void notifyRankingUpdateLocked(List<NotificationRecord> list) {
            boolean z;
            boolean z2 = list != null && list.size() > 0;
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (managedServiceInfo.isEnabledForCurrentProfiles() && NotificationManagerService.this.isInteractionVisibleToListener(managedServiceInfo, ActivityManager.getCurrentUser())) {
                    if (z2 && managedServiceInfo.targetSdkVersion >= 28) {
                        for (NotificationRecord notificationRecord : list) {
                            if (NotificationManagerService.this.isVisibleToListener(notificationRecord.getSbn(), notificationRecord.getNotificationType(), managedServiceInfo)) {
                                z = true;
                                break;
                            }
                        }
                    }
                    z = false;
                    if (z || !z2) {
                        final NotificationRankingUpdate makeRankingUpdateLocked = NotificationManagerService.this.makeRankingUpdateLocked(managedServiceInfo);
                        NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationManagerService.NotificationListeners.this.lambda$notifyRankingUpdateLocked$6(managedServiceInfo, makeRankingUpdateLocked);
                            }
                        });
                    }
                }
            }
        }

        @GuardedBy({"mNotificationLock"})
        public void notifyListenerHintsChangedLocked(final int i) {
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (managedServiceInfo.isEnabledForCurrentProfiles() && NotificationManagerService.this.isInteractionVisibleToListener(managedServiceInfo, ActivityManager.getCurrentUser())) {
                    NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationManagerService.NotificationListeners.this.lambda$notifyListenerHintsChangedLocked$7(managedServiceInfo, i);
                        }
                    });
                }
            }
        }

        @GuardedBy({"mNotificationLock"})
        public void notifyHiddenLocked(List<NotificationRecord> list) {
            if (list == null || list.size() == 0) {
                return;
            }
            notifyRankingUpdateLocked(list);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NotificationRecord notificationRecord = list.get(i);
                NotificationManagerService.this.mListeners.notifyRemovedLocked(notificationRecord, 14, notificationRecord.getStats());
            }
        }

        @GuardedBy({"mNotificationLock"})
        public void notifyUnhiddenLocked(List<NotificationRecord> list) {
            if (list == null || list.size() == 0) {
                return;
            }
            notifyRankingUpdateLocked(list);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                NotificationRecord notificationRecord = list.get(i);
                notifyPostedLocked(notificationRecord, notificationRecord, false);
            }
        }

        public void notifyInterruptionFilterChanged(final int i) {
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (managedServiceInfo.isEnabledForCurrentProfiles() && NotificationManagerService.this.isInteractionVisibleToListener(managedServiceInfo, ActivityManager.getCurrentUser())) {
                    NotificationManagerService.this.mHandler.post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationManagerService.NotificationListeners.this.lambda$notifyInterruptionFilterChanged$8(managedServiceInfo, i);
                        }
                    });
                }
            }
        }

        protected void notifyNotificationChannelChanged(final String str, final UserHandle userHandle, final NotificationChannel notificationChannel, final int i) {
            if (notificationChannel == null) {
                return;
            }
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (managedServiceInfo.enabledAndUserMatches(UserHandle.getCallingUserId()) && NotificationManagerService.this.isInteractionVisibleToListener(managedServiceInfo, UserHandle.getCallingUserId())) {
                    BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationManagerService.NotificationListeners.this.lambda$notifyNotificationChannelChanged$9(managedServiceInfo, str, userHandle, notificationChannel, i);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyNotificationChannelChanged$9(ManagedServices.ManagedServiceInfo managedServiceInfo, String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            if (managedServiceInfo.isSystem || NotificationManagerService.this.hasCompanionDevice(managedServiceInfo) || NotificationManagerService.this.isServiceTokenValid(managedServiceInfo.service)) {
                notifyNotificationChannelChanged(managedServiceInfo, str, userHandle, notificationChannel, i);
            }
        }

        protected void notifyNotificationChannelGroupChanged(final String str, final UserHandle userHandle, final NotificationChannelGroup notificationChannelGroup, final int i) {
            if (notificationChannelGroup == null) {
                return;
            }
            for (final ManagedServices.ManagedServiceInfo managedServiceInfo : getServices()) {
                if (managedServiceInfo.enabledAndUserMatches(UserHandle.getCallingUserId()) && NotificationManagerService.this.isInteractionVisibleToListener(managedServiceInfo, UserHandle.getCallingUserId())) {
                    BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotificationManagerService.NotificationListeners.this.lambda$notifyNotificationChannelGroupChanged$10(managedServiceInfo, str, userHandle, notificationChannelGroup, i);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyNotificationChannelGroupChanged$10(ManagedServices.ManagedServiceInfo managedServiceInfo, String str, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup, int i) {
            if (managedServiceInfo.isSystem() || NotificationManagerService.this.hasCompanionDevice(managedServiceInfo)) {
                notifyNotificationChannelGroupChanged(managedServiceInfo, str, userHandle, notificationChannelGroup, i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: notifyPosted, reason: merged with bridge method [inline-methods] */
        public void lambda$prepareNotifyPostedLocked$2(ManagedServices.ManagedServiceInfo managedServiceInfo, StatusBarNotification statusBarNotification, NotificationRankingUpdate notificationRankingUpdate) {
            try {
                managedServiceInfo.service.onNotificationPosted(new StatusBarNotificationHolder(statusBarNotification), notificationRankingUpdate);
            } catch (DeadObjectException e) {
                Slog.wtf(this.TAG, "unable to notify listener (posted): " + managedServiceInfo, e);
            } catch (RemoteException e2) {
                Slog.e(this.TAG, "unable to notify listener (posted): " + managedServiceInfo, e2);
                if (!(e2 instanceof TransactionTooLargeException) || NotificationManagerService.this.mNMSWrapper.getNMSExt() == null) {
                    return;
                }
                NotificationManagerService.this.mNMSWrapper.getNMSExt().onClearAllNotifications(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, -1);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: notifyRemoved, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public void lambda$notifyRemovedLocked$4(ManagedServices.ManagedServiceInfo managedServiceInfo, StatusBarNotification statusBarNotification, NotificationRankingUpdate notificationRankingUpdate, NotificationStats notificationStats, int i) {
            INotificationListener iNotificationListener = managedServiceInfo.service;
            StatusBarNotificationHolder statusBarNotificationHolder = new StatusBarNotificationHolder(statusBarNotification);
            try {
                if (!CompatChanges.isChangeEnabled(NotificationManagerService.NOTIFICATION_CANCELLATION_REASONS, managedServiceInfo.uid) && (i == 20 || i == 21)) {
                    i = 17;
                }
                if (!CompatChanges.isChangeEnabled(NotificationManagerService.NOTIFICATION_LOG_ASSISTANT_CANCEL, managedServiceInfo.uid) && i == 22) {
                    i = 10;
                }
                iNotificationListener.onNotificationRemoved(statusBarNotificationHolder, notificationRankingUpdate, notificationStats, i);
            } catch (DeadObjectException e) {
                Slog.wtf(this.TAG, "unable to notify listener (removed): " + managedServiceInfo, e);
            } catch (RemoteException e2) {
                Slog.e(this.TAG, "unable to notify listener (removed): " + managedServiceInfo, e2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: notifyRankingUpdate, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyRankingUpdateLocked$6(ManagedServices.ManagedServiceInfo managedServiceInfo, NotificationRankingUpdate notificationRankingUpdate) {
            try {
                managedServiceInfo.service.onNotificationRankingUpdate(notificationRankingUpdate);
            } catch (DeadObjectException e) {
                Slog.wtf(this.TAG, "unable to notify listener (ranking update): " + managedServiceInfo, e);
            } catch (RemoteException e2) {
                Slog.e(this.TAG, "unable to notify listener (ranking update): " + managedServiceInfo, e2);
                if (!(e2 instanceof TransactionTooLargeException) || NotificationManagerService.this.mNMSWrapper.getNMSExt() == null) {
                    return;
                }
                NotificationManagerService.this.mNMSWrapper.getNMSExt().onClearAllNotifications(NotificationManagerService.MY_UID, NotificationManagerService.MY_PID, -1);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: notifyListenerHintsChanged, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyListenerHintsChangedLocked$7(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
            try {
                managedServiceInfo.service.onListenerHintsChanged(i);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify listener (listener hints): " + managedServiceInfo, e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: notifyInterruptionFilterChanged, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyInterruptionFilterChanged$8(ManagedServices.ManagedServiceInfo managedServiceInfo, int i) {
            try {
                managedServiceInfo.service.onInterruptionFilterChanged(i);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify listener (interruption filter): " + managedServiceInfo, e);
            }
        }

        void notifyNotificationChannelChanged(ManagedServices.ManagedServiceInfo managedServiceInfo, String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
            try {
                managedServiceInfo.service.onNotificationChannelModification(str, userHandle, notificationChannel, i);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify listener (channel changed): " + managedServiceInfo, e);
            }
        }

        private void notifyNotificationChannelGroupChanged(ManagedServices.ManagedServiceInfo managedServiceInfo, String str, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup, int i) {
            try {
                managedServiceInfo.getService().onNotificationChannelGroupModification(str, userHandle, notificationChannelGroup, i);
            } catch (RemoteException e) {
                Slog.e(this.TAG, "unable to notify listener (channel group changed): " + managedServiceInfo, e);
            }
        }

        public boolean isListenerPackage(String str) {
            if (str == null) {
                return false;
            }
            synchronized (NotificationManagerService.this.mNotificationLock) {
                Iterator<ManagedServices.ManagedServiceInfo> it = getServices().iterator();
                while (it.hasNext()) {
                    if (str.equals(it.next().component.getPackageName())) {
                        return true;
                    }
                }
                return false;
            }
        }

        boolean hasAllowedListener(String str, int i) {
            if (str == null) {
                return false;
            }
            List<ComponentName> allowedComponents = getAllowedComponents(i);
            for (int i2 = 0; i2 < allowedComponents.size(); i2++) {
                if (allowedComponents.get(i2).getPackageName().equals(str)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class RoleObserver implements OnRoleHoldersChangedListener {
        private final Executor mExecutor;
        private final Looper mMainLooper;
        private ArrayMap<String, ArrayMap<Integer, ArraySet<String>>> mNonBlockableDefaultApps;
        private final IPackageManager mPm;
        private final RoleManager mRm;
        private volatile ArraySet<Integer> mTrampolineExemptUids = new ArraySet<>();

        RoleObserver(Context context, RoleManager roleManager, IPackageManager iPackageManager, Looper looper) {
            this.mRm = roleManager;
            this.mPm = iPackageManager;
            this.mExecutor = context.getMainExecutor();
            this.mMainLooper = looper;
        }

        public void init() {
            List userHandles = NotificationManagerService.this.mUm.getUserHandles(true);
            this.mNonBlockableDefaultApps = new ArrayMap<>();
            int i = 0;
            while (true) {
                String[] strArr = NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES;
                if (i < strArr.length) {
                    ArrayMap<Integer, ArraySet<String>> arrayMap = new ArrayMap<>();
                    this.mNonBlockableDefaultApps.put(strArr[i], arrayMap);
                    for (int i2 = 0; i2 < userHandles.size(); i2++) {
                        Integer valueOf = Integer.valueOf(((UserHandle) userHandles.get(i2)).getIdentifier());
                        ArraySet<String> arraySet = new ArraySet<>(this.mRm.getRoleHoldersAsUser(NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES[i], UserHandle.of(valueOf.intValue())));
                        ArraySet<Pair<String, Integer>> arraySet2 = new ArraySet<>();
                        Iterator<String> it = arraySet.iterator();
                        while (it.hasNext()) {
                            String next = it.next();
                            arraySet2.add(new Pair<>(next, Integer.valueOf(getUidForPackage(next, valueOf.intValue()))));
                        }
                        arrayMap.put(valueOf, arraySet);
                        NotificationManagerService.this.mPreferencesHelper.updateDefaultApps(valueOf.intValue(), null, arraySet2);
                    }
                    i++;
                } else {
                    updateTrampolineExemptUidsForUsers((UserHandle[]) userHandles.toArray(new UserHandle[0]));
                    this.mRm.addOnRoleHoldersChangedListenerAsUser(this.mExecutor, this, UserHandle.ALL);
                    return;
                }
            }
        }

        @VisibleForTesting
        public boolean isApprovedPackageForRoleForUser(String str, String str2, int i) {
            return this.mNonBlockableDefaultApps.get(str).get(Integer.valueOf(i)).contains(str2);
        }

        @VisibleForTesting
        public boolean isUidExemptFromTrampolineRestrictions(int i) {
            return this.mTrampolineExemptUids.contains(Integer.valueOf(i));
        }

        public void onRoleHoldersChanged(String str, UserHandle userHandle) {
            onRoleHoldersChangedForNonBlockableDefaultApps(str, userHandle);
            onRoleHoldersChangedForTrampolines(str, userHandle);
        }

        private void onRoleHoldersChangedForNonBlockableDefaultApps(String str, UserHandle userHandle) {
            boolean z = false;
            int i = 0;
            while (true) {
                String[] strArr = NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES;
                if (i >= strArr.length) {
                    break;
                }
                if (strArr[i].equals(str)) {
                    z = true;
                    break;
                }
                i++;
            }
            if (z) {
                ArraySet<String> arraySet = new ArraySet<>(this.mRm.getRoleHoldersAsUser(str, userHandle));
                ArrayMap<Integer, ArraySet<String>> orDefault = this.mNonBlockableDefaultApps.getOrDefault(str, new ArrayMap<>());
                ArraySet<String> orDefault2 = orDefault.getOrDefault(Integer.valueOf(userHandle.getIdentifier()), new ArraySet<>());
                ArraySet<String> arraySet2 = new ArraySet<>();
                ArraySet<Pair<String, Integer>> arraySet3 = new ArraySet<>();
                Iterator<String> it = orDefault2.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (!arraySet.contains(next)) {
                        arraySet2.add(next);
                    }
                }
                Iterator<String> it2 = arraySet.iterator();
                while (it2.hasNext()) {
                    String next2 = it2.next();
                    if (!orDefault2.contains(next2)) {
                        arraySet3.add(new Pair<>(next2, Integer.valueOf(getUidForPackage(next2, userHandle.getIdentifier()))));
                    }
                }
                orDefault.put(Integer.valueOf(userHandle.getIdentifier()), arraySet);
                this.mNonBlockableDefaultApps.put(str, orDefault);
                NotificationManagerService.this.mPreferencesHelper.updateDefaultApps(userHandle.getIdentifier(), arraySet2, arraySet3);
            }
        }

        private void onRoleHoldersChangedForTrampolines(String str, UserHandle userHandle) {
            if ("android.app.role.BROWSER".equals(str)) {
                updateTrampolineExemptUidsForUsers(userHandle);
            }
        }

        private void updateTrampolineExemptUidsForUsers(UserHandle... userHandleArr) {
            Preconditions.checkState(this.mMainLooper.isCurrentThread());
            ArraySet<Integer> arraySet = this.mTrampolineExemptUids;
            ArraySet<Integer> arraySet2 = new ArraySet<>();
            int size = arraySet.size();
            for (int i = 0; i < size; i++) {
                int intValue = arraySet.valueAt(i).intValue();
                if (!ArrayUtils.contains(userHandleArr, UserHandle.of(UserHandle.getUserId(intValue)))) {
                    arraySet2.add(Integer.valueOf(intValue));
                }
            }
            for (UserHandle userHandle : userHandleArr) {
                for (String str : this.mRm.getRoleHoldersAsUser("android.app.role.BROWSER", userHandle)) {
                    int uidForPackage = getUidForPackage(str, userHandle.getIdentifier());
                    if (uidForPackage != -1) {
                        arraySet2.add(Integer.valueOf(uidForPackage));
                    } else {
                        Slog.e(NotificationManagerService.TAG, "Bad uid (-1) for browser package " + str);
                    }
                }
            }
            this.mTrampolineExemptUids = arraySet2;
        }

        private int getUidForPackage(String str, int i) {
            try {
                return this.mPm.getPackageUid(str, 131072L, i);
            } catch (RemoteException unused) {
                Slog.e(NotificationManagerService.TAG, "role manager has bad default " + str + " " + i);
                return -1;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class DumpFilter {
        public String pkgFilter;
        public boolean rvStats;
        public long since;
        public boolean stats;
        public boolean zen;
        public boolean filtered = false;
        public boolean redact = true;
        public boolean proto = false;
        public boolean criticalPriority = false;
        public boolean normalPriority = false;

        public static DumpFilter parseFromArguments(String[] strArr) {
            DumpFilter dumpFilter = new DumpFilter();
            int i = 0;
            while (i < strArr.length) {
                String str = strArr[i];
                if ("--proto".equals(str)) {
                    dumpFilter.proto = true;
                } else if ("--noredact".equals(str) || "--reveal".equals(str)) {
                    dumpFilter.redact = false;
                } else if ("p".equals(str) || "pkg".equals(str) || "--package".equals(str)) {
                    if (i < strArr.length - 1) {
                        i++;
                        String lowerCase = strArr[i].trim().toLowerCase();
                        dumpFilter.pkgFilter = lowerCase;
                        if (lowerCase.isEmpty()) {
                            dumpFilter.pkgFilter = null;
                        } else {
                            dumpFilter.filtered = true;
                        }
                    }
                } else if ("--zen".equals(str) || "zen".equals(str)) {
                    dumpFilter.filtered = true;
                    dumpFilter.zen = true;
                } else if ("--stats".equals(str)) {
                    dumpFilter.stats = true;
                    if (i < strArr.length - 1) {
                        i++;
                        dumpFilter.since = Long.parseLong(strArr[i]);
                    } else {
                        dumpFilter.since = 0L;
                    }
                } else if ("--remote-view-stats".equals(str)) {
                    dumpFilter.rvStats = true;
                    if (i < strArr.length - 1) {
                        i++;
                        dumpFilter.since = Long.parseLong(strArr[i]);
                    } else {
                        dumpFilter.since = 0L;
                    }
                } else if (PriorityDump.PRIORITY_ARG.equals(str) && i < strArr.length - 1) {
                    i++;
                    String str2 = strArr[i];
                    str2.hashCode();
                    if (str2.equals(PriorityDump.PRIORITY_ARG_NORMAL)) {
                        dumpFilter.normalPriority = true;
                    } else if (str2.equals(PriorityDump.PRIORITY_ARG_CRITICAL)) {
                        dumpFilter.criticalPriority = true;
                    }
                }
                i++;
            }
            return dumpFilter;
        }

        public boolean matches(StatusBarNotification statusBarNotification) {
            if (this.filtered && !this.zen) {
                return statusBarNotification != null && (matches(statusBarNotification.getPackageName()) || matches(statusBarNotification.getOpPkg()));
            }
            return true;
        }

        public boolean matches(ComponentName componentName) {
            if (this.filtered && !this.zen) {
                return componentName != null && matches(componentName.getPackageName());
            }
            return true;
        }

        public boolean matches(String str) {
            if (this.filtered && !this.zen) {
                return str != null && str.toLowerCase().contains(this.pkgFilter);
            }
            return true;
        }

        public String toString() {
            if (this.stats) {
                return "stats";
            }
            if (this.zen) {
                return "zen";
            }
            return '\'' + this.pkgFilter + '\'';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void resetAssistantUserSet(int i) {
        checkCallerIsSystemOrShell();
        this.mAssistants.setUserSet(i, false);
        handleSavePolicyFile();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public ComponentName getApprovedAssistant(int i) {
        checkCallerIsSystemOrShell();
        return (ComponentName) CollectionUtils.firstOrNull(this.mAssistants.getAllowedComponents(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class StatusBarNotificationHolder extends IStatusBarNotificationHolder.Stub {
        private StatusBarNotification mValue;

        public StatusBarNotificationHolder(StatusBarNotification statusBarNotification) {
            this.mValue = statusBarNotification;
        }

        public StatusBarNotification get() {
            StatusBarNotification statusBarNotification = this.mValue;
            this.mValue = null;
            return statusBarNotification;
        }
    }

    private void writeSecureNotificationsPolicy(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.startTag((String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG);
        typedXmlSerializer.attributeBoolean((String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_VALUE, this.mLockScreenAllowSecureNotifications);
        typedXmlSerializer.endTag((String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG);
    }

    protected Notification createReviewPermissionsNotification() {
        Intent intent = new Intent("android.settings.ALL_APPS_NOTIFICATION_SETTINGS_FOR_REVIEW");
        if (this.mNMSWrapper.getNMSExt() != null) {
            Intent notificationCenterIntent = this.mNMSWrapper.getNMSExt().getNotificationCenterIntent();
            if (notificationCenterIntent != null) {
                intent = notificationCenterIntent;
            } else {
                Slog.d(TAG, "center intent is null, we start the original notification setting activity instead.");
            }
        }
        Intent intent2 = new Intent(REVIEW_NOTIF_ACTION_REMIND);
        Intent intent3 = new Intent(REVIEW_NOTIF_ACTION_DISMISS);
        Intent intent4 = new Intent(REVIEW_NOTIF_ACTION_CANCELED);
        Notification.Action build = new Notification.Action.Builder((Icon) null, getContext().getResources().getString(17041529), PendingIntent.getBroadcast(getContext(), 0, intent2, 201326592)).build();
        return new Notification.Builder(getContext(), SystemNotificationChannels.SYSTEM_CHANGES).setSmallIcon(R.drawable.sym_keyboard_num5).setContentTitle(getContext().getResources().getString(17041531)).setContentText(getContext().getResources().getString(17041530)).setContentIntent(PendingIntent.getActivity(getContext(), 0, intent, 201326592)).setStyle(new Notification.BigTextStyle()).setFlag(32, true).setAutoCancel(true).addAction(build).addAction(new Notification.Action.Builder((Icon) null, getContext().getResources().getString(17041528), PendingIntent.getBroadcast(getContext(), 0, intent3, 201326592)).build()).setDeleteIntent(PendingIntent.getBroadcast(getContext(), 0, intent4, 201326592)).build();
    }

    protected void maybeShowInitialReviewPermissionsNotification() {
        if (this.mShowReviewPermissionsNotification) {
            int i = Settings.Global.getInt(getContext().getContentResolver(), "review_permissions_notification_state", -1);
            if (i == 0 || i == 3) {
                ((NotificationManager) getContext().getSystemService(NotificationManager.class)).notify(TAG, 71, createReviewPermissionsNotification());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class NotificationTrampolineCallback implements BackgroundActivityStartCallback {
        private NotificationTrampolineCallback() {
        }

        public boolean isActivityStartAllowed(Collection<IBinder> collection, int i, String str) {
            Preconditions.checkArgument(!collection.isEmpty());
            Iterator<IBinder> it = collection.iterator();
            while (it.hasNext()) {
                if (it.next() != NotificationManagerService.ALLOWLIST_TOKEN) {
                    return true;
                }
            }
            String str2 = "Indirect notification activity start (trampoline) from " + str;
            if (blockTrampoline(i)) {
                Slog.e(NotificationManagerService.TAG, str2 + " blocked");
                return false;
            }
            Slog.w(NotificationManagerService.TAG, str2 + ", this should be avoided for performance reasons");
            return true;
        }

        private boolean blockTrampoline(int i) {
            if (NotificationManagerService.this.mRoleObserver != null && NotificationManagerService.this.mRoleObserver.isUidExemptFromTrampolineRestrictions(i)) {
                return CompatChanges.isChangeEnabled(NotificationManagerService.NOTIFICATION_TRAMPOLINE_BLOCK_FOR_EXEMPT_ROLES, i);
            }
            return CompatChanges.isChangeEnabled(NotificationManagerService.NOTIFICATION_TRAMPOLINE_BLOCK, i);
        }

        public boolean canCloseSystemDialogs(Collection<IBinder> collection, int i) {
            return collection.contains(NotificationManagerService.ALLOWLIST_TOKEN) && !CompatChanges.isChangeEnabled(NotificationManagerService.NOTIFICATION_TRAMPOLINE_BLOCK, i);
        }
    }

    public INotificationManagerServiceWrapper getWrapper() {
        return this.mNMSWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class NotificationManagerServiceWrapper implements INotificationManagerServiceWrapper {
        private NotificationManagerServiceWrapper() {
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public INotificationManagerServiceExt getNMSExt() {
            return NotificationManagerService.mNMSExt;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public IZenModeManagerExt getZenModeManagerExt() {
            return NotificationManagerService.this.mZenModeManagerExt;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public PackageManager getPackageManagerClient() {
            return NotificationManagerService.this.mPackageManagerClient;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isTelevision() {
            return NotificationManagerService.this.mIsTelevision;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isAutomotive() {
            return NotificationManagerService.this.mIsAutomotive;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean notificationEffectsEnabledForAutomotive() {
            return NotificationManagerService.this.mNotificationEffectsEnabledForAutomotive;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean systemReady() {
            return NotificationManagerService.this.mSystemReady;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public NotificationUsageStats getNotificationUsageStats() {
            return NotificationManagerService.this.mUsageStats;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public NotificationListeners getNotificationListeners() {
            return NotificationManagerService.this.mListeners;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public SnoozeHelper getSnoozeHelper() {
            return NotificationManagerService.this.mSnoozeHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public String getSoundNotificationKey() {
            return NotificationManagerService.this.mSoundNotificationKey;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public String getVibrateNotificationKey() {
            return NotificationManagerService.this.mVibrateNotificationKey;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean useAttentionLight() {
            return NotificationManagerService.this.mUseAttentionLight;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public LogicalLight getNotificationLight() {
            return NotificationManagerService.this.mNotificationLight;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public ZenModeHelper getZenModeHelper() {
            return NotificationManagerService.this.mZenModeHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public AlarmManager getAlarmManager() {
            return NotificationManagerService.this.mAlarmManager;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public PermissionHelper getPermissionHelper() {
            return NotificationManagerService.this.mPermissionHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public ShortcutHelper getShortcutHelper() {
            return NotificationManagerService.this.mShortcutHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public ActivityManager getActivityManager() {
            return NotificationManagerService.this.mActivityManager;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public Handler getHandler() {
            return NotificationManagerService.this.mHandler;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public IBinder getAllowListToken() {
            return NotificationManagerService.ALLOWLIST_TOKEN;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public IBinder getService() {
            return NotificationManagerService.this.mService;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkCallerIsSameApp(String str) {
            NotificationManagerService.this.checkCallerIsSameApp(str);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkCallerIsSystemOrSameApp(String str) {
            NotificationManagerService.this.checkCallerIsSystemOrSameApp(str);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkCallerIsSystem() {
            NotificationManagerService.this.checkCallerIsSystem();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isCallerSystemOrPhone() {
            return NotificationManagerService.this.isCallerSystemOrPhone();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean areNotificationsEnabledForPackageInt(String str, int i) {
            return NotificationManagerService.this.areNotificationsEnabledForPackageInt(str, i);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void doChannelWarningToast(int i, CharSequence charSequence) {
            NotificationManagerService.this.doChannelWarningToast(i, charSequence);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean checkDisqualifyingFeatures(int i, int i2, int i3, String str, NotificationRecord notificationRecord, boolean z, boolean z2) {
            return NotificationManagerService.this.checkDisqualifyingFeatures(i, i2, i3, str, notificationRecord, z, z2);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isNotificationForCurrentUser(NotificationRecord notificationRecord) {
            return NotificationManagerService.this.isNotificationForCurrentUser(notificationRecord);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean playSound(NotificationRecord notificationRecord, Uri uri) {
            return NotificationManagerService.this.playSound(notificationRecord, uri);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean playVibration(NotificationRecord notificationRecord, VibrationEffect vibrationEffect, boolean z) {
            return NotificationManagerService.this.playVibration(notificationRecord, vibrationEffect, z);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean removeFromNotificationListsLocked(NotificationRecord notificationRecord) {
            return NotificationManagerService.this.removeFromNotificationListsLocked(notificationRecord);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void cancelNotificationLocked(NotificationRecord notificationRecord, boolean z, int i, boolean z2, String str, long j) {
            NotificationManagerService.this.cancelNotificationLocked(notificationRecord, z, i, z2, str, j);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void updateNotificationPulse() {
            NotificationManagerService.this.updateNotificationPulse();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void clearLightsLocked() {
            NotificationManagerService.this.clearLightsLocked();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkRestrictedCategories(Notification notification) {
            NotificationManagerService.this.checkRestrictedCategories(notification);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isInCall() {
            return NotificationManagerService.this.isInCall();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PostNotificationTrackerFactory {
        default PostNotificationTracker newTracker(PowerManager.WakeLock wakeLock) {
            return new PostNotificationTracker(wakeLock);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PostNotificationTracker {
        private final PowerManager.WakeLock mWakeLock;
        private final long mStartTime = SystemClock.elapsedRealtime();
        private boolean mOngoing = true;

        @VisibleForTesting
        PostNotificationTracker(PowerManager.WakeLock wakeLock) {
            this.mWakeLock = wakeLock;
            if (NotificationManagerService.DBG) {
                Slog.d(NotificationManagerService.TAG, "PostNotification: Started");
            }
        }

        long getStartTime() {
            return this.mStartTime;
        }

        @VisibleForTesting
        boolean isOngoing() {
            return this.mOngoing;
        }

        void cancel() {
            if (!isOngoing()) {
                Log.wtfStack(NotificationManagerService.TAG, "cancel() called on already-finished tracker");
                return;
            }
            this.mOngoing = false;
            if (this.mWakeLock != null) {
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$PostNotificationTracker$$ExternalSyntheticLambda1
                    public final void runOrThrow() {
                        NotificationManagerService.PostNotificationTracker.this.lambda$cancel$0();
                    }
                });
            }
            if (NotificationManagerService.DBG) {
                Slog.d(NotificationManagerService.TAG, TextUtils.formatSimple("PostNotification: Abandoned after %d ms", new Object[]{Long.valueOf(SystemClock.elapsedRealtime() - this.mStartTime)}));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cancel$0() throws Exception {
            this.mWakeLock.release();
        }

        long finish() {
            long elapsedRealtime = SystemClock.elapsedRealtime() - this.mStartTime;
            if (!isOngoing()) {
                Log.wtfStack(NotificationManagerService.TAG, "finish() called on already-finished tracker");
                return elapsedRealtime;
            }
            this.mOngoing = false;
            if (this.mWakeLock != null) {
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$PostNotificationTracker$$ExternalSyntheticLambda0
                    public final void runOrThrow() {
                        NotificationManagerService.PostNotificationTracker.this.lambda$finish$1();
                    }
                });
            }
            if (NotificationManagerService.DBG) {
                Slog.d(NotificationManagerService.TAG, TextUtils.formatSimple("PostNotification: Finished in %d ms", new Object[]{Long.valueOf(elapsedRealtime)}));
            }
            return elapsedRealtime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$finish$1() throws Exception {
            this.mWakeLock.release();
        }
    }
}
