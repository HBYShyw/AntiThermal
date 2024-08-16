package com.android.server.autofill;

import android.R;
import android.app.ActivityTaskManager;
import android.app.IAssistDataReceiver;
import android.app.PendingIntent;
import android.app.assist.AssistStructure;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SystemClock;
import android.service.assist.classification.FieldClassificationRequest;
import android.service.assist.classification.FieldClassificationResponse;
import android.service.autofill.AutofillFieldClassificationService;
import android.service.autofill.CompositeUserData;
import android.service.autofill.Dataset;
import android.service.autofill.FieldClassification;
import android.service.autofill.FieldClassificationUserData;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.InlinePresentation;
import android.service.autofill.InternalSanitizer;
import android.service.autofill.InternalValidator;
import android.service.autofill.SaveInfo;
import android.service.autofill.SaveRequest;
import android.service.autofill.UserData;
import android.service.autofill.ValueFinder;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.LocalLog;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.view.KeyEvent;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.autofill.IAutoFillManagerClient;
import android.view.autofill.IAutofillWindowPresenter;
import android.view.inputmethod.InlineSuggestionsRequest;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.LocalServices;
import com.android.server.autofill.RemoteFieldClassificationService;
import com.android.server.autofill.RemoteFillService;
import com.android.server.autofill.ViewState;
import com.android.server.autofill.ui.AutoFillUI;
import com.android.server.autofill.ui.InlineFillUi;
import com.android.server.autofill.ui.PendingUi;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.wm.ActivityTaskManagerInternal;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class Session implements RemoteFillService.FillServiceCallbacks, ViewState.Listener, AutoFillUI.AutoFillUiCallback, ValueFinder, RemoteFieldClassificationService.FieldClassificationServiceCallbacks {
    private static final String ACTION_DELAYED_FILL = "android.service.autofill.action.DELAYED_FILL";
    static final int AUGMENTED_AUTOFILL_REQUEST_ID = 1;
    private static final boolean DBG = false;
    public static final String EXTRA_KEY_DETECTIONS = "detections";
    private static final String EXTRA_REQUEST_ID = "android.service.autofill.extra.REQUEST_ID";
    private static final String PCC_HINTS_DELIMITER = ",";
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_REMOVED = 3;
    public static final int STATE_UNKNOWN = 0;
    private static final String TAG = "AutofillSession";
    private static AtomicInteger sIdCounter = new AtomicInteger(2);
    private static AtomicInteger sIdCounterForPcc = new AtomicInteger(2);
    public final int id;

    @GuardedBy({"mLock"})
    private IBinder mActivityToken;
    private final AssistDataReceiverImpl mAssistReceiver;

    @GuardedBy({"mLock"})
    private Runnable mAugmentedAutofillDestroyer;

    @GuardedBy({"mLock"})
    private ArrayList<AutofillId> mAugmentedAutofillableIds;

    @GuardedBy({"mLock"})
    private ArrayList<LogMaker> mAugmentedRequestsLogs;
    private final ClassificationState mClassificationState;

    @GuardedBy({"mLock"})
    private IAutoFillManagerClient mClient;

    @GuardedBy({"mLock"})
    private Bundle mClientState;

    @GuardedBy({"mLock"})
    private IBinder.DeathRecipient mClientVulture;
    private final boolean mCompatMode;
    private final ComponentName mComponentName;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private ArrayList<FillContext> mContexts;

    @GuardedBy({"mLock"})
    private AutofillId mCurrentViewId;
    private final BroadcastReceiver mDelayedFillBroadcastReceiver;

    @GuardedBy({"mLock"})
    private boolean mDelayedFillBroadcastReceiverRegistered;

    @GuardedBy({"mLock"})
    private PendingIntent mDelayedFillPendingIntent;

    @GuardedBy({"mLock"})
    boolean mDestroyed;

    @GuardedBy({"mLock"})
    private FillRequestEventLogger mFillRequestEventLogger;

    @GuardedBy({"mLock"})
    private FillResponseEventLogger mFillResponseEventLogger;
    public final int mFlags;
    private final Handler mHandler;
    private boolean mHasCallback;
    final AutofillInlineSessionController mInlineSessionController;

    @GuardedBy({"mLock"})
    private AutofillId[] mLastFillDialogTriggerIds;

    @GuardedBy({"mLock"})
    private Pair<Integer, InlineSuggestionsRequest> mLastInlineSuggestionsRequest;

    @GuardedBy({"mLock"})
    private long mLatencyBaseTime;
    final Object mLock;

    @GuardedBy({"mLock"})
    private boolean mLogViewEntered;

    @GuardedBy({"mLock"})
    private boolean mLoggedInlineDatasetShown;
    private final MetricsLogger mMetricsLogger;
    private final PccAssistDataReceiverImpl mPccAssistReceiver;

    @GuardedBy({"mLock"})
    private PendingUi mPendingSaveUi;

    @GuardedBy({"mLock"})
    private PresentationStatsEventLogger mPresentationStatsEventLogger;

    @GuardedBy({"mLock"})
    private boolean mPreviouslyFillDialogPotentiallyStarted;
    private final RemoteFillService mRemoteFillService;
    private int mRequestCount;

    @GuardedBy({"mLock"})
    private final SparseArray<LogMaker> mRequestLogs;

    @GuardedBy({"mLock"})
    private SparseArray<FillResponse> mResponses;

    @GuardedBy({"mLock"})
    private SaveEventLogger mSaveEventLogger;

    @GuardedBy({"mLock"})
    private boolean mSaveOnAllViewsInvisible;

    @GuardedBy({"mLock"})
    private ArrayList<String> mSelectedDatasetIds;
    private final AutofillManagerServiceImpl mService;

    @GuardedBy({"mLock"})
    private SessionCommittedEventLogger mSessionCommittedEventLogger;

    @GuardedBy({"mLock"})
    private final SessionFlags mSessionFlags;

    @GuardedBy({"mLock"})
    private int mSessionState;
    private final long mStartTime;
    private final AutoFillUI mUi;

    @GuardedBy({"mLock"})
    private final LocalLog mUiLatencyHistory;

    @GuardedBy({"mLock"})
    private long mUiShownTime;

    @GuardedBy({"mLock"})
    private AssistStructure.ViewNode mUrlBar;

    @GuardedBy({"mLock"})
    private final ArrayMap<AutofillId, ViewState> mViewStates;

    @GuardedBy({"mLock"})
    private final LocalLog mWtfHistory;
    public final int taskId;
    public final int uid;
    public final int userId;
    private ISessionExt mSessionExt = (ISessionExt) ExtLoader.type(ISessionExt.class).base(this).create();
    private ISessionWrapper mSessionWrapper = new SessionWrapper();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface SessionState {
    }

    private boolean isRequestSupportFillDialog(int i) {
        return (i & 64) != 0;
    }

    private boolean isViewFocusedLocked(int i) {
        return (i & 16) == 0;
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onClassificationRequestFailure(int i, CharSequence charSequence) {
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onClassificationRequestTimeout(int i) {
    }

    public ISessionWrapper getWrapper() {
        return this.mSessionWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SessionWrapper implements ISessionWrapper {
        /* synthetic */ SessionWrapper(Session session, SessionWrapperIA sessionWrapperIA) {
            this();
        }

        private SessionWrapper() {
        }

        @Override // com.android.server.autofill.ISessionWrapper
        public void save() {
            Session.this.save();
        }

        @Override // com.android.server.autofill.ISessionWrapper
        public void autofill(int i, int i2, Dataset dataset, boolean z, int i3) {
            Session.this.autoFill(i, i2, dataset, z, i3);
        }

        @Override // com.android.server.autofill.ISessionWrapper
        public ISessionExt getSessionExt() {
            return Session.this.mSessionExt;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.autofill.Session$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(Session.ACTION_DELAYED_FILL)) {
                Slog.wtf(Session.TAG, "Unexpected action is received.");
                return;
            }
            if (!intent.hasExtra(Session.EXTRA_REQUEST_ID)) {
                Slog.e(Session.TAG, "Delay fill action is missing request id extra.");
                return;
            }
            Slog.v(Session.TAG, "mDelayedFillBroadcastReceiver delayed fill action received");
            synchronized (Session.this.mLock) {
                Session.this.mAssistReceiver.processDelayedFillLocked(intent.getIntExtra(Session.EXTRA_REQUEST_ID, 0), (FillResponse) intent.getParcelableExtra("android.service.autofill.extra.FILL_RESPONSE", FillResponse.class));
            }
        }
    }

    public void onSwitchInputMethodLocked() {
        if (!this.mSessionFlags.mExpiredResponse && shouldResetSessionStateOnInputMethodSwitch()) {
            this.mSessionFlags.mExpiredResponse = true;
            this.mAugmentedAutofillableIds = null;
            if (this.mSessionFlags.mAugmentedAutofillOnly) {
                this.mCurrentViewId = null;
            }
        }
    }

    private boolean shouldResetSessionStateOnInputMethodSwitch() {
        if (this.mService.getRemoteInlineSuggestionRenderServiceLocked() == null) {
            return false;
        }
        if (this.mSessionFlags.mInlineSupportedByService) {
            return true;
        }
        ViewState viewState = this.mViewStates.get(this.mCurrentViewId);
        return (viewState == null || (viewState.getState() & 4096) == 0) ? false : true;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SessionFlags {
        private boolean mAugmentedAutofillOnly;
        private boolean mAutofillDisabled;
        private boolean mExpiredResponse;
        private boolean mFillDialogDisabled;
        private boolean mInlineSupportedByService;
        private boolean mScreenHasCredmanField;
        private boolean mShowingSaveUi;

        /* synthetic */ SessionFlags(Session session, SessionFlagsIA sessionFlagsIA) {
            this();
        }

        private SessionFlags() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class AssistDataReceiverImpl extends IAssistDataReceiver.Stub {

        @GuardedBy({"mLock"})
        private FillRequest mLastFillRequest;

        @GuardedBy({"mLock"})
        private FillRequest mPendingFillRequest;

        @GuardedBy({"mLock"})
        private InlineSuggestionsRequest mPendingInlineSuggestionsRequest;

        @GuardedBy({"mLock"})
        private boolean mWaitForInlineRequest;

        public void onHandleAssistScreenshot(Bitmap bitmap) {
        }

        AssistDataReceiverImpl() {
        }

        Consumer<InlineSuggestionsRequest> newAutofillRequestLocked(ViewState viewState, boolean z) {
            this.mPendingFillRequest = null;
            this.mWaitForInlineRequest = z;
            this.mPendingInlineSuggestionsRequest = null;
            if (z) {
                return new InlineSuggestionRequestConsumer(new WeakReference(this), new WeakReference(viewState));
            }
            return null;
        }

        public void handleInlineSuggestionRequest(InlineSuggestionsRequest inlineSuggestionsRequest, ViewState viewState) {
            synchronized (Session.this.mLock) {
                if (this.mWaitForInlineRequest && this.mPendingInlineSuggestionsRequest == null) {
                    this.mWaitForInlineRequest = inlineSuggestionsRequest != null;
                    this.mPendingInlineSuggestionsRequest = inlineSuggestionsRequest;
                    maybeRequestFillLocked();
                    viewState.resetState(65536);
                }
            }
        }

        @GuardedBy({"mLock"})
        void maybeRequestFillLocked() {
            if (this.mPendingFillRequest == null) {
                return;
            }
            if (this.mWaitForInlineRequest) {
                if (this.mPendingInlineSuggestionsRequest == null) {
                    return;
                }
                this.mPendingFillRequest = new FillRequest(this.mPendingFillRequest.getId(), this.mPendingFillRequest.getFillContexts(), this.mPendingFillRequest.getHints(), Session.this.mSessionExt.hookOnFillRequestClientState(this.mPendingFillRequest.getClientState()), this.mPendingFillRequest.getFlags(), this.mPendingInlineSuggestionsRequest, this.mPendingFillRequest.getDelayedFillIntentSender());
            }
            this.mLastFillRequest = this.mPendingFillRequest;
            Session.this.mRemoteFillService.onFillRequest(this.mPendingFillRequest);
            this.mPendingInlineSuggestionsRequest = null;
            this.mWaitForInlineRequest = false;
            this.mPendingFillRequest = null;
            int elapsedRealtime = (int) (SystemClock.elapsedRealtime() - Session.this.mLatencyBaseTime);
            Session.this.mPresentationStatsEventLogger.maybeSetFillRequestSentTimestampMs(elapsedRealtime);
            Session.this.mFillRequestEventLogger.maybeSetLatencyFillRequestSentMillis(elapsedRealtime);
            Session.this.mFillRequestEventLogger.logAndEndEvent();
        }

        public void onHandleAssistData(Bundle bundle) throws RemoteException {
            if (Session.this.mRemoteFillService == null) {
                Session session = Session.this;
                session.wtf(null, "onHandleAssistData() called without a remote service. mForAugmentedAutofillOnly: %s", Boolean.valueOf(session.mSessionFlags.mAugmentedAutofillOnly));
                return;
            }
            AutofillId autofillId = Session.this.mCurrentViewId;
            if (autofillId == null) {
                Slog.w(Session.TAG, "No current view id - session might have finished");
                return;
            }
            AssistStructure assistStructure = (AssistStructure) bundle.getParcelable("structure", AssistStructure.class);
            if (assistStructure == null) {
                Slog.e(Session.TAG, "No assist structure - app might have crashed providing it");
                return;
            }
            Bundle bundle2 = bundle.getBundle("receiverExtras");
            if (bundle2 == null) {
                Slog.e(Session.TAG, "No receiver extras - app might have crashed providing it");
                return;
            }
            int i = bundle2.getInt(Session.EXTRA_REQUEST_ID);
            if (Helper.sVerbose) {
                Slog.v(Session.TAG, "New structure for requestId " + i + ": " + assistStructure);
            }
            synchronized (Session.this.mLock) {
                try {
                    assistStructure.ensureDataForAutofill();
                    ArrayList<AutofillId> autofillIds = Helper.getAutofillIds(assistStructure, false);
                    for (int i2 = 0; i2 < autofillIds.size(); i2++) {
                        autofillIds.get(i2).setSessionId(Session.this.id);
                    }
                    int flags = assistStructure.getFlags();
                    if (Session.this.mCompatMode) {
                        String[] urlBarResourceIdsForCompatMode = Session.this.mService.getUrlBarResourceIdsForCompatMode(Session.this.mComponentName.getPackageName());
                        if (Helper.sDebug) {
                            Slog.d(Session.TAG, "url_bars in compat mode: " + Arrays.toString(urlBarResourceIdsForCompatMode));
                        }
                        if (urlBarResourceIdsForCompatMode != null) {
                            Session.this.mUrlBar = Helper.sanitizeUrlBar(assistStructure, urlBarResourceIdsForCompatMode);
                            if (Session.this.mUrlBar != null) {
                                AutofillId autofillId2 = Session.this.mUrlBar.getAutofillId();
                                if (Helper.sDebug) {
                                    Slog.d(Session.TAG, "Setting urlBar as id=" + autofillId2 + " and domain " + Session.this.mUrlBar.getWebDomain());
                                }
                                Session.this.mViewStates.put(autofillId2, new ViewState(autofillId2, Session.this, 512));
                            }
                        }
                        flags |= 2;
                    }
                    int i3 = flags;
                    Session.this.mSessionExt.hookSanitizeForParceling(assistStructure);
                    if (Session.this.mContexts == null) {
                        Session.this.mContexts = new ArrayList(1);
                    }
                    Session.this.mContexts.add(new FillContext(i, assistStructure, autofillId));
                    Session.this.cancelCurrentRequestLocked();
                    int size = Session.this.mContexts.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        Session session2 = Session.this;
                        session2.fillContextWithAllowedValuesLocked((FillContext) session2.mContexts.get(i4), i3);
                    }
                    ArrayList mergePreviousSessionLocked = Session.this.mergePreviousSessionLocked(false);
                    List typeHintsForProvider = Session.this.getTypeHintsForProvider();
                    Session session3 = Session.this;
                    session3.mDelayedFillPendingIntent = session3.createPendingIntent(i);
                    Session session4 = Session.this;
                    session4.mClientState = session4.mSessionExt.hookOnFillRequestClientState(Session.this.mClientState);
                    this.mPendingFillRequest = new FillRequest(i, mergePreviousSessionLocked, typeHintsForProvider, Session.this.mClientState, i3, null, Session.this.mDelayedFillPendingIntent != null ? Session.this.mDelayedFillPendingIntent.getIntentSender() : null);
                    maybeRequestFillLocked();
                } catch (RuntimeException e) {
                    Session.this.wtf(e, "Exception lazy loading assist structure for %s: %s", assistStructure.getActivityComponent(), e);
                    return;
                }
            }
            if (Session.this.mActivityToken != null) {
                Session.this.mService.sendActivityAssistDataToContentCapture(Session.this.mActivityToken, bundle);
            }
        }

        @GuardedBy({"mLock"})
        void processDelayedFillLocked(int i, FillResponse fillResponse) {
            FillRequest fillRequest = this.mLastFillRequest;
            if (fillRequest == null || i != fillRequest.getId()) {
                return;
            }
            Slog.v(Session.TAG, "processDelayedFillLocked: calling onFillRequestSuccess with new response");
            Session session = Session.this;
            session.onFillRequestSuccess(i, fillResponse, session.mService.getServicePackageName(), this.mLastFillRequest.getFlags());
        }
    }

    public List<String> getTypeHintsForProvider() {
        if (!this.mService.isPccClassificationEnabled()) {
            return Collections.EMPTY_LIST;
        }
        String pccProviderHints = ((AutofillManagerService) this.mService.getMaster()).getPccProviderHints();
        if (Helper.sVerbose) {
            Slog.v(TAG, "TypeHints flag:" + pccProviderHints);
        }
        if (TextUtils.isEmpty(pccProviderHints)) {
            return new ArrayList();
        }
        return List.of((Object[]) pccProviderHints.split(PCC_HINTS_DELIMITER));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PccAssistDataReceiverImpl extends IAssistDataReceiver.Stub {
        /* synthetic */ PccAssistDataReceiverImpl(Session session, PccAssistDataReceiverImplIA pccAssistDataReceiverImplIA) {
            this();
        }

        public void onHandleAssistScreenshot(Bitmap bitmap) {
        }

        private PccAssistDataReceiverImpl() {
        }

        @GuardedBy({"mLock"})
        void maybeRequestFieldClassificationFromServiceLocked() {
            if (Session.this.mClassificationState.mPendingFieldClassificationRequest == null) {
                Slog.w(Session.TAG, "Received AssistData without pending classification request");
                return;
            }
            RemoteFieldClassificationService remoteFieldClassificationServiceLocked = Session.this.mService.getRemoteFieldClassificationServiceLocked();
            if (remoteFieldClassificationServiceLocked != null) {
                remoteFieldClassificationServiceLocked.onFieldClassificationRequest(Session.this.mClassificationState.mPendingFieldClassificationRequest, new WeakReference<>(Session.this));
            }
            Session.this.mClassificationState.onFieldClassificationRequestSent();
        }

        public void onHandleAssistData(Bundle bundle) throws RemoteException {
            AssistStructure assistStructure = (AssistStructure) bundle.getParcelable("structure", AssistStructure.class);
            if (assistStructure == null) {
                Slog.e(Session.TAG, "No assist structure for pcc detection - app might have crashed providing it");
                return;
            }
            Bundle bundle2 = bundle.getBundle("receiverExtras");
            if (bundle2 == null) {
                Slog.e(Session.TAG, "No receiver extras for pcc detection - app might have crashed providing it");
                return;
            }
            int i = bundle2.getInt(Session.EXTRA_REQUEST_ID);
            if (Helper.sVerbose) {
                Slog.v(Session.TAG, "New structure for PCC Detection: requestId " + i + ": " + assistStructure);
            }
            synchronized (Session.this.mLock) {
                try {
                    try {
                        assistStructure.ensureDataForAutofill();
                        ArrayList<AutofillId> autofillIds = Helper.getAutofillIds(assistStructure, false);
                        for (int i2 = 0; i2 < autofillIds.size(); i2++) {
                            autofillIds.get(i2).setSessionId(Session.this.id);
                        }
                        Session.this.mClassificationState.onAssistStructureReceived(assistStructure);
                        maybeRequestFieldClassificationFromServiceLocked();
                    } catch (RuntimeException e) {
                        Session.this.wtf(e, "Exception lazy loading assist structure for %s: %s", assistStructure.getActivityComponent(), e);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public PendingIntent createPendingIntent(int i) {
        Slog.d(TAG, "createPendingIntent for request " + i);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return PendingIntent.getBroadcast(this.mContext, this.id, new Intent(ACTION_DELAYED_FILL).setPackage("android").putExtra(EXTRA_REQUEST_ID, i), 1375731712);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mLock"})
    private void clearPendingIntentLocked() {
        Slog.d(TAG, "clearPendingIntentLocked");
        if (this.mDelayedFillPendingIntent == null) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mDelayedFillPendingIntent.cancel();
            this.mDelayedFillPendingIntent = null;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mLock"})
    private void registerDelayedFillBroadcastLocked() {
        if (this.mDelayedFillBroadcastReceiverRegistered) {
            return;
        }
        Slog.v(TAG, "registerDelayedFillBroadcastLocked()");
        this.mContext.registerReceiver(this.mDelayedFillBroadcastReceiver, new IntentFilter(ACTION_DELAYED_FILL));
        this.mDelayedFillBroadcastReceiverRegistered = true;
    }

    @GuardedBy({"mLock"})
    private void unregisterDelayedFillBroadcastLocked() {
        if (this.mDelayedFillBroadcastReceiverRegistered) {
            Slog.v(TAG, "unregisterDelayedFillBroadcastLocked()");
            this.mContext.unregisterReceiver(this.mDelayedFillBroadcastReceiver);
            this.mDelayedFillBroadcastReceiverRegistered = false;
        }
    }

    @GuardedBy({"mLock"})
    private AutofillId[] getIdsOfAllViewStatesLocked() {
        int size = this.mViewStates.size();
        AutofillId[] autofillIdArr = new AutofillId[size];
        for (int i = 0; i < size; i++) {
            autofillIdArr[i] = this.mViewStates.valueAt(i).id;
        }
        return autofillIdArr;
    }

    public String findByAutofillId(AutofillId autofillId) {
        synchronized (this.mLock) {
            AutofillValue findValueLocked = findValueLocked(autofillId);
            if (findValueLocked != null) {
                if (findValueLocked.isText()) {
                    return findValueLocked.getTextValue().toString();
                }
                if (findValueLocked.isList()) {
                    CharSequence[] autofillOptionsFromContextsLocked = getAutofillOptionsFromContextsLocked(autofillId);
                    if (autofillOptionsFromContextsLocked != null) {
                        CharSequence charSequence = autofillOptionsFromContextsLocked[findValueLocked.getListValue()];
                        return charSequence != null ? charSequence.toString() : null;
                    }
                    Slog.w(TAG, "findByAutofillId(): no autofill options for id " + autofillId);
                }
            }
            return null;
        }
    }

    public AutofillValue findRawValueByAutofillId(AutofillId autofillId) {
        AutofillValue findValueLocked;
        synchronized (this.mLock) {
            findValueLocked = findValueLocked(autofillId);
        }
        return findValueLocked;
    }

    @GuardedBy({"mLock"})
    private AutofillValue findValueLocked(AutofillId autofillId) {
        AutofillValue findValueFromThisSessionOnlyLocked = findValueFromThisSessionOnlyLocked(autofillId);
        if (findValueFromThisSessionOnlyLocked != null) {
            return getSanitizedValue(Helper.createSanitizers(getSaveInfoLocked()), autofillId, findValueFromThisSessionOnlyLocked);
        }
        ArrayList<Session> previousSessionsLocked = this.mService.getPreviousSessionsLocked(this);
        if (previousSessionsLocked == null) {
            return null;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "findValueLocked(): looking on " + previousSessionsLocked.size() + " previous sessions for autofillId " + autofillId);
        }
        for (int i = 0; i < previousSessionsLocked.size(); i++) {
            Session session = previousSessionsLocked.get(i);
            AutofillValue findValueFromThisSessionOnlyLocked2 = session.findValueFromThisSessionOnlyLocked(autofillId);
            if (findValueFromThisSessionOnlyLocked2 != null) {
                return getSanitizedValue(Helper.createSanitizers(session.getSaveInfoLocked()), autofillId, findValueFromThisSessionOnlyLocked2);
            }
        }
        return null;
    }

    private AutofillValue findValueFromThisSessionOnlyLocked(AutofillId autofillId) {
        ViewState viewState = this.mViewStates.get(autofillId);
        if (viewState == null) {
            if (!Helper.sDebug) {
                return null;
            }
            Slog.d(TAG, "findValueLocked(): no view state for " + autofillId);
            return null;
        }
        AutofillValue currentValue = viewState.getCurrentValue();
        if (currentValue != null) {
            return currentValue;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "findValueLocked(): no current value for " + autofillId);
        }
        return getValueFromContextsLocked(autofillId);
    }

    @GuardedBy({"mLock"})
    public void fillContextWithAllowedValuesLocked(FillContext fillContext, int i) {
        AssistStructure.ViewNode[] findViewNodesByAutofillIds = fillContext.findViewNodesByAutofillIds(getIdsOfAllViewStatesLocked());
        int size = this.mViewStates.size();
        for (int i2 = 0; i2 < size; i2++) {
            ViewState valueAt = this.mViewStates.valueAt(i2);
            AssistStructure.ViewNode viewNode = findViewNodesByAutofillIds[i2];
            if (viewNode == null) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "fillContextWithAllowedValuesLocked(): no node for " + valueAt.id);
                }
            } else {
                AutofillValue currentValue = valueAt.getCurrentValue();
                AutofillValue autofilledValue = valueAt.getAutofilledValue();
                AssistStructure.AutofillOverlay autofillOverlay = new AssistStructure.AutofillOverlay();
                if (autofilledValue != null && autofilledValue.equals(currentValue)) {
                    autofillOverlay.value = currentValue;
                }
                AutofillId autofillId = this.mCurrentViewId;
                if (autofillId != null) {
                    boolean equals = autofillId.equals(valueAt.id);
                    autofillOverlay.focused = equals;
                    if (equals && (i & 1) != 0) {
                        autofillOverlay.value = currentValue;
                    }
                }
                viewNode.setAutofillOverlay(autofillOverlay);
            }
        }
    }

    @GuardedBy({"mLock"})
    public void cancelCurrentRequestLocked() {
        ArrayList<FillContext> arrayList;
        if (this.mRemoteFillService == null) {
            wtf(null, "cancelCurrentRequestLocked() called without a remote service. mForAugmentedAutofillOnly: %s", Boolean.valueOf(this.mSessionFlags.mAugmentedAutofillOnly));
            return;
        }
        if (this.mSessionExt.isOplusAutofillService()) {
            this.mRemoteFillService.getWrapper().delayCancelRequest(this.mContexts);
            return;
        }
        int cancelCurrentRequest = this.mRemoteFillService.cancelCurrentRequest();
        if (cancelCurrentRequest == Integer.MIN_VALUE || (arrayList = this.mContexts) == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (this.mContexts.get(size).getRequestId() == cancelCurrentRequest) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "cancelCurrentRequest(): id = " + cancelCurrentRequest);
                }
                this.mContexts.remove(size);
                return;
            }
        }
    }

    @GuardedBy({"mLock"})
    private void requestNewFillResponseLocked(ViewState viewState, int i, int i2) {
        int andIncrement;
        FillResponse response = viewState.getResponse();
        this.mFillRequestEventLogger.startLogForNewRequest();
        this.mRequestCount++;
        this.mFillRequestEventLogger.maybeSetAppPackageUid(this.uid);
        this.mFillRequestEventLogger.maybeSetFlags(this.mFlags);
        if (this.mPreviouslyFillDialogPotentiallyStarted) {
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(3);
        } else {
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(4);
        }
        if (response != null) {
            setViewStatesLocked(response, 1, true);
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(5);
        }
        this.mSessionFlags.mExpiredResponse = false;
        this.mSessionState = 1;
        if (this.mSessionFlags.mAugmentedAutofillOnly || this.mRemoteFillService == null) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "requestNewFillResponse(): triggering augmented autofill instead (mForAugmentedAutofillOnly=" + this.mSessionFlags.mAugmentedAutofillOnly + ", flags=" + i2 + ")");
            }
            this.mSessionFlags.mAugmentedAutofillOnly = true;
            this.mFillRequestEventLogger.maybeSetRequestId(1);
            this.mFillRequestEventLogger.maybeSetIsAugmented(true);
            this.mFillRequestEventLogger.logAndEndEvent();
            triggerAugmentedAutofillLocked(i2);
            return;
        }
        viewState.setState(i);
        do {
            andIncrement = sIdCounter.getAndIncrement();
        } while (andIncrement == Integer.MIN_VALUE);
        int size = this.mRequestLogs.size() + 1;
        LogMaker addTaggedData = newLogMaker(907).addTaggedData(1454, Integer.valueOf(size));
        if (i2 != 0) {
            addTaggedData.addTaggedData(1452, Integer.valueOf(i2));
        }
        this.mRequestLogs.put(andIncrement, addTaggedData);
        if (Helper.sVerbose) {
            Slog.v(TAG, "Requesting structure for request #" + size + " ,requestId=" + andIncrement + ", flags=" + i2);
        }
        this.mPresentationStatsEventLogger.maybeSetRequestId(andIncrement);
        this.mFillRequestEventLogger.maybeSetRequestId(andIncrement);
        this.mFillRequestEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        if (this.mSessionFlags.mInlineSupportedByService) {
            this.mFillRequestEventLogger.maybeSetInlineSuggestionHostUid(this.mContext, this.userId);
        }
        this.mFillRequestEventLogger.maybeSetIsFillDialogEligible(!this.mSessionFlags.mFillDialogDisabled);
        cancelCurrentRequestLocked();
        if (this.mService.isPccClassificationEnabled() && this.mClassificationState.mHintsToAutofillIdMap == null) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "triggering field classification");
            }
            requestAssistStructureForPccLocked(i2 | 512);
        }
        RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderServiceLocked = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (this.mSessionFlags.mInlineSupportedByService && remoteInlineSuggestionRenderServiceLocked != null && (isViewFocusedLocked(i2) || isRequestSupportFillDialog(i2))) {
            Consumer<InlineSuggestionsRequest> newAutofillRequestLocked = this.mAssistReceiver.newAutofillRequestLocked(viewState, true);
            if (newAutofillRequestLocked != null) {
                remoteInlineSuggestionRenderServiceLocked.getInlineSuggestionsRendererInfo(new RemoteCallback(new InlineSuggestionRendorInfoCallbackOnResultListener(new WeakReference(this), andIncrement, newAutofillRequestLocked, this.mCurrentViewId), this.mHandler));
                viewState.setState(65536);
            }
        } else {
            this.mAssistReceiver.newAutofillRequestLocked(viewState, false);
        }
        requestAssistStructureLocked(andIncrement, i2);
    }

    @GuardedBy({"mLock"})
    private void requestAssistStructureForPccLocked(int i) {
        int andIncrement;
        if (this.mClassificationState.shouldTriggerRequest()) {
            this.mClassificationState.updatePendingRequest();
            do {
                andIncrement = sIdCounterForPcc.getAndIncrement();
            } while (andIncrement == Integer.MIN_VALUE);
            if (Helper.sVerbose) {
                Slog.v(TAG, "request id is " + andIncrement + ", requesting assist structure for pcc");
            }
            try {
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_REQUEST_ID, andIncrement);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!ActivityTaskManager.getService().requestAutofillData(this.mPccAssistReceiver, bundle, this.mActivityToken, i)) {
                        Slog.w(TAG, "failed to request autofill data for " + this.mActivityToken);
                    }
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
            } catch (RemoteException unused) {
            }
        }
    }

    @GuardedBy({"mLock"})
    private void requestAssistStructureLocked(int i, int i2) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_REQUEST_ID, i);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                if (!ActivityTaskManager.getService().requestAutofillData(this.mAssistReceiver, bundle, this.mActivityToken, i2)) {
                    Slog.w(TAG, "failed to request autofill data for " + this.mActivityToken);
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        } catch (RemoteException unused) {
        }
    }

    public Session(AutofillManagerServiceImpl autofillManagerServiceImpl, AutoFillUI autoFillUI, Context context, Handler handler, int i, Object obj, int i2, int i3, int i4, IBinder iBinder, IBinder iBinder2, boolean z, LocalLog localLog, LocalLog localLog2, ComponentName componentName, ComponentName componentName2, boolean z2, boolean z3, boolean z4, int i5, InputMethodManagerInternal inputMethodManagerInternal) {
        MetricsLogger metricsLogger = new MetricsLogger();
        this.mMetricsLogger = metricsLogger;
        this.mSessionState = 0;
        this.mViewStates = new ArrayMap<>();
        this.mRequestLogs = new SparseArray<>(1);
        this.mAssistReceiver = new AssistDataReceiverImpl();
        this.mPccAssistReceiver = new PccAssistDataReceiverImpl();
        this.mClassificationState = new ClassificationState();
        this.mDelayedFillBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.autofill.Session.1
            AnonymousClass1() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (!intent.getAction().equals(Session.ACTION_DELAYED_FILL)) {
                    Slog.wtf(Session.TAG, "Unexpected action is received.");
                    return;
                }
                if (!intent.hasExtra(Session.EXTRA_REQUEST_ID)) {
                    Slog.e(Session.TAG, "Delay fill action is missing request id extra.");
                    return;
                }
                Slog.v(Session.TAG, "mDelayedFillBroadcastReceiver delayed fill action received");
                synchronized (Session.this.mLock) {
                    Session.this.mAssistReceiver.processDelayedFillLocked(intent.getIntExtra(Session.EXTRA_REQUEST_ID, 0), (FillResponse) intent.getParcelableExtra("android.service.autofill.extra.FILL_RESPONSE", FillResponse.class));
                }
            }
        };
        if (i2 < 0) {
            wtf(null, "Non-positive sessionId: %s", Integer.valueOf(i2));
        }
        this.id = i2;
        this.mFlags = i5;
        this.userId = i;
        this.taskId = i3;
        this.uid = i4;
        this.mService = autofillManagerServiceImpl;
        this.mLock = obj;
        this.mUi = autoFillUI;
        this.mHandler = handler;
        this.mRemoteFillService = componentName == null ? null : new RemoteFillService(context, componentName, i, this, z3);
        this.mActivityToken = iBinder;
        this.mHasCallback = z;
        this.mUiLatencyHistory = localLog;
        this.mWtfHistory = localLog2;
        this.mContext = Helper.getDisplayContext(context, ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).getDisplayId(iBinder));
        this.mComponentName = componentName2;
        this.mCompatMode = z2;
        this.mSessionState = 1;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mStartTime = elapsedRealtime;
        this.mLatencyBaseTime = elapsedRealtime;
        this.mRequestCount = 0;
        this.mPresentationStatsEventLogger = PresentationStatsEventLogger.forSessionId(i2);
        this.mFillRequestEventLogger = FillRequestEventLogger.forSessionId(i2);
        this.mFillResponseEventLogger = FillResponseEventLogger.forSessionId(i2);
        SessionCommittedEventLogger forSessionId = SessionCommittedEventLogger.forSessionId(i2);
        this.mSessionCommittedEventLogger = forSessionId;
        forSessionId.maybeSetComponentPackageUid(i4);
        this.mSaveEventLogger = SaveEventLogger.forSessionId(i2);
        this.mSessionExt.setRemoteServiceComponentName(componentName);
        synchronized (obj) {
            SessionFlags sessionFlags = new SessionFlags();
            this.mSessionFlags = sessionFlags;
            sessionFlags.mAugmentedAutofillOnly = z4;
            sessionFlags.mInlineSupportedByService = autofillManagerServiceImpl.isInlineSuggestionsEnabledLocked();
            setClientLocked(iBinder2);
        }
        this.mInlineSessionController = new AutofillInlineSessionController(inputMethodManagerInternal, i, componentName2, handler, obj, new InlineFillUi.InlineUiEventCallback() { // from class: com.android.server.autofill.Session.2
            AnonymousClass2() {
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback
            public void notifyInlineUiShown(AutofillId autofillId) {
                Session.this.notifyFillUiShown(autofillId);
                synchronized (Session.this.mLock) {
                    Session.this.mPresentationStatsEventLogger.maybeSetSuggestionPresentedTimestampMs((int) (SystemClock.elapsedRealtime() - Session.this.mLatencyBaseTime));
                }
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback
            public void notifyInlineUiHidden(AutofillId autofillId) {
                Session.this.notifyFillUiHidden(autofillId);
            }
        });
        metricsLogger.write(newLogMaker(906).addTaggedData(1452, Integer.valueOf(i5)));
        this.mLogViewEntered = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.autofill.Session$2 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass2 implements InlineFillUi.InlineUiEventCallback {
        AnonymousClass2() {
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback
        public void notifyInlineUiShown(AutofillId autofillId) {
            Session.this.notifyFillUiShown(autofillId);
            synchronized (Session.this.mLock) {
                Session.this.mPresentationStatsEventLogger.maybeSetSuggestionPresentedTimestampMs((int) (SystemClock.elapsedRealtime() - Session.this.mLatencyBaseTime));
            }
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback
        public void notifyInlineUiHidden(AutofillId autofillId) {
            Session.this.notifyFillUiHidden(autofillId);
        }
    }

    @GuardedBy({"mLock"})
    public IBinder getActivityTokenLocked() {
        return this.mActivityToken;
    }

    public void switchActivity(IBinder iBinder, IBinder iBinder2) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#switchActivity() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mActivityToken = iBinder;
            setClientLocked(iBinder2);
            updateTrackedIdsLocked();
        }
    }

    @GuardedBy({"mLock"})
    private void setClientLocked(IBinder iBinder) {
        unlinkClientVultureLocked();
        this.mClient = IAutoFillManagerClient.Stub.asInterface(iBinder);
        this.mClientVulture = new IBinder.DeathRecipient() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda2
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                Session.this.lambda$setClientLocked$0();
            }
        };
        try {
            this.mClient.asBinder().linkToDeath(this.mClientVulture, 0);
        } catch (RemoteException e) {
            Slog.w(TAG, "could not set binder death listener on autofill client: " + e);
            this.mClientVulture = null;
        }
    }

    public /* synthetic */ void lambda$setClientLocked$0() {
        synchronized (this.mLock) {
            Slog.d(TAG, "handling death of " + this.mActivityToken + " when saving=" + this.mSessionFlags.mShowingSaveUi);
            if (this.mSessionFlags.mShowingSaveUi) {
                this.mUi.hideFillUi(this);
            } else {
                this.mUi.destroyAll(this.mPendingSaveUi, this, false);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void unlinkClientVultureLocked() {
        IAutoFillManagerClient iAutoFillManagerClient = this.mClient;
        if (iAutoFillManagerClient == null || this.mClientVulture == null) {
            return;
        }
        if (!iAutoFillManagerClient.asBinder().unlinkToDeath(this.mClientVulture, 0)) {
            Slog.w(TAG, "unlinking vulture from death failed for " + this.mActivityToken);
        }
        this.mClientVulture = null;
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestSuccess(int i, FillResponse fillResponse, String str, int i2) {
        long j;
        this.mFillResponseEventLogger.startLogForNewResponse();
        this.mFillResponseEventLogger.maybeSetRequestId(i);
        this.mFillResponseEventLogger.maybeSetAppPackageUid(this.uid);
        this.mFillResponseEventLogger.maybeSetResponseStatus(2);
        this.mFillResponseEventLogger.startResponseProcessingTime();
        int elapsedRealtime = (int) (SystemClock.elapsedRealtime() - this.mLatencyBaseTime);
        this.mPresentationStatsEventLogger.maybeSetFillResponseReceivedTimestampMs(elapsedRealtime);
        this.mFillResponseEventLogger.maybeSetLatencyFillResponseReceivedMillis(elapsedRealtime);
        this.mFillResponseEventLogger.maybeSetDetectionPreference(getDetectionPreferenceForLogging());
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#onFillRequestSuccess() rejected - session: " + this.id + " destroyed");
                this.mFillResponseEventLogger.maybeSetResponseStatus(5);
                this.mFillResponseEventLogger.logAndEndEvent();
                return;
            }
            LogMaker logMaker = this.mRequestLogs.get(i);
            if (logMaker != null) {
                logMaker.setType(10);
            } else {
                Slog.w(TAG, "onFillRequestSuccess(): no request log for id " + i);
            }
            if (fillResponse == null) {
                this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(0);
                if (logMaker != null) {
                    logMaker.addTaggedData(909, -1);
                }
                processNullResponseLocked(i, i2);
                return;
            }
            AutofillId[] fieldClassificationIds = fillResponse.getFieldClassificationIds();
            if (fieldClassificationIds != null && !this.mService.isFieldClassificationEnabledLocked()) {
                Slog.w(TAG, "Ignoring " + fillResponse + " because field detection is disabled");
                processNullResponseLocked(i, i2);
                return;
            }
            this.mLastFillDialogTriggerIds = fillResponse.getFillDialogTriggerIds();
            if ((fillResponse.getFlags() & 4) != 0) {
                Slog.v(TAG, "Service requested to wait for delayed fill response.");
                registerDelayedFillBroadcastLocked();
            }
            this.mService.setLastResponse(this.id, fillResponse);
            synchronized (this.mLock) {
                if (this.mLogViewEntered) {
                    this.mLogViewEntered = false;
                    this.mService.logViewEntered(this.id, null);
                }
            }
            long disableDuration = fillResponse.getDisableDuration();
            boolean z = disableDuration > 0;
            if (z) {
                int flags = fillResponse.getFlags();
                boolean z2 = (flags & 2) != 0;
                notifyDisableAutofillToClient(disableDuration, z2 ? this.mComponentName : null);
                if (z2) {
                    j = disableDuration;
                    this.mService.disableAutofillForActivity(this.mComponentName, disableDuration, this.id, this.mCompatMode);
                } else {
                    j = disableDuration;
                    this.mService.disableAutofillForApp(this.mComponentName.getPackageName(), disableDuration, this.id, this.mCompatMode);
                }
                synchronized (this.mLock) {
                    this.mSessionFlags.mAutofillDisabled = true;
                    if (triggerAugmentedAutofillLocked(i2) != null) {
                        this.mSessionFlags.mAugmentedAutofillOnly = true;
                        if (Helper.sDebug) {
                            Slog.d(TAG, "Service disabled autofill for " + this.mComponentName + ", but session is kept for augmented autofill only");
                        }
                        return;
                    }
                    if (Helper.sDebug) {
                        StringBuilder sb = new StringBuilder("Service disabled autofill for ");
                        sb.append(this.mComponentName);
                        sb.append(": flags=");
                        sb.append(flags);
                        sb.append(", duration=");
                        TimeUtils.formatDuration(j, sb);
                        Slog.d(TAG, sb.toString());
                    }
                }
            }
            List datasets = fillResponse.getDatasets();
            if (((datasets == null || datasets.isEmpty()) && fillResponse.getAuthentication() == null) || z) {
                notifyUnavailableToClient(z ? 4 : 0, null);
                synchronized (this.mLock) {
                    this.mInlineSessionController.setInlineFillUiLocked(InlineFillUi.emptyUi(this.mCurrentViewId));
                }
            }
            if (logMaker != null) {
                logMaker.addTaggedData(909, Integer.valueOf(fillResponse.getDatasets() == null ? 0 : fillResponse.getDatasets().size()));
                if (fieldClassificationIds != null) {
                    logMaker.addTaggedData(1271, Integer.valueOf(fieldClassificationIds.length));
                }
            }
            int size = datasets == null ? 0 : datasets.size();
            this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(size);
            this.mFillResponseEventLogger.maybeSetAvailableCount(size);
            processResponseLockedForPcc(fillResponse, fillResponse.getClientState(), i2);
            this.mFillResponseEventLogger.maybeSetLatencyResponseProcessingMillis();
        }
    }

    @GuardedBy({"mLock"})
    private void processResponseLockedForPcc(FillResponse fillResponse, Bundle bundle, int i) {
        synchronized (this.mLock) {
            FillResponse effectiveFillResponse = getEffectiveFillResponse(fillResponse);
            if (isEmptyResponse(effectiveFillResponse)) {
                processNullResponseLocked(effectiveFillResponse != null ? effectiveFillResponse.getRequestId() : 0, i);
            } else {
                processResponseLocked(effectiveFillResponse, bundle, i);
            }
        }
    }

    private boolean isEmptyResponse(FillResponse fillResponse) {
        boolean z = true;
        if (fillResponse == null) {
            return true;
        }
        SaveInfo saveInfo = fillResponse.getSaveInfo();
        synchronized (this.mLock) {
            if ((fillResponse.getDatasets() != null && !fillResponse.getDatasets().isEmpty()) || fillResponse.getAuthentication() != null || ((saveInfo != null && (!ArrayUtils.isEmpty(saveInfo.getOptionalIds()) || !ArrayUtils.isEmpty(saveInfo.getRequiredIds()) || (saveInfo.getFlags() & 4) != 0)) || !ArrayUtils.isEmpty(fillResponse.getFieldClassificationIds()))) {
                z = false;
            }
        }
        return z;
    }

    private FillResponse getEffectiveFillResponse(FillResponse fillResponse) {
        DatasetComputationContainer datasetComputationContainer = new DatasetComputationContainer();
        computeDatasetsForProviderAndUpdateContainer(fillResponse, datasetComputationContainer);
        if (!this.mService.isPccClassificationEnabled()) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "PCC classification is disabled");
            }
            return createShallowCopy(fillResponse, datasetComputationContainer);
        }
        synchronized (this.mLock) {
            if (this.mClassificationState.mState == 4 && this.mClassificationState.mLastFieldClassificationResponse != null) {
                if (!this.mClassificationState.processResponse()) {
                    return fillResponse;
                }
                boolean preferProviderOverPcc = ((AutofillManagerService) this.mService.getMaster()).preferProviderOverPcc();
                boolean shouldUsePccFallback = ((AutofillManagerService) this.mService.getMaster()).shouldUsePccFallback();
                if (preferProviderOverPcc && !shouldUsePccFallback) {
                    if (Helper.sVerbose) {
                        Slog.v(TAG, "preferAutofillProvider but no fallback");
                    }
                    return createShallowCopy(fillResponse, datasetComputationContainer);
                }
                DatasetComputationContainer datasetComputationContainer2 = new DatasetComputationContainer();
                computeDatasetsForPccAndUpdateContainer(fillResponse, datasetComputationContainer2);
                if (!preferProviderOverPcc) {
                    if (shouldUsePccFallback) {
                        addFallbackDatasets(datasetComputationContainer2, datasetComputationContainer);
                    }
                    datasetComputationContainer = datasetComputationContainer2;
                } else if (shouldUsePccFallback) {
                    addFallbackDatasets(datasetComputationContainer, datasetComputationContainer2);
                }
                return createShallowCopy(fillResponse, datasetComputationContainer);
            }
            if (Helper.sVerbose) {
                StringBuilder sb = new StringBuilder();
                sb.append("PCC classification no last response:");
                boolean z = true;
                sb.append(this.mClassificationState.mLastFieldClassificationResponse == null);
                sb.append(" ,ineligible state=");
                if (this.mClassificationState.mState == 4) {
                    z = false;
                }
                sb.append(z);
                Slog.v(TAG, sb.toString());
            }
            return createShallowCopy(fillResponse, datasetComputationContainer);
        }
    }

    private FillResponse createShallowCopy(FillResponse fillResponse, DatasetComputationContainer datasetComputationContainer) {
        return FillResponse.shallowCopy(fillResponse, new ArrayList(datasetComputationContainer.mDatasets), getEligibleSaveInfo(fillResponse));
    }

    private SaveInfo getEligibleSaveInfo(FillResponse fillResponse) {
        SaveInfo saveInfo = fillResponse.getSaveInfo();
        if (saveInfo == null || !ArrayUtils.isEmpty(saveInfo.getOptionalIds()) || !ArrayUtils.isEmpty(saveInfo.getRequiredIds()) || (saveInfo.getFlags() & 4) != 0) {
            return saveInfo;
        }
        synchronized (this.mLock) {
            ArrayMap arrayMap = this.mClassificationState.mHintsToAutofillIdMap;
            if (arrayMap != null && !arrayMap.isEmpty()) {
                ArraySet arraySet = new ArraySet();
                int type = saveInfo.getType();
                if (type == 0) {
                    Iterator it = arrayMap.values().iterator();
                    while (it.hasNext()) {
                        arraySet.addAll((Set) it.next());
                    }
                } else {
                    Set<String> hintsForSaveType = HintsHelper.getHintsForSaveType(type);
                    for (Map.Entry entry : arrayMap.entrySet()) {
                        if (hintsForSaveType.contains((String) entry.getKey())) {
                            arraySet.addAll((Collection) entry.getValue());
                        }
                    }
                }
                if (arraySet.isEmpty()) {
                    return saveInfo;
                }
                AutofillId[] autofillIdArr = new AutofillId[arraySet.size()];
                this.mSaveEventLogger.maybeSetIsFrameworkCreatedSaveInfo(true);
                arraySet.toArray(autofillIdArr);
                return SaveInfo.copy(saveInfo, autofillIdArr);
            }
            return saveInfo;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DatasetComputationContainer {
        ArrayMap<AutofillId, Set<Dataset>> mAutofillIdToDatasetMap;
        Set<AutofillId> mAutofillIds;
        Set<Dataset> mDatasets;

        /* synthetic */ DatasetComputationContainer(DatasetComputationContainerIA datasetComputationContainerIA) {
            this();
        }

        private DatasetComputationContainer() {
            this.mAutofillIds = new ArraySet();
            this.mDatasets = new LinkedHashSet();
            this.mAutofillIdToDatasetMap = new ArrayMap<>();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("DatasetComputationContainer[");
            if (this.mAutofillIds != null) {
                sb.append(", autofillIds=");
                sb.append(this.mAutofillIds);
            }
            if (this.mDatasets != null) {
                sb.append(", mDatasets=");
                sb.append(this.mDatasets);
            }
            if (this.mAutofillIdToDatasetMap != null) {
                sb.append(", mAutofillIdToDatasetMap=");
                sb.append(this.mAutofillIdToDatasetMap);
            }
            sb.append(']');
            return sb.toString();
        }
    }

    private void addFallbackDatasets(DatasetComputationContainer datasetComputationContainer, DatasetComputationContainer datasetComputationContainer2) {
        for (AutofillId autofillId : datasetComputationContainer2.mAutofillIds) {
            if (!datasetComputationContainer.mAutofillIds.contains(autofillId)) {
                if (datasetComputationContainer2.mAutofillIdToDatasetMap.get(autofillId).isEmpty()) {
                    return;
                }
                Set<Dataset> set = datasetComputationContainer2.mAutofillIdToDatasetMap.get(autofillId);
                ArraySet arraySet = new ArraySet(set);
                datasetComputationContainer.mAutofillIds.add(autofillId);
                datasetComputationContainer.mAutofillIdToDatasetMap.put(autofillId, arraySet);
                datasetComputationContainer.mDatasets.addAll(arraySet);
                for (Dataset dataset : set) {
                    Iterator it = dataset.getFieldIds().iterator();
                    while (it.hasNext()) {
                        AutofillId autofillId2 = (AutofillId) it.next();
                        if (!autofillId2.equals(autofillId)) {
                            datasetComputationContainer2.mAutofillIdToDatasetMap.get(autofillId2).remove(dataset);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0132  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void computeDatasetsForProviderAndUpdateContainer(FillResponse fillResponse, DatasetComputationContainer datasetComputationContainer) {
        int i;
        Iterator it;
        int i2 = this.mService.isPccClassificationEnabled() ? 2 : 1;
        if (fillResponse.getDatasets() == null) {
            return;
        }
        ArrayMap<AutofillId, Set<Dataset>> arrayMap = new ArrayMap<>();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        ArraySet arraySet = new ArraySet();
        for (Dataset dataset : fillResponse.getDatasets()) {
            if (dataset.getFieldIds() != null && !dataset.getFieldIds().isEmpty()) {
                if (dataset.getAutofillDatatypes() != null && !dataset.getAutofillDatatypes().isEmpty()) {
                    int size = dataset.getFieldIds().size();
                    Iterator it2 = dataset.getFieldIds().iterator();
                    boolean z = false;
                    while (it2.hasNext()) {
                        if (((AutofillId) it2.next()) == null) {
                            size--;
                            z = true;
                        }
                    }
                    if (size != 0) {
                        if (z) {
                            ArrayList arrayList = new ArrayList(size);
                            ArrayList arrayList2 = new ArrayList(size);
                            ArrayList arrayList3 = new ArrayList(size);
                            ArrayList arrayList4 = new ArrayList(size);
                            ArrayList arrayList5 = new ArrayList(size);
                            ArrayList arrayList6 = new ArrayList(size);
                            ArrayList arrayList7 = new ArrayList(size);
                            for (int i3 = 0; i3 < dataset.getFieldIds().size(); i3++) {
                                AutofillId autofillId = (AutofillId) dataset.getFieldIds().get(i3);
                                if (autofillId != null) {
                                    arrayList.add(autofillId);
                                    arrayList2.add((AutofillValue) dataset.getFieldValues().get(i3));
                                    arrayList3.add(dataset.getFieldPresentation(i3));
                                    arrayList4.add(dataset.getFieldDialogPresentation(i3));
                                    arrayList5.add(dataset.getFieldInlinePresentation(i3));
                                    arrayList6.add(dataset.getFieldInlineTooltipPresentation(i3));
                                    arrayList7.add(dataset.getFilter(i3));
                                }
                            }
                            Dataset dataset2 = new Dataset(arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, arrayList7, new ArrayList(), dataset.getFieldContent(), null, null, null, null, dataset.getId(), dataset.getAuthentication());
                            i = 3;
                            dataset = dataset2;
                            dataset.setEligibleReasonReason(i);
                            linkedHashSet.add(dataset);
                            it = dataset.getFieldIds().iterator();
                            while (it.hasNext()) {
                                AutofillId autofillId2 = (AutofillId) it.next();
                                arraySet.add(autofillId2);
                                Set<Dataset> set = arrayMap.get(autofillId2);
                                if (set == null) {
                                    set = new ArraySet<>();
                                }
                                set.add(dataset);
                                arrayMap.put(autofillId2, set);
                            }
                        }
                    }
                }
                i = i2;
                dataset.setEligibleReasonReason(i);
                linkedHashSet.add(dataset);
                it = dataset.getFieldIds().iterator();
                while (it.hasNext()) {
                }
            }
        }
        datasetComputationContainer.mAutofillIdToDatasetMap = arrayMap;
        datasetComputationContainer.mDatasets = linkedHashSet;
        datasetComputationContainer.mAutofillIds = arraySet;
    }

    private void computeDatasetsForPccAndUpdateContainer(FillResponse fillResponse, DatasetComputationContainer datasetComputationContainer) {
        ArrayMap<AutofillId, Set<Dataset>> arrayMap;
        List list;
        LinkedHashSet linkedHashSet;
        int i;
        ArrayMap<AutofillId, Set<Dataset>> arrayMap2;
        Set<Dataset> arraySet;
        LinkedHashSet linkedHashSet2;
        DatasetComputationContainer datasetComputationContainer2 = datasetComputationContainer;
        List datasets = fillResponse.getDatasets();
        if (datasets == null) {
            return;
        }
        synchronized (this.mLock) {
            ArrayMap arrayMap3 = this.mClassificationState.mHintsToAutofillIdMap;
            ArrayMap unused = this.mClassificationState.mGroupHintsToAutofillIdMap;
            ArrayMap<AutofillId, Set<Dataset>> arrayMap4 = new ArrayMap<>();
            LinkedHashSet linkedHashSet3 = new LinkedHashSet();
            ArraySet arraySet2 = new ArraySet();
            int i2 = 0;
            while (i2 < datasets.size()) {
                Dataset dataset = (Dataset) datasets.get(i2);
                if (dataset.getAutofillDatatypes() != null && !dataset.getAutofillDatatypes().isEmpty()) {
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = new ArrayList();
                    ArrayList arrayList4 = new ArrayList();
                    ArrayList arrayList5 = new ArrayList();
                    ArrayList arrayList6 = new ArrayList();
                    ArrayList arrayList7 = new ArrayList();
                    list = datasets;
                    ArraySet<AutofillId> arraySet3 = new ArraySet();
                    i = i2;
                    int i3 = 4;
                    int i4 = 0;
                    while (true) {
                        arrayMap2 = arrayMap4;
                        if (i4 >= dataset.getAutofillDatatypes().size()) {
                            break;
                        }
                        if (dataset.getAutofillDatatypes().get(i4) == null) {
                            if (dataset.getFieldIds() != null && dataset.getFieldIds().get(i4) != null) {
                                i3 = 5;
                            }
                        } else {
                            String str = (String) dataset.getAutofillDatatypes().get(i4);
                            if (arrayMap3.containsKey(str)) {
                                linkedHashSet2 = linkedHashSet3;
                                Iterator it = new ArrayList((Collection) arrayMap3.get(str)).iterator();
                                while (it.hasNext()) {
                                    AutofillId autofillId = (AutofillId) it.next();
                                    arraySet2.add(autofillId);
                                    arraySet3.add(autofillId);
                                    arrayList.add(autofillId);
                                    arrayList2.add((AutofillValue) dataset.getFieldValues().get(i4));
                                    arrayList3.add(dataset.getFieldPresentation(i4));
                                    arrayList4.add(dataset.getFieldDialogPresentation(i4));
                                    arrayList5.add(dataset.getFieldInlinePresentation(i4));
                                    arrayList6.add(dataset.getFieldInlineTooltipPresentation(i4));
                                    arrayList7.add(dataset.getFilter(i4));
                                }
                                i4++;
                                arrayMap4 = arrayMap2;
                                linkedHashSet3 = linkedHashSet2;
                            }
                        }
                        linkedHashSet2 = linkedHashSet3;
                        i4++;
                        arrayMap4 = arrayMap2;
                        linkedHashSet3 = linkedHashSet2;
                    }
                    Dataset dataset2 = new Dataset(arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, arrayList7, new ArrayList(), dataset.getFieldContent(), null, null, null, null, dataset.getId(), dataset.getAuthentication());
                    dataset2.setEligibleReasonReason(i3);
                    linkedHashSet = linkedHashSet3;
                    linkedHashSet.add(dataset2);
                    for (AutofillId autofillId2 : arraySet3) {
                        ArrayMap<AutofillId, Set<Dataset>> arrayMap5 = arrayMap2;
                        if (arrayMap5.containsKey(autofillId2)) {
                            arraySet = arrayMap5.get(autofillId2);
                        } else {
                            arraySet = new ArraySet<>();
                        }
                        arraySet.add(dataset2);
                        arrayMap5.put(autofillId2, arraySet);
                        arrayMap2 = arrayMap5;
                    }
                    arrayMap = arrayMap2;
                    i2 = i + 1;
                    datasets = list;
                    linkedHashSet3 = linkedHashSet;
                    arrayMap4 = arrayMap;
                    datasetComputationContainer2 = datasetComputationContainer;
                }
                arrayMap = arrayMap4;
                list = datasets;
                linkedHashSet = linkedHashSet3;
                i = i2;
                i2 = i + 1;
                datasets = list;
                linkedHashSet3 = linkedHashSet;
                arrayMap4 = arrayMap;
                datasetComputationContainer2 = datasetComputationContainer;
            }
            ArrayMap<AutofillId, Set<Dataset>> arrayMap6 = arrayMap4;
            DatasetComputationContainer datasetComputationContainer3 = datasetComputationContainer2;
            datasetComputationContainer3.mAutofillIds = arraySet2;
            datasetComputationContainer3.mDatasets = linkedHashSet3;
            datasetComputationContainer3.mAutofillIdToDatasetMap = arrayMap6;
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestFailure(int i, CharSequence charSequence) {
        onFillRequestFailureOrTimeout(i, false, charSequence);
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestTimeout(int i) {
        onFillRequestFailureOrTimeout(i, true, null);
    }

    private void onFillRequestFailureOrTimeout(int i, boolean z, CharSequence charSequence) {
        boolean z2 = !TextUtils.isEmpty(charSequence);
        this.mFillResponseEventLogger.startLogForNewResponse();
        this.mFillResponseEventLogger.maybeSetRequestId(i);
        this.mFillResponseEventLogger.maybeSetAppPackageUid(this.uid);
        this.mFillResponseEventLogger.maybeSetAvailableCount(-1);
        this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(-1);
        this.mFillResponseEventLogger.maybeSetDetectionPreference(getDetectionPreferenceForLogging());
        this.mFillResponseEventLogger.maybeSetLatencyFillResponseReceivedMillis((int) (SystemClock.elapsedRealtime() - this.mLatencyBaseTime));
        synchronized (this.mLock) {
            unregisterDelayedFillBroadcastLocked();
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#onFillRequestFailureOrTimeout(req=" + i + ") rejected - session: " + this.id + " destroyed");
                this.mFillResponseEventLogger.maybeSetResponseStatus(5);
                this.mFillResponseEventLogger.logAndEndEvent();
                return;
            }
            if (Helper.sDebug) {
                StringBuilder sb = new StringBuilder();
                sb.append("finishing session due to service ");
                sb.append(z ? "timeout" : "failure");
                Slog.d(TAG, sb.toString());
            }
            this.mService.resetLastResponse();
            this.mLastFillDialogTriggerIds = null;
            LogMaker logMaker = this.mRequestLogs.get(i);
            if (logMaker == null) {
                Slog.w(TAG, "onFillRequestFailureOrTimeout(): no log for id " + i);
            } else {
                logMaker.setType(z ? 2 : 11);
            }
            if (z2) {
                int targedSdkLocked = this.mService.getTargedSdkLocked();
                if (targedSdkLocked >= 29) {
                    Slog.w(TAG, "onFillRequestFailureOrTimeout(): not showing '" + ((Object) charSequence) + "' because service's targetting API " + targedSdkLocked);
                    z2 = false;
                }
                if (charSequence != null) {
                    logMaker.addTaggedData(1572, Integer.valueOf(charSequence.length()));
                }
            }
            if (z) {
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(5);
                this.mFillResponseEventLogger.maybeSetResponseStatus(4);
            } else {
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(7);
                this.mFillResponseEventLogger.maybeSetResponseStatus(1);
            }
            this.mPresentationStatsEventLogger.logAndEndEvent();
            this.mFillResponseEventLogger.logAndEndEvent();
            notifyUnavailableToClient(6, null);
            if (z2) {
                getUiForShowing().showError(charSequence, this);
            }
            removeFromService();
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onSaveRequestSuccess(String str, IntentSender intentSender) {
        synchronized (this.mLock) {
            this.mSessionFlags.mShowingSaveUi = false;
            this.mSaveEventLogger.maybeSetIsSaved(true);
            this.mSaveEventLogger.maybeSetLatencySaveFinishMillis(SystemClock.elapsedRealtime() - this.mLatencyBaseTime);
            this.mSaveEventLogger.logAndEndEvent();
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#onSaveRequestSuccess() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mMetricsLogger.write(newLogMaker(918, str).setType(intentSender == null ? 10 : 1));
            if (intentSender != null) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "Starting intent sender on save()");
                }
                startIntentSenderAndFinishSession(intentSender);
            }
            removeFromService();
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onSaveRequestFailure(CharSequence charSequence, String str) {
        int targedSdkLocked;
        boolean z = !TextUtils.isEmpty(charSequence);
        synchronized (this.mLock) {
            this.mSessionFlags.mShowingSaveUi = false;
            this.mSaveEventLogger.maybeSetLatencySaveFinishMillis(SystemClock.elapsedRealtime() - this.mLatencyBaseTime);
            this.mSaveEventLogger.logAndEndEvent();
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#onSaveRequestFailure() rejected - session: " + this.id + " destroyed");
                return;
            }
            if (z && (targedSdkLocked = this.mService.getTargedSdkLocked()) >= 29) {
                Slog.w(TAG, "onSaveRequestFailure(): not showing '" + ((Object) charSequence) + "' because service's targetting API " + targedSdkLocked);
                z = false;
            }
            LogMaker type = newLogMaker(918, str).setType(11);
            if (charSequence != null) {
                type.addTaggedData(1572, Integer.valueOf(charSequence.length()));
            }
            this.mMetricsLogger.write(type);
            if (z) {
                getUiForShowing().showError(charSequence, this);
            }
            removeFromService();
        }
    }

    @GuardedBy({"mLock"})
    private FillContext getFillContextByRequestIdLocked(int i) {
        ArrayList<FillContext> arrayList = this.mContexts;
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            FillContext fillContext = this.mContexts.get(i2);
            if (fillContext.getRequestId() == i) {
                return fillContext;
            }
        }
        return null;
    }

    public void onServiceDied(RemoteFillService remoteFillService) {
        Slog.w(TAG, "removing session because service died");
        synchronized (this.mLock) {
            forceRemoveFromServiceLocked();
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void authenticate(int i, int i2, IntentSender intentSender, Bundle bundle, int i3) {
        if (Helper.sDebug) {
            Slog.d(TAG, "authenticate(): requestId=" + i + "; datasetIdx=" + i2 + "; intentSender=" + intentSender);
        }
        synchronized (this.mLock) {
            this.mPresentationStatsEventLogger.maybeSetAuthenticationType(2);
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#authenticate() rejected - session: " + this.id + " destroyed");
                return;
            }
            Intent createAuthFillInIntentLocked = createAuthFillInIntentLocked(i, bundle);
            if (createAuthFillInIntentLocked == null) {
                forceRemoveFromServiceLocked();
                return;
            }
            this.mService.setAuthenticationSelected(this.id, this.mClientState, i3);
            this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda5
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((Session) obj).startAuthentication(((Integer) obj2).intValue(), (IntentSender) obj3, (Intent) obj4, ((Boolean) obj5).booleanValue());
                }
            }, this, Integer.valueOf(AutofillManager.makeAuthenticationId(i, i2)), intentSender, createAuthFillInIntentLocked, Boolean.valueOf(i3 == 2)));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void fill(int i, int i2, Dataset dataset, int i3) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#fill() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mHandler.sendMessage(PooledLambda.obtainMessage(new HexConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda3
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                    ((Session) obj).autoFill(((Integer) obj2).intValue(), ((Integer) obj3).intValue(), (Dataset) obj4, ((Boolean) obj5).booleanValue(), ((Integer) obj6).intValue());
                }
            }, this, Integer.valueOf(i), Integer.valueOf(i2), dataset, Boolean.TRUE, Integer.valueOf(i3)));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void save() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#save() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mSaveEventLogger.maybeSetLatencySaveRequestMillis(SystemClock.elapsedRealtime() - this.mLatencyBaseTime);
            this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda7
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((AutofillManagerServiceImpl) obj).handleSessionSave((Session) obj2);
                }
            }, this.mService, this));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void cancelSave() {
        synchronized (this.mLock) {
            this.mSessionFlags.mShowingSaveUi = false;
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#cancelSave() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((Session) obj).removeFromService();
                }
            }, this));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void onShown(int i) {
        synchronized (this.mLock) {
            if (i == 2) {
                if (this.mLoggedInlineDatasetShown) {
                    return;
                } else {
                    this.mLoggedInlineDatasetShown = true;
                }
            }
            this.mService.logDatasetShown(this.id, this.mClientState, i);
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestShowFillUi(AutofillId autofillId, int i, int i2, IAutofillWindowPresenter iAutofillWindowPresenter) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#requestShowFillUi() rejected - session: " + autofillId + " destroyed");
                return;
            }
            if (autofillId.equals(this.mCurrentViewId)) {
                try {
                    this.mClient.requestShowFillUi(this.id, autofillId, i, i2, this.mViewStates.get(autofillId).getVirtualBounds(), iAutofillWindowPresenter);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Error requesting to show fill UI", e);
                }
            } else if (Helper.sDebug) {
                Slog.d(TAG, "Do not show full UI on " + autofillId + " as it is not the current view (" + this.mCurrentViewId + ") anymore");
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void dispatchUnhandledKey(AutofillId autofillId, KeyEvent keyEvent) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#dispatchUnhandledKey() rejected - session: " + autofillId + " destroyed");
                return;
            }
            if (autofillId.equals(this.mCurrentViewId)) {
                try {
                    this.mClient.dispatchUnhandledKey(this.id, autofillId, keyEvent);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Error requesting to dispatch unhandled key", e);
                }
            } else {
                Slog.w(TAG, "Do not dispatch unhandled key on " + autofillId + " as it is not the current view (" + this.mCurrentViewId + ") anymore");
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestHideFillUi(AutofillId autofillId) {
        synchronized (this.mLock) {
            try {
                this.mClient.requestHideFillUi(this.id, autofillId);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error requesting to hide fill UI", e);
            }
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(autofillId);
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void cancelSession() {
        synchronized (this.mLock) {
            removeFromServiceLocked();
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void startIntentSenderAndFinishSession(IntentSender intentSender) {
        startIntentSender(intentSender, null);
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void startIntentSender(IntentSender intentSender, Intent intent) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#startIntentSender() rejected - session: " + this.id + " destroyed");
                return;
            }
            if (intent == null) {
                removeFromServiceLocked();
            }
            this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda6
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((Session) obj).doStartIntentSender((IntentSender) obj2, (Intent) obj3);
                }
            }, this, intentSender, intent));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestShowSoftInput(AutofillId autofillId) {
        IAutoFillManagerClient client = getClient();
        if (client != null) {
            try {
                client.requestShowSoftInput(autofillId);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error sending input show up notification", e);
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestFallbackFromFillDialog() {
        setFillDialogDisabled();
        synchronized (this.mLock) {
            AutofillId autofillId = this.mCurrentViewId;
            if (autofillId == null) {
                return;
            }
            this.mViewStates.get(autofillId).maybeCallOnFillReady(this.mFlags);
        }
    }

    public void notifyFillUiHidden(AutofillId autofillId) {
        synchronized (this.mLock) {
            try {
                this.mClient.notifyFillUiHidden(this.id, autofillId);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error sending fill UI hidden notification", e);
            }
        }
    }

    public void notifyFillUiShown(AutofillId autofillId) {
        synchronized (this.mLock) {
            try {
                this.mClient.notifyFillUiShown(this.id, autofillId);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error sending fill UI shown notification", e);
            }
        }
    }

    public void doStartIntentSender(IntentSender intentSender, Intent intent) {
        try {
            synchronized (this.mLock) {
                this.mClient.startIntentSender(intentSender, intent);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Error launching auth intent", e);
        }
    }

    @GuardedBy({"mLock"})
    public void setAuthenticationResultLocked(Bundle bundle, int i) {
        if (this.mDestroyed) {
            Slog.w(TAG, "Call to Session#setAuthenticationResultLocked() rejected - session: " + this.id + " destroyed");
            return;
        }
        int requestIdFromAuthenticationId = AutofillManager.getRequestIdFromAuthenticationId(i);
        if (requestIdFromAuthenticationId == 1) {
            setAuthenticationResultForAugmentedAutofillLocked(bundle, i);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            return;
        }
        SparseArray<FillResponse> sparseArray = this.mResponses;
        if (sparseArray == null) {
            Slog.w(TAG, "setAuthenticationResultLocked(" + i + "): no responses");
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            removeFromService();
            return;
        }
        FillResponse fillResponse = sparseArray.get(requestIdFromAuthenticationId);
        if (fillResponse == null || bundle == null) {
            Slog.w(TAG, "no authenticated response");
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            removeFromService();
            return;
        }
        int datasetIdFromAuthenticationId = AutofillManager.getDatasetIdFromAuthenticationId(i);
        if (datasetIdFromAuthenticationId != 65535 && ((Dataset) fillResponse.getDatasets().get(datasetIdFromAuthenticationId)) == null) {
            Slog.w(TAG, "no dataset with index " + datasetIdFromAuthenticationId + " on fill response");
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            removeFromService();
            return;
        }
        this.mSessionFlags.mExpiredResponse = false;
        Parcelable parcelable = bundle.getParcelable("android.view.autofill.extra.AUTHENTICATION_RESULT");
        Bundle bundle2 = bundle.getBundle("android.view.autofill.extra.CLIENT_STATE");
        if (Helper.sDebug) {
            Slog.d(TAG, "setAuthenticationResultLocked(): result=" + parcelable + ", clientState=" + bundle2 + ", authenticationId=" + i);
        }
        if (parcelable instanceof FillResponse) {
            logAuthenticationStatusLocked(requestIdFromAuthenticationId, FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_720P_HD_ALMOST);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(1);
            replaceResponseLocked(fillResponse, (FillResponse) parcelable, bundle2);
            return;
        }
        if (!(parcelable instanceof Dataset)) {
            if (parcelable != null) {
                Slog.w(TAG, "service returned invalid auth type: " + parcelable);
            }
            logAuthenticationStatusLocked(requestIdFromAuthenticationId, 1128);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            processNullResponseLocked(requestIdFromAuthenticationId, 0);
            return;
        }
        if (datasetIdFromAuthenticationId != 65535) {
            logAuthenticationStatusLocked(requestIdFromAuthenticationId, 1126);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(1);
            if (bundle2 != null) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "Updating client state from auth dataset");
                }
                this.mClientState = bundle2;
            }
            Dataset dataset = (Dataset) getEffectiveFillResponse(new FillResponse.Builder().addDataset((Dataset) parcelable).build()).getDatasets().get(0);
            if (!isAuthResultDatasetEphemeral((Dataset) fillResponse.getDatasets().get(datasetIdFromAuthenticationId), bundle)) {
                fillResponse.getDatasets().set(datasetIdFromAuthenticationId, dataset);
            }
            autoFill(requestIdFromAuthenticationId, datasetIdFromAuthenticationId, dataset, false, 0);
            return;
        }
        Slog.w(TAG, "invalid index (" + datasetIdFromAuthenticationId + ") for authentication id " + i);
        logAuthenticationStatusLocked(requestIdFromAuthenticationId, 1127);
        this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
    }

    private static boolean isAuthResultDatasetEphemeral(Dataset dataset, Bundle bundle) {
        if (bundle.containsKey("android.view.autofill.extra.AUTHENTICATION_RESULT_EPHEMERAL_DATASET")) {
            return bundle.getBoolean("android.view.autofill.extra.AUTHENTICATION_RESULT_EPHEMERAL_DATASET");
        }
        return isPinnedDataset(dataset);
    }

    private static boolean isPinnedDataset(Dataset dataset) {
        if (dataset != null && dataset.getFieldIds() != null) {
            int size = dataset.getFieldIds().size();
            for (int i = 0; i < size; i++) {
                InlinePresentation fieldInlinePresentation = dataset.getFieldInlinePresentation(i);
                if (fieldInlinePresentation != null && fieldInlinePresentation.isPinned()) {
                    return true;
                }
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    void setAuthenticationResultForAugmentedAutofillLocked(Bundle bundle, int i) {
        Dataset dataset = bundle == null ? null : (Dataset) bundle.getParcelable("android.view.autofill.extra.AUTHENTICATION_RESULT", Dataset.class);
        if (Helper.sDebug) {
            Slog.d(TAG, "Auth result for augmented autofill: sessionId=" + this.id + ", authId=" + i + ", dataset=" + dataset);
        }
        AutofillId autofillId = (dataset == null || dataset.getFieldIds().size() != 1) ? null : (AutofillId) dataset.getFieldIds().get(0);
        AutofillValue autofillValue = (dataset == null || dataset.getFieldValues().size() != 1) ? null : (AutofillValue) dataset.getFieldValues().get(0);
        ClipData fieldContent = dataset != null ? dataset.getFieldContent() : null;
        if (autofillId == null || (autofillValue == null && fieldContent == null)) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Rejecting empty/invalid auth result");
            }
            this.mService.resetLastAugmentedAutofillResponse();
            removeFromServiceLocked();
            return;
        }
        RemoteAugmentedAutofillService remoteAugmentedAutofillServiceIfCreatedLocked = this.mService.getRemoteAugmentedAutofillServiceIfCreatedLocked();
        if (remoteAugmentedAutofillServiceIfCreatedLocked == null) {
            Slog.e(TAG, "Can't fill after auth: RemoteAugmentedAutofillService is null");
            this.mService.resetLastAugmentedAutofillResponse();
            removeFromServiceLocked();
            return;
        }
        autofillId.setSessionId(this.id);
        this.mCurrentViewId = autofillId;
        this.mService.logAugmentedAutofillSelected(this.id, dataset.getId(), bundle.getBundle("android.view.autofill.extra.CLIENT_STATE"));
        if (fieldContent != null) {
            remoteAugmentedAutofillServiceIfCreatedLocked.getAutofillUriGrantsManager().grantUriPermissions(this.mComponentName, this.mActivityToken, this.userId, fieldContent);
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "Filling after auth: fieldId=" + autofillId + ", value=" + autofillValue + ", content=" + fieldContent);
        }
        try {
            if (fieldContent != null) {
                this.mClient.autofillContent(this.id, autofillId, fieldContent);
            } else {
                this.mClient.autofill(this.id, dataset.getFieldIds(), dataset.getFieldValues(), true);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Error filling after auth: fieldId=" + autofillId + ", value=" + autofillValue + ", content=" + fieldContent, e);
        }
        this.mInlineSessionController.setInlineFillUiLocked(InlineFillUi.emptyUi(autofillId));
    }

    @GuardedBy({"mLock"})
    public void setHasCallbackLocked(boolean z) {
        if (this.mDestroyed) {
            Slog.w(TAG, "Call to Session#setHasCallbackLocked() rejected - session: " + this.id + " destroyed");
            return;
        }
        this.mHasCallback = z;
    }

    @GuardedBy({"mLock"})
    private FillResponse getLastResponseLocked(String str) {
        String format = (!Helper.sDebug || str == null) ? null : String.format(str, Integer.valueOf(this.id));
        if (this.mContexts == null) {
            if (format != null) {
                Slog.d(TAG, format + ": no contexts");
            }
            return null;
        }
        if (this.mResponses == null) {
            if (Helper.sVerbose && format != null) {
                Slog.v(TAG, format + ": no responses on session");
            }
            return null;
        }
        int lastResponseIndexLocked = getLastResponseIndexLocked();
        if (lastResponseIndexLocked < 0) {
            if (format != null) {
                Slog.w(TAG, format + ": did not get last response. mResponses=" + this.mResponses + ", mViewStates=" + this.mViewStates);
            }
            return null;
        }
        FillResponse valueAt = this.mResponses.valueAt(lastResponseIndexLocked);
        if (Helper.sVerbose && format != null) {
            Slog.v(TAG, format + ": mResponses=" + this.mResponses + ", mContexts=" + this.mContexts + ", mViewStates=" + this.mViewStates);
        }
        return valueAt;
    }

    @GuardedBy({"mLock"})
    private SaveInfo getSaveInfoLocked() {
        FillResponse lastResponseLocked = getLastResponseLocked(null);
        if (lastResponseLocked == null) {
            return null;
        }
        return lastResponseLocked.getSaveInfo();
    }

    @GuardedBy({"mLock"})
    public int getSaveInfoFlagsLocked() {
        SaveInfo saveInfoLocked = getSaveInfoLocked();
        if (saveInfoLocked == null) {
            return 0;
        }
        return saveInfoLocked.getFlags();
    }

    public void logContextCommitted() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Session$$ExternalSyntheticLambda9(), this, 0, 0));
        logAllEvents(0);
    }

    public void logContextCommitted(int i, int i2) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Session$$ExternalSyntheticLambda9(), this, Integer.valueOf(i), Integer.valueOf(i2)));
        logAllEvents(i2);
    }

    public void handleLogContextCommitted(int i, int i2) {
        FillResponse lastResponseLocked;
        synchronized (this.mLock) {
            lastResponseLocked = getLastResponseLocked("logContextCommited(%s)");
        }
        if (lastResponseLocked == null) {
            Slog.w(TAG, "handleLogContextCommitted(): last response is null");
            return;
        }
        UserData userData = this.mService.getUserData();
        FieldClassificationUserData userData2 = lastResponseLocked.getUserData();
        if (userData2 == null && userData == null) {
            userData2 = null;
        } else if (userData2 != null && userData != null) {
            userData2 = new CompositeUserData(userData, userData2);
        } else if (userData2 == null) {
            userData2 = this.mService.getUserData();
        }
        FieldClassificationStrategy fieldClassificationStrategy = this.mService.getFieldClassificationStrategy();
        if (userData2 != null && fieldClassificationStrategy != null) {
            logFieldClassificationScore(fieldClassificationStrategy, userData2, i, i2);
        } else {
            logContextCommitted(null, null, i, i2);
        }
    }

    private void logContextCommitted(ArrayList<AutofillId> arrayList, ArrayList<FieldClassification> arrayList2, int i, int i2) {
        synchronized (this.mLock) {
            logContextCommittedLocked(arrayList, arrayList2, i, i2);
        }
    }

    @GuardedBy({"mLock"})
    private void logContextCommittedLocked(ArrayList<AutofillId> arrayList, ArrayList<FieldClassification> arrayList2, int i, int i2) {
        String str;
        ArrayList<AutofillId> arrayList3;
        ArrayList<ArrayList<String>> arrayList4;
        int i3;
        boolean z;
        String str2;
        int i4;
        AutofillValue autofillValue;
        boolean z2;
        String str3;
        ArrayList<String> arrayList5;
        String str4;
        List list;
        AutofillValue autofillValue2;
        ArrayList<String> arrayList6;
        AutofillValue autofillValue3;
        ArrayList<String> arrayList7;
        FillResponse lastResponseLocked = getLastResponseLocked("logContextCommited(%s)");
        if (lastResponseLocked == null) {
            return;
        }
        this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(PresentationStatsEventLogger.getNoPresentationEventReason(i2));
        this.mPresentationStatsEventLogger.logAndEndEvent();
        int flags = lastResponseLocked.getFlags();
        if ((flags & 1) == 0) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "logContextCommittedLocked(): ignored by flags " + flags);
                return;
            }
            return;
        }
        int size = this.mResponses.size();
        int i5 = 0;
        ArraySet<String> arraySet = null;
        boolean z3 = false;
        while (true) {
            str = "logContextCommitted() skipping idless dataset ";
            if (i5 >= size) {
                break;
            }
            List datasets = this.mResponses.valueAt(i5).getDatasets();
            if (datasets == null || datasets.isEmpty()) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "logContextCommitted() no datasets at " + i5);
                }
            } else {
                for (int i6 = 0; i6 < datasets.size(); i6++) {
                    Dataset dataset = (Dataset) datasets.get(i6);
                    String id = dataset.getId();
                    if (id == null) {
                        if (Helper.sVerbose) {
                            Slog.v(TAG, "logContextCommitted() skipping idless dataset " + dataset);
                        }
                    } else {
                        ArrayList<String> arrayList8 = this.mSelectedDatasetIds;
                        if (arrayList8 == null || !arrayList8.contains(id)) {
                            if (Helper.sVerbose) {
                                Slog.v(TAG, "adding ignored dataset " + id);
                            }
                            if (arraySet == null) {
                                arraySet = new ArraySet<>();
                            }
                            arraySet.add(id);
                        }
                        z3 = true;
                    }
                }
            }
            i5++;
        }
        ArraySet<String> arraySet2 = arraySet;
        int i7 = 0;
        ArrayMap arrayMap = null;
        ArrayList<AutofillId> arrayList9 = null;
        ArrayList<String> arrayList10 = null;
        while (i7 < this.mViewStates.size()) {
            ViewState valueAt = this.mViewStates.valueAt(i7);
            int state = valueAt.getState();
            if ((state & 8) != 0) {
                if ((state & 2048) != 0) {
                    String datasetId = valueAt.getDatasetId();
                    if (datasetId == null) {
                        Slog.w(TAG, "logContextCommitted(): no dataset id on " + valueAt);
                    } else {
                        AutofillValue autofilledValue = valueAt.getAutofilledValue();
                        AutofillValue currentValue = valueAt.getCurrentValue();
                        if (autofilledValue != null && autofilledValue.equals(currentValue)) {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "logContextCommitted(): ignoring changed " + valueAt + " because it has same value that was autofilled");
                            }
                        } else {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "logContextCommitted() found changed state: " + valueAt);
                            }
                            if (arrayList9 == null) {
                                ArrayList<AutofillId> arrayList11 = new ArrayList<>();
                                arrayList10 = new ArrayList<>();
                                arrayList9 = arrayList11;
                            }
                            arrayList9.add(valueAt.id);
                            arrayList10.add(datasetId);
                            i3 = size;
                            z = z3;
                            str2 = str;
                        }
                    }
                } else {
                    AutofillValue currentValue2 = valueAt.getCurrentValue();
                    if (currentValue2 == null) {
                        if (Helper.sDebug) {
                            Slog.d(TAG, "logContextCommitted(): skipping view without current value ( " + valueAt + ")");
                        }
                    } else if (z3) {
                        int i8 = 0;
                        while (i8 < size) {
                            List datasets2 = this.mResponses.valueAt(i8).getDatasets();
                            if (datasets2 == null || datasets2.isEmpty()) {
                                i4 = size;
                                autofillValue = currentValue2;
                                z2 = z3;
                                str3 = str;
                                arrayList5 = arrayList10;
                                if (Helper.sVerbose) {
                                    Slog.v(TAG, "logContextCommitted() no datasets at " + i8);
                                }
                            } else {
                                i4 = size;
                                ArrayMap arrayMap2 = arrayMap;
                                int i9 = 0;
                                while (i9 < datasets2.size()) {
                                    Dataset dataset2 = (Dataset) datasets2.get(i9);
                                    boolean z4 = z3;
                                    String id2 = dataset2.getId();
                                    if (id2 == null) {
                                        if (Helper.sVerbose) {
                                            Slog.v(TAG, str + dataset2);
                                        }
                                        autofillValue2 = currentValue2;
                                        str4 = str;
                                        list = datasets2;
                                        arrayList6 = arrayList10;
                                    } else {
                                        ArrayList fieldValues = dataset2.getFieldValues();
                                        str4 = str;
                                        list = datasets2;
                                        int i10 = 0;
                                        while (i10 < fieldValues.size()) {
                                            if (currentValue2.equals((AutofillValue) fieldValues.get(i10))) {
                                                if (Helper.sDebug) {
                                                    StringBuilder sb = new StringBuilder();
                                                    autofillValue3 = currentValue2;
                                                    sb.append("field ");
                                                    sb.append(valueAt.id);
                                                    sb.append(" was manually filled with value set by dataset ");
                                                    sb.append(id2);
                                                    Slog.d(TAG, sb.toString());
                                                } else {
                                                    autofillValue3 = currentValue2;
                                                }
                                                if (arrayMap2 == null) {
                                                    arrayMap2 = new ArrayMap();
                                                }
                                                ArrayMap arrayMap3 = arrayMap2;
                                                ArraySet arraySet3 = (ArraySet) arrayMap3.get(valueAt.id);
                                                if (arraySet3 == null) {
                                                    arrayList7 = arrayList10;
                                                    arraySet3 = new ArraySet(1);
                                                    arrayMap3.put(valueAt.id, arraySet3);
                                                } else {
                                                    arrayList7 = arrayList10;
                                                }
                                                arraySet3.add(id2);
                                                arrayMap2 = arrayMap3;
                                            } else {
                                                autofillValue3 = currentValue2;
                                                arrayList7 = arrayList10;
                                            }
                                            i10++;
                                            currentValue2 = autofillValue3;
                                            arrayList10 = arrayList7;
                                        }
                                        autofillValue2 = currentValue2;
                                        arrayList6 = arrayList10;
                                        ArrayList<String> arrayList12 = this.mSelectedDatasetIds;
                                        if (arrayList12 == null || !arrayList12.contains(id2)) {
                                            if (Helper.sVerbose) {
                                                Slog.v(TAG, "adding ignored dataset " + id2);
                                            }
                                            if (arraySet2 == null) {
                                                arraySet2 = new ArraySet<>();
                                            }
                                            arraySet2.add(id2);
                                        }
                                    }
                                    i9++;
                                    z3 = z4;
                                    str = str4;
                                    datasets2 = list;
                                    currentValue2 = autofillValue2;
                                    arrayList10 = arrayList6;
                                }
                                autofillValue = currentValue2;
                                z2 = z3;
                                str3 = str;
                                arrayList5 = arrayList10;
                                arrayMap = arrayMap2;
                            }
                            i8++;
                            size = i4;
                            z3 = z2;
                            str = str3;
                            currentValue2 = autofillValue;
                            arrayList10 = arrayList5;
                        }
                        i3 = size;
                        z = z3;
                        str2 = str;
                    }
                }
                i7++;
                size = i3;
                z3 = z;
                str = str2;
            }
            i3 = size;
            z = z3;
            str2 = str;
            arrayList10 = arrayList10;
            i7++;
            size = i3;
            z3 = z;
            str = str2;
        }
        ArrayList<String> arrayList13 = arrayList10;
        if (arrayMap != null) {
            int size2 = arrayMap.size();
            ArrayList<AutofillId> arrayList14 = new ArrayList<>(size2);
            ArrayList<ArrayList<String>> arrayList15 = new ArrayList<>(size2);
            for (int i11 = 0; i11 < size2; i11++) {
                AutofillId autofillId = (AutofillId) arrayMap.keyAt(i11);
                ArraySet arraySet4 = (ArraySet) arrayMap.valueAt(i11);
                arrayList14.add(autofillId);
                arrayList15.add(new ArrayList<>(arraySet4));
            }
            arrayList3 = arrayList14;
            arrayList4 = arrayList15;
        } else {
            arrayList3 = null;
            arrayList4 = null;
        }
        this.mService.logContextCommittedLocked(this.id, this.mClientState, this.mSelectedDatasetIds, arraySet2, arrayList9, arrayList13, arrayList3, arrayList4, arrayList, arrayList2, this.mComponentName, this.mCompatMode, i);
        logAllEvents(i2);
    }

    private void logFieldClassificationScore(FieldClassificationStrategy fieldClassificationStrategy, FieldClassificationUserData fieldClassificationUserData, int i, int i2) {
        Collection<ViewState> values;
        String[] values2 = fieldClassificationUserData.getValues();
        String[] categoryIds = fieldClassificationUserData.getCategoryIds();
        String fieldClassificationAlgorithm = fieldClassificationUserData.getFieldClassificationAlgorithm();
        Bundle defaultFieldClassificationArgs = fieldClassificationUserData.getDefaultFieldClassificationArgs();
        ArrayMap<String, String> fieldClassificationAlgorithms = fieldClassificationUserData.getFieldClassificationAlgorithms();
        ArrayMap<String, Bundle> fieldClassificationArgs = fieldClassificationUserData.getFieldClassificationArgs();
        if (values2 == null || categoryIds == null || values2.length != categoryIds.length) {
            Slog.w(TAG, "setScores(): user data mismatch: values.length = " + (values2 == null ? -1 : values2.length) + ", ids.length = " + (categoryIds != null ? categoryIds.length : -1));
            return;
        }
        int maxFieldClassificationIdsSize = UserData.getMaxFieldClassificationIdsSize();
        ArrayList arrayList = new ArrayList(maxFieldClassificationIdsSize);
        ArrayList arrayList2 = new ArrayList(maxFieldClassificationIdsSize);
        synchronized (this.mLock) {
            values = this.mViewStates.values();
        }
        int size = values.size();
        AutofillId[] autofillIdArr = new AutofillId[size];
        ArrayList arrayList3 = new ArrayList(size);
        int i3 = 0;
        for (ViewState viewState : values) {
            arrayList3.add(viewState.getCurrentValue());
            autofillIdArr[i3] = viewState.id;
            i3++;
        }
        fieldClassificationStrategy.calculateScores(new RemoteCallback(new LogFieldClassificationScoreOnResultListener(this, i, i2, size, autofillIdArr, values2, categoryIds, arrayList, arrayList2)), arrayList3, values2, categoryIds, fieldClassificationAlgorithm, defaultFieldClassificationArgs, fieldClassificationAlgorithms, fieldClassificationArgs);
    }

    public void handleLogFieldClassificationScore(Bundle bundle, int i, int i2, int i3, AutofillId[] autofillIdArr, String[] strArr, String[] strArr2, ArrayList<AutofillId> arrayList, ArrayList<FieldClassification> arrayList2) {
        String[] strArr3 = strArr;
        ArrayMap arrayMap = null;
        if (bundle == null) {
            if (Helper.sDebug) {
                Slog.d(TAG, "setFieldClassificationScore(): no results");
            }
            logContextCommitted(null, null, i, i2);
            return;
        }
        AutofillFieldClassificationService.Scores scores = (AutofillFieldClassificationService.Scores) bundle.getParcelable("scores", AutofillFieldClassificationService.Scores.class);
        if (scores == null) {
            Slog.w(TAG, "No field classification score on " + bundle);
            return;
        }
        int i4 = i3;
        int i5 = 0;
        int i6 = 0;
        while (i5 < i4) {
            try {
                AutofillId autofillId = autofillIdArr[i5];
                ArrayMap arrayMap2 = arrayMap;
                int i7 = 0;
                while (i7 < strArr3.length) {
                    try {
                        String str = strArr2[i7];
                        float f = scores.scores[i5][i7];
                        if (f > 0.0f) {
                            if (arrayMap2 == null) {
                                arrayMap2 = new ArrayMap(strArr3.length);
                            }
                            Float f2 = (Float) arrayMap2.get(str);
                            if (f2 != null && f2.floatValue() > f) {
                                if (Helper.sVerbose) {
                                    Slog.v(TAG, "skipping score " + f + " because it's less than " + f2);
                                }
                            } else {
                                if (Helper.sVerbose) {
                                    Slog.v(TAG, "adding score " + f + " at index " + i7 + " and id " + autofillId);
                                }
                                arrayMap2.put(str, Float.valueOf(f));
                            }
                        } else if (Helper.sVerbose) {
                            Slog.v(TAG, "skipping score 0 at index " + i7 + " and id " + autofillId);
                        }
                        i7++;
                        strArr3 = strArr;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e = e;
                        i6 = i7;
                    }
                }
                if (arrayMap2 == null) {
                    if (Helper.sVerbose) {
                        Slog.v(TAG, "no score for autofillId=" + autofillId);
                    }
                    i6 = i7;
                } else {
                    ArrayList arrayList3 = new ArrayList(arrayMap2.size());
                    int i8 = 0;
                    while (i8 < arrayMap2.size()) {
                        try {
                            arrayList3.add(new FieldClassification.Match((String) arrayMap2.keyAt(i8), ((Float) arrayMap2.valueAt(i8)).floatValue()));
                            i8++;
                        } catch (ArrayIndexOutOfBoundsException e2) {
                            e = e2;
                            i6 = i8;
                            wtf(e, "Error accessing FC score at [%d, %d] (%s): %s", Integer.valueOf(i5), Integer.valueOf(i6), scores, e);
                            return;
                        }
                    }
                    arrayList.add(autofillId);
                    arrayList2.add(new FieldClassification(arrayList3));
                    i6 = i8;
                }
                i5++;
                i4 = i3;
                strArr3 = strArr;
                arrayMap = null;
            } catch (ArrayIndexOutOfBoundsException e3) {
                e = e3;
            }
        }
        logContextCommitted(arrayList, arrayList2, i, i2);
    }

    public void logSaveUiShown() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Session) obj).logSaveShown();
            }
        }, this));
    }

    @GuardedBy({"mLock"})
    public SaveResult showSaveLocked() {
        boolean z;
        boolean z2;
        Drawable serviceIcon;
        CharSequence serviceLabel;
        boolean z3;
        int i;
        boolean z4;
        boolean z5 = false;
        if (this.mDestroyed) {
            Slog.w(TAG, "Call to Session#showSaveLocked() rejected - session: " + this.id + " destroyed");
            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(9);
            this.mSaveEventLogger.logAndEndEvent();
            return new SaveResult(false, false, 0);
        }
        this.mSessionState = 2;
        FillResponse lastResponseLocked = getLastResponseLocked("showSaveLocked(%s)");
        SaveInfo saveInfo = lastResponseLocked == null ? null : lastResponseLocked.getSaveInfo();
        if (this.mSessionExt.skipSaveUiAndNativeProcess()) {
            save();
            return new SaveResult(true, false, 0);
        }
        if (this.mSessionFlags.mScreenHasCredmanField) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "Call to Session#showSaveLocked() rejected - there is credman field in screen");
            }
            return new SaveResult(false, true, 0);
        }
        if (saveInfo == null) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "showSaveLocked(" + this.id + "): no saveInfo from service");
            }
            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(2);
            this.mSaveEventLogger.logAndEndEvent();
            return new SaveResult(false, true, 1);
        }
        if ((saveInfo.getFlags() & 4) != 0) {
            if (Helper.sDebug) {
                Slog.v(TAG, "showSaveLocked(" + this.id + "): service asked to delay save");
            }
            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(3);
            this.mSaveEventLogger.logAndEndEvent();
            return new SaveResult(false, false, 2);
        }
        ArrayMap<AutofillId, InternalSanitizer> createSanitizers = Helper.createSanitizers(saveInfo);
        ArrayMap arrayMap = new ArrayMap();
        ArraySet arraySet = new ArraySet();
        AutofillId[] requiredIds = saveInfo.getRequiredIds();
        if (requiredIds != null) {
            int i2 = 0;
            z = false;
            z2 = false;
            while (i2 < requiredIds.length) {
                AutofillId autofillId = requiredIds[i2];
                if (autofillId == null) {
                    Slog.w(TAG, "null autofill id on " + Arrays.toString(requiredIds));
                } else {
                    arraySet.add(autofillId);
                    ViewState viewState = this.mViewStates.get(autofillId);
                    if (viewState == null) {
                        Slog.w(TAG, "showSaveLocked(): no ViewState for required " + autofillId);
                        break;
                    }
                    AutofillValue currentValue = viewState.getCurrentValue();
                    if (currentValue == null || currentValue.isEmpty()) {
                        currentValue = getValueFromContextsLocked(autofillId);
                        if (currentValue != null) {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "Value of required field " + autofillId + " didn't change; using initial value (" + currentValue + ") instead");
                            }
                        } else {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "empty value for required " + autofillId);
                            }
                            z5 = false;
                        }
                    }
                    AutofillValue sanitizedValue = getSanitizedValue(createSanitizers, autofillId, currentValue);
                    if (sanitizedValue == null) {
                        if (Helper.sDebug) {
                            Slog.d(TAG, "value of required field " + autofillId + " failed sanitization");
                        }
                        z5 = false;
                    } else {
                        viewState.setSanitizedValue(sanitizedValue);
                        arrayMap.put(autofillId, sanitizedValue);
                        AutofillValue autofilledValue = viewState.getAutofilledValue();
                        if (!sanitizedValue.equals(autofilledValue)) {
                            if (autofilledValue == null) {
                                AutofillValue valueFromContextsLocked = getValueFromContextsLocked(autofillId);
                                if (valueFromContextsLocked != null && valueFromContextsLocked.equals(sanitizedValue)) {
                                    if (Helper.sDebug) {
                                        Slog.d(TAG, "id " + autofillId + " is part of dataset but initial value didn't change: " + sanitizedValue);
                                    }
                                    z4 = false;
                                } else {
                                    this.mSaveEventLogger.maybeSetIsNewField(true);
                                    z4 = true;
                                }
                            } else {
                                z4 = true;
                                z2 = true;
                            }
                            if (z4) {
                                if (Helper.sDebug) {
                                    Slog.d(TAG, "found a change on required " + autofillId + ": " + autofilledValue + " => " + sanitizedValue);
                                }
                                z = true;
                            }
                        }
                    }
                }
                i2++;
                z5 = false;
            }
        } else {
            z = false;
            z2 = false;
        }
        z5 = true;
        AutofillId[] optionalIds = saveInfo.getOptionalIds();
        if (Helper.sVerbose) {
            StringBuilder sb = new StringBuilder();
            sb.append("allRequiredAreNotEmpty: ");
            sb.append(z5);
            sb.append(" hasOptional: ");
            sb.append(optionalIds != null);
            Slog.v(TAG, sb.toString());
        }
        if (z5) {
            if (optionalIds != null && (!z || !z2)) {
                for (AutofillId autofillId2 : optionalIds) {
                    arraySet.add(autofillId2);
                    ViewState viewState2 = this.mViewStates.get(autofillId2);
                    if (viewState2 == null) {
                        Slog.w(TAG, "no ViewState for optional " + autofillId2);
                    } else if ((viewState2.getState() & 8) != 0) {
                        AutofillValue sanitizedValue2 = getSanitizedValue(createSanitizers, autofillId2, viewState2.getCurrentValue());
                        if (sanitizedValue2 == null) {
                            if (Helper.sDebug) {
                                Slog.d(TAG, "value of opt. field " + autofillId2 + " failed sanitization");
                            }
                        } else {
                            arrayMap.put(autofillId2, sanitizedValue2);
                            AutofillValue autofilledValue2 = viewState2.getAutofilledValue();
                            if (!sanitizedValue2.equals(autofilledValue2)) {
                                if (Helper.sDebug) {
                                    Slog.d(TAG, "found a change on optional " + autofillId2 + ": " + autofilledValue2 + " => " + sanitizedValue2);
                                }
                                if (autofilledValue2 != null) {
                                    z2 = true;
                                } else {
                                    this.mSaveEventLogger.maybeSetIsNewField(true);
                                }
                                z = true;
                            }
                        }
                    } else {
                        AutofillValue valueFromContextsLocked2 = getValueFromContextsLocked(autofillId2);
                        if (Helper.sDebug) {
                            Slog.d(TAG, "no current value for " + autofillId2 + "; initial value is " + valueFromContextsLocked2);
                        }
                        if (valueFromContextsLocked2 != null) {
                            arrayMap.put(autofillId2, valueFromContextsLocked2);
                        }
                    }
                }
            }
            if (!z) {
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(5);
                this.mSaveEventLogger.logAndEndEvent();
                i = 4;
            } else {
                if (Helper.sDebug) {
                    Slog.d(TAG, "at least one field changed, validate fields for save UI");
                }
                InternalValidator validator = saveInfo.getValidator();
                if (validator != null) {
                    LogMaker newLogMaker = newLogMaker(1133);
                    try {
                        boolean isValid = validator.isValid(this);
                        if (Helper.sDebug) {
                            Slog.d(TAG, validator + " returned " + isValid);
                        }
                        newLogMaker.setType(isValid ? 10 : 5);
                        this.mMetricsLogger.write(newLogMaker);
                        if (!isValid) {
                            Slog.i(TAG, "not showing save UI because fields failed validation");
                            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(6);
                            this.mSaveEventLogger.logAndEndEvent();
                            return new SaveResult(false, true, 5);
                        }
                    } catch (Exception e) {
                        Slog.e(TAG, "Not showing save UI because validation failed:", e);
                        newLogMaker.setType(11);
                        this.mMetricsLogger.write(newLogMaker);
                        this.mSaveEventLogger.maybeSetSaveUiNotShownReason(6);
                        this.mSaveEventLogger.logAndEndEvent();
                        return new SaveResult(false, true, 5);
                    }
                }
                List datasets = lastResponseLocked.getDatasets();
                if (datasets != null) {
                    for (int i3 = 0; i3 < datasets.size(); i3++) {
                        Dataset dataset = (Dataset) datasets.get(i3);
                        ArrayMap<AutofillId, AutofillValue> fields = Helper.getFields(dataset);
                        if (Helper.sVerbose) {
                            Slog.v(TAG, "Checking if saved fields match contents of dataset #" + i3 + ": " + dataset + "; savableIds=" + arraySet);
                        }
                        for (int i4 = 0; i4 < arraySet.size(); i4++) {
                            AutofillId autofillId3 = (AutofillId) arraySet.valueAt(i4);
                            AutofillValue autofillValue = (AutofillValue) arrayMap.get(autofillId3);
                            if (autofillValue == null) {
                                if (Helper.sDebug) {
                                    Slog.d(TAG, "dataset has value for field that is null: " + autofillId3);
                                }
                            } else {
                                AutofillValue autofillValue2 = fields.get(autofillId3);
                                if (!autofillValue.equals(autofillValue2)) {
                                    if (Helper.sDebug) {
                                        Slog.d(TAG, "found a dataset change on id " + autofillId3 + ": from " + autofillValue2 + " to " + autofillValue);
                                    }
                                } else if (Helper.sVerbose) {
                                    Slog.v(TAG, "no dataset changes for id " + autofillId3);
                                }
                            }
                        }
                        if (Helper.sDebug) {
                            Slog.d(TAG, "ignoring Save UI because all fields match contents of dataset #" + i3 + ": " + dataset);
                        }
                        this.mSaveEventLogger.maybeSetSaveUiNotShownReason(7);
                        this.mSaveEventLogger.logAndEndEvent();
                        return new SaveResult(false, true, 6);
                    }
                }
                if (Helper.sDebug) {
                    Slog.d(TAG, "Good news, everyone! All checks passed, show save UI for " + this.id + "!");
                }
                IAutoFillManagerClient client = getClient();
                this.mPendingSaveUi = new PendingUi(new Binder(), this.id, client);
                synchronized (this.mLock) {
                    serviceIcon = getServiceIcon(lastResponseLocked);
                    serviceLabel = getServiceLabel(lastResponseLocked);
                }
                if (serviceLabel == null || serviceIcon == null) {
                    wtf(null, "showSaveLocked(): no service label or icon", new Object[0]);
                    this.mSaveEventLogger.maybeSetSaveUiNotShownReason(1);
                    this.mSaveEventLogger.logAndEndEvent();
                    return new SaveResult(false, true, 0);
                }
                if (this.mSessionExt.skipSaveUi()) {
                    save();
                    return new SaveResult(true, false, 0);
                }
                long elapsedRealtime = SystemClock.elapsedRealtime();
                getUiForShowing().showSaveUi(serviceLabel, serviceIcon, this.mService.getServicePackageName(), saveInfo, this, this.mComponentName, this, this.mContext, this.mPendingSaveUi, z2, this.mCompatMode, lastResponseLocked.getShowSaveDialogIcon(), this.mSaveEventLogger);
                this.mSaveEventLogger.maybeSetLatencySaveUiDisplayMillis(SystemClock.elapsedRealtime() - elapsedRealtime);
                if (client != null) {
                    try {
                        z3 = true;
                        try {
                            client.setSaveUiState(this.id, true);
                        } catch (RemoteException e2) {
                            e = e2;
                            Slog.e(TAG, "Error notifying client to set save UI state to shown: " + e);
                            this.mSessionFlags.mShowingSaveUi = z3;
                            return new SaveResult(z3, false, 0);
                        }
                    } catch (RemoteException e3) {
                        e = e3;
                        z3 = true;
                    }
                } else {
                    z3 = true;
                }
                this.mSessionFlags.mShowingSaveUi = z3;
                return new SaveResult(z3, false, 0);
            }
        } else {
            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(4);
            this.mSaveEventLogger.logAndEndEvent();
            i = 3;
        }
        if (Helper.sDebug) {
            Slog.d(TAG, "showSaveLocked(" + this.id + "): with no changes, comes no responsibilities.allRequiredAreNotNull=" + z5 + ", atLeastOneChanged=" + z);
        }
        return new SaveResult(false, true, i);
    }

    public void logSaveShown() {
        this.mService.logSaveShown(this.id, this.mClientState);
    }

    private AutofillValue getSanitizedValue(ArrayMap<AutofillId, InternalSanitizer> arrayMap, AutofillId autofillId, AutofillValue autofillValue) {
        if (arrayMap == null || autofillValue == null) {
            return autofillValue;
        }
        ViewState viewState = this.mViewStates.get(autofillId);
        AutofillValue sanitizedValue = viewState == null ? null : viewState.getSanitizedValue();
        if (sanitizedValue == null) {
            InternalSanitizer internalSanitizer = arrayMap.get(autofillId);
            if (internalSanitizer == null) {
                return autofillValue;
            }
            sanitizedValue = internalSanitizer.sanitize(autofillValue);
            if (Helper.sDebug) {
                Slog.d(TAG, "Value for " + autofillId + "(" + autofillValue + ") sanitized to " + sanitizedValue);
            }
            if (viewState != null) {
                viewState.setSanitizedValue(sanitizedValue);
            }
        }
        return sanitizedValue;
    }

    @GuardedBy({"mLock"})
    public boolean isSaveUiShowingLocked() {
        return this.mSessionFlags.mShowingSaveUi;
    }

    @GuardedBy({"mLock"})
    private AutofillValue getValueFromContextsLocked(AutofillId autofillId) {
        for (int size = this.mContexts.size() - 1; size >= 0; size--) {
            AssistStructure.ViewNode findViewNodeByAutofillId = Helper.findViewNodeByAutofillId(this.mContexts.get(size).getStructure(), autofillId);
            if (findViewNodeByAutofillId != null) {
                AutofillValue autofillValue = findViewNodeByAutofillId.getAutofillValue();
                if (Helper.sDebug) {
                    Slog.d(TAG, "getValueFromContexts(" + this.id + "/" + autofillId + ") at " + size + ": " + autofillValue);
                }
                if (autofillValue != null && !autofillValue.isEmpty()) {
                    return autofillValue;
                }
            }
        }
        return null;
    }

    @GuardedBy({"mLock"})
    private CharSequence[] getAutofillOptionsFromContextsLocked(AutofillId autofillId) {
        for (int size = this.mContexts.size() - 1; size >= 0; size--) {
            AssistStructure.ViewNode findViewNodeByAutofillId = Helper.findViewNodeByAutofillId(this.mContexts.get(size).getStructure(), autofillId);
            if (findViewNodeByAutofillId != null && findViewNodeByAutofillId.getAutofillOptions() != null) {
                return findViewNodeByAutofillId.getAutofillOptions();
            }
        }
        return null;
    }

    private void updateValuesForSaveLocked() {
        ArrayMap<AutofillId, InternalSanitizer> createSanitizers = Helper.createSanitizers(getSaveInfoLocked());
        int size = this.mContexts.size();
        for (int i = 0; i < size; i++) {
            FillContext fillContext = this.mContexts.get(i);
            AssistStructure.ViewNode[] findViewNodesByAutofillIds = fillContext.findViewNodesByAutofillIds(getIdsOfAllViewStatesLocked());
            if (Helper.sVerbose) {
                Slog.v(TAG, "updateValuesForSaveLocked(): updating " + fillContext);
            }
            for (int i2 = 0; i2 < this.mViewStates.size(); i2++) {
                ViewState valueAt = this.mViewStates.valueAt(i2);
                AutofillId autofillId = valueAt.id;
                AutofillValue currentValue = valueAt.getCurrentValue();
                if (currentValue == null) {
                    if (Helper.sVerbose) {
                        Slog.v(TAG, "updateValuesForSaveLocked(): skipping " + autofillId);
                    }
                } else {
                    AssistStructure.ViewNode viewNode = findViewNodesByAutofillIds[i2];
                    if (viewNode == null) {
                        Slog.w(TAG, "callSaveLocked(): did not find node with id " + autofillId);
                    } else {
                        if (Helper.sVerbose) {
                            Slog.v(TAG, "updateValuesForSaveLocked(): updating " + autofillId + " to " + currentValue);
                        }
                        AutofillValue sanitizedValue = valueAt.getSanitizedValue();
                        if (sanitizedValue == null) {
                            sanitizedValue = getSanitizedValue(createSanitizers, autofillId, currentValue);
                        }
                        if (sanitizedValue != null) {
                            viewNode.updateAutofillValue(sanitizedValue);
                        } else if (Helper.sDebug) {
                            Slog.d(TAG, "updateValuesForSaveLocked(): not updating field " + autofillId + " because it failed sanitization");
                        }
                    }
                }
            }
            fillContext.getStructure().sanitizeForParceling(false);
            if (Helper.sVerbose) {
                Slog.v(TAG, "updateValuesForSaveLocked(): dumping structure of " + fillContext + " before calling service.save()");
                fillContext.getStructure().dump(false);
            }
        }
    }

    @GuardedBy({"mLock"})
    public void callSaveLocked() {
        if (this.mDestroyed) {
            Slog.w(TAG, "Call to Session#callSaveLocked() rejected - session: " + this.id + " destroyed");
            this.mSaveEventLogger.maybeSetIsSaved(false);
            this.mSaveEventLogger.logAndEndEvent();
            return;
        }
        if (this.mRemoteFillService == null) {
            wtf(null, "callSaveLocked() called without a remote service. mForAugmentedAutofillOnly: %s", Boolean.valueOf(this.mSessionFlags.mAugmentedAutofillOnly));
            this.mSaveEventLogger.maybeSetIsSaved(false);
            this.mSaveEventLogger.logAndEndEvent();
            return;
        }
        if (Helper.sVerbose) {
            Slog.v(TAG, "callSaveLocked(" + this.id + "): mViewStates=" + this.mViewStates);
        }
        if (this.mContexts == null) {
            Slog.w(TAG, "callSaveLocked(): no contexts");
            this.mSaveEventLogger.maybeSetIsSaved(false);
            this.mSaveEventLogger.logAndEndEvent();
            return;
        }
        updateValuesForSaveLocked();
        cancelCurrentRequestLocked();
        ArrayList<FillContext> mergePreviousSessionLocked = mergePreviousSessionLocked(true);
        this.mClientState = this.mSessionExt.hookOnSaveRequestClientState(this.mClientState);
        FieldClassificationResponse fieldClassificationResponse = this.mClassificationState.mLastFieldClassificationResponse;
        if (this.mService.isPccClassificationEnabled() && fieldClassificationResponse != null && !fieldClassificationResponse.getClassifications().isEmpty()) {
            if (this.mClientState == null) {
                this.mClientState = new Bundle();
            }
            this.mClientState.putParcelableArrayList(EXTRA_KEY_DETECTIONS, new ArrayList<>(fieldClassificationResponse.getClassifications()));
        }
        this.mRemoteFillService.onSaveRequest(new SaveRequest(mergePreviousSessionLocked, this.mClientState, this.mSelectedDatasetIds));
    }

    public ArrayList<FillContext> mergePreviousSessionLocked(boolean z) {
        ArrayList<Session> previousSessionsLocked = this.mService.getPreviousSessionsLocked(this);
        if (previousSessionsLocked != null) {
            if (Helper.sDebug) {
                Slog.d(TAG, "mergeSessions(" + this.id + "): Merging the content of " + previousSessionsLocked.size() + " sessions for task " + this.taskId);
            }
            ArrayList<FillContext> arrayList = new ArrayList<>();
            for (int i = 0; i < previousSessionsLocked.size(); i++) {
                Session session = previousSessionsLocked.get(i);
                ArrayList<FillContext> arrayList2 = session.mContexts;
                if (arrayList2 == null) {
                    Slog.w(TAG, "mergeSessions(" + this.id + "): Not merging null contexts from " + session.id);
                } else {
                    if (z) {
                        session.updateValuesForSaveLocked();
                    }
                    if (Helper.sDebug) {
                        Slog.d(TAG, "mergeSessions(" + this.id + "): adding " + arrayList2.size() + " context from previous session #" + session.id);
                    }
                    arrayList.addAll(arrayList2);
                    if (this.mClientState == null && session.mClientState != null) {
                        if (Helper.sDebug) {
                            Slog.d(TAG, "mergeSessions(" + this.id + "): setting client state from previous session" + session.id);
                        }
                        this.mClientState = session.mClientState;
                    }
                }
            }
            arrayList.addAll(this.mContexts);
            return arrayList;
        }
        return new ArrayList<>(this.mContexts);
    }

    @GuardedBy({"mLock"})
    private boolean requestNewFillResponseOnViewEnteredIfNecessaryLocked(AutofillId autofillId, ViewState viewState, int i) {
        if ((i & 1) != 0 || this.mSessionExt.hookShouldRequestNewFillResponse()) {
            this.mSessionExt.hookSetOnFillRequestReason(1);
            this.mSessionFlags.mAugmentedAutofillOnly = false;
            if (Helper.sDebug) {
                Slog.d(TAG, "Re-starting session on view " + autofillId + " and flags " + i);
            }
            requestNewFillResponseLocked(viewState, 256, i);
            return true;
        }
        if (shouldStartNewPartitionLocked(autofillId)) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Starting partition or augmented request for view id " + autofillId + ": " + viewState.getStateAsString());
            }
            this.mSessionFlags.mAugmentedAutofillOnly = false;
            requestNewFillResponseLocked(viewState, 32, i);
            return true;
        }
        if (Helper.sVerbose) {
            Slog.v(TAG, "Not starting new partition for view " + autofillId + ": " + viewState.getStateAsString());
        }
        return false;
    }

    @GuardedBy({"mLock"})
    private boolean shouldStartNewPartitionLocked(AutofillId autofillId) {
        ViewState viewState = this.mViewStates.get(autofillId);
        if (this.mResponses == null) {
            return viewState != null && (viewState.getState() & 65536) == 0;
        }
        if (this.mSessionFlags.mExpiredResponse) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Starting a new partition because the response has expired.");
            }
            return true;
        }
        int size = this.mResponses.size();
        if (size >= AutofillManagerService.getPartitionMaxCount()) {
            Slog.e(TAG, "Not starting a new partition on " + autofillId + " because session " + this.id + " reached maximum of " + AutofillManagerService.getPartitionMaxCount());
            return false;
        }
        for (int i = 0; i < size; i++) {
            FillResponse valueAt = this.mResponses.valueAt(i);
            if (ArrayUtils.contains(valueAt.getIgnoredIds(), autofillId)) {
                return false;
            }
            SaveInfo saveInfo = valueAt.getSaveInfo();
            if (saveInfo != null && (ArrayUtils.contains(saveInfo.getOptionalIds(), autofillId) || ArrayUtils.contains(saveInfo.getRequiredIds(), autofillId))) {
                return false;
            }
            List datasets = valueAt.getDatasets();
            if (datasets != null) {
                int size2 = datasets.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    ArrayList fieldIds = ((Dataset) datasets.get(i2)).getFieldIds();
                    if (fieldIds != null && fieldIds.contains(autofillId)) {
                        return false;
                    }
                }
            }
            if (ArrayUtils.contains(valueAt.getAuthenticationIds(), autofillId)) {
                return false;
            }
        }
        return true;
    }

    @GuardedBy({"mLock"})
    public void updateLocked(AutofillId autofillId, Rect rect, AutofillValue autofillValue, int i, int i2) {
        if (this.mDestroyed) {
            Slog.w(TAG, "Call to Session#updateLocked() rejected - session: " + autofillId + " destroyed");
            return;
        }
        if (i == 5) {
            this.mSessionFlags.mExpiredResponse = true;
            if (Helper.sDebug) {
                Slog.d(TAG, "Set the response has expired.");
            }
            this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReasonIfNoReasonExists(3);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            return;
        }
        autofillId.setSessionId(this.id);
        if (Helper.sVerbose) {
            Slog.v(TAG, "updateLocked(" + this.id + "): id=" + autofillId + ", action=" + actionAsString(i) + ", flags=" + i2);
        }
        ViewState viewState = this.mViewStates.get(autofillId);
        if (Helper.sVerbose) {
            Slog.v(TAG, "updateLocked(" + this.id + "): mCurrentViewId=" + this.mCurrentViewId + ", mExpiredResponse=" + this.mSessionFlags.mExpiredResponse + ", viewState=" + viewState);
        }
        if (viewState == null) {
            if (i == 1 || i == 4 || i == 2) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "Creating viewState for " + autofillId);
                }
                boolean isIgnoredLocked = isIgnoredLocked(autofillId);
                ViewState viewState2 = new ViewState(autofillId, this, isIgnoredLocked ? 128 : 1);
                this.mViewStates.put(autofillId, viewState2);
                if (isIgnoredLocked) {
                    if (Helper.sDebug) {
                        Slog.d(TAG, "updateLocked(): ignoring view " + viewState2);
                        return;
                    }
                    return;
                }
                viewState = viewState2;
            } else {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "Ignoring specific action when viewState=null");
                    return;
                }
                return;
            }
        }
        if ((i2 & 256) != 0) {
            if (Helper.sDebug) {
                Log.d(TAG, "force to reset fill dialog state");
            }
            this.mSessionFlags.mFillDialogDisabled = false;
        }
        if ((i2 & 512) != 0) {
            requestAssistStructureForPccLocked(i2);
            return;
        }
        if ((i2 & 1024) != 0) {
            this.mSessionFlags.mScreenHasCredmanField = true;
        }
        if (i == 1) {
            this.mCurrentViewId = viewState.id;
            viewState.update(autofillValue, rect, i2);
            startNewEventForPresentationStatsEventLogger();
            this.mPresentationStatsEventLogger.maybeSetIsNewRequest(true);
            if (!isRequestSupportFillDialog(i2)) {
                this.mSessionFlags.mFillDialogDisabled = true;
                this.mPreviouslyFillDialogPotentiallyStarted = false;
            } else {
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(8);
                this.mPreviouslyFillDialogPotentiallyStarted = true;
            }
            requestNewFillResponseLocked(viewState, 16, i2);
            return;
        }
        if (i != 2) {
            if (i == 3) {
                if (Objects.equals(this.mCurrentViewId, viewState.id)) {
                    if (Helper.sVerbose) {
                        Slog.v(TAG, "Exiting view " + autofillId);
                    }
                    this.mUi.hideFillUi(this);
                    this.mUi.hideFillDialog(this);
                    hideAugmentedAutofillLocked(viewState);
                    this.mInlineSessionController.resetInlineFillUiLocked();
                    this.mCurrentViewId = null;
                    this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(2);
                    this.mPresentationStatsEventLogger.logAndEndEvent();
                    return;
                }
                return;
            }
            if (i == 4) {
                if (this.mCompatMode && (viewState.getState() & 512) != 0) {
                    AssistStructure.ViewNode viewNode = this.mUrlBar;
                    String trim = viewNode == null ? null : viewNode.getText().toString().trim();
                    if (trim == null) {
                        wtf(null, "URL bar value changed, but current value is null", new Object[0]);
                        return;
                    }
                    if (autofillValue == null || !autofillValue.isText()) {
                        wtf(null, "URL bar value changed to null or non-text: %s", autofillValue);
                        return;
                    }
                    if (autofillValue.getTextValue().toString().equals(trim)) {
                        if (Helper.sDebug) {
                            Slog.d(TAG, "Ignoring change on URL bar as it's the same");
                            return;
                        }
                        return;
                    } else if (this.mSaveOnAllViewsInvisible) {
                        if (Helper.sDebug) {
                            Slog.d(TAG, "Ignoring change on URL because session will finish when views are gone");
                            return;
                        }
                        return;
                    } else {
                        if (Helper.sDebug) {
                            Slog.d(TAG, "Finishing session because URL bar changed");
                        }
                        forceRemoveFromServiceLocked(5);
                        return;
                    }
                }
                if (Objects.equals(autofillValue, viewState.getCurrentValue())) {
                    return;
                }
                logIfViewClearedLocked(autofillId, autofillValue, viewState);
                updateViewStateAndUiOnValueChangedLocked(autofillId, autofillValue, viewState, i2);
                return;
            }
            Slog.w(TAG, "updateLocked(): unknown action: " + i);
            return;
        }
        this.mLatencyBaseTime = SystemClock.elapsedRealtime();
        boolean z = this.mPreviouslyFillDialogPotentiallyStarted;
        this.mPreviouslyFillDialogPotentiallyStarted = false;
        if (Helper.sVerbose && rect != null) {
            Slog.v(TAG, "entered on virtual child " + autofillId + ": " + rect);
        }
        boolean equals = Objects.equals(this.mCurrentViewId, viewState.id);
        this.mCurrentViewId = viewState.id;
        if (autofillValue != null) {
            viewState.setCurrentValue(autofillValue);
        }
        if (this.mCompatMode && (viewState.getState() & 512) != 0) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Ignoring VIEW_ENTERED on URL BAR (id=" + autofillId + ")");
                return;
            }
            return;
        }
        synchronized (this.mLock) {
            if (!this.mLogViewEntered) {
                this.mService.logViewEntered(this.id, null);
            }
            this.mLogViewEntered = true;
        }
        if (!z) {
            this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(2);
            this.mPresentationStatsEventLogger.logAndEndEvent();
        }
        if ((i2 & 1) == 0) {
            ArrayList<AutofillId> arrayList = this.mAugmentedAutofillableIds;
            if (arrayList != null && arrayList.contains(autofillId)) {
                if (!equals) {
                    if (Helper.sDebug) {
                        Slog.d(TAG, "trigger augmented autofill.");
                    }
                    triggerAugmentedAutofillLocked(i2);
                    return;
                } else {
                    if (Helper.sDebug) {
                        Slog.d(TAG, "skip augmented autofill for same view: same view entered");
                        return;
                    }
                    return;
                }
            }
            if (this.mSessionFlags.mAugmentedAutofillOnly && equals) {
                if (Helper.sDebug) {
                    Slog.d(TAG, "skip augmented autofill for same view: standard autofill disabled.");
                    return;
                }
                return;
            }
        }
        if (!z) {
            startNewEventForPresentationStatsEventLogger();
        }
        if (requestNewFillResponseOnViewEnteredIfNecessaryLocked(autofillId, viewState, i2)) {
            if (z) {
                this.mPresentationStatsEventLogger.logAndEndEvent();
                startNewEventForPresentationStatsEventLogger();
                return;
            }
            return;
        }
        if (viewState.getResponse() != null) {
            FillResponse response = viewState.getResponse();
            this.mPresentationStatsEventLogger.maybeSetRequestId(response.getRequestId());
            this.mPresentationStatsEventLogger.maybeSetAvailableCount(response.getDatasets(), this.mCurrentViewId);
        }
        if (equals) {
            setFillDialogDisabledAndStartInput();
        } else {
            viewState.update(autofillValue, rect, i2);
        }
    }

    @GuardedBy({"mLock"})
    private void hideAugmentedAutofillLocked(ViewState viewState) {
        if ((viewState.getState() & 4096) != 0) {
            viewState.resetState(4096);
            cancelAugmentedAutofillLocked();
        }
    }

    @GuardedBy({"mLock"})
    private boolean isIgnoredLocked(AutofillId autofillId) {
        FillResponse lastResponseLocked = getLastResponseLocked(null);
        if (lastResponseLocked == null) {
            return false;
        }
        return ArrayUtils.contains(lastResponseLocked.getIgnoredIds(), autofillId);
    }

    @GuardedBy({"mLock"})
    private void logIfViewClearedLocked(AutofillId autofillId, AutofillValue autofillValue, ViewState viewState) {
        if ((autofillValue != null && !autofillValue.isEmpty()) || viewState.getCurrentValue() == null || !viewState.getCurrentValue().isText() || viewState.getCurrentValue().getTextValue() == null || getSaveInfoLocked() == null) {
            return;
        }
        int length = viewState.getCurrentValue().getTextValue().length();
        if (Helper.sDebug) {
            Slog.d(TAG, "updateLocked(" + autofillId + "): resetting value that was " + length + " chars long");
        }
        this.mMetricsLogger.write(newLogMaker(1124).addTaggedData(1125, Integer.valueOf(length)));
    }

    @GuardedBy({"mLock"})
    private void updateViewStateAndUiOnValueChangedLocked(AutofillId autofillId, AutofillValue autofillValue, ViewState viewState, int i) {
        CharSequence textValue;
        String str = null;
        if (autofillValue != null && autofillValue.isText() && (textValue = autofillValue.getTextValue()) != null) {
            str = textValue.toString();
        }
        updateFilteringStateOnValueChangedLocked(str, viewState);
        viewState.setCurrentValue(autofillValue);
        AutofillValue autofilledValue = viewState.getAutofilledValue();
        if (autofilledValue != null) {
            if (autofilledValue.equals(autofillValue)) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "ignoring autofilled change on id " + autofillId);
                }
                this.mInlineSessionController.hideInlineSuggestionsUiLocked(viewState.id);
                viewState.resetState(8);
                return;
            }
            if (viewState.id.equals(this.mCurrentViewId) && (viewState.getState() & 4) != 0) {
                if (Helper.sVerbose) {
                    Slog.v(TAG, "field changed after autofill on id " + autofillId);
                }
                viewState.resetState(4);
                this.mViewStates.get(this.mCurrentViewId).maybeCallOnFillReady(i);
            }
        }
        if (viewState.id.equals(this.mCurrentViewId) && (viewState.getState() & 8192) != 0) {
            if ((viewState.getState() & 32768) != 0) {
                this.mInlineSessionController.disableFilterMatching(viewState.id);
            }
            this.mInlineSessionController.filterInlineFillUiLocked(this.mCurrentViewId, str);
        } else if (viewState.id.equals(this.mCurrentViewId) && (viewState.getState() & 4096) != 0 && !TextUtils.isEmpty(str)) {
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(this.mCurrentViewId);
        }
        viewState.setState(8);
        getUiForShowing().filterFillUi(str, this);
    }

    @GuardedBy({"mLock"})
    private void updateFilteringStateOnValueChangedLocked(String str, ViewState viewState) {
        String str2 = "";
        if (str == null) {
            str = "";
        }
        AutofillValue currentValue = viewState.getCurrentValue();
        if (currentValue != null && currentValue.isText()) {
            str2 = currentValue.getTextValue().toString();
        }
        if ((viewState.getState() & 16384) == 0) {
            if (Helper.containsCharsInOrder(str, str2)) {
                return;
            }
            viewState.setState(16384);
        } else {
            if (Helper.containsCharsInOrder(str2, str)) {
                return;
            }
            viewState.setState(32768);
        }
    }

    @Override // com.android.server.autofill.ViewState.Listener
    public void onFillReady(FillResponse fillResponse, AutofillId autofillId, AutofillValue autofillValue, int i) {
        CharSequence serviceLabelLocked;
        Drawable serviceIconLocked;
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#onFillReady() rejected - session: " + this.id + " destroyed");
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(9);
                this.mSaveEventLogger.logAndEndEvent();
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(6);
                this.mPresentationStatsEventLogger.logAndEndEvent();
                return;
            }
            String charSequence = (autofillValue == null || !autofillValue.isText()) ? null : autofillValue.getTextValue().toString();
            synchronized (((AbstractPerUserSystemService) this.mService).mLock) {
                serviceLabelLocked = this.mService.getServiceLabelLocked();
                serviceIconLocked = this.mService.getServiceIconLocked();
            }
            if (serviceLabelLocked == null || serviceIconLocked == null) {
                wtf(null, "onFillReady(): no service label or icon", new Object[0]);
                return;
            }
            synchronized (this.mLock) {
                this.mPresentationStatsEventLogger.maybeSetSuggestionSentTimestampMs((int) (SystemClock.elapsedRealtime() - this.mLatencyBaseTime));
            }
            AutofillId[] fillDialogTriggerIds = fillResponse.getFillDialogTriggerIds();
            if (fillDialogTriggerIds != null && ArrayUtils.contains(fillDialogTriggerIds, autofillId)) {
                if (requestShowFillDialog(fillResponse, autofillId, charSequence, i)) {
                    synchronized (this.mLock) {
                        this.mViewStates.get(this.mCurrentViewId).setState(131072);
                        this.mPresentationStatsEventLogger.maybeSetCountShown(fillResponse.getDatasets(), this.mCurrentViewId);
                        this.mPresentationStatsEventLogger.maybeSetDisplayPresentationType(3);
                    }
                    setFillDialogDisabled();
                    synchronized (this.mLock) {
                        this.mPresentationStatsEventLogger.maybeSetSuggestionPresentedTimestampMs((int) (SystemClock.elapsedRealtime() - this.mLatencyBaseTime));
                    }
                    return;
                }
                setFillDialogDisabled();
            }
            if (fillResponse.supportsInlineSuggestions()) {
                synchronized (this.mLock) {
                    if (requestShowInlineSuggestionsLocked(fillResponse, charSequence)) {
                        this.mViewStates.get(this.mCurrentViewId).setState(8192);
                        this.mPresentationStatsEventLogger.maybeSetCountShown(fillResponse.getDatasets(), this.mCurrentViewId);
                        this.mPresentationStatsEventLogger.maybeSetInlinePresentationAndSuggestionHostUid(this.mContext, this.userId);
                        return;
                    }
                }
            }
            getUiForShowing().showFillUi(autofillId, fillResponse, charSequence, this.mService.getServicePackageName(), this.mComponentName, serviceLabelLocked, serviceIconLocked, this, this.mContext, this.id, this.mCompatMode);
            synchronized (this.mLock) {
                this.mPresentationStatsEventLogger.maybeSetCountShown(fillResponse.getDatasets(), this.mCurrentViewId);
                this.mPresentationStatsEventLogger.maybeSetDisplayPresentationType(1);
            }
            synchronized (this.mLock) {
                if (this.mUiShownTime == 0) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    this.mUiShownTime = elapsedRealtime;
                    long j = elapsedRealtime - this.mStartTime;
                    this.mPresentationStatsEventLogger.maybeSetSuggestionPresentedTimestampMs((int) (elapsedRealtime - this.mLatencyBaseTime));
                    if (Helper.sDebug) {
                        StringBuilder sb = new StringBuilder("1st UI for ");
                        sb.append(this.mActivityToken);
                        sb.append(" shown in ");
                        TimeUtils.formatDuration(j, sb);
                        Slog.d(TAG, sb.toString());
                    }
                    StringBuilder sb2 = new StringBuilder("id=");
                    sb2.append(this.id);
                    sb2.append(" app=");
                    sb2.append(this.mActivityToken);
                    sb2.append(" svc=");
                    sb2.append(this.mService.getServicePackageName());
                    sb2.append(" latency=");
                    TimeUtils.formatDuration(j, sb2);
                    this.mUiLatencyHistory.log(sb2.toString());
                    addTaggedDataToRequestLogLocked(fillResponse.getRequestId(), 1145, Long.valueOf(j));
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void updateFillDialogTriggerIdsLocked() {
        FillResponse lastResponseLocked = getLastResponseLocked(null);
        if (lastResponseLocked == null) {
            return;
        }
        AutofillId[] fillDialogTriggerIds = lastResponseLocked.getFillDialogTriggerIds();
        notifyClientFillDialogTriggerIds(fillDialogTriggerIds != null ? Arrays.asList(fillDialogTriggerIds) : null);
    }

    private void notifyClientFillDialogTriggerIds(List<AutofillId> list) {
        try {
            if (Helper.sVerbose) {
                Slog.v(TAG, "notifyFillDialogTriggerIds(): " + list);
            }
            getClient().notifyFillDialogTriggerIds(list);
        } catch (RemoteException e) {
            Slog.w(TAG, "Cannot set trigger ids for fill dialog", e);
        }
    }

    private boolean isFillDialogUiEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mSessionFlags.mFillDialogDisabled || this.mSessionFlags.mScreenHasCredmanField) ? false : true;
        }
        return z;
    }

    private void setFillDialogDisabled() {
        synchronized (this.mLock) {
            this.mSessionFlags.mFillDialogDisabled = true;
        }
        notifyClientFillDialogTriggerIds(null);
    }

    private void setFillDialogDisabledAndStartInput() {
        AutofillId autofillId;
        if (getUiForShowing().isFillDialogShowing()) {
            setFillDialogDisabled();
            synchronized (this.mLock) {
                autofillId = this.mCurrentViewId;
            }
            requestShowSoftInput(autofillId);
        }
    }

    private boolean requestShowFillDialog(FillResponse fillResponse, AutofillId autofillId, String str, int i) {
        Drawable serviceIcon;
        if (!isFillDialogUiEnabled()) {
            if (Helper.sDebug) {
                Log.w(TAG, "requestShowFillDialog: fill dialog is disabled");
            }
            return false;
        }
        if ((i & 128) != 0) {
            if (Helper.sDebug) {
                Log.w(TAG, "requestShowFillDialog: IME is showing");
            }
            return false;
        }
        if (this.mInlineSessionController.isImeShowing()) {
            return false;
        }
        synchronized (this.mLock) {
            AutofillId[] autofillIdArr = this.mLastFillDialogTriggerIds;
            if (autofillIdArr != null && ArrayUtils.contains(autofillIdArr, autofillId)) {
                synchronized (this.mLock) {
                    serviceIcon = getServiceIcon(fillResponse);
                }
                getUiForShowing().showFillDialog(autofillId, fillResponse, str, this.mService.getServicePackageName(), this.mComponentName, serviceIcon, this, this.id, this.mCompatMode, this.mPresentationStatsEventLogger);
                return true;
            }
            if (Helper.sDebug) {
                Log.w(TAG, "Last fill dialog triggered ids are changed.");
            }
            return false;
        }
    }

    @GuardedBy({"mLock"})
    private Drawable getServiceIcon(FillResponse fillResponse) {
        int iconResourceId = fillResponse.getIconResourceId();
        Drawable drawable = iconResourceId != 0 ? ((AutofillManagerService) this.mService.getMaster()).getContext().getPackageManager().getDrawable(this.mService.getServicePackageName(), iconResourceId, null) : null;
        return drawable == null ? this.mService.getServiceIconLocked() : drawable;
    }

    @GuardedBy({"mLock"})
    private CharSequence getServiceLabel(FillResponse fillResponse) {
        int serviceDisplayNameResourceId = fillResponse.getServiceDisplayNameResourceId();
        CharSequence text = serviceDisplayNameResourceId != 0 ? ((AutofillManagerService) this.mService.getMaster()).getContext().getPackageManager().getText(this.mService.getServicePackageName(), serviceDisplayNameResourceId, null) : null;
        return text == null ? this.mService.getServiceLabelLocked() : text;
    }

    private boolean requestShowInlineSuggestionsLocked(FillResponse fillResponse, String str) {
        AutofillId autofillId = this.mCurrentViewId;
        if (autofillId == null) {
            Log.w(TAG, "requestShowInlineSuggestionsLocked(): no view currently focused");
            return false;
        }
        Optional<InlineSuggestionsRequest> inlineSuggestionsRequestLocked = this.mInlineSessionController.getInlineSuggestionsRequestLocked();
        if (!inlineSuggestionsRequestLocked.isPresent()) {
            Log.w(TAG, "InlineSuggestionsRequest unavailable");
            return false;
        }
        RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderServiceLocked = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (remoteInlineSuggestionRenderServiceLocked == null) {
            Log.w(TAG, "RemoteInlineSuggestionRenderService not found");
            return false;
        }
        synchronized (this.mLock) {
            this.mLoggedInlineDatasetShown = false;
        }
        return this.mInlineSessionController.setInlineFillUiLocked(InlineFillUi.forAutofill(new InlineFillUi.InlineFillUiInfo(inlineSuggestionsRequestLocked.get(), autofillId, str, remoteInlineSuggestionRenderServiceLocked, this.userId, this.id), fillResponse, new InlineFillUi.InlineSuggestionUiCallback() { // from class: com.android.server.autofill.Session.3
            final /* synthetic */ AutofillId val$focusedId;
            final /* synthetic */ FillResponse val$response;

            AnonymousClass3(FillResponse fillResponse2, AutofillId autofillId2) {
                r2 = fillResponse2;
                r3 = autofillId2;
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void autofill(Dataset dataset, int i) {
                Session.this.fill(r2.getRequestId(), i, dataset, 2);
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void authenticate(int i, int i2) {
                Session.this.authenticate(r2.getRequestId(), i2, r2.getAuthentication(), r2.getClientState(), 2);
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void startIntentSender(IntentSender intentSender) {
                Session.this.startIntentSender(intentSender, new Intent());
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onError() {
                synchronized (Session.this.mLock) {
                    Session.this.mInlineSessionController.setInlineFillUiLocked(InlineFillUi.emptyUi(r3));
                }
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onInflate() {
                Session.this.onShown(2);
            }
        }));
    }

    /* renamed from: com.android.server.autofill.Session$3 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AnonymousClass3 implements InlineFillUi.InlineSuggestionUiCallback {
        final /* synthetic */ AutofillId val$focusedId;
        final /* synthetic */ FillResponse val$response;

        AnonymousClass3(FillResponse fillResponse2, AutofillId autofillId2) {
            r2 = fillResponse2;
            r3 = autofillId2;
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
        public void autofill(Dataset dataset, int i) {
            Session.this.fill(r2.getRequestId(), i, dataset, 2);
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
        public void authenticate(int i, int i2) {
            Session.this.authenticate(r2.getRequestId(), i2, r2.getAuthentication(), r2.getClientState(), 2);
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
        public void startIntentSender(IntentSender intentSender) {
            Session.this.startIntentSender(intentSender, new Intent());
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
        public void onError() {
            synchronized (Session.this.mLock) {
                Session.this.mInlineSessionController.setInlineFillUiLocked(InlineFillUi.emptyUi(r3));
            }
        }

        @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
        public void onInflate() {
            Session.this.onShown(2);
        }
    }

    public boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    public IAutoFillManagerClient getClient() {
        IAutoFillManagerClient iAutoFillManagerClient;
        synchronized (this.mLock) {
            iAutoFillManagerClient = this.mClient;
        }
        return iAutoFillManagerClient;
    }

    private void notifyUnavailableToClient(int i, ArrayList<AutofillId> arrayList) {
        synchronized (this.mLock) {
            AutofillId autofillId = this.mCurrentViewId;
            if (autofillId == null) {
                return;
            }
            try {
                if (this.mHasCallback) {
                    this.mClient.notifyNoFillUi(this.id, autofillId, i);
                } else if (i != 0) {
                    this.mClient.setSessionFinished(i, arrayList);
                }
            } catch (RemoteException e) {
                Slog.e(TAG, "Error notifying client no fill UI: id=" + this.mCurrentViewId, e);
            }
        }
    }

    private void notifyDisableAutofillToClient(long j, ComponentName componentName) {
        synchronized (this.mLock) {
            if (this.mCurrentViewId == null) {
                return;
            }
            try {
                this.mClient.notifyDisableAutofill(j, componentName);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error notifying client disable autofill: id=" + this.mCurrentViewId, e);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void updateTrackedIdsLocked() {
        ArraySet arraySet;
        AutofillId autofillId;
        int i;
        boolean z;
        ArraySet arraySet2 = null;
        FillResponse lastResponseLocked = getLastResponseLocked(null);
        if (lastResponseLocked == null) {
            return;
        }
        this.mSaveOnAllViewsInvisible = false;
        SaveInfo saveInfo = lastResponseLocked.getSaveInfo();
        if (saveInfo != null) {
            AutofillId triggerId = saveInfo.getTriggerId();
            if (triggerId != null) {
                writeLog(1228);
                this.mSaveEventLogger.maybeSetSaveUiShownReason(3);
            }
            i = saveInfo.getFlags();
            this.mSaveOnAllViewsInvisible = (i & 1) != 0;
            this.mFillResponseEventLogger.maybeSetSaveUiTriggerIds(1);
            this.mSaveEventLogger.maybeSetRequestId(lastResponseLocked.getRequestId());
            this.mSaveEventLogger.maybeSetAppPackageUid(this.uid);
            this.mSaveEventLogger.maybeSetSaveUiTriggerIds(1);
            this.mSaveEventLogger.maybeSetFlag(i);
            if (this.mSaveOnAllViewsInvisible) {
                arraySet = new ArraySet();
                if (saveInfo.getRequiredIds() != null) {
                    Collections.addAll(arraySet, saveInfo.getRequiredIds());
                    this.mSaveEventLogger.maybeSetSaveUiShownReason(1);
                }
                if (saveInfo.getOptionalIds() != null) {
                    Collections.addAll(arraySet, saveInfo.getOptionalIds());
                    this.mSaveEventLogger.maybeSetSaveUiShownReason(2);
                }
            } else {
                arraySet = null;
            }
            if ((i & 2) != 0) {
                this.mSaveEventLogger.maybeSetSaveUiShownReason(0);
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(8);
                z = false;
            } else {
                z = true;
            }
            autofillId = triggerId;
        } else {
            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(2);
            arraySet = null;
            autofillId = null;
            i = 0;
            z = true;
        }
        List datasets = lastResponseLocked.getDatasets();
        if (datasets != null) {
            for (int i2 = 0; i2 < datasets.size(); i2++) {
                ArrayList fieldIds = ((Dataset) datasets.get(i2)).getFieldIds();
                if (fieldIds != null) {
                    for (int i3 = 0; i3 < fieldIds.size(); i3++) {
                        AutofillId autofillId2 = (AutofillId) fieldIds.get(i3);
                        if (autofillId2 != null && (arraySet == null || !arraySet.contains(autofillId2))) {
                            arraySet2 = ArrayUtils.add(arraySet2, autofillId2);
                        }
                    }
                }
            }
        }
        try {
            if (Helper.sVerbose) {
                StringBuilder sb = new StringBuilder();
                sb.append("updateTrackedIdsLocked(): ");
                sb.append(arraySet);
                sb.append(" => ");
                sb.append(arraySet2);
                sb.append(" triggerId: ");
                sb.append(autofillId);
                sb.append(" saveOnFinish:");
                sb.append(z);
                sb.append(" flags: ");
                sb.append(i);
                sb.append(" hasSaveInfo: ");
                sb.append(saveInfo != null);
                Slog.v(TAG, sb.toString());
            }
            this.mClient.setTrackedViews(this.id, Helper.toArray(arraySet), this.mSaveOnAllViewsInvisible, z, Helper.toArray(arraySet2), autofillId);
        } catch (RemoteException e) {
            Slog.w(TAG, "Cannot set tracked ids", e);
        }
    }

    @GuardedBy({"mLock"})
    public void setAutofillFailureLocked(List<AutofillId> list) {
        for (int i = 0; i < list.size(); i++) {
            AutofillId autofillId = list.get(i);
            ViewState viewState = this.mViewStates.get(autofillId);
            if (viewState == null) {
                Slog.w(TAG, "setAutofillFailure(): no view for id " + autofillId);
            } else {
                viewState.resetState(4);
                viewState.setState(viewState.getState() | 1024);
                if (Helper.sVerbose) {
                    Slog.v(TAG, "Changed state of " + autofillId + " to " + viewState.getStateAsString());
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    private void replaceResponseLocked(FillResponse fillResponse, FillResponse fillResponse2, Bundle bundle) {
        setViewStatesLocked(fillResponse, 1, true);
        fillResponse2.setRequestId(fillResponse.getRequestId());
        processResponseLockedForPcc(fillResponse2, bundle, 0);
    }

    @GuardedBy({"mLock"})
    private void processNullResponseLocked(int i, int i2) {
        ArrayList<AutofillId> arrayList;
        unregisterDelayedFillBroadcastLocked();
        if ((i2 & 1) != 0) {
            getUiForShowing().showError(R.string.autofill_save_type_email_address, this);
        }
        FillContext fillContextByRequestIdLocked = getFillContextByRequestIdLocked(i);
        if (fillContextByRequestIdLocked != null) {
            arrayList = Helper.getAutofillIds(fillContextByRequestIdLocked.getStructure(), true);
        } else {
            Slog.w(TAG, "processNullResponseLocked(): no context for req " + i);
            arrayList = null;
        }
        this.mFillResponseEventLogger.maybeSetAvailableCount(0);
        this.mFillResponseEventLogger.logAndEndEvent();
        this.mService.resetLastResponse();
        Runnable triggerAugmentedAutofillLocked = triggerAugmentedAutofillLocked(i2);
        this.mAugmentedAutofillDestroyer = triggerAugmentedAutofillLocked;
        if (triggerAugmentedAutofillLocked == null && (i2 & 4) == 0) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "canceling session " + this.id + " when service returned null and it cannot be augmented. AutofillableIds: " + arrayList);
            }
            notifyUnavailableToClient(2, arrayList);
            removeFromService();
            return;
        }
        if ((i2 & 4) != 0) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "keeping session " + this.id + " when service returned null and augmented service is disabled for password fields. AutofillableIds: " + arrayList);
            }
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(this.mCurrentViewId);
        } else if (Helper.sVerbose) {
            Slog.v(TAG, "keeping session " + this.id + " when service returned null but it can be augmented. AutofillableIds: " + arrayList);
        }
        this.mAugmentedAutofillableIds = arrayList;
        try {
            this.mClient.setState(32);
        } catch (RemoteException e) {
            Slog.e(TAG, "Error setting client to autofill-only", e);
        }
    }

    @GuardedBy({"mLock"})
    private Runnable triggerAugmentedAutofillLocked(int i) {
        if ((i & 4) != 0) {
            return null;
        }
        int supportedSmartSuggestionModesLocked = this.mService.getSupportedSmartSuggestionModesLocked();
        if (supportedSmartSuggestionModesLocked == 0) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "triggerAugmentedAutofillLocked(): no supported modes");
            }
            return null;
        }
        final RemoteAugmentedAutofillService remoteAugmentedAutofillServiceLocked = this.mService.getRemoteAugmentedAutofillServiceLocked();
        if (remoteAugmentedAutofillServiceLocked == null) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "triggerAugmentedAutofillLocked(): no service for user");
            }
            return null;
        }
        if ((supportedSmartSuggestionModesLocked & 1) == 0) {
            Slog.w(TAG, "Unsupported Smart Suggestion mode: " + supportedSmartSuggestionModesLocked);
            return null;
        }
        if (this.mCurrentViewId == null) {
            Slog.w(TAG, "triggerAugmentedAutofillLocked(): no view currently focused");
            return null;
        }
        boolean isWhitelistedForAugmentedAutofillLocked = this.mService.isWhitelistedForAugmentedAutofillLocked(this.mComponentName);
        if (!isWhitelistedForAugmentedAutofillLocked) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "triggerAugmentedAutofillLocked(): " + ComponentName.flattenToShortString(this.mComponentName) + " not whitelisted ");
            }
            logAugmentedAutofillRequestLocked(1, remoteAugmentedAutofillServiceLocked.getComponentName(), this.mCurrentViewId, isWhitelistedForAugmentedAutofillLocked, null);
            return null;
        }
        if (Helper.sVerbose) {
            Slog.v(TAG, "calling Augmented Autofill Service (" + ComponentName.flattenToShortString(remoteAugmentedAutofillServiceLocked.getComponentName()) + ") on view " + this.mCurrentViewId + " using suggestion mode " + AutofillManager.getSmartSuggestionModeToString(1) + " when server returned null for session " + this.id);
        }
        this.mFillRequestEventLogger.startLogForNewRequest();
        this.mRequestCount++;
        this.mFillRequestEventLogger.maybeSetAppPackageUid(this.uid);
        this.mFillRequestEventLogger.maybeSetFlags(this.mFlags);
        this.mFillRequestEventLogger.maybeSetRequestId(1);
        this.mFillRequestEventLogger.maybeSetIsAugmented(true);
        this.mFillRequestEventLogger.logAndEndEvent();
        ViewState viewState = this.mViewStates.get(this.mCurrentViewId);
        viewState.setState(4096);
        AutofillValue currentValue = viewState.getCurrentValue();
        if (this.mAugmentedRequestsLogs == null) {
            this.mAugmentedRequestsLogs = new ArrayList<>();
        }
        this.mAugmentedRequestsLogs.add(newLogMaker(1630, remoteAugmentedAutofillServiceLocked.getComponentName().getPackageName()));
        AutofillId autofillId = this.mCurrentViewId;
        AugmentedAutofillInlineSuggestionRequestConsumer augmentedAutofillInlineSuggestionRequestConsumer = new AugmentedAutofillInlineSuggestionRequestConsumer(this, autofillId, isWhitelistedForAugmentedAutofillLocked, 1, currentValue);
        RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderServiceLocked = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (remoteInlineSuggestionRenderServiceLocked != null && ((this.mSessionFlags.mAugmentedAutofillOnly || !this.mSessionFlags.mInlineSupportedByService || this.mSessionFlags.mExpiredResponse) && (isViewFocusedLocked(i) || isRequestSupportFillDialog(i)))) {
            if (Helper.sDebug) {
                Slog.d(TAG, "Create inline request for augmented autofill");
            }
            remoteInlineSuggestionRenderServiceLocked.getInlineSuggestionsRendererInfo(new RemoteCallback(new AugmentedAutofillInlineSuggestionRendererOnResultListener(this, autofillId, augmentedAutofillInlineSuggestionRequestConsumer), this.mHandler));
        } else {
            augmentedAutofillInlineSuggestionRequestConsumer.accept((AugmentedAutofillInlineSuggestionRequestConsumer) this.mInlineSessionController.getInlineSuggestionsRequestLocked().orElse(null));
        }
        if (this.mAugmentedAutofillDestroyer == null) {
            this.mAugmentedAutofillDestroyer = new Runnable() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteAugmentedAutofillService.this.onDestroyAutofillWindowsRequest();
                }
            };
        }
        return this.mAugmentedAutofillDestroyer;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AugmentedAutofillInlineSuggestionRendererOnResultListener implements RemoteCallback.OnResultListener {
        final AutofillId mFocusedId;
        Consumer<InlineSuggestionsRequest> mRequestAugmentedAutofill;
        WeakReference<Session> mSessionWeakRef;

        AugmentedAutofillInlineSuggestionRendererOnResultListener(Session session, AutofillId autofillId, Consumer<InlineSuggestionsRequest> consumer) {
            this.mSessionWeakRef = new WeakReference<>(session);
            this.mFocusedId = autofillId;
            this.mRequestAugmentedAutofill = consumer;
        }

        public void onResult(Bundle bundle) {
            Session session = this.mSessionWeakRef.get();
            if (Session.logIfSessionNull(session, "AugmentedAutofillInlineSuggestionRendererOnResultListener:")) {
                return;
            }
            synchronized (session.mLock) {
                session.mInlineSessionController.onCreateInlineSuggestionsRequestLocked(this.mFocusedId, this.mRequestAugmentedAutofill, bundle);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AugmentedAutofillInlineSuggestionRequestConsumer implements Consumer<InlineSuggestionsRequest> {
        final AutofillValue mCurrentValue;
        final AutofillId mFocusedId;
        final boolean mIsAllowlisted;
        final int mMode;
        WeakReference<Session> mSessionWeakRef;

        AugmentedAutofillInlineSuggestionRequestConsumer(Session session, AutofillId autofillId, boolean z, int i, AutofillValue autofillValue) {
            this.mSessionWeakRef = new WeakReference<>(session);
            this.mFocusedId = autofillId;
            this.mIsAllowlisted = z;
            this.mMode = i;
            this.mCurrentValue = autofillValue;
        }

        @Override // java.util.function.Consumer
        public void accept(InlineSuggestionsRequest inlineSuggestionsRequest) {
            Session session = this.mSessionWeakRef.get();
            if (Session.logIfSessionNull(session, "AugmentedAutofillInlineSuggestionRequestConsumer:")) {
                return;
            }
            session.onAugmentedAutofillInlineSuggestionAccept(inlineSuggestionsRequest, this.mFocusedId, this.mIsAllowlisted, this.mMode, this.mCurrentValue);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AugmentedAutofillInlineSuggestionsResponseCallback implements Function<InlineFillUi, Boolean> {
        WeakReference<Session> mSessionWeakRef;

        AugmentedAutofillInlineSuggestionsResponseCallback(Session session) {
            this.mSessionWeakRef = new WeakReference<>(session);
        }

        @Override // java.util.function.Function
        public Boolean apply(InlineFillUi inlineFillUi) {
            Boolean valueOf;
            Session session = this.mSessionWeakRef.get();
            if (Session.logIfSessionNull(session, "AugmentedAutofillInlineSuggestionsResponseCallback:")) {
                return Boolean.FALSE;
            }
            synchronized (session.mLock) {
                valueOf = Boolean.valueOf(session.mInlineSessionController.setInlineFillUiLocked(inlineFillUi));
            }
            return valueOf;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AugmentedAutofillErrorCallback implements Runnable {
        WeakReference<Session> mSessionWeakRef;

        AugmentedAutofillErrorCallback(Session session) {
            this.mSessionWeakRef = new WeakReference<>(session);
        }

        @Override // java.lang.Runnable
        public void run() {
            Session session = this.mSessionWeakRef.get();
            if (Session.logIfSessionNull(session, "AugmentedAutofillErrorCallback:")) {
                return;
            }
            session.onAugmentedAutofillErrorCallback();
        }
    }

    public static boolean logIfSessionNull(Session session, String str) {
        if (session == null) {
            Slog.wtf(TAG, str + " Session null");
            return true;
        }
        if (!session.mDestroyed) {
            return false;
        }
        Slog.w(TAG, str + " Session destroyed, but following through");
        return false;
    }

    public void onAugmentedAutofillInlineSuggestionAccept(InlineSuggestionsRequest inlineSuggestionsRequest, AutofillId autofillId, boolean z, int i, AutofillValue autofillValue) {
        synchronized (this.mLock) {
            RemoteAugmentedAutofillService remoteAugmentedAutofillServiceLocked = this.mService.getRemoteAugmentedAutofillServiceLocked();
            logAugmentedAutofillRequestLocked(i, remoteAugmentedAutofillServiceLocked.getComponentName(), autofillId, z, Boolean.valueOf(inlineSuggestionsRequest != null));
            remoteAugmentedAutofillServiceLocked.onRequestAutofillLocked(this.id, this.mClient, this.taskId, this.mComponentName, this.mActivityToken, AutofillId.withoutSession(autofillId), autofillValue, inlineSuggestionsRequest, new AugmentedAutofillInlineSuggestionsResponseCallback(this), new AugmentedAutofillErrorCallback(this), this.mService.getRemoteInlineSuggestionRenderServiceLocked(), this.userId);
        }
    }

    public void onAugmentedAutofillErrorCallback() {
        synchronized (this.mLock) {
            cancelAugmentedAutofillLocked();
            this.mInlineSessionController.setInlineFillUiLocked(InlineFillUi.emptyUi(this.mCurrentViewId));
        }
    }

    @GuardedBy({"mLock"})
    private void cancelAugmentedAutofillLocked() {
        RemoteAugmentedAutofillService remoteAugmentedAutofillServiceLocked = this.mService.getRemoteAugmentedAutofillServiceLocked();
        if (remoteAugmentedAutofillServiceLocked == null) {
            Slog.w(TAG, "cancelAugmentedAutofillLocked(): no service for user");
            return;
        }
        if (Helper.sVerbose) {
            Slog.v(TAG, "cancelAugmentedAutofillLocked() on " + this.mCurrentViewId);
        }
        remoteAugmentedAutofillServiceLocked.onDestroyAutofillWindowsRequest();
    }

    @GuardedBy({"mLock"})
    private void processResponseLocked(FillResponse fillResponse, Bundle bundle, int i) {
        this.mUi.hideAll(this);
        if ((fillResponse.getFlags() & 4) == 0) {
            Slog.d(TAG, "Service did not request to wait for delayed fill response.");
            unregisterDelayedFillBroadcastLocked();
        }
        int requestId = fillResponse.getRequestId();
        if (Helper.sVerbose) {
            Slog.v(TAG, "processResponseLocked(): mCurrentViewId=" + this.mCurrentViewId + ",flags=" + i + ", reqId=" + requestId + ", resp=" + fillResponse + ",newClientState=" + bundle);
        }
        if (this.mResponses == null) {
            this.mResponses = new SparseArray<>(2);
        }
        this.mResponses.put(requestId, fillResponse);
        if (bundle == null) {
            bundle = fillResponse.getClientState();
        }
        this.mClientState = bundle;
        List<Dataset> datasets = fillResponse.getDatasets();
        this.mPresentationStatsEventLogger.maybeSetAvailableCount(datasets, this.mCurrentViewId);
        this.mFillResponseEventLogger.maybeSetDatasetsCountAfterPotentialPccFiltering(datasets);
        setViewStatesLocked(fillResponse, 2, false);
        updateFillDialogTriggerIdsLocked();
        updateTrackedIdsLocked();
        if (this.mSessionExt.useOplusAutofillService(this.mClientState, fillResponse)) {
            List datasets2 = fillResponse.getDatasets();
            if (datasets2 != null) {
                autoFill(fillResponse.getRequestId(), 0, (Dataset) datasets2.get(0), false, 0);
                return;
            }
            return;
        }
        AutofillId autofillId = this.mCurrentViewId;
        if (autofillId == null) {
            return;
        }
        this.mViewStates.get(autofillId).maybeCallOnFillReady(i);
    }

    @GuardedBy({"mLock"})
    private void setViewStatesLocked(FillResponse fillResponse, int i, boolean z) {
        List datasets = fillResponse.getDatasets();
        if (datasets != null && !datasets.isEmpty()) {
            for (int i2 = 0; i2 < datasets.size(); i2++) {
                Dataset dataset = (Dataset) datasets.get(i2);
                if (dataset == null) {
                    Slog.w(TAG, "Ignoring null dataset on " + datasets);
                } else {
                    setViewStatesLocked(fillResponse, dataset, i, z);
                }
            }
        } else if (fillResponse.getAuthentication() != null) {
            for (AutofillId autofillId : fillResponse.getAuthenticationIds()) {
                ViewState createOrUpdateViewStateLocked = createOrUpdateViewStateLocked(autofillId, i, null);
                if (!z) {
                    createOrUpdateViewStateLocked.setResponse(fillResponse);
                } else {
                    createOrUpdateViewStateLocked.setResponse(null);
                }
            }
        }
        SaveInfo saveInfo = fillResponse.getSaveInfo();
        if (saveInfo != null) {
            AutofillId[] requiredIds = saveInfo.getRequiredIds();
            if (requiredIds != null) {
                for (AutofillId autofillId2 : requiredIds) {
                    createOrUpdateViewStateLocked(autofillId2, i, null);
                }
            }
            AutofillId[] optionalIds = saveInfo.getOptionalIds();
            if (optionalIds != null) {
                for (AutofillId autofillId3 : optionalIds) {
                    createOrUpdateViewStateLocked(autofillId3, i, null);
                }
            }
        }
        AutofillId[] authenticationIds = fillResponse.getAuthenticationIds();
        if (authenticationIds != null) {
            for (AutofillId autofillId4 : authenticationIds) {
                createOrUpdateViewStateLocked(autofillId4, i, null);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void setViewStatesLocked(FillResponse fillResponse, Dataset dataset, int i, boolean z) {
        ArrayList fieldIds = dataset.getFieldIds();
        ArrayList fieldValues = dataset.getFieldValues();
        for (int i2 = 0; i2 < fieldIds.size(); i2++) {
            ViewState createOrUpdateViewStateLocked = createOrUpdateViewStateLocked((AutofillId) fieldIds.get(i2), i, (AutofillValue) fieldValues.get(i2));
            String id = dataset.getId();
            if (id != null) {
                createOrUpdateViewStateLocked.setDatasetId(id);
            }
            if (z) {
                createOrUpdateViewStateLocked.setResponse(null);
            } else if (fillResponse != null) {
                createOrUpdateViewStateLocked.setResponse(fillResponse);
            }
        }
    }

    @GuardedBy({"mLock"})
    private ViewState createOrUpdateViewStateLocked(AutofillId autofillId, int i, AutofillValue autofillValue) {
        ViewState viewState = this.mViewStates.get(autofillId);
        if (viewState != null) {
            viewState.setState(i);
        } else {
            viewState = new ViewState(autofillId, this, i);
            if (Helper.sVerbose) {
                Slog.v(TAG, "Adding autofillable view with id " + autofillId + " and state " + i);
            }
            viewState.setCurrentValue(findValueLocked(autofillId));
            this.mViewStates.put(autofillId, viewState);
        }
        if ((i & 4) != 0) {
            viewState.setAutofilledValue(autofillValue);
        }
        return viewState;
    }

    public void autoFill(int i, int i2, Dataset dataset, boolean z, int i3) {
        if (Helper.sDebug) {
            Slog.d(TAG, "autoFill(): requestId=" + i + "; datasetIdx=" + i2 + "; dataset=" + dataset);
        }
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#autoFill() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mPresentationStatsEventLogger.maybeSetSelectedDatasetId(i2);
            this.mPresentationStatsEventLogger.maybeSetSelectedDatasetPickReason(dataset.getEligibleReason());
            if (dataset.getAuthentication() == null) {
                if (z) {
                    this.mService.logDatasetSelected(dataset.getId(), this.id, this.mClientState, i3);
                }
                AutofillId autofillId = this.mCurrentViewId;
                if (autofillId != null) {
                    this.mInlineSessionController.hideInlineSuggestionsUiLocked(autofillId);
                }
                autoFillApp(dataset);
                return;
            }
            this.mService.logDatasetAuthenticationSelected(dataset.getId(), this.id, this.mClientState, i3);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationType(1);
            setViewStatesLocked(null, dataset, 64, false);
            Intent createAuthFillInIntentLocked = createAuthFillInIntentLocked(i, this.mClientState);
            if (createAuthFillInIntentLocked == null) {
                forceRemoveFromServiceLocked();
            } else {
                startAuthentication(AutofillManager.makeAuthenticationId(i, i2), dataset.getAuthentication(), createAuthFillInIntentLocked, false);
            }
        }
    }

    @GuardedBy({"mLock"})
    private Intent createAuthFillInIntentLocked(int i, Bundle bundle) {
        Intent intent = new Intent();
        FillContext fillContextByRequestIdLocked = getFillContextByRequestIdLocked(i);
        if (fillContextByRequestIdLocked == null) {
            wtf(null, "createAuthFillInIntentLocked(): no FillContext. requestId=%d; mContexts=%s", Integer.valueOf(i), this.mContexts);
            return null;
        }
        Pair<Integer, InlineSuggestionsRequest> pair = this.mLastInlineSuggestionsRequest;
        if (pair != null && ((Integer) pair.first).intValue() == i) {
            intent.putExtra("android.view.autofill.extra.INLINE_SUGGESTIONS_REQUEST", (Parcelable) this.mLastInlineSuggestionsRequest.second);
        }
        intent.putExtra("android.view.autofill.extra.ASSIST_STRUCTURE", fillContextByRequestIdLocked.getStructure());
        intent.putExtra("android.view.autofill.extra.CLIENT_STATE", bundle);
        return intent;
    }

    public Consumer<InlineSuggestionsRequest> inlineSuggestionsRequestCacheDecorator(final Consumer<InlineSuggestionsRequest> consumer, final int i) {
        return new Consumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Session.this.lambda$inlineSuggestionsRequestCacheDecorator$1(consumer, i, (InlineSuggestionsRequest) obj);
            }
        };
    }

    public /* synthetic */ void lambda$inlineSuggestionsRequestCacheDecorator$1(Consumer consumer, int i, InlineSuggestionsRequest inlineSuggestionsRequest) {
        consumer.accept(inlineSuggestionsRequest);
        synchronized (this.mLock) {
            this.mLastInlineSuggestionsRequest = Pair.create(Integer.valueOf(i), inlineSuggestionsRequest);
        }
    }

    private int getDetectionPreferenceForLogging() {
        if (this.mService.isPccClassificationEnabled()) {
            return ((AutofillManagerService) this.mService.getMaster()).preferProviderOverPcc() ? 1 : 2;
        }
        return 0;
    }

    private void startNewEventForPresentationStatsEventLogger() {
        synchronized (this.mLock) {
            this.mPresentationStatsEventLogger.startNewEvent();
            this.mPresentationStatsEventLogger.maybeSetDetectionPreference(getDetectionPreferenceForLogging());
            this.mPresentationStatsEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        }
    }

    public void startAuthentication(int i, IntentSender intentSender, Intent intent, boolean z) {
        try {
            synchronized (this.mLock) {
                this.mClient.authenticate(this.id, i, intentSender, intent, z);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Error launching auth intent", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class SaveResult {
        private boolean mLogSaveShown;
        private boolean mRemoveSession;
        private int mSaveDialogNotShowReason;

        SaveResult(boolean z, boolean z2, int i) {
            this.mLogSaveShown = z;
            this.mRemoveSession = z2;
            this.mSaveDialogNotShowReason = i;
        }

        public boolean isLogSaveShown() {
            return this.mLogSaveShown;
        }

        public void setLogSaveShown(boolean z) {
            this.mLogSaveShown = z;
        }

        public boolean isRemoveSession() {
            return this.mRemoveSession;
        }

        public void setRemoveSession(boolean z) {
            this.mRemoveSession = z;
        }

        public int getNoSaveUiReason() {
            return this.mSaveDialogNotShowReason;
        }

        public void setSaveDialogNotShowReason(int i) {
            this.mSaveDialogNotShowReason = i;
        }

        public String toString() {
            return "SaveResult: [logSaveShown=" + this.mLogSaveShown + ", removeSession=" + this.mRemoveSession + ", saveDialogNotShowReason=" + this.mSaveDialogNotShowReason + "]";
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ClassificationState {
        private static final int STATE_INITIAL = 1;
        private static final int STATE_INVALIDATED = 5;
        private static final int STATE_PENDING_ASSIST_REQUEST = 2;
        private static final int STATE_PENDING_REQUEST = 3;
        private static final int STATE_RESPONSE = 4;

        @GuardedBy({"mLock"})
        private ArrayMap<AutofillId, Set<String>> mClassificationCombinedHintsMap;

        @GuardedBy({"mLock"})
        private ArrayMap<AutofillId, Set<String>> mClassificationGroupHintsMap;

        @GuardedBy({"mLock"})
        private ArrayMap<AutofillId, Set<String>> mClassificationHintsMap;

        @GuardedBy({"mLock"})
        private ArrayMap<String, Set<AutofillId>> mGroupHintsToAutofillIdMap;

        @GuardedBy({"mLock"})
        private ArrayMap<String, Set<AutofillId>> mHintsToAutofillIdMap;

        @GuardedBy({"mLock"})
        private FieldClassificationResponse mLastFieldClassificationResponse;

        @GuardedBy({"mLock"})
        private FieldClassificationRequest mPendingFieldClassificationRequest;

        @GuardedBy({"mLock"})
        private int mState;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        @interface ClassificationRequestState {
        }

        /* synthetic */ ClassificationState(ClassificationStateIA classificationStateIA) {
            this();
        }

        private ClassificationState() {
            this.mState = 1;
        }

        @GuardedBy({"mLock"})
        private String stateToString() {
            int i = this.mState;
            if (i == 1) {
                return "STATE_INITIAL";
            }
            if (i == 2) {
                return "STATE_PENDING_ASSIST_REQUEST";
            }
            if (i == 3) {
                return "STATE_PENDING_REQUEST";
            }
            if (i == 4) {
                return "STATE_RESPONSE";
            }
            if (i == 5) {
                return "STATE_INVALIDATED";
            }
            return "UNKNOWN_CLASSIFICATION_STATE_" + this.mState;
        }

        @GuardedBy({"mLock"})
        public boolean processResponse() {
            ArrayMap<AutofillId, Set<String>> arrayMap = this.mClassificationHintsMap;
            if (arrayMap != null && !arrayMap.isEmpty()) {
                return true;
            }
            FieldClassificationResponse fieldClassificationResponse = this.mLastFieldClassificationResponse;
            if (fieldClassificationResponse == null) {
                return false;
            }
            this.mClassificationHintsMap = new ArrayMap<>();
            this.mClassificationGroupHintsMap = new ArrayMap<>();
            this.mHintsToAutofillIdMap = new ArrayMap<>();
            this.mGroupHintsToAutofillIdMap = new ArrayMap<>();
            this.mClassificationCombinedHintsMap = new ArrayMap<>();
            for (android.service.assist.classification.FieldClassification fieldClassification : fieldClassificationResponse.getClassifications()) {
                AutofillId autofillId = fieldClassification.getAutofillId();
                Set<String> hints = fieldClassification.getHints();
                Set<String> groupHints = fieldClassification.getGroupHints();
                ArraySet arraySet = new ArraySet(hints);
                this.mClassificationHintsMap.put(autofillId, hints);
                if (groupHints != null) {
                    this.mClassificationGroupHintsMap.put(autofillId, groupHints);
                    arraySet.addAll(groupHints);
                }
                this.mClassificationCombinedHintsMap.put(autofillId, arraySet);
                processDetections(hints, autofillId, this.mHintsToAutofillIdMap);
                processDetections(groupHints, autofillId, this.mGroupHintsToAutofillIdMap);
            }
            return true;
        }

        @GuardedBy({"mLock"})
        private static void processDetections(Set<String> set, AutofillId autofillId, ArrayMap<String, Set<AutofillId>> arrayMap) {
            Set<AutofillId> arraySet;
            for (String str : set) {
                if (arrayMap.containsKey(str)) {
                    arraySet = arrayMap.get(str);
                } else {
                    arraySet = new ArraySet<>();
                }
                arraySet.add(autofillId);
                arrayMap.put(str, arraySet);
            }
        }

        @GuardedBy({"mLock"})
        private void invalidateState() {
            this.mState = 5;
        }

        @GuardedBy({"mLock"})
        private void updatePendingAssistData() {
            this.mState = 2;
        }

        @GuardedBy({"mLock"})
        public void updatePendingRequest() {
            this.mState = 3;
        }

        @GuardedBy({"mLock"})
        public void updateResponseReceived(FieldClassificationResponse fieldClassificationResponse) {
            this.mState = 4;
            this.mLastFieldClassificationResponse = fieldClassificationResponse;
            this.mPendingFieldClassificationRequest = null;
            processResponse();
        }

        @GuardedBy({"mLock"})
        public void onAssistStructureReceived(AssistStructure assistStructure) {
            this.mState = 3;
            this.mPendingFieldClassificationRequest = new FieldClassificationRequest(assistStructure);
        }

        @GuardedBy({"mLock"})
        public void onFieldClassificationRequestSent() {
            this.mState = 3;
            this.mPendingFieldClassificationRequest = null;
        }

        @GuardedBy({"mLock"})
        public boolean shouldTriggerRequest() {
            int i = this.mState;
            return i == 1 || i == 5;
        }

        @GuardedBy({"mLock"})
        public String toString() {
            return "ClassificationState: [state=" + stateToString() + ", mPendingFieldClassificationRequest=" + this.mPendingFieldClassificationRequest + ", mLastFieldClassificationResponse=" + this.mLastFieldClassificationResponse + ", mClassificationHintsMap=" + this.mClassificationHintsMap + ", mClassificationGroupHintsMap=" + this.mClassificationGroupHintsMap + ", mHintsToAutofillIdMap=" + this.mHintsToAutofillIdMap + ", mGroupHintsToAutofillIdMap=" + this.mGroupHintsToAutofillIdMap + "]";
        }
    }

    public String toString() {
        return "Session: [id=" + this.id + ", component=" + this.mComponentName + ", state=" + sessionStateAsString(this.mSessionState) + "]";
    }

    @GuardedBy({"mLock"})
    public void dumpLocked(String str, PrintWriter printWriter) {
        String str2 = str + "  ";
        printWriter.print(str);
        printWriter.print("id: ");
        printWriter.println(this.id);
        printWriter.print(str);
        printWriter.print("uid: ");
        printWriter.println(this.uid);
        printWriter.print(str);
        printWriter.print("taskId: ");
        printWriter.println(this.taskId);
        printWriter.print(str);
        printWriter.print("flags: ");
        printWriter.println(this.mFlags);
        printWriter.print(str);
        printWriter.print("displayId: ");
        printWriter.println(this.mContext.getDisplayId());
        printWriter.print(str);
        printWriter.print("state: ");
        printWriter.println(sessionStateAsString(this.mSessionState));
        printWriter.print(str);
        printWriter.print("mComponentName: ");
        printWriter.println(this.mComponentName);
        printWriter.print(str);
        printWriter.print("mActivityToken: ");
        printWriter.println(this.mActivityToken);
        printWriter.print(str);
        printWriter.print("mStartTime: ");
        printWriter.println(this.mStartTime);
        printWriter.print(str);
        printWriter.print("Time to show UI: ");
        long j = this.mUiShownTime;
        if (j == 0) {
            printWriter.println("N/A");
        } else {
            TimeUtils.formatDuration(j - this.mStartTime, printWriter);
            printWriter.println();
        }
        int size = this.mRequestLogs.size();
        printWriter.print(str);
        printWriter.print("mSessionLogs: ");
        printWriter.println(size);
        for (int i = 0; i < size; i++) {
            int keyAt = this.mRequestLogs.keyAt(i);
            LogMaker valueAt = this.mRequestLogs.valueAt(i);
            printWriter.print(str2);
            printWriter.print('#');
            printWriter.print(i);
            printWriter.print(": req=");
            printWriter.print(keyAt);
            printWriter.print(", log=");
            dumpRequestLog(printWriter, valueAt);
            printWriter.println();
        }
        printWriter.print(str);
        printWriter.print("mResponses: ");
        SparseArray<FillResponse> sparseArray = this.mResponses;
        if (sparseArray == null) {
            printWriter.println("null");
        } else {
            printWriter.println(sparseArray.size());
            for (int i2 = 0; i2 < this.mResponses.size(); i2++) {
                printWriter.print(str2);
                printWriter.print('#');
                printWriter.print(i2);
                printWriter.print(' ');
                printWriter.println(this.mResponses.valueAt(i2));
            }
        }
        printWriter.print(str);
        printWriter.print("mCurrentViewId: ");
        printWriter.println(this.mCurrentViewId);
        printWriter.print(str);
        printWriter.print("mDestroyed: ");
        printWriter.println(this.mDestroyed);
        printWriter.print(str);
        printWriter.print("mShowingSaveUi: ");
        printWriter.println(this.mSessionFlags.mShowingSaveUi);
        printWriter.print(str);
        printWriter.print("mPendingSaveUi: ");
        printWriter.println(this.mPendingSaveUi);
        int size2 = this.mViewStates.size();
        printWriter.print(str);
        printWriter.print("mViewStates size: ");
        printWriter.println(this.mViewStates.size());
        for (int i3 = 0; i3 < size2; i3++) {
            printWriter.print(str);
            printWriter.print("ViewState at #");
            printWriter.println(i3);
            this.mViewStates.valueAt(i3).dump(str2, printWriter);
        }
        printWriter.print(str);
        printWriter.print("mContexts: ");
        ArrayList<FillContext> arrayList = this.mContexts;
        if (arrayList != null) {
            int size3 = arrayList.size();
            for (int i4 = 0; i4 < size3; i4++) {
                FillContext fillContext = this.mContexts.get(i4);
                printWriter.print(str2);
                printWriter.print(fillContext);
                if (Helper.sVerbose) {
                    printWriter.println("AssistStructure dumped at logcat)");
                    fillContext.getStructure().dump(false);
                }
            }
        } else {
            printWriter.println("null");
        }
        printWriter.print(str);
        printWriter.print("mHasCallback: ");
        printWriter.println(this.mHasCallback);
        if (this.mClientState != null) {
            printWriter.print(str);
            printWriter.print("mClientState: ");
            printWriter.print(this.mClientState.getSize());
            printWriter.println(" bytes");
        }
        printWriter.print(str);
        printWriter.print("mCompatMode: ");
        printWriter.println(this.mCompatMode);
        printWriter.print(str);
        printWriter.print("mUrlBar: ");
        if (this.mUrlBar == null) {
            printWriter.println("N/A");
        } else {
            printWriter.print("id=");
            printWriter.print(this.mUrlBar.getAutofillId());
            printWriter.print(" domain=");
            printWriter.print(this.mUrlBar.getWebDomain());
            printWriter.print(" text=");
            Helper.printlnRedactedText(printWriter, this.mUrlBar.getText());
        }
        printWriter.print(str);
        printWriter.print("mSaveOnAllViewsInvisible: ");
        printWriter.println(this.mSaveOnAllViewsInvisible);
        printWriter.print(str);
        printWriter.print("mSelectedDatasetIds: ");
        printWriter.println(this.mSelectedDatasetIds);
        if (this.mSessionFlags.mAugmentedAutofillOnly) {
            printWriter.print(str);
            printWriter.println("For Augmented Autofill Only");
        }
        if (this.mSessionFlags.mFillDialogDisabled) {
            printWriter.print(str);
            printWriter.println("Fill Dialog disabled");
        }
        if (this.mLastFillDialogTriggerIds != null) {
            printWriter.print(str);
            printWriter.println("Last Fill Dialog trigger ids: ");
            printWriter.println(this.mSelectedDatasetIds);
        }
        if (this.mAugmentedAutofillDestroyer != null) {
            printWriter.print(str);
            printWriter.println("has mAugmentedAutofillDestroyer");
        }
        if (this.mAugmentedRequestsLogs != null) {
            printWriter.print(str);
            printWriter.print("number augmented requests: ");
            printWriter.println(this.mAugmentedRequestsLogs.size());
        }
        if (this.mAugmentedAutofillableIds != null) {
            printWriter.print(str);
            printWriter.print("mAugmentedAutofillableIds: ");
            printWriter.println(this.mAugmentedAutofillableIds);
        }
        RemoteFillService remoteFillService = this.mRemoteFillService;
        if (remoteFillService != null) {
            remoteFillService.dump(str, printWriter);
        }
    }

    private static void dumpRequestLog(PrintWriter printWriter, LogMaker logMaker) {
        printWriter.print("CAT=");
        printWriter.print(logMaker.getCategory());
        printWriter.print(", TYPE=");
        int type = logMaker.getType();
        if (type == 2) {
            printWriter.print("CLOSE");
        } else if (type == 10) {
            printWriter.print("SUCCESS");
        } else if (type == 11) {
            printWriter.print("FAILURE");
        } else {
            printWriter.print("UNSUPPORTED");
        }
        printWriter.print('(');
        printWriter.print(type);
        printWriter.print(')');
        printWriter.print(", PKG=");
        printWriter.print(logMaker.getPackageName());
        printWriter.print(", SERVICE=");
        printWriter.print(logMaker.getTaggedData(908));
        printWriter.print(", ORDINAL=");
        printWriter.print(logMaker.getTaggedData(1454));
        dumpNumericValue(printWriter, logMaker, "FLAGS", 1452);
        dumpNumericValue(printWriter, logMaker, "NUM_DATASETS", 909);
        dumpNumericValue(printWriter, logMaker, "UI_LATENCY", 1145);
        int numericValue = Helper.getNumericValue(logMaker, 1453);
        if (numericValue != 0) {
            printWriter.print(", AUTH_STATUS=");
            if (numericValue == 912) {
                printWriter.print("AUTHENTICATED");
            } else {
                switch (numericValue) {
                    case 1126:
                        printWriter.print("DATASET_AUTHENTICATED");
                        break;
                    case 1127:
                        printWriter.print("INVALID_DATASET_AUTHENTICATION");
                        break;
                    case 1128:
                        printWriter.print("INVALID_AUTHENTICATION");
                        break;
                    default:
                        printWriter.print("UNSUPPORTED");
                        break;
                }
            }
            printWriter.print('(');
            printWriter.print(numericValue);
            printWriter.print(')');
        }
        dumpNumericValue(printWriter, logMaker, "FC_IDS", 1271);
        dumpNumericValue(printWriter, logMaker, "COMPAT_MODE", 1414);
    }

    private static void dumpNumericValue(PrintWriter printWriter, LogMaker logMaker, String str, int i) {
        int numericValue = Helper.getNumericValue(logMaker, i);
        if (numericValue != 0) {
            printWriter.print(", ");
            printWriter.print(str);
            printWriter.print('=');
            printWriter.print(numericValue);
        }
    }

    void autoFillApp(Dataset dataset) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                Slog.w(TAG, "Call to Session#autoFillApp() rejected - session: " + this.id + " destroyed");
                return;
            }
            try {
                int size = dataset.getFieldIds().size();
                ArrayList arrayList = new ArrayList(size);
                ArrayList arrayList2 = new ArrayList(size);
                boolean z = size == 1 && ((AutofillId) dataset.getFieldIds().get(0)).equals(this.mCurrentViewId);
                boolean z2 = false;
                for (int i = 0; i < size; i++) {
                    if (dataset.getFieldValues().get(i) != null) {
                        AutofillId autofillId = (AutofillId) dataset.getFieldIds().get(i);
                        arrayList.add(autofillId);
                        arrayList2.add((AutofillValue) dataset.getFieldValues().get(i));
                        ViewState viewState = this.mViewStates.get(autofillId);
                        if (viewState != null && (viewState.getState() & 64) != 0) {
                            if (Helper.sVerbose) {
                                Slog.v(TAG, "autofillApp(): view " + autofillId + " waiting auth");
                            }
                            viewState.resetState(64);
                            z2 = true;
                        }
                    }
                }
                if (!arrayList.isEmpty()) {
                    if (z2) {
                        this.mUi.hideFillUi(this);
                    }
                    if (Helper.sDebug) {
                        Slog.d(TAG, "autoFillApp(): the buck is on the app: " + dataset);
                    }
                    this.mClient.autofill(this.id, arrayList, arrayList2, z);
                    if (dataset.getId() != null) {
                        if (this.mSelectedDatasetIds == null) {
                            this.mSelectedDatasetIds = new ArrayList<>();
                        }
                        this.mSelectedDatasetIds.add(dataset.getId());
                    }
                    setViewStatesLocked(null, dataset, 4, false);
                }
            } catch (RemoteException e) {
                Slog.w(TAG, "Error autofilling activity: " + e);
            }
        }
    }

    private AutoFillUI getUiForShowing() {
        AutoFillUI autoFillUI;
        synchronized (this.mLock) {
            this.mUi.setCallback(this);
            autoFillUI = this.mUi;
        }
        return autoFillUI;
    }

    @GuardedBy({"mLock"})
    private void logAllEvents(int i) {
        this.mSessionCommittedEventLogger.maybeSetCommitReason(i);
        this.mSessionCommittedEventLogger.maybeSetRequestCount(this.mRequestCount);
        this.mSessionCommittedEventLogger.maybeSetSessionDurationMillis(SystemClock.elapsedRealtime() - this.mStartTime);
        this.mFillRequestEventLogger.logAndEndEvent();
        this.mFillResponseEventLogger.logAndEndEvent();
        this.mPresentationStatsEventLogger.logAndEndEvent();
        this.mSaveEventLogger.logAndEndEvent();
        this.mSessionCommittedEventLogger.logAndEndEvent();
    }

    @GuardedBy({"mLock"})
    public RemoteFillService destroyLocked() {
        logAllEvents(5);
        if (this.mDestroyed) {
            return null;
        }
        clearPendingIntentLocked();
        unregisterDelayedFillBroadcastLocked();
        unlinkClientVultureLocked();
        this.mUi.destroyAll(this.mPendingSaveUi, this, true);
        this.mUi.clearCallback(this);
        AutofillId autofillId = this.mCurrentViewId;
        if (autofillId != null) {
            this.mInlineSessionController.destroyLocked(autofillId);
        }
        RemoteInlineSuggestionRenderService remoteInlineSuggestionRenderServiceLocked = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (remoteInlineSuggestionRenderServiceLocked != null) {
            remoteInlineSuggestionRenderServiceLocked.destroySuggestionViews(this.userId, this.id);
        }
        this.mDestroyed = true;
        int size = this.mRequestLogs.size();
        if (size > 0) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "destroyLocked(): logging " + size + " requests");
            }
            for (int i = 0; i < size; i++) {
                this.mMetricsLogger.write(this.mRequestLogs.valueAt(i));
            }
        }
        ArrayList<LogMaker> arrayList = this.mAugmentedRequestsLogs;
        int size2 = arrayList == null ? 0 : arrayList.size();
        if (size2 > 0) {
            if (Helper.sVerbose) {
                Slog.v(TAG, "destroyLocked(): logging " + size + " augmented requests");
            }
            for (int i2 = 0; i2 < size2; i2++) {
                this.mMetricsLogger.write(this.mAugmentedRequestsLogs.get(i2));
            }
        }
        LogMaker addTaggedData = newLogMaker(919).addTaggedData(1455, Integer.valueOf(size));
        if (size2 > 0) {
            addTaggedData.addTaggedData(1631, Integer.valueOf(size2));
        }
        if (this.mSessionFlags.mAugmentedAutofillOnly) {
            addTaggedData.addTaggedData(1720, 1);
        }
        this.mMetricsLogger.write(addTaggedData);
        return this.mRemoteFillService;
    }

    @GuardedBy({"mLock"})
    public void forceRemoveFromServiceLocked() {
        forceRemoveFromServiceLocked(0);
    }

    @GuardedBy({"mLock"})
    public void forceRemoveFromServiceIfForAugmentedOnlyLocked() {
        if (Helper.sVerbose) {
            Slog.v(TAG, "forceRemoveFromServiceIfForAugmentedOnlyLocked(" + this.id + "): " + this.mSessionFlags.mAugmentedAutofillOnly);
        }
        if (this.mSessionFlags.mAugmentedAutofillOnly) {
            forceRemoveFromServiceLocked();
        }
    }

    @GuardedBy({"mLock"})
    void forceRemoveFromServiceLocked(int i) {
        if (Helper.sVerbose) {
            Slog.v(TAG, "forceRemoveFromServiceLocked(): " + this.mPendingSaveUi);
        }
        boolean isSaveUiPendingLocked = isSaveUiPendingLocked();
        this.mPendingSaveUi = null;
        removeFromServiceLocked();
        this.mUi.destroyAll(this.mPendingSaveUi, this, false);
        if (!isSaveUiPendingLocked) {
            try {
                this.mClient.setSessionFinished(i, (List) null);
            } catch (RemoteException e) {
                Slog.e(TAG, "Error notifying client to finish session", e);
            }
        }
        destroyAugmentedAutofillWindowsLocked();
    }

    @GuardedBy({"mLock"})
    public void destroyAugmentedAutofillWindowsLocked() {
        Runnable runnable = this.mAugmentedAutofillDestroyer;
        if (runnable != null) {
            runnable.run();
            this.mAugmentedAutofillDestroyer = null;
        }
    }

    public void removeFromService() {
        synchronized (this.mLock) {
            removeFromServiceLocked();
        }
    }

    @GuardedBy({"mLock"})
    public void removeFromServiceLocked() {
        if (Helper.sVerbose) {
            Slog.v(TAG, "removeFromServiceLocked(" + this.id + "): " + this.mPendingSaveUi);
        }
        if (this.mDestroyed) {
            Slog.w(TAG, "Call to Session#removeFromServiceLocked() rejected - session: " + this.id + " destroyed");
            return;
        }
        if (isSaveUiPendingLocked()) {
            Slog.i(TAG, "removeFromServiceLocked() ignored, waiting for pending save ui");
            return;
        }
        RemoteFillService destroyLocked = destroyLocked();
        this.mService.removeSessionLocked(this.id);
        if (destroyLocked != null) {
            destroyLocked.destroy();
        }
        this.mSessionState = 3;
    }

    public void onPendingSaveUi(int i, IBinder iBinder) {
        getUiForShowing().onPendingSaveUi(i, iBinder);
    }

    @GuardedBy({"mLock"})
    public boolean isSaveUiPendingForTokenLocked(IBinder iBinder) {
        return isSaveUiPendingLocked() && iBinder.equals(this.mPendingSaveUi.getToken());
    }

    @GuardedBy({"mLock"})
    private boolean isSaveUiPendingLocked() {
        PendingUi pendingUi = this.mPendingSaveUi;
        return pendingUi != null && pendingUi.getState() == 2;
    }

    @GuardedBy({"mLock"})
    private int getLastResponseIndexLocked() {
        SparseArray<FillResponse> sparseArray = this.mResponses;
        int i = -1;
        if (sparseArray != null) {
            int size = sparseArray.size();
            int i2 = -1;
            for (int i3 = 0; i3 < size; i3++) {
                if (this.mResponses.keyAt(i3) > i2) {
                    i2 = this.mResponses.keyAt(i3);
                    i = i3;
                }
            }
        }
        return i;
    }

    private LogMaker newLogMaker(int i) {
        return newLogMaker(i, this.mService.getServicePackageName());
    }

    private LogMaker newLogMaker(int i, String str) {
        return Helper.newLogMaker(i, this.mComponentName, str, this.id, this.mCompatMode);
    }

    private void writeLog(int i) {
        this.mMetricsLogger.write(newLogMaker(i));
    }

    @GuardedBy({"mLock"})
    private void logAuthenticationStatusLocked(int i, int i2) {
        addTaggedDataToRequestLogLocked(i, 1453, Integer.valueOf(i2));
    }

    @GuardedBy({"mLock"})
    private void addTaggedDataToRequestLogLocked(int i, int i2, Object obj) {
        LogMaker logMaker = this.mRequestLogs.get(i);
        if (logMaker == null) {
            Slog.w(TAG, "addTaggedDataToRequestLogLocked(tag=" + i2 + "): no log for id " + i);
            return;
        }
        logMaker.addTaggedData(i2, obj);
    }

    @GuardedBy({"mLock"})
    private void logAugmentedAutofillRequestLocked(int i, ComponentName componentName, AutofillId autofillId, boolean z, Boolean bool) {
        ((AutofillManagerService) this.mService.getMaster()).logRequestLocked("aug:id=" + this.id + " u=" + this.uid + " m=" + i + " a=" + ComponentName.flattenToShortString(this.mComponentName) + " f=" + autofillId + " s=" + componentName + " w=" + z + " i=" + bool);
    }

    public void wtf(Exception exc, String str, Object... objArr) {
        String format = String.format(str, objArr);
        synchronized (this.mLock) {
            this.mWtfHistory.log(format);
        }
        if (exc != null) {
            Slog.wtf(TAG, format, exc);
        } else {
            Slog.wtf(TAG, format);
        }
    }

    private static String actionAsString(int i) {
        if (i == 1) {
            return "START_SESSION";
        }
        if (i == 2) {
            return "VIEW_ENTERED";
        }
        if (i == 3) {
            return "VIEW_EXITED";
        }
        if (i == 4) {
            return "VALUE_CHANGED";
        }
        if (i == 5) {
            return "RESPONSE_EXPIRED";
        }
        return "UNKNOWN_" + i;
    }

    private static String sessionStateAsString(int i) {
        if (i == 0) {
            return "STATE_UNKNOWN";
        }
        if (i == 1) {
            return "STATE_ACTIVE";
        }
        if (i == 2) {
            return "STATE_FINISHED";
        }
        if (i == 3) {
            return "STATE_REMOVED";
        }
        return "UNKNOWN_SESSION_STATE_" + i;
    }

    private int getAutofillServiceUid() {
        ServiceInfo serviceInfo = this.mService.getServiceInfo();
        if (serviceInfo == null) {
            return -1;
        }
        return serviceInfo.applicationInfo.uid;
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onClassificationRequestSuccess(FieldClassificationResponse fieldClassificationResponse) {
        this.mClassificationState.updateResponseReceived(fieldClassificationResponse);
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onServiceDied(RemoteFieldClassificationService remoteFieldClassificationService) {
        Slog.w(TAG, "removing session because service died");
        synchronized (this.mLock) {
        }
    }
}
